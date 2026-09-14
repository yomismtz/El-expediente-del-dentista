package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import kotlin.math.round

private data class PersistentIpcSextant(val number: Int, val label: String, val teeth: List<Int>)

private val persistentIpcSextants = listOf(
    PersistentIpcSextant(1, "18–14", listOf(18, 17, 16, 15, 14)),
    PersistentIpcSextant(2, "13–23", listOf(13, 12, 11, 21, 22, 23)),
    PersistentIpcSextant(3, "24–28", listOf(24, 25, 26, 27, 28)),
    PersistentIpcSextant(4, "34–38", listOf(34, 35, 36, 37, 38)),
    PersistentIpcSextant(5, "33–43", listOf(33, 32, 31, 41, 42, 43)),
    PersistentIpcSextant(6, "44–48", listOf(44, 45, 46, 47, 48))
)

private val persistentIpcSites = listOf("MV", "V", "DV", "ML/P", "L/P", "DL/P")

private fun normalizeIpcSites(values: List<String>?): List<String> =
    List(6) { index -> values?.getOrNull(index) ?: "0" }

private fun highestPersistentIpcCode(values: List<String>): String {
    val numeric = values.mapNotNull { it.toIntOrNull() }
    return numeric.maxOrNull()?.toString() ?: if (values.any { it == "X" }) "X" else "0"
}

@Composable
fun IpcPersistentV22Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var sextantIndex by remember { mutableStateOf(0) }
    var selectedTooth by remember { mutableStateOf(persistentIpcSextants.first().teeth.first()) }
    var selectedSite by remember { mutableStateOf(0) }

    fun toothCodes(tooth: Int): List<String> = normalizeIpcSites(session.ipcSiteCodes[tooth])
    fun toothResult(tooth: Int): String = highestPersistentIpcCode(toothCodes(tooth))
    fun sextantResult(sextant: PersistentIpcSextant): String {
        val results = sextant.teeth.map { toothResult(it) }
        return if (results.count { it != "X" } < 2) "X" else highestPersistentIpcCode(results)
    }

    fun updateSite(code: String) {
        val list = toothCodes(selectedTooth).toMutableList()
        list[selectedSite] = code
        val updatedSites = session.ipcSiteCodes + (selectedTooth to list)
        val summary = persistentIpcSextants.map { sextant ->
            val results = sextant.teeth.map { tooth ->
                highestPersistentIpcCode(normalizeIpcSites(updatedSites[tooth]))
            }
            if (results.count { it != "X" } < 2) "X" else highestPersistentIpcCode(results)
        }
        onSessionChanged(session.copy(ipcSiteCodes = updatedSites, ipcCodes = summary))
    }

    ResponsiveScreenV17(
        tr(lang, "IPC por sextantes", "CPI by sextants"),
        tr(
            lang,
            "Los seis sitios por diente quedan guardados durante toda la sesión educativa.",
            "All six sites per tooth remain stored for the full educational session."
        ),
        onBack
    ) { profile ->
        val sextantColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
        ResponsiveSectionV17(tr(lang, "1 · Selecciona sextante", "1 · Select sextant")) {
            AdaptiveGridV17(persistentIpcSextants.size, sextantColumns) { index ->
                val sextant = persistentIpcSextants[index]
                Card(
                    onClick = {
                        sextantIndex = index
                        selectedTooth = sextant.teeth.first()
                        selectedSite = 0
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (sextantIndex == index) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (sextantIndex == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha = .4f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "S${sextant.number} · ${sextant.label} · ${sextantResult(sextant)}",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        val selectedSextant = persistentIpcSextants[sextantIndex]
        ResponsiveSectionV17("${tr(lang, "2 · Dientes S", "2 · Teeth S")}${selectedSextant.number}") {
            val columns = when {
                profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT -> 2
                profile.width == ScreenWidthV17.MEDIUM -> 3
                else -> selectedSextant.teeth.size
            }
            AdaptiveGridV17(selectedSextant.teeth.size, columns) { index ->
                val tooth = selectedSextant.teeth[index]
                FilterChip(
                    selected = selectedTooth == tooth,
                    onClick = {
                        selectedTooth = tooth
                        selectedSite = 0
                    },
                    label = { Text("OD $tooth · ${toothResult(tooth)}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17("${tr(lang, "3 · Seis sitios OD", "3 · Six sites tooth")} $selectedTooth") {
            val siteColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
            AdaptiveGridV17(persistentIpcSites.size, siteColumns) { index ->
                FilterChip(
                    selected = selectedSite == index,
                    onClick = { selectedSite = index },
                    label = { Text("${persistentIpcSites[index]} · ${toothCodes(selectedTooth)[index]}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text("${tr(lang, "Código para", "Code for")} ${persistentIpcSites[selectedSite]}", fontWeight = FontWeight.Black)
            val codeColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 3 else 6
            val codes = listOf("0", "1", "2", "3", "4", "X")
            AdaptiveGridV17(codes.size, codeColumns) { index ->
                val code = codes[index]
                FilterChip(
                    selected = toothCodes(selectedTooth)[selectedSite] == code,
                    onClick = { updateSite(code) },
                    label = { Text(code) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "Resultado automático", "Automatic result")) {
            Text(
                persistentIpcSextants.joinToString(" · ") { "S${it.number}=${sextantResult(it)}" },
                fontWeight = FontWeight.Black
            )
            Text(
                tr(
                    lang,
                    "0 sin hallazgo · 1 sangrado · 2 cálculo/factor retentivo · 3 bolsa 4–5 mm · 4 bolsa ≥6 mm · X no evaluable.",
                    "0 no finding · 1 bleeding · 2 calculus/retentive factor · 3 pocket 4–5 mm · 4 pocket ≥6 mm · X not evaluable."
                )
            )
        }

        NoticeCard(
            tr(
                lang,
                "Qué escribir: IPC: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Añade los hallazgos periodontales relevantes.",
                "What to write: CPI: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Add relevant periodontal findings."
            )
        )
    }
}

private data class PersistentIhosSlot(
    val indexTooth: Int,
    val surfaceEs: String,
    val surfaceEn: String,
    val substitutes: List<Int>
)

private val persistentIhosSlots = listOf(
    PersistentIhosSlot(16, "Vestibular", "Buccal", listOf(17, 18)),
    PersistentIhosSlot(11, "Labial", "Labial", listOf(21)),
    PersistentIhosSlot(26, "Vestibular", "Buccal", listOf(27, 28)),
    PersistentIhosSlot(36, "Lingual", "Lingual", listOf(37, 38)),
    PersistentIhosSlot(31, "Labial", "Labial", listOf(41)),
    PersistentIhosSlot(46, "Lingual", "Lingual", listOf(47, 48))
)

private fun roundIhos(value: Double): Double = round(value * 10.0) / 10.0

@Composable
fun IhosPersistentV22Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedSlotIndex by remember { mutableStateOf(0) }
    val selectedSlot = persistentIhosSlots[selectedSlotIndex]
    val selectedTooth = session.ihosSelections[selectedSlot.indexTooth] ?: selectedSlot.indexTooth
    val excluded = session.ihosExcludedSlots

    val evaluableSlots = persistentIhosSlots.filterNot { it.indexTooth in excluded }
    val debrisAverage = if (evaluableSlots.isEmpty()) 0.0 else evaluableSlots.map { slot ->
        val tooth = session.ihosSelections[slot.indexTooth] ?: slot.indexTooth
        session.ihosDebris[tooth] ?: 0
    }.average()
    val calculusAverage = if (evaluableSlots.isEmpty()) 0.0 else evaluableSlots.map { slot ->
        val tooth = session.ihosSelections[slot.indexTooth] ?: slot.indexTooth
        session.ihosCalculus[tooth] ?: 0
    }.average()
    val dAvg = roundIhos(debrisAverage)
    val cAvg = roundIhos(calculusAverage)
    val total = roundIhos(dAvg + cAvg)
    val interpretation = when {
        total <= 1.2 -> tr(lang, "Buena", "Good")
        total <= 3.0 -> tr(lang, "Regular", "Fair")
        else -> tr(lang, "Mala", "Poor")
    }

    fun selectTooth(tooth: Int) {
        onSessionChanged(
            session.copy(
                ihosSelections = session.ihosSelections + (selectedSlot.indexTooth to tooth),
                ihosExcludedSlots = session.ihosExcludedSlots - selectedSlot.indexTooth
            )
        )
    }

    fun toggleExcluded() {
        val updated = if (selectedSlot.indexTooth in excluded) {
            excluded - selectedSlot.indexTooth
        } else {
            excluded + selectedSlot.indexTooth
        }
        onSessionChanged(session.copy(ihosExcludedSlots = updated))
    }

    fun setScore(debris: Boolean, value: Int) {
        if (selectedSlot.indexTooth in excluded) return
        if (debris) {
            onSessionChanged(session.copy(ihosDebris = session.ihosDebris + (selectedTooth to value)))
        } else {
            onSessionChanged(session.copy(ihosCalculus = session.ihosCalculus + (selectedTooth to value)))
        }
    }

    ResponsiveScreenV17(
        "IHOS / OHI-S",
        tr(
            lang,
            "La selección de dientes sustitutos y sitios excluidos queda guardada durante la sesión.",
            "Substitute-tooth selections and excluded sites remain stored during the session."
        ),
        onBack
    ) { profile ->
        val slotColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
        ResponsiveSectionV17(tr(lang, "1 · Dientes índice", "1 · Index teeth")) {
            AdaptiveGridV17(persistentIhosSlots.size, slotColumns) { index ->
                val slot = persistentIhosSlots[index]
                val current = session.ihosSelections[slot.indexTooth] ?: slot.indexTooth
                Card(
                    onClick = { selectedSlotIndex = index },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedSlotIndex == index) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (selectedSlotIndex == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha = .4f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        when {
                            slot.indexTooth in excluded -> "OD ${slot.indexTooth} · ${tr(lang, "No evaluable", "Not evaluable")}"
                            current != slot.indexTooth -> "OD $current · ${tr(lang, "sustituye", "substitutes")} ${slot.indexTooth}"
                            else -> "OD $current · ${if (lang == "en") slot.surfaceEn else slot.surfaceEs}"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        ResponsiveSectionV17("${tr(lang, "2 · Sitio seleccionado", "2 · Selected site")}: ${selectedSlot.indexTooth}") {
            val candidates = listOf(selectedSlot.indexTooth) + selectedSlot.substitutes
            Text(tr(lang, "Selecciona el diente índice o uno de sus sustitutos permitidos.", "Select the index tooth or an allowed substitute."))
            val columns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else candidates.size.coerceAtMost(3)
            AdaptiveGridV17(candidates.size, columns) { index ->
                val tooth = candidates[index]
                FilterChip(
                    selected = selectedTooth == tooth && selectedSlot.indexTooth !in excluded,
                    onClick = { selectTooth(tooth) },
                    label = { Text("OD $tooth") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            FilterChip(
                selected = selectedSlot.indexTooth in excluded,
                onClick = { toggleExcluded() },
                label = { Text(tr(lang, "Sin diente evaluable para este sitio", "No evaluable tooth for this site")) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        ResponsiveSectionV17(tr(lang, "3 · Detritos y cálculo", "3 · Debris and calculus")) {
            if (selectedSlot.indexTooth in excluded) {
                Text(tr(lang, "Este sitio está excluido del denominador del IHOS.", "This site is excluded from the OHI-S denominator."))
            } else {
                Text("${tr(lang, "Detritos", "Debris")} · OD $selectedTooth", fontWeight = FontWeight.Bold)
                AdaptiveGridV17(4, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4) { index ->
                    FilterChip(
                        selected = session.ihosDebris[selectedTooth] == index,
                        onClick = { setScore(true, index) },
                        label = { Text(index.toString()) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text("${tr(lang, "Cálculo", "Calculus")} · OD $selectedTooth", fontWeight = FontWeight.Bold)
                AdaptiveGridV17(4, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4) { index ->
                    FilterChip(
                        selected = session.ihosCalculus[selectedTooth] == index,
                        onClick = { setScore(false, index) },
                        label = { Text(index.toString()) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    tr(
                        lang,
                        "0 = ausente · 1 = hasta 1/3 · 2 = >1/3 hasta 2/3 · 3 = >2/3.",
                        "0 = absent · 1 = up to 1/3 · 2 = >1/3 to 2/3 · 3 = >2/3."
                    )
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "Resultado automático", "Automatic result")) {
            Text("ID-S $dAvg + IC-S $cAvg = IHOS $total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text("${tr(lang, "Interpretación", "Interpretation")}: $interpretation", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }

        NoticeCard(
            tr(
                lang,
                "Qué escribir: IHOS = ___ (ID-S ___ + IC-S ___). Añade la interpretación obtenida.",
                "What to write: OHI-S = ___ (DI-S ___ + CI-S ___). Add the resulting interpretation."
            )
        )
    }
}
