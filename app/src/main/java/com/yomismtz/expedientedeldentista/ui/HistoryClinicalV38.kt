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
 var reason by remember{mutableStateOf("")}; var openSymptoms by remember{mutableStateOf(false)}
 val examples=listOf("“Tiene un hoyo en el diente” → conservar la frase y, después de explorar, describir la lesión y diagnóstico confirmado.","“Le duele el diente” → caracterizar dolor y pruebas; el dolor por sí solo no define diagnóstico pulpar.","“Le sale agua blanca / pus del diente” → registrar secreción; buscar trayecto fistuloso, inflamación y origen antes de diagnosticar.","“Se le rompió el diente” → precisar traumatismo/fractura, tejidos comprometidos, tiempo y síntomas.")
 val symptoms=listOf("Dolor: inicio, espontáneo/provocado, intensidad y duración","Localización e irradiación","Frío, calor, dulce y masticación","Dolor nocturno","Inflamación / aumento de volumen","Secreción, pus o fístula","Sangrado","Movilidad","Traumatismo","Fiebre o malestar referido")
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Motivo de consulta y padecimiento actual",onBack,"Primero conserva las palabras del paciente o acompañante; después construye el padecimiento actual con lenguaje clínico y hallazgos comprobados.")}
  item{SectionCard("1 · Motivo de consulta literal"){OutlinedTextField(reason,{reason=it.take(250)},modifier=Modifier.fillMaxWidth(),label={Text("Escribe tal cual lo dijo el paciente, mamá/papá o acompañante")},minLines=3);Text("No sustituyas aquí las palabras originales por un diagnóstico.",style=MaterialTheme.typography.bodySmall)}}
  item{SectionCard("2 · Traducción clínica educativa"){examples.forEach{Text("• $it")}}}
  item{Card(onClick={openSymptoms=!openSymptoms},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp)){Text("3 · Signos y síntomas",fontWeight=FontWeight.Bold);Text(if(openSymptoms)"Toca para cerrar" else "Toca para abrir interrogatorio")}}}
  if(openSymptoms)items(symptoms.size){i->Card(Modifier.fillMaxWidth()){Text("□ "+symptoms[i],Modifier.padding(13.dp))}}
  item{NoticeCard("El padecimiento actual se redacta con inicio, evolución, localización, características, factores desencadenantes/atenuantes y hallazgos relacionados. La orientación no convierte automáticamente una frase coloquial en diagnóstico definitivo.")}
 }
}

@Composable fun HistoryHereditaryV38(lang:String,onBack:()->Unit){
 val relatives=listOf("Madre","Padre","Hermanas/os","Hijas/os","Abuelas/os maternos","Abuelas/os paternos","Tías/os maternos","Tías/os paternos")
 val diseases=listOf("Diabetes mellitus","Hipertensión arterial","Cardiopatías","Infarto / evento vascular","Enfermedad renal","Enfermedad hepática","Asma / enfermedad pulmonar","Alergias","Epilepsia / neurológicas","Trastornos hematológicos","Cáncer / neoplasias","Enfermedades autoinmunes","Osteoporosis / enfermedad ósea","Trastornos tiroideos/endocrinos","Tuberculosis / enfermedades fímicas","VIH","Hepatitis","Infecciones de transmisión sexual","Alteraciones congénitas o hereditarias","Otras")
 var relative by remember{mutableStateOf(0)};val selected=remember{mutableStateMapOf<String,Boolean>()}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes heredo-familiares",onBack,"Elige primero el familiar y después marca sus antecedentes. Repite el proceso para construir el registro familiar completo.")}
  item{SectionCard("1 · Familiar"){relatives.forEachIndexed{i,x->FilterChip(relative==i,{relative=i},{Text(x)},modifier=Modifier.fillMaxWidth())}}}
  item{Text("2 · Enfermedades de "+relatives[relative],fontWeight=FontWeight.Bold)}
  items(diseases.size){i->val key=relatives[relative]+"|"+diseases[i];Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){Checkbox(selected[key]==true,{selected[key]=it});Text(diseases[i],Modifier.weight(1f))}}
  item{SectionCard("3 · Qué ampliar ante un positivo"){Text("Registra parentesco, enfermedad específica, edad aproximada de inicio, tratamiento/complicaciones y estado: vive, controlado/en tratamiento, curado cuando corresponda o falleció.")}}
  item{SectionCard("Resumen familiar"){val positives=selected.filterValues{it}.keys;if(positives.isEmpty())Text("Aún no hay antecedentes marcados.") else positives.forEach{Text("• "+it.replace("|"," · "))}}}
 }
}

@Composable fun HistoryNonPathV38(lang:String,onBack:()->Unit)=explain("Antecedentes personales no patológicos",listOf(
 E("Habitación y servicios","Material de vivienda/piso; habitantes y habitaciones; agua potable, drenaje/saneamiento, electricidad, gas, ventilación y recolección de basura. Contextualiza exposición ambiental, higiene y posibilidades de autocuidado."),
 E("Higiene bucal","Cepillado: 0, 1, 2, 3 o más veces al día; técnica, pasta fluorada, hilo/interdental y supervisión en niños. Explica por qué la frecuencia debe correlacionarse con técnica y hallazgos."),
 E("Higiene general","Baño y aseo: frecuencia semanal o diaria según el caso, acceso a agua y condiciones. Es contexto clínico y no debe usarse para juzgar al paciente."),
 E("Alimentación","Frecuencia de carne/proteínas, frutas, verduras, cereales/carbohidratos, azúcares, refrescos/bebidas azucaradas y embutidos. Importa especialmente la frecuencia de exposiciones cariogénicas."),
 E("Inmunizaciones","Vacunas referidas y fecha/dosis cuando se conozca: BCG, hepatitis B, hexavalente/pentavalente según esquema, rotavirus, neumococo, influenza, SRP, DPT/Td/Tdap, VPH, COVID-19 y otras por edad/riesgo. Verificar cartilla cuando se necesite certeza; el esquema depende de país, edad y año."),
 E("Tabaco","Tipo, consumo actual, cantidad/frecuencia, años y exposición pasiva. Importa por mucosa, periodonto, cicatrización y riesgo sistémico."),
 E("Alcohol","Tipo, cantidad y frecuencia. Puede modificar riesgo sistémico, interacciones y seguridad de tratamientos."),
 E("Drogas / otras sustancias","Sustancia, vía, frecuencia, última exposición referida y tratamiento si existe. Importa por interacciones, signos vitales, anestesia y seguridad."),
 E("Perforaciones, piercing y tatuajes","Sitio, antigüedad, complicaciones/infecciones y condiciones de realización. En piercing oral observar trauma dentario/gingival.")
),onBack)

@Composable fun HistoryGynecoV38(lang:String,onBack:()->Unit){
 var sex by remember{mutableStateOf("")}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Antecedentes gineco-obstétricos",onBack,"Selecciona sexo para determinar si el apartado aplica.")}
  item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(sex=="H",{sex="H"},{Text("Hombre")});FilterChip(sex=="M",{sex="M"},{Text("Mujer")})}}
  if(sex=="H")item{Card{Text("No aplica.",Modifier.padding(16.dp),fontWeight=FontWeight.Bold)}}
  if(sex=="M"){val q=listOf("Menarca","Inicio de vida sexual activa","Número de embarazos","Partos y cesáreas","Abortos y antecedentes relevantes","Fecha de última menstruación / posibilidad de embarazo","Anticonceptivos, lactancia y menopausia cuando correspondan");items(q.size){i->Card(Modifier.fillMaxWidth()){Text(q[i],Modifier.padding(14.dp))}}}
 }
}
@Composable fun HistorySurgicalTraumaV38(lang:String,onBack:()->Unit)=explain("Antecedentes quirúrgicos y traumáticos",listOf(E("Cirugías y hospitalizaciones","Pregunta motivo, fecha, complicaciones y secuelas."),E("Transfusiones","Registra motivo, fecha aproximada y reacciones."),E("Traumatismos, fracturas y luxaciones","Cabeza, cuello y maxilares pueden modificar anatomía, oclusión y ATM.")),onBack)
@Composable fun HistoryPhysicalV38(lang:String,onBack:()->Unit)=explain("Exploración física",listOf(E("Cráneo","Observa forma, simetría, lesiones, deformidades y dolor."),E("Facies","Evalúa simetría, proporciones, volumen, coloración y movimientos."),E("Músculos","Explora expresión y masticación según técnica clínica."),E("Cuello","Inspecciona y palpa movilidad, masas y dolor."),E("Cadenas ganglionares","Valora localización, tamaño, consistencia, movilidad y dolor."),E("ATM","Explora apertura, trayectoria, dolor y ruidos, correlacionando músculos y oclusión.")),onBack)
@Composable fun HistoryOrthoV38(lang:String,onBack:()->Unit)=explain("Antecedentes de tratamientos ortodónticos",listOf(E("Tratamiento previo","Pregunta tipo de aparato, edad, duración, extracciones, retención, motivo de suspensión y resultado referido.")),onBack)
@Composable fun HistoryDentalAlterationsV38(lang:String,onBack:()->Unit)=explain("Alteraciones de órganos dentarios",listOf(E("Número","Ausencias, supernumerarios y alteraciones de erupción."),E("Forma y tamaño","Micro/macrodoncia, fusión, geminación y otras variaciones."),E("Estructura y color","Cambios de esmalte, dentina o coloración requieren diagnóstico diferencial."),E("Erupción","Distingue ectópica/atípica, prematura y retardada según edad y secuencia.")),onBack)
@Composable fun HistoryHabitsV38(lang:String,onBack:()->Unit)=explain("Hábitos y parafunciones",listOf(E("Succión digital","Pregunta dedo, frecuencia, duración e intensidad. Extraoral: postura labial/facial. Intraoral: incisivos, overjet, mordida abierta, arco y paladar."),E("Chupón o mamila prolongados","Registra edad y patrón. Extraoral: postura labial. Intraoral: mordida, arco y erupción."),E("Respiración oral","Pregunta respiración y sueño. Extraoral: labios entreabiertos/postura. Intraoral: sequedad, gingivitis y patrón oclusal; la causa respiratoria debe investigarse."),E("Interposición lingual / deglución atípica","Observa deglución, lengua y competencia labial; intraoralmente puede acompañarse de mordida abierta o espacios."),E("Onicofagia / mordisqueo","Extraoral: uñas/labios. Intraoral: desgaste, fracturas pequeñas, trauma mucoso o recesión localizada."),E("Bruxismo y apretamiento","Pregunta sueño/vigilia, fatiga y dolor. Extraoral: músculos. Intraoral: facetas, fracturas, restauraciones dañadas y línea alba; ningún signo aislado confirma el diagnóstico.")),onBack)
@Composable fun HistoryOralExamV38(lang:String,onBack:()->Unit)=explain("Examen peribucal e intrabucal",listOf(E("Piel, labios y comisuras","Inspecciona color, hidratación, integridad, lesiones y simetría."),E("Carrillos y mucosa","Inspecciona bilateralmente y describe lesiones por sitio, tamaño, color y superficie."),E("Paladar y orofaringe","Observa paladar duro/blando, úvula y orofaringe."),E("Lengua y piso de boca","Examina dorso, bordes, cara ventral y piso mediante inspección y palpación cuando corresponda.")),onBack)
