package com.yomismtz.expedientedeldentista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.yomismtz.expedientedeldentista.ui.OnboardingV15Screen
import com.yomismtz.expedientedeldentista.ui.theme.ExpedienteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val store = SettingsStore(applicationContext)
        val loadedPreferences = runCatching { store.load() }
            .getOrElse { AppPreferences() }

        // Preview builds are frequently installed over earlier test builds.  Start a new
        // Preview version from a known-safe onboarding state once, so an old navigation
        // preference cannot skip directly into a stale screen graph.
        val runtimePrefs = getSharedPreferences("expediente_runtime", MODE_PRIVATE)
        val isPreview = BuildConfig.APPLICATION_ID.endsWith(".preview")
        val seenVersion = runtimePrefs.getInt("preview_seen_version", -1)
        val isFirstLaunchOfPreviewVersion = isPreview && seenVersion != BuildConfig.VERSION_CODE
        val initialPreferences = if (isFirstLaunchOfPreviewVersion) {
            loadedPreferences.copy(onboardingComplete = false)
        } else {
            loadedPreferences
        }
        if (isPreview) {
            runtimePrefs.edit().putInt("preview_seen_version", BuildConfig.VERSION_CODE).apply()
        }

        setContent {
            var preferences by remember { mutableStateOf(initialPreferences) }
            var session by remember { mutableStateOf(EducationalSession()) }

            val savePreferences: (AppPreferences) -> Unit = { updated ->
                preferences = updated
                runCatching { store.save(updated) }
            }

            ExpedienteTheme(
                paletteStyle = preferences.birdPaletteStyle,
                fontStyle = preferences.fontStyle,
                textSizeStyle = preferences.textSizeStyle
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(Modifier.fillMaxSize()) {
                        if (!preferences.onboardingComplete) {
                            OnboardingV15Screen(
                                preferences = preferences,
                                onPreferencesChanged = savePreferences,
                                onContinue = { completed ->
                                    savePreferences(completed.copy(onboardingComplete = true))
                                }
                            )
                        } else {
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
}
