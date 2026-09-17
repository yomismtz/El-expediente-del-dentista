package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class AsaTeachingV40(val code:String,val definition:String,val examples:String)
private val asaTeachingV40=listOf(
    AsaTeachingV40("ASA I","Paciente sano, sin enfermedad sistémica relevante.","No fumador; consumo nulo o mínimo de alcohol; ejercicio/tolerancia funcional normal."),
    AsaTeachingV40("ASA II","Enfermedad sistémica leve o condición sin limitación funcional sustancial.","Hipertensión o diabetes bien controladas, enfermedad pulmonar leve, embarazo, tabaquismo actual, obesidad IMC 30–40, consumo social de alcohol."),
    AsaTeachingV40("ASA III","Enfermedad sistémica grave con limitación funcional sustancial.","Diabetes/hipertensión mal controladas, EPOC, obesidad mórbida, dependencia/abuso de alcohol, marcapasos, enfermedad renal terminal con diálisis, antecedente remoto de infarto/EVC."),
    AsaTeachingV40("ASA IV","Enfermedad sistémica grave que constituye amenaza constante para la vida.","Infarto/EVC/isquemia reciente, disfunción valvular o ventricular grave, sepsis, coagulación intravascular diseminada, enfermedad renal terminal sin diálisis regular."),
    AsaTeachingV40("ASA V","Paciente moribundo que no se espera sobreviva sin la operación.","Ejemplos hospitalarios de cirugía de rescate; no corresponde a la consulta dental ambulatoria ordinaria."),
    AsaTeachingV40("ASA VI","Paciente con muerte encefálica cuyos órganos se retiran para donación.","Categoría hospitalaria de donación de órganos."),
    AsaTeachingV40("E","Modificador de emergencia que se añade a ASA I–VI cuando retrasar el procedimiento aumenta de manera significativa la amenaza para vida o parte corporal.","Ejemplo: ASA IIIE; la letra E no es una clase independiente.")
)

private data class BloodV40(val group:String,val prevalence:String,val donates:String,val receives:String)
private val bloodV40=listOf(
    BloodV40("O+","≈64.01% en estudio multicéntrico mexicano 2026","O+, A+, B+, AB+","O+, O−"),
    BloodV40("A+","≈23.24%","A+, AB+","A+, A−, O+, O−"),
    BloodV40("B+","≈7.92%","B+, AB+","B+, B−, O+, O−"),
    BloodV40("AB+","≈1.43%","AB+","O±, A±, B±, AB±"),
    BloodV40("O−","≈1.98%","O±, A±, B±, AB± (glóbulos rojos)","O−"),
    BloodV40("A−","≈1.02%","A−, A+, AB−, AB+","A−, O−"),
    BloodV40("B−","≈0.32%","B−, B+, AB−, AB+","B−, O−"),
    BloodV40("AB−","≈0.07%","AB−, AB+","AB−, A−, B−, O−")
)

private data class MedExampleV40(val condition:String,val examples:String)
private val medExamplesV40=listOf(
    MedExampleV40("Hipertensión","Ejemplos que el paciente podría reportar: losartán, enalapril, amlodipino, hidroclorotiazida. Registrar exactamente nombre, concentración/dosis referida, vía e intervalo."),
    MedExampleV40("Diabetes tipo 2","Metformina, inhibidores SGLT2, agonistas GLP-1, sulfonilureas o insulina, según el tratamiento real del paciente."),
    MedExampleV40("Dislipidemia","Atorvastatina, rosuvastatina u otro hipolipemiante referido."),
    MedExampleV40("Hipotiroidismo","Levotiroxina; registrar microgramos y horario sólo como lo refiere el paciente."),
    MedExampleV40("Asma/EPOC","Salbutamol u otros broncodilatadores, esteroides inhalados y combinaciones; preguntar uso de rescate y control."),
    MedExampleV40("Cardiopatía/antitrombóticos","Ácido acetilsalicílico, clopidogrel, warfarina, apixabán, rivaroxabán u otros; NO suspenderlos desde esta app."),
    MedExampleV40("Reflujo/gastritis","Omeprazol u otros inhibidores de bomba; antiácidos según lo referido."),
    MedExampleV40("Dolor crónico/artritis","Paracetamol, AINE, gabapentinoides u otros según antecedente; verificar automedicación."),
    MedExampleV40("Ansiedad/depresión","ISRS, benzodiacepinas u otros psicofármacos; registrar sin juzgar y considerar xerostomía/interacciones."),
    MedExampleV40("Sin enfermedad específica","Vitaminas, hierro, calcio, suplementos, herbolaria, anticonceptivos y productos de venta libre también deben preguntarse.")
)

@Composable
fun IdentificationTeachingV40Screen(lang:String,onBack:()->Unit){
    var asaOpen by remember{mutableStateOf(0)}
    var bloodOpen by remember{mutableStateOf(0)}
    var medOpen by remember{mutableStateOf<Int?>(null)}
    ResponsiveScreenV17("Ficha de identificación · resumen educativo","No captura datos personales. Enseña qué se integra en la ficha-resumen y recupera resultados de otros módulos.",onBack){profile->
        NoticeCard("No escribas nombre, domicilio, teléfono, expediente ni datos institucionales. Practica con casos ficticios y revisa cómo se integra la información clínica.")
        ResponsiveSectionV17("Clasificación ASA · toca para ejemplos"){
            asaTeachingV40.forEachIndexed{i,a->
                Card(onClick={asaOpen=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(asaOpen==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){
                    Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){Text(a.code,fontWeight=FontWeight.Black);if(asaOpen==i){Text(a.definition);Text("Ejemplos: ${a.examples}")}}
                }
            }
            NoticeCard("Corrección importante: el consumo social de alcohol aparece entre los ejemplos de ASA II, no ASA I. La clase final depende del estado completo del paciente, no de una etiqueta aislada.")
        }
        ResponsiveSectionV17("Grupo ABO/Rh · toca cada grupo"){
            AdaptiveGridV17(bloodV40.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                val b=bloodV40[i]
                Card(onClick={bloodOpen=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(bloodOpen==i)MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)){
                    Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){Text(b.group,fontWeight=FontWeight.Black);if(bloodOpen==i){Text("Frecuencia aproximada: ${b.prevalence}");Text("Glóbulos rojos · puede donar a: ${b.donates}");Text("Puede recibir de: ${b.receives}")}}
                }
            }
            NoticeCard("Compatibilidad simplificada de glóbulos rojos para aprendizaje. Una transfusión real requiere pruebas del banco de sangre y compatibilidad individual. Centro Nacional de la Transfusión Sanguínea, CDMX: 55 6392 2250; Trabajo Social 55 6392 2270/2271.")
        }
        ResponsiveSectionV17("Medicamentos actuales · ejemplos de cómo preguntar, no de qué recetar"){
            medExamplesV40.forEachIndexed{i,m->
                Card(onClick={medOpen=if(medOpen==i)null else i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(medOpen==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){
                    Column(Modifier.padding(10.dp)){Text(m.condition,fontWeight=FontWeight.Black);if(medOpen==i)Text(m.examples)}
                }
            }
            NoticeCard("Ejemplo de redacción: “Losartán 50 mg VO cada 24 h, referido por el paciente por hipertensión”. Es un ejemplo de REGISTRO; no indica que esa dosis deba prescribirse o modificarse.")
        }
        ResponsiveSectionV17("Resumen integrado desde los demás módulos"){
            val keys=listOf(
                "caries" to "Caries / CPOD-CEOS",
                "endo" to "Endodóntico",
                "periodontal" to "Periodontal",
                "prosthetic" to "Protésico / Kennedy",
                "tmd" to "Temporomandibular",
                "surgical" to "Patológico / quirúrgico",
                "occlusion" to "Oclusión",
                "mucosa" to "Mucosas"
            )
            keys.forEach{(key,label)->
                Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){
                    Column(Modifier.padding(10.dp)){Text(label,fontWeight=FontWeight.Black);Text(TeachingStateV40.moduleSummaries[key]?:"Aún sin resumen guardado desde su módulo.")}
                }
            }
        }
    }
}

private data class ComplaintV40(val complaint:String,val current:List<String>)
private val complaintsV40=listOf(
    ComplaintV40("“Tengo un diente negro”",listOf("Lesión de caries activa","Lesión de caries inactiva","Pigmentación/mancha extrínseca")),
    ComplaintV40("“Me duele una muela”",listOf("Pulpitis reversible","Pulpitis irreversible sintomática","Dolor apical/periodontal u otra fuente")),
    ComplaintV40("“Me duele con lo frío”",listOf("Hipersensibilidad dentinaria","Pulpitis reversible","Pulpitis irreversible si el dolor persiste")),
    ComplaintV40("“Me duele con lo caliente”",listOf("Inflamación pulpar","Pulpitis irreversible sintomática","Otra fuente de dolor a investigar")),
    ComplaintV40("“Me duele al morder”",listOf("Periodontitis apical sintomática","Fractura dental","Trauma oclusal/periodontal")),
    ComplaintV40("“Se me rompió un diente”",listOf("Fractura de esmalte/dentina","Fractura con exposición pulpar","Restauración fracturada")),
    ComplaintV40("“Se me cayó una curación”",listOf("Pérdida de restauración sin caries evidente","Caries recurrente/secundaria","Fractura de tejido dentario/restauración")),
    ComplaintV40("“Tengo un hoyo”",listOf("Caries cavitada activa","Caries cavitada inactiva","Defecto/restauración perdida")),
    ComplaintV40("“Me sangran las encías”",listOf("Gingivitis inducida por biofilm","Periodontitis a valorar con sondaje","Trauma/irritación local u otra causa")),
    ComplaintV40("“Tengo mal aliento”",listOf("Biofilm/gingivitis/periodontitis","Saburra lingual/xerostomía","Causa extraoral o sistémica a investigar")),
    ComplaintV40("“Se me mueve un diente”",listOf("Pérdida de soporte periodontal","Trauma oclusal","Traumatismo/luxación")),
    ComplaintV40("“Se me hinchó la cara/encía”",listOf("Infección odontógena/absceso","Inflamación no odontógena","Lesión de tejido blando/glándula salival")),
    ComplaintV40("“Me salió una bolita”",listOf("Lesión reactiva","Quiste/lesión salival","Lesión infecciosa o neoplásica a diferenciar")),
    ComplaintV40("“Tengo una llaga”",listOf("Úlcera traumática","Afta","Lesión persistente que requiere diagnóstico diferencial")),
    ComplaintV40("“Tengo una mancha blanca”",listOf("Queratosis/fricción","Candidiasis removible","Leucoplasia u otra lesión blanca persistente")),
    ComplaintV40("“Tengo una mancha roja”",listOf("Inflamación/trauma","Lesión erosiva","Eritroplasia u otra lesión persistente")),
    ComplaintV40("“Me truena la mandíbula”",listOf("Desplazamiento discal con reducción, posible","Hipermovilidad/ruido articular","Otro TTM; integrar dolor y función")),
    ComplaintV40("“No puedo abrir bien la boca”",listOf("Trismus inflamatorio/infeccioso","TTM con limitación/bloqueo","Trauma u otra causa")),
    ComplaintV40("“Aprieto/rechino los dientes”",listOf("Bruxismo referido/probable","Desgaste dental por múltiples causas","Sobrecarga muscular a valorar")),
    ComplaintV40("“Quiero blanquear mis dientes”",listOf("Pigmentación extrínseca","Cambio de color intrínseco","Color dental dentro de variación normal")),
    ComplaintV40("“Quiero arreglar mis dientes chuecos”",listOf("Malposición/apiñamiento","Alteración transversal/sagital/vertical","Necesidad ortodóncica a integrar con crecimiento y oclusión")),
    ComplaintV40("“Mi hijo no ha sacado un diente”",listOf("Cronología dentro de variación normal","Retraso/retención eruptiva","Obstáculo, agenesia o impactación a investigar")),
    ComplaintV40("“Le salió un diente en otro lugar”",listOf("Erupción ectópica","Transposición/malposición","Diente supernumerario u otra anomalía")),
    ComplaintV40("“Tiene un diente más chico/grande”",listOf("Microdoncia/macrodoncia","Anomalía de forma","Diferencia aparente por posición/erupción")),
    ComplaintV40("“Quiero una prótesis”",listOf("Edentulismo parcial","Edentulismo total","Reemplazo/ajuste de prótesis existente")),
    ComplaintV40("“Mi prótesis me lastima”",listOf("Trauma por sobreextensión/presión","Mucositis/lesión asociada","Problema de retención, estabilidad u oclusión")),
    ComplaintV40("“Quiero que me saquen una muela”",listOf("Diente no restaurable","Tercer molar con indicación","Diente potencialmente conservable: reevaluar diagnóstico/alternativas")),
    ComplaintV40("“Me salió pus”",listOf("Absceso odontógeno","Trayecto fistuloso","Infección periodontal/periapical a diferenciar")),
    ComplaintV40("“Tengo sensibilidad”",listOf("Hipersensibilidad dentinaria","Lesión de caries inicial/profunda","Fisura/restauración defectuosa u otra causa")),
    ComplaintV40("“Vengo a revisión/limpieza”",listOf("Consulta preventiva sin síntoma principal","Biofilm/cálculo a evaluar","Hallazgo incidental durante exploración"))
)

@Composable
fun ChiefComplaintV40Screen(lang:String,onBack:()->Unit){
    var selected by remember{mutableStateOf(0)}
    var current by remember{mutableStateOf("")}
    ResponsiveScreenV17("Motivo de consulta y padecimiento actual","Primero se conserva el motivo literal; después se plantean posibilidades clínicas que deben verificarse con exploración.",onBack){profile->
        PracticeSaveControlsV48(lang, "history_chief_complaint_v48")
        ResponsiveSectionV17("Motivos de consulta frecuentes · 30 ejemplos"){
            AdaptiveGridV17(complaintsV40.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                val c=complaintsV40[i]
                FilterChip(selected==i,{selected=i;current=""},{Text(c.complaint)},Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17("Padecimiento actual · opciones para razonar"){
            Text("Motivo literal seleccionado: ${complaintsV40[selected].complaint}",fontWeight=FontWeight.Black)
            complaintsV40[selected].current.forEach{option->FilterChip(current==option,{current=option},{Text(option)},Modifier.fillMaxWidth())}
            Text("✍️ Cómo se escribe", fontWeight=FontWeight.SemiBold, color=MaterialTheme.colorScheme.primary)
            Text("Conserva primero las palabras del paciente entre comillas o claramente identificadas como referidas; después describe inicio, evolución, desencadenantes, duración, intensidad y factores que modifican el problema.")
            if(current.isNotBlank()){
                Text("🧾 Ejemplo: “Se evalúa ${complaintsV40[selected].complaint.removeSurrounding("“","”").lowercase()}; hallazgos compatibles con $current, pendiente de correlación con exploración y pruebas”.")
            }
            Text("⚠️ Error común", fontWeight=FontWeight.SemiBold, color=MaterialTheme.colorScheme.error)
            Text("Convertir el motivo de consulta en un diagnóstico antes de explorar o cambiar las palabras del paciente por una interpretación clínica.")
            NoticeCard("Estas tres opciones son hipótesis educativas, no diagnósticos automáticos. Por ejemplo, un diente oscuro puede representar caries activa, caries inactiva, tinción, restauración o cambio pulpar según los hallazgos.")
        }
        PracticeSaveControlsV48(lang, "history_chief_complaint_v48")
    }
}

private data class ChoiceExplainV40(val title:String,val options:List<Pair<String,String>>)
private val nonPathGroupsV40=listOf(
    ChoiceExplainV40("Habitaciones de la vivienda",listOf("1–2" to "Puede sugerir mayor densidad habitacional según número de habitantes; no define por sí sola hacinamiento.","3–4" to "Contextualizar siempre con número de habitantes y uso de los espacios.","5 o más" to "Mayor número de cuartos no equivale automáticamente a mejores condiciones; revisar servicios, ventilación y ocupantes.")),
    ChoiceExplainV40("Paredes",listOf("Concreto/tabique" to "Material sólido común.","Madera" to "Valorar conservación, humedad y ventilación.","Lámina" to "Puede asociarse a variaciones térmicas; contextualizar.","Adobe" to "Material tradicional; valorar integridad/humedad.","Otro" to "Describir sólo en el expediente real si es relevante.")),
    ChoiceExplainV40("Piso",listOf("Loseta/cerámica" to "Superficie lavable.","Cemento" to "Superficie sólida; valorar condiciones de higiene/humedad.","Madera" to "Valorar conservación.","Tierra" to "Indicador contextual de vivienda que puede asociarse con vulnerabilidad; no es diagnóstico clínico.")),
    ChoiceExplainV40("Techo",listOf("Concreto" to "Material sólido común.","Teja" to "Valorar mantenimiento y filtraciones.","Lámina metálica" to "Puede modificar temperatura interior.","Fibrocemento/cemento-asbesto antiguo" to "El asbesto es carcinógeno para humanos. El riesgo depende de tipo, estado y liberación de fibras; no manipular material sospechoso.")),
    ChoiceExplainV40("Ventilación",listOf("Ventanas funcionales" to "Facilitan intercambio de aire.","Ventilación cruzada" to "Entrada y salida de aire por aperturas distintas.","Extractor/ventilación mecánica" to "Apoya recambio en espacios cerrados.","Sin ventilación suficiente" to "Puede favorecer humedad/contaminantes interiores; es un dato contextual.")),
    ChoiceExplainV40("Higiene general",listOf("Baño diario o casi diario" to "Registrar frecuencia sin juzgar; adaptar a contexto/cultura.","2–4 veces por semana" to "Explorar acceso a agua y contexto si es clínicamente relevante.","Menos frecuente" to "No inferir negligencia; puede relacionarse con acceso, dependencia o condiciones sociales.","Cambio/lavado regular de ropa" to "Complementa prácticas de higiene general.")),
    ChoiceExplainV40("Higiene bucal",listOf("Cepillado 2+ veces/día con pasta fluorada" to "Factor protector cuando técnica y concentración son adecuadas.","Cepillado 1 vez/día" to "Puede ser insuficiente según riesgo; revisar técnica y fluoruro.","Cepillado irregular" to "Aumenta tiempo de biofilm; integrar con riesgo de caries/periodontal.","Limpieza interdental" to "Hilo/cepillos interproximales según espacios y habilidad.","No usa pasta fluorada" to "Explorar motivo y exposición total a fluoruro.")),
    ChoiceExplainV40("Tipo de alimentación",listOf("Variada" to "Incluye grupos diversos; importa frecuencia y calidad, no sólo etiqueta.","Alta en ultraprocesados" to "Suele aumentar azúcares/sodio/grasas y frecuencia de exposición.","Vegetariana" to "Puede ser adecuada si está planificada; preguntar fuentes de proteína/micronutrientes.","Vegana" to "Requiere planificación de B12 y otros nutrientes; no implica por sí sola riesgo oral.","Blanda/papillas prolongadas" to "En pediatría puede relacionarse con desarrollo de habilidades orales según edad/contexto."))
)

@Composable
fun NonPathologicalV40Screen(lang:String,onBack:()->Unit){
    val selected=remember{mutableStateListOf<String>()}
    val services=listOf("Agua potable","Drenaje","Electricidad","Gas","Recolección de basura","Internet/telefonía","Sanitario","Refrigeración de alimentos")
    val dietFreq=listOf("Nunca/casi nunca","1–2 veces/semana","3–4 veces/semana","1 vez/día","2–3 veces/día","≥4 veces/día")
    val foodGroups=listOf("Dulces/caramelos","Refrescos/bebidas azucaradas","Pan dulce/galletas","Ultraprocesados/snacks","Fruta fresca","Verduras","Huevo","Lácteos","Leguminosas","Cereales integrales","Carne blanca/pescado","Carne roja")
    var activeFood by remember{mutableStateOf(foodGroups.first())};var foodFreq by remember{mutableStateOf("")}
    ResponsiveScreenV17("Antecedentes personales no patológicos · selector educativo","Todo se practica con opciones; no solicita datos reales ni texto libre.",onBack){profile->
        PracticeSaveControlsV48(lang, "history_nonpath_v48")
        nonPathGroupsV40.forEach{group->
            ResponsiveSectionV17(group.title){group.options.forEach{(label,meaning)->ExpandableChoiceV40(label,meaning,selected)}}
        }
        ResponsiveSectionV17("Servicios de la vivienda · checklist"){
            AdaptiveGridV17(services.size,if(profile.largeSystemText)1 else 2){i->val x=services[i];FilterChip(x in selected,{toggleV40(selected,x)},{Text(x)},Modifier.fillMaxWidth())}
        }
        ResponsiveSectionV17("Frecuencia de alimentos · ejemplo por grupo"){
            foodGroups.forEach{x->FilterChip(activeFood==x,{activeFood=x;foodFreq=""},{Text(x)},Modifier.fillMaxWidth())}
            Text("Ejemplos para $activeFood: ${when(activeFood){"Dulces/caramelos"->"caramelos, gomitas, chocolates";"Refrescos/bebidas azucaradas"->"refresco, té embotellado, bebidas energéticas";"Ultraprocesados/snacks"->"papas, frituras, galletas empaquetadas";"Leguminosas"->"frijol, lenteja, garbanzo";"Carne blanca/pescado"->"pollo, pavo, pescado";else->"alimentos del grupo seleccionado"}}")
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(4.dp)){dietFreq.chunked(2).first().forEach{}}
            dietFreq.forEach{f->FilterChip(foodFreq==f,{foodFreq=f},{Text(f)},Modifier.fillMaxWidth())}
            if(foodFreq.isNotBlank())Text("Lectura: $activeFood · $foodFreq. Para caries importa especialmente la FRECUENCIA de exposición a azúcares fermentables entre comidas, no sólo la cantidad total.")
        }
        HabitsExposureV40(selected,profile)
        PracticeSaveControlsV48(lang, "history_nonpath_v48")
    }
}

@Composable private fun HabitsExposureV40(selected:MutableList<String>,profile:ScreenProfileV17){
    val habits=listOf(
        "Tabaco combustible" to "Importa cantidad (p. ej. cigarrillos/día), años de uso y si suspendió; relacionado con cáncer, enfermedad periodontal y cicatrización.",
        "Vapeo" to "Preguntar frecuencia, nicotina y líquidos; no asumir que es inocuo.",
        "Alcohol" to "Preguntar frecuencia/cantidad y patrón; consumo social no equivale a dependencia.",
        "Cannabis u otras drogas" to "Preguntar sustancia, vía, frecuencia, última exposición y repercusiones sin juzgar.",
        "Onicofagia" to "Puede asociarse a desgaste/trauma y es relevante en hábitos/parafunción.",
        "Perforaciones" to "Sitios comunes: lóbulo/hélix de oreja, nariz, labio, lengua, ceja; las orales pueden traumatizar dientes/encía.",
        "Tatuajes" to "Registrar presencia sólo si el formato lo solicita; preguntar condiciones de realización si existe riesgo de infección relevante."
    )
    ResponsiveSectionV17("Hábitos y exposiciones · por qué importan"){
        habits.forEach{(h,d)->ExpandableChoiceV40(h,"$d · En el expediente real se documentan frecuencia, cantidad, duración y fecha de suspensión cuando aplique.",selected)}
    }
}

private data class VaccineV40(val name:String,val protects:String,val schedule:String)
private val vaccinesV40=listOf(
    VaccineV40("BCG","formas graves de tuberculosis","México: al nacer; revisar Cartilla vigente para rescates/edad máxima."),
    VaccineV40("Hepatitis B","hepatitis B","idealmente al nacer/primeros 7 días; esquemas de rescate dependen de edad y antecedentes."),
    VaccineV40("Hexavalente acelular","difteria, tosferina, tétanos, poliomielitis, Hib y hepatitis B","2, 4 y 6 meses; refuerzo a los 18 meses en esquema infantil vigente."),
    VaccineV40("Rotavirus","gastroenteritis grave por rotavirus","dosis en primeros meses de vida; existen límites estrictos de edad según biológico."),
    VaccineV40("Neumocócica conjugada","enfermedad por neumococo","2 y 4 meses, refuerzo alrededor de 12 meses en esquema infantil."),
    VaccineV40("Influenza","influenza estacional","desde los 6 meses en grupos/edades indicados; esquema inicial y refuerzo/anual según lineamientos."),
    VaccineV40("SRP","sarampión, rubéola y parotiditis","12 y 18 meses en esquema infantil mexicano actual; revisar campañas/rescates."),
    VaccineV40("DPT","difteria, tosferina y tétanos","refuerzo infantil alrededor de 4 años según esquema."),
    VaccineV40("Td/Tdpa","tétanos/difteria y, con Tdpa, tosferina","refuerzos y embarazo según edad/antecedente; consultar cartilla vigente."),
    VaccineV40("VPH","infecciones por virus del papiloma humano y cánceres asociados","población objetivo escolar/adolescente según campaña nacional vigente; esquema puede actualizarse."),
    VaccineV40("COVID-19","COVID-19","aplicación y refuerzos dependen de lineamientos vigentes, edad y factores de riesgo."),
    VaccineV40("Neumocócica polisacárida","enfermedad neumocócica en adultos/grupos de riesgo","indicación depende de edad y condiciones de riesgo; no confundir con conjugada infantil.")
)

private data class DiseaseMedicationV40(val disease:String,val meds:List<String>)
private val diseaseMedsV40=listOf(
    DiseaseMedicationV40("Hipertensión",listOf("Losartán","Enalapril","Amlodipino","Hidroclorotiazida","Metoprolol/otro beta-bloqueador","Otro referido")),
    DiseaseMedicationV40("Diabetes",listOf("Metformina","Insulina","Empagliflozina/dapagliflozina","Semaglutida/otro GLP-1","Sulfonilurea","Otro referido")),
    DiseaseMedicationV40("Asma/EPOC",listOf("Salbutamol","Esteroide inhalado","LABA/LAMA","Combinación inhalada","Otro referido")),
    DiseaseMedicationV40("Tiroides",listOf("Levotiroxina","Metimazol/tiamazol","Otro referido")),
    DiseaseMedicationV40("Epilepsia",listOf("Levetiracetam","Valproato","Carbamazepina","Lamotrigina","Otro referido")),
    DiseaseMedicationV40("Ansiedad/depresión",listOf("Sertralina/otro ISRS","Duloxetina/otro IRSN","Benzodiacepina referida","Otro referido")),
    DiseaseMedicationV40("Cardiovascular/antitrombótico",listOf("Aspirina","Clopidogrel","Warfarina","Apixabán","Rivaroxabán","Otro referido")),
    DiseaseMedicationV40("Otros frecuentes",listOf("Vitaminas/multivitamínico","Hierro","Calcio/vitamina D","Anticonceptivo","Suplemento/herbolaria","Analgésico de venta libre"))
)

@Composable
fun PathologicalV40Screen(lang:String,onBack:()->Unit){
    val vaccines=remember{mutableStateListOf<String>()};var openVaccine by remember{mutableStateOf<Int?>(null)}
    var disease by remember{mutableStateOf("Hipertensión")};var onsetN by remember{mutableStateOf(1)};var onsetUnit by remember{mutableStateOf("años")};var status by remember{mutableStateOf("En tratamiento / control")};var med by remember{mutableStateOf("")}
    ResponsiveScreenV17("Antecedentes personales patológicos","Vacunación, enfermedades por sistemas, tiempo de evolución, estado actual y medicamentos referidos.",onBack){profile->
        PracticeSaveControlsV48(lang, "history_pathological_v48")
        ResponsiveSectionV17("Vacunación · checklist con explicación"){
            AdaptiveGridV17(vaccinesV40.size,if(profile.largeSystemText)1 else 2){i->val v=vaccinesV40[i];Card(onClick={toggleV40(vaccines,v.name);openVaccine=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(v.name in vaccines)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Text((if(v.name in vaccines)"✓ " else "")+v.name,fontWeight=FontWeight.Black);if(openVaccine==i){Text("Protege contra: ${v.protects}");Text("Cuándo: ${v.schedule}")}}}}
            NoticeCard("Los esquemas cambian. Para una persona real siempre se verifica la Cartilla Nacional de Salud y lineamientos vigentes, no la memoria del paciente ni esta app.")
        }
        ResponsiveSectionV17("Enfermedad del ejercicio"){
            Text("🔎 Qué va aquí", fontWeight=FontWeight.SemiBold, color=MaterialTheme.colorScheme.primary)
            Text("Enfermedad referida, tiempo de evolución, estado actual y medicamentos. Si es positiva, agrega control, complicaciones y atención médica relacionada.")
            diseaseMedsV40.forEach{d->FilterChip(disease==d.disease,{disease=d.disease;med=""},{Text(d.disease)},Modifier.fillMaxWidth())}
            Text("Desde cuándo")
            Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){listOf(1,2,3,5,10,15,20).forEach{n->FilterChip(onsetN==n,{onsetN=n},{Text(n.toString())})}}
            Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){listOf("días","semanas","meses","años").forEach{u->FilterChip(onsetUnit==u,{onsetUnit=u},{Text(u)})}}
            Text("Estado actual")
            listOf("Recién diagnosticado / comenzando","Resuelto/curado","Estable / mantenimiento","En tratamiento / control","Mal controlado / actualmente enfermo","No sabe").forEach{s->FilterChip(status==s,{status=s},{Text(s)},Modifier.fillMaxWidth())}
            Text("Medicamento referido")
            diseaseMedsV40.firstOrNull{it.disease==disease}?.meds?.forEach{m->FilterChip(med==m,{med=m},{Text(m)},Modifier.fillMaxWidth())}
            Text("✍️ Cómo se escribe", fontWeight=FontWeight.SemiBold, color=MaterialTheme.colorScheme.primary)
            Text("Distingue siempre entre “refiere” y un diagnóstico confirmado en documentos disponibles. Registra nombre del medicamento, dosis/concentración, vía e intervalo sólo si se conocen.")
            Text("🧾 Resumen del ejercicio: $disease desde hace $onsetN $onsetUnit · $status"+(if(med.isNotBlank())" · medicamento referido: $med (registrar dosis/vía/intervalo exactamente como lo diga la persona)" else ""))
            Text("⚠️ Error común", fontWeight=FontWeight.SemiBold, color=MaterialTheme.colorScheme.error)
            Text("Anotar únicamente el nombre de la enfermedad sin tiempo de evolución, control actual, tratamiento o complicaciones; también es un error suspender o modificar medicamentos desde esta guía.")
        }
        PracticeSaveControlsV48(lang, "history_pathological_v48")
    }
}

@Composable
fun SurgicalTraumaHistoryV40Screen(lang:String,onBack:()->Unit){
    var event by remember{mutableStateOf("Fractura")};var site by remember{mutableStateOf("Mandíbula")};var age by remember{mutableStateOf("Infancia 0–9")};var treatment by remember{mutableStateOf("Inmovilización/ferulización")}
    val events=listOf("Fractura","Fisura","Luxación","Subluxación","Trauma dental","Cirugía previa","Hospitalización","Transfusión recibida","Donación de sangre","Reacción a anestesia")
    val sites=listOf("Cráneo/cara","Mandíbula","Maxilar","Nariz","Clavícula/hombro","Brazo/antebrazo","Mano","Costillas","Columna","Pelvis","Fémur/pierna","Tobillo/pie","Diente/periodonto")
    val treatments=listOf("Observación/control","Inmovilización/ferulización","Reducción cerrada","Reducción abierta y fijación","Sutura","Tratamiento dental","Cirugía","Hospitalización","Rehabilitación/fisioterapia","No recuerda")
    ResponsiveScreenV17("Antecedentes quirúrgicos y traumáticos","Toca un evento para saber qué significa y arma un resumen ficticio.",onBack){profile->
        PracticeSaveControlsV48(lang, "history_surgical_trauma_v48")
        ResponsiveSectionV17("Eventos"){
            events.forEach{e->ExpandableSelectableV40(e,when(e){"Fractura"->"Pérdida de continuidad completa del hueso.";"Fisura"->"Trazo incompleto sin separación completa.";"Luxación"->"Pérdida completa de congruencia articular; en diente, desplazamiento traumático según tipo.";"Subluxación"->"Lesión con movilidad/aumento de movilidad sin pérdida completa de posición.";"Transfusión recibida"->"Recepción de componentes sanguíneos; preguntar cuándo, por qué y si hubo reacción.";"Donación de sangre"->"Antecedente de haber donado; no equivale a transfusión.";"Hospitalización"->"Ingreso a hospital por una condición o procedimiento.";else->"Evento relevante que debe contextualizarse con fecha, causa, tratamiento y secuelas."},event==e){event=e}}
        }
        ResponsiveSectionV17("Zona anatómica"){
            AdaptiveGridV17(sites.size,if(profile.largeSystemText)1 else 2){i->val s=sites[i];FilterChip(site==s,{site=s},{Text(s)},Modifier.fillMaxWidth())}
        }
        ResponsiveSectionV17("Edad aproximada cuando ocurrió"){
            listOf("Primera infancia 0–5","Infancia 6–9","Adolescencia 10–19","20–39 años","40–59 años","≥60 años","No recuerda").forEach{a->FilterChip(age==a,{age=a},{Text(a)},Modifier.fillMaxWidth())}
        }
        ResponsiveSectionV17("Tratamiento recibido"){
            treatments.forEach{t->FilterChip(treatment==t,{treatment=t},{Text(t)},Modifier.fillMaxWidth())}
        }
        NoticeCard("Resumen: antecedente de $event en $site durante $age; tratamiento referido: $treatment. En un expediente real se agregan fecha aproximada, causa, hospital, secuelas y documentación disponible.")
        Text("⚠️ Error común: escribir sólo “cirugía” o “fractura” sin sitio, fecha aproximada, causa, tratamiento, secuelas o reacción asociada.", color=MaterialTheme.colorScheme.error)
        PracticeSaveControlsV48(lang, "history_surgical_trauma_v48")
    }
}

@Composable private fun ExpandableChoiceV40(label:String,meaning:String,selected:MutableList<String>){
    var open by remember{mutableStateOf(false)}
    Card(onClick={toggleV40(selected,label);open=true},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(label in selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.25f)),shape=RoundedCornerShape(12.dp)){
        Column(Modifier.padding(10.dp)){Text((if(label in selected)"✓ " else "")+label,fontWeight=FontWeight.Black);if(open)Text(meaning)}
    }
}

@Composable private fun ExpandableSelectableV40(label:String,meaning:String,selected:Boolean,onClick:()->Unit){
    Card(onClick=onClick,modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){
        Column(Modifier.padding(10.dp)){Text(label,fontWeight=FontWeight.Black);if(selected)Text(meaning)}
    }
}
