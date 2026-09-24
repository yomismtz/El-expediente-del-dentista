package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ColloquialDentalPhrase(val id:Int,val phrase:String,val clinical:List<String>)

private val colloquialDentalPhrasesV42=listOf(
 ColloquialDentalPhrase(1,"Me duele una muela",listOf("Dolor dental referido","Odontalgia referida por el paciente","Sintomatología dolorosa localizada en órgano dentario por determinar")),
 ColloquialDentalPhrase(2,"Tengo una muela picada",listOf("Lesión cariosa referida por el paciente","Pérdida de estructura dental compatible con caries por valorar","Órgano dentario con posible lesión de caries pendiente de exploración")),
 ColloquialDentalPhrase(3,"Tengo un hoyo en el diente",listOf("Cavidad dental referida","Pérdida localizada de estructura coronaria por valorar","Lesión cavitada de etiología por determinar")),
 ColloquialDentalPhrase(4,"Me duele el nervio",listOf("Dolor de probable origen pulpar por valorar","Sintomatología dolorosa dental con compromiso pulpar por determinar","Dolor referido al interior del órgano dentario pendiente de pruebas pulpares")),
 ColloquialDentalPhrase(5,"Me sale agua blanca de la encía",listOf("Secreción blanquecina referida a nivel gingival","Exudado referido por el paciente","Secreción asociada a lesión o trayecto fistuloso por confirmar")),
 ColloquialDentalPhrase(6,"Me sale pus",listOf("Secreción purulenta referida","Exudado de aspecto purulento por confirmar clínicamente","Drenaje de secreción asociado a proceso inflamatorio/infeccioso por valorar")),
 ColloquialDentalPhrase(7,"Me salió una bolita en la encía",listOf("Aumento de volumen gingival localizado","Lesión nodular o elevada en encía por describir","Posible trayecto fistuloso o lesión gingival por confirmar")),
 ColloquialDentalPhrase(8,"Se me hinchó la cara",listOf("Aumento de volumen facial referido","Edema o tumefacción facial por valorar","Asimetría facial de aparición reciente asociada a aumento de volumen")),
 ColloquialDentalPhrase(9,"Tengo la encía inflamada",listOf("Aumento de volumen y/o eritema gingival referido","Signos inflamatorios gingivales por valorar","Alteración gingival compatible con inflamación clínica por confirmar")),
 ColloquialDentalPhrase(10,"Me sangran las encías",listOf("Sangrado gingival referido","Hemorragia gingival espontánea o provocada por precisar","Sangrado al cepillado o espontáneo pendiente de exploración periodontal")),
 ColloquialDentalPhrase(11,"Se me aflojó el diente",listOf("Movilidad dental referida","Aumento de movilidad del órgano dentario por valorar","Movilidad dentaria de etiología por determinar")),
 ColloquialDentalPhrase(12,"Se me quebró el diente",listOf("Fractura dental referida","Pérdida traumática o estructural de tejido dentario por valorar","Fractura coronaria/corono-radicular por determinar")),
 ColloquialDentalPhrase(13,"Se me cayó un pedazo de muela",listOf("Pérdida de estructura dental referida","Fractura o desprendimiento de tejido/restauración por valorar","Defecto coronario reciente de origen por determinar")),
 ColloquialDentalPhrase(14,"Se me cayó la tapadura",listOf("Pérdida de restauración referida","Desalojo de material restaurador","Restauración ausente o parcialmente perdida por confirmar")),
 ColloquialDentalPhrase(15,"Se me cayó la corona",listOf("Descementación o pérdida de corona protésica referida","Prótesis fija extracoronaria desalojada","Pérdida de retención de restauración indirecta por valorar")),
 ColloquialDentalPhrase(16,"Me duele con lo frío",listOf("Dolor provocado por estímulo frío","Sensibilidad dental al frío referida","Respuesta dolorosa a estímulo térmico frío pendiente de pruebas")),
 ColloquialDentalPhrase(17,"Me duele con lo caliente",listOf("Dolor provocado por estímulo caliente","Sensibilidad dental al calor referida","Respuesta dolorosa a estímulo térmico caliente pendiente de pruebas")),
 ColloquialDentalPhrase(18,"Me duele con lo dulce",listOf("Dolor provocado por estímulo dulce","Sensibilidad dental a estímulo osmótico/alimentos dulces","Sintomatología dental asociada al consumo de azúcares por valorar")),
 ColloquialDentalPhrase(19,"Me duele al morder",listOf("Dolor a la masticación referido","Dolor provocado por carga oclusal","Sintomatología a la presión/masticación pendiente de pruebas de percusión y oclusión")),
 ColloquialDentalPhrase(20,"Me despierta el dolor en la noche",listOf("Dolor nocturno referido","Sintomatología dental que interrumpe el sueño","Dolor espontáneo nocturno pendiente de valoración pulpar/periapical")),
 ColloquialDentalPhrase(21,"Me late la muela",listOf("Dolor pulsátil referido","Odontalgia de carácter pulsátil","Dolor dental rítmico/pulsátil pendiente de valoración")),
 ColloquialDentalPhrase(22,"Siento que el diente está alto",listOf("Sensación de contacto oclusal prematuro","Percepción de extrusión o interferencia oclusal","Molestia asociada al contacto dentario por valorar")),
 ColloquialDentalPhrase(23,"Tengo sarro",listOf("Cálculo dental referido","Depósitos mineralizados supragingivales/subgingivales por valorar","Presencia referida de cálculo dental pendiente de exploración periodontal")),
 ColloquialDentalPhrase(24,"Tengo placa",listOf("Biofilm dental referido","Acumulación de placa dentobacteriana referida","Depósitos blandos sobre superficies dentarias por valorar")),
 ColloquialDentalPhrase(25,"Tengo mal aliento",listOf("Halitosis referida","Olor bucal desagradable percibido por el paciente","Alteración del aliento de origen por determinar")),
 ColloquialDentalPhrase(26,"Tengo los dientes amarillos",listOf("Cambio de coloración dental referido","Discromía dental amarillenta referida","Alteración cromática extrínseca o intrínseca por determinar")),
 ColloquialDentalPhrase(27,"Quiero blanquearme los dientes",listOf("Solicitud de aclaramiento dental","Motivo estético relacionado con color dental","Consulta por modificación electiva de la tonalidad dentaria")),
 ColloquialDentalPhrase(28,"Me rechinan los dientes",listOf("Bruxismo referido con rechinamiento","Actividad masticatoria repetitiva durante sueño/vigilia por valorar","Reporte de rechinamiento dentario pendiente de evaluación")),
 ColloquialDentalPhrase(29,"Aprieto mucho los dientes",listOf("Apretamiento dental referido","Actividad parafuncional de contacto dentario sostenido por valorar","Bruxismo de vigilia/sueño posible, pendiente de caracterización")),
 ColloquialDentalPhrase(30,"Me truena la mandíbula",listOf("Ruido articular referido en ATM","Chasquido articular durante movimiento mandibular","Sonido de ATM compatible con clic por valorar clínicamente")),
 ColloquialDentalPhrase(31,"Se me atora la mandíbula",listOf("Bloqueo mandibular referido","Limitación o bloqueo funcional de la ATM por valorar","Episodio de restricción de apertura/cierre mandibular")),
 ColloquialDentalPhrase(32,"No puedo abrir bien la boca",listOf("Limitación de apertura bucal referida","Disminución del rango de apertura mandibular","Trismus o limitación funcional de etiología por determinar")),
 ColloquialDentalPhrase(33,"Me duele la quijada",listOf("Dolor mandibular referido","Dolor en región mandibular/masticatoria por localizar","Sintomatología en mandíbula, músculos o ATM por diferenciar")),
 ColloquialDentalPhrase(34,"Me muerdo el cachete",listOf("Mordisqueo de mucosa bucal referido","Traumatismo mecánico repetitivo de mucosa yugal","Hábito de mordisqueo con posible queratosis friccional por valorar")),
 ColloquialDentalPhrase(35,"Me salió una llaga",listOf("Úlcera oral referida","Pérdida de continuidad del epitelio oral por describir","Lesión ulcerada de etiología por determinar")),
 ColloquialDentalPhrase(36,"Tengo un fuego en la boca",listOf("Lesión vesicular/ulcerativa referida coloquialmente","Lesión dolorosa de mucosa o labio por caracterizar","Proceso vesículo-ulcerativo por confirmar; no asumir etiología")),
 ColloquialDentalPhrase(37,"Me arde la boca",listOf("Sensación de ardor bucal referida","Disestesia oral tipo ardor","Síntoma urente de mucosa oral de etiología por determinar")),
 ColloquialDentalPhrase(38,"Tengo la boca seca",listOf("Xerostomía referida","Sensación subjetiva de sequedad oral","Hiposalivación posible pendiente de evaluación del flujo salival")),
 ColloquialDentalPhrase(39,"Me sale mucha saliva",listOf("Sialorrea referida","Percepción de exceso de saliva","Aumento aparente de salivación o dificultad para manejar secreciones por valorar")),
 ColloquialDentalPhrase(40,"Tengo la lengua blanca",listOf("Recubrimiento blanquecino lingual referido","Saburra o placa blanca lingual por caracterizar","Alteración blanca del dorso lingual de etiología por determinar")),
 ColloquialDentalPhrase(41,"Tengo la lengua partida",listOf("Lengua fisurada referida","Presencia de surcos/fisuras linguales por valorar","Alteración de superficie dorsal lingual compatible con fisuración")),
 ColloquialDentalPhrase(42,"Me salió una mancha en la boca",listOf("Mácula o placa pigmentada/coloreada por describir","Cambio focal de coloración de mucosa oral","Lesión plana de coloración anormal pendiente de caracterización")),
 ColloquialDentalPhrase(43,"No me sale el diente",listOf("Retraso o ausencia de erupción referida","Órgano dentario no erupcionado en el tiempo esperado por valorar","Retención, impactación o variación eruptiva por determinar")),
 ColloquialDentalPhrase(44,"Me salió el diente chueco",listOf("Erupción en posición anómala referida","Malposición dentaria","Desviación de la posición esperada del órgano dentario por valorar")),
 ColloquialDentalPhrase(45,"Me salió otro diente atrás",listOf("Erupción ectópica o doble hilera referida","Órgano dentario erupcionado en posición lingual/palatina por valorar","Persistencia temporal con erupción del sucesor u otra alteración eruptiva por determinar")),
 ColloquialDentalPhrase(46,"Tengo un diente de más",listOf("Diente supernumerario referido","Aumento del número dentario por confirmar","Órgano dentario adicional compatible con supernumerario pendiente de evaluación")),
 ColloquialDentalPhrase(47,"Me falta un diente desde siempre",listOf("Ausencia congénita referida","Agenesia/hipodoncia posible por confirmar","Ausencia dentaria sin antecedente de extracción pendiente de estudio")),
 ColloquialDentalPhrase(48,"Se me enterró la muela del juicio",listOf("Tercer molar no erupcionado referido","Retención o impactación de tercer molar por valorar","Tercer molar incluido/impactado posible pendiente de imagenología")),
 ColloquialDentalPhrase(49,"Me lastima la dentadura",listOf("Molestia asociada a prótesis removible","Trauma de mucosa relacionado con prótesis por valorar","Punto de presión, inestabilidad o desajuste protésico por determinar")),
 ColloquialDentalPhrase(50,"Se me mueve la placa",listOf("Inestabilidad de prótesis removible referida","Pérdida de retención/estabilidad protésica","Movilidad funcional de prótesis removible pendiente de evaluación"))
)

@Composable
fun ColloquialDentalTranslatorV42(onBack:()->Unit){
 var query by remember{mutableStateOf("")}
 var open by remember{mutableStateOf<Int?>(null)}
 val filtered=colloquialDentalPhrasesV42.filter{query.isBlank()||it.phrase.contains(query,true)||it.clinical.any{x->x.contains(query,true)}}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Traductor de palabras coloquiales en odontología",onBack,"50 expresiones frecuentes con tres formas posibles de trasladarlas a lenguaje clínico. Son opciones de redacción y orientación, no diagnósticos automáticos.")}
  item{OutlinedTextField(query,{query=it.take(80)},modifier=Modifier.fillMaxWidth(),label={Text("Buscar expresión o término clínico")},singleLine=true)}
  item{NoticeCard("Conserva siempre el motivo de consulta literal entre comillas. Estas traducciones sirven para construir el padecimiento actual después del interrogatorio y la exploración.")}
  items(filtered.size){i->val x=filtered[i];Card(onClick={open=if(open==x.id)null else x.id},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text("“"+x.phrase+"”",fontWeight=FontWeight.Bold);if(open==x.id)x.clinical.forEachIndexed{n,t->Text("${n+1}. $t")}else Text("Toca para ver 3 traducciones clínicas",style=MaterialTheme.typography.bodySmall)}}}
 }
}
