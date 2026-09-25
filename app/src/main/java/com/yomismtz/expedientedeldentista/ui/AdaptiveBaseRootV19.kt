package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.clinical.AppScreen
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences

private data class TabV19(val screen:AppScreen,val icon:String,val es:String,val en:String,val group:Int)

private val tabsV19=listOf(
    TabV19(AppScreen.ACTIVITIES,"✍","Autorización de actividades","Activity authorization",0),
    TabV19(AppScreen.TREATMENT,"📝","Diagnóstico y tratamiento","Diagnosis and treatment",0),
    TabV19(AppScreen.SESSIONS,"🗓","Tratamiento por sesiones","Treatment by sessions",0),
    TabV19(AppScreen.OLEARY,"🔴","O’Leary","O’Leary",0),
    TabV19(AppScreen.CALCULATORS,"🧮","Calculadoras · anestésico y medicamentos pediátricos","Calculators · anesthetic and pediatric medication",0),
    TabV19(AppScreen.SYSTEMIC_PROTOCOLS,"📚","Protocolos para enfermedades sistémicas","Systemic disease protocols",0),

    TabV19(AppScreen.HISTORY,"🩺","Historia clínica","Clinical history",1),
    TabV19(AppScreen.AUXILIARIES,"🧪","Auxiliares de diagnóstico","Diagnostic aids",1),
    TabV19(AppScreen.ODONTOGRAM,"🦷","Análisis dentales","Dental analyses",1),
    TabV19(AppScreen.CONSENT,"✍","Consentimiento informado","Informed consent",1),
    TabV19(AppScreen.REQUEST,"📨","Solicitud de tratamiento","Treatment request",1),
    TabV19(AppScreen.BUDGET,"💰","Presupuesto","Budget",1),
    TabV19(AppScreen.EVOLUTION,"📄","Notas de evolución","Progress notes",1),

    TabV19(AppScreen.ENDO,"⚡","Ficha endodóntica","Endodontic sheet",2),
    TabV19(AppScreen.PROSTHETIC,"👑","Ficha protésica","Prosthetic sheet",2),
    TabV19(AppScreen.PERIODONTOGRAM,"📈","Ficha periodontal","Periodontal sheet",2),
    TabV19(AppScreen.SURGICAL,"✚","Ficha quirúrgica","Surgical sheet",2),
    TabV19(AppScreen.ATM,"◉","Ficha de diagnóstico de trastornos temporomandibulares","Temporomandibular disorder diagnostic sheet",2)
)

@Composable
fun AdaptiveBaseRootV19(
    preferences:AppPreferences,
    onPreferencesChanged:(AppPreferences)->Unit,
    onLanguageChanged:(String)->Unit,
    session:EducationalSession,
    onSessionChanged:(EducationalSession)->Unit
) {
    var screen by remember { mutableStateOf(AppScreen.HOME) }
    val history = remember { mutableStateListOf<AppScreen>() }
    val lang=preferences.languageTag

    fun navigate(next: AppScreen) {
        if (next == screen) return
        history.add(screen)
        screen = next
    }

    fun goBack() {
        screen = if (history.isNotEmpty()) history.removeAt(history.lastIndex) else AppScreen.HOME
    }

    val backPrevious = { goBack() }

    BackHandler(enabled=screen!=AppScreen.HOME) { goBack() }

    Box(
        Modifier
            .fillMaxSize()
            .edgeSwipeBackV21 { if (screen != AppScreen.HOME) goBack() }
    ) {
    when(screen) {
        AppScreen.HOME -> CoverV19(lang){navigate(AppScreen.FOLDER)}
        AppScreen.FOLDER -> FolderV19(lang,{navigate(it)},backPrevious)
        AppScreen.SETTINGS -> ResponsiveScreenV17(tr(lang,"Configuración","Settings"),tr(lang,"Usa el botón de Configuración de la barra superior.","Use Settings in the top bar."),backPrevious){ }
        AppScreen.IDENTIFICATION -> IdentificationScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.HISTORY -> ClinicalHistoryHubV38(lang,{navigate(it)},backPrevious)
        AppScreen.HISTORY_IDENTIFICATION -> IdentificationScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.HISTORY_REASON -> HistoryReasonV38(lang,backPrevious)
        AppScreen.HISTORY_HEREDITARY -> HistoryHereditaryV38(lang,backPrevious)
        AppScreen.HISTORY_NONPATH -> HistoryNonPathV38(lang,backPrevious)
        AppScreen.HISTORY_GYNECO -> HistoryGynecoV38(lang,backPrevious)
        AppScreen.HISTORY_PATH -> PathologicalHistory37Screen(lang,session,onSessionChanged,{navigate(AppScreen.SYSTEMIC_PROTOCOLS)},backPrevious)
        AppScreen.HISTORY_SURGICAL_TRAUMA -> HistorySurgicalTraumaV38(lang,backPrevious)
        AppScreen.HISTORY_PHYSICAL -> HistoryPhysicalV38(lang,backPrevious)
        AppScreen.HISTORY_ORTHO -> HistoryOrthoV38(lang,backPrevious)
        AppScreen.HISTORY_DENTAL_ALTERATIONS -> HistoryDentalAlterationsV38(lang,backPrevious)
        AppScreen.HISTORY_HABITS -> HistoryHabitsV38(lang,backPrevious)
        AppScreen.HISTORY_ORAL_EXAM -> HistoryOralExamV38(lang,backPrevious)
        AppScreen.SYSTEMIC_PROTOCOLS -> SystemicProtocols37Screen(lang,backPrevious)
        AppScreen.INTAKE -> IntakeNoteScreen(lang,session,backPrevious)
        AppScreen.ACTIVITIES -> ActivitiesScreen(lang,backPrevious)
        AppScreen.VITALS -> VitalsInteractiveV19Screen(lang,backPrevious)
        AppScreen.ATM -> AtmScreen(lang,backPrevious)
        AppScreen.OCCLUSION -> OcclusionInteractiveV19Screen(lang,backPrevious)
        AppScreen.MUCOSA -> MucosaInteractiveV19Screen(lang,backPrevious)
        AppScreen.AUXILIARIES -> AuxiliariesV20Screen(lang,backPrevious)
        AppScreen.CALCULATORS -> DentalCalculatorsV40Screen(lang,backPrevious)
        AppScreen.ODONTOGRAM -> DentalAnalysisHubV41(lang,session,onSessionChanged,backPrevious)
        AppScreen.ICDAS -> IcdasScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.CPOD -> CpodInteractiveV19Screen(lang,session,onSessionChanged,backPrevious)
        AppScreen.OLEARY -> OlearyScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.IPC -> IpcResponsiveV17Screen(lang,session,onSessionChanged,backPrevious)
        AppScreen.IHOS -> IhosResponsiveV17Screen(lang,session,onSessionChanged,backPrevious)
        AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.POSTURE -> PostureVisualScreen(lang,backPrevious)
        AppScreen.PULPAL,AppScreen.APICAL -> PulpalPeriapicalInteractiveV2Screen(lang,session,onSessionChanged,{navigate(AppScreen.ENDO)},backPrevious)
        AppScreen.TREATMENT -> TreatmentScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.SESSIONS -> TreatmentBySessionsScreen(lang,session,backPrevious)
        AppScreen.ENDO -> EndodonticInteractiveV2Screen(lang,session,{navigate(AppScreen.PULPAL)},{navigate(AppScreen.PULPAL)},backPrevious)
        AppScreen.PROSTHETIC -> ProstheticResponsiveV17Screen(lang,backPrevious)
        AppScreen.SURGICAL -> SurgicalSheetScreen(lang,backPrevious)
        AppScreen.CONSENT -> ConsentTeachingScreen(lang,backPrevious)
        AppScreen.REQUEST -> TreatmentRequestTeachingV41(lang,backPrevious)
        AppScreen.BUDGET -> BudgetTeachingV41(lang,backPrevious)
        AppScreen.EVOLUTION -> EvolutionScreen(lang,session,backPrevious)
    }
    }
}

@Composable
private fun CoverV19(lang:String,onOpen:()->Unit) {
    BoxWithConstraints(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary,MaterialTheme.colorScheme.primaryContainer,MaterialTheme.colorScheme.background)))) {
        val compact=maxWidth<360.dp || LocalDensity.current.fontScale>=1.30f
        Column(Modifier.fillMaxSize().safeDrawingPadding().padding(horizontal=if(compact)16.dp else 28.dp,vertical=20.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Center) {
            Image(painterResource(R.drawable.ysm_logo),tr(lang,"Logo YSM con ave y expediente dental","YSM bird and dental record logo"),Modifier.size(if(compact)118.dp else 158.dp),contentScale=ContentScale.Fit)
            Spacer(Modifier.height(10.dp))
            Text("YSM Expediente",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Black,textAlign=TextAlign.Center)
            Text(tr(lang,"El expediente del dentista","The dentist's record"),style=MaterialTheme.typography.titleLarge,textAlign=TextAlign.Center)
            Text(tr(lang,"Deja volar tu imaginación y tus conocimientos renacerán","Let your imagination take flight and your knowledge be reborn"),style=MaterialTheme.typography.bodyLarge,fontWeight=FontWeight.SemiBold,textAlign=TextAlign.Center,color=MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(18.dp))
            Button(onClick=onOpen,modifier=Modifier.fillMaxWidth()) { Text("📖 ${tr(lang,"Abrir expediente","Open record")}",fontWeight=FontWeight.Black) }
        }
    }
}

@Composable
private fun FolderV19(lang:String,onNavigate:(AppScreen)->Unit,onClose:()->Unit) {
    var group by remember { mutableStateOf(0) }
    var query by remember { mutableStateOf("") }
    ResponsiveScreenV17("YSM Expediente",tr(lang,"Elige una sección. La barra superior queda reservada y nunca tapa el contenido.","Choose a section. The top bar has reserved space and never covers content."),onClose) { profile ->
        val names=listOf(tr(lang,"Acciones y herramientas","Actions and tools"),tr(lang,"Expediente clínico","Clinical record"),tr(lang,"Fichas","Sheets"))
        OutlinedTextField(value=query,onValueChange={query=it},modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text("🔎 "+tr(lang,"Buscar en el expediente","Search record"))},placeholder={Text(tr(lang,"Ej. bruxismo, CPOD, mucosa, presión arterial","e.g. bruxism, DMFT, mucosa, blood pressure"))})
        if(query.isNotBlank()) {
            val q=query.trim().lowercase()
            val special=listOf(
                Triple(AppScreen.HISTORY_HABITS,"🦷",tr(lang,"Hábitos · bruxismo","Habits · bruxism")),
                Triple(AppScreen.CPOD,"➕","CPOD / ceod"), Triple(AppScreen.MUCOSA,"👄",tr(lang,"Mucosa oral","Oral mucosa")),
                Triple(AppScreen.VITALS,"❤️",tr(lang,"Signos vitales · presión arterial","Vital signs · blood pressure")),
                Triple(AppScreen.OCCLUSION,"↔",tr(lang,"Oclusión","Occlusion")), Triple(AppScreen.ATM,"◉","ATM"), Triple(AppScreen.PERIODONTOGRAM,"📈",tr(lang,"Periodoncia","Periodontics")))
            val results=special.filter{it.third.lowercase().contains(q)||(q.contains("brux")&&it.first==AppScreen.HISTORY_HABITS)||(q.contains("pres")&&it.first==AppScreen.VITALS)||(q.contains("cpod")&&it.first==AppScreen.CPOD)}
            ResponsiveSectionV17(tr(lang,"Resultados","Results")) { AdaptiveGridV17(results.size.coerceAtLeast(1),if(profile.width==ScreenWidthV17.EXPANDED)3 else 2){i-> if(results.isEmpty()) Text(tr(lang,"Sin coincidencias","No matches")) else { val r=results[i]; Card(onClick={onNavigate(r.first)},modifier=Modifier.fillMaxWidth(),shape=MaterialTheme.shapes.medium){Text("${r.second} ${r.third}",Modifier.padding(12.dp),fontWeight=FontWeight.Bold)} } } }
        }
        ResponsiveSectionV17(tr(lang,"Secciones del expediente","Record sections")) {
            AdaptiveGridV17(3,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 3) { i ->
                FilterChip(group==i,{group=i},{Text(names[i])},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(names[group]) {
            val items=tabsV19.filter{it.group==group}
            AdaptiveGridV17(items.size,when { profile.largeSystemText -> 1; profile.width==ScreenWidthV17.COMPACT -> 2; profile.width==ScreenWidthV17.MEDIUM -> 2; else -> 3 }) { i ->
                val tab=items[i]
                val cardColor by animateColorAsState(MaterialTheme.colorScheme.primaryContainer.copy(alpha=.55f),animationSpec=tween(220),label="menuCard")
                Card(onClick={onNavigate(tab.screen)},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=cardColor),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.30f)),shape=RoundedCornerShape(16.dp)) {
                    Column(Modifier.fillMaxWidth().padding(13.dp),verticalArrangement=Arrangement.spacedBy(3.dp)) {
                        Text("${tab.icon} ${if(lang=="en")tab.en else tab.es}",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
                        Text(tr(lang,"Toca para abrir","Tap to open"),style=MaterialTheme.typography.bodyMedium)
                        CompletionBadgeV22(done=false,inProgress=false,lang=lang)
                    }
                }
            }
        }
    }
}
