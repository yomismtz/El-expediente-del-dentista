package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
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
        assertEquals(null, ClinicalEngines.derivedTreatmentPlanId(s, 16))
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
}
