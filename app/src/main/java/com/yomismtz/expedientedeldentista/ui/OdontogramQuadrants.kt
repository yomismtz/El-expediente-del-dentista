package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.Surface as ToothSurface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

private val QLavender = Color(0xFFE9DBF5)
private val QLilac = Color(0xFFD2B9E8)
private val QPurple = Color(0xFF7A4BA5)
private val QDeep = Color(0xFF43235F)
private val QMetal = Color(0xFFB4ADB8)

private data class DentalQuadrant(val titleEs: String, val titleEn: String, val teeth: List<Int>)

private fun quadrants(primary: Boolean): List<DentalQuadrant> = if (!primary) {
    listOf(
        DentalQuadrant("Q1 · superior derecho", "Q1 · upper right", listOf(18,17,16,15,14,13,12,11)),
        DentalQuadrant("Q2 · superior izquierdo", "Q2 · upper left", listOf(21,22,23,24,25,26,27,28)),
        DentalQuadrant("Q4 · inferior derecho", "Q4 · lower right", listOf(48,47,46,45,44,43,42,41)),
        DentalQuadrant("Q3 · inferior izquierdo", "Q3 · lower left", listOf(31,32,33,34,35,36,37,38))
    )
} else {
    listOf(
        DentalQuadrant("Q5 · superior derecho", "Q5 · upper right", listOf(55,54,53,52,51)),
        DentalQuadrant("Q6 · superior izquierdo", "Q6 · upper left", listOf(61,62,63,64,65)),
        DentalQuadrant("Q8 · inferior derecho", "Q8 · lower right", listOf(85,84,83,82,81)),
        DentalQuadrant("Q7 · inferior izquierdo", "Q7 · lower left", listOf(71,72,73,74,75))
    )
}

private fun wholeStatusName(status: ToothStatus, lang: String) = when (status) {
    ToothStatus.HEALTHY -> tr(lang, "Presente / sano", "Present / sound")
    ToothStatus.MISSING_CARIES -> tr(lang, "Ausente por caries", "Missing due to caries")
    ToothStatus.MISSING_OTHER -> tr(lang, "Ausente por otra causa", "Missing for another reason")
    ToothStatus.EXTRACTION_INDICATED -> tr(lang, "Extracción indicada", "Extraction indicated")
    else -> status.name
}

private fun markLabel(mark: SurfaceMark, lang: String) = when (mark) {
    SurfaceMark.HEALTHY -> tr(lang, "Limpiar", "Clear")
    SurfaceMark.CARIES -> tr(lang, "Caries", "Caries")
    SurfaceMark.RESTORATION -> tr(lang, "Restauración", "Restoration")
    SurfaceMark.SEALANT -> tr(lang, "Sellador", "Sealant")
}

@Composable
fun OdontogramQuadrantsScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var primary by remember { mutableStateOf(false) }
    var selectedTooth by remember { mutableStateOf(16) }
    var selectedMark by remember { mutableStateOf(SurfaceMark.CARIES) }
    var legendMode by remember { mutableStateOf("ODONTO") }
    val qs = quadrants(primary)
    val allTeeth = qs.flatMap { it.teeth }
    if (selectedTooth !in allTeeth) selectedTooth = allTeeth.first()

    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val marks = session.odontogramSurfaces[selectedTooth] ?: emptyMap()
    val crownSurfaces = listOf(ToothSurface.VESTIBULAR, ToothSurface.LINGUAL_PALATAL, ToothSurface.MESIAL, ToothSurface.DISTAL, ToothSurface.OCCLUSAL)

    fun saveMap(newMarks: Map<ToothSurface, SurfaceMark>) {
        val all = session.odontogramSurfaces.toMutableMap().apply { put(selectedTooth, newMarks) }
        val derived = when {
            newMarks.values.any { it == SurfaceMark.CARIES } -> ToothStatus.CARIES
            newMarks.values.any { it == SurfaceMark.RESTORATION } -> ToothStatus.RESTORED
            newMarks.values.any { it == SurfaceMark.SEALANT } -> ToothStatus.SEALANT
            else -> ToothStatus.HEALTHY
        }
        val teeth = session.teeth + (selectedTooth to record.copy(status = derived))
        onSessionChanged(session.copy(teeth = teeth, odontogramSurfaces = all, presentTeeth = session.presentTeeth + selectedTooth))
    }

    fun saveSurface(surface: ToothSurface) {
        val newMarks = marks.toMutableMap()
        if (selectedMark == SurfaceMark.HEALTHY) newMarks.remove(surface) else newMarks[surface] = selectedMark
        saveMap(newMarks)
    }

    fun setWholeStatus(status: ToothStatus) {
        val teeth = session.teeth + (selectedTooth to record.copy(status = status))
        val present = session.presentTeeth.toMutableSet()
        if (status == ToothStatus.MISSING_CARIES || status == ToothStatus.MISSING_OTHER) present.remove(selectedTooth) else present.add(selectedTooth)
        onSessionChanged(session.copy(teeth = teeth, presentTeeth = present))
    }

    LazyColumn(
        Modifier.fillMaxSize().padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Odontograma por cuadrantes", "Quadrant odontogram"),
                onBack,
                tr(lang, "Los dientes se muestran en cuatro cuadrantes para reducir desplazamientos. Toca un diente y después marca una o varias superficies.", "Teeth are arranged in four quadrants to reduce scrolling. Tap a tooth, then mark one or more surfaces.")
            )
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanente", "Permanent")) }, modifier = Modifier.weight(1f))
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporal", "Primary")) }, modifier = Modifier.weight(1f))
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                qs.chunked(2).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        row.forEach { quadrant ->
                            QuadrantCard(
                                lang = lang,
                                quadrant = quadrant,
                                selectedTooth = selectedTooth,
                                marked = { tooth ->
                                    session.odontogramSurfaces[tooth]?.isNotEmpty() == true ||
                                        session.teeth[tooth]?.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER, ToothStatus.EXTRACTION_INDICATED)
                                },
                                onSelected = { selectedTooth = it },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCF9FE)),
                border = BorderStroke(1.dp, QMetal),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("OD $selectedTooth", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = QDeep)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        SurfaceMark.entries.forEach { mark ->
                            FilterChip(selectedMark == mark, { selectedMark = mark }, { Text(markLabel(mark, lang), fontSize = 10.sp) }, modifier = Modifier.weight(1f))
                        }
                    }
                    DentalSurfaceDiagram(
                        centerEnabled = true,
                        surfaceColor = { surface ->
                            when (marks[surface]) {
                                SurfaceMark.CARIES -> Color(0xFFE04B57)
                                SurfaceMark.RESTORATION -> Color(0xFF4C8DEB)
                                SurfaceMark.SEALANT -> Color(0xFF9B6AD6)
                                else -> QLavender.copy(alpha = 0.55f)
                            }
                        },
                        onSurfaceTap = { saveSurface(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedButton(
                        onClick = {
                            if (selectedMark == SurfaceMark.HEALTHY) saveMap(emptyMap())
                            else saveMap(crownSurfaces.associateWith { selectedMark })
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (selectedMark == SurfaceMark.HEALTHY) tr(lang, "Limpiar todas las caras", "Clear all surfaces") else tr(lang, "Aplicar a todas las caras", "Apply to all surfaces"))
                    }
                    Text(tr(lang, "Estado del diente completo", "Whole-tooth status"), fontWeight = FontWeight.Bold, color = QDeep)
                    listOf(ToothStatus.HEALTHY, ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER, ToothStatus.EXTRACTION_INDICATED).forEach { status ->
                        FilterChip(
                            selected = record.status == status && (status != ToothStatus.HEALTHY || marks.isEmpty()),
                            onClick = { setWholeStatus(status) },
                            label = { Text(wholeStatusName(status, lang)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Leyenda y modo de análisis", "Legend and analysis mode")) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("ODONTO", "ICDAS", "CPOD", "IHOS", "IPC").forEach { mode ->
                        FilterChip(legendMode == mode, { legendMode = mode }, { Text(mode, fontSize = 9.sp) }, modifier = Modifier.weight(1f))
                    }
                }
                val text = when (legendMode) {
                    "ICDAS" -> tr(lang, "ICDAS se registra por superficie; el resumen del diente toma el código de mayor relevancia entre sus caras.", "ICDAS is surface-based; the tooth summary uses the highest relevant surface code.")
                    "CPOD" -> tr(lang, "CPOD/ceod usa el diente como unidad de análisis y no cada superficie.", "DMFT/dmft uses the whole tooth as its unit of analysis.")
                    "IHOS" -> tr(lang, "IHOS utiliza dientes y superficies índice específicos; el módulo correspondiente guía cuáles registrar.", "OHI-S uses specific index teeth and surfaces; its module guides which ones to record.")
                    "IPC" -> tr(lang, "IPC se registra por sextantes y conserva el hallazgo de mayor código de cada sextante.", "CPI is recorded by sextants and keeps the highest code in each sextant.")
                    else -> tr(lang, "Rojo = caries · azul = restauración · lila = sellador. Las superficies pueden combinarse en un mismo diente.", "Red = caries · blue = restoration · lilac = sealant. Multiple surfaces may be marked on one tooth.")
                }
                Text(text)
            }
        }
    }
}

@Composable
private fun QuadrantCard(
    lang: String,
    quadrant: DentalQuadrant,
    selectedTooth: Int,
    marked: (Int) -> Boolean,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF9FF)),
        border = BorderStroke(1.dp, QLilac),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(7.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(if (lang == "en") quadrant.titleEn else quadrant.titleEs, color = QDeep, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                quadrant.teeth.forEach { tooth ->
                    val selected = tooth == selectedTooth
                    val hasMark = marked(tooth)
                    Card(
                        onClick = { onSelected(tooth) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(7.dp),
                        colors = CardDefaults.cardColors(containerColor = when {
                            selected -> QPurple
                            hasMark -> QLavender
                            else -> Color.White
                        }),
                        border = BorderStroke(0.7.dp, if (selected) QDeep else QMetal)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🦷", fontSize = 9.sp)
                            Text(tooth.toString(), fontSize = 7.sp, color = if (selected) Color.White else QDeep, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}
