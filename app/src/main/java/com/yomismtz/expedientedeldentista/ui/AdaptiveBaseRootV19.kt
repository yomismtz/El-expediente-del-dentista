package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.clinical.AppScreen
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.ui.theme.paletteDisplayName

private data class NavigationStateV40(val screen: AppScreen, val extra: FolderExtraV33?)

@Composable
fun AdaptiveBaseRootV19(preferences:AppPreferences,onPreferencesChanged:(AppPreferences)->Unit,onLanguageChanged:(String)->Unit,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onIntake:(()->Unit)?=null,onSettings:(()->Unit)?=null) {
    val screenState=remember{mutableStateOf(AppScreen.HOME)}
    val extraState=remember{mutableStateOf<FolderExtraV33?>(null)}
    val navigationHistory=remember{mutableStateListOf<NavigationStateV40>()}
    val lang=preferences.languageTag
    fun save(){navigationHistory.add(NavigationStateV40(screenState.value,extraState.value))}
    fun navigate(next:AppScreen){if(extraState.value==null&&next==screenState.value)return;save();screenState.value=next;extraState.value=null}
    fun openExtra(next:FolderExtraV33){if(next==extraState.value)return;save();extraState.value=next}
    fun goBack(){if(navigationHistory.isNotEmpty()){val p=navigationHistory.removeAt(navigationHistory.lastIndex);screenState.value=p.screen;extraState.value=p.extra}else{screenState.value=AppScreen.HOME;extraState.value=null}}
    val backPrevious={goBack()}
    BackHandler(enabled=extraState.value!=null||screenState.value!=AppScreen.HOME){goBack()}
    when(extraState.value){
        FolderExtraV33.SYMPTOMS->VitalsInteractiveV19Screen(lang,backPrevious)
        FolderExtraV33.CALCULATORS->ClinicalCalculatorsV26Screen(lang,backPrevious)
        FolderExtraV33.HISTORY_HUB->HistoryHubV44Screen(lang,{navigate(AppScreen.VITALS)},backPrevious)
        FolderExtraV33.ODONTOGRAM_HUB->OdontogramHubV35Screen(lang,{navigate(it)},backPrevious)
        FolderExtraV33.HEAD_NECK->HeadNeckTeachingV44Screen(lang,backPrevious)
        FolderExtraV33.GENERAL_INSPECTION->GeneralInspectionV44Screen(lang,backPrevious)
        FolderExtraV33.PHYSICAL_HUB->PhysicalExamHubV44Screen(lang,{navigate(AppScreen.VITALS)},backPrevious)
        FolderExtraV33.CAMBRA->CambraInteractiveV39Screen(lang,backPrevious)
        FolderExtraV33.DENTAL_ANOMALIES->DentalAnomaliesPhotoV46Screen(lang,backPrevious)
        FolderExtraV33.ERUPTION_ANOMALIES->EruptionAnomaliesPhotoV46Screen(lang,backPrevious)
        FolderExtraV33.HABITS->HabitsTeachingV40Screen(lang,backPrevious)
        FolderExtraV33.ORTHODONTIC_HISTORY->OrthodonticHistoryV33Screen(lang,backPrevious)
        null->when(screenState.value){
            AppScreen.HOME->CoverV19(preferences){navigate(AppScreen.FOLDER)}
            AppScreen.FOLDER->FolderMenuV40Screen(lang,{navigate(it)},{openExtra(it)},onIntake?:{navigate(AppScreen.INTAKE)},onSettings?:{navigate(AppScreen.SETTINGS)},backPrevious)
            AppScreen.SETTINGS->ResponsiveScreenV17(tr(lang,"Configuración","Settings"),tr(lang,"Idioma, paleta de ave, tipo y tamaño de letra se conservan en la configuración de la app.","Language, bird palette, font and text size are kept in app settings."),backPrevious){}
            AppScreen.IDENTIFICATION->IdentificationTeachingV40Screen(lang,backPrevious)
            AppScreen.HISTORY->HistoryHubV44Screen(lang,{navigate(AppScreen.VITALS)},backPrevious)
            AppScreen.INTAKE->IntakeNoteScreen(lang,session,backPrevious)
            AppScreen.ACTIVITIES->ActivitiesTableV40Screen(lang,backPrevious)
            AppScreen.VITALS->VitalsInteractiveV19Screen(lang,backPrevious)
            AppScreen.ATM->TmjPhotoAtlasV46Screen(lang,backPrevious)
            AppScreen.OCCLUSION->OcclusionPhotoAtlasV46Screen(lang,backPrevious)
            AppScreen.MUCOSA->MucosaPhotoAtlasV46Screen(lang,backPrevious)
            AppScreen.AUXILIARIES->AuxiliariesV20Screen(lang,backPrevious)
            AppScreen.ODONTOGRAM->OdontogramV20Screen(lang,session,onSessionChanged,backPrevious)
            AppScreen.ICDAS->IcdasScreen(lang,session,onSessionChanged,backPrevious)
            AppScreen.CPOD->CpodCeosV40Screen(lang,session,onSessionChanged,backPrevious)
            AppScreen.OLEARY->OlearyScreen(lang,session,onSessionChanged,backPrevious)
            AppScreen.IPC->CpiModifiedV48Screen(lang,session,onSessionChanged,backPrevious)
            AppScreen.IHOS->IhosPersistentV23Screen(lang,session,onSessionChanged,backPrevious)
            AppScreen.PERIODONTOGRAM->PeriodontalTeachingV40Screen(lang,session,onSessionChanged,backPrevious)
            AppScreen.POSTURE->PostureVisualScreen(lang,backPrevious)
            AppScreen.PULPAL,AppScreen.APICAL->EndodonticTeachingV40Screen(lang,backPrevious)
            AppScreen.TREATMENT->TreatmentPlannerV40Screen(lang,session,onSessionChanged,backPrevious)
            AppScreen.SESSIONS->TreatmentSessionsV40Screen(lang,backPrevious)
            AppScreen.ENDO->EndodonticTeachingV40Screen(lang,backPrevious)
            AppScreen.PROSTHETIC->ProstheticResponsiveV17Screen(lang,session,onSessionChanged,backPrevious)
            AppScreen.SURGICAL->SurgicalTeachingV40Screen(lang,backPrevious)
            AppScreen.CONSENT->ConsentTeachingScreen(lang,backPrevious)
            AppScreen.REQUEST->TreatmentRequestGuideV40Screen(lang,backPrevious)
            AppScreen.BUDGET->BudgetGuideV40Screen(lang,backPrevious)
            AppScreen.EVOLUTION->EvolutionExamplesV40Screen(lang,backPrevious)
        }
    }
}

@Composable private fun CoverV19(preferences:AppPreferences,onOpen:()->Unit){
    val lang=preferences.languageTag
    val config=LocalConfiguration.current
    val fontScale=LocalDensity.current.fontScale
    BoxWithConstraints(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.background,MaterialTheme.colorScheme.primaryContainer.copy(alpha=.72f),MaterialTheme.colorScheme.background)))){
        val availableWidth=maxWidth
        val compact=availableWidth<380.dp||fontScale>=1.30f
        val landscape=config.screenWidthDp>config.screenHeightDp
        val mascotSize=when{compact->150.dp;landscape->170.dp;availableWidth>=600.dp->230.dp;else->200.dp}
        Column(Modifier.fillMaxSize().safeDrawingPadding().verticalScroll(rememberScrollState()).padding(horizontal=if(availableWidth>=600.dp)36.dp else VisualSpacingV49.lg,vertical=VisualSpacingV49.md),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(if(compact)VisualSpacingV49.sm else VisualSpacingV49.md)){
            Image(painterResource(R.drawable.ysm_logo),tr(lang,"Logo YSM con ave y expediente dental","YSM bird and dental record logo"),Modifier.size(if(compact)76.dp else if(availableWidth>=600.dp)116.dp else 94.dp),contentScale=ContentScale.Fit)
            Text("YSM Expediente",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Black,textAlign=TextAlign.Center)
            Text(tr(lang,"Bienvenida Doctor","Welcome Doctor"),style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black,textAlign=TextAlign.Center)
            Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),shape=MaterialTheme.shapes.extraLarge,modifier=Modifier.fillMaxWidth().heightIn(min=mascotSize+60.dp),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),elevation=CardDefaults.cardElevation(defaultElevation=2.dp)){
                if(landscape&&availableWidth>=600.dp&&fontScale<1.3f) Row(Modifier.fillMaxWidth().padding(VisualSpacingV49.lg),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(VisualSpacingV49.lg)){
                    MascotImageV54(preferences.mascotStyle,lang,Modifier.size(mascotSize))
                    CoverGuideText(preferences,Modifier.weight(1f))
                } else Column(Modifier.fillMaxWidth().padding(VisualSpacingV49.lg),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(VisualSpacingV49.sm)){
                    MascotImageV54(preferences.mascotStyle,lang,Modifier.size(mascotSize))
                    CoverGuideText(preferences,Modifier.fillMaxWidth())
                }
            }
            Text(tr(lang,"Deja volar tu imaginación y tus conocimientos renacerán","Let your imagination take flight and your knowledge be reborn"),style=MaterialTheme.typography.bodyLarge,fontWeight=FontWeight.SemiBold,textAlign=TextAlign.Center,color=MaterialTheme.colorScheme.secondary)
            Button(onClick=onOpen,modifier=Modifier.fillMaxWidth().heightIn(min=54.dp),shape=MaterialTheme.shapes.medium){Text(tr(lang,"Abrir expediente","Open record"),fontWeight=FontWeight.SemiBold,style=MaterialTheme.typography.titleMedium)}
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable private fun CoverGuideText(preferences:AppPreferences,modifier:Modifier=Modifier){
    val lang=preferences.languageTag
    Column(modifier,horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(5.dp)){
        Text(tr(lang,"Tu mascota","Your pet"),style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Black,textAlign=TextAlign.Center)
        Text(paletteDisplayName(preferences.birdPaletteStyle,lang),style=MaterialTheme.typography.labelLarge,fontWeight=FontWeight.SemiBold,textAlign=TextAlign.Center,color=MaterialTheme.colorScheme.primary)
        Text(tr(lang,"Guía educativa interactiva para aprender el llenado del expediente clínico odontológico.","Interactive educational guide for learning how to complete a dental clinical record."),style=MaterialTheme.typography.bodyMedium,textAlign=TextAlign.Center)
    }
}

private fun clinicianDescription(lang:String,title:ClinicianTitle)=if(lang=="en"){if(title==ClinicianTitle.DOCTORA)"Selected female dentist" else "Selected male dentist"}else{if(title==ClinicianTitle.DOCTORA)"Doctora seleccionada" else "Doctor seleccionado"}
