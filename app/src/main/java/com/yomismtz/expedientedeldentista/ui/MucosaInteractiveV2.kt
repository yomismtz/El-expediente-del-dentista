package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class MucosaZone(
    val id: String,
    val es: String,
    val en: String,
    val normalEs: String,
    val normalEn: String,
    val changesEs: String,
    val changesEn: String,
    val exploreEs: String,
    val exploreEn: String
)

private val mucosaZones = listOf(
    MucosaZone(
        "labio_superior", "Labio superior y bermellón", "Upper lip and vermilion",
        "Rosado, hidratado e íntegro, con contorno regular y sin lesiones aparentes.",
        "Pink, hydrated and intact, with a regular contour and no apparent lesions.",
        "Fisuras, costras, vesículas, úlceras, edema, resequedad, pigmentación o cambios de color.",
        "Fissures, crusts, vesicles, ulcers, edema, dryness, pigmentation or color change.",
        "Inspecciona con buena iluminación; eversa el labio y palpa suavemente si hay aumento de volumen o induración.",
        "Inspect with good lighting; evert the lip and palpate gently if swelling or induration is present."
    ),
    MucosaZone(
        "labio_inferior", "Labio inferior y mucosa labial", "Lower lip and labial mucosa",
        "Rosado, húmedo, blando e íntegro, sin aumento de volumen.",
        "Pink, moist, soft and intact, without swelling.",
        "Úlcera traumática, mucocele, queilitis, pigmentación, fisuras, costras o aumento de volumen.",
        "Traumatic ulcer, mucocele, cheilitis, pigmentation, fissures, crusts or swelling.",
        "Eversa el labio inferior y revisa fondo de vestíbulo, frenillo, glándulas menores y cualquier asimetría.",
        "Evert the lower lip and inspect the vestibule, frenum, minor glands and any asymmetry."
    ),
    MucosaZone(
        "carrillo_derecho", "Carrillo derecho / mucosa yugal", "Right buccal mucosa",
        "Rosada, húmeda y lisa; salida del conducto parotídeo sin alteraciones aparentes.",
        "Pink, moist and smooth; parotid duct opening without apparent abnormality.",
        "Línea alba, mordisqueo, úlcera, placa blanca, pigmentación, eritema, masa o alteración salival.",
        "Linea alba, cheek biting, ulcer, white plaque, pigmentation, erythema, mass or salivary change.",
        "Separa el carrillo con espejo, observa de comisura a región posterior y palpa bimanualmente si hay una masa.",
        "Retract with a mirror, inspect from commissure to posterior region and use bimanual palpation for a mass."
    ),
    MucosaZone(
        "carrillo_izquierdo", "Carrillo izquierdo / mucosa yugal", "Left buccal mucosa",
        "Rosada, húmeda y lisa; sin ulceración ni induración.",
        "Pink, moist and smooth; no ulceration or induration.",
        "Línea alba, mordisqueo, úlcera, placa blanca, pigmentación, eritema, masa o alteración salival.",
        "Linea alba, cheek biting, ulcer, white plaque, pigmentation, erythema, mass or salivary change.",
        "Explora sistemáticamente toda la mucosa yugal y compara ambos lados.",
        "Systematically inspect the entire buccal mucosa and compare both sides."
    ),
    MucosaZone(
        "encia", "Encía y mucosa alveolar", "Gingiva and alveolar mucosa",
        "Encía firme, color acorde al fenotipo, contorno regular y sin ulceraciones; mucosa alveolar móvil y húmeda.",
        "Firm gingiva, color consistent with phenotype, regular contour and no ulceration; alveolar mucosa mobile and moist.",
        "Eritema, edema, sangrado, recesión, aumento de volumen, fístula, ulceración o pigmentación atípica.",
        "Erythema, edema, bleeding, recession, swelling, fistula, ulceration or atypical pigmentation.",
        "Inspecciona marginal, papilar y adherida; correlaciona con sondaje periodontal cuando corresponda.",
        "Inspect marginal, papillary and attached gingiva; correlate with periodontal probing when appropriate."
    ),
    MucosaZone(
        "paladar_duro", "Paladar duro", "Hard palate",
        "Rosado pálido, firme, queratinizado e íntegro; rugas y rafe medio reconocibles.",
        "Pale pink, firm, keratinized and intact; rugae and midline raphe recognizable.",
        "Torus, petequias, placas, úlceras, eritema, pigmentación, quemadura o aumento de volumen.",
        "Torus, petechiae, plaques, ulcers, erythema, pigmentation, burn or swelling.",
        "Observa con iluminación directa y palpa cualquier elevación para valorar consistencia y fijación.",
        "Inspect with direct light and palpate any elevation to assess consistency and fixation."
    ),
    MucosaZone(
        "paladar_blando", "Paladar blando, úvula y pilares", "Soft palate, uvula and pillars",
        "Rosado, móvil, con elevación simétrica y úvula centrada.",
        "Pink and mobile, with symmetric elevation and centered uvula.",
        "Eritema, petequias, úlceras, edema, asimetría, exudado, placas o desviación de úvula.",
        "Erythema, petechiae, ulcers, edema, asymmetry, exudate, plaques or uvular deviation.",
        "Pide abrir ampliamente y fonar; observa movilidad, simetría, pilares y transición con la orofaringe.",
        "Ask the patient to open widely and phonate; observe mobility, symmetry, pillars and transition to the oropharynx."
    ),
    MucosaZone(
        "lengua_dorso", "Lengua: dorso", "Tongue: dorsum",
        "Rosada, papilada, húmeda y con movilidad conservada.",
        "Pink, papillary, moist and with preserved mobility.",
        "Saburra, depapilación, lengua geográfica/fisurada, placa, pigmentación, úlcera o masa.",
        "Coating, depapillation, geographic/fissured tongue, plaque, pigmentation, ulcer or mass.",
        "Pide protruir la lengua y moverla; observa simetría, papilas, superficie y cualquier cambio focal.",
        "Ask for tongue protrusion and movement; inspect symmetry, papillae, surface and focal changes."
    ),
    MucosaZone(
        "lengua_lateral", "Lengua: bordes laterales y cara ventral", "Tongue: lateral borders and ventral surface",
        "Mucosa rosada, húmeda, flexible y sin induración.",
        "Pink, moist, flexible mucosa without induration.",
        "Úlcera, placa blanca/roja, masa, induración, pigmentación o lesión vascular.",
        "Ulcer, white/red plaque, mass, induration, pigmentation or vascular lesion.",
        "Sujeta la punta con gasa, desplaza a ambos lados y palpa los bordes; revisa también la cara ventral.",
        "Hold the tip with gauze, move to both sides and palpate the borders; also inspect the ventral surface."
    ),
    MucosaZone(
        "piso_boca", "Piso de boca", "Floor of mouth",
        "Rosado, blando, húmedo, sin masas ni aumento de volumen; salivación visible cuando corresponde.",
        "Pink, soft, moist, without masses or swelling; visible salivary flow when appropriate.",
        "Ránula, aumento de volumen, coloración azulada/rojiza, masa, úlcera, dolor o disminución del flujo salival.",
        "Ranula, swelling, bluish/reddish change, mass, ulcer, pain or reduced salivary flow.",
        "Eleva la lengua y realiza palpación bimanual cuando proceda; observa frenillo y desembocaduras salivales.",
        "Lift the tongue and use bimanual palpation when appropriate; inspect the frenum and salivary duct openings."
    ),
    MucosaZone(
        "orofaringe", "Orofaringe y amígdalas", "Oropharynx and tonsils",
        "Pilares sin inflamación evidente, amígdalas sin exudado y pared posterior sin lesiones aparentes.",
        "Pillars without evident inflammation, tonsils without exudate and posterior wall without apparent lesions.",
        "Eritema, exudado, hipertrofia, placas, secreción, asimetría o ulceración.",
        "Erythema, exudate, hypertrophy, plaques, secretion, asymmetry or ulceration.",
        "Inspecciona con buena iluminación y depresor cuando esté indicado; evita provocar reflejo nauseoso innecesariamente.",
        "Inspect with good lighting and a tongue depressor when indicated; avoid unnecessarily provoking gag reflex."
    )
)

@Composable
fun MucosaInteractiveV2Screen(lang: String, onBack: () -> Unit) {
    var selectedId by remember { mutableStateOf("labio_superior") }
    var detailTab by remember { mutableStateOf(0) }
    var finding by remember { mutableStateOf("Normal") }
    var sizeMm by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("Rosado") }
    var surface by remember { mutableStateOf("Lisa") }
    var consistency by remember { mutableStateOf("Blanda") }
    var symptoms by remember { mutableStateOf("Ninguno") }
    var notes by remember { mutableStateOf("") }
    var draft by remember { mutableStateOf(listOf<String>()) }

    val selected = mucosaZones.first { it.id == selectedId }
    val normal = finding == "Normal"
    val zoneName = if (lang == "en") selected.en else selected.es
    val narrative = if (lang == "en") {
        if (normal) {
            "$zoneName: ${selected.normalEn}"
        } else {
            "$zoneName: $finding; approximate size ${if (sizeMm.isBlank()) "not entered" else "$sizeMm mm"}; color $color; surface $surface; consistency $consistency; symptoms $symptoms.${if (notes.isBlank()) "" else " Notes: $notes"} Descriptive educational record; correlate clinically before assigning a diagnosis."
        }
    } else {
        if (normal) {
            "$zoneName: ${selected.normalEs}"
        } else {
            "$zoneName: $finding; tamaño aproximado ${if (sizeMm.isBlank()) "no registrado" else "$sizeMm mm"}; color $color; superficie $surface; consistencia $consistency; síntomas $symptoms.${if (notes.isBlank()) "" else " Observaciones: $notes"} Registro descriptivo educativo; correlacionar clínicamente antes de establecer un diagnóstico."
        }
    }

    BackHandler { onBack() }

    LazyColumn(
        Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Mucosas orales interactivas", "Interactive oral mucosa"),
                onBack,
                tr(
                    lang,
                    "Explora por zonas, compara con la normalidad y practica una descripción clínica. Los datos son temporales y no se guardan como expediente de un paciente.",
                    "Explore by region, compare with normal findings and practice a clinical description. Data are temporary and are not stored as a patient record."
                )
            )
        }

        item {
            SectionCard(tr(lang, "1 · Toca una zona de la boca", "1 · Tap an oral region")) {
                InteractiveMouthMap(selectedId) { selectedId = it }
                Text(
                    tr(lang, "Zona seleccionada: ", "Selected region: ") + zoneName,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    items(mucosaZones) { zone ->
                        FilterChip(
                            selected = selectedId == zone.id,
                            onClick = { selectedId = zone.id },
                            label = { Text(if (lang == "en") zone.en else zone.es) }
                        )
                    }
                }
            }
        }

        item {
            SectionCard(zoneName) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        tr(lang, "Normal", "Normal"),
                        tr(lang, "Alteraciones", "Changes"),
                        tr(lang, "Cómo explorar", "How to examine")
                    ).forEachIndexed { index, label ->
                        FilterChip(
                            selected = detailTab == index,
                            onClick = { detailTab = index },
                            label = { Text(label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                val text = when (detailTab) {
                    1 -> if (lang == "en") selected.changesEn else selected.changesEs
                    2 -> if (lang == "en") selected.exploreEn else selected.exploreEs
                    else -> if (lang == "en") selected.normalEn else selected.normalEs
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(text, modifier = Modifier.padding(14.dp))
                }
            }
        }

        item {
            SectionCard(tr(lang, "2 · Ficha de registro rápido", "2 · Quick description practice")) {
                Text(tr(lang, "Hallazgo", "Finding"), fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    listOf("Normal", "Úlcera", "Placa blanca", "Placa roja", "Eritema", "Aumento de volumen", "Pigmentación", "Vesícula", "Fisura/costra", "Otro").forEach { option ->
                        FilterChip(finding == option, { finding = option }, { Text(option) })
                    }
                }

                if (!normal) {
                    OutlinedTextField(
                        value = sizeMm,
                        onValueChange = { sizeMm = it.filter { c -> c.isDigit() || c == '.' }.take(6) },
                        label = { Text(tr(lang, "Tamaño aproximado (mm)", "Approximate size (mm)")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text(tr(lang, "Color", "Color"), fontWeight = FontWeight.Bold)
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        listOf("Rosado", "Rojo", "Blanco", "Azulado", "Negro/marrón", "Mixto").forEach { option ->
                            FilterChip(color == option, { color = option }, { Text(option) })
                        }
                    }

                    Text(tr(lang, "Superficie", "Surface"), fontWeight = FontWeight.Bold)
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        listOf("Lisa", "Rugosa", "Ulcerada", "Papilar", "Costrosa", "Vesicular").forEach { option ->
                            FilterChip(surface == option, { surface = option }, { Text(option) })
                        }
                    }

                    Text(tr(lang, "Consistencia", "Consistency"), fontWeight = FontWeight.Bold)
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        listOf("Blanda", "Firme", "Indurada", "Fluctuante", "No valorada").forEach { option ->
                            FilterChip(consistency == option, { consistency = option }, { Text(option) })
                        }
                    }

                    Text(tr(lang, "Síntomas", "Symptoms"), fontWeight = FontWeight.Bold)
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        listOf("Ninguno", "Dolor", "Ardor", "Sangrado", "Prurito", "Parestesia").forEach { option ->
                            FilterChip(symptoms == option, { symptoms = option }, { Text(option) })
                        }
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it.take(160) },
                        label = { Text(tr(lang, "Observaciones descriptivas", "Descriptive notes")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(tr(lang, "3 · Ejemplo de redacción", "3 · Writing example"), fontWeight = FontWeight.Bold)
                    Text(narrative)
                    Button(
                        onClick = { draft = draft + narrative },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(tr(lang, "Agregar al borrador didáctico de Nota de ingreso", "Add to educational intake-note draft"))
                    }
                }
            }
        }

        if (draft.isNotEmpty()) {
            item {
                SectionCard(tr(lang, "Borrador didáctico · no se guarda", "Educational draft · not saved")) {
                    draft.forEachIndexed { index, line -> Text("${index + 1}. $line") }
                    OutlinedButton(onClick = { draft = emptyList() }, modifier = Modifier.fillMaxWidth()) {
                        Text(tr(lang, "Limpiar práctica", "Clear practice"))
                    }
                }
            }
        }

        item {
            NoticeCard(
                tr(
                    lang,
                    "Describe antes de diagnosticar: ubicación, tamaño, color, forma, superficie, bordes, consistencia y síntomas. Una lesión persistente, indurada, ulcerada sin causa clara, con crecimiento o signos de alarma requiere valoración docente/profesional y seguimiento; la app no emite diagnóstico definitivo.",
                    "Describe before diagnosing: site, size, color, shape, surface, borders, consistency and symptoms. A persistent, indurated or unexplained ulcerated lesion, growth, or warning sign requires faculty/professional assessment and follow-up; the app does not issue a definitive diagnosis."
                )
            )
        }
    }
}

@Composable
private fun InteractiveMouthMap(selectedId: String, onSelected: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4EDF8))
    ) {
        Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { point ->
                            val nx = point.x / size.width.toFloat()
                            val ny = point.y / size.height.toFloat()
                            val id = when {
                                ny < 0.15f -> "labio_superior"
                                ny > 0.86f -> "labio_inferior"
                                nx < 0.20f && ny in 0.22f..0.78f -> "carrillo_derecho"
                                nx > 0.80f && ny in 0.22f..0.78f -> "carrillo_izquierdo"
                                ny < 0.34f -> "paladar_duro"
                                ny < 0.46f -> "paladar_blando"
                                ny < 0.68f -> "lengua_dorso"
                                ny < 0.80f -> "piso_boca"
                                else -> "lengua_lateral"
                            }
                            onSelected(id)
                        }
                    }
            ) {
                val w = size.width
                val h = size.height
                drawOval(
                    color = Color(0xFFEAA0B2),
                    topLeft = Offset(w * .08f, h * .04f),
                    size = Size(w * .84f, h * .92f)
                )
                drawOval(
                    color = Color(0xFF7F334E),
                    topLeft = Offset(w * .13f, h * .12f),
                    size = Size(w * .74f, h * .76f)
                )
                drawOval(
                    color = Color(0xFFE5A49E),
                    topLeft = Offset(w * .26f, h * .17f),
                    size = Size(w * .48f, h * .25f)
                )
                drawOval(
                    color = Color(0xFFE8848F),
                    topLeft = Offset(w * .24f, h * .48f),
                    size = Size(w * .52f, h * .31f)
                )

                repeat(8) { i ->
                    val x = w * (.25f + i * .071f)
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(x, h * .35f),
                        size = Size(w * .055f, h * .075f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                    )
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(x, h * .73f),
                        size = Size(w * .055f, h * .07f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                    )
                }

                val centers = mapOf(
                    "labio_superior" to Offset(w * .50f, h * .08f),
                    "labio_inferior" to Offset(w * .50f, h * .92f),
                    "carrillo_derecho" to Offset(w * .16f, h * .52f),
                    "carrillo_izquierdo" to Offset(w * .84f, h * .52f),
                    "encia" to Offset(w * .50f, h * .40f),
                    "paladar_duro" to Offset(w * .50f, h * .24f),
                    "paladar_blando" to Offset(w * .50f, h * .40f),
                    "lengua_dorso" to Offset(w * .50f, h * .61f),
                    "lengua_lateral" to Offset(w * .68f, h * .67f),
                    "piso_boca" to Offset(w * .50f, h * .82f),
                    "orofaringe" to Offset(w * .50f, h * .44f)
                )
                centers[selectedId]?.let { center ->
                    drawCircle(Color(0xAA7B4DA8), radius = minOf(w, h) * .075f, center = center)
                    drawCircle(Color.White, radius = minOf(w, h) * .025f, center = center)
                }
            }
        }
    }
}
