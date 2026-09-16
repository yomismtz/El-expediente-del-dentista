package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

@Composable
fun PhysicalExamHubV40Screen(lang:String,onVitals:()->Unit,onBack:()->Unit){
    var mode by remember{mutableStateOf<Int?>(null)}
    if(mode==1){GeneralInspectionV40Screen(lang){mode=null};return}
    ResponsiveScreenV17("Exploración física","Dos rutas: parámetros fisiológicos y observación general del comportamiento/estado.",onBack){_->
        Card(onClick=onVitals,modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){
            Column(Modifier.padding(16.dp)){Text("❤️ Signos vitales y glucosa capilar",fontWeight=FontWeight.Black);Text("Temperatura · frecuencia cardiaca · frecuencia respiratoria · tensión arterial · glucosa capilar. Toca para abrir la herramienta existente.")}
        }
        Card(onClick={mode=1},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
            Column(Modifier.padding(16.dp)){Text("👁 Inspección general",fontWeight=FontWeight.Black);Text("Marcha, forma de sentarse, cooperación, conducta, edad aparente/mental vs cronológica, juego y autocuidado.")}
        }
    }
}

private data class InspectionItemV40(val title:String,val options:List<Pair<String,String>>)
private val inspectionV40=listOf(
    InspectionItemV40("Marcha / manera de caminar",listOf("Estable y coordinada" to "Paso simétrico, equilibrio y ritmo adecuados para el contexto.","Cautelosa/lenta" to "Puede asociarse a dolor, miedo, debilidad o edad; preguntar contexto.","Claudicante" to "Apoyo asimétrico; investigar dolor o lesión.","Inestable/atáxica" to "Requiere atención por posible alteración neurológica, vestibular, fármacos u otra causa.")),
    InspectionItemV40("Forma de sentarse y postura",listOf("Erguida y relajada" to "Postura funcional sin tensión evidente.","Encogida/defensiva" to "Puede reflejar ansiedad, dolor o incomodidad; no diagnostica por sí sola.","Inquieta" to "Movimientos frecuentes; considerar edad, ansiedad, dolor, TDAH referido u otros factores.","Necesita apoyo" to "Puede indicar limitación motora, fatiga o condición sistémica.")),
    InspectionItemV40("Cooperación clínica",listOf("Cooperador" to "Acepta interacción y procedimientos acorde a su edad.","Cooperador con reservas" to "Requiere explicación, pausas o adaptación conductual.","Potencialmente cooperador" to "Puede progresar con técnicas de guía de conducta.","No cooperador en este momento" to "No es una etiqueta permanente; explorar miedo, dolor, desarrollo, comunicación y contexto.")),
    InspectionItemV40("Edad cronológica vs desarrollo observado",listOf("Acorde a edad" to "Conducta y comprensión compatibles de forma aproximada.","Comprensión menor a la esperada" to "Adaptar lenguaje y verificar antecedentes de desarrollo; no asignar edad mental sin evaluación formal.","Comprensión mayor a la esperada" to "Puede manejar explicaciones más complejas; confirmar comprensión.","No valorable" to "Evitar inferencias cuando no hay suficiente interacción.")),
    InspectionItemV40("Juego / conducta pediátrica",listOf("Juego espontáneo" to "Explora, interactúa o usa objetos acorde al contexto.","Juego paralelo" to "Frecuente en etapas tempranas; observar sin patologizar.","Juego cooperativo" to "Interacción compartida con reglas/objetivos.","Evita jugar/interactuar" to "Puede relacionarse con ansiedad, dolor, cansancio o desarrollo; requiere contexto.")),
    InspectionItemV40("Aspecto y autocuidado",listOf("Aseo acorde al contexto" to "Registrar observación sin juicios de valor.","Signos de descuido" to "Explorar acceso, dependencia, enfermedad o barreras sociales; no asumir negligencia.","Vestimenta inadecuada al clima" to "Dato de contexto que puede orientar a vulnerabilidad o condición cognitiva.","No valorable" to "No forzar conclusiones."))
)

@Composable
fun GeneralInspectionV40Screen(lang:String,onBack:()->Unit){
    val choices=remember{mutableStateListOf<String>()}
    ResponsiveScreenV17("Inspección general · guía interactiva","Observa antes de tocar al paciente: movilidad, postura, interacción, cooperación y aspecto general.",onBack){profile->
        inspectionV40.forEach{group->
            ResponsiveSectionV17(group.title){
                AdaptiveGridV17(group.options.size,if(profile.largeSystemText)1 else 2){i->val(o,d)=group.options[i];Card(onClick={val prefix=group.title+":";choices.removeAll{it.startsWith(prefix)};choices.add(prefix+o)},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(group.title+":"+o in choices)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text(o,fontWeight=FontWeight.Black);if(group.title+":"+o in choices)Text(d)}}}
            }
        }
        NoticeCard("En pediatría la cooperación y conducta cambian con edad, dolor, ansiedad, experiencias previas y desarrollo. La observación guía comunicación; no sustituye evaluación psicológica o del neurodesarrollo.")
    }
}

private data class RocabadoPointV40(val n:Int,val name:String,val detail:String)
private val rocabadoV40=listOf(
    RocabadoPointV40(1,"Sinovial anteroinferior","Palpación anterior/inferior del compartimento articular; dolor puede sugerir irritación local, pero no define por sí solo un diagnóstico."),
    RocabadoPointV40(2,"Sinovial anterosuperior","Zona anterior/superior de la articulación."),
    RocabadoPointV40(3,"Ligamento colateral lateral","Explora sensibilidad de tejidos laterales relacionados con el complejo disco-condilar."),
    RocabadoPointV40(4,"Ligamento temporomandibular","Valora dolor localizado sobre el ligamento lateral/temporomandibular."),
    RocabadoPointV40(5,"Sinovial posteroinferior","Zona posterior/inferior del compartimento articular."),
    RocabadoPointV40(6,"Sinovial posterosuperior","Zona posterior/superior del compartimento articular."),
    RocabadoPointV40(7,"Ligamento posterior","Explora estructuras posteriores articulares con presión controlada."),
    RocabadoPointV40(8,"Región retrodiscal","Dolor puede acompañar inflamación/irritación retrodiscal; integrar con historia, movimientos y ruidos.")
)

@Composable
fun TmjTeachingV40Screen(lang:String,onBack:()->Unit){
    val painful=remember{mutableStateListOf<Int>()}
    val findings=remember{mutableStateListOf<String>()}
    var opening by remember{mutableStateOf("40–50 mm aprox.")}
    val checklist=listOf("Dolor al abrir/cerrar","Chasquido","Crepitación","Desviación","Deflexión","Bloqueo","Limitación funcional","Dolor muscular","Bruxismo referido","Sin alteraciones aparentes")
    ResponsiveScreenV17("Exploración de ATM y TTM","Anatomía, mapa de dolor de Rocabado, movimientos, ruidos y resumen didáctico.",onBack){profile->
        ResponsiveSectionV17("Anatomía de la ATM"){
            TmjAnatomyV40(painful)
            Text("La ATM integra cóndilo mandibular, fosa/eminencia temporal, disco, cápsula, ligamentos y tejidos retrodiscales. La imagen es una ilustración anatómica educativa, no una radiografía.")
        }
        ResponsiveSectionV17("Mapa de dolor de Rocabado · 8 zonas"){
            AdaptiveGridV17(rocabadoV40.size,if(profile.largeSystemText)1 else 2){i->val p=rocabadoV40[i];Card(onClick={if(p.n in painful)painful.remove(p.n) else painful.add(p.n)},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(p.n in painful)MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text("${p.n}. ${p.name}",fontWeight=FontWeight.Black);if(p.n in painful)Text("Dolor marcado · ${p.detail}")}}}
            NoticeCard("El mapa de dolor es una herramienta complementaria. La reproducibilidad de la palpación depende de técnica/entrenamiento y los puntos dolorosos no diagnostican por sí solos el trastorno.")
        }
        ResponsiveSectionV17("Movimientos y apertura"){
            listOf("<30 mm limitada","30–39 mm reducida/valorar","40–50 mm aprox.",">50 mm hipermovilidad/variación a valorar").forEach{x->FilterChip(opening==x,{opening=x},{Text(x)},Modifier.fillMaxWidth())}
            Text("Medir apertura interincisal y registrar dolor; laterotrusión/protrusión se miden en mm y se comparan bilateralmente.")
        }
        ResponsiveSectionV17("Checklist de hallazgos"){
            checklist.forEach{x->FilterChip(x in findings,{toggleV40(findings,x)},{Text(x)},Modifier.fillMaxWidth())}
        }
        ResponsiveSectionV17("Resumen TTM"){
            val painText=if(painful.isEmpty())"sin puntos de dolor marcados" else "dolor en zonas Rocabado ${painful.sorted().joinToString()}"
            val fText=if(findings.isEmpty())"sin hallazgos seleccionados" else findings.joinToString()
            val summary="Apertura: $opening; $painText; hallazgos: $fText. Diagnóstico TTM: integrar con criterios diagnósticos estandarizados y examen completo."
            Text(summary)
            Card(onClick={TeachingStateV40.moduleSummaries["tmd"]=summary},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Text("Guardar resumen de ATM",Modifier.padding(12.dp),fontWeight=FontWeight.Black)}
        }
    }
}

@Composable private fun TmjAnatomyV40(painful:List<Int>){
    Canvas(Modifier.fillMaxWidth().height(270.dp).padding(8.dp)){
        val w=size.width;val h=size.height
        // temporal bone/fossa and articular eminence
        val temporal=Path().apply{moveTo(w*.15f,h*.20f);cubicTo(w*.35f,h*.08f,w*.72f,h*.12f,w*.84f,h*.28f);cubicTo(w*.72f,h*.34f,w*.65f,h*.38f,w*.60f,h*.48f);lineTo(w*.28f,h*.48f);cubicTo(w*.24f,h*.35f,w*.20f,h*.28f,w*.15f,h*.20f);close()}
        drawPath(temporal,Color(0xFFE7D7B7));drawPath(temporal,Color(0xFF756A56),style=Stroke(3f))
        // mandibular ramus + condyle
        val mandible=Path().apply{moveTo(w*.48f,h*.83f);lineTo(w*.44f,h*.54f);cubicTo(w*.42f,h*.44f,w*.47f,h*.37f,w*.55f,h*.36f);cubicTo(w*.63f,h*.35f,w*.68f,h*.41f,w*.67f,h*.49f);lineTo(w*.61f,h*.83f);close()}
        drawPath(mandible,Color(0xFFF1DFC0));drawPath(mandible,Color(0xFF756A56),style=Stroke(3f))
        drawOval(Color(0xFFBBA0B6),Offset(w*.43f,h*.32f),Size(w*.25f,h*.07f)) // disc
        drawArc(Color(0xFF9B5F69),190f,160f,false,Offset(w*.39f,h*.27f),Size(w*.34f,h*.31f),style=Stroke(5f))
        val positions=listOf(.44f to .47f,.46f to .37f,.39f to .43f,.34f to .48f,.62f to .49f,.64f to .39f,.69f to .45f,.72f to .52f)
        positions.forEachIndexed{i,(x,y)->val n=i+1;drawCircle(if(n in painful)Color(0xFFD33B4C) else Color(0xFF2D7CC0),radius=12f,center=Offset(w*x,h*y));drawCircle(Color.White,radius=12f,center=Offset(w*x,h*y),style=Stroke(2f))}
    }
}

private data class OcclusionExampleV40(val group:String,val option:String,val explanation:String)
private val occlusionOptionsV40=listOf(
    OcclusionExampleV40("Plano terminal","Recto","Caras distales de segundos molares temporales superiores e inferiores coinciden aproximadamente en un plano vertical."),
    OcclusionExampleV40("Plano terminal","Escalón mesial","La superficie distal del segundo molar temporal inferior queda mesial a la superior."),
    OcclusionExampleV40("Plano terminal","Escalón distal","La superficie distal del segundo molar temporal inferior queda distal a la superior; puede asociarse a patrón Clase II."),
    OcclusionExampleV40("Angle molar","Clase I","Cúspide mesiovestibular del primer molar superior en relación con surco vestibular del primer molar inferior compatible con Clase I."),
    OcclusionExampleV40("Angle molar","Clase II","Molar inferior relativamente distal respecto al superior."),
    OcclusionExampleV40("Angle molar","Clase III","Molar inferior relativamente mesial respecto al superior."),
    OcclusionExampleV40("Relación canina","Clase I","Cúspide del canino superior en la tronera entre canino inferior y primer premolar/primer molar temporal según dentición."),
    OcclusionExampleV40("Relación canina","Clase II","Canino superior relativamente mesial respecto a referencia inferior."),
    OcclusionExampleV40("Relación canina","Clase III","Canino superior relativamente distal respecto a referencia inferior."),
    OcclusionExampleV40("Overjet","Normal/positivo","Distancia horizontal de incisivos superiores por delante de inferiores; medir en mm."),
    OcclusionExampleV40("Overjet","Aumentado","Resalte horizontal mayor al esperado; registrar medida, no sólo etiqueta."),
    OcclusionExampleV40("Overjet","Negativo","Incisivos inferiores por delante de superiores; puede acompañar mordida cruzada anterior."),
    OcclusionExampleV40("Overbite","Normal","Solapamiento vertical moderado de incisivos."),
    OcclusionExampleV40("Overbite","Profundo","Solapamiento vertical aumentado; registrar porcentaje/mm y contacto traumático si existe."),
    OcclusionExampleV40("Overbite","Abierto","Ausencia de solapamiento/espacio vertical anterior o posterior según sitio."),
    OcclusionExampleV40("Mordida cruzada","Anterior","Uno o más dientes anteriores superiores ocluyen linguales a inferiores."),
    OcclusionExampleV40("Mordida cruzada","Posterior unilateral","Relación transversal invertida en un lado; evaluar deslizamiento funcional y expansión."),
    OcclusionExampleV40("Mordida cruzada","Posterior bilateral","Relación transversal invertida en ambos lados."))

@Composable
fun OcclusionTeachingV40Screen(lang:String,onBack:()->Unit){
    val selections=remember{mutableStateListOf<String>()}
    var display by remember{mutableStateOf(occlusionOptionsV40[3])}
    ResponsiveScreenV17("Examen de oclusión · atlas interactivo","Selecciona relaciones y observa esquemas dentarios anatómicos simplificados; al final se genera un resumen.",onBack){profile->
        val groups=occlusionOptionsV40.groupBy{it.group}
        groups.forEach{(group,opts)->
            ResponsiveSectionV17(group){
                OcclusionTeethDiagramV40(display.group,display.option)
                AdaptiveGridV17(opts.size,if(profile.largeSystemText)1 else 3){i->val o=opts[i];val key="$group:${o.option}";FilterChip(key in selections,{selections.removeAll{it.startsWith("$group:")};selections.add(key);display=o},{Text(o.option)},Modifier.fillMaxWidth())}
                val current=selections.firstOrNull{it.startsWith("$group:")}?.substringAfter(":")
                val info=opts.firstOrNull{it.option==current}
                if(info!=null)Text(info.explanation)
            }
        }
        ResponsiveSectionV17("Resumen de oclusión"){
            val summary=if(selections.isEmpty())"Sin relaciones seleccionadas." else selections.joinToString(" · "){it.replace(":",": ")}
            Text(summary)
            Card(onClick={TeachingStateV40.moduleSummaries["occlusion"]=summary},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Text("Guardar resumen de oclusión",Modifier.padding(12.dp),fontWeight=FontWeight.Black)}
        }
    }
}

@Composable private fun OcclusionTeethDiagramV40(group:String,option:String){
    Canvas(Modifier.fillMaxWidth().height(180.dp).padding(8.dp)){
        val w=size.width;val h=size.height
        fun crown(cx:Float,base:Float,upper:Boolean,scale:Float=1f){
            val dir=if(upper)1f else -1f
            val p=Path().apply{moveTo(cx-w*.06f*scale,base);quadraticBezierTo(cx,base-dir*h*.08f,cx+w*.06f*scale,base);lineTo(cx+w*.045f*scale,base+dir*h*.18f);quadraticBezierTo(cx,base+dir*h*.21f,cx-w*.045f*scale,base+dir*h*.18f);close()}
            drawPath(p,Color(0xFFFFF1D3));drawPath(p,Color(0xFF806F5A),style=Stroke(2.5f))
        }
        drawLine(Color(0xFFE69AA0),Offset(w*.08f,h*.46f),Offset(w*.92f,h*.46f),strokeWidth=10f)
        drawLine(Color(0xFFE69AA0),Offset(w*.08f,h*.54f),Offset(w*.92f,h*.54f),strokeWidth=10f)
        val shift=when{option.contains("II")||option.contains("Distal")||option.contains("distal") -> w*.08f;option.contains("III")||option.contains("Mesial")||option.contains("mesial") -> -w*.08f;else->0f}
        val cross=option.contains("cruzada",true)||option.contains("Negativo")
        for(i in 0..4){val x=w*(.28f+i*.11f);crown(x,h*.43f,true,if(group.contains("canina",true)&&i==2) .75f else 1f);crown(x+shift,h*.57f,false,if(group.contains("canina",true)&&i==2).75f else 1f)}
        if(group=="Overjet")drawLine(Color(0xFF2E79B6),Offset(w*.46f,h*.42f),Offset(w*(if(cross).39f else .57f),h*.42f),strokeWidth=5f)
        if(group=="Overbite")drawLine(Color(0xFF2E79B6),Offset(w*.70f,h*.42f),Offset(w*.70f,if(option=="Abierto")h*.52f else if(option=="Profundo")h*.66f else h*.56f),strokeWidth=5f)
    }
}

private data class HeadItemV40(val title:String,val detail:String)
private val headItemsV40=listOf(
    HeadItemV40("Edad aparente","Compara de forma general apariencia con edad conocida; no diagnostica envejecimiento ni desarrollo."),
    HeadItemV40("Marcha","Observa estabilidad, simetría, apoyo y necesidad de asistencia."),
    HeadItemV40("Facies","Describe expresión y rasgos clínicos visibles sin atribuir síndromes sólo por apariencia."),
    HeadItemV40("Actitud/postura","Observa posición espontánea, dolor defensivo, equilibrio y simetrías."),
    HeadItemV40("Cráneo","Inspección/palpación de simetría, contorno, lesiones y dolor."),
    HeadItemV40("Cara","Simetría, tercios faciales, perfil, piel y movimientos."),
    HeadItemV40("Músculos masticatorios","Palpar masetero, temporal y otros músculos indicados; comparar dolor/tensión bilateral."),
    HeadItemV40("Cuello","Inspección, movilidad, dolor, masas, tiroides cuando proceda y cadenas ganglionares."))
private val nodesV40=listOf(
    "Preauriculares" to "Delante del trago/oreja; drenan regiones temporales, párpados y áreas faciales.",
    "Postauriculares" to "Detrás de la oreja sobre mastoides.",
    "Occipitales" to "Región posterior del cráneo cerca de línea nucal.",
    "Submentonianos" to "Debajo del mentón, entre vientres anteriores del digástrico.",
    "Submandibulares" to "Debajo del borde mandibular, especialmente triángulo submandibular.",
    "Cervicales anteriores" to "A lo largo de estructuras anteriores del cuello/esternocleidomastoideo.",
    "Cervicales posteriores" to "Cadena posterior al esternocleidomastoideo.",
    "Supraclaviculares" to "Fosas por encima de clavículas; adenopatía aquí requiere atención clínica cuidadosa."))

@Composable
fun HeadNeckTeachingV40Screen(lang:String,onBack:()->Unit){
    var open by remember{mutableStateOf<Int?>(null)};var node by remember{mutableStateOf(0)}
    ResponsiveScreenV17("Exploración de cabeza y cuello","Sin numeración romana: toca cada elemento y cada cadena ganglionar para saber qué se explora.",onBack){profile->
        ResponsiveSectionV17("Secuencia de exploración"){
            AdaptiveGridV17(headItemsV40.size,if(profile.largeSystemText)1 else 2){i->val x=headItemsV40[i];Card(onClick={open=if(open==i)null else i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(open==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text(x.title,fontWeight=FontWeight.Black);if(open==i)Text(x.detail)}}}
        }
        ResponsiveSectionV17("Mapa anatómico de ganglios de cabeza y cuello"){
            LymphNodeMapV40(node)
            nodesV40.forEachIndexed{i,(name,detail)->Card(onClick={node=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(node==i)MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)){Column(Modifier.padding(9.dp)){Text(name,fontWeight=FontWeight.Black);if(node==i)Text(detail)}}}
            NoticeCard("Al palpar un ganglio se describe localización, tamaño aproximado, dolor, consistencia, movilidad/fijación y cambios en el tiempo. No toda adenopatía significa infección dental.")
        }
    }
}

@Composable private fun LymphNodeMapV40(selected:Int){
    Canvas(Modifier.fillMaxWidth().height(290.dp).padding(8.dp)){
        val w=size.width;val h=size.height
        drawOval(Color(0xFFF0C6B0),Offset(w*.32f,h*.08f),Size(w*.36f,h*.38f));drawLine(Color(0xFF9A7564),Offset(w*.50f,h*.46f),Offset(w*.50f,h*.86f),strokeWidth=w*.20f)
        val pts=listOf(.67f to .25f,.67f to .33f,.47f to .10f,.50f to .43f,.58f to .43f,.61f to .58f,.66f to .67f,.63f to .82f)
        pts.forEachIndexed{i,(x,y)->drawCircle(if(i==selected)Color(0xFFD23C4A) else Color(0xFF3C8C55),radius=12f,center=Offset(w*x,h*y));drawCircle(Color.White,radius=12f,center=Offset(w*x,h*y),style=Stroke(2f))}
    }
}

private data class AnomalyV40(val name:String,val definition:String,val visual:String)
private val dentalAnomaliesV40=listOf(
    AnomalyV40("Agenesia/hipodoncia","Ausencia congénita de uno o más dientes; confirmar con historia e imagen cuando corresponda.","missing"),
    AnomalyV40("Supernumerario","Diente adicional a la fórmula normal; puede alterar erupción/posición.","extra"),
    AnomalyV40("Microdoncia","Diente menor de lo esperado; puede ser localizada, como lateral conoide.","small"),
    AnomalyV40("Macrodoncia","Diente mayor de lo esperado; distinguir de fusión/geminación.","large"),
    AnomalyV40("Geminación","Intento de división de un germen; corona aumentada/bífida con conteo dental usualmente normal.","double"),
    AnomalyV40("Fusión","Unión de dos gérmenes dentarios; corona aumentada y conteo aparente reducido si se cuenta como uno.","double"),
    AnomalyV40("Dens invaginatus","Invaginación de esmalte/dentina; puede crear anatomía profunda susceptible a caries/pulpa.","invag"),
    AnomalyV40("Dens evaginatus","Cúspide/tubérculo accesorio con posible prolongación pulpar.","evag"),
    AnomalyV40("Taurodontismo","Cámara pulpar alargada y furcación desplazada apicalmente, diagnóstico principalmente radiográfico.","root"),
    AnomalyV40("Hipoplasia de esmalte","Defecto cuantitativo del esmalte: menor espesor, fosas/surcos según causa y momento.","enamel"),
    AnomalyV40("Hipomineralización","Defecto cualitativo de mineralización; opacidades demarcadas y fragilidad en algunos casos.","enamel"),
    AnomalyV40("Amelogénesis imperfecta","Trastorno hereditario generalizado del esmalte con distintos fenotipos.","enamel"),
    AnomalyV40("Dentinogénesis imperfecta","Trastorno hereditario de dentina, coloración/opalescencia y cambios radiográficos característicos.","enamel"))
private val eruptionV40=listOf(
    AnomalyV40("Erupción ectópica","Trayectoria eruptiva anormal que puede impactar/reabsorber dientes vecinos.","tilt"),
    AnomalyV40("Transposición","Intercambio posicional de dos dientes, parcial o completo.","swap"),
    AnomalyV40("Retraso eruptivo","Diente no erupciona en el tiempo esperado; comparar contralateral, desarrollo e imagen.","delay"),
    AnomalyV40("Retención primaria","Falla de erupción antes de emerger, sin barrera mecánica evidente en algunos casos.","delay"),
    AnomalyV40("Impactación","Erupción impedida por barrera, posición/anatomía o falta de espacio.","tilt"),
    AnomalyV40("Inclusión","Diente permanece dentro de hueso/tejido; describir posición y desarrollo.","delay"),
    AnomalyV40("Anquilosis / diente sumergido","Fusión cemento-hueso con infraoclusión progresiva, frecuente en temporales.","low"),
    AnomalyV40("Diente natal/neonatal","Presente al nacimiento o erupciona en primeras semanas; valorar movilidad, alimentación y riesgo.","early"),
    AnomalyV40("Erupción precoz","Emergencia antes de lo esperado; evaluar desarrollo radicular y secuencia.","early"))

@Composable fun DentalAnomaliesTeachingV40Screen(lang:String,onBack:()->Unit)=AnomalyAtlasV40("Anomalías dentales",dentalAnomaliesV40,onBack)
@Composable fun EruptionAnomaliesTeachingV40Screen(lang:String,onBack:()->Unit)=AnomalyAtlasV40("Alteraciones de erupción y posición",eruptionV40,onBack)

@Composable private fun AnomalyAtlasV40(title:String,items:List<AnomalyV40>,onBack:()->Unit){
    var selected by remember{mutableStateOf(0)}
    ResponsiveScreenV17(title,"Toca una alteración para ver qué es y una ilustración dental representativa.",onBack){profile->
        ToothAnomalyIllustrationV40(items[selected].visual)
        AdaptiveGridV17(items.size,if(profile.largeSystemText)1 else 2){i->val a=items[i];Card(onClick={selected=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(selected==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text(a.name,fontWeight=FontWeight.Black);if(selected==i)Text(a.definition)}}}
        NoticeCard("Las ilustraciones enseñan el concepto morfológico; el diagnóstico real puede requerir odontograma, fotografías, radiografías, antecedentes y evaluación genética/medicina cuando corresponda.")
    }
}

@Composable private fun ToothAnomalyIllustrationV40(kind:String){
    Canvas(Modifier.fillMaxWidth().height(250.dp).padding(8.dp)){
        val w=size.width;val h=size.height
        fun tooth(cx:Float,scale:Float=1f,angle:Float=0f,low:Float=0f){rotate(angle,Offset(cx,h*.50f)){val p=Path().apply{moveTo(cx-w*.07f*scale,h*(.18f+low));quadraticBezierTo(cx,h*(.10f+low),cx+w*.07f*scale,h*(.18f+low));lineTo(cx+w*.05f*scale,h*(.44f+low));lineTo(cx+w*.025f*scale,h*(.82f+low));quadraticBezierTo(cx,h*(.91f+low),cx-w*.025f*scale,h*(.82f+low));lineTo(cx-w*.05f*scale,h*(.44f+low));close()};drawPath(p,Color(0xFFFFF1D4));drawPath(p,Color(0xFF7B6D58),style=Stroke(3f))}}
        when(kind){
            "missing"->{tooth(w*.38f);tooth(w*.62f);drawCircle(Color(0xFFCC5560).copy(alpha=.25f),w*.07f,Offset(w*.50f,h*.25f))}
            "extra"->{tooth(w*.32f);tooth(w*.50f,.75f);tooth(w*.68f)}
            "small"->{tooth(w*.40f);tooth(w*.60f,.55f)}
            "large"->{tooth(w*.38f);tooth(w*.62f,1.45f)}
            "double"->{tooth(w*.46f,1.25f);tooth(w*.54f,1.25f)}
            "tilt"->{tooth(w*.40f);tooth(w*.62f,1f,-35f)}
            "swap"->{tooth(w*.40f,.8f,15f);tooth(w*.62f,1.1f,-15f);drawLine(Color(0xFF3C7DB5),Offset(w*.40f,h*.12f),Offset(w*.62f,h*.12f),strokeWidth=4f)}
            "delay"->{tooth(w*.38f);tooth(w*.62f,1f,0f,.20f);drawLine(Color(0xFFE69AA0),Offset(w*.20f,h*.32f),Offset(w*.80f,h*.32f),strokeWidth=7f)}
            "low"->{tooth(w*.38f);tooth(w*.62f,1f,0f,.10f)}
            "early"->{tooth(w*.50f,.75f);drawLine(Color(0xFFE69AA0),Offset(w*.20f,h*.42f),Offset(w*.80f,h*.42f),strokeWidth=7f)}
            else->{tooth(w*.40f);tooth(w*.60f)}
        }
    }
}

private data class HabitV40(val name:String,val extra:String,val intra:String,val management:String)
private val habitsV40=listOf(
    HabitV40("Succión digital","Callosidad/dedo irritado, patrón facial según duración/intensidad.","Mordida abierta anterior, overjet aumentado, arco superior estrecho en algunos casos.","Educación, refuerzo positivo, identificar contexto; aparatos sólo cuando están indicados y con valoración ortodóncica."),
    HabitV40("Chupón","Observar frecuencia, duración y si es nocturno.","Cambios oclusales similares a succión según intensidad/duración.","Retiro gradual acorde a edad/desarrollo, apoyo familiar y control de oclusión."),
    HabitV40("Interposición/proyección lingual","Patrón de deglución/habla y postura lingual.","Mordida abierta/proinclinación o espacios en algunos casos.","Identificar causa; terapia miofuncional/fonoaudiología y ortodoncia cuando proceda."),
    HabitV40("Respiración bucal","Labios entreabiertos, sequedad, postura de cabeza; confirmar causa respiratoria.","Mucosa seca, gingivitis anterior, posible patrón maxilar; no diagnosticar obstrucción por apariencia.","Derivar a medicina/ORL cuando se sospeche obstrucción; manejo dental de sequedad/higiene y ortodoncia según caso."),
    HabitV40("Onicofagia","Uñas cortas/trauma periungueal.","Desgaste/incisivos, microtrauma o restauraciones afectadas.","Concientización, sustitución de hábito, manejo conductual; férula sólo si existe indicación independiente."),
    HabitV40("Bruxismo","Hipertrofia/tensión muscular, cefalea referida, dolor matutino.","Facetas de desgaste, fracturas/restauraciones, movilidad o sensibilidad; desgaste no prueba por sí solo bruxismo actual.","Educación, sueño/estrés, protección dental y manejo TTM según diagnóstico; evitar tratamientos irreversibles de rutina."),
    HabitV40("Mordisqueo de labio/mejilla","Marcas o trauma externo ocasional.","Línea alba, morsicatio, queratosis/fricción.","Identificar disparadores, explicar naturaleza reactiva y retirar trauma; reevaluar si persiste."),
    HabitV40("Morder objetos","Observar lápices, hielo u objetos referidos.","Fisuras, desgaste o fracturas en zonas de carga.","Eliminar hábito, educación y valorar daño/restaurabilidad."))

@Composable
fun HabitsTeachingV40Screen(lang:String,onBack:()->Unit){
    var selected by remember{mutableStateOf(0)}
    ResponsiveScreenV17("Hábitos y parafunciones","Toca cada hábito para ver identificación extraoral, intraoral y opciones de manejo.",onBack){profile->
        AdaptiveGridV17(habitsV40.size,if(profile.largeSystemText)1 else 2){i->val h=habitsV40[i];Card(onClick={selected=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(selected==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text(h.name,fontWeight=FontWeight.Black);if(selected==i){Text("Extraoral: ${h.extra}");Text("Intraoral: ${h.intra}");Text("Manejo: ${h.management}")}}}}
        NoticeCard("Registrar inicio, frecuencia, duración, intensidad/contexto y si el hábito ya se suspendió. No todos los hallazgos son causados por un único hábito.")
    }
}
