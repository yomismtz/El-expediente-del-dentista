package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences

private enum class IntakeOverlay {
    NONE, INTAKE, IDENTIFICATION, HISTORY, GENERAL_INSPECTION, HEAD_NECK,
    VITALS, ATM, OCCLUSION, MUCOSA, CPOD, PERIODONTAL, PULPAL, PROSTHETIC
}

@Composable
fun AppRootV6(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var overlay by remember { mutableStateOf(IntakeOverlay.NONE) }
    var writingHelp by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        AppRootV5(
            preferences = preferences,
            onPreferencesChanged = onPreferencesChanged,
            onLanguageChanged = onLanguageChanged,
            session = session,
            onSessionChanged = onSessionChanged
        )

        if (preferences.onboardingComplete && overlay == IntakeOverlay.NONE) {
            Button(
                onClick = { overlay = IntakeOverlay.INTAKE },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .safeDrawingPadding()
                    .padding(10.dp)
            ) {
                Text("📋 ${tr(preferences.languageTag,"Nota de ingreso","Intake note")}")
            }
        }

        if (overlay != IntakeOverlay.NONE) {
            Surface(Modifier.fillMaxSize(), tonalElevation = 8.dp) {
                when (overlay) {
                    IntakeOverlay.INTAKE -> IntakeInteractiveV3Screen(
                        lang = preferences.languageTag,
                        onIdentification = { overlay = IntakeOverlay.IDENTIFICATION },
                        onHistory = { overlay = IntakeOverlay.HISTORY },
                        onGeneralInspection = { overlay = IntakeOverlay.GENERAL_INSPECTION },
                        onHeadNeck = { overlay = IntakeOverlay.HEAD_NECK },
                        onVitals = { overlay = IntakeOverlay.VITALS },
                        onAtm = { overlay = IntakeOverlay.ATM },
                        onOcclusion = { overlay = IntakeOverlay.OCCLUSION },
                        onMucosa = { overlay = IntakeOverlay.MUCOSA },
                        onCpod = { overlay = IntakeOverlay.CPOD },
                        onPeriodontal = { overlay = IntakeOverlay.PERIODONTAL },
                        onPulpal = { overlay = IntakeOverlay.PULPAL },
                        onProsthetic = { overlay = IntakeOverlay.PROSTHETIC },
                        onBack = { overlay = IntakeOverlay.NONE }
                    )
                    IntakeOverlay.IDENTIFICATION -> IdentificationScreen(preferences.languageTag, session, onSessionChanged) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.HISTORY -> HistoryScreen(preferences.languageTag, session, onSessionChanged) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.GENERAL_INSPECTION -> GeneralInspectionV24Screen(preferences.languageTag, session, onSessionChanged) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.HEAD_NECK -> HeadNeckExplorationV24Screen(preferences.languageTag, session, onSessionChanged) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.VITALS -> VitalsInteractiveScreen(preferences.languageTag) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.ATM -> AtmScreen(preferences.languageTag) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.OCCLUSION -> OcclusionScreen(preferences.languageTag) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.MUCOSA -> MucosaExamV24Screen(preferences.languageTag, session, onSessionChanged) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.CPOD -> CpodScreen(preferences.languageTag, session) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.PERIODONTAL -> PeriodontogramScreen(preferences.languageTag, session, onSessionChanged) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.PULPAL -> PulpalScreen(preferences.languageTag, session, onSessionChanged) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.PROSTHETIC -> ProstheticSheetScreen(preferences.languageTag) { overlay = IntakeOverlay.INTAKE }
                    IntakeOverlay.NONE -> Unit
                }
            }

            if (overlay !in listOf(IntakeOverlay.INTAKE, IntakeOverlay.NONE)) {
                OutlinedButton(
                    onClick = { writingHelp = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .safeDrawingPadding()
                        .padding(14.dp)
                ) {
                    Text("✍️ ${tr(preferences.languageTag,"Qué escribir","What to write")}")
                }
            }

            if (writingHelp) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Card(
                        modifier = Modifier
                            .safeDrawingPadding()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFCFF)),
                        border = BorderStroke(1.dp, Color(0xFF2EB9B1))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                tr(preferences.languageTag,"¿Qué escribo al final en el expediente?","What do I write in the record?"),
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF43235F)
                            )
                            Text(writeBackText(overlay, preferences.languageTag), modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
                            Button(onClick = { writingHelp = false }) { Text(tr(preferences.languageTag,"Cerrar","Close")) }
                        }
                    }
                }
            }
        }
    }

    BackHandler(enabled = overlay != IntakeOverlay.NONE) {
        if (writingHelp) writingHelp = false
        else if (overlay == IntakeOverlay.INTAKE) overlay = IntakeOverlay.NONE
        else overlay = IntakeOverlay.INTAKE
    }
}

private fun writeBackText(screen: IntakeOverlay, lang: String): String {
    val es = when (screen) {
        IntakeOverlay.IDENTIFICATION -> "Registra únicamente los datos solicitados por el formato físico. Motivo de consulta y padecimiento actual deben ser breves, cronológicos y fieles a lo referido."
        IntakeOverlay.HISTORY -> "Resume antecedentes positivos, estado/control actual, tratamiento o medicamentos relevantes y la clasificación ASA sustentada. Si faltan datos, indícalo y confirma con supervisión docente."
        IntakeOverlay.GENERAL_INSPECTION -> "Exploración general: edad aparente ___; marcha ___; facies ___; actitud/cooperación ___; constitución/habitus ___; movimientos anormales ___; conciencia/orientación ___; actitud psicológica observable ___; cuidado personal ___."
        IntakeOverlay.HEAD_NECK -> "Cabeza y cuello: cráneo ___; cara/perfil/simetría ___; músculos de expresión ___; músculos masticatorios ___; cuello ___; cadenas ganglionares ___. Para un ganglio palpable documenta lado, tamaño, dolor, consistencia y movilidad."
        IntakeOverlay.VITALS -> "Ejemplo de estructura: TA ___/___ mmHg · FC ___ lpm · FR ___ rpm · T ___ °C · peso ___ kg · talla ___ m · IMC ___ cuando corresponda."
        IntakeOverlay.ATM -> "Ejemplo: “ATM: apertura ___ mm, trayectoria ___, ruidos ___, dolor ___”. Los músculos masticatorios se documentan en la exploración de cabeza y cuello."
        IntakeOverlay.OCCLUSION -> "Ejemplo: “Dentición ___; relación molar ___; canina ___; overjet ___ mm; overbite ___%; líneas medias ___; mordida abierta/cruzada/profunda: ___”."
        IntakeOverlay.MUCOSA -> "Ejemplo: “Mucosas: labios ___, carrillos ___, encía ___, lengua ___, piso de boca ___, paladar ___, orofaringe ___”. Si existe lesión: localización exacta + tipo + tamaño + color + superficie + bordes + consistencia + síntomas + evolución."
        IntakeOverlay.CPOD -> "Registra C, P y O (o c, e, o en dentición temporal), el total y los dientes que sustentan cada componente. No mezcles denticiones en un mismo índice."
        IntakeOverlay.PERIODONTAL -> "Resume sondaje, sangrado, placa, cálculo, recesión, movilidad y furcación según el examen realizado. Si usas IPC/IHOS, anota también su resultado e interpretación."
        IntakeOverlay.PULPAL -> "Ejemplo: “OD ___: diagnóstico pulpar más compatible con ___; diagnóstico periapical ___; sustentado por ___”. Mantén pulpar y periapical separados y señala pruebas faltantes si las hay."
        IntakeOverlay.PROSTHETIC -> "Ejemplo: “Arcada ___: dientes ausentes ___; espacios edéntulos ___; clasificación de Kennedy ___, modificación ___”. Confirma la clasificación con las reglas de Applegate."
        else -> "Resume el hallazgo final del módulo, su interpretación y los datos que lo sustentan."
    }
    val en = when (screen) {
        IntakeOverlay.IDENTIFICATION -> "Record only the data requested by the physical form. Reason for visit and current condition should be brief, chronological and faithful to what was reported."
        IntakeOverlay.HISTORY -> "Summarize positive history, current control/status, relevant treatment or medicines, and the supported ASA class. If information is missing, state it and confirm with faculty supervision."
        IntakeOverlay.GENERAL_INSPECTION -> "General inspection: apparent age ___; gait ___; facies ___; attitude/cooperation ___; body habitus ___; abnormal movements ___; consciousness/orientation ___; observable psychological attitude ___; personal care ___."
        IntakeOverlay.HEAD_NECK -> "Head and neck: cranium ___; face/profile/symmetry ___; muscles of facial expression ___; masticatory muscles ___; neck ___; lymph-node chains ___. For a palpable node record side, size, tenderness, consistency and mobility."
        IntakeOverlay.VITALS -> "Example structure: BP ___/___ mmHg · HR ___ bpm · RR ___ rpm · T ___ °C · weight ___ kg · height ___ m · BMI ___ when appropriate."
        IntakeOverlay.ATM -> "Example: “TMJ: opening ___ mm, path ___, sounds ___, pain ___”. Document masticatory muscles in the head-and-neck examination."
        IntakeOverlay.OCCLUSION -> "Example: “Dentition ___; molar relation ___; canine relation ___; overjet ___ mm; overbite ___%; midlines ___; open/cross/deep bite: ___”."
        IntakeOverlay.MUCOSA -> "Example: “Mucosa: lips ___, cheeks ___, gingiva ___, tongue ___, floor of mouth ___, palate ___, oropharynx ___”. If a lesion is present: exact site + type + size + color + surface + borders + consistency + symptoms + evolution."
        IntakeOverlay.CPOD -> "Record D, M and F (or d, e, f in primary dentition), the total, and the teeth supporting each component. Do not mix dentitions in the same index."
        IntakeOverlay.PERIODONTAL -> "Summarize probing, bleeding, plaque, calculus, recession, mobility and furcation according to the examination. If CPI/OHI-S are used, include their result and interpretation."
        IntakeOverlay.PULPAL -> "Example: “Tooth ___: pulpal diagnosis most compatible with ___; apical diagnosis ___; supported by ___”. Keep pulpal and apical diagnoses separate and state missing tests when applicable."
        IntakeOverlay.PROSTHETIC -> "Example: “Arch ___: missing teeth ___; edentulous spaces ___; Kennedy class ___, modification ___”. Confirm classification using Applegate rules."
        else -> "Summarize the module's final finding, interpretation and supporting data."
    }
    return if (lang == "en") en else es
}
