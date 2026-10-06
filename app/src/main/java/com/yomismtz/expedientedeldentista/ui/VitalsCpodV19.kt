package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.evaluateClinicalDecisionV1
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus
import kotlin.math.pow

private data class VitalBand19(
    val label:String,
    val rrMin:Int,val rrMax:Int,
    val hrMin:Int,val hrMax:Int
)

private fun vitalRange19(value:Double?,min:Double,max:Double,lang:String):String = when {
    value==null -> tr(lang,"Sin dato","No value")
    value<min -> tr(lang,"↓ por debajo de referencia","↓ below reference")
    value>max -> tr(lang,"↑ por encima de referencia","↑ above reference")
    else -> tr(lang,"✓ dentro de referencia","✓ within reference")
}

private fun temperature19(value:Double?,lang:String):String = when {
    value==null -> tr(lang,"Escribe la temperatura para interpretarla.","Enter temperature for interpretation.")
    value<=35.0 -> tr(lang,"≤35 °C: temperatura muy baja. Confirma la medición y solicita valoración clínica.","≤35 °C: very low temperature. Confirm measurement and obtain clinical assessment.")
    value<36.0 -> tr(lang,"Temperatura baja; confirma sitio, método y contexto.","Low temperature; confirm site, method and context.")
    value<=37.5 -> tr(lang,"Intervalo adulto habitual aproximado; la normalidad varía por persona, hora y sitio de medición.","Approximate common adult interval; normal varies by person, time and measurement site.")
    value<38.0 -> tr(lang,"Temperatura elevada, todavía por debajo del umbral habitual de fiebre de 38 °C.","Elevated temperature, still below the usual 38 °C fever threshold.")
    value<40.0 -> tr(lang,"≥38 °C: fiebre. Confirma la medición y valora el contexto antes de atención electiva.","≥38 °C: fever. Confirm measurement and assess context before elective care.")
    else -> tr(lang,"≥40 °C: fiebre muy alta; requiere valoración médica prioritaria según el contexto.","≥40 °C: very high fever; prompt medical assessment is needed depending on context.")
}

private enum class GlucoseContext19 { FASTING, PREMEAL, POSTMEAL, RANDOM }

private enum class GlucoseStatus19 { NO_KNOWN, DIABETES, PREDIABETES, INSULIN_RESISTANCE, UNKNOWN }

private fun glucoseStatusLabel19(status:GlucoseStatus19,lang:String):String = when(status) {
    GlucoseStatus19.NO_KNOWN -> tr(lang,"Sin diabetes conocida","No known diabetes")
    GlucoseStatus19.DIABETES -> tr(lang,"Diabetes conocida","Known diabetes")
    GlucoseStatus19.PREDIABETES -> tr(lang,"Prediabetes","Prediabetes")
    GlucoseStatus19.INSULIN_RESISTANCE -> tr(lang,"Resistencia a la insulina","Insulin resistance")
    GlucoseStatus19.UNKNOWN -> tr(lang,"Desconocido / no documentado","Unknown / not documented")
}

private enum class OxygenContext19 { ROOM_AIR, SUPPLEMENTAL_OXYGEN, UNKNOWN }

private fun oxygenContextLabel19(ctx:OxygenContext19,lang:String):String = when(ctx) {
    OxygenContext19.ROOM_AIR -> tr(lang,"Aire ambiente","Room air")
    OxygenContext19.SUPPLEMENTAL_OXYGEN -> tr(lang,"Oxígeno suplementario","Supplemental oxygen")
    OxygenContext19.UNKNOWN -> tr(lang,"Contexto no documentado","Context not documented")
}

private fun glucoseClinicalContext19(
    value:Int?,
    status:GlucoseStatus19,
    context:GlucoseContext19,
    medications:Set<String>,
    lang:String
):String {
    if(value==null) return tr(lang,"Registra diabetes/prediabetes, medicamentos y contexto de la medición antes de interpretar la glucosa.","Record diabetes/prediabetes, medications and measurement context before interpreting glucose.")
    val therapy=if(medications.isEmpty()) tr(lang,"sin fármacos hipoglucemiantes registrados","no glucose-lowering drugs recorded")
        else tr(lang,"con fármacos hipoglucemiantes registrados","with glucose-lowering drugs recorded")
    val condition=glucoseStatusLabel19(status,lang)
    val timing=glucoseContext19(context,lang)
    return tr(
        lang,
        "Contexto: $condition · $timing · $therapy. La glucosa capilar es una medición de cribado/monitorización y no equivale automáticamente a glucosa plasmática diagnóstica.",
        "Context: $condition · $timing · $therapy. Capillary glucose is a screening/monitoring measurement and is not automatically equivalent to diagnostic plasma glucose."
    )
}

private fun spo2ClinicalContext19(
    value:Int?,
    context:OxygenContext19,
    oxygenFlow:String,
    respiratoryDisease:Boolean,
    smoking:Boolean,
    lang:String
):String {
    if(value==null) return tr(lang,"Registra si está en aire ambiente o recibe oxígeno y, si recibe, el flujo/dispositivo.","Record whether the patient is on room air or receiving oxygen and, if receiving it, the flow/device.")
    val ctx=oxygenContextLabel19(context,lang)
    val flow=oxygenFlow.ifBlank { tr(lang,"flujo no registrado","flow not recorded") }
    val disease=if(respiratoryDisease) tr(lang,"con enfermedad respiratoria registrada","with recorded respiratory disease") else tr(lang,"sin enfermedad respiratoria registrada","without recorded respiratory disease")
    val smoke=if(smoking) tr(lang,"tabaquismo actual","current tobacco use") else tr(lang,"sin tabaquismo actual registrado","no current tobacco use recorded")
    return tr(
        lang,
        "Contexto SpO₂: $ctx · $flow · $disease · $smoke. Una cifra aislada no debe interpretarse sin síntomas, perfusión, dispositivo y contexto clínico.",
        "SpO₂ context: $ctx · $flow · $disease · $smoke. A single value should not be interpreted without symptoms, perfusion, device and clinical context."
    )
}

private fun glucoseContext19(ctx:GlucoseContext19,lang:String):String = when(ctx) {
    GlucoseContext19.FASTING -> tr(lang,"Ayuno ≥8 h","Fasting ≥8 h")
    GlucoseContext19.PREMEAL -> tr(lang,"Diabetes · antes de comer","Diabetes · premeal")
    GlucoseContext19.POSTMEAL -> tr(lang,"Diabetes · 1–2 h poscomida","Diabetes · 1–2 h postmeal")
    GlucoseContext19.RANDOM -> tr(lang,"Casual / tiempo no definido","Random / timing unknown")
}

private fun glucose19(value:Int?,ctx:GlucoseContext19,lang:String):String {
    if(value==null) return tr(lang,"Escribe la glucosa capilar y selecciona el contexto.","Enter capillary glucose and select context.")
    if(value<70) return tr(lang,"<70 mg/dL: valor bajo/hipoglucemia para muchas personas con diabetes. Confirma y sigue el protocolo clínico.","<70 mg/dL: low/hypoglycemic for many people with diabetes. Confirm and follow the clinical protocol.")
    return when(ctx) {
        GlucoseContext19.FASTING -> when {
            value<=99 -> tr(lang,"70–99 mg/dL: referencia habitual de glucosa en ayuno normal.","70–99 mg/dL: common normal fasting reference.")
            value<=125 -> tr(lang,"100–125 mg/dL: glucosa en ayuno elevada; requiere valoración médica/laboratorial.","100–125 mg/dL: elevated fasting glucose; medical/laboratory assessment is needed.")
            else -> tr(lang,"≥126 mg/dL: supera el umbral diagnóstico de glucosa plasmática en ayuno usado por ADA/CDC. Una lectura capilar aislada no confirma diabetes.","≥126 mg/dL: above the ADA/CDC fasting plasma diagnostic threshold. A single capillary reading does not diagnose diabetes.")
        }
        GlucoseContext19.PREMEAL -> if(value in 80..130)
            tr(lang,"Dentro del objetivo preprandial frecuente de ADA: 80–130 mg/dL.","Within the common ADA premeal target: 80–130 mg/dL.")
        else tr(lang,"Fuera del objetivo preprandial frecuente de 80–130 mg/dL; confirma y contextualiza.","Outside the common 80–130 mg/dL premeal target; confirm and contextualize.")
        GlucoseContext19.POSTMEAL -> if(value<180)
            tr(lang,"Por debajo del objetivo pico posprandial frecuente de ADA (<180 mg/dL, 1–2 h tras iniciar la comida).","Below the common ADA peak postmeal target (<180 mg/dL, 1–2 h after the meal begins).")
        else tr(lang,"≥180 mg/dL: por encima del objetivo pico posprandial frecuente; confirma y contextualiza.","≥180 mg/dL: above the common peak postmeal target; confirm and contextualize.")
        GlucoseContext19.RANDOM -> when {
            value<140 -> tr(lang,"Lectura casual sin hipoglucemia; depende del tiempo desde la última comida y del contexto.","Random reading without hypoglycemia; interpretation depends on time since last meal and context.")
            value<200 -> tr(lang,"Lectura casual elevada; registra última comida y antecedentes y considera confirmación médica.","Elevated random reading; record last meal/history and consider medical confirmation.")
            else -> tr(lang,"≥200 mg/dL es médicamente relevante. ADA usa ese umbral en plasma aleatorio con síntomas clásicos/crisis; una lectura capilar aislada no establece diagnóstico.","≥200 mg/dL is medically significant. ADA uses this random plasma threshold with classic symptoms/crisis; one capillary reading does not establish diagnosis.")
        }
    }
}


private fun vitalBandForAge19(age:Int):VitalBand19 {
    val ref=clinicalVitalBandV20(age)
    return VitalBand19(ref.labelEs,ref.rrMin,ref.rrMax,ref.hrMin,ref.hrMax)
}
private data class PediatricBpScreen19(val systolic:Int,val diastolic:Int,val label:String)
private fun pediatricBpScreen19(age:Int,sex:String):PediatricBpScreen19? {
    if(age < 1) return null
    if(age >= 13) return PediatricBpScreen19(120,80,"≥13 años: cribado 120/80 mmHg")
    val boys=listOf(98 to 52,100 to 55,101 to 58,102 to 60,103 to 63,105 to 66,106 to 68,107 to 69,107 to 70,108 to 72,110 to 74,113 to 75)
    val girls=listOf(98 to 54,101 to 58,102 to 60,103 to 62,104 to 64,105 to 67,106 to 68,107 to 69,108 to 71,109 to 72,111 to 74,114 to 75)
    val pair=when(sex) { "Masculino"->boys[age-1]; "Femenino"->girls[age-1]; else->return null }
    return PediatricBpScreen19(pair.first,pair.second,"Umbral AAP de cribado: ${if(sex=="Masculino")"niño" else "niña"} de $age años ${pair.first}/${pair.second} mmHg")
}
private fun bpAction19(age:Int?,sex:String,heightCm:Double?,sys:Int?,dia:Int?,lang:String):String {
    if(age==null||age !in 0..120)return tr(lang,"⚠️ Edad obligatoria: no se puede clasificar la TA con seguridad sin edad.","⚠️ Age is required: BP cannot be safely classified without age.")
    if(sys==null||dia==null)return tr(lang,"Introduce ambas cifras de presión arterial y repite la medición si el resultado parece inesperado.","Enter both BP values and repeat the measurement if the result seems unexpected.")
    if(sys<40||sys>300||dia<20||dia>200)return tr(lang,"⚠️ Valor de TA no plausible: repetir con técnica y brazalete adecuados.","⚠️ Implausible BP value: repeat with proper technique and cuff.")
    if(age>=18)return when{
        sys>180||dia>120->tr(lang,"🔴 MUY ALTA / CRÍTICA: >180 y/o >120 mmHg. Repetir tras al menos 1 minuto. Si persiste, no trabajar; contactar de inmediato a un profesional. Con dolor torácico, disnea, debilidad/adormecimiento, alteración visual o del habla: emergencia médica.","🔴 VERY HIGH / CRITICAL: >180 and/or >120 mmHg. Repeat after at least 1 minute. If persistent, do not treat; contact a health professional immediately. With chest pain, dyspnea, weakness/numbness, visual or speech changes: medical emergency.")
        sys>=160||dia>=100->tr(lang,"🟠 ALTA: puede corresponder a hipertensión importante. Repetir correctamente y, si se confirma, enviar a valoración médica antes de atención electiva invasiva. No diagnosticar con una sola lectura.","🟠 HIGH: may represent significant hypertension. Repeat correctly and, if confirmed, obtain medical assessment before elective invasive care. Do not diagnose from one reading.")
        sys>=140||dia>=90->tr(lang,"🟠 ALTA: compatible con hipertensión en rango adulto. Repetir y recomendar valoración médica; una lectura aislada no confirma diagnóstico. La atención odontológica depende del procedimiento y del estado clínico.","🟠 HIGH: compatible with an adult hypertension range. Repeat and recommend medical assessment; one reading does not confirm diagnosis. Dental care depends on the procedure and clinical status.")
        sys<90||dia<60->tr(lang,"🔵 BAJA: puede significar hipotensión, especialmente si hay mareo, síncope, debilidad o mala perfusión. Repetir y valorar hidratación, medicamentos y síntomas; si persiste o es sintomática, no trabajar y solicitar valoración.","🔵 LOW: may indicate hypotension, especially with dizziness, syncope, weakness or poor perfusion. Repeat and assess hydration, medications and symptoms; if persistent or symptomatic, do not treat and seek assessment.")
        else->tr(lang,"🟢 SIN CRITERIO DE ALERTA POR TA AISLADA. La clasificación adulta actual considera <120/80 normal, pero la decisión odontológica también depende de síntomas, comorbilidades y procedimiento.","🟢 NO ALERT CRITERION FROM BP ALONE. Current adult classification considers <120/80 normal, but dental decisions also depend on symptoms, comorbidities and procedure.")
    }
    if(age<1)return tr(lang,"⚠️ En lactantes, usa una referencia pediátrica específica; esta pantalla no clasifica hipertensión del lactante.","⚠️ In infants, use a specific pediatric reference; this screen does not classify infant hypertension.")
    val screen=pediatricBpScreen19(age,sex)
    if(screen==null)return tr(lang,"🟠 No se puede aplicar el cribado pediátrico sin sexo. Selecciona sexo y registra talla; la AAP exige interpretación por edad, sexo y talla para la clasificación definitiva.","🟠 Pediatric screening cannot be applied without sex. Select sex and record height; AAP requires age, sex and height for definitive classification.")
    if(heightCm==null||heightCm<=0)return tr(lang,"🟠 FALTA TALLA: repetir la TA y registrar talla antes de clasificarla definitivamente. La tabla simplificada AAP sólo sirve para cribado.","🟠 HEIGHT MISSING: repeat BP and record height before definitive classification. The AAP simplified table is screening-only.")
    val low=if(age<=10)70+2*age else 90
    return when{
        sys<low->tr(lang,"🔵 BAJA: por debajo del umbral pediátrico de seguridad de cribado. Repetir y valorar perfusión/síntomas. Si persiste o es sintomática, no trabajar y solicitar valoración médica.","🔵 LOW: below the pediatric screening safety threshold. Repeat and assess perfusion/symptoms. If persistent or symptomatic, do not treat and seek medical assessment.")
        sys>=140||dia>=90->tr(lang,"🔴 ALTA: TA pediátrica marcadamente elevada. Repetir con técnica adecuada y buscar valoración médica; si hay síntomas de alarma, atención urgente. No realizar atención electiva hasta aclarar la situación.","🔴 HIGH: markedly elevated pediatric BP. Repeat with proper technique and obtain medical assessment; if red-flag symptoms occur, seek urgent care. Do not provide elective care until clarified.")
        sys>=screen.systolic||dia>=screen.diastolic->tr(lang,"🟠 ELEVADA EN CRIBADO: supera la tabla simplificada AAP. Repetir y confirmar con las tablas completas por edad, sexo y talla. Esto no diagnostica hipertensión.","🟠 ELEVATED ON SCREENING: above the AAP simplified table. Repeat and confirm with complete tables by age, sex and height. This does not diagnose hypertension.")
        else->tr(lang,"🟢 SIN ALERTA EN CRIBADO: por debajo del umbral simplificado AAP. La clasificación definitiva pediátrica sigue requiriendo edad, sexo y talla.","🟢 NO SCREENING ALERT: below the AAP simplified threshold. Definitive pediatric classification still requires age, sex and height.")
    }
}
private fun rhythmAction19(rr:Int?,hr:Int?,band:VitalBand19,lang:String):String {
    if(rr==null&&hr==null)return tr(lang,"Introduce FR y/o FC.","Enter RR and/or HR.")
    val rrMeaning=rr?.let{clinicalVitalMeaningV20("FR",it.toDouble(),band.rrMin.toDouble(),band.rrMax.toDouble(),lang)}
    val hrMeaning=hr?.let{clinicalVitalMeaningV20("FC",it.toDouble(),band.hrMin.toDouble(),band.hrMax.toDouble(),lang)}
    val abnormal=rr?.let{it<band.rrMin||it>band.rrMax}==true||hr?.let{it<band.hrMin||it>band.hrMax}==true
    return if(!abnormal)tr(lang,"🟢 FC/FR dentro de la referencia etaria. No hay restricción por estos valores aislados si el paciente está clínicamente estable.","🟢 HR/RR within the age reference. No restriction from these values alone if clinically stable.")
    else tr(lang,"🟠 " + (rrMeaning ?: "") + " " + (hrMeaning ?: "") + " Repetir en reposo. Considera dolor, ansiedad, fiebre, deshidratación, medicamentos y enfermedad respiratoria/cardiaca. Si persiste o hay disnea, dolor torácico, síncope, mala perfusión o alteración de conciencia: valoración médica antes de trabajar.","🟠 " + (rrMeaning ?: "") + " " + (hrMeaning ?: "") + " Repeat at rest. Consider pain, anxiety, fever, dehydration, medications and respiratory/cardiac disease. If persistent or with dyspnea, chest pain, syncope, poor perfusion or altered consciousness: medical assessment before treatment.")
}
private fun temperatureAction19(value:Double?,lang:String):String=when{
    value==null->tr(lang,"Introduce la temperatura.","Enter temperature.")
    value<=35.0->tr(lang,"🔵 BAJA: confirmar medición. Suspender tratamiento hasta valoración clínica; si persiste o hay alteración de conciencia, activar atención urgente.","🔵 LOW: confirm the measurement. Stop treatment pending clinical assessment; if persistent or altered consciousness occurs, activate urgent care.")
    value>=38.0->tr(lang,"🔴 ALTA: fiebre. Diferir atención electiva y buscar la causa. Si hay infección odontógena con fiebre/malestar, priorizar control del foco y valorar antibiótico sólo cuando esté indicado.","🔴 HIGH: fever. Defer elective care and identify the cause. If odontogenic infection with fever/malaise is present, prioritize source control and consider antibiotics only when indicated.")
    value>37.2->tr(lang,"🟠 ELEVADA: repetir y correlacionar con síntomas. Evitar tratamiento electivo si existe sospecha de infección sistémica.","🟠 ELEVATED: repeat and correlate with symptoms. Avoid elective care if systemic infection is suspected.")
    else->tr(lang,"🟢 NORMAL: temperatura compatible con el rango habitual. Continuar según hallazgos clínicos.","🟢 NORMAL: temperature is compatible with the usual range. Proceed according to clinical findings.")
}
private fun spo2Action19(value:Int?,lang:String):String=when{
    value==null->tr(lang,"Introduce SpO₂.","Enter SpO₂.")
    value>100||value<0->tr(lang,"Valor no válido.","Invalid value.")
    value>=95->tr(lang,"🟢 NORMAL: 95–100 % es habitual en la mayoría de personas sanas. Si el paciente está estable, puede continuar la atención.","🟢 NORMAL: 95–100% is usual in most healthy people. If clinically stable, care may continue.")
    value>=92->tr(lang,"🟠 BAJA/ATÍPICA: repetir con técnica correcta y valorar síntomas y antecedentes. Si persiste, considerar valoración médica antes de atención electiva.","🟠 LOW/ATYPICAL: repeat correctly and assess symptoms/history. If persistent, consider medical assessment before elective care.")
    value>=90->tr(lang,"🔴 BAJA: repetir inmediatamente y valorar clínicamente. No continuar tratamiento electivo si persiste; buscar valoración médica urgente.","🔴 LOW: repeat immediately and assess clinically. Do not continue elective treatment if persistent; seek urgent medical assessment.")
    else->tr(lang,"🚨 MUY BAJA: <90 % puede indicar hipoxemia significativa. Suspender tratamiento y activar valoración urgente/emergente según síntomas. Algunas enfermedades crónicas tienen objetivos individualizados.","🚨 VERY LOW: <90% may indicate significant hypoxemia. Stop treatment and activate urgent/emergency assessment according to symptoms.")
}
private fun glucoseAction19(value:Int?,status:GlucoseStatus19,context:GlucoseContext19,medications:Set<String>,lang:String):String{
    if(value==null)return tr(lang,"Introduce glucosa, condición metabólica, medicamentos y momento de medición.","Enter glucose, metabolic status, medications and measurement timing.")
    if(value<54)return tr(lang,"🔴 MUY BAJA: <54 mg/dL es hipoglucemia clínicamente importante. No trabajar; tratar según protocolo y repetir en 15 min. Si no puede deglutir o hay alteración de conciencia, activar emergencias.","🔴 VERY LOW: <54 mg/dL is clinically significant hypoglycemia. Do not treat; manage per protocol and recheck in 15 min. If unable to swallow or mental status is altered, activate emergency response.")
    if(value<70)return tr(lang,"🟠 BAJA: <70 mg/dL es hipoglucemia clínicamente relevante. Suspender procedimiento, corregir según protocolo y repetir en 15 min. Considera insulina/sulfonilureas y ayuno.","🟠 LOW: <70 mg/dL is clinically important hypoglycemia. Stop the procedure, manage per protocol and recheck in 15 min. Consider insulin/sulfonylureas and fasting.")
    if(value>=350)return tr(lang,"🔴 MUY ALTA: ≥350 mg/dL. En diabetes, especialmente con insulina, valorar cetonas y síntomas de descompensación; posponer trabajo electivo y solicitar valoración médica.","🔴 VERY HIGH: ≥350 mg/dL. In diabetes, especially with insulin, assess ketones and symptoms of decompensation; postpone elective care and obtain medical assessment.")
    if(value>=300)return tr(lang,"🔴 MUY ALTA: ≥300 mg/dL. Repetir/confirmar, revisar diabetes, medicamentos y síntomas. No realizar atención electiva invasiva hasta valorar el control metabólico.","🔴 VERY HIGH: ≥300 mg/dL. Repeat/confirm, review diabetes, medications and symptoms. Do not provide elective invasive care until metabolic control is assessed.")
    if(value>=180)return tr(lang,"🟠 ALTA: >180 mg/dL puede superar objetivos frecuentes en diabetes, pero depende de ayuno, comida, edad, tratamiento y objetivo individual. Repetir/contextualizar; si persiste antes de un procedimiento invasivo, considerar valoración médica.","🟠 HIGH: >180 mg/dL may exceed common diabetes targets, but interpretation depends on fasting, meals, age, treatment and individualized goal. Repeat/contextualize; if persistent before invasive care, consider medical assessment.")
    return tr(lang,"🟢 SIN ALERTA POR GLUCOSA AISLADA. Mantener el contexto de diabetes, prediabetes o resistencia a la insulina, fármacos y comida; una glucosa capilar aislada no diagnostica diabetes.","🟢 NO ALERT FROM ISOLATED GLUCOSE. Keep diabetes, prediabetes or insulin-resistance, medication and meal context; an isolated capillary value does not diagnose diabetes.")
}
private val dentalSignsSymptoms19=listOf("Dolor dental/orofacial","Sensibilidad al frío/calor","Dolor espontáneo/nocturno","Inflamación intraoral","Inflamación facial/cervical","Sangrado gingival","Sangrado oral no controlable","Halitosis","Xerostomía","Sialorrea","Trismus","Disfagia","Odinofagia","Parestesia/numbness","Úlcera o lesión >2 semanas","Mancha blanca/roja persistente","Movilidad dental","Supuración/fístula","Fiebre/malestar","Cefalea/dolor facial","Disnea","Dolor torácico","Mareo/síncope","Alteración de conciencia","Convulsiones")
private fun triageAction19(selected:Set<String>,lang:String):String{
    val emergency=setOf("Sangrado oral no controlable","Inflamación facial/cervical","Disnea","Dolor torácico","Alteración de conciencia","Convulsiones")
    return when{
        selected.any{it in emergency}->tr(lang,"🚨 SIGNO DE ALARMA: detener el procedimiento, valorar ABC y activar el protocolo de emergencia. La inflamación con compromiso de vía aérea y el sangrado no controlable no deben enviarse simplemente a casa.","🚨 RED FLAG: stop the procedure, assess ABC and activate the emergency protocol. Airway-threatening swelling and uncontrolled bleeding should not simply be sent home.")
        "Fiebre/malestar" in selected&&("Inflamación intraoral" in selected||"Supuración/fístula" in selected)->tr(lang,"⚠️ Posible infección odontógena con compromiso sistémico: atención dental urgente para control del foco y valoración de antibiótico cuando esté indicado; si existe compromiso de vía aérea o deterioro general, derivación urgente.","⚠️ Possible odontogenic infection with systemic involvement: urgent dental care for source control and antibiotic assessment when indicated; if airway compromise or systemic deterioration occurs, urgent referral.")
        "Úlcera o lesión >2 semanas" in selected||"Mancha blanca/roja persistente" in selected->tr(lang,"⚠️ Lesión persistente/sospechosa: documentar, examinar y realizar biopsia o referencia según hallazgos. No etiquetar como cáncer sólo por el aspecto.","⚠️ Persistent/suspicious lesion: document, examine and biopsy or refer according to findings. Do not label it cancer based on appearance alone.")
        else->tr(lang,"✓ No se seleccionó un signo de alarma mayor. Continuar exploración, diagnóstico diferencial y tratamiento según el hallazgo odontológico.","✓ No major red flag selected. Continue examination, differential diagnosis and treatment according to the dental finding.")
    }
}

private enum class DentalProcedure19 {
    DIAGNOSTIC, RESTORATIVE, ENDODONTIC, PERIODONTAL, EXTRACTION, SURGERY, ORTHODONTIC, PROSTHETIC, LOCAL_ANESTHESIA, SEDATION
}
private fun procedureName19(p:DentalProcedure19,lang:String)=when(p){
    DentalProcedure19.DIAGNOSTIC->tr(lang,"Exploración / diagnóstico","Examination / diagnosis")
    DentalProcedure19.RESTORATIVE->tr(lang,"Operatoria / restauración","Restorative dentistry")
    DentalProcedure19.ENDODONTIC->tr(lang,"Endodoncia","Endodontics")
    DentalProcedure19.PERIODONTAL->tr(lang,"Periodoncia","Periodontal treatment")
    DentalProcedure19.EXTRACTION->tr(lang,"Extracción dental","Dental extraction")
    DentalProcedure19.SURGERY->tr(lang,"Cirugía oral","Oral surgery")
    DentalProcedure19.ORTHODONTIC->tr(lang,"Ortodoncia / ortopedia","Orthodontics / dentofacial orthopedics")
    DentalProcedure19.PROSTHETIC->tr(lang,"Prótesis","Prosthodontics")
    DentalProcedure19.LOCAL_ANESTHESIA->tr(lang,"Anestesia local","Local anesthesia")
    DentalProcedure19.SEDATION->tr(lang,"Sedación / anestesia profunda","Sedation / deep anesthesia")
}
private fun bmiAction19(age:Int,sex:String,bmi:Double?,lang:String):String{
    if(bmi==null)return tr(lang,"Introduce peso y talla para calcular el IMC.","Enter weight and height to calculate BMI.")
    if(age<2)return tr(lang,"IMC calculado: ${"%.1f".format(bmi)} kg/m². En menores de 2 años no debe interpretarse con categorías de IMC infantil de 2–19 años; usar estándares de crecimiento apropiados.","Calculated BMI: ${"%.1f".format(bmi)} kg/m². In children under 2, do not use the 2–19 child BMI categories; use appropriate growth standards.")
    if(age<20){
        val sexOk=sex=="Masculino"||sex=="Femenino"
        return if(!sexOk) tr(lang,"IMC ${"%.1f".format(bmi)} kg/m². Para menores de 20 años, el IMC debe interpretarse como percentil por edad y sexo; selecciona el sexo y consulta la curva/tabla correspondiente. No usar categorías adultas.","BMI ${"%.1f".format(bmi)} kg/m². Under 20, BMI must be interpreted as an age- and sex-specific percentile; select sex and consult the appropriate growth chart. Do not use adult categories.")
        else tr(lang,"IMC ${"%.1f".format(bmi)} kg/m². En $age años, interprétalo por percentil de IMC para edad y sexo; el IMC por sí solo no decide si un procedimiento dental puede realizarse.","BMI ${"%.1f".format(bmi)} kg/m². At age $age, interpret it by BMI-for-age percentile and sex; BMI alone does not determine whether a dental procedure can be performed.")
    }
    return when{
        bmi<18.5->tr(lang,"IMC ${"%.1f".format(bmi)}: bajo peso en clasificación adulta. Correlacionar con estado nutricional y enfermedad; no suspender tratamiento dental sólo por el IMC.","BMI ${"%.1f".format(bmi)}: adult underweight category. Correlate with nutritional/medical status; do not stop dental care based on BMI alone.")
        bmi<25.0->tr(lang,"IMC ${"%.1f".format(bmi)}: rango de peso saludable en clasificación adulta. Continuar según signos, antecedentes y procedimiento.","BMI ${"%.1f".format(bmi)}: healthy-weight adult category. Proceed according to signs, history and procedure.")
        bmi<30.0->tr(lang,"IMC ${"%.1f".format(bmi)}: sobrepeso en clasificación adulta. El IMC no contraindica por sí mismo la atención dental; considerar comorbilidades y vía aérea si se planifica sedación.","BMI ${"%.1f".format(bmi)}: adult overweight category. BMI alone does not contraindicate dental care; consider comorbidities and airway issues if sedation is planned.")
        else->tr(lang,"IMC ${"%.1f".format(bmi)}: obesidad en clasificación adulta. El IMC no decide por sí solo la aptitud dental; valorar comorbilidades, vía aérea, movilidad y riesgo anestésico si corresponde.","BMI ${"%.1f".format(bmi)}: adult obesity category. BMI alone does not determine dental fitness; assess comorbidities, airway, mobility and anesthetic risk when relevant.")
    }
}
private fun anestheticRecommendation19(
    age:Int, sys:Int?, dia:Int?, hr:Int?, spo2:Int?, temp:Double?,
    selected:Set<String>, bmi:Double?, lang:String
):String {
    val majorCardio = selected.any {
        it=="Dolor torácico" || it=="Disnea" || it=="Mareo/síncope"
    }
    val severe = majorCardio || (sys!=null && dia!=null && (sys>180 || dia>110)) ||
        (spo2!=null && spo2<90) || (temp!=null && temp>=38.0 &&
        ("Fiebre/malestar" in selected || "Inflamación facial/cervical" in selected))
    if(severe) return tr(lang,
        "🚨 Anestesia local electiva: diferir y valorar primero la condición sistémica. No seleccionar anestésico con vasoconstrictor como si fuera una autorización.",
        "🚨 Elective local anesthesia: defer and assess the systemic condition first. Do not treat a vasoconstrictor choice as clearance."
    )

    val cardiovascularCaution = (sys!=null && dia!=null && (sys>=160 || dia>=100)) ||
        (hr!=null && (hr<50 || hr>120))
    if(age<4) {
        return if(cardiovascularCaution) tr(lang,
            "💉 Sugerencia educativa: lidocaína 2% sin vasoconstrictor puede ser una opción cuando se requiere evitar vasoconstrictor; en niños la dosis debe calcularse estrictamente por peso y por el producto específico. No usar esta pantalla para autorizar dosis.",
            "💉 Educational suggestion: 2% lidocaine without vasoconstrictor may be an option when avoiding vasoconstrictor; in children dose strictly by weight and product-specific labeling. Do not use this screen to authorize dosing."
        ) else tr(lang,
            "💉 Sugerencia educativa: lidocaína 2% con epinefrina 1:100,000 puede considerarse si está indicada y el paciente está estable; en menores la dosis debe calcularse por peso y por el producto específico.",
            "💉 Educational suggestion: 2% lidocaine with epinephrine 1:100,000 may be considered when indicated and the patient is stable; in children dose by weight and product-specific labeling."
        )
    }

    return if(cardiovascularCaution) tr(lang,
        "💉 VASOCONSTRICTOR: usar con precaución. Una opción educativa es mepivacaína 3% sin vasoconstrictor cuando el objetivo sea evitar epinefrina. Si se necesita vasoconstrictor, la ADA señala como precaución habitual en adultos con riesgo cardiovascular limitar la epinefrina a 0.04 mg, con aspiración y administración lenta.",
        "💉 VASOCONSTRICTOR: use caution. An educational option is 3% mepivacaine without vasoconstrictor when avoiding epinephrine. If a vasoconstrictor is needed, ADA notes a common adult cardiovascular precaution of limiting epinephrine to 0.04 mg, with aspiration and slow injection."
    ) else tr(lang,
        "💉 VASOCONSTRICTOR: puede considerarse. Opciones habituales: lidocaína 2% + epinefrina 1:100,000; articaína 4% + epinefrina 1:100,000 o 1:200,000. La elección depende del procedimiento, técnica, duración requerida, antecedentes y ficha técnica.",
        "💉 VASOCONSTRICTOR: may be considered. Common options: 2% lidocaine + epinephrine 1:100,000; 4% articaine + epinephrine 1:100,000 or 1:200,000. Choice depends on procedure, technique, duration required, history and product labeling."
    )
}

private fun availableTreatments19(
    age:Int,
    sys:Int?, dia:Int?, glucose:Int?, spo2:Int?, temp:Double?,
    selected:Set<String>, pain:Int?
):List<DentalProcedure19> {
    val emergency=setOf("Sangrado oral no controlable","Inflamación facial/cervical","Disnea","Dolor torácico","Alteración de conciencia","Convulsiones")
    if(selected.any{it in emergency}) return emptyList()
    if(spo2!=null && spo2<90) return emptyList()
    if(glucose!=null && glucose<70) return emptyList()
    if(age>=18 && sys!=null && dia!=null && (sys>180 || dia>110)) return emptyList()
    if(temp!=null && temp>=38.0 && ("Fiebre/malestar" in selected || "Inflamación intraoral" in selected || "Supuración/fístula" in selected))
        return listOf(DentalProcedure19.DIAGNOSTIC)
    if(age>=18 && sys!=null && dia!=null && (sys>=160 || dia>=100))
        return listOf(DentalProcedure19.DIAGNOSTIC)

    val lesion = "Úlcera o lesión >2 semanas" in selected || "Mancha blanca/roja persistente" in selected
    if(lesion) return listOf(DentalProcedure19.DIAGNOSTIC)

    val result=mutableListOf<DentalProcedure19>()
    fun add(p:DentalProcedure19){ if(p !in result && result.size<3) result.add(p) }

    val pulpLike=selected.any{it=="Dolor espontáneo/nocturno" || it=="Sensibilidad al frío/calor" || it=="Supuración/fístula"}
    val cariesLike=selected.any{it=="Dolor dental/orofacial" || it=="Sensibilidad al frío/calor"}
    val periodontal=selected.any{it=="Sangrado gingival" || it=="Movilidad dental" || it=="Halitosis"}
    val infection=selected.any{it=="Inflamación intraoral" || it=="Supuración/fístula" || it=="Fiebre/malestar"}

    when {
        infection || pulpLike -> {
            add(DentalProcedure19.ENDODONTIC)
            add(DentalProcedure19.EXTRACTION)
            add(DentalProcedure19.LOCAL_ANESTHESIA)
        }
        periodontal -> {
            add(DentalProcedure19.PERIODONTAL)
            add(DentalProcedure19.LOCAL_ANESTHESIA)
            add(DentalProcedure19.DIAGNOSTIC)
        }
        cariesLike || (pain!=null && pain>=1) -> {
            add(DentalProcedure19.RESTORATIVE)
            add(DentalProcedure19.LOCAL_ANESTHESIA)
            add(DentalProcedure19.DIAGNOSTIC)
        }
        else -> {
            add(DentalProcedure19.DIAGNOSTIC)
            add(DentalProcedure19.PERIODONTAL)
            add(DentalProcedure19.RESTORATIVE)
        }
    }

    if(age<2) return result.filter{it==DentalProcedure19.DIAGNOSTIC || it==DentalProcedure19.LOCAL_ANESTHESIA}.take(3)
    if(glucose!=null && glucose>=300) return result.filter{it==DentalProcedure19.DIAGNOSTIC}.take(3)
    if(temp!=null && temp>=38.0) return result.filter{it==DentalProcedure19.DIAGNOSTIC}.take(3)
    return result.take(3)
}

private fun treatmentRestrictionBySign19(
    selected:Set<String>, sys:Int?, dia:Int?, rr:Int?, hr:Int?,
    spo2:Int?, glucose:Int?, temp:Double?, bmi:Double?, lang:String
):String {
    val lines=mutableListOf<String>()
    fun add(es:String,en:String){lines.add(tr(lang,es,en))}
    selected.forEach { sign ->
        when(sign) {
            "Dolor dental/orofacial" -> add("• Dolor: permite diagnóstico y, si el estado general es estable, tratamiento dirigido al origen; no basta el dolor para decidir extracción o endodoncia.","• Pain: diagnosis and source-directed treatment may be considered if clinically stable; pain alone does not determine extraction or root canal.")
            "Sensibilidad al frío/calor" -> add("• Sensibilidad térmica: diagnóstico y operatoria pueden ser posibles; si es persistente/espontánea, primero descartar patología pulpar.","• Thermal sensitivity: diagnosis and restorative care may be possible; if persistent/spontaneous, first assess for pulpal disease.")
            "Dolor espontáneo/nocturno" -> add("• Dolor espontáneo/nocturno: priorizar diagnóstico pulpar; endodoncia puede ser una opción si el diagnóstico la indica. No asumir extracción.","• Spontaneous/night pain: prioritize pulpal diagnosis; root canal may be an option when indicated. Do not assume extraction.")
            "Inflamación intraoral","Supuración/fístula" -> add("• Inflamación/supuración: priorizar control del foco. El tratamiento definitivo puede incluir endodoncia, extracción o drenaje según el origen; antibiótico no es automático.","• Swelling/drainage: prioritize source control. Definitive care may include root canal, extraction or drainage depending on source; antibiotics are not automatic.")
            "Inflamación facial/cervical" -> add("• Inflamación facial/cervical: no tratamiento electivo; valorar urgencia y vía aérea.","• Facial/cervical swelling: no elective treatment; assess urgency and airway.")
            "Sangrado gingival" -> add("• Sangrado gingival: evaluación periodontal y tratamiento periodontal no quirúrgico pueden considerarse si no hay sangrado no controlable.","• Gingival bleeding: periodontal assessment and nonsurgical periodontal care may be considered if bleeding is controllable.")
            "Sangrado oral no controlable" -> add("• Sangrado no controlable: ningún tratamiento electivo; activar protocolo de emergencia.","• Uncontrolled oral bleeding: no elective treatment; activate emergency protocol.")
            "Movilidad dental" -> add("• Movilidad: diagnóstico periodontal primero; el tratamiento puede ser periodontal y la extracción sólo si existe indicación específica.","• Mobility: periodontal diagnosis first; periodontal treatment may be appropriate, with extraction only if specifically indicated.")
            "Úlcera o lesión >2 semanas","Mancha blanca/roja persistente" -> add("• Lesión persistente: exploración/documentación y biopsia o referencia cuando esté indicada; no asumir diagnóstico por apariencia.","• Persistent lesion: examination/documentation and biopsy or referral when indicated; do not diagnose by appearance alone.")
            "Trismus","Disfagia","Odinofagia" -> add("• Trismus/disfagia/odinofagia: buscar causa y valorar urgencia; si hay compromiso de vía aérea, no realizar tratamiento electivo.","• Trismus/dysphagia/odynophagia: identify cause and assess urgency; if airway compromise exists, no elective treatment.")
            "Disnea","Dolor torácico","Alteración de conciencia","Convulsiones" -> add("• Signo sistémico mayor: ningún tratamiento dental electivo; atención de emergencia.","• Major systemic sign: no elective dental treatment; emergency care.")
            "Fiebre/malestar" -> add("• Fiebre/malestar: diferir tratamiento electivo si sugiere infección sistémica; priorizar evaluación y control del foco.","• Fever/malaise: defer elective treatment when systemic infection is suspected; prioritize assessment and source control.")
        }
    }
    if(sys!=null && dia!=null) when {
        sys>180 || dia>110 -> add("• TA >180 o >110 mmHg: no tratamiento electivo; repetir y solicitar valoración médica.","• BP >180 or >110 mmHg: no elective treatment; repeat and obtain medical assessment.")
        sys>=160 || dia>=100 -> add("• TA ≥160/100 mmHg confirmada: diferir procedimientos electivos invasivos; la atención urgente limitada depende del estado clínico.","• Confirmed BP ≥160/100 mmHg: defer invasive elective procedures; limited urgent care depends on clinical status.")
        sys<90 || dia<60 -> add("• TA baja: repetir y valorar síntomas/perfusión; si persiste o es sintomática, diferir.","• Low BP: repeat and assess symptoms/perfusion; defer if persistent or symptomatic.")
    }
    if(rr!=null && hr!=null && (rr<12 || rr>20 || hr<60 || hr>100))
        add("• FR/FC fuera de referencia: repetir en reposo y buscar la causa antes de procedimientos invasivos.","• RR/HR outside reference: repeat at rest and identify the cause before invasive procedures.")
    if(spo2!=null && spo2<95) add(
        if(spo2<90)"• SpO₂ <90 %: no tratamiento electivo y valoración urgente." else "• SpO₂ 90–94 %: repetir y contextualizar; si persiste, valorar antes de tratamiento electivo.",
        if(spo2<90)"• SpO₂ <90%: no elective treatment and urgent assessment." else "• SpO₂ 90–94%: repeat and contextualize; if persistent, assess before elective care."
    )
    if(glucose!=null) when {
        glucose<70 -> add("• Glucosa <70 mg/dL: no iniciar; corregir hipoglucemia y reevaluar.","• Glucose <70 mg/dL: do not start; correct hypoglycemia and reassess.")
        glucose>=300 -> add("• Glucosa ≥300 mg/dL: diferir tratamiento electivo, especialmente cirugía, y valorar control metabólico.","• Glucose ≥300 mg/dL: defer elective treatment, especially surgery, and assess metabolic control.")
    }
    if(temp!=null) when {
        temp>=38.0 -> add("• Temperatura ≥38 °C: diferir electivo si existe sospecha de infección sistémica.","• Temperature ≥38 °C: defer elective care when systemic infection is suspected.")
        temp<=35.0 -> add("• Temperatura ≤35 °C: confirmar y valorar clínicamente antes de tratar.","• Temperature ≤35 °C: confirm and assess clinically before treatment.")
    }
    if(bmi!=null && bmi>=40) add("• IMC ≥40: no contraindica por sí solo la atención dental, pero requiere valorar comorbilidades y vía aérea si se considera sedación/anestesia.","• BMI ≥40: not by itself a contraindication to dental care, but assess comorbidities and airway if sedation/anesthesia is considered.")
    return if(lines.isEmpty()) tr(lang,"Sin restricciones adicionales por los datos seleccionados. La decisión final depende del diagnóstico odontológico y del estado clínico.","No additional restrictions from the selected data. Final decision depends on the dental diagnosis and clinical status.")
    else lines.take(8).joinToString("\n")
}

private fun treatmentAction19(p:DentalProcedure19,age:Int,sex:String,sys:Int?,dia:Int?,glucose:Int?,spo2:Int?,temp:Double?,bmi:Double?,selected:Set<String>,lang:String):String{
    val emergency=setOf("Sangrado oral no controlable","Inflamación facial/cervical","Disnea","Dolor torácico","Alteración de conciencia","Convulsiones")
    if(selected.any{it in emergency}) return tr(lang,"🚨 NO TRATAR ELECTIVAMENTE: existe un signo de alarma mayor. Suspender, estabilizar según competencia y activar el protocolo de emergencia.","🚨 DO NOT PROVIDE ELECTIVE CARE: a major red flag is present. Stop, stabilize within scope and activate the emergency protocol.")
    if(spo2!=null&&spo2<90)return tr(lang,"🚨 NO TRATAR ELECTIVAMENTE: SpO₂ <90 % persistente puede indicar hipoxemia significativa. Repetir/confirmar y solicitar valoración urgente.","🚨 DO NOT PROVIDE ELECTIVE CARE: persistent SpO₂ <90% may indicate significant hypoxemia. Repeat/confirm and obtain urgent assessment.")
    if(temp!=null&&temp>=38.0&&("Fiebre/malestar" in selected||"Inflamación intraoral" in selected||"Supuración/fístula" in selected))
        return tr(lang,"⚠️ DIFERIR ELECTIVO: fiebre con signos compatibles con infección. Priorizar diagnóstico y control del foco; antibiótico sólo cuando esté indicado.","⚠️ DEFER ELECTIVE CARE: fever with signs compatible with infection. Prioritize diagnosis and source control; antibiotics only when indicated.")
    if(age<2&&p!=DentalProcedure19.DIAGNOSTIC&&p!=DentalProcedure19.LOCAL_ANESTHESIA)
        return tr(lang,"⚠️ PACIENTE <2 AÑOS: no usar esta pantalla como autorización automática. Coordinar manejo con odontopediatría y considerar entorno/recursos apropiados según el procedimiento.","⚠️ PATIENT <2 YEARS: do not use this screen as automatic clearance. Coordinate pediatric dental management and use an appropriate setting/resources for the procedure.")
    if(sys!=null&&dia!=null&&age>=18&&(sys<90||dia<60)) return tr(lang,"🔵 TA BAJA: repetir tras reposo y valorar síntomas. Si se normaliza y el paciente está estable, puede considerarse atención según el procedimiento; si persiste o hay síncope/confusión/dolor torácico/mala perfusión, no tratar y solicitar valoración urgente.","🔵 LOW BP: repeat after rest and assess symptoms. If it normalizes and the patient is stable, care may be considered according to the procedure; if persistent or accompanied by syncope/confusion/chest pain/poor perfusion, do not treat and obtain urgent assessment.")
    if(age<18&&sys!=null){ val lowP=if(age<=10)70+2*age else 90; if(sys<lowP) return tr(lang,"🔵 TA SISTÓLICA BAJA PARA LA EDAD: repetir y valorar perfusión/síntomas. Si persiste, diferir tratamiento electivo y solicitar valoración clínica.","🔵 LOW AGE-BASED SYSTOLIC BP: repeat and assess perfusion/symptoms. If persistent, defer elective treatment and obtain clinical assessment.") }
    if(sys!=null&&dia!=null&&age>=18&&(sys>180||dia>110))
        return tr(lang,"🚨 NO TRATAMIENTO ELECTIVO: TA >180 o >110 mmHg. Repetir tras reposo y solicitar valoración médica; si hay síntomas de emergencia, activar emergencias.","🚨 NO ELECTIVE TREATMENT: BP >180 or >110 mmHg. Repeat after rest and obtain medical assessment; if emergency symptoms occur, activate emergency response.")
    if(sys!=null&&dia!=null&&age>=18&&(sys>=160||dia>=100))
        return if(p==DentalProcedure19.DIAGNOSTIC) tr(lang,"🟠 DIAGNÓSTICO/URGENCIA: puede realizarse valoración limitada y controlada si el paciente está estable; diferir procedimientos electivos invasivos y consultar cuando la TA esté confirmada elevada.","🟠 DIAGNOSTIC/URGENT: limited controlled assessment may be performed if stable; defer invasive elective procedures and seek medical assessment when BP is confirmed high.")
        else tr(lang,"🟠 DIFERIR ELECTIVO: TA ≥160/100 mmHg confirmada. Repetir, valorar síntomas y coordinar evaluación médica. No usar esta cifra como autorización para cirugía/anestesia/sedación.","🟠 DEFER ELECTIVE CARE: confirmed BP ≥160/100 mmHg. Repeat, assess symptoms and coordinate medical evaluation. Do not use this value as clearance for surgery/anesthesia/sedation.")
    if(glucose!=null&&glucose<70)return tr(lang,"🔵 NO INICIAR: glucosa <70 mg/dL. Tratar la hipoglucemia según protocolo y revalorar antes de continuar.","🔵 DO NOT START: glucose <70 mg/dL. Treat hypoglycemia per protocol and reassess before continuing.")
    if(glucose!=null&&glucose>=300&&p!=DentalProcedure19.DIAGNOSTIC)return tr(lang,"🔴 DIFERIR ELECTIVO: glucosa capilar ≥300 mg/dL. Valorar control metabólico; si hay vómitos, dolor abdominal, respiración anormal, deshidratación, confusión o cetonas, derivación urgente.","🔴 DEFER ELECTIVE CARE: capillary glucose ≥300 mg/dL. Assess metabolic control; with vomiting, abdominal pain, abnormal breathing, dehydration, confusion or ketones, urgent referral.")
    if(p==DentalProcedure19.LOCAL_ANESTHESIA){
        return if(age<18) tr(lang,"🦷 ANESTESIA LOCAL: puede considerarse si el paciente está clínicamente estable. En pediatría la dosis debe calcularse por peso, con el anestésico específico y sin exceder la dosis máxima recomendada; documentar fármaco, concentración y dosis. El IMC no sustituye el cálculo por kg.","🦷 LOCAL ANESTHESIA: may be considered if clinically stable. In pediatrics, dose by body weight for the specific anesthetic and never exceed the recommended maximum; document drug, concentration and dose. BMI does not replace kg-based dosing.")
        else tr(lang,"🦷 ANESTESIA LOCAL: si está clínicamente estable puede realizarse según procedimiento y comorbilidades. Con compromiso cardiovascular, usar vasoconstrictor con precaución y técnica de inyección segura; la ADA señala 0.04 mg de epinefrina como límite habitual de precaución en adultos con necesidad de cautela cardiovascular.","🦷 LOCAL ANESTHESIA: if clinically stable, may be performed according to procedure and comorbidities. With cardiovascular compromise, use vasoconstrictor cautiously and safe injection technique; ADA notes 0.04 mg epinephrine as a common precautionary adult limit when cardiovascular caution is needed.")
    }
    if(p==DentalProcedure19.SEDATION) return if(age<18) tr(lang,"🟠 SEDACIÓN: no debe autorizarse sólo con signos vitales/IMC. Requiere evaluación pre-sedación, ayuno cuando corresponda, personal capacitado, monitorización y equipo de rescate; en pediatría seguir AAP/AAPD y el nivel de sedación previsto.","🟠 SEDATION: do not clear based only on vitals/BMI. Requires pre-sedation evaluation, appropriate fasting when applicable, trained personnel, monitoring and rescue equipment; in pediatrics follow AAP/AAPD guidance and intended sedation level.") else tr(lang,"🟠 SEDACIÓN: no debe autorizarse sólo con signos vitales/IMC. Requiere evaluación pre-sedación, ayuno cuando corresponda, personal capacitado, monitorización y equipo de rescate; en pediatría seguir AAP/AAPD y el nivel de sedación previsto.","🟠 SEDATION: do not clear based only on vitals/BMI. Requires pre-sedation evaluation, appropriate fasting when applicable, trained personnel, monitoring and rescue equipment; in pediatrics follow AAP/AAPD guidance and intended sedation level.")
    return when(p){
        DentalProcedure19.DIAGNOSTIC->tr(lang,"🟢 EXPLORACIÓN/DIAGNÓSTICO: puede realizarse si el paciente está estable; registrar hallazgos y completar historia clínica.","🟢 EXAMINATION/DIAGNOSIS: may be performed if the patient is stable; document findings and complete the medical history.")
        DentalProcedure19.RESTORATIVE->tr(lang,"🟢 OPERATORIA/RESTAURACIÓN: puede realizarse si no hay contraindicaciones clínicas adicionales. La anestesia local se evalúa por separado y, en pediatría, la dosis depende del peso.","🟢 RESTORATIVE CARE: may be performed if there are no additional clinical contraindications. Local anesthesia is assessed separately and pediatric dosing is weight-based.")
        DentalProcedure19.ENDODONTIC->tr(lang,"🟢 ENDODONCIA: puede realizarse si el paciente está estable; si existe infección con fiebre/malestar, priorizar control del foco y diferir lo electivo.","🟢 ENDODONTICS: may be performed if stable; if infection is accompanied by fever/malaise, prioritize source control and defer elective care.")
        DentalProcedure19.PERIODONTAL->tr(lang,"🟢 PERIODONCIA: puede realizarse según diagnóstico y extensión. En sangrado no controlable o deterioro sistémico, detener y valorar urgencia.","🟢 PERIODONTAL CARE: may be performed according to diagnosis and extent. With uncontrolled bleeding or systemic deterioration, stop and assess urgently.")
        DentalProcedure19.EXTRACTION->tr(lang,"🟢 EXTRACCIÓN: puede realizarse si el paciente está estable y existe indicación clínica, con valoración de hemostasia, medicamentos y anestesia. No equivale a autorización quirúrgica automática.","🟢 EXTRACTION: may be performed if stable and clinically indicated, after assessing hemostasis, medications and anesthesia. This is not automatic surgical clearance.")
        DentalProcedure19.SURGERY->tr(lang,"🟢 CIRUGÍA ORAL: puede considerarse sólo tras revisar riesgo médico, hemostasia, infección, anestesia y complejidad. Una cirugía mayor puede requerir valoración médica/anestésica adicional aunque los signos vitales estén normales.","🟢 ORAL SURGERY: may be considered only after medical risk, hemostasis, infection, anesthesia and complexity are reviewed. Major surgery may require additional medical/anesthesia assessment even when vitals are normal.")
        DentalProcedure19.ORTHODONTIC->tr(lang,"🟢 ORTODONCIA/ORTOPEDIA: puede realizarse en paciente estable; edad, crecimiento y diagnóstico odontológico determinan indicación. El IMC no decide por sí solo la indicación ortodóncica.","🟢 ORTHODONTICS/DENTOFACIAL ORTHOPEDICS: may be performed in a stable patient; age, growth and dental diagnosis determine indication. BMI alone does not determine orthodontic indication.")
        DentalProcedure19.PROSTHETIC->tr(lang,"🟢 PRÓTESIS: puede realizarse en paciente estable. Si se planea cirugía de implantes, añadir evaluación médica, metabólica y periodontal específica; el IMC aislado no autoriza ni contraindica implantes.","🟢 PROSTHODONTICS: may be performed in a stable patient. If implant surgery is planned, add specific medical, metabolic and periodontal assessment; BMI alone neither clears nor contraindicates implants.")
        DentalProcedure19.LOCAL_ANESTHESIA->tr(lang,"🦷 ANESTESIA LOCAL: puede realizarse si el paciente está estable y no hay contraindicación del fármaco. En menores, calcular la dosis por peso y anestésico específico; no usar el IMC como sustituto de mg/kg.","🦷 LOCAL ANESTHESIA: may be performed if stable and there is no drug-specific contraindication. In children, calculate dose by body weight and specific anesthetic; do not use BMI instead of mg/kg.")
        DentalProcedure19.SEDATION->tr(lang,"🟠 SEDACIÓN/ANESTESIA PROFUNDA: no se autoriza con esta pantalla. Requiere evaluación preanestésica, selección del nivel de sedación, ayuno cuando corresponda, personal acreditado, monitorización y equipo de rescate; en pediatría seguir AAP/AAPD.","🟠 SEDATION/DEEP ANESTHESIA: this screen does not clear it. Requires pre-anesthesia assessment, intended sedation level, appropriate fasting, credentialed personnel, monitoring and rescue equipment; in pediatrics follow AAP/AAPD.")
    }
}

@Composable
private fun ResultCard19(text:String) {
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
        Text(text,Modifier.padding(12.dp),fontWeight=FontWeight.SemiBold)
    }
}

@Composable
fun VitalsInteractiveV19Screen(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onBack:()->Unit) {
    val patientAge=session.profile.age.toIntOrNull()
    val patientSex=session.profile.sex
    var ageText by rememberRecordState("vitals.age",session.profile.age)
    var sex by rememberRecordState("vitals.sex",if(patientSex=="Masculino"||patientSex=="Femenino")patientSex else "No especificado")
    val ageForCalc=patientAge ?: 18
    val ageValid=patientAge in 0..120
    val b=vitalBandForAge19(ageForCalc)
    var spo2 by rememberRecordState("vitals.spo2",""); var rr by rememberRecordState("vitals.rr",""); var hr by rememberRecordState("vitals.hr","")
    var sys by rememberRecordState("vitals.sys",""); var dia by rememberRecordState("vitals.dia",""); var temp by rememberRecordState("vitals.temp",""); var glucose by rememberRecordState("vitals.glucose","")
    var glucoseContext by rememberRecordState("vitals.glucoseContext",GlucoseContext19.RANDOM)
    var glucoseStatus by rememberRecordState("vitals.glucoseStatus",GlucoseStatus19.UNKNOWN)
    var glucoseMedicationsRaw by rememberRecordState("vitals.glucoseMedications","")
    var oxygenContext by rememberRecordState("vitals.oxygenContext",OxygenContext19.ROOM_AIR)
    var oxygenFlow by rememberRecordState("vitals.oxygenFlow","")
    var respiratoryDisease by rememberRecordState("vitals.respiratoryDisease",false)
    var currentSmoking by rememberRecordState("vitals.currentSmoking",false)
    var selectedSignsRaw by rememberRecordState("vitals.dentalSigns","")
    var painScore by rememberRecordState("vitals.painScore","")
    var weight by rememberRecordState("vitals.weight",session.profile.weightKg); var height by rememberRecordState("vitals.height",session.profile.heightCm)
    val bmi=run{val w=weight.toDoubleOrNull();val h=height.toDoubleOrNull()?.div(100.0);if(w!=null&&h!=null&&h>0)w/h.pow(2)else null}
    LaunchedEffect(ageText,sex,spo2,rr,hr,sys,dia,temp,glucose,weight,height,bmi,selectedSignsRaw,painScore,glucoseContext,glucoseStatus,glucoseMedicationsRaw,oxygenContext,oxygenFlow,respiratoryDisease,currentSmoking) {
        val bp = if (sys.isNotBlank() || dia.isNotBlank()) "$sys/$dia" else ""
        val updated = session.profile.copy(
            age = ageText.filter(Char::isDigit).take(3),
            sex = if(sex=="No especificado") session.profile.sex else sex,
            heartRate = hr,
            respiratoryRate = rr,
            bloodPressure = bp,
            temperature = temp,
            spo2 = spo2,
            weightKg = weight,
            heightCm = height,
            bmi = bmi?.let{"%.1f".format(it)} ?: "",
            clinicalSigns = selectedSignsRaw,
            painScore = painScore,
            glucose = glucose,
            glucoseContext = glucoseContext.name
        )
        if (updated != session.profile) onSessionChanged(session.copy(profile = updated))
    }
    ResponsiveScreenV17(tr(lang,"Signos, síntomas y triage clínico","Clinical signs, symptoms and triage"),tr(lang,"Registra parámetros, signos y síntomas y obtén una orientación educativa sobre continuidad, diferimiento o referencia.","Record parameters, signs and symptoms and get educational guidance on proceeding, deferring or referring."),onBack){profile->
        ResponsiveSectionV17(tr(lang,"1 · Edad y sexo","1 · Age and sex")){
            OutlinedTextField(value=ageText,onValueChange={ageText=it.filter(Char::isDigit).take(3)},label={Text(tr(lang,"Edad en años *","Age in years *"))},modifier=Modifier.fillMaxWidth(),singleLine=true)
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Femenino","Masculino","No especificado").forEach{s->FilterChip(sex==s,{sex=s},{Text(if(lang=="en"&&s=="Femenino")"Female" else if(lang=="en"&&s=="Masculino")"Male" else if(lang=="en")"Not specified" else s)},modifier=Modifier.weight(1f))}}
            if(!ageValid) ResultCard19(tr(lang,"⚠️ La edad es obligatoria para el triage.","⚠️ Age is required for triage."))
            Text(tr(lang,"La valoración cruza edad, sexo, talla, signos/síntomas y antecedentes sistémicos; la TA pediátrica además requiere talla.","The assessment cross-checks age, sex, height, signs/symptoms and systemic history; pediatric BP also requires height."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"2 · Referencias fisiológicas","2 · Physiologic references")){
            Text(tr(lang,"Grupo etario: ${b.label}. FR ${b.rrMin}–${b.rrMax}/min · FC ${b.hrMin}–${b.hrMax}/min.","Age group: ${b.label}. RR ${b.rrMin}–${b.rrMax}/min · HR ${b.hrMin}–${b.hrMax}/min."))
            Text(clinicalReferenceSourcesV20(lang),style=MaterialTheme.typography.bodySmall)
            pediatricBpScreen19(ageForCalc,sex)?.let{Text(it.label,style=MaterialTheme.typography.bodySmall)}
            Text(tr(lang,"SpO₂ habitual en personas sanas: 95–100 %. Temperatura habitual aproximada: 36.1–37.2 °C.","Usual SpO₂ in healthy people: 95–100%. Approximate usual temperature: 36.1–37.2 °C."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"3 · FR, FC y presión arterial","3 · RR, HR and blood pressure")){
            val cols=if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2
            AdaptiveGridV17(4,cols){i->when(i){
                0->OutlinedTextField(rr,{rr=it.filter(Char::isDigit).take(3)},label={Text("FR /min")},modifier=Modifier.fillMaxWidth())
                1->OutlinedTextField(hr,{hr=it.filter(Char::isDigit).take(3)},label={Text("FC /min")},modifier=Modifier.fillMaxWidth())
                2->OutlinedTextField(sys,{sys=it.filter(Char::isDigit).take(3)},label={Text(tr(lang,"TA sistólica","Systolic BP"))},modifier=Modifier.fillMaxWidth())
                else->OutlinedTextField(dia,{dia=it.filter(Char::isDigit).take(3)},label={Text(tr(lang,"TA diastólica","Diastolic BP"))},modifier=Modifier.fillMaxWidth())
            }}
            Text("FR ${b.rrMin}–${b.rrMax}: ${vitalRange19(rr.toDoubleOrNull(),b.rrMin.toDouble(),b.rrMax.toDouble(),lang)}")
            Text("FC ${b.hrMin}–${b.hrMax}: ${vitalRange19(hr.toDoubleOrNull(),b.hrMin.toDouble(),b.hrMax.toDouble(),lang)}")
            ResultCard19(rhythmAction19(rr.toIntOrNull(),hr.toIntOrNull(),b,lang)); ResultCard19(bpAction19(patientAge,sex,height.toDoubleOrNull(),sys.toIntOrNull(),dia.toIntOrNull(),lang))
        }
        ResponsiveSectionV17(tr(lang,"4 · Temperatura y oxigenación","4 · Temperature and oxygenation")){
            OutlinedTextField(temp,{temp=it.filter{ch->ch.isDigit()||ch=='.'}.take(5)},label={Text("°C")},modifier=Modifier.fillMaxWidth())
            ResultCard19(temperature19(temp.toDoubleOrNull(),lang)); ResultCard19(temperatureAction19(temp.toDoubleOrNull(),lang))
            OutlinedTextField(spo2,{spo2=it.filter(Char::isDigit).take(3)},label={Text("SpO₂ %")},modifier=Modifier.fillMaxWidth())
            val s=spo2.toIntOrNull()
            Text(tr(lang,"Contexto de oxigenación","Oxygenation context"),fontWeight=FontWeight.Black)
            OxygenContext19.entries.forEach{ctx->
                FilterChip(oxygenContext==ctx,{oxygenContext=ctx},{Text(oxygenContextLabel19(ctx,lang))},modifier=Modifier.fillMaxWidth())
            }
            if(oxygenContext==OxygenContext19.SUPPLEMENTAL_OXYGEN){
                OutlinedTextField(
                    oxygenFlow,
                    {oxygenFlow=it.filter{ch->ch.isDigit()||ch=='.'}.take(5)},
                    label={Text(tr(lang,"Flujo de O₂ (L/min) · opcional","O₂ flow (L/min) · optional"))},
                    modifier=Modifier.fillMaxWidth()
                )
            }
            FilterChip(respiratoryDisease,{respiratoryDisease=!respiratoryDisease},{Text(tr(lang,"Enfermedad respiratoria conocida","Known respiratory disease"))},modifier=Modifier.fillMaxWidth())
            FilterChip(currentSmoking,{currentSmoking=!currentSmoking},{Text(tr(lang,"Tabaquismo actual","Current tobacco use"))},modifier=Modifier.fillMaxWidth())
            ResultCard19(when{s==null->tr(lang,"Escribe la SpO₂.","Enter SpO₂.");s>=95->tr(lang,"95–100 %: habitual en la mayoría de personas sanas, especialmente en aire ambiente.","95–100%: usual in most healthy people, especially on room air.");s>=92->tr(lang,"92–94 %: repetir y contextualizar; puede ser relevante según enfermedad y oxígeno suplementario.","92–94%: repeat and contextualize; may be clinically relevant depending on disease and supplemental oxygen.");s>=90->tr(lang,"90–91 %: baja; confirmar y valorar síntomas y contexto.","90–91%: low; confirm and assess symptoms and context.");else->tr(lang,"<90 %: potencialmente urgente; confirmar inmediatamente y valorar clínicamente.","<90%: potentially urgent; confirm immediately and assess clinically.")})
            ResultCard19(spo2ClinicalContext19(s,oxygenContext,oxygenFlow,respiratoryDisease,currentSmoking,lang))
            ResultCard19(spo2Action19(s,lang))
        }
        ResponsiveSectionV17(
            tr(lang,"5 · Glucosa capilar y contexto metabólico","5 · Capillary glucose and metabolic context"),
            tr(lang,"La interpretación cambia según diabetes, prediabetes/resistencia a la insulina, tratamiento y momento de la medición.","Interpretation changes with diabetes, prediabetes/insulin resistance, treatment and measurement timing.")
        ){
            OutlinedTextField(glucose,{glucose=it.filter(Char::isDigit).take(4)},label={Text("Glucosa capilar · mg/dL")},modifier=Modifier.fillMaxWidth())
            Text(tr(lang,"Condición metabólica","Metabolic condition"),fontWeight=FontWeight.Black)
            GlucoseStatus19.entries.forEach{status->
                FilterChip(glucoseStatus==status,{glucoseStatus=status},{Text(glucoseStatusLabel19(status,lang))},modifier=Modifier.fillMaxWidth())
            }
            Text(tr(lang,"Medicamentos para controlar la glucosa","Glucose-lowering medications"),fontWeight=FontWeight.Black)
            val glucoseMedOptions=listOf(
                "Metformina","Insulina","Sulfonilurea / secretagogo","Agonista GLP-1 / dual","Inhibidor SGLT2","Otro / no especificado"
            )
            val selectedGlucoseMeds=glucoseMedicationsRaw.split("|").filter{it.isNotBlank()}.toSet()
            glucoseMedOptions.forEach{med->
                FilterChip(selectedGlucoseMeds.contains(med),{
                    glucoseMedicationsRaw=(if(selectedGlucoseMeds.contains(med)) selectedGlucoseMeds-med else selectedGlucoseMeds+med).joinToString("|")
                },{Text(med)},modifier=Modifier.fillMaxWidth())
            }
            Text(tr(lang,"Momento de la medición","Measurement timing"),fontWeight=FontWeight.Black)
            GlucoseContext19.entries.forEach{ctx->FilterChip(glucoseContext==ctx,{glucoseContext=ctx},{Text(glucoseContext19(ctx,lang))},modifier=Modifier.fillMaxWidth())}
            ResultCard19(glucose19(glucose.toIntOrNull(),glucoseContext,lang))
            ResultCard19(glucoseClinicalContext19(glucose.toIntOrNull(),glucoseStatus,glucoseContext,selectedGlucoseMeds,lang))
            ResultCard19(glucoseAction19(glucose.toIntOrNull(),glucoseStatus,glucoseContext,selectedGlucoseMeds,lang))
        }
        ResponsiveSectionV17(
            tr(lang,"6 · Antecedentes sistémicos que pueden afectar la atención","6 · Systemic conditions that may affect care"),
            tr(lang,"Revisa las enfermedades registradas en antecedentes patológicos para contextualizar el triage.","Review diseases recorded in the medical history to contextualize triage.")
        ){
            val activeDiseases=session.history.diseases.filterValues{it.present}
            if(activeDiseases.isEmpty()){
                ResultCard19(tr(lang,"🟢 No hay enfermedades sistémicas registradas como presentes.","🟢 No systemic diseases are recorded as present."))
            }else{
                Text(tr(lang,"Enfermedades sistémicas registradas","Recorded systemic conditions"),fontWeight=FontWeight.Black)
                activeDiseases.forEach{(id,answer)->
                    Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
                        Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){
                            Text(id,fontWeight=FontWeight.Black)
                            if(answer.currentStatus.isNotBlank()) Text(tr(lang,"Estado: ","Status: ")+answer.currentStatus)
                            if(answer.complications.isNotBlank()) Text(tr(lang,"Complicaciones: ","Complications: ")+answer.complications)
                            if(answer.treatment.isNotBlank()) Text(tr(lang,"Tratamiento referido: ","Reported treatment: ")+answer.treatment)
                        }
                    }
                }
                ResultCard19(tr(lang,"⚠️ Cruzar antecedentes con signos, síntomas, medicamentos, alergias y procedimiento antes de continuar, diferir o referir.","⚠️ Cross-check history with signs, symptoms, medications, allergies and procedure before proceeding, deferring or referring."))
            }
        }
        ResponsiveSectionV17(
            tr(lang,"7 · Signos y síntomas odontológicos","7 · Dental signs and symptoms"),
            tr(lang,"La edad, el sexo, los signos vitales y los antecedentes modifican la interpretación; las referencias deben provenir de tablas clínicas apropiadas para el grupo del paciente.","Age, sex, vital signs and medical history modify interpretation; references should come from clinical tables appropriate for the patient group.")
        ){
            val selectedSigns=selectedSignsRaw.split("|").filter{it.isNotBlank()}.toSet()
            AdaptiveGridV17(dentalSignsSymptoms19.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                val item=dentalSignsSymptoms19[i]
                FilterChip(selectedSigns.contains(item),{selectedSignsRaw=(if(selectedSigns.contains(item))selectedSigns-item else selectedSigns+item).joinToString("|")},{Text(item)},modifier=Modifier.fillMaxWidth())
            }
            ResultCard19(triageAction19(selectedSigns,lang))
            OutlinedTextField(painScore,{painScore=it.filter(Char::isDigit).take(2)},label={Text(tr(lang,"Dolor 0–10","Pain 0–10"))},modifier=Modifier.fillMaxWidth())
            val pain=painScore.toIntOrNull()
            ResultCard19(if(pain==null) tr(lang,"Escala de dolor opcional: 0 sin dolor · 10 máximo.","Optional pain scale: 0 no pain · 10 maximum.") else tr(lang,"Dolor registrado: \$pain/10. La intensidad ayuda a priorizar diagnóstico y tratamiento, pero no sustituye el diagnóstico odontológico.","Recorded pain: \$pain/10. Intensity helps prioritize diagnosis and treatment but does not replace the dental diagnosis."))
        }
        ResponsiveSectionV17(tr(lang,"8 · Peso, talla e IMC","8 · Weight, height and BMI")){
            val cols=if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2
            AdaptiveGridV17(2,cols){i->if(i==0)OutlinedTextField(weight,{weight=it.filter{ch->ch.isDigit()||ch=='.'}.take(6)},label={Text("kg")},modifier=Modifier.fillMaxWidth())else OutlinedTextField(height,{height=it.filter{ch->ch.isDigit()||ch=='.'}.take(6)},label={Text("cm")},modifier=Modifier.fillMaxWidth())}
            Text(if(bmi==null)tr(lang,"IMC = peso / talla²","BMI = weight / height²") else "IMC = ${"%.1f".format(bmi)} kg/m²",fontWeight=FontWeight.Bold)
            ResultCard19(bmiAction19(ageForCalc,sex,bmi,lang))
        }

        ResponsiveSectionV17(
            tr(lang,"9 · Semáforo de atención odontológica","9 · Dental care traffic light"),
            tr(lang,"Resume si es razonable continuar, limitar o diferir la atención con los datos registrados.","Summarizes whether care should proceed, be limited, or be deferred from the recorded data.")
        ){
            val activeSystemicCount=session.history.diseases.count{it.value.present}
            val semaphore=dentalActivitySemaphore19(
                age=patientAge,
                sex=sex,
                heightCm=height.toDoubleOrNull(),
                sys=sys.toIntOrNull(),
                dia=dia.toIntOrNull(),
                rr=rr.toIntOrNull(),
                hr=hr.toIntOrNull(),
                spo2=spo2.toIntOrNull(),
                temp=temp.toDoubleOrNull(),
                glucose=glucose.toIntOrNull(),
                signs=selectedSignsRaw.split("|").filter{it.isNotBlank()}.toSet(),
                activeSystemic=activeSystemicCount,
                glucoseStatus=glucoseStatus,
                oxygenContext=oxygenContext
            )
            DentalActivitySemaphoreCard19(semaphore,lang)
        }

        ResponsiveSectionV17(
            tr(lang,"10 · Tratamientos posibles hoy","10 · Treatments that may be possible today"),
            tr(lang,"La aplicación cruza automáticamente edad, sexo, peso, talla, IMC, TA, FR, FC, SpO₂, temperatura, glucosa, dolor y signos/síntomas. No tienes que seleccionar el tratamiento: sólo aparecen hasta 3 opciones compatibles con los datos registrados.","The app automatically cross-checks age, sex, weight, height, BMI, BP, RR, HR, SpO₂, temperature, glucose, pain and signs/symptoms. You do not select the treatment: only up to 3 options compatible with the recorded data are shown.")
        ){
            val selectedSigns=selectedSignsRaw.split("|").filter{it.isNotBlank()}.toSet()
            val decision=evaluateClinicalDecisionV1(
                age=ageForCalc, sex=sex, sys=sys.toIntOrNull(), dia=dia.toIntOrNull(),
                rr=rr.toIntOrNull(), hr=hr.toIntOrNull(), spo2=spo2.toIntOrNull(),
                glucose=glucose.toIntOrNull(), temp=temp.toDoubleOrNull(), bmi=bmi,
                signs=selectedSigns, pain=painScore.toIntOrNull(), profile=session.profile
            )
            val candidates=decision.treatments.take(3)
            if(candidates.isEmpty()){
                ResultCard19(tr(lang,"🚨 No hay tratamiento dental electivo compatible con los datos actuales. Primero corrige o valora el parámetro alterado, el antecedente relevante o el signo de alarma.","🚨 No elective dental treatment is compatible with the current data. First correct or assess the abnormal parameter, relevant history, or red flag."))
            } else {
                candidates.forEachIndexed { index,item ->
                    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
                        Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                            Text("${index+1}. $item",fontWeight=FontWeight.Black)
                            Text(if(lang=="en") decision.detailEn else decision.detailEs)
                        }
                    }
                }
            }
            Text(
                tr(lang,"Por cada dato seleccionado, la aplicación indica qué puede continuar y qué debe diferirse. Un dato normal no autoriza por sí solo cirugía, anestesia o sedación.","For each selected finding, the app indicates what may proceed and what should be deferred. A normal value alone does not clear surgery, anesthesia or sedation."),
                style=MaterialTheme.typography.bodySmall
            )
            ResultCard19(
                treatmentRestrictionBySign19(
                    selectedSigns,sys.toIntOrNull(),dia.toIntOrNull(),rr.toIntOrNull(),hr.toIntOrNull(),
                    spo2.toIntOrNull(),glucose.toIntOrNull(),temp.toDoubleOrNull(),bmi,lang
                )
            )
            ResultCard19(
                anestheticRecommendation19(
                    ageForCalc,sys.toIntOrNull(),dia.toIntOrNull(),hr.toIntOrNull(),
                    spo2.toIntOrNull(),temp.toDoubleOrNull(),selectedSigns,bmi,lang
                )
            )
        }
        val dashboardSigns=selectedSignsRaw.split("|").filter{it.isNotBlank()}.toSet()
        ClinicalDecisionDashboardV1(
            lang=lang, session=session, age=ageForCalc, sex=sex,
            sys=sys.toIntOrNull(), dia=dia.toIntOrNull(), rr=rr.toIntOrNull(), hr=hr.toIntOrNull(),
            spo2=spo2.toIntOrNull(), glucose=glucose.toIntOrNull(), temp=temp.toDoubleOrNull(),
            bmi=bmi, signs=dashboardSigns, pain=painScore.toIntOrNull(), onSessionChanged=onSessionChanged
        )
        NoticeCard(tr(lang,"Fuentes educativas: tablas pediátricas AAP para TA por edad/sexo/talla; ADA Standards 2026 para interpretación de glucosa; FDA para limitaciones de la oximetría. La herramienta debe contrastar cada parámetro con la referencia clínica apropiada para edad, sexo y contexto; no sustituye protocolos institucionales ni valoración médica.","Educational sources: AAP pediatric BP tables by age/sex/height; ADA Standards 2026 for glucose interpretation; FDA for pulse-oximetry limitations. Each parameter should be checked against the appropriate clinical reference for age, sex and context; this tool does not replace institutional protocols or medical assessment."))
    }
}
@Composable
fun VitalsInteractiveV19Screen(lang:String,session:EducationalSession,onBack:()->Unit){VitalsInteractiveV19Screen(lang,session,{},onBack)}
@Composable
fun VitalsInteractiveV19Screen(lang:String,onBack:()->Unit){VitalsInteractiveV19Screen(lang,EducationalSession(),{},onBack)}

private enum class DentalTriageLight19 { GREEN, YELLOW, RED }

private data class DentalActivitySemaphore19(
    val light:DentalTriageLight19,
    val titleEs:String,
    val titleEn:String,
    val reasonEs:String,
    val reasonEn:String,
    val activitiesEs:List<String>,
    val activitiesEn:List<String>
)

private fun dentalActivitySemaphore19(
    age:Int?,
    sex:String,
    heightCm:Double?,
    sys:Int?,
    dia:Int?,
    rr:Int?,
    hr:Int?,
    spo2:Int?,
    temp:Double?,
    glucose:Int?,
    signs:Set<String>,
    activeSystemic:Int,
    glucoseStatus:GlucoseStatus19,
    oxygenContext:OxygenContext19
):DentalActivitySemaphore19 {
    val emergencySigns=setOf("Sangrado oral no controlable","Inflamación facial/cervical","Disnea","Dolor torácico","Alteración de conciencia","Convulsiones")
    val redReasons=mutableListOf<String>()
    if(signs.any{it in emergencySigns})redReasons.add("signo/síntoma de alarma")
    if(sys!=null && (sys>180 || dia?.let{it>120}==true))redReasons.add("TA >180/120 mmHg")
    if(spo2!=null && spo2<90)redReasons.add("SpO₂ <90%")
    if(temp!=null && temp>=40)redReasons.add("temperatura ≥40 °C")
    if(glucose!=null && glucose<54)redReasons.add("hipoglucemia <54 mg/dL")
    if(glucose!=null && glucose>=350)redReasons.add("glucosa ≥350 mg/dL")
    if(hr!=null && (hr<40 || hr>140))redReasons.add("FC extrema")
    if(rr!=null && (rr<8 || rr>30))redReasons.add("FR extrema")
    if(redReasons.isNotEmpty()){
        return DentalActivitySemaphore19(
            DentalTriageLight19.RED,
            "🔴 ROJO · NO TRABAJAR EN CONSULTA AMBULATORIA",
            "🔴 RED · DO NOT TREAT IN THE OUTPATIENT OFFICE",
            "Criterio: " + redReasons.joinToString(", ") + ". No permite atención odontológica rutinaria/electiva. Primero estabilizar, repetir/confirmar cuando corresponda y activar la ruta médica o de urgencias.",
            "Criterion: " + redReasons.joinToString(", ") + ". Routine/elective dental care is not allowed. Stabilize first, repeat/confirm when appropriate, and activate the medical/emergency pathway.",
            listOf("⛔ No procedimientos electivos ni invasivos","⛔ No extracción/cirugía/endodoncia electiva","⛔ No sedación ni anestesia electiva","🚑 Activar urgencias/derivación según criterio clínico","🔁 Repetir medición cuando corresponda"),
            listOf("⛔ No elective or invasive procedures","⛔ No elective extraction/surgery/endodontics","⛔ No elective sedation or anesthesia","🚑 Activate emergency/referral pathway as indicated","🔁 Repeat measurements when appropriate")
        )
    }
    val requirements=mutableListOf<String>()
    if(age==null)requirements.add("registrar edad")
    if(age!=null && age<18 && (sex=="No especificado" || sex.isBlank()))requirements.add("registrar sexo para TA pediátrica")
    if(age!=null && age<18 && heightCm==null)requirements.add("registrar talla para TA pediátrica")
    if(sys!=null && dia!=null && (sys>=140 || dia>=90))requirements.add("repetir TA y valorar médico si persiste")
    if(age!=null && rr!=null){val b=clinicalVitalBandV20(age);if(rr<b.rrMin||rr>b.rrMax)requirements.add("repetir FR en reposo y buscar causa")}
    if(age!=null && hr!=null){val b=clinicalVitalBandV20(age);if(hr<b.hrMin||hr>b.hrMax)requirements.add("repetir FC en reposo y buscar causa")}
    if(spo2!=null && spo2<95)requirements.add("repetir SpO₂ y valorar síntomas, perfusión y dispositivo")
    if(glucose!=null && (glucose>=180 || glucose<70))requirements.add("confirmar contexto de glucosa, fármacos y comida")
    if(temp!=null && temp>=38)requirements.add("repetir temperatura y buscar causa")
    if(signs.contains("Fiebre/malestar"))requirements.add("valorar infección sistémica")
    if(signs.contains("Inflamación intraoral")||signs.contains("Supuración/fístula"))requirements.add("controlar foco odontógeno antes de electivo")
    if(activeSystemic>0)requirements.add("revisar enfermedad sistémica, medicamentos y estabilidad")
    if(glucoseStatus==GlucoseStatus19.UNKNOWN && glucose!=null)requirements.add("documentar diabetes/prediabetes/resistencia a la insulina")
    if(oxygenContext==OxygenContext19.UNKNOWN && spo2!=null)requirements.add("documentar aire ambiente u oxígeno")
    if(requirements.isNotEmpty()){
        return DentalActivitySemaphore19(
            DentalTriageLight19.YELLOW,
            "🟠 AMARILLO · TRABAJAR SÓLO CON REQUISITOS",
            "🟠 YELLOW · TREAT ONLY WITH REQUIREMENTS",
            "No hay un criterio rojo inmediato, pero falta confirmar estabilidad o existe un factor que cambia el riesgo. Amarillo no autoriza automáticamente procedimientos invasivos.",
            "There is no immediate red criterion, but stability needs confirmation or a risk-changing factor is present. Yellow does not automatically clear invasive procedures.",
            listOf("✅ Historia, exploración, documentación y prevención si está estable.","🔁 Requisitos: " + requirements.joinToString("; ") + ".","⚕️ Si se confirma una alteración o aparecen síntomas, diferir lo invasivo y valorar médicamente.","💉 Anestesia/sedación sólo después de resolver la condición limitante y según protocolo."),
            listOf("✅ History, examination, documentation and prevention if stable.","🔁 Requirements: " + requirements.joinToString("; ") + ".","⚕️ If an abnormality is confirmed or symptoms appear, defer invasive care and obtain medical assessment.","💉 Anesthesia/sedation only after the limiting condition is addressed and per protocol.")
        )
    }
    return DentalActivitySemaphore19(
        DentalTriageLight19.GREEN,
        "🟢 VERDE · SE PUEDE TRABAJAR",
        "🟢 GREEN · CARE MAY PROCEED",
        "No se detecta criterio rojo ni requisito pendiente importante en los datos registrados. Es orientación educativa de triage, no autorización legal o anestésica.",
        "No red criterion or major pending requirement was detected in the recorded data. This is educational triage guidance, not legal or anesthesia clearance.",
        listOf("✅ Historia y exploración","✅ Prevención e higiene","✅ Restauraciones y procedimientos no quirúrgicos","✅ Anestesia local si está indicada y el producto/protocolo lo permite","⚠️ Procedimientos invasivos sólo si diagnóstico, procedimiento y estado clínico lo permiten"),
        listOf("✅ History and examination","✅ Prevention and hygiene","✅ Restorative and non-surgical procedures","✅ Local anesthesia when indicated and permitted","⚠️ Invasive procedures only when diagnosis, procedure and clinical status allow")
    )
}

@Composable
private fun DentalActivitySemaphoreCard19(light:DentalActivitySemaphore19,lang:String){
    val (container,title,reason,activities)=when(light.light){
        DentalTriageLight19.GREEN->Quadruple(
            MaterialTheme.colorScheme.primaryContainer,
            if(lang=="en")light.titleEn else light.titleEs,
            if(lang=="en")light.reasonEn else light.reasonEs,
            if(lang=="en")light.activitiesEn else light.activitiesEs
        )
        DentalTriageLight19.YELLOW->Quadruple(
            MaterialTheme.colorScheme.secondaryContainer,
            if(lang=="en")light.titleEn else light.titleEs,
            if(lang=="en")light.reasonEn else light.reasonEs,
            if(lang=="en")light.activitiesEn else light.activitiesEs
        )
        DentalTriageLight19.RED->Quadruple(
            MaterialTheme.colorScheme.errorContainer,
            if(lang=="en")light.titleEn else light.titleEs,
            if(lang=="en")light.reasonEn else light.reasonEs,
            if(lang=="en")light.activitiesEn else light.activitiesEs
        )
    }
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=container)){
        Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){
            Text(title,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
            Text(reason)
            Text(if(lang=="en")"Activities currently compatible:" else "Actividades compatibles en este momento:",fontWeight=FontWeight.Bold)
            activities.forEach{Text("• $it")}
            if(light.light==DentalTriageLight19.RED){
                Text(if(lang=="en")"⚠️ Red means do not provide routine/elective dental care in the office. If this is an emergency, activate the emergency/referral pathway rather than proceeding with routine treatment."
                    else "⚠️ Rojo significa no realizar atención odontológica rutinaria/electiva en consulta. Si se trata de una urgencia, activar la ruta de emergencia/derivación en lugar de continuar con el tratamiento habitual.",
                    fontWeight=FontWeight.Black)
            }
        }
    }
}

private data class Quadruple<A,B,C,D>(val first:A,val second:B,val third:C,val fourth:D)

private fun cpodStatus19(status:ToothStatus,lang:String):String = when(status) {
    ToothStatus.HEALTHY -> tr(lang,"Sano / presente","Sound / present")
    ToothStatus.CARIES -> tr(lang,"Cariado","Decayed")
    ToothStatus.RESTORED -> tr(lang,"Obturado","Filled")
    ToothStatus.MISSING_CARIES -> tr(lang,"Ausente por caries","Missing due to caries")
    ToothStatus.MISSING_OTHER -> tr(lang,"Ausente por otra causa","Missing other cause")
    ToothStatus.EXTRACTION_INDICATED -> tr(lang,"Extracción indicada","Extraction indicated")
    ToothStatus.SEALANT -> tr(lang,"Sellador / no cuenta como O","Sealant / not counted as F")
}

@Composable
fun CpodInteractiveV19Screen(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onBack:()->Unit) {
    var primary by remember{mutableStateOf(false)}
    val shown=if(primary)ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    var selected by remember{mutableStateOf(shown.first())}
    if(selected !in shown) selected=shown.first()
    val record=session.teeth[selected]?:ToothRecord()
    val result=ClinicalEngines.cpod(session.teeth,primary)
    val choices=listOf(ToothStatus.HEALTHY,ToothStatus.CARIES,ToothStatus.RESTORED,ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER,ToothStatus.SEALANT)
    fun setStatus(status:ToothStatus) {
        val present=session.presentTeeth.toMutableSet()
        if(status==ToothStatus.MISSING_CARIES||status==ToothStatus.MISSING_OTHER)present.remove(selected) else present.add(selected)
        onSessionChanged(session.copy(teeth=session.teeth+(selected to record.copy(status=status)),presentTeeth=present))
    }

    ResponsiveScreenV17(tr(lang,"CPOD / ceod interactivo","Interactive DMFT / dmft"),tr(lang,"Toca cada diente, clasifícalo y observa el cálculo automático.","Tap each tooth, classify it and view the automatic calculation."),onBack) { profile ->
        ResponsiveSectionV17(tr(lang,"Referencia visual opcional","Optional visual reference"),tr(lang,"Consulta esta lámina cuando necesites recordar las categorías; el registro se realiza por diente en los pasos siguientes.","Use this sheet when you need to review the categories; tooth-by-tooth recording is done in the following steps.")) { UploadedCpodRefsV51(lang) }
        ResponsiveSectionV17(tr(lang,"1 · Dentición","1 · Dentition")) {
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2) { i ->
                val p=i==1
                FilterChip(primary==p,{primary=p},{Text(if(p)tr(lang,"Temporal · ceod","Primary · dmft") else tr(lang,"Permanente · CPOD","Permanent · DMFT"))},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"2 · Selecciona el diente","2 · Select tooth"),tr(lang,"Maxilar arriba · mandibular abajo.","Maxillary above · mandibular below.")) {
            DentalArchSelector(shown,selected,{selected=it}) { tooth -> session.teeth[tooth]?.status?.let{it!=ToothStatus.HEALTHY}==true }
        }
        ResponsiveSectionV17(tr(lang,"3 · Clasificación del OD $selected","3 · Tooth $selected classification")) { choices.forEach { s -> FilterChip(record.status==s,{setStatus(s)},{Text(cpodStatus19(s,lang))},modifier=Modifier.fillMaxWidth()) } }
        Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)) {
            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                Text(tr(lang,"4 · Resultado ","4 · Result ")+(if(primary)"ceod" else "CPOD"),fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                Text(if(primary)"c = ${result.carious}   e = ${result.missing}   o = ${result.filled}" else "C = ${result.carious}   P = ${result.missing}   O = ${result.filled}")
                Text("${if(primary)"ceod" else "CPOD"} = ${result.total}",fontWeight=FontWeight.Black,style=MaterialTheme.typography.headlineSmall)
                Text(ClinicalEngines.cpodInterpretation(result.total,lang))
            }
        }
        NoticeCard(tr(lang,"La unidad es el diente. Caries activa tiene prioridad sobre una restauración para el conteo. Ausencias por causas distintas de caries no suman como P/e.","The unit is the tooth. Active caries takes priority over a restoration for counting. Missing teeth for causes other than caries do not count as M/e."))
    }
}