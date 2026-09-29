package com.yomismtz.expedientedeldentista.ui

import com.yomismtz.expedientedeldentista.R

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.ui.graphics.graphicsLayer
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
    var selectedId by rememberRecordState("mucosa.selectedId","lengua")
    val tissueStatus=rememberRecordStateMap<String,String>("mucosa.tissueStatus")
    val tissueLesion=rememberRecordStateMap<String,String>("mucosa.tissueLesion")
    val tissuePathology=rememberRecordStateMap<String,String>("mucosa.tissuePathology")
    var finding by rememberRecordState("mucosa.finding","Normal")
    var sizeMm by rememberRecordState("mucosa.sizeMm","5 mm")
    var notes by rememberRecordState("mucosa.notes","Sin observaciones adicionales")
    var color by rememberRecordState("mucosa.color","Rosado")
    var shape by rememberRecordState("mucosa.shape","Redonda/oval")
    var surface by rememberRecordState("mucosa.surface","Lisa")
    var border by rememberRecordState("mucosa.border","Regular/definido")
    var base by rememberRecordState("mucosa.base","Sésil")
    var consistency by rememberRecordState("mucosa.consistency","Blanda")
    var mobility by rememberRecordState("mucosa.mobility","Móvil")
    var symptoms by rememberRecordState("mucosa.symptoms","Asintomática")
    var duration by rememberRecordState("mucosa.duration","No referido")
    var evolution by rememberRecordState("mucosa.evolution","No referida")
    var count by rememberRecordState("mucosa.count","Única")
    var showElementaryHelp by remember { mutableStateOf(false) }
    val selected=zones19.firstOrNull{it.id==selectedId} ?: zones19.first()
    val name=if(lang=="en")selected.en else selected.es
    val example=if(finding=="Normal") "$name: ${if(lang=="en")selected.normalEn else selected.normalEs}"
    else tr(lang,"$name: $finding; ${if(count=="Única")"lesión única" else "lesiones múltiples"}; tamaño ${sizeMm}; color $color; forma $shape; superficie $surface; borde $border; base $base; consistencia $consistency; movilidad $mobility; $symptoms; duración $duration; evolución $evolution${if(notes.isBlank())"" else "; $notes"}. Descripción clínica; correlacionar antes de diagnosticar.","$name: $finding; size ${if(sizeMm.isBlank())"not entered" else "$sizeMm mm"}; color $color; shape $shape; surface $surface; border $border; base $base; consistency $consistency; mobility $mobility; symptoms $symptoms; duration $duration; evolution $evolution. Clinical description; correlate before diagnosis.")

    ResponsiveScreenV17(tr(lang,"Mucosas orales interactivas","Interactive oral mucosa"),tr(lang,"Selecciona un tejido y practica una descripción clínica sistemática.","Select a tissue and practice systematic clinical description."),onBack) { profile ->
        val zoneColumns=when {
            profile.largeSystemText -> 2
            profile.width==ScreenWidthV17.COMPACT -> 3
            profile.width==ScreenWidthV17.MEDIUM -> 4
            else -> 6
        }
        ResponsiveSectionV17(tr(lang,"Selecciona un tejido","Select a tissue")) {
            Text("${tr(lang,"Zona seleccionada","Selected region")}: $name",fontWeight=FontWeight.Black,color=MaterialTheme.colorScheme.primary)
            AdaptiveGridV17(zones19.size,zoneColumns){i->
                val z=zones19[i]
                FilterChip(selectedId==z.id,{selectedId=z.id},{Text(if(lang=="en")z.en else z.es)},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(name,tr(lang,"Indica primero si el tejido está sano o presenta una alteración. Las opciones posteriores se muestran sólo cuando corresponde.","First indicate whether the tissue is healthy or altered. Further options appear only when appropriate.")) {
            val status=tissueStatus[selected.id]?:""
            AdaptiveGridV17(2,2){i->
                val value=if(i==0)"Sano" else "Alteración"
                FilterChip(status==value,{tissueStatus[selected.id]=value;if(value=="Sano"){tissueLesion[selected.id]="";tissuePathology[selected.id]=""}},{Text(if(lang=="en" && value=="Sano")"Healthy" else if(lang=="en")"Alteration" else value)},modifier=Modifier.fillMaxWidth())
            }
            if(status=="Sano"){
                Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){
                    Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){
                        Text(tr(lang,"Aspecto compatible con tejido sano","Appearance compatible with healthy tissue"),fontWeight=FontWeight.Bold)
                        Text(if(lang=="en")selected.normalEn else selected.normalEs)
                        Text(if(lang=="en")selected.exploreEn else selected.exploreEs,style=MaterialTheme.typography.bodySmall)
                    }
                }
            }
            if(status=="Alteración"){
                val elementary=listOf("Mácula / mancha","Eritema","Petequia","Púrpura / equimosis","Pápula","Placa blanca","Placa roja","Nódulo","Masa","Vesícula","Ampolla / bula","Pústula","Quiste","Erosión","Úlcera","Fisura / grieta","Costra","Atrofia","Queratosis","Lesión papilar / vegetación","Fístula / trayecto sinusal","Edema","Hematoma","Pigmentación")
                val pathologyByZone=mapOf(
                    "labio_sup" to listOf("Queilitis irritativa/traumática","Queilitis actínica","Herpes labial","Mucocele","Fibroma traumático"),
                    "labio_inf" to listOf("Mucocele","Queilitis irritativa/traumática","Herpes labial","Fibroma traumático","Lesión por mordisqueo"),
                    "carrillo_der" to listOf("Línea alba","Morsicatio / mordisqueo","Fibroma traumático","Leucoedema","Liquen plano oral","Úlcera traumática"),
                    "carrillo_izq" to listOf("Línea alba","Morsicatio / mordisqueo","Fibroma traumático","Leucoedema","Liquen plano oral","Úlcera traumática"),
                    "encia" to listOf("Gingivitis","Recesión gingival","Hiperplasia gingival","Absceso/fístula a valorar","Granuloma piógeno","Lesión periodontal a valorar"),
                    "paladar_duro" to listOf("Torus palatino","Quemadura térmica","Estomatitis nicotínica","Candidiasis","Úlcera traumática","Lesión pigmentada a valorar"),
                    "paladar_blando" to listOf("Eritema inflamatorio","Petequias","Candidiasis","Úlcera aftosa","Lesión viral a valorar","Asimetría funcional a valorar"),
                    "lengua" to listOf("Lengua geográfica","Lengua fisurada","Lengua saburral","Candidiasis","Glositis atrófica","Úlcera traumática","Fibroma traumático"),
                    "piso" to listOf("Ránula","Sialolitiasis/obstrucción a valorar","Quiste/masa a valorar","Lesión vascular a valorar","Úlcera traumática"),
                    "orofaringe" to listOf("Faringoamigdalitis a valorar","Hipertrofia amigdalina","Exudado amigdalino","Úlcera/lesión mucosa","Asimetría amigdalina a valorar")
                )
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){
                    Text(tr(lang,"Lesión elemental observada","Observed elementary lesion"),fontWeight=FontWeight.Black)
                    TextButton(onClick={showElementaryHelp=!showElementaryHelp}){Text("?")}
                }
                if(showElementaryHelp){
                    Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
                        Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                            Text(tr(lang,"Lesiones elementales","Elementary lesions"),fontWeight=FontWeight.Black)
                            Text(tr(lang,"Selecciona la lesión elemental que mejor describa el hallazgo. Esta referencia se puede cerrar con el botón ? para mantener compacta la pantalla.","Select the elementary lesion that best describes the finding. Close this reference with the ? button to keep the screen compact."),style=MaterialTheme.typography.bodySmall)
                            AdaptiveGridV17(elementary.size,zoneColumns){i->
                                val option=elementary[i]
                                FilterChip(tissueLesion[selected.id]==option,{tissueLesion[selected.id]=option;finding=option},{Text(option)},modifier=Modifier.fillMaxWidth())
                            }
                        }
                    }
                } else {
                    val current=tissueLesion[selected.id].orEmpty()
                    Text(if(current.isBlank()) tr(lang,"Toca ? para elegir o consultar una lesión elemental.","Tap ? to choose or review an elementary lesion.") else tr(lang,"Seleccionada: $current · Toca ? para cambiarla o consultar la referencia.","Selected: $current · Tap ? to change it or review the reference."),style=MaterialTheme.typography.bodySmall)
                }
                val selectedLesion=tissueLesion[selected.id].orEmpty()
                val lesionVisual=when {
                    selectedLesion.startsWith("Mácula") -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_macula,"Mácula / mancha","Lesión plana definida por un cambio de color. Observa límites, color, distribución y evolución.")
                    selectedLesion=="Pápula" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_papula,"Pápula","Elevación sólida pequeña y circunscrita. Observa tamaño, superficie, color y consistencia.")
                    selectedLesion.startsWith("Placa") -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_placa,selectedLesion,"Área elevada o engrosada. Observa color, superficie, límites y si se desprende o no.")
                    selectedLesion=="Nódulo" || selectedLesion=="Masa" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_nodulo,selectedLesion,"Aumento de volumen sólido. Observa tamaño, profundidad, consistencia, movilidad y evolución.")
                    selectedLesion=="Vesícula" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_vesicula,"Vesícula","Elevación pequeña con contenido líquido. Observa número, agrupación, integridad y síntomas.")
                    selectedLesion.startsWith("Ampolla") -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_ampolla,"Ampolla / bula","Elevación con contenido líquido de mayor tamaño. Observa integridad, extensión y síntomas.")
                    selectedLesion=="Pústula" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_pustula,"Pústula","Elevación con contenido purulento. Observa tamaño, localización, drenaje y tejido circundante.")
                    selectedLesion=="Erosión" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_erosion,"Erosión","Pérdida superficial del epitelio. Observa extensión, fondo, bordes, dolor y posible causa local.")
                    selectedLesion=="Úlcera" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_ulcera,"Úlcera","Pérdida epitelial con exposición del tejido conjuntivo. Observa fondo, bordes, induración, dolor y duración.")
                    selectedLesion.startsWith("Fisura") -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_fisura,"Fisura / grieta","Hendidura lineal de la superficie. Observa profundidad, localización, síntomas y factores locales.")
                    selectedLesion.startsWith("Fístula") -> Triple(com.yomismtz.expedientedeldentista.R.drawable.edu_lesion_fistula,"Fístula / trayecto sinusal","Trayecto de drenaje. Observa localización, secreción y correlaciona clínicamente el posible origen.")
                    else -> null
                }
                if(showElementaryHelp) lesionVisual?.let{v->
                    Card(Modifier.fillMaxWidth()){
                        Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                            Text(v.second,fontWeight=FontWeight.Black)
                            Text(tr(lang,"¿Qué es?","What is it?"),fontWeight=FontWeight.SemiBold)
                            Text(v.third)
                            Text(tr(lang,"¿Qué observar?","What to observe?"),fontWeight=FontWeight.SemiBold)
                            Text(tr(lang,"Describe sitio, número, tamaño, color, forma, superficie, bordes, base, consistencia, síntomas, duración y evolución.","Describe site, number, size, color, shape, surface, borders, base, consistency, symptoms, duration and evolution."))
                            LocalClinicalInlineZoomImageV48(lang,v.second,v.second,v.first,v.third,v.third)
                        }
                    }
                }
                val pathologies=pathologyByZone[selected.id].orEmpty()
                if(pathologies.isNotEmpty()){
                    Text(tr(lang,"Patologías o condiciones frecuentes en este tejido","Frequent pathologies or conditions in this tissue"),fontWeight=FontWeight.Black)
                    Text(tr(lang,"Son opciones de registro y orientación clínica; seleccionarlas no establece por sí solo un diagnóstico.","These are recording and clinical-orientation options; selecting one does not by itself establish a diagnosis."),style=MaterialTheme.typography.bodySmall)
                    AdaptiveGridV17(pathologies.size,zoneColumns){i->
                        val option=pathologies[i]
                        FilterChip(tissuePathology[selected.id]==option,{tissuePathology[selected.id]=option},{Text(option)},modifier=Modifier.fillMaxWidth())
                    }
                    val selectedPathology=tissuePathology[selected.id].orEmpty()
                    val pathologyVisual=when(selected.id to selectedPathology){
                        "labio_sup" to "Queilitis irritativa/traumática" -> Triple(R.drawable.allimg_089_quelitis_irritativa_labio_superior,"Queilitis irritativa/traumática","Imagen de la opción seleccionada.")
                        "labio_sup" to "Herpes labial" -> Triple(R.drawable.allimg_049_herpes_labial,"Herpes labial","Imagen de la opción seleccionada.")
                        "labio_sup" to "Fibroma traumático" -> Triple(R.drawable.allimg_043_fibroma_traumatico_labio,"Fibroma traumático","Imagen de la opción seleccionada.")
                        "labio_inf" to "Mucocele" -> Triple(R.drawable.allimg_070_mucocele,"Mucocele","Imagen de la opción seleccionada.")
                        "labio_inf" to "Queilitis irritativa/traumática" -> Triple(R.drawable.allimg_088_quelitis_irritativa_labio_inferior,"Queilitis irritativa/traumática","Imagen de la opción seleccionada.")
                        "labio_inf" to "Herpes labial" -> Triple(R.drawable.allimg_049_herpes_labial,"Herpes labial","Imagen de la opción seleccionada.")
                        "labio_inf" to "Fibroma traumático" -> Triple(R.drawable.allimg_043_fibroma_traumatico_labio,"Fibroma traumático","Imagen de la opción seleccionada.")
                        "labio_inf" to "Lesión por mordisqueo" -> Triple(R.drawable.allimg_058_lesion_por_mordisueo_labio_inferior,"Lesión por mordisqueo","Imagen de la opción seleccionada.")
                        "carrillo_der" to "Línea alba","carrillo_izq" to "Línea alba" -> Triple(R.drawable.allimg_062_linea_laba_carrillo,"Línea alba","Imagen de la opción seleccionada.")
                        "carrillo_der" to "Morsicatio / mordisqueo","carrillo_izq" to "Morsicatio / mordisqueo" -> Triple(R.drawable.allimg_069_morsicatio_o_mordisque_carrillo,"Morsicatio / mordisqueo","Imagen de la opción seleccionada.")
                        "carrillo_der" to "Fibroma traumático","carrillo_izq" to "Fibroma traumático" -> Triple(R.drawable.allimg_045_fibroma_traumatico_carrillo,"Fibroma traumático","Imagen de la opción seleccionada.")
                        "carrillo_der" to "Leucoedema","carrillo_izq" to "Leucoedema" -> Triple(R.drawable.allimg_061_leucodema,"Leucoedema","Imagen de la opción seleccionada.")
                        "carrillo_der" to "Liquen plano oral","carrillo_izq" to "Liquen plano oral" -> Triple(R.drawable.allimg_064_liquen_plano_oral_carrillos,"Liquen plano oral","Imagen de la opción seleccionada.")
                        "carrillo_der" to "Úlcera traumática","carrillo_izq" to "Úlcera traumática" -> Triple(R.drawable.allimg_103_ulcera_traumatica_carrillos,"Úlcera traumática","Imagen de la opción seleccionada.")
                        "encia" to "Gingivitis" -> Triple(R.drawable.allimg_047_gingivitis,"Gingivitis","Imagen de la opción seleccionada.")
                        "encia" to "Recesión gingival" -> Triple(R.drawable.allimg_094_recesion_gingival,"Recesión gingival","Imagen de la opción seleccionada.")
                        "encia" to "Hiperplasia gingival" -> Triple(R.drawable.allimg_050_hiperplasia_gingival820x383,"Hiperplasia gingival","Imagen de la opción seleccionada.")
                        "encia" to "Absceso/fístula a valorar" -> Triple(R.drawable.allimg_046_fistula_encia,"Fístula gingival","Imagen de la opción seleccionada.")
                        "encia" to "Granuloma piógeno" -> Triple(R.drawable.allimg_048_granuloma_piogeno,"Granuloma piógeno","Imagen de la opción seleccionada.")
                        "encia" to "Lesión periodontal a valorar" -> Triple(R.drawable.allimg_056_lesion_peridontontal,"Lesión periodontal","Imagen de la opción seleccionada.")
                        "paladar_duro" to "Torus palatino" -> Triple(R.drawable.allimg_100_torus_palatino,"Torus palatino","Imagen de la opción seleccionada.")
                        "paladar_duro" to "Quemadura térmica" -> Triple(R.drawable.allimg_091_quemadura_termica_pladar,"Quemadura térmica","Imagen de la opción seleccionada.")
                        "paladar_duro" to "Estomatitis nicotínica" -> Triple(R.drawable.allimg_037_estomatitis_nicotinica,"Estomatitis nicotínica","Imagen de la opción seleccionada.")
                        "paladar_duro" to "Candidiasis" -> Triple(R.drawable.allimg_016_candidiasis_paladar,"Candidiasis palatina","Imagen de la opción seleccionada.")
                        "paladar_duro" to "Úlcera traumática" -> Triple(R.drawable.allimg_105_ulcera_traumatica_paladar,"Úlcera traumática","Imagen de la opción seleccionada.")
                        "paladar_duro" to "Lesión pigmentada a valorar" -> Triple(R.drawable.allimg_057_lesion_pigmentaria_pladar_a_valorar,"Lesión pigmentada","Imagen de la opción seleccionada.")
                        "paladar_blando" to "Eritema inflamatorio" -> Triple(R.drawable.allimg_032_eritema_inflaatorio_paladar_blando,"Eritema inflamatorio","Imagen de la opción seleccionada.")
                        "paladar_blando" to "Petequias" -> Triple(R.drawable.allimg_076_petquias_paladar_blando,"Petequias","Imagen de la opción seleccionada.")
                        "paladar_blando" to "Candidiasis" -> Triple(R.drawable.allimg_015_candidiasis_paladar_blando,"Candidiasis","Imagen de la opción seleccionada.")
                        "paladar_blando" to "Úlcera aftosa" -> Triple(R.drawable.allimg_101_ulcera_aftosa_pladar_blando,"Úlcera aftosa","Imagen de la opción seleccionada.")
                        "paladar_blando" to "Lesión viral a valorar" -> Triple(R.drawable.allimg_060_lesion_viral_en_pladar_blando,"Lesión viral","Imagen de la opción seleccionada.")
                        "paladar_blando" to "Asimetría funcional a valorar" -> Triple(R.drawable.allimg_010_asimetria_funcional_paladar_a_valorar,"Asimetría funcional","Imagen de la opción seleccionada.")
                        "lengua" to "Lengua geográfica" -> Triple(R.drawable.allimg_053_lengua_geografica,"Lengua geográfica","Imagen de la opción seleccionada.")
                        "lengua" to "Lengua fisurada" -> Triple(R.drawable.allimg_052_lengua_fisurada,"Lengua fisurada","Imagen de la opción seleccionada.")
                        "lengua" to "Lengua saburral" -> Triple(R.drawable.allimg_054_lengua_saburral,"Lengua saburral","Imagen de la opción seleccionada.")
                        "lengua" to "Candidiasis" -> Triple(R.drawable.allimg_014_candidiasis_lengua,"Candidiasis lingual","Imagen de la opción seleccionada.")
                        "lengua" to "Úlcera traumática" -> Triple(R.drawable.allimg_104_ulcera_traumatica_lengua,"Úlcera traumática de lengua","Imagen de la opción seleccionada.")
                        "lengua" to "Fibroma traumático" -> Triple(R.drawable.allimg_044_fibroma_traumatico_lengua,"Fibroma traumático de lengua","Imagen de la opción seleccionada.")
                        "piso" to "Ránula" -> Triple(R.drawable.allimg_093_ranula_piso_de_boca,"Ránula","Imagen de la opción seleccionada.")
                        "piso" to "Sialolitiasis/obstrucción a valorar" -> Triple(R.drawable.allimg_098_sialolitiasis,"Sialolitiasis","Imagen de la opción seleccionada.")
                        "piso" to "Quiste/masa a valorar" -> Triple(R.drawable.allimg_092_quiste_piso_de_boca,"Quiste / masa","Imagen de la opción seleccionada.")
                        "piso" to "Lesión vascular a valorar" -> Triple(R.drawable.allimg_059_lesion_vascular_piso_de_boca,"Lesión vascular","Imagen de la opción seleccionada.")
                        "piso" to "Úlcera traumática" -> Triple(R.drawable.allimg_106_ulcera_traumatica_piso_de_boca,"Úlcera traumática","Imagen de la opción seleccionada.")
                        "orofaringe" to "Faringoamigdalitis a valorar" -> Triple(R.drawable.allimg_042_faringo_amigdalitis_streptocica,"Faringoamigdalitis","Imagen de la opción seleccionada.")
                        "orofaringe" to "Hipertrofia amigdalina" -> Triple(R.drawable.allimg_051_hipertrofia_amigdalina,"Hipertrofia amigdalina","Imagen de la opción seleccionada.")
                        "orofaringe" to "Exudado amigdalino" -> Triple(R.drawable.allimg_039_exudado_amigdalino,"Exudado amigdalino","Imagen de la opción seleccionada.")
                        "orofaringe" to "Úlcera/lesión mucosa" -> Triple(R.drawable.allimg_102_ulcera_orofaringe,"Úlcera / lesión mucosa","Imagen de la opción seleccionada.")
                        "orofaringe" to "Asimetría amigdalina a valorar" -> Triple(R.drawable.allimg_009_asimetria_amigdalina,"Asimetría amigdalina","Imagen de la opción seleccionada.")
                        else -> null
                    }
                    pathologyVisual?.let{v->
                        LocalClinicalInlineZoomImageV48(lang,v.second,v.second,v.first,v.third,v.third)
                    }
                }
                Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
                    Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
                        Text(tr(lang,"Cómo explorar","How to examine"),fontWeight=FontWeight.Bold)
                        Text(if(lang=="en")selected.exploreEn else selected.exploreEs)
                        Text(tr(lang,"Cambios que pueden observarse","Changes that may be observed"),fontWeight=FontWeight.Bold)
                        Text(if(lang=="en")selected.changesEn else selected.changesEs)
                    }
                }
            }
        }
        // Las imágenes regionales ya no se muestran como galerías; cada imagen aparece sólo al seleccionar su opción.
        if(tissueStatus[selected.id]=="Alteración" && !tissueLesion[selected.id].isNullOrBlank()){
            ResponsiveSectionV17(tr(lang,"Características de la alteración seleccionada","Characteristics of the selected alteration")) {
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

        NoticeCard(tr(lang,"Describe antes de diagnosticar. Lesiones persistentes, induradas, ulceradas sin causa clara, masas o crecimiento requieren supervisión docente/profesional y seguimiento.","Describe before diagnosing. Persistent, indurated, unexplained ulcerated lesions, masses or growth require faculty/professional assessment and follow-up."))
    }
}


@Composable private fun MucosaPick19(label:String,options:List<String>,selected:String,onSelected:(String)->Unit){
    Text(label,fontWeight=FontWeight.Bold)
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)){
        options.forEach{o->FilterChip(selected==o,{onSelected(o)},{Text(o)})}
    }
}
