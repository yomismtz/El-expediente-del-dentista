package com.yomismtz.expedientedeldentista.ui

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.security.KeyStore
import java.util.HashMap
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import org.json.JSONObject

/**
 * Returns an encrypted value only when decrypting it reproduces the exact original bytes.
 * Keeping this check separate makes migration fail closed if encryption or verification fails.
 */
internal fun verifiedEncryptedFieldValue(
    plaintext: ByteArray,
    encrypted: String?,
    decrypt: (String) -> ByteArray?
): String? {
    if (encrypted.isNullOrBlank()) return null
    val recovered = runCatching { decrypt(encrypted) }.getOrNull() ?: return null
    return encrypted.takeIf { recovered.contentEquals(plaintext) }
}

class RecordFieldStore(context: Context) {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences("clinical_record_fields_v1", Context.MODE_PRIVATE)
    private val keyAlias = "expediente_dentista_fields_v1"

    private fun legacyStorageKey(recordId: String, key: String) = "$recordId::$key"
    private fun secureStorageKey(recordId: String, key: String) = "$SECURE_STORAGE_PREFIX$recordId::$key"
    private fun secureRecordPrefix(recordId: String) = "$SECURE_STORAGE_PREFIX$recordId::"

    @Synchronized
    fun load(recordId: String?, key: String): Any? {
        if (recordId.isNullOrBlank()) return null

        val secureKey = secureStorageKey(recordId, key)
        val encrypted = prefs.getString(secureKey, null)
        if (encrypted != null) {
            val plaintext = decrypt(encrypted) ?: return null
            return deserialize(plaintext)
        }

        // Legacy values were Java-serialized and Base64-encoded, but not encrypted.
        // Keep them untouched unless a verified encrypted copy has been committed.
        val legacyKey = legacyStorageKey(recordId, key)
        val legacy = prefs.getString(legacyKey, null) ?: return null
        val plaintext = runCatching { Base64.decode(legacy, Base64.DEFAULT) }.getOrNull() ?: return null
        val value = deserialize(plaintext) ?: return null

        val migrated = verifiedEncryptedFieldValue(plaintext, encrypt(plaintext), ::decrypt)
        if (migrated != null) {
            // One editor update replaces the old value only after round-trip verification.
            prefs.edit().putString(secureKey, migrated).remove(legacyKey).apply()
        }
        return value
    }

    @Synchronized
    fun save(recordId: String?, key: String, value: Any?): Boolean {
        if (recordId.isNullOrBlank() || value !is Serializable) return false
        val plaintext = serialize(value) ?: return false
        val legacyKey = legacyStorageKey(recordId, key)
        val secureKey = secureStorageKey(recordId, key)

        val existingSecure = prefs.getString(secureKey, null)
        if (existingSecure != null) {
            val existingPlaintext = decrypt(existingSecure) ?: return false
            // Do not replace a payload that cannot be decoded; that may be the only
            // remaining copy of a user's previously saved clinical information.
            if (deserialize(existingPlaintext) == null) return false
            if (existingPlaintext.contentEquals(plaintext)) {
                prefs.edit().remove(legacyKey).apply()
                return false
            }
        } else {
            val existingLegacy = prefs.getString(legacyKey, null)
            if (existingLegacy != null) {
                val legacyBytes = runCatching { Base64.decode(existingLegacy, Base64.DEFAULT) }.getOrNull()
                    ?: return false
                if (deserialize(legacyBytes) == null) return false
            }
        }

        val encrypted = verifiedEncryptedFieldValue(plaintext, encrypt(plaintext), ::decrypt) ?: return false
        // apply() updates memory immediately and persists asynchronously without blocking
        // the Compose thread for every field edit.
        prefs.edit().putString(secureKey, encrypted).remove(legacyKey).apply()
        return true
    }

    /**
     * True when at least one saved encrypted field exists but cannot be decrypted or decoded.
     * The UI uses this to warn rather than silently treating protected data as empty.
     */
    @Synchronized
    fun hasUnreadableSecureData(recordId: String?): Boolean {
        if (recordId.isNullOrBlank()) return false
        val prefix = secureRecordPrefix(recordId)
        return prefs.all.any { (storageKey, value) ->
            if (!storageKey.startsWith(prefix) || value !is String) {
                false
            } else {
                val plaintext = decrypt(value)
                plaintext == null || deserialize(plaintext) == null
            }
        }
    }

    /**
     * Backups intentionally retain the existing portable format: serialized payloads are
     * Base64-encoded in the user-selected JSON file. They are decrypted only for export.
     * A field that cannot be decrypted aborts the export instead of producing a partial backup.
     */
    @Synchronized
    fun exportRecord(recordId: String): JSONObject {
        val output = JSONObject()
        val legacyPrefix = "$recordId::"
        prefs.all.forEach { (storageKey, value) ->
            if (storageKey.startsWith(legacyPrefix) && value is String) {
                output.put(storageKey.removePrefix(legacyPrefix), value)
            }
        }

        val securePrefix = secureRecordPrefix(recordId)
        prefs.all.forEach { (storageKey, value) ->
            if (storageKey.startsWith(securePrefix)) {
                val fieldName = storageKey.removePrefix(securePrefix)
                val encoded = value as? String
                    ?: throw IllegalStateException("No se pudo preparar el respaldo: campo almacenado inválido.")
                val plaintext = decrypt(encoded)
                    ?: throw IllegalStateException("No se pudo descifrar un campo de la práctica. Conserva los datos del dispositivo y revisa la clave de seguridad.")
                if (deserialize(plaintext) == null) {
                    throw IllegalStateException("No se pudo leer un campo de la práctica. El respaldo se canceló para evitar perder información.")
                }
                output.put(fieldName, Base64.encodeToString(plaintext, Base64.NO_WRAP))
            }
        }
        return output
    }

    /**
     * Imports the established backup representation, encrypting every field before writing.
     * All fields are validated and encrypted before a single preferences commit is attempted.
     */
    @Synchronized
    fun importRecord(recordId: String, values: JSONObject) {
        require(recordId.isNotBlank()) { "Identificador de práctica inválido." }
        val prepared = mutableListOf<Pair<String, String>>()
        val keys = values.keys()
        while (keys.hasNext()) {
            val fieldName = keys.next()
            val raw = values.optString(fieldName, "")
            val plaintext = runCatching { Base64.decode(raw, Base64.DEFAULT) }.getOrNull()
                ?: throw IllegalArgumentException("El respaldo contiene un campo ilegible.")
            if (deserialize(plaintext) == null) {
                throw IllegalArgumentException("El respaldo contiene un campo dañado; no se importaron los campos.")
            }
            val encrypted = verifiedEncryptedFieldValue(plaintext, encrypt(plaintext), ::decrypt)
                ?: throw IllegalStateException("No fue posible cifrar los campos del respaldo. No se importaron para evitar guardarlos sin protección.")
            prepared += fieldName to encrypted
        }

        val editor = prefs.edit()
        prepared.forEach { (fieldName, encrypted) ->
            editor.putString(secureStorageKey(recordId, fieldName), encrypted)
            editor.remove(legacyStorageKey(recordId, fieldName))
        }
        if (!editor.commit()) {
            throw IllegalStateException("No se pudieron guardar los campos importados en el dispositivo.")
        }
    }

    @Synchronized
    fun deleteRecord(recordId: String) {
        val legacyPrefix = "$recordId::"
        val securePrefix = secureRecordPrefix(recordId)
        val editor = prefs.edit()
        prefs.all.keys
            .filter { it.startsWith(legacyPrefix) || it.startsWith(securePrefix) }
            .forEach { editor.remove(it) }
        editor.apply()

        // Photos selected/captured in the updated photography sheet and older restored
        // backup attachments are both private app files owned by this practice.
        File(File(appContext.filesDir, "record_photos"), recordId).deleteRecursively()
        File(File(appContext.filesDir, "record_attachments"), recordId).deleteRecursively()
    }

    private fun serialize(value: Serializable): ByteArray? = runCatching {
        ByteArrayOutputStream().use { bytes ->
            ObjectOutputStream(bytes).use { it.writeObject(value) }
            bytes.toByteArray()
        }
    }.getOrNull()

    private fun deserialize(bytes: ByteArray): Any? = runCatching {
        ObjectInputStream(ByteArrayInputStream(bytes)).use { it.readObject() }
    }.getOrNull()

    private fun existingSecretKey(): SecretKey? = runCatching {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        keyStore.getKey(keyAlias, null) as? SecretKey
    }.getOrNull()

    private fun secretKeyForEncryption(): SecretKey? = runCatching {
        existingSecretKey() ?: run {
            val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            generator.init(
                KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .setUserAuthenticationRequired(false)
                    .build()
            )
            generator.generateKey()
        }
    }.getOrNull()

    private fun encrypt(plaintext: ByteArray): String? = runCatching {
        val key = secretKeyForEncryption() ?: return@runCatching null
        val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(plaintext)
        ENCRYPTED_VALUE_PREFIX + Base64.encodeToString(cipher.iv + encrypted, Base64.NO_WRAP)
    }.getOrNull()

    private fun decrypt(encoded: String): ByteArray? = runCatching {
        require(encoded.startsWith(ENCRYPTED_VALUE_PREFIX)) { "Unsupported encrypted field format." }
        val payload = Base64.decode(encoded.removePrefix(ENCRYPTED_VALUE_PREFIX), Base64.DEFAULT)
        require(payload.size > GCM_IV_LENGTH)
        val key = existingSecretKey() ?: return@runCatching null
        val iv = payload.copyOfRange(0, GCM_IV_LENGTH)
        val ciphertext = payload.copyOfRange(GCM_IV_LENGTH, payload.size)
        val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv))
        cipher.doFinal(ciphertext)
    }.getOrNull()

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_IV_LENGTH = 12
        const val GCM_TAG_LENGTH_BITS = 128
        const val SECURE_STORAGE_PREFIX = "secure_v1::"
        const val ENCRYPTED_VALUE_PREFIX = "enc:v1:"
    }
}

val LocalActiveRecordId = compositionLocalOf<String?> { null }
val LocalRecordFieldStore = staticCompositionLocalOf<RecordFieldStore?> { null }
val LocalRecordFieldChanged = staticCompositionLocalOf<(String) -> Unit> { {} }

private fun compatibleRecordValue(value: Any?, initial: Any?): Boolean {
    if (value == null) return false
    if (initial == null) return true
    return when (initial) {
        is String -> value is String
        is Int -> value is Int
        is Long -> value is Long
        is Float -> value is Float
        is Double -> value is Double
        is Boolean -> value is Boolean
        is Set<*> -> value is Set<*>
        is List<*> -> value is List<*>
        is Map<*, *> -> value is Map<*, *>
        else -> initial::class.java.isInstance(value)
    }
}

@Composable
fun <T> rememberRecordState(key: String, initial: T): MutableState<T> {
    val id = LocalActiveRecordId.current
    val store = LocalRecordFieldStore.current
    val changed = rememberUpdatedState(LocalRecordFieldChanged.current)
    @Suppress("UNCHECKED_CAST")
    val state = remember(id, key) {
        val loaded = store?.load(id, key)
        mutableStateOf(if (compatibleRecordValue(loaded, initial)) loaded as T else initial)
    }
    LaunchedEffect(id, key, state.value) {
        val didChange = store?.save(id, key, state.value) == true
        if (didChange && !id.isNullOrBlank()) changed.value(id)
    }
    return state
}

@Composable
fun <K, V> rememberRecordStateMap(key: String): SnapshotStateMap<K, V> {
    val id = LocalActiveRecordId.current
    val store = LocalRecordFieldStore.current
    val changed = rememberUpdatedState(LocalRecordFieldChanged.current)
    @Suppress("UNCHECKED_CAST")
    val map = remember(id, key) {
        mutableStateMapOf<K, V>().also { target ->
            val loaded = store?.load(id, key)
            if (loaded is Map<*, *>) {
                runCatching { target.putAll(loaded as Map<K, V>) }
            }
        }
    }
    val snapshot = map.toMap()
    LaunchedEffect(id, key, snapshot) {
        val didChange = store?.save(id, key, HashMap(snapshot)) == true
        if (didChange && !id.isNullOrBlank()) changed.value(id)
    }
    return map
}
