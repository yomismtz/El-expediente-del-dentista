package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    TabV19(AppScreen.IDENTIFICATION,"👤","Ficha de identificación","Identification",0),
    TabV19(AppScreen.TREATMENT,"📝","Diagnóstico y tratamiento","Diagnosis and treatment",0),
    TabV19(AppScreen.HISTORY,"🩺","Historia clínica","Medical history",0),
    TabV19(AppScreen.SESSIONS,"🗓","Tratamiento por sesiones","Treatment by sessions",0),
    TabV19(AppScreen.ODONTOGRAM,"🦷","Análisis del odontograma","Odontogram analysis",0),
    TabV19(AppScreen.ENDO,"⚡","Ficha endodóntica","Endodontic sheet",0),
    TabV19(AppScreen.AUXILIARIES,"🧪","Auxiliares de diagnóstico","Diagnostic auxiliaries",0),
    TabV19(AppScreen.PROSTHETIC,"👑","Ficha protésica","Prosthetic sheet",0),
    TabV19(AppScreen.BUDGET,"💰","Presupuesto","Budget",0),
    TabV19(AppScreen.PERIODONTOGRAM,"📈","Ficha periodontal / periodontograma","Periodontal sheet / periodontogram",0),
    TabV19(AppScreen.CONSENT,"✍","Consentimiento informado","Informed consent",0),
    TabV19(AppScreen.SURGICAL,"✚","Ficha quirúrgica","Surgical sheet",0),
    TabV19(AppScreen.REQUEST,"📨","Solicitud de tratamiento","Treatment request",0),
    TabV19(AppScreen.ATM,"◉","Ficha de trastornos temporomandibulares","Temporomandibular disorders",0),
    TabV19(AppScreen.EVOLUTION,"📄","Notas de evolución","Progress notes",0),
    TabV19(AppScreen.VITALS,"❤️","Signos vitales y glucosa capilar","Vital signs and capillary glucose",0),
    TabV19(AppScreen.INTAKE,"📋","Nota de ingreso","Intake note",0),
    TabV19(AppScreen.SYSTEMIC_PROTOCOLS,"📚","Protocolos para enfermedades sistémicas","Systemic disease protocols",0),

    TabV19(AppScreen.ICDAS,"🔎","ICDAS","ICDAS",1),
    TabV19(AppScreen.CPOD,"➕","CPOD / ceod","DMFT / dmft",1),
    TabV19(AppScreen.OLEARY,"🔴","O’Leary","O’Leary",1),
    TabV19(AppScreen.IPC,"6️⃣","IPC","CPI",1),
    TabV19(AppScreen.IHOS,"🪥","IHOS","OHI-S",1),
    TabV19(AppScreen.POSTURE,"🧍","Postura craneocervical","Craniocervical posture",1),
    TabV19(AppScreen.OCCLUSION,"↔","Oclusión y clasificación de Angle","Occlusion and Angle classification",1),
    TabV19(AppScreen.MUCOSA,"👄","Mucosas orales","Oral mucosa",1),
    TabV19(AppScreen.PULPAL,"⚡","Diagnóstico pulpar y periapical","Pulpal and periapical diagnosis",1),

    TabV19(AppScreen.AUXILIARIES,"🧮","Calculadoras y auxiliares ortodóncicos","Calculators and orthodontic auxiliaries",2),
    TabV19(AppScreen.PROSTHETIC,"📐","Kennedy / Applegate / Seibert / PPR","Kennedy / Applegate / Seibert / RPD",2),
    TabV19(AppScreen.ENDO,"🦷","Herramientas endodónticas","Endodontic tools",2)
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

    when(screen) {
        AppScreen.HOME -> CoverV19(lang){navigate(AppScreen.FOLDER)}
        AppScreen.FOLDER -> FolderV19(lang,{navigate(it)},backPrevious)
        AppScreen.SETTINGS -> ResponsiveScreenV17(tr(lang,"Configuración","Settings"),tr(lang,"Usa el botón de Configuración de la barra superior.","Use Settings in the top bar."),backPrevious){ }
        AppScreen.IDENTIFICATION -> IdentificationScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.HISTORY -> PathologicalHistory37Screen(lang,session,onSessionChanged,{navigate(AppScreen.SYSTEMIC_PROTOCOLS)},backPrevious)
        AppScreen.SYSTEMIC_PROTOCOLS -> SystemicProtocols37Screen(lang,backPrevious)
        AppScreen.INTAKE -> IntakeNoteScreen(lang,session,backPrevious)
        AppScreen.ACTIVITIES -> ActivitiesScreen(lang,backPrevious)
        AppScreen.VITALS -> VitalsInteractiveV19Screen(lang,backPrevious)
        AppScreen.ATM -> AtmScreen(lang,backPrevious)
        AppScreen.OCCLUSION -> OcclusionInteractiveV19Screen(lang,backPrevious)
        AppScreen.MUCOSA -> MucosaInteractiveV19Screen(lang,backPrevious)
        AppScreen.AUXILIARIES -> AuxiliariesV20Screen(lang,backPrevious)
        AppScreen.ODONTOGRAM -> OdontogramV20Screen(lang,session,onSessionChanged,backPrevious)
        AppScreen.ICDAS -> IcdasScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.CPOD -> CpodInteractiveV19Screen(lang,session,onSessionChanged,backPrevious)
        AppScreen.OLEARY -> OlearyScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.IPC -> IpcResponsiveV17Screen(lang,session,onSessionChanged,backPrevious)
        AppScreen.IHOS -> IhosResponsiveV17Screen(lang,session,onSessionChanged,backPrevious)
        AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.POSTURE -> PostureVisualScreen(lang,backPrevious)
        AppScreen.PULPAL,AppScreen.APICAL -> PulpalPeriapicalInteractiveV2Screen(lang,session,onSessionChanged,{navigate(AppScreen.ENDO)},backPrevious)
        AppScreen.TREATMENT -> TreatmentScreen(lang,session,onSessionChanged,backPrevious)
        AppScreen.SESSIONS -> TreatmentBySessionsScreen(lang,backPrevious)
        AppScreen.ENDO -> EndodonticInteractiveV2Screen(lang,session,{navigate(AppScreen.PULPAL)},{navigate(AppScreen.PULPAL)},backPrevious)
        AppScreen.PROSTHETIC -> ProstheticResponsiveV17Screen(lang,backPrevious)
        AppScreen.SURGICAL -> SurgicalSheetScreen(lang,backPrevious)
        AppScreen.CONSENT -> ConsentTeachingScreen(lang,backPrevious)
        AppScreen.REQUEST -> SimpleEducationalSheet(lang,"Solicitud de tratamiento","Treatment request","Aprende para qué sirve y qué debe identificar claramente.","Learn its purpose and what it should clearly identify.",listOf("Servicio solicitado","Motivo","Área u órgano dentario","Prioridad / referencia","Responsable y supervisión"),listOf("Requested service","Reason","Area or tooth","Priority / referral","Responsible clinician and supervision"),backPrevious)
        AppScreen.BUDGET -> SimpleEducationalSheet(lang,"Presupuesto","Budget","Aprende su estructura administrativa sin registrar cobros reales.","Learn its administrative structure without recording real payments.",listOf("Procedimiento","Cantidad","Costo unitario","Subtotal","Total","Laboratorio cuando proceda"),listOf("Procedure","Quantity","Unit cost","Subtotal","Total","Laboratory when applicable"),backPrevious)
        AppScreen.EVOLUTION -> EvolutionScreen(lang,session,backPrevious)
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
    ResponsiveScreenV17("YSM Expediente",tr(lang,"Elige una sección. La barra superior queda reservada y nunca tapa el contenido.","Choose a section. The top bar has reserved space and never covers content."),onClose) { profile ->
        val names=listOf(tr(lang,"Apartados del expediente","Record sections"),tr(lang,"Exámenes y referencias","Exams and references"),tr(lang,"Calculadoras y herramientas","Calculators and tools"))
        ResponsiveSectionV17(tr(lang,"Secciones del expediente","Record sections")) {
            AdaptiveGridV17(3,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 3) { i ->
                FilterChip(group==i,{group=i},{Text(names[i])},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(names[group]) {
            val items=tabsV19.filter{it.group==group}
            AdaptiveGridV17(items.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2) { i ->
                val tab=items[i]
                Card(onClick={onNavigate(tab.screen)},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer.copy(alpha=.55f)),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.30f)),shape=RoundedCornerShape(16.dp)) {
                    Column(Modifier.fillMaxWidth().padding(13.dp),verticalArrangement=Arrangement.spacedBy(3.dp)) {
                        Text("${tab.icon} ${if(lang=="en")tab.en else tab.es}",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
                        Text(tr(lang,"Toca para abrir","Tap to open"),style=MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
