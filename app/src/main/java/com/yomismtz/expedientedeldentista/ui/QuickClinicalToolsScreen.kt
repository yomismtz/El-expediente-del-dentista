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

private val quickClinicalTools = listOf(
    QuickClinicalTool("🧮", "Calculadoras clínicas", "Clinical calculators", AppScreen.CALCULATORS),
    QuickClinicalTool("💊", "Medicamentos y referencias por peso", "Medications and weight references", AppScreen.MEDICATIONS),
    QuickClinicalTool("💉", "Anestésicos y cálculo por peso", "Local anesthetics and weight calculation", AppScreen.MEDICATIONS),
    QuickClinicalTool("🦷", "Odontograma", "Odontogram", AppScreen.ODONTOGRAM),
    QuickClinicalTool("🔎", "ICDAS", "ICDAS", AppScreen.ICDAS),
    QuickClinicalTool("📊", "CPOD / DMFT", "DMFT", AppScreen.CPOD),
    QuickClinicalTool("🪥", "Índice de O'Leary", "O'Leary plaque index", AppScreen.OLEARY),
    QuickClinicalTool("🦠", "Índice de placa comunitario (IPC)", "Community plaque index", AppScreen.IPC),
    QuickClinicalTool("🧼", "IHOS", "Simplified oral hygiene index", AppScreen.IHOS),
    QuickClinicalTool("📈", "Periodontograma", "Periodontogram", AppScreen.PERIODONTOGRAM),
    QuickClinicalTool("❤️", "Signos vitales", "Vital signs", AppScreen.VITALS),
    QuickClinicalTool("⚡", "Evaluación pulpar y periapical", "Pulpal and periapical assessment", AppScreen.PULPAL),
    QuickClinicalTool("🦷", "ATM", "TMJ", AppScreen.ATM),
    QuickClinicalTool("↔️", "Oclusión", "Occlusion", AppScreen.OCCLUSION),
    QuickClinicalTool("👄", "Mucosas y tejidos blandos", "Mucosa and soft tissues", AppScreen.MUCOSA),
    QuickClinicalTool("🧍", "Postura craneocervical", "Craniocervical posture", AppScreen.POSTURE),
    QuickClinicalTool("📚", "Protocolos sistémicos", "Systemic protocols", AppScreen.SYSTEMIC_PROTOCOLS),
    QuickClinicalTool("⚕️", "Diagnóstico y tratamiento por diente", "Diagnosis and treatment by tooth", AppScreen.TREATMENT),
    QuickClinicalTool("🗓️", "Plan de tratamiento por sesiones", "Treatment plan by sessions", AppScreen.SESSIONS),
    QuickClinicalTool("⚡", "Ficha endodóntica", "Endodontic sheet", AppScreen.ENDO),
    QuickClinicalTool("🧩", "Operatoria y restauración", "Operative and restorative dentistry", AppScreen.OPERATORIA),
    QuickClinicalTool("🚨", "Urgencias odontológicas", "Dental emergencies", AppScreen.EMERGENCY),
    QuickClinicalTool("📷", "Fotografía clínica", "Clinical photography", AppScreen.CLINICAL_PHOTO),
    QuickClinicalTool("🔩", "Implantología", "Implantology", AppScreen.IMPLANTOLOGY),
    QuickClinicalTool("🧒", "Odontopediatría", "Pediatric dentistry", AppScreen.PEDIATRIC_DENTISTRY),
    QuickClinicalTool("🛡️", "Caries y riesgo de caries", "Caries and caries risk", AppScreen.CARIES_RISK)
)

@Composable
fun QuickClinicalToolsScreen(
    lang: String,
    onExit: () -> Unit
) {
    var selected by remember { mutableStateOf<AppScreen?>(null) }
    var quickSession by remember { mutableStateOf(EducationalSession()) }

    val backToTools = { selected = null }

    BackHandler(enabled = selected != null) { backToTools() }
    BackHandler(enabled = selected == null) { onExit() }

    if (selected == null) {
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
                    "Modo clínico rápido. Puedes usar las herramientas sin crear ni abrir un expediente.",
                    "Quick clinical mode. Use the tools without creating or opening a patient record."
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
            quickClinicalTools.forEach { tool ->
                Card(
                    onClick = { selected = tool.screen },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        Modifier.padding(15.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            tool.icon + " " + if (lang == "en") tool.en else tool.es,
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
            OutlinedButton(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
                Text("‹ " + tr(lang, "Volver", "Back"))
            }
        }
    } else {
        QuickClinicalToolContent(
            lang = lang,
            screen = selected!!,
            session = quickSession,
            onSessionChanged = { quickSession = it },
            onBack = backToTools
        )
    }
}

@Composable
private fun QuickClinicalToolContent(
    lang: String,
    screen: AppScreen,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    when (screen) {
        AppScreen.CALCULATORS -> DentalCalculatorsV40Screen(lang, onBack)
        AppScreen.MEDICATIONS -> MedicationManagementScreen(lang, session, onSessionChanged, onBack)
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
