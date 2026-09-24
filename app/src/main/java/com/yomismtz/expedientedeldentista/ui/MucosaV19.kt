package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class MucosaZone19(
    val id:String,val es:String,val en:String,
    val normalEs:String,val normalEn:String,
    val changesEs:String,val changesEn:String,
    val exploreEs:String,val exploreEn:String
)

private val zones19=listOf(
    MucosaZone19("labio_sup","Labio superior / bermellón","Upper lip / vermilion","Rosado, íntegro e hidratado.","Pink, intact and hydrated.","Fisuras, costras, vesículas, úlceras, edema, pigmentación o resequedad.","Fissures, crusts, vesicles, ulcers, edema, pigmentation or dryness.","Inspecciona y eversa el labio; palpa si hay aumento de volumen o induración.","Inspect and evert the lip; palpate swelling or induration."),
    MucosaZone19("labio_inf","Labio inferior / mucosa labial","Lower lip / labial mucosa","Rosado, húmedo, blando e íntegro.","Pink, moist, soft and intact.","Mucocele, úlcera, fisura, edema, pigmentación o resequedad.","Mucocele, ulcer, fissure, edema, pigmentation or dryness.","Eversa y revisa fondo de vestíbulo, frenillo y glándulas menores.","Evert and inspect vestibule, frenum and minor glands."),
    MucosaZone19("carrillo_der","Carrillo derecho / mucosa yugal","Right buccal mucosa","Rosada, húmeda y lisa; salida parotídea sin alteración aparente.","Pink, moist and smooth; parotid opening without apparent change.","Línea alba, mordisqueo, úlcera, placa blanca/roja, masa o cambio salival.","Linea alba, biting, ulcer, white/red plaque, mass or salivary change.","Retrae con espejo y recorre de comisura a región posterior.","Retract with a mirror and inspect from commissure posteriorly."),
    MucosaZone19("carrillo_izq","Carrillo izquierdo / mucosa yugal","Left buccal mucosa","Rosada, húmeda y lisa, sin induración.","Pink, moist and smooth, without induration.","Línea alba, mordisqueo, úlcera, placa, masa o pigmentación atípica.","Linea alba, biting, ulcer, plaque, mass or atypical pigmentation.","Compara ambos carrillos y palpa si hay masa.","Compare both cheeks and palpate if a mass is present."),
    MucosaZone19("encia","Encía y mucosa alveolar","Gingiva and alveolar mucosa","Encía firme y de contorno regular; mucosa alveolar móvil y húmeda.","Firm gingiva with regular contour; alveolar mucosa mobile and moist.","Eritema, edema, sangrado, recesión, fístula, úlcera o aumento de volumen.","Erythema, edema, bleeding, recession, fistula, ulcer or swelling.","Inspecciona encía marginal, papilar y adherida; correlaciona con sondaje.","Inspect marginal, papillary and attached gingiva; correlate with probing."),
    MucosaZone19("paladar_duro","Paladar duro","Hard palate","Rosado pálido, firme y queratinizado; rugas y rafe reconocibles.","Pale pink, firm and keratinized; rugae and raphe recognizable.","Torus, petequias, placa, úlcera, eritema, quemadura o masa.","Torus, petechiae, plaque, ulcer, erythema, burn or mass.","Ilumina directamente y palpa elevaciones.","Use direct light and palpate elevations."),
    MucosaZone19("paladar_blando","Paladar blando, úvula y pilares","Soft palate, uvula and pillars","Rosado y móvil, con elevación simétrica y úvula centrada.","Pink and mobile, with symmetric elevation and centered uvula.","Eritema, petequias, exudado, úlcera, asimetría o desviación de úvula.","Erythema, petechiae, exudate, ulcer, asymmetry or uvular deviation.","Pide abrir y fonar; observa movilidad, pilares y orofaringe.","Ask the patient to open and phonate; observe mobility, pillars and oropharynx."),
    MucosaZone19("lengua","Lengua: dorso, bordes y cara ventral","Tongue: dorsum, borders and ventral surface","Rosada, papilada, húmeda, móvil y sin induración.","Pink, papillary, moist, mobile and without induration.","Saburra, depapilación, fisuras, placa blanca/roja, úlcera, masa o induración.","Coating, depapillation, fissures, white/red plaque, ulcer, mass or induration.","Protruye, desplaza con gasa y revisa dorso, bordes y cara ventral.","Protrude, move with gauze and inspect dorsum, borders and ventral surface."),
    MucosaZone19("piso","Piso de boca","Floor of mouth","Rosado, blando y húmedo, sin masas ni aumento de volumen.","Pink, soft and moist, without masses or swelling.","Ránula, masa, coloración azul/roja, úlcera, dolor o disminución salival.","Ranula, mass, blue/red change, ulcer, pain or reduced salivary flow.","Eleva la lengua y realiza palpación bimanual cuando esté indicada.","Lift the tongue and use bimanual palpation when indicated."),
    MucosaZone19("orofaringe","Orofaringe / amígdalas","Oropharynx / tonsils","Pilares sin inflamación evidente, sin exudado ni lesión aparente.","Pillars without evident inflammation, exudate or apparent lesion.","Eritema, exudado, hipertrofia, placa, asimetría o ulceración.","Erythema, exudate, hypertrophy, plaque, asymmetry or ulceration.","Inspecciona con luz y depresor cuando proceda, evitando reflejo nauseoso innecesario.","Inspect with light and depressor when appropriate, avoiding unnecessary gag reflex.")
)

@Composable
fun MucosaInteractiveV19Screen(lang:String,onBack:()->Unit) {
    var selectedId by remember{mutableStateOf("lengua")}
    var tab by remember{mutableStateOf(0)}
    var finding by remember{mutableStateOf("Normal")}
    var sizeMm by remember{mutableStateOf("5 mm")}
    var notes by remember{mutableStateOf("Sin observaciones adicionales")}
    var color by remember{mutableStateOf("Rosado")}
    var shape by remember{mutableStateOf("Redonda/oval")}
    var surface by remember{mutableStateOf("Lisa")}
    var border by remember{mutableStateOf("Regular/definido")}
    var base by remember{mutableStateOf("Sésil")}
    var consistency by remember{mutableStateOf("Blanda")}
    var mobility by remember{mutableStateOf("Móvil")}
    var symptoms by remember{mutableStateOf("Asintomática")}
    var duration by remember{mutableStateOf("No referido")}
    var evolution by remember{mutableStateOf("No referida")}
    var lesionHelp by remember{mutableStateOf(false)}
    var count by remember{mutableStateOf("Única")}
    val selected=zones19.first{it.id==selectedId}
    val name=if(lang=="en")selected.en else selected.es
    val example=if(finding=="Normal") "$name: ${if(lang=="en")selected.normalEn else selected.normalEs}"
    else tr(lang,"$name: $finding; ${if(count=="Única")"lesión única" else "lesiones múltiples"}; tamaño ${sizeMm}; color $color; forma $shape; superficie $surface; borde $border; base $base; consistencia $consistency; movilidad $mobility; $symptoms; duración $duration; evolución $evolution${if(notes.isBlank())"" else "; $notes"}. Descripción clínica; correlacionar antes de diagnosticar.","$name: $finding; size ${if(sizeMm.isBlank())"not entered" else "$sizeMm mm"}; color $color; shape $shape; surface $surface; border $border; base $base; consistency $consistency; mobility $mobility; symptoms $symptoms; duration $duration; evolution $evolution. Clinical description; correlate before diagnosis.")

    ResponsiveScreenV17(tr(lang,"Mucosas orales interactivas","Interactive oral mucosa"),tr(lang,"Toca una zona en la boca abierta y practica una descripción clínica sistemática.","Tap a region on the open-mouth diagram and practice systematic clinical description."),onBack) { profile ->
        ResponsiveSectionV17(tr(lang,"1 · Boca abierta: toca una zona","1 · Open mouth: tap a region")) {
            OpenMouthMap19(selectedId){selectedId=it}
            Text("${tr(lang,"Zona seleccionada","Selected region")}: $name",fontWeight=FontWeight.Black,color=MaterialTheme.colorScheme.primary)
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)) {
                zones19.forEach { z -> FilterChip(selectedId==z.id,{selectedId=z.id},{Text(if(lang=="en")z.en else z.es)}) }
            }
        }
        ResponsiveSectionV17(name) {
            AdaptiveGridV17(3,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 3) { i ->
                val labels=listOf(tr(lang,"Normal","Normal"),tr(lang,"Alteraciones","Changes"),tr(lang,"Cómo explorar","How to examine"))
                FilterChip(tab==i,{tab=i},{Text(labels[i])},modifier=Modifier.fillMaxWidth())
            }
            val detail=when(tab){1->if(lang=="en")selected.changesEn else selected.changesEs;2->if(lang=="en")selected.exploreEn else selected.exploreEs;else->if(lang=="en")selected.normalEn else selected.normalEs}
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) { Text(detail,Modifier.padding(12.dp)) }
        }
        ResponsiveSectionV17(tr(lang,"2 · Registro rápido","2 · Quick description")) {
            Button(onClick={lesionHelp=true},modifier=Modifier.fillMaxWidth()){Text("❓ Ayuda · Lesiones elementales de mucosa oral")}
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)) {
                listOf("Normal","Úlcera","Placa blanca","Placa roja","Eritema","Aumento de volumen","Pigmentación","Vesícula","Fístula","Masa").forEach { f -> FilterChip(finding==f,{finding=f},{Text(f)}) }
            }
            if(finding!="Normal") {
                MucosaPick19("Tamaño mayor aproximado",listOf("<2 mm","2–4 mm","5–9 mm","10–19 mm","20–29 mm","≥30 mm","No medido"),sizeMm){sizeMm=it}
                MucosaPick19("Número",listOf("Única","Múltiples"),count){count=it}
                MucosaPick19("Color",listOf("Rosado","Rojo","Blanco","Rojo-blanco","Amarillo","Azulado/violáceo","Marrón/negro","Translúcido","Mixto"),color){color=it}
                MucosaPick19("Forma",listOf("Redonda/oval","Irregular","Lineal","Anular","Lobulada","Difusa/no delimitable"),shape){shape=it}
                MucosaPick19("Superficie",listOf("Lisa","Rugosa","Papilar/verrugosa","Ulcerada","Erosionada","Costrosa","Vesicular/ampollar","Queratinizada"),surface){surface=it}
                MucosaPick19("Bordes",listOf("Regular/definido","Irregular","Elevado","Evertido","Indurado","Mal definido"),border){border=it}
                MucosaPick19("Base",listOf("Sésil","Pediculada","Plana","No aplica/no valorable"),base){base=it}
                MucosaPick19("Consistencia a palpación",listOf("Blanda","Firme","Indurada","Fluctuante","Compresible","No valorada"),consistency){consistency=it}
                MucosaPick19("Movilidad",listOf("Móvil","Fija","No valorada/no aplica"),mobility){mobility=it}
                MucosaPick19("Síntomas",listOf("Asintomática","Dolor","Ardor","Sangrado","Prurito","Parestesia/adormecimiento"),symptoms){symptoms=it}
                MucosaPick19("Duración referida",listOf("<1 semana","1–2 semanas","2–4 semanas","1–3 meses",">3 meses","Recurrente","No referido"),duration){duration=it}
                MucosaPick19("Evolución",listOf("Nueva","Estable","En crecimiento","Disminuyendo","Recurrente","No referida"),evolution){evolution=it}
                MucosaPick19("Observaciones adicionales",listOf("Sin observaciones adicionales","Trauma local aparente","Prótesis/aparato en contacto","Próximo a diente/restauración","Secreción presente","Sangrado al contacto","No valorable"),notes){notes=it}
            }
        }
        Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)) {
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                Text(tr(lang,"¿Qué escribo en el expediente?","What do I write in the record?"),fontWeight=FontWeight.Black)
                Text(example)
            }
        }

        if(lesionHelp){
            val elementary=listOf(
                "Mácula / mancha" to "Cambio circunscrito de color, plano y no palpable.",
                "Eritema" to "Enrojecimiento de la mucosa por aumento visible de la vascularidad; describir distribución y contexto.",
                "Petequia" to "Punto hemorrágico pequeño, rojo a violáceo, que no desaparece a la presión.",
                "Púrpura / equimosis" to "Extravasación sanguínea mayor que una petequia; la equimosis es una zona hemorrágica más extensa.",
                "Pápula" to "Elevación sólida, pequeña y circunscrita.",
                "Placa" to "Lesión elevada o engrosada, de superficie relativamente amplia; puede ser blanca, roja o mixta.",
                "Nódulo" to "Lesión sólida palpable, más profunda o voluminosa que una pápula.",
                "Tumor / masa" to "Aumento de volumen sólido; término descriptivo y no sinónimo automático de cáncer.",
                "Vesícula" to "Elevación pequeña con contenido líquido.",
                "Ampolla / bula" to "Elevación con contenido líquido de mayor tamaño que una vesícula.",
                "Pústula" to "Elevación circunscrita con contenido purulento.",
                "Quiste" to "Cavidad patológica revestida, habitualmente con contenido líquido o semisólido; suele requerir correlación clínica/radiográfica o histológica.",
                "Erosión" to "Pérdida superficial del epitelio, sin exposición profunda del tejido conjuntivo.",
                "Úlcera" to "Pérdida del epitelio con exposición del tejido conjuntivo; describir fondo, bordes, dolor e induración.",
                "Fisura / grieta" to "Hendidura lineal de la superficie mucosa.",
                "Costra" to "Material seco de exudado o sangre; es más habitual en piel o bermellón que dentro de la mucosa húmeda.",
                "Escama" to "Lámina de queratina desprendida; principalmente observable en superficies queratinizadas/piel.",
                "Atrofia" to "Adelgazamiento epitelial que puede dar aspecto liso o eritematoso.",
                "Queratosis" to "Engrosamiento queratósico clínicamente blanquecino; es una descripción, no una etiología.",
                "Vegetación / lesión papilar" to "Crecimiento exofítico con superficie papilar, verrugosa o digitiforme.",
                "Fístula / trayecto sinusal" to "Conducto de drenaje hacia la superficie; buscar el origen clínico.",
                "Trauma" to "Mecanismo o antecedente, no lesión elemental. Puede producir erosión, úlcera, hematoma, fisura u otras lesiones.",
                "Edema" to "Aumento de volumen por acumulación de líquido en tejidos.",
                "Hematoma" to "Colección localizada de sangre en tejidos, generalmente relacionada con trauma o sangrado.",
                "Pigmentación" to "Cambio de color por pigmento endógeno o exógeno; describir color, patrón, extensión y evolución."
            )
            AlertDialog(onDismissRequest={lesionHelp=false},confirmButton={TextButton(onClick={lesionHelp=false}){Text("Cerrar")}},title={Text("Lesiones elementales · ayuda rápida")},text={
                Column(Modifier.height(460.dp).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(8.dp)){
                    Text("Toca esta ayuda cuando necesites recordar qué estás observando. Primero describe la lesión; después se integra el diagnóstico.",fontWeight=FontWeight.Bold)
                    Image(painter=painterResource(com.yomismtz.expedientedeldentista.R.drawable.mucosa_lesiones_elementales),contentDescription="Ilustración de lesiones elementales de mucosa oral",modifier=Modifier.fillMaxWidth().height(360.dp),contentScale=ContentScale.Fit)
                    Text("Ilustración educativa de referencia: compara el aspecto general; no sustituye la exploración clínica ni confirma diagnósticos.",style=MaterialTheme.typography.bodySmall)
                    elementary.forEach{(n,d)->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(9.dp)){Text(n,fontWeight=FontWeight.Black);Text(d)}}}
                }
            })
        }
        NoticeCard(tr(lang,"Describe antes de diagnosticar. Lesiones persistentes, induradas, ulceradas sin causa clara, masas o crecimiento requieren supervisión docente/profesional y seguimiento.","Describe before diagnosing. Persistent, indurated, unexplained ulcerated lesions, masses or growth require faculty/professional assessment and follow-up."))
    }
}


@Composable private fun MucosaPick19(label:String,options:List<String>,selected:String,onSelected:(String)->Unit){
    Text(label,fontWeight=FontWeight.Bold)
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)){
        options.forEach{o->FilterChip(selected==o,{onSelected(o)},{Text(o)})}
    }
}

@Composable
private fun OpenMouthMap19(selectedId:String,onSelected:(String)->Unit) {
    val outline=MaterialTheme.colorScheme.outline
    val selectionColor=MaterialTheme.colorScheme.primary
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer.copy(alpha=.35f))) {
        Canvas(
            Modifier.fillMaxWidth().height(340.dp).padding(8.dp).pointerInput(Unit) {
                detectTapGestures { p ->
                    val x=p.x/size.width.toFloat(); val y=p.y/size.height.toFloat()
                    val id=when {
                        y<0.12f -> "labio_sup"
                        y>0.88f -> "labio_inf"
                        x<0.18f && y in 0.20f..0.78f -> "carrillo_der"
                        x>0.82f && y in 0.20f..0.78f -> "carrillo_izq"
                        y<0.32f -> "paladar_duro"
                        y<0.42f -> "paladar_blando"
                        y<0.50f -> "orofaringe"
                        y<0.75f -> "lengua"
                        y>0.78f -> "piso"
                        else -> "encia"
                    }
                    onSelected(id)
                }
            }
        ) {
            val w=size.width; val h=size.height
            val lip=Color(0xFFE98CA5); val oral=Color(0xFF5E2338); val palate=Color(0xFFE8AAA8); val tongue=Color(0xFFE87886); val gum=Color(0xFFD98592)
            val outer=Path().apply {
                moveTo(w*.08f,h*.50f)
                cubicTo(w*.18f,h*.08f,w*.36f,h*.02f,w*.50f,h*.07f)
                cubicTo(w*.64f,h*.02f,w*.82f,h*.08f,w*.92f,h*.50f)
                cubicTo(w*.82f,h*.92f,w*.64f,h*.98f,w*.50f,h*.93f)
                cubicTo(w*.36f,h*.98f,w*.18f,h*.92f,w*.08f,h*.50f)
                close()
            }
            drawPath(outer,lip); drawPath(outer,outline,style=Stroke(3f))
            val cavity=Path().apply {
                moveTo(w*.16f,h*.48f)
                cubicTo(w*.22f,h*.20f,w*.36f,h*.14f,w*.50f,h*.16f)
                cubicTo(w*.64f,h*.14f,w*.78f,h*.20f,w*.84f,h*.48f)
                cubicTo(w*.78f,h*.78f,w*.65f,h*.86f,w*.50f,h*.84f)
                cubicTo(w*.35f,h*.86f,w*.22f,h*.78f,w*.16f,h*.48f)
                close()
            }
            drawPath(cavity,oral)
            drawOval(palate,Offset(w*.30f,h*.19f),Size(w*.40f,h*.20f))
            drawOval(Color(0xFFD99298),Offset(w*.35f,h*.31f),Size(w*.30f,h*.11f))
            val uvula=Path().apply { moveTo(w*.47f,h*.37f); quadraticBezierTo(w*.50f,h*.49f,w*.53f,h*.37f); close() }
            drawPath(uvula,Color(0xFFC96E7B))
            drawArc(gum,200f,140f,false,Offset(w*.22f,h*.30f),Size(w*.56f,h*.22f),style=Stroke(h*.035f))
            drawArc(gum,20f,140f,false,Offset(w*.22f,h*.58f),Size(w*.56f,h*.20f),style=Stroke(h*.035f))
            repeat(10) { i ->
                val f=i/9f; val x=w*(.245f+.51f*f); val dy=kotlin.math.abs(f-.5f)*h*.035f
                drawRoundRect(Color(0xFFFFFDF8),Offset(x-w*.022f,h*.36f+dy),Size(w*.044f,h*.095f),CornerRadius(7f,7f))
                drawRoundRect(outline,Offset(x-w*.022f,h*.36f+dy),Size(w*.044f,h*.095f),CornerRadius(7f,7f),style=Stroke(1.5f))
                drawRoundRect(Color(0xFFFFFDF8),Offset(x-w*.022f,h*.65f-dy),Size(w*.044f,h*.09f),CornerRadius(7f,7f))
                drawRoundRect(outline,Offset(x-w*.022f,h*.65f-dy),Size(w*.044f,h*.09f),CornerRadius(7f,7f),style=Stroke(1.5f))
            }
            val tonguePath=Path().apply {
                moveTo(w*.29f,h*.69f)
                cubicTo(w*.31f,h*.52f,w*.40f,h*.48f,w*.50f,h*.49f)
                cubicTo(w*.60f,h*.48f,w*.69f,h*.52f,w*.71f,h*.69f)
                cubicTo(w*.64f,h*.80f,w*.57f,h*.82f,w*.50f,h*.82f)
                cubicTo(w*.43f,h*.82f,w*.36f,h*.80f,w*.29f,h*.69f)
                close()
            }
            drawPath(tonguePath,tongue)
            drawLine(Color(0xFFC75F70),Offset(w*.50f,h*.55f),Offset(w*.50f,h*.76f),strokeWidth=2.5f)
            val centers=mapOf(
                "labio_sup" to Offset(w*.50f,h*.07f),"labio_inf" to Offset(w*.50f,h*.93f),
                "carrillo_der" to Offset(w*.13f,h*.52f),"carrillo_izq" to Offset(w*.87f,h*.52f),
                "encia" to Offset(w*.50f,h*.35f),"paladar_duro" to Offset(w*.50f,h*.25f),
                "paladar_blando" to Offset(w*.50f,h*.36f),"orofaringe" to Offset(w*.50f,h*.45f),
                "lengua" to Offset(w*.50f,h*.64f),"piso" to Offset(w*.50f,h*.84f)
            )
            centers[selectedId]?.let { c -> drawCircle(selectionColor.copy(alpha=.72f),minOf(w,h)*.055f,c); drawCircle(Color.White,minOf(w,h)*.018f,c) }
        }
    }
}
