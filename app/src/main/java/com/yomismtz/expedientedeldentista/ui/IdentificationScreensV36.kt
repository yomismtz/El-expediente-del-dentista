package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class IdentificationItemV36(
    val es: String,
    val en: String,
    val helpEs: String,
    val helpEn: String
)

private data class SheetDiagnosisV37(
    val es: String,
    val en: String,
    val optionsEs: List<String>,
    val optionsEn: List<String>
)

private val patientIdentificationItemsV36 = listOf(
    IdentificationItemV36(
        "Nombre",
        "Name",
        "Identificación legal y clínica del paciente. Regístralo como aparece en el documento o como lo refiere la persona según el formato institucional.",
        "Legal and clinical patient identification. Record it as shown on the document or as reported, according to the institutional form."
    ),
    IdentificationItemV36(
        "Sexo / género",
        "Sex / gender",
        "Registra el dato solicitado por el formato de manera respetuosa. Puede ser relevante para antecedentes biológicos, hormonales y para una comunicación clínica adecuada.",
        "Record the information requested by the form respectfully. It may be relevant to biological/hormonal history and appropriate clinical communication."
    ),
    IdentificationItemV36(
        "Edad",
        "Age",
        "Ayuda a contextualizar dentición, crecimiento, desarrollo y enfermedades cuya frecuencia cambia con la edad.",
        "Helps contextualize dentition, growth, development and conditions whose frequency changes with age."
    ),
    IdentificationItemV36(
        "Fecha de nacimiento",
        "Date of birth",
        "Permite corroborar la edad y ubicar el momento de crecimiento y desarrollo.",
        "Helps confirm age and place the patient within growth and developmental stages."
    ),
    IdentificationItemV36(
        "Lugar de nacimiento",
        "Place of birth",
        "Dato demográfico solicitado por el formato. Se registra tal como lo refiere la persona, sin inferir por sí solo un diagnóstico o riesgo.",
        "Demographic information requested by the form. Record it as reported without inferring a diagnosis or risk from it alone."
    ),
    IdentificationItemV36(
        "Dirección / domicilio",
        "Address",
        "Dato de localización y contacto; también puede ser útil para el contexto epidemiológico. En esta guía no se capturan datos reales.",
        "Location/contact information that may also support epidemiologic context. This guide does not collect real personal data."
    ),
    IdentificationItemV36(
        "Teléfono",
        "Telephone",
        "Se utiliza para seguimiento y contacto ante cambios de cita o situaciones clínicas relevantes. La app no solicita un número real.",
        "Used for follow-up and contact regarding appointments or clinically relevant situations. The app does not request a real number."
    ),
    IdentificationItemV36(
        "Ocupación anterior y actual",
        "Previous and current occupation",
        "Puede orientar sobre estilo de vida y exposiciones laborales; por ejemplo, estrés, sustancias, horarios o hábitos relacionados con salud.",
        "May provide context about lifestyle and occupational exposures such as stress, substances, schedules or health-related habits."
    ),
    IdentificationItemV36(
        "Religión",
        "Religion",
        "Se registra sólo cuando el formato lo solicita y con respeto. Algunas creencias pueden influir en decisiones sobre procedimientos, medicamentos o productos de origen biológico.",
        "Record only when requested by the form and with respect. Some beliefs may affect decisions about procedures, medicines or biologic products."
    ),
    IdentificationItemV36(
        "Número de miembros en la familia",
        "Number of family members",
        "Dato social solicitado por el formato. Ayuda a describir el contexto familiar sin atribuirle por sí solo una condición clínica.",
        "Social information requested by the form. It describes family context without assigning a clinical condition by itself."
    ),
    IdentificationItemV36(
        "Estado civil",
        "Marital status",
        "Forma parte del contexto social y puede relacionarse con redes de apoyo, estrés y hábitos; no determina por sí solo el estado de salud.",
        "Part of the social context and may relate to support networks, stress and habits; it does not determine health status by itself."
    ),
    IdentificationItemV36(
        "Escolaridad",
        "Education",
        "Orienta la forma de comunicar indicaciones y verificar comprensión, sin asumir capacidad o adherencia únicamente por el nivel escolar.",
        "Helps adapt communication and verify understanding without assuming ability or adherence from education level alone."
    ),
    IdentificationItemV36(
        "Servicio de salud",
        "Health service",
        "El formato distingue atención privada o institucional y permite especificar la institución. Es útil para continuidad y coordinación de la atención.",
        "The form distinguishes private or institutional care and allows the institution to be specified. It helps with continuity and coordination of care."
    )
)

private val asaOptionsV37 = listOf(
    "ASA I · sano",
    "ASA II · enfermedad sistémica leve",
    "ASA III · enfermedad sistémica grave",
    "ASA IV · enfermedad grave con amenaza constante para la vida",
    "ASA V · paciente moribundo",
    "ASA VI · donador de órganos con muerte encefálica",
    "ASA E · emergencia, si aplica"
)

private val bloodGroupsV37 = listOf("A+", "A−", "B+", "B−", "AB+", "AB−", "O+", "O−", "Desconocido")

private val medicationAllergiesV37 = listOf(
    "Penicilina / amoxicilina", "Cefalosporinas", "Sulfonamidas", "AINE / aspirina",
    "Macrólidos", "Clindamicina", "Anestésico local o conservador referido", "Otro medicamento"
)

private val foodAllergiesV37 = listOf(
    "Cacahuate / nueces", "Mariscos", "Pescado", "Leche", "Huevo", "Soya", "Trigo", "Otro alimento"
)

private val dentalMaterialAllergiesV37 = listOf(
    "Látex", "Acrílico / metacrilatos", "Níquel", "Cobalto / cromo", "Resinas / adhesivos",
    "Eugenol", "Clorhexidina", "Material de impresión", "Guantes / barreras", "Otro material odontológico"
)

private val reactionOptionsV37 = listOf(
    "Urticaria", "Angioedema", "Disnea / broncoespasmo", "Anafilaxia", "Dermatitis / reacción de contacto",
    "Síntoma gastrointestinal", "Efecto adverso no alérgico", "Reacción desconocida"
)

private val diagnosisGroupsV37 = listOf(
    SheetDiagnosisV37(
        "Diagnóstico sistémico / cardiológico",
        "Systemic / cardiovascular diagnosis",
        listOf("Sin enfermedad sistémica referida", "Hipertensión", "Cardiopatía / arritmia", "Diabetes / endocrino", "Respiratorio", "Hematológico", "Renal", "Otro · describir"),
        listOf("No reported systemic disease", "Hypertension", "Cardiac disease / arrhythmia", "Diabetes / endocrine", "Respiratory", "Hematologic", "Renal", "Other · describe")
    ),
    SheetDiagnosisV37(
        "Diagnóstico de caries y anomalías",
        "Caries and anomaly diagnosis",
        listOf("Sin lesión de caries detectada", "Lesión inicial", "Lesión moderada", "Lesión severa/cavitada", "Anomalía dental presente", "Pendiente de completar odontograma / ICDAS"),
        listOf("No caries lesion detected", "Initial lesion", "Moderate lesion", "Severe/cavitated lesion", "Dental anomaly present", "Odontogram / ICDAS incomplete")
    ),
    SheetDiagnosisV37(
        "Diagnóstico de oclusión",
        "Occlusal diagnosis",
        listOf("Relación oclusal dentro de referencia", "Clase I", "Clase II", "Clase III", "Mordida abierta", "Mordida profunda", "Mordida cruzada", "No valorable / describir"),
        listOf("Occlusion within reference", "Class I", "Class II", "Class III", "Open bite", "Deep bite", "Crossbite", "Not assessable / describe")
    ),
    SheetDiagnosisV37(
        "Diagnóstico periodontal",
        "Periodontal diagnosis",
        listOf("Salud periodontal", "Gingivitis", "Periodontitis · estadio/grado por determinar", "Periodontitis · estadio/grado documentado", "Absceso periodontal", "Lesión endoperiodontal", "Otro"),
        listOf("Periodontal health", "Gingivitis", "Periodontitis · stage/grade pending", "Periodontitis · documented stage/grade", "Periodontal abscess", "Endo-periodontal lesion", "Other")
    ),
    SheetDiagnosisV37(
        "Diagnóstico endodóntico · pulpar y periapical",
        "Endodontic diagnosis · pulpal and apical",
        listOf("Pulpa normal", "Pulpitis reversible", "Pulpitis irreversible", "Necrosis pulpar", "Previamente tratado/iniciado", "Tejidos apicales normales", "Periodontitis apical", "Absceso apical", "Otro / pendiente de pruebas"),
        listOf("Normal pulp", "Reversible pulpitis", "Irreversible pulpitis", "Pulp necrosis", "Previously treated/initiated", "Normal apical tissues", "Apical periodontitis", "Apical abscess", "Other / tests pending")
    ),
    SheetDiagnosisV37(
        "ATM y músculos",
        "TMJ and muscles",
        listOf("Sin alteraciones aparentes", "Dolor miofascial", "Artralgia", "Chasquido", "Crepitación", "Limitación / bloqueo", "Hiperlaxitud / luxación", "Bruxismo / sobrecarga", "Otro"),
        listOf("No apparent alteration", "Myofascial pain", "Arthralgia", "Click", "Crepitus", "Limitation / locking", "Hypermobility / dislocation", "Bruxism / overload", "Other")
    ),
    SheetDiagnosisV37(
        "Diagnóstico de mucosas / patología oral",
        "Oral mucosa / oral pathology diagnosis",
        listOf("Sin lesión aparente", "Lesión traumática", "Úlcera", "Lesión blanca", "Lesión roja", "Aumento de volumen", "Lesión pigmentada", "Infección probable", "Requiere diagnóstico diferencial / biopsia"),
        listOf("No apparent lesion", "Traumatic lesion", "Ulcer", "White lesion", "Red lesion", "Swelling", "Pigmented lesion", "Probable infection", "Differential diagnosis / biopsy required")
    ),
    SheetDiagnosisV37(
        "Índice CPOD / ceod",
        "DMFT / dmft index",
        listOf("CPOD registrado", "ceod registrado", "Dentición mixta · registrar ambos", "Pendiente de completar"),
        listOf("DMFT recorded", "dmft recorded", "Mixed dentition · record both", "Incomplete")
    ),
    SheetDiagnosisV37(
        "Diagnóstico protésico",
        "Prosthetic diagnosis",
        listOf("Dentición completa / sin indicación protésica", "Kennedy I", "Kennedy II", "Kennedy III", "Kennedy IV", "Edéntulo total", "Prótesis existente · valorar estado", "Otro"),
        listOf("Complete dentition / no prosthetic indication", "Kennedy I", "Kennedy II", "Kennedy III", "Kennedy IV", "Completely edentulous", "Existing prosthesis · assess condition", "Other")
    ),
    SheetDiagnosisV37(
        "Diagnóstico quirúrgico",
        "Surgical diagnosis",
        listOf("Sin indicación quirúrgica actual", "Extracción indicada", "Tercer molar / retención", "Diente incluido / impactado", "Lesión que requiere biopsia", "Infección odontógena con valoración quirúrgica", "Otro"),
        listOf("No current surgical indication", "Extraction indicated", "Third molar / retention", "Included / impacted tooth", "Lesion requiring biopsy", "Odontogenic infection requiring surgical assessment", "Other")
    )
)

@Composable
fun IdentificationSheetV36Screen(lang: String, onBack: () -> Unit) {
    var patientName by remember { mutableStateOf("") }
    var recordNumber by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var ldc by remember { mutableStateOf("") }
    var shift by remember { mutableStateOf("") }
    var selectedAsa by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("") }
    var currentMedicines by remember { mutableStateOf("") }
    var allergyDetail by remember { mutableStateOf("") }
    val selectedMedicationAllergies = remember { mutableStateListOf<String>() }
    val selectedFoodAllergies = remember { mutableStateListOf<String>() }
    val selectedMaterialAllergies = remember { mutableStateListOf<String>() }
    val selectedReactions = remember { mutableStateListOf<String>() }
    val selectedDiagnosisOptions = remember { mutableStateListOf<String>() }
    val diagnosisNotes = remember { MutableList(diagnosisGroupsV37.size) { mutableStateOf("") } }

    ResponsiveScreenV17(
        tr(lang, "Ficha de identificación", "Identification sheet"),
        tr(
            lang,
            "Ficha-resumen del expediente odontológico. No es lo mismo que “Identificación del paciente” dentro de Historia clínica.",
            "Dental-record summary sheet. It is not the same as “Patient identification” inside the clinical history."
        ),
        onBack
    ) { profile ->
        NoticeCard(
            tr(
                lang,
                "Esta vista es didáctica. Usa datos ficticios para practicar. La ficha integra datos de control, ASA, grupo sanguíneo, alergias, medicamentos y el resumen diagnóstico obtenido de los demás apartados del expediente.",
                "This is a teaching view. Use fictional data for practice. The sheet integrates control data, ASA, blood group, allergies, medicines and the diagnostic summary obtained from the other record sections."
            )
        )

        ResponsiveSectionV17(tr(lang, "Datos de la ficha", "Sheet data")) {
            OutlinedTextField(patientName, { patientName = it }, label = { Text(tr(lang, "Nombre", "Name")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(recordNumber, { recordNumber = it }, label = { Text(tr(lang, "No. de expediente", "Record number")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(date, { date = it }, label = { Text(tr(lang, "Fecha", "Date")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(ldc, { ldc = it }, label = { Text("LDC") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(shift, { shift = it }, label = { Text(tr(lang, "Turno", "Shift")) }, modifier = Modifier.fillMaxWidth())
        }

        ResponsiveSectionV17(tr(lang, "Clasificación ASA", "ASA classification")) {
            Text(tr(lang,
                "Selecciona sólo después de integrar antecedentes, estado actual y valoración clínica. La letra E se añade cuando el procedimiento es de emergencia.",
                "Select only after integrating medical history, current status and clinical assessment. The E modifier is added for emergency procedures."
            ))
            AdaptiveGridV17(asaOptionsV37.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val option = asaOptionsV37[index]
                FilterChip(selected = selectedAsa == option, onClick = { selectedAsa = option }, label = { Text(option) }, modifier = Modifier.fillMaxWidth())
            }
        }

        ResponsiveSectionV17(tr(lang, "Tipo de sangre", "Blood group")) {
            AdaptiveGridV17(bloodGroupsV37.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 3) { index ->
                val option = bloodGroupsV37[index]
                FilterChip(selected = selectedBloodGroup == option, onClick = { selectedBloodGroup = option }, label = { Text(option) }, modifier = Modifier.fillMaxWidth())
            }
            NoticeCard(tr(lang,
                "Si el paciente no conoce su grupo sanguíneo o no existe documento, selecciona “Desconocido”; no lo infieras.",
                "If the patient does not know the blood group or there is no documentation, select “Unknown”; do not infer it."
            ))
        }

        ResponsiveSectionV17(tr(lang, "Alergias", "Allergies")) {
            Text(tr(lang, "Medicamentos", "Medicines"), fontWeight = FontWeight.Black)
            AllergyChipGridV37(profile, medicationAllergiesV37, selectedMedicationAllergies)
            Text(tr(lang, "Alimentos", "Foods"), fontWeight = FontWeight.Black)
            AllergyChipGridV37(profile, foodAllergiesV37, selectedFoodAllergies)
            Text(tr(lang, "Materiales y materiales relacionados con odontología", "Materials and dental-related materials"), fontWeight = FontWeight.Black)
            AllergyChipGridV37(profile, dentalMaterialAllergiesV37, selectedMaterialAllergies)
            Text(tr(lang, "Tipo de reacción referida", "Reported reaction type"), fontWeight = FontWeight.Black)
            AllergyChipGridV37(profile, reactionOptionsV37, selectedReactions)
            OutlinedTextField(
                allergyDetail,
                { allergyDetail = it },
                label = { Text(tr(lang, "Detalle: sustancia, reacción, cuándo ocurrió y atención requerida", "Detail: substance, reaction, when it occurred and treatment required")) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            NoticeCard(tr(lang,
                "Distingue alergia de efecto adverso o intolerancia. Una reacción previa debe describirse; no etiquetes como alergia sólo porque el paciente tuvo náusea, dolor u otro efecto secundario.",
                "Distinguish allergy from adverse effect or intolerance. Describe the previous reaction; do not label an allergy solely because nausea, pain or another side effect occurred."
            ))
        }

        ResponsiveSectionV17(tr(lang, "Medicamentos actuales", "Current medicines")) {
            OutlinedTextField(
                currentMedicines,
                { currentMedicines = it },
                label = { Text(tr(lang, "Nombre · presentación/dosis referida · vía · intervalo · motivo", "Name · reported strength/dose · route · interval · reason")) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }

        ResponsiveSectionV17(tr(lang, "Resumen diagnóstico", "Diagnostic summary")) {
            Text(tr(lang,
                "Selecciona opciones sólo cuando estén sustentadas por la historia y el examen correspondiente. Usa el campo libre para órgano dentario, superficie, lateralidad, estadio/grado, clasificación u otros detalles.",
                "Select options only when supported by the corresponding history and examination. Use the free-text field for tooth, surface, laterality, stage/grade, classification or other details."
            ))
            diagnosisGroupsV37.forEachIndexed { groupIndex, group ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .35f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (lang == "en") group.en else group.es, fontWeight = FontWeight.Black)
                        val options = if (lang == "en") group.optionsEn else group.optionsEs
                        AdaptiveGridV17(options.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { optionIndex ->
                            val option = options[optionIndex]
                            val key = "$groupIndex::$option"
                            FilterChip(
                                selected = key in selectedDiagnosisOptions,
                                onClick = {
                                    val previous = selectedDiagnosisOptions.filter { it.startsWith("$groupIndex::") }
                                    selectedDiagnosisOptions.removeAll(previous)
                                    selectedDiagnosisOptions.add(key)
                                },
                                label = { Text(option) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        OutlinedTextField(
                            diagnosisNotes[groupIndex].value,
                            { diagnosisNotes[groupIndex].value = it },
                            label = { Text(tr(lang, "Detalle / redacción final", "Detail / final wording")) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }
                }
            }
        }

        NoticeCard(
            tr(
                lang,
                "Identificación del paciente (nombre, edad, nacimiento, domicilio, ocupación, escolaridad, etc.) permanece dentro de Historia clínica. Motivo de consulta y padecimiento actual son apartados posteriores e independientes.",
                "Patient identification (name, age, birth, address, occupation, education, etc.) remains inside the clinical history. Chief complaint and present illness are later, separate sections."
            )
        )
    }
}

@Composable
private fun AllergyChipGridV37(
    profile: ScreenProfileV17,
    items: List<String>,
    selected: MutableList<String>
) {
    AdaptiveGridV17(items.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
        val item = items[index]
        FilterChip(
            selected = item in selected,
            onClick = {
                if (item in selected) selected.remove(item) else selected.add(item)
            },
            label = { Text(item) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PatientIdentificationV36Screen(lang: String, onBack: () -> Unit) {
    var opened by remember { mutableStateOf<Int?>(0) }

    ResponsiveScreenV17(
        tr(lang, "Identificación del paciente", "Patient identification"),
        tr(
            lang,
            "Apartado de Historia clínica. Toca cada dato para revisar para qué se solicita y cómo se interpreta dentro del contexto clínico.",
            "Clinical-history section. Tap each item to review why it is requested and how it is used in clinical context."
        ),
        onBack
    ) { _ ->
        NoticeCard(
            tr(
                lang,
                "Este apartado termina en Servicio de salud. “Motivo de consulta” y “Padecimiento actual” van después como apartados independientes de la Historia clínica.",
                "This section ends with Health service. “Chief complaint” and “Present illness” follow as separate sections of the clinical history."
            )
        )
        patientIdentificationItemsV36.forEachIndexed { index, item ->
            val isOpen = opened == index
            Card(
                onClick = { opened = if (isOpen) null else index },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOpen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .35f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(if (lang == "en") item.en else item.es, fontWeight = FontWeight.Black)
                    if (isOpen) {
                        Text("💡 ${if (lang == "en") item.helpEn else item.helpEs}")
                    } else {
                        Text(
                            tr(lang, "Toca para ver su utilidad", "Tap to see its purpose"),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
