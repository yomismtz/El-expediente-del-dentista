package com.yomismtz.expedientedeldentista.clinical

/**
 * Pure counting logic for the tooth-level caries indices used by the teaching UI.
 *
 * CPOD/DMFT:
 * - C/D: present permanent tooth with caries. A permanent tooth already indicated
 *   for extraction because of caries remains in C/D until it is actually missing.
 * - P/M: permanent tooth confirmed missing due to caries.
 * - O/F: restored permanent tooth without current caries.
 * - Missing for another reason and sealants do not contribute.
 *
 * ceod/deft teaching convention used in this app:
 * - c/d: carious primary tooth.
 * - e/e: primary tooth indicated for extraction due to caries.
 * - o/f: restored primary tooth.
 * - A primary tooth already absent is not automatically assigned to e because
 *   physiologic exfoliation can make the cause of absence ambiguous.
 */
object CariesToothIndexV48 {

    fun permanentTeeth(includeThirdMolars: Boolean): List<Int> =
        if (includeThirdMolars) ClinicalContent.permanentTeeth
        else ClinicalContent.permanentTeeth.filter { it % 10 != 8 }

    fun cpod(
        records: Map<Int, ToothRecord>,
        includeThirdMolars: Boolean
    ): IndexResult {
        var carious = 0
        var missing = 0
        var filled = 0

        permanentTeeth(includeThirdMolars).forEach { tooth ->
            when (records[tooth]?.status ?: ToothStatus.HEALTHY) {
                ToothStatus.CARIES,
                ToothStatus.EXTRACTION_INDICATED -> carious++

                ToothStatus.MISSING_CARIES -> missing++
                ToothStatus.RESTORED -> filled++

                ToothStatus.HEALTHY,
                ToothStatus.MISSING_OTHER,
                ToothStatus.SEALANT -> Unit
            }
        }
        return IndexResult(carious, missing, filled, carious + missing + filled)
    }

    fun ceod(records: Map<Int, ToothRecord>): IndexResult {
        var carious = 0
        var extractionIndicated = 0
        var filled = 0

        ClinicalContent.primaryTeeth.forEach { tooth ->
            when (records[tooth]?.status ?: ToothStatus.HEALTHY) {
                ToothStatus.CARIES -> carious++
                ToothStatus.EXTRACTION_INDICATED -> extractionIndicated++
                ToothStatus.RESTORED -> filled++

                ToothStatus.HEALTHY,
                ToothStatus.MISSING_CARIES,
                ToothStatus.MISSING_OTHER,
                ToothStatus.SEALANT -> Unit
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
