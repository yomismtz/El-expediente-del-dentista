package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.MedicationRecord
import kotlin.math.floor
import kotlin.math.min

private data class MedicationPresentation(val label:String,val dose:String,val unit:String,val route:String)
private data class MedicationOption(val generic:String,val presentations:List<MedicationPresentation>)

enum class MedicationToolSection { ALL, MEDICATIONS, ANESTHETICS }

private data class AnestheticReference(
    val presentation: MedicationPresentation,
    val mgPerKg: Double?,
    val absoluteMaxMg: Double?,
    val absoluteMaxLabel: String?,
    val cartridgeVolumeMl: Double?,
    val cartridgeMg: Double?,
    val vasoconstrictorMicrogramsPerCartridge: Double? = null
)

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
    MedicationOption("Ketorolaco",listOf(MedicationPresentation("Tableta 10 mg","10","mg","Oral"))),
    MedicationOption("Dexametasona",listOf(MedicationPresentation("Tableta 0.5 mg","0.5","mg","Oral"))),
    MedicationOption("Prednisona",listOf(MedicationPresentation("Tableta 5 mg","5","mg","Oral"),MedicationPresentation("Tableta 20 mg","20","mg","Oral"))),
    MedicationOption("Clorhexidina",listOf(MedicationPresentation("Enjuague 0.12%","0.12","%","Oral"))),
    MedicationOption("Nistatina",listOf(MedicationPresentation("Suspensión oral 100,000 UI/mL","100000","UI/mL","Oral"))),
    MedicationOption("Aciclovir",listOf(MedicationPresentation("Tableta 200 mg","200","mg","Oral"),MedicationPresentation("Tableta 400 mg","400","mg","Oral"))),
    MedicationOption("Fluconazol",listOf(MedicationPresentation("Cápsula 150 mg","150","mg","Oral"))),
    MedicationOption("Ketoprofeno",listOf(MedicationPresentation("Cápsula 100 mg","100","mg","Oral"))),
    MedicationOption("Celecoxib",listOf(MedicationPresentation("Cápsula 200 mg","200","mg","Oral")))
)

private val pediatricDoseGuides=mapOf(
    "Paracetamol" to "10–15 mg/kg por dosis",
    "Ibuprofeno" to "5–10 mg/kg por dosis",
    "Amoxicilina" to "25–50 mg/kg/día",
    "Amoxicilina + ácido clavulánico" to "25–45 mg/kg/día (componente de amoxicilina)",
    "Azitromicina" to "10 mg/kg el día 1; después 5 mg/kg/día",
    "Clindamicina" to "20–40 mg/kg/día",
    "Metronidazol" to "20–30 mg/kg/día"
)

private val frequencyOptions=listOf("Dosis única","Cada 6 horas","Cada 8 horas","Cada 12 horas","Cada 24 horas","Cada 48 horas")

private val anestheticReferences=listOf(
    AnestheticReference(
        MedicationPresentation("Lidocaína 2% + epinefrina 1:100,000 · cartucho 1.8 mL","36","mg/cartucho","Infiltración/bloqueo"),
        7.0,500.0,"máximo absoluto de referencia 500 mg",1.8,36.0,18.0
    ),
    AnestheticReference(
        MedicationPresentation("Lidocaína 2% sin vasoconstrictor","20","mg/mL","Infiltración/bloqueo"),
        4.5,300.0,"máximo absoluto de referencia 300 mg",null,null
    ),
    AnestheticReference(
        MedicationPresentation("Prilocaína 3% + felipresina · cartucho 1.8 mL","54","mg/cartucho","Infiltración"),
        8.0,null,null,1.8,54.0
    ),
    AnestheticReference(
        MedicationPresentation("Articaína 4% + epinefrina · cartucho 1.8 mL","72","mg/cartucho","Infiltración/bloqueo"),
        7.0,500.0,"máximo absoluto de referencia 500 mg",1.8,72.0
    ),
    AnestheticReference(
        MedicationPresentation("Mepivacaína 3% sin vasoconstrictor · cartucho 1.8 mL","54","mg/cartucho","Infiltración/bloqueo"),
        6.6,270.0,"máximo absoluto de la referencia dental consultada 270 mg",1.8,54.0
    ),
    AnestheticReference(
        MedicationPresentation("Bupivacaína 0.5% + vasoconstrictor · cartucho 1.8 mL","9","mg/cartucho","Bloqueo"),
        null,90.0,"máximo dental de referencia 90 mg por sesión",1.8,9.0
    ),
    AnestheticReference(
        MedicationPresentation("Articaína 4% sin vasoconstrictor · cartucho 1.8 mL","72","mg/cartucho","Infiltración/bloqueo"),
        7.0,500.0,"máximo absoluto de referencia 500 mg",1.8,72.0
    ),
    AnestheticReference(
        MedicationPresentation("Mepivacaína 2% + levonordefrina · cartucho 1.8 mL","36","mg/cartucho","Infiltración/bloqueo"),
        6.6,180.0,"máximo absoluto de la referencia dental consultada 180 mg",1.8,36.0
    )
)

private fun normalizeMedication(text:String)=text.trim().lowercase().replace(Regex("\\s+")," ")
private fun medicationKey(m:MedicationRecord)=listOf(m.activeIngredient,m.dose,m.unit,m.route,m.frequency).joinToString("|"){normalizeMedication(it)}

@Composable private fun ChoiceRow(title:String,choices:List<String>,selected:String,onSelect:(String)->Unit){
    Column(verticalArrangement=Arrangement.spacedBy(6.dp)){
        Text(title,fontWeight=FontWeight.Bold)
        choices.chunked(3).forEach{row->
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                row.forEach{choice->FilterChip(selected=choice==selected,onClick={onSelect(choice)},label={Text(choice)})}
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable fun MedicationManagementScreen(
    lang:String,
    session:EducationalSession,
    onSessionChanged:(EducationalSession)->Unit,
    onBack:()->Unit,
    section: MedicationToolSection = MedicationToolSection.ALL
){
    var weightText by remember{mutableStateOf("")}
    var selectedMedication by remember{mutableStateOf<MedicationOption?>(null)}
    var selectedPresentation by remember{mutableStateOf<MedicationPresentation?>(null)}
    var selectedFrequency by remember{mutableStateOf("")}
    var selectedAnesthetic by remember{mutableStateOf<AnestheticReference?>(null)}

    val medications=session.medicationsStructured
    val weight=weightText.replace(",","." ).toDoubleOrNull()
    val pediatricGuide=selectedMedication?.generic?.let{pediatricDoseGuides[it]}
    val pediatricRange=when(selectedMedication?.generic){
        "Paracetamol"->weight?.let{w->String.format("%.1f–%.1f mg por dosis",w*10,w*15)}
        "Ibuprofeno"->weight?.let{w->String.format("%.1f–%.1f mg por dosis",w*5,w*10)}
        else->null
    }
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

        if(section != MedicationToolSection.ANESTHETICS){
        OutlinedTextField(
            value=weightText,
            onValueChange={weightText=it.filter{ch->ch.isDigit()||ch=='.'||ch==','}},
            label={Text(tr(lang,"Peso del paciente (kg) *","Patient weight (kg) *"))},
            singleLine=true,
            modifier=Modifier.fillMaxWidth()
        )

        Text(tr(lang,"Medicamento","Medication"),fontWeight=FontWeight.Black)
        medicationCatalog.chunked(3).forEach{row->
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                row.forEach{option->
                    FilterChip(
                        selected=selectedMedication?.generic==option.generic,
                        onClick={selectedMedication=option;selectedPresentation=null;selectedFrequency=""},
                        label={Text(option.generic)}
                    )
                }
            }
        }

        selectedMedication?.let{
            pediatricGuide?.let{guide->
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
                    Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
                        Text(tr(lang,"Referencia pediátrica educativa","Educational pediatric reference"),fontWeight=FontWeight.Black)
                        Text(tr(lang,"Referencia por peso","Weight-based reference")+": "+guide)
                        pediatricRange?.let{range->Text(tr(lang,"Rango calculado para el peso registrado","Calculated range for recorded weight")+": "+range)}
                        Text(tr(lang,"Este cálculo es orientativo para aprendizaje; debe verificarse con la información oficial del medicamento, edad, indicación y características del paciente.","This calculation is educational; verify it against official product information, age, indication and patient characteristics."))
                    }
                }
            }
        }

        selectedMedication?.let{medication->
            ChoiceRow(tr(lang,"Presentación","Presentation"),medication.presentations.map{it.label},selectedPresentation?.label.orEmpty()){label->selectedPresentation=medication.presentations.first{it.label==label}}
            ChoiceRow(tr(lang,"Frecuencia / pauta","Frequency / schedule"),frequencyOptions,selectedFrequency){selectedFrequency=it}
        }

        if(duplicate)Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer)){
            Text(tr(lang,"⚠ Ya existe el mismo principio activo, presentación y frecuencia.","⚠ The same active ingredient, presentation and frequency already exists."),Modifier.padding(12.dp),fontWeight=FontWeight.Bold)
        }

        val canAddMedication=weight!=null&&weight>0&&medicationDraft!=null&&selectedFrequency.isNotBlank()&&!duplicate
        Button(
            onClick={if(canAddMedication){onSessionChanged(session.copy(medicationsStructured=medications+medicationDraft!!));selectedMedication=null;selectedPresentation=null;selectedFrequency=""}},
            enabled=canAddMedication,
            modifier=Modifier.fillMaxWidth()
        ){Text(tr(lang,"Agregar medicamento seleccionado","Add selected medication"))}
        }

        if(section != MedicationToolSection.MEDICATIONS){
        Text(tr(lang,"Anestésico local","Local anesthetic"),fontWeight=FontWeight.Black)
        Text(tr(lang,"Primero captura el peso del paciente. Después selecciona el anestésico. Las opciones se muestran en dos columnas para evitar que los textos se amontonen.","First enter the patient's weight. Then select the anesthetic. Options are shown in two columns to prevent crowded text."))

        if(section == MedicationToolSection.ANESTHETICS){
            OutlinedTextField(
                value=weightText,
                onValueChange={weightText=it.filter{ch->ch.isDigit()||ch=='.'||ch==','}},
                label={Text(tr(lang,"Peso del paciente (kg) *","Patient weight (kg) *"))},
                singleLine=true,
                modifier=Modifier.fillMaxWidth()
            )
        }

        FlowRow(
            modifier=Modifier.fillMaxWidth(),
            maxItemsInEachRow=2,
            horizontalArrangement=Arrangement.spacedBy(8.dp),
            verticalArrangement=Arrangement.spacedBy(8.dp)
        ){
            anestheticReferences.forEach{reference->
                FilterChip(
                    selected=selectedAnesthetic?.presentation?.label==reference.presentation.label,
                    onClick={selectedAnesthetic=reference},
                    modifier=Modifier.fillMaxWidth(0.48f),
                    label={
                        Text(
                            reference.presentation.label,
                            maxLines=3,
                            minLines=3
                        )
                    }
                )
            }
        }

        selectedAnesthetic?.let{reference->
            val anesthetic=reference.presentation
            val weightBasedMg=if(weight!=null&&reference.mgPerKg!=null)weight*reference.mgPerKg else null
            val effectiveMaxMg=when{
                weightBasedMg!=null&&reference.absoluteMaxMg!=null->min(weightBasedMg,reference.absoluteMaxMg)
                weightBasedMg!=null->weightBasedMg
                reference.absoluteMaxMg!=null->reference.absoluteMaxMg
                else->null
            }
            val weightBasedCartridges=if(weightBasedMg!=null&&reference.cartridgeMg!=null)weightBasedMg/reference.cartridgeMg else null
            val effectiveCartridges=if(effectiveMaxMg!=null&&reference.cartridgeMg!=null)effectiveMaxMg/reference.cartridgeMg else null
            val wholeCartridges=effectiveCartridges?.let{floor(it).toInt()}
            val limitingReason=when{
                weightBasedMg!=null&&reference.absoluteMaxMg!=null&&reference.absoluteMaxMg<weightBasedMg->reference.absoluteMaxLabel
                reference.absoluteMaxMg!=null&&weightBasedMg==null->reference.absoluteMaxLabel
                weightBasedMg!=null->tr(lang,"límite por peso","weight-based limit")
                else->reference.absoluteMaxLabel
            }

            Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){
                Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                    Text(anesthetic.label,fontWeight=FontWeight.Black)
                    Text(tr(lang,"Concentración","Strength")+": "+anesthetic.dose+" "+anesthetic.unit)
                    Text(tr(lang,"Vía/técnica","Route/technique")+": "+anesthetic.route)

                    reference.mgPerKg?.let{
                        Text(tr(lang,"Referencia de máximo por peso","Weight-based maximum reference")+": "+String.format("%.1f",it)+" mg/kg")
                        weightBasedMg?.let{mg->Text(tr(lang,"Máximo calculado por peso","Weight-based calculated maximum")+": "+String.format("%.1f",mg)+" mg")}
                    }

                    reference.absoluteMaxMg?.let{
                        Text(tr(lang,"Límite absoluto de referencia","Absolute reference limit")+": "+String.format("%.1f",it)+" mg")
                    }

                    effectiveMaxMg?.let{
                        Text(tr(lang,"Límite efectivo usado para el cálculo","Effective limit used for calculation")+": "+String.format("%.1f",it)+" mg · "+limitingReason)
                    }

                    if(reference.cartridgeMg!=null&&reference.cartridgeVolumeMl!=null){
                        Text(tr(lang,"Contenido del cartucho","Cartridge content")+": "+String.format("%.1f",reference.cartridgeMg)+" mg en "+String.format("%.1f",reference.cartridgeVolumeMl)+" mL")
                        effectiveCartridges?.let{
                            Text(tr(lang,"Equivalencia máxima teórica","Theoretical maximum equivalent")+": "+String.format("%.2f",it)+" cartuchos")
                            Text(tr(lang,"Cartuchos completos sin superar ese límite","Whole cartridges without exceeding that limit")+": "+wholeCartridges)
                        }
                    }else{
                        Text(tr(lang,"No se muestra equivalencia en cartuchos porque el volumen de cartucho no está verificado para esta presentación.","Cartridge equivalence is not shown because the cartridge volume is not verified for this presentation."))
                    }

                    reference.vasoconstrictorMicrogramsPerCartridge?.let{
                        Text(tr(lang,"Epinefrina por cartucho (referencia)","Epinephrine per cartridge (reference)")+": "+String.format("%.1f",it)+" µg")
                    }

                    Text(tr(lang,"Peso registrado: ","Recorded weight: ")+weightLabel+" kg.")
                    Text(tr(lang,"La cifra es un máximo teórico educativo, no una indicación de cuántos cartuchos debe recibir el paciente. La ficha técnica del producto, la edad, el estado clínico, la técnica, las interacciones, el vasoconstrictor y los límites locales pueden reducir el máximo aplicable.","This is an educational theoretical maximum, not an instruction for how many cartridges to administer. Product labeling, age, clinical status, technique, interactions, vasoconstrictor and local limits may reduce the applicable maximum."))
                    Text(tr(lang,"Fuente de referencia revisada: COFEPRIS cuando existe presentación mexicana identificable y fichas técnicas/etiquetado farmacológico para los límites de dosis. Verificar siempre la presentación concreta antes de usar el cálculo.","Reference sources reviewed: COFEPRIS when an identifiable Mexican presentation exists and drug labeling/product information for dose limits. Always verify the exact presentation before using the calculation."))
                }
            }
        }

        }
        
        if(section != MedicationToolSection.ANESTHETICS){
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
        }        }
    }
}
