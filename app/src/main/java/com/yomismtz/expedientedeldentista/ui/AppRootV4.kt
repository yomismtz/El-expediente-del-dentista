package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences

/**
 * v0.8 shell: preserves the complete v0.7 folder and adds the new native,
 * fully-offline interactive oral-mucosa module without turning the app into
 * a patient-record system.
 */
@Composable
fun AppRootV4(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var mucosaOpen by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        AppRootV3(
            preferences = preferences,
            onPreferencesChanged = onPreferencesChanged,
            onLanguageChanged = onLanguageChanged,
            session = session,
            onSessionChanged = onSessionChanged
        )

        if (preferences.onboardingComplete && !mucosaOpen) {
            Button(
                onClick = { mucosaOpen = true },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .safeDrawingPadding()
                    .padding(10.dp)
            ) {
                Text(if (preferences.languageTag == "en") "👄 Interactive mucosa" else "👄 Mucosas interactivas")
            }
        }

        if (mucosaOpen) {
            Surface(Modifier.fillMaxSize(), tonalElevation = 8.dp) {
                MucosaInteractiveV2Screen(
                    lang = preferences.languageTag,
                    onBack = { mucosaOpen = false }
                )
            }
        }
    }

    BackHandler(enabled = mucosaOpen) { mucosaOpen = false }
}
