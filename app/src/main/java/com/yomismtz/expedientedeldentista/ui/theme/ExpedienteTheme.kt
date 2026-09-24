package com.yomismtz.expedientedeldentista.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.TextSizeStyle
import com.yomismtz.expedientedeldentista.settings.ThemeMode

data class BirdPalette(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color,
    val onSurface: Color,
    val primaryContainer: Color,
    val secondaryContainer: Color,
    val outline: Color
)

val BirdPaletteChoices = BirdPaletteStyle.entries.toList()

fun paletteDisplayName(style: BirdPaletteStyle, lang: String = "es"): String = when (style) {
    BirdPaletteStyle.AGAPORNI -> "Agaporni"
    BirdPaletteStyle.TUCAN -> if (lang == "en") "Toucan" else "Tucán"
    BirdPaletteStyle.PAVO_REAL -> if (lang == "en") "Peacock" else "Pavo real"
    BirdPaletteStyle.FENIX -> if (lang == "en") "Phoenix" else "Fénix"
    BirdPaletteStyle.COLIBRI -> if (lang == "en") "Hummingbird" else "Colibrí"
    BirdPaletteStyle.PATO_MANDARIN -> if (lang == "en") "Mandarin duck" else "Pato mandarín"
    BirdPaletteStyle.NINFA -> if (lang == "en") "Cockatiel" else "Ninfa"
    BirdPaletteStyle.MARTIN_PESCADOR -> if (lang == "en") "Kingfisher" else "Martín pescador"
    BirdPaletteStyle.GUACAMAYA -> if (lang == "en") "Macaw" else "Guacamaya"
    BirdPaletteStyle.QUETZAL -> "Quetzal"
    BirdPaletteStyle.TRICHOGLOSSUS_MOLUCCANUS -> "Trichoglossus moluccanus"
    BirdPaletteStyle.CUERVO -> if (lang == "en") "Raven" else "Cuervo"
    BirdPaletteStyle.ABEJARUCO -> if (lang == "en") "Bee-eater" else "Abejaruco"
}

fun birdPalette(style: BirdPaletteStyle): BirdPalette = when (style) {
    BirdPaletteStyle.AGAPORNI -> BirdPalette(
        primary = Color(0xFF5D347F), onPrimary = Color.White,
        secondary = Color(0xFF66D6C7), tertiary = Color(0xFF2EB9B1),
        background = Color(0xFFF5EFFA), surface = Color(0xFFFFFCFF), onSurface = Color(0xFF321943),
        primaryContainer = Color(0xFFE7D7F6), secondaryContainer = Color(0xFFD5F4EF), outline = Color(0xFFB7B0BD)
    )
    BirdPaletteStyle.TUCAN -> BirdPalette(
        primary = Color(0xFF153C43), onPrimary = Color.White,
        secondary = Color(0xFFFFA321), tertiary = Color(0xFFFFD447),
        background = Color(0xFFFFF8E9), surface = Color(0xFFFFFCF5), onSurface = Color(0xFF172B30),
        primaryContainer = Color(0xFFCDE9E6), secondaryContainer = Color(0xFFFFE2B5), outline = Color(0xFF819598)
    )
    BirdPaletteStyle.PAVO_REAL -> BirdPalette(
        primary = Color(0xFF1457A6), onPrimary = Color.White,
        secondary = Color(0xFF0A8F78), tertiary = Color(0xFFD2A72C),
        background = Color(0xFFF0F8FB), surface = Color(0xFFFCFEFF), onSurface = Color(0xFF102A3A),
        primaryContainer = Color(0xFFD6E9FF), secondaryContainer = Color(0xFFD5F3EA), outline = Color(0xFF8AA0AC)
    )
    BirdPaletteStyle.FENIX -> BirdPalette(
        primary = Color(0xFFA92A34), onPrimary = Color.White,
        secondary = Color(0xFFE76122), tertiary = Color(0xFFF4B73D),
        background = Color(0xFFFFF4EC), surface = Color(0xFFFFFCF8), onSurface = Color(0xFF3D1F20),
        primaryContainer = Color(0xFFFFD8D9), secondaryContainer = Color(0xFFFFE0C9), outline = Color(0xFFAF8F82)
    )
    BirdPaletteStyle.COLIBRI -> BirdPalette(
        primary = Color(0xFF7B3AA7), onPrimary = Color.White,
        secondary = Color(0xFF00A89A), tertiary = Color(0xFFE34D9A),
        background = Color(0xFFF7F2FB), surface = Color(0xFFFFFCFF), onSurface = Color(0xFF30203A),
        primaryContainer = Color(0xFFE9D9F8), secondaryContainer = Color(0xFFD1F4EE), outline = Color(0xFFAA9DB2)
    )
    BirdPaletteStyle.PATO_MANDARIN -> BirdPalette(
        primary = Color(0xFF7A3F31), onPrimary = Color.White,
        secondary = Color(0xFFE17A2D), tertiary = Color(0xFF2D6B8D),
        background = Color(0xFFFFF7EF), surface = Color(0xFFFFFCF8), onSurface = Color(0xFF34241E),
        primaryContainer = Color(0xFFF2D9CC), secondaryContainer = Color(0xFFFFDFC4), outline = Color(0xFFA69185)
    )
    BirdPaletteStyle.NINFA -> BirdPalette(
        primary = Color(0xFF6E7179), onPrimary = Color.White,
        secondary = Color(0xFFF2C84B), tertiary = Color(0xFFF28B35),
        background = Color(0xFFFAF9F3), surface = Color(0xFFFFFFFF), onSurface = Color(0xFF2D2F33),
        primaryContainer = Color(0xFFE5E6E8), secondaryContainer = Color(0xFFFFF1B8), outline = Color(0xFFA7A8AC)
    )
    BirdPaletteStyle.MARTIN_PESCADOR -> BirdPalette(
        primary = Color(0xFF1465A8), onPrimary = Color.White,
        secondary = Color(0xFF19A7B8), tertiary = Color(0xFFF28B39),
        background = Color(0xFFEFF8FC), surface = Color(0xFFFCFEFF), onSurface = Color(0xFF173047),
        primaryContainer = Color(0xFFD5EAFE), secondaryContainer = Color(0xFFD6F3F5), outline = Color(0xFF8AA8B5)
    )
    BirdPaletteStyle.GUACAMAYA -> BirdPalette(
        primary = Color(0xFFB3262E), onPrimary = Color.White,
        secondary = Color(0xFFF4C430), tertiary = Color(0xFF2468B4),
        background = Color(0xFFFFF7F2), surface = Color(0xFFFFFCF9), onSurface = Color(0xFF382122),
        primaryContainer = Color(0xFFFFD9DC), secondaryContainer = Color(0xFFFFEDAF), outline = Color(0xFFAA8D85)
    )
    BirdPaletteStyle.QUETZAL -> BirdPalette(
        primary = Color(0xFF087A62), onPrimary = Color.White,
        secondary = Color(0xFF13A6A0), tertiary = Color(0xFFC73C5A),
        background = Color(0xFFF0FAF6), surface = Color(0xFFFCFFFD), onSurface = Color(0xFF17362F),
        primaryContainer = Color(0xFFD2F1E6), secondaryContainer = Color(0xFFD4F5F2), outline = Color(0xFF87A39B)
    )
    BirdPaletteStyle.TRICHOGLOSSUS_MOLUCCANUS -> BirdPalette(
        primary = Color(0xFF2C63C7), onPrimary = Color.White,
        secondary = Color(0xFF49A82D), tertiary = Color(0xFFF28A18),
        background = Color(0xFFF4F9FF), surface = Color(0xFFFEFFFF), onSurface = Color(0xFF172B4C),
        primaryContainer = Color(0xFFD9E7FF), secondaryContainer = Color(0xFFE3F4D8), outline = Color(0xFF8EA1B5)
    )
    BirdPaletteStyle.CUERVO -> BirdPalette(
        primary = Color(0xFF24212E), onPrimary = Color.White,
        secondary = Color(0xFF5E4B8B), tertiary = Color(0xFF8E78B7),
        background = Color(0xFFF4F2F7), surface = Color(0xFFFCFBFE), onSurface = Color(0xFF201C29),
        primaryContainer = Color(0xFFE4DFEB), secondaryContainer = Color(0xFFE8E1F3), outline = Color(0xFF8F8898)
    )
    BirdPaletteStyle.ABEJARUCO -> BirdPalette(
        primary = Color(0xFF0A7E55), onPrimary = Color.White,
        secondary = Color(0xFFF1B929), tertiary = Color(0xFF34BFC2),
        background = Color(0xFFF5FBF7), surface = Color(0xFFFEFFFD), onSurface = Color(0xFF173429),
        primaryContainer = Color(0xFFD5F0E3), secondaryContainer = Color(0xFFFFEDB2), outline = Color(0xFF8AA497)
    )
}

fun paletteSwatches(style: BirdPaletteStyle): List<Color> {
    val p = birdPalette(style)
    return listOf(p.primary, p.secondary, p.tertiary, p.primaryContainer)
}

fun fontDisplayName(style: FontStyle, lang: String = "es"): String = when (style) {
    FontStyle.MODERN -> if (lang == "en") "Modern" else "Moderna"
    FontStyle.ROUNDED -> if (lang == "en") "Rounded" else "Redondeada"
    FontStyle.ACADEMIC -> if (lang == "en") "Academic" else "Académica"
    FontStyle.ACCESSIBLE -> if (lang == "en") "Accessible" else "Accesible"
}

fun textSizeDisplayName(style: TextSizeStyle, lang: String = "es"): String = when (style) {
    TextSizeStyle.SMALL -> if (lang == "en") "Small" else "Pequeña"
    TextSizeStyle.NORMAL -> if (lang == "en") "Standard" else "Normal"
    TextSizeStyle.LARGE -> if (lang == "en") "Large" else "Grande"
    TextSizeStyle.EXTRA_LARGE -> if (lang == "en") "Extra large" else "Muy grande"
}

private fun familyFor(style: FontStyle): FontFamily = when (style) {
    FontStyle.MODERN -> FontFamily.SansSerif
    FontStyle.ROUNDED -> FontFamily.Cursive
    FontStyle.ACADEMIC -> FontFamily.Serif
    FontStyle.ACCESSIBLE -> FontFamily.Monospace
}

private fun scaledSp(base: Float, textSizeStyle: TextSizeStyle) = (base * textSizeStyle.multiplier).sp

private fun contrastOn(color: Color): Color =
    if (color.luminance() > 0.48f) Color(0xFF181319) else Color.White

private fun lightScheme(p: BirdPalette) = lightColorScheme(
    primary = p.primary,
    onPrimary = contrastOn(p.primary),
    secondary = p.secondary,
    onSecondary = contrastOn(p.secondary),
    tertiary = p.tertiary,
    onTertiary = contrastOn(p.tertiary),
    background = p.background,
    onBackground = p.onSurface,
    surface = p.surface,
    onSurface = p.onSurface,
    surfaceVariant = lerp(p.primaryContainer, p.surface, 0.42f),
    onSurfaceVariant = p.onSurface.copy(alpha = 0.84f),
    primaryContainer = p.primaryContainer,
    onPrimaryContainer = p.onSurface,
    secondaryContainer = p.secondaryContainer,
    onSecondaryContainer = p.onSurface,
    tertiaryContainer = lerp(p.tertiary, p.surface, 0.80f),
    onTertiaryContainer = p.onSurface,
    outline = p.outline,
    outlineVariant = p.outline.copy(alpha = 0.52f)
)

private fun darkScheme(p: BirdPalette) = darkColorScheme(
    primary = lerp(p.primary, Color.White, 0.34f),
    onPrimary = Color(0xFF120F13),
    secondary = lerp(p.secondary, Color.White, 0.22f),
    onSecondary = Color(0xFF120F13),
    tertiary = lerp(p.tertiary, Color.White, 0.22f),
    onTertiary = Color(0xFF120F13),
    background = lerp(p.onSurface, Color.Black, 0.76f),
    onBackground = Color(0xFFF3EEF4),
    surface = lerp(p.onSurface, Color.Black, 0.66f),
    onSurface = Color(0xFFF3EEF4),
    surfaceVariant = lerp(p.primary, Color.Black, 0.69f),
    onSurfaceVariant = Color(0xFFD8D0DA),
    primaryContainer = lerp(p.primary, Color.Black, 0.44f),
    onPrimaryContainer = Color(0xFFF7F1F8),
    secondaryContainer = lerp(p.secondary, Color.Black, 0.56f),
    onSecondaryContainer = Color(0xFFF7F1F8),
    tertiaryContainer = lerp(p.tertiary, Color.Black, 0.56f),
    onTertiaryContainer = Color(0xFFF7F1F8),
    outline = lerp(p.outline, Color.White, 0.28f),
    outlineVariant = lerp(p.outline, Color.Black, 0.34f)
)

private val ExpedienteShapesV49 = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun ExpedienteTheme(
    paletteStyle: BirdPaletteStyle,
    fontStyle: FontStyle,
    textSizeStyle: TextSizeStyle = TextSizeStyle.NORMAL,
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val p = birdPalette(paletteStyle)
    val family = familyFor(fontStyle)
    val colors = if (themeMode == ThemeMode.DARK) darkScheme(p) else lightScheme(p)

    val typography = Typography(
        displaySmall = TextStyle(
            fontFamily = family,
            fontSize = scaledSp(32f, textSizeStyle),
            fontWeight = FontWeight.SemiBold,
            lineHeight = scaledSp(38f, textSizeStyle)
        ),
        headlineMedium = TextStyle(
            fontFamily = family,
            fontSize = scaledSp(26f, textSizeStyle),
            fontWeight = FontWeight.Bold,
            lineHeight = scaledSp(32f, textSizeStyle)
        ),
        titleLarge = TextStyle(
            fontFamily = family,
            fontSize = scaledSp(22f, textSizeStyle),
            fontWeight = FontWeight.SemiBold,
            lineHeight = scaledSp(28f, textSizeStyle)
        ),
        titleMedium = TextStyle(
            fontFamily = family,
            fontSize = scaledSp(18f, textSizeStyle),
            fontWeight = FontWeight.SemiBold,
            lineHeight = scaledSp(24f, textSizeStyle)
        ),
        bodyLarge = TextStyle(
            fontFamily = family,
            fontSize = scaledSp(17f, textSizeStyle),
            lineHeight = scaledSp(24f, textSizeStyle)
        ),
        bodyMedium = TextStyle(
            fontFamily = family,
            fontSize = scaledSp(15f, textSizeStyle),
            lineHeight = scaledSp(21f, textSizeStyle)
        ),
        bodySmall = TextStyle(
            fontFamily = family,
            fontSize = scaledSp(13f, textSizeStyle),
            lineHeight = scaledSp(18f, textSizeStyle)
        ),
        labelLarge = TextStyle(
            fontFamily = family,
            fontSize = scaledSp(14f, textSizeStyle),
            fontWeight = FontWeight.SemiBold
        )
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = ExpedienteShapesV49,
        content = content
    )
}
