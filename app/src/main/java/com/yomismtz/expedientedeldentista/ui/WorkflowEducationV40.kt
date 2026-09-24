package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.ToothRecord

private data class ActivityRowV40(
    val procedure: String,
    val objective: String,
    val before: String,
    val during: String,
    val after: String
)

private val activityRowsV40 = listOf(
    ActivityRowV40("Historia clínica", "Integrar antecedentes y riesgo", "Explicar propósito y privacidad", "Interrogar sin inducir respuestas", "Corroborar datos relevantes"),
    ActivityRowV40("Signos vitales y glucosa capilar", "Reconocer parámetros generales", "Verificar equipo y condiciones de medición", "Aplicar técnica correcta", "Interpretar en contexto y remitir si procede"),
    ActivityRowV40("Exploración cabeza/cuello", "Reconocer normalidad y hallazgos", "Higiene de manos y explicación", "Inspección y palpación sistemáticas", "Describir hallazgos sin diagnosticar por apariencia aislada"),
    ActivityRowV40("Exploración ATM", "Valorar dolor, ruidos y movimiento", "Explicar maniobras", "Medir y palpar de forma bilateral", "Integrar resumen y diagnóstico presuntivo"),
    ActivityRowV40("Exploración de mucosas", "Distinguir normalidad, variantes y lesiones", "Iluminación, guantes y secuencia", "Inspección y palpación por regiones", "Registrar descripción y diagnóstico diferencial"),
    ActivityRowV40("Odontograma / ICDAS", "Registrar estado dentario por diente y superficie", "Limpiar y secar según el sistema", "Explorar en orden", "Verificar congruencia del registro"),
    ActivityRowV40("Periodontograma", "Registrar parámetros periodontales", "Sonda adecuada y control de infección", "Seis sitios por diente", "Integrar sangrado, movilidad, furcación y resumen"),
    ActivityRowV40("Radiografías / imagenología", "Complementar diagnóstico", "Confirmar indicación y protección", "Aplicar técnica indicada", "Evaluar calidad e integrar con la clínica"),
    ActivityRowV40("Anestesia local", "Control del dolor", "Historia médica, dosis máxima y consentimiento", "Técnica supervisada", "Vigilar respuesta y documentar"),
    ActivityRowV40("Operatoria / restauración", "Control de enfermedad y restitución funcional", "Diagnóstico, aislamiento y plan", "Técnica y materiales según indicación", "Oclusión, acabado y control"),
    ActivityRowV40("Endodoncia", "Conservar diente cuando esté indicado", "Diagnóstico pulpar/periapical e imagen", "Aislamiento, longitud de trabajo, instrumentación e irrigación", "Sellado, restauración y seguimiento"),
    ActivityRowV40("Cirugía oral de pregrado", "Resolver indicaciones quirúrgicas dentro de competencia", "Diagnóstico, imagen, riesgo y consentimiento", "Técnica bajo supervisión docente", "Hemostasia, instrucciones y control"),
    ActivityRowV40("Prótesis", "Restituir función y estabilidad", "Diagnóstico protésico y plan", "Procedimiento clínico/laboratorio indicado", "Adaptación, higiene y mantenimiento"),
    ActivityRowV40("Educación preventiva", "Reducir riesgo y favorecer autocuidado", "Identificar necesidades", "Demostrar técnica y explicar riesgos", "Confirmar comprensión y plan de seguimiento")
)

@Composable
fun ActivitiesTableV40Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "Autorización de actividades · guía", "Activity authorization · guide"),
        tr(lang, "La tabla es una guía académica; no solicita nombres, firmas ni datos de un paciente.", "This table is an academic guide; it does not request patient names, signatures or personal data."),
        onBack
    ) { _ ->
        NoticeCard(tr(lang,
            "Cada institución define qué actividades puede realizar el estudiante y con qué supervisión. Usa esta pantalla para reconocer qué revisar antes, durante y después de una actividad clínica.",
            "Each institution defines which activities a student may perform and under what supervision. Use this screen to recognize what to review before, during and after a clinical activity."))
        val scroll = rememberScrollState()
        Column(Modifier.fillMaxWidth().horizontalScroll(scroll), verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Row {
                TableCellV40("Actividad", 190, true)
                TableCellV40("Objetivo", 240, true)
                TableCellV40("Antes", 260, true)
                TableCellV40("Durante", 290, true)
                TableCellV40("Después", 280, true)
            }
            activityRowsV40.forEach { row ->
                Row {
                    TableCellV40(row.procedure, 190)
                    TableCellV40(row.objective, 240)
                    TableCellV40(row.before, 260)
                    TableCellV40(row.during, 290)
                    TableCellV40(row.after, 280)
                }
            }
        }
    }
}

@Composable
private fun TableCellV40(text: String, width: Int, header: Boolean = false) {
    Card(
        modifier = Modifier.width(width.dp),
        colors = CardDefaults.cardColors(containerColor = if (header) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.7.dp, MaterialTheme.colorScheme.outline.copy(alpha = .55f)),
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(text, Modifier.padding(10.dp), fontWeight = if (header) FontWeight.Black else FontWeight.Normal)
    }
}

private fun priorityForV40(diagnosisId: String, treatmentId: String): Pair<Int, String> = when {
    diagnosisId in setOf("necrosis", "irreversible_pulpitis") || treatmentId.contains("drain") -> 1 to "Urgencia, infección o dolor: primero controlar riesgo y síntomas."
    treatmentId.contains("extract") || treatmentId.contains("endo") || treatmentId.contains("pulp") -> 2 to "Control de enfermedad y conservación/eliminación del foco según pronóstico."
    diagnosisId.contains("caries") || treatmentId in setOf("resin", "art", "hall", "ipt") -> 3 to "Control de caries y restauración de dientes activos/restaurables."
    diagnosisId == "healthy" || treatmentId in setOf("observe", "fluoride", "remin", "sealant") -> 5 to "Prevención y mantenimiento; se programa después de resolver necesidades prioritarias."
    else -> 4 to "Rehabilitación o tratamiento definitivo después del control de enfermedad."
}

@Composable
fun TreatmentPlannerV40Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var mode by remember { mutableStateOf(0) }
    var selectedTooth by remember { mutableStateOf(16) }
    var primary by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val selectedPlan = ClinicalContent.treatmentPlans.firstOrNull { it.id == record.diagnosisId }

    fun updateRecord(updated: ToothRecord) {
        onSessionChanged(session.copy(
            teeth = session.teeth + (selectedTooth to updated),
            presentTeeth = session.presentTeeth + selectedTooth
        ))
    }

    ResponsiveScreenV17(
        tr(lang, "Diagnóstico y tratamiento", "Diagnosis and treatment"),
        tr(lang, "Dos vistas: seleccionar por órgano dentario y consultar lo que quedó guardado.", "Two views: select by tooth and review saved choices."),
        onBack
    ) { profile ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(mode == 0, { mode = 0 }, { Text(tr(lang, "1 · Seleccionar", "1 · Select")) }, Modifier.weight(1f))
            FilterChip(mode == 1, { mode = 1 }, { Text(tr(lang, "2 · Consultar", "2 · Review")) }, Modifier.weight(1f))
        }

        if (mode == 0) {
            ResponsiveSectionV17(tr(lang, "Dentición y órgano dentario", "Dentition and tooth")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanente", "Permanent")) })
                    FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporal", "Primary")) })
                }
                DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { TeachingStateV40.toothPlans.containsKey(it) }
            }
            ResponsiveSectionV17(tr(lang, "Diagnóstico educativo", "Teaching diagnosis")) {
                AdaptiveGridV17(ClinicalContent.treatmentPlans.size, if (profile.largeSystemText) 1 else 2) { i ->
                    val plan = ClinicalContent.treatmentPlans[i]
                    FilterChip(
                        selected = record.diagnosisId == plan.id,
                        onClick = { updateRecord(record.copy(diagnosisId = plan.id, treatmentId = null)) },
                        label = { Text(if (lang == "en") plan.diagnosisEn else plan.diagnosisEs) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (selectedPlan != null) {
                ResponsiveSectionV17(tr(lang, "Tres alternativas de tratamiento", "Three treatment alternatives")) {
                    selectedPlan.options.take(3).forEachIndexed { index, option ->
                        Card(
                            onClick = {
                                updateRecord(record.copy(treatmentId = option.id))
                                val (priority, reason) = priorityForV40(selectedPlan.id, option.id)
                                TeachingStateV40.toothPlans[selectedTooth] = ToothTeachingPlanV40(
                                    diagnosisId = selectedPlan.id,
                                    diagnosisLabel = if (lang == "en") selectedPlan.diagnosisEn else selectedPlan.diagnosisEs,
                                    treatmentId = option.id,
                                    treatmentLabel = if (lang == "en") option.labelEn else option.labelEs,
                                    priority = priority,
                                    reason = reason
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = if (record.treatmentId == option.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .4f))
                        ) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("${index + 1}. ${if (lang == "en") option.labelEn else option.labelEs}", fontWeight = FontWeight.Black)
                                Text(if (lang == "en") option.explanationEn else option.explanationEs)
                                if (option.preferred) Text(tr(lang, "Preferible en el escenario didáctico", "Preferred in the teaching scenario"), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            NoticeCard(tr(lang,
                "La app enseña el razonamiento entre diagnóstico y alternativas. La selección no sustituye diagnóstico completo, restaurabilidad, pronóstico, edad, condiciones sistémicas, consentimiento ni supervisión clínica.",
                "The app teaches reasoning between diagnosis and alternatives. Selection does not replace complete diagnosis, restorability, prognosis, age, systemic conditions, consent or clinical supervision."))
        } else {
            ResponsiveSectionV17(tr(lang, "Diagnósticos y tratamientos guardados", "Saved diagnoses and treatments")) {
                if (TeachingStateV40.toothPlans.isEmpty()) {
                    Text(tr(lang, "Aún no hay dientes con diagnóstico y tratamiento seleccionados.", "No teeth have saved diagnosis/treatment selections yet."))
                } else {
                    TeachingStateV40.toothPlans.toSortedMap().forEach { (tooth, plan) ->
                        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("🦷 OD $tooth", fontWeight = FontWeight.Black)
                                Text("Dx: ${plan.diagnosisLabel}")
                                Text("Tx: ${plan.treatmentLabel}")
                                Text(tr(lang, "Prioridad ${plan.priority}: ${plan.reason}", "Priority ${plan.priority}: ${plan.reason}"), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TreatmentSessionsV40Screen(lang: String, onBack: () -> Unit) {
    val sorted = TeachingStateV40.toothPlans.toList().sortedWith(compareBy({ it.second.priority }, { it.first }))
    ResponsiveScreenV17(
        tr(lang, "Tratamiento por sesiones", "Treatment by sessions"),
        tr(lang, "Ordena automáticamente las selecciones del módulo Diagnóstico y tratamiento por prioridad educativa.", "Automatically orders selections from Diagnosis and treatment by teaching priority."),
        onBack
    ) { _ ->
        ResponsiveSectionV17(tr(lang, "Cómo se organiza por fases", "How treatment is phased")) {
            Text(tr(lang, "1 · Urgencia: dolor, infección, sangrado o riesgo inmediato.", "1 · Urgency: pain, infection, bleeding or immediate risk."))
            Text(tr(lang, "2 · Control de enfermedad: focos infecciosos, endodoncia/exodoncia indicada y control periodontal.", "2 · Disease control: infectious foci, indicated endodontics/extraction and periodontal control."))
            Text(tr(lang, "3 · Control de caries y restauraciones necesarias.", "3 · Caries control and necessary restorations."))
            Text(tr(lang, "4 · Rehabilitación definitiva: prótesis, tratamientos complementarios y ajustes.", "4 · Definitive rehabilitation: prosthetics, complementary treatments and adjustments."))
            Text(tr(lang, "5 · Prevención, mantenimiento y reevaluación.", "5 · Prevention, maintenance and reevaluation."))
        }
        ResponsiveSectionV17(tr(lang, "Plan sugerido con tus selecciones", "Suggested plan from your selections")) {
            if (sorted.isEmpty()) Text(tr(lang, "Primero selecciona diagnósticos y tratamientos por diente.", "First select diagnoses and treatments by tooth."))
            sorted.forEachIndexed { index, (tooth, plan) ->
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("${index + 1}. OD $tooth · ${plan.treatmentLabel}", fontWeight = FontWeight.Black)
                        Text("Dx: ${plan.diagnosisLabel}")
                        Text("Prioridad ${plan.priority} · ${plan.reason}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        NoticeCard(tr(lang,
            "El orden es didáctico y debe ajustarse a urgencias reales, estado sistémico, consentimiento, disponibilidad de recursos y criterio del docente/clinico responsable.",
            "The order is educational and must be adjusted to real urgency, systemic status, consent, available resources and the supervising clinician's judgment."))
    }
}

@Composable
fun BudgetGuideV40Screen(lang: String, onBack: () -> Unit) {
    val items = listOf(
        "Procedimiento" to "Nombre exacto del servicio o fase; evita términos ambiguos.",
        "Cantidad" to "Número de unidades, dientes, superficies o sesiones incluidas.",
        "Costo unitario" to "Costo de una unidad del procedimiento según la institución.",
        "Subtotal" to "Cantidad × costo unitario.",
        "Laboratorio / servicio externo" to "Costo separado cuando interviene laboratorio, imagen u otro proveedor.",
        "Total" to "Suma final de subtotales; debe distinguir lo incluido y lo no incluido."
    )
    ResponsiveScreenV17(tr(lang, "Presupuesto · cómo leerlo", "Budget · how to read it"), tr(lang, "Guía administrativa, no captura cobros reales.", "Administrative guide; it does not capture real charges."), onBack) { _ ->
        items.forEach { (title, detail) ->
            ExpandableTeachingCardV40(title, detail)
        }
        NoticeCard(tr(lang, "Ejemplo: Restauración con resina · OD 16 · cantidad 1 · costo unitario según institución · subtotal igual al costo unitario.", "Example: Composite restoration · tooth 16 · quantity 1 · unit cost according to institution · subtotal equals unit cost."))
    }
}

@Composable
fun TreatmentRequestGuideV40Screen(lang: String, onBack: () -> Unit) {
    val items = listOf(
        "Servicio solicitado" to "Área o procedimiento al que se remite: endodoncia, cirugía, periodoncia, prótesis, imagenología, etc.",
        "Motivo" to "Razón clínica concreta de la solicitud, no una frase vaga como 'revisar'.",
        "Área u órgano dentario" to "Región, lado, tejido u órgano dentario que debe valorar el servicio.",
        "Prioridad / referencia" to "Urgente, preferente o programable según riesgo y síntomas; la institución define sus categorías.",
        "Información clínica útil" to "Antecedentes, hallazgos, pruebas e imágenes que justifican la solicitud.",
        "Responsable y supervisión" to "Quién solicita y bajo qué servicio/docente se integra la atención."
    )
    ResponsiveScreenV17(tr(lang, "Solicitud de tratamiento", "Treatment request"), tr(lang, "Toca cada rubro para aprender qué significa.", "Tap each item to learn what it means."), onBack) { _ ->
        items.forEach { (title, detail) -> ExpandableTeachingCardV40(title, detail) }
        NoticeCard(tr(lang,
            "Ejemplo educativo: “Solicito valoración por Endodoncia del OD 36 por dolor espontáneo y persistente al frío, dolor a percusión y caries profunda. Se adjunta radiografía periapical. Prioridad: preferente”.",
            "Teaching example: “Endodontic assessment requested for tooth 36 due to spontaneous and lingering cold pain, percussion pain and deep caries. Periapical radiograph attached. Priority: prompt”."))
    }
}

@Composable
private fun ExpandableTeachingCardV40(title: String, detail: String) {
    var open by remember { mutableStateOf(false) }
    Card(
        onClick = { open = !open },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (open) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .4f))
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontWeight = FontWeight.Black)
            if (open) Text(detail) else Text("Toca para explicación", style = MaterialTheme.typography.bodySmall)
        }
    }
}
