package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.MedicationRecord

private fun normalizeMedication(text: String): String =
    text.trim().lowercase().replace(Regex("\\s+"), " ")

private fun medicationKey(m: MedicationRecord): String =
    listOf(m.activeIngredient.ifBlank { m.name }, m.dose, m.unit, m.route)
        .joinToString("|") { normalizeMedication(it) }

@Composable
fun MedicationManagementScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ingredient by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var indication by remember { mutableStateOf("") }
    var asNeeded by remember { mutableStateOf(false) }

    val medications = session.medicationsStructured
    val draft = MedicationRecord(
        name = name.trim(),
        activeIngredient = ingredient.trim(),
        dose = dose.trim(),
        unit = unit.trim(),
        route = route.trim(),
        frequency = frequency.trim(),
        indication = indication.trim(),
        asNeeded = asNeeded
    )
    val duplicate = if (draft.activeIngredient.isBlank()) false
    else medications.any { medicationKey(it) == medicationKey(draft) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) { Text("‹ ${tr(lang, "Volver", "Back")}") }
            Text(tr(lang, "Medicamentos", "Medications"), fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
        }

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(tr(lang, "Registro estructurado", "Structured record"), fontWeight = FontWeight.Black)
                Text(tr(lang, "Registra nombre, principio activo, dosis, vía, frecuencia e indicación por separado. Es un ejercicio educativo y no sustituye la prescripción.", "Record name, active ingredient, dose, route, frequency and indication separately. This is educational and does not replace prescribing."))
            }
        }

        OutlinedTextField(name, { name = it }, label = { Text(tr(lang, "Nombre comercial (opcional)", "Brand name (optional)")) }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(ingredient, { ingredient = it }, label = { Text(tr(lang, "Principio activo *", "Active ingredient *")) }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(dose, { dose = it }, label = { Text(tr(lang, "Dosis", "Dose")) }, singleLine = true, modifier = Modifier.weight(1f))
            OutlinedTextField(unit, { unit = it }, label = { Text(tr(lang, "Unidad", "Unit")) }, singleLine = true, modifier = Modifier.weight(1f))
        }
        OutlinedTextField(route, { route = it }, label = { Text(tr(lang, "Vía", "Route")) }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(frequency, { frequency = it }, label = { Text(tr(lang, "Frecuencia / pauta", "Frequency / schedule")) }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(indication, { indication = it }, label = { Text(tr(lang, "Indicación educativa", "Educational indication")) }, singleLine = true, modifier = Modifier.fillMaxWidth())

        FilterChip(asNeeded, { asNeeded = !asNeeded }, { Text(tr(lang, "A demanda", "As needed")) })

        if (duplicate) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Text(
                    tr(lang, "⚠ Posible duplicado: ya existe un medicamento con el mismo principio activo/nombre, dosis, unidad y vía.", "⚠ Possible duplicate: a medication with the same active ingredient/name, dose, unit and route already exists."),
                    Modifier.padding(12.dp), fontWeight = FontWeight.Bold
                )
            }
        }

        val canAdd = ingredient.isNotBlank() && dose.isNotBlank() && route.isNotBlank()
        Button(
            onClick = {
                if (canAdd && !duplicate) {
                    onSessionChanged(session.copy(medicationsStructured = medications + draft))
                    name = ""; ingredient = ""; dose = ""; unit = ""; route = ""; frequency = ""; indication = ""; asNeeded = false
                }
            },
            enabled = canAdd && !duplicate,
            modifier = Modifier.fillMaxWidth()
        ) { Text(tr(lang, "Agregar medicamento", "Add medication")) }

        Text("${medications.size} ${tr(lang, "medicamento(s) estructurado(s)", "structured medication(s)")}", fontWeight = FontWeight.Bold)

        medications.forEach { medication ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(medication.name.ifBlank { medication.activeIngredient }, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                    if (medication.activeIngredient.isNotBlank() && medication.name.isNotBlank()) Text("Principio activo: ${medication.activeIngredient}")
                    Text("Dosis: ${medication.dose} ${medication.unit}".trim())
                    Text("Vía: ${medication.route} · ${medication.frequency.ifBlank { "Pauta no especificada" }}")
                    if (medication.indication.isNotBlank()) Text("Indicación: ${medication.indication}")
                    if (medication.asNeeded) Text("A demanda", fontWeight = FontWeight.Bold)
                    OutlinedButton(
                        onClick = { onSessionChanged(session.copy(medicationsStructured = medications.filterNot { it.id == medication.id })) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(tr(lang, "Eliminar", "Remove")) }
                }
            }
        }

        if (medications.size >= 2) {
            val duplicateGroups = medications.groupBy(::medicationKey).values.count { it.size > 1 }
            Card(colors = CardDefaults.cardColors(containerColor = if (duplicateGroups > 0) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer)) {
                Text(
                    if (duplicateGroups > 0)
                        tr(lang, "⚠ Hay ${duplicateGroups} grupo(s) con posibles duplicados. Revisa principio activo/nombre, dosis, unidad y vía.", "⚠ There are ${duplicateGroups} possible duplicate group(s). Review active ingredient/name, dose, unit and route.")
                    else
                        tr(lang, "✓ No se detectaron duplicados exactos entre los medicamentos estructurados.", "✓ No exact duplicates were detected among structured medications."),
                    Modifier.padding(12.dp), fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
