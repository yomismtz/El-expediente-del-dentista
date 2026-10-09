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
 Category37("VIH e inmunodeficiencias",listOf(d37("hiv","VIH","immune"),d37("aids","VIH con antecedente de enfermedad avanzada/SIDA referido","immune"),d37("primary_immune","Inmunodeficiencia primaria","immune"),d37("transplant_immune","Inmunosupresión por trasplante","immune"),d37("steroid_immune","Inmunosupresión por corticoides","immune"),d37("biologic_immune","Inmunosupresión por terapia biológica","immune"),d37("chemo_immune","Inmunosupresión por quimioterapia","immune"),d37("other_immune","Otra inmunodeficiencia diagnosticada","immune"))),
 Category37("Genéticas / hereditarias",listOf(
  d37("down","Síndrome de Down","genetic"),
  d37("hemophilia_genetic","Hemofilia","genetic"),
  d37("cystic_fibrosis","Fibrosis quística","genetic"),
  d37("turner","Síndrome de Turner","genetic"),
  d37("pku","Fenilcetonuria","genetic"),
  d37("cah","Hiperplasia suprarrenal congénita","genetic"),
  d37("gaucher","Enfermedad de Gaucher","genetic"),
  d37("genetic_other","Otra enfermedad genética / hereditaria","genetic")
 ))
)
private fun asa37(map:Map<String,DiseaseAnswer>,tobacco:String="",alcohol:String=""):Int{
 val active=map.values.filter{it.present}
 val tobaccoCurrent=tobacco.isNotBlank() && tobacco !in listOf("No fuma","Exfumador","No sabe / no recuerda")
 val alcoholCurrent=alcohol.isNotBlank() && alcohol !in listOf("No consume","No sabe / no recuerda")
 val alcoholSevere=alcohol.contains("dependencia",true) || alcohol.contains("abuso",true)

 var level=when{
  alcoholSevere -> 3
  tobaccoCurrent || alcoholCurrent -> 2
  else -> 1
 }

 if(active.any{
   it.currentStatus.contains("inestable",true) ||
   it.currentStatus.contains("descompensación grave",true) ||
   it.currentStatus.contains("amenaza constante",true)
  }) level=maxOf(level,4)
 else if(active.any{
  it.currentStatus.contains("descontrolado",true) ||
  it.currentStatus.contains("mal control",true) ||
  it.currentStatus.contains("limitación funcional importante",true) ||
  it.complications.equals("Sí, en seguimiento",true)
 }) level=maxOf(level,3)
 else if(active.isNotEmpty()) level=maxOf(level,2)

 return level
}

@Composable fun PathologicalHistory37Screen(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onProtocols:()->Unit,onBack:()->Unit){
 var category by remember{mutableStateOf<Category37?>(null)}
 var disease by remember{mutableStateOf<Disease37?>(null)}
 var tobacco by rememberRecordState("history.path.tobacco","")
 var alcohol by rememberRecordState("history.path.alcohol","")
 var noPathologicalHistory by rememberRecordState("history.path.noneDenied",false)
 var noAllergies by rememberRecordState("history.path.allergies.noneDenied",false)
 val saved=session.history.diseases
 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{ScreenHeader("Antecedentes personales patológicos",onBack,"Primero registra tabaco y alcohol. Después selecciona una clasificación de enfermedades → enfermedad → datos del antecedente. Al guardar regresarás a la lista de enfermedades.")}
  if(category==null){
   item{SectionCard("Clasificación ASA clínica"){
    val asaOptions=listOf(1 to "ASA I",2 to "ASA II",3 to "ASA III",4 to "ASA IV",5 to "ASA V",6 to "ASA VI")
    Text("Registra la clasificación confirmada por el profesional. La estimación automática basada en antecedentes es sólo educativa y no sustituye la valoración clínica.",style=MaterialTheme.typography.bodySmall)
    ChipChoices(asaOptions.map{it.second to (session.history.asaClassClinician==it.first)},{i->
     onSessionChanged(session.copy(history=session.history.copy(asaClassClinician=asaOptions[i].first)))
    },columns=3)
    OutlinedButton(onClick={onSessionChanged(session.copy(history=session.history.copy(asaClassClinician=null)))},modifier=Modifier.fillMaxWidth()){
     Text("Dejar ASA clínica como no evaluada")
    }
    Text(if(session.history.asaClassClinician==null) "Estado: pendiente; no se confunde con ASA I por defecto." else "ASA clínica registrada: ASA ${session.history.asaClassClinician}.",style=MaterialTheme.typography.bodySmall)
   }}
   item{SectionCard("1 · Tabaco y alcohol"){
    Text("Registra primero las exposiciones personales. Después podrás entrar directamente a las clasificaciones de enfermedades.",style=MaterialTheme.typography.bodySmall)
    Text("¿Fuma / usa nicotina?",fontWeight=FontWeight.Bold)
    val tobaccoOptions=listOf("No fuma","Exfumador","Ocasional","Eventual · 1–3 días/semana","Frecuente · 4–6 días/semana","Diario","Vapeo / nicotina actual","No sabe / no recuerda")
    ChipChoices(tobaccoOptions.map{x->x to (tobacco==x)},{i->
     tobacco=tobaccoOptions[i]
     onSessionChanged(session.copy(history=session.history.copy(asaClass=asa37(saved,tobacco,alcohol))))
    },columns=2)
    Spacer(Modifier.height(10.dp))
    Text("¿Ingiere alcohol?",fontWeight=FontWeight.Bold)
    val alcoholOptions=listOf("No consume","Ocasional / social","Eventual · 1–3 días/semana","Frecuente · 4–6 días/semana","Diario","Dependencia / abuso referido","No sabe / no recuerda")
    ChipChoices(alcoholOptions.map{x->x to (alcohol==x)},{i->
     alcohol=alcoholOptions[i]
     onSessionChanged(session.copy(history=session.history.copy(asaClass=asa37(saved,tobacco,alcohol))))
    },columns=2)
    if(tobacco.isNotBlank() || alcohol.isNotBlank()){
     Spacer(Modifier.height(8.dp))
     Text("Exposiciones registradas",fontWeight=FontWeight.SemiBold)
     Text(
      listOfNotNull(
       tobacco.takeIf{it.isNotBlank()}?.let{"Tabaco/nicotina: $it"},
       alcohol.takeIf{it.isNotBlank()}?.let{"Alcohol: $it"}
      ).joinToString(" · "),
      style=MaterialTheme.typography.bodySmall
     )
    }
   }}
   item{SectionCard("2 · Antecedentes personales patológicos"){
    Text("Si el paciente niega enfermedades o antecedentes patológicos, puedes dejarlo asentado explícitamente sin recorrer todas las clasificaciones.",style=MaterialTheme.typography.bodySmall)
    val hasPositivePathology=saved.values.any{it.present}
    FilterChip(selected=noPathologicalHistory,onClick={
     if(!noPathologicalHistory && !hasPositivePathology){
      noPathologicalHistory=true
      onSessionChanged(session.copy(history=session.history.copy(diseases=emptyMap(),asaClass=asa37(emptyMap(),tobacco,alcohol))))
     }else if(noPathologicalHistory){
      noPathologicalHistory=false
     }
    },label={Text("Negado · ningún antecedente personal patológico")},modifier=Modifier.fillMaxWidth())
    if(hasPositivePathology && !noPathologicalHistory) Text("Para marcar «Negado», primero revisa los antecedentes positivos registrados; la opción negativa no elimina datos clínicos existentes.",style=MaterialTheme.typography.bodySmall)
    if(noPathologicalHistory) Text("Registro: paciente niega antecedentes personales patológicos de las clasificaciones disponibles.",style=MaterialTheme.typography.bodySmall)
   }}
   item{
    Text("Alergias",fontWeight=FontWeight.Bold)
    val hasPositiveAllergy=saved.any{it.key in setOf("drug_allergy","latex_allergy","food_allergy","allergic_rhinitis","urticaria","atopic_dermatitis","anaphylaxis","contact_dermatitis") && it.value.present}
    FilterChip(selected=noAllergies,onClick={
     if(!noAllergies && !hasPositiveAllergy){
      noAllergies=true
     }else if(noAllergies){
      noAllergies=false
     }
    },label={Text("Negado · ninguna alergia conocida")},modifier=Modifier.fillMaxWidth())
    if(hasPositiveAllergy && !noAllergies) Text("Para marcar «Negado», primero revisa las alergias positivas registradas; la opción negativa no elimina datos clínicos existentes.",style=MaterialTheme.typography.bodySmall)
    if(noAllergies) Text("Registro explícito: el paciente no refiere alergias conocidas.",style=MaterialTheme.typography.bodySmall)
   }
   item{Text("3 · Clasificaciones de enfermedades",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold)}
   item{
    BoxWithConstraints(Modifier.fillMaxWidth()){
     val columns=if(maxWidth<700.dp)2 else 3
     Column(verticalArrangement=Arrangement.spacedBy(9.dp)){
      categories37.chunked(columns).forEach{row->
       Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(9.dp)){
        row.forEach{x->
         Card(onClick={noPathologicalHistory=false;if(x.name=="Alérgicas")noAllergies=false;category=x},modifier=Modifier.weight(1f)){
          Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
           Text(x.name,fontWeight=FontWeight.Bold)
           Text("${x.diseases.count{(saved[it.id]?.let { answer -> answer.recorded || answer.present } == true)}}/${x.diseases.size} evaluados",style=MaterialTheme.typography.bodySmall)
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
   item{Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Button(onClick={category=null}){Text("← Clasificaciones")};Text("${category!!.diseases.count{(saved[it.id]?.let { answer -> answer.recorded || answer.present } == true)}} evaluados",style=MaterialTheme.typography.bodySmall,fontWeight=FontWeight.SemiBold)}}
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
    onSessionChanged(session.copy(history=session.history.copy(diseases=n,asaClass=asa37(n,tobacco,alcohol))))
    disease=null
   },onProtocols){disease=null}}
  }
 }
}
private fun presetTreatments37(d:Disease37):List<String> = when(d.id){
 "dm1" -> listOf("Insulina basal-bolo referida","Bomba de insulina referida","Insulina basal + bolos ajustados referidos","Otro esquema de insulina indicado por endocrinología")
 "dm2" -> listOf("Metformina referida","Metformina + segundo antidiabético referido","SGLT2 / GLP-1 referido","Insulina con o sin fármacos orales referida")
 "prediabetes" -> listOf("Plan de alimentación y actividad física referido","Metformina referida","Programa de control de peso/metabólico referido","Seguimiento médico sin fármaco referido")
 "hypothyroid","hashimoto" -> listOf("Levotiroxina referida","Ajuste de hormona tiroidea referido","Seguimiento endocrinológico con mismo esquema","Otro tratamiento tiroideo referido")
 "hyperthyroid" -> listOf("Antitiroideo referido","Betabloqueador para síntomas referido","Radioyodo antecedente/referido","Cirugía tiroidea antecedente/referida")
 "obesity" -> listOf("Plan nutricional + actividad física referido","Tratamiento farmacológico para peso referido","Programa multidisciplinario de control de peso","Cirugía bariátrica antecedente/referida")
 "metabolic" -> listOf("Cambios de estilo de vida supervisados","Antihipertensivo + hipolipemiante referidos","Metformina/u otro antidiabético referido","Tratamiento combinado de los componentes metabólicos")
 "htn" -> listOf("ARA-II referido","IECA referido","Calcioantagonista referido","Combinación antihipertensiva referida")
 "ischemic","angina" -> listOf("Antiagregante + estatina referidos","Betabloqueador/antianginoso referido","Anticoagulante/antiagregante según indicación referida","Revascularización + tratamiento médico referido")
 "arrhythmia" -> listOf("Betabloqueador/antiarrítmico referido","Anticoagulante referido","Ablación antecedente/referida","Marcapasos/dispositivo + seguimiento referido")
 "heart_failure" -> listOf("ARNI/IECA/ARA-II referido","Betabloqueador referido","Diurético referido","Tratamiento combinado para insuficiencia cardiaca referido")
 "valvular","congenital_heart" -> listOf("Seguimiento cardiológico sin fármaco referido","Anticoagulación/antiagregación referida","Tratamiento para síntomas/hemodinámica referido","Cirugía/intervención cardiaca antecedente/referida")
 "stroke","stroke_neuro" -> listOf("Antiagregante referido","Anticoagulante referido","Estatina/hipolipemiante referido","Rehabilitación + control de factores de riesgo")
 "asthma" -> listOf("Corticoide inhalado referido","Corticoide inhalado + broncodilatador prolongado","Broncodilatador de rescate referido","Terapia biológica/especializada referida")
 "copd","chronic_bronchitis","emphysema" -> listOf("Broncodilatador prolongado referido","Doble broncodilatador inhalado referido","Broncodilatador + corticoide inhalado referido","Oxígeno/rehabilitación pulmonar referidos")
 "sleep_apnea" -> listOf("CPAP referido","Dispositivo de avance mandibular referido","Medidas de control de peso/posición referidas","Cirugía de vía aérea antecedente/referida")
 "pulmonary_fibrosis" -> listOf("Antifibrótico referido","Oxígeno suplementario referido","Rehabilitación pulmonar referida","Tratamiento especializado referido")
 "pneumonia" -> listOf("Antibiótico referido cuando correspondió","Tratamiento sintomático referido","Hospitalización/oxígeno antecedente","Seguimiento neumológico referido")
 "tuberculosis" -> listOf("Esquema antituberculoso de primera línea referido","Tratamiento directamente observado referido","Esquema especializado por resistencia referido","Seguimiento sin tratamiento activo referido")
 "drug_allergy","latex_allergy","food_allergy","allergic_rhinitis","urticaria","atopic_dermatitis","contact_dermatitis" -> listOf("Evitación del desencadenante referida","Antihistamínico referido","Corticoide tópico/nasal/sistémico referido","Tratamiento por alergología/inmunoterapia referido")
 "anaphylaxis" -> listOf("Autoinyector de adrenalina indicado/referido","Antihistamínico referido","Plan de evitación y emergencia referido","Seguimiento por alergología referido")
 "measles","rubella","varicella","mumps","scarlet","roseola","fifth","hfmd" -> listOf("Tratamiento sintomático referido","Antiviral referido cuando correspondió","Antibiótico referido cuando correspondió","Sin tratamiento activo / antecedente resuelto")
 "lupus","ra","sjogren","scleroderma","psoriasis","celiac","multiple_sclerosis","vasculitis","multiple_sclerosis_neuro" -> listOf("Inmunomodulador referido","Corticoide referido","Inmunosupresor referido","Terapia biológica/especializada referida")
 "osteoporosis","osteopenia" -> listOf("Bisfosfonato referido","Denosumab referido","Calcio/vitamina D referidos","Otro tratamiento antirresortivo/anabólico referido")
 "paget" -> listOf("Bisfosfonato referido","Analgésico referido","Suplementación indicada referida","Seguimiento especializado referido")
 "osteogenesis" -> listOf("Bisfosfonato referido","Fisioterapia/rehabilitación referida","Cirugía ortopédica antecedente","Seguimiento multidisciplinario referido")
 "osteoarthritis","fibromyalgia" -> listOf("Analgésico/antiinflamatorio referido","Fisioterapia/ejercicio terapéutico referido","Tratamiento neuromodulador referido","Manejo multimodal referido")
 "muscular_dystrophy" -> listOf("Corticoide referido","Rehabilitación/fisioterapia referida","Soporte respiratorio/cardiaco referido","Terapia especializada referida")
 "osteomyelitis" -> listOf("Antibiótico prolongado referido","Drenaje/desbridamiento antecedente","Cirugía ósea antecedente","Seguimiento infectológico/ortopédico referido")
 "iron_anemia" -> listOf("Hierro oral referido","Hierro intravenoso referido","Tratamiento de causa de pérdida referido","Seguimiento hematológico referido")
 "b12_anemia" -> listOf("Vitamina B12 referida","Ácido fólico referido","Suplementación combinada referida","Tratamiento de causa de malabsorción referido")
 "sickle" -> listOf("Hidroxiurea referida","Ácido fólico referido","Transfusiones referidas","Tratamiento especializado/terapia modificadora referida")
 "hemophilia_a","hemophilia_genetic" -> listOf("Factor VIII referido","Emicizumab referido","Desmopresina referida cuando aplica","Antifibrinolítico/terapia de apoyo referida")
 "hemophilia_b" -> listOf("Factor IX referido","Profilaxis con factor referida","Tratamiento a demanda con factor referido","Antifibrinolítico/terapia de apoyo referida")
 "vwd" -> listOf("Desmopresina referida cuando aplica","Concentrado de factor von Willebrand referido","Antifibrinolítico referido","Tratamiento de apoyo hematológico referido")
 "thrombocytopenia" -> listOf("Corticoide referido","Inmunoglobulina referida","Agonista de trombopoyetina referido","Tratamiento especializado según causa referido")
 "thrombophilia" -> listOf("Anticoagulante oral referido","Heparina referida","Antiagregante referido cuando aplica","Vigilancia hematológica sin fármaco activo")
 "ckd","renal_failure" -> listOf("Tratamiento nefroprotector/antihipertensivo referido","Control metabólico y dietético referido","Tratamiento de anemia/mineral óseo referido","Seguimiento nefrológico especializado referido")
 "dialysis" -> listOf("Hemodiálisis referida","Diálisis peritoneal referida","Tratamiento de anemia/mineral óseo referido","Medicamentos nefrológicos concomitantes referidos")
 "renal_transplant" -> listOf("Tacrolimus/esquema inmunosupresor referido","Ciclosporina/esquema inmunosupresor referido","Corticoide + inmunosupresores referidos","Otro esquema postrasplante referido")
 "glomerulonephritis","nephrotic" -> listOf("Corticoide referido","Inmunosupresor referido","IECA/ARA-II referido","Tratamiento nefrológico combinado referido")
 "polycystic" -> listOf("Control de presión arterial referido","Tratamiento renal protector referido","Tratamiento de complicaciones referido","Seguimiento nefrológico sin fármaco específico")
 "recurrent_uti" -> listOf("Antibiótico por episodio referido","Profilaxis antibiótica referida cuando aplica","Medidas preventivas/uroterapia referidas","Seguimiento urológico referido")
 "gerd","gastritis","peptic_ulcer" -> listOf("Inhibidor de bomba de protones referido","Antagonista H2 referido","Erradicación de H. pylori referida cuando aplica","Medidas dietéticas/antiácido referido")
 "celiac_gi" -> listOf("Dieta sin gluten referida","Suplementación por deficiencias referida","Seguimiento gastroenterológico referido","Tratamiento de complicaciones referido")
 "crohn","ulcerative_colitis" -> listOf("Aminosalicilato referido","Corticoide referido","Inmunomodulador referido","Terapia biológica referida")
 "hepatitis" -> listOf("Antiviral referido","Seguimiento hepatológico sin antiviral activo","Tratamiento de soporte referido","Otro tratamiento específico según etiología")
 "cirrhosis" -> listOf("Diurético referido","Betabloqueador portal referido","Lactulosa/tratamiento de encefalopatía referido","Tratamiento combinado de complicaciones referido")
 "epilepsy" -> listOf("Antiepiléptico en monoterapia referido","Combinación de antiepilépticos referida","Ajuste por neurología referido","Tratamiento quirúrgico/especializado referido")
 "migraine" -> listOf("Analgésico/antiinflamatorio referido","Triptán/u otro tratamiento agudo referido","Tratamiento preventivo referido","Medidas no farmacológicas referidas")
 "parkinson" -> listOf("Levodopa/combinación dopaminérgica referida","Agonista dopaminérgico referido","Otro tratamiento neurológico referido","Rehabilitación/estimulación especializada referida")
 "alzheimer" -> listOf("Inhibidor de colinesterasa referido","Memantina referida","Manejo conductual/sintomático referido","Seguimiento geriátrico/neurológico referido")
 "neuropathy" -> listOf("Gabapentinoide referido","Antidepresivo neuromodulador referido","Control de enfermedad causal referido","Rehabilitación/tratamiento especializado referido")
 "cerebral_palsy" -> listOf("Rehabilitación/fisioterapia referida","Antiespástico referido","Anticonvulsivante referido cuando aplica","Manejo multidisciplinario referido")
 "oral_cancer","breast_cancer","prostate_cancer","lung_cancer","colorectal_cancer","leukemia","lymphoma","myeloma" -> listOf("Cirugía oncológica referida","Quimioterapia referida","Radioterapia referida","Terapia dirigida/inmunoterapia/hormonal referida")
 "hiv","aids" -> listOf("Terapia antirretroviral combinada referida","Esquema de tableta única referido","Cambio/ajuste de esquema por infectología referido","Otro esquema antirretroviral referido")
 "primary_immune" -> listOf("Inmunoglobulina de reemplazo referida","Profilaxis antimicrobiana referida","Inmunomodulación referida","Trasplante/terapia especializada referida")
 "transplant_immune" -> listOf("Tacrolimus/esquema inmunosupresor referido","Ciclosporina/esquema inmunosupresor referido","Micofenolato/combinación referida","Corticoide + inmunosupresores referidos")
 "steroid_immune" -> listOf("Corticoide sistémico referido","Reducción gradual indicada/referida","Profilaxis asociada referida","Tratamiento de enfermedad de base referido")
 "biologic_immune" -> listOf("Anti-TNF referido","Antiinterleucina referido","Otro biológico referido","Biológico + inmunomodulador referido")
 "chemo_immune" -> listOf("Quimioterapia activa referida","Profilaxis antimicrobiana referida","Factor estimulante de colonias referido","Seguimiento hematológico/oncológico referido")
 "other_immune" -> listOf("Inmunoglobulina referida","Inmunosupresor referido","Profilaxis antimicrobiana referida","Tratamiento especializado referido")
 "down" -> listOf("Seguimiento multidisciplinario sin fármaco específico","Tratamiento tiroideo referido cuando aplica","Tratamiento cardiológico referido cuando aplica","Apoyos de desarrollo/rehabilitación referidos")
 "cystic_fibrosis" -> listOf("Terapia de limpieza de vía aérea referida","Enzimas pancreáticas/nutrición referidas","Antibiótico inhalado/sistémico referido","Modulador CFTR referido cuando aplica")
 "turner" -> listOf("Hormona de crecimiento antecedente/referida","Estrógeno/progestágeno de reemplazo referido","Tratamiento tiroideo referido cuando aplica","Seguimiento cardiometabólico referido")
 "pku" -> listOf("Dieta restringida en fenilalanina referida","Fórmula médica/aminoácidos referidos","Sapropterina referida cuando aplica","Tratamiento metabólico especializado referido")
 "cah" -> listOf("Hidrocortisona/glucocorticoide referido","Mineralocorticoide referido","Ajuste de dosis por estrés indicado por endocrinología","Seguimiento endocrinológico especializado referido")
 "gaucher" -> listOf("Terapia de reemplazo enzimático referida","Terapia de reducción de sustrato referida","Tratamiento de soporte referido","Seguimiento especializado sin tratamiento activo")
 "genetic_other" -> listOf("Tratamiento farmacológico específico referido","Tratamiento no farmacológico/rehabilitación referido","Terapia especializada/genética referida","Seguimiento sin tratamiento activo referido")
 else -> when(d.protocol){
  "cardio","hypertension" -> listOf("Antihipertensivo referido","Antiagregante/anticoagulante referido","Hipolipemiante referido","Tratamiento cardiovascular combinado referido")
  "respiratory" -> listOf("Broncodilatador inhalado referido","Corticoide inhalado referido","Combinación de inhaladores referida","Oxígeno/rehabilitación referidos")
  "thyroid" -> listOf("Levotiroxina referida","Antitiroideo referido","Tratamiento definitivo antecedente","Seguimiento endocrinológico referido")
  "immune" -> listOf("Inmunomodulador referido","Corticoide referido","Inmunosupresor referido","Biológico/especializado referido")
  "bone" -> listOf("Antirresortivo referido","Calcio/vitamina D referidos","Analgésico/rehabilitación referidos","Tratamiento óseo especializado referido")
  "hematologic" -> listOf("Suplemento/medicamento hematológico referido","Factor/hemoderivado referido","Anticoagulante/antiagregante referido","Seguimiento hematológico referido")
  "renal" -> listOf("Tratamiento nefroprotector referido","Tratamiento metabólico renal referido","Diálisis/trasplante según corresponda","Seguimiento nefrológico referido")
  "liver","gi" -> listOf("Antisecretor/digestivo referido","Antiviral referido cuando aplica","Tratamiento antiinflamatorio/inmunológico referido","Seguimiento especializado referido")
  "neuro" -> listOf("Medicamento neurológico referido","Combinación farmacológica referida","Rehabilitación referida","Tratamiento especializado referido")
  "oncology" -> listOf("Cirugía oncológica referida","Quimioterapia referida","Radioterapia referida","Terapia dirigida/inmunoterapia referida")
  "genetic" -> listOf("Tratamiento específico referido","Rehabilitación/soporte referido","Terapia especializada referida","Seguimiento sin tratamiento activo")
  else -> listOf("Tratamiento farmacológico referido","Tratamiento no farmacológico referido","Tratamiento combinado referido","Otro tratamiento médico referido")
 }
}

@Composable private fun DiseaseEditor37(d:Disease37,initial:DiseaseAnswer,onSave:(DiseaseAnswer)->Unit,onProtocol:()->Unit,onBack:()->Unit){
 var onset by remember(d.id){mutableStateOf(initial.onset)}
 var treatment by remember(d.id){mutableStateOf(initial.treatment)}
 var status by remember(d.id){mutableStateOf(initial.currentStatus)}
 var complications by remember(d.id){mutableStateOf(initial.complications)}
 var customDisease by rememberRecordState("history.path."+d.id+".customName","")
 var treatmentMode by remember(d.id){
  mutableStateOf(
   when{
    initial.treatment.isBlank() -> ""
    initial.treatment.contains("No toma",true) -> "No toma tratamiento"
    initial.treatment.contains("Suspend",true) -> "Suspendió tratamiento"
    initial.treatment.contains("No sabe",true) -> "No sabe / no recuerda"
    else -> "Sí, sigue tratamiento médico"
   }
  )
 }
 val dates=listOf("Diagnóstico este año","1–2 años","3–5 años","6–10 años","Más de 10 años","Desde la infancia","No recuerda")
 val states=listOf(
  "Controlado según seguimiento médico referido",
  "Descontrolado referido",
  "En tratamiento, control no conocido",
  "Sin tratamiento actualmente",
  "Suspendió tratamiento",
  "En estudio / diagnóstico reciente",
  "Resuelto / antecedente, cuando aplique",
  "Inestable / descompensación grave",
  "No sabe"
 )
 val complicationOptions=listOf("Ninguna referida","Sí, en seguimiento","Sí, antecedente resuelto","No sabe / no recuerda")
 val treatmentModes=listOf("Sí, sigue tratamiento médico","No toma tratamiento","Suspendió tratamiento","No sabe / no recuerda")
 val schemes=presetTreatments37(d)

 Column(verticalArrangement=Arrangement.spacedBy(12.dp)){
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){
   Button(onClick=onBack){Text("← Enfermedades")}
   Text("Formulario compacto",style=MaterialTheme.typography.bodySmall,fontWeight=FontWeight.SemiBold)
  }
  Text(if(d.id=="genetic_other" && customDisease.isNotBlank()) customDisease else d.name,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black)

  if(d.id=="genetic_other"){
   OutlinedTextField(
    value=customDisease,
    onValueChange={customDisease=it},
    label={Text("¿Cuál enfermedad genética / hereditaria?")},
    modifier=Modifier.fillMaxWidth(),
    singleLine=true
   )
  }

  Text("1 · ¿Desde cuándo?",fontWeight=FontWeight.Bold)
  ChipChoices(dates.map{x->x to (onset==x)},{i->onset=dates[i]},columns=3)

  Text("2 · Estado actual / control",fontWeight=FontWeight.Bold)
  ChipChoices(states.map{x->x to (status==x)},{i->
   status=states[i]
   if(status.startsWith("Resuelto")){
    treatmentMode="No toma tratamiento"
    treatment="Sin tratamiento activo / antecedente resuelto"
   }
  },columns=3)

  if(status.isNotBlank() && !status.startsWith("Resuelto")){
   Text("3 · Tratamiento médico",fontWeight=FontWeight.Bold)
   if(d.protocol=="genetic" && status.startsWith("Controlado"))Text("La enfermedad genética/hereditaria está registrada como controlada. Indica si sigue tratamiento y, si corresponde, selecciona uno de los 4 esquemas referidos.",style=MaterialTheme.typography.bodySmall)
   ChipChoices(treatmentModes.map{x->x to (treatmentMode==x)},{i->
    treatmentMode=treatmentModes[i]
    treatment=when(treatmentMode){
     "No toma tratamiento" -> "No toma tratamiento actualmente"
     "Suspendió tratamiento" -> "Suspendió tratamiento referido"
     "No sabe / no recuerda" -> "No sabe / no recuerda tratamiento"
     else -> ""
    }
   },columns=2)

   if(treatmentMode=="Sí, sigue tratamiento médico"){
    Text("Tratamiento referido · 4 opciones",fontWeight=FontWeight.SemiBold)
    ChipChoices(schemes.map{x->x to (treatment==x)},{i->treatment=schemes[i]},columns=2)
    Text("Registra lo que el paciente ya utiliza o refiere. No constituye prescripción ni incluye dosis.",style=MaterialTheme.typography.bodySmall)
   }
  }

  Text("4 · Complicaciones referidas",fontWeight=FontWeight.Bold)
  ChipChoices(complicationOptions.map{x->x to (complications==x)},{i->complications=complicationOptions[i]},columns=2)

  val projected=DiseaseAnswer(
   true,
   onset,
   if(treatment.isNotBlank())treatment else treatmentMode,
   status,
   complications
  )
  val localAsa=asa37(mapOf(d.id to projected))
  Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
   Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
    Text("Orientación ASA de este antecedente: ASA $localAsa",fontWeight=FontWeight.Bold)
    if(localAsa==2)Text("Enfermedad sistémica referida sin datos actuales de mal control, inestabilidad o complicación activa importante.",style=MaterialTheme.typography.bodySmall)
    if(localAsa==3)Text("Se seleccionó descontrol, limitación importante o complicación activa en seguimiento; integrar el resto de antecedentes y la valoración clínica.",style=MaterialTheme.typography.bodySmall)
    if(localAsa==4)Text("Se seleccionó inestabilidad/descompensación grave. Esta orientación requiere valoración clínica y médica inmediata según el contexto.",style=MaterialTheme.typography.bodySmall)
   }
  }

  if(d.id=="metabolic")NoticeCard("Síndrome metabólico controlado, sin complicación activa importante ni inestabilidad, no debe subir automáticamente a ASA III. La clasificación final depende del conjunto clínico.")
  if(d.protocol=="diabetes")NoticeCard("En diabetes el tratamiento se registra según lo que el paciente refiere. El control actual, complicaciones y comorbilidades son los elementos que modifican la orientación ASA; el diagnóstico aislado no basta.")
  if(d.protocol=="genetic")NoticeCard("Las condiciones genéticas/hereditarias tienen manifestaciones muy variables. El diagnóstico por sí solo no define ASA: importa la repercusión sistémica, el control, las comorbilidades y el estado funcional.")
  if(d.protocol=="exanthem")NoticeCard("Registrar edad al padecerla, tratamiento recibido y complicaciones; una imagen aislada no confirma el diagnóstico.")

  val customOk=d.id!="genetic_other" || customDisease.isNotBlank()
  val canSave=customOk && onset.isNotBlank() && status.isNotBlank() && treatment.isNotBlank() && complications.isNotBlank()
  Button(enabled=canSave,onClick={
   onSave(DiseaseAnswer(true,onset,treatment,status,complications,true))
  },modifier=Modifier.fillMaxWidth()){Text("💾 Guardar antecedente positivo")}
  OutlinedButton(onClick={
   onSave(DiseaseAnswer(false,recorded=true))
  },modifier=Modifier.fillMaxWidth()){
   Text("✓ Registrar explícitamente que NO presenta esta condición")
  }
  Text("No registrar una opción no significa que el paciente la niegue; queda como no preguntada/no registrada.",style=MaterialTheme.typography.bodySmall)
  Button(onClick=onProtocol,modifier=Modifier.fillMaxWidth()){Text("📚 Consultar protocolo odontológico")}
 }
}
@Composable fun SystemicProtocols37Screen(lang:String,onBack:()->Unit){
 var selected by remember{mutableStateOf<Pair<String,String>?>(null)}
 var carePlan by remember{mutableStateOf<String?>(null)}
 val p=listOf(
  "Diabetes mellitus" to "Confirmar tipo, tratamiento, control referido, alimentación y antecedentes de hipoglucemia.",
  "Hipertensión / cardiopatía" to "Confirmar diagnóstico, tratamiento, control, signos vitales, capacidad funcional y anticoagulación/antiagregación cuando corresponda.",
  "Hipotensión arterial" to "Confirmar cifras habituales, síntomas como mareo o síncope, hidratación, medicamentos, causas conocidas y respuesta a cambios posturales. Considerar posición del sillón, levantamiento gradual y vigilancia de signos vitales.",
  "Asma / EPOC" to "Identificar control, desencadenantes, inhaladores, exacerbaciones recientes y capacidad respiratoria.",
  "Enfermedad renal / diálisis" to "Precisar función renal, diálisis o trasplante, medicamentos, sangrado y coordinación médica cuando corresponda.",
  "Enfermedad hepática" to "Precisar diagnóstico, función hepática, medicamentos y antecedentes de sangrado o descompensación.",
  "Trastornos hematológicos" to "Precisar diagnóstico, gravedad, tratamiento, antecedentes de sangrado/trombosis y estudios indicados para el procedimiento.",
  "VIH / SIDA" to "Registrar control médico, tratamiento antirretroviral referido, antecedentes de infecciones oportunistas, lesiones orales, estado inmunológico cuando sea clínicamente pertinente e interacciones; mantener confidencialidad y evitar estigma.",
  "Paciente fumador / nicotina" to "Registrar producto utilizado, frecuencia, tiempo de consumo, último consumo, intentos de suspensión, síntomas respiratorios y repercusión periodontal/oral. Integrar prevención, cicatrización y riesgo de complicaciones al plan odontológico.",
  "Consumo problemático de alcohol" to "Registrar frecuencia y cantidad referidas, último consumo, antecedentes de abstinencia o dependencia, función hepática cuando sea pertinente, medicamentos e interacciones. Evitar atención electiva si hay intoxicación aguda y valorar interconsulta cuando exista riesgo médico.",
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
  item{ScreenHeader("Protocolos para pacientes sistémicos",onBack,"Selecciona una condición y revisa el protocolo por etapas. Sólo se muestra el contenido de la condición elegida para reducir desplazamiento y evitar mezclar recomendaciones.")}
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
   item{Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){Text(x.first,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Black);Text("Ruta: control → estudios → atención → anestesia → medicamentos → procedimientos y urgencias",style=MaterialTheme.typography.bodySmall)}}}
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
     "Hipotensión arterial" -> "No existe un panel universal. Revisar presión arterial seriada cuando sea pertinente, antecedentes de síncope, medicamentos y estudios de la causa subyacente sólo si modifican el manejo odontológico."
     "VIH / SIDA" -> "Los estudios se revisan según contexto clínico y procedimiento: tratamiento antirretroviral, antecedentes de infecciones, y cuando sea pertinente recuento de CD4, carga viral o biometría hemática. No solicitar estudios de rutina sólo por el diagnóstico."
     "Paciente fumador / nicotina" -> "No existe un estudio de laboratorio odontológico rutinario específico por tabaquismo. Valorar exposición, síntomas respiratorios, estado periodontal, cicatrización y comorbilidades; solicitar estudios sólo si cambian la seguridad del procedimiento."
     "Consumo problemático de alcohol" -> "No existe un panel odontológico universal. Según historia y procedimiento pueden ser pertinentes función hepática, biometría hemática o coagulación, especialmente si hay enfermedad hepática, sangrado o consumo crónico importante."
     else -> "Revisar estudios recientes pertinentes al diagnóstico y al procedimiento. No existe un panel universal para todos los pacientes sistémicos; usar protocolo institucional e interconsulta cuando esté indicada."
    };Text(specific)
   }}
   item{ResponsiveSectionV17("3 · Atención odontológica","Decidir tratar, modificar, posponer o interconsultar"){val careOptions=listOf("Atención habitual si está estable","Modificar plan / cita","Posponer atención electiva","Interconsulta médica"); ChipChoices(careOptions.map{it to (carePlan==it)},{carePlan=careOptions[it]},2); carePlan?.let{Text("Plan seleccionado: $it",fontWeight=FontWeight.Bold);Text("Esta selección es educativa y debe sustentarse en el estado clínico, procedimiento previsto y valoración completa.",style=MaterialTheme.typography.bodySmall)}}}
   item{ResponsiveSectionV17("4 · Anestesia","La elección depende de enfermedad, control, medicamentos y procedimiento"){
    val anesthesiaText=when(x.first){
     "Hipotensión arterial" -> "Registrar presión y síntomas antes del procedimiento. Evitar cambios posturales bruscos, levantar el sillón gradualmente y revisar medicamentos que puedan favorecer hipotensión. La selección de anestésico/vasoconstrictor depende del cuadro completo."
     "Paciente fumador / nicotina" -> "Comprobar anestésico, vasoconstrictor, signos vitales y comorbilidades. Registrar consumo reciente de nicotina y síntomas cardiovasculares o respiratorios; no aplicar una regla universal sólo por fumar."
     "Consumo problemático de alcohol" -> "Revisar intoxicación o abstinencia, función hepática cuando sea pertinente, medicamentos e interacciones. No realizar tratamiento electivo con intoxicación aguda; adaptar anestesia al estado clínico."
     else -> "Comprobar anestésico, vasoconstrictor, dosis máxima aplicable, interacciones y contraindicaciones antes de administrar. Evitar reglas universales por diagnóstico."
    }
    Text(anesthesiaText)
   }}
   item{ResponsiveSectionV17("5 · Analgesia y antiinflamatorios","Seleccionar según antecedentes y tratamiento actual"){Text("Revisar riesgo renal, hepático, gastrointestinal, cardiovascular, hemorrágico e interacciones. No indicar AINE automáticamente.")}}
   item{ResponsiveSectionV17("6 · Antibióticos","No se indican por el solo hecho de tener una enfermedad sistémica"){Text("Usar antibiótico sólo cuando exista una indicación independiente o profilaxis específicamente indicada por una guía vigente. Verificar alergias, función renal/hepática e interacciones.")}}
   item{ResponsiveSectionV17("7 · Procedimientos y urgencias","Plan de seguridad"){Text("Definir qué procedimientos son apropiados según estabilidad, invasividad y riesgo; reconocer signos de descompensación y contar con plan de urgencias e interconsulta.")}}
   item{NoticeCard("Antes de usar este protocolo en un paciente real, confirmar diagnóstico, control, medicamentos y recomendaciones institucionales vigentes.")}
  }
 }
}
