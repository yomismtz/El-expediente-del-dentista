package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
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
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.CpiModifiedV48
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

private fun cpiToothMissingV48(session: EducationalSession, tooth: Int): Boolean =
    session.teeth[tooth]?.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER)

@Composable
fun CpiModifiedV48Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var sextantIndex by remember { mutableStateOf(0) }
    var selectedTooth by remember { mutableStateOf(CpiModifiedV48.sextants.first().first()) }
    var age15Plus by remember { mutableStateOf(true) }

    val presentTeeth = ClinicalContent.permanentTeeth
        .filterNot { cpiToothMissingV48(session, it) }
        .toSet()

    val selectedSextant = CpiModifiedV48.sextants[sextantIndex]
    if (selectedTooth !in selectedSextant) selectedTooth = selectedSextant.first()

    val selectedMissing = cpiToothMissingV48(session, selectedTooth)
    val bleedingCode = session.cpiBleedingByTooth[selectedTooth]
    val pocketCode = session.cpiPocketByTooth[selectedTooth]

    val summary = CpiModifiedV48.summarize(
        presentTeeth = presentTeeth,
        bleeding = session.cpiBleedingByTooth,
        pockets = session.cpiPocketByTooth,
        recordPockets = age15Plus
    )

    fun setBleeding(code: Int) {
        if (selectedMissing) return
        val bleeding = session.cpiBleedingByTooth + (selectedTooth to CpiModifiedV48.sanitizeBleeding(code))
        val pockets = if (code == CpiModifiedV48.EXCLUDED && age15Plus) {
            session.cpiPocketByTooth + (selectedTooth to CpiModifiedV48.EXCLUDED)
        } else session.cpiPocketByTooth
        onSessionChanged(session.copy(cpiBleedingByTooth = bleeding, cpiPocketByTooth = pockets))
    }

    fun setPocket(code: Int) {
        if (selectedMissing || !age15Plus) return
        val pockets = session.cpiPocketByTooth + (selectedTooth to CpiModifiedV48.sanitizePocket(code))
        val bleeding = if (code == CpiModifiedV48.EXCLUDED) {
            session.cpiBleedingByTooth + (selectedTooth to CpiModifiedV48.EXCLUDED)
        } else session.cpiBleedingByTooth
        onSessionChanged(session.copy(cpiBleedingByTooth = bleeding, cpiPocketByTooth = pockets))
    }

    fun toothStatusLabel(tooth: Int): String {
        if (cpiToothMissingV48(session, tooth)) return "X"
        val b = CpiModifiedV48.bleedingLabel(session.cpiBleedingByTooth[tooth])
        val p = if (age15Plus) CpiModifiedV48.pocketLabel(session.cpiPocketByTooth[tooth]) else "—"
        return if (age15Plus) "B$b · P$p" else "B$b"
    }

    ResponsiveScreenV17(
        tr(lang, "IPC/CPI modificado · OMS", "Modified CPI · WHO"),
        tr(
            lang,
            "Método principal alineado con Oral Health Surveys: Basic Methods, 5.ª ed.: sangrado y bolsas se registran por separado en todos los dientes presentes.",
            "Primary method aligned with Oral Health Surveys: Basic Methods, 5th ed.: bleeding and pockets are recorded separately for all present teeth."
        ),
        onBack
    ) { profile ->
        PracticeSaveControlsV48(lang, "cpi_modified_v48")

        ResponsiveSectionV17(tr(lang, "1 · Grupo de edad del ejercicio", "1 · Exercise age group")) {
            AdaptiveGridV17(2, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val adultPocketMode = index == 1
                FilterChip(
                    selected = age15Plus == adultPocketMode,
                    onClick = { age15Plus = adultPocketMode },
                    label = {
                        Text(
                            if (adultPocketMode) tr(lang, "15 años o más", "Age 15 or older")
                            else tr(lang, "Menor de 15 años", "Younger than 15")
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                if (age15Plus) {
                    tr(lang, "Se registran sangrado y profundidad de bolsa.", "Bleeding and pocket depth are recorded.")
                } else {
                    tr(
                        lang,
                        "Se registra sangrado. La OMS indica no registrar bolsas periodontales en menores de 15 años.",
                        "Bleeding is recorded. WHO states that periodontal pockets are not recorded in people younger than 15 years."
                    )
                }
            )
        }

        ResponsiveSectionV17(
            tr(lang, "2 · Sextante para navegar", "2 · Sextant for navigation"),
            tr(
                lang,
                "En CPI modificado los sextantes aquí sólo organizan la pantalla. El registro principal es por diente, no un único código 0–4 por sextante.",
                "In modified CPI, sextants here only organize navigation. The primary record is per tooth, not one combined 0–4 code per sextant."
            )
        ) {
            AdaptiveGridV17(
                CpiModifiedV48.sextants.size,
                if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
            ) { index ->
                val teeth = CpiModifiedV48.sextants[index]
                val recorded = teeth.count { tooth ->
                    cpiToothMissingV48(session, tooth) ||
                        (tooth in session.cpiBleedingByTooth && (!age15Plus || tooth in session.cpiPocketByTooth))
                }
                Card(
                    onClick = {
                        sextantIndex = index
                        selectedTooth = teeth.first()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (sextantIndex == index) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (sextantIndex == index) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.outline.copy(alpha = .4f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(10.dp)) {
                        Text("S${index + 1} · ${teeth.first()}–${teeth.last()}", fontWeight = FontWeight.Black)
                        Text(
                            tr(lang, "Completados: $recorded/${teeth.size}", "Completed: $recorded/${teeth.size}"),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "3 · Diente seleccionado", "3 · Selected tooth")) {
            AdaptiveGridV17(
                selectedSextant.size,
                if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else selectedSextant.size
            ) { index ->
                val tooth = selectedSextant[index]
                FilterChip(
                    selected = selectedTooth == tooth,
                    onClick = { selectedTooth = tooth },
                    label = { Text("OD $tooth · ${toothStatusLabel(tooth)}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (selectedMissing) {
                NoticeCard(
                    tr(
                        lang,
                        "OD $selectedTooth está ausente en el odontograma. En la forma OMS se registra como X = diente no presente.",
                        "Tooth $selectedTooth is missing in the odontogram. In the WHO form it is recorded as X = tooth not present."
                    )
                )
            } else {
                Text(
                    tr(
                        lang,
                        "Explora suavemente todo el surco/bolsa alrededor del diente con sonda CPI OMS; el registro de la forma se resume por diente.",
                        "Gently explore the full sulcus/pocket around the tooth with the WHO CPI probe; the form record is summarized per tooth."
                    )
                )
            }
        }

        if (!selectedMissing) {
            ResponsiveSectionV17(tr(lang, "4 · Sangrado gingival", "4 · Gingival bleeding")) {
                listOf(
                    0 to tr(lang, "0 · Ausencia de sangrado", "0 · No bleeding"),
                    1 to tr(lang, "1 · Presencia de sangrado", "1 · Bleeding present"),
                    9 to tr(lang, "9 · Diente excluido", "9 · Tooth excluded")
                ).forEach { (code, label) ->
                    FilterChip(
                        selected = bleedingCode == code,
                        onClick = { setBleeding(code) },
                        label = { Text(label) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    tr(
                        lang,
                        "0 sólo se selecciona después de examinar el diente y confirmar ausencia de sangrado; un campo vacío no equivale a 0.",
                        "Select 0 only after examining the tooth and confirming no bleeding; an empty field is not equivalent to 0."
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            ResponsiveSectionV17(tr(lang, "5 · Bolsas periodontales", "5 · Periodontal pockets")) {
                if (!age15Plus) {
                    NoticeCard(
                        tr(
                            lang,
                            "No se registran bolsas en este grupo de edad según el protocolo OMS de la 5.ª edición.",
                            "Pocket scores are not recorded for this age group under the WHO 5th-edition protocol."
                        )
                    )
                } else {
                    listOf(
                        0 to tr(lang, "0 · Sin bolsa de 4 mm o más", "0 · No pocket 4 mm or deeper"),
                        1 to tr(lang, "1 · Bolsa de 4–5 mm", "1 · Pocket 4–5 mm"),
                        2 to tr(lang, "2 · Bolsa de 6 mm o más", "2 · Pocket 6 mm or deeper"),
                        9 to tr(lang, "9 · Diente excluido", "9 · Tooth excluded")
                    ).forEach { (code, label) ->
                        FilterChip(
                            selected = pocketCode == code,
                            onClick = { setPocket(code) },
                            label = { Text(label) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "6 · Técnica y sonda", "6 · Technique and probe")) {
            Text(
                tr(
                    lang,
                    "Sonda CPI OMS: punta esférica de 0.5 mm, banda negra entre 3.5 y 5.5 mm y anillos a 8.5 y 11.5 mm. La fuerza recomendada no debe superar aproximadamente 20 g.",
                    "WHO CPI probe: 0.5-mm ball tip, black band from 3.5 to 5.5 mm, and rings at 8.5 and 11.5 mm. Recommended probing force should not exceed approximately 20 g."
                )
            )
            Text(
                tr(
                    lang,
                    "⚠️ Error común: convertir automáticamente sangrado en bolsa, o usar el código de bolsa como diagnóstico individual de periodontitis.",
                    "⚠️ Common mistake: automatically turning bleeding into a pocket score, or using the pocket code as an individual diagnosis of periodontitis."
                ),
                color = MaterialTheme.colorScheme.error
            )
        }

        ResponsiveSectionV17(tr(lang, "7 · Resumen del ejercicio", "7 · Exercise summary")) {
            Text(
                tr(
                    lang,
                    "Dientes presentes: ${summary.presentTeeth}. Sangrado registrado: ${summary.bleedingRecorded}; positivos: ${summary.bleedingPositive}.",
                    "Present teeth: ${summary.presentTeeth}. Bleeding recorded: ${summary.bleedingRecorded}; positive: ${summary.bleedingPositive}."
                )
            )
            if (age15Plus) {
                Text(
                    tr(
                        lang,
                        "Bolsas registradas: ${summary.pocketRecorded}; 4–5 mm: ${summary.pocket45}; ≥6 mm: ${summary.pocket6Plus}.",
                        "Pocket scores recorded: ${summary.pocketRecorded}; 4–5 mm: ${summary.pocket45}; ≥6 mm: ${summary.pocket6Plus}."
                    )
                )
            }
            Text(
                tr(
                    lang,
                    "Dientes excluidos con código 9: ${summary.excludedTeeth}.",
                    "Teeth excluded with code 9: ${summary.excludedTeeth}."
                )
            )

            if (summary.complete) {
                Button(
                    onClick = {
                        TeachingStateV40.moduleSummaries["cpi_modified"] =
                            "CPI modificado · sangrado ${summary.bleedingPositive}/${summary.bleedingRecorded}" +
                                if (age15Plus) " · bolsas 4–5 mm ${summary.pocket45} · ≥6 mm ${summary.pocket6Plus}" else ""
                        TeachingStateV40.savedPracticeSections["cpi_modified_v48"] = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tr(lang, "Guardar resumen CPI modificado", "Save modified CPI summary"))
                }
            } else {
                NoticeCard(
                    tr(
                        lang,
                        "El registro está incompleto. Los dientes todavía sin código no se interpretan como sanos.",
                        "The record is incomplete. Teeth without a recorded code are not interpreted as healthy."
                    )
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "8 · CPI clásico 0–4 · referencia", "8 · Classic CPI 0–4 · reference")) {
            Text(
                tr(
                    lang,
                    "Versiones previas y numerosos estudios resumen cada sextante con el hallazgo de mayor gravedad: 0 sano · 1 sangrado · 2 cálculo · 3 bolsa 4–5 mm · 4 bolsa ≥6 mm · X sextante excluido. No mezcles este sistema con los códigos separados del CPI modificado.",
                    "Earlier versions and many studies summarize each sextant by the highest finding: 0 healthy · 1 bleeding · 2 calculus · 3 pocket 4–5 mm · 4 pocket ≥6 mm · X excluded sextant. Do not mix this system with the separate modified-CPI codes."
                )
            )
        }

        NoticeCard(
            tr(
                lang,
                "Fuente principal: World Health Organization. Oral Health Surveys: Basic Methods. 5th ed. WHO; 2013. CPI modificado. https://www.who.int/publications/i/item/9789241548649",
                "Primary source: World Health Organization. Oral Health Surveys: Basic Methods. 5th ed. WHO; 2013. Modified CPI. https://www.who.int/publications/i/item/9789241548649"
            )
        )

        PracticeSaveControlsV48(lang, "cpi_modified_v48")
    }
}
