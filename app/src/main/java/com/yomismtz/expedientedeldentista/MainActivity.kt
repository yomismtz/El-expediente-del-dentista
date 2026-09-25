package com.yomismtz.expedientedeldentista

import android.os.Bundle
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.SettingsStore
import com.yomismtz.expedientedeldentista.ui.AppRootV19
import com.yomismtz.expedientedeldentista.ui.theme.ExpedienteTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = SettingsStore(this)
        // Breve trino sintetizado: funciona 100% offline y no requiere archivo de audio ni permiso.
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
            var session by remember { mutableStateOf(EducationalSession()) }

            val savePreferences: (AppPreferences) -> Unit = { updated ->
                preferences = updated
                store.save(updated)
            }

            ExpedienteTheme(
                paletteStyle = preferences.birdPaletteStyle,
                fontStyle = preferences.fontStyle,
                textSizeStyle = preferences.textSizeStyle
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = androidx.compose.material3.MaterialTheme.colorScheme.background) {
                    Box(Modifier.fillMaxSize()) {
                        AppRootV19(
                            preferences = preferences,
                            onPreferencesChanged = savePreferences,
                            onLanguageChanged = { tag ->
                                savePreferences(preferences.copy(languageTag = tag))
                            },
                            session = session,
                            onSessionChanged = { session = it }
                        )
                    }
                }
            }
        }
    }
}
