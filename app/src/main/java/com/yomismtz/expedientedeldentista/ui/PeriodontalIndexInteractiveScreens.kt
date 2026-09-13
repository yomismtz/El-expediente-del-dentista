package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import kotlin.math.round

private val IndexLavender = Color(0xFFE9DDF5)
private val IndexLilac = Color(0xFFD3BCE9)
private val IndexPurple = Color(0xFF7447A3)
private val IndexDeep = Color(0xFF43235F)
private val IndexMint = Color(0xFF66D6C7)
private val IndexTurquoise = Color(0xFF2EB9B1)
private val IndexMetal = Color(0xFFB8B1C0)
private val IndexPaper = Color(0xFFFFFCFF)

private val ipcSites = listOf("MV", "V", "DV", "ML/P", "L/P", "DL/P")
private val ipcCodeOrder = listOf("0", "1", "2", "3", "4")

private data class IpcSextant(val number: Int, val label: String, val teeth: List<Int>)
private val ipcSextants = listOf(
    IpcSextant(1, "18–14", listOf(18, 17, 16, 15, 14)),
    IpcSextant(2, "13–23", listOf(13, 12, 11, 21, 22, 23)),
    IpcSextant(3, "24–28", listOf(24, 25, 26, 27, 28)),
    IpcSextant(4, "34–38", listOf(34, 35, 36, 37, 38)),
    IpcSextant(5, "33–43", listOf(33, 32, 31, 41, 42, 43)),
    IpcSextant(6, "44–48", listOf(44, 45, 46, 47, 48))
)

private fun highestIpc(codes: List<String>): String {
    val numeric = codes.mapNotNull { it.toIntOrNull() }
    return if (numeric.isNotEmpty()) numeric.maxOrNull().toString() else "X"
}

@Composable
fun IpcInteractiveV2Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedSextant by remember { mutableStateOf(0) }
    var selectedTooth by remember { mutableStateOf(ipcSextants.first().teeth.first()) }
    var selectedSite by remember { mutableStateOf(0) }
    var siteCodes by remember { mutableStateOf<Map<Int, List<String>>>(emptyMap()) }

    fun toothCodes(tooth: Int): List<String> = siteCodes[tooth] ?: List(6) { "0" }
    fun toothResult(tooth: Int): String = highestIpc(toothCodes(tooth))
    fun sextantResult(s: IpcSextant): String {
        val toothResults = s.teeth.map { toothResult(it) }
        val evaluable = toothResults.count { it != "X" }
        return if (evaluable < 2) "X" else highestIpc(toothResults)
    }
    fun syncSession(newMap: Map<Int, List<String>>) {
        siteCodes = newMap
        val sextantCodes = ipcSextants.map { s ->
            val results = s.teeth.map { tooth -> highestIpc(newMap[tooth] ?: List(6) { "0" }) }
            val evaluable = results.count { it != "X" }
            if (evaluable < 2) "X" else highestIpc(results)
        }
        onSessionChanged(session.copy(ipcCodes = sextantCodes))
    }

    LazyColumn(
        Modifier.fillMaxSize().safeDrawingPadding().background(
            Brush.verticalGradient(listOf(IndexLavender, Color(0xFFF8F4FB), Color.White))
        ).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "IPC por sextantes", "CPI by sextants"),
                onBack,
                tr(lang,
                    "Selecciona el sextante, después el diente y finalmente uno de sus seis sitios. La app conserva el hallazgo de mayor código por diente y por sextante.",
                    "Select the sextant, then the tooth, then one of its six sites. The app retains the highest finding for each tooth and sextant."
                )
            )
        }
        item {
            SectionCard(tr(lang, "1 · Selecciona un sextante", "1 · Select a sextant")) {
                ipcSextants.chunked(3).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEachIndexed { _, s ->
                            val idx = ipcSextants.indexOf(s)
                            Card(
                                modifier = Modifier.weight(1f).clickable {
                                    selectedSextant = idx
                                    selectedTooth = s.teeth.first()
                                    selectedSite = 0
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedSextant == idx) IndexMint.copy(alpha = .36f) else IndexPaper
                                ),
                                border = BorderStroke(1.dp, if (selectedSextant == idx) IndexTurquoise else IndexLilac)
                            ) {
                                Column(Modifier.padding(9.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("S${s.number}", fontWeight = FontWeight.Black, color = IndexDeep)
                                    Text(s.label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                    Text("Código ${sextantResult(s)}", style = MaterialTheme.typography.labelSmall, color = IndexPurple)
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
        item {
            val sextant = ipcSextants[selectedSextant]
            SectionCard("${tr(lang, "2 · Dientes del sextante", "2 · Teeth in sextant")} ${sextant.number}") {
                Text(tr(lang, "Toca cada diente para revisar sus seis sitios.", "Tap each tooth to review its six sites."))
                sextant.teeth.chunked(3).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEach { tooth ->
                            FilterChip(
                                selected = selectedTooth == tooth,
                                onClick = { selectedTooth = tooth; selectedSite = 0 },
                                label = { Text("OD $tooth · ${toothResult(tooth)}") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
        item {
            SectionCard("${tr(lang, "3 · Sitios del OD", "3 · Tooth sites")} $selectedTooth") {
                Text(tr(lang,
                    "MV = mesio-vestibular, V = vestibular, DV = disto-vestibular, ML/P = mesio-lingual/palatino, L/P = lingual/palatino, DL/P = disto-lingual/palatino.",
                    "MV = mesiobuccal, B = buccal, DB = distobuccal, ML/P = mesiolingual/palatal, L/P = lingual/palatal, DL/P = distolingual/palatal."
                ), style = MaterialTheme.typography.bodySmall)
                ipcSites.chunked(3).forEachIndexed { r, row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEachIndexed { c, site ->
                            val index = r * 3 + c
                            val code = toothCodes(selectedTooth)[index]
                            FilterChip(
                                selected = selectedSite == index,
                                onClick = { selectedSite = index },
                                label = { Text("$site · $code") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text("${tr(lang, "Código del sitio seleccionado", "Selected site code")}: ${ipcSites[selectedSite]}", fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    listOf("0", "1", "2", "3", "4", "X").forEach { code ->
                        FilterChip(
                            selected = toothCodes(selectedTooth)[selectedSite] == code,
                            onClick = {
                                val list = toothCodes(selectedTooth).toMutableList()
                                list[selectedSite] = code
                                syncSession(siteCodes + (selectedTooth to list))
                            },
                            label = { Text(code) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Text("${tr(lang, "Resultado del diente", "Tooth result")}: ${toothResult(selectedTooth)}", fontWeight = FontWeight.Bold, color = IndexPurple)
            }
        }
        item {
            SectionCard(tr(lang, "Guía rápida de códigos", "Quick code guide")) {
                Text("0 · ${tr(lang, "Sin hallazgos codificados", "No indexed finding")}")
                Text("1 · ${tr(lang, "Sangrado después del sondaje", "Bleeding after probing")}")
                Text("2 · ${tr(lang, "Cálculo y/o factor retentivo de placa", "Calculus and/or plaque-retentive factor")}")
                Text("3 · ${tr(lang, "Bolsa de 4–5 mm", "4–5 mm pocket")}")
                Text("4 · ${tr(lang, "Bolsa de 6 mm o más", "Pocket 6 mm or more")}")
                Text("X · ${tr(lang, "Sitio/diente no evaluable; el sextante se excluye si no reúne dientes suficientes", "Site/tooth not evaluable; sextant is excluded when too few teeth are available")}")
            }
        }
        item {
            val summary = ipcSextants.joinToString(" · ") { "S${it.number}=${sextantResult(it)}" }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = IndexMint.copy(alpha = .22f)),
                border = BorderStroke(1.dp, IndexTurquoise),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(tr(lang, "Resultado automático", "Automatic result"), fontWeight = FontWeight.Black, color = IndexDeep)
                    Text(summary, fontWeight = FontWeight.Bold)
                    Text(tr(lang,
                        "Ejemplo de redacción: “IPC: $summary”. Describe después los hallazgos relevantes y complétalos con la valoración periodontal.",
                        "Writing example: “CPI: $summary”. Then describe relevant findings and complete the periodontal assessment."
                    ))
                }
            }
        }
    }
}

private data class IhosSlot(
    val indexTooth: Int,
    val surfaceEs: String,
    val surfaceEn: String,
    val substitutes: List<Int>
)

private val ihosSlots = listOf(
    IhosSlot(16, "Vestibular", "Buccal", listOf(17, 18)),
    IhosSlot(11, "Labial", "Labial", listOf(21)),
    IhosSlot(26, "Vestibular", "Buccal", listOf(27, 28)),
    IhosSlot(36, "Lingual", "Lingual", listOf(37, 38)),
    IhosSlot(31, "Labial", "Labial", listOf(41)),
    IhosSlot(46, "Lingual", "Lingual", listOf(47, 48))
)

private fun round1(v: Double): Double = round(v * 10.0) / 10.0
private fun ihosInterpretation(value: Double, lang: String): String = when {
    value <= 1.2 -> tr(lang, "Buena", "Good")
    value <= 3.0 -> tr(lang, "Regular", "Fair")
    else -> tr(lang, "Mala", "Poor")
}

@Composable
fun IhosInteractiveV2Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedSlot by remember { mutableStateOf(0) }
    var selections by remember {
        mutableStateOf(ihosSlots.associate { it.indexTooth to it.indexTooth })
    }
    var excludedSlots by remember { mutableStateOf<Set<Int>>(emptySet()) }

    val slot = ihosSlots[selectedSlot]
    val selectedTooth = selections[slot.indexTooth] ?: slot.indexTooth

    fun evaluableSlots(): List<IhosSlot> = ihosSlots.filter { it.indexTooth !in excludedSlots }
    fun debrisAverage(): Double {
        val rows = evaluableSlots()
        if (rows.isEmpty()) return 0.0
        return rows.map { s -> session.ihosDebris[selections[s.indexTooth] ?: s.indexTooth] ?: 0 }.average()
    }
    fun calculusAverage(): Double {
        val rows = evaluableSlots()
        if (rows.isEmpty()) return 0.0
        return rows.map { s -> session.ihosCalculus[selections[s.indexTooth] ?: s.indexTooth] ?: 0 }.average()
    }

    val dAvg = round1(debrisAverage())
    val cAvg = round1(calculusAverage())
    val total = round1(dAvg + cAvg)

    LazyColumn(
        Modifier.fillMaxSize().safeDrawingPadding().background(
            Brush.verticalGradient(listOf(IndexLavender, Color(0xFFF9F5FC), Color.White))
        ).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                "IHOS / OHI-S",
                onBack,
                tr(lang,
                    "Evalúa detritos y cálculo en seis superficies índice. Si un diente índice no puede evaluarse, selecciona el sustituto indicado y la app recalcula automáticamente.",
                    "Assess debris and calculus on six index surfaces. If an index tooth cannot be evaluated, select the indicated substitute and the app recalculates automatically."
                )
            )
        }
        item {
            SectionCard(tr(lang, "1 · Dientes índice y sustitutos", "1 · Index teeth and substitutes")) {
                ihosSlots.chunked(3).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEach { s ->
                            val current = selections[s.indexTooth] ?: s.indexTooth
                            Card(
                                modifier = Modifier.weight(1f).clickable { selectedSlot = ihosSlots.indexOf(s) },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedSlot == ihosSlots.indexOf(s)) IndexMint.copy(alpha = .34f) else IndexPaper
                                ),
                                border = BorderStroke(1.dp, if (selectedSlot == ihosSlots.indexOf(s)) IndexTurquoise else IndexLilac)
                            ) {
                                Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("${s.indexTooth}", fontWeight = FontWeight.Black, color = IndexDeep)
                                    Text(if (lang == "en") s.surfaceEn else s.surfaceEs, style = MaterialTheme.typography.labelSmall)
                                    Text(
                                        if (s.indexTooth in excludedSlots) tr(lang, "No evaluable", "Not evaluable")
                                        else if (current == s.indexTooth) tr(lang, "Índice", "Index")
                                        else "${tr(lang, "Sust.", "Sub.")} $current",
                                        color = if (current == s.indexTooth) IndexPurple else IndexTurquoise,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
        item {
            SectionCard("${tr(lang, "2 · Selección para", "2 · Selection for")} ${slot.indexTooth}") {
                Text(tr(lang,
                    "Usa el diente índice si es evaluable. En posteriores, el sustituto es el siguiente molar plenamente erupcionado distal al área de premolares; en los incisivos centrales, se usa el central contralateral.",
                    "Use the index tooth when evaluable. Posterior substitutes are the next fully erupted molars distal to the premolar area; for central incisors, use the contralateral central incisor."
                ), style = MaterialTheme.typography.bodySmall)
                val candidates = listOf(slot.indexTooth) + slot.substitutes
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    candidates.forEach { tooth ->
                        FilterChip(
                            selected = slot.indexTooth !in excludedSlots && selectedTooth == tooth,
                            onClick = {
                                excludedSlots = excludedSlots - slot.indexTooth
                                selections = selections + (slot.indexTooth to tooth)
                            },
                            label = { Text("OD $tooth") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                OutlinedButton(
                    onClick = { excludedSlots = excludedSlots + slot.indexTooth },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tr(lang, "Sin diente evaluable para este sitio", "No evaluable tooth for this site"))
                }
            }
        }
        item {
            if (slot.indexTooth !in excludedSlots) {
                SectionCard("OD $selectedTooth · ${if (lang == "en") slot.surfaceEn else slot.surfaceEs}") {
                    Text(tr(lang, "Detritos", "Debris"), fontWeight = FontWeight.Black, color = IndexDeep)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        (0..3).forEach { score ->
                            FilterChip(
                                selected = (session.ihosDebris[selectedTooth] ?: 0) == score,
                                onClick = { onSessionChanged(session.copy(ihosDebris = session.ihosDebris + (selectedTooth to score))) },
                                label = { Text(score.toString()) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(tr(lang, "Cálculo", "Calculus"), fontWeight = FontWeight.Black, color = IndexDeep)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        (0..3).forEach { score ->
                            FilterChip(
                                selected = (session.ihosCalculus[selectedTooth] ?: 0) == score,
                                onClick = { onSessionChanged(session.copy(ihosCalculus = session.ihosCalculus + (selectedTooth to score))) },
                                label = { Text(score.toString()) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Guía de puntuación", "Scoring guide")) {
                Text("${tr(lang, "Detritos", "Debris")}: 0 = ${tr(lang, "sin detritos/mancha", "no debris/stain")}; 1 = ${tr(lang, "hasta 1/3 de la superficie", "up to 1/3")}; 2 = ${tr(lang, "más de 1/3 y hasta 2/3", ">1/3 to 2/3")}; 3 = ${tr(lang, "más de 2/3", ">2/3")}.")
                Spacer(Modifier.height(4.dp))
                Text("${tr(lang, "Cálculo", "Calculus")}: 0 = ${tr(lang, "sin cálculo", "none")}; 1 = ${tr(lang, "supragingival hasta 1/3", "supragingival up to 1/3")}; 2 = ${tr(lang, "supragingival >1/3 y hasta 2/3 o depósitos subgingivales aislados", "supragingival >1/3 to 2/3 or isolated subgingival deposits")}; 3 = ${tr(lang, "supragingival >2/3 y/o banda subgingival abundante/continua", "supragingival >2/3 and/or heavy continuous subgingival band")}.")
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = IndexMint.copy(alpha = .22f)),
                border = BorderStroke(1.dp, IndexTurquoise)
            ) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(tr(lang, "Resumen del IHOS", "OHI-S summary"), fontWeight = FontWeight.Black, color = IndexDeep)
                    Text("${tr(lang, "Promedio detritos", "Debris average")}: $dAvg")
                    Text("${tr(lang, "Promedio cálculo", "Calculus average")}: $cAvg")
                    Text("IHOS: $total", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = IndexPurple)
                    Text("${tr(lang, "Interpretación", "Interpretation")}: ${ihosInterpretation(total, lang)}", fontWeight = FontWeight.Bold)
                    Text(tr(lang,
                        "Ejemplo de redacción: “IHOS = $total. Higiene oral ${ihosInterpretation(total, lang).lowercase()}”.",
                        "Writing example: “OHI-S = $total. Oral hygiene ${ihosInterpretation(total, lang).lowercase()}.”"
                    ))
                    Text(tr(lang,
                        "La app usa únicamente los sitios evaluables en el denominador. Si no existe diente índice ni sustituto válido, marca el sitio como no evaluable.",
                        "The app uses only evaluable sites in the denominator. If neither the index tooth nor a valid substitute exists, mark that site not evaluable."
                    ), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
