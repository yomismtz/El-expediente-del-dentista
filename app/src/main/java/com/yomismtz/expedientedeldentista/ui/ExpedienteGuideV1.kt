package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class GuideSectionV1(val icon:String,val title:String,val purpose:String,val fields:String,val why:String,val tips:String)

private val guideSectionsV1=listOf(
GuideSectionV1("🪪","Identificación del paciente","Identifica correctamente a la persona y vincula su atención.","Iniciales o identificador institucional, edad, sexo, datos de contacto según el sistema autorizado y motivo de consulta.","Evita confusiones de identidad y permite interpretar hallazgos según edad y sexo.","Usar sólo datos necesarios y autorizados; en prácticas educativas usar datos ficticios."),
GuideSectionV1("🗣️","Anamnesis y motivo de consulta","Recoge qué trae al paciente, desde cuándo y cómo evolucionó.","Motivo principal, inicio, duración, localización, intensidad, desencadenantes, síntomas asociados, tratamientos previos y expectativas.","Orienta el examen clínico y ayuda a detectar signos de alarma.","Registrar lo referido por el paciente sin convertirlo automáticamente en diagnóstico."),
GuideSectionV1("📋","Antecedentes personales y sistémicos","Documenta enfermedades que pueden modificar el riesgo odontológico.","Enfermedades actuales y previas, cirugías, hospitalizaciones, alergias, medicamentos, embarazo cuando corresponda y antecedentes relevantes.","Pueden modificar anestesia, sangrado, infección, cicatrización o tratamiento.","Precisar enfermedad, estado, complicaciones y tratamiento; evitar casillas genéricas."),
GuideSectionV1("🍷","Hábitos y antecedentes de riesgo","Registra exposiciones que afectan salud oral y atención.","Tabaco, alcohol, hábitos orales, higiene, dieta y otros factores relevantes.","Ayuda a explicar riesgo de caries, periodontitis, lesiones y problemas funcionales.","Registrar frecuencia y contexto cuando sea relevante, sin juicios de valor."),
GuideSectionV1("🩺","Signos vitales y triage","Determina estabilidad clínica antes de procedimientos.","Edad, sexo, talla, FC, FR, TA sistólica/diastólica, temperatura, SpO₂, glucosa capilar y contexto metabólico/oxígeno.","Los valores cambian con edad, sexo, talla y contexto. Sirven para detectar cuándo repetir, derivar o diferir atención.","La herramienta debe explicar valor alto/bajo, significado clínico, acción y semáforo verde/amarillo/rojo."),
GuideSectionV1("🦷","Examen extraoral e intraoral","Describe objetivamente lo observado.","Cara, ganglios cuando corresponda, ATM, labios, mucosa, lengua, paladar, piso de boca, encía, dientes y hallazgos.","Permite documentar el estado inicial y comparar controles.","Separar observación de interpretación."),
GuideSectionV1("🦷","Odontograma","Registra el estado de cada diente y superficie.","Diente, superficies, ausencia/presencia, restauraciones, lesiones, hallazgos y vínculos diagnósticos/tratamientos.","Facilita planificación y seguimiento.","Verificar que cada hallazgo corresponda a la pieza y superficie correctas."),
GuideSectionV1("🔎","ICDAS y caries","Registra ICDAS II con codificación de dos dígitos por superficie: primer dígito para restauración/sellante y segundo para caries 0–6.","Registrar 00–86 según restauración/sellante + caries; usar 90–93 para implante/póntico, 96 para superficie excluida y 97–99 para diente ausente/no erupcionado.","Estandariza la descripción de restauraciones y lesiones y permite comparar controles por superficie.","No usar 94–95: no están definidos en la codificación estándar consultada. Los códigos 97, 98 y 99 corresponden a ausencia por caries, otras razones y no erupcionado. El código ICDAS no sustituye el diagnóstico clínico integral."),
GuideSectionV1("📊","CPOD/ceod","Resume experiencia de caries en dentición permanente o temporal.","Dientes cariados, perdidos/extraídos por caries y obturados según el índice.","Permite medir experiencia de caries de forma estandarizada.","Elegir el índice según dentición y protocolo."),
GuideSectionV1("🪥","Índice de O'Leary","Evalúa control de placa mediante superficies teñidas.","Superficies positivas, total evaluadas y porcentaje.","Objetiva higiene y seguimiento de educación preventiva.","Registrar superficies consistentemente en cada visita."),
GuideSectionV1("🧫","IPC e IHOS","Resume condiciones periodontales y de higiene.","Sextantes/dientes y códigos periodontales; depósitos de placa/cálculo según IHOS.","Ayuda a detectar necesidad de evaluación periodontal y seguimiento.","Son herramientas de cribado/seguimiento; no sustituyen un periodontograma cuando está indicado."),
GuideSectionV1("📏","Periodontograma","Registra mediciones periodontales detalladas.","Profundidad de sondaje, recesión, nivel de inserción, sangrado, movilidad, furcación y observaciones.","Permite evaluar distribución y evolución periodontal.","Mantener técnica y puntos de medición consistentes."),
GuideSectionV1("🦴","Oclusión, ATM y postura","Registra relaciones funcionales y hallazgos relevantes.","Oclusión, contactos, movimientos mandibulares, ruidos/dolor de ATM y observaciones posturales.","Integra factores funcionales relevantes para síntomas y planificación.","Documentar hallazgos sin atribuir causalidad sin evaluación suficiente."),
GuideSectionV1("👄","Mucosa y tejidos blandos","Documenta lesiones o alteraciones.","Ubicación, tamaño, color, forma, superficie, consistencia, duración, síntomas y fotografías cuando proceda.","La descripción objetiva facilita seguimiento y referencia.","Registrar evolución y signos de alarma; no equivale a diagnóstico histopatológico."),
GuideSectionV1("🩻","Auxiliares y fotografías clínicas","Organiza evidencia complementaria.","Radiografías, fotografías, estudios y observaciones de calidad/fecha.","Permite correlacionar hallazgos clínicos con estudios.","Registrar qué estudio se realizó y qué pregunta clínica responde."),
GuideSectionV1("🧪","Diagnóstico pulpar y periapical","Integra pruebas y hallazgos para valoración endodóntica.","Síntomas, pruebas pulpares, percusión, palpación, imagen y diagnóstico según nomenclatura utilizada.","Evita basar la valoración en una sola prueba.","Registrar pruebas y resultados antes de la conclusión."),
GuideSectionV1("🔬","Endodoncia","Documenta las etapas del tratamiento endodóntico.","Acceso, longitud de trabajo, conductos, instrumentación, irrigación, conometría, obturación, radiografía final y restauración coronal.","Permite trazabilidad y control de cada etapa.","Registrar materiales, mediciones y controles reproduciblemente."),
GuideSectionV1("🦷","Operatoria y restauradora","Documenta lesiones y tratamientos restauradores.","Diente/superficie, diagnóstico, procedimiento, material, aislamiento, pasos y resultado.","Facilita continuidad y comparación entre citas.","Vincular tratamiento con pieza y superficie."),
GuideSectionV1("🦷","Prótesis","Organiza información para rehabilitación protésica.","Tipo de prótesis, pilares, espacio, tejidos, diseño, impresiones/registros, pruebas y controles.","Permite planificar y documentar una rehabilitación por etapas.","Registrar indicaciones, limitaciones y controles."),
GuideSectionV1("🔪","Cirugía y urgencias","Documenta procedimientos quirúrgicos o atención urgente.","Indicación, evaluación previa, consentimiento, procedimiento, anestesia, hallazgos, complicaciones, indicaciones y seguimiento.","Asegura trazabilidad y continuidad.","Verificar triage, consentimiento y antecedentes antes del procedimiento."),
GuideSectionV1("💊","Medicamentos y anestesia","Registra tratamientos farmacológicos y anestésicos estructuradamente.","Principio activo, presentación, dosis, unidad, vía, frecuencia, duración cuando corresponda, alergias; anestésico, concentración, vasoconstrictor, peso y cálculo educativo cuando aplique.","Reduce errores y permite revisar duplicidades y límites de seguridad.","No sustituye prescripción ni protocolos; verificar producto y ficha técnica."),
GuideSectionV1("📝","Consentimientos informados","Documenta que se explicó el procedimiento y alternativas.","Procedimiento, beneficios, riesgos, alternativas, preguntas, aceptación/rechazo y fecha.","Favorece decisión informada y documentada.","El consentimiento no reemplaza una explicación real."),
GuideSectionV1("📆","Evolución y seguimiento","Registra qué ocurrió después de cada visita.","Cambios, procedimientos, respuesta, complicaciones, indicaciones y plan siguiente.","Permite continuidad y demuestra evolución clínica.","Escribir hechos y decisiones concretas con fecha."),
GuideSectionV1("🧾","Resumen clínico y plan","Integra información relevante para decisiones.","Problemas, hallazgos, valoración, prioridades, tratamientos y seguimiento.","Evita información dispersa y facilita continuidad.","Diferenciar hallazgo, valoración, plan y resultado."),
GuideSectionV1("🔐","Cierre, privacidad y calidad","Verifica que el expediente quede completo y protegido.","Campos obligatorios, coherencia entre piezas/diagnósticos, consentimientos, adjuntos y respaldo según política.","La calidad del expediente también es seguridad clínica y protección de datos.","Revisar identidad, fecha, signos vitales, antecedentes, hallazgos, diagnóstico, plan, consentimiento y evolución.")
)

@Composable
fun ExpedienteGuideV1(lang:String,onBack:()->Unit){
    var selected by rememberSaveable { mutableStateOf<Int?>(null) }
    BackHandler { if(selected!=null) selected=null else onBack() }
    Column(Modifier.fillMaxSize().safeDrawingPadding().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
            OutlinedButton(onClick={if(selected!=null)selected=null else onBack()}){Text("‹")}
            Column(Modifier.weight(1f)){
                Text(tr(lang,"📖 Guía para llenar el expediente","📖 Guide to completing the clinical record"),style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black)
                Text(tr(lang,"Modo educativo: no crea, guarda ni modifica ningún expediente.","Educational mode: it does not create, save or modify any clinical record."))
            }
        }
        Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer),modifier=Modifier.fillMaxWidth()){
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                Text(tr(lang,"¿Cómo funciona?","How does it work?"),fontWeight=FontWeight.Black)
                Text(tr(lang,"Recorre todos los apartados y aprende qué se registra, para qué sirve, por qué es importante y qué errores evitar. No se crea ningún paciente ni se persisten datos.","Review every section and learn what is recorded, why it matters and common errors to avoid. No patient is created and no data is persisted."))
            }
        }
        if(selected==null){
            guideSectionsV1.forEachIndexed { index,s->
                Card(onClick={selected=index},modifier=Modifier.fillMaxWidth()){
                    Row(Modifier.fillMaxWidth().padding(14.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)){
                        Text(s.icon,style=MaterialTheme.typography.headlineSmall)
                        Column(Modifier.weight(1f)){
                            Text("${index+1}. ${tr(lang,s.title,s.title)}",fontWeight=FontWeight.Black)
                            Text(tr(lang,s.purpose,s.purpose),style=MaterialTheme.typography.bodySmall)
                        }
                        Text("›",style=MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }else{
            val selectedIndex = selected ?: 0
            val s=guideSectionsV1[selectedIndex]
            Text("${selectedIndex+1}. ${tr(lang,s.title,s.title)}",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black)
            GuideBlockV1(tr(lang,"¿Qué se llena?","What is recorded?"),tr(lang,s.fields,s.fields))
            GuideBlockV1(tr(lang,"¿Por qué se llena?","Why is it recorded?"),tr(lang,s.why,s.why))
            GuideBlockV1(tr(lang,"¿Qué debo considerar?","What should I consider?"),tr(lang,s.tips,s.tips))
            Text(tr(lang,"Ruta del expediente","Record workflow"),fontWeight=FontWeight.Black)
            Text(tr(lang,"Identificación → anamnesis → antecedentes → signos y síntomas/triage → examen → auxiliares → índices → valoración/diagnóstico → plan y tratamiento → consentimiento → evolución → cierre y seguimiento.","Identification → history → medical history → signs/symptoms/triage → examination → diagnostics → indices → assessment/diagnosis → plan/treatment → consent → progress → closing/follow-up."))
            Button(onClick={if(selectedIndex+1<guideSectionsV1.size)selected=selectedIndex+1 else selected=null},modifier=Modifier.fillMaxWidth()){Text(if(selectedIndex+1<guideSectionsV1.size)tr(lang,"Siguiente apartado","Next section") else tr(lang,"Volver al índice","Back to index"))}
        }
    }
}
@Composable private fun GuideBlockV1(title:String,text:String){
    Card(Modifier.fillMaxWidth()){
        Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
            Text(title,fontWeight=FontWeight.Black)
            Text(text)
        }
    }
}
