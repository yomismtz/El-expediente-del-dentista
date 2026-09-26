package com.yomismtz.expedientedeldentista.ui
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable private fun PText45(key:String,label:String){ var v by rememberRecordState(key,""); OutlinedTextField(v,{v=it},Modifier.fillMaxWidth(),label={Text(label)}) }
@Composable private fun PChoice45(key:String,options:List<String>,columns:Int){ var v by rememberRecordState(key,""); AdaptiveGridV17(options.size,columns){i->FilterChip(v==options[i],{v=options[i]},{Text(options[i])},Modifier.fillMaxWidth())} }

@Composable fun PediatricDentistryV45(lang:String,onBack:()->Unit){
 ResponsiveScreenV17("🧒 "+tr(lang,"Ficha de Odontopediatría","Pediatric Dentistry Sheet"),tr(lang,"Documenta desarrollo, conducta, prevención, exploración y tratamiento pediátrico. Complementa odontograma e índices existentes sin modificarlos.","Documents pediatric development, behavior, prevention, examination and treatment. It complements existing odontogram and indices without modifying them."),onBack){p->
  val cols=if(p.largeSystemText)1 else if(p.width==ScreenWidthV17.COMPACT)2 else 3
  ResponsiveSectionV17(tr(lang,"Desarrollo y contexto","Development and context"),tr(lang,"La edad cronológica por sí sola no define maduración dental ni conducta; registra lo observado y los antecedentes relevantes.","Chronological age alone does not define dental maturation or behavior; record observed findings and relevant history.")){
   AdaptiveGridV17(6,cols){i->when(i){0->PText45("pedo.guardian",tr(lang,"Madre/padre/tutor acompañante","Accompanying parent/guardian"));1->PText45("pedo.dentition",tr(lang,"Dentición / etapa eruptiva","Dentition / eruption stage"));2->PText45("pedo.firstVisit",tr(lang,"Primera visita dental / experiencias previas","First dental visit / prior experiences"));3->PText45("pedo.behavior",tr(lang,"Conducta y cooperación observadas","Observed behavior and cooperation"));4->PText45("pedo.feeding",tr(lang,"Alimentación / frecuencia de azúcares","Diet / sugar frequency"));else->PText45("pedo.fluoride",tr(lang,"Exposición a flúor / prevención","Fluoride exposure / prevention"))}}
  }
  ResponsiveSectionV17(tr(lang,"Hábitos y prevención","Habits and prevention"),tr(lang,"Registra duración, frecuencia y contexto; un hábito aislado no determina por sí mismo una alteración dentofacial.","Record duration, frequency and context; an isolated habit does not by itself determine a dentofacial alteration.")){
   PChoice45("pedo.habits",listOf("Sin hábito referido","Succión digital","Chupón","Mamila prolongada","Onicofagia","Bruxismo / apretamiento","Respiración oral referida","Otro"),cols)
   PText45("pedo.habitDetail",tr(lang,"Frecuencia, duración y observaciones","Frequency, duration and observations")); PText45("pedo.hygiene",tr(lang,"Cepillado, pasta fluorada y supervisión","Brushing, fluoride toothpaste and supervision"))
  }
  ResponsiveSectionV17(tr(lang,"Exploración pediátrica","Pediatric examination"),tr(lang,"Describe tejidos, dientes, erupción, trauma y necesidades observadas; usa odontograma/ICDAS/índices en sus módulos específicos.","Describe tissues, teeth, eruption, trauma and observed needs; use odontogram/ICDAS/indices in their specific modules.")){
   AdaptiveGridV17(6,cols){i->when(i){0->PText45("pedo.eruption",tr(lang,"Erupción / exfoliación","Eruption / exfoliation"));1->PText45("pedo.caries",tr(lang,"Lesiones de caries observadas","Observed caries lesions"));2->PText45("pedo.trauma",tr(lang,"Trauma dentoalveolar","Dentoalveolar trauma"));3->PText45("pedo.soft",tr(lang,"Tejidos blandos / periodonto","Soft tissues / periodontium"));4->PText45("pedo.occlusion",tr(lang,"Oclusión / espacio","Occlusion / space"));else->PText45("pedo.radiographs",tr(lang,"Auxiliares indicados / hallazgos","Indicated aids / findings"))}}
  }
  ResponsiveSectionV17(tr(lang,"Plan y seguimiento","Plan and follow-up"),tr(lang,"Documenta sólo procedimientos indicados y realizados, educación al cuidador, consentimiento y supervisión correspondiente.","Document only indicated/performed procedures, caregiver education, consent and appropriate supervision.")){
   PText45("pedo.plan",tr(lang,"Plan preventivo / restaurador / pulpar / ortopédico","Preventive / restorative / pulp / orthopedic plan")); PText45("pedo.guidance",tr(lang,"Indicaciones al niño y cuidador","Instructions to child and caregiver")); PText45("pedo.followup",tr(lang,"Control, reevaluación y remisiones","Follow-up, reassessment and referrals"))
  }
 }
}
private data class RiskFactor45(val key:String,val label:String,val kind:String)
@Composable fun CariesRiskV45(lang:String,onBack:()->Unit){
 val factors=listOf(RiskFactor45("active","Lesiones de caries activas / cavitadas observadas","d"),RiskFactor45("recent","Caries/restauraciones recientes relevantes","d"),RiskFactor45("sugar","Exposición frecuente a azúcares fermentables","r"),RiskFactor45("plaque","Control de biopelícula insuficiente","r"),RiskFactor45("saliva","Hiposalivación / xerostomía referida o medida","r"),RiskFactor45("appliance","Aparato que dificulta higiene","r"),RiskFactor45("social","Barreras relevantes para prevención/atención","r"),RiskFactor45("fluoride","Uso apropiado de pasta fluorada","p"),RiskFactor45("professional","Medidas profesionales preventivas indicadas","p"),RiskFactor45("hygiene","Higiene efectiva / apoyo del cuidador","p"),RiskFactor45("diet","Frecuencia de azúcares controlada","p"))
 val states=factors.associate{it.key to rememberRecordState("cariesRisk."+it.key,"No valorado")}
 ResponsiveScreenV17("🛡️ "+tr(lang,"Ficha de Caries y Riesgo de Caries","Caries and Caries-Risk Sheet"),tr(lang,"Instrumento educativo propio de la app, multifactorial y no equivalente ni afiliado a CAMBRA. Organiza enfermedad observada, factores de riesgo y protección; no sustituye una herramienta validada ni el juicio clínico.","App-owned multifactorial educational instrument; it is not CAMBRA and is not affiliated with CAMBRA. It organizes observed disease, risk and protective factors; it does not replace a validated tool or clinical judgment."),onBack){p->
  val cols=if(p.largeSystemText)1 else if(p.width==ScreenWidthV17.COMPACT)2 else 3
  ResponsiveSectionV17(tr(lang,"Factores","Factors"),tr(lang,"Marca Presente, Ausente o No valorado. No asumas un dato que no fue interrogado o explorado.","Mark Present, Absent or Not assessed. Do not assume information that was not obtained.")){
   factors.forEach{f->val st=states.getValue(f.key);Text(f.label,fontWeight=FontWeight.SemiBold);AdaptiveGridV17(3,cols){i->val o=listOf("Presente","Ausente","No valorado")[i];FilterChip(st.value==o,{st.value=o},{Text(o)},Modifier.fillMaxWidth())}}
  }
  val disease=factors.count{it.kind=="d"&&states.getValue(it.key).value=="Presente"}; val risks=factors.count{it.kind=="r"&&states.getValue(it.key).value=="Presente"}; val protective=factors.count{it.kind=="p"&&states.getValue(it.key).value=="Presente"}; val unknown=factors.count{states.getValue(it.key).value=="No valorado"}
  ResponsiveSectionV17(tr(lang,"Resumen descriptivo de la app","App descriptive summary"),tr(lang,"Los conteos ayudan a revisar el caso; no son una puntuación validada ni generan diagnóstico automático.","Counts help review the case; they are not a validated score and do not generate an automatic diagnosis.")){
   Text(tr(lang,"Indicadores de enfermedad presentes: "+disease+" · factores de riesgo presentes: "+risks+" · factores protectores presentes: "+protective+" · no valorados: "+unknown+".","Disease indicators present: "+disease+" · risk factors present: "+risks+" · protective factors present: "+protective+" · not assessed: "+unknown+"."),fontWeight=FontWeight.Bold)
   PText45("cariesRisk.context",tr(lang,"Contexto clínico / índices / saliva / auxiliares","Clinical context / indices / saliva / aids")); PText45("cariesRisk.plan",tr(lang,"Plan preventivo individualizado y fecha de reevaluación","Individualized preventive plan and reassessment date"))
  }
  NoticeCard(tr(lang,"La categoría final de riesgo debe establecerse integrando historia, exploración, actividad de lesiones, dieta, flúor, saliva y otros factores pertinentes. Esta ficha evita convertir un conteo aislado en diagnóstico.","Final risk category should integrate history, examination, lesion activity, diet, fluoride, saliva and other relevant factors. This sheet avoids turning an isolated count into a diagnosis."))
 }
}
