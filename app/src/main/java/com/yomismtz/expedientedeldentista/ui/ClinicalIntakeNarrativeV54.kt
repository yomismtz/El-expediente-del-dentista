package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

private data class IntakeNoteSectionV54(val title:String,val text:String)

@Composable
fun ClinicalIntakeNarrativeV54(lang:String,session:EducationalSession,modifier:Modifier=Modifier) {
    val mucosaStatus=rememberRecordStateMap<String,String>("mucosa.tissueStatus")
    val mucosaLesion=rememberRecordStateMap<String,String>("mucosa.tissueLesion")
    val mucosaPathology=rememberRecordStateMap<String,String>("mucosa.tissuePathology")
    val atmChecked=rememberRecordStateMap<String,Boolean>("atm.checked")
    val atmOpening=rememberRecordState("atm.opening","No medida")
    val atmTrajectory=rememberRecordState("atm.trajectory","Recta / sin desviación evidente")
    val atmJoint=rememberRecordState("atm.jointPalpation","Sin dolor reproducible")
    val atmMuscle=rememberRecordState("atm.musclePalpation","Sin dolor reproducible")
    val occlusionChoice=rememberRecordState("occlusion.choice","")
    val upperPresent=rememberRecordState("prosthetic.v2.upperPresent",setOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27))
    val lowerPresent=rememberRecordState("prosthetic.v2.lowerPresent",setOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37))
    val rpdMaterial=rememberRecordState("prosthetic.v2.rpdMaterial","metal-acrylic")
    val fixedMaterial=rememberRecordState("prosthetic.v2.fixedMaterial","zirconia")
    val surgicalProcedure=rememberRecordState("surgical.procedure","Exodoncia simple")
    val surgicalSite=rememberRecordState("surgical.site","Órgano dentario seleccionado")
    val surgicalFinding=rememberRecordState("surgical.finding","Sin hallazgos inesperados")
    val sections=remember(session,mucosaStatus.toMap(),mucosaLesion.toMap(),mucosaPathology.toMap(),atmChecked.toMap(),atmOpening,atmTrajectory,atmJoint,atmMuscle,occlusionChoice,upperPresent,lowerPresent,rpdMaterial,fixedMaterial,surgicalProcedure,surgicalSite,surgicalFinding,lang) {
        buildIntakeSectionsV54(lang,session,mucosaStatus,mucosaLesion,mucosaPathology,atmChecked,atmOpening,atmTrajectory,atmJoint,atmMuscle,occlusionChoice,upperPresent,lowerPresent,rpdMaterial,fixedMaterial,surgicalProcedure,surgicalSite,surgicalFinding)
    }
    Card(modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer),border=BorderStroke(1.dp,MaterialTheme.colorScheme.primary.copy(alpha=.35f))) {
        Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
                Text("🧾 "+tr54(lang,"Nota de ingreso","Intake note"),fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                Text(tr54(lang,"En vivo","Live"),fontWeight=FontWeight.Bold)
            }
            Text(tr54(lang,"Se construye automáticamente con la información registrada en las pantallas clínicas.","Built automatically from information recorded in the clinical screens."),style=MaterialTheme.typography.bodySmall)
            sections.forEach { s ->
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(11.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                        Text(s.title,fontWeight=FontWeight.Black)
                        Text(s.text)
                    }
                }
            }
        }
    }
}

private fun buildIntakeSectionsV54(lang:String,s:EducationalSession,ms:Map<String,String>,ml:Map<String,String>,mp:Map<String,String>,atm:Map<String,Boolean>,atmOpening:String,atmTrajectory:String,atmJoint:String,atmMuscle:String,occlusion:String,upperPresent:Set<Int>,lowerPresent:Set<Int>,rpdMaterial:String,fixedMaterial:String,surgicalProcedure:String,surgicalSite:String,surgicalFinding:String):List<IntakeNoteSectionV54> {
    val p=s.profile
    val sex=p.sex.trim()
    val patient=when {
        sex.equals("masculino",true)||sex.equals("male",true)||sex.equals("m",true) -> "Paciente masculino"
        sex.equals("femenino",true)||sex.equals("female",true)||sex.equals("f",true) -> "Paciente femenino"
        else -> "Paciente"
    }
    val history=s.history.diseases.filterValues{it.present}.keys.map{it.replace('_',' ')}
    val historyText=if(history.isEmpty()) tr54(lang,"Sin antecedentes patológicos positivos registrados.","No positive medical history recorded.") else tr54(lang,"Antecedentes registrados: ","Recorded history: ")+history.joinToString(", ")+"."
    val allergies=p.allergies.ifBlank{tr54(lang,"No registradas.","Not recorded.")}
    val meds=p.medications.ifBlank{tr54(lang,"No registrados.","Not recorded.")}

    val vitals=listOf(
        if(p.bloodPressure.isNotBlank()) "TA "+p.bloodPressure else "",
        if(p.heartRate.isNotBlank()) "FC "+p.heartRate else "",
        if(p.respiratoryRate.isNotBlank()) "FR "+p.respiratoryRate else "",
        if(p.temperature.isNotBlank()) "T "+p.temperature else ""
    ).filter{it.isNotBlank()}.joinToString(" · ").ifBlank{tr54(lang,"Sin signos vitales registrados.","No vital signs recorded.")}

    val mucosa=mutableListOf<String>()
    (ms.keys+ml.keys+mp.keys).distinct().forEach { k ->
        val f=listOf(ms[k],ml[k],mp[k]).filterNotNull().map{it.trim()}.filter{it.isNotBlank()&&!it.equals("Normal",true)&&!it.equals("Normal.",true)}.distinct()
        if(f.isNotEmpty()) mucosa.add(humanize54(k)+": "+f.joinToString(", "))
    }
    val mucosaText=if(mucosa.isEmpty()) tr54(lang,"Sin hallazgos de mucosa registrados.","No mucosal findings recorded.") else patient+" presenta "+mucosa.joinToString("; ")+"."

    val caries=s.teeth.filterValues{it.status==ToothStatus.CARIES||it.status==ToothStatus.MISSING_CARIES||it.icdas>0}.keys.toMutableSet()
    s.odontogramSurfaces.forEach { (tooth,marks)->if(marks.values.any{it==SurfaceMark.CARIES}) caries.add(tooth) }
    val restored=s.teeth.filterValues{it.status==ToothStatus.RESTORED}.keys.sorted()
    val sealants=s.teeth.filterValues{it.status==ToothStatus.SEALANT}.keys.sorted()
    val extraction=s.teeth.filterValues{it.status==ToothStatus.EXTRACTION_INDICATED}.keys.sorted()
    val cariesText=buildString {
        if(caries.isNotEmpty()) append("Lesiones de caries registradas en OD "+caries.sorted().joinToString(", "))
        if(restored.isNotEmpty()) append(if(isNotEmpty()) "; " else "").append("restauraciones en OD "+restored.joinToString(", "))
        if(sealants.isNotEmpty()) append(if(isNotEmpty()) "; " else "").append("selladores en OD "+sealants.joinToString(", "))
        if(extraction.isNotEmpty()) append(if(isNotEmpty()) "; " else "").append("extracción indicada en OD "+extraction.joinToString(", "))
        if(isEmpty()) append(tr54(lang,"Sin hallazgos cariológicos registrados.","No cariological findings recorded."))
    }

    val perio=s.periodontogram
    val bleeding=perio.filterValues{it.bleeding||it.bleedingSites.isNotEmpty()}.keys.sorted()
    val plaque=perio.filterValues{it.plaque||it.plaqueSites.isNotEmpty()}.keys.sorted()
    val mobility=perio.filterValues{it.mobility>0}.keys.sorted()
    val perioText=if(perio.isEmpty()) tr54(lang,"Sin diagnóstico periodontal registrado.","No periodontal diagnosis recorded.") else buildString {
        append("Periodontograma registrado")
        if(bleeding.isNotEmpty()) append("; sangrado en OD "+bleeding.joinToString(", "))
        if(plaque.isNotEmpty()) append("; placa en OD "+plaque.joinToString(", "))
        if(mobility.isNotEmpty()) append("; movilidad en OD "+mobility.joinToString(", "))
        append(".")
    }

    val pu=s.pulpal
    val pulpalText=if(pu.tooth<=0) tr54(lang,"Sin valoración pulpar/periapical registrada.","No pulpal/periapical assessment recorded.") else {
        val f=mutableListOf<String>()
        if(pu.spontaneousPain) f.add("dolor espontáneo")
        if(pu.nightPain) f.add("dolor nocturno")
        if(pu.coldPositive) f.add("respuesta positiva al frío")
        if(pu.coldLingering) f.add("dolor persistente al frío")
        if(pu.heatPositive) f.add("respuesta al calor")
        if(pu.percussionPain) f.add("dolor a percusión")
        if(pu.palpationPain) f.add("dolor a palpación")
        if(pu.swelling) f.add("edema")
        if(pu.fistula) f.add("fístula")
        if(pu.apicalRadiolucency) f.add("radiolucidez apical")
        "OD "+pu.tooth+": "+if(f.isEmpty()) "valoración registrada sin hallazgos positivos seleccionados." else f.joinToString(", ")+"."
    }


    val atmPositive=atm.filterValues{it}.keys.joinToString(", ")
    val atmText=if(atmPositive.isBlank() && atmOpening=="No medida" && atmJoint=="Sin dolor reproducible" && atmMuscle=="Sin dolor reproducible") "Exploración ATM registrada sin hallazgos positivos seleccionados; apertura: "+atmOpening+"; trayectoria: "+atmTrajectory+"." else "Apertura: "+atmOpening+"; trayectoria: "+atmTrajectory+"; palpación articular: "+atmJoint+"; palpación muscular: "+atmMuscle+"; hallazgos seleccionados: "+atmPositive.ifBlank{"ninguno"}+"."
    val occlusionText=if(occlusion.isBlank()) "Sin hallazgo oclusal seleccionado." else "Hallazgo oclusal seleccionado: "+occlusion+"."
    val upperMissing=listOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27).filter{!upperPresent.contains(it)}
    val lowerMissing=listOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37).filter{!lowerPresent.contains(it)}
    val prostheticText="Arcada superior: "+(if(upperMissing.isEmpty()) "sin ausencias registradas" else "ausencias en OD "+upperMissing.joinToString(", "))+"; arcada inferior: "+(if(lowerMissing.isEmpty()) "sin ausencias registradas" else "ausencias en OD "+lowerMissing.joinToString(", "))+"; material removible: "+rpdMaterial+"; material fijo: "+fixedMaterial+"."
    val surgicalText="Procedimiento: "+surgicalProcedure+"; sitio: "+surgicalSite+"; hallazgo: "+surgicalFinding+"."
    return listOf(
        IntakeNoteSectionV54(tr54(lang,"Identificación y motivo","Identification and reason"),patient+(if(p.age.isNotBlank()) ", "+p.age+" años" else "")+". "+tr54(lang,"Motivo de consulta: ","Reason for consultation: ")+p.reasonForVisit.ifBlank{tr54(lang,"no registrado.","not recorded.")}),
        IntakeNoteSectionV54("ASA y antecedentes médicos","ASA "+s.history.asaClass+". "+historyText),
        IntakeNoteSectionV54(tr54(lang,"Alergias","Allergies"),allergies),
        IntakeNoteSectionV54(tr54(lang,"Medicamentos","Medications"),meds),
        IntakeNoteSectionV54(tr54(lang,"Antecedentes cardiovasculares","Cardiovascular history"),cardio54(history)),
        IntakeNoteSectionV54(tr54(lang,"Signos vitales","Vital signs"),vitals),
        IntakeNoteSectionV54(tr54(lang,"Exploración de mucosas","Mucosal examination"),mucosaText),
        IntakeNoteSectionV54(tr54(lang,"Exploración de ATM","TMJ examination"),atmText),
        IntakeNoteSectionV54(tr54(lang,"Oclusión","Occlusion"),occlusionText),
        IntakeNoteSectionV54(tr54(lang,"Cariología","Cariology"),cariesText),
        IntakeNoteSectionV54(tr54(lang,"Diagnóstico periodontal","Periodontal diagnosis"),perioText),
        IntakeNoteSectionV54(tr54(lang,"Diagnóstico pulpar / periapical","Pulpal / periapical diagnosis"),pulpalText),
        IntakeNoteSectionV54(tr54(lang,"Diagnóstico protésico","Prosthetic diagnosis"),prostheticText),
        IntakeNoteSectionV54(tr54(lang,"Diagnóstico de cirugía","Surgical diagnosis"),surgicalText)
    )
}
private fun cardio54(h:List<String>):String {
    val x=h.filter{v->listOf("cardio","hipert","presion","infarto","angina","arrit","coron","insuficiencia cardi").any{v.lowercase().contains(it)}}
    return if(x.isEmpty()) "Sin antecedente cardiovascular positivo identificado en los datos registrados." else "Antecedentes cardiovasculares registrados: "+x.joinToString(", ")+"."
}
private fun humanize54(k:String)=k.replace('_',' ').replace('-',' ').trim().replaceFirstChar{it.uppercase()}
private fun tr54(lang:String,es:String,en:String)=if(lang=="en") en else es
