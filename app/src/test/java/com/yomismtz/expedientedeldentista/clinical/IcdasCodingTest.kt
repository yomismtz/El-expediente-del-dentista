package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.*
import org.junit.Test
import com.yomismtz.expedientedeldentista.clinical.auditOdontogram

class IcdasCodingTest {
    @Test fun clearAndSpecialSemanticsAreRepresentable() {
        val session = EducationalSession(
            icdasSurfaceRecords = mapOf(16 to mapOf(Surface.OCCLUSAL to IcdasSurfaceRecord(restorationCode = 4, cariesCode = 6)))
        )
        assertTrue(session.icdasSurfaceRecords[16]?.containsKey(Surface.OCCLUSAL) == true)
        assertEquals(46, session.icdasSurfaceRecords[16]?.get(Surface.OCCLUSAL)?.combinedCode)
        assertEquals(90, IcdasCoding.fromCombined(90)?.specialCode)
        assertEquals(97, IcdasCoding.fromCombined(97)?.specialCode)
    }

    @Test fun soundUnrestoredIs00() {
        assertEquals(0, IcdasCoding.combine(0, 0))
        assertEquals(0, IcdasCoding.fromCombined(0)?.combinedCode)
    }

    @Test fun cariesCodes01Through06AreValidWithUnrestoredSurface() {
        (1..6).forEach { caries ->
            assertTrue(IcdasCoding.isValidCombined(caries))
            assertEquals(caries, IcdasCoding.combine(0, caries))
        }
    }

    @Test fun representativeRestorationCombinationsAreValid() {
        assertEquals(11, IcdasCoding.combine(1, 1))
        assertEquals(20, IcdasCoding.combine(2, 0))
        assertEquals(30, IcdasCoding.combine(3, 0))
        assertEquals(46, IcdasCoding.combine(4, 6))
        assertEquals(80, IcdasCoding.combine(8, 0))
        assertTrue(IcdasCoding.isValidCombined(86))
    }

    @Test fun invalidCombinationsAreRejected() {
        assertFalse(IcdasCoding.isValidCombined(94))
        assertFalse(IcdasCoding.isValidCombined(95))
        assertFalse(IcdasCoding.isValidCombined(89))
        assertTrue(IcdasCoding.isValidCombined(91))
        assertFalse(IcdasCoding.isValidCombined(87))
    }

    @Test fun specialCodesMatchOfficialIcdasSet() {
        assertEquals(
            setOf(90, 91, 92, 93, 96, 97, 98, 99),
            IcdasCoding.specialCodes
        )
        assertFalse(IcdasCoding.isValidSpecial(94))
        assertFalse(IcdasCoding.isValidSpecial(95))
    }

    @Test fun specialCodeMeaningsRemainDistinctFromCariesDigit() {
        assertEquals(92, IcdasCoding.fromCombined(92)?.specialCode)
        assertNull(IcdasCoding.fromCombined(94))
        assertEquals(96, IcdasCoding.fromCombined(96)?.specialCode)
        assertEquals(97, IcdasCoding.fromCombined(97)?.specialCode)
        assertEquals(98, IcdasCoding.fromCombined(98)?.specialCode)
        assertEquals(99, IcdasCoding.fromCombined(99)?.specialCode)
    }

    @Test fun legacySingleDigitMigrationDoesNotInventRestorationStatus() {
        val migrated = IcdasCoding.fromLegacy(4)
        assertNotNull(migrated)
        assertEquals(4, migrated!!.cariesCode)
        assertNull(migrated.restorationCode)
        assertTrue(migrated.legacyPending)
        assertEquals(4, migrated.combinedCode)
    }

    @Test fun legacyTwoDigitMigrationSeparatesComponents() {
        val migrated = IcdasCoding.fromLegacy(46)
        assertEquals(4, migrated?.restorationCode)
        assertEquals(6, migrated?.cariesCode)
        assertFalse(migrated!!.legacyPending)
    }

    @Test fun legacySpecialCodesMigrateWithoutReinterpretation() {
        listOf(90, 91, 92, 93, 96, 97, 98, 99).forEach { code ->
            assertEquals(code, IcdasCoding.fromLegacy(code)?.specialCode)
        }
    }

    @Test fun odontogramAuditDetectsMissingToothWithIcdasAndSurfaceDisagreement() {
        val session = EducationalSession(
            teeth = mapOf(16 to ToothRecord(status = ToothStatus.MISSING_OTHER)),
            odontogramSurfaces = mapOf(16 to mapOf(Surface.OCCLUSAL to SurfaceMark.CARIES)),
            icdasSurfaceRecords = mapOf(16 to mapOf(Surface.OCCLUSAL to IcdasSurfaceRecord(restorationCode = 0, cariesCode = 0)))
        )
        val issues = auditOdontogram(session)
        assertTrue(issues.any { it.code == "MISSING_WITH_SURFACE_MARKS" })
        assertTrue(issues.any { it.code == "TOOTH_STATUS_WITH_ICDAS" })
        assertTrue(issues.any { it.code == "CARIES_SURFACE_ICDAS_SOUND" })
    }

    @Test fun uneruptedToothIsExplicitAndAuditedAgainstIcdas() {
        val session = EducationalSession(
            teeth = mapOf(18 to ToothRecord(status = ToothStatus.UNERUPTED)),
            icdasSurfaceRecords = mapOf(18 to mapOf(Surface.OCCLUSAL to IcdasSurfaceRecord(specialCode = 99)))
        )
        assertTrue(auditOdontogram(session).none { it.code == "TOOTH_STATUS_WITH_ICDAS" })
    }

    @Test fun missingToothWithMatchingIcdas97IsConsistent() {
        val session = EducationalSession(
            teeth = mapOf(16 to ToothRecord(status = ToothStatus.MISSING_CARIES)),
            icdasSurfaceRecords = mapOf(16 to mapOf(Surface.OCCLUSAL to IcdasSurfaceRecord(specialCode = 97)))
        )
        assertTrue(auditOdontogram(session).none { it.code == "TOOTH_STATUS_WITH_ICDAS" })
    }

}
