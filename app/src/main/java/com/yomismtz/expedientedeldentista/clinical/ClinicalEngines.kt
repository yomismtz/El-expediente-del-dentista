package com.yomismtz.expedientedeldentista.clinical

import kotlin.math.roundToInt

object ClinicalEngines {
    fun isPrimaryTooth(tooth: Int): Boolean = tooth in ClinicalContent.primaryTeeth

    fun cpod(teeth: Map<Int, ToothRecord>, primary: Boolean): IndexResult {
        val domain = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
        var c = 0
        var p = 0
        var o = 0
        domain.forEach { tooth ->
            when (teeth[tooth]?.status ?: ToothStatus.HEALTHY) {
                ToothStatus.CARIES -> c++
                ToothStatus.EXTRACTION_INDICATED -> if (primary) p++ else c++
                ToothStatus.MISSING_CARIES -> p++
                ToothStatus.RESTORED -> o++
                else -> Unit
            }
        }
        return IndexResult(c, p, o, c + p + o)
    }

    fun cpodInterpretation(value: Int, lang: String): String {
        val es = when (value) {
            0 -> "Libre de caries registrada en el índice"
            in 1..2 -> "Baja experiencia de caries"
            in 3..4 -> "Experiencia moderada de caries"
            in 5..6 -> "Alta experiencia de caries"
            else -> "Muy alta experiencia de caries"
        }
        val en = when (value) {
            0 -> "No caries experience recorded in the index"
            in 1..2 -> "Low caries experience"
            in 3..4 -> "Moderate caries experience"
            in 5..6 -> "High caries experience"
            else -> "Very high caries experience"
        }
        return if (lang == "en") en else es
    }

    fun olearyPercentage(session: EducationalSession): Double {
        val present = session.presentTeeth.filter { tooth ->
            when (session.teeth[tooth]?.status ?: ToothStatus.HEALTHY) {
                ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER -> false
                else -> true
            }
        }
        val denominator = present.size * 4
        if (denominator == 0) return 0.0
        val affected = present.sumOf { session.oleary[it]?.size ?: 0 }
        return affected * 100.0 / denominator
    }

    fun ihos(session: EducationalSession): Double {
        val indexTeeth = listOf(16, 11, 26, 36, 31, 46)
        val valid = indexTeeth.filter { tooth ->
            when (session.teeth[tooth]?.status ?: ToothStatus.HEALTHY) {
                ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER -> false
                else -> true
            }
        }
        if (valid.isEmpty()) return 0.0
        val total = valid.sumOf { (session.ihosDebris[it] ?: 0) + (session.ihosCalculus[it] ?: 0) }
        return total.toDouble() / valid.size
    }

    fun ihosInterpretation(value: Double, lang: String): String {
        val es = when {
            value <= 1.2 -> "Buena higiene oral"
            value <= 3.0 -> "Higiene oral regular"
            else -> "Higiene oral deficiente"
        }
        val en = when {
            value <= 1.2 -> "Good oral hygiene"
            value <= 3.0 -> "Fair oral hygiene"
            else -> "Poor oral hygiene"
        }
        return if (lang == "en") en else es
    }

    fun ipcHighest(codes: List<String>): String {
        return codes.mapNotNull { it.toIntOrNull() }.maxOrNull()?.toString() ?: if (codes.any { it == "X" }) "X" else "9"
    }

    fun ipcInterpretation(code: String, lang: String): String {
        val es = when (code) {
            "0" -> "Periodontalmente sano"
            "1" -> "Sangrado al sondaje; compatible con gingivitis"
            "2" -> "Cálculo dental; requiere control de higiene profesional"
            "3" -> "Bolsa periodontal de 4–5 mm; requiere valoración periodontal"
            "4" -> "Bolsa periodontal ≥6 mm; probable compromiso periodontal avanzado"
            "X" -> "Sextante excluido"
            else -> "No registrable"
        }
        val en = when (code) {
            "0" -> "Periodontally healthy"
            "1" -> "Bleeding on probing; compatible with gingivitis"
            "2" -> "Dental calculus; professional hygiene control is needed"
            "3" -> "4–5 mm periodontal pocket; periodontal assessment is required"
            "4" -> "Periodontal pocket ≥6 mm; probable advanced periodontal involvement"
            "X" -> "Excluded sextant"
            else -> "Not recordable"
        }
        return if (lang == "en") en else es
    }

    fun pulpalDiagnosis(a: PulpalAssessment): DiagnosisResult {
        val pulpal = when {
            a.previousRootCanal -> "previously_treated"
            a.previousPartialEndo -> "previously_initiated"
            a.sensitivityNegative -> "necrosis"
            a.spontaneousPain || a.nightPain || a.coldLingering || (a.heatPositive && a.coldPositive) -> "irreversible"
            a.coldPositive || a.sweetsPain -> "reversible"
            a.deepCariesOrExposure -> "irreversible_asymptomatic"
            else -> "normal"
        }

        val apical = when {
            a.fistula -> "chronic_abscess"
            a.swelling && (a.sensitivityNegative || a.apicalRadiolucency) -> "acute_abscess"
            a.apicalRadiopacity -> "condensing_osteitis"
            (a.percussionPain || a.palpationPain) -> "symptomatic_apical"
            a.apicalRadiolucency && !a.percussionPain && !a.palpationPain -> "asymptomatic_apical"
            else -> "normal_apical"
        }

        val pulpalEs = when (pulpal) {
            "previously_treated" -> "Previamente tratado"
            "previously_initiated" -> "Terapia previamente iniciada"
            "necrosis" -> "Necrosis pulpar"
            "irreversible" -> "Pulpitis irreversible sintomática"
            "irreversible_asymptomatic" -> "Pulpitis irreversible asintomática (orientación educativa)"
            "reversible" -> "Pulpitis reversible"
            else -> "Pulpa normal / hallazgos insuficientes para patología pulpar"
        }
        val pulpalEn = when (pulpal) {
            "previously_treated" -> "Previously treated"
            "previously_initiated" -> "Previously initiated therapy"
            "necrosis" -> "Pulp necrosis"
            "irreversible" -> "Symptomatic irreversible pulpitis"
            "irreversible_asymptomatic" -> "Asymptomatic irreversible pulpitis (educational orientation)"
            "reversible" -> "Reversible pulpitis"
            else -> "Normal pulp / insufficient findings for pulpal disease"
        }
        val apicalEs = when (apical) {
            "acute_abscess" -> "Absceso apical agudo"
            "chronic_abscess" -> "Absceso apical crónico"
            "condensing_osteitis" -> "Osteítis condensante"
            "symptomatic_apical" -> "Periodontitis apical sintomática"
            "asymptomatic_apical" -> "Periodontitis apical asintomática"
            else -> "Tejidos apicales normales / sin datos suficientes"
        }
        val apicalEn = when (apical) {
            "acute_abscess" -> "Acute apical abscess"
            "chronic_abscess" -> "Chronic apical abscess"
            "condensing_osteitis" -> "Condensing osteitis"
            "symptomatic_apical" -> "Symptomatic apical periodontitis"
            "asymptomatic_apical" -> "Asymptomatic apical periodontitis"
            else -> "Normal apical tissues / insufficient data"
        }

        val explainEs = buildList {
            if (a.spontaneousPain) add("dolor espontáneo")
            if (a.nightPain) add("dolor nocturno")
            if (a.coldLingering) add("dolor térmico persistente")
            if (a.sensitivityNegative) add("ausencia de respuesta a sensibilidad")
            if (a.percussionPain) add("dolor a la percusión")
            if (a.palpationPain) add("dolor a la palpación")
            if (a.swelling) add("aumento de volumen")
            if (a.fistula) add("fístula")
            if (a.apicalRadiolucency) add("radiolucidez apical")
            if (a.widenedPdl) add("ensanchamiento del ligamento periodontal")
            if (a.apicalRadiopacity) add("radiopacidad apical")
            if (a.deepCariesOrExposure) add("lesión profunda/exposición compatible con compromiso pulpar")
        }.joinToString(", ").ifBlank { "sin hallazgos patológicos seleccionados" }

        val explainEn = buildList {
            if (a.spontaneousPain) add("spontaneous pain")
            if (a.nightPain) add("night pain")
            if (a.coldLingering) add("lingering thermal pain")
            if (a.sensitivityNegative) add("negative sensitivity response")
            if (a.percussionPain) add("percussion pain")
            if (a.palpationPain) add("palpation pain")
            if (a.swelling) add("swelling")
            if (a.fistula) add("sinus tract")
            if (a.apicalRadiolucency) add("apical radiolucency")
            if (a.widenedPdl) add("widened periodontal ligament")
            if (a.apicalRadiopacity) add("apical radiopacity")
            if (a.deepCariesOrExposure) add("deep lesion/exposure compatible with pulpal involvement")
        }.joinToString(", ").ifBlank { "no pathologic findings selected" }

        return DiagnosisResult(
            pulpalEs = pulpalEs,
            pulpalEn = pulpalEn,
            apicalEs = apicalEs,
            apicalEn = apicalEn,
            explanationEs = "Hallazgos que orientan el resultado: $explainEs. Es una ayuda educativa y debe correlacionarse con exploración y pruebas clínicas completas.",
            explanationEn = "Findings supporting the result: $explainEn. This is an educational aid and must be correlated with complete examination and clinical testing."
        )
    }

    fun periodontalSummary(session: EducationalSession, lang: String): String {
        val records = session.periodontogram.values
        if (records.isEmpty()) return if (lang == "en") "No periodontal chart data yet." else "Aún no hay datos en el periodontograma."
        val maxPocket = records.flatMap { it.probingDepths }.maxOrNull() ?: 0
        val bleedingCount = records.count { it.bleeding }
        val suppuration = records.any { it.suppuration }
        val severeMobility = records.any { it.mobility >= 2 }
        val es = "Profundidad máxima registrada: ${maxPocket} mm. Dientes con sangrado: $bleedingCount. " +
            (if (suppuration) "Hay supuración registrada. " else "") +
            (if (severeMobility) "Existe movilidad grado 2–3. " else "") +
            "El diagnóstico periodontal definitivo requiere integrar pérdida de inserción, radiografías, extensión y factores de riesgo."
        val en = "Maximum recorded probing depth: ${maxPocket} mm. Teeth with bleeding: $bleedingCount. " +
            (if (suppuration) "Suppuration is recorded. " else "") +
            (if (severeMobility) "Grade 2–3 mobility is present. " else "") +
            "Definitive periodontal diagnosis requires attachment loss, radiographs, extent and risk factors."
        return if (lang == "en") en else es
    }

    fun generateIntakeNote(session: EducationalSession, lang: String): String {
        val p = session.profile
        val history = session.history
        val diseases = ClinicalContent.diseases.mapNotNull { guide ->
            val answer = history.diseases[guide.id]
            if (answer?.present == true) {
                if (lang == "en") guide.nameEn else guide.nameEs
            } else null
        }
        val permanent = cpod(session.teeth, primary = false)
        val primary = cpod(session.teeth, primary = true)
        val diagnosis = pulpalDiagnosis(session.pulpal)
        val ipc = ipcHighest(session.ipcCodes)

        return if (lang == "en") {
            buildString {
                append("EDUCATIONAL INTAKE NOTE\n")
                append("Identification: ${p.exerciseName.ifBlank { "Not entered" }}; age ${p.age.ifBlank { "—" }}; sex ${p.sex.ifBlank { "—" }}.\n")
                append("Reason for visit: ${p.reasonForVisit.ifBlank { "Not entered" }}.\n")
                append("Current condition: ${p.currentCondition.ifBlank { "Not entered" }}.\n")
                append("Systemic history: ASA ${history.asaClass}${if (history.asaEmergency) "E" else ""}; ")
                append(if (diseases.isEmpty()) "no selected conditions" else diseases.joinToString(", "))
                append(". Medications: ${p.medications.ifBlank { "none entered" }}. Allergies: ${p.allergies.ifBlank { "none entered" }}.\n")
                append("Caries indices: DMFT=${permanent.total} (D=${permanent.carious}, M=${permanent.missing}, F=${permanent.filled}); dmft=${primary.total}.\n")
                append("Periodontal screening: highest CPI code $ipc. OHI-S=${"%.2f".format(ihos(session))}. O'Leary=${"%.1f".format(olearyPercentage(session))}%.\n")
                append("Pulpal/periapical educational orientation: ${diagnosis.pulpalEn}; ${diagnosis.apicalEn}.\n")
                append("This automatically generated text is for learning how an intake note is organized; it is not a real clinical record.")
            }
        } else {
            buildString {
                append("NOTA DE INGRESO EDUCATIVA\n")
                append("Identificación: ${p.exerciseName.ifBlank { "No capturada" }}; edad ${p.age.ifBlank { "—" }}; sexo ${p.sex.ifBlank { "—" }}.\n")
                append("Motivo de consulta: ${p.reasonForVisit.ifBlank { "No capturado" }}.\n")
                append("Padecimiento actual: ${p.currentCondition.ifBlank { "No capturado" }}.\n")
                append("Antecedentes sistémicos: ASA ${history.asaClass}${if (history.asaEmergency) "E" else ""}; ")
                append(if (diseases.isEmpty()) "sin padecimientos seleccionados" else diseases.joinToString(", "))
                append(". Medicamentos: ${p.medications.ifBlank { "ninguno capturado" }}. Alergias: ${p.allergies.ifBlank { "ninguna capturada" }}.\n")
                append("Índices de caries: CPOD=${permanent.total} (C=${permanent.carious}, P=${permanent.missing}, O=${permanent.filled}); ceod=${primary.total}.\n")
                append("Tamizaje periodontal: código IPC más alto $ipc. IHOS=${"%.2f".format(ihos(session))}. O'Leary=${"%.1f".format(olearyPercentage(session))}%.\n")
                append("Orientación pulpar/periapical educativa: ${diagnosis.pulpalEs}; ${diagnosis.apicalEs}.\n")
                append("Este texto automático sirve para aprender cómo se integra una nota de ingreso; no constituye un expediente clínico real.")
            }
        }
    }

    fun generateEvolutionNotes(session: EducationalSession, lang: String): List<String> {
        val plansById = ClinicalContent.treatmentPlans.associateBy { it.id }
        val optionsById = ClinicalContent.treatmentPlans.flatMap { it.options }.associateBy { it.id }
        val p = session.profile
        return session.teeth.toSortedMap().mapNotNull { (tooth, record) ->
            val diagnosis = record.diagnosisId?.let { plansById[it] } ?: return@mapNotNull null
            val option = record.treatmentId?.let { optionsById[it] } ?: return@mapNotNull null
            if (lang == "en") {
                "Tooth $tooth. Educational example: patient/session reviewed, vital signs recorded when applicable. Diagnosis: ${diagnosis.diagnosisEn}. Procedure/plan: ${option.labelEn}. Tolerance and relevant findings should be documented, followed by postoperative instructions, warning signs and next appointment. BP ${p.bloodPressure.ifBlank { "—" }}, HR ${p.heartRate.ifBlank { "—" }}, RR ${p.respiratoryRate.ifBlank { "—" }}, T ${p.temperature.ifBlank { "—" }}."
            } else {
                "OD $tooth. Ejemplo educativo: se revisa el estado de la sesión y se registran signos vitales cuando corresponda. Diagnóstico: ${diagnosis.diagnosisEs}. Procedimiento/plan: ${option.labelEs}. Debe documentarse tolerancia, hallazgos relevantes, indicaciones posoperatorias, signos de alarma y próxima cita. TA ${p.bloodPressure.ifBlank { "—" }}, FC ${p.heartRate.ifBlank { "—" }}, FR ${p.respiratoryRate.ifBlank { "—" }}, Temp. ${p.temperature.ifBlank { "—" }}."
            }
        }
    }

    fun orthodonticSuggestion(session: EducationalSession, lang: String): String {
        val extractionLike = session.teeth.filter { (tooth, record) ->
            isPrimaryTooth(tooth) && (
                record.status == ToothStatus.MISSING_CARIES ||
                    record.status == ToothStatus.EXTRACTION_INDICATED ||
                    record.treatmentId in setOf("extract", "extract_necrosis", "extract_space")
                )
        }.keys.sorted()
        if (extractionLike.isEmpty()) {
            return if (lang == "en") "No primary-tooth loss/extraction has been marked. No space-maintenance suggestion is generated." else
                "No se ha marcado pérdida o extracción de dientes temporales. No se genera sugerencia de mantenimiento de espacio."
        }
        val multiple = extractionLike.size > 1
        val secondPrimaryMolar = extractionLike.any { it % 10 == 5 }
        val es = buildString {
            append("Se detectó pérdida o extracción educativa de dientes temporales: ${extractionLike.joinToString()}. ")
            if (secondPrimaryMolar) append("Si se pierde un segundo molar temporal antes de la erupción del primer molar permanente, debe valorarse una zapatilla distal. ")
            if (multiple) append("Ante pérdidas múltiples, valorar mantenedores bilaterales como arco lingual en mandíbula o botón de Nance en maxilar, según erupción y soporte. ")
            else append("Ante una pérdida unilateral seleccionada puede valorarse un mantenedor tipo banda-ansa. ")
            append("La selección final exige revisar edad dental, diente sucesor, erupción, espacio disponible, oclusión y radiografía; la app solo muestra una orientación educativa.")
        }
        val en = buildString {
            append("Educational primary-tooth loss/extraction detected: ${extractionLike.joinToString()}. ")
            if (secondPrimaryMolar) append("If a second primary molar is lost before eruption of the first permanent molar, a distal shoe may need assessment. ")
            if (multiple) append("For multiple losses, consider bilateral appliances such as a lower lingual arch or maxillary Nance button according to eruption and support. ")
            else append("For a selected unilateral loss, a band-and-loop appliance may be considered. ")
            append("Final selection requires dental age, successor tooth, eruption, available space, occlusion and radiographic assessment; the app only provides educational orientation.")
        }
        return if (lang == "en") en else es
    }

    fun round1(value: Double): Double = (value * 10.0).roundToInt() / 10.0
}
