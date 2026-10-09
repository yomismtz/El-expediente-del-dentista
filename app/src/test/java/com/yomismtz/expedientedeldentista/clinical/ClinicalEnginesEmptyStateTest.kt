package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClinicalEnginesEmptyStateTest {
    @Test fun medicalHistoryNegativeIsDistinctFromUnrecorded() {
        val unrecorded = DiseaseAnswer()
        val explicitNegative = DiseaseAnswer(recorded = true)
        val positive = DiseaseAnswer(present = true, recorded = true)

        assertTrue(!unrecorded.recorded)
        assertTrue(explicitNegative.recorded)
        assertTrue(!explicitNegative.present)
        assertTrue(positive.recorded)
        assertTrue(positive.present)
    }

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
        assertEquals(0.0, requireNotNull(ClinicalEngines.olearyPercentageOrNull(s)), 0.001)
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
        assertEquals(0.0, requireNotNull(ClinicalEngines.ihosOrNull(s)), 0.001)
    }

    @Test fun periodontalZeroIsOnlyClinicalWhenExplicitlyRecorded() {
        val pending = PerioRecord()
        val explicitZero = PerioRecord(
            probingDepths = List(6) { 0 },
            probingDepthRecordedSites = (0..5).toSet(),
            mobility = 0,
            mobilityRecorded = true,
            furcation = 0,
            furcationRecorded = true,
            recessionBySite = List(6) { 0 },
            recessionRecordedSites = (0..5).toSet()
        )

        assertTrue(pending.probingDepthRecordedSites.isEmpty())
        assertTrue(!pending.mobilityRecorded)
        assertTrue(!pending.furcationRecorded)
        assertTrue(pending.recessionRecordedSites.isEmpty())

        assertEquals((0..5).toSet(), explicitZero.probingDepthRecordedSites)
        assertTrue(explicitZero.mobilityRecorded)
        assertTrue(explicitZero.furcationRecorded)
        assertEquals((0..5).toSet(), explicitZero.recessionRecordedSites)
    }

    @Test fun birthDateValidationRejectsInvalidAndFutureDates() {
        val today = java.time.LocalDate.of(2026, 10, 8)
        assertEquals(BirthDateStatus.EMPTY, validateBirthDate("", today))
        assertEquals(BirthDateStatus.VALID, validateBirthDate("08/10/2000", today))
        assertEquals(BirthDateStatus.VALID, validateBirthDate("2000-10-08", today))
        assertEquals(BirthDateStatus.INVALID_FORMAT, validateBirthDate("08-10-2000", today))
        assertEquals(BirthDateStatus.INVALID_DATE, validateBirthDate("31/02/2000", today))
        assertEquals(BirthDateStatus.FUTURE, validateBirthDate("09/10/2026", today))
    }

    @Test fun ageCalculationAndConsistencyWork() {
        val today = java.time.LocalDate.of(2026, 10, 8)
        assertEquals(26, calculateAgeYears("08/10/2000", today))
        assertEquals(AgeConsistencyStatus.VALID, validateAgeConsistency("08/10/2000", "26", today))
        assertEquals(AgeConsistencyStatus.MISMATCH, validateAgeConsistency("08/10/2000", "25", today))
        assertEquals(AgeConsistencyStatus.INVALID_RECORDED_AGE, validateAgeConsistency("08/10/2000", "abc", today))
    }

    @Test fun sexValidationRequiresExplicitRecordedValue() {
        assertEquals(SexRecordStatus.EMPTY, validateRecordedSex(""))
        assertEquals(SexRecordStatus.RECORDED, validateRecordedSex("referido por paciente"))
    }

    @Test fun asaDefaultIsNotMistakenForClinicianAssessment() {
        assertEquals(1, EducationalSession().history.asaClass)
        assertEquals(null, EducationalSession().history.asaClassClinician)
        assertEquals(AsaRecordStatus.NOT_ASSESSED, validateClinicianAsaClass(null))
    }

    @Test fun clinicianAsaRequiresValidClassFromOneToSix() {
        assertEquals(AsaRecordStatus.VALID, validateClinicianAsaClass(1))
        assertEquals(AsaRecordStatus.VALID, validateClinicianAsaClass(6))
        assertEquals(AsaRecordStatus.INVALID, validateClinicianAsaClass(0))
        assertEquals(AsaRecordStatus.INVALID, validateClinicianAsaClass(7))
    }


@Test
fun emptyOdontogramIsNotConsideredComplete() {
    val result = validateOdontogramCompleteness(EducationalSession())
    assertEquals(OdontogramCompletenessStatus.EMPTY, result.status)
    assertEquals(32, result.missingPermanent.size)
    assertEquals(20, result.missingPrimary.size)
}

@Test
fun partialPermanentOdontogramIsDetected() {
    val teeth = (setOf(11, 12, 13, 14, 15, 16, 17, 18)).associateWith { ToothRecord() }
    val result = validateOdontogramCompleteness(EducationalSession(teeth = teeth))
    assertEquals(OdontogramCompletenessStatus.PARTIAL_PERMANENT, result.status)
    assertEquals(8, result.registeredPermanent)
    assertEquals(24, result.missingPermanent.size)
}

@Test
fun completePermanentOdontogramIsAccepted() {
    val teeth = listOf(
        11,12,13,14,15,16,17,18,21,22,23,24,25,26,27,28,
        31,32,33,34,35,36,37,38,41,42,43,44,45,46,47,48
    ).associateWith { ToothRecord() }
    val result = validateOdontogramCompleteness(EducationalSession(teeth = teeth))
    assertEquals(OdontogramCompletenessStatus.COMPLETE_PERMANENT, result.status)
    assertEquals(32, result.registeredPermanent)
    assertTrue(result.missingPermanent.isEmpty())
}

@Test
fun mixedDentitionIsNotSilentlyDeclaredComplete() {
    val teeth = mapOf(11 to ToothRecord(), 51 to ToothRecord())
    val result = validateOdontogramCompleteness(EducationalSession(teeth = teeth))
    assertEquals(OdontogramCompletenessStatus.MIXED_DENTITION, result.status)
}

}
