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
    MEPIVACAINE_EPI,
    ARTICAINE
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
        nameEs = "Mepivacaína",
        nameEn = "Mepivacaine",
        concentrationPercent = 3.0,
        conservativeMaxMgKg = 4.4,
        cautionEs = "La presentación dental sin vasoconstrictor suele ser 3%. AAPD usa 4.4 mg/kg como máximo dental pediátrico conservador.",
        cautionEn = "The plain dental formulation is commonly 3%. AAPD uses 4.4 mg/kg as a conservative pediatric dental maximum."
    ),
    LocalAnestheticSpec(
        id = LocalAnestheticId.MEPIVACAINE_EPI,
        nameEs = "Mepivacaína con epinefrina",
        nameEn = "Mepivacaine with epinephrine",
        concentrationPercent = 2.0,
        conservativeMaxMgKg = 4.4,
        epinephrineRatios = listOf(100_000),
        cautionEs = "Existen presentaciones internacionales 2% con epinefrina 1:100,000, pero en otros mercados la mepivacaína 2% se combina con levonordefrina. Verifica el cartucho disponible antes de usar el cálculo.",
        cautionEn = "International 2% + 1:100,000 epinephrine presentations exist, while other markets use 2% mepivacaine with levonordefrin. Verify the actual cartridge before using the calculation."
    ),
    LocalAnestheticSpec(
        id = LocalAnestheticId.ARTICAINE,
        nameEs = "Articaína",
        nameEn = "Articaine",
        concentrationPercent = 4.0,
        conservativeMaxMgKg = 7.0,
        epinephrineRatios = listOf(100_000, 200_000),
        minimumAgeYears = 4,
        cautionEs = "Las presentaciones dentales de referencia contienen epinefrina 1:100,000 o 1:200,000. No se recomienda en menores de 4 años.",
        cautionEn = "Reference dental formulations contain epinephrine 1:100,000 or 1:200,000. Use under 4 years of age is not recommended."
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
