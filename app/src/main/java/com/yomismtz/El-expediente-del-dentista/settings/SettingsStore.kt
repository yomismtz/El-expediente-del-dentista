package com.yomismtz.expedientedeldentista.settings

import android.content.Context

enum class ClinicianTitle { DOCTOR, DOCTORA }
enum class SkinTone { LIGHT, PEACH, TAN, BROWN, DEEP }
enum class HairStyle { SHORT, WAVY, CURLY, LONG, BOB, BUN }
enum class EyeShape { ROUND, ALMOND, SOFT }
enum class EyeColor { BROWN, HAZEL, GREEN, BLUE, GRAY }
enum class MouthStyle { NATURAL, SMILE, FULL }
enum class LipColor { NATURAL, ROSE, CORAL, RED, WINE }
enum class ScrubColor { TURQUOISE, BLUE, NAVY, PURPLE, LILAC, PINK, BLACK, WHITE, MINT, WINE }
enum class MascotStyle { TOUCAN, LOVEBIRD_GREEN, LOVEBIRD_PASTEL, PUG, POMERANIAN, CLOWNFISH, SHARK, RAVEN, MANDARIN_DUCK, FLAMINGO, MACAW, PERSIAN_CAT, WHITE_YELLOW_CAT, ELEPHANT, TURTLE, WHITE_RABBIT, IGUANA }

/** Compatibilidad con pantallas antiguas. */
enum class PaletteStyle { WOOD, CLINICAL_GREEN, DENTAL_BLUE, WINE, SAGE, MONO }

/** Paletas nuevas de la interfaz YSM. */
enum class BirdPaletteStyle {
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
    TRICHOGLOSSUS_MOLUCCANUS,
    CUERVO,
    ABEJARUCO
}

enum class FontStyle { MODERN, ROUNDED, ACADEMIC, ACCESSIBLE }

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
    val languageTag: String = "es",
    val skinTone: SkinTone = SkinTone.PEACH,
    val hairStyle: HairStyle = HairStyle.WAVY,
    val eyeShape: EyeShape = EyeShape.ALMOND,
    val eyeColor: EyeColor = EyeColor.BROWN,
    val mouthStyle: MouthStyle = MouthStyle.SMILE,
    val lipColor: LipColor = LipColor.NATURAL,
    val scrubColor: ScrubColor = ScrubColor.BLUE,
    val mascotStyle: MascotStyle = MascotStyle.LOVEBIRD_GREEN
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
        languageTag = prefs.getString(KEY_LANGUAGE, "es") ?: "es",
        skinTone = enumValueOrDefault(prefs.getString(KEY_SKIN, null), SkinTone.PEACH),
        hairStyle = enumValueOrDefault(prefs.getString(KEY_HAIR, null), HairStyle.WAVY),
        eyeShape = enumValueOrDefault(prefs.getString(KEY_EYE_SHAPE, null), EyeShape.ALMOND),
        eyeColor = enumValueOrDefault(prefs.getString(KEY_EYE_COLOR, null), EyeColor.BROWN),
        mouthStyle = enumValueOrDefault(prefs.getString(KEY_MOUTH, null), MouthStyle.SMILE),
        lipColor = enumValueOrDefault(prefs.getString(KEY_LIPS, null), LipColor.NATURAL),
        scrubColor = enumValueOrDefault(prefs.getString(KEY_SCRUB, null), ScrubColor.BLUE),
        mascotStyle = enumValueOrDefault(prefs.getString(KEY_MASCOT, null), MascotStyle.LOVEBIRD_GREEN)
    )

    fun save(value: AppPreferences) {
        prefs.edit()
            .putBoolean(KEY_ONBOARDING, value.onboardingComplete)
            .putString(KEY_TITLE, value.clinicianTitle.name)
            .putString(KEY_PALETTE, value.paletteStyle.name)
            .putString(KEY_BIRD_PALETTE, value.birdPaletteStyle.name)
            .putString(KEY_FONT, value.fontStyle.name)
            .putString(KEY_TEXT_SIZE, value.textSizeStyle.name)
            .putString(KEY_LANGUAGE, value.languageTag)
            .putString(KEY_SKIN, value.skinTone.name)
            .putString(KEY_HAIR, value.hairStyle.name)
            .putString(KEY_EYE_SHAPE, value.eyeShape.name)
            .putString(KEY_EYE_COLOR, value.eyeColor.name)
            .putString(KEY_MOUTH, value.mouthStyle.name)
            .putString(KEY_LIPS, value.lipColor.name)
            .putString(KEY_SCRUB, value.scrubColor.name)
            .putString(KEY_MASCOT, value.mascotStyle.name)
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
        const val KEY_LANGUAGE = "language_tag"
        const val KEY_SKIN = "avatar_skin"
        const val KEY_HAIR = "avatar_hair"
        const val KEY_EYE_SHAPE = "avatar_eye_shape"
        const val KEY_EYE_COLOR = "avatar_eye_color"
        const val KEY_MOUTH = "avatar_mouth"
        const val KEY_LIPS = "avatar_lips"
        const val KEY_SCRUB = "avatar_scrub"
        const val KEY_MASCOT = "avatar_mascot"
    }
}
