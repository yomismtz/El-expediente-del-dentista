package com.yomismtz.expedientedeldentista.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

@Composable
fun EmergencyDentalSheetV43(lang:String,onBack:()->Unit){
    var reason by rememberRecordState("emergency.reason","")
    var onset by rememberRecordState("emergency.onset","")
    var pain by rememberRecordState("emergency.pain","")
    var swelling by rememberRecordState("emergency.swelling","No valorado")
    var bleeding by rememberRecordState("emergency.bleeding","No valorado")
    var trauma by rememberRecordState("emergency.trauma","No valorado")
    var fever by rememberRecordState("emergency.fever","No valorado")
    var airway by rememberRecordState("emergency.airway","Sin datos de compromiso")
    var toothSite by rememberRecordState("emergency.site","")
    var findings by rememberRecordState("emergency.findings","")
    var action by rememberRecordState("emergency.action","")
    var referral by rememberRecordState("emergency.referral","")
    ResponsiveScreenV17("🚨 "+tr(lang,"Ficha de Urgencias Odontológicas","Dental Emergency Sheet"),
        tr(lang,"Registro estructurado para documentar el motivo urgente, signos de alarma, exploración, conducta y seguimiento. No sustituye el triaje ni los protocolos institucionales.","Structured record for the urgent complaint, red flags, examination, management and follow-up. It does not replace triage or institutional protocols."),onBack){profile->
        ResponsiveSectionV17(tr(lang,"1 · Motivo y evolución","1 · Complaint and course"),tr(lang,"Documenta qué ocurrió, desde cuándo y dónde. Esto ayuda a ordenar la valoración, no establece el diagnóstico.","Record what happened, since when and where. This organizes assessment; it does not establish a diagnosis.")){
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                if(i==0) OutlinedTextField(reason,{reason=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Motivo urgente","Urgent complaint"))})
                else OutlinedTextField(onset,{onset=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Inicio / evolución","Onset / course"))})
            }
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                if(i==0) OutlinedTextField(pain,{pain=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Dolor 0–10 y características","Pain 0–10 and features"))})
                else OutlinedTextField(toothSite,{toothSite=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"OD / sitio","Tooth / site"))})
            }
        }
        ResponsiveSectionV17(tr(lang,"2 · Signos de alarma","2 · Red flags"),tr(lang,"Registra si fueron valorados. Disnea, disfagia progresiva, deterioro sistémico, hemorragia no controlada o infección de rápida progresión requieren escalamiento profesional urgente según el contexto.","Record whether these were assessed. Dyspnea, progressive dysphagia, systemic deterioration, uncontrolled bleeding or rapidly progressive infection require urgent professional escalation as appropriate.")){
            val labels=listOf("Tumefacción" to swelling,"Sangrado" to bleeding,"Traumatismo" to trauma,"Fiebre / compromiso sistémico" to fever)
            AdaptiveGridV17(labels.size,if(profile.largeSystemText)1 else if(profile.width==ScreenWidthV17.COMPACT)2 else 4){i->
                val (label,value)=labels[i]
                FilterChip(value=="Presente",{
                    val next=when(value){"No valorado"->"Presente";"Presente"->"Ausente";else->"No valorado"}
                    when(i){0->swelling=next;1->bleeding=next;2->trauma=next;else->fever=next}
                },{Text("$label: $value")},Modifier.fillMaxWidth())
            }
            OutlinedTextField(airway,{airway=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Vía aérea / deglución / extensión","Airway / swallowing / spread"))})
        }
        ResponsiveSectionV17(tr(lang,"3 · Exploración y conducta","3 · Examination and management"),tr(lang,"Describe únicamente hallazgos obtenidos y la conducta realmente indicada o realizada.","Record only obtained findings and management actually indicated or performed.")){
            OutlinedTextField(findings,{findings=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Hallazgos clínicos y auxiliares","Clinical and diagnostic findings"))})
            OutlinedTextField(action,{action=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Conducta / tratamiento inmediato","Immediate management / treatment"))})
            OutlinedTextField(referral,{referral=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Seguimiento, remisión y signos de alarma explicados","Follow-up, referral and explained warning signs"))})
        }
        NoticeCard(tr(lang,"La ficha organiza documentación. No debe usarse un hallazgo aislado para descartar una urgencia médica, infección profunda, trauma complejo o hemorragia significativa.","This sheet organizes documentation. An isolated finding must not be used to rule out a medical emergency, deep infection, complex trauma or significant hemorrhage."))
    }
}

@Composable
fun ClinicalPhotographySheetV43(lang:String,onBack:()->Unit){
    val context=LocalContext.current
    var view by rememberRecordState("photo.view","Frontal extraoral")
    var purpose by rememberRecordState("photo.purpose","Documentación inicial")
    var notes by rememberRecordState("photo.notes","")
    val photoUris=rememberRecordStateMap<String,String>("photo.uris")
    val launcher=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){uri:Uri?->
        if(uri!=null){
            runCatching{context.contentResolver.takePersistableUriPermission(uri,android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)}
            photoUris[view]=uri.toString()
        }
    }
    val views=listOf("Frontal extraoral","Perfil derecho","Perfil izquierdo","Sonrisa","Frontal intraoral","Lateral derecha","Lateral izquierda","Oclusal superior","Oclusal inferior","Detalle de lesión / procedimiento")
    ResponsiveScreenV17("📷 "+tr(lang,"Ficha de Fotografía Clínica","Clinical Photography Sheet"),
        tr(lang,"Organiza fotografías clínicas locales por vista y finalidad. La imagen permanece como URI local del dispositivo; la app no la envía a internet.","Organizes local clinical photographs by view and purpose. The image remains a local device URI; the app does not send it to the internet."),onBack){profile->
        ResponsiveSectionV17(tr(lang,"1 · Vista fotográfica","1 · Photographic view"),tr(lang,"Selecciona la vista que estás documentando. Estandarizar encuadre, posición y escala facilita comparaciones longitudinales.","Select the view being documented. Standardized framing, position and scale improve longitudinal comparison.")){
            AdaptiveGridV17(views.size,when{profile.largeSystemText->1;profile.width==ScreenWidthV17.COMPACT->2;profile.width==ScreenWidthV17.MEDIUM->3;else->5}){i->
                FilterChip(view==views[i],{view=views[i]},{Text(views[i])},Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"2 · Finalidad y archivo local","2 · Purpose and local file"),tr(lang,"Registra por qué se tomó la fotografía y vincula únicamente una imagen clínica autorizada del expediente activo.","Record why the photograph was taken and link only an authorized clinical image for the active record.")){
            val purposes=listOf("Documentación inicial","Diagnóstico / seguimiento","Antes del tratamiento","Durante el tratamiento","Después del tratamiento","Comunicación / interconsulta")
            AdaptiveGridV17(purposes.size,if(profile.largeSystemText)1 else if(profile.width==ScreenWidthV17.COMPACT)2 else 3){i->FilterChip(purpose==purposes[i],{purpose=purposes[i]},{Text(purposes[i])},Modifier.fillMaxWidth())}
            Button(onClick={launcher.launch(arrayOf("image/*"))},modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Seleccionar fotografía local","Select local photograph"),fontWeight=FontWeight.Bold)}
            val uriText=photoUris[view].orEmpty()
            if(uriText.isNotBlank()){
                Text(tr(lang,"Fotografía local vinculada a esta vista del expediente.","Local photograph linked to this record view."),fontWeight=FontWeight.Bold)
                Text(tr(lang,"Archivo local conservado mediante permiso persistente. Puede reemplazarse seleccionando otra fotografía.","Local file retained through persistent permission. It can be replaced by selecting another photograph."),style=androidx.compose.material3.MaterialTheme.typography.bodySmall)
            }
            OutlinedTextField(notes,{notes=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Hallazgos, calidad, orientación o notas","Findings, quality, orientation or notes"))})
        }
        NoticeCard(tr(lang,"La fotografía complementa la exploración; no reemplaza examen clínico, radiografías u otros estudios cuando estén indicados. Verifica consentimiento, privacidad y política institucional antes de fotografiar.","Photography complements examination; it does not replace clinical examination, radiographs or other studies when indicated. Verify consent, privacy and institutional policy before photographing."))
    }
}
