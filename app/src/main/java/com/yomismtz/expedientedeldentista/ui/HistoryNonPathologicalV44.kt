package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R

@Composable
fun NonPathologicalHubV44Screen(lang: String, onBack: () -> Unit) {
    var section by remember { mutableStateOf(0) }
    if (section == 1) {
        NonPathologicalV40Screen(lang) { section = 0 }
        return
    }
    if (section == 2) {
        GyneObstetricTeachingV44Screen(lang) { section = 0 }
        return
    }
    ResponsiveScreenV17(
        tr(lang, "Antecedentes personales no patológicos", "Non-pathological personal history"),
        tr(lang, "Guía interactiva; no captura datos personales reales.", "Interactive teaching guide; it does not capture real personal data."),
        onBack
    ) { _ ->
        Card(
            onClick = { section = 1 },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("🏠 Vivienda · higiene · alimentación · hábitos", fontWeight = FontWeight.Black)
                Text("Opciones educativas de vivienda, servicios, higiene general/bucal, alimentación, hábitos y exposiciones.")
            }
        }
        Card(
            onClick = { section = 2 },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("♀ Antecedentes gineco-obstétricos", fontWeight = FontWeight.Black)
                Text("Menarquia, inicio de vida sexual, anticoncepción, embarazos, partos, abortos y menopausia.")
            }
        }
    }
}

private data class GyneItemV44(
    val title: String,
    val what: String,
    val usual: String,
    val howToRecord: String,
    val choices: List<String>
)

private val gyneItemsV44 = listOf(
    GyneItemV44(
        "Menarquia",
        "Primera menstruación de la vida.",
        "La mediana suele estar alrededor de 12–13 años; por lo general ya ocurrió hacia los 15 años. No se usa una edad aislada para diagnosticar.",
        "Registrar la edad aproximada referida y, si el ejercicio lo pide, regularidad/cambios del ciclo.",
        listOf("<10 años · muy temprana/valorar contexto", "10–11 años", "12–13 años · frecuente", "14–15 años", ">15 años sin menarquia · requiere valoración clínica", "No recuerda")
    ),
    GyneItemV44(
        "Inicio de vida sexual activa",
        "Edad en la que la persona refiere haber iniciado actividad sexual. Es un antecedente confidencial y debe preguntarse sin juicio ni suposiciones.",
        "No existe una edad clínica 'normal' que deba imponerse. La OMS define adolescencia como 10–19 años; en una historia real se registra sólo lo que la persona decide referir.",
        "Registrar edad aproximada de inicio, uso de barrera/anticoncepción y riesgos relevantes sólo cuando corresponda al objetivo clínico.",
        listOf("No ha iniciado", "10–14 años", "15–17 años", "18–19 años", "20–29 años", "≥30 años", "Prefiere no responder")
    ),
    GyneItemV44(
        "Uso de anticonceptivos",
        "Identifica si se utiliza actualmente o se utilizó antes algún método anticonceptivo.",
        "No depende de una edad única. La elección debe individualizarse según salud, anatomía, preferencias y elegibilidad médica.",
        "Registrar método actual o previo; no modificar ni suspender tratamientos desde esta app.",
        listOf("Ninguno", "Preservativo externo", "Preservativo interno", "Píldora combinada", "Píldora sólo progestágeno", "Parche", "Anillo vaginal", "Inyección", "Implante", "DIU de cobre", "DIU hormonal", "Diafragma/capuchón", "Vasectomía de la pareja", "Otro")
    ),
    GyneItemV44(
        "Embarazos a término",
        "Embarazos que alcanzaron al menos 37 semanas; clínicamente se distinguen término temprano 37+0–38+6, término completo 39+0–40+6 y término tardío 41+0–41+6.",
        "No tiene una edad 'habitual' útil para el registro; se documenta cada antecedente y su desenlace.",
        "Registrar número de embarazos que llegaron a término y, si el formato lo solicita, vía de nacimiento y complicaciones.",
        listOf("0", "1", "2", "3", "4", "≥5")
    ),
    GyneItemV44(
        "Embarazo precoz / adolescente",
        "Embarazo durante la adolescencia. La OMS considera adolescentes a las personas de 10–19 años.",
        "Se registra la edad en que ocurrió; no se utiliza como etiqueta de juicio.",
        "En el ejercicio, seleccionar el grupo de edad y documentar desenlace/complicaciones si son relevantes.",
        listOf("Ninguno", "10–14 años", "15–17 años", "18–19 años", "No recuerda")
    ),
    GyneItemV44(
        "Número de partos",
        "Cantidad de nacimientos/partos previos. No es sinónimo del número total de embarazos.",
        "Se pregunta en cualquier edad reproductiva o posterior cuando sea clínicamente pertinente.",
        "Registrar la cantidad; si el expediente usa una fórmula obstétrica, mantener la convención institucional.",
        listOf("0", "1", "2", "3", "4", "≥5")
    ),
    GyneItemV44(
        "Abortos / pérdidas gestacionales",
        "Pérdida o terminación del embarazo antes de la viabilidad según la definición clínica utilizada. 'Natural' y 'espontáneo' describen el mismo concepto; 'provocado' corresponde a aborto inducido.",
        "No existe edad frecuente que deba asumirse. Es un antecedente sensible y debe preguntarse con privacidad y lenguaje neutral.",
        "Registrar número total y, sólo si es relevante, diferenciar espontáneo e inducido según lo referido.",
        listOf("0", "1 espontáneo", "≥2 espontáneos", "1 inducido", "≥2 inducidos", "Mixtos", "Prefiere no responder")
    ),
    GyneItemV44(
        "Menopausia",
        "Cese permanente de la menstruación; se reconoce retrospectivamente después de 12 meses consecutivos sin menstruación si no existe otra causa.",
        "Suele ocurrir entre 45 y 55 años; la edad promedio ronda 51–52 años según la población.",
        "Registrar edad aproximada de la última menstruación/menopausia, si fue natural o inducida médicamente/quirúrgicamente y tratamientos relevantes.",
        listOf("No aplica / premenopausia", "<40 años · menopausia prematura/valorar", "40–44 años", "45–49 años", "50–54 años", "≥55 años", "Inducida médica/quirúrgicamente", "No recuerda")
    )
)

@Composable
fun GyneObstetricTeachingV44Screen(lang: String, onBack: () -> Unit) {
    var open by remember { mutableStateOf<Int?>(0) }
    var selected by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    ResponsiveScreenV17(
        tr(lang, "Antecedentes gineco-obstétricos", "Gynecologic-obstetric history"),
        tr(lang, "Toca cada rubro para saber qué significa, qué edades son frecuentes y cómo se registra.", "Tap each item to learn what it means, common ages and how it is recorded."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang,
            "Es material educativo. En una historia real estos datos son sensibles: se preguntan con privacidad, consentimiento, lenguaje neutral y sólo cuando son clínicamente pertinentes.",
            "This is teaching material. In a real history these data are sensitive: ask privately, with consent, neutral language and only when clinically relevant."))

        gyneItemsV44.forEachIndexed { index, item ->
            Card(
                onClick = { open = if (open == index) null else index },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (open == index) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(item.title, fontWeight = FontWeight.Black)
                    if (open == index) {
                        Text("¿Qué es?", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Text(item.what)
                        Text("Edad / momento frecuente", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Text(item.usual)
                        Text("¿Cómo se registra?", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Text(item.howToRecord)
                        AdaptiveGridV17(item.choices.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                            val choice = item.choices[i]
                            FilterChip(
                                selected = selected[item.title] == choice,
                                onClick = { selected = selected + (item.title to choice) },
                                label = { Text(choice) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "Anticonceptivos · imagen de referencia", "Contraception · reference image")) {
            Image(
                painter = painterResource(R.drawable.contraceptives_reference),
                contentDescription = tr(lang, "Tabla de métodos anticonceptivos proporcionada por la autora", "Contraceptive methods reference table provided by the author"),
                modifier = Modifier.fillMaxWidth().height(280.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                tr(lang, "Imagen proporcionada por la autora; se conserva visualmente sin recortes ni rediseño.", "Image provided by the author; kept visually without cropping or redesign."),
                style = MaterialTheme.typography.bodySmall
            )
            Text("Métodos con uso femenino/anatomía uterina: píldoras, parche, anillo, inyección, implante, DIU de cobre u hormonal, preservativo interno, diafragma/capuchón. Métodos de uso masculino: preservativo externo y vasectomía. El preservativo ayuda además a reducir el riesgo de ITS.")
        }

        ResponsiveSectionV17(tr(lang, "Resumen del ejercicio", "Exercise summary")) {
            if (selected.isEmpty()) Text("Aún no hay selecciones.")
            selected.forEach { (k, v) -> Text("• $k: $v") }
        }

        NoticeCard("Fuentes clínicas: ACOG · Menstruation in Girls and Adolescents; ACOG/SMFM · Definition of Term Pregnancy; OMS · Embarazo en la adolescencia; CDC · Contraception and Birth Control Methods; NIH/NICHD · Menopause.")
    }
}
