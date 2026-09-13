package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.settings.PaletteStyle
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
        Text(
            "YSM Expediente",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = cs.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            if (lang == "en") "Interactive dental education" else "Educación odontológica interactiva",
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

                Text(if (lang == "en") "2 · Professional title" else "2 · ¿Cómo quieres que te llame la app?", fontWeight = FontWeight.Black)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                        label = { Text("Doctor") }
                    )
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                        label = { Text("Doctora") }
                    )
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
                    if (lang == "en") "3 · Interface color palette" else "3 · Paleta de colores del interfaz",
                    fontWeight = FontWeight.Black
                )
                Text(
                    if (lang == "en") "Choose a bird-inspired palette. Agaporni is the default YSM palette."
                    else "Elige una paleta inspirada en aves. Agaporni es la paleta YSM predeterminada.",
                    style = MaterialTheme.typography.bodyMedium
                )

                PaletteStyle.entries.chunked(2).forEach { pair ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        pair.forEach { style ->
                            BirdPaletteCard(
                                style = style,
                                lang = lang,
                                selected = preferences.paletteStyle == style,
                                onClick = { onPreferencesChanged(preferences.copy(paletteStyle = style)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }

        Text(
            if (lang == "en") "Selected: ${paletteDisplayName(preferences.paletteStyle, lang)}"
            else "Seleccionada: ${paletteDisplayName(preferences.paletteStyle, lang)}",
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
            if (lang == "en") "You can change language, title and palette later in Settings."
            else "Después podrás cambiar idioma, título y paleta desde Configuración.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = cs.onSurface.copy(alpha = 0.76f)
        )
    }
}

@Composable
private fun BirdPaletteCard(
    style: PaletteStyle,
    lang: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val swatches = paletteSwatches(style)
    val cs = MaterialTheme.colorScheme
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) cs.primaryContainer else cs.surface
        ),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) cs.primary else cs.outline.copy(alpha = 0.45f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                paletteDisplayName(style, lang),
                fontWeight = if (selected) FontWeight.Black else FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                swatches.forEach { color ->
                    Box(Modifier.size(18.dp).background(color, CircleShape))
                }
            }
            if (style == PaletteStyle.AGAPORNI) {
                Text(
                    if (lang == "en") "Default" else "Predeterminada",
                    style = MaterialTheme.typography.labelLarge,
                    color = cs.secondary
                )
            }
        }
    }
}
