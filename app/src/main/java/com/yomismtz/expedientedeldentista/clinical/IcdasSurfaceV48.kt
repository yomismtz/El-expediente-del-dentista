package com.yomismtz.expedientedeldentista.clinical

/**
 * Pure helpers for surface-level ICDAS teaching records.
 * Anterior teeth use four coronal surfaces; posterior teeth add occlusal.
 */
object IcdasSurfaceV48 {

    fun eligibleSurfaces(tooth: Int): List<Surface> =
        if (tooth % 10 >= 4) {
            listOf(
                Surface.VESTIBULAR,
                Surface.LINGUAL_PALATAL,
                Surface.MESIAL,
                Surface.DISTAL,
                Surface.OCCLUSAL
            )
        } else {
            listOf(
                Surface.VESTIBULAR,
                Surface.LINGUAL_PALATAL,
                Surface.MESIAL,
                Surface.DISTAL
            )
        }

    fun sanitize(tooth: Int, values: Map<Surface, Int>): Map<Surface, Int> =
        values
            .filterKeys { it in eligibleSurfaces(tooth) }
            .mapValues { (_, code) -> code.coerceIn(0, 6) }

    fun setCode(
        tooth: Int,
        current: Map<Surface, Int>,
        surface: Surface,
        code: Int
    ): Map<Surface, Int> {
        if (surface !in eligibleSurfaces(tooth)) return sanitize(tooth, current)
        return sanitize(tooth, current + (surface to code.coerceIn(0, 6)))
    }

    fun setAll(tooth: Int, code: Int): Map<Surface, Int> =
        eligibleSurfaces(tooth).associateWith { code.coerceIn(0, 6) }

    fun highestCode(tooth: Int, values: Map<Surface, Int>): Int =
        sanitize(tooth, values).values.maxOrNull() ?: 0
}
