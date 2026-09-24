package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
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
 var sex by remember{mutableStateOf("")}
 val chosen=remember{mutableStateMapOf<String,String>()}
 val nums=listOf("0","1","2","3","4","5 o más")
 @Composable fun OptionsCard(title:String,options:List<String>,note:String=""){
  SectionCard(title){
   options.forEach{x->FilterChip(chosen[title]==x,{chosen[title]=x},{Text(x)},modifier=Modifier.fillMaxWidth())}
   if(note.isNotBlank())Text(note,style=MaterialTheme.typography.bodySmall)
  }
 }
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes gineco-obstétricos",onBack,"Registro educativo con opciones predeterminadas. Primero selecciona sexo; el interrogatorio gineco-obstétrico sólo se despliega cuando corresponde.")}
  item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(sex=="H",{sex="H"},{Text("Hombre")});FilterChip(sex=="M",{sex="M"},{Text("Mujer")})}}
  if(sex=="H")item{NoticeCard("No aplica el interrogatorio gineco-obstétrico. No se solicitan ni despliegan estos campos.")}
  if(sex=="M"){
   item{OptionsCard("Menarca",listOf("Aún no presenta","8–9 años","10–11 años","12–13 años","14–15 años","16 años o más","No recuerda"),"Menarca = primera menstruación. Se registra la edad referida; una edad aislada no establece diagnóstico.")}
   item{OptionsCard("Inicio de vida sexual activa (IVSA)",listOf("No ha iniciado","Antes de 15 años","15–17 años","18–20 años","21–25 años","26 años o más","Prefiere no responder","No recuerda"),"Dato confidencial; registrar sólo lo referido por la paciente.")}
   item{OptionsCard("Embarazos / gestas",nums,"Número total de embarazos referidos, independientemente de su desenlace.")}
   item{OptionsCard("Partos vaginales",nums)}
   item{OptionsCard("Cesáreas",nums,"Las cesáreas también deben considerarse en antecedentes quirúrgicos.")}
   item{OptionsCard("Abortos / pérdidas gestacionales",listOf("0","1","2","3","4 o más","Prefiere no responder"),"Registrar sin juicios. La suma de desenlaces debe revisarse contra el número de gestas; la app no inventa datos faltantes.")}
   item{OptionsCard("Tipo de pérdida/aborto referido",listOf("No aplica","Espontáneo","Inducido referido","Ambos antecedentes","No especificado","Prefiere no responder"),"Sólo se registra lo que la paciente refiere; no inferir causa ni circunstancia.")}
   item{OptionsCard("Fecha de última menstruación (FUM)",listOf("Menos de 1 semana","1–2 semanas","3–4 semanas","1–2 meses","3–6 meses","Más de 6 meses","No recuerda","Amenorrea","Menopausia"),"Selección por intervalo para evitar escritura libre en este módulo educativo.")}
   item{OptionsCard("Regularidad del ciclo",listOf("Regular referido","Irregular referido","Amenorrea","Menopausia","No sabe/no recuerda"))}
   item{OptionsCard("Duración habitual del ciclo",listOf("Menos de 21 días","21–24 días","25–35 días","Más de 35 días","Variable","No sabe/no recuerda"))}
   item{OptionsCard("Duración del sangrado",listOf("1–2 días","3–7 días","8 días o más","Variable","No sabe/no recuerda"))}
   item{OptionsCard("Embarazo actual",listOf("No","Sí confirmado referido","Posible/no confirmado","No sabe","Prefiere no responder"),"Si existe embarazo actual, se registra la edad gestacional y el control prenatal referidos.")}
   item{OptionsCard("Semanas de gestación",listOf("No aplica","1–4","5–8","9–12","13–16","17–20","21–24","25–28","29–32","33–36","37–40","Más de 40","No sabe"))}
   item{OptionsCard("Control prenatal",listOf("No aplica","Sí","No","Aún no inicia","No sabe"))}
   item{OptionsCard("Método anticonceptivo",listOf("Ninguno","Condón/barrera","Anticonceptivo oral","Inyectable hormonal","Implante subdérmico","DIU de cobre","DIU hormonal","Parche","Anillo vaginal","Esterilización","Otro método referido","Prefiere no responder"),"Registrar el método referido; no inferir eficacia, adherencia ni indicación.")}
   item{OptionsCard("Lactancia",listOf("No","Sí actualmente","Antecedente de lactancia","No aplica","Prefiere no responder"),"Puede ser relevante al revisar medicamentos y tratamiento odontológico.")}
   item{OptionsCard("Menopausia",listOf("No","Sí: antes de 40 años","Sí: 40–44 años","Sí: 45–49 años","Sí: 50–54 años","Sí: 55 años o más","No recuerda","No aplica"),"Registrar edad aproximada referida, sin diagnosticar a partir de este dato.")}
   item{NoticeCard("Congruencia obstétrica: gestas = total de embarazos. Partos, cesáreas y pérdidas describen desenlaces; si las cantidades no concuerdan, el estudiante debe revisar el interrogatorio en vez de completar datos automáticamente.")}
  }
 }
}

@Composable fun HistorySurgicalTraumaV38(lang:String,onBack:()->Unit){
 var section by remember{mutableStateOf("Cirugías")}
 val chosen=remember{mutableStateMapOf<String,String>()}
 val sections=listOf("Cirugías","Hospitalizaciones","Transfusiones","Donación de sangre","Trasplantes","Traumatismos")
 @Composable fun OptionsCard(title:String,options:List<String>,note:String=""){
  SectionCard(title){options.forEach{x->FilterChip(chosen[title]==x,{chosen[title]=x},{Text(x)},modifier=Modifier.fillMaxWidth())};if(note.isNotBlank())Text(note,style=MaterialTheme.typography.bodySmall)}
 }
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes quirúrgicos, hospitalarios y traumáticos",onBack,"Selecciona opciones predeterminadas. El objetivo es registrar antecedentes, antigüedad, complicaciones y secuelas sin inventar información.")}
  item{sections.forEach{x->FilterChip(section==x,{section=x},{Text(x)},modifier=Modifier.fillMaxWidth())}}
  if(section=="Cirugías"){
   item{OptionsCard("Antecedente de cirugía",listOf("Ninguna","Sí, una","Sí, dos","Sí, tres o más","No recuerda"))}
   item{OptionsCard("Tipo de cirugía",listOf("No aplica","Cesárea","Apendicectomía","Colecistectomía","Hernioplastia","Amigdalectomía/adenoidectomía","Ortopédica","Maxilofacial/dental","Ginecológica","Cardiovascular","Abdominal/digestiva","Otra/no recuerda"))}
   item{OptionsCard("Antigüedad de cirugía",listOf("<1 mes","1–6 meses","7–12 meses","1–5 años","6–10 años",">10 años","Infancia","No recuerda"))}
   item{OptionsCard("Complicaciones quirúrgicas",listOf("Ninguna referida","Infección","Hemorragia","Reacción anestésica referida","Problema de cicatrización","Reintervención","Otra complicación","No sabe/no recuerda"))}
   item{OptionsCard("Secuelas actuales",listOf("Ninguna referida","Dolor","Limitación funcional","Alteración sensitiva","Cicatriz problemática","Otra secuela","No sabe"))}
  }
  if(section=="Hospitalizaciones"){
   item{OptionsCard("Hospitalizaciones previas",listOf("Nunca","1","2","3","4 o más","No recuerda"))}
   item{OptionsCard("Motivo principal",listOf("No aplica","Cirugía programada","Parto/cesárea","Infección/neumonía","Accidente/trauma","Enfermedad gastrointestinal","Descompensación metabólica","Problema cardiovascular","Problema respiratorio","Otra/no recuerda"))}
   item{OptionsCard("Antigüedad de hospitalización",listOf("<1 mes","1–6 meses","7–12 meses","1–5 años","6–10 años",">10 años","Infancia","No recuerda"))}
   item{OptionsCard("Duración aproximada",listOf("<24 horas","1–3 días","4–7 días","8–14 días",">14 días","No recuerda"))}
   item{OptionsCard("Complicaciones durante hospitalización",listOf("Ninguna referida","Infección","Hemorragia","Ingreso a terapia intensiva","Reintervención","Otra","No sabe/no recuerda"))}
  }
  if(section=="Transfusiones"){
   item{OptionsCard("Transfusiones sanguíneas",listOf("Nunca","Sí, una vez","Sí, varias","No recuerda"))}
   item{OptionsCard("Motivo de transfusión",listOf("No aplica","Cirugía","Hemorragia/trauma","Parto/cesárea","Anemia/enfermedad hematológica","Tratamiento oncológico","Otro","No recuerda"))}
   item{OptionsCard("Antigüedad de transfusión",listOf("<1 año","1–5 años","6–10 años",">10 años","Infancia","No recuerda"))}
   item{OptionsCard("Reacción transfusional referida",listOf("No presentó","Fiebre/escalofríos","Reacción alérgica","Dificultad respiratoria","Otra reacción","No sabe/no recuerda"))}
  }
  if(section=="Donación de sangre"){
   item{OptionsCard("Donación de sangre",listOf("Nunca","Sí, una vez","Sí, varias veces","No recuerda"))}
   item{OptionsCard("Última donación",listOf("No aplica","<1 mes","1–6 meses","7–12 meses","1–5 años",">5 años","No recuerda"))}
   item{OptionsCard("Reacción posterior a donación",listOf("No presentó","Mareo/lipotimia","Sangrado prolongado","Malestar general","Otra","No recuerda"))}
  }
  if(section=="Trasplantes"){
   item{OptionsCard("Antecedente de trasplante",listOf("Ninguno","Renal","Hepático","Cardiaco","Pulmonar","Médula/células hematopoyéticas","Otro","No sabe"))}
   item{OptionsCard("Antigüedad del trasplante",listOf("No aplica","<1 año","1–5 años","6–10 años",">10 años","No recuerda"))}
   item{OptionsCard("Inmunosupresión referida",listOf("No aplica","Sí actualmente","Antecedente, ya no","No","No sabe"),"Si existe inmunosupresión, debe vincularse con APP y medicamentos referidos.")}
   item{OptionsCard("Seguimiento médico",listOf("Regular referido","Irregular referido","Sin seguimiento actual","No sabe"))}
  }
  if(section=="Traumatismos"){
   item{OptionsCard("Antecedente de traumatismo/fractura/luxación",listOf("Ninguno","Traumatismo sin fractura","Fractura","Luxación","Más de un tipo","No recuerda"))}
   item{OptionsCard("Región afectada",listOf("No aplica","Cráneo/cara","Mandíbula/maxilar","Dientes","ATM","Columna","Miembro superior","Miembro inferior","Tórax","Otra/múltiples"))}
   item{OptionsCard("Antigüedad del trauma",listOf("<1 mes","1–6 meses","7–12 meses","1–5 años","6–10 años",">10 años","Infancia","No recuerda"))}
   item{OptionsCard("Tratamiento recibido",listOf("No aplica","Observación/reposo","Inmovilización","Reducción","Cirugía","Tratamiento dental","Rehabilitación/fisioterapia","Combinado","No recuerda"))}
   item{OptionsCard("Secuelas",listOf("Ninguna referida","Dolor","Limitación de movimiento","Alteración de mordida/oclusión","Alteración de ATM","Pérdida/daño dental","Alteración sensitiva","Otra","No sabe"),"Trauma de cara, maxilares, dientes o ATM puede requerir correlación con exploración clínica y estudios auxiliares.")}
  }
  item{Button(onClick={},modifier=Modifier.fillMaxWidth()){Text("💾 Guardar antecedentes seleccionados")}}
  item{NoticeCard("Una cesárea también debe ser congruente con antecedentes gineco-obstétricos. Trasplantes e inmunosupresión deben correlacionarse con APP y medicamentos.")}
 }
}

@Composable private fun ClinicalPhotoV38(title:String,url:String,credit:String){
 Card(Modifier.fillMaxWidth()){Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
  Text(title,fontWeight=FontWeight.Bold)
  AsyncImage(model=url,contentDescription=title,modifier=Modifier.fillMaxWidth().height(220.dp),contentScale=ContentScale.Fit)
  Text("Imagen clínica real · "+credit,style=MaterialTheme.typography.bodySmall)
 }}
}

@Composable fun HistoryPhysicalV38(lang:String,onBack:()->Unit){
 var section by remember{mutableStateOf("Signos vitales")}
 val selected=remember{mutableStateMapOf<String,String>()}
 var weight by remember{mutableStateOf("")};var height by remember{mutableStateOf("")}
 @Composable fun Pick(title:String,options:List<String>,note:String=""){
  SectionCard(title){options.forEach{x->FilterChip(selected[title]==x,{selected[title]=x},{Text(x)},modifier=Modifier.fillMaxWidth())};if(note.isNotBlank())Text(note,style=MaterialTheme.typography.bodySmall)}
 }
 val w=weight.toDoubleOrNull();val h=height.toDoubleOrNull();val bmi=if(w!=null&&h!=null&&h>0) w/((h/100)*(h/100)) else null
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Exploración física y signos vitales",onBack,"Registro educativo. Los valores deben medirse; la app no sustituye la valoración clínica ni asigna diagnósticos automáticamente.")}
  item{listOf("Signos vitales","Somatometría","Glucosa capilar","Inspección general","Cráneo y cara","Músculos","Cuello","Ganglios","ATM y dimensión vertical").forEach{x->FilterChip(section==x,{section=x},{Text(x)},modifier=Modifier.fillMaxWidth())}}
  if(section=="Signos vitales"){
   item{Pick("Temperatura (°C)",listOf("<35.0","35.0–35.9","36.0–36.9","37.0–37.9","38.0–38.9","39.0–39.9","≥40.0","No medida"),"Seleccionar el intervalo correspondiente a la medición obtenida; interpretar según sitio y técnica de medición.")}
   item{Pick("Presión arterial sistólica (mmHg)",listOf("<90","90–99","100–109","110–119","120–129","130–139","140–159","160–179","≥180","No medida"))}
   item{Pick("Presión arterial diastólica (mmHg)",listOf("<60","60–69","70–79","80–89","90–99","100–109","≥110","No medida"),"La interpretación depende de edad y contexto clínico. En población pediátrica requiere edad, sexo y talla; no aplicar categorías de adulto automáticamente.")}
   item{Pick("Frecuencia cardiaca (lpm)",listOf("<50","50–59","60–69","70–79","80–89","90–99","100–119","120–139","≥140","No medida"),"Interpretar según edad, reposo, síntomas y contexto clínico.")}
   item{Pick("Frecuencia respiratoria (rpm)",listOf("<10","10–11","12–15","16–20","21–24","25–29","≥30","No medida"),"Los rangos normales varían especialmente con la edad; no clasificar automáticamente a niños con criterios de adulto.")}
  }
  if(section=="Somatometría"){
   item{SectionCard("Peso y talla"){OutlinedTextField(weight,{weight=it.filter{x->x.isDigit()||x=='.'}},label={Text("Peso medido (kg)")},modifier=Modifier.fillMaxWidth());OutlinedTextField(height,{height=it.filter{x->x.isDigit()||x=='.'}},label={Text("Talla medida (cm)")},modifier=Modifier.fillMaxWidth());if(bmi!=null)Text("IMC calculado: %.1f kg/m²".format(bmi),fontWeight=FontWeight.Bold);Text("En adultos el IMC puede contextualizarse con criterios aplicables. En menores de edad debe interpretarse con referencias por edad y sexo; este valor aislado no establece diagnóstico.",style=MaterialTheme.typography.bodySmall)}}
  }
  if(section=="Glucosa capilar"){
   item{Pick("Glucosa capilar (mg/dL)",listOf("<54","54–69","70–99","100–125","126–179","180–199","200–249","250–299","≥300","No medida"))}
   item{Pick("Contexto de la medición",listOf("Ayuno referido","Antes de alimento","Después de alimento","Medición aleatoria","No se conoce"),"La glucosa capilar aislada debe interpretarse según contexto, síntomas y antecedentes; la app no diagnostica diabetes con una medición aislada.")}
  }
  if(section=="Inspección general"){
   item{Pick("Edad aparente",listOf("Acorde con edad cronológica","Aparenta menor edad","Aparenta mayor edad","No valorable"))}
   item{Pick("Marcha",listOf("Sin alteración aparente","Con apoyo","Claudicante","Inestable","No deambula","No valorable"))}
   item{Pick("Actitud / postura",listOf("Libremente escogida","Postura antálgica referida/observada","Limitada","Otra alteración observable","No valorable"))}
   item{Pick("Constitución / habitus",listOf("Sin particularidades aparentes","Delgado","Robusto","Otra constitución observable","No valorable"))}
   item{Pick("Movimientos anormales",listOf("No observados","Temblor","Tics","Movimientos involuntarios","Otro","No valorable"))}
   item{Pick("Estado de conciencia",listOf("Alerta","Somnoliento","Confuso/desorientado","Respuesta disminuida","No valorable"))}
   item{Pick("Actitud durante la consulta",listOf("Cooperador","Ansioso","Temeroso","Poco cooperador","No valorable"))}
   item{Pick("Cuidado personal",listOf("Adecuado","Regular","Deficiente aparente","No valorable"))}
  }

  if(section=="Cráneo y cara"){
   item{Pick("Forma craneal",listOf("Dolicocefálico","Mesocéfalo","Braquicéfalo","No valorable"),"Dolicocefálico: cráneo relativamente largo y estrecho. Mesocéfalo: proporciones intermedias. Braquicéfalo: cráneo relativamente corto y ancho.")}
   item{Pick("Patrón facial",listOf("Dolicofacial","Mesofacial","Braquifacial","No valorable"),"Dolicofacial: cara relativamente larga y estrecha. Mesofacial: proporción intermedia. Braquifacial: cara relativamente corta y ancha.")}
   item{Pick("Simetría facial",listOf("Simétrica aparente","Asimetría derecha","Asimetría izquierda","Asimetría compleja","No valorable"))}
   item{ClinicalPhotoV38("Referencia clínica real · asimetría de expresión facial","https://commons.wikimedia.org/wiki/Special:Redirect/file/Bellspalsy.JPG","James Heilman, MD · Wikimedia Commons · licencia abierta; publicación con consentimiento declarado por el autor.")}
  }
  if(section=="Músculos"){
   item{Pick("Expresión facial · inspección",listOf("Simetría conservada","Asimetría al sonreír","Asimetría al fruncir ceño","Asimetría al cerrar ojos","Asimetría al inflar mejillas","Debilidad aparente","No valorable"),"Evaluar en reposo y pedir elevar cejas/fruncir ceño, cerrar ojos, sonreír/mostrar dientes e inflar mejillas. Comparar ambos lados.")}
   item{ClinicalPhotoV38("Referencia clínica real · evaluación de sonrisa en parálisis facial","https://commons.wikimedia.org/wiki/Special:Redirect/file/Bell%27s_Palsy_smiling.jpg","Shantoo · Wikimedia Commons · CC0; uso educativo para ilustrar parálisis facial.")}
   item{Pick("Músculos de la expresión · tono/función",listOf("Función aparentemente conservada","Hipotonía aparente","Hipertonía aparente","Movimiento involuntario","Dolor referido","No valorable"))}
   item{Pick("Temporal",listOf("Sin dolor","Dolor derecho","Dolor izquierdo","Dolor bilateral","Hipertrofia/asimetría","No valorable"))}
   item{Pick("Masetero",listOf("Sin dolor","Dolor derecho","Dolor izquierdo","Dolor bilateral","Hipertrofia/asimetría","No valorable"))}
   item{Pick("Pterigoideos / función clínica",listOf("Sin hallazgos aparentes","Dolor reproducible en maniobra","Limitación funcional","No valorable"),"Correlacionar palpación accesible y movimientos contra resistencia; no atribuir dolor inespecífico a un músculo profundo sin sustento clínico.")}
  }
  if(section=="Cuello"){
   item{Pick("Simetría del cuello",listOf("Simétrico aparente","Asimetría derecha","Asimetría izquierda","Aumento de volumen localizado","No valorable"))}
   item{Pick("Movilidad cervical",listOf("Conservada","Limitada a derecha","Limitada a izquierda","Limitada en flexión/extensión","Limitación global","Dolorosa","No valorable"),"Observar flexión, extensión y rotación sin forzar movimientos dolorosos.")}
   item{Pick("Dolor a exploración",listOf("Sin dolor","Derecho","Izquierdo","Bilateral","Localizado anterior","Localizado posterior","No valorable"))}
   item{Pick("Masas / aumento de volumen",listOf("No observado/palpado","Anterior","Lateral derecho","Lateral izquierdo","Posterior","Difuso","No valorable"),"Un aumento de volumen requiere descripción clínica y valoración; la app no asigna etiología.")}
   item{Pick("Tiroides · hallazgo clínico",listOf("Sin aumento aparente","Aumento aparente","Asimetría aparente","Nódulo/masa referida o palpable","Antecedente tiroideo sin hallazgo visible","No valorable"),"Registrar sólo el hallazgo o antecedente; no diagnosticar enfermedad tiroidea por inspección/palpación aislada.")}
  }
  if(section=="Ganglios"){
   item{Pick("Cadena ganglionar",listOf("Preauriculares","Mastoideos/postauriculares","Occipitales","Submentonianos","Submandibulares","Cervicales superficiales/anterior","Cervicales profundos","Cervicales posteriores","Supraclaviculares"))}
   item{ClinicalPhotoV38("Referencia clínica real · linfadenopatía cervical","https://commons.wikimedia.org/wiki/Special:Redirect/file/Cervical_lymphadenopathy.jpg","Whispyhistory · Wikimedia Commons · CC0.")}
   item{Pick("Palpabilidad",listOf("No palpable","Palpable","No valorable"))}
   item{Pick("Movilidad",listOf("Móvil","Fijo/adherido aparente","No aplica/no palpable","No valorable"))}
   item{Pick("Dolor",listOf("No doloroso","Doloroso","No aplica/no palpable","No valorable"))}
   item{Pick("Lateralidad ganglionar",listOf("Derecha","Izquierda","Bilateral","No aplica/no palpable","No valorable"))}
   item{Pick("Tamaño aproximado",listOf("<0.5 cm","0.5–0.9 cm","1.0–1.9 cm","≥2 cm","No aplica/no palpable","No medido"))}
   item{Pick("Consistencia",listOf("Blanda","Elástica","Firme","Dura","No aplica/no palpable","No valorable"),"Registrar sitio, lateralidad y tamaño. Un ganglio palpable no establece por sí solo una etiología.")}
  }
  if(section=="ATM y dimensión vertical"){
   item{ClinicalPhotoV38("Referencia clínica real · región de cabeza y cuello","https://commons.wikimedia.org/wiki/Special:Redirect/file/Cervical_lymphadenopathy_right_neck.png","Coronation Dental Specialty Group · Wikimedia Commons · CC BY-SA 4.0. Imagen de aumento cervical; no representa un diagnóstico de ATM.")}
   item{Pick("ATM · dolor",listOf("Sin dolor","Derecha","Izquierda","Bilateral","No valorable"))}
   item{Pick("Dolor durante movimiento",listOf("No","En apertura","En cierre","En lateralidad derecha","En lateralidad izquierda","En protrusión","En retrusión","En varios movimientos","No valorable"))}
   item{Pick("ATM · sonido",listOf("Sin sonido detectable","Click/chasquido derecho","Click/chasquido izquierdo","Click bilateral","Crepitación derecha","Crepitación izquierda","Crepitación bilateral","Otro/no valorable"),"Palpar región preauricular durante apertura, cierre y excursiones. Registrar el sonido sin generar diagnóstico automático.")}
   item{Pick("Palpación ATM",listOf("Sin dolor","Dolor polo lateral derecho","Dolor polo lateral izquierdo","Dolor bilateral","No valorable"))}
   item{Pick("Línea media al abrir/cerrar",listOf("Recta/centrada","Desviación derecha con retorno","Desviación izquierda con retorno","Deflexión persistente derecha","Deflexión persistente izquierda","Trayectoria irregular","No valorable"))}
   item{Pick("Apertura máxima interincisal",listOf("<25 mm","25–34 mm","35–39 mm","40–44 mm","45–55 mm","56–60 mm",">60 mm","No medida"),"Referencia adulta: se reportan rangos frecuentes alrededor de 42–55 mm; existe variación por edad, sexo y anatomía.")}
   item{Pick("Lateralidad derecha",listOf("<4 mm","4–6 mm","7–9 mm","10–12 mm",">12 mm","No medida"),"7 mm o más se usa como referencia clínica funcional mínima; individualizar.")}
   item{Pick("Lateralidad izquierda",listOf("<4 mm","4–6 mm","7–9 mm","10–12 mm",">12 mm","No medida"),"Comparar ambos lados y registrar dolor, click o limitación.")}
   item{Pick("Protrusión",listOf("<4 mm","4–5 mm","6–9 mm","10–12 mm",">12 mm","No medida"),"6 mm se usa como referencia clínica funcional mínima; individualizar.")}
   item{Pick("Retrusión",listOf("<1 mm","1–2 mm","3–4 mm",">4 mm","No medida"),"Registrar el desplazamiento medido; las referencias clínicas son menos uniformes para retrusión.")}
   item{Pick("Limitación de movimiento",listOf("No aparente","Apertura","Cierre","Lateralidad derecha","Lateralidad izquierda","Protrusión","Retrusión","Varios movimientos","No valorable"))}
   item{Pick("DVR · distancia medida",listOf("<50 mm","50–54 mm","55–59 mm","60–64 mm","65–69 mm","70–74 mm","≥75 mm","No medida"),"Medir entre dos puntos faciales reproducibles con mandíbula en reposo fisiológico.")}
   item{Pick("DVO · distancia medida",listOf("<50 mm","50–54 mm","55–59 mm","60–64 mm","65–69 mm","70–74 mm","≥75 mm","No medida"),"Medir entre los mismos puntos con dientes en oclusión habitual. No existe una DVO universal en milímetros: depende de los puntos elegidos y de la anatomía individual.")}
   item{SectionCard("Espacio interoclusal"){Text("Espacio interoclusal = DVR − DVO. Referencia habitual: 2–4 mm, con variación individual.",fontWeight=FontWeight.Bold);listOf("DVR − DVO <2 mm","DVR − DVO 2–4 mm","DVR − DVO >4 mm","No calculado").forEach{v->FilterChip(selected["Espacio interoclusal"]==v,{selected["Espacio interoclusal"]=v},{Text(v)},modifier=Modifier.fillMaxWidth())}}}
  }
  item{Button(onClick={},modifier=Modifier.fillMaxWidth()){Text("💾 Guardar exploración")}}
  item{NoticeCard("Los valores de referencia son educativos y deben interpretarse con edad, sexo, anatomía, síntomas, técnica de medición y contexto clínico. Los hallazgos no generan un diagnóstico automático.")}
 }
}

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

