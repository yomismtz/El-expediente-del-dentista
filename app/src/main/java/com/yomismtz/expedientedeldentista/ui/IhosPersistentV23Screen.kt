package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.yomismtz.expedientedeldentista.clinical.ToothStatus
import kotlin.math.round

private data class IhosSlotV23(
    val indexTooth: Int,
    val surfaceEs: String,
    val surfaceEn: String,
    val substitutes: List<Int>
)

private val ihosSlotsV23 = listOf(
    IhosSlotV23(16, "Vestibular", "Buccal", listOf(17, 18)),
    IhosSlotV23(11, "Labial", "Labial", listOf(21)),
    IhosSlotV23(26, "Vestibular", "Buccal", listOf(27, 28)),
    IhosSlotV23(36, "Lingual", "Lingual", listOf(37, 38)),
    IhosSlotV23(31, "Labial", "Labial", listOf(41)),
    IhosSlotV23(46, "Lingual", "Lingual", listOf(47, 48))
)

private fun roundIhosV23(value: Double): Double = round(value * 10.0) / 10.0

@Composable
fun IhosPersistentV23Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedSlotIndex by remember { mutableStateOf(0) }
    val selectedSlot = ihosSlotsV23[selectedSlotIndex]
    val selectedTooth = session.ihosSelections[selectedSlot.indexTooth] ?: selectedSlot.indexTooth
    val excluded = session.ihosExcludedSlots

    fun selectedToothFor(slot: IhosSlotV23): Int = session.ihosSelections[slot.indexTooth] ?: slot.indexTooth
    fun isMarkedMissing(tooth: Int): Boolean = when (session.teeth[tooth]?.status ?: ToothStatus.HEALTHY) {
        ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER -> true
        else -> false
    }
    fun isEvaluable(slot: IhosSlotV23): Boolean =
        slot.indexTooth !in excluded && !isMarkedMissing(selectedToothFor(slot))

    val evaluableSlots = ihosSlotsV23.filter(::isEvaluable)
    val debrisAverage = if (evaluableSlots.isEmpty()) 0.0 else evaluableSlots.map { slot ->
        session.ihosDebris[selectedToothFor(slot)] ?: 0
    }.average()
    val calculusAverage = if (evaluableSlots.isEmpty()) 0.0 else evaluableSlots.map { slot ->
        session.ihosCalculus[selectedToothFor(slot)] ?: 0
    }.average()
    val dAvg = roundIhosV23(debrisAverage)
    val cAvg = roundIhosV23(calculusAverage)
    val total = roundIhosV23(debrisAverage + calculusAverage)
    val interpretation = when {
        total <= 1.2 -> tr(lang, "Buena", "Good")
        total <= 3.0 -> tr(lang, "Regular", "Fair")
        else -> tr(lang, "Mala", "Poor")
    }
    val selectedMissing = isMarkedMissing(selectedTooth)

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
        if (selectedSlot.indexTooth in excluded || selectedMissing) return
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
            "La selección de sustitutos y exclusiones queda guardada y usa el mismo criterio que el resultado de la nota de ingreso.",
            "Substitute selections and exclusions are stored and use the same criteria as the intake-note result."
        ),
        onBack
    ) { profile ->
        val slotColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
        ResponsiveSectionV17(tr(lang, "1 · Dientes índice", "1 · Index teeth")) {
            AdaptiveGridV17(ihosSlotsV23.size, slotColumns) { index ->
                val slot = ihosSlotsV23[index]
                val current = selectedToothFor(slot)
                val missing = isMarkedMissing(current)
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
                            missing -> "OD $current · ${tr(lang, "marcado ausente", "marked missing")}"
                            current != slot.indexTooth -> "OD $current · ${tr(lang, "sustituye", "substitutes")} ${slot.indexTooth}"
                            else -> "OD $current · ${if (lang == "en") slot.surfaceEn else slot.surfaceEs}"
                        },
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        ResponsiveSectionV17("${tr(lang, "2 · Sitio seleccionado", "2 · Selected site")}: ${selectedSlot.indexTooth}") {
            val candidates = listOf(selectedSlot.indexTooth) + selectedSlot.substitutes
            Text(
                tr(
                    lang,
                    "Selecciona el diente índice o un sustituto permitido. Si el seleccionado está marcado como ausente en el odontograma, no entra al denominador.",
                    "Select the index tooth or an allowed substitute. If the selected tooth is marked missing in the odontogram, it is excluded from the denominator."
                )
            )
            val columns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else candidates.size.coerceAtMost(3)
            AdaptiveGridV17(candidates.size, columns) { index ->
                val tooth = candidates[index]
                FilterChip(
                    selected = selectedTooth == tooth && selectedSlot.indexTooth !in excluded,
                    onClick = { selectTooth(tooth) },
                    label = { Text("OD $tooth${if (isMarkedMissing(tooth)) " · ${tr(lang, "ausente", "missing")}" else ""}") },
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
            when {
                selectedSlot.indexTooth in excluded -> {
                    Text(tr(lang, "Este sitio está excluido del denominador del IHOS.", "This site is excluded from the OHI-S denominator."))
                }
                selectedMissing -> {
                    Text(
                        tr(
                            lang,
                            "El OD $selectedTooth está marcado como ausente. Selecciona un sustituto evaluable o excluye este sitio.",
                            "Tooth $selectedTooth is marked missing. Select an evaluable substitute or exclude this site."
                        )
                    )
                }
                else -> {
                    Text("${tr(lang, "Detritos", "Debris")} · OD $selectedTooth", fontWeight = FontWeight.Bold)
                    AdaptiveGridV17(4, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4) { index ->
                        FilterChip(
                            selected = (session.ihosDebris[selectedTooth] ?: 0) == index,
                            onClick = { setScore(true, index) },
                            label = { Text(index.toString()) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text("${tr(lang, "Cálculo", "Calculus")} · OD $selectedTooth", fontWeight = FontWeight.Bold)
                    AdaptiveGridV17(4, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4) { index ->
                        FilterChip(
                            selected = (session.ihosCalculus[selectedTooth] ?: 0) == index,
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
