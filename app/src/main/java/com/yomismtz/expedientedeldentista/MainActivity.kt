package com.yomismtz.expedientedeldentista

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.SettingsStore
import com.yomismtz.expedientedeldentista.ui.AppRootV7
import com.yomismtz.expedientedeldentista.ui.OnboardingV15Screen
import com.yomismtz.expedientedeldentista.ui.theme.ExpedienteTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = SettingsStore(this)

        setContent {
            var preferences by remember { mutableStateOf(store.load()) }
            var session by remember { mutableStateOf(EducationalSession()) }

            val savePreferences: (com.yomismtz.expedientedeldentista.settings.AppPreferences) -> Unit = { updated ->
                preferences = updated
                store.save(updated)
            }

            ExpedienteTheme(
                paletteStyle = preferences.paletteStyle,
                fontStyle = preferences.fontStyle
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (!preferences.onboardingComplete) {
                        OnboardingV15Screen(
                            preferences = preferences,
                            onPreferencesChanged = savePreferences,
                            onContinue = { completed ->
                                savePreferences(completed.copy(onboardingComplete = true))
                            }
                        )
                    } else {
                        AppRootV7(
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
