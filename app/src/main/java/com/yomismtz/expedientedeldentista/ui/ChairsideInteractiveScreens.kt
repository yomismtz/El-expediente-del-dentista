package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen
import kotlin.math.pow

private data class QuickTool(
    val screen: AppScreen,
    val icon: String,
    val es: String,
    val en: String,
    val keywords: String
)

private val quickTools = listOf(
    QuickTool(AppScreen.VITALS, "❤️", "Signos vitales e IMC", "Vital signs and BMI", "respiracion respiraciones frecuencia cardiaca pulso tension presion arterial temperatura glucosa peso talla imc"),
    QuickTool(AppScreen.ATM, "◉", "ATM y músculos", "TMJ and muscles", "atm articulacion dolor click crepitacion apertura desviacion musculos"),
    QuickTool(AppScreen.OCCLUSION, "↔", "Oclusión", "Occlusion", "angle plano terminal canina overjet overbite mordida cruzada abierta linea media"),
    QuickTool(AppScreen.MUCOSA, "👄", "Mucosas y tejidos blandos", "Mucosa and soft tissues", "labio carrillo lengua paladar piso boca gingiva lesion mucosa ganglios"),
    QuickTool(AppScreen.AUXILIARIES, "🧪", "Análisis y auxiliares", "Tests and diagnostic aids", "biometria hemograma quimica sanguinea glucosa coagulacion tp ttp inr histologia biopsia radiografia laboratorio"),
    QuickTool(AppScreen.ODONTOGRAM, "🦷", "Odontograma", "Odontogram", "odontograma caries restauracion diente caras superficies"),
    QuickTool(AppScreen.ICDAS, "🔎", "ICDAS", "ICDAS", "icdas caries codigo superficie oclusal vestibular mesial distal palatino"),
    QuickTool(AppScreen.CPOD, "➕", "CPOD / ceod", "DMFT / dmft", "cpod ceod caries perdido obturado indice"),
    QuickTool(AppScreen.OLEARY, "🔴", "O’Leary", "O’Leary", "oleary placa superficies vestibular lingual palatina mesial distal"),
    QuickTool(AppScreen.IPC, "6️⃣", "IPC", "CPI", "ipc cpi sextantes sangrado calculo bolsa periodontal"),
    QuickTool(AppScreen.IHOS, "🪥", "IHOS", "OHI-S", "ihos ohi placa detritos calculo higiene"),
    QuickTool(AppScreen.PERIODONTOGRAM, "📈", "Periodontograma", "Periodontal chart", "periodontograma sondaje sangrado furca movilidad recesion"),
    QuickTool(AppScreen.PULPAL, "⚡", "Diagnóstico pulpar interactivo", "Interactive pulpal diagnosis", "pulpar frio calor dolor espontaneo nocturno necrosis pulpitis prueba sensibilidad"),
    QuickTool(AppScreen.APICAL, "🩻", "Diagnóstico periapical interactivo", "Interactive periapical diagnosis", "periapical percusion palpacion radiolucidez absceso periodontitis apical"),
    QuickTool(AppScreen.ENDO, "⚡", "Ficha endodóntica", "Endodontic sheet", "endo endodoncia conductometria longitud trabajo pulpotomia pulpectomia conductos"),
    QuickTool(AppScreen.PROSTHETIC, "🦷", "Prótesis y Kennedy", "Prosthetics and Kennedy", "protesis kennedy edentulo arcada maxilar mandibular pilares"),
    QuickTool(AppScreen.SURGICAL, "✚", "Ficha quirúrgica", "Surgical sheet", "cirugia extraccion exodoncia hemostasia quirurgica"),
    QuickTool(AppScreen.EVOLUTION, "📋", "Notas de evolución", "Progress notes", "nota alta baja ingreso resina amalgama corona extraccion profilaxis ultrasonido evolucion")
)

@Composable
fun QuickClinicalAccess(lang: String, onOpen: (AppScreen) -> Unit) {
    var query by remember { mutableStateOf("") }
    val normalized = query.trim().lowercase()
    val matches = if (normalized.isBlank()) quickTools.take(6) else quickTools.filter {
        (it.es + " " + it.en + " " + it.keywords).lowercase().contains(normalized)
    }.take(8)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("🔎 ${tr(lang, "Consulta rápida durante clínica", "Quick chairside lookup")}", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it.take(40) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(tr(lang, "Ej. prueba de frío, Kennedy, O’Leary…", "e.g. cold test, Kennedy, O’Leary…")) }
            )
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                matches.forEach { tool ->
                    OutlinedButton(onClick = { onOpen(tool.screen) }) {
                        Text("${tool.icon} ${if (lang == "en") tool.en else tool.es}")
                    }
                }
                if (matches.isEmpty()) {
                    Text(tr(lang, "No encontré ese término; prueba con una palabra clínica más corta.", "No match; try a shorter clinical term."))
                }
            }
        }
    }
}

private data class VitalBand(
    val label: String,
    val rrMin: Int,
    val rrMax: Int,
    val hrMin: Int,
    val hrMax: Int,
    val sysMin: Int,
    val sysMax: Int,
    val diaMin: Int,
    val diaMax: Int
)

private fun classify(value: Double?, min: Double, max: Double, lang: String): String = when {
    value == null -> tr(lang, "Sin dato", "No value")
    value < min -> tr(lang, "↓ por debajo de la referencia didáctica", "↓ below teaching reference")
    value > max -> tr(lang, "↑ por encima de la referencia didáctica", "↑ above teaching reference")
    else -> tr(lang, "✓ dentro de la referencia didáctica", "✓ within teaching reference")
}

@Composable
fun VitalsInteractiveScreen(lang: String, onBack: () -> Unit) {
    val bands = listOf(
        VitalBand("0–6 m", 30, 50, 82, 205, 60, 90, 30, 62),
        VitalBand("6 m–2 a", 20, 40, 100, 190, 60, 90, 30, 62),
        VitalBand("2–7 a", 15, 30, 60, 140, 78, 112, 48, 78),
        VitalBand("8–11 a", 15, 25, 60, 140, 85, 114, 52, 85),
        VitalBand("≥12 a", 13, 20, 60, 100, 95, 135, 58, 88),
        VitalBand(tr(lang, "Adulto", "Adult"), 12, 20, 60, 100, 100, 140, 60, 90)
    )
    var bandIndex by remember { mutableStateOf(5) }
    var rr by remember { mutableStateOf("") }
    var hr by remember { mutableStateOf("") }
    var sys by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var glucose by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val band = bands[bandIndex]
    val bmi = run {
        val w = weight.toDoubleOrNull()
        val h = height.toDoubleOrNull()?.div(100.0)
        if (w != null && h != null && h > 0) w / h.pow(2) else null
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Signos vitales · práctica interactiva", "Vital signs · interactive practice"),
                onBack,
                tr(lang,
                    "Escribe los valores que estás observando en el expediente físico. La app los compara con las referencias didácticas incluidas en el material; no guarda estos datos.",
                    "Enter the values you are observing in the physical record. The app compares them with teaching references and does not save them."
                )
            )
        }
        item {
            SectionCard(tr(lang, "1 · Selecciona grupo de edad", "1 · Select age group")) {
                bands.chunked(3).forEachIndexed { row, chunk ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        chunk.forEachIndexed { col, b ->
                            val index = row * 3 + col
                            FilterChip(
                                selected = bandIndex == index,
                                onClick = { bandIndex = index },
                                label = { Text(b.label) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "2 · Registra y comprueba", "2 · Enter and check")) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(rr, { rr = it.filter(Char::isDigit).take(3) }, label = { Text("FR /min") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(hr, { hr = it.filter(Char::isDigit).take(3) }, label = { Text("FC /min") }, modifier = Modifier.weight(1f))
                }
                Text("FR ${band.rrMin}–${band.rrMax}: ${classify(rr.toDoubleOrNull(), band.rrMin.toDouble(), band.rrMax.toDouble(), lang)}")
                Text("FC ${band.hrMin}–${band.hrMax}: ${classify(hr.toDoubleOrNull(), band.hrMin.toDouble(), band.hrMax.toDouble(), lang)}")
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(sys, { sys = it.filter(Char::isDigit).take(3) }, label = { Text(tr(lang, "TA sistólica", "Systolic BP")) }, modifier = Modifier.weight(1f))
                    OutlinedTextField(dia, { dia = it.filter(Char::isDigit).take(3) }, label = { Text(tr(lang, "TA diastólica", "Diastolic BP")) }, modifier = Modifier.weight(1f))
                }
                Text("TA sistólica ${band.sysMin}–${band.sysMax}: ${classify(sys.toDoubleOrNull(), band.sysMin.toDouble(), band.sysMax.toDouble(), lang)}")
                Text("TA diastólica ${band.diaMin}–${band.diaMax}: ${classify(dia.toDoubleOrNull(), band.diaMin.toDouble(), band.diaMax.toDouble(), lang)}")
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(temperature, { temperature = it.filter { c -> c.isDigit() || c == '.' }.take(5) }, label = { Text("°C") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(glucose, { glucose = it.filter(Char::isDigit).take(4) }, label = { Text(tr(lang, "Glucosa capilar", "Capillary glucose")) }, modifier = Modifier.weight(1f))
                }
                Text(tr(lang,
                    "Temperatura y glucosa se muestran para practicar el registro, pero deben interpretarse según sitio/método, ayuno o no ayuno, dispositivo y referencia institucional.",
                    "Temperature and glucose are recorded for practice but should be interpreted according to site/method, fasting state, device and institutional reference."
                ))
            }
        }
        item {
            SectionCard("IMC / BMI") {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(weight, { weight = it.filter { c -> c.isDigit() || c == '.' }.take(6) }, label = { Text("kg") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(height, { height = it.filter { c -> c.isDigit() || c == '.' }.take(6) }, label = { Text("cm") }, modifier = Modifier.weight(1f))
                }
                Text(if (bmi == null) tr(lang, "IMC = peso (kg) / talla² (m)", "BMI = weight (kg) / height² (m)") else "IMC = ${"%.1f".format(bmi)} kg/m²", fontWeight = FontWeight.Bold)
                Text(tr(lang,
                    "En niños y adolescentes el IMC se interpreta por edad y sexo mediante percentiles; no uses puntos de corte de adulto.",
                    "In children and adolescents BMI is interpreted by age and sex percentiles; adult cutoffs do not apply."
                ))
            }
        }
        item {
            SectionCard(tr(lang, "3 · Cómo tomar bien el dato", "3 · How to measure correctly")) {
                Text("🫁 ${tr(lang, "FR: observa ciclos respiratorios completos; evita modificar conscientemente el patrón del paciente.", "RR: observe complete respiratory cycles and avoid consciously changing the patient's pattern.")}")
                Text("❤️ ${tr(lang, "FC: palpa un pulso adecuado o usa monitor validado; registra frecuencia y, cuando proceda, ritmo.", "HR: palpate an appropriate pulse or use a validated monitor; record rate and, when relevant, rhythm.")}")
                Text("🩺 ${tr(lang, "TA: reposo previo, brazo apoyado y manguito de tamaño correcto; repite si el valor no concuerda con el contexto.", "BP: rest beforehand, supported arm and correct cuff size; repeat when the value conflicts with the clinical context.")}")
                Text("🌡️ ${tr(lang, "Temperatura: anota el sitio o método cuando cambie la interpretación.", "Temperature: document site/method when it changes interpretation.")}")
            }
        }
        item {
            NoticeCard(tr(lang,
                "Resultado educativo, no autorización clínica. Si un valor está fuera de rango, confirma técnica/medición y consulta al docente antes de decidir si procede un tratamiento.",
                "Educational result, not clinical clearance. If a value is outside range, confirm technique/measurement and consult faculty before deciding whether treatment proceeds."
            ))
        }
    }
}

private data class LabGuide(
    val icon: String,
    val es: String,
    val en: String,
    val whyEs: String,
    val whyEn: String,
    val exampleEs: String,
    val exampleEn: String
)

@Composable
fun AuxiliariesInteractiveScreen(lang: String, onBack: () -> Unit) {
    val guides = listOf(
        LabGuide("🩸", "Biometría hemática", "Complete blood count",
            "Ayuda a revisar hemoglobina/hematocrito, leucocitos y plaquetas cuando la historia o el procedimiento justifican valorar anemia, infección, respuesta inflamatoria o capacidad hemostática.",
            "Helps review hemoglobin/hematocrit, leukocytes and platelets when history/procedure justifies assessing anemia, infection, inflammation or hemostatic capacity.",
            "Ejemplo: cirugía planeada en una persona con antecedente de sangrado, fatiga marcada o enfermedad hematológica.",
            "Example: planned surgery in a person with bleeding history, marked fatigue or hematologic disease."),
        LabGuide("🧪", "Química sanguínea", "Blood chemistry",
            "Integra analitos metabólicos y de función orgánica. La utilidad odontológica depende del antecedente: diabetes, enfermedad renal/hepática, medicamentos o cirugía, entre otros.",
            "Integrates metabolic and organ-function analytes. Dental relevance depends on history such as diabetes, renal/hepatic disease, medication or surgery.",
            "Ejemplo: valorar estudios recientes cuando un antecedente sistémico puede modificar cicatrización, medicación o manejo quirúrgico.",
            "Example: review recent tests when systemic history may alter healing, medication or surgical management."),
        LabGuide("⏱", "Pruebas de coagulación", "Coagulation tests",
            "TP/INR y TTPa se interpretan según indicación clínica, tratamiento anticoagulante, enfermedad hepática o trastorno de coagulación. No sustituyen la historia de sangrado ni el recuento plaquetario.",
            "PT/INR and aPTT are interpreted according to clinical indication, anticoagulation, liver disease or coagulation disorder. They do not replace bleeding history or platelet count.",
            "Ejemplo: procedimiento invasivo en paciente con anticoagulante o antecedente de alteración de coagulación.",
            "Example: invasive procedure in a patient taking an anticoagulant or with a coagulation-disorder history."),
        LabGuide("🔬", "Histopatología / biopsia", "Histopathology / biopsy",
            "Permite estudiar tejido obtenido de una lesión y correlacionar la descripción clínica con un diagnóstico microscópico. La muestra debe manejarse según protocolo del servicio de patología.",
            "Allows tissue from a lesion to be examined and correlated with the clinical description. Handle specimens according to pathology-service protocol.",
            "Ejemplo: lesión de mucosa persistente o con características que justifican valoración y estudio histológico.",
            "Example: persistent mucosal lesion or one with features warranting specialist assessment and histologic study."),
        LabGuide("🩻", "Imagenología", "Imaging",
            "La elección de periapical, bitewing, panorámica, cefalometría u otro estudio depende de la pregunta diagnóstica y del principio de justificación.",
            "Choice of periapical, bitewing, panoramic, cephalometric or other imaging depends on the diagnostic question and justification principle.",
            "Ejemplo: periapical para valorar región apical de un diente con signos/síntomas endodónticos.",
            "Example: periapical image to assess the apical region of a tooth with endodontic signs/symptoms."),
        LabGuide("🧫", "Microbiología y otras pruebas", "Microbiology and other tests",
            "Son auxiliares seleccionados para preguntas concretas; no deben solicitarse por rutina sin una indicación que cambie diagnóstico o manejo.",
            "These are selected for specific questions and should not be ordered routinely without an indication that can change diagnosis or management.",
            "Ejemplo: estudio dirigido cuando una infección o lesión requiere identificación adicional bajo supervisión.",
            "Example: targeted testing when an infection or lesion needs additional identification under supervision.")
    )
    var selected by remember { mutableStateOf(0) }
    var analyte by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var min by remember { mutableStateOf("") }
    var max by remember { mutableStateOf("") }
    val value = result.toDoubleOrNull()
    val minValue = min.toDoubleOrNull()
    val maxValue = max.toDoubleOrNull()
    val interpretation = when {
        value == null || minValue == null || maxValue == null || minValue >= maxValue -> tr(lang, "Escribe resultado y rango del laboratorio.", "Enter result and the laboratory range.")
        value < minValue -> tr(lang, "↓ Resultado por debajo del intervalo capturado", "↓ Result below entered interval")
        value > maxValue -> tr(lang, "↑ Resultado por encima del intervalo capturado", "↑ Result above entered interval")
        else -> tr(lang, "✓ Resultado dentro del intervalo capturado", "✓ Result within entered interval")
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Análisis y auxiliares · interactivo", "Tests and diagnostic aids · interactive"),
                onBack,
                tr(lang,
                    "Selecciona el estudio para saber por qué puede ser importante en odontología. Para valores de laboratorio, captura el intervalo que aparece en el propio reporte y la app compara el resultado.",
                    "Select a study to see why it may matter in dentistry. For lab values, enter the reference interval printed on the report and the app compares the result."
                )
            )
        }
        item {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                guides.forEachIndexed { index, guide ->
                    FilterChip(selected == index, { selected = index }, { Text("${guide.icon} ${if (lang == "en") guide.en else guide.es}") })
                }
            }
        }
        item {
            val guide = guides[selected]
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${guide.icon} ${if (lang == "en") guide.en else guide.es}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(if (lang == "en") guide.whyEn else guide.whyEs)
                    Text("💡 ${if (lang == "en") guide.exampleEn else guide.exampleEs}")
                }
            }
        }
        item {
            SectionCard(tr(lang, "Comprobador de resultados", "Result checker")) {
                OutlinedTextField(analyte, { analyte = it.take(40) }, modifier = Modifier.fillMaxWidth(), label = { Text(tr(lang, "Analito (ej. hemoglobina)", "Analyte (e.g. hemoglobin)")) })
                OutlinedTextField(result, { result = it.filter { c -> c.isDigit() || c == '.' || c == '-' }.take(12) }, modifier = Modifier.fillMaxWidth(), label = { Text(tr(lang, "Resultado", "Result")) })
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(min, { min = it.filter { c -> c.isDigit() || c == '.' || c == '-' }.take(12) }, modifier = Modifier.weight(1f), label = { Text(tr(lang, "Límite inferior", "Lower limit")) })
                    OutlinedTextField(max, { max = it.filter { c -> c.isDigit() || c == '.' || c == '-' }.take(12) }, modifier = Modifier.weight(1f), label = { Text(tr(lang, "Límite superior", "Upper limit")) })
                }
                Text((if (analyte.isBlank()) "" else "$analyte · ") + interpretation, fontWeight = FontWeight.Bold)
                Text(tr(lang,
                    "La app no fija un único rango universal: usa exactamente el intervalo impreso por el laboratorio, porque puede variar por método, edad, sexo y contexto.",
                    "The app does not impose one universal range: use the interval printed by the laboratory because it may vary by method, age, sex and context."
                ))
            }
        }
        item {
            SectionCard(tr(lang, "Antes de solicitar un auxiliar", "Before requesting a test")) {
                Text("1. ${tr(lang, "¿Qué pregunta clínica quiero responder?", "What clinical question am I trying to answer?")}")
                Text("2. ${tr(lang, "¿El resultado puede cambiar diagnóstico, riesgo o manejo?", "Can the result change diagnosis, risk or management?")}")
                Text("3. ${tr(lang, "¿Existe un estudio más simple o más apropiado?", "Is there a simpler or more appropriate study?")}")
                Text("4. ${tr(lang, "¿Necesito interconsulta o supervisión antes de interpretar?", "Do I need consultation or faculty supervision before interpreting it?")}")
            }
        }
        item {
            NoticeCard(tr(lang,
                "El comprobador solo indica si un número cae dentro o fuera del intervalo que tú capturaste. No diagnostica enfermedades ni autoriza procedimientos.",
                "The checker only tells whether a number falls inside or outside the interval you entered. It does not diagnose disease or clear procedures."
            ))
        }
    }
}
