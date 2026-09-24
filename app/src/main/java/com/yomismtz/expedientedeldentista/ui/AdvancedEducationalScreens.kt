package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import kotlin.math.pow

private data class TeachingItem(
    val titleEs: String,
    val titleEn: String,
    val bodyEs: String,
    val bodyEn: String,
    val exampleEs: String = "",
    val exampleEn: String = ""
)

@Composable
private fun ExpandableTeachingCard(item: TeachingItem, lang: String) {
    var open by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().clickable { open = !open },
        colors = CardDefaults.cardColors(
            containerColor = if (open) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text((if (open) "▾ " else "▸ ") + if (lang == "en") item.titleEn else item.titleEs,
                fontWeight = FontWeight.Bold)
            if (open) {
                Text(if (lang == "en") item.bodyEn else item.bodyEs)
                val example = if (lang == "en") item.exampleEn else item.exampleEs
                if (example.isNotBlank()) Text("✍️ $example", color = MaterialTheme.colorScheme.primary)
            } else {
                Text(tr(lang, "Toca para ver qué significa y cómo se llena", "Tap to see what it means and how to complete it"),
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun TreatmentBySessionsScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    var child by remember { mutableStateOf(true) }
    val childItems = listOf(
        TeachingItem("Sesión 1 · Integración y diagnóstico", "Session 1 · Integration and diagnosis",
            "Historia clínica, valoración odontopediátrica, auxiliares necesarios, integración diagnóstica y explicación del plan. La prioridad cambia si existe dolor o urgencia.",
            "Medical/dental history, pediatric assessment, needed diagnostic aids, diagnosis and explanation of the plan. Acute pain or urgency changes the priority.",
            "Historia clínica + evaluación diagnóstica + radiografías/modelos si están indicados.",
            "History + diagnostic assessment + radiographs/models when indicated."),
        TeachingItem("Sesión 2 · Prevención y aclimatación", "Session 2 · Prevention and acclimatization",
            "Busca una experiencia inicial de baja carga: instrucción de higiene, revelado de placa, profilaxis y medidas preventivas. En niños también importa la cooperación y el manejo de conducta.",
            "A low-burden first clinical experience: hygiene instruction, plaque disclosure, prophylaxis and prevention. In children cooperation and behavior guidance also matter."),
        TeachingItem("Sesión 3 · Baja complejidad", "Session 3 · Low-complexity care",
            "Selladores, medidas de mínima intervención y restauraciones sencillas cuando el diagnóstico y la cooperación lo permiten.",
            "Sealants, minimally invasive care and simple restorations when diagnosis and cooperation allow it."),
        TeachingItem("Sesiones siguientes · Por zonas", "Following sessions · By zones",
            "Agrupa procedimientos compatibles por cuadrante o zona para reducir anestesias y aislamientos repetidos. Las urgencias siempre tienen prioridad.",
            "Group compatible procedures by quadrant or area to reduce repeated anesthesia and isolation. Emergencies always take priority.")
    )
    val adultItems = listOf(
        TeachingItem("Fase 1 · Urgencias y diagnóstico", "Phase 1 · Emergencies and diagnosis",
            "Control de dolor/infección cuando exista, integración del expediente, estudios indicados y diagnóstico.",
            "Control pain/infection when present, integrate the record, obtain indicated studies and diagnose."),
        TeachingItem("Fase 2 · Control de enfermedad", "Phase 2 · Disease control",
            "Fase higiénica y de saneamiento: profilaxis, control periodontal, eliminación de focos infecciosos, tratamientos endodónticos necesarios y restauraciones provisionales cuando se indiquen.",
            "Hygienic and disease-control phase: prophylaxis, periodontal control, removal of infectious foci, needed endodontics and provisional restorations when indicated."),
        TeachingItem("Fase 3 · Restaurativa", "Phase 3 · Restorative",
            "Restauraciones definitivas y reconstrucciones que estabilizan función y estructura.",
            "Definitive restorations and reconstructions that stabilize function and tooth structure."),
        TeachingItem("Fase 4 · Rehabilitación", "Phase 4 · Rehabilitation",
            "Prótesis y rehabilitación de mayor complejidad después de controlar la enfermedad activa y establecer un pronóstico.",
            "Prosthetic and higher-complexity rehabilitation after active disease is controlled and prognosis is established.")
    )

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(tr(lang, "Tratamiento por sesiones", "Treatment by sessions"), onBack,
                tr(lang, "Organiza el plan en un orden lógico. La secuencia depende de urgencia, edad/dentición, cooperación, enfermedad activa, pronóstico, tiempo clínico y complejidad.",
                    "Organize treatment in a logical sequence. Order depends on urgency, age/dentition, cooperation, active disease, prognosis, chair time and complexity."))
        }
        item {
            SectionCard(tr(lang,"Resumen importado de Diagnóstico y tratamiento","Summary imported from Diagnosis and treatment")) {
                val planned=session.teeth.filterValues{it.diagnosisId!=null || it.treatmentId!=null}
                if(planned.isEmpty()) Text(tr(lang,"Aún no hay diagnósticos/tratamientos seleccionados por diente. Regresa a Diagnóstico y tratamiento para construir el plan.","No tooth-level diagnoses/treatments selected yet. Return to Diagnosis and treatment to build the plan."))
                else planned.toSortedMap().forEach { entry ->
                    val tooth=entry.key; val record=entry.value
                    Text("OD "+tooth+" · "+tr(lang,"Diagnóstico","Diagnosis")+": "+(record.diagnosisId ?: "—")+" · "+tr(lang,"Tratamiento","Treatment")+": "+(record.treatmentId ?: "—"),fontWeight=FontWeight.SemiBold)
                }
                Text(tr(lang,"Estos datos son los mismos guardados por diente; se usan como base para organizar las sesiones por urgencia, control de enfermedad, complejidad y secuencia clínica.","These are the same tooth-level saved data and are used to organize sessions by urgency, disease control, complexity and clinical sequence."),style=MaterialTheme.typography.bodySmall)
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(child, { child = true }, { Text(tr(lang, "Niños", "Children")) })
                FilterChip(!child, { child = false }, { Text(tr(lang, "Adultos", "Adults")) })
            }
        }
        items(if (child) childItems else adultItems) { ExpandableTeachingCard(it, lang) }
        item {
            NoticeCard(tr(lang,
                "Regla didáctica: no existe una secuencia rígida para todos los casos. El alumno debe justificar por qué una actividad va antes que otra y modificar el orden ante una urgencia o un cambio diagnóstico.",
                "Teaching rule: there is no rigid sequence for every case. The student should justify why one activity precedes another and adapt the order if an emergency or diagnostic change occurs."))
        }
    }
}

@Composable
fun EndodonticSheetScreen(
    lang: String,
    session: EducationalSession,
    onOpenPulpal: () -> Unit,
    onOpenApical: () -> Unit,
    onBack: () -> Unit
) {
    var tab by remember { mutableStateOf(0) }
    val result = ClinicalEngines.pulpalDiagnosis(session.pulpal)
    val steps = listOf(
        TeachingItem("Longitud de trabajo / conductometría", "Working length",
            "Es la longitud clínica de referencia para preparar y obturar el sistema de conductos sin sobrepasar innecesariamente los tejidos apicales. Se establece con métodos clínicos apropiados, localizador apical y/o control radiográfico conforme al protocolo docente.",
            "Clinical reference length used to prepare and fill the root-canal system while avoiding unnecessary extension beyond apical tissues. It is established with appropriate clinical methods, an apex locator and/or radiographic control according to the teaching protocol."),
        TeachingItem("Tratamiento de conductos", "Root-canal treatment",
            "Secuencia didáctica: diagnóstico y restaurabilidad → anestesia cuando corresponda → aislamiento absoluto → acceso y localización → longitud de trabajo → preparación biomecánica e irrigación → secado → obturación → control y restauración coronal.",
            "Teaching sequence: diagnosis/restorability → anesthesia when applicable → rubber-dam isolation → access/location → working length → shaping and irrigation → drying → obturation → control and coronal restoration."),
        TeachingItem("Pulpotomía", "Pulpotomy",
            "Procedimiento de terapia pulpar vital en el que se elimina pulpa coronal inflamada y se busca conservar tejido radicular vital cuando el diagnóstico y el caso son compatibles. La indicación depende de dentición, estado pulpar, restaurabilidad y protocolo docente.",
            "Vital-pulp procedure in which inflamed coronal pulp is removed while attempting to preserve vital radicular pulp when diagnosis and case selection are compatible."),
        TeachingItem("Pulpectomía", "Pulpectomy",
            "Elimina tejido pulpar de cámara y conductos. En dentición temporal la anatomía, reabsorción fisiológica y material de obturación requieren consideraciones específicas; en permanentes se integra al tratamiento endodóntico según diagnóstico.",
            "Removes pulp tissue from chamber and canals. Primary teeth require specific considerations for anatomy, physiologic resorption and filling material; in permanent teeth it is integrated into endodontic therapy according to diagnosis."),
        TeachingItem("Dientes permanentes jóvenes", "Immature permanent teeth",
            "El material distingue apicogénesis en dientes jóvenes con pulpa vital y ápice abierto, y apicoformación cuando existe necrosis y la raíz no ha terminado de formarse. La selección requiere diagnóstico y supervisión especializada.",
            "The teaching material distinguishes apexogenesis for immature teeth with vital pulp and apexification when necrosis occurs before root completion. Selection requires diagnosis and expert supervision.")
    )

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(tr(lang, "Ficha endodóntica", "Endodontic sheet"), onBack,
                tr(lang, "Integra anamnesis, pruebas, imagen, diagnóstico pulpar + periapical y el razonamiento terapéutico en una misma ficha.",
                    "Integrates history, tests, imaging, pulpal + periapical diagnosis and treatment reasoning in one sheet."))
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                listOf(tr(lang,"Diagnóstico","Diagnosis"), tr(lang,"Procedimientos","Procedures"), tr(lang,"Irrigación segura","Safe irrigation")).forEachIndexed { i, label ->
                    FilterChip(tab == i, { tab = i }, { Text(label) }, modifier = Modifier.weight(1f))
                }
            }
        }
        when (tab) {
            0 -> {
                item {
                    SectionCard(tr(lang, "Diagnóstico integrado", "Integrated diagnosis")) {
                        Text(tr(lang, "Pulpar: ", "Pulpal: ") + if (lang == "en") result.pulpalEn else result.pulpalEs, fontWeight = FontWeight.Bold)
                        Text(tr(lang, "Periapical: ", "Periapical: ") + if (lang == "en") result.apicalEn else result.apicalEs, fontWeight = FontWeight.Bold)
                        OutlinedButton(onClick = onOpenPulpal, modifier = Modifier.fillMaxWidth()) { Text("⚡ ${tr(lang,"Revisar signos y pruebas pulpares","Review pulpal signs and tests")}") }
                        OutlinedButton(onClick = onOpenApical, modifier = Modifier.fillMaxWidth()) { Text("🩻 ${tr(lang,"Revisar hallazgos periapicales","Review periapical findings")}") }
                    }
                }
                item {
                    SectionCard(tr(lang, "Clasificación del material docente", "Classification in the teaching material")) {
                        Text(tr(lang, "Pulpares: pulpa normal, pulpitis reversible, pulpitis irreversible sintomática/asintomática, necrosis, terapia previamente iniciada y diente previamente tratado.",
                            "Pulpal: normal pulp, reversible pulpitis, symptomatic/asymptomatic irreversible pulpitis, necrosis, previously initiated therapy and previously treated tooth."))
                        Text(tr(lang, "Periapicales: tejidos apicales normales, periodontitis apical sintomática/asintomática, absceso apical agudo/crónico y osteítis condensante.",
                            "Periapical: normal apical tissues, symptomatic/asymptomatic apical periodontitis, acute/chronic apical abscess and condensing osteitis."))
                    }
                }
                item {
                    NoticeCard(tr(lang,
                        "Actualización 2025–2026: AAE y ESE están revisando conjuntamente la terminología diagnóstica. La propuesta aún está en proceso de revisión de partes interesadas; por eso esta versión conserva la clasificación docente del expediente y señala que debe verificarse la terminología final antes de publicación académica.",
                        "2025–2026 update: AAE and ESE are jointly revising diagnostic terminology. The proposal remains under stakeholder review; this build therefore preserves the record's teaching classification and notes that final terminology should be verified before academic publication."))
                }
            }
            1 -> items(steps) { ExpandableTeachingCard(it, lang) }
            else -> {
                item {
                    NoticeCard(tr(lang,
                        "Seguridad con hipoclorito de sodio: esta app NO enseña a fabricar, mezclar ni diluir soluciones. En clínica debe utilizarse un irrigante dental preparado/comercial en la concentración indicada por el protocolo institucional y el fabricante, con aislamiento absoluto, protección ocular/facial y técnica de irrigación segura.",
                        "Sodium hypochlorite safety: this app does NOT teach compounding, mixing or dilution. Clinically use a prepared/commercial dental irrigant at the concentration specified by the institution/manufacturer, with rubber-dam isolation, eye/face protection and safe irrigation technique."))
                }
                item {
                    SectionCard(tr(lang, "Advertencias esenciales", "Essential warnings")) {
                        Text("• ${tr(lang,"No mezclar con ácidos ni amoniaco: pueden generarse gases peligrosos.","Do not mix with acids or ammonia: dangerous gases may form.")}")
                        Text("• ${tr(lang,"Evitar mezclar directamente con clorhexidina dentro del conducto; seguir el protocolo de irrigación y enjuague institucional.","Avoid direct mixing with chlorhexidine in the canal; follow the institutional irrigation/rinsing protocol.")}")
                        Text("• ${tr(lang,"Evitar extrusión apical y contacto con tejidos blandos, ojos o piel.","Avoid apical extrusion and contact with soft tissue, eyes or skin.")}")
                        Text("• ${tr(lang,"Ante exposición accidental, seguir de inmediato el protocolo de seguridad de la clínica y solicitar supervisión docente.","For accidental exposure, immediately follow the clinic safety protocol and obtain faculty supervision.")}")
                    }
                }
            }
        }
    }
}

private fun kennedyClass(present: Set<Int>, upper: Boolean, lang: String): String {
    val arch = if (upper) listOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27)
    else listOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37)
    val p = present.intersect(arch.toSet())
    if (p.isEmpty()) return tr(lang, "Edéntulo total: no corresponde a Kennedy I–IV", "Completely edentulous: outside Kennedy I–IV")
    val missing = arch.filter { it !in p }.toSet()
    if (missing.isEmpty()) return tr(lang, "Sin áreas edéntulas", "No edentulous areas")

    val rightPosterior = if (upper) listOf(17,16,15,14) else listOf(47,46,45,44)
    val leftPosterior = if (upper) listOf(24,25,26,27) else listOf(34,35,36,37)
    val rightDistal = rightPosterior.first() in missing
    val leftDistal = leftPosterior.last() in missing
    if (rightDistal && leftDistal) return tr(lang, "Kennedy Clase I · áreas posteriores bilaterales de extremo libre", "Kennedy Class I · bilateral posterior distal-extension areas")
    if (rightDistal || leftDistal) return tr(lang, "Kennedy Clase II · área posterior unilateral de extremo libre", "Kennedy Class II · unilateral posterior distal-extension area")

    val centralPair = if (upper) setOf(11,21) else setOf(41,31)
    val posteriorMissing = missing.any { it in rightPosterior || it in leftPosterior }
    if (centralPair.all { it in missing } && !posteriorMissing) {
        return tr(lang, "Kennedy Clase IV · área anterior que cruza la línea media", "Kennedy Class IV · anterior area crossing the midline")
    }
    return tr(lang, "Kennedy Clase III · área edéntula limitada por dientes", "Kennedy Class III · bounded edentulous area")
}

@Composable
fun ProstheticSheetScreen(lang: String, onBack: () -> Unit) {
    val upper = listOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27)
    val lower = listOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37)
    var presentUpper by remember { mutableStateOf(upper.toSet()) }
    var presentLower by remember { mutableStateOf(lower.toSet()) }
    val sections = listOf(
        TeachingItem("Modelos de estudio", "Study models", "Permiten analizar espacios edéntulos, migraciones, forma de arcada, relación oclusal y zonas candidatas a soporte protésico.", "Allow analysis of edentulous spaces, migration, arch form, occlusion and potential prosthetic support."),
        TeachingItem("Análisis radiográfico y periodontal", "Radiographic and periodontal analysis", "Valora lesiones periapicales, tratamientos previos, soporte óseo, relación corona-raíz, ligamento periodontal y pronóstico de posibles pilares.", "Assesses periapical disease, previous treatment, bone support, crown-root relation, periodontal ligament and prognosis of potential abutments."),
        TeachingItem("Prótesis fija", "Fixed prosthesis", "Identifica pilares, pónticos, material, restauraciones individuales y condiciones que determinan si el diseño es viable.", "Identifies abutments, pontics, material, individual restorations and conditions that determine design feasibility."),
        TeachingItem("PPR", "Removable partial denture", "Distingue soporte dentario/mucoso, preparaciones preprotésicas, retenedores, conectores, dientes artificiales y distribución de fuerzas.", "Distinguishes tooth/mucosal support, preprosthetic preparations, retainers, connectors, artificial teeth and force distribution."),
        TeachingItem("Prótesis total", "Complete denture", "Revisa reborde alveolar, mucosa, espacio protésico, relación maxilomandibular y auxiliares necesarios.", "Reviews alveolar ridge, mucosa, prosthetic space, maxillomandibular relation and required aids.")
    )

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Ficha protésica","Prosthetic sheet"), onBack,
            tr(lang,"Toca cada apartado y practica la clasificación de Kennedy marcando dientes presentes o ausentes.","Tap each section and practice Kennedy classification by marking teeth present or missing.")) }
        items(sections) { ExpandableTeachingCard(it, lang) }
        item {
            SectionCard(tr(lang, "Clasificación de Kennedy · práctica", "Kennedy classification · practice")) {
                Text(tr(lang,"Toca los dientes para alternar presente/ausente. La orientación es didáctica y simplificada; terceros molares se excluyen de este ejercicio.","Tap teeth to toggle present/missing. This is a simplified teaching orientation; third molars are excluded from this exercise."))
                Text(tr(lang,"MAXILAR","MAXILLA"), fontWeight = FontWeight.Bold)
                KennedyArchRow(upper, presentUpper) { tooth ->
                    presentUpper = if (tooth in presentUpper) presentUpper - tooth else presentUpper + tooth
                }
                Text(kennedyClass(presentUpper, true, lang), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(tr(lang,"MANDÍBULA","MANDIBLE"), fontWeight = FontWeight.Bold)
                KennedyArchRow(lower, presentLower) { tooth ->
                    presentLower = if (tooth in presentLower) presentLower - tooth else presentLower + tooth
                }
                Text(kennedyClass(presentLower, false, lang), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }
        item {
            SectionCard(tr(lang,"Referencia rápida","Quick reference")) {
                Text("I · ${tr(lang,"extremos libres posteriores bilaterales","bilateral posterior distal extensions")}")
                Text("II · ${tr(lang,"extremo libre posterior unilateral","unilateral posterior distal extension")}")
                Text("III · ${tr(lang,"espacio edéntulo limitado por dientes","bounded edentulous space")}")
                Text("IV · ${tr(lang,"espacio anterior único que cruza línea media","single anterior space crossing the midline")}")
            }
        }
        item { NoticeCard(tr(lang,"La clasificación definitiva requiere aplicar todas las reglas de Kennedy/Applegate y valorar qué dientes realmente se usarán como pilares; esta herramienta es para aprendizaje inicial.","Definitive classification requires all Kennedy/Applegate rules and evaluation of actual abutment teeth; this tool is for initial learning.")) }
    }
}

@Composable
private fun KennedyArchRow(teeth: List<Int>, present: Set<Int>, onTap: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        teeth.forEach { tooth ->
            Card(
                modifier = Modifier.weight(1f).clickable { onTap(tooth) },
                colors = CardDefaults.cardColors(containerColor = if (tooth in present) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(Modifier.padding(vertical = 6.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (tooth in present) "🦷" else "✕", style = MaterialTheme.typography.bodySmall)
                    Text(tooth.toString(), style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun SurgicalSheetScreen(lang: String, onBack: () -> Unit) {
    val sections = listOf(
        TeachingItem("Datos médicos que modifican cirugía", "Medical data affecting surgery", "Antecedentes cardiovasculares, hematológicos, respiratorios, endocrinos, renales, hepáticos, medicamentos, alergias y embarazo pueden cambiar el riesgo, las pruebas necesarias o la necesidad de interconsulta.", "Cardiovascular, hematologic, respiratory, endocrine, renal/hepatic history, medications, allergies and pregnancy may change risk, testing or need for consultation."),
        TeachingItem("Indicación y diagnóstico", "Indication and diagnosis", "Debe quedar claro por qué se propone el procedimiento y cuál es el diagnóstico que lo justifica.", "Document why the procedure is proposed and the diagnosis supporting it."),
        TeachingItem("Zona / órgano dentario", "Site / tooth", "Identifica de manera inequívoca el sitio quirúrgico y correlaciónalo con imagen y plan.", "Clearly identify the surgical site and correlate it with imaging and treatment plan."),
        TeachingItem("Anestesia y control intraoperatorio", "Anesthesia and intraoperative control", "Registra la técnica y el control clínico conforme a la supervisión docente; no basta escribir solo “anestesia”.", "Record the technique and clinical control according to faculty supervision; do not document only 'anesthesia'."),
        TeachingItem("Procedimiento", "Procedure", "Describe cronológicamente lo realizado, hallazgos relevantes, complicaciones y medidas de hemostasia.", "Chronologically describe what was done, relevant findings, complications and hemostatic measures."),
        TeachingItem("Posoperatorio", "Postoperative care", "Documenta indicaciones, signos de alarma, seguimiento y cita de control.", "Document instructions, warning signs, follow-up and review appointment.")
    )
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Ficha quirúrgica","Surgical sheet"), onBack,
            tr(lang,"Cada rubro se abre para explicar qué documenta y por qué importa.","Each field opens to explain what it documents and why it matters.")) }
        items(sections) { ExpandableTeachingCard(it, lang) }
        item { NoticeCard(tr(lang,"Antes de procedimientos invasivos, los auxiliares y la interconsulta se solicitan cuando la historia, el examen y el riesgo lo justifican; no por rutina indiscriminada.","Before invasive procedures, tests and consultation are requested when history, examination and risk justify them; not indiscriminately.")) }
    }
}

@Composable
fun ConsentTeachingScreen(lang: String, onBack: () -> Unit) {
    val clauses = listOf(
        TeachingItem("1 · Veracidad de la historia clínica", "1 · Accuracy of the medical history", "El consentimiento parte de información clínica aportada de forma veraz. Si se omiten alergias, enfermedades o medicamentos, la toma de decisiones puede verse afectada.", "Consent relies on truthful clinical information. Omitted allergies, disease or medications can affect decisions."),
        TeachingItem("2 · Riesgos y posibles complicaciones", "2 · Risks and possible complications", "Debe explicarse que ningún procedimiento está libre de riesgo y que existen complicaciones previsibles que deben comprenderse antes de aceptar.", "Explain that no procedure is risk-free and that foreseeable complications should be understood before agreement."),
        TeachingItem("3 · Condiciones sistémicas", "3 · Systemic conditions", "Enfermedades sistémicas pueden modificar el riesgo y justificar interconsulta, estudios auxiliares o cambios del plan.", "Systemic disease can modify risk and justify consultation, testing or plan changes."),
        TeachingItem("4 · Alternativas e imprevistos", "4 · Alternatives and unforeseen findings", "El alumno debe entender que el consentimiento incluye alternativas razonables y que un hallazgo imprevisto puede obligar a detener o modificar la conducta bajo supervisión.", "Consent includes reasonable alternatives and an unforeseen finding may require stopping or changing care under supervision."),
        TeachingItem("5 · Firmas y capacidad para consentir", "5 · Signatures and capacity", "La firma documenta la decisión informada. En menores o personas sin capacidad legal correspondiente interviene el representante/tutor conforme a la normativa aplicable.", "The signature documents the informed decision. For minors or persons lacking legal capacity, the appropriate representative/guardian acts according to applicable law.")
    )
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Consentimiento informado","Informed consent"), onBack,
            tr(lang,"Ejemplo genérico comentado. No reproduce documentos institucionales ni sustituye asesoría jurídica.","Commented generic example. It does not reproduce an institutional form or replace legal advice.")) }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(tr(lang,"EJEMPLO EDUCATIVO","EDUCATIONAL EXAMPLE"), fontWeight = FontWeight.Bold)
                    Text(tr(lang,"Declaro haber proporcionado información clínica veraz. Se me explicó el diagnóstico, la naturaleza del procedimiento propuesto, beneficios esperados, riesgos relevantes, alternativas y la posibilidad de no realizarlo. Tuve oportunidad de formular preguntas y comprendo que pueden surgir hallazgos imprevistos que requieran reevaluación del plan. Mi decisión es voluntaria.",
                        "I declare that I provided truthful clinical information. The diagnosis, nature of the proposed procedure, expected benefits, relevant risks, alternatives and the option of not proceeding were explained to me. I had an opportunity to ask questions and understand that unforeseen findings may require reassessment. My decision is voluntary."))
                }
            }
        }
        items(clauses) { ExpandableTeachingCard(it, lang) }
        item { NoticeCard(tr(lang,"El contenido legal exacto depende del país, institución, procedimiento, edad/capacidad y normativa vigente. La app enseña la función de cada cláusula, no entrega un formato legal listo para uso clínico.","Exact legal content depends on jurisdiction, institution, procedure, age/capacity and current regulations. The app teaches the function of each clause; it is not a ready-to-use legal form.")) }
    }
}

private data class VitalReference(val group: String, val rr: String, val hr: String, val bp: String, val temp: String)

@Composable
fun VitalsTeachingScreen(lang: String, onBack: () -> Unit) {
    var ageGroup by remember { mutableStateOf(3) }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val refs = listOf(
        VitalReference("0–6 m", "30–50", "82–205 (0–3 m)", "60–90 / 30–62", "36.1–37.7 °C newborn"),
        VitalReference("6 m–2 a", "20–40 / 20–30", "100–190", "60–90 / 30–62", "≈37.2 °C infant"),
        VitalReference("2–7 a", "15–30", "60–140", "78–112 / 48–78", "≈37.0 °C"),
        VitalReference("8–11 a", "15–25", "60–140", "85–114 / 52–85", "≈37.0 °C"),
        VitalReference("≥12 a", "13–20", "60–100", "95–135 / 58–88 at 12 y", "adult ≈36.0 °C"),
        VitalReference(tr(lang,"Adulto","Adult"), "12–20", "60–100*", "100–140 / 60–90*", "≈36.0 °C*")
    )
    val ref = refs[ageGroup]
    val w = weight.toDoubleOrNull()
    val h = height.toDoubleOrNull()?.div(100.0)
    val bmi = if (w != null && h != null && h > 0) w / h.pow(2) else null

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Signos vitales y somatometría","Vital signs and anthropometry"), onBack,
            tr(lang,"Aprende cómo se toman, qué unidad se registra y compara con las referencias orientativas incluidas en el material docente.","Learn how they are measured, the recorded unit and compare with the teaching material's reference values.")) }
        item {
            SectionCard(tr(lang,"Grupo de edad","Age group")) {
                refs.chunked(3).forEachIndexed { row, list ->
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
                        list.forEachIndexed { col, r ->
                            val index = row * 3 + col
                            FilterChip(ageGroup == index, { ageGroup = index }, { Text(r.group) }, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang,"Referencia orientativa del material","Teaching-material reference")) {
                Text("🫁 FR: ${ref.rr} /min")
                Text("❤️ FC: ${ref.hr} /min")
                Text("🩺 TA: ${ref.bp} mmHg")
                Text("🌡️ ${tr(lang,"Temperatura","Temperature")}: ${ref.temp}")
            }
        }
        item {
            SectionCard(tr(lang,"Cómo tomar cada dato","How to measure each value")) {
                Text("• ${tr(lang,"FR: observar respiraciones sin avisar al paciente si es posible y contar ciclos completos por minuto o según protocolo.","RR: observe complete breathing cycles, preferably without altering the patient's pattern, according to protocol.")}")
                Text("• ${tr(lang,"FC: palpar pulso adecuado o usar monitor validado; registrar frecuencia y, si procede, ritmo.","HR: palpate an appropriate pulse or use a validated monitor; record rate and, when relevant, rhythm.")}")
                Text("• ${tr(lang,"TA: manguito de tamaño adecuado, paciente en reposo y técnica estandarizada.","BP: appropriate cuff size, patient at rest and standardized technique.")}")
                Text("• ${tr(lang,"Temperatura: anotar sitio/método si modifica la interpretación.","Temperature: document measurement site/method when it affects interpretation.")}")
            }
        }
        item {
            SectionCard("IMC / BMI") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(weight, { weight = it.filter { c -> c.isDigit() || c == '.' }.take(6) }, label = { Text("kg") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(height, { height = it.filter { c -> c.isDigit() || c == '.' }.take(6) }, label = { Text("cm") }, modifier = Modifier.weight(1f))
                }
                Text(if (bmi == null) tr(lang,"Escribe peso y talla para practicar el cálculo: kg / m².","Enter weight and height to practice: kg / m².") else "IMC = ${"%.1f".format(bmi)} kg/m²",
                    fontWeight = FontWeight.Bold)
                Text(tr(lang,"En niños y adolescentes el IMC se interpreta por edad y sexo mediante percentiles; no se clasifica con los puntos de corte de adultos.","In children/adolescents BMI is interpreted by age and sex percentiles; adult cutoffs do not apply."))
            }
        }
        item {
            SectionCard(tr(lang,"Glucosa capilar y laboratorios","Capillary glucose and laboratory tests")) {
                Text(tr(lang,"La app enseña cuándo podría ser relevante medir o solicitar estudios, pero no fija un único “valor normal” porque la interpretación depende de ayuno/no ayuno, edad, contexto clínico, método y rango del laboratorio. Usa el intervalo de referencia del equipo/laboratorio y las guías vigentes de tu institución.",
                    "The app teaches when measurement/testing may be relevant but does not impose one universal 'normal' value because interpretation depends on fasting state, age, clinical context, method and the reporting laboratory. Use the device/lab reference interval and current institutional guidance."))
            }
        }
        item { NoticeCard(tr(lang,"Los valores mostrados son los del material docente aportado y se presentan para aprender el llenado. Deben validarse contra referencias clínicas vigentes antes de usarlos para decisiones asistenciales.","Displayed values come from the supplied teaching material and are shown for completion practice. Validate them against current clinical references before patient-care decisions.")) }
    }
}
