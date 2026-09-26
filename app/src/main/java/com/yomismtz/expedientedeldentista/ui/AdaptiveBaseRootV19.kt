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
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Surface
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    // 1 · Expediente clínico
    // Conserva aquí el interrogatorio y todos los antecedentes de la historia clínica.
    TabV19(AppScreen.HISTORY,"🩺","Historia clínica completa","Complete clinical history",0),
    TabV19(AppScreen.INTAKE,"📋","Nota de ingreso","Intake note",0),

    // 2 · Exploración clínica
    // Hallazgos del paciente: signos vitales, postura, ATM, tejidos blandos y oclusión.
    TabV19(AppScreen.VITALS,"❤️","Signos vitales","Vital signs",1),
    TabV19(AppScreen.POSTURE,"🧍","Postura y evaluación extraoral","Posture and extraoral assessment",1),
    TabV19(AppScreen.ATM,"◉","ATM y trastornos temporomandibulares","TMJ and temporomandibular disorders",1),
    TabV19(AppScreen.MUCOSA,"👄","Examen peribucal, intrabucal y mucosas","Perioral, intraoral and mucosal exam",1),
    TabV19(AppScreen.OCCLUSION,"↔","Oclusión","Occlusion",1),

    // 3 · Análisis dentales
    // El concentrador mantiene juntos odontograma, caries, higiene y periodoncia.
    TabV19(AppScreen.ODONTOGRAM,"🦷","Odontograma e índices dentales","Odontogram and dental indices",2),
    TabV19(AppScreen.PULPAL,"⚡","Análisis pulpar y periapical","Pulpal and periapical analysis",2),

    // 4 · Auxiliares de diagnóstico
    // El concentrador existente conserva imagenología, radiografías, modelos,
    // cefalometría, fotografía, laboratorio, histología, microbiología y CAMBRA.
    TabV19(AppScreen.AUXILIARIES,"🔬","Auxiliares de diagnóstico","Diagnostic aids",3),

    // 5 · Diagnóstico y plan de tratamiento
    TabV19(AppScreen.TREATMENT,"📝","Diagnóstico y tratamiento por diente","Diagnosis and treatment by tooth",4),
    TabV19(AppScreen.SESSIONS,"🗓","Plan de tratamiento por sesiones","Treatment plan by sessions",4),

    // 6 · Tratamiento y fichas clínicas
    TabV19(AppScreen.ENDO,"⚡","Ficha endodóntica","Endodontic sheet",5),
    TabV19(AppScreen.PROSTHETIC,"👑","Ficha protésica","Prosthetic sheet",5),
    TabV19(AppScreen.SURGICAL,"✚","Ficha quirúrgica","Surgical sheet",5),
    TabV19(AppScreen.PERIODONTOGRAM,"📈","Periodontograma","Periodontogram",5),
    TabV19(AppScreen.ATM,"◉","Ficha de trastornos temporomandibulares","Temporomandibular disorders sheet",5),
    TabV19(AppScreen.EMERGENCY,"🚨","Ficha de Urgencias Odontológicas","Dental Emergency Sheet",5),
    TabV19(AppScreen.CLINICAL_PHOTO,"📷","Ficha de Fotografía Clínica","Clinical Photography Sheet",5),
    TabV19(AppScreen.PERIODONTAL_FOLLOWUP,"📊","Ficha de Seguimiento Periodontal","Periodontal Follow-up Sheet",5),
    TabV19(AppScreen.IMPLANTOLOGY,"🔩","Ficha de Implantología","Implantology Sheet",5),
    TabV19(AppScreen.DENTOMAXILLARY_ORTHOPEDICS,"↔","Ficha de Ortopedia Dentomaxilar","Dentomaxillary Orthopedics Sheet",5),
    TabV19(AppScreen.PEDIATRIC_DENTISTRY,"🧒","Ficha de Odontopediatría","Pediatric Dentistry Sheet",5),
    TabV19(AppScreen.CARIES_RISK,"🛡️","Ficha de Caries y Riesgo de Caries","Caries and Caries-Risk Sheet",5),

    // 7 · Herramientas administrativas
    TabV19(AppScreen.ACTIVITIES,"✍","Autorización de actividades","Activity authorization",6),
    TabV19(AppScreen.REQUEST,"📨","Solicitud de tratamiento","Treatment request",6),
    TabV19(AppScreen.CONSENT,"📑","Consentimiento informado","Informed consent",6),
    TabV19(AppScreen.BUDGET,"💰","Presupuesto","Budget",6),
    TabV19(AppScreen.EVOLUTION,"📄","Notas de evolución","Progress notes",6),

    // 8 · Herramientas clínicas
    TabV19(AppScreen.CALCULATORS,"🧮","Calculadoras clínicas","Clinical calculators",7),
    TabV19(AppScreen.SYSTEMIC_PROTOCOLS,"📚","Protocolos sistémicos y atención especial","Systemic and special-care protocols",7)
)

@Composable
fun AdaptiveBaseRootV19(
    preferences:AppPreferences,
    onPreferencesChanged:(AppPreferences)->Unit,
    onLanguageChanged:(String)->Unit,
    session:EducationalSession,
    onSessionChanged:(EducationalSession)->Unit
) {
    var screenName by rememberSaveable { mutableStateOf(AppScreen.HOME.name) }
    val screen = AppScreen.valueOf(screenName)
    var completedCount by rememberSaveable { mutableStateOf(0) }
    var celebrate by rememberSaveable { mutableStateOf(false) }
    var selectedGroup by rememberSaveable { mutableStateOf(0) }
    var historyNames by rememberSaveable { mutableStateOf(listOf<String>()) }
    val lang=preferences.languageTag

    fun navigate(next: AppScreen) {
        if (next == screen) return
        if(screen != AppScreen.HOME && screen != AppScreen.FOLDER) completedCount++
        if(completedCount >= 8) celebrate=true
        historyNames = historyNames + screen.name
        screenName = next.name
    }

    fun goBack() {
        if (historyNames.isNotEmpty()) {
            screenName = historyNames.last()
            historyNames = historyNames.dropLast(1)
        } else {
            screenName = AppScreen.HOME.name
        }
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
        AppScreen.FOLDER -> FolderV19(lang,{ group ->
            // Auxiliares es ya un concentrador de cuatro módulos; entrar directo evita
            // repetir una sección intermedia con una sola tarjeta.
            if(group==3) navigate(AppScreen.AUXILIARIES)
            else { selectedGroup=group; navigate(AppScreen.SECTION) }
        },backPrevious)
        AppScreen.SECTION -> SectionV19(lang,selectedGroup,{navigate(it)},backPrevious)
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
        AppScreen.HISTORY_ORAL_EXAM -> MucosaInteractiveV19Screen(lang,backPrevious)
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
        AppScreen.EMERGENCY -> EmergencyDentalSheetV43(lang,backPrevious)
        AppScreen.CLINICAL_PHOTO -> ClinicalPhotographySheetV43(lang,backPrevious)
        AppScreen.PERIODONTAL_FOLLOWUP -> PeriodontalFollowupV44(lang,backPrevious)
        AppScreen.IMPLANTOLOGY -> ImplantologySheetV44(lang,backPrevious)
        AppScreen.DENTOMAXILLARY_ORTHOPEDICS -> DentomaxillaryOrthopedicsV44(lang,backPrevious)
        AppScreen.PEDIATRIC_DENTISTRY -> PediatricDentistryV45(lang,backPrevious)
        AppScreen.CARIES_RISK -> CariesRiskV45(lang,backPrevious)
        AppScreen.CONSENT -> ConsentTeachingScreen(lang,backPrevious)
        AppScreen.REQUEST -> TreatmentRequestTeachingV41(lang,backPrevious)
        AppScreen.BUDGET -> BudgetTeachingV41(lang,backPrevious)
        AppScreen.EVOLUTION -> EvolutionScreen(lang,session,backPrevious)
    }
    AnimatedVisibility(celebrate) {
        Surface(Modifier.align(Alignment.BottomCenter).safeDrawingPadding().padding(16.dp),color=MaterialTheme.colorScheme.secondaryContainer,shape=MaterialTheme.shapes.large,shadowElevation=4.dp){
            Column(Modifier.padding(14.dp),horizontalAlignment=Alignment.CenterHorizontally){Text("🎓",style=MaterialTheme.typography.headlineMedium);Text(tr(lang,"Buen avance: varias secciones revisadas","Good progress: several sections reviewed"),fontWeight=FontWeight.Bold);Button(onClick={celebrate=false}){Text(tr(lang,"Continuar","Continue"))}}
        }
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
            Text(tr(lang,"Deja volar tu imaginación y tus conocimientos renacerán","Let your imagination take flight and your knowledge be reborn"),style=MaterialTheme.typography.bodyLarge,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center,color=Color(0xFF173B63))
            Spacer(Modifier.height(12.dp))
            Surface(color=MaterialTheme.colorScheme.surface.copy(alpha=.92f),shape=MaterialTheme.shapes.large){
                Text(tr(lang,"App educativa de ayuda diagnóstica. Toda orientación, cálculo, interpretación y posibilidad diagnóstica debe comprobarse con historia clínica, exploración, estudios apropiados, fuentes clínicas vigentes y supervisión profesional. No sustituye el diagnóstico ni el criterio clínico.","Educational diagnostic-support app. Every suggestion, calculation, interpretation and diagnostic possibility must be verified with history, examination, appropriate studies, current clinical sources and professional supervision. It does not replace diagnosis or clinical judgment."),Modifier.padding(12.dp),style=MaterialTheme.typography.bodyMedium,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
            }
            Spacer(Modifier.height(18.dp))
            Button(onClick=onOpen,modifier=Modifier.fillMaxWidth()) { Text("📖 ${tr(lang,"Abrir expediente","Open record")}",fontWeight=FontWeight.Black) }
        }
    }
}

@Composable
private fun FolderV19(lang:String,onOpenGroup:(Int)->Unit,onClose:()->Unit) {
    ResponsiveScreenV17(
        "YSM Expediente",
        tr(lang,"Elige uno de los 8 rubros para abrirlo.","Choose one of the 8 sections to open it."),
        onClose
    ) { profile ->
        val groups=listOf(
            Triple("📋",tr(lang,"1 · Expediente clínico","1 · Clinical record"),Color(0xFFD7EEF8)),
            Triple("🩺",tr(lang,"2 · Exploración clínica","2 · Clinical examination"),Color(0xFFDDF3E4)),
            Triple("🦷",tr(lang,"3 · Análisis dentales","3 · Dental analyses"),Color(0xFFFFE6C9)),
            Triple("🔬",tr(lang,"4 · Auxiliares de diagnóstico","4 · Diagnostic aids"),Color(0xFFE9E0F8)),
            Triple("🧠",tr(lang,"5 · Diagnóstico y plan de tratamiento","5 · Diagnosis and treatment plan"),Color(0xFFFFE1E8)),
            Triple("🩹",tr(lang,"6 · Tratamiento y fichas clínicas","6 · Treatment and clinical sheets"),Color(0xFFDFF0EC)),
            Triple("🗂️",tr(lang,"7 · Herramientas administrativas","7 · Administrative tools"),Color(0xFFFFF0C9)),
            Triple("🧮",tr(lang,"8 · Herramientas clínicas","8 · Clinical tools"),Color(0xFFE1E8FF))
        )
        ResponsiveSectionV17(tr(lang,"Secciones del expediente","Record sections")) {
            AdaptiveGridV17(groups.size,when {
                profile.largeSystemText -> 1
                profile.width==ScreenWidthV17.COMPACT -> 2
                profile.width==ScreenWidthV17.MEDIUM -> 2
                else -> 4
            }) { i ->
                val item=groups[i]
                Card(
                    onClick={onOpenGroup(i)},
                    modifier=Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(containerColor=item.third),
                    border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.25f)),
                    shape=RoundedCornerShape(22.dp)
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(horizontal=14.dp,vertical=20.dp),
                        horizontalAlignment=Alignment.CenterHorizontally,
                        verticalArrangement=Arrangement.spacedBy(8.dp)
                    ) {
                        Text(item.first,style=MaterialTheme.typography.headlineMedium)
                        Text(item.second,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium,textAlign=TextAlign.Center)
                        Text(tr(lang,"Toca para abrir","Tap to open"),style=MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionV19(lang:String,group:Int,onNavigate:(AppScreen)->Unit,onBack:()->Unit) {
    val names=listOf(
        tr(lang,"1 · Expediente clínico","1 · Clinical record"),
        tr(lang,"2 · Exploración clínica","2 · Clinical examination"),
        tr(lang,"3 · Análisis dentales","3 · Dental analyses"),
        tr(lang,"4 · Auxiliares de diagnóstico","4 · Diagnostic aids"),
        tr(lang,"5 · Diagnóstico y plan de tratamiento","5 · Diagnosis and treatment plan"),
        tr(lang,"6 · Tratamiento y fichas clínicas","6 · Treatment and clinical sheets"),
        tr(lang,"7 · Herramientas administrativas","7 · Administrative tools"),
        tr(lang,"8 · Herramientas clínicas","8 · Clinical tools")
    )
    var query by rememberSaveable(group) { mutableStateOf("") }
    ResponsiveScreenV17(
        names[group],
        tr(lang,"Selecciona el apartado que deseas abrir.","Select the item you want to open."),
        onBack
    ) { profile ->
        OutlinedTextField(
            value=query,
            onValueChange={query=it},
            modifier=Modifier.fillMaxWidth(),
            singleLine=true,
            label={Text("🔎 "+tr(lang,"Buscar en esta sección","Search this section"))}
        )
        val q=query.trim().lowercase()
        val items=tabsV19.filter {
            it.group==group && (q.isBlank() || it.es.lowercase().contains(q) || it.en.lowercase().contains(q))
        }
        ResponsiveSectionV17(names[group]) {
            AdaptiveGridV17(items.size.coerceAtLeast(1),when {
                profile.largeSystemText -> 1
                profile.width==ScreenWidthV17.COMPACT -> 2
                profile.width==ScreenWidthV17.MEDIUM -> 2
                else -> 3
            }) { i ->
                if(items.isEmpty()) {
                    Text(tr(lang,"Sin coincidencias","No matches"))
                } else {
                    val tab=items[i]
                    Card(
                        onClick={onNavigate(tab.screen)},
                        modifier=Modifier.fillMaxWidth(),
                        colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer.copy(alpha=.55f)),
                        border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.30f)),
                        shape=RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(14.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                            Text("${tab.icon} ${if(lang=="en")tab.en else tab.es}",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
                            Text(tr(lang,"Toca para abrir","Tap to open"),style=MaterialTheme.typography.bodyMedium)
                            CompletionBadgeV22(done=false,inProgress=false,lang=lang)
                        }
                    }
                }
            }
        }
    }
}
