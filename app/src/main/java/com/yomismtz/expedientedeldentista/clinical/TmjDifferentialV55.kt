package com.yomismtz.expedientedeldentista.clinical

/**
 * Motor educativo de orientación para hallazgos de ATM/TTM.
 * No establece diagnóstico definitivo ni sustituye anamnesis, exploración estandarizada
 * o criterios diagnósticos validados (p. ej. DC/TMD).
 */
data class TmjDifferentialResultV55(
    val primaryOrientation: String,
    val differentials: List<String>,
    val redFlags: List<String>,
    val note: String = "Orientación presuntiva educativa; requiere correlación clínica y no constituye diagnóstico definitivo."
)

fun tmjDifferentialV55(findings: Set<String>, openingLabel: String): TmjDifferentialResultV55 {
    val f = findings.map { it.trim().lowercase() }.toSet()
    fun has(vararg terms: String) = terms.any { term -> f.any { it.contains(term) } }

    val pain = has("dolor al abrir", "dolor articular", "dolor preauricular")
    val musclePain = has("dolor muscular", "masetero", "temporal dolor")
    val click = has("chasquido", "click")
    val crepitus = has("crepitación", "crepitacion")
    val lock = has("bloqueo", "locking")
    val deviation = has("desviación", "desviacion")
    val deflection = has("deflexión", "deflexion")
    val headache = has("cefalea", "dolor de cabeza")
    val bruxism = has("bruxismo", "apretamiento")
    val trauma = has("traumatismo", "trauma")
    val swelling = has("aumento de volumen", "inflamación", "inflamacion")
    val neuro = has("parestesia", "anestesia", "déficit neurológico", "deficit neurologico")
    val systemic = has("fiebre", "pérdida de peso", "perdida de peso")
    val limited = openingLabel.startsWith("<30") || has("limitación funcional", "limitacion funcional")

    val redFlags = buildList {
        if (trauma && limited) add("Traumatismo con limitación marcada: descartar lesión ósea/articular antes de atribuirlo a TTM común.")
        if (swelling || systemic) add("Aumento de volumen o síntomas sistémicos: valorar infección, inflamación sistémica u otra patología no mecánica.")
        if (neuro) add("Alteración neurológica/sensitiva: requiere evaluación dirigida y no debe atribuirse automáticamente a TTM.")
        if (limited && !pain && !click && !lock) add("Apertura muy limitada sin patrón mecánico claro: investigar causas extraarticulares y sistémicas.")
    }

    val differentials = mutableListOf<String>()
    val primary = when {
        redFlags.isNotEmpty() -> "Hallazgos que requieren descartar patología distinta o adicional a un TTM mecánico habitual"
        lock && limited -> {
            differentials += "Desplazamiento discal sin reducción con limitación de apertura (posibilidad clínica; confirmar con criterios diagnósticos)."
            "Trastorno intraarticular con bloqueo/limitación"
        }
        click && !limited -> {
            differentials += "Desplazamiento discal con reducción como posibilidad clínica si el ruido es reproducible y concordante."
            "Trastorno intraarticular con chasquido"
        }
        crepitus -> {
            differentials += "Cambio articular degenerativo como posibilidad; la crepitación aislada no basta para diagnosticar osteoartrosis."
            "Compromiso articular con crepitación"
        }
        musclePain && (bruxism || headache) -> {
            differentials += "Dolor miofascial/mialgia de músculos masticatorios."
            if (headache) differentials += "Cefalea atribuible a TTM solo si cumple criterios clínicos; considerar cefaleas primarias en el diferencial."
            "Predominio muscular"
        }
        pain -> {
            differentials += "Artralgia de ATM como posibilidad si el dolor familiar se reproduce con palpación o movimientos provocativos."
            "Dolor articular a correlacionar"
        }
        deviation || deflection -> {
            differentials += "Alteración del patrón de apertura; correlacionar con rango, ruidos, dolor y simetría de movimientos."
            "Alteración funcional del trayecto mandibular"
        }
        else -> "Sin patrón suficiente para orientar un TTM específico"
    }

    if (bruxism && differentials.none { it.contains("mialgia", ignoreCase = true) }) {
        differentials += "Bruxismo/apretamiento referido como factor contribuyente posible; no equivale por sí solo a TTM."
    }
    if (differentielsNeedMuscle(f, musclePain) && differentials.none { it.contains("mialgia", ignoreCase = true) }) {
        differentials += "Mialgia/miositis u otra fuente muscular según palpación, carga y antecedentes."
    }

    return TmjDifferentialResultV55(primary, differentials.distinct(), redFlags.distinct())
}

private fun differentielsNeedMuscle(findings: Set<String>, musclePain: Boolean): Boolean =
    musclePain || findings.any { it.contains("fatiga mandibular") || it.contains("dolor al masticar") }
