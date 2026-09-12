package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

@Composable
fun ToothSelector(
    teeth: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    isMarked: (Int) -> Boolean = { false }
) {
    DentalArchSelector(teeth, selected, onSelected, isMarked)
}

private fun statusName(status: ToothStatus, lang: String): String = when (status) {
    ToothStatus.HEALTHY -> tr(lang, "Presente", "Present")
    ToothStatus.CARIES -> tr(lang, "Cariado", "Carious")
    ToothStatus.RESTORED -> tr(lang, "Obturado", "Filled")
    ToothStatus.MISSING_CARIES -> tr(lang, "Ausente por caries", "Missing due to caries")
    ToothStatus.MISSING_OTHER -> tr(lang, "Ausente por otra causa", "Missing for another reason")
    ToothStatus.EXTRACTION_INDICATED -> tr(lang, "Extracción indicada", "Extraction indicated")
    ToothStatus.SEALANT -> tr(lang, "Sellador", "Sealant")
}

private fun surfaceName(surface: Surface, lang: String): String = when (surface) {
    Surface.VESTIBULAR -> tr(lang, "Vestibular", "Buccal")
    Surface.LINGUAL_PALATAL -> tr(lang, "Lingual / palatina", "Lingual / palatal")
    Surface.MESIAL -> "Mesial"
    Surface.DISTAL -> "Distal"
    Surface.OCCLUSAL -> tr(lang, "Oclusal", "Occlusal")
}

private fun markName(mark: SurfaceMark, lang: String): String = when (mark) {
    SurfaceMark.HEALTHY -> tr(lang, "Borrar / sano", "Clear / sound")
    SurfaceMark.CARIES -> tr(lang, "Caries · rojo", "Caries · red")
    SurfaceMark.RESTORATION -> tr(lang, "Restauración · azul", "Restoration · blue")
    SurfaceMark.SEALANT -> tr(lang, "Sellador", "Sealant")
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
    var selectedMark by remember { mutableStateOf(SurfaceMark.CARIES) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val marks = session.odontogramSurfaces[selectedTooth] ?: emptyMap()

    fun saveSurface(surface: Surface) {
        val toothSurfaces = marks.toMutableMap()
        if (selectedMark == SurfaceMark.HEALTHY) toothSurfaces.remove(surface) else toothSurfaces[surface] = selectedMark
        val allSurfaceMaps = session.odontogramSurfaces.toMutableMap()
        allSurfaceMaps[selectedTooth] = toothSurfaces
        val derived = when {
            toothSurfaces.values.any { it == SurfaceMark.CARIES } -> ToothStatus.CARIES
            toothSurfaces.values.any { it == SurfaceMark.RESTORATION } -> ToothStatus.RESTORED
            toothSurfaces.values.any { it == SurfaceMark.SEALANT } -> ToothStatus.SEALANT
            else -> ToothStatus.HEALTHY
        }
        val toothMap = session.teeth.toMutableMap()
        toothMap[selectedTooth] = record.copy(status = derived)
        onSessionChanged(
            session.copy(
                teeth = toothMap,
                odontogramSurfaces = allSurfaceMaps,
                presentTeeth = session.presentTeeth + selectedTooth
            )
        )
    }

    fun setWholeStatus(status: ToothStatus) {
        val toothMap = session.teeth.toMutableMap()
        toothMap[selectedTooth] = record.copy(status = status)
        val present = session.presentTeeth.toMutableSet()
        if (status == ToothStatus.MISSING_CARIES || status == ToothStatus.MISSING_OTHER) present.remove(selectedTooth)
        else present.add(selectedTooth)
        onSessionChanged(session.copy(teeth = toothMap, presentTeeth = present))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Odontograma por caras", "Odontogram by surfaces"),
                onBack,
                tr(
                    lang,
                    "El odontograma se aprende pintando la superficie dental correspondiente. Toca una cara del esquema: vestibular, lingual/palatina, mesial, distal u oclusal.",
                    "Learn the odontogram by marking the corresponding dental surface. Tap buccal, lingual/palatal, mesial, distal or occlusal."
                )
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanentes", "Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporales", "Primary")) })
            }
        }
        item {
            SectionCard(tr(lang, "Arcada dental", "Dental arch")) {
                Text(tr(lang, "El número queda debajo del diente como referencia; la interacción principal es visual.",
                    "The number remains below the tooth as a reference; the main interaction is visual."))
                DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { tooth ->
                    session.odontogramSurfaces[tooth]?.isNotEmpty() == true ||
                        session.teeth[tooth]?.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER)
                }
            }
        }
        item {
            SectionCard("OD $selectedTooth") {
                Text(tr(lang, "1. Elige qué quieres marcar", "1. Choose what to mark"), fontWeight = FontWeight.Bold)
                SurfaceMark.entries.forEach { mark ->
                    FilterChip(
                        selected = selectedMark == mark,
                        onClick = { selectedMark = mark },
                        label = { Text(markName(mark, lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(tr(lang, "2. Toca la cara dental", "2. Tap the dental surface"), fontWeight = FontWeight.Bold)
                DentalSurfaceDiagram(
                    centerEnabled = true,
                    surfaceColor = { surface ->
                        when (marks[surface]) {
                            SurfaceMark.CARIES -> Color(0xFFD64545)
                            SurfaceMark.RESTORATION -> Color(0xFF3C74C9)
                            SurfaceMark.SEALANT -> Color(0xFF62A56A)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    },
                    onSurfaceTap = { saveSurface(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(tr(lang, "Ausencia o extracción indicada se registra para todo el diente:",
                    "Absence or extraction indication is recorded for the whole tooth:"))
                listOf(ToothStatus.HEALTHY, ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER, ToothStatus.EXTRACTION_INDICATED).forEach { status ->
                    FilterChip(
                        selected = record.status == status && (status != ToothStatus.HEALTHY || marks.isEmpty()),
                        onClick = { setWholeStatus(status) },
                        label = { Text(statusName(status, lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
            NoticeCard(
                tr(
                    lang,
                    "Convención didáctica del expediente: caries se marca en rojo y restauración en azul sobre la cara afectada. La ausencia por una causa distinta de caries no debe contarse como P en CPOD.",
                    "Teaching convention: caries is marked red and restorations blue on the affected surface. A tooth missing for a reason other than caries is not counted as M in DMFT."
                )
            )
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
    var selectedSurface by remember { mutableStateOf(Surface.OCCLUSAL) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val surfaceCodes = session.icdasSurfaces[selectedTooth] ?: emptyMap()
    val currentCode = surfaceCodes[selectedSurface] ?: 0

    fun setCode(code: Int) {
        val bySurface = surfaceCodes.toMutableMap()
        bySurface[selectedSurface] = code
        val all = session.icdasSurfaces.toMutableMap()
        all[selectedTooth] = bySurface
        val maxCode = bySurface.values.maxOrNull() ?: 0
        val record = session.teeth[selectedTooth] ?: ToothRecord()
        val updatedStatus = if (maxCode > 0) ToothStatus.CARIES else if (record.status == ToothStatus.CARIES) ToothStatus.HEALTHY else record.status
        onSessionChanged(
            session.copy(
                icdasSurfaces = all,
                teeth = session.teeth + (selectedTooth to record.copy(icdas = maxCode, status = updatedStatus)),
                presentTeeth = session.presentTeeth + selectedTooth
            )
        )
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "ICDAS",
                onBack,
                tr(lang,
                    "ICDAS se registra por superficie. A las cuatro caras vestibular, lingual/palatina, mesial y distal se agrega la superficie oclusal.",
                    "ICDAS is recorded by surface. The occlusal surface is added to buccal, lingual/palatal, mesial and distal surfaces.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanentes", "Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporales", "Primary")) })
            }
        }
        item {
            SectionCard(tr(lang, "Selecciona diente y superficie", "Select tooth and surface")) {
                DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { session.icdasSurfaces[it]?.values?.any { code -> code > 0 } == true }
                DentalSurfaceDiagram(
                    centerEnabled = true,
                    surfaceColor = { surface ->
                        val code = surfaceCodes[surface] ?: 0
                        when {
                            surface == selectedSurface -> MaterialTheme.colorScheme.primaryContainer
                            code >= 5 -> MaterialTheme.colorScheme.errorContainer
                            code > 0 -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    },
                    onSurfaceTap = { selectedSurface = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${surfaceName(selectedSurface, lang)} · ICDAS $currentCode", fontWeight = FontWeight.Bold)
            }
        }
        items(ClinicalContent.icdas.size) { index ->
            val guide = ClinicalContent.icdas[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentCode == guide.code) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                onClick = { setCode(guide.code) }
            ) {
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(guide.code.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(if (lang == "en") guide.en else guide.es)
                        if (currentCode == guide.code) {
                            Text(tr(lang, "✓ Código seleccionado para ${surfaceName(selectedSurface, lang)}", "✓ Selected for ${surfaceName(selectedSurface, lang)}"),
                                color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
        item {
            val category = when (currentCode) {
                0 -> tr(lang, "Superficie sana", "Sound surface")
                1, 2 -> tr(lang, "Lesión inicial", "Initial lesion")
                3, 4 -> tr(lang, "Lesión moderada", "Moderate lesion")
                else -> tr(lang, "Lesión severa / extensa", "Severe / extensive lesion")
            }
            NoticeCard("OD $selectedTooth · ${surfaceName(selectedSurface, lang)} · ICDAS $currentCode · $category")
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
                tr(lang, "Aquí la unidad sí es el diente completo. CPOD = C + P + O; ceod = c + e + o.",
                    "Here the unit is the whole tooth. DMFT = D + M + F; dmft follows the primary-dentition equivalent.")
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
                "El alumno puede volver al odontograma, marcar visualmente los dientes y observar cómo cambia el índice. El cálculo es una ayuda para aprender la fórmula.",
                "Return to the odontogram, mark teeth visually and observe how the index changes. The calculation is a learning aid for the formula."))
        }
    }
}
