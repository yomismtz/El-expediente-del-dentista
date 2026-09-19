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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class OcclusionChoiceV41(
    val topic: String,
    val label: String,
    val what: String,
    val identify: String,
    val meaning: String
)

private val occlusionChoicesV41 = listOf(
    OcclusionChoiceV41("Plano terminal", "Recto", "Las superficies distales de los segundos molares temporales superior e inferior terminan aproximadamente en el mismo plano vertical.", "Se observa lateralmente la relación distal de los segundos molares temporales con la dentición en máxima intercuspidación.", "Es una relación frecuente en dentición temporal. La relación molar permanente posterior depende también del crecimiento mandibular y del aprovechamiento de espacios."),
    OcclusionChoiceV41("Plano terminal", "Escalón mesial", "La superficie distal del segundo molar temporal inferior queda por delante (mesial) de la del superior.", "Compara las caras distales de los segundos molares temporales; el inferior se encuentra más anterior.", "Puede favorecer una relación molar permanente de Clase I y, cuando el escalón es marcado, puede asociarse con tendencia hacia Clase III. Debe interpretarse con crecimiento y oclusión completa."),
    OcclusionChoiceV41("Plano terminal", "Escalón distal", "La superficie distal del segundo molar temporal inferior queda por detrás (distal) de la del superior.", "En vista lateral, la cara distal del segundo molar temporal mandibular se localiza más posterior.", "Se relaciona con mayor tendencia a una relación molar permanente Clase II; no predice por sí solo el resultado final."),

    OcclusionChoiceV41("Angle molar", "Clase I", "Relación de primeros molares permanentes en la que la cúspide mesiovestibular del primer molar superior corresponde aproximadamente con el surco mesiovestibular del primer molar inferior.", "Se valora bilateralmente con los dientes en máxima intercuspidación.", "Describe la relación sagital molar; no significa por sí sola que toda la oclusión sea normal."),
    OcclusionChoiceV41("Angle molar", "Clase II", "El primer molar inferior se encuentra relativamente distal respecto al superior en comparación con la relación de Clase I.", "Se observa que la referencia mandibular queda más posterior. Debe registrarse derecha e izquierda y, cuando aplique, división 1 o 2 según relación incisiva.", "Puede coexistir con overjet aumentado, profundo u otras relaciones; se integra con perfil, crecimiento y posición dentaria."),
    OcclusionChoiceV41("Angle molar", "Clase III", "El primer molar inferior se encuentra relativamente mesial respecto al superior en comparación con Clase I.", "La referencia mandibular queda más anterior en la vista lateral.", "Puede acompañarse de overjet reducido o negativo, pero el diagnóstico ortodóncico requiere evaluación completa."),

    OcclusionChoiceV41("Relación canina", "Clase I", "La cúspide del canino superior se sitúa aproximadamente en la tronera entre el canino inferior y el primer premolar inferior.", "Se observa por cada lado con máxima intercuspidación y se compara la punta cuspídea superior con los dientes inferiores.", "Es una referencia sagital canina. Debe anotarse por lado porque puede existir asimetría."),
    OcclusionChoiceV41("Relación canina", "Clase II", "La cúspide del canino superior se encuentra más anterior (mesial) de la posición esperada para Clase I.", "Compara la cúspide superior con la tronera canino–primer premolar inferior.", "Sugiere una relación sagital de Clase II en el sector canino; requiere correlación con molares e incisivos."),
    OcclusionChoiceV41("Relación canina", "Clase III", "La cúspide del canino superior se encuentra más posterior (distal) de la posición de Clase I.", "Se valora bilateralmente observando el desplazamiento relativo de la referencia canina.", "Sugiere una relación sagital de Clase III en el sector canino; no sustituye el análisis integral."),

    OcclusionChoiceV41("Overjet", "Dentro de rango esperado", "Es la distancia horizontal entre incisivos superiores e inferiores. Un valor positivo moderado es habitual.", "Se mide en milímetros desde la superficie vestibular del incisivo inferior hasta el borde/incisivo superior correspondiente, con referencia horizontal reproducible.", "El valor debe registrarse en mm y relacionarse con inclinación incisiva, hábitos y patrón sagital."),
    OcclusionChoiceV41("Overjet", "Aumentado", "La distancia horizontal entre incisivos superiores e inferiores está aumentada.", "Los incisivos superiores quedan más adelantados respecto a los inferiores; se confirma midiendo en mm.", "Puede relacionarse con Clase II, inclinación incisiva, hábitos u otros factores. No debe inferirse la causa sólo por el aspecto."),
    OcclusionChoiceV41("Overjet", "Borde a borde", "Los bordes incisales superiores e inferiores contactan con resalte horizontal cercano a cero.", "Se observa contacto borde con borde de incisivos al cerrar.", "Puede representar una relación transicional o una tendencia sagital que debe contextualizarse con edad y crecimiento."),
    OcclusionChoiceV41("Overjet", "Negativo", "Los incisivos inferiores se encuentran por delante de los superiores en sentido horizontal.", "La medición del resalte resulta negativa y puede observarse una mordida cruzada anterior.", "Requiere diferenciar componente dentario, funcional y esquelético mediante evaluación ortodóncica completa."),

    OcclusionChoiceV41("Overbite", "Dentro de rango esperado", "Es el solapamiento vertical de los incisivos superiores sobre los inferiores.", "Se registra en milímetros o porcentaje de cobertura coronaria con dientes en máxima intercuspidación.", "Una superposición moderada puede ser fisiológica; siempre debe relacionarse con función y edad."),
    OcclusionChoiceV41("Overbite", "Aumentado / profundo", "Existe solapamiento vertical excesivo de los incisivos.", "Los incisivos superiores cubren una proporción mayor de la corona clínica inferior; se cuantifica en mm o porcentaje.", "Puede asociarse con trauma palatino/gingival en casos severos, desgaste o determinados patrones verticales."),
    OcclusionChoiceV41("Overbite", "Reducido", "La cobertura vertical entre incisivos es menor de la esperada sin llegar necesariamente a una mordida abierta.", "Existe poca superposición vertical; debe medirse.", "Puede relacionarse con patrón vertical, erupción, hábitos o posición dentaria."),
    OcclusionChoiceV41("Overbite", "Mordida abierta anterior", "No existe contacto o solapamiento vertical entre determinados dientes anteriores al cerrar.", "Se observa un espacio vertical entre incisivos superiores e inferiores en máxima intercuspidación.", "Puede ser dentoalveolar, funcional o tener componente esquelético; deben revisarse hábitos, función y crecimiento."),

    OcclusionChoiceV41("Líneas medias", "Coincidentes", "Las líneas medias dentales superior e inferior coinciden entre sí y con las referencias faciales dentro de la variación clínica.", "Se comparan líneas interincisivas con el centro facial y entre ambas arcadas.", "La coincidencia apoya simetría dental, pero no descarta otras alteraciones oclusales."),
    OcclusionChoiceV41("Líneas medias", "Desviación superior", "La línea media maxilar se desplaza respecto a la referencia facial o mandibular.", "Se identifica el lado y se cuantifica en milímetros.", "Puede relacionarse con pérdida de espacio, migración dentaria, discrepancias de tamaño o asimetría."),
    OcclusionChoiceV41("Líneas medias", "Desviación inferior", "La línea media mandibular está desplazada respecto a la superior o la facial.", "Se registra dirección y magnitud; conviene observar si cambia entre relación céntrica y máxima intercuspidación.", "Una desviación funcional puede diferir de una asimetría estructural; requiere exploración adicional."),

    OcclusionChoiceV41("Mordida cruzada", "Anterior", "Uno o más dientes anteriores superiores ocluyen por lingual de sus antagonistas inferiores.", "Se observa la relación labiolingual de incisivos y caninos en máxima intercuspidación.", "Debe diferenciarse una interferencia funcional de una discrepancia dentaria o esquelética."),
    OcclusionChoiceV41("Mordida cruzada", "Posterior unilateral", "En un lado, uno o más dientes posteriores superiores ocluyen en relación transversal invertida respecto a los inferiores.", "Se examina derecha e izquierda y se observa la trayectoria de cierre.", "Puede acompañarse de desplazamiento funcional mandibular; la asimetría debe documentarse."),
    OcclusionChoiceV41("Mordida cruzada", "Posterior bilateral", "La relación transversal invertida aparece en ambos lados posteriores.", "Se observan ambas hemiarcadas en máxima intercuspidación.", "Puede asociarse a discrepancia transversal del maxilar/mandíbula y requiere evaluación ortodóncica integral."),
    OcclusionChoiceV41("Mordida cruzada", "Funcional", "La mandíbula se desplaza al cerrar para evitar una interferencia, produciendo una relación cruzada o una desviación aparente.", "Compara la posición inicial de cierre con la máxima intercuspidación y busca contactos prematuros.", "La detección temprana es relevante porque el patrón puede mantener una asimetría funcional."),

    OcclusionChoiceV41("Mordida abierta", "Anterior", "Existe separación vertical entre dientes anteriores cuando los posteriores están en oclusión.", "Se observa y se mide el espacio vertical entre incisivos.", "Puede relacionarse con hábitos, postura lingual, erupción o patrón esquelético; la etiología debe estudiarse."),
    OcclusionChoiceV41("Mordida abierta", "Posterior", "Existe falta de contacto vertical en un sector posterior al cerrar.", "Se localiza el segmento sin contacto y se descartan interferencias, alteraciones eruptivas o causas mecánicas.", "Es menos frecuente que la anterior y necesita evaluación del mecanismo que impide la intercuspidación."),

    OcclusionChoiceV41("Mordida profunda", "Profunda anterior", "Los incisivos superiores cubren en exceso a los inferiores en sentido vertical.", "Se cuantifica el overbite y se revisa si existe contacto traumático en paladar o encía.", "La severidad se interpreta junto con patrón facial, erupción, desgaste y relación sagital."),

    OcclusionChoiceV41("Espacios y alineación", "Apiñamiento", "No existe espacio suficiente para alinear todos los dientes en la arcada, provocando rotaciones, solapamientos o desplazamientos.", "Se observa falta de espacio y se cuantifica mediante análisis de discrepancia arco-diente cuando se requiere.", "Puede afectar higiene, erupción y planificación ortodóncica; la gravedad no se define sólo por una fotografía."),
    OcclusionChoiceV41("Espacios y alineación", "Diastemas", "Existen espacios visibles entre dientes contiguos.", "Se identifica localización, tamaño y distribución de los espacios.", "Puede ser fisiológico en determinadas etapas o relacionarse con tamaño dentario, frenillo, ausencia dental, hábitos u otras causas."),
    OcclusionChoiceV41("Espacios y alineación", "Espacios primates", "Espacios característicos de la dentición temporal, típicamente mesial al canino superior y distal al canino inferior.", "Se observan en dentición temporal y se diferencian de diastemas patológicos.", "Contribuyen al espacio disponible para la transición hacia la dentición permanente."),
    OcclusionChoiceV41("Espacios y alineación", "Espaciamiento generalizado", "Hay múltiples espacios distribuidos en la arcada.", "Se valora tamaño de dientes, longitud del arco, ausencias, frenillos y etapa de desarrollo.", "Puede ser una variante de desarrollo o requerir estudio según edad, distribución y causa.")
)

@Composable
fun OcclusionTeachingV41Screen(lang: String, onBack: () -> Unit) {
    val selections = remember { mutableStateMapOf<String, String>() }
    var focused by remember { mutableStateOf(occlusionChoicesV41.first()) }
    val groups = occlusionChoicesV41.groupBy { it.topic }

    ResponsiveScreenV17(
        tr(lang, "Examen de oclusión · atlas interactivo", "Occlusion exam · interactive atlas"),
        tr(lang, "Toca cada recuadro para ver qué es, cómo se identifica y cómo se interpreta.", "Tap each card to see what it is, how it is identified and how it is interpreted."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang,
            "Las relaciones oclusales se describen por dentición, lado, medida y etapa de crecimiento. Ningún hallazgo aislado determina por sí solo un diagnóstico ortodóncico.",
            "Occlusal relationships are described by dentition, side, measurement and growth stage. No isolated finding establishes an orthodontic diagnosis by itself."))

        groups.forEach { (topic, options) ->
            ResponsiveSectionV17(topic) {
                OcclusionAnatomyV41(topic, selections[topic] ?: focused.label)
                AdaptiveGridV17(
                    options.size,
                    if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else if (options.size >= 4) 2 else options.size
                ) { i ->
                    val item = options[i]
                    val isSelected = selections[topic] == item.label
                    Card(
                        onClick = {
                            selections[topic] = item.label
                            focused = item
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                        ),
                        border = BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = .35f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(item.label, fontWeight = FontWeight.Black)
                            Text(
                                if (isSelected) tr(lang, "Seleccionado · toca nuevamente otro recuadro para comparar", "Selected · tap another card to compare")
                                else tr(lang, "Toca para ver la descripción", "Tap to view description"),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                val chosen = selections[topic]?.let { label -> options.firstOrNull { it.label == label } }
                if (chosen != null) {
                    OcclusionDescriptionV41(chosen)
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "Resumen educativo", "Teaching summary")) {
            if (selections.isEmpty()) {
                Text(tr(lang, "Selecciona al menos una relación oclusal para formar el resumen.", "Select at least one occlusal relationship to build the summary."))
            } else {
                selections.forEach { (topic, value) ->
                    Text("• $topic: $value")
                }
                val summary = selections.entries.joinToString(" · ") { "${it.key}: ${it.value}" }
                Card(
                    onClick = { TeachingStateV40.moduleSummaries["occlusion"] = summary },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .4f))
                ) {
                    Text(tr(lang, "Guardar resumen de oclusión", "Save occlusion summary"), Modifier.padding(13.dp), fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun OcclusionDescriptionV41(item: OcclusionChoiceV41) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = .35f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("¿Qué es?", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            Text(item.what)
            Text("¿Cómo se identifica?", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            Text(item.identify)
            Text("Interpretación clínica", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            Text(item.meaning)
        }
    }
}

@Composable
private fun OcclusionAnatomyV41(topic: String, option: String) {
    val enamel = Color(0xFFFFF4DE)
    val enamelEdge = Color(0xFF9A8069)
    val gum = Color(0xFFE996A5)
    val gumDark = Color(0xFFB95E70)
    val accent = MaterialTheme.colorScheme.primary

    Canvas(Modifier.fillMaxWidth().height(225.dp).padding(vertical = 6.dp)) {
        when (topic) {
            "Plano terminal", "Angle molar", "Relación canina" -> drawLateralOcclusionV41(topic, option, enamel, enamelEdge, gum, gumDark, accent)
            "Overjet", "Overbite", "Líneas medias", "Mordida abierta", "Mordida profunda" -> drawAnteriorOcclusionV41(topic, option, enamel, enamelEdge, gum, gumDark, accent)
            "Mordida cruzada" -> drawCrossbiteV41(option, enamel, enamelEdge, gum, gumDark, accent)
            else -> drawSpacingV41(option, enamel, enamelEdge, gum, gumDark, accent)
        }
    }
}

private fun DrawScope.drawLateralOcclusionV41(
    topic: String,
    option: String,
    enamel: Color,
    edge: Color,
    gum: Color,
    gumDark: Color,
    accent: Color
) {
    val w = size.width
    val h = size.height

    val upperGum = Path().apply {
        moveTo(w * .08f, h * .20f)
        cubicTo(w * .28f, h * .10f, w * .70f, h * .11f, w * .92f, h * .22f)
        lineTo(w * .92f, h * .34f)
        cubicTo(w * .70f, h * .28f, w * .30f, h * .28f, w * .08f, h * .35f)
        close()
    }
    val lowerGum = Path().apply {
        moveTo(w * .08f, h * .80f)
        cubicTo(w * .30f, h * .89f, w * .70f, h * .89f, w * .92f, h * .78f)
        lineTo(w * .92f, h * .66f)
        cubicTo(w * .72f, h * .72f, w * .30f, h * .72f, w * .08f, h * .65f)
        close()
    }
    drawPath(upperGum, gum)
    drawPath(lowerGum, gum)
    drawPath(upperGum, gumDark, style = Stroke(2.5f))
    drawPath(lowerGum, gumDark, style = Stroke(2.5f))

    val lowerShift = when {
        option.contains("Clase II") || option.contains("distal", ignoreCase = true) -> w * .045f
        option.contains("Clase III") || option.contains("mesial", ignoreCase = true) -> -w * .045f
        else -> 0f
    }

    val xs = listOf(.22f, .34f, .47f, .61f, .75f)
    xs.forEachIndexed { index, x ->
        val type = when (index) { 0 -> 0; 1 -> 1; 2 -> 2; else -> 3 }
        drawClinicalCrownV41(Offset(w * x, h * .37f), w * .085f, h * .19f, true, type, enamel, edge)
        drawClinicalCrownV41(Offset(w * x + lowerShift, h * .63f), w * .085f, h * .19f, false, type, enamel, edge)
    }

    if (topic == "Plano terminal") {
        val upperX = w * .82f
        val lowerX = upperX + lowerShift
        drawLine(accent, Offset(upperX, h * .27f), Offset(upperX, h * .47f), strokeWidth = 5f)
        drawLine(accent.copy(alpha = .65f), Offset(lowerX, h * .54f), Offset(lowerX, h * .74f), strokeWidth = 5f)
    }
    if (topic == "Relación canina") {
        drawCircle(accent, 9f, Offset(w * .47f, h * .43f))
        drawCircle(accent.copy(alpha = .65f), 9f, Offset(w * .47f + lowerShift, h * .57f))
    }
    if (topic == "Angle molar") {
        drawLine(accent, Offset(w * .64f, h * .41f), Offset(w * .64f + lowerShift, h * .59f), strokeWidth = 4f)
    }
}

private fun DrawScope.drawAnteriorOcclusionV41(
    topic: String,
    option: String,
    enamel: Color,
    edge: Color,
    gum: Color,
    gumDark: Color,
    accent: Color
) {
    val w = size.width
    val h = size.height
    drawRoundRect(gum, Offset(w * .08f, h * .18f), androidx.compose.ui.geometry.Size(w * .84f, h * .22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(34f))
    drawRoundRect(gum, Offset(w * .08f, h * .62f), androidx.compose.ui.geometry.Size(w * .84f, h * .20f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(34f))
    drawLine(gumDark, Offset(w * .10f, h * .39f), Offset(w * .90f, h * .39f), strokeWidth = 2.5f)
    drawLine(gumDark, Offset(w * .10f, h * .62f), Offset(w * .90f, h * .62f), strokeWidth = 2.5f)

    val spacing = if (option.contains("Diastemas") || option.contains("Espaciamiento")) .105f else .088f
    val lowerY = when {
        topic == "Overbite" && option.contains("abierta", true) -> .70f
        topic == "Mordida abierta" -> .71f
        topic == "Overbite" && option.contains("profundo", true) -> .56f
        topic == "Mordida profunda" -> .55f
        else -> .64f
    }
    val overjetShift = when {
        topic == "Overjet" && option.contains("Aumentado") -> w * .045f
        topic == "Overjet" && option.contains("Negativo") -> -w * .035f
        else -> 0f
    }

    for (i in -2..2) {
        val x = .50f + i * spacing
        drawClinicalCrownV41(Offset(w * x, h * .42f), w * .070f, h * .20f, true, if (kotlin.math.abs(i) == 2) 1 else 0, enamel, edge)
        drawClinicalCrownV41(Offset(w * x - overjetShift, h * lowerY), w * .068f, h * .19f, false, if (kotlin.math.abs(i) == 2) 1 else 0, enamel, edge)
    }

    when (topic) {
        "Overjet" -> drawLine(accent, Offset(w * .50f - overjetShift, h * .52f), Offset(w * .50f, h * .52f), strokeWidth = 5f)
        "Overbite", "Mordida profunda", "Mordida abierta" -> drawLine(accent, Offset(w * .82f, h * .45f), Offset(w * .82f, h * lowerY), strokeWidth = 5f)
        "Líneas medias" -> {
            val shift = if (option.contains("superior", true)) -w * .04f else if (option.contains("inferior", true)) w * .04f else 0f
            drawLine(accent, Offset(w * .50f + if (option.contains("superior", true)) shift else 0f, h * .22f), Offset(w * .50f + if (option.contains("superior", true)) shift else 0f, h * .48f), strokeWidth = 4f)
            drawLine(accent.copy(alpha = .65f), Offset(w * .50f + if (option.contains("inferior", true)) shift else 0f, h * .55f), Offset(w * .50f + if (option.contains("inferior", true)) shift else 0f, h * .80f), strokeWidth = 4f)
        }
    }
}

private fun DrawScope.drawCrossbiteV41(
    option: String,
    enamel: Color,
    edge: Color,
    gum: Color,
    gumDark: Color,
    accent: Color
) {
    val w = size.width
    val h = size.height
    drawRoundRect(gum, Offset(w * .08f, h * .19f), androidx.compose.ui.geometry.Size(w * .84f, h * .22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(30f))
    drawRoundRect(gum, Offset(w * .08f, h * .60f), androidx.compose.ui.geometry.Size(w * .84f, h * .22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(30f))
    drawLine(gumDark, Offset(w * .10f, h * .40f), Offset(w * .90f, h * .40f), strokeWidth = 2.5f)
    drawLine(gumDark, Offset(w * .10f, h * .60f), Offset(w * .90f, h * .60f), strokeWidth = 2.5f)

    for (i in 0..6) {
        val x = .20f + i * .10f
        val posterior = i <= 1 || i >= 5
        val cross = when {
            option.contains("Anterior") -> i in 2..4
            option.contains("unilateral", true) || option.contains("Funcional") -> i >= 4
            option.contains("bilateral", true) -> posterior
            else -> false
        }
        val upperShift = if (cross) (if (i >= 4) -w * .018f else w * .018f) else 0f
        drawClinicalCrownV41(Offset(w * x + upperShift, h * .42f), w * .062f, h * .18f, true, if (posterior) 3 else 0, enamel, edge)
        drawClinicalCrownV41(Offset(w * x, h * .59f), w * .063f, h * .18f, false, if (posterior) 3 else 0, enamel, edge)
        if (cross) drawCircle(accent.copy(alpha = .35f), w * .032f, Offset(w * x, h * .505f))
    }
}

private fun DrawScope.drawSpacingV41(
    option: String,
    enamel: Color,
    edge: Color,
    gum: Color,
    gumDark: Color,
    accent: Color
) {
    val w = size.width
    val h = size.height
    drawRoundRect(gum, Offset(w * .08f, h * .25f), androidx.compose.ui.geometry.Size(w * .84f, h * .28f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(36f))
    drawLine(gumDark, Offset(w * .10f, h * .50f), Offset(w * .90f, h * .50f), strokeWidth = 2.5f)
    val crowded = option.contains("Apiñamiento")
    val spaced = option.contains("Diastemas") || option.contains("Espaciamiento") || option.contains("primates", true)
    for (i in -3..3) {
        val baseX = .50f + i * if (spaced) .095f else .078f
        val extra = if (crowded) (if (i % 2 == 0) .018f else -.014f) else 0f
        val y = .54f + if (crowded && i % 2 == 0) .025f else 0f
        drawClinicalCrownV41(Offset(w * (baseX + extra), h * y), w * .060f, h * .20f, false, if (kotlin.math.abs(i) >= 2) 1 else 0, enamel, edge)
    }
    if (spaced) {
        drawLine(accent, Offset(w * .46f, h * .66f), Offset(w * .54f, h * .66f), strokeWidth = 4f)
    }
}

private fun DrawScope.drawClinicalCrownV41(
    center: Offset,
    width: Float,
    height: Float,
    upper: Boolean,
    type: Int,
    enamel: Color,
    edge: Color
) {
    val dir = if (upper) 1f else -1f
    val half = width / 2f
    val shoulder = when (type) { 3 -> .30f; 2 -> .24f; else -> .20f }
    val cusp = when (type) { 3 -> .14f; 2 -> .22f; 1 -> .13f; else -> .08f }
    val p = Path().apply {
        moveTo(center.x - half * .82f, center.y)
        cubicTo(
            center.x - half * .95f, center.y + dir * height * .13f,
            center.x - half * .70f, center.y + dir * height * .72f,
            center.x - half * .32f, center.y + dir * height * .91f
        )
        cubicTo(
            center.x - half * shoulder, center.y + dir * height * (1f + cusp),
            center.x + half * shoulder, center.y + dir * height * (1f + cusp),
            center.x + half * .32f, center.y + dir * height * .91f
        )
        cubicTo(
            center.x + half * .70f, center.y + dir * height * .72f,
            center.x + half * .95f, center.y + dir * height * .13f,
            center.x + half * .82f, center.y
        )
        quadraticBezierTo(center.x, center.y - dir * height * .13f, center.x - half * .82f, center.y)
        close()
    }
    drawPath(p, enamel)
    drawPath(p, edge, style = Stroke(2.2f))
    drawOval(Color.White.copy(alpha = .35f), Offset(center.x - half * .45f, center.y + dir * height * .12f), androidx.compose.ui.geometry.Size(width * .26f, height * .28f))
}
