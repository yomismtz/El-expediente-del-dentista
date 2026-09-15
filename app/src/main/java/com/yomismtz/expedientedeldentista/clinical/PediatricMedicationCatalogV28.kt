package com.yomismtz.expedientedeldentista.clinical

/**
 * Catálogo revisado para México.
 *
 * La pauta de dosis se basa prioritariamente en AAPD Useful Medications for Oral Conditions
 * (Reference Manual 2025-2026; revisión 2025). Las presentaciones se contrastaron con
 * fuentes mexicanas (COFEPRIS/Secretaría de Salud CDMX e información para prescribir de
 * productos comercializados en México). El catálogo es educativo y no sustituye la ficha
 * técnica del producto concreto ni la prescripción clínica.
 */
data class PediatricMedicationVerificationV28(
    val medicineId: String,
    val mexicoEvidenceEs: String,
    val mexicoEvidenceEn: String,
    val doseEvidenceEs: String = "Dosis de referencia: AAPD, Useful Medications for Oral Conditions, revisión 2025.",
    val doseEvidenceEn: String = "Reference dose: AAPD, Useful Medications for Oral Conditions, 2025 revision.",
    val reviewed: String = "2026-09-15"
)

private fun liquidV28(
    id: String,
    labelEs: String,
    labelEn: String,
    mgPer5Ml: Double,
    noteEs: String = "",
    noteEn: String = ""
) = PediatricMedicationPresentationV27(
    id = id,
    labelEs = labelEs,
    labelEn = labelEn,
    mgPerMl = mgPer5Ml / 5.0,
    noteEs = noteEs,
    noteEn = noteEn
)

private fun unitV28(
    id: String,
    labelEs: String,
    labelEn: String,
    mg: Double,
    noteEs: String = "",
    noteEn: String = ""
) = PediatricMedicationPresentationV27(
    id = id,
    labelEs = labelEs,
    labelEn = labelEn,
    mgPerUnit = mg,
    noteEs = noteEs,
    noteEn = noteEn
)

private val amoxClav4to1V28 = PediatricMedicationSpecV27(
    id = "AMOX_CLAV_4_1",
    category = PediatricMedicationCategoryV27.ANTIBIOTIC,
    nameEs = "Amoxicilina + ácido clavulánico · relación 4:1",
    nameEn = "Amoxicillin + clavulanate · 4:1 ratio",
    presentations = listOf(
        liquidV28(
            "AMC_125_31_5_Q8",
            "Suspensión 125/31.25 mg por 5 mL",
            "125/31.25 mg per 5 mL suspension",
            125.0,
            "El cálculo usa el componente amoxicilina. Presentación 4:1: no intercambiar volumen por volumen con relaciones diferentes.",
            "Calculation uses the amoxicillin component. 4:1 formulation: do not interchange volume-for-volume with different ratios."
        ),
        liquidV28(
            "AMC_250_62_5_Q8",
            "Suspensión 250/62.5 mg por 5 mL",
            "250/62.5 mg per 5 mL suspension",
            250.0,
            "El cálculo usa el componente amoxicilina. Presentación 4:1: no intercambiar volumen por volumen con relaciones diferentes.",
            "Calculation uses the amoxicillin component. 4:1 formulation: do not interchange volume-for-volume with different ratios."
        )
    ),
    regimens = listOf(
        PediatricMedicationRegimenV27(
            id = "AMC_4_1_Q8",
            labelEs = "Presentación 4:1 · cada 8 h",
            labelEn = "4:1 formulation · every 8 h",
            basis = PediatricDoseBasis.PER_DAY,
            minMgKg = 20.0,
            maxMgKg = 40.0,
            intervalHours = listOf(8),
            maxSingleMg = 500.0,
            noteEs = "Ficha mexicana de amoxicilina/clavulanato 4:1: 20–40 mg/kg/día del componente amoxicilina divididos cada 8 h. AAPD señala que las formulaciones 4:1 se administran 3 veces al día.",
            noteEn = "Mexican 4:1 amoxicillin/clavulanate labeling: 20–40 mg/kg/day of the amoxicillin component divided every 8 h. AAPD notes that 4:1 formulations are dosed 3 times daily."
        )
    ),
    cautionEs = "Usar la menor exposición de clavulanato compatible con la indicación. Verifica la relación exacta impresa en el frasco; las distintas relaciones no son intercambiables volumen por volumen.",
    cautionEn = "Use the lowest clavulanate exposure compatible with the indication. Verify the exact ratio on the bottle; different ratios are not volume-for-volume interchangeable."
)

private val amoxClav7to1V28 = PediatricMedicationSpecV27(
    id = "AMOX_CLAV_7_1",
    category = PediatricMedicationCategoryV27.ANTIBIOTIC,
    nameEs = "Amoxicilina + ácido clavulánico · relación 7:1",
    nameEn = "Amoxicillin + clavulanate · 7:1 ratio",
    presentations = listOf(
        liquidV28(
            "AMC_200_28_5_Q12",
            "Suspensión 200/28.5 mg por 5 mL",
            "200/28.5 mg per 5 mL suspension",
            200.0,
            "El cálculo usa el componente amoxicilina.",
            "Calculation uses the amoxicillin component."
        ),
        liquidV28(
            "AMC_400_57_5_Q12",
            "Suspensión 400/57 mg por 5 mL",
            "400/57 mg per 5 mL suspension",
            400.0,
            "El cálculo usa el componente amoxicilina.",
            "Calculation uses the amoxicillin component."
        ),
        unitV28(
            "AMC_TAB_875_125_Q12",
            "Tableta 875/125 mg",
            "875/125 mg tablet",
            875.0,
            "El cálculo usa el componente amoxicilina.",
            "Calculation uses the amoxicillin component."
        )
    ),
    regimens = listOf(
        PediatricMedicationRegimenV27(
            id = "AMC_7_1_Q12",
            labelEs = "Presentación 7:1 · cada 12 h",
            labelEn = "7:1 formulation · every 12 h",
            basis = PediatricDoseBasis.PER_DAY,
            minMgKg = 25.0,
            maxMgKg = 45.0,
            intervalHours = listOf(12),
            maxSingleMg = 875.0,
            maxDailyMg = 1750.0,
            noteEs = "AAPD: basado en el componente amoxicilina, 25–45 mg/kg/día divididos cada 12 h; máximo 875 mg por dosis y 1750 mg/día. Las formulaciones 7:1 se administran 2 veces al día.",
            noteEn = "AAPD: based on the amoxicillin component, 25–45 mg/kg/day divided every 12 h; maximum 875 mg per dose and 1750 mg/day. 7:1 formulations are dosed twice daily."
        )
    ),
    cautionEs = "Verifica la relación exacta y el componente clavulanato. La suspensión 600/42.9 mg por 5 mL no se incluye aquí porque corresponde a una relación/esquema especial y no debe tratarse como intercambiable con 4:1 o 7:1.",
    cautionEn = "Verify the exact ratio and clavulanate component. The 600/42.9 mg per 5 mL suspension is not included here because it is a special ratio/regimen and must not be treated as interchangeable with 4:1 or 7:1."
)

/** Catálogo usado por la interfaz a partir de v0.32. */
val pediatricMedicationSpecsV28: List<PediatricMedicationSpecV27> = buildList {
    pediatricMedicationSpecsV27.forEach { med ->
        when (med.id) {
            "PARACETAMOL" -> add(
                med.copy(
                    presentations = listOf(
                        PediatricMedicationPresentationV27(
                            id = "PARA_DROPS_100_MX",
                            labelEs = "Solución oral / gotas 100 mg/mL",
                            labelEn = "Oral solution / drops 100 mg/mL",
                            mgPerMl = 100.0,
                            noteEs = "Presentación documentada en México por Secretaría de Salud CDMX y productos registrados/comercializados.",
                            noteEn = "Formulation documented in Mexico by Mexico City Health and registered/marketed products."
                        ),
                        unitV28("PARA_TAB_500_MX", "Tableta 500 mg", "500 mg tablet", 500.0)
                    )
                )
            )
            "IBUPROFEN" -> add(
                med.copy(
                    presentations = listOf(
                        liquidV28("IBU_100_5_MX", "Suspensión oral 100 mg/5 mL", "100 mg/5 mL oral suspension", 100.0),
                        PediatricMedicationPresentationV27(
                            id = "IBU_40_ML_MX",
                            labelEs = "Suspensión/gotas 40 mg/mL (equivale a 200 mg/5 mL)",
                            labelEn = "Suspension/drops 40 mg/mL (equivalent to 200 mg/5 mL)",
                            mgPerMl = 40.0
                        ),
                        unitV28("IBU_TAB_200_MX", "Tableta/cápsula 200 mg", "200 mg tablet/capsule", 200.0),
                        unitV28("IBU_TAB_400_MX", "Tableta/cápsula 400 mg", "400 mg tablet/capsule", 400.0)
                    )
                )
            )
            "NAPROXEN" -> add(
                med.copy(
                    presentations = listOf(
                        liquidV28("NAP_125_5_MX", "Suspensión oral 125 mg/5 mL", "125 mg/5 mL oral suspension", 125.0),
                        unitV28("NAP_TAB_250_MX", "Tableta 250 mg", "250 mg tablet", 250.0),
                        unitV28(
                            "NAP_TAB_500_MX",
                            "Tableta 500 mg",
                            "500 mg tablet",
                            500.0,
                            "La indicación pediátrica puede variar entre productos; verifica edad mínima del fabricante.",
                            "Pediatric labeling may vary between products; verify the manufacturer's minimum age."
                        )
                    )
                )
            )
            "AMOX_CLAV" -> {
                add(amoxClav4to1V28)
                add(amoxClav7to1V28)
            }
            "CEPHALEXIN" -> add(
                med.copy(
                    presentations = listOf(
                        liquidV28("CEPH_125_5_MX", "Suspensión 125 mg/5 mL", "125 mg/5 mL suspension", 125.0),
                        liquidV28("CEPH_250_5_MX", "Suspensión 250 mg/5 mL", "250 mg/5 mL suspension", 250.0),
                        unitV28("CEPH_CAP_250_MX", "Cápsula 250 mg", "250 mg capsule", 250.0),
                        unitV28("CEPH_CAP_500_MX", "Cápsula 500 mg", "500 mg capsule", 500.0)
                    )
                )
            )
            "ACYCLOVIR" -> add(
                med.copy(
                    presentations = listOf(
                        liquidV28("ACY_200_5_MX", "Suspensión 200 mg/5 mL", "200 mg/5 mL suspension", 200.0),
                        unitV28("ACY_TAB_200_MX", "Tableta 200 mg", "200 mg tablet", 200.0),
                        unitV28("ACY_TAB_400_MX", "Tableta 400 mg", "400 mg tablet", 400.0),
                        PediatricMedicationPresentationV27(
                            id = "ACY_CREAM_5_MX",
                            labelEs = "Crema 5% (tópica; sin cálculo por kg)",
                            labelEn = "5% cream (topical; no weight-based calculation)",
                            topicalPercent = 5.0,
                            noteEs = "Presentación tópica comercializada en México. La frecuencia depende de edad e indicación; no se convierte a mg/kg.",
                            noteEn = "Topical formulation marketed in Mexico. Frequency depends on age and indication; it is not converted to mg/kg."
                        )
                    )
                )
            )
            "VALACYCLOVIR" -> add(
                med.copy(
                    presentations = listOf(
                        unitV28("VALA_TAB_500_MX", "Tableta/comprimido 500 mg", "500 mg tablet", 500.0),
                        unitV28("VALA_TAB_1000_MX", "Tableta/comprimido 1000 mg", "1000 mg tablet", 1000.0)
                    )
                )
            )
            else -> add(med)
        }
    }
}

val pediatricMedicationVerificationsV28 = listOf(
    PediatricMedicationVerificationV28("PARACETAMOL", "México: solución oral 100 mg/mL y tableta 500 mg documentadas por Secretaría de Salud CDMX/PLM.", "Mexico: 100 mg/mL oral solution and 500 mg tablet documented by Mexico City Health/PLM."),
    PediatricMedicationVerificationV28("IBUPROFEN", "México: suspensión 100 mg/5 mL, 40 mg/mL y tabletas/cápsulas 200–400 mg documentadas por COFEPRIS/Secretaría de Salud CDMX/PLM.", "Mexico: 100 mg/5 mL suspension, 40 mg/mL liquid and 200–400 mg tablets/capsules documented by COFEPRIS/Mexico City Health/PLM."),
    PediatricMedicationVerificationV28("NAPROXEN", "México: suspensión 125 mg/5 mL y tabletas 250/500 mg documentadas por Secretaría de Salud CDMX/PLM.", "Mexico: 125 mg/5 mL suspension and 250/500 mg tablets documented by Mexico City Health/PLM."),
    PediatricMedicationVerificationV28("AMOXICILLIN", "México: suspensiones 250 y 500 mg/5 mL y formas sólidas documentadas en información para prescribir mexicana.", "Mexico: 250 and 500 mg/5 mL suspensions and solid forms documented in Mexican prescribing information."),
    PediatricMedicationVerificationV28("AMOX_CLAV_4_1", "México: 125/31.25 y 250/62.5 mg por 5 mL documentadas; relación 4:1 con pauta cada 8 h según ficha mexicana.", "Mexico: 125/31.25 and 250/62.5 mg per 5 mL documented; 4:1 ratio with every-8-hour schedule per Mexican labeling."),
    PediatricMedicationVerificationV28("AMOX_CLAV_7_1", "México: 200/28.5 y 400/57 mg por 5 mL y 875/125 mg documentadas; relación 7:1 cada 12 h.", "Mexico: 200/28.5 and 400/57 mg per 5 mL and 875/125 mg documented; 7:1 ratio every 12 h."),
    PediatricMedicationVerificationV28("AZITHROMYCIN", "México: suspensión 200 mg/5 mL y tableta 500 mg documentadas por PLM.", "Mexico: 200 mg/5 mL suspension and 500 mg tablet documented by PLM."),
    PediatricMedicationVerificationV28("CEPHALEXIN", "México: suspensiones 125/5 y 250/5 mg/mL y cápsulas 250/500 mg documentadas por PLM.", "Mexico: 125/5 and 250/5 mg/mL suspensions and 250/500 mg capsules documented by PLM."),
    PediatricMedicationVerificationV28("CLARITHROMYCIN", "México: suspensiones 125 y 250 mg/5 mL y tabletas 250/500 mg documentadas por PLM.", "Mexico: 125 and 250 mg/5 mL suspensions and 250/500 mg tablets documented by PLM."),
    PediatricMedicationVerificationV28("CLINDAMYCIN", "México: solución 75 mg/5 mL y cápsulas documentadas; la suspensión DALACIN C cuenta con registro sanitario mexicano.", "Mexico: 75 mg/5 mL solution and capsules documented; DALACIN C suspension has Mexican sanitary registration."),
    PediatricMedicationVerificationV28("METRONIDAZOLE", "México: suspensiones 125 y 250 mg/5 mL y tabletas documentadas por PLM.", "Mexico: 125 and 250 mg/5 mL suspensions and tablets documented by PLM."),
    PediatricMedicationVerificationV28("ACYCLOVIR", "México: suspensión 200 mg/5 mL, tabletas 200/400 mg y crema 5% documentadas por PLM.", "Mexico: 200 mg/5 mL suspension, 200/400 mg tablets and 5% cream documented by PLM."),
    PediatricMedicationVerificationV28("VALACYCLOVIR", "México: comprimidos 500 y 1000 mg documentados por PLM.", "Mexico: 500 and 1000 mg tablets documented by PLM.")
).associateBy { it.medicineId }

fun pediatricMedicationVerificationV28(medicineId: String): PediatricMedicationVerificationV28? =
    pediatricMedicationVerificationsV28[medicineId]
