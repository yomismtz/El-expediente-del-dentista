package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private val P3Paper = Color(0xFFFFFCFF)
private val P3Lilac = Color(0xFFD7C4EA)
private val P3Purple = Color(0xFF7447A3)
private val P3Deep = Color(0xFF43235F)
private val P3Mint = Color(0xFFE3F7F3)
private val P3Turquoise = Color(0xFF2EB9B1)
private val P3Metal = Color(0xFFAAA3B2)

private val p3Upper = listOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27)
private val p3Lower = listOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37)

private data class P3Space(
    val teeth: List<Int>,
    val rightEnd: Boolean,
    val leftEnd: Boolean,
    val crossesMidline: Boolean,
    val description: String
)

private data class P3Kennedy(
    val label: String,
    val classNumber: Int,
    val modifications: Int,
    val explanation: String,
    val spaces: List<P3Space>,
    val steps: List<String>
)

private fun p3Segments(arch: List<Int>, present: Set<Int>): List<List<Int>> {
    val out = mutableListOf<List<Int>>()
    var current = mutableListOf<Int>()
    arch.forEach { tooth ->
        if (tooth !in present) current.add(tooth)
        else if (current.isNotEmpty()) {
            out.add(current.toList())
            current = mutableListOf()
        }
    }
    if (current.isNotEmpty()) out.add(current.toList())
    return out
}

private fun p3Range(teeth: List<Int>): String = when (teeth.size) {
    0 -> "—"
    1 -> teeth.first().toString()
    else -> "${teeth.first()}–${teeth.last()}"
}

private fun p3Kennedy(
    arch: List<Int>,
    present: Set<Int>,
    ignoredSecondMolars: Set<Int>,
    lang: String
): P3Kennedy {
    val effectiveArch = arch.filterNot { it in ignoredSecondMolars }
    val effectivePresent = present.intersect(effectiveArch.toSet())
    val upper = arch.first() == 17
    val midlinePair = if (upper) setOf(11,21) else setOf(41,31)

    if (effectivePresent.isEmpty()) {
        return P3Kennedy(
            tr(lang,"Edéntulo total · Kennedy no aplica","Completely edentulous · Kennedy does not apply"),
            0,0,
            tr(lang,"No quedan dientes en el arco efectivo. Kennedy se usa para arcos parcialmente edéntulos.","No teeth remain in the effective arch. Kennedy is used for partially edentulous arches."),
            listOf(P3Space(effectiveArch,true,true,true,tr(lang,"Arco completamente edéntulo","Completely edentulous arch"))),
            listOf(
                tr(lang,"1. Regla 1: representa el arco después de las extracciones que lo modificarán.","1. Rule 1: represent the arch after extractions that will alter it."),
                tr(lang,"2. Los terceros molares no se muestran en este ejercicio.","2. Third molars are not displayed in this exercise."),
                tr(lang,"3. Resultado: edentulismo total; Kennedy no corresponde.","3. Result: complete edentulism; Kennedy does not apply.")
            )
        )
    }

    val raw = p3Segments(effectiveArch,effectivePresent)
    if (raw.isEmpty()) {
        return P3Kennedy(
            tr(lang,"Sin áreas edéntulas","No edentulous areas"),0,0,
            tr(lang,"Todos los dientes del arco efectivo están presentes.","All teeth in the effective arch are present."),
            emptyList(),
            listOf(tr(lang,"No se detectaron espacios edéntulos; Kennedy no es necesaria.","No edentulous spaces were detected; Kennedy is not required."))
        )
    }

    val spaces = raw.map { seg ->
        val start = effectiveArch.indexOf(seg.first())
        val end = effectiveArch.indexOf(seg.last())
        val rightEnd = start == 0
        val leftEnd = end == effectiveArch.lastIndex
        val crosses = midlinePair.all { it in seg }
        val desc = when {
            rightEnd && leftEnd -> tr(lang,"Abarca todo el arco efectivo","Spans the entire effective arch")
            rightEnd -> tr(lang,"Extensión distal derecha","Right distal extension")
            leftEnd -> tr(lang,"Extensión distal izquierda","Left distal extension")
            crosses -> tr(lang,"Espacio anterior que cruza la línea media","Anterior space crossing the midline")
            else -> tr(lang,"Espacio limitado por dientes","Tooth-bounded edentulous space")
        }
        P3Space(seg,rightEnd,leftEnd,crosses,desc)
    }

    val terminalCount = spaces.count { it.rightEnd || it.leftEnd }
    val cls = when {
        terminalCount >= 2 -> 1
        terminalCount == 1 -> 2
        spaces.size == 1 && spaces.first().crossesMidline -> 4
        else -> 3
    }
    val mods = when (cls) {
        1 -> (spaces.size - 2).coerceAtLeast(0)
        2,3 -> (spaces.size - 1).coerceAtLeast(0)
        else -> 0
    }
    val roman = listOf("","I","II","III","IV")[cls]
    val label = "Kennedy $roman" + if (mods > 0) tr(lang," · modificación $mods"," · modification $mods") else ""
    val explanation = when (cls) {
        1 -> tr(lang,
            "Hay extensiones distales posteriores bilaterales. Esas dos áreas determinan la Clase I; cualquier espacio adicional es modificación.",
            "There are bilateral posterior distal extensions. Those two areas determine Class I; any additional space is a modification.")
        2 -> tr(lang,
            "Hay una sola extensión distal posterior. Esa área más posterior determina la Clase II; los demás espacios son modificaciones.",
            "There is one posterior distal extension. That most posterior area determines Class II; other spaces are modifications.")
        3 -> tr(lang,
            "El espacio determinante está limitado por dientes por delante y por detrás. Los demás espacios son modificaciones.",
            "The determining space is bounded by teeth anteriorly and posteriorly. Other spaces are modifications.")
        else -> tr(lang,
            "Existe un único espacio anterior que cruza la línea media y no hay un espacio posterior que cambie la clase. Por eso es Clase IV y no admite modificaciones.",
            "There is a single anterior space crossing the midline and no posterior space that changes the class. Therefore it is Class IV and has no modifications.")
    }

    val ignoredText = if (ignoredSecondMolars.isEmpty()) tr(lang,"ninguno","none") else ignoredSecondMolars.sorted().joinToString(", ")
    val spacesText = spaces.joinToString("; ") { "${p3Range(it.teeth)}: ${it.description}" }
    val determinant = when (cls) {
        1 -> tr(lang,"las dos extensiones distales","the two distal extensions")
        2 -> tr(lang,"la extensión distal más posterior","the most posterior distal extension")
        3 -> tr(lang,"el espacio limitado más posterior","the most posterior bounded space")
        else -> tr(lang,"el único espacio anterior que cruza la línea media","the single anterior space crossing the midline")
    }

    val steps = listOf(
        tr(lang,"1. Regla 1: marca el arco final después de las extracciones que modificarán la clasificación.","1. Rule 1: mark the final arch after extractions that will alter the classification."),
        tr(lang,"2. Reglas 2 y 3: los terceros molares no se incluyen en la arcada mostrada; si uno fuera pilar real, debe valorarse aparte.","2. Rules 2 and 3: third molars are not included in the displayed arch; if one is a real abutment, evaluate it separately."),
        tr(lang,"3. Regla 4: segundos molares ausentes seleccionados como 'no reemplazar': $ignoredText.","3. Rule 4: missing second molars selected as 'do not replace': $ignoredText."),
        tr(lang,"4. Regla 5: espacios detectados automáticamente: $spacesText.","4. Rule 5: automatically detected spaces: $spacesText."),
        tr(lang,"5. El área que determina la clase es: $determinant.","5. The area determining the class is: $determinant."),
        tr(lang,"6. Reglas 6 y 7: las áreas adicionales se cuentan por número, no por tamaño. Modificaciones: $mods.","6. Rules 6 and 7: additional areas are counted by number, not size. Modifications: $mods."),
        if (cls == 4)
            tr(lang,"7. Regla 8: la Clase IV no admite modificaciones.","7. Rule 8: Class IV does not permit modifications.")
        else
            tr(lang,"7. Regla 8: la restricción de Clase IV no aplica.","7. Rule 8: the Class IV restriction does not apply."),
        tr(lang,"8. Resultado orientativo: $label.","8. Educational result: $label.")
    )
    return P3Kennedy(label,cls,mods,explanation,spaces,steps)
}

@Composable
fun ProstheticInteractiveV3Screen(lang: String, onBack: () -> Unit) {
    var showFullModule by remember { mutableStateOf(false) }
    if (showFullModule) {
        ProstheticInteractiveV2Screen(lang) { showFullModule = false }
        return
    }

    var upperPresent by remember { mutableStateOf(p3Upper.toSet()) }
    var lowerPresent by remember { mutableStateOf(p3Lower.toSet()) }
    var upperIgnored by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var lowerIgnored by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var seibert by remember { mutableStateOf(0) }
    var retainer by remember { mutableStateOf("RPI") }
    var useUpperForRetainer by remember { mutableStateOf(true) }

    val upperK = p3Kennedy(p3Upper,upperPresent,upperIgnored,lang)
    val lowerK = p3Kennedy(p3Lower,lowerPresent,lowerIgnored,lang)
    val selectedK = if (useUpperForRetainer) upperK else lowerK

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang,"Prótesis v0.14 · Kennedy, Seibert y retenedores","Prosthodontics v0.14 · Kennedy, Seibert and clasp assemblies"),
                onBack,
                tr(lang,
                    "Toca cada diente, detecta espacios edéntulos, aplica Applegate paso a paso y practica defectos de reborde y retenedores de PPR.",
                    "Tap each tooth, detect edentulous spaces, apply Applegate step by step and practice ridge defects and RPD clasp assemblies.")
            )
        }

        item {
            P3Section(tr(lang,"1 · Dos arcadas interactivas","1 · Two interactive arches")) {
                Text(tr(lang,"Toca cada diente: P = presente, A = ausente. El análisis cambia al instante.","Tap each tooth: P = present, A = missing. The analysis updates immediately."))

                Text(tr(lang,"MAXILAR","MAXILLA"),fontWeight=FontWeight.Black,color=P3Deep)
                P3Arch(p3Upper,upperPresent,lang) { tooth ->
                    val becomingPresent = tooth !in upperPresent
                    upperPresent = if (tooth in upperPresent) upperPresent - tooth else upperPresent + tooth
                    if (becomingPresent) upperIgnored = upperIgnored - tooth
                }
                P3SecondMolarRule(lang,listOf(17,27),upperPresent,upperIgnored) { tooth ->
                    upperIgnored = if (tooth in upperIgnored) upperIgnored - tooth else upperIgnored + tooth
                }
                P3KennedyCard(upperK,lang)

                Spacer(Modifier.height(8.dp))
                Text(tr(lang,"MANDÍBULA","MANDIBLE"),fontWeight=FontWeight.Black,color=P3Deep)
                P3Arch(p3Lower,lowerPresent,lang) { tooth ->
                    val becomingPresent = tooth !in lowerPresent
                    lowerPresent = if (tooth in lowerPresent) lowerPresent - tooth else lowerPresent + tooth
                    if (becomingPresent) lowerIgnored = lowerIgnored - tooth
                }
                P3SecondMolarRule(lang,listOf(47,37),lowerPresent,lowerIgnored) { tooth ->
                    lowerIgnored = if (tooth in lowerIgnored) lowerIgnored - tooth else lowerIgnored + tooth
                }
                P3KennedyCard(lowerK,lang)
            }
        }

        item {
            P3Section(tr(lang,"2 · Espacios edéntulos detectados automáticamente","2 · Automatically detected edentulous spaces")) {
                P3Spaces(tr(lang,"Maxilar","Maxilla"),upperK,lang)
                P3Spaces(tr(lang,"Mandíbula","Mandible"),lowerK,lang)
            }
        }

        item {
            P3Section(tr(lang,"3 · Reglas de Applegate aplicadas paso a paso","3 · Applegate rules applied step by step")) {
                Text(tr(lang,"MAXILAR","MAXILLA"),fontWeight=FontWeight.Black,color=P3Purple)
                upperK.steps.forEach { Text(it,style=MaterialTheme.typography.bodySmall) }
                Spacer(Modifier.height(8.dp))
                Text(tr(lang,"MANDÍBULA","MANDIBLE"),fontWeight=FontWeight.Black,color=P3Purple)
                lowerK.steps.forEach { Text(it,style=MaterialTheme.typography.bodySmall) }
            }
        }

        item {
            P3Section(tr(lang,"4 · Seibert · defecto del reborde residual","4 · Seibert · residual ridge defect")) {
                Text(tr(lang,
                    "Seibert complementa el análisis del sitio protésico; no reemplaza a Kennedy.",
                    "Seibert complements prosthetic-site analysis; it does not replace Kennedy."))
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                    listOf(0 to tr(lang,"Sin defecto","No defect"),1 to "I",2 to "II",3 to "III").forEach { (id,label) ->
                        FilterChip(seibert==id,{seibert=id},{Text(label)},modifier=Modifier.weight(1f))
                    }
                }
                P3SeibertDiagram(seibert)
                Text(p3SeibertText(seibert,lang),fontWeight=FontWeight.SemiBold)
                Text(tr(lang,
                    "Relaciona el defecto con el diseño del póntico, el acceso a higiene y el posible manejo de tejidos.",
                    "Relate the defect to pontic design, hygiene access and possible tissue management."),
                    style=MaterialTheme.typography.bodySmall)
            }
        }

        item {
            P3Section(tr(lang,"5 · Diseñador visual de retenedores PPR","5 · Visual RPD clasp designer")) {
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                    FilterChip(useUpperForRetainer,{useUpperForRetainer=true},{Text(tr(lang,"Maxilar","Maxilla"))},modifier=Modifier.weight(1f))
                    FilterChip(!useUpperForRetainer,{useUpperForRetainer=false},{Text(tr(lang,"Mandíbula","Mandible"))},modifier=Modifier.weight(1f))
                }
                listOf(
                    "Akers" to tr(lang,"Akers / circunferencial","Akers / circumferential"),
                    "RPI" to "RPI",
                    "RPA" to "RPA",
                    "I-bar" to tr(lang,"Barra I","I-bar"),
                    "Combination" to tr(lang,"Combinado · alambre forjado","Combination · wrought wire")
                ).forEach { (id,label) ->
                    FilterChip(retainer==id,{retainer=id},{Text(label)},modifier=Modifier.fillMaxWidth())
                }
                P3RetainerDiagram(retainer)
                Text(p3RetainerText(retainer,lang),style=MaterialTheme.typography.bodySmall)
                Card(
                    colors=CardDefaults.cardColors(containerColor=P3Mint),
                    border=BorderStroke(1.dp,P3Turquoise),
                    shape=RoundedCornerShape(14.dp),
                    modifier=Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                        Text(tr(lang,"Sugerencia didáctica según el arco marcado","Teaching suggestion from the marked arch"),fontWeight=FontWeight.Black,color=P3Deep)
                        Text(p3RetainerSuggestion(selectedK,lang))
                    }
                }
                Text(tr(lang,
                    "Leyenda: morado = apoyo/descanso; turquesa = elemento retentivo; gris = reciprocación o placa proximal; oscuro = conector menor.",
                    "Legend: purple = support/rest; turquoise = retentive element; gray = reciprocation or proximal plate; dark = minor connector."),
                    style=MaterialTheme.typography.bodySmall)
            }
        }

        item {
            P3Section(tr(lang,"6 · Qué escribir al final","6 · What to write at the end")) {
                Text(tr(lang,
                    "Maxilar: ${upperK.label}; espacios ${p3SpaceSummary(upperK,lang)}. Mandíbula: ${lowerK.label}; espacios ${p3SpaceSummary(lowerK,lang)}. Seibert: ${if(seibert==0) "—" else seibert}. Retenedor a discutir: $retainer. Completar pilares, descansos, planos guía, conector mayor, base y retención indirecta según biomecánica.",
                    "Maxilla: ${upperK.label}; spaces ${p3SpaceSummary(upperK,lang)}. Mandible: ${lowerK.label}; spaces ${p3SpaceSummary(lowerK,lang)}. Seibert: ${if(seibert==0) "—" else seibert}. Clasp to discuss: $retainer. Complete abutments, rests, guide planes, major connector, base and indirect retention according to biomechanics."))
                Text(tr(lang,
                    "Resultado educativo, no diagnóstico definitivo. Confirmar con examen periodontal, radiográfico, oclusal, paralelizado y supervisión docente.",
                    "Educational result, not a definitive diagnosis. Confirm with periodontal, radiographic, occlusal and surveying assessment plus faculty supervision."),
                    style=MaterialTheme.typography.bodySmall)
            }
        }

        item {
            Button(onClick={showFullModule=true},modifier=Modifier.fillMaxWidth()) {
                Text(tr(lang,
                    "Abrir módulo completo · PPR, total, fija, materiales, terminaciones y pónticos",
                    "Open complete module · RPD, complete, fixed, materials, finish lines and pontics"),
                    textAlign=TextAlign.Center)
            }
            Spacer(Modifier.height(6.dp))
            OutlinedButton(onClick=onBack,modifier=Modifier.fillMaxWidth()) {
                Text(tr(lang,"Volver a Nota de ingreso","Back to intake note"))
            }
        }
    }
}

@Composable
private fun P3Section(title:String,content:@Composable ()->Unit) {
    Card(
        modifier=Modifier.fillMaxWidth(),
        colors=CardDefaults.cardColors(containerColor=P3Paper),
        border=BorderStroke(1.dp,P3Lilac),
        shape=RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
            Text(title,fontWeight=FontWeight.Black,color=P3Deep,style=MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
private fun P3Arch(arch:List<Int>,present:Set<Int>,lang:String,onTooth:(Int)->Unit) {
    arch.chunked(7).forEach { teeth ->
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(4.dp)) {
            teeth.forEach { tooth ->
                val isPresent = tooth in present
                FilterChip(
                    selected=!isPresent,
                    onClick={onTooth(tooth)},
                    label={
                        Column {
                            Text(tooth.toString(),fontWeight=FontWeight.Bold)
                            Text(if(isPresent) "P" else "A",style=MaterialTheme.typography.labelSmall)
                        }
                    },
                    modifier=Modifier.weight(1f)
                )
            }
        }
    }
    Text(tr(lang,"P = presente · A = ausente","P = present · A = missing"),style=MaterialTheme.typography.labelSmall)
}

@Composable
private fun P3SecondMolarRule(lang:String,candidates:List<Int>,present:Set<Int>,ignored:Set<Int>,onToggle:(Int)->Unit) {
    val absent = candidates.filter { it !in present }
    if(absent.isEmpty()) return
    Text(tr(lang,
        "Applegate: si un segundo molar ausente NO se reemplazará, selecciónalo para excluirlo de la clasificación:",
        "Applegate: if a missing second molar will NOT be replaced, select it to exclude it from classification:"),
        style=MaterialTheme.typography.bodySmall)
    absent.forEach { tooth ->
        FilterChip(
            selected=tooth in ignored,
            onClick={onToggle(tooth)},
            label={Text("$tooth · ${tr(lang,"no reemplazar","do not replace")}")},
            modifier=Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun P3KennedyCard(k:P3Kennedy,lang:String) {
    Card(
        colors=CardDefaults.cardColors(containerColor=P3Mint),
        border=BorderStroke(1.dp,P3Turquoise),
        shape=RoundedCornerShape(14.dp),
        modifier=Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
            Text(k.label,fontWeight=FontWeight.Black,color=P3Deep,style=MaterialTheme.typography.titleMedium)
            Text(k.explanation)
            Text(tr(lang,"Modificaciones calculadas: ${k.modifications}.","Calculated modifications: ${k.modifications}."),style=MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun P3Spaces(name:String,k:P3Kennedy,lang:String) {
    Text(name,fontWeight=FontWeight.Black,color=P3Purple)
    if(k.spaces.isEmpty()) Text(tr(lang,"Sin espacios detectados.","No spaces detected."))
    else k.spaces.forEachIndexed { index,space ->
        Text("${index+1}. ${p3Range(space.teeth)} · ${space.description}")
    }
}

private fun p3SpaceSummary(k:P3Kennedy,lang:String):String =
    if(k.spaces.isEmpty()) tr(lang,"ninguno","none") else k.spaces.joinToString(" / "){p3Range(it.teeth)}

private fun p3SeibertText(cls:Int,lang:String):String = when(cls) {
    1 -> tr(lang,"Seibert I: pérdida de anchura bucolingual/labiolingual con altura apicocoronal relativamente conservada.","Seibert I: buccolingual/labiolingual width loss with relatively preserved apicocoronal height.")
    2 -> tr(lang,"Seibert II: pérdida de altura apicocoronal con anchura bucolingual relativamente conservada.","Seibert II: apicocoronal height loss with relatively preserved buccolingual width.")
    3 -> tr(lang,"Seibert III: pérdida combinada de anchura y altura del reborde.","Seibert III: combined loss of ridge width and height.")
    else -> tr(lang,"Sin defecto seleccionado.","No defect selected.")
}

@Composable
private fun P3SeibertDiagram(cls:Int) {
    Canvas(Modifier.fillMaxWidth().height(145.dp)) {
        val mid=size.width/2f
        val base=size.height*.75f
        val normalW=size.width*.42f
        val normalH=size.height*.42f
        val w=normalW*(if(cls==1||cls==3).58f else 1f)
        val h=normalH*(if(cls==2||cls==3).58f else 1f)
        drawRoundRect(P3Lilac,Offset(mid-w/2f,base-h),Size(w,h),CornerRadius(28f,28f))
        drawRoundRect(P3Purple,Offset(mid-normalW/2f,base-normalH),Size(normalW,normalH),CornerRadius(28f,28f),style=Stroke(width=4f))
        drawLine(P3Deep,Offset(mid-normalW/2f,base+10f),Offset(mid+normalW/2f,base+10f),strokeWidth=3f)
    }
}

private fun p3RetainerText(id:String,lang:String):String = when(id) {
    "Akers" -> tr(lang,
        "Akers/circunferencial: conjunto suprabuldge con apoyo, brazo retentivo y componente recíproco; frecuente en situaciones dentosoportadas.",
        "Akers/circumferential: suprabulge assembly with rest, retentive arm and reciprocal component; common in tooth-supported situations.")
    "RPI" -> tr(lang,
        "RPI = descanso mesial + placa proximal + barra I. Es un concepto de liberación de tensiones para extensiones distales cuando la anatomía permite la aproximación gingival.",
        "RPI = mesial Rest + Proximal plate + I-bar. It is a stress-releasing concept for distal extensions when anatomy permits gingival approach.")
    "RPA" -> tr(lang,
        "RPA = descanso mesial + placa proximal + brazo tipo Akers. Puede considerarse cuando una barra I no es favorable por anatomía vestibular o tejidos.",
        "RPA = mesial Rest + Proximal plate + Akers-type arm. It may be considered when an I-bar is unfavorable because of vestibular anatomy or tissues.")
    "I-bar" -> tr(lang,
        "Barra I: retenedor infrabuldge que se aproxima desde gingival; requiere un trayecto libre de interferencias de tejidos.",
        "I-bar: infrabulge retainer approaching from gingival; it requires a path free of tissue interference.")
    else -> tr(lang,
        "Combinado con alambre forjado: el brazo retentivo puede ser más flexible que uno colado; debe acompañarse de apoyo, reciprocación y control de trayectoria.",
        "Combination with wrought wire: the retentive arm may be more flexible than a cast arm; it must be accompanied by support, reciprocation and path control.")
}

private fun p3RetainerSuggestion(k:P3Kennedy,lang:String):String = when(k.classNumber) {
    1,2 -> tr(lang,
        "El patrón incluye extensión distal. Compara RPI y RPA y revisa soporte de la base, retención indirecta y control de rotación.",
        "The pattern includes a distal extension. Compare RPI and RPA and review base support, indirect retention and rotational control.")
    3 -> tr(lang,
        "El patrón es principalmente dentosoportado. Compara Akers/circunferencial con otras opciones según socavado, paralelizado, estética y condición del pilar.",
        "The pattern is mainly tooth-supported. Compare Akers/circumferential with other options according to undercut, surveying, esthetics and abutment condition.")
    4 -> tr(lang,
        "Clase IV exige atención especial a estética, soporte y trayectoria de inserción; Kennedy por sí sola no decide el retenedor.",
        "Class IV requires special attention to esthetics, support and path of insertion; Kennedy alone does not determine the clasp.")
    else -> tr(lang,"Marca ausencias para obtener una sugerencia biomecánica orientativa.","Mark missing teeth to obtain an educational biomechanical suggestion.")
}

@Composable
private fun P3RetainerDiagram(id:String) {
    Canvas(Modifier.fillMaxWidth().height(185.dp)) {
        val cx=size.width/2f
        val cy=size.height*.48f
        val tw=size.width*.20f
        val th=size.height*.45f

        drawRoundRect(Color(0xFFFFF6D7),Offset(cx-tw/2f,cy-th/2f),Size(tw,th),CornerRadius(24f,24f))
        drawRoundRect(P3Deep,Offset(cx-tw/2f,cy-th/2f),Size(tw,th),CornerRadius(24f,24f),style=Stroke(width=3f))
        drawLine(P3Purple,Offset(cx-tw*.16f,cy-th*.45f),Offset(cx+tw*.16f,cy-th*.45f),strokeWidth=10f)
        drawLine(P3Deep,Offset(cx,cy+th*.48f),Offset(cx,size.height*.88f),strokeWidth=6f)

        when(id) {
            "RPI","I-bar" -> {
                val p=Path().apply {
                    moveTo(cx-tw*.48f,size.height*.86f)
                    quadraticBezierTo(cx-tw*.62f,cy+th*.18f,cx-tw*.44f,cy+th*.02f)
                }
                drawPath(p,P3Turquoise,style=Stroke(width=8f))
                drawLine(P3Metal,Offset(cx+tw*.48f,cy-th*.18f),Offset(cx+tw*.48f,cy+th*.16f),strokeWidth=10f)
            }
            "RPA" -> {
                val p=Path().apply {
                    moveTo(cx-tw*.50f,cy-th*.10f)
                    quadraticBezierTo(cx-tw*.72f,cy+th*.20f,cx-tw*.30f,cy+th*.32f)
                }
                drawPath(p,P3Turquoise,style=Stroke(width=8f))
                drawLine(P3Metal,Offset(cx+tw*.48f,cy-th*.18f),Offset(cx+tw*.48f,cy+th*.16f),strokeWidth=10f)
            }
            "Combination" -> {
                val p=Path().apply {
                    moveTo(cx-tw*.52f,cy-th*.10f)
                    cubicTo(cx-tw*.70f,cy,cx-tw*.62f,cy+th*.30f,cx-tw*.18f,cy+th*.34f)
                }
                drawPath(p,P3Turquoise,style=Stroke(width=5f))
                drawLine(P3Metal,Offset(cx+tw*.48f,cy-th*.12f),Offset(cx+tw*.48f,cy+th*.25f),strokeWidth=10f)
            }
            else -> {
                val left=Path().apply {
                    moveTo(cx-tw*.52f,cy-th*.08f)
                    quadraticBezierTo(cx-tw*.70f,cy+th*.18f,cx-tw*.18f,cy+th*.30f)
                }
                val right=Path().apply {
                    moveTo(cx+tw*.52f,cy-th*.08f)
                    quadraticBezierTo(cx+tw*.70f,cy+th*.18f,cx+tw*.18f,cy+th*.30f)
                }
                drawPath(left,P3Turquoise,style=Stroke(width=8f))
                drawPath(right,P3Metal,style=Stroke(width=8f))
            }
        }
    }
}
