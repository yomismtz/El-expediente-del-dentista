package com.yomismtz.expedientedeldentista.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
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
   Box(Modifier.fillMaxWidth().height(300.dp)){
    Canvas(Modifier.fillMaxSize()){ drawDentistV52(p) }
    Image(
     painter=painterResource(mascotRes(p.mascotStyle)),
     contentDescription=mascotNameV52(p.mascotStyle,p.languageTag),
     modifier=Modifier.align(Alignment.BottomEnd).size(118.dp).padding(4.dp),
     contentScale=ContentScale.Fit
    )
   }
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
 val skin=when(p.skinTone){SkinTone.LIGHT->Color(0xFFFFD9C5);SkinTone.PEACH->Color(0xFFF2B48F);SkinTone.TAN->Color(0xFFD68B62);SkinTone.BROWN->Color(0xFFA96348);SkinTone.DEEP->Color(0xFF704333)}
 val shadow=Color.Black.copy(alpha=.10f); val highlight=Color.White.copy(alpha=.22f)
 val scrub=when(p.scrubColor){ScrubColor.TURQUOISE->Color(0xFF27AAA1);ScrubColor.BLUE->Color(0xFF3E7FC7);ScrubColor.NAVY->Color(0xFF31406F);ScrubColor.PURPLE->Color(0xFF7A56B5);ScrubColor.LILAC->Color(0xFFA98AD4);ScrubColor.PINK->Color(0xFFE37CA7);ScrubColor.BLACK->Color(0xFF302E34);ScrubColor.WHITE->Color(0xFFECECF0);ScrubColor.MINT->Color(0xFF7CCCB4);ScrubColor.WINE->Color(0xFF843D59)}
 val hair=if(p.clinicianTitle==ClinicianTitle.DOCTORA) Color(0xFF5B392B) else Color(0xFF493127)
 val eye=when(p.eyeColor){EyeColor.BROWN->Color(0xFF5A3426);EyeColor.HAZEL->Color(0xFF8A6A35);EyeColor.GREEN->Color(0xFF4C7A58);EyeColor.BLUE->Color(0xFF4779A8);EyeColor.GRAY->Color(0xFF747982)}
 // sombra suave y cuerpo tipo figura 3D
 drawOval(shadow,Offset(cx-w*.19f,h*.91f),Size(w*.38f,h*.055f))
 drawRoundRect(scrub,Offset(cx-w*.19f,h*.50f),Size(w*.38f,h*.36f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.075f))
 drawRoundRect(highlight,Offset(cx-w*.145f,h*.525f),Size(w*.055f,h*.26f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))
 drawRoundRect(scrub,Offset(cx-w*.135f,h*.81f),Size(w*.105f,h*.14f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f));drawRoundRect(scrub,Offset(cx+w*.03f,h*.81f),Size(w*.105f,h*.14f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))
 drawRoundRect(Color(0xFFF4F4F6),Offset(cx-w*.135f,h*.935f),Size(w*.105f,h*.025f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.02f));drawRoundRect(Color(0xFFF4F4F6),Offset(cx+w*.03f,h*.935f),Size(w*.105f,h*.025f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.02f))
 drawRoundRect(skin,Offset(cx-w*.045f,h*.405f),Size(w*.09f,h*.12f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))
 // cabeza y orejas
 drawCircle(skin,w*.032f,Offset(cx-w*.143f,h*.285f));drawCircle(skin,w*.032f,Offset(cx+w*.143f,h*.285f));drawOval(skin,Offset(cx-w*.145f,h*.115f),Size(w*.29f,h*.35f))
 drawOval(highlight,Offset(cx-w*.10f,h*.14f),Size(w*.065f,h*.18f))
 // cabello, con siluetas distintas
 val top=when(p.hairStyle){HairStyle.SHORT->h*.095f;HairStyle.WAVY->h*.06f;HairStyle.CURLY->h*.045f;HairStyle.LONG->h*.055f;HairStyle.BOB->h*.065f;HairStyle.BUN->h*.035f}
 drawOval(hair,Offset(cx-w*.155f,top),Size(w*.31f,h*.17f))
 if(p.hairStyle==HairStyle.LONG){drawRoundRect(hair,Offset(cx-w*.175f,h*.13f),Size(w*.06f,h*.34f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f));drawRoundRect(hair,Offset(cx+w*.115f,h*.13f),Size(w*.06f,h*.34f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))}
 if(p.hairStyle==HairStyle.BOB){drawRoundRect(hair,Offset(cx-w*.17f,h*.14f),Size(w*.055f,h*.22f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f));drawRoundRect(hair,Offset(cx+w*.115f,h*.14f),Size(w*.055f,h*.22f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))}
 if(p.hairStyle==HairStyle.BUN) drawCircle(hair,w*.062f,Offset(cx,h*.065f))
 if(p.hairStyle==HairStyle.CURLY){for(i in -3..3) drawCircle(hair,w*.036f,Offset(cx+i*w*.04f,h*.11f+(kotlin.math.abs(i)%2)*h*.014f))}
 // cejas
 drawRoundRect(hair,Offset(cx-w*.095f,h*.225f),Size(w*.07f,h*.009f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.01f));drawRoundRect(hair,Offset(cx+w*.025f,h*.225f),Size(w*.07f,h*.009f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.01f))
 // ojos
 val erx=when(p.eyeShape){EyeShape.ROUND->w*.027f;EyeShape.ALMOND->w*.034f;EyeShape.SOFT->w*.030f}; val ery=when(p.eyeShape){EyeShape.ROUND->w*.027f;EyeShape.ALMOND->w*.018f;EyeShape.SOFT->w*.021f}
 listOf(cx-w*.06f,cx+w*.06f).forEach{x->drawOval(Color.White,Offset(x-erx,h*.267f-ery),Size(erx*2,ery*2));drawCircle(eye,ery*.64f,Offset(x,h*.267f));drawCircle(Color.Black,ery*.30f,Offset(x,h*.267f));drawCircle(Color.White,ery*.11f,Offset(x-ery*.15f,h*.262f))}
 // nariz y boca
 drawCircle(Color(0xFFD58B70),w*.012f,Offset(cx,h*.315f))
 val lip=when(p.lipColor){LipColor.NATURAL->Color(0xFFC77C72);LipColor.ROSE->Color(0xFFD66F86);LipColor.CORAL->Color(0xFFE27165);LipColor.RED->Color(0xFFC7464C);LipColor.WINE->Color(0xFF873B55)}
 val mw=when(p.mouthStyle){MouthStyle.NATURAL->w*.068f;MouthStyle.SMILE->w*.095f;MouthStyle.FULL->w*.088f}; val mh=if(p.mouthStyle==MouthStyle.FULL) h*.026f else h*.016f
 drawOval(lip,Offset(cx-mw/2,h*.355f),Size(mw,mh));if(p.mouthStyle==MouthStyle.SMILE)drawOval(Color.White,Offset(cx-mw*.33f,h*.356f),Size(mw*.66f,mh*.48f))
 // cuello del uniforme, bolsillo e insignia dental
 val neck=Path().apply{moveTo(cx-w*.075f,h*.51f);lineTo(cx,h*.59f);lineTo(cx+w*.075f,h*.51f);lineTo(cx+w*.035f,h*.50f);lineTo(cx,h*.545f);lineTo(cx-w*.035f,h*.50f);close()};drawPath(neck,Color.White.copy(alpha=.40f))
 drawRoundRect(Color.Black.copy(alpha=.10f),Offset(cx+w*.045f,h*.62f),Size(w*.09f,h*.075f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.01f));drawCircle(Color.White,w*.018f,Offset(cx+w*.09f,h*.645f))
}

private fun mascotName(v:MascotStyle)=v.name.lowercase().replace('_',' ').replaceFirstChar(Char::uppercase)

@Composable fun DentistAvatarCustomizerV51(p:AppPreferences,onChange:(AppPreferences)->Unit,lang:String){
 Text(tr(lang,"Crea tu dentista","Create your dentist"),style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black)
 Text(tr(lang,"Elige cada característica y observa el cambio en tu personaje.","Choose each feature and see it change on your character."),style=MaterialTheme.typography.bodyMedium)
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

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable private fun <T> AvatarChoiceV52(title:String,values:List<T>,selected:T,onSelect:(T)->Unit,label:(T)->String){
 Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(6.dp)){
  Text(title,fontWeight=FontWeight.Bold)
  androidx.compose.foundation.layout.FlowRow(
   modifier=Modifier.fillMaxWidth(),
   horizontalArrangement=Arrangement.spacedBy(8.dp),
   verticalArrangement=Arrangement.spacedBy(6.dp)
  ){
   values.forEach{v->FilterChip(selected==v,{onSelect(v)},{Text(label(v),textAlign=TextAlign.Center)},Modifier.widthIn(min=96.dp))}
  }
 }
}


