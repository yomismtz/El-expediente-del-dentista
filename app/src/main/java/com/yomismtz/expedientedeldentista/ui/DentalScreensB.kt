package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

@Composable
fun OlearyScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    var primary by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    val shownSet = shown.toSet()
    var selectedTooth by remember { mutableStateOf(shown.first()) }
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val surfaces = listOf(Surface.VESTIBULAR, Surface.LINGUAL_PALATAL, Surface.MESIAL, Surface.DISTAL)
    val initialized = if (primary) session.olearyPrimaryInitialized else session.olearyPermanentInitialized
    val odontogramPresent = shownSet.filter { tooth ->
        session.teeth[tooth]?.status !in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER)
    }.toSet()
    val present = if (initialized) session.olearyPresentTeeth.intersect(shownSet) else odontogramPresent
    val otherDentitionPresent = session.olearyPresentTeeth - shownSet
    val selectedSurfaces = (session.oleary[selectedTooth] ?: emptySet()).intersect(surfaces.toSet())
    val plaqueFaces = present.sumOf { tooth -> (session.oleary[tooth] ?: emptySet()).count { it in surfaces } }
    val totalFaces = present.size * 4
    val percentage = if (totalFaces == 0) 0.0 else ClinicalEngines.round1(plaqueFaces * 100.0 / totalFaces)

    fun saveState(currentDentitionPresent: Set<Int>, map: Map<Int, Set<Surface>> = session.oleary) {
        onSessionChanged(
            session.copy(
                oleary = map,
                olearyPresentTeeth = otherDentitionPresent + currentDentitionPresent,
                olearyPermanentInitialized = session.olearyPermanentInitialized || !primary,
                olearyPrimaryInitialized = session.olearyPrimaryInitialized || primary
            )
        )
    }

    fun setMarks(newSet: Set<Surface>) {
        val map = session.oleary.toMutableMap().apply { put(selectedTooth, newSet) }
        saveState(present + selectedTooth, map)
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
                FilterChip(selectedTooth in present,{saveState(present+selectedTooth)},{Text(tr(lang,"Presente","Present"))})
                FilterChip(selectedTooth !in present,{
                    val p=present-selectedTooth; val map=session.oleary.toMutableMap().apply{remove(selectedTooth)}
                    saveState(p,map)
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
    val sextants = listOf("17–14", "13–23", "24–27", "47–44", "43–33", "34–37")
    val codes = listOf("0", "1", "2", "3", "4", "X")
    val highest = ClinicalEngines.ipcHighest(session.ipcCodes)
    val summary = session.ipcCodes.take(6).mapIndexed { index, code -> "S${index + 1}=$code" }.joinToString(" · ")
    var selected by remember { mutableStateOf(0) }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "IPC / CPI",
                onBack,
                tr(lang,
                    "El IPC se registra por sextantes. Toca uno de los seis sectores, examina sus dientes y conserva el peor hallazgo de ese sextante.",
                    "CPI is recorded by sextants. Tap one of the six areas, examine its teeth and retain the worst finding in that sextant.")
            )
        }
        item {
            SectionCard(tr(lang, "Guía visual IPC", "CPI visual guide")) {
                Image(
                    painter = painterResource(R.drawable.ipc_visual_guide),
                    contentDescription = tr(lang, "Infografías de sextantes, códigos y sonda OMS del IPC", "CPI sextant, code and WHO probe infographics"),
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Text(tr(lang,
                    "La lámina resume: mapa de sextantes, códigos 0–4/X, sonda OMS/CPI y secuencia de registro. Debajo puedes practicar el índice de forma interactiva.",
                    "The guide summarizes the sextant map, codes 0–4/X, WHO/CPI probe and recording sequence. Practice interactively below."))
            }
        }
        item {
            SectionCard(tr(lang, "Mapa interactivo por sextantes", "Interactive sextant map")) {
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    (0..2).forEach { i -> SextantTile(i,sextants[i],session.ipcCodes.getOrElse(i){"0"},selected==i){selected=i} }
                }
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    (5 downTo 3).forEach { i -> SextantTile(i,sextants[i],session.ipcCodes.getOrElse(i){"0"},selected==i){selected=i} }
                }
            }
        }
        item {
            SectionCard("${tr(lang,"Sextante","Sextant")} ${selected+1} · ${sextants[selected]}") {
                Text(tr(lang,"Selecciona el código que corresponda al peor hallazgo observado en el sextante.","Select the code corresponding to the worst finding observed in the sextant."))
                codes.chunked(3).forEach { row ->
                    Row(horizontalArrangement=Arrangement.spacedBy(6.dp),modifier=Modifier.fillMaxWidth()) {
                        row.forEach { code ->
                            FilterChip(
                                session.ipcCodes.getOrElse(selected){"0"} == code,
                                {
                                    val list = session.ipcCodes.toMutableList()
                                    while (list.size < 6) list.add("0")
                                    list[selected] = code
                                    onSessionChanged(session.copy(ipcCodes=list))
                                },
                                { Text(code) },
                                modifier=Modifier.weight(1f)
                            )
                        }
                    }
                }
                Text(ClinicalEngines.ipcInterpretation(session.ipcCodes.getOrElse(selected){"0"},lang),fontWeight=FontWeight.SemiBold)
            }
        }
        item {
            SectionCard(tr(lang,"Cómo reconocer los códigos","How to recognize codes")) {
                Text("0 · ${tr(lang,"sano: sin los hallazgos codificados del índice","healthy: no indexed findings")}")
                Text("1 · ${tr(lang,"sangrado después del sondaje","bleeding after probing")}")
                Text("2 · ${tr(lang,"cálculo y/o factores retentivos de placa","calculus and/or plaque-retentive factors")}")
                Text("3 · ${tr(lang,"bolsa de 4–5 mm","4–5 mm pocket")}")
                Text("4 · ${tr(lang,"bolsa de 6 mm o más","pocket 6 mm or greater")}")
                Text("X · ${tr(lang,"sextante excluido según los criterios del método","sextant excluded according to method criteria")}")
            }
        }
        item {
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(tr(lang,"Resumen para practicar la redacción","Summary for writing practice"),fontWeight=FontWeight.Bold)
                    Text(summary, style=MaterialTheme.typography.titleMedium, fontWeight=FontWeight.Bold)
                    Text("${tr(lang,"Código más alto","Highest code")}: $highest")
                    Text(ClinicalEngines.ipcInterpretation(highest,lang))
                    Text(tr(lang,
                        "Ejemplo: “IPC: $summary”. Después agrega en palabras los hallazgos relevantes y complétalos con la valoración periodontal cuando corresponda.",
                        "Example: “CPI: $summary”. Then describe relevant findings and complete them with periodontal assessment as appropriate."))
                }
            }
        }
    }
}

@Composable
private fun RowScope.SextantTile(index:Int,label:String,code:String,active:Boolean,onClick:()->Unit) {
    Card(modifier=Modifier.weight(1f),onClick=onClick,colors=CardDefaults.cardColors(containerColor=if(active)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxWidth().padding(9.dp),horizontalAlignment=Alignment.CenterHorizontally) {
            Text("S${index + 1}", fontWeight=FontWeight.Bold, color=MaterialTheme.colorScheme.primary)
            Text(label,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.bodySmall)
            Text(code,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
        }
    }
}

@Composable
fun IhosScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    val teeth = listOf(16,11,26,36,31,46)
    val debrisAverage = teeth.map { session.ihosDebris[it] ?: 0 }.average()
    val calculusAverage = teeth.map { session.ihosCalculus[it] ?: 0 }.average()
    val result = ClinicalEngines.ihos(session)
    var selected by remember { mutableStateOf(16) }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "IHOS / OHI-S",
                onBack,
                tr(lang,
                    "Usa seis superficies índice: 16V, 11V, 26V, 36L, 31V y 46L. Toca cada diente, asigna detritos y cálculo de 0 a 3 y observa el cálculo automático.",
                    "Use six index surfaces: 16B, 11B, 26B, 36L, 31B and 46L. Tap each tooth, assign debris and calculus from 0 to 3 and view the automatic calculation.")
            )
        }
        item {
            SectionCard(tr(lang,"Guía visual IHOS","OHI-S visual guide")) {
                Image(
                    painter = painterResource(R.drawable.ihos_visual_guide),
                    contentDescription = tr(lang,"Dientes índice, escalas de detritos y cálculo y guía de cálculo del IHOS","Index teeth, debris/calculus scales and OHI-S calculation guide"),
                    modifier=Modifier.fillMaxWidth(),
                    contentScale=ContentScale.FillWidth
                )
                Text(tr(lang,
                    "La guía reúne los seis dientes índice, la escala de detritos 0–3, la escala de cálculo 0–3 y la fórmula. La captura de práctica sigue siendo interactiva debajo.",
                    "The guide combines the six index teeth, debris scale 0–3, calculus scale 0–3 and formula. Practice entry remains interactive below."))
            }
        }
        item {
            SectionCard(tr(lang,"Dientes y superficies índice","Index teeth and surfaces")) {
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    teeth.take(3).forEach { tooth -> IhosToothChip(tooth,selected==tooth,tooth in listOf(36,46),{selected=tooth},Modifier.weight(1f)) }
                }
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    teeth.drop(3).forEach { tooth -> IhosToothChip(tooth,selected==tooth,tooth in listOf(36,46),{selected=tooth},Modifier.weight(1f)) }
                }
            }
        }
        item {
            SectionCard("OD $selected · ${if(selected in listOf(36,46)) tr(lang,"Lingual","Lingual") else tr(lang,"Vestibular","Buccal")}") {
                Text(tr(lang,"Detritos / placa blanda","Debris / soft deposits"),fontWeight=FontWeight.Bold)
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    (0..3).forEach { c ->
                        FilterChip(
                            (session.ihosDebris[selected] ?: 0) == c,
                            { onSessionChanged(session.copy(ihosDebris=session.ihosDebris+(selected to c))) },
                            { Text(c.toString()) },
                            modifier=Modifier.weight(1f)
                        )
                    }
                }
                Text(tr(lang,"Cálculo dental","Dental calculus"),fontWeight=FontWeight.Bold)
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    (0..3).forEach { c ->
                        FilterChip(
                            (session.ihosCalculus[selected] ?: 0) == c,
                            { onSessionChanged(session.copy(ihosCalculus=session.ihosCalculus+(selected to c))) },
                            { Text(c.toString()) },
                            modifier=Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang,"Guía rápida de códigos","Quick code guide")) {
                Text(tr(lang,"Detritos: 0 ninguno · 1 <1/3 · 2 de 1/3 a 2/3 · 3 >2/3 de la superficie.","Debris: 0 none · 1 <1/3 · 2 from 1/3 to 2/3 · 3 >2/3 of surface."))
                Text(tr(lang,"Cálculo: 0 ninguno · 1 supragingival <1/3 · 2 de 1/3 a 2/3 y/o presencia ligera subgingival · 3 >2/3 y/o banda subgingival importante.","Calculus: 0 none · 1 supragingival <1/3 · 2 from 1/3 to 2/3 and/or light subgingival presence · 3 >2/3 and/or important subgingival band."))
            }
        }
        item {
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                    Text(tr(lang,"Cálculo automático","Automatic calculation"),fontWeight=FontWeight.Bold)
                    Text("ID-S = ${"%.2f".format(debrisAverage)}")
                    Text("IC-S = ${"%.2f".format(calculusAverage)}")
                    Text("IHOS = ${"%.2f".format(result)}",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
                    Text(ClinicalEngines.ihosInterpretation(result,lang))
                    Text(tr(lang,"Referencia didáctica: 0.0–1.2 buena · 1.3–3.0 regular · 3.1–6.0 deficiente.","Teaching reference: 0.0–1.2 good · 1.3–3.0 fair · 3.1–6.0 poor."))
                    Text(tr(lang,
                        "Ejemplo de redacción: “IHOS ${"%.2f".format(result)} (ID-S ${"%.2f".format(debrisAverage)} + IC-S ${"%.2f".format(calculusAverage)}), ${ClinicalEngines.ihosInterpretation(result,lang)}”.",
                        "Writing example: “OHI-S ${"%.2f".format(result)} (DI-S ${"%.2f".format(debrisAverage)} + CI-S ${"%.2f".format(calculusAverage)}), ${ClinicalEngines.ihosInterpretation(result,lang)}”."))
                }
            }
        }
    }
}

@Composable
private fun IhosToothChip(tooth:Int,selected:Boolean,lingual:Boolean,onClick:()->Unit,modifier:Modifier=Modifier) {
    Card(modifier=modifier,onClick=onClick,colors=CardDefaults.cardColors(containerColor=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxWidth().padding(8.dp),horizontalAlignment=Alignment.CenterHorizontally) {
            Text("🦷",textAlign=TextAlign.Center)
            Text("$tooth ${if(lingual)"L" else "V"}",fontWeight=FontWeight.Bold)
        }
    }
}
