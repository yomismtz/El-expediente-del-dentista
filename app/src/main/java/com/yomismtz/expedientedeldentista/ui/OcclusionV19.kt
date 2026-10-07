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
        listOf("Temporal","Mixta temprana","Mixta tardía","Permanente joven","Permanente adulta","No valorable"),
        listOf("Recto bilateral","Mesial bilateral","Distal bilateral","Asimétrico","No valorable"),
        listOf("Derecha Clase I","Derecha Clase II","Derecha Clase III","Izquierda Clase I","Izquierda Clase II","Izquierda Clase III","No valorable"),
        listOf("Derecha Clase I","Derecha Clase II","Derecha Clase III","Izquierda Clase I","Izquierda Clase II","Izquierda Clase III","No valorable"),
        listOf("Relación habitual","Aumentado","Reducido","Borde a borde","Invertido","No medido"),
        listOf("Traslape vertical habitual","Profunda","Borde a borde","Abierta anterior","Abierta posterior","No valorable"),
        listOf("Sin mordida cruzada","Anterior","Posterior derecha","Posterior izquierda","Posterior bilateral","Con desplazamiento funcional","No valorable"),
        listOf("Líneas medias coincidentes","Superior desviada derecha","Superior desviada izquierda","Inferior desviada derecha","Inferior desviada izquierda","Discrepancia superior-inferior","No valorable"),
        listOf("Sin alteración aparente","Apiñamiento leve","Apiñamiento moderado","Apiñamiento severo","Diastemas/espacios","Rotaciones/inclinaciones"),
        listOf("Sin alteración aparente","Ovoide simétrica","Triangular","Cuadrada/amplia","Estrecha","Asimétrica"),
        listOf("Sin alteración aparente","Curva de Spee discreta","Curva aumentada","Curva plana","Mordida profunda","Mordida abierta"),
        listOf("Relación transversal habitual","Cruzada unilateral","Cruzada bilateral","Mordida en tijera/Brodie","Asimetría transversal","No valorable"),
        listOf("Contactos posteriores bilaterales","Predominio derecho","Predominio izquierdo","Contacto prematuro aparente","Ausencia de contacto posterior","No valorable"),
        listOf("Cierre sin desplazamiento","Deslizamiento funcional derecho","Deslizamiento funcional izquierdo","Discrepancia RC/MI aparente","No valorable"),
        listOf("Sin desgaste aparente","Facetas anteriores","Facetas posteriores","Generalizado","Unilateral","Severo","No valorable"),
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
    ResponsiveScreenV17(tr(lang,"Examen clínico de oclusión","Clinical occlusal examination"),tr(lang,"Exploración por subapartados con registro seleccionable e imágenes asociadas a cada hallazgo.","Sectioned examination with selectable findings and images associated with each finding."),onBack) { profile ->
        ResponsiveSectionV17("Subapartados") {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                sections.forEachIndexed{i,s->FilterChip(selected==i,{selected=i;choice=""},{Text(s)})}
            }
        }
        ResponsiveSectionV17(sections[selected]) {
            Text(help[selected],fontWeight=FontWeight.SemiBold)
            Text("Registro clínico",fontWeight=FontWeight.Black)
            AdaptiveGridV17(options[selected].size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 3) { i ->
                val o=options[selected][i]
                FilterChip(choice==o,{choice=o},{Text(o)},modifier=Modifier.fillMaxWidth())
            }
            val visual=occlusionChoiceVisual19(selected,choice)
            if(visual!=null){
                Card(Modifier.fillMaxWidth()){Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                    Text(visual.first,fontWeight=FontWeight.Bold)
                    Text(tr(lang,"¿Qué es?","What is it?"),fontWeight=FontWeight.SemiBold)
                    Text(visual.third)
                    Text(tr(lang,"¿Qué observar?","What to observe?"),fontWeight=FontWeight.SemiBold)
                    Text(tr(lang,"Compara la relación dental seleccionada con el hallazgo clínico y registra lateralidad, magnitud y simetría cuando correspondan.","Compare the selected dental relationship with the clinical finding and record laterality, magnitude and symmetry when appropriate."))
                    Text(tr(lang,"Imagen","Image"),fontWeight=FontWeight.SemiBold)
                    LocalClinicalInlineZoomImageV48(lang,visual.first,visual.first,visual.second,visual.third,visual.third)
                }}
            }
        }
        LocalClinicalImageSectionV46(lang,profile,"Oclusión","Occlusion")
        NoticeCard(tr(lang,"Las imágenes aparecen al seleccionar el hallazgo correspondiente. Registrar un hallazgo no equivale a emitir automáticamente un diagnóstico.","Images appear when the corresponding finding is selected. Recording a finding does not automatically establish a diagnosis."))
    }
}

private fun occlusionChoiceVisual19(section:Int,choice:String):Triple<String,Int,String>?=when(section){
 0->when(choice){
  "Temporal"->Triple("Dentición temporal",R.drawable.new17_dentcion_temporal,"Imagen de dentición temporal.")
  "Mixta temprana"->Triple("Dentición mixta temprana",R.drawable.new17_denticion_mixta_temprana,"Imagen de dentición mixta temprana.")
  "Mixta tardía"->Triple("Dentición mixta tardía",R.drawable.new17_denticion_mixta_tardia,"Imagen de dentición mixta tardía.")
  "Permanente joven"->Triple("Dentición permanente joven",R.drawable.new17_denticion_permanente_joven,"Imagen de dentición permanente joven.")
  "Permanente adulta"->Triple("Dentición permanente adulta",R.drawable.new17_denticion_permanente_adulta,"Imagen de dentición permanente adulta.")
  "No valorable"->Triple("Dentición no valorable / edentulismo",R.drawable.new17_denticion_no_valorable_edentulismo,"Imagen de la opción no valorable.")
  else->null}
 1->when(choice){
  "Recto bilateral"->Triple("Plano terminal recto",R.drawable.new77_plano_terminalm_recto,"Imagen de plano terminal recto.")
  "Mesial bilateral"->Triple("Plano terminal mesial",R.drawable.new77_plano_terminal_mesial,"Imagen de escalón mesial.")
  "Distal bilateral"->Triple("Plano terminal distal",R.drawable.new77_plano_terminal_distal,"Imagen de escalón distal.")
  "No valorable"->Triple("Planos terminales · no valorables",R.drawable.new77_planos_terminales_no_valorables,"Referencia local para cuando la relación terminal no puede valorarse.")
  else->null}
 2->when(choice){
  "Derecha Clase I"->Triple("Angle molar derecha · Clase I",R.drawable.new77_clase_i_molar_derecha,"Imagen específica de relación molar derecha Clase I.")
  "Derecha Clase II"->Triple("Angle molar derecha · Clase II",R.drawable.new77_clase_ii_molar_derecha,"Imagen específica de relación molar derecha Clase II.")
  "Derecha Clase III"->Triple("Angle molar derecha · Clase III",R.drawable.new77_clase_iii_molar_derecha,"Imagen específica de relación molar derecha Clase III.")
  "Izquierda Clase I"->Triple("Angle molar izquierda · Clase I",R.drawable.new77_clase_i_molar_izquierda,"Imagen específica de relación molar izquierda Clase I.")
  "Izquierda Clase II"->Triple("Angle molar izquierda · Clase II",R.drawable.new77_clase_ii_molar_izquierda,"Imagen específica de relación molar izquierda Clase II.")
  "Izquierda Clase III"->Triple("Angle molar izquierda · Clase III",R.drawable.new77_clase_iii_molar_izquierda,"Imagen específica de relación molar izquierda Clase III.")
  "No valorable"->Triple("Clasificación de Angle · no valorable",R.drawable.new77_clasificacion_de_angle_no_valorable,"Imagen de la opción seleccionada.")
  else->null}
 3->when(choice){
  "Derecha Clase I"->Triple("Relación canina derecha · Clase I",R.drawable.new77_clase_i_canina_derecha,"Imagen específica de relación canina derecha Clase I.")
  "Derecha Clase II"->Triple("Relación canina derecha · Clase II",R.drawable.new77_clase_ii_canina_derecha,"Imagen específica de relación canina derecha Clase II.")
  "Derecha Clase III"->Triple("Relación canina derecha · Clase III",R.drawable.new77_clase_iii_canina_derecha,"Imagen específica de relación canina derecha Clase III.")
  "Izquierda Clase I"->Triple("Relación canina izquierda · Clase I",R.drawable.new77_clase_i_canina_izquierda,"Imagen específica de relación canina izquierda Clase I.")
  "Izquierda Clase II"->Triple("Relación canina izquierda · Clase II",R.drawable.new77_clase_ii_canina_izquierda,"Imagen específica de relación canina izquierda Clase II.")
  "Izquierda Clase III"->Triple("Relación canina izquierda · Clase III",R.drawable.new77_clase_iii_canina_izquierda,"Imagen específica de relación canina izquierda Clase III.")
  "No valorable"->Triple("Relación canina · no valorable",R.drawable.new77_relacion_canina_no_valorable,"Imagen de la opción seleccionada.")
  else->null}
 4->when(choice){
  "Positivo habitual"->Triple("Overjet normal",R.drawable.new77_edu_oclusion_overjet_normal,"Imagen de la opción seleccionada.")
  "Aumentado"->Triple("Overjet positivo / aumentado",R.drawable.new77_edu_oclusion_overjet_positivo,"Imagen de la opción seleccionada.")
  "Reducido","Borde a borde","Invertido"->Triple("Overjet disminuido / negativo",R.drawable.new77_edu_oclusion_overjet_disminuido_negativo,"Imagen de la opción seleccionada.")
  "No medido"->Triple("Overjet · no valorable",R.drawable.new77_edu_oclusion_overjet_no_valorable,"Imagen de la opción seleccionada.")
  else->null}
 5->when(choice){
  "Traslape habitual"->Triple("Overbite normal",R.drawable.new77_edu_oclusion_overbite_normal,"Imagen de la opción seleccionada.")
  "Profunda"->Triple("Overbite aumentado / mordida profunda",R.drawable.new77_edu_oclusion_overbite_aqumentado_o_positivo,"Imagen de la opción seleccionada.")
  "Borde a borde"->Triple("Overbite disminuido",R.drawable.new77_edu_oclusion_overbite_negativo_disminuido,"Imagen de la opción seleccionada.")
  "Abierta anterior"->Triple("Mordida abierta anterior",R.drawable.edu_oclusion_mordida_abierta,"Imagen de la opción seleccionada.")
  "Abierta posterior"->Triple("Mordida abierta posterior",R.drawable.edu_oclusion_mordida_abierta_posterior,"Imagen de la opción seleccionada.")
  "No valorable"->Triple("Overbite · no valorable",R.drawable.new77_edu_oclusion_overbite_no_valorable,"Imagen de la opción seleccionada.")
  else->null}
 6->when(choice){"Anterior"->Triple("Mordida cruzada anterior",R.drawable.edu_oclusion_cruzada_anterior,"Imagen de mordida cruzada anterior.");"Posterior derecha","Posterior izquierda"->Triple("Mordida cruzada posterior unilateral",R.drawable.edu_oclusion_cruzada_posterior_unilateral,"Imagen de mordida cruzada posterior unilateral.");"Posterior bilateral"->Triple("Mordida cruzada posterior bilateral",R.drawable.edu_oclusion_cruzada_posterior_bilateral,"Imagen de mordida cruzada posterior bilateral.");else->null}
 7->when(choice){
  "Coincidentes"->Triple("Línea media centrada",R.drawable.new77_edu_oclusion_linea_media_centrada,"Imagen de la opción seleccionada.")
  ""->null
  else->Triple("Línea media desviada",R.drawable.new77_edu_oclusion_linea_media_desviada,"Imagen de la opción seleccionada.")}
 8->when(choice){
  "Apiñamiento leve","Apiñamiento moderado"->Triple("Apiñamiento dental anterior",R.drawable.allimg_006_apinamiento_dental_anterior,"Imagen clínica local de apiñamiento anterior.")
  "Apiñamiento severo"->Triple("Apiñamiento dental",R.drawable.allimg_008_apinamiento_dental,"Imagen clínica local de apiñamiento.")
  "Diastemas/espacios"->Triple("Espaciamiento / diastema",R.drawable.allimg_035_espaciamiento_dental_diastema,"Imagen clínica local de espaciamiento dental.")
  else->null}
 9->when(choice){
  "Sin alteración aparente"->null
  "Ovoide simétrica"->Triple("Arcada ovoide",R.drawable.new17_arcada_de_forma_ovoide,"Imagen de forma de arcada ovoide.")
  "Triangular"->Triple("Arcada triangular",R.drawable.new17_arcada_de_forma_triangular,"Imagen de forma de arcada triangular.")
  "Cuadrada/amplia"->Triple("Arcada cuadrada",R.drawable.new17_arcada_de_forma_cuadrada,"Imagen de forma de arcada cuadrada.")
  "Estrecha"->Triple("Arcada estrecha",R.drawable.new17_arcada_de_forma_estrecha,"Imagen de arcada estrecha.")
  "Asimétrica"->Triple("Arcada asimétrica",R.drawable.new17_arcada_de_forma_asimetrica,"Imagen de asimetría de arcada.")
  else->null}
 10->when(choice){
  "Sin alteración aparente"->null
  "Curva de Spee discreta"->Triple("Curva de Spee discreta",R.drawable.allimg_021_curva_de_spee_discreta,"Imagen local.")
  "Curva aumentada"->Triple("Curva de Spee profunda",R.drawable.allimg_024_curva_de_spee_profunda,"Imagen local.")
  "Curva plana"->Triple("Curva de Spee plana",R.drawable.allimg_022_curva_de_spee_plana,"Imagen local.")
  "Mordida profunda"->Triple("Mordida profunda",R.drawable.edu_oclusion_mordida_profunda_1,"Imagen.")
  "Mordida abierta"->Triple("Mordida abierta",R.drawable.edu_oclusion_mordida_abierta,"Imagen.")
  else->null}
 11->when(choice){
  "Relación transversal habitual"->Triple("Relación transversal habitual",R.drawable.allimg_095_relacion_transversal_habitual,"Imagen clínica local.")
  "Cruzada unilateral"->Triple("Cruzada posterior unilateral",R.drawable.edu_oclusion_cruzada_posterior_unilateral_2,"Imagen.")
  "Cruzada bilateral"->Triple("Cruzada posterior bilateral",R.drawable.edu_oclusion_cruzada_posterior_bilateral_2,"Imagen.")
  "Mordida en tijera/Brodie"->Triple("Mordida de Brodie",R.drawable.allimg_012_brodie_bite,"Imagen clínica local.")
  "Asimetría transversal"->Triple("Asimetría transversal oclusal",R.drawable.allimg_011_asimetria_transversal_oclusal,"Imagen clínica local.")
  else->null}
 12->when(choice){
  "Posteriores bilaterales"->Triple("Contactos oclusales bilaterales",R.drawable.allimg_019_contactos_oclusales_bilaterales,"Imagen local.")
  "Predominio derecho"->Triple("Contacto oclusal predominante derecho",R.drawable.allimg_017_contacto_oclusal_predominante_derecho,"Imagen local.")
  "Predominio izquierdo"->Triple("Contacto oclusal predominante izquierdo",R.drawable.allimg_018_contacto_oclusal_predominante_izquierdo,"Imagen local.")
  "Contacto prematuro aparente"->Triple("Contacto prematuro",R.drawable.allimg_020_contatco_preaturo_de_contacto_anterior,"Imagen local.")
  else->null}
 13->when(choice){
  "Cierre sin desplazamiento"->Triple("Cierre sin desplazamiento",R.drawable.allimg_001_cierre_sin_desplazamiento,"Imagen local.")
  "Deslizamiento funcional derecho"->Triple("Desplazamiento funcional a la derecha",R.drawable.allimg_028_desplazamiento_funcional_a_la_derecha,"Imagen local.")
  "Deslizamiento funcional izquierdo"->Triple("Desplazamiento funcional a la izquierda",R.drawable.allimg_029_desplazamiento_funcional_a_la_izquierda,"Imagen local.")
  "Discrepancia RC/MI aparente"->Triple("Discrepancia entre RC y máxima intercuspidación",R.drawable.allimg_030_discrepancia_entre_maxima_interscupidacion_y_,"Imagen local.")
  "No valorable"->Triple("Máxima intercuspidación · no valorable",R.drawable.allimg_068_maxima_intercupidacion_no_valorable,"Imagen local de la opción no valorable.")
  else->null}
 14->when(choice){
  "Sin desgaste aparente"->Triple("Sin desgaste aparente",R.drawable.allimg_004_sin_desgaste_aparente,"Imagen clínica local.")
  "Facetas anteriores"->Triple("Facetas anteriores",R.drawable.allimg_040_facetas_anteriores,"Imagen clínica local.")
  "Facetas posteriores"->Triple("Facetas posteriores",R.drawable.allimg_041_facetas_posteriorers,"Imagen clínica local.")
  "Generalizado"->Triple("Desgaste generalizado",R.drawable.allimg_025_desgaste_generalizado,"Imagen clínica local.")
  "Unilateral"->Triple("Desgaste oclusal unilateral",R.drawable.allimg_027_desgasteoclusal_unilateral,"Imagen clínica local.")
  "Severo"->Triple("Desgaste oclusal severo",R.drawable.allimg_026_desgaste_oclusal_severo,"Imagen clínica local.")
  else->null}
 15->when(choice){
  "Sin alteración adicional"->Triple("Sin alteración oclusal adicional",R.drawable.allimg_071_oclusion_sin_alteraciona_dicional,"Imagen local de la opción seleccionada.")
  "Protrusión incisiva"->Triple("Protrusión incisiva",R.drawable.allimg_084_protrusion_incisiva,"Imagen clínica local de protrusión incisiva.")
  "Apiñamiento"->Triple("Apiñamiento dental",R.drawable.allimg_008_apinamiento_dental,"Imagen clínica local de apiñamiento.")
  "Espaciamiento"->Triple("Espaciamiento dental",R.drawable.allimg_036_espaciamiento_dental,"Imagen clínica local de espaciamiento.")
  "Mordida profunda"->Triple("Mordida profunda",R.drawable.edu_oclusion_mordida_profunda,"Imagen.")
  "Mordida abierta"->Triple("Mordida abierta",R.drawable.edu_oclusion_mordida_abierta,"Imagen.")
  "Mordida invertida/underbite"->Triple("Relación anterior invertida",R.drawable.edu_oclusion_cruzada_anterior,"Imagen.")
  else->null}
 else->null
}
