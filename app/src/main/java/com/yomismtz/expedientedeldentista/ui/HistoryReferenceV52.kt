package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun GynecoObstetricGuideV52Screen(lang: String, onBack: () -> Unit) {
    var openTopic by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    var applies by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<Boolean?>(null) }
    val contraception = listOf(
        Triple("Combinado oral", "Combined oral contraceptive", "Tabletas con estrógeno y progestágeno. Se toman siguiendo el esquema del producto y requieren valorar antecedentes y contraindicaciones."),
        Triple("Píldora de progestágeno", "Progestin-only pill", "Tableta sin estrógeno. Se usa diariamente y la regularidad de la toma es especialmente importante."),
        Triple("Parche anticonceptivo", "Contraceptive patch", "Parche hormonal transdérmico que se cambia según un esquema periódico. Su elegibilidad depende de la historia clínica."),
        Triple("Anillo vaginal", "Vaginal ring", "Dispositivo hormonal flexible colocado en la vagina y usado siguiendo ciclos definidos por el producto."),
        Triple("Inyectable", "Injectable contraceptive", "Progestágeno o combinación hormonal administrada mediante inyección a intervalos establecidos."),
        Triple("Implante subdérmico", "Subdermal implant", "Varilla con progestágeno colocada bajo la piel por personal capacitado; es un método reversible de larga duración."),
        Triple("DIU de cobre", "Copper IUD", "Dispositivo intrauterino no hormonal colocado por personal capacitado; es reversible y de larga duración."),
        Triple("Sistema intrauterino con levonorgestrel", "Levonorgestrel intrauterine system", "Dispositivo intrauterino que libera progestágeno localmente; requiere colocación por personal capacitado."),
        Triple("Preservativo externo o interno", "External or internal condom", "Método de barrera utilizado en cada relación sexual. Los preservativos también reducen el riesgo de infecciones de transmisión sexual."),
        Triple("Otros métodos de barrera", "Other barrier methods", "Incluyen diafragma y capuchón cervical, utilizados para impedir el paso de espermatozoides; algunos se combinan con espermicida."),
        Triple("Anticoncepción de emergencia", "Emergency contraception", "Método utilizado después de una relación sin protección o falla del método habitual; no sustituye un método regular."),
        Triple("Conocimiento de la fertilidad", "Fertility-awareness methods", "Métodos que identifican días fértiles mediante el ciclo y otros signos. Requieren aprendizaje y seguimiento consistente."),
        Triple("Esterilización", "Sterilization", "Método permanente mediante procedimiento quirúrgico. Debe considerarse como una decisión definitiva.")
    )
    ResponsiveScreenV17(
        tr(lang, "VI. Antecedentes gineco-obstétricos", "VI. Gynecologic-obstetric history"),
        tr(lang, "Guía para aprender qué preguntar, cómo organizar la respuesta y qué significa cada campo del expediente.", "Guide for learning what to ask, how to organize the answer and what each record field means."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang, "Este módulo enseña a llenar el apartado. Practica una redacción objetiva y registra sólo lo que la persona refiera; no infieras diagnósticos.", "This module teaches how to complete the section. Practice objective wording and record only what the person reports; do not infer diagnoses."))
        ResponsiveSectionV17(tr(lang, "¿Este apartado aplica?", "Does this section apply?")) {
            Text(tr(lang, "En este formato educativo, si el paciente es hombre selecciona «No aplica» y no se llena el interrogatorio gineco-obstétrico. Si la paciente es mujer, selecciona «Sí aplica» para estudiar y completar los campos correspondientes.", "In this educational form, if the patient is male select “Not applicable” and do not complete the gynecologic-obstetric interview. If the patient is female, select “Applies” to study and complete the corresponding fields."))
            androidx.compose.foundation.layout.Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                androidx.compose.material3.FilterChip(
                    selected = applies == false,
                    onClick = { applies = false },
                    label = { Text(tr(lang, "Hombre · No aplica", "Male · Not applicable")) },
                    modifier = Modifier.weight(1f)
                )
                androidx.compose.material3.FilterChip(
                    selected = applies == true,
                    onClick = { applies = true },
                    label = { Text(tr(lang, "Mujer · Sí aplica", "Female · Applies")) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if (applies == false) {
            NoticeCard(tr(lang, "Registro educativo: antecedentes gineco-obstétricos — No aplica. No es necesario llenar los campos siguientes.", "Teaching record: gynecologic-obstetric history — Not applicable. The following fields do not need to be completed."))
            return@ResponsiveScreenV17
        }
        if (applies == null) {
            NoticeCard(tr(lang, "Selecciona primero si el apartado aplica para continuar con el interrogatorio.", "First select whether this section applies to continue with the interview."))
            return@ResponsiveScreenV17
        }
        val fields = listOf(
            "Menarca" to "Edad de la primera menstruación.",
            "Patrón menstrual y FUM" to "Regularidad, características relevantes y fecha de última menstruación cuando corresponda.",
            "Inicio de vida sexual activa" to "Edad referida de inicio; evita juicios o suposiciones.",
            "Gestas" to "Número total de embarazos referidos.",
            "Abortos" to "Número total y desglose en espontáneos e inducidos.",
            "Partos / paras" to "Número de partos referidos; registra además cesáreas cuando corresponda.",
            "Resultados de los embarazos" to "Nacidos vivos y pérdidas fetales/perinatales referidas, usando la terminología del formato clínico.",
            "Embarazo actual" to "Si existe embarazo actual y edad gestacional referida.",
            "Lactancia" to "Si actualmente lacta o si existe un antecedente relevante para el interrogatorio.",
            "Menopausia / climaterio" to "Si ya inició, edad de inicio referida y síntomas relevantes.",
            "Medicamentos en menopausia" to "Terapia hormonal u otros medicamentos referidos; registra nombre si la persona lo conoce.",
            "Anticoncepción" to "Si utiliza algún método, cuál, desde cuándo y el motivo referido.",
            "Condición sistémica y anticoncepción" to "Pregunta si el método fue indicado o elegido en relación con una enfermedad o condición sistémica. No concluyas por la app si el método es apropiado."
        )
        ResponsiveSectionV17(tr(lang, "Qué debes preguntar", "What to ask")) {
            fields.forEach { (title, detail) ->
                Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)) {
                    Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(4.dp)) {
                        Text(title, fontWeight=FontWeight.Black)
                        Text(detail, style=MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        ResponsiveSectionV17(tr(lang, "Métodos anticonceptivos · toca cada opción", "Contraceptive methods · tap each option")) {
            Text(tr(lang, "El objetivo es que el estudiante reconozca qué es cada método y cómo se utiliza de forma general. No es una herramienta para prescribir ni seleccionar el método de una paciente.", "The goal is for the student to recognize what each method is and how it is generally used. This is not a tool for prescribing or selecting a method for a patient."), style=MaterialTheme.typography.bodySmall)
            AdaptiveGridV17(contraception.size, if(profile.largeSystemText || profile.width==ScreenWidthV17.COMPACT) 1 else 2) { i ->
                val item = contraception[i]
                Card(
                    onClick = { openTopic = if(openTopic == item.first) null else item.first },
                    modifier = Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(5.dp)) {
                        Text(if(lang=="en") item.second else item.first, fontWeight=FontWeight.Black)
                        if(openTopic == item.first) Text(
                            if(lang=="en") when(item.first) {
                                "Combinado oral" -> "Pills containing estrogen and progestin. They are taken according to the product schedule and require review of history and contraindications."
                                "Píldora de progestágeno" -> "An estrogen-free daily pill. Consistent timing is especially important."
                                "Parche anticonceptivo" -> "A transdermal hormonal patch changed on a periodic schedule. Eligibility depends on clinical history."
                                "Anillo vaginal" -> "A flexible hormonal device placed in the vagina and used in cycles defined by the product."
                                "Inyectable" -> "Progestin or combined hormonal contraception given by injection at established intervals."
                                "Implante subdérmico" -> "A progestin-releasing rod placed under the skin by trained personnel; a long-acting reversible method."
                                "DIU de cobre" -> "A non-hormonal intrauterine device placed by trained personnel; reversible and long acting."
                                "Sistema intrauterino con levonorgestrel" -> "An intrauterine device that releases progestin locally and is placed by trained personnel."
                                "Preservativo externo o interno" -> "A barrier method used with each sexual act. Condoms also reduce the risk of sexually transmitted infections."
                                "Otros métodos de barrera" -> "These include diaphragms and cervical caps; some are used with spermicide."
                                "Anticoncepción de emergencia" -> "Used after unprotected sex or failure of the regular method; it does not replace regular contraception."
                                "Conocimiento de la fertilidad" -> "Methods that identify fertile days using the cycle and other signs; they require learning and consistent tracking."
                                else -> "A permanent contraceptive method involving a procedure and intended as a definitive decision."
                            } else item.third,
                            style=MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        ResponsiveSectionV17(tr(lang, "Cómo redactarlo", "How to document it")) {
            Text(tr(lang, "Ejemplo educativo: «Menarca: 12 años. IVSA: 18 años. G3 P2 A1; aborto espontáneo referido. Dos nacidos vivos. FUM referida: ___. Método anticonceptivo: ___.» Adapta siempre la redacción a lo realmente referido y al formato que estés aprendiendo a llenar.", "Teaching example: “Menarche: age 12. Sexual activity began at 18. G3 P2 A1; reported spontaneous abortion. Two live births. Reported LMP: ___. Contraceptive method: ___.” Always adapt the wording to what was actually reported and to the form you are learning to complete."))
        }
    }
}

@Composable
internal fun HistoryPeriodontalHubV52Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "XV. Examen periodontal", "XV. Periodontal examination"),
        tr(lang, "El formato de referencia agrupa IPC, IHOS y observaciones gingivales/periodontales.", "The reference form groups CPI, OHI-S and gingival/periodontal observations."),
        onBack
    ) {
        ResponsiveSectionV17("IPC / CPI") { Text(tr(lang, "Evalúa el índice con su protocolo por edad, sextantes/dientes índice y hallazgos registrados.", "Use the age protocol, sextants/index teeth and recorded findings.")) }
        ResponsiveSectionV17("IHOS / OHI-S") { Text(tr(lang, "Registra depósitos blandos y cálculo en las superficies indicadas y calcula el índice.", "Record debris and calculus on the indicated surfaces and calculate the index.")) }
        ResponsiveSectionV17(tr(lang, "Observaciones periodontales", "Periodontal observations")) { Text(tr(lang, "Describe encía, sangrado, cálculo, movilidad y otros hallazgos observables sin convertir una imagen aislada en diagnóstico.", "Describe gingiva, bleeding, calculus, mobility and other observable findings without turning an isolated image into a diagnosis.")) }
        NoticeCard(tr(lang, "Los módulos interactivos IPC, IHOS y periodontograma siguen disponibles en el índice general del expediente.", "Interactive CPI, OHI-S and periodontogram modules remain available from the general record index."))
    }
}

@Composable
internal fun RadiographicStudiesV52Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "XVI. Estudios radiográficos", "XVI. Radiographic studies"),
        tr(lang, "Organizado según el apartado radiográfico del formato de referencia.", "Organized according to the radiographic section of the reference form."),
        onBack
    ) {
        val studies = listOf(
            "Dentoalveolar / periapical" to "Dentoalveolar / periapical",
            "Aleta mordible" to "Bitewing",
            "Oclusal" to "Occlusal",
            "Extraoral" to "Extraoral"
        )
        studies.forEach { (es, en) ->
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (lang == "en") en else es, fontWeight = FontWeight.Black)
                    Text(tr(lang, "Primero identifica el tipo de estudio; después describe hallazgos objetivos y correlación clínica.", "Identify the study type first; then describe objective findings and clinical correlation."))
                }
            }
        }
    }
}

@Composable
internal fun HistoryOdontogramGuideV52Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "XVIII. Odontograma · CPOD/ceod", "XVIII. Odontogram · DMFT/dmft"),
        tr(lang, "Cierre del índice de historia clínica de referencia.", "Final section of the reference clinical-history index."),
        onBack
    ) {
        ResponsiveSectionV17(tr(lang, "Odontograma", "Odontogram")) {
            Text(tr(lang, "Registra por órgano dentario y superficie usando la convención del formato. Diferencia hallazgo clínico, restauración, ausencia y tratamiento indicado.", "Record by tooth and surface using the form convention. Distinguish clinical finding, restoration, absence and planned treatment."))
        }
        ResponsiveSectionV17("CPOD / ceod") {
            Text(tr(lang, "Separa dentición permanente y temporal y evita mezclar los componentes de ambos índices.", "Keep permanent and primary dentitions separate and do not mix the components of the two indices."))
        }
        NoticeCard(tr(lang, "El odontograma interactivo y la calculadora CPOD/ceod permanecen en Odontograma e índices para conservar toda la funcionalidad existente.", "The interactive odontogram and DMFT/dmft calculator remain under Odontogram and indices to preserve existing functionality."))
    }
}
