package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.ToothRecord

@Composable
fun TreatmentScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedTooth by remember { mutableStateOf(16) }
    var primary by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val selectedPlan = ClinicalContent.treatmentPlans.firstOrNull { it.id == record.diagnosisId }
    val selectedOption = selectedPlan?.options?.firstOrNull { it.id == record.treatmentId }

    fun updateRecord(updated: ToothRecord) {
        onSessionChanged(session.copy(teeth = session.teeth + (selectedTooth to updated), presentTeeth = session.presentTeeth + selectedTooth))
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Diagnóstico y tratamiento por diente", "Diagnosis and treatment by tooth"),
                onBack,
                tr(lang, "Asigna un diagnóstico, revisa tres alternativas y elige la que consideres mejor. Después la app explica cuál es la opción preferible en el escenario educativo.",
                    "Assign a diagnosis, review three alternatives and choose the one you consider best. The app then explains the preferred option for the educational scenario.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanentes", "Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporales", "Primary")) })
            }
        }
        item { SectionCard(tr(lang, "Diente", "Tooth")) { ToothSelector(shown, selectedTooth, { selectedTooth = it }) { session.teeth[it]?.diagnosisId != null } } }
        item {
            SectionCard(tr(lang, "1. Selecciona diagnóstico", "1. Select diagnosis")) {
                ClinicalContent.treatmentPlans.forEach { plan ->
                    FilterChip(
                        selected = record.diagnosisId == plan.id,
                        onClick = { updateRecord(record.copy(diagnosisId = plan.id, treatmentId = null)) },
                        label = { Text(if (lang == "en") plan.diagnosisEn else plan.diagnosisEs) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        if (selectedPlan != null) {
            item {
                SectionCard(tr(lang, "2. Elige una de las tres opciones", "2. Choose one of the three options")) {
                    selectedPlan.options.forEachIndexed { index, option ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (record.treatmentId == option.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            onClick = { updateRecord(record.copy(treatmentId = option.id)) }
                        ) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("${index + 1}. ${if (lang == "en") option.labelEn else option.labelEs}", fontWeight = FontWeight.Bold)
                                if (record.treatmentId == option.id) {
                                    Text(if (lang == "en") option.explanationEn else option.explanationEs)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (selectedOption != null) {
            item {
                val preferred = selectedOption.preferred
                NoticeCard(
                    if (preferred) tr(lang,
                        "✓ Elegiste la opción preferible para este escenario educativo. Revisa siempre edad, restaurabilidad, diagnóstico completo y condiciones del paciente.",
                        "✓ You selected the preferred option for this educational scenario. Always review age, restorability, complete diagnosis and patient conditions.")
                    else tr(lang,
                        "Esta alternativa puede ser válida en situaciones específicas, pero no es la opción marcada como preferible para este escenario. Compara la explicación de las tres opciones.",
                        "This alternative may be valid in specific situations, but it is not marked as preferred for this scenario. Compare the explanations of all three options.")
                )
            }
        }
        item {
            SectionCard(tr(lang, "Ortodoncia preventiva / mantenimiento de espacio", "Preventive orthodontics / space maintenance")) {
                Text(ClinicalEngines.orthodonticSuggestion(session, lang))
            }
        }
    }
}

@Composable
fun EvolutionScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    val notes = ClinicalEngines.generateEvolutionNotes(session, lang)
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Notas de evolución automáticas", "Automatic progress notes"),
                onBack,
                tr(lang, "Se generan a partir del diagnóstico y tratamiento que elegiste para cada diente. Sirven como modelo de redacción.",
                    "They are generated from the diagnosis and treatment selected for each tooth. They serve as writing models.")
            )
        }
        item {
            NoticeCard(tr(lang,
                "Una nota de evolución debe documentar cronológicamente el procedimiento realizado, cambios relevantes, signos vitales cuando correspondan, indicaciones y seguimiento. Los textos de esta app son ejemplos educativos.",
                "A progress note should chronologically document the procedure, relevant changes, vital signs when applicable, instructions and follow-up. Texts in this app are educational examples."))
        }
        if (notes.isEmpty()) {
            item {
                SectionCard(tr(lang, "Aún no hay notas", "No notes yet")) {
                    Text(tr(lang,
                        "Ve a “Diagnóstico y tratamiento por diente”, asigna diagnósticos y selecciona tratamientos. Aquí aparecerán las notas correspondientes.",
                        "Go to “Diagnosis and treatment by tooth”, assign diagnoses and select treatments. Their notes will appear here."))
                }
            }
        } else {
            items(notes.size) { index ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("${tr(lang, "Nota", "Note")} ${index + 1}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(notes[index])
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Estructura sugerida", "Suggested structure")) {
                Text(tr(lang,
                    "1) Fecha y órgano dentario.\n2) Estado general y signos vitales cuando correspondan.\n3) Diagnóstico.\n4) Procedimiento y materiales relevantes.\n5) Respuesta/tolerancia e incidencias.\n6) Indicaciones, signos de alarma y próxima cita.",
                    "1) Date and tooth.\n2) General status and vital signs when applicable.\n3) Diagnosis.\n4) Procedure and relevant materials.\n5) Response/tolerance and incidents.\n6) Instructions, warning signs and next appointment."))
            }
        }
    }
}
