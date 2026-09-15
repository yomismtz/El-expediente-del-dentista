package com.yomismtz.expedientedeldentista.clinical

import kotlin.math.min

enum class PediatricMedicationCategoryV27 {
    ANALGESIC,
    NSAID,
    ANTIBIOTIC,
    NITROIMIDAZOLE,
    ANTIVIRAL
}

data class PediatricMedicationPresentationV27(
    val id: String,
    val labelEs: String,
    val labelEn: String,
    val mgPerMl: Double? = null,
    val mgPerUnit: Double? = null,
    val topicalPercent: Double? = null,
    val noteEs: String = "",
    val noteEn: String = ""
) {
    val isTopical: Boolean get() = topicalPercent != null
}

data class PediatricMedicationRegimenV27(
    val id: String,
    val labelEs: String,
    val labelEn: String,
    val basis: PediatricDoseBasis,
    val minMgKg: Double,
    val maxMgKg: Double,
    val intervalHours: List<Int>,
    val maxSingleMg: Double? = null,
    val maxDailyMg: Double? = null,
    val maxDailyMgKg: Double? = null,
    val noteEs: String = "",
    val noteEn: String = ""
)

data class PediatricMedicationSpecV27(
    val id: String,
    val category: PediatricMedicationCategoryV27,
    val nameEs: String,
    val nameEn: String,
    val presentations: List<PediatricMedicationPresentationV27>,
    val regimens: List<PediatricMedicationRegimenV27>,
    val cautionEs: String,
    val cautionEn: String
)

data class PediatricMedicationCalculationV27(
    val doseMinMg: Double,
    val doseMaxMg: Double,
    val dailyMinMg: Double,
    val dailyMaxMg: Double,
    val mlMin: Double?,
    val mlMax: Double?,
    val unitsMin: Double?,
    val unitsMax: Double?,
    val dosesPerDayEquivalent: Double
)

fun calculatePediatricMedicationRangeV27(
    weightKg: Double,
    regimen: PediatricMedicationRegimenV27,
    intervalHours: Int,
    presentation: PediatricMedicationPresentationV27
): PediatricMedicationCalculationV27? {
    if (weightKg <= 0.0 || intervalHours <= 0 || presentation.isTopical) return null
    val dosesPerDay = 24.0 / intervalHours.toDouble()

    var doseMin = when (regimen.basis) {
        PediatricDoseBasis.PER_DOSE -> weightKg * regimen.minMgKg
        PediatricDoseBasis.PER_DAY -> weightKg * regimen.minMgKg / dosesPerDay
    }
    var doseMax = when (regimen.basis) {
        PediatricDoseBasis.PER_DOSE -> weightKg * regimen.maxMgKg
        PediatricDoseBasis.PER_DAY -> weightKg * regimen.maxMgKg / dosesPerDay
    }

    val maxDailyByKg = regimen.maxDailyMgKg?.times(weightKg)
    val absoluteDailyLimit = listOfNotNull(maxDailyByKg, regimen.maxDailyMg).minOrNull()
    if (absoluteDailyLimit != null) {
        doseMax = min(doseMax, absoluteDailyLimit / dosesPerDay)
    }
    regimen.maxSingleMg?.let { doseMax = min(doseMax, it) }
    if (doseMin > doseMax) doseMin = doseMax

    val dailyMin = doseMin * dosesPerDay
    val dailyMax = doseMax * dosesPerDay
    val mlMin = presentation.mgPerMl?.let { doseMin / it }
    val mlMax = presentation.mgPerMl?.let { doseMax / it }
    val unitsMin = presentation.mgPerUnit?.let { doseMin / it }
    val unitsMax = presentation.mgPerUnit?.let { doseMax / it }

    return PediatricMedicationCalculationV27(
        doseMinMg = doseMin,
        doseMaxMg = doseMax,
        dailyMinMg = dailyMin,
        dailyMaxMg = dailyMax,
        mlMin = mlMin,
        mlMax = mlMax,
        unitsMin = unitsMin,
        unitsMax = unitsMax,
        dosesPerDayEquivalent = dosesPerDay
    )
}

private fun liquid(id: String, es: String, en: String, mgPer5Ml: Double, noteEs: String = "", noteEn: String = "") =
    PediatricMedicationPresentationV27(id, es, en, mgPerMl = mgPer5Ml / 5.0, noteEs = noteEs, noteEn = noteEn)

private fun unit(id: String, es: String, en: String, mg: Double, noteEs: String = "", noteEn: String = "") =
    PediatricMedicationPresentationV27(id, es, en, mgPerUnit = mg, noteEs = noteEs, noteEn = noteEn)

val pediatricMedicationSpecsV27 = listOf(
    PediatricMedicationSpecV27(
        id = "PARACETAMOL",
        category = PediatricMedicationCategoryV27.ANALGESIC,
        nameEs = "Paracetamol / acetaminofén",
        nameEn = "Acetaminophen / paracetamol",
        presentations = listOf(
            liquid("PARA_160_5", "Jarabe/suspensión 160 mg/5 mL", "Syrup/suspension 160 mg/5 mL", 160.0),
            PediatricMedicationPresentationV27("PARA_DROPS_100", "Gotas 100 mg/mL", "Drops 100 mg/mL", mgPerMl = 100.0),
            unit("PARA_TAB_500", "Tableta 500 mg", "500 mg tablet", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "PARA_PAIN",
                labelEs = "Dolor leve/moderado",
                labelEn = "Mild/moderate pain",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 10.0,
                maxMgKg = 15.0,
                intervalHours = listOf(4, 6),
                maxDailyMg = 4000.0,
                maxDailyMgKg = 75.0,
                noteEs = "AAPD: 10–15 mg/kg por dosis cada 4–6 h; máximo 75 mg/kg/día sin exceder 4 g/24 h.",
                noteEn = "AAPD: 10–15 mg/kg per dose every 4–6 h; maximum 75 mg/kg/day, not to exceed 4 g/24 h."
            )
        ),
        cautionEs = "Revisar duplicidad con otros productos que contengan paracetamol y riesgo de hepatotoxicidad.",
        cautionEn = "Check for duplicate acetaminophen-containing products and hepatotoxicity risk."
    ),
    PediatricMedicationSpecV27(
        id = "IBUPROFEN",
        category = PediatricMedicationCategoryV27.NSAID,
        nameEs = "Ibuprofeno",
        nameEn = "Ibuprofen",
        presentations = listOf(
            liquid("IBU_100_5", "Suspensión 100 mg/5 mL", "Suspension 100 mg/5 mL", 100.0),
            liquid("IBU_200_5", "Suspensión forte 200 mg/5 mL", "Forte suspension 200 mg/5 mL", 200.0),
            unit("IBU_TAB_200", "Tableta/cápsula 200 mg", "200 mg tablet/capsule", 200.0),
            unit("IBU_TAB_400", "Tableta/cápsula 400 mg", "400 mg tablet/capsule", 400.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "IBU_PAIN",
                labelEs = "Dolor/inflamación",
                labelEn = "Pain/inflammation",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 4.0,
                maxMgKg = 10.0,
                intervalHours = listOf(6, 8),
                maxSingleMg = 600.0,
                noteEs = "AAPD: 4–10 mg/kg por dosis cada 6–8 h en menores de 12 años; máximo por dosis 600 mg.",
                noteEn = "AAPD: 4–10 mg/kg per dose every 6–8 h in children under 12; maximum single dose 600 mg."
            )
        ),
        cautionEs = "AINE: valorar deshidratación, enfermedad renal, asma sensible a AINE, sangrado y otras contraindicaciones.",
        cautionEn = "NSAID: assess dehydration, kidney disease, NSAID-sensitive asthma, bleeding, and other contraindications."
    ),
    PediatricMedicationSpecV27(
        id = "NAPROXEN",
        category = PediatricMedicationCategoryV27.NSAID,
        nameEs = "Naproxeno",
        nameEn = "Naproxen",
        presentations = listOf(
            liquid("NAP_125_5", "Suspensión 125 mg/5 mL", "Suspension 125 mg/5 mL", 125.0),
            unit("NAP_TAB_250", "Tableta 250 mg", "250 mg tablet", 250.0),
            unit("NAP_TAB_500", "Tableta 500 mg", "500 mg tablet", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "NAP_PAIN",
                labelEs = "Dolor/inflamación",
                labelEn = "Pain/inflammation",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 5.0,
                maxMgKg = 7.0,
                intervalHours = listOf(8, 12),
                maxDailyMg = 1000.0,
                noteEs = "AAPD: 5–7 mg/kg por dosis cada 8–12 h; máximo 1000 mg/día.",
                noteEn = "AAPD: 5–7 mg/kg per dose every 8–12 h; maximum 1000 mg/day."
            )
        ),
        cautionEs = "AINE. Diferenciar naproxeno base de naproxeno sódico en el marbete.",
        cautionEn = "NSAID. Distinguish naproxen base from naproxen sodium on the label."
    ),
    PediatricMedicationSpecV27(
        id = "AMOXICILLIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Amoxicilina",
        nameEn = "Amoxicillin",
        presentations = listOf(
            liquid("AMOX_250_5", "Suspensión 250 mg/5 mL", "Suspension 250 mg/5 mL", 250.0),
            liquid("AMOX_500_5", "Suspensión 500 mg/5 mL", "Suspension 500 mg/5 mL", 500.0),
            unit("AMOX_CAP_500", "Cápsula 500 mg", "500 mg capsule", 500.0),
            unit("AMOX_TAB_875", "Tableta 875 mg", "875 mg tablet", 875.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "AMOX_Q8",
                labelEs = "Régimen cada 8 h",
                labelEn = "Every-8-hour regimen",
                basis = PediatricDoseBasis.PER_DAY,
                minMgKg = 20.0,
                maxMgKg = 40.0,
                intervalHours = listOf(8),
                maxSingleMg = 500.0,
                noteEs = "AAPD: >3 meses y <40 kg: 20–40 mg/kg/día divididos cada 8 h; máximo 500 mg por dosis.",
                noteEn = "AAPD: >3 months and <40 kg: 20–40 mg/kg/day divided every 8 h; maximum 500 mg per dose."
            ),
            PediatricMedicationRegimenV27(
                id = "AMOX_Q12",
                labelEs = "Régimen cada 12 h",
                labelEn = "Every-12-hour regimen",
                basis = PediatricDoseBasis.PER_DAY,
                minMgKg = 25.0,
                maxMgKg = 45.0,
                intervalHours = listOf(12),
                maxSingleMg = 875.0,
                noteEs = "AAPD: >3 meses y <40 kg: 25–45 mg/kg/día divididos cada 12 h; máximo 875 mg por dosis.",
                noteEn = "AAPD: >3 months and <40 kg: 25–45 mg/kg/day divided every 12 h; maximum 875 mg per dose."
            )
        ),
        cautionEs = "Antibiótico: confirmar indicación bacteriana, alergias y función renal. No usar para procesos virales.",
        cautionEn = "Antibiotic: confirm bacterial indication, allergies, and renal function. Do not use for viral disease."
    ),
    PediatricMedicationSpecV27(
        id = "AMOX_CLAV",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Amoxicilina + ácido clavulánico",
        nameEn = "Amoxicillin + clavulanate",
        presentations = listOf(
            liquid("AMC_125_31_5", "Suspensión 125/31.25 mg por 5 mL", "Suspension 125/31.25 mg per 5 mL", 125.0, "El cálculo usa el componente amoxicilina.", "Calculation uses the amoxicillin component."),
            liquid("AMC_200_28_5", "Suspensión 200/28.5 mg por 5 mL", "Suspension 200/28.5 mg per 5 mL", 200.0, "El cálculo usa el componente amoxicilina.", "Calculation uses the amoxicillin component."),
            liquid("AMC_250_62_5", "Suspensión 250/62.5 mg por 5 mL", "Suspension 250/62.5 mg per 5 mL", 250.0, "El cálculo usa el componente amoxicilina.", "Calculation uses the amoxicillin component."),
            liquid("AMC_400_57_5", "Suspensión 400/57 mg por 5 mL", "Suspension 400/57 mg per 5 mL", 400.0, "El cálculo usa el componente amoxicilina.", "Calculation uses the amoxicillin component."),
            unit("AMC_TAB_875", "Tableta 875/125 mg", "875/125 mg tablet", 875.0, "El cálculo usa el componente amoxicilina.", "Calculation uses the amoxicillin component.")
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "AMC_Q12",
                labelEs = "Régimen cada 12 h",
                labelEn = "Every-12-hour regimen",
                basis = PediatricDoseBasis.PER_DAY,
                minMgKg = 25.0,
                maxMgKg = 45.0,
                intervalHours = listOf(12),
                maxSingleMg = 875.0,
                maxDailyMg = 1750.0,
                noteEs = "AAPD: basado en amoxicilina, 25–45 mg/kg/día cada 12 h; máximo 875 mg por dosis y 1750 mg/día de amoxicilina.",
                noteEn = "AAPD: based on amoxicillin, 25–45 mg/kg/day every 12 h; maximum 875 mg per dose and 1750 mg/day of amoxicillin."
            )
        ),
        cautionEs = "Usar la formulación con la menor carga de clavulanato compatible con el régimen y verificar la proporción exacta del producto.",
        cautionEn = "Use the formulation with the lowest clavulanate exposure compatible with the regimen and verify the exact product ratio."
    ),
    PediatricMedicationSpecV27(
        id = "AZITHROMYCIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Azitromicina",
        nameEn = "Azithromycin",
        presentations = listOf(
            liquid("AZI_200_5", "Suspensión 200 mg/5 mL", "Suspension 200 mg/5 mL", 200.0),
            unit("AZI_TAB_500", "Tableta/capleta 500 mg", "500 mg tablet/caplet", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "AZI_DAY1",
                labelEs = "Día 1",
                labelEn = "Day 1",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 10.0,
                maxMgKg = 12.0,
                intervalHours = listOf(24),
                maxSingleMg = 500.0,
                noteEs = "AAPD: 10–12 mg/kg en una dosis el día 1; máximo 500 mg.",
                noteEn = "AAPD: 10–12 mg/kg as a single dose on day 1; maximum 500 mg."
            ),
            PediatricMedicationRegimenV27(
                id = "AZI_LATER",
                labelEs = "Días posteriores (2–5)",
                labelEn = "Subsequent days (2–5)",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 5.0,
                maxMgKg = 6.0,
                intervalHours = listOf(24),
                maxSingleMg = 250.0,
                noteEs = "AAPD: 5–6 mg/kg cada 24 h para el resto del tratamiento habitual de 2–5 días.",
                noteEn = "AAPD: 5–6 mg/kg every 24 h for the remainder of the usual 2–5 day course."
            )
        ),
        cautionEs = "Puede prolongar QT en pacientes susceptibles. Confirmar indicación y alergias.",
        cautionEn = "May prolong QT in susceptible patients. Confirm indication and allergies."
    ),
    PediatricMedicationSpecV27(
        id = "CEPHALEXIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Cefalexina",
        nameEn = "Cephalexin",
        presentations = listOf(
            liquid("CEPH_250_5", "Suspensión 250 mg/5 mL", "Suspension 250 mg/5 mL", 250.0),
            unit("CEPH_CAP_250", "Cápsula 250 mg", "250 mg capsule", 250.0),
            unit("CEPH_CAP_500", "Cápsula 500 mg", "500 mg capsule", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "CEPH_MILD",
                labelEs = "Infección leve/moderada",
                labelEn = "Mild/moderate infection",
                basis = PediatricDoseBasis.PER_DAY,
                minMgKg = 25.0,
                maxMgKg = 50.0,
                intervalHours = listOf(6, 8, 12),
                maxDailyMg = 2000.0,
                noteEs = "AAPD: 25–50 mg/kg/día divididos cada 6–12 h; máximo 2000 mg/día.",
                noteEn = "AAPD: 25–50 mg/kg/day divided every 6–12 h; maximum 2000 mg/day."
            ),
            PediatricMedicationRegimenV27(
                id = "CEPH_SEVERE",
                labelEs = "Infección grave",
                labelEn = "Severe infection",
                basis = PediatricDoseBasis.PER_DAY,
                minMgKg = 75.0,
                maxMgKg = 100.0,
                intervalHours = listOf(6, 8),
                maxDailyMg = 4000.0,
                noteEs = "AAPD: 75–100 mg/kg/día divididos cada 6–8 h; máximo 4000 mg/día.",
                noteEn = "AAPD: 75–100 mg/kg/day divided every 6–8 h; maximum 4000 mg/day."
            )
        ),
        cautionEs = "No usar como alternativa si hubo anafilaxia, angioedema o urticaria con penicilina/ampicilina sin valorar el riesgo de reacción cruzada.",
        cautionEn = "Do not use as an alternative after penicillin/ampicillin anaphylaxis, angioedema, or urticaria without assessing cross-reactivity risk."
    ),
    PediatricMedicationSpecV27(
        id = "CLARITHROMYCIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Claritromicina",
        nameEn = "Clarithromycin",
        presentations = listOf(
            liquid("CLARI_125_5", "Suspensión 125 mg/5 mL", "Suspension 125 mg/5 mL", 125.0),
            liquid("CLARI_250_5", "Suspensión 250 mg/5 mL", "Suspension 250 mg/5 mL", 250.0),
            unit("CLARI_TAB_250", "Tableta 250 mg", "250 mg tablet", 250.0),
            unit("CLARI_TAB_500", "Tableta 500 mg", "500 mg tablet", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "CLARI_Q12",
                labelEs = "Régimen pediátrico",
                labelEn = "Pediatric regimen",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 7.5,
                maxMgKg = 7.5,
                intervalHours = listOf(12),
                maxSingleMg = 500.0,
                noteEs = "AAPD: 7.5 mg/kg por dosis cada 12 h; máximo 500 mg por dosis.",
                noteEn = "AAPD: 7.5 mg/kg per dose every 12 h; maximum 500 mg per dose."
            )
        ),
        cautionEs = "Puede prolongar QT y tiene interacciones por CYP3A4; revisar medicación concomitante.",
        cautionEn = "May prolong QT and has CYP3A4 interactions; review concomitant medications."
    ),
    PediatricMedicationSpecV27(
        id = "CLINDAMYCIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Clindamicina",
        nameEn = "Clindamycin",
        presentations = listOf(
            liquid("CLINDA_75_5", "Suspensión 75 mg/5 mL", "Suspension 75 mg/5 mL", 75.0),
            unit("CLINDA_CAP_150", "Cápsula 150 mg", "150 mg capsule", 150.0),
            unit("CLINDA_CAP_300", "Cápsula 300 mg", "300 mg capsule", 300.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "CLINDA_SOFT",
                labelEs = "Infección de tejidos blandos",
                labelEn = "Soft-tissue infection",
                basis = PediatricDoseBasis.PER_DAY,
                minMgKg = 20.0,
                maxMgKg = 30.0,
                intervalHours = listOf(8),
                maxSingleMg = 450.0,
                noteEs = "AAPD: 20–30 mg/kg/día divididos cada 8 h; máximo 450 mg por dosis.",
                noteEn = "AAPD: 20–30 mg/kg/day divided every 8 h; maximum 450 mg per dose."
            )
        ),
        cautionEs = "Riesgo importante de colitis por C. difficile. Ya no se recomienda para profilaxis de endocarditis dental.",
        cautionEn = "Important C. difficile colitis risk. No longer recommended for dental endocarditis prophylaxis."
    ),
    PediatricMedicationSpecV27(
        id = "METRONIDAZOLE",
        category = PediatricMedicationCategoryV27.NITROIMIDAZOLE,
        nameEs = "Metronidazol",
        nameEn = "Metronidazole",
        presentations = listOf(
            liquid("METRO_125_5", "Suspensión 125 mg/5 mL", "Suspension 125 mg/5 mL", 125.0),
            liquid("METRO_250_5", "Suspensión 250 mg/5 mL", "Suspension 250 mg/5 mL", 250.0),
            unit("METRO_TAB_250", "Tableta 250 mg", "250 mg tablet", 250.0),
            unit("METRO_TAB_500", "Tableta 500 mg", "500 mg tablet", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "METRO_PERIO",
                labelEs = "Enfermedad periodontal / gingivitis necrosante (alergia a penicilina)",
                labelEn = "Periodontal disease / necrotizing gingivitis (penicillin allergy)",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 10.0,
                maxMgKg = 10.0,
                intervalHours = listOf(8),
                maxSingleMg = 250.0,
                noteEs = "AAPD: 10 mg/kg por dosis cada 8 h por 7 días; máximo 250 mg por dosis. AAPD no da recomendación pediátrica para infección odontógena anaerobia aislada.",
                noteEn = "AAPD: 10 mg/kg per dose every 8 h for 7 days; maximum 250 mg per dose. AAPD gives no pediatric recommendation for isolated anaerobic odontogenic infection."
            )
        ),
        cautionEs = "Evitar alcohol y productos con propilenglicol durante el tratamiento según la ficha farmacológica. Usar solo con indicación clínica apropiada.",
        cautionEn = "Avoid alcohol and propylene-glycol-containing products during therapy per drug labeling. Use only for an appropriate clinical indication."
    ),
    PediatricMedicationSpecV27(
        id = "ACYCLOVIR",
        category = PediatricMedicationCategoryV27.ANTIVIRAL,
        nameEs = "Aciclovir",
        nameEn = "Acyclovir",
        presentations = listOf(
            liquid("ACY_200_5", "Suspensión 200 mg/5 mL", "Suspension 200 mg/5 mL", 200.0),
            unit("ACY_TAB_400", "Tableta 400 mg", "400 mg tablet", 400.0),
            PediatricMedicationPresentationV27(
                id = "ACY_CREAM_5",
                labelEs = "Crema 5% (tópica; sin cálculo por kg)",
                labelEn = "5% cream (topical; no weight-based calculation)",
                topicalPercent = 5.0,
                noteEs = "AAPD: para herpes labial en ≥12 años, capa fina 5 veces al día durante 4 días; la forma tópica no se calcula por kg.",
                noteEn = "AAPD: for herpes labialis in ages ≥12 years, thin layer 5 times daily for 4 days; the topical form is not weight-based."
            )
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "ACY_PRIMARY",
                labelEs = "Gingivoestomatitis herpética primaria",
                labelEn = "Primary herpetic gingivostomatitis",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 20.0,
                maxMgKg = 20.0,
                intervalHours = listOf(6),
                maxSingleMg = 800.0,
                noteEs = "AAPD: 20 mg/kg por dosis 4 veces al día por 5–7 días (equivalente matemático aproximado: cada 6 h); máximo 800 mg por dosis. Uso no aprobado por FDA para esta indicación.",
                noteEn = "AAPD: 20 mg/kg per dose 4 times daily for 5–7 days (approximate evenly-spaced equivalent: every 6 h); maximum 800 mg per dose. Not FDA-approved for this indication."
            ),
            PediatricMedicationRegimenV27(
                id = "ACY_LABIAL",
                labelEs = "Herpes labial sistémico",
                labelEn = "Systemic herpes labialis",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 20.0,
                maxMgKg = 20.0,
                intervalHours = listOf(6),
                maxSingleMg = 400.0,
                noteEs = "AAPD: 20 mg/kg por dosis 4 veces al día por 7–10 días o hasta resolución (equivalente matemático aproximado: cada 6 h); máximo 400 mg por dosis.",
                noteEn = "AAPD: 20 mg/kg per dose 4 times daily for 7–10 days or until resolution (approximate evenly-spaced equivalent: every 6 h); maximum 400 mg per dose."
            )
        ),
        cautionEs = "Ajustar en insuficiencia renal y asegurar hidratación apropiada. La indicación y duración dependen del cuadro herpético.",
        cautionEn = "Adjust for renal impairment and ensure appropriate hydration. Indication and duration depend on the herpetic condition."
    ),
    PediatricMedicationSpecV27(
        id = "VALACYCLOVIR",
        category = PediatricMedicationCategoryV27.ANTIVIRAL,
        nameEs = "Valaciclovir",
        nameEn = "Valacyclovir",
        presentations = listOf(
            unit("VALA_TAB_500", "Tableta/comprimido 500 mg", "500 mg tablet", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "VALA_PRIMARY",
                labelEs = "Gingivoestomatitis herpética primaria",
                labelEn = "Primary herpetic gingivostomatitis",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 20.0,
                maxMgKg = 20.0,
                intervalHours = listOf(12),
                maxSingleMg = 1000.0,
                noteEs = "AAPD: ≥3 meses, 20 mg/kg por dosis cada 12 h por 5–7 días; máximo 1000 mg por dosis. Uso no aprobado por FDA para esta indicación.",
                noteEn = "AAPD: age ≥3 months, 20 mg/kg per dose every 12 h for 5–7 days; maximum 1000 mg per dose. Not FDA-approved for this indication."
            )
        ),
        cautionEs = "La suspensión puede requerir preparación magistral; en México se comercializan comprimidos de 500 mg. Ajustar en insuficiencia renal.",
        cautionEn = "Suspension may require compounding; 500 mg tablets are marketed in Mexico. Adjust for renal impairment."
    )
)
