package com.yomismtz.expedientedeldentista.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
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
@DrawableRes private fun mascotRes(v:MascotStyle)=when(v){MascotStyle.TOUCAN->R.drawable.mascot_toucan;MascotStyle.LOVEBIRD_GREEN->R.drawable.mascot_lovebird_green;MascotStyle.LOVEBIRD_PASTEL->R.drawable.mascot_lovebird_pastel;MascotStyle.PUG->R.drawable.mascot_pug;MascotStyle.POMERANIAN->R.drawable.mascot_pomeranian;MascotStyle.CLOWNFISH->R.drawable.mascot_clownfish;MascotStyle.SHARK->R.drawable.mascot_shark;MascotStyle.RAVEN->R.drawable.mascot_raven;MascotStyle.MANDARIN_DUCK->R.drawable.mascot_mandarin_duck;MascotStyle.FLAMINGO->R.drawable.mascot_flamingo;MascotStyle.MACAW->R.drawable.mascot_macaw;MascotStyle.PERSIAN_CAT->R.drawable.mascot_persian_cat;MascotStyle.WHITE_YELLOW_CAT->R.drawable.mascot_white_yellow_cat;MascotStyle.ELEPHANT->R.drawable.mascot_elephant;MascotStyle.TURTLE->R.drawable.mascot_turtle;MascotStyle.WHITE_RABBIT->R.drawable.mascot_white_rabbit;MascotStyle.IGUANA->R.drawable.mascot_iguana;MascotStyle.COCKATIEL->R.drawable.mascot_lovebird_pastel;MascotStyle.OWL->R.drawable.mascot_raven;MascotStyle.BROWN_HORSE->R.drawable.mascot_pug;MascotStyle.PINTO_HORSE->R.drawable.mascot_pomeranian;MascotStyle.SPIDER->R.drawable.mascot_raven;MascotStyle.DOLPHIN->R.drawable.mascot_shark;MascotStyle.AXOLOTL->R.drawable.mascot_lovebird_pastel;MascotStyle.PENGUIN->R.drawable.mascot_raven;MascotStyle.HAMSTER->R.drawable.mascot_pomeranian;MascotStyle.PHOENIX->R.drawable.mascot_macaw;MascotStyle.FOX->R.drawable.mascot_pomeranian;MascotStyle.RACCOON->R.drawable.mascot_pug;MascotStyle.KOALA->R.drawable.mascot_elephant;MascotStyle.CAPYBARA->R.drawable.mascot_pug}

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
 val skin=when(p.skinTone){SkinTone.LIGHT->Color(0xFFFFD9C5);SkinTone.PEACH->Color(0xFFF2B48F);SkinTone.TAN->Color(0xFFD68B62);SkinTone.BROWN->Color(0xFFA96348);SkinTone.DEEP->Color(0xFF704333)}
 val shadow=Color.Black.copy(alpha=.10f); val highlight=Color.White.copy(alpha=.22f)
 val scrub=when(p.scrubColor){ScrubColor.TURQUOISE->Color(0xFF27AAA1);ScrubColor.BLUE->Color(0xFF3E7FC7);ScrubColor.NAVY->Color(0xFF31406F);ScrubColor.PURPLE->Color(0xFF7A56B5);ScrubColor.LILAC->Color(0xFFA98AD4);ScrubColor.PINK->Color(0xFFE37CA7);ScrubColor.BLACK->Color(0xFF302E34);ScrubColor.WHITE->Color(0xFFECECF0);ScrubColor.MINT->Color(0xFF7CCCB4);ScrubColor.WINE->Color(0xFF843D59)}
 val isWoman=p.clinicianTitle==ClinicianTitle.DOCTORA
 val hair=if(isWoman) Color(0xFF5B392B) else Color(0xFF493127)
 val eye=when(p.eyeColor){EyeColor.BROWN->Color(0xFF5A3426);EyeColor.HAZEL->Color(0xFF8A6A35);EyeColor.GREEN->Color(0xFF4C7A58);EyeColor.BLUE->Color(0xFF4779A8);EyeColor.GRAY->Color(0xFF747982)}
 // sombra suave y cuerpo tipo figura 3D
 drawOval(shadow,Offset(cx-w*.19f,h*.91f),Size(w*.38f,h*.055f))
 val torsoHalf=if(isWoman) w*.165f else w*.205f
 drawRoundRect(scrub,Offset(cx-torsoHalf,h*.50f),Size(torsoHalf*2f,h*.36f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(if(isWoman) w*.095f else w*.065f))
 drawRoundRect(highlight,Offset(cx-torsoHalf+w*.04f,h*.525f),Size(w*.05f,h*.26f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))
 drawRoundRect(scrub,Offset(cx-w*.135f,h*.81f),Size(w*.105f,h*.14f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f));drawRoundRect(scrub,Offset(cx+w*.03f,h*.81f),Size(w*.105f,h*.14f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))
 drawRoundRect(Color(0xFFF4F4F6),Offset(cx-w*.135f,h*.935f),Size(w*.105f,h*.025f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.02f));drawRoundRect(Color(0xFFF4F4F6),Offset(cx+w*.03f,h*.935f),Size(w*.105f,h*.025f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.02f))
 drawRoundRect(skin,Offset(cx-w*.045f,h*.405f),Size(w*.09f,h*.12f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))
 // cabeza y orejas
 val faceHalf=if(isWoman) w*.128f else w*.15f
 drawCircle(skin,w*.032f,Offset(cx-faceHalf,h*.285f));drawCircle(skin,w*.032f,Offset(cx+faceHalf,h*.285f))
 drawOval(skin,Offset(cx-faceHalf,h*.115f),Size(faceHalf*2f,if(isWoman) h*.36f else h*.34f))
 drawOval(highlight,Offset(cx-faceHalf*.68f,h*.14f),Size(w*.06f,h*.18f))
 // cabello, con siluetas distintas
 val top=when(p.hairStyle){HairStyle.SHORT->h*.095f;HairStyle.WAVY->h*.06f;HairStyle.CURLY->h*.045f;HairStyle.LONG->h*.055f;HairStyle.BOB->h*.065f;HairStyle.BUN->h*.035f}
 val hairHalf=if(isWoman) w*.16f else w*.155f
 drawOval(hair,Offset(cx-hairHalf,top),Size(hairHalf*2f,if(p.hairStyle==HairStyle.SHORT) h*.135f else h*.17f))
 if(p.hairStyle==HairStyle.SHORT){drawRoundRect(hair,Offset(cx-hairHalf,h*.13f),Size(w*.035f,h*.10f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.018f));drawRoundRect(hair,Offset(cx+hairHalf-w*.035f,h*.13f),Size(w*.035f,h*.10f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.018f))}
 if(p.hairStyle==HairStyle.WAVY){for(i in -3..3) drawCircle(hair,w*.033f,Offset(cx+i*w*.045f,h*.115f+(kotlin.math.abs(i)%2)*h*.025f));drawCircle(hair,w*.038f,Offset(cx-w*.145f,h*.22f));drawCircle(hair,w*.038f,Offset(cx+w*.145f,h*.22f))}
 if(p.hairStyle==HairStyle.LONG){drawRoundRect(hair,Offset(cx-w*.175f,h*.13f),Size(w*.06f,h*.34f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f));drawRoundRect(hair,Offset(cx+w*.115f,h*.13f),Size(w*.06f,h*.34f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))}
 if(p.hairStyle==HairStyle.BOB){drawRoundRect(hair,Offset(cx-w*.17f,h*.14f),Size(w*.055f,h*.22f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f));drawRoundRect(hair,Offset(cx+w*.115f,h*.14f),Size(w*.055f,h*.22f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.03f))}
 if(p.hairStyle==HairStyle.BUN) drawCircle(hair,w*.062f,Offset(cx,h*.065f))
 if(p.hairStyle==HairStyle.CURLY){for(i in -3..3) drawCircle(hair,w*.036f,Offset(cx+i*w*.04f,h*.11f+(kotlin.math.abs(i)%2)*h*.014f))}
 // cejas y pequeños rasgos diferenciadores del personaje base
 val browH=if(isWoman) h*.006f else h*.010f
 drawRoundRect(hair,Offset(cx-w*.095f,h*.225f),Size(w*.07f,browH),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.01f));drawRoundRect(hair,Offset(cx+w*.025f,h*.225f),Size(w*.07f,browH),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.01f))
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
 Text(tr(lang,"Elige tu mascota","Choose your pet"),style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black)
 Text(tr(lang,"Tu mascota será tu identidad visual. Al seleccionarla, toda la aplicación adopta automáticamente su paleta de colores.","Your pet becomes your visual identity. Selecting it automatically applies its color palette across the app."),style=MaterialTheme.typography.bodyMedium)
 MascotPickerV53(p, onChange, lang)
 NoticeCard(tr(lang,"Cada una de las 30 mascotas tiene una identidad propia. Las que comparten colores similares usan un acento diferenciador para que su paleta siga siendo reconocible.","Each of the 30 pets has its own identity. Pets with similar colors use a distinguishing accent so their palettes remain recognizable."))
}

@Composable
internal fun MascotPickerV53(p:AppPreferences,onChange:(AppPreferences)->Unit,lang:String){
 val values=MascotStyle.entries
 values.chunked(2).forEach { pair ->
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
   pair.forEach { mascot ->
    val selected=p.mascotStyle==mascot
    Card(
     onClick={onChange(p.copy(mascotStyle=mascot,birdPaletteStyle=paletteForMascot(mascot)))},
     modifier=Modifier.weight(1f),
     colors=CardDefaults.cardColors(containerColor=if(selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
     border=BorderStroke(if(selected)3.dp else 1.dp,if(selected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
     shape=MaterialTheme.shapes.large
    ){
     Column(Modifier.fillMaxWidth().padding(10.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(6.dp)){
      Image(painterResource(mascotRes(mascot)),mascotNameV52(mascot,lang),Modifier.fillMaxWidth().height(110.dp),contentScale=ContentScale.Fit)
      Text(mascotNameV52(mascot,lang),fontWeight=FontWeight.Black,textAlign=TextAlign.Center)
      Text(mascotAccentV53(mascot,lang),style=MaterialTheme.typography.labelSmall,textAlign=TextAlign.Center,color=MaterialTheme.colorScheme.onSurfaceVariant)
      if(selected) Text(tr(lang,"Seleccionada ✓","Selected ✓"),color=MaterialTheme.colorScheme.primary,fontWeight=FontWeight.Bold)
     }
    }
   }
   if(pair.size==1) Spacer(Modifier.weight(1f))
  }
 }
}

private fun mascotAccentV53(v:MascotStyle,lang:String)=when(v){
 MascotStyle.TOUCAN->tr(lang,"Negro + amarillo","Black + yellow")
 MascotStyle.LOVEBIRD_GREEN->tr(lang,"Verde + coral","Green + coral")
 MascotStyle.LOVEBIRD_PASTEL->tr(lang,"Pastel + lavanda","Pastel + lavender")
 MascotStyle.PUG->tr(lang,"Arena + carbón","Sand + charcoal")
 MascotStyle.POMERANIAN->tr(lang,"Miel + crema","Honey + cream")
 MascotStyle.CLOWNFISH->tr(lang,"Naranja + azul marino","Orange + navy")
 MascotStyle.SHARK->tr(lang,"Azul acero + hielo","Steel blue + ice")
 MascotStyle.RAVEN->tr(lang,"Carbón + violeta","Charcoal + violet")
 MascotStyle.MANDARIN_DUCK->tr(lang,"Esmeralda + naranja","Emerald + orange")
 MascotStyle.FLAMINGO->tr(lang,"Rosa + coral","Pink + coral")
 MascotStyle.MACAW->tr(lang,"Rojo + azul","Red + blue")
 MascotStyle.PERSIAN_CAT->tr(lang,"Gris + ciruela","Gray + plum")
 MascotStyle.WHITE_YELLOW_CAT->tr(lang,"Blanco + amarillo","White + yellow")
 MascotStyle.ELEPHANT->tr(lang,"Gris + turquesa","Gray + turquoise")
 MascotStyle.TURTLE->tr(lang,"Oliva + aqua","Olive + aqua")
 MascotStyle.WHITE_RABBIT->tr(lang,"Blanco + rosa","White + pink")
 MascotStyle.IGUANA->tr(lang,"Lima + jade","Lime + jade")
 MascotStyle.COCKATIEL->tr(lang,"Gris + amarillo","Gray + yellow")
 MascotStyle.OWL->tr(lang,"Café + ámbar","Brown + amber")
 MascotStyle.BROWN_HORSE->tr(lang,"Castaño + crema","Chestnut + cream")
 MascotStyle.PINTO_HORSE->tr(lang,"Pinto + turquesa","Pinto + turquoise")
 MascotStyle.SPIDER->tr(lang,"Grafito + violeta","Graphite + violet")
 MascotStyle.DOLPHIN->tr(lang,"Azul océano + aqua","Ocean blue + aqua")
 MascotStyle.AXOLOTL->tr(lang,"Rosa + lavanda","Pink + lavender")
 MascotStyle.PENGUIN->tr(lang,"Carbón + azul hielo","Charcoal + ice blue")
 MascotStyle.HAMSTER->tr(lang,"Caramelo + crema","Caramel + cream")
 MascotStyle.PHOENIX->tr(lang,"Rojo fuego + dorado","Fire red + gold")
 MascotStyle.FOX->tr(lang,"Naranja + crema","Orange + cream")
 MascotStyle.RACCOON->tr(lang,"Grafito + plata","Graphite + silver")
 MascotStyle.KOALA->tr(lang,"Gris + lavanda","Gray + lavender")
 MascotStyle.CAPYBARA->tr(lang,"Cacao + menta","Cocoa + mint")
}

private fun mascotNameV52(v:MascotStyle,lang:String)=when(v){
 MascotStyle.TOUCAN->tr(lang,"Tucán","Toucan");MascotStyle.LOVEBIRD_GREEN->tr(lang,"Agaporni verde","Green lovebird");MascotStyle.LOVEBIRD_PASTEL->tr(lang,"Agaporni pastel","Pastel lovebird");MascotStyle.PUG->"Pug";MascotStyle.POMERANIAN->tr(lang,"Pomerania","Pomeranian");MascotStyle.CLOWNFISH->tr(lang,"Pez payaso","Clownfish");MascotStyle.SHARK->tr(lang,"Tiburón","Shark");MascotStyle.RAVEN->tr(lang,"Cuervo","Raven");MascotStyle.MANDARIN_DUCK->tr(lang,"Pato mandarín","Mandarin duck");MascotStyle.FLAMINGO->tr(lang,"Flamenco","Flamingo");MascotStyle.MACAW->tr(lang,"Guacamaya","Macaw");MascotStyle.PERSIAN_CAT->tr(lang,"Gato persa","Persian cat");MascotStyle.WHITE_YELLOW_CAT->tr(lang,"Gato blanco y amarillo","White/yellow cat");MascotStyle.ELEPHANT->tr(lang,"Elefante","Elephant");MascotStyle.TURTLE->tr(lang,"Tortuga","Turtle");MascotStyle.WHITE_RABBIT->tr(lang,"Conejo blanco","White rabbit");MascotStyle.IGUANA->"Iguana";MascotStyle.COCKATIEL->tr(lang,"Ninfa","Cockatiel");MascotStyle.OWL->tr(lang,"Búho","Owl");MascotStyle.BROWN_HORSE->tr(lang,"Caballo café","Brown horse");MascotStyle.PINTO_HORSE->tr(lang,"Caballo pinto","Pinto horse");MascotStyle.SPIDER->tr(lang,"Araña","Spider");MascotStyle.DOLPHIN->tr(lang,"Delfín","Dolphin");MascotStyle.AXOLOTL->tr(lang,"Ajolote","Axolotl");MascotStyle.PENGUIN->tr(lang,"Pingüino","Penguin");MascotStyle.HAMSTER->tr(lang,"Hámster","Hamster");MascotStyle.PHOENIX->tr(lang,"Fénix","Phoenix");MascotStyle.FOX->tr(lang,"Zorro","Fox");MascotStyle.RACCOON->tr(lang,"Mapache","Raccoon");MascotStyle.KOALA->"Koala";MascotStyle.CAPYBARA->tr(lang,"Capibara","Capybara")
}

@Composable private fun <T> AvatarChoiceV52(title:String,values:List<T>,selected:T,onSelect:(T)->Unit,label:(T)->String){
 Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(6.dp)){
  Text(title,fontWeight=FontWeight.Bold)
  values.chunked(2).forEach { pair ->
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
    pair.forEach{v->FilterChip(selected==v,{onSelect(v)},{Text(label(v),textAlign=TextAlign.Center)},Modifier.weight(1f))}
    if(pair.size==1) Spacer(Modifier.weight(1f))
   }
  }
 }
}


