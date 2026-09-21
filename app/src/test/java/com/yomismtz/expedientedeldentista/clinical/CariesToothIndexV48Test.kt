package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Test

class CariesToothIndexV48Test {

    @Test
    fun cpod_separatesCariesMissingAndFilled_andExcludesOtherMissing() {
        val records = mapOf(
            16 to ToothRecord(status = ToothStatus.CARIES),
            26 to ToothRecord(status = ToothStatus.EXTRACTION_INDICATED),
            36 to ToothRecord(status = ToothStatus.MISSING_CARIES),
            46 to ToothRecord(status = ToothStatus.RESTORED),
            11 to ToothRecord(status = ToothStatus.MISSING_OTHER),
            12 to ToothRecord(status = ToothStatus.SEALANT)
        )

        val result = CariesToothIndexV48.cpod(records, includeThirdMolars = true)

        assertEquals(2, result.carious)
        assertEquals(1, result.missing)
        assertEquals(1, result.filled)
        assertEquals(4, result.total)
    }

    @Test
    fun cpod_canExcludeThirdMolarsWithoutChangingOtherTeeth() {
        val records = mapOf(
            18 to ToothRecord(status = ToothStatus.CARIES),
            28 to ToothRecord(status = ToothStatus.RESTORED),
            16 to ToothRecord(status = ToothStatus.CARIES)
        )

        val withThirdMolars = CariesToothIndexV48.cpod(records, includeThirdMolars = true)
        val withoutThirdMolars = CariesToothIndexV48.cpod(records, includeThirdMolars = false)

        assertEquals(3, withThirdMolars.total)
        assertEquals(1, withoutThirdMolars.total)
        assertEquals(1, withoutThirdMolars.carious)
    }

    @Test
    fun ceod_countsExtractionIndicatedAsE_andDoesNotCountAbsentPrimaryTooth() {
        val records = mapOf(
            55 to ToothRecord(status = ToothStatus.CARIES),
            54 to ToothRecord(status = ToothStatus.EXTRACTION_INDICATED),
            53 to ToothRecord(status = ToothStatus.RESTORED),
            52 to ToothRecord(status = ToothStatus.MISSING_CARIES),
            51 to ToothRecord(status = ToothStatus.MISSING_OTHER)
        )

        val result = CariesToothIndexV48.ceod(records)

        assertEquals(1, result.carious)
        assertEquals(1, result.missing)
        assertEquals(1, result.filled)
        assertEquals(3, result.total)
    }
}
