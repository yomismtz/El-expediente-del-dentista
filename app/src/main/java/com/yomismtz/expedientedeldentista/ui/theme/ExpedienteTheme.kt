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

private fun paletteFor(style: PaletteStyle): Palette = when (style) {
    PaletteStyle.WOOD -> Palette(Color(0xFF7A5230), Color.White, Color(0xFFB78B5B), Color(0xFFF6F0E7), Color(0xFFFFFBF5), Color(0xFF2C2118))
    PaletteStyle.CLINICAL_GREEN -> Palette(Color(0xFF2F6F61), Color.White, Color(0xFF80A89E), Color(0xFFF1F7F5), Color(0xFFFBFEFD), Color(0xFF1E2B27))
    PaletteStyle.DENTAL_BLUE -> Palette(Color(0xFF315F8C), Color.White, Color(0xFF83A7C8), Color(0xFFF1F5FA), Color(0xFFFCFDFF), Color(0xFF1D2731))
    PaletteStyle.WINE -> Palette(Color(0xFF7B3349), Color.White, Color(0xFFB77B8B), Color(0xFFFAF2F4), Color(0xFFFFFBFC), Color(0xFF2E1E23))
    PaletteStyle.SAGE -> Palette(Color(0xFF65745D), Color.White, Color(0xFFAAB49F), Color(0xFFF5F7F2), Color(0xFFFEFFFC), Color(0xFF242A21))
    PaletteStyle.MONO -> Palette(Color(0xFF202020), Color.White, Color(0xFF6B6B6B), Color(0xFFF5F5F5), Color.White, Color.Black)
}

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
        primaryContainer = p.secondary.copy(alpha = 0.22f),
        secondaryContainer = p.secondary.copy(alpha = 0.16f)
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
