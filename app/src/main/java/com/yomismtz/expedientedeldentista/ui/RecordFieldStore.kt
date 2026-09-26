package com.yomismtz.expedientedeldentista.ui

import android.content.Context
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
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.HashMap

class RecordFieldStore(context: Context) {
    private val prefs = context.getSharedPreferences("clinical_record_fields_v1", Context.MODE_PRIVATE)

    fun load(recordId: String?, key: String): Any? {
        if (recordId.isNullOrBlank()) return null
        val raw = prefs.getString("$recordId::$key", null) ?: return null
        return runCatching {
            ObjectInputStream(ByteArrayInputStream(Base64.decode(raw, Base64.NO_WRAP))).use { it.readObject() }
        }.getOrNull()
    }

    fun save(recordId: String?, key: String, value: Any?) {
        if (recordId.isNullOrBlank() || value !is Serializable) return
        runCatching {
            val out = ByteArrayOutputStream()
            ObjectOutputStream(out).use { it.writeObject(value) }
            prefs.edit().putString(
                "$recordId::$key",
                Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
            ).apply()
        }
    }

    fun deleteRecord(recordId: String) {
        val prefix = "$recordId::"
        val editor = prefs.edit()
        prefs.all.keys.filter { it.startsWith(prefix) }.forEach { editor.remove(it) }
        editor.apply()
    }
}

val LocalActiveRecordId = compositionLocalOf<String?> { null }
val LocalRecordFieldStore = staticCompositionLocalOf<RecordFieldStore?> { null }
val LocalRecordFieldChanged = staticCompositionLocalOf<(String) -> Unit> { {} }

@Composable
fun <T> rememberRecordState(key: String, initial: T): MutableState<T> {
    val id = LocalActiveRecordId.current
    val store = LocalRecordFieldStore.current
    val changed = rememberUpdatedState(LocalRecordFieldChanged.current)
    @Suppress("UNCHECKED_CAST")
    val state = remember(id, key) {
        mutableStateOf((store?.load(id, key) as? T) ?: initial)
    }
    LaunchedEffect(id, key, state.value) {
        store?.save(id, key, state.value)
        if (!id.isNullOrBlank()) changed.value(id)
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
            (store?.load(id, key) as? Map<K, V>)?.let(target::putAll)
        }
    }
    val snapshot = map.toMap()
    LaunchedEffect(id, key, snapshot) {
        store?.save(id, key, HashMap(snapshot))
        if (!id.isNullOrBlank()) changed.value(id)
    }
    return map
}
