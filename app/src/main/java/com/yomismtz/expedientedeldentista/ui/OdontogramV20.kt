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
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

private data class OdontoArchV20(val titleEs:String,val titleEn:String,val teeth:List<Int>)

private fun archesV20(primary:Boolean)=if(primary) listOf(
    OdontoArchV20("Q5 · superior derecho","Q5 · upper right",listOf(55,54,53,52,51)),
    OdontoArchV20("Q6 · superior izquierdo","Q6 · upper left",listOf(61,62,63,64,65)),
    OdontoArchV20("Q8 · inferior derecho","Q8 · lower right",listOf(85,84,83,82,81)),
    OdontoArchV20("Q7 · inferior izquierdo","Q7 · lower left",listOf(71,72,73,74,75))
) else listOf(
    OdontoArchV20("Q1 · superior derecho","Q1 · upper right",listOf(18,17,16,15,14,13,12,11)),
    OdontoArchV20("Q2 · superior izquierdo","Q2 · upper left",listOf(21,22,23,24,25,26,27,28)),
    OdontoArchV20("Q4 · inferior derecho","Q4 · lower right",listOf(48,47,46,45,44,43,42,41)),
    OdontoArchV20("Q3 · inferior izquierdo","Q3 · lower left",listOf(31,32,33,34,35,36,37,38))
)

private fun isPosteriorV48(tooth:Int)=tooth%10>=4
private fun eligibleSurfacesV48(tooth:Int)=if(isPosteriorV48(tooth)) Surface.entries.toSet()
else setOf(Surface.VESTIBULAR,Surface.LINGUAL_PALATAL,Surface.MESIAL,Surface.DISTAL)

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
    val arches=archesV20(primary)
    val all=arches.flatMap{it.teeth}
    if(selectedTooth !in all) selectedTooth=all.first()

    val record=session.teeth[selectedTooth]?:ToothRecord()
    val missing=record.status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
    val marks=session.odontogramSurfaces[selectedTooth]?:emptyMap()

    fun saveSurface(surface:Surface){
        if(missing || surface !in eligibleSurfacesV48(selectedTooth)) return
        val updated=marks.toMutableMap()
        if(selectedMark==SurfaceMark.HEALTHY) updated.remove(surface) else updated[surface]=selectedMark
        val status=when{
            updated.values.any{it==SurfaceMark.CARIES}->ToothStatus.CARIES
            updated.values.any{it==SurfaceMark.RESTORATION}->ToothStatus.RESTORED
            updated.values.any{it==SurfaceMark.SEALANT}->ToothStatus.SEALANT
            else->ToothStatus.HEALTHY
        }
        onSessionChanged(session.copy(
            odontogramSurfaces=session.odontogramSurfaces+(selectedTooth to updated),
            teeth=session.teeth+(selectedTooth to record.copy(status=status)),
            presentTeeth=session.presentTeeth+selectedTooth
        ))
    }

    fun setWholeStatus(status:ToothStatus){
        val present=session.presentTeeth.toMutableSet()
        val becomesMissing=status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
        if(becomesMissing) present.remove(selectedTooth) else present.add(selectedTooth)
        val surfaces=if(becomesMissing) session.odontogramSurfaces-selectedTooth else session.odontogramSurfaces
        onSessionChanged(session.copy(
            presentTeeth=present,
            odontogramSurfaces=surfaces,
            teeth=session.teeth+(selectedTooth to record.copy(status=status))
        ))
    }

    ResponsiveScreenV17(
        tr(lang,"Odontograma clínico","Clinical odontogram"),
        tr(lang,"Trabaja por cuadrantes. Los dientes anteriores usan cuatro superficies y los posteriores cinco; cada superficie conserva su propia marca.","Work by quadrants. Anterior teeth use four surfaces and posterior teeth five; each surface keeps its own mark."),
        onBack
    ){profile->
        PracticeSaveControlsV48(lang,"odontogram_v48")
        ResponsiveSectionV17(tr(lang,"Dentición","Dentition")){
            AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                val value=i==1
                FilterChip(primary==value,{primary=value},{Text(if(value)tr(lang,"Temporal","Primary") else tr(lang,"Permanente","Permanent"))},Modifier.fillMaxWidth())
            }
        }

        arches.forEach{arch->
            ResponsiveSectionV17(if(lang=="en")arch.titleEn else arch.titleEs){
                val columns=when{
                    profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT->4
                    else->arch.teeth.size
                }
                AdaptiveGridV17(arch.teeth.size,columns){i->
                    val tooth=arch.teeth[i]
                    val status=session.teeth[tooth]?.status
                    val isMissing=status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
                    val selected=selectedTooth==tooth
                    val hasMark=session.odontogramSurfaces[tooth]?.isNotEmpty()==true
                    Card(
                        onClick={selectedTooth=tooth},
                        modifier=Modifier.fillMaxWidth(),
                        colors=CardDefaults.cardColors(containerColor=when{
                            selected->MaterialTheme.colorScheme.primary
                            isMissing->MaterialTheme.colorScheme.errorContainer
                            hasMark->MaterialTheme.colorScheme.secondaryContainer
                            else->MaterialTheme.colorScheme.surface
                        }),
                        border=BorderStroke(1.dp,if(selected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha=.4f)),
                        shape=RoundedCornerShape(12.dp)
                    ){
                        Column(Modifier.fillMaxWidth().padding(vertical=7.dp),horizontalAlignment=Alignment.CenterHorizontally){
                            Text(if(isMissing)"✕" else "🦷",fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleLarge)
                            Text(tooth.toString(),fontWeight=FontWeight.Black,color=if(selected)MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        ResponsiveSectionV17("OD $selectedTooth",if(missing)tr(lang,"Diente ausente: se muestra una X en su viñeta.","Missing tooth: an X is shown in its tile.") else tr(lang,"Puedes combinar marcas en distintas caras del mismo diente.","You can combine marks on different surfaces of the same tooth.")){
            AdaptiveGridV17(SurfaceMark.entries.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 4){i->
                val mark=SurfaceMark.entries[i]
                FilterChip(selectedMark==mark,{selectedMark=mark},{Text(markLabelV20(mark,lang))},Modifier.fillMaxWidth(),enabled=!missing)
            }

            if(missing){
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer),modifier=Modifier.fillMaxWidth()){
                    Column(Modifier.padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally){
                        Text("✕",style=MaterialTheme.typography.displayMedium,fontWeight=FontWeight.Black)
                        Text(tr(lang,"OD $selectedTooth ausente. Márcalo presente para registrar superficies.","Tooth $selectedTooth is missing. Mark it present to record surfaces."),fontWeight=FontWeight.Bold)
                    }
                }
            }else{
                DentalSurfaceDiagram(
                    centerEnabled=isPosteriorV48(selectedTooth),
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

            AdaptiveGridV17(3,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 3){i->
                val status=when(i){
                    0->ToothStatus.HEALTHY
                    1->ToothStatus.MISSING_CARIES
                    else->ToothStatus.MISSING_OTHER
                }
                val selectedStatus=when(status){
                    ToothStatus.HEALTHY->!missing
                    else->record.status==status
                }
                FilterChip(
                    selected=selectedStatus,
                    onClick={setWholeStatus(status)},
                    label={Text(when(status){
                        ToothStatus.HEALTHY->tr(lang,"🦷 Diente presente","🦷 Present tooth")
                        ToothStatus.MISSING_CARIES->tr(lang,"✕ Perdido por caries","✕ Missing due to caries")
                        else->tr(lang,"✕ Ausente por otra causa","✕ Missing for another reason")
                    })},
                    modifier=Modifier.fillMaxWidth()
                )
            }
        }

        NoticeCard(tr(lang,
            "Ejemplo: un mismo OD posterior puede quedar O y V en caries, M en restauración y D en sellador. Los anteriores sólo habilitan V, L/P, M y D. Al declarar un diente ausente se borran sus marcas para evitar datos contradictorios; registra además si la ausencia fue por caries u otra causa.",
            "Example: the same posterior tooth may have O and B caries, an M restoration and a D sealant. Anterior teeth only enable B, L/P, M and D. Marking a tooth missing clears its surface marks; also record whether it is missing due to caries or another cause."
        ))
        PracticeSaveControlsV48(lang,"odontogram_v48")
    }
}
