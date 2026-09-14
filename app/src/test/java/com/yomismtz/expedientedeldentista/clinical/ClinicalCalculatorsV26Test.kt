package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    fun mepivacaineWithEpinephrineCalculatesVasoconstrictor() {
        val spec = localAnestheticSpecsV26.first { it.id == LocalAnestheticId.MEPIVACAINE_EPI }
        val result = calculateLocalAnesthetic(spec, 20.0, 1.8, 100_000)
        assertNotNull(result)
        assertEquals(20.0, result!!.mgPerMl, 0.001)
        assertEquals(36.0, result.mgPerCartridge, 0.001)
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
}
