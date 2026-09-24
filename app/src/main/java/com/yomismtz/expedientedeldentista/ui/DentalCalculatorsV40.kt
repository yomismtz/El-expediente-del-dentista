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

 LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{ScreenHeader("Calculadoras odontológicas",onBack,"Herramientas educativas separadas de los auxiliares de diagnóstico. No sustituyen prescripción, ficha técnica ni supervisión clínica.")}
  item{Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
   FilterChip(tab==0,{tab=0},{Text("Anestésico local")},Modifier.weight(1f))
   FilterChip(tab==1,{tab=1},{Text("Medicamentos pediátricos")},Modifier.weight(1f))
   FilterChip(tab==2,{tab=2},{Text("Conversión y práctica")},Modifier.weight(1f))
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
