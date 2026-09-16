package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen

internal enum class FolderExtraV33 {
    SYMPTOMS,
    CALCULATORS,
    HISTORY_HUB,
    ODONTOGRAM_HUB,
    HEAD_NECK,
    GENERAL_INSPECTION,
    PHYSICAL_HUB,
    CAMBRA,
    DENTAL_ANOMALIES,
    ERUPTION_ANOMALIES,
    HABITS,
    ORTHODONTIC_HISTORY
}

private data class FolderItemV33(
    val es: String,
    val en: String,
    val icon: String,
    val screen: AppScreen? = null,
    val extra: FolderExtraV33? = null
)

private val limeV33 = Color(0xFF8CFF28)
private val aquaV33 = Color(0xFF62C3BF)
private val historyColorsV33 = listOf(
    Color(0xFFF36F2E), Color(0xFFED941F), Color(0xFFE2B719), Color(0xFFC7D20D),
    Color(0xFF86B91A), Color(0xFF5AA718), Color(0xFF2E8B26), Color(0xFF247F25), Color(0xFF1D7229)
)

private val leftFolderV33 = listOf(
    FolderItemV33("Autorización de actividades", "Activity authorization", "✓", AppScreen.ACTIVITIES),
    FolderItemV33("Diagnóstico y tratamiento", "Diagnosis and treatment", "📝", AppScreen.TREATMENT),
    FolderItemV33("Tratamiento por sesiones", "Treatment by sessions", "🗓", AppScreen.SESSIONS),
    FolderItemV33("Ficha endodóntica", "Endodontic sheet", "⚡", AppScreen.ENDO),
    FolderItemV33("Ficha protésica", "Prosthetic sheet", "👑", AppScreen.PROSTHETIC),
    FolderItemV33("Ficha periodontal (periodontograma)", "Periodontal sheet (periodontogram)", "📈", AppScreen.PERIODONTOGRAM),
    FolderItemV33("Ficha quirúrgica", "Surgical sheet", "✚", AppScreen.SURGICAL),
    FolderItemV33("Ficha de trastornos temporomandibulares", "Temporomandibular disorders sheet", "◉", AppScreen.ATM),
    FolderItemV33("Signos y síntomas", "Signs and symptoms", "🩹", extra = FolderExtraV33.SYMPTOMS),
    FolderItemV33("Calculadora", "Calculator", "🧮", extra = FolderExtraV33.CALCULATORS),
    FolderItemV33("O’Leary", "O’Leary", "🔴", AppScreen.OLEARY),
    FolderItemV33("CAMBRA", "CAMBRA", "🛡", extra = FolderExtraV33.CAMBRA)
)

private val rightFolderV33 = listOf(
    FolderItemV33("Ficha de identificación", "Identification sheet", "👤", AppScreen.IDENTIFICATION),
    FolderItemV33("Historia clínica", "Clinical history", "🩺", extra = FolderExtraV33.HISTORY_HUB),
    FolderItemV33("Examen de mucosas", "Oral mucosa examination", "👄", AppScreen.MUCOSA),
    FolderItemV33("Auxiliares de diagnóstico", "Diagnostic aids", "🧪", AppScreen.AUXILIARIES),
    FolderItemV33("Odontograma (exámenes de diagnóstico)", "Odontogram (diagnostic exams)", "🦷", extra = FolderExtraV33.ODONTOGRAM_HUB),
    FolderItemV33("Presupuesto", "Budget", "$", AppScreen.BUDGET),
    FolderItemV33("Consentimiento informado", "Informed consent", "✍", AppScreen.CONSENT),
    FolderItemV33("Solicitud de tratamiento", "Treatment request", "📨", AppScreen.REQUEST),
    FolderItemV33("Notas de evolución", "Progress notes", "📄", AppScreen.EVOLUTION),
    FolderItemV33("Exploración de cabeza y cuello", "Head and neck examination", "👤", extra = FolderExtraV33.HEAD_NECK),
    FolderItemV33("Exploración de articulación temporomandibular", "Temporomandibular joint examination", "◉", AppScreen.ATM),
    FolderItemV33("Exploración / examen de oclusión", "Occlusion examination", "↔", AppScreen.OCCLUSION),
    FolderItemV33("Exploración de anomalías dentales", "Dental anomaly examination", "◆", extra = FolderExtraV33.DENTAL_ANOMALIES),
    FolderItemV33("Exploración de anomalías dentales de erupción", "Dental eruption anomalies", "↥", extra = FolderExtraV33.ERUPTION_ANOMALIES),
    FolderItemV33("Hábitos y parafunciones", "Habits and parafunctions", "🦷", extra = FolderExtraV33.HABITS)
)

@Composable
internal fun FolderMenuV33Screen(
    lang: String,
    onNavigate: (AppScreen) -> Unit,
    onExtra: (FolderExtraV33) -> Unit,
    onBack: () -> Unit
) {
    ResponsiveScreenV17(
        tr(lang, "Expediente abierto", "Open clinical record"),
        tr(lang,
            "Los recuadros verde limón corresponden a tratamiento/herramientas y los azul aqua a historia, exploración y registro.",
            "Lime cards are treatment/tools; aqua cards are history, examination and record sections."),
        onBack
    ) { profile ->
        val stack = profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT
        if (stack) {
            FolderColumnV33(lang, tr(lang, "Hoja izquierda · tratamiento y herramientas", "Left page · treatment and tools"), leftFolderV33, limeV33, onNavigate, onExtra)
            FolderColumnV33(lang, tr(lang, "Hoja derecha · historia y exploración", "Right page · history and examination"), rightFolderV33, aquaV33, onNavigate, onExtra)
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f)) {
                    FolderColumnV33(lang, tr(lang, "Hoja izquierda · tratamiento y herramientas", "Left page · treatment and tools"), leftFolderV33, limeV33, onNavigate, onExtra)
                }
                Column(Modifier.weight(1f)) {
                    FolderColumnV33(lang, tr(lang, "Hoja derecha · historia y exploración", "Right page · history and examination"), rightFolderV33, aquaV33, onNavigate, onExtra)
                }
            }
        }
    }
}

@Composable
private fun FolderColumnV33(
    lang: String,
    title: String,
    items: List<FolderItemV33>,
    color: Color,
    onNavigate: (AppScreen) -> Unit,
    onExtra: (FolderExtraV33) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
        items.forEach { item ->
            Card(
                onClick = { item.extra?.let(onExtra) ?: item.screen?.let(onNavigate) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = color),
                border = BorderStroke(1.5.dp, Color(0xFF17333A)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    "${item.icon} ${if (lang == "en") item.en else item.es}",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 14.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
        }
    }
}

private data class HistoryItemV33(
    val titleEs: String,
    val titleEn: String,
    val detailEs: String,
    val detailEn: String,
    val screen: AppScreen? = null,
    val extra: FolderExtraV33? = null
)

private val historyItemsV33 = listOf(
    HistoryItemV33("I. Identificación del paciente", "I. Patient identification", "Nombre · edad · sexo/género · religión · ocupación y datos de identificación.", "Name · age · sex/gender · religion · occupation and identification data.", AppScreen.IDENTIFICATION),
    HistoryItemV33("II. Motivo de consulta y padecimiento actual", "II. Chief complaint and present illness", "Motivo principal, inicio, evolución y características del problema actual.", "Main complaint, onset, evolution and characteristics of the current problem.", AppScreen.HISTORY),
    HistoryItemV33("IV. Antecedentes heredo-familiares", "IV. Family history", "Antecedentes relevantes en familiares y posibles riesgos compartidos.", "Relevant family history and potentially shared risks.", AppScreen.HISTORY),
    HistoryItemV33("V. Antecedentes personales no patológicos", "V. Non-pathological personal history", "Hogar · hábitos · higiene bucal · higiene general · alcohol · tabaco · tatuajes y otras exposiciones.", "Home · habits · oral hygiene · general hygiene · alcohol · tobacco · tattoos and other exposures.", AppScreen.HISTORY),
    HistoryItemV33("VII. Antecedentes personales patológicos", "VII. Pathological personal history", "Enfermedades, alergias, medicamentos, hospitalizaciones y clasificación ASA cuando corresponda.", "Diseases, allergies, medicines, hospitalizations and ASA classification when applicable.", AppScreen.HISTORY),
    HistoryItemV33("VIII. Antecedentes quirúrgicos y traumáticos", "VIII. Surgical and trauma history", "Cirugías, traumatismos, fracturas, transfusiones y eventos relevantes.", "Surgery, trauma, fractures, transfusions and relevant events.", AppScreen.HISTORY),
    HistoryItemV33("IX. Exploración física", "IX. Physical examination", "Signos vitales · signos y síntomas · exploración general · marcha · actitud · constitución · movimientos anormales.", "Vital signs · signs and symptoms · general inspection · gait · attitude · habitus · abnormal movements.", extra = FolderExtraV33.PHYSICAL_HUB),
    HistoryItemV33("Antecedentes de tratamientos ortodónticos", "Orthodontic treatment history", "Si tuvo tratamiento, tipo de aparato, duración y motivo de indicación.", "Previous treatment, appliance type, duration and indication.", extra = FolderExtraV33.ORTHODONTIC_HISTORY)
)

@Composable
internal fun HistoryMenuV33Screen(lang: String, onNavigate: (AppScreen) -> Unit, onExtra: (FolderExtraV33) -> Unit, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "Historia clínica", "Clinical history"),
        tr(lang, "Selecciona el apartado que quieres revisar o llenar.", "Choose the section you want to review or complete."),
        onBack
    ) { profile ->
        AdaptiveGridV17(historyItemsV33.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val item = historyItemsV33[index]
            Card(
                onClick = { item.extra?.let(onExtra) ?: item.screen?.let(onNavigate) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = historyColorsV33[index % historyColorsV33.size]),
                border = BorderStroke(1.dp, Color(0xFF324229)),
                shape = RoundedCornerShape(3.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (lang == "en") item.titleEn else item.titleEs, fontWeight = FontWeight.Black, color = Color.White)
                    Text(if (lang == "en") item.detailEn else item.detailEs, color = Color.White, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
internal fun PhysicalExamMenuV33Screen(lang: String, onNavigate: (AppScreen) -> Unit, onExtra: (FolderExtraV33) -> Unit, onBack: () -> Unit) {
    ResponsiveScreenV17(tr(lang, "IX. Exploración física", "IX. Physical examination"), tr(lang, "Abre cada componente de la exploración física.", "Open each physical-examination component."), onBack) { profile ->
        val items = listOf(
            FolderItemV33("Signos vitales", "Vital signs", "❤️", AppScreen.VITALS),
            FolderItemV33("Signos y síntomas", "Signs and symptoms", "🩹", extra = FolderExtraV33.SYMPTOMS),
            FolderItemV33("Exploración general", "General inspection", "👁", extra = FolderExtraV33.GENERAL_INSPECTION),
            FolderItemV33("Exploración de cabeza y cuello", "Head and neck examination", "👤", extra = FolderExtraV33.HEAD_NECK)
        )
        AdaptiveGridV17(items.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val item = items[index]
            Card(onClick = { item.extra?.let(onExtra) ?: item.screen?.let(onNavigate) }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = historyColorsV33[6]), border = BorderStroke(1.dp, Color(0xFF17333A))) {
                Text("${item.icon} ${if (lang == "en") item.en else item.es}", Modifier.fillMaxWidth().padding(14.dp), textAlign = TextAlign.Center, color = Color.White, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
internal fun OdontogramHubV33Screen(lang: String, onNavigate: (AppScreen) -> Unit, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "Odontograma · exámenes de diagnóstico", "Odontogram · diagnostic examinations"),
        tr(lang, "El odontograma es el punto de entrada a los índices y sistemas de registro dental solicitados.", "The odontogram is the entry point to the requested dental indices and recording systems."),
        onBack
    ) { profile ->
        val items = listOf(
            FolderItemV33("Odontograma clínico", "Clinical odontogram", "🦷", AppScreen.ODONTOGRAM),
            FolderItemV33("CPOD · dentición permanente", "DMFT · permanent dentition", "➕", AppScreen.CPOD),
            FolderItemV33("ceod · dentición temporal", "dmft · primary dentition", "➕", AppScreen.CPOD),
            FolderItemV33("ICDAS", "ICDAS", "🔎", AppScreen.ICDAS),
            FolderItemV33("IHOS", "OHI-S", "🪥", AppScreen.IHOS),
            FolderItemV33("IPC", "CPI", "6️⃣", AppScreen.IPC)
        )
        AdaptiveGridV17(items.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val item = items[index]
            Card(onClick = { item.screen?.let(onNavigate) }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = aquaV33), border = BorderStroke(1.5.dp, Color(0xFF17333A)), shape = RoundedCornerShape(4.dp)) {
                Text("${item.icon} ${if (lang == "en") item.en else item.es}", Modifier.fillMaxWidth().padding(14.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Black, color = Color.Black)
            }
        }
        NoticeCard(tr(lang,
            "CPOD/ceod resumen experiencia de caries por dentición; ICDAS registra severidad por superficie. IHOS e IPC son índices distintos y se mantienen como módulos independientes.",
            "DMFT/dmft summarize caries experience by dentition; ICDAS records lesion severity by surface. OHI-S and CPI are separate indices and remain independent modules."))
    }
}

@Composable
internal fun CambraV33Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        "CAMBRA",
        tr(lang, "Caries Management by Risk Assessment · evaluación educativa del riesgo de caries.", "Caries Management by Risk Assessment · educational caries-risk assessment."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang, "Estructura de la valoración", "Assessment structure")) {
            Text(tr(lang, "• Indicadores de enfermedad: lesiones/cavitación reciente, restauraciones recientes y otros datos de actividad.", "• Disease indicators: recent lesions/cavitation, recent restorations and other activity findings."))
            Text(tr(lang, "• Factores de riesgo: exposición frecuente a carbohidratos fermentables, flujo salival reducido, antecedentes relevantes y otros factores biológicos/contextuales.", "• Risk factors: frequent fermentable-carbohydrate exposure, reduced salivary flow, relevant history and other biologic/contextual factors."))
            Text(tr(lang, "• Factores protectores: exposición adecuada a fluoruro, higiene y medidas preventivas individualizadas.", "• Protective factors: appropriate fluoride exposure, hygiene and individualized preventive measures."))
        }
        AdaptiveGridV17(2, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            ResponsiveSectionV17(if (index == 0) tr(lang, "0–6 años", "0–6 years") else tr(lang, ">6 años", ">6 years")) {
                Text(tr(lang, "Usa la herramienta correspondiente a la edad; los factores y su peso clínico no son idénticos entre niños pequeños y pacientes mayores.", "Use the age-appropriate tool; factors and clinical weighting are not identical in young children and older patients."))
            }
        }
        NoticeCard(tr(lang, "CAMBRA orienta prevención y manejo según riesgo; no sustituye diagnóstico de caries ni la valoración clínica completa.", "CAMBRA guides prevention and management by risk; it does not replace caries diagnosis or a complete clinical assessment."))
    }
}

@Composable
internal fun DentalAnomaliesV33Screen(lang: String, onBack: () -> Unit) {
    SimpleEducationalSheet(
        lang,
        "Alteraciones de forma, número y estructura de los órganos dentarios",
        "Tooth number, size, shape and structure anomalies",
        "Registra si existe la alteración y describe qué dientes están afectados.",
        "Record whether an anomaly is present and which teeth are affected.",
        listOf("Número: aumento o disminución", "Tamaño: macrodoncia / microdoncia", "Forma: localizada o generalizada", "Estructura: esmalte / dentina / cemento", "Color: intrínseco / extrínseco"),
        listOf("Number: increased or decreased", "Size: macrodontia / microdontia", "Shape: localized or generalized", "Structure: enamel / dentin / cementum", "Color: intrinsic / extrinsic"),
        onBack
    )
}

@Composable
internal fun EruptionAnomaliesV33Screen(lang: String, onBack: () -> Unit) {
    SimpleEducationalSheet(
        lang,
        "Alteraciones de erupción y posición",
        "Eruption and position alterations",
        "Describe el tipo de alteración, el diente y la localización.",
        "Describe the alteration, tooth and location.",
        listOf("Erupción ectópica", "Transposición", "Erupción tardía", "Retención", "Impactación / inclusión", "Erupción precoz", "Diente natal / neonatal"),
        listOf("Ectopic eruption", "Transposition", "Delayed eruption", "Retention", "Impaction / inclusion", "Precocious eruption", "Natal / neonatal tooth"),
        onBack
    )
}

@Composable
internal fun HabitsParafunctionsV33Screen(lang: String, onBack: () -> Unit) {
    SimpleEducationalSheet(
        lang,
        "Hábitos y parafunciones",
        "Habits and parafunctions",
        "Registra presencia, desde cuándo, frecuencia/contexto y manifestaciones clínicas.",
        "Record presence, onset, frequency/context and clinical manifestations.",
        listOf("Succión digital", "Interposición / proyección lingual", "Succión labial", "Respiración bucal", "Onicofagia", "Uso de chupón", "Bruxismo / rechinamiento", "Manifestaciones faciales u odontológicas"),
        listOf("Thumb/finger sucking", "Tongue thrust/interposition", "Lip sucking", "Mouth breathing", "Nail biting", "Pacifier use", "Bruxism / grinding", "Facial or dental manifestations"),
        onBack
    )
}

@Composable
internal fun OrthodonticHistoryV33Screen(lang: String, onBack: () -> Unit) {
    SimpleEducationalSheet(
        lang,
        "Antecedentes de tratamientos ortodónticos",
        "Orthodontic treatment history",
        "Registra tratamiento previo sin asumir que un aparato explica por sí solo el estado actual.",
        "Record prior treatment without assuming an appliance alone explains the current condition.",
        listOf("¿Tuvo tratamiento ortodóntico?", "Tipo de aparato", "Tiempo de uso", "Motivo de indicación", "Edad aproximada al iniciar", "Retención posterior / abandono / seguimiento"),
        listOf("Previous orthodontic treatment?", "Appliance type", "Duration of use", "Reason for prescription", "Approximate age at start", "Retention / discontinuation / follow-up"),
        onBack
    )
}
