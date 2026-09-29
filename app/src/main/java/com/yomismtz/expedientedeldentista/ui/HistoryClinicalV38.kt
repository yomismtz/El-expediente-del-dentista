package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.DrawableRes
import com.yomismtz.expedientedeldentista.R
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
 HItem(AppScreen.HISTORY_ORTHO,"Antecedentes de tratamientos ortodónticos","Aparatología, duración, finalidad, retención y resultado."),
 HItem(AppScreen.HISTORY_DENTAL_ALTERATIONS,"Alteraciones de órganos dentarios","Número, forma, tamaño, estructura y erupción."),
 HItem(AppScreen.HISTORY_HABITS,"Hábitos y parafunciones","Qué son y cómo se observan extraoral e intraoralmente."),
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
 data class ReasonDx(val reason:String,val dx:List<String>)
 val catalog=listOf(
  ReasonDx("Dolor dental espontáneo",listOf("Pulpitis reversible","Pulpitis irreversible sintomática","Necrosis pulpar con periodontitis apical","Fisura o fractura dental","Dolor referido no odontogénico")),
  ReasonDx("Dolor al frío",listOf("Hipersensibilidad dentinaria","Caries dental","Pulpitis reversible","Restauración con filtración o defecto","Fisura dental")),
  ReasonDx("Dolor al calor",listOf("Pulpitis irreversible sintomática","Necrosis pulpar parcial","Periodontitis apical sintomática","Fisura dental","Dolor referido")),
  ReasonDx("Dolor al masticar",listOf("Periodontitis apical sintomática","Fisura o fractura dental","Trauma oclusal","Absceso apical agudo","Enfermedad periodontal localizada")),
  ReasonDx("Dolor nocturno",listOf("Pulpitis irreversible sintomática","Periodontitis apical sintomática","Absceso apical agudo","Fisura dental","Dolor orofacial no odontogénico")),
  ReasonDx("Dolor después de una restauración",listOf("Sensibilidad postoperatoria","Interferencia oclusal","Pulpitis reversible","Pulpitis irreversible sintomática","Filtración o defecto restaurador")),
  ReasonDx("Dolor después de una extracción",listOf("Dolor postoperatorio esperado","Alveolitis","Infección posoperatoria","Trauma de tejidos blandos","Fragmento o cuerpo extraño a valorar")),
  ReasonDx("Dolor en encía",listOf("Gingivitis localizada","Absceso periodontal","Trauma local","Lesión ulcerativa","Dolor de origen dental referido a encía")),
  ReasonDx("Dolor en mandíbula o maxilar",listOf("Origen odontogénico","Trastorno temporomandibular","Sinusitis maxilar a valorar","Traumatismo","Dolor neuropático u otro dolor orofacial")),
  ReasonDx("Dolor en ATM",listOf("Dolor articular temporomandibular","Dolor muscular masticatorio","Desplazamiento discal","Enfermedad articular degenerativa","Dolor referido")),
  ReasonDx("Chasquido de ATM",listOf("Desplazamiento discal con reducción","Hipermovilidad articular","Alteración mecánica articular","Cambios degenerativos","Hallazgo articular sin dolor a correlacionar")),
  ReasonDx("Limitación para abrir la boca",listOf("Trastorno temporomandibular","Espasmo o dolor muscular","Infección odontogénica","Pericoronitis","Traumatismo")),
  ReasonDx("Inflamación de cara",listOf("Absceso odontogénico","Celulitis odontogénica","Infección periodontal","Pericoronitis","Origen no odontogénico a valorar")),
  ReasonDx("Inflamación de encía",listOf("Gingivitis","Absceso periodontal","Absceso de origen endodóntico con drenaje","Pericoronitis","Trauma o irritación local")),
  ReasonDx("Bolita en la encía",listOf("Trayecto sinusal de origen endodóntico","Absceso periodontal","Quiste o lesión reactiva","Fibroma irritativo","Otra lesión de tejidos blandos")),
  ReasonDx("Salida de pus",listOf("Absceso apical","Absceso periodontal","Pericoronitis supurada","Trayecto sinusal odontogénico","Infección de tejidos blandos")),
  ReasonDx("Sangrado de encías",listOf("Gingivitis inducida por biofilm","Periodontitis","Trauma por higiene","Inflamación local","Alteración sistémica o medicamentosa a valorar")),
  ReasonDx("Mal aliento",listOf("Biofilm lingual","Gingivitis o periodontitis","Caries o retención alimentaria","Xerostomía","Origen extraoral a valorar")),
  ReasonDx("Diente flojo",listOf("Periodontitis","Trauma dental","Trauma oclusal","Reabsorción fisiológica en diente temporal","Lesión periapical u otra causa")),
  ReasonDx("Diente roto",listOf("Fractura coronaria","Fisura dental","Caries extensa con pérdida estructural","Fractura de restauración","Traumatismo dentoalveolar")),
  ReasonDx("Se cayó una restauración",listOf("Falla restauradora","Caries recurrente","Fractura dental","Desgaste o pérdida de retención","Problema oclusal a valorar")),
  ReasonDx("Se rompió una prótesis",listOf("Fractura protésica","Pérdida de retención","Desgaste de componentes","Cambio del soporte oral","Sobrecarga oclusal")),
  ReasonDx("Corona floja o desprendida",listOf("Pérdida de cementación","Caries recurrente","Fractura del muñón o diente","Falla del material restaurador","Problema oclusal")),
  ReasonDx("Implante con molestia",listOf("Mucositis periimplantaria","Periimplantitis","Sobrecarga oclusal","Problema protésico","Dolor de origen adyacente")),
  ReasonDx("Diente oscuro",listOf("Necrosis pulpar","Cambio de color postraumático","Tinción intrínseca","Caries","Pigmentación o restauración previa")),
  ReasonDx("Manchas blancas en dientes",listOf("Lesión inicial de caries","Fluorosis","Hipomineralización","Hipoplasia del esmalte","Desmineralización asociada a ortodoncia")),
  ReasonDx("Manchas oscuras en dientes",listOf("Caries","Tinción extrínseca","Pigmentación de fosas y fisuras","Restauración pigmentada","Defecto estructural del esmalte")),
  ReasonDx("Dientes amarillos",listOf("Color dental fisiológico","Tinción extrínseca","Cambios por edad","Alteración del esmalte o dentina","Cambio de color por medicamentos o exposición")),
  ReasonDx("Quiero blanqueamiento",listOf("Tinción extrínseca","Tinción intrínseca","Cambio de color asociado a edad","Fluorosis a valorar","Discromía de diente no vital")),
  ReasonDx("Dientes chuecos",listOf("Apiñamiento dental","Malposición dentaria","Discrepancia dentoalveolar","Alteración de erupción","Maloclusión")),
  ReasonDx("Espacios entre dientes",listOf("Diastema fisiológico","Discrepancia dentoalveolar","Ausencia dental","Frenillo u otro factor local","Migración dental periodontal")),
  ReasonDx("Mordida incorrecta",listOf("Maloclusión sagital","Mordida cruzada","Mordida abierta","Sobremordida aumentada","Desviación de línea media")),
  ReasonDx("Dientes de adelante muy salidos",listOf("Overjet aumentado","Proinclinación incisiva","Maloclusión Clase II","Hábito oral asociado","Discrepancia esqueletal a valorar")),
  ReasonDx("Mordida abierta",listOf("Mordida abierta dentoalveolar","Hábito de succión","Interposición lingual","Alteración eruptiva","Componente esqueletal a valorar")),
  ReasonDx("Mordida cruzada",listOf("Mordida cruzada anterior","Mordida cruzada posterior unilateral","Mordida cruzada posterior bilateral","Desplazamiento funcional","Discrepancia esqueletal a valorar")),
  ReasonDx("No sale un diente",listOf("Erupción tardía","Diente impactado","Agenesia dental","Obstáculo eruptivo","Erupción ectópica")),
  ReasonDx("Salió un diente en otro lugar",listOf("Erupción ectópica","Malposición dentaria","Falta de espacio","Diente supernumerario","Alteración de trayectoria eruptiva")),
  ReasonDx("Diente extra",listOf("Diente supernumerario","Mesiodens","Odontoma a descartar","Diente temporal retenido confundido con extra","Variación anatómica a valorar")),
  ReasonDx("Falta un diente",listOf("Agenesia","Diente impactado","Pérdida dental previa","Retardo eruptivo","Diente retenido")),
  ReasonDx("Diente de leche no se cae",listOf("Retención prolongada de temporal","Ausencia del sucesor permanente","Erupción ectópica del sucesor","Anquilosis","Alteración de cronología eruptiva")),
  ReasonDx("Diente permanente salió detrás del de leche",listOf("Erupción lingual/palatina del sucesor","Retención de diente temporal","Falta de espacio","Erupción ectópica","Alteración de exfoliación")),
  ReasonDx("Dolor por muela del juicio",listOf("Pericoronitis","Caries del tercer molar","Pulpitis","Periodontitis apical","Dolor periodontal o de segundo molar adyacente")),
  ReasonDx("Quiero sacar una muela del juicio",listOf("Tercer molar impactado","Pericoronitis recurrente","Caries no restaurable","Patología periodontal distal","Indicación quirúrgica por lesión asociada a valorar")),
  ReasonDx("Llaga en la boca",listOf("Úlcera traumática","Afta","Lesión herpética","Lesión inmunomediada","Lesión persistente que requiere estudio")),
  ReasonDx("Ampollas en la boca",listOf("Lesión viral","Lesión inmunomediada","Trauma térmico o químico","Reacción medicamentosa","Otra enfermedad vesículo-ampollar")),
  ReasonDx("Mancha en la boca",listOf("Pigmentación fisiológica","Lesión melanótica","Mácula vascular","Tatuaje por material","Lesión pigmentada que requiere valoración")),
  ReasonDx("Bulto en la boca",listOf("Fibroma irritativo","Mucocele","Lesión inflamatoria reactiva","Quiste","Neoplasia benigna o maligna a descartar")),
  ReasonDx("Lengua dolorosa o ardor",listOf("Trauma local","Candidiasis","Glositis","Xerostomía","Síndrome de boca ardiente a valorar")),
  ReasonDx("Boca seca",listOf("Xerostomía medicamentosa","Hipofunción salival","Deshidratación","Enfermedad sistémica asociada","Respiración oral")),
  ReasonDx("Muchísima saliva",listOf("Sialorrea funcional","Irritación oral","Problema de deglución","Efecto medicamentoso","Condición neurológica a valorar")),
  ReasonDx("Dolor o aumento de volumen de glándula salival",listOf("Sialolitiasis","Sialadenitis","Obstrucción ductal","Quiste o lesión salival","Otra masa glandular a valorar")),
  ReasonDx("Me truena o aprieto los dientes",listOf("Bruxismo referido","Apretamiento","Dolor muscular masticatorio","Desgaste dental","Trastorno temporomandibular asociado")),
  ReasonDx("Desgaste de dientes",listOf("Atrición","Erosión","Abrasión","Abfracción a valorar","Bruxismo asociado")),
  ReasonDx("Sensibilidad generalizada",listOf("Hipersensibilidad dentinaria","Recesión gingival","Erosión dental","Abrasión","Caries múltiples")),
  ReasonDx("Comida se atora entre dientes",listOf("Contacto proximal abierto","Caries proximal","Migración dental","Defecto restaurador","Enfermedad periodontal")),
  ReasonDx("Encía se bajó",listOf("Recesión gingival","Trauma por cepillado","Periodontitis","Malposición dental","Fenotipo periodontal y factores locales")),
  ReasonDx("Quiero limpieza dental",listOf("Biofilm y cálculo supragingival","Gingivitis","Periodontitis a descartar","Tinciones extrínsecas","Necesidad preventiva sin enfermedad activa")),
  ReasonDx("Quiero revisión general",listOf("Paciente aparentemente sano a confirmar","Caries dental","Gingivitis","Periodontitis","Alteraciones oclusales o mucosas a detectar")),
  ReasonDx("Golpe en un diente",listOf("Conmoción o subluxación","Luxación dental","Fractura coronaria","Fractura radicular","Avulsión o lesión alveolar")),
  ReasonDx("Diente se salió por un golpe",listOf("Avulsión de diente permanente","Avulsión de diente temporal","Lesión alveolar asociada","Lesión de tejidos blandos","Trauma de dientes vecinos")),
  ReasonDx("Necesito prótesis porque me faltan dientes",listOf("Edentulismo parcial","Edentulismo total","Necesidad de prótesis removible","Necesidad de prótesis fija según caso","Rehabilitación implantosoportada a valorar"))
 )
 var selectedReason by rememberRecordState<Int?>("history.reason.selectedReason",null)
 var selectedDx by rememberRecordState<Int?>("history.reason.selectedDx",null)
 var showCatalog by remember { mutableStateOf(selectedReason==null) }
 var onset by rememberRecordState("history.reason.onset","")
 var evolution by rememberRecordState("history.reason.evolution","")
 var symptoms by rememberRecordState("history.reason.symptoms","")
 var modifiers by rememberRecordState("history.reason.modifiers","")
 val current=selectedReason?.let{catalog.getOrNull(it)}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Motivo de consulta y padecimiento actual",onBack,"Selecciona el motivo referido. Al tocarlo, el catálogo se cierra y los diagnósticos diferenciales aparecen inmediatamente en su lugar, sin obligarte a recorrer una lista larga.")}
  item{NoticeCard("Los diagnósticos mostrados son posibilidades educativas. El diagnóstico clínico requiere integrar interrogatorio, exploración y pruebas indicadas.")}
  if(showCatalog || current==null){
   item{SectionCard("1 · Motivo de consulta"){
    Text("Selecciona una opción. Al elegirla, esta lista se sustituye por el motivo seleccionado y los siguientes pasos quedan visibles justo debajo.",style=MaterialTheme.typography.bodySmall)
    ChipChoices(
     catalog.mapIndexed{i,x->x.reason to (selectedReason==i)},
     {i->selectedReason=i;selectedDx=null;showCatalog=false},
     columns=5
    )
   }}
  }else{
   item{SectionCard("1 · Motivo seleccionado"){
    Text(current.reason,fontWeight=FontWeight.Bold)
    OutlinedButton(onClick={showCatalog=true}){Text("Cambiar motivo")}
   }}
   item{SectionCard("2 · Cinco diagnósticos diferenciales posibles"){
    ChipChoices(current.dx.mapIndexed{i,x->x to (selectedDx==i)},{selectedDx=it},columns=5)
   }}
   item{SectionCard("3 · Padecimiento actual"){
    Text("Registra la evolución con las palabras del paciente y completa sólo lo que realmente se obtuvo en el interrogatorio.",style=MaterialTheme.typography.bodySmall)
    OutlinedTextField(onset,{onset=it},label={Text("Inicio · ¿desde cuándo?")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(evolution,{evolution=it},label={Text("Evolución · ¿cómo ha cambiado?")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(symptoms,{symptoms=it},label={Text("Síntomas y características asociadas")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(modifiers,{modifiers=it},label={Text("Qué lo aumenta, disminuye o desencadena")},modifier=Modifier.fillMaxWidth())
   }}
   item{SectionCard("4 · Selección para estudio"){
    Text("Motivo: "+current.reason,fontWeight=FontWeight.Bold)
    Text("Posibilidad diagnóstica seleccionada: "+(selectedDx?.let{current.dx.getOrNull(it)}?:"sin seleccionar"))
    Text("Confirma o descarta mediante anamnesis dirigida, exploración clínica y auxiliares/pruebas que correspondan; no conviertas esta selección en diagnóstico definitivo.",style=MaterialTheme.typography.bodySmall)
   }}
  }
 }
}

@Composable fun HistoryHereditaryV38(lang:String,onBack:()->Unit){
 val relatives=listOf("Madre","Padre","Hermana/o","Hija/o","Tía/o","Abuela/o (sin especificar)","Abuelo paterno","Abuela paterna","Abuelo materno","Abuela materna")
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
 var relative by rememberRecordState("history.hereditary.relative",0)
 var category by remember{mutableStateOf<String?>(null)}
 val selected=rememberRecordStateMap<String,Boolean>("history.hereditary.selected")
 val status=rememberRecordStateMap<String,String>("history.hereditary.status")
 var openHelp by remember{mutableStateOf(false)}
 var showRelatives by remember{mutableStateOf(false)}
 val current=relatives[relative]
 val diseases=category?.let{categories[it]}?:emptyList()
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes heredo-familiares",onBack,"Primero selecciona el familiar y después la categoría de enfermedad. Ante un positivo, amplía inicio, evolución, estado actual, medicamentos y complicaciones.")}
  item{NoticeCard("Este apartado busca antecedentes familiares que puedan aportar predisposición o contexto clínico. No significa que el paciente padezca la misma enfermedad.")}
  item{SectionCard("1 · Familiar"){
   Text("Seleccionado: $current",fontWeight=FontWeight.Bold)
   OutlinedButton(onClick={showRelatives=!showRelatives}){Text(if(showRelatives)"Ocultar familiares" else "Cambiar familiar")}
   if(showRelatives){
    Spacer(Modifier.height(8.dp))
    ChipChoices(relatives.mapIndexed{i,x->x to (relative==i)},{i->relative=i;category=null;showRelatives=false},columns=3)
   }
  }}
  item{SectionCard("2 · Categoría"){
   Text("Elige una categoría; sus enfermedades se abren aquí mismo para evitar desplazamiento innecesario.",style=MaterialTheme.typography.bodySmall)
   ChipChoices(categories.keys.map{x->x to (category==x)},{i->category=categories.keys.elementAt(i)},columns=2)
   category?.let { cat ->
    Spacer(Modifier.height(10.dp))
    Text("3 · $cat · $current",fontWeight=FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
    diseases.forEach { d ->
     val key="$current|$cat|$d"
     val checked=selected[key]==true
     Card(Modifier.fillMaxWidth().padding(vertical=4.dp)){Column(Modifier.padding(10.dp)){
      Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){Checkbox(checked,{selected[key]=it;if(!it)status.remove(key)});Text(d,Modifier.weight(1f))}
      if(checked){
       ChipChoices(listOf("Vive","Falleció","No sabe").map{x->x to (status[key]==x)},{i->status[key]=listOf("Vive","Falleció","No sabe")[i]},columns=3)
       Text("Amplía: inicio/edad aproximada · evolución · estado actual · tratamiento/medicamentos · complicaciones.",style=MaterialTheme.typography.bodySmall)
      }
     }}
    }
   }
  }}
  item{Card(onClick={openHelp=!openHelp},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(13.dp)){Text("4 · ¿Cómo registrar un positivo?",fontWeight=FontWeight.Bold);if(openHelp){Text("Ejemplo de estructura: «Abuela materna · diabetes mellitus · inicio aproximado ___ · evolución/estado actual ___ · tratamiento referido ___ · complicaciones ___ · vive/falleció/no sabe».");Text("Ejemplos del material docente incluyen abuelo paterno con hipertensión, abuela materna con diabetes, madre con resistencia a la insulina, hermano con asma, hermana con alergia a penicilina y abuelo con Alzheimer.")}else Text("Toca para ver la estructura")}}}
  item{SectionCard("5 · Cuando no hay información"){Text("Usa la opción que corresponda al interrogatorio: «Negado» · «Sin antecedentes» · «No referido». No son equivalentes: «no referido» indica que no se obtuvo o no se proporcionó el dato.")}}
  item{SectionCard("Resumen familiar"){val positives=selected.filterValues{it}.keys;if(positives.isEmpty())Text("Aún no hay antecedentes positivos seleccionados.") else positives.forEach{key->val p=key.split("|");Text("• "+p.joinToString(" · ")+" · "+(status[key]?:"estado no indicado"))}}}
 }
}

@Composable fun HistoryNonPathV38(lang:String,onBack:()->Unit){
 var section by rememberRecordState("history.nonpath.section","Alimentación")
 var sub by rememberRecordState("history.nonpath.sub","")
 val chosen=rememberRecordStateMap<String,String>("history.nonpath.chosen")
 val multi=rememberRecordStateMap<String,Boolean>("history.nonpath.multi")
 val sections=listOf("Alimentación","Vivienda","Higiene","Inmunizaciones","Hábitos y exposiciones")
 fun optionsCard(title:String,options:List<String>,note:String="",columns:Int=3)=@Composable{
  SectionCard(title){
   if(note.isNotBlank())Text(note,style=MaterialTheme.typography.bodySmall)
   ChipChoices(options.map{x->x to (chosen[title]==x)},{i->chosen[title]=options[i]},columns=columns)
  }
 }
 val services=linkedMapOf(
  "Agua entubada" to "Abastecimiento de agua conducida por tubería. Ayuda a contextualizar acceso a higiene, preparación de alimentos y saneamiento; no informa por sí solo la calidad microbiológica del agua.",
  "Drenaje" to "Sistema de evacuación de aguas residuales. Su disponibilidad aporta contexto sanitario del domicilio.",
  "Electricidad" to "Suministro eléctrico del hogar. Forma parte de las condiciones generales de vivienda y conservación de alimentos o medicamentos cuando requieren refrigeración.",
  "Gas" to "Fuente doméstica para cocción o calentamiento. Registrar el servicio y, si existe exposición a humo o combustión deficiente, ampliarla por separado.",
  "Internet" to "Acceso doméstico a conectividad. Es un dato de contexto y puede influir en acceso a información, comunicación y seguimiento, pero no es un indicador clínico aislado.",
  "Recolección de basura" to "Retiro regular de residuos domésticos. Ayuda a describir las condiciones de saneamiento y manejo de desechos."
 )
 val vaccines=linkedMapOf(
  "BCG" to "Prevención de formas graves de tuberculosis.",
  "Hepatitis B" to "Previene infección por virus de hepatitis B y sus complicaciones.",
  "Hexavalente acelular" to "Protege contra difteria, tosferina, tétanos, poliomielitis, Haemophilus influenzae tipo b y hepatitis B.",
  "Rotavirus" to "Previene gastroenteritis grave por rotavirus en lactantes.",
  "Neumococo conjugada" to "Previene enfermedad neumocócica invasiva y otras infecciones por neumococo.",
  "Influenza" to "Reduce el riesgo de influenza y de enfermedad grave; la indicación depende de edad, temporada y condiciones de riesgo.",
  "SRP" to "Triple viral: sarampión, rubéola y parotiditis.",
  "SR" to "Doble viral: sarampión y rubéola.",
  "DPT" to "Protege contra difteria, tosferina y tétanos.",
  "Td" to "Protege contra tétanos y difteria.",
  "Tdpa" to "Protege contra tétanos, difteria y tosferina; tiene indicaciones específicas, incluido el embarazo.",
  "VPH" to "Previene infección por tipos de VPH asociados a cánceres y otras enfermedades.",
  "COVID-19" to "Reduce principalmente el riesgo de enfermedad grave por COVID-19; la indicación vigente depende de edad y riesgo.",
  "Hepatitis A" to "Previene hepatitis A; puede indicarse de acuerdo con edad, antecedentes y riesgo.",
  "VSR materna" to "Vacunación durante el embarazo destinada a proteger al bebé frente a enfermedad por virus sincitial respiratorio.",
  "Varicela" to "Previene varicela y reduce el riesgo de complicaciones; la indicación depende de edad, antecedentes de infección y esquema previo.",
  "Meningococo" to "Protege frente a enfermedad meningocócica por los serogrupos incluidos en la vacuna; se usa según edad, condición de riesgo, brote o viaje.",
  "Herpes zóster" to "Reduce el riesgo de herpes zóster y neuralgia posherpética en grupos para los que esté indicada.",
  "Rabia" to "Puede utilizarse antes o después de una exposición de riesgo según valoración médica y protocolos de salud pública.",
  "Fiebre amarilla" to "Vacuna indicada principalmente por riesgo epidemiológico o requisitos de viaje a determinadas regiones."
 )
 val foodGroups=linkedMapOf(
  "Verduras" to "Aportan fibra, vitaminas, minerales y agua. Registrar frecuencia ayuda a describir el patrón dietético general.",
  "Frutas" to "Aportan fibra, vitaminas y minerales. Para caries importa además la forma de consumo y la frecuencia de exposiciones, sobre todo en jugos o productos azucarados.",
  "Cereales y tubérculos" to "Son una fuente importante de energía. Conviene distinguir preparaciones integrales de productos muy refinados o con azúcares añadidos.",
  "Leguminosas" to "Frijol, lenteja, garbanzo y similares aportan proteína vegetal, fibra y micronutrientes.",
  "Alimentos de origen animal" to "Carne, pescado, pollo y otros aportan proteína y micronutrientes. La cantidad y preparación forman parte del contexto dietético.",
  "Lácteos" to "Leche, yogur y queso aportan proteína y calcio; algunos productos pueden contener azúcares añadidos.",
  "Grasas y aceites" to "Son fuente concentrada de energía y participan en absorción de vitaminas. Registrar frecuencia y tipo ayuda a describir el patrón alimentario."
 )
 val vaccineEntries=vaccines.entries.toList()
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes personales no patológicos",onBack,"Alimentación abre primero por su relevancia odontológica. Selecciona frecuencias y el resumen orientativo se actualiza con las respuestas registradas.")}
  item{SectionCard("Categorías"){ChipChoices(sections.map{x->x to (section==x)},{i->section=sections[i];sub=""},columns=3)}}

  if(section=="Vivienda"){
   item{optionsCard("Número de cuartos",listOf("1","2","3","4","5","6 o más"),"Se interpreta junto con el número de habitantes para describir densidad habitacional; no genera por sí solo una clasificación.",3)}
   item{optionsCard("Número de habitantes",listOf("1","2","3","4","5","6","7 o más"),"Permite contextualizar posible hacinamiento junto con el número de habitaciones.",3)}
   item{optionsCard("Techo",listOf("Concreto/losa","Lámina metálica","Fibrocemento sin asbesto referido","Asbesto/amianto referido","Teja","Madera","Palma/material vegetal","Otro/no sabe"),"Describe el material principal del techo. Si se refiere asbesto/amianto, registra la exposición sin diagnosticar enfermedad.",3)}
   item{optionsCard("Paredes",listOf("Concreto/block/ladrillo","Adobe","Madera","Lámina","Material vegetal","Mixto","Otro/no sabe"),"El material de las paredes forma parte de las condiciones físicas de vivienda y puede orientar preguntas sobre humedad, polvo, ventilación o exposición ambiental.",3)}
   item{optionsCard("Pintura de las paredes",listOf("Sin pintura","Pintura actual/sin plomo referido","Pintura antigua con plomo conocida","Pintura antigua descascarada o deteriorada","Tipo de pintura desconocido"),"La pintura con plomo y su polvo pueden ser fuente de exposición, sobre todo cuando se deterioran, lijan o remueven. Registrar lo referido; no diagnosticar intoxicación.",3)}
   item{optionsCard("Piso",listOf("Tierra","Cemento/concreto","Loseta/cerámica","Madera","Vinilo/laminado","Otro/no sabe"),"Describe la superficie predominante del piso y complementa el contexto de saneamiento, humedad y facilidad de limpieza.",3)}
   item{optionsCard("Ventilación",listOf("Adecuada referida","Limitada","Sin ventilación aparente","No sabe"),"La ventilación modifica la exposición a humedad, humo y contaminantes interiores; es un dato de contexto, no un diagnóstico.",3)}
   item{SectionCard("Servicios domiciliarios"){
    Text("Marca los servicios disponibles. Las opciones se organizan en 2–3 celdas según el ancho de pantalla.",style=MaterialTheme.typography.bodySmall)
    ChipChoices(services.keys.map{x->x to (multi["serv|$x"]==true)},{i->
     val x=services.keys.elementAt(i)
     multi["serv|$x"]=!(multi["serv|$x"]?:false)
    },columns=3)
    Spacer(Modifier.height(6.dp))
    services.forEach{(name,description)->
     Text(name,fontWeight=FontWeight.SemiBold)
     Text(description,style=MaterialTheme.typography.bodySmall)
    }
   }}
  }

  if(section=="Higiene"){
   item{SectionCard("Higiene · registro de hábitos"){
    Text("Primero registra higiene general y después higiene bucal. Las respuestas permanecen guardadas al cambiar de categoría.",fontWeight=FontWeight.SemiBold)
    Text("Prioridad odontológica: frecuencia de cepillado, tipo de pasta y limpieza interdental; después correlaciona con biofilm, caries y periodonto.",style=MaterialTheme.typography.bodySmall)
   }}
   item{NoticeCard("La higiene se registra como hábito referido. Ninguna respuesta aislada califica a la persona ni establece por sí sola su estado de salud; debe integrarse con el contexto y la exploración clínica.")}
   item{optionsCard("Higiene general · baño corporal",listOf("Menos de 1 vez/semana","1–2/semana","3–4/semana","5–6/semana","Diario","2 o más/día"),"Frecuencia habitual de baño corporal referida.",3)}
   item{optionsCard("Lavado de manos · frecuencia diaria",listOf("0–1/día","2–3/día","4–5/día","6–9/día","10 o más/día","No sabe"),"Ayuda a describir hábitos generales de higiene. La calidad del lavado y los momentos en que se realiza también importan.",3)}
   item{SectionCard("Lavado de manos · momentos habituales"){
    val moments=listOf("Antes de preparar alimentos","Antes de comer","Después de ir al baño","Al llegar de la calle","Después de toser/estornudar","Después de contacto con animales")
    ChipChoices(moments.map{x->x to (multi["hand|$x"]==true)},{i->val x=moments[i];multi["hand|$x"]=!(multi["hand|$x"]?:false)},columns=3)
   }}
   item{optionsCard("Cambio de ropa · veces al día",listOf("Menos de 1/día","1/día","2/día","3 o más/día","Variable","No sabe"),"Registra cuántos cambios completos de ropa refiere en un día habitual.",3)}
   item{optionsCard("Cambio de ropa · días por semana",listOf("1 día","2–3 días","4–5 días","6 días","7 días","Variable/no sabe"),"Complementa la frecuencia diaria para evitar confundir «veces por día» con «días de la semana».",3)}
   item{optionsCard("Higiene bucal · cepillado dental",listOf("No se cepilla","Menos de 1 vez/día","1 vez/día","2 veces/día","3 veces/día","4 o más/día"),"Frecuencia de cepillado referida; después debe correlacionarse con técnica, biofilm y hallazgos clínicos.",3)}
   item{optionsCard("Higiene bucal · tipo de pasta",listOf("Pasta comercial regulada/etiquetada","Pasta comercial · no sabe","Producto naturista","Producto alternativo/casero","No usa pasta","No sabe"),"Registrar lo referido. Naturista o alternativo no equivale automáticamente a seguro, eficaz ni a una pasta fluorada.",3)}
   item{optionsCard("Higiene bucal · hilo/interdental",listOf("Nunca","Ocasional","1–3 veces/semana","4–6 veces/semana","Diario","2 o más/día"),"Describe la frecuencia de limpieza interdental; no sustituye la evaluación clínica de placa o periodonto.",3)}
  }

  if(section=="Alimentación"){
   item{SectionCard("Alimentación · evaluación dietética"){
    Text("Registra la frecuencia real de consumo. Empieza por grupos protectores y después revisa las exposiciones cariogénicas; las respuestas quedan conservadas en el expediente.",fontWeight=FontWeight.SemiBold)
    Text("Prioridad odontológica: frecuencia de azúcares, consumo entre comidas y nocturno, bebidas azucaradas/ácidas, agua simple y patrón general de alimentación.",style=MaterialTheme.typography.bodySmall)
   }}
   val freqOptions=listOf("Nunca","Menos de 1/semana","1–3/semana","4–6/semana","1/día","2–3/día","4 o más/día")
   val protectiveGroups=listOf(
    Triple("Verduras","Fuentes de fibra, folato, vitaminas y minerales.","Una frecuencia muy baja puede contribuir a un patrón con baja densidad de micronutrientes; valorar el conjunto de la dieta."),
    Triple("Fruta entera","Aporta fibra, vitamina C y otros micronutrientes.","Preferir fruta entera. Jugos y presentaciones azucaradas se valoran aparte por su exposición cariogénica."),
    Triple("Leguminosas","Frijol, lenteja, garbanzo y similares aportan proteína vegetal, hierro, folato y fibra.","Una frecuencia baja, especialmente si también hay poco alimento de origen animal, puede sugerir ingesta insuficiente de hierro/proteína."),
    Triple("Carne, pollo, pescado y huevo","Aportan proteína; varios de estos alimentos son fuentes relevantes de hierro, zinc y vitamina B12.","Consumo muy bajo junto con pocas leguminosas puede justificar tamizaje de posible ingesta insuficiente de proteína, hierro o B12."),
    Triple("Lácteos sin azúcar añadido","Leche, yogur natural y queso aportan proteína y calcio; algunos también vitamina D según fortificación.","Una ingesta baja puede contribuir a aporte insuficiente de calcio; no diagnostica deficiencia."),
    Triple("Cereales y tubérculos","Aportan energía; conviene diferenciar integrales de refinados y productos con azúcar añadido.","Un patrón muy restringido puede asociarse con ingesta energética insuficiente; refinados azucarados frecuentes aumentan exposiciones cariogénicas."),
    Triple("Agua simple","Principal bebida de hidratación y no añade azúcares fermentables.","Elegir agua simple en lugar de bebidas azucaradas reduce exposiciones a azúcar y ácidos.")
   )
   val cariogenicGroups=listOf(
    Triple("Dulces, caramelos y chocolate azucarado","Los azúcares libres son sustrato para bacterias del biofilm.","La frecuencia importa: exposiciones repetidas favorecen descensos repetidos del pH y aumentan el riesgo de desmineralización."),
    Triple("Galletas, pan dulce y postres","Combinan carbohidratos fermentables; algunos son retentivos.","Consumirlos repetidamente entre comidas prolonga o repite el desafío cariogénico."),
    Triple("Refrescos y bebidas azucaradas","Aportan azúcares libres; muchas además son ácidas.","La exposición frecuente aumenta riesgo cariogénico y puede contribuir a erosión dental según bebida y patrón."),
    Triple("Jugos y bebidas de fruta","Incluso el jugo 100% fruta contiene azúcares libres y suele ser ácido.","Tomarlo repetidamente o a sorbos prolonga la exposición de los dientes a azúcar/ácidos."),
    Triple("Bebidas deportivas o energéticas","Con frecuencia contienen azúcar y ácidos.","El consumo repetido, especialmente entre comidas, puede elevar el riesgo de caries y erosión."),
    Triple("Botanas/almidones refinados retentivos","Papas, crackers y productos similares aportan almidones procesados que pueden permanecer en boca.","La frecuencia y retención importan; se interpreta junto con higiene, saliva y exposición a fluoruro.")
   )
   item{NoticeCard("Tamizaje dietético odontológico. Registra qué consume y con qué frecuencia. Las alertas describen patrones de posible riesgo; no diagnostican anemia, desnutrición ni deficiencias vitamínicas. Los hallazgos relevantes deben correlacionarse con historia clínica, exploración y, cuando proceda, valoración médica/nutricional o estudios de laboratorio.")}
   protectiveGroups.forEach{(name,benefit,lowRisk)->
    item{optionsCard(name,freqOptions,"Beneficio: $benefit Si el consumo es bajo: $lowRisk",3)}
   }
   cariogenicGroups.forEach{(name,why,risk)->
    item{optionsCard(name,freqOptions,"Por qué importa: $why $risk",3)}
   }
   item{optionsCard("Alimentos azucarados · entre comidas",freqOptions,"Las exposiciones azucaradas entre comidas aumentan la frecuencia de desafíos ácidos. Menor frecuencia suele ser más favorable para control de caries.",3)}
   item{optionsCard("Alimentos o bebidas azucaradas · antes de dormir o durante la noche",freqOptions,"Es una exposición especialmente relevante porque durante el sueño disminuye el flujo salival. Registrar también higiene posterior cuando corresponda.",3)}
   item{optionsCard("Comidas principales al día",listOf("1","2","3","4","5","6 o más"),"Ayuda a interpretar regularidad e ingesta global. Una respuesta aislada no diagnostica malnutrición.",3)}
   item{optionsCard("Consistencia habitual",listOf("Predominio muy blando","Predominio blando","Mixta","Incluye firmes/fibrosos","Frecuentemente muy duros","Variable/no sabe"),"Describe demanda masticatoria. No existe una consistencia universalmente correcta y los alimentos extremadamente duros pueden producir trauma en personas susceptibles.",3)}
   item{SectionCard("Interpretación orientativa del patrón registrado"){
    val highSugar=listOf("Dulces, caramelos y chocolate azucarado","Galletas, pan dulce y postres","Refrescos y bebidas azucaradas","Jugos y bebidas de fruta","Bebidas deportivas o energéticas","Alimentos azucarados · entre comidas","Alimentos o bebidas azucaradas · antes de dormir o durante la noche").count{
     chosen[it] in listOf("1/día","2–3/día","4 o más/día")
    }
    val ironLow=chosen["Leguminosas"] in listOf("Nunca","Menos de 1/semana") && chosen["Carne, pollo, pescado y huevo"] in listOf("Nunca","Menos de 1/semana")
    val proteinLow=ironLow && chosen["Lácteos sin azúcar añadido"] in listOf("Nunca","Menos de 1/semana")
    val calciumLow=chosen["Lácteos sin azúcar añadido"] in listOf("Nunca","Menos de 1/semana")
    val produceLow=chosen["Verduras"] in listOf("Nunca","Menos de 1/semana") && chosen["Fruta entera"] in listOf("Nunca","Menos de 1/semana")
    val broadLow=listOf("Verduras","Fruta entera","Leguminosas","Carne, pollo, pescado y huevo","Lácteos sin azúcar añadido","Cereales y tubérculos").count{chosen[it] in listOf("Nunca","Menos de 1/semana")}
    if(chosen.none{it.key in protectiveGroups.map{x->x.first}+cariogenicGroups.map{x->x.first}+listOf("Alimentos azucarados · entre comidas","Alimentos o bebidas azucaradas · antes de dormir o durante la noche")}) Text("Selecciona frecuencias para generar el resumen orientativo.",style=MaterialTheme.typography.bodySmall)
    else {
     if(highSugar>=2) Text("• Riesgo cariogénico dietético aumentado: se registran varias exposiciones azucaradas diarias. Integrar con higiene, fluoruro, saliva y hallazgos clínicos.")
     else Text("• Riesgo cariogénico: interpretar con la frecuencia de azúcares registrada y los demás factores clínicos; la dieta sola no determina el riesgo total.")
     if(ironLow) Text("• Posible riesgo de ingesta insuficiente de hierro: baja frecuencia referida de leguminosas y alimentos de origen animal. No equivale a diagnóstico de anemia; correlacionar clínicamente y valorar estudios/derivación si están indicados.")
     if(proteinLow) Text("• Posible ingesta insuficiente de proteína: varios grupos fuente se reportan con frecuencia muy baja.")
     if(calciumLow) Text("• Posible ingesta insuficiente de calcio: frecuencia muy baja de lácteos referida. Considerar otras fuentes de calcio antes de concluir deficiencia.")
     if(produceLow) Text("• Posible baja ingesta de micronutrientes/fibra: verduras y fruta entera se reportan con frecuencia muy baja; revisar variedad dietética, vitamina C, folato y otros micronutrientes.")
     if(broadLow>=4) Text("• Riesgo de patrón alimentario poco variado / posible malnutrición: múltiples grupos básicos presentan frecuencia muy baja. Requiere valoración integral; esta pantalla no diagnostica desnutrición.")
     if(highSugar==0 && !ironLow && !proteinLow && !calciumLow && !produceLow && broadLow<4) Text("• No se activaron alertas dietéticas principales con las respuestas actuales. Esto no sustituye una valoración nutricional ni odontológica completa.")
    }
   }}
  }

  if(section=="Inmunizaciones"){
   item{NoticeCard("Listado educativo que combina vacunas del esquema nacional y otras que pueden indicarse por edad, condición de riesgo, exposición o viaje. Selecciona «Sí», «No» o «No sabe» según lo referido; la indicación individual debe verificarse con el esquema y lineamientos vigentes.")}
   items(vaccineEntries.size){i->
    val v=vaccineEntries[i]
    Card(Modifier.fillMaxWidth()){Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
     Text(v.key,fontWeight=FontWeight.Bold)
     Text(v.value,style=MaterialTheme.typography.bodySmall)
     ChipChoices(listOf("Sí","No","No sabe").map{x->x to (chosen["vac|"+v.key]==x)},{j->chosen["vac|"+v.key]=listOf("Sí","No","No sabe")[j]},columns=3)
    }}
   }
  }

  if(section=="Hábitos y exposiciones"){
   item{SectionCard("Tipo de hábito o exposición"){
    val habitTypes=listOf("Tabaquismo","Alcohol","Drogas","Tatuajes","Perforaciones")
    ChipChoices(habitTypes.map{x->x to (sub==x)},{i->sub=habitTypes[i]},columns=3)
   }}
  }
  if(section=="Hábitos y exposiciones" && sub=="Tabaquismo"){
   item{NoticeCard("Registrar tipo, cantidad y frecuencia permite estimar la exposición referida. En odontología el tabaco se relaciona con cambios periodontales, cicatrización y riesgo de lesiones; esta ficha no diagnostica esos problemas.")}
   item{optionsCard("Tabaquismo · estado",listOf("Nunca","Exfumador","Actual","Exposición pasiva","No sabe"),"",3)}
   item{optionsCard("Tabaco · días de consumo por semana",listOf("Menos de 1 día","1 día","2–3 días","4–6 días","7 días","No sabe"),"Distingue consumo ocasional de consumo diario.",3)}
   item{optionsCard("Tabaco · cigarrillos por día de consumo",listOf("1","2–5","6–10","11–20","21–40","Más de 40","No sabe/no aplica"),"Cantidad aproximada en un día en que sí fuma.",3)}
   item{optionsCard("Tabaco · cigarrillos por semana",listOf("1–5","6–20","21–50","51–100","101–140","Más de 140","No sabe/no aplica"),"Útil cuando el consumo no es diario; registrar la mejor aproximación referida.",3)}
   item{optionsCard("Tabaco · tiempo de exposición",listOf("<1 año","1–5 años","6–10 años","11–20 años",">20 años","No sabe"),"",3)}
   item{optionsCard("Producto",listOf("Cigarrillo","Puro","Pipa","Tabaco sin humo","Vapeador/cigarrillo electrónico","Más de uno","Otro/no sabe"),"Productos diferentes implican exposiciones distintas; evita convertirlos automáticamente a una equivalencia sin datos suficientes.",3)}
  }
  if(section=="Hábitos y exposiciones" && sub=="Alcohol"){
   item{NoticeCard("Registrar frecuencia y cantidad por ocasión ayuda a describir la exposición. La interpretación clínica debe considerar tipo de bebida, patrón de consumo, medicamentos y antecedentes.")}
   item{optionsCard("Alcohol · estado",listOf("Nunca","Anteriormente","Actual","No sabe"),"",3)}
   item{optionsCard("Alcohol · frecuencia",listOf("Menos de 1/mes","1–3/mes","1/semana","2–3/semana","4–6/semana","Diario","No sabe"),"",3)}
   item{optionsCard("Alcohol · cantidad por ocasión",listOf("1 bebida","2 bebidas","3–4 bebidas","5–6 bebidas","7 o más","No sabe"),"Cantidad aproximada en una ocasión típica; el tamaño y graduación de la bebida pueden variar.",3)}
   item{optionsCard("Tipo habitual",listOf("Cerveza","Vino","Destilados","Bebidas preparadas","Varios","Otro/no sabe"),"",3)}
  }
  if(section=="Hábitos y exposiciones" && sub=="Drogas"){
   item{NoticeCard("El registro es clínico y no punitivo. Pregunta sustancia, frecuencia, vía y último consumo porque pueden modificar signos vitales, interacción con medicamentos, xerostomía, bruxismo, cicatrización o conducta durante la atención.")}
   item{optionsCard("Sustancia referida",listOf("Ninguna","Cannabis","Cocaína/crack","Metanfetaminas/estimulantes","Opioides","Alucinógenos","Inhalables","Sedantes sin indicación referida","Varias","Otra/no sabe"),"",3)}
   item{optionsCard("Frecuencia",listOf("Nunca","Una vez/experimental","Menos de 1/mes","1–3/mes","1–6/semana","Diario","No sabe"),"",3)}
   item{optionsCard("Vía",listOf("Fumada/vaporizada","Oral","Intranasal","Inyectada","Inhalada","Otra/no sabe"),"",3)}
   item{optionsCard("Último consumo",listOf("<24 h","1–7 días","1–4 semanas","1–12 meses",">1 año","No recuerda/no sabe"),"",3)}
  }
  if(section=="Hábitos y exposiciones" && sub=="Perforaciones"){
   item{NoticeCard("Selecciona la localización referida. En perforaciones orales o periorales conviene registrar trauma dental/gingival, inflamación, sangrado, secreción e irritación.")}
   item{optionsCard("Localización de perforación",listOf("Ninguna","Lóbulo de oreja","Cartílago de oreja","Nariz · aleta","Nariz · septum","Ceja","Labio superior","Labio inferior","Frenillo labial","Lengua","Frenillo lingual","Mejilla","Ombligo","Pezón","Genital","Otra","Múltiples"),"La localización oral o perioral puede relacionarse con contacto repetido contra dientes y encía; se describe el sitio y los hallazgos, sin asumir complicación.",3)}
   item{optionsCard("Antigüedad",listOf("<1 mes","1–6 meses","7–12 meses","1–5 años",">5 años","No sabe"),"",3)}
   item{optionsCard("Complicaciones referidas",listOf("Ninguna","Dolor","Inflamación","Sangrado","Infección/secreción","Trauma dental/gingival","Alergia/irritación","Otra/no sabe"),"",3)}
  }
  if(section=="Hábitos y exposiciones" && sub=="Tatuajes"){
   item{NoticeCard("Registrar tatuajes permite documentar exposiciones cutáneas y antecedentes de cicatrización o reacciones. No se infieren conductas ni riesgos sin datos concretos.")}
   item{optionsCard("Número de tatuajes",listOf("Ninguno","1","2–3","4–5","6 o más"),"",3)}
   item{optionsCard("Localización principal",listOf("Cabeza/cuello","Tórax","Espalda","Abdomen","Brazo/antebrazo","Mano/dedos","Muslo/pierna","Pie/tobillo","Múltiples regiones","Otra/no sabe"),"Ubicación anatómica principal del tatuaje o de los tatuajes referidos.",3)}
   item{optionsCard("Antigüedad del más reciente",listOf("<1 mes","1–6 meses","7–12 meses","1–5 años",">5 años","No sabe"),"",3)}
   item{optionsCard("Lugar de realización",listOf("Estudio establecido","Servicio sanitario/profesional referido","Domicilio/no profesional","Centro penitenciario","Otro","No sabe"),"",3)}
   item{optionsCard("Complicaciones referidas",listOf("Ninguna","Infección","Reacción alérgica/dermatitis","Sangrado prolongado","Cicatrización anormal","Otra/no sabe"),"",3)}
  }
 }
}

@Composable fun HistoryGynecoV38(lang:String,onBack:()->Unit){
 var sex by rememberRecordState("history.gyneco.sex","")
 val chosen=rememberRecordStateMap<String,String>("history.gyneco.chosen")
 val nums=listOf("0","1","2","3","4","5 o más")
 @Composable fun OptionsCard(title:String,options:List<String>,note:String=""){
  SectionCard(title){
   val preferred=when {
    options.size<=2 -> 2
    options.any{it.length>30} -> 3
    options.size<=4 -> options.size
    else -> 5
   }
   ChipChoices(options.map{it to (chosen[title]==it)},{i->chosen[title]=options[i]},columns=preferred)
   if(note.isNotBlank())Text(note,style=MaterialTheme.typography.bodySmall)
  }
 }
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes gineco-obstétricos",onBack,"Registro educativo con opciones predeterminadas. Primero selecciona sexo; el interrogatorio gineco-obstétrico sólo se despliega cuando corresponde.")}
  item{SectionCard("Aplicación del interrogatorio"){
   Text("Selecciona si corresponde desplegar este apartado clínico.",style=MaterialTheme.typography.bodySmall)
   Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(sex=="H",{sex="H"},{Text("No aplica")});FilterChip(sex=="M",{sex="M"},{Text("Sí aplica")})}
  }}
  if(sex=="H")item{NoticeCard("Interrogatorio gineco-obstétrico marcado como no aplicable. Los campos sensibles permanecen ocultos.")}
  if(sex=="M"){
   item{NoticeCard("Registra únicamente datos clínicamente pertinentes y referidos por la paciente. Las opciones «Prefiere no responder» y «No recuerda» deben conservarse cuando estén disponibles; no completes información por inferencia.")}
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
 val chosen=rememberRecordStateMap<String,String>("history.surgical.chosen")
 val sections=listOf("Cirugías","Hospitalizaciones","Transfusiones","Donación de sangre","Trasplantes","Traumatismos")
 @Composable fun OptionsCard(title:String,options:List<String>,note:String=""){
  SectionCard(title){
   val preferred=when { options.size<=2->2; options.any{it.length>30}->3; options.size<=4->options.size; else->5 }
   ChipChoices(options.map{it to (chosen[title]==it)},{i->chosen[title]=options[i]},columns=preferred)
   if(note.isNotBlank())Text(note,style=MaterialTheme.typography.bodySmall)
  }
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

@Composable private fun ClinicalPhotoV38(title:String,@DrawableRes resource:Int,credit:String){
 LocalZoomableImageV21(title=title,resource=resource,attribution=credit)
}

@Composable fun HistoryPhysicalV38(lang:String,onBack:()->Unit){
 var section by rememberRecordState("history.physical.section","Inspección general")
 val selected=rememberRecordStateMap<String,String>("history.physical.selected")
 @Composable fun Pick(title:String,options:List<String>,note:String=""){
  val longest=options.maxOfOrNull{it.length}?:0
  // Clinical option labels must stay readable on phones; never trade legibility for density.
  val cols=when{
   longest>18->2
   options.size>=6->3
   else->2
  }
  SectionCard(title){
   ChipChoices(options.map{x->x to (selected[title]==x)},{i->selected[title]=options[i]},columns=cols)
   if(note.isNotBlank())Text(note,style=MaterialTheme.typography.bodySmall)
  }
 }
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Exploración física y extraoral",onBack,"Registro de inspección general, cráneo/cara, músculos extraorales, cuello y ganglios. Signos vitales y ATM se registran en sus módulos especializados para evitar duplicación.")}
  item{SectionCard("Apartado de exploración"){
   val physicalSections=listOf("Inspección general","Cráneo y cara","Músculos","Cuello","Ganglios")
   ChipChoices(
    physicalSections.map{x->x to (section==x)},
    {i->section=physicalSections[i]},
    columns=3
   )
   Text("Signos vitales, somatometría y glucosa se registran en Signos vitales. ATM y movimientos mandibulares se registran en el módulo ATM.",style=MaterialTheme.typography.bodySmall)
  }}
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
   item{when(selected["Forma craneal"]){
    "Dolicocefálico"->ClinicalPhotoV38("Dolicocefálico",R.drawable.face13_dolicocefalico,"Imagen local de la opción seleccionada.")
    "Mesocéfalo"->ClinicalPhotoV38("Mesocéfalo",R.drawable.face13_mesocefalico,"Imagen local de la opción seleccionada.")
    "Braquicéfalo"->ClinicalPhotoV38("Braquicéfalo",R.drawable.face13_braquicefalico,"Imagen local de la opción seleccionada.")
    "No valorable"->ClinicalPhotoV38("Cráneo no valorable",R.drawable.face13_craneo_no_valorable,"Imagen local de la opción seleccionada.")
   }}
   item{Pick("Patrón facial",listOf("Dolicofacial","Mesofacial","Braquifacial","No valorable"),"Dolicofacial: cara relativamente larga y estrecha. Mesofacial: proporción intermedia. Braquifacial: cara relativamente corta y ancha.")}
   item{when(selected["Patrón facial"]){
    "Dolicofacial"->ClinicalPhotoV38("Dolicofacial",R.drawable.face13_dolicofacial,"Imagen de la opción seleccionada.")
    "Mesofacial"->ClinicalPhotoV38("Mesofacial",R.drawable.face13_mesofacial,"Imagen de la opción seleccionada.")
    "Braquifacial"->ClinicalPhotoV38("Braquifacial",R.drawable.face13_braquifacial,"Imagen de la opción seleccionada.")
    "No valorable"->ClinicalPhotoV38("Patrón facial no valorable",R.drawable.face13_perfil_no_valorable,"Imagen de la opción seleccionada.")
   }}
   item{Pick("Perfil facial",listOf("Recto","Convexo","Cóncavo","No valorable"),"Qué es: relación anteroposterior aparente entre frente, región media facial, labios y mentón observada de perfil. Por qué importa: complementa el análisis facial y orienta la descripción de relaciones esqueletales y dentofaciales; no establece por sí solo un diagnóstico.")}
   item{when(selected["Perfil facial"]){
    "Recto"->ClinicalPhotoV38("Perfil facial · recto",R.drawable.allimg_075_perfil_recto,"Frente, labios y mentón se observan relativamente equilibrados. Interpretar junto con el resto de la exploración facial.")
    "Convexo"->ClinicalPhotoV38("Perfil facial · convexo",R.drawable.allimg_074_perfil_convexo,"El mentón se aprecia relativamente retruido. Correlacionar con la exploración facial, oclusal y esqueletal.")
    "Cóncavo"->ClinicalPhotoV38("Perfil facial · cóncavo",R.drawable.allimg_073_perfil_concavo,"El mentón se aprecia relativamente prominente o adelantado. Correlacionar con la exploración facial, oclusal y esqueletal.")
    "No valorable"->ClinicalPhotoV38("Perfil facial · no valorable",R.drawable.face13_perfil_no_valorable,"La fotografía o la posición no permiten clasificar el perfil con seguridad.")
   }}
   item{Pick("Forma de la cara",listOf("Ovalada","Redonda","Cuadrada","Rectangular / alargada","Triangular","Triangular invertida / corazón","Romboidal","Asimétrica","No valorable"),"Describe el contorno facial observado. La forma de la cara es descriptiva y no constituye por sí sola un diagnóstico.")}
   item{when(selected["Forma de la cara"]){
    "Ovalada"->ClinicalPhotoV38("Forma de la cara · ovalada",R.drawable.new77_edu_extraoral_formas_del_rostro_ovalado,"Imagen de la opción seleccionada.")
    "Redonda"->ClinicalPhotoV38("Forma de la cara · redonda",R.drawable.new77_edu_extraoral_formas_del_rostro_redondo,"Imagen de la opción seleccionada.")
    "Cuadrada"->ClinicalPhotoV38("Forma de la cara · cuadrada",R.drawable.new77_edu_extraoral_formas_del_rostro_cuadrado,"Imagen de la opción seleccionada.")
    "Rectangular / alargada"->ClinicalPhotoV38("Forma de la cara · rectangular / alargada",R.drawable.new77_edu_extraoral_formas_del_rostro_rectangular,"Imagen de la opción seleccionada.")
    "Triangular"->ClinicalPhotoV38("Forma de la cara · triangular",R.drawable.new77_edu_extraoral_formas_del_rostro_triangular,"Imagen de la opción seleccionada.")
    "Triangular invertida / corazón"->ClinicalPhotoV38("Forma de la cara · corazón",R.drawable.new77_edu_extraoral_formas_del_rostro_corazon,"Imagen de la opción seleccionada.")
    "Romboidal"->ClinicalPhotoV38("Forma de la cara · romboidal",R.drawable.new77_edu_extraoral_formas_del_rostro_rombo,"Imagen de la opción seleccionada.")
    "Asimétrica"->ClinicalPhotoV38("Forma de la cara · asimétrica",R.drawable.face13_asimetria_compleja,"Imagen de la opción seleccionada.")
    "No valorable"->ClinicalPhotoV38("Forma de la cara · no valorable",R.drawable.face13_perfil_no_valorable,"Imagen de la opción seleccionada.")
   }}
   item{Pick("Tez · pigmentación basal aparente",listOf("Albinismo","Muy clara","Clara","Claro medio","Medio","Morena clara","Morena media","Morena oscura","Oscura","Muy oscura","No valorable"),"Registrar de forma descriptiva la pigmentación basal aparente, separándola de cambios patológicos de coloración.")}
   item{when(selected["Tez · pigmentación basal aparente"]){
    "Albinismo"->ClinicalPhotoV38("Tez · albinismo",R.drawable.skin20_02,"Imagen de la opción seleccionada.")
    "Muy clara"->ClinicalPhotoV38("Tez · muy clara",R.drawable.skin20_09,"Imagen de la opción seleccionada.")
    "Clara"->ClinicalPhotoV38("Tez · clara",R.drawable.skin20_03,"Imagen de la opción seleccionada.")
    "Claro medio"->ClinicalPhotoV38("Tez · claro medio",R.drawable.skin20_04,"Imagen de la opción seleccionada.")
    "Medio"->ClinicalPhotoV38("Tez · medio",R.drawable.skin20_05,"Imagen de la opción seleccionada.")
    "Morena clara"->ClinicalPhotoV38("Tez · morena clara",R.drawable.skin20_06,"Imagen de la opción seleccionada.")
    "Morena media"->ClinicalPhotoV38("Tez · morena media",R.drawable.skin20_07,"Imagen de la opción seleccionada.")
    "Morena oscura"->ClinicalPhotoV38("Tez · morena oscura",R.drawable.skin20_08,"Imagen de la opción seleccionada.")
    "Oscura"->ClinicalPhotoV38("Tez · oscura",R.drawable.skin20_11,"Imagen de la opción seleccionada.")
    "Muy oscura"->ClinicalPhotoV38("Tez · muy oscura",R.drawable.skin20_10,"Imagen de la opción seleccionada.")
   }}
   item{Pick("Coloración cutánea · alteraciones",listOf("Sin alteración aparente","Palidez cutaneomucosa","Ictericia","Cianosis","Eritema / rubicundez","Coloración grisácea / cenicienta","Hiperpigmentación","Hipopigmentación","Discromía localizada","Discromía difusa","No valorable"),"Palidez = disminución aparente de coloración; ictericia = tonalidad amarillenta; cianosis = tonalidad azulada/violácea; eritema o rubicundez = enrojecimiento; coloración grisácea/cenicienta = tono gris anormal. Registrar el hallazgo observado sin atribuir una causa automáticamente.")}
   item{when(selected["Coloración cutánea · alteraciones"]){
    "Sin alteración aparente"->ClinicalPhotoV38("Coloración cutánea · sin alteración aparente",R.drawable.skin20_20,"Imagen de pigmentación basal.")
    "Palidez cutaneomucosa"->ClinicalPhotoV38("Palidez cutaneomucosa",R.drawable.skin20_19,"Imagen de la opción seleccionada.")
    "Ictericia"->ClinicalPhotoV38("Ictericia",R.drawable.skin20_18,"Imagen de la opción seleccionada.")
    "Cianosis"->ClinicalPhotoV38("Cianosis",R.drawable.skin20_01,"Imagen de la opción seleccionada.")
    "Eritema / rubicundez"->ClinicalPhotoV38("Eritema / rubicundez",R.drawable.skin20_14,"Imagen de la opción seleccionada.")
    "Coloración grisácea / cenicienta"->ClinicalPhotoV38("Coloración grisácea / cenicienta",R.drawable.skin20_15,"Imagen de la opción seleccionada.")
    "Hiperpigmentación"->ClinicalPhotoV38("Hiperpigmentación / melasma",R.drawable.skin20_16,"Imagen de la opción seleccionada.")
    "Hipopigmentación"->ClinicalPhotoV38("Hipopigmentación",R.drawable.skin20_17,"Imagen de la opción seleccionada.")
    "Discromía localizada"->ClinicalPhotoV38("Discromía localizada",R.drawable.skin20_13,"Imagen de la opción seleccionada.")
    "Discromía difusa"->ClinicalPhotoV38("Discromía difusa",R.drawable.skin20_12,"Imagen de la opción seleccionada.")
   }}
   item{Pick("Distribución de la alteración de color",listOf("No aplica","Generalizada","Facial difusa","Perioral","Periorbitaria","Localizada","Simétrica","Asimétrica","No valorable"),"La distribución ayuda a describir el hallazgo. Correlacionar con mucosas, iluminación, antecedentes y contexto clínico.")}
   item{Pick("Simetría facial",listOf("Simétrica aparente","Asimetría derecha","Asimetría izquierda","Asimetría compleja","No valorable"),"Qué es: comparación de ambos hemirrostros en reposo. Por qué importa: permite registrar diferencias de volumen, altura o posición y decidir qué estructuras requieren exploración dirigida; una asimetría aislada no establece diagnóstico.")}
   item{when(selected["Simetría facial"]){
    "Simétrica aparente"->ClinicalPhotoV38("Simetría facial aparente",R.drawable.face13_simetria_aparente,"Referencia local de la opción seleccionada; correlacionar con la exploración clínica.")
    "Asimetría derecha"->ClinicalPhotoV38("Asimetría facial derecha",R.drawable.face13_asimetria_derecha,"Referencia local de la opción seleccionada; describir región y magnitud clínica.")
    "Asimetría izquierda"->ClinicalPhotoV38("Asimetría facial izquierda",R.drawable.face13_asimetria_izquierda,"Referencia local de la opción seleccionada; describir región y magnitud clínica.")
    "Asimetría compleja"->ClinicalPhotoV38("Asimetría facial compleja",R.drawable.face13_asimetria_compleja,"Referencia local de la opción seleccionada; correlacionar con antecedentes y exploración.")
    "No valorable"->ClinicalPhotoV38("Simetría no valorable",R.drawable.face13_simetria_no_valorable,"Referencia local de la opción seleccionada.")
   }}
   item{Pick("Proporciones faciales · referencia",listOf("Tercios faciales","Quintos faciales","No valorable"),"Qué es: división clínica del rostro en segmentos verticales u horizontales para observar proporciones. Por qué importa: ayuda a describir discrepancias faciales y orientar el análisis ortodóntico/ortopédico sin convertir la proporción aislada en diagnóstico.")}
   item{when(selected["Proporciones faciales · referencia"]){
    "Tercios faciales"->ClinicalPhotoV38("Tercios faciales",R.drawable.edu_extraoral_tercios_faciales,"Divide el rostro en tercios de referencia para comparar proporciones verticales; interpretar con edad, anatomía y contexto clínico.")
    "Quintos faciales"->ClinicalPhotoV38("Quintos faciales",R.drawable.edu_extraoral_quintos_faciales,"Divide el ancho facial en quintos de referencia para comparar proporciones transversales; interpretar como guía descriptiva.")
   }}
   item{Pick("Línea media facial",listOf("Referencia identificable","Referencia difícil de identificar","No valorable"),"Qué es: referencia vertical clínica del rostro que permite comparar la posición de estructuras faciales y dentales. Por qué importa: sirve como referencia para describir simetría y valorar la relación de las líneas medias dentales; debe interpretarse junto con el análisis facial completo.")}
   item{Pick("Línea media dental maxilar",listOf("Coincide con línea media facial","Desviada a la derecha","Desviada a la izquierda","No valorable"),"Qué es: línea interincisiva superior comparada con la línea media facial. Por qué importa: permite registrar discrepancias transversales estéticas y oclusales y orientar el análisis ortodóntico; cuando exista desviación debe cuantificarse clínicamente en milímetros.")}
   item{when(selected["Línea media dental maxilar"]){
    "Coincide con línea media facial"->ClinicalPhotoV38("Línea media maxilar coincidente",R.drawable.new17_lineas_medias_coincidentes,"Referencia local de líneas medias coincidentes. Correlacionar con la línea media facial y cuantificar clínicamente cualquier discrepancia.")
    "Desviada a la derecha"->ClinicalPhotoV38("Línea media superior desviada a la derecha",R.drawable.new17_linea_media_superior_desviada_a_la_derecha,"Referencia local de la opción seleccionada. Registrar clínicamente la magnitud de la desviación en milímetros.")
    "Desviada a la izquierda"->ClinicalPhotoV38("Línea media superior desviada a la izquierda",R.drawable.new17_linea_media_superior_desviada_a_la_izquierda,"Referencia local de la opción seleccionada. Registrar clínicamente la magnitud de la desviación en milímetros.")
   }}
   item{Pick("Línea media dental mandibular",listOf("Coincide con línea media facial/maxilar","Desviada a la derecha","Desviada a la izquierda","No valorable"),"Qué es: línea interincisiva inferior comparada con la referencia facial y con la línea media maxilar. Por qué importa: permite documentar discrepancias entre arcadas y orientar el análisis oclusal/ortodóntico; cuando exista desviación debe cuantificarse clínicamente en milímetros.")}
   item{when(selected["Línea media dental mandibular"]){
    "Coincide con línea media facial/maxilar"->ClinicalPhotoV38("Líneas medias coincidentes",R.drawable.new17_lineas_medias_coincidentes,"Referencia local de coincidencia entre líneas medias. Confirmar clínicamente la relación facial, maxilar y mandibular.")
    "Desviada a la derecha"->ClinicalPhotoV38("Línea media inferior desviada a la derecha",R.drawable.new17_linea_media_inferior_desviada_a_la_derecha,"Referencia local de la opción seleccionada. Registrar clínicamente la magnitud de la desviación en milímetros.")
    "Desviada a la izquierda"->ClinicalPhotoV38("Línea media inferior desviada a la izquierda",R.drawable.new17_linea_media_inferior_desviada_a_la_izquierda,"Referencia local de la opción seleccionada. Registrar clínicamente la magnitud de la desviación en milímetros.")
   }}
   item{Pick("Perfil facial",listOf("Recto","Convexo","Cóncavo","No valorable"),"Qué es: relación anteroposterior aparente del perfil facial observada de lado. Por qué importa: orienta la descripción de la relación maxilomandibular y el análisis ortodóntico; no sustituye mediciones ni estudios diagnósticos.")}
   item{when(selected["Perfil facial"]){
    "Recto"->ClinicalPhotoV38("Perfil recto",R.drawable.allimg_075_perfil_recto,"Referencia local del perfil seleccionado. Correlacionar con examen facial, oclusión y estudios cuando estén indicados.")
    "Convexo"->ClinicalPhotoV38("Perfil convexo",R.drawable.allimg_074_perfil_convexo,"Referencia local del perfil seleccionado. La convexidad describe el contorno y no determina por sí sola su causa.")
    "Cóncavo"->ClinicalPhotoV38("Perfil cóncavo",R.drawable.allimg_073_perfil_concavo,"Referencia local del perfil seleccionado. La concavidad describe el contorno y no determina por sí sola su causa.")
    "No valorable"->ClinicalPhotoV38("Perfil no valorable",R.drawable.face13_perfil_no_valorable,"Registrar como no valorable cuando la observación no permita una clasificación fiable.")
   }}
   item{Pick("Exostosis craneal · identificación",listOf("No se observa","Frontal","Parietal","Occipital","Temporal / mastoidea","Múltiple","No valorable"),"Exostosis = prominencia ósea localizada. Selecciona la región observada o palpada; este hallazgo por sí solo no establece la causa.")}
   item{Pick("Exostosis craneal · aspecto frecuente",listOf("Prominencia frontal localizada","Prominencia parietal localizada","Prominencia occipital localizada","Prominencia mastoidea / temporal","Prominencias múltiples","No valorable"),"Al seleccionar una opción se muestran ejemplos anatómicos frecuentes por localización; distinguir una variante/prominencia ósea de una masa de tejidos blandos requiere exploración clínica.")}
   item{Pick("Hundimiento craneal · identificación",listOf("No se observa","Frontal","Parietal","Temporal","Occipital","Múltiple","No valorable"),"Hundimiento = depresión o pérdida aparente del contorno craneal. Registrar localización, simetría y antecedente traumático o quirúrgico cuando corresponda.")}
   item{Pick("Hundimiento craneal · aspecto frecuente",listOf("Depresión frontal","Depresión parietal","Depresión temporal","Depresión occipital","Depresión posquirúrgica / postraumática","No valorable"),"Las depresiones pueden corresponder a anatomía individual, secuela traumática o posquirúrgica, entre otras causas. Una selección educativa no equivale a diagnóstico.")}
   // Las imágenes de cráneo, patrón facial, forma de cara y tez se muestran sólo al seleccionar su opción, igual que en Hábitos y parafunciones.
  }
  if(section=="Músculos"){
   val facialManeuvers=listOf(
    "Abrir la boca" to R.drawable.new77_evaluacion_de_musculos_faciales_abriendo_la_boca,
    "Arrugar la nariz" to R.drawable.new77_evaluacion_de_musculos_faciales_arrugando_la_nariz,
    "Cerrar los ojos fuerte" to R.drawable.new77_evaluacion_de_musculos_faciales_cerrando_los_ojos_fuerte,
    "Elevar las cejas" to R.drawable.new77_evaluacion_de_musculos_faciales_elevando_las_cejas,
    "Expresión de enojo" to R.drawable.new77_evaluacion_de_musculos_faciales_enojandose,
    "Fruncir el ceño" to R.drawable.new77_evaluacion_de_musculos_faciales_frunciendo_el_cen_o,
    "Inflar cachetes" to R.drawable.new77_evaluacion_de_musculos_faciales_inflando_cachetes,
    "Mandar besos" to R.drawable.new77_evaluacion_de_musculos_faciales_mandando_besos,
    "Sacar la lengua" to R.drawable.new77_evaluacion_de_musculos_faciales_sancando_la_laengua,
    "Silbar" to R.drawable.new77_evaluacion_de_musculos_faciales_silvando,
    "Sonreír mostrando los dientes" to R.drawable.new77_evaluacion_de_musculos_faciales_sonreir_mostrando_los_dientes,
    "Sonreír" to R.drawable.new77_evaluacion_de_musculos_faciales_sonriendo,
    "Sonreír exageradamente" to R.drawable.new77_evaluacion_de_musculos_faciales_sonriendocexageradamente,
    "Succionar cachetes" to R.drawable.new77_evaluacion_de_musculos_faciales_succionando_sus_cachetes
   )
   item{Pick("Maniobra de músculos faciales",facialManeuvers.map{it.first},"Qué es: una prueba breve del movimiento facial. Por qué importa: permite comparar ambos lados y registrar asimetría, debilidad o limitación observada.")}
   val facialVisual=facialManeuvers.firstOrNull{it.first==selected["Maniobra de músculos faciales"]}
   if(facialVisual!=null)item{
    LocalClinicalInlineZoomImageV48(lang,facialVisual.first,facialVisual.first,facialVisual.second,
     "Imagen correspondiente a la maniobra seleccionada. Compara ambos lados y registra el hallazgo clínico.",
     "Image corresponding to the selected maneuver. Compare both sides and record the clinical finding.")
   }
   item{Pick("Expresión facial · inspección",listOf("Simetría conservada","Asimetría al sonreír","Asimetría al fruncir ceño","Asimetría al cerrar ojos","Asimetría al inflar mejillas","Debilidad aparente","No valorable"),"Evalúa en reposo y durante las maniobras seleccionadas. Compara ambos lados.")}
   item{Pick("Músculos de la expresión · tono/función",listOf("Función aparentemente conservada","Hipotonía aparente","Hipertonía aparente","Movimiento involuntario","Dolor referido","No valorable"))}

   val masticatoryMuscles=listOf(
    "Temporal" to R.drawable.new77_exploacion_fisica_y_extraoral_musculos_temporal,
    "Masetero" to R.drawable.new77_exploacion_fisica_y_extraoral_musculos_masetero,
    "Pterigoideo interno" to R.drawable.new77_exploacion_fisica_y_extraoral_musculos_pterigoideo_interno,
    "Pterigoideo externo" to R.drawable.new77_exploacion_fisica_y_extraoral_musculos_pterigoideo_externo7
   )
   item{Pick("Músculo masticatorio · localización",masticatoryMuscles.map{it.first},"Qué es: localización de los principales músculos de la masticación. Por qué importa: al seleccionarlo se muestra su referencia anatómica para relacionar la exploración con dolor, asimetría o limitación funcional.")}
   val muscleVisual=masticatoryMuscles.firstOrNull{it.first==selected["Músculo masticatorio · localización"]}
   if(muscleVisual!=null)item{
    LocalClinicalInlineZoomImageV48(lang,muscleVisual.first,muscleVisual.first,muscleVisual.second,
     "Imagen anatómica local del músculo seleccionado. Correlaciona con palpación y maniobras clínicas.",
     "Local anatomical image of the selected muscle. Correlate with palpation and clinical maneuvers.")
   }
   item{Pick("Temporal",listOf("Sin dolor","Dolor derecho","Dolor izquierdo","Dolor bilateral","Hipertrofia/asimetría","No valorable"),"Qué es: músculo masticatorio de la región temporal que participa principalmente en elevación y retrusión mandibular. Por qué importa: dolor reproducible, asimetría o hipertrofia deben correlacionarse con palpación, movimientos mandibulares y síntomas.")}
   if(selected["Temporal"]!=null && selected["Temporal"]!="No valorable")item{
    LocalClinicalInlineZoomImageV48(lang,"Temporal","Temporal",R.drawable.new77_exploacion_fisica_y_extraoral_musculos_temporal,
     "Referencia anatómica local del temporal. Palpa comparativamente ambos lados y relaciona el hallazgo con dolor, volumen y función mandibular.",
     "Local anatomical reference of the temporalis. Compare both sides and correlate the finding with pain, volume and mandibular function.")
   }
   item{Pick("Masetero",listOf("Sin dolor","Dolor derecho","Dolor izquierdo","Dolor bilateral","Hipertrofia/asimetría","No valorable"),"Qué es: músculo masticatorio superficial potente que participa principalmente en la elevación mandibular. Por qué importa: dolor, aumento de volumen o asimetría deben compararse bilateralmente y correlacionarse con función y parafunciones.")}
   if(selected["Masetero"]!=null && selected["Masetero"]!="No valorable")item{
    LocalClinicalInlineZoomImageV48(lang,"Masetero","Masetero",R.drawable.new77_exploacion_fisica_y_extraoral_musculos_masetero,
     "Referencia anatómica local del masetero. Compara ambos lados durante reposo y contracción y registra dolor, asimetría o hipertrofia.",
     "Local anatomical reference of the masseter. Compare both sides at rest and during contraction and record pain, asymmetry or hypertrophy.")
   }
   item{Pick("Pterigoideos / función clínica",listOf("Sin hallazgos aparentes","Dolor reproducible en maniobra","Limitación funcional","No valorable"),"Qué es: valoración funcional orientativa de músculos pterigoideos profundos relacionados con movimientos mandibulares. Por qué importa: dolor reproducible o limitación puede aportar información al examen masticatorio, pero no debe atribuirse dolor inespecífico a un músculo profundo sin sustento clínico. Correlacionar con movimientos contra resistencia.")}
   if(selected["Pterigoideos / función clínica"]!=null && selected["Pterigoideos / función clínica"]!="No valorable")item{
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)){
     LocalClinicalInlineZoomImageV48(lang,"Pterigoideo interno","Pterigoideo interno",R.drawable.new77_exploacion_fisica_y_extraoral_musculos_pterigoideo_interno,
      "Referencia anatómica local del pterigoideo interno. Relaciona anatomía y función sin sustituir la valoración clínica.",
      "Local anatomical reference of the medial pterygoid. Relate anatomy and function without replacing clinical assessment.")
     LocalClinicalInlineZoomImageV48(lang,"Pterigoideo externo","Pterigoideo externo",R.drawable.new77_exploacion_fisica_y_extraoral_musculos_pterigoideo_externo7,
      "Referencia anatómica local del pterigoideo externo. Correlaciona con lateralidades, protrusión, apertura y síntomas.",
      "Local anatomical reference of the lateral pterygoid. Correlate with lateral movements, protrusion, opening and symptoms.")
    }
   }
  }
  if(section=="Cuello"){
   item{NoticeCard("Explora el cuello de forma descriptiva: postura, simetría, movilidad, dolor, aumentos de volumen y región tiroidea. Los hallazgos orientan la exploración, pero no establecen por sí solos una causa o diagnóstico.")}
   val cervicalPostureImages=listOf(
    "Equilibrada" to R.drawable.allimg_083_postura_cervical_equilibrada,
    "Cabeza adelantada" to R.drawable.allimg_077_postura_cervical_cabeza_adelaantada,
    "Flexión" to R.drawable.allimg_080_postura_cervical_cabeza_flexion,
    "Extensión" to R.drawable.allimg_079_postura_cervical_cabeza_extension,
    "Rectificación cervical aparente" to R.drawable.allimg_082_postura_cervical_cabeza_rectificacion,
    "Lordosis cervical aumentada aparente" to R.drawable.allimg_081_postura_cervical_cabeza_lordosis_cervical_aum,
    "Cifosis / postura cifótica aparente" to R.drawable.allimg_078_postura_cervical_cabeza_cifosis
   )
   item{Pick("Postura cervical",cervicalPostureImages.map{it.first}+listOf("No valorable"),"Qué es: posición observable de cabeza y cuello en relación con el tronco. Por qué importa: cambios posturales pueden acompañar compensaciones musculares o limitación funcional y deben correlacionarse con síntomas y exploración.")}
   val cervicalPostureVisual=cervicalPostureImages.firstOrNull{it.first==selected["Postura cervical"]}
   if(cervicalPostureVisual!=null)item{
    LocalClinicalInlineZoomImageV48(lang,cervicalPostureVisual.first,cervicalPostureVisual.first,cervicalPostureVisual.second,
     "Imagen local correspondiente a la postura seleccionada. Úsala como referencia descriptiva y correlaciona con la exploración clínica; la imagen por sí sola no establece un diagnóstico.",
     "Local image corresponding to the selected posture. Use it as a descriptive reference and correlate it with the clinical examination; the image alone does not establish a diagnosis.")
   }
   item{Pick("Simetría del cuello",listOf("Simétrico aparente","Asimetría derecha","Asimetría izquierda","Aumento de volumen localizado","No valorable"),"Qué es: comparación visual de ambos lados del cuello. Por qué importa: una asimetría o aumento de volumen puede señalar un hallazgo que requiere describir localización, extensión y evolución.")}
   item{Pick("Movilidad cervical",listOf("Conservada","Limitada a derecha","Limitada a izquierda","Limitada en flexión/extensión","Limitación global","Dolorosa","No valorable"),"Qué es: valoración de flexión, extensión y rotación cervical sin forzar. Por qué importa: la limitación o el dolor pueden modificar la postura y la exploración craneofacial y deben registrarse antes de maniobras que provoquen molestias.")}
   item{Pick("Dolor a exploración",listOf("Sin dolor","Derecho","Izquierdo","Bilateral","Localizado anterior","Localizado posterior","No valorable"),"Qué es: dolor referido o reproducido durante la exploración del cuello. Por qué importa: su localización y relación con movimiento o palpación ayudan a decidir qué estructuras requieren valoración adicional, sin asignar automáticamente una causa.")}
   item{Pick("Masas / aumento de volumen",listOf("No observado/palpado","Anterior","Lateral derecho","Lateral izquierdo","Posterior","Difuso","No valorable"),"Qué es: aumento de volumen observado o palpado en una región cervical. Por qué importa: debe describirse por sitio, tamaño aproximado, consistencia, movilidad, dolor y evolución; la app no asigna etiología.")}
   item{Pick("Tiroides · hallazgo clínico",listOf("Sin aumento aparente","Aumento aparente","Asimetría aparente","Nódulo/masa referida o palpable","Antecedente tiroideo sin hallazgo visible","No valorable"),"Qué es: registro descriptivo de la región tiroidea y del antecedente referido. Por qué importa: un aumento, asimetría o nódulo aparente puede requerir valoración médica; la inspección o palpación aislada no diagnostica enfermedad tiroidea.")}
  }
  if(section=="Ganglios"){
   item{NoticeCard("La exploración ganglionar registra la cadena examinada y las características palpables. Localización, tamaño, movilidad, dolor y consistencia deben interpretarse en conjunto con síntomas, infecciones, lesiones orales y contexto clínico.")}
   val ganglionImages=listOf(
    "Técnica de palpación de cadenas ganglionares" to R.drawable.allimg_099_tecnica_de_palpacion_de_cadenas_ganglionares,
    "Mapa anatómico de cadenas cervicales" to R.drawable.allimg_067_mapa_anatomico_de_cadenas_cervicales,
    "Linfadenopatía cervical" to R.drawable.clinical_cervical_nodes
   )
   item{Pick("Ganglios · imagen",ganglionImages.map{it.first},"Qué es: referencia de la técnica, localización anatómica o aspecto de linfadenopatía. Por qué importa: al seleccionar una opción se muestra únicamente la imagen clínica local correspondiente para relacionarla con la exploración registrada.")}
   val ganglionVisual=ganglionImages.firstOrNull{it.first==selected["Ganglios · imagen"]}
   if(ganglionVisual!=null)item{
    LocalClinicalInlineZoomImageV48(lang,ganglionVisual.first,ganglionVisual.first,ganglionVisual.second,
     "Imagen local correspondiente a la selección. Correlaciónala con palpación, localización, tamaño, movilidad, dolor y consistencia; no genera un diagnóstico automático.",
     "Local image corresponding to the selection. Correlate it with palpation, location, size, mobility, tenderness and consistency; it does not generate an automatic diagnosis.")
   }
   item{Pick("Cadena ganglionar",listOf("Preauriculares","Mastoideos/postauriculares","Occipitales","Submentonianos","Submandibulares","Cervicales superficiales/anterior","Cervicales profundos","Cervicales posteriores","Supraclaviculares"),"Qué es: región anatómica donde se realiza la palpación. Por qué importa: la distribución de un hallazgo orienta qué territorios de cabeza, cuello y cavidad oral deben revisarse con mayor detalle.")}
   item{Pick("Palpabilidad",listOf("No palpable","Palpable","No valorable"),"Qué es: registro de si se identifica un ganglio durante la palpación. Por qué importa: cuando es palpable deben describirse sus demás características; la palpabilidad aislada no determina una causa.")}
   item{Pick("Movilidad",listOf("Móvil","Fijo/adherido aparente","No aplica/no palpable","No valorable"),"Qué es: desplazamiento del ganglio respecto a planos vecinos durante la palpación. Por qué importa: aporta información descriptiva útil para decidir seguimiento o valoración adicional, siempre integrada con el resto de hallazgos.")}
   item{Pick("Dolor",listOf("No doloroso","Doloroso","No aplica/no palpable","No valorable"),"Qué es: sensibilidad o dolor provocado a la palpación. Por qué importa: puede acompañar procesos inflamatorios u otras condiciones, pero es un dato inespecífico y no debe interpretarse de forma aislada.")}
   item{Pick("Lateralidad ganglionar",listOf("Derecha","Izquierda","Bilateral","No aplica/no palpable","No valorable"),"Qué es: lado en el que se identifica el hallazgo. Por qué importa: registrar si es unilateral o bilateral ayuda a describir el patrón y correlacionarlo con hallazgos orales, faciales y cervicales.")}
   item{Pick("Tamaño aproximado",listOf("<0.5 cm","0.5–0.9 cm","1.0–1.9 cm","≥2 cm","No aplica/no palpable","No medido"),"Qué es: estimación clínica del diámetro del ganglio palpable. Por qué importa: permite documentar y comparar evolución; el tamaño aislado no confirma benignidad ni malignidad y depende de localización y contexto.")}
   item{Pick("Consistencia",listOf("Blanda","Elástica","Firme","Dura","No aplica/no palpable","No valorable"),"Qué es: sensación obtenida durante la palpación. Por qué importa: blandura, elasticidad, firmeza o dureza son datos descriptivos que deben correlacionarse con sitio, tamaño, movilidad, dolor, evolución y antecedentes; un solo dato no establece etiología.")}
  }

  item{Button(onClick={},modifier=Modifier.fillMaxWidth()){Text("💾 Guardar exploración")}}
  item{NoticeCard("Los valores de referencia son educativos y deben interpretarse con edad, sexo, anatomía, síntomas, técnica de medición y contexto clínico. Los hallazgos no generan un diagnóstico automático.")}
 }
}

@Composable fun HistoryOrthoV38(lang:String,onBack:()->Unit){
 var prior by rememberRecordState<Boolean?>("history.ortho.prior",null)
 val selected=rememberRecordStateMap<String,String>("history.ortho.selected")
 val sections=listOf(
  "Tratamiento previo" to listOf("Sin tratamiento previo","Brackets metálicos","Brackets estéticos","Alineadores transparentes","Aparato removible","Expansor palatino","Mantenedor de espacio","Aparato funcional/ortopédico","Cirugía ortognática asociada"),
  "Edad al tratamiento" to listOf("<6 años","6–8","9–11","12–14","15–17","18–25",">25","No recuerda"),
  "Duración" to listOf("<6 meses","6–11 meses","1–2 años","2–3 años",">3 años","En tratamiento","No recuerda"),
  "Finalización" to listOf("Completado","Suspendido por paciente","Suspendido por profesional","Interrumpido por otra causa","Actualmente activo","No recuerda"),
  "Retención" to listOf("Sin retención","Retenedor Hawley","Retenedor transparente","Retenedor fijo","Fijo + removible","No recuerda"),
  "Tiempo de retención" to listOf("<6 meses","6–12 meses","1–2 años","2–5 años",">5 años","Uso actual","No recuerda"),
  "Resultado referido" to listOf("Estable","Recidiva leve","Recidiva moderada","Recidiva importante","Insatisfecho","No sabe/no recuerda")
 )
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes ortodónticos y ortopédicos",onBack,"Registro guiado del tratamiento previo, aparatología, duración, retención y resultado referido. Las opciones se reorganizan de 2 a 4 celdas según el ancho disponible.")}
  item{SectionCard("¿Recibió tratamiento previo?"){ChipChoices(listOf("No recibió" to (prior==false),"Sí recibió" to (prior==true)),{prior=it==1},columns=2)}}
  if(prior==true) sections.forEach{(name,opts)->
   item{SectionCard(name){
    ChipChoices(opts.map{o->o to (selected[name]==o)},{i->selected[name]=opts[i]},columns=4)
    if(name=="Tratamiento previo") when(selected[name]){
     "Brackets metálicos"->ClinicalPhotoV38("Brackets metálicos",R.drawable.new77_edu_brackets_metalicos,"Imagen de la opción seleccionada.")
     "Brackets estéticos"->ClinicalPhotoV38("Brackets estéticos",R.drawable.new77_edu_brackets_esteticos,"Imagen de la opción seleccionada.")
     "Alineadores transparentes"->ClinicalPhotoV38("Alineadores transparentes",R.drawable.new77_edu_alineadores_tranparentes,"Imagen de la opción seleccionada.")
     "Aparato removible"->ClinicalPhotoV38("Aparatología removible",R.drawable.new77_edu_aparatologia_removible,"Imagen de la opción seleccionada.")
     "Expansor palatino"->ClinicalPhotoV38("Expansor de paladar",R.drawable.new77_edu_expansor_de_paladar,"Imagen de la opción seleccionada.")
     "Cirugía ortognática asociada"->ClinicalPhotoV38("Cirugía ortognática",R.drawable.new77_edu_cirugia_ortognatica,"Imagen de la opción seleccionada.")
    }
    if(name=="Retención") when(selected[name]){
     "Sin retención"->ClinicalPhotoV38("Sin retenedores",R.drawable.new77_edu_sin_retenedores,"Imagen de la opción seleccionada.")
     "Retenedor Hawley"->ClinicalPhotoV38("Retenedor removible Hawley",R.drawable.new77_edu_retendedor_removible_hawley,"Imagen de la opción seleccionada.")
     "Retenedor transparente"->ClinicalPhotoV38("Retenedor transparente",R.drawable.new77_edi_retenedor_transparente,"Imagen de la opción seleccionada.")
     "Retenedor fijo"->ClinicalPhotoV38("Retenedor fijo",R.drawable.new77_edu_retenedor_fijo,"Imagen de la opción seleccionada.")
     "Fijo + removible"->ClinicalPhotoV38("Retenedor fijo inferior y removible superior",R.drawable.new77_edu_retenedor_fijo_inferior_y_removible_superior,"Imagen de la opción seleccionada.")
    }
   }}
  }
  item{NoticeCard("El antecedente ortodóntico se registra según lo referido y lo observable. No asumir diagnóstico previo, indicación original ni estabilidad futura sin expediente, exploración y estudios.")}
 }
}

@Composable fun HistoryDentalAlterationsV38(lang:String,onBack:()->Unit){
 var group by rememberRecordState("history.dentalAlterations.group",0)
 var subgroup by rememberRecordState("history.dentalAlterations.subgroup",0)
 val selected=rememberRecordStateMap<String,String>("history.dentalAlterations.selected")
 data class Subgroup38(val name:String,val findings:List<String>)
 data class Group38(val name:String,val subs:List<Subgroup38>)
 val groups=listOf(
  Group38("1 · Anomalías de número",listOf(
   Subgroup38("Disminución del número",listOf("Sin alteración","Anodoncia","Hipodoncia","Oligodoncia")),
   Subgroup38("Aumento del número / hiperdoncia",listOf("Sin alteración","Diente supernumerario","Mesiodens","Paramolar","Distomolar / cuarto molar","Premolar supernumerario","Suplementario","Conoidal","Tuberculado"))
  )),
  Group38("2 · Anomalías de tamaño",listOf(
   Subgroup38("Disminución",listOf("Sin alteración","Microdoncia localizada","Microdoncia generalizada verdadera","Microdoncia generalizada relativa","Incisivo lateral conoide")),
   Subgroup38("Aumento",listOf("Sin alteración","Macrodoncia localizada","Macrodoncia generalizada verdadera","Macrodoncia generalizada relativa"))
  )),
  Group38("3 · Anomalías de forma o morfología",listOf(
   Subgroup38("Corona",listOf("Sin alteración","Incisivo conoide","Dens invaginatus","Dens evaginatus","Cúspide en talón","Cúspides accesorias","Tubérculo de Carabelli","Protostílido","Incisivos en pala","Surco palatorradicular")),
   Subgroup38("Raíz",listOf("Sin alteración","Dilaceración","Raíces supernumerarias / accesorias","Raíz corta","Raíces fusionadas","Curvaturas radiculares pronunciadas")),
   Subgroup38("Corona y raíz",listOf("Sin alteración","Hipotaurodontismo","Mesotaurodontismo","Hipertaurodontismo")),
   Subgroup38("Esmalte ectópico sobre la raíz",listOf("Sin alteración","Perla de esmalte","Proyección cervical de esmalte"))
  )),
  Group38("4 · Anomalías de unión/división",listOf(
   Subgroup38("Unión de gérmenes",listOf("Sin alteración","Fusión")),
   Subgroup38("Intento de división de un germen",listOf("Sin alteración","Geminación")),
   Subgroup38("Unión por cemento",listOf("Sin alteración","Concrescencia")),
   Subgroup38("Formas combinadas",listOf("Sin alteración","Fusión de diente normal con supernumerario","Diente doble"))
  )),
  Group38("5 · Anomalías de estructura",listOf(
   Subgroup38("Esmalte · cantidad",listOf("Sin alteración","Hipoplasia del esmalte","Diente de Turner")),
   Subgroup38("Esmalte · calidad/mineralización",listOf("Sin alteración","Hipomineralización","MIH / HMI","HSPM","Fluorosis","Defectos postraumáticos")),
   Subgroup38("Alteraciones hereditarias del esmalte",listOf("Sin alteración","Amelogénesis imperfecta hipoplásica","Amelogénesis imperfecta hipomadurativa","Amelogénesis imperfecta hipocalcificada / hipomineralizada","Amelogénesis imperfecta mixta")),
   Subgroup38("Dentina",listOf("Sin alteración","Dentinogénesis imperfecta","Displasia dentinaria tipo I","Displasia dentinaria tipo II")),
   Subgroup38("Esmalte + dentina",listOf("Sin alteración","Odontodisplasia regional / ghost teeth"))
  )),
  Group38("6 · Alteraciones de color",listOf(
   Subgroup38("Intrínsecas",listOf("Sin alteración","Fluorosis","MIH","Amelogénesis imperfecta","Dentinogénesis imperfecta","Tetraciclinas","Necrosis pulpar","Hemorragia pulpar","Reabsorción interna / pink spot","Hiperbilirrubinemia","Porfiria","Pigmentación por materiales / endodoncia")),
   Subgroup38("Extrínsecas",listOf("Sin alteración","Café","Té","Vino","Tabaco","Alimentos cromógenos","Placa cromógena negra","Mancha verde / naranja","Hierro","Clorhexidina","Metales","Pigmentaciones ocupacionales"))
  )),
  Group38("7 · Anomalías de erupción",listOf(
   Subgroup38("A · Cronología o tiempo",listOf("Sin alteración","Erupción precoz / adelantada","Erupción prematura","Dientes natales","Dientes neonatales","Erupción retardada localizada","Erupción retardada generalizada")),
   Subgroup38("B · Posición, trayectoria o dirección",listOf("Sin alteración","Erupción ectópica","Transposición dentaria","Transmigración","Erupción vestibular","Erupción lingual / palatina","Erupción mesial anómala","Erupción distal anómala","Rotación")),
   Subgroup38("C · Detención o fracaso eruptivo",listOf("Sin alteración","Impactación","Inclusión","Retención primaria","Retención secundaria","Fallo primario de erupción (PFE)","Fallo mecánico de erupción","Anquilosis","Infraoclusión")),
   Subgroup38("D · Asociadas a la erupción",listOf("Sin alteración","Quiste de erupción","Hematoma de erupción","Secuestro eruptivo","Pericoronitis","Retención prolongada del temporal","Sobreerupción"))
  ))
 )
 val quickFindings=listOf("Mancha blanca","Mancha marrón","Mancha negra","Pérdida de estructura")
 var quickFinding by rememberRecordState("history.dentalAlterations.quickFinding","")
 val quickRelated=when(quickFinding){
  "Mancha blanca"->listOf("Lesión de mancha blanca activa","Lesión de mancha blanca inactiva","Mancha blanca temporal por deshidratación","Hipoplasia del esmalte","Hipomineralización","MIH / HMI","Fluorosis","Amelogénesis imperfecta")
  "Mancha marrón"->listOf("Fluorosis","MIH / HMI","Hipoplasia del esmalte","Dentinogénesis imperfecta","Tetraciclinas","Café","Té","Tabaco","Hierro","Clorhexidina","Pigmentación por materiales / endodoncia")
  "Mancha negra"->listOf("Caries activa","Caries inactiva","Caries cavitada","Placa cromógena negra","Tabaco","Hierro","Metales","Pigmentaciones ocupacionales")
  "Pérdida de estructura"->listOf("Atrición","Abrasión","Erosión","Abfracción","Desgaste generalizado","Desgaste oclusal severo","Caries cavitada","Fractura / trauma")
  else->emptyList()
 }
 val quickSelected=rememberRecordStateMap<String,String>("history.dentalAlterations.quickSelected")
 val safeGroup=group.coerceIn(0,groups.lastIndex)
 val g=groups[safeGroup]
 val safeSub=subgroup.coerceIn(0,g.subs.lastIndex)
 val s=g.subs[safeSub]
  val finding=selected[g.name+"|"+s.name]
  val anomalyImages=when(finding){
   "Anodoncia"->listOf(R.drawable.anomaly49_02)
   "Hipodoncia"->listOf(R.drawable.anomaly49_28)
   "Oligodoncia"->listOf(R.drawable.anomaly49_37)
   "Mesiodens"->listOf(R.drawable.anomaly49_26)
   "Paramolar"->listOf(R.drawable.anomaly49_06)
   "Distomolar / cuarto molar"->listOf(R.drawable.anomaly49_05)
   "Premolar supernumerario"->listOf(R.drawable.anomaly49_07)
   "Suplementario"->listOf(R.drawable.anomaly49_24)
   "Conoidal"->listOf(R.drawable.anomaly49_04)
   "Tuberculado"->listOf(R.drawable.anomaly49_08)
   "Microdoncia localizada"->listOf(R.drawable.anomaly49_34,R.drawable.anomaly49_35)
   "Microdoncia generalizada verdadera"->listOf(R.drawable.anomaly49_33)
   "Microdoncia generalizada relativa"->listOf(R.drawable.anomaly49_32)
   "Incisivo lateral conoide"->listOf(R.drawable.anomaly49_30)
   "Macrodoncia localizada"->listOf(R.drawable.anomaly49_15)
   "Macrodoncia generalizada verdadera"->listOf(R.drawable.anomaly49_14)
   "Incisivo conoide"->listOf(R.drawable.anomaly49_13)
   "Dens invaginatus"->listOf(R.drawable.anomaly49_23)
   "Dens evaginatus"->listOf(R.drawable.anomaly49_22)
   "Cúspide en talón"->listOf(R.drawable.anomaly49_19)
   "Cúspides accesorias"->listOf(R.drawable.anomaly49_20)
   "Tubérculo de Carabelli"->listOf(R.drawable.anomaly49_49)
   "Protostílido"->listOf(R.drawable.anomaly49_41)
   "Incisivos en pala"->listOf(R.drawable.anomaly49_36)
   "Surco palatorradicular"->listOf(R.drawable.anomaly49_47)
   "Dilaceración"->listOf(R.drawable.anomaly49_09)
   "Raíces supernumerarias / accesorias"->listOf(R.drawable.anomaly49_45)
   "Raíz corta"->listOf(R.drawable.anomaly49_43)
   "Raíces fusionadas"->listOf(R.drawable.anomaly49_44)
   "Curvaturas radiculares pronunciadas"->listOf(R.drawable.anomaly49_18)
   "Hipotaurodontismo"->listOf(R.drawable.anomaly49_29,R.drawable.anomaly49_48)
   "Mesotaurodontismo"->listOf(R.drawable.anomaly49_31,R.drawable.anomaly49_48)
   "Hipertaurodontismo"->listOf(R.drawable.anomaly49_27,R.drawable.anomaly49_48)
   "Perla de esmalte"->listOf(R.drawable.anomaly49_17)
   "Proyección cervical de esmalte"->listOf(R.drawable.anomaly49_42)
   "Fusión"->listOf(R.drawable.anomaly49_11)
   "Geminación"->listOf(R.drawable.anomaly49_25)
   "Concrescencia"->listOf(R.drawable.anomaly49_03)
   "Fusión de diente normal con supernumerario"->listOf(R.drawable.anomaly49_10)
   "HSPM"->listOf(R.drawable.anomaly49_12)
   "Defectos postraumáticos"->listOf(R.drawable.anomaly49_21)
   "Amelogénesis imperfecta hipoplásica","Amelogénesis imperfecta hipomadurativa","Amelogénesis imperfecta hipocalcificada / hipomineralizada","Amelogénesis imperfecta mixta","Amelogénesis imperfecta"->listOf(R.drawable.anomaly49_01)
   "Dentinogénesis imperfecta"->listOf(R.drawable.anomaly_dentinogenesis_imperfecta)
   "Odontodisplasia regional / ghost teeth"->listOf(R.drawable.anomaly49_16)
   "Tetraciclinas"->listOf(R.drawable.anomaly49_40)
   "Reabsorción interna / pink spot"->listOf(R.drawable.anomaly49_46)
   "Tabaco"->listOf(R.drawable.anomaly49_39)
   "Clorhexidina"->listOf(R.drawable.anomaly49_38)
   "Erupción precoz / adelantada","Erupción prematura","Dientes natales","Dientes neonatales","Erupción retardada localizada","Erupción retardada generalizada"->listOf(R.drawable.anomaly_erupcion_cronologia)
   "Erupción ectópica","Transposición dentaria","Transmigración","Erupción vestibular","Erupción lingual / palatina","Erupción mesial anómala","Erupción distal anómala","Rotación"->listOf(R.drawable.anomaly_erupcion_posicion)
   "Impactación","Inclusión","Retención primaria","Retención secundaria","Fallo primario de erupción (PFE)","Fallo mecánico de erupción","Anquilosis","Infraoclusión"->listOf(R.drawable.anomaly_erupcion_fracaso)
   else->emptyList()
  }
 var anomalyImageIndex by remember(finding){mutableStateOf(0)}
 val safeAnomalyImageIndex=if(anomalyImages.isEmpty())0 else anomalyImageIndex.coerceIn(0,anomalyImages.lastIndex)
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Anomalías dentales",onBack,"Clasificación por número, tamaño, forma, unión/división, estructura, color y erupción. Selecciona primero el grupo y después la subclasificación.")}
  item{SectionCard("Opciones rápidas"){
   Text("Acceso por apariencia clínica. Selecciona el hallazgo inicial para desplegar posibilidades relacionadas; después confirma el diagnóstico en la clasificación completa.",fontWeight=FontWeight.SemiBold)
   ChipChoices(quickFindings.map{x->x to (quickFinding==x)},{i->quickFinding=quickFindings[i]},columns=2)
   if(quickRelated.isNotEmpty()){
    Text("Relacionadas con: "+quickFinding,fontWeight=FontWeight.SemiBold)
    ChipChoices(quickRelated.map{x->x to (quickSelected[quickFinding]==x)},{i->quickSelected[quickFinding]=quickRelated[i]},columns=2)
    when(quickSelected[quickFinding]){
    }
   }
  }}
  item{SectionCard("1 · Grupo principal"){ChipChoices(groups.mapIndexed{i,x->x.name to (safeGroup==i)},{i->group=i;subgroup=0},columns=2)}}
  item{SectionCard("2 · Subclasificación"){ChipChoices(g.subs.mapIndexed{i,x->x.name to (safeSub==i)},{i->subgroup=i},columns=2)}}
  item{SectionCard("3 · Anomalía / hallazgo"){
   ChipChoices(s.findings.map{x->x to (selected[g.name+"|"+s.name]==x)},{i->selected[g.name+"|"+s.name]=s.findings[i]},columns=3)
   if(anomalyImages.isNotEmpty()){
    if(anomalyImages.size>1){
     Text("Selecciona una imagen",fontWeight=FontWeight.SemiBold)
     ChipChoices(anomalyImages.indices.map{i->"Imagen "+(i+1) to (safeAnomalyImageIndex==i)},{i->anomalyImageIndex=i},columns=2)
    }
    val res=anomalyImages[safeAnomalyImageIndex]
    LocalClinicalInlineZoomImageV48(lang,(finding?:"Anomalía")+" · imagen "+(safeAnomalyImageIndex+1),(finding?:"Dental anomaly")+" · image "+(safeAnomalyImageIndex+1),res,
     "Imagen local correspondiente al hallazgo seleccionado. Úsala junto con los criterios clínicos y radiográficos; no genera diagnóstico automático.",
     "Local image corresponding to the selected finding. Use it with clinical and radiographic criteria; it does not generate an automatic diagnosis.")
   }
  }}
  val extension=listOf("Un diente","Varios dientes","Localizado por cuadrante","Generalizado","No valorable")
  item{SectionCard("4 · Extensión"){ChipChoices(extension.map{x->x to (selected["Extensión"]==x)},{i->selected["Extensión"]=extension[i]},columns=3)}}
  val confirmation=listOf("Sólo clínica","Clínica + radiografía","Antecedente documentado","Requiere estudio complementario","No aplica")
  item{SectionCard("5 · Confirmación disponible"){ChipChoices(confirmation.map{x->x to (selected["Confirmación"]==x)},{i->selected["Confirmación"]=confirmation[i]},columns=3)}}
  item{NoticeCard("Las categorías organizan el registro educativo y no generan diagnóstico automático. Correlaciona los hallazgos con historia clínica, exploración y estudios apropiados. Las imágenes locales aparecen únicamente al seleccionar el hallazgo correspondiente.")}
 }
}

@Composable fun HistoryHabitsV38(lang:String,onBack:()->Unit){
 data class Habit38(val id:String,val name:String,val description:String,val observe:String)
 val habits=listOf(
  Habit38("suction","Succión digital","Introducción repetida de uno o más dedos en la boca. Registra dedo, edad de inicio, frecuencia, duración e intensidad.","Observa postura labial/facial, incisivos, overjet, mordida abierta, forma de arco y paladar."),
  Habit38("pacifier","Chupón o mamila prolongados","Uso persistente de chupón o mamila más allá del periodo esperado para alimentación/consolación. Importan edad, duración diaria y si continúa durante el sueño.","Observa postura labial, relación incisiva, mordida abierta, forma de arco y patrón eruptivo."),
  Habit38("mouthbreathing","Respiración oral","Patrón referido de respiración predominante por la boca. La app registra el hallazgo; la causa nasal, faríngea o funcional requiere valoración específica.","Observa labios entreabiertos, sequedad, gingivitis, postura y patrón oclusal."),
  Habit38("tongue","Interposición lingual / deglución atípica","Posición o movimiento lingual que se interpone entre arcadas durante reposo o deglución. Debe valorarse funcionalmente, no sólo por apariencia.","Observa deglución, postura lingual, competencia labial, mordida abierta y espacios."),
  Habit38("nail","Onicofagia / mordisqueo","Morder uñas, labios, carrillos u objetos de manera repetitiva. Registra frecuencia, momento del día y estructuras involucradas.","Observa uñas/labios, desgaste, microfracturas, trauma mucoso o recesión localizada."),
  Habit38("bruxism","Bruxismo y apretamiento","Actividad masticatoria repetitiva referida durante sueño o vigilia, con rechinamiento, apretamiento o empuje mandibular. Ningún signo aislado confirma el diagnóstico.","Pregunta sueño/vigilia, fatiga y dolor; observa músculos, facetas, fracturas, restauraciones y línea alba.")
 )
 val present=rememberRecordStateMap<String,Boolean>("history.habits.present")
 val frequency=rememberRecordStateMap<String,String>("history.habits.frequency")
 val duration=rememberRecordStateMap<String,String>("history.habits.duration")
 var openId by rememberRecordState("history.habits.open","")
 val freqOpts=listOf("Ocasional","1–2 días/semana","3–4 días/semana","5–6 días/semana","Diario","Varias veces al día","No sabe")
 val durationOpts=listOf("<1 mes","1–6 meses","7–12 meses","1–2 años","3–5 años",">5 años","Desde infancia","No sabe")
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Hábitos y parafunciones",onBack,"Selecciona los hábitos presentes. Las opciones se organizan en 2–3 celdas y cada una incluye explicación, qué observar y una fotografía local opcional asociada al expediente.")}
  item{SectionCard("Panorama de hábitos orales"){
   Text("Toca el recuadro para abrir la imagen general; después selecciona el hábito referido para ver su explicación e imagen específica.",style=MaterialTheme.typography.bodySmall)
   LocalClinicalHelpImageV47(lang,"Panorama de hábitos orales","Oral habits overview",R.drawable.edu_habitos_orales,"Imagen general de los principales hábitos y parafunciones.","General image of common oral habits and parafunctions.")
  }}
  item{SectionCard("Hábitos referidos"){
   ChipChoices(habits.map{h->h.name to (present[h.id]==true)},{i->
    val h=habits[i]
    val newValue=!(present[h.id]?:false)
    present[h.id]=newValue
    openId=if(newValue)h.id else if(openId==h.id)"" else openId
   },columns=3)
  }}
  habits.forEach{h->
   if(present[h.id]==true){
    item{Card(Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){
     Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){
      Text(h.name,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f))
      TextButton(onClick={openId=if(openId==h.id)"" else h.id}){Text(if(openId==h.id)"Ocultar" else "Ver detalles")}
     }
     if(openId==h.id){
      Text("¿Qué es?",fontWeight=FontWeight.SemiBold);Text(h.description)
      Text("¿Qué observar?",fontWeight=FontWeight.SemiBold);Text(h.observe)
      val habitVisual=when(h.id){
       "suction"->Triple(R.drawable.edu_habito_succion_digital,"Succión digital","Imagen asociada a succión digital")
       "pacifier"->Triple(R.drawable.edu_habito_chupon,"Chupón","Imagen asociada al uso de chupón")
       "mouthbreathing"->Triple(R.drawable.edu_habito_respiracion_oral,"Respiración oral","Imagen asociada a respiración oral")
       "tongue"->Triple(R.drawable.edu_habito_interposicion_lingual,"Interposición lingual","Imagen asociada a interposición lingual")
       "nail"->Triple(R.drawable.edu_habito_onicofagia,"Onicofagia / mordisqueo","Imagen de hábito oral")
       "bruxism"->Triple(R.drawable.edu_habito_bruxismo,"Bruxismo / apretamiento","Imagen; ningún signo aislado confirma bruxismo")
       else->null
      }
      habitVisual?.let{v->
       LocalClinicalInlineZoomImageV48(lang,v.second,v.second,v.first,v.third,v.third)
      }
      if(h.id=="pacifier") LocalClinicalHelpImageV47(lang,"Mamila prolongada","Prolonged bottle use",R.drawable.edu_habito_mamila,"Imagen complementaria asociada al uso prolongado de mamila.","Additional image associated with prolonged bottle use.")
      if(h.id=="nail") LocalClinicalHelpImageV47(lang,"Mordisqueo labial","Lip biting",R.drawable.edu_habito_mordisqueo_labial,"Imagen de mordisqueo labial.","Image of lip biting.")
      Text("Frecuencia",fontWeight=FontWeight.SemiBold)
      ChipChoices(freqOpts.map{x->x to (frequency[h.id]==x)},{i->frequency[h.id]=freqOpts[i]},columns=3)
      Text("Tiempo de evolución / duración",fontWeight=FontWeight.SemiBold)
      ChipChoices(durationOpts.map{x->x to (duration[h.id]==x)},{i->duration[h.id]=durationOpts[i]},columns=3)
      HabitPhotoPickerV38(h.id,h.name)
     }
    }}}
   }
  }
  item{NoticeCard("Las fotografías seleccionadas son archivos locales elegidos por el usuario y se asocian al expediente mediante su URI persistente. La fotografía documenta el aspecto observado; no sustituye exploración ni establece por sí sola un diagnóstico.")}
 }
}

@Composable private fun HabitPhotoPickerV38(id:String,title:String){
 val context=LocalContext.current
 var uriString by rememberRecordState("history.habits.photo.$id","")
 val launcher=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){uri->
  if(uri!=null){
   runCatching{context.contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION)}
   uriString=uri.toString()
  }
 }
 val bitmap=remember(uriString){
  if(uriString.isBlank()) null else runCatching{
   context.contentResolver.openInputStream(Uri.parse(uriString)).use{input->BitmapFactory.decodeStream(input)?.asImageBitmap()}
  }.getOrNull()
 }
 SectionCard("Fotografía local · $title"){
  Text("Puedes vincular una fotografía clínica tomada/guardada en el dispositivo para documentar cómo se observa este hábito.",style=MaterialTheme.typography.bodySmall)
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
   Button(onClick={launcher.launch(arrayOf("image/*"))}){Text(if(uriString.isBlank())"Seleccionar foto" else "Cambiar foto")}
   if(uriString.isNotBlank())OutlinedButton(onClick={uriString=""}){Text("Quitar")}
  }
  bitmap?.let{Image(it,"Fotografía local de $title",Modifier.fillMaxWidth().heightIn(min=160.dp,max=320.dp),contentScale=ContentScale.Fit)}
  if(uriString.isNotBlank() && bitmap==null)Text("La imagen vinculada no está disponible actualmente en el dispositivo.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.error)
 }
}

private fun OralSiteImagesV50HasRefs(site:String)=site in setOf("Labio superior","Labio inferior","Carrillo derecho / mucosa bucal","Carrillo izquierdo / mucosa bucal","Piso de boca","Paladar duro","Paladar blando","Orofaringe / pared posterior","Úvula","Amígdala derecha","Amígdala izquierda","Lengua · dorso","Lengua · bordes laterales","Lengua · cara ventral")

@Composable private fun OralSiteImagesV50(lang:String,site:String){
 val refs=when(site){
  "Labio superior"->listOf(
   Triple(R.drawable.allimg_089_quelitis_irritativa_labio_superior,"Queilitis irritativa · labio superior","Imagen clínica local de un hallazgo documentado."),
   Triple(R.drawable.allimg_090_quelitis_traumatica_labio_superior,"Queilitis traumática · labio superior","Imagen clínica local de un hallazgo documentado.")
  )
  "Labio inferior"->listOf(
   Triple(R.drawable.allimg_058_lesion_por_mordisueo_labio_inferior,"Lesión por mordisqueo · labio inferior","Imagen clínica local de un hallazgo documentado."),
   Triple(R.drawable.allimg_088_quelitis_irritativa_labio_inferior,"Queilitis irritativa · labio inferior","Imagen clínica local de un hallazgo documentado."),
   Triple(R.drawable.allimg_043_fibroma_traumatico_labio,"Fibroma traumático · labio","Imagen clínica local; correlacionar con la exploración.")
  )
  "Carrillo derecho / mucosa bucal","Carrillo izquierdo / mucosa bucal"->listOf(
   Triple(R.drawable.allimg_062_linea_laba_carrillo,"Línea alba · carrillo","Imagen clínica local."),
   Triple(R.drawable.allimg_069_morsicatio_o_mordisque_carrillo,"Morsicatio / mordisqueo · carrillo","Imagen clínica local."),
   Triple(R.drawable.allimg_103_ulcera_traumatica_carrillos,"Úlcera traumática · carrillo","Imagen clínica local."),
   Triple(R.drawable.allimg_045_fibroma_traumatico_carrillo,"Fibroma traumático · carrillo","Imagen clínica local."),
   Triple(R.drawable.allimg_064_liquen_plano_oral_carrillos,"Liquen plano oral · carrillos","Imagen clínica local; correlacionar con evaluación clínica.")
  )
  "Piso de boca"->listOf(
   Triple(R.drawable.allimg_093_ranula_piso_de_boca,"Ránula · piso de boca","Imagen clínica local; describir el hallazgo y correlacionarlo clínicamente."),
   Triple(R.drawable.allimg_092_quiste_piso_de_boca,"Lesión quística · piso de boca","Imagen clínica local; la imagen no establece diagnóstico automático."),
   Triple(R.drawable.allimg_059_lesion_vascular_piso_de_boca,"Lesión vascular · piso de boca","Imagen clínica local."),
   Triple(R.drawable.allimg_106_ulcera_traumatica_piso_de_boca,"Úlcera traumática · piso de boca","Imagen clínica local.")
  )
  "Paladar duro"->listOf(
   Triple(R.drawable.allimg_016_candidiasis_paladar,"Candidiasis · paladar","Imagen clínica local; correlacionar con exploración y antecedentes."),
   Triple(R.drawable.allimg_105_ulcera_traumatica_paladar,"Úlcera traumática · paladar","Imagen clínica local.")
  )
  "Paladar blando"->listOf(
   Triple(R.drawable.allimg_015_candidiasis_paladar_blando,"Candidiasis · paladar blando","Imagen clínica local; correlacionar con exploración y antecedentes."),
   Triple(R.drawable.allimg_032_eritema_inflaatorio_paladar_blando,"Eritema inflamatorio · paladar blando","Imagen clínica local."),
   Triple(R.drawable.allimg_076_petquias_paladar_blando,"Petequias · paladar blando","Imagen clínica local.")
  )
  "Orofaringe / pared posterior"->listOf(
   Triple(R.drawable.allimg_005_afta_de_bednar_orofaringe,"Afta de Bednar · orofaringe","Imagen clínica local."),
   Triple(R.drawable.allimg_102_ulcera_orofaringe,"Úlcera · orofaringe","Imagen clínica local.")
  )
  "Úvula"->listOf(
   Triple(R.drawable.allimg_107_ulcera_uvula,"Úlcera · úvula","Imagen clínica local.")
  )
  "Amígdala derecha","Amígdala izquierda"->listOf(
   Triple(R.drawable.allimg_009_asimetria_amigdalina,"Asimetría amigdalina","Imagen clínica local para comparación bilateral."),
   Triple(R.drawable.allimg_039_exudado_amigdalino,"Exudado amigdalino","Imagen clínica local."),
   Triple(R.drawable.allimg_051_hipertrofia_amigdalina,"Hipertrofia amigdalina","Imagen clínica local; el aspecto aislado no establece etiología.")
  )
  "Lengua · dorso"->listOf(
   Triple(R.drawable.allimg_052_lengua_fisurada,"Lengua fisurada","Imagen clínica local."),
   Triple(R.drawable.allimg_053_lengua_geografica,"Lengua geográfica","Imagen clínica local."),
   Triple(R.drawable.allimg_054_lengua_saburral,"Lengua saburral","Imagen clínica local."),
   Triple(R.drawable.allimg_014_candidiasis_lengua,"Candidiasis · lengua","Imagen clínica local; correlacionar con exploración y antecedentes.")
  )
  "Lengua · bordes laterales","Lengua · cara ventral"->listOf(
   Triple(R.drawable.allimg_104_ulcera_traumatica_lengua,"Úlcera traumática · lengua","Imagen clínica local."),
   Triple(R.drawable.allimg_044_fibroma_traumatico_lengua,"Fibroma traumático · lengua","Imagen clínica local; la imagen no establece diagnóstico automático.")
  )
  else->emptyList()
 }
 refs.forEach{v->LocalClinicalInlineZoomImageV48(lang,v.second,v.second,v.first,v.third,v.third)}
}

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
 var helpSite by remember{mutableStateOf<Int?>(null)}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Examen peribucal e intrabucal / mucosas",onBack,"Exploración por sitio anatómico. Toca cada estructura para ver aspecto normal, qué observar y alteraciones seleccionables. Primero se describe el hallazgo; después se orienta el diagnóstico.")}
  item{NoticeCard("Secuencia sugerida: piel peribucal → labios y comisuras → mucosa labial/frenillos → carrillos → encía → paladares → orofaringe/úvula/pilares/amígdalas → lengua → frenillo lingual → piso de boca.")}
  items(sites.size){i->val x=sites[i];Card(onClick={selected=if(selected==i)null else i;if(selected!=i && helpSite==i)helpSite=null},modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){Text(x.n,fontWeight=FontWeight.Bold);if(selected==i){Text(x.d);Text("□ Normal / sin alteración evidente   □ Hallazgo presente   □ No valorable",style=MaterialTheme.typography.bodySmall);if(OralSiteImagesV50HasRefs(x.n)){TextButton(onClick={helpSite=if(helpSite==i)null else i}){Text(if(helpSite==i) "Ocultar referencias" else "?  Ver referencias")};if(helpSite==i)OralSiteImagesV50(lang,x.n)}}else Text("Toca para explorar",style=MaterialTheme.typography.bodySmall)}}}
  item{NoticeCard("Las referencias clínicas locales se muestran sólo cuando están disponibles para el sitio explorado y se solicitan con ?. Sirven para comparación educativa; el hallazgo debe describirse y correlacionarse con la exploración.")}
 }
}

