package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.MedicationRecord

private data class MedicationPresentation(val label:String,val dose:String,val unit:String,val route:String)
private data class MedicationOption(val generic:String,val presentations:List<MedicationPresentation>)

private val medicationCatalog=listOf(
    MedicationOption("Amoxicilina",listOf(MedicationPresentation("Cápsula 500 mg","500","mg","Oral"),MedicationPresentation("Suspensión 500 mg/5 mL","500","mg/5 mL","Oral"))),
    MedicationOption("Amoxicilina + ácido clavulánico",listOf(MedicationPresentation("Tableta 500 mg/125 mg","500/125","mg","Oral"),MedicationPresentation("Suspensión 400 mg/57 mg/5 mL","400/57","mg/5 mL","Oral"))),
    MedicationOption("Azitromicina",listOf(MedicationPresentation("Tableta 500 mg","500","mg","Oral"),MedicationPresentation("Suspensión 200 mg/5 mL","200","mg/5 mL","Oral"))),
    MedicationOption("Clindamicina",listOf(MedicationPresentation("Cápsula 300 mg","300","mg","Oral"))),
    MedicationOption("Metronidazol",listOf(MedicationPresentation("Tableta 500 mg","500","mg","Oral"))),
    MedicationOption("Paracetamol",listOf(MedicationPresentation("Tableta 500 mg","500","mg","Oral"),MedicationPresentation("Suspensión 100 mg/mL","100","mg/mL","Oral"))),
    MedicationOption("Ibuprofeno",listOf(MedicationPresentation("Tableta 400 mg","400","mg","Oral"),MedicationPresentation("Tableta 600 mg","600","mg","Oral"),MedicationPresentation("Suspensión 100 mg/5 mL","100","mg/5 mL","Oral"))),
    MedicationOption("Naproxeno",listOf(MedicationPresentation("Tableta 250 mg","250","mg","Oral"),MedicationPresentation("Tableta 500 mg","500","mg","Oral"))),
    MedicationOption("Diclofenaco",listOf(MedicationPresentation("Tableta 50 mg","50","mg","Oral"))),
    MedicationOption("Ketorolaco",listOf(MedicationPresentation("Tableta 10 mg","10","mg","Oral")))
)

private val frequencyOptions=listOf("Dosis única","Cada 6 horas","Cada 8 horas","Cada 12 horas","Cada 24 horas")

private val anestheticOptions=listOf(
    MedicationPresentation("Lidocaína 2% + epinefrina 1:100,000 · cartucho 1.8 mL","36","mg/cartucho","Infiltración/bloqueo"),
    MedicationPresentation("Lidocaína 2% sin vasoconstrictor","20","mg/mL","Infiltración/bloqueo"),
    MedicationPresentation("Prilocaína 3% + felipresina · cartucho 1.8 mL","54","mg/cartucho","Infiltración"),
    MedicationPresentation("Articaína 4% + epinefrina · cartucho 1.8 mL","72","mg/cartucho","Infiltración/bloqueo"),
    MedicationPresentation("Mepivacaína 3% sin vasoconstrictor · cartucho 1.8 mL","54","mg/cartucho","Infiltración/bloqueo"),
    MedicationPresentation("Bupivacaína 0.5% + vasoconstrictor · cartucho 1.8 mL","9","mg/cartucho","Bloqueo")
)

private fun normalizeMedication(text:String)=text.trim().lowercase().replace(Regex("\\s+")," ")
private fun medicationKey(m:MedicationRecord)=listOf(m.activeIngredient,m.dose,m.unit,m.route,m.frequency).joinToString("|"){normalizeMedication(it)}

@Composable
private fun ChoiceRow(title:String,choices:List<String>,selected:String,onSelect:(String)->Unit){
    Column(verticalArrangement=Arrangement.spacedBy(6.dp)){
        Text(title,fontWeight=FontWeight.Bold)
        choices.chunked(3).forEach{row->
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                row.forEach{choice->FilterChip(selected=choice==selected,onClick={onSelect(choice)},label={Text(choice)})}
            }
        }
    }
}

@Composable
fun MedicationManagementScreen(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onBack:()->Unit){
    var weightText by remember{mutableStateOf("")}
    var selectedMedication by remember{mutableStateOf<MedicationOption?>(null)}
    var selectedPresentation by remember{mutableStateOf<MedicationPresentation?>(null)}
    var selectedFrequency by remember{mutableStateOf("")}
    var selectedAnesthetic by remember{mutableStateOf<MedicationPresentation?>(null)}

    val medications=session.medicationsStructured
    val weight=weightText.replace(",",".").toDoubleOrNull()
    val medicationDraft=selectedMedication?.let{med->selectedPresentation?.let{p->MedicationRecord(name=med.generic,activeIngredient=med.generic,dose=p.dose,unit=p.unit,route=p.route,frequency=selectedFrequency)}}
    val duplicate=medicationDraft!=null&&selectedFrequency.isNotBlank()&&medications.any{medicationKey(it)==medicationKey(medicationDraft)}
    val weightLabel=if(weightText.isBlank())"pendiente" else weightText

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){
            OutlinedButton(onClick=onBack){Text("‹ "+tr(lang,"Volver","Back"))}
            Text(tr(lang,"Medicamentos y anestésicos","Medications and anesthetics"),fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
        }
        Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                Text(tr(lang,"Selector clínico educativo","Educational clinical selector"),fontWeight=FontWeight.Black)
                Text(tr(lang,"El alumno no escribe el medicamento, la presentación, la dosis ni la vía. Selecciona opciones del catálogo y captura únicamente el peso del paciente.","The student does not type the medication, presentation, dose or route. They select catalog options and enter only the patient's weight."))
            }
        }
        OutlinedTextField(value=weightText,onValueChange={weightText=it.filter{ch->ch.isDigit()||ch=='.'||ch==','}},label={Text(tr(lang,"Peso del paciente (kg) *","Patient weight (kg) *"))},singleLine=true,modifier=Modifier.fillMaxWidth())

        Text(tr(lang,"Medicamento","Medication"),fontWeight=FontWeight.Black)
        medicationCatalog.chunked(3).forEach{row->
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                row.forEach{option->FilterChip(selected=selectedMedication?.generic==option.generic,onClick={selectedMedication=option;selectedPresentation=null;selectedFrequency=""},label={Text(option.generic)})}
            }
        }
        selectedMedication?.let{medication->
            ChoiceRow(tr(lang,"Presentación","Presentation"),medication.presentations.map{it.label},selectedPresentation?.label.orEmpty()){label->selectedPresentation=medication.presentations.first{it.label==label}}
            ChoiceRow(tr(lang,"Frecuencia / pauta","Frequency / schedule"),frequencyOptions,selectedFrequency){selectedFrequency=it}
        }
        if(duplicate)Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer)){Text(tr(lang,"⚠ Ya existe el mismo principio activo, presentación y frecuencia.","⚠ The same active ingredient, presentation and frequency already exists."),Modifier.padding(12.dp),fontWeight=FontWeight.Bold)}
        val canAddMedication=weight!=null&&weight>0&&medicationDraft!=null&&selectedFrequency.isNotBlank()&&!duplicate
        Button(onClick={if(canAddMedication){onSessionChanged(session.copy(medicationsStructured=medications+medicationDraft!!));selectedMedication=null;selectedPresentation=null;selectedFrequency=""}},enabled=canAddMedication,modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Agregar medicamento seleccionado","Add selected medication"))}

        Text(tr(lang,"Anestésico local","Local anesthetic"),fontWeight=FontWeight.Black)
        Text(tr(lang,"Selecciona el anestésico y su presentación; se utiliza el mismo peso del paciente.","Select the anesthetic and its presentation; the same patient weight is used."))
        anestheticOptions.chunked(2).forEach{row->
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                row.forEach{option->FilterChip(selected=selectedAnesthetic?.label==option.label,onClick={selectedAnesthetic=option},label={Text(option.label)})}
            }
        }
        selectedAnesthetic?.let{anesthetic->
            Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
                Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
                    Text(anesthetic.label,fontWeight=FontWeight.Black)
                    Text(tr(lang,"Concentración","Strength")+": "+anesthetic.dose+" "+anesthetic.unit)
                    Text(tr(lang,"Vía/técnica","Route/technique")+": "+anesthetic.route)
                    Text(tr(lang,"Peso registrado: ","Recorded weight: ")+weightLabel+" kg. "+tr(lang,"La dosis máxima debe verificarse con la información oficial del producto y las características del paciente.","Maximum dose must be verified against official product information and patient characteristics."))
                }
            }
        }

        Text(medications.size.toString()+" "+tr(lang,"medicamento(s) estructurado(s)","structured medication(s)"),fontWeight=FontWeight.Bold)
        medications.forEach{medication->
            Card(Modifier.fillMaxWidth()){
                Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){
                    Text(medication.activeIngredient,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
                    Text(medication.dose+" "+medication.unit+" · "+medication.route)
                    Text(medication.frequency.ifBlank{tr(lang,"Pauta no seleccionada","Schedule not selected")})
                    OutlinedButton(onClick={onSessionChanged(session.copy(medicationsStructured=medications.filterNot{it.id==medication.id}))},modifier=Modifier.fillMaxWidth()){Text(tr(lang,"Eliminar","Remove"))}
                }
            }
        }
    }
}
