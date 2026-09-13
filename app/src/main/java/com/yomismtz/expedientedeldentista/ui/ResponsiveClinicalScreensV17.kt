package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.Surface as ToothSurface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus
import kotlin.math.round

// ---------- ODONTOGRAMA RESPONSIVO ----------

private data class V17Quadrant(val es: String, val en: String, val teeth: List<Int>)

private fun v17Quadrants(primary: Boolean): List<V17Quadrant> = if (primary) {
    listOf(
        V17Quadrant("Q5 · superior derecho", "Q5 · upper right", listOf(55,54,53,52,51)),
        V17Quadrant("Q6 · superior izquierdo", "Q6 · upper left", listOf(61,62,63,64,65)),
        V17Quadrant("Q8 · inferior derecho", "Q8 · lower right", listOf(85,84,83,82,81)),
        V17Quadrant("Q7 · inferior izquierdo", "Q7 · lower left", listOf(71,72,73,74,75))
    )
} else {
    listOf(
        V17Quadrant("Q1 · superior derecho", "Q1 · upper right", listOf(18,17,16,15,14,13,12,11)),
        V17Quadrant("Q2 · superior izquierdo", "Q2 · upper left", listOf(21,22,23,24,25,26,27,28)),
        V17Quadrant("Q4 · inferior derecho", "Q4 · lower right", listOf(48,47,46,45,44,43,42,41)),
        V17Quadrant("Q3 · inferior izquierdo", "Q3 · lower left", listOf(31,32,33,34,35,36,37,38))
    )
}

private fun markNameV17(mark: SurfaceMark, lang: String): String = when (mark) {
    SurfaceMark.HEALTHY -> tr(lang, "Limpiar", "Clear")
    SurfaceMark.CARIES -> tr(lang, "Caries", "Caries")
    SurfaceMark.RESTORATION -> tr(lang, "Restauración", "Restoration")
    SurfaceMark.SEALANT -> tr(lang, "Sellador", "Sealant")
}

@Composable
fun OdontogramResponsiveV17Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var primary by remember { mutableStateOf(false) }
    var selectedTooth by remember { mutableStateOf(16) }
    var selectedMark by remember { mutableStateOf(SurfaceMark.CARIES) }
    val quadrants = v17Quadrants(primary)
    val allTeeth = quadrants.flatMap { it.teeth }
    if (selectedTooth !in allTeeth) selectedTooth = allTeeth.first()

    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val marks = session.odontogramSurfaces[selectedTooth] ?: emptyMap()

    fun saveSurface(surface: ToothSurface) {
        val updated = marks.toMutableMap()
        if (selectedMark == SurfaceMark.HEALTHY) updated.remove(surface) else updated[surface] = selectedMark
        val derived = when {
            updated.values.any { it == SurfaceMark.CARIES } -> ToothStatus.CARIES
            updated.values.any { it == SurfaceMark.RESTORATION } -> ToothStatus.RESTORED
            updated.values.any { it == SurfaceMark.SEALANT } -> ToothStatus.SEALANT
            else -> ToothStatus.HEALTHY
        }
        onSessionChanged(
            session.copy(
                odontogramSurfaces = session.odontogramSurfaces + (selectedTooth to updated),
                teeth = session.teeth + (selectedTooth to record.copy(status = derived)),
                presentTeeth = session.presentTeeth + selectedTooth
            )
        )
    }

    fun setMissing(missing: Boolean) {
        val status = if (missing) ToothStatus.MISSING_OTHER else ToothStatus.HEALTHY
        val present = session.presentTeeth.toMutableSet()
        if (missing) present.remove(selectedTooth) else present.add(selectedTooth)
        onSessionChanged(session.copy(teeth = session.teeth + (selectedTooth to record.copy(status = status)), presentTeeth = present))
    }

    ResponsiveScreenV17(
        tr(lang, "Odontograma adaptable", "Adaptive odontogram"),
        tr(lang,
            "En teléfono pequeño muestra cuadrantes y controles en una sola columna; en pantallas amplias distribuye el trabajo sin reducir el tamaño del texto.",
            "Small phones use a single-column workflow; larger screens distribute the work without shrinking text."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang, "Dentición", "Dentition")) {
            AdaptiveGridV17(2, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val isPrimary = index == 1
                FilterChip(
                    selected = primary == isPrimary,
                    onClick = { primary = isPrimary },
                    label = { Text(if (isPrimary) tr(lang, "Temporal", "Primary") else tr(lang, "Permanente", "Permanent")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        val quadrantColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
        AdaptiveGridV17(quadrants.size, quadrantColumns) { qIndex ->
            val q = quadrants[qIndex]
            ResponsiveSectionV17(if (lang == "en") q.en else q.es) {
                val toothColumns = when {
                    profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT -> 4
                    else -> q.teeth.size.coerceAtMost(8)
                }
                AdaptiveGridV17(q.teeth.size, toothColumns) { tIndex ->
                    val tooth = q.teeth[tIndex]
                    val selected = selectedTooth == tooth
                    val hasMark = session.odontogramSurfaces[tooth]?.isNotEmpty() == true ||
                        session.teeth[tooth]?.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER, ToothStatus.EXTRACTION_INDICATED)
                    Card(
                        onClick = { selectedTooth = tooth },
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                selected -> MaterialTheme.colorScheme.primary
                                hasMark -> MaterialTheme.colorScheme.secondaryContainer
                                else -> MaterialTheme.colorScheme.surface
                            }
                        ),
                        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = .45f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.fillMaxWidth().padding(vertical = 9.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🦷")
                            Text(tooth.toString(), fontWeight = FontWeight.Black, color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        ResponsiveSectionV17("OD $selectedTooth", tr(lang, "Marca una o varias superficies", "Mark one or more surfaces")) {
            val markColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4
            AdaptiveGridV17(SurfaceMark.entries.size, markColumns) { index ->
                val mark = SurfaceMark.entries[index]
                FilterChip(
                    selected = selectedMark == mark,
                    onClick = { selectedMark = mark },
                    label = { Text(markNameV17(mark, lang)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            DentalSurfaceDiagram(
                centerEnabled = true,
                surfaceColor = { surface ->
                    when (marks[surface]) {
                        SurfaceMark.CARIES -> Color(0xFFE04B57)
                        SurfaceMark.RESTORATION -> Color(0xFF4C8DEB)
                        SurfaceMark.SEALANT -> Color(0xFF9B6AD6)
                        else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = .65f)
                    }
                },
                onSurfaceTap = { saveSurface(it) },
                modifier = Modifier.fillMaxWidth()
            )

            AdaptiveGridV17(2, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val missing = index == 1
                FilterChip(
                    selected = if (missing) record.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER) else record.status !in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER),
                    onClick = { setMissing(missing) },
                    label = { Text(if (missing) tr(lang, "Diente ausente", "Missing tooth") else tr(lang, "Diente presente", "Present tooth")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        NoticeCard(tr(lang,
            "Qué escribir: registra el órgano dentario y las superficies afectadas usando la simbología de tu expediente físico. Esta pantalla es una guía de aprendizaje y no sustituye la valoración clínica.",
            "What to write: record the tooth and affected surfaces using the symbols required by your physical record. This screen is a learning guide and does not replace clinical assessment."))
    }
}

// ---------- IPC RESPONSIVO ----------

private data class V17Sextant(val number: Int, val label: String, val teeth: List<Int>)
private val v17Sextants = listOf(
    V17Sextant(1, "18–14", listOf(18,17,16,15,14)),
    V17Sextant(2, "13–23", listOf(13,12,11,21,22,23)),
    V17Sextant(3, "24–28", listOf(24,25,26,27,28)),
    V17Sextant(4, "34–38", listOf(34,35,36,37,38)),
    V17Sextant(5, "33–43", listOf(33,32,31,41,42,43)),
    V17Sextant(6, "44–48", listOf(44,45,46,47,48))
)
private val v17Sites = listOf("MV","V","DV","ML/P","L/P","DL/P")
private fun highestCodeV17(values: List<String>): String = values.mapNotNull { it.toIntOrNull() }.maxOrNull()?.toString() ?: "X"

@Composable
fun IpcResponsiveV17Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var sextantIndex by remember { mutableStateOf(0) }
    var selectedTooth by remember { mutableStateOf(v17Sextants.first().teeth.first()) }
    var selectedSite by remember { mutableStateOf(0) }
    var siteCodes by remember { mutableStateOf<Map<Int, List<String>>>(emptyMap()) }

    fun toothCodes(tooth: Int) = siteCodes[tooth] ?: List(6) { "0" }
    fun toothResult(tooth: Int) = highestCodeV17(toothCodes(tooth))
    fun sextantResult(s: V17Sextant): String {
        val results = s.teeth.map { toothResult(it) }
        return if (results.count { it != "X" } < 2) "X" else highestCodeV17(results)
    }
    fun updateSite(code: String) {
        val list = toothCodes(selectedTooth).toMutableList()
        list[selectedSite] = code
        val updated = siteCodes + (selectedTooth to list)
        siteCodes = updated
        val summary = v17Sextants.map { s ->
            val results = s.teeth.map { highestCodeV17(updated[it] ?: List(6) { "0" }) }
            if (results.count { it != "X" } < 2) "X" else highestCodeV17(results)
        }
        onSessionChanged(session.copy(ipcCodes = summary))
    }

    ResponsiveScreenV17(
        tr(lang, "IPC por sextantes", "CPI by sextants"),
        tr(lang, "Flujo adaptable: sextante → diente → sitio → código.", "Adaptive workflow: sextant → tooth → site → code."),
        onBack
    ) { profile ->
        val sextantColumns = when {
            profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT -> 2
            profile.width == ScreenWidthV17.MEDIUM -> 3
            else -> 3
        }
        ResponsiveSectionV17(tr(lang, "1 · Selecciona sextante", "1 · Select sextant")) {
            AdaptiveGridV17(v17Sextants.size, sextantColumns) { index ->
                val s = v17Sextants[index]
                Card(
                    onClick = { sextantIndex = index; selectedTooth = s.teeth.first(); selectedSite = 0 },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = if (sextantIndex == index) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, if (sextantIndex == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha=.4f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("S${s.number}", fontWeight = FontWeight.Black)
                        Text(s.label, style = MaterialTheme.typography.bodyMedium)
                        Text("${tr(lang,"Código","Code")} ${sextantResult(s)}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        val selectedSextant = v17Sextants[sextantIndex]
        ResponsiveSectionV17("${tr(lang,"2 · Dientes S","2 · Teeth S")}${selectedSextant.number}") {
            val columns = when {
                profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT -> 2
                profile.width == ScreenWidthV17.MEDIUM -> 3
                else -> selectedSextant.teeth.size
            }
            AdaptiveGridV17(selectedSextant.teeth.size, columns) { index ->
                val tooth = selectedSextant.teeth[index]
                FilterChip(
                    selected = selectedTooth == tooth,
                    onClick = { selectedTooth = tooth; selectedSite = 0 },
                    label = { Text("OD $tooth · ${toothResult(tooth)}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17("${tr(lang,"3 · Seis sitios OD","3 · Six sites tooth")} $selectedTooth") {
            val siteColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
            AdaptiveGridV17(v17Sites.size, siteColumns) { index ->
                FilterChip(
                    selected = selectedSite == index,
                    onClick = { selectedSite = index },
                    label = { Text("${v17Sites[index]} · ${toothCodes(selectedTooth)[index]}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text("${tr(lang,"Código para","Code for")} ${v17Sites[selectedSite]}", fontWeight = FontWeight.Black)
            val codeColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 3 else 6
            AdaptiveGridV17(6, codeColumns) { index ->
                val code = listOf("0","1","2","3","4","X")[index]
                FilterChip(
                    selected = toothCodes(selectedTooth)[selectedSite] == code,
                    onClick = { updateSite(code) },
                    label = { Text(code) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "Resultado automático", "Automatic result")) {
            Text(v17Sextants.joinToString(" · ") { "S${it.number}=${sextantResult(it)}" }, fontWeight = FontWeight.Black)
            Text(tr(lang,
                "0 sin hallazgo · 1 sangrado · 2 cálculo/factor retentivo · 3 bolsa 4–5 mm · 4 bolsa ≥6 mm · X no evaluable.",
                "0 no finding · 1 bleeding · 2 calculus/retentive factor · 3 pocket 4–5 mm · 4 pocket ≥6 mm · X not evaluable."))
        }
        NoticeCard(tr(lang,
            "Qué escribir: IPC: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Añade los hallazgos periodontales relevantes.",
            "What to write: CPI: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Add relevant periodontal findings."))
    }
}

// ---------- IHOS RESPONSIVO ----------

private data class V17IhosSlot(val tooth: Int, val surfaceEs: String, val surfaceEn: String, val substitutes: List<Int>)
private val v17IhosSlots = listOf(
    V17IhosSlot(16,"Vestibular","Buccal",listOf(17,18)),
    V17IhosSlot(11,"Labial","Labial",listOf(21)),
    V17IhosSlot(26,"Vestibular","Buccal",listOf(27,28)),
    V17IhosSlot(36,"Lingual","Lingual",listOf(37,38)),
    V17IhosSlot(31,"Labial","Labial",listOf(41)),
    V17IhosSlot(46,"Lingual","Lingual",listOf(47,48))
)
private fun round1V17(value: Double): Double = round(value * 10.0) / 10.0

@Composable
fun IhosResponsiveV17Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedSlotIndex by remember { mutableStateOf(0) }
    var selections by remember { mutableStateOf(v17IhosSlots.associate { it.tooth to it.tooth }) }
    var excluded by remember { mutableStateOf<Set<Int>>(emptySet()) }
    val selectedSlot = v17IhosSlots[selectedSlotIndex]
    val selectedTooth = selections[selectedSlot.tooth] ?: selectedSlot.tooth

    fun scoreMap(debris: Boolean, value: Int) {
        if (debris) onSessionChanged(session.copy(ihosDebris = session.ihosDebris + (selectedTooth to value)))
        else onSessionChanged(session.copy(ihosCalculus = session.ihosCalculus + (selectedTooth to value)))
    }
    val evaluable = v17IhosSlots.filter { it.tooth !in excluded }
    val debris = if (evaluable.isEmpty()) 0.0 else evaluable.map { session.ihosDebris[selections[it.tooth] ?: it.tooth] ?: 0 }.average()
    val calculus = if (evaluable.isEmpty()) 0.0 else evaluable.map { session.ihosCalculus[selections[it.tooth] ?: it.tooth] ?: 0 }.average()
    val dAvg = round1V17(debris)
    val cAvg = round1V17(calculus)
    val total = round1V17(dAvg + cAvg)
    val interpretation = when {
        total <= 1.2 -> tr(lang,"Buena","Good")
        total <= 3.0 -> tr(lang,"Regular","Fair")
        else -> tr(lang,"Mala","Poor")
    }

    ResponsiveScreenV17(
        "IHOS / OHI-S",
        tr(lang, "Dientes índice y sustitutos organizados para cualquier tamaño de pantalla.", "Index teeth and substitutes arranged for any screen size."),
        onBack
    ) { profile ->
        val slotColumns = when {
            profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT -> 2
            else -> 3
        }
        ResponsiveSectionV17(tr(lang,"1 · Dientes índice","1 · Index teeth")) {
            AdaptiveGridV17(v17IhosSlots.size, slotColumns) { index ->
                val slot = v17IhosSlots[index]
                val current = selections[slot.tooth] ?: slot.tooth
                Card(
                    onClick = { selectedSlotIndex = index },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = if (selectedSlotIndex == index) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, if (selectedSlotIndex == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha=.4f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("OD $current", fontWeight = FontWeight.Black)
                        Text(if (lang == "en") slot.surfaceEn else slot.surfaceEs, style = MaterialTheme.typography.bodyMedium)
                        if (current != slot.tooth) Text("${tr(lang,"sustituye","substitutes")} ${slot.tooth}", color = MaterialTheme.colorScheme.primary)
                        if (slot.tooth in excluded) Text(tr(lang,"No evaluable","Not evaluable"), color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        ResponsiveSectionV17("${tr(lang,"2 · Sitio seleccionado","2 · Selected site")}: ${selectedSlot.tooth}") {
            val candidates = listOf(selectedSlot.tooth) + selectedSlot.substitutes
            Text(tr(lang,"Selecciona el diente índice o uno de sus sustitutos permitidos.","Select the index tooth or an allowed substitute."))
            AdaptiveGridV17(candidates.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else candidates.size.coerceAtMost(3)) { index ->
                val tooth = candidates[index]
                FilterChip(
                    selected = selectedTooth == tooth && selectedSlot.tooth !in excluded,
                    onClick = { selections = selections + (selectedSlot.tooth to tooth); excluded = excluded - selectedSlot.tooth },
                    label = { Text("OD $tooth") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            FilterChip(
                selected = selectedSlot.tooth in excluded,
                onClick = {
                    excluded = if (selectedSlot.tooth in excluded) excluded - selectedSlot.tooth else excluded + selectedSlot.tooth
                },
                label = { Text(tr(lang,"Sin diente evaluable para este sitio","No evaluable tooth for this site")) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        ResponsiveSectionV17(tr(lang,"3 · Detritos y cálculo","3 · Debris and calculus")) {
            Text("${tr(lang,"Detritos","Debris")} · OD $selectedTooth", fontWeight = FontWeight.Bold)
            AdaptiveGridV17(4, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4) { index ->
                FilterChip(
                    selected = session.ihosDebris[selectedTooth] == index,
                    onClick = { scoreMap(true, index) },
                    label = { Text(index.toString()) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text("${tr(lang,"Cálculo","Calculus")} · OD $selectedTooth", fontWeight = FontWeight.Bold)
            AdaptiveGridV17(4, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4) { index ->
                FilterChip(
                    selected = session.ihosCalculus[selectedTooth] == index,
                    onClick = { scoreMap(false, index) },
                    label = { Text(index.toString()) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(tr(lang,
                "0 = ausente · 1 = hasta 1/3 · 2 = >1/3 hasta 2/3 · 3 = >2/3. Usa los criterios docentes del módulo para distinguir detritos y cálculo.",
                "0 = absent · 1 = up to 1/3 · 2 = >1/3 to 2/3 · 3 = >2/3. Use the module teaching criteria to distinguish debris and calculus."))
        }

        ResponsiveSectionV17(tr(lang,"Resultado automático","Automatic result")) {
            Text("ID-S $dAvg + IC-S $cAvg = IHOS $total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text("${tr(lang,"Interpretación","Interpretation")}: $interpretation", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        NoticeCard(tr(lang,
            "Qué escribir: IHOS = ___ (ID-S ___ + IC-S ___). Añade la interpretación obtenida.",
            "What to write: OHI-S = ___ (DI-S ___ + CI-S ___). Add the resulting interpretation."))
    }
}

// ---------- KENNEDY / PRÓTESIS RESPONSIVA ----------

private val upperArchV17 = listOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27)
private val lowerArchV17 = listOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37)

private data class KSpaceV17(val teeth: List<Int>, val rightEnd: Boolean, val leftEnd: Boolean, val crossesMidline: Boolean)
private data class KResultV17(val label: String, val explanation: String, val spaces: List<KSpaceV17>, val steps: List<String>)

private fun missingSegmentsV17(arch: List<Int>, present: Set<Int>): List<List<Int>> {
    val out = mutableListOf<List<Int>>()
    var current = mutableListOf<Int>()
    arch.forEach { tooth ->
        if (tooth !in present) current.add(tooth)
        else if (current.isNotEmpty()) { out.add(current.toList()); current = mutableListOf() }
    }
    if (current.isNotEmpty()) out.add(current.toList())
    return out
}

private fun kennedyV17(arch: List<Int>, present: Set<Int>, ignored: Set<Int>, lang: String): KResultV17 {
    val effective = arch.filterNot { it in ignored }
    val effectivePresent = present.intersect(effective.toSet())
    val upper = arch.first() == 17
    val centralPair = if (upper) setOf(11,21) else setOf(41,31)
    if (effectivePresent.isEmpty()) {
        return KResultV17(
            tr(lang,"Edéntulo total · Kennedy no aplica","Completely edentulous · Kennedy not applicable"),
            tr(lang,"No quedan dientes en el arco efectivo; usa el protocolo de prótesis total.","No teeth remain in the effective arch; use the complete denture protocol."),
            emptyList(),
            listOf(tr(lang,"1. Kennedy se usa para arcos parcialmente edéntulos.","1. Kennedy is used for partially edentulous arches."))
        )
    }
    val segments = missingSegmentsV17(effective, effectivePresent)
    if (segments.isEmpty()) {
        return KResultV17(tr(lang,"Sin espacios edéntulos","No edentulous spaces"), tr(lang,"Todos los dientes del arco efectivo están presentes.","All teeth in the effective arch are present."), emptyList(), emptyList())
    }
    val spaces = segments.map { seg ->
        val start = effective.indexOf(seg.first())
        val end = effective.indexOf(seg.last())
        KSpaceV17(seg, start == 0, end == effective.lastIndex, centralPair.all { it in seg })
    }
    val terminalCount = spaces.count { it.rightEnd || it.leftEnd }
    val cls = when {
        terminalCount >= 2 -> 1
        terminalCount == 1 -> 2
        spaces.size == 1 && spaces.first().crossesMidline -> 4
        else -> 3
    }
    val mods = when (cls) { 1 -> (spaces.size - 2).coerceAtLeast(0); 2,3 -> (spaces.size - 1).coerceAtLeast(0); else -> 0 }
    val roman = listOf("","I","II","III","IV")[cls]
    val label = "Kennedy $roman" + if (mods > 0) tr(lang," · modificación $mods"," · modification $mods") else ""
    val determinant = when (cls) {
        1 -> tr(lang,"dos extensiones distales bilaterales","two bilateral distal extensions")
        2 -> tr(lang,"una extensión distal unilateral","one unilateral distal extension")
        3 -> tr(lang,"el espacio limitado posterior","the posterior tooth-bounded space")
        else -> tr(lang,"el único espacio anterior que cruza la línea media","the single anterior space crossing the midline")
    }
    val explanation = tr(lang,"La clase la determina $determinant. Las áreas adicionales se cuentan como modificaciones.","The class is determined by $determinant. Additional areas are counted as modifications.")
    val ignoredText = if (ignored.isEmpty()) tr(lang,"ninguno","none") else ignored.sorted().joinToString(", ")
    val steps = listOf(
        tr(lang,"1. Regla 1: clasifica después de las extracciones que cambiarán el arco.","1. Rule 1: classify after extractions that will alter the arch."),
        tr(lang,"2. Reglas 2–3: los terceros molares no se muestran; si serán pilares se valoran aparte.","2. Rules 2–3: third molars are not shown; if used as abutments, assess them separately."),
        tr(lang,"3. Regla 4: segundos molares ausentes marcados como no reemplazar: $ignoredText.","3. Rule 4: missing second molars marked as not replaced: $ignoredText."),
        tr(lang,"4. Regla 5: el área edéntula más posterior determina la clase.","4. Rule 5: the most posterior edentulous area determines the class."),
        tr(lang,"5. Reglas 6–7: las modificaciones se cuentan por número, no por tamaño. Resultado: $mods.","5. Rules 6–7: modifications are counted by number, not size. Result: $mods."),
        if (cls == 4) tr(lang,"6. Regla 8: la Clase IV no admite modificaciones.","6. Rule 8: Class IV has no modifications.") else tr(lang,"6. Regla 8: la restricción de Clase IV no aplica.","6. Rule 8: the Class IV restriction does not apply."),
        tr(lang,"7. Resultado orientativo: $label.","7. Educational result: $label.")
    )
    return KResultV17(label, explanation, spaces, steps)
}

@Composable
fun ProstheticResponsiveV17Screen(lang: String, onBack: () -> Unit) {
    var fullModule by remember { mutableStateOf(false) }
    if (fullModule) {
        ProstheticInteractiveV2Screen(lang) { fullModule = false }
        return
    }
    var upperPresent by remember { mutableStateOf(upperArchV17.toSet()) }
    var lowerPresent by remember { mutableStateOf(lowerArchV17.toSet()) }
    var upperIgnored by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var lowerIgnored by remember { mutableStateOf<Set<Int>>(emptySet()) }

    val upperResult = kennedyV17(upperArchV17, upperPresent, upperIgnored, lang)
    val lowerResult = kennedyV17(lowerArchV17, lowerPresent, lowerIgnored, lang)

    ResponsiveScreenV17(
        tr(lang,"Prótesis · Kennedy adaptable","Prosthodontics · Adaptive Kennedy"),
        tr(lang,"Dos arcadas, dientes presente/ausente, espacios edéntulos, clase, modificaciones y Applegate paso a paso.","Two arches, present/missing teeth, edentulous spaces, class, modifications and Applegate step by step."),
        onBack
    ) { profile ->
        KennedyArchSectionV17(lang, tr(lang,"MAXILAR","MAXILLA"), upperArchV17, upperPresent, upperIgnored, upperResult, profile,
            onToggle = { tooth -> upperPresent = if (tooth in upperPresent) upperPresent - tooth else upperPresent + tooth; if (tooth in upperPresent) upperIgnored = upperIgnored - tooth },
            onIgnore = { tooth -> upperIgnored = if (tooth in upperIgnored) upperIgnored - tooth else upperIgnored + tooth })
        KennedyArchSectionV17(lang, tr(lang,"MANDÍBULA","MANDIBLE"), lowerArchV17, lowerPresent, lowerIgnored, lowerResult, profile,
            onToggle = { tooth -> lowerPresent = if (tooth in lowerPresent) lowerPresent - tooth else lowerPresent + tooth; if (tooth in lowerPresent) lowerIgnored = lowerIgnored - tooth },
            onIgnore = { tooth -> lowerIgnored = if (tooth in lowerIgnored) lowerIgnored - tooth else lowerIgnored + tooth })

        ResponsiveSectionV17(tr(lang,"Diseño protésico completo","Complete prosthetic design")) {
            Text(tr(lang,
                "Abre el módulo para diseñar PPR, descansos, conectores y retenedores; revisar Seibert; prótesis total; prótesis fija, pilares, pónticos, materiales y terminaciones.",
                "Open the module to design RPDs, rests, connectors and clasp assemblies; review Seibert; complete dentures; fixed prostheses, abutments, pontics, materials and finish lines."))
            Button(onClick = { fullModule = true }, modifier = Modifier.fillMaxWidth()) {
                Text(tr(lang,"Abrir diseñador protésico completo","Open complete prosthetic designer"), fontWeight = FontWeight.Black)
            }
        }
        NoticeCard(tr(lang,
            "Qué escribir: arco ___; Kennedy ___ mod. ___ cuando aplique; espacios edéntulos ___; pilares candidatos ___; tipo de prótesis ___; diseño ___; etapa clínica ___; indicaciones y seguimiento ___.",
            "What to write: arch ___; Kennedy ___ mod. ___ when applicable; edentulous areas ___; candidate abutments ___; prosthesis type ___; design ___; clinical stage ___; instructions and follow-up ___."))
    }
}

@Composable
private fun KennedyArchSectionV17(
    lang: String,
    title: String,
    arch: List<Int>,
    present: Set<Int>,
    ignored: Set<Int>,
    result: KResultV17,
    profile: ScreenProfileV17,
    onToggle: (Int) -> Unit,
    onIgnore: (Int) -> Unit
) {
    ResponsiveSectionV17(title, tr(lang,"Toca cada diente: presente ↔ ausente","Tap each tooth: present ↔ missing")) {
        val columns = when {
            profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT -> 4
            profile.width == ScreenWidthV17.MEDIUM -> 7
            else -> 14
        }
        AdaptiveGridV17(arch.size, columns) { index ->
            val tooth = arch[index]
            val isPresent = tooth in present
            Card(
                onClick = { onToggle(tooth) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isPresent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer),
                border = BorderStroke(1.dp, if (isPresent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (isPresent) "🦷" else "—")
                    Text(tooth.toString(), fontWeight = FontWeight.Black)
                    Text(if (isPresent) "P" else "A", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        val secondMolars = if (arch.first() == 17) listOf(17,27) else listOf(47,37)
        secondMolars.filter { it !in present }.forEach { tooth ->
            FilterChip(
                selected = tooth in ignored,
                onClick = { onIgnore(tooth) },
                label = { Text("OD $tooth · ${tr(lang,"ausente y no se reemplazará","missing and will not be replaced")}") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(result.label, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text(result.explanation)
                if (result.spaces.isNotEmpty()) {
                    Text(tr(lang,"Espacios detectados:","Detected spaces:"), fontWeight = FontWeight.Bold)
                    result.spaces.forEachIndexed { index, space ->
                        val range = if (space.teeth.size == 1) space.teeth.first().toString() else "${space.teeth.first()}–${space.teeth.last()}"
                        Text("${index + 1}. $range")
                    }
                }
            }
        }

        if (result.steps.isNotEmpty()) {
            Text(tr(lang,"Reglas de Applegate aplicadas","Applied Applegate rules"), fontWeight = FontWeight.Black)
            result.steps.forEach { Text(it) }
        }
    }
}
