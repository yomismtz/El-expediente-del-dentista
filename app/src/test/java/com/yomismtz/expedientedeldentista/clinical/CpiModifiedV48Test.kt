package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CpiModifiedV48Test {

    @Test
    fun summary_doesNotTreatUnrecordedTeethAsHealthy() {
        val present = setOf(11, 12, 13)
        val result = CpiModifiedV48.summarize(
            presentTeeth = present,
            bleeding = mapOf(11 to 0, 12 to 1),
            pockets = mapOf(11 to 0, 12 to 1),
            recordPockets = true
        )

        assertEquals(3, result.presentTeeth)
        assertEquals(2, result.bleedingRecorded)
        assertEquals(1, result.bleedingPositive)
        assertEquals(2, result.pocketRecorded)
        assertFalse(result.complete)
    }

    @Test
    fun summary_countsPocketCategoriesSeparately() {
        val present = setOf(11, 12, 13, 14)
        val result = CpiModifiedV48.summarize(
            presentTeeth = present,
            bleeding = mapOf(11 to 0, 12 to 1, 13 to 0, 14 to 9),
            pockets = mapOf(11 to 0, 12 to 1, 13 to 2, 14 to 9),
            recordPockets = true
        )

        assertEquals(1, result.pocket45)
        assertEquals(1, result.pocket6Plus)
        assertEquals(1, result.excludedTeeth)
        assertTrue(result.complete)
    }

    @Test
    fun under15_canBeCompleteWithoutPocketRecording() {
        val present = setOf(11, 12)
        val result = CpiModifiedV48.summarize(
            presentTeeth = present,
            bleeding = mapOf(11 to 0, 12 to 1),
            pockets = emptyMap(),
            recordPockets = false
        )

        assertEquals(2, result.bleedingRecorded)
        assertEquals(0, result.pocketRecorded)
        assertTrue(result.complete)
    }

    @Test
    fun sextants_matchFdiRanges() {
        assertEquals(listOf(18,17,16,15,14), CpiModifiedV48.sextants[0])
        assertEquals(listOf(13,12,11,21,22,23), CpiModifiedV48.sextants[1])
        assertEquals(listOf(44,45,46,47,48), CpiModifiedV48.sextants[5])
    }
}
