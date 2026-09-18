package com.yomismtz.expedientedeldentista.clinical

import kotlin.math.round

data class OlearyResultV48(
    val plaqueSurfaces: Int,
    val examinedSurfaces: Int,
    val percentage: Double
)

/**
 * Pure O'Leary Plaque Control Record calculation.
 *
 * The record is binary by surface and uses four surfaces per evaluable tooth:
 * vestibular/buccal, lingual/palatal, mesial and distal. Occlusal surfaces are
 * intentionally excluded.
 */
object OlearyIndexV48 {

    val surfaces: Set<Surface> = setOf(
        Surface.VESTIBULAR,
        Surface.LINGUAL_PALATAL,
        Surface.MESIAL,
        Surface.DISTAL
    )

    fun permanentTeeth(includeThirdMolars: Boolean): List<Int> =
        if (includeThirdMolars) {
            ClinicalContent.permanentTeeth
        } else {
            ClinicalContent.permanentTeeth.filter { it % 10 != 8 }
        }

    fun calculate(
        teethInScope: Collection<Int>,
        presentTeeth: Set<Int>,
        plaqueByTooth: Map<Int, Set<Surface>>
    ): OlearyResultV48 {
        val evaluable = teethInScope.filter { it in presentTeeth }
        val plaque = evaluable.sumOf { tooth ->
            plaqueByTooth[tooth].orEmpty().count { it in surfaces }
        }
        val total = evaluable.size * surfaces.size
        val percentage = if (total == 0) {
            0.0
        } else {
            round((plaque * 1000.0 / total)) / 10.0
        }
        return OlearyResultV48(
            plaqueSurfaces = plaque,
            examinedSurfaces = total,
            percentage = percentage
        )
    }
}
