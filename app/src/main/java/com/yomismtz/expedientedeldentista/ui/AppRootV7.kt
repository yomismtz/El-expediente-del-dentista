package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
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
    preferences:AppPreferences,
    onPreferencesChanged:(AppPreferences)->Unit,
    onLanguageChanged:(String)->Unit,
    session:EducationalSession,
    onSessionChanged:(EducationalSession)->Unit,
    onOpenSettings:(()->Unit)?=null
) {
    var overlay by remember{ mutableStateOf(V7Overlay.NONE) }
    var writingHelp by remember{ mutableStateOf(false) }
    val lang=preferences.languageTag
    val backToIntake={overlay=V7Overlay.INTAKE}

    Column(Modifier.fillMaxSize()) {
        GlobalBar19(lang,{overlay=V7Overlay.INTAKE},onOpenSettings)
        Box(Modifier.weight(1f).fillMaxWidth()) {
            AdaptiveBaseRootV17(preferences,onPreferencesChanged,onLanguageChanged,session,onSessionChanged)

            if(overlay!=V7Overlay.NONE) {
                Surface(Modifier.fillMaxSize(),tonalElevation=8.dp) {
                    when(overlay) {
                        V7Overlay.INTAKE -> IntakeInteractiveV3Screen(
                            lang,
                            onIdentification={overlay=V7Overlay.IDENTIFICATION},
                            onHistory={overlay=V7Overlay.HISTORY},
                            onVitals={overlay=V7Overlay.VITALS},
                            onAtm={overlay=V7Overlay.ATM},
                            onOcclusion={overlay=V7Overlay.OCCLUSION},
                            onMucosa={overlay=V7Overlay.MUCOSA},
                            onCpod={overlay=V7Overlay.CPOD},
                            onPeriodontal={overlay=V7Overlay.PERIODONTAL},
                            onPulpal={overlay=V7Overlay.PULPAL_APICAL},
                            onProsthetic={overlay=V7Overlay.PROSTHETIC},
                            onBack={overlay=V7Overlay.NONE}
                        )
                        V7Overlay.HUB -> ExamHubV1Screen(lang,{overlay=it},backToIntake)
                        V7Overlay.IDENTIFICATION -> IdentificationScreen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.HISTORY -> HistoryScreen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.VITALS -> VitalsInteractiveV19Screen(lang,backToIntake)
                        V7Overlay.ATM -> AtmScreen(lang,backToIntake)
                        V7Overlay.OCCLUSION -> OcclusionInteractiveV19Screen(lang,backToIntake)
                        V7Overlay.MUCOSA -> MucosaInteractiveV19Screen(lang,backToIntake)
                        V7Overlay.AUXILIARIES -> AuxiliariesInteractiveScreen(lang,backToIntake)
                        V7Overlay.ODONTOGRAM -> OdontogramResponsiveV17Screen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.ICDAS -> IcdasScreen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.CPOD -> CpodInteractiveV19Screen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.OLEARY -> OlearyScreen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.IPC -> IpcResponsiveV17Screen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.IHOS -> IhosResponsiveV17Screen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.PERIODONTAL -> PeriodontogramScreen(lang,session,onSessionChanged,backToIntake)
                        V7Overlay.POSTURE -> PostureVisualScreen(lang,backToIntake)
                        V7Overlay.PULPAL_APICAL -> PulpalPeriapicalInteractiveV2Screen(lang,session,onSessionChanged,{overlay=V7Overlay.ENDO},backToIntake)
                        V7Overlay.ENDO -> EndodonticInteractiveV2Screen(lang,session,{overlay=V7Overlay.PULPAL_APICAL},{overlay=V7Overlay.PULPAL_APICAL},backToIntake)
                        V7Overlay.PROSTHETIC -> ProstheticResponsiveV17Screen(lang,backToIntake)
                        V7Overlay.SURGICAL -> SurgicalSheetScreen(lang,backToIntake)
                        V7Overlay.CONSENT -> ConsentTeachingScreen(lang,backToIntake)
                        V7Overlay.EVOLUTION -> EvolutionScreen(lang,session,backToIntake)
                        V7Overlay.NONE -> Unit
                    }
                }

                if(overlay==V7Overlay.INTAKE) {
                    OutlinedButton(onClick={overlay=V7Overlay.HUB},modifier=Modifier.align(Alignment.BottomStart).safeDrawingPadding().padding(12.dp)) {
                        Text("🧭 ${tr(lang,"Todos los exámenes","All examinations")}")
                    }
                }

                if(overlay !in listOf(V7Overlay.NONE,V7Overlay.INTAKE,V7Overlay.HUB)) {
                    FloatingActions19(lang,{writingHelp=true},backToIntake,Modifier.align(Alignment.BottomEnd))
                }

                if(writingHelp) {
                    Box(Modifier.fillMaxSize(),contentAlignment=Alignment.BottomCenter) {
                        Card(modifier=Modifier.safeDrawingPadding().padding(16.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,MaterialTheme.colorScheme.secondary)) {
                            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                                Text(tr(lang,"¿Qué escribo al final en el expediente?","What do I write in the record?"),fontWeight=FontWeight.Black)
                                Text(finalWriting19(overlay,lang))
                                Button(onClick={writingHelp=false}){Text(tr(lang,"Cerrar","Close"))}
                            }
                        }
                    }
                }
            }
        }
    }

    BackHandler(enabled=overlay!=V7Overlay.NONE) {
        if(writingHelp) writingHelp=false
        else if(overlay==V7Overlay.INTAKE) overlay=V7Overlay.NONE
        else overlay=V7Overlay.INTAKE
    }
}

@Composable
private fun GlobalBar19(lang:String,onIntake:()->Unit,onSettings:(()->Unit)?) {
    Surface(color=MaterialTheme.colorScheme.surface,tonalElevation=4.dp,shadowElevation=2.dp) {
        BoxWithConstraints(Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal=10.dp,vertical=7.dp)) {
            val compact=maxWidth<380.dp || LocalDensity.current.fontScale>=1.20f
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                Button(onClick=onIntake,modifier=Modifier.weight(1f)) {
                    Text(if(compact)"📋 ${tr(lang,"Ingreso","Intake")}" else "📋 ${tr(lang,"Nota de ingreso","Intake note")}")
                }
                if(onSettings!=null) {
                    OutlinedButton(onClick=onSettings,modifier=Modifier.weight(1f)) {
                        Text(if(compact)"⚙ ${tr(lang,"Ajustes","Settings")}" else "⚙ ${tr(lang,"Configuración","Settings")}")
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingActions19(lang:String,onWriting:()->Unit,onIntake:()->Unit,modifier:Modifier=Modifier) {
    BoxWithConstraints(modifier.safeDrawingPadding().padding(10.dp)) {
        val compact=maxWidth<330.dp || LocalDensity.current.fontScale>=1.25f
        if(compact) {
            Column(horizontalAlignment=Alignment.End) {
                OutlinedButton(onClick=onWriting){Text("✍️")}
                OutlinedButton(onClick=onIntake){Text("↩ 📋")}
            }
        } else {
            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                OutlinedButton(onClick=onWriting){Text("✍️ ${tr(lang,"Qué escribir","What to write")}")}
                OutlinedButton(onClick=onIntake){Text("↩ ${tr(lang,"Ingreso","Intake")}")}
            }
        }
    }
}

private fun finalWriting19(screen:V7Overlay,lang:String):String {
    if(lang=="en") return when(screen) {
        V7Overlay.PULPAL_APICAL -> "Tooth ___: pulpal diagnosis most compatible with ___; apical diagnosis most compatible with ___; supported by ___. State missing tests."
        V7Overlay.IPC -> "CPI: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Add relevant periodontal findings."
        V7Overlay.IHOS -> "OHI-S = ___ (DI-S ___ + CI-S ___). Add the interpretation."
        V7Overlay.OLEARY -> "O'Leary = ___%. Record final percentage and plaque-positive surfaces."
        V7Overlay.ICDAS -> "Record ICDAS by surface; if one value per tooth is required, use the highest surface code."
        V7Overlay.CPOD -> "DMFT/dmft: D/d=___, M/e=___, F/f=___; total=___."
        V7Overlay.VITALS -> "BP ___/___ mmHg · HR ___ bpm · RR ___ rpm · T ___ °C · capillary glucose ___ mg/dL (fasting/premeal/postmeal/random) · weight ___ kg · height ___ · BMI ___."
        V7Overlay.ENDO -> "Tooth ___; pulpal ___; apical ___; procedure ___; coronal reference ___; working lengths ___; irrigation per protocol; restoration ___; follow-up ___."
        V7Overlay.PROSTHETIC -> "Arch ___; Kennedy ___ mod. ___ when applicable; edentulous areas ___; prosthesis/material/design ___; stage ___; instructions ___."
        V7Overlay.MUCOSA -> "Describe each examined mucosal region. For a lesion record site, size, color, surface, borders, consistency and symptoms."
        else -> "Summarize result, interpretation, supporting findings and missing information."
    }
    return when(screen) {
        V7Overlay.PULPAL_APICAL -> "OD ___: diagnóstico pulpar más compatible con ___; diagnóstico periapical más compatible con ___; sustentado por ___. Indica pruebas faltantes."
        V7Overlay.IPC -> "IPC: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Añade hallazgos periodontales relevantes."
        V7Overlay.IHOS -> "IHOS = ___ (ID-S ___ + IC-S ___). Añade la interpretación obtenida."
        V7Overlay.OLEARY -> "O'Leary = ___%. Registra porcentaje final y superficies con placa."
        V7Overlay.ICDAS -> "Registra ICDAS por superficie; si el formato pide un valor por diente, usa el mayor código entre sus caras."
        V7Overlay.CPOD -> "CPOD/ceod: C/c=___, P/e=___, O/o=___; total=___."
        V7Overlay.VITALS -> "TA ___/___ mmHg · FC ___ lpm · FR ___ rpm · T ___ °C · glucosa capilar ___ mg/dL (ayuno/preprandial/posprandial/casual) · peso ___ kg · talla ___ · IMC ___."
        V7Overlay.ENDO -> "OD ___; diagnóstico pulpar ___; periapical ___; procedimiento ___; referencia coronal ___; longitudes ___; irrigación según protocolo; restauración ___; seguimiento ___."
        V7Overlay.PROSTHETIC -> "Arco ___; Kennedy ___ mod. ___ cuando aplique; áreas edéntulas ___; tipo/material/diseño ___; etapa ___; indicaciones ___."
        V7Overlay.MUCOSA -> "Describe cada zona de mucosa. Si hay lesión: localización, tamaño, color, superficie, bordes, consistencia y síntomas."
        else -> "Resume resultado, interpretación, hallazgos que lo sustentan y datos faltantes."
    }
}
