package com.yomismtz.expedientedeldentista.clinical

data class CpiModifiedSummaryV48(
    val presentTeeth: Int,
    val bleedingRecorded: Int,
    val bleedingPositive: Int,
    val pocketRecorded: Int,
    val pocket45: Int,
    val pocket6Plus: Int,
    val excludedTeeth: Int,
    val complete: Boolean
)

object CpiModifiedV48 {
    const val ABSENT = 0
    const val PRESENT = 1
    const val POCKET_45 = 1
    const val POCKET_6_PLUS = 2
    const val EXCLUDED = 9

    val sextants: List<List<Int>> = listOf(
        listOf(18,17,16,15,14),
        listOf(13,12,11,21,22,23),
        listOf(24,25,26,27,28),
        listOf(38,37,36,35,34),
        listOf(33,32,31,41,42,43),
        listOf(44,45,46,47,48)
    )

    fun sanitizeBleeding(code: Int): Int =
        if (code in setOf(ABSENT, PRESENT, EXCLUDED)) code else ABSENT

    fun sanitizePocket(code: Int): Int =
        if (code in setOf(ABSENT, POCKET_45, POCKET_6_PLUS, EXCLUDED)) code else ABSENT

    fun bleedingLabel(code: Int?): String = when (code) {
        ABSENT -> "0"
        PRESENT -> "1"
        EXCLUDED -> "9"
        else -> "—"
    }

    fun pocketLabel(code: Int?): String = when (code) {
        ABSENT -> "0"
        POCKET_45 -> "1"
        POCKET_6_PLUS -> "2"
        EXCLUDED -> "9"
        else -> "—"
    }

    fun summarize(
        presentTeeth: Set<Int>,
        bleeding: Map<Int, Int>,
        pockets: Map<Int, Int>,
        recordPockets: Boolean
    ): CpiModifiedSummaryV48 {
        val present = ClinicalContent.permanentTeeth.filter { it in presentTeeth }
        val excluded = present.count { bleeding[it] == EXCLUDED || pockets[it] == EXCLUDED }

        val bleedingRecorded = present.count { it in bleeding }
        val bleedingPositive = present.count { bleeding[it] == PRESENT }

        val pocketRecorded = if (recordPockets) present.count { it in pockets } else 0
        val pocket45 = if (recordPockets) present.count { pockets[it] == POCKET_45 } else 0
        val pocket6Plus = if (recordPockets) present.count { pockets[it] == POCKET_6_PLUS } else 0

        val bleedingComplete = present.all { it in bleeding }
        val pocketComplete = !recordPockets || present.all { it in pockets }

        return CpiModifiedSummaryV48(
            presentTeeth = present.size,
            bleedingRecorded = bleedingRecorded,
            bleedingPositive = bleedingPositive,
            pocketRecorded = pocketRecorded,
            pocket45 = pocket45,
            pocket6Plus = pocket6Plus,
            excludedTeeth = excluded,
            complete = bleedingComplete && pocketComplete
        )
    }
}
