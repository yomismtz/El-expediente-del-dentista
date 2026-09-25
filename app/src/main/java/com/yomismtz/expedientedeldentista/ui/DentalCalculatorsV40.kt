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

private data class DrugGroup(val title:String,val examples:String)
private val pediatricDrugGroupsV40=listOf(
 DrugGroup("Antibióticos","Amoxicilina; amoxicilina/ácido clavulánico; azitromicina; clindamicina cuando esté indicada según protocolo."),
 DrugGroup("Antiinflamatorios / analgésicos","Ibuprofeno; paracetamol/acetaminofén. La selección depende de edad, antecedentes, indicación y contraindicaciones."),
 DrugGroup("Antimicóticos","Nistatina; fluconazol cuando exista indicación y prescripción profesional."),
 DrugGroup("Antivirales","Aciclovir; valaciclovir cuando exista una indicación clínica compatible y prescripción profesional.")
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
  item{Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
   FilterChip(tab==0,{tab=0},{Text("Anestésico local")},Modifier.weight(1f))
   FilterChip(tab==1,{tab=1},{Text("Medicamentos pediátricos")},Modifier.weight(1f))
   FilterChip(tab==2,{tab=2},{Text("Fluoruros / clorhexidina")},Modifier.weight(1f))
   FilterChip(tab==3,{tab=3},{Text("IMC")},Modifier.weight(1f))
   FilterChip(tab==4,{tab=4},{Text("Conversión y práctica")},Modifier.weight(1f))
  }}
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
   item{SectionCard("1 · Grupo farmacológico"){
    pediatricDrugGroupsV40.forEach{g->Text(g.title,fontWeight=FontWeight.Bold);Text(g.examples);Spacer(Modifier.height(6.dp))}
   }}
   item{SectionCard("2 · Cálculo desde una prescripción/protocolo"){
    Text("Introduce la dosis mg/kg ya indicada por una fuente clínica autorizada. La calculadora solo convierte matemáticamente; no decide qué fármaco, dosis, intervalo ni duración debe recibir el paciente.")
    OutlinedTextField(weight,{weight=it.filter{x->x.isDigit()||x=='.'}.take(6)},label={Text("Peso (kg)")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(doseMgKg,{doseMgKg=it.filter{x->x.isDigit()||x=='.'}.take(7)},label={Text("Dosis indicada (mg/kg por dosis)")},modifier=Modifier.fillMaxWidth())
    OutlinedTextField(concentration,{concentration=it.filter{x->x.isDigit()||x=='.'}.take(8)},label={Text("Concentración de la presentación (mg/mL)")},modifier=Modifier.fillMaxWidth())
    Text(if(doseMg==null)"Completa peso y dosis indicada." else "Resultado: %.1f mg por dosis".format(doseMg),fontWeight=FontWeight.Bold)
    Text(if(doseMl==null)"Captura mg/mL para convertir a volumen." else "Volumen matemático: %.2f mL por dosis".format(doseMl),fontWeight=FontWeight.Bold)
   }}
   item{NoticeCard("Verifica edad, alergias, función renal/hepática, interacciones, contraindicaciones, concentración comercial, máximo diario y pauta con el docente/profesional antes de usar cualquier resultado.")}
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
