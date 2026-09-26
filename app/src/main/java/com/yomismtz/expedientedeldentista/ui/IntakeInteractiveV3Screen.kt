package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent

private val IntakeLavender = Color(0xFFE9DDF5)
private val IntakeLilac = Color(0xFFD3BCE9)
private val IntakePurple = Color(0xFF7447A3)
private val IntakeDeep = Color(0xFF43235F)
private val IntakeMint = Color(0xFF66D6C7)
private val IntakeTurquoise = Color(0xFF2EB9B1)
private val IntakePaper = Color(0xFFFFFCFF)

private data class MedicationGuide(
    val name: String,
    val purposeEs: String,
    val purposeEn: String,
    val dentalEs: String,
    val dentalEn: String,
    val schedulesEs: List<String>,
    val schedulesEn: List<String>
)

private val medicationGuides = listOf(
    MedicationGuide(
        "Metformina",
        "Antidiabético oral.", "Oral antidiabetic medicine.",
        "Interesa conocer control glucémico, horario de alimentos y otros fármacos. Registrar exactamente el esquema referido; la app no modifica tratamientos.",
        "Review glycemic control, meal timing and other medicines. Record the reported regimen exactly; the app does not modify treatment.",
        listOf("1 vez al día", "cada 12 h", "cada 8 h", "liberación prolongada 1 vez al día", "otro esquema referido"),
        listOf("once daily", "every 12 h", "every 8 h", "extended release once daily", "other reported regimen")
    ),
    MedicationGuide(
        "Losartán",
        "Antihipertensivo.", "Antihypertensive medicine.",
        "Relacionar con control de tensión arterial, mareo/hipotensión y medicamentos concomitantes.",
        "Relate to blood-pressure control, dizziness/hypotension and concomitant medicines.",
        listOf("1 vez al día", "cada 12 h", "otro esquema referido"),
        listOf("once daily", "every 12 h", "other reported regimen")
    ),
    MedicationGuide(
        "Enalapril",
        "Antihipertensivo inhibidor de ECA.", "ACE-inhibitor antihypertensive.",
        "Registrar esquema referido y revisar control de TA; preguntar por tos, mareo y otros medicamentos.",
        "Record the reported regimen and review BP control; ask about cough, dizziness and other medicines.",
        listOf("1 vez al día", "cada 12 h", "otro esquema referido"),
        listOf("once daily", "every 12 h", "other reported regimen")
    ),
    MedicationGuide(
        "Insulina",
        "Tratamiento para diabetes.", "Diabetes treatment.",
        "Importa el tipo de insulina, horario, alimentación y riesgo de hipoglucemia. Confirmar siempre el esquema que la persona realmente usa.",
        "Type, timing, meals and hypoglycemia risk matter. Always confirm the regimen the person actually uses.",
        listOf("basal 1 vez al día", "antes de comidas", "basal + bolos", "otro esquema referido"),
        listOf("basal once daily", "before meals", "basal + bolus", "other reported regimen")
    ),
    MedicationGuide(
        "Ácido acetilsalicílico",
        "Antiagregante en ciertos contextos.", "Antiplatelet in selected contexts.",
        "Registrar indicación y dosis referida; considerar su relevancia para sangrado y no suspenderlo por cuenta propia.",
        "Record indication and reported dose; consider bleeding relevance and never stop it independently.",
        listOf("1 vez al día", "otro esquema referido"),
        listOf("once daily", "other reported regimen")
    ),
    MedicationGuide(
        "Clopidogrel",
        "Antiagregante plaquetario.", "Antiplatelet medicine.",
        "Es relevante para valoración de sangrado y antecedentes cardiovasculares. No indicar suspensión sin valoración médica correspondiente.",
        "Relevant to bleeding assessment and cardiovascular history. Do not advise stopping it without the appropriate medical evaluation.",
        listOf("1 vez al día", "otro esquema referido"),
        listOf("once daily", "other reported regimen")
    ),
    MedicationGuide(
        "Salbutamol",
        "Broncodilatador inhalado.", "Inhaled bronchodilator.",
        "Preguntar frecuencia de uso, control del asma y si lleva inhalador de rescate.",
        "Ask frequency of use, asthma control and whether a rescue inhaler is available.",
        listOf("según necesidad", "esquema fijo referido", "otro esquema referido"),
        listOf("as needed", "reported scheduled use", "other reported regimen")
    ),
    MedicationGuide(
        "Levotiroxina",
        "Hormona tiroidea.", "Thyroid hormone.",
        "Registrar horario, control médico y otras enfermedades o medicamentos relevantes.",
        "Record timing, medical follow-up and other relevant conditions or medicines.",
        listOf("1 vez al día", "otro esquema referido"),
        listOf("once daily", "other reported regimen")
    )
)

@Composable
fun IntakeInteractiveV3Screen(
    lang: String,
    onIdentification: () -> Unit,
    onHistory: () -> Unit,
    onVitals: () -> Unit,
    onAtm: () -> Unit,
    onOcclusion: () -> Unit,
    onMucosa: () -> Unit,
    onCpod: () -> Unit,
    onPeriodontal: () -> Unit,
    onPulpal: () -> Unit,
    onProsthetic: () -> Unit,
    onBack: () -> Unit
) {
    var selectedAsa by rememberRecordState("intake.selectedAsa",1)
    var selectedMedication by rememberRecordState("intake.selectedMedication",medicationGuides.first())
    var selectedSchedule by rememberRecordState("intake.selectedSchedule",selectedMedication.schedulesEs.first())

    fun schedules() = if (lang == "en") selectedMedication.schedulesEn else selectedMedication.schedulesEs

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Nota de ingreso interactiva", "Interactive intake note"),
                onBack,
                tr(lang,
                    "Centro de navegación para aprender el llenado del expediente físico. Cada apartado abre su guía o examen interactivo correspondiente.",
                    "Navigation hub for learning how to complete the physical record. Each section opens its corresponding guide or interactive examination.")
            )
        }

        item {
            NoticeCard(tr(lang,
                "Uso educativo: no introduzcas nombre, teléfono, domicilio ni otros datos identificables de pacientes reales. La app te enseña qué preguntar, explorar y escribir en el expediente físico.",
                "Educational use: do not enter names, telephone numbers, addresses or other identifying data of real patients. The app teaches what to ask, examine and write in the physical record."))
        }

        item {
            IntakeSectionCard("1", tr(lang,"Identificación","Identification")) {
                Text(tr(lang,
                    "Aprende cómo registrar edad/fecha de nacimiento, sexo según el formato, ocupación, escolaridad, servicio de salud, motivo de consulta y padecimiento actual.",
                    "Learn how to record age/date of birth, sex as required by the form, occupation, education, health service, reason for consultation and current condition."))
                Button(onClick = onIdentification, modifier = Modifier.fillMaxWidth()) {
                    Text(tr(lang,"Abrir guía de identificación","Open identification guide"))
                }
                WhatToWriteCard(
                    tr(lang,"¿Qué escribo al final en el expediente?","What do I write in the record?"),
                    tr(lang,
                        "Redacta solo los datos que solicita tu formato físico y el motivo/padecimiento actual de forma breve, cronológica y fiel a lo referido.",
                        "Record only the data requested by the physical form and summarize the reason/current condition briefly, chronologically and faithfully to what was reported."))
            }
        }

        item {
            IntakeSectionCard("2", "ASA") {
                Text(tr(lang,
                    "Selecciona una clase para estudiar qué significa. Después abre Anamnesis para revisar los antecedentes que la sustentan.",
                    "Select a class to study its meaning. Then open History to review the findings that support it."))
                ClinicalContent.asa.forEach { guide ->
                    FilterChip(
                        selected = selectedAsa == guide.value,
                        onClick = { selectedAsa = guide.value },
                        label = { Text("ASA ${guide.value}") },
                        modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                    )
                }
                val asa = ClinicalContent.asa.first { it.value == selectedAsa }
                Card(colors = CardDefaults.cardColors(containerColor = IntakeMint.copy(alpha = .18f))) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(if(lang=="en") asa.titleEn else asa.titleEs, fontWeight = FontWeight.Bold, color = IntakeDeep)
                        Text(if(lang=="en") asa.examplesEn else asa.examplesEs)
                    }
                }
                OutlinedButton(onClick = onHistory, modifier = Modifier.fillMaxWidth()) {
                    Text(tr(lang,"Abrir Anamnesis / antecedentes","Open history / medical background"))
                }
                WhatToWriteCard(
                    tr(lang,"¿Qué escribo al final en el expediente?","What do I write in the record?"),
                    tr(lang,
                        "Ejemplo de estructura: “ASA ${selectedAsa}: [antecedentes y estado actual que sustentan la clasificación]”. Si faltan datos, escribe que la clasificación requiere completar la valoración y confirmar con supervisión docente.",
                        "Example structure: “ASA ${selectedAsa}: [history and current status supporting the classification]”. If data are missing, state that assessment must be completed and confirmed with faculty supervision."))
            }
        }

        item {
            IntakeSectionCard("3", tr(lang,"Medicamentos actuales","Current medications")) {
                Text(tr(lang,
                    "Selecciona un medicamento frecuente para estudiar qué debes preguntar. El objetivo es registrar el esquema referido por la persona, no sugerir cambios de tratamiento.",
                    "Select a common medicine to study what should be asked. The goal is to record the person's reported regimen, not suggest treatment changes."))
                medicationGuides.chunked(2).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEach { med ->
                            FilterChip(
                                selected = selectedMedication.name == med.name,
                                onClick = {
                                    selectedMedication = med
                                    selectedSchedule = if(lang=="en") med.schedulesEn.first() else med.schedulesEs.first()
                                },
                                label = { Text(med.name) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) Column(Modifier.weight(1f)) {}
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = IntakeLavender.copy(alpha = .55f))) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(selectedMedication.name, fontWeight = FontWeight.Black, color = IntakeDeep)
                        Text(if(lang=="en") selectedMedication.purposeEn else selectedMedication.purposeEs)
                        Text(if(lang=="en") selectedMedication.dentalEn else selectedMedication.dentalEs)
                        Text(tr(lang,"Esquema referido para practicar el registro:","Reported regimen for documentation practice:"), fontWeight = FontWeight.Bold)
                        schedules().forEach { schedule ->
                            FilterChip(
                                selected = selectedSchedule == schedule,
                                onClick = { selectedSchedule = schedule },
                                label = { Text(schedule) },
                                modifier = Modifier.padding(end = 4.dp, bottom = 3.dp)
                            )
                        }
                    }
                }
                WhatToWriteCard(
                    tr(lang,"¿Qué escribo al final en el expediente?","What do I write in the record?"),
                    tr(lang,
                        "Ejemplo de estructura: “Refiere ${selectedMedication.name}, vía ___, presentación/dosis referida ___, $selectedSchedule, por ___”. Confirma el esquema con la receta, envase o fuente clínica disponible cuando sea posible.",
                        "Example structure: “Reports ${selectedMedication.name}, route ___, reported strength/dose ___, $selectedSchedule, for ___”. Confirm the regimen with the prescription, container or available clinical source when possible."))
            }
        }

        item {
            IntakeSectionCard("4", tr(lang,"Exámenes y análisis relacionados","Related examinations and analyses")) {
                Text(tr(lang,
                    "Toca el examen que estés realizando. Cada módulo debe terminar con una sección “¿Qué escribo al final en el expediente?”.",
                    "Tap the examination you are performing. Each module should end with a “What do I write in the record?” section."), fontWeight = FontWeight.SemiBold)
                QuickExamButton(tr(lang,"♥ Signos vitales","♥ Vital signs"), onVitals)
                QuickExamButton(tr(lang,"◌ ATM y músculos","◌ TMJ and muscles"), onAtm)
                QuickExamButton(tr(lang,"◇ Oclusión","◇ Occlusion"), onOcclusion)
                QuickExamButton(tr(lang,"◡ Mucosas","◡ Mucosa"), onMucosa)
                QuickExamButton(tr(lang,"+ CPOD / ceod","+ DMFT / dmft"), onCpod)
                QuickExamButton(tr(lang,"⌇ Periodontal","⌇ Periodontal"), onPeriodontal)
                QuickExamButton(tr(lang,"⚡ Pulpar / periapical","⚡ Pulpal / apical"), onPulpal)
                QuickExamButton(tr(lang,"⌒ Prótesis / Kennedy","⌒ Prosthetics / Kennedy"), onProsthetic)
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IntakeTurquoise.copy(alpha = .14f)),
                border = BorderStroke(1.dp, IntakeTurquoise),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(tr(lang,"Regla de la app","App rule"), fontWeight = FontWeight.Black, color = IntakeDeep, style = MaterialTheme.typography.titleMedium)
                    Text(tr(lang,
                        "Explorar → interpretar → revisar por qué → ver cómo redactarlo → volver a Nota de ingreso.",
                        "Examine → interpret → review why → see how to write it → return to Intake note."))
                }
            }
        }
    }
}

@Composable
private fun IntakeSectionCard(number:String,title:String,content:@Composable ()->Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = IntakePaper),
        border = BorderStroke(1.dp, IntakeLilac),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text("$number  $title", fontWeight = FontWeight.Black, color = IntakeDeep, style = MaterialTheme.typography.titleLarge)
            content()
        }
    }
}

@Composable
private fun WhatToWriteCard(title:String,text:String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = IntakeMint.copy(alpha = .18f)),
        border = BorderStroke(1.dp, IntakeTurquoise),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("✍️ $title", fontWeight = FontWeight.Black, color = IntakeDeep)
            Text(text)
        }
    }
}

@Composable
private fun QuickExamButton(label:String,onClick:()->Unit) {
    OutlinedButton(onClick=onClick,modifier=Modifier.fillMaxWidth()) { Text(label,fontWeight=FontWeight.SemiBold) }
}
