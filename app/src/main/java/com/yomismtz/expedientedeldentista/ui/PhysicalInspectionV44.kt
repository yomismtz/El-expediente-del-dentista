package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class InspectionRubricV44(
    val title: String,
    val observe: String,
    val importance: String,
    val options: List<Pair<String, String>>,
    val normalExample: String
)

private val inspectionRubricsV44 = listOf(
    InspectionRubricV44("Edad aparente", "Si la apariencia general coincide de forma aproximada con la edad cronológica conocida.", "Puede orientar a enfermedad sistémica, alteraciones del crecimiento, envejecimiento prematuro o deterioro general; nunca establece un diagnóstico por sí sola.", listOf("Aparenta edad acorde" to "La apariencia general es aproximadamente concordante con la edad cronológica.", "Aparenta menor edad" to "Describir el hallazgo sin atribuir una causa; correlacionar con desarrollo y antecedentes.", "Aparenta mayor edad" to "Describir sin juzgar; considerar contexto sistémico, nutricional y social.", "No valorable" to "No hay elementos suficientes para una comparación útil."), "Edad aparente concordante con la cronológica."),
    InspectionRubricV44("Marcha", "Forma de caminar: estabilidad, simetría, velocidad, apoyo y necesidad de ayuda.", "Puede orientar a alteraciones neurológicas o musculares, dolor, debilidad o limitación funcional.", listOf("Normal/estable" to "Paso coordinado, simétrico y sin apoyo adicional aparente.", "Lenta/cautelosa" to "Puede asociarse a dolor, miedo, debilidad o edad; requiere contexto.", "Claudicante" to "Apoyo asimétrico o marcha antálgica; investigar dolor/lesión.", "Inestable/atáxica" to "Desequilibrio o base alterada; requiere valoración clínica adicional.", "Usa apoyo" to "Bastón, andadera, silla u otro soporte debe documentarse."), "Marcha estable, simétrica y coordinada."),
    InspectionRubricV44("Facies", "Expresión y rasgos visibles de la cara: simetría, dolor, cansancio, dificultad respiratoria u otras características.", "Puede orientar a síndromes, dolor o enfermedad sistémica, pero no debe asignarse un síndrome sólo por apariencia.", listOf("Sin alteraciones aparentes" to "Expresión facial y simetría sin hallazgos llamativos.", "Facies dolorosa" to "Gestos o expresión compatibles con dolor; corroborar con interrogatorio.", "Facies ansiosa" to "Tensión/expresión de preocupación; correlacionar con conducta y signos vitales.", "Asimetría facial" to "Registrar lado y región; explorar edema, lesión, desarrollo o causas neuromusculares.", "Otra característica" to "Describir objetivamente el rasgo observado."), "Facies sin alteraciones aparentes."),
    InspectionRubricV44("Actitud", "Cooperación y conducta general durante la entrevista y exploración.", "Ayuda a adaptar el manejo conductual, la comunicación y la secuencia de atención.", listOf("Tranquila/receptiva" to "Acepta indicaciones y mantiene interacción funcional.", "Expectante" to "Observa antes de participar; puede requerir explicación gradual.", "Defensiva" to "Evita contacto/procedimientos; investigar dolor, miedo o experiencias previas.", "Inquieta" to "Movimientos frecuentes o dificultad para permanecer en posición.", "Somnolienta" to "Valorar medicamentos, sueño, enfermedad y estado de conciencia."), "Actitud receptiva y colaboradora."),
    InspectionRubricV44("Constitución", "Habitus corporal observado: delgado, normoconstituido, obesidad aparente, atlético u otra constitución.", "Orienta a riesgo sistémico y estado nutricional; el diagnóstico de sobrepeso/obesidad requiere medidas, no sólo inspección.", listOf("Delgada" to "Constitución aparentemente delgada; no equivale a desnutrición sin evaluación.", "Normoconstituida" to "Proporciones generales sin hallazgos llamativos.", "Obesidad aparente" to "Debe confirmarse con medidas apropiadas; puede relacionarse con riesgo metabólico.", "Atlética/musculosa" to "Constitución con desarrollo muscular evidente.", "No valorable" to "La inspección no permite clasificar con utilidad."), "Constitución normoconstituida a la inspección."),
    InspectionRubricV44("Movimientos anormales", "Temblores, tics, espasmos, discinesias u otros movimientos involuntarios.", "Puede orientar a alteraciones neurológicas, ansiedad o efectos de medicamentos y modifica la seguridad del procedimiento.", listOf("Ausentes" to "No se observan movimientos involuntarios evidentes.", "Temblor" to "Registrar región, reposo/acción y si interfiere con función.", "Tics" to "Movimiento/sonido repetitivo; no asumir etiología.", "Espasmos" to "Contracciones involuntarias visibles; contextualizar.", "Otro movimiento involuntario" to "Describir patrón y región."), "Sin movimientos anormales aparentes."),
    InspectionRubricV44("Conciencia", "Estado de alerta y orientación básica al entorno; respuesta coherente a estímulos y preguntas.", "Es esencial para reconocer una urgencia médica y para valorar capacidad de cooperación/comunicación.", listOf("Alerta y orientada" to "Responde de forma coherente y mantiene atención adecuada.", "Somnolienta pero responde" to "Requiere valorar causa y seguridad antes de continuar.", "Confusa/desorientada" to "Hallazgo de alarma que requiere valoración médica contextual.", "Respuesta disminuida" to "Priorizar evaluación de urgencia según situación clínica."), "Consciente, alerta y orientada."),
    InspectionRubricV44("Actitud psicológica", "Manifestaciones observables de ansiedad, miedo, angustia, retraimiento o irritabilidad.", "Importa para el manejo dental, la comunicación y la planificación de técnicas de reducción de ansiedad.", listOf("Tranquila" to "Sin signos evidentes de ansiedad durante la interacción.", "Ansiosa" to "Inquietud, tensión o preocupación; adaptar comunicación.", "Miedo dental" to "Evitación o temor específicamente vinculado con la atención dental.", "Retraída" to "Participación limitada; considerar edad, dolor, desarrollo y contexto.", "Irritable" to "Respuesta de molestia/tensión; no atribuir causa sin preguntar."), "Actitud psicológica tranquila durante la consulta."),
    InspectionRubricV44("Cuidado personal", "Higiene general, arreglo y condiciones observables de autocuidado, descritas sin juicios de valor.", "Puede sugerir barreras de autocuidado o contexto social y orientar preguntas de riesgo epidemiológico/social.", listOf("Adecuado" to "Aseo y arreglo general acordes al contexto.", "Higiene limitada" to "Describir objetivamente; explorar acceso, dependencia o enfermedad.", "Vestimenta no acorde al clima" to "Dato contextual; no implica por sí solo abandono.", "Dependencia para autocuidado" to "Puede requerir apoyo de cuidador o adaptación del plan.", "No valorable" to "No hay elementos suficientes."), "Cuidado personal e higiene general adecuados al contexto."),
    InspectionRubricV44("Cooperación", "Grado en que la persona permite y participa en la exploración y los procedimientos.", "Influye en el plan de tratamiento, duración de sesiones, técnicas de comunicación y necesidad de adaptación.", listOf("Cooperador" to "Acepta la interacción/procedimientos apropiados para el contexto.", "Cooperador con reservas" to "Participa pero necesita pausas, explicación o apoyo.", "Potencialmente cooperador" to "Puede mejorar con adaptación y guía de conducta.", "No cooperador en este momento" to "Situación temporal; investigar dolor, miedo, desarrollo y comunicación.", "No valorable" to "No hubo oportunidad suficiente para valorar."), "Paciente cooperador durante la exploración.")
)

@Composable
fun PhysicalExamHubV44Screen(lang: String, onVitals: () -> Unit, onBack: () -> Unit) {
    var general by remember { mutableStateOf(false) }
    if (general) {
        GeneralInspectionV44Screen(lang) { general = false }
        return
    }
    ResponsiveScreenV17(tr(lang, "Exploración física", "Physical examination"), tr(lang, "Dos rutas: signos vitales/glucosa capilar e inspección general.", "Two routes: vital signs/capillary glucose and general inspection."), onBack) { _ ->
        Card(onClick = onVitals, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(16.dp)) {
                Text(tr(lang, "❤️ Signos vitales y glucosa capilar", "❤️ Vital signs and capillary glucose"), fontWeight = FontWeight.Black)
                Text(tr(lang, "Temperatura · frecuencia cardiaca · frecuencia respiratoria · tensión arterial · glucosa capilar.", "Temperature · heart rate · respiratory rate · blood pressure · capillary glucose."))
            }
        }
        Card(onClick = { general = true }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Column(Modifier.padding(16.dp)) {
                Text(tr(lang, "👁 Inspección general", "👁 General inspection"), fontWeight = FontWeight.Black)
                Text(tr(lang, "Edad aparente · marcha · facies · actitud · constitución · movimientos anormales · conciencia · actitud psicológica · cuidado personal · cooperación.", "Apparent age · gait · facies · attitude · constitution · abnormal movements · consciousness · psychological attitude · self-care · cooperation."))
            }
        }
    }
}

@Composable
fun GeneralInspectionV44Screen(lang: String, onBack: () -> Unit) {
    var selections by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var open by remember { mutableStateOf<Int?>(0) }
    ResponsiveScreenV17(tr(lang, "Inspección general", "General inspection"), tr(lang, "Rubros, qué se observa, importancia clínica y ejemplos de registro.", "Items, what is observed, clinical relevance and charting examples."), onBack) { profile ->
        PracticeSaveControlsV48(lang, "history_general_inspection_v48")
        ResponsiveSectionV17(tr(lang, "Cómo registrar la inspección", "How to chart the inspection")) {
            Text(tr(lang, "Qué va aquí: hallazgos observables de cada rubro. Cómo se escribe: describe lo que ves de forma objetiva, sin convertir una apariencia aislada en diagnóstico.", "What belongs here: observable findings for each item. How to write it: describe what you see objectively without turning an isolated appearance into a diagnosis."))
            Text(tr(lang, "🧾 Ejemplo: “Marcha estable y simétrica; facies sin alteraciones aparentes; consciente, alerta y cooperador durante la exploración”.", "🧾 Example: “Stable symmetric gait; no apparent facial abnormalities; conscious, alert and cooperative during examination”."))
            Text(tr(lang, "⚠️ Error común: etiquetar ansiedad, obesidad, síndrome, deterioro neurológico o condición social sólo por inspección.", "⚠️ Common mistake: labeling anxiety, obesity, a syndrome, neurologic impairment or social condition based only on inspection."), color = MaterialTheme.colorScheme.error)
        }
        ResponsiveSectionV17(tr(lang, "Tabla de referencia", "Reference table")) {
            NoticeCard(tr(lang,
                "La imagen de referencia de inspección general se retiró temporalmente porque el recurso empaquetado no es verificable. La fuente original indicada es “3. Llenado de expediente.pdf”, p. 43. No se sustituye por una imagen distinta ni se recorta/rediseña.",
                "The general-inspection reference image was temporarily removed because the packaged resource is not verifiable. The indicated original source is “3. Llenado de expediente.pdf”, p. 43. It is not replaced with a different image or cropped/redesigned."
            ))
        }
        inspectionRubricsV44.forEachIndexed { index, rubric ->
            Card(onClick = { open = if (open == index) null else index }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (open == index) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(15.dp)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(rubric.title, fontWeight = FontWeight.Black)
                    if (open == index) {
                        Text(tr(lang, "¿Qué se observa?", "What is observed?"), fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Text(rubric.observe)
                        Text(tr(lang, "Importancia", "Clinical relevance"), fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Text(rubric.importance)
                        Text(tr(lang, "Opciones del ejercicio", "Exercise options"), fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        AdaptiveGridV17(rubric.options.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                            val (label, meaning) = rubric.options[i]
                            FilterChip(selected = selections[rubric.title] == label, onClick = { selections = selections + (rubric.title to label) }, label = { Text(label) }, modifier = Modifier.fillMaxWidth())
                            if (selections[rubric.title] == label) Text(meaning, style = MaterialTheme.typography.bodySmall)
                        }
                        Text(tr(lang, "Ejemplo de registro", "Charting example"), fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Text(rubric.normalExample)
                    }
                }
            }
        }
        ResponsiveSectionV17(tr(lang, "Resumen de inspección", "Inspection summary")) {
            if (selections.isEmpty()) Text(tr(lang, "Aún no hay selecciones.", "No selections yet."))
            selections.forEach { (rubric, value) -> Text("• $rubric: $value") }
        }
        NoticeCard(tr(lang, "La inspección orienta la exploración y comunicación. Ningún rubro aislado confirma una enfermedad, síndrome, estado psicológico o condición social.", "Inspection guides examination and communication. No single item confirms a disease, syndrome, psychological state or social condition."))
        PracticeSaveControlsV48(lang, "history_general_inspection_v48")
    }
}
