package com.yomismtz.expedientedeldentista.ui

import android.content.Context
import android.util.Base64
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.io.*

private const val NO_RECORD = "__no_record__"

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
            val bytes = ByteArrayOutputStream().also { out ->
                ObjectOutputStream(out).use { it.writeObject(value) }
            }.toByteArray()
            prefs.edit().putString("$recordId::$key", Base64.encodeToString(bytes, Base64.NO_WRAP)).apply()
        }
    }
    fun deleteRecord(recordId: String) {
        val prefix="$recordId::"
        val e=prefs.edit()
        prefs.all.keys.filter { it.startsWith(prefix) }.forEach { e.remove(it) }
        e.apply()
    }
}

val LocalActiveRecordId = compositionLocalOf<String?> { null }
val LocalRecordFieldStore = staticCompositionLocalOf<RecordFieldStore?> { null }

@Composable
fun <T> rememberRecordState(key: String, initial: T): MutableState<T> {
    val id=LocalActiveRecordId.current
    val store=LocalRecordFieldStore.current
    @Suppress("UNCHECKED_CAST")
    val state=remember(id,key) { mutableStateOf((store?.load(id,key) as? T) ?: initial) }
    LaunchedEffect(id,key,state.value) { store?.save(id,key,state.value) }
    return state
}
