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
                tr(lang,
                    "Selecciona un diente, asigna un diagnóstico educativo y compara tres alternativas terapéuticas. No estás elaborando un plan para un paciente real.",
                    "Select a tooth, assign an educational diagnosis and compare three treatment alternatives. You are not creating a real patient's treatment plan.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanentes", "Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporales", "Primary")) })
            }
        }
        item {
            SectionCard(tr(lang, "1 · Elige el diente", "1 · Choose the tooth")) {
                DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { session.teeth[it]?.diagnosisId != null }
            }
        }
        item {
            SectionCard(tr(lang, "2 · ¿Qué diagnóstico corresponde?", "2 · Which diagnosis applies?")) {
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
                SectionCard(tr(lang, "3 · Compara las tres alternativas", "3 · Compare the three alternatives")) {
                    selectedPlan.options.forEachIndexed { index, option ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (record.treatmentId == option.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            onClick = { updateRecord(record.copy(treatmentId = option.id)) }
                        ) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("${index + 1}. ${if (lang == "en") option.labelEn else option.labelEs}", fontWeight = FontWeight.Bold)
                                Text("• ${if (lang == "en") option.explanationEn else option.explanationEs}")
                                if (record.treatmentId == option.id) {
                                    Text(
                                        if (option.preferred) tr(lang, "✓ Opción preferible en este escenario didáctico", "✓ Preferred in this teaching scenario")
                                        else tr(lang, "△ Puede ser válida en situaciones específicas; compara indicaciones", "△ May be valid in specific situations; compare indications"),
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (selectedOption != null) {
            item {
                NoticeCard(tr(lang,
                    "La selección final en clínica real requiere diagnóstico completo, edad/dentición, restaurabilidad, pronóstico, condiciones sistémicas y consentimiento. Aquí solo se enseña a razonar las alternativas.",
                    "Real clinical selection requires complete diagnosis, age/dentition, restorability, prognosis, systemic conditions and consent. Here the goal is only to learn therapeutic reasoning."))
            }
        }
        item {
            SectionCard(tr(lang, "Ortodoncia preventiva / mantenimiento de espacio", "Preventive orthodontics / space maintenance")) {
                Text("🧭 ${ClinicalEngines.orthodonticSuggestion(session, lang)}")
                Text(tr(lang,
                    "Si se marca pérdida prematura o extracción de un temporal, la app recuerda valorar espacio y muestra el aparato que podría considerarse según el patrón educativo.",
                    "When premature primary-tooth loss/extraction is marked, the app reminds the student to assess space and shows an appliance that could be considered in the teaching pattern."))
            }
        }
    }
}

@Composable
fun EvolutionScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    val plansById = ClinicalContent.treatmentPlans.associateBy { it.id }
    val optionsById = ClinicalContent.treatmentPlans.flatMap { it.options }.associateBy { it.id }
    val notes = session.teeth.toSortedMap().mapNotNull { (tooth, record) ->
        val diagnosis = record.diagnosisId?.let { plansById[it] } ?: return@mapNotNull null
        val option = record.treatmentId?.let { optionsById[it] } ?: return@mapNotNull null
        if (lang == "en") {
            "Tooth $tooth. Educational writing model. Diagnosis: ${diagnosis.diagnosisEn}. Procedure/plan: ${option.labelEn}. Record the procedure actually performed, anesthesia when applicable, relevant materials, tolerance/incidents, postoperative instructions, warning signs and follow-up."
        } else {
            "OD $tooth. Modelo de redacción educativa. Diagnóstico: ${diagnosis.diagnosisEs}. Procedimiento/plan: ${option.labelEs}. Se debe registrar el procedimiento realmente realizado, anestesia cuando corresponda, materiales relevantes, tolerancia/incidencias, indicaciones posoperatorias, signos de alarma y seguimiento."
        }
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Notas de evolución: modelos de redacción", "Progress notes: writing models"),
                onBack,
                tr(lang,
                    "La app no crea una nota clínica real. Muestra cómo debería estructurarse una nota según el tratamiento elegido en la pestaña de diagnóstico y tratamiento.",
                    "The app does not create a real clinical note. It shows how a note should be structured according to the treatment chosen in the diagnosis/treatment tab.")
            )
        }
        item {
            NoticeCard(tr(lang,
                "Una nota de evolución documenta cronológicamente lo realizado, cambios relevantes, signos vitales cuando correspondan, indicaciones y seguimiento.",
                "A progress note chronologically documents what was done, relevant changes, vital signs when applicable, instructions and follow-up."))
        }
        if (notes.isEmpty()) {
            item {
                SectionCard(tr(lang, "¿Cómo ver ejemplos?", "How to see examples")) {
                    Text(tr(lang,
                        "Entra a “Diagnóstico y tratamiento”, elige un diagnóstico y una alternativa terapéutica para cualquier diente. Al volver aquí aparecerá un modelo de nota para ese procedimiento.",
                        "Open “Diagnosis and treatment”, choose a diagnosis and treatment alternative for any tooth. When you return here, a progress-note model for that procedure will appear."))
                }
            }
        } else {
            items(notes.size) { index ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text("📋 ${tr(lang, "Ejemplo", "Example")} ${index + 1}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(notes[index])
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Plantilla mental", "Mental template")) {
                Text(tr(lang,
                    "• Fecha y órgano dentario\n• Estado general / signos vitales cuando correspondan\n• Diagnóstico\n• Procedimiento realmente realizado\n• Anestesia y materiales relevantes\n• Incidencias y tolerancia\n• Indicaciones y próxima cita",
                    "• Date and tooth\n• General status / vital signs when applicable\n• Diagnosis\n• Procedure actually performed\n• Anesthesia and relevant materials\n• Incidents and tolerance\n• Instructions and next visit"))
            }
        }
    }
}
