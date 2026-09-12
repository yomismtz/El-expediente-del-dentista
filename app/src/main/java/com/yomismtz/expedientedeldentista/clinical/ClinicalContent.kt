package com.yomismtz.expedientedeldentista.clinical

data class DiseaseGuide(
    val id: String,
    val nameEs: String,
    val nameEn: String,
    val questionEs: String,
    val questionEn: String,
    val examplesEs: String,
    val examplesEn: String
)

data class AsaGuide(
    val value: Int,
    val titleEs: String,
    val titleEn: String,
    val examplesEs: String,
    val examplesEn: String
)

data class IcdasGuide(val code: Int, val es: String, val en: String)

object ClinicalContent {
    val permanentTeeth = listOf(
        18,17,16,15,14,13,12,11,21,22,23,24,25,26,27,28,
        48,47,46,45,44,43,42,41,31,32,33,34,35,36,37,38
    )

    val primaryTeeth = listOf(
        55,54,53,52,51,61,62,63,64,65,
        85,84,83,82,81,71,72,73,74,75
    )

    val diseases = listOf(
        DiseaseGuide("exanthematic", "Exantemáticas", "Exanthematous diseases",
            "¿Le ha dado sarampión, varicela, rubéola o paperas?",
            "Have you had measles, chickenpox, rubella or mumps?",
            "Sarampión, varicela, rubéola, paperas", "Measles, chickenpox, rubella, mumps"),
        DiseaseGuide("congenital", "Congénitas", "Congenital disorders",
            "¿Nació con algún problema del corazón, labio, huesos o alguna enfermedad desde nacimiento?",
            "Were you born with a heart, lip, bone or other condition?",
            "Cardiopatías congénitas, labio/paladar hendido, alteraciones del desarrollo",
            "Congenital heart disease, cleft lip/palate, developmental disorders"),
        DiseaseGuide("osteo", "Osteo-mio-articulares", "Musculoskeletal",
            "¿Tiene artritis, dolor de huesos, rodillas, mandíbula o columna?",
            "Do you have arthritis or recurrent bone, joint, jaw or back pain?",
            "Artritis, osteoporosis, trastornos articulares", "Arthritis, osteoporosis, joint disorders"),
        DiseaseGuide("cardio", "Cardiovasculares", "Cardiovascular",
            "¿Tiene presión alta, soplos, arritmias, problemas del corazón o ha tenido un infarto?",
            "Do you have high blood pressure, a murmur, arrhythmia, heart disease or a previous heart attack?",
            "Hipertensión, arritmias, angina, infarto, insuficiencia cardiaca",
            "Hypertension, arrhythmia, angina, myocardial infarction, heart failure"),
        DiseaseGuide("endocrine", "Endocrinas", "Endocrine",
            "¿Tiene diabetes, problemas de tiroides o le han dicho que tiene el azúcar alta?",
            "Do you have diabetes, thyroid disease or high blood sugar?",
            "Diabetes, hipo/hipertiroidismo", "Diabetes, hypo/hyperthyroidism"),
        DiseaseGuide("respiratory", "Respiratorias", "Respiratory",
            "¿Tiene asma, bronquitis, EPOC, falta de aire o usa inhaladores?",
            "Do you have asthma, bronchitis, COPD, shortness of breath or use inhalers?",
            "Asma, bronquitis, EPOC", "Asthma, bronchitis, COPD"),
        DiseaseGuide("neuro", "Neurológicas / psiquiátricas", "Neurologic / psychiatric",
            "¿Ha tenido convulsiones, epilepsia, desmayos o recibe tratamiento psiquiátrico?",
            "Have you had seizures, epilepsy, fainting or psychiatric treatment?",
            "Epilepsia, convulsiones, ansiedad, depresión", "Epilepsy, seizures, anxiety, depression"),
        DiseaseGuide("hematologic", "Hematológicas", "Hematologic",
            "¿Sangra o se amorata con facilidad? ¿Le han diagnosticado anemia, hemofilia u otro problema de la sangre?",
            "Do you bleed or bruise easily? Have you been diagnosed with anemia, hemophilia or another blood disorder?",
            "Anemia, hemofilia, trastornos plaquetarios", "Anemia, hemophilia, platelet disorders"),
        DiseaseGuide("gastro", "Gastrointestinales", "Gastrointestinal",
            "¿Tiene gastritis, reflujo, úlceras, diarrea frecuente u otra enfermedad digestiva?",
            "Do you have gastritis, reflux, ulcers, frequent diarrhea or another digestive disorder?",
            "Gastritis, reflujo, úlcera", "Gastritis, reflux, ulcer"),
        DiseaseGuide("renal", "Renales", "Renal",
            "¿Tiene enfermedad renal, insuficiencia renal, diálisis o trasplante?",
            "Do you have kidney disease, kidney failure, dialysis or a transplant?",
            "Insuficiencia renal, diálisis, trasplante", "Kidney failure, dialysis, transplant"),
        DiseaseGuide("neoplastic", "Neoplásicas", "Neoplastic",
            "¿Le han diagnosticado cáncer o recibe quimioterapia/radioterapia?",
            "Have you been diagnosed with cancer or are you receiving chemotherapy/radiotherapy?",
            "Leucemias, tumores sólidos", "Leukemia, solid tumors"),
        DiseaseGuide("hiv", "VIH / inmunodeficiencia", "HIV / immunodeficiency",
            "¿Tiene VIH o alguna enfermedad o tratamiento que disminuya sus defensas?",
            "Do you have HIV or a disease/treatment that lowers your immune defenses?",
            "VIH, inmunodeficiencias", "HIV, immunodeficiency"),
        DiseaseGuide("hepatitis", "Hepáticas / hepatitis", "Liver disease / hepatitis",
            "¿Ha tenido hepatitis, cirrosis u otra enfermedad del hígado?",
            "Have you had hepatitis, cirrhosis or another liver disease?",
            "Hepatitis, cirrosis", "Hepatitis, cirrhosis"),
        DiseaseGuide("allergy", "Alérgicas", "Allergic",
            "¿Es alérgico a medicamentos, alimentos, látex, anestésicos u otras sustancias?",
            "Are you allergic to medicines, foods, latex, local anesthetics or other substances?",
            "Medicamentos, látex, alimentos", "Medicines, latex, foods"),
        DiseaseGuide("sti", "Infecciones de transmisión sexual", "Sexually transmitted infections",
            "¿Le han diagnosticado alguna infección de transmisión sexual?",
            "Have you been diagnosed with a sexually transmitted infection?",
            "Sífilis, herpes, otras ITS", "Syphilis, herpes, other STIs")
    )

    val asa = listOf(
        AsaGuide(1, "ASA I · paciente sano", "ASA I · healthy patient",
            "Sin enfermedad sistémica relevante.", "No relevant systemic disease."),
        AsaGuide(2, "ASA II · enfermedad sistémica leve", "ASA II · mild systemic disease",
            "Ejemplos didácticos: hipertensión o diabetes controladas, asma leve, tabaquismo, obesidad leve.",
            "Educational examples: controlled hypertension or diabetes, mild asthma, smoking, mild obesity."),
        AsaGuide(3, "ASA III · enfermedad sistémica grave", "ASA III · severe systemic disease",
            "Ejemplos didácticos: EPOC, diabetes mal controlada, obesidad mórbida, insuficiencia renal en diálisis.",
            "Educational examples: COPD, poorly controlled diabetes, morbid obesity, renal failure on dialysis."),
        AsaGuide(4, "ASA IV · amenaza constante para la vida", "ASA IV · constant threat to life",
            "Ejemplos didácticos: insuficiencia cardiaca severa, sepsis o infarto reciente.",
            "Educational examples: severe heart failure, sepsis or recent myocardial infarction."),
        AsaGuide(5, "ASA V · paciente moribundo", "ASA V · moribund patient",
            "No se espera que sobreviva sin cirugía.", "Not expected to survive without surgery."),
        AsaGuide(6, "ASA VI · muerte cerebral", "ASA VI · brain-dead donor",
            "Paciente con muerte cerebral para donación de órganos.", "Brain-dead patient for organ donation.")
    )

    val icdas = listOf(
        IcdasGuide(0, "Superficie sana, sin evidencia de caries.", "Sound surface, no evidence of caries."),
        IcdasGuide(1, "Primer cambio visual en esmalte después del secado.", "First visual change in enamel after drying."),
        IcdasGuide(2, "Cambio visual evidente en esmalte incluso sin secar.", "Distinct visual change in enamel even without drying."),
        IcdasGuide(3, "Pérdida localizada de integridad del esmalte, sin dentina visible.", "Localized enamel breakdown without visible dentin."),
        IcdasGuide(4, "Sombra oscura subyacente de dentina, con o sin ruptura del esmalte.", "Underlying dark shadow from dentin, with or without enamel breakdown."),
        IcdasGuide(5, "Cavidad distinta con dentina visible.", "Distinct cavity with visible dentin."),
        IcdasGuide(6, "Cavidad extensa y profunda con dentina visible.", "Extensive distinct cavity with visible dentin.")
    )

    val treatmentPlans = listOf(
        TreatmentPlan("healthy", "Diente sano", "Healthy tooth", listOf(
            TreatmentOption("observe", "Prevención y control periódico", "Prevention and periodic review",
                "Preferible cuando no existe enfermedad activa; mantener higiene y medidas preventivas según riesgo.",
                "Preferred when there is no active disease; maintain hygiene and preventive measures according to risk.", true),
            TreatmentOption("fluoride", "Flúor tópico según riesgo", "Topical fluoride according to risk",
                "Puede indicarse como medida preventiva según el riesgo de caries.", "May be indicated as a preventive measure according to caries risk."),
            TreatmentOption("restore_not", "Restauración operatoria", "Operative restoration",
                "No es la primera elección en una superficie sana.", "Not the first choice for a sound surface.")
        )),
        TreatmentPlan("initial_caries", "Lesión inicial de caries / ICDAS 1–2", "Initial caries lesion / ICDAS 1–2", listOf(
            TreatmentOption("remin", "Remineralización y control de riesgo", "Remineralization and risk control",
                "Opción educativa preferible para una lesión no cavitada: control de biofilm, fluoruro y seguimiento.",
                "Educationally preferred for a non-cavitated lesion: biofilm control, fluoride and monitoring.", true),
            TreatmentOption("sealant", "Sellador de fosetas y fisuras", "Pit and fissure sealant",
                "Útil en superficies susceptibles cuando la indicación clínica lo permite.", "Useful on susceptible surfaces when clinically indicated."),
            TreatmentOption("resin_infiltration", "Infiltración de resina", "Resin infiltration",
                "Alternativa microinvasiva en lesiones seleccionadas no cavitadas.", "Micro-invasive alternative for selected non-cavitated lesions.")
        )),
        TreatmentPlan("cavitated_caries", "Caries cavitada restaurable", "Restorable cavitated caries", listOf(
            TreatmentOption("resin", "Restauración con resina compuesta", "Composite resin restoration",
                "Preferible cuando existe cavitación restaurable y se puede lograr aislamiento adecuado.",
                "Preferred for a restorable cavity when adequate isolation can be achieved.", true),
            TreatmentOption("art", "Tratamiento restaurador atraumático (ART)", "Atraumatic restorative treatment (ART)",
                "Alternativa con remoción manual y material de alta viscosidad en escenarios seleccionados.",
                "Alternative using hand excavation and high-viscosity material in selected scenarios."),
            TreatmentOption("hall", "Técnica de Hall (diente temporal indicado)", "Hall technique (indicated primary tooth)",
                "Alternativa específica para molares temporales seleccionados; requiere valorar indicaciones y contraindicaciones.",
                "Specific alternative for selected primary molars; indications and contraindications must be assessed.")
        )),
        TreatmentPlan("reversible_pulpitis", "Pulpitis reversible / caries profunda con pulpa vital", "Reversible pulpitis / deep caries with vital pulp", listOf(
            TreatmentOption("ipt", "Tratamiento pulpar indirecto / remoción selectiva", "Indirect pulp treatment / selective caries removal",
                "Preferible en un escenario compatible con pulpa vital recuperable y diente restaurable.",
                "Preferred in a scenario compatible with recoverable vital pulp and a restorable tooth.", true),
            TreatmentOption("pulpotomy", "Pulpotomía cuando esté indicada", "Pulpotomy when indicated",
                "Puede corresponder en dientes temporales o permanentes jóvenes bajo criterios específicos.",
                "May be appropriate in primary or young permanent teeth under specific criteria."),
            TreatmentOption("direct_cap", "Recubrimiento pulpar directo", "Direct pulp capping",
                "Se reserva para exposiciones seleccionadas; no debe elegirse automáticamente ante caries profunda.",
                "Reserved for selected exposures; it should not be chosen automatically for deep caries.")
        )),
        TreatmentPlan("irreversible_pulpitis", "Pulpitis irreversible sintomática", "Symptomatic irreversible pulpitis", listOf(
            TreatmentOption("endo", "Tratamiento endodóntico si el diente es conservable", "Root canal treatment if the tooth is restorable",
                "Opción educativa preferible para un diente permanente conservable con diagnóstico pulpar irreversible.",
                "Educationally preferred for a restorable permanent tooth with irreversible pulpal diagnosis.", true),
            TreatmentOption("pulpotomy_vital", "Pulpotomía vital en casos seleccionados", "Vital pulpotomy in selected cases",
                "Puede considerarse con criterios contemporáneos específicos; requiere valoración clínica completa.",
                "May be considered under specific contemporary criteria; requires complete clinical assessment."),
            TreatmentOption("extract", "Extracción si el diente no es restaurable", "Extraction if the tooth is non-restorable",
                "Se considera cuando el pronóstico restaurador es desfavorable; en menores debe valorarse el espacio.",
                "Consider when restorative prognosis is poor; in children space must be assessed.")
        )),
        TreatmentPlan("necrosis", "Necrosis pulpar", "Pulp necrosis", listOf(
            TreatmentOption("endo_necrosis", "Tratamiento de conductos / pulpectomía según dentición", "Root canal treatment / pulpectomy according to dentition",
                "Preferible cuando el diente es conservable y el tratamiento endodóntico está indicado.",
                "Preferred when the tooth is restorable and endodontic treatment is indicated.", true),
            TreatmentOption("extract_necrosis", "Extracción si el pronóstico es desfavorable", "Extraction if prognosis is poor",
                "Puede ser necesaria si el diente no es restaurable o existen otras contraindicaciones para conservarlo.",
                "May be needed if the tooth is non-restorable or there are other contraindications to retaining it."),
            TreatmentOption("urgent_drain", "Manejo de urgencia y drenaje cuando corresponda", "Urgent management and drainage when appropriate",
                "No sustituye el tratamiento definitivo; depende de los signos de infección y del estado clínico.",
                "Does not replace definitive treatment; depends on infection signs and clinical status.")
        )),
        TreatmentPlan("periodontal", "Enfermedad periodontal asociada a placa", "Plaque-associated periodontal disease", listOf(
            TreatmentOption("hygiene_phase", "Fase higiénica periodontal", "Periodontal hygiene phase",
                "Preferible como base: control de biofilm, instrucción de higiene y desbridamiento según hallazgos.",
                "Preferred as the foundation: biofilm control, hygiene instruction and debridement according to findings.", true),
            TreatmentOption("reassess", "Reevaluación periodontal", "Periodontal reassessment",
                "Debe realizarse después del control inicial para valorar respuesta y necesidades adicionales.",
                "Should follow initial control to assess response and additional needs."),
            TreatmentOption("refer", "Referencia a periodoncia cuando la complejidad lo amerite", "Periodontal referral when complexity warrants",
                "Indicada ante bolsas profundas, pérdida de inserción avanzada u otros hallazgos que excedan el manejo básico.",
                "Indicated for deep pockets, advanced attachment loss or findings beyond basic management.")
        )),
        TreatmentPlan("nonrestorable", "Diente no restaurable / extracción indicada", "Non-restorable tooth / extraction indicated", listOf(
            TreatmentOption("extract_space", "Extracción y valorar mantenimiento de espacio", "Extraction and assess space maintenance",
                "Preferible cuando la conservación no es viable; si es un diente temporal, valorar edad dental, sucesor y espacio.",
                "Preferred when retention is not viable; for a primary tooth assess dental age, successor and space.", true),
            TreatmentOption("temporary", "Manejo temporal si la extracción debe diferirse", "Temporary management if extraction must be delayed",
                "Puede considerarse solo como medida transitoria según el estado clínico.", "May be considered only as a temporary measure according to clinical status."),
            TreatmentOption("observe_nonrest", "Observación sin tratamiento", "Observation without treatment",
                "No es la opción preferible si existe una indicación real de extracción.", "Not preferred when a true extraction indication exists.")
        ))
    )
}
