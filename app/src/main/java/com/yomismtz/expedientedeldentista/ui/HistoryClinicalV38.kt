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
@Composable fun HistoryReasonV38(lang:String,onBack:()->Unit)=explain("Motivo de consulta y padecimiento actual",listOf(E("Motivo de consulta","Se registra preferentemente con las palabras del paciente para conservar su prioridad."),E("Inicio y evolución","Pregunta cuándo inició, forma de inicio y cambios en el tiempo."),E("Signos y síntomas","Caracteriza dolor, localización, duración, estímulos y manifestaciones asociadas.")),onBack)
@Composable fun HistoryHereditaryV38(lang:String,onBack:()->Unit)=explain("Antecedentes heredo-familiares",listOf(E("Diabetes","Pregunta familiar afectado, tipo, edad de inicio y complicaciones."),E("Hipertensión y cardiovascular","Aclara parentesco, eventos y edad de aparición."),E("Cáncer y enfermedades hereditarias","Especifica tipo, parentesco y edad; evita registros vagos.")),onBack)
@Composable fun HistoryNonPathV38(lang:String,onBack:()->Unit)=explain("Antecedentes personales no patológicos",listOf(E("Habitación y servicios","Contextualiza ambiente, agua y saneamiento."),E("Higiene general y bucal","Pregunta frecuencia, técnica y auxiliares; correlaciona con hallazgos clínicos."),E("Alimentación","Pregunta patrón y frecuencia de azúcares/bebidas para valorar riesgo."),E("Inmunizaciones","Registra esquema referido y verifica documento cuando se requiera certeza."),E("Tabaco, alcohol y otras sustancias","Pregunta tipo, cantidad, frecuencia y tiempo de exposición.")),onBack)
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
