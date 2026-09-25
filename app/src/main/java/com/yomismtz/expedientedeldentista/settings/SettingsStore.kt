package com.yomismtz.expedientedeldentista.settings

import android.content.Context

enum class ClinicianTitle { DOCTOR, DOCTORA }

/** Compatibilidad con pantallas antiguas. */
enum class PaletteStyle { WOOD, CLINICAL_GREEN, DENTAL_BLUE, WINE, SAGE, MONO }

/** Paletas nuevas de la interfaz YSM. */
enum class BirdPaletteStyle {
    AGAPORNI, TUCAN, NINFA, BUHO, CUERVO,
    GUACAMAYA, PUG, POMERANIA, GATO, CONEJO,
    ELEFANTE, CABALLO_CAFE, CABALLO_PINTO, IGUANA, ARANA,
    TORTUGA, PEZ_PAYASO, DELFIN, TIBURON, AJOLOTE,
    PINGUINO, PATO_MANDARIN, HAMSTER, FENIX, ZORRO,
    PAVO_REAL, COLIBRI, MARTIN_PESCADOR, QUETZAL, DRAGON,
    BALLENA_AZUL, RANA_VERDE, MARIPOSA_LAVANDA, FLAMENCO_ROSA, CABALLITO_TURQUESA,
    CANGREJO_CORAL, MURCIELAGO_NOCHE, PANDA_MONO, ABEJA_CONTRASTE
}

enum class CardShapeStyle { SOFT, ROUNDED, SQUARE }

enum class FontStyle { MODERN, ROUNDED, ACADEMIC, ACCESSIBLE, CLASSIC, HANDWRITTEN, COMPACT }

enum class TextSizeStyle(val multiplier: Float) {
    SMALL(0.90f),
    NORMAL(1.00f),
    LARGE(1.15f),
    EXTRA_LARGE(1.30f)
}

data class AppPreferences(
    val onboardingComplete: Boolean = false,
    val clinicianTitle: ClinicianTitle = ClinicianTitle.DOCTORA,
    val paletteStyle: PaletteStyle = PaletteStyle.WOOD,
    val birdPaletteStyle: BirdPaletteStyle = BirdPaletteStyle.AGAPORNI,
    val fontStyle: FontStyle = FontStyle.MODERN,
    val textSizeStyle: TextSizeStyle = TextSizeStyle.NORMAL,
    val cardShapeStyle: CardShapeStyle = CardShapeStyle.ROUNDED,
    val languageTag: String = "es"
)

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("expediente_settings", Context.MODE_PRIVATE)

    fun load(): AppPreferences = AppPreferences(
        onboardingComplete = prefs.getBoolean(KEY_ONBOARDING, false),
        clinicianTitle = enumValueOrDefault(prefs.getString(KEY_TITLE, null), ClinicianTitle.DOCTORA),
        paletteStyle = enumValueOrDefault(prefs.getString(KEY_PALETTE, null), PaletteStyle.WOOD),
        birdPaletteStyle = enumValueOrDefault(prefs.getString(KEY_BIRD_PALETTE, null), BirdPaletteStyle.AGAPORNI),
        fontStyle = enumValueOrDefault(prefs.getString(KEY_FONT, null), FontStyle.MODERN),
        textSizeStyle = enumValueOrDefault(prefs.getString(KEY_TEXT_SIZE, null), TextSizeStyle.NORMAL),
        cardShapeStyle = enumValueOrDefault(prefs.getString(KEY_CARD_SHAPE, null), CardShapeStyle.ROUNDED),
        languageTag = prefs.getString(KEY_LANGUAGE, "es") ?: "es"
    )

    fun save(value: AppPreferences) {
        prefs.edit()
            .putBoolean(KEY_ONBOARDING, value.onboardingComplete)
            .putString(KEY_TITLE, value.clinicianTitle.name)
            .putString(KEY_PALETTE, value.paletteStyle.name)
            .putString(KEY_BIRD_PALETTE, value.birdPaletteStyle.name)
            .putString(KEY_FONT, value.fontStyle.name)
            .putString(KEY_TEXT_SIZE, value.textSizeStyle.name)
            .putString(KEY_CARD_SHAPE, value.cardShapeStyle.name)
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
        const val KEY_BIRD_PALETTE = "bird_palette_style"
        const val KEY_FONT = "font_style"
        const val KEY_TEXT_SIZE = "text_size_style"
        const val KEY_CARD_SHAPE = "card_shape_style"
        const val KEY_LANGUAGE = "language_tag"
    }
}
