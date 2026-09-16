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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class MucosaRegionV40(val name:String,val normal:String,val alterations:List<Pair<String,String>>)
private val mucosaRegionsV40=listOf(
    MucosaRegionV40("Labio superior","Borde bermellón y mucosa interna íntegros, húmedos, sin ulceración, masa ni placa persistente.",listOf("Queilitis" to "Inflamación/fisuración; valorar irritantes, clima, contacto y causas sistémicas.","Herpes labial" to "Vesículas/erosiones recurrentes típicamente en borde bermellón; integrar historia.","Úlcera traumática" to "Defecto ulcerado relacionado con trauma local; debe resolver al retirar causa.")),
    MucosaRegionV40("Labio inferior","Mucosa rosada y flexible; glándulas menores pueden palparse sin ser patológicas.",listOf("Mucocele" to "Aumento fluctuante/translúcido o azulado, frecuente tras trauma de glándula menor.","Úlcera" to "Trauma, afta u otra etiología según patrón y duración.","Lesión actínica" to "Cambio crónico del bermellón por exposición solar; requiere valoración si persiste.")),
    MucosaRegionV40("Mucosa labial","Rosada, húmeda, móvil; frenillos y vasos finos son normales.",listOf("Afta" to "Úlcera redonda/oval dolorosa con halo eritematoso en mucosa no queratinizada.","Fibroma traumático" to "Nódulo firme reactivo en zona de trauma repetido.","Pigmentación" to "Mácula; describir color, bordes, evolución y causas locales.")),
    MucosaRegionV40("Mucosa yugal","Rosada, lisa y húmeda; línea alba y gránulos de Fordyce pueden ser variantes.",listOf("Morsicatio" to "Placa blanca irregular por mordisqueo crónico.","Liquen plano/lesión liquenoide" to "Estrías blancas reticulares; puede coexistir eritema/erosión.","Candidiasis" to "Placas blancas removibles o eritema según forma clínica; confirmar contexto.")),
    MucosaRegionV40("Fondo de saco / vestíbulo","Mucosa móvil sin tumefacción, fístula ni ulceración.",listOf("Fístula odontógena" to "Punto de drenaje que obliga a buscar diente/lesión causal con pruebas e imagen.","Absceso localizado" to "Aumento de volumen doloroso/fluctuante; valorar extensión y signos sistémicos.","Trauma" to "Ulceración por prótesis, borde dentario u objeto.")),
    MucosaRegionV40("Encía","Margen adaptado, textura y color compatibles con salud; pigmentación fisiológica es variable.",listOf("Gingivitis" to "Eritema, edema y sangrado; diagnóstico se integra con biofilm y sondaje.","Hiperplasia gingival" to "Aumento de volumen; considerar inflamación, fármacos y causas sistémicas.","Lesión descamativa" to "Eritema/descamación puede acompañar enfermedades mucocutáneas; requiere diferencial.")),
    MucosaRegionV40("Dorso de lengua","Papilas visibles, simetría y superficie sin masa/induración persistente.",listOf("Lengua geográfica" to "Áreas depapiladas eritematosas con borde blanco migratorio.","Lengua fisurada" to "Surcos de profundidad variable; variante frecuente.","Saburra lingual" to "Recubrimiento superficial por detritos/biofilm; distinguir de placas patológicas.")),
    MucosaRegionV40("Bordes laterales de lengua","Mucosa blanda; papilas foliadas posteriores pueden ser visibles.",listOf("Úlcera traumática" to "Frecuente por borde dental; reevaluar tras eliminar causa.","Leucoplasia / lesión blanca persistente" to "Placa blanca no explicada por otra entidad; requiere diagnóstico diferencial.","Eritroplasia / lesión roja persistente" to "Área roja persistente sin causa obvia; prioridad diagnóstica y biopsia cuando corresponda.")),
    MucosaRegionV40("Cara ventral de lengua","Mucosa fina/translúcida con venas superficiales visibles.",listOf("Varicosidades" to "Venas dilatadas, frecuentes con edad; evaluar si atípicas/sintomáticas.","Úlcera" to "Trauma, afta u otras causas.","Lesión pigmentada" to "Describir y valorar cambios/asimetría.")),
    MucosaRegionV40("Piso de boca","Mucosa fina, húmeda y blanda; carúnculas y pliegues sublinguales son normales.",listOf("Ránula" to "Aumento translúcido/azulado relacionado con glándula sublingual.","Eritroplasia/lesión roja" to "Sitio de riesgo cuando es persistente e inexplicado.","Sialolitiasis / obstrucción" to "Dolor/aumento con comidas y alteración de flujo; valorar conductos salivales.")),
    MucosaRegionV40("Paladar duro","Mucosa queratinizada firme con rugas anteriores y rafe medio.",listOf("Torus palatino" to "Exostosis ósea media, dura y recubierta por mucosa normal.","Lesión nicotínica/calor" to "Cambios queratósicos/puntos eritematosos en exposición crónica; contextualizar.","Herpes intraoral recurrente" to "Vesículas/úlceras agrupadas en mucosa queratinizada en patrones típicos.")),
    MucosaRegionV40("Paladar blando","Mucosa más rojiza, no queratinizada y móvil.",listOf("Petequias" to "Puntos hemorrágicos; trauma, infección u otras causas según contexto.","Eritema" to "Inflamación/irritación; integrar síntomas sistémicos.","Úlcera/lesión persistente" to "Requiere diferencial si no resuelve.")),
    MucosaRegionV40("Orofaringe","Pilares y tejido linfoide simétricos dentro de variación normal.",listOf("Faringoamigdalitis" to "Eritema/exudado con síntomas sistémicos; diagnóstico médico según caso.","Hipertrofia amigdalina" to "Aumento de volumen bilateral/asimétrico; valorar síntomas y vía aérea.","Lesión persistente" to "Masa, ulceración o asimetría persistente requiere valoración especializada.")),
    MucosaRegionV40("Comisuras labiales","Integridad sin fisuras, maceración o costra persistente.",listOf("Queilitis angular" to "Fisura/eritema; puede ser multifactorial: humedad, Candida, prótesis, deficiencias, etc.","Trauma" to "Fisura mecánica o mordedura.","Dermatitis de contacto" to "Relacionar con cosméticos/materiales y distribución."))
)

@Composable
fun MucosaAtlasV40Screen(lang:String,onBack:()->Unit){
    var region by remember{mutableStateOf(0)}
    var lesion by remember{mutableStateOf<Int?>(null)}
    ResponsiveScreenV17("Examen de mucosas · atlas sin dientes","Sólo tejidos blandos: toca una región para revisar normalidad, alteraciones y diagnóstico presuntivo/diferencial.",onBack){profile->
        NoticeCard("La imagen deliberadamente no muestra dientes. El objetivo es reconocer mucosa y describir color, superficie, borde, tamaño, consistencia, síntomas, tiempo y localización antes de proponer un diagnóstico.")
        ResponsiveSectionV17("Mapa de mucosas"){
            MucosaOnlyIllustrationV40(mucosaRegionsV40[region].name,lesion)
            AdaptiveGridV17(mucosaRegionsV40.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->FilterChip(region==i,{region=i;lesion=null},{Text(mucosaRegionsV40[i].name)},Modifier.fillMaxWidth())}
        }
        ResponsiveSectionV17("Aspecto normal") { Text(mucosaRegionsV40[region].normal) }
        ResponsiveSectionV17("Alteraciones clínicas frecuentes"){
            mucosaRegionsV40[region].alterations.forEachIndexed{i,(name,detail)->
                Card(onClick={lesion=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(lesion==i)MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.35f))){Column(Modifier.padding(10.dp)){Text(name,fontWeight=FontWeight.Black);if(lesion==i){Text(detail);Text("Diagnóstico: presuntivo/diferencial; confirmar con historia, palpación, pruebas, seguimiento y biopsia cuando esté indicada.")}}}
            }
        }
        ResponsiveSectionV17("Resumen de mucosa"){
            val chosen=lesion?.let{mucosaRegionsV40[region].alterations[it]}
            val summary=if(chosen==null)"${mucosaRegionsV40[region].name}: apariencia normal revisada; sin alteración seleccionada en el ejercicio." else "${mucosaRegionsV40[region].name}: ${chosen.first} · ${chosen.second}"
            Text(summary)
            Card(onClick={TeachingStateV40.moduleSummaries["mucosa"]=summary},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Text("Guardar resumen de mucosas",Modifier.padding(12.dp),fontWeight=FontWeight.Black)}
        }
        NoticeCard("Alarma: lesión persistente, indurada, roja/blanca inexplicada, ulceración que no cicatriza, crecimiento, sangrado espontáneo o alteración neurológica debe valorarse oportunamente.")
    }
}

@Composable private fun MucosaOnlyIllustrationV40(region:String,lesion:Int?){
    Canvas(Modifier.fillMaxWidth().height(270.dp).padding(8.dp)){
        val w=size.width;val h=size.height
        val outer=Path().apply{moveTo(w*.12f,h*.50f);cubicTo(w*.24f,h*.18f,w*.39f,h*.17f,w*.50f,h*.30f);cubicTo(w*.61f,h*.17f,w*.76f,h*.18f,w*.88f,h*.50f);cubicTo(w*.75f,h*.82f,w*.61f,h*.82f,w*.50f,h*.70f);cubicTo(w*.39f,h*.82f,w*.25f,h*.82f,w*.12f,h*.50f);close()}
        drawPath(outer,Color(0xFFE5858F));drawPath(outer,Color(0xFF8B4650),style=Stroke(3f))
        val cavity=Path().apply{moveTo(w*.20f,h*.50f);cubicTo(w*.30f,h*.30f,w*.43f,h*.31f,w*.50f,h*.40f);cubicTo(w*.57f,h*.31f,w*.70f,h*.30f,w*.80f,h*.50f);cubicTo(w*.69f,h*.68f,w*.58f,h*.66f,w*.50f,h*.60f);cubicTo(w*.42f,h*.66f,w*.31f,h*.68f,w*.20f,h*.50f);close()}
        drawPath(cavity,Color(0xFFF1A6AA))
        // Palate and tongue are soft-tissue forms only; no teeth are drawn.
        val palate=Path().apply{moveTo(w*.33f,h*.43f);quadraticBezierTo(w*.50f,h*.25f,w*.67f,h*.43f);quadraticBezierTo(w*.50f,h*.50f,w*.33f,h*.43f);close()}
        drawPath(palate,Color(0xFFF5B7B2))
        val tongue=Path().apply{moveTo(w*.31f,h*.57f);cubicTo(w*.38f,h*.47f,w*.62f,h*.47f,w*.69f,h*.57f);cubicTo(w*.65f,h*.72f,w*.56f,h*.76f,w*.50f,h*.73f);cubicTo(w*.44f,h*.76f,w*.35f,h*.72f,w*.31f,h*.57f);close()}
        drawPath(tongue,Color(0xFFE77883));drawPath(tongue,Color(0xFFA64E59),style=Stroke(2f))
        val highlight=MaterialTheme.colorScheme.primary.copy(alpha=.35f)
        val center=when{
            region.contains("Labio superior")->Offset(w*.50f,h*.32f)
            region.contains("Labio inferior")->Offset(w*.50f,h*.70f)
            region.contains("yugal")->Offset(w*.25f,h*.50f)
            region.contains("Encía")->Offset(w*.50f,h*.49f)
            region.contains("Dorso")->Offset(w*.50f,h*.59f)
            region.contains("Bordes")->Offset(w*.65f,h*.59f)
            region.contains("ventral")->Offset(w*.50f,h*.68f)
            region.contains("Piso")->Offset(w*.50f,h*.72f)
            region.contains("duro")->Offset(w*.50f,h*.39f)
            region.contains("blando")||region.contains("Orofaringe")->Offset(w*.50f,h*.44f)
            region.contains("Comisuras")->Offset(w*.16f,h*.50f)
            else->Offset(w*.50f,h*.50f)
        }
        drawCircle(highlight,w*.08f,center);drawCircle(MaterialTheme.colorScheme.primary,w*.08f,center=center,style=Stroke(3f))
        if(lesion!=null){drawOval(Color(0xFFB72E3D).copy(alpha=.75f),Offset(center.x-w*.035f,center.y-h*.025f),Size(w*.07f,h*.05f))}
    }
}

private val evolutionExamplesV40=listOf(
    "Revisión preventiva y actualización de historia clínica","Profilaxis y control de biofilm","Aplicación de barniz de fluoruro","Sellador de fosetas y fisuras","Instrucción de técnica de cepillado","Instrucción de higiene interdental","Control CAMBRA y plan preventivo","Restauración con resina clase I","Restauración con resina clase II","Restauración con resina clase III","Restauración con resina clase IV","Restauración con resina clase V","Restauración provisional/ITR","Técnica ART","Remoción selectiva de caries/IPT","Corona preformada de acero","Técnica Hall","Aplicación de SDF para arresto de caries","Pulpotomía en diente temporal","Pulpectomía en diente temporal","LSTR/CTZ en caso seleccionado","Terapia pulpar vital en permanente","Pulpotomía parcial en permanente joven","Pulpotomía completa en permanente","Tratamiento de conductos unirradicular","Tratamiento de conductos multirradicular","Conductometría/localizador apical","Irrigación y preparación químico-mecánica","Obturación del sistema de conductos","Apexificación","Procedimiento regenerativo en permanente inmaduro","Control endodóntico clínico-radiográfico","Raspado y alisado radicular","Control periodontal no quirúrgico","Periodontograma de reevaluación","Mantenimiento periodontal","Curetaje abierto/acceso periodontal","Alargamiento de corona","Extracción simple","Extracción de resto radicular","Extracción múltiple","Extracción de tercer molar","Alveoloplastia/regularización del reborde","Biopsia incisional","Biopsia excisional","Frenectomía/frenotomía","Control de herida y retiro de suturas","Ajuste de prótesis removible","Impresión para prótesis","Prueba/entrega de prótesis y educación de higiene","Valoración de ATM/TTM y autocuidado","Evaluación de oclusión y registro","Valoración de hábito/parafunción","Control de erupción/posición dental","Interconsulta/remisión a especialidad")

@Composable
fun EvolutionExamplesV40Screen(lang:String,onBack:()->Unit){
    var selected by remember{mutableStateOf(0)}
    ResponsiveScreenV17("Notas de evolución · 50+ ejemplos","Selecciona un tratamiento para ver un ejemplo de estructura de nota. No captura datos reales.",onBack){profile->
        NoticeCard("Una nota real depende de normativa e institución. Debe documentar fecha/hora, evaluación relevante, procedimiento, materiales/anestesia cuando corresponda, respuesta, indicaciones, complicaciones, plan y firmas requeridas.")
        AdaptiveGridV17(evolutionExamplesV40.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->FilterChip(selected==i,{selected=i},{Text(evolutionExamplesV40[i])},Modifier.fillMaxWidth())}
        ResponsiveSectionV17("Ejemplo de redacción"){
            val tx=evolutionExamplesV40[selected]
            Text("Ejemplo educativo: “Se realiza $tx conforme a diagnóstico y plan previamente establecidos. Se verifican antecedentes relevantes y condiciones clínicas del ejercicio. Procedimiento efectuado bajo técnica indicada y supervisión correspondiente. Sin complicaciones inmediatas en el caso simulado. Se explican cuidados, signos de alarma y seguimiento”.")
            val saved=TeachingStateV40.toothPlans.toList().sortedBy{it.second.priority}
            if(saved.isNotEmpty()){
                Text("También puedes basarte en los tratamientos guardados:",fontWeight=FontWeight.Black)
                saved.forEach{(tooth,plan)->Text("• OD $tooth · ${plan.treatmentLabel} · Dx ${plan.diagnosisLabel}")}
            }
        }
    }
}
