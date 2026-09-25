package com.yomismtz.expedientedeldentista.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.CardShapeStyle
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
    BirdPaletteStyle.SERPIENTE -> if(lang=="en") "Snake" else "Serpiente"
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
    BirdPaletteStyle.BALLENA_AZUL -> if(lang=="en") "Clinical blue whale" else "Ballena azul clínica"
    BirdPaletteStyle.RANA_VERDE -> if(lang=="en") "Surgical green frog" else "Rana verde quirúrgica"
    BirdPaletteStyle.MARIPOSA_MONARCA -> if(lang=="en") "Monarch butterfly" else "Mariposa monarca"
    BirdPaletteStyle.FLAMENCO_ROSA -> if(lang=="en") "Soft pink flamingo" else "Flamenco rosa suave"
    BirdPaletteStyle.CABALLITO_TURQUESA -> if(lang=="en") "Turquoise seahorse" else "Caballito turquesa"
    BirdPaletteStyle.CANGREJO_CORAL -> if(lang=="en") "Coral crab" else "Cangrejo coral"
    BirdPaletteStyle.MURCIELAGO_NOCHE -> if(lang=="en") "Night blue bat" else "Murciélago azul noche"
    BirdPaletteStyle.PANDA_MONO -> if(lang=="en") "Monochrome panda" else "Panda monocromático"
    BirdPaletteStyle.ABEJA_CONTRASTE -> if(lang=="en") "High contrast bee" else "Abeja alto contraste"
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
        BirdPaletteStyle.PUG -> p(0xFF8B6F47,0xFF2F2925,0xFFE7C79A,0xFFF7F0E5,0xFFD8C19F,0xFFB8A58C)
        BirdPaletteStyle.POMERANIA -> p(0xFFC9822D,0xFFFFD166,0xFF7A4A20,0xFFFFF4D8,0xFFFFDFA1,0xFFF5C46B)
        BirdPaletteStyle.GATO -> p(0xFF53606D,0xFFB35C88,0xFF6F4C8E,0xFFF1EDF5,0xFFCBD3DC,0xFFE5BED1)
        BirdPaletteStyle.CONEJO -> p(0xFFD05B91,0xFFF6C5D8,0xFF7D5A8C,0xFFFFEAF3,0xFFFFBCD5,0xFFE8D4F0)
        BirdPaletteStyle.ELEFANTE -> p(0xFF596875,0xFF93A4B0,0xFF345E7A,0xFFEAF0F3,0xFFC8D2D9,0xFFD3E4EE)
        BirdPaletteStyle.CABALLO_CAFE -> p(0xFF6B3E24,0xFFC08A58,0xFF3E271C,0xFFF4E7D7,0xFFD9B58E,0xFFE8CFB0)
        BirdPaletteStyle.SERPIENTE -> p(0xFF6B8E23,0xFFD4E157,0xFF274E13,0xFFF0F5CF,0xFFC8DB72,0xFFE3ED9A)
        BirdPaletteStyle.IGUANA -> p(0xFF177245,0xFF56B870,0xFF8B6F47,0xFFE5F3E8,0xFFAED9B8,0xFFD7C8A9)
        BirdPaletteStyle.ARANA -> p(0xFF2D2A31,0xFFE67E32,0xFF8A5A44,0xFFFFF6EF,0xFFE3E0E5,0xFFFFDFC8)
        BirdPaletteStyle.TORTUGA -> p(0xFF486B3B,0xFF8A6A3F,0xFFB3A35A,0xFFEDE8D2,0xFFBFC99B,0xFFD8C59C)
        BirdPaletteStyle.PEZ_PAYASO -> p(0xFFEA7627,0xFFFFF4E8,0xFF343A40,0xFFFFF7F0,0xFFFFDFC6,0xFFF4ECE5)
        BirdPaletteStyle.DELFIN -> p(0xFF1F8DBA,0xFF62D0D9,0xFF0E5F88,0xFFE2F7FA,0xFFAEE5EC,0xFFC8F2F1)
        BirdPaletteStyle.TIBURON -> p(0xFF455A64,0xFF78909C,0xFF263238,0xFFE7ECEF,0xFFB0BEC5,0xFFD1D9DD)
        BirdPaletteStyle.AJOLOTE -> p(0xFFD77FA8,0xFF8056A5,0xFFF0A7C6,0xFFFFF4FA,0xFFF7DCE9,0xFFE9DDF3)
        BirdPaletteStyle.PINGUINO -> p(0xFF171A1D,0xFFE8F5F9,0xFFFFB547,0xFFEAF6FA,0xFFBFDCE6,0xFFFFD28A)
        BirdPaletteStyle.PATO_MANDARIN -> p(0xFFE47D32,0xFF35A9A3,0xFF7D4C3D,0xFFFFF7EF,0xFFFFDFC6,0xFFD6F1EE)
        BirdPaletteStyle.HAMSTER -> p(0xFFB97845,0xFFF2D5A4,0xFFD99578,0xFFFFF7EF,0xFFF5DFC9,0xFFFFEED4)
        BirdPaletteStyle.FENIX -> p(0xFFB51E23,0xFFFFA000,0xFFFFD54F,0xFFFFE3C2,0xFFFF9E80,0xFFFFCC80)
        BirdPaletteStyle.ZORRO -> p(0xFFD85B1F,0xFFF4C27A,0xFF513126,0xFFFBE6CF,0xFFF2A66A,0xFFE7C49A)
        BirdPaletteStyle.PAVO_REAL -> p(0xFF176A9A,0xFF138C7A,0xFFB99132,0xFFF0F8FA,0xFFD4EAF4,0xFFD6F1EA)
        BirdPaletteStyle.COLIBRI -> p(0xFF8A3FA8,0xFF1BA89C,0xFFD85A9D,0xFFF8F2FA,0xFFEBDCF4,0xFFD5F2ED)
        BirdPaletteStyle.MARTIN_PESCADOR -> p(0xFF2479B8,0xFF1BA9B5,0xFFF18A38,0xFFF0F8FC,0xFFD8ECFA,0xFFD5F2F4)
        BirdPaletteStyle.QUETZAL -> p(0xFF087C62,0xFF15A69B,0xFFC43E58,0xFFF0FAF6,0xFFD2F1E5,0xFFD4F4F0)
        BirdPaletteStyle.DRAGON -> BirdPalette(Color(0xFF4B176D),Color.White,Color(0xFF6D28A2),Color(0xFF9B59D0),Color(0xFFF7F0FC),Color(0xFFFFFBFF),Color(0xFF2D1538),Color(0xFFE6D2F2),Color(0xFFF0E2F8),Color(0xFF9D82AC))
        BirdPaletteStyle.BALLENA_AZUL -> p(0xFF075985,0xFF38BDF8,0xFF0C4A6E,0xFFDFF4FF,0xFF7DD3FC,0xFFBAE6FD)
        BirdPaletteStyle.RANA_VERDE -> p(0xFF16803A,0xFF8BC34A,0xFFF4D03F,0xFFE6F4D7,0xFFB7DD8A,0xFFFFE58A)
        BirdPaletteStyle.MARIPOSA_MONARCA -> p(0xFFE56A1F,0xFF171717,0xFFF2A23A,0xFFFFF6EA,0xFFFFD8B5,0xFFE8E0D4)
        BirdPaletteStyle.FLAMENCO_ROSA -> p(0xFFD84A7F,0xFFFF8FAB,0xFFFFC2D1,0xFFFFE0EA,0xFFFFA8C0,0xFFFFC8D8)
        BirdPaletteStyle.CABALLITO_TURQUESA -> p(0xFF007F86,0xFF28C7B7,0xFFF2B84B,0xFFDDF7F2,0xFF7CE0D2,0xFFFFD98A)
        BirdPaletteStyle.CANGREJO_CORAL -> p(0xFFD9473F,0xFFFF7F66,0xFF8F2D2A,0xFFFFE0D8,0xFFFFA38F,0xFFFFC0B2)
        BirdPaletteStyle.MURCIELAGO_NOCHE -> BirdPalette(Color(0xFF243B66),Color.White,Color(0xFF536D9B),Color(0xFF7184AD),Color(0xFFEFF3FA),Color(0xFFF8FAFD),Color(0xFF17243D),Color(0xFFD9E2F1),Color(0xFFE4EAF4),Color(0xFF71809A))
        BirdPaletteStyle.PANDA_MONO -> BirdPalette(Color(0xFF303030),Color.White,Color(0xFF666666),Color(0xFF8A8A8A),Color(0xFFF5F5F5),Color.White,Color(0xFF1E1E1E),Color(0xFFE2E2E2),Color(0xFFECECEC),Color(0xFF777777))
        BirdPaletteStyle.ABEJA_CONTRASTE -> BirdPalette(Color(0xFF111111),Color.White,Color(0xFFFFD600),Color(0xFF005BBB),Color.White,Color.White,Color.Black,Color(0xFFFFE766),Color(0xFFDDEBFF),Color.Black)
    }
}

fun paletteSwatches(style: BirdPaletteStyle): List<Color> {
    val p = birdPalette(style)
    return listOf(p.primary, p.secondary, p.tertiary, p.primaryContainer, p.secondaryContainer, p.background)
}

fun fontDisplayName(style: FontStyle, lang: String = "es"): String = when (style) {
    FontStyle.MODERN -> if (lang == "en") "Modern" else "Moderna"
    FontStyle.ROUNDED -> if (lang == "en") "Rounded" else "Redondeada"
    FontStyle.ACADEMIC -> if (lang == "en") "Academic" else "Académica"
    FontStyle.ACCESSIBLE -> if (lang == "en") "Accessible" else "Accesible"
    FontStyle.CLASSIC -> if (lang == "en") "Classic" else "Clásica"
    FontStyle.HANDWRITTEN -> if (lang == "en") "Handwritten" else "Manuscrita"
    FontStyle.COMPACT -> if (lang == "en") "Compact" else "Compacta"
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
    FontStyle.CLASSIC -> FontFamily.Serif
    FontStyle.HANDWRITTEN -> FontFamily.Cursive
    FontStyle.COMPACT -> FontFamily.SansSerif
}

private fun scaledSp(base: Float, textSizeStyle: TextSizeStyle) = (base * textSizeStyle.multiplier).sp

@Composable
fun ExpedienteTheme(
    paletteStyle: BirdPaletteStyle,
    fontStyle: FontStyle,
    textSizeStyle: TextSizeStyle = TextSizeStyle.NORMAL,
    cardShapeStyle: CardShapeStyle = CardShapeStyle.ROUNDED,
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
        surface = p.surface.copy(alpha = 0.98f),
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

    val radius = when(cardShapeStyle) { CardShapeStyle.SOFT -> 8.dp; CardShapeStyle.ROUNDED -> 18.dp; CardShapeStyle.SQUARE -> 2.dp }
    val shapes = Shapes(extraSmall=RoundedCornerShape(radius), small=RoundedCornerShape(radius), medium=RoundedCornerShape(radius), large=RoundedCornerShape(radius), extraLarge=RoundedCornerShape(radius))
    MaterialTheme(colorScheme = colors, typography = typography, shapes = shapes, content = content)
}
