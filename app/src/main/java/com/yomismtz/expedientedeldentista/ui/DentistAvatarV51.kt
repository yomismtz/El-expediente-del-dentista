package com.yomismtz.expedientedeldentista.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.settings.*

@DrawableRes private fun baseRes(p: AppPreferences) = if (p.clinicianTitle == ClinicianTitle.DOCTORA) R.drawable.avatar_base_female else R.drawable.avatar_base_male
@DrawableRes private fun skinRes(v: SkinTone) = when(v){SkinTone.LIGHT->R.drawable.avatar_skin_1;SkinTone.PEACH->R.drawable.avatar_skin_2;SkinTone.TAN->R.drawable.avatar_skin_3;SkinTone.BROWN->R.drawable.avatar_skin_4;SkinTone.DEEP->R.drawable.avatar_skin_5}
@DrawableRes private fun hairRes(p: AppPreferences): Int { val n=p.hairStyle.ordinal+1; return if(p.clinicianTitle==ClinicianTitle.DOCTORA) when(n){1->R.drawable.avatar_hair_f_1;2->R.drawable.avatar_hair_f_2;3->R.drawable.avatar_hair_f_3;4->R.drawable.avatar_hair_f_4;5->R.drawable.avatar_hair_f_5;else->R.drawable.avatar_hair_f_6} else when(n){1->R.drawable.avatar_hair_m_1;2->R.drawable.avatar_hair_m_2;3->R.drawable.avatar_hair_m_3;4->R.drawable.avatar_hair_m_4;5->R.drawable.avatar_hair_m_5;else->R.drawable.avatar_hair_m_6}}
@DrawableRes private fun eyeRes(p: AppPreferences)=when((p.eyeShape.ordinal*2+p.eyeColor.ordinal)%6){0->R.drawable.avatar_eye_1;1->R.drawable.avatar_eye_2;2->R.drawable.avatar_eye_3;3->R.drawable.avatar_eye_4;4->R.drawable.avatar_eye_5;else->R.drawable.avatar_eye_6}
@DrawableRes private fun mouthRes(p: AppPreferences)=when((p.mouthStyle.ordinal*3+p.lipColor.ordinal)%8){0->R.drawable.avatar_mouth_1;1->R.drawable.avatar_mouth_2;2->R.drawable.avatar_mouth_3;3->R.drawable.avatar_mouth_4;4->R.drawable.avatar_mouth_5;5->R.drawable.avatar_mouth_6;6->R.drawable.avatar_mouth_7;else->R.drawable.avatar_mouth_8}
@DrawableRes private fun scrubRes(v:ScrubColor)=when(v){ScrubColor.TURQUOISE->R.drawable.avatar_scrub_1;ScrubColor.BLUE->R.drawable.avatar_scrub_2;ScrubColor.NAVY->R.drawable.avatar_scrub_3;ScrubColor.PURPLE->R.drawable.avatar_scrub_4;ScrubColor.LILAC->R.drawable.avatar_scrub_5;ScrubColor.PINK->R.drawable.avatar_scrub_6;ScrubColor.BLACK->R.drawable.avatar_scrub_7;ScrubColor.WHITE->R.drawable.avatar_scrub_8;ScrubColor.MINT->R.drawable.avatar_scrub_9;ScrubColor.WINE->R.drawable.avatar_scrub_10}
@DrawableRes private fun mascotRes(v:MascotStyle)=when(v){MascotStyle.TOUCAN->R.drawable.mascot_toucan;MascotStyle.LOVEBIRD_GREEN->R.drawable.mascot_lovebird_green;MascotStyle.LOVEBIRD_PASTEL->R.drawable.mascot_lovebird_pastel;MascotStyle.PUG->R.drawable.mascot_pug;MascotStyle.POMERANIAN->R.drawable.mascot_pomeranian;MascotStyle.CLOWNFISH->R.drawable.mascot_clownfish;MascotStyle.SHARK->R.drawable.mascot_shark;MascotStyle.RAVEN->R.drawable.mascot_raven;MascotStyle.MANDARIN_DUCK->R.drawable.mascot_mandarin_duck;MascotStyle.FLAMINGO->R.drawable.mascot_flamingo;MascotStyle.MACAW->R.drawable.mascot_macaw;MascotStyle.PERSIAN_CAT->R.drawable.mascot_persian_cat;MascotStyle.WHITE_YELLOW_CAT->R.drawable.mascot_white_yellow_cat;MascotStyle.ELEPHANT->R.drawable.mascot_elephant;MascotStyle.TURTLE->R.drawable.mascot_turtle;MascotStyle.WHITE_RABBIT->R.drawable.mascot_white_rabbit;MascotStyle.IGUANA->R.drawable.mascot_iguana}

@Composable private fun Layer(@DrawableRes id:Int,modifier:Modifier=Modifier){Image(painterResource(id),null,modifier,contentScale=ContentScale.Fit)}

@Composable fun DentistAvatarPreviewV51(p:AppPreferences,modifier:Modifier=Modifier){
 Card(modifier=modifier,shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){
  Column(Modifier.fillMaxWidth().padding(12.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(8.dp)){
   Canvas(Modifier.fillMaxWidth().height(300.dp)){ drawDentistV52(p) }
   Card(colors=CardDefaults.cardColors(containerColor=Color(0xFFF8F4FF))){
    Column(Modifier.fillMaxWidth().padding(10.dp),horizontalAlignment=Alignment.CenterHorizontally){
     Text(tr(p.languageTag,"Tu personaje","Your character"),fontWeight=FontWeight.Bold,color=Color(0xFF20133A))
     Text(tr(p.languageTag,"La vista previa cambia al seleccionar cada característica.","The preview changes as you select each feature."),color=Color(0xFF51475F),textAlign=TextAlign.Center)
    }
   }
  }
 }
}

private fun DrawScope.drawDentistV52(p:AppPreferences){
 val w=size.width; val h=size.height; val cx=w/2f
 val skin=when(p.skinTone){SkinTone.LIGHT->Color(0xFFFFD6C2);SkinTone.PEACH->Color(0xFFF4B38D);SkinTone.TAN->Color(0xFFD98A5F);SkinTone.BROWN->Color(0xFFA95F43);SkinTone.DEEP->Color(0xFF70402F)}
 val scrub=when(p.scrubColor){ScrubColor.TURQUOISE->Color(0xFF22A9A0);ScrubColor.BLUE->Color(0xFF397CC5);ScrubColor.NAVY->Color(0xFF283B70);ScrubColor.PURPLE->Color(0xFF7B55B7);ScrubColor.LILAC->Color(0xFFAA87D8);ScrubColor.PINK->Color(0xFFE879A6);ScrubColor.BLACK->Color(0xFF29272D);ScrubColor.WHITE->Color(0xFFF1F1F3);ScrubColor.MINT->Color(0xFF77CDB3);ScrubColor.WINE->Color(0xFF853A58)}
 val hair=Color(0xFF553322); val eye=when(p.eyeColor){EyeColor.BROWN->Color(0xFF5A3426);EyeColor.HAZEL->Color(0xFF8A6A35);EyeColor.GREEN->Color(0xFF4C7A58);EyeColor.BLUE->Color(0xFF4779A8);EyeColor.GRAY->Color(0xFF747982)}
 // cuerpo y uniforme
 drawRoundRect(scrub,Offset(cx-w*.19f,h*.49f),Size(w*.38f,h*.39f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.07f))
 drawRect(scrub,Offset(cx-w*.13f,h*.82f),Size(w*.11f,h*.16f)); drawRect(scrub,Offset(cx+w*.02f,h*.82f),Size(w*.11f,h*.16f))
 // cuello y cabeza
 drawRoundRect(skin,Offset(cx-w*.045f,h*.40f),Size(w*.09f,h*.12f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))
 drawOval(skin,Offset(cx-w*.145f,h*.12f),Size(w*.29f,h*.34f))
 // cabello: silueta cambia con el estilo
 val hairTop=when(p.hairStyle){HairStyle.SHORT->h*.09f;HairStyle.WAVY->h*.055f;HairStyle.CURLY->h*.035f;HairStyle.LONG->h*.045f;HairStyle.BOB->h*.06f;HairStyle.BUN->h*.02f}
 drawOval(hair,Offset(cx-w*.155f,hairTop),Size(w*.31f,h*.18f))
 if(p.hairStyle==HairStyle.LONG||p.hairStyle==HairStyle.BOB) {drawRoundRect(hair,Offset(cx-w*.17f,h*.13f),Size(w*.055f,h*.29f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.025f));drawRoundRect(hair,Offset(cx+w*.115f,h*.13f),Size(w*.055f,h*.29f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.025f))}
 if(p.hairStyle==HairStyle.BUN) drawCircle(hair,w*.065f,Offset(cx,h*.055f))
 if(p.hairStyle==HairStyle.CURLY){for(i in -3..3) drawCircle(hair,w*.035f,Offset(cx+i*w*.04f,h*.105f+(kotlin.math.abs(i)%2)*h*.015f))}
 // ojos: tamaño/forma cambia
 val eyeRx=when(p.eyeShape){EyeShape.ROUND->w*.026f;EyeShape.ALMOND->w*.032f;EyeShape.SOFT->w*.029f}; val eyeRy=when(p.eyeShape){EyeShape.ROUND->w*.026f;EyeShape.ALMOND->w*.018f;EyeShape.SOFT->w*.021f}
 listOf(cx-w*.06f,cx+w*.06f).forEach{x->drawOval(Color.White,Offset(x-eyeRx,h*.265f-eyeRy),Size(eyeRx*2,eyeRy*2));drawCircle(eye,eyeRy*.62f,Offset(x,h*.265f));drawCircle(Color.Black,eyeRy*.28f,Offset(x,h*.265f))}
 // nariz
 drawCircle(Color(0xFFD98E72),w*.012f,Offset(cx,h*.315f))
 // boca y labios
 val lip=when(p.lipColor){LipColor.NATURAL->Color(0xFFC97970);LipColor.ROSE->Color(0xFFD56E82);LipColor.CORAL->Color(0xFFE46F63);LipColor.RED->Color(0xFFC7464C);LipColor.WINE->Color(0xFF873B55)}
 val mw=when(p.mouthStyle){MouthStyle.NATURAL->w*.07f;MouthStyle.SMILE->w*.09f;MouthStyle.FULL->w*.085f}; val mh=if(p.mouthStyle==MouthStyle.FULL) h*.025f else h*.014f
 drawOval(lip,Offset(cx-mw/2,h*.355f),Size(mw,mh)); if(p.mouthStyle==MouthStyle.SMILE) drawOval(Color.White,Offset(cx-mw*.34f,h*.356f),Size(mw*.68f,mh*.45f))
 // insignia dental simple
 drawCircle(Color.White,w*.022f,Offset(cx+w*.09f,h*.59f))
}

private fun mascotName(v:MascotStyle)=v.name.lowercase().replace('_',' ').replaceFirstChar(Char::uppercase)

@Composable fun DentistAvatarCustomizerV51(p:AppPreferences,onChange:(AppPreferences)->Unit,lang:String){
 Text(tr(lang,"Crea tu dentista","Create your dentist"),style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black)
 Text(tr(lang,"Personaliza por secciones. La vista previa usa únicamente recursos compatibles para evitar deformaciones.","Customize by sections. The preview uses only compatible resources to avoid distortion."),style=MaterialTheme.typography.bodyMedium)
 DentistAvatarPreviewV51(p,Modifier.fillMaxWidth())
 AvatarChoiceV52(tr(lang,"1. Sexo / personaje base","1. Sex / base character"),ClinicianTitle.entries,p.clinicianTitle,{onChange(p.copy(clinicianTitle=it))}){if(it==ClinicianTitle.DOCTORA)tr(lang,"Mujer","Woman") else tr(lang,"Hombre","Man")}
 AvatarChoiceV52(tr(lang,"2. Color de piel","2. Skin tone"),SkinTone.entries,p.skinTone,{onChange(p.copy(skinTone=it))}){when(it){SkinTone.LIGHT->tr(lang,"Claro","Light");SkinTone.PEACH->tr(lang,"Durazno","Peach");SkinTone.TAN->tr(lang,"Bronceado","Tan");SkinTone.BROWN->tr(lang,"Moreno","Brown");SkinTone.DEEP->tr(lang,"Profundo","Deep")}}
 AvatarChoiceV52(tr(lang,"3. Estilo de cabello","3. Hair style"),HairStyle.entries,p.hairStyle,{onChange(p.copy(hairStyle=it))}){when(it){HairStyle.SHORT->tr(lang,"Corto","Short");HairStyle.WAVY->tr(lang,"Ondulado","Wavy");HairStyle.CURLY->tr(lang,"Rizado","Curly");HairStyle.LONG->tr(lang,"Largo","Long");HairStyle.BOB->"Bob";HairStyle.BUN->tr(lang,"Chongo","Bun")}}
 AvatarChoiceV52(tr(lang,"4. Forma de ojos","4. Eye shape"),EyeShape.entries,p.eyeShape,{onChange(p.copy(eyeShape=it))}){when(it){EyeShape.ROUND->tr(lang,"Redondos","Round");EyeShape.ALMOND->tr(lang,"Almendrados","Almond");EyeShape.SOFT->tr(lang,"Suaves","Soft")}}
 AvatarChoiceV52(tr(lang,"Color de ojos","Eye color"),EyeColor.entries,p.eyeColor,{onChange(p.copy(eyeColor=it))}){when(it){EyeColor.BROWN->tr(lang,"Café","Brown");EyeColor.HAZEL->tr(lang,"Avellana","Hazel");EyeColor.GREEN->tr(lang,"Verde","Green");EyeColor.BLUE->tr(lang,"Azul","Blue");EyeColor.GRAY->tr(lang,"Gris","Gray")}}
 AvatarChoiceV52(tr(lang,"5. Boca","5. Mouth"),MouthStyle.entries,p.mouthStyle,{onChange(p.copy(mouthStyle=it))}){when(it){MouthStyle.NATURAL->tr(lang,"Natural","Natural");MouthStyle.SMILE->tr(lang,"Sonrisa","Smile");MouthStyle.FULL->tr(lang,"Labios llenos","Full")}}
 AvatarChoiceV52(tr(lang,"Color de labios","Lip color"),LipColor.entries,p.lipColor,{onChange(p.copy(lipColor=it))}){when(it){LipColor.NATURAL->tr(lang,"Natural","Natural");LipColor.ROSE->tr(lang,"Rosa","Rose");LipColor.CORAL->"Coral";LipColor.RED->tr(lang,"Rojo","Red");LipColor.WINE->tr(lang,"Vino","Wine")}}
 AvatarChoiceV52(tr(lang,"6. Color del quirúrgico","6. Scrub color"),ScrubColor.entries,p.scrubColor,{onChange(p.copy(scrubColor=it))}){when(it){ScrubColor.TURQUOISE->tr(lang,"Turquesa","Turquoise");ScrubColor.BLUE->tr(lang,"Azul","Blue");ScrubColor.NAVY->tr(lang,"Azul marino","Navy");ScrubColor.PURPLE->tr(lang,"Morado","Purple");ScrubColor.LILAC->tr(lang,"Lila","Lilac");ScrubColor.PINK->tr(lang,"Rosa","Pink");ScrubColor.BLACK->tr(lang,"Negro","Black");ScrubColor.WHITE->tr(lang,"Blanco","White");ScrubColor.MINT->tr(lang,"Menta","Mint");ScrubColor.WINE->tr(lang,"Vino","Wine")}}
 AvatarChoiceV52(tr(lang,"7. Mascota · define la paleta","7. Pet · sets palette"),MascotStyle.entries,p.mascotStyle,{onChange(p.copy(mascotStyle=it,birdPaletteStyle=paletteForMascot(it)))}){mascotNameV52(it,lang)}
 NoticeCard(tr(lang,"La mascota cambia automáticamente la paleta de la app. Agaporni es la opción predeterminada.","The pet automatically changes the app palette. Lovebird is the default option."))
}

private fun mascotNameV52(v:MascotStyle,lang:String)=when(v){
 MascotStyle.TOUCAN->tr(lang,"Tucán","Toucan");MascotStyle.LOVEBIRD_GREEN->tr(lang,"Agaporni verde","Green lovebird");MascotStyle.LOVEBIRD_PASTEL->tr(lang,"Agaporni pastel","Pastel lovebird");MascotStyle.PUG->"Pug";MascotStyle.POMERANIAN->tr(lang,"Pomerania","Pomeranian");MascotStyle.CLOWNFISH->tr(lang,"Pez payaso","Clownfish");MascotStyle.SHARK->tr(lang,"Tiburón","Shark");MascotStyle.RAVEN->tr(lang,"Cuervo","Raven");MascotStyle.MANDARIN_DUCK->tr(lang,"Pato mandarín","Mandarin duck");MascotStyle.FLAMINGO->tr(lang,"Flamenco","Flamingo");MascotStyle.MACAW->tr(lang,"Guacamaya","Macaw");MascotStyle.PERSIAN_CAT->tr(lang,"Gato persa","Persian cat");MascotStyle.WHITE_YELLOW_CAT->tr(lang,"Gato blanco y amarillo","White/yellow cat");MascotStyle.ELEPHANT->tr(lang,"Elefante","Elephant");MascotStyle.TURTLE->tr(lang,"Tortuga","Turtle");MascotStyle.WHITE_RABBIT->tr(lang,"Conejo blanco","White rabbit");MascotStyle.IGUANA->"Iguana"
}

@Composable private fun <T> AvatarChoiceV52(title:String,values:List<T>,selected:T,onSelect:(T)->Unit,label:(T)->String){
 Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(6.dp)){
  Text(title,fontWeight=FontWeight.Bold)
  Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)){
   values.forEach{v->FilterChip(selected==v,{onSelect(v)},{Text(label(v),textAlign=TextAlign.Center)},Modifier.widthIn(min=96.dp))}
  }
 }
}


