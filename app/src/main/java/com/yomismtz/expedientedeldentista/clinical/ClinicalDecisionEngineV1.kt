package com.yomismtz.expedientedeldentista.clinical

data class ClinicalAlertV1(
    val severity: String,
    val triggerEs: String,
    val actionEs: String,
    val triggerEn: String,
    val actionEn: String
)

data class ClinicalDecisionV1(
    val status: String,
    val titleEs: String,
    val titleEn: String,
    val detailEs: String,
    val detailEn: String,
    val treatments: List<String>,
    val restrictionsEs: List<String>,
    val restrictionsEn: List<String>,
    val alerts: List<ClinicalAlertV1>,
    val anestheticEs: String,
    val anestheticEn: String
)

fun evaluateClinicalDecisionV1(
    age: Int,
    sex: String,
    sys: Int?,
    dia: Int?,
    rr: Int?,
    hr: Int?,
    spo2: Int?,
    glucose: Int?,
    temp: Double?,
    bmi: Double?,
    signs: Set<String>,
    pain: Int?,
    profile: PatientProfile
): ClinicalDecisionV1 {
    val emergency = setOf("Sangrado oral no controlable","Inflamación facial/cervical","Disnea","Dolor torácico","Alteración de conciencia","Convulsiones")
    val infection = "Fiebre/malestar" in signs && ("Inflamación intraoral" in signs || "Supuración/fístula" in signs)
    val cardiovascularHistory = profile.cardiovascularHistory.trim().lowercase()
    val antithrombotic = profile.anticoagulantsAntiplatelets.trim().lowercase()
    val rhythmMeds = profile.betaBlockersAntiarrhythmics.trim().lowercase()
    val allergy = profile.allergies.trim().lowercase()
    val systemicCaution = profile.hepaticRenalDisease.isNotBlank() || profile.diabetesTreatment.isNotBlank() || profile.pregnancyStatus.isNotBlank()

    val red = emergency.any { it in signs } ||
        (spo2 != null && spo2 < 90) ||
        (glucose != null && glucose < 70) ||
        (age >= 18 && sys != null && dia != null && (sys > 180 || dia > 110)) ||
        (temp != null && temp <= 35.0) ||
        (temp != null && temp >= 40.0)

    val yellow = !red && (
        infection ||
        (spo2 != null && spo2 in 90..94) ||
        (glucose != null && glucose >= 300) ||
        (age >= 18 && sys != null && dia != null && (sys >= 160 || dia >= 100)) ||
        (rr != null && (rr < 12 || rr > 20) && age >= 13) ||
        (hr != null && (hr < 50 || hr > 120) && age >= 13) ||
        (temp != null && temp >= 38.0) ||
        "Úlcera o lesión >2 semanas" in signs ||
        "Mancha blanca/roja persistente" in signs ||
        antithrombotic.isNotBlank() ||
        rhythmMeds.isNotBlank() ||
        cardiovascularHistory.isNotBlank() ||
        systemicCaution
    )

    val status = if (red) "RED" else if (yellow) "YELLOW" else "GREEN"
    val titleEs = when(status) {
        "RED" -> "🚨 ROJO · posponer atención electiva"
        "YELLOW" -> "🟡 AMARILLO · precaución y verificación"
        else -> "🟢 VERDE · sin alerta sistémica mayor registrada"
    }
    val titleEn = when(status) {
        "RED" -> "🚨 RED · defer elective care"
        "YELLOW" -> "🟡 YELLOW · caution and verification"
        else -> "🟢 GREEN · no major systemic alert recorded"
    }

    val detailEs = when(status) {
        "RED" -> "Existe al menos un dato de alarma. Repetir/confirmar la medición cuando corresponda, estabilizar dentro del alcance profesional y activar el protocolo de urgencia o referencia indicado."
        "YELLOW" -> "Hay uno o más factores que modifican el manejo. Confirmar datos, revisar antecedentes/medicamentos y ajustar o diferir el procedimiento según diagnóstico, complejidad y recursos."
        else -> "Los datos registrados no muestran una alarma sistémica mayor en esta pantalla. Esto no equivale a autorización automática para cirugía, sedación o anestesia."
    }
    val detailEn = when(status) {
        "RED" -> "At least one red flag is present. Repeat/confirm the measurement when appropriate, stabilize within professional scope, and activate the indicated emergency or referral protocol."
        "YELLOW" -> "One or more factors modify management. Confirm data, review history/medications, and adjust or defer the procedure according to diagnosis, complexity and resources."
        else -> "The recorded data show no major systemic alert on this screen. This is not automatic clearance for surgery, sedation or anesthesia."
    }

    val treatments = when {
        red -> emptyList()
        infection -> listOf("Diagnóstico y control del foco","Endodoncia o drenaje según diagnóstico","Anestesia local con evaluación farmacológica")
        "Dolor dental/orofacial" in signs || "Sensibilidad al frío/calor" in signs || "Dolor espontáneo/nocturno" in signs ->
            listOf("Diagnóstico pulpar/periapical","Tratamiento restaurador o endodóntico según diagnóstico","Anestesia local")
        "Supuración/fístula" in signs ->
            listOf("Diagnóstico y control del foco","Tratamiento periodontal/endodóntico según origen","Anestesia local")
        "Sangrado gingival" in signs || "Movilidad dental" in signs ->
            listOf("Evaluación periodontal","Control de placa/instrumentación periodontal","Anestesia local si está indicada")
        else ->
            listOf("Exploración y diagnóstico","Prevención / control de riesgo","Restauración si existe lesión compatible")
    }.take(3)

    val restrictionsEs = mutableListOf<String>()
    val restrictionsEn = mutableListOf<String>()
    fun add(es:String,en:String){restrictionsEs += es; restrictionsEn += en}
    if ("Sangrado oral no controlable" in signs) add("Sangrado no controlable: no iniciar procedimiento electivo; activar protocolo de hemostasia/urgencia.","Uncontrolled bleeding: do not start elective care; activate hemostasis/emergency protocol.")
    if ("Inflamación facial/cervical" in signs || "Disnea" in signs || "Disfagia" in signs || "Odinofagia" in signs) add("Posible compromiso de vía aérea: no tratar electivamente y valorar urgencia.","Possible airway compromise: do not provide elective care and assess urgently.")
    if (sys != null && dia != null && age >= 18) {
        if (sys > 180 || dia > 110) add("TA >180/>110 mmHg: repetir y solicitar valoración médica; no tratamiento electivo.","BP >180/>110 mmHg: repeat and obtain medical assessment; no elective treatment.")
        else if (sys >= 160 || dia >= 100) add("TA ≥160/100 mmHg confirmada: diferir procedimientos electivos invasivos y reevaluar.","Confirmed BP ≥160/100 mmHg: defer invasive elective procedures and reassess.")
        else if (sys < 90 || dia < 60) add("TA baja: repetir y valorar síntomas, perfusión y medicamentos antes de procedimientos invasivos.","Low BP: repeat and assess symptoms, perfusion and medications before invasive procedures.")
    }
    if (spo2 != null && spo2 < 95) add(if(spo2 < 90) "SpO₂ <90 %: valoración urgente y sin tratamiento electivo." else "SpO₂ 90–94 %: repetir y contextualizar; si persiste, valorar antes de atención electiva.", if(spo2 < 90) "SpO₂ <90%: urgent assessment and no elective treatment." else "SpO₂ 90–94%: repeat and contextualize; if persistent, assess before elective care.")
    if (glucose != null && glucose < 70) add("Glucosa <70 mg/dL: corregir hipoglucemia y reevaluar antes de continuar.","Glucose <70 mg/dL: correct hypoglycemia and reassess before continuing.")
    if (glucose != null && glucose >= 300) add("Glucosa ≥300 mg/dL: diferir procedimientos electivos invasivos y valorar control metabólico.","Glucose ≥300 mg/dL: defer invasive elective procedures and assess metabolic control.")
    if (temp != null && temp >= 38) add("Temperatura ≥38 °C: buscar causa; si hay infección sistémica, diferir electivo.","Temperature ≥38 °C: identify cause; if systemic infection is suspected, defer elective care.")
    if ("Úlcera o lesión >2 semanas" in signs || "Mancha blanca/roja persistente" in signs) add("Lesión persistente: documentar y valorar referencia/biopsia según hallazgos; no asumir diagnóstico por aspecto.","Persistent lesion: document and consider referral/biopsy according to findings; do not assume a diagnosis from appearance.")
    if (antithrombotic.isNotBlank()) add("Anticoagulante/antiagregante registrado: no suspender ni modificar automáticamente; planificar hemostasia local y consultar al médico si se considera modificar tratamiento.","Anticoagulant/antiplatelet recorded: do not stop or alter automatically; plan local hemostasis and consult the physician if medication modification is considered.")
    if (rhythmMeds.isNotBlank() || cardiovascularHistory.isNotBlank()) add("Antecedente cardiovascular/fármaco cardiovascular: revisar antes de usar vasoconstrictor; usar técnica de aspiración e inyección lenta y considerar límite de epinefrina de 0.04 mg en adultos cuando exista necesidad de cautela.","Cardiovascular history/cardiac medication: review before vasoconstrictor use; use aspiration and slow injection, and consider the 0.04 mg adult epinephrine precaution limit when cardiovascular caution is needed.")
    if (bmi != null && bmi >= 40) add("IMC ≥40: no contraindica por sí solo la atención dental; valorar comorbilidades y vía aérea si se considera sedación.","BMI ≥40: not by itself a dental contraindication; assess comorbidities and airway if sedation is considered.")
    if (systemicCaution) add("Antecedentes sistémicos adicionales registrados: revisar comorbilidades, medicamentos y plan antes de procedimientos invasivos.","Additional systemic history recorded: review comorbidities, medications and the plan before invasive procedures.")

    val pregnancyStatus = profile.pregnancyStatus.trim().lowercase()
    val hepaticRenalStatus = profile.hepaticRenalDisease.trim().lowercase()
    val weightKg = profile.weightKg.toDoubleOrNull()
    val cautionCardio = (age >= 18 && sys != null && dia != null && (sys >= 160 || dia >= 100)) ||
        (hr != null && (hr < 50 || hr > 120)) ||
        cardiovascularHistory.isNotBlank() || rhythmMeds.isNotBlank()
    val allergyLocal = allergy.contains("lidoca") || allergy.contains("mepiv") || allergy.contains("artic") || allergy.contains("bupiv") || allergy.contains("anestes")

    fun doseLine(name:String, concentration:String, vasoconstrictor:String, mgKg:Double):String {
        if (weightKg == null || weightKg <= 0.0) {
            return "$name $concentration $vasoconstrictor · límite educativo: "+mgKg+" mg/kg. Peso no registrado: no calcular dosis total."
        }
        val mg = weightKg * mgKg
        return "$name $concentration $vasoconstrictor · "+mgKg+" mg/kg × "+("%.1f".format(weightKg))+" kg = "+("%.0f".format(mg))+" mg como límite educativo calculado. Verificar ficha técnica y límites específicos antes de administrar."
    }

    val anestheticEs = when {
        red -> "ANESTESIA: NO seleccionar ni administrar anestésico como si existiera autorización. Primero resolver/valorar la condición sistémica de alarma."
        allergyLocal -> "ANESTESIA: antecedente de alergia relacionado con anestésicos locales registrado. Verificar fármaco, reacción y ficha técnica; no asumir que otro anestésico es seguro sin evaluación."
        cautionCardio -> "ANESTESIA: opción educativa de referencia: " +
            doseLine("Mepivacaína","3%","SIN vasoconstrictor",4.4) +
            " Si el vasoconstrictor es necesario, la ADA describe como precaución habitual en adultos limitar la epinefrina a 0.04 mg, con aspiración y administración lenta. No convertir automáticamente ese límite a cartuchos: depende de la concentración y volumen del producto."
        age < 4 -> "ANESTESIA: " +
            doseLine("Lidocaína","2%","CON vasoconstrictor, si el producto está indicado",4.4) +
            " En menores, la dosis debe calcularse estrictamente por peso y ficha técnica. La articaína no se recomienda en menores de 4 años según la tabla ADA consultada."
        else -> {
            val primary = if (treatments.any { it.contains("extracción", ignoreCase=true) })
                doseLine("Articaína","4%","CON epinefrina 1:100,000–1:200,000",7.0)
            else
                doseLine("Lidocaína","2%","CON epinefrina 1:100,000",4.4)
            "ANESTESIA: opción educativa principal: $primary. Alternativa frecuente: " +
                doseLine("Articaína","4%","CON epinefrina 1:100,000–1:200,000",7.0) +
                " En extracciones, la guía ADA también contempla bupivacaína 0.5% CON epinefrina 1:200,000 en adultos/adolescentes. La elección depende de técnica, procedimiento, antecedentes, edad, peso y ficha técnica."
        }
    }
    val anestheticEn = when {
        red -> "ANESTHESIA: DO NOT select or administer an anesthetic as if clearance existed. First assess/resolve the systemic red flag."
        allergyLocal -> "ANESTHESIA: a local-anesthetic-related allergy is recorded. Verify the drug, reaction and labeling; do not assume another anesthetic is safe without assessment."
        cautionCardio -> "ANESTHESIA: educational reference option: " +
            doseLine("Mepivacaine","3%","WITHOUT vasoconstrictor",4.4) +
            " If a vasoconstrictor is necessary, ADA describes 0.04 mg epinephrine as a common adult precaution limit, with aspiration and slow injection. Do not automatically convert that limit to cartridges: it depends on product concentration and volume."
        age < 4 -> "ANESTHESIA: " +
            doseLine("Lidocaine","2%","WITH vasoconstrictor, if the product is age-appropriate",4.4) +
            " In young children, dose must be calculated strictly by weight and labeling. Articaine is not recommended under age 4 in the cited ADA table."
        else -> {
            val primary = if (treatments.any { it.contains("extracción", ignoreCase=true) })
                doseLine("Articaine","4%","WITH epinephrine 1:100,000–1:200,000",7.0)
            else
                doseLine("Lidocaine","2%","WITH epinephrine 1:100,000",4.4)
            "ANESTHESIA: educational primary option: $primary. Common alternative: " +
                doseLine("Articaine","4%","WITH epinephrine 1:100,000–1:200,000",7.0) +
                " For extractions, ADA guidance also includes 0.5% bupivacaine WITH 1:200,000 epinephrine in adolescents/adults. Choice depends on technique, procedure, history, age, weight and labeling."
        }
    }

    val alerts = mutableListOf<ClinicalAlertV1>()
    fun alert(severity:String, esTrigger:String, esAction:String, enTrigger:String, enAction:String) {
        alerts += ClinicalAlertV1(severity, esTrigger, esAction, enTrigger, enAction)
    }
    if (sys != null && dia != null && age >= 18 && (sys > 180 || dia > 110))
        alert("RED","TA $sys/$dia mmHg","Repetir la medición y solicitar valoración médica; no iniciar atención electiva.","BP $sys/$dia mmHg","Repeat the measurement and obtain medical assessment; do not start elective care.")
    else if (sys != null && dia != null && age >= 18 && (sys >= 160 || dia >= 100))
        alert("YELLOW","TA $sys/$dia mmHg","Confirmar la medición y revisar el plan antes de procedimientos invasivos.","BP $sys/$dia mmHg","Confirm the measurement and review the plan before invasive procedures.")
    if (spo2 != null && spo2 < 90)
        alert("RED","SpO₂ $spo2 %","Confirmar la lectura y realizar valoración urgente según el contexto.","SpO₂ $spo2%","Confirm the reading and obtain urgent assessment as appropriate.")
    else if (spo2 != null && spo2 < 95)
        alert("YELLOW","SpO₂ $spo2 %","Repetir y contextualizar; si persiste, valorar antes de atención electiva.","SpO₂ $spo2%","Repeat and contextualize; if persistent, assess before elective care.")
    if (glucose != null && glucose < 70)
        alert("RED","Glucosa $glucose mg/dL","Corregir/seguir el protocolo de hipoglucemia y reevaluar antes de continuar.","Glucose $glucose mg/dL","Treat/follow the hypoglycemia protocol and reassess before continuing.")
    else if (glucose != null && glucose >= 300)
        alert("YELLOW","Glucosa $glucose mg/dL","Valorar control metabólico y diferir procedimientos invasivos electivos si corresponde.","Glucose $glucose mg/dL","Assess metabolic control and defer invasive elective procedures when appropriate.")
    if (temp != null && temp >= 38.0)
        alert(if (temp >= 40.0) "RED" else "YELLOW","Temperatura $temp °C","Confirmar y buscar la causa; si se sospecha infección sistémica, diferir atención electiva.","Temperature $temp °C","Confirm and identify the cause; if systemic infection is suspected, defer elective care.")
    if ("Sangrado oral no controlable" in signs)
        alert("RED","Sangrado oral no controlable","No iniciar procedimiento electivo y activar protocolo de hemostasia/urgencia.","Uncontrolled oral bleeding","Do not start elective care; activate the hemostasis/emergency protocol.")
    if ("Inflamación facial/cervical" in signs || "Disnea" in signs || "Disfagia" in signs)
        alert("RED","Posible compromiso de vía aérea","No tratar electivamente y valorar urgencia.","Possible airway compromise","Do not provide elective care and obtain urgent assessment.")
    if ("Úlcera o lesión >2 semanas" in signs || "Mancha blanca/roja persistente" in signs)
        alert("YELLOW","Lesión oral persistente","Documentar evolución y valorar referencia/biopsia según hallazgos; no asumir diagnóstico por apariencia.","Persistent oral lesion","Document the evolution and consider referral/biopsy according to findings; do not assume a diagnosis from appearance.")
    if (antithrombotic.isNotBlank())
        alert("YELLOW","Anticoagulante/antiagregante registrado","No suspenderlo automáticamente; planificar hemostasia y revisar el procedimiento.","Anticoagulant/antiplatelet recorded","Do not stop it automatically; plan hemostasis and review the procedure.")
    if (cardiovascularHistory.isNotBlank() || rhythmMeds.isNotBlank())
        alert("YELLOW","Antecedente/fármaco cardiovascular registrado","Revisar antes de usar vasoconstrictor y considerar la precaución de epinefrina indicada.","Cardiovascular history/medication recorded","Review before vasoconstrictor use and consider the indicated epinephrine precaution.")
    if (systemicCaution)
        alert("YELLOW","Comorbilidad sistémica registrada","Revisar enfermedad, medicamentos y plan antes de procedimientos invasivos.","Systemic comorbidity recorded","Review disease, medications and plan before invasive procedures.")

    return ClinicalDecisionV1(status,titleEs,titleEn,detailEs,detailEn,treatments,restrictionsEs.take(8),restrictionsEn.take(8),alerts.take(10),anestheticEs,anestheticEn)
}
