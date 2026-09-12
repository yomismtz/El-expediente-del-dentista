package com.yomismtz.expedientedeldentista.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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

    val folderBack = { screen = AppScreen.FOLDER }
    Crossfade(targetState = screen, label = "folder_navigation") { target ->
        when (target) {
            AppScreen.HOME -> FolderCoverScreen(
                lang = preferences.languageTag,
                title = preferences.clinicianTitle,
                onOpen = { screen = AppScreen.FOLDER },
                onSettings = { screen = AppScreen.SETTINGS }
            )
            AppScreen.FOLDER -> FolderSpreadScreen(
                lang = preferences.languageTag,
                onNavigate = { screen = it },
                onClose = { screen = AppScreen.HOME },
                onSettings = { screen = AppScreen.SETTINGS }
            )
            AppScreen.SETTINGS -> SettingsScreen(
                preferences = preferences,
                onPreferencesChanged = onPreferencesChanged,
                onLanguageChanged = onLanguageChanged,
                onResetSession = { onSessionChanged(EducationalSession()) },
                onBack = folderBack
            )
            AppScreen.IDENTIFICATION -> IdentificationScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.INTAKE -> IntakeNoteScreen(preferences.languageTag, session, folderBack)
            AppScreen.ACTIVITIES -> ActivitiesScreen(preferences.languageTag, folderBack)
            AppScreen.ATM -> AtmScreen(preferences.languageTag, folderBack)
            AppScreen.OCCLUSION -> OcclusionScreen(preferences.languageTag, folderBack)
            AppScreen.MUCOSA -> MucosaScreen(preferences.languageTag, folderBack)
            AppScreen.AUXILIARIES -> AuxiliariesScreen(preferences.languageTag, folderBack)
            AppScreen.ODONTOGRAM -> OdontogramScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.ICDAS -> IcdasScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.CPOD -> CpodScreen(preferences.languageTag, session, folderBack)
            AppScreen.OLEARY -> OlearyScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.IPC -> IpcScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.IHOS -> IhosScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.POSTURE -> PostureScreen(preferences.languageTag, folderBack)
            AppScreen.PULPAL -> PulpalScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.APICAL -> ApicalScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.TREATMENT -> TreatmentScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.EVOLUTION -> EvolutionScreen(preferences.languageTag, session, folderBack)
            AppScreen.SESSIONS -> SimpleEducationalSheet(
                preferences.languageTag, "Tratamiento por sesiones", "Treatment by sessions",
                "Aprende cómo organizar el tratamiento cronológicamente por citas.",
                "Learn how to organize treatment chronologically by visits.",
                listOf("Fecha de la sesión", "Procedimiento planeado", "Procedimiento realizado", "Indicaciones", "Seguimiento / siguiente cita"),
                listOf("Session date", "Planned procedure", "Procedure performed", "Instructions", "Follow-up / next visit"), folderBack
            )
            AppScreen.ENDO -> SimpleEducationalSheet(
                preferences.languageTag, "Ficha endodóntica", "Endodontic sheet",
                "Reconoce los apartados destinados a pruebas pulpares, diagnóstico, conductometría y tratamiento endodóntico.",
                "Recognize the fields for pulp testing, diagnosis, working length and endodontic treatment.",
                listOf("Pruebas de sensibilidad", "Percusión y palpación", "Diagnóstico pulpar y periapical", "Longitud de trabajo", "Irrigación, medicación y obturación"),
                listOf("Sensitivity tests", "Percussion and palpation", "Pulpal and periapical diagnosis", "Working length", "Irrigation, medication and obturation"), folderBack
            )
            AppScreen.PROSTHETIC -> SimpleEducationalSheet(
                preferences.languageTag, "Ficha protésica", "Prosthetic sheet",
                "Aprende qué información se registra cuando el diagnóstico y tratamiento incluyen rehabilitación protésica.",
                "Learn what is recorded when diagnosis and treatment include prosthetic rehabilitation.",
                listOf("Dientes ausentes", "Clasificación de Kennedy", "Soporte y pronóstico", "Diseño protésico", "Plan de tratamiento"),
                listOf("Missing teeth", "Kennedy classification", "Support and prognosis", "Prosthetic design", "Treatment plan"), folderBack
            )
            AppScreen.SURGICAL -> SimpleEducationalSheet(
                preferences.languageTag, "Ficha quirúrgica", "Surgical sheet",
                "Identifica los datos que deben documentarse cuando existe un procedimiento quirúrgico.",
                "Identify what should be documented when a surgical procedure is performed.",
                listOf("Indicación", "Zona / órgano dentario", "Anestesia", "Procedimiento", "Indicaciones posoperatorias y seguimiento"),
                listOf("Indication", "Site / tooth", "Anesthesia", "Procedure", "Postoperative instructions and follow-up"), folderBack
            )
            AppScreen.CONSENT -> SimpleEducationalSheet(
                preferences.languageTag, "Consentimiento informado", "Informed consent",
                "El alumno debe reconocer que el consentimiento explica el procedimiento, riesgos, beneficios, alternativas y dudas antes de aceptar.",
                "The student should recognize that consent explains the procedure, risks, benefits, alternatives and questions before agreement.",
                listOf("Procedimiento propuesto", "Beneficios", "Riesgos y complicaciones", "Alternativas", "Oportunidad de hacer preguntas"),
                listOf("Proposed procedure", "Benefits", "Risks and complications", "Alternatives", "Opportunity to ask questions"), folderBack
            )
            AppScreen.REQUEST -> SimpleEducationalSheet(
                preferences.languageTag, "Solicitud de tratamiento", "Treatment request",
                "Explica para qué sirve la solicitud y qué información debe identificar claramente.",
                "Explains the purpose of the request and what information it should clearly identify.",
                listOf("Servicio solicitado", "Motivo", "Área o diente", "Prioridad / referencia", "Responsable"),
                listOf("Requested service", "Reason", "Area or tooth", "Priority / referral", "Responsible clinician"), folderBack
            )
            AppScreen.BUDGET -> SimpleEducationalSheet(
                preferences.languageTag, "Presupuesto", "Budget",
                "Muestra la estructura del presupuesto como parte administrativa del expediente, sin capturar pagos reales.",
                "Shows the structure of a budget as an administrative record component, without recording real payments.",
                listOf("Procedimiento", "Cantidad", "Costo unitario", "Subtotal", "Total y condiciones"),
                listOf("Procedure", "Quantity", "Unit cost", "Subtotal", "Total and conditions"), folderBack
            )
        }
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
            Text("📁 ${tr(preferences.languageTag, "El expediente del dentista", "The Dentist's Clinical Record")}",
                style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(tr(preferences.languageTag,
                "Una carpeta didáctica para aprender cómo se llena cada hoja del expediente odontológico.",
                "A teaching folder for learning how each dental clinical-record sheet is completed."))
        }
        item {
            SectionCard(tr(preferences.languageTag, "Idioma", "Language")) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(preferences.languageTag == "es", {
                        onPreferencesChanged(preferences.copy(languageTag = "es")); onLanguageChanged("es")
                    }, { Text("Español") })
                    FilterChip(preferences.languageTag == "en", {
                        onPreferencesChanged(preferences.copy(languageTag = "en")); onLanguageChanged("en")
                    }, { Text("English") })
                }
            }
        }
        item {
            SectionCard(tr(preferences.languageTag, "¿Doctor o Doctora?", "Professional title")) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                        { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                        { Text("Doctor") })
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                        { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                        { Text(tr(preferences.languageTag, "Doctora", "Doctor")) })
                }
            }
        }
        item { NoticeCard(tr(preferences.languageTag,
            "Uso educativo. No se solicitan ni almacenan datos de pacientes reales.",
            "Educational use. Real patient data is neither requested nor stored.")) }
        item {
            Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
                Text(tr(preferences.languageTag, "Ver la portada del expediente", "View folder cover"))
            }
        }
    }
}

@Composable
private fun FolderCoverScreen(lang: String, title: ClinicianTitle, onOpen: () -> Unit, onSettings: () -> Unit) {
    val greeting = if (title == ClinicianTitle.DOCTORA) tr(lang, "Doctora", "Doctor") else "Doctor"
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onSettings) { Text("⚙") }
                }
                Text("📋", style = MaterialTheme.typography.displaySmall)
                Text(tr(lang, "EL EXPEDIENTE DEL DENTISTA", "THE DENTIST'S CLINICAL RECORD"),
                    style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text(tr(lang, "Carpeta de aprendizaje · $greeting", "Learning folder · $greeting"), textAlign = TextAlign.Center)
                Surface(
                    modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(tr(lang, "PORTADA", "COVER"), fontWeight = FontWeight.Bold)
                        Text("▸ ${tr(lang, "Ficha de identificación", "Identification sheet")}")
                        Text("▸ ASA · ATM · ${tr(lang, "Oclusión", "Occlusion")}")
                        Text("▸ ${tr(lang, "Historia clínica y mucosas", "Medical history and mucosa")}")
                        Text("▸ ${tr(lang, "Odontograma", "Odontogram")}")
                        Text("▸ ICDAS · CPOD/ceod · O’Leary · IPC · IHOS")
                        Text("▸ ${tr(lang, "Periodoncia · Diagnóstico · Tratamiento · Evolución", "Periodontics · Diagnosis · Treatment · Progress")}")
                    }
                }
                Text(tr(lang,
                    "La portada identifica las secciones. Al abrirla aparecen dos hojas y separadores inferiores como en un expediente físico.",
                    "The cover identifies the sections. Opening it shows two pages and bottom tabs like a physical clinical folder."),
                    style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
                    Text("📂 ${tr(lang, "Abrir expediente", "Open folder")}")
                }
            }
        }
    }
}

private data class FolderTab(val screen: AppScreen, val icon: String, val es: String, val en: String)

private fun treatmentTabs(): List<FolderTab> = listOf(
    FolderTab(AppScreen.ACTIVITIES, "✓", "Autorización", "Authorization"),
    FolderTab(AppScreen.TREATMENT, "📝", "Diagnóstico y tratamiento", "Diagnosis & treatment"),
    FolderTab(AppScreen.SESSIONS, "🗓", "Tratamiento por sesiones", "Treatment by sessions"),
    FolderTab(AppScreen.ENDO, "⚡", "Ficha endodóntica", "Endodontic sheet"),
    FolderTab(AppScreen.PROSTHETIC, "🦷", "Ficha protésica", "Prosthetic sheet"),
    FolderTab(AppScreen.PERIODONTOGRAM, "📈", "Periodontograma", "Periodontal chart"),
    FolderTab(AppScreen.SURGICAL, "✚", "Ficha quirúrgica", "Surgical sheet"),
    FolderTab(AppScreen.OLEARY, "🔴", "O’Leary", "O’Leary")
)

private fun recordTabs(): List<FolderTab> = listOf(
    FolderTab(AppScreen.IDENTIFICATION, "👤", "Ficha de identificación", "Identification"),
    FolderTab(AppScreen.HISTORY, "🩺", "Historia clínica", "History"),
    FolderTab(AppScreen.MUCOSA, "👄", "Mucosas", "Mucosa"),
    FolderTab(AppScreen.IPC, "6️⃣", "IPC", "CPI"),
    FolderTab(AppScreen.IHOS, "🪥", "IHOS", "OHI-S"),
    FolderTab(AppScreen.AUXILIARIES, "🩻", "Auxiliares", "Diagnostic aids"),
    FolderTab(AppScreen.ICDAS, "🔎", "ICDAS", "ICDAS"),
    FolderTab(AppScreen.CPOD, "➕", "CPOD/ceod", "DMFT/dmft"),
    FolderTab(AppScreen.CONSENT, "✍", "Consentimiento", "Consent"),
    FolderTab(AppScreen.REQUEST, "📨", "Solicitud", "Request"),
    FolderTab(AppScreen.BUDGET, "💲", "Presupuesto", "Budget"),
    FolderTab(AppScreen.EVOLUTION, "📋", "Notas de evolución", "Progress notes"),
    FolderTab(AppScreen.ATM, "🦴", "ATM", "TMJ"),
    FolderTab(AppScreen.OCCLUSION, "↔", "Oclusión", "Occlusion"),
    FolderTab(AppScreen.POSTURE, "🧍", "Postura", "Posture"),
    FolderTab(AppScreen.PULPAL, "⚡", "Pulpar", "Pulpal"),
    FolderTab(AppScreen.APICAL, "◉", "Periapical", "Periapical"),
    FolderTab(AppScreen.ODONTOGRAM, "🦷", "Odontograma", "Odontogram")
)

@Composable
private fun FolderSpreadScreen(lang: String, onNavigate: (AppScreen) -> Unit, onClose: () -> Unit, onSettings: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onClose) { Text("📁 ${tr(lang, "Portada", "Cover")}") }
            Text(tr(lang, "Expediente abierto", "Open folder"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = onSettings) { Text("⚙") }
        }
        BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth().padding(12.dp)) {
            if (maxWidth >= 700.dp) {
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActivityPage(lang, { onNavigate(AppScreen.ACTIVITIES) }, Modifier.weight(1f))
                    IntakePage(lang, { onNavigate(AppScreen.INTAKE) }, Modifier.weight(1f))
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActivityPage(lang, { onNavigate(AppScreen.ACTIVITIES) }, Modifier.width(310.dp))
                    IntakePage(lang, { onNavigate(AppScreen.INTAKE) }, Modifier.width(310.dp))
                }
            }
        }
        Text(tr(lang, "Separadores de tratamiento", "Treatment tabs"), modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp), fontWeight = FontWeight.SemiBold)
        FolderTabRow(lang, treatmentTabs(), MaterialTheme.colorScheme.primaryContainer, onNavigate)
        Text(tr(lang, "Separadores del expediente · desliza para ver todos", "Clinical-record tabs · swipe to see all"), modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp), fontWeight = FontWeight.SemiBold)
        FolderTabRow(lang, recordTabs(), MaterialTheme.colorScheme.secondaryContainer, onNavigate)
    }
}

@Composable
private fun FolderTabRow(lang: String, tabs: List<FolderTab>, color: androidx.compose.ui.graphics.Color, onNavigate: (AppScreen) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        items(tabs) { tab ->
            Card(onClick = { onNavigate(tab.screen) }, colors = CardDefaults.cardColors(containerColor = color)) {
                Column(Modifier.padding(horizontal = 10.dp, vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(tab.icon)
                    Text(if (lang == "en") tab.en else tab.es, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun ActivityPage(lang: String, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("📋 ${tr(lang, "REGISTRO DE ACTIVIDADES", "ACTIVITY RECORD")}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(tr(lang,
                "Hoja izquierda, como en la presentación: alumno, actividad, autorización, actividad realizada y supervisión.",
                "Left page, as in the presentation: student, activity, authorization, activity performed and supervision."))
            listOf(
                tr(lang, "Alumno / operador", "Student / operator"),
                tr(lang, "Actividad planeada", "Planned activity"),
                tr(lang, "Autorización antes de iniciar", "Authorization before starting"),
                tr(lang, "Actividad realmente realizada", "Activity actually performed"),
                tr(lang, "Supervisión al finalizar", "Supervision at completion")
            ).forEach { Text("□ $it __________________") }
            Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "¿Para qué sirve esta hoja?", "What is this sheet for?")) }
        }
    }
}

@Composable
private fun IntakePage(lang: String, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("📄 ${tr(lang, "NOTA DE INGRESO", "INTAKE NOTE")}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            listOf(
                tr(lang, "Identificación", "Identification"),
                tr(lang, "Sistémico / ASA", "Systemic / ASA"),
                tr(lang, "Medicamentos", "Medications"),
                tr(lang, "ATM y músculos", "TMJ and muscles"),
                tr(lang, "Caries y anomalías", "Caries and anomalies"),
                tr(lang, "Oclusión", "Occlusion"),
                tr(lang, "Tejidos blandos / mucosas", "Soft tissues / mucosa"),
                "CPOD / ceod",
                tr(lang, "Periodontal", "Periodontal"),
                tr(lang, "Pulpar", "Pulpal"),
                tr(lang, "Protésico cuando corresponda", "Prosthetic when applicable")
            ).forEach { Text("▸ $it: ____________", style = MaterialTheme.typography.bodyMedium) }
            Spacer(Modifier.height(4.dp))
            Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "Ver cómo se integra", "See how it is assembled")) }
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
    LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { ScreenHeader(tr(lang, "Configuración", "Settings"), onBack) }
        item {
            SectionCard(tr(lang, "Idioma", "Language")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(lang == "es", { onPreferencesChanged(preferences.copy(languageTag = "es")); onLanguageChanged("es") }, { Text("Español") })
                    FilterChip(lang == "en", { onPreferencesChanged(preferences.copy(languageTag = "en")); onLanguageChanged("en") }, { Text("English") })
                }
            }
        }
        item {
            SectionCard(tr(lang, "Tratamiento", "Title")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                        { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) }, { Text("Doctor") })
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                        { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) }, { Text(tr(lang, "Doctora", "Doctor")) })
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
                    FilterChip(preferences.paletteStyle == style,
                        { onPreferencesChanged(preferences.copy(paletteStyle = style)) },
                        { Text(label) }, modifier = Modifier.padding(end = 5.dp, bottom = 5.dp))
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
                        FontStyle.ACCESSIBLE -> tr(lang, "Alta legibilidad", "High readability")
                    }
                    FilterChip(preferences.fontStyle == style,
                        { onPreferencesChanged(preferences.copy(fontStyle = style)) },
                        { Text(label) }, modifier = Modifier.padding(end = 5.dp, bottom = 5.dp))
                }
            }
        }
        item {
            SectionCard(tr(lang, "Vista previa", "Preview")) {
                Text("📁 ${tr(lang, "El expediente del dentista", "The Dentist's Clinical Record")}", style = MaterialTheme.typography.titleLarge)
                Text(tr(lang, "La apariencia cambia al instante y queda guardada en este dispositivo.",
                    "Appearance changes instantly and is stored on this device."))
            }
        }
        item {
            OutlinedButton(onClick = onResetSession, modifier = Modifier.fillMaxWidth()) {
                Text(tr(lang, "Limpiar marcas didácticas de esta sesión", "Clear teaching marks from this session"))
            }
        }
    }
}
