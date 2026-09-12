package com.yomismtz.expedientedeldentista.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.PaletteStyle

private data class Palette(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val onSurface: Color
)

/*
 * Identidad visual YSM.
 * Las opciones históricas de paleta se conservan por compatibilidad con las
 * preferencias locales, pero toda la aplicación utiliza ahora la misma familia
 * lavanda · lila · púrpura · metal para mantener una experiencia coherente.
 */
private val YsmPalette = Palette(
    primary = Color(0xFF5D347F),
    onPrimary = Color.White,
    secondary = Color(0xFFA57AC7),
    background = Color(0xFFF5EFFA),
    surface = Color(0xFFFFFCFF),
    onSurface = Color(0xFF321943)
)

@Suppress("UNUSED_PARAMETER")
private fun paletteFor(style: PaletteStyle): Palette = YsmPalette

private fun familyFor(style: FontStyle): FontFamily = when (style) {
    FontStyle.MODERN -> FontFamily.SansSerif
    FontStyle.ROUNDED -> FontFamily.Cursive
    FontStyle.ACADEMIC -> FontFamily.Serif
    FontStyle.ACCESSIBLE -> FontFamily.Monospace
}

@Composable
fun ExpedienteTheme(
    paletteStyle: PaletteStyle,
    fontStyle: FontStyle,
    content: @Composable () -> Unit
) {
    val p = paletteFor(paletteStyle)
    val family = familyFor(fontStyle)
    val colors = lightColorScheme(
        primary = p.primary,
        onPrimary = p.onPrimary,
        secondary = p.secondary,
        background = p.background,
        surface = p.surface,
        onSurface = p.onSurface,
        primaryContainer = Color(0xFFE7D7F6),
        onPrimaryContainer = Color(0xFF321943),
        secondaryContainer = Color(0xFFD0B6E8),
        onSecondaryContainer = Color(0xFF321943),
        outline = Color(0xFFB7B0BD),
        surfaceVariant = Color(0xFFF0E7F6),
        onSurfaceVariant = Color(0xFF4D365B)
    )
    val typography = Typography(
        displaySmall = TextStyle(fontFamily = family, fontSize = 32.sp),
        headlineMedium = TextStyle(fontFamily = family, fontSize = 26.sp),
        titleLarge = TextStyle(fontFamily = family, fontSize = 22.sp),
        titleMedium = TextStyle(fontFamily = family, fontSize = 18.sp),
        bodyLarge = TextStyle(fontFamily = family, fontSize = 17.sp),
        bodyMedium = TextStyle(fontFamily = family, fontSize = 15.sp),
        labelLarge = TextStyle(fontFamily = family, fontSize = 14.sp)
    )

    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
