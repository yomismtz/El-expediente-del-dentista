package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class LabParamV20(
    val key:String,
    val es:String,
    val en:String,
    val unit:String,
    val maleMin:Double,
    val maleMax:Double,
    val femaleMin:Double=maleMin,
    val femaleMax:Double=maleMax,
    val lowEs:String,
    val highEs:String,
    val lowEn:String=lowEs,
    val highEn:String=highEs
)

private val cbcV20=listOf(
    LabParamV20("rbc","Eritrocitos","RBC","millones/µL",4.6,6.2,4.2,5.4,"Disminución compatible con anemia, pérdida sanguínea o menor producción; correlacionar con Hb/Hto e índices.","Elevación posible en hemoconcentración, hipoxia crónica o eritrocitosis; correlacionar clínicamente."),
    LabParamV20("hb","Hemoglobina","Hemoglobin","g/dL",13.0,18.0,12.0,16.0,"Valor bajo sugiere anemia; valorar magnitud, síntomas, pérdidas, nutrición y causa.","Valor alto puede relacionarse con hemoconcentración, hipoxia o eritrocitosis."),
    LabParamV20("hct","Hematocrito","Hematocrit","%",40.0,55.0,36.0,48.0,"Hematocrito bajo acompaña con frecuencia anemia o hemodilución.","Hematocrito alto puede verse en deshidratación o eritrocitosis."),
    LabParamV20("wbc","Leucocitos","WBC","/µL",4500.0,11000.0,4500.0,11000.0,"Leucopenia: puede aumentar susceptibilidad a infección según causa y diferencial.","Leucocitosis: puede acompañar infección, inflamación, estrés u otras causas; revisar diferencial."),
    LabParamV20("plt","Plaquetas","Platelets","/µL",150000.0,400000.0,150000.0,400000.0,"Trombocitopenia: puede aumentar riesgo hemorrágico según cifra, función plaquetaria y contexto.","Trombocitosis: puede ser reactiva o clonal; requiere correlación clínica."),
    LabParamV20("mcv","VCM","MCV","fL",80.0,100.0,80.0,100.0,"VCM bajo orienta a microcitosis (p. ej., ferropenia entre varias causas).","VCM alto orienta a macrocitosis; revisar B12/folato, fármacos y otras causas."),
    LabParamV20("mch","HCM","MCH","pg",27.0,32.0,27.0,32.0,"HCM baja acompaña hipocromía/microcitosis con frecuencia.","HCM alta suele acompañar macrocitosis; interpretar junto con VCM."),
    LabParamV20("mchc","CHCM","MCHC","g/dL",32.0,36.0,32.0,36.0,"CHCM baja sugiere hipocromía.","CHCM alta es menos frecuente; confirmar muestra y correlacionar con el laboratorio.")
)

private val chemistryV20=listOf(
    LabParamV20("glu","Glucosa","Glucose","mg/dL",70.0,100.0,70.0,100.0,"Glucosa baja: confirmar contexto/ayuno, síntomas y posible hipoglucemia.","Glucosa elevada: puede relacionarse con ingesta reciente, estrés o alteración glucémica; una cifra aislada no diagnostica diabetes."),
    LabParamV20("bun","Nitrógeno ureico (BUN)","BUN","mg/dL",6.0,20.0,6.0,20.0,"BUN bajo puede relacionarse con menor producción, hidratación o nutrición; interpretar con creatinina.","BUN alto puede acompañar deshidratación o disminución de función renal, entre otras causas."),
    LabParamV20("crea","Creatinina","Creatinine","mg/dL",0.6,1.3,0.6,1.3,"Creatinina baja suele reflejar menor masa muscular u otros factores; usar eGFR cuando corresponda.","Creatinina alta puede indicar disminución del filtrado renal; interpretar con eGFR y contexto."),
    LabParamV20("na","Sodio","Sodium","mEq/L",135.0,145.0,135.0,145.0,"Hiponatremia: alteración hidroelectrolítica que requiere valorar gravedad y síntomas.","Hipernatremia: puede indicar déficit de agua u otras alteraciones; valorar contexto."),
    LabParamV20("k","Potasio","Potassium","mEq/L",3.7,5.2,3.7,5.2,"Hipopotasemia puede alterar función neuromuscular/cardiaca según magnitud.","Hiperpotasemia puede ser clínicamente importante; confirmar muestra y valorar riesgo cardiaco."),
    LabParamV20("cl","Cloruro","Chloride","mEq/L",96.0,106.0,96.0,106.0,"Cloruro bajo se interpreta junto con sodio y equilibrio ácido-base.","Cloruro alto se interpreta junto con sodio, hidratación y equilibrio ácido-base."),
    LabParamV20("ca","Calcio","Calcium","mg/dL",8.5,10.2,8.5,10.2,"Calcio bajo requiere considerar albúmina, vitamina D y otras causas.","Calcio alto requiere confirmar y estudiar causa; correlacionar con albúmina/PTH según el caso."),
    LabParamV20("alb","Albúmina","Albumin","g/dL",3.4,5.4,3.4,5.4,"Albúmina baja puede relacionarse con inflamación, hígado, riñón o nutrición.","Albúmina alta suele asociarse a hemoconcentración/deshidratación."),
    LabParamV20("ast","AST/TGO","AST","U/L",8.0,33.0,8.0,33.0,"AST baja rara vez tiene significado aislado.","AST alta puede reflejar lesión hepática o muscular; interpretar con ALT y contexto."),
    LabParamV20("alt","ALT/TGP","ALT","U/L",4.0,36.0,4.0,36.0,"ALT baja rara vez tiene significado aislado.","ALT alta orienta a lesión hepatocelular, pero no define por sí sola la causa."),
    LabParamV20("co2","CO₂ / bicarbonato","CO₂ / bicarbonate","mEq/L",23.0,29.0,23.0,29.0,"Valor bajo puede acompañar alteraciones ácido-base; interpretar clínicamente.","Valor alto puede acompañar alteraciones ácido-base; interpretar clínicamente."),
    LabParamV20("tp","Proteínas totales","Total protein","g/dL",6.0,8.3,6.0,8.3,"Valor bajo puede asociarse con nutrición, hígado, riñón u otras causas.","Valor alto puede reflejar deshidratación, inflamación u otras causas."),
    LabParamV20("alp","Fosfatasa alcalina","Alkaline phosphatase","U/L",44.0,147.0,44.0,147.0,"Valor bajo requiere correlación con edad, nutrición y laboratorio.","Valor alto puede ser de origen hepatobiliar u óseo; edad y crecimiento modifican la interpretación."),
    LabParamV20("bt","Bilirrubina total","Total bilirubin","mg/dL",0.1,1.2,0.1,1.2,"Valor bajo suele carecer de significado aislado.","Elevación requiere correlacionar con fracciones, hígado, hemólisis y clínica."),
    LabParamV20("chol","Colesterol total","Total cholesterol","mg/dL",0.0,199.0,0.0,199.0,"Un valor bajo se interpreta según nutrición, enfermedad y contexto.","Un valor alto se relaciona con riesgo cardiometabólico; valorar perfil lipídico completo."),
    LabParamV20("tg","Triglicéridos","Triglycerides","mg/dL",0.0,149.0,0.0,149.0,"Valor bajo suele interpretarse con nutrición y contexto.","Valor alto puede relacionarse con alimentación, metabolismo, diabetes u otras causas."),
    LabParamV20("ua","Ácido úrico","Uric acid","mg/dL",3.4,7.0,2.4,6.0,"Valor bajo puede relacionarse con fármacos o menor producción; correlacionar.","Valor alto puede acompañar hiperuricemia/gota, función renal u otras causas.")
)

private val thyroidV23=listOf(
    LabParamV20("tsh","TSH","TSH","µUI/mL",0.4,4.8,0.4,4.8,"TSH baja puede acompañar hipertiroidismo primario o alteraciones hipofisarias; interpretar con T4L/T3.","TSH alta puede acompañar hipotiroidismo primario; interpretar con T4L y contexto."),
    LabParamV20("ft4","T4 libre","Free T4","ng/dL",0.8,1.8,0.8,1.8,"T4L baja puede acompañar hipotiroidismo; interpretar junto con TSH.","T4L alta puede acompañar hipertiroidismo; interpretar junto con TSH."),
    LabParamV20("t3","T3 total","Total T3","ng/dL",80.0,200.0,80.0,200.0,"T3 baja puede verse en hipotiroidismo o enfermedad no tiroidea; no interpretar sola.","T3 alta puede acompañar hipertiroidismo; interpretar con TSH y T4L.")
)

private data class MicroV23(val name:String,val sample:String,val meaning:String)
private val microV23=listOf(
    MicroV23("Cultivo bacteriano aerobio + antibiograma","Pus/exudado o muestra profunda obtenida con técnica adecuada","Identifica bacterias aerobias cultivables y, cuando procede, susceptibilidad. Un aislamiento debe correlacionarse con sitio, calidad de muestra y clínica."),
    MicroV23("Cultivo anaerobio","Aspirado o muestra profunda, transportada sin exposición al oxígeno","Útil en infecciones profundas seleccionadas; una muestra superficial puede contaminarse con microbiota oral."),
    MicroV23("Cultivo micológico","Raspado, hisopo o muestra de lesión según protocolo","Puede apoyar identificación de Candida u otros hongos cuando la presentación o respuesta clínica lo justifica."),
    MicroV23("PCR / prueba molecular","Muestra definida por el ensayo","Detecta material genético de microorganismos específicos; detección no siempre equivale a infección activa."),
    MicroV23("Tinción de Gram","Muestra de exudado/tejido","Orienta morfología y respuesta de Gram; es complementaria y no sustituye cultivo/identificación cuando éstos son necesarios."),
    MicroV23("Hemocultivos","Sangre, solicitados por equipo médico","Se usan ante sospecha de bacteriemia/infección sistémica; no son un estudio odontológico rutinario.")
)

private data class CambraItemV23(val label:String,val kind:Int)
private val cambraAdultV23=listOf(
    CambraItemV23("Lesiones cavitadas/no cavitadas o restauraciones recientes por caries",2),
    CambraItemV23("Diente perdido por caries recientemente",2),
    CambraItemV23("Xerostomía / flujo salival marcadamente reducido",2),
    CambraItemV23("Exposición frecuente a azúcares entre comidas",1),
    CambraItemV23("Biofilm abundante / higiene insuficiente",1),
    CambraItemV23("Aparatos que favorecen retención de biofilm",1),
    CambraItemV23("Necesidades especiales que dificultan higiene",1),
    CambraItemV23("Cepillado dos veces al día con dentífrico fluorurado",0),
    CambraItemV23("Exposición adecuada a fluoruro / medidas preventivas",0),
    CambraItemV23("Atención dental regular y medidas de control activas",0)
)
private val cambraChildV23=listOf(
    CambraItemV23("Lesiones de caries/restauraciones recientes",2),
    CambraItemV23("Diente perdido por caries",2),
    CambraItemV23("Flujo salival visualmente inadecuado",2),
    CambraItemV23("Biberón/vaso con bebida azucarada o exposición frecuente a azúcares",1),
    CambraItemV23("Madre/cuidador/hermanos con experiencia reciente de caries",1),
    CambraItemV23("Necesidades especiales que dificultan higiene",1),
    CambraItemV23("Biofilm visible / higiene insuficiente",1),
    CambraItemV23("Cepillado con dentífrico fluorurado apropiado para edad",0),
    CambraItemV23("Exposición adecuada a fluoruro",0),
    CambraItemV23("Hogar dental / controles preventivos regulares",0)
)

private val coagV20=listOf(
    LabParamV20("pt","TP","PT","s",11.0,13.5,11.0,13.5,"TP corto suele tener menor utilidad aislada; revisar preanalítica y contexto.","TP prolongado significa coagulación más lenta; revisar anticoagulantes, hígado, vitamina K y trastornos de coagulación."),
    LabParamV20("inr","INR","INR","",0.8,1.1,0.8,1.1,"INR bajo solo se interpreta con el contexto y, si usa warfarina, con su objetivo terapéutico.","INR alto implica coagulación más lenta; si usa warfarina el rango objetivo es distinto y debe indicarlo su médico."),
    LabParamV20("ptt","TTPa / PTT","aPTT / PTT","s",25.0,35.0,25.0,35.0,"TTPa corto puede ser preanalítico o asociarse a estados procoagulantes; confirmar.","TTPa prolongado puede relacionarse con heparina, déficits/inhibidores de factores u otras causas."),
    LabParamV20("fib","Fibrinógeno","Fibrinogen","mg/dL",200.0,400.0,200.0,400.0,"Fibrinógeno bajo puede aumentar riesgo de sangrado y aparecer por consumo, déficit o fibrinólisis.","Fibrinógeno alto puede aumentar como reactante de fase aguda en inflamación/infección, entre otras causas.")
)

private fun labStatusV20(value:Double?,p:LabParamV20,male:Boolean,lang:String):Pair<String,String>{
    if(value==null) return tr(lang,"Sin resultado","No result") to ""
    val min=if(male)p.maleMin else p.femaleMin
    val max=if(male)p.maleMax else p.femaleMax
    return when{
        value<min->tr(lang,"↓ Bajo","↓ Low") to if(lang=="en")p.lowEn else p.lowEs
        value>max->tr(lang,"↑ Alto","↑ High") to if(lang=="en")p.highEn else p.highEs
        else->tr(lang,"✓ En referencia","✓ In reference") to tr(lang,"Dentro del intervalo educativo mostrado; confirma siempre el intervalo del laboratorio que emitió el estudio.","Within the teaching interval shown; always confirm the reporting laboratory's own interval.")
    }
}

@Composable
fun LaboratoryAuxiliariesV20Screen(lang:String,onBack:()->Unit){
 var tab by remember{mutableStateOf(0)}; var male by rememberRecordState("lab.male",true)
 val values=rememberRecordStateMap<String,String>("lab.values")
 val tabs=listOf(tr(lang,"Biometría","CBC"),tr(lang,"Química 18","Chemistry 18"),tr(lang,"Tiroides","Thyroid"),tr(lang,"Coagulación","Coagulation"),tr(lang,"Histología","Histology"),tr(lang,"Microbiología","Microbiology"),"CAMBRA")
 ResponsiveScreenV17(tr(lang,"Laboratorio e histopatología","Laboratory & histopathology"),tr(lang,"El alumno no escribe ni sube archivos o resultados en este módulo. La carga de archivos se reserva exclusivamente para los módulos de análisis de imágenes radiográficas, imagenología y cefalometría. Selecciona valores educativos y la app explica qué significan; todo debe comprobarse con el reporte real y el contexto clínico.","The student does not type or upload files or results in this module. File upload is reserved exclusively for radiographic image analysis, imaging and cephalometric modules. Select teaching values and the app explains their meaning; everything must be verified against the actual report and clinical context."),onBack){profile->
  val tabCols=when{profile.largeSystemText->3;profile.width==ScreenWidthV17.COMPACT->3;profile.width==ScreenWidthV17.MEDIUM->4;else->5}
  AdaptiveGridV17(tabs.size,tabCols){i->FilterChip(tab==i,{tab=i},{Text(tabs[i])},Modifier.fillMaxWidth())}
  if(tab<4){
   ResponsiveSectionV17(tr(lang,"Sexo para intervalos que cambian","Sex for intervals that differ")){ChipChoices(listOf(tr(lang,"Hombre","Male") to male,tr(lang,"Mujer","Female") to !male),{male=it==0},columns=3)}
   val params=when(tab){0->cbcV20;1->chemistryV20;2->thyroidV23;else->coagV20}
   params.forEach{p->
    val min=if(male)p.maleMin else p.femaleMin; val max=if(male)p.maleMax else p.femaleMax
    val low=if(min<=0.0)0.0 else min*.8; val mid=(min+max)/2.0; val high=max+(max-min).coerceAtLeast(max*.1)
    val examples=listOf(low,min,mid,max,high).distinct().sorted(); val raw=values[p.key].orEmpty(); val result=labStatusV20(raw.toDoubleOrNull(),p,male,lang)
    ResponsiveSectionV17(if(lang=="en")p.en else p.es,"${tr(lang,"Referencia educativa","Teaching reference")}: ${formatLabV20(min)}–${formatLabV20(max)} ${p.unit}"){
     Text(tr(lang,"Selecciona un valor de ejemplo. No hay captura libre.","Select an example value. There is no free-form entry."),style=MaterialTheme.typography.bodySmall)
     ChipChoices(examples.map{v->"${formatLabV20(v)} ${p.unit}".trim() to (raw==v.toString())},{i->values[p.key]=examples[i].toString()},columns=5)
     if(raw.isNotBlank()){Text(result.first,fontWeight=FontWeight.Black,color=if(raw.toDouble()<min||raw.toDouble()>max)MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary);Text(result.second)}
    }
   }
   if(tab==3) NoticeCard(tr(lang,"Los objetivos de INR cambian en pacientes con anticoagulación. Estas opciones enseñan interpretación; no autorizan procedimientos ni cambios de medicamentos.","INR targets differ in anticoagulated patients. These options teach interpretation; they do not clear procedures or medication changes."))
  }else when(tab){4->HistopathologyV20(lang);5->MicrobiologyV23(lang);else->CambraV23(lang)}
  NoticeCard(tr(lang,"Valores y resultados son ejemplos educativos seleccionables. El alumno no sube archivos ni escribe resultados en este módulo. Un valor seleccionado no equivale a un resultado real ni a un diagnóstico.","Values and results are selectable teaching examples. The student does not upload files or type results in this module. A selected value is not an actual result or a diagnosis."))
 }
}

private fun formatLabV20(v:Double)=if(v%1.0==0.0)v.toInt().toString() else v.toString()

@Composable
private fun HistopathologyV20(lang:String){
    val categories=listOf(
        Triple("Reacción / hiperplasia","Reactive change / hyperplasia","Aumento del número de células o tejido, a menudo como respuesta a irritación/trauma. Ejemplos orales incluyen hiperplasias fibrosas reactivas. No significa automáticamente neoplasia."),
        Triple("Inflamación / infección","Inflammation / infection","El reporte puede describir inflamación aguda, crónica, granulomatosa, absceso, colonias microbianas u otros hallazgos. La etiología requiere correlación clínica y, a veces, tinciones/pruebas adicionales."),
        Triple("Displasia epitelial","Epithelial dysplasia","Alteraciones citológicas y arquitectónicas premalignas que suelen graduarse como leve, moderada o severa según el sistema utilizado. No es sinónimo de cáncer, pero requiere seguimiento/manejo profesional."),
        Triple("Neoplasia benigna","Benign neoplasm","Proliferación neoplásica sin criterios de malignidad. El diagnóstico exacto depende del tipo celular, arquitectura, márgenes y correlación clínica/radiográfica."),
        Triple("Neoplasia maligna","Malignant neoplasm","Puede incluir carcinoma, sarcoma, neoplasias hematolinfoides u otras. Requiere derivación y manejo especializado; la app no interpreta grado, estadio ni tratamiento."),
        Triple("Lesión quística / odontogénica","Cystic / odontogenic lesion","Puede corresponder a quistes odontogénicos/no odontogénicos o tumores odontogénicos. El informe histológico debe integrarse con localización e imagen."),
        Triple("Alteraciones del desarrollo","Developmental alterations","Aplasia significa ausencia del desarrollo de un órgano/tejido; hipoplasia significa desarrollo incompleto. Son términos del desarrollo y no deben confundirse con displasia o neoplasia."),
        Triple("Inconcluso / muestra insuficiente","Inconclusive / insufficient sample","El patólogo puede informar material insuficiente, tejido no representativo o recomendar nueva toma, inmunohistoquímica u otros estudios.")
    )
    ResponsiveSectionV17(tr(lang,"Cómo leer un informe de biopsia","How to read a biopsy report"),tr(lang,"No intentes convertir cada palabra en un diagnóstico automático. Identifica primero el tipo de proceso y conserva literalmente el diagnóstico del patólogo.","Do not turn every word into an automatic diagnosis. First identify the process category and preserve the pathologist's exact diagnosis.")){
        Text(tr(lang,"Busca: tipo de espécimen y sitio → descripción microscópica → diagnóstico → grado si aplica → márgenes si aplica → estudios adicionales/comentarios.","Look for: specimen/site → microscopic description → diagnosis → grade if applicable → margins if applicable → ancillary studies/comments."))
    }
    categories.forEach{item->
        ResponsiveSectionV17(if(lang=="en")item.second else item.first){Text(if(lang=="en")histologyEnglishV20(item.first) else item.third)}
    }
    NoticeCard(tr(lang,"En el expediente escribe el resultado histopatológico tal como fue emitido y después una interpretación clínica prudente. Si aparecen términos como displasia severa, carcinoma, neoplasia maligna, márgenes comprometidos o un resultado inesperado, requiere revisión docente/profesional prioritaria.","In the record, copy the histopathology result exactly as issued, then add a cautious clinical interpretation. Severe dysplasia, carcinoma, malignant neoplasm, involved margins or an unexpected result requires prompt faculty/professional review."))
}

private fun histologyEnglishV20(es:String)=when(es){
    "Reacción / hiperplasia"->"An increase in cells or tissue, often reactive to irritation or trauma. Hyperplasia does not automatically mean neoplasia."
    "Inflamación / infección"->"Reports may describe acute, chronic or granulomatous inflammation, abscess, organisms or related findings; correlate clinically."
    "Displasia epitelial"->"Premalignant cytologic/architectural abnormalities, commonly graded mild, moderate or severe depending on the system. It is not synonymous with cancer."
    "Neoplasia benigna"->"A neoplastic proliferation without malignant criteria; exact diagnosis depends on lineage, architecture, margins and clinical/radiographic context."
    "Neoplasia maligna"->"May include carcinoma, sarcoma, hematolymphoid or other malignancies and requires specialist management."
    "Lesión quística / odontogénica"->"May include odontogenic/non-odontogenic cysts or odontogenic tumors; integrate pathology with site and imaging."
    "Alteraciones del desarrollo"->"Aplasia means failure of development; hypoplasia means incomplete development. These are developmental terms and differ from dysplasia/neoplasia."
    else->"The pathologist may report insufficient or non-representative tissue or request repeat biopsy or ancillary studies."
}

@Composable private fun MicrobiologyV23(lang:String){
    ResponsiveSectionV17(tr(lang,"Cultivos y pruebas microbiológicas frecuentes","Common microbiology cultures and tests"),tr(lang,"Selecciona el estudio según la pregunta clínica y el tipo de muestra; no se solicitan de rutina para toda infección odontogénica.","Choose testing according to the clinical question and specimen; these are not routine for every odontogenic infection.")){
        microV23.forEach{x->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(12.dp)){Text(x.name,fontWeight=FontWeight.Bold);Text("Muestra: "+x.sample);Text("Interpretación: "+x.meaning)}}}
    }
    NoticeCard(tr(lang,"Un resultado «positivo» puede representar infección, colonización o contaminación según microorganismo, sitio y técnica. Correlaciona con clínica y antibiograma cuando corresponda.","A positive result may represent infection, colonization or contamination depending on organism, site and technique. Correlate with clinical findings and susceptibility testing when appropriate."))
}

@Composable private fun CambraV23(lang:String){
    var child by rememberRecordState("lab.child",false)
    val selected=rememberRecordStateMap<String,Boolean>("lab.selected")
    val items=if(child)cambraChildV23 else cambraAdultV23
    ResponsiveSectionV17("CAMBRA · "+tr(lang,"riesgo de caries","caries risk"),tr(lang,"Herramienta educativa para seleccionar indicadores, factores de riesgo y factores protectores.","Teaching tool to select disease indicators, risk factors and protective factors.")){
        ChipChoices(listOf(tr(lang,"Adulto / >6 años","Adult / >6 years") to !child,tr(lang,"Niño 0–6 años","Child 0–6 years") to child),{child=it==1;selected.clear()},columns=2)
        items.forEach{x->Row(Modifier.fillMaxWidth()){Checkbox(selected[x.label]==true,{selected[x.label]=it});Text(x.label,Modifier.weight(1f))}}
        val high=items.any{it.kind==2&&selected[it.label]==true}
        val risks=items.count{it.kind==1&&selected[it.label]==true}
        val protective=items.count{it.kind==0&&selected[it.label]==true}
        val level=when{high->tr(lang,"ALTO","HIGH");risks==0->tr(lang,"BAJO","LOW");risks<=protective+1->tr(lang,"MEDIO","MODERATE");else->tr(lang,"ALTO","HIGH")}
        Text(tr(lang,"Riesgo educativo calculado: ","Calculated teaching risk: ")+level,fontWeight=FontWeight.Black)
        Text(tr(lang,"Los indicadores de enfermedad dominan la clasificación. En ausencia de ellos, el balance entre factores de riesgo y protectores orienta la categoría; el juicio clínico puede modificarla.","Disease indicators dominate classification. Without them, the balance of risk and protective factors guides the category; clinical judgment may modify it."),style=MaterialTheme.typography.bodySmall)
    }
    NoticeCard(tr(lang,"CAMBRA/valoración de riesgo no diagnostica caries. Confirma lesiones, actividad, dieta, saliva, exposición a fluoruro y antecedentes antes de definir el plan preventivo.","CAMBRA/risk assessment does not diagnose caries. Confirm lesions, activity, diet, saliva, fluoride exposure and history before defining prevention."))
}
