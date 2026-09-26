package com.yomismtz.expedientedeldentista.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.DiseaseAnswer
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

private data class Disease37(val id:String,val name:String,val meds:String,val protocol:String)
private data class Category37(val name:String,val diseases:List<Disease37>)
private fun d37(id:String,name:String,protocol:String="general")=Disease37(id,name,"Registrar tratamiento referido por el paciente.",protocol)
private val categories37=listOf(
 Category37("Endocrinas y metabólicas",listOf(d37("dm1","Diabetes mellitus tipo 1","diabetes"),d37("dm2","Diabetes mellitus tipo 2","diabetes"),d37("prediabetes","Prediabetes","diabetes"),d37("hypothyroid","Hipotiroidismo","thyroid"),d37("hyperthyroid","Hipertiroidismo","thyroid"),d37("hashimoto","Tiroiditis de Hashimoto","thyroid"),d37("obesity","Obesidad"),d37("metabolic","Síndrome metabólico"))),
 Category37("Cardiovasculares",listOf(d37("htn","Hipertensión arterial","hypertension"),d37("ischemic","Cardiopatía isquémica / antecedente de infarto","cardio"),d37("angina","Angina de pecho","cardio"),d37("arrhythmia","Arritmias","cardio"),d37("heart_failure","Insuficiencia cardiaca","cardio"),d37("valvular","Valvulopatía","cardio"),d37("congenital_heart","Cardiopatía congénita","cardio"),d37("stroke","Antecedente de evento cerebrovascular","cardio"))),
 Category37("Respiratorias",listOf(d37("asthma","Asma","respiratory"),d37("copd","EPOC","respiratory"),d37("chronic_bronchitis","Bronquitis crónica","respiratory"),d37("emphysema","Enfisema","respiratory"),d37("sleep_apnea","Apnea obstructiva del sueño","respiratory"),d37("pulmonary_fibrosis","Fibrosis pulmonar","respiratory"),d37("pneumonia","Neumonía recurrente / antecedente relevante","respiratory"),d37("tuberculosis","Tuberculosis","respiratory"))),
 Category37("Alérgicas",listOf(d37("drug_allergy","Alergia a medicamentos","allergy"),d37("latex_allergy","Alergia al látex","allergy"),d37("food_allergy","Alergia alimentaria","allergy"),d37("allergic_rhinitis","Rinitis alérgica","allergy"),d37("urticaria","Urticaria","allergy"),d37("atopic_dermatitis","Dermatitis atópica","allergy"),d37("anaphylaxis","Antecedente de anafilaxia","allergy"),d37("contact_dermatitis","Dermatitis de contacto","allergy"))),
 Category37("Exantemáticas e infecciosas",listOf(d37("measles","Sarampión","exanthem"),d37("rubella","Rubéola","exanthem"),d37("varicella","Varicela","exanthem"),d37("mumps","Parotiditis (paperas)","exanthem"),d37("scarlet","Escarlatina","exanthem"),d37("roseola","Exantema súbito / roséola","exanthem"),d37("fifth","Eritema infeccioso / quinta enfermedad","exanthem"),d37("hfmd","Enfermedad mano-pie-boca","exanthem"))),
 Category37("Autoinmunes",listOf(d37("lupus","Lupus eritematoso sistémico","immune"),d37("ra","Artritis reumatoide","immune"),d37("sjogren","Síndrome de Sjögren","immune"),d37("scleroderma","Esclerosis sistémica / esclerodermia","immune"),d37("psoriasis","Psoriasis / artritis psoriásica","immune"),d37("celiac","Enfermedad celíaca","immune"),d37("multiple_sclerosis","Esclerosis múltiple","immune"),d37("vasculitis","Vasculitis autoinmune","immune"))),
 Category37("Osteomusculares y óseas",listOf(d37("osteoporosis","Osteoporosis","bone"),d37("osteopenia","Osteopenia","bone"),d37("paget","Enfermedad de Paget ósea","bone"),d37("osteogenesis","Osteogénesis imperfecta","bone"),d37("osteoarthritis","Osteoartrosis","bone"),d37("fibromyalgia","Fibromialgia","bone"),d37("muscular_dystrophy","Distrofia muscular","bone"),d37("osteomyelitis","Antecedente de osteomielitis","bone"))),
 Category37("Hematológicas",listOf(d37("iron_anemia","Anemia ferropénica","hematologic"),d37("b12_anemia","Anemia por vitamina B12/folato","hematologic"),d37("sickle","Enfermedad de células falciformes","hematologic"),d37("hemophilia_a","Hemofilia A","hematologic"),d37("hemophilia_b","Hemofilia B","hematologic"),d37("vwd","Enfermedad de von Willebrand","hematologic"),d37("thrombocytopenia","Trombocitopenia","hematologic"),d37("thrombophilia","Trombofilia / antecedente trombótico","hematologic"))),
 Category37("Renales y urinarias",listOf(d37("ckd","Enfermedad renal crónica","renal"),d37("renal_failure","Insuficiencia renal avanzada","renal"),d37("dialysis","Paciente en diálisis","renal"),d37("renal_transplant","Trasplante renal","renal"),d37("glomerulonephritis","Glomerulonefritis","renal"),d37("nephrotic","Síndrome nefrótico","renal"),d37("polycystic","Enfermedad renal poliquística","renal"),d37("recurrent_uti","Infecciones urinarias recurrentes","renal"))),
 Category37("Gastrointestinales y hepáticas",listOf(d37("gerd","Reflujo gastroesofágico","gi"),d37("gastritis","Gastritis","gi"),d37("peptic_ulcer","Úlcera péptica","gi"),d37("celiac_gi","Enfermedad celíaca","gi"),d37("crohn","Enfermedad de Crohn","gi"),d37("ulcerative_colitis","Colitis ulcerosa","gi"),d37("hepatitis","Hepatitis viral / crónica","liver"),d37("cirrhosis","Cirrosis / enfermedad hepática crónica","liver"))),
 Category37("Neurológicas",listOf(d37("epilepsy","Epilepsia / convulsiones","neuro"),d37("migraine","Migraña","neuro"),d37("parkinson","Enfermedad de Parkinson","neuro"),d37("alzheimer","Enfermedad de Alzheimer / demencia","neuro"),d37("multiple_sclerosis_neuro","Esclerosis múltiple","neuro"),d37("neuropathy","Neuropatía periférica","neuro"),d37("cerebral_palsy","Parálisis cerebral","neuro"),d37("stroke_neuro","Secuelas de evento cerebrovascular","neuro"))),
 Category37("Neoplásicas",listOf(d37("oral_cancer","Cáncer oral / orofaríngeo","oncology"),d37("breast_cancer","Cáncer de mama","oncology"),d37("prostate_cancer","Cáncer de próstata","oncology"),d37("lung_cancer","Cáncer pulmonar","oncology"),d37("colorectal_cancer","Cáncer colorrectal","oncology"),d37("leukemia","Leucemia","oncology"),d37("lymphoma","Linfoma","oncology"),d37("myeloma","Mieloma múltiple","oncology"))),
 Category37("VIH e inmunodeficiencias",listOf(d37("hiv","VIH","immune"),d37("aids","VIH con antecedente de enfermedad avanzada/SIDA referido","immune"),d37("primary_immune","Inmunodeficiencia primaria","immune"),d37("transplant_immune","Inmunosupresión por trasplante","immune"),d37("steroid_immune","Inmunosupresión por corticoides","immune"),d37("biologic_immune","Inmunosupresión por terapia biológica","immune"),d37("chemo_immune","Inmunosupresión por quimioterapia","immune"),d37("other_immune","Otra inmunodeficiencia diagnosticada","immune")))
)
private fun asa37(map:Map<String,DiseaseAnswer>):Int{val a=map.values.filter{it.present};if(a.isEmpty())return 1;if(a.any{it.currentStatus.contains("inestable",true)||it.currentStatus.contains("amenaza",true)||it.currentStatus.contains("sever",true)})return 4;if(a.any{it.currentStatus.contains("mal control",true)||it.complications.isNotBlank()})return 3;return 2}

@Composable fun PathologicalHistory37Screen(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onProtocols:()->Unit,onBack:()->Unit){
 var category by remember{mutableStateOf<Category37?>(null)}
 var disease by remember{mutableStateOf<Disease37?>(null)}
 val saved=session.history.diseases
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{ScreenHeader("Antecedentes personales patológicos",onBack,"Selecciona una clasificación y después una enfermedad. La rejilla muestra 2 columnas en pantallas pequeñas y 3 cuando hay más espacio.")}
  item{NoticeCard("ASA es una orientación educativa: depende de gravedad, control, repercusión sistémica y valoración completa; el diagnóstico por sí solo no determina la clase.")}
  item{Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Column(Modifier.padding(14.dp)){Text("ASA "+asa37(saved)+" · orientación automática",fontWeight=FontWeight.Black);Text("Debe confirmarse clínicamente y con supervisión docente.")}}}
  if(category==null){
   item{
    BoxWithConstraints(Modifier.fillMaxWidth()){
     val columns=if(maxWidth<700.dp)2 else 3
     Column(verticalArrangement=Arrangement.spacedBy(9.dp)){
      categories37.chunked(columns).forEach{row->
       Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(9.dp)){
        row.forEach{x->
         Card(onClick={category=x},modifier=Modifier.weight(1f)){
          Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
           Text(x.name,fontWeight=FontWeight.Bold)
           Text("Toca para ver enfermedades frecuentes y relevantes",style=MaterialTheme.typography.bodySmall)
          }
         }
        }
        repeat(columns-row.size){Spacer(Modifier.weight(1f))}
       }
      }
     }
    }
   }
  }else if(disease==null){
   item{Button(onClick={category=null}){Text("← Clasificaciones")}}
   item{
    BoxWithConstraints(Modifier.fillMaxWidth()){
     val columns=if(maxWidth<700.dp)2 else 3
     Column(verticalArrangement=Arrangement.spacedBy(9.dp)){
      category!!.diseases.chunked(columns).forEach{row->
       Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(9.dp)){
        row.forEach{x->
         Card(onClick={disease=x},modifier=Modifier.weight(1f)){
          Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
           Text(x.name,fontWeight=FontWeight.Bold)
           if(saved[x.id]?.present==true)Text("✓ Guardado en esta sesión",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.primary)
          }
         }
        }
        repeat(columns-row.size){Spacer(Modifier.weight(1f))}
       }
      }
     }
    }
   }
  }else{
   item{DiseaseEditor37(disease!!,saved[disease!!.id]?:DiseaseAnswer(),{a->
    val n=saved.toMutableMap()
    n[disease!!.id]=a
    onSessionChanged(session.copy(history=session.history.copy(diseases=n,asaClass=asa37(n))))
    disease=null
   },onProtocols){disease=null}}
  }
 }
}
private fun presetTreatments37(d:Disease37):List<String> = when(d.protocol){
 "diabetes" -> listOf("Metformina referida","SGLT2 referido","GLP-1 / GIP-GLP-1 referido","Insulina basal referida","Insulina basal-bolo referida","Combinación de fármacos referida","Cambios de estilo de vida / educación","Otro esquema indicado por su médico","No toma tratamiento")
 "hypertension","cardio" -> listOf("IECA referido","ARA-II referido","Calcioantagonista referido","Diurético referido","Betabloqueador referido","Antiagregante/anticoagulante referido cuando corresponda","Combinación referida","Otro","No toma tratamiento")
 "respiratory" -> listOf("Inhalador de rescate referido","Corticoide inhalado referido","Broncodilatador de acción prolongada referido","Combinación de inhaladores","Oxígeno referido","Otro","No toma tratamiento")
 "thyroid" -> listOf("Levotiroxina referida","Antitiroideo referido","Yodo/radioyodo antecedente","Otro","No toma tratamiento")
 "immune" -> listOf("Inmunomodulador/inmunosupresor referido","Corticoide referido","Antirretroviral referido cuando corresponda","Biológico referido","Otro","No toma tratamiento")
 "bone" -> listOf("Bisfosfonato referido","Denosumab referido","Calcio/vitamina D referidos","Otro tratamiento óseo","No toma tratamiento")
 "hematologic" -> listOf("Hierro referido","Ácido fólico/B12 referido","Anticoagulante referido","Factor/hemoderivado referido","Otro","No toma tratamiento")
 "renal" -> listOf("Tratamiento renal referido","Diálisis","Trasplante + inmunosupresión referida","Otro","No toma tratamiento")
 "liver","gi" -> listOf("Tratamiento gastrointestinal/hepático referido","Antiviral referido cuando corresponda","Protector/antisecretor referido","Otro","No toma tratamiento")
 "neuro" -> listOf("Anticonvulsivante referido","Otro tratamiento neurológico","No toma tratamiento")
 "oncology" -> listOf("Quimioterapia actual/previa","Radioterapia actual/previa","Terapia dirigida/inmunoterapia referida","Seguimiento sin tratamiento activo","Otro")
 "exanthem" -> listOf("Tratamiento sintomático referido","Antiviral referido cuando correspondió","Sin tratamiento / resuelto","Otro")
 else -> listOf("Tratamiento farmacológico referido","Tratamiento no farmacológico referido","Otro","No toma tratamiento")
}
@Composable private fun DiseaseEditor37(d:Disease37,initial:DiseaseAnswer,onSave:(DiseaseAnswer)->Unit,onProtocol:()->Unit,onBack:()->Unit){
 var onset by remember(d.id){mutableStateOf(initial.onset)};var treatment by remember(d.id){mutableStateOf(initial.treatment)};var status by remember(d.id){mutableStateOf(initial.currentStatus)};var complications by remember(d.id){mutableStateOf(initial.complications)}
 val dates=listOf("Diagnóstico este año","1–2 años","3–5 años","6–10 años","Más de 10 años","Desde la infancia","No recuerda")
 val states=listOf("Controlado según seguimiento médico referido","Descontrolado referido","En tratamiento, control no conocido","Sin tratamiento actualmente","Suspendió tratamiento","En estudio / diagnóstico reciente","Resuelto / antecedente, cuando aplique","No sabe")
 Column(verticalArrangement=Arrangement.spacedBy(9.dp)){
  Button(onClick=onBack){Text("← Enfermedades")};Text(d.name,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black)
  Text("1 · ¿Desde cuándo?",fontWeight=FontWeight.Bold);dates.forEach{x->FilterChip(onset==x,{onset=x},{Text(x)},modifier=Modifier.fillMaxWidth())}
  Text("2 · Tratamiento referido",fontWeight=FontWeight.Bold);presetTreatments37(d).forEach{x->FilterChip(treatment==x,{treatment=x},{Text(x)},modifier=Modifier.fillMaxWidth())}
  Text("3 · Estado actual",fontWeight=FontWeight.Bold);states.forEach{x->FilterChip(status==x,{status=x},{Text(x)},modifier=Modifier.fillMaxWidth())}
  Text("4 · Complicaciones referidas",fontWeight=FontWeight.Bold);listOf("Ninguna referida","Sí, en seguimiento","Sí, antecedente resuelto","No sabe / no recuerda").forEach{x->FilterChip(complications==x,{complications=x},{Text(x)},modifier=Modifier.fillMaxWidth())}
  if(d.protocol=="diabetes")NoticeCard("En diabetes tipo 2 los estándares actuales recomiendan tratamiento individualizado según metas, comorbilidades cardiovasculares/renales, riesgo de hipoglucemia, tolerancia y preferencias. Estas opciones sirven para registrar lo que el paciente ya usa; no son un esquema automático de prescripción.")
  if(d.protocol=="exanthem")NoticeCard("Registrar edad al padecerla, tratamiento recibido y complicaciones; una imagen aislada no confirma el diagnóstico.")
  Button(enabled=onset.isNotBlank()&&treatment.isNotBlank()&&status.isNotBlank(),onClick={onSave(DiseaseAnswer(true,onset,treatment,status,complications))},modifier=Modifier.fillMaxWidth()){Text("💾 Guardar antecedente")}
  Button(onClick=onProtocol,modifier=Modifier.fillMaxWidth()){Text("📚 Consultar protocolo odontológico")}
 }
}
@Composable fun SystemicProtocols37Screen(lang:String,onBack:()->Unit){
 var selected by remember{mutableStateOf<Pair<String,String>?>(null)}
 var carePlan by remember{mutableStateOf<String?>(null)}
 val p=listOf(
  "Diabetes mellitus" to "Confirmar tipo, tratamiento, control referido, alimentación y antecedentes de hipoglucemia.",
  "Hipertensión / cardiopatía" to "Confirmar diagnóstico, tratamiento, control, signos vitales, capacidad funcional y anticoagulación/antiagregación cuando corresponda.",
  "Asma / EPOC" to "Identificar control, desencadenantes, inhaladores, exacerbaciones recientes y capacidad respiratoria.",
  "Enfermedad renal / diálisis" to "Precisar función renal, diálisis o trasplante, medicamentos, sangrado y coordinación médica cuando corresponda.",
  "Enfermedad hepática" to "Precisar diagnóstico, función hepática, medicamentos y antecedentes de sangrado o descompensación.",
  "Trastornos hematológicos" to "Precisar diagnóstico, gravedad, tratamiento, antecedentes de sangrado/trombosis y estudios indicados para el procedimiento.",
  "VIH / inmunodeficiencia" to "Registrar control médico, tratamiento referido, infecciones o lesiones orales e interacciones; mantener confidencialidad y evitar estigma.",
  "Trastornos tiroideos" to "Precisar hipo/hipertiroidismo, tratamiento, control y datos de descompensación.",
  "Osteoporosis / antirresortivos" to "Documentar fármaco, vía, indicación, duración y antecedentes de procedimientos óseos antes de cirugía dentoalveolar.",
  "Epilepsia" to "Registrar control, última crisis, desencadenantes y tratamiento; preparar medidas de seguridad.",
  "Cáncer / inmunosupresión" to "Identificar tratamiento activo, radioterapia de cabeza/cuello, estado hematológico cuando corresponda y coordinación oncológica.",
  "Alergias / anafilaxia" to "Confirmar sustancia, reacción, gravedad, fecha, atención requerida y alternativas seguras.",
  "TDAH" to "Confirmar diagnóstico y tratamiento referido, horario y duración habitual de la cita, capacidad de atención, impulsividad, ansiedad, experiencias odontológicas previas y estrategias que facilitan la cooperación. Preferir instrucciones breves, secuenciales, refuerzo positivo y un plan individualizado.",
  "Trastorno del espectro autista (TEA)" to "Preguntar directamente al paciente o cuidador sobre comunicación preferida, sensibilidad a luz, sonido, tacto, sabores u olores, desencadenantes, rutinas y estrategias que funcionan. Considerar desensibilización, apoyos visuales, ambiente sensorial adaptado y citas individualizadas.",
  "Discapacidad intelectual / del desarrollo" to "Valorar comunicación, comprensión, autonomía para higiene oral, apoyos necesarios, consentimiento o asentimiento cuando corresponda, medicación y comorbilidades. Adaptar instrucciones y prevención al nivel funcional individual.",
  "Síndrome de Down" to "Revisar antecedentes médicos y cardiacos, vía aérea y apnea del sueño, función tiroidea, medicación, capacidad de cooperación y necesidades de apoyo. No asumir riesgos ni limitaciones sólo por el diagnóstico; individualizar el manejo.",
  "Parálisis cerebral / trastorno motor" to "Identificar movilidad, postura y transferencia seguras, control cefálico, deglución, riesgo de aspiración, reflejos, comunicación, medicación y apoyo del cuidador. Adaptar posición, tiempos y dispositivos a la función individual.",
  "Parálisis facial" to "Registrar inicio y evolución, lado afectado, cierre ocular, movilidad facial, alteraciones del habla, masticación, deglución o control labial, dolor u otros síntomas neurológicos, diagnóstico médico y tratamiento referido. Adaptar el manejo odontológico a la función individual y derivar para valoración médica cuando el cuadro sea reciente, progresivo, recurrente o presente signos de alarma.",
  "Trastornos de ansiedad / fobia dental" to "Identificar desencadenantes, experiencias previas, estrategias de afrontamiento y preferencias del paciente. Considerar comunicación anticipatoria, tell-show-do, respiración, distracción, desensibilización y otras técnicas de guía de conducta según necesidad."
 )
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{ScreenHeader("Protocolos para pacientes sistémicos",onBack,"Selecciona una condición. El protocolo se abre por apartados; no es una lista estática.")}
  if(selected==null){
   item{NoticeCard("Guía educativa. La condición sistémica no genera automáticamente antibiótico, suspensión de anticoagulantes ni autorización para tratar. La decisión depende del control, procedimiento, medicamentos, hallazgos e indicaciones médicas vigentes.")}
   items(p.chunked(2)){row->
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)){
     row.forEach{x->Card(onClick={selected=x},modifier=Modifier.weight(1f)){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text(x.first,fontWeight=FontWeight.Black);Text("Abrir protocolo ›",color=MaterialTheme.colorScheme.primary)}}}
     if(row.size==1)Spacer(Modifier.weight(1f))
    }
   }
  }else{
   val x=selected!!
   item{OutlinedButton(onClick={selected=null;carePlan=null}){Text("← Condiciones")}}
   item{Text(x.first,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black)}
   item{ResponsiveSectionV17("1 · Signos, síntomas y control","Qué confirmar antes de decidir el manejo"){Text(x.second);Text("Selecciona y confirma el estado clínico en la historia; no asumir control sólo por el nombre del diagnóstico.")}}
   item{ResponsiveSectionV17("2 · Estudios y marcadores","Solicitar o revisar sólo cuando cambien la seguridad o la conducta clínica"){
    val specific=when(x.first){
     "TDAH" -> "No existe un estudio de laboratorio odontológico rutinario específico por TDAH. Revisar diagnóstico, medicación referida, efectos adversos relevantes y comorbilidades; solicitar información médica sólo si modifica la seguridad del tratamiento."
     "Trastorno del espectro autista (TEA)" -> "No existe un marcador de laboratorio odontológico rutinario específico por TEA. La valoración se centra en comunicación, perfil sensorial, conducta adaptativa, medicación, comorbilidades y necesidades individuales."
     "Discapacidad intelectual / del desarrollo" -> "No existe un panel universal. Revisar los estudios relacionados con la etiología, comorbilidades y medicamentos sólo cuando sean pertinentes para el procedimiento odontológico."
     "Síndrome de Down" -> "Revisar antecedentes y controles médicos pertinentes a las comorbilidades presentes, por ejemplo cardiopatía, función tiroidea, apnea del sueño o alteraciones hematológicas. No solicitar estudios de rutina únicamente por el diagnóstico."
     "Parálisis cerebral / trastorno motor" -> "No existe un panel odontológico universal. Revisar estudios o informes relacionados con deglución, vía aérea, epilepsia, nutrición, movilidad y otras comorbilidades cuando cambien el manejo."
     "Parálisis facial" -> "La causa y el tiempo de evolución determinan la valoración médica. En odontología documentar función facial y oral; no existe un estudio de laboratorio universal. Un inicio agudo o signos neurológicos asociados requieren valoración médica urgente."
     "Trastornos de ansiedad / fobia dental" -> "No existe un marcador de laboratorio odontológico rutinario. Valorar desencadenantes, intensidad, medicación, experiencias previas y repercusión en la atención; interconsultar cuando sea necesario."
     else -> "Revisar estudios recientes pertinentes al diagnóstico y al procedimiento. No existe un panel universal para todos los pacientes sistémicos; usar protocolo institucional e interconsulta cuando esté indicada."
    };Text(specific)
   }}
   item{ResponsiveSectionV17("3 · Atención odontológica","Decidir tratar, modificar, posponer o interconsultar"){val careOptions=listOf("Atención habitual si está estable","Modificar plan / cita","Posponer atención electiva","Interconsulta médica"); ChipChoices(careOptions.map{it to (carePlan==it)},{carePlan=careOptions[it]},2); carePlan?.let{Text("Selección educativa: $it",fontWeight=FontWeight.Bold)}}}
   item{ResponsiveSectionV17("4 · Anestesia","La elección depende de enfermedad, control, medicamentos y procedimiento"){Text("Comprobar anestésico, vasoconstrictor, dosis máxima aplicable, interacciones y contraindicaciones antes de administrar. Evitar reglas universales por diagnóstico.")}}
   item{ResponsiveSectionV17("5 · Analgesia y antiinflamatorios","Seleccionar según antecedentes y tratamiento actual"){Text("Revisar riesgo renal, hepático, gastrointestinal, cardiovascular, hemorrágico e interacciones. No indicar AINE automáticamente.")}}
   item{ResponsiveSectionV17("6 · Antibióticos","No se indican por el solo hecho de tener una enfermedad sistémica"){Text("Usar antibiótico sólo cuando exista una indicación independiente o profilaxis específicamente indicada por una guía vigente. Verificar alergias, función renal/hepática e interacciones.")}}
   item{ResponsiveSectionV17("7 · Procedimientos y urgencias","Plan de seguridad"){Text("Definir qué procedimientos son apropiados según estabilidad, invasividad y riesgo; reconocer signos de descompensación y contar con plan de urgencias e interconsulta.")}}
   item{NoticeCard("Antes de usar este protocolo en un paciente real, confirmar diagnóstico, control, medicamentos y recomendaciones institucionales vigentes.")}
  }
 }
}
