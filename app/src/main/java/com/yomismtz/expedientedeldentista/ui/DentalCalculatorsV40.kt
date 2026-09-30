package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class LocalAnesthetic(val name:String,val mgPerMl:Double,val cartridgeMl:Double,val maxMgKg:Double?,val maxAbsoluteMg:Double?,val notes:String)
private val dentalAnestheticsV40=listOf(
 LocalAnesthetic("Lidocaína 2% + epinefrina 1:100,000",20.0,1.8,7.0,500.0,"36 mg de lidocaína + 0.018 mg de epinefrina por cartucho de 1.8 mL. Referencia sistémica: 7 mg/kg o 500 mg; verificar además el límite individual de epinefrina."),
 LocalAnesthetic("Articaína 4% + epinefrina",40.0,1.8,7.0,500.0,"72 mg por cartucho de 1.8 mL. Referencia educativa: 7 mg/kg, máximo 500 mg; confirmar ficha técnica del producto concreto."),
 LocalAnesthetic("Prilocaína 3% + felipresina",30.0,1.8,null,null,"54 mg por cartucho de 1.8 mL. El cuadro nacional documenta 1–2 cartuchos en adultos y 1/2–1 en niños; no se extrapola un máximo mg/kg universal."),
 LocalAnesthetic("Mepivacaína 3%",30.0,1.8,null,null,"54 mg por cartucho de 1.8 mL. El máximo debe verificarse en la ficha técnica de la presentación usada."),
 LocalAnesthetic("Mepivacaína 2% + vasoconstrictor",20.0,1.8,null,null,"36 mg por cartucho de 1.8 mL. Verificar vasoconstrictor y límites en la ficha técnica del producto."),
 LocalAnesthetic("Bupivacaína 0.5%",5.0,1.8,null,175.0,"9 mg por 1.8 mL. El cuadro nacional documenta máximo de dosis única de 175 mg para infiltración regional; confirmar presentación odontológica concreta.")
)

private data class DrugOptionV40(val name:String,val presentation:String,val note:String)
private data class WeightDoseV40(val drug:String,val presentation:String,val mgPerMl:Double,val mgKgDay:List<Double>,val intervals:List<Int>,val maxMgDay:Double?,val source:String)
private val verifiedWeightDosesV40=listOf(
 WeightDoseV40("Amoxicilina","Suspensión oral 500 mg/5 mL",100.0,listOf(90.0,100.0),listOf(8),4500.0,"IMSS GPC 120GER · indicación documentada: neumonía adquirida en la comunidad pediátrica"),
 WeightDoseV40("Amoxicilina / ácido clavulánico","Suspensión 125 mg/31.25 mg por 5 mL",25.0,listOf(20.0,40.0),listOf(8),null,"Listado Institucional IMSS · dosis expresada según componente amoxicilina"),
 WeightDoseV40("Ibuprofeno","Suspensión oral 2 g/100 mL (100 mg/5 mL)",20.0,listOf(20.0,30.0,40.0),listOf(6,8),null,"Listado Institucional IMSS: equivalencia educativa de pauta documentada"),
 WeightDoseV40("Paracetamol / acetaminofén","Solución oral 100 mg/mL",100.0,listOf(40.0,60.0,120.0),listOf(4,6),null,"IMSS GPC: 10–30 mg/kg por dosis cada 4–6 h; seleccionar sólo la pauta que corresponda a la fuente/indicación"),
 WeightDoseV40("Naproxeno","Suspensión oral 125 mg/5 mL",25.0,listOf(15.0),listOf(8),null,"IMSS Grupo 20: máximo pediátrico documentado 15 mg/kg/día; equivalencia educativa del máximo diario"),
 WeightDoseV40("Azitromicina","Suspensión 200 mg/5 mL",40.0,listOf(10.0),listOf(24),500.0,"IMSS GPC 261: 10 mg/kg/día, máximo 500 mg, para sinusitis bacteriana pediátrica; no extrapolar automáticamente a odontología"),
 WeightDoseV40("Clindamicina","Suspensión 75 mg/5 mL",15.0,listOf(30.0),listOf(8),1800.0,"IMSS GPC 261: 30 mg/kg/día en 3 dosis, máximo 1.8 g, para sinusitis bacteriana pediátrica; no extrapolar automáticamente a odontología"),
 WeightDoseV40("Claritromicina","Suspensión 125 mg/5 mL",25.0,listOf(15.0),listOf(12),1000.0,"IMSS GPC 261: 15 mg/kg/día en 2 dosis, máximo 1 g, para sinusitis bacteriana pediátrica; no extrapolar automáticamente a odontología"),
 WeightDoseV40("Cefuroxima","Suspensión 250 mg/5 mL",50.0,listOf(30.0),listOf(12),1000.0,"IMSS GPC 261: 30 mg/kg/día en 2 dosis, máximo 1 g, para sinusitis bacteriana pediátrica; ejercicio de la pauta documentada")
)
private data class DrugGroupV40(val title:String,val drugs:List<DrugOptionV40>)
private val drugGroupsV40=listOf(
 DrugGroupV40("Antibióticos",listOf(
  DrugOptionV40("Amoxicilina","Suspensión oral 500 mg/5 mL; cápsula 500 mg","Presentaciones documentadas en el Listado Institucional IMSS. La pauta depende de la infección y del protocolo."),
  DrugOptionV40("Amoxicilina / ácido clavulánico","Suspensión 125 mg/31.25 mg por 5 mL; tableta 500 mg/125 mg","Seleccionar según indicación, edad/peso, alergias y función renal."),
  DrugOptionV40("Azitromicina","Suspensión 200 mg/5 mL; tabletas","Presentación documentada en IMSS; la pauta depende de la indicación."),
  DrugOptionV40("Clindamicina","Suspensión 75 mg/5 mL; cápsulas","Confirmar indicación y guía vigente."),
  DrugOptionV40("Claritromicina","Suspensión 125 mg/5 mL; tabletas","Pauta disponible para ejercicios documentados."),
  DrugOptionV40("Cefuroxima","Suspensión 250 mg/5 mL; tabletas","Pauta disponible para ejercicios documentados.")
 )),
 DrugGroupV40("Antiinflamatorios / analgésicos",listOf(
  DrugOptionV40("Paracetamol / acetaminofén","Solución oral 100 mg/mL; tabletas","Presentación y pauta pediátrica documentadas por IMSS."),
  DrugOptionV40("Ibuprofeno","Tableta y suspensión oral según presentación","Revisar edad, función renal, riesgo gastrointestinal/cardiovascular e interacciones."),
  DrugOptionV40("Naproxeno","Suspensión oral 125 mg/5 mL; tableta 250 mg","Presentación documentada por IMSS; comprobar contraindicaciones de AINE.")
 )),
 DrugGroupV40("Antivirales",listOf(
  DrugOptionV40("Aciclovir","Tableta y suspensión oral según presentación","La pauta cambia por diagnóstico, edad y función renal."),
  DrugOptionV40("Valaciclovir","Tableta según presentación","Confirmar indicación y ajuste renal.")
 )),
 DrugGroupV40("Nitroimidazoles",listOf(
  DrugOptionV40("Metronidazol","Tableta 500 mg; suspensión oral 250 mg/5 mL","Presentaciones documentadas por IMSS. No es antibiótico automático para todo cuadro odontógeno; confirmar indicación.")
 )),
 DrugGroupV40("Antimicóticos",listOf(
  DrugOptionV40("Nistatina","Suspensión oral; presentación IMSS con 2,400,000 UI para 24 mL","Indicada en fuentes IMSS para candidiasis bucofaríngea; verificar pauta y producto."),
  DrugOptionV40("Fluconazol","Cápsula/tableta o suspensión según presentación","Revisar interacciones, función hepática/renal e indicación.")
 )),
 DrugGroupV40("Vitaminas",listOf(
  DrugOptionV40("Ácido fólico","Tableta según presentación","Usar sólo ante indicación clínica; no sustituye el diagnóstico de la causa de anemia/deficiencia."),
  DrugOptionV40("Vitamina B12","Presentación oral o parenteral según producto e indicación","Confirmar deficiencia, causa y esquema médico."),
  DrugOptionV40("Vitamina D","Presentaciones variables","No calcular como tratamiento odontológico rutinario; confirmar indicación y esquema médico.")
 ))
)

@Composable
fun DentalCalculatorsV40Screen(lang:String,onBack:()->Unit){
 var tab by remember{mutableStateOf(0)}
 var weight by rememberRecordState("calculators.anesthetic.weight","")
 var selected by rememberRecordState("calculators.anesthetic.selected",0)
 var medGroup by rememberRecordState<Int?>("calculators.medGroup",null)
 var medDrug by rememberRecordState<Int?>("calculators.medDrug",null)
 var medChecks by rememberRecordState("calculators.medChecks",setOf<Int>())
 var medWeight by rememberRecordState("calculators.medWeight","")
 var medPresentation by rememberRecordState<Int?>("calculators.medPresentation",null)
 var medDoseDay by rememberRecordState<Double?>("calculators.medDoseDay",null)
 var medInterval by rememberRecordState<Int?>("calculators.medInterval",null)
 var topicalAge by rememberRecordState("calculators.topicalAge",0)
 var topicalProduct by rememberRecordState("calculators.topicalProduct",0)
 var bmiWeight by rememberRecordState("calculators.bmiWeight","")
 var bmiHeightCm by rememberRecordState("calculators.bmiHeightCm","")
 val w=weight.toDoubleOrNull()
 val anesthetic=dentalAnestheticsV40[selected.coerceIn(0,dentalAnestheticsV40.lastIndex)]
 val mgLimit=when{
  w!=null&&anesthetic.maxMgKg!=null&&anesthetic.maxAbsoluteMg!=null->minOf(w*anesthetic.maxMgKg,anesthetic.maxAbsoluteMg)
  w!=null&&anesthetic.maxMgKg!=null->w*anesthetic.maxMgKg
  anesthetic.maxAbsoluteMg!=null->anesthetic.maxAbsoluteMg
  else->null
 }
 val mgPerCartridge=anesthetic.mgPerMl*anesthetic.cartridgeMl
 val cartridges=if(mgLimit!=null&&mgPerCartridge>0)mgLimit/mgPerCartridge else null
 val bmiW=bmiWeight.toDoubleOrNull()
 val bmiH=bmiHeightCm.toDoubleOrNull()?.div(100.0)
 val bmi=if(bmiW!=null&&bmiH!=null&&bmiH>0)bmiW/(bmiH*bmiH) else null
 val bmiCategory=when{bmi==null->"";bmi<18.5->"Bajo peso";bmi<25.0->"Peso saludable";bmi<30.0->"Sobrepeso";bmi<35.0->"Obesidad clase 1";bmi<40.0->"Obesidad clase 2";else->"Obesidad clase 3 (severa)"}

 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{ScreenHeader("Calculadoras odontológicas",onBack,"Herramientas educativas separadas de los auxiliares de diagnóstico. No sustituyen prescripción, ficha técnica ni supervisión clínica.")}
  item{
   ChipChoices(
    listOf(
     "Anestésico local" to (tab==0),
     "Medicamentos" to (tab==1),
     "Fluoruros / clorhexidina" to (tab==2),
     "IMC" to (tab==3),
     "Conversión y práctica" to (tab==4)
    ),
    { tab=it },
    columns=3
   )
  }
  if(tab==0){
   item{SectionCard("1 · Paciente"){
    OutlinedTextField(weight,{weight=it.filter{x->x.isDigit()||x=='.'}.take(6)},label={Text("Peso (kg)")},modifier=Modifier.fillMaxWidth())
   }}
   item{SectionCard("2 · Anestésico"){
    ChipChoices(dentalAnestheticsV40.mapIndexed{i,a->a.name to (selected==i)},{selected=it},2)
    Text(anesthetic.notes)
   }}
   item{SectionCard("3 · Datos y cálculo automático"){
    Text("Concentración: "+anesthetic.mgPerMl+" mg/mL · cartucho de "+anesthetic.cartridgeMl+" mL · "+"%.1f".format(mgPerCartridge)+" mg/cartucho.",fontWeight=FontWeight.Bold)
    if(anesthetic.maxMgKg!=null) Text("Referencia seleccionada: "+anesthetic.maxMgKg+" mg/kg"+(anesthetic.maxAbsoluteMg?.let{" · máximo absoluto "+it.toInt()+" mg"}?:""))
    Text(if(mgLimit==null)"Esta presentación no tiene un límite mg/kg precargado verificable; consulta su ficha técnica." else "Límite matemático por anestésico local: %.1f mg".format(mgLimit),fontWeight=FontWeight.Bold)
    Text(if(cartridges==null)"No se calcula número de cartuchos para esta presentación." else "Equivalencia teórica por anestésico local: %.2f cartuchos".format(cartridges),fontWeight=FontWeight.Bold)
    Text("Revisa también vasoconstrictor, edad, comorbilidades, interacciones y ficha técnica. La app no decide cuántos cartuchos administrar.")
   }}
  }else if(tab==1){
   item{SectionCard("1 · Selecciona grupo farmacológico"){
    ChipChoices(drugGroupsV40.mapIndexed{i,g->g.title to (medGroup==i)},{i->medGroup=i;medDrug=null;medChecks=emptySet();medPresentation=null;medDoseDay=null;medInterval=null},2)
   }}
   if(medGroup!=null){
    item{SectionCard("2 · Selecciona medicamento"){
     val g=drugGroupsV40[medGroup!!]
     ChipChoices(g.drugs.mapIndexed{i,d->d.name to (medDrug==i)},{i->medDrug=i;medChecks=emptySet();medPresentation=null;medDoseDay=null;medInterval=null},2)
    }}
   }
   if(medGroup!=null&&medDrug!=null){
    item{SectionCard("3 · Datos de la opción seleccionada"){
     val d=drugGroupsV40[medGroup!!].drugs[medDrug!!]
     Text(d.name,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black)
     Text("Presentación: "+d.presentation,fontWeight=FontWeight.Bold)
     Text(d.note)
     Text("El alumno selecciona opciones; no tiene que escribir dosis ni concentración en este apartado.")
    }}
    val currentDrug=drugGroupsV40[medGroup!!].drugs[medDrug!!]
    val protocols=verifiedWeightDosesV40.filter{it.drug==currentDrug.name}
    item{SectionCard("4 · Peso del paciente"){
     OutlinedTextField(medWeight,{medWeight=it.filter{x->x.isDigit()||x=='.'}.take(6)},label={Text("Peso medido (kg)")},modifier=Modifier.fillMaxWidth())
    }}
    item{SectionCard("5 · Presentación"){
     if(protocols.isEmpty()) Text("Todavía no hay una pauta por peso verificada para este medicamento. No se habilita el cálculo.")
     else ChipChoices(protocols.mapIndexed{i,p->p.presentation to (medPresentation==i)},{i->medPresentation=i;medDoseDay=null;medInterval=null},1)
    }}
    if(medPresentation!=null&&protocols.isNotEmpty()){
     val p=protocols[medPresentation!!.coerceIn(0,protocols.lastIndex)]
     item{SectionCard("6 · Pauta documentada por peso"){
      Text(p.source)
      ChipChoices(p.mgKgDay.map{v->"${v.toInt()} mg/kg/día" to (medDoseDay==v)},{i->medDoseDay=p.mgKgDay[i];medInterval=null},2)
      Text("La pauta sólo debe seleccionarse cuando corresponda a la indicación y población de la fuente.",fontWeight=FontWeight.Bold)
     }}
     if(medDoseDay!=null&&medWeight.toDoubleOrNull()!=null){
      val rawDaily=medWeight.toDouble()*medDoseDay!!
      val daily=if(p.maxMgDay!=null) minOf(rawDaily,p.maxMgDay) else rawDaily
      item{SectionCard("7 · Cálculo por día"){
       Text("Peso × pauta = %.1f mg/día".format(rawDaily),fontWeight=FontWeight.Bold)
       if(p.maxMgDay!=null&&rawDaily>p.maxMgDay) Text("Se aplica el máximo documentado: %.0f mg/día".format(p.maxMgDay),fontWeight=FontWeight.Bold)
      }}
      item{SectionCard("8 · Intervalo"){
       ChipChoices(p.intervals.map{h->"Cada $h horas" to (medInterval==h)},{i->medInterval=p.intervals[i]},2)
      }}
      if(medInterval!=null){
       val doses=24.0/medInterval!!
       val perDose=daily/doses
       val mlDose=perDose/p.mgPerMl
       item{SectionCard("9 · Esquema calculado"){
        Text(currentDrug.name,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black)
        Text("Peso: ${medWeight} kg")
        Text("Pauta seleccionada: ${medDoseDay!!.toInt()} mg/kg/día")
        Text("Total calculado: %.1f mg/día".format(daily))
        Text("Frecuencia: cada ${medInterval} horas (%.0f administraciones/día)".format(doses))
        Text("Presentación: ${p.presentation}")
        Text("Equivalencia matemática: %.1f mg = %.2f mL por administración".format(perDose,mlDose),fontWeight=FontWeight.Bold)
        Text("La duración no se infiere: debe corresponder a la indicación y protocolo seleccionado.")
       }}
      }
     }
    }
    item{SectionCard("10 · Comprobaciones de seguridad"){
     val checks=listOf("Confirmar peso medido","Confirmar indicación","Revisar alergias","Función renal/hepática","Interacciones","Ficha técnica / protocolo"); ChipChoices(checks.mapIndexed{i,x->x to medChecks.contains(i)},{i->medChecks=if(medChecks.contains(i)) medChecks-i else medChecks+i},2); Text("${medChecks.size}/${checks.size} comprobaciones marcadas",fontWeight=FontWeight.Bold)
    }}
   }
   item{NoticeCard("La selección muestra datos educativos y presentaciones de referencia; no prescribe automáticamente. Para antibióticos, antivirales, nitroimidazoles y antimicóticos la indicación, dosis, frecuencia y duración deben corresponder al diagnóstico y a una fuente clínica vigente.")}
  }else if(tab==2){
   item{SectionCard("1 · Edad del paciente"){
    Text("Selecciona el grupo de edad. Para productos tópicos no se aplica una fórmula mg/kg cuando la norma o ficha técnica no los dosifica por peso.")
    listOf("Menor de 3 años","3 a 5 años","6 años o más").forEachIndexed{i,v->FilterChip(topicalAge==i,{topicalAge=i},{Text(v)},modifier=Modifier.fillMaxWidth())}
   }}
   item{SectionCard("2 · Producto odontológico"){
    val products=listOf("Pasta dental fluorurada","Enjuague fluorurado","Gel fluorurado profesional","Barniz fluorurado profesional","Clorhexidina 0.12%")
    products.forEachIndexed{i,v->FilterChip(topicalProduct==i,{topicalProduct=i},{Text(v)},modifier=Modifier.fillMaxWidth())}
   }}
   item{SectionCard("3 · Orientación verificada para México"){
    val guidance=when(topicalProduct){
     0->if(topicalAge<2) "NOM-013-SSA2-2015: en menores de 6 años se orienta pasta con 550 ppm de fluoruro. La cantidad y supervisión deben ajustarse a edad y capacidad para evitar ingestión." else "NOM-013-SSA2-2015: a partir de 6 años se contemplan pastas fluoruradas de 551 a 1500 ppm; la norma también enfatiza evitar la ingestión."
     1->if(topicalAge<2) "NO INDICADO POR EDAD: la NOM-013-SSA2-2015 establece que los enjuagues fluorurados no deben utilizarse en menores de 6 años." else "NOM-013-SSA2-2015: pueden emplearse enjuagues fluorurados desde los 6 años. En programas escolares, la norma contempla fluoruro de sodio al 0.2% semanal o quincenal."
     2->if(topicalAge==0) "NO INDICADO POR EDAD: la NOM-013-SSA2-2015 establece que los geles fluorurados no deben utilizarse en menores de 3 años." else "NOM-013-SSA2-2015: el gel fluorurado puede aplicarse a partir de los 3 años, según riesgo de caries y bajo vigilancia de personal de salud bucal capacitado."
     3->"NOM-013-SSA2-2015: los barnices fluorurados son de aplicación profesional y se indican de acuerdo con el riesgo de caries, diagnóstico y plan de tratamiento. No se calcula su uso por mg/kg en esta herramienta."
     else->"Clorhexidina 0.12%: concentración documentada en fuentes clínicas mexicanas. Su indicación, volumen, frecuencia y duración dependen del producto y del objetivo clínico; la app no extrapola una pauta universal ni la calcula por kg."
    }
    Text(guidance,fontWeight=FontWeight.Bold)
   }}
   item{NoticeCard("Fuentes del contenido: NOM-013-SSA2-2015 (Diario Oficial de la Federación) para fluoruros y documentación clínica mexicana/IMSS para clorhexidina al 0.12%. Verifica además la ficha técnica del producto concreto. Uso educativo; no sustituye valoración ni prescripción profesional.")}
  }else if(tab==3){
   item{SectionCard("1 · Calculadora de índice de masa corporal (IMC)"){
    Text("Para adultos de 20 años o más. Fórmula: peso (kg) ÷ estatura² (m).")
    OutlinedTextField(bmiWeight,{bmiWeight=it.filter{x->x.isDigit()||x=='.'}.take(6)},label={Text("Peso (kg)")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(bmiHeightCm,{bmiHeightCm=it.filter{x->x.isDigit()||x=='.'}.take(6)},label={Text("Estatura (cm)")},modifier=Modifier.fillMaxWidth())
    Text(if(bmi==null)"Ingresa peso y estatura." else "IMC: %.1f kg/m² · %s".format(bmi,bmiCategory),fontWeight=FontWeight.Bold)
   }}
   item{SectionCard("2 · Interpretación en adultos"){
    Text("Bajo peso: <18.5\nPeso saludable: 18.5–24.9\nSobrepeso: 25.0–29.9\nObesidad clase 1: 30.0–34.9\nObesidad clase 2: 35.0–39.9\nObesidad clase 3 (severa): ≥40.0")
   }}
   item{NoticeCard("Fuente clínica: CDC. El IMC es una medida de detección, no un diagnóstico. Para pacientes de 2 a 19 años debe interpretarse mediante IMC por edad y sexo/percentiles; esta calculadora no aplica esas categorías de adulto.")}
  }else{
   item{SectionCard("1 · Actividades de cálculo y conversión"){
    Text("Practica conversiones sin que la app prescriba tratamientos.",fontWeight=FontWeight.Bold)
    Text("• mg ↔ mL a partir de una concentración conocida.\n• mg/kg × peso = mg por dosis cuando la dosis ya fue indicada.\n• mg por cartucho = concentración (mg/mL) × volumen (mL).\n• Número teórico de cartuchos = límite total (mg) ÷ mg por cartucho.\n• Revisión de unidades antes de aceptar el resultado.")
   }}
   item{SectionCard("2 · Protocolo de comprobación"){
    listOf("1. Confirmar identidad, edad y peso del paciente.","2. Confirmar medicamento/anestésico y concentración exacta de la presentación.","3. Consultar ficha técnica o protocolo docente vigente.","4. Revisar alergias, embarazo cuando aplique, función renal/hepática, enfermedades e interacciones.","5. Realizar la conversión matemática.","6. Comprobar máximo por dosis y máximo diario cuando correspondan.","7. Hacer una segunda verificación antes de administrar o registrar.").forEach{Text(it)}
   }}
   item{NoticeCard("Actividad educativa: la calculadora verifica operaciones matemáticas; no selecciona fármaco, indicación, dosis, intervalo ni duración.")}
  }
 }
}
