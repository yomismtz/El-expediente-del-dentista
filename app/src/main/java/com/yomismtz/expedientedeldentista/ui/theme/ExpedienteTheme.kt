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
    BirdPaletteStyle.TUCAN -> if(lang=="en") "Toucan" else "Tucán"
    BirdPaletteStyle.NINFA -> if(lang=="en") "Cockatiel" else "Ninfa"
    BirdPaletteStyle.BUHO -> if(lang=="en") "Owl" else "Búho"
    BirdPaletteStyle.CUERVO -> if(lang=="en") "Raven" else "Cuervo"
    BirdPaletteStyle.GUACAMAYA -> if(lang=="en") "Macaw" else "Guacamaya"
    BirdPaletteStyle.PUG -> "Pug"
    BirdPaletteStyle.POMERANIA -> if(lang=="en") "Pomeranian" else "Pomerania"
    BirdPaletteStyle.GATO -> if(lang=="en") "Cat" else "Gato"
    BirdPaletteStyle.CONEJO -> if(lang=="en") "Rabbit" else "Conejo"
    BirdPaletteStyle.ELEFANTE -> if(lang=="en") "Elephant" else "Elefante"
    BirdPaletteStyle.CABALLO_CAFE -> if(lang=="en") "Brown horse" else "Caballo café"
    BirdPaletteStyle.CABALLO_PINTO -> if(lang=="en") "Pinto horse" else "Caballo pinto"
    BirdPaletteStyle.IGUANA -> "Iguana"
    BirdPaletteStyle.ARANA -> if(lang=="en") "Spider" else "Araña"
    BirdPaletteStyle.TORTUGA -> if(lang=="en") "Turtle" else "Tortuga"
    BirdPaletteStyle.PEZ_PAYASO -> if(lang=="en") "Clownfish" else "Pez payaso"
    BirdPaletteStyle.DELFIN -> if(lang=="en") "Dolphin" else "Delfín"
    BirdPaletteStyle.TIBURON -> if(lang=="en") "Shark" else "Tiburón"
    BirdPaletteStyle.AJOLOTE -> if(lang=="en") "Axolotl" else "Ajolote"
    BirdPaletteStyle.PINGUINO -> if(lang=="en") "Penguin" else "Pingüino"
    BirdPaletteStyle.PATO_MANDARIN -> if(lang=="en") "Mandarin duck" else "Pato mandarín"
    BirdPaletteStyle.HAMSTER -> if(lang=="en") "Hamster" else "Hámster"
    BirdPaletteStyle.FENIX -> if(lang=="en") "Phoenix" else "Fénix"
    BirdPaletteStyle.ZORRO -> if(lang=="en") "Fox" else "Zorro"
    BirdPaletteStyle.PAVO_REAL -> if(lang=="en") "Peacock" else "Pavo real"
    BirdPaletteStyle.COLIBRI -> if(lang=="en") "Hummingbird" else "Colibrí"
    BirdPaletteStyle.MARTIN_PESCADOR -> if(lang=="en") "Kingfisher" else "Martín pescador"
    BirdPaletteStyle.QUETZAL -> "Quetzal"
    BirdPaletteStyle.DRAGON -> if(lang=="en") "Dragon" else "Dragón"
}

fun birdPalette(style: BirdPaletteStyle): BirdPalette {
    fun p(a:Long,b:Long,c:Long,bg:Long,pc:Long,sc:Long,on:Long=0xFF29232AL)=BirdPalette(Color(a),Color.White,Color(b),Color(c),Color(bg),Color(0xFFFFFCFF),Color(on),Color(pc),Color(sc),Color(0xFF9B919BL))
    return when(style){
        BirdPaletteStyle.AGAPORNI -> p(0xFF6FAF79,0xFFF2A47F,0xFF54B8A5,0xFFFFF7F1,0xFFDDF0DE,0xFFFFE0D1)
        BirdPaletteStyle.TUCAN -> p(0xFFF2C230,0xFF202124,0xFFF28C28,0xFFFFFAE8,0xFFFFED9B,0xFFE4E4E4)
        BirdPaletteStyle.NINFA -> p(0xFF74777D,0xFFF0C83F,0xFFF28B35,0xFFFAF9F2,0xFFE6E7E8,0xFFFFF0AD)
        BirdPaletteStyle.BUHO -> p(0xFF76513D,0xFFD8B98C,0xFF9A714F,0xFFFFF8F0,0xFFEAD9CA,0xFFF5E7CF)
        BirdPaletteStyle.CUERVO -> p(0xFF5D4388,0xFF191820,0xFF8870AE,0xFFF5F2F8,0xFFE7DFF2,0xFFDCD9E1)
        BirdPaletteStyle.GUACAMAYA -> p(0xFFC72E36,0xFF2874C6,0xFFF2C438,0xFFFFF5F3,0xFFFFD9DC,0xFFD8E9FC)
        BirdPaletteStyle.PUG -> p(0xFFC49A6C,0xFF704A35,0xFF3C3533,0xFFFFF8EE,0xFFF2DFC8,0xFFE6D4C8)
        BirdPaletteStyle.POMERANIA -> p(0xFFE4B86B,0xFFFFE3AE,0xFFB97935,0xFFFFFAF0,0xFFFFEBCB,0xFFFFF2D9)
        BirdPaletteStyle.GATO -> p(0xFF626B78,0xFFD88FA9,0xFF9B82BD,0xFFF9F5FA,0xFFE3E6EB,0xFFF4DDE7)
        BirdPaletteStyle.CONEJO -> p(0xFFB77A9B,0xFFFFD6E5,0xFF8D6AA8,0xFFFFF8FB,0xFFFFE7EF,0xFFF3E7F7)
        BirdPaletteStyle.ELEFANTE -> p(0xFF697987,0xFF6EA7D8,0xFF9BAAB4,0xFFF4F8FB,0xFFDDE6EC,0xFFDCECF9)
        BirdPaletteStyle.CABALLO_CAFE -> p(0xFF7A4E32,0xFFD5B38A,0xFF9B6844,0xFFFFF7EF,0xFFEBD7C5,0xFFF2E5D4)
        BirdPaletteStyle.CABALLO_PINTO -> p(0xFF9B6A4A,0xFFF7F1E7,0xFF594338,0xFFFFFBF6,0xFFE9D6C8,0xFFF4EEE5)
        BirdPaletteStyle.IGUANA -> p(0xFF438B50,0xFFA7CF3A,0xFF74B65D,0xFFF5FAEF,0xFFD9EED5,0xFFEAF5C7)
        BirdPaletteStyle.ARANA -> p(0xFF2D2A31,0xFFE67E32,0xFF8A5A44,0xFFFFF6EF,0xFFE3E0E5,0xFFFFDFC8)
        BirdPaletteStyle.TORTUGA -> p(0xFF4D7950,0xFFA37A50,0xFF87A85C,0xFFF5F8EF,0xFFDDE9D5,0xFFE9DFCF)
        BirdPaletteStyle.PEZ_PAYASO -> p(0xFFEA7627,0xFFFFF4E8,0xFF343A40,0xFFFFF7F0,0xFFFFDFC6,0xFFF4ECE5)
        BirdPaletteStyle.DELFIN -> p(0xFF337FB5,0xFF8ED8EA,0xFF4CA6C6,0xFFF0FAFD,0xFFD6ECF8,0xFFD9F5FA)
        BirdPaletteStyle.TIBURON -> p(0xFF657783,0xFF4B93C6,0xFF9DB1BC,0xFFF2F7FA,0xFFDDE5EA,0xFFDCECF7)
        BirdPaletteStyle.AJOLOTE -> p(0xFFD77FA8,0xFF8056A5,0xFFF0A7C6,0xFFFFF4FA,0xFFF7DCE9,0xFFE9DDF3)
        BirdPaletteStyle.PINGUINO -> p(0xFF343B43,0xFFA7D9EA,0xFF6FA9C2,0xFFF3F9FB,0xFFDEE4E8,0xFFDFF3F9)
        BirdPaletteStyle.PATO_MANDARIN -> p(0xFFE47D32,0xFF35A9A3,0xFF7D4C3D,0xFFFFF7EF,0xFFFFDFC6,0xFFD6F1EE)
        BirdPaletteStyle.HAMSTER -> p(0xFFB97845,0xFFF2D5A4,0xFFD99578,0xFFFFF7EF,0xFFF5DFC9,0xFFFFEED4)
        BirdPaletteStyle.FENIX -> p(0xFFC83B2F,0xFFF2B536,0xFFE96D25,0xFFFFF3EA,0xFFFFD8D1,0xFFFFE9B9)
        BirdPaletteStyle.ZORRO -> p(0xFFE36F2D,0xFFFFE2B8,0xFF8B5033,0xFFFFF6ED,0xFFFFDCC6,0xFFFFEBD4)
        BirdPaletteStyle.PAVO_REAL -> p(0xFF176A9A,0xFF138C7A,0xFFB99132,0xFFF0F8FA,0xFFD4EAF4,0xFFD6F1EA)
        BirdPaletteStyle.COLIBRI -> p(0xFF8A3FA8,0xFF1BA89C,0xFFD85A9D,0xFFF8F2FA,0xFFEBDCF4,0xFFD5F2ED)
        BirdPaletteStyle.MARTIN_PESCADOR -> p(0xFF2479B8,0xFF1BA9B5,0xFFF18A38,0xFFF0F8FC,0xFFD8ECFA,0xFFD5F2F4)
        BirdPaletteStyle.QUETZAL -> p(0xFF087C62,0xFF15A69B,0xFFC43E58,0xFFF0FAF6,0xFFD2F1E5,0xFFD4F4F0)
        BirdPaletteStyle.DRAGON -> BirdPalette(Color(0xFF4B176D),Color.White,Color(0xFF6D28A2),Color(0xFF9B59D0),Color(0xFFF7F0FC),Color(0xFFFFFBFF),Color(0xFF2D1538),Color(0xFFE6D2F2),Color(0xFFF0E2F8),Color(0xFF9D82AC))
    }
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
