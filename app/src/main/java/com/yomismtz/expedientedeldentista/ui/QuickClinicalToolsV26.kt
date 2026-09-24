package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.PediatricDoseBasis
import com.yomismtz.expedientedeldentista.clinical.PediatricMedicationCategoryV27
import com.yomismtz.expedientedeldentista.clinical.PediatricMedicationSpecV27
import com.yomismtz.expedientedeldentista.clinical.QuickSignsSymptomsState
import com.yomismtz.expedientedeldentista.clinical.calculateLocalAnesthetic
import com.yomismtz.expedientedeldentista.clinical.calculatePediatricMedicationRangeV27
import com.yomismtz.expedientedeldentista.clinical.localAnestheticSpecsV26
import com.yomismtz.expedientedeldentista.clinical.pediatricMedicationSpecsV27
import kotlin.math.abs

private data class ChoiceV26(val value: String, val es: String, val en: String)

private val yesNoUnknownV26 = listOf(
    ChoiceV26("NO", "No", "No"),
    ChoiceV26("YES", "Sí", "Yes"),
    ChoiceV26("UNKNOWN", "No valorado", "Not assessed")
)

private fun decimalInputV26(raw: String, maxChars: Int = 8): String {
    val normalized = raw.replace(',', '.').filter { it.isDigit() || it == '.' }
    val firstDot = normalized.indexOf('.')
    val singleDot = if (firstDot < 0) normalized else normalized.substring(0, firstDot + 1) + normalized.substring(firstDot + 1).replace(".", "")
    return singleDot.take(maxChars)
}

private fun doseRangeV27(min: Double, max: Double, decimals: Int = 1): String {
    val pattern = if (decimals == 2) "%.2f" else "%.1f"
    val minText = pattern.format(min)
    val maxText = pattern.format(max)
    return if (abs(max - min) < 0.005) minText else "$minText–$maxText"
}

private fun categoryLabelV27(lang: String, category: PediatricMedicationCategoryV27): String = when (category) {
    PediatricMedicationCategoryV27.ANALGESIC -> tr(lang, "Analgésicos", "Analgesics")
    PediatricMedicationCategoryV27.NSAID -> tr(lang, "AINE", "NSAIDs")
    PediatricMedicationCategoryV27.ANTIBIOTIC -> tr(lang, "Antibióticos", "Antibiotics")
    PediatricMedicationCategoryV27.NITROIMIDAZOLE -> tr(lang, "Nitroimidazoles", "Nitroimidazoles")
    PediatricMedicationCategoryV27.ANTIVIRAL -> tr(lang, "Antivirales", "Antivirals")
}

@Composable
private fun ChoiceGridV26(
    lang: String,
    labelEs: String,
    labelEn: String,
    value: String,
    options: List<ChoiceV26>,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(tr(lang, labelEs, labelEn), fontWeight = FontWeight.Bold)
        options.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { option ->
                    FilterChip(
                        selected = value == option.value,
                        onClick = { onSelect(option.value) },
                        label = { Text(if (lang == "en") option.en else option.es) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) Column(Modifier.weight(1f)) {}
            }
        }
    }
}

@Composable
fun QuickSignsSymptomsV26Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val state = session.quickSignsSymptoms
    fun save(next: QuickSignsSymptomsState) = onSessionChanged(session.copy(quickSignsSymptoms = next))

    ResponsiveScreenV17(
        tr(lang, "Signos y síntomas · registro rápido", "Signs and symptoms · quick record"),
        tr(lang,
            "Acceso rápido para documentar el cuadro actual sin recorrer toda la historia clínica. No sustituye la anamnesis completa.",
            "Quick access to document the current complaint without opening the full history. It does not replace a complete history."),
        onBack
    ) { _ ->
        ResponsiveSectionV17(tr(lang, "Dolor y evolución", "Pain and evolution")) {
            ChoiceGridV26(lang, "¿Hay dolor?", "Pain present?", state.painStatus, yesNoUnknownV26) { save(state.copy(painStatus = it)) }
            OutlinedTextField(state.location, { save(state.copy(location = it.take(160))) }, label = { Text(tr(lang, "Localización", "Location")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(state.onset, { save(state.copy(onset = it.take(160))) }, label = { Text(tr(lang, "Inicio / desde cuándo", "Onset / since when")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(state.evolution, { save(state.copy(evolution = it.take(200))) }, label = { Text(tr(lang, "Evolución", "Evolution")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                state.intensity,
                { save(state.copy(intensity = decimalInputV26(it, 4))) },
                label = { Text(tr(lang, "Intensidad 0–10", "Intensity 0–10")) },
                supportingText = { Text(tr(lang, "Usa la escala reportada por la persona.", "Use the person's reported scale.")) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(state.character, { save(state.copy(character = it.take(180))) }, label = { Text(tr(lang, "Carácter: pulsátil, punzante, ardor, presión…", "Character: throbbing, sharp, burning, pressure…")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(state.triggers, { save(state.copy(triggers = it.take(220))) }, label = { Text(tr(lang, "Qué lo provoca o agrava", "Triggers / aggravating factors")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(state.relief, { save(state.copy(relief = it.take(220))) }, label = { Text(tr(lang, "Qué lo alivia", "Relieving factors")) }, modifier = Modifier.fillMaxWidth())
        }

        ResponsiveSectionV17(tr(lang, "Signos y síntomas asociados", "Associated signs and symptoms")) {
            ChoiceGridV26(lang, "Aumento de volumen / inflamación", "Swelling", state.swelling, yesNoUnknownV26) { save(state.copy(swelling = it)) }
            ChoiceGridV26(lang, "Sangrado", "Bleeding", state.bleeding, yesNoUnknownV26) { save(state.copy(bleeding = it)) }
            ChoiceGridV26(lang, "Fiebre referida o medida", "Reported or measured fever", state.fever, yesNoUnknownV26) { save(state.copy(fever = it)) }
            ChoiceGridV26(lang, "Limitación de apertura", "Limited mouth opening", state.limitedOpening, yesNoUnknownV26) { save(state.copy(limitedOpening = it)) }
            ChoiceGridV26(lang, "Parestesia / alteración sensitiva", "Paresthesia / sensory change", state.alteredSensation, yesNoUnknownV26) { save(state.copy(alteredSensation = it)) }
            ChoiceGridV26(lang, "Disfagia / dificultad para deglutir", "Dysphagia / difficulty swallowing", state.dysphagia, yesNoUnknownV26) { save(state.copy(dysphagia = it)) }
            OutlinedTextField(
                state.notes,
                { save(state.copy(notes = it.take(600))) },
                label = { Text(tr(lang, "Otros síntomas, signos o contexto", "Other symptoms, signs or context")) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(tr(lang, "Redacción rápida sugerida", "Suggested quick wording"), fontWeight = FontWeight.Black)
                Text(
                    tr(lang,
                        "Localización: ${state.location.ifBlank { "___" }}. Inicio: ${state.onset.ifBlank { "___" }}. Evolución: ${state.evolution.ifBlank { "___" }}. Intensidad: ${state.intensity.ifBlank { "___" }}/10. Carácter: ${state.character.ifBlank { "___" }}. Asociados/observaciones: ${state.notes.ifBlank { "___" }}.",
                        "Location: ${state.location.ifBlank { "___" }}. Onset: ${state.onset.ifBlank { "___" }}. Evolution: ${state.evolution.ifBlank { "___" }}. Intensity: ${state.intensity.ifBlank { "___" }}/10. Character: ${state.character.ifBlank { "___" }}. Associated findings/notes: ${state.notes.ifBlank { "___" }}.")
                )
            }
        }

        NoticeCard(tr(lang,
            "Disnea, disfagia progresiva, aumento de volumen rápidamente progresivo, alteración importante del estado general o signos de compromiso de vía aérea requieren valoración urgente; este registro no debe retrasarla.",
            "Dyspnea, progressive dysphagia, rapidly progressive swelling, marked systemic compromise, or signs of airway involvement require urgent assessment; this record must not delay it."))
    }
}

@Composable
fun ClinicalCalculatorsV26Screen(lang: String, onBack: () -> Unit) {
    var tab by remember { mutableIntStateOf(0) }

    ResponsiveScreenV17(
        tr(lang, "Calculadoras clínicas", "Clinical calculators"),
        tr(lang,
            "Herramientas educativas con referencias pediátricas precargadas. Verifica siempre peso, edad, indicación, alergias, función renal/hepática y la ficha técnica del producto disponible.",
            "Educational tools with preloaded pediatric references. Always verify weight, age, indication, allergies, renal/hepatic function, and the actual product label."),
        onBack
    ) { profile ->
        AdaptiveGridV17(2, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            FilterChip(
                selected = tab == index,
                onClick = { tab = index },
                label = { Text(if (index == 0) tr(lang, "Medicamentos pediátricos", "Pediatric medicines") else tr(lang, "Anestésicos locales", "Local anesthetics")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (tab == 0) PediatricDoseCalculatorV26(lang) else LocalAnestheticCalculatorV26(lang)
    }
}

@Composable
private fun PediatricDoseCalculatorV26(lang: String) {
    var weight by remember { mutableStateOf("") }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    ResponsiveSectionV17(tr(lang, "Paciente y medicamentos", "Patient and medicines")) {
        NoticeCard(tr(lang,
            "Selecciona uno o varios medicamentos. La app muestra rangos pediátricos de referencia y presentaciones comunes encontradas en México; no elige la indicación ni sustituye la prescripción. La dosis pediátrica nunca debe exceder el límite adulto aplicable.",
            "Select one or more medicines. The app shows pediatric reference ranges and common formulations found in Mexico; it does not choose the indication or replace prescribing. Pediatric dosing must never exceed the applicable adult limit."))
        OutlinedTextField(
            weight,
            { weight = decimalInputV26(it) },
            label = { Text(tr(lang, "Peso (kg)", "Weight (kg)")) },
            supportingText = { Text(tr(lang, "El peso se aplica a todos los medicamentos seleccionados.", "The weight is applied to every selected medicine.")) },
            modifier = Modifier.fillMaxWidth()
        )

        PediatricMedicationCategoryV27.entries.forEach { category ->
            val medicines = pediatricMedicationSpecsV27.filter { it.category == category }
            Text(categoryLabelV27(lang, category), fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
            medicines.forEach { medicine ->
                val selected = medicine.id in selectedIds
                FilterChip(
                    selected = selected,
                    onClick = {
                        selectedIds = if (selected) selectedIds - medicine.id else selectedIds + medicine.id
                    },
                    label = { Text(if (lang == "en") medicine.nameEn else medicine.nameEs) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (selectedIds.isEmpty()) {
        ResponsiveSectionV17(tr(lang, "Cálculo", "Calculation")) {
            Text(tr(lang,
                "Selecciona uno o varios medicamentos para ver dosis por kg, intervalo en horas, presentaciones y cálculo por peso.",
                "Select one or more medicines to see weight-based dosing, hourly interval, formulations, and weight-based calculation."))
        }
    } else {
        pediatricMedicationSpecsV27.filter { it.id in selectedIds }.forEach { medicine ->
            key(medicine.id) {
                PediatricMedicationCardV27(lang, weight.toDoubleOrNull() ?: 0.0, medicine)
            }
        }
    }
}

@Composable
private fun PediatricMedicationCardV27(
    lang: String,
    weightKg: Double,
    medicine: PediatricMedicationSpecV27
) {
    var regimenIndex by remember(medicine.id) { mutableIntStateOf(0) }
    var presentationIndex by remember(medicine.id) { mutableIntStateOf(0) }
    val presentation = medicine.presentations[presentationIndex]
    val regimen = medicine.regimens[regimenIndex]
    var intervalHours by remember(medicine.id, regimenIndex) { mutableIntStateOf(regimen.intervalHours.first()) }
    val result = calculatePediatricMedicationRangeV27(weightKg, regimen, intervalHours, presentation)
    val name = if (lang == "en") medicine.nameEn else medicine.nameEs

    ResponsiveSectionV17(name) {
        Text(categoryLabelV27(lang, medicine.category), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        Text(tr(lang, "Presentación", "Formulation"), fontWeight = FontWeight.Black)
        medicine.presentations.forEachIndexed { index, option ->
            FilterChip(
                selected = presentationIndex == index,
                onClick = { presentationIndex = index },
                label = { Text(if (lang == "en") option.labelEn else option.labelEs) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        val presentationNote = if (lang == "en") presentation.noteEn else presentation.noteEs
        if (presentationNote.isNotBlank()) NoticeCard(presentationNote)

        if (presentation.isTopical) {
            NoticeCard(tr(lang,
                "Esta presentación es tópica y no se calcula en mg/kg. Sigue la frecuencia, edad e indicación específicas de la ficha farmacológica mostrada.",
                "This is a topical formulation and is not calculated in mg/kg. Follow the specific frequency, age, and indication in the displayed drug reference."))
        } else {
            if (medicine.regimens.size > 1) {
                Text(tr(lang, "Esquema de referencia", "Reference regimen"), fontWeight = FontWeight.Black)
                medicine.regimens.forEachIndexed { index, option ->
                    FilterChip(
                        selected = regimenIndex == index,
                        onClick = { regimenIndex = index },
                        label = { Text(if (lang == "en") option.labelEn else option.labelEs) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Text(if (lang == "en") regimen.labelEn else regimen.labelEs, fontWeight = FontWeight.Bold)
            }

            val basisText = if (regimen.basis == PediatricDoseBasis.PER_DOSE) {
                tr(lang, "mg/kg por dosis", "mg/kg per dose")
            } else {
                tr(lang, "mg/kg por día", "mg/kg per day")
            }
            Text(tr(lang,
                "Dosis de referencia: ${doseRangeV27(regimen.minMgKg, regimen.maxMgKg)} $basisText",
                "Reference dose: ${doseRangeV27(regimen.minMgKg, regimen.maxMgKg)} $basisText"),
                fontWeight = FontWeight.Black)
            regimen.maxDailyMgKg?.let { maxKg ->
                Text(tr(lang, "Máximo por peso: ${doseRangeV27(maxKg, maxKg)} mg/kg/día", "Weight-based maximum: ${doseRangeV27(maxKg, maxKg)} mg/kg/day"))
            }
            regimen.maxSingleMg?.let { maxSingle ->
                Text(tr(lang, "Máximo por toma: ${doseRangeV27(maxSingle, maxSingle)} mg", "Maximum per dose: ${doseRangeV27(maxSingle, maxSingle)} mg"))
            }
            regimen.maxDailyMg?.let { maxDaily ->
                Text(tr(lang, "Máximo absoluto: ${doseRangeV27(maxDaily, maxDaily)} mg/24 h", "Absolute maximum: ${doseRangeV27(maxDaily, maxDaily)} mg/24 h"))
            }

            Text(tr(lang, "¿Cada cuántas horas?", "How many hours between doses?"), fontWeight = FontWeight.Black)
            regimen.intervalHours.forEach { hours ->
                FilterChip(
                    selected = intervalHours == hours,
                    onClick = { intervalHours = hours },
                    label = { Text(tr(lang, "Cada $hours h", "Every $hours h")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            val regimenNote = if (lang == "en") regimen.noteEn else regimen.noteEs
            NoticeCard(regimenNote)

            if (weightKg <= 0.0 || result == null) {
                Text(tr(lang, "Introduce el peso para calcular esta presentación.", "Enter weight to calculate this formulation."))
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(tr(lang, "Resultado por toma", "Result per dose"), fontWeight = FontWeight.Black)
                        Text("${doseRangeV27(result.doseMinMg, result.doseMaxMg)} mg", fontWeight = FontWeight.Black)
                        if (result.mlMin != null && result.mlMax != null) {
                            Text(tr(lang,
                                "Volumen: ${doseRangeV27(result.mlMin, result.mlMax, 2)} mL cada $intervalHours h",
                                "Volume: ${doseRangeV27(result.mlMin, result.mlMax, 2)} mL every $intervalHours h"),
                                fontWeight = FontWeight.Black)
                        }
                        if (result.unitsMin != null && result.unitsMax != null) {
                            Text(tr(lang,
                                "Equivalente matemático de la presentación: ${doseRangeV27(result.unitsMin, result.unitsMax, 2)} unidad(es) cada $intervalHours h.",
                                "Mathematical formulation equivalent: ${doseRangeV27(result.unitsMin, result.unitsMax, 2)} unit(s) every $intervalHours h."))
                            Text(tr(lang,
                                "No implica que una tableta o cápsula pueda o deba fraccionarse; verifica la forma farmacéutica real.",
                                "This does not mean a tablet or capsule can or should be split; verify the actual dosage form."))
                        }
                        Text(tr(lang,
                            "Total equivalente: ${doseRangeV27(result.dailyMinMg, result.dailyMaxMg)} mg/24 h",
                            "Equivalent total: ${doseRangeV27(result.dailyMinMg, result.dailyMaxMg)} mg/24 h"))
                    }
                }
            }
        }

        val caution = if (lang == "en") medicine.cautionEn else medicine.cautionEs
        NoticeCard(caution)
        if (medicine.category == PediatricMedicationCategoryV27.ANTIBIOTIC || medicine.category == PediatricMedicationCategoryV27.NITROIMIDAZOLE) {
            NoticeCard(tr(lang,
                "Los antibióticos no se indican por dolor dental aislado. Debe confirmarse una indicación infecciosa apropiada y considerar alergias, resistencia, función renal/hepática y guías locales.",
                "Antibiotics are not indicated for isolated dental pain. Confirm an appropriate infectious indication and consider allergies, resistance, renal/hepatic function, and local guidance."))
        }
    }
}

@Composable
private fun LocalAnestheticCalculatorV26(lang: String) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var weight by remember { mutableStateOf("") }
    var ageYears by remember { mutableStateOf("") }
    var cartridgeMl by remember { mutableStateOf("1.8") }
    var ratio by remember { mutableStateOf(localAnestheticSpecsV26.first().epinephrineRatios.firstOrNull()) }
    val spec = localAnestheticSpecsV26[selectedIndex]

    val result = calculateLocalAnesthetic(
        spec = spec,
        weightKg = weight.toDoubleOrNull() ?: 0.0,
        cartridgeMl = cartridgeMl.toDoubleOrNull() ?: 0.0,
        epinephrineRatio = ratio
    )

    ResponsiveSectionV17(tr(lang, "Anestésico y presentación", "Anesthetic and formulation")) {
        localAnestheticSpecsV26.forEachIndexed { index, option ->
            FilterChip(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    ratio = option.epinephrineRatios.firstOrNull()
                },
                label = { Text(if (lang == "en") option.nameEn else option.nameEs) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(tr(lang, "Concentración de referencia: ${spec.concentrationPercent}%", "Reference concentration: ${spec.concentrationPercent}%"), fontWeight = FontWeight.Bold)
        OutlinedTextField(weight, { weight = decimalInputV26(it) }, label = { Text(tr(lang, "Peso (kg)", "Weight (kg)")) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(ageYears, { ageYears = it.filter(Char::isDigit).take(3) }, label = { Text(tr(lang, "Edad (años, para alertas)", "Age (years, for alerts)")) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            cartridgeMl,
            { cartridgeMl = decimalInputV26(it, 4) },
            label = { Text(tr(lang, "Volumen real del cartucho (mL)", "Actual cartridge volume (mL)")) },
            supportingText = { Text(tr(lang, "Confirma siempre lo impreso en el cartucho/caja; el volumen comercial puede variar.", "Always confirm the cartridge/box label; commercial volume may vary.")) },
            modifier = Modifier.fillMaxWidth()
        )
        if (spec.epinephrineRatios.isNotEmpty()) {
            Text(tr(lang, "Epinefrina", "Epinephrine"), fontWeight = FontWeight.Bold)
            spec.epinephrineRatios.forEach { value ->
                FilterChip(
                    selected = ratio == value,
                    onClick = { ratio = value },
                    label = { Text("1:${"%,d".format(value)}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        val caution = if (lang == "en") spec.cautionEn else spec.cautionEs
        NoticeCard(caution)
        val age = ageYears.toIntOrNull()
        if (spec.minimumAgeYears != null && age != null && age < spec.minimumAgeYears) {
            NoticeCard(tr(lang,
                "⚠ Esta referencia no recomienda ${if (lang == "en") spec.nameEn else spec.nameEs} por debajo de ${spec.minimumAgeYears} años.",
                "⚠ This reference does not recommend ${spec.nameEn} below ${spec.minimumAgeYears} years of age."))
        }
    }

    ResponsiveSectionV17(tr(lang, "Resultado educativo", "Educational result")) {
        if (result == null) {
            Text(tr(lang, "Introduce un peso y un volumen de cartucho válidos.", "Enter a valid weight and cartridge volume."))
        } else {
            Text(tr(lang, "Concentración calculada: ${"%.1f".format(result.mgPerMl)} mg/mL", "Calculated concentration: ${"%.1f".format(result.mgPerMl)} mg/mL"))
            Text(tr(lang, "Anestésico por cartucho: ${"%.1f".format(result.mgPerCartridge)} mg", "Anesthetic per cartridge: ${"%.1f".format(result.mgPerCartridge)} mg"), fontWeight = FontWeight.Black)
            Text(tr(lang,
                "Máximo conservador por peso (${spec.conservativeMaxMgKg} mg/kg): ${"%.1f".format(result.maxMgByWeight)} mg",
                "Conservative weight-based maximum (${spec.conservativeMaxMgKg} mg/kg): ${"%.1f".format(result.maxMgByWeight)} mg"))
            Text(tr(lang,
                "Máximo teórico por anestésico: ${"%.2f".format(result.maxCartridges)} cartuchos",
                "Theoretical anesthetic maximum: ${"%.2f".format(result.maxCartridges)} cartridges"), fontWeight = FontWeight.Black)
            Text(tr(lang,
                "Cartuchos enteros sin rebasar ese máximo por peso: ${result.wholeCartridges}",
                "Whole cartridges without exceeding that weight-based maximum: ${result.wholeCartridges}"))
            result.epinephrineMcgPerCartridge?.let { epi ->
                Text(tr(lang,
                    "Epinefrina por cartucho: ${"%.1f".format(epi)} µg",
                    "Epinephrine per cartridge: ${"%.1f".format(epi)} µg"))
            }
        }
        NoticeCard(tr(lang,
            "El número mostrado NO es una indicación de cuántos cartuchos aplicar. El vasoconstrictor, la edad, enfermedades cardiovasculares, otros fármacos, embarazo, función hepática, técnica y ficha técnica pueden imponer un límite menor. AAPD recomienda usar la menor dosis total que logre anestesia eficaz.",
            "The displayed number is NOT a recommendation of how many cartridges to administer. Vasoconstrictor exposure, age, cardiovascular disease, other drugs, pregnancy, hepatic function, technique and the product label can impose a lower limit. AAPD recommends using the lowest total dose that provides effective anesthesia."))
    }
}
