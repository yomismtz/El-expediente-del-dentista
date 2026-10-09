package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.AgeConsistencyStatus
import com.yomismtz.expedientedeldentista.clinical.BirthDateStatus
import com.yomismtz.expedientedeldentista.clinical.SexRecordStatus
import com.yomismtz.expedientedeldentista.clinical.validateAgeConsistency
import com.yomismtz.expedientedeldentista.clinical.validateBirthDate
import com.yomismtz.expedientedeldentista.clinical.validateRecordedSex
import com.yomismtz.expedientedeldentista.clinical.validateOdontogramCompleteness
import com.yomismtz.expedientedeldentista.clinical.OdontogramCompletenessStatus
import com.yomismtz.expedientedeldentista.clinical.AsaRecordStatus
import com.yomismtz.expedientedeldentista.clinical.validateClinicianAsaClass

private data class CompletionItemV1(val title:String,val done:Boolean)
private data class CompletionResultV1(val items:List<CompletionItemV1>,val warnings:List<String>)

private fun completionResultV1(
 session:EducationalSession, hereditaryNone:Boolean, hereditaryPositive:Boolean, nonPathStatus:Map<String,String>,
 habitsNone:Boolean, habitsPositive:Boolean, mucosaStatus:Map<String,String>, pathNone:Boolean,
 medicationsNone:Boolean, allergiesNone:Boolean
):CompletionResultV1{
 val diseases=session.history.diseases
 val allergyIds=setOf("drug_allergy","latex_allergy","food_allergy","allergic_rhinitis","urticaria","atopic_dermatitis","anaphylaxis","contact_dermatitis")
 val allergyById=diseases.any{(id,a)->a.present && id in allergyIds}
 val nonPathSections=listOf("Alimentación","Vivienda","Higiene","Inmunizaciones","Hábitos y exposiciones")
 val nonPathDone=nonPathSections.all{nonPathStatus[it].orEmpty().isNotBlank()}
 val mucosaSites=listOf("Labio superior","Labio inferior","Carrillo derecho / mucosa bucal","Carrillo izquierdo / mucosa bucal","Piso de boca","Paladar duro","Paladar blando","Orofaringe / pared posterior","Úvula","Amígdala derecha","Amígdala izquierda","Lengua · dorso","Lengua · bordes laterales","Lengua · cara ventral")
 val mucosaDone=mucosaSites.all{mucosaStatus[it].orEmpty().isNotBlank()}
 val birthDateOk=validateBirthDate(session.profile.birthDate)==BirthDateStatus.VALID
 val ageOk=validateAgeConsistency(session.profile.birthDate,session.profile.age)==AgeConsistencyStatus.VALID
 val sexOk=validateRecordedSex(session.profile.sex)==SexRecordStatus.RECORDED
 val identification=session.profile.patientInitials.isNotBlank() && birthDateOk && ageOk && sexOk
 val reason=session.profile.reasonForVisit.isNotBlank()
 val appDone=pathNone || diseases.values.any{it.present} || session.history.tobaccoAlcohol.isNotBlank()
 val medsDone=medicationsNone || session.medicationsStructured.isNotEmpty()
 val allergiesDone=allergiesNone || allergyById || session.profile.allergies.isNotBlank()
 val vitalsDone=session.clinicalMeasurements.isNotEmpty() || listOf(session.profile.bloodPressure,session.profile.heartRate,session.profile.temperature,session.profile.spo2).any{it.isNotBlank()}
 val odontogram=validateOdontogramCompleteness(session)
 val odontogramDone=odontogram.status==OdontogramCompletenessStatus.COMPLETE_PERMANENT || odontogram.status==OdontogramCompletenessStatus.COMPLETE_PRIMARY
 val items=listOf(
  CompletionItemV1("Identificación",identification), CompletionItemV1("Motivo de consulta",reason),
  CompletionItemV1("Heredo-familiares",hereditaryNone || hereditaryPositive), CompletionItemV1("Antecedentes patológicos",appDone),
  CompletionItemV1("No patológicos",nonPathDone), CompletionItemV1("Hábitos / parafunciones",habitsNone || habitsPositive),
  CompletionItemV1("Medicamentos",medsDone), CompletionItemV1("Alergias",allergiesDone),
  CompletionItemV1("Signos vitales",vitalsDone), CompletionItemV1("Examen de mucosas",mucosaDone), CompletionItemV1("Odontograma",odontogramDone),
  CompletionItemV1("Clasificación ASA clínica",validateClinicianAsaClass(session.history.asaClassClinician)==AsaRecordStatus.VALID)
 )
 val warnings=buildList{
  if(session.history.asaClass>1 && diseases.values.none{it.present} && !pathNone) add("ASA > I sin antecedentes patológicos registrados; revisar congruencia.")
  if(allergiesNone && allergyById) add("Alergias marcadas como negadas y, al mismo tiempo, existe una alergia registrada.")
  if(medicationsNone && session.medicationsStructured.isNotEmpty()) add("Medicamentos marcados como ninguno y, al mismo tiempo, hay medicamentos registrados.")
  when(odontogram.status){
   OdontogramCompletenessStatus.PARTIAL_PERMANENT -> add("Odontograma permanente incompleto: faltan ${odontogram.missingPermanent.size} dientes por registrar explícitamente.")
   OdontogramCompletenessStatus.PARTIAL_PRIMARY -> add("Odontograma temporal incompleto: faltan ${odontogram.missingPrimary.size} dientes por registrar explícitamente.")
   OdontogramCompletenessStatus.MIXED_DENTITION -> add("Odontograma con dentición mixta: requiere revisión clínica/manual; no se declara completo automáticamente.")
   OdontogramCompletenessStatus.EMPTY -> add("Odontograma sin dientes registrados explícitamente.")
   else -> Unit
  }
 }
 return CompletionResultV1(items,warnings)
}

@Composable
fun ClinicalCompletenessCardV1(lang:String,session:EducationalSession){
 val hereditaryNone by rememberRecordState("history.hereditary.none",false)
 val hereditarySelected=rememberRecordStateMap<String,Boolean>("history.hereditary.selected")
 val hereditaryPositive=hereditarySelected.values.any{it}
 val nonPathStatus=rememberRecordStateMap<String,String>("history.nonpath.sectionStatus")
 val habitsNone by rememberRecordState("history.habits.noneDenied",false)
 val habitsPresent=rememberRecordStateMap<String,Boolean>("history.habits.present")
 val habitsPositive=habitsPresent.values.any{it}
 val mucosaStatus=rememberRecordStateMap<String,String>("history.oralExam.siteStatus")
 val pathNone by rememberRecordState("history.path.noneDenied",false)
 val medicationsNone by rememberRecordState("history.medications.noneDenied",false)
 val allergiesNone by rememberRecordState("history.path.allergies.noneDenied",false)
 val result=completionResultV1(session,hereditaryNone,hereditaryPositive,nonPathStatus,habitsNone,habitsPositive,mucosaStatus,pathNone,medicationsNone,allergiesNone)
 val done=result.items.count{it.done}; val total=result.items.size
 val pending=result.items.filterNot{it.done}.map{it.title}; val complete=done==total
 Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(complete)MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer)){
  Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){
   Text(if(complete)"🟢 Expediente de captura completo" else "🟡 Expediente de captura en progreso",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
   Text("${done}/${total} apartados del núcleo de historia clínica registrados.")
   Text("Estado por apartado",fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleSmall)
   result.items.forEach { item ->
    Text(
     "${if(item.done) "✓" else "○"} ${item.title}: ${if(item.done) "registrado" else "pendiente"}",
     style=MaterialTheme.typography.bodySmall,
     color=if(item.done) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
    )
   }
   if(pending.isNotEmpty()) Text("Acción requerida: completar ${pending.joinToString(" · ")}.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.error)
   if(result.warnings.isNotEmpty()){
    Text("⚠ Revisar congruencia",fontWeight=FontWeight.Bold)
    result.warnings.forEach{Text("• ${it}",style=MaterialTheme.typography.bodySmall)}
   }else Text("✓ Sin contradicciones básicas detectadas.",style=MaterialTheme.typography.bodySmall)
  }
 }
}

@Composable
fun ClinicalCompletenessMiniV1(session:EducationalSession){
 val hereditaryNone by rememberRecordState("history.hereditary.none",false)
 val hereditarySelected=rememberRecordStateMap<String,Boolean>("history.hereditary.selected")
 val hereditaryPositive=hereditarySelected.values.any{it}
 val nonPathStatus=rememberRecordStateMap<String,String>("history.nonpath.sectionStatus")
 val habitsNone by rememberRecordState("history.habits.noneDenied",false)
 val habitsPresent=rememberRecordStateMap<String,Boolean>("history.habits.present")
 val habitsPositive=habitsPresent.values.any{it}
 val mucosaStatus=rememberRecordStateMap<String,String>("history.oralExam.siteStatus")
 val pathNone by rememberRecordState("history.path.noneDenied",false)
 val medicationsNone by rememberRecordState("history.medications.noneDenied",false)
 val allergiesNone by rememberRecordState("history.path.allergies.noneDenied",false)
 val result=completionResultV1(session,hereditaryNone,hereditaryPositive,nonPathStatus,habitsNone,habitsPositive,mucosaStatus,pathNone,medicationsNone,allergiesNone)
 val done=result.items.count{it.done}; val total=result.items.size
 Text(if(result.warnings.isNotEmpty())"⚠ ${done}/${total} · revisar congruencia" else "✓ ${done}/${total} · captura clínica",style=MaterialTheme.typography.bodySmall,fontWeight=FontWeight.Bold,color=if(result.warnings.isNotEmpty())MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
}
