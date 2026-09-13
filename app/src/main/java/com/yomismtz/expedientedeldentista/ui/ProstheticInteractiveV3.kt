package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val ProstV3Paper = Color(0xFFFFFCFF)
private val ProstV3Lilac = Color(0xFFD7C4EA)
private val ProstV3Purple = Color(0xFF7447A3)
private val ProstV3Deep = Color(0xFF43235F)
private val ProstV3Mint = Color(0xFFE3F7F3)
private val ProstV3Turquoise = Color(0xFF2EB9B1)
private val ProstV3Metal = Color(0xFFAAA3B2)
private val ProstV3Missing = Color(0xFFFFE0E3)
private val ProstV3Present = Color(0xFFE8F6EE)

private val prostV3Upper = listOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27)
private val prostV3Lower = listOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37)

private data class EdentulousSpaceV3(
    val teeth: List<Int>,
    val terminalRight: Boolean,
    val terminalLeft: Boolean,
    val crossesMidline: Boolean,
    val description: String
)

private data class KennedyAnalysisV3(
    val label: String,
    val classNumber: Int,
    val modifications: Int,
    val explanation: String,
    val spaces: List<EdentulousSpaceV3>,
    val applegateSteps: List<String>
)

private fun contiguousMissingV3(arch: List<Int>, present: Set<Int>): List<List<Int>> {
    val segments = mutableListOf<MutableList<Int>>()
    var current = mutableListOf<Int>()
    arch.forEach { tooth ->
        if (tooth !in present) {
            current.add(tooth)
        } else if (current.isNotEmpty()) {
            segments.add(current)
            current = mutableListOf()
        }
    }
    if (current.isNotEmpty()) segments.add(current)
    return segments
}

private fun rangeTextV3(teeth: List<Int>): String = when (teeth.size) {
    0 -> "—"
    1 -> teeth.first().toString()
    else -> "${teeth.first()}–${teeth.last()}"
}

private fun analyzeKennedyV3(
    arch: List<Int>,
    present: Set<Int>,
    ignoredSecondMolars: Set<Int>,
    lang: String
): KennedyAnalysisV3 {
    val effectiveArch = arch.filterNot { it in ignoredSecondMolars }
    val effectivePresent = present.intersect(effectiveArch.toSet())
    val upper = arch.firstOrNull() == 17
    val midlinePair = if (upper) setOf(11, 21) else setOf(41, 31)

    if (effectivePresent.isEmpty()) {
        return KennedyAnalysisV3(
            label = tr(lang, "Edéntulo total · Kennedy no aplica", "Completely edentulous · Kennedy does not apply"),
            classNumber = 0,
            modifications = 0,
            explanation = tr(lang,
                "No quedan dientes en el arco efectivo. Kennedy se reserva para arcos parcialmente edéntulos.",
                "No teeth remain in the effective arch. Kennedy is reserved for partially edentulous arches."),
            spaces = listOf(
                EdentulousSpaceV3(
                    effectiveArch,
                    terminalRight = true,
                    terminalLeft = true,
                    crossesMidline = true,
                    description = tr(lang, "Arco completamente edéntulo", "Completely edentulous arch")
                )
            ),
            applegateSteps = listOf(
                tr(lang, "1. Se asume que ya se realizaron o decidieron las extracciones que modificarán el arco.", "1. Extractions that will alter the arch are assumed to have been completed or decided."),
                tr(lang, "2. Los terceros molares no participan en este ejercicio porque no están incluidos en la arcada mostrada.", "2. Third molars do not participate in this exercise because they are not included in the displayed arch."),
                tr(lang, "3. Resultado: el arco es totalmente edéntulo; no corresponde asignar Kennedy.", "3. Result: the arch is completely edentulous; Kennedy should not be assigned.")
            )
        )
    }

    val rawSegments = contiguousMissingV3(effectiveArch, effectivePresent)
    if (rawSegments.isEmpty()) {
        return KennedyAnalysisV3(
            label = tr(lang, "Sin áreas edéntulas", "No edentulous areas"),
            classNumber = 0,
            modifications = 0,
            explanation = tr(lang, "Todos los dientes del arco efectivo están presentes.", "All teeth in the effective arch are present."),
            spaces = emptyList(),
            applegateSteps = listOf(
                tr(lang, "1. No se detectaron espacios edéntulos después de aplicar las exclusiones seleccionadas.", "1. No edentulous spaces were detected after applying the selected exclusions."),
                tr(lang, "2. Sin espacios edéntulos, Kennedy no es necesaria.", "2. Without edentulous spaces, Kennedy is not required.")
            )
        )
    }

    val spaces = rawSegments.map { segment ->
        val start = effectiveArch.indexOf(segment.first())
        val end = effectiveArch.indexOf(segment.last())
        val terminalRight = start == 0
        val terminalLeft = end == effectiveArch.lastIndex
        val crossesMidline = midlinePair.all { it in segment }
        val description = when {
            terminalRight && terminalLeft -> tr(lang, "Espacio que abarca todo el arco efectivo", "Space spanning the entire effective arch")
            terminalRight -> tr(lang, "Extensión distal del lado derecho", "Right distal-extension area")
            terminalLeft -> tr(lang, "Extensión distal del lado izquierdo", "Left distal-extension area")
            crossesMidline -> tr(lang, "Espacio anterior que cruza la línea media", "Anterior space crossing the midline")
            else -> tr(lang, "Espacio limitado por dientes", "Tooth-bounded edentulous space")
        }
        EdentulousSpaceV3(segment, terminalRight, terminalLeft, crossesMidline, description)
    }

    val terminalSpaces = spaces.count { it.terminalRight || it.terminalLeft }
    val anteriorSingleCrossMidline = spaces.size == 1 && spaces.first().crossesMidline && terminalSpaces == 0

    val cls = when {
        terminalSpaces >= 2 -> 1
        terminalSpaces == 1 -> 2
        anteriorSingleCrossMidline -> 4
        else -> 3
    }
    val modifications = when (cls) {
        1 -> (spaces.size - 2).coerceAtLeast(0)
        2, 3 -> (spaces.size - 1).coerceAtLeast(0)
        else -> 0
    }
    val roman = listOf("", "I", "II", "III", "IV")[cls]
    val modText = if (modifications > 0) tr(lang, " · modificación $modifications", " · modification $modifications") else ""
    val label = "Kennedy $roman$modText"

    val explanation = when (cls) {
        1 -> tr(lang,
            "Hay áreas edéntulas posteriores de extremo libre a ambos lados. Esas dos extensiones distales determinan la Clase I; cualquier otro espacio se cuenta como modificación.",
            "Posterior distal-extension areas exist on both sides. Those two distal extensions determine Class I; any additional space is counted as a modification.")
        2 -> tr(lang,
            "Hay una sola extensión distal posterior. Esa área más posterior determina la Clase II; los demás espacios, si existen, son modificaciones.",
            "There is one posterior distal extension. That most posterior area determines Class II; any other spaces are modifications."),
        3 -> tr(lang,
            "El área edéntula determinante está limitada por dientes por delante y por detrás. Los demás espacios se registran como modificaciones.",
            "The determining edentulous area is bounded by teeth anteriorly and posteriorly. Additional spaces are recorded as modifications."),
        else -> tr(lang,
            "Existe un único espacio edéntulo anterior que cruza la línea media y no hay un espacio posterior que cambie la clase. Por eso corresponde Clase IV y no admite modificaciones.",
            "There is a single anterior edentulous space crossing the midline and no posterior area that would change the class. Therefore it is Class IV and has no modifications.")
    }

    val ignoredText = if (ignoredSecondMolars.isEmpty()) {
        tr(lang, "ninguno", "none")
    } else ignoredSecondMolars.sorted().joinToString(", ")
    val detectedText = spaces.joinToString("; ") { "${rangeTextV3(it.teeth)}: ${it.description}" }
    val determiningText = when (cls) {
        1 -> tr(lang, "las dos extensiones distales posteriores", "the two posterior distal extensions")
        2 -> tr(lang, "la extensión distal posterior", "the posterior distal extension")
        3 -> tr(lang, "el espacio limitado más posterior", "the most posterior bounded space")
        else -> tr(lang, "el único espacio anterior que cruza la línea media", "the single anterior space crossing the midline")
    }

    val steps = listOf(
        tr(lang,
            "1. Regla 1: clasificar después de las extracciones que cambiarán el arco. En la app, marca como ausentes los dientes que representen el arco definitivo a clasificar.",
            "1. Rule 1: classify after extractions that will change the arch. In the app, mark as missing the teeth representing the definitive arch to classify."),
        tr(lang,
            "2. Reglas 2 y 3: los terceros molares no se muestran. Si un tercer molar fuera relevante como pilar en el caso real, debe valorarse fuera de este ejercicio simplificado.",
            "2. Rules 2 and 3: third molars are not displayed. If a third molar is relevant as an abutment in the real case, it must be considered outside this simplified exercise."),
        tr(lang,
            "3. Regla 4: segundos molares ausentes marcados como 'no se reemplazará': $ignoredText. Se excluyen de la longitud del arco para la clasificación.",
            "3. Rule 4: missing second molars marked 'will not be replaced': $ignoredText. They are excluded from the arch length for classification."),
        tr(lang,
            "4. Regla 5: espacios detectados automáticamente: $detectedText.",
            "4. Rule 5: automatically detected edentulous spaces: $detectedText."),
        tr(lang,
            "5. El área o áreas más posteriores que determinan la clase son: $determiningText.",
            "5. The most posterior area or areas determining the class are: $determiningText."),
        tr(lang,
            "6. Reglas 6 y 7: las áreas adicionales se cuentan por número, no por su tamaño. Modificaciones calculadas: $modifications.",
            "6. Rules 6 and 7: additional areas are counted by number, not by size. Calculated modifications: $modifications."),
        if (cls == 4)
            tr(lang, "7. Regla 8: la Clase IV no admite modificaciones; por eso el valor final es 0.", "7. Rule 8: Class IV has no modifications; therefore the final value is 0.")
        else
            tr(lang, "7. Regla 8: no aplica la restricción de Clase IV en este arco.", "7. Rule 8: the Class IV restriction does not apply to this arch."),
        tr(lang, "8. Resultado orientativo: $label. Confirma el caso con exploración, radiografías y supervisión docente.", "8. Educational result: $label. Confirm the case with examination, radiographs and faculty supervision.")
    )

    return KennedyAnalysisV3(label, cls, modifications, explanation, spaces, steps)
}

@Composable
fun ProstheticInteractiveV3Screen(lang: String, onBack: () -> Unit) {
    var openCompleteModule by remember { mutableStateOf(false) }
    if (openCompleteModule) {
        ProstheticInteractiveV2Screen(lang) { openCompleteModule = false }
        return
    }

    var upperPresent by remember { mutableStateOf(prostV3Upper.toSet()) }
    var lowerPresent by remember { mutableStateOf(prostV3Lower.toSet()) }
    var upperIgnoredSecondMolars by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var lowerIgnoredSecondMolars by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var seibert by remember { mutableStateOf(0) }
    var retainer by remember { mutableStateOf("RPI") }
    var retainerUpper by remember { mutableStateOf(true) }

    val upperAnalysis = analyzeKennedyV3(prostV3Upper, upperPresent, upperIgnoredSecondMolars, lang)
    val lowerAnalysis = analyzeKennedyV3(prostV3Lower, lowerPresent, lowerIgnoredSecondMolars, lang)
    val retainerAnalysis = if (retainerUpper) upperAnalysis else lowerAnalysis

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Prótesis v0.14 · Kennedy, Seibert y retenedores", "Prosthodontics v0.14 · Kennedy, Seibert and clasp assemblies"),
                onBack,
                tr(lang,
                    "Toca cada diente, detecta espacios edéntulos, aplica Applegate paso a paso y practica defectos de reborde y diseño de retenedores de PPR.",
                    "Tap each tooth, detect edentulous spaces, apply Applegate step by step and practice ridge defects and RPD clasp design.")
            )
        }

        item {
            ProstV3SectionCard(tr(lang, "1 · Dos arcadas interactivas", "1 · Two interactive arches")) {
                Text(tr(lang,
                    "Cada diente cambia entre PRESENTE y AUSENTE. El resultado se recalcula inmediatamente.",
                    "Each tooth toggles between PRESENT and MISSING. The result recalculates immediately."))
                Text(tr(lang, "MAXILAR", "MAXILLA"), fontWeight = FontWeight.Black, color = ProstV3Deep)
                ProstV3ArchEditor(prostV3Upper, upperPresent) { tooth ->
                    upperPresent = if (tooth in upperPresent) upperPresent - tooth else upperPresent + tooth
                    if (tooth in upperPresent) upperIgnoredSecondMolars = upperIgnoredSecondMolars - tooth
                }
                SecondMolarExclusionV3(
                    lang = lang,
                    candidates = listOf(17,27),
                    present = upperPresent,
                    ignored = upperIgnoredSecondMolars,
                    onToggle = { tooth ->
                        upperIgnoredSecondMolars = if (tooth in upperIgnoredSecondMolars) upperIgnoredSecondMolars - tooth else upperIgnoredSecondMolars + tooth
                    }
                )
                KennedySummaryV3(upperAnalysis, lang)

                Spacer(Modifier.height(8.dp))
                Text(tr(lang, "MANDÍBULA", "MANDIBLE"), fontWeight = FontWeight.Black, color = ProstV3Deep)
                ProstV3ArchEditor(prostV3Lower, lowerPresent) { tooth ->
                    lowerPresent = if (tooth in lowerPresent) lowerPresent - tooth else lowerPresent + tooth
                    if (tooth in lowerPresent) lowerIgnoredSecondMolars = lowerIgnoredSecondMolars - tooth
                }
                SecondMolarExclusionV3(
                    lang = lang,
                    candidates = listOf(47,37),
                    present = lowerPresent,
                    ignored = lowerIgnoredSecondMolars,
                    onToggle = { tooth ->
                        lowerIgnoredSecondMolars = if (tooth in lowerIgnoredSecondMolars) lowerIgnoredSecondMolars - tooth else lowerIgnoredSecondMolars + tooth
                    }
                )
                KennedySummaryV3(lowerAnalysis, lang)
            }
        }

        item {
            ProstV3SectionCard(tr(lang, "2 · Espacios edéntulos detectados", "2 · Detected edentulous spaces")) {
                SpaceListV3(tr(lang, "Maxilar", "Maxilla"), upperAnalysis, lang)
                SpaceListV3(tr(lang, "Mandíbula", "Mandible"), lowerAnalysis, lang)
            }
        }

        item {
            ProstV3SectionCard(tr(lang, "3 · Applegate aplicado paso a paso", "3 · Applegate applied step by step")) {
                Text(tr(lang, "MAXILAR", "MAXILLA"), fontWeight = FontWeight.Black, color = ProstV3Purple)
                upperAnalysis.applegateSteps.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
                Spacer(Modifier.height(8.dp))
                Text(tr(lang, "MANDÍBULA", "MANDIBLE"), fontWeight = FontWeight.Black, color = ProstV3Purple)
                lowerAnalysis.applegateSteps.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
        }

        item {
            ProstV3SectionCard(tr(lang, "4 · Seibert · defecto del reborde residual", "4 · Seibert · residual ridge defect")) {
                Text(tr(lang,
                    "Seibert complementa el análisis del sitio protésico; no sustituye a Kennedy. Selecciona el patrón que más se parece al defecto del reborde.",
                    "Seibert complements prosthetic-site analysis; it does not replace Kennedy. Select the pattern that best matches the ridge defect."))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(0 to tr(lang,"Sin defecto","No defect"),1 to "I",2 to "II",3 to "III").forEach { (id,label) ->
                        FilterChip(
                            selected = seibert == id,
                            onClick = { seibert = id },
                            label = { Text(label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                SeibertDiagramV3(seibert)
                Text(seibertTextV3(seibert, lang), fontWeight = FontWeight.SemiBold)
                Text(tr(lang,
                    "Úsalo para enseñar qué dimensión del reborde está perdida y por qué puede influir en el diseño del póntico, la higiene y la necesidad de acondicionamiento o manejo de tejidos.",
                    "Use it to teach which ridge dimension is lost and why it may influence pontic design, hygiene and the need for tissue conditioning or management."),
                    style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            ProstV3SectionCard(tr(lang, "5 · Diseñador visual de retenedores de PPR", "5 · Visual RPD clasp designer")) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(retainerUpper, { retainerUpper = true }, { Text(tr(lang,"Maxilar","Maxilla")) }, modifier = Modifier.weight(1f))
                    FilterChip(!retainerUpper, { retainerUpper = false }, { Text(tr(lang,"Mandíbula","Mandible")) }, modifier = Modifier.weight(1f))
                }
                listOf(
                    "Akers" to tr(lang,"Akers / circunferencial","Akers / circumferential"),
                    "RPI" to "RPI",
                    "RPA" to "RPA",
                    "I-bar" to tr(lang,"Barra I","I-bar"),
                    "Combination" to tr(lang,"Combinado · alambre forjado","Combination · wrought wire")
                ).chunked(2).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEach { (id,label) ->
                            FilterChip(retainer == id, { retainer = id }, { Text(label) }, modifier = Modifier.weight(1f))
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
                RetainerDiagramV3(retainer)
                Text(retainerTextV3(retainer, lang), style = MaterialTheme.typography.bodySmall)
                Card(
                    colors = CardDefaults.cardColors(containerColor = ProstV3Mint),
                    border = BorderStroke(1.dp, ProstV3Turquoise),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(tr(lang,"Sugerencia didáctica según el arco marcado","Teaching suggestion from the marked arch"), fontWeight = FontWeight.Black, color = ProstV3Deep)
                        Text(retainerSuggestionV3(retainerAnalysis, lang))
                    }
                }
                Text(tr(lang,
                    "Leyenda del esquema: morado = descanso/apoyo; turquesa = elemento retentivo; gris = reciprocación/placa proximal; línea oscura = conector menor.",
                    "Diagram legend: purple = rest/support; turquoise = retentive element; gray = reciprocation/proximal plate; dark line = minor connector."),
                    style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            ProstV3SectionCard(tr(lang, "6 · Redacción final orientativa", "6 · Educational final wording")) {
                Text(tr(lang,
                    "Maxilar: ${upperAnalysis.label}; espacios: ${spaceCompactV3(upperAnalysis, lang)}. Mandíbula: ${lowerAnalysis.label}; espacios: ${spaceCompactV3(lowerAnalysis, lang)}. Seibert: ${if (seibert == 0) "—" else seibert}. Diseño PPR sugerido a discutir: retenedor $retainer, descansos/planos guía/conector mayor según soporte y biomecánica del caso.",
                    "Maxilla: ${upperAnalysis.label}; spaces: ${spaceCompactV3(upperAnalysis, lang)}. Mandible: ${lowerAnalysis.label}; spaces: ${spaceCompactV3(lowerAnalysis, lang)}. Seibert: ${if (seibert == 0) "—" else seibert}. RPD design to discuss: $retainer clasp assembly, rests/guide planes/major connector according to support and case biomechanics."))
                Text(tr(lang,
                    "La app orienta el razonamiento; el diseño definitivo requiere análisis periodontal, radiográfico, oclusal, paralelizado y supervisión docente.",
                    "The app supports reasoning; definitive design requires periodontal, radiographic, occlusal and surveying analysis plus faculty supervision."),
                    style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            Button(onClick = { openCompleteModule = true }, modifier = Modifier.fillMaxWidth()) {
                Text(tr(lang,
                    "Abrir módulo protésico completo · PPR, total, fija, materiales, terminaciones y pónticos",
                    "Open complete prosthodontic module · RPD, complete, fixed, materials, finish lines and pontics"),
                    textAlign = TextAlign.Center)
            }
            Spacer(Modifier.height(6.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text(tr(lang,"Volver a Nota de ingreso","Back to intake note"))
            }
        }
    }
}

@Composable
private fun ProstV3SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ProstV3Paper),
        border = BorderStroke(1.dp, ProstV3Lilac),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontWeight = FontWeight.Black, color = ProstV3Deep, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
private fun ProstV3ArchEditor(arch: List<Int>, present: Set<Int>, onTooth: (Int) -> Unit) {
    arch.chunked(7).forEach { rowTeeth ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            rowTeeth.forEach { tooth ->
                val isPresent = tooth in present
                FilterChip(
                    selected = !isPresent,
                    onClick = { onTooth(tooth) },
                    label = {
                        Column {
                            Text("🦷 $tooth", fontWeight = FontWeight.Bold)
                            Text(if (isPresent) "P" else "A", style = MaterialTheme.typography.labelSmall)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
    Text(
        "P = ${tr("es","presente","present")} · A = ${tr("es","ausente","missing")}",
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
private fun SecondMolarExclusionV3(
    lang: String,
    candidates: List<Int>,
    present: Set<Int>,
    ignored: Set<Int>,
    onToggle: (Int) -> Unit
) {
    val absentCandidates = candidates.filter { it !in present }
    if (absentCandidates.isEmpty()) return
    Text(tr(lang,
        "Applegate · si un segundo molar ausente NO se reemplazará, márcalo aquí para excluirlo de la clasificación:",
        "Applegate · if a missing second molar will NOT be replaced, mark it here to exclude it from classification:"),
        style = MaterialTheme.typography.bodySmall)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        absentCandidates.forEach { tooth ->
            FilterChip(
                selected = tooth in ignored,
                onClick = { onToggle(tooth) },
                label = { Text("$tooth · ${tr(lang,"no reemplazar","do not replace")}") },
                modifier = Modifier.weight(1f)
            )
        }
        if (absentCandidates.size == 1) Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun KennedySummaryV3(a: KennedyAnalysisV3, lang: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ProstV3Mint),
        border = BorderStroke(1.dp, ProstV3Turquoise),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(a.label, fontWeight = FontWeight.Black, color = ProstV3Deep, style = MaterialTheme.typography.titleMedium)
            Text(a.explanation)
            Text(tr(lang,
                "Modificaciones: ${a.modifications}. La propuesta se basa únicamente en la distribución de dientes presente/ausente marcada en pantalla.",
                "Modifications: ${a.modifications}. The proposal is based only on the present/missing distribution marked on screen."),
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SpaceListV3(archName: String, a: KennedyAnalysisV3, lang: String) {
    Text(archName, fontWeight = FontWeight.Black, color = ProstV3Purple)
    if (a.spaces.isEmpty()) {
        Text(tr(lang,"Sin espacios edéntulos detectados.","No edentulous spaces detected."))
    } else {
        a.spaces.forEachIndexed { index, space ->
            Text("${index + 1}. ${rangeTextV3(space.teeth)} · ${space.description}")
        }
    }
}

private fun spaceCompactV3(a: KennedyAnalysisV3, lang: String): String =
    if (a.spaces.isEmpty()) tr(lang,"ninguno","none")
    else a.spaces.joinToString(" / ") { rangeTextV3(it.teeth) }

private fun seibertTextV3(cls: Int, lang: String): String = when (cls) {
    1 -> tr(lang,
        "Seibert I: pérdida de anchura bucolingual/labiolingual con altura apicocoronal relativamente conservada.",
        "Seibert I: buccolingual/labiolingual width loss with relatively preserved apicocoronal height.")
    2 -> tr(lang,
        "Seibert II: pérdida de altura apicocoronal con anchura bucolingual relativamente conservada.",
        "Seibert II: apicocoronal height loss with relatively preserved buccolingual width.")
    3 -> tr(lang,
        "Seibert III: pérdida combinada de anchura y altura del reborde.",
        "Seibert III: combined loss of ridge width and height.")
    else -> tr(lang,
        "Sin defecto seleccionado. Compara clínica y visualmente antes de asignar una clase.",
        "No defect selected. Compare clinically and visually before assigning a class.")
}

@Composable
private fun SeibertDiagramV3(cls: Int) {
    Canvas(Modifier.fillMaxWidth().height(150.dp)) {
        val mid = size.width / 2f
        val baseY = size.height * .72f
        val normalWidth = size.width * .42f
        val normalHeight = size.height * .42f
        val widthFactor = if (cls == 1 || cls == 3) .58f else 1f
        val heightFactor = if (cls == 2 || cls == 3) .58f else 1f
        val w = normalWidth * widthFactor
        val h = normalHeight * heightFactor

        drawRoundRect(
            color = ProstV3Lilac,
            topLeft = Offset(mid - w / 2f, baseY - h),
            size = Size(w, h),
            cornerRadius = CornerRadius(32f, 32f)
        )
        drawRoundRect(
            color = ProstV3Purple,
            topLeft = Offset(mid - normalWidth / 2f, baseY - normalHeight),
            size = Size(normalWidth, normalHeight),
            cornerRadius = CornerRadius(32f, 32f),
            style = Stroke(width = 4f)
        )
        drawLine(ProstV3Deep, Offset(mid - normalWidth / 2f, baseY + 12f), Offset(mid + normalWidth / 2f, baseY + 12f), strokeWidth = 3f)
    }
}

private fun retainerTextV3(id: String, lang: String): String = when (id) {
    "Akers" -> tr(lang,
        "Akers/circunferencial: conjunto colado suprabuldge con apoyo, brazo retentivo y componente recíproco. Es frecuente en situaciones dentosoportadas; la ubicación del apoyo y del brazo depende del diseño global.",
        "Akers/circumferential: cast suprabulge assembly with rest, retentive arm and reciprocal component. Common in tooth-supported situations; rest and arm location depend on the global design."),
    "RPI" -> tr(lang,
        "RPI = descanso mesial + placa proximal + barra I. Se usa como concepto de liberación de tensiones en extensiones distales cuando la anatomía vestibular y los tejidos permiten una barra de aproximación gingival.",
        "RPI = mesial Rest + Proximal plate + I-bar. It is used as a stress-releasing concept in distal extensions when vestibular anatomy and soft tissues permit a gingivally approaching bar."),
    "RPA" -> tr(lang,
        "RPA = descanso mesial + placa proximal + brazo tipo Akers. Puede considerarse cuando un RPI no es conveniente por profundidad vestibular, frenillos, socavados de tejidos u otras limitaciones anatómicas.",
        "RPA = mesial Rest + Proximal plate + Akers-type arm. It may be considered when RPI is unsuitable because of vestibular depth, frena, tissue undercuts or other anatomic limitations."),
    "I-bar" -> tr(lang,
        "Barra I: elemento retentivo infrabuldge que se aproxima desde gingival y contacta una zona retentiva del diente. Requiere trayecto de aproximación libre de interferencias de tejidos.",
        "I-bar: infrabulge retentive element approaching from the gingival direction and contacting a retentive tooth area. It requires an approach path free of tissue interference."),
    else -> tr(lang,
        "Combinado con alambre forjado: un brazo retentivo de alambre puede ofrecer mayor flexibilidad que un brazo colado. Debe integrarse con apoyo, reciprocación y control de la trayectoria de inserción.",
        "Combination clasp with wrought wire: a wrought-wire retentive arm can provide greater flexibility than a cast arm. It must be integrated with support, reciprocation and control of the path of insertion.")
}

private fun retainerSuggestionV3(a: KennedyAnalysisV3, lang: String): String = when (a.classNumber) {
    1, 2 -> tr(lang,
        "El patrón marcado incluye extensión distal. Como ejercicio, compara RPI y RPA y revisa retención indirecta, soporte de la base y control de rotación antes de decidir.",
        "The marked pattern includes a distal extension. As an exercise, compare RPI and RPA and review indirect retention, base support and rotational control before deciding."),
    3 -> tr(lang,
        "El patrón es principalmente dentosoportado. Compara un conjunto circunferencial/Akers con otras opciones según socavado, estética, paralelizado y condición del pilar.",
        "The pattern is mainly tooth-supported. Compare a circumferential/Akers assembly with other options according to undercut, esthetics, surveying and abutment condition."),
    4 -> tr(lang,
        "Clase IV exige atención especial a estética, soporte y trayectoria de inserción; la selección de retenedores no debe resolverse sólo por la clase de Kennedy.",
        "Class IV requires special attention to esthetics, support and path of insertion; clasp selection should not be decided from Kennedy class alone."),
    else -> tr(lang,
        "No hay una clase parcial definida en el arco seleccionado. Marca ausencias antes de usar esta sugerencia.",
        "No partial-edentulous class is defined in the selected arch. Mark missing teeth before using this suggestion.")
}

@Composable
private fun RetainerDiagramV3(id: String) {
    Canvas(Modifier.fillMaxWidth().height(190.dp)) {
        val cx = size.width / 2f
        val cy = size.height * .48f
        val toothW = size.width * .20f
        val toothH = size.height * .45f

        drawRoundRect(
            color = Color(0xFFFFF6D7),
            topLeft = Offset(cx - toothW / 2f, cy - toothH / 2f),
            size = Size(toothW, toothH),
            cornerRadius = CornerRadius(26f, 26f)
        )
        drawRoundRect(
            color = ProstV3Deep,
            topLeft = Offset(cx - toothW / 2f, cy - toothH / 2f),
            size = Size(toothW, toothH),
            cornerRadius = CornerRadius(26f, 26f),
            style = Stroke(width = 3f)
        )

        // Rest/support
        drawLine(ProstV3Purple, Offset(cx - toothW * .15f, cy - toothH * .46f), Offset(cx + toothW * .15f, cy - toothH * .46f), strokeWidth = 10f)
        // Minor connector
        drawLine(ProstV3Deep, Offset(cx, cy + toothH * .48f), Offset(cx, size.height * .88f), strokeWidth = 6f)

        when (id) {
            "RPI", "I-bar" -> {
                val p = Path().apply {
                    moveTo(cx - toothW * .48f, size.height * .86f)
                    quadraticBezierTo(cx - toothW * .62f, cy + toothH * .18f, cx - toothW * .44f, cy + toothH * .02f)
                }
                drawPath(p, ProstV3Turquoise, style = Stroke(width = 8f))
                drawLine(ProstV3Metal, Offset(cx + toothW * .48f, cy - toothH * .18f), Offset(cx + toothW * .48f, cy + toothH * .16f), strokeWidth = 10f)
            }
            "RPA" -> {
                val p = Path().apply {
                    moveTo(cx - toothW * .50f, cy - toothH * .10f)
                    quadraticBezierTo(cx - toothW * .72f, cy + toothH * .20f, cx - toothW * .30f, cy + toothH * .32f)
                }
                drawPath(p, ProstV3Turquoise, style = Stroke(width = 8f))
                drawLine(ProstV3Metal, Offset(cx + toothW * .48f, cy - toothH * .18f), Offset(cx + toothW * .48f, cy + toothH * .16f), strokeWidth = 10f)
            }
            "Combination" -> {
                val p = Path().apply {
                    moveTo(cx - toothW * .52f, cy - toothH * .10f)
                    cubicTo(cx - toothW * .70f, cy, cx - toothW * .62f, cy + toothH * .30f, cx - toothW * .18f, cy + toothH * .34f)
                }
                drawPath(p, ProstV3Turquoise, style = Stroke(width = 5f))
                drawLine(ProstV3Metal, Offset(cx + toothW * .48f, cy - toothH * .12f), Offset(cx + toothW * .48f, cy + toothH * .25f), strokeWidth = 10f)
            }
            else -> {
                val left = Path().apply {
                    moveTo(cx - toothW * .52f, cy - toothH * .08f)
                    quadraticBezierTo(cx - toothW * .70f, cy + toothH * .18f, cx - toothW * .18f, cy + toothH * .30f)
                }
                val right = Path().apply {
                    moveTo(cx + toothW * .52f, cy - toothH * .08f)
                    quadraticBezierTo(cx + toothW * .70f, cy + toothH * .18f, cx + toothW * .18f, cy + toothH * .30f)
                }
                drawPath(left, ProstV3Turquoise, style = Stroke(width = 8f))
                drawPath(right, ProstV3Metal, style = Stroke(width = 8f))
            }
        }
    }
}
