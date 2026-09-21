package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Test

class CariesSurfaceIndexV48Test {

    @Test
    fun cpos_countsMultipleSurfacesOnSameTooth_andMissingPermanentAsAllEligibleSurfaces() {
        val records = mapOf(
            16 to ToothRecord(status = ToothStatus.CARIES),
            11 to ToothRecord(status = ToothStatus.RESTORED),
            36 to ToothRecord(status = ToothStatus.MISSING_CARIES),
            46 to ToothRecord(status = ToothStatus.MISSING_OTHER)
        )
        val surfaces = mapOf(
            16 to mapOf(
                Surface.VESTIBULAR to SurfaceMark.CARIES,
                Surface.OCCLUSAL to SurfaceMark.CARIES,
                Surface.MESIAL to SurfaceMark.RESTORATION,
                Surface.DISTAL to SurfaceMark.SEALANT
            ),
            11 to mapOf(
                Surface.VESTIBULAR to SurfaceMark.CARIES,
                Surface.LINGUAL_PALATAL to SurfaceMark.RESTORATION
            )
        )

        val result = CariesSurfaceIndexV48.cpos(records, surfaces, includeThirdMolars = true)

        assertEquals(3, result.carious)
        assertEquals(5, result.missing)
        assertEquals(2, result.filled)
        assertEquals(10, result.total)
    }

    @Test
    fun cpos_ignoresOcclusalOnAnteriorTeeth() {
        val surfaces = mapOf(
            11 to mapOf(
                Surface.OCCLUSAL to SurfaceMark.CARIES,
                Surface.MESIAL to SurfaceMark.CARIES
            )
        )

        val result = CariesSurfaceIndexV48.cpos(
            records = emptyMap(),
            surfaces = surfaces,
            includeThirdMolars = true
        )

        assertEquals(1, result.carious)
        assertEquals(1, result.total)
    }

    @Test
    fun cpos_canExcludeThirdMolarSurfaces() {
        val surfaces = mapOf(
            18 to mapOf(Surface.OCCLUSAL to SurfaceMark.CARIES),
            16 to mapOf(Surface.OCCLUSAL to SurfaceMark.CARIES)
        )

        val withThirdMolars = CariesSurfaceIndexV48.cpos(emptyMap(), surfaces, includeThirdMolars = true)
        val withoutThirdMolars = CariesSurfaceIndexV48.cpos(emptyMap(), surfaces, includeThirdMolars = false)

        assertEquals(2, withThirdMolars.carious)
        assertEquals(1, withoutThirdMolars.carious)
    }

    @Test
    fun ceos_countsExtractionIndicatedAsAllEligibleSurfaces_andIgnoresAbsentPrimaryTeeth() {
        val records = mapOf(
            55 to ToothRecord(status = ToothStatus.EXTRACTION_INDICATED),
            51 to ToothRecord(status = ToothStatus.EXTRACTION_INDICATED),
            65 to ToothRecord(status = ToothStatus.MISSING_CARIES),
            75 to ToothRecord(status = ToothStatus.MISSING_OTHER)
        )
        val surfaces = mapOf(
            54 to mapOf(
                Surface.OCCLUSAL to SurfaceMark.CARIES,
                Surface.MESIAL to SurfaceMark.CARIES,
                Surface.DISTAL to SurfaceMark.RESTORATION
            )
        )

        val result = CariesSurfaceIndexV48.ceos(records, surfaces)

        assertEquals(2, result.carious)
        assertEquals(9, result.missing)
        assertEquals(1, result.filled)
        assertEquals(12, result.total)
    }
}
