package com.yomismtz.expedientedeldentista.clinical

data class ClinicalDecisionV1(
    val status: String,
    val titleEs: String,
    val titleEn: String,
    val detailEs: String,
    val detailEn: String,
    val treatments: List<String>,
    val restrictionsEs: List<String>,
    val restrictionsEn: List<String>,
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
    val cautionCardio = (age >= 18 && sys != null && dia != null && (sys >= 160 || dia >= 100)) ||
        (hr != null && (hr < 50 || hr > 120)) ||
        cardiovascularHistory.isNotBlank() || rhythmMeds.isNotBlank()
    val allergyLocal = allergy.contains("lidoca") || allergy.contains("mepiv") || allergy.contains("artic") || allergy.contains("bupiv") || allergy.contains("anestes")
    val pregnancyKnown = pregnancyStatus.isNotBlank()
    val pregnancyPositive = pregnancyStatus.contains("embaraz") || pregnancyStatus.contains("gest") || pregnancyStatus.contains("pregnan")
    val hepaticRenalCaution = hepaticRenalStatus.isNotBlank()
    val weightKnown = profile.weightKg.toDoubleOrNull() != null

    val anestheticEs = when {
        red -> "ANESTESIA: 🚫 NO seleccionar ni administrar anestésico como si existiera autorización. Primero resolver/valorar la condición sistémica de alarma."
        allergyLocal -> "ANESTESIA: ⚠️ SIN recomendación automática. Hay antecedente relacionado con anestésicos locales. Identificar fármaco, reacción y gravedad antes de elegir otro agente; verificar ficha técnica."
        cautionCardio -> "ANESTESIA: 🟡 opción educativa: mepivacaína 3% SIN vasoconstrictor, cuando sea apropiada para el procedimiento. Si se necesita vasoconstrictor, valorar individualmente; en adultos con necesidad de cautela cardiovascular, la ADA describe como precaución habitual limitar epinefrina a 0.04 mg, con aspiración e inyección lenta."
        age < 4 -> "ANESTESIA: 🟡 lidocaína 2% CON vasoconstrictor sólo si el paciente está estable, el producto está indicado para la edad y la dosis se calcula por peso. NO usar esta pantalla para autorizar una dosis. Articaína: evitar en menores de 4 años según el etiquetado/tabla de referencia consultada."
        pregnancyPositive -> "ANESTESIA: 🟢 el embarazo por sí solo NO obliga a retirar el vasoconstrictor. La ADA indica que la anestesia local con o sin epinefrina puede utilizarse durante el embarazo. Una opción educativa habitual es lidocaína 2% CON epinefrina 1:100,000, si está clínicamente indicada y no existen otras contraindicaciones."
        hepaticRenalCaution -> "ANESTESIA: 🟡 SIN elección automática. Hay enfermedad hepática/renal registrada; revisar agente, dosis, función orgánica, medicamentos y ficha técnica antes de administrar. Si se requiere, individualizar la dosis."
        !weightKnown && age < 18 -> "ANESTESIA: 🟡 NO calcular dosis pediátrica todavía. Registrar peso real y utilizar la dosis máxima específica del producto antes de administrar."
        else -> "ANESTESIA: 🟢 opción educativa habitual CON vasoconstrictor: lidocaína 2% + epinefrina 1:100,000; alternativa habitual: articaína 4% + epinefrina 1:100,000–1:200,000. La elección depende de procedimiento, técnica, antecedentes, peso/edad y ficha técnica."
    }
    val anestheticEn = when {
        red -> "ANESTHESIA: 🚫 DO NOT select or administer an anesthetic as if clearance existed. First assess/resolve the systemic red flag."
        allergyLocal -> "ANESTHESIA: ⚠️ NO automatic recommendation. A local-anesthetic-related history is recorded. Identify the drug, reaction and severity before choosing another agent; verify labeling."
        cautionCardio -> "ANESTHESIA: 🟡 educational option: 3% mepivacaine WITHOUT vasoconstrictor when appropriate for the procedure. If a vasoconstrictor is needed, individualize; for adults needing cardiovascular caution, ADA describes 0.04 mg epinephrine as a common precaution limit, with aspiration and slow injection."
        age < 4 -> "ANESTHESIA: 🟡 2% lidocaine WITH vasoconstrictor only if stable, age-appropriate labeling applies, and dose is weight-based. Do NOT use this screen to authorize a dose. Articaine: avoid under age 4 according to the referenced labeling/table."
        pregnancyPositive -> "ANESTHESIA: 🟢 pregnancy alone does NOT require avoiding vasoconstrictor. ADA states local anesthesia with or without epinephrine can be used during pregnancy. A common educational option is 2% lidocaine WITH 1:100,000 epinephrine when clinically indicated and no other contraindication exists."
        hepaticRenalCaution -> "ANESTHESIA: 🟡 NO automatic selection. Hepatic/renal disease is recorded; review agent, dose, organ function, medications and labeling before administration. Individualize dosing when required."
        !weightKnown && age < 18 -> "ANESTHESIA: 🟡 DO NOT calculate a pediatric dose yet. Record actual body weight and use the product-specific maximum dose before administration."
        else -> "ANESTHESIA: 🟢 common educational option WITH vasoconstrictor: 2% lidocaine + 1:100,000 epinephrine; common alternative: 4% articaine + 1:100,000–1:200,000 epinephrine. Selection depends on procedure, technique, history, age/weight and labeling."
    }
    return ClinicalDecisionV1(status,titleEs,titleEn,detailEs,detailEn,treatments,restrictionsEs.take(8),restrictionsEn.take(8),anestheticEs,anestheticEn)
}
