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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen

private data class FolderItemV35(
    val es: String,
    val en: String,
    val icon: String,
    val screen: AppScreen? = null,
    val extra: FolderExtraV33? = null
)

private data class HistoryItemV35(
    val es: String,
    val en: String,
    val detailEs: String,
    val detailEn: String,
    val screen: AppScreen? = null,
    val extra: FolderExtraV33? = null
)

private data class InteractiveCategoryV35(
    val es: String,
    val en: String,
    val introEs: String,
    val introEn: String,
    val findings: List<Pair<String, String>>
)

private val leftFolderV35 = listOf(
    FolderItemV35("Autorización de actividades", "Activity authorization", "✓", AppScreen.ACTIVITIES),
    FolderItemV35("Diagnóstico y tratamiento", "Diagnosis and treatment", "📝", AppScreen.TREATMENT),
    FolderItemV35("Tratamiento por sesiones", "Treatment by sessions", "🗓", AppScreen.SESSIONS),
    FolderItemV35("Ficha endodóntica", "Endodontic sheet", "⚡", AppScreen.ENDO),
    FolderItemV35("Ficha protésica", "Prosthetic sheet", "👑", AppScreen.PROSTHETIC),
    FolderItemV35("Ficha periodontal (periodontograma)", "Periodontal sheet (periodontogram)", "📈", AppScreen.PERIODONTOGRAM),
    FolderItemV35("Ficha quirúrgica", "Surgical sheet", "✚", AppScreen.SURGICAL),
    FolderItemV35("Ficha de trastornos temporomandibulares", "Temporomandibular disorders sheet", "◉", AppScreen.ATM),
    FolderItemV35("Signos y síntomas", "Signs and symptoms", "🩹", extra = FolderExtraV33.SYMPTOMS),
    FolderItemV35("Calculadora", "Calculator", "🧮", extra = FolderExtraV33.CALCULATORS),
    FolderItemV35("O’Leary", "O’Leary", "🔴", AppScreen.OLEARY),
    FolderItemV35("CAMBRA", "CAMBRA", "🛡", extra = FolderExtraV33.CAMBRA)
)

private val rightFolderV35 = listOf(
    FolderItemV35("Ficha de identificación", "Identification sheet", "👤", AppScreen.IDENTIFICATION),
    FolderItemV35("Historia clínica", "Clinical history", "🩺", extra = FolderExtraV33.HISTORY_HUB),
    FolderItemV35("Examen de mucosas", "Oral mucosa examination", "👄", AppScreen.MUCOSA),
    FolderItemV35("Auxiliares de diagnóstico", "Diagnostic aids", "🧪", AppScreen.AUXILIARIES),
    FolderItemV35("Odontograma (exámenes de diagnóstico)", "Odontogram (diagnostic examinations)", "🦷", extra = FolderExtraV33.ODONTOGRAM_HUB),
    FolderItemV35("Presupuesto", "Budget", "$", AppScreen.BUDGET),
    FolderItemV35("Consentimiento informado", "Informed consent", "✍", AppScreen.CONSENT),
    FolderItemV35("Solicitud de tratamiento", "Treatment request", "📨", AppScreen.REQUEST),
    FolderItemV35("Notas de evolución", "Progress notes", "📄", AppScreen.EVOLUTION),
    FolderItemV35("Exploración de cabeza y cuello", "Head and neck examination", "👤", extra = FolderExtraV33.HEAD_NECK),
    FolderItemV35("Exploración de articulación temporomandibular", "Temporomandibular joint examination", "◉", AppScreen.ATM),
    FolderItemV35("Exploración / examen de oclusión", "Occlusion examination", "↔", AppScreen.OCCLUSION),
    FolderItemV35("Exploración de anomalías dentales", "Dental anomaly examination", "◆", extra = FolderExtraV33.DENTAL_ANOMALIES),
    FolderItemV35("Alteraciones de erupción y posición", "Eruption and position alterations", "↥", extra = FolderExtraV33.ERUPTION_ANOMALIES),
    FolderItemV35("Hábitos y parafunciones", "Habits and parafunctions", "🦷", extra = FolderExtraV33.HABITS)
)

private val historyItemsV35 = listOf(
    HistoryItemV35("Identificación del paciente", "Patient identification", "Nombre, edad, sexo/género, fecha y lugar de nacimiento, domicilio, teléfono, ocupación, religión, familia, estado civil, escolaridad y servicio de salud.", "Name, age, sex/gender, date and place of birth, address, phone, occupation, religion, family, marital status, education and health service.", AppScreen.IDENTIFICATION),
    HistoryItemV35("Motivo de consulta y padecimiento actual", "Chief complaint and present illness", "Motivo principal, inicio, evolución, intensidad, características, desencadenantes y factores que modifican el cuadro.", "Main complaint, onset, evolution, intensity, characteristics, triggers and modifying factors.", AppScreen.HISTORY),
    HistoryItemV35("Antecedentes heredo-familiares", "Family history", "Antecedentes relevantes en familiares y posibles riesgos compartidos.", "Relevant family history and potentially shared risks.", AppScreen.HISTORY),
    HistoryItemV35("Antecedentes personales no patológicos", "Non-pathological personal history", "Hogar, alimentación, higiene bucal y general, actividad física, alcohol, tabaco, tóxicos, tatuajes y otras exposiciones.", "Home, diet, oral/general hygiene, physical activity, alcohol, tobacco, toxic exposures, tattoos and other exposures.", AppScreen.HISTORY),
    HistoryItemV35("Antecedentes personales patológicos", "Pathological personal history", "Enfermedades, alergias, medicamentos, hospitalizaciones y clasificación ASA cuando corresponda.", "Diseases, allergies, medicines, hospitalizations and ASA classification when applicable.", AppScreen.HISTORY),
    HistoryItemV35("Antecedentes quirúrgicos y traumáticos", "Surgical and trauma history", "Cirugías, traumatismos, fracturas, transfusiones y eventos relevantes.", "Surgery, trauma, fractures, transfusions and relevant events.", AppScreen.HISTORY),
    HistoryItemV35("Exploración física", "Physical examination", "Signos vitales, signos y síntomas, inspección general y exploración de cabeza y cuello.", "Vital signs, signs and symptoms, general inspection and head and neck examination.", extra = FolderExtraV33.PHYSICAL_HUB),
    HistoryItemV35("Exploración de la articulación temporomandibular", "Temporomandibular joint examination", "Movimientos mandibulares, apertura, lateralidades, protrusión/retrusión, dolor, ruidos, desviaciones, limitaciones y dimensiones verticales.", "Mandibular movements, opening, lateral excursions, protrusion/retrusion, pain, sounds, deviations, limitations and vertical dimensions.", AppScreen.ATM),
    HistoryItemV35("Examen de oclusión", "Occlusion examination", "Dentición, erupción, plano terminal, Angle, relación canina, líneas medias, overjet, overbite, mordidas, espacios, apiñamiento y contactos prematuros.", "Dentition, eruption, terminal plane, Angle, canine relation, midlines, overjet, overbite, bites, spaces, crowding and premature contacts.", AppScreen.OCCLUSION),
    HistoryItemV35("Antecedentes de tratamientos ortodónticos", "Orthodontic treatment history", "Tratamiento previo, tipo de aparato, duración e indicación.", "Previous treatment, appliance type, duration and indication.", extra = FolderExtraV33.ORTHODONTIC_HISTORY),
    HistoryItemV35("Anomalías dentales", "Dental anomalies", "Número, tamaño, forma, estructura y color; registra el órgano dentario y la distribución.", "Number, size, form, structure and color; record tooth and distribution.", extra = FolderExtraV33.DENTAL_ANOMALIES),
    HistoryItemV35("Alteraciones de erupción y posición", "Eruption and position alterations", "Erupción ectópica, transposición, retraso, retención/no erupción, impactación, inclusión y erupción precoz.", "Ectopic eruption, transposition, delayed eruption, retention/non-eruption, impaction, inclusion and premature eruption.", extra = FolderExtraV33.ERUPTION_ANOMALIES),
    HistoryItemV35("Hábitos y parafunciones", "Habits and parafunctions", "Succión digital/labial, interposición lingual, respiración bucal, onicofagia, chupón y bruxismo.", "Digit/lip sucking, tongue thrust, mouth breathing, nail biting, pacifier and bruxism.", extra = FolderExtraV33.HABITS)
)

@Composable
internal fun FolderMenuV35Screen(
    lang: String,
    onNavigate: (AppScreen) -> Unit,
    onExtra: (FolderExtraV33) -> Unit,
    onBack: () -> Unit
) {
    ResponsiveScreenV17(
        tr(lang, "Expediente abierto", "Open clinical record"),
        tr(lang, "Selecciona el apartado que deseas abrir. Los colores se adaptan a la paleta de ave elegida en Configuración.", "Choose the section you want to open. Colors follow the bird palette selected in Settings."),
        onBack
    ) { profile ->
        val stack = profile.largeSystemText
        val treatmentColor = MaterialTheme.colorScheme.tertiaryContainer
        val treatmentText = MaterialTheme.colorScheme.onTertiaryContainer
        val clinicalColor = MaterialTheme.colorScheme.secondaryContainer
        val clinicalText = MaterialTheme.colorScheme.onSecondaryContainer
        if (stack) {
            FolderColumnV35(lang, leftFolderV35, treatmentColor, treatmentText, onNavigate, onExtra)
            FolderColumnV35(lang, rightFolderV35, clinicalColor, clinicalText, onNavigate, onExtra)
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f)) { FolderColumnV35(lang, leftFolderV35, treatmentColor, treatmentText, onNavigate, onExtra) }
                Column(Modifier.weight(1f)) { FolderColumnV35(lang, rightFolderV35, clinicalColor, clinicalText, onNavigate, onExtra) }
            }
        }
    }
}

@Composable
private fun FolderColumnV35(
    lang: String,
    items: List<FolderItemV35>,
    color: Color,
    textColor: Color,
    onNavigate: (AppScreen) -> Unit,
    onExtra: (FolderExtraV33) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { item ->
            Card(
                onClick = { item.extra?.let(onExtra) ?: item.screen?.let(onNavigate) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = color, contentColor = textColor),
                border = BorderStroke(1.25.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    "${item.icon} ${if (lang == "en") item.en else item.es}",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 14.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
internal fun HistoryMenuV35Screen(lang: String, onNavigate: (AppScreen) -> Unit, onExtra: (FolderExtraV33) -> Unit, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "Historia clínica", "Clinical history"),
        tr(lang, "Selecciona un apartado. Cada submenú conserva la misma paleta visual de la aplicación.", "Choose a section. Every submenu keeps the app's selected visual palette."),
        onBack
    ) { profile ->
        AdaptiveGridV17(historyItemsV35.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val item = historyItemsV35[index]
            PaletteMenuCardV35(index, { item.extra?.let(onExtra) ?: item.screen?.let(onNavigate) }) {
                Text(if (lang == "en") item.en else item.es, fontWeight = FontWeight.Black)
                Text(if (lang == "en") item.detailEn else item.detailEs, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
internal fun PhysicalExamMenuV35Screen(lang: String, onNavigate: (AppScreen) -> Unit, onExtra: (FolderExtraV33) -> Unit, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "Exploración física", "Physical examination"),
        tr(lang, "Abre cada componente de la exploración física.", "Open each physical-examination component."),
        onBack
    ) { profile ->
        val items = listOf(
            FolderItemV35("Signos vitales", "Vital signs", "❤️", AppScreen.VITALS),
            FolderItemV35("Signos y síntomas", "Signs and symptoms", "🩹", extra = FolderExtraV33.SYMPTOMS),
            FolderItemV35("Exploración general", "General inspection", "👁", extra = FolderExtraV33.GENERAL_INSPECTION),
            FolderItemV35("Exploración de cabeza y cuello", "Head and neck examination", "👤", extra = FolderExtraV33.HEAD_NECK)
        )
        AdaptiveGridV17(items.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val item = items[index]
            PaletteMenuCardV35(index, { item.extra?.let(onExtra) ?: item.screen?.let(onNavigate) }) {
                Text("${item.icon} ${if (lang == "en") item.en else item.es}", fontWeight = FontWeight.Black, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
internal fun OdontogramHubV35Screen(lang: String, onNavigate: (AppScreen) -> Unit, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "Odontograma · exámenes de diagnóstico", "Odontogram · diagnostic examinations"),
        tr(lang, "Desde aquí abre el odontograma y los índices dentales solicitados.", "Open the odontogram and requested dental indices from here."),
        onBack
    ) { profile ->
        val items = listOf(
            FolderItemV35("Odontograma clínico", "Clinical odontogram", "🦷", AppScreen.ODONTOGRAM),
            FolderItemV35("CPOD · dentición permanente", "DMFT · permanent dentition", "➕", AppScreen.CPOD),
            FolderItemV35("ceod · dentición temporal", "dmft · primary dentition", "➕", AppScreen.CPOD),
            FolderItemV35("ICDAS", "ICDAS", "🔎", AppScreen.ICDAS),
            FolderItemV35("IHOS", "OHI-S", "🪥", AppScreen.IHOS),
            FolderItemV35("IPC", "CPI", "6️⃣", AppScreen.IPC)
        )
        AdaptiveGridV17(items.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val item = items[index]
            PaletteMenuCardV35(index, { item.screen?.let(onNavigate) }) {
                Text("${item.icon} ${if (lang == "en") item.en else item.es}", fontWeight = FontWeight.Black, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun PaletteMenuCardV35(index: Int, onClick: () -> Unit, content: @Composable () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val colors = listOf(
        scheme.primaryContainer to scheme.onPrimaryContainer,
        scheme.secondaryContainer to scheme.onSecondaryContainer,
        scheme.tertiaryContainer to scheme.onTertiaryContainer,
        scheme.surfaceVariant to scheme.onSurfaceVariant
    )
    val (background, foreground) = colors[index % colors.size]
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = background, contentColor = foreground),
        border = BorderStroke(1.dp, scheme.outline.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp), content = { content() })
    }
}

private val dentalCategoriesV35 = listOf(
    InteractiveCategoryV35(
        "Número", "Number",
        "Registra disminución o aumento del número de órganos dentarios y confirma con historia e imagen cuando corresponda.",
        "Record decreased or increased tooth number and confirm with history/imaging when appropriate.",
        listOf(
            "Hipodoncia / agenesia" to "Hypodontia / agenesis",
            "Oligodoncia" to "Oligodontia",
            "Anodoncia" to "Anodontia",
            "Diente supernumerario / hiperdoncia" to "Supernumerary tooth / hyperdontia"
        )
    ),
    InteractiveCategoryV35(
        "Tamaño", "Size",
        "Describe si la alteración es localizada o generalizada y qué dientes están afectados.",
        "Describe whether the finding is localized or generalized and which teeth are affected.",
        listOf("Microdoncia" to "Microdontia", "Macrodoncia" to "Macrodontia")
    ),
    InteractiveCategoryV35(
        "Forma y morfología", "Shape and morphology",
        "La forma coronaria, radicular e interna puede variar; algunos hallazgos requieren radiografía para caracterizarlos.",
        "Crown, root and internal morphology can vary; some findings require radiographs for characterization.",
        listOf(
            "Fusión" to "Fusion",
            "Geminación" to "Gemination",
            "Concrescencia" to "Concrescence",
            "Dens invaginatus" to "Dens invaginatus",
            "Dens evaginatus / cúspide accesoria" to "Dens evaginatus / accessory cusp",
            "Taurodontismo" to "Taurodontism",
            "Alteración radicular" to "Root morphology alteration"
        )
    ),
    InteractiveCategoryV35(
        "Estructura", "Structure",
        "Distingue alteraciones del esmalte, dentina o cemento. No asignes una etiología sólo por la apariencia.",
        "Distinguish enamel, dentin or cementum abnormalities. Do not assign an etiology from appearance alone.",
        listOf(
            "Defecto del esmalte: hipoplasia / hipomineralización" to "Enamel defect: hypoplasia / hypomineralization",
            "Amelogénesis imperfecta" to "Amelogenesis imperfecta",
            "Dentinogénesis imperfecta" to "Dentinogenesis imperfecta",
            "Displasia dentinaria" to "Dentin dysplasia",
            "Alteración del cemento" to "Cementum alteration"
        )
    ),
    InteractiveCategoryV35(
        "Color", "Color",
        "Registra color, distribución y si parece superficial o interno; el color es un hallazgo descriptivo, no un diagnóstico etiológico.",
        "Record color, distribution and whether it appears superficial or internal; color is descriptive, not an etiologic diagnosis.",
        listOf(
            "Cambio de color extrínseco / superficial" to "Extrinsic / superficial discoloration",
            "Cambio de color intrínseco" to "Intrinsic discoloration",
            "Localizado" to "Localized",
            "Generalizado" to "Generalized"
        )
    )
)

private val eruptionCategoriesV35 = listOf(
    InteractiveCategoryV35(
        "Tiempo de erupción", "Eruption timing",
        "Compara con la edad dental, la secuencia eruptiva y el contexto clínico; una cronología aislada no establece por sí sola la causa.",
        "Compare with dental age, eruption sequence and clinical context; timing alone does not establish the cause.",
        listOf(
            "Erupción adelantada / precoz" to "Early / premature eruption",
            "Diente natal" to "Natal tooth",
            "Diente neonatal" to "Neonatal tooth",
            "Erupción tardía / retrasada" to "Delayed eruption"
        )
    ),
    InteractiveCategoryV35(
        "Trayecto y posición", "Path and position",
        "Registra la vía eruptiva anormal y su relación con dientes vecinos; la evaluación radiográfica suele ser necesaria.",
        "Record the abnormal eruption path and relationship with neighboring teeth; radiographic assessment is often needed.",
        listOf(
            "Erupción ectópica" to "Ectopic eruption",
            "Transposición" to "Transposition",
            "Desplazamiento / posición anómala" to "Displacement / abnormal position"
        )
    ),
    InteractiveCategoryV35(
        "Diente no erupcionado", "Unerupted tooth",
        "Retención, inclusión e impactación pueden solaparse según la terminología usada. Describe primero el hallazgo clínico/radiográfico y la posible barrera mecánica.",
        "Retention, inclusion and impaction can overlap depending on terminology. First describe clinical/radiographic findings and any mechanical barrier.",
        listOf(
            "Retención / no erupción más allá de lo esperado" to "Retention / unerupted beyond expected timing",
            "Impactación" to "Impaction",
            "Inclusión intraósea" to "Intraosseous inclusion",
            "Falla primaria de erupción" to "Primary failure of eruption"
        )
    ),
    InteractiveCategoryV35(
        "Alteraciones relacionadas", "Related eruption disturbances",
        "Algunos problemas de erupción se relacionan con anquilosis, pérdida de espacio, supernumerarios u otras alteraciones locales.",
        "Some eruption problems relate to ankylosis, space loss, supernumerary teeth or other local disturbances.",
        listOf(
            "Anquilosis / diente sumergido" to "Ankylosis / submerged tooth",
            "Obstáculo local sospechado" to "Suspected local obstruction",
            "Asimetría eruptiva" to "Asymmetric eruption"
        )
    )
)

@Composable
internal fun DentalAnomaliesV35Screen(lang: String, onBack: () -> Unit) {
    InteractiveAnomalyScreenV35(
        lang = lang,
        titleEs = "Anomalías dentales",
        titleEn = "Dental anomalies",
        introEs = "Selecciona una categoría y registra los hallazgos. La clasificación organiza anomalías de número, tamaño, forma/morfología, estructura y color.",
        introEn = "Choose a category and record findings. The classification organizes anomalies of number, size, shape/morphology, structure and color.",
        categories = dentalCategoriesV35,
        onBack = onBack
    )
}

@Composable
internal fun EruptionAnomaliesV35Screen(lang: String, onBack: () -> Unit) {
    InteractiveAnomalyScreenV35(
        lang = lang,
        titleEs = "Alteraciones de erupción y posición",
        titleEn = "Eruption and position alterations",
        introEs = "Registra tiempo, trayecto, posición y estado de erupción. Confirma los hallazgos con exploración e imagen cuando esté indicado.",
        introEn = "Record timing, path, position and eruption status. Confirm findings with examination and imaging when indicated.",
        categories = eruptionCategoriesV35,
        onBack = onBack
    )
}

@Composable
private fun InteractiveAnomalyScreenV35(
    lang: String,
    titleEs: String,
    titleEn: String,
    introEs: String,
    introEn: String,
    categories: List<InteractiveCategoryV35>,
    onBack: () -> Unit
) {
    var categoryIndex by remember { mutableStateOf(0) }
    val selected = remember { mutableStateListOf<String>() }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    ResponsiveScreenV17(tr(lang, titleEs, titleEn), tr(lang, introEs, introEn), onBack) { profile ->
        AdaptiveGridV17(categories.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val c = categories[index]
            FilterChip(
                selected = categoryIndex == index,
                onClick = { categoryIndex = index },
                label = { Text(if (lang == "en") c.en else c.es, fontWeight = FontWeight.Bold) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        val category = categories[categoryIndex]
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
        ) {
            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(if (lang == "en") category.en else category.es, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                Text(if (lang == "en") category.introEn else category.introEs)
            }
        }

        Text(tr(lang, "Hallazgos", "Findings"), fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
        category.findings.forEach { finding ->
            val key = finding.first
            FilterChip(
                selected = selected.contains(key),
                onClick = { if (selected.contains(key)) selected.remove(key) else selected.add(key) },
                label = { Text(if (lang == "en") finding.second else finding.first) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(tr(lang, "Órgano dentario / distribución", "Tooth / distribution")) },
            placeholder = { Text(tr(lang, "Ej. OD 12; localizado / generalizado", "E.g. tooth 12; localized / generalized")) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text(tr(lang, "Descripción clínica y/o radiográfica", "Clinical and/or radiographic description")) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(tr(lang, "Resumen del registro", "Record summary"), fontWeight = FontWeight.Black)
                Text(
                    if (selected.isEmpty()) tr(lang, "Aún no hay hallazgos seleccionados.", "No findings selected yet.")
                    else selected.joinToString(" · ")
                )
                if (location.isNotBlank()) Text("${tr(lang, "Localización", "Location")}: $location")
                if (notes.isNotBlank()) Text("${tr(lang, "Descripción", "Description")}: $notes")
            }
        }

        Text(
            tr(lang,
                "Uso educativo: registra primero lo observado. La apariencia clínica por sí sola no confirma la etiología; varias anomalías requieren correlación con historia, exploración y radiografías.",
                "Educational use: record observations first. Clinical appearance alone does not confirm etiology; several anomalies require correlation with history, examination and radiographs."
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
