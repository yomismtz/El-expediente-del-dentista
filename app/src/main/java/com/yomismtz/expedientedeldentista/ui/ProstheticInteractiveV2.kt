package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

private val ProstLavender = Color(0xFFE9DDF5)
private val ProstLilac = Color(0xFFD3BCE9)
private val ProstPurple = Color(0xFF7447A3)
private val ProstDeep = Color(0xFF43235F)
private val ProstMint = Color(0xFF66D6C7)
private val ProstTurquoise = Color(0xFF2EB9B1)
private val ProstMetal = Color(0xFFB8B1C0)
private val ProstPaper = Color(0xFFFFFCFF)

private val prostUpper = listOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27)
private val prostLower = listOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37)

private enum class ProstTab { DIAGNOSIS, RPD, COMPLETE, FIXED }
private enum class DesignMark { REST_M, REST_D, CINGULUM, DIRECT, INDIRECT, GUIDE, BASE, PONTIC, ABUTMENT }

private data class KennedyResult(
    val classNumber: Int,
    val label: String,
    val modifications: Int,
    val explanation: String
)

private fun edentulousSegments(arch: List<Int>, present: Set<Int>): List<List<Int>> {
    val out = mutableListOf<MutableList<Int>>()
    var current = mutableListOf<Int>()
    arch.forEach { tooth ->
        if (tooth !in present) current.add(tooth)
        else if (current.isNotEmpty()) {
            out.add(current)
            current = mutableListOf()
        }
    }
    if (current.isNotEmpty()) out.add(current)
    return out
}

private fun kennedyResult(arch: List<Int>, present: Set<Int>, lang: String): KennedyResult {
    val presentInArch = present.intersect(arch.toSet())
    if (presentInArch.isEmpty()) {
        return KennedyResult(
            0,
            tr(lang,"Edéntulo total · Kennedy no aplica","Completely edentulous · Kennedy does not apply"),
            0,
            tr(lang,"La clasificación de Kennedy se utiliza para arcos parcialmente edéntulos.","Kennedy classification is used for partially edentulous arches.")
        )
    }
    val segments = edentulousSegments(arch, present)
    if (segments.isEmpty()) return KennedyResult(0,tr(lang,"Sin áreas edéntulas","No edentulous areas"),0,"")

    val firstPresent = arch.indexOfFirst { it in present }
    val lastPresent = arch.indexOfLast { it in present }
    val distalRight = firstPresent > 0
    val distalLeft = lastPresent in 0 until arch.lastIndex
    val upper = arch.firstOrNull() == 17
    val mids = if (upper) setOf(11,21) else setOf(41,31)
    val anteriorCrossesMidline = segments.any { seg -> mids.all { it in seg } }

    val cls = when {
        distalRight && distalLeft -> 1
        distalRight || distalLeft -> 2
        anteriorCrossesMidline && segments.size == 1 -> 4
        else -> 3
    }
    val mods = if (cls == 4) 0 else (segments.size - 1).coerceAtLeast(0)
    val explanation = when (cls) {
        1 -> tr(lang,"Áreas edéntulas posteriores bilaterales de extremo libre.","Bilateral posterior distal-extension edentulous areas.")
        2 -> tr(lang,"Área edéntula posterior unilateral de extremo libre.","Unilateral posterior distal-extension edentulous area.")
        3 -> tr(lang,"Área edéntula limitada por dientes por delante y por detrás.","Bounded edentulous area with teeth anterior and posterior to it.")
        else -> tr(lang,"Área edéntula anterior única que cruza la línea media.","Single anterior edentulous area crossing the midline.")
    }
    val roman = listOf("","I","II","III","IV")[cls]
    val modText = if (mods > 0) tr(lang," · modificación $mods"," · modification $mods") else ""
    return KennedyResult(cls,"Kennedy $roman$modText",mods,explanation)
}

@Composable
fun ProstheticInteractiveV2Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var tab by remember { mutableStateOf(ProstTab.DIAGNOSIS) }
    var upperDesign by remember { mutableStateOf(true) }
    var designTool by remember { mutableStateOf(DesignMark.REST_M) }
    var fixedUpper by remember { mutableStateOf(true) }

    val upperPresent = session.prosthetic.upperPresent
    val lowerPresent = session.prosthetic.lowerPresent
    val designUpper = session.prosthetic.upperDesignMarks.mapValues { (_, marks) ->
        marks.mapNotNull { runCatching { DesignMark.valueOf(it) }.getOrNull() }.toSet()
    }
    val designLower = session.prosthetic.lowerDesignMarks.mapValues { (_, marks) ->
        marks.mapNotNull { runCatching { DesignMark.valueOf(it) }.getOrNull() }.toSet()
    }
    val rpdMaterial = session.prosthetic.rpdMaterial
    val majorConnector = if (upperDesign) session.prosthetic.upperMajorConnector else session.prosthetic.lowerMajorConnector
    val fixedMaterial = session.prosthetic.fixedMaterial
    val ponticType = session.prosthetic.ponticType

    val upperK = kennedyResult(prostUpper, upperPresent, lang)
    val lowerK = kennedyResult(prostLower, lowerPresent, lang)

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang,"Prótesis · diagnóstico, diseño y protocolos","Prosthodontics · diagnosis, design and protocols"),
                onBack,
                tr(lang,"Marca dientes presentes/ausentes, practica Kennedy-Applegate, diseña PPR, revisa prótesis total y planifica prótesis fija con pilares, pónticos y terminaciones.",
                    "Mark present/missing teeth, practice Kennedy-Applegate, design RPDs, review complete dentures and plan fixed prostheses with abutments, pontics and finish lines.")
            )
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    ProstTab.DIAGNOSIS to tr(lang,"Diagnóstico","Diagnosis"),
                    ProstTab.RPD to "PPR",
                    ProstTab.COMPLETE to tr(lang,"Total","Complete"),
                    ProstTab.FIXED to tr(lang,"Fija","Fixed")
                ).forEach { (t,label) ->
                    FilterChip(selected = tab == t, onClick = { tab = t }, label = { Text(label) }, modifier = Modifier.weight(1f))
                }
            }
        }

        when (tab) {
            ProstTab.DIAGNOSIS -> {
                item {
                    SectionCard(tr(lang,"1 · Marca los dientes presentes y ausentes","1 · Mark present and missing teeth")) {
                        Text(tr(lang,"Toca un diente para alternar presente/ausente. Los terceros molares se excluyen por defecto en este ejercicio; aplica las reglas de Applegate según el caso real.",
                            "Tap a tooth to toggle present/missing. Third molars are excluded by default in this exercise; apply Applegate rules to the real case."))
                        Text(tr(lang,"MAXILAR","MAXILLA"), fontWeight = FontWeight.Black, color = ProstDeep)
                        ProstArchRow(prostUpper, upperPresent, emptyMap()) { tooth ->
                            val updated = if (tooth in upperPresent) upperPresent - tooth else upperPresent + tooth
                            onSessionChanged(session.copy(prosthetic = session.prosthetic.copy(upperPresent = updated)))
                        }
                        KennedyResultCard(upperK)
                        Spacer(Modifier.height(6.dp))
                        Text(tr(lang,"MANDÍBULA","MANDIBLE"), fontWeight = FontWeight.Black, color = ProstDeep)
                        ProstArchRow(prostLower, lowerPresent, emptyMap()) { tooth ->
                            val updated = if (tooth in lowerPresent) lowerPresent - tooth else lowerPresent + tooth
                            onSessionChanged(session.copy(prosthetic = session.prosthetic.copy(lowerPresent = updated)))
                        }
                        KennedyResultCard(lowerK)
                    }
                }
                item {
                    SectionCard(tr(lang,"2 · Reglas de Applegate","2 · Applegate rules")) {
                        val rules = listOf(
                            tr(lang,"Clasifica después de las extracciones que modificarán el arco.","Classify after extractions that will alter the arch."),
                            tr(lang,"Un tercer molar ausente y no reemplazado no se considera; si está presente y se usará como pilar, sí cuenta.","A missing third molar that will not be replaced is ignored; if present and used as an abutment, it is considered."),
                            tr(lang,"Un segundo molar ausente que no se reemplazará tampoco determina la clasificación.","A missing second molar not intended for replacement does not determine the classification."),
                            tr(lang,"El área edéntula más posterior determina la clase.","The most posterior edentulous area determines the class."),
                            tr(lang,"Las demás áreas son modificaciones; se cuenta su número, no su tamaño.","Other areas are modifications; count their number, not their extent."),
                            tr(lang,"La Clase IV no tiene modificaciones.","Class IV has no modifications.")
                        )
                        rules.forEachIndexed { i, r -> Text("${i+1}. $r") }
                    }
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ProstMint.copy(alpha = .18f)),
                        border = BorderStroke(1.dp, ProstTurquoise),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(tr(lang,"¿Conviene otra clasificación además de Kennedy?","Use another classification besides Kennedy?"), fontWeight = FontWeight.Black, color = ProstDeep)
                            Text(tr(lang,
                                "Sí. Como segunda capa docente conviene conocer el Prosthodontic Diagnostic Index (PDI) del American College of Prosthodontists, porque Kennedy describe la distribución de espacios edéntulos pero no la complejidad global. Kennedy-Applegate queda como clasificación anatómica principal y PDI como referencia avanzada de complejidad, no como sustituto.",
                                "Yes. As a second teaching layer, the American College of Prosthodontists Prosthodontic Diagnostic Index (PDI) is useful because Kennedy describes edentulous-space distribution, not overall complexity. Kennedy-Applegate remains the main anatomic classification and PDI is an advanced complexity reference, not a replacement."))
                        }
                    }
                }
                item {
                    ProstWriteCard(
                        tr(lang,"Ejemplo: “Maxilar: ${upperK.label}. Mandíbula: ${lowerK.label}. Áreas edéntulas: ___. Pilares candidatos: ___. Pronóstico periodontal/radiográfico de pilares: ___.”",
                            "Example: “Maxilla: ${upperK.label}. Mandible: ${lowerK.label}. Edentulous areas: ___. Candidate abutments: ___. Periodontal/radiographic abutment prognosis: ___.”")
                    )
                }
            }

            ProstTab.RPD -> {
                item {
                    SectionCard(tr(lang,"1 · Tipo de arco y material de base","1 · Arch and base material")) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(upperDesign,{upperDesign=true},{Text(tr(lang,"Maxilar","Maxilla"))},modifier=Modifier.weight(1f))
                            FilterChip(!upperDesign,{upperDesign=false},{Text(tr(lang,"Mandíbula","Mandible"))},modifier=Modifier.weight(1f))
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("flexible" to tr(lang,"Flexible","Flexible"), "acrylic" to tr(lang,"Acrílica","Acrylic"), "metal-acrylic" to tr(lang,"Metal-acrílica","Metal-acrylic")).forEach { (id,label) ->
                                FilterChip(rpdMaterial==id,{onSessionChanged(session.copy(prosthetic = session.prosthetic.copy(rpdMaterial = id)))},{Text(label)},modifier=Modifier.weight(1f))
                            }
                        }
                        Text(rpdMaterialNote(rpdMaterial,lang), style = MaterialTheme.typography.bodySmall)
                    }
                }
                item {
                    SectionCard(tr(lang,"2 · Diseñador de PPR","2 · RPD designer")) {
                        Text(tr(lang,"Selecciona un elemento y toca el diente donde quieres colocarlo. Vuelve a tocar para retirarlo.","Select an element and tap the tooth where you want it. Tap again to remove it."))
                        val tools = listOf(
                            DesignMark.REST_M to tr(lang,"Descanso M","M rest"),
                            DesignMark.REST_D to tr(lang,"Descanso D","D rest"),
                            DesignMark.CINGULUM to tr(lang,"Cingular","Cingulum"),
                            DesignMark.DIRECT to tr(lang,"Ret. directo","Direct ret."),
                            DesignMark.INDIRECT to tr(lang,"Ret. indirecto","Indirect ret."),
                            DesignMark.GUIDE to tr(lang,"Plano guía","Guide plane"),
                            DesignMark.BASE to tr(lang,"Base/malla","Base/mesh")
                        )
                        tools.chunked(3).forEach { row ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                row.forEach { (tool,label) -> FilterChip(designTool==tool,{designTool=tool},{Text(label)},modifier=Modifier.weight(1f)) }
                                repeat(3-row.size){ Spacer(Modifier.weight(1f)) }
                            }
                        }
                        val teeth = if (upperDesign) prostUpper else prostLower
                        val present = if (upperDesign) upperPresent else lowerPresent
                        val marks = if (upperDesign) designUpper else designLower
                        ProstArchRow(teeth,present,marks) { tooth ->
                            val current = marks[tooth].orEmpty()
                            val updated = if (designTool in current) current - designTool else current + designTool
                            val stored = updated.map { it.name }.toSet()
                            val prosthetic = if (upperDesign) session.prosthetic.copy(upperDesignMarks = session.prosthetic.upperDesignMarks + (tooth to stored))
                            else session.prosthetic.copy(lowerDesignMarks = session.prosthetic.lowerDesignMarks + (tooth to stored))
                            onSessionChanged(session.copy(prosthetic = prosthetic))
                        }
                        DesignLegend(lang)
                    }
                }
                item {
                    SectionCard(tr(lang,"3 · Conector mayor","3 · Major connector")) {
                        val choices = if (upperDesign) listOf(
                            "palatal strap" to tr(lang,"Banda palatina","Palatal strap"),
                            "AP palatal" to tr(lang,"Anteroposterior palatino","A-P palatal"),
                            "palatal plate" to tr(lang,"Placa palatina","Palatal plate")
                        ) else listOf(
                            "lingual bar" to tr(lang,"Barra lingual","Lingual bar"),
                            "lingual plate" to tr(lang,"Placa lingual","Lingual plate")
                        )
                        choices.forEach { (id,label) ->
                            FilterChip(majorConnector==id,{
                                val prosthetic = if (upperDesign) session.prosthetic.copy(upperMajorConnector = id)
                                else session.prosthetic.copy(lowerMajorConnector = id)
                                onSessionChanged(session.copy(prosthetic = prosthetic))
                            },{Text(label)},modifier=Modifier.padding(end=4.dp,bottom=4.dp))
                        }
                        Text(tr(lang,
                            "El conector se elige por anatomía, soporte periodontal, extensión de la base, espacio disponible, higiene y biomecánica; no solo por la clase de Kennedy.",
                            "Connector choice depends on anatomy, periodontal support, base extension, available space, hygiene and biomechanics; not only on Kennedy class."))
                    }
                }
                item { RpdProtocolCard(if (upperDesign) upperK else lowerK, lang) }
                item {
                    SectionCard(tr(lang,"Principios que debe comprobar el alumno","Principles the student must verify")) {
                        Text("• ${tr(lang,"Soporte → estabilidad → retención, en ese orden.","Support → stability → retention, in that order.")}")
                        Text("• ${tr(lang,"En extensiones distales, controlar rotación y considerar retención indirecta.","For distal extensions, control rotation and consider indirect retention.")}")
                        Text("• ${tr(lang,"Los descansos transmiten soporte; planos guía, conectores y reciprocación ayudan a estabilidad; el retenedor directo aporta retención.","Rests provide support; guide planes, connectors and reciprocation aid stability; direct retainers provide retention.")}")
                        Text("• ${tr(lang,"El diseño final debe verificarse en modelo analizado/surveyado y bajo supervisión docente.","Final design should be verified on a surveyed cast under faculty supervision.")}")
                    }
                }
                item {
                    ProstWriteCard(
                        tr(lang,
                            "“PPR ${if(upperDesign) "maxilar" else "mandibular"}; ${if(upperDesign) upperK.label else lowerK.label}; base $rpdMaterial; conector mayor $majorConnector; descansos ___; retenedores directos ___; retenedores indirectos ___; planos guía ___; dientes a reemplazar ___.”",
                            "“${if(upperDesign) "Maxillary" else "Mandibular"} RPD; ${if(upperDesign) upperK.label else lowerK.label}; base $rpdMaterial; major connector $majorConnector; rests ___; direct retainers ___; indirect retainers ___; guide planes ___; teeth to replace ___.”")
                    )
                }
            }

            ProstTab.COMPLETE -> {
                item {
                    SectionCard(tr(lang,"Prótesis total · protocolo por etapas","Complete denture · staged protocol")) {
                        val steps = listOf(
                            tr(lang,"Diagnóstico: rebordes, mucosa, frenillos, saliva, apertura, relación intermaxilar, prótesis previa y expectativas.","Diagnosis: ridges, mucosa, frena, saliva, opening, maxillomandibular relation, previous dentures and expectations."),
                            tr(lang,"Impresión preliminar y modelo diagnóstico.","Preliminary impression and diagnostic cast."),
                            tr(lang,"Cubeta individual, modelado periférico e impresión definitiva según protocolo.","Custom tray, border molding and definitive impression per protocol."),
                            tr(lang,"Bases de registro y rodillos: soporte labial, plano oclusal y dimensión vertical.","Record bases and rims: lip support, occlusal plane and vertical dimension."),
                            tr(lang,"Relación céntrica/registro maxilomandibular y montaje cuando corresponda.","Centric relation/maxillomandibular record and mounting when indicated."),
                            tr(lang,"Selección y montaje de dientes; prueba en cera de estética, fonética, soporte y oclusión.","Tooth selection/arrangement; wax try-in of esthetics, phonetics, support and occlusion."),
                            tr(lang,"Procesado, acabado y pulido; inserción con control de presión y oclusión.","Processing, finishing and polishing; insertion with pressure and occlusal checks."),
                            tr(lang,"Indicaciones, adaptación y controles tempranos y posteriores según necesidad.","Instructions, adaptation and early/subsequent reviews as needed.")
                        )
                        steps.forEachIndexed { i,s -> Text("${i+1}. $s") }
                    }
                }
                item { CompleteDentureDiagram(lang) }
                item {
                    ProstWriteCard(
                        tr(lang,"“Edentulismo total ___; condiciones de reborde/mucosa ___; plan: prótesis total ___; etapa realizada ___; hallazgos/ajustes ___; indicaciones y próxima cita ___.”",
                            "“Complete edentulism ___; ridge/mucosa conditions ___; plan: complete denture ___; stage completed ___; findings/adjustments ___; instructions and next visit ___.”")
                    )
                }
            }

            ProstTab.FIXED -> {
                item {
                    SectionCard(tr(lang,"1 · Diseña pilares y pónticos","1 · Design abutments and pontics")) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(fixedUpper,{fixedUpper=true},{Text(tr(lang,"Maxilar","Maxilla"))},modifier=Modifier.weight(1f))
                            FilterChip(!fixedUpper,{fixedUpper=false},{Text(tr(lang,"Mandíbula","Mandible"))},modifier=Modifier.weight(1f))
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(designTool==DesignMark.ABUTMENT,{designTool=DesignMark.ABUTMENT},{Text(tr(lang,"Pilar","Abutment"))},modifier=Modifier.weight(1f))
                            FilterChip(designTool==DesignMark.PONTIC,{designTool=DesignMark.PONTIC},{Text(tr(lang,"Póntico","Pontic"))},modifier=Modifier.weight(1f))
                        }
                        val teeth = if (fixedUpper) prostUpper else prostLower
                        val present = if (fixedUpper) upperPresent else lowerPresent
                        val marks = if (fixedUpper) designUpper else designLower
                        ProstArchRow(teeth,present,marks) { tooth ->
                            val current = marks[tooth].orEmpty()
                            val updated = if (designTool in current) current - designTool else current + designTool
                            val stored = updated.map { it.name }.toSet()
                            val prosthetic = if (fixedUpper) session.prosthetic.copy(upperDesignMarks = session.prosthetic.upperDesignMarks + (tooth to stored))
                            else session.prosthetic.copy(lowerDesignMarks = session.prosthetic.lowerDesignMarks + (tooth to stored))
                            onSessionChanged(session.copy(prosthetic = prosthetic))
                        }
                        Text(tr(lang,"A = pilar · P = póntico. El diseño no valida por sí mismo que el pilar sea biológicamente adecuado.","A = abutment · P = pontic. The design does not by itself validate biologic suitability of an abutment."),style=MaterialTheme.typography.bodySmall)
                    }
                }
                item {
                    SectionCard(tr(lang,"2 · Material y terminación","2 · Material and finish line")) {
                        val materials = listOf(
                            "zirconia" to tr(lang,"Zirconia","Zirconia"),
                            "metal-ceramic" to tr(lang,"Metal-porcelana (PFM/PAM/PPM)","Metal-ceramic (PFM/PAM/PPM)"),
                            "full-metal" to tr(lang,"Metálica","Full metal"),
                            "lithium-disilicate" to tr(lang,"Disilicato de litio","Lithium disilicate")
                        )
                        materials.chunked(2).forEach { row ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                row.forEach { (id,label) -> FilterChip(fixedMaterial==id,{onSessionChanged(session.copy(prosthetic = session.prosthetic.copy(fixedMaterial = id)))},{Text(label)},modifier=Modifier.weight(1f)) }
                            }
                        }
                        FinishLineDiagram(fixedMaterial, lang)
                        Text(fixedMaterialGuide(fixedMaterial,lang))
                        if (fixedMaterial == "metal-ceramic") {
                            Text(tr(lang,
                                "Nota: PFM, PAM y PPM se usan como denominaciones de restauraciones metal-cerámica/porcelana sobre metal; aquí se manejan como la misma familia, no como tres materiales distintos.",
                                "Note: PFM, PAM and PPM are used as terms for metal-ceramic/porcelain-to-metal restorations; they are treated here as one family, not three different materials."),
                                style = MaterialTheme.typography.bodySmall, color = ProstPurple)
                        }
                    }
                }
                item {
                    SectionCard(tr(lang,"3 · Tipo de póntico","3 · Pontic form")) {
                        val pontics = listOf(
                            "modified-ridge-lap" to tr(lang,"Silla de montar modificada","Modified ridge lap"),
                            "ovate" to tr(lang,"Ovoide","Ovate"),
                            "conical" to tr(lang,"Cónico","Conical"),
                            "hygienic" to tr(lang,"Higiénico","Hygienic")
                        )
                        pontics.chunked(2).forEach { row ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                row.forEach { (id,label) -> FilterChip(ponticType==id,{onSessionChanged(session.copy(prosthetic = session.prosthetic.copy(ponticType = id)))},{Text(label)},modifier=Modifier.weight(1f)) }
                            }
                        }
                        PonticDiagram(ponticType, lang)
                        Text(ponticGuide(ponticType,lang), style = MaterialTheme.typography.bodySmall)
                    }
                }
                item {
                    SectionCard(tr(lang,"4 · Protocolo de prótesis fija","4 · Fixed prosthesis protocol")) {
                        val steps = listOf(
                            tr(lang,"Diagnóstico y restaurabilidad: caries, endodoncia, periodonto, relación corona-raíz, ferrule cuando corresponda, oclusión y espacio protésico.","Diagnosis/restorability: caries, endodontics, periodontium, crown-root relation, ferrule when applicable, occlusion and prosthetic space."),
                            tr(lang,"Planificación: pilares, extensión del tramo, pónticos, material y diseño; encerado diagnóstico cuando aporte valor.","Planning: abutments, span length, pontics, material and design; diagnostic wax-up when useful."),
                            tr(lang,"Preparación dentaria conservadora compatible con el material, con eje de inserción, reducción suficiente, ángulos internos redondeados y margen continuo.","Conservative tooth preparation compatible with the material, with path of insertion, adequate reduction, rounded internal angles and continuous margin."),
                            tr(lang,"Provisional: protección pulpar/periodontal, contactos, estética y oclusión.","Provisional: pulpal/periodontal protection, contacts, esthetics and occlusion."),
                            tr(lang,"Impresión o escaneo, registro oclusal y orden al laboratorio con material, color, diseño de póntico y margen.","Impression or scan, occlusal record and lab prescription with material, shade, pontic design and margin."),
                            tr(lang,"Prueba: asentamiento, margen, contactos, perfil de emergencia, póntico-tejido, estética y oclusión.","Try-in: seating, margin, contacts, emergence profile, pontic-tissue relation, esthetics and occlusion."),
                            tr(lang,"Cementación/adhesión según material y fabricante; eliminar excedentes y verificar oclusión.","Cementation/bonding according to material/manufacturer; remove excess and verify occlusion."),
                            tr(lang,"Control: higiene de pilares/pónticos, respuesta periodontal, integridad del material y oclusión.","Review: abutment/pontic hygiene, periodontal response, material integrity and occlusion.")
                        )
                        steps.forEachIndexed { i,s -> Text("${i+1}. $s") }
                    }
                }
                item {
                    NoticeCard(tr(lang,
                        "Las dimensiones exactas de reducción y ancho de la terminación dependen del sistema restaurador y de las instrucciones del fabricante. La app enseña la forma de terminación y el razonamiento; no sustituye la guía técnica específica del material seleccionado.",
                        "Exact reduction and finish-line dimensions depend on the restorative system and manufacturer instructions. The app teaches finish-line form and reasoning; it does not replace the material-specific technical guide."))
                }
                item {
                    ProstWriteCard(
                        tr(lang,
                            "“Prótesis fija en zona ___; pilares ___; pónticos ___; material $fixedMaterial; diseño de póntico $ponticType; terminación ___; provisional ___; impresión/escaneo ___; prueba ___; cementación/adhesión ___; control ___.”",
                            "“Fixed prosthesis in area ___; abutments ___; pontics ___; material $fixedMaterial; pontic design $ponticType; finish line ___; provisional ___; impression/scan ___; try-in ___; cementation/bonding ___; review ___.”")
                    )
                }
            }
        }
    }
}

@Composable
private fun ProstWriteCard(text:String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ProstMint.copy(alpha = .18f)),
        border = BorderStroke(1.dp, ProstTurquoise),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("✍️ ${if(text.startsWith("Example")) "What do I write in the record?" else "¿Qué escribo al final en el expediente?"}", fontWeight = FontWeight.Black, color = ProstDeep)
            Text(text)
        }
    }
}

@Composable
private fun KennedyResultCard(r: KennedyResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ProstMint.copy(alpha = .18f)),
        border = BorderStroke(1.dp, ProstTurquoise),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(r.label, fontWeight = FontWeight.Black, color = ProstDeep)
            if (r.explanation.isNotBlank()) Text(r.explanation, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ProstArchRow(teeth: List<Int>, present: Set<Int>, marks: Map<Int, Set<DesignMark>>, onTap: (Int)->Unit) {
    teeth.chunked(7).forEach { row ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            row.forEach { tooth ->
                val toothMarks = marks[tooth].orEmpty()
                Card(
                    modifier = Modifier.weight(1f).clickable { onTap(tooth) },
                    colors = CardDefaults.cardColors(containerColor = when {
                        tooth !in present -> Color(0xFFF6D8E0)
                        toothMarks.isNotEmpty() -> ProstMint.copy(alpha=.30f)
                        else -> ProstPaper
                    }),
                    border = BorderStroke(1.dp, if (toothMarks.isNotEmpty()) ProstTurquoise else ProstLilac)
                ) {
                    Column(Modifier.fillMaxWidth().padding(vertical=6.dp,horizontal=2.dp), horizontalAlignment=Alignment.CenterHorizontally) {
                        Text(if (tooth in present) "🦷" else "✕", style = MaterialTheme.typography.bodyMedium)
                        Text(tooth.toString(), fontWeight=FontWeight.Bold, style=MaterialTheme.typography.labelSmall)
                        if (toothMarks.isNotEmpty()) Text(toothMarks.joinToString("") { markSymbol(it) }, style=MaterialTheme.typography.labelSmall, textAlign=TextAlign.Center)
                    }
                }
            }
            repeat(7-row.size){ Spacer(Modifier.weight(1f)) }
        }
        Spacer(Modifier.height(4.dp))
    }
}

private fun markSymbol(mark: DesignMark): String = when(mark) {
    DesignMark.REST_M -> "M◉"
    DesignMark.REST_D -> "D◉"
    DesignMark.CINGULUM -> "C◉"
    DesignMark.DIRECT -> "⌒"
    DesignMark.INDIRECT -> "◇"
    DesignMark.GUIDE -> "▥"
    DesignMark.BASE -> "▦"
    DesignMark.PONTIC -> "P"
    DesignMark.ABUTMENT -> "A"
}

@Composable
private fun DesignLegend(lang: String) {
    Text(tr(lang,"Leyenda: M◉ descanso mesial · D◉ descanso distal · C◉ descanso cingular · ⌒ retenedor directo · ◇ indirecto · ▥ plano guía · ▦ base/malla.",
        "Legend: M◉ mesial rest · D◉ distal rest · C◉ cingulum rest · ⌒ direct retainer · ◇ indirect retainer · ▥ guide plane · ▦ base/mesh."), style=MaterialTheme.typography.bodySmall)
}

private fun rpdMaterialNote(id:String,lang:String):String = when(id){
    "flexible" -> tr(lang,"Flexible: puede mejorar comodidad/estética en casos seleccionados, pero no sustituye automáticamente un armazón rígido cuando el control biomecánico requiere descansos, reciprocación y retención indirecta.","Flexible: may improve comfort/esthetics in selected cases, but it does not automatically replace a rigid framework when biomechanical control requires rests, reciprocation and indirect retention.")
    "acrylic" -> tr(lang,"Acrílica: útil como solución provisional/transicional o en indicaciones seleccionadas; suele ser más voluminosa y el diseño debe proteger tejidos y dientes remanentes.","Acrylic: useful as provisional/transitional treatment or in selected indications; usually bulkier and must be designed to protect tissues and remaining teeth.")
    else -> tr(lang,"Metal-acrílica: permite un armazón rígido con descansos, conectores, retenedores y bases acrílicas; facilita aplicar principios de soporte, estabilidad, reciprocación y retención indirecta.","Metal-acrylic: provides a rigid framework with rests, connectors, retainers and acrylic bases; facilitates support, stability, reciprocation and indirect retention principles.")
}

@Composable
private fun RpdProtocolCard(k:KennedyResult,lang:String){
    val distal = k.classNumber == 1 || k.classNumber == 2
    SectionCard(tr(lang,"4 · Protocolo sugerido según soporte","4 · Suggested protocol by support")) {
        Text(k.label, fontWeight=FontWeight.Black, color=ProstPurple)
        if(distal){
            Text(tr(lang,"Caso con extensión distal: diagnóstico y survey → preparaciones preprotésicas → descansos que reduzcan torque según el diseño → planos guía/proximal plate → retenedores con liberación de tensiones cuando se indiquen → conector mayor rígido → retención indirecta → impresión de la extensión según protocolo → prueba de armazón → relación maxilomandibular → prueba en cera → procesado → inserción y controles.",
                "Distal-extension case: diagnosis/survey → mouth preparation → rests that reduce torque as indicated → guide planes/proximal plate → stress-releasing retention when indicated → rigid major connector → indirect retention → distal-extension impression per protocol → framework try-in → jaw relation → wax try-in → processing → insertion and reviews."))
        } else {
            Text(tr(lang,"Caso principalmente dentosoportado: diagnóstico y survey → definir eje de inserción → descansos adyacentes a espacios → planos guía → conector mayor → retenedores directos/reciprocación → base/malla → impresión definitiva → prueba de armazón → registros/prueba en cera → procesado → inserción y controles.",
                "Mainly tooth-supported case: diagnosis/survey → define path of insertion → rests adjacent to spaces → guide planes → major connector → direct retainers/reciprocation → base/mesh → definitive impression → framework try-in → records/wax try-in → processing → insertion and reviews."))
        }
    }
}

@Composable
private fun CompleteDentureDiagram(lang:String){
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=ProstLavender.copy(alpha=.45f)),shape=RoundedCornerShape(18.dp)){
        Column(Modifier.padding(14.dp),horizontalAlignment=Alignment.CenterHorizontally){
            Text(tr(lang,"Imagen guía · rebordes edéntulos","Guide image · edentulous ridges"),fontWeight=FontWeight.Black,color=ProstDeep)
            Canvas(Modifier.fillMaxWidth().height(170.dp)){
                val w=size.width; val h=size.height
                val upper=Path().apply{ moveTo(w*.15f,h*.42f); cubicTo(w*.22f,h*.05f,w*.78f,h*.05f,w*.85f,h*.42f); cubicTo(w*.78f,h*.62f,w*.22f,h*.62f,w*.15f,h*.42f); close() }
                val lower=Path().apply{ moveTo(w*.22f,h*.72f); cubicTo(w*.34f,h*.98f,w*.66f,h*.98f,w*.78f,h*.72f); cubicTo(w*.68f,h*.58f,w*.32f,h*.58f,w*.22f,h*.72f); close() }
                drawPath(upper,ProstLilac); drawPath(upper,ProstDeep,style=Stroke(4f))
                drawPath(lower,ProstMint.copy(alpha=.55f)); drawPath(lower,ProstDeep,style=Stroke(4f))
                drawLine(ProstTurquoise,Offset(w*.5f,h*.12f),Offset(w*.5f,h*.88f),strokeWidth=3f)
            }
            Text(tr(lang,"Valora soporte, extensión funcional, frenillos, zonas de alivio, relación maxilomandibular y oclusión antes de diseñar.","Assess support, functional extension, frena, relief areas, maxillomandibular relation and occlusion before design."),style=MaterialTheme.typography.bodySmall)
        }
    }
}

private fun fixedMaterialGuide(id:String,lang:String):String = when(id){
    "zirconia" -> tr(lang,"Zirconia: hombro redondeado o chaflán bien definido, ángulos internos redondeados y reducción uniforme suficiente para el sistema. Evita márgenes irregulares y socavados; sigue el espesor mínimo del fabricante.","Zirconia: rounded shoulder or well-defined chamfer, rounded internal angles and uniform reduction sufficient for the system. Avoid irregular margins and undercuts; follow manufacturer minimum thickness.")
    "metal-ceramic" -> tr(lang,"Metal-porcelana: en zona estética suele requerir hombro redondeado o chaflán profundo para dar espacio a metal + cerámica; si existe margen lingual metálico, puede usarse chaflán más conservador. El diseño exacto depende del margen cerámico/metálico y del laboratorio.","Metal-ceramic: esthetic surfaces commonly need a rounded shoulder or heavy chamfer to provide room for metal + ceramic; a lingual metal margin may use a more conservative chamfer. Exact design depends on ceramic/metal margin and laboratory system.")
    "full-metal" -> tr(lang,"Metálica: chaflán continuo y conservador, con bisel de cúspide funcional cuando corresponda y reducción suficiente para la aleación sin sobrecontornear.","Full metal: continuous conservative chamfer, functional cusp bevel when indicated and enough reduction for the alloy without overcontouring.")
    else -> tr(lang,"Disilicato de litio: hombro redondeado o chaflán profundo continuo, sin ángulos internos agudos ni biseles finos; requiere espesor cerámico suficiente y estrategia adhesiva/cementación acorde al sistema.","Lithium disilicate: continuous rounded shoulder or deep chamfer, no sharp internal angles or thin bevels; requires adequate ceramic thickness and a bonding/cementation strategy compatible with the system.")
}

@Composable
private fun FinishLineDiagram(id:String,lang:String){
    val type = when(id){ "full-metal" -> "chamfer"; "metal-ceramic" -> "mixed"; else -> "rounded" }
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=ProstLavender.copy(alpha=.40f)),shape=RoundedCornerShape(16.dp)){
        Column(Modifier.padding(12.dp),horizontalAlignment=Alignment.CenterHorizontally){
            Text(tr(lang,"Imagen guía de terminación","Finish-line guide image"),fontWeight=FontWeight.Bold,color=ProstDeep)
            Canvas(Modifier.fillMaxWidth().height(150.dp)){
                val baseY=size.height*.78f; val cx=size.width*.5f
                drawLine(ProstMetal,Offset(size.width*.08f,baseY),Offset(size.width*.92f,baseY),strokeWidth=10f)
                val tooth=Path().apply{
                    moveTo(cx-size.width*.18f,size.height*.10f)
                    lineTo(cx-size.width*.14f,baseY-size.height*.16f)
                    if(type=="chamfer") quadraticBezierTo(cx-size.width*.12f,baseY-size.height*.03f,cx-size.width*.05f,baseY)
                    else lineTo(cx-size.width*.08f,baseY-size.height*.03f)
                    if(type!="chamfer") quadraticBezierTo(cx-size.width*.02f,baseY,cx+size.width*.02f,baseY)
                    if(type=="mixed") lineTo(cx+size.width*.10f,baseY-size.height*.02f)
                    else quadraticBezierTo(cx+size.width*.10f,baseY-size.height*.03f,cx+size.width*.12f,baseY-size.height*.12f)
                    lineTo(cx+size.width*.18f,size.height*.10f)
                    close()
                }
                drawPath(tooth,ProstPaper); drawPath(tooth,ProstDeep,style=Stroke(5f))
                drawLine(ProstTurquoise,Offset(cx-size.width*.24f,baseY),Offset(cx+size.width*.24f,baseY),strokeWidth=5f)
            }
            Text(when(type){
                "chamfer" -> tr(lang,"Chaflán continuo","Continuous chamfer")
                "mixed" -> tr(lang,"Hombro/chaflán según cara y diseño del margen","Shoulder/chamfer according to surface and margin design")
                else -> tr(lang,"Hombro redondeado o chaflán profundo","Rounded shoulder or deep chamfer")
            },fontWeight=FontWeight.Bold)
        }
    }
}

private fun ponticGuide(id:String,lang:String):String = when(id){
    "ovate" -> tr(lang,"Ovoide: opción estética cuando la arquitectura del reborde y el acondicionamiento de tejidos lo permiten; requiere higiene y control cuidadosos.","Ovate: esthetic option when ridge architecture and tissue conditioning allow; requires careful hygiene and maintenance.")
    "hygienic" -> tr(lang,"Higiénico: sin contacto con el reborde; se reserva para zonas posteriores no estéticas con espacio suficiente para higiene.","Hygienic: no ridge contact; reserved for non-esthetic posterior areas with adequate hygiene clearance.")
    "conical" -> tr(lang,"Cónico: contacto pequeño y convexo; puede ser útil en rebordes delgados posteriores, manteniendo acceso a higiene.","Conical: small convex contact; may be useful on thin posterior ridges while preserving hygiene access.")
    else -> tr(lang,"Silla de montar modificada: contacto vestibular/crestal con superficie lingual despejada para higiene; frecuente en zonas estéticas y premolares.","Modified ridge lap: facial/crestal contact with lingual clearance for hygiene; common in esthetic and premolar regions.")
}

@Composable
private fun PonticDiagram(id:String,lang:String){
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=ProstMint.copy(alpha=.14f)),shape=RoundedCornerShape(16.dp)){
        Column(Modifier.padding(12.dp),horizontalAlignment=Alignment.CenterHorizontally){
            Canvas(Modifier.fillMaxWidth().height(130.dp)){
                val ridgeY=size.height*.78f
                drawRoundRect(ProstLilac,topLeft=Offset(size.width*.08f,ridgeY),size=Size(size.width*.84f,size.height*.15f),cornerRadius=CornerRadius(30f,30f))
                val cx=size.width*.5f
                val p=Path().apply{
                    moveTo(cx-size.width*.14f,size.height*.15f)
                    quadraticBezierTo(cx,size.height*.02f,cx+size.width*.14f,size.height*.15f)
                    lineTo(cx+size.width*.12f,size.height*.58f)
                    when(id){
                        "hygienic" -> { lineTo(cx+size.width*.08f,size.height*.62f); lineTo(cx-size.width*.08f,size.height*.62f) }
                        "conical" -> { quadraticBezierTo(cx,size.height*.80f,cx-size.width*.06f,size.height*.58f) }
                        "ovate" -> { quadraticBezierTo(cx,size.height*.92f,cx-size.width*.10f,size.height*.58f) }
                        else -> { quadraticBezierTo(cx-size.width*.02f,size.height*.82f,cx-size.width*.12f,size.height*.58f) }
                    }
                    close()
                }
                drawPath(p,ProstPaper); drawPath(p,ProstDeep,style=Stroke(4f))
            }
            Text(tr(lang,"Esquema didáctico del contacto póntico-reborde","Teaching schematic of pontic-ridge contact"),style=MaterialTheme.typography.bodySmall)
        }
    }
}
