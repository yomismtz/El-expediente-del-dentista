package com.yomismtz.expedientedeldentista.ui

import android.net.Uri
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.yomismtz.expedientedeldentista.clinical.ClinicalRecordStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import kotlin.math.min
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
private fun GuidedChoiceV50(key:String,label:String,options:List<String>,columns:Int=2){
    var value by rememberRecordState(key,"No valorado")
    Column(Modifier.fillMaxWidth()){Text(label,fontWeight=FontWeight.SemiBold);AdaptiveGridV17(options.size,columns){i->val o=options[i];FilterChip(value==o,{value=o},{Text(o)},Modifier.fillMaxWidth())}}
}
@Composable
fun EmergencyDentalSheetV43(lang:String,onBack:()->Unit){
    ResponsiveScreenV17("🚨 "+tr(lang,"Ficha de Urgencias Odontológicas","Dental Emergency Sheet"),
        tr(lang,"Ejercicio educativo estructurado: el alumno selecciona hallazgos predefinidos y no redacta datos de un paciente real.","Structured educational exercise: the student selects predefined findings and does not enter real-patient data."),onBack){profile->
        val cols=if(profile.largeSystemText)1 else 2
        ResponsiveSectionV17(tr(lang,"1 · Motivo y evolución","1 · Complaint and course")){
            GuidedChoiceV50("emergency.reason",tr(lang,"Motivo urgente","Urgent complaint"),listOf("Dolor dental","Tumefacción","Traumatismo","Sangrado","Restauración / prótesis desplazada","Otro escenario docente"),cols)
            GuidedChoiceV50("emergency.onset",tr(lang,"Evolución","Course"),listOf("< 24 h","1–3 días","4–7 días","> 7 días","No valorado"),cols)
            GuidedChoiceV50("emergency.pain",tr(lang,"Intensidad del dolor","Pain intensity"),listOf("0","1–3","4–6","7–10","No valorado"),cols)
            GuidedChoiceV50("emergency.site",tr(lang,"Sitio","Site"),listOf("Anterior superior","Posterior superior","Anterior inferior","Posterior inferior","Tejidos blandos","No valorado"),cols)
        }
        ResponsiveSectionV17(tr(lang,"2 · Signos de alarma","2 · Red flags")){
            listOf("swelling" to "Tumefacción","bleeding" to "Sangrado","trauma" to "Traumatismo","fever" to "Fiebre / compromiso sistémico").forEach{(k,l)->GuidedChoiceV50("emergency."+k,l,listOf("Presente","Ausente","No valorado"),3)}
            GuidedChoiceV50("emergency.airway",tr(lang,"Vía aérea / deglución / extensión","Airway / swallowing / spread"),listOf("Sin datos de compromiso","Disfagia referida","Disnea referida","Extensión progresiva","No valorado"),cols)
        }
        ResponsiveSectionV17(tr(lang,"3 · Exploración y conducta","3 · Examination and management")){
            GuidedChoiceV50("emergency.findings",tr(lang,"Hallazgo principal","Main finding"),listOf("Sin hallazgo concluyente","Caries / destrucción coronaria","Inflamación localizada","Colección / supuración","Trauma dentoalveolar","Sangrado","No valorado"),cols)
            GuidedChoiceV50("emergency.action",tr(lang,"Conducta educativa seleccionada","Selected educational management"),listOf("Valoración y auxiliares","Control local","Tratamiento dental indicado","Interconsulta","Remisión urgente","Reevaluación"),cols)
            GuidedChoiceV50("emergency.referral",tr(lang,"Seguimiento","Follow-up"),listOf("Control programado","Reevaluación temprana","Remisión odontológica","Remisión médica","Urgencias hospitalarias","No valorado"),cols)
        }
        NoticeCard(tr(lang,"Ficha exclusivamente educativa. Las selecciones ayudan a practicar el orden del expediente y no sustituyen triaje, diagnóstico ni atención clínica.","Educational sheet only. Selections help practice record structure and do not replace triage, diagnosis or clinical care."))
    }
}
private fun photoBitmapV43(context:android.content.Context,uriText:String,maxSide:Int=1800):Bitmap? = runCatching {
    val uri=Uri.parse(uriText)
    val bounds=BitmapFactory.Options().apply{inJustDecodeBounds=true}
    context.contentResolver.openInputStream(uri)?.use{BitmapFactory.decodeStream(it,null,bounds)}
    var sample=1
    while(bounds.outWidth/sample>maxSide*2 || bounds.outHeight/sample>maxSide*2) sample*=2
    val options=BitmapFactory.Options().apply{inSampleSize=sample.coerceAtLeast(1)}
    context.contentResolver.openInputStream(uri)?.use{BitmapFactory.decodeStream(it,null,options)}
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

private fun framedPhotoV43(source:Bitmap,brightness:Float,contrast:Float,sharpness:Float,rotation:Int,zoom:Float,offsetX:Float,offsetY:Float,outWidth:Int=1200,outHeight:Int=800):Bitmap {
    val adjusted=adjustedPhotoV43(source,brightness,contrast,sharpness)
    val output=Bitmap.createBitmap(outWidth,outHeight,Bitmap.Config.ARGB_8888)
    val canvas=Canvas(output)
    canvas.drawColor(android.graphics.Color.BLACK)
    val matrix=Matrix()
    val normalized=((rotation%360)+360)%360
    val rotatedWidth=if(normalized==90||normalized==270) adjusted.height.toFloat() else adjusted.width.toFloat()
    val rotatedHeight=if(normalized==90||normalized==270) adjusted.width.toFloat() else adjusted.height.toFloat()
    val baseScale=maxOf(outWidth/rotatedWidth,outHeight/rotatedHeight)
    val scale=baseScale*zoom.coerceIn(1f,3f)
    matrix.postTranslate(-adjusted.width/2f,-adjusted.height/2f)
    matrix.postRotate(normalized.toFloat())
    matrix.postScale(scale,scale)
    val maxPanX=(outWidth*(zoom.coerceIn(1f,3f)-1f)/2f)
    val maxPanY=(outHeight*(zoom.coerceIn(1f,3f)-1f)/2f)
    matrix.postTranslate(outWidth/2f+offsetX.coerceIn(-1f,1f)*maxPanX,outHeight/2f+offsetY.coerceIn(-1f,1f)*maxPanY)
    canvas.drawBitmap(adjusted,matrix,Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG))
    if(adjusted!==source) adjusted.recycle()
    return output
}

private fun drawBitmapFitV43(canvas:Canvas,bmp:Bitmap,left:Int,top:Int,right:Int,bottom:Int){
    val scale=min((right-left).toFloat()/bmp.width,(bottom-top).toFloat()/bmp.height)
    val w=(bmp.width*scale).toInt();val h=(bmp.height*scale).toInt();val x=left+(right-left-w)/2;val y=top+(bottom-top-h)/2
    canvas.drawBitmap(bmp,null,android.graphics.Rect(x,y,x+w,y+h),Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG))
}

private fun drawWrappedTextV43(canvas:Canvas,text:String,x:Float,y:Float,maxWidth:Float,paint:Paint,maxLines:Int=5):Float{
    var line="";var yy=y;var count=0
    text.replace("\\n"," ").split(" ").filter{it.isNotBlank()}.forEach{word->
        if(count>=maxLines)return@forEach
        val candidate=if(line.isBlank())word else "$line $word"
        if(paint.measureText(candidate)>maxWidth&&line.isNotBlank()){canvas.drawText(line,x,yy,paint);yy+=paint.textSize+3f;count++;line=word}else line=candidate
    }
    if(line.isNotBlank()&&count<maxLines){canvas.drawText(line,x,yy,paint);yy+=paint.textSize+3f}
    return yy
}

private fun printPhotoPdfV43(context:android.content.Context,uri:Uri){
    val manager=context.getSystemService(android.content.Context.PRINT_SERVICE) as? PrintManager ?: return
    manager.print("Registro fotográfico clínico",object:PrintDocumentAdapter(){
        override fun onLayout(oldAttributes:PrintAttributes?,newAttributes:PrintAttributes?,cancellationSignal:CancellationSignal?,callback:LayoutResultCallback,extras:Bundle?){
            if(cancellationSignal?.isCanceled==true){callback.onLayoutCancelled();return}
            callback.onLayoutFinished(PrintDocumentInfo.Builder("registro_fotografico_clinico.pdf").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build(),true)
        }
        override fun onWrite(pages:Array<out android.print.PageRange>,destination:ParcelFileDescriptor,cancellationSignal:CancellationSignal?,callback:WriteResultCallback){
            try{context.contentResolver.openInputStream(uri)?.use{input->java.io.FileOutputStream(destination.fileDescriptor).use{output->input.copyTo(output)}}?:throw java.io.IOException("PDF no disponible");callback.onWriteFinished(arrayOf(android.print.PageRange.ALL_PAGES))}catch(e:Exception){callback.onWriteFailed(e.message)}
        }
    },null)
}

private fun createPhotoPdfV43(context:android.content.Context,photos:Map<String,String>,brightness:Map<String,Float>,contrast:Map<String,Float>,sharpness:Map<String,Float>,rotation:Map<String,Int>,zoom:Map<String,Float>,offsetX:Map<String,Float>,offsetY:Map<String,Float>,patient:String,recordId:String,dateText:String,purpose:String,notes:String,compact:Boolean,anonymous:Boolean):Uri? = runCatching {
    val pdf=PdfDocument()
    val extraoral=listOf("Frontal extraoral","Perfil derecho","Perfil izquierdo","Sonrisa")
    val intraoral=listOf("Frontal intraoral","Lateral derecha","Lateral izquierda","Oclusal superior","Oclusal inferior","Detalle de lesión / procedimiento")
    val ordered=extraoral+intraoral
    val entries=ordered.mapNotNull{k->photos[k]?.takeIf{it.isNotBlank()}?.let{k to it}}
    if(entries.isEmpty()){pdf.close();return@runCatching null}
    val perPage=if(compact)6 else 4;val chunks=entries.chunked(perPage);val generated=SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault()).format(Date())
    chunks.forEachIndexed{pageIndex,chunk->
        val page=pdf.startPage(PdfDocument.PageInfo.Builder(595,842,pageIndex+1).create());val canvas=page.canvas
        val title=Paint(Paint.ANTI_ALIAS_FLAG).apply{textSize=17f;isFakeBoldText=true};val body=Paint(Paint.ANTI_ALIAS_FLAG).apply{textSize=9f};val label=Paint(Paint.ANTI_ALIAS_FLAG).apply{textSize=10f;isFakeBoldText=true}
        canvas.drawText("EL EXPEDIENTE DEL DENTISTA · REGISTRO FOTOGRÁFICO",24f,28f,title)
        if(!anonymous){canvas.drawText("Paciente: "+patient.ifBlank{"Sin identificar"},24f,46f,body);canvas.drawText("Expediente: "+recordId.ifBlank{"—"},24f,60f,body)}else canvas.drawText("Registro sin datos identificativos",24f,48f,body)
        canvas.drawText("Fecha clínica: "+dateText,330f,46f,body);canvas.drawText("Finalidad: "+purpose.ifBlank{"No especificada"},24f,76f,body)
        val cols=2;val rows=if(compact)3 else 2;val cellW=267;val cellH=if(compact)205 else 285;val imageH=if(compact)160 else 225
        chunk.forEachIndexed{i,e->
            val col=i%cols;val row=i/cols;val left=24+col*279;val top=94+row*cellH;canvas.drawText(e.first,left.toFloat(),(top-5).toFloat(),label)
            val source=photoBitmapV43(context,e.second,1800)
            if(source==null){canvas.drawRect(left.toFloat(),top.toFloat(),(left+cellW).toFloat(),(top+imageH).toFloat(),Paint(Paint.ANTI_ALIAS_FLAG).apply{style=Paint.Style.STROKE});canvas.drawText("Toma no disponible",(left+12).toFloat(),(top+imageH/2).toFloat(),body)}else{
                val bmp=framedPhotoV43(source,brightness[e.first]?:0f,contrast[e.first]?:1f,sharpness[e.first]?:0f,rotation[e.first]?:0,zoom[e.first]?:1f,offsetX[e.first]?:0f,offsetY[e.first]?:0f)
                drawBitmapFitV43(canvas,bmp,left,top,left+cellW,top+imageH);bmp.recycle();source.recycle()
            }
        }
        if(pageIndex==chunks.lastIndex&&notes.isNotBlank()){canvas.drawText("Notas clínicas:",24f,748f,label);drawWrappedTextV43(canvas,notes,24f,764f,547f,body,4)}
        canvas.drawText("Registro fotográfico clínico · Generado $generated",24f,825f,body);canvas.drawText("Página "+(pageIndex+1)+"/"+chunks.size,520f,825f,body);pdf.finishPage(page)
    }
    val file=File(context.cacheDir,"registro_fotografico_clinico.pdf");file.outputStream().use{pdf.writeTo(it)};pdf.close();androidx.core.content.FileProvider.getUriForFile(context,context.packageName+".fileprovider",file)
}.getOrNull()

@Composable
fun ClinicalPhotographySheetV43(lang:String,onBack:()->Unit){
    val context=LocalContext.current
    var category by rememberRecordState("photo.category","Extraoral")
    var view by rememberRecordState("photo.view","Frontal extraoral")
    var purpose by rememberRecordState("photo.purpose","Documentación inicial")
    var notes by rememberRecordState("photo.notes","")
    var pdfCompact by rememberRecordState("photo.pdfCompact",false)
    var pdfIncludeIdentity by rememberRecordState("photo.pdfIncludeIdentity",true)
    val activeRecordId=LocalActiveRecordId.current.orEmpty()
    val activeProfile=remember(activeRecordId){runCatching{ClinicalRecordStore(context).loadAll().firstOrNull{it.id==activeRecordId}?.session?.profile}.getOrNull()}
    val patientLabel=listOfNotNull(activeProfile?.patientInitials?.takeIf{it.isNotBlank()},activeProfile?.exerciseName?.takeIf{it.isNotBlank()}).joinToString(" · ")
    val pdfDate=remember{SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault()).format(Date())}
    var lastPdfUri by remember{androidx.compose.runtime.mutableStateOf<Uri?>(null)}
    val photoUris=rememberRecordStateMap<String,String>("photo.uris")
    val photoBrightness=rememberRecordStateMap<String,Float>("photo.brightnessByView")
    val photoContrast=rememberRecordStateMap<String,Float>("photo.contrastByView")
    val photoSharpness=rememberRecordStateMap<String,Float>("photo.sharpnessByView")
    val photoRotation=rememberRecordStateMap<String,Int>("photo.rotationByView")
    val photoZoom=rememberRecordStateMap<String,Float>("photo.zoomByView")
    val photoOffsetX=rememberRecordStateMap<String,Float>("photo.offsetXByView")
    val photoOffsetY=rememberRecordStateMap<String,Float>("photo.offsetYByView")
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
    val brightness=photoBrightness[safeView] ?: 0f
    val contrast=photoContrast[safeView] ?: 1f
    val sharpness=photoSharpness[safeView] ?: 0f
    val rotation=(photoRotation[safeView] ?: 0).let{((it%360)+360)%360}
    val zoom=(photoZoom[safeView] ?: 1f).coerceIn(1f,3f)
    val offsetX=(photoOffsetX[safeView] ?: 0f).coerceIn(-1f,1f)
    val offsetY=(photoOffsetY[safeView] ?: 0f).coerceIn(-1f,1f)
    val original=remember(uriText){if(uriText.isBlank())null else photoBitmapV43(context,uriText,1400)}
    val preview=remember(original,brightness,contrast,sharpness){original?.let{adjustedPhotoV43(it,brightness,contrast,sharpness)}}

    ResponsiveScreenV17("📷 "+tr(lang,"Ficha de Fotografía Clínica","Clinical Photography Sheet"),
        tr(lang,"Registro local y offline de fotografías clínicas. Separa vistas extraorales e intraorales y permite preparar una hoja PDF para el expediente físico.","Local offline clinical photo record. Separates extraoral and intraoral views and can prepare a PDF sheet for the physical record."),onBack){profile->
        ResponsiveSectionV17(tr(lang,"1 · Tipo y vista","1 · Type and view")){
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
                listOf("Extraoral","Intraoral").forEach{o->FilterChip(category==o,{category=o;view=if(o=="Intraoral")intraoral.first() else extraoral.first()},{Text(o)},Modifier.weight(1f))}
            }
            val allViews=extraoral+intraoral
            val completed=allViews.count{!photoUris[it].isNullOrBlank()}
            Text(tr(lang,"Tomas registradas: $completed / ${allViews.size}","Recorded views: $completed / ${allViews.size}"),fontWeight=FontWeight.Bold)
            AdaptiveGridV17(views.size,when{profile.largeSystemText->1;profile.width==ScreenWidthV17.COMPACT->2;profile.width==ScreenWidthV17.MEDIUM->3;else->5}){i->
                val slot=views[i]
                val filled=!photoUris[slot].isNullOrBlank()
                Card(onClick={view=slot},modifier=Modifier.fillMaxWidth()){
                    Column(Modifier.fillMaxWidth().padding(10.dp)){
                        Text(if(filled)"✓ $slot" else "○ $slot",fontWeight=if(safeView==slot)FontWeight.Black else FontWeight.Medium)
                        Text(if(filled)tr(lang,"Fotografía registrada","Photo registered") else tr(lang,"Pendiente","Pending"),style=MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        ResponsiveSectionV17(tr(lang,"2 · Fotografía del paciente","2 · Patient photograph"),tr(lang,"Las fotografías permanecen vinculadas localmente al expediente activo.","Photos remain locally linked to the active record.")){
            Button(onClick={launcher.launch(arrayOf("image/*"))},modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Seleccionar / reemplazar fotografía","Select / replace photograph"),fontWeight=FontWeight.Bold)}
            if(preview!=null){
                Image(preview.asImageBitmap(),safeView,Modifier.fillMaxWidth().height(260.dp).pointerInput(safeView,zoom,offsetX,offsetY){detectTransformGestures{_,pan,gestureZoom,_->photoZoom[safeView]=(zoom*gestureZoom).coerceIn(1f,3f);photoOffsetX[safeView]=(offsetX+pan.x/300f).coerceIn(-1f,1f);photoOffsetY[safeView]=(offsetY+pan.y/300f).coerceIn(-1f,1f)}}.graphicsLayer{rotationZ=rotation.toFloat();scaleX=zoom;scaleY=zoom;translationX=offsetX*180f;translationY=offsetY*180f},contentScale=ContentScale.Fit)
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
                    OutlinedButton(onClick={photoRotation[safeView]=(rotation+270)%360},modifier=Modifier.weight(1f)){Text("↶ 90°")}
                    OutlinedButton(onClick={photoRotation[safeView]=(rotation+90)%360},modifier=Modifier.weight(1f)){Text("↷ 90°")}
                }
                Text(tr(lang,"Zoom / recorte visual","Zoom / visual crop"));Slider(zoom,{photoZoom[safeView]=it},valueRange=1f..3f)
                Text(tr(lang,"Posición horizontal","Horizontal position"));Slider(offsetX,{photoOffsetX[safeView]=it},valueRange=-1f..1f)
                Text(tr(lang,"Posición vertical","Vertical position"));Slider(offsetY,{photoOffsetY[safeView]=it},valueRange=-1f..1f)
                Text(tr(lang,"Brillo","Brightness"));Slider(brightness,{photoBrightness[safeView]=it},valueRange=-1f..1f)
                Text(tr(lang,"Contraste","Contrast"));Slider(contrast,{photoContrast[safeView]=it},valueRange=.5f..1.8f)
                Text(tr(lang,"Nitidez","Sharpness"));Slider(sharpness,{photoSharpness[safeView]=it},valueRange=0f..1f)
                OutlinedButton(onClick={photoBrightness[safeView]=0f;photoContrast[safeView]=1f;photoSharpness[safeView]=0f;photoRotation[safeView]=0;photoZoom[safeView]=1f;photoOffsetX[safeView]=0f;photoOffsetY[safeView]=0f},modifier=Modifier.fillMaxWidth()){Text("↺ "+tr(lang,"Restablecer foto original y encuadre","Reset original photo and framing"))}
                Text(tr(lang,"El zoom y la posición funcionan como recorte no destructivo: la fotografía original se conserva y puedes volver a ella con Restablecer.","Zoom and position act as a non-destructive crop: the original photo is preserved and can be restored with Reset."),style=MaterialTheme.typography.bodySmall)
                OutlinedButton(onClick={
                    val source=Uri.parse(uriText)
                    val intent=Intent("com.android.camera.action.CROP").apply{setDataAndType(source,"image/*");putExtra("crop",true);putExtra("return-data",false);addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)}
                    runCatching{cropLauncher.launch(intent)}.onFailure{Toast.makeText(context,tr(lang,"El dispositivo no ofrece un editor externo compatible. Puedes usar el recorte no destructivo de esta ficha.","No compatible external editor is available. You can use this sheet's non-destructive crop."),Toast.LENGTH_SHORT).show()}
                },modifier=Modifier.fillMaxWidth()){Text("✂️ "+tr(lang,"Editor externo opcional","Optional external editor"))}
            }
        }
        LocalClinicalImageSectionV46(lang,profile,"Fotografía extraoral","Extraoral photography")
        LocalClinicalImageSectionV46(lang,profile,"Fotografía intraoral","Intraoral photography")
        ResponsiveSectionV17(tr(lang,"3 · Finalidad y notas","3 · Purpose and notes")){
            val purposes=listOf("Documentación inicial","Diagnóstico / seguimiento","Antes del tratamiento","Durante el tratamiento","Después del tratamiento","Comunicación / interconsulta")
            AdaptiveGridV17(purposes.size,if(profile.largeSystemText)1 else if(profile.width==ScreenWidthV17.COMPACT)2 else 3){i->FilterChip(purpose==purposes[i],{purpose=purposes[i]},{Text(purposes[i])},Modifier.fillMaxWidth())}
            val noteOptions=listOf("Calidad adecuada","Repetir por encuadre","Repetir por iluminación","Referencia diagnóstica","Seguimiento comparativo","Sin observaciones")
            AdaptiveGridV17(noteOptions.size,if(profile.largeSystemText)1 else 2){i->val o=noteOptions[i];FilterChip(notes==o,{notes=o},{Text(o)},Modifier.fillMaxWidth())}
        }
        ResponsiveSectionV17(tr(lang,"4 · Expediente físico","4 · Physical record"),tr(lang,"Genera una hoja clínica local con identificación, fecha, etiquetas, finalidad y notas. Elige composición completa o compacta.","Creates a local clinical sheet with identification, date, labels, purpose and notes. Choose full or compact layout.")){
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(!pdfCompact,{pdfCompact=false},{Text(tr(lang,"Completo","Full"))},Modifier.weight(1f));FilterChip(pdfCompact,{pdfCompact=true},{Text(tr(lang,"Compacto","Compact"))},Modifier.weight(1f))}
            Row(Modifier.fillMaxWidth()){Checkbox(pdfIncludeIdentity,{pdfIncludeIdentity=it});Text(tr(lang,"Incluir identificación y número de expediente","Include identification and record number"),Modifier.padding(top=12.dp))}
            Button(onClick={
                val uri=createPhotoPdfV43(context,photoUris.toMap(),photoBrightness.toMap(),photoContrast.toMap(),photoSharpness.toMap(),photoRotation.toMap(),photoZoom.toMap(),photoOffsetX.toMap(),photoOffsetY.toMap(),if(pdfIncludeIdentity)patientLabel else "",if(pdfIncludeIdentity)activeRecordId else "",pdfDate,purpose,notes,pdfCompact,!pdfIncludeIdentity)
                if(uri!=null){
                    lastPdfUri=uri
                    val send=Intent(Intent.ACTION_SEND).apply{type="application/pdf";putExtra(Intent.EXTRA_STREAM,uri);addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)}
                    context.startActivity(Intent.createChooser(send,tr(lang,"Guardar, imprimir o compartir PDF","Save, print or share PDF")))
                }else Toast.makeText(context,tr(lang,"No fue posible generar el PDF.","PDF could not be generated."),Toast.LENGTH_SHORT).show()
            },enabled=photoUris.isNotEmpty(),modifier=Modifier.fillMaxWidth()){Text("📄 "+tr(lang,"Generar / guardar / compartir PDF","Generate / save / share PDF"),fontWeight=FontWeight.Bold)}
            OutlinedButton(onClick={lastPdfUri?.let{printPhotoPdfV43(context,it)}},enabled=lastPdfUri!=null,modifier=Modifier.fillMaxWidth()){Text("🖨️ "+tr(lang,"Imprimir último PDF generado","Print last generated PDF"))}
        }
        NoticeCard(tr(lang,"La fotografía complementa la exploración. Los ajustes de brillo, contraste y nitidez son de presentación y no deben utilizarse para ocultar, crear o alterar hallazgos clínicos.","Photography complements examination. Brightness, contrast and sharpness adjustments are for presentation and must not be used to hide, create or alter clinical findings."))
    }
}
