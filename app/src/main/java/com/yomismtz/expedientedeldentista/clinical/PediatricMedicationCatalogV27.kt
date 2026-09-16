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
    if (intervalHours !in regimen.intervalHours) return null
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

private fun liquid(
    id: String,
    es: String,
    en: String,
    mgPer5Ml: Double,
    noteEs: String = "",
    noteEn: String = ""
) = PediatricMedicationPresentationV27(
    id = id,
    labelEs = es,
    labelEn = en,
    mgPerMl = mgPer5Ml / 5.0,
    noteEs = noteEs,
    noteEn = noteEn
)

private fun unit(
    id: String,
    es: String,
    en: String,
    mg: Double,
    noteEs: String = "",
    noteEn: String = ""
) = PediatricMedicationPresentationV27(
    id = id,
    labelEs = es,
    labelEn = en,
    mgPerUnit = mg,
    noteEs = noteEs,
    noteEn = noteEn
)

val pediatricMedicationSpecsV27 = listOf(
    PediatricMedicationSpecV27(
        id = "PARACETAMOL",
        category = PediatricMedicationCategoryV27.ANALGESIC,
        nameEs = "Paracetamol / acetaminofén",
        nameEn = "Acetaminophen / paracetamol",
        presentations = listOf(
            PediatricMedicationPresentationV27(
                id = "PARA_100_ML",
                labelEs = "Solución oral / gotas 100 mg/mL",
                labelEn = "Oral solution / drops 100 mg/mL",
                mgPerMl = 100.0,
                noteEs = "Presentación documentada en México por Secretaría de Salud CDMX y productos con información para prescribir mexicana.",
                noteEn = "Formulation documented in Mexico by Mexico City Health and Mexican prescribing information."
            ),
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
                noteEs = "AAPD: 10–15 mg/kg por dosis cada 4–6 h; máximo 75 mg/kg/día sin exceder 4000 mg/24 h.",
                noteEn = "AAPD: 10–15 mg/kg per dose every 4–6 h; maximum 75 mg/kg/day, not to exceed 4000 mg/24 h."
            )
        ),
        cautionEs = "Verificado 15-09-2026. Dosis: AAPD Useful Medications, revisión 2025. Presentaciones mexicanas: Secretaría de Salud CDMX/PLM. Revisar duplicidad con otros productos que contengan paracetamol y riesgo de hepatotoxicidad.",
        cautionEn = "Verified 2026-09-15. Dose: AAPD Useful Medications, 2025 revision. Mexican formulations: Mexico City Health/PLM. Check duplicate acetaminophen products and hepatotoxicity risk."
    ),

    PediatricMedicationSpecV27(
        id = "IBUPROFEN",
        category = PediatricMedicationCategoryV27.NSAID,
        nameEs = "Ibuprofeno",
        nameEn = "Ibuprofen",
        presentations = listOf(
            liquid("IBU_100_5", "Suspensión oral 100 mg/5 mL", "100 mg/5 mL oral suspension", 100.0),
            PediatricMedicationPresentationV27(
                id = "IBU_40_ML",
                labelEs = "Suspensión/gotas 40 mg/mL (equivale a 200 mg/5 mL)",
                labelEn = "Suspension/drops 40 mg/mL (equivalent to 200 mg/5 mL)",
                mgPerMl = 40.0
            ),
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
        cautionEs = "Verificado 15-09-2026. México: COFEPRIS/Secretaría de Salud documentan suspensión 100 mg/5 mL y 40 mg/mL; PLM/documentos públicos documentan 200–400 mg en formas sólidas. AINE: valorar deshidratación, enfermedad renal, asma sensible a AINE y sangrado.",
        cautionEn = "Verified 2026-09-15. Mexico: COFEPRIS/Mexico City Health document 100 mg/5 mL and 40 mg/mL liquids; PLM/public records document 200–400 mg solid forms. NSAID: assess dehydration, renal disease, NSAID-sensitive asthma and bleeding."
    ),

    PediatricMedicationSpecV27(
        id = "NAPROXEN",
        category = PediatricMedicationCategoryV27.NSAID,
        nameEs = "Naproxeno",
        nameEn = "Naproxen",
        presentations = listOf(
            liquid("NAP_125_5", "Suspensión oral 125 mg/5 mL", "125 mg/5 mL oral suspension", 125.0),
            unit("NAP_TAB_250", "Tableta 250 mg", "250 mg tablet", 250.0),
            unit(
                "NAP_TAB_500",
                "Tableta 500 mg",
                "500 mg tablet",
                500.0,
                "La autorización pediátrica puede variar por producto; verifica la edad mínima del fabricante.",
                "Pediatric authorization may vary by product; verify the manufacturer's minimum age."
            )
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
        cautionEs = "Verificado 15-09-2026. México: Secretaría de Salud CDMX documenta suspensión 125 mg/5 mL y tableta 250 mg; PLM documenta 250/500 mg. Diferenciar naproxeno base de naproxeno sódico y verificar edad autorizada del producto.",
        cautionEn = "Verified 2026-09-15. Mexico: Mexico City Health documents 125 mg/5 mL suspension and 250 mg tablet; PLM documents 250/500 mg. Distinguish naproxen base from naproxen sodium and verify product age labeling."
    ),

    PediatricMedicationSpecV27(
        id = "AMOXICILLIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Amoxicilina",
        nameEn = "Amoxicillin",
        presentations = listOf(
            liquid("AMOX_250_5", "Suspensión 250 mg/5 mL", "250 mg/5 mL suspension", 250.0),
            liquid("AMOX_500_5", "Suspensión 500 mg/5 mL", "500 mg/5 mL suspension", 500.0),
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
        cautionEs = "Verificado 15-09-2026. Presentaciones 250 y 500 mg/5 mL documentadas en México. Antibiótico: confirmar indicación bacteriana, alergias y función renal; no usar para procesos virales.",
        cautionEn = "Verified 2026-09-15. 250 and 500 mg/5 mL formulations are documented in Mexico. Antibiotic: confirm bacterial indication, allergies and renal function; do not use for viral disease."
    ),

    PediatricMedicationSpecV27(
        id = "AMOX_CLAV_4_1",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Amoxicilina + ácido clavulánico · 4:1",
        nameEn = "Amoxicillin + clavulanate · 4:1",
        presentations = listOf(
            liquid(
                "AMC_125_31_5",
                "Suspensión 125/31.25 mg por 5 mL",
                "125/31.25 mg per 5 mL suspension",
                125.0,
                "El cálculo usa el componente amoxicilina. No intercambiar volumen por volumen con otra relación.",
                "Calculation uses the amoxicillin component. Do not interchange volume-for-volume with another ratio."
            ),
            liquid(
                "AMC_250_62_5",
                "Suspensión 250/62.5 mg por 5 mL",
                "250/62.5 mg per 5 mL suspension",
                250.0,
                "El cálculo usa el componente amoxicilina. No intercambiar volumen por volumen con otra relación.",
                "Calculation uses the amoxicillin component. Do not interchange volume-for-volume with another ratio."
            )
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "AMC_4_1_Q8",
                labelEs = "Relación 4:1 · cada 8 h",
                labelEn = "4:1 ratio · every 8 h",
                basis = PediatricDoseBasis.PER_DAY,
                minMgKg = 20.0,
                maxMgKg = 40.0,
                intervalHours = listOf(8),
                maxSingleMg = 500.0,
                noteEs = "Ficha mexicana: 20–40 mg/kg/día del componente amoxicilina divididos cada 8 h. AAPD indica que las formulaciones 4:1 se administran 3 veces al día.",
                noteEn = "Mexican labeling: 20–40 mg/kg/day of the amoxicillin component divided every 8 h. AAPD states that 4:1 formulations are dosed 3 times daily."
            )
        ),
        cautionEs = "CORRECCIÓN VERIFICADA 15-09-2026: 125/31.25 y 250/62.5 mg por 5 mL son relaciones 4:1 y no deben calcularse con el esquema 7:1 cada 12 h. Usa la menor carga de clavulanato compatible con la indicación.",
        cautionEn = "VERIFIED CORRECTION 2026-09-15: 125/31.25 and 250/62.5 mg per 5 mL are 4:1 ratios and must not be calculated using the every-12-hour 7:1 regimen. Use the lowest clavulanate exposure compatible with the indication."
    ),

    PediatricMedicationSpecV27(
        id = "AMOX_CLAV_7_1",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Amoxicilina + ácido clavulánico · 7:1",
        nameEn = "Amoxicillin + clavulanate · 7:1",
        presentations = listOf(
            liquid("AMC_200_28_5", "Suspensión 200/28.5 mg por 5 mL", "200/28.5 mg per 5 mL suspension", 200.0, "El cálculo usa amoxicilina.", "Calculation uses amoxicillin."),
            liquid("AMC_400_57_5", "Suspensión 400/57 mg por 5 mL", "400/57 mg per 5 mL suspension", 400.0, "El cálculo usa amoxicilina.", "Calculation uses amoxicillin."),
            unit("AMC_TAB_875", "Tableta 875/125 mg", "875/125 mg tablet", 875.0, "El cálculo usa amoxicilina.", "Calculation uses amoxicillin.")
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "AMC_7_1_Q12",
                labelEs = "Relación 7:1 · cada 12 h",
                labelEn = "7:1 ratio · every 12 h",
                basis = PediatricDoseBasis.PER_DAY,
                minMgKg = 25.0,
                maxMgKg = 45.0,
                intervalHours = listOf(12),
                maxSingleMg = 875.0,
                maxDailyMg = 1750.0,
                noteEs = "AAPD: basado en amoxicilina, 25–45 mg/kg/día cada 12 h; máximo 875 mg por dosis y 1750 mg/día. Las formulaciones 7:1 se administran 2 veces al día.",
                noteEn = "AAPD: based on amoxicillin, 25–45 mg/kg/day every 12 h; maximum 875 mg per dose and 1750 mg/day. 7:1 formulations are dosed twice daily."
            )
        ),
        cautionEs = "Verificado 15-09-2026. México: 200/28.5 y 400/57 mg por 5 mL están documentadas. La presentación 600/42.9 mg por 5 mL se excluye de esta calculadora porque usa una relación/esquema especial y no es intercambiable con 4:1 o 7:1.",
        cautionEn = "Verified 2026-09-15. Mexico: 200/28.5 and 400/57 mg per 5 mL are documented. The 600/42.9 mg per 5 mL formulation is excluded because it uses a special ratio/regimen and is not interchangeable with 4:1 or 7:1."
    ),

    PediatricMedicationSpecV27(
        id = "AZITHROMYCIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Azitromicina",
        nameEn = "Azithromycin",
        presentations = listOf(
            liquid("AZI_200_5", "Suspensión 200 mg/5 mL", "200 mg/5 mL suspension", 200.0),
            unit("AZI_TAB_500", "Tableta 500 mg", "500 mg tablet", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "AZI_DAY1",
                labelEs = "Día 1 del esquema habitual",
                labelEn = "Day 1 of usual regimen",
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
                noteEs = "AAPD: 5–6 mg/kg cada 24 h para el resto del tratamiento habitual de 2–5 días; máximo 250 mg por toma.",
                noteEn = "AAPD: 5–6 mg/kg every 24 h for the remainder of the usual 2–5 day course; maximum 250 mg per dose."
            ),
            PediatricMedicationRegimenV27(
                id = "AZI_PERIO",
                labelEs = "Periodontal · casos seleccionados con alergia a penicilina",
                labelEn = "Periodontal · selected cases with penicillin allergy",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 10.0,
                maxMgKg = 12.0,
                intervalHours = listOf(24),
                maxSingleMg = 500.0,
                noteEs = "AAPD: 10–12 mg/kg cada 24 h durante 3 días en casos periodontales seleccionados cuando existe alergia a penicilina; máximo 500 mg/día.",
                noteEn = "AAPD: 10–12 mg/kg every 24 h for 3 days in selected periodontal cases with penicillin allergy; maximum 500 mg/day."
            )
        ),
        cautionEs = "Verificado 15-09-2026. México: suspensión 200 mg/5 mL y tableta 500 mg documentadas por PLM. Puede prolongar QT en pacientes susceptibles.",
        cautionEn = "Verified 2026-09-15. Mexico: 200 mg/5 mL suspension and 500 mg tablet documented by PLM. May prolong QT in susceptible patients."
    ),

    PediatricMedicationSpecV27(
        id = "CEPHALEXIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Cefalexina",
        nameEn = "Cephalexin",
        presentations = listOf(
            liquid("CEPH_125_5", "Suspensión 125 mg/5 mL", "125 mg/5 mL suspension", 125.0),
            liquid("CEPH_250_5", "Suspensión 250 mg/5 mL", "250 mg/5 mL suspension", 250.0),
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
        cautionEs = "Verificado 15-09-2026. México: PLM documenta suspensiones 125 y 250 mg/5 mL y cápsulas 250/500 mg. No usar como alternativa tras anafilaxia, angioedema o urticaria con penicilina/ampicilina sin valorar reacción cruzada.",
        cautionEn = "Verified 2026-09-15. Mexico: PLM documents 125 and 250 mg/5 mL suspensions and 250/500 mg capsules. Do not use as an alternative after penicillin/ampicillin anaphylaxis, angioedema or urticaria without assessing cross-reactivity."
    ),

    PediatricMedicationSpecV27(
        id = "CLARITHROMYCIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Claritromicina",
        nameEn = "Clarithromycin",
        presentations = listOf(
            liquid("CLARI_125_5", "Suspensión 125 mg/5 mL", "125 mg/5 mL suspension", 125.0),
            liquid("CLARI_250_5", "Suspensión 250 mg/5 mL", "250 mg/5 mL suspension", 250.0),
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
        cautionEs = "Verificado 15-09-2026. México: 125 y 250 mg/5 mL y tabletas 250/500 mg documentadas por PLM. Puede prolongar QT y tiene interacciones por CYP3A4.",
        cautionEn = "Verified 2026-09-15. Mexico: 125 and 250 mg/5 mL and 250/500 mg tablets documented by PLM. May prolong QT and has CYP3A4 interactions."
    ),

    PediatricMedicationSpecV27(
        id = "CLINDAMYCIN",
        category = PediatricMedicationCategoryV27.ANTIBIOTIC,
        nameEs = "Clindamicina",
        nameEn = "Clindamycin",
        presentations = listOf(
            liquid("CLINDA_75_5", "Solución/suspensión 75 mg/5 mL", "75 mg/5 mL solution/suspension", 75.0),
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
                noteEs = "AAPD: 20–30 mg/kg/día divididos cada 8 h; máximo 450 mg por dosis. Dosis mayores de 30–40 mg/kg/día se reservan en la referencia para infecciones por MRSA, no como pauta dental rutinaria.",
                noteEn = "AAPD: 20–30 mg/kg/day divided every 8 h; maximum 450 mg per dose. Higher 30–40 mg/kg/day dosing is reserved in the reference for MRSA infections, not routine dental use."
            )
        ),
        cautionEs = "Verificado 15-09-2026. México: DALACIN C 75 mg/5 mL cuenta con registro/información mexicana. Riesgo importante de colitis por C. difficile; ya no se recomienda para profilaxis de endocarditis dental.",
        cautionEn = "Verified 2026-09-15. Mexico: DALACIN C 75 mg/5 mL has Mexican registration/prescribing information. Important C. difficile colitis risk; no longer recommended for dental endocarditis prophylaxis."
    ),

    PediatricMedicationSpecV27(
        id = "METRONIDAZOLE",
        category = PediatricMedicationCategoryV27.NITROIMIDAZOLE,
        nameEs = "Metronidazol",
        nameEn = "Metronidazole",
        presentations = listOf(
            liquid("METRO_125_5", "Suspensión 125 mg/5 mL", "125 mg/5 mL suspension", 125.0),
            liquid("METRO_250_5", "Suspensión 250 mg/5 mL", "250 mg/5 mL suspension", 250.0),
            unit("METRO_TAB_250", "Tableta 250 mg", "250 mg tablet", 250.0),
            unit("METRO_TAB_500", "Tableta 500 mg", "500 mg tablet", 500.0)
        ),
        regimens = listOf(
            PediatricMedicationRegimenV27(
                id = "METRO_PERIO",
                labelEs = "Periodontal / gingivitis necrosante · casos seleccionados",
                labelEn = "Periodontal / necrotizing gingivitis · selected cases",
                basis = PediatricDoseBasis.PER_DOSE,
                minMgKg = 10.0,
                maxMgKg = 10.0,
                intervalHours = listOf(8),
                maxSingleMg = 250.0,
                noteEs = "AAPD: 10 mg/kg por dosis cada 8 h durante 7 días; máximo 250 mg por dosis. La referencia no ofrece pauta pediátrica para infección odontógena anaerobia aislada.",
                noteEn = "AAPD: 10 mg/kg per dose every 8 h for 7 days; maximum 250 mg per dose. The reference does not provide a pediatric regimen for isolated anaerobic odontogenic infection."
            )
        ),
        cautionEs = "Verificado 15-09-2026. México: PLM documenta suspensiones 125 y 250 mg/5 mL. Evitar alcohol durante el tratamiento según ficha farmacológica; usar sólo con indicación clínica apropiada.",
        cautionEn = "Verified 2026-09-15. Mexico: PLM documents 125 and 250 mg/5 mL suspensions. Avoid alcohol during therapy per drug labeling; use only for an appropriate clinical indication."
    ),

    PediatricMedicationSpecV27(
        id = "ACYCLOVIR",
        category = PediatricMedicationCategoryV27.ANTIVIRAL,
        nameEs = "Aciclovir",
        nameEn = "Acyclovir",
        presentations = listOf(
            liquid("ACY_200_5", "Suspensión 200 mg/5 mL", "200 mg/5 mL suspension", 200.0),
            unit("ACY_TAB_200", "Tableta 200 mg", "200 mg tablet", 200.0),
            unit("ACY_TAB_400", "Tableta 400 mg", "400 mg tablet", 400.0),
            PediatricMedicationPresentationV27(
                id = "ACY_CREAM_5",
                labelEs = "Crema 5% (tópica; sin cálculo por kg)",
                labelEn = "5% cream (topical; no weight-based calculation)",
                topicalPercent = 5.0,
                noteEs = "Presentación tópica comercializada en México. No se convierte a mg/kg; usa la pauta de edad/indicación de la ficha farmacológica.",
                noteEn = "Topical formulation marketed in Mexico. It is not converted to mg/kg; use the age/indication schedule from product labeling."
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
                noteEs = "AAPD: 20 mg/kg por dosis 4 veces al día durante 5–7 días (equivalente si se espacian uniformemente: cada 6 h); máximo 800 mg por dosis. Uso no aprobado por FDA para esta indicación.",
                noteEn = "AAPD: 20 mg/kg per dose 4 times daily for 5–7 days (evenly spaced equivalent: every 6 h); maximum 800 mg per dose. Not FDA-approved for this indication."
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
                noteEs = "AAPD: 20 mg/kg por dosis 4 veces al día durante 7–10 días o hasta resolución (equivalente si se espacian uniformemente: cada 6 h); máximo 400 mg por dosis.",
                noteEn = "AAPD: 20 mg/kg per dose 4 times daily for 7–10 days or until resolution (evenly spaced equivalent: every 6 h); maximum 400 mg per dose."
            )
        ),
        cautionEs = "Verificado 15-09-2026. México: suspensión 200 mg/5 mL, tabletas 200/400 mg y crema 5% documentadas por PLM. Ajustar en insuficiencia renal y asegurar hidratación apropiada.",
        cautionEn = "Verified 2026-09-15. Mexico: 200 mg/5 mL suspension, 200/400 mg tablets and 5% cream documented by PLM. Adjust for renal impairment and ensure appropriate hydration."
    ),

    PediatricMedicationSpecV27(
        id = "VALACYCLOVIR",
        category = PediatricMedicationCategoryV27.ANTIVIRAL,
        nameEs = "Valaciclovir",
        nameEn = "Valacyclovir",
        presentations = listOf(
            unit("VALA_TAB_500", "Tableta/comprimido 500 mg", "500 mg tablet", 500.0),
            unit("VALA_TAB_1000", "Tableta/comprimido 1000 mg", "1000 mg tablet", 1000.0)
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
                noteEs = "AAPD: ≥3 meses, 20 mg/kg por dosis cada 12 h durante 5–7 días; máximo 1000 mg por dosis. Uso no aprobado por FDA para esta indicación.",
                noteEn = "AAPD: age ≥3 months, 20 mg/kg per dose every 12 h for 5–7 days; maximum 1000 mg per dose. Not FDA-approved for this indication."
            )
        ),
        cautionEs = "Verificado 15-09-2026. México: comprimidos 500 y 1000 mg documentados por PLM. No se añadió una suspensión comercial porque la referencia puede requerir preparación magistral. Ajustar en insuficiencia renal.",
        cautionEn = "Verified 2026-09-15. Mexico: 500 and 1000 mg tablets documented by PLM. A commercial suspension was not added because the reference may require compounding. Adjust for renal impairment."
    )
)
