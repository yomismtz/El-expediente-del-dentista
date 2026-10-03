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
import com.yomismtz.expedientedeldentista.clinical.ClinicalMeasurement
import com.yomismtz.expedientedeldentista.clinical.InformedConsent
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.evaluateClinicalDecisionV1
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines

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
            val clinicalMeasurement = ClinicalMeasurement(
                timestamp = System.currentTimeMillis(),
                systolic = sys, diastolic = dia, heartRate = hr, respiratoryRate = rr,
                spo2 = spo2, temperature = temp, glucose = glucose,
                weightKg = session.profile.weightKg.toDoubleOrNull(),
                bmi = bmi, pain = pain
            )
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
                clinicalMeasurements=session.clinicalMeasurements + clinicalMeasurement,
                clinicalEvents=session.clinicalEvents + ClinicalEvent(
                    System.currentTimeMillis(),
                    "vital_signs_snapshot",
                    vitalDetail
                )
            ))
        },modifier=Modifier.fillMaxWidth()){ Text(tr(lang,"Registrar signos vitales en bitácora","Log vital signs")) }
    }

    ResponsiveSectionV17(tr(lang,"15 · Consentimiento informado","15 · Informed consent"),tr(lang,"Registra qué procedimiento se explicó, sus beneficios, riesgos, alternativas y la decisión documentada.","Records the explained procedure, benefits, risks, alternatives and documented decision.")) {
        var procedure by remember { mutableStateOf("") }
        var site by remember { mutableStateOf("") }
        var diagnosis by remember { mutableStateOf("") }
        var benefits by remember { mutableStateOf("") }
        var risks by remember { mutableStateOf("") }
        var alternatives by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }
        var responsible by remember { mutableStateOf("") }
        var questions by remember { mutableStateOf(false) }
        var understood by remember { mutableStateOf(false) }
        var accepted by remember { mutableStateOf(false) }
        var declined by remember { mutableStateOf(false) }
        OutlinedTextField(procedure,{procedure=it},label={Text(tr(lang,"Procedimiento explicado","Procedure explained"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(site,{site=it},label={Text(tr(lang,"Diente / sitio","Tooth / site"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(diagnosis,{diagnosis=it},label={Text(tr(lang,"Diagnóstico explicado","Diagnosis explained"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(benefits,{benefits=it},label={Text(tr(lang,"Beneficios esperados","Expected benefits"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(risks,{risks=it},label={Text(tr(lang,"Riesgos y posibles complicaciones","Risks and possible complications"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(alternatives,{alternatives=it},label={Text(tr(lang,"Alternativas y opción de no realizarlo","Alternatives and option not to proceed"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(notes,{notes=it},label={Text(tr(lang,"Preguntas / notas","Questions / notes"))},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(responsible,{responsible=it},label={Text(tr(lang,"Responsable que documenta","Responsible person"))},modifier=Modifier.fillMaxWidth())
        androidx.compose.material3.Checkbox(questions,{questions=it}); Text(tr(lang,"Preguntas respondidas","Questions answered"))
        androidx.compose.material3.Checkbox(understood,{understood=it}); Text(tr(lang,"Comprensión documentada","Understanding documented"))
        androidx.compose.material3.Checkbox(accepted,{accepted=it; if(it) declined=false}); Text(tr(lang,"Acepta","Accepts"))
        androidx.compose.material3.Checkbox(declined,{declined=it; if(it) accepted=false}); Text(tr(lang,"Rechaza","Declines"))
        Button(onClick={
            if(procedure.isNotBlank() && understood && questions && (accepted || declined)) {
                val consent=InformedConsent(System.currentTimeMillis(),procedure.trim(),site.trim(),diagnosis.trim(),benefits.trim(),risks.trim(),alternatives.trim(),questions,understood,accepted,declined,notes.trim(),responsible.trim())
                val decision=if(accepted) "aceptado" else "rechazado"
                onSessionChanged(session.copy(informedConsents=session.informedConsents + consent,clinicalEvents=session.clinicalEvents + ClinicalEvent(System.currentTimeMillis(),"informed_consent",procedure.trim()+" · "+decision)))
                procedure=""; site=""; diagnosis=""; benefits=""; risks=""; alternatives=""; notes=""; responsible=""; questions=false; understood=false; accepted=false; declined=false
            }
        },modifier=Modifier.fillMaxWidth()){ Text(tr(lang,"Guardar consentimiento","Save consent")) }
        if(session.informedConsents.isNotEmpty()) {
            Text(tr(lang,"Últimos consentimientos","Recent consents"),fontWeight=FontWeight.Bold)
            session.informedConsents.takeLast(5).asReversed().forEach { c ->
                Text(java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",java.util.Locale.getDefault()).format(java.util.Date(c.timestamp))+" · "+c.procedure+" · "+if(c.accepted)tr(lang,"Aceptado","Accepted") else tr(lang,"Rechazado","Declined"))
            }
        }
        Text(tr(lang,"⚠️ El registro electrónico no sustituye los requisitos legales, institucionales ni la firma/autorización que corresponda. Documenta el proceso de información y la decisión; verifica la normativa aplicable.","⚠️ Electronic recording does not replace applicable legal, institutional or signature/authorization requirements. Verify applicable rules."),style=MaterialTheme.typography.bodySmall,fontWeight=FontWeight.Bold)
    }

    ResponsiveSectionV17(tr(lang,"14 · Nota clínica automática","14 · Automatic clinical note"),tr(lang,"Genera un borrador estructurado a partir de los datos realmente capturados en el expediente.","Generates a structured draft from data actually captured in the record.")) {
        var note by remember(session){mutableStateOf(ClinicalEngines.generateAutomaticClinicalNote(session,lang))}
        Button(onClick={ note = ClinicalEngines.generateAutomaticClinicalNote(session,lang) },modifier=Modifier.fillMaxWidth()){
            Text(tr(lang,"Generar / actualizar nota","Generate / refresh note"))
        }
        Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){
            Text(note,Modifier.padding(12.dp),style=MaterialTheme.typography.bodySmall)
        }
        Text(tr(lang,"⚠️ Es un borrador: revisa y corrige cualquier dato antes de usarlo. No documenta automáticamente actos que no hayan sido realizados.","⚠️ Draft only: review and correct every detail before use. It does not document procedures that were not actually performed."),style=MaterialTheme.typography.bodySmall,fontWeight=FontWeight.Bold)
    }

    ResponsiveSectionV17(tr(lang,"13 · Evolución clínica","13 · Clinical evolution"),tr(lang,"Conserva mediciones sucesivas para comparar signos vitales, peso, dolor y glucosa a lo largo del tiempo.","Keeps serial measurements to compare vital signs, weight, pain and glucose over time.")) {
        val measurements = session.clinicalMeasurements.takeLast(8).asReversed()
        if (measurements.isEmpty()) {
            Text(tr(lang,"Sin mediciones longitudinales registradas. Usa “Registrar signos vitales” para crear el primer punto.","No longitudinal measurements yet. Use “Log vital signs” to create the first point."))
        } else {
            measurements.forEachIndexed { index, m ->
                val date=java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",java.util.Locale.getDefault()).format(java.util.Date(m.timestamp))
                val bp=if(m.systolic!=null&&m.diastolic!=null) "${m.systolic}/${m.diastolic}" else "—"
                Text("${measurements.size-index}. $date",fontWeight=FontWeight.Bold)
                Text("TA $bp · FC ${m.heartRate ?: "—"} · FR ${m.respiratoryRate ?: "—"} · SpO₂ ${m.spo2 ?: "—"}%")
                Text("Glucosa ${m.glucose ?: "—"} mg/dL · Temp ${m.temperature ?: "—"} °C · Peso ${m.weightKg ?: "—"} kg · IMC ${m.bmi ?: "—"} · Dolor ${m.pain ?: "—"}/10",style=MaterialTheme.typography.bodySmall)
            }
            if(measurements.size>=2){
                val newest=measurements[0]; val previous=measurements[1]
                val painDelta=if(newest.pain!=null&&previous.pain!=null) newest.pain-previous.pain else null
                val weightDelta=if(newest.weightKg!=null&&previous.weightKg!=null) newest.weightKg-previous.weightKg else null
                Text(tr(lang,"Cambio desde la medición anterior","Change from previous measurement"),fontWeight=FontWeight.Bold)
                if(painDelta!=null) Text("Dolor: ${if(painDelta>0)"+" else ""}$painDelta/10")
                if(weightDelta!=null) Text("Peso: ${if(weightDelta>0)"+" else ""}${"%.1f".format(weightDelta)} kg")
                if(painDelta==null&&weightDelta==null) Text(tr(lang,"No hay variables comparables suficientes.","Not enough comparable variables."))
            }
        }
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
