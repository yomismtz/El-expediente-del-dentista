package com.yomismtz.expedientedeldentista.clinical

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

object ClinicalSafetyEngine {
    fun alerts(session: EducationalSession): List<ClinicalAlert> {
        val p=session.profile
        val out=mutableListOf<ClinicalAlert>()
        val bp=Regex("""(\\d{2,3})\\s*/\\s*(\\d{2,3})""").find(p.bloodPressure)
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
