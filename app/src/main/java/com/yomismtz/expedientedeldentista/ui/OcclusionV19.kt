package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private data class OcclusionTopic19(val es:String,val en:String,val bodyEs:String,val bodyEn:String)

@Composable
fun OcclusionInteractiveV19Screen(lang:String,onBack:()->Unit) {
    var selected by remember{mutableStateOf(0)}
    val topics=listOf(
        OcclusionTopic19("Planos terminales","Terminal planes","En dentición temporal compara las caras distales de los segundos molares: plano terminal recto (flush), escalón mesial y escalón distal.","In primary dentition compare distal surfaces of second molars: flush terminal plane, mesial step and distal step."),
        OcclusionTopic19("Angle molar","Molar Angle class","Relaciona los primeros molares permanentes. Los dibujos son esquemas de referencia para Clase I, II y III.","Relate the permanent first molars. The drawings are reference schematics for Class I, II and III."),
        OcclusionTopic19("Relación canina","Canine relation","Relaciona la cúspide del canino superior con la región canino-primer premolar inferior; describe Clase I, II o III.","Relate the upper canine cusp to the lower canine-first premolar region; describe Class I, II or III."),
        OcclusionTopic19("Overjet","Overjet","Distancia horizontal entre incisivos superiores e inferiores. Registra milímetros y si es positivo, borde a borde o invertido.","Horizontal distance between upper and lower incisors. Record millimeters and whether positive, edge-to-edge or reversed."),
        OcclusionTopic19("Overbite / abierta","Overbite / open bite","Traslape vertical anterior. Registra milímetros o porcentaje y reconoce sobremordida profunda, borde a borde y mordida abierta.","Anterior vertical overlap. Record millimeters or percentage and recognize deep bite, edge-to-edge and open bite."),
        OcclusionTopic19("Mordida cruzada","Crossbite","Describe si la relación cruzada es anterior o posterior y unilateral o bilateral; valora línea media y posibles desplazamientos funcionales.","Describe anterior/posterior and unilateral/bilateral crossbite; assess midline and possible functional shifts.")
    )

    ResponsiveScreenV17(tr(lang,"Examen de oclusión","Occlusal examination"),tr(lang,"Selecciona un concepto para ver una lámina esquemática propia y su significado.","Select a concept to see an original schematic and its meaning."),onBack) { profile ->
        ResponsiveSectionV17(tr(lang,"Conceptos","Concepts")) {
            AdaptiveGridV17(topics.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 3) { i ->
                FilterChip(selected==i,{selected=i},{Text(if(lang=="en")topics[i].en else topics[i].es)},modifier=Modifier.fillMaxWidth())
            }
        }
        val t=topics[selected]
        ResponsiveSectionV17(if(lang=="en")t.en else t.es) {
            when(selected) {
                0 -> TerminalPlanes19(lang)
                1 -> AngleMolar19()
                2 -> Canine19(lang)
                3 -> Overjet19(lang)
                4 -> Overbite19(lang)
                else -> Crossbite19(lang)
            }
            Text(if(lang=="en")t.bodyEn else t.bodyEs,fontWeight=FontWeight.SemiBold)
        }
        NoticeCard(tr(lang,"Dibujos originales de YSM Expediente. Base conceptual: Columbia University para planos terminales y definiciones ortodóncicas convencionales para Angle, overjet, overbite y mordida cruzada. Correlaciona con el examen clínico y criterios docentes.","Original YSM Expediente drawings. Conceptual basis: Columbia University for terminal planes and conventional orthodontic definitions for Angle, overjet, overbite and crossbite. Correlate with clinical examination and faculty criteria."))
    }
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
