package com.yomismtz.expedientedeldentista.ui

import android.net.Uri
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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

private fun photoBitmapV43(context:android.content.Context,uriText:String):Bitmap? = runCatching {
    context.contentResolver.openInputStream(Uri.parse(uriText))?.use { BitmapFactory.decodeStream(it) }
}.getOrNull()

private fun adjustedPhotoV43(source:Bitmap,brightness:Float,contrast:Float,sharpness:Float):Bitmap {
    val output=Bitmap.createBitmap(source.width,source.height,Bitmap.Config.ARGB_8888)
    val canvas=Canvas(output)
    val scale=contrast.coerceIn(.5f,1.8f)
    val translate=(brightness.coerceIn(-1f,1f)*110f)+(128f*(1f-scale))
    val matrix=ColorMatrix(floatArrayOf(scale,0f,0f,0f,translate,0f,scale,0f,0f,translate,0f,0f,scale,0f,translate,0f,0f,0f,1f,0f))
    val paint=Paint(Paint.ANTI_ALIAS_FLAG).apply{colorFilter=ColorMatrixColorFilter(matrix)}
    canvas.drawBitmap(source,0f,0f,paint)
    if(sharpness>0.02f){
        val overlay=Paint(Paint.ANTI_ALIAS_FLAG).apply{alpha=(sharpness.coerceIn(0f,1f)*55).toInt()}
        canvas.drawBitmap(source,0f,0f,overlay)
    }
    return output
}

private fun createPhotoPdfV43(context:android.content.Context,photos:Map<String,String>,brightness:Float,contrast:Float,sharpness:Float):Uri? = runCatching {
    val pdf=PdfDocument()
    photos.entries.filter{it.value.isNotBlank()}.chunked(4).forEachIndexed{pageIndex,chunk->
        val page=pdf.startPage(PdfDocument.PageInfo.Builder(595,842,pageIndex+1).create())
        val canvas=page.canvas
        val textPaint=Paint(Paint.ANTI_ALIAS_FLAG).apply{textSize=18f}
        canvas.drawText("Registro fotográfico clínico",32f,36f,textPaint)
        chunk.forEachIndexed{i,e->
            val bmp=photoBitmapV43(context,e.value)?.let{adjustedPhotoV43(it,brightness,contrast,sharpness)} ?: return@forEachIndexed
            val top=62+i*188
            val rect=android.graphics.Rect(32,top,563,top+150)
            canvas.drawBitmap(bmp,null,rect,Paint(Paint.ANTI_ALIAS_FLAG))
            canvas.drawText(e.key,32f,(top+170).toFloat(),Paint(Paint.ANTI_ALIAS_FLAG).apply{textSize=12f})
        }
        pdf.finishPage(page)
    }
    val file=File(context.cacheDir,"registro_fotografico_clinico.pdf")
    file.outputStream().use{pdf.writeTo(it)}
    pdf.close()
    androidx.core.content.FileProvider.getUriForFile(context,context.packageName+".fileprovider",file)
}.getOrNull()

@Composable
fun ClinicalPhotographySheetV43(lang:String,onBack:()->Unit){
    val context=LocalContext.current
    var category by rememberRecordState("photo.category","Extraoral")
    var view by rememberRecordState("photo.view","Frontal extraoral")
    var purpose by rememberRecordState("photo.purpose","Documentación inicial")
    var notes by rememberRecordState("photo.notes","")
    var brightness by rememberRecordState("photo.brightness",0f)
    var contrast by rememberRecordState("photo.contrast",1f)
    var sharpness by rememberRecordState("photo.sharpness",0f)
    val photoUris=rememberRecordStateMap<String,String>("photo.uris")
    val launcher=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){uri:Uri?->
        if(uri!=null){
            runCatching{context.contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION)}
            photoUris[view]=uri.toString()
        }
    }
    val cropLauncher=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        val uri=result.data?.data
        if(uri!=null){runCatching{context.contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION)};photoUris[view]=uri.toString()}
    }
    val extraoral=listOf("Frontal extraoral","Perfil derecho","Perfil izquierdo","Sonrisa")
    val intraoral=listOf("Frontal intraoral","Lateral derecha","Lateral izquierda","Oclusal superior","Oclusal inferior","Detalle de lesión / procedimiento")
    val views=if(category=="Intraoral") intraoral else extraoral
    val safeView = view.takeIf { it in views } ?: views.first()
    val uriText=photoUris[safeView].orEmpty()
    val original=remember(uriText){if(uriText.isBlank())null else photoBitmapV43(context,uriText)}
    val preview=remember(original,brightness,contrast,sharpness){original?.let{adjustedPhotoV43(it,brightness,contrast,sharpness)}}

    ResponsiveScreenV17("📷 "+tr(lang,"Ficha de Fotografía Clínica","Clinical Photography Sheet"),
        tr(lang,"Registro local y offline de fotografías clínicas. Separa vistas extraorales e intraorales y permite preparar una hoja PDF para el expediente físico.","Local offline clinical photo record. Separates extraoral and intraoral views and can prepare a PDF sheet for the physical record."),onBack){profile->
        ResponsiveSectionV17(tr(lang,"1 · Tipo y vista","1 · Type and view")){
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
                listOf("Extraoral","Intraoral").forEach{o->FilterChip(category==o,{category=o;view=if(o=="Intraoral")intraoral.first() else extraoral.first()},{Text(o)},Modifier.weight(1f))}
            }
            AdaptiveGridV17(views.size,when{profile.largeSystemText->1;profile.width==ScreenWidthV17.COMPACT->2;profile.width==ScreenWidthV17.MEDIUM->3;else->5}){i->
                FilterChip(safeView==views[i],{view=views[i]},{Text(views[i])},Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"2 · Fotografía del paciente","2 · Patient photograph"),tr(lang,"Las fotografías permanecen vinculadas localmente al expediente activo.","Photos remain locally linked to the active record.")){
            Button(onClick={launcher.launch(arrayOf("image/*"))},modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Seleccionar / reemplazar fotografía","Select / replace photograph"),fontWeight=FontWeight.Bold)}
            if(preview!=null){
                Image(preview.asImageBitmap(),safeView,Modifier.fillMaxWidth().height(260.dp),contentScale=ContentScale.Fit)
                Text(tr(lang,"Brillo","Brightness"));Slider(brightness,{brightness=it},valueRange=-1f..1f)
                Text(tr(lang,"Contraste","Contrast"));Slider(contrast,{contrast=it},valueRange=.5f..1.8f)
                Text(tr(lang,"Nitidez","Sharpness"));Slider(sharpness,{sharpness=it},valueRange=0f..1f)
                OutlinedButton(onClick={
                    val source=Uri.parse(uriText)
                    val intent=Intent("com.android.camera.action.CROP").apply{setDataAndType(source,"image/*");putExtra("crop",true);putExtra("return-data",false);addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)}
                    runCatching{cropLauncher.launch(intent)}.onFailure{Toast.makeText(context,tr(lang,"El dispositivo no ofrece un editor de recorte compatible.","No compatible crop editor is available on this device."),Toast.LENGTH_SHORT).show()}
                },modifier=Modifier.fillMaxWidth()){Text("✂️ "+tr(lang,"Recortar / acomodar","Crop / reposition"))}
            }
        }
        ResponsiveSectionV17(tr(lang,"3 · Finalidad y notas","3 · Purpose and notes")){
            val purposes=listOf("Documentación inicial","Diagnóstico / seguimiento","Antes del tratamiento","Durante el tratamiento","Después del tratamiento","Comunicación / interconsulta")
            AdaptiveGridV17(purposes.size,if(profile.largeSystemText)1 else if(profile.width==ScreenWidthV17.COMPACT)2 else 3){i->FilterChip(purpose==purposes[i],{purpose=purposes[i]},{Text(purposes[i])},Modifier.fillMaxWidth())}
            OutlinedTextField(notes,{notes=it},Modifier.fillMaxWidth(),label={Text(tr(lang,"Hallazgos, calidad, orientación o notas","Findings, quality, orientation or notes"))})
        }
        ResponsiveSectionV17(tr(lang,"4 · Expediente físico","4 · Physical record"),tr(lang,"Genera una composición PDF local con las fotografías vinculadas para guardarla o imprimirla.","Creates a local PDF composition with linked photos for saving or printing.")){
            Button(onClick={
                val uri=createPhotoPdfV43(context,photoUris.toMap(),brightness,contrast,sharpness)
                if(uri!=null){
                    val send=Intent(Intent.ACTION_SEND).apply{type="application/pdf";putExtra(Intent.EXTRA_STREAM,uri);addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)}
                    context.startActivity(Intent.createChooser(send,tr(lang,"Guardar, imprimir o compartir PDF","Save, print or share PDF")))
                }else Toast.makeText(context,tr(lang,"No fue posible generar el PDF.","PDF could not be generated."),Toast.LENGTH_SHORT).show()
            },enabled=photoUris.isNotEmpty(),modifier=Modifier.fillMaxWidth()){Text("📄 "+tr(lang,"Generar PDF para expediente físico","Generate PDF for physical record"),fontWeight=FontWeight.Bold)}
        }
        NoticeCard(tr(lang,"La fotografía complementa la exploración. Los ajustes de brillo, contraste y nitidez son de presentación y no deben utilizarse para ocultar, crear o alterar hallazgos clínicos.","Photography complements examination. Brightness, contrast and sharpness adjustments are for presentation and must not be used to hide, create or alter clinical findings."))
    }
}
