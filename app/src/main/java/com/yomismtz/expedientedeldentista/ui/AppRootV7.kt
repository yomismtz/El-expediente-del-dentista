package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences

@Composable
fun AppRootV7(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var overlay by remember { mutableStateOf(V7Overlay.NONE) }
    var writingHelp by remember { mutableStateOf(false) }
    val lang = preferences.languageTag
    val backToIntake = { overlay = V7Overlay.INTAKE }

    Box(Modifier.fillMaxSize()) {
        AdaptiveBaseRootV17(preferences, onPreferencesChanged, onLanguageChanged, session, onSessionChanged)

        if (preferences.onboardingComplete && overlay == V7Overlay.NONE) {
            Button(
                onClick = { overlay = V7Overlay.INTAKE },
                modifier = Modifier.align(Alignment.TopStart).safeDrawingPadding().padding(10.dp)
            ) { Text("📋 ${tr(lang, "Nota de ingreso", "Intake note")}") }
        }

        if (overlay != V7Overlay.NONE) {
            Surface(Modifier.fillMaxSize(), tonalElevation = 8.dp) {
                when (overlay) {
                    V7Overlay.INTAKE -> IntakeInteractiveV3Screen(
                        lang,
                        onIdentification = { overlay = V7Overlay.IDENTIFICATION },
                        onHistory = { overlay = V7Overlay.HISTORY },
                        onVitals = { overlay = V7Overlay.VITALS },
                        onAtm = { overlay = V7Overlay.ATM },
                        onOcclusion = { overlay = V7Overlay.OCCLUSION },
                        onMucosa = { overlay = V7Overlay.MUCOSA },
                        onCpod = { overlay = V7Overlay.CPOD },
                        onPeriodontal = { overlay = V7Overlay.PERIODONTAL },
                        onPulpal = { overlay = V7Overlay.PULPAL_APICAL },
                        onProsthetic = { overlay = V7Overlay.PROSTHETIC },
                        onBack = { overlay = V7Overlay.NONE }
                    )
                    V7Overlay.HUB -> ExamHubV1Screen(lang, { overlay = it }, backToIntake)
                    V7Overlay.IDENTIFICATION -> IdentificationScreen(lang, session, onSessionChanged, backToIntake)
                    V7Overlay.HISTORY -> HistoryScreen(lang, session, onSessionChanged, backToIntake)
                    V7Overlay.VITALS -> VitalsInteractiveScreen(lang, backToIntake)
                    V7Overlay.ATM -> AtmScreen(lang, backToIntake)
                    V7Overlay.OCCLUSION -> OcclusionScreen(lang, backToIntake)
                    V7Overlay.MUCOSA -> MucosaInteractiveV2Screen(lang, backToIntake)
                    V7Overlay.AUXILIARIES -> AuxiliariesInteractiveScreen(lang, backToIntake)
                    V7Overlay.ODONTOGRAM -> OdontogramResponsiveV17Screen(lang, session, onSessionChanged, backToIntake)
                    V7Overlay.ICDAS -> IcdasScreen(lang, session, onSessionChanged, backToIntake)
                    V7Overlay.CPOD -> CpodScreen(lang, session, backToIntake)
                    V7Overlay.OLEARY -> OlearyScreen(lang, session, onSessionChanged, backToIntake)
                    V7Overlay.IPC -> IpcResponsiveV17Screen(lang, session, onSessionChanged, backToIntake)
                    V7Overlay.IHOS -> IhosResponsiveV17Screen(lang, session, onSessionChanged, backToIntake)
                    V7Overlay.PERIODONTAL -> PeriodontogramScreen(lang, session, onSessionChanged, backToIntake)
                    V7Overlay.POSTURE -> PostureVisualScreen(lang, backToIntake)
                    V7Overlay.PULPAL_APICAL -> PulpalPeriapicalInteractiveV2Screen(
                        lang, session, onSessionChanged,
                        onOpenEndo = { overlay = V7Overlay.ENDO },
                        onBack = backToIntake
                    )
                    V7Overlay.ENDO -> EndodonticInteractiveV2Screen(
                        lang = lang,
                        session = session,
                        onOpenPulpal = { overlay = V7Overlay.PULPAL_APICAL },
                        onOpenApical = { overlay = V7Overlay.PULPAL_APICAL },
                        onBack = backToIntake
                    )
                    V7Overlay.PROSTHETIC -> ProstheticResponsiveV17Screen(lang, backToIntake)
                    V7Overlay.SURGICAL -> SurgicalSheetScreen(lang, backToIntake)
                    V7Overlay.CONSENT -> ConsentTeachingScreen(lang, backToIntake)
                    V7Overlay.EVOLUTION -> EvolutionScreen(lang, session, backToIntake)
                    V7Overlay.NONE -> Unit
                }
            }

            if (overlay == V7Overlay.INTAKE) {
                OutlinedButton(
                    onClick = { overlay = V7Overlay.HUB },
                    modifier = Modifier.align(Alignment.BottomStart).safeDrawingPadding().padding(14.dp)
                ) { Text("🧭 ${tr(lang, "Todos los exámenes", "All examinations")}") }
            }

            if (overlay !in listOf(V7Overlay.NONE, V7Overlay.INTAKE, V7Overlay.HUB)) {
                AdaptiveFloatingActionsV17(
                    lang = lang,
                    onWriting = { writingHelp = true },
                    onIntake = backToIntake,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }

            if (writingHelp) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Card(
                        modifier = Modifier.safeDrawingPadding().padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(tr(lang, "¿Qué escribo al final en el expediente?", "What do I write in the record?"), fontWeight = FontWeight.Black)
                            Text(finalWritingV7(overlay, lang), modifier = Modifier.padding(top = 8.dp, bottom = 10.dp))
                            Button(onClick = { writingHelp = false }) { Text(tr(lang, "Cerrar", "Close")) }
                        }
                    }
                }
            }
        }
    }

    BackHandler(enabled = overlay != V7Overlay.NONE) {
        if (writingHelp) writingHelp = false
        else if (overlay == V7Overlay.INTAKE) overlay = V7Overlay.NONE
        else overlay = V7Overlay.INTAKE
    }
}

@Composable
private fun AdaptiveFloatingActionsV17(
    lang: String,
    onWriting: () -> Unit,
    onIntake: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier.safeDrawingPadding().padding(10.dp)) {
        val compact = maxWidth < 330.dp || LocalDensity.current.fontScale >= 1.25f
        if (compact) {
            Column(horizontalAlignment = Alignment.End) {
                OutlinedButton(onClick = onWriting) { Text("✍️") }
                OutlinedButton(onClick = onIntake) { Text("↩ 📋") }
            }
        } else {
            Row {
                OutlinedButton(onClick = onWriting) { Text("✍️ ${tr(lang, "Qué escribir", "What to write")}") }
                OutlinedButton(onClick = onIntake) { Text("↩ ${tr(lang, "Ingreso", "Intake")}") }
            }
        }
    }
}

private fun finalWritingV7(screen: V7Overlay, lang: String): String {
    val es = when (screen) {
        V7Overlay.PULPAL_APICAL -> "OD ___: diagnóstico pulpar más compatible con ___; diagnóstico periapical más compatible con ___; sustentado por ___. Si faltan pruebas, indícalo."
        V7Overlay.IPC -> "IPC: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Añade los hallazgos periodontales relevantes."
        V7Overlay.IHOS -> "IHOS = ___ (ID-S ___ + IC-S ___). Añade la interpretación obtenida."
        V7Overlay.OLEARY -> "O'Leary = ___%. Registra el porcentaje final y las superficies relevantes con placa."
        V7Overlay.ICDAS -> "Registra ICDAS por superficie y, si tu formato pide un valor por diente, conserva el código de mayor severidad entre sus caras."
        V7Overlay.ENDO -> "OD ___; diagnóstico pulpar ___; periapical ___; procedimiento ___; referencia coronal ___; longitudes de trabajo por conducto ___; irrigación según protocolo institucional; sellado/restauración ___; indicaciones y seguimiento ___."
        V7Overlay.PROSTHETIC -> "Diagnóstico protésico: arco ___; Kennedy ___ mod. ___ cuando aplique; áreas edéntulas ___; pilares candidatos ___; tipo de prótesis ___; material ___; diseño (descansos/conectores/retención o pilares/pónticos) ___; etapa realizada ___; indicaciones y seguimiento ___."
        V7Overlay.MUCOSA -> "Mucosas: describe cada zona explorada. Si hay lesión, registra localización, tamaño, color, superficie, bordes, consistencia y síntomas."
        V7Overlay.VITALS -> "TA ___/___ mmHg · FC ___ lpm · FR ___ rpm · T ___ °C · peso ___ kg · talla ___ m · IMC ___ cuando corresponda."
        else -> "Resume el resultado del módulo, su interpretación, los hallazgos que lo sustentan y lo que falta para completar la valoración."
    }
    if (lang != "en") return es
    return when (screen) {
        V7Overlay.PULPAL_APICAL -> "Tooth ___: pulpal diagnosis most compatible with ___; apical diagnosis most compatible with ___; supported by ___. State missing tests."
        V7Overlay.IPC -> "CPI: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Add relevant periodontal findings."
        V7Overlay.IHOS -> "OHI-S = ___ (DI-S ___ + CI-S ___). Add the resulting interpretation."
        V7Overlay.ENDO -> "Tooth ___; pulpal diagnosis ___; apical diagnosis ___; procedure ___; coronal reference ___; working lengths by canal ___; irrigation per institutional protocol; coronal seal/restoration ___; instructions and follow-up ___."
        V7Overlay.PROSTHETIC -> "Prosthodontic diagnosis: arch ___; Kennedy ___ mod. ___ when applicable; edentulous areas ___; candidate abutments ___; prosthesis type ___; material ___; design (rests/connectors/retention or abutments/pontics) ___; stage completed ___; instructions and follow-up ___."
        else -> "Summarize the module result, interpretation, supporting findings and missing information needed to complete the assessment."
    }
}
