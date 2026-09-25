package com.yomismtz.expedientedeldentista.ui

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

private enum class OrthoV20Tab { FRONTAL, POWELL, STEINER, MODELS, PANORAMIC }

@Composable
fun OrthodonticAuxiliariesV20Screen(lang:String,onBack:()->Unit){
    var tab by remember{mutableStateOf(OrthoV20Tab.FRONTAL)}
    val labels=listOf(
        OrthoV20Tab.FRONTAL to tr(lang,"Foto frontal","Frontal photo"),
        OrthoV20Tab.POWELL to "Powell",
        OrthoV20Tab.STEINER to "Steiner",
        OrthoV20Tab.MODELS to tr(lang,"Moyers / Tanaka / Bolton","Moyers / Tanaka / Bolton"),
        OrthoV20Tab.PANORAMIC to tr(lang,"Panorámica / Nolla","Panoramic / Nolla")
    )
    ResponsiveScreenV17(tr(lang,"Análisis ortodóncicos interactivos","Interactive orthodontic analyses"),tr(lang,"Carga una imagen cuando aplique y marca manualmente los puntos. La app calcula proporciones/ángulos a partir de tus marcas; no identifica puntos anatómicos automáticamente.","Upload an image when applicable and mark landmarks manually. The app calculates ratios/angles from your marks; it does not automatically identify anatomy."),onBack){profile->
        AdaptiveGridV17(labels.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 3){i->
            FilterChip(tab==labels[i].first,{tab=labels[i].first},{Text(labels[i].second)},Modifier.fillMaxWidth())
        }
        when(tab){
            OrthoV20Tab.FRONTAL->FacialFrontalV20(lang)
            OrthoV20Tab.POWELL->PowellV20(lang)
            OrthoV20Tab.STEINER->SteinerV20(lang)
            OrthoV20Tab.MODELS->MixedDentitionV20(lang)
            OrthoV20Tab.PANORAMIC->PanoramicNollaV20(lang,profile)
        }
    }
}

@Composable
private fun imageLoaderV20(onLoaded:(ImageBitmap)->Unit):()->Unit{
    val context=LocalContext.current
    val launcher=rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){uri->
        if(uri!=null){
            runCatching{context.contentResolver.openInputStream(uri)?.use{BitmapFactory.decodeStream(it)?.asImageBitmap()}}.getOrNull()?.let(onLoaded)
        }
    }
    return {launcher.launch("image/*")}
}

@Composable
private fun LandmarkImageV20(bitmap:ImageBitmap?,labels:List<String>,points:MutableList<Offset>,onReset:()->Unit){
    val launch=imageLoaderV20{img->onReset(); imageHolderV20=img}
    if(bitmap==null){
        Button(onClick=launch,modifier=Modifier.fillMaxWidth()){Text("🖼️ ${"Seleccionar imagen"}")}
        return
    }
    val ratio=(bitmap.width.toFloat()/bitmap.height.toFloat()).coerceIn(.45f,2.2f)
    Card(modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp)){
        Box(Modifier.fillMaxWidth().aspectRatio(ratio).pointerInput(labels.size,points.size){
            detectTapGestures{p->if(points.size<labels.size) points.add(p)}
        }){
            Image(bitmap=bitmap,contentDescription=null,modifier=Modifier.fillMaxSize(),contentScale=ContentScale.FillBounds)
            Canvas(Modifier.fillMaxSize()){
                points.forEachIndexed{i,p->
                    drawCircle(Color(0xFF7B2CBF),radius=13f,center=p)
                    drawCircle(Color.White,radius=5f,center=p)
                }
            }
        }
    }
    Text(if(points.size<labels.size)"Toca: ${labels[points.size]}" else "Puntos completos",fontWeight=FontWeight.Black,color=MaterialTheme.colorScheme.primary)
    Text(labels.mapIndexed{i,s->"${i+1}. $s"}.joinToString("   "),style=MaterialTheme.typography.bodySmall)
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
        OutlinedButton(onClick=onReset,modifier=Modifier.weight(1f)){Text("Reiniciar puntos")}
        OutlinedButton(onClick=launch,modifier=Modifier.weight(1f)){Text("Cambiar imagen")}
    }
}

// Holder used only during composition handoff from the system picker; screens immediately copy it to local state.
private var imageHolderV20:ImageBitmap?=null

@Composable
private fun PickableImageV20(bitmap:ImageBitmap?,onBitmap:(ImageBitmap)->Unit,labels:List<String>,points:MutableList<Offset>){
    val context=LocalContext.current
    val launcher=rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){uri->
        if(uri!=null){
            runCatching{context.contentResolver.openInputStream(uri)?.use{BitmapFactory.decodeStream(it)?.asImageBitmap()}}.getOrNull()?.let{points.clear();onBitmap(it)}
        }
    }
    if(bitmap==null){
        Button(onClick={launcher.launch("image/*")},modifier=Modifier.fillMaxWidth()){Text("🖼️ Seleccionar imagen")}
    }else{
        val ratio=(bitmap.width.toFloat()/bitmap.height.toFloat()).coerceIn(.45f,2.2f)
        Card(modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp)){
            Box(Modifier.fillMaxWidth().aspectRatio(ratio).pointerInput(labels.size,points.size){detectTapGestures{p->if(points.size<labels.size)points.add(p)}}){
                Image(bitmap,null,Modifier.fillMaxSize(),contentScale=ContentScale.FillBounds)
                Canvas(Modifier.fillMaxSize()){points.forEach{p->drawCircle(Color(0xFF7B2CBF),13f,p);drawCircle(Color.White,5f,p)}}
            }
        }
        Text(if(points.size<labels.size)"Toca el punto ${points.size+1}: ${labels[points.size]}" else "✓ Puntos completos",fontWeight=FontWeight.Black,color=MaterialTheme.colorScheme.primary)
        Text(labels.mapIndexed{i,s->"${i+1}. $s"}.joinToString("   "),style=MaterialTheme.typography.bodySmall)
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
            OutlinedButton(onClick={points.clear()},modifier=Modifier.weight(1f)){Text("Reiniciar puntos")}
            OutlinedButton(onClick={launcher.launch("image/*")},modifier=Modifier.weight(1f)){Text("Cambiar imagen")}
        }
    }
}

@Composable
private fun FacialFrontalV20(lang:String){
    var mode by remember{mutableStateOf(0)}
    var bitmap by remember{mutableStateOf<ImageBitmap?>(null)}
    val thirds=remember{mutableStateListOf<Offset>()}
    val fifths=remember{mutableStateListOf<Offset>()}
    ResponsiveSectionV17(tr(lang,"Fotografía frontal: tercios y quintos","Frontal photograph: thirds and fifths"),tr(lang,"La imagen solo vive en memoria durante esta práctica y no se guarda como expediente.","The image remains only in memory during this practice and is not stored as a record.")){
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
            FilterChip(mode==0,{mode=0},{Text(tr(lang,"3 tercios","3 thirds"))},Modifier.weight(1f))
            FilterChip(mode==1,{mode=1},{Text(tr(lang,"5 quintos","5 fifths"))},Modifier.weight(1f))
        }
        if(mode==0){
            PickableImageV20(bitmap,{bitmap=it},listOf("Tr","Of","Sn","Me"),thirds)
            if(thirds.size==4){
                val a=abs(thirds[1].y-thirds[0].y);val b=abs(thirds[2].y-thirds[1].y);val c=abs(thirds[3].y-thirds[2].y);val total=(a+b+c).coerceAtLeast(.001f)
                val ps=listOf(a/total*100,b/total*100,c/total*100)
                Text("Tr–Of ${"%.1f".format(ps[0])}% · Of–Sn ${"%.1f".format(ps[1])}% · Sn–Me ${"%.1f".format(ps[2])}%",fontWeight=FontWeight.Black)
                val range=ps.maxOrNull()!!-ps.minOrNull()!!
                Text(if(range<=6f)tr(lang,"Proporción aproximadamente equilibrada entre los tres tercios.","Approximately balanced proportions among the three thirds.") else tr(lang,"Hay diferencia proporcional entre los tercios; revisa colocación de puntos y determina cuál está aumentado/disminuido.","There is a proportional difference among thirds; review landmarks and identify the increased/decreased third."))
            }
            Text(tr(lang,"Material docente: Tr–Of, Of–Sn y Sn–Me deben ser aproximadamente equivalentes; también se describe N–Sn 43% y Sn–Me 57% para los dos tercios inferiores.","Teaching material: Tr–Of, Of–Sn and Sn–Me should be approximately equivalent; N–Sn 43% and Sn–Me 57% are also used for the two lower thirds."),style=MaterialTheme.typography.bodySmall)
        }else{
            PickableImageV20(bitmap,{bitmap=it},listOf("borde facial D","canto lateral D","canto medial D","canto medial I","canto lateral I","borde facial I"),fifths)
            if(fifths.size==6){
                val xs=fifths.map{it.x}
                val widths=(0..4).map{abs(xs[it+1]-xs[it])}
                val mean=widths.average().coerceAtLeast(.001)
                Text(widths.mapIndexed{i,w->"Q${i+1} ${"%.0f".format(w/mean*100)}%"}.joinToString(" · "),fontWeight=FontWeight.Black)
                val deviation=widths.maxOf{abs(it/mean-1.0)}
                Text(if(deviation<=.12)tr(lang,"Quintos aproximadamente simétricos/equilibrados.","Fifths are approximately symmetric/balanced.") else tr(lang,"Uno o más quintos se apartan del promedio; revisa simetría y colocación de puntos.","One or more fifths differ from the average; review symmetry and landmark placement."))
            }
            Text(tr(lang,"El material usa cinco anchos faciales y señala que el quinto central corresponde a la distancia intercantal interna y debe relacionarse con el ancho nasal.","The material uses five facial widths; the central fifth corresponds to inner-canthal distance and should relate to nasal width."),style=MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun PowellV20(lang:String){
    var bitmap by remember{mutableStateOf<ImageBitmap?>(null)}
    val points=remember{mutableStateListOf<Offset>()}
    var male by remember{mutableStateOf(false)}
    ResponsiveSectionV17(tr(lang,"Fotografía lateral · análisis de Powell","Lateral photograph · Powell analysis"),tr(lang,"Marca G’, N’, Prn, Pg’ y punto cervical. Los ángulos dependen de la exactitud de tus puntos y de una fotografía lateral estandarizada.","Mark G’, N’, Prn, Pg’ and a cervical point. Angles depend on accurate landmarks and a standardized lateral photograph.")){
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
            FilterChip(!male,{male=false},{Text(tr(lang,"Mujer","Female"))},Modifier.weight(1f))
            FilterChip(male,{male=true},{Text(tr(lang,"Hombre","Male"))},Modifier.weight(1f))
        }
        PickableImageV20(bitmap,{bitmap=it},listOf("G’","N’ / radix","Prn","Pg’","Cervical"),points)
        if(points.size==5){
            val g=points[0];val n=points[1];val prn=points[2];val pg=points[3];val c=points[4]
            val nasofrontal=angleV20(g,n,prn)
            val nasofacial=angleV20(prn,n,pg)
            val nasomental=angleV20(n,prn,pg)
            val mentocervical=angleV20(n,pg,c)
            PowellResultV20("Nasofrontal",nasofrontal,115.0,130.0,lang)
            val target=if(male)40.0 else 30.0
            Text("Mesofacial ≈ ${"%.1f".format(nasofacial)}° · ${tr(lang,"referencia del material","material reference")} ${target.toInt()}°",fontWeight=FontWeight.Bold)
            PowellResultV20("Nasomentón / Ricketts",nasomental,120.0,132.0,lang)
            PowellResultV20("Mentocervical",mentocervical,80.0,95.0,lang)
            Text(tr(lang,"Si un ángulo no concuerda visualmente, reinicia y corrige los puntos; la app no detecta automáticamente tangentes ni errores de postura.","If an angle does not match the visual profile, reset and correct landmarks; the app does not automatically detect tangents or positioning errors."),style=MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun PowellResultV20(name:String,value:Double,min:Double,max:Double,lang:String){
    val status=when{value<min->tr(lang,"disminuido","decreased");value>max->tr(lang,"aumentado","increased");else->tr(lang,"en referencia","in reference")}
    Text("$name = ${"%.1f".format(value)}° · $status ($min–$max°)",fontWeight=FontWeight.Bold)
}

@Composable
private fun SteinerV20(lang:String){
    var bitmap by remember{mutableStateOf<ImageBitmap?>(null)}
    val points=remember{mutableStateListOf<Offset>()}
    ResponsiveSectionV17(tr(lang,"Radiografía lateral de cráneo · Steiner simplificado","Lateral cephalogram · simplified Steiner"),tr(lang,"Steiner simplificado: usa únicamente los puntos S, N, A, B, Go y Gn para obtener SNA, SNB, ANB y GoGn–SN.","Simplified Steiner: use only S, N, A, B, Go and Gn to obtain SNA, SNB, ANB and GoGn–SN.")){
        PickableImageV20(bitmap,{bitmap=it},listOf("S","N","A","B","Go","Gn"),points)
        if(points.size==6){
            val s=points[0];val n=points[1];val a=points[2];val b=points[3];val go=points[4];val gn=points[5]
            val sna=angleV20(s,n,a);val snb=angleV20(s,n,b);val anb=sna-snb;val sngogn=lineAngleV20(s,n,go,gn)
            SteinerRowV20("SNA",sna,80.0,84.0,tr(lang,"<80°: posición maxilar retruida; >84°: adelantada/prognática según el material.","<80°: retruded maxillary position; >84°: advanced/prognathic per the teaching material."),lang)
            SteinerRowV20("SNB",snb,78.0,82.0,tr(lang,"<78°: retrogenismo mandibular; >82°: prognatismo mandibular.","<78°: mandibular retrognathism; >82°: mandibular prognathism."),lang)
            SteinerRowV20("ANB",anb,1.0,3.0,tr(lang,"<1°: tendencia Clase III; 1–3°: Clase I; >3°: tendencia Clase II (norma 2° ±1° en la tabla de Steiner del material).","<1°: Class III tendency; 1–3°: Class I; >3°: Class II tendency (2° ±1° in the material's Steiner table)."),lang)
            SteinerRowV20("SN–GoGn",sngogn,27.0,37.0,tr(lang,"<27°: patrón horizontal; 27–37°: mesofacial; >37°: patrón vertical (norma 32° ±5°).","<27°: horizontal pattern; 27–37°: mesofacial; >37°: vertical pattern (32° ±5°)."),lang)
            NoticeCard(tr(lang,"Resultado educativo de trazado manual. Antes de usarlo académicamente verifica calibración de imagen, identificación de puntos y protocolo del curso.","Educational manual-tracing result. Before academic use, verify image calibration, landmark identification and course protocol."))
        }
    }
}

@Composable
private fun SteinerRowV20(name:String,value:Double,min:Double,max:Double,explanation:String,lang:String){
    val status=when{value<min->tr(lang,"disminuido","decreased");value>max->tr(lang,"aumentado","increased");else->tr(lang,"en norma","within reference")}
    Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer),modifier=Modifier.fillMaxWidth()){
        androidx.compose.foundation.layout.Column(Modifier.padding(10.dp)){
            Text("$name = ${"%.1f".format(value)}° · $status",fontWeight=FontWeight.Black)
            Text(explanation,style=MaterialTheme.typography.bodySmall)
        }
    }
}

private val moyersInputV20=listOf(20.5,21.0,21.5,22.0,22.5,23.5,24.0,24.5,25.0,25.5,26.0,26.5,27.0,27.5,28.0,28.5,29.0,29.5,30.0,30.5,31.0,32.0)
private val moyersUpperV20=listOf(20.9,21.2,21.5,21.8,22.0,22.3,22.6,22.9,23.1,23.4,23.7,24.0,24.2,24.5,24.7,25.0,25.3,25.8,26.1,26.4,26.7,27.1)
private val moyersLowerV20=listOf(20.4,20.7,21.0,21.3,21.6,21.9,22.2,22.5,22.8,23.1,23.4,23.7,24.0,24.3,24.6,24.8,25.1,25.4,25.9,26.2,26.5,26.9)

private fun moyersV20(sum:Double,upper:Boolean):Pair<Double,Double>{
    val values=if(upper)moyersUpperV20 else moyersLowerV20
    val idx=moyersInputV20.indices.minByOrNull{abs(moyersInputV20[it]-sum)}?:0
    return moyersInputV20[idx] to values[idx]
}

@Composable
private fun MixedDentitionV20(lang:String){
    var method by remember{mutableStateOf(0)}
    val incisors=remember{mutableStateMapOf<Int,String>()}
    val available=remember{mutableStateMapOf<String,String>()}
    val teeth=listOf(42,41,31,32)
    val sum=teeth.mapNotNull{incisors[it]?.toDoubleOrNull()}.takeIf{it.size==4}?.sum()
    ResponsiveSectionV17(tr(lang,"Análisis de dentición mixta","Mixed dentition analysis"),tr(lang,"Mide el ancho mesiodistal máximo de 42, 41, 31 y 32. Después registra el espacio disponible de cada segmento canino–premolar.","Measure the maximum mesiodistal width of 42, 41, 31 and 32, then enter available space for each canine–premolar segment.")){
        ChipChoices(listOf("Moyers 75%" to (method==0),"Tanaka–Johnston" to (method==1),"Bolton" to (method==2)),{method=it},columns=3)
        if(method==2){
            Text(tr(lang,"Bolton compara la suma de anchos mesiodistales mandibulares con los maxilares. Relación total clásica = Σ12 inferiores / Σ12 superiores ×100 (≈91.3%); relación anterior = Σ6 inferiores / Σ6 superiores ×100 (≈77.2%). Mide cada diente de primer molar a primer molar para total y canino a canino para anterior. Una desviación orienta a discrepancia de tamaño dentario; debe comprobarse con mediciones completas y contexto ortodóncico.","Bolton compares summed mandibular and maxillary mesiodistal widths. Classic overall ratio = lower 12 / upper 12 ×100 (≈91.3%); anterior ratio = lower 6 / upper 6 ×100 (≈77.2%). Measure first molar to first molar for overall and canine to canine for anterior. Deviation suggests tooth-size discrepancy and requires complete measurements and orthodontic context."),fontWeight=FontWeight.Bold)
            NoticeCard(tr(lang,"En esta pantalla Bolton se enseña como método y fórmula. No se calcula con los cuatro incisivos inferiores usados por Moyers/Tanaka; requiere las sumas dentarias correspondientes.","This screen teaches the Bolton method and formula. It cannot be calculated from the four lower incisors used for Moyers/Tanaka; the corresponding tooth sums are required."))
        }
        AdaptiveGridV17(4,2){i->
            val tooth=teeth[i]
            OutlinedTextField(incisors[tooth].orEmpty(),{incisors[tooth]=numberOnlyV20(it)},label={Text("OD $tooth (mm)")},modifier=Modifier.fillMaxWidth(),singleLine=true)
        }
        Text("${tr(lang,"Suma 42+41+31+32","Sum 42+41+31+32")}: ${sum?.let{"%.1f mm".format(it)}?:"—"}",fontWeight=FontWeight.Black)
        val quadrants=listOf("SD","SI","ID","II")
        AdaptiveGridV17(4,2){i->OutlinedTextField(available[quadrants[i]].orEmpty(),{available[quadrants[i]]=numberOnlyV20(it)},label={Text("${tr(lang,"Disponible","Available")} ${quadrants[i]} mm")},modifier=Modifier.fillMaxWidth(),singleLine=true)}

        if(sum!=null && method!=2){
            if(method==0){
                val up=moyersV20(sum,true);val low=moyersV20(sum,false)
                Text("Moyers 75% · ${tr(lang,"fila usada por suma más cercana","nearest sum row used")}: ${up.first} mm",fontWeight=FontWeight.Bold)
                Text("${tr(lang,"Requerido estimado por cuadrante maxilar","Estimated required per maxillary quadrant")}: ${up.second} mm")
                Text("${tr(lang,"Requerido estimado por cuadrante mandibular","Estimated required per mandibular quadrant")}: ${low.second} mm")
                SpaceDiagnosisV20(lang,available,mapOf("SD" to up.second,"SI" to up.second,"ID" to low.second,"II" to low.second))
                Text(tr(lang,"Según el material: suma los cuatro incisivos inferiores, consulta la tabla de predictibilidad al 75%, mide el espacio disponible para canino y premolares y compara disponible contra requerido. Para una arcada completa, la predicción bilateral es 2 × el valor por cuadrante.","Per the teaching material: sum the four lower incisors, use the 75% prediction table, measure space available for canine/premolars, and compare available versus required. Whole-arch bilateral prediction is 2 × the quadrant value."),style=MaterialTheme.typography.bodySmall)
            }else{
                val upper=sum/2.0+11.0;val lower=sum/2.0+10.5
                Text("Maxilar = (${"%.1f".format(sum)} ÷ 2) + 11.0 = ${"%.1f".format(upper)} mm / cuadrante",fontWeight=FontWeight.Bold)
                Text("Mandíbula = (${"%.1f".format(sum)} ÷ 2) + 10.5 = ${"%.1f".format(lower)} mm / cuadrante",fontWeight=FontWeight.Bold)
                SpaceDiagnosisV20(lang,available,mapOf("SD" to upper,"SI" to upper,"ID" to lower,"II" to lower))
                Text(tr(lang,"La fórmula de Tanaka–Johnston estima canino + primer premolar + segundo premolar no erupcionados de un lado. La fórmula no aparece escrita en las celdas del documento aportado; se incorpora aquí como la fórmula clásica publicada.","The Tanaka–Johnston equation estimates unerupted canine + first premolar + second premolar on one side. The supplied document leaves the equation cells blank; the classic published equation is used here."),style=MaterialTheme.typography.bodySmall)
            }
        }
        NoticeCard(tr(lang,"Cómo medir espacio disponible: sigue el arco en el segmento donde deben erupcionar canino y premolares, entre los puntos dentarios definidos por tu protocolo/modelo. El material diferencia una medición total y otra posterior; conserva siempre el mismo método en los cuatro cuadrantes.","How to measure available space: follow the arch through the segment where canine and premolars will erupt, between the dental reference points defined by your course/model protocol. The material distinguishes total and posterior measurements; use the same method in all quadrants."))
    }
}

@Composable
private fun SpaceDiagnosisV20(lang:String,available:Map<String,String>,required:Map<String,Double>){
    listOf("SD","SI","ID","II").forEach{q->
        val a=available[q]?.toDoubleOrNull();val r=required[q]?:return@forEach
        if(a!=null){
            val d=a-r
            val text=when{d<-.2->tr(lang,"deficiencia de espacio / apiñamiento probable","space deficiency / likely crowding");d>.2->tr(lang,"exceso de espacio / posible espaciamiento","space excess / possible spacing");else->tr(lang,"espacio aproximadamente exacto/equilibrado","approximately exact/balanced space")}
            Text("$q: ${"%.1f".format(a)} − ${"%.1f".format(r)} = ${"%+.1f".format(d)} mm → $text",fontWeight=FontWeight.Bold,color=if(abs(d)<=.2)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
        }
    }
}

private val nollaDescriptionsV20=listOf(
    "0 · ausencia de cripta","1 · presencia de cripta","2 · calcificación inicial","3 · 1/3 de corona","4 · 2/3 de corona","5 · corona casi completa","6 · corona completa","7 · 1/3 de raíz","8 · 2/3 de raíz","9 · raíz casi completa, ápice abierto","10 · raíz completa, cierre apical"
)

@Composable
private fun PanoramicNollaV20(lang:String,profile:ScreenProfileV17){
    var bitmap by remember{mutableStateOf<ImageBitmap?>(null)}
    val points=remember{mutableStateListOf<Offset>()}
    var selectedTooth by remember{mutableStateOf(16)}
    val stages=remember{mutableStateMapOf<Int,Int>()}
    val checks=remember{mutableStateMapOf<String,Boolean>()}
    val upper=listOf(18,17,16,15,14,13,12,11,21,22,23,24,25,26,27,28)
    val lower=listOf(48,47,46,45,44,43,42,41,31,32,33,34,35,36,37,38)
    val review=listOf("Calidad y posicionamiento","Dentición presente/ausente y supernumerarios","Desarrollo dental y erupción","Caries/restauraciones visibles","Lesiones periapicales","Nivel óseo periodontal","Raíces, reabsorciones y dilaceraciones","Retenidos/impactados y terceros molares","Radiolucideces/radiopacidades/lesiones quísticas","Senos maxilares y cavidad nasal","Cóndilos/ATM, ramas, cuerpos y ángulos mandibulares","Canal mandibular, forámenes mentonianos y asimetrías")
    ResponsiveSectionV17(tr(lang,"Radiografía panorámica · Nolla","Panoramic radiograph · Nolla"),tr(lang,"Carga la panorámica para verla durante la práctica. Selecciona cada OD y asigna manualmente su estadio de Nolla 0–10.","Upload the panoramic radiograph for practice. Select each tooth and manually assign Nolla stage 0–10.")){
        PickableImageV20(bitmap,{bitmap=it},emptyList(),points)
        Text(tr(lang,"Maxilar","Maxillary"),fontWeight=FontWeight.Black)
        AdaptiveGridV17(upper.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)4 else 8){i->val t=upper[i];FilterChip(selectedTooth==t,{selectedTooth=t},{Text("$t${stages[t]?.let{"·$it"}?:""}")},Modifier.fillMaxWidth())}
        Text(tr(lang,"Mandíbula","Mandibular"),fontWeight=FontWeight.Black)
        AdaptiveGridV17(lower.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)4 else 8){i->val t=lower[i];FilterChip(selectedTooth==t,{selectedTooth=t},{Text("$t${stages[t]?.let{"·$it"}?:""}")},Modifier.fillMaxWidth())}
        Text("OD $selectedTooth · ${stages[selectedTooth]?.let{nollaDescriptionsV20[it]}?:tr(lang,"sin estadio","no stage")}",fontWeight=FontWeight.Black)
        AdaptiveGridV17(11,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)4 else 6){i->FilterChip(stages[selectedTooth]==i,{stages[selectedTooth]=i},{Text(i.toString())},Modifier.fillMaxWidth())}
        stages[selectedTooth]?.let{Text(nollaDescriptionsV20[it])}
        if(stages.isNotEmpty()){
            Text("${tr(lang,"Dientes estadificados","Staged teeth")}: ${stages.size} · ${tr(lang,"promedio de estadio (no equivale a edad dental)","mean stage (not dental age)")}: ${"%.2f".format(stages.values.average())}",fontWeight=FontWeight.Bold)
        }
        NoticeCard(tr(lang,"Nolla 0–10 describe desarrollo radiográfico desde ausencia de cripta hasta cierre apical. Esta versión no convierte la suma a edad dental porque esa conversión requiere tablas/estándares específicos por sexo y población; evita inventar una edad a partir del promedio.","Nolla 0–10 describes radiographic development from absence of crypt to apical completion. This version does not convert the score into dental age because that requires sex/population-specific standards; do not infer age from the mean stage."))
    }
    ResponsiveSectionV17(tr(lang,"Revisión sistemática de la panorámica","Systematic panoramic review"),tr(lang,"Marca cada región revisada para evitar una lectura por saltos.","Check each reviewed region to avoid a fragmented reading.")){
        review.forEach{item->
            Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){
                Checkbox(checks[item]==true,{checks[item]=it})
                Text(item,Modifier.weight(1f))
            }
        }
        val done=review.count{checks[it]==true}
        Text("$done / ${review.size} ${tr(lang,"apartados revisados","sections reviewed")}",fontWeight=FontWeight.Black)
    }
}

private fun numberOnlyV20(s:String)=s.filter{it.isDigit()||it=='.'}.take(7)

private fun angleV20(a:Offset,b:Offset,c:Offset):Double{
    val v1x=(a.x-b.x).toDouble();val v1y=(a.y-b.y).toDouble();val v2x=(c.x-b.x).toDouble();val v2y=(c.y-b.y).toDouble()
    val den=sqrt(v1x*v1x+v1y*v1y)*sqrt(v2x*v2x+v2y*v2y)
    if(den<1e-9)return 0.0
    return acos(((v1x*v2x+v1y*v2y)/den).coerceIn(-1.0,1.0))*180.0/PI
}

private fun lineAngleV20(a:Offset,b:Offset,c:Offset,d:Offset):Double{
    val x=abs((atan2((b.y-a.y).toDouble(),(b.x-a.x).toDouble())-atan2((d.y-c.y).toDouble(),(d.x-c.x).toDouble()))*180.0/PI)%180.0
    return if(x>90.0)180.0-x else x
}
