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
fun AppRootV2(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var screen by remember { mutableStateOf(AppScreen.HOME) }

    if (!preferences.onboardingComplete) {
        BackHandler(enabled = true) { }
        OnboardingV2(preferences, onPreferencesChanged, onLanguageChanged) {
            onPreferencesChanged(preferences.copy(onboardingComplete = true))
        }
        return
    }

    BackHandler(enabled = true) {
        screen = when (screen) {
            AppScreen.HOME -> AppScreen.HOME
            AppScreen.SECTION -> AppScreen.FOLDER
            AppScreen.FOLDER -> AppScreen.HOME
            else -> AppScreen.FOLDER
        }
    }

    val folderBack = { screen = AppScreen.FOLDER }
    Crossfade(targetState = screen, label = "folder_v05") { target ->
        when (target) {
            AppScreen.HOME -> FolderCoverV2(
                lang = preferences.languageTag,
                title = preferences.clinicianTitle,
                onOpen = { screen = AppScreen.FOLDER },
                onSettings = { screen = AppScreen.SETTINGS }
            )
            AppScreen.FOLDER -> FolderSpreadV2(
                lang = preferences.languageTag,
                onNavigate = { screen = it },
                onClose = { screen = AppScreen.HOME },
                onSettings = { screen = AppScreen.SETTINGS }
            )
            AppScreen.SETTINGS -> SettingsV2(preferences, onPreferencesChanged, onLanguageChanged, { onSessionChanged(EducationalSession()) }, folderBack)
            AppScreen.IDENTIFICATION -> IdentificationScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY_IDENTIFICATION, AppScreen.HISTORY_REASON, AppScreen.HISTORY_HEREDITARY, AppScreen.HISTORY_NONPATH, AppScreen.HISTORY_GYNECO, AppScreen.HISTORY_PATH, AppScreen.HISTORY_SURGICAL_TRAUMA, AppScreen.HISTORY_PHYSICAL, AppScreen.HISTORY_ORTHO, AppScreen.HISTORY_DENTAL_ALTERATIONS, AppScreen.HISTORY_HABITS, AppScreen.HISTORY_ORAL_EXAM -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.SYSTEMIC_PROTOCOLS -> SystemicProtocols37Screen(preferences.languageTag, folderBack)
            AppScreen.INTAKE -> IntakeNoteScreen(preferences.languageTag, session, folderBack)
            AppScreen.ACTIVITIES -> ActivitiesScreen(preferences.languageTag, folderBack)
            AppScreen.VITALS -> VitalsInteractiveScreen(preferences.languageTag, folderBack)
            AppScreen.ATM -> AtmScreen(preferences.languageTag, folderBack)
            AppScreen.OCCLUSION -> OcclusionScreen(preferences.languageTag, folderBack)
            AppScreen.MUCOSA -> MucosaScreen(preferences.languageTag, folderBack)
            AppScreen.CALCULATORS -> DentalCalculatorsV40Screen(preferences.languageTag, folderBack) 
            AppScreen.AUXILIARIES -> AuxiliariesInteractiveScreen(preferences.languageTag, folderBack)
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
                preferences.languageTag,
                session,
                onOpenPulpal = { screen = AppScreen.PULPAL },
                onOpenApical = { screen = AppScreen.APICAL },
                onBack = folderBack
            )
            AppScreen.PROSTHETIC -> ProstheticSheetScreen(preferences.languageTag, folderBack)
            AppScreen.SURGICAL -> SurgicalSheetScreen(preferences.languageTag, folderBack)
            AppScreen.CONSENT -> ConsentTeachingScreen(preferences.languageTag, folderBack)
            AppScreen.REQUEST -> SimpleEducationalSheet(
                preferences.languageTag,
                "Solicitud de tratamiento",
                "Treatment request",
                "Aprende para qué sirve y qué debe identificar claramente.",
                "Learn its purpose and what it should clearly identify.",
                listOf("Servicio solicitado", "Motivo", "Área u órgano dentario", "Prioridad / referencia", "Responsable y supervisión"),
                listOf("Requested service", "Reason", "Area or tooth", "Priority / referral", "Responsible clinician and supervision"),
                folderBack
            )
            AppScreen.BUDGET -> SimpleEducationalSheet(
                preferences.languageTag,
                "Presupuesto",
                "Budget",
                "Aprende su estructura administrativa sin registrar cobros reales.",
                "Learn its administrative structure without recording real payments.",
                listOf("Procedimiento o concepto", "Cantidad", "Costo unitario", "Subtotal", "Total", "Servicios externos/laboratorio cuando procedan"),
                listOf("Procedure or concept", "Quantity", "Unit cost", "Subtotal", "Total", "External/laboratory services when applicable"),
                folderBack
            )
            AppScreen.EVOLUTION -> EvolutionScreen(preferences.languageTag, session, folderBack)
        }
    }
}

@Composable
private fun OnboardingV2(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    onContinue: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(painterResource(R.drawable.ic_app_logo), null, modifier = Modifier.width(72.dp).height(72.dp))
                Column {
                    Text(tr(preferences.languageTag, "El expediente del dentista", "The Dentist's Clinical Record"), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(tr(preferences.languageTag, "Configura y entra al folder de aprendizaje", "Configure and enter the learning folder"))
                }
            }
        }
        item {
            SectionCard(tr(preferences.languageTag, "Idioma", "Language")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
            SectionCard(tr(preferences.languageTag, "Doctor / Doctora", "Professional title")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTOR, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) }, { Text("Doctor") })
                    FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTORA, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) }, { Text("Doctora") })
                }
            }
        }
        item {
            NoticeCard(tr(preferences.languageTag,
                "Funciona offline y está pensada para acompañar el llenado del expediente físico. No crea ni almacena un expediente clínico digital.",
                "Works offline and is designed to assist completion of the physical record. It does not create or store a digital patient record."
            ))
        }
        item { Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text(tr(preferences.languageTag, "Ir a la portada del folder", "Go to folder cover")) } }
    }
}

@Composable
private fun FolderCoverV2(lang: String, title: ClinicianTitle, onOpen: () -> Unit, onSettings: () -> Unit) {
    val professional = if (title == ClinicianTitle.DOCTORA) "Doctora" else "Doctor"
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 34.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(13.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        OutlinedButton(onClick = onSettings) { Text("⚙") }
                    }
                    Image(painterResource(R.drawable.ic_app_logo), tr(lang, "Logo", "Logo"), Modifier.width(104.dp).height(104.dp))
                    Text(tr(lang, "EL EXPEDIENTE DEL DENTISTA", "THE DENTIST'S CLINICAL RECORD"), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text(tr(lang, "CARPETA DE APRENDIZAJE CLÍNICO", "CLINICAL LEARNING FOLDER"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                    Surface(Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium), color = MaterialTheme.colorScheme.surface) {
                        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("$professional · ${tr(lang, "Consulta offline al lado del expediente físico", "Offline chairside reference")}", fontWeight = FontWeight.Bold)
                            Text(tr(lang, "Explorar → medir → marcar → analizar → comprender → registrar", "Examine → measure → mark → analyze → understand → record"))
                            Text(tr(lang, "Índices y diagnósticos continúan siendo interactivos.", "Indices and diagnostic modules remain interactive."))
                        }
                    }
                    Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text("📂 ${tr(lang, "Abrir expediente", "Open folder")}") }
                }
            }
            Card(
                modifier = Modifier.width(190.dp).height(55.dp).align(Alignment.TopStart).padding(start = 14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(tr(lang, "EXPEDIENTE CLÍNICO", "CLINICAL RECORD"), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private data class TabV2(val screen: AppScreen, val icon: String, val es: String, val en: String)

private fun treatmentTabsV2() = listOf(
    TabV2(AppScreen.TREATMENT, "📝", "Dx / Tx", "Dx / Tx"),
    TabV2(AppScreen.SESSIONS, "🗓", "Por sesiones", "By sessions"),
    TabV2(AppScreen.ENDO, "⚡", "Endodoncia", "Endodontics"),
    TabV2(AppScreen.PROSTHETIC, "🦷", "Prótesis", "Prosthetics"),
    TabV2(AppScreen.PERIODONTOGRAM, "📈", "Periodonto", "Periodontal"),
    TabV2(AppScreen.SURGICAL, "✚", "Cirugía", "Surgery"),
    TabV2(AppScreen.OLEARY, "🔴", "O’Leary", "O’Leary"),
    TabV2(AppScreen.EVOLUTION, "📋", "Evolución", "Progress")
)

private fun recordTabsV2() = listOf(
    TabV2(AppScreen.IDENTIFICATION, "👤", "Ficha", "ID"),
    TabV2(AppScreen.HISTORY, "🩺", "Historia", "History"),
    TabV2(AppScreen.VITALS, "❤️", "Signos vitales", "Vitals"),
    TabV2(AppScreen.ATM, "◉", "ATM", "TMJ"),
    TabV2(AppScreen.OCCLUSION, "↔", "Oclusión", "Occlusion"),
    TabV2(AppScreen.MUCOSA, "👄", "Mucosas", "Mucosa"),
    TabV2(AppScreen.AUXILIARIES, "🧪", "Análisis", "Tests"),
    TabV2(AppScreen.ODONTOGRAM, "🦷", "Odontograma", "Odontogram"),
    TabV2(AppScreen.ICDAS, "🔎", "ICDAS", "ICDAS"),
    TabV2(AppScreen.CPOD, "➕", "CPOD/ceod", "DMFT/dmft"),
    TabV2(AppScreen.IPC, "6️⃣", "IPC", "CPI"),
    TabV2(AppScreen.IHOS, "🪥", "IHOS", "OHI-S"),
    TabV2(AppScreen.POSTURE, "🧍", "Postura", "Posture"),
    TabV2(AppScreen.CONSENT, "✍", "Consentimiento", "Consent"),
    TabV2(AppScreen.REQUEST, "📨", "Solicitud", "Request"),
    TabV2(AppScreen.BUDGET, "$", "Presupuesto", "Budget")
)

private data class IntakeV2(val titleEs: String, val titleEn: String, val helpEs: String, val helpEn: String, val exampleEs: String, val exampleEn: String)

private fun intakeV2() = listOf(
    IntakeV2("Identificación", "Identification", "Resume los datos de identificación y contexto necesarios en el expediente físico.", "Summarizes identifying and contextual information in the physical record.", "Adolescente en dentición permanente joven.", "Adolescent in young permanent dentition."),
    IntakeV2("Sistémico / ASA", "Systemic / ASA", "Resume antecedentes relevantes y la clasificación ASA que corresponda tras valoración.", "Summarizes relevant history and the appropriate ASA classification after assessment.", "ASA II cuando el escenario educativo corresponde a enfermedad sistémica leve controlada.", "ASA II when the teaching scenario matches controlled mild systemic disease."),
    IntakeV2("Medicamentos", "Medications", "Anota medicamentos actuales y relaciona posibles implicaciones odontológicas.", "Record current medication and relate possible dental implications.", "Antihipertensivo referido; revisar control y posibles interacciones.", "Reported antihypertensive; review control and interactions."),
    IntakeV2("ATM y músculos", "TMJ and muscles", "Resume apertura, trayectoria, dolor, ruidos y músculos explorados.", "Summarizes opening, path, pain, sounds and examined muscles.", "Apertura simétrica, sin dolor ni ruidos.", "Symmetric opening without pain or sounds."),
    IntakeV2("Oclusión", "Occlusion", "Integra dentición, relación molar/canina, plano terminal cuando aplique, líneas medias, overjet, overbite y mordidas.", "Integrates dentition, molar/canine relation, terminal plane when applicable, midlines, overjet, overbite and bite findings.", "Clase I molar con overjet aumentado.", "Class I molar relation with increased overjet."),
    IntakeV2("Mucosas", "Mucosa", "Resume tejidos blandos normales y describe cualquier lesión con localización y características.", "Summarizes normal soft tissues and describes lesions by site and features.", "Mucosas rosadas, húmedas e íntegras, sin lesión aparente.", "Pink, moist, intact mucosa without apparent lesion."),
    IntakeV2("Caries / índices", "Caries / indices", "Integra odontograma, ICDAS, CPOD/ceod, O’Leary, IHOS e IPC según corresponda.", "Integrates odontogram, ICDAS, DMFT/dmft, O’Leary, OHI-S and CPI as appropriate.", "OD 26 ICDAS 5; registrar además el índice que corresponda.", "Tooth 26 ICDAS 5; also record the appropriate index."),
    IntakeV2("Periodontal", "Periodontal", "Resume hallazgos de sondaje, sangrado, placa, cálculo, movilidad, furcas y diagnóstico periodontal.", "Summarizes probing, bleeding, plaque, calculus, mobility, furcation and periodontal diagnosis.", "Hallazgos compatibles con inflamación gingival; integrar diagnóstico completo.", "Findings compatible with gingival inflammation; integrate full diagnosis."),
    IntakeV2("Pulpar / periapical", "Pulpal / periapical", "Registra un diagnóstico pulpar y uno periapical sustentados por signos, síntomas, pruebas e imagen.", "Record one pulpal and one periapical diagnosis supported by signs, symptoms, tests and imaging.", "Necrosis pulpar + periodontitis apical asintomática.", "Pulp necrosis + asymptomatic apical periodontitis."),
    IntakeV2("Protésico", "Prosthetic", "Resume edentulismo, clasificación de Kennedy cuando aplique y necesidades protésicas.", "Summarizes edentulism, Kennedy class when applicable and prosthetic needs.", "Espacio edéntulo limitado por dientes: revisar Kennedy y reglas de Applegate.", "Bounded edentulous space: review Kennedy and Applegate rules.")
)

@Composable
private fun FolderSpreadV2(lang: String, onNavigate: (AppScreen) -> Unit, onClose: () -> Unit, onSettings: () -> Unit) {
    var selectedIntake by remember { mutableStateOf<Int?>(null) }
    val intake = intakeV2()
    Column(Modifier.fillMaxSize().padding(top = 8.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onClose) { Text("📁 ${tr(lang, "Portada", "Cover")}") }
            Text(tr(lang, "Expediente abierto", "Open folder"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedButton(onClick = onSettings) { Text("⚙") }
        }

        QuickClinicalAccess(lang, onNavigate)

        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp)) {
            if (maxWidth >= 720.dp) {
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ActivitiesPageV2(lang, { onNavigate(AppScreen.ACTIVITIES) }, Modifier.weight(1f))
                    IntakePageV2(lang, intake, selectedIntake, { selectedIntake = it }, { onNavigate(AppScreen.INTAKE) }, Modifier.weight(1f))
                }
            } else {
                Row(Modifier.fillMaxSize().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ActivitiesPageV2(lang, { onNavigate(AppScreen.ACTIVITIES) }, Modifier.width(330.dp))
                    IntakePageV2(lang, intake, selectedIntake, { selectedIntake = it }, { onNavigate(AppScreen.INTAKE) }, Modifier.width(330.dp))
                }
            }
        }

        Text(tr(lang, "TRATAMIENTO", "TREATMENT"), modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        TabRowV2(lang, treatmentTabsV2(), onNavigate)
        Text(tr(lang, "HOJAS Y ANÁLISIS", "SHEETS AND ANALYSES"), modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        TabRowV2(lang, recordTabsV2(), onNavigate)
        Spacer(Modifier.height(5.dp))
    }
}

@Composable
private fun TabRowV2(lang: String, tabs: List<TabV2>, onNavigate: (AppScreen) -> Unit) {
    LazyRow(Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 8.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
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
private fun ActivitiesPageV2(lang: String, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("📋 ${tr(lang, "REGISTRO DE ACTIVIDADES", "ACTIVITY RECORD")}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(tr(lang, "Hoja izquierda: permite relacionar lo planeado, autorizado, realizado y supervisado.", "Left page: relates planned, authorized, performed and supervised work."))
            Text("${tr(lang, "Alumno", "Student")} | ${tr(lang, "Actividad", "Activity")} | ✓ | ${tr(lang, "Realizada", "Done")} | ${tr(lang, "Supervisión", "Supervision")}", style = MaterialTheme.typography.bodySmall)
            repeat(8) { Text("______ | ______ | ___ | ______ | ______", style = MaterialTheme.typography.bodySmall) }
            Spacer(Modifier.weight(1f))
            Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "Aprender a llenarlo", "Learn how to complete it")) }
        }
    }
}

@Composable
private fun IntakePageV2(
    lang: String,
    fields: List<IntakeV2>,
    selected: Int?,
    onSelected: (Int?) -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("📄 ${tr(lang, "NOTA DE INGRESO", "INTAKE NOTE")}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(tr(lang, "Toca cada renglón para ver qué va ahí y un ejemplo.", "Tap each line to see what belongs there and an example."), style = MaterialTheme.typography.bodySmall)
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                fields.forEachIndexed { index, field ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onSelected(if (selected == index) null else index) },
                        colors = CardDefaults.cardColors(containerColor = if (selected == index) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.padding(7.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(if (lang == "en") field.titleEn else field.titleEs, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            if (selected == index) {
                                Text(if (lang == "en") field.helpEn else field.helpEs, style = MaterialTheme.typography.bodySmall)
                                Text("✍️ ${if (lang == "en") field.exampleEn else field.exampleEs}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
            OutlinedButton(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "Guía completa de ingreso", "Full intake guide")) }
        }
    }
}

@Composable
private fun SettingsV2(
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
                        FontStyle.CLASSIC -> tr(lang, "Clásica", "Classic")
                        FontStyle.HANDWRITTEN -> tr(lang, "Manuscrita", "Handwritten")
                        FontStyle.COMPACT -> tr(lang, "Compacta", "Compact")
                    }
                    FilterChip(preferences.fontStyle == style, { onPreferencesChanged(preferences.copy(fontStyle = style)) }, { Text(label) }, modifier = Modifier.padding(end = 4.dp, bottom = 4.dp))
                }
            }
        }
        item {
            NoticeCard(tr(lang,
                "Los datos escritos en los comprobadores interactivos se usan solo mientras la pantalla está abierta. No forman un expediente de paciente.",
                "Values entered in interactive checkers are used only while the screen is open. They do not form a patient record."
            ))
        }
        item { OutlinedButton(onClick = onResetSession, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "Limpiar marcas didácticas", "Clear teaching marks")) } }
    }
}
