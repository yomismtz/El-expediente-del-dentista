package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalDecisionV1
import com.yomismtz.expedientedeldentista.clinical.ClinicalEvent
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.evaluateClinicalDecisionV1

private fun decisionTextV1(d:ClinicalDecisionV1,lang:String):String =
    if(lang=="en") d.detailEn else d.detailEs

@Composable
fun ClinicalDecisionDashboardV1(
    lang:String, session:EducationalSession, age:Int, sex:String, sys:Int?, dia:Int?, rr:Int?, hr:Int?,
    spo2:Int?, glucose:Int?, temp:Double?, bmi:Double?, signs:Set<String>, pain:Int?,
    onSessionChanged:(EducationalSession)->Unit
) {
    val p=session.profile
    val d=evaluateClinicalDecisionV1(age,sex,sys,dia,rr,hr,spo2,glucose,temp,bmi,signs,pain,p)
    var cardiovascular by remember(p.cardiovascularHistory){mutableStateOf(p.cardiovascularHistory)}
    var antithrombotic by remember(p.anticoagulantsAntiplatelets){mutableStateOf(p.anticoagulantsAntiplatelets)}
    var rhythmMeds by remember(p.betaBlockersAntiarrhythmics){mutableStateOf(p.betaBlockersAntiarrhythmics)}
    var diabetesTreatment by remember(p.diabetesTreatment){mutableStateOf(p.diabetesTreatment)}
    var hepaticRenal by remember(p.hepaticRenalDisease){mutableStateOf(p.hepaticRenalDisease)}
    var pregnancy by remember(p.pregnancyStatus){mutableStateOf(p.pregnancyStatus)}

    ResponsiveSectionV17(
        tr(lang,"9 · Contexto médico para decisiones","9 · Medical context for decisions"),
        tr(lang,"Estos datos no diagnostican ni autorizan procedimientos. Sirven para detectar factores que pueden cambiar el manejo, la hemostasia o la selección anestésica.","These data do not diagnose or clear procedures. They help identify factors that may change management, hemostasis or anesthetic selection.")
    ) {
        OutlinedTextField(cardiovascular,{cardiovascular=it},label={Text(tr(lang,"Antecedentes cardiovasculares","Cardiovascular history"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(antithrombotic,{antithrombotic=it},label={Text(tr(lang,"Anticoagulantes / antiagregantes","Anticoagulants / antiplatelets"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(rhythmMeds,{rhythmMeds=it},label={Text(tr(lang,"Betabloqueadores / antiarrítmicos","Beta-blockers / antiarrhythmics"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(diabetesTreatment,{diabetesTreatment=it},label={Text(tr(lang,"Diabetes y tratamiento","Diabetes and treatment"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(hepaticRenal,{hepaticRenal=it},label={Text(tr(lang,"Enfermedad hepática / renal","Hepatic / renal disease"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(pregnancy,{pregnancy=it},label={Text(tr(lang,"Embarazo / lactancia / estado relevante","Pregnancy / lactation / relevant status"))},modifier=Modifier.fillMaxWidth())
        Button(onClick={
            val np=p.copy(
                cardiovascularHistory=cardiovascular.trim(),
                anticoagulantsAntiplatelets=antithrombotic.trim(),
                betaBlockersAntiarrhythmics=rhythmMeds.trim(),
                diabetesTreatment=diabetesTreatment.trim(),
                hepaticRenalDisease=hepaticRenal.trim(),
                pregnancyStatus=pregnancy.trim()
            )
            val detail=if(lang=="en") "Medical context updated" else "Contexto médico actualizado"
            onSessionChanged(session.copy(profile=np,clinicalEvents=session.clinicalEvents + ClinicalEvent(System.currentTimeMillis(),"medical_context",detail)))
        },modifier=Modifier.fillMaxWidth()){ Text(tr(lang,"Guardar contexto médico","Save medical context")) }
    }

    ResponsiveSectionV17(tr(lang,"10 · Semáforo clínico","10 · Clinical traffic light"),tr(lang,"Resultado operativo de los datos capturados; no sustituye la valoración clínica.","Operational result of captured data; it does not replace clinical assessment.")) {
        Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){
            Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
                Text(tr(lang,"Datos utilizados en esta decisión","Data used for this decision"),fontWeight=FontWeight.Bold)
                Text("Edad: $age · Sexo: ${sex.ifBlank{"—"}} · IMC: ${bmi ?: "—"}")
                Text("TA: ${sys ?: "—"}/${dia ?: "—"} · FC: ${hr ?: "—"} · FR: ${rr ?: "—"}")
                Text("SpO₂: ${spo2 ?: "—"}% · Glucosa: ${glucose ?: "—"} mg/dL · Temp: ${temp ?: "—"} °C")
                Text("Dolor: ${pain ?: "—"}/10 · Signos: ${signs.joinToString(", ").ifBlank{"ninguno"}}",style=MaterialTheme.typography.bodySmall)
            }
        }
        Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=when(d.status){"RED"->MaterialTheme.colorScheme.errorContainer;"YELLOW"->MaterialTheme.colorScheme.tertiaryContainer;else->MaterialTheme.colorScheme.secondaryContainer})){
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                Text(if(lang=="en")d.titleEn else d.titleEs,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
                Text(decisionTextV1(d,lang))
            }
        }
    }

    ResponsiveSectionV17(tr(lang,"10.1 · Alertas inteligentes","10.1 · Smart alerts"),tr(lang,"Cada alerta muestra el dato que la activa y la acción de verificación correspondiente.","Each alert shows the trigger and the corresponding verification action.")) {
        if (d.alerts.isEmpty()) {
            Text(tr(lang,"✓ No hay alertas adicionales con los datos registrados.","✓ No additional alerts with the recorded data."))
        } else {
            d.alerts.forEach { a ->
                val container = if (a.severity == "RED") MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer
                Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=container)){
                    Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
                        Text((if(a.severity=="RED")"🚨 " else "⚠️ ")+a.triggerEs,fontWeight=FontWeight.Bold)
                        Text(a.actionEs)
                    }
                }
            }
        }
    }

    ResponsiveSectionV17(tr(lang,"11 · Resumen operativo","11 · Operational summary"),tr(lang,"Sólo se muestran hasta 3 líneas de manejo compatibles con los datos. No son una autorización automática.","Only up to 3 management lines compatible with the data are shown. They are not automatic clearance.")) {
        if(d.treatments.isEmpty()) Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer)){Text(tr(lang,"🚨 No hay tratamiento dental electivo compatible con los datos actuales.","🚨 No elective dental treatment is compatible with the current data."),Modifier.padding(12.dp),fontWeight=FontWeight.Bold)}
        else d.treatments.forEachIndexed { index,t ->
            Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){ Text("${index+1}. $t",Modifier.padding(12.dp),fontWeight=FontWeight.Bold) }
        }
        if(d.restrictionsEs.isNotEmpty()){
            Text(tr(lang,"Restricciones / precauciones","Restrictions / precautions"),fontWeight=FontWeight.Black)
            (if(lang=="en")d.restrictionsEn else d.restrictionsEs).forEach{Text("• $it")}
        }
        Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){
            Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                Text(tr(lang,"Anestesia local","Local anesthesia"),fontWeight=FontWeight.Black)
                Text(if(lang=="en")d.anestheticEn else d.anestheticEs)
            }
        }
        Text(tr(lang,"La elección del anestésico debe verificarse contra peso, edad, alergias, comorbilidades, medicamentos, técnica, concentración y ficha técnica. No modificar anticoagulantes/antiagregantes por cuenta propia.","Verify anesthetic choice against weight, age, allergies, comorbidities, medications, technique, concentration and labeling. Do not independently alter anticoagulant/antiplatelet therapy."),style=MaterialTheme.typography.bodySmall)
        Button(onClick={
            val detail=if(lang=="en") "${d.titleEn}. Treatments: ${d.treatments.joinToString(" | ")}. Anesthesia: ${d.anestheticEn}" else "${d.titleEs}. Tratamientos: ${d.treatments.joinToString(" | ")}. Anestesia: ${d.anestheticEs}"
            onSessionChanged(session.copy(clinicalEvents=session.clinicalEvents + ClinicalEvent(System.currentTimeMillis(),"clinical_assessment",detail)))
        },modifier=Modifier.fillMaxWidth()){ Text(tr(lang,"Registrar valoración en bitácora","Log assessment")) }
        Button(onClick={
            val vitalDetail = listOf(
                "Edad=" + age, "Sexo=" + sex,
                "TA=" + (sys ?: "—") + "/" + (dia ?: "—"),
                "FR=" + (rr ?: "—"), "FC=" + (hr ?: "—"),
                "SpO₂=" + (spo2 ?: "—") + "%",
                "Glucosa=" + (glucose ?: "—") + " mg/dL",
                "Temp=" + (temp ?: "—") + " °C",
                "IMC=" + (bmi ?: "—"), "Dolor=" + (pain ?: "—") + "/10",
                "Signos=" + signs.joinToString(", ").ifBlank { "ninguno registrado" }
            ).joinToString(" · ")
            onSessionChanged(session.copy(
                clinicalEvents=session.clinicalEvents + ClinicalEvent(
                    System.currentTimeMillis(),
                    "vital_signs_snapshot",
                    vitalDetail
                )
            ))
        },modifier=Modifier.fillMaxWidth()){ Text(tr(lang,"Registrar signos vitales en bitácora","Log vital signs")) }
    }

    ResponsiveSectionV17(tr(lang,"12 · Bitácora clínica","12 · Clinical log"),tr(lang,"Registra decisiones educativas y cambios relevantes para conservar trazabilidad dentro del expediente.","Log educational decisions and relevant changes for traceability within the record.")) {
        if(session.clinicalEvents.isEmpty()) Text(tr(lang,"Sin eventos registrados todavía.","No events recorded yet."))
        else session.clinicalEvents.takeLast(8).asReversed().forEach { e ->
            Card(Modifier.fillMaxWidth()){
                Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(3.dp)){
                    Text(java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",java.util.Locale.getDefault()).format(java.util.Date(e.timestamp)),fontWeight=FontWeight.Bold)
                    Text(e.type)
                    Text(e.detail,style=MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
