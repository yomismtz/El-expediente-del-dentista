package com.yomismtz.expedientedeldentista

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.net.Uri
import android.util.Base64
import java.io.File
import java.security.MessageDigest
import org.json.JSONArray
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yomismtz.expedientedeldentista.clinical.ClinicalRecordStore
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.SavedRecord
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.SettingsStore
import com.yomismtz.expedientedeldentista.ui.AppRootV19
import com.yomismtz.expedientedeldentista.ui.LocalActiveRecordId
import com.yomismtz.expedientedeldentista.ui.LocalRecordFieldStore
import com.yomismtz.expedientedeldentista.ui.LocalRecordFieldChanged
import com.yomismtz.expedientedeldentista.ui.RecordFieldStore
import com.yomismtz.expedientedeldentista.ui.theme.ExpedienteTheme
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = SettingsStore(this)
        val recordStore = ClinicalRecordStore(this)
        val fieldStore = RecordFieldStore(this)

        runCatching {
            val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 32)
            tone.startTone(ToneGenerator.TONE_PROP_BEEP2, 90)
            window.decorView.postDelayed({
                tone.startTone(ToneGenerator.TONE_PROP_BEEP, 75)
                window.decorView.postDelayed({ tone.release() }, 120)
            }, 105)
        }

        setContent {
            var preferences by remember { mutableStateOf(store.load()) }
            var savedRecords by remember { mutableStateOf(recordStore.loadAll()) }
            // Keep the active record identity across configuration changes such as rotation.
            var activeRecordId by rememberSaveable { mutableStateOf<String?>(null) }
            var activeRecord by remember(activeRecordId) {
                mutableStateOf(activeRecordId?.let { id -> recordStore.loadAll().firstOrNull { it.id == id } })
            }
            var session by remember(activeRecordId) { mutableStateOf(activeRecord?.session ?: EducationalSession()) }

            val savePreferences: (AppPreferences) -> Unit = { updated ->
                preferences = updated
                store.save(updated)
            }

            ExpedienteTheme(
                paletteStyle = preferences.birdPaletteStyle,
                fontStyle = preferences.fontStyle,
                textSizeStyle = preferences.textSizeStyle,
                cardShapeStyle = preferences.cardShapeStyle
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    Box(Modifier.fillMaxSize()) {
                        CompositionLocalProvider(
                            LocalActiveRecordId provides activeRecord?.id,
                            LocalRecordFieldStore provides fieldStore,
                            LocalRecordFieldChanged provides { id ->
                                recordStore.touch(id)
                                savedRecords = recordStore.loadAll()
                            }
                        ) {
                            AppRootV19(
                                preferences = preferences,
                                onPreferencesChanged = savePreferences,
                                onLanguageChanged = { tag ->
                                    savePreferences(preferences.copy(languageTag = tag))
                                },
                                session = session,
                                onSessionChanged = { updated ->
                                    session = updated
                                    activeRecord?.let { current ->
                                        val saved = current.copy(session = updated)
                                        recordStore.save(saved)
                                        activeRecord = recordStore.loadAll()
                                            .firstOrNull { it.id == current.id } ?: saved
                                        savedRecords = recordStore.loadAll()
                                    }
                                },
                                savedRecords = savedRecords,
                                activeRecordId = activeRecord?.id,
                                onNewRecord = { profile ->
                                    val created = recordStore.create(EducationalSession(profile = profile))
                                    activeRecord = created
                                    activeRecordId = created.id
                                    session = created.session
                                    savedRecords = recordStore.loadAll()
                                },
                                onLoadRecord = { record ->
                                    activeRecord = record
                                    activeRecordId = record.id
                                    session = record.session
                                },
                                onDeleteRecord = { id ->
                                    recordStore.delete(id)
                                    fieldStore.deleteRecord(id)
                                    if (activeRecord?.id == id) {
                                        activeRecord = null
                                        activeRecordId = null
                                        session = EducationalSession()
                                    }
                                    savedRecords = recordStore.loadAll()
                                },
                                onExportRecord = { id ->
                                    recordStore.exportRecordJson(id)?.also { root ->
                                        root.put("fields", fieldStore.exportRecord(id))
                                        root.put("attachments", exportAttachments(id,fieldStore))
                                    }?.toString(2)
                                },
                                onImportRecord = { raw ->
                                    runCatching {
                                        val root=JSONObject(raw)
                                        val imported=recordStore.importRecordJson(root)
                                        root.optJSONObject("fields")?.let { fieldStore.importRecord(imported.id,it) }
                                        restoreAttachments(root.optJSONArray("attachments"),imported.id,fieldStore)
                                        savedRecords=recordStore.loadAll()
                                        imported
                                    }.getOrNull()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun exportAttachments(recordId:String,fieldStore:RecordFieldStore):JSONArray {
        val out=JSONArray()
        val photos=fieldStore.load(recordId,"photo.uris") as? Map<*,*> ?: return out
        photos.forEach { (slot,value) ->
            val uri=value as? String ?: return@forEach
            val bytes=runCatching { contentResolver.openInputStream(Uri.parse(uri))?.use { it.readBytes() } }.getOrNull() ?: return@forEach
            val sha=MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
            out.put(JSONObject().put("kind","clinicalPhoto").put("slot",slot.toString()).put("mime",contentResolver.getType(Uri.parse(uri))?:"image/jpeg").put("sha256",sha).put("data",Base64.encodeToString(bytes,Base64.NO_WRAP)))
        }
        return out
    }

    private fun restoreAttachments(items:JSONArray?,recordId:String,fieldStore:RecordFieldStore) {
        if(items==null) return
        val restored=HashMap<String,String>()
        val dir=File(filesDir,"record_attachments/$recordId").apply { mkdirs() }
        for(i in 0 until items.length()) {
            val item=items.optJSONObject(i) ?: continue
            if(item.optString("kind")!="clinicalPhoto") continue
            val bytes=runCatching { Base64.decode(item.getString("data"),Base64.DEFAULT) }.getOrNull() ?: continue
            val expected=item.optString("sha256")
            val actual=MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
            if(expected.isNotBlank() && expected!=actual) continue
            val originalSlot=item.optString("slot")
            val safeSlot=originalSlot.replace(Regex("[^A-Za-z0-9_-]"),"_").ifBlank { "photo_$i" }
            val ext=if(item.optString("mime").contains("png")) "png" else "jpg"
            val file=File(dir,"$safeSlot.$ext")
            file.outputStream().use { it.write(bytes) }
            restored[originalSlot]=Uri.fromFile(file).toString()
        }
        if(restored.isNotEmpty()) fieldStore.replacePhotoUris(recordId,restored)
    }

}
