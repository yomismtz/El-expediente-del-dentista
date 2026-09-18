package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Test

class IhosIndexV48Test {

    @Test
    fun calculate_usesOnlySitesWithBothDebrisAndCalculusScores() {
        val result = IhosIndexV48.calculate(
            examinedTeeth = listOf(16, 11, 26),
            debrisScores = mapOf(16 to 1, 11 to 2, 26 to 3),
            calculusScores = mapOf(16 to 0, 11 to 1)
        )

        assertEquals(2, result.examinedSites)
        assertEquals(1.5, result.debrisAverage, 0.0)
        assertEquals(0.5, result.calculusAverage, 0.0)
        assertEquals(2.0, result.total, 0.0)
    }

    @Test
    fun calculate_preservesLegitimateZeroScores() {
        val result = IhosIndexV48.calculate(
            examinedTeeth = listOf(16, 11),
            debrisScores = mapOf(16 to 0, 11 to 0),
            calculusScores = mapOf(16 to 0, 11 to 0)
        )

        assertEquals(2, result.examinedSites)
        assertEquals(0.0, result.debrisAverage, 0.0)
        assertEquals(0.0, result.calculusAverage, 0.0)
        assertEquals(0.0, result.total, 0.0)
    }

    @Test
    fun calculate_clampsScoresToZeroThroughThree() {
        val result = IhosIndexV48.calculate(
            examinedTeeth = listOf(16, 11),
            debrisScores = mapOf(16 to 9, 11 to -1),
            calculusScores = mapOf(16 to 4, 11 to 2)
        )

        assertEquals(1.5, result.debrisAverage, 0.0)
        assertEquals(2.5, result.calculusAverage, 0.0)
        assertEquals(4.0, result.total, 0.0)
    }

    @Test
    fun individualScore_requiresAtLeastTwoExaminedSites() {
        assertEquals(false, IhosIndexV48.hasMinimumSites(0))
        assertEquals(false, IhosIndexV48.hasMinimumSites(1))
        assertEquals(true, IhosIndexV48.hasMinimumSites(2))
        assertEquals(true, IhosIndexV48.hasMinimumSites(6))
    }

    @Test
    fun interpretation_usesClassicOhiSThresholds() {
        assertEquals("Buena", IhosIndexV48.interpretationEs(1.2))
        assertEquals("Regular", IhosIndexV48.interpretationEs(1.3))
        assertEquals("Regular", IhosIndexV48.interpretationEs(3.0))
        assertEquals("Mala", IhosIndexV48.interpretationEs(3.1))
    }
}
