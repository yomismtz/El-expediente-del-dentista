package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Test

class ClinicalEnginesTest {
    @Test
    fun ihos_usesSelectedSubstituteAndExcludedSlots() {
        val session = EducationalSession(
            ihosDebris = mapOf(17 to 2, 46 to 3),
            ihosCalculus = mapOf(17 to 1, 46 to 3),
            ihosSelections = mapOf(16 to 17),
            ihosExcludedSlots = setOf(46)
        )

        val result = ClinicalEngines.ihos(session)

        assertEquals(0.6, result, 0.0001)
    }

    @Test
    fun ipcHighest_returnsHighestNumericCode() {
        assertEquals("4", ClinicalEngines.ipcHighest(listOf("0", "2", "4", "X", "1", "3")))
    }

    @Test
    fun cpod_countsPermanentCariesMissingAndFilled() {
        val result = ClinicalEngines.cpod(
            teeth = mapOf(
                11 to ToothRecord(status = ToothStatus.CARIES),
                12 to ToothRecord(status = ToothStatus.MISSING_CARIES),
                13 to ToothRecord(status = ToothStatus.RESTORED)
            ),
            primary = false
        )

        assertEquals(1, result.carious)
        assertEquals(1, result.missing)
        assertEquals(1, result.filled)
        assertEquals(3, result.total)
    }
}
