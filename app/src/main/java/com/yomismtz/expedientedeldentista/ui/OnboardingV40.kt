package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.ui.theme.BirdPaletteChoices
import com.yomismtz.expedientedeldentista.ui.theme.paletteDisplayName
import com.yomismtz.expedientedeldentista.ui.theme.paletteSwatches

@Composable
fun OnboardingV40Screen(preferences:AppPreferences,onPreferencesChanged:(AppPreferences)->Unit,onContinue:(AppPreferences)->Unit){
    val lang=preferences.languageTag;val cs=MaterialTheme.colorScheme
    Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(cs.primary,cs.primaryContainer,cs.background))).safeDrawingPadding().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(14.dp),horizontalAlignment=Alignment.CenterHorizontally){
        Text("YSM Expediente",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Black,color=cs.onPrimary,textAlign=TextAlign.Center)
        Text(if(lang=="en")"Interactive guide to the dentist's clinical record" else "Guía interactiva del expediente clínico odontológico",color=cs.onPrimary,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
        Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=cs.surface.copy(alpha=.96f)),shape=RoundedCornerShape(20.dp)){
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
                Text(if(lang=="en")"Language" else "Idioma",fontWeight=FontWeight.Black)
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(lang=="es",{onPreferencesChanged(preferences.copy(languageTag="es"))},{Text("Español")});FilterChip(lang=="en",{onPreferencesChanged(preferences.copy(languageTag="en"))},{Text("English")})}
                Text(if(lang=="en")"Doctor / Doctora · always side by side" else "Doctor / Doctora · siempre uno al lado del otro",fontWeight=FontWeight.Black)
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(10.dp)){
                    ClinicianBirdCardV40(ClinicianTitle.DOCTORA,preferences.clinicianTitle==ClinicianTitle.DOCTORA,preferences.birdPaletteStyle,lang,{onPreferencesChanged(preferences.copy(clinicianTitle=ClinicianTitle.DOCTORA))})
                    ClinicianBirdCardV40(ClinicianTitle.DOCTOR,preferences.clinicianTitle==ClinicianTitle.DOCTOR,preferences.birdPaletteStyle,lang,{onPreferencesChanged(preferences.copy(clinicianTitle=ClinicianTitle.DOCTOR))})
                }
                Text(if(lang=="en")"Choose a bird palette. The bird on the clinician's shoulder changes with the palette." else "Elige una paleta de ave. El ave del hombro cambia con la paleta.",fontWeight=FontWeight.Black)
                BirdPaletteChoices.chunked(2).forEach{pair->Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){pair.forEach{style->PaletteCardV40(style,lang,preferences.birdPaletteStyle==style,{onPreferencesChanged(preferences.copy(birdPaletteStyle=style))},Modifier.weight(1f))};if(pair.size==1)Spacer(Modifier.weight(1f))}}
            }
        }
        NoticeCard(if(lang=="en")"The application is an educational guide. It does not ask for a patient's name, address, telephone or real record number." else "La aplicación es una guía educativa. No pide nombre, domicilio, teléfono ni número real de expediente del paciente.")
        Button({onContinue(preferences.copy(onboardingComplete=true))},Modifier.fillMaxWidth().height(58.dp)){Text(if(lang=="en")"Continue" else "Continuar",fontWeight=FontWeight.Black)}
    }
}

@Composable private fun ClinicianBirdCardV40(title:ClinicianTitle,selected:Boolean,style:BirdPaletteStyle,lang:String,onClick:()->Unit){
    Card(onClick=onClick,modifier=Modifier.width(245.dp),colors=CardDefaults.cardColors(containerColor=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),border=BorderStroke(if(selected)3.dp else 1.dp,if(selected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),shape=RoundedCornerShape(18.dp)){
        Column(Modifier.fillMaxWidth().padding(10.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(5.dp)){
            ClinicianBirdIllustrationV40(title,style)
            Text(if(title==ClinicianTitle.DOCTORA)"Doctora" else "Doctor",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
            Text("🐦 ${paletteDisplayName(style,lang)}",fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
            if(selected)Text(if(lang=="en")"Selected ✓" else "Seleccionado ✓",color=MaterialTheme.colorScheme.primary,fontWeight=FontWeight.Bold)
        }
    }
}

@Composable private fun ClinicianBirdIllustrationV40(title:ClinicianTitle,style:BirdPaletteStyle){
    val sw=paletteSwatches(style)
    val outline=MaterialTheme.colorScheme.outline
    val primary=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(190.dp)){
        val w=size.width;val h=size.height;val skin=Color(0xFFD9A27F);val coat=Color.White
        drawCircle(skin,w*.12f,Offset(w*.50f,h*.29f))
        val hair=if(title==ClinicianTitle.DOCTORA)Color(0xFF4B2E2B) else Color(0xFF332A26)
        drawArc(hair,190f,160f,false,Offset(w*.37f,h*.14f),Size(w*.26f,h*.25f),style=Stroke(w*.055f))
        val torso=Path().apply{moveTo(w*.25f,h*.92f);lineTo(w*.30f,h*.48f);quadraticBezierTo(w*.50f,h*.40f,w*.70f,h*.48f);lineTo(w*.75f,h*.92f);close()}
        drawPath(torso,coat);drawPath(torso,outline,style=Stroke(2f))
        drawLine(primary,Offset(w*.50f,h*.50f),Offset(w*.50f,h*.90f),strokeWidth=3f)
        val bx=w*.72f;val by=h*.48f
        drawOval(sw[0],Offset(bx-w*.055f,by-h*.045f),Size(w*.11f,h*.10f))
        drawCircle(sw[1],w*.036f,Offset(bx+w*.035f,by-h*.055f))
        val beakLong=style in setOf(BirdPaletteStyle.TUCAN,BirdPaletteStyle.COLIBRI,BirdPaletteStyle.MARTIN_PESCADOR,BirdPaletteStyle.ABEJARUCO)
        val beak=Path().apply{moveTo(bx+w*.065f,by-h*.06f);lineTo(bx+w*(if(beakLong).16f else .10f),by-h*.045f);lineTo(bx+w*.065f,by-h*.025f);close()}
        drawPath(beak,sw[2]);drawCircle(Color.Black,3f,Offset(bx+w*.045f,by-h*.065f))
        val tailLong=style in setOf(BirdPaletteStyle.QUETZAL,BirdPaletteStyle.PAVO_REAL,BirdPaletteStyle.GUACAMAYA,BirdPaletteStyle.FENIX)
        drawLine(sw[0],Offset(bx-w*.02f,by+h*.03f),Offset(bx-w*(if(tailLong).11f else .06f),by+h*(if(tailLong).20f else .11f)),strokeWidth=8f)
        if(style==BirdPaletteStyle.NINFA)drawLine(sw[2],Offset(bx+w*.02f,by-h*.08f),Offset(bx+w*.005f,by-h*.14f),strokeWidth=4f)
        if(style==BirdPaletteStyle.PAVO_REAL){for(i in -2..2)drawCircle(sw[(i+2)%3],7f,Offset(bx+w*i*.035f,by+h*.17f))}
        drawLine(Color(0xFF6B513F),Offset(bx-w*.06f,by+h*.055f),Offset(bx+w*.06f,by+h*.055f),strokeWidth=3f)
    }
}

@Composable private fun PaletteCardV40(style:BirdPaletteStyle,lang:String,selected:Boolean,onClick:()->Unit,modifier:Modifier=Modifier){
    val sw=paletteSwatches(style)
    Card(onClick=onClick,modifier=modifier,colors=CardDefaults.cardColors(containerColor=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),border=BorderStroke(if(selected)2.dp else 1.dp,if(selected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),shape=RoundedCornerShape(14.dp)){
        Column(Modifier.fillMaxWidth().padding(9.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(6.dp)){
            Text(paletteDisplayName(style,lang),fontWeight=FontWeight.Black,textAlign=TextAlign.Center)
            Row(horizontalArrangement=Arrangement.spacedBy(3.dp)){sw.forEach{c->Canvas(Modifier.width(25.dp).height(18.dp)){drawRoundRect(c,cornerRadius=androidx.compose.ui.geometry.CornerRadius(6f,6f))}}}
        }
    }
}
