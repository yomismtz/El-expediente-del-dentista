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
import androidx.compose.material3.OutlinedTextField
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

private fun toggleV40(list: MutableList<String>, item: String) {
    if (item in list) list.remove(item) else list.add(item)
}

private val painHistoryV40 = listOf(
    "Sin dolor", "Dolor espontáneo", "Dolor nocturno", "Frío", "Calor", "Dulces", "Chocolate",
    "Aire", "Masticación", "Al acostarse", "Dolor referido", "Dolor pulsátil", "Dolor breve", "Dolor persistente tras retirar estímulo"
)
private val pulpalTestsV40 = listOf(
    "Frío: respuesta normal", "Frío: respuesta aumentada breve", "Frío: dolor persistente", "Frío: sin respuesta",
    "Calor: positivo", "Prueba eléctrica: respuesta", "Prueba eléctrica: sin respuesta", "Exposición pulpar", "Hemorragia controlable", "Hemorragia difícil de controlar"
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
    "Reabsorción interna", "Reabsorción externa", "Ápice abierto", "Raíz incompleta", "Raíz fracturada", "Sin alteraciones radiculares visibles"
)

private fun deriveEndoV40(pain: List<String>, pulp: List<String>, apical: List<String>): Triple<String, String, String> {
    val pulpal = when {
        "Frío: sin respuesta" in pulp || "Prueba eléctrica: sin respuesta" in pulp -> "Necrosis pulpar (presuntiva; confirmar con pruebas concordantes)"
        "Dolor espontáneo" in pain || "Dolor nocturno" in pain || "Dolor persistente tras retirar estímulo" in pain || "Frío: dolor persistente" in pulp -> "Pulpitis irreversible sintomática · terminología clásica AAE; integrar diagnóstico contemporáneo de vitalidad"
        "Frío" in pain || "Dulces" in pain || "Chocolate" in pain || "Aire" in pain || "Frío: respuesta aumentada breve" in pulp -> "Pulpitis reversible / pulpa vital inflamada compatible"
        else -> "Pulpa normal o diagnóstico pulpar no concluyente"
    }
    val apicalDx = when {
        "Fístula/tracto sinuoso" in apical -> "Absceso apical crónico / lesión apical con drenaje, presuntivo"
        "Aumento de volumen" in apical -> "Absceso apical agudo, presuntivo; valorar urgencia y extensión"
        "Percusión vertical positiva" in apical || "Percusión horizontal positiva" in apical || "Palpación apical positiva" in apical || "Dolor a la mordida" in apical -> "Periodontitis apical sintomática, compatible"
        else -> "Tejidos apicales normales o sin signos clínicos suficientes"
    }
    val tx = when {
        pulpal.startsWith("Necrosis") -> "Si el diente es conservable: tratamiento no vital según dentición y madurez radicular; si el pronóstico es desfavorable, valorar extracción."
        pulpal.startsWith("Pulpitis irreversible") -> "Valorar terapia pulpar vital contemporánea o tratamiento de conductos según vitalidad, control de hemorragia, madurez y restaurabilidad."
        pulpal.startsWith("Pulpitis reversible") -> "Control de caries con preservación pulpar: remoción selectiva/IPT y restauración; VPT si existe exposición e indicación."
        else -> "Prevención, restauración o vigilancia según el diagnóstico definitivo y la restaurabilidad."
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
    var instrumentation by remember { mutableStateOf("Manual NiTi/acero inoxidable según caso") }
    val irrigants = remember { mutableStateListOf<String>() }
    var obturation by remember { mutableStateOf("Concluir según dentición y diagnóstico") }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val (pulpDx, apicalDx, suggested) = deriveEndoV40(pain, pulp, apical)

    ResponsiveScreenV17(
        tr(lang, "Ficha endodóntica · guía interactiva", "Endodontic sheet · interactive guide"),
        tr(lang, "Tres rubros: diagnóstico, tratamiento y ejemplos contemporáneos. Es un simulador educativo, no una receta.", "Three areas: diagnosis, treatment and contemporary examples. This is a teaching simulator, not a prescription."),
        onBack
    ) { profile ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("1 · Diagnóstico", "2 · Tratamiento", "3 · Ejemplos").forEachIndexed { i, label ->
                FilterChip(tab == i, { tab = i }, { Text(label) }, Modifier.weight(1f))
            }
        }

        if (tab == 0) {
            ResponsiveSectionV17(tr(lang, "Órgano dentario del caso ficticio", "Tooth in the fictional case")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(!primary, { primary = false }, { Text("Permanente") })
                    FilterChip(primary, { primary = true }, { Text("Temporal") })
                }
                DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { it == selectedTooth }
            }
            SelectableTeachingSectionV40("Historia del dolor · lo que refiere el paciente", painHistoryV40, pain, profile)
            SelectableTeachingSectionV40("Pruebas y signos pulpares", pulpalTestsV40, pulp, profile)
            SelectableTeachingSectionV40("Pruebas y signos periapicales", apicalTestsV40, apical, profile)
            ResponsiveSectionV17("Diagnóstico de imagenología · corona") {
                ToothRadiographIllustrationV40(crown, root)
                MultiChipGridV40(crownImageV40, crown, profile)
            }
            ResponsiveSectionV17("Diagnóstico de imagenología · raíz y periápice") {
                MultiChipGridV40(rootImageV40, root, profile)
                Text("Número de conductos visibles", fontWeight = FontWeight.Black)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (1..5).forEach { n -> FilterChip(visibleCanals == n, { visibleCanals = n }, { Text(n.toString()) }) }
                }
            }
            ResponsiveSectionV17("Integración diagnóstica") {
                Text("🧠 Diagnóstico pulpar: $pulpDx", fontWeight = FontWeight.Black)
                Text("🦴 Diagnóstico periapical: $apicalDx", fontWeight = FontWeight.Black)
                Text("🛠 Tratamiento sugerido didácticamente: $suggested")
                Text("Nota: periapical no es lo mismo que periodontal. El periodontograma integra el diagnóstico periodontal.", style = MaterialTheme.typography.bodySmall)
                Card(
                    onClick = {
                        TeachingStateV40.endoResult.value = EndoTeachingResultV40(selectedTooth, pulpDx, apicalDx, suggested, visibleCanals)
                        TeachingStateV40.moduleSummaries["endo"] = "OD $selectedTooth · $pulpDx · $apicalDx · $suggested"
                    },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar resumen educativo", Modifier.padding(14.dp), fontWeight = FontWeight.Black) }
            }
        }

        if (tab == 1) {
            val saved = TeachingStateV40.endoResult.value
            ResponsiveSectionV17("Tratamiento sugerido desde el diagnóstico") {
                Text(saved?.let { "OD ${it.tooth} · ${it.suggestedTreatment}" } ?: suggested)
            }
            ResponsiveSectionV17("Conductos y longitudes") {
                Text("Número de conductos a trabajar en el ejercicio")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (1..5).forEach { n -> FilterChip(canalCount == n, { canalCount = n }, { Text(n.toString()) }) }
                }
                Text("Longitud inicial estimada: se obtiene de imagen preoperatoria y referencias anatómicas; NO es todavía la longitud real de trabajo.")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { (16..26 step 2).forEach { n -> FilterChip(initialLength == n, { initialLength = n }, { Text("$n mm") }) } }
                Text("Longitud real de trabajo: se confirma con localizador apical y/o imagen de conductometría según protocolo y situación clínica.")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { (15..25 step 2).forEach { n -> FilterChip(workingLength == n, { workingLength = n }, { Text("$n mm") }) } }
            }
            ResponsiveSectionV17("Instrumentación") {
                listOf("Manual NiTi/acero inoxidable según caso", "Rotatoria NiTi", "Reciprocante NiTi", "Técnica híbrida").forEach { item ->
                    FilterChip(instrumentation == item, { instrumentation = item }, { Text(item) }, Modifier.fillMaxWidth())
                }
                NoticeCard("La secuencia exacta depende del sistema comercial, anatomía, permeabilidad, curvatura y protocolo docente/fabricante; no se fija una secuencia universal en la app.")
            }
            ResponsiveSectionV17("Irrigación · materiales que el estudiante debe reconocer") {
                val options = listOf(
                    "Hipoclorito de sodio (NaOCl) · principal antimicrobiano/disolvente tisular; evitar extrusión apical",
                    "EDTA · quelante para componente inorgánico/smear layer, uso complementario",
                    "Solución salina · irrigante/arrastre sin capacidad equivalente a NaOCl",
                    "Clorhexidina · antimicrobiana en situaciones seleccionadas; NO mezclar directamente con NaOCl",
                    "Agua destilada/estéril · enjuague según protocolo"
                )
                MultiChipGridV40(options, irrigants, profile)
                NoticeCard("La concentración, volumen, activación y secuencia son decisiones clínicas y del protocolo docente. Deben considerarse seguridad, ápice abierto, dentición y fabricante.")
            }
            ResponsiveSectionV17("Cómo concluye el tratamiento") {
                val endings = if (primary) listOf(
                    "Diente temporal vital · VPT/restauración según diagnóstico",
                    "Diente temporal no vital · pulpectomía con material reabsorbible cuando está indicada",
                    "LSTR/lesion sterilization-tissue repair en indicaciones seleccionadas y seguimiento",
                    "Extracción cuando el diente no es conservable o el pronóstico lo indica"
                ) else listOf(
                    "Permanente maduro · obturación tridimensional y sellado coronal",
                    "Permanente inmaduro vital · preservar vitalidad/apexogénesis cuando sea posible",
                    "Permanente inmaduro necrótico · valorar regeneración/apexificación según caso",
                    "Restauración definitiva y seguimiento clínico-radiográfico"
                )
                endings.forEach { item -> FilterChip(obturation == item, { obturation = item }, { Text(item) }, Modifier.fillMaxWidth()) }
            }
        }

        if (tab == 2) {
            val examples = listOf(
                Triple("IPT / remoción selectiva", "Primarios y permanentes vitales con caries profunda y diagnóstico compatible.", "Preserva dentina afectada profunda para disminuir riesgo de exposición; requiere sellado adecuado."),
                Triple("Pulpotomía con cemento de silicato de calcio", "Dientes vitales seleccionados; MTA/Biodentine u otros biocerámicos según protocolo.", "La indicación depende de diagnóstico, control de hemorragia, restaurabilidad y madurez."),
                Triple("Pulpectomía en diente temporal", "Diente temporal no vital/restaurable cuando está indicada.", "Materiales de obturación deben ser apropiados para dentición temporal y reabsorción fisiológica."),
                Triple("CTZ / LSTR", "Técnica regional para situaciones seleccionadas en dentición temporal, no equivalente a la endodoncia convencional ni primera elección universal.", "Revisar protocolo institucional, antibióticos tópicos implicados, indicaciones y seguimiento."),
                Triple("Pasta a base de yodoformo", "Material de obturación/relleno usado en algunas pulpectomías temporales según producto/protocolo.", "No confundir con yodo tópico general; valorar alergias y características del material."),
                Triple("SDF · fluoruro diamino de plata", "NO es tratamiento pulpar. Es una intervención de control/arresto de caries en lesiones seleccionadas.", "Puede oscurecer la lesión tratada y requiere consentimiento/instrucción; no resuelve enfermedad pulpar irreversible."),
                Triple("Técnica de Hall", "NO es tratamiento pulpar. Es una técnica mínimamente invasiva para molares temporales seleccionados, sellando caries bajo corona preformada.", "Requiere criterios de selección y ausencia de signos de patología pulpar incompatible."),
                Triple("Terapia pulpar vital en permanente", "IPT, recubrimiento directo, pulpotomía parcial/completa en escenarios contemporáneos.", "La guía moderna enfatiza vitalidad, control de hemorragia, biocerámicos y restauración sellada."),
                Triple("Regeneración endodóntica", "Permanente inmaduro necrótico en casos seleccionados.", "Objetivo biológico distinto de una obturación convencional; requiere protocolo y seguimiento especializado."),
                Triple("Apexificación", "Permanente inmaduro necrótico cuando se busca barrera apical.", "Debe diferenciarse de apexogénesis, que depende de pulpa vital.")
            )
            examples.forEach { (title, indication, note) ->
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha=.35f))) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(title, fontWeight = FontWeight.Black)
                        Text(indication)
                        Text(note, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            NoticeCard("Las recomendaciones cambian con nueva evidencia. La app presenta conceptos educativos; la técnica clínica concreta debe seguir guías vigentes, fabricante, normativa local y supervisión docente.")
        }
    }
}

@Composable
private fun SelectableTeachingSectionV40(title: String, options: List<String>, selected: MutableList<String>, profile: ScreenProfileV17) {
    ResponsiveSectionV17(title) { MultiChipGridV40(options, selected, profile) }
}

@Composable
private fun MultiChipGridV40(options: List<String>, selected: MutableList<String>, profile: ScreenProfileV17) {
    AdaptiveGridV17(options.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
        val item = options[i]
        FilterChip(item in selected, { toggleV40(selected, item) }, { Text(item) }, Modifier.fillMaxWidth())
    }
}

@Composable
private fun ToothRadiographIllustrationV40(crown: List<String>, root: List<String>) {
    val outline = MaterialTheme.colorScheme.outline
    Canvas(Modifier.fillMaxWidth().height(230.dp).padding(8.dp)) {
        drawRoundRect(Color(0xFF27292C), size = size)
        val w = size.width; val h = size.height; val cx = w/2
        val tooth = Path().apply {
            moveTo(cx-w*.13f,h*.12f); cubicTo(cx-w*.20f,h*.18f,cx-w*.18f,h*.34f,cx-w*.10f,h*.40f)
            cubicTo(cx-w*.08f,h*.55f,cx-w*.07f,h*.77f,cx-w*.03f,h*.87f)
            quadraticBezierTo(cx,h*.94f,cx+w*.03f,h*.87f)
            cubicTo(cx+w*.07f,h*.77f,cx+w*.08f,h*.55f,cx+w*.10f,h*.40f)
            cubicTo(cx+w*.18f,h*.34f,cx+w*.20f,h*.18f,cx+w*.13f,h*.12f)
            quadraticBezierTo(cx,h*.06f,cx-w*.13f,h*.12f); close()
        }
        drawPath(tooth, Color(0xFFE7E8EA)); drawPath(tooth, Color(0xFFA0A3A8), style=Stroke(3f))
        val canalWidth = if ("Conductos estrechos/calcificados" in root) 3f else 8f
        drawLine(Color(0xFF5B5D62), Offset(cx,h*.22f), Offset(cx,h*.84f), strokeWidth=canalWidth)
        if ("Cámara pulpar reducida/calcificada" !in crown) drawOval(Color(0xFF66686D), Offset(cx-w*.055f,h*.23f), Size(w*.11f,h*.16f))
        if ("Radiolucidez periapical" in root) drawCircle(Color(0xFF090A0B), w*.09f, Offset(cx,h*.91f))
        if ("Radiopacidad periapical" in root) drawCircle(Color(0xFFF2F2F2), w*.09f, Offset(cx,h*.91f), style=Stroke(7f))
        if ("Espacio del ligamento periodontal ensanchado" in root) drawPath(tooth, Color(0xFF101112), style=Stroke(8f))
        if ("Caries profunda" in crown) drawCircle(Color(0xFF0C0D0E), w*.045f, Offset(cx-w*.08f,h*.20f))
    }
}

private data class SurgicalProtocolV40(val name:String, val diagnosis:String, val overview:String, val key:String)
private val surgicalProtocolsV40 = listOf(
    SurgicalProtocolV40("Extracción de resto radicular", "Confirmar fragmento, restaurabilidad, relación anatómica e imagen cuando esté indicada.", "Planear anestesia y acceso; realizar luxación/elevación o abordaje quirúrgico supervisado, verificar integridad, irrigar cuando proceda, hemostasia e indicaciones.", "resto"),
    SurgicalProtocolV40("Tercer molar", "Evaluar síntomas, caries/pericoronitis, espacio, relación con estructuras y clasificación radiográfica.", "Plan de acceso según posición; colgajo/osteotomía/odontosección sólo si están indicados y dentro del nivel de competencia supervisado; cierre y control.", "third"),
    SurgicalProtocolV40("Biopsia", "Lesión persistente o de diagnóstico incierto que requiere tejido para estudio histopatológico.", "Seleccionar tipo de biopsia, sitio representativo, manejo atraumático de la muestra, fijación/rotulado y solicitud anatomopatológica.", "biopsy"),
    SurgicalProtocolV40("Curetaje abierto / acceso periodontal", "Defectos periodontales seleccionados tras diagnóstico periodontal y fase etiológica.", "Acceso mediante colgajo periodontal, debridamiento y manejo de superficies bajo supervisión; reposición/sutura y mantenimiento.", "curettage"),
    SurgicalProtocolV40("Regularización de proceso alveolar / alveoloplastia", "Irregularidades óseas que interfieren con cicatrización, prótesis o cierre adecuado.", "Exposición controlada, regularización conservadora, irrigación, palpación del contorno y cierre sin tensión.", "alveolo"),
    SurgicalProtocolV40("Extracciones múltiples", "Dientes con indicación de exodoncia dentro de un plan integral; valorar secuencia y futura rehabilitación.", "Planificar cuadrantes/secuencia, preservar tejido, controlar alvéolos y bordes, hemostasia y seguimiento.", "multiple"),
    SurgicalProtocolV40("Alargamiento de corona", "Necesidad restauradora/periodontal con evaluación del ancho supracrestal y arquitectura ósea.", "Plan periodontal-restaurador; reposicionamiento apical y/o manejo óseo cuando esté indicado, respetando tejidos y cicatrización.", "crown"),
    SurgicalProtocolV40("Frenectomía/frenotomía", "Frenillo con indicación funcional, periodontal, protésica u ortodóncica sustentada.", "Liberación controlada de fibras y cierre según técnica; evitar indicaciones basadas sólo en apariencia.", "frenum"),
    SurgicalProtocolV40("Drenaje de absceso odontógeno localizado", "Colección fluctuante y diagnóstico de infección odontógena; identificar origen y signos de diseminación.", "Cuando está indicado y dentro de competencia: drenaje y control del foco; urgencias con diseminación/síntomas sistémicos requieren escalamiento.", "drain"),
    SurgicalProtocolV40("Operculectomía", "Opérculo con inflamación recurrente en caso seleccionado, tras evaluar erupción y posición dental.", "Remoción conservadora del tejido indicado bajo control; tratar también factores locales y dar seguimiento.", "operculum"),
    SurgicalProtocolV40("Sutura de herida intraoral menor", "Herida traumática/quirúrgica que requiere aproximación y hemostasia.", "Evaluar profundidad, contaminación y estructuras; limpiar, aproximar sin tensión y elegir material/técnica apropiada.", "suture"),
    SurgicalProtocolV40("Exposición quirúrgica de diente retenido", "Diente retenido con plan ortodóncico-quirúrgico coordinado.", "Localización tridimensional, acceso conservador y técnica abierta/cerrada según plan interdisciplinario.", "exposure")
)

@Composable
fun SurgicalTeachingV40Screen(lang:String, onBack:()->Unit) {
    var selected by remember { mutableStateOf(0) }
    val symptoms = remember { mutableStateListOf<String>() }
    ResponsiveScreenV17("Ficha quirúrgica · guía de pregrado", "Anamnesis, clasificación y protocolos educativos bajo supervisión.", onBack) { profile ->
        ResponsiveSectionV17("Anamnesis quirúrgica · historia del dolor") {
            MultiChipGridV40(painHistoryV40 + listOf("Edema facial", "Fiebre referida", "Trismus", "Supuración", "Sangrado", "Trauma reciente"), symptoms, profile)
            NoticeCard("Antes de cualquier procedimiento se integran antecedentes sistémicos, alergias, medicamentos/anticoagulantes, embarazo cuando aplique, signos vitales, imagen indicada y consentimiento.")
        }
        ResponsiveSectionV17("Tercer molar · clasificación angular de Winter") {
            ThirdMolarAngulationV40()
            listOf("Vertical", "Mesioangular", "Distoangular", "Horizontal", "Bucoangular", "Linguoangular", "Invertido / atípico").forEach { item ->
                FilterChip(false, {}, { Text(item) })
            }
            Text("La angulación describe el eje del tercer molar respecto al segundo molar; la dificultad no depende sólo de Winter. Debe integrarse profundidad, rama/espacio, raíces y estructuras vecinas.")
        }
        ResponsiveSectionV17("Tipos de biopsia") {
            listOf(
                "Excisional · se retira toda una lesión pequeña cuando es apropiado.",
                "Incisional · se toma una porción representativa de lesión grande o sospechosa.",
                "Punch · cilindro de tejido en sitios seleccionados.",
                "Aspiración / PAAF · obtiene contenido/células; no sustituye histopatología cuando se requiere arquitectura.",
                "Citología exfoliativa/cepillado · auxiliar de cribado/valoración, no reemplazo automático de biopsia."
            ).forEach { ExpandableMiniV40(it.substringBefore(" ·"), it.substringAfter(" ·")) }
        }
        ResponsiveSectionV17("Protocolos didácticos de procedimientos") {
            surgicalProtocolsV40.forEachIndexed { i, p ->
                Card(onClick={selected=i}, modifier=Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=if(selected==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(5.dp)) {
                        Text(p.name,fontWeight=FontWeight.Black)
                        if(selected==i) {
                            Text("Cómo se indica/diagnostica: ${p.diagnosis}")
                            Text("Secuencia educativa general: ${p.overview}")
                        }
                    }
                }
            }
        }
        ResponsiveSectionV17("Resumen quirúrgico") {
            val chosen=surgicalProtocolsV40[selected]
            Text("Procedimiento estudiado: ${chosen.name}. ${chosen.diagnosis}")
            Card(onClick={TeachingStateV40.moduleSummaries["surgical"]="${chosen.name} · ${chosen.diagnosis}"},colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),modifier=Modifier.fillMaxWidth()) { Text("Guardar resumen educativo",Modifier.padding(12.dp),fontWeight=FontWeight.Black) }
        }
        NoticeCard("Los pasos son un mapa de aprendizaje, no instrucciones para cirugía sin supervisión. El alcance de pregrado depende de la institución, competencia demostrada, caso, anatomía, consentimiento y docente responsable.")
    }
}

@Composable private fun ThirdMolarAngulationV40() {
    Canvas(Modifier.fillMaxWidth().height(170.dp)) {
        val w=size.width; val h=size.height
        fun tooth(x:Float,y:Float,angle:Float) {
            // Esquema anatómico simplificado con corona y raíces; cuatro ejemplos se reconocen por orientación.
            val p=Path().apply { moveTo(x-22,y-42); quadraticBezierTo(x,y-58,x+22,y-42); lineTo(x+17,y-8); lineTo(x+10,y+40); lineTo(x,y+18); lineTo(x-10,y+40); lineTo(x-17,y-8);close() }
            withTransform({rotate(angle,Offset(x,y))}) { drawPath(p,Color(0xFFFFF4D8)); drawPath(p,Color(0xFF8B8171),style=Stroke(2.5f)) }
        }
        tooth(w*.15f,h*.50f,0f); tooth(w*.38f,h*.50f,-35f); tooth(w*.62f,h*.50f,35f); tooth(w*.85f,h*.52f,-88f)
    }
}

@Composable
fun PeriodontalTeachingV40Screen(lang:String, session:EducationalSession, onSessionChanged:(EducationalSession)->Unit, onBack:()->Unit) {
    var showChart by remember { mutableStateOf(false) }
    ResponsiveScreenV17("Ficha periodontal · guía", "Antes del periodontograma revisa cómo se gradúan movilidad y furcación.", onBack) { _ ->
        ResponsiveSectionV17("Movilidad dentaria · guía didáctica") {
            listOf(
                "Grado 0 · movilidad fisiológica dentro de límites normales.",
                "Grado 1 · movilidad horizontal leve, aproximadamente hasta 1 mm.",
                "Grado 2 · movilidad horizontal mayor de 1 mm, sin componente vertical franco.",
                "Grado 3 · movilidad horizontal marcada y componente vertical/depresible."
            ).forEach { (ExpandableMiniV40(it.substringBefore(" ·"),it.substringAfter(" ·"))) }
            NoticeCard("La fuerza aplicada y la clasificación deben estandarizarse; la movilidad se interpreta junto con soporte periodontal, trauma oclusal, inflamación y otras causas.")
        }
        ResponsiveSectionV17("Furcación · guía horizontal") {
            listOf(
                "Grado 0 · no se detecta compromiso de furcación.",
                "Grado I · pérdida horizontal incipiente; entrada de furcación, sin atravesar el ancho del diente.",
                "Grado II · pérdida horizontal parcial; fondo óseo aún impide paso completo de lado a lado.",
                "Grado III · pérdida horizontal de lado a lado (through-and-through); puede estar cubierta por tejido gingival."
            ).forEach { ExpandableMiniV40(it.substringBefore(" ·"),it.substringAfter(" ·")) }
        }
        Card(onClick={showChart=!showChart},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) { Text(if(showChart)"Ocultar periodontograma" else "Abrir periodontograma interactivo",Modifier.padding(14.dp),fontWeight=FontWeight.Black) }
        if(showChart) {
            NoticeCard("El periodontograma completo aparece debajo. Usa seis sitios por diente y guarda únicamente un ejercicio ficticio.")
            // Se mantiene el motor existente: no se reimplementa el cálculo periodontal.
            PeriodontogramEmbeddedV40(session,onSessionChanged,lang)
        }
    }
}

@Composable
private fun PeriodontogramEmbeddedV40(session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,lang:String) {
    var selectedTooth by remember{mutableStateOf(16)}
    val record=session.periodontogram[selectedTooth]?:com.yomismtz.expedientedeldentista.clinical.PerioRecord()
    val sites=listOf("MV","V","DV","ML","L/P","DL")
    fun update(r:com.yomismtz.expedientedeldentista.clinical.PerioRecord){onSessionChanged(session.copy(periodontogram=session.periodontogram+(selectedTooth to r)))}
    ResponsiveSectionV17("OD $selectedTooth") {
        DentalArchSelector(ClinicalContent.permanentTeeth,selectedTooth,{selectedTooth=it}){it in session.periodontogram}
        sites.chunked(3).forEachIndexed{ri,row-> Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){row.forEachIndexed{ci,site->
            val idx=ri*3+ci
            OutlinedTextField(record.probingDepths[idx].takeIf{it>0}?.toString()?:"",{raw->val vals=record.probingDepths.toMutableList();vals[idx]=raw.toIntOrNull()?.coerceIn(0,15)?:0;update(record.copy(probingDepths=vals))},{Text(site)},modifier=Modifier.weight(1f))
        }}}
        Text("Movilidad")
        Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){(0..3).forEach{g->FilterChip(record.mobility==g,{update(record.copy(mobility=g))},{Text(g.toString())})}}
        Text("Furcación")
        Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){(0..3).forEach{g->FilterChip(record.furcation==g,{update(record.copy(furcation=g))},{Text(g.toString())})}}
        BooleanRow("Sangrado al sondaje",record.bleeding){update(record.copy(bleeding=it))}
        BooleanRow("Placa",record.plaque){update(record.copy(plaque=it))}
        BooleanRow("Supuración",record.suppuration){update(record.copy(suppuration=it))}
        Card(onClick={TeachingStateV40.moduleSummaries["periodontal"]="OD $selectedTooth · movilidad ${record.mobility} · furcación ${record.furcation} · sangrado ${if(record.bleeding)"sí" else "no"}"},colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),modifier=Modifier.fillMaxWidth()){Text("Guardar resumen periodontal educativo",Modifier.padding(12.dp),fontWeight=FontWeight.Black)}
    }
}

@Composable private fun ExpandableMiniV40(title:String,detail:String) {
    var open by remember{mutableStateOf(false)}
    Card(onClick={open=!open},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(open)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.3f)),shape=RoundedCornerShape(12.dp)){
        Column(Modifier.padding(10.dp)){Text(title,fontWeight=FontWeight.Black);if(open)Text(detail)else Text("Toca para explicación",style=MaterialTheme.typography.bodySmall)}
    }
}
