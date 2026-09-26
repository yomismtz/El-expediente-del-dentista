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
    val label:String,val rrMin:Int,val rrMax:Int,val hrMin:Int,val hrMax:Int,
    val sysMin:Int,val sysMax:Int,val diaMin:Int,val diaMax:Int
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

@Composable
private fun ResultCard19(text:String) {
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
        Text(text,Modifier.padding(12.dp),fontWeight=FontWeight.SemiBold)
    }
}

@Composable
fun VitalsInteractiveV19Screen(lang:String,onBack:()->Unit) {
    val bands=listOf(
        VitalBand19("0–6 m",30,50,82,205,60,90,30,62),
        VitalBand19("6 m–2 a",20,40,100,190,60,90,30,62),
        VitalBand19("2–7 a",15,30,60,140,78,112,48,78),
        VitalBand19("8–11 a",15,25,60,140,85,114,52,85),
        VitalBand19("≥12 a",13,20,60,100,95,135,58,88),
        VitalBand19(tr(lang,"Adulto","Adult"),12,20,60,100,100,140,60,90)
    )
    var bandIndex by remember{mutableStateOf(5)}
    var age by remember{mutableStateOf("")}
    var sex by remember{mutableStateOf("Femenino")}
    var spo2 by remember{mutableStateOf("")}
    var rr by remember{mutableStateOf("")}; var hr by remember{mutableStateOf("")}
    var sys by remember{mutableStateOf("")}; var dia by remember{mutableStateOf("")}
    var temp by remember{mutableStateOf("")}; var glucose by remember{mutableStateOf("")}
    var glucoseContext by remember{mutableStateOf(GlucoseContext19.RANDOM)}
    var weight by remember{mutableStateOf("")}; var height by remember{mutableStateOf("")}
    val b=bands[bandIndex]
    val bmi=run { val w=weight.toDoubleOrNull(); val h=height.toDoubleOrNull()?.div(100.0); if(w!=null&&h!=null&&h>0)w/h.pow(2) else null }

    ResponsiveScreenV17(tr(lang,"Signos vitales y glucosa","Vital signs and glucose"),tr(lang,"Registra valores y compáralos con referencias educativas; confirma cualquier valor anormal.","Record values and compare with teaching references; confirm any abnormal value."),onBack) { profile ->
        ResponsiveSectionV17(tr(lang,"1 · Edad y sexo","1 · Age and sex")) {
            OutlinedTextField(age,{age=it.filter(Char::isDigit).take(3)},label={Text(tr(lang,"Edad en años","Age in years"))},modifier=Modifier.fillMaxWidth())
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                listOf("Femenino","Masculino").forEach { s -> FilterChip(sex==s,{sex=s},{Text(if(lang=="en" && s=="Femenino") "Female" else if(lang=="en") "Male" else s)}) }
            }
            Text(tr(lang,"Selecciona también el grupo etario de referencia. En pediatría, TA e IMC requieren edad, sexo y, según el parámetro, talla/percentiles.","Also select the reference age group. In pediatrics, BP and BMI require age, sex and, depending on the parameter, height/percentiles."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"2 · Grupo de edad de referencia","2 · Reference age group")) {
            AdaptiveGridV17(bands.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 3) { i ->
                FilterChip(bandIndex==i,{bandIndex=i},{Text(bands[i].label)},modifier=Modifier.fillMaxWidth())
            }
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
            Text("TA sistólica ${b.sysMin}–${b.sysMax}: ${vitalRange19(sys.toDoubleOrNull(),b.sysMin.toDouble(),b.sysMax.toDouble(),lang)}")
            Text("TA diastólica ${b.diaMin}–${b.diaMax}: ${vitalRange19(dia.toDoubleOrNull(),b.diaMin.toDouble(),b.diaMax.toDouble(),lang)}")
        }
        ResponsiveSectionV17(tr(lang,"4 · Temperatura y saturación de oxígeno","4 · Temperature and oxygen saturation")) {
            OutlinedTextField(temp,{temp=it.filter{c->c.isDigit()||c=='.'}.take(5)},label={Text("°C")},modifier=Modifier.fillMaxWidth())
            ResultCard19(temperature19(temp.toDoubleOrNull(),lang))
            Text(tr(lang,"Referencia resumida: alrededor de 37 °C es habitual; ≥38 °C suele considerarse fiebre; ≤35 °C es muy baja. Sitio y método modifican la lectura.","Summary reference: around 37 °C is common; ≥38 °C is usually fever; ≤35 °C is very low. Site and method affect the reading."),style=MaterialTheme.typography.bodySmall)
            OutlinedTextField(spo2,{spo2=it.filter(Char::isDigit).take(3)},label={Text("SpO₂ %")},modifier=Modifier.fillMaxWidth())
            val s=spo2.toIntOrNull()
            ResultCard19(when { s==null -> tr(lang,"Escribe la SpO₂ para interpretarla.","Enter SpO₂ for interpretation."); s>100 -> tr(lang,"Valor no válido: SpO₂ no puede superar 100 %.","Invalid value: SpO₂ cannot exceed 100%."); s>=95 -> tr(lang,"95–100 %: intervalo habitual en la mayoría de personas sanas.","95–100%: usual range for most healthy individuals."); else -> tr(lang,"SpO₂ menor de 95 %: repite y confirma la medición y valora síntomas, antecedentes, altitud y contexto clínico.","SpO₂ below 95%: repeat and confirm the measurement and assess symptoms, history, altitude and clinical context.") })
            Text(tr(lang,"La pulsioximetría es una estimación. Perfusión deficiente, temperatura de la piel, esmalte de uñas, pigmentación cutánea y otros factores pueden afectar la precisión.","Pulse oximetry is an estimate. Poor circulation, skin temperature, nail polish, skin pigmentation and other factors can affect accuracy."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"5 · Glucosa capilar","5 · Capillary glucose")) {
            OutlinedTextField(glucose,{glucose=it.filter(Char::isDigit).take(4)},label={Text("mg/dL")},modifier=Modifier.fillMaxWidth())
            GlucoseContext19.entries.forEach { ctx -> FilterChip(glucoseContext==ctx,{glucoseContext=ctx},{Text(glucoseContext19(ctx,lang))},modifier=Modifier.fillMaxWidth()) }
            ResultCard19(glucose19(glucose.toIntOrNull(),glucoseContext,lang))
            Text(tr(lang,"Los umbrales diagnósticos corresponden a pruebas estandarizadas de plasma/laboratorio y requieren confirmación. La lectura capilar sirve aquí como alerta educativa.","Diagnostic thresholds refer to standardized plasma/laboratory tests and require confirmation. Capillary readings here are an educational alert."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"6 · Peso, talla e IMC","6 · Weight, height and BMI")) {
            val cols=if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2
            AdaptiveGridV17(2,cols) { i -> if(i==0)
                OutlinedTextField(weight,{weight=it.filter{c->c.isDigit()||c=='.'}.take(6)},label={Text("kg")},modifier=Modifier.fillMaxWidth())
            else OutlinedTextField(height,{height=it.filter{c->c.isDigit()||c=='.'}.take(6)},label={Text("cm")},modifier=Modifier.fillMaxWidth()) }
            Text(if(bmi==null)tr(lang,"IMC = peso / talla²","BMI = weight / height²") else "IMC = ${"%.1f".format(bmi)} kg/m²",fontWeight=FontWeight.Bold)
            val a=age.toIntOrNull()
            Text(when { bmi==null -> tr(lang,"Introduce peso y talla para calcular el IMC.","Enter weight and height to calculate BMI."); a!=null && a in 2..19 -> tr(lang,"IMC pediátrico: debe clasificarse por percentil de IMC para edad y sexo (CDC); el valor de IMC aislado no debe clasificarse con límites de adulto.","Pediatric BMI: classify using BMI-for-age and sex percentile (CDC); the BMI value alone should not use adult cutoffs."); a!=null && a>=20 -> when { bmi<18.5 -> tr(lang,"IMC adulto: bajo peso (<18.5).","Adult BMI: underweight (<18.5)."); bmi<25 -> tr(lang,"IMC adulto: peso saludable (18.5–24.9).","Adult BMI: healthy weight (18.5–24.9)."); bmi<30 -> tr(lang,"IMC adulto: sobrepeso (25.0–29.9).","Adult BMI: overweight (25.0–29.9)."); else -> tr(lang,"IMC adulto: rango de obesidad (≥30).","Adult BMI: obesity range (≥30).") }; else -> tr(lang,"Indica la edad para interpretar el IMC correctamente.","Enter age to interpret BMI correctly.") })
        }
        NoticeCard(tr(lang,"Fuentes educativas resumidas: NHS para temperatura; MedlinePlus, CDC y ADA para glucosa. El protocolo institucional y la valoración clínica prevalecen.","Teaching sources summarized: NHS for temperature; MedlinePlus, CDC and ADA for glucose. Institutional protocol and clinical assessment prevail."))
    }
}

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
        ResponsiveSectionV17(tr(lang,"Dentición","Dentition")) {
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2) { i ->
                val p=i==1
                FilterChip(primary==p,{primary=p},{Text(if(p)tr(lang,"Temporal · ceod","Primary · dmft") else tr(lang,"Permanente · CPOD","Permanent · DMFT"))},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"Maxilar arriba · mandibular abajo","Maxillary above · mandibular below")) {
            DentalArchSelector(shown,selected,{selected=it}) { tooth -> session.teeth[tooth]?.status?.let{it!=ToothStatus.HEALTHY}==true }
        }
        ResponsiveSectionV17("OD $selected") { choices.forEach { s -> FilterChip(record.status==s,{setStatus(s)},{Text(cpodStatus19(s,lang))},modifier=Modifier.fillMaxWidth()) } }
        Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)) {
            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                Text(if(primary)"ceod" else "CPOD",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                Text(if(primary)"c = ${result.carious}   e = ${result.missing}   o = ${result.filled}" else "C = ${result.carious}   P = ${result.missing}   O = ${result.filled}")
                Text("${if(primary)"ceod" else "CPOD"} = ${result.total}",fontWeight=FontWeight.Black,style=MaterialTheme.typography.headlineSmall)
                Text(ClinicalEngines.cpodInterpretation(result.total,lang))
            }
        }
        NoticeCard(tr(lang,"La unidad es el diente. Caries activa tiene prioridad sobre una restauración para el conteo. Ausencias por causas distintas de caries no suman como P/e.","The unit is the tooth. Active caries takes priority over a restoration for counting. Missing teeth for causes other than caries do not count as M/e."))
    }
}
