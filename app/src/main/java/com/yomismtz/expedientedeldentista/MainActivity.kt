package com.yomismtz.expedientedeldentista

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
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

        val runtimePrefs = getSharedPreferences(RUNTIME_PREFS, MODE_PRIVATE)
        val isPreview = BuildConfig.APPLICATION_ID.endsWith(".preview")
        val seenVersion = runtimePrefs.safeInt(PREVIEW_SEEN_VERSION, -1)
        val isFirstLaunchOfPreviewVersion = isPreview && seenVersion != BuildConfig.VERSION_CODE

        // A Preview version always starts from the known-safe onboarding path on its first
        // successful launch. The version is marked as seen only after Compose has completed
        // its first composition; a crash before that point cannot poison the next launch.
        val initialPreferences = if (isFirstLaunchOfPreviewVersion) {
            loadedPreferences.copy(onboardingComplete = false)
        } else {
            loadedPreferences
        }

        setContent {
            var preferences by remember { mutableStateOf(initialPreferences) }
            var session by remember { mutableStateOf(EducationalSession()) }

            val savePreferences: (AppPreferences) -> Unit = { updated ->
                preferences = updated
                runCatching { store.save(updated) }
            }

            LaunchedEffect(isPreview, isFirstLaunchOfPreviewVersion) {
                if (isPreview && isFirstLaunchOfPreviewVersion) {
                    runCatching {
                        runtimePrefs.edit()
                            .putInt(PREVIEW_SEEN_VERSION, BuildConfig.VERSION_CODE)
                            .commit()
                    }
                }
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

    /**
     * SharedPreferences throws ClassCastException when an old build stored the same key with
     * another type. Treat that as stale/corrupt test state instead of crashing MainActivity.
     */
    private fun SharedPreferences.safeInt(key: String, fallback: Int): Int {
        return runCatching { getInt(key, fallback) }
            .getOrElse {
                runCatching { edit().remove(key).commit() }
                fallback
            }
    }

    private companion object {
        const val RUNTIME_PREFS = "expediente_runtime"
        const val PREVIEW_SEEN_VERSION = "preview_seen_version"
    }
}
