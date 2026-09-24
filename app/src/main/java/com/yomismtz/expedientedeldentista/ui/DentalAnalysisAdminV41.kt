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
  }
  return
 }
 val items=listOf(
  Triple("odontogram","Odontograma","Registro clínico por órgano dentario y superficies."),
  Triple("cpod","CPOD / ceod","Índice de experiencia de caries en dentición permanente y temporal."),
  Triple("ipc","IPC","Índice periodontal comunitario."),
  Triple("ihos","IHOS","Índice de higiene oral simplificado."),
  Triple("oleary","O’Leary","Control de placa por superficies dentales."),
  Triple("icdas","ICDAS","Registro visual estandarizado de lesiones de caries.")
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
@Composable fun TreatmentRequestTeachingV41(lang:String,onBack:()->Unit)=ExplainedAdministrativeSheet("Solicitud de tratamiento","Cada apartado explica qué debe registrar el alumno y por qué es necesario para una solicitud clara.",listOf(
 ExplainField("Servicios solicitados","Define exactamente qué atención, estudio o intervención se está solicitando y evita referencias ambiguas.","valoración endodóntica; extracción; rehabilitación protésica; estudio radiográfico; valoración periodontal."),
 ExplainField("Motivo","Relaciona la solicitud con el problema clínico que la origina.","dolor persistente; lesión cariosa profunda; diente no restaurable; necesidad de valoración especializada."),
 ExplainField("Área u órgano dentario","Evita confundir el sitio y permite relacionar solicitud, diagnóstico e imagen.","OD 36; región posterior superior derecha; ATM derecha; mucosa de carrillo izquierdo."),
 ExplainField("Prioridad","Permite distinguir atención inmediata/urgente de la programable según el escenario clínico.","urgente; prioritaria; programable; control."),
 ExplainField("Responsable","Identifica quién solicita o realiza el procedimiento dentro del flujo clínico.","alumno responsable; operador; servicio receptor."),
 ExplainField("Supervisión","Documenta al docente/profesional que revisa o autoriza la actividad cuando corresponde.","docente supervisor; firma/autorización institucional; servicio responsable.")
),onBack)
@Composable fun BudgetTeachingV41(lang:String,onBack:()->Unit)=ExplainedAdministrativeSheet("Presupuesto","Estructura educativa del presupuesto. No registra cobros reales.",listOf(
 ExplainField("Procedimiento","Relaciona el costo con una actividad identificable del plan.","resina; corona; prótesis; estudio auxiliar."),
 ExplainField("Cantidad","Indica cuántas unidades del procedimiento o material se consideran.","1 corona; 2 restauraciones; 1 estudio."),
 ExplainField("Costo unitario","Permite conocer el importe de una unidad antes de multiplicarlo por la cantidad.","importe institucional por una restauración o procedimiento."),
 ExplainField("Subtotal","Muestra el resultado de cantidad × costo unitario por concepto.","2 × costo unitario = subtotal del concepto."),
 ExplainField("Laboratorio cuando proceda","Separa costos externos o de laboratorio cuando el procedimiento los requiere.","corona, prótesis removible, aparato, férula."),
 ExplainField("Total","Suma los subtotales y conceptos aplicables para presentar el importe global de forma comprensible.","suma final del plan presupuestado.")
),onBack)
