package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.CariesToothIndexV48
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

private enum class IndexModeV40 { CPOD, CEOD, CPOS, CEOS }

private fun modeLabelV48(mode:IndexModeV40)=when(mode){
    IndexModeV40.CPOD->"CPOD"
    IndexModeV40.CEOD->"ceod"
    IndexModeV40.CPOS->"CPOS"
    IndexModeV40.CEOS->"ceos"
}

private fun posteriorV40(tooth:Int):Boolean = tooth%10 >=4
private fun eligibleSurfacesV40(tooth:Int):List<Surface> = if(posteriorV40(tooth)) listOf(Surface.VESTIBULAR,Surface.LINGUAL_PALATAL,Surface.MESIAL,Surface.DISTAL,Surface.OCCLUSAL) else listOf(Surface.VESTIBULAR,Surface.LINGUAL_PALATAL,Surface.MESIAL,Surface.DISTAL)

@Composable
fun CpodCeosV40Screen(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onBack:()->Unit){
    var mode by remember{mutableStateOf(IndexModeV40.CPOD)}
    var tooth by remember{mutableStateOf(16)}
    var surfaceMark by remember{mutableStateOf(SurfaceMark.CARIES)}
    var includeThirdMolars by remember{mutableStateOf(true)}
    val teeth=when(mode){
        IndexModeV40.CPOD->CariesToothIndexV48.permanentTeeth(includeThirdMolars)
        IndexModeV40.CPOS->ClinicalContent.permanentTeeth
        else->ClinicalContent.primaryTeeth
    }
    if(tooth !in teeth) tooth=teeth.first()
    val record=session.teeth[tooth]?:ToothRecord()
    val surfaceMap=TeachingStateV40.ceosSurfaces[tooth]?:emptyMap()

    fun setToothStatus(status:ToothStatus){
        val missing=status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
        val present=session.presentTeeth.toMutableSet()
        if(missing) present.remove(tooth) else present.add(tooth)
        val odontogram=if(missing) session.odontogramSurfaces-tooth else session.odontogramSurfaces
        onSessionChanged(session.copy(
            teeth=session.teeth+(tooth to record.copy(status=status)),
            presentTeeth=present,
            odontogramSurfaces=odontogram
        ))
    }
    fun setSurface(s:Surface){
        if(s !in eligibleSurfacesV40(tooth)) return
        // Leer siempre el mapa vivo. Así cada toque parte de lo ya pintado y no reemplaza
        // la cara anterior por un estado capturado en un render previo.
        val map=(TeachingStateV40.ceosSurfaces[tooth]?:emptyMap()).toMutableMap()
        if(map[s]==surfaceMark) map.remove(s) else map[s]=surfaceMark
        TeachingStateV40.ceosSurfaces[tooth]=map
    }
    fun markAllSurfaces(){
        val map=(TeachingStateV40.ceosSurfaces[tooth]?:emptyMap()).toMutableMap()
        eligibleSurfacesV40(tooth).forEach{map[it]=surfaceMark}
        TeachingStateV40.ceosSurfaces[tooth]=map
    }

    fun surfaceCounts(list:List<Int>):Triple<Int,Int,Int>{
        var c=0;var m=0;var f=0
        list.forEach{t->
            val r=session.teeth[t]
            if(r?.status==ToothStatus.MISSING_CARIES){m+=eligibleSurfacesV40(t).size}
            else{
                val mp=TeachingStateV40.ceosSurfaces[t]?:emptyMap()
                eligibleSurfacesV40(t).forEach{s->when(mp[s]){SurfaceMark.CARIES->c++;SurfaceMark.RESTORATION->f++;else->Unit}}
            }
        }
        return Triple(c,m,f)
    }
    val cpod=CariesToothIndexV48.cpod(session.teeth,includeThirdMolars)
    val ceod=CariesToothIndexV48.ceod(session.teeth)
    val cpodC=cpod.carious
    val cpodP=cpod.missing
    val cpodO=cpod.filled
    val ceodC=ceod.carious
    val ceodE=ceod.missing
    val ceodO=ceod.filled
    val(cposC,cposP,cposO)=surfaceCounts(ClinicalContent.permanentTeeth)
    val(ceosC,ceosE,ceosO)=surfaceCounts(ClinicalContent.primaryTeeth)

    ResponsiveScreenV17("CPOD · ceod · CPOS · ceos","Por diente y por superficie son unidades distintas. En superficie puedes marcar varias caras del mismo órgano dentario y cada marca se conserva.",onBack){profile->
        AdaptiveGridV17(4,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 4){i->
            val m=IndexModeV40.entries[i]
            FilterChip(mode==m,{mode=m},{Text(modeLabelV48(m))},Modifier.fillMaxWidth())
        }
        NoticeCard(when(mode){
            IndexModeV40.CPOD->tr(lang,
                "CPOD: C = cariado, P = perdido por caries, O = obturado. Una ausencia por otra causa no suma. Un diente permanente presente con extracción indicada por caries sigue en C hasta que esté perdido.",
                "DMFT: D = decayed, M = missing due to caries, F = filled. Missing for another reason does not count. A present permanent tooth indicated for extraction due to caries remains in D until it is missing."
            )
            IndexModeV40.CEOD->tr(lang,
                "ceod (convención docente): c = cariado, e = extracción indicada por caries, o = obturado. Un temporal ya ausente no se suma automáticamente a e porque puede confundirse con exfoliación fisiológica.",
                "deft (teaching convention): d = decayed, e = extraction indicated due to caries, f = filled. An already absent primary tooth is not automatically counted as e because physiologic exfoliation may be the cause."
            )
            IndexModeV40.CPOS->"CPOS: superficies permanentes como unidad. Incisivos/caninos usan 4 caras; premolares/molares 5. Puedes pintar 1, 2, 3, 4 o 5 caras y conservarlas simultáneamente."
            IndexModeV40.CEOS->"ceos: superficies temporales como unidad. Incisivos/caninos usan 4 caras; molares 5. Puedes pintar varias caras del mismo diente sin borrar las anteriores."
        })
        if(mode==IndexModeV40.CPOD){
            ResponsiveSectionV17(tr(lang,"Protocolo de dientes permanentes","Permanent-tooth scope")){
                Text(tr(lang,
                    "Selecciona el conjunto que exige tu protocolo. La OMS permite calcular DMFT sobre 32 dientes; algunos protocolos docentes/epidemiológicos excluyen terceros molares y trabajan con 28.",
                    "Select the tooth set required by your protocol. WHO permits DMFT calculation over 32 teeth; some teaching/epidemiologic protocols exclude third molars and use 28."
                ))
                AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
                    val include=i==0
                    FilterChip(
                        selected=includeThirdMolars==include,
                        onClick={includeThirdMolars=include},
                        label={Text(if(include)tr(lang,"32 · incluye terceros molares","32 · include third molars") else tr(lang,"28 · excluye terceros molares","28 · exclude third molars"))},
                        modifier=Modifier.fillMaxWidth()
                    )
                }
            }
        }
        if(mode==IndexModeV40.CPOD||mode==IndexModeV40.CEOD){
            PracticeSaveControlsV48(lang,"cpod_ceod_v48")
        }
        ResponsiveSectionV17(tr(lang,"Órgano dentario","Tooth")){
            DentalArchSelector(teeth,tooth,{tooth=it}){t->(session.teeth[t]?.status!=null&&session.teeth[t]?.status!=ToothStatus.HEALTHY)||(TeachingStateV40.ceosSurfaces[t]?.isNotEmpty()==true)}
        }
        if(mode==IndexModeV40.CPOD||mode==IndexModeV40.CEOD){
            ResponsiveSectionV17(tr(lang,"Estado del diente completo","Whole-tooth status")){
                val opts=if(mode==IndexModeV40.CPOD){
                    listOf(
                        ToothStatus.HEALTHY to tr(lang,"Sano · no suma","Sound · does not count"),
                        ToothStatus.CARIES to tr(lang,"Cariado · C","Decayed · D"),
                        ToothStatus.RESTORED to tr(lang,"Obturado · O","Filled · F"),
                        ToothStatus.MISSING_CARIES to tr(lang,"Perdido por caries · P","Missing due to caries · M"),
                        ToothStatus.MISSING_OTHER to tr(lang,"Ausente por otra causa · no suma","Missing for another reason · does not count"),
                        ToothStatus.EXTRACTION_INDICATED to tr(lang,"Extracción indicada por caries · cuenta C","Extraction indicated due to caries · counts D")
                    )
                }else{
                    listOf(
                        ToothStatus.HEALTHY to tr(lang,"Sano · no suma","Sound · does not count"),
                        ToothStatus.CARIES to tr(lang,"Cariado · c","Decayed · d"),
                        ToothStatus.RESTORED to tr(lang,"Obturado · o","Filled · f"),
                        ToothStatus.EXTRACTION_INDICATED to tr(lang,"Extracción indicada por caries · e","Extraction indicated due to caries · e"),
                        ToothStatus.MISSING_CARIES to tr(lang,"Ya ausente · no suma en esta convención ceod","Already absent · does not count in this deft convention"),
                        ToothStatus.MISSING_OTHER to tr(lang,"Exfoliado/ausente otra causa · no suma","Exfoliated/missing other cause · does not count")
                    )
                }
                opts.forEach{(status,label)->
                    FilterChip(record.status==status,{setToothStatus(status)},{Text(label)},Modifier.fillMaxWidth())
                }
                if(mode==IndexModeV40.CEOD){
                    NoticeCard(tr(lang,
                        "En esta app, e significa extracción indicada por caries. Si tu escuela o estudio usa otra definición de ceod/deft, aplica el protocolo institucional y documenta la convención.",
                        "In this app, e means extraction indicated due to caries. If your school or study uses another ceod/deft definition, follow the institutional protocol and document the convention."
                    ))
                }
            }
        } else {
            ResponsiveSectionV17("Marca una, dos, tres, cuatro o cinco superficies"){
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                    FilterChip(surfaceMark==SurfaceMark.CARIES,{surfaceMark=SurfaceMark.CARIES},{Text("Caries")},Modifier.weight(1f))
                    FilterChip(surfaceMark==SurfaceMark.RESTORATION,{surfaceMark=SurfaceMark.RESTORATION},{Text("Obturada")},Modifier.weight(1f))
                    FilterChip(surfaceMark==SurfaceMark.SEALANT,{surfaceMark=SurfaceMark.SEALANT},{Text("Sellador")},Modifier.weight(1f))
                }
                DentalSurfaceDiagram(
                    centerEnabled=posteriorV40(tooth),
                    surfaceColor={s->when(surfaceMap[s]){
                        SurfaceMark.CARIES->MaterialTheme.colorScheme.errorContainer
                        SurfaceMark.RESTORATION->MaterialTheme.colorScheme.primaryContainer
                        SurfaceMark.SEALANT->MaterialTheme.colorScheme.tertiaryContainer
                        else->MaterialTheme.colorScheme.surfaceVariant
                    }},
                    onSurfaceTap={setSurface(it)},
                    modifier=Modifier.fillMaxWidth()
                )
                val selectedSurfaces=eligibleSurfacesV40(tooth).filter{it in surfaceMap}
                Text("Caras marcadas en OD $tooth (${selectedSurfaces.size}): "+if(selectedSurfaces.isEmpty())"ninguna" else selectedSurfaces.joinToString{it.name})
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                    Card(onClick={markAllSurfaces()},modifier=Modifier.weight(1f),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){Text("Marcar todas las caras",Modifier.padding(10.dp),fontWeight=FontWeight.Black)}
                    Card(onClick={TeachingStateV40.ceosSurfaces.remove(tooth)},modifier=Modifier.weight(1f),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){Text("Limpiar diente",Modifier.padding(10.dp),fontWeight=FontWeight.Black)}
                }
            }
        }
        ResponsiveSectionV17("Conteo acumulado"){
            Text("CPOD = C $cpodC + P $cpodP + O $cpodO = ${cpodC+cpodP+cpodO}",fontWeight=FontWeight.Black)
            Text("ceod = c $ceodC + e $ceodE + o $ceodO = ${ceodC+ceodE+ceodO}")
            Text("CPOS = C $cposC + P(superficies) $cposP + O $cposO = ${cposC+cposP+cposO}")
            Text("ceos = c $ceosC + e(superficies) $ceosE + o $ceosO = ${ceosC+ceosE+ceosO}",fontWeight=FontWeight.Black)
            Card(onClick={TeachingStateV40.moduleSummaries["caries"]="CPOD ${cpodC+cpodP+cpodO} (C$cpodC P$cpodP O$cpodO) · CPOS ${cposC+cposP+cposO} · ceod ${ceodC+ceodE+ceodO} · ceos ${ceosC+ceosE+ceosO} (c$ceosC e$ceosE o$ceosO)"},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline)){Text("Guardar resultados CPOD · ceod · CPOS · ceos",Modifier.padding(12.dp),fontWeight=FontWeight.Black)}
        }
        NoticeCard("En epidemiología, CPOD/ceod y CPOS/ceos deben seguir un protocolo definido. La app separa claramente diente vs superficie para que el estudiante no mezcle unidades.")
    }
}