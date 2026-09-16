package com.yomismtz.expedientedeldentista.clinical

import kotlin.math.floor

/**
 * Educational calculation helpers only. They do not choose a drug or prescribe a dose.
 * Local-anesthetic limits use the conservative pediatric dental maximums published by AAPD.
 */
enum class PediatricDoseBasis { PER_DOSE, PER_DAY }

data class PediatricDoseResult(
    val doseMg: Double,
    val dailyMg: Double,
    val mlPerDose: Double
)

fun calculatePediatricDose(
    weightKg: Double,
    orderedMgKg: Double,
    concentrationMgMl: Double,
    dosesPerDay: Int,
    basis: PediatricDoseBasis
): PediatricDoseResult? {
    if (weightKg <= 0.0 || orderedMgKg <= 0.0 || concentrationMgMl <= 0.0 || dosesPerDay <= 0) return null
    val dailyMg = when (basis) {
        PediatricDoseBasis.PER_DOSE -> weightKg * orderedMgKg * dosesPerDay
        PediatricDoseBasis.PER_DAY -> weightKg * orderedMgKg
    }
    val doseMg = when (basis) {
        PediatricDoseBasis.PER_DOSE -> weightKg * orderedMgKg
        PediatricDoseBasis.PER_DAY -> dailyMg / dosesPerDay
    }
    return PediatricDoseResult(
        doseMg = doseMg,
        dailyMg = dailyMg,
        mlPerDose = doseMg / concentrationMgMl
    )
}

enum class LocalAnestheticId {
    LIDOCAINE_EPI,
    LIDOCAINE_PLAIN,
    MEPIVACAINE_PLAIN,
    ARTICAINE,
    BUPIVACAINE
}

data class LocalAnestheticSpec(
    val id: LocalAnestheticId,
    val nameEs: String,
    val nameEn: String,
    val concentrationPercent: Double,
    val conservativeMaxMgKg: Double,
    val epinephrineRatios: List<Int> = emptyList(),
    val minimumAgeYears: Int? = null,
    val cautionEs: String = "",
    val cautionEn: String = ""
)

val localAnestheticSpecsV26 = listOf(
    LocalAnestheticSpec(
        id = LocalAnestheticId.LIDOCAINE_EPI,
        nameEs = "Lidocaína con epinefrina",
        nameEn = "Lidocaine with epinephrine",
        concentrationPercent = 2.0,
        conservativeMaxMgKg = 4.4,
        epinephrineRatios = listOf(100_000, 50_000),
        cautionEs = "AAPD usa 4.4 mg/kg como máximo dental pediátrico conservador; la dosis máxima del fabricante puede ser mayor.",
        cautionEn = "AAPD uses 4.4 mg/kg as a conservative pediatric dental maximum; the manufacturer's maximum may be higher."
    ),
    LocalAnestheticSpec(
        id = LocalAnestheticId.LIDOCAINE_PLAIN,
        nameEs = "Lidocaína sola",
        nameEn = "Plain lidocaine",
        concentrationPercent = 2.0,
        conservativeMaxMgKg = 4.4,
        cautionEs = "Sin vasoconstrictor. Usa la menor dosis eficaz y verifica siempre la ficha local del producto.",
        cautionEn = "Without vasoconstrictor. Use the lowest effective dose and always verify the local product label."
    ),
    LocalAnestheticSpec(
        id = LocalAnestheticId.MEPIVACAINE_PLAIN,
        nameEs = "Mepivacaína sin vasoconstrictor",
        nameEn = "Plain mepivacaine",
        concentrationPercent = 3.0,
        conservativeMaxMgKg = 4.4,
        cautionEs = "AAPD incluye mepivacaína 3% sin vasoconstrictor y usa 4.4 mg/kg como máximo dental pediátrico conservador.",
        cautionEn = "AAPD lists plain 3% mepivacaine and uses 4.4 mg/kg as a conservative pediatric dental maximum."
    ),
    LocalAnestheticSpec(
        id = LocalAnestheticId.ARTICAINE,
        nameEs = "Articaína con epinefrina",
        nameEn = "Articaine with epinephrine",
        concentrationPercent = 4.0,
        conservativeMaxMgKg = 7.0,
        epinephrineRatios = listOf(100_000, 200_000),
        minimumAgeYears = 4,
        cautionEs = "AAPD incluye articaína 4% con epinefrina 1:100,000 o 1:200,000. No se recomienda en menores de 4 años.",
        cautionEn = "AAPD lists 4% articaine with 1:100,000 or 1:200,000 epinephrine. Use under 4 years of age is not recommended."
    ),
    LocalAnestheticSpec(
        id = LocalAnestheticId.BUPIVACAINE,
        nameEs = "Bupivacaína con epinefrina",
        nameEn = "Bupivacaine with epinephrine",
        concentrationPercent = 0.5,
        conservativeMaxMgKg = 1.3,
        epinephrineRatios = listOf(200_000),
        minimumAgeYears = 12,
        cautionEs = "AAPD incluye bupivacaína 0.5% con epinefrina 1:200,000 y máximo dental de 1.3 mg/kg. No se recomienda en menores de 12 años.",
        cautionEn = "AAPD lists 0.5% bupivacaine with 1:200,000 epinephrine and a 1.3 mg/kg dental maximum. Use under 12 years is not recommended."
    )
)

data class LocalAnestheticResult(
    val mgPerMl: Double,
    val mgPerCartridge: Double,
    val maxMgByWeight: Double,
    val maxCartridges: Double,
    val wholeCartridges: Int,
    val epinephrineMcgPerCartridge: Double?,
    val epinephrineMcgAtMaxCartridges: Double?
)

fun calculateLocalAnesthetic(
    spec: LocalAnestheticSpec,
    weightKg: Double,
    cartridgeMl: Double,
    epinephrineRatio: Int? = spec.epinephrineRatios.firstOrNull()
): LocalAnestheticResult? {
    if (weightKg <= 0.0 || cartridgeMl <= 0.0) return null
    val mgPerMl = spec.concentrationPercent * 10.0
    val mgPerCartridge = mgPerMl * cartridgeMl
    if (mgPerCartridge <= 0.0) return null
    val maxMg = weightKg * spec.conservativeMaxMgKg
    val maxCartridges = maxMg / mgPerCartridge
    val whole = floor(maxCartridges).toInt().coerceAtLeast(0)
    val epiMcgPerCartridge = epinephrineRatio?.takeIf { it > 0 }?.let { ratio ->
        val mcgPerMl = 1_000_000.0 / ratio.toDouble()
        mcgPerMl * cartridgeMl
    }
    return LocalAnestheticResult(
        mgPerMl = mgPerMl,
        mgPerCartridge = mgPerCartridge,
        maxMgByWeight = maxMg,
        maxCartridges = maxCartridges,
        wholeCartridges = whole,
        epinephrineMcgPerCartridge = epiMcgPerCartridge,
        epinephrineMcgAtMaxCartridges = epiMcgPerCartridge?.times(maxCartridges)
    )
}
