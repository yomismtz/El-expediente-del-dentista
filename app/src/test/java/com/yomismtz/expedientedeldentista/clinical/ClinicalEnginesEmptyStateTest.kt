package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClinicalEnginesEmptyStateTest {
    @Test fun emptyIpcIsNotHealthy() {
        assertTrue(EducationalSession().ipcCodes.isEmpty())
        assertEquals("", ClinicalEngines.ipcHighest(emptyList()))
    }

    @Test fun explicitHealthyIpcIsCodeZero() {
        assertEquals("0", ClinicalEngines.ipcHighest(List(6) { "0" }))
        assertEquals("Periodontalmente sano", ClinicalEngines.ipcInterpretation("0", "es"))
    }

    @Test fun heatAloneDoesNotForceEndodonticTreatment() {
        val s = EducationalSession(pulpal = PulpalAssessment(tooth = 16, heatPositive = true))
        val dx = ClinicalEngines.pulpalDiagnosis(s.pulpal)
        assertTrue(dx.pulpalEs.contains("no concluida"))
        assertNull(ClinicalEngines.derivedTreatmentPlanId(s, 16))
    }

    @Test fun necrosisEvidenceCanDeriveNecrosisPlan() {
        val s = EducationalSession(pulpal = PulpalAssessment(tooth = 16, sensitivityNegative = true))
        val dx = ClinicalEngines.pulpalDiagnosis(s.pulpal)
        assertTrue(dx.pulpalEs.contains("Necrosis pulpar"))
        assertEquals("necrosis", ClinicalEngines.derivedTreatmentPlanId(s, 16))
    }

    @Test fun emptyPulpalIsNotEvaluated() {
        val result = ClinicalEngines.pulpalDiagnosis(PulpalAssessment())
        assertTrue(result.pulpalEs.contains("no concluida"))
        assertTrue(result.apicalEs.contains("no concluida"))
    }

    @Test fun partialOLearyIsNotReportedAsZero() {
        val s = EducationalSession(presentTeeth = setOf(11, 12), oleary = mapOf(11 to emptySet()))
        assertNull(ClinicalEngines.olearyPercentageOrNull(s))
    }

    @Test fun explicitZeroOLearyRequiresEveryPresentTooth() {
        val s = EducationalSession(
            presentTeeth = setOf(11, 12),
            oleary = mapOf(11 to emptySet(), 12 to emptySet())
        )
        assertEquals(0.0, ClinicalEngines.olearyPercentageOrNull(s), 0.001)
    }

    @Test fun partialIhosIsNotReportedAsHealthy() {
        val s = EducationalSession(
            ihosDebris = mapOf(16 to 0),
            ihosCalculus = mapOf(16 to 0)
        )
        assertNull(ClinicalEngines.ihosOrNull(s))
    }

    @Test fun completeIhosCanBeZeroWhenAllSitesAreExplicitlyRecorded() {
        val teeth = listOf(16, 11, 26, 36, 31, 46)
        val s = EducationalSession(
            ihosDebris = teeth.associateWith { 0 },
            ihosCalculus = teeth.associateWith { 0 }
        )
        assertEquals(0.0, ClinicalEngines.ihosOrNull(s), 0.001)
    }

}
