package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

private data class OcclusionChoiceV42(
    val topic: String,
    val label: String,
    val what: String,
    val identify: String,
    val meaning: String
)

private data class OcclusionPhotoV42(
    val url: String,
    val caption: String,
    val credit: String
)

private val occlusionChoicesV42 = listOf(
    OcclusionChoiceV42("Plano terminal", "Recto", "Las superficies distales de los segundos molares temporales superior e inferior terminan aproximadamente en el mismo plano vertical.", "Se valora lateralmente con la dentición temporal en máxima intercuspidación, comparando las caras distales de los segundos molares temporales.", "Puede evolucionar a una relación molar permanente de Clase I, aunque el crecimiento y el aprovechamiento de espacios modifican el resultado."),
    OcclusionChoiceV42("Plano terminal", "Escalón mesial", "La superficie distal del segundo molar temporal inferior está más mesial que la del superior.", "Se observa el segundo molar temporal mandibular más adelantado respecto al maxilar.", "Puede favorecer Clase I y, si el escalón es marcado, relacionarse con una tendencia hacia Clase III; no predice por sí solo la oclusión permanente."),
    OcclusionChoiceV42("Plano terminal", "Escalón distal", "La superficie distal del segundo molar temporal inferior queda más distal que la del superior.", "En vista lateral, el segundo molar temporal mandibular se encuentra más posterior.", "Se asocia con mayor tendencia a una relación molar permanente Clase II, pero requiere seguimiento del crecimiento."),

    OcclusionChoiceV42("Angle molar", "Clase I", "La cúspide mesiovestibular del primer molar superior se relaciona aproximadamente con el surco mesiovestibular del primer molar inferior.", "Se observa por lado con los primeros molares permanentes en máxima intercuspidación.", "Describe una relación sagital molar. No significa por sí sola que toda la oclusión sea normal."),
    OcclusionChoiceV42("Angle molar", "Clase II", "El primer molar inferior está relativamente distal respecto al superior en comparación con Clase I.", "La referencia mandibular queda más posterior. Debe registrarse derecha e izquierda y, cuando aplique, división 1 o 2 según relación incisiva.", "Puede coexistir con overjet aumentado, sobremordida profunda u otras relaciones."),
    OcclusionChoiceV42("Angle molar", "Clase III", "El primer molar inferior está relativamente mesial respecto al superior en comparación con Clase I.", "La referencia mandibular queda más anterior en vista lateral.", "Puede acompañarse de overjet reducido o negativo; el diagnóstico ortodóncico requiere evaluación completa."),

    OcclusionChoiceV42("Relación canina", "Clase I", "La cúspide del canino superior se sitúa aproximadamente en la tronera entre canino inferior y primer premolar inferior.", "Se valora bilateralmente con máxima intercuspidación.", "Es una referencia sagital canina y debe registrarse por lado porque puede existir asimetría."),
    OcclusionChoiceV42("Relación canina", "Clase II", "La cúspide del canino superior está más mesial que la posición esperada para Clase I.", "Se compara la cúspide superior con la tronera canino–primer premolar inferior.", "Sugiere relación sagital Clase II en el sector canino; se correlaciona con molares e incisivos."),
    OcclusionChoiceV42("Relación canina", "Clase III", "La cúspide del canino superior está más distal que la posición de Clase I.", "Se valora bilateralmente observando el desplazamiento relativo de la referencia canina.", "Sugiere relación sagital Clase III en el sector canino; no sustituye análisis integral."),

    OcclusionChoiceV42("Overjet", "Dentro de rango esperado", "Es la distancia horizontal entre incisivos superiores e inferiores.", "Se mide en milímetros desde la superficie vestibular del incisivo inferior hasta el borde/incisivo superior correspondiente.", "Debe registrarse en mm y relacionarse con inclinación incisiva, hábitos y patrón sagital."),
    OcclusionChoiceV42("Overjet", "Aumentado", "La distancia horizontal entre incisivos superiores e inferiores está aumentada.", "Los incisivos superiores quedan más adelantados respecto a los inferiores; se confirma midiendo en mm.", "Puede asociarse con Clase II, inclinación incisiva, hábitos u otros factores."),
    OcclusionChoiceV42("Overjet", "Borde a borde", "Los bordes incisales superiores e inferiores contactan con resalte horizontal cercano a cero.", "Se observa contacto borde con borde de incisivos al cerrar.", "Puede ser transicional o reflejar una relación sagital que debe contextualizarse con edad y crecimiento."),
    OcclusionChoiceV42("Overjet", "Negativo", "Los incisivos inferiores se encuentran por delante de los superiores en sentido horizontal.", "La medición del resalte resulta negativa y puede acompañar una mordida cruzada anterior.", "Debe diferenciarse componente dentario, funcional y esquelético."),

    OcclusionChoiceV42("Overbite", "Dentro de rango esperado", "Es el solapamiento vertical de los incisivos superiores sobre los inferiores.", "Se registra en milímetros o porcentaje de cobertura coronaria con máxima intercuspidación.", "Una superposición moderada puede ser fisiológica; debe relacionarse con función y edad."),
    OcclusionChoiceV42("Overbite", "Aumentado / profundo", "Existe solapamiento vertical excesivo de los incisivos.", "Los incisivos superiores cubren mayor proporción de la corona clínica inferior; se cuantifica en mm o porcentaje.", "En casos severos puede acompañarse de trauma palatino/gingival, desgaste u otros cambios."),
    OcclusionChoiceV42("Overbite", "Reducido", "La cobertura vertical entre incisivos es menor de la esperada sin llegar necesariamente a una mordida abierta.", "Existe poca superposición vertical y debe medirse.", "Puede relacionarse con patrón vertical, erupción, hábitos o posición dentaria."),
    OcclusionChoiceV42("Overbite", "Mordida abierta anterior", "No existe contacto o solapamiento vertical entre determinados dientes anteriores al cerrar.", "Se observa un espacio vertical entre incisivos superiores e inferiores en máxima intercuspidación.", "Puede ser dentoalveolar, funcional o tener componente esquelético."),

    OcclusionChoiceV42("Líneas medias", "Coincidentes", "Las líneas medias dentales superior e inferior coinciden entre sí y con las referencias faciales dentro de la variación clínica.", "Se comparan líneas interincisivas con el centro facial y entre ambas arcadas.", "La coincidencia apoya simetría dental, pero no descarta otras alteraciones oclusales."),
    OcclusionChoiceV42("Líneas medias", "Desviación superior", "La línea media maxilar está desplazada respecto a la referencia facial o mandibular.", "Se identifica el lado y se cuantifica en milímetros.", "Puede relacionarse con pérdida de espacio, migración dentaria, discrepancias de tamaño o asimetría."),
    OcclusionChoiceV42("Líneas medias", "Desviación inferior", "La línea media mandibular está desplazada respecto a la superior o facial.", "Se registra dirección y magnitud y se observa si cambia durante el cierre.", "Una desviación funcional puede diferir de una asimetría estructural."),

    OcclusionChoiceV42("Mordida cruzada", "Anterior", "Uno o más dientes anteriores superiores ocluyen por lingual de sus antagonistas inferiores.", "Se observa la relación labiolingual de incisivos y caninos en máxima intercuspidación.", "Debe diferenciarse interferencia funcional de discrepancia dentaria o esquelética."),
    OcclusionChoiceV42("Mordida cruzada", "Posterior unilateral", "En un lado, uno o más dientes posteriores superiores ocluyen en relación transversal invertida respecto a los inferiores.", "Se examina derecha e izquierda y se observa la trayectoria de cierre.", "Puede acompañarse de desplazamiento funcional mandibular; la asimetría debe documentarse."),
    OcclusionChoiceV42("Mordida cruzada", "Posterior bilateral", "La relación transversal invertida aparece en ambos lados posteriores.", "Se observan ambas hemiarcadas en máxima intercuspidación.", "Puede asociarse con discrepancia transversal y requiere evaluación ortodóncica integral."),
    OcclusionChoiceV42("Mordida cruzada", "Funcional", "La mandíbula se desplaza al cerrar para evitar una interferencia, produciendo una relación cruzada o desviación aparente.", "Compara la posición inicial de cierre con la máxima intercuspidación y busca contactos prematuros.", "La detección temprana es relevante porque el patrón puede mantener una asimetría funcional."),

    OcclusionChoiceV42("Mordida abierta", "Anterior", "Existe separación vertical entre dientes anteriores cuando los posteriores están en oclusión.", "Se observa y se mide el espacio vertical entre incisivos.", "Puede relacionarse con hábitos, postura lingual, erupción o patrón esquelético."),
    OcclusionChoiceV42("Mordida abierta", "Posterior", "Existe falta de contacto vertical en un sector posterior al cerrar.", "Se localiza el segmento sin contacto y se descartan interferencias, alteraciones eruptivas o causas mecánicas.", "Es menos frecuente que la anterior y requiere estudiar por qué no se produce intercuspidación."),

    OcclusionChoiceV42("Mordida profunda", "Profunda anterior", "Los incisivos superiores cubren en exceso a los inferiores en sentido vertical.", "Se cuantifica el overbite y se revisa si existe contacto traumático en paladar o encía.", "La severidad se interpreta junto con patrón facial, erupción, desgaste y relación sagital."),

    OcclusionChoiceV42("Espacios y alineación", "Apiñamiento", "No existe espacio suficiente para alinear todos los dientes en la arcada, provocando rotaciones, solapamientos o desplazamientos.", "Se observa falta de espacio y, cuando procede, se cuantifica la discrepancia arco-diente.", "Puede afectar higiene, erupción y planificación ortodóncica."),
    OcclusionChoiceV42("Espacios y alineación", "Diastemas", "Existen espacios visibles entre dientes contiguos.", "Se identifica localización, tamaño y distribución de los espacios.", "Puede ser fisiológico en determinadas etapas o relacionarse con tamaño dentario, frenillo, ausencia dental, hábitos u otras causas."),
    OcclusionChoiceV42("Espacios y alineación", "Espacios primates", "Son espacios característicos de la dentición temporal, típicamente mesial al canino superior y distal al canino inferior.", "Se observan en dentición temporal y se diferencian de espacios patológicos.", "Contribuyen al espacio disponible para la transición hacia dentición permanente."),
    OcclusionChoiceV42("Espacios y alineación", "Espaciamiento generalizado", "Hay múltiples espacios distribuidos en la arcada.", "Se valora tamaño de dientes, longitud del arco, ausencias, frenillos y etapa de desarrollo.", "Puede ser una variante de desarrollo o requerir estudio según edad, distribución y causa.")
)

private fun photoForV42(topic: String, label: String): OcclusionPhotoV42 = when (topic) {
    "Plano terminal" -> OcclusionPhotoV42(
        "https://commons.wikimedia.org/wiki/Special:Redirect/file/Deciduous%20teeth%20by%20David%20Shankbone%20new.jpg",
        "Dentición temporal clínica para reconocer segundos molares temporales antes de valorar el plano terminal.",
        "Wikimedia Commons · David Shankbone · consultar licencia en la ficha del archivo"
    )
    "Angle molar" -> when {
        label.contains("III") -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Zahnfehlstellung%20Angle-Klasse%20III.jpg", "Fotografía clínica de relación Angle Clase III.", "Wikimedia Commons · Dr. Georg Risse · CC BY-SA/GFDL")
        label.contains("II") -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Zahnfehlstellung%20Angle-Klasse%20II-1.jpg", "Fotografía clínica de relación Angle Clase II.", "Wikimedia Commons · Dr. Georg Risse · CC BY-SA/GFDL")
        else -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Class%201%20bimaxillary%20protrusion.jpg", "Fotografía clínica de oclusión con relación molar Clase I como referencia.", "Wikimedia Commons · consultar autor y licencia en la ficha del archivo")
    }
    "Relación canina" -> when {
        label.contains("III") -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Zahnfehlstellung%20Angle-Klasse%20III.jpg", "Vista lateral útil para localizar la relación canina junto con la molar.", "Wikimedia Commons · Dr. Georg Risse · CC BY-SA/GFDL")
        label.contains("II") -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Zahnfehlstellung%20Angle-Klasse%20II-1.jpg", "Vista lateral útil para localizar la relación canina junto con la molar.", "Wikimedia Commons · Dr. Georg Risse · CC BY-SA/GFDL")
        else -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Class%201%20bimaxillary%20protrusion.jpg", "Vista clínica lateral de referencia para reconocer canino superior e inferior.", "Wikimedia Commons · consultar autor y licencia")
    }
    "Overjet" -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Overjet.jpg", "Fotografía clínica de overjet; la medida debe realizarse en milímetros.", "Wikimedia Commons · Rama2k1 · CC BY-SA 4.0")
    "Overbite" -> if (label.contains("abierta", true))
        OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Anterior%20open%20bite%20malocclusion.jpg", "Fotografía clínica de mordida abierta anterior.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")
    else
        OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Deep%20bite.jpg", "Fotografía clínica de sobremordida profunda.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")
    "Líneas medias" -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Canted%20occlusal%20plane.jpg", "Vista frontal clínica útil para revisar referencias faciales, líneas medias y plano oclusal.", "Wikimedia Commons · consultar autor y licencia")
    "Mordida cruzada" -> if (label.contains("Anterior"))
        OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Anterior%20crossbite.jpg", "Fotografía clínica de mordida cruzada anterior.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")
    else
        OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Crossbite.jpg", "Fotografía clínica de mordida cruzada posterior unilateral.", "Wikimedia Commons · Giorgio Fiorelli · dominio público")
    "Mordida abierta" -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Anterior%20open%20bite%20malocclusion.jpg", "Fotografía clínica de mordida abierta anterior.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")
    "Mordida profunda" -> OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Deep%20bite.jpg", "Fotografía clínica de mordida profunda.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")
    else -> if (label.contains("Diastema", true) || label.contains("Espaciamiento", true) || label.contains("primates", true))
        OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Brian%20diastema.png", "Fotografía de diastema como referencia visual de espaciamiento.", "Wikimedia Commons · Ian Furst · CC BY-SA 4.0")
    else
        OcclusionPhotoV42("https://commons.wikimedia.org/wiki/Special:Redirect/file/Sever%20Crowding%20of%20teeth.jpg", "Fotografía clínica de apiñamiento dental.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")
}

@Composable
fun OcclusionPhotoAtlasV42Screen(lang: String, onBack: () -> Unit) {
    val selections = remember { mutableStateMapOf<String, String>() }
    var focused by remember { mutableStateOf(occlusionChoicesV42.first()) }
    val groups = occlusionChoicesV42.groupBy { it.topic }

    ResponsiveScreenV17(
        tr(lang, "Examen de oclusión · atlas fotográfico", "Occlusion exam · photographic atlas"),
        tr(lang, "Las figuras geométricas fueron sustituidas por fotografías dentales. Toca cada recuadro violeta para abrir su descripción.", "Geometric figures were replaced with dental photographs. Tap each purple card to open its description."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang,
            "Las fotografías se muestran sin recorte intencional (ContentScale.Fit) para conservar su proporción. Requieren conexión la primera vez; el cargador de imágenes puede conservarlas en caché para usos posteriores.",
            "Photos are shown without intentional cropping (ContentScale.Fit) to preserve their proportions. They require a connection the first time; the image loader may cache them for later use."))

        groups.forEach { (topic, options) ->
            ResponsiveSectionV17(topic) {
                val selectedLabel = selections[topic] ?: options.first().label
                val selectedItem = options.firstOrNull { it.label == selectedLabel } ?: options.first()
                val photo = photoForV42(topic, selectedItem.label)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .35f)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        AsyncImage(
                            model = photo.url,
                            contentDescription = photo.caption,
                            modifier = Modifier.fillMaxWidth().height(250.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(photo.caption, fontWeight = FontWeight.SemiBold)
                        Text(photo.credit, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }

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
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(item.label, fontWeight = FontWeight.Black)
                            Text(
                                if (isSelected) tr(lang, "Abierto · mira la descripción debajo", "Open · see description below")
                                else tr(lang, "Toca para saber qué es", "Tap to learn what it is"),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                selections[topic]?.let { label ->
                    options.firstOrNull { it.label == label }?.let { item ->
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
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "Resumen educativo", "Teaching summary")) {
            if (selections.isEmpty()) {
                Text(tr(lang, "Selecciona relaciones oclusales para formar el resumen.", "Select occlusal relationships to build the summary."))
            } else {
                selections.forEach { (topic, value) -> Text("• $topic: $value") }
                val summary = selections.entries.joinToString(" · ") { "${it.key}: ${it.value}" }
                Card(
                    onClick = { TeachingStateV40.moduleSummaries["occlusion"] = summary },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Text(tr(lang, "Guardar resumen de oclusión", "Save occlusion summary"), Modifier.padding(13.dp), fontWeight = FontWeight.Black)
                }
            }
        }

        NoticeCard(tr(lang,
            "Créditos de las fotografías: Wikimedia Commons. Cada ficha conserva el autor/licencia indicados por su archivo original. Las fotos sirven como ejemplos educativos y no sustituyen la exploración clínica.",
            "Photo credits: Wikimedia Commons. Each item retains the author/license stated on its original file page. Photos are educational examples and do not replace clinical examination."))
    }
}
