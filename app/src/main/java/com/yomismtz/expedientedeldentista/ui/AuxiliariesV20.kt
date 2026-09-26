package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private enum class AuxV20Page { HOME, LAB, ORTHO, IMAGING, SALIVA }

@Composable
fun AuxiliariesV20Screen(lang:String,onBack:()->Unit){
    var pageName by rememberSaveable { mutableStateOf(AuxV20Page.HOME.name) }
    val page=runCatching { AuxV20Page.valueOf(pageName) }.getOrDefault(AuxV20Page.HOME)
    fun open(next:AuxV20Page){ pageName=next.name }
    when(page){
        AuxV20Page.LAB->LaboratoryAuxiliariesV20Screen(lang){open(AuxV20Page.HOME)}
        AuxV20Page.ORTHO->OrthodonticAuxiliariesV20Screen(lang){open(AuxV20Page.HOME)}
        AuxV20Page.IMAGING->ImagingAuxiliariesV20Screen(lang){open(AuxV20Page.HOME)}
        AuxV20Page.SALIVA->ResponsiveScreenV17("Flujo salival · sialometría","Auxiliar diagnóstico para medir flujo salival total.",{open(AuxV20Page.HOME)}){_ ->
            var method by rememberRecordState("saliva.method","No estimulada")
            var volume by rememberRecordState("saliva.volumeMl","")
            var minutes by rememberRecordState("saliva.minutes","")
            val v=volume.replace(',','.').toDoubleOrNull()
            val t=minutes.replace(',','.').toDoubleOrNull()
            val flow=if(v!=null && t!=null && v>=0.0 && t>0.0) v/t else null
            SectionCard("Registro por expediente"){
                Text("Selecciona el método y registra volumen y tiempo medidos. El cálculo se guarda a partir de estos datos; no genera un diagnóstico automático.")
                ChipChoices(listOf("No estimulada","Estimulada").map{it to (method==it)},{method=listOf("No estimulada","Estimulada")[it]},columns=2)
                OutlinedTextField(volume,{volume=it.filter{c->c.isDigit()||c=='.'||c==','}},label={Text("Volumen recolectado (mL)")},modifier=Modifier.fillMaxWidth())
                OutlinedTextField(minutes,{minutes=it.filter{c->c.isDigit()||c=='.'||c==','}},label={Text("Tiempo de recolección (min)")},modifier=Modifier.fillMaxWidth())
                Text(if(flow!=null)"Flujo calculado: %.2f mL/min".format(flow) else "Flujo calculado: completa volumen y tiempo válidos.",fontWeight=FontWeight.Bold)
            }
            SectionCard("Métodos de obtención"){
                Text("No estimulada: drenaje pasivo, escupido, succión o papel absorbente. Estimulada: parafina/base de goma sin sabor o estímulo gustativo, con recolección cronometrada.")
                Text("Flujo salival = volumen recolectado (mL) ÷ tiempo (min).",fontWeight=FontWeight.Bold)
            }
            SectionCard("Interpretación educativa"){
                Text("Saliva total no estimulada: el flujo habitual se sitúa aproximadamente en 0.3–0.4 mL/min; ≤0.1 mL/min es un punto de corte ampliamente utilizado para hiposalivación.")
                Text("Saliva total estimulada: el flujo habitual se sitúa aproximadamente en 1.5–2.0 mL/min; se han usado puntos de corte de 0.5–0.7 mL/min para flujo reducido.")
            }
            SectionCard("Tira de papel"){
                Text("La tira debe interpretarse según el método o dispositivo específico. La longitud humedecida en mm no se convierte directamente a mL sin una calibración validada.",fontWeight=FontWeight.Bold)
                Text("En el Oral Schirmer Test se han estudiado resultados en mm/5 min; una tira genérica no debe etiquetarse como normal, hiposalivación o hipersalivación usando una conversión inventada a mL.")
            }
            NoticeCard("Xerostomía es la sensación subjetiva de boca seca; hiposalivación es una reducción objetiva del flujo. No existe un único punto de corte universal de mL/min que diagnostique por sí solo hipersalivación/sialorrea. Interpretar según técnica, síntomas y contexto clínico.")
        }
        AuxV20Page.HOME->ResponsiveScreenV17(
            tr(lang,"Auxiliares de diagnóstico","Diagnostic aids"),
            tr(lang,"Pantalla inicial de auxiliares: Laboratorio, Ortodoncia, Imagenología y Sialometría. Los colores se derivan de la paleta activa y todo funciona localmente. Acceso directo desde el rubro 4.","Diagnostic aids home: Laboratory, Orthodontics, Imaging and Sialometry. Colors are derived from the active palette and everything works locally. Direct access from section 4."),
            onBack
        ){profile->
            val columns=when {
                profile.largeSystemText -> 1
                profile.width==ScreenWidthV17.COMPACT -> 2
                profile.width==ScreenWidthV17.MEDIUM -> 2
                else -> 4
            }
            val cardColors=listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.tertiaryContainer,
                MaterialTheme.colorScheme.surfaceVariant
            )
            AdaptiveGridV17(4,columns){i->
                val lab=i==0
                val imaging=i==2
                val saliva=i==3
                Card(
                    onClick={open(if(lab)AuxV20Page.LAB else if(imaging)AuxV20Page.IMAGING else if(saliva)AuxV20Page.SALIVA else AuxV20Page.ORTHO)},
                    modifier=Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(
                        containerColor=cardColors[i],
                        contentColor=contentColorFor(cardColors[i])
                    ),
                    border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.35f)),
                    shape=RoundedCornerShape(18.dp)
                ){
                    androidx.compose.foundation.layout.Column(
                        Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement=androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ){
                        Text(if(lab)"🧪 ${tr(lang,"Laboratorio e histopatología","Laboratory & histopathology")}" else if(imaging)"🩻 ${tr(lang,"Imagenología dental","Dental imaging")}" else if(saliva)"💧 ${tr(lang,"Flujo salival / sialometría","Salivary flow / sialometry")}" else "📐 ${tr(lang,"Ortodoncia y análisis","Orthodontics & analysis")}",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black)
                        Text(if(lab)tr(lang,"Biometría hemática, química sanguínea, coagulación y lectura educativa de biopsia.","CBC, blood chemistry, coagulation and educational biopsy reading.") else if(imaging)tr(lang,"Periapical, bitewing, oclusal, panorámica, cefalométrica, CBCT y registro sistemático de hallazgos.","Periapical, bitewing, occlusal, panoramic, cephalometric, CBCT and systematic findings.") else if(saliva)tr(lang,"Flujo no estimulado y estimulado, métodos de obtención, tira de papel y cálculo en mL/min.","Unstimulated and stimulated flow, collection methods, paper strip and mL/min calculation.") else tr(lang,"Fotos frontal/lateral, Powell, Steiner, Moyers, Tanaka–Johnston, panorámica y Nolla.","Frontal/lateral photos, Powell, Steiner, Moyers, Tanaka–Johnston, panoramic and Nolla."),style=MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            NoticeCard(tr(lang,"La app compara y enseña; no sustituye el reporte del laboratorio, del patólogo o del radiólogo, ni la supervisión docente. Cuando el laboratorio reporte su propio intervalo de referencia, ese intervalo tiene prioridad sobre el ejemplo educativo de la app.","The app compares and teaches; it does not replace laboratory, pathology or radiology reports or faculty supervision. When the reporting laboratory provides its own reference interval, that interval takes priority over the app's teaching example."))
        }
    }
}
