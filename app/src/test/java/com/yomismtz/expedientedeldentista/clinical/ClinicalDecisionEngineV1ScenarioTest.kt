package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Scenario coverage for the clinical decision traffic light.
 * These are decision-support guardrails, not a substitute for clinical validation.
 */
class ClinicalDecisionEngineV1ScenarioTest {
    private val baselineProfile = PatientProfile(weightKg = "70")

    private fun evaluate(
        age: Int = 35,
        sys: Int? = 120,
        dia: Int? = 80,
        rr: Int? = 16,
        hr: Int? = 72,
        spo2: Int? = 98,
        glucose: Int? = 100,
        temp: Double? = 36.7,
        signs: Set<String> = emptySet(),
        pain: Int? = 0,
        profile: PatientProfile = baselineProfile
    ) = evaluateClinicalDecisionV1(
        age = age,
        sex = "no especificado",
        sys = sys,
        dia = dia,
        rr = rr,
        hr = hr,
        spo2 = spo2,
        glucose = glucose,
        temp = temp,
        bmi = 23.0,
        signs = signs,
        pain = pain,
        profile = profile
    )

    @Test
    fun normalScenarioReturnsGreenWithoutEmergencyRestrictions() {
        val result = evaluate()
        assertEquals("GREEN", result.status)
        assertTrue(result.treatments.isNotEmpty())
        assertTrue(result.alerts.isEmpty())
        assertFalse(result.anestheticEs.contains("NO seleccionar ni administrar"))
    }

    @Test
    fun abnormalBloodPressureReturnsYellowAndAddsPrecaution() {
        val result = evaluate(sys = 165, dia = 102)
        assertEquals("YELLOW", result.status)
        assertTrue(result.alerts.any { it.severity == "YELLOW" && it.triggerEs.contains("165/102") })
        assertTrue(result.restrictionsEs.any { it.contains("diferir") || it.contains("Confirmar") })
    }

    @Test
    fun persistentOralLesionReturnsYellowAndReferralCaution() {
        val result = evaluate(signs = setOf("Úlcera o lesión >2 semanas"))
        assertEquals("YELLOW", result.status)
        assertTrue(result.alerts.any { it.triggerEs.contains("Lesión oral persistente") })
        assertTrue(result.restrictionsEs.any { it.contains("Lesión persistente") })
    }

    @Test
    fun airwayWarningReturnsRedNoElectiveTreatmentAndNoAnestheticClearance() {
        val result = evaluate(signs = setOf("Disnea"))
        assertEquals("RED", result.status)
        assertTrue(result.treatments.isEmpty())
        assertTrue(result.restrictionsEs.any { it.contains("vía aérea") })
        assertTrue(result.anestheticEs.contains("NO seleccionar ni administrar"))
        assertTrue(result.alerts.any { it.severity == "RED" })
    }

    @Test
    fun severeHypoxemiaReturnsRed() {
        val result = evaluate(spo2 = 88)
        assertEquals("RED", result.status)
        assertTrue(result.treatments.isEmpty())
        assertTrue(result.alerts.any { it.severity == "RED" && it.triggerEs.contains("88") })
    }

    @Test
    fun hypoglycemiaReturnsRedAndRequiresReassessment() {
        val result = evaluate(glucose = 58)
        assertEquals("RED", result.status)
        assertTrue(result.treatments.isEmpty())
        assertTrue(result.restrictionsEs.any { it.contains("hipoglucemia") })
        assertTrue(result.alerts.any { it.severity == "RED" && it.triggerEs.contains("58") })
    }

    @Test
    fun uncontrolledOralBleedingReturnsRed() {
        val result = evaluate(signs = setOf("Sangrado oral no controlable"))
        assertEquals("RED", result.status)
        assertTrue(result.treatments.isEmpty())
        assertTrue(result.restrictionsEs.any { it.contains("hemostasia/urgencia") })
    }

    @Test
    fun emergencyClassificationOverridesAbnormalButNonEmergencyFindings() {
        val result = evaluate(
            sys = 165,
            dia = 102,
            spo2 = 88,
            signs = setOf("Úlcera o lesión >2 semanas")
        )
        assertEquals("RED", result.status)
        assertTrue(result.alerts.first().severity == "RED")
        assertTrue(result.treatments.isEmpty())
    }
}
