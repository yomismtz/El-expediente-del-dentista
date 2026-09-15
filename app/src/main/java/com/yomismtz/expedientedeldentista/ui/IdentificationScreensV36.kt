package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

private data class IdentificationItemV36(
    val es: String,
    val en: String,
    val helpEs: String,
    val helpEn: String
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

private val identificationSheetFieldsV36 = listOf(
    "Nombre" to "Name",
    "Género" to "Gender",
    "Edad" to "Age",
    "Fecha de nacimiento" to "Date of birth",
    "Lugar de nacimiento" to "Place of birth",
    "Dirección" to "Address",
    "Teléfono" to "Telephone",
    "Ocupación anterior" to "Previous occupation",
    "Ocupación actual" to "Current occupation",
    "Religión" to "Religion",
    "Número de miembros en la familia" to "Number of family members",
    "Estado civil" to "Marital status",
    "Escolaridad" to "Education",
    "Servicio de salud: privado / institucional / especifique" to "Health service: private / institutional / specify"
)

/**
 * Top-level physical identification sheet.
 * It intentionally looks like a blank form and does not collect patient data.
 */
@Composable
fun IdentificationSheetV36Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "Ficha de identificación", "Identification sheet"),
        tr(
            lang,
            "Vista didáctica del formato físico. Esta ficha es distinta del apartado “Identificación del paciente” dentro de Historia clínica.",
            "Teaching view of the physical form. This sheet is different from the “Patient identification” section inside the clinical history."
        ),
        onBack
    ) { profile ->
        NoticeCard(
            tr(
                lang,
                "No escribas datos identificables de pacientes reales. La ficha se muestra para aprender su estructura y dónde corresponde cada dato.",
                "Do not enter identifiable data from real patients. The sheet is shown only to teach its structure and where each item belongs."
            )
        )
        ResponsiveSectionV17(tr(lang, "Formato", "Form")) {
            val columns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
            AdaptiveGridV17(identificationSheetFieldsV36.size, columns) { index ->
                val field = identificationSheetFieldsV36[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .45f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(if (lang == "en") field.second else field.first, fontWeight = FontWeight.Bold)
                        Text("____________________________", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        NoticeCard(
            tr(
                lang,
                "Después, en Historia clínica → Identificación del paciente, se explica la utilidad de cada uno de estos datos. Motivo de consulta y padecimiento actual son apartados distintos y no forman parte de esta identificación.",
                "Then, under Clinical history → Patient identification, the purpose of each item is explained. Chief complaint and present illness are separate sections and are not part of this identification."
            )
        )
    }
}

/**
 * Clinical-history subsection: Patient identification.
 * This is explanatory, not the top-level physical identification sheet.
 */
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
