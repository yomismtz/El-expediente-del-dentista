package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IcdasSurfaceV48Test {

    @Test
    fun anterior_hasFourSurfaces_withoutOcclusal() {
        val surfaces = IcdasSurfaceV48.eligibleSurfaces(11)

        assertEquals(4, surfaces.size)
        assertFalse(Surface.OCCLUSAL in surfaces)
    }

    @Test
    fun posterior_hasFiveSurfaces_withOcclusal() {
        val surfaces = IcdasSurfaceV48.eligibleSurfaces(16)

        assertEquals(5, surfaces.size)
        assertTrue(Surface.OCCLUSAL in surfaces)
    }

    @Test
    fun settingOneSurface_preservesOtherSurfaceCodes() {
        val initial = mapOf(
            Surface.VESTIBULAR to 2,
            Surface.MESIAL to 3
        )

        val updated = IcdasSurfaceV48.setCode(
            tooth = 16,
            current = initial,
            surface = Surface.OCCLUSAL,
            code = 5
        )

        assertEquals(2, updated[Surface.VESTIBULAR])
        assertEquals(3, updated[Surface.MESIAL])
        assertEquals(5, updated[Surface.OCCLUSAL])
        assertEquals(5, IcdasSurfaceV48.highestCode(16, updated))
    }

    @Test
    fun anterior_ignoresLegacyOcclusalCode_andClampsCodesToZeroThroughSix() {
        val sanitized = IcdasSurfaceV48.sanitize(
            tooth = 21,
            values = mapOf(
                Surface.OCCLUSAL to 6,
                Surface.VESTIBULAR to 9,
                Surface.DISTAL to -2
            )
        )

        assertFalse(Surface.OCCLUSAL in sanitized)
        assertEquals(6, sanitized[Surface.VESTIBULAR])
        assertEquals(0, sanitized[Surface.DISTAL])
        assertEquals(6, IcdasSurfaceV48.highestCode(21, sanitized))
    }
}
