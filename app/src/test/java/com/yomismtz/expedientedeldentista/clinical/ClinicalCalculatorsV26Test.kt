package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClinicalCalculatorsV26Test {
    @Test
    fun pediatricPerDoseCalculation() {
        val result = calculatePediatricDose(
            weightKg = 20.0,
            orderedMgKg = 10.0,
            concentrationMgMl = 20.0,
            dosesPerDay = 4,
            basis = PediatricDoseBasis.PER_DOSE
        )
        assertNotNull(result)
        assertEquals(200.0, result!!.doseMg, 0.001)
        assertEquals(800.0, result.dailyMg, 0.001)
        assertEquals(10.0, result.mlPerDose, 0.001)
    }

    @Test
    fun pediatricPerDayCalculation() {
        val result = calculatePediatricDose(
            weightKg = 20.0,
            orderedMgKg = 40.0,
            concentrationMgMl = 20.0,
            dosesPerDay = 4,
            basis = PediatricDoseBasis.PER_DAY
        )
        assertNotNull(result)
        assertEquals(200.0, result!!.doseMg, 0.001)
        assertEquals(800.0, result.dailyMg, 0.001)
        assertEquals(10.0, result.mlPerDose, 0.001)
    }

    @Test
    fun acetaminophenEveryFourHoursUsesMexico100MgMlAndDailyMaximum() {
        val medicine = pediatricMedicationSpecsV27.first { it.id == "PARACETAMOL" }
        val regimen = medicine.regimens.first()
        val presentation = medicine.presentations.first { it.id == "PARA_100_ML" }
        val result = calculatePediatricMedicationRangeV27(20.0, regimen, 4, presentation)

        assertNotNull(result)
        assertEquals(200.0, result!!.doseMinMg, 0.001)
        assertEquals(250.0, result.doseMaxMg, 0.001)
        assertEquals(1200.0, result.dailyMinMg, 0.001)
        assertEquals(1500.0, result.dailyMaxMg, 0.001)
        assertEquals(2.0, result.mlMin!!, 0.001)
        assertEquals(2.5, result.mlMax!!, 0.001)
    }

    @Test
    fun amoxicillinEveryTwelveHoursCalculatesMexicoSuspensionVolume() {
        val medicine = pediatricMedicationSpecsV27.first { it.id == "AMOXICILLIN" }
        val regimen = medicine.regimens.first { it.id == "AMOX_Q12" }
        val presentation = medicine.presentations.first { it.id == "AMOX_250_5" }
        val result = calculatePediatricMedicationRangeV27(20.0, regimen, 12, presentation)

        assertNotNull(result)
        assertEquals(250.0, result!!.doseMinMg, 0.001)
        assertEquals(450.0, result.doseMaxMg, 0.001)
        assertEquals(5.0, result.mlMin!!, 0.001)
        assertEquals(9.0, result.mlMax!!, 0.001)
        assertEquals(500.0, result.dailyMinMg, 0.001)
        assertEquals(900.0, result.dailyMaxMg, 0.001)
    }

    @Test
    fun amoxicillinClavulanateFourToOneOnlyUsesEveryEightHours() {
        val medicine = pediatricMedicationSpecsV27.first { it.id == "AMOX_CLAV_4_1" }
        val regimen = medicine.regimens.single()
        val presentation = medicine.presentations.first { it.id == "AMC_125_31_5" }

        assertEquals(listOf(8), regimen.intervalHours)
        assertNotNull(calculatePediatricMedicationRangeV27(20.0, regimen, 8, presentation))
        assertNull(calculatePediatricMedicationRangeV27(20.0, regimen, 12, presentation))
    }

    @Test
    fun amoxicillinClavulanateSevenToOneOnlyUsesEveryTwelveHours() {
        val medicine = pediatricMedicationSpecsV27.first { it.id == "AMOX_CLAV_7_1" }
        val regimen = medicine.regimens.single()
        val presentation = medicine.presentations.first { it.id == "AMC_400_57_5" }
        val result = calculatePediatricMedicationRangeV27(20.0, regimen, 12, presentation)

        assertEquals(listOf(12), regimen.intervalHours)
        assertNotNull(result)
        assertEquals(3.125, result!!.mlMin!!, 0.001)
        assertEquals(5.625, result.mlMax!!, 0.001)
        assertNull(calculatePediatricMedicationRangeV27(20.0, regimen, 8, presentation))
    }

    @Test
    fun specialSixHundredFortyTwoNineClavulanateFormulationIsNotSilentlyIncluded() {
        val allPresentationIds = pediatricMedicationSpecsV27.flatMap { it.presentations }.map { it.id }
        assertFalse(allPresentationIds.any { it.contains("600_42") })
    }

    @Test
    fun mexicoVerifiedCephalexinAndAntiviralPresentationsExist() {
        val cephalexin = pediatricMedicationSpecsV27.first { it.id == "CEPHALEXIN" }
        assertTrue(cephalexin.presentations.any { it.id == "CEPH_125_5" && it.mgPerMl == 25.0 })

        val acyclovir = pediatricMedicationSpecsV27.first { it.id == "ACYCLOVIR" }
        assertTrue(acyclovir.presentations.any { it.id == "ACY_TAB_200" && it.mgPerUnit == 200.0 })

        val valacyclovir = pediatricMedicationSpecsV27.first { it.id == "VALACYCLOVIR" }
        assertTrue(valacyclovir.presentations.any { it.id == "VALA_TAB_1000" && it.mgPerUnit == 1000.0 })
    }

    @Test
    fun topicalAcyclovirDoesNotRunWeightBasedCalculation() {
        val medicine = pediatricMedicationSpecsV27.first { it.id == "ACYCLOVIR" }
        val regimen = medicine.regimens.first()
        val presentation = medicine.presentations.first { it.id == "ACY_CREAM_5" }

        assertNull(calculatePediatricMedicationRangeV27(20.0, regimen, 6, presentation))
    }

    @Test
    fun lidocaineUsesActualCartridgeVolume() {
        val spec = localAnestheticSpecsV26.first { it.id == LocalAnestheticId.LIDOCAINE_EPI }
        val result = calculateLocalAnesthetic(spec, 20.0, 1.8, 100_000)
        assertNotNull(result)
        assertEquals(20.0, result!!.mgPerMl, 0.001)
        assertEquals(36.0, result.mgPerCartridge, 0.001)
        assertEquals(88.0, result.maxMgByWeight, 0.001)
        assertEquals(2.4444, result.maxCartridges, 0.001)
        assertEquals(2, result.wholeCartridges)
        assertEquals(18.0, result.epinephrineMcgPerCartridge!!, 0.001)
    }

    @Test
    fun articaineCalculation() {
        val spec = localAnestheticSpecsV26.first { it.id == LocalAnestheticId.ARTICAINE }
        val result = calculateLocalAnesthetic(spec, 20.0, 1.8, 100_000)
        assertNotNull(result)
        assertEquals(40.0, result!!.mgPerMl, 0.001)
        assertEquals(72.0, result.mgPerCartridge, 0.001)
        assertEquals(140.0, result.maxMgByWeight, 0.001)
        assertEquals(1.9444, result.maxCartridges, 0.001)
        assertEquals(1, result.wholeCartridges)
    }

    @Test
    fun bupivacaineUsesAapdDentalMaximum() {
        val spec = localAnestheticSpecsV26.first { it.id == LocalAnestheticId.BUPIVACAINE }
        val result = calculateLocalAnesthetic(spec, 40.0, 1.8, 200_000)
        assertNotNull(result)
        assertEquals(5.0, result!!.mgPerMl, 0.001)
        assertEquals(9.0, result.mgPerCartridge, 0.001)
        assertEquals(52.0, result.maxMgByWeight, 0.001)
        assertEquals(5.7777, result.maxCartridges, 0.001)
        assertEquals(5, result.wholeCartridges)
        assertEquals(9.0, result.epinephrineMcgPerCartridge!!, 0.001)
    }
}
