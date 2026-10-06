package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.IcdasCoding
import com.yomismtz.expedientedeldentista.clinical.IcdasSurfaceRecord
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
                    "ICDAS" -> tr(lang,"ICDAS II se registra por superficie con dos dígitos: restauración/sellante + caries. Los códigos especiales 96–99 se interpretan según la condición registrada.","ICDAS is surface-based; the tooth's main code is the highest code observed among its surfaces.")
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
    var showIcdasHelp by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val records = session.icdasSurfaceRecords[selectedTooth] ?: emptyMap()
    val allSurfaces = listOf(Surface.VESTIBULAR, Surface.LINGUAL_PALATAL, Surface.MESIAL, Surface.DISTAL, Surface.OCCLUSAL)
    val current = records[selectedSurface] ?: IcdasSurfaceRecord(restorationCode = 0, cariesCode = 0)
    val currentCode = current.combinedCode
    val restorationKnown = current.restorationCode != null
    val currentRestoration = current.restorationCode ?: 0
    val currentCaries = current.cariesCode ?: 0
    val toothSpecial = records.values.firstOrNull { it.specialCode != null }?.specialCode
    val toothCode = toothSpecial ?: (records.values.maxOfOrNull { it.combinedCode } ?: 0)

    fun saveCodes(codes: Map<Surface, IcdasSurfaceRecord>, statusOverride: ToothStatus? = null, presentOverride: Boolean? = null) {
        val all = session.icdasSurfaceRecords.toMutableMap().apply { put(selectedTooth, codes) }
        val legacy = session.icdasSurfaces.toMutableMap().apply { put(selectedTooth, codes.mapValues { it.value.combinedCode }) }
        val record = session.teeth[selectedTooth] ?: ToothRecord()
        // ICDAS is a surface-level detection system. Do not overwrite the
        // epidemiological CPOD/ceod tooth status from an ICDAS lesion/restoration.
        // Missing/special tooth states are the exception because they define tooth presence.
        val status = statusOverride ?: when {
            codes.values.any { it.specialCode in setOf(91, 93, 97) } -> ToothStatus.MISSING_CARIES
            codes.values.any { it.specialCode in setOf(90, 92, 98, 99) } -> ToothStatus.MISSING_OTHER
            else -> record.status
        }
        val present = session.presentTeeth.toMutableSet()
        if (presentOverride == false || status == ToothStatus.MISSING_CARIES || status == ToothStatus.MISSING_OTHER) present.remove(selectedTooth)
        else if (presentOverride == true) present.add(selectedTooth)
        onSessionChanged(session.copy(
            icdasSurfaceRecords = all,
            icdasSurfaces = legacy,
            teeth = session.teeth + (selectedTooth to record.copy(
                icdas = codes.values.maxOfOrNull { it.combinedCode } ?: 0,
                icdasLegacyPending = codes.values.any { it.legacyPending },
                status = status
            )),
            presentTeeth = present
        ))
    }
    fun setTwoDigit(restoration: Int, caries: Int) {
        if (!IcdasCoding.isValidRestoration(restoration) || !IcdasCoding.isValidCaries(caries)) return
        saveCodes(records.toMutableMap().apply { put(selectedSurface, IcdasSurfaceRecord(restorationCode = restoration, cariesCode = caries)) }, presentOverride = true)
    }

    fun setSpecial(code: Int) {
        val codes = if (code == 96) records.toMutableMap().apply { put(selectedSurface, IcdasSurfaceRecord(specialCode = code)) } else allSurfaces.associateWith { IcdasSurfaceRecord(specialCode = code) }
        val status = when (code) {
            91, 93, 97 -> ToothStatus.MISSING_CARIES
            90, 92, 98, 99 -> ToothStatus.MISSING_OTHER
            else -> session.teeth[selectedTooth]?.status
        }
        saveCodes(codes, statusOverride = status, presentOverride = if (code >= 97 || code in 90..93) false else true)
    }

    fun setAll(code: Int) {
        val record = IcdasCoding.fromCombined(code) ?: return
        saveCodes(allSurfaces.associateWith { record }, presentOverride = true)
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader("ICDAS", onBack, tr(lang,
            "ICDAS II de dos dígitos: primer dígito = restauración/sellante; segundo dígito = caries 0–6. Se registra por superficie.",
            "Two-digit ICDAS II: first digit = restoration/sealant; second digit = caries 0–6. Recorded by surface.")) }
        item { OutlinedButton(onClick={showIcdasHelp=true},modifier=Modifier.fillMaxWidth()){
            Text(tr(lang,"ⓘ Ayuda ICDAS II · codificación de dos dígitos","ⓘ ICDAS II help · two-digit coding"))
        } }
        item { SectionCard(tr(lang,"1 · Dentición","1 · Dentition")) { Row(horizontalArrangement=Arrangement.spacedBy(8.dp),modifier=Modifier.fillMaxWidth()) {
            FilterChip(!primary,{primary=false},{Text(tr(lang,"Permanente","Permanent"))},modifier=Modifier.weight(1f))
            FilterChip(primary,{primary=true},{Text(tr(lang,"Temporal","Primary"))},modifier=Modifier.weight(1f))
        } } }
        item { SectionCard(tr(lang,"2 · Diente y superficie","2 · Tooth and surface")) {
            DentalArchSelector(shown,selectedTooth,{selectedTooth=it}){session.icdasSurfaceRecords[it]?.values?.any{r->r.combinedCode>0}==true}
            DentalSurfaceDiagram(centerEnabled=true,surfaceColor={surface->
                val c=session.icdasSurfaceRecords[selectedTooth]?.get(surface)?.combinedCode?:0
                when {
                    surface==selectedSurface -> MaterialTheme.colorScheme.primaryContainer
                    session.icdasSurfaceRecords[selectedTooth]?.get(surface)?.specialCode in setOf(96,97,98,99) -> MaterialTheme.colorScheme.errorContainer
                    (if(c>=10)c%10 else c)>=5 -> MaterialTheme.colorScheme.errorContainer
                    c>0 -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            },onSurfaceTap={selectedSurface=it},modifier=Modifier.fillMaxWidth())
            Text("${surfaceName(selectedSurface,lang)} · ICDAS ${"%02d".format(currentCode)}")
            Text(tr(lang,if(restorationKnown) "Código actual = ${"%02d".format(currentCode)} · restauración $currentRestoration · caries $currentCaries" else "Código actual = ${"%02d".format(currentCode)} · restauración pendiente · caries $currentCaries",
                if(restorationKnown) "Current code = ${"%02d".format(currentCode)} · restoration $currentRestoration · caries $currentCaries" else "Current code = ${"%02d".format(currentCode)} · restoration pending · caries $currentCaries"),
                fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.primary)
            if (toothSpecial != null) Text(tr(lang,"Código especial del diente: $toothSpecial","Special tooth code: $toothSpecial"),fontWeight=FontWeight.Bold)
            if (current.legacyPending) Text(tr(lang,"⚠️ Registro antiguo: se conservó el código de caries, pero falta confirmar restauración/sellante. No se inventó ese dato.","⚠️ Legacy record: the caries code was preserved, but restoration/sealant status still needs confirmation. No value was invented."),style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.error)
        } }
        item { SectionCard(tr(lang,"3 · Primer dígito: restauración / sellante","3 · First digit: restoration / sealant")) {
            ClinicalContent.icdasRestorations.forEach { guide ->
                val active = restorationKnown && current.specialCode == null && currentRestoration == guide.code
                Card(onClick={setTwoDigit(guide.code,currentCaries)},modifier=Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(containerColor=if(active)MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.fillMaxWidth().padding(12.dp),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                        Text((if(active)"✓ " else "")+guide.code.toString(),style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold,
                            color=if(active)MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface)
                        Text(if(lang=="en")guide.en else guide.es,modifier=Modifier.weight(1f),
                            color=if(active)MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        } }
        item { SectionCard(tr(lang,"4 · Segundo dígito: estado de caries 0–6","4 · Second digit: caries status 0–6")) {
            ClinicalContent.icdas.forEach { guide ->
                val active = currentCaries == guide.code
                Card(onClick={setTwoDigit(currentRestoration,guide.code)},modifier=Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(containerColor=if(active)MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.fillMaxWidth().padding(12.dp),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                        Text((if(active)"✓ " else "")+guide.code.toString(),style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold,
                            color=if(active)MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface)
                        Text(if(lang=="en")guide.en else guide.es,modifier=Modifier.weight(1f),
                            color=if(active)MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface)
                    }
                }
                if(active) {
                    val icdasImage=when(guide.code){0->R.drawable.icdas_uploaded_0;1->R.drawable.icdas_uploaded_1;2->R.drawable.icdas_uploaded_2;3->R.drawable.icdas_uploaded_3;4->R.drawable.icdas_uploaded_4;5->R.drawable.icdas_uploaded_5;else->R.drawable.icdas_uploaded_6}
                    LocalClinicalInlineZoomImageV48(lang,tr(lang,"ICDAS ${guide.code} · imagen clínica","ICDAS ${guide.code} · clinical image"),tr(lang,"ICDAS ${guide.code} · imagen clínica","ICDAS ${guide.code} · clinical image"),icdasImage,
                        tr(lang,"Ejemplo clínico correspondiente al segundo dígito. Correlaciona la imagen con los criterios escritos antes de registrar la superficie.","Clinical example corresponding to the second digit. Correlate the image with the written criteria before recording the surface."),
                        tr(lang,"Ejemplo clínico correspondiente al segundo dígito. Correlaciona la imagen con los criterios escritos antes de registrar la superficie.","Clinical example corresponding to the second digit. Correlate the image with the written criteria before recording the surface."))
                }
            }
        } }
        item { SectionCard(tr(lang,"5 · Códigos especiales de diente / superficie","5 · Special tooth / surface codes")) {
            Text(tr(lang,"La bibliografía ICDAS distingue 90–93 para implante/póntico, 96 para superficie excluida y 97–99 para ausencia/no erupción. 94 y 95 no están definidos en la codificación estándar consultada.",
                "ICDAS references use 90–93 for implant/pontic, 96 for excluded surface and 97–99 for missing/unerupted teeth. 94 and 95 are not defined in the standard coding consulted."))
            ClinicalContent.icdasSpecial.forEach { guide ->
                Card(onClick={setSpecial(guide.code)},modifier=Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(containerColor=if(toothCode==guide.code)MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.fillMaxWidth().padding(12.dp),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                        Text((if(toothCode==guide.code)"✓ " else "")+guide.code.toString(),style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold,
                            color=if(toothCode==guide.code)MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface)
                        Text(if(lang=="en")guide.en else guide.es,modifier=Modifier.weight(1f),
                            color=if(toothCode==guide.code)MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        } }
        item { SectionCard(tr(lang,"6 · Acciones","6 · Actions")) { Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick={setAll(currentCode)},modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Aplicar ${"%02d".format(currentCode)} a todas las superficies","Apply ${"%02d".format(currentCode)} to all surfaces"))}
            OutlinedButton(onClick={setAll(0)},modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Limpiar códigos ICDAS del diente","Clear ICDAS codes from tooth"))}
            Text(tr(lang,"Usa «aplicar a todas» sólo cuando todas las superficies examinadas cumplen el mismo criterio.",
                "Use “apply to all” only when every examined surface meets the same criterion."),style=MaterialTheme.typography.bodySmall)
        } } }
        item { NoticeCard(tr(lang,
            "Ejemplo: restauración de amalgama + cavidad extensa = 46. Diente sin restauración + lesión inicial = 01. Diente extraído por caries = 97. Un diente ausente por otra razón = 98.",
            "Example: amalgam restoration + extensive cavity = 46. Unrestored tooth + initial lesion = 01. Tooth missing due to caries = 97. Tooth missing for another reason = 98.")) }
    }
    if(showIcdasHelp){
        AlertDialog(onDismissRequest={showIcdasHelp=false},confirmButton={OutlinedButton(onClick={showIcdasHelp=false}){Text(tr(lang,"Cerrar","Close"))}},
            title={Text(tr(lang,"ⓘ ICDAS II · dos dígitos","ⓘ ICDAS II · two digits"))},text={
            Column(verticalArrangement=Arrangement.spacedBy(8.dp)){
                Text(tr(lang,"Primero identifica restauración/sellante y después determina el código de caries de la superficie. Ambos se combinan en un código de dos dígitos.",
                    "First identify restoration/sealant status and then determine the surface caries code. Combine both into a two-digit code."))
                Text(tr(lang,"00 = sano sin restauración; 03 = superficie no restaurada con pérdida localizada de esmalte; 46 = amalgama + cavidad extensa.",
                    "00 = sound/unrestored; 03 = unrestored surface with localized enamel breakdown; 46 = amalgam + extensive cavity."))
                Text(tr(lang,"Especiales: 90–93 implant/póntico; 96 superficie excluida; 97 caries; 98 otras causas; 99 no erupcionado. 94–95 no se asignan.",
                    "Special: 90–93 implant/pontic; 96 excluded surface; 97 caries; 98 other causes; 99 unerupted. 94–95 are not assigned."))
                ClinicalContent.icdas.forEach { g -> Text("${g.code} · ${if(lang=="en")g.en else g.es}",fontWeight=if(g.code==currentCaries)FontWeight.Bold else FontWeight.Normal) }
            }
        })
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
