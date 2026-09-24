package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
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
        BackHandler(enabled = true) { }
        OnboardingScreen(
            preferences = preferences,
            onPreferencesChanged = onPreferencesChanged,
            onLanguageChanged = onLanguageChanged,
            onContinue = { onPreferencesChanged(preferences.copy(onboardingComplete = true)) }
        )
        return
    }

    BackHandler(enabled = true) {
        screen = when (screen) {
            AppScreen.HOME -> AppScreen.HOME
            AppScreen.FOLDER -> AppScreen.HOME
            else -> AppScreen.FOLDER
        }
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
            AppScreen.SETTINGS -> SettingsScreen(preferences, onPreferencesChanged, onLanguageChanged, { onSessionChanged(EducationalSession()) }, folderBack)
            AppScreen.IDENTIFICATION -> IdentificationScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY_IDENTIFICATION, AppScreen.HISTORY_REASON, AppScreen.HISTORY_HEREDITARY, AppScreen.HISTORY_NONPATH, AppScreen.HISTORY_GYNECO, AppScreen.HISTORY_PATH, AppScreen.HISTORY_SURGICAL_TRAUMA, AppScreen.HISTORY_PHYSICAL, AppScreen.HISTORY_ORTHO, AppScreen.HISTORY_DENTAL_ALTERATIONS, AppScreen.HISTORY_HABITS, AppScreen.HISTORY_ORAL_EXAM -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.SYSTEMIC_PROTOCOLS -> SystemicProtocols37Screen(preferences.languageTag, folderBack)
            AppScreen.INTAKE -> IntakeNoteScreen(preferences.languageTag, session, folderBack)
            AppScreen.ACTIVITIES -> ActivitiesScreen(preferences.languageTag, folderBack)
            AppScreen.VITALS -> VitalsTeachingScreen(preferences.languageTag, folderBack)
            AppScreen.ATM -> AtmScreen(preferences.languageTag, folderBack)
            AppScreen.OCCLUSION -> OcclusionScreen(preferences.languageTag, folderBack)
            AppScreen.MUCOSA -> MucosaScreen(preferences.languageTag, folderBack)
            AppScreen.CALCULATORS -> DentalCalculatorsV40Screen(preferences.languageTag, folderBack) 
            AppScreen.AUXILIARIES -> AuxiliariesScreen(preferences.languageTag, folderBack)
            AppScreen.ODONTOGRAM -> OdontogramScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.ICDAS -> IcdasScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.CPOD -> CpodScreen(preferences.languageTag, session, folderBack)
            AppScreen.OLEARY -> OlearyScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.IPC -> IpcScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.IHOS -> IhosScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.POSTURE -> PostureVisualScreen(preferences.languageTag, folderBack)
            AppScreen.PULPAL -> PulpalScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.APICAL -> ApicalScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.TREATMENT -> TreatmentScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.SESSIONS -> TreatmentBySessionsScreen(preferences.languageTag, session, folderBack)
            AppScreen.ENDO -> EndodonticSheetScreen(
                preferences.languageTag, session,
                onOpenPulpal = { screen = AppScreen.PULPAL },
                onOpenApical = { screen = AppScreen.APICAL },
                onBack = folderBack
            )
            AppScreen.PROSTHETIC -> ProstheticSheetScreen(preferences.languageTag, folderBack)
            AppScreen.SURGICAL -> SurgicalSheetScreen(preferences.languageTag, folderBack)
            AppScreen.CONSENT -> ConsentTeachingScreen(preferences.languageTag, folderBack)
            AppScreen.REQUEST -> SimpleEducationalSheet(
                preferences.languageTag, "Solicitud de tratamiento", "Treatment request",
                "Aprende para qué sirve y qué debe identificar claramente.", "Learn its purpose and what it should clearly identify.",
                listOf("Servicio solicitado", "Motivo", "Área u órgano dentario", "Prioridad / referencia", "Responsable y supervisión"),
                listOf("Requested service", "Reason", "Area or tooth", "Priority / referral", "Responsible clinician and supervision"), folderBack
            )
            AppScreen.BUDGET -> SimpleEducationalSheet(
                preferences.languageTag, "Presupuesto", "Budget",
                "Aprende su estructura administrativa sin registrar cobros reales.", "Learn its administrative structure without recording real payments.",
                listOf("Procedimiento o concepto", "Cantidad", "Costo unitario", "Subtotal", "Total", "Servicios externos/laboratorio cuando procedan"),
                listOf("Procedure or concept", "Quantity", "Unit cost", "Subtotal", "Total", "External/laboratory services when applicable"), folderBack
            )
            AppScreen.EVOLUTION -> EvolutionScreen(preferences.languageTag, session, folderBack)
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
    LazyColumn(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(painterResource(R.drawable.ic_app_logo), null, modifier = Modifier.width(64.dp).height(64.dp))
                Column {
                    Text(tr(preferences.languageTag, "El expediente del dentista", "The Dentist's Clinical Record"), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(tr(preferences.languageTag, "Configura solo lo necesario para empezar", "Set only what is needed to begin"))
                }
            }
        }
        item {
            SectionCard(tr(preferences.languageTag, "Idioma", "Language")) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(preferences.languageTag == "es", { onPreferencesChanged(preferences.copy(languageTag = "es")); onLanguageChanged("es") }, { Text("Español") })
                    FilterChip(preferences.languageTag == "en", { onPreferencesChanged(preferences.copy(languageTag = "en")); onLanguageChanged("en") }, { Text("English") })
                }
            }
        }
        item {
            SectionCard(tr(preferences.languageTag, "Tratamiento profesional", "Professional title")) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTOR, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) }, { Text("Doctor") })
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTORA, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) }, { Text("Doctora") })
                }
            }
        }
        item { NoticeCard(tr(preferences.languageTag, "Uso educativo. No captura expedientes ni datos reales de pacientes.", "Educational use. It does not capture real patient records or personal data.")) }
        item { Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text(tr(preferences.languageTag, "Continuar a la portada", "Continue to folder cover")) } }
    }
}

@Composable
private fun FolderCoverScreen(lang: String, title: ClinicianTitle, onOpen: () -> Unit, onSettings: () -> Unit) {
    val professional = if (title == ClinicianTitle.DOCTORA) "Doctora" else "Doctor"
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(18.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { OutlinedButton(onClick = onSettings) { Text("⚙") } }
                    Image(painterResource(R.drawable.ic_app_logo), tr(lang, "Logo de la app", "App logo"), Modifier.width(96.dp).height(96.dp))
                    Text(tr(lang, "EL EXPEDIENTE DEL DENTISTA", "THE DENTIST'S CLINICAL RECORD"), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text(tr(lang, "CARPETA DE APRENDIZAJE", "LEARNING FOLDER"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Surface(Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium), color = MaterialTheme.colorScheme.surface) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            Text("$professional · ${tr(lang, "Modo aprender", "Learn mode")}", fontWeight = FontWeight.Bold)
                            Text(tr(lang, "Ficha · Historia · Signos vitales · ATM · Oclusión · Mucosas", "ID · History · Vitals · TMJ · Occlusion · Mucosa"))
                            Text("Odontograma · ICDAS · CPOD/ceod · O’Leary · IPC · IHOS")
                            Text(tr(lang, "Endodoncia · Prótesis · Cirugía · Evolución", "Endodontics · Prosthetics · Surgery · Progress notes"))
                        }
                    }
                    Text(tr(lang, "Toca “Abrir expediente” para desplegar las dos hojas y sus separadores.", "Tap “Open folder” to unfold the two pages and tabs."), textAlign = TextAlign.Center)
                    Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text("📂 ${tr(lang, "Abrir expediente", "Open folder")}") }
                }
            }
            Card(
                modifier = Modifier.width(170.dp).height(50.dp).align(Alignment.TopStart).padding(start = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(tr(lang, "EXPEDIENTE", "CLINICAL FOLDER"), fontWeight = FontWeight.Bold) } }
        }
    }
}

private data class FolderTab(val screen: AppScreen, val icon: String, val es: String, val en: String)

private fun treatmentTabs() = listOf(
    FolderTab(AppScreen.ACTIVITIES, "✓", "Autorización", "Authorization"),
    FolderTab(AppScreen.TREATMENT, "📝", "Dx / Tx", "Dx / Tx"),
    FolderTab(AppScreen.SESSIONS, "🗓", "Por sesiones", "By sessions"),
    FolderTab(AppScreen.ENDO, "⚡", "Endodoncia", "Endodontics"),
    FolderTab(AppScreen.PROSTHETIC, "🦷", "Prótesis", "Prosthetics"),
    FolderTab(AppScreen.PERIODONTOGRAM, "📈", "Periodonto", "Periodontal"),
    FolderTab(AppScreen.SURGICAL, "✚", "Cirugía", "Surgery"),
    FolderTab(AppScreen.OLEARY, "🔴", "O’Leary", "O’Leary")
)

private fun recordTabs() = listOf(
    FolderTab(AppScreen.IDENTIFICATION, "👤", "Ficha", "ID"),
    FolderTab(AppScreen.HISTORY, "🩺", "Historia", "History"),
    FolderTab(AppScreen.VITALS, "❤️", "Signos vitales", "Vitals"),
    FolderTab(AppScreen.ATM, "◉", "ATM", "TMJ"),
    FolderTab(AppScreen.OCCLUSION, "↔", "Oclusión", "Occlusion"),
    FolderTab(AppScreen.MUCOSA, "👄", "Mucosas", "Mucosa"),
    FolderTab(AppScreen.AUXILIARIES, "🩻", "Auxiliares", "Aids"),
    FolderTab(AppScreen.ODONTOGRAM, "🦷", "Odontograma", "Odontogram"),
    FolderTab(AppScreen.ICDAS, "🔎", "ICDAS", "ICDAS"),
    FolderTab(AppScreen.CPOD, "➕", "CPOD/ceod", "DMFT/dmft"),
    FolderTab(AppScreen.IPC, "6️⃣", "IPC", "CPI"),
    FolderTab(AppScreen.IHOS, "🪥", "IHOS", "OHI-S"),
    FolderTab(AppScreen.POSTURE, "🧍", "Postura", "Posture"),
    FolderTab(AppScreen.CONSENT, "✍", "Consentimiento", "Consent"),
    FolderTab(AppScreen.REQUEST, "📨", "Solicitud", "Request"),
    FolderTab(AppScreen.BUDGET, "$", "Presupuesto", "Budget"),
    FolderTab(AppScreen.EVOLUTION, "📋", "Evolución", "Progress")
)

private data class IntakeField(val titleEs: String, val titleEn: String, val helpEs: String, val helpEn: String, val exampleEs: String, val exampleEn: String)
private fun intakeFields() = listOf(
    IntakeField("Identificación", "Identification", "Resume los datos necesarios para identificar a la persona y contextualizar edad/dentición. En esta app no se capturan datos reales.", "Summarizes identifying/contextual information. This app does not capture real patient data.", "Ejemplo didáctico: adolescente en dentición permanente joven.", "Teaching example: adolescent in young permanent dentition."),
    IntakeField("Sistémico / ASA", "Systemic / ASA", "Integra antecedentes relevantes y clasificación ASA educativa.", "Integrates relevant medical history and educational ASA class.", "Ejemplo: ASA II por enfermedad sistémica leve controlada.", "Example: ASA II for controlled mild systemic disease."),
    IntakeField("Medicamentos", "Medications", "Se anotan fármacos de uso actual relevantes para la atención y posibles interacciones/riesgos.", "Record current medications relevant to care and possible interactions/risks.", "Ejemplo: antihipertensivo referido por el paciente.", "Example: patient-reported antihypertensive."),
    IntakeField("ATM y músculos", "TMJ and muscles", "Resume apertura, dolor, ruidos articulares, trayectoria y hallazgos musculares.", "Summarizes opening, pain, joint sounds, path and muscle findings.", "Ejemplo: apertura simétrica, sin dolor ni ruidos.", "Example: symmetric opening without pain or sounds."),
    IntakeField("Caries y anomalías", "Caries and anomalies", "Resume lesiones de caries y anomalías relevantes de número, forma, tamaño, estructura o posición.", "Summarizes caries and relevant anomalies of number, shape, size, structure or position.", "Ejemplo: lesión ICDAS 5 en OD 26 y giroversión anterior.", "Example: ICDAS 5 lesion on tooth 26 and anterior rotation."),
    IntakeField("Oclusión", "Occlusion", "Integra dentición, Angle/plano terminal cuando corresponda, relación canina, líneas medias, overjet/overbite y alteraciones transversales/verticales.", "Integrates dentition, Angle/terminal plane when applicable, canine relation, midlines, overjet/overbite and transverse/vertical findings.", "Ejemplo: Clase I molar con overjet aumentado.", "Example: Class I molar relation with increased overjet."),
    IntakeField("Mucosas", "Oral mucosa", "Resume hallazgos peribucales e intrabucales y describe cualquier lesión relevante.", "Summarizes perioral/intraoral soft-tissue findings and any relevant lesion.", "Ejemplo: mucosas rosadas, húmedas e íntegras, sin lesiones aparentes.", "Example: pink, moist and intact mucosa without apparent lesions."),
    IntakeField("CPOD / ceod", "DMFT / dmft", "Anota el resultado de los índices según dentición permanente o temporal.", "Record the index result for permanent or primary dentition.", "Ejemplo: CPOD = C + P + O.", "Example: DMFT = D + M + F."),
    IntakeField("Periodontal", "Periodontal", "Resume el diagnóstico periodontal a partir de examen, IPC/periodontograma y hallazgos complementarios.", "Summarizes periodontal diagnosis from examination, CPI/chart and complementary findings.", "Ejemplo: gingivitis asociada a placa; requiere correlación completa.", "Example: plaque-associated gingivitis; requires complete correlation."),
    IntakeField("Pulpar / periapical", "Pulpal / periapical", "En dientes indicados se registra un diagnóstico pulpar y uno periapical, sustentados por síntomas, pruebas e imagen.", "For indicated teeth record one pulpal and one periapical diagnosis supported by symptoms, tests and imaging.", "Ejemplo: necrosis pulpar + periodontitis apical asintomática.", "Example: pulp necrosis + asymptomatic apical periodontitis."),
    IntakeField("Protésico / Kennedy", "Prosthetic / Kennedy", "Cuando hay edentulismo parcial se resume el diagnóstico protésico y la clasificación de Kennedy aplicable.", "For partial edentulism summarize the prosthetic diagnosis and applicable Kennedy class.", "Ejemplo: Kennedy Clase III en espacio edéntulo limitado por dientes.", "Example: Kennedy Class III for a bounded edentulous space.")
)

@Composable
private fun FolderSpreadScreen(lang: String, onNavigate: (AppScreen) -> Unit, onClose: () -> Unit, onSettings: () -> Unit) {
    var intakeSelected by remember { mutableStateOf<Int?>(null) }
    val fields = intakeFields()
    Column(Modifier.fillMaxSize().padding(top = 8.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onClose) { Text("📁 ${tr(lang, "Portada", "Cover")}") }
            Text(tr(lang, "Expediente abierto", "Open clinical folder"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            OutlinedButton(onClick = onSettings) { Text("⚙") }
        }
        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth().padding(10.dp)) {
            if (maxWidth >= 720.dp) {
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    ActivityPage(lang, { onNavigate(AppScreen.ACTIVITIES) }, Modifier.weight(1f))
                    IntakeInteractivePage(lang, fields, intakeSelected, { intakeSelected = it }, { onNavigate(AppScreen.INTAKE) }, Modifier.weight(1f))
                }
            } else {
                Row(Modifier.fillMaxSize().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    ActivityPage(lang, { onNavigate(AppScreen.ACTIVITIES) }, Modifier.width(330.dp))
                    IntakeInteractivePage(lang, fields, intakeSelected, { intakeSelected = it }, { onNavigate(AppScreen.INTAKE) }, Modifier.width(330.dp))
                }
            }
        }
        Text(tr(lang, "SEPARADORES DE TRATAMIENTO", "TREATMENT TABS"), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp), style = MaterialTheme.typography.labelMedium)
        FolderTabRow(lang, treatmentTabs(), onNavigate)
        Text(tr(lang, "HOJAS DEL EXPEDIENTE", "RECORD SHEETS"), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp), style = MaterialTheme.typography.labelMedium)
        FolderTabRow(lang, recordTabs(), onNavigate)
        Spacer(Modifier.height(6.dp))
    }
}

@Composable
private fun FolderTabRow(lang: String, tabs: List<FolderTab>, onNavigate: (AppScreen) -> Unit) {
    LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp), contentPadding = PaddingValues(horizontal = 10.dp)) {
        items(tabs) { tab ->
            Card(onClick = { onNavigate(tab.screen) }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(horizontal = 10.dp, vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(tab.icon)
                    Text(if (lang == "en") tab.en else tab.es, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun ActivityPage(lang: String, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("📋 ${tr(lang, "REGISTRO DE ACTIVIDADES", "ACTIVITY RECORD")}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(tr(lang, "Hoja izquierda del expediente: bitácora operativa de lo planeado, autorizado, realizado y supervisado.", "Left-hand page: operational log of planned, authorized, performed and supervised work."))
            Text("Alumno | Actividad | Autorización | Realizada | Supervisión", style = MaterialTheme.typography.bodySmall)
            repeat(8) { Text("______ | ______ | ______ | ______ | ______", style = MaterialTheme.typography.bodySmall) }
            Spacer(Modifier.weight(1f))
            Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "¿Qué significa cada columna?", "What does each column mean?")) }
        }
    }
}

@Composable
private fun IntakeInteractivePage(lang: String, fields: List<IntakeField>, selected: Int?, onSelected: (Int?) -> Unit, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("📄 ${tr(lang, "NOTA DE INGRESO", "INTAKE NOTE")}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(tr(lang, "Toca cada apartado para aprender qué va ahí y ver un ejemplo.", "Tap each field to learn what belongs there and see an example."), style = MaterialTheme.typography.bodySmall)
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                fields.forEachIndexed { index, f ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onSelected(if (selected == index) null else index) },
                        colors = CardDefaults.cardColors(containerColor = if (selected == index) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.padding(7.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(if (lang == "en") f.titleEn else f.titleEs, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                            if (selected == index) {
                                Text(if (lang == "en") f.helpEn else f.helpEs, style = MaterialTheme.typography.bodySmall)
                                Text("✍️ ${if (lang == "en") f.exampleEn else f.exampleEs}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
            OutlinedButton(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "Abrir guía completa", "Open full guide")) }
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
    LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
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
            SectionCard(tr(lang, "Doctor / Doctora", "Professional title")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTOR, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) }, { Text("Doctor") })
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTORA, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) }, { Text("Doctora") })
                }
            }
        }
        item {
            SectionCard(tr(lang, "Paleta", "Palette")) {
                PaletteStyle.entries.forEach { style ->
                    val label = when (style) {
                        PaletteStyle.WOOD -> tr(lang, "Madera clásica", "Classic wood")
                        PaletteStyle.CLINICAL_GREEN -> tr(lang, "Verde clínico", "Clinical green")
                        PaletteStyle.DENTAL_BLUE -> tr(lang, "Azul dental", "Dental blue")
                        PaletteStyle.WINE -> tr(lang, "Vino", "Wine")
                        PaletteStyle.SAGE -> tr(lang, "Salvia", "Sage")
                        PaletteStyle.MONO -> tr(lang, "Blanco/negro", "Monochrome")
                    }
                    FilterChip(preferences.paletteStyle == style, { onPreferencesChanged(preferences.copy(paletteStyle = style)) }, { Text(label) }, modifier = Modifier.padding(end = 4.dp, bottom = 4.dp))
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
                    FilterChip(preferences.fontStyle == style, { onPreferencesChanged(preferences.copy(fontStyle = style)) }, { Text(label) }, modifier = Modifier.padding(end = 4.dp, bottom = 4.dp))
                }
            }
        }
        item { OutlinedButton(onClick = onResetSession, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "Limpiar marcas didácticas", "Clear teaching marks")) } }
    }
}
