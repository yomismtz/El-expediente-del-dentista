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
                AsaReferenceIllustration(lang)
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
    var opened by remember { mutableStateOf<Int?>(0) }
    val fields = listOf(
        TeachingField(
            "Identificación del paciente", "Patient identification",
            "Resume los datos de identificación que solicita el expediente físico: nombre, número de expediente, fecha, edad/fecha de nacimiento, sexo y datos generales pertinentes. La app no captura datos personales reales.",
            "Summarize the identification data requested by the physical record. The app does not collect real personal data.",
            "Ejemplo didáctico: Paciente ficticio de 21 años, sexo registrado femenino, expediente de práctica 0001, valoración inicial.",
            "Teaching example: Fictional 21-year-old patient, recorded sex female, practice record 0001, initial assessment."
        ),
        TeachingField(
            "Signos vitales", "Vital signs",
            "Antes de integrar la nota revisa TA, frecuencia cardiaca, frecuencia respiratoria, temperatura, peso y talla; agrega IMC o glucosa capilar cuando corresponda al ejercicio. Usa la pestaña Vitales para aprender la técnica e interpretación por edad.",
            "Review BP, heart rate, respiratory rate, temperature, weight and height; add BMI or capillary glucose when appropriate to the exercise.",
            "Ejemplo de estructura: TA ___/___ mmHg · FC ___ lpm · FR ___ rpm · T ___ °C · peso ___ kg · talla ___ m.",
            "Example structure: BP ___/___ mmHg · HR ___ bpm · RR ___ rpm · T ___ °C · weight ___ kg · height ___ m."
        ),
        TeachingField(
            "Diagnóstico sistémico y clasificación ASA", "Systemic diagnosis and ASA classification",
            "Integra los antecedentes médicos relevantes y la clasificación ASA que el alumno sustentaría con la historia clínica. Si faltan datos, la redacción debe decir que la clasificación requiere completar la valoración.",
            "Integrate relevant medical history and the ASA class supported by the history. If data are missing, state that assessment must be completed.",
            "Ejemplo: Antecedente de hipertensión referida en tratamiento; clasificación ASA por confirmar con control actual, signos vitales y supervisión docente.",
            "Example: History of treated hypertension; ASA classification to be confirmed with current control, vital signs and faculty supervision."
        ),
        TeachingField(
            "Medicamentos que está tomando", "Current medications",
            "Documenta lo que el paciente refiere: nombre genérico, presentación/dosis si la conoce, vía, frecuencia y motivo. Puedes reconocer medicamentos frecuentes como metformina, insulina, losartán, enalapril, AAS, clopidogrel, anticoagulantes, salbutamol o levotiroxina. La app enseña a registrar el esquema referido, no a prescribirlo.",
            "Document the medication as reported: generic name, known strength, route, frequency and reason. This teaches documentation, not prescribing.",
            "Ejemplo: Refiere metformina por vía oral; registrar presentación y frecuencia exactamente como las refiere o como aparecen en su receta/envase.",
            "Example: Reports oral metformin; document strength and frequency exactly as reported or shown on the prescription/container."
        ),
        TeachingField(
            "ATM y músculos", "TMJ and muscles",
            "Resume interrogatorio y exploración: dolor, ruidos, limitación, apertura, trayectoria mandibular, palpación de ATM y músculos de la masticación. El módulo ATM orienta hacia el hallazgo más compatible sin sustituir el diagnóstico docente.",
            "Summarize history and examination: pain, sounds, limitation, opening, mandibular path, TMJ and masticatory muscle palpation.",
            "Ejemplo: Apertura conservada, sin dolor a la palpación, sin ruidos articulares; sin hallazgos positivos en el ejercicio.",
            "Example: Preserved opening, no tenderness on palpation, no joint sounds; no positive findings in the exercise."
        ),
        TeachingField(
            "Diagnóstico de caries y anomalías", "Caries and anomaly diagnosis",
            "Se resume a partir del odontograma y, cuando se use, del ICDAS por superficie. Señala dientes con lesiones, restauraciones, ausencias y anomalías relevantes; evita inventar diagnósticos que no fueron explorados.",
            "Summarize from the odontogram and, when used, surface-based ICDAS. Record lesions, restorations, missing teeth and relevant anomalies.",
            "Ejemplo: Hallazgos de caries registrados en OD __ y __; consultar odontograma/ICDAS para superficies y códigos.",
            "Example: Caries findings recorded on teeth __ and __; see odontogram/ICDAS for surfaces and codes."
        ),
        TeachingField(
            "Diagnóstico de oclusión", "Occlusal diagnosis",
            "Describe lo que corresponda a la edad y dentición: Angle, relación canina, plano terminal en temporal, overjet, overbite, líneas medias y presencia de mordida abierta, profunda o cruzada.",
            "Describe findings appropriate to age and dentition: Angle, canine relation, primary terminal plane, overjet, overbite, midlines and open/deep/crossbite.",
            "Ejemplo: Dentición permanente; relación molar Clase I bilateral, línea media coincidente, overjet y overbite dentro de la referencia del ejercicio.",
            "Example: Permanent dentition; bilateral Class I molar relation, coincident midline, overjet and overbite within the exercise reference."
        ),
        TeachingField(
            "Diagnóstico de mucosas orales", "Oral mucosal diagnosis",
            "Resume la exploración sistemática de labios, carrillos, encía, lengua, piso de boca, paladar y orofaringe. Si existe una lesión, describe localización, tamaño, color, superficie, base/consistencia y síntomas.",
            "Summarize systematic examination of lips, cheeks, gingiva, tongue, floor, palate and oropharynx. Describe any lesion by site, size, color, surface, base/consistency and symptoms.",
            "Ejemplo: Mucosas húmedas e íntegras en el ejercicio; sin cambios visibles. Si hay lesión: describirla, no nombrarla solo por apariencia.",
            "Example: Moist intact mucosa in the exercise; no visible changes. If a lesion exists, describe it rather than naming it from appearance alone."
        ),
        TeachingField(
            "Índice CPOD / ceod", "DMFT / dmft index",
            "Registra el índice que corresponda a la dentición y conserva el detalle de cómo se obtuvo a partir de dientes cariados, perdidos/extraídos u obturados según el método enseñado.",
            "Record the index appropriate to dentition and retain how it was obtained from decayed, missing/extracted and filled teeth according to the taught method.",
            "Ejemplo: CPOD = C__ + P__ + O__ = __; ceod = c__ + e__ + o__ = __ cuando corresponda.",
            "Example: DMFT = D__ + M__ + F__ = __; dmft when applicable."
        ),
        TeachingField(
            "Diagnóstico periodontal", "Periodontal diagnosis",
            "Integra IPC por sextantes, IHOS y periodontograma cuando se hayan realizado. Distingue el resultado de un índice de un diagnóstico periodontal completo y menciona los hallazgos que lo apoyan.",
            "Integrate CPI by sextants, OHI-S and the periodontal chart when performed. Distinguish an index result from a complete periodontal diagnosis.",
            "Ejemplo: IPC S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__; IHOS __. Complementar con sondaje y valoración periodontal.",
            "Example: CPI S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__; OHI-S __. Complete with probing and periodontal assessment."
        ),
        TeachingField(
            "Diagnóstico pulpar y periapical", "Pulpal and apical diagnosis",
            "Los diagnósticos pulpar y periapical se escriben por separado. Deben estar sustentados por síntomas, pruebas térmicas cuando correspondan, percusión, palpación, sondaje, movilidad y hallazgos radiográficos disponibles.",
            "Write pulpal and apical diagnoses separately and support them with symptoms, tests and available radiographic findings.",
            "Ejemplo: OD 36 · Pulpar: hallazgos más compatibles con ____ · Periapical: hallazgos más compatibles con ____ · faltan/soportan estas pruebas: ____.",
            "Example: Tooth 36 · Pulpal: findings most compatible with ____ · Apical: findings most compatible with ____ · supporting/missing tests: ____."
        ),
        TeachingField(
            "Diagnóstico protésico / Kennedy", "Prosthetic diagnosis / Kennedy",
            "Cuando exista edentulismo parcial, identifica dientes presentes y ausentes en cada arcada y practica la clasificación de Kennedy con las reglas enseñadas. Señala si la información es insuficiente antes de proponer una clase.",
            "For partial edentulism, identify present/missing teeth in each arch and practice Kennedy classification using the taught rules.",
            "Ejemplo: Arcada maxilar: patrón de ausencias compatible con Kennedy clase __; confirmar modificaciones y reglas aplicables.",
            "Example: Maxillary arch: missing-tooth pattern compatible with Kennedy class __; confirm modifications and applicable rules."
        ),
        TeachingField(
            "Auxiliares de diagnóstico", "Diagnostic aids",
            "Resume únicamente los estudios disponibles y relevantes: imagenología, biometría hemática, química sanguínea, coagulación, histopatología/biopsia o microbiología según el caso. Los rangos de laboratorio deben tomarse del reporte correspondiente.",
            "Summarize only available relevant tests: imaging, CBC, chemistry, coagulation, histopathology/biopsy or microbiology as applicable. Use the laboratory report's reference intervals.",
            "Ejemplo: Panorámica disponible: ____; biometría hemática: revisar valores y rango del laboratorio antes de interpretarlos.",
            "Example: Panoramic image available: ____; CBC: review values and that laboratory's reference interval before interpreting."
        ),
        TeachingField(
            "Pronóstico y plan inicial", "Prognosis and initial plan",
            "Al final integra problemas prioritarios, estudios o interconsultas pendientes, tratamiento inicial y seguimiento. En la app se formula como orientación educativa y requiere supervisión clínica antes de aplicarse a una persona real.",
            "Finish with priority problems, pending studies/referrals, initial treatment and follow-up. This is educational guidance requiring clinical supervision.",
            "Ejemplo: Pronóstico reservado a completar estudios de ____. Plan inicial: control de ____, completar diagnóstico y revisar alternativas terapéuticas con el docente.",
            "Example: Prognosis pending completion of __ studies. Initial plan: control __, complete diagnosis and review treatment alternatives with faculty."
        )
    )

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Nota de ingreso", "Intake note"),
                onBack,
                tr(lang,
                    "Toca cada apartado de la hoja para aprender qué va ahí, cómo se obtiene el dato y un ejemplo de redacción. Es una guía para llenar el expediente físico, no un expediente electrónico.",
                    "Tap each section to learn what belongs there, how the information is obtained and a writing example. It is a guide for the physical record, not an electronic patient record.")
            )
        }
        item {
            NoticeCard(tr(lang,
                "La estructura sigue la hoja de Nota de ingreso del material docente y la amplía con signos vitales, auxiliares, tratamiento/pronóstico como guía de aprendizaje. No introduzcas datos reales del paciente en la app.",
                "The structure follows the teaching intake-note sheet and expands it with vital signs, diagnostic aids and treatment/prognosis as a learning guide. Do not enter real patient data in the app."))
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(tr(lang, "NOTA DE INGRESO · 14 apartados", "INTAKE NOTE · 14 sections"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text(tr(lang, "Selecciona un apartado. La explicación se abre dentro de la hoja y permanece lejos de la barra de navegación del teléfono.", "Select a section. Help opens inside the page and stays clear of the phone navigation area."))
                }
            }
        }
        items(fields.size) { index ->
            val field = fields[index]
            val open = opened == index
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (open) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface),
                onClick = { opened = if (open) null else index }
            ) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("${index + 1}. ${if (lang == "en") field.titleEn else field.titleEs}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    if (open) {
                        Text("🔎 ${tr(lang, "Qué va aquí / cómo se obtiene", "What belongs here / how to obtain it")}", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                        Text(if (lang == "en") field.helpEn else field.helpEs)
                        Text("✍️ ${tr(lang, "Cómo puede redactarse", "How it may be written")}", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                        Text(if (lang == "en") field.exampleEn else field.exampleEs)
                    } else {
                        Text(tr(lang, "Toca para abrir la guía", "Tap to open the guide"))
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Regla de uso", "Use rule")) {
                Text(tr(lang,
                    "No memorices una frase fija. Obtén primero los hallazgos en los módulos interactivos (Vitales, ATM, Odontograma/ICDAS, Mucosas, IPC, IHOS, Periodonto, Pulpar/Periapical y Prótesis) y después practica cómo resumirlos aquí.",
                    "Do not memorize a fixed sentence. Obtain findings first in the interactive modules, then practice summarizing them here."))
            }
        }
    }
}
