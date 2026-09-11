package com.yomismtz.expedientedeldentista.settings

import android.content.Context

enum class ClinicianTitle { DOCTOR, DOCTORA }
enum class PaletteStyle { WOOD, CLINICAL_GREEN, DENTAL_BLUE, WINE, SAGE, MONO }
enum class FontStyle { MODERN, ROUNDED, ACADEMIC, ACCESSIBLE }

data class AppPreferences(
    val onboardingComplete: Boolean = false,
    val clinicianTitle: ClinicianTitle = ClinicianTitle.DOCTORA,
    val paletteStyle: PaletteStyle = PaletteStyle.WOOD,
    val fontStyle: FontStyle = FontStyle.MODERN,
    val languageTag: String = "es"
)

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("expediente_settings", Context.MODE_PRIVATE)

    fun load(): AppPreferences = AppPreferences(
        onboardingComplete = prefs.getBoolean(KEY_ONBOARDING, false),
        clinicianTitle = enumValueOrDefault(prefs.getString(KEY_TITLE, null), ClinicianTitle.DOCTORA),
        paletteStyle = enumValueOrDefault(prefs.getString(KEY_PALETTE, null), PaletteStyle.WOOD),
        fontStyle = enumValueOrDefault(prefs.getString(KEY_FONT, null), FontStyle.MODERN),
        languageTag = prefs.getString(KEY_LANGUAGE, "es") ?: "es"
    )

    fun save(value: AppPreferences) {
        prefs.edit()
            .putBoolean(KEY_ONBOARDING, value.onboardingComplete)
            .putString(KEY_TITLE, value.clinicianTitle.name)
            .putString(KEY_PALETTE, value.paletteStyle.name)
            .putString(KEY_FONT, value.fontStyle.name)
            .putString(KEY_LANGUAGE, value.languageTag)
            .apply()
    }

    private inline fun <reified T : Enum<T>> enumValueOrDefault(raw: String?, fallback: T): T {
        return raw?.let { runCatching { enumValueOf<T>(it) }.getOrNull() } ?: fallback
    }

    private companion object {
        const val KEY_ONBOARDING = "onboarding_complete"
        const val KEY_TITLE = "clinician_title"
        const val KEY_PALETTE = "palette_style"
        const val KEY_FONT = "font_style"
        const val KEY_LANGUAGE = "language_tag"
    }
}
