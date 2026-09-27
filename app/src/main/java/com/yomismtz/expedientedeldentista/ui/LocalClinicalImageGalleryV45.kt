package com.yomismtz.expedientedeldentista.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R

private data class LocalClinicalGuideV45(
    val groupEs:String,val groupEn:String,val titleEs:String,val titleEn:String,
    @DrawableRes val drawable:Int,val noteEs:String,val noteEn:String
)

private val localClinicalGuidesV45=listOf(
    LocalClinicalGuideV45("Cariología","Cariology","Lesión no cavitada","Non-cavitated lesion",R.drawable.edu_lesion_no_cavitada,"Referencia educativa para distinguir una lesión sin cavitación visible; valorar superficie, actividad y contexto clínico.","Educational reference for a lesion without visible cavitation; assess surface, activity and clinical context."),
    LocalClinicalGuideV45("Cariología","Cariology","Lesión cavitada","Cavitated lesion",R.drawable.edu_lesion_cavitada,"Referencia educativa de pérdida de integridad superficial/cavitación; correlacionar con exploración.","Educational cavitation reference; correlate with examination."),
    LocalClinicalGuideV45("Cariología","Cariology","Caries activa","Active caries",R.drawable.edu_caries_activa,"Referencia educativa de características compatibles con actividad; la imagen aislada no determina actividad.","Educational reference for features compatible with activity; an image alone does not determine activity."),
    LocalClinicalGuideV45("Cariología","Cariology","Caries inactiva","Inactive caries",R.drawable.edu_caries_inactiva,"Referencia educativa de características compatibles con inactividad; integrar textura, brillo, localización y control de biopelícula.","Educational reference for features compatible with inactivity; integrate texture, shine, site and biofilm control."),
    LocalClinicalGuideV45("Cariología","Cariology","Caries de corona","Coronal caries",R.drawable.edu_caries_de_corona,"Referencia educativa de lesión de caries coronaria.","Educational coronal-caries reference."),
    LocalClinicalGuideV45("Cariología","Cariology","Caries radicular","Root caries",R.drawable.edu_caries_radicular,"Referencia educativa de lesión de caries en superficie radicular expuesta.","Educational root-caries reference."),
    LocalClinicalGuideV45("Cariología","Cariology","ICDAS 0","ICDAS 0",R.drawable.edu_icdas_0,"Referencia visual educativa para código 0; conservar los criterios del módulo ICDAS existente.","Educational visual reference for code 0; retain the criteria in the existing ICDAS module."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Apertura y cierre","Opening and closing",R.drawable.edu_atm_apertura_y_cierre,"Secuencia educativa del movimiento mandibular.","Educational mandibular movement sequence."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Trayectoria normal","Normal trajectory",R.drawable.edu_atm_trayectoria_normal,"Referencia de trayectoria de apertura.","Opening-trajectory reference."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Desviación","Deviation",R.drawable.edu_atm_desviacion,"La desviación retorna hacia la línea media.","Deviation returns toward the midline."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Deflexión","Deflection",R.drawable.edu_atm_deflexion,"La deflexión permanece hacia un lado al final.","Deflection remains to one side at the end."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Desviación · explicación","Deviation · explanation",R.drawable.edu_atm_desviacion_explicacion,"Apoyo visual para diferenciar trayectorias.","Visual aid for differentiating trajectories."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Lateralidades","Excursions",R.drawable.edu_atm_lateralidad_derecha_izquierda,"Lateralidad derecha e izquierda.","Right and left excursions."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Protrusión","Protrusion",R.drawable.edu_atm_protrusion,"Movimiento anterior mandibular.","Forward mandibular movement."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Palpación de ATM","TMJ palpation",R.drawable.edu_atm_palpacion,"Referencia educativa de palpación articular.","Educational joint-palpation reference."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Palpación muscular","Muscle palpation",R.drawable.edu_atm_palpacion_muscular,"Referencia educativa de palpación muscular.","Educational muscle-palpation reference."),
    LocalClinicalGuideV45("ATM y movimientos","TMJ and movements","Palpación de ATM · vista complementaria","TMJ palpation · additional view",R.drawable.edu_palpacion_atm,"Segunda referencia de posicionamiento para palpación.","Additional palpation-positioning reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Mucosa normal","Normal mucosa",R.drawable.edu_mucosa_normal,"Referencia educativa de mucosa sin lesión elemental destacada.","Educational normal-mucosa reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Mácula","Macule",R.drawable.edu_lesion_macula,"Referencia visual de lesión elemental plana.","Visual reference for a flat elementary lesion."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Pápula","Papule",R.drawable.edu_lesion_papula,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Placa","Plaque",R.drawable.edu_lesion_placa,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Nódulo","Nodule",R.drawable.edu_lesion_nodulo,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Vesícula","Vesicle",R.drawable.edu_lesion_vesicula,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Ampolla","Bulla",R.drawable.edu_lesion_ampolla,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Pústula","Pustule",R.drawable.edu_lesion_pustula,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Erosión","Erosion",R.drawable.edu_lesion_erosion,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Úlcera","Ulcer",R.drawable.edu_lesion_ulcera,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Fisura","Fissure",R.drawable.edu_lesion_fisura,"Referencia visual educativa.","Educational visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Fisura lingual","Tongue fissure",R.drawable.edu_lesion_fisura_lengua,"Referencia visual complementaria.","Additional visual reference."),
    LocalClinicalGuideV45("Mucosa y lesiones elementales","Mucosa and elementary lesions","Fístula","Fistula",R.drawable.edu_lesion_fistula,"Referencia visual educativa; correlacionar origen clínicamente.","Educational visual reference; clinically correlate the source."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle · relación molar","Angle · molar relation",R.drawable.edu_oclusion_angle,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle Clase I · referencia 1","Angle Class I · reference 1",R.drawable.edu_oclusion_angle_clase_1,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle Clase I · referencia 2","Angle Class I · reference 2",R.drawable.edu_oclusion_angle_clase_1_2,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle Clase II · referencia 1","Angle Class II · reference 1",R.drawable.edu_oclusion_angle_clase_2,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle Clase II · referencia 2","Angle Class II · reference 2",R.drawable.edu_oclusion_angle_clase_2_2,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle Clase II división 1","Angle Class II division 1",R.drawable.edu_oclusion_angle_clase_2_div1,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle Clase II división 2","Angle Class II division 2",R.drawable.edu_oclusion_angle_clase_2_div2,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle Clase III · referencia 1","Angle Class III · reference 1",R.drawable.edu_oclusion_angle_clase_3,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Angle Clase III · referencia 2","Angle Class III · reference 2",R.drawable.edu_oclusion_angle_clase_3_2,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Mordida cruzada anterior","Anterior crossbite",R.drawable.edu_oclusion_cruzada_anterior,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Cruzada posterior bilateral · 1","Bilateral posterior crossbite · 1",R.drawable.edu_oclusion_cruzada_posterior_bilateral,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Cruzada posterior bilateral · 2","Bilateral posterior crossbite · 2",R.drawable.edu_oclusion_cruzada_posterior_bilateral_2,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Cruzada posterior unilateral · 1","Unilateral posterior crossbite · 1",R.drawable.edu_oclusion_cruzada_posterior_unilateral,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Cruzada posterior unilateral · 2","Unilateral posterior crossbite · 2",R.drawable.edu_oclusion_cruzada_posterior_unilateral_2,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Líneas medias","Midlines",R.drawable.edu_oclusion_linea_media,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Mordida abierta anterior","Anterior open bite",R.drawable.edu_oclusion_mordida_abierta,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Mordida abierta posterior","Posterior open bite",R.drawable.edu_oclusion_mordida_abierta_posterior,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Mordida profunda · 1","Deep bite · 1",R.drawable.edu_oclusion_mordida_profunda,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Mordida profunda · 2","Deep bite · 2",R.drawable.edu_oclusion_mordida_profunda_1,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Overjet y overbite · referencia","Overjet and overbite · reference",R.drawable.edu_oclusion_oveerjetoverbite,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Overbite","Overbite",R.drawable.edu_oclusion_overbite,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Oclusión","Occlusion","Overjet","Overjet",R.drawable.edu_oclusion_overjet,"Referencia visual educativa; correlacionar con la exploración.","Educational visual reference; correlate with examination."),
    LocalClinicalGuideV45("Fotografía extraoral","Extraoral photography","Frontal extraoral","Extraoral frontal",R.drawable.clin_extraoral_frontal,"Guía de encuadre frontal.","Frontal framing guide."),
    LocalClinicalGuideV45("Fotografía extraoral","Extraoral photography","Perfil derecho","Right profile",R.drawable.clin_extraoral_perfil_derecho,"Guía de toma de perfil derecho.","Right-profile capture guide."),
    LocalClinicalGuideV45("Fotografía extraoral","Extraoral photography","Perfil izquierdo","Left profile",R.drawable.clin_extraoral_perfil_izquierdo,"Guía de toma de perfil izquierdo.","Left-profile capture guide."),
    LocalClinicalGuideV45("Fotografía extraoral","Extraoral photography","Sonrisa","Smile",R.drawable.clin_extraoral_sonrisa,"Guía de fotografía de sonrisa.","Smile photography guide."),
    LocalClinicalGuideV45("Fotografía intraoral","Intraoral photography","Frontal intraoral","Intraoral frontal",R.drawable.clin_intraoral_frontal,"Guía de vista frontal intraoral.","Intraoral frontal-view guide."),
    LocalClinicalGuideV45("Fotografía intraoral","Intraoral photography","Lateral derecha","Right lateral",R.drawable.clin_intraoral_lateral_derecha,"Guía de vista lateral derecha.","Right lateral-view guide."),
    LocalClinicalGuideV45("Fotografía intraoral","Intraoral photography","Lateral izquierda","Left lateral",R.drawable.clin_intraoral_lateral_izquierda,"Guía de vista lateral izquierda.","Left lateral-view guide."),
    LocalClinicalGuideV45("Fotografía intraoral","Intraoral photography","Oclusal superior","Upper occlusal",R.drawable.clin_intraoral_oclusal_superior,"Guía de vista oclusal superior.","Upper occlusal-view guide."),
    LocalClinicalGuideV45("Fotografía intraoral","Intraoral photography","Oclusal inferior","Lower occlusal",R.drawable.clin_intraoral_oclusal_inferior,"Guía de vista oclusal inferior.","Lower occlusal-view guide."),
    LocalClinicalGuideV45("Documentación clínica","Clinical documentation","Detalle de lesión","Lesion detail",R.drawable.clin_lesion_detalle,"Referencia para documentar un detalle de lesión; describir y correlacionar clínicamente.","Reference for documenting lesion detail; describe and correlate clinically."),
    LocalClinicalGuideV45("Documentación clínica","Clinical documentation","Detalle de procedimiento","Procedure detail",R.drawable.clin_procedimiento_detalle,"Referencia para documentar detalles durante un procedimiento.","Reference for documenting procedure details."),
    LocalClinicalGuideV45("Análisis facial","Facial analysis","Simetría facial","Facial symmetry",R.drawable.edu_extraoral_simetria_facial,"Referencia educativa de simetría facial.","Educational facial-symmetry reference."),
    LocalClinicalGuideV45("Análisis facial","Facial analysis","Asimetría facial leve","Mild facial asymmetry",R.drawable.edu_extraoral_asimetria_facial_leve,"Ejemplo educativo de asimetría leve.","Educational example of mild asymmetry."),
    LocalClinicalGuideV45("Análisis facial","Facial analysis","Asimetría facial media","Moderate facial asymmetry",R.drawable.edu_extraoral_asimetria_facial_media,"Ejemplo educativo de asimetría intermedia.","Educational example of moderate asymmetry."),
    LocalClinicalGuideV45("Análisis facial","Facial analysis","Asimetría facial marcada","Marked facial asymmetry",R.drawable.edu_extraoral_asimetria_facial_marcada,"Ejemplo educativo de asimetría marcada.","Educational example of marked asymmetry."),
    LocalClinicalGuideV45("Análisis facial","Facial analysis","Formas del rostro","Face shapes",R.drawable.edu_extraoral_formas_del_rostro,"Referencia educativa para describir forma facial.","Educational reference for describing face shape."),
    LocalClinicalGuideV45("Análisis facial","Facial analysis","Tercios faciales","Facial thirds",R.drawable.edu_extraoral_tercios_faciales,"Guía educativa de tercios faciales.","Educational facial-thirds guide."),
    LocalClinicalGuideV45("Análisis facial","Facial analysis","Quintos faciales","Facial fifths",R.drawable.edu_extraoral_quintos_faciales,"Guía educativa de quintos faciales.","Educational facial-fifths guide."),
    LocalClinicalGuideV45("Perfil y labios","Profile and lips","Perfil recto","Straight profile",R.drawable.edu_perfil_recto,"Referencia educativa de perfil recto.","Educational straight-profile reference."),
    LocalClinicalGuideV45("Perfil y labios","Profile and lips","Perfil convexo","Convex profile",R.drawable.edu_perfil_convexo,"Referencia educativa de perfil convexo.","Educational convex-profile reference."),
    LocalClinicalGuideV45("Perfil y labios","Profile and lips","Perfil cóncavo","Concave profile",R.drawable.edu_perfil_concavo,"Referencia educativa de perfil cóncavo.","Educational concave-profile reference."),
    LocalClinicalGuideV45("Perfil y labios","Profile and lips","Labios competentes","Competent lips",R.drawable.edu_labios_competentes,"Referencia educativa de competencia labial.","Educational lip-competence reference."),
    LocalClinicalGuideV45("Perfil y labios","Profile and lips","Labios incompetentes","Incompetent lips",R.drawable.edu_labios_incompetentes,"Referencia educativa de incompetencia labial.","Educational lip-incompetence reference.")
)

@Composable
fun LocalClinicalImageSectionV46(lang:String,profile:ScreenProfileV17?=null,groupEs:String,groupEn:String){
    var expanded by remember{mutableStateOf<LocalClinicalGuideV45?>(null)}
    val items=localClinicalGuidesV45.filter{it.groupEs==groupEs}
    if(items.isEmpty())return
    ResponsiveSectionV17(if(lang=="en")groupEn else groupEs,tr(lang,"Referencias visuales locales incluidas en el APK. Toca para ampliar.","Local visual references included in the APK. Tap to enlarge.")){
        val columns=when{profile==null->2;profile.largeSystemText->1;profile.width==ScreenWidthV17.COMPACT->2;profile.width==ScreenWidthV17.MEDIUM->3;else->4}
        AdaptiveGridV17(items.size,columns){i->
            val item=items[i]
            Card(Modifier.fillMaxWidth().clickable{expanded=item},shape=RoundedCornerShape(14.dp)){
                Column(Modifier.padding(8.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){
                    Image(painterResource(item.drawable),if(lang=="en")item.titleEn else item.titleEs,Modifier.fillMaxWidth().heightIn(min=110.dp,max=190.dp),contentScale=ContentScale.Fit)
                    Text(if(lang=="en")item.titleEn else item.titleEs,fontWeight=FontWeight.Bold)
                    Text(if(lang=="en")item.noteEn else item.noteEs,style=MaterialTheme.typography.bodySmall)
                }
            }
        }
        Text(tr(lang,"Apoyo educativo: interpretar junto con interrogatorio y exploración; la imagen aislada no establece diagnóstico.","Educational aid: interpret with history and examination; an isolated image does not establish diagnosis."),style=MaterialTheme.typography.bodySmall)
    }
    expanded?.let{item->AlertDialog(onDismissRequest={expanded=null},confirmButton={TextButton(onClick={expanded=null}){Text(tr(lang,"Cerrar","Close"))}},title={Text(if(lang=="en")item.titleEn else item.titleEs)},text={Box(Modifier.fillMaxWidth().sizeIn(minHeight=220.dp,maxHeight=620.dp),contentAlignment=Alignment.Center){Image(painterResource(item.drawable),if(lang=="en")item.titleEn else item.titleEs,Modifier.fillMaxSize(),contentScale=ContentScale.Fit)}})}
}

@Composable
fun LocalClinicalImageGalleryV45(lang:String,profile:ScreenProfileV17){
    var expanded by remember{mutableStateOf<LocalClinicalGuideV45?>(null)}
    val groups=localClinicalGuidesV45.groupBy{if(lang=="en")it.groupEn else it.groupEs}
    ResponsiveSectionV17(
        tr(lang,"Guías visuales locales","Local visual guides"),
        tr(lang,"Las imágenes clínicas y educativas cargadas están empaquetadas dentro de la aplicación y funcionan sin internet. Toca una imagen para ampliarla.","The uploaded clinical and educational images are packaged inside the app and work offline. Tap an image to enlarge it.")
    ){
        groups.forEach{(group,items)->
            Text(group,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
            AdaptiveGridV17(items.size,when{profile.largeSystemText->1;profile.width==ScreenWidthV17.COMPACT->2;profile.width==ScreenWidthV17.MEDIUM->3;else->4}){i->
                val item=items[i]
                Card(
                    modifier=Modifier.fillMaxWidth().clickable{expanded=item},
                    shape=RoundedCornerShape(14.dp),
                    colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)
                ){
                    Column(Modifier.fillMaxWidth().padding(8.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                        Image(painterResource(item.drawable),if(lang=="en")item.titleEn else item.titleEs,Modifier.fillMaxWidth().heightIn(min=110.dp,max=190.dp),contentScale=ContentScale.Fit)
                        Text(if(lang=="en")item.titleEn else item.titleEs,fontWeight=FontWeight.Bold)
                        Text(if(lang=="en")item.noteEn else item.noteEs,style=MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Text(tr(lang,"Estas imágenes son referencias educativas y de estandarización fotográfica. Ninguna imagen aislada establece un diagnóstico.","These images are educational and photographic-standardization references. No isolated image establishes a diagnosis."),style=MaterialTheme.typography.bodySmall)
    }
    expanded?.let{item->
        AlertDialog(
            onDismissRequest={expanded=null},
            confirmButton={TextButton(onClick={expanded=null}){Text(tr(lang,"Cerrar","Close"))}},
            title={Text(if(lang=="en")item.titleEn else item.titleEs)},
            text={Box(Modifier.fillMaxWidth().sizeIn(minHeight=220.dp,maxHeight=620.dp),contentAlignment=Alignment.Center){
                Image(painterResource(item.drawable),if(lang=="en")item.titleEn else item.titleEs,Modifier.fillMaxSize(),contentScale=ContentScale.Fit)
            }}
        )
    }
}
