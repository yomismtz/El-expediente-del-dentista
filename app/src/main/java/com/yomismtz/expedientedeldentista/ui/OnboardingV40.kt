package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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

private const val ONBOARDING_LAST_PAGE = 2

@Composable
fun OnboardingV40Screen(preferences: AppPreferences, onPreferencesChanged: (AppPreferences) -> Unit, onContinue: (AppPreferences) -> Unit) {
    val lang = preferences.languageTag
    val cs = MaterialTheme.colorScheme
    val fontScale = LocalDensity.current.fontScale
    val configuration = LocalConfiguration.current
    val landscape = configuration.screenWidthDp > configuration.screenHeightDp
    var page by remember { mutableIntStateOf(0) }
    val scroll = rememberScrollState()

    Column(
        Modifier.fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .navigationBarsPadding()
            .padding(horizontal = if (configuration.screenWidthDp >= 600) 28.dp else 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.md), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("YSM Expediente", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF20133A), textAlign = TextAlign.Center)
        Text(if (lang == "en") "Interactive guide to the dentist's clinical record" else "Guía interactiva del expediente clínico odontológico", color = Color(0xFF51475F), fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        Text(if (lang == "en") "Step ${page + 1} of ${ONBOARDING_LAST_PAGE + 1}" else "Paso ${page + 1} de ${ONBOARDING_LAST_PAGE + 1}", style = MaterialTheme.typography.labelLarge, color = cs.primary)

        Card(Modifier.weight(1f).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = MaterialTheme.shapes.extraLarge, border = BorderStroke(1.dp, cs.outlineVariant), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
            Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(if (configuration.screenWidthDp >= 600) VisualSpacingV49.xl else VisualSpacingV49.lg), verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.md)) {
                when (page) {
                    0 -> WelcomePageV47(lang)
                    1 -> DentistAvatarCustomizerV51(preferences, onPreferencesChanged, lang)
                    else -> PalettePageV47(preferences, onPreferencesChanged, lang, fontScale)
                }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            if (page > 0) OutlinedButton(onClick = { page-- }, modifier = Modifier.weight(1f).heightIn(min = 52.dp)) { Text(if (lang == "en") "Back" else "Atrás", fontWeight = FontWeight.Bold) }
            else Spacer(Modifier.weight(1f))
            Button(onClick = { if (page < ONBOARDING_LAST_PAGE) page++ else onContinue(preferences.copy(onboardingComplete = true)) }, modifier = Modifier.weight(1f).heightIn(min = 52.dp)) {
                Text(if (page == ONBOARDING_LAST_PAGE) { if (lang == "en") "Start" else "Comenzar" } else { if (lang == "en") "Next" else "Siguiente" }, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable private fun WelcomePageV47(lang: String) {
    Text(if (lang == "en") "Welcome" else "Bienvenida", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
    Text(if (lang == "en") "Learn how to complete and explore a dental clinical record step by step." else "Aprende a llenar y explorar un expediente clínico odontológico paso a paso.", style = MaterialTheme.typography.bodyLarge)
    NoticeCard(if (lang == "en") "This is an educational guide. Do not enter a real patient's name, address, telephone number or record number." else "Esta es una guía educativa. No introduzcas nombre, domicilio, teléfono ni número real de expediente de un paciente.")
    Text(if (lang == "en") "You can change language, clinician, palette and text preferences later in the app." else "Después podrás cambiar idioma, profesional, paleta y preferencias de texto desde la aplicación.", style = MaterialTheme.typography.bodyMedium)
}

@Composable private fun ClinicianPageV47(preferences: AppPreferences, onPreferencesChanged: (AppPreferences) -> Unit, lang: String, fontScale: Float, landscape: Boolean) {
    Text(if (lang == "en") "Choose your guide" else "Elige a tu profesional", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
    Text(if (lang == "en") "Choose Doctor or Doctora. Your choice is saved and will also appear on the main screen." else "Elige Doctor o Doctora. Tu elección se guarda y también aparecerá en la pantalla principal.", style = MaterialTheme.typography.bodyLarge)
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val stacked = maxWidth < 560.dp || fontScale >= 1.20f || (landscape && maxWidth < 760.dp)
        if (stacked) Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ClinicianCardV47(ClinicianTitle.DOCTORA, preferences, onPreferencesChanged, lang, Modifier.fillMaxWidth())
            ClinicianCardV47(ClinicianTitle.DOCTOR, preferences, onPreferencesChanged, lang, Modifier.fillMaxWidth())
        } else Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ClinicianCardV47(ClinicianTitle.DOCTORA, preferences, onPreferencesChanged, lang, Modifier.weight(1f))
            ClinicianCardV47(ClinicianTitle.DOCTOR, preferences, onPreferencesChanged, lang, Modifier.weight(1f))
        }
    }
}

@Composable private fun ClinicianCardV47(title: ClinicianTitle, preferences: AppPreferences, onPreferencesChanged: (AppPreferences) -> Unit, lang: String, modifier: Modifier) {
    val selected = preferences.clinicianTitle == title
    Card(onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = title)) }, modifier = modifier, colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface), border = BorderStroke(if (selected) 3.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline), shape = MaterialTheme.shapes.large) {
        Column(Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Box(Modifier.fillMaxWidth().heightIn(min = 190.dp, max = 270.dp)) {
                ClinicianPortraitV47(title, Modifier.fillMaxSize(), if (lang == "en") if (title == ClinicianTitle.DOCTORA) "Female dentist" else "Male dentist" else if (title == ClinicianTitle.DOCTORA) "Doctora" else "Doctor")
                PaletteBirdBadgeV40(preferences.birdPaletteStyle, Modifier.align(Alignment.TopEnd).padding(VisualSpacingV49.sm).size(68.dp))
            }
            Text(if (title == ClinicianTitle.DOCTORA) "Doctora" else "Doctor", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            if (selected) Text(if (lang == "en") "Selected ✓" else "Seleccionado ✓", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable private fun PalettePageV47(preferences: AppPreferences, onPreferencesChanged: (AppPreferences) -> Unit, lang: String, fontScale: Float) {
    Text(if (lang == "en") "Choose a bird palette" else "Elige una paleta de ave", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
    Text(if (lang == "en") "The app colors and bird badge will follow this palette." else "Los colores de la app y la insignia del ave seguirán esta paleta.", style = MaterialTheme.typography.bodyLarge)
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val columns = if (maxWidth >= 600.dp && fontScale < 1.3f) 3 else if (maxWidth >= 360.dp && fontScale < 1.6f) 2 else 1
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BirdPaletteChoices.chunked(columns).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { style -> PaletteCardV40(style, lang, preferences.birdPaletteStyle == style, { onPreferencesChanged(preferences.copy(birdPaletteStyle = style)) }, Modifier.weight(1f)) }
                    repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
    NoticeCard(if (lang == "en") "Your selections are stored on this device and can be changed later." else "Tus selecciones se guardan en este dispositivo y podrás cambiarlas después.")
}

@Composable private fun PaletteBirdBadgeV40(style: BirdPaletteStyle, modifier: Modifier = Modifier) {
    val sw = paletteSwatches(style)
    Card(
        modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            Modifier.fillMaxSize().padding(VisualSpacingV49.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                paletteDisplayName(style, "es").take(2).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                sw.take(3).forEach { color ->
                    Box(Modifier.size(10.dp).background(color, MaterialTheme.shapes.extraSmall))
                }
            }
        }
    }
}

@Composable private fun PaletteCardV40(style: BirdPaletteStyle, lang: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val sw = paletteSwatches(style)
    Card(onClick = onClick, modifier = modifier.heightIn(min = 74.dp), colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface), border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline), shape = MaterialTheme.shapes.medium) {
        Column(Modifier.fillMaxWidth().padding(9.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(paletteDisplayName(style, lang), fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) { sw.forEach { color -> Box(Modifier.size(width = 25.dp, height = 18.dp).background(color, RoundedCornerShape(6.dp))) } }
            if (selected) Text("✓", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
        }
    }
}
