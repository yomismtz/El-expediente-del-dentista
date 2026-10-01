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


private fun vitalBandForAge19(age:Int):VitalBand19 = when {
    age < 1 -> VitalBand19("0–11 meses",25,50,100,160)
    age <= 5 -> VitalBand19("1–5 años",20,30,80,140)
    age <= 12 -> VitalBand19("6–12 años",15,25,70,120)
    else -> VitalBand19("≥13 años",12,20,60,100)
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
private fun bpAction19(age:Int,sex:String,sys:Int?,dia:Int?,lang:String):String {
    if(sys==null||dia==null)return tr(lang,"Introduce ambas cifras de presión arterial.","Enter both blood-pressure values.")
    if(age>=18)return when{
        sys>180||dia>110->tr(lang,"🔴 ALTA/CRÍTICA: no realizar tratamiento dental electivo. Repetir tras reposo y solicitar valoración médica urgente; si hay dolor torácico, disnea, déficit neurológico o alteración visual, activar emergencias.","🔴 HIGH/CRITICAL: do not perform elective dental treatment. Repeat after rest and obtain urgent medical assessment; if chest pain, dyspnea, neurologic deficit or visual disturbance occurs, activate emergency response.")
        sys>=160||dia>=100->tr(lang,"🟠 ALTA: repetir correctamente. Si se confirma, diferir tratamiento electivo y solicitar valoración médica. La atención urgente sólo debe realizarse con control clínico y monitorización apropiada.","🟠 HIGH: repeat correctly. If confirmed, defer elective dental treatment and obtain medical assessment. Urgent care should only proceed with appropriate clinical monitoring.")
        sys<90||dia<60->tr(lang,"🔵 BAJA: repetir tras reposo y valorar síntomas, hidratación, medicamentos y posible hipotensión ortostática. Si está asintomático y se normaliza, puede continuarse según criterio clínico; si persiste o hay síncope, confusión, dolor torácico o mala perfusión, suspender y valorar urgentemente.","🔵 LOW: repeat after rest and assess symptoms, hydration, medications and possible orthostatic hypotension. If asymptomatic and it normalizes, care may continue based on clinical judgment; if persistent or accompanied by syncope, confusion, chest pain or poor perfusion, stop and obtain urgent assessment.")
        else->tr(lang,"🟢 NORMAL: <160/100 mmHg y sin datos de alarma. No requiere modificación de la atención dental por la TA aislada; continuar según el procedimiento y el estado clínico.","🟢 NORMAL: <160/100 mmHg with no red flags. No dental-care modification is required based on BP alone; proceed according to the procedure and clinical status.")
    }
    val low=if(age<=10)70+2*age else 90
    if(sys<low)return tr(lang,"🔵 BAJA: por debajo del umbral pediátrico de seguridad. Repetir y valorar perfusión/síntomas. No iniciar tratamiento electivo si persiste o hay síntomas; buscar valoración médica.","🔵 LOW: below the pediatric safety threshold. Repeat and assess perfusion/symptoms. Do not start elective treatment if persistent or symptomatic; obtain medical assessment.")
    val screen=pediatricBpScreen19(age,sex)
    if(screen==null)return tr(lang,"⚠️ En pediatría, la TA debe interpretarse por edad, sexo y talla. Selecciona sexo para aplicar el cribado AAP; no usar esta pantalla para diagnosticar hipertensión.","⚠️ Pediatric BP must be interpreted by age, sex and height. Select sex to apply AAP screening; do not use this screen to diagnose hypertension.")
    return when{
        sys>=140||dia>=90->tr(lang,"🔴 ALTA: repetir con técnica adecuada. Si persiste, diferir atención electiva y solicitar valoración médica; si es sintomática, atención urgente.","🔴 HIGH: repeat with proper technique. If persistent, defer elective care and obtain medical assessment; if symptomatic, urgent care.")
        sys>=screen.systolic||dia>=screen.diastolic->tr(lang,"🟠 ELEVADA: supera el umbral simplificado AAP. Repetir y confirmar con tablas pediátricas por edad, sexo y talla. No diagnostica hipertensión por sí sola; evitar decidir tratamiento únicamente con una medición.","🟠 ELEVATED: above the AAP simplified screening threshold. Repeat and confirm with pediatric tables by age, sex and height. It does not diagnose hypertension by itself; do not decide treatment solely from one reading.")
        else->tr(lang,"🟢 NORMAL PARA CRIBADO: por debajo del umbral simplificado AAP. La clasificación pediátrica definitiva requiere percentil por edad, sexo y talla.","🟢 NORMAL FOR SCREENING: below the AAP simplified threshold. Definitive pediatric classification requires age-, sex- and height-based percentile.")
    }
}
private fun rhythmAction19(rr:Int?,hr:Int?,band:VitalBand19,lang:String):String {
    if(rr==null&&hr==null)return tr(lang,"Introduce FR y/o FC.","Enter RR and/or HR.")
    val rrText=rr?.let{when{it<band.rrMin->"🔵 FR BAJA";it>band.rrMax->"🔴 FR ALTA";else->"🟢 FR NORMAL"}}?:""
    val hrText=hr?.let{when{it<band.hrMin->"🔵 FC BAJA";it>band.hrMax->"🔴 FC ALTA";else->"🟢 FC NORMAL"}}?:""
    val abnormal=rr?.let{it<band.rrMin||it>band.rrMax}==true||hr?.let{it<band.hrMin||it>band.hrMax}==true
    return if(!abnormal)tr(lang,"🟢 NORMAL: FR y FC dentro de la referencia etaria. Si el paciente está clínicamente estable, puede continuar la atención.","🟢 NORMAL: RR and HR are within the age reference. If clinically stable, care may continue.")
    else tr(lang,"${rrText} · ${hrText}. Repetir en reposo y valorar dolor, ansiedad, fiebre, medicamentos y síntomas. Si persiste o hay disnea, dolor torácico, síncope o alteración de conciencia, suspender y valorar urgentemente.","${rrText} · ${hrText}. Repeat at rest and assess pain, anxiety, fever, medications and symptoms. If persistent or accompanied by dyspnea, chest pain, syncope or altered consciousness, stop care and assess urgently.")
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
private fun glucoseAction19(value:Int?,lang:String):String{
    if(value==null)return tr(lang,"Introduce la glucosa capilar y el contexto.","Enter capillary glucose and context.")
    if(value<70)return tr(lang,"🔵 BAJA: hipoglucemia. Suspender procedimiento. Si está consciente y puede deglutir, administrar 15 g de carbohidrato de acción rápida y volver a medir en 15 min; repetir según protocolo. Si no puede deglutir o está inconsciente, activar emergencias.","🔵 LOW: hypoglycemia. Stop the procedure. If conscious and able to swallow, give 15 g of fast-acting carbohydrate and recheck in 15 min; repeat per protocol. If unable to swallow or unconscious, activate emergency response.")
    if(value>=300)return tr(lang,"🔴 MUY ALTA: ≥300 mg/dL. Posponer tratamiento electivo, especialmente cirugía, y valorar control metabólico. Con vómitos, dolor abdominal, respiración anormal, deshidratación, confusión o cetonas: valoración urgente.","🔴 VERY HIGH: ≥300 mg/dL. Postpone elective treatment, especially surgery, and assess metabolic control. With vomiting, abdominal pain, abnormal breathing, dehydration, confusion or ketones: urgent assessment.")
    if(value>=180)return tr(lang,"🟠 ALTA: contextualizar con comida, diabetes y síntomas. Para procedimientos electivos invasivos, considerar diferir si el control es deficiente y coordinar valoración médica.","🟠 HIGH: interpret with meals, diabetes history and symptoms. For invasive elective procedures, consider deferring if control is poor and coordinate medical assessment.")
    return tr(lang,"🟢 EN RANGO: no hay criterio de emergencia por la glucosa aislada. Interpretar según ayuno/comida, diabetes, medicamentos y síntomas; si el paciente está estable, continuar según el procedimiento.","🟢 IN RANGE: no emergency criterion from isolated glucose. Interpret according to fasting/meal status, diabetes, medications and symptoms; if clinically stable, proceed according to the procedure.")
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
private fun treatmentAction19(p:DentalProcedure19,age:Int,sex:String,sys:Int?,dia:Int?,glucose:Int?,spo2:Int?,temp:Double?,bmi:Double?,selected:Set<String>,lang:String):String{
    val emergency=setOf("Sangrado oral no controlable","Inflamación facial/cervical","Disnea","Dolor torácico","Alteración de conciencia","Convulsiones")
    if(selected.any{it in emergency}) return tr(lang,"🚨 NO TRATAR ELECTIVAMENTE: existe un signo de alarma mayor. Suspender, estabilizar según competencia y activar el protocolo de emergencia.","🚨 DO NOT PROVIDE ELECTIVE CARE: a major red flag is present. Stop, stabilize within scope and activate the emergency protocol.")
    if(spo2!=null&&spo2<90)return tr(lang,"🚨 NO TRATAR ELECTIVAMENTE: SpO₂ <90 % persistente puede indicar hipoxemia significativa. Repetir/confirmar y solicitar valoración urgente.","🚨 DO NOT PROVIDE ELECTIVE CARE: persistent SpO₂ <90% may indicate significant hypoxemia. Repeat/confirm and obtain urgent assessment.")
    if(temp!=null&&temp>=38.0&&("Fiebre/malestar" in selected||"Inflamación intraoral" in selected||"Supuración/fístula" in selected))
        return tr(lang,"⚠️ DIFERIR ELECTIVO: fiebre con signos compatibles con infección. Priorizar diagnóstico y control del foco; antibiótico sólo cuando esté indicado.","⚠️ DEFER ELECTIVE CARE: fever with signs compatible with infection. Prioritize diagnosis and source control; antibiotics only when indicated.")
    if(age<2&&p!=DentalProcedure19.DIAGNOSTIC&&p!=DentalProcedure19.LOCAL_ANESTHESIA)
        return tr(lang,"⚠️ PACIENTE <2 AÑOS: no usar esta pantalla como autorización automática. Coordinar manejo con odontopediatría y considerar entorno/recursos apropiados según el procedimiento.","⚠️ PATIENT <2 YEARS: do not use this screen as automatic clearance. Coordinate pediatric dental management and use an appropriate setting/resources for the procedure.")
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
    if(p==DentalProcedure19.SEDATION)return if(age<18) tr(lang,"🟠 SEDACIÓN: no debe autorizarse sólo con signos vitales/IMC. Requiere evaluación pre-sedación, ayuno cuando corresponda, personal capacitado, monitorización y equipo de rescate; en pediatría seguir AAP/AAPD y el nivel de sedación previsto.","🟠 SEDATION: do not clear based only on vitals/BMI. Requires pre-sedation evaluation, appropriate fasting when applicable, trained personnel, monitoring and rescue equipment; in pediatrics follow AAP/AAPD guidance and intended sedation level.")
    else tr(lang,"🟢 APTO PARA VALORACIÓN DEL PROCEDIMIENTO: no se detectó una contraindicación general en esta pantalla. La decisión final depende de historia médica, medicamentos, diagnóstico, complejidad, hemostasia, infección y protocolo clínico.","🟢 SUITABLE FOR PROCEDURE ASSESSMENT: no general contraindication was detected by this screen. Final decision depends on medical history, medications, diagnosis, complexity, hemostasis, infection and clinical protocol.")
}

@Composable
private fun ResultCard19(text:String) {
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
        Text(text,Modifier.padding(12.dp),fontWeight=FontWeight.SemiBold)
    }
}

@Composable
fun VitalsInteractiveV19Screen(lang:String,session:EducationalSession,onBack:()->Unit) {
    val patientAge=session.profile.age.toIntOrNull() ?: 18
    val patientSex=session.profile.sex
    var sex by rememberRecordState("vitals.sex",if(patientSex=="Masculino"||patientSex=="Femenino")patientSex else "No especificado")
    val b=vitalBandForAge19(patientAge)
    var spo2 by rememberRecordState("vitals.spo2",""); var rr by rememberRecordState("vitals.rr",""); var hr by rememberRecordState("vitals.hr","")
    var sys by rememberRecordState("vitals.sys",""); var dia by rememberRecordState("vitals.dia",""); var temp by rememberRecordState("vitals.temp",""); var glucose by rememberRecordState("vitals.glucose","")
    var glucoseContext by rememberRecordState("vitals.glucoseContext",GlucoseContext19.RANDOM); var selectedSignsRaw by rememberRecordState("vitals.dentalSigns","")
    var weight by rememberRecordState("vitals.weight",""); var height by rememberRecordState("vitals.height","")
    val bmi=run{val w=weight.toDoubleOrNull();val h=height.toDoubleOrNull()?.div(100.0);if(w!=null&&h!=null&&h>0)w/h.pow(2)else null}
    ResponsiveScreenV17(tr(lang,"Signos, síntomas y triage clínico","Clinical signs, symptoms and triage"),tr(lang,"Registra parámetros, signos y síntomas y obtén una orientación educativa sobre continuidad, diferimiento o referencia.","Record parameters, signs and symptoms and get educational guidance on proceeding, deferring or referring."),onBack){profile->
        ResponsiveSectionV17(tr(lang,"1 · Edad y sexo","1 · Age and sex")){
            Text(tr(lang,"Edad registrada: $patientAge años.","Recorded age: $patientAge years."))
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Femenino","Masculino","No especificado").forEach{s->FilterChip(sex==s,{sex=s},{Text(if(lang=="en"&&s=="Femenino")"Female" else if(lang=="en"&&s=="Masculino")"Male" else if(lang=="en")"Not specified" else s)},modifier=Modifier.weight(1f))}}
            Text(tr(lang,"TA pediátrica: edad + sexo + talla. El umbral AAP mostrado es sólo de cribado y no diagnostica hipertensión.","Pediatric BP: age + sex + height. The displayed AAP threshold is screening only and does not diagnose hypertension."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"2 · Referencias fisiológicas","2 · Physiologic references")){
            Text(tr(lang,"Grupo etario: ${b.label}. FR ${b.rrMin}–${b.rrMax}/min · FC ${b.hrMin}–${b.hrMax}/min.","Age group: ${b.label}. RR ${b.rrMin}–${b.rrMax}/min · HR ${b.hrMin}–${b.hrMax}/min."))
            pediatricBpScreen19(patientAge,sex)?.let{Text(it.label,style=MaterialTheme.typography.bodySmall)}
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
            ResultCard19(rhythmAction19(rr.toIntOrNull(),hr.toIntOrNull(),b,lang)); ResultCard19(bpAction19(patientAge,sex,sys.toIntOrNull(),dia.toIntOrNull(),lang))
        }
        ResponsiveSectionV17(tr(lang,"4 · Temperatura y oxigenación","4 · Temperature and oxygenation")){
            OutlinedTextField(temp,{temp=it.filter{ch->ch.isDigit()||ch=='.'}.take(5)},label={Text("°C")},modifier=Modifier.fillMaxWidth())
            ResultCard19(temperature19(temp.toDoubleOrNull(),lang)); ResultCard19(temperatureAction19(temp.toDoubleOrNull(),lang))
            OutlinedTextField(spo2,{spo2=it.filter(Char::isDigit).take(3)},label={Text("SpO₂ %")},modifier=Modifier.fillMaxWidth())
            val s=spo2.toIntOrNull()
            ResultCard19(when{s==null->tr(lang,"Escribe la SpO₂.","Enter SpO₂.");s>=95->tr(lang,"95–100 %: habitual en la mayoría de personas sanas.","95–100%: usual in most healthy people.");s>=92->tr(lang,"92–94 %: repetir y contextualizar.","92–94%: repeat and contextualize.");s>=90->tr(lang,"90–91 %: baja.","90–91%: low.");else->tr(lang,"<90 %: baja y potencialmente urgente.","<90%: low and potentially urgent.")}); ResultCard19(spo2Action19(s,lang))
        }
        ResponsiveSectionV17(tr(lang,"5 · Glucosa capilar","5 · Capillary glucose")){
            OutlinedTextField(glucose,{glucose=it.filter(Char::isDigit).take(4)},label={Text("mg/dL")},modifier=Modifier.fillMaxWidth())
            GlucoseContext19.entries.forEach{ctx->FilterChip(glucoseContext==ctx,{glucoseContext=ctx},{Text(glucoseContext19(ctx,lang))},modifier=Modifier.fillMaxWidth())}
            ResultCard19(glucose19(glucose.toIntOrNull(),glucoseContext,lang)); ResultCard19(glucoseAction19(glucose.toIntOrNull(),lang))
        }
        ResponsiveSectionV17(tr(lang,"6 · Signos y síntomas odontológicos","6 · Dental signs and symptoms")){
            val selectedSigns=selectedSignsRaw.split("|").filter{it.isNotBlank()}.toSet()
            AdaptiveGridV17(dentalSignsSymptoms19.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                val item=dentalSignsSymptoms19[i]
                FilterChip(selectedSigns.contains(item),{selectedSignsRaw=(if(selectedSigns.contains(item))selectedSigns-item else selectedSigns+item).joinToString("|")},{Text(item)},modifier=Modifier.fillMaxWidth())
            }
            ResultCard19(triageAction19(selectedSigns,lang))
        }
        ResponsiveSectionV17(tr(lang,"7 · Peso, talla e IMC","7 · Weight, height and BMI")){
            val cols=if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2
            AdaptiveGridV17(2,cols){i->if(i==0)OutlinedTextField(weight,{weight=it.filter{ch->ch.isDigit()||ch=='.'}.take(6)},label={Text("kg")},modifier=Modifier.fillMaxWidth())else OutlinedTextField(height,{height=it.filter{ch->ch.isDigit()||ch=='.'}.take(6)},label={Text("cm")},modifier=Modifier.fillMaxWidth())}
            Text(if(bmi==null)tr(lang,"IMC = peso / talla²","BMI = weight / height²") else "IMC = ${"%.1f".format(bmi)} kg/m²",fontWeight=FontWeight.Bold)
            ResultCard19(bmiAction19(patientAge,sex,bmi,lang))
        }

        ResponsiveSectionV17(tr(lang,"8 · ¿Qué tratamiento dental puede realizarse hoy?","8 · Which dental treatment can be performed today?"),tr(lang,"Selecciona el procedimiento planeado. La herramienta cruza edad, sexo, TA, glucosa, SpO₂, temperatura, signos de alarma, peso, talla e IMC para orientar continuidad, modificación o diferimiento.","Select the planned procedure. The tool cross-checks age, sex, BP, glucose, SpO₂, temperature, red flags, weight, height and BMI to guide continuation, modification or deferral")){
            var procedureRaw by rememberRecordState("vitals.procedure","DIAGNOSTIC")
            val procedure=runCatching{DentalProcedure19.valueOf(procedureRaw)}.getOrElse{DentalProcedure19.DIAGNOSTIC}
            DentalProcedure19.entries.forEach{item->
                FilterChip(procedure==item,{procedureRaw=item.name},{Text(procedureName19(item,lang))},modifier=Modifier.fillMaxWidth())
            }
            Text(tr(lang,"La salida es una guía de triage, no una autorización legal ni anestésica automática.","The output is a triage guide, not automatic legal or anesthesia clearance."),style=MaterialTheme.typography.bodySmall)
            ResultCard19(treatmentAction19(procedure,patientAge,sex,sys.toIntOrNull(),dia.toIntOrNull(),glucose.toIntOrNull(),spo2.toIntOrNull(),temp.toDoubleOrNull(),bmi,selectedSigns,lang))
        }
        NoticeCard(tr(lang,"Fuentes educativas: AAP para cribado de TA pediátrica; AHA/PALS para hipotensión pediátrica; ADA/ADA Standards 2026 para glucosa e hipertensión dental; FDA para SpO₂. La herramienta orienta el triage y no sustituye protocolos institucionales ni valoración médica.","Educational sources: AAP for pediatric BP screening; AHA/PALS for pediatric hypotension; ADA/ADA Standards 2026 for glucose and dental hypertension; FDA for SpO₂. This tool supports triage and does not replace institutional protocols or medical assessment."))
    }
}
@Composable
fun VitalsInteractiveV19Screen(lang:String,onBack:()->Unit){VitalsInteractiveV19Screen(lang,EducationalSession(),onBack)}

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
