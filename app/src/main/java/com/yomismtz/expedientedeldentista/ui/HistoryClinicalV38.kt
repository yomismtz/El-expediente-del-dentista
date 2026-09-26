package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
 var selectedReason by remember{mutableStateOf<Int?>(null)}
 var selectedDx by remember{mutableStateOf<Int?>(null)}
 val current=selectedReason?.let{catalog[it]}
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  item{ScreenHeader("Motivo de consulta y padecimiento actual",onBack,"Módulo sin escritura libre: selecciona el motivo referido y revisa cinco diagnósticos diferenciales posibles. El motivo por sí solo no establece el diagnóstico.")}
  item{NoticeCard("Los diagnósticos mostrados son posibilidades educativas. El diagnóstico clínico requiere integrar interrogatorio, exploración y pruebas indicadas.")}
  item{SectionCard("1 · Motivo de consulta"){ChipChoices(catalog.mapIndexed{i,x->x.reason to (selectedReason==i)},{i->selectedReason=i;selectedDx=null},columns=5)}}
  current?.let { reason ->
   item{SectionCard("2 · Cinco diagnósticos diferenciales posibles"){ChipChoices(reason.dx.mapIndexed{i,x->x to (selectedDx==i)},{selectedDx=it},columns=5)}}
   item{SectionCard("3 · Selección para estudio"){
    Text("Motivo: "+reason.reason,fontWeight=FontWeight.Bold)
    Text("Posibilidad diagnóstica seleccionada: "+(selectedDx?.let{reason.dx[it]}?:"sin seleccionar"))
    Text("Confirma o descarta mediante anamnesis dirigida, exploración clínica y auxiliares/pruebas que correspondan; no conviertas esta selección en diagnóstico definitivo.",style=MaterialTheme.typography.bodySmall)
   }}
  }
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
 var category by remember{mutableStateOf<String?>(null)}
 val selected=remember{mutableStateMapOf<String,Boolean>()}
 val status=remember{mutableStateMapOf<String,String>()}
 var openHelp by remember{mutableStateOf(false)}
 val current=relatives[relative]
 val diseases=category?.let{categories[it]}?:emptyList()
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Antecedentes heredo-familiares",onBack,"Primero selecciona el familiar y después la categoría de enfermedad. Ante un positivo, amplía inicio, evolución, estado actual, medicamentos y complicaciones.")}
  item{NoticeCard("Este apartado busca antecedentes familiares que puedan aportar predisposición o contexto clínico. No significa que el paciente padezca la misma enfermedad.")}
  item{SectionCard("1 · Familiar"){
   ChipChoices(relatives.mapIndexed{i,x->x to (relative==i)},{i->relative=i;category=null},columns=3)
  }}
  item{SectionCard("2 · Categoría"){
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
 var section by remember{mutableStateOf<String?>(null)}
 var sub by remember{mutableStateOf("")}
 val chosen=remember{mutableStateMapOf<String,String>()}
 val multi=remember{mutableStateMapOf<String,Boolean>()}
 val sections=listOf("Vivienda","Higiene","Alimentación","Inmunizaciones","Hábitos")
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
  item{SectionCard("Categorías"){ChipChoices(sections.map{x->x to (section==x)},{i->section=sections[i];sub=""},columns=2)}}
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
   item{optionsCard("Higiene general · baño corporal",listOf("Menos de 1 vez/semana","1–2/semana","3–4/semana","5–6/semana","Diario","2 o más/día"))}
   item{optionsCard("Higiene bucal · cepillado dental",listOf("No se cepilla","Menos de 1 vez/día","1 vez/día","2 veces/día","3 veces/día","4 o más/día"))}
   item{optionsCard("Higiene bucal · tipo de pasta",listOf("Pasta comercial regulada/etiquetada","Pasta comercial · no sabe","Producto naturista","Producto alternativo/casero","No usa pasta","No sabe"),"Registrar lo referido. Naturista o alternativo no equivale automáticamente a seguro, eficaz ni a una pasta fluorada.")}
   item{optionsCard("Higiene bucal · hilo/interdental",listOf("Nunca","Ocasional","1–3 veces/semana","4–6 veces/semana","Diario"))}
   item{optionsCard("Higiene general · cambio de ropa",listOf("Menos de 1/semana","1–2/semana","3–4/semana","5–6/semana","Diario","Más de 1/día"))}
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
  if(section=="Hábitos"){
   item{SectionCard("Tipo de hábito"){ChipChoices(listOf("Tabaquismo","Alcohol","Drogas","Tatuajes","Perforaciones").map{x->x to (sub==x)},{i->sub=listOf("Tabaquismo","Alcohol","Drogas","Tatuajes","Perforaciones")[i]},columns=2)}}
  }
  if(section=="Hábitos" && sub=="Tabaquismo"){
   item{optionsCard("Tabaquismo · estado",listOf("Nunca","Exfumador","Actual","Exposición pasiva","No sabe"))}
   item{optionsCard("Tabaco · frecuencia",listOf("Ocasional","1–5 cigarrillos/día","6–10/día","11–20/día","Más de 20/día","No sabe"))}
   item{optionsCard("Tabaco · tiempo",listOf("<1 año","1–5 años","6–10 años","11–20 años",">20 años","No sabe"))}
   item{optionsCard("Producto",listOf("Cigarrillo","Puro","Pipa","Tabaco sin humo","Vapeador/cigarrillo electrónico","Más de uno","Otro/no sabe"))}
  }
  if(section=="Hábitos" && sub=="Alcohol"){
   item{optionsCard("Alcohol · estado",listOf("Nunca","Anteriormente","Actual","No sabe"))}
   item{optionsCard("Alcohol · frecuencia",listOf("Menos de 1/mes","1–3/mes","1/semana","2–3/semana","4–6/semana","Diario","No sabe"))}
   item{optionsCard("Alcohol · cantidad por ocasión",listOf("1 bebida","2 bebidas","3–4 bebidas","5–6 bebidas","7 o más","No sabe"))}
   item{optionsCard("Tipo habitual",listOf("Cerveza","Vino","Destilados","Bebidas preparadas","Varios","Otro/no sabe"))}
  }
  if(section=="Hábitos" && sub=="Drogas"){
   item{optionsCard("Sustancia referida",listOf("Ninguna","Cannabis","Cocaína/crack","Metanfetaminas/estimulantes","Opioides","Alucinógenos","Inhalables","Sedantes sin indicación referida","Varias","Otra/no sabe"))}
   item{optionsCard("Frecuencia",listOf("Nunca","Una vez/experimental","Menos de 1/mes","1–3/mes","1–6/semana","Diario","No sabe"))}
   item{optionsCard("Vía",listOf("Fumada/vaporizada","Oral","Intranasal","Inyectada","Inhalada","Otra/no sabe"))}
   item{optionsCard("Último consumo",listOf("<24 h","1–7 días","1–4 semanas","1–12 meses",">1 año","No recuerda/no sabe"))}
  }
  if(section=="Hábitos" && sub=="Perforaciones"){
   item{NoticeCard("Selecciona la localización referida. En perforaciones orales o periorales registra también trauma dental/gingival, inflamación, sangrado, secreción e irritación.")}
   item{optionsCard("Localización de perforación",listOf("Ninguna","Lóbulo de oreja","Cartílago de oreja","Nariz · aleta","Nariz · septum","Ceja","Labio","Lengua","Mejilla","Ombligo","Pezón","Otra","Múltiples"))}
   item{optionsCard("Antigüedad",listOf("<1 mes","1–6 meses","7–12 meses","1–5 años",">5 años","No sabe"))}
   item{optionsCard("Complicaciones referidas",listOf("Ninguna","Dolor","Inflamación","Sangrado","Infección/secreción","Trauma dental/gingival","Alergia/irritación","Otra/no sabe"))}
  }
  if(section=="Hábitos" && sub=="Tatuajes"){
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
 var section by remember{mutableStateOf("Signos vitales")}
 val selected=remember{mutableStateMapOf<String,String>()}
 var weight by remember{mutableStateOf("")};var height by remember{mutableStateOf("")}
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
 val w=weight.toDoubleOrNull();val h=height.toDoubleOrNull();val bmi=if(w!=null&&h!=null&&h>0) w/((h/100)*(h/100)) else null
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Exploración física y signos vitales",onBack,"Registro educativo. Los valores deben medirse; la app no sustituye la valoración clínica ni asigna diagnósticos automáticamente.")}
  item{SectionCard("Apartado de exploración"){
   ChipChoices(
    listOf("Signos vitales","Somatometría","Glucosa capilar","Inspección general","Cráneo y cara","Músculos","Cuello","Ganglios","ATM y dimensión vertical").map{x->x to (section==x)},
    {i->section=listOf("Signos vitales","Somatometría","Glucosa capilar","Inspección general","Cráneo y cara","Músculos","Cuello","Ganglios","ATM y dimensión vertical")[i]},
    columns=3
   )
  }}
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
   item{ClinicalPhotoV38("Dolicocefálico y braquicéfalo · comparación morfológica real",R.drawable.clinical_cranial_morphology,"Fotografía antropométrica histórica real · Popular Science Monthly, 1897–1898 · dominio público. Úsese sólo como referencia morfológica, no diagnóstica.")}
   item{Pick("Patrón facial",listOf("Dolicofacial","Mesofacial","Braquifacial","No valorable"),"Dolicofacial: cara relativamente larga y estrecha. Mesofacial: proporción intermedia. Braquifacial: cara relativamente corta y ancha.")}
   item{Pick("Simetría facial",listOf("Simétrica aparente","Asimetría derecha","Asimetría izquierda","Asimetría compleja","No valorable"))}
   item{ClinicalPhotoV38("Referencia clínica real · asimetría de expresión facial",R.drawable.clinical_facial_asymmetry,"James Heilman, MD · Wikimedia Commons · licencia abierta; publicación con consentimiento declarado por el autor.")}
  }
  if(section=="Músculos"){
   item{ClinicalPhotoV38("Expresión facial · sonrisa",R.drawable.clinical_smile,"Fotografía clínica real · Shantoo · CC0 · muestra asimetría facial durante sonrisa.")}
   item{ClinicalPhotoV38("Expresión facial · cierre ocular",R.drawable.clinical_eye_closure,"Fotografía clínica real · Benjaminginterr · CC BY-SA · maniobra de cierre ocular.")}
   item{ClinicalPhotoV38("Expresión facial · apertura oral",R.drawable.clinical_mouth_open,"Fotografía clínica real · Benjaminginterr · CC BY-SA · maniobra de apertura oral.")}
   item{Pick("Expresión facial · inspección",listOf("Simetría conservada","Asimetría al sonreír","Asimetría al fruncir ceño","Asimetría al cerrar ojos","Asimetría al inflar mejillas","Debilidad aparente","No valorable"),"Evaluar en reposo y pedir elevar cejas/fruncir ceño, cerrar ojos, sonreír/mostrar dientes e inflar mejillas. Comparar ambos lados.")}
   item{ClinicalPhotoV38("Referencia clínica real · evaluación de sonrisa en parálisis facial",R.drawable.clinical_smile,"Shantoo · Wikimedia Commons · CC0; uso educativo para ilustrar parálisis facial.")}
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
   item{ClinicalPhotoV38("Técnica de palpación ganglionar",R.drawable.clinical_node_palpation,"Fotografía clínica real de palpación ganglionar · Fernandovet · Wikimedia Commons.")}
   item{ClinicalPhotoV38("Hallazgo · linfadenopatía cervical",R.drawable.clinical_cervical_nodes,"Fotografía clínica real · Whispyhistory · CC0.")}
   item{ClinicalPhotoV38("Mapa anatómico de cadenas cervicales",R.drawable.clinical_cervical_map,"Referencia anatómica didáctica · Wikimedia Commons. No es fotografía clínica.")}
   item{Pick("Cadena ganglionar",listOf("Preauriculares","Mastoideos/postauriculares","Occipitales","Submentonianos","Submandibulares","Cervicales superficiales/anterior","Cervicales profundos","Cervicales posteriores","Supraclaviculares"))}
   item{ClinicalPhotoV38("Referencia clínica real · linfadenopatía cervical",R.drawable.clinical_cervical_nodes,"Whispyhistory · Wikimedia Commons · CC0.")}
   item{Pick("Palpabilidad",listOf("No palpable","Palpable","No valorable"))}
   item{Pick("Movilidad",listOf("Móvil","Fijo/adherido aparente","No aplica/no palpable","No valorable"))}
   item{Pick("Dolor",listOf("No doloroso","Doloroso","No aplica/no palpable","No valorable"))}
   item{Pick("Lateralidad ganglionar",listOf("Derecha","Izquierda","Bilateral","No aplica/no palpable","No valorable"))}
   item{Pick("Tamaño aproximado",listOf("<0.5 cm","0.5–0.9 cm","1.0–1.9 cm","≥2 cm","No aplica/no palpable","No medido"))}
   item{Pick("Consistencia",listOf("Blanda","Elástica","Firme","Dura","No aplica/no palpable","No valorable"),"Registrar sitio, lateralidad y tamaño. Un ganglio palpable no establece por sí solo una etiología.")}
  }
  if(section=="ATM y dimensión vertical"){
   item{Pick("ATM · dolor",listOf("Sin dolor","Derecha","Izquierda","Bilateral","No valorable"))}
   item{Pick("Dolor durante movimiento",listOf("No","En apertura","En cierre","En lateralidad derecha","En lateralidad izquierda","En protrusión","En retrusión","En varios movimientos","No valorable"))}
   item{Pick("ATM · sonido",listOf("Sin sonido detectable","Click/chasquido derecho","Click/chasquido izquierdo","Click bilateral","Crepitación derecha","Crepitación izquierda","Crepitación bilateral","Otro/no valorable"),"Palpar región preauricular durante apertura, cierre y excursiones. Registrar el sonido sin generar diagnóstico automático.")}
   item{Pick("Palpación ATM",listOf("Sin dolor","Dolor polo lateral derecho","Dolor polo lateral izquierdo","Dolor bilateral","No valorable"))}
   item{Pick("Línea media al abrir/cerrar",listOf("Recta/centrada","Desviación derecha con retorno","Desviación izquierda con retorno","Deflexión persistente derecha","Deflexión persistente izquierda","Trayectoria irregular","No valorable"))}
   item{ClinicalPhotoV38("Apertura mandibular · referencia clínica",R.drawable.clinical_mouth_open,"Fotografía clínica real · Benjaminginterr · CC BY-SA. Ilustra apertura oral; la medición debe realizarse clínicamente con regla/calibrador.")}
   item{Pick("Apertura máxima interincisal",listOf("<25 mm","25–34 mm","35–39 mm","40–44 mm","45–55 mm","56–60 mm",">60 mm","No medida"),"Referencia adulta: se reportan rangos frecuentes alrededor de 42–55 mm; existe variación por edad, sexo y anatomía.")}
   item{Pick("Lateralidad derecha",listOf("<4 mm","4–6 mm","7–9 mm","10–12 mm",">12 mm","No medida"),"7 mm o más se usa como referencia clínica funcional mínima; individualizar.")}
   item{Pick("Lateralidad izquierda",listOf("<4 mm","4–6 mm","7–9 mm","10–12 mm",">12 mm","No medida"),"Comparar ambos lados y registrar dolor, click o limitación.")}
   item{Pick("Protrusión",listOf("<4 mm","4–5 mm","6–9 mm","10–12 mm",">12 mm","No medida"),"6 mm se usa como referencia clínica funcional mínima; individualizar.")}
   item{Pick("Retrusión",listOf("<1 mm","1–2 mm","3–4 mm",">4 mm","No medida"),"Registrar el desplazamiento medido; las referencias clínicas son menos uniformes para retrusión.")}
   item{Pick("Limitación de movimiento",listOf("No aparente","Apertura","Cierre","Lateralidad derecha","Lateralidad izquierda","Protrusión","Retrusión","Varios movimientos","No valorable"))}
   item{ClinicalPhotoV38("Dimensión vertical · referencia clínica",R.drawable.clinical_vertical_dimension,"Fotografía clínica real disponible en Wikimedia Commons para referencia de dimensión vertical; verificar puntos de medición y aplicar la técnica descrita en la app.")}
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
 val selected=remember{mutableStateMapOf<String,String>()}
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
  item{ScreenHeader("Antecedentes ortodónticos y ortopédicos",onBack,"Registro guiado del tratamiento previo, aparatología, duración, retención y resultado referido.")}
  item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){FilterChip(prior==false,{prior=false},{Text("No recibió")});FilterChip(prior==true,{prior=true},{Text("Sí recibió")})}}
  if(prior==true) sections.forEach{(name,opts)->
   item{Text(name,fontWeight=FontWeight.Black)}
   items(opts.size){i->val o=opts[i];FilterChip(selected[name]==o,{selected[name]=o},{Text(o)},modifier=Modifier.fillMaxWidth())}
  }
  if(prior==true)item{ClinicalPhotoV38("Aparatología ortodóntica fija · referencia clínica real",R.drawable.clinical_braces,"Fotografía clínica real · Wikimedia Commons · referencia visual de aparatología fija; consultar autor/licencia del archivo.")}
  item{NoticeCard("El antecedente ortodóntico se registra según lo referido y lo observable. No asumir diagnóstico previo, indicación original ni estabilidad futura sin expediente, exploración y estudios.")}
 }
}

@Composable fun HistoryDentalAlterationsV38(lang:String,onBack:()->Unit){
 var section by remember{mutableStateOf(0)}
 val selected=remember{mutableStateMapOf<String,String>()}
 val sections=listOf(
  "Número · disminución" to listOf("Sin alteración","Hipodoncia/agenesia","Oligodoncia","Anodoncia"),
  "Número · aumento" to listOf("Sin alteración","Supernumerario","Mesiodens","Paramolar","Distomolar"),
  "Tamaño" to listOf("Sin alteración","Microdoncia localizada","Microdoncia generalizada","Macrodoncia localizada","Macrodoncia generalizada"),
  "Forma / morfología" to listOf("Sin alteración","Fusión","Geminación","Concrescencia","Dens invaginatus","Dens evaginatus","Taurodontismo","Perla de esmalte","Dilaceración"),
  "Estructura" to listOf("Sin alteración","Hipoplasia del esmalte","Hipomineralización","Amelogénesis imperfecta","Dentinogénesis imperfecta","Displasia dentinaria","Fluorosis sospechada"),
  "Color" to listOf("Sin alteración","Extrínseco localizado","Extrínseco generalizado","Intrínseco localizado","Intrínseco generalizado","Opacidad blanca","Tonalidad amarilla/marrón","Tonalidad gris/azulada"),
  "Erupción" to listOf("Normal aparente","Erupción precoz","Erupción tardía","Ectópica","Retenido/no erupcionado","Impactado","Incluido intraóseo","Natal","Neonatal","Anquilosis/diente sumergido","Falla primaria sospechada"),
  "Posición" to listOf("Sin alteración","Rotación","Versión/inclinación","Transposición","Desplazamiento vestibular","Desplazamiento lingual/palatino","Infraoclusión","Supraoclusión")
 )
 val photos=listOf(
  R.drawable.clinical_hypodontia,
  R.drawable.clinical_mesiodens,
  R.drawable.clinical_microdontia,
  R.drawable.clinical_gemination,
  R.drawable.clinical_amelogenesis,
  R.drawable.clinical_fluorosis,
  R.drawable.clinical_impacted,
  R.drawable.clinical_transposition
 )
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
  item{ScreenHeader("Alteraciones y anomalías dentales",onBack,"Selecciona el grupo, revisa una referencia visual y registra el hallazgo. Describe primero; confirma con radiografía/estudios cuando corresponda.")}
  item{LazyRow(horizontalArrangement=Arrangement.spacedBy(6.dp)){items(sections.size){i->FilterChip(section==i,{section=i},{Text(sections[i].first)})}}}
  item{ClinicalPhotoV38(sections[section].first+" · referencia visual",photos[section],"Imagen clínica/radiográfica real de referencia · Wikimedia Commons. Verificar autor y licencia del archivo; no usar una imagen aislada para establecer diagnóstico.")}
  item{Text("Hallazgo",fontWeight=FontWeight.Black)}
  items(sections[section].second.size){i->val o=sections[section].second[i];FilterChip(selected[sections[section].first]==o,{selected[sections[section].first]=o},{Text(o)},modifier=Modifier.fillMaxWidth())}
  item{Text("Extensión",fontWeight=FontWeight.Black)}
  items(listOf("Un diente","Varios dientes","Localizado por cuadrante","Generalizado","No valorable").size){i->val o=listOf("Un diente","Varios dientes","Localizado por cuadrante","Generalizado","No valorable")[i];FilterChip(selected["Extensión"]==o,{selected["Extensión"]=o},{Text(o)},modifier=Modifier.fillMaxWidth())}
  item{Text("Confirmación disponible",fontWeight=FontWeight.Black)}
  items(listOf("Sólo clínica","Clínica + radiografía","Antecedente documentado","Requiere estudio complementario","No aplica").size){i->val o=listOf("Sólo clínica","Clínica + radiografía","Antecedente documentado","Requiere estudio complementario","No aplica")[i];FilterChip(selected["Confirmación"]==o,{selected["Confirmación"]=o},{Text(o)},modifier=Modifier.fillMaxWidth())}
  item{NoticeCard("Registrar el órgano dentario específico se completa en el odontograma/análisis dental. Retención, inclusión e impactación pueden solaparse según la fuente; correlacionar clínica y radiografía antes de etiquetar.")}
 }
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

