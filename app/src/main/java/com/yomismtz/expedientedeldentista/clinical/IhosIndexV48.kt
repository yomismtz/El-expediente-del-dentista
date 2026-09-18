package com.yomismtz.expedientedeldentista.clinical

import kotlin.math.round

data class IhosResultV48(
    val examinedSites: Int,
    val debrisAverage: Double,
    val calculusAverage: Double,
    val total: Double
)

object IhosIndexV48 {

    private fun round1(value: Double): Double = round(value * 10.0) / 10.0

    fun calculate(
        examinedTeeth: Collection<Int>,
        debrisScores: Map<Int, Int>,
        calculusScores: Map<Int, Int>
    ): IhosResultV48 {
        val complete = examinedTeeth.distinct().filter { tooth ->
            tooth in debrisScores && tooth in calculusScores
        }

        if (complete.isEmpty()) {
            return IhosResultV48(
                examinedSites = 0,
                debrisAverage = 0.0,
                calculusAverage = 0.0,
                total = 0.0
            )
        }

        val debrisRaw = complete.map { debrisScores.getValue(it).coerceIn(0, 3) }.average()
        val calculusRaw = complete.map { calculusScores.getValue(it).coerceIn(0, 3) }.average()

        return IhosResultV48(
            examinedSites = complete.size,
            debrisAverage = round1(debrisRaw),
            calculusAverage = round1(calculusRaw),
            total = round1(debrisRaw + calculusRaw)
        )
    }

    fun interpretationEs(total: Double): String = when {
        total <= 1.2 -> "Buena"
        total <= 3.0 -> "Regular"
        else -> "Mala"
    }

    fun interpretationEn(total: Double): String = when {
        total <= 1.2 -> "Good"
        total <= 3.0 -> "Fair"
        else -> "Poor"
    }
}
