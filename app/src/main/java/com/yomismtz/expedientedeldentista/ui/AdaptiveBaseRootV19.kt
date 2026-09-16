package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.clinical.AppScreen
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences

private data class NavigationStateV40(val screen: AppScreen, val extra: FolderExtraV33?)

@Composable
fun AdaptiveBaseRootV19(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    val screenState = remember { mutableStateOf(AppScreen.HOME) }
    val extraState = remember { mutableStateOf<FolderExtraV33?>(null) }
    val navigationHistory = remember { mutableStateListOf<NavigationStateV40>() }
    val lang = preferences.languageTag

    fun save() { navigationHistory.add(NavigationStateV40(screenState.value, extraState.value)) }
    fun navigate(next: AppScreen) {
        if (extraState.value == null && next == screenState.value) return
        save(); screenState.value = next; extraState.value = null
    }
    fun openExtra(next: FolderExtraV33) {
        if (next == extraState.value) return
        save(); extraState.value = next
    }
    fun goBack() {
        if (navigationHistory.isNotEmpty()) {
            val p = navigationHistory.removeAt(navigationHistory.lastIndex)
            screenState.value = p.screen; extraState.value = p.extra
        } else {
            screenState.value = AppScreen.HOME; extraState.value = null
        }
    }
    val backPrevious = { goBack() }
    BackHandler(enabled = extraState.value != null || screenState.value != AppScreen.HOME) { goBack() }

    when (extraState.value) {
        FolderExtraV33.SYMPTOMS -> VitalsInteractiveV19Screen(lang, backPrevious)
        FolderExtraV33.CALCULATORS -> ClinicalCalculatorsV26Screen(lang, backPrevious)
        FolderExtraV33.HISTORY_HUB -> HistoryHubV40Screen(lang, { navigate(AppScreen.VITALS) }, backPrevious)
        FolderExtraV33.ODONTOGRAM_HUB -> OdontogramHubV35Screen(lang, { navigate(it) }, backPrevious)
        FolderExtraV33.HEAD_NECK -> HeadNeckTeachingV40Screen(lang, backPrevious)
        FolderExtraV33.GENERAL_INSPECTION -> GeneralInspectionV40Screen(lang, backPrevious)
        FolderExtraV33.PHYSICAL_HUB -> PhysicalExamHubV40Screen(lang, { navigate(AppScreen.VITALS) }, backPrevious)
        FolderExtraV33.CAMBRA -> CambraInteractiveV39Screen(lang, backPrevious)
        FolderExtraV33.DENTAL_ANOMALIES -> DentalAnomaliesTeachingV40Screen(lang, backPrevious)
        FolderExtraV33.ERUPTION_ANOMALIES -> EruptionAnomaliesTeachingV40Screen(lang, backPrevious)
        FolderExtraV33.HABITS -> HabitsTeachingV40Screen(lang, backPrevious)
        FolderExtraV33.ORTHODONTIC_HISTORY -> OrthodonticHistoryV33Screen(lang, backPrevious)
        null -> when (screenState.value) {
            AppScreen.HOME -> CoverV19(lang) { navigate(AppScreen.FOLDER) }
            AppScreen.FOLDER -> FolderMenuV40Screen(lang, { navigate(it) }, { openExtra(it) }, backPrevious)
            AppScreen.SETTINGS -> ResponsiveScreenV17(
                tr(lang, "Configuración", "Settings"),
                tr(lang, "Idioma, paleta de ave, tipo y tamaño de letra se conservan en la configuración de la app.", "Language, bird palette, font and text size are kept in app settings."),
                backPrevious
            ) { }
            AppScreen.IDENTIFICATION -> IdentificationTeachingV40Screen(lang, backPrevious)
            AppScreen.HISTORY -> HistoryHubV40Screen(lang, { navigate(AppScreen.VITALS) }, backPrevious)
            AppScreen.INTAKE -> IntakeNoteScreen(lang, session, backPrevious)
            AppScreen.ACTIVITIES -> ActivitiesTableV40Screen(lang, backPrevious)
            AppScreen.VITALS -> VitalsInteractiveV19Screen(lang, backPrevious)
            AppScreen.ATM -> TmjTeachingV40Screen(lang, backPrevious)
            AppScreen.OCCLUSION -> OcclusionTeachingV41Screen(lang, backPrevious)
            AppScreen.MUCOSA -> MucosaAtlasV40Screen(lang, backPrevious)
            AppScreen.AUXILIARIES -> AuxiliariesV20Screen(lang, backPrevious)
            AppScreen.ODONTOGRAM -> OdontogramV20Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.ICDAS -> IcdasScreen(lang, session, onSessionChanged, backPrevious)
            AppScreen.CPOD -> CpodCeosV40Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.OLEARY -> OlearyScreen(lang, session, onSessionChanged, backPrevious)
            AppScreen.IPC -> IpcResponsiveV17Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.IHOS -> IhosResponsiveV17Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.PERIODONTOGRAM -> PeriodontalTeachingV40Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.POSTURE -> PostureVisualScreen(lang, backPrevious)
            AppScreen.PULPAL, AppScreen.APICAL -> EndodonticTeachingV40Screen(lang, backPrevious)
            AppScreen.TREATMENT -> TreatmentPlannerV40Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.SESSIONS -> TreatmentSessionsV40Screen(lang, backPrevious)
            AppScreen.ENDO -> EndodonticTeachingV40Screen(lang, backPrevious)
            AppScreen.PROSTHETIC -> ProstheticBridgeV40Screen(lang, backPrevious)
            AppScreen.SURGICAL -> SurgicalTeachingV40Screen(lang, backPrevious)
            AppScreen.CONSENT -> ConsentTeachingScreen(lang, backPrevious)
            AppScreen.REQUEST -> TreatmentRequestGuideV40Screen(lang, backPrevious)
            AppScreen.BUDGET -> BudgetGuideV40Screen(lang, backPrevious)
            AppScreen.EVOLUTION -> EvolutionExamplesV40Screen(lang, backPrevious)
        }
    }
}

@Composable
private fun CoverV19(lang: String, onOpen: () -> Unit) {
    BoxWithConstraints(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.background
                )
            )
        )
    ) {
        val compact = maxWidth < 360.dp || LocalDensity.current.fontScale >= 1.30f
        Column(
            Modifier.fillMaxSize().safeDrawingPadding().padding(horizontal = if (compact) 16.dp else 28.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.ysm_logo),
                tr(lang, "Logo YSM con ave y expediente dental", "YSM bird and dental record logo"),
                Modifier.size(if (compact) 118.dp else 158.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(10.dp))
            Text("YSM Expediente", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Text(tr(lang, "El expediente del dentista", "The dentist's record"), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text(
                tr(lang, "Deja volar tu imaginación y tus conocimientos renacerán", "Let your imagination take flight and your knowledge be reborn"),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(18.dp))
            Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
                Text("📖 ${tr(lang, "Abrir guía", "Open guide")}", fontWeight = FontWeight.Black)
            }
        }
    }
}
