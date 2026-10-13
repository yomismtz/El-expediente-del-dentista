package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClinicalIndexAuditTest {
    @Test
    fun thirdMolarIsNotUsedAsAutomaticOhiSSubstitute() {
        val scores = mapOf(18 to 0, 11 to 0, 26 to 0, 36 to 0, 31 to 0, 46 to 0)
        val session = EducationalSession(
            teeth = mapOf(
                16 to ToothRecord(status = ToothStatus.MISSING_OTHER),
                17 to ToothRecord(status = ToothStatus.MISSING_OTHER),
                18 to ToothRecord(status = ToothStatus.HEALTHY)
            ),
            ihosDebris = scores,
            ihosCalculus = scores
        )
        assertNull(ClinicalEngines.ihosOrNull(session))
    }

    @Test
    fun cpodInterpretationDoesNotApplyUnsupportedUniversalSeverityBands() {
        val result = ClinicalEngines.cpodInterpretation(7, "es")
        assertTrue(result.contains("7 dientes"))
        assertTrue(!result.contains("Muy alta"))
    }
}
