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
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.Surface

@Composable
fun OlearyScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    var primary by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    var selectedTooth by remember { mutableStateOf(shown.first()) }
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val surfaces = listOf(Surface.VESTIBULAR, Surface.LINGUAL_PALATAL, Surface.MESIAL, Surface.DISTAL)
    val present = if (session.presentTeeth.intersect(shown.toSet()).isEmpty()) shown.toSet() else session.presentTeeth.intersect(shown.toSet())
    val selectedSurfaces = (session.oleary[selectedTooth] ?: emptySet()).intersect(surfaces.toSet())
    val plaqueFaces = present.sumOf { tooth -> (session.oleary[tooth] ?: emptySet()).count { it in surfaces } }
    val totalFaces = present.size * 4
    val percentage = if (totalFaces == 0) 0.0 else ClinicalEngines.round1(plaqueFaces * 100.0 / totalFaces)

    fun setMarks(newSet: Set<Surface>) {
        val map = session.oleary.toMutableMap().apply { put(selectedTooth, newSet) }
        onSessionChanged(session.copy(oleary = map, presentTeeth = present + selectedTooth))
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader("O’Leary", onBack,
            tr(lang, "Marca una, varias o las cuatro caras. O’Leary usa vestibular, lingual/palatina, mesial y distal; no incluye oclusal.", "Mark one, several or all four surfaces. O’Leary uses buccal, lingual/palatal, mesial and distal; occlusal is not included.")) }
        item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(!primary,{primary=false},{Text(tr(lang,"Permanentes","Permanent"))}); FilterChip(primary,{primary=true},{Text(tr(lang,"Temporales","Primary"))})
        } }
        item { SectionCard(tr(lang,"Arcada y dientes evaluables","Arch and evaluable teeth")) {
            DentalArchSelector(shown,selectedTooth,{selectedTooth=it}) { tooth -> session.oleary[tooth]?.any{it in surfaces}==true }
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                FilterChip(selectedTooth in present,{onSessionChanged(session.copy(presentTeeth=present+selectedTooth))},{Text(tr(lang,"Presente","Present"))})
                FilterChip(selectedTooth !in present,{
                    val p=present-selectedTooth; val map=session.oleary.toMutableMap().apply{remove(selectedTooth)}
                    onSessionChanged(session.copy(presentTeeth=p,oleary=map))
                },{Text(tr(lang,"Ausente / excluir","Missing / exclude"))})
            }
        } }
        item { SectionCard("OD $selectedTooth") {
            if(selectedTooth !in present) Text(tr(lang,"Diente excluido del denominador.","Tooth excluded from denominator.")) else {
                DentalSurfaceDiagram(centerEnabled=false,surfaceColor={s->if(s in selectedSurfaces)Color(0xFFD64545) else MaterialTheme.colorScheme.surfaceVariant},onSurfaceTap={s->
                    if(s in surfaces){val set=selectedSurfaces.toMutableSet();if(!set.add(s))set.remove(s);setMarks(set)}
                },modifier=Modifier.fillMaxWidth())
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp),modifier=Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick={setMarks(surfaces.toSet())},modifier=Modifier.weight(1f)){Text(tr(lang,"Marcar 4 caras","Mark all 4"))}
                    OutlinedButton(onClick={setMarks(emptySet())},modifier=Modifier.weight(1f)){Text(tr(lang,"Limpiar","Clear"))}
                }
                Text(tr(lang,"Rojo = placa dentobacteriana. Puedes marcar tantas caras como correspondan.","Red = plaque. Mark as many surfaces as needed."),color=Color(0xFFD64545),fontWeight=FontWeight.Bold)
            }
        } }
        item { SectionCard(tr(lang,"Cálculo automático","Automatic calculation")) {
            Text("$plaqueFaces / $totalFaces × 100",style=MaterialTheme.typography.titleMedium)
            Text("$percentage %",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
            Text(tr(lang,"Caras evaluables = dientes presentes × 4. Los dientes ausentes quedan fuera del denominador.","Evaluable surfaces = present teeth × 4. Missing teeth are excluded from the denominator."))
        } }
    }
}

@Composable
fun IpcScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    val sextants=listOf("17–14","13–23","24–27","47–44","43–33","34–37")
    val codes=listOf("0","1","2","3","4","X","9")
    val highest=ClinicalEngines.ipcHighest(session.ipcCodes)
    var selected by remember { mutableStateOf(0) }
    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader("IPC / CPI",onBack,tr(lang,"La boca se divide en 6 sextantes. Examina cada zona y registra únicamente el peor hallazgo del sextante.","The mouth is divided into 6 sextants. Examine each area and record only the worst finding in each sextant.")) }
        item { SectionCard(tr(lang,"Mapa de sextantes","Sextant map")) {
            Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                (0..2).forEach{i->SextantTile(i,sextants[i],session.ipcCodes[i],selected==i){selected=i}}
            }
            Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                (5 downTo 3).forEach{i->SextantTile(i,sextants[i],session.ipcCodes[i],selected==i){selected=i}}
            }
        } }
        item { SectionCard("${tr(lang,"Sextante","Sextant")} ${selected+1} · ${sextants[selected]}") {
            codes.chunked(4).forEach { row -> Row(horizontalArrangement=Arrangement.spacedBy(6.dp),modifier=Modifier.fillMaxWidth()) {
                row.forEach { code -> FilterChip(session.ipcCodes[selected]==code,{
                    val list=session.ipcCodes.toMutableList();list[selected]=code;onSessionChanged(session.copy(ipcCodes=list))
                },{Text(code)},modifier=Modifier.weight(1f)) }
                repeat(4-row.size){Text("",modifier=Modifier.weight(1f))}
            } }
            Text(ClinicalEngines.ipcInterpretation(session.ipcCodes[selected],lang),fontWeight=FontWeight.SemiBold)
        } }
        item { SectionCard(tr(lang,"Cómo reconocer los códigos","How to recognize codes")) {
            Text("0 · ${tr(lang,"sano: sin sangrado, cálculo ni bolsa del índice","healthy: no bleeding, calculus or indexed pocket")}")
            Text("1 · ${tr(lang,"sangrado después del sondaje","bleeding after probing")}")
            Text("2 · ${tr(lang,"cálculo/restauración defectuosa con bolsa <4 mm","calculus/defective restoration with pocket <4 mm")}")
            Text("3 · ${tr(lang,"bolsa de 4–5 mm","4–5 mm pocket")}")
            Text("4 · ${tr(lang,"bolsa ≥6 mm","pocket ≥6 mm")}")
            Text("X · ${tr(lang,"sextante excluido: menos de 2 dientes funcionales","excluded sextant: fewer than 2 functional teeth")}")
            Text("9 · ${tr(lang,"no registrable","not recordable")}")
        } }
        item { Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(16.dp)) { Text(tr(lang,"Código más alto","Highest code"),fontWeight=FontWeight.Bold);Text(highest,style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold);Text(ClinicalEngines.ipcInterpretation(highest,lang)) }
        } }
    }
}

@Composable
private fun RowScope.SextantTile(index:Int,label:String,code:String,active:Boolean,onClick:()->Unit) {
    Card(modifier=Modifier.weight(1f),onClick=onClick,colors=CardDefaults.cardColors(containerColor=if(active)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxWidth().padding(9.dp),horizontalAlignment=Alignment.CenterHorizontally) {
            Text(label,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.bodySmall)
            Text(code,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
        }
    }
}

@Composable
fun IhosScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    val teeth=listOf(16,11,26,36,31,46)
    val result=ClinicalEngines.ihos(session)
    var selected by remember { mutableStateOf(16) }
    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader("IHOS / OHI-S",onBack,tr(lang,"Usa seis superficies índice: 16V, 11V, 26V, 36L, 31V y 46L. Selecciona el diente para codificar detritos y cálculo.","Use six index surfaces: 16B, 11B, 26B, 36L, 31B and 46L. Select a tooth to code debris and calculus.")) }
        item { SectionCard(tr(lang,"Dientes y superficies índice","Index teeth and surfaces")) {
            Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                teeth.take(3).forEach{tooth->IhosToothChip(tooth,selected==tooth,tooth in listOf(36,46),{selected=tooth},Modifier.weight(1f))}
            }
            Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                teeth.drop(3).forEach{tooth->IhosToothChip(tooth,selected==tooth,tooth in listOf(36,46),{selected=tooth},Modifier.weight(1f))}
            }
        } }
        item { SectionCard("OD $selected · ${if(selected in listOf(36,46)) tr(lang,"Lingual","Lingual") else tr(lang,"Vestibular","Buccal")}") {
            Text(tr(lang,"Detritos / placa","Debris"),fontWeight=FontWeight.Bold)
            Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {(0..3).forEach{c->FilterChip((session.ihosDebris[selected]?:0)==c,{onSessionChanged(session.copy(ihosDebris=session.ihosDebris+(selected to c)))},{Text(c.toString())},modifier=Modifier.weight(1f))}}
            Text(tr(lang,"Cálculo","Calculus"),fontWeight=FontWeight.Bold)
            Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {(0..3).forEach{c->FilterChip((session.ihosCalculus[selected]?:0)==c,{onSessionChanged(session.copy(ihosCalculus=session.ihosCalculus+(selected to c)))},{Text(c.toString())},modifier=Modifier.weight(1f))}}
        } }
        item { SectionCard(tr(lang,"Guía de códigos","Code guide")) {
            Text(tr(lang,"Detritos: 0 ninguno · 1 hasta 1/3 · 2 >1/3 hasta 2/3 · 3 >2/3 de la superficie.","Debris: 0 none · 1 up to 1/3 · 2 >1/3 to 2/3 · 3 >2/3 of surface."))
            Text(tr(lang,"Cálculo: 0 ninguno · 1 supragingival hasta 1/3 · 2 >1/3 hasta 2/3 o pequeños depósitos subgingivales · 3 >2/3 o banda subgingival continua.","Calculus: 0 none · 1 supragingival up to 1/3 · 2 >1/3 to 2/3 or small subgingival deposits · 3 >2/3 or continuous subgingival band."))
        } }
        item { Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                Text(tr(lang,"Resultado IHOS","OHI-S result"),fontWeight=FontWeight.Bold)
                Text("${"%.2f".format(result)}",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
                Text(ClinicalEngines.ihosInterpretation(result,lang))
                Text(tr(lang,"Referencia del material: 0.0–1.2 buena · 1.3–3.0 regular · 3.1–6.0 deficiente.","Teaching-material reference: 0.0–1.2 good · 1.3–3.0 fair · 3.1–6.0 poor."))
            }
        } }
    }
}

@Composable
private fun IhosToothChip(tooth:Int,selected:Boolean,lingual:Boolean,onClick:()->Unit,modifier:Modifier=Modifier) {
    Card(modifier=modifier,onClick=onClick,colors=CardDefaults.cardColors(containerColor=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxWidth().padding(8.dp),horizontalAlignment=Alignment.CenterHorizontally) {
            Text("🦷",textAlign=TextAlign.Center);Text("$tooth ${if(lingual)"L" else "V"}",fontWeight=FontWeight.Bold)
        }
    }
}
