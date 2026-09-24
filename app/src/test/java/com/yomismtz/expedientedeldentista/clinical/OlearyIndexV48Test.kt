package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Test

class OlearyIndexV48Test {

    @Test
    fun calculate_countsIndependentSurfacesOnSameTooth() {
        val present = setOf(11, 12)
        val plaque = mapOf(
            11 to setOf(Surface.VESTIBULAR, Surface.MESIAL, Surface.DISTAL),
            12 to setOf(Surface.LINGUAL_PALATAL)
        )

        val result = OlearyIndexV48.calculate(
            teethInScope = listOf(11, 12),
            presentTeeth = present,
            plaqueByTooth = plaque
        )

        assertEquals(4, result.plaqueSurfaces)
        assertEquals(8, result.examinedSurfaces)
        assertEquals(50.0, result.percentage, 0.0)
    }

    @Test
    fun calculate_ignoresOcclusalSurface() {
        val result = OlearyIndexV48.calculate(
            teethInScope = listOf(16),
            presentTeeth = setOf(16),
            plaqueByTooth = mapOf(
                16 to setOf(Surface.OCCLUSAL, Surface.VESTIBULAR)
            )
        )

        assertEquals(1, result.plaqueSurfaces)
        assertEquals(4, result.examinedSurfaces)
        assertEquals(25.0, result.percentage, 0.0)
    }

    @Test
    fun calculate_excludesMissingTeethFromDenominator() {
        val result = OlearyIndexV48.calculate(
            teethInScope = listOf(11, 12, 13),
            presentTeeth = setOf(11, 13),
            plaqueByTooth = mapOf(
                11 to setOf(Surface.VESTIBULAR),
                12 to setOf(Surface.MESIAL, Surface.DISTAL, Surface.VESTIBULAR, Surface.LINGUAL_PALATAL)
            )
        )

        assertEquals(1, result.plaqueSurfaces)
        assertEquals(8, result.examinedSurfaces)
        assertEquals(12.5, result.percentage, 0.0)
    }

    @Test
    fun permanentScope_canExcludeThirdMolars() {
        val withThirdMolars = OlearyIndexV48.permanentTeeth(includeThirdMolars = true)
        val withoutThirdMolars = OlearyIndexV48.permanentTeeth(includeThirdMolars = false)

        assertEquals(32, withThirdMolars.size)
        assertEquals(28, withoutThirdMolars.size)
        assertEquals(false, 18 in withoutThirdMolars)
        assertEquals(false, 28 in withoutThirdMolars)
        assertEquals(false, 38 in withoutThirdMolars)
        assertEquals(false, 48 in withoutThirdMolars)
    }
}
