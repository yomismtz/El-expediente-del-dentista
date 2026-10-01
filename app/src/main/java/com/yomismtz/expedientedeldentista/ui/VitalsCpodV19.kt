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

private data class PediatricBpScreen19(val systolic:Int,val diastolic:Int,val label:String)

private fun vitalBandForAge19(age:Int):VitalBand19 = when {
    age < 1 -> VitalBand19("0–11 meses",25,50,100,160)
    age <= 5 -> VitalBand19("1–5 años",20,30,80,140)
    age <= 12 -> VitalBand19("6–12 años",15,25,70,120)
    else -> VitalBand19("≥13 años",12,20,60,100)
}

private fun pediatricBpScreen19(age:Int,sex:String):PediatricBpScreen19? {
    if(age < 1) return null
    if(age >= 13) return PediatricBpScreen19(120,80,"≥13 años: cribado 120/80 mmHg")
    val boys=listOf(98 to 52,100 to 55,101 to 58,102 to 60,103 to 63,105 to 66,106 to 68,107 to 69,107 to 70,108 to 72,110 to 74,113 to 75)
    val girls=listOf(98 to 54,101 to 58,102 to 60,103 to 62,104 to 64,105 to 67,106 to 68,107 to 69,108 to 71,109 to 72,111 to 74,114 to 75)
    val pair=when(sex) {
        "Masculino" -> boys[age-1]
        "Femenino" -> girls[age-1]
        else -> return null
    }
    return PediatricBpScreen19(pair.first,pair.second,"Umbral AAP de cribado: ${if(sex=="Masculino")"niño" else "niña"} de $age años ${pair.first}/${pair.second} mmHg")
}

private fun bpAction19(age:Int,sex:String,sys:Int?,dia:Int?,lang:String):String {
    if(sys==null || dia==null) return tr(lang,"Introduce ambas cifras de presión arterial.","Enter both blood-pressure values.")
    if(age>=18) return when {
        sys>180 || dia>110 -> tr(lang,"🚨 >180/110 mmHg: no realizar tratamiento dental electivo. Repetir tras reposo; si persiste, solicitar valoración médica urgente. Con dolor torácico, disnea o alteraciones neurológicas/visuales: activar emergencias.","🚨 >180/110 mmHg: no elective dental treatment. Repeat after rest; if persistent, obtain urgent medical assessment. With chest pain, dyspnea or neurologic/visual symptoms: activate emergency response.")
        sys>=160 || dia>=100 -> tr(lang,"⚠️ ≥160/100 mmHg: repetir correctamente. Si se confirma, diferir tratamiento electivo y solicitar valoración médica. La atención odontológica urgente requiere monitorización y juicio clínico.","⚠️ ≥160/100 mmHg: repeat correctly. If confirmed, defer elective dental treatment and obtain medical assessment. Urgent dental care requires monitoring and clinical judgment.")
        sys<90 || dia<60 -> tr(lang,"⚠️ TA baja: repetir tras reposo y valorar síntomas. Si hay síncope, confusión, dolor torácico, disnea o mala perfusión, suspender tratamiento y activar valoración urgente.","⚠️ Low BP: repeat after rest and assess symptoms. If syncope, confusion, chest pain, dyspnea or poor perfusion occurs, stop treatment and obtain urgent assessment.")
        else -> tr(lang,"✓ Compatible con atención dental habitual si el paciente está clínicamente estable.","✓ Compatible with routine dental care if the patient is clinically stable.")
    }
    val low=if(age<=10)70+2*age else 90
    if(sys<low) return tr(lang,"⚠️ Hipotensión pediátrica por umbral de seguridad: repetir y valorar perfusión/síntomas. No iniciar tratamiento electivo si persiste o hay síntomas; buscar valoración urgente.","⚠️ Pediatric hypotension by safety threshold: repeat and assess perfusion/symptoms. Do not start elective treatment if persistent or symptomatic; seek urgent assessment.")
    val screen=pediatricBpScreen19(age,sex)
    if(screen==null) return tr(lang,"⚠️ En pediatría, la TA debe interpretarse por edad, sexo y talla. Selecciona sexo para aplicar el cribado AAP; no usar esta pantalla para diagnosticar hipertensión.","⚠️ Pediatric BP must be interpreted by age, sex and height. Select sex to apply AAP screening; do not use this screen to diagnose hypertension.")
    return when {
        sys>=140 || dia>=90 -> tr(lang,"⚠️ TA claramente elevada: repetir con técnica adecuada. Si persiste, diferir atención electiva y solicitar valoración médica; si es sintomática, atención urgente.","⚠️ Clearly elevated BP: repeat with proper technique. If persistent, defer elective care and obtain medical assessment; if symptomatic, urgent care.")
        sys>=screen.systolic || dia>=screen.diastolic -> tr(lang,"⚠️ Supera el umbral simplificado de cribado AAP. Repetir y confirmar con tablas pediátricas por edad, sexo y talla; no diagnostica hipertensión por sí solo.","⚠️ Above the AAP simplified screening threshold. Repeat and confirm with pediatric tables by age, sex and height; this does not diagnose hypertension by itself.")
        else -> tr(lang,"✓ Por debajo del umbral simplificado de cribado AAP. Para clasificar como normal se requiere percentil por edad, sexo y talla.","✓ Below the AAP simplified screening threshold. Normal classification requires age-, sex- and height-based percentile.")
    }
}

private fun rhythmAction19(rr:Int?,hr:Int?,band:VitalBand19,lang:String):String {
    if(rr==null && hr==null) return tr(lang,"Introduce FR y/o FC.","Enter RR and/or HR.")
    val rrBad=rr?.let{it<band.rrMin||it>band.rrMax}==true
    val hrBad=hr?.let{it<band.hrMin||it>band.hrMax}==true
    return if(rrBad||hrBad) tr(lang,"⚠️ Fuera de la referencia etaria: repetir en reposo y valorar dolor, ansiedad, fiebre, medicamentos y síntomas. Si persiste o hay disnea, dolor torácico, síncope o alteración de conciencia, suspender atención y valorar urgentemente.","⚠️ Outside the age reference: repeat at rest and assess pain, anxiety, fever, medications and symptoms. If persistent or accompanied by dyspnea, chest pain, syncope or altered consciousness, stop care and assess urgently.")
    else tr(lang,"✓ Dentro de la referencia etaria seleccionada.","✓ Within the selected age reference.")
}

private fun temperatureAction19(value:Double?,lang:String):String = when {
    value==null -> tr(lang,"Introduce la temperatura.","Enter temperature.")
    value>=38.0 -> tr(lang,"⚠️ Fiebre: diferir atención electiva y buscar la causa. Si se acompaña de infección odontógena con fiebre/malestar, priorizar control del foco y valorar antibiótico sólo cuando esté indicado.","⚠️ Fever: defer elective care and identify the cause. If accompanied by odontogenic infection with fever/malaise, prioritize source control and consider antibiotics only when indicated.")
    value<=35.0 -> tr(lang,"🚨 Temperatura ≤35 °C: confirmar medición y suspender tratamiento hasta valoración clínica; si persiste o hay alteración de conciencia, activar atención urgente.","🚨 Temperature ≤35 °C: confirm measurement and stop treatment pending clinical assessment; if persistent or altered consciousness occurs, activate urgent care.")
    value>37.2 -> tr(lang,"⚠️ Temperatura elevada: repetir y correlacionar con síntomas; evitar tratamiento electivo si hay sospecha de infección sistémica.","⚠️ Elevated temperature: repeat and correlate with symptoms; avoid elective care if systemic infection is suspected.")
    else -> tr(lang,"✓ Compatible con temperatura habitual.","✓ Compatible with usual temperature.")
}

private fun spo2Action19(value:Int?,lang:String):String = when {
    value==null -> tr(lang,"Introduce SpO₂.","Enter SpO₂.")
    value>100 || value<0 -> tr(lang,"Valor no válido.","Invalid value.")
    value>=95 -> tr(lang,"✓ Habitualmente compatible con atención dental si el paciente está clínicamente estable.","✓ Usually compatible with dental care if clinically stable.")
    value>=92 -> tr(lang,"⚠️ Repetir con técnica correcta y valorar síntomas/antecedentes. Si persiste, considerar valoración médica antes de atención electiva.","⚠️ Repeat correctly and assess symptoms/history. If persistent, consider medical assessment before elective care.")
    value>=90 -> tr(lang,"🚨 Saturación baja: repetir inmediatamente y valorar clínicamente. No continuar tratamiento electivo si persiste; buscar valoración médica urgente.","🚨 Low saturation: repeat immediately and assess clinically. Do not continue elective treatment if persistent; seek urgent medical assessment.")
    else -> tr(lang,"🚨 <90 %: posible hipoxemia significativa. Suspender tratamiento y activar valoración urgente/emergente según síntomas. Algunas enfermedades crónicas tienen objetivos individualizados.","🚨 <90%: possible significant hypoxemia. Stop treatment and activate urgent/emergency assessment according to symptoms. Some chronic diseases have individualized targets.")
}

private fun glucoseAction19(value:Int?,ctx:GlucoseContext19,lang:String):String {
    if(value==null) return tr(lang,"Introduce la glucosa capilar y el contexto.","Enter capillary glucose and context.")
    if(value<70) return tr(lang,"🚨 Hipoglucemia: suspender procedimiento. Si está consciente y puede deglutir, administrar 15 g de carbohidrato de acción rápida y volver a medir en 15 min; repetir según protocolo. Si no puede deglutir o está inconsciente, activar emergencias y seguir el protocolo de hipoglucemia del consultorio.","🚨 Hypoglycemia: stop the procedure. If conscious and able to swallow, give 15 g of fast-acting carbohydrate and recheck in 15 min; repeat per protocol. If unable to swallow or unconscious, activate emergency response and follow the office hypoglycemia protocol.")
    if(value>=300) return tr(lang,"🚨 ≥300 mg/dL: posponer tratamiento electivo, especialmente cirugía, y valorar control metabólico. Si hay vómitos, dolor abdominal, respiración anormal, deshidratación, confusión o cetonas, requiere valoración urgente por posible cetoacidosis.","🚨 ≥300 mg/dL: postpone elective treatment, especially surgery, and assess metabolic control. Vomiting, abdominal pain, abnormal breathing, dehydration, confusion or ketones require urgent assessment for possible ketoacidosis.")
    if(value>=180) return tr(lang,"⚠️ Glucosa elevada: contextualizar con comida, diabetes y síntomas. Para procedimientos electivos invasivos, considerar diferir si el control es deficiente y coordinar valoración médica.","⚠️ Elevated glucose: interpret with meals, diabetes history and symptoms. For invasive elective procedures, consider deferring if control is poor and coordinate medical assessment.")
    tr(lang,"✓ Sin criterio de emergencia por glucosa aislada; interpretar con contexto y antecedentes.","✓ No emergency criterion from isolated glucose; interpret with context and history.")
}

private val dentalSignsSymptoms19=listOf(
    "Dolor dental/orofacial","Sensibilidad al frío/calor","Dolor espontáneo/nocturno","Inflamación intraoral","Inflamación facial/cervical",
    "Sangrado gingival","Sangrado oral no controlable","Halitosis","Xerostomía","Sialorrea","Trismus","Disfagia","Odinofagia",
    "Parestesia/numbness","Úlcera o lesión >2 semanas","Mancha blanca/roja persistente","Movilidad dental","Supuración/fístula",
    "Fiebre/malestar","Cefalea/dolor facial","Disnea","Dolor torácico","Mareo/síncope","Alteración de conciencia","Convulsiones"
)

private fun triageAction19(selected:Set<String>,lang:String):String {
    val emergency=setOf("Sangrado oral no controlable","Inflamación facial/cervical","Disnea","Dolor torácico","Alteración de conciencia","Convulsiones")
    return when {
        selected.any{it in emergency} -> tr(lang,"🚨 SIGNO DE ALARMA: detener el procedimiento, valorar ABC y activar el protocolo de emergencia/servicios de emergencia según el cuadro. La inflamación con compromiso de vía aérea y el sangrado no controlable no deben enviarse simplemente a casa.","🚨 RED FLAG: stop the procedure, assess ABC and activate the emergency protocol/emergency services according to the presentation. Airway-threatening swelling and uncontrolled bleeding should not simply be sent home.")
        "Fiebre/malestar" in selected && ("Inflamación intraoral" in selected || "Supuración/fístula" in selected) -> tr(lang,"⚠️ Posible infección odontógena con compromiso sistémico: atención dental urgente para control del foco y valoración de antibiótico cuando esté indicado; si existe compromiso de vía aérea o deterioro general, derivación urgente.","⚠️ Possible odontogenic infection with systemic involvement: urgent dental care for source control and antibiotic assessment when indicated; if airway compromise or systemic deterioration occurs, urgent referral.")
        "Úlcera o lesión >2 semanas" in selected || "Mancha blanca/roja persistente" in selected -> tr(lang,"⚠️ Lesión persistente/sospechosa: documentar, examinar y realizar biopsia o referencia según hallazgos. No etiquetar como cáncer sólo por el aspecto.","⚠️ Persistent/suspicious lesion: document, examine and biopsy or refer according to findings. Do not label it cancer based on appearance alone.")
        else -> tr(lang,"✓ No se seleccionó un signo de alarma mayor. Continuar exploración, diagnóstico diferencial y tratamiento según el hallazgo odontológico.","✓ No major red flag selected. Continue examination, differential diagnosis and treatment according to the dental finding.")
    }
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
    var spo2 by rememberRecordState("vitals.spo2","")
    var rr by rememberRecordState("vitals.rr",""); var hr by rememberRecordState("vitals.hr","")
    var sys by rememberRecordState("vitals.sys",""); var dia by rememberRecordState("vitals.dia","")
    var temp by rememberRecordState("vitals.temp",""); var glucose by rememberRecordState("vitals.glucose","")
    var glucoseContext by rememberRecordState("vitals.glucoseContext",GlucoseContext19.RANDOM)
    var selectedSignsRaw by rememberRecordState("vitals.dentalSigns","")
    var weight by rememberRecordState("vitals.weight",""); var height by rememberRecordState("vitals.height","")
    val bmi=run { val w=weight.toDoubleOrNull(); val h=height.toDoubleOrNull()?.div(100.0); if(w!=null&&h!=null&&h>0)w/h.pow(2) else null }

    ResponsiveScreenV17(tr(lang,"Signos, síntomas y triage clínico","Clinical signs, symptoms and triage"),tr(lang,"Registra parámetros, signos y síntomas y obtén una orientación educativa sobre continuidad, diferimiento o referencia.","Record parameters, signs and symptoms and get educational guidance on proceeding, deferring or referring."),onBack) { profile ->
        ResponsiveSectionV17(tr(lang,"1 · Edad y sexo","1 · Age and sex")) {
            Text(tr(lang,"Edad registrada: $patientAge años.","Recorded age: $patientAge years."))
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                listOf("Femenino","Masculino","No especificado").forEach { s ->
                    FilterChip(sex==s,{sex=s},{Text(if(lang=="en" && s=="Femenino")"Female" else if(lang=="en" && s=="Masculino")"Male" else if(lang=="en")"Not specified" else s)},modifier=Modifier.weight(1f))
                }
            }
            Text(tr(lang,"TA pediátrica: edad + sexo + talla. El umbral AAP mostrado es sólo de cribado y no diagnostica hipertensión.","Pediatric BP: age + sex + height. The displayed AAP threshold is screening only and does not diagnose hypertension."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"2 · Referencias fisiológicas","2 · Physiologic references")) {
            Text(tr(lang,"Grupo etario: ${b.label}. FR ${b.rrMin}–${b.rrMax}/min · FC ${b.hrMin}–${b.hrMax}/min.","Age group: ${b.label}. RR ${b.rrMin}–${b.rrMax}/min · HR ${b.hrMin}–${b.hrMax}/min."))
            pediatricBpScreen19(patientAge,sex)?.let{Text(it.label,style=MaterialTheme.typography.bodySmall)}
            Text(tr(lang,"SpO₂ habitual en personas sanas: 95–100 %. Temperatura habitual aproximada: 36.1–37.2 °C.","Usual SpO₂ in healthy people: 95–100%. Approximate usual temperature: 36.1–37.2 °C."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"3 · FR, FC y presión arterial","3 · RR, HR and blood pressure")) {
            val cols=if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2
            AdaptiveGridV17(4,cols) { i -> when(i) {
                0 -> OutlinedTextField(rr,{rr=it.filter(Char::isDigit).take(3)},label={Text("FR /min")},modifier=Modifier.fillMaxWidth())
                1 -> OutlinedTextField(hr,{hr=it.filter(Char::isDigit).take(3)},label={Text("FC /min")},modifier=Modifier.fillMaxWidth())
                2 -> OutlinedTextField(sys,{sys=it.filter(Char::isDigit).take(3)},label={Text(tr(lang,"TA sistólica","Systolic BP"))},modifier=Modifier.fillMaxWidth())
                else -> OutlinedTextField(dia,{dia=it.filter(Char::isDigit).take(3)},label={Text(tr(lang,"TA diastólica","Diastolic BP"))},modifier=Modifier.fillMaxWidth())
            } }
            Text("FR ${b.rrMin}–${b.rrMax}: ${vitalRange19(rr.toDoubleOrNull(),b.rrMin.toDouble(),b.rrMax.toDouble(),lang)}")
            Text("FC ${b.hrMin}–${b.hrMax}: ${vitalRange19(hr.toDoubleOrNull(),b.hrMin.toDouble(),b.hrMax.toDouble(),lang)}")
            ResultCard19(rhythmAction19(rr.toIntOrNull(),hr.toIntOrNull(),b,lang))
            ResultCard19(bpAction19(patientAge,sex,sys.toIntOrNull(),dia.toIntOrNull(),lang))
        }
        ResponsiveSectionV17(tr(lang,"4 · Temperatura y oxigenación","4 · Temperature and oxygenation")) {
            OutlinedTextField(temp,{temp=it.filter{ch->ch.isDigit()||ch=='.'}.take(5)},label={Text("°C")},modifier=Modifier.fillMaxWidth())
            ResultCard19(temperature19(temp.toDoubleOrNull(),lang))
            ResultCard19(temperatureAction19(temp.toDoubleOrNull(),lang))
            OutlinedTextField(spo2,{spo2=it.filter(Char::isDigit).take(3)},label={Text("SpO₂ %")},modifier=Modifier.fillMaxWidth())
            val s=spo2.toIntOrNull()
            ResultCard19(when { s==null -> tr(lang,"Escribe la SpO₂.","Enter SpO₂."); s>=95 -> tr(lang,"95–100 %: habitual en la mayoría de personas sanas.","95–100%: usual in most healthy people."); s>=92 -> tr(lang,"92–94 %: repetir y contextualizar.","92–94%: repeat and contextualize."); s>=90 -> tr(lang,"90–91 %: baja.","90–91%: low."); else -> tr(lang,"<90 %: baja y potencialmente urgente.","<90%: low and potentially urgent.") })
            ResultCard19(spo2Action19(s,lang))
            Text(tr(lang,"La SpO₂ es una estimación y puede verse afectada por perfusión, temperatura, esmalte y otras limitaciones del dispositivo.","SpO₂ is an estimate and can be affected by perfusion, temperature, nail polish and other device limitations."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"5 · Glucosa capilar","5 · Capillary glucose")) {
            OutlinedTextField(glucose,{glucose=it.filter(Char::isDigit).take(4)},label={Text("mg/dL")},modifier=Modifier.fillMaxWidth())
            GlucoseContext19.entries.forEach { ctx -> FilterChip(glucoseContext==ctx,{glucoseContext=ctx},{Text(glucoseContext19(ctx,lang))},modifier=Modifier.fillMaxWidth()) }
            ResultCard19(glucose19(glucose.toIntOrNull(),glucoseContext,lang))
            ResultCard19(glucoseAction19(glucose.toIntOrNull(),glucoseContext,lang))
            Text(tr(lang,"<70 mg/dL es hipoglucemia clínicamente importante. Los umbrales diagnósticos de diabetes requieren pruebas estandarizadas y confirmación.","<70 mg/dL is clinically important hypoglycemia. Diabetes diagnostic thresholds require standardized tests and confirmation."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"6 · Signos y síntomas odontológicos","6 · Dental signs and symptoms")) {
            val selectedSigns=selectedSignsRaw.split("|").filter{it.isNotBlank()}.toSet()
            AdaptiveGridV17(dentalSignsSymptoms19.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2) {
                val item=dentalSignsSymptoms19[it]
                FilterChip(selectedSigns.contains(item),{
                    val next=if(selectedSigns.contains(item))selectedSigns-item else selectedSigns+item
                    selectedSignsRaw=next.joinToString("|")
                },{Text(item)},modifier=Modifier.fillMaxWidth())
            }
            ResultCard19(triageAction19(selectedSigns,lang))
        }
        ResponsiveSectionV17(tr(lang,"7 · Peso, talla e IMC","7 · Weight, height and BMI")) {
            val cols=if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2
            AdaptiveGridV17(2,cols) { i -> if(i==0)
                OutlinedTextField(weight,{weight=it.filter{ch->ch.isDigit()||ch=='.'}.take(6)},label={Text("kg")},modifier=Modifier.fillMaxWidth())
            else OutlinedTextField(height,{height=it.filter{ch->ch.isDigit()||ch=='.'}.take(6)},label={Text("cm")},modifier=Modifier.fillMaxWidth()) }
            Text(if(bmi==null)tr(lang,"IMC = peso / talla²","BMI = weight / height²") else "IMC = ${"%.1f".format(bmi)} kg/m²",fontWeight=FontWeight.Bold)
            Text(when { bmi==null -> tr(lang,"Introduce peso y talla para calcular el IMC.","Enter weight and height to calculate BMI."); patientAge<18 -> tr(lang,"IMC pediátrico/adolescente: debe clasificarse con edad exacta y sexo mediante percentil de IMC para la edad.","Pediatric/adolescent BMI: classify with exact age and sex using BMI-for-age percentile."); else -> when { bmi<18.5 -> tr(lang,"IMC adulto: bajo peso (<18.5).","Adult BMI: underweight (<18.5)."); bmi<25 -> tr(lang,"IMC adulto: peso saludable (18.5–24.9).","Adult BMI: healthy weight (18.5–24.9)."); bmi<30 -> tr(lang,"IMC adulto: sobrepeso (25.0–29.9).","Adult BMI: overweight (25.0–29.9)."); else -> tr(lang,"IMC adulto: rango de obesidad (≥30).","Adult BMI: obesity range (≥30).") } })
        }
        NoticeCard(tr(lang,"Fuentes educativas: AAP para cribado de TA pediátrica; AHA/PALS para hipotensión pediátrica; ADA/ADA Standards 2026 para glucosa e hipertensión dental; FDA para SpO₂. La herramienta orienta el triage y no sustituye protocolos institucionales ni valoración médica.","Educational sources: AAP for pediatric BP screening; AHA/PALS for pediatric hypotension; ADA/ADA Standards 2026 for glucose and dental hypertension; FDA for SpO₂. This tool supports triage and does not replace institutional protocols or medical assessment."))
    }
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
