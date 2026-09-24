package com.yomismtz.expedientedeldentista.ui

import androidx.compose.runtime.Composable

/**
 * Compatibility entry point kept for older navigation references.
 *
 * Clinical photographs are no longer loaded from remote URLs at runtime.
 * The active V46 atlas uses APK-bundled, build-verified resources with attribution.
 */
@Composable
fun OcclusionPhotoAtlasV42Screen(lang: String, onBack: () -> Unit) {
    OcclusionPhotoAtlasV46Screen(lang, onBack)
}
