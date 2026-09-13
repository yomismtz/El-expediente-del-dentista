package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus
import kotlin.math.pow

// ---------- SIGNOS VITALES + TEMPERATURA/GLUCOSA ----------

private data class VitalBandV19(
    val label: String,
    val rrMin: Int,
    val rrMax: Int,
    val hrMin: Int,
    val hrMax: Int,
    val sysMin: Int,
    val sysMax: Int,
    val diaMin: Int,
    val diaMax: Int
)

private fun rangeTextV19(value: Double?, min: Double, max: Double, lang: String): String = when {
    value == null -> tr(lang, "Sin dato", "No value")
    value < min -> tr(lang, "↓ por debajo de la referencia didáctica", "↓ below teaching reference")
    value > max -> tr(lang, "↑ por encima de la referencia didáctica", "↑ above teaching reference")
    else -> tr(lang, "✓ dentro de la referencia didáctica", "✓ within teaching reference")
}

private fun temperatureTextV19(value: Double?, lang: String): String = when {
    value == null -> tr(lang, "Escribe la temperatura para interpretarla.", "Enter temperature for interpretation.")
    value <= 35.0 -> tr(lang, "Temperatura muy baja (≤35 °C): confirma la medición y solicita valoración clínica.", "Very low temperature (≤35 °C): confirm measurement and obtain clinical assessment.")
    value < 36.0 -> tr(lang, "Temperatura baja: confirma sitio, técnica y contexto.", "Low temperature: confirm site, technique and context.")
    value <= 37.5 -> tr(lang, "Dentro de un intervalo adulto habitual aproximado. La temperatura normal varía por persona, hora y sitio de medición.", "Within an approximate common adult interval. Normal temperature varies by person, time and measurement site.")
    value < 38.0 -> tr(lang, "Temperatura elevada, todavía por debajo del umbral habitual de fiebre de 38 °C. Correlaciona con síntomas y método.", "Elevated temperature, still below the usual 38 °C fever threshold. Correlate with symptoms and method.")
    value < 40.0 -> tr(lang, "Fiebre (≥38 °C): confirma la medición y valora el contexto clínico antes de continuar atención electiva.", "Fever (≥38 °C): confirm measurement and assess clinical context before elective care.")
    else -> tr(lang, "Fiebre muy alta (≥40 °C): requiere valoración médica prioritaria según el contexto.", "Very high fever (≥40 °C): needs prompt medical assessment depending on context.")
}

private enum class GlucoseContextV19 { FASTING, DIABETES_PREMEAL, DIABETES_POSTMEAL, RANDOM }

private fun glucoseContextNameV19(context: GlucoseContextV19, lang: String): String = when (context) {
    GlucoseContextV19.FASTING -> tr(lang, "Ayuno ≥8 h", "Fasting ≥8 h")
    GlucoseContextV19.DIABETES_PREMEAL -> tr(lang, "Diabetes · antes de comer", "Diabetes · premeal")
    GlucoseContextV19.DIABETES_POSTMEAL -> tr(lang, "Diabetes · 1–2 h poscomida", "Diabetes · 1–2 h postmeal")
    GlucoseContextV19.RANDOM -> tr(lang, "Casual / momento no definido", "Random / timing unknown")
}

private fun glucoseTextV19(value: Int?, context: GlucoseContextV19, lang: String): String {
    if (value == null) return tr(lang, "Escribe la glucosa capilar y selecciona el contexto.", "Enter capillary glucose and select context.")
    if (value < 70) return tr(lang,
        "<70 mg/dL: valor bajo/hipoglucemia para muchas personas con diabetes. Confirma el dato y sigue el protocolo clínico de hipoglucemia.",
        "<70 mg/dL: low/hypoglycemic value for many people with diabetes. Confirm and follow the clinical hypoglycemia protocol.")
    return when (context) {
        GlucoseContextV19.FASTING -> when {
            value <= 99 -> tr(lang, "70–99 mg/dL: referencia habitual de glucosa en ayuno normal.", "70–99 mg/dL: common normal fasting reference.")
            value <= 125 -> tr(lang, "100–125 mg/dL: glucosa en ayuno elevada; requiere valoración médica/laboratorial, no diagnóstico desde esta app.", "100–125 mg/dL: elevated fasting glucose; needs medical/laboratory assessment, not diagnosis by this app.")
            else -> tr(lang, "≥126 mg/dL: supera el umbral diagnóstico de glucosa plasmática en ayuno usado por ADA/CDC. Una lectura capilar aislada no confirma diabetes; requiere evaluación y confirmación médica.", "≥126 mg/dL: above the ADA/CDC fasting plasma diagnostic threshold. A single capillary reading does not diagnose diabetes; medical evaluation and confirmation are required.")
        }
        GlucoseContextV19.DIABETES_PREMEAL -> when {
            value in 80..130 -> tr(lang, "Dentro del objetivo preprandial frecuente de ADA para muchos adultos con diabetes: 80–130 mg/dL.", "Within the common ADA premeal target for many adults with diabetes: 80–130 mg/dL.")
            value < 80 -> tr(lang, "Por debajo del objetivo preprandial frecuente; correlaciona con síntomas, tratamiento y protocolo individual.", "Below the common premeal target; correlate with symptoms, therapy and the individual plan.")
            else -> tr(lang, "Por encima del objetivo preprandial frecuente de 80–130 mg/dL; confirmar y contextualizar.", "Above the common 80–130 mg/dL premeal target; confirm and contextualize.")
        }
        GlucoseContextV19.DIABETES_POSTMEAL -> if (value < 180) {
            tr(lang, "Por debajo del objetivo pico posprandial frecuente de ADA (<180 mg/dL, 1–2 h tras iniciar la comida).", "Below the common ADA peak postmeal target (<180 mg/dL, 1–2 h after the meal starts).")
        } else {
            tr(lang, "≥180 mg/dL: por encima del objetivo pico posprandial frecuente de ADA para muchos adultos con diabetes; confirmar y contextualizar.", "≥180 mg/dL: above the common ADA peak postmeal target for many adults with diabetes; confirm and contextualize.")
        }
        GlucoseContextV19.RANDOM -> when {
            value < 140 -> tr(lang, "Lectura casual sin señal de hipoglucemia; su interpretación depende del tiempo desde la última comida y del contexto.", "Random reading without hypoglycemia; interpretation depends on time since last meal and context.")
            value < 200 -> tr(lang, "Lectura casual elevada; registra hora/última comida y considera confirmación médica según antecedentes y síntomas.", "Elevated random reading; record timing/last meal and consider medical confirmation based on history and symptoms.")
            else -> tr(lang, "≥200 mg/dL es un umbral de importancia médica. ADA lo usa para diagnóstico con glucosa plasmática aleatoria solo cuando hay síntomas clásicos de hiperglucemia/crisis; una lectura capilar aislada en la app no establece diagnóstico.", "≥200 mg/dL is medically significant. ADA uses random plasma glucose at this threshold for diagnosis only with classic hyperglycemia symptoms/crisis; one capillary app reading does not establish a diagnosis.")
        }
    }
}

@Composable
fun VitalsInteractiveV19Screen(lang: String, onBack: () -> Unit) {
    val bands = listOf(
        VitalBandV19("0–6 m",30,50,82,205,60,90,30,62),
        VitalBandV19("6 m–2 a",20,40,100,190,60,90,30,62),
        VitalBandV19("2–7 a",15,30,60,140,78,112,48,78),
        VitalBandV19("8–11 a",15,25,60,140,85,114,52,85),
        VitalBandV19("≥12 a",13,20,60,100,95,135,58,88),
        VitalBandV19(tr(lang,"Adulto","Adult"),12,20,60,100,100,140,60,90)
    )
    var bandIndex by remember { mutableStateOf(5) }
    var rr by remember { mutableStateOf("") }
    var hr by remember { mutableStateOf("") }
    var sys by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var glucose by remember { mutableStateOf("") }
    var glucoseContext by remember { mutableStateOf(GlucoseContextV19.RANDOM) }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val band = bands[bandIndex]
    val bmi = run {
        val w = weight.toDoubleOrNull()
        val h = height.toDoubleOrNull()?.div(100.0)
        if (w != null && h != null && h > 0) w / h.pow(2) else null
    }

    ResponsiveScreenV17(
        tr(lang,"Signos vitales y glucosa","Vital signs and glucose"),
        tr(lang,"Registra, compara con referencias educativas y confirma cualquier valor anormal antes de decidir conducta clínica.","Record values, compare with teaching references and confirm abnormalities before clinical decisions."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang,"1 · Grupo de edad","1 · Age group")) {
            AdaptiveGridV17(bands.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3) { i ->
                FilterChip(bandIndex == i, { bandIndex = i }, { Text(bands[i].label) }, Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"2 · Frecuencia y presión arterial","2 · Rate and blood pressure")) {
            val cols = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
            AdaptiveGridV17(4, cols) { i ->
                when(i) {
                    0 -> OutlinedTextField(rr,{rr=it.filter(Char::isDigit).take(3)},label={Text("FR /min")},modifier=Modifier.fillMaxWidth())
                    1 -> OutlinedTextField(hr,{hr=it.filter(Char::isDigit).take(3)},label={Text("FC /min")},modifier=Modifier.fillMaxWidth())
                    2 -> OutlinedTextField(sys,{sys=it.filter(Char::isDigit).take(3)},label={Text(tr(lang,"TA sistólica","Systolic BP"))},modifier=Modifier.fillMaxWidth())
                    else -> OutlinedTextField(dia,{dia=it.filter(Char::isDigit).take(3)},label={Text(tr(lang,"TA diastólica","Diastolic BP"))},modifier=Modifier.fillMaxWidth())
                }
            }
            Text("FR ${band.rrMin}–${band.rrMax}: ${rangeTextV19(rr.toDoubleOrNull(),band.rrMin.toDouble(),band.rrMax.toDouble(),lang)}")
            Text("FC ${band.hrMin}–${band.hrMax}: ${rangeTextV19(hr.toDoubleOrNull(),band.hrMin.toDouble(),band.hrMax.toDouble(),lang)}")
            Text("TA sistólica ${band.sysMin}–${band.sysMax}: ${rangeTextV19(sys.toDoubleOrNull(),band.sysMin.toDouble(),band.sysMax.toDouble(),lang)}")
            Text("TA diastólica ${band.diaMin}–${band.diaMax}: ${rangeTextV19(dia.toDoubleOrNull(),band.diaMin.toDouble(),band.diaMax.toDouble(),lang)}")
        }
        ResponsiveSectionV17(tr(lang,"3 · Temperatura","3 · Temperature")) {
            OutlinedTextField(temperature,{temperature=it.filter{c->c.isDigit()||c=='.'}.take(5)},label={Text("°C")},modifier=Modifier.fillMaxWidth())
            VitalResultCardV19(temperatureTextV19(temperature.toDoubleOrNull(),lang))
            Text(tr(lang,"Referencia: alrededor de 37 °C es habitual; ≥38 °C suele considerarse fiebre; ≤35 °C es muy baja. El sitio y método de medición pueden cambiar la lectura.","Reference: around 37 °C is common; ≥38 °C is usually considered fever; ≤35 °C is very low. Measurement site and method can change the reading."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"4 · Glucosa capilar","4 · Capillary glucose")) {
            OutlinedTextField(glucose,{glucose=it.filter(Char::isDigit).take(4)},label={Text("mg/dL")},modifier=Modifier.fillMaxWidth())
            GlucoseContextV19.entries.forEach { ctx ->
                FilterChip(glucoseContext==ctx,{glucoseContext=ctx},{Text(glucoseContextNameV19(ctx,lang))},modifier=Modifier.fillMaxWidth())
            }
            VitalResultCardV19(glucoseTextV19(glucose.toIntOrNull(),glucoseContext,lang))
            Text(tr(lang,"Las cifras diagnósticas de diabetes corresponden a pruebas estandarizadas de plasma/laboratorio y requieren confirmación cuando no existe hiperglucemia inequívoca. La glucosa capilar de esta app es una guía para reconocer valores que ameritan atención.","Diabetes diagnostic thresholds refer to standardized plasma/laboratory testing and require confirmation unless hyperglycemia is unequivocal. Capillary glucose here is a guide to identify values needing attention."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17("IMC / BMI") {
            val cols = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
            AdaptiveGridV17(2,cols) { i ->
                if(i==0) OutlinedTextField(weight,{weight=it.filter{c->c.isDigit()||c=='.'}.take(6)},label={Text("kg")},modifier=Modifier.fillMaxWidth())
                else OutlinedTextField(height,{height=it.filter{c->c.isDigit()||c=='.'}.take(6)},label={Text("cm")},modifier=Modifier.fillMaxWidth())
            }
            Text(if(bmi==null) tr(lang,"IMC = peso (kg) / talla² (m)","BMI = weight (kg) / height² (m)") else "IMC = ${"%.1f".format(bmi)} kg/m²",fontWeight=FontWeight.Bold)
            Text(tr(lang,"En menores, interpreta IMC por edad y sexo; no apliques puntos de corte de adulto.","For children/adolescents, interpret BMI by age and sex; do not apply adult cutoffs."))
        }
        NoticeCard(tr(lang,"Fuentes educativas resumidas: NHS para temperatura; MedlinePlus/CDC/ADA para glucosa. Confirma siempre el protocolo institucional y el contexto del paciente.","Teaching sources summarized: NHS for temperature; MedlinePlus/CDC/ADA for glucose. Always confirm institutional protocol and patient context."))
    }
}

@Composable
private fun VitalResultCardV19(text: String) {
    Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer),modifier=Modifier.fillMaxWidth()) {
        Text(text,Modifier.padding(12.dp),fontWeight=FontWeight.SemiBold)
    }
}

// ---------- CPOD / ceod REALMENTE INTERACTIVO ----------

private fun cpodStatusNameV19(status: ToothStatus, lang: String): String = when(status) {
    ToothStatus.HEALTHY -> tr(lang,"Sano / presente","Sound / present")
    ToothStatus.CARIES -> tr(lang,"Cariado","Decayed")
    ToothStatus.RESTORED -> tr(lang,"Obturado","Filled")
    ToothStatus.MISSING_CARIES -> tr(lang,"Ausente por caries","Missing due to caries")
    ToothStatus.MISSING_OTHER -> tr(lang,"Ausente por otra causa","Missing other cause")
    ToothStatus.EXTRACTION_INDICATED -> tr(lang,"Extracción indicada","Extraction indicated")
    ToothStatus.SEALANT -> tr(lang,"Sellador / no cuenta como O","Sealant / not counted as F")
}

@Composable
fun CpodInteractiveV19Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var primary by remember { mutableStateOf(false) }
    val shown = if(primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    var selectedTooth by remember { mutableStateOf(shown.first()) }
    if(selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val result = ClinicalEngines.cpod(session.teeth,primary)
    val choices = listOf(ToothStatus.HEALTHY,ToothStatus.CARIES,ToothStatus.RESTORED,ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER,ToothStatus.SEALANT)

    fun setStatus(status: ToothStatus) {
        val present = session.presentTeeth.toMutableSet()
        if(status==ToothStatus.MISSING_CARIES || status==ToothStatus.MISSING_OTHER) present.remove(selectedTooth) else present.add(selectedTooth)
        onSessionChanged(session.copy(teeth=session.teeth+(selectedTooth to record.copy(status=status)),presentTeeth=present))
    }

    ResponsiveScreenV17(
        tr(lang,"CPOD / ceod interactivo","Interactive DMFT / dmft"),
        tr(lang,"Toca cada diente y clasifícalo. El índice se recalcula automáticamente.","Tap each tooth and classify it. The index recalculates automatically."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang,"Dentición","Dentition")) {
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2) { i ->
                val isPrimary=i==1
                FilterChip(primary==isPrimary,{primary=isPrimary},{Text(if(isPrimary)tr(lang,"Temporal · ceod","Primary · dmft") else tr(lang,"Permanente · CPOD","Permanent · DMFT"))},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"Arcadas: maxilar arriba · mandibular abajo","Arches: maxillary above · mandibular below")) {
            DentalArchSelector(shown,selectedTooth,{selectedTooth=it}) { tooth ->
                session.teeth[tooth]?.status?.let { it != ToothStatus.HEALTHY } == true
            }
        }
        ResponsiveSectionV17("OD $selectedTooth") {
            choices.forEach { status ->
                FilterChip(record.status==status,{setStatus(status)},{Text(cpodStatusNameV19(status,lang))},modifier=Modifier.fillMaxWidth())
            }
        }
        Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),modifier=Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                Text(if(primary)"ceod" else "CPOD",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                if(primary) Text("c = ${result.carious}   e = ${result.missing}   o = ${result.filled}")
                else Text("C = ${result.carious}   P = ${result.missing}   O = ${result.filled}")
                Text("${if(primary)"ceod" else "CPOD"} = ${result.total}",fontWeight=FontWeight.Black,style=MaterialTheme.typography.headlineSmall)
                Text(ClinicalEngines.cpodInterpretation(result.total,lang))
            }
        }
        NoticeCard(tr(lang,"Regla de conteo: la unidad es el diente. Si existe restauración y además caries activa, se cuenta como cariado. Una ausencia por causa distinta de caries no se suma como P/e. La extracción indicada se registra clínicamente, pero no debe convertirse automáticamente en perdido por caries.","Counting rule: the unit is the tooth. If a restoration and active caries coexist, count as decayed. Missing for a reason other than caries does not count as M/e. Extraction indicated is a clinical finding and should not automatically become missing due to caries."))
    }
}

// ---------- MUCOSAS: BOCA ABIERTA CLARA ----------

private data class MucosaV19(
    val id:String,val es:String,val en:String,
    val normalEs:String,val normalEn:String,
    val changeEs:String,val changeEn:String,
    val exploreEs:String,val exploreEn:String
)

private val mucosaV19Zones = listOf(
    MucosaV19("labio_sup","Labio superior / bermellón","Upper lip / vermilion","Rosado, íntegro e hidratado.","Pink, intact and hydrated.","Fisuras, costras, vesículas, úlceras, edema, pigmentación o resequedad.","Fissures, crusts, vesicles, ulcers, edema, pigmentation or dryness.","Inspecciona, eversa el labio y palpa si hay aumento de volumen o induración.","Inspect, evert the lip and palpate swelling or induration."),
    MucosaV19("labio_inf","Labio inferior / mucosa labial","Lower lip / labial mucosa","Rosado, húmedo, blando e íntegro.","Pink, moist, soft and intact.","Mucocele, úlcera, fisura, edema, pigmentación o resequedad.","Mucocele, ulcer, fissure, edema, pigmentation or dryness.","Eversa y revisa fondo de vestíbulo, frenillo y glándulas menores.","Evert and inspect vestibule, frenum and minor glands."),
    MucosaV19("carrillo_der","Carrillo derecho / mucosa yugal","Right buccal mucosa","Rosada, húmeda y lisa; salida parotídea sin alteración aparente.","Pink, moist and smooth; parotid opening without apparent change.","Línea alba, mordisqueo, úlcera, placa blanca/roja, masa o cambio salival.","Linea alba, biting, ulcer, white/red plaque, mass or salivary change.","Retrae con espejo y recorre de comisura a región posterior.","Retract with mirror and inspect from commissure posteriorly."),
    MucosaV19("carrillo_izq","Carrillo izquierdo / mucosa yugal","Left buccal mucosa","Rosada, húmeda y lisa, sin induración.","Pink, moist and smooth, without induration.","Línea alba, mordisqueo, úlcera, placa, masa o pigmentación atípica.","Linea alba, biting, ulcer, plaque, mass or atypical pigmentation.","Compara ambos carrillos y palpa si hay masa.","Compare both cheeks and palpate a mass when present."),
    MucosaV19("encia","Encía y mucosa alveolar","Gingiva and alveolar mucosa","Encía firme y contorno regular; mucosa alveolar móvil y húmeda.","Firm gingiva with regular contour; alveolar mucosa mobile and moist.","Eritema, edema, sangrado, recesión, fístula, úlcera o aumento de volumen.","Erythema, edema, bleeding, recession, fistula, ulcer or swelling.","Inspecciona encía marginal, papilar y adherida y correlaciona con sondaje.","Inspect marginal, papillary and attached gingiva and correlate with probing."),
    MucosaV19("paladar_duro","Paladar duro","Hard palate","Rosado pálido, firme y queratinizado; rugas y rafe reconocibles.","Pale pink, firm and keratinized; rugae and raphe recognizable.","Torus, petequias, placa, úlcera, eritema, quemadura o masa.","Torus, petechiae, plaque, ulcer, erythema, burn or mass.","Ilumina directamente y palpa elevaciones.","Use direct light and palpate elevations."),
    MucosaV19("paladar_blando","Paladar blando, úvula y pilares","Soft palate, uvula and pillars","Rosado y móvil, elevación simétrica y úvula centrada.","Pink and mobile, symmetric elevation and centered uvula.","Eritema, petequias, exudado, úlcera, asimetría o desviación de úvula.","Erythema, petechiae, exudate, ulcer, asymmetry or uvular deviation.","Pide abrir y fonar; observa movilidad, pilares y transición a orofaringe.","Ask patient to open and phonate; observe movement, pillars and oropharynx."),
    MucosaV19("lengua","Lengua: dorso y bordes","Tongue: dorsum and borders","Rosada, papilada, húmeda, móvil y sin induración.","Pink, papillary, moist, mobile and without induration.","Saburra, depapilación, fisuras, placa blanca/roja, úlcera, masa o induración.","Coating, depapillation, fissures, white/red plaque, ulcer, mass or induration.","Protruye, moviliza con gasa y revisa dorso, bordes y cara ventral.","Protrude, move with gauze and inspect dorsum, borders and ventral surface."),
    MucosaV19("piso","Piso de boca","Floor of mouth","Rosado, blando y húmedo, sin masas ni aumento de volumen.","Pink, soft and moist, without masses or swelling.","Ránula, masa, color azul/rojo, úlcera, dolor o disminución salival.","Ranula, mass, blue/red color, ulcer, pain or reduced salivary flow.","Eleva la lengua y realiza palpación bimanual cuando esté indicada.","Lift tongue and use bimanual palpation when indicated."),
    MucosaV19("orofaringe","Orofaringe / amígdalas","Oropharynx / tonsils","Pilares sin inflamación evidente, sin exudado o lesión aparente.","Pillars without evident inflammation, exudate or apparent lesion.","Eritema, exudado, hipertrofia, placa, asimetría o ulceración.","Erythema, exudate, hypertrophy, plaque, asymmetry or ulceration.","Inspecciona con luz y depresor cuando proceda, evitando reflejo nauseoso innecesario.","Inspect with light and depressor when appropriate, avoiding unnecessary gag reflex." )
)

@Composable
fun MucosaInteractiveV19Screen(lang:String,onBack:()->Unit) {
    var selectedId by remember { mutableStateOf("lengua") }
    var tab by remember { mutableStateOf(0) }
    var finding by remember { mutableStateOf("Normal") }
    var sizeMm by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val selected=mucosaV19Zones.first{it.id==selectedId}
    val name=if(lang=="en")selected.en else selected.es
    val example=if(finding=="Normal") {
        "$name: ${if(lang=="en")selected.normalEn else selected.normalEs}"
    } else tr(lang,
        "$name: $finding; tamaño aproximado ${if(sizeMm.isBlank())"no registrado" else "$sizeMm mm"}${if(notes.isBlank())"" else "; $notes"}. Descripción clínica; correlacionar y completar valoración.",
        "$name: $finding; approximate size ${if(sizeMm.isBlank())"not entered" else "$sizeMm mm"}${if(notes.isBlank())"" else "; $notes"}. Clinical description; correlate and complete assessment."
    )

    ResponsiveScreenV17(
        tr(lang,"Mucosas orales interactivas","Interactive oral mucosa"),
        tr(lang,"Toca directamente una zona de la boca abierta; después compara normalidad, alteraciones y técnica de exploración.","Tap a region on the open-mouth diagram, then compare normal findings, changes and examination technique."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang,"1 · Boca abierta: selecciona una zona","1 · Open mouth: select a region")) {
            OpenMouthMapV19(selectedId){selectedId=it}
            Text("${tr(lang,"Zona seleccionada","Selected region")}: $name",fontWeight=FontWeight.Black,color=MaterialTheme.colorScheme.primary)
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)) {
                mucosaV19Zones.forEach { z -> FilterChip(selectedId==z.id,{selectedId=z.id},{Text(if(lang=="en")z.en else z.es)}) }
            }
        }
        ResponsiveSectionV17(name) {
            AdaptiveGridV17(3,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 3) { i ->
                val label=listOf(tr(lang,"Normal","Normal"),tr(lang,"Alteraciones","Changes"),tr(lang,"Cómo explorar","How to examine"))[i]
                FilterChip(tab==i,{tab=i},{Text(label)},modifier=Modifier.fillMaxWidth())
            }
            val text=when(tab){1->if(lang=="en")selected.changeEn else selected.changeEs;2->if(lang=="en")selected.exploreEn else selected.exploreEs;else->if(lang=="en")selected.normalEn else selected.normalEs}
            VitalResultCardV19(text)
        }
        ResponsiveSectionV17(tr(lang,"2 · Registro rápido","2 · Quick description")) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)) {
                listOf("Normal","Úlcera","Placa blanca","Placa roja","Eritema","Aumento de volumen","Pigmentación","Vesícula","Fístula","Masa").forEach { f -> FilterChip(finding==f,{finding=f},{Text(f)}) }
            }
            if(finding!="Normal") {
                OutlinedTextField(sizeMm,{sizeMm=it.filter{c->c.isDigit()||c=='.'}.take(6)},label={Text(tr(lang,"Tamaño aproximado (mm)","Approximate size (mm)"))},modifier=Modifier.fillMaxWidth())
                OutlinedTextField(notes,{notes=it.take(180)},label={Text(tr(lang,"Color, superficie, bordes, consistencia y síntomas","Color, surface, borders, consistency and symptoms"))},modifier=Modifier.fillMaxWidth())
            }
        }
        Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),modifier=Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                Text(tr(lang,"¿Qué escribo en el expediente?","What do I write in the record?"),fontWeight=FontWeight.Black)
                Text(example)
            }
        }
        NoticeCard(tr(lang,"Describe antes de diagnosticar. Lesiones persistentes, induradas, ulceradas sin causa clara, masas, crecimiento o signos de alarma requieren supervisión docente/profesional y seguimiento.","Describe before diagnosing. Persistent, indurated, unexplained ulcerated lesions, masses, growth or warning signs require faculty/professional assessment and follow-up."))
    }
}

@Composable
private fun OpenMouthMapV19(selectedId:String,onSelected:(String)->Unit) {
    val outline=MaterialTheme.colorScheme.outline
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer.copy(alpha=.35f))) {
        Canvas(Modifier.fillMaxWidth().height(340.dp).padding(8.dp).pointerInput(Unit){
            detectTapGestures { p ->
                val x=p.x/size.width.toFloat(); val y=p.y/size.height.toFloat()
                val id=when {
                    y<.12f -> "labio_sup"
                    y>.88f -> "labio_inf"
                    x<.18f && y in .20f.. .78f -> "carrillo_der"
                    x>.82f && y in .20f.. .78f -> "carrillo_izq"
                    y<.32f -> "paladar_duro"
                    y<.42f -> "paladar_blando"
                    y in .42f.. .50f -> "orofaringe"
                    y in .50f.. .75f -> "lengua"
                    y>.75f -> "piso"
                    else -> "encia"
                }
                onSelected(id)
            }
        }) {
            val w=size.width; val h=size.height
            val lip=Color(0xFFE98CA5); val oral=Color(0xFF5E2338); val palate=Color(0xFFE8AAA8); val tongue=Color(0xFFE87886); val gum=Color(0xFFD98592)
            val outer=Path().apply {
                moveTo(w*.08f,h*.50f)
                cubicTo(w*.18f,h*.08f,w*.36f,h*.02f,w*.50f,h*.07f)
                cubicTo(w*.64f,h*.02f,w*.82f,h*.08f,w*.92f,h*.50f)
                cubicTo(w*.82f,h*.92f,w*.64f,h*.98f,w*.50f,h*.93f)
                cubicTo(w*.36f,h*.98f,w*.18f,h*.92f,w*.08f,h*.50f)
                close()
            }
            drawPath(outer,lip); drawPath(outer,outline,style=Stroke(3f))
            val cavity=Path().apply {
                moveTo(w*.16f,h*.48f)
                cubicTo(w*.22f,h*.20f,w*.36f,h*.14f,w*.50f,h*.16f)
                cubicTo(w*.64f,h*.14f,w*.78f,h*.20f,w*.84f,h*.48f)
                cubicTo(w*.78f,h*.78f,w*.65f,h*.86f,w*.50f,h*.84f)
                cubicTo(w*.35f,h*.86f,w*.22f,h*.78f,w*.16f,h*.48f)
                close()
            }
            drawPath(cavity,oral)
            // paladar duro y blando con úvula
            drawOval(palate,Offset(w*.30f,h*.19f),Size(w*.40f,h*.20f))
            drawOval(Color(0xFFD99298),Offset(w*.35f,h*.31f),Size(w*.30f,h*.11f))
            val uvula=Path().apply{moveTo(w*.47f,h*.37f);quadraticBezierTo(w*.50f,h*.49f,w*.53f,h*.37f);close()}
            drawPath(uvula,Color(0xFFC96E7B))
            // encía superior/inferior
            drawArc(gum,200f,140f,false,Offset(w*.22f,h*.30f),Size(w*.56f,h*.22f),style=Stroke(h*.035f))
            drawArc(gum,20f,140f,false,Offset(w*.22f,h*.58f),Size(w*.56f,h*.20f),style=Stroke(h*.035f))
            // dientes superiores e inferiores en arco
            repeat(10) { i ->
                val f=i/9f
                val x=w*(.245f+.51f*f)
                val dy=kotlin.math.abs(f-.5f)*h*.035f
                drawRoundRect(Color(0xFFFFFDF8),Offset(x-w*.022f,h*.36f+dy),Size(w*.044f,h*.095f),CornerRadius(7f,7f))
                drawRoundRect(outline,Offset(x-w*.022f,h*.36f+dy),Size(w*.044f,h*.095f),CornerRadius(7f,7f),style=Stroke(1.6f))
                drawRoundRect(Color(0xFFFFFDF8),Offset(x-w*.022f,h*.65f-dy),Size(w*.044f,h*.09f),CornerRadius(7f,7f))
                drawRoundRect(outline,Offset(x-w*.022f,h*.65f-dy),Size(w*.044f,h*.09f),CornerRadius(7f,7f),style=Stroke(1.6f))
            }
            // lengua claramente visible
            val tonguePath=Path().apply {
                moveTo(w*.29f,h*.69f)
                cubicTo(w*.31f,h*.52f,w*.40f,h*.48f,w*.50f,h*.49f)
                cubicTo(w*.60f,h*.48f,w*.69f,h*.52f,w*.71f,h*.69f)
                cubicTo(w*.64f,h*.80f,w*.57f,h*.82f,w*.50f,h*.82f)
                cubicTo(w*.43f,h*.82f,w*.36f,h*.80f,w*.29f,h*.69f)
                close()
            }
            drawPath(tonguePath,tongue)
            drawLine(Color(0xFFC75F70),Offset(w*.50f,h*.55f),Offset(w*.50f,h*.76f),strokeWidth=2.5f)
            val centers=mapOf(
                "labio_sup" to Offset(w*.50f,h*.07f),"labio_inf" to Offset(w*.50f,h*.93f),
                "carrillo_der" to Offset(w*.13f,h*.52f),"carrillo_izq" to Offset(w*.87f,h*.52f),
                "encia" to Offset(w*.50f,h*.35f),"paladar_duro" to Offset(w*.50f,h*.25f),
                "paladar_blando" to Offset(w*.50f,h*.36f),"orofaringe" to Offset(w*.50f,h*.45f),
                "lengua" to Offset(w*.50f,h*.64f),"piso" to Offset(w*.50f,h*.84f)
            )
            centers[selectedId]?.let { c -> drawCircle(MaterialTheme.colorScheme.primary.copy(alpha=.72f),minOf(w,h)*.055f,c);drawCircle(Color.White,minOf(w,h)*.018f,c) }
        }
    }
}

// ---------- OCLUSIÓN CON DIBUJOS DOCENTES PROPIOS ----------

private data class OcclusionTopicV19(val es:String,val en:String,val bodyEs:String,val bodyEn:String)

@Composable
fun OcclusionInteractiveV19Screen(lang:String,onBack:()->Unit) {
    var selected by remember { mutableStateOf(0) }
    val topics=listOf(
        OcclusionTopicV19("Planos terminales","Terminal planes","En dentición temporal compara las caras distales de los segundos molares: plano terminal recto (flush), escalón mesial y escalón distal.","In primary dentition compare distal surfaces of second molars: flush terminal plane, mesial step and distal step."),
        OcclusionTopicV19("Angle molar","Molar Angle class","Relaciona los primeros molares permanentes. La app muestra Clase I, II y III como esquema de referencia, no como sustituto de la exploración.","Relate the permanent first molars. The app shows Class I, II and III as a reference, not a substitute for examination."),
        OcclusionTopicV19("Relación canina","Canine relation","Relaciona la cúspide del canino superior con canino y primer premolar inferiores; se describe como Clase I, II o III.","Relate the upper canine cusp to the lower canine/first-premolar region; describe Class I, II or III."),
        OcclusionTopicV19("Overjet","Overjet","Distancia horizontal entre incisivos superiores e inferiores. Registra milímetros y si es positivo, reducido, borde a borde o invertido.","Horizontal distance between upper and lower incisors. Record millimeters and whether positive, reduced, edge-to-edge or reversed."),
        OcclusionTopicV19("Overbite / mordida abierta","Overbite / open bite","Traslape vertical anterior. Registra milímetros o porcentaje y reconoce sobremordida profunda, borde a borde y mordida abierta.","Anterior vertical overlap. Record millimeters or percentage and recognize deep bite, edge-to-edge and open bite."),
        OcclusionTopicV19("Mordida cruzada","Crossbite","Describe si es anterior o posterior y unilateral o bilateral; valora además desviación funcional de línea media.","Describe anterior/posterior and unilateral/bilateral crossbite; also assess functional midline shift.")
    )
    ResponsiveScreenV17(tr(lang,"Examen de oclusión","Occlusal examination"),tr(lang,"Selecciona un concepto para ver una lámina esquemática propia y su significado clínico.","Select a concept to see an original schematic reference and its clinical meaning."),onBack) { profile ->
        ResponsiveSectionV17(tr(lang,"Conceptos","Concepts")) {
            AdaptiveGridV17(topics.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 3) { i ->
                FilterChip(selected==i,{selected=i},{Text(if(lang=="en")topics[i].en else topics[i].es)},modifier=Modifier.fillMaxWidth())
            }
        }
        val t=topics[selected]
        ResponsiveSectionV17(if(lang=="en")t.en else t.es) {
            when(selected) {
                0 -> TerminalPlanesV19(lang)
                1 -> AngleMolarV19(lang)
                2 -> CanineRelationV19(lang)
                3 -> OverjetV19(lang)
                4 -> OverbiteV19(lang)
                else -> CrossbiteV19(lang)
            }
            Text(if(lang=="en")t.bodyEn else t.bodyEs,fontWeight=FontWeight.SemiBold)
        }
        NoticeCard(tr(lang,"Los dibujos son esquemas originales de YSM Expediente. Base conceptual: relaciones de dentición temporal descritas por Columbia University y definiciones ortodóncicas convencionales de Angle, overjet, overbite y mordida cruzada. Correlaciona siempre con examen clínico y criterios docentes.","These are original YSM Expediente schematics. Conceptual basis: primary-dentition relationships described by Columbia University and conventional orthodontic definitions of Angle, overjet, overbite and crossbite. Always correlate with clinical examination and faculty criteria."))
    }
}

@Composable private fun TerminalPlanesV19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        OcclusionMiniCardV19(tr(lang,"Plano terminal recto","Flush terminal plane")) { TerminalPlaneDrawingV19(0f) }
        OcclusionMiniCardV19(tr(lang,"Escalón mesial","Mesial step")) { TerminalPlaneDrawingV19(-.12f) }
        OcclusionMiniCardV19(tr(lang,"Escalón distal","Distal step")) { TerminalPlaneDrawingV19(.12f) }
    }
}

@Composable private fun AngleMolarV19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        OcclusionMiniCardV19("Angle I") { MolarDrawingV19(0f) }
        OcclusionMiniCardV19("Angle II") { MolarDrawingV19(.13f) }
        OcclusionMiniCardV19("Angle III") { MolarDrawingV19(-.13f) }
    }
}

@Composable private fun CanineRelationV19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        OcclusionMiniCardV19(tr(lang,"Canina Clase I","Canine Class I")) { CanineDrawingV19(0f) }
        OcclusionMiniCardV19(tr(lang,"Canina Clase II","Canine Class II")) { CanineDrawingV19(.12f) }
        OcclusionMiniCardV19(tr(lang,"Canina Clase III","Canine Class III")) { CanineDrawingV19(-.12f) }
    }
}

@Composable private fun OverjetV19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        OcclusionMiniCardV19(tr(lang,"Overjet positivo / aumentado","Positive / increased overjet")) { IncisorHorizontalV19(.14f) }
        OcclusionMiniCardV19(tr(lang,"Borde a borde","Edge-to-edge")) { IncisorHorizontalV19(0f) }
        OcclusionMiniCardV19(tr(lang,"Overjet invertido","Reverse overjet")) { IncisorHorizontalV19(-.12f) }
    }
}

@Composable private fun OverbiteV19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        OcclusionMiniCardV19(tr(lang,"Overbite habitual","Usual overbite")) { IncisorVerticalV19(.30f) }
        OcclusionMiniCardV19(tr(lang,"Sobremordida profunda","Deep overbite")) { IncisorVerticalV19(.62f) }
        OcclusionMiniCardV19(tr(lang,"Mordida abierta","Open bite")) { IncisorVerticalV19(-.18f) }
    }
}

@Composable private fun CrossbiteV19(lang:String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        OcclusionMiniCardV19(tr(lang,"Relación transversal habitual","Usual transverse relation")) { CrossbiteDrawingV19(false) }
        OcclusionMiniCardV19(tr(lang,"Mordida cruzada posterior","Posterior crossbite")) { CrossbiteDrawingV19(true) }
    }
}

@Composable
private fun OcclusionMiniCardV19(title:String,drawing:@Composable()->Unit) {
    Card(modifier=Modifier.fillMaxWidth(),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.35f)),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(10.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(4.dp)) {
            Text(title,fontWeight=FontWeight.Black,textAlign=TextAlign.Center)
            drawing()
        }
    }
}

private fun DrawScope.toothBlockV19(x:Float,y:Float,w:Float,h:Float,color:Color,outline:Color) {
    val p=Path().apply {
        moveTo(x+w*.10f,y+h*.10f);quadraticBezierTo(x+w*.50f,y-h*.04f,x+w*.90f,y+h*.10f)
        lineTo(x+w*.84f,y+h*.86f);quadraticBezierTo(x+w*.50f,y+h,x+w*.16f,y+h*.86f);close()
    }
    drawPath(p,color);drawPath(p,outline,style=Stroke(2f))
}

@Composable private fun TerminalPlaneDrawingV19(lowerShift:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer; val lower=MaterialTheme.colorScheme.secondaryContainer; val outline=MaterialTheme.colorScheme.outline; val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(120.dp)) {
        val w=size.width; val h=size.height; val tw=w*.19f; val th=h*.30f; val x=w*.40f
        toothBlockV19(x,h*.16f,tw,th,upper,outline)
        val lx=x+w*lowerShift
        toothBlockV19(lx,h*.58f,tw,th,lower,outline)
        val upperDistal=x+tw; val lowerDistal=lx+tw
        drawLine(accent,Offset(upperDistal,h*.10f),Offset(upperDistal,h*.52f),strokeWidth=4f)
        drawLine(accent.copy(alpha=.65f),Offset(lowerDistal,h*.53f),Offset(lowerDistal,h*.95f),strokeWidth=4f)
        drawLine(outline,Offset(w*.12f,h*.50f),Offset(w*.88f,h*.50f),strokeWidth=1.5f)
    }
}

@Composable private fun MolarDrawingV19(lowerShift:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer; val lower=MaterialTheme.colorScheme.secondaryContainer; val outline=MaterialTheme.colorScheme.outline; val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val w=size.width;val h=size.height;val tw=w*.25f;val th=h*.34f;val x=w*.38f
        toothBlockV19(x,h*.12f,tw,th,upper,outline)
        val lx=x+w*lowerShift
        toothBlockV19(lx,h*.58f,tw,th,lower,outline)
        drawCircle(accent,7f,Offset(x+tw*.35f,h*.46f))
        drawCircle(accent.copy(alpha=.65f),7f,Offset(lx+tw*.55f,h*.58f))
        drawLine(accent,Offset(x+tw*.35f,h*.46f),Offset(lx+tw*.55f,h*.58f),strokeWidth=3f)
    }
}

@Composable private fun CanineDrawingV19(lowerShift:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer;val lower=MaterialTheme.colorScheme.secondaryContainer;val outline=MaterialTheme.colorScheme.outline;val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val w=size.width;val h=size.height
        val ux=w*.48f
        val canine=Path().apply{moveTo(ux-w*.08f,h*.15f);lineTo(ux+w*.08f,h*.15f);lineTo(ux,h*.48f);close()}
        drawPath(canine,upper);drawPath(canine,outline,style=Stroke(2f))
        val base=w*(.40f+lowerShift)
        toothBlockV19(base,h*.60f,w*.14f,h*.25f,lower,outline);toothBlockV19(base+w*.15f,h*.60f,w*.14f,h*.25f,lower,outline)
        val embrasure=base+w*.145f
        drawLine(accent,Offset(ux,h*.48f),Offset(embrasure,h*.60f),strokeWidth=4f)
    }
}

@Composable private fun IncisorHorizontalV19(shift:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer;val lower=MaterialTheme.colorScheme.secondaryContainer;val outline=MaterialTheme.colorScheme.outline;val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val w=size.width;val h=size.height;val iw=w*.13f
        toothBlockV19(w*.42f,h*.10f,iw,h*.42f,upper,outline)
        toothBlockV19(w*(.42f+shift),h*.58f,iw,h*.34f,lower,outline)
        val y=h*.54f;val x1=w*.485f;val x2=w*(.485f+shift)
        drawLine(accent,Offset(x1,y),Offset(x2,y),strokeWidth=5f)
        drawLine(accent,Offset(x2,y),Offset(x2-kotlin.math.sign(x2-x1)*12f,y-8f),strokeWidth=4f)
        drawLine(accent,Offset(x2,y),Offset(x2-kotlin.math.sign(x2-x1)*12f,y+8f),strokeWidth=4f)
    }
}

@Composable private fun IncisorVerticalV19(overlap:Float) {
    val upper=MaterialTheme.colorScheme.primaryContainer;val lower=MaterialTheme.colorScheme.secondaryContainer;val outline=MaterialTheme.colorScheme.outline;val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(135.dp)) {
        val w=size.width;val h=size.height;val iw=w*.13f;val uh=h*.45f;val lh=h*.34f
        toothBlockV19(w*.42f,h*.08f,iw,uh,upper,outline)
        val lowerY=h*(.52f-overlap*.24f)
        toothBlockV19(w*.44f,lowerY,iw,lh,lower,outline)
        drawLine(accent,Offset(w*.60f,h*.53f),Offset(w*.60f,lowerY),strokeWidth=4f)
    }
}

@Composable private fun CrossbiteDrawingV19(cross:Boolean) {
    val upper=MaterialTheme.colorScheme.primaryContainer;val lower=MaterialTheme.colorScheme.secondaryContainer;val outline=MaterialTheme.colorScheme.outline;val accent=MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(135.dp)) {
        val w=size.width;val h=size.height
        val upperLeft=if(cross)w*.37f else w*.31f; val upperRight=if(cross)w*.63f else w*.69f
        toothBlockV19(upperLeft-w*.07f,h*.18f,w*.14f,h*.30f,upper,outline);toothBlockV19(upperRight-w*.07f,h*.18f,w*.14f,h*.30f,upper,outline)
        toothBlockV19(w*.36f,h*.58f,w*.14f,h*.27f,lower,outline);toothBlockV19(w*.50f,h*.58f,w*.14f,h*.27f,lower,outline)
        drawLine(accent,Offset(w*.50f,h*.10f),Offset(w*.50f,h*.92f),strokeWidth=2.5f)
    }
}
