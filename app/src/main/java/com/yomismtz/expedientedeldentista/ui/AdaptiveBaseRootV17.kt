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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

private data class AdaptiveTabV17(
    val screen: AppScreen,
    val icon: String,
    val es: String,
    val en: String,
    val group: Int
)

private val adaptiveTabsV17 = listOf(
    AdaptiveTabV17(AppScreen.IDENTIFICATION, "👤", "Identificación", "Identification", 0),
    AdaptiveTabV17(AppScreen.HISTORY, "🩺", "Anamnesis / ASA", "History / ASA", 0),
    AdaptiveTabV17(AppScreen.INTAKE, "📋", "Nota de ingreso", "Intake note", 0),
    AdaptiveTabV17(AppScreen.VITALS, "❤️", "Signos vitales", "Vitals", 0),
    AdaptiveTabV17(AppScreen.ATM, "◉", "ATM", "TMJ", 0),
    AdaptiveTabV17(AppScreen.OCCLUSION, "↔", "Oclusión", "Occlusion", 0),
    AdaptiveTabV17(AppScreen.MUCOSA, "👄", "Mucosas", "Mucosa", 0),
    AdaptiveTabV17(AppScreen.AUXILIARIES, "🧪", "Auxiliares", "Auxiliaries", 0),

    AdaptiveTabV17(AppScreen.ODONTOGRAM, "🦷", "Odontograma", "Odontogram", 1),
    AdaptiveTabV17(AppScreen.ICDAS, "🔎", "ICDAS", "ICDAS", 1),
    AdaptiveTabV17(AppScreen.CPOD, "➕", "CPOD / ceod", "DMFT / dmft", 1),
    AdaptiveTabV17(AppScreen.OLEARY, "🔴", "O’Leary", "O’Leary", 1),
    AdaptiveTabV17(AppScreen.IPC, "6️⃣", "IPC", "CPI", 1),
    AdaptiveTabV17(AppScreen.IHOS, "🪥", "IHOS", "OHI-S", 1),
    AdaptiveTabV17(AppScreen.PERIODONTOGRAM, "📈", "Periodontograma", "Periodontogram", 1),
    AdaptiveTabV17(AppScreen.POSTURE, "🧍", "Postura", "Posture", 1),
    AdaptiveTabV17(AppScreen.PULPAL, "⚡", "Pulpar + periapical", "Pulpal + apical", 1),

    AdaptiveTabV17(AppScreen.TREATMENT, "📝", "Diagnóstico / tratamiento", "Diagnosis / treatment", 2),
    AdaptiveTabV17(AppScreen.SESSIONS, "🗓", "Tratamiento por sesiones", "Treatment by sessions", 2),
    AdaptiveTabV17(AppScreen.ENDO, "⚡", "Endodoncia", "Endodontics", 2),
    AdaptiveTabV17(AppScreen.PROSTHETIC, "👑", "Prótesis / Kennedy", "Prosthodontics / Kennedy", 2),
    AdaptiveTabV17(AppScreen.SURGICAL, "✚", "Cirugía", "Surgery", 2),
    AdaptiveTabV17(AppScreen.CONSENT, "✍", "Consentimiento", "Consent", 2),
    AdaptiveTabV17(AppScreen.EVOLUTION, "📄", "Evolución", "Progress notes", 2)
)

@Composable
fun AdaptiveBaseRootV17(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var screen by remember { mutableStateOf(AppScreen.HOME) }
    val lang = preferences.languageTag
    val folderBack = { screen = AppScreen.FOLDER }

    BackHandler(enabled = screen != AppScreen.HOME) {
        screen = if (screen == AppScreen.FOLDER) AppScreen.HOME else AppScreen.FOLDER
    }

    when (screen) {
        AppScreen.HOME -> AdaptiveCoverV17(lang) { screen = AppScreen.FOLDER }
        AppScreen.FOLDER -> AdaptiveFolderV17(lang, onNavigate = { screen = it }, onClose = { screen = AppScreen.HOME })
        AppScreen.SETTINGS -> ResponsiveScreenV17(
            tr(lang, "Configuración", "Settings"),
            tr(lang, "Usa el botón ⚙ fijo de la esquina superior para cambiar apariencia y accesibilidad.", "Use the fixed ⚙ button in the upper corner to change appearance and accessibility."),
            folderBack
        ) { }
        AppScreen.IDENTIFICATION -> IdentificationScreen(lang, session, onSessionChanged, folderBack)
        AppScreen.HISTORY -> HistoryScreen(lang, session, onSessionChanged, folderBack)
        AppScreen.INTAKE -> IntakeNoteScreen(lang, session, folderBack)
        AppScreen.ACTIVITIES -> ActivitiesScreen(lang, folderBack)
        AppScreen.VITALS -> VitalsInteractiveScreen(lang, folderBack)
        AppScreen.ATM -> AtmScreen(lang, folderBack)
        AppScreen.OCCLUSION -> OcclusionScreen(lang, folderBack)
        AppScreen.MUCOSA -> MucosaInteractiveV2Screen(lang, folderBack)
        AppScreen.AUXILIARIES -> AuxiliariesInteractiveScreen(lang, folderBack)
        AppScreen.ODONTOGRAM -> OdontogramResponsiveV17Screen(lang, session, onSessionChanged, folderBack)
        AppScreen.ICDAS -> IcdasScreen(lang, session, onSessionChanged, folderBack)
        AppScreen.CPOD -> CpodScreen(lang, session, folderBack)
        AppScreen.OLEARY -> OlearyScreen(lang, session, onSessionChanged, folderBack)
        AppScreen.IPC -> IpcResponsiveV17Screen(lang, session, onSessionChanged, folderBack)
        AppScreen.IHOS -> IhosResponsiveV17Screen(lang, session, onSessionChanged, folderBack)
        AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(lang, session, onSessionChanged, folderBack)
        AppScreen.POSTURE -> PostureVisualScreen(lang, folderBack)
        AppScreen.PULPAL, AppScreen.APICAL -> PulpalPeriapicalInteractiveV2Screen(
            lang, session, onSessionChanged,
            onOpenEndo = { screen = AppScreen.ENDO },
            onBack = folderBack
        )
        AppScreen.TREATMENT -> TreatmentScreen(lang, session, onSessionChanged, folderBack)
        AppScreen.SESSIONS -> TreatmentBySessionsScreen(lang, folderBack)
        AppScreen.ENDO -> EndodonticInteractiveV2Screen(
            lang = lang,
            session = session,
            onOpenPulpal = { screen = AppScreen.PULPAL },
            onOpenApical = { screen = AppScreen.PULPAL },
            onBack = folderBack
        )
        AppScreen.PROSTHETIC -> ProstheticResponsiveV17Screen(lang, folderBack)
        AppScreen.SURGICAL -> SurgicalSheetScreen(lang, folderBack)
        AppScreen.CONSENT -> ConsentTeachingScreen(lang, folderBack)
        AppScreen.REQUEST -> SimpleEducationalSheet(
            lang,
            "Solicitud de tratamiento", "Treatment request",
            "Aprende para qué sirve y qué debe identificar claramente.",
            "Learn its purpose and what it should clearly identify.",
            listOf("Servicio solicitado", "Motivo", "Área u órgano dentario", "Prioridad / referencia", "Responsable y supervisión"),
            listOf("Requested service", "Reason", "Area or tooth", "Priority / referral", "Responsible clinician and supervision"),
            folderBack
        )
        AppScreen.BUDGET -> SimpleEducationalSheet(
            lang,
            "Presupuesto", "Budget",
            "Aprende su estructura administrativa sin registrar cobros reales.",
            "Learn its administrative structure without recording real payments.",
            listOf("Procedimiento", "Cantidad", "Costo unitario", "Subtotal", "Total", "Laboratorio cuando proceda"),
            listOf("Procedure", "Quantity", "Unit cost", "Subtotal", "Total", "Laboratory when applicable"),
            folderBack
        )
        AppScreen.EVOLUTION -> EvolutionScreen(lang, session, folderBack)
    }
}

@Composable
private fun AdaptiveCoverV17(lang: String, onOpen: () -> Unit) {
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
        val logo = if (compact) 94.dp else 132.dp
        val sidePadding = if (compact) 16.dp else 28.dp
        Column(
            Modifier.fillMaxSize().safeDrawingPadding().padding(horizontal = sidePadding, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painterResource(R.drawable.ysm_logo), null, Modifier.size(logo), contentScale = ContentScale.Fit)
            Spacer(Modifier.height(14.dp))
            Text("YSM Expediente", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Text(
                tr(lang, "El expediente del dentista", "The dentist's clinical record"),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(10.dp))
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Text(
                    tr(lang,
                        "Interfaz adaptable: teléfono pequeño, estándar, pantalla grande y letra grande de Android.",
                        "Adaptive interface for small phones, standard phones, large screens and Android large text."),
                    Modifier.padding(14.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(18.dp))
            Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
                Text("📖  ${tr(lang, "Abrir expediente", "Open record")}", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun AdaptiveFolderV17(
    lang: String,
    onNavigate: (AppScreen) -> Unit,
    onClose: () -> Unit
) {
    var selectedGroup by remember { mutableStateOf(0) }
    ResponsiveScreenV17(
        title = "YSM Expediente",
        subtitle = tr(lang,
            "Carpeta clínica adaptable. En pantallas pequeñas se muestra una sección a la vez; en pantallas amplias se muestran varias hojas sin reducir el texto.",
            "Adaptive clinical folder. Small screens show one section at a time; larger screens show multiple sheets without shrinking text."),
        onBack = onClose
    ) { profile ->
        val groups = listOf(
            tr(lang, "Ingreso", "Intake"),
            tr(lang, "Exámenes", "Exams"),
            tr(lang, "Tratamiento", "Treatment")
        )

        ResponsiveSectionV17(tr(lang, "Secciones del expediente", "Record sections")) {
            val groupColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 3
            AdaptiveGridV17(groups.size, groupColumns) { index ->
                FilterChip(
                    selected = selectedGroup == index,
                    onClick = { selectedGroup = index },
                    label = { Text(groups[index]) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        val visibleGroups = if (profile.width == ScreenWidthV17.EXPANDED && !profile.largeSystemText) listOf(0, 1, 2) else listOf(selectedGroup)
        if (visibleGroups.size == 1) {
            FolderGroupV17(lang, visibleGroups.first(), onNavigate, profile)
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                visibleGroups.forEach { group ->
                    Column(Modifier.weight(1f)) {
                        FolderGroupV17(lang, group, onNavigate, profile.copy(columns = 1))
                    }
                }
            }
        }
    }
}

@Composable
private fun FolderGroupV17(
    lang: String,
    group: Int,
    onNavigate: (AppScreen) -> Unit,
    profile: ScreenProfileV17
) {
    val title = when (group) {
        0 -> tr(lang, "Hoja de ingreso", "Intake sheet")
        1 -> tr(lang, "Exámenes interactivos", "Interactive exams")
        else -> tr(lang, "Plan y tratamiento", "Plan and treatment")
    }
    val items = adaptiveTabsV17.filter { it.group == group }
    ResponsiveSectionV17(title) {
        val columns = when {
            profile.largeSystemText -> 1
            profile.width == ScreenWidthV17.COMPACT -> 1
            profile.width == ScreenWidthV17.MEDIUM -> 2
            else -> 1
        }
        AdaptiveGridV17(items.size, columns) { index ->
            val item = items[index]
            Card(
                onClick = { onNavigate(item.screen) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(15.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text(item.icon, style = MaterialTheme.typography.titleLarge)
                    Column(Modifier.weight(1f)) {
                        Text(if (lang == "en") item.en else item.es, fontWeight = FontWeight.Bold)
                        Text(
                            tr(lang, "Toca para abrir", "Tap to open"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f)
                        )
                    }
                    Text("›", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}
