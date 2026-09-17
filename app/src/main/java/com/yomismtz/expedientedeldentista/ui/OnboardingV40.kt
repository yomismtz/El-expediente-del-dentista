package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.ui.theme.BirdPaletteChoices
import com.yomismtz.expedientedeldentista.ui.theme.paletteDisplayName
import com.yomismtz.expedientedeldentista.ui.theme.paletteSwatches

@Composable
fun OnboardingV40Screen(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onContinue: (AppPreferences) -> Unit
) {
    val lang = preferences.languageTag
    val cs = MaterialTheme.colorScheme
    val fontScale = LocalDensity.current.fontScale

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(cs.primary, cs.primaryContainer, cs.background)))
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "YSM Expediente",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = cs.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            if (lang == "en") "Interactive guide to the dentist's clinical record" else "Guía interactiva del expediente clínico odontológico",
            color = cs.onPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cs.surface.copy(alpha = .96f)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (lang == "en") "Language" else "Idioma", fontWeight = FontWeight.Black)
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
                    if (lang == "en") "Choose the clinician shown in the guide" else "Elige al profesional que aparecerá en la guía",
                    fontWeight = FontWeight.Black
                )
                Text(
                    if (lang == "en")
                        "The portrait now uses a detailed dental-clinic scene, lab coat, scrubs, gloves and dental instruments."
                    else
                        "El retrato ahora muestra un entorno odontológico detallado, bata, uniforme clínico, guantes e instrumental dental.",
                    style = MaterialTheme.typography.bodySmall
                )

                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val stacked = maxWidth < 560.dp || fontScale >= 1.20f
                    if (stacked) {
                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ClinicianBirdCardV40(
                                title = ClinicianTitle.DOCTORA,
                                selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                                style = preferences.birdPaletteStyle,
                                lang = lang,
                                onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            ClinicianBirdCardV40(
                                title = ClinicianTitle.DOCTOR,
                                selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                                style = preferences.birdPaletteStyle,
                                lang = lang,
                                onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ClinicianBirdCardV40(
                                title = ClinicianTitle.DOCTORA,
                                selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                                style = preferences.birdPaletteStyle,
                                lang = lang,
                                onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                                modifier = Modifier.weight(1f)
                            )
                            ClinicianBirdCardV40(
                                title = ClinicianTitle.DOCTOR,
                                selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                                style = preferences.birdPaletteStyle,
                                lang = lang,
                                onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Text(
                    if (lang == "en") "Choose a bird palette. The bird badge changes with the palette." else "Elige una paleta de ave. La insignia del ave cambia con la paleta.",
                    fontWeight = FontWeight.Black
                )
                BirdPaletteChoices.chunked(2).forEach { pair ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        pair.forEach { style ->
                            PaletteCardV40(
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

        NoticeCard(
            if (lang == "en")
                "The application is an educational guide. It does not ask for a patient's name, address, telephone or real record number."
            else
                "La aplicación es una guía educativa. No pide nombre, domicilio, teléfono ni número real de expediente del paciente."
        )
        Button(
            onClick = { onContinue(preferences.copy(onboardingComplete = true)) },
            modifier = Modifier.fillMaxWidth().height(58.dp)
        ) {
            Text(if (lang == "en") "Continue" else "Continuar", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun ClinicianBirdCardV40(
    title: ClinicianTitle,
    selected: Boolean,
    style: BirdPaletteStyle,
    lang: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if (selected) 3.dp else 1.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(Modifier.fillMaxWidth().height(224.dp)) {
                ClinicianPortraitV47(
                    title = title,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = if (lang == "en") {
                        if (title == ClinicianTitle.DOCTORA) "Female dentist in a dental clinic" else "Male dentist in a dental clinic"
                    } else {
                        if (title == ClinicianTitle.DOCTORA) "Doctora en consultorio dental" else "Doctor en consultorio dental"
                    }
                )
                PaletteBirdBadgeV40(
                    style = style,
                    modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).size(74.dp)
                )
            }
            Text(
                if (title == ClinicianTitle.DOCTORA) "Doctora" else "Doctor",
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge
            )
            Text("🐦 ${paletteDisplayName(style, lang)}", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            if (selected) {
                Text(
                    if (lang == "en") "Selected ✓" else "Seleccionado ✓",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PaletteBirdBadgeV40(style: BirdPaletteStyle, modifier: Modifier = Modifier) {
    val swatches = paletteSwatches(style)
    val surface = MaterialTheme.colorScheme.surface
    val outline = MaterialTheme.colorScheme.outline
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        drawCircle(surface, radius = w * .48f, center = Offset(w * .5f, h * .5f))
        drawCircle(outline, radius = w * .48f, center = Offset(w * .5f, h * .5f), style = androidx.compose.ui.graphics.drawscope.Stroke(w * .025f))
        val bodyX = w * .43f
        val bodyY = h * .57f
        drawOval(swatches[0], Offset(w * .20f, h * .37f), Size(w * .44f, h * .38f))
        drawCircle(swatches[1], w * .17f, Offset(w * .61f, h * .35f))
        drawOval(swatches[2], Offset(w * .29f, h * .48f), Size(w * .25f, h * .18f))
        val longBeak = style in setOf(
            BirdPaletteStyle.TUCAN,
            BirdPaletteStyle.COLIBRI,
            BirdPaletteStyle.MARTIN_PESCADOR,
            BirdPaletteStyle.ABEJARUCO
        )
        val beak = Path().apply {
            moveTo(w * .74f, h * .34f)
            lineTo(w * if (longBeak) .98f else .88f, h * .39f)
            lineTo(w * .74f, h * .45f)
            close()
        }
        drawPath(beak, swatches[2])
        drawCircle(Color.Black, w * .027f, Offset(w * .66f, h * .31f))
        drawCircle(Color.White, w * .009f, Offset(w * .67f, h * .30f))
        val longTail = style in setOf(
            BirdPaletteStyle.QUETZAL,
            BirdPaletteStyle.PAVO_REAL,
            BirdPaletteStyle.GUACAMAYA,
            BirdPaletteStyle.FENIX
        )
        drawLine(
            swatches[0],
            Offset(bodyX, bodyY + h * .11f),
            Offset(w * .20f, h * if (longTail) .97f else .84f),
            strokeWidth = w * .08f
        )
        if (style == BirdPaletteStyle.NINFA) {
            drawLine(swatches[2], Offset(w * .58f, h * .18f), Offset(w * .55f, h * .06f), strokeWidth = w * .035f)
        }
        if (style == BirdPaletteStyle.PAVO_REAL) {
            for (i in -2..2) {
                drawCircle(swatches[(i + 2) % swatches.size], w * .04f, Offset(w * (.34f + i * .09f), h * .86f))
            }
        }
    }
}

@Composable
private fun PaletteCardV40(
    style: BirdPaletteStyle,
    lang: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sw = paletteSwatches(style)
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(9.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(paletteDisplayName(style, lang), fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                sw.forEach { color ->
                    Canvas(Modifier.size(width = 25.dp, height = 18.dp)) {
                        drawRoundRect(color, cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
                    }
                }
            }
        }
    }
}
