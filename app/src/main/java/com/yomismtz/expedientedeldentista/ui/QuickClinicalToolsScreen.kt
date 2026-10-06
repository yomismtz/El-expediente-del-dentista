package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

private data class QuickClinicalTool(
    val icon: String,
    val es: String,
    val en: String,
    val screen: AppScreen
)

private data class QuickClinicalCategory(
    val icon: String,
    val es: String,
    val en: String,
    val tools: List<QuickClinicalTool>
)

private val quickClinicalCategories = listOf(
    QuickClinicalCategory(
        "🧮", "Evaluación y medición", "Assessment and measurement",
        listOf(
            QuickClinicalTool("🧮", "Calculadoras clínicas", "Clinical calculators", AppScreen.CALCULATORS),
            QuickClinicalTool("❤️", "Signos vitales y triage", "Vital signs and triage", AppScreen.VITALS),
            QuickClinicalTool("🔎", "ICDAS", "ICDAS", AppScreen.ICDAS),
            QuickClinicalTool("📊", "CPOD / DMFT", "DMFT", AppScreen.CPOD),
            QuickClinicalTool("🪥", "Índice de O'Leary", "O'Leary plaque index", AppScreen.OLEARY),
            QuickClinicalTool("🦠", "Índice de placa comunitario (IPC)", "Community plaque index", AppScreen.IPC),
            QuickClinicalTool("🧼", "IHOS", "Simplified oral hygiene index", AppScreen.IHOS),
            QuickClinicalTool("📈", "Periodontograma", "Periodontogram", AppScreen.PERIODONTOGRAM)
        )
    ),
    QuickClinicalCategory(
        "🦷", "Diagnóstico clínico", "Clinical diagnosis",
        listOf(
            QuickClinicalTool("🦷", "Odontograma", "Odontogram", AppScreen.ODONTOGRAM),
            QuickClinicalTool("⚡", "Evaluación pulpar y periapical", "Pulpal and periapical assessment", AppScreen.PULPAL),
            QuickClinicalTool("🦷", "ATM", "TMJ", AppScreen.ATM),
            QuickClinicalTool("↔️", "Oclusión", "Occlusion", AppScreen.OCCLUSION),
            QuickClinicalTool("👄", "Mucosas y tejidos blandos", "Mucosa and soft tissues", AppScreen.MUCOSA),
            QuickClinicalTool("🧍", "Postura craneocervical", "Craniocervical posture", AppScreen.POSTURE),
            QuickClinicalTool("🛡️", "Caries y riesgo de caries", "Caries and caries risk", AppScreen.CARIES_RISK)
        )
    ),
    QuickClinicalCategory(
        "💊", "Medicamentos y anestesia", "Medications and anesthesia",
        listOf(
            QuickClinicalTool("💊", "Medicamentos y referencias por peso", "Medications and weight references", AppScreen.MEDICATIONS),
            QuickClinicalTool("💉", "Anestésicos y cálculo por peso", "Local anesthetics and weight calculation", AppScreen.MEDICATIONS)
        )
    ),
    QuickClinicalCategory(
        "⚕️", "Tratamiento", "Treatment",
        listOf(
            QuickClinicalTool("⚕️", "Diagnóstico y tratamiento por diente", "Diagnosis and treatment by tooth", AppScreen.TREATMENT),
            QuickClinicalTool("🗓️", "Plan de tratamiento por sesiones", "Treatment plan by sessions", AppScreen.SESSIONS),
            QuickClinicalTool("🧩", "Operatoria y restauración", "Operative and restorative dentistry", AppScreen.OPERATORIA),
            QuickClinicalTool("⚡", "Ficha endodóntica", "Endodontic sheet", AppScreen.ENDO)
        )
    ),
    QuickClinicalCategory(
        "🚨", "Urgencias y protocolos", "Emergencies and protocols",
        listOf(
            QuickClinicalTool("🚨", "Urgencias odontológicas", "Dental emergencies", AppScreen.EMERGENCY),
            QuickClinicalTool("📚", "Protocolos sistémicos", "Systemic protocols", AppScreen.SYSTEMIC_PROTOCOLS)
        )
    ),
    QuickClinicalCategory(
        "🧒", "Odontopediatría", "Pediatric dentistry",
        listOf(
            QuickClinicalTool("🧒", "Odontopediatría", "Pediatric dentistry", AppScreen.PEDIATRIC_DENTISTRY)
        )
    ),
    QuickClinicalCategory(
        "🔩", "Implantología", "Implantology",
        listOf(
            QuickClinicalTool("🔩", "Implantología", "Implantology", AppScreen.IMPLANTOLOGY)
        )
    ),
    QuickClinicalCategory(
        "📷", "Documentación clínica", "Clinical documentation",
        listOf(
            QuickClinicalTool("📷", "Fotografía clínica", "Clinical photography", AppScreen.CLINICAL_PHOTO)
        )
    )
)

@Composable
fun QuickClinicalToolsScreen(
    lang: String,
    onExit: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf<QuickClinicalCategory?>(null) }
    var selectedTool by remember { mutableStateOf<QuickClinicalTool?>(null) }
    var quickSession by remember { mutableStateOf(EducationalSession()) }

    val backToCategories = { selectedCategory = null }
    val backToCategory = { selectedTool = null }

    BackHandler(enabled = selectedTool != null) { backToCategory() }
    BackHandler(enabled = selectedTool == null && selectedCategory != null) { backToCategories() }
    BackHandler(enabled = selectedTool == null && selectedCategory == null) { onExit() }

    when {
        selectedTool != null -> QuickClinicalToolContent(
            lang = lang,
            tool = selectedTool!!,
            session = quickSession,
            onSessionChanged = { quickSession = it },
            onBack = backToCategory
        )

        selectedCategory != null -> {
            val category = selectedCategory!!
            Column(
                Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(onClick = backToCategories) {
                    Text("‹ " + tr(lang, "Categorías", "Categories"))
                }
                Text(
                    category.icon + " " + tr(lang, category.es, category.en),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
                Text(
                    tr(
                        lang,
                        "Selecciona una herramienta. Cada una se abre en su propia pantalla.",
                        "Select a tool. Each tool opens on its own screen."
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
                category.tools.forEach { tool ->
                    Card(
                        onClick = { selectedTool = tool },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            Modifier.padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                tool.icon + " " + tr(lang, tool.es, tool.en),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                tr(lang, "Abrir herramienta", "Open tool"),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        else -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "🧰 " + tr(lang, "Herramientas clínicas", "Clinical tools"),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
                Text(
                    tr(
                        lang,
                        "Modo clínico rápido. Primero elige una categoría y después la herramienta.",
                        "Quick clinical mode. Choose a category first, then the tool."
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "🔒 " + tr(
                            lang,
                            "Lo que hagas aquí es temporal: no se guarda en ningún expediente.",
                            "Anything you do here is temporary: it is not saved to a patient record."
                        ),
                        Modifier.padding(14.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                quickClinicalCategories.forEach { category ->
                    Card(
                        onClick = { selectedCategory = category },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(category.icon, style = MaterialTheme.typography.headlineSmall)
                            Column(Modifier.weight(1f)) {
                                Text(
                                    tr(lang, category.es, category.en),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    category.tools.size.toString() + " " +
                                        tr(lang, "herramientas", "tools"),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text("›", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                }
                OutlinedButton(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
                    Text("‹ " + tr(lang, "Volver", "Back"))
                }
            }
        }
    }
}

@Composable
private fun QuickClinicalToolContent(
    lang: String,
    tool: QuickClinicalTool,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    when (tool.screen) {
        AppScreen.CALCULATORS -> DentalCalculatorsV40Screen(lang, onBack)
        AppScreen.MEDICATIONS -> MedicationManagementScreen(
            lang,
            session,
            onSessionChanged,
            onBack,
            section = if (tool.es.startsWith("Anestésicos")) {
                MedicationToolSection.ANESTHETICS
            } else {
                MedicationToolSection.MEDICATIONS
            }
        )
        AppScreen.ODONTOGRAM -> DentalAnalysisHubV41(lang, session, onSessionChanged, onBack)
        AppScreen.ICDAS -> IcdasScreen(lang, session, onSessionChanged, onBack)
        AppScreen.CPOD -> CpodInteractiveV19Screen(lang, session, onSessionChanged, onBack)
        AppScreen.OLEARY -> OlearyScreen(lang, session, onSessionChanged, onBack)
        AppScreen.IPC -> IpcResponsiveV17Screen(lang, session, onSessionChanged, onBack)
        AppScreen.IHOS -> IhosResponsiveV17Screen(lang, session, onSessionChanged, onBack)
        AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(lang, session, onSessionChanged, onBack)
        AppScreen.VITALS -> VitalsInteractiveV19Screen(lang, session, onSessionChanged, onBack)
        AppScreen.PULPAL -> PulpalPeriapicalInteractiveV2Screen(lang, session, onSessionChanged, { }, onBack)
        AppScreen.ATM -> AtmScreen(lang, onBack)
        AppScreen.OCCLUSION -> OcclusionInteractiveV19Screen(lang, onBack)
        AppScreen.MUCOSA -> MucosaInteractiveV19Screen(lang, onBack)
        AppScreen.POSTURE -> PostureVisualScreen(lang, onBack)
        AppScreen.SYSTEMIC_PROTOCOLS -> SystemicProtocols37Screen(lang, onBack)
        AppScreen.TREATMENT -> TreatmentScreen(lang, session, onSessionChanged, onBack)
        AppScreen.SESSIONS -> TreatmentBySessionsScreen(lang, session, onBack)
        AppScreen.ENDO -> EndodonticInteractiveV2Screen(lang, session, { }, { }, onBack)
        AppScreen.OPERATORIA -> OperativeRestorativeScreen(lang, onBack)
        AppScreen.EMERGENCY -> EmergencyDentalSheetV43(lang, onBack)
        AppScreen.CLINICAL_PHOTO -> ClinicalPhotographySheetV43(lang, onBack)
        AppScreen.IMPLANTOLOGY -> ImplantologySheetV44(lang, onBack)
        AppScreen.PEDIATRIC_DENTISTRY -> PediatricDentistryV45(lang, onBack)
        AppScreen.CARIES_RISK -> CariesRiskV45(lang, onBack)
        else -> onBack()
    }
}
