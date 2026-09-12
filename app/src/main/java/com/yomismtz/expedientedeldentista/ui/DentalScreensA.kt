package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
fun ToothSelector(teeth: List<Int>, selected: Int, onSelected: (Int) -> Unit, isMarked: (Int) -> Boolean = { false }) {
    DentalArchSelector(teeth, selected, onSelected, isMarked)
}

private fun statusName(status: ToothStatus, lang: String) = when (status) {
    ToothStatus.HEALTHY -> tr(lang, "Presente / sano", "Present / sound")
    ToothStatus.CARIES -> tr(lang, "Cariado", "Carious")
    ToothStatus.RESTORED -> tr(lang, "Obturado", "Filled")
    ToothStatus.MISSING_CARIES -> tr(lang, "Ausente por caries", "Missing due to caries")
    ToothStatus.MISSING_OTHER -> tr(lang, "Ausente por otra causa", "Missing for another reason")
    ToothStatus.EXTRACTION_INDICATED -> tr(lang, "Extracción indicada", "Extraction indicated")
    ToothStatus.SEALANT -> tr(lang, "Sellador", "Sealant")
}

private fun surfaceName(surface: Surface, lang: String) = when (surface) {
    Surface.VESTIBULAR -> tr(lang, "Vestibular", "Buccal")
    Surface.LINGUAL_PALATAL -> tr(lang, "Lingual / palatina", "Lingual / palatal")
    Surface.MESIAL -> "Mesial"
    Surface.DISTAL -> "Distal"
    Surface.OCCLUSAL -> tr(lang, "Oclusal", "Occlusal")
}

private fun markName(mark: SurfaceMark, lang: String) = when (mark) {
    SurfaceMark.HEALTHY -> tr(lang, "Borrar / sano", "Clear / sound")
    SurfaceMark.CARIES -> tr(lang, "Caries · rojo", "Caries · red")
    SurfaceMark.RESTORATION -> tr(lang, "Restauración · azul", "Restoration · blue")
    SurfaceMark.SEALANT -> tr(lang, "Sellador · verde", "Sealant · green")
}

@Composable
fun OdontogramScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    var primary by remember { mutableStateOf(false) }
    var selectedTooth by remember { mutableStateOf(16) }
    var selectedMark by remember { mutableStateOf(SurfaceMark.CARIES) }
    var legendMode by remember { mutableStateOf("ODONTO") }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val marks = session.odontogramSurfaces[selectedTooth] ?: emptyMap()
    val crownSurfaces = listOf(Surface.VESTIBULAR, Surface.LINGUAL_PALATAL, Surface.MESIAL, Surface.DISTAL, Surface.OCCLUSAL)

    fun saveMap(newMarks: Map<Surface, SurfaceMark>) {
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

    fun saveSurface(surface: Surface) {
        val newMarks = marks.toMutableMap()
        if (selectedMark == SurfaceMark.HEALTHY) newMarks.remove(surface) else newMarks[surface] = selectedMark
        saveMap(newMarks)
    }

    fun markWholeTooth() {
        if (selectedMark == SurfaceMark.HEALTHY) saveMap(emptyMap())
        else saveMap(crownSurfaces.associateWith { selectedMark })
    }

    fun setWholeStatus(status: ToothStatus) {
        val teeth = session.teeth + (selectedTooth to record.copy(status = status))
        val present = session.presentTeeth.toMutableSet()
        if (status == ToothStatus.MISSING_CARIES || status == ToothStatus.MISSING_OTHER) present.remove(selectedTooth) else present.add(selectedTooth)
        onSessionChanged(session.copy(teeth = teeth, presentTeeth = present))
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, "Odontograma interactivo", "Interactive odontogram"), onBack,
            tr(lang, "Marca una o varias superficies. También puedes aplicar la marca elegida a toda la corona.", "Mark one or several surfaces. You can also apply the selected mark to the whole crown.")) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang,"Permanentes","Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang,"Temporales","Primary")) })
            }
        }
        item { SectionCard(tr(lang,"Arcada dental","Dental arch")) {
            DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { tooth ->
                session.odontogramSurfaces[tooth]?.isNotEmpty() == true || session.teeth[tooth]?.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER)
            }
        } }
        item { SectionCard("OD $selectedTooth") {
            SurfaceMark.entries.forEach { mark -> FilterChip(selectedMark==mark,{selectedMark=mark},{Text(markName(mark,lang))},modifier=Modifier.fillMaxWidth()) }
            DentalSurfaceDiagram(
                centerEnabled=true,
                surfaceColor={ surface -> when(marks[surface]) {
                    SurfaceMark.CARIES -> Color(0xFFD64545)
                    SurfaceMark.RESTORATION -> Color(0xFF3C74C9)
                    SurfaceMark.SEALANT -> Color(0xFF62A56A)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }},
                onSurfaceTap={saveSurface(it)}, modifier=Modifier.fillMaxWidth()
            )
            OutlinedButton(onClick={markWholeTooth()},modifier=Modifier.fillMaxWidth()) {
                Text(if(selectedMark==SurfaceMark.HEALTHY) tr(lang,"Limpiar todas las caras","Clear all surfaces") else tr(lang,"Aplicar a todas las caras","Apply to all surfaces"))
            }
            Text(tr(lang,"Estados que afectan al diente completo:","Whole-tooth states:"),fontWeight=FontWeight.Bold)
            listOf(ToothStatus.HEALTHY,ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER,ToothStatus.EXTRACTION_INDICATED).forEach { status ->
                FilterChip(record.status==status && (status!=ToothStatus.HEALTHY || marks.isEmpty()),{setWholeStatus(status)},{Text(statusName(status,lang))},modifier=Modifier.fillMaxWidth())
            }
        } }
        item {
            SectionCard(tr(lang,"Viñetas y lectura según el análisis","Legend by analysis")) {
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    listOf("ODONTO","ICDAS","CPOD","IHOS","IPC").forEach { mode -> FilterChip(legendMode==mode,{legendMode=mode},{Text(mode)},modifier=Modifier.weight(1f)) }
                }
                val text=when(legendMode) {
                    "ICDAS" -> tr(lang,"ICDAS se registra por superficie; el código principal del diente es el mayor código observado entre sus caras.","ICDAS is surface-based; the tooth's main code is the highest code observed among its surfaces.")
                    "CPOD" -> tr(lang,"CPOD/ceod usa el diente como unidad. Si un mismo diente está restaurado y además tiene caries activa, se clasifica como cariado para el índice.","DMFT/dmft uses the tooth as the unit. If a tooth is restored and also has active caries, it is counted as decayed for the index.")
                    "IHOS" -> tr(lang,"IHOS no usa todos los dientes: guía 16V, 11V, 26V, 36L, 31V y 46L, con códigos de detritos y cálculo.","OHI-S uses index surfaces: 16B, 11B, 26B, 36L, 31B and 46L, with debris/calculus codes.")
                    "IPC" -> tr(lang,"IPC se registra por sextantes y conserva únicamente el hallazgo de mayor código del sextante; no es un código por cara dental.","CPI is recorded by sextants and keeps the highest-code finding in the sextant; it is not a tooth-surface code.")
                    else -> tr(lang,"Rojo = caries · azul = restauración · verde = sellador · X/ausencia = diente completo. Las superficies pueden combinarse.","Red = caries · blue = restoration · green = sealant · X/missing = whole tooth. Surface markings can be combined.")
                }
                Text(text)
            }
        }
    }
}

@Composable
fun IcdasScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    var selectedTooth by remember { mutableStateOf(16) }
    var primary by remember { mutableStateOf(false) }
    var selectedSurface by remember { mutableStateOf(Surface.OCCLUSAL) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val surfaceCodes = session.icdasSurfaces[selectedTooth] ?: emptyMap()
    val currentCode = surfaceCodes[selectedSurface] ?: 0
    val toothCode = surfaceCodes.values.maxOrNull() ?: 0
    val allSurfaces = listOf(Surface.VESTIBULAR, Surface.LINGUAL_PALATAL, Surface.MESIAL, Surface.DISTAL, Surface.OCCLUSAL)

    fun saveCodes(codes: Map<Surface, Int>) {
        val all = session.icdasSurfaces.toMutableMap().apply { put(selectedTooth, codes) }
        val max = codes.values.maxOrNull() ?: 0
        val record = session.teeth[selectedTooth] ?: ToothRecord()
        val status = if(max>0) ToothStatus.CARIES else if(record.status==ToothStatus.CARIES) ToothStatus.HEALTHY else record.status
        onSessionChanged(session.copy(icdasSurfaces=all,teeth=session.teeth+(selectedTooth to record.copy(icdas=max,status=status)),presentTeeth=session.presentTeeth+selectedTooth))
    }
    fun setCode(code:Int) { saveCodes(surfaceCodes.toMutableMap().apply{put(selectedSurface,code)}) }
    fun setAll(code:Int) { saveCodes(allSurfaces.associateWith{code}) }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader("ICDAS",onBack,tr(lang,"Puedes asignar códigos diferentes a varias caras. El código global del diente se actualiza automáticamente con el mayor código registrado.","You can assign different codes to multiple surfaces. The overall tooth code automatically becomes the highest recorded code.")) }
        item { Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            FilterChip(!primary,{primary=false},{Text(tr(lang,"Permanentes","Permanent"))}); FilterChip(primary,{primary=true},{Text(tr(lang,"Temporales","Primary"))})
        } }
        item { SectionCard(tr(lang,"Diente y superficies","Tooth and surfaces")) {
            DentalArchSelector(shown,selectedTooth,{selectedTooth=it}){session.icdasSurfaces[it]?.values?.any{c->c>0}==true}
            DentalSurfaceDiagram(centerEnabled=true,surfaceColor={surface->
                val c=surfaceCodes[surface]?:0
                when { surface==selectedSurface->MaterialTheme.colorScheme.primaryContainer;c>=5->MaterialTheme.colorScheme.errorContainer;c>0->MaterialTheme.colorScheme.secondaryContainer;else->MaterialTheme.colorScheme.surfaceVariant }
            },onSurfaceTap={selectedSurface=it},modifier=Modifier.fillMaxWidth())
            Text("${surfaceName(selectedSurface,lang)} · ICDAS $currentCode")
            Text(tr(lang,"Código del diente = $toothCode (mayor código entre sus caras)","Tooth code = $toothCode (highest code among surfaces)"),fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.primary)
        } }
        items(ClinicalContent.icdas.size) { index ->
            val guide=ClinicalContent.icdas[index]
            Card(onClick={setCode(guide.code)},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(currentCode==guide.code)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) {
                Row(Modifier.fillMaxWidth().padding(12.dp),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    Text(guide.code.toString(),style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
                    Text(if(lang=="en")guide.en else guide.es,modifier=Modifier.weight(1f))
                }
            }
        }
        item { Row(horizontalArrangement=Arrangement.spacedBy(8.dp),modifier=Modifier.fillMaxWidth()) {
            OutlinedButton(onClick={setAll(currentCode)},modifier=Modifier.weight(1f)){Text(tr(lang,"Mismo código en todo el diente","Same code on whole tooth"))}
            OutlinedButton(onClick={setAll(0)},modifier=Modifier.weight(1f)){Text(tr(lang,"Limpiar diente","Clear tooth"))}
        } }
        item { NoticeCard(tr(lang,"Para práctica: selecciona una cara, asigna su código y continúa con las demás. Un diente puede tener varias superficies con códigos distintos; el resumen toma el mayor.","For practice: select a surface, assign its code and continue with the others. One tooth may have several different surface codes; the summary uses the highest.")) }
    }
}

@Composable
fun CpodScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    val permanent=ClinicalEngines.cpod(session.teeth,false)
    val primary=ClinicalEngines.cpod(session.teeth,true)
    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"CPOD / ceod","DMFT / dmft"),onBack,tr(lang,"La unidad es el diente completo, no la superficie.","The unit is the whole tooth, not the surface.")) }
        item { SectionCard(tr(lang,"Dentición permanente · CPOD","Permanent dentition · DMFT")) {
            Text("C = ${permanent.carious}   P = ${permanent.missing}   O = ${permanent.filled}",style=MaterialTheme.typography.titleLarge)
            Text("CPOD = ${permanent.total}",fontWeight=FontWeight.Bold,style=MaterialTheme.typography.headlineSmall)
            Text(ClinicalEngines.cpodInterpretation(permanent.total,lang))
        } }
        item { SectionCard(tr(lang,"Dentición temporal · ceod","Primary dentition · dmft")) {
            Text("c = ${primary.carious}   e = ${primary.missing}   o = ${primary.filled}",style=MaterialTheme.typography.titleLarge)
            Text("ceod = ${primary.total}",fontWeight=FontWeight.Bold,style=MaterialTheme.typography.headlineSmall)
            Text(ClinicalEngines.cpodInterpretation(primary.total,lang))
        } }
        item { NoticeCard(tr(lang,"Si un diente tiene una restauración y además caries activa en otra superficie, la caries tiene prioridad para el conteo. Las ausencias por causas distintas de caries no suman como P.","If a tooth has a restoration and active caries on another surface, decay takes priority for the count. Missing teeth for reasons other than caries do not count as M.")) }
    }
}
