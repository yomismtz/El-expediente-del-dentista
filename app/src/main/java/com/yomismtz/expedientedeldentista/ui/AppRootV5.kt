package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.clinical.AppScreen
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.PaletteStyle

private val YLavender = Color(0xFFE9DDF5)
private val YLilac = Color(0xFFD3BCE9)
private val YPurple = Color(0xFF7447A3)
private val YDeep = Color(0xFF43235F)
private val YMint = Color(0xFF66D6C7)
private val YTurquoise = Color(0xFF2EB9B1)
private val YMetal = Color(0xFFB8B1C0)
private val YPaper = Color(0xFFFFFCFF)
private val YInk = Color(0xFF321943)

@Composable
fun AppRootV5(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var screen by remember { mutableStateOf(AppScreen.HOME) }

    if (!preferences.onboardingComplete) {
        OnboardingV5(preferences, onPreferencesChanged, onLanguageChanged) {
            onPreferencesChanged(preferences.copy(onboardingComplete = true))
        }
        BackHandler(enabled = true) { }
        return
    }

    BackHandler(enabled = true) {
        screen = when (screen) {
            AppScreen.HOME -> AppScreen.HOME
            AppScreen.FOLDER -> AppScreen.HOME
            else -> AppScreen.FOLDER
        }
    }

    val folderBack = { screen = AppScreen.FOLDER }

    PageTurnV5(screen) { target ->
        when (target) {
            AppScreen.HOME -> CoverV5(preferences.languageTag, { screen = AppScreen.FOLDER }, { screen = AppScreen.SETTINGS })
            AppScreen.FOLDER -> FolderV5(preferences.languageTag, { screen = it }, { screen = AppScreen.HOME }, { screen = AppScreen.SETTINGS })
            AppScreen.SETTINGS -> SettingsV5(preferences, onPreferencesChanged, onLanguageChanged, { onSessionChanged(EducationalSession()) }, folderBack)
            AppScreen.IDENTIFICATION -> IdentificationScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY_IDENTIFICATION, AppScreen.HISTORY_REASON, AppScreen.HISTORY_HEREDITARY, AppScreen.HISTORY_NONPATH, AppScreen.HISTORY_GYNECO, AppScreen.HISTORY_PATH, AppScreen.HISTORY_SURGICAL_TRAUMA, AppScreen.HISTORY_PHYSICAL, AppScreen.HISTORY_ORTHO, AppScreen.HISTORY_DENTAL_ALTERATIONS, AppScreen.HISTORY_HABITS, AppScreen.HISTORY_ORAL_EXAM -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.SYSTEMIC_PROTOCOLS -> SystemicProtocols37Screen(preferences.languageTag, folderBack)
            AppScreen.INTAKE -> IntakeNoteScreen(preferences.languageTag, session, folderBack)
            AppScreen.ACTIVITIES -> ActivitiesScreen(preferences.languageTag, folderBack)
            AppScreen.VITALS -> VitalsInteractiveScreen(preferences.languageTag, folderBack)
            AppScreen.ATM -> AtmScreen(preferences.languageTag, folderBack)
            AppScreen.OCCLUSION -> OcclusionScreen(preferences.languageTag, folderBack)
            AppScreen.MUCOSA -> MucosaInteractiveV2Screen(preferences.languageTag, folderBack)
            AppScreen.AUXILIARIES -> AuxiliariesInteractiveScreen(preferences.languageTag, folderBack)
            AppScreen.ODONTOGRAM -> OdontogramQuadrantsScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.ICDAS -> IcdasScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.CPOD -> CpodScreen(preferences.languageTag, session, folderBack)
            AppScreen.OLEARY -> OlearyScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.IPC -> IpcInteractiveV2Screen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.IHOS -> IhosInteractiveV2Screen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.POSTURE -> PostureVisualScreen(preferences.languageTag, folderBack)
            AppScreen.PULPAL -> PulpalScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.APICAL -> ApicalScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.TREATMENT -> TreatmentScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.SESSIONS -> TreatmentBySessionsScreen(preferences.languageTag, folderBack)
            AppScreen.ENDO -> EndodonticSheetScreen(preferences.languageTag, session, { screen = AppScreen.PULPAL }, { screen = AppScreen.APICAL }, folderBack)
            AppScreen.PROSTHETIC -> ProstheticSheetScreen(preferences.languageTag, folderBack)
            AppScreen.SURGICAL -> SurgicalSheetScreen(preferences.languageTag, folderBack)
            AppScreen.CONSENT -> ConsentTeachingScreen(preferences.languageTag, folderBack)
            AppScreen.REQUEST -> SimpleEducationalSheet(preferences.languageTag,"Solicitud de tratamiento","Treatment request","Aprende para qué sirve y qué debe identificar claramente.","Learn its purpose and what it should clearly identify.",listOf("Servicio solicitado","Motivo","Área u órgano dentario","Prioridad / referencia","Responsable y supervisión"),listOf("Requested service","Reason","Area or tooth","Priority / referral","Responsible clinician and supervision"),folderBack)
            AppScreen.BUDGET -> SimpleEducationalSheet(preferences.languageTag,"Presupuesto","Budget","Aprende su estructura administrativa sin registrar cobros reales.","Learn its administrative structure without recording real payments.",listOf("Procedimiento o concepto","Cantidad","Costo unitario","Subtotal","Total","Servicios externos/laboratorio cuando procedan"),listOf("Procedure or concept","Quantity","Unit cost","Subtotal","Total","External/laboratory services when applicable"),folderBack)
            AppScreen.EVOLUTION -> EvolutionScreen(preferences.languageTag, session, folderBack)
        }
    }
}

@Composable
private fun PageTurnV5(target: AppScreen, content: @Composable (AppScreen) -> Unit) {
    var current by remember { mutableStateOf(target) }
    var previous by remember { mutableStateOf<AppScreen?>(null) }
    val progress = remember { Animatable(1f) }
    LaunchedEffect(target) {
        if (target != current) {
            previous = current
            current = target
            progress.snapTo(0f)
            progress.animateTo(1f, tween(520, easing = FastOutSlowInEasing))
            previous = null
        }
    }
    Box(Modifier.fillMaxSize().background(Color(0xFFF4EDF8))) {
        previous?.let { old ->
            Box(Modifier.fillMaxSize().graphicsLayer {
                rotationY = -92f * progress.value
                transformOrigin = TransformOrigin(1f, .5f)
                cameraDistance = 24f
                alpha = (1f - progress.value * .45f).coerceIn(0f,1f)
            }) { content(old) }
        }
        Box(Modifier.fillMaxSize().graphicsLayer {
            rotationY = 92f * (1f - progress.value)
            transformOrigin = TransformOrigin(0f,.5f)
            cameraDistance = 24f
            alpha = progress.value.coerceIn(0f,1f)
        }) { content(current) }
    }
}

@Composable
private fun OnboardingV5(preferences: AppPreferences,onPreferencesChanged:(AppPreferences)->Unit,onLanguageChanged:(String)->Unit,onContinue:()->Unit) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().background(Brush.verticalGradient(listOf(YDeep,YPurple))).padding(24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(R.drawable.ysm_logo),null,Modifier.size(150.dp))
        Text("YSM Expediente",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Black,color=Color.White)
        Text(tr(preferences.languageTag,"Educación odontológica interactiva","Interactive dental education"),color=YMint,fontWeight=FontWeight.Bold)
        Card(colors=CardDefaults.cardColors(containerColor=Color.White.copy(alpha=.94f))) {
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
                Text(tr(preferences.languageTag,"Idioma","Language"),fontWeight=FontWeight.Bold)
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    FilterChip(preferences.languageTag=="es",{onPreferencesChanged(preferences.copy(languageTag="es"));onLanguageChanged("es")},{Text("Español")})
                    FilterChip(preferences.languageTag=="en",{onPreferencesChanged(preferences.copy(languageTag="en"));onLanguageChanged("en")},{Text("English")})
                }
                Text(tr(preferences.languageTag,"Doctor / Doctora","Professional title"),fontWeight=FontWeight.Bold)
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    FilterChip(preferences.clinicianTitle==ClinicianTitle.DOCTOR,{onPreferencesChanged(preferences.copy(clinicianTitle=ClinicianTitle.DOCTOR))},{Text("Doctor")})
                    FilterChip(preferences.clinicianTitle==ClinicianTitle.DOCTORA,{onPreferencesChanged(preferences.copy(clinicianTitle=ClinicianTitle.DOCTORA))},{Text("Doctora")})
                }
            }
        }
        Button(onClick=onContinue,modifier=Modifier.fillMaxWidth()){Text(tr(preferences.languageTag,"Continuar","Continue"))}
    }
}

@Composable
private fun CoverV5(lang:String,onOpen:()->Unit,onSettings:()->Unit) {
    Box(Modifier.fillMaxSize().background(YDeep)) {
        Image(painterResource(R.drawable.folder_cover_ysm),tr(lang,"Portada YSM","YSM cover"),Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
        Surface(Modifier.fillMaxSize(),color=Color(0x33000000)){}
        OutlinedButton(onClick=onSettings,modifier=Modifier.align(Alignment.TopEnd).safeDrawingPadding().padding(14.dp)){Text("⚙",color=YMint)}
        Column(Modifier.align(Alignment.Center).safeDrawingPadding().padding(horizontal=28.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(10.dp)) {
            Image(painterResource(R.drawable.ysm_logo),null,Modifier.size(150.dp))
            Text("YSM Expediente",style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Black,color=Color.White,textAlign=TextAlign.Center)
            Text(tr(lang,"El expediente del dentista","The dentist's record"),style=MaterialTheme.typography.titleLarge,color=Color.White,textAlign=TextAlign.Center)
            Text(tr(lang,"Educación odontológica interactiva","Interactive dental education"),color=YMint,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
            Spacer(Modifier.height(18.dp))
            Text(tr(lang,"PULSA PARA INICIAR","TAP TO START"),color=YLavender,fontWeight=FontWeight.Bold)
            Button(onClick=onOpen,modifier=Modifier.fillMaxWidth().height(64.dp),shape=RoundedCornerShape(24.dp)) {
                Text("📖  ${tr(lang,"Abrir expediente","Open record")}",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Black)
            }
            Text(tr(lang,"Aprender · Practicar · Transformar","Learn · Practice · Transform"),color=YMint,fontWeight=FontWeight.SemiBold)
        }
    }
}

private data class RootTab(val screen:AppScreen,val icon:String,val es:String,val en:String,val group:Int)
private val leftRootTabs=listOf(
    RootTab(AppScreen.IDENTIFICATION,"▤","Ficha","ID",0),RootTab(AppScreen.HISTORY,"●","Anamnesis","History",0),RootTab(AppScreen.INTAKE,"✚","Ingreso","Intake",0),RootTab(AppScreen.MUCOSA,"◡","Mucosas","Mucosa",0),RootTab(AppScreen.OCCLUSION,"◇","Oclusión","Occlusion",0),RootTab(AppScreen.VITALS,"♥","Vitales","Vitals",0),RootTab(AppScreen.AUXILIARIES,"⚗","Auxiliares","Aids",0)
)
private val rightRootTabs=listOf(
    RootTab(AppScreen.ODONTOGRAM,"🦷","Odontograma","Odontogram",1),RootTab(AppScreen.ICDAS,"▣","ICDAS","ICDAS",1),RootTab(AppScreen.OLEARY,"⌁","O’Leary","O’Leary",1),RootTab(AppScreen.IPC,"Ⅵ","IPC","CPI",1),RootTab(AppScreen.IHOS,"▥","IHOS","OHI-S",1),RootTab(AppScreen.PERIODONTOGRAM,"⌇","Periodonto","Periodontal",1),RootTab(AppScreen.PULPAL,"⚡","Pulpar","Pulpal",1),RootTab(AppScreen.APICAL,"◎","Periapical","Apical",1),RootTab(AppScreen.ENDO,"│","Endo","Endo",2),RootTab(AppScreen.PROSTHETIC,"⌒","Prótesis","Prosthetics",2),RootTab(AppScreen.SURGICAL,"✦","Cirugía","Surgery",2),RootTab(AppScreen.EVOLUTION,"▤","Evolución","Progress",2)
)

@Composable
private fun FolderV5(lang:String,onNavigate:(AppScreen)->Unit,onClose:()->Unit,onSettings:()->Unit) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().background(Brush.verticalGradient(listOf(YLavender,Color(0xFFF8F4FB),YLilac)))) {
        Row(Modifier.fillMaxWidth().padding(6.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween) {
            OutlinedButton(onClick=onClose){Text("‹ ${tr(lang,"Portada","Cover")}")}
            Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                Image(painterResource(R.drawable.ysm_logo),null,Modifier.size(40.dp)); Text("YSM Expediente",fontWeight=FontWeight.Black,color=YDeep)
            }
            OutlinedButton(onClick=onSettings){Text("⚙")}
        }
        Row(Modifier.weight(1f).fillMaxWidth().padding(horizontal=4.dp,vertical=3.dp)) {
            RootSideTabs(lang,leftRootTabs,onNavigate,Modifier.width(52.dp).fillMaxHeight())
            Spacer(Modifier.width(3.dp))
            ActivityPaperV5(lang,onNavigate,Modifier.weight(1f))
            Surface(Modifier.width(7.dp).fillMaxHeight(),color=YMetal){}
            IntakePaperV5(lang,onNavigate,Modifier.weight(1f))
            Spacer(Modifier.width(3.dp))
            RootSideTabs(lang,rightRootTabs,onNavigate,Modifier.width(56.dp).fillMaxHeight())
        }
    }
}

@Composable
private fun RootSideTabs(lang:String,tabs:List<RootTab>,onNavigate:(AppScreen)->Unit,modifier:Modifier=Modifier) {
    Column(modifier.verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(5.dp)) {
        tabs.forEach { tab ->
            val bg=when(tab.group){0->YLavender;1->YPurple;else->YDeep}; val fg=if(tab.group==0)YInk else Color.White
            Card(onClick={onNavigate(tab.screen)},modifier=Modifier.fillMaxWidth().height(58.dp),shape=RoundedCornerShape(12.dp),colors=CardDefaults.cardColors(containerColor=bg),border=BorderStroke(1.dp,if(tab.group==2)YMint else YMetal.copy(alpha=.45f))) {
                Column(Modifier.fillMaxSize().padding(3.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Center){Text(tab.icon,color=fg,fontSize=14.sp);Text(if(lang=="en")tab.en else tab.es,color=fg,fontSize=8.sp,lineHeight=9.sp,textAlign=TextAlign.Center,fontWeight=FontWeight.Bold)}
            }
        }
    }
}

@Composable
private fun ActivityPaperV5(lang:String,onNavigate:(AppScreen)->Unit,modifier:Modifier=Modifier) {
    Card(onClick={onNavigate(AppScreen.ACTIVITIES)},modifier=modifier.fillMaxHeight(),shape=RoundedCornerShape(12.dp),colors=CardDefaults.cardColors(containerColor=YPaper),border=BorderStroke(1.dp,YMetal)) {
        Column(Modifier.fillMaxSize().padding(8.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
            Text(tr(lang,"REGISTRO DE ACTIVIDADES","ACTIVITY RECORD"),fontWeight=FontWeight.Black,color=YDeep,fontSize=12.sp,textAlign=TextAlign.Center,modifier=Modifier.fillMaxWidth())
            Text(tr(lang,"Alumno / operador","Student / operator"),fontSize=8.sp,fontWeight=FontWeight.Bold)
            repeat(9){i->Surface(Modifier.fillMaxWidth().height(23.dp),color=if(i%2==0)Color(0xFFF8F3FB) else Color.White,border=BorderStroke(.5.dp,YLilac)) { Text("${i+1}   __________________",fontSize=7.sp,modifier=Modifier.padding(4.dp)) }}
            Spacer(Modifier.weight(1f)); Text(tr(lang,"Toca la hoja para aprender a llenarla.","Tap to learn how to complete it."),fontSize=8.sp,color=YTurquoise,textAlign=TextAlign.Center,modifier=Modifier.fillMaxWidth())
        }
    }
}

private val intakeLinks=listOf(
    Triple("Identificación","Identification",AppScreen.IDENTIFICATION),Triple("ASA y antecedentes","ASA and history",AppScreen.HISTORY),Triple("Medicamentos","Medications",AppScreen.HISTORY),Triple("ATM y músculos","TMJ and muscles",AppScreen.ATM),Triple("Oclusión","Occlusion",AppScreen.OCCLUSION),Triple("Mucosas","Mucosa",AppScreen.MUCOSA),Triple("CPOD / ceod","DMFT / dmft",AppScreen.CPOD),Triple("Periodontal","Periodontal",AppScreen.PERIODONTOGRAM),Triple("Pulpar / periapical","Pulpal / apical",AppScreen.PULPAL),Triple("Prótesis","Prosthetics",AppScreen.PROSTHETIC)
)

@Composable
private fun IntakePaperV5(lang:String,onNavigate:(AppScreen)->Unit,modifier:Modifier=Modifier) {
    Card(modifier=modifier.fillMaxHeight(),shape=RoundedCornerShape(12.dp),colors=CardDefaults.cardColors(containerColor=YPaper),border=BorderStroke(1.dp,YMetal)) {
        Column(Modifier.fillMaxSize().padding(8.dp)) {
            Text(tr(lang,"NOTA DE INGRESO","INTAKE NOTE"),fontWeight=FontWeight.Black,color=YDeep,fontSize=12.sp,textAlign=TextAlign.Center,modifier=Modifier.fillMaxWidth())
            Text(tr(lang,"Toca cada apartado para abrir su guía.","Tap a section to open its guide."),fontSize=8.sp,color=YTurquoise,modifier=Modifier.padding(vertical=5.dp))
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                intakeLinks.forEach { item -> Card(onClick={onNavigate(item.third)},colors=CardDefaults.cardColors(containerColor=YLavender.copy(alpha=.36f)),shape=RoundedCornerShape(8.dp),border=BorderStroke(.5.dp,YLilac)) { Row(Modifier.fillMaxWidth().padding(6.dp),verticalAlignment=Alignment.CenterVertically){Text(if(lang=="en")item.second else item.first,fontSize=8.sp,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text("›",color=YTurquoise)} } }
            }
        }
    }
}

@Composable
private fun SettingsV5(preferences:AppPreferences,onPreferencesChanged:(AppPreferences)->Unit,onLanguageChanged:(String)->Unit,onReset:()->Unit,onBack:()->Unit) {
    val lang=preferences.languageTag
    Column(Modifier.fillMaxSize().safeDrawingPadding().background(Color(0xFFF8F4FB)).padding(18.dp).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        ScreenHeader(tr(lang,"Configuración","Settings"),onBack)
        SectionCard(tr(lang,"Idioma","Language")){Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(lang=="es",{onPreferencesChanged(preferences.copy(languageTag="es"));onLanguageChanged("es")},{Text("Español")});FilterChip(lang=="en",{onPreferencesChanged(preferences.copy(languageTag="en"));onLanguageChanged("en")},{Text("English")})}}
        SectionCard(tr(lang,"Doctor / Doctora","Professional title")){Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(preferences.clinicianTitle==ClinicianTitle.DOCTOR,{onPreferencesChanged(preferences.copy(clinicianTitle=ClinicianTitle.DOCTOR))},{Text("Doctor")});FilterChip(preferences.clinicianTitle==ClinicianTitle.DOCTORA,{onPreferencesChanged(preferences.copy(clinicianTitle=ClinicianTitle.DOCTORA))},{Text("Doctora")})}}
        SectionCard(tr(lang,"Paleta","Palette")){PaletteStyle.entries.forEach{style->FilterChip(preferences.paletteStyle==style,{onPreferencesChanged(preferences.copy(paletteStyle=style))},{Text(style.name.replace('_',' '))},modifier=Modifier.padding(end=4.dp,bottom=4.dp))}}
        SectionCard(tr(lang,"Tipografía","Typography")){FontStyle.entries.forEach{style->FilterChip(preferences.fontStyle==style,{onPreferencesChanged(preferences.copy(fontStyle=style))},{Text(style.name)},modifier=Modifier.padding(end=4.dp,bottom=4.dp))}}
        OutlinedButton(onClick=onReset,modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Limpiar marcas didácticas","Clear teaching marks"))}
    }
}
