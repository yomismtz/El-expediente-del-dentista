package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
    LabParamV20("alt","ALT/TGP","ALT","U/L",4.0,36.0,4.0,36.0,"ALT baja rara vez tiene significado aislado.","ALT alta orienta a lesión hepatocelular, pero no define por sí sola la causa.")
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
    var tab by remember{mutableStateOf(0)}
    var male by remember{mutableStateOf(true)}
    val values=remember{mutableStateMapOf<String,String>()}
    val tabs=listOf(tr(lang,"Biometría","CBC"),tr(lang,"Química","Chemistry"),tr(lang,"Coagulación","Coagulation"),tr(lang,"Biopsia","Biopsy"))

    ResponsiveScreenV17(tr(lang,"Laboratorio e histopatología","Laboratory & histopathology"),tr(lang,"Introduce resultados para compararlos con intervalos educativos de adulto. La referencia del laboratorio y el contexto clínico siempre tienen prioridad.","Enter results to compare them with adult teaching intervals. The reporting laboratory and clinical context always take priority."),onBack){profile->
        AdaptiveGridV17(tabs.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 4){i->
            FilterChip(tab==i,{tab=i},{Text(tabs[i])},Modifier.fillMaxWidth())
        }

        if(tab<3){
            ResponsiveSectionV17(tr(lang,"Sexo para intervalos que cambian","Sex for intervals that differ")){
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
                    FilterChip(male,{male=true},{Text(tr(lang,"Hombre","Male"))},Modifier.weight(1f))
                    FilterChip(!male,{male=false},{Text(tr(lang,"Mujer","Female"))},Modifier.weight(1f))
                }
            }
            val params=when(tab){0->cbcV20;1->chemistryV20;else->coagV20}
            params.forEach{p->
                val raw=values[p.key].orEmpty()
                val result=labStatusV20(raw.toDoubleOrNull(),p,male,lang)
                val min=if(male)p.maleMin else p.femaleMin
                val max=if(male)p.maleMax else p.femaleMax
                ResponsiveSectionV17(if(lang=="en")p.en else p.es,"${tr(lang,"Referencia","Reference")}: ${formatLabV20(min)}–${formatLabV20(max)} ${p.unit}"){
                    OutlinedTextField(value=raw,onValueChange={values[p.key]=it.filter{c->c.isDigit()||c=='.'}.take(10)},label={Text(tr(lang,"Resultado","Result"))},suffix={if(p.unit.isNotBlank())Text(p.unit)},modifier=Modifier.fillMaxWidth(),singleLine=true)
                    if(raw.isNotBlank()){
                        Text(result.first,fontWeight=FontWeight.Black,color=when{raw.toDoubleOrNull()==null->MaterialTheme.colorScheme.error;raw.toDouble()<min||raw.toDouble()>max->MaterialTheme.colorScheme.error;else->MaterialTheme.colorScheme.primary})
                        Text(result.second)
                    }
                }
            }
            if(tab==2) NoticeCard(tr(lang,"Los objetivos de INR cambian en pacientes con anticoagulación (p. ej. warfarina). No uses el intervalo de una persona sin anticoagulante para decidir suspender/modificar medicamentos ni para autorizar un procedimiento.","INR targets differ for anticoagulated patients (e.g. warfarin). Do not use the non-anticoagulated interval to change medication or clear a procedure."))
        }else{
            HistopathologyV20(lang)
        }

        NoticeCard(tr(lang,"Estos intervalos son referencias educativas para adultos y pueden variar por laboratorio, método, edad, embarazo, altitud, medicación y enfermedad. Un valor fuera de rango no equivale por sí solo a un diagnóstico.","These are adult teaching references and may vary by laboratory, method, age, pregnancy, altitude, medication and disease. An out-of-range value is not a diagnosis by itself."))
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
