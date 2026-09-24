package com.yomismtz.expedientedeldentista.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.TextSizeStyle

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
    BirdPaletteStyle.PERRO -> if (lang == "en") "Dog" else "Perro"
    BirdPaletteStyle.KOALA -> "Koala"
    BirdPaletteStyle.PUG -> "Pug"
    BirdPaletteStyle.GATO -> if (lang == "en") "Cat" else "Gato"
    BirdPaletteStyle.TORTUGA -> if (lang == "en") "Turtle" else "Tortuga"
    BirdPaletteStyle.HAMSTER -> if (lang == "en") "Hamster" else "Hámster"
    BirdPaletteStyle.DRAGON -> if (lang == "en") "Dragon" else "Dragón"
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
    BirdPaletteStyle.PERRO -> BirdPalette(Color(0xFF6D4C41),Color.White,Color(0xFFD7A86E),Color(0xFF5D8A66),Color(0xFFFFF8F2),Color.White,Color(0xFF35251F),Color(0xFFEEDFD5),Color(0xFFE6F0E7),Color(0xFFA58F85))
    BirdPaletteStyle.KOALA -> BirdPalette(Color(0xFF59636E),Color.White,Color(0xFF9DA7B1),Color(0xFF7FAE9B),Color(0xFFF5F7F8),Color.White,Color(0xFF273039),Color(0xFFE0E5E9),Color(0xFFDDEEE7),Color(0xFF9AA2A9))
    BirdPaletteStyle.PUG -> BirdPalette(Color(0xFF76513A),Color.White,Color(0xFFC99B6B),Color(0xFF30343B),Color(0xFFFFF8EF),Color.White,Color(0xFF382A22),Color(0xFFF0DDC7),Color(0xFFE1E2E5),Color(0xFFA48F7D))
    BirdPaletteStyle.GATO -> BirdPalette(Color(0xFF4D5664),Color.White,Color(0xFFD38FA8),Color(0xFF8A72B5),Color(0xFFF8F5FA),Color.White,Color(0xFF2D2933),Color(0xFFE4E6EB),Color(0xFFF2DFE8),Color(0xFF9D96A3))
    BirdPaletteStyle.TORTUGA -> BirdPalette(Color(0xFF47734D),Color.White,Color(0xFF8DAA62),Color(0xFFB88A54),Color(0xFFF5F8EE),Color.White,Color(0xFF293629),Color(0xFFDCE9D4),Color(0xFFECE4CF),Color(0xFF93A08A))
    BirdPaletteStyle.HAMSTER -> BirdPalette(Color(0xFFA66745),Color.White,Color(0xFFE1AD72),Color(0xFFD9878B),Color(0xFFFFF7F1),Color.White,Color(0xFF402B22),Color(0xFFF5DECB),Color(0xFFF5DFDF),Color(0xFFB19A8E))
    BirdPaletteStyle.DRAGON -> BirdPalette(Color(0xFF633A8C),Color.White,Color(0xFF9B7AC7),Color(0xFF54CDBF),Color(0xFFF7F0FC),Color.White,Color(0xFF32203F),Color(0xFFE7D6F4),Color(0xFFD4F4ED),Color(0xFFA596AF))
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

@Composable
fun ExpedienteTheme(
    paletteStyle: BirdPaletteStyle,
    fontStyle: FontStyle,
    textSizeStyle: TextSizeStyle = TextSizeStyle.NORMAL,
    content: @Composable () -> Unit
) {
    val p = birdPalette(paletteStyle)
    val family = familyFor(fontStyle)
    val colors = lightColorScheme(
        primary = p.primary,
        onPrimary = p.onPrimary,
        secondary = p.secondary,
        tertiary = p.tertiary,
        background = p.background,
        surface = p.surface,
        onSurface = p.onSurface,
        primaryContainer = p.primaryContainer,
        onPrimaryContainer = p.onSurface,
        secondaryContainer = p.secondaryContainer,
        onSecondaryContainer = p.onSurface,
        tertiaryContainer = p.tertiary.copy(alpha = 0.18f),
        onTertiaryContainer = p.onSurface,
        outline = p.outline,
        surfaceVariant = p.primaryContainer.copy(alpha = 0.58f),
        onSurfaceVariant = p.onSurface.copy(alpha = 0.82f)
    )

    val typography = Typography(
        displaySmall = TextStyle(fontFamily = family, fontSize = scaledSp(32f, textSizeStyle)),
        headlineMedium = TextStyle(fontFamily = family, fontSize = scaledSp(26f, textSizeStyle)),
        titleLarge = TextStyle(fontFamily = family, fontSize = scaledSp(22f, textSizeStyle)),
        titleMedium = TextStyle(fontFamily = family, fontSize = scaledSp(18f, textSizeStyle)),
        bodyLarge = TextStyle(fontFamily = family, fontSize = scaledSp(17f, textSizeStyle)),
        bodyMedium = TextStyle(fontFamily = family, fontSize = scaledSp(15f, textSizeStyle)),
        labelLarge = TextStyle(fontFamily = family, fontSize = scaledSp(14f, textSizeStyle))
    )

    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
