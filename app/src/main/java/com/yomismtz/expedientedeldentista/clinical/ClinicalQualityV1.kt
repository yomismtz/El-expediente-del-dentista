package com.yomismtz.expedientedeldentista.clinical
data class ClinicalValidationIssue(val severity:String,val field:String,val messageEs:String,val messageEn:String)
enum class OdontogramCompletenessStatus {
    EMPTY,
    COMPLETE_PERMANENT,
    COMPLETE_PRIMARY,
    PARTIAL_PERMANENT,
    PARTIAL_PRIMARY,
    MIXED_DENTITION
}

data class OdontogramCompleteness(
    val status: OdontogramCompletenessStatus,
    val registeredPermanent: Int,
    val registeredPrimary: Int,
    val missingPermanent: Set<Int>,
    val missingPrimary: Set<Int>
)

private val PERMANENT_TEETH_V1 = setOf(
    11,12,13,14,15,16,17,18,21,22,23,24,25,26,27,28,
    31,32,33,34,35,36,37,38,41,42,43,44,45,46,47,48
)

private val PRIMARY_TEETH_V1 = setOf(
    51,52,53,54,55,61,62,63,64,65,71,72,73,74,75,81,82,83,84,85
)

/**
 * Detects whether the odontogram has enough explicit tooth records to be considered complete.
 * An unregistered tooth is never treated as healthy. Mixed dentition is reported separately
 * because it requires clinical/manual review rather than assuming all 20 or all 32 teeth.
 */
fun validateOdontogramCompleteness(session: EducationalSession): OdontogramCompleteness {
    val registered = session.teeth.keys
    val permanent = registered intersect PERMANENT_TEETH_V1
    val primary = registered intersect PRIMARY_TEETH_V1
    val hasPermanent = permanent.isNotEmpty()
    val hasPrimary = primary.isNotEmpty()
    val status = when {
        !hasPermanent && !hasPrimary -> OdontogramCompletenessStatus.EMPTY
        hasPermanent && hasPrimary -> OdontogramCompletenessStatus.MIXED_DENTITION
        hasPermanent && permanent.size == PERMANENT_TEETH_V1.size -> OdontogramCompletenessStatus.COMPLETE_PERMANENT
        hasPrimary && primary.size == PRIMARY_TEETH_V1.size -> OdontogramCompletenessStatus.COMPLETE_PRIMARY
        hasPermanent -> OdontogramCompletenessStatus.PARTIAL_PERMANENT
        else -> OdontogramCompletenessStatus.PARTIAL_PRIMARY
    }
    return OdontogramCompleteness(
        status = status,
        registeredPermanent = permanent.size,
        registeredPrimary = primary.size,
        missingPermanent = PERMANENT_TEETH_V1 - permanent,
        missingPrimary = PRIMARY_TEETH_V1 - primary
    )
}

object ClinicalQualityV1{
 fun validate(s:EducationalSession):List<ClinicalValidationIssue>{val p=s.profile;val o=mutableListOf<ClinicalValidationIssue>();val age=p.age.toIntOrNull();if(p.age.isNotBlank()&&(age==null||age !in 0..120))o+=ClinicalValidationIssue("ERROR","Edad","La edad debe estar entre 0 y 120 años.","Age must be between 0 and 120 years.");val spo=p.spo2.toIntOrNull();if(spo!=null&&spo !in 50..100)o+=ClinicalValidationIssue("ERROR","SpO₂","La SpO₂ registrada está fuera del rango permitido.","Recorded SpO₂ is outside the accepted range.");val hr=p.heartRate.toIntOrNull();if(hr!=null&&hr !in 20..250)o+=ClinicalValidationIssue("ERROR","Frecuencia cardiaca","La frecuencia cardiaca está fuera del rango permitido.","Heart rate is outside the accepted range.");val rr=p.respiratoryRate.toIntOrNull();if(rr!=null&&rr !in 5..80)o+=ClinicalValidationIssue("ERROR","Frecuencia respiratoria","La frecuencia respiratoria está fuera del rango permitido.","Respiratory rate is outside the accepted range.");val t=p.temperature.toDoubleOrNull();if(t!=null&&t !in 25.0..45.0)o+=ClinicalValidationIssue("ERROR","Temperatura","La temperatura está fuera del rango permitido.","Temperature is outside the accepted range.");if(p.bloodPressure.isNotBlank()){val x=p.bloodPressure.split("/");if(x.size!=2||x.any{it.toIntOrNull()==null})o+=ClinicalValidationIssue("ERROR","Presión arterial","La presión arterial debe registrarse como sistólica/diastólica.","Blood pressure must be entered as systolic/diastolic.")};if(s.history.asaClass !in 1..6)o+=ClinicalValidationIssue("ERROR","ASA","La clasificación ASA registrada no es válida.","Recorded ASA class is invalid.");return o.distinctBy{it.field+it.messageEs}}
 fun medicationIssues(s:EducationalSession):List<ClinicalValidationIssue>{val o=mutableListOf<ClinicalValidationIssue>();s.medicationsStructured.forEach{m->if(m.name.isBlank())o+=ClinicalValidationIssue("WARNING","Medicamentos","Hay un medicamento sin nombre.","A medication has no name.");if(m.active&&m.activeIngredient.isBlank())o+=ClinicalValidationIssue("WARNING",m.name.ifBlank{"Medicamento"},"Un medicamento activo no tiene principio activo registrado.","An active medication has no active ingredient recorded.");if(m.active&&m.route.isBlank())o+=ClinicalValidationIssue("WARNING",m.name.ifBlank{"Medicamento"},"Un medicamento activo no tiene vía registrada.","An active medication has no route recorded.")};s.medicationsStructured.filter{it.active}.groupBy{it.activeIngredient.trim().lowercase()}.filterKeys{it.isNotBlank()}.filterValues{it.size>1}.forEach{o+=ClinicalValidationIssue("WARNING","Medicamentos","Hay medicamentos activos con el mismo principio activo; revisar si son duplicados.","Active medications share the same active ingredient; review for possible duplicates.")};return o.distinctBy{it.field+it.messageEs}}
 fun photoIssues(s:EducationalSession)=s.clinicalPhotos.values.filter{it.uri.isBlank()}.map{ClinicalValidationIssue("WARNING",it.slot,"Existe un registro fotográfico sin archivo asociado.","A photographic record has no associated file.")}
}