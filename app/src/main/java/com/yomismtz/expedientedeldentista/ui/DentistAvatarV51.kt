package com.yomismtz.expedientedeldentista.ui
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.settings.*

private fun skinColor(v:SkinTone)=when(v){SkinTone.LIGHT->Color(0xFFFFDFC4);SkinTone.PEACH->Color(0xFFF4B184);SkinTone.TAN->Color(0xFFD58B5B);SkinTone.BROWN->Color(0xFFA96643);SkinTone.DEEP->Color(0xFF68402F)}
private fun eyeColor(v:EyeColor)=when(v){EyeColor.BROWN->Color(0xFF5B3825);EyeColor.HAZEL->Color(0xFF8A6B32);EyeColor.GREEN->Color(0xFF527A55);EyeColor.BLUE->Color(0xFF4B79A8);EyeColor.GRAY->Color(0xFF737B83)}
private fun scrubColor(v:ScrubColor)=when(v){ScrubColor.TURQUOISE->Color(0xFF27A7A2);ScrubColor.BLUE->Color(0xFF477FD1);ScrubColor.NAVY->Color(0xFF263B67);ScrubColor.PURPLE->Color(0xFF7350B5);ScrubColor.LILAC->Color(0xFFA986D4);ScrubColor.PINK->Color(0xFFE989A6);ScrubColor.BLACK->Color(0xFF27272B);ScrubColor.WHITE->Color(0xFFF4F4F6);ScrubColor.MINT->Color(0xFF77CDB9);ScrubColor.WINE->Color(0xFF8B334C)}
private fun lipColor(v:LipColor)=when(v){LipColor.NATURAL->Color(0xFFC97870);LipColor.ROSE->Color(0xFFD85E78);LipColor.CORAL->Color(0xFFE76F61);LipColor.RED->Color(0xFFB93643);LipColor.WINE->Color(0xFF7C3048)}
private fun mascotEmoji(v:MascotStyle)=when(v){MascotStyle.TOUCAN->"🦜";MascotStyle.LOVEBIRD_GREEN->"🦜";MascotStyle.LOVEBIRD_PASTEL->"🦜";MascotStyle.PUG->"🐶";MascotStyle.POMERANIAN->"🐕";MascotStyle.CLOWNFISH->"🐠";MascotStyle.SHARK->"🦈";MascotStyle.RAVEN->"🐦‍⬛";MascotStyle.MANDARIN_DUCK->"🦆";MascotStyle.FLAMINGO->"🦩";MascotStyle.MACAW->"🦜";MascotStyle.PERSIAN_CAT->"🐱";MascotStyle.WHITE_YELLOW_CAT->"🐈";MascotStyle.ELEPHANT->"🐘";MascotStyle.TURTLE->"🐢";MascotStyle.WHITE_RABBIT->"🐇";MascotStyle.IGUANA->"🦎"}

@Composable fun DentistAvatarPreviewV51(p:AppPreferences,modifier:Modifier=Modifier){
 Card(modifier=modifier,shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){
  Box(Modifier.fillMaxWidth().height(280.dp).padding(12.dp),contentAlignment=Alignment.Center){
   Canvas(Modifier.fillMaxSize()){
    val cx=size.width/2f; val face=skinColor(p.skinTone); val hair=Color(0xFF4A2C22); val uniform=scrubColor(p.scrubColor)
    drawOval(uniform,Offset(cx-size.width*.22f,size.height*.60f),Size(size.width*.44f,size.height*.42f))
    when(p.hairStyle){HairStyle.SHORT->drawArc(hair,180f,180f,true,Offset(cx-size.width*.15f,size.height*.13f),Size(size.width*.30f,size.height*.24f));HairStyle.BUN->{drawCircle(hair,size.width*.07f,Offset(cx,size.height*.12f));drawOval(hair,Offset(cx-size.width*.16f,size.height*.13f),Size(size.width*.32f,size.height*.28f))};HairStyle.LONG->drawOval(hair,Offset(cx-size.width*.18f,size.height*.12f),Size(size.width*.36f,size.height*.49f));else->drawOval(hair,Offset(cx-size.width*.17f,size.height*.12f),Size(size.width*.34f,size.height*.35f))}
    drawOval(face,Offset(cx-size.width*.135f,size.height*.19f),Size(size.width*.27f,size.height*.37f))
    val ey=size.height*.34f;val sep=size.width*.055f;val er=if(p.eyeShape==EyeShape.ROUND)size.width*.025f else size.width*.022f
    drawCircle(Color.White,er*1.7f,Offset(cx-sep,ey));drawCircle(Color.White,er*1.7f,Offset(cx+sep,ey));drawCircle(eyeColor(p.eyeColor),er,Offset(cx-sep,ey));drawCircle(eyeColor(p.eyeColor),er,Offset(cx+sep,ey));drawCircle(Color.Black,er*.45f,Offset(cx-sep,ey));drawCircle(Color.Black,er*.45f,Offset(cx+sep,ey))
    drawArc(lipColor(p.lipColor),0f,180f,false,Offset(cx-size.width*.045f,size.height*.44f),Size(size.width*.09f,size.height*.055f),strokeWidth=if(p.mouthStyle==MouthStyle.FULL)8f else 5f)
    val neck=Path().apply{moveTo(cx-size.width*.055f,size.height*.55f);lineTo(cx+size.width*.055f,size.height*.55f);lineTo(cx+size.width*.09f,size.height*.66f);lineTo(cx-size.width*.09f,size.height*.66f);close()};drawPath(neck,face)
    drawLine(Color(0xFF444A54),Offset(cx-size.width*.08f,size.height*.63f),Offset(cx-size.width*.025f,size.height*.78f),5f);drawLine(Color(0xFF444A54),Offset(cx+size.width*.08f,size.height*.63f),Offset(cx+size.width*.025f,size.height*.78f),5f)
   }
   Text(mascotEmoji(p.mascotStyle),style=MaterialTheme.typography.displayMedium,modifier=Modifier.align(Alignment.CenterEnd).padding(end=20.dp))
  }
 }
}
@Composable fun DentistAvatarCustomizerV51(p:AppPreferences,onChange:(AppPreferences)->Unit,lang:String){
 Text(tr(lang,"Crea tu dentista","Create your dentist"),style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black);DentistAvatarPreviewV51(p,Modifier.fillMaxWidth())
 AvatarChoiceV51(tr(lang,"1. Sexo / personaje base","1. Sex / base character"),ClinicianTitle.entries,p.clinicianTitle,{onChange(p.copy(clinicianTitle=it))}){if(it==ClinicianTitle.DOCTORA)tr(lang,"Mujer","Woman") else tr(lang,"Hombre","Man")}
 AvatarChoiceV51(tr(lang,"2. Color de piel","2. Skin tone"),SkinTone.entries,p.skinTone,{onChange(p.copy(skinTone=it))}){it.name.lowercase().replaceFirstChar(Char::uppercase)}
 AvatarChoiceV51(tr(lang,"3. Estilo de cabello","3. Hair style"),HairStyle.entries,p.hairStyle,{onChange(p.copy(hairStyle=it))}){it.name.lowercase().replace('_',' ').replaceFirstChar(Char::uppercase)}
 AvatarChoiceV51(tr(lang,"4. Forma de ojos","4. Eye shape"),EyeShape.entries,p.eyeShape,{onChange(p.copy(eyeShape=it))}){it.name.lowercase().replaceFirstChar(Char::uppercase)}
 AvatarChoiceV51(tr(lang,"Color de ojos","Eye color"),EyeColor.entries,p.eyeColor,{onChange(p.copy(eyeColor=it))}){it.name.lowercase().replaceFirstChar(Char::uppercase)}
 AvatarChoiceV51(tr(lang,"5. Boca","5. Mouth"),MouthStyle.entries,p.mouthStyle,{onChange(p.copy(mouthStyle=it))}){it.name.lowercase().replaceFirstChar(Char::uppercase)}
 AvatarChoiceV51(tr(lang,"Color de labios","Lip color"),LipColor.entries,p.lipColor,{onChange(p.copy(lipColor=it))}){it.name.lowercase().replaceFirstChar(Char::uppercase)}
 AvatarChoiceV51(tr(lang,"6. Color del quirúrgico","6. Scrub color"),ScrubColor.entries,p.scrubColor,{onChange(p.copy(scrubColor=it))}){it.name.lowercase().replaceFirstChar(Char::uppercase)}
 AvatarChoiceV51(tr(lang,"7. Mascota · define la paleta de la app","7. Pet · sets the app palette"),MascotStyle.entries,p.mascotStyle,{onChange(p.copy(mascotStyle=it, birdPaletteStyle=paletteForMascot(it)))}){mascotEmoji(it)+" "+it.name.lowercase().replace('_',' ').replaceFirstChar(Char::uppercase)}
 NoticeCard(tr(lang,"Tu personaje y mascota se guardan en este dispositivo. La mascota seleccionada también aplica automáticamente una paleta inspirada en sus colores a toda la app. Es un avatar educativo y no representa a un profesional real.","Your character and pet are saved on this device. The selected pet also automatically applies a palette inspired by its colors across the app. It is an educational avatar and does not represent a real professional."))
}
@Composable private fun <T> AvatarChoiceV51(title:String,values:List<T>,selected:T,onSelect:(T)->Unit,label:(T)->String){Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(6.dp)){Text(title,fontWeight=FontWeight.Bold);values.chunked(3).forEach{row->Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){row.forEach{v->FilterChip(selected==v,{onSelect(v)},{Text(label(v),textAlign=TextAlign.Center)},Modifier.weight(1f))};repeat(3-row.size){Spacer(Modifier.weight(1f))}}}}}
