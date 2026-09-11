package com.yomismtz.expedientedeldentista.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.PaletteStyle

@Composable
fun AppRoot(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var screen by remember { mutableStateOf(AppScreen.HOME) }

    if (!preferences.onboardingComplete) {
        OnboardingScreen(
            preferences = preferences,
            onPreferencesChanged = onPreferencesChanged,
            onLanguageChanged = onLanguageChanged,
            onContinue = { onPreferencesChanged(preferences.copy(onboardingComplete = true)) }
        )
        return
    }

    val back = { screen = AppScreen.HOME }
    when (screen) {
        AppScreen.HOME -> HomeScreen(
            lang = preferences.languageTag,
            title = preferences.clinicianTitle,
            onNavigate = { screen = it },
            onSettings = { screen = AppScreen.SETTINGS }
        )
        AppScreen.SETTINGS -> SettingsScreen(
            preferences = preferences,
            onPreferencesChanged = onPreferencesChanged,
            onLanguageChanged = onLanguageChanged,
            onResetSession = { onSessionChanged(EducationalSession()) },
            onBack = back
        )
        AppScreen.IDENTIFICATION -> IdentificationScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.HISTORY -> HistoryScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.INTAKE -> IntakeNoteScreen(preferences.languageTag, session, back)
        AppScreen.ODONTOGRAM -> OdontogramScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.ICDAS -> IcdasScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.CPOD -> CpodScreen(preferences.languageTag, session, back)
        AppScreen.OLEARY -> OlearyScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.IPC -> IpcScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.IHOS -> IhosScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.PULPAL -> PulpalScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.TREATMENT -> TreatmentScreen(preferences.languageTag, session, onSessionChanged, back)
        AppScreen.EVOLUTION -> EvolutionScreen(preferences.languageTag, session, back)
    }
}

@Composable
private fun OnboardingScreen(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    onContinue: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                "🦷 ${tr(preferences.languageTag, "El expediente del dentista", "The Dentist's Clinical Record")}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                tr(
                    preferences.languageTag,
                    "Guía interactiva offline para aprender a llenar el expediente clínico odontológico.",
                    "Offline interactive guide for learning how to complete a dental clinical record."
                )
            )
        }
        item {
            SectionCard(tr(preferences.languageTag, "Idioma", "Language")) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = preferences.languageTag == "es",
                        onClick = {
                            onPreferencesChanged(preferences.copy(languageTag = "es"))
                            onLanguageChanged("es")
                        },
                        label = { Text("Español") }
                    )
                    FilterChip(
                        selected = preferences.languageTag == "en",
                        onClick = {
                            onPreferencesChanged(preferences.copy(languageTag = "en"))
                            onLanguageChanged("en")
                        },
                        label = { Text("English") }
                    )
                }
            }
        }
        item {
            SectionCard(tr(preferences.languageTag, "¿Doctor o Doctora?", "Doctor title")) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                        label = { Text(tr(preferences.languageTag, "Doctor", "Doctor")) }
                    )
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                        label = { Text(tr(preferences.languageTag, "Doctora", "Doctor")) }
                    )
                }
            }
        }
        item {
            NoticeCard(
                tr(
                    preferences.languageTag,
                    "Uso exclusivamente educativo. La app no crea, administra ni almacena expedientes clínicos reales. Los datos del ejercicio se mantienen solo durante la sesión de uso.",
                    "Educational use only. The app does not create, manage or store real clinical records. Exercise data is kept only during the current app session."
                )
            )
        }
        item {
            Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
                Text(tr(preferences.languageTag, "Abrir el expediente", "Open the record"))
            }
        }
    }
}

@Composable
private fun HomeScreen(
    lang: String,
    title: ClinicianTitle,
    onNavigate: (AppScreen) -> Unit,
    onSettings: () -> Unit
) {
    val greeting = if (title == ClinicianTitle.DOCTORA) {
        tr(lang, "Bienvenida, Doctora", "Welcome, Doctor")
    } else {
        tr(lang, "Bienvenido, Doctor", "Welcome, Doctor")
    }
    val sections = listOf(
        Triple(AppScreen.IDENTIFICATION, "👤", tr(lang, "Ficha de identificación", "Identification sheet")),
        Triple(AppScreen.HISTORY, "🩺", tr(lang, "Historia clínica y anamnesis", "Medical history and anamnesis")),
        Triple(AppScreen.INTAKE, "📄", tr(lang, "Nota de ingreso automática", "Automatic intake note")),
        Triple(AppScreen.ODONTOGRAM, "🦷", tr(lang, "Odontograma interactivo", "Interactive odontogram")),
        Triple(AppScreen.ICDAS, "🔎", "ICDAS"),
        Triple(AppScreen.CPOD, "➕", tr(lang, "CPOD / ceod", "DMFT / dmft")),
        Triple(AppScreen.OLEARY, "🟥", "O’Leary"),
        Triple(AppScreen.IPC, "6️⃣", "IPC / CPI"),
        Triple(AppScreen.IHOS, "🪥", "IHOS / OHI-S"),
        Triple(AppScreen.PERIODONTOGRAM, "📈", tr(lang, "Periodontograma", "Periodontal chart")),
        Triple(AppScreen.PULPAL, "⚡", tr(lang, "Diagnóstico pulpar y periapical", "Pulpal and periapical diagnosis")),
        Triple(AppScreen.TREATMENT, "📝", tr(lang, "Diagnóstico y tratamiento por diente", "Diagnosis and treatment by tooth")),
        Triple(AppScreen.EVOLUTION, "📋", tr(lang, "Notas de evolución automáticas", "Automatic progress notes"))
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(tr(lang, "El expediente del dentista", "The Dentist's Clinical Record"), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(greeting)
                }
                OutlinedButton(onClick = onSettings) { Text("⚙") }
            }
        }
        item { ClipboardHero(lang) }
        items(sections.size) { index ->
            val section = sections[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigate(section.first) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(section.second, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.width(12.dp))
                    Text(section.third, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    Text("›", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
        item {
            NoticeCard(
                tr(
                    lang,
                    "Todo funciona sin internet. La información clínica capturada sirve únicamente para aprender durante la sesión actual.",
                    "Everything works without internet. Entered clinical information is used only for learning during the current session."
                )
            )
        }
    }
}

@Composable
private fun ClipboardHero(lang: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(112.dp)
                    .height(150.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📋", style = MaterialTheme.typography.headlineMedium)
                        Text("🦷", style = MaterialTheme.typography.headlineMedium)
                        Text("📄 📄", textAlign = TextAlign.Center)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("👩‍⚕️", style = MaterialTheme.typography.displaySmall)
                Text(
                    tr(lang, "Tu carpeta de aprendizaje odontológico", "Your dental learning folder"),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    tr(lang, "Abre cada pestaña, aprende qué se registra y deja que la app haga los cálculos y resúmenes.",
                        "Open each tab, learn what is recorded, and let the app calculate and summarize."),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    onResetSession: () -> Unit,
    onBack: () -> Unit
) {
    val lang = preferences.languageTag
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { ScreenHeader(tr(lang, "Configuración", "Settings"), onBack) }
        item {
            SectionCard(tr(lang, "Idioma", "Language")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = lang == "es",
                        onClick = {
                            onPreferencesChanged(preferences.copy(languageTag = "es"))
                            onLanguageChanged("es")
                        },
                        label = { Text("Español") }
                    )
                    FilterChip(
                        selected = lang == "en",
                        onClick = {
                            onPreferencesChanged(preferences.copy(languageTag = "en"))
                            onLanguageChanged("en")
                        },
                        label = { Text("English") }
                    )
                }
            }
        }
        item {
            SectionCard(tr(lang, "Tratamiento de título", "Professional title")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                        label = { Text("Doctor") }
                    )
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                        label = { Text(tr(lang, "Doctora", "Doctor")) }
                    )
                }
            }
        }
        item {
            SectionCard(tr(lang, "Paleta de colores", "Color palette")) {
                PaletteStyle.entries.forEach { style ->
                    val label = when (style) {
                        PaletteStyle.WOOD -> tr(lang, "Madera clásica", "Classic wood")
                        PaletteStyle.CLINICAL_GREEN -> tr(lang, "Verde clínico", "Clinical green")
                        PaletteStyle.DENTAL_BLUE -> tr(lang, "Azul dental", "Dental blue")
                        PaletteStyle.WINE -> tr(lang, "Vino", "Wine")
                        PaletteStyle.SAGE -> tr(lang, "Salvia", "Sage")
                        PaletteStyle.MONO -> tr(lang, "Blanco y negro", "Black and white")
                    }
                    FilterChip(
                        selected = preferences.paletteStyle == style,
                        onClick = { onPreferencesChanged(preferences.copy(paletteStyle = style)) },
                        label = { Text(label) },
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }
        }
        item {
            SectionCard(tr(lang, "Tipografía", "Typography")) {
                FontStyle.entries.forEach { style ->
                    val label = when (style) {
                        FontStyle.MODERN -> tr(lang, "Moderna", "Modern")
                        FontStyle.ROUNDED -> tr(lang, "Redondeada", "Rounded")
                        FontStyle.ACADEMIC -> tr(lang, "Académica", "Academic")
                        FontStyle.ACCESSIBLE -> tr(lang, "Alta legibilidad", "High legibility")
                    }
                    FilterChip(
                        selected = preferences.fontStyle == style,
                        onClick = { onPreferencesChanged(preferences.copy(fontStyle = style)) },
                        label = { Text(label) },
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Text(
                        tr(lang, "Vista previa: La historia clínica se integra paso a paso.", "Preview: The clinical record is completed step by step."),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        item {
            SectionCard(tr(lang, "Sesión educativa", "Educational session")) {
                Text(tr(lang, "Borra los datos introducidos en el ejercicio actual. No afecta idioma, paleta ni tipografía.",
                    "Clears data entered in the current exercise. Language, palette and typography are kept."))
                OutlinedButton(onClick = onResetSession, modifier = Modifier.fillMaxWidth()) {
                    Text(tr(lang, "Reiniciar expediente de aprendizaje", "Reset learning record"))
                }
            }
        }
    }
}
