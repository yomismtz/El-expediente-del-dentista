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
fun LocalClinicalImageGalleryV45(lang:String,profile:ScreenProfileV17){
    var expanded by remember{mutableStateOf<LocalClinicalGuideV45?>(null)}
    val groups=localClinicalGuidesV45.groupBy{if(lang=="en")it.groupEn else it.groupEs}
    ResponsiveSectionV17(
        tr(lang,"Guías visuales locales","Local visual guides"),
        tr(lang,"Las 23 imágenes cargadas están empaquetadas dentro de la aplicación y funcionan sin internet. Toca una imagen para ampliarla.","All 23 uploaded images are packaged inside the app and work offline. Tap an image to enlarge it.")
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
