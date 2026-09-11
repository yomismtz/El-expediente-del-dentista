package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.PerioRecord
import com.yomismtz.expedientedeldentista.clinical.PulpalAssessment
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

@Composable
fun ToothSelector(
    teeth: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    isMarked: (Int) -> Boolean = { false }
) {
    teeth.chunked(8).forEach { rowTeeth ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            rowTeeth.forEach { tooth ->
                FilterChip(
                    selected = tooth == selected,
                    onClick = { onSelected(tooth) },
                    label = { Text(if (isMarked(tooth)) "$tooth•" else tooth.toString()) },
                    modifier = Modifier.weight(1f)
                )
            }
            repeat(8 - rowTeeth.size) { Text("", modifier = Modifier.weight(1f)) }
        }
    }
}

private fun statusName(status: ToothStatus, lang: String): String = when (status) {
    ToothStatus.HEALTHY -> tr(lang, "Sano", "Healthy")
    ToothStatus.CARIES -> tr(lang, "Cariado", "Carious")
    ToothStatus.RESTORED -> tr(lang, "Obturado", "Filled")
    ToothStatus.MISSING_CARIES -> tr(lang, "Ausente por caries", "Missing due to caries")
    ToothStatus.MISSING_OTHER -> tr(lang, "Ausente por otra causa", "Missing for another reason")
    ToothStatus.EXTRACTION_INDICATED -> tr(lang, "Extracción indicada", "Extraction indicated")
    ToothStatus.SEALANT -> tr(lang, "Sellador", "Sealant")
}

@Composable
fun OdontogramScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var primary by remember { mutableStateOf(false) }
    var selectedTooth by remember { mutableStateOf(16) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()

    fun updateRecord(updated: ToothRecord) {
        val map = session.teeth.toMutableMap()
        map[selectedTooth] = updated
        val present = session.presentTeeth.toMutableSet()
        if (updated.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER)) {
            present.remove(selectedTooth)
        } else {
            present.add(selectedTooth)
        }
        onSessionChanged(session.copy(teeth = map, presentTeeth = present))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Odontograma interactivo", "Interactive odontogram"),
                onBack,
                tr(lang,
                    "Selecciona un órgano dentario y registra su condición. CPOD/ceod se actualizan automáticamente. Una ausencia por agenesia, ortodoncia o falta de erupción no se suma como pérdida por caries.",
                    "Select a tooth and record its condition. DMFT/dmft updates automatically. Absence due to agenesis, orthodontics or lack of eruption is not counted as missing due to caries.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanentes", "Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporales", "Primary")) })
            }
        }
        item {
            SectionCard(tr(lang, "Selecciona el diente", "Select tooth")) {
                ToothSelector(shown, selectedTooth, { selectedTooth = it }) { tooth ->
                    session.teeth[tooth]?.status != null && session.teeth[tooth]?.status != ToothStatus.HEALTHY
                }
            }
        }
        item {
            SectionCard("OD $selectedTooth") {
                ToothStatus.entries.forEach { status ->
                    FilterChip(
                        selected = record.status == status,
                        onClick = { updateRecord(record.copy(status = status)) },
                        label = { Text(statusName(status, lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    tr(lang,
                        "ICDAS actual: ${record.icdas}. Puedes modificarlo con mayor detalle en la pestaña ICDAS.",
                        "Current ICDAS: ${record.icdas}. You can change it in detail in the ICDAS tab."),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        item {
            NoticeCard(tr(lang,
                "Regla de aprendizaje: si un diente tiene restauración y caries activa, para CPOD/ceod se prioriza la condición cariosa. En esta app selecciona Cariado cuando exista enfermedad activa.",
                "Learning rule: if a tooth has both a restoration and active caries, the carious condition takes priority for DMFT/dmft. Select Carious when active disease is present."))
        }
    }
}

@Composable
fun IcdasScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedTooth by remember { mutableStateOf(16) }
    var primary by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "ICDAS",
                onBack,
                tr(lang, "Selecciona el hallazgo visual que corresponda; el código queda asociado al diente dentro de la sesión.",
                    "Select the matching visual finding; the code is associated with the tooth during the session.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanentes", "Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporales", "Primary")) })
            }
        }
        item { SectionCard(tr(lang, "Diente", "Tooth")) { ToothSelector(shown, selectedTooth, { selectedTooth = it }) } }
        items(ClinicalContent.icdas.size) { index ->
            val guide = ClinicalContent.icdas[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (record.icdas == guide.code) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                onClick = {
                    val map = session.teeth.toMutableMap()
                    val status = if (guide.code == 0 && record.status == ToothStatus.CARIES) ToothStatus.HEALTHY
                    else if (guide.code > 0 && record.status == ToothStatus.HEALTHY) ToothStatus.CARIES
                    else record.status
                    map[selectedTooth] = record.copy(icdas = guide.code, status = status)
                    onSessionChanged(session.copy(teeth = map, presentTeeth = session.presentTeeth + selectedTooth))
                }
            ) {
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(guide.code.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(if (lang == "en") guide.en else guide.es, modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            val category = when (record.icdas) {
                0 -> tr(lang, "Sano", "Sound")
                1, 2 -> tr(lang, "Lesión inicial", "Initial lesion")
                3, 4 -> tr(lang, "Lesión moderada", "Moderate lesion")
                else -> tr(lang, "Lesión severa/extensa", "Severe/extensive lesion")
            }
            NoticeCard("OD $selectedTooth · ICDAS ${record.icdas} · $category")
        }
    }
}

@Composable
fun CpodScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    val permanent = ClinicalEngines.cpod(session.teeth, primary = false)
    val primary = ClinicalEngines.cpod(session.teeth, primary = true)
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "CPOD / ceod", "DMFT / dmft"),
                onBack,
                tr(lang, "El cálculo usa los datos que registraste en el odontograma. La unidad es el diente, no la superficie.",
                    "The calculation uses the odontogram data. The unit is the tooth, not the surface.")
            )
        }
        item {
            SectionCard(tr(lang, "Dentición permanente · CPOD", "Permanent dentition · DMFT")) {
                Text("C = ${permanent.carious}   P = ${permanent.missing}   O = ${permanent.filled}", style = MaterialTheme.typography.titleLarge)
                Text("CPOD = ${permanent.carious} + ${permanent.missing} + ${permanent.filled} = ${permanent.total}", fontWeight = FontWeight.Bold)
                Text(ClinicalEngines.cpodInterpretation(permanent.total, lang))
            }
        }
        item {
            SectionCard(tr(lang, "Dentición temporal · ceod", "Primary dentition · dmft")) {
                Text("c = ${primary.carious}   e = ${primary.missing}   o = ${primary.filled}", style = MaterialTheme.typography.titleLarge)
                Text("ceod = ${primary.carious} + ${primary.missing} + ${primary.filled} = ${primary.total}", fontWeight = FontWeight.Bold)
                Text(ClinicalEngines.cpodInterpretation(primary.total, lang))
            }
        }
        item {
            NoticeCard(tr(lang,
                "Los valores se muestran con finalidad docente. La interpretación epidemiológica depende del criterio y del grupo de edad utilizado en cada estudio.",
                "Values are shown for teaching. Epidemiologic interpretation depends on the criterion and age group used in each study."))
        }
    }
}
