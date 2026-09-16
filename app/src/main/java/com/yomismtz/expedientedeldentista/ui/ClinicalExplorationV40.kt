package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PhysicalExamHubV40Screen(lang: String, onVitals: () -> Unit, onBack: () -> Unit) {
    var showGeneral by remember { mutableStateOf(false) }
    if (showGeneral) { GeneralInspectionV40Screen(lang) { showGeneral = false }; return }
    ResponsiveScreenV17("Exploración física", "Dos rutas: signos vitales/glucosa e inspección general.", onBack) { _ ->
        Card(onClick = onVitals, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(16.dp)) {
                Text("❤️ Signos vitales y glucosa capilar", fontWeight = FontWeight.Black)
                Text("Temperatura · frecuencia cardiaca · frecuencia respiratoria · tensión arterial · glucosa capilar.")
            }
        }
        Card(onClick = { showGeneral = true }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Column(Modifier.padding(16.dp)) {
                Text("👁 Inspección general", fontWeight = FontWeight.Black)
                Text("Marcha, postura al sentarse, cooperación, desarrollo observado, juego y autocuidado.")
            }
        }
    }
}

private data class InspectionV40(val title: String, val options: List<Pair<String,String>>)
private val inspectionGroupsV40 = listOf(
    InspectionV40("Marcha", listOf("Estable y coordinada" to "Paso simétrico y equilibrio funcional.", "Cautelosa/lenta" to "Puede relacionarse con dolor, miedo, debilidad o edad.", "Claudicante" to "Apoyo asimétrico; investigar dolor o lesión.", "Inestable" to "Requiere contextualizar causas neurológicas, vestibulares, farmacológicas u otras.")),
    InspectionV40("Forma de sentarse y postura", listOf("Erguida y relajada" to "Postura funcional sin tensión evidente.", "Defensiva/encogida" to "Puede reflejar dolor o ansiedad.", "Inquieta" to "Movimientos frecuentes; interpretar según edad y contexto.", "Necesita apoyo" to "Puede acompañar limitación motora, fatiga o enfermedad.")),
    InspectionV40("Cooperación", listOf("Cooperador" to "Acepta interacción acorde a su edad.", "Cooperador con reservas" to "Requiere explicación, pausas o adaptación.", "Potencialmente cooperador" to "Puede progresar con guía de conducta.", "No cooperador en este momento" to "No es una etiqueta permanente; investigar miedo, dolor, desarrollo y comunicación.")),
    InspectionV40("Edad cronológica vs desarrollo observado", listOf("Acorde a edad" to "Comprensión y conducta aproximadamente compatibles.", "Comprensión menor a la esperada" to "Adaptar lenguaje; no asignar edad mental sin evaluación formal.", "Comprensión mayor a la esperada" to "Puede tolerar explicaciones más complejas.", "No valorable" to "Evitar inferencias cuando no hay interacción suficiente.")),
    InspectionV40("Juego / conducta pediátrica", listOf("Juego espontáneo" to "Explora e interactúa acorde al contexto.", "Juego paralelo" to "Frecuente en etapas tempranas.", "Juego cooperativo" to "Interacción compartida con reglas u objetivos.", "Evita jugar/interactuar" to "Puede relacionarse con ansiedad, dolor, cansancio o desarrollo.")),
    InspectionV40("Aspecto y autocuidado", listOf("Aseo acorde al contexto" to "Describir sin juicios.", "Signos de descuido" to "Explorar acceso, dependencia, enfermedad o barreras sociales.", "Vestimenta inadecuada al clima" to "Dato contextual que requiere cautela.", "No valorable" to "No forzar conclusiones."))
)

@Composable
fun GeneralInspectionV40Screen(lang: String, onBack: () -> Unit) {
    val selections = remember { mutableStateListOf<String>() }
    ResponsiveScreenV17("Inspección general", "Observa antes de tocar: movilidad, postura, interacción, cooperación y aspecto.", onBack) { profile ->
        inspectionGroupsV40.forEach { group ->
            ResponsiveSectionV17(group.title) {
                AdaptiveGridV17(group.options.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                    val (label, detail) = group.options[i]
                    val key = "${group.title}:$label"
                    Card(onClick = { selections.removeAll { it.startsWith("${group.title}:") }; selections.add(key) }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (key in selections) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
                        Column(Modifier.padding(10.dp)) { Text(label, fontWeight = FontWeight.Black); if (key in selections) Text(detail) }
                    }
                }
            }
        }
        NoticeCard("La conducta varía con edad, desarrollo, dolor, ansiedad y experiencias previas. La inspección orienta comunicación; no sustituye evaluación psicológica o del neurodesarrollo.")
    }
}

private data class RocabadoV40(val n: Int, val name: String, val detail: String)
private val rocabadoPointsV40 = listOf(
    RocabadoV40(1,"Sinovial anteroinferior","Región anterior e inferior del compartimento articular."),
    RocabadoV40(2,"Sinovial anterosuperior","Región anterior y superior del compartimento articular."),
    RocabadoV40(3,"Ligamento colateral lateral","Tejidos laterales relacionados con el complejo disco-condilar."),
    RocabadoV40(4,"Ligamento temporomandibular","Región del ligamento lateral/temporomandibular."),
    RocabadoV40(5,"Sinovial posteroinferior","Región posterior e inferior del compartimento articular."),
    RocabadoV40(6,"Sinovial posterosuperior","Región posterior y superior del compartimento articular."),
    RocabadoV40(7,"Ligamento posterior","Estructuras posteriores articulares."),
    RocabadoV40(8,"Región retrodiscal","Tejidos retrodiscales; dolor debe integrarse con historia, ruidos y movimientos.")
)

@Composable
fun TmjTeachingV40Screen(lang: String, onBack: () -> Unit) {
    val painful = remember { mutableStateListOf<Int>() }
    val findings = remember { mutableStateListOf<String>() }
    var opening by remember { mutableStateOf("40–50 mm aprox.") }
    val checklist = listOf("Dolor al abrir/cerrar","Chasquido","Crepitación","Desviación","Deflexión","Bloqueo","Limitación funcional","Dolor muscular","Bruxismo referido","Sin alteraciones aparentes")
    ResponsiveScreenV17("Exploración de ATM y TTM", "Anatomía, mapa de dolor de Rocabado, movimientos, ruidos y resumen.", onBack) { profile ->
        ResponsiveSectionV17("Anatomía de la ATM") { TmjCanvasV40(painful); Text("Cóndilo mandibular · temporal/fosa y eminencia · disco · cápsula · ligamentos · tejidos retrodiscales.") }
        ResponsiveSectionV17("Mapa de dolor de Rocabado · 8 zonas") {
            AdaptiveGridV17(rocabadoPointsV40.size, if (profile.largeSystemText) 1 else 2) { i ->
                val p = rocabadoPointsV40[i]
                Card(onClick = { if (p.n in painful) painful.remove(p.n) else painful.add(p.n) }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (p.n in painful) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(Modifier.padding(10.dp)) { Text("${p.n}. ${p.name}", fontWeight = FontWeight.Black); if (p.n in painful) Text("Dolor marcado · ${p.detail}") }
                }
            }
            NoticeCard("Los puntos dolorosos no establecen un diagnóstico por sí solos; la palpación debe estandarizarse y correlacionarse con historia y función.")
        }
        ResponsiveSectionV17("Apertura") { listOf("<30 mm limitada","30–39 mm reducida/valorar","40–50 mm aprox.",">50 mm hipermovilidad/variación").forEach { x -> FilterChip(opening == x, { opening = x }, { Text(x) }, Modifier.fillMaxWidth()) } }
        ResponsiveSectionV17("Checklist") { checklist.forEach { x -> FilterChip(x in findings, { toggleV40(findings,x) }, { Text(x) }, Modifier.fillMaxWidth()) } }
        val summary = "Apertura: $opening · zonas dolorosas: ${if (painful.isEmpty()) "ninguna" else painful.sorted().joinToString()} · hallazgos: ${if (findings.isEmpty()) "ninguno" else findings.joinToString()}."
        ResponsiveSectionV17("Resumen") { Text(summary); Card(onClick = { TeachingStateV40.moduleSummaries["tmd"] = summary }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) { Text("Guardar resumen ATM", Modifier.padding(12.dp), fontWeight = FontWeight.Black) } }
    }
}

@Composable
private fun TmjCanvasV40(painful: List<Int>) {
    Canvas(Modifier.fillMaxWidth().height(260.dp).padding(8.dp)) {
        val w=size.width; val h=size.height
        val temporal=Path().apply{moveTo(w*.15f,h*.20f);cubicTo(w*.35f,h*.08f,w*.72f,h*.12f,w*.84f,h*.28f);cubicTo(w*.72f,h*.34f,w*.65f,h*.38f,w*.60f,h*.48f);lineTo(w*.28f,h*.48f);cubicTo(w*.24f,h*.35f,w*.20f,h*.28f,w*.15f,h*.20f);close()}
        drawPath(temporal,Color(0xFFE7D7B7));drawPath(temporal,Color(0xFF756A56),style=Stroke(3f))
        val mandible=Path().apply{moveTo(w*.48f,h*.83f);lineTo(w*.44f,h*.54f);cubicTo(w*.42f,h*.44f,w*.47f,h*.37f,w*.55f,h*.36f);cubicTo(w*.63f,h*.35f,w*.68f,h*.41f,w*.67f,h*.49f);lineTo(w*.61f,h*.83f);close()}
        drawPath(mandible,Color(0xFFF1DFC0));drawPath(mandible,Color(0xFF756A56),style=Stroke(3f));drawOval(Color(0xFFBBA0B6),Offset(w*.43f,h*.32f),Size(w*.25f,h*.07f))
        val pts=listOf(.44f to .47f,.46f to .37f,.39f to .43f,.34f to .48f,.62f to .49f,.64f to .39f,.69f to .45f,.72f to .52f)
        pts.forEachIndexed{i,(x,y)->val n=i+1;drawCircle(if(n in painful)Color(0xFFD33B4C) else Color(0xFF2D7CC0),12f,Offset(w*x,h*y));drawCircle(Color.White,12f,Offset(w*x,h*y),style=Stroke(2f))}
    }
}

private data class OcclusionV40(val group:String,val option:String,val explanation:String)
private val occlusionDataV40=listOf(
    OcclusionV40("Plano terminal","Recto","Caras distales de segundos molares temporales coinciden aproximadamente."), OcclusionV40("Plano terminal","Escalón mesial","Segundo molar temporal inferior termina mesial respecto al superior."), OcclusionV40("Plano terminal","Escalón distal","Segundo molar temporal inferior termina distal respecto al superior."),
    OcclusionV40("Angle molar","Clase I","Relación del primer molar permanente compatible con Clase I."), OcclusionV40("Angle molar","Clase II","Molar inferior relativamente distal respecto al superior."), OcclusionV40("Angle molar","Clase III","Molar inferior relativamente mesial respecto al superior."),
    OcclusionV40("Relación canina","Clase I","Canino superior en relación de Clase I con referencia inferior."), OcclusionV40("Relación canina","Clase II","Canino superior relativamente mesial."), OcclusionV40("Relación canina","Clase III","Canino superior relativamente distal."),
    OcclusionV40("Overjet","Normal/positivo","Distancia horizontal; debe medirse en mm."), OcclusionV40("Overjet","Aumentado","Resalte horizontal aumentado; registrar medida."), OcclusionV40("Overjet","Negativo","Incisivos inferiores por delante de superiores."),
    OcclusionV40("Overbite","Normal","Solapamiento vertical moderado."), OcclusionV40("Overbite","Profundo","Solapamiento aumentado; registrar porcentaje/mm."), OcclusionV40("Overbite","Abierto","Ausencia de solapamiento/espacio vertical."),
    OcclusionV40("Mordida cruzada","Anterior","Relación transversal/sagital invertida en dientes anteriores."), OcclusionV40("Mordida cruzada","Posterior unilateral","Relación transversal invertida en un lado."), OcclusionV40("Mordida cruzada","Posterior bilateral","Relación transversal invertida en ambos lados.")
)

@Composable
fun OcclusionTeachingV40Screen(lang: String, onBack: () -> Unit) {
    val selected = remember { mutableStateListOf<String>() }
    var display by remember { mutableStateOf(occlusionDataV40[3]) }
    ResponsiveScreenV17("Examen de oclusión · atlas", "Relaciones dentarias con ilustraciones anatómicas simplificadas y resumen final.", onBack) { profile ->
        occlusionDataV40.groupBy { it.group }.forEach { (group, options) ->
            ResponsiveSectionV17(group) {
                OcclusionCanvasV40(group, display.option)
                AdaptiveGridV17(options.size, if (profile.largeSystemText) 1 else 3) { i ->
                    val o=options[i]; val key="$group:${o.option}"
                    FilterChip(key in selected, { selected.removeAll { it.startsWith("$group:") }; selected.add(key); display=o }, { Text(o.option) }, Modifier.fillMaxWidth())
                }
                selected.firstOrNull { it.startsWith("$group:") }?.substringAfter(":")?.let { choice -> options.firstOrNull { it.option==choice }?.let { Text(it.explanation) } }
            }
        }
        val summary = if (selected.isEmpty()) "Sin relaciones seleccionadas." else selected.joinToString(" · ") { it.replace(":",": ") }
        ResponsiveSectionV17("Resumen de oclusión") { Text(summary); Card(onClick={TeachingStateV40.moduleSummaries["occlusion"]=summary},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Text("Guardar resumen",Modifier.padding(12.dp),fontWeight=FontWeight.Black)} }
    }
}

@Composable
private fun OcclusionCanvasV40(group:String,option:String) {
    Canvas(Modifier.fillMaxWidth().height(175.dp).padding(8.dp)) {
        val w=size.width; val h=size.height
        fun crown(cx:Float,base:Float,upper:Boolean){val dir=if(upper)1f else -1f;val p=Path().apply{moveTo(cx-w*.055f,base);quadraticBezierTo(cx,base-dir*h*.07f,cx+w*.055f,base);lineTo(cx+w*.04f,base+dir*h*.17f);quadraticBezierTo(cx,base+dir*h*.20f,cx-w*.04f,base+dir*h*.17f);close()};drawPath(p,Color(0xFFFFF1D3));drawPath(p,Color(0xFF806F5A),style=Stroke(2.5f))}
        drawLine(Color(0xFFE69AA0),Offset(w*.08f,h*.46f),Offset(w*.92f,h*.46f),strokeWidth=9f);drawLine(Color(0xFFE69AA0),Offset(w*.08f,h*.54f),Offset(w*.92f,h*.54f),strokeWidth=9f)
        val shift=when{option.contains("II")||option.contains("distal",true)->w*.07f;option.contains("III")||option.contains("mesial",true)->-w*.07f;else->0f}
        for(i in 0..4){val x=w*(.28f+i*.11f);crown(x,h*.43f,true);crown(x+shift,h*.57f,false)}
        if(group=="Overjet")drawLine(Color(0xFF2E79B6),Offset(w*.46f,h*.42f),Offset(w*(if(option=="Negativo").39f else .57f),h*.42f),strokeWidth=5f)
        if(group=="Overbite")drawLine(Color(0xFF2E79B6),Offset(w*.70f,h*.42f),Offset(w*.70f,if(option=="Abierto")h*.51f else if(option=="Profundo")h*.66f else h*.56f),strokeWidth=5f)
    }
}

private data class HeadV40(val title:String,val detail:String)
private val headDataV40=listOf(
    HeadV40("Edad aparente","Comparación general con edad conocida; no diagnostica desarrollo."), HeadV40("Marcha","Estabilidad, simetría y apoyo."), HeadV40("Facies","Descripción de expresión y rasgos visibles sin atribuir síndromes sólo por apariencia."), HeadV40("Actitud/postura","Posición espontánea y signos defensivos."), HeadV40("Cráneo","Simetría, contorno, lesiones y dolor."), HeadV40("Cara","Simetría, tercios, perfil, piel y movimientos."), HeadV40("Músculos masticatorios","Palpar masetero/temporal y comparar dolor/tensión."), HeadV40("Cuello","Movilidad, dolor, masas y cadenas ganglionares."))
private val nodesV40=listOf(
    "Preauriculares" to "Delante del trago.", "Postauriculares" to "Detrás de la oreja sobre mastoides.", "Occipitales" to "Región posterior del cráneo.", "Submentonianos" to "Debajo del mentón.", "Submandibulares" to "Debajo del borde mandibular.", "Cervicales anteriores" to "Cadena anterior del cuello.", "Cervicales posteriores" to "Cadena posterior al esternocleidomastoideo.", "Supraclaviculares" to "Fosas por encima de clavículas; adenopatía persistente requiere atención cuidadosa.")

@Composable
fun HeadNeckTeachingV40Screen(lang:String,onBack:()->Unit) {
    var open by remember{mutableStateOf<Int?>(null)}; var node by remember{mutableStateOf(0)}
    ResponsiveScreenV17("Exploración de cabeza y cuello","Sin números romanos. Toca cada elemento y ganglio.",onBack){profile->
        ResponsiveSectionV17("Secuencia") { AdaptiveGridV17(headDataV40.size,if(profile.largeSystemText)1 else 2){i->val x=headDataV40[i];Card(onClick={open=if(open==i)null else i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(open==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text(x.title,fontWeight=FontWeight.Black);if(open==i)Text(x.detail)}}} }
        ResponsiveSectionV17("Mapa de ganglios") { LymphCanvasV40(node); nodesV40.forEachIndexed{i,(name,detail)->Card(onClick={node=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(node==i)MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)){Column(Modifier.padding(9.dp)){Text(name,fontWeight=FontWeight.Black);if(node==i)Text(detail)}}}; NoticeCard("Describe localización, tamaño, dolor, consistencia, movilidad/fijación y evolución.") }
    }
}

@Composable
private fun LymphCanvasV40(selected:Int) {
    Canvas(Modifier.fillMaxWidth().height(285.dp).padding(8.dp)) {
        val w=size.width;val h=size.height;drawOval(Color(0xFFF0C6B0),Offset(w*.32f,h*.08f),Size(w*.36f,h*.38f));drawLine(Color(0xFF9A7564),Offset(w*.50f,h*.46f),Offset(w*.50f,h*.86f),strokeWidth=w*.20f)
        val pts=listOf(.67f to .25f,.67f to .33f,.47f to .10f,.50f to .43f,.58f to .43f,.61f to .58f,.66f to .67f,.63f to .82f)
        pts.forEachIndexed{i,(x,y)->drawCircle(if(i==selected)Color(0xFFD23C4A) else Color(0xFF3C8C55),12f,Offset(w*x,h*y));drawCircle(Color.White,12f,Offset(w*x,h*y),style=Stroke(2f))}
    }
}

private data class AnomalyV40(val name:String,val definition:String,val kind:String)
private val dentalAnomaliesV40=listOf(
    AnomalyV40("Agenesia/hipodoncia","Ausencia congénita de uno o más dientes; confirmar con historia e imagen.","missing"),AnomalyV40("Supernumerario","Diente adicional a la fórmula normal.","extra"),AnomalyV40("Microdoncia","Diente menor de lo esperado.","small"),AnomalyV40("Macrodoncia","Diente mayor de lo esperado.","large"),AnomalyV40("Geminación","Intento de división de un germen dentario.","double"),AnomalyV40("Fusión","Unión de dos gérmenes dentarios.","double"),AnomalyV40("Dens invaginatus","Invaginación de esmalte/dentina.","invag"),AnomalyV40("Dens evaginatus","Cúspide o tubérculo accesorio.","evag"),AnomalyV40("Taurodontismo","Cámara pulpar alargada y furcación desplazada apicalmente.","root"),AnomalyV40("Hipoplasia de esmalte","Defecto cuantitativo de esmalte.","enamel"),AnomalyV40("Hipomineralización","Defecto cualitativo de mineralización.","enamel"))
private val eruptionAnomaliesV40=listOf(
    AnomalyV40("Erupción ectópica","Trayectoria eruptiva anormal.","tilt"),AnomalyV40("Transposición","Intercambio posicional de dos dientes.","swap"),AnomalyV40("Retraso eruptivo","No erupciona en el tiempo esperado.","delay"),AnomalyV40("Retención primaria","Falla de erupción antes de emerger.","delay"),AnomalyV40("Impactación","Erupción impedida por posición, barrera o espacio.","tilt"),AnomalyV40("Inclusión","Diente permanece dentro de hueso/tejido.","delay"),AnomalyV40("Anquilosis / sumergido","Fusión cemento-hueso con infraoclusión progresiva.","low"),AnomalyV40("Natal/neonatal","Presente al nacimiento o primeras semanas.","early"),AnomalyV40("Erupción precoz","Emergencia antes de lo esperado.","early"))

@Composable fun DentalAnomaliesTeachingV40Screen(lang:String,onBack:()->Unit)=AnomalyAtlasV40("Anomalías dentales",dentalAnomaliesV40,onBack)
@Composable fun EruptionAnomaliesTeachingV40Screen(lang:String,onBack:()->Unit)=AnomalyAtlasV40("Erupción y posición",eruptionAnomaliesV40,onBack)

@Composable
private fun AnomalyAtlasV40(title:String,items:List<AnomalyV40>,onBack:()->Unit) {
    var selected by remember{mutableStateOf(0)}
    ResponsiveScreenV17(title,"Toca una alteración para explicación e ilustración dental.",onBack){profile->
        ToothAnomalyCanvasV40(items[selected].kind)
        AdaptiveGridV17(items.size,if(profile.largeSystemText)1 else 2){i->val a=items[i];Card(onClick={selected=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(selected==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text(a.name,fontWeight=FontWeight.Black);if(selected==i)Text(a.definition)}}}
    }
}

@Composable
private fun ToothAnomalyCanvasV40(kind:String) {
    Canvas(Modifier.fillMaxWidth().height(245.dp).padding(8.dp)) {
        val w=size.width;val h=size.height
        fun tooth(cx:Float,scale:Float=1f,shiftY:Float=0f){val p=Path().apply{moveTo(cx-w*.07f*scale,h*(.18f+shiftY));quadraticBezierTo(cx,h*(.10f+shiftY),cx+w*.07f*scale,h*(.18f+shiftY));lineTo(cx+w*.05f*scale,h*(.44f+shiftY));lineTo(cx+w*.025f*scale,h*(.82f+shiftY));quadraticBezierTo(cx,h*(.91f+shiftY),cx-w*.025f*scale,h*(.82f+shiftY));lineTo(cx-w*.05f*scale,h*(.44f+shiftY));close()};drawPath(p,Color(0xFFFFF1D4));drawPath(p,Color(0xFF7B6D58),style=Stroke(3f))}
        when(kind){"missing"->{tooth(w*.38f);tooth(w*.62f);drawCircle(Color(0x44CC5560),w*.07f,Offset(w*.50f,h*.25f))};"extra"->{tooth(w*.32f);tooth(w*.50f,.72f);tooth(w*.68f)};"small"->{tooth(w*.40f);tooth(w*.60f,.55f)};"large"->{tooth(w*.38f);tooth(w*.62f,1.4f)};"double"->{tooth(w*.46f,1.2f);tooth(w*.54f,1.2f)};"delay"->{tooth(w*.38f);tooth(w*.62f,1f,.18f);drawLine(Color(0xFFE69AA0),Offset(w*.2f,h*.32f),Offset(w*.8f,h*.32f),strokeWidth=7f)};"low"->{tooth(w*.38f);tooth(w*.62f,1f,.10f)};"early"->{tooth(w*.50f,.75f);drawLine(Color(0xFFE69AA0),Offset(w*.2f,h*.42f),Offset(w*.8f,h*.42f),strokeWidth=7f)};else->{tooth(w*.40f);tooth(w*.60f)}}
    }
}

private data class HabitV40(val name:String,val extra:String,val intra:String,val management:String)
private val habitsV40=listOf(
    HabitV40("Succión digital","Callosidad/dedo irritado según intensidad.","Mordida abierta, overjet aumentado o arco estrecho en algunos casos.","Educación, refuerzo positivo y valoración ortodóncica si persiste."),HabitV40("Chupón","Frecuencia/duración, especialmente nocturna.","Cambios oclusales según intensidad y duración.","Retiro gradual acorde a edad y seguimiento."),HabitV40("Interposición lingual","Patrón de deglución/habla.","Mordida abierta o espacios en algunos casos.","Identificar causa; terapia miofuncional/fonoaudiología y ortodoncia cuando proceda."),HabitV40("Respiración bucal","Labios entreabiertos, sequedad, postura de cabeza.","Mucosa seca y gingivitis anterior; no diagnosticar obstrucción sólo por apariencia.","Derivar a medicina/ORL si se sospecha obstrucción y tratar salud oral."),HabitV40("Onicofagia","Uñas cortas/trauma periungueal.","Desgaste o microtrauma incisivo.","Concientización, sustitución y manejo conductual."),HabitV40("Bruxismo","Tensión muscular o cefalea referida.","Facetas, fracturas/restauraciones o sensibilidad; desgaste no prueba bruxismo actual.","Educación, sueño/estrés y manejo TTM; férula sólo con indicación."),HabitV40("Mordisqueo de labio/mejilla","Trauma repetido.","Línea alba/morsicatio/queratosis.","Retirar trauma y reevaluar si persiste."),HabitV40("Morder objetos","Lápices, hielo u objetos referidos.","Fisuras/desgaste/fracturas.","Eliminar hábito y valorar daño."))

@Composable
fun HabitsTeachingV40Screen(lang:String,onBack:()->Unit) {
    var selected by remember{mutableStateOf(0)}
    ResponsiveScreenV17("Hábitos y parafunciones","Identificación extraoral, intraoral y manejo.",onBack){profile->
        AdaptiveGridV17(habitsV40.size,if(profile.largeSystemText)1 else 2){i->val h=habitsV40[i];Card(onClick={selected=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(selected==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text(h.name,fontWeight=FontWeight.Black);if(selected==i){Text("Extraoral: ${h.extra}");Text("Intraoral: ${h.intra}");Text("Manejo: ${h.management}")}}}}
        NoticeCard("Registrar inicio, frecuencia, duración, intensidad/contexto y si se suspendió. Los hallazgos suelen ser multifactoriales.")
    }
}
