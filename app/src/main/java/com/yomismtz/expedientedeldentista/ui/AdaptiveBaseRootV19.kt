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

private data class NavigationStateV35(
    val screen: AppScreen,
    val extra: FolderExtraV33?
)

@Composable
fun AdaptiveBaseRootV19(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var screen by remember { mutableStateOf(AppScreen.HOME) }
    var extraScreen by remember { mutableStateOf<FolderExtraV33?>(null) }
    val navigationHistory = remember { mutableStateListOf<NavigationStateV35>() }
    val lang = preferences.languageTag

    fun saveCurrentRoute() {
        navigationHistory.add(NavigationStateV35(screen, extraScreen))
    }

    fun navigate(next: AppScreen) {
        if (extraScreen == null && next == screen) return
        saveCurrentRoute()
        screen = next
        extraScreen = null
    }

    fun openExtra(next: FolderExtraV33) {
        if (next == extraScreen) return
        saveCurrentRoute()
        extraScreen = next
    }

    fun goBack() {
        if (navigationHistory.isNotEmpty()) {
            val previous = navigationHistory.removeAt(navigationHistory.lastIndex)
            screen = previous.screen
            extraScreen = previous.extra
        } else {
            screen = AppScreen.HOME
            extraScreen = null
        }
    }

    val backPrevious = { goBack() }

    BackHandler(enabled = extraScreen != null || screen != AppScreen.HOME) { goBack() }

    when (extraScreen) {
        FolderExtraV33.SYMPTOMS -> QuickSignsSymptomsV26Screen(lang, session, onSessionChanged, backPrevious)
        FolderExtraV33.CALCULATORS -> ClinicalCalculatorsV26Screen(lang, backPrevious)
        FolderExtraV33.HISTORY_HUB -> HistoryMenuV35Screen(lang, { navigate(it) }, { openExtra(it) }, backPrevious)
        FolderExtraV33.ODONTOGRAM_HUB -> OdontogramHubV35Screen(lang, { navigate(it) }, backPrevious)
        FolderExtraV33.HEAD_NECK -> HeadNeckExplorationV25Screen(lang, session, onSessionChanged, { navigate(AppScreen.ATM) }, backPrevious)
        FolderExtraV33.GENERAL_INSPECTION -> GeneralInspectionV24Screen(lang, session, onSessionChanged, backPrevious)
        FolderExtraV33.PHYSICAL_HUB -> PhysicalExamMenuV35Screen(lang, { navigate(it) }, { openExtra(it) }, backPrevious)
        FolderExtraV33.CAMBRA -> CambraV33Screen(lang, backPrevious)
        FolderExtraV33.DENTAL_ANOMALIES -> DentalAnomaliesV35Screen(lang, backPrevious)
        FolderExtraV33.ERUPTION_ANOMALIES -> EruptionAnomaliesV35Screen(lang, backPrevious)
        FolderExtraV33.HABITS -> HabitsParafunctionsV33Screen(lang, backPrevious)
        FolderExtraV33.ORTHODONTIC_HISTORY -> OrthodonticHistoryV33Screen(lang, backPrevious)
        null -> when (screen) {
            AppScreen.HOME -> CoverV19(lang) { navigate(AppScreen.FOLDER) }
            AppScreen.FOLDER -> FolderMenuV35Screen(lang, { navigate(it) }, { openExtra(it) }, backPrevious)
            AppScreen.SETTINGS -> ResponsiveScreenV17(
                tr(lang, "Configuración", "Settings"),
                tr(lang, "Usa el botón de Configuración de la barra superior.", "Use Settings in the top bar."),
                backPrevious
            ) { }
            AppScreen.IDENTIFICATION -> IdentificationScreen(lang, session, onSessionChanged, backPrevious)
            AppScreen.HISTORY -> HistoryScreen(lang, session, onSessionChanged, backPrevious)
            AppScreen.INTAKE -> IntakeNoteScreen(lang, session, backPrevious)
            AppScreen.ACTIVITIES -> ActivitiesScreen(lang, backPrevious)
            AppScreen.VITALS -> VitalsInteractiveV19Screen(lang, backPrevious)
            AppScreen.ATM -> AtmScreen(lang, backPrevious)
            AppScreen.OCCLUSION -> OcclusionInteractiveV19Screen(lang, backPrevious)
            AppScreen.MUCOSA -> MucosaExamV24Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.AUXILIARIES -> AuxiliariesV20Screen(lang, backPrevious)
            AppScreen.ODONTOGRAM -> OdontogramV20Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.ICDAS -> IcdasScreen(lang, session, onSessionChanged, backPrevious)
            AppScreen.CPOD -> CpodInteractiveV19Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.OLEARY -> OlearyScreen(lang, session, onSessionChanged, backPrevious)
            AppScreen.IPC -> IpcResponsiveV17Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.IHOS -> IhosResponsiveV17Screen(lang, session, onSessionChanged, backPrevious)
            AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(lang, session, onSessionChanged, backPrevious)
            AppScreen.POSTURE -> PostureVisualScreen(lang, backPrevious)
            AppScreen.PULPAL, AppScreen.APICAL -> PulpalPeriapicalInteractiveV2Screen(lang, session, onSessionChanged, { navigate(AppScreen.ENDO) }, backPrevious)
            AppScreen.TREATMENT -> TreatmentScreen(lang, session, onSessionChanged, backPrevious)
            AppScreen.SESSIONS -> TreatmentBySessionsScreen(lang, backPrevious)
            AppScreen.ENDO -> EndodonticInteractiveV2Screen(lang, session, { navigate(AppScreen.PULPAL) }, { navigate(AppScreen.PULPAL) }, backPrevious)
            AppScreen.PROSTHETIC -> ProstheticResponsiveV17Screen(lang, backPrevious)
            AppScreen.SURGICAL -> SurgicalSheetScreen(lang, backPrevious)
            AppScreen.CONSENT -> ConsentTeachingScreen(lang, backPrevious)
            AppScreen.REQUEST -> SimpleEducationalSheet(
                lang,
                "Solicitud de tratamiento",
                "Treatment request",
                "Aprende para qué sirve y qué debe identificar claramente.",
                "Learn its purpose and what it should clearly identify.",
                listOf("Servicio solicitado", "Motivo", "Área u órgano dentario", "Prioridad / referencia", "Responsable y supervisión"),
                listOf("Requested service", "Reason", "Area or tooth", "Priority / referral", "Responsible clinician and supervision"),
                backPrevious
            )
            AppScreen.BUDGET -> SimpleEducationalSheet(
                lang,
                "Presupuesto",
                "Budget",
                "Aprende su estructura administrativa sin registrar cobros reales.",
                "Learn its administrative structure without recording real payments.",
                listOf("Procedimiento", "Cantidad", "Costo unitario", "Subtotal", "Total", "Laboratorio cuando proceda"),
                listOf("Procedure", "Quantity", "Unit cost", "Subtotal", "Total", "Laboratory when applicable"),
                backPrevious
            )
            AppScreen.EVOLUTION -> EvolutionScreen(lang, session, backPrevious)
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
                Text("📖 ${tr(lang, "Abrir expediente", "Open record")}", fontWeight = FontWeight.Black)
            }
        }
    }
}