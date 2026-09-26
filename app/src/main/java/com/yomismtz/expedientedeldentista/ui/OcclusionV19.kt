package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import com.yomismtz.expedientedeldentista.R

private data class OcclusionTopic19(val es:String,val en:String,val bodyEs:String,val bodyEn:String)

@Composable
fun OcclusionInteractiveV19Screen(lang:String,onBack:()->Unit) {
    val sections=listOf(
        "Dentición y desarrollo","Planos terminales","Relación molar de Angle","Relación canina","Overjet","Overbite / mordida abierta",
        "Mordida cruzada","Líneas medias","Alineación y espacios","Forma y simetría de arcadas","Plano vertical","Plano transversal",
        "Contactos oclusales","Máxima intercuspidación y cierre","Desgaste oclusal","Alteraciones asociadas"
    )
    var selected by rememberRecordState("occlusion.selected",0)
    var choice by rememberRecordState("occlusion.choice","")
    val options=listOf(
        listOf("Temporal","Mixta temprana","Mixta tardía","Permanente","No valorable"),
        listOf("Recto bilateral","Mesial bilateral","Distal bilateral","Asimétrico","No valorable"),
        listOf("Clase I bilateral","Clase II bilateral","Clase III bilateral","Asimétrica derecha/izquierda","No valorable"),
        listOf("Clase I bilateral","Clase II bilateral","Clase III bilateral","Asimétrica","No valorable"),
        listOf("Positivo habitual","Aumentado","Reducido","Borde a borde","Invertido","No medido"),
        listOf("Traslape habitual","Profunda","Borde a borde","Abierta anterior","Abierta posterior","No valorable"),
        listOf("Sin mordida cruzada","Anterior","Posterior derecha","Posterior izquierda","Posterior bilateral","Con desplazamiento funcional"),
        listOf("Coincidentes","Superior desviada derecha","Superior desviada izquierda","Inferior desviada derecha","Inferior desviada izquierda","Discrepancia superior-inferior"),
        listOf("Sin alteración aparente","Apiñamiento leve","Apiñamiento moderado","Apiñamiento severo","Diastemas/espacios","Rotaciones/inclinaciones"),
        listOf("Ovoide simétrica","Triangular","Cuadrada/amplia","Estrecha","Asimétrica"),
        listOf("Curva de Spee discreta","Curva aumentada","Curva plana","Mordida profunda","Mordida abierta"),
        listOf("Relación transversal habitual","Cruzada unilateral","Cruzada bilateral","Mordida en tijera/Brodie","Asimetría transversal"),
        listOf("Posteriores bilaterales","Predominio derecho","Predominio izquierdo","Contacto prematuro aparente","Ausencia de contacto posterior"),
        listOf("Cierre sin desplazamiento","Deslizamiento funcional derecho","Deslizamiento funcional izquierdo","Discrepancia RC/MI aparente","No valorable"),
        listOf("Sin desgaste aparente","Facetas anteriores","Facetas posteriores","Generalizado","Unilateral","Severo"),
        listOf("Sin alteración adicional","Protrusión incisiva","Mordida invertida/underbite","Apiñamiento","Espaciamiento","Mordida profunda","Mordida abierta")
    )
    val help=listOf(
        "Identifica la etapa eruptiva antes de interpretar las relaciones oclusales.",
        "En dentición temporal compara por separado las caras distales de los segundos molares temporales.",
        "Evalúa derecha e izquierda. La cúspide mesiovestibular del primer molar superior es la referencia clásica de Angle.",
        "Compara la cúspide del canino superior con la región canino-primer premolar inferior en ambos lados.",
        "Mide horizontalmente entre incisivos en milímetros; registra también borde a borde o relación invertida.",
        "Valora el traslape vertical anterior en milímetros o porcentaje y si existe mordida abierta o contacto traumático.",
        "Determina anterior/posterior, lado y si al cierre aparece desplazamiento funcional mandibular.",
        "Compara línea media facial, superior e inferior; registra dirección y milímetros de desviación.",
        "Observa ambas arcadas: apiñamiento, espacios, diastemas, rotaciones, inclinaciones y desplazamientos.",
        "Describe cada arcada por separado y compara simetría derecha-izquierda.",
        "Valora curva de Spee, profundidad de mordida, apertura anterior/posterior y contactos verticales.",
        "Evalúa anchura relativa de las arcadas, cruzada posterior y mordida en tijera.",
        "Observa simultaneidad y distribución de contactos; un hallazgo visual no demuestra por sí solo una interferencia funcional.",
        "Observa la trayectoria desde apertura hasta máxima intercuspidación y cualquier desplazamiento mandibular.",
        "Registra localización y extensión de facetas; no diagnostiques bruxismo únicamente por desgaste.",
        "Resume las alteraciones oclusales visibles sin sustituir el diagnóstico ortodóncico completo."
    )
    val photos=listOf(
        Pair("Dentición temporal · referencia real",R.drawable.occlusion_primary),
        Pair("Plano terminal · apoyo esquemático",0),
        Pair("Angle Clase I · fotografía clínica real",R.drawable.occlusion_angle1),
        Pair("Angle Clase II · fotografía clínica real",R.drawable.occlusion_angle2),
        Pair("Overjet y overbite · técnica de medición",R.drawable.occlusion_overjet),
        Pair("Mordida abierta anterior · fotografía clínica real",R.drawable.occlusion_openbite),
        Pair("Mordida cruzada · referencia clínica",R.drawable.occlusion_crossbite),
        Pair("Plano oclusal y asimetría · fotografía clínica real",R.drawable.occlusion_canted),
        Pair("Apiñamiento severo · fotografía clínica real",R.drawable.occlusion_crowding),
        Pair("Arcadas · valorar forma y simetría",R.drawable.occlusion_crowding),
        Pair("Mordida profunda · fotografía clínica real",R.drawable.occlusion_deepbite),
        Pair("Relación transversal · referencia clínica",R.drawable.occlusion_crossbite),
        Pair("Contactos oclusales · referencia clínica",R.drawable.occlusion_angle1),
        Pair("Máxima intercuspidación · referencia clínica",R.drawable.occlusion_angle1),
        Pair("Desgaste: inspección clínica",R.drawable.occlusion_deepbite),
        Pair("Diastema · fotografía clínica real",R.drawable.occlusion_diastema)
    )
    ResponsiveScreenV17(tr(lang,"Examen clínico de oclusión","Clinical occlusal examination"),tr(lang,"Exploración por subapartados con registro seleccionable y apoyo visual.","Sectioned examination with selectable findings and visual support."),onBack) {
        ResponsiveSectionV17("Subapartados") {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                sections.forEachIndexed{i,s->FilterChip(selected==i,{selected=i;choice=""},{Text(s)})}
            }
        }
        ResponsiveSectionV17(sections[selected]) {
            Text(help[selected],fontWeight=FontWeight.SemiBold)
            val ph=photos[selected]
            if(ph.second!=0) OcclusionPhoto19(ph.first,ph.second) else TerminalPlanes19(lang)
            Text("Registro clínico",fontWeight=FontWeight.Black)
            options[selected].forEach{o->FilterChip(choice==o,{choice=o},{Text(o)},modifier=Modifier.fillMaxWidth())}
            when(selected){
                1->TerminalPlanes19(lang)
                2->AngleMolar19()
                3->Canine19(lang)
                4->Overjet19(lang)
                5->Overbite19(lang)
                6->Crossbite19(lang)
            }
        }
        NoticeCard(tr(lang,"Los espacios visuales clínicos están preparados para recursos locales del APK. Mientras un recurso siga pendiente de integración, no debe interpretarse el marcador visual como fotografía clínica. Los esquemas de medición se identifican como esquemas. Registrar hallazgos no equivale a emitir automáticamente un diagnóstico.","Clinical visual slots are prepared for local APK resources. While a resource is still pending integration, its visual placeholder must not be interpreted as a clinical photograph. Measurement diagrams are identified as diagrams. Recording findings does not automatically establish a diagnosis."))
    }
}

@Composable private fun OcclusionPhoto19(title:String,@DrawableRes resource:Int){
 LocalZoomableImageV21(title=title,resource=resource,attribution="Recurso visual local del APK · verificar imagen clínica definitiva antes de uso docente.")
}

@Composable
private fun MiniOcclusionCard19(title:String,drawing:@Composable ()->Unit) {
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.35f))) {
        Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
            Text(title,modifier=Modifier.fillMaxWidth(),fontWeight=FontWeight.Black,textAlign=TextAlign.Center)
            drawing()
        }
    }
}

private fun DrawScope.toothBlock19(x:Float,y:Float,w:Float,h:Float,color:Color,outline:Color) {
    val p=Path().apply {
        moveTo(x+w*.10f,y+h*.10f)
        quadraticBezierTo(x+w*.50f,y-h*.04f,x+w*.90f,y+h*.10f)
        lineTo(x+w*.84f,y+h*.86f)
        quadraticBezierTo(x+w*.50f,y+h,x+w*.16f,y+h*.86f)
        close()
    }
    drawPath(p,color); drawPath(p,outline,style=Stroke(2f))
}

@Composable
private fun TerminalPlanes19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        MiniOcclusionCard19(tr(lang,"Plano terminal recto","Flush terminal plane")) { TerminalPlaneDrawing19(0f) }
        MiniOcclusionCard19(tr(lang,"Escalón mesial","Mesial step")) { TerminalPlaneDrawing19(-.12f) }
        MiniOcclusionCard19(tr(lang,"Escalón distal","Distal step")) { TerminalPlaneDrawing19(.12f) }
    }
}

@Composable
private fun TerminalPlaneDrawing19(lowerShift:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer; val lower=MaterialTheme.colorScheme.secondaryContainer
    val outline=MaterialTheme.colorScheme.outline; val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(120.dp)) {
        val w=size.width; val h=size.height; val tw=w*.19f; val th=h*.30f; val x=w*.40f
        toothBlock19(x,h*.16f,tw,th,upper,outline)
        val lx=x+w*lowerShift
        toothBlock19(lx,h*.58f,tw,th,lower,outline)
        drawLine(accent,Offset(x+tw,h*.10f),Offset(x+tw,h*.52f),strokeWidth=4f)
        drawLine(accent.copy(alpha=.65f),Offset(lx+tw,h*.53f),Offset(lx+tw,h*.95f),strokeWidth=4f)
    }
}

@Composable
private fun AngleMolar19() {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        MiniOcclusionCard19("Angle I") { MolarDrawing19(0f) }
        MiniOcclusionCard19("Angle II") { MolarDrawing19(.13f) }
        MiniOcclusionCard19("Angle III") { MolarDrawing19(-.13f) }
    }
}

@Composable
private fun MolarDrawing19(lowerShift:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer; val lower=MaterialTheme.colorScheme.secondaryContainer
    val outline=MaterialTheme.colorScheme.outline; val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val w=size.width; val h=size.height; val tw=w*.25f; val th=h*.34f; val x=w*.38f
        toothBlock19(x,h*.12f,tw,th,upper,outline)
        val lx=x+w*lowerShift
        toothBlock19(lx,h*.58f,tw,th,lower,outline)
        drawCircle(accent,7f,Offset(x+tw*.35f,h*.46f)); drawCircle(accent.copy(alpha=.65f),7f,Offset(lx+tw*.55f,h*.58f))
        drawLine(accent,Offset(x+tw*.35f,h*.46f),Offset(lx+tw*.55f,h*.58f),strokeWidth=3f)
    }
}

@Composable
private fun Canine19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        MiniOcclusionCard19(tr(lang,"Canina Clase I","Canine Class I")) { CanineDrawing19(0f) }
        MiniOcclusionCard19(tr(lang,"Canina Clase II","Canine Class II")) { CanineDrawing19(.12f) }
        MiniOcclusionCard19(tr(lang,"Canina Clase III","Canine Class III")) { CanineDrawing19(-.12f) }
    }
}

@Composable
private fun CanineDrawing19(lowerShift:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer; val lower=MaterialTheme.colorScheme.secondaryContainer
    val outline=MaterialTheme.colorScheme.outline; val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val w=size.width; val h=size.height; val ux=w*.48f
        val canine=Path().apply { moveTo(ux-w*.08f,h*.15f); lineTo(ux+w*.08f,h*.15f); lineTo(ux,h*.48f); close() }
        drawPath(canine,upper); drawPath(canine,outline,style=Stroke(2f))
        val base=w*(.40f+lowerShift)
        toothBlock19(base,h*.60f,w*.14f,h*.25f,lower,outline); toothBlock19(base+w*.15f,h*.60f,w*.14f,h*.25f,lower,outline)
        drawLine(accent,Offset(ux,h*.48f),Offset(base+w*.145f,h*.60f),strokeWidth=4f)
    }
}

@Composable
private fun Overjet19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        MiniOcclusionCard19(tr(lang,"Overjet positivo / aumentado","Positive / increased overjet")) { IncisorHorizontal19(.14f) }
        MiniOcclusionCard19(tr(lang,"Borde a borde","Edge-to-edge")) { IncisorHorizontal19(0f) }
        MiniOcclusionCard19(tr(lang,"Overjet invertido","Reverse overjet")) { IncisorHorizontal19(-.12f) }
    }
}

@Composable
private fun IncisorHorizontal19(shift:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer; val lower=MaterialTheme.colorScheme.secondaryContainer
    val outline=MaterialTheme.colorScheme.outline; val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val w=size.width; val h=size.height; val iw=w*.13f
        toothBlock19(w*.42f,h*.10f,iw,h*.42f,upper,outline); toothBlock19(w*(.42f+shift),h*.58f,iw,h*.34f,lower,outline)
        val x1=w*.485f; val x2=w*(.485f+shift); val y=h*.54f
        drawLine(accent,Offset(x1,y),Offset(x2,y),strokeWidth=5f)
    }
}

@Composable
private fun Overbite19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        MiniOcclusionCard19(tr(lang,"Overbite habitual","Usual overbite")) { IncisorVertical19(.30f) }
        MiniOcclusionCard19(tr(lang,"Sobremordida profunda","Deep overbite")) { IncisorVertical19(.62f) }
        MiniOcclusionCard19(tr(lang,"Mordida abierta","Open bite")) { IncisorVertical19(-.18f) }
    }
}

@Composable
private fun IncisorVertical19(overlap:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer; val lower=MaterialTheme.colorScheme.secondaryContainer
    val outline=MaterialTheme.colorScheme.outline; val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(135.dp)) {
        val w=size.width; val h=size.height; val iw=w*.13f
        toothBlock19(w*.42f,h*.08f,iw,h*.45f,upper,outline)
        val lowerY=h*(.52f-overlap*.24f)
        toothBlock19(w*.44f,lowerY,iw,h*.34f,lower,outline)
        drawLine(accent,Offset(w*.60f,h*.53f),Offset(w*.60f,lowerY),strokeWidth=4f)
    }
}

@Composable
private fun Crossbite19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        MiniOcclusionCard19(tr(lang,"Relación transversal habitual","Usual transverse relation")) { CrossbiteDrawing19(false) }
        MiniOcclusionCard19(tr(lang,"Mordida cruzada posterior","Posterior crossbite")) { CrossbiteDrawing19(true) }
    }
}

@Composable
private fun CrossbiteDrawing19(cross:Boolean) {
    val upper=MaterialTheme.colorScheme.primaryContainer; val lower=MaterialTheme.colorScheme.secondaryContainer
    val outline=MaterialTheme.colorScheme.outline; val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(135.dp)) {
        val w=size.width; val h=size.height
        val upperLeft=if(cross)w*.37f else w*.31f; val upperRight=if(cross)w*.63f else w*.69f
        toothBlock19(upperLeft-w*.07f,h*.18f,w*.14f,h*.30f,upper,outline); toothBlock19(upperRight-w*.07f,h*.18f,w*.14f,h*.30f,upper,outline)
        toothBlock19(w*.36f,h*.58f,w*.14f,h*.27f,lower,outline); toothBlock19(w*.50f,h*.58f,w*.14f,h*.27f,lower,outline)
        drawLine(accent,Offset(w*.50f,h*.10f),Offset(w*.50f,h*.92f),strokeWidth=2.5f)
    }
}
