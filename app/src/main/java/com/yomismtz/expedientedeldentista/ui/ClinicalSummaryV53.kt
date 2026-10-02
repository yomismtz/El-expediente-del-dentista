package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.ClinicalSafetyEngine
import com.yomismtz.expedientedeldentista.clinical.ClinicalAlert
import com.yomismtz.expedientedeldentista.clinical.evaluateClinicalDecisionV1

@Composable
fun ClinicalSummaryV53Screen(lang:String,session:EducationalSession,onBack:()->Unit) {
    val p=session.profile
    val teeth=session.teeth
    val present=teeth.count { (_,it) -> it.status.name !in setOf("MISSING_CARIES","MISSING_OTHER") }
    val caries=teeth.values.count { it.icdas>0 }
    val perio=if(session.periodontogram.isEmpty()) "Pendiente" else "Registrado"
    val pulpal=if(session.pulpal.tooth>0) "Registrado · OD "+session.pulpal.tooth else "Pendiente"
    val history=if(session.history.diseases.values.any { it.present }) "Con antecedentes seleccionados" else "Sin antecedentes activos registrados"
    val cpod=ClinicalEngines.cpod(session.teeth,false)
    val ceod=ClinicalEngines.cpod(session.teeth,true)
    val alerts=ClinicalSafetyEngine.alerts(session)
    val linkedTreatment=teeth.filterValues { it.diagnosisId!=null || it.treatmentId!=null }.keys.sorted()
    val checks: List<Pair<String, Boolean>> = listOf(
        "Identificación" to (p.patientInitials.isNotBlank() && p.age.isNotBlank() && p.sex.isNotBlank()),
        "Motivo / anamnesis" to (p.reasonForVisit.isNotBlank()),
        "Signos vitales" to (p.bloodPressure.isNotBlank() || p.heartRate.isNotBlank() || p.temperature.isNotBlank()),
        "Antecedentes sistémicos" to (session.history.diseases.isNotEmpty() || session.history.tobaccoAlcohol.isNotBlank()),
        "Odontograma" to (teeth.isNotEmpty()),
        "Periodontograma" to (session.periodontogram.isNotEmpty()),
        "Pulpar / periapical" to (session.pulpal.tooth > 0)
    )
    val done=checks.count{it.second}
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick=onBack){Text("‹")}
            Column(Modifier.weight(1f)){
                Text(tr(lang,"Resumen clínico y revisión final","Clinical summary and final review"),fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                Text(tr(lang,"Revisa antes de cerrar o exportar la práctica.","Review before closing or exporting the exercise."),style=MaterialTheme.typography.bodySmall)
            }
        }
        Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(done==checks.size) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer)){
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){
                Text("✓ "+done+" / "+checks.size+" "+tr(lang,"apartados principales completos","main sections complete"),fontWeight=FontWeight.Black)
                Text(if(done==checks.size) tr(lang,"Revisión básica completa.","Basic review complete.") else tr(lang,"Hay apartados pendientes; puedes corregirlos antes de finalizar.","Some sections are pending; you can correct them before finishing."))
            }
        }
        SummaryCardV53(tr(lang,"Identificación","Identification")){
            SummaryLineV53("Iniciales",p.patientInitials.ifBlank{"—"}); SummaryLineV53("Edad",p.age.ifBlank{"—"}); SummaryLineV53("Sexo",p.sex.ifBlank{"—"}); SummaryLineV53("Motivo",p.reasonForVisit.ifBlank{"—"})
        }
        SummaryCardV53(tr(lang,"Antecedentes y estado general","History and general status")){
            SummaryLineV53("ASA",session.history.asaClass.toString()); SummaryLineV53("Antecedentes",history); SummaryLineV53("Tabaco / alcohol",session.history.tobaccoAlcohol.ifBlank{"No registrado"}); SummaryLineV53("Medicamentos",p.medications.ifBlank{"No registrados"}); SummaryLineV53("Alergias",p.allergies.ifBlank{"No registradas"})
        }
        if(alerts.isNotEmpty()) {
            SummaryCardV53(tr(lang,"⚠️ Alertas y recordatorios de seguridad","⚠️ Safety alerts and reminders")) {
                alerts.forEach { alert ->
                    val icon=when(alert.severity){ClinicalAlert.Severity.URGENT->"🚨";ClinicalAlert.Severity.WARNING->"⚠️";ClinicalAlert.Severity.INFO->"ℹ️"}
                    Text("$icon ${alert.title(lang)}",fontWeight=FontWeight.Black)
                    Text(alert.detail(lang),style=MaterialTheme.typography.bodySmall)
                }
                Text(tr(lang,"Son recordatorios de verificación; no constituyen diagnósticos ni autorizaciones automáticas.","These are verification reminders; they are not diagnoses or automatic clearances."),style=MaterialTheme.typography.bodySmall)
            }
        }
        val triageSigns=p.clinicalSigns.split("|").filter{it.isNotBlank()}.toSet()
        val triage=evaluateClinicalDecisionV1(
            p.age.toIntOrNull() ?: 18,p.sex,p.bloodPressure.substringBefore("/").toIntOrNull(),p.bloodPressure.substringAfter("/", "").toIntOrNull(),
            p.respiratoryRate.toIntOrNull(),p.heartRate.toIntOrNull(),p.spo2.toIntOrNull(),p.glucose.toIntOrNull(),p.temperature.toDoubleOrNull(),
            p.bmi.toDoubleOrNull(),triageSigns,p.painScore.toIntOrNull(),p
        )
        SummaryCardV53(tr(lang,"Signos vitales y antropometría","Vital signs and anthropometrics")){
            SummaryLineV53("TA",p.bloodPressure.ifBlank{"—"})
            SummaryLineV53("FC",p.heartRate.ifBlank{"—"})
            SummaryLineV53("FR",p.respiratoryRate.ifBlank{"—"})
            SummaryLineV53("SpO₂",if(p.spo2.isBlank())"—" else p.spo2+" %")
            SummaryLineV53("Temperatura",if(p.temperature.isBlank())"—" else p.temperature+" °C")
            SummaryLineV53("Glucosa",if(p.glucose.isBlank())"—" else p.glucose+" mg/dL")
            SummaryLineV53("Peso / talla",if(p.weightKg.isBlank()&&p.heightCm.isBlank())"—" else p.weightKg+" kg / "+p.heightCm+" cm")
            SummaryLineV53("IMC",p.bmi.ifBlank{"—"})
            SummaryLineV53("Dolor",if(p.painScore.isBlank())"—" else p.painScore+"/10")
            SummaryLineV53("Signos",triageSigns.joinToString(", ").ifBlank{"Ninguno registrado"})
        }
        SummaryCardV53(tr(lang,"Triage, tratamientos y anestesia","Triage, treatments and anesthesia")){
            Text(if(lang=="en")triage.titleEn else triage.titleEs,fontWeight=FontWeight.Black)
            Text(if(lang=="en")triage.detailEn else triage.detailEs)
            Text(tr(lang,"Tratamientos compatibles registrados","Compatible treatments recorded"),fontWeight=FontWeight.Bold)
            Text(if(triage.treatments.isEmpty())"—" else triage.treatments.joinToString(" · "))
            Text(tr(lang,"Anestesia","Anesthesia"),fontWeight=FontWeight.Bold)
            Text(if(lang=="en")triage.anestheticEn else triage.anestheticEs)
            if(session.clinicalEvents.isNotEmpty()){
                Text(tr(lang,"Último evento","Latest event"),fontWeight=FontWeight.Bold)
                Text(session.clinicalEvents.last().detail,style=MaterialTheme.typography.bodySmall)
            }
        }
        SummaryCardV53(tr(lang,"Exploración y análisis","Examination and analysis")){
            SummaryLineV53("Dientes registrados",teeth.size.toString()); SummaryLineV53("Dientes presentes",present.toString()); SummaryLineV53("Dientes con ICDAS > 0",caries.toString()); SummaryLineV53("CPOD / DMFT","${cpod.total} (C ${cpod.carious} · P ${cpod.missing} · O ${cpod.filled})"); SummaryLineV53("ceod / dmft","${ceod.total} (c ${ceod.carious} · e ${ceod.missing} · o ${ceod.filled})"); SummaryLineV53("Periodontograma",perio); SummaryLineV53("Pulpar / periapical",pulpal); SummaryLineV53("IPC",if(session.ipcCodes.any{it!="0"}) "Registrado" else "Pendiente"); SummaryLineV53("O'Leary",if(session.oleary.isNotEmpty()) "Registrado" else "Pendiente"); SummaryLineV53("Tratamientos vinculados",if(linkedTreatment.isEmpty()) "Pendiente" else linkedTreatment.joinToString(", ") { "OD ${it}" })
        }
        SummaryCardV53(tr(lang,"Lista de revisión","Review checklist")){
            checks.forEach { (label,ok) -> Text((if(ok)"✓" else "○")+" "+label,fontWeight=if(ok) FontWeight.Medium else FontWeight.SemiBold) }
        }
        Card(Modifier.fillMaxWidth()){
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text(tr(lang,"Importante","Important"),fontWeight=FontWeight.Black);Text(tr(lang,"Este resumen organiza los datos capturados y no sustituye la revisión clínica o docente.","This summary organizes captured data and does not replace clinical or faculty review."),style=MaterialTheme.typography.bodySmall)}
        }
        Button(onClick=onBack,modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Volver al expediente","Back to record"))}
    }
}
@Composable private fun SummaryCardV53(title:String,content:@Composable ()->Unit){Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text(title,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium);content()}}}
@Composable private fun SummaryLineV53(label:String,value:String){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){Text(label,fontWeight=FontWeight.SemiBold,modifier=Modifier.weight(.42f));Text(value,modifier=Modifier.weight(.58f))}}
