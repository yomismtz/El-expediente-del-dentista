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
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
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
    var rr by remember{mutableStateOf("")}; var hr by remember{mutableStateOf("")}
    var sys by remember{mutableStateOf("")}; var dia by remember{mutableStateOf("")}
    var temp by remember{mutableStateOf("")}; var glucose by remember{mutableStateOf("")}
    var glucoseContext by remember{mutableStateOf(GlucoseContext19.RANDOM)}
    var weight by remember{mutableStateOf("")}; var height by remember{mutableStateOf("")}
    val b=bands[bandIndex]
    val bmi=run { val w=weight.toDoubleOrNull(); val h=height.toDoubleOrNull()?.div(100.0); if(w!=null&&h!=null&&h>0)w/h.pow(2) else null }

    ResponsiveScreenV17(tr(lang,"Signos vitales y glucosa","Vital signs and glucose"),tr(lang,"Registra valores y compáralos con referencias educativas; confirma cualquier valor anormal.","Record values and compare with teaching references; confirm any abnormal value."),onBack) { profile ->
        ResponsiveSectionV17(tr(lang,"1 · Grupo de edad","1 · Age group")) {
            AdaptiveGridV17(bands.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 3) { i ->
                FilterChip(bandIndex==i,{bandIndex=i},{Text(bands[i].label)},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"2 · FR, FC y presión arterial","2 · RR, HR and blood pressure")) {
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
        ResponsiveSectionV17(tr(lang,"3 · Temperatura","3 · Temperature")) {
            OutlinedTextField(temp,{temp=it.filter{c->c.isDigit()||c=='.'}.take(5)},label={Text("°C")},modifier=Modifier.fillMaxWidth())
            ResultCard19(temperature19(temp.toDoubleOrNull(),lang))
            Text(tr(lang,"Referencia resumida: alrededor de 37 °C es habitual; ≥38 °C suele considerarse fiebre; ≤35 °C es muy baja. Sitio y método modifican la lectura.","Summary reference: around 37 °C is common; ≥38 °C is usually fever; ≤35 °C is very low. Site and method affect the reading."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17(tr(lang,"4 · Glucosa capilar","4 · Capillary glucose")) {
            OutlinedTextField(glucose,{glucose=it.filter(Char::isDigit).take(4)},label={Text("mg/dL")},modifier=Modifier.fillMaxWidth())
            GlucoseContext19.entries.forEach { ctx -> FilterChip(glucoseContext==ctx,{glucoseContext=ctx},{Text(glucoseContext19(ctx,lang))},modifier=Modifier.fillMaxWidth()) }
            ResultCard19(glucose19(glucose.toIntOrNull(),glucoseContext,lang))
            Text(tr(lang,"Los umbrales diagnósticos corresponden a pruebas estandarizadas de plasma/laboratorio y requieren confirmación. La lectura capilar sirve aquí como alerta educativa.","Diagnostic thresholds refer to standardized plasma/laboratory tests and require confirmation. Capillary readings here are an educational alert."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17("IMC / BMI") {
            val cols=if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2
            AdaptiveGridV17(2,cols) { i -> if(i==0)
                OutlinedTextField(weight,{weight=it.filter{c->c.isDigit()||c=='.'}.take(6)},label={Text("kg")},modifier=Modifier.fillMaxWidth())
            else OutlinedTextField(height,{height=it.filter{c->c.isDigit()||c=='.'}.take(6)},label={Text("cm")},modifier=Modifier.fillMaxWidth()) }
            Text(if(bmi==null)tr(lang,"IMC = peso / talla²","BMI = weight / height²") else "IMC = ${"%.1f".format(bmi)} kg/m²",fontWeight=FontWeight.Bold)
            Text(tr(lang,"En niños y adolescentes, interpreta IMC por edad y sexo.","For children and adolescents, interpret BMI by age and sex."))
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

private fun cpodSurfaceMark19(mark:SurfaceMark,lang:String):String = when(mark) {
    SurfaceMark.HEALTHY -> tr(lang,"Sana / borrar","Sound / clear")
    SurfaceMark.CARIES -> tr(lang,"Cariada","Decayed")
    SurfaceMark.RESTORATION -> tr(lang,"Obturada","Filled")
    SurfaceMark.SEALANT -> tr(lang,"Sellada / no cuenta","Sealant / not counted")
}

private fun cpodSurfaces19(tooth:Int):List<Surface> {
    val position=tooth%10
    val posterior=position>=4
    return if(posterior) listOf(Surface.VESTIBULAR,Surface.LINGUAL_PALATAL,Surface.MESIAL,Surface.DISTAL,Surface.OCCLUSAL)
    else listOf(Surface.VESTIBULAR,Surface.LINGUAL_PALATAL,Surface.MESIAL,Surface.DISTAL)
}

@Composable
fun CpodInteractiveV19Screen(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onBack:()->Unit) {
    var primary by remember{mutableStateOf(false)}
    var surfaceMode by remember{mutableStateOf(false)}
    var selectedMark by remember{mutableStateOf(SurfaceMark.CARIES)}
    val shown=if(primary)ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    var selected by remember{mutableStateOf(shown.first())}
    if(selected !in shown) selected=shown.first()
    val record=session.teeth[selected]?:ToothRecord()
    val result=ClinicalEngines.cpod(session.teeth,primary)
    val choices=listOf(ToothStatus.HEALTHY,ToothStatus.CARIES,ToothStatus.RESTORED,ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER,ToothStatus.SEALANT)
    val applicableSurfaces=cpodSurfaces19(selected)
    val surfaceMap=session.odontogramSurfaces[selected]?:emptyMap()

    fun setStatus(status:ToothStatus) {
        val present=session.presentTeeth.toMutableSet()
        if(status==ToothStatus.MISSING_CARIES||status==ToothStatus.MISSING_OTHER)present.remove(selected) else present.add(selected)
        val cleared=if(status==ToothStatus.MISSING_CARIES||status==ToothStatus.MISSING_OTHER) session.odontogramSurfaces-selected else session.odontogramSurfaces
        onSessionChanged(session.copy(teeth=session.teeth+(selected to record.copy(status=status)),presentTeeth=present,odontogramSurfaces=cleared))
    }

    fun setSurface(surface:Surface) {
        if(surface !in applicableSurfaces) return
        val updated=surfaceMap.toMutableMap()
        if(selectedMark==SurfaceMark.HEALTHY) updated.remove(surface) else updated[surface]=selectedMark
        val derived=when {
            updated.values.any{it==SurfaceMark.CARIES}->ToothStatus.CARIES
            updated.values.any{it==SurfaceMark.RESTORATION}->ToothStatus.RESTORED
            updated.values.any{it==SurfaceMark.SEALANT}->ToothStatus.SEALANT
            else->ToothStatus.HEALTHY
        }
        onSessionChanged(session.copy(
            odontogramSurfaces=session.odontogramSurfaces+(selected to updated),
            teeth=session.teeth+(selected to record.copy(status=derived)),
            presentTeeth=session.presentTeeth+selected
        ))
    }

    val surfaceCounts=run {
        var d=0; var m=0; var f=0
        shown.forEach { tooth ->
            val r=session.teeth[tooth]?:ToothRecord()
            val surfaces=cpodSurfaces19(tooth)
            when(r.status) {
                ToothStatus.MISSING_CARIES -> m+=surfaces.size
                ToothStatus.MISSING_OTHER -> Unit
                else -> {
                    val marks=session.odontogramSurfaces[tooth]?:emptyMap()
                    surfaces.forEach { s ->
                        when(marks[s]) {
                            SurfaceMark.CARIES -> d++
                            SurfaceMark.RESTORATION -> f++
                            else -> Unit
                        }
                    }
                }
            }
        }
        Triple(d,m,f)
    }
    val surfaceTotal=surfaceCounts.first+surfaceCounts.second+surfaceCounts.third

    ResponsiveScreenV17(
        tr(lang,"CPOD / ceod · diente y superficie","DMFT / dmft · tooth and surface"),
        tr(lang,"Alterna entre el índice por diente (CPOD/ceod) y por superficie (CPOS/ceos).","Switch between tooth-level DMFT/dmft and surface-level DMFS/dmfs."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang,"Modo de registro","Recording mode")) {
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2) { i ->
                val surfaces=i==1
                FilterChip(surfaceMode==surfaces,{surfaceMode=surfaces},{Text(if(surfaces)tr(lang,"Por superficie · CPOS/ceos","By surface · DMFS/dmfs") else tr(lang,"Por diente · CPOD/ceod","By tooth · DMFT/dmft"))},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"Dentición","Dentition")) {
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2) { i ->
                val p=i==1
                FilterChip(primary==p,{primary=p},{Text(if(p)tr(lang,"Temporal","Primary") else tr(lang,"Permanente","Permanent"))},modifier=Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17(tr(lang,"Maxilar arriba · mandibular abajo","Maxillary above · mandibular below")) {
            DentalArchSelector(shown,selected,{selected=it}) { tooth ->
                if(surfaceMode) session.odontogramSurfaces[tooth]?.isNotEmpty()==true || session.teeth[tooth]?.status==ToothStatus.MISSING_CARIES
                else session.teeth[tooth]?.status?.let{it!=ToothStatus.HEALTHY}==true
            }
        }

        if(!surfaceMode) {
            ResponsiveSectionV17("OD $selected") {
                choices.forEach { s -> FilterChip(record.status==s,{setStatus(s)},{Text(cpodStatus19(s,lang))},modifier=Modifier.fillMaxWidth()) }
            }
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                    Text(if(primary)"ceod" else "CPOD",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                    Text(if(primary)"c = ${result.carious}   e = ${result.missing}   o = ${result.filled}" else "C = ${result.carious}   P = ${result.missing}   O = ${result.filled}")
                    Text("${if(primary)"ceod" else "CPOD"} = ${result.total}",fontWeight=FontWeight.Black,style=MaterialTheme.typography.headlineSmall)
                    Text(ClinicalEngines.cpodInterpretation(result.total,lang))
                }
            }
            NoticeCard(tr(lang,"La unidad es el diente. Caries activa tiene prioridad sobre una restauración para el conteo. Ausencias por causas distintas de caries no suman como P/e.","The unit is the tooth. Active caries takes priority over a restoration for counting. Missing teeth for causes other than caries do not count as M/e."))
        } else {
            ResponsiveSectionV17("OD $selected · ${if(primary)"ceos" else "CPOS"}") {
                if(record.status==ToothStatus.MISSING_CARIES) {
                    Text(tr(lang,"Diente ausente por caries: sus superficies se contabilizan en el componente P/e del índice por superficies.","Tooth missing due to caries: its surfaces are counted in the M/e component of the surface index."),fontWeight=FontWeight.Bold)
                    FilterChip(false,{setStatus(ToothStatus.HEALTHY)},{Text(tr(lang,"Marcar diente presente","Mark tooth present"))},modifier=Modifier.fillMaxWidth())
                } else {
                    SurfaceMark.entries.forEach { mark ->
                        FilterChip(selectedMark==mark,{selectedMark=mark},{Text(cpodSurfaceMark19(mark,lang))},modifier=Modifier.fillMaxWidth())
                    }
                    DentalSurfaceDiagram(
                        centerEnabled=applicableSurfaces.contains(Surface.OCCLUSAL),
                        surfaceColor={surface ->
                            when(surfaceMap[surface]) {
                                SurfaceMark.CARIES -> MaterialTheme.colorScheme.errorContainer
                                SurfaceMark.RESTORATION -> MaterialTheme.colorScheme.primaryContainer
                                SurfaceMark.SEALANT -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        },
                        onSurfaceTap={setSurface(it)},
                        modifier=Modifier.fillMaxWidth()
                    )
                    Text(tr(lang,
                        if(applicableSurfaces.size==5)"Este diente posterior tiene 5 superficies evaluables." else "Este diente anterior tiene 4 superficies evaluables.",
                        if(applicableSurfaces.size==5)"This posterior tooth has 5 evaluable surfaces." else "This anterior tooth has 4 evaluable surfaces."
                    ),fontWeight=FontWeight.SemiBold)
                    FilterChip(false,{setStatus(ToothStatus.MISSING_CARIES)},{Text(tr(lang,"Ausente por caries","Missing due to caries"))},modifier=Modifier.fillMaxWidth())
                }
            }
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                    Text(if(primary)"ceos" else "CPOS",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                    Text(if(primary)"c = ${surfaceCounts.first}   e = ${surfaceCounts.second}   o = ${surfaceCounts.third}" else "C = ${surfaceCounts.first}   P = ${surfaceCounts.second}   O = ${surfaceCounts.third}")
                    Text("${if(primary)"ceos" else "CPOS"} = $surfaceTotal",fontWeight=FontWeight.Black,style=MaterialTheme.typography.headlineSmall)
                }
            }
            NoticeCard(tr(lang,
                "En el registro por superficies, dientes anteriores usan 4 caras y posteriores 5. Una superficie con caries y restauración se contabiliza como cariada; selladores y ausencias por causas distintas de caries no suman al índice.",
                "In surface recording, anterior teeth use 4 surfaces and posterior teeth 5. A surface with both caries and restoration is counted as decayed; sealants and teeth missing for causes other than caries are excluded."
            ))
        }
    }
}
