package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen

private data class HItem(val screen:AppScreen,val title:String,val subtitle:String)
private val hItems=listOf(
 HItem(AppScreen.HISTORY_IDENTIFICATION,"Identificación del paciente","Datos de identificación y cómo se integran al expediente."),
 HItem(AppScreen.HISTORY_REASON,"Motivo de consulta y padecimiento actual","Motivo literal, inicio, evolución y síntomas."),
 HItem(AppScreen.HISTORY_HEREDITARY,"Antecedentes heredo-familiares","Familiares, enfermedades y qué ampliar ante un positivo."),
 HItem(AppScreen.HISTORY_NONPATH,"Antecedentes personales no patológicos","Habitación, higiene, alimentación, inmunizaciones y exposiciones."),
 HItem(AppScreen.HISTORY_GYNECO,"Antecedentes gineco-obstétricos","Hombre: no aplica. Mujer: despliega interrogatorio correspondiente."),
 HItem(AppScreen.HISTORY_PATH,"Antecedentes personales patológicos","Enfermedades, medicamentos, alergias, vacunas y ASA."),
 HItem(AppScreen.HISTORY_SURGICAL_TRAUMA,"Antecedentes quirúrgicos y traumáticos","Cirugías, hospitalizaciones, transfusiones y traumatismos."),
 HItem(AppScreen.HISTORY_PHYSICAL,"Exploración física","Signos vitales, cráneo, facies, músculos, cuello, ganglios y ATM."),
 HItem(AppScreen.OCCLUSION,"Examen de oclusión","Erupción, planos terminales, Angle, caninos, líneas medias, apiñamiento y mordidas."),
 HItem(AppScreen.HISTORY_ORTHO,"Antecedentes de tratamientos ortodónticos","Aparatología, duración, finalidad, retención y resultado."),
 HItem(AppScreen.HISTORY_DENTAL_ALTERATIONS,"Alteraciones de órganos dentarios","Número, forma, tamaño, estructura y erupción."),
 HItem(AppScreen.HISTORY_HABITS,"Hábitos y parafunciones","Qué son y cómo se observan extraoral e intraoralmente."),
 HItem(AppScreen.HISTORY_ORAL_EXAM,"Examen peribucal e intrabucal / mucosas","Piel peribucal, labios, frenillos, comisuras, carrillos, Stensen, paladares, orofaringe, úvula, pilares, amígdalas, lengua, piso de boca y conductos salivales.")
)

@Composable fun ClinicalHistoryHubV38(lang:String,onNavigate:(AppScreen)->Unit,onBack:()->Unit){
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader(tr(lang,"Historia clínica","Clinical history"),onBack,tr(lang,"Toca cada apartado para abrir su guía interactiva.","Tap each section to open its interactive guide."))}
  items(hItems.size){i->val x=hItems[i];Card(onClick={onNavigate(x.screen)},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp)){Text(x.title,fontWeight=FontWeight.Bold);Text(x.subtitle)}}}
 }
}
private data class E(val n:String,val d:String)
@Composable private fun explain(title:String,items:List<E>,onBack:()->Unit){
 var open by remember{mutableStateOf<Int?>(null)}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader(title,onBack,"Toca cada opción para saber qué preguntar, observar y por qué es importante.")}
  items(items.size){i->val x=items[i];Card(onClick={open=if(open==i)null else i},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp)){Text(x.n,fontWeight=FontWeight.Bold);if(open==i)Text(x.d) else Text("Toca para explicar",style=MaterialTheme.typography.bodySmall)}}}
 }
}
@Composable fun HistoryReasonV38(lang:String,onBack:()->Unit){
 var reason by remember{mutableStateOf("")}
 var visitType by remember{mutableStateOf<String?>(null)}
 var translatorOpen by remember{mutableStateOf(false)}
 val selectedSymptoms=remember{mutableStateMapOf<String,Boolean>()}
 if(translatorOpen){ ColloquialDentalTranslatorV42{translatorOpen=false}; return }
 val visitTypes=listOf("Urgencia","Primera vez","Otra causa")
 val symptoms=listOf("Dolor","Inflamación / aumento de volumen","Secreción / exudado / pus","Fístula","Sangrado","Movilidad dental","Traumatismo / fractura","Fiebre o malestar referido","Dificultad para masticar","Sensibilidad a frío","Sensibilidad a calor","Sensibilidad a dulce","Dolor al masticar","Dolor nocturno","Otro")
 val guide=listOf(
  E("Fecha de inicio","¿Cuándo comenzó? Registra fecha o tiempo aproximado. Evita inventar precisión si el paciente no la recuerda."),
  E("Factor desencadenante","Pregunta qué estaba ocurriendo cuando inició: espontáneo, alimento, frío/calor, masticación, traumatismo, procedimiento previo u otro factor referido."),
  E("Evolución","Aclara si ha aumentado, disminuido, permanece igual, aparece por episodios o cambió de características desde el inicio."),
  E("Características de los síntomas","En dolor: localización, irradiación, intensidad referida, duración, espontáneo/provocado y estímulos. En otros síntomas describe tamaño, frecuencia, duración y cambios."),
  E("Signos observables","Se registran después de explorar: aumento de volumen, cambio de color, fístula, exudado, sangrado, movilidad, fractura u otros hallazgos comprobados."),
  E("Factores que alivian o agravan","Pregunta por frío, calor, masticación, postura, reposo, alimentos y cualquier medida que modifique el problema."),
  E("Medicamentos usados","Pregunta qué tomó o aplicó, dosis/presentación si la conoce, frecuencia, desde cuándo y si produjo alivio. Registrar lo referido; no convertirlo en prescripción.")
 )
 val examples=listOf(
  "“Me duele una muela cuando tomo frío.” → conservar la frase; después caracterizar el dolor y realizar las pruebas correspondientes.",
  "“Le salió una bolita en la encía a mi hijo y le sale agua blanca.” → conservar la frase; después describir clínicamente aumento de volumen, trayecto/fístula o exudado si realmente se observan.",
  "“Tengo inflamado aquí y no puedo comer.” → precisar sitio, inicio, evolución, dolor, función, signos locales y síntomas generales.",
  "“Se le rompió el diente.” → precisar mecanismo, tiempo, órgano dentario, tejidos comprometidos y síntomas."
 )
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Motivo de consulta y padecimiento actual",onBack,"El motivo se escribe literalmente con las palabras del paciente o tutor. El padecimiento actual se construye después, con interrogatorio y hallazgos clínicos.")}
  item{SectionCard("1 · Tipo de consulta"){visitTypes.forEach{x->FilterChip(selected=visitType==x,onClick={visitType=x},label={Text(x)},modifier=Modifier.fillMaxWidth())}}}
  item{SectionCard("2 · Motivo de consulta literal"){OutlinedTextField(reason,{reason=it.take(300)},modifier=Modifier.fillMaxWidth(),label={Text("Palabras exactas del paciente, mamá/papá o tutor")},placeholder={Text("Ej.: “Me duele una muela cuando tomo frío.”")},minLines=3);Text("Debe conservarse la expresión original. No escribas aquí un diagnóstico.",style=MaterialTheme.typography.bodySmall)}}
  item{SectionCard("3 · Traductor coloquial → clínico"){Button(onClick={translatorOpen=true},modifier=Modifier.fillMaxWidth()){Text("Abrir traductor · 50 expresiones")};Spacer(Modifier.height(6.dp));examples.forEach{Text("• $it")}}}
  item{Text("4 · Síntomas referidos · selecciona los presentes",fontWeight=FontWeight.Bold)}
  items(symptoms.size){i->val x=symptoms[i];Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){Checkbox(selectedSymptoms[x]==true,{selectedSymptoms[x]=it});Text(x,Modifier.weight(1f))}}
  item{Text("5 · Construcción del padecimiento actual",fontWeight=FontWeight.Bold)}
  items(guide.size){i->val x=guide[i];Card(Modifier.fillMaxWidth()){Column(Modifier.padding(13.dp)){Text(x.n,fontWeight=FontWeight.Bold);Text(x.d)}}}
  item{SectionCard("Resumen didáctico de lo seleccionado"){Text("Tipo: "+(visitType?:"sin seleccionar"));Text("Motivo literal: "+if(reason.isBlank())"sin escribir" else reason);val positives=selectedSymptoms.filterValues{it}.keys;Text("Síntomas: "+if(positives.isEmpty())"ninguno seleccionado" else positives.joinToString())}}
  item{NoticeCard("La interpretación clínica no debe convertir automáticamente una frase coloquial en un diagnóstico definitivo. El diagnóstico requiere integrar interrogatorio, exploración y pruebas indicadas.")}
 }
}

@Composable fun HistoryHereditaryV38(lang:String,onBack:()->Unit){
 val relatives=listOf("Madre","Padre","Hermana/o","Hija/o","Tía/o","Abuela/o")
 val categories=linkedMapOf(
  "Cardiovasculares" to listOf("Hipertensión arterial","Infarto","Cardiopatía","Evento vascular referido","Otra cardiovascular"),
  "Endocrinos" to listOf("Diabetes mellitus","Hipotiroidismo","Hipertiroidismo","Obesidad","Resistencia a la insulina","Otra endocrina"),
  "Pulmonares" to listOf("Asma","EPOC","Tuberculosis","Fibrosis pulmonar","Bronquitis crónica","Otra pulmonar"),
  "Alérgicos" to listOf("Alergia a medicamentos","Alergia a alimentos","Rinitis alérgica","Dermatitis atópica","Urticaria","Otra alergia"),
  "Neurológicos" to listOf("Epilepsia","Alzheimer","Parkinson","Migraña","Esclerosis múltiple","Otra neurológica"),
  "Neoplásicos" to listOf("Cáncer de mama","Cáncer de próstata","Cáncer colorrectal","Cáncer de pulmón","Cáncer de cabeza/cuello","Leucemia/linfoma","Otro cáncer"),
  "ETS / infecciosos relevantes" to listOf("VIH referido","Sífilis","Hepatitis B","Herpes genital referido","VPH referido","Otra infección referida"),
  "Otros" to listOf("Psoriasis","Enfermedad renal","Enfermedad ósea/hereditaria","Alteración congénita","Enfermedad autoinmune","Otra")
 )
 var relative by remember{mutableStateOf(0)}
 var category by remember{mutableStateOf(categories.keys.first())}
 val selected=remember{mutableStateMapOf<String,Boolean>()}
 val status=remember{mutableStateMapOf<String,String>()}
 var openHelp by remember{mutableStateOf(false)}
 val current=relatives[relative]
 val diseases=categories[category]?:emptyList()
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes heredo-familiares",onBack,"Primero selecciona el familiar y después la categoría de enfermedad. Ante un positivo, amplía inicio, evolución, estado actual, medicamentos y complicaciones.")}
  item{NoticeCard("Este apartado busca antecedentes familiares que puedan aportar predisposición o contexto clínico. No significa que el paciente padezca la misma enfermedad.")}
  item{SectionCard("1 · Familiar"){relatives.forEachIndexed{i,x->FilterChip(relative==i,{relative=i},{Text(x)},modifier=Modifier.fillMaxWidth())}}}
  item{SectionCard("2 · Categoría"){categories.keys.forEach{x->FilterChip(category==x,{category=x},{Text(x)},modifier=Modifier.fillMaxWidth())}}}
  item{Text("3 · $category · $current",fontWeight=FontWeight.Bold)}
  items(diseases.size){i->
   val d=diseases[i];val key="$current|$category|$d";val checked=selected[key]==true
   Card(Modifier.fillMaxWidth()){Column(Modifier.padding(10.dp)){
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){Checkbox(checked,{selected[key]=it;if(!it)status.remove(key)});Text(d,Modifier.weight(1f))}
    if(checked){Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){listOf("Vive","Falleció","No sabe").forEach{x->FilterChip(status[key]==x,{status[key]=x},{Text(x)})}};Text("Amplía: inicio/edad aproximada · evolución · estado actual · tratamiento/medicamentos · complicaciones.",style=MaterialTheme.typography.bodySmall)}
   }}
  }
  item{Card(onClick={openHelp=!openHelp},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(13.dp)){Text("4 · ¿Cómo registrar un positivo?",fontWeight=FontWeight.Bold);if(openHelp){Text("Ejemplo de estructura: «Abuela materna · diabetes mellitus · inicio aproximado ___ · evolución/estado actual ___ · tratamiento referido ___ · complicaciones ___ · vive/falleció/no sabe».");Text("Ejemplos del material docente incluyen abuelo paterno con hipertensión, abuela materna con diabetes, madre con resistencia a la insulina, hermano con asma, hermana con alergia a penicilina y abuelo con Alzheimer.")}else Text("Toca para ver la estructura")}}}
  item{SectionCard("5 · Cuando no hay información"){Text("Usa la opción que corresponda al interrogatorio: «Negado» · «Sin antecedentes» · «No referido». No son equivalentes: «no referido» indica que no se obtuvo o no se proporcionó el dato.")}}
  item{SectionCard("Resumen familiar"){val positives=selected.filterValues{it}.keys;if(positives.isEmpty())Text("Aún no hay antecedentes positivos seleccionados.") else positives.forEach{key->val p=key.split("|");Text("• "+p.joinToString(" · ")+" · "+(status[key]?:"estado no indicado"))}}}
 }
}

@Composable fun HistoryNonPathV38(lang:String,onBack:()->Unit){
 var section by remember{mutableStateOf("Vivienda")}
 var sub by remember{mutableStateOf("")}
 val chosen=remember{mutableStateMapOf<String,String>()}
 val multi=remember{mutableStateMapOf<String,Boolean>()}
 val sections=listOf("Vivienda","Higiene","Alimentación","Inmunizaciones","Tabaquismo","Alcohol","Drogas","Perforaciones","Tatuajes")
 fun optionsCard(title:String,options:List<String>,note:String="")=@Composable{
  SectionCard(title){if(note.isNotBlank())Text(note,style=MaterialTheme.typography.bodySmall);options.forEach{x->FilterChip(chosen[title]==x,{chosen[title]=x},{Text(x)},modifier=Modifier.fillMaxWidth())}}
 }
 val vaccines=linkedMapOf(
  "BCG" to "Prevención de formas graves de tuberculosis.",
  "Hepatitis B" to "Previene infección por virus de hepatitis B y sus complicaciones.",
  "Hexavalente acelular" to "Protege contra difteria, tosferina, tétanos, poliomielitis, Haemophilus influenzae tipo b y hepatitis B.",
  "Rotavirus" to "Previene gastroenteritis grave por rotavirus en lactantes.",
  "Neumococo conjugada" to "Previene enfermedad neumocócica invasiva y otras infecciones por neumococo.",
  "Influenza" to "Previene influenza y reduce riesgo de enfermedad grave; se aplica según edad/temporada/condición.",
  "SRP" to "Triple viral: sarampión, rubéola y parotiditis.",
  "SR" to "Doble viral: sarampión y rubéola.",
  "DPT" to "Protege contra difteria, tosferina y tétanos.",
  "Td" to "Protege contra tétanos y difteria.",
  "Tdpa" to "Protege contra tétanos, difteria y tosferina; tiene indicaciones específicas, incluido embarazo.",
  "VPH" to "Previene infección por tipos de VPH asociados a cánceres y otras enfermedades.",
  "COVID-19" to "Previene principalmente enfermedad grave por COVID-19; indicación vigente depende de edad y riesgo.",
  "Hepatitis A" to "Previene hepatitis A; aparece en las acciones de vacunación mexicanas por grupos de edad.",
  "VSR materna" to "Vacunación durante el embarazo para proteger al bebé frente a enfermedad por virus sincitial respiratorio."
 )
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes personales no patológicos",onBack,"Todo se responde seleccionando opciones predeterminadas. El estudiante no necesita escribir texto libre.")}
  item{sections.forEach{x->FilterChip(section==x,{section=x;sub=""},{Text(x)},modifier=Modifier.fillMaxWidth())}}
  if(section=="Vivienda"){
   item{optionsCard("Número de cuartos",listOf("1","2","3","4","5","6 o más"))}
   item{optionsCard("Número de habitantes",listOf("1","2","3","4","5","6","7 o más"),"Permite contextualizar posible hacinamiento junto con el número de habitaciones.")}
   item{optionsCard("Techo",listOf("Concreto/losa","Lámina metálica","Fibrocemento sin asbesto referido","Asbesto/amianto referido","Teja","Madera","Palma/material vegetal","Otro/no sabe"),"El asbesto/amianto es carcinógeno; la exposición a fibras puede causar mesotelioma, cáncer pulmonar y otras enfermedades respiratorias. Registrar material referido, no diagnosticar exposición.")}
   item{optionsCard("Paredes",listOf("Concreto/block/ladrillo","Adobe","Madera","Lámina","Material vegetal","Mixto","Otro/no sabe"))}
   item{optionsCard("Pintura de las paredes",listOf("Sin pintura","Pintura actual/sin plomo referido","Pintura antigua con plomo conocida","Pintura antigua descascarada o deteriorada","Tipo de pintura desconocido"),"La pintura con plomo y su polvo son una fuente de exposición. El riesgo aumenta cuando la pintura se deteriora, se descascara, se lija o se remueve; es especialmente importante en niñas, niños y embarazo. Registrar exposición referida, no diagnosticar intoxicación.")}
   item{optionsCard("Piso",listOf("Tierra","Cemento/concreto","Loseta/cerámica","Madera","Vinilo/laminado","Otro/no sabe"))}
   item{optionsCard("Ventilación",listOf("Adecuada referida","Limitada","Sin ventilación aparente","No sabe"))}
   item{Text("Servicios domiciliarios",fontWeight=FontWeight.Bold)}
   items(listOf("Agua entubada","Drenaje","Electricidad","Gas","Internet","Recolección de basura").size){i->val x=listOf("Agua entubada","Drenaje","Electricidad","Gas","Internet","Recolección de basura")[i];FilterChip(multi["serv|$x"]==true,{multi["serv|$x"]=! (multi["serv|$x"]?:false)},{Text(x)},modifier=Modifier.fillMaxWidth())}
  }
  if(section=="Higiene"){
   item{optionsCard("Baño corporal",listOf("Menos de 1 vez/semana","1–2/semana","3–4/semana","5–6/semana","Diario","2 o más/día"))}
   item{optionsCard("Cepillado dental",listOf("No se cepilla","Menos de 1 vez/día","1 vez/día","2 veces/día","3 veces/día","4 o más/día"))}
   item{optionsCard("Hilo/interdental",listOf("Nunca","Ocasional","1–3 veces/semana","4–6 veces/semana","Diario"))}
   item{optionsCard("Cambio de ropa",listOf("Menos de 1/semana","1–2/semana","3–4/semana","5–6/semana","Diario","Más de 1/día"))}
  }
  if(section=="Alimentación"){
   item{NoticeCard("Se registra frecuencia, no una calificación automática de la dieta. En odontología importa especialmente cuántas veces se expone la boca a azúcares y bebidas azucaradas.")}
   items(listOf("Carne/proteína animal","Huevo","Lácteos","Fruta","Verdura","Cereales/tubérculos","Leguminosas").size){i->val x=listOf("Carne/proteína animal","Huevo","Lácteos","Fruta","Verdura","Cereales/tubérculos","Leguminosas")[i];optionsCard(x,listOf("0/semana","1/semana","2–3/semana","4–6/semana","1/día","2 o más/día"),"Selecciona la frecuencia habitual referida.")}
   items(listOf("Dulces","Refrescos/bebidas azucaradas","Comida chatarra","Embutidos","Enlatados").size){i->val x=listOf("Dulces","Refrescos/bebidas azucaradas","Comida chatarra","Embutidos","Enlatados")[i];optionsCard(x,listOf("Nunca","Menos de 1/semana","1–3/semana","4–6/semana","1/día","2–3/día","4 o más/día"),"Mayor frecuencia = mayor exposición habitual; para dulces/refrescos, exposiciones frecuentes a azúcares son especialmente relevantes para riesgo de caries. No equivale por sí sola a diagnóstico.")}
   item{optionsCard("Comidas al día",listOf("1","2","3","4","5","6 o más"))}
  }
  if(section=="Inmunizaciones"){
   item{NoticeCard("Listado educativo basado en vacunas vigentes/relevantes del esquema mexicano. Selecciona: aplicada, no aplicada o no sabe. La indicación depende de edad, embarazo, antecedentes y riesgo.")}
   items(vaccines.entries.toList().size){i->val v=vaccines.entries.toList()[i];Card(Modifier.fillMaxWidth()){Column(Modifier.padding(12.dp)){Text(v.key,fontWeight=FontWeight.Bold);Text(v.value,style=MaterialTheme.typography.bodySmall);Row(horizontalArrangement=Arrangement.spacedBy(4.dp)){listOf("Sí","No","No sabe").forEach{x->FilterChip(chosen["vac|"+v.key]==x,{chosen["vac|"+v.key]=x},{Text(x)})}}}}}
  }
  if(section=="Tabaquismo"){
   item{optionsCard("Tabaquismo · estado",listOf("Nunca","Exfumador","Actual","Exposición pasiva","No sabe"))}
   item{optionsCard("Tabaco · frecuencia",listOf("Ocasional","1–5 cigarrillos/día","6–10/día","11–20/día","Más de 20/día","No sabe"))}
   item{optionsCard("Tabaco · tiempo",listOf("<1 año","1–5 años","6–10 años","11–20 años",">20 años","No sabe"))}
   item{optionsCard("Producto",listOf("Cigarrillo","Puro","Pipa","Tabaco sin humo","Vapeador/cigarrillo electrónico","Más de uno","Otro/no sabe"))}
  }
  if(section=="Alcohol"){
   item{optionsCard("Alcohol · estado",listOf("Nunca","Anteriormente","Actual","No sabe"))}
   item{optionsCard("Alcohol · frecuencia",listOf("Menos de 1/mes","1–3/mes","1/semana","2–3/semana","4–6/semana","Diario","No sabe"))}
   item{optionsCard("Alcohol · cantidad por ocasión",listOf("1 bebida","2 bebidas","3–4 bebidas","5–6 bebidas","7 o más","No sabe"))}
   item{optionsCard("Tipo habitual",listOf("Cerveza","Vino","Destilados","Bebidas preparadas","Varios","Otro/no sabe"))}
  }
  if(section=="Drogas"){
   item{optionsCard("Sustancia referida",listOf("Ninguna","Cannabis","Cocaína/crack","Metanfetaminas/estimulantes","Opioides","Alucinógenos","Inhalables","Sedantes sin indicación referida","Varias","Otra/no sabe"))}
   item{optionsCard("Frecuencia",listOf("Nunca","Una vez/experimental","Menos de 1/mes","1–3/mes","1–6/semana","Diario","No sabe"))}
   item{optionsCard("Vía",listOf("Fumada/vaporizada","Oral","Intranasal","Inyectada","Inhalada","Otra/no sabe"))}
   item{optionsCard("Último consumo",listOf("<24 h","1–7 días","1–4 semanas","1–12 meses",">1 año","No recuerda/no sabe"))}
  }
  if(section=="Perforaciones"){
   item{optionsCard("Perforaciones",listOf("Ninguna","Oreja","Nariz","Labio","Lengua","Mejilla","Ceja","Otra","Múltiples"))}
   item{optionsCard("Antigüedad",listOf("<1 mes","1–6 meses","7–12 meses","1–5 años",">5 años","No sabe"))}
   item{optionsCard("Complicaciones referidas",listOf("Ninguna","Dolor","Inflamación","Sangrado","Infección/secreción","Trauma dental/gingival","Alergia/irritación","Otra/no sabe"))}
  }
  if(section=="Tatuajes"){
   item{optionsCard("Número de tatuajes",listOf("Ninguno","1","2–3","4–5","6 o más"))}
   item{optionsCard("Antigüedad del más reciente",listOf("<1 mes","1–6 meses","7–12 meses","1–5 años",">5 años","No sabe"))}
   item{optionsCard("Lugar de realización",listOf("Estudio establecido","Servicio sanitario/profesional referido","Domicilio/no profesional","Centro penitenciario","Otro","No sabe"))}
   item{optionsCard("Complicaciones referidas",listOf("Ninguna","Infección","Reacción alérgica/dermatitis","Sangrado prolongado","Cicatrización anormal","Otra/no sabe"))}
  }
 }
}

@Composable fun HistoryGynecoV38(lang:String,onBack:()->Unit){
 var sex by remember{mutableStateOf("")};var open by remember{mutableStateOf<Int?>(null)}
 val q=listOf(
  E("Menarca","Primera menstruación. Opciones educativas: aún no presenta / edad 8–9 / 10–11 / 12–13 / 14–15 / 16 o más / no recuerda. Se pregunta para contextualizar maduración y etapa reproductiva; una edad aislada no establece enfermedad."),
  E("Inicio de vida sexual activa (IVSA)","Opciones: no ha iniciado / edad al inicio por intervalos / prefiere no responder / no recuerda. Es información confidencial y se pregunta sólo con pertinencia clínica, respeto y privacidad."),
  E("Embarazos (gestas)","Opciones: 0 / 1 / 2 / 3 / 4 / 5 o más. El número de gestas permite organizar después partos, cesáreas, abortos y embarazo actual."),
  E("Partos vaginales","Opciones: 0 / 1 / 2 / 3 / 4 / 5 o más. Debe ser congruente con el total de gestas."),
  E("Cesáreas","Opciones: 0 / 1 / 2 / 3 / 4 / 5 o más. Una cesárea también cuenta como antecedente quirúrgico y debe aparecer en ese apartado."),
  E("Abortos / pérdidas gestacionales","Opciones: 0 / 1 / 2 / 3 o más / prefiere no responder. Registrar sin juicios y ampliar sólo cuando sea clínicamente pertinente."),
  E("Fecha de última menstruación (FUM)","Opciones: fecha conocida / no recuerda / ciclos irregulares / amenorrea / menopausia. Ayuda a contextualizar posibilidad de embarazo y etapa reproductiva."),
  E("Embarazo actual","Opciones: no / sí y semanas de gestación / posible-no confirmado / no sabe. Si es positivo, registrar semanas y control prenatal referido."),
  E("Anticoncepción","Opciones: ninguno / barrera / hormonal oral / inyectable / implante / DIU / esterilización / otro. Registrar método referido, no inferirlo."),
  E("Lactancia","Opciones: no / sí actualmente / antecedente. Puede ser relevante para selección de medicamentos cuando exista tratamiento."),
  E("Menopausia","Opciones: no aplica / sí y edad aproximada / no recuerda. Contextualiza etapa hormonal y antecedentes generales.")
 )
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes gineco-obstétricos",onBack,"El estudiante selecciona opciones predeterminadas y puede abrir cada concepto para aprender qué significa y por qué se pregunta.")}
  item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(sex=="H",{sex="H"},{Text("Hombre")});FilterChip(sex=="M",{sex="M"},{Text("Mujer")})}}
  if(sex=="H")item{NoticeCard("No aplica este interrogatorio gineco-obstétrico.")}
  if(sex=="M")items(q.size){i->val x=q[i];Card(onClick={open=if(open==i)null else i},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp)){Text(x.n,fontWeight=FontWeight.Bold);if(open==i)Text(x.d)else Text("Toca para ver significado, opciones y utilidad clínica",style=MaterialTheme.typography.bodySmall)}}}
 }
}

@Composable fun HistorySurgicalTraumaV38(lang:String,onBack:()->Unit){
 val groups=listOf(
  E("Cirugías","Opciones frecuentes para el interrogatorio: cesárea, apendicectomía, colecistectomía, hernioplastia, amigdalectomía/adenoidectomía, cirugía ortopédica, cirugía maxilofacial/dental, cirugía ginecológica, otra. Registrar tipo, fecha aproximada, complicaciones y secuelas."),
  E("Hospitalizaciones","Opciones orientadoras: parto/cesárea, neumonía/infección, accidente/trauma, cirugía programada, enfermedad gastrointestinal, descompensación metabólica/cardiovascular, otra. Registrar motivo, año/edad, duración y complicaciones."),
  E("Partos y cesáreas","Si hubo cesárea, enlazarla también con antecedentes gineco-obstétricos. Registrar número y fecha aproximada; evitar duplicar información contradictoria."),
  E("Transfusiones sanguíneas","Opciones: nunca / sí una vez / varias / no recuerda. Si sí: motivo, fecha aproximada, reacciones y producto si se conoce."),
  E("Donación de sangre","Opciones: nunca / sí / no recuerda; registrar última donación aproximada y reacción si existió."),
  E("Trasplantes","Opciones: ninguno / renal / hepático / cardiaco / pulmonar / médula o células hematopoyéticas / otro. Registrar fecha, inmunosupresión y seguimiento médico."),
  E("Traumatismos, fracturas y luxaciones","Opciones por región: cráneo/cara, mandíbula/maxilar, dientes, columna, extremidades u otras. Registrar fecha, tratamiento, secuelas y repercusión en oclusión/ATM cuando corresponda.")
 )
 explain("Antecedentes quirúrgicos, hospitalarios y traumáticos",groups,onBack)
}

@Composable fun HistoryPhysicalV38(lang:String,onBack:()->Unit)=explain("Exploración física",listOf(
 E("Signos vitales y somatometría","Temperatura, tensión arterial, frecuencia respiratoria, frecuencia cardiaca, peso, talla, IMC y glucosa capilar. El formato fuente incluye estos campos e IMC; los rangos por edad/sexo se mostrarán como referencia separada para no inventarlos a partir del formato."),
 E("Inspección general","Edad aparente, marcha, facies, actitud, constitución/habitus, movimientos anormales, conciencia, actitud psicológica, cuidado personal y cooperación."),
 E("Cráneo","Forma, volumen, implantación del cabello, exostosis/abultamientos, hundimientos/depresiones, simetría, lesiones y dolor. Las referencias visuales se incorporarán desde fuentes reales citadas, no como dibujo vectorial."),
 E("Cara / facies","Perfil, simetría, color de tez, volumen, lesiones y movimientos. La fuente usa ejemplos de normocromía, palidez, ictericia, cianosis y eritema; la imagen será referencia clínica citada."),
 E("Músculos de la expresión facial","Valorar tono, simetría y función; registrar normotonía, hipotonía o hipertonía cuando corresponda y describir el hallazgo."),
 E("Músculos de la masticación","Explorar maseteros, temporales y músculos accesibles clínicamente según técnica; valorar dolor, tono, hipertrofia/asimetría y función."),
 E("Cuello","Inspección y palpación de simetría, movilidad, masas, dolor y otros hallazgos pertinentes."),
 E("Cadenas ganglionares","Preauriculares, mastoideos, submandibulares, cervicales, submentonianos y claviculares: palpable/no palpable, fijos/móviles, dolorosos/no dolorosos y descripción."),
 E("ATM","Dolor, chasquido, crepitación, desviación, limitación, apertura/cierre, lateralidades, protrusión/retrusión, apertura máxima y relación con línea media; complementar con DVO/DVR cuando corresponda.")
),onBack)

@Composable fun HistoryOrthoV38(lang:String,onBack:()->Unit){
 var prior by remember{mutableStateOf<Boolean?>(null)}
 val options=listOf("Brackets metálicos","Brackets estéticos","Alineadores transparentes","Aparato removible","Expansor palatino","Mantenedor de espacio","Aparato funcional/ortopédico","Retenedor fijo","Retenedor removible Hawley","Retenedor transparente","Extracciones asociadas al tratamiento","Cirugía ortognática asociada","Otro")
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes de tratamientos ortodónticos",onBack,"Primero indica si recibió tratamiento previo; si la respuesta es sí, aparecen opciones de aparatología y retención.")}
  item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(prior==false,{prior=false},{Text("No")});FilterChip(prior==true,{prior=true},{Text("Sí")})}}
  if(prior==true)items(options.size){i->Card(Modifier.fillMaxWidth()){Text("□ "+options[i],Modifier.padding(13.dp))}}
  if(prior==true)item{NoticeCard("Además registra edad aproximada al tratamiento, duración, motivo de indicación, si lo terminó, tiempo de retención y resultado referido.")}
 }
}

@Composable fun HistoryDentalAlterationsV38(lang:String,onBack:()->Unit){
 val groups=listOf(
  E("Número · disminución","Hipodoncia/agenesia, oligodoncia y anodoncia. Registrar órgano(s) dentario(s), localizado/generalizado y confirmar con historia/radiografía cuando corresponda."),
  E("Número · aumento","Dientes supernumerarios; registrar localización, erupcionado/no erupcionado y relación con dientes vecinos."),
  E("Tamaño","Microdoncia y macrodoncia; localizada o generalizada. Comparar con anatomía, arco y dientes homólogos."),
  E("Forma / morfología","Fusión, geminación, concrescencia, dens invaginatus, dens evaginatus, taurodontismo y otras alteraciones morfológicas. No inferir etiología sólo por apariencia."),
  E("Estructura","Defectos del esmalte, amelogénesis imperfecta, dentinogénesis imperfecta, displasia dentinaria y alteraciones de tejidos dentarios; integrar clínica, antecedentes y radiografía."),
  E("Color","Cambio intrínseco o extrínseco; registrar distribución, dientes afectados, tonalidad, antecedentes y hallazgos asociados antes de orientar causa."),
  E("Erupción y posición","Erupción ectópica, transposición, erupción tardía/retardada, erupción precoz/prematura, retención/no erupción más allá de lo esperado, impactación, inclusión intraósea, falla primaria de erupción, diente natal, diente neonatal, anquilosis/diente sumergido, obstáculo local sospechado, asimetría eruptiva y desplazamiento/posición anómala. Retención, inclusión e impactación pueden solaparse entre fuentes: primero describir clínica y radiografía.")
 )
 explain("Alteraciones de órganos dentarios",groups,onBack)
}

@Composable fun HistoryHabitsV38(lang:String,onBack:()->Unit)=explain("Hábitos y parafunciones",listOf(E("Succión digital","Pregunta dedo, frecuencia, duración e intensidad. Extraoral: postura labial/facial. Intraoral: incisivos, overjet, mordida abierta, arco y paladar."),E("Chupón o mamila prolongados","Registra edad y patrón. Extraoral: postura labial. Intraoral: mordida, arco y erupción."),E("Respiración oral","Pregunta respiración y sueño. Extraoral: labios entreabiertos/postura. Intraoral: sequedad, gingivitis y patrón oclusal; la causa respiratoria debe investigarse."),E("Interposición lingual / deglución atípica","Observa deglución, lengua y competencia labial; intraoralmente puede acompañarse de mordida abierta o espacios."),E("Onicofagia / mordisqueo","Extraoral: uñas/labios. Intraoral: desgaste, fracturas pequeñas, trauma mucoso o recesión localizada."),E("Bruxismo y apretamiento","Pregunta sueño/vigilia, fatiga y dolor. Extraoral: músculos. Intraoral: facetas, fracturas, restauraciones dañadas y línea alba; ningún signo aislado confirma el diagnóstico.")),onBack)
@Composable fun HistoryOralExamV38(lang:String,onBack:()->Unit){
 val sites=listOf(
  E("Piel peribucal","Normal: piel íntegra, sin lesiones evidentes y simetría conservada. Selección de hallazgos: eritema/cambio de color, descamación, costra, fisura, úlcera, vesícula/ampolla, pápula/nódulo, aumento de volumen, cicatriz, pigmentación o asimetría. Describir antes de diagnosticar."),
  E("Labio superior","Explorar piel, bermellón y mucosa labial superior: color, hidratación, simetría, integridad y superficie. Hallazgos seleccionables: resequedad, fisura, costra, erosión/úlcera, placa/mancha blanca o roja, pigmentación, vesículas, aumento de volumen o lesión palpable."),
  E("Labio inferior","Explorar piel, bermellón y mucosa labial inferior con los mismos criterios; registrar sitio, tamaño, color, superficie, consistencia y síntomas de cualquier lesión."),
  E("Comisura derecha","Normal: continuidad e integridad sin fisura ni ulceración. Hallazgos: fisura, eritema, maceración, costra, erosión/úlcera, lesión blanca/roja, aumento de volumen u otro."),
  E("Comisura izquierda","Comparar bilateralmente. Seleccionar normal o el hallazgo observable y describirlo; no convertir automáticamente una fisura en un diagnóstico etiológico."),
  E("Carrillo derecho / mucosa bucal","Normal puede verse rosada, húmeda y lisa, con variantes anatómicas. Hallazgos: línea alba, mordisqueo/queratosis friccional sospechada, placa/mancha blanca, eritema, úlcera/erosión, pigmentación, pápula/nódulo, vesícula/ampolla, aumento de volumen o lesión palpable."),
  E("Carrillo izquierdo / mucosa bucal","Mismos criterios del lado derecho y comparación bilateral. Inspeccionar también desembocadura de Stensen y registrar alteraciones sin asumir etiología."),
  E("Encía","Normal clínicamente compatible con tejido firme, contorno adaptado y color variable según pigmentación fisiológica. Hallazgos: eritema, edema/aumento de volumen, sangrado, ulceración, recesión, hiperplasia, pigmentación, lesión blanca/roja, masa u otro."),
  E("Piso de boca","Normal: mucosa fina, húmeda, sin masa o ulceración evidente; inspeccionar con lengua elevada y palpar cuando corresponda. Hallazgos: aumento de volumen, induración, úlcera/erosión, cambio blanco/rojo, pigmentación, lesión quística aparente, asimetría o alteración de conductos salivales."),
  E("Paladar duro","Normal: mucosa masticatoria firme y queratinizada. Hallazgos: cambio de color, úlcera, erosión, placa/mancha, pigmentación, petequias, aumento de volumen, torus/variación anatómica o masa."),
  E("Paladar blando","Normal: mucosa más flexible y móvil. Hallazgos: eritema, petequias, úlcera/erosión, placa/mancha, asimetría, aumento de volumen o alteración del movimiento."),
  E("Orofaringe / pared posterior","Examinar con buena iluminación y lengua deprimida cuando sea necesario. Selecciones: aspecto sin alteración evidente, eritema, exudado, lesión, aumento de volumen, asimetría u otro."),
  E("Úvula","Normal: centrada o sin desviación significativa y movilidad conservada al fonar. Hallazgos: desviación, edema/aumento de volumen, eritema, lesión superficial o alteración del movimiento."),
  E("Pilares amigdalinos","Inspeccionar pilares anterior y posterior bilateralmente. Hallazgos: simetría conservada, eritema, lesión, ulceración, exudado o aumento de volumen."),
  E("Amígdala derecha","Registrar tamaño/aspecto, simetría, eritema, exudado, lesión o aumento de volumen; una apariencia aislada no establece etiología."),
  E("Amígdala izquierda","Comparar con el lado derecho y registrar los mismos criterios."),
  E("Lengua · dorso","Normal: papilas y superficie compatibles con variación anatómica. Hallazgos: saburra, depapilación, lengua geográfica, fisuras, placa/mancha, pigmentación, úlcera, aumento de volumen o lesión."),
  E("Lengua · bordes laterales","Inspeccionar y palpar bilateralmente. Hallazgos: úlcera/erosión, placa/mancha blanca o roja, induración, masa, trauma aparente, pigmentación o asimetría."),
  E("Lengua · cara ventral","Normal: mucosa delgada con vasos visibles como variante frecuente. Hallazgos: lesión blanca/roja, úlcera, masa, induración, alteración vascular aparente u otro."),
  E("Frenillo lingual","Registrar inserción, movilidad lingual y aspecto. Opciones: sin alteración evidente / inserción que limita movilidad / lesión traumática / inflamación / otro. La apariencia sola no basta para diagnosticar anquiloglosia funcional."),
  E("Frenillo labial superior","Observar inserción, grosor, integridad y relación con encía/diastema. Opciones: aspecto habitual / inserción prominente o baja / trauma-ulceración / inflamación / otro."),
  E("Frenillo labial inferior","Observar inserción, integridad y tensión sobre tejidos. Opciones: aspecto habitual / inserción prominente / trauma-ulceración / inflamación / otro.")
 )
 var selected by remember{mutableStateOf<Int?>(null)}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Examen peribucal e intrabucal / mucosas",onBack,"Exploración por sitio anatómico. Toca cada estructura para ver aspecto normal, qué observar y alteraciones seleccionables. Primero se describe el hallazgo; después se orienta el diagnóstico.")}
  item{NoticeCard("Secuencia sugerida: piel peribucal → labios y comisuras → mucosa labial/frenillos → carrillos → encía → paladares → orofaringe/úvula/pilares/amígdalas → lengua → frenillo lingual → piso de boca.")}
  items(sites.size){i->val x=sites[i];Card(onClick={selected=if(selected==i)null else i},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){Text(x.n,fontWeight=FontWeight.Bold);if(selected==i){Text(x.d);Text("□ Normal / sin alteración evidente   □ Hallazgo presente   □ No valorable",style=MaterialTheme.typography.bodySmall)}else Text("Toca para explorar",style=MaterialTheme.typography.bodySmall)}}}
  item{NoticeCard("Las fotografías clínicas que acompañen este módulo serán referencias reales con fuente/cita y se identificarán como normal, variante anatómica o lesión documentada; no se usarán imágenes inventadas como sustituto diagnóstico.")}
 }
}

