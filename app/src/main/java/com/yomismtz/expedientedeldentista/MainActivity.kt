package com.yomismtz.expedientedeldentista

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
            // Deliberately start with no active record after a process restart.
            // Records are durable; the user explicitly chooses which one to load.
            var activeRecordId by remember { mutableStateOf<String?>(null) }
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
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
