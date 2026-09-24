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
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.draw.clip
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

private val V3Lavender = Color(0xFFE7D7F6)
private val V3Lilac = Color(0xFFD0B6E8)
private val V3Purple = Color(0xFF7B4DA8)
private val V3DeepPurple = Color(0xFF43235F)
private val V3Metal = Color(0xFFB7B0BD)
private val V3Paper = Color(0xFFFFFCFF)
private val V3Ink = Color(0xFF321943)

@Composable
fun AppRootV3(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var screen by remember { mutableStateOf(AppScreen.HOME) }

    if (!preferences.onboardingComplete) {
        BackHandler(enabled = true) { }
        OnboardingV3(preferences, onPreferencesChanged, onLanguageChanged) {
            onPreferencesChanged(preferences.copy(onboardingComplete = true))
        }
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

    PageTurnHost(target = screen) { target ->
        when (target) {
            AppScreen.HOME -> FolderCoverV3(
                lang = preferences.languageTag,
                onOpen = { screen = AppScreen.FOLDER },
                onSettings = { screen = AppScreen.SETTINGS }
            )
            AppScreen.FOLDER -> FolderSpreadV3(
                lang = preferences.languageTag,
                onNavigate = { screen = it },
                onClose = { screen = AppScreen.HOME },
                onSettings = { screen = AppScreen.SETTINGS }
            )
            AppScreen.SETTINGS -> SettingsV3(preferences, onPreferencesChanged, onLanguageChanged, { onSessionChanged(EducationalSession()) }, folderBack)
            AppScreen.IDENTIFICATION -> IdentificationScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.HISTORY_IDENTIFICATION, AppScreen.HISTORY_REASON, AppScreen.HISTORY_HEREDITARY, AppScreen.HISTORY_NONPATH, AppScreen.HISTORY_GYNECO, AppScreen.HISTORY_PATH, AppScreen.HISTORY_SURGICAL_TRAUMA, AppScreen.HISTORY_PHYSICAL, AppScreen.HISTORY_ORTHO, AppScreen.HISTORY_DENTAL_ALTERATIONS, AppScreen.HISTORY_HABITS, AppScreen.HISTORY_ORAL_EXAM -> HistoryScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.SYSTEMIC_PROTOCOLS -> SystemicProtocols37Screen(preferences.languageTag, folderBack)
            AppScreen.INTAKE -> IntakeNoteScreen(preferences.languageTag, session, folderBack)
            AppScreen.ACTIVITIES -> ActivitiesScreen(preferences.languageTag, folderBack)
            AppScreen.VITALS -> VitalsInteractiveScreen(preferences.languageTag, folderBack)
            AppScreen.ATM -> AtmScreen(preferences.languageTag, folderBack)
            AppScreen.OCCLUSION -> OcclusionScreen(preferences.languageTag, folderBack)
            AppScreen.MUCOSA -> MucosaScreen(preferences.languageTag, folderBack)
            AppScreen.CALCULATORS -> DentalCalculatorsV40Screen(lang, onBack) 
            AppScreen.AUXILIARIES -> AuxiliariesInteractiveScreen(preferences.languageTag, folderBack)
            AppScreen.ODONTOGRAM -> OdontogramQuadrantsScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.ICDAS -> IcdasScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.CPOD -> CpodScreen(preferences.languageTag, session, folderBack)
            AppScreen.OLEARY -> OlearyScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.IPC -> IpcScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.IHOS -> IhosScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.PERIODONTOGRAM -> PeriodontogramScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.POSTURE -> PostureVisualScreen(preferences.languageTag, folderBack)
            AppScreen.PULPAL -> PulpalScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.APICAL -> ApicalScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.TREATMENT -> TreatmentScreen(preferences.languageTag, session, onSessionChanged, folderBack)
            AppScreen.SESSIONS -> TreatmentBySessionsScreen(preferences.languageTag, folderBack)
            AppScreen.ENDO -> EndodonticSheetScreen(
                preferences.languageTag,
                session,
                onOpenPulpal = { screen = AppScreen.PULPAL },
                onOpenApical = { screen = AppScreen.APICAL },
                onBack = folderBack
            )
            AppScreen.PROSTHETIC -> ProstheticSheetScreen(preferences.languageTag, folderBack)
            AppScreen.SURGICAL -> SurgicalSheetScreen(preferences.languageTag, folderBack)
            AppScreen.CONSENT -> ConsentTeachingScreen(preferences.languageTag, folderBack)
            AppScreen.REQUEST -> SimpleEducationalSheet(
                preferences.languageTag,
                "Solicitud de tratamiento",
                "Treatment request",
                "Aprende para qué sirve y qué debe identificar claramente.",
                "Learn its purpose and what it should clearly identify.",
                listOf("Servicio solicitado", "Motivo", "Área u órgano dentario", "Prioridad / referencia", "Responsable y supervisión"),
                listOf("Requested service", "Reason", "Area or tooth", "Priority / referral", "Responsible clinician and supervision"),
                folderBack
            )
            AppScreen.BUDGET -> SimpleEducationalSheet(
                preferences.languageTag,
                "Presupuesto",
                "Budget",
                "Aprende su estructura administrativa sin registrar cobros reales.",
                "Learn its administrative structure without recording real payments.",
                listOf("Procedimiento o concepto", "Cantidad", "Costo unitario", "Subtotal", "Total", "Servicios externos/laboratorio cuando procedan"),
                listOf("Procedure or concept", "Quantity", "Unit cost", "Subtotal", "Total", "External/laboratory services when applicable"),
                folderBack
            )
            AppScreen.EVOLUTION -> EvolutionScreen(preferences.languageTag, session, folderBack)
        }
    }
}

@Composable
private fun PageTurnHost(target: AppScreen, content: @Composable (AppScreen) -> Unit) {
    var current by remember { mutableStateOf(target) }
    var previous by remember { mutableStateOf<AppScreen?>(null) }
    val progress = remember { Animatable(1f) }

    LaunchedEffect(target) {
        if (target != current) {
            previous = current
            current = target
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis = 520, easing = FastOutSlowInEasing))
            previous = null
        }
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        previous?.let { old ->
            Box(
                Modifier.fillMaxSize().graphicsLayer {
                    rotationY = -92f * progress.value
                    transformOrigin = TransformOrigin(1f, 0.5f)
                    cameraDistance = 24f
                    alpha = (1f - progress.value * 0.45f).coerceIn(0f, 1f)
                }
            ) { content(old) }
        }
        Box(
            Modifier.fillMaxSize().graphicsLayer {
                rotationY = 92f * (1f - progress.value)
                transformOrigin = TransformOrigin(0f, 0.5f)
                cameraDistance = 24f
                alpha = progress.value.coerceIn(0f, 1f)
            }
        ) { content(current) }
    }
}

@Composable
private fun OnboardingV3(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().safeDrawingPadding().padding(22.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(painterResource(R.drawable.ysm_logo), null, modifier = Modifier.align(Alignment.CenterHorizontally).size(150.dp))
        Text(tr(preferences.languageTag, "El expediente del dentista", "The Dentist's Clinical Record"), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        SectionCard(tr(preferences.languageTag, "Idioma", "Language")) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(preferences.languageTag == "es", { onPreferencesChanged(preferences.copy(languageTag = "es")); onLanguageChanged("es") }, { Text("Español") })
                FilterChip(preferences.languageTag == "en", { onPreferencesChanged(preferences.copy(languageTag = "en")); onLanguageChanged("en") }, { Text("English") })
            }
        }
        SectionCard(tr(preferences.languageTag, "Tratamiento profesional", "Professional title")) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTOR, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) }, { Text("Doctor") })
                FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTORA, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) }, { Text("Doctora") })
            }
        }
        NoticeCard(tr(preferences.languageTag,
            "Uso educativo offline. La app acompaña el llenado del expediente físico; no crea un expediente electrónico del paciente.",
            "Offline educational use. The app assists completion of the physical record; it does not create an electronic patient record."
        ))
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text(tr(preferences.languageTag, "Continuar a la portada", "Continue to cover")) }
    }
}

@Composable
private fun FolderCoverV3(lang: String, onOpen: () -> Unit, onSettings: () -> Unit) {
    Box(Modifier.fillMaxSize().background(V3DeepPurple)) {
        Image(
            painter = painterResource(R.drawable.folder_cover_ysm),
            contentDescription = tr(lang, "Portada del expediente", "Folder cover"),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        OutlinedButton(
            onClick = onSettings,
            modifier = Modifier.align(Alignment.TopEnd).safeDrawingPadding().padding(14.dp)
        ) { Text("⚙") }
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(bottom = 76.dp)
                .fillMaxWidth(0.78f)
                .height(76.dp)
                .clip(RoundedCornerShape(28.dp))
                .clickable(onClick = onOpen)
        )
    }
}

private data class V3Tab(val screen: AppScreen, val icon: String, val es: String, val en: String, val group: Int)

private fun leftTabsV3() = listOf(
    V3Tab(AppScreen.IDENTIFICATION, "▤", "Ficha", "ID", 0),
    V3Tab(AppScreen.HISTORY, "●", "Anamnesis", "History", 0),
    V3Tab(AppScreen.INTAKE, "✚", "Ingreso", "Intake", 0),
    V3Tab(AppScreen.MUCOSA, "◡", "Mucosas", "Mucosa", 0),
    V3Tab(AppScreen.OCCLUSION, "◇", "Oclusión", "Occlusion", 0),
    V3Tab(AppScreen.VITALS, "♥", "Vitales", "Vitals", 0),
    V3Tab(AppScreen.AUXILIARIES, "⚗", "Auxiliares", "Aids", 0)
)

private fun rightTabsV3() = listOf(
    V3Tab(AppScreen.ODONTOGRAM, "🦷", "Odontograma", "Odontogram", 1),
    V3Tab(AppScreen.ICDAS, "▣", "ICDAS", "ICDAS", 1),
    V3Tab(AppScreen.OLEARY, "⌁", "O’Leary", "O’Leary", 1),
    V3Tab(AppScreen.IPC, "Ⅵ", "IPC", "CPI", 1),
    V3Tab(AppScreen.IHOS, "▥", "IHOS", "OHI-S", 1),
    V3Tab(AppScreen.PERIODONTOGRAM, "⌇", "Periodonto", "Periodontal", 1),
    V3Tab(AppScreen.PULPAL, "⚡", "Pulpar", "Pulpal", 1),
    V3Tab(AppScreen.APICAL, "◎", "Periapical", "Apical", 1),
    V3Tab(AppScreen.ENDO, "│", "Endo", "Endo", 2),
    V3Tab(AppScreen.PROSTHETIC, "⌒", "Prótesis", "Prosthetics", 2),
    V3Tab(AppScreen.SURGICAL, "✦", "Cirugía", "Surgery", 2),
    V3Tab(AppScreen.EVOLUTION, "▤", "Evolución", "Progress", 2)
)

@Composable
private fun FolderSpreadV3(lang: String, onNavigate: (AppScreen) -> Unit, onClose: () -> Unit, onSettings: () -> Unit) {
    var searchOpen by remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().safeDrawingPadding().background(
            Brush.verticalGradient(listOf(V3Lavender, Color(0xFFF4EDF8), V3Lilac.copy(alpha = 0.7f)))
        )
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onClose) { Text("‹ ${tr(lang, "Portada", "Cover")}") }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(painterResource(R.drawable.ysm_logo), null, modifier = Modifier.size(44.dp))
                Text(tr(lang, "Expediente abierto", "Open folder"), fontWeight = FontWeight.Bold, color = V3Ink)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedButton(onClick = { searchOpen = !searchOpen }) { Text("🔎") }
                OutlinedButton(onClick = onSettings) { Text("⚙") }
            }
        }
        if (searchOpen) {
            Box(Modifier.padding(horizontal = 8.dp)) { QuickClinicalAccess(lang, onNavigate) }
        }

        Row(Modifier.fillMaxSize().weight(1f).padding(horizontal = 5.dp, vertical = 4.dp)) {
            SideTabsV3(lang, leftTabsV3(), onNavigate, Modifier.width(58.dp).fillMaxHeight())
            Spacer(Modifier.width(4.dp))
            BoxWithConstraints(Modifier.weight(1f).fillMaxHeight()) {
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    ActivitySheetV3(lang, onNavigate, Modifier.weight(1f))
                    IntakeSheetV3(lang, onNavigate, Modifier.weight(1f))
                }
            }
            Spacer(Modifier.width(4.dp))
            SideTabsV3(lang, rightTabsV3(), onNavigate, Modifier.width(62.dp).fillMaxHeight())
        }
    }
}

@Composable
private fun SideTabsV3(lang: String, tabs: List<V3Tab>, onNavigate: (AppScreen) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        tabs.forEach { tab ->
            val bg = when (tab.group) {
                0 -> V3Lavender
                1 -> V3Purple
                else -> V3DeepPurple
            }
            val fg = if (tab.group == 0) V3Ink else Color.White
            Card(
                onClick = { onNavigate(tab.screen) },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = bg),
                border = if (tab.group == 2) BorderStroke(1.dp, V3Metal) else null
            ) {
                Column(Modifier.fillMaxSize().padding(horizontal = 3.dp, vertical = 4.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(tab.icon, color = fg, fontSize = 15.sp)
                    Text(if (lang == "en") tab.en else tab.es, color = fg, fontSize = 8.sp, lineHeight = 9.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun ActivitySheetV3(lang: String, onNavigate: (AppScreen) -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = { onNavigate(AppScreen.ACTIVITIES) },
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = V3Paper),
        border = BorderStroke(1.dp, V3Metal)
    ) {
        Column(Modifier.fillMaxSize().padding(9.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(tr(lang, "REGISTRO DE ACTIVIDADES", "ACTIVITY RECORD"), color = V3DeepPurple, fontWeight = FontWeight.Black, fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(tr(lang, "Alumno / Operador", "Student / Operator"), fontSize = 8.sp, fontWeight = FontWeight.Bold)
            Surface(Modifier.fillMaxWidth().height(22.dp), shape = RoundedCornerShape(6.dp), color = V3Lavender.copy(alpha = 0.32f)) {}
            Text(tr(lang, "Actividad planeada · Autorización · Realizada · Supervisión", "Planned · Authorization · Done · Supervision"), fontSize = 7.sp, color = V3Ink)
            repeat(8) { idx ->
                Row(Modifier.fillMaxWidth().height(25.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("${idx + 1}", fontSize = 7.sp, modifier = Modifier.width(15.dp))
                    Surface(Modifier.weight(1f).height(19.dp), color = if (idx % 2 == 0) Color(0xFFF8F3FB) else Color.White, border = BorderStroke(0.5.dp, V3Lilac)) {}
                }
            }
            Spacer(Modifier.weight(1f))
            Text(tr(lang, "Toca la hoja para aprender cómo se llena.", "Tap the page to learn how to complete it."), fontSize = 8.sp, color = V3Purple, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

private data class IntakeJump(val icon: String, val es: String, val en: String, val screen: AppScreen)
private val intakeJumpsV3 = listOf(
    IntakeJump("●", "Identificación", "Identification", AppScreen.IDENTIFICATION),
    IntakeJump("✚", "ASA", "ASA", AppScreen.HISTORY),
    IntakeJump("◉", "Medicamentos", "Medications", AppScreen.HISTORY),
    IntakeJump("◌", "ATM y músculos", "TMJ and muscles", AppScreen.ATM),
    IntakeJump("◇", "Oclusión", "Occlusion", AppScreen.OCCLUSION),
    IntakeJump("◡", "Mucosas", "Mucosa", AppScreen.MUCOSA),
    IntakeJump("+", "CPOD / ceod", "DMFT / dmft", AppScreen.CPOD),
    IntakeJump("⌇", "Periodontal", "Periodontal", AppScreen.PERIODONTOGRAM),
    IntakeJump("⚡", "Pulpar / periapical", "Pulpal / apical", AppScreen.PULPAL),
    IntakeJump("⌒", "Prótesis", "Prosthetics", AppScreen.PROSTHETIC)
)

@Composable
private fun IntakeSheetV3(lang: String, onNavigate: (AppScreen) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = V3Paper),
        border = BorderStroke(1.dp, V3Metal)
    ) {
        Column(Modifier.fillMaxSize().padding(9.dp)) {
            Text(tr(lang, "NOTA DE INGRESO", "INTAKE NOTE"), color = V3DeepPurple, fontWeight = FontWeight.Black, fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(tr(lang, "Toca un apartado para abrir su guía interactiva.", "Tap a section to open its interactive guide."), fontSize = 8.sp, color = V3Purple, modifier = Modifier.padding(vertical = 5.dp))
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                intakeJumpsV3.forEach { jump ->
                    Card(
                        onClick = { onNavigate(jump.screen) },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = V3Lavender.copy(alpha = 0.34f))
                    ) {
                        Row(Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(jump.icon, color = V3Purple, fontSize = 10.sp)
                            Text(if (lang == "en") jump.en else jump.es, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = V3Ink, modifier = Modifier.weight(1f))
                            Text("›", color = V3Purple)
                        }
                    }
                }
            }
            Text(tr(lang, "Identificación · diagnóstico · plan inicial", "Identification · diagnosis · initial plan"), fontSize = 7.sp, color = V3Purple, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
        }
    }
}

@Composable
private fun SettingsV3(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    onResetSession: () -> Unit,
    onBack: () -> Unit
) {
    val lang = preferences.languageTag
    Column(Modifier.fillMaxSize().safeDrawingPadding().padding(18.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ScreenHeader(tr(lang, "Configuración", "Settings"), onBack)
        SectionCard(tr(lang, "Idioma", "Language")) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(lang == "es", { onPreferencesChanged(preferences.copy(languageTag = "es")); onLanguageChanged("es") }, { Text("Español") })
                FilterChip(lang == "en", { onPreferencesChanged(preferences.copy(languageTag = "en")); onLanguageChanged("en") }, { Text("English") })
            }
        }
        SectionCard(tr(lang, "Doctor / Doctora", "Professional title")) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTOR, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) }, { Text("Doctor") })
                FilterChip(preferences.clinicianTitle == ClinicianTitle.DOCTORA, { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) }, { Text("Doctora") })
            }
        }
        SectionCard(tr(lang, "Paleta", "Palette")) {
            PaletteStyle.entries.forEach { style ->
                val label = when (style) {
                    PaletteStyle.WOOD -> tr(lang, "Madera clásica", "Classic wood")
                    PaletteStyle.CLINICAL_GREEN -> tr(lang, "Verde clínico", "Clinical green")
                    PaletteStyle.DENTAL_BLUE -> tr(lang, "Azul dental", "Dental blue")
                    PaletteStyle.WINE -> tr(lang, "Vino", "Wine")
                    PaletteStyle.SAGE -> tr(lang, "Salvia", "Sage")
                    PaletteStyle.MONO -> tr(lang, "Blanco/negro", "Monochrome")
                }
                FilterChip(preferences.paletteStyle == style, { onPreferencesChanged(preferences.copy(paletteStyle = style)) }, { Text(label) }, modifier = Modifier.padding(end = 4.dp, bottom = 4.dp))
            }
        }
        SectionCard(tr(lang, "Tipografía", "Typography")) {
            FontStyle.entries.forEach { style ->
                val label = when (style) {
                    FontStyle.MODERN -> tr(lang, "Moderna", "Modern")
                    FontStyle.ROUNDED -> tr(lang, "Redondeada", "Rounded")
                    FontStyle.ACADEMIC -> tr(lang, "Académica", "Academic")
                    FontStyle.ACCESSIBLE -> tr(lang, "Alta legibilidad", "High readability")
                }
                FilterChip(preferences.fontStyle == style, { onPreferencesChanged(preferences.copy(fontStyle = style)) }, { Text(label) }, modifier = Modifier.padding(end = 4.dp, bottom = 4.dp))
            }
        }
        OutlinedButton(onClick = onResetSession, modifier = Modifier.fillMaxWidth()) { Text(tr(lang, "Limpiar marcas didácticas", "Clear teaching marks")) }
    }
}
