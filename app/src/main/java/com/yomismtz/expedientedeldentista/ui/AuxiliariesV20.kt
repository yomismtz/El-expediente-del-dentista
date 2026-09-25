package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private enum class AuxV20Page { HOME, LAB, ORTHO, IMAGING, SALIVA }

@Composable
fun AuxiliariesV20Screen(lang:String,onBack:()->Unit){
    var page by remember{mutableStateOf(AuxV20Page.HOME)}
    when(page){
        AuxV20Page.LAB->LaboratoryAuxiliariesV20Screen(lang){page=AuxV20Page.HOME}
        AuxV20Page.ORTHO->OrthodonticAuxiliariesV20Screen(lang){page=AuxV20Page.HOME}
        AuxV20Page.IMAGING->ImagingAuxiliariesV20Screen(lang){page=AuxV20Page.HOME}
        AuxV20Page.SALIVA->SalivaryFlowV20Screen(lang){page=AuxV20Page.HOME}
        AuxV20Page.HOME->ResponsiveScreenV17(
            tr(lang,"Auxiliares de diagnóstico","Diagnostic aids"),
            tr(lang,"Selecciona el bloque que necesitas. Todo funciona localmente y las imágenes cargadas se usan solo durante la práctica actual.","Choose the block you need. Everything works locally and imported images are used only during the current practice."),
            onBack
        ){profile->
            val columns=if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2
            AdaptiveGridV17(4,columns){i->
                val lab=i==0
                val imaging=i==2
                val saliva=i==3
                Card(
                    onClick={page=if(lab)AuxV20Page.LAB else if(imaging)AuxV20Page.IMAGING else if(saliva)AuxV20Page.SALIVA else AuxV20Page.ORTHO},
                    modifier=Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(containerColor=if(lab)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer),
                    border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.35f)),
                    shape=RoundedCornerShape(18.dp)
                ){
                    androidx.compose.foundation.layout.Column(Modifier.padding(16.dp)){
                        Text(if(lab)"🧪 ${tr(lang,"Laboratorio e histopatología","Laboratory & histopathology")}" else if(imaging)"🩻 ${tr(lang,"Imagenología dental","Dental imaging")}" else if(saliva)"💧 ${tr(lang,"Flujo salival / sialometría","Salivary flow / sialometry")}" else "📐 ${tr(lang,"Ortodoncia y análisis","Orthodontics & analysis")}",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black)
                        Text(if(lab)tr(lang,"Biometría hemática, química sanguínea, coagulación y lectura educativa de biopsia.","CBC, blood chemistry, coagulation and educational biopsy reading.") else if(imaging)tr(lang,"Periapical, bitewing, oclusal, panorámica, cefalométrica, CBCT y registro sistemático de hallazgos.","Periapical, bitewing, occlusal, panoramic, cephalometric, CBCT and systematic findings.") else if(saliva)tr(lang,"Flujo no estimulado y estimulado, métodos de obtención, tira de papel y cálculo en mL/min.","Unstimulated and stimulated flow, collection methods, paper strip and mL/min calculation.") else tr(lang,"Fotos frontal/lateral, Powell, Steiner, Moyers, Tanaka–Johnston, panorámica y Nolla.","Frontal/lateral photos, Powell, Steiner, Moyers, Tanaka–Johnston, panoramic and Nolla."))
                    }
                }
            }
            NoticeCard(tr(lang,"La app compara y enseña; no sustituye el reporte del laboratorio, del patólogo o del radiólogo, ni la supervisión docente. Cuando el laboratorio reporte su propio intervalo de referencia, ese intervalo tiene prioridad sobre el ejemplo educativo de la app.","The app compares and teaches; it does not replace laboratory, pathology or radiology reports or faculty supervision. When the reporting laboratory provides its own reference interval, that interval takes priority over the app's teaching example."))
        }
    }
}
