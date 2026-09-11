package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.DiseaseAnswer
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

@Composable
fun IdentificationScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val p = session.profile
    fun update(block: (com.yomismtz.expedientedeldentista.clinical.PatientProfile) -> com.yomismtz.expedientedeldentista.clinical.PatientProfile) {
        onSessionChanged(session.copy(profile = block(p)))
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Ficha de identificación", "Identification sheet"),
                onBack,
                tr(lang, "Llena los campos para aprender qué información suele integrar la ficha. Usa datos ficticios durante el aprendizaje.",
                    "Complete the fields to learn what information is usually included. Use fictional data while learning.")
            )
        }
        item {
            NoticeCard(tr(lang,
                "Los datos de este ejercicio no se guardan como expediente ni se almacenan en una base de pacientes.",
                "Exercise data is not saved as a clinical record or stored in a patient database."))
        }
        item {
            SectionCard(tr(lang, "Datos de identificación", "Identification data")) {
                ProfileField(tr(lang, "Nombre ficticio o nombre del ejercicio", "Fictional name or exercise name"), p.exerciseName) { update { x -> x.copy(exerciseName = it) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = p.age,
                        onValueChange = { value -> update { it.copy(age = value) } },
                        label = { Text(tr(lang, "Edad", "Age")) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = p.sex,
                        onValueChange = { value -> update { it.copy(sex = value) } },
                        label = { Text(tr(lang, "Sexo/género", "Sex/gender")) },
                        modifier = Modifier.weight(1f)
                    )
                }
                ProfileField(tr(lang, "Fecha de nacimiento", "Date of birth"), p.birthDate) { update { x -> x.copy(birthDate = it) } }
                ProfileField(tr(lang, "Ocupación", "Occupation"), p.occupation) { update { x -> x.copy(occupation = it) } }
            }
        }
        item {
            SectionCard(tr(lang, "Motivo de consulta y padecimiento actual", "Reason for visit and current condition")) {
                Text(tr(lang,
                    "Motivo de consulta: registra de forma breve lo que refiere la persona. Padecimiento actual: describe inicio, evolución, síntomas, signos, factores que alivian o agravan y medicamentos relacionados.",
                    "Reason for visit: briefly record what the person reports. Current condition: describe onset, course, symptoms, signs, relieving/aggravating factors and related medication."))
                ProfileField(tr(lang, "Motivo de consulta", "Reason for visit"), p.reasonForVisit, minLines = 2) { update { x -> x.copy(reasonForVisit = it) } }
                ProfileField(tr(lang, "Padecimiento actual", "Current condition"), p.currentCondition, minLines = 3) { update { x -> x.copy(currentCondition = it) } }
            }
        }
        item {
            SectionCard(tr(lang, "Medicamentos y alergias", "Medications and allergies")) {
                ProfileField(tr(lang, "Medicamentos que toma", "Current medications"), p.medications, minLines = 2) { update { x -> x.copy(medications = it) } }
                ProfileField(tr(lang, "Alergias", "Allergies"), p.allergies, minLines = 2) { update { x -> x.copy(allergies = it) } }
            }
        }
        item {
            SectionCard(tr(lang, "Ficha generada automáticamente", "Automatically generated identification sheet")) {
                Text(
                    tr(
                        lang,
                        "Nombre/ejercicio: ${p.exerciseName.ifBlank { "—" }}\nEdad: ${p.age.ifBlank { "—" }} · Sexo/género: ${p.sex.ifBlank { "—" }}\nNacimiento: ${p.birthDate.ifBlank { "—" }}\nOcupación: ${p.occupation.ifBlank { "—" }}\nMotivo de consulta: ${p.reasonForVisit.ifBlank { "—" }}\nMedicamentos: ${p.medications.ifBlank { "—" }}\nAlergias: ${p.allergies.ifBlank { "—" }}",
                        "Name/exercise: ${p.exerciseName.ifBlank { "—" }}\nAge: ${p.age.ifBlank { "—" }} · Sex/gender: ${p.sex.ifBlank { "—" }}\nBirth date: ${p.birthDate.ifBlank { "—" }}\nOccupation: ${p.occupation.ifBlank { "—" }}\nReason for visit: ${p.reasonForVisit.ifBlank { "—" }}\nMedications: ${p.medications.ifBlank { "—" }}\nAllergies: ${p.allergies.ifBlank { "—" }}"
                    )
                )
            }
        }
        item {
            SectionCard(tr(lang, "Signos vitales para las notas de evolución", "Vital signs for progress notes")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(p.bloodPressure, { value -> update { it.copy(bloodPressure = value) } }, label = { Text("TA / BP") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(p.heartRate, { value -> update { it.copy(heartRate = value) } }, label = { Text("FC / HR") }, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(p.respiratoryRate, { value -> update { it.copy(respiratoryRate = value) } }, label = { Text("FR / RR") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(p.temperature, { value -> update { it.copy(temperature = value) } }, label = { Text(tr(lang, "Temp.", "Temp.")) }, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String, minLines: Int = 1, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        minLines = minLines,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun HistoryScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val h = session.history
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Historia clínica y anamnesis", "Medical history and anamnesis"),
                onBack,
                tr(lang, "La guía muestra una forma coloquial de preguntar y después ordena inicio, tratamiento, estado actual y complicaciones.",
                    "The guide shows a conversational way to ask, then organizes onset, treatment, current status and complications.")
            )
        }
        item {
            SectionCard("ASA") {
                Text(tr(lang,
                    "Selecciona la clasificación que corresponda al ejercicio. La app muestra ejemplos educativos; la clasificación real requiere juicio clínico.",
                    "Select the class that fits the exercise. The app shows educational examples; real classification requires clinical judgment."))
                ClinicalContent.asa.forEach { guide ->
                    FilterChip(
                        selected = h.asaClass == guide.value,
                        onClick = { onSessionChanged(session.copy(history = h.copy(asaClass = guide.value))) },
                        label = { Text(if (lang == "en") guide.titleEn else guide.titleEs) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (h.asaClass == guide.value) {
                        Text(if (lang == "en") guide.examplesEn else guide.examplesEs, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                BooleanRow(
                    tr(lang, "Agregar E si el procedimiento es de emergencia", "Add E for an emergency procedure"),
                    h.asaEmergency
                ) { checked -> onSessionChanged(session.copy(history = h.copy(asaEmergency = checked))) }
            }
        }
        item {
            SectionCard(tr(lang, "Hábitos y antecedentes generales", "Habits and general history")) {
                ProfileField(tr(lang, "Tabaco / alcohol: tipo, frecuencia y cantidad", "Tobacco / alcohol: type, frequency and amount"), h.tobaccoAlcohol, 2) {
                    onSessionChanged(session.copy(history = h.copy(tobaccoAlcohol = it)))
                }
                ProfileField(tr(lang, "Hospitalizaciones previas", "Previous hospitalizations"), h.hospitalizations, 2) {
                    onSessionChanged(session.copy(history = h.copy(hospitalizations = it)))
                }
                ProfileField(tr(lang, "Embarazo o sospecha, cuando corresponda", "Pregnancy or suspected pregnancy, when applicable"), h.pregnancy, 2) {
                    onSessionChanged(session.copy(history = h.copy(pregnancy = it)))
                }
            }
        }
        items(ClinicalContent.diseases.size) { index ->
            val guide = ClinicalContent.diseases[index]
            val answer = h.diseases[guide.id] ?: DiseaseAnswer()
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (answer.present) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = answer.present,
                            onCheckedChange = { checked ->
                                val map = h.diseases.toMutableMap()
                                map[guide.id] = answer.copy(present = checked)
                                onSessionChanged(session.copy(history = h.copy(diseases = map)))
                            }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(if (lang == "en") guide.nameEn else guide.nameEs, fontWeight = FontWeight.Bold)
                            Text(if (lang == "en") guide.questionEn else guide.questionEs)
                            Text(
                                tr(lang, "Ejemplos: ${guide.examplesEs}", "Examples: ${guide.examplesEn}"),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    if (answer.present) {
                        DiseaseDetailFields(lang, answer) { updated ->
                            val map = h.diseases.toMutableMap()
                            map[guide.id] = updated
                            onSessionChanged(session.copy(history = h.copy(diseases = map)))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiseaseDetailFields(lang: String, value: DiseaseAnswer, onChange: (DiseaseAnswer) -> Unit) {
    ProfileField(tr(lang, "Inicio / desde cuándo", "Onset / since when"), value.onset) { onChange(value.copy(onset = it)) }
    ProfileField(tr(lang, "Tratamiento / medicamentos", "Treatment / medications"), value.treatment) { onChange(value.copy(treatment = it)) }
    ProfileField(tr(lang, "Estado actual / control", "Current status / control"), value.currentStatus) { onChange(value.copy(currentStatus = it)) }
    ProfileField(tr(lang, "Complicaciones", "Complications"), value.complications) { onChange(value.copy(complications = it)) }
}

@Composable
fun IntakeNoteScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    val note = ClinicalEngines.generateIntakeNote(session, lang)
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Nota de ingreso automática", "Automatic intake note"),
                onBack,
                tr(lang, "Se actualiza con lo que llenas en las demás pestañas; no necesitas capturar la misma información dos veces.",
                    "It updates from the other tabs, so the same information does not need to be entered twice.")
            )
        }
        item { NoticeCard(tr(lang,
            "Este texto es un ejemplo didáctico generado dentro de la sesión. No sustituye una nota clínica real ni se guarda como expediente.",
            "This is an educational example generated within the session. It does not replace a real clinical note and is not stored as a record.")) }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(note, modifier = Modifier.padding(18.dp), style = MaterialTheme.typography.bodyLarge)
            }
        }
        item {
            SectionCard(tr(lang, "¿De dónde sale cada rubro?", "Where does each section come from?")) {
                Text(tr(lang,
                    "Identificación y motivo de consulta → Ficha de identificación.\nASA, enfermedades, medicamentos y alergias → Historia clínica.\nCPOD/ceod → Odontograma.\nIPC, IHOS y O’Leary → sus respectivas pestañas.\nDiagnóstico pulpar/periapical → selección de signos, síntomas y radiografía.",
                    "Identification and reason for visit → Identification sheet.\nASA, diseases, medications and allergies → Medical history.\nDMFT/dmft → Odontogram.\nCPI, OHI-S and O’Leary → their respective tabs.\nPulpal/periapical diagnosis → selected symptoms, signs and radiographic findings."))
            }
        }
    }
}
