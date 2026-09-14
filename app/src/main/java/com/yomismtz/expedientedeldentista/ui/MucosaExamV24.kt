package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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
import com.yomismtz.expedientedeldentista.clinical.MucosaFindingState

private data class MucosaRegionV24(
    val id: String,
    val es: String,
    val en: String,
    val normalEs: String,
    val normalEn: String,
    val correlationsEs: String,
    val correlationsEn: String,
    val examEs: String,
    val examEn: String
)

private val mucosaRegionsV24 = listOf(
    MucosaRegionV24(
        "labios", "Labios, bermellón y mucosa labial", "Lips, vermilion and labial mucosa",
        "Integridad, hidratación y coloración sin cambios focales aparentes.",
        "Integrity, hydration and coloration without apparent focal changes.",
        "Correlaciones frecuentes: queilitis, herpes labial, trauma y mucocele de mucosa labial. El aspecto por sí solo no confirma etiología.",
        "Common correlations: cheilitis, herpes labialis, trauma, and labial-mucosa mucocele. Appearance alone does not confirm etiology.",
        "Inspecciona piel perioral, comisuras y bermellón; eversa ambos labios y palpa aumentos de volumen o induración.",
        "Inspect perioral skin, commissures and vermilion; evert both lips and palpate swelling or induration."
    ),
    MucosaRegionV24(
        "yugal", "Mucosa yugal / carrillos", "Buccal mucosa / cheeks",
        "Rosada, húmeda, sin ulceración ni induración; compara ambos lados.",
        "Pink and moist, without ulceration or induration; compare both sides.",
        "Correlaciones frecuentes: línea alba/queratosis friccional, mordisqueo y úlcera traumática. Placas blancas, rojas o mixtas persistentes requieren valoración específica.",
        "Common correlations: linea alba/frictional keratosis, cheek biting, and traumatic ulcer. Persistent white, red, or mixed plaques require specific assessment.",
        "Retrae con espejo desde comisura hasta región posterior; observa el orificio del conducto parotídeo y palpa lesiones o masas.",
        "Retract with a mirror from the commissure posteriorly; inspect the parotid duct opening and palpate lesions or masses."
    ),
    MucosaRegionV24(
        "encia", "Encía y mucosa alveolar", "Gingiva and alveolar mucosa",
        "Encía de contorno regular y mucosa alveolar sin ulceración, fístula ni aumento de volumen aparente.",
        "Gingiva with regular contour and alveolar mucosa without apparent ulceration, sinus tract, or swelling.",
        "Correlaciones frecuentes: inflamación gingival, agrandamiento gingival, absceso/fístula. La periodontitis no se diagnostica por inspección de mucosa: requiere sondaje y correlación clínica/radiográfica.",
        "Common correlations: gingival inflammation, gingival enlargement, abscess/sinus tract. Periodontitis is not diagnosed from mucosal inspection alone; probing and clinical/radiographic correlation are required.",
        "Inspecciona encía marginal, papilar y adherida, mucosa alveolar y fondos de vestíbulo; relaciona con periodontograma cuando proceda.",
        "Inspect marginal, papillary and attached gingiva, alveolar mucosa and vestibules; correlate with periodontal charting when appropriate."
    ),
    MucosaRegionV24(
        "lengua", "Lengua: dorso, bordes, punta y cara ventral", "Tongue: dorsum, borders, tip and ventral surface",
        "Húmeda y móvil, sin ulceración, placa focal, masa ni induración aparente.",
        "Moist and mobile, without apparent ulceration, focal plaque, mass, or induration.",
        "Correlaciones frecuentes: lengua geográfica o fisurada, candidiasis, trauma y alteraciones del frenillo. Lesiones persistentes en bordes laterales/ventrales requieren especial atención.",
        "Common correlations: geographic or fissured tongue, candidiasis, trauma, and frenulum abnormalities. Persistent lateral/ventral lesions deserve special attention.",
        "Pide protrusión y movimientos laterales; usa gasa para sujetar la punta y examina especialmente bordes laterales y cara ventral; palpa si hay lesión.",
        "Ask for protrusion and lateral movements; hold the tip with gauze and especially inspect lateral borders and ventral surface; palpate when a lesion is present."
    ),
    MucosaRegionV24(
        "piso", "Piso de boca y región sublingual", "Floor of mouth and sublingual region",
        "Blando, húmedo y sin aumento de volumen o masa aparente.",
        "Soft and moist, without apparent swelling or mass.",
        "Correlaciones frecuentes: ránula, alteración del flujo salival y obstrucción del conducto; una sialolitiasis requiere correlación con historia, palpación y, cuando corresponda, imagen.",
        "Common correlations: ranula, salivary-flow alteration and duct obstruction; sialolithiasis requires correlation with history, palpation and, when appropriate, imaging.",
        "Eleva la lengua, observa carúnculas y pliegues sublinguales; realiza palpación bimanual cuando esté indicada.",
        "Lift the tongue, inspect caruncles and sublingual folds; perform bimanual palpation when indicated."
    ),
    MucosaRegionV24(
        "paladar", "Paladar duro y blando", "Hard and soft palate",
        "Sin ulceración ni masa; paladar blando móvil y simétrico.",
        "Without ulceration or mass; soft palate mobile and symmetric.",
        "Correlaciones frecuentes: torus palatino, trauma/quemadura, petequias y candidiasis. El término 'estomatitis' debe acompañarse de una descripción clínica y contexto causal.",
        "Common correlations: palatal torus, trauma/burn, petechiae and candidiasis. The term 'stomatitis' should be accompanied by a clinical description and causal context.",
        "Ilumina directamente; inspecciona rugas, rafe, unión duro-blando, úvula y pilares; palpa cualquier prominencia o masa.",
        "Use direct illumination; inspect rugae, raphe, hard-soft junction, uvula and pillars; palpate any prominence or mass."
    ),
    MucosaRegionV24(
        "orofaringe", "Orofaringe y pilares amigdalinos", "Oropharynx and tonsillar pillars",
        "Sin exudado, ulceración, masa o asimetría marcada aparente.",
        "Without apparent exudate, ulceration, mass, or marked asymmetry.",
        "Correlaciones frecuentes: inflamación, exudado, hipertrofia y lesiones ulceradas o de placa. La causa no se define sólo por inspección.",
        "Common correlations: inflammation, exudate, hypertrophy, and ulcerated or plaque-like lesions. Cause is not established by inspection alone.",
        "Observa con luz directa; usa depresor sólo cuando sea necesario y valora simetría, movilidad del paladar y presencia de lesiones.",
        "Inspect with direct light; use a tongue depressor only when needed and assess symmetry, palatal mobility, and lesions."
    ),
    MucosaRegionV24(
        "general", "Mucosa oral general", "General oral mucosa",
        "Hidratación e integridad conservadas, sin lesiones focales aparentes.",
        "Hydration and integrity preserved, without apparent focal lesions.",
        "Patrones que pueden encontrarse en distintas zonas incluyen aftas, lesiones liquenoides/liquen plano, candidiasis, sequedad y lesiones pigmentadas, blancas, rojas o mixtas. Son descriptores/correlaciones, no diagnósticos automáticos.",
        "Patterns across multiple sites may include aphthae, lichenoid/lichen planus lesions, candidiasis, dryness, and pigmented, white, red, or mixed lesions. These are descriptors/correlations, not automatic diagnoses.",
        "Realiza una secuencia constante y compara regiones bilaterales. Documenta localización exacta y morfología de cualquier alteración.",
        "Use a consistent sequence and compare bilateral regions. Document the exact site and morphology of any abnormality."
    )
)

private data class MucosaChoiceV24(val code: String, val es: String, val en: String)

private val mucosaFindingTypesV24 = listOf(
    MucosaChoiceV24("NORMAL", "Sin alteración aparente", "No apparent abnormality"),
    MucosaChoiceV24("ULCER", "Úlcera / erosión", "Ulcer / erosion"),
    MucosaChoiceV24("VESICLE", "Vesícula / ampolla", "Vesicle / bulla"),
    MucosaChoiceV24("WHITE", "Lesión blanca", "White lesion"),
    MucosaChoiceV24("RED", "Lesión roja", "Red lesion"),
    MucosaChoiceV24("RED_WHITE", "Lesión roja y blanca", "Red-and-white lesion"),
    MucosaChoiceV24("PIGMENTED", "Lesión pigmentada", "Pigmented lesion"),
    MucosaChoiceV24("MASS", "Nódulo / masa / aumento de volumen", "Nodule / mass / swelling"),
    MucosaChoiceV24("FISSURE", "Fisura", "Fissure"),
    MucosaChoiceV24("PETECHIA", "Petequia / equimosis", "Petechia / ecchymosis"),
    MucosaChoiceV24("FISTULA", "Fístula / trayecto de drenaje", "Sinus tract / drainage tract"),
    MucosaChoiceV24("DRYNESS", "Sequedad / atrofia aparente", "Dryness / apparent atrophy"),
    MucosaChoiceV24("OTHER", "Otro hallazgo", "Other finding")
)
private val colorsV24 = listOf(
    MucosaChoiceV24("PINK", "Rosado", "Pink"),
    MucosaChoiceV24("WHITE", "Blanco", "White"),
    MucosaChoiceV24("RED", "Rojo", "Red"),
    MucosaChoiceV24("RED_WHITE", "Rojo-blanco", "Red-white"),
    MucosaChoiceV24("BLUE", "Azulado", "Bluish"),
    MucosaChoiceV24("BROWN_BLACK", "Marrón/negro", "Brown/black"),
    MucosaChoiceV24("YELLOW", "Amarillento", "Yellowish"),
    MucosaChoiceV24("OTHER", "Otro", "Other")
)
private val surfacesV24 = listOf(
    MucosaChoiceV24("SMOOTH", "Lisa", "Smooth"),
    MucosaChoiceV24("ROUGH", "Rugosa", "Rough"),
    MucosaChoiceV24("PAPILLARY", "Papilar / verrugosa", "Papillary / verrucous"),
    MucosaChoiceV24("PSEUDOMEMBRANE", "Pseudomembranosa", "Pseudomembranous"),
    MucosaChoiceV24("CRUSTED", "Costrosa", "Crusted"),
    MucosaChoiceV24("OTHER", "Otra", "Other")
)
private val bordersV24 = listOf(
    MucosaChoiceV24("WELL_DEFINED", "Bien definidos", "Well-defined"),
    MucosaChoiceV24("ILL_DEFINED", "Mal definidos", "Ill-defined"),
    MucosaChoiceV24("REGULAR", "Regulares", "Regular"),
    MucosaChoiceV24("IRREGULAR", "Irregulares", "Irregular")
)
private val consistencyV24 = listOf(
    MucosaChoiceV24("SOFT", "Blanda", "Soft"),
    MucosaChoiceV24("FIRM", "Firme", "Firm"),
    MucosaChoiceV24("INDURATED", "Indurada", "Indurated"),
    MucosaChoiceV24("FLUCTUANT", "Fluctuante", "Fluctuant"),
    MucosaChoiceV24("NOT_PALPATED", "No palpada", "Not palpated")
)
private val symptomsV24 = listOf(
    MucosaChoiceV24("NONE", "Asintomática", "Asymptomatic"),
    MucosaChoiceV24("PAIN", "Dolor", "Pain"),
    MucosaChoiceV24("BURNING", "Ardor", "Burning"),
    MucosaChoiceV24("ITCHING", "Prurito", "Itching"),
    MucosaChoiceV24("BLEEDING", "Sangrado", "Bleeding"),
    MucosaChoiceV24("OTHER", "Otro", "Other")
)

private fun mucosaLabelV24(lang: String, code: String, options: List<MucosaChoiceV24>): String =
    options.firstOrNull { it.code == code }?.let { if (lang == "en") it.en else it.es } ?: tr(lang, "No registrado", "Not recorded")

@Composable
private fun MucosaChoiceRowV24(
    lang: String,
    titleEs: String,
    titleEn: String,
    value: String,
    options: List<MucosaChoiceV24>,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(tr(lang, titleEs, titleEn), fontWeight = FontWeight.Bold)
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = value == option.code,
                    onClick = { onSelect(option.code) },
                    label = { Text(if (lang == "en") option.en else option.es) }
                )
            }
        }
    }
}

private fun numericMucosaV24(raw: String): String = raw
    .replace(',', '.')
    .filter { it.isDigit() || it == '.' }
    .let { filtered ->
        val firstDot = filtered.indexOf('.')
        if (firstDot < 0) filtered else filtered.substring(0, firstDot + 1) + filtered.substring(firstDot + 1).replace(".", "")
    }
    .take(7)

@Composable
fun MucosaExamV24Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedId by remember { mutableStateOf(mucosaRegionsV24.first().id) }
    var mapOpen by remember { mutableStateOf(false) }

    if (mapOpen) {
        MucosaInteractiveV19Screen(lang) { mapOpen = false }
        return
    }

    val region = mucosaRegionsV24.first { it.id == selectedId }
    val current = session.mucosaFindings[selectedId] ?: MucosaFindingState()
    fun save(next: MucosaFindingState) = onSessionChanged(
        session.copy(mucosaFindings = session.mucosaFindings + (selectedId to next))
    )

    ResponsiveScreenV17(
        tr(lang, "Examen de mucosas orales", "Oral mucosa examination"),
        tr(lang,
            "Describe localización y morfología. Las correlaciones frecuentes son apoyo didáctico, no un diagnóstico automático.",
            "Describe location and morphology. Common correlations are educational support, not an automatic diagnosis."),
        onBack
    ) { _ ->
        ResponsiveSectionV17(tr(lang, "1 · Selecciona la región", "1 · Select the region")) {
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                mucosaRegionsV24.forEach { item ->
                    FilterChip(
                        selectedId == item.id,
                        { selectedId = item.id },
                        { Text(if (lang == "en") item.en else item.es) }
                    )
                }
            }
            Button(onClick = { mapOpen = true }, modifier = Modifier.fillMaxWidth()) {
                Text(tr(lang, "Abrir mapa anatómico interactivo", "Open interactive anatomic map"))
            }
        }

        ResponsiveSectionV17(if (lang == "en") region.en else region.es) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text(tr(lang, "Aspecto sin alteración aparente", "No apparent abnormality"), fontWeight = FontWeight.Black)
                    Text(if (lang == "en") region.normalEn else region.normalEs)
                    Text(tr(lang, "Cómo explorar", "How to examine"), fontWeight = FontWeight.Black)
                    Text(if (lang == "en") region.examEn else region.examEs)
                    Text(tr(lang, "Correlaciones clínicas frecuentes", "Common clinical correlations"), fontWeight = FontWeight.Black)
                    Text(if (lang == "en") region.correlationsEn else region.correlationsEs)
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "2 · Registro morfológico", "2 · Morphologic record")) {
            MucosaChoiceRowV24(lang, "Hallazgo principal", "Main finding", current.findingType, mucosaFindingTypesV24) {
                save(if (it == "NORMAL") MucosaFindingState(findingType = "NORMAL") else current.copy(findingType = it))
            }
            if (current.findingType.isNotBlank() && current.findingType != "NORMAL") {
                OutlinedTextField(
                    current.sizeMm,
                    { save(current.copy(sizeMm = numericMucosaV24(it))) },
                    label = { Text(tr(lang, "Tamaño mayor aproximado (mm)", "Approximate largest dimension (mm)")) },
                    modifier = Modifier.fillMaxWidth()
                )
                MucosaChoiceRowV24(lang, "Color", "Color", current.color, colorsV24) { save(current.copy(color = it)) }
                MucosaChoiceRowV24(lang, "Superficie", "Surface", current.surface, surfacesV24) { save(current.copy(surface = it)) }
                MucosaChoiceRowV24(lang, "Bordes", "Borders", current.borders, bordersV24) { save(current.copy(borders = it)) }
                MucosaChoiceRowV24(lang, "Consistencia", "Consistency", current.consistency, consistencyV24) { save(current.copy(consistency = it)) }
                MucosaChoiceRowV24(lang, "Síntomas", "Symptoms", current.symptoms, symptomsV24) { save(current.copy(symptoms = it)) }
                OutlinedTextField(
                    current.notes,
                    { save(current.copy(notes = it.take(500))) },
                    label = { Text(tr(lang, "Número, forma, base, duración, evolución, factores traumáticos y otros datos", "Number, shape, base, duration, evolution, traumatic factors, and other data")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(tr(lang, "¿Qué escribo en el expediente?", "What do I write in the record?"), fontWeight = FontWeight.Black)
                val regionName = if (lang == "en") region.en else region.es
                val text = when {
                    current.findingType.isBlank() -> tr(lang, "$regionName: no evaluado.", "$regionName: not assessed.")
                    current.findingType == "NORMAL" -> tr(lang, "$regionName: sin alteración aparente.", "$regionName: no apparent abnormality.")
                    else -> buildString {
                        append("$regionName: ${mucosaLabelV24(lang, current.findingType, mucosaFindingTypesV24)}")
                        if (current.sizeMm.isNotBlank()) append(", ${current.sizeMm} mm")
                        if (current.color.isNotBlank()) append(", ${mucosaLabelV24(lang, current.color, colorsV24)}")
                        if (current.surface.isNotBlank()) append(", ${mucosaLabelV24(lang, current.surface, surfacesV24)}")
                        if (current.borders.isNotBlank()) append(", ${mucosaLabelV24(lang, current.borders, bordersV24)}")
                        if (current.consistency.isNotBlank()) append(", ${mucosaLabelV24(lang, current.consistency, consistencyV24)}")
                        if (current.symptoms.isNotBlank()) append(", ${mucosaLabelV24(lang, current.symptoms, symptomsV24)}")
                        if (current.notes.isNotBlank()) append("; ${current.notes}")
                        append(".")
                    }
                }
                Text(text)
            }
        }

        NoticeCard(tr(lang,
            "Lesiones persistentes o progresivas, induradas, masas, ulceraciones sin causa clara y lesiones blancas/rojas/mixtas sospechosas requieren valoración profesional y, cuando corresponda, biopsia o referencia. El registro visual no sustituye el diagnóstico histopatológico.",
            "Persistent or progressive lesions, induration, masses, unexplained ulceration, and suspicious white/red/mixed lesions require professional assessment and, when appropriate, biopsy or referral. Visual recording does not replace histopathologic diagnosis."))
    }
}
