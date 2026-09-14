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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.PediatricDoseBasis
import com.yomismtz.expedientedeldentista.clinical.QuickSignsSymptomsState
import com.yomismtz.expedientedeldentista.clinical.calculateLocalAnesthetic
import com.yomismtz.expedientedeldentista.clinical.calculatePediatricDose
import com.yomismtz.expedientedeldentista.clinical.localAnestheticSpecsV26

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
            "Herramientas matemáticas educativas. Verifica siempre peso, presentación, ficha técnica y la indicación clínica antes de utilizar un resultado.",
            "Educational math tools. Always verify weight, formulation, product label and clinical indication before using a result."),
        onBack
    ) { profile ->
        AdaptiveGridV17(2, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            FilterChip(
                selected = tab == index,
                onClick = { tab = index },
                label = { Text(if (index == 0) tr(lang, "Dosis pediátrica", "Pediatric dose") else tr(lang, "Anestésicos locales", "Local anesthetics")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (tab == 0) PediatricDoseCalculatorV26(lang) else LocalAnestheticCalculatorV26(lang)
    }
}

@Composable
private fun PediatricDoseCalculatorV26(lang: String) {
    var weight by remember { mutableStateOf("") }
    var orderedMgKg by remember { mutableStateOf("") }
    var concentration by remember { mutableStateOf("") }
    var dosesPerDay by remember { mutableStateOf("") }
    var basis by remember { mutableStateOf(PediatricDoseBasis.PER_DOSE) }

    val result = calculatePediatricDose(
        weightKg = weight.toDoubleOrNull() ?: 0.0,
        orderedMgKg = orderedMgKg.toDoubleOrNull() ?: 0.0,
        concentrationMgMl = concentration.toDoubleOrNull() ?: 0.0,
        dosesPerDay = dosesPerDay.toIntOrNull() ?: 0,
        basis = basis
    )

    ResponsiveSectionV17(tr(lang, "Cálculo de dosis pediátrica", "Pediatric dose calculation")) {
        NoticeCard(tr(lang,
            "La app NO selecciona medicamento ni recomienda una dosis. Introduce la dosis en mg/kg tomada de una prescripción, protocolo o fuente farmacológica verificada.",
            "The app does NOT select a medicine or recommend a dose. Enter the mg/kg dose from a verified prescription, protocol, or drug reference."))
        OutlinedTextField(weight, { weight = decimalInputV26(it) }, label = { Text(tr(lang, "Peso (kg)", "Weight (kg)")) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(orderedMgKg, { orderedMgKg = decimalInputV26(it) }, label = { Text(tr(lang, "Dosis indicada (mg/kg)", "Ordered dose (mg/kg)")) }, modifier = Modifier.fillMaxWidth())
        ChoiceGridV26(
            lang,
            "La dosis indicada está expresada como…",
            "The ordered dose is expressed as…",
            basis.name,
            listOf(
                ChoiceV26(PediatricDoseBasis.PER_DOSE.name, "mg/kg por dosis", "mg/kg per dose"),
                ChoiceV26(PediatricDoseBasis.PER_DAY.name, "mg/kg por día", "mg/kg per day")
            )
        ) { basis = PediatricDoseBasis.valueOf(it) }
        OutlinedTextField(dosesPerDay, { dosesPerDay = it.filter(Char::isDigit).take(2) }, label = { Text(tr(lang, "Número de dosis al día", "Doses per day")) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            concentration,
            { concentration = decimalInputV26(it) },
            label = { Text(tr(lang, "Concentración (mg/mL)", "Concentration (mg/mL)")) },
            supportingText = { Text(tr(lang, "Convierte primero la presentación a mg/mL si la etiqueta está en mg/5 mL.", "Convert the formulation to mg/mL first if the label is in mg/5 mL.")) },
            modifier = Modifier.fillMaxWidth()
        )
    }

    ResponsiveSectionV17(tr(lang, "Resultado matemático", "Mathematical result")) {
        if (result == null) {
            Text(tr(lang, "Completa peso, dosis, frecuencia y concentración con valores mayores que cero.", "Enter weight, dose, frequency and concentration with values greater than zero."))
        } else {
            Text(tr(lang, "Por dosis: ${"%.2f".format(result.doseMg)} mg", "Per dose: ${"%.2f".format(result.doseMg)} mg"), fontWeight = FontWeight.Black)
            Text(tr(lang, "Volumen por dosis: ${"%.2f".format(result.mlPerDose)} mL", "Volume per dose: ${"%.2f".format(result.mlPerDose)} mL"), fontWeight = FontWeight.Black)
            Text(tr(lang, "Total diario matemático: ${"%.2f".format(result.dailyMg)} mg/día", "Mathematical daily total: ${"%.2f".format(result.dailyMg)} mg/day"))
            NoticeCard(tr(lang,
                "Antes de usar el resultado compara la dosis por toma y la dosis diaria con el máximo permitido para ESE medicamento, edad, indicación y función renal/hepática. La calculadora no realiza esa decisión clínica.",
                "Before using the result, compare the per-dose and daily dose with the maximum for THAT medicine, age, indication, and renal/hepatic function. The calculator does not make that clinical decision."))
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
            supportingText = { Text(tr(lang, "No asumas 1.8 mL: confirma lo impreso en el cartucho/caja.", "Do not assume 1.8 mL: confirm the cartridge/box label.")) },
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
            "El número mostrado NO es una indicación de cuántos cartuchos aplicar. El vasoconstrictor, la edad, enfermedades cardiovasculares, otros fármacos, embarazo, función hepática, técnica y ficha técnica pueden imponer un límite menor. Usa siempre la menor dosis eficaz y verifica el producto disponible.",
            "The displayed number is NOT a recommendation of how many cartridges to administer. Vasoconstrictor exposure, age, cardiovascular disease, other drugs, pregnancy, hepatic function, technique and the product label can impose a lower limit. Always use the lowest effective dose and verify the actual product."))
    }
}
