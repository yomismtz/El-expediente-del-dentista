package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

data class TeachingField(val titleEs: String, val titleEn: String, val helpEs: String, val helpEn: String, val exampleEs: String, val exampleEn: String)

@Composable
fun IdentificationScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var opened by remember { mutableStateOf<Int?>(0) }
    val fields = listOf(
        TeachingField("Nombre", "Name", "Se registra el nombre completo tal como aparece en la identificación del paciente.", "Record the complete name as it appears on the patient's identification.", "Ejemplo: Apellido paterno · apellido materno · nombre(s).", "Example: family name(s) and given name(s)."),
        TeachingField("Edad y fecha de nacimiento", "Age and date of birth", "La edad se registra junto con la fecha de nacimiento para contextualizar dentición, crecimiento y antecedentes.", "Age and birth date help contextualize dentition, growth and history.", "Ejemplo: 10 años · 14/03/2016.", "Example: 10 years · 14/03/2016."),
        TeachingField("Sexo / género", "Sex / gender", "Anota el dato solicitado por el formato clínico de manera respetuosa y consistente.", "Record the information requested by the clinical form respectfully and consistently.", "Ejemplo: femenino / masculino / dato referido según el formato.", "Example: female / male / reported information according to the form."),
        TeachingField("Domicilio y teléfono", "Address and telephone", "Son datos de localización y contacto. En esta app solo se explica dónde se anotan; no se capturan datos reales.", "These are contact details. This app only teaches where they belong; no real data is entered.", "Ejemplo de estructura: calle, número, colonia, municipio y teléfono.", "Example structure: street, number, district, city and phone."),
        TeachingField("Ocupación, escolaridad y estado civil", "Occupation, education and marital status", "Se registran como parte del contexto social de la ficha de identificación.", "These are recorded as part of the social context in the identification sheet.", "Ejemplo: estudiante · primaria · soltero(a).", "Example: student · primary school · single."),
        TeachingField("Servicio de salud", "Health service", "Indica si cuenta con algún servicio de atención médica o seguridad social.", "Indicates whether the patient has access to a health service or insurance.", "Ejemplo: institución correspondiente o “ninguno”, según lo referido.", "Example: corresponding service or “none”, as reported."),
        TeachingField("Motivo de consulta", "Reason for consultation", "Debe reflejar de manera breve lo que refiere la persona como razón principal de la consulta.", "Briefly reflects what the person reports as the main reason for the visit.", "Ejemplo: “Vengo porque me duele una muela desde ayer”.", "Example: “I came because a tooth has hurt since yesterday”."),
        TeachingField("Padecimiento actual", "Current condition", "Describe inicio, desencadenante, evolución, signos, síntomas, factores que alivian o agravan y medicamentos relacionados.", "Describe onset, trigger, course, signs, symptoms, relieving/aggravating factors and related medication.", "Ejemplo: inició hace 2 días, espontáneo, aumenta con frío y persiste después de retirar el estímulo.", "Example: began 2 days ago, spontaneous, worsens with cold and lingers after the stimulus is removed.")
    )

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Ficha de identificación", "Identification sheet"),
                onBack,
                tr(lang,
                    "Aquí no se llena un paciente. Toca cada rubro para aprender qué información corresponde y ver un ejemplo de redacción.",
                    "No patient is entered here. Tap each field to learn what belongs there and see a writing example.")
            )
        }
        item {
            NoticeCard(tr(lang,
                "La app es una guía de llenado: no solicita nombre, teléfono, domicilio ni otros datos personales.",
                "This app is a completion guide: it does not request names, telephone numbers, addresses or other personal data."))
        }
        items(fields.size) { index ->
            val field = fields[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (opened == index) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                onClick = { opened = if (opened == index) null else index }
            ) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text(if (lang == "en") field.titleEn else field.titleEs, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    if (opened == index) {
                        Text("💡 ${if (lang == "en") field.helpEn else field.helpEs}")
                        Text("✍️ ${if (lang == "en") field.exampleEn else field.exampleEs}", color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(tr(lang, "Toca para ver cómo se llena", "Tap to see how it is completed"), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedDisease by remember { mutableStateOf<String?>(null) }
    var selectedAsa by remember { mutableStateOf(1) }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Anamnesis y antecedentes", "Anamnesis and medical history"),
                onBack,
                tr(lang,
                    "Aprende qué enfermedades pertenecen a cada grupo, cómo preguntarlas en lenguaje cotidiano y qué datos debes ampliar si la respuesta es positiva.",
                    "Learn which diseases belong to each group, how to ask about them conversationally and what details to expand when the answer is positive.")
            )
        }
        item {
            SectionCard("ASA") {
                Text(tr(lang,
                    "Toca una clasificación para ver su significado. Es una guía educativa; no estás clasificando a un paciente real.",
                    "Tap a class to see its meaning. This is an educational guide; you are not classifying a real patient."))
                ClinicalContent.asa.forEach { guide ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = if (selectedAsa == guide.value) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface),
                        onClick = { selectedAsa = guide.value }
                    ) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(if (lang == "en") guide.titleEn else guide.titleEs, fontWeight = FontWeight.Bold)
                            if (selectedAsa == guide.value) Text(if (lang == "en") guide.examplesEn else guide.examplesEs)
                        }
                    }
                }
            }
        }
        item {
            NoticeCard(tr(lang,
                "Cuando un antecedente es positivo, no basta escribir el nombre de la enfermedad: amplía inicio, evolución/estado actual, tratamiento o medicamentos y complicaciones.",
                "When a history item is positive, do not record only the disease name: expand onset, course/current status, treatment or medication and complications."))
        }
        items(ClinicalContent.diseases.size) { index ->
            val guide = ClinicalContent.diseases[index]
            val open = selectedDisease == guide.id
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (open) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                onClick = { selectedDisease = if (open) null else guide.id }
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text(if (lang == "en") guide.nameEn else guide.nameEs, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    if (open) {
                        Text("🗣️ ${if (lang == "en") guide.questionEn else guide.questionEs}")
                        Text("📚 ${tr(lang, "Incluye: ${guide.examplesEs}", "Includes: ${guide.examplesEn}")}")
                        Text(tr(lang,
                            "Después pregunta: ¿desde cuándo?, ¿qué tratamiento recibe?, ¿está controlado actualmente?, ¿ha presentado complicaciones?",
                            "Then ask: since when?, what treatment is used?, is it currently controlled?, have there been complications?"))
                    } else {
                        Text(tr(lang, "Toca para ver cómo preguntar", "Tap to see how to ask"), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun IntakeNoteScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    val rows = listOf(
        tr(lang, "Identificación del paciente", "Patient identification"),
        tr(lang, "Diagnóstico sistémico y clasificación ASA", "Systemic diagnosis and ASA classification"),
        tr(lang, "Medicamentos", "Medications"),
        tr(lang, "ATM y músculos", "TMJ and muscles"),
        tr(lang, "Diagnóstico de caries y anomalías", "Caries and anomaly diagnosis"),
        tr(lang, "Diagnóstico de oclusión", "Occlusal diagnosis"),
        tr(lang, "Diagnóstico de tejidos blandos / mucosas", "Soft-tissue / mucosal diagnosis"),
        "CPOD / ceod",
        tr(lang, "Diagnóstico periodontal", "Periodontal diagnosis"),
        tr(lang, "Diagnóstico pulpar", "Pulpal diagnosis"),
        tr(lang, "Diagnóstico protésico cuando corresponda", "Prosthetic diagnosis when applicable")
    )

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Nota de ingreso: cómo se integra", "Intake note: how it is assembled"),
                onBack,
                tr(lang,
                    "La nota de ingreso reúne de forma resumida lo obtenido en las distintas hojas del expediente. Aquí se aprende el orden y de dónde sale cada dato; no se genera un expediente real.",
                    "The intake note summarizes information from the different record sheets. Here you learn the order and source of each item; no real record is generated.")
            )
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(tr(lang, "NOTA DE INGRESO", "INTAKE NOTE"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    rows.forEach { row ->
                        Text("▸ $row: ______________________________")
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Cómo aprenderla", "How to learn it")) {
                Text(tr(lang,
                    "No memorices un texto fijo. Entra a cada pestaña del folder, aprende cómo se obtiene el dato y después vuelve a esta hoja para reconocer dónde se resume.",
                    "Do not memorize a fixed paragraph. Open each folder tab, learn how the information is obtained, then return here to recognize where it is summarized."))
            }
        }
    }
}
