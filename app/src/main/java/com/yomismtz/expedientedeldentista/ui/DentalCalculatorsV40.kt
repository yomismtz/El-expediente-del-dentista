package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class LocalAnesthetic(val name:String,val concentration:String,val notes:String)
private val dentalAnestheticsV40=listOf(
 LocalAnesthetic("Lidocaína 2%","20 mg/mL","Disponible sola o asociada a vasoconstrictor según presentación."),
 LocalAnesthetic("Lidocaína 2% + epinefrina","20 mg/mL","Captura la dosis máxima permitida por tu protocolo y revisa además el límite del vasoconstrictor."),
 LocalAnesthetic("Mepivacaína 3%","30 mg/mL","Frecuente en presentaciones sin vasoconstrictor."),
 LocalAnesthetic("Mepivacaína 2% + vasoconstrictor","20 mg/mL","La composición exacta depende de la presentación comercial."),
 LocalAnesthetic("Articaína 4% + epinefrina","40 mg/mL","Usa únicamente los límites de edad y dosis establecidos por ficha técnica/protocolo local."),
 LocalAnesthetic("Prilocaína 4%","40 mg/mL","Revisar contraindicaciones y ficha técnica antes de calcular."),
 LocalAnesthetic("Bupivacaína 0.5% + epinefrina","5 mg/mL","Anestésico de acción prolongada; requiere selección clínica específica.")
)

private data class DrugOptionV40(val name:String,val presentation:String,val note:String)
private data class DrugGroupV40(val title:String,val drugs:List<DrugOptionV40>)
private val drugGroupsV40=listOf(
 DrugGroupV40("Antibióticos",listOf(
  DrugOptionV40("Amoxicilina","Suspensión oral 500 mg/5 mL; cápsula 500 mg","Presentaciones documentadas en el Listado Institucional IMSS. La pauta depende de la infección y del protocolo."),
  DrugOptionV40("Amoxicilina / ácido clavulánico","Suspensión 125 mg/31.25 mg por 5 mL; tableta 500 mg/125 mg","Seleccionar según indicación, edad/peso, alergias y función renal."),
  DrugOptionV40("Azitromicina","Tableta o suspensión según presentación disponible","Verificar presentación y pauta vigente antes de calcular."),
  DrugOptionV40("Clindamicina","Cápsula o solución según presentación disponible","No usar como sustitución automática por alergia; confirmar indicación y guía vigente.")
 )),
 DrugGroupV40("Antiinflamatorios / analgésicos",listOf(
  DrugOptionV40("Paracetamol / acetaminofén","Tableta y solución/suspensión oral según presentación","Comprobar dosis indicada, máximo diario y función hepática."),
  DrugOptionV40("Ibuprofeno","Tableta y suspensión oral según presentación","Revisar edad, función renal, riesgo gastrointestinal/cardiovascular e interacciones."),
  DrugOptionV40("Naproxeno","Tableta o suspensión según presentación","Usar sólo cuando esté indicado y comprobar contraindicaciones de AINE.")
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
 var weight by remember{mutableStateOf("")}
 var mgKg by remember{mutableStateOf("")}
 var maxAbsolute by remember{mutableStateOf("")}
 var cartridgeMl by remember{mutableStateOf("1.8")}
 var selected by remember{mutableStateOf(0)}
 var concentration by remember{mutableStateOf("")}
 var doseMgKg by remember{mutableStateOf("")}
 var medGroup by remember{mutableStateOf<Int?>(null)}
 var medDrug by remember{mutableStateOf<Int?>(null)}
 var medChecks by remember{mutableStateOf(setOf<Int>())}
 var topicalAge by remember{mutableStateOf(0)}
 var topicalProduct by remember{mutableStateOf(0)}
 var bmiWeight by remember{mutableStateOf("")}
 var bmiHeightCm by remember{mutableStateOf("")}
 val w=weight.toDoubleOrNull()
 val limitPerKg=mgKg.toDoubleOrNull()
 val absolute=maxAbsolute.toDoubleOrNull()
 val ml=cartridgeMl.toDoubleOrNull()
 val mgMl=dentalAnestheticsV40[selected].concentration.substringBefore(" ").toDoubleOrNull()
 val mgLimit=if(w!=null&&limitPerKg!=null) minOf(w*limitPerKg,absolute?:Double.MAX_VALUE) else null
 val cartridges=if(mgLimit!=null&&mgMl!=null&&ml!=null&&mgMl*ml>0) mgLimit/(mgMl*ml) else null
 val medConc=concentration.toDoubleOrNull()
 val medDose=doseMgKg.toDoubleOrNull()
 val doseMg=if(w!=null&&medDose!=null)w*medDose else null
 val doseMl=if(doseMg!=null&&medConc!=null&&medConc>0)doseMg/medConc else null
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
    dentalAnestheticsV40.forEachIndexed{i,a->FilterChip(selected==i,{selected=i},{Text(a.name)},modifier=Modifier.fillMaxWidth())}
    Text(dentalAnestheticsV40[selected].notes)
   }}
   item{SectionCard("3 · Límite del protocolo"){
    Text("La app no impone una dosis máxima universal: captura el valor mg/kg y el máximo absoluto de la ficha técnica o protocolo que estés utilizando.")
    OutlinedTextField(mgKg,{mgKg=it.filter{x->x.isDigit()||x=='.'}.take(6)},label={Text("Máximo indicado por protocolo (mg/kg)")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(maxAbsolute,{maxAbsolute=it.filter{x->x.isDigit()||x=='.'}.take(7)},label={Text("Máximo absoluto (mg), si aplica")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(cartridgeMl,{cartridgeMl=it.filter{x->x.isDigit()||x=='.'}.take(4)},label={Text("Volumen del cartucho (mL)")},modifier=Modifier.fillMaxWidth())
    Text(if(mgLimit==null)"Completa peso y límite mg/kg." else "Límite calculado: %.1f mg".format(mgLimit),fontWeight=FontWeight.Bold)
    Text(if(cartridges==null)"Completa los datos para estimar cartuchos." else "Equivalencia matemática: %.2f cartuchos".format(cartridges),fontWeight=FontWeight.Bold)
   }}
  }else if(tab==1){
   item{SectionCard("1 · Selecciona grupo farmacológico"){
    ChipChoices(drugGroupsV40.mapIndexed{i,g->g.title to (medGroup==i)},{i->medGroup=i;medDrug=null;medChecks=emptySet()},2)
   }}
   if(medGroup!=null){
    item{SectionCard("2 · Selecciona medicamento"){
     val g=drugGroupsV40[medGroup!!]
     ChipChoices(g.drugs.mapIndexed{i,d->d.name to (medDrug==i)},{i->medDrug=i;medChecks=emptySet()},2)
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
    item{SectionCard("4 · Antes de calcular una pauta"){
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
   item{SectionCard("Calculadora de índice de masa corporal (IMC)"){
    Text("Para adultos de 20 años o más. Fórmula: peso (kg) ÷ estatura² (m).")
    OutlinedTextField(bmiWeight,{bmiWeight=it.filter{x->x.isDigit()||x=='.'}.take(6)},label={Text("Peso (kg)")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(bmiHeightCm,{bmiHeightCm=it.filter{x->x.isDigit()||x=='.'}.take(6)},label={Text("Estatura (cm)")},modifier=Modifier.fillMaxWidth())
    Text(if(bmi==null)"Ingresa peso y estatura." else "IMC: %.1f kg/m² · %s".format(bmi,bmiCategory),fontWeight=FontWeight.Bold)
   }}
   item{SectionCard("Interpretación en adultos"){
    Text("Bajo peso: <18.5\nPeso saludable: 18.5–24.9\nSobrepeso: 25.0–29.9\nObesidad clase 1: 30.0–34.9\nObesidad clase 2: 35.0–39.9\nObesidad clase 3 (severa): ≥40.0")
   }}
   item{NoticeCard("Fuente clínica: CDC. El IMC es una medida de detección, no un diagnóstico. Para pacientes de 2 a 19 años debe interpretarse mediante IMC por edad y sexo/percentiles; esta calculadora no aplica esas categorías de adulto.")}
  }else{
   item{SectionCard("Actividades de cálculo y conversión"){
    Text("Practica conversiones sin que la app prescriba tratamientos.",fontWeight=FontWeight.Bold)
    Text("• mg ↔ mL a partir de una concentración conocida.\n• mg/kg × peso = mg por dosis cuando la dosis ya fue indicada.\n• mg por cartucho = concentración (mg/mL) × volumen (mL).\n• Número teórico de cartuchos = límite total (mg) ÷ mg por cartucho.\n• Revisión de unidades antes de aceptar el resultado.")
   }}
   item{SectionCard("Protocolo de comprobación"){
    listOf("1. Confirmar identidad, edad y peso del paciente.","2. Confirmar medicamento/anestésico y concentración exacta de la presentación.","3. Consultar ficha técnica o protocolo docente vigente.","4. Revisar alergias, embarazo cuando aplique, función renal/hepática, enfermedades e interacciones.","5. Realizar la conversión matemática.","6. Comprobar máximo por dosis y máximo diario cuando correspondan.","7. Hacer una segunda verificación antes de administrar o registrar.").forEach{Text(it)}
   }}
   item{NoticeCard("Actividad educativa: la calculadora verifica operaciones matemáticas; no selecciona fármaco, indicación, dosis, intervalo ni duración.")}
  }
 }
}
