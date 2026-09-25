package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle as ComposeFontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.ui.theme.BirdPaletteChoices
import com.yomismtz.expedientedeldentista.ui.theme.paletteDisplayName
import com.yomismtz.expedientedeldentista.ui.theme.paletteSwatches

@Composable
fun OnboardingV15Screen(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onContinue: (AppPreferences) -> Unit
) {
    val lang = preferences.languageTag
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(cs.primary, cs.primaryContainer, cs.background)))
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ysm_logo),
            contentDescription = if (lang == "en") "YSM bird and dental record logo" else "Logo YSM con ave y expediente dental",
            modifier = Modifier.size(176.dp)
        )
        Text(
            "YSM Expediente",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = cs.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            if (lang == "en") "The dentist's record" else "El expediente del dentista",
            style = MaterialTheme.typography.titleMedium,
            color = cs.onPrimary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            if (lang == "en") "Let your imagination take flight and your knowledge be reborn"
            else "Deja volar tu imaginación y tus conocimientos renacerán",
            style = MaterialTheme.typography.bodyLarge,
            fontStyle = ComposeFontStyle.Italic,
            color = cs.secondary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cs.surface.copy(alpha = 0.97f)),
            border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.55f)),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (lang == "en") "1 · Language" else "1 · Idioma", fontWeight = FontWeight.Black)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = lang == "es",
                        onClick = { onPreferencesChanged(preferences.copy(languageTag = "es")) },
                        label = { Text("Español") }
                    )
                    FilterChip(
                        selected = lang == "en",
                        onClick = { onPreferencesChanged(preferences.copy(languageTag = "en")) },
                        label = { Text("English") }
                    )
                }

                Text(
                    if (lang == "en") "2 · Are you Doctor or Doctora?"
                    else "2 · ¿Eres Doctor o Doctora?",
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    if (lang == "en") "Choose your anime profile. Doctora is accompanied by an Agaporni; Doctor by a rainbow lorikeet."
                    else "Elige tu perfil anime. La Doctora está acompañada por un agaporni y el Doctor por un Trichoglossus moluccanus.",
                    style = MaterialTheme.typography.bodyMedium
                )

                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val stacked = maxWidth < 430.dp || LocalDensity.current.fontScale >= 1.20f
                    if (stacked) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            ClinicianCardV21(
                                title = ClinicianTitle.DOCTORA,
                                selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                                imageRes = R.drawable.doctora_agaporni_anime,
                                bird = "Agaporni",
                                lang = lang,
                                onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) }
                            )
                            ClinicianCardV21(
                                title = ClinicianTitle.DOCTOR,
                                selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                                imageRes = R.drawable.doctor_lori_anime,
                                bird = "Trichoglossus moluccanus",
                                lang = lang,
                                onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) }
                            )
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            ClinicianCardV21(
                                title = ClinicianTitle.DOCTORA,
                                selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                                imageRes = R.drawable.doctora_agaporni_anime,
                                bird = "Agaporni",
                                lang = lang,
                                onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                                modifier = Modifier.weight(1f)
                            )
                            ClinicianCardV21(
                                title = ClinicianTitle.DOCTOR,
                                selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                                imageRes = R.drawable.doctor_lori_anime,
                                bird = "Trichoglossus moluccanus",
                                lang = lang,
                                onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cs.surface.copy(alpha = 0.97f)),
            border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.55f)),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    if (lang == "en") "3 · Choose your mascot and color palette" else "3 · Elige tu mascota y paleta de color",
                    fontWeight = FontWeight.Black
                )
                Text(
                    if (lang == "en") "Choose your mascot. Each mascot has its own app color palette."
                    else "Elige tu mascota. Cada mascota tiene su propia paleta de colores para la app.",
                    style = MaterialTheme.typography.bodyMedium
                )

                BirdPaletteChoices.chunked(2).forEach { pair ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        pair.forEach { style ->
                            BirdPaletteCard(
                                style = style,
                                lang = lang,
                                selected = preferences.birdPaletteStyle == style,
                                onClick = { onPreferencesChanged(preferences.copy(birdPaletteStyle = style)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }

        Text(
            if (lang == "en") "Selected: ${paletteDisplayName(preferences.birdPaletteStyle, lang)}"
            else "Seleccionada: ${paletteDisplayName(preferences.birdPaletteStyle, lang)}",
            fontWeight = FontWeight.Bold,
            color = cs.onSurface,
            modifier = Modifier
                .background(cs.surface.copy(alpha = 0.82f), RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp)
        )

        Button(
            onClick = { onContinue(preferences.copy(onboardingComplete = true)) },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(22.dp)
        ) {
            Text(if (lang == "en") "Continue" else "Continuar", fontWeight = FontWeight.Black)
        }

        Text(
            if (lang == "en") "You can change language, title, palette, font and text size later in Settings."
            else "Después podrás cambiar idioma, título, paleta, tipo y tamaño de letra desde Configuración.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = cs.onSurface.copy(alpha = 0.76f)
        )
    }
}

@Composable
private fun ClinicianCardV21(
    title: ClinicianTitle,
    selected: Boolean,
    imageRes: Int,
    bird: String,
    lang: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (selected) cs.primaryContainer else cs.surface),
        border = BorderStroke(if (selected) 3.dp else 1.dp, if (selected) cs.primary else cs.outline.copy(alpha = .45f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = if (title == ClinicianTitle.DOCTORA) "Doctora anime con agaporni" else "Doctor anime con Trichoglossus moluccanus",
                modifier = Modifier.fillMaxWidth().height(220.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier.fillMaxWidth().padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    if (title == ClinicianTitle.DOCTORA) "Doctora" else "Doctor",
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleLarge
                )
                Text("🐦 $bird", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                if (selected) Text(if (lang == "en") "Selected ✓" else "Seleccionado ✓", color = cs.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun BirdPaletteCard(
    style: BirdPaletteStyle,
    lang: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val swatches = paletteSwatches(style)
    val cs = MaterialTheme.colorScheme
    var reaction by remember { mutableStateOf(0) }
    val motion = remember { Animatable(0f) }
    LaunchedEffect(reaction) { if(reaction>0){ motion.snapTo(0f); motion.animateTo(1f,tween(180)); motion.animateTo(0f,tween(300)) } }
    Card(
        onClick = { reaction++; onClick() },
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = if (selected) cs.primaryContainer else cs.surface),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) cs.primary else cs.outline.copy(alpha = 0.45f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(5.dp)) {
                Text(mascotEmoji(style),modifier=Modifier.graphicsLayer { val k=motion.value; when(mascotMotionOnboarding(style)){0->{translationY=-18f*k;scaleX=1f+.12f*k;scaleY=1f+.12f*k};1->{rotationZ=14f*k;scaleX=1f+.08f*k};2->{translationX=12f*k;rotationZ=-10f*k};else->{scaleX=1f+.16f*k;scaleY=1f-.10f*k} } })
                Text(paletteDisplayName(style, lang),fontWeight=if(selected) FontWeight.Black else FontWeight.SemiBold,textAlign=TextAlign.Center)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                swatches.forEach { color ->
                    Box(Modifier.size(18.dp).background(color, CircleShape))
                }
            }
            if (style == BirdPaletteStyle.AGAPORNI) {
                Text(
                    if (lang == "en") "Default" else "Predeterminada",
                    style = MaterialTheme.typography.labelLarge,
                    color = cs.secondary
                )
            }
        }
    }
}

private fun mascotEmoji(style: BirdPaletteStyle): String = when(style) {
    BirdPaletteStyle.AGAPORNI -> "🐦"
    BirdPaletteStyle.TUCAN, BirdPaletteStyle.GUACAMAYA -> "🦜"
    BirdPaletteStyle.NINFA, BirdPaletteStyle.COLIBRI, BirdPaletteStyle.MARTIN_PESCADOR, BirdPaletteStyle.QUETZAL -> "🐦"
    BirdPaletteStyle.BUHO -> "🦉"
    BirdPaletteStyle.CUERVO -> "🐦‍⬛"
    BirdPaletteStyle.PUG -> "🐶"
    BirdPaletteStyle.POMERANIA -> "🐕"
    BirdPaletteStyle.GATO -> "🐱"
    BirdPaletteStyle.CONEJO -> "🐰"
    BirdPaletteStyle.ELEFANTE -> "🐘"
    BirdPaletteStyle.CABALLO_CAFE, BirdPaletteStyle.SERPIENTE -> "🐴"
    BirdPaletteStyle.IGUANA -> "🦎"
    BirdPaletteStyle.ARANA -> "🕷️"
    BirdPaletteStyle.TORTUGA -> "🐢"
    BirdPaletteStyle.PEZ_PAYASO -> "🐠"
    BirdPaletteStyle.DELFIN -> "🐬"
    BirdPaletteStyle.TIBURON -> "🦈"
    BirdPaletteStyle.AJOLOTE -> "🩷"
    BirdPaletteStyle.PINGUINO -> "🐧"
    BirdPaletteStyle.PATO_MANDARIN -> "🦆"
    BirdPaletteStyle.HAMSTER -> "🐹"
    BirdPaletteStyle.FENIX -> "🔥"
    BirdPaletteStyle.ZORRO -> "🦊"
    BirdPaletteStyle.PAVO_REAL -> "🦚"
    BirdPaletteStyle.DRAGON -> "🐉"
    BirdPaletteStyle.BALLENA_AZUL -> "🐋"
    BirdPaletteStyle.RANA_VERDE -> "🐸"
    BirdPaletteStyle.MARIPOSA_MONARCA -> "🦋"
    BirdPaletteStyle.FLAMENCO_ROSA -> "🦩"
    BirdPaletteStyle.CABALLITO_TURQUESA -> "🐠"
    BirdPaletteStyle.CANGREJO_CORAL -> "🦀"
    BirdPaletteStyle.MURCIELAGO_NOCHE -> "🦇"
    BirdPaletteStyle.PANDA_MONO -> "🐼"
    BirdPaletteStyle.ABEJA_CONTRASTE -> "🐝"
    BirdPaletteStyle.CAMALEON -> "🦎"
    BirdPaletteStyle.PERICO -> "🦜"
    BirdPaletteStyle.GALLO -> "🐓"
}

private fun mascotMotionOnboarding(style:BirdPaletteStyle)=when(style){
 BirdPaletteStyle.AGAPORNI,BirdPaletteStyle.NINFA,BirdPaletteStyle.CONEJO,BirdPaletteStyle.RANA_VERDE,BirdPaletteStyle.PINGUINO->0
 BirdPaletteStyle.TUCAN,BirdPaletteStyle.GUACAMAYA,BirdPaletteStyle.PERICO,BirdPaletteStyle.GALLO,BirdPaletteStyle.COLIBRI,BirdPaletteStyle.MARIPOSA_MONARCA,BirdPaletteStyle.MURCIELAGO_NOCHE->1
 BirdPaletteStyle.SERPIENTE,BirdPaletteStyle.IGUANA,BirdPaletteStyle.CAMALEON,BirdPaletteStyle.CANGREJO_CORAL,BirdPaletteStyle.PEZ_PAYASO,BirdPaletteStyle.DELFIN,BirdPaletteStyle.CABALLITO_TURQUESA->2
 else->3
}
