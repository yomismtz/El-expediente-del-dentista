package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

private val EndoLavender = Color(0xFFE9DDF5)
private val EndoLilac = Color(0xFFD3BCE9)
private val EndoPurple = Color(0xFF7447A3)
private val EndoDeep = Color(0xFF43235F)
private val EndoMint = Color(0xFF66D6C7)
private val EndoTurquoise = Color(0xFF2EB9B1)
private val EndoPaper = Color(0xFFFFFCFF)

private data class EndoCanalRow(
    val name: String,
    val estimated: String = "",
    val apex: String = "",
    val working: String = ""
)

private data class EndoTeachingStep(
    val key: String,
    val es: String,
    val en: String,
    val whyEs: String,
    val whyEn: String
)

@Composable
fun EndodonticInteractiveV2Screen(
    lang: String,
    session: EducationalSession,
    onOpenPulpal: () -> Unit,
    onOpenApical: () -> Unit,
    onBack: () -> Unit
) {
    val result = ClinicalEngines.pulpalDiagnosis(session.pulpal)
    var tab by remember { mutableStateOf(0) }
    var selectedTooth by remember {
        mutableStateOf(session.pulpal.tooth.takeIf { it > 0 } ?: 36)
    }
    var dentition by remember { mutableStateOf("mature") }
    var procedure by remember { mutableStateOf("root_canal") }
    var restoration by remember { mutableStateOf("") }
    var referencePoint by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var canals by remember { mutableStateOf(defaultCanals(selectedTooth)) }
    val completed = remember { mutableStateMapOf<String, Boolean>() }

    val pulpal = if (lang == "en") result.pulpalEn else result.pulpalEs
    val apical = if (lang == "en") result.apicalEn else result.apicalEs
    val orientation = treatmentOrientation(dentition, session, lang)
    val procedureLabel = procedureLabel(procedure, lang)

    fun changeTooth(tooth: Int) {
        selectedTooth = tooth
        canals = defaultCanals(tooth)
        referencePoint = ""
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Ficha endodóntica interactiva", "Interactive endodontic sheet"),
                onBack,
                tr(
                    lang,
                    "Guía de razonamiento y llenado para diagnóstico, conductometría, secuencia clínica, terapia pulpar en niños y adultos, irrigación segura y redacción final.",
                    "Reasoning and documentation guide for diagnosis, working length, clinical sequence, pulp therapy in children and adults, safe irrigation and final charting."
                )
            )
        }

        item {
            NoticeCard(
                tr(
                    lang,
                    "Uso educativo y bajo supervisión. No sustituye valoración clínica, radiografías, pruebas de sensibilidad/vitalidad ni el protocolo de tu escuela. No introduzcas datos identificables de pacientes reales.",
                    "Educational use under supervision. It does not replace clinical assessment, imaging, pulp tests or your school's protocol. Do not enter identifiable real-patient data."
                )
            )
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                val labels = listOf(
                    tr(lang, "Ficha", "Sheet"),
                    tr(lang, "Longitud", "Length"),
                    tr(lang, "Pasos", "Steps"),
                    tr(lang, "Niños", "Pediatric"),
                    tr(lang, "Irrigación", "Irrigation"),
                    tr(lang, "Final", "Final")
                )
                labels.forEachIndexed { i, label ->
                    FilterChip(
                        selected = tab == i,
                        onClick = { tab = i },
                        label = { Text(label) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        when (tab) {
            0 -> {
                item {
                    SectionCard(tr(lang, "1 · Órgano dentario", "1 · Tooth")) {
                        DentalArchSelector(
                            teeth = ClinicalContent.permanentTeeth,
                            selectedTooth = selectedTooth,
                            onSelect = { changeTooth(it) }
                        ) { it == selectedTooth }
                    }
                }
                item {
                    SectionCard(tr(lang, "2 · Etapa dentaria", "2 · Dentition stage")) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(
                                "mature" to tr(lang, "Permanente maduro", "Mature permanent"),
                                "immature" to tr(lang, "Permanente joven", "Immature permanent"),
                                "primary" to tr(lang, "Temporal", "Primary")
                            ).forEach { (id, label) ->
                                FilterChip(
                                    selected = dentition == id,
                                    onClick = {
                                        dentition = id
                                        procedure = defaultProcedure(id, session)
                                    },
                                    label = { Text(label) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
                item {
                    SectionCard(tr(lang, "3 · Diagnóstico endodóntico", "3 · Endodontic diagnosis")) {
                        Text(tr(lang, "Pulpar", "Pulpal"), fontWeight = FontWeight.Black, color = EndoDeep)
                        Text(pulpal, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(tr(lang, "Periapical", "Apical"), fontWeight = FontWeight.Black, color = EndoDeep)
                        Text(apical, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(onClick = onOpenPulpal, modifier = Modifier.fillMaxWidth()) {
                            Text("⚡ ${tr(lang, "Revisar diagnóstico pulpar/periapical", "Review pulpal/apical diagnosis")}")
                        }
                        OutlinedButton(onClick = onOpenApical, modifier = Modifier.fillMaxWidth()) {
                            Text("🩻 ${tr(lang, "Revisar pruebas e imagen", "Review tests and imaging")}")
                        }
                    }
                }
                item {
                    SectionCard(tr(lang, "4 · Orientación terapéutica", "4 · Treatment orientation")) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = EndoMint.copy(alpha = .18f)),
                            border = BorderStroke(1.dp, EndoTurquoise),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(tr(lang, "Más compatible con", "Most compatible with"), fontWeight = FontWeight.Black, color = EndoDeep)
                                Text(orientation, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(tr(lang, "Procedimiento para practicar la ficha", "Procedure for documentation practice"), fontWeight = FontWeight.Bold)
                        val options = procedureOptions(dentition, lang)
                        options.forEach { (id, label) ->
                            FilterChip(
                                selected = procedure == id,
                                onClick = { procedure = id },
                                label = { Text(label) },
                                modifier = Modifier.padding(end = 5.dp, bottom = 4.dp)
                            )
                        }
                        NoticeCard(
                            tr(
                                lang,
                                "La selección es didáctica. Restaurabilidad, desarrollo radicular, control de infección, pronóstico, cooperación, posibilidad de aislamiento y supervisión docente pueden cambiar el tratamiento.",
                                "Selection is educational. Restorability, root development, infection control, prognosis, cooperation, isolation and faculty supervision can change treatment."
                            )
                        )
                    }
                }
            }

            1 -> {
                item {
                    SectionCard(tr(lang, "Conductometría / longitud de trabajo", "Working length")) {
                        Text(
                            tr(
                                lang,
                                "La longitud de trabajo es una referencia clínica para preparar y obturar sin extenderse innecesariamente a tejidos apicales. Debe integrarse con referencia coronal reproducible, localizador apical y control radiográfico cuando el protocolo lo indique.",
                                "Working length is a clinical reference for shaping and filling while avoiding unnecessary extension into apical tissues. Integrate a reproducible coronal reference, an apex locator and radiographic control when indicated by protocol."
                            )
                        )
                        OutlinedTextField(
                            value = referencePoint,
                            onValueChange = { referencePoint = it },
                            label = { Text(tr(lang, "Punto de referencia coronal", "Coronal reference point")) },
                            placeholder = { Text(tr(lang, "Ej. cúspide MV / borde incisal", "e.g. MB cusp / incisal edge")) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                items(canals.size) { index ->
                    val canal = canals[index]
                    SectionCard("${tr(lang, "Conducto", "Canal")} ${canal.name}") {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedTextField(
                                value = canal.estimated,
                                onValueChange = { v -> canals = canals.updated(index, canal.copy(estimated = numericLength(v))) },
                                label = { Text(tr(lang, "Estimación Rx mm", "Rx estimate mm")) },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = canal.apex,
                                onValueChange = { v -> canals = canals.updated(index, canal.copy(apex = numericLength(v))) },
                                label = { Text(tr(lang, "Localizador mm", "Apex locator mm")) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        OutlinedTextField(
                            value = canal.working,
                            onValueChange = { v -> canals = canals.updated(index, canal.copy(working = numericLength(v))) },
                            label = { Text(tr(lang, "Longitud de trabajo elegida (mm)", "Selected working length (mm)")) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            tr(
                                lang,
                                "No existe una resta fija que sustituya la confirmación clínica. Si localizador, anatomía e imagen no concuerdan, detente y revisa antes de instrumentar.",
                                "No fixed subtraction replaces clinical confirmation. If the apex locator, anatomy and image disagree, stop and reassess before instrumentation."
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = EndoPurple
                        )
                    }
                }
                item {
                    OutlinedButton(
                        onClick = { canals = canals + EndoCanalRow("C${canals.size + 1}") },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(tr(lang, "+ Añadir conducto", "+ Add canal")) }
                    if (canals.size > 1) {
                        OutlinedButton(
                            onClick = { canals = canals.dropLast(1) },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(tr(lang, "Quitar último conducto", "Remove last canal")) }
                    }
                }
            }

            2 -> {
                item {
                    SectionCard("$procedureLabel · ${tr(lang, "secuencia guiada", "guided sequence")}") {
                        Text(
                            tr(
                                lang,
                                "Marca los pasos conforme los revisas. No es una lista de autorización clínica; sirve para que el alumno no omita etapas de razonamiento, aislamiento, control y documentación.",
                                "Check steps as you review them. This is not clinical authorization; it helps students avoid omitting reasoning, isolation, control and documentation stages."
                            )
                        )
                    }
                }
                items(stepsFor(procedure, lang)) { step ->
                    val isDone = completed[step.key] == true
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { completed[step.key] = !isDone },
                        colors = CardDefaults.cardColors(containerColor = if (isDone) EndoMint.copy(alpha = .16f) else EndoPaper),
                        border = BorderStroke(1.dp, if (isDone) EndoTurquoise else EndoLilac),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Checkbox(checked = isDone, onCheckedChange = { completed[step.key] = it })
                            Column(Modifier.weight(1f)) {
                                Text(if (lang == "en") step.en else step.es, fontWeight = FontWeight.Bold, color = EndoDeep)
                                Text(if (lang == "en") step.whyEn else step.whyEs, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            3 -> {
                item {
                    SectionCard(tr(lang, "Terapia pulpar en niños y dientes jóvenes", "Pediatric and immature-tooth pulp therapy")) {
                        Text(
                            tr(
                                lang,
                                "Primero distingue: diente temporal vs permanente joven, vitalidad/estado pulpar, signos de infección, restaurabilidad, etapa de desarrollo y posibilidad de mantener el diente.",
                                "First distinguish: primary vs immature permanent tooth, pulp status, infection signs, restorability, development stage and whether the tooth can be retained."
                            )
                        )
                    }
                }
                item {
                    PediatricEndoCard(
                        title = tr(lang, "Diente temporal · pulpa vital", "Primary tooth · vital pulp"),
                        body = tr(
                            lang,
                            "Con pulpa normal o pulpitis reversible pueden considerarse terapias pulpares vitales según profundidad, exposición y hemostasia: tratamiento pulpar indirecto, recubrimiento selectivo o pulpotomía cuando esté indicada. Una pulpotomía completa también puede considerarse en casos seleccionados con signos de pulpitis irreversible si no hay signos clínicos/radiográficos de infección, bajo criterios actuales y supervisión.",
                            "With normal pulp or reversible pulpitis, vital pulp therapies may be considered according to depth, exposure and hemostasis: indirect pulp treatment, selected capping or pulpotomy when indicated. Complete pulpotomy may also be considered in selected cases showing irreversible-pulpitis signs when clinical/radiographic infection is absent, under current criteria and supervision."
                        )
                    )
                }
                item {
                    PediatricEndoCard(
                        title = tr(lang, "Diente temporal · pulpa no vital", "Primary tooth · non-vital pulp"),
                        body = tr(
                            lang,
                            "En pulpitis irreversible no recuperable o necrosis, la pulpectomía puede ser una opción si el diente es restaurable y conservarlo es útil. La reabsorción fisiológica, anatomía de conductos, proximidad al sucesor permanente y material reabsorbible condicionan la técnica y el pronóstico.",
                            "For non-recoverable irreversible pulpitis or necrosis, pulpectomy may be an option when the tooth is restorable and worth retaining. Physiologic resorption, canal anatomy, the permanent successor and resorbable filling material affect technique and prognosis."
                        )
                    )
                }
                item {
                    PediatricEndoCard(
                        title = tr(lang, "Permanente joven · pulpa vital", "Immature permanent · vital pulp"),
                        body = tr(
                            lang,
                            "La prioridad es conservar tejido pulpar vital cuando sea razonable para permitir desarrollo radicular. Dependiendo del diagnóstico y la exposición pueden considerarse tratamiento pulpar indirecto, recubrimiento directo, pulpotomía parcial o completa con biomaterial indicado.",
                            "The priority is to preserve vital pulp when reasonable so root development can continue. Depending on diagnosis and exposure, indirect pulp treatment, direct pulp capping, partial pulpotomy or complete pulpotomy with an indicated biomaterial may be considered."
                        )
                    )
                }
                item {
                    PediatricEndoCard(
                        title = tr(lang, "Permanente joven · necrosis", "Immature permanent · necrosis"),
                        body = tr(
                            lang,
                            "La raíz incompleta cambia el plan. Puede requerir endodoncia regenerativa, apicoformación u otra estrategia especializada. No debe aplicarse automáticamente el protocolo de un diente permanente maduro.",
                            "An incomplete root changes the plan. Regenerative endodontics, apexification or another specialized strategy may be needed. A mature permanent-tooth protocol should not be applied automatically."
                        )
                    )
                }
                item {
                    NoticeCard(
                        tr(
                            lang,
                            "En dientes temporales y permanentes inmaduros, las pruebas térmicas/eléctricas pueden ser menos confiables; integra síntomas, exploración, radiografías, palpación, percusión, movilidad y evolución clínica.",
                            "In primary and immature permanent teeth, thermal/electric pulp tests may be less reliable; integrate symptoms, examination, radiographs, palpation, percussion, mobility and clinical course."
                        )
                    )
                }
            }

            4 -> {
                item {
                    SectionCard(tr(lang, "Irrigación: objetivos", "Irrigation: goals")) {
                        Text(tr(lang, "• Favorecer arrastre de detritos y reducción microbiana.", "• Flush debris and reduce microbial load."))
                        Text(tr(lang, "• Complementar, no sustituir, la preparación mecánica y el control de longitud.", "• Complement, not replace, mechanical preparation and length control."))
                        Text(tr(lang, "• Renovar irrigante según el protocolo clínico y mantener control de seguridad.", "• Refresh irrigant according to the clinical protocol while maintaining safety controls."))
                    }
                }
                item {
                    SectionCard(tr(lang, "Hipoclorito de sodio · uso seguro", "Sodium hypochlorite · safe use")) {
                        Text(
                            tr(
                                lang,
                                "El NaOCl se usa ampliamente por su acción antimicrobiana y capacidad de disolver tejido orgánico. La concentración debe seguir el protocolo institucional y el producto dental; concentraciones mayores aumentan eficacia de disolución, pero también toxicidad si ocurre extrusión.",
                                "NaOCl is widely used for antimicrobial action and organic-tissue dissolution. Concentration should follow the institution and dental product; higher concentrations can increase tissue dissolution but also toxicity if extrusion occurs."
                            )
                        )
                        Text("• ${tr(lang, "Aislamiento absoluto obligatorio para el ejercicio clínico indicado.", "Use rubber-dam isolation when clinically indicated by protocol.")}")
                        Text("• ${tr(lang, "Usa aguja adecuada y sin enclavarla; evita presión excesiva y extrusión apical.", "Use an appropriate needle without binding it; avoid excessive pressure and apical extrusion.")}")
                        Text("• ${tr(lang, "Protege ojos, piel, ropa y tejidos blandos.", "Protect eyes, skin, clothing and soft tissues.")}")
                        Text("• ${tr(lang, "No uses soluciones domésticas ni concentraciones desconocidas.", "Do not use household solutions or unknown concentrations.")}")
                    }
                }
                item {
                    SectionCard(tr(lang, "EDTA y otros irrigantes", "EDTA and other irrigants")) {
                        Text(
                            tr(
                                lang,
                                "EDTA se utiliza como quelante para componente inorgánico/capa de barrillo según el protocolo. NaOCl y EDTA deben manejarse como pasos separados porque interactúan y pueden modificar sus propiedades.",
                                "EDTA is used as a chelator for inorganic components/smear layer according to protocol. NaOCl and EDTA should be handled as separate steps because they interact and can alter their properties."
                            )
                        )
                        Text(
                            tr(
                                lang,
                                "Evita mezclar directamente NaOCl con clorhexidina. Si el protocolo contempla distintos irrigantes, usa la secuencia y enjuagues indicados por tu institución.",
                                "Avoid directly mixing NaOCl with chlorhexidine. If the protocol uses multiple irrigants, follow the institution's required sequence and intermediate rinses."
                            )
                        )
                    }
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2F2)),
                        border = BorderStroke(1.dp, Color(0xFFD64545)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("⚠ ${tr(lang, "Preparación / dilución", "Preparation / dilution")}", fontWeight = FontWeight.Black, color = Color(0xFF9A2D2D))
                            Text(
                                tr(
                                    lang,
                                    "Por seguridad, la app no da una receta para fabricar o diluir hipoclorito a partir de productos domésticos o concentraciones desconocidas. Usa irrigante dental comercial o preparación institucional validada y verifica etiqueta, concentración, fecha y almacenamiento.",
                                    "For safety, the app does not provide a recipe for making or diluting hypochlorite from household products or unknown stock. Use a commercial dental irrigant or a validated institutional preparation and verify label, concentration, date and storage."
                                )
                            )
                        }
                    }
                }
                item {
                    SectionCard(tr(lang, "Si sospechas accidente con NaOCl", "If a NaOCl accident is suspected")) {
                        Text(tr(lang, "Suspende la irrigación/procedimiento y solicita supervisión inmediata.", "Stop irrigation/the procedure and obtain immediate supervision."), fontWeight = FontWeight.Bold)
                        Text(tr(lang, "Valora dolor súbito, edema, sangrado, compromiso ocular, respiratorio o deglutorio y activa el protocolo de urgencias de la clínica.", "Assess sudden pain, swelling, bleeding, eye involvement, breathing or swallowing compromise and activate the clinic emergency protocol."))
                        Text(tr(lang, "La atención posterior depende de la gravedad y debe seguir el protocolo institucional; los síntomas progresivos o compromiso de vía aérea requieren atención urgente.", "Further care depends on severity and should follow institutional protocol; progressive symptoms or airway compromise require urgent care."))
                    }
                }
            }

            else -> {
                item {
                    SectionCard(tr(lang, "Resumen para el expediente físico", "Summary for the physical record")) {
                        Text("OD $selectedTooth", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = EndoDeep)
                        Text("${tr(lang, "Diagnóstico pulpar", "Pulpal diagnosis")}: $pulpal")
                        Text("${tr(lang, "Diagnóstico periapical", "Apical diagnosis")}: $apical")
                        Text("${tr(lang, "Procedimiento practicado", "Procedure practiced")}: $procedureLabel")
                        val wl = canals.filter { it.working.isNotBlank() }.joinToString(" · ") { "${it.name} ${it.working} mm" }
                        Text("${tr(lang, "Longitudes de trabajo", "Working lengths")}: ${wl.ifBlank { tr(lang, "pendientes", "pending") }}")
                        Text("${tr(lang, "Referencia coronal", "Coronal reference")}: ${referencePoint.ifBlank { "___" }}")
                    }
                }
                item {
                    SectionCard(tr(lang, "Restauración y control", "Restoration and follow-up")) {
                        OutlinedTextField(
                            value = restoration,
                            onValueChange = { restoration = it },
                            label = { Text(tr(lang, "Restauración provisional/definitiva prevista", "Planned provisional/definitive restoration")) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text(tr(lang, "Indicaciones, control radiográfico o seguimiento", "Instructions, radiographic control or follow-up")) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                    }
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = EndoMint.copy(alpha = .18f)),
                        border = BorderStroke(1.dp, EndoTurquoise),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("✍️ ${tr(lang, "¿Qué escribo al final?", "What do I write at the end?")}", fontWeight = FontWeight.Black, color = EndoDeep)
                            Text(
                                finalEndoWriting(
                                    lang = lang,
                                    tooth = selectedTooth,
                                    pulpal = pulpal,
                                    apical = apical,
                                    procedure = procedureLabel,
                                    canals = canals,
                                    referencePoint = referencePoint,
                                    restoration = restoration,
                                    notes = notes
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PediatricEndoCard(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = EndoLavender.copy(alpha = .42f)),
        border = BorderStroke(1.dp, EndoLilac),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, fontWeight = FontWeight.Black, color = EndoDeep)
            Text(body)
        }
    }
}

private fun defaultCanals(tooth: Int): List<EndoCanalRow> {
    val unit = tooth % 10
    val quadrant = tooth / 10
    return when {
        unit in listOf(1, 2, 3) -> listOf(EndoCanalRow("C1"))
        unit in listOf(4, 5) && quadrant in listOf(1, 2) -> listOf(EndoCanalRow("V"), EndoCanalRow("P"))
        unit in listOf(4, 5) -> listOf(EndoCanalRow("C1"), EndoCanalRow("C2"))
        unit in listOf(6, 7, 8) && quadrant in listOf(1, 2) -> listOf(
            EndoCanalRow("MV1"), EndoCanalRow("MV2"), EndoCanalRow("DV"), EndoCanalRow("P")
        )
        else -> listOf(EndoCanalRow("MV"), EndoCanalRow("ML"), EndoCanalRow("D"))
    }
}

private fun defaultProcedure(dentition: String, session: EducationalSession): String = when (dentition) {
    "primary" -> if (session.pulpal.sensitivityNegative || session.pulpal.fistula || session.pulpal.swelling) "pulpectomy" else "pulpotomy"
    "immature" -> if (session.pulpal.sensitivityNegative) "immature_nonvital" else "vital_pulp"
    else -> if (session.pulpal.previousRootCanal) "retreatment_review" else "root_canal"
}

private fun procedureOptions(dentition: String, lang: String): List<Pair<String, String>> = when (dentition) {
    "primary" -> listOf(
        "vital_pulp" to tr(lang, "Terapia pulpar vital", "Vital pulp therapy"),
        "pulpotomy" to tr(lang, "Pulpotomía", "Pulpotomy"),
        "pulpectomy" to tr(lang, "Pulpectomía", "Pulpectomy"),
        "extract_review" to tr(lang, "Valorar extracción", "Assess extraction")
    )
    "immature" -> listOf(
        "vital_pulp" to tr(lang, "Terapia pulpar vital / apicogénesis", "Vital pulp therapy / apexogenesis"),
        "pulpotomy" to tr(lang, "Pulpotomía parcial/completa", "Partial/complete pulpotomy"),
        "immature_nonvital" to tr(lang, "Necrosis con ápice abierto", "Necrosis with open apex"),
        "root_canal" to tr(lang, "Endodoncia según desarrollo", "RCT according to development")
    )
    else -> listOf(
        "root_canal" to tr(lang, "Tratamiento de conductos", "Root-canal treatment"),
        "vital_pulp" to tr(lang, "Terapia pulpar vital seleccionada", "Selected vital pulp therapy"),
        "pulpotomy" to tr(lang, "Pulpotomía seleccionada", "Selected pulpotomy"),
        "retreatment_review" to tr(lang, "Revisión de retratamiento", "Retreatment review")
    )
}

private fun procedureLabel(id: String, lang: String): String = when (id) {
    "pulpotomy" -> tr(lang, "Pulpotomía", "Pulpotomy")
    "pulpectomy" -> tr(lang, "Pulpectomía", "Pulpectomy")
    "vital_pulp" -> tr(lang, "Terapia pulpar vital", "Vital pulp therapy")
    "immature_nonvital" -> tr(lang, "Manejo de necrosis con ápice abierto", "Management of necrosis with open apex")
    "retreatment_review" -> tr(lang, "Valoración de retratamiento", "Retreatment assessment")
    "extract_review" -> tr(lang, "Valoración de extracción", "Extraction assessment")
    else -> tr(lang, "Tratamiento de conductos", "Root-canal treatment")
}

private fun treatmentOrientation(dentition: String, session: EducationalSession, lang: String): String {
    val a = session.pulpal
    return when (dentition) {
        "primary" -> when {
            a.sensitivityNegative || a.fistula || a.swelling -> tr(lang, "Pulpectomía si el diente es restaurable y su conservación está indicada; valorar alternativas según infección, reabsorción y sucesor permanente.", "Pulpectomy if the tooth is restorable and retention is indicated; assess alternatives according to infection, resorption and the permanent successor.")
            a.spontaneousPain || a.nightPain || a.coldLingering -> tr(lang, "Valorar pulpotomía completa u otra terapia pulpar según hallazgos, ausencia/presencia de infección y criterios actuales.", "Assess complete pulpotomy or other pulp therapy according to findings, infection status and current criteria.")
            else -> tr(lang, "Terapia pulpar vital conservadora según profundidad de lesión, exposición y control de hemostasia.", "Conservative vital pulp therapy according to lesion depth, exposure and hemostasis control.")
        }
        "immature" -> if (a.sensitivityNegative) {
            tr(lang, "Necrosis con desarrollo radicular incompleto: valorar endodoncia regenerativa, apicoformación u otra estrategia especializada.", "Necrosis with incomplete root development: assess regenerative endodontics, apexification or another specialized strategy.")
        } else {
            tr(lang, "Priorizar conservación de pulpa vital para favorecer desarrollo radicular cuando el caso lo permita.", "Prioritize vital-pulp preservation to support root development when the case permits.")
        }
        else -> when {
            a.previousRootCanal -> tr(lang, "Diente previamente tratado: confirmar causa de signos/síntomas y valorar restauración, calidad endodóntica y necesidad de retratamiento o referencia.", "Previously treated tooth: confirm the source of signs/symptoms and assess restoration, endodontic quality and need for retreatment or referral.")
            a.sensitivityNegative || a.spontaneousPain || a.nightPain || a.coldLingering -> tr(lang, "Si el diente es restaurable y la indicación endodóntica se confirma, tratamiento de conductos; considerar terapia pulpar vital solo en casos seleccionados compatibles.", "If the tooth is restorable and endodontic indication is confirmed, root-canal treatment; consider vital pulp therapy only in selected compatible cases.")
            else -> tr(lang, "Completa pruebas antes de decidir. Una pulpa normal o reversible no debe llevar automáticamente a tratamiento de conductos.", "Complete testing before deciding. A normal or reversible pulp should not automatically lead to root-canal treatment.")
        }
    }
}

private fun stepsFor(procedure: String, lang: String): List<EndoTeachingStep> = when (procedure) {
    "pulpotomy" -> listOf(
        EndoTeachingStep("diag", "Confirmar diagnóstico, restaurabilidad y radiografía", "Confirm diagnosis, restorability and radiograph", "Evita tratar un diente sin integrar estado pulpar, infección y pronóstico.", "Avoid treating a tooth without integrating pulp status, infection and prognosis."),
        EndoTeachingStep("iso", "Anestesia cuando corresponda y aislamiento absoluto", "Anesthesia when appropriate and rubber-dam isolation", "Controla contaminación y protege tejidos.", "Controls contamination and protects tissues."),
        EndoTeachingStep("access", "Acceso y remoción de pulpa coronal", "Access and remove coronal pulp", "Permite valorar tejido remanente y obtener hemostasia.", "Allows assessment of remaining tissue and hemostasis."),
        EndoTeachingStep("hemo", "Evaluar y controlar hemostasia según protocolo", "Assess and control hemostasis per protocol", "La respuesta del tejido aporta información clínica y condiciona la técnica.", "Tissue response provides clinical information and affects technique."),
        EndoTeachingStep("material", "Colocar biomaterial indicado y sellado coronal", "Place indicated biomaterial and coronal seal", "El sellado y material seleccionado influyen en el pronóstico.", "Seal and selected material affect prognosis."),
        EndoTeachingStep("control", "Control clínico/radiográfico", "Clinical/radiographic follow-up", "Confirma ausencia de signos de fracaso y evolución favorable.", "Confirms absence of failure signs and favorable evolution.")
    )
    "pulpectomy" -> listOf(
        EndoTeachingStep("diag", "Confirmar diagnóstico, restaurabilidad, reabsorción y sucesor", "Confirm diagnosis, restorability, resorption and successor", "En temporales estos datos cambian indicación y técnica.", "In primary teeth these findings change indication and technique."),
        EndoTeachingStep("iso", "Aislamiento y acceso", "Isolation and access", "Reduce contaminación y facilita visibilidad.", "Reduces contamination and improves visibility."),
        EndoTeachingStep("length", "Establecer longitud de trabajo con criterio pediátrico", "Establish working length with pediatric considerations", "Evita lesión de tejidos periapicales y del sucesor permanente.", "Helps avoid injury to periapical tissues and the permanent successor."),
        EndoTeachingStep("clean", "Limpieza e irrigación cuidadosa", "Careful cleaning and irrigation", "La anatomía y reabsorción exigen prudencia para evitar extrusión.", "Anatomy and resorption require caution to avoid extrusion."),
        EndoTeachingStep("fill", "Obturación con material indicado para temporal", "Fill with an indicated primary-tooth material", "El material debe ser compatible con el objetivo de mantener el temporal y su reabsorción fisiológica.", "Material should be compatible with retention goals and physiologic resorption."),
        EndoTeachingStep("restore", "Sellado/restauración coronal y seguimiento", "Coronal seal/restoration and follow-up", "El control coronal y radiográfico es parte del pronóstico.", "Coronal seal and radiographic follow-up are part of prognosis.")
    )
    "vital_pulp" -> listOf(
        EndoTeachingStep("case", "Selección del caso y diagnóstico pulpar", "Case selection and pulpal diagnosis", "La terapia vital depende de síntomas, exposición, desarrollo radicular y posibilidad de sellado.", "Vital pulp therapy depends on symptoms, exposure, root development and sealability."),
        EndoTeachingStep("isolate", "Aislamiento y control de contaminación", "Isolation and contamination control", "Favorece un campo limpio para conservar tejido vital.", "Supports a clean field for preserving vital tissue."),
        EndoTeachingStep("caries", "Remoción selectiva/abordaje de exposición según indicación", "Selective caries removal/exposure management as indicated", "Evita convertir una estrategia conservadora en eliminación innecesaria de tejido.", "Avoids turning a conservative strategy into unnecessary tissue removal."),
        EndoTeachingStep("hemostasis", "Valorar hemostasia y tejido pulpar", "Assess hemostasis and pulp tissue", "Ayuda a confirmar que el tejido remanente es compatible con la estrategia elegida.", "Helps confirm remaining tissue is compatible with the chosen strategy."),
        EndoTeachingStep("bio", "Biomaterial y restauración con sellado adecuado", "Biomaterial and well-sealed restoration", "El sellado coronal es esencial para reducir reinfección.", "Coronal seal is essential to reduce reinfection."),
        EndoTeachingStep("follow", "Seguimiento de síntomas, vitalidad y desarrollo radicular", "Follow symptoms, vitality and root development", "En dientes jóvenes debe vigilarse la continuación del desarrollo.", "In immature teeth continued root development should be monitored.")
    )
    "immature_nonvital" -> listOf(
        EndoTeachingStep("confirm", "Confirmar necrosis y ápice abierto", "Confirm necrosis and open apex", "La estrategia depende de desarrollo radicular y presencia de infección.", "Strategy depends on root development and infection."),
        EndoTeachingStep("plan", "Elegir estrategia especializada", "Choose specialized strategy", "Regenerativa y apicoformación tienen objetivos y protocolos diferentes.", "Regenerative and apexification approaches have different goals and protocols."),
        EndoTeachingStep("disinfect", "Desinfección química conservadora", "Conservative chemical disinfection", "Las paredes radiculares inmaduras son frágiles; evita instrumentación agresiva.", "Immature root walls are fragile; avoid aggressive instrumentation."),
        EndoTeachingStep("protocol", "Seguir protocolo institucional específico", "Follow the specific institutional protocol", "Concentraciones, medicación intracanal y pasos varían según estrategia.", "Concentrations, intracanal medicaments and steps vary by strategy."),
        EndoTeachingStep("seal", "Sellado coronal y controles programados", "Coronal seal and scheduled follow-up", "El éxito requiere vigilancia clínica y radiográfica en el tiempo.", "Success requires longitudinal clinical and radiographic monitoring.")
    )
    else -> listOf(
        EndoTeachingStep("diagnosis", "Diagnóstico pulpar + periapical y restaurabilidad", "Pulpal + apical diagnosis and restorability", "La endodoncia no comienza con la lima: comienza confirmando que el diente debe y puede conservarse.", "Endodontics does not start with a file: it starts by confirming the tooth should and can be retained."),
        EndoTeachingStep("anesthesia", "Anestesia cuando corresponda, aislamiento absoluto y campo seguro", "Anesthesia when appropriate, rubber-dam isolation and safe field", "Controla dolor, contaminación y protege al paciente.", "Controls pain, contamination and protects the patient."),
        EndoTeachingStep("access", "Acceso conservador y localización de conductos", "Conservative access and canal location", "Debe permitir limpieza sin debilitar estructura innecesariamente.", "Should permit cleaning without unnecessary structural weakening."),
        EndoTeachingStep("glide", "Permeabilidad/negociación y trayectoria reproducible", "Negotiation and reproducible glide path", "Reduce errores de instrumentación y ayuda a mantener anatomía.", "Reduces instrumentation errors and helps preserve anatomy."),
        EndoTeachingStep("working", "Determinar y verificar longitud de trabajo", "Determine and verify working length", "Guía la preparación, irrigación y obturación dentro de límites clínicos.", "Guides shaping, irrigation and filling within clinical limits."),
        EndoTeachingStep("shape", "Preparación biomecánica con irrigación renovada", "Biomechanical preparation with renewed irrigation", "Instrumentación e irrigación trabajan en conjunto para limpiar el sistema.", "Instrumentation and irrigation work together to clean the system."),
        EndoTeachingStep("final_irrigation", "Protocolo final de irrigación según la clínica", "Final irrigation protocol according to the clinic", "La secuencia debe respetar compatibilidades entre irrigantes y seguridad apical.", "Sequence should respect irrigant compatibility and apical safety."),
        EndoTeachingStep("dry", "Secado y valoración antes de obturar", "Dry and reassess before obturation", "Persistencia de exudado, dolor o sangrado puede indicar que el caso no está listo para obturación.", "Persistent exudate, pain or bleeding may indicate the case is not ready for filling."),
        EndoTeachingStep("fill", "Obturación del sistema de conductos", "Root-canal filling", "Busca sellado adecuado conforme a la técnica docente seleccionada.", "Seeks adequate filling according to the selected teaching technique."),
        EndoTeachingStep("restore", "Sellado/restauración coronal", "Coronal seal/restoration", "La restauración final influye de forma importante en el pronóstico.", "Final restoration strongly affects prognosis."),
        EndoTeachingStep("followup", "Control clínico y radiográfico", "Clinical and radiographic follow-up", "Permite valorar resolución de signos/síntomas y tejidos periapicales.", "Allows assessment of symptom resolution and periapical healing.")
    )
}

private fun finalEndoWriting(
    lang: String,
    tooth: Int,
    pulpal: String,
    apical: String,
    procedure: String,
    canals: List<EndoCanalRow>,
    referencePoint: String,
    restoration: String,
    notes: String
): String {
    val wl = canals.filter { it.working.isNotBlank() }.joinToString(", ") { "${it.name} ${it.working} mm" }.ifBlank { "___" }
    val ref = referencePoint.ifBlank { "___" }
    val rest = restoration.ifBlank { "___" }
    val follow = notes.ifBlank { "___" }
    return if (lang == "en") {
        "Tooth $tooth. Pulpal diagnosis: $pulpal. Apical diagnosis: $apical. Procedure: $procedure. Coronal reference: $ref. Working lengths: $wl. Irrigation: according to institutional endodontic protocol, with rubber-dam isolation and extrusion control. Coronal restoration/seal: $rest. Instructions/follow-up: $follow."
    } else {
        "OD $tooth. Diagnóstico pulpar: $pulpal. Diagnóstico periapical: $apical. Procedimiento: $procedure. Referencia coronal: $ref. Longitudes de trabajo: $wl. Irrigación: conforme al protocolo endodóntico institucional, con aislamiento absoluto y control de extrusión. Restauración/sellado coronal: $rest. Indicaciones/seguimiento: $follow."
    }
}

private fun numericLength(raw: String): String {
    val cleaned = raw.filter { it.isDigit() || it == '.' || it == ',' }.replace(',', '.')
    return cleaned.take(5)
}

private fun List<EndoCanalRow>.updated(index: Int, value: EndoCanalRow): List<EndoCanalRow> =
    mapIndexed { i, row -> if (i == index) value else row }
