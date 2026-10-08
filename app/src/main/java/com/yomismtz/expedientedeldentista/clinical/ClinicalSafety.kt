package com.yomismtz.expedientedeldentista.clinical

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

data class ClinicalAlert(
    val severity: Severity,
    val titleEs: String,
    val titleEn: String,
    val detailEs: String,
    val detailEn: String
) {
    enum class Severity { INFO, WARNING, URGENT }
    fun title(lang:String)=if(lang=="en")titleEn else titleEs
    fun detail(lang:String)=if(lang=="en")detailEn else detailEs
}

enum class BirthDateStatus { EMPTY, VALID, INVALID_FORMAT, INVALID_DATE, FUTURE }

fun validateBirthDate(value:String, today:LocalDate=LocalDate.now()):BirthDateStatus {
    val text=value.trim()
    if(text.isBlank()) return BirthDateStatus.EMPTY
    val normalized = text
    val pattern = when {
        Regex("""^\d{4}-\d{2}-\d{2}$""").matches(text) -> DateTimeFormatter.ISO_LOCAL_DATE
        Regex("""^\d{1,2}/\d{1,2}/\d{4}$""").matches(normalized) -> DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT)
        else -> return BirthDateStatus.INVALID_FORMAT
    }
    val date=runCatching { LocalDate.parse(text,pattern) }.getOrElse { return BirthDateStatus.INVALID_DATE }
    if(date.isAfter(today)) return BirthDateStatus.FUTURE
    return BirthDateStatus.VALID
}


/** Calculates completed years from a validated date of birth. Returns null when the date is not valid. */
fun calculateAgeYears(value:String, today:LocalDate=LocalDate.now()):Int? {
    if (validateBirthDate(value,today) != BirthDateStatus.VALID) return null
    val normalized=value.trim()
    val date=if (normalized.matches(Regex("""^\\d{4}-\\d{2}-\\d{2}$"""))) {
        LocalDate.parse(normalized,DateTimeFormatter.ISO_LOCAL_DATE)
    } else {
        LocalDate.parse(normalized,DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT))
    }
    return java.time.Period.between(date,today).years
}

enum class AgeConsistencyStatus { EMPTY_BIRTH_DATE, INVALID_BIRTH_DATE, EMPTY_RECORDED_AGE, VALID, MISMATCH, INVALID_RECORDED_AGE }

fun validateAgeConsistency(birthDate:String, recordedAge:String, today:LocalDate=LocalDate.now()):AgeConsistencyStatus {
    val birthStatus=validateBirthDate(birthDate,today)
    if (birthStatus == BirthDateStatus.EMPTY) return AgeConsistencyStatus.EMPTY_BIRTH_DATE
    if (birthStatus != BirthDateStatus.VALID) return AgeConsistencyStatus.INVALID_BIRTH_DATE
    if (recordedAge.isBlank()) return AgeConsistencyStatus.EMPTY_RECORDED_AGE
    val age=recordedAge.trim().toIntOrNull() ?: return AgeConsistencyStatus.INVALID_RECORDED_AGE
    if (age !in 0..120) return AgeConsistencyStatus.INVALID_RECORDED_AGE
    return if (calculateAgeYears(birthDate,today)==age) AgeConsistencyStatus.VALID else AgeConsistencyStatus.MISMATCH
}

enum class SexRecordStatus { EMPTY, RECORDED }
fun validateRecordedSex(value:String):SexRecordStatus = if (value.trim().isBlank()) SexRecordStatus.EMPTY else SexRecordStatus.RECORDED

object ClinicalSafetyEngine {
    fun alerts(session: EducationalSession): List<ClinicalAlert> {
        val p=session.profile
        val out=mutableListOf<ClinicalAlert>()
        when(validateAgeConsistency(p.birthDate,p.age)) {
            AgeConsistencyStatus.MISMATCH -> out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"Edad no coincide con la fecha de nacimiento","Age does not match date of birth","La edad registrada no corresponde a los años cumplidos según la fecha de nacimiento; corregir el dato antes de usarlo clínicamente.","The recorded age does not match completed years from the date of birth; correct it before clinical use.")
            AgeConsistencyStatus.INVALID_RECORDED_AGE -> out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"Edad registrada inválida","Invalid recorded age","La edad debe ser un número entero entre 0 y 120 años.","Age must be a whole number between 0 and 120 years.")
            AgeConsistencyStatus.EMPTY_RECORDED_AGE -> out += ClinicalAlert(ClinicalAlert.Severity.INFO,"Edad calculable pendiente de registrar","Calculated age pending","Existe una fecha de nacimiento válida, pero falta la edad calculada/registrada.","A valid date of birth exists, but the calculated/recorded age is missing.")
            else -> Unit
        }
        if (validateRecordedSex(p.sex) == SexRecordStatus.EMPTY) {
            out += ClinicalAlert(ClinicalAlert.Severity.INFO,"Sexo registrado pendiente","Recorded sex pending","Cuando este dato sea clínicamente necesario, debe registrarse según lo referido y sin inferirlo por apariencia.","When clinically necessary, record this information as reported; do not infer it from appearance.")
        }
        when(validateBirthDate(p.birthDate)) {
            BirthDateStatus.INVALID_FORMAT -> out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"Fecha de nacimiento inválida","Invalid date of birth","Revisar el formato. Usa AAAA-MM-DD o DD/MM/AAAA y una fecha de calendario válida.","Review the format. Use YYYY-MM-DD or DD/MM/YYYY and a valid calendar date.")
            BirthDateStatus.INVALID_DATE -> out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"Fecha de nacimiento inválida","Invalid date of birth","La fecha no corresponde a un día de calendario válido.","The date is not a valid calendar date.")
            BirthDateStatus.FUTURE -> out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"Fecha de nacimiento futura","Future date of birth","La fecha de nacimiento no puede ser posterior a la fecha actual.","Date of birth cannot be later than today.")
            else -> Unit
        }
        val bp=Regex("""(\d{2,3})\s*/\s*(\d{2,3})""").find(p.bloodPressure)
        val sys=bp?.groupValues?.getOrNull(1)?.toIntOrNull()
        val dia=bp?.groupValues?.getOrNull(2)?.toIntOrNull()
        if(sys!=null && dia!=null) when {
            sys>=180 || dia>=120 -> out += ClinicalAlert(ClinicalAlert.Severity.URGENT,"TA muy elevada registrada","Very high BP recorded","Confirmar la medición y solicitar valoración urgente según el contexto.","Confirm the measurement and obtain urgent clinical assessment as appropriate.")
            sys>=140 || dia>=90 -> out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"TA elevada registrada","Elevated BP recorded","Repetir/confirmar con técnica adecuada y considerar el contexto antes de procedimientos electivos.","Repeat/confirm with proper technique and consider context before elective procedures.")
        }
        val spo2=p.spo2.toIntOrNull()
        if(spo2!=null) when {
            spo2<90 -> out += ClinicalAlert(ClinicalAlert.Severity.URGENT,"SpO₂ baja registrada","Low SpO₂ recorded","Confirmar la medición y solicitar valoración urgente si persiste o hay síntomas.","Confirm the measurement and obtain urgent assessment if persistent or symptomatic.")
            spo2<95 -> out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"SpO₂ por debajo del rango habitual","SpO₂ below usual range","Repetir y contextualizar el resultado antes de procedimientos electivos.","Repeat and contextualize before elective procedures.")
        }
        if(p.allergies.isNotBlank()) out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"Alergia registrada","Allergy recorded","Verificar el agente, tipo de reacción y relevancia para medicamentos/materiales/procedimientos antes de atender.","Verify agent, reaction and relevance to medications, materials and procedures before care.")
        if(p.medications.isNotBlank()) out += ClinicalAlert(ClinicalAlert.Severity.INFO,"Medicamentos registrados","Medications recorded","Revisar medicamentos actuales, dosis y posibles implicaciones para el procedimiento antes de actuar.","Review current medications, doses and possible procedural implications before acting.")
        val systemic=session.history.diseases.filterValues{it.present}.keys
        if(systemic.isNotEmpty() || session.history.asaClass>1) out += ClinicalAlert(ClinicalAlert.Severity.WARNING,"Antecedente sistémico relevante","Relevant systemic history","Revisar enfermedad, control actual, ASA e interconsultas/protocolos aplicables antes del procedimiento.","Review disease, current control, ASA status and applicable consultation/protocols before the procedure.")
        val missingDocumentation=mutableListOf<String>()
        if(p.patientInitials.isBlank() || p.age.isBlank() || p.sex.isBlank()) missingDocumentation += "identificación"
        if(p.reasonForVisit.isBlank()) missingDocumentation += "motivo de consulta"
        if(p.allergies.isBlank()) missingDocumentation += "alergias"
        if(p.medications.isBlank()) missingDocumentation += "medicamentos"
        if(missingDocumentation.isNotEmpty()) out += ClinicalAlert(ClinicalAlert.Severity.INFO,"Documentación pendiente","Documentation pending","Faltan: ${missingDocumentation.joinToString(", ")}.","Missing: ${missingDocumentation.joinToString(", ")}.")
        return out
    }
}
