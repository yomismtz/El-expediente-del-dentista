package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.yomismtz.expedientedeldentista.clinical.IhosIndexV48
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

private data class IhosSlotV48(
    val indexTooth: Int,
    val surfaceEs: String,
    val surfaceEn: String,
    val substitutes: List<Int>
)

private val ihosSlotsV48 = listOf(
    IhosSlotV48(16, "Vestibular", "Buccal", listOf(17, 18)),
    IhosSlotV48(11, "Labial", "Labial", listOf(21)),
    IhosSlotV48(26, "Vestibular", "Buccal", listOf(27, 28)),
    IhosSlotV48(36, "Lingual", "Lingual", listOf(37, 38)),
    IhosSlotV48(31, "Labial", "Labial", listOf(41)),
    IhosSlotV48(46, "Lingual", "Lingual", listOf(47, 48))
)

private data class IhosCriterionV48(
    val score: Int,
    val debrisEs: String,
    val debrisEn: String,
    val calculusEs: String,
    val calculusEn: String
)

private val ihosCriteriaV48 = listOf(
    IhosCriterionV48(
        0,
        "Sin detritos blandos ni tinción extrínseca en la superficie índice.",
        "No soft debris or extrinsic stain on the index surface.",
        "Sin cálculo supragingival ni subgingival detectable.",
        "No detectable supragingival or subgingival calculus."
    ),
    IhosCriterionV48(
        1,
        "Detritos blandos que cubren como máximo 1/3 de la superficie; también puede registrarse tinción extrínseca sin otros detritos.",
        "Soft debris covers no more than 1/3 of the surface; extrinsic stain without other debris may also receive this score.",
        "Cálculo supragingival que cubre como máximo 1/3 de la superficie expuesta.",
        "Supragingival calculus covers no more than 1/3 of the exposed surface."
    ),
    IhosCriterionV48(
        2,
        "Detritos blandos que cubren más de 1/3 y hasta 2/3 de la superficie.",
        "Soft debris covers more than 1/3 and up to 2/3 of the surface.",
        "Cálculo supragingival que cubre más de 1/3 y hasta 2/3, o flecos/depósitos aislados de cálculo subgingival alrededor de la región cervical.",
        "Supragingival calculus covers more than 1/3 and up to 2/3, or isolated flecks of subgingival calculus are present around the cervical region."
    ),
    IhosCriterionV48(
        3,
        "Detritos blandos que cubren más de 2/3 de la superficie.",
        "Soft debris covers more than 2/3 of the surface.",
        "Cálculo supragingival que cubre más de 2/3, o banda continua/gruesa de cálculo subgingival alrededor de la región cervical.",
        "Supragingival calculus covers more than 2/3, or a continuous heavy band of subgingival calculus is present around the cervical region."
    )
)

@Composable
fun IhosPersistentV23Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedSlotIndex by remember { mutableStateOf(0) }
    var teachingMode by remember { mutableStateOf("debris") }

    val selectedSlot = ihosSlotsV48[selectedSlotIndex]
    val excluded = session.ihosExcludedSlots

    fun selectedToothFor(slot: IhosSlotV48): Int =
        session.ihosSelections[slot.indexTooth] ?: slot.indexTooth

    fun isMarkedMissing(tooth: Int): Boolean = when (session.teeth[tooth]?.status ?: ToothStatus.HEALTHY) {
        ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER -> true
        else -> false
    }

    fun isEvaluable(slot: IhosSlotV48): Boolean =
        slot.indexTooth !in excluded && !isMarkedMissing(selectedToothFor(slot))

    val selectedTooth = selectedToothFor(selectedSlot)
    val selectedMissing = isMarkedMissing(selectedTooth)
    val evaluableSlots = ihosSlotsV48.filter(::isEvaluable)
    val examinedTeeth = evaluableSlots.map(::selectedToothFor)
    val result = IhosIndexV48.calculate(
        examinedTeeth = examinedTeeth,
        debrisScores = session.ihosDebris,
        calculusScores = session.ihosCalculus
    )
    val completedSlots = evaluableSlots.count { slot ->
        val tooth = selectedToothFor(slot)
        tooth in session.ihosDebris && tooth in session.ihosCalculus
    }
    val minimumSitesMet = IhosIndexV48.hasMinimumSites(evaluableSlots.size)
    val complete = minimumSitesMet && completedSlots == evaluableSlots.size
    val interpretation = when {
        !minimumSitesMet -> tr(
            lang,
            "No calculable · se requieren al menos 2 superficies evaluables",
            "Not calculable · at least 2 evaluable surfaces are required"
        )
        complete -> if (lang == "en") IhosIndexV48.interpretationEn(result.total)
        else IhosIndexV48.interpretationEs(result.total)
        else -> tr(lang, "Pendiente · completa todos los sitios evaluables", "Pending · complete every evaluable site")
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
        if (selectedSlot.indexTooth in excluded || selectedMissing) return
        if (debris) {
            onSessionChanged(session.copy(ihosDebris = session.ihosDebris + (selectedTooth to value.coerceIn(0, 3))))
        } else {
            onSessionChanged(session.copy(ihosCalculus = session.ihosCalculus + (selectedTooth to value.coerceIn(0, 3))))
        }
    }

    ResponsiveScreenV17(
        "IHOS / OHI-S",
        tr(
            lang,
            "Índice de Higiene Oral Simplificado de Greene y Vermillion: ID-S + IC-S en seis superficies índice. Los sustitutos y exclusiones quedan guardados en la sesión educativa.",
            "Greene and Vermillion Simplified Oral Hygiene Index: DI-S + CI-S on six index surfaces. Substitute selections and exclusions are stored in the educational session."
        ),
        onBack
    ) { profile ->
        PracticeSaveControlsV48(lang, "ihos_v48")

        ResponsiveSectionV17(tr(lang, "1 · Superficies índice", "1 · Index surfaces")) {
            Text(
                tr(
                    lang,
                    "16V · 11L · 26V · 36Li · 31L · 46Li. En el contexto IHOS, L = labial y Li = lingual.",
                    "16B · 11 labial · 26B · 36L · 31 labial · 46L."
                )
            )
            val slotColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
            AdaptiveGridV17(ihosSlotsV48.size, slotColumns) { index ->
                val slot = ihosSlotsV48[index]
                val current = selectedToothFor(slot)
                val missing = isMarkedMissing(current)
                val debrisDone = current in session.ihosDebris
                val calculusDone = current in session.ihosCalculus
                Card(
                    onClick = { selectedSlotIndex = index },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedSlotIndex == index) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (selectedSlotIndex == index) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.outline.copy(alpha = .4f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            when {
                                slot.indexTooth in excluded ->
                                    "OD ${slot.indexTooth} · ${tr(lang, "No evaluable", "Not evaluable")}"
                                missing ->
                                    "OD $current · ${tr(lang, "ausente", "missing")}"
                                current != slot.indexTooth ->
                                    "OD $current · ${tr(lang, "sustituye", "substitutes")} ${slot.indexTooth}"
                                else ->
                                    "OD $current · ${if (lang == "en") slot.surfaceEn else slot.surfaceEs}"
                            },
                            fontWeight = FontWeight.Bold
                        )
                        if (slot.indexTooth !in excluded && !missing) {
                            Text(
                                tr(
                                    lang,
                                    "Detritos ${if (debrisDone) "✓" else "○"} · Cálculo ${if (calculusDone) "✓" else "○"}",
                                    "Debris ${if (debrisDone) "✓" else "○"} · Calculus ${if (calculusDone) "✓" else "○"}"
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        ResponsiveSectionV17(
            "${tr(lang, "2 · Sitio seleccionado", "2 · Selected site")}: ${selectedSlot.indexTooth}"
        ) {
            val candidates = listOf(selectedSlot.indexTooth) + selectedSlot.substitutes
            Text(
                tr(
                    lang,
                    "Usa el diente índice cuando la superficie puede examinarse. Si no está disponible/evaluable, selecciona uno de los sustitutos permitidos; si ninguno puede valorarse, excluye el sitio.",
                    "Use the index tooth when its surface can be examined. If it is unavailable/not evaluable, select an allowed substitute; if none can be assessed, exclude the site."
                )
            )
            AdaptiveGridV17(
                candidates.size,
                if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1
                else candidates.size.coerceAtMost(3)
            ) { index ->
                val tooth = candidates[index]
                FilterChip(
                    selected = selectedTooth == tooth && selectedSlot.indexTooth !in excluded,
                    onClick = { selectTooth(tooth) },
                    label = {
                        Text(
                            "OD $tooth" +
                                if (isMarkedMissing(tooth)) " · ${tr(lang, "ausente", "missing")}" else ""
                        )
                    },
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

        ResponsiveSectionV17(tr(lang, "3 · Aprende los criterios 0–3", "3 · Learn the 0–3 criteria")) {
            AdaptiveGridV17(
                2,
                if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
            ) { index ->
                val debris = index == 0
                FilterChip(
                    selected = teachingMode == if (debris) "debris" else "calculus",
                    onClick = { teachingMode = if (debris) "debris" else "calculus" },
                    label = { Text(if (debris) tr(lang, "ID-S · Detritos", "DI-S · Debris") else tr(lang, "IC-S · Cálculo", "CI-S · Calculus")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ihosCriteriaV48.forEach { criterion ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("${tr(lang, "Código", "Score")} ${criterion.score}", fontWeight = FontWeight.Black)
                        Text(
                            if (teachingMode == "debris") {
                                if (lang == "en") criterion.debrisEn else criterion.debrisEs
                            } else {
                                if (lang == "en") criterion.calculusEn else criterion.calculusEs
                            }
                        )
                    }
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "4 · Registra el sitio", "4 · Record the site")) {
            when {
                selectedSlot.indexTooth in excluded -> {
                    NoticeCard(
                        tr(
                            lang,
                            "Este sitio está excluido del denominador porque no existe una superficie evaluable seleccionada.",
                            "This site is excluded from the denominator because no evaluable surface is selected."
                        )
                    )
                }

                selectedMissing -> {
                    NoticeCard(
                        tr(
                            lang,
                            "El OD $selectedTooth está marcado como ausente. Selecciona un sustituto evaluable o excluye este sitio.",
                            "Tooth $selectedTooth is marked missing. Select an evaluable substitute or exclude this site."
                        )
                    )
                }

                else -> {
                    Text(
                        "${tr(lang, "Detritos", "Debris")} · OD $selectedTooth · ${if (lang == "en") selectedSlot.surfaceEn else selectedSlot.surfaceEs}",
                        fontWeight = FontWeight.Bold
                    )
                    AdaptiveGridV17(
                        4,
                        if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4
                    ) { score ->
                        FilterChip(
                            selected = session.ihosDebris[selectedTooth] == score,
                            onClick = { setScore(true, score) },
                            label = { Text(score.toString()) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(
                        "${tr(lang, "Cálculo", "Calculus")} · OD $selectedTooth · ${if (lang == "en") selectedSlot.surfaceEn else selectedSlot.surfaceEs}",
                        fontWeight = FontWeight.Bold
                    )
                    AdaptiveGridV17(
                        4,
                        if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4
                    ) { score ->
                        FilterChip(
                            selected = session.ihosCalculus[selectedTooth] == score,
                            onClick = { setScore(false, score) },
                            label = { Text(score.toString()) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(
                        tr(
                            lang,
                            "Un valor 0 sólo cuenta como examinado cuando tú lo seleccionas. Un sitio todavía sin puntuar no reduce artificialmente el promedio.",
                            "A zero counts as examined only after you select it. An unscored site does not artificially lower the average."
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "5 · Resultado automático", "5 · Automatic result")) {
            Text(
                tr(
                    lang,
                    "Sitios completados: $completedSlots / ${evaluableSlots.size}",
                    "Completed sites: $completedSlots / ${evaluableSlots.size}"
                ),
                fontWeight = FontWeight.Bold
            )
            Text(
                "ID-S ${result.debrisAverage} + IC-S ${result.calculusAverage} = IHOS ${result.total}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )
            Text(
                "${tr(lang, "Interpretación", "Interpretation")}: $interpretation",
                color = if (complete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )

            if (complete) {
                Button(
                    onClick = {
                        TeachingStateV40.moduleSummaries["ihos"] =
                            "IHOS ${result.total} · ID-S ${result.debrisAverage} · IC-S ${result.calculusAverage} · sitios ${result.examinedSites}"
                        TeachingStateV40.savedPracticeSections["ihos_v48"] = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tr(lang, "Guardar resultado IHOS", "Save OHI-S result"))
                }
            } else {
                NoticeCard(
                    if (!minimumSitesMet) {
                        tr(
                            lang,
                            "El IHOS individual no debe calcularse con menos de 2 de las 6 superficies posibles. Selecciona sustitutos evaluables cuando proceda.",
                            "An individual OHI-S should not be calculated with fewer than 2 of the 6 possible surfaces. Select evaluable substitutes when appropriate."
                        )
                    } else {
                        tr(
                            lang,
                            "El resultado mostrado es provisional. Completa detritos y cálculo en cada sitio evaluable antes de guardar la interpretación final.",
                            "The displayed result is provisional. Complete debris and calculus at every evaluable site before saving the final interpretation."
                        )
                    }
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "6 · Cómo se escribe", "6 · How to chart it")) {
            Text(
                tr(
                    lang,
                    "Ejemplo: “IHOS = 1.8 (ID-S 1.2 + IC-S 0.6), higiene oral regular”. Registra también sustituciones o sitios excluidos cuando sean relevantes para interpretar el índice.",
                    "Example: “OHI-S = 1.8 (DI-S 1.2 + CI-S 0.6), fair oral hygiene”. Also document substitutions or excluded sites when relevant to interpreting the index."
                )
            )
            Text(
                tr(
                    lang,
                    "⚠️ Error común: usar la misma descripción para detritos y cálculo. En IC-S, los códigos 2 y 3 también consideran cálculo subgingival.",
                    "⚠️ Common mistake: using the same description for debris and calculus. In CI-S, scores 2 and 3 also account for subgingival calculus."
                ),
                color = MaterialTheme.colorScheme.error
            )
        }

        NoticeCard(
            tr(
                lang,
                "Criterio clásico: IHOS = ID-S + IC-S. ID-S e IC-S se calculan dividiendo la suma de sus puntuaciones entre el número de superficies realmente examinadas; se requieren al menos 2 superficies para un puntaje individual. Interpretación educativa usada: 0–1.2 buena, 1.3–3.0 regular, 3.1–6.0 mala.",
                "Classic criterion: OHI-S = DI-S + CI-S. DI-S and CI-S are calculated by dividing their score sums by the number of surfaces actually examined; at least 2 surfaces are required for an individual score. Educational interpretation used: 0–1.2 good, 1.3–3.0 fair, 3.1–6.0 poor."
            )
        )

        PracticeSaveControlsV48(lang, "ihos_v48")
    }
}
