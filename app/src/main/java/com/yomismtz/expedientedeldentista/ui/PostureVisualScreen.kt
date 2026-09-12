package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class VisualPosture(val nameEs:String,val nameEn:String,val descriptionEs:String,val descriptionEn:String,val headDx:Float,val headDy:Float,val angle:Float,val curve:Float)

@Composable
fun PostureVisualScreen(lang:String,onBack:()->Unit) {
    var selected by remember { mutableStateOf(0) }
    val list=listOf(
        VisualPosture("Natural / equilibrada","Natural / balanced","Cabeza erguida con relación visual equilibrada respecto al cuello. Se usa como referencia descriptiva.","Upright head with a visually balanced relation to the neck. Used as a descriptive reference.",0f,0f,0f,0.12f),
        VisualPosture("Cabeza adelantada","Forward head posture","El cráneo se observa desplazado hacia anterior respecto al tronco; describe la postura y busca compensaciones cervicales.","The head appears translated anteriorly relative to the trunk; describe the posture and look for cervical compensation.",28f,5f,0f,0.18f),
        VisualPosture("Flexión","Flexion","El mentón se orienta hacia abajo por rotación de la cabeza. Distingue una postura sostenida de una inclinación accidental de la fotografía.","The chin rotates downward. Distinguish sustained posture from accidental photo positioning.",0f,8f,18f,0.10f),
        VisualPosture("Extensión","Extension","El mentón se eleva y la cabeza rota hacia atrás. Describe el hallazgo sin asumir una causa.","The chin elevates and the head rotates backward. Describe the finding without assuming a cause.",0f,-5f,-18f,0.17f),
        VisualPosture("Rectificación cervical","Cervical straightening","La curvatura cervical se aprecia disminuida. Requiere correlación con exploración y estudios adecuados.","The cervical curve appears reduced. Correlate with examination and appropriate studies.",0f,0f,0f,0.02f),
        VisualPosture("Lordosis cervical aumentada","Increased cervical lordosis","La curvatura cervical se observa más pronunciada. Es una descripción postural, no un diagnóstico etiológico aislado.","The cervical curve appears more pronounced. It is a postural description, not an isolated etiologic diagnosis.",0f,0f,0f,0.27f)
    )
    val p=list[selected]
    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Postura craneocervical · guía visual","Craniocervical posture · visual guide"),onBack,
            tr(lang,"Selecciona una postura para comparar el esquema y aprender cómo describirla.","Select a posture to compare the diagram and learn how to describe it.")) }
        item { Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
            list.take(3).forEachIndexed{i,v->FilterChip(selected==i,{selected=i},{Text(if(lang=="en")v.nameEn else v.nameEs)},modifier=Modifier.weight(1f))}
        } }
        item { Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
            list.drop(3).forEachIndexed{i,v->val n=i+3;FilterChip(selected==n,{selected=n},{Text(if(lang=="en")v.nameEn else v.nameEs)},modifier=Modifier.weight(1f))}
        } }
        item {
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text(if(lang=="en")p.nameEn else p.nameEs,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleLarge)
                    PostureFigure(p)
                    Text(if(lang=="en")p.descriptionEn else p.descriptionEs)
                }
            }
        }
        item { SectionCard(tr(lang,"Perfil facial","Facial profile")) {
            Text("• ${tr(lang,"Recto: frente, labios y mentón se observan relativamente equilibrados.","Straight: forehead, lips and chin appear relatively balanced.")}")
            Text("• ${tr(lang,"Convexo: el mentón se aprecia relativamente retruido.","Convex: the chin appears relatively retruded.")}")
            Text("• ${tr(lang,"Cóncavo: el mentón se aprecia relativamente prominente/adelantado.","Concave: the chin appears relatively prominent/forward.")}")
        } }
        item { NoticeCard(tr(lang,"La imagen es un esquema original para enseñanza. No sustituye análisis cefalométrico, valoración funcional ni diagnóstico de alteraciones cervicales.","The image is an original teaching schematic. It does not replace cephalometric, functional or cervical assessment.")) }
    }
}

@Composable
private fun PostureFigure(p:VisualPosture) {
    val primary=MaterialTheme.colorScheme.primary
    val outline=MaterialTheme.colorScheme.outline
    Canvas(Modifier.fillMaxWidth().height(260.dp)) {
        val w=size.width;val h=size.height
        val shoulderY=h*.82f
        drawLine(outline,Offset(w*.2f,shoulderY),Offset(w*.8f,shoulderY),strokeWidth=6f)
        val spine=Path().apply {
            moveTo(w*.5f,shoulderY)
            cubicTo(w*(.5f-p.curve),h*.65f,w*(.5f+p.curve),h*.48f,w*.5f,h*.34f)
        }
        drawPath(spine,primary,style=Stroke(8f))
        val cx=w*.5f+p.headDx;val cy=h*.22f+p.headDy
        drawCircle(MaterialTheme.colorScheme.primaryContainer,radius=w*.12f,center=Offset(cx,cy))
        drawCircle(outline,radius=w*.12f,center=Offset(cx,cy),style=Stroke(4f))
        val r=Math.toRadians(p.angle.toDouble())
        val chin=Offset(cx+(w*.115f*kotlin.math.cos(r).toFloat()),cy+(w*.115f*kotlin.math.sin(r).toFloat()))
        drawLine(primary,Offset(cx,cy),chin,strokeWidth=5f)
        drawCircle(primary,radius=6f,center=chin)
        drawLine(outline,Offset(w*.5f,h*.34f),Offset(cx,cy+w*.12f),strokeWidth=5f)
    }
}
