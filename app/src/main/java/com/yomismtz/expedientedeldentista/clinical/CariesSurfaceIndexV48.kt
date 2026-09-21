package com.yomismtz.expedientedeldentista.clinical

/**
 * Pure surface-level caries index logic used by CPOS/DMFS and ceos/defs.
 *
 * A surface map is required for present teeth: tooth-level CARIES/RESTORED alone
 * is intentionally not expanded to every surface, because surface indices must
 * preserve the actual number of affected surfaces.
 *
 * CPOS/DMFS teaching convention:
 * - C/D: surfaces explicitly marked CARIES.
 * - P/M: a permanent tooth confirmed missing due to caries contributes all of
 *   its eligible surfaces (4 for anterior teeth, 5 for posterior teeth).
 * - O/F: surfaces explicitly marked RESTORATION.
 * - Missing for another reason and sealants do not contribute.
 *
 * ceos/defs teaching convention used in this app:
 * - c/d: primary surfaces explicitly marked CARIES.
 * - e/e: a primary tooth indicated for extraction due to caries contributes all
 *   eligible surfaces (4 anterior, 5 posterior).
 * - o/f: primary surfaces explicitly marked RESTORATION.
 * - An already absent/exfoliated primary tooth is not automatically counted.
 */
object CariesSurfaceIndexV48 {

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

    fun cpos(
        records: Map<Int, ToothRecord>,
        surfaces: Map<Int, Map<Surface, SurfaceMark>>,
        includeThirdMolars: Boolean
    ): IndexResult {
        var carious = 0
        var missing = 0
        var filled = 0

        CariesToothIndexV48.permanentTeeth(includeThirdMolars).forEach { tooth ->
            val status = records[tooth]?.status ?: ToothStatus.HEALTHY
            when (status) {
                ToothStatus.MISSING_CARIES -> {
                    missing += eligibleSurfaces(tooth).size
                }

                ToothStatus.MISSING_OTHER -> Unit

                else -> {
                    val map = surfaces[tooth].orEmpty()
                    eligibleSurfaces(tooth).forEach { surface ->
                        when (map[surface]) {
                            SurfaceMark.CARIES -> carious++
                            SurfaceMark.RESTORATION -> filled++
                            SurfaceMark.HEALTHY,
                            SurfaceMark.SEALANT,
                            null -> Unit
                        }
                    }
                }
            }
        }

        return IndexResult(carious, missing, filled, carious + missing + filled)
    }

    fun ceos(
        records: Map<Int, ToothRecord>,
        surfaces: Map<Int, Map<Surface, SurfaceMark>>
    ): IndexResult {
        var carious = 0
        var extractionIndicated = 0
        var filled = 0

        ClinicalContent.primaryTeeth.forEach { tooth ->
            val status = records[tooth]?.status ?: ToothStatus.HEALTHY
            when (status) {
                ToothStatus.EXTRACTION_INDICATED -> {
                    extractionIndicated += eligibleSurfaces(tooth).size
                }

                ToothStatus.MISSING_CARIES,
                ToothStatus.MISSING_OTHER -> Unit

                else -> {
                    val map = surfaces[tooth].orEmpty()
                    eligibleSurfaces(tooth).forEach { surface ->
                        when (map[surface]) {
                            SurfaceMark.CARIES -> carious++
                            SurfaceMark.RESTORATION -> filled++
                            SurfaceMark.HEALTHY,
                            SurfaceMark.SEALANT,
                            null -> Unit
                        }
                    }
                }
            }
        }

        return IndexResult(
            carious = carious,
            missing = extractionIndicated,
            filled = filled,
            total = carious + extractionIndicated + filled
        )
    }
}
