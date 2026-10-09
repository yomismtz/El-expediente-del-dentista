package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEvent
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus
import com.yomismtz.expedientedeldentista.clinical.auditOdontogram

private data class OdontoArchV20(val titleEs:String,val titleEn:String,val teeth:List<Int>)
private data class OdontoQuadrantV20(val titleEs:String,val titleEn:String,val teeth:List<Int>)

private fun quadrantsV20(primary:Boolean)=if(primary) listOf(
    OdontoQuadrantV20("Q5 · superior derecho","Q5 · upper right",listOf(55,54,53,52,51)),
    OdontoQuadrantV20("Q6 · superior izquierdo","Q6 · upper left",listOf(61,62,63,64,65)),
    OdontoQuadrantV20("Q8 · inferior derecho","Q8 · lower right",listOf(85,84,83,82,81)),
    OdontoQuadrantV20("Q7 · inferior izquierdo","Q7 · lower left",listOf(71,72,73,74,75))
) else listOf(
    OdontoQuadrantV20("Q1 · superior derecho","Q1 · upper right",listOf(18,17,16,15,14,13,12,11)),
    OdontoQuadrantV20("Q2 · superior izquierdo","Q2 · upper left",listOf(21,22,23,24,25,26,27,28)),
    OdontoQuadrantV20("Q4 · inferior derecho","Q4 · lower right",listOf(48,47,46,45,44,43,42,41)),
    OdontoQuadrantV20("Q3 · inferior izquierdo","Q3 · lower left",listOf(31,32,33,34,35,36,37,38))
)

private fun surfaceShortV20(surface:Surface)=when(surface){
    Surface.VESTIBULAR->"V"
    Surface.LINGUAL_PALATAL->"L/P"
    Surface.MESIAL->"M"
    Surface.DISTAL->"D"
    Surface.OCCLUSAL->"O"
}

private fun markLabelV20(mark:SurfaceMark,lang:String)=when(mark){
    SurfaceMark.HEALTHY->tr(lang,"Borrar cara","Clear surface")
    SurfaceMark.CARIES->tr(lang,"Caries","Caries")
    SurfaceMark.RESTORATION->tr(lang,"Restauración","Restoration")
    SurfaceMark.SEALANT->tr(lang,"Sellador","Sealant")
}

private fun markColorV20(mark:SurfaceMark?)=when(mark){
    SurfaceMark.CARIES->Color(0xFFE04B57)
    SurfaceMark.RESTORATION->Color(0xFF4C8DEB)
    SurfaceMark.SEALANT->Color(0xFF9B6AD6)
    else->Color.Transparent
}

@Composable
fun OdontogramV20Screen(
    lang:String,
    session:EducationalSession,
    onSessionChanged:(EducationalSession)->Unit,
    onBack:()->Unit
){
    var primary by remember{mutableStateOf(false)}
    var selectedTooth by remember{mutableStateOf(16)}
    var selectedMark by remember{mutableStateOf(SurfaceMark.CARIES)}
    val quadrants=quadrantsV20(primary)
    val all=quadrants.flatMap{it.teeth}
    if(selectedTooth !in all) selectedTooth=all.first()

    val record=session.teeth[selectedTooth]
    val isRecorded=record!=null
    val currentRecord=record?:ToothRecord()
    val missing=record?.status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
    val unerupted=record?.status == ToothStatus.UNERUPTED
    val unavailable=missing || unerupted
    val auditIssues=auditOdontogram(session).filter { it.tooth == selectedTooth }
    val marks=session.odontogramSurfaces[selectedTooth]?:emptyMap()

    fun saveSurface(surface:Surface){
        if(!isRecorded || unavailable) return
        val updated=marks.toMutableMap()
        if(selectedMark==SurfaceMark.HEALTHY) updated.remove(surface) else updated[surface]=selectedMark
        val status=when{
            currentRecord.status==ToothStatus.EXTRACTION_INDICATED->ToothStatus.EXTRACTION_INDICATED
            updated.values.any{it==SurfaceMark.CARIES}->ToothStatus.CARIES
            updated.values.any{it==SurfaceMark.RESTORATION}->ToothStatus.RESTORED
            updated.values.any{it==SurfaceMark.SEALANT}->ToothStatus.SEALANT
            else->ToothStatus.HEALTHY
        }
        onSessionChanged(session.copy(
            odontogramSurfaces=session.odontogramSurfaces+(selectedTooth to updated),
            teeth=session.teeth+(selectedTooth to currentRecord.copy(status=status)),
            presentTeeth=session.presentTeeth+selectedTooth
        ))
    }

    fun setWholeStatus(status:ToothStatus){
        val present=session.presentTeeth.toMutableSet()
        if(status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER,ToothStatus.UNERUPTED)) present.remove(selectedTooth) else present.add(selectedTooth)
        val surfaces=if(status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER,ToothStatus.UNERUPTED)) session.odontogramSurfaces-selectedTooth else session.odontogramSurfaces
        val effectiveStatus=if(status==ToothStatus.HEALTHY){
            val retained=surfaces[selectedTooth].orEmpty().values
            when { retained.any{it==SurfaceMark.CARIES}->ToothStatus.CARIES; retained.any{it==SurfaceMark.RESTORATION}->ToothStatus.RESTORED; retained.any{it==SurfaceMark.SEALANT}->ToothStatus.SEALANT; else->ToothStatus.HEALTHY }
        } else status
        onSessionChanged(session.copy(presentTeeth=present,odontogramSurfaces=surfaces,teeth=session.teeth+(selectedTooth to currentRecord.copy(status=effectiveStatus))))
    }

    ResponsiveScreenV17(
        tr(lang,"Odontograma","Odontogram"),
        tr(lang,"Dentición → cuadrante → órgano dentario → estado del diente → superficies. Cada superficie conserva su propia marca.","Dentition → quadrant → tooth → whole-tooth status → surfaces. Each surface keeps its own mark."),
        onBack
    ){profile->
        ResponsiveSectionV17(tr(lang,"1 · Dentición","1 · Dentition"),tr(lang,"Selecciona permanente o temporal antes de elegir el órgano dentario.","Select permanent or primary dentition before choosing a tooth.")){
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                val value=i==1
                FilterChip(primary==value,{primary=value},{Text(if(value)tr(lang,"Temporal","Primary") else tr(lang,"Permanente","Permanent"))},Modifier.fillMaxWidth())
            }
        }

        val wideQuadrants = profile.width == ScreenWidthV17.EXPANDED && !profile.largeSystemText
        @Composable fun quadrantContent(q:OdontoQuadrantV20) {
            ResponsiveSectionV17("2 · "+(if(lang=="en")q.titleEn else q.titleEs)) {
                AdaptiveGridV17(q.teeth.size,q.teeth.size){i->
                    val tooth=q.teeth[i]
                    val status=session.teeth[tooth]?.status
                    val isRecordedTooth=status!=null
                    val isMissing=status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
                    val isUnerupted=status==ToothStatus.UNERUPTED
                    val selected=selectedTooth==tooth
                    val hasMark=session.odontogramSurfaces[tooth]?.isNotEmpty()==true
                    Card(onClick={selectedTooth=tooth},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=when{selected->MaterialTheme.colorScheme.primary;isMissing->MaterialTheme.colorScheme.errorContainer;hasMark->MaterialTheme.colorScheme.secondaryContainer;!isRecordedTooth->MaterialTheme.colorScheme.surfaceVariant;else->MaterialTheme.colorScheme.surface}),border=BorderStroke(1.dp,if(selected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha=.4f)),shape=RoundedCornerShape(12.dp)){
                        Column(Modifier.fillMaxWidth().padding(vertical=7.dp),horizontalAlignment=Alignment.CenterHorizontally){
                            Text(if(isMissing)"✕" else if(isUnerupted)"U" else if(!isRecordedTooth)"?" else "🦷",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                            Text(tooth.toString(),fontWeight=FontWeight.Black,color=if(selected)MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
        if(wideQuadrants) {
            quadrants.chunked(2).forEach { pair ->
                androidx.compose.foundation.layout.Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    pair.forEach { q -> androidx.compose.foundation.layout.Box(Modifier.weight(1f)) { quadrantContent(q) } }
                }
            }
        } else {
            quadrants.forEach { q -> quadrantContent(q) }
        }

        ResponsiveSectionV17(tr(lang,"3 · OD $selectedTooth","3 · Tooth $selectedTooth"),if(!isRecorded)tr(lang,"Diente no registrado: primero marca explícitamente Presente o Ausente.","Tooth not registered: first explicitly mark it Present or Missing.") else if(missing)tr(lang,"Diente ausente: se muestra una X en su viñeta.","Missing tooth: an X is shown in its tile.") else if(unerupted)tr(lang,"Diente no erupcionado: no se registran superficies clínicas todavía.","Unerupted tooth: clinical surfaces are not recorded yet.") else tr(lang,"Puedes combinar marcas en distintas caras del mismo diente.","You can combine marks on different surfaces of the same tooth.")){
            Text(tr(lang,"Marca de superficie","Surface mark"),fontWeight=FontWeight.Black)
            AdaptiveGridV17(SurfaceMark.entries.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 4){i->
                val mark=SurfaceMark.entries[i]
                FilterChip(selectedMark==mark,{selectedMark=mark},{Text(markLabelV20(mark,lang))},Modifier.fillMaxWidth(),enabled=isRecorded && !unavailable)
            }

            if(!isRecorded){
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),modifier=Modifier.fillMaxWidth()){
                    Column(Modifier.padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally){
                        Text("?",style=MaterialTheme.typography.displayMedium,fontWeight=FontWeight.Black)
                        Text(tr(lang,"OD $selectedTooth no registrado. Selecciona \"Presente\" para registrarlo como sano o \"Ausente\" para registrar su ausencia.","Tooth $selectedTooth is not registered. Select \"Present\" to record it as sound or \"Missing\" to record its absence."),fontWeight=FontWeight.Bold)
                    }
                }
            }else if(missing){
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer),modifier=Modifier.fillMaxWidth()){
                    Column(Modifier.padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally){
                        Text("✕",style=MaterialTheme.typography.displayMedium,fontWeight=FontWeight.Black)
                        Text(tr(lang,"OD $selectedTooth ausente. Márcalo presente para registrar superficies.","Tooth $selectedTooth is missing. Mark it present to record surfaces."),fontWeight=FontWeight.Bold)
                    }
                }
            }else if(unerupted){
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),modifier=Modifier.fillMaxWidth()){
                    Column(Modifier.padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally){
                        Text("U",style=MaterialTheme.typography.displayMedium,fontWeight=FontWeight.Black)
                        Text(tr(lang,"Diente no erupcionado; excluido del registro de superficies hasta valoración clínica.","Unerupted tooth; excluded from surface recording pending clinical assessment."),fontWeight=FontWeight.Bold)
                    }
                }
            }else{
                DentalSurfaceDiagram(
                    centerEnabled=true,
                    surfaceColor={surface->markColorV20(marks[surface]).let{if(it==Color.Transparent)MaterialTheme.colorScheme.primaryContainer.copy(alpha=.58f) else it}},
                    onSurfaceTap={saveSurface(it)},
                    modifier=Modifier.fillMaxWidth()
                )
                val groups=marks.entries.groupBy{it.value}
                if(groups.isEmpty()) Text(tr(lang,"Aún no hay caras marcadas en este diente.","No surfaces are marked on this tooth yet."))
                groups.forEach{(mark,entries)->
                    Text("${markLabelV20(mark,lang)}: ${entries.map{surfaceShortV20(it.key)}.sorted().joinToString(", ")}",fontWeight=FontWeight.Bold,color=if(mark==SurfaceMark.CARIES)Color(0xFFE04B57) else MaterialTheme.colorScheme.primary)
                }
                OutlinedButton(onClick={
                    onSessionChanged(session.copy(
                        odontogramSurfaces=session.odontogramSurfaces-selectedTooth,
                        teeth=session.teeth+(selectedTooth to record.copy(status=ToothStatus.HEALTHY))
                    ))
                },modifier=Modifier.fillMaxWidth()) { Text(tr(lang,"Limpiar todas las caras del OD $selectedTooth","Clear all surfaces on tooth $selectedTooth")) }
            }

            val linkedPlan=record?.diagnosisId?.let { id -> ClinicalContent.treatmentPlans.firstOrNull { it.id==id } }
            val linkedOption=linkedPlan?.options?.firstOrNull { it.id==record?.treatmentId }
            Text(tr(lang,"Cadena clínica del OD","Tooth clinical chain"),fontWeight=FontWeight.Black)
            Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),modifier=Modifier.fillMaxWidth()){
                Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){
                    Text("OD $selectedTooth",fontWeight=FontWeight.Black)
                    Text("1. "+tr(lang,"Hallazgos","Findings")+": "+if(marks.isEmpty())tr(lang,"Sin marcas de superficie","No surface marks") else marks.entries.joinToString(", "){surfaceShortV20(it.key)+": "+markLabelV20(it.value,lang)})
                    Text("2. "+tr(lang,"Diagnóstico educativo","Teaching diagnosis")+": "+(linkedPlan?.let{if(lang=="en")it.diagnosisEn else it.diagnosisEs}?:tr(lang,"No vinculado","Not linked")))
                    Text("3. "+tr(lang,"Tratamiento","Treatment")+": "+(linkedOption?.let{if(lang=="en")it.labelEn else it.labelEs}?:tr(lang,"No seleccionado","Not selected")))
                    Text(tr(lang,"La relación se guarda por diente y no sustituye el diagnóstico clínico.","The relationship is stored per tooth and does not replace clinical diagnosis."),style=MaterialTheme.typography.bodySmall)
                }
            }
            if(linkedPlan!=null){
                Text(tr(lang,"Cambiar diagnóstico educativo","Change teaching diagnosis"),fontWeight=FontWeight.Black)
                AdaptiveGridV17(ClinicalContent.treatmentPlans.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                    val plan=ClinicalContent.treatmentPlans[i]
                    FilterChip(record?.diagnosisId==plan.id,{onSessionChanged(session.copy(teeth=session.teeth+(selectedTooth to currentRecord.copy(diagnosisId=plan.id,treatmentId=null)),clinicalEvents=session.clinicalEvents+ClinicalEvent(System.currentTimeMillis(),"tooth_diagnosis_link","OD $selectedTooth → diagnóstico: "+plan.diagnosisEs)))},{Text(if(lang=="en")plan.diagnosisEn else plan.diagnosisEs)},Modifier.fillMaxWidth())
                }
                Text(tr(lang,"Tratamiento educativo","Teaching treatment"),fontWeight=FontWeight.Black)
                linkedPlan.options.forEach { option->
                    FilterChip(linkedOption?.id==option.id,{onSessionChanged(session.copy(teeth=session.teeth+(selectedTooth to currentRecord.copy(treatmentId=option.id)),clinicalEvents=session.clinicalEvents+ClinicalEvent(System.currentTimeMillis(),"tooth_treatment_link","OD $selectedTooth → tratamiento: "+option.labelEs)))},{Text(if(lang=="en")option.labelEn else option.labelEs)},Modifier.fillMaxWidth())
                }
            }

            Text(tr(lang,"Estado del diente completo","Whole-tooth status"),fontWeight=FontWeight.Black)
            val wholeStatuses=listOf(ToothStatus.HEALTHY,ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER,ToothStatus.UNERUPTED,ToothStatus.EXTRACTION_INDICATED)
            AdaptiveGridV17(wholeStatuses.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                val status=wholeStatuses[i]
                val label=when(status){
                    ToothStatus.HEALTHY->tr(lang,"Presente","Present")
                    ToothStatus.MISSING_CARIES->tr(lang,"Ausente por caries","Missing due to caries")
                    ToothStatus.MISSING_OTHER->tr(lang,"Ausente por otra causa","Missing for another reason")
                    ToothStatus.UNERUPTED->tr(lang,"No erupcionado","Unerupted")
                    ToothStatus.EXTRACTION_INDICATED->tr(lang,"Extracción indicada","Extraction indicated")
                    else->status.name
                }
                FilterChip(selected=isRecorded && record?.status==status,onClick={setWholeStatus(status)},label={Text(label)},modifier=Modifier.fillMaxWidth())
            }
            auditIssues.forEach { issue ->
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer),modifier=Modifier.fillMaxWidth()) {
                    Text("⚠ ${issue.messageEs}",Modifier.padding(10.dp),color=MaterialTheme.colorScheme.onErrorContainer,style=MaterialTheme.typography.bodySmall)
                }
            }
        }

        LocalClinicalImageSectionV46(lang,profile,"Odontograma","Odontogram")

        NoticeCard(tr(lang,
            "Ejemplo: un mismo OD puede quedar O y V en caries, M en restauración y D en sellador. Cada cara conserva su propia marca. Al declarar el diente ausente se borran las marcas de superficies para evitar datos contradictorios. Registra al final la simbología exigida por tu expediente físico.",
            "Example: the same tooth may have O and V caries, an M restoration and a D sealant. Each surface keeps its own mark. Marking a tooth missing clears its surface marks to avoid contradictory data. Record the symbols required by your physical chart."
        ))
    }
}
