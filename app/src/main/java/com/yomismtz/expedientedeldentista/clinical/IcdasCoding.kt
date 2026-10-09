package com.yomismtz.expedientedeldentista.clinical

/**
 * ICDAS II coronal two-digit coding.
 *
 * The first component represents restoration/sealant status (0-8).
 * The second component represents coronal caries severity (0-6).
 * Special whole-tooth/surface codes are represented separately and are
 * deliberately not encoded as a restoration digit + caries digit.
 */
data class IcdasSurfaceRecord(
    val restorationCode: Int? = null,
    val cariesCode: Int? = null,
    val specialCode: Int? = null,
    val legacyPending: Boolean = false
) {
    val isComplete: Boolean
        get() = specialCode != null || (restorationCode != null && cariesCode != null)

    val combinedCode: Int
        get() = specialCode ?: ((restorationCode ?: 0) * 10 + (cariesCode ?: 0))
}

object IcdasCoding {
    val restorationCodes = 0..8
    val cariesCodes = 0..6
    val specialCodes = setOf(90, 91, 92, 93, 96, 97, 98, 99)

    fun isValidRestoration(code: Int) = code in restorationCodes
    fun isValidCaries(code: Int) = code in cariesCodes
    fun isValidSpecial(code: Int) = code in specialCodes

    fun toothStatusForSpecial(code: Int, current: ToothStatus): ToothStatus = when (code) {
        91, 93, 97 -> ToothStatus.MISSING_CARIES
        90, 92, 98 -> ToothStatus.MISSING_OTHER
        99 -> ToothStatus.UNERUPTED
        else -> current
    }

    fun isValidCombined(code: Int): Boolean {
        if (code in 0..6) return true // 00–06; leading zero is not representable by Int
        if (code in specialCodes) return true
        if (code !in 10..86) return false
        return isValidRestoration(code / 10) && isValidCaries(code % 10)
    }

    fun combine(restoration: Int, caries: Int): Int {
        require(isValidRestoration(restoration)) { "ICDAS restoration code invalid: $restoration" }
        require(isValidCaries(caries)) { "ICDAS caries code invalid: $caries" }
        return restoration * 10 + caries
    }

    /**
     * Migrates the former Int representation without inventing a restoration code.
     *
     * Legacy 0-6 values are ambiguous: preserve the value as the caries digit,
     * leave restoration unknown, and mark legacyPending=true. combinedCode is a
     * compatibility projection only while pending; the UI must not present it as complete.
     */
    fun fromLegacy(code: Int): IcdasSurfaceRecord? {
        if (code in 0..6) {
            return IcdasSurfaceRecord(restorationCode = null, cariesCode = code, legacyPending = true)
        }
        if (isValidSpecial(code)) return IcdasSurfaceRecord(specialCode = code)
        if (code in 10..86) {
            val restoration = code / 10
            val caries = code % 10
            if (isValidRestoration(restoration) && isValidCaries(caries)) {
                return IcdasSurfaceRecord(restorationCode = restoration, cariesCode = caries)
            }
        }
        return null
    }

    fun fromCombined(code: Int): IcdasSurfaceRecord? =
        if (isValidCombined(code)) fromLegacy(code) else null

    fun migrateLegacySurfaceMap(legacy: Map<Surface, Int>): Map<Surface, IcdasSurfaceRecord> =
        legacy.mapNotNull { (surface, code) -> fromLegacy(code)?.let { surface to it } }.toMap()
}
