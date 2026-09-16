package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.PerioRecord

private val painHistoryV40 = listOf(
    "Sin dolor", "Dolor espontáneo", "Dolor nocturno", "Frío", "Calor", "Dulces", "Chocolate",
    "Aire", "Masticación", "Al acostarse", "Dolor referido", "Dolor pulsátil", "Dolor breve",
    "Dolor persistente tras retirar estímulo"
)
private val pulpalTestsV40 = listOf(
    "Frío: respuesta normal", "Frío: respuesta aumentada breve", "Frío: dolor persistente", "Frío: sin respuesta",
    "Calor: positivo", "Prueba eléctrica: respuesta", "Prueba eléctrica: sin respuesta", "Exposición pulpar",
    "Hemorragia controlable", "Hemorragia difícil de controlar"
)
private val apicalTestsV40 = listOf(
    "Percusión vertical positiva", "Percusión horizontal positiva", "Palpación apical positiva", "Dolor a la mordida",
    "Aumento de volumen", "Fístula/tracto sinuoso", "Movilidad asociada", "Sin signos apicales"
)
private val crownImageV40 = listOf(
    "Caries superficial", "Caries profunda", "Restauración amplia", "Fractura coronaria", "Radiolucidez coronaria",
    "Radiopacidad coronaria/restauración", "Cámara pulpar amplia", "Cámara pulpar reducida/calcificada", "Sin alteraciones visibles"
)
private val rootImageV40 = listOf(
    "Espacio del ligamento periodontal normal", "Espacio del ligamento periodontal ensanchado", "Lámina dura alterada",
    "Radiolucidez periapical", "Radiopacidad periapical", "Conductos estrechos/calcificados", "Conductos amplios",
    "Reabsorción interna", "Reabsorción externa", "Ápice abierto", "Raíz incompleta", "Raíz fracturada",
    "Sin alteraciones radiculares visibles"
)

private fun deriveEndoV40(pain: List<String>, pulp: List<String>, apical: List<String>): Triple<String, String, String> {
    val pulpal = when {
        "Frío: sin respuesta" in pulp || "Prueba eléctrica: sin respuesta" in pulp -> "Necrosis pulpar, presuntiva; confirmar con pruebas concordantes"
        "Dolor espontáneo" in pain || "Dolor nocturno" in pain || "Dolor persistente tras retirar estímulo" in pain || "Frío: dolor persistente" in pulp -> "Inflamación pulpar severa compatible con pulpitis irreversible sintomática en terminología clásica"
        "Frío" in pain || "Dulces" in pain || "Chocolate" in pain || "Aire" in pain || "Frío: respuesta aumentada breve" in pulp -> "Pulpa vital inflamada compatible con pulpitis reversible"
        else -> "Pulpa normal o diagnóstico pulpar no concluyente"
    }
    val apicalDx = when {
        "Fístula/tracto sinuoso" in apical -> "Lesión apical con drenaje / absceso apical crónico, presuntivo"
        "Aumento de volumen" in apical -> "Absceso apical agudo, presuntivo; valorar urgencia y extensión"
        "Percusión vertical positiva" in apical || "Percusión horizontal positiva" in apical || "Palpación apical positiva" in apical || "Dolor a la mordida" in apical -> "Periodontitis apical sintomática, compatible"
        else -> "Tejidos apicales normales o sin signos suficientes"
    }
    val tx = when {
        pulpal.startsWith("Necrosis") -> "Si es conservable: tratamiento no vital según dentición y madurez; si el pronóstico es desfavorable, valorar extracción."
        pulpal.startsWith("Inflamación pulpar severa") -> "Valorar terapia pulpar vital contemporánea o tratamiento de conductos según vitalidad, control de hemorragia, madurez y restaurabilidad."
        pulpal.startsWith("Pulpa vital inflamada") -> "Control de caries con preservación pulpar, sellado y restauración; VPT si existe exposición e indicación."
        else -> "Prevención, restauración o vigilancia según diagnóstico definitivo y restaurabilidad."
    }
    return Triple(pulpal, apicalDx, tx)
}

@Composable
fun EndodonticTeachingV40Screen(lang: String, onBack: () -> Unit) {
    var tab by remember { mutableStateOf(0) }
    var selectedTooth by remember { mutableStateOf(16) }
    var primary by remember { mutableStateOf(false) }
    val pain = remember { mutableStateListOf<String>() }
    val pulp = remember { mutableStateListOf<String>() }
    val apical = remember { mutableStateListOf<String>() }
    val crown = remember { mutableStateListOf<String>() }
    val root = remember { mutableStateListOf<String>() }
    var visibleCanals by remember { mutableStateOf(1) }
    var canalCount by remember { mutableStateOf(1) }
    var initialLength by remember { mutableStateOf(20) }
    var workingLength by remember { mutableStateOf(19) }
    var instrumentation by remember { mutableStateOf("Manual") }
    val irrigants = remember { mutableStateListOf<String>() }
    var ending by remember { mutableStateOf("") }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val integrated = deriveEndoV40(pain, pulp, apical)

    ResponsiveScreenV17(
        tr(lang, "Ficha endodóntica · guía interactiva", "Endodontic sheet · interactive guide"),
        tr(lang, "Diagnóstico, tratamiento y ejemplos. Es un ejercicio educativo y no una receta clínica.", "Diagnosis, treatment and examples. This is an educational exercise, not a prescription."),
        onBack
    ) { profile ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            listOf("Diagnóstico", "Tratamiento", "Ejemplos").forEachIndexed { i, label ->
                FilterChip(selected = tab == i, onClick = { tab = i }, label = { Text(label) }, modifier = Modifier.weight(1f))
            }
        }
        when (tab) {
            0 -> {
                ResponsiveSectionV17("Órgano dentario del caso ficticio") {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(!primary, { primary = false }, { Text("Permanente") })
                        FilterChip(primary, { primary = true }, { Text("Temporal") })
                    }
                    DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { it == selectedTooth }
                }
                MultiSelectSectionV40("Historia del dolor · lo que refiere el paciente", painHistoryV40, pain, profile)
                MultiSelectSectionV40("Pruebas y signos pulpares", pulpalTestsV40, pulp, profile)
                MultiSelectSectionV40("Pruebas y signos periapicales", apicalTestsV40, apical, profile)
                ResponsiveSectionV17("Imagenología · corona") {
                    EndoRadiographV40(crown, root)
                    MultiSelectGridV40(crownImageV40, crown, profile)
                }
                ResponsiveSectionV17("Imagenología · raíz y periápice") {
                    MultiSelectGridV40(rootImageV40, root, profile)
                    Text("Número de conductos visibles", fontWeight = FontWeight.Black)
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        (1..5).forEach { n -> FilterChip(visibleCanals == n, { visibleCanals = n }, { Text(n.toString()) }) }
                    }
                }
                ResponsiveSectionV17("Integración diagnóstica") {
                    Text("Diagnóstico pulpar: ${integrated.first}", fontWeight = FontWeight.Black)
                    Text("Diagnóstico periapical: ${integrated.second}", fontWeight = FontWeight.Black)
                    Text("Tratamiento sugerido didácticamente: ${integrated.third}")
                    Text("Periapical y periodontal no son sinónimos. El diagnóstico periodontal se integra desde el periodontograma.", style = MaterialTheme.typography.bodySmall)
                    Card(
                        onClick = {
                            TeachingStateV40.endoResult.value = EndoTeachingResultV40(selectedTooth, integrated.first, integrated.second, integrated.third, visibleCanals)
                            TeachingStateV40.moduleSummaries["endo"] = "OD $selectedTooth · ${integrated.first} · ${integrated.second} · ${integrated.third}"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) { Text("Guardar resumen endodóntico", Modifier.padding(12.dp), fontWeight = FontWeight.Black) }
                }
            }
            1 -> {
                ResponsiveSectionV17("Tratamiento sugerido desde el diagnóstico") {
                    Text(TeachingStateV40.endoResult.value?.suggestedTreatment ?: integrated.third)
                }
                ResponsiveSectionV17("Número de conductos") {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        (1..5).forEach { n -> FilterChip(canalCount == n, { canalCount = n }, { Text(n.toString()) }) }
                    }
                }
                ResponsiveSectionV17("Longitudes") {
                    Text("Longitud inicial: estimación desde imagen preoperatoria y referencias anatómicas; todavía no es la longitud real de trabajo.")
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(16,18,20,22,24,26).forEach { n -> FilterChip(initialLength == n, { initialLength = n }, { Text("$n mm") }) }
                    }
                    Text("Longitud real de trabajo: confirmar con localizador apical y/o imagen de conductometría según protocolo, anatomía y caso.")
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(15,17,19,21,23,25).forEach { n -> FilterChip(workingLength == n, { workingLength = n }, { Text("$n mm") }) }
                    }
                }
                ResponsiveSectionV17("Instrumentación") {
                    listOf("Manual", "Rotatoria NiTi", "Reciprocante NiTi", "Híbrida").forEach { item ->
                        FilterChip(instrumentation == item, { instrumentation = item }, { Text(item) }, Modifier.fillMaxWidth())
                    }
                    NoticeCard("La secuencia exacta depende del sistema comercial, anatomía, curvatura y protocolo docente/fabricante; no existe una secuencia universal para todos los casos.")
                }
                ResponsiveSectionV17("Irrigación · materiales a reconocer") {
                    MultiSelectGridV40(
                        listOf(
                            "Hipoclorito de sodio · antimicrobiano y disolvente tisular; evitar extrusión",
                            "EDTA · quelante complementario para componente inorgánico/smear layer",
                            "Solución salina · arrastre sin capacidad equivalente a NaOCl",
                            "Clorhexidina · uso seleccionado; no mezclar directamente con NaOCl",
                            "Agua estéril/destilada · enjuague según protocolo"
                        ), irrigants, profile
                    )
                    NoticeCard("Concentración, volumen, activación y secuencia dependen del caso, edad, ápice, seguridad y protocolo clínico.")
                }
                ResponsiveSectionV17("Cómo concluye el tratamiento") {
                    val options = if (primary) listOf(
                        "Temporal vital · VPT/restauración según diagnóstico",
                        "Temporal no vital · pulpectomía con material apropiado/reabsorbible cuando está indicada",
                        "LSTR en casos seleccionados y con seguimiento",
                        "Extracción cuando no es conservable"
                    ) else listOf(
                        "Permanente maduro · obturación del sistema y sellado coronal",
                        "Permanente inmaduro vital · preservar vitalidad/apexogénesis",
                        "Permanente inmaduro necrótico · regeneración/apexificación según caso",
                        "Restauración definitiva y seguimiento"
                    )
                    options.forEach { item -> FilterChip(ending == item, { ending = item }, { Text(item) }, Modifier.fillMaxWidth()) }
                }
            }
            else -> {
                val examples = listOf(
                    "IPT / remoción selectiva" to "Preservación pulpar en caries profunda compatible, con buen sellado.",
                    "Pulpotomía con silicato de calcio" to "MTA/Biodentine u otro biocerámico según indicación, diagnóstico y control de hemorragia.",
                    "Pulpectomía temporal" to "Diente temporal no vital y restaurable en indicaciones seleccionadas.",
                    "CTZ / LSTR" to "Técnica regional seleccionada; no equivale a endodoncia convencional ni es primera elección universal.",
                    "Pasta con yodoformo" to "Material presente en algunos protocolos/productos para dentición temporal.",
                    "SDF" to "No es tratamiento pulpar: es una intervención para control/arresto de caries en lesiones seleccionadas.",
                    "Técnica Hall" to "No es tratamiento pulpar: sella caries de molares temporales seleccionados bajo corona preformada.",
                    "Terapia pulpar vital permanente" to "IPT, recubrimiento o pulpotomía según diagnóstico, vitalidad y restaurabilidad.",
                    "Regeneración endodóntica" to "Permanente inmaduro necrótico en casos seleccionados y con protocolo especializado.",
                    "Apexificación" to "Permanente inmaduro necrótico cuando se busca una barrera apical; distinta de apexogénesis."
                )
                examples.forEach { (title, detail) -> TeachingCardV40(title, detail) }
                NoticeCard("La evidencia y las guías cambian. La técnica clínica concreta debe seguir guías vigentes, fabricante, normativa local y supervisión docente.")
            }
        }
    }
}

@Composable
private fun MultiSelectSectionV40(title: String, options: List<String>, selected: MutableList<String>, profile: ScreenProfileV17) {
    ResponsiveSectionV17(title) { MultiSelectGridV40(options, selected, profile) }
}

@Composable
private fun MultiSelectGridV40(options: List<String>, selected: MutableList<String>, profile: ScreenProfileV17) {
    AdaptiveGridV17(options.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
        val item = options[i]
        FilterChip(item in selected, { toggleV40(selected, item) }, { Text(item) }, Modifier.fillMaxWidth())
    }
}

@Composable
private fun EndoRadiographV40(crown: List<String>, root: List<String>) {
    Canvas(Modifier.fillMaxWidth().height(230.dp).padding(8.dp)) {
        val w = size.width; val h = size.height; val cx = w / 2f
        drawRoundRect(Color(0xFF25272A), size = size)
        val tooth = Path().apply {
            moveTo(cx-w*.13f,h*.12f); cubicTo(cx-w*.20f,h*.18f,cx-w*.18f,h*.34f,cx-w*.10f,h*.40f)
            cubicTo(cx-w*.08f,h*.55f,cx-w*.07f,h*.77f,cx-w*.03f,h*.87f)
            quadraticBezierTo(cx,h*.94f,cx+w*.03f,h*.87f)
            cubicTo(cx+w*.07f,h*.77f,cx+w*.08f,h*.55f,cx+w*.10f,h*.40f)
            cubicTo(cx+w*.18f,h*.34f,cx+w*.20f,h*.18f,cx+w*.13f,h*.12f)
            quadraticBezierTo(cx,h*.06f,cx-w*.13f,h*.12f); close()
        }
        drawPath(tooth, Color(0xFFE8EAEC)); drawPath(tooth, Color(0xFFA0A3A8), style = Stroke(3f))
        drawLine(Color(0xFF5B5D62), Offset(cx,h*.22f), Offset(cx,h*.84f), strokeWidth = if ("Conductos estrechos/calcificados" in root) 3f else 8f)
        if ("Cámara pulpar reducida/calcificada" !in crown) drawOval(Color(0xFF66686D), Offset(cx-w*.055f,h*.23f), Size(w*.11f,h*.16f))
        if ("Radiolucidez periapical" in root) drawCircle(Color(0xFF08090A), w*.09f, Offset(cx,h*.91f))
        if ("Radiopacidad periapical" in root) drawCircle(Color.White, w*.09f, Offset(cx,h*.91f), style = Stroke(7f))
        if ("Caries profunda" in crown) drawCircle(Color(0xFF0C0D0E), w*.045f, Offset(cx-w*.08f,h*.20f))
    }
}

private data class SurgicalProtocolV40(val name: String, val diagnosis: String, val overview: String)
private val surgicalProtocolsV40 = listOf(
    SurgicalProtocolV40("Extracción de resto radicular", "Confirmar fragmento, restaurabilidad, relación anatómica e imagen cuando esté indicada.", "Planear anestesia y acceso; luxación/elevación o abordaje quirúrgico supervisado, verificación, hemostasia e indicaciones."),
    SurgicalProtocolV40("Tercer molar", "Evaluar síntomas, pericoronitis/caries, espacio, raíces y relación con estructuras.", "Planificar acceso según posición; colgajo, osteotomía u odontosección sólo si están indicados y dentro de competencia supervisada."),
    SurgicalProtocolV40("Biopsia", "Lesión persistente o incierta que requiere tejido para estudio histopatológico.", "Elegir tipo de biopsia, sitio representativo, manejo atraumático, fijación, rotulado y solicitud anatomopatológica."),
    SurgicalProtocolV40("Curetaje abierto / acceso periodontal", "Defecto periodontal seleccionado tras diagnóstico y fase etiológica.", "Acceso mediante colgajo, debridamiento y manejo de superficies bajo supervisión; reposición/sutura y mantenimiento."),
    SurgicalProtocolV40("Regularización de proceso alveolar", "Irregularidad ósea que interfiere con cicatrización, prótesis o cierre.", "Exposición controlada, regularización conservadora, irrigación y cierre sin tensión."),
    SurgicalProtocolV40("Extracciones múltiples", "Varios dientes con indicación dentro de un plan integral.", "Planificar secuencia, preservar tejido, controlar alvéolos/bordes, hemostasia y seguimiento."),
    SurgicalProtocolV40("Alargamiento de corona", "Necesidad restauradora/periodontal con evaluación del ancho supracrestal y arquitectura ósea.", "Plan periodontal-restaurador; reposicionamiento apical y/o manejo óseo cuando esté indicado."),
    SurgicalProtocolV40("Frenectomía/frenotomía", "Frenillo con indicación funcional, periodontal, protésica u ortodóncica sustentada.", "Liberación controlada y cierre según técnica; evitar indicación sólo por apariencia."),
    SurgicalProtocolV40("Drenaje de absceso localizado", "Colección fluctuante odontógena y diagnóstico del origen.", "Cuando está indicado: drenaje y control del foco; signos de diseminación o sistémicos requieren escalamiento."),
    SurgicalProtocolV40("Operculectomía", "Opérculo con inflamación recurrente en caso seleccionado tras valorar erupción y posición.", "Remoción conservadora del tejido indicado y control de factores locales."),
    SurgicalProtocolV40("Sutura de herida intraoral menor", "Herida que requiere aproximación y hemostasia.", "Valorar profundidad/contaminación, limpiar y aproximar sin tensión con técnica apropiada."),
    SurgicalProtocolV40("Exposición quirúrgica de retenido", "Diente retenido con plan ortodóncico-quirúrgico coordinado.", "Localización tridimensional y acceso conservador según plan interdisciplinario.")
)

@Composable
fun SurgicalTeachingV40Screen(lang: String, onBack: () -> Unit) {
    val symptoms = remember { mutableStateListOf<String>() }
    var selected by remember { mutableStateOf(0) }
    var winter by remember { mutableStateOf("Vertical") }
    ResponsiveScreenV17("Ficha quirúrgica · guía de pregrado", "Anamnesis, clasificación y protocolos educativos bajo supervisión.", onBack) { profile ->
        MultiSelectSectionV40("Anamnesis quirúrgica · historia del dolor", painHistoryV40 + listOf("Edema facial","Fiebre referida","Trismus","Supuración","Sangrado","Trauma reciente"), symptoms, profile)
        ResponsiveSectionV17("Tercer molar · angulación de Winter") {
            ThirdMolarDiagramV40(winter)
            listOf("Vertical","Mesioangular","Distoangular","Horizontal","Bucoangular","Linguoangular","Invertido/atípico").forEach { item ->
                FilterChip(winter == item, { winter = item }, { Text(item) }, Modifier.fillMaxWidth())
            }
            Text("Winter describe la angulación. La dificultad también depende de profundidad, espacio/ramus, raíces y relación con estructuras vecinas.")
        }
        ResponsiveSectionV17("Tipos de biopsia") {
            listOf(
                "Excisional" to "Retira toda una lesión pequeña cuando es apropiado.",
                "Incisional" to "Obtiene una porción representativa de lesión grande o sospechosa.",
                "Punch" to "Obtiene un cilindro de tejido en sitios seleccionados.",
                "Aspiración / PAAF" to "Obtiene contenido/células; no sustituye histopatología cuando se necesita arquitectura.",
                "Citología/cepillado" to "Auxiliar de valoración; no reemplaza automáticamente una biopsia indicada."
            ).forEach { (t,d) -> TeachingCardV40(t,d) }
        }
        ResponsiveSectionV17("Protocolos didácticos") {
            surgicalProtocolsV40.forEachIndexed { i, p ->
                Card(
                    onClick = { selected = i },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = if (selected == i) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(p.name, fontWeight = FontWeight.Black)
                        if (selected == i) { Text("Cómo se indica/diagnostica: ${p.diagnosis}"); Text("Secuencia educativa general: ${p.overview}") }
                    }
                }
            }
        }
        val chosen = surgicalProtocolsV40[selected]
        Card(
            onClick = { TeachingStateV40.moduleSummaries["surgical"] = "${chosen.name} · ${chosen.diagnosis}" },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) { Text("Guardar resumen quirúrgico", Modifier.padding(12.dp), fontWeight = FontWeight.Black) }
        NoticeCard("Los pasos son un mapa de aprendizaje, no instrucciones para realizar cirugía sin supervisión. El alcance de pregrado depende de institución, competencia, caso y docente responsable.")
    }
}

@Composable
private fun ThirdMolarDiagramV40(winter: String) {
    val tilt = when (winter) { "Mesioangular" -> -0.10f; "Distoangular" -> 0.10f; "Horizontal" -> -0.22f; else -> 0f }
    Canvas(Modifier.fillMaxWidth().height(175.dp)) {
        val w = size.width; val h = size.height
        fun tooth(cx: Float, baseY: Float, lean: Float) {
            val p = Path().apply {
                moveTo(cx-w*.055f+lean*w, baseY-h*.18f); quadraticBezierTo(cx+lean*w, baseY-h*.24f, cx+w*.055f+lean*w, baseY-h*.18f)
                lineTo(cx+w*.04f, baseY); lineTo(cx+w*.02f, baseY+h*.25f); lineTo(cx, baseY+h*.13f); lineTo(cx-w*.02f, baseY+h*.25f); lineTo(cx-w*.04f, baseY); close()
            }
            drawPath(p, Color(0xFFFFF2D7)); drawPath(p, Color(0xFF806F5A), style = Stroke(2.5f))
        }
        tooth(w*.38f, h*.48f, 0f); tooth(w*.65f, h*.48f, tilt)
        drawLine(Color(0xFFD9888F), Offset(w*.12f,h*.52f), Offset(w*.90f,h*.52f), strokeWidth = 7f)
    }
}

@Composable
fun PeriodontalTeachingV40Screen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    var showChart by remember { mutableStateOf(false) }
    ResponsiveScreenV17("Ficha periodontal · guía", "Movilidad, furcación y periodontograma interactivo.", onBack) { _ ->
        ResponsiveSectionV17("Movilidad dentaria") {
            listOf(
                "Grado 0" to "Movilidad fisiológica dentro de límites normales.",
                "Grado 1" to "Movilidad horizontal leve, aproximadamente hasta 1 mm.",
                "Grado 2" to "Movilidad horizontal mayor de 1 mm, sin componente vertical franco.",
                "Grado 3" to "Movilidad horizontal marcada y componente vertical/depresible."
            ).forEach { (t,d) -> TeachingCardV40(t,d) }
        }
        ResponsiveSectionV17("Furcación · guía horizontal") {
            listOf(
                "Grado 0" to "No se detecta compromiso de furcación.",
                "Grado I" to "Compromiso horizontal incipiente; entrada de furcación sin atravesarla.",
                "Grado II" to "Compromiso horizontal parcial; no existe paso completo de lado a lado.",
                "Grado III" to "Compromiso horizontal de lado a lado; puede estar cubierto por tejido gingival."
            ).forEach { (t,d) -> TeachingCardV40(t,d) }
        }
        Card(
            onClick = { showChart = !showChart },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) { Text(if (showChart) "Ocultar periodontograma" else "Abrir periodontograma", Modifier.padding(14.dp), fontWeight = FontWeight.Black) }
        if (showChart) PeriodontogramEmbeddedV40(session, onSessionChanged)
    }
}

@Composable
private fun PeriodontogramEmbeddedV40(session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit) {
    var tooth by remember { mutableStateOf(16) }
    val record = session.periodontogram[tooth] ?: PerioRecord()
    fun update(r: PerioRecord) { onSessionChanged(session.copy(periodontogram = session.periodontogram + (tooth to r))) }
    ResponsiveSectionV17("Ejercicio por órgano dentario") {
        DentalArchSelector(ClinicalContent.permanentTeeth, tooth, { tooth = it }) { it in session.periodontogram }
        Text("Profundidad de sondaje · seis sitios. Selecciona un valor por sitio.", fontWeight = FontWeight.Black)
        listOf("MV","V","DV","ML","L/P","DL").forEachIndexed { idx, site ->
            Text(site, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(0,2,3,4,5,6,8,10).forEach { mm ->
                    FilterChip(record.probingDepths[idx] == mm, {
                        val values = record.probingDepths.toMutableList(); values[idx] = mm; update(record.copy(probingDepths = values))
                    }, { Text("$mm") })
                }
            }
        }
        Text("Movilidad")
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) { (0..3).forEach { g -> FilterChip(record.mobility == g, { update(record.copy(mobility = g)) }, { Text(g.toString()) }) } }
        Text("Furcación")
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) { (0..3).forEach { g -> FilterChip(record.furcation == g, { update(record.copy(furcation = g)) }, { Text(g.toString()) }) } }
        BooleanRow("Sangrado al sondaje", record.bleeding) { update(record.copy(bleeding = it)) }
        BooleanRow("Placa", record.plaque) { update(record.copy(plaque = it)) }
        BooleanRow("Supuración", record.suppuration) { update(record.copy(suppuration = it)) }
        Card(
            onClick = { TeachingStateV40.moduleSummaries["periodontal"] = "OD $tooth · movilidad ${record.mobility} · furcación ${record.furcation} · sangrado ${if (record.bleeding) "sí" else "no"}" },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) { Text("Guardar resumen periodontal", Modifier.padding(12.dp), fontWeight = FontWeight.Black) }
    }
}

@Composable
private fun TeachingCardV40(title: String, detail: String) {
    var open by remember { mutableStateOf(false) }
    Card(
        onClick = { open = !open },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (open) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, fontWeight = FontWeight.Black)
            if (open) Text(detail) else Text("Toca para explicación", style = MaterialTheme.typography.bodySmall)
        }
    }
}
