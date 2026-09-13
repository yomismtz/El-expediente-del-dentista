package com.yomismtz.expedientedeldentista.settings

import android.content.Context

enum class ClinicianTitle { DOCTOR, DOCTORA }

enum class PaletteStyle {
    AGAPORNI,
    TUCAN,
    PAVO_REAL,
    FENIX,
    COLIBRI,
    PATO_MANDARIN,
    NINFA,
    MARTIN_PESCADOR,
    GUACAMAYA,
    QUETZAL,

    // Valores heredados para compatibilidad binaria con pantallas antiguas.
    // No se muestran en el onboarding nuevo y se migran visualmente a paletas de aves.
    WOOD,
    CLINICAL_GREEN,
    DENTAL_BLUE,
    WINE,
    SAGE,
    MONO
}

enum class FontStyle { MODERN, ROUNDED, ACADEMIC, ACCESSIBLE }

data class AppPreferences(
    val onboardingComplete: Boolean = false,
    val clinicianTitle: ClinicianTitle = ClinicianTitle.DOCTORA,
    val paletteStyle: PaletteStyle = PaletteStyle.AGAPORNI,
    val fontStyle: FontStyle = FontStyle.MODERN,
    val languageTag: String = "es"
)

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("expediente_settings", Context.MODE_PRIVATE)

    fun load(): AppPreferences {
        val storedPalette = enumValueOrDefault(prefs.getString(KEY_PALETTE, null), PaletteStyle.AGAPORNI)
        return AppPreferences(
            onboardingComplete = prefs.getBoolean(KEY_ONBOARDING, false),
            clinicianTitle = enumValueOrDefault(prefs.getString(KEY_TITLE, null), ClinicianTitle.DOCTORA),
            paletteStyle = migrateLegacyPalette(storedPalette),
            fontStyle = enumValueOrDefault(prefs.getString(KEY_FONT, null), FontStyle.MODERN),
            languageTag = prefs.getString(KEY_LANGUAGE, "es") ?: "es"
        )
    }

    fun save(value: AppPreferences) {
        prefs.edit()
            .putBoolean(KEY_ONBOARDING, value.onboardingComplete)
            .putString(KEY_TITLE, value.clinicianTitle.name)
            .putString(KEY_PALETTE, value.paletteStyle.name)
            .putString(KEY_FONT, value.fontStyle.name)
            .putString(KEY_LANGUAGE, value.languageTag)
            .apply()
    }

    private fun migrateLegacyPalette(style: PaletteStyle): PaletteStyle = when (style) {
        PaletteStyle.WOOD -> PaletteStyle.AGAPORNI
        PaletteStyle.CLINICAL_GREEN -> PaletteStyle.QUETZAL
        PaletteStyle.DENTAL_BLUE -> PaletteStyle.MARTIN_PESCADOR
        PaletteStyle.WINE -> PaletteStyle.FENIX
        PaletteStyle.SAGE -> PaletteStyle.QUETZAL
        PaletteStyle.MONO -> PaletteStyle.NINFA
        else -> style
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
