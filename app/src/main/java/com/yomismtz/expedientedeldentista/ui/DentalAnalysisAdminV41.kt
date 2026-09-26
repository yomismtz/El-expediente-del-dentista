package com.yomismtz.expedientedeldentista.ui
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

@Composable fun DentalAnalysisHubV41(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onBack:()->Unit){
 var page by remember{mutableStateOf<String?>(null)}
 BackHandler(enabled=page!=null){page=null}
 if(page!=null){
  val back={page=null}
  when(page){
   "odontogram"->OdontogramV20Screen(lang,session,onSessionChanged,back)
   "cpod"->CpodInteractiveV19Screen(lang,session,onSessionChanged,back)
   "ipc"->IpcResponsiveV17Screen(lang,session,onSessionChanged,back)
   "ihos"->IhosResponsiveV17Screen(lang,session,onSessionChanged,back)
   "oleary"->OlearyScreen(lang,session,onSessionChanged,back)
   "icdas"->IcdasScreen(lang,session,onSessionChanged,back)
   "pulpal"->PulpalPeriapicalInteractiveV2Screen(lang,session,onSessionChanged,{page="endo"},back)
   "endo"->EndodonticInteractiveV2Screen(lang,session,{page="pulpal"},{page="pulpal"},back)
  }
  return
 }
 val items=listOf(
  Triple("odontogram","Odontograma","Registro clínico por órgano dentario y superficies."),
  Triple("cpod","CPOD / ceod","Índice de experiencia de caries en dentición permanente y temporal."),
  Triple("ipc","IPC","Índice periodontal comunitario."),
  Triple("ihos","IHOS","Índice de higiene oral simplificado."),
  Triple("oleary","O’Leary","Control de placa por superficies dentales."),
  Triple("icdas","ICDAS","Registro visual estandarizado de lesiones de caries."),
  Triple("pulpal","Análisis pulpar y periapical","Pruebas y hallazgos para integrar el diagnóstico pulpar y periapical.")
 )
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Análisis dentales",onBack,"Selecciona el análisis que deseas realizar. Todos quedan concentrados dentro de este módulo.")}
  items(items.size){i->val x=items[i];Card(onClick={page=x.first},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(15.dp)){Text(x.second,fontWeight=FontWeight.Bold);Text(x.third)}}}
 }
}

private data class ExplainField(val n:String,val why:String,val examples:String)
@Composable private fun ExplainedAdministrativeSheet(title:String,intro:String,fields:List<ExplainField>,onBack:()->Unit){
 var open by remember{mutableStateOf<Int?>(null)}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader(title,onBack,intro)}
  items(fields.size){i->val f=fields[i];Card(onClick={open=if(open==i)null else i},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){Text(f.n,fontWeight=FontWeight.Bold);if(open==i){Text("Por qué importa: "+f.why);Text("Ejemplos: "+f.examples)}else Text("Toca para ver explicación y ejemplos",style=MaterialTheme.typography.bodySmall)}}}
 }
}
@Composable fun TreatmentRequestTeachingV41(lang:String,onBack:()->Unit){
 var service by rememberRecordState("request.service","Valoración")
 var site by rememberRecordState("request.site","Órgano dentario")
 var priority by rememberRecordState("request.priority","Programable")
 var reason by rememberRecordState("request.reason","Dolor / síntomas")
 var destination by rememberRecordState("request.destination","Clínica integral")
 val services=listOf("Valoración","Estudio radiográfico","Endodoncia","Periodoncia","Cirugía / extracción","Restauración","Prótesis","Ortodoncia","Medicina bucal")
 val sites=listOf("Órgano dentario","Cuadrante","Arcada","Región periapical","Periodonto","Mucosa oral","ATM","Maxilar / mandíbula")
 val priorities=listOf("Urgente","Prioritaria","Programable","Control")
 val reasons=listOf("Dolor / síntomas","Hallazgo clínico","Hallazgo radiográfico","Lesión de tejidos blandos","Pérdida dental","Alteración periodontal","Alteración oclusal","Continuidad de tratamiento")
 val destinations=listOf("Clínica integral","Endodoncia","Periodoncia","Cirugía","Prótesis","Ortodoncia","Radiología","Patología / medicina bucal")
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Solicitud de tratamiento",onBack,"Selecciona los datos; la app construye una solicitud educativa sin redacción libre.")}
  item{SectionCard("1 · Servicio solicitado"){services.forEach{FilterChip(service==it,{service=it},{Text(it)},Modifier.fillMaxWidth())}}}
  item{SectionCard("2 · Sitio o estructura"){sites.forEach{FilterChip(site==it,{site=it},{Text(it)},Modifier.fillMaxWidth())}}}
  item{SectionCard("3 · Motivo"){reasons.forEach{FilterChip(reason==it,{reason=it},{Text(it)},Modifier.fillMaxWidth())}}}
  item{SectionCard("4 · Prioridad"){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(5.dp)){priorities.forEach{FilterChip(priority==it,{priority=it},{Text(it)},Modifier.weight(1f))}}}}
  item{SectionCard("5 · Servicio receptor"){destinations.forEach{FilterChip(destination==it,{destination=it},{Text(it)},Modifier.fillMaxWidth())}}}
  item{SectionCard("Solicitud generada"){Text("Se solicita $service para $site por $reason. Prioridad: $priority. Servicio receptor: $destination. La autorización y supervisión deberán documentarse conforme al formato institucional.",fontWeight=FontWeight.Bold)}}
  item{NoticeCard("La solicitud no sustituye diagnóstico, consentimiento, indicación clínica ni autorización del docente/profesional responsable.")}
 }
}

@Composable fun BudgetTeachingV41(lang:String,onBack:()->Unit){
 var procedure by rememberRecordState("budget.procedure","Valoración")
 var qty by rememberRecordState("budget.qty",1)
 var costBand by rememberRecordState("budget.costBand","Por cotizar")
 var lab by rememberRecordState("budget.lab","No aplica")
 var status by rememberRecordState("budget.status","Borrador")
 val procedures=listOf("Valoración","Radiografía / imagen","Profilaxis","Restauración","Endodoncia","Extracción","Corona","Prótesis removible","Férula / aparato","Otro concepto institucional")
 val procedureDescriptions=mapOf(
  "Valoración" to "Consulta de evaluación clínica para integrar antecedentes, exploración, hallazgos y necesidades de atención.",
  "Radiografía / imagen" to "Estudio de imagen indicado como auxiliar diagnóstico; el tipo debe corresponder a la necesidad clínica.",
  "Profilaxis" to "Procedimiento preventivo de remoción de biofilm y depósitos supragingivales según valoración.",
  "Restauración" to "Tratamiento restaurador de un diente; material, superficies y extensión dependen del diagnóstico y plan autorizado.",
  "Endodoncia" to "Tratamiento del sistema de conductos radiculares cuando existe una indicación endodóntica confirmada.",
  "Extracción" to "Remoción de un órgano dentario cuando está clínicamente indicada y autorizada.",
  "Corona" to "Restauración de cobertura coronaria; requiere valoración del diente, soporte y plan protésico.",
  "Prótesis removible" to "Rehabilitación protésica removible para sustituir dientes ausentes según diseño y diagnóstico.",
  "Férula / aparato" to "Dispositivo indicado para una finalidad clínica específica; su diseño depende del diagnóstico.",
  "Otro concepto institucional" to "Concepto autorizado por la institución que no corresponde a las categorías anteriores."
 )
 val costs=listOf("Por cotizar","Tarifa institucional baja","Tarifa institucional media","Tarifa institucional alta")
 val labs=listOf("No aplica","Incluido","Laboratorio por separado","Pendiente de cotización")
 val states=listOf("Borrador","Explicado al paciente","Pendiente de autorización","Autorizado","Requiere actualización")
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Presupuesto",onBack,"Ejercicio educativo sin cobros reales. Todo se selecciona mediante opciones predefinidas.")}
  item{SectionCard("1 · Procedimiento"){
   ChipChoices(procedures.map{x->x to (procedure==x)},{i->procedure=procedures[i]},columns=3)
   Spacer(Modifier.height(8.dp))
   Text(procedureDescriptions[procedure]?:"",style=MaterialTheme.typography.bodyMedium)
  }}
  item{SectionCard("2 · Cantidad"){Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){(1..5).forEach{FilterChip(qty==it,{qty=it},{Text(it.toString())})}}}}
  item{SectionCard("3 · Costo institucional"){costs.forEach{FilterChip(costBand==it,{costBand=it},{Text(it)},Modifier.fillMaxWidth())};Text("La app no inventa precios: el importe monetario debe provenir del tarifario autorizado de la institución.")}}
  item{SectionCard("4 · Laboratorio"){labs.forEach{FilterChip(lab==it,{lab=it},{Text(it)},Modifier.fillMaxWidth())}}}
  item{SectionCard("5 · Estado"){states.forEach{FilterChip(status==it,{status=it},{Text(it)},Modifier.fillMaxWidth())}}}
  item{SectionCard("Resumen"){Text("$qty × $procedure · Costo: $costBand · Laboratorio: $lab · Estado: $status",fontWeight=FontWeight.Bold)}}
  item{SectionCard("6 · Comprobación antes de entregar"){
    listOf(
      "El procedimiento coincide con el plan de tratamiento autorizado.",
      "La cantidad corresponde a las unidades realmente presupuestadas.",
      "La tarifa proviene del tabulador institucional vigente.",
      "Los costos de laboratorio están identificados por separado cuando corresponde.",
      "El presupuesto fue explicado antes de solicitar aceptación.",
      "Cualquier cambio posterior requiere actualizar el presupuesto."
    ).forEach{Text("✓ $it")}
  }}
  item{SectionCard("7 · Estado administrativo"){
    Text("Registro generado: $procedure · cantidad $qty · $costBand · laboratorio: $lab · $status.",fontWeight=FontWeight.Bold)
    Text("La aceptación económica no equivale al consentimiento informado del procedimiento clínico.")
  }}
  item{NoticeCard("Presupuesto educativo. El total monetario real debe calcularse con precios institucionales vigentes y conceptos efectivamente autorizados. No se registran cobros ni datos bancarios en este módulo.")}
 }
}
