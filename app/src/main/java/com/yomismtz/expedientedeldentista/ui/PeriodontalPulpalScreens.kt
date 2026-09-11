package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.yomismtz.expedientedeldentista.clinical.PerioRecord
import com.yomismtz.expedientedeldentista.clinical.PulpalAssessment

@Composable
fun PeriodontogramScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedTooth by remember { mutableStateOf(16) }
    val record = session.periodontogram[selectedTooth] ?: PerioRecord()
    val siteNames = listOf("MV/MB", "V/B", "DV/DB", "ML", "L/P", "DL")
    fun update(updated: PerioRecord) {
        onSessionChanged(session.copy(periodontogram = session.periodontogram + (selectedTooth to updated)))
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Periodontograma", "Periodontal chart"),
                onBack,
                tr(lang, "Registra seis sitios por diente, sangrado, placa, supuración, movilidad, furcación y recesión. El resumen orienta el aprendizaje, no reemplaza el diagnóstico periodontal completo.",
                    "Record six sites per tooth, bleeding, plaque, suppuration, mobility, furcation and recession. The summary supports learning and does not replace a complete periodontal diagnosis.")
            )
        }
        item {
            SectionCard(tr(lang, "Selecciona diente", "Select tooth")) {
                ToothSelector(ClinicalContent.permanentTeeth, selectedTooth, { selectedTooth = it }) { it in session.periodontogram }
            }
        }
        item {
            SectionCard("OD $selectedTooth · ${tr(lang, "Profundidad de sondaje (mm)", "Probing depth (mm)")}") {
                siteNames.chunked(3).forEachIndexed { rowIndex, row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        row.forEachIndexed { colIndex, site ->
                            val index = rowIndex * 3 + colIndex
                            OutlinedTextField(
                                value = record.probingDepths[index].takeIf { it > 0 }?.toString() ?: "",
                                onValueChange = { raw ->
                                    val value = raw.toIntOrNull()?.coerceIn(0, 15) ?: 0
                                    val values = record.probingDepths.toMutableList()
                                    values[index] = value
                                    update(record.copy(probingDepths = values))
                                },
                                label = { Text(site) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = record.recessionMm.takeIf { it != 0 }?.toString() ?: "",
                    onValueChange = { update(record.copy(recessionMm = it.toIntOrNull()?.coerceIn(-10, 15) ?: 0)) },
                    label = { Text(tr(lang, "Margen/recesión gingival (mm)", "Gingival margin/recession (mm)")) },
                    modifier = Modifier.fillMaxWidth()
                )
                BooleanRow(tr(lang, "Sangrado al sondaje", "Bleeding on probing"), record.bleeding) { update(record.copy(bleeding = it)) }
                BooleanRow(tr(lang, "Placa", "Plaque"), record.plaque) { update(record.copy(plaque = it)) }
                BooleanRow(tr(lang, "Supuración", "Suppuration"), record.suppuration) { update(record.copy(suppuration = it)) }
                Text(tr(lang, "Movilidad", "Mobility"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..3).forEach { grade ->
                        FilterChip(record.mobility == grade, { update(record.copy(mobility = grade)) }, { Text(grade.toString()) }, modifier = Modifier.weight(1f))
                    }
                }
                Text(tr(lang, "Furcación", "Furcation"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..3).forEach { grade ->
                        FilterChip(record.furcation == grade, { update(record.copy(furcation = grade)) }, { Text(grade.toString()) }, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        item { NoticeCard(ClinicalEngines.periodontalSummary(session, lang)) }
    }
}

@Composable
fun PulpalScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val a = session.pulpal
    fun update(updated: PulpalAssessment) = onSessionChanged(session.copy(pulpal = updated))
    val result = ClinicalEngines.pulpalDiagnosis(a)

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Diagnóstico pulpar y periapical", "Pulpal and periapical diagnosis"),
                onBack,
                tr(lang, "Selecciona síntomas, pruebas clínicas y hallazgos radiográficos. La app muestra el diagnóstico más compatible y qué datos lo apoyan.",
                    "Select symptoms, clinical tests and radiographic findings. The app shows the most compatible diagnosis and the supporting data.")
            )
        }
        item {
            SectionCard(tr(lang, "Órgano dentario", "Tooth")) {
                OutlinedTextField(
                    value = if (a.tooth == 0) "" else a.tooth.toString(),
                    onValueChange = { update(a.copy(tooth = it.toIntOrNull() ?: 0)) },
                    label = { Text("OD") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            SectionCard(tr(lang, "Síntomas y pruebas pulpares", "Pulpal symptoms and tests")) {
                BooleanRow(tr(lang, "Dolor espontáneo", "Spontaneous pain"), a.spontaneousPain) { update(a.copy(spontaneousPain = it)) }
                BooleanRow(tr(lang, "Dolor nocturno", "Night pain"), a.nightPain) { update(a.copy(nightPain = it)) }
                BooleanRow(tr(lang, "Respuesta al frío", "Positive cold response"), a.coldPositive) { update(a.copy(coldPositive = it)) }
                BooleanRow(tr(lang, "Dolor al frío persiste >10 s", "Cold pain lingers >10 s"), a.coldLingering) { update(a.copy(coldLingering = it)) }
                BooleanRow(tr(lang, "Respuesta dolorosa al calor", "Painful heat response"), a.heatPositive) { update(a.copy(heatPositive = it)) }
                BooleanRow(tr(lang, "Dolor con alimentos dulces", "Pain with sweets"), a.sweetsPain) { update(a.copy(sweetsPain = it)) }
                BooleanRow(tr(lang, "Sin respuesta a pruebas de sensibilidad", "No response to sensitivity tests"), a.sensitivityNegative) { update(a.copy(sensitivityNegative = it)) }
                BooleanRow(tr(lang, "Diente previamente tratado endodónticamente", "Previously root-canal treated tooth"), a.previousRootCanal) { update(a.copy(previousRootCanal = it)) }
                BooleanRow(tr(lang, "Terapia endodóntica previamente iniciada", "Previously initiated endodontic therapy"), a.previousPartialEndo) { update(a.copy(previousPartialEndo = it)) }
            }
        }
        item {
            SectionCard(tr(lang, "Signos periapicales y radiografía", "Periapical signs and radiograph")) {
                BooleanRow(tr(lang, "Dolor a la percusión / masticación", "Percussion / biting pain"), a.percussionPain) { update(a.copy(percussionPain = it)) }
                BooleanRow(tr(lang, "Dolor a la palpación apical", "Apical palpation pain"), a.palpationPain) { update(a.copy(palpationPain = it)) }
                BooleanRow(tr(lang, "Aumento de volumen", "Swelling"), a.swelling) { update(a.copy(swelling = it)) }
                BooleanRow(tr(lang, "Fístula / tracto sinuoso", "Sinus tract"), a.fistula) { update(a.copy(fistula = it)) }
                BooleanRow(tr(lang, "Radiolucidez apical", "Apical radiolucency"), a.apicalRadiolucency) { update(a.copy(apicalRadiolucency = it)) }
                BooleanRow(tr(lang, "Ensanchamiento del ligamento periodontal", "Widened periodontal ligament"), a.widenedPdl) { update(a.copy(widenedPdl = it)) }
                BooleanRow(tr(lang, "Radiopacidad apical difusa", "Diffuse apical radiopacity"), a.apicalRadiopacity) { update(a.copy(apicalRadiopacity = it)) }
                BooleanRow(tr(lang, "Caries profunda o exposición con compromiso pulpar evidente", "Deep caries or exposure with evident pulpal involvement"), a.deepCariesOrExposure) { update(a.copy(deepCariesOrExposure = it)) }
            }
        }
        item {
            SectionCard(tr(lang, "Resultado educativo", "Educational result")) {
                Text(if (lang == "en") result.pulpalEn else result.pulpalEs, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(if (lang == "en") result.apicalEn else result.apicalEs, style = MaterialTheme.typography.titleMedium)
                Text(if (lang == "en") result.explanationEn else result.explanationEs)
            }
        }
    }
}
