package com.yomismtz.expedientedeldentista.ui

import androidx.compose.runtime.Composable
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences

/**
 * Compatibility entry point kept so existing navigation calls now use the
 * corrected v0.19 responsive shell without duplicating the old layout.
 */
@Composable
fun AdaptiveBaseRootV17(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    AdaptiveBaseRootV19(
        preferences = preferences,
        onPreferencesChanged = onPreferencesChanged,
        onLanguageChanged = onLanguageChanged,
        session = session,
        onSessionChanged = onSessionChanged
    )
}
