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
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

private enum class IndexModeV40 { CPOD, CEOD, CPOS, CEOS }

private fun posteriorV40(tooth:Int):Boolean = tooth%10 >=4
private fun eligibleSurfacesV40(tooth:Int):List<Surface> = if(posteriorV40(tooth)) listOf(Surface.VESTIBULAR,Surface.LINGUAL_PALATAL,Surface.MESIAL,Surface.DISTAL,Surface.OCCLUSAL) else listOf(Surface.VESTIBULAR,Surface.LINGUAL_PALATAL,Surface.MESIAL,Surface.DISTAL)

@Composable
fun CpodCeosV40Screen(lang:String,session:EducationalSession,onSessionChanged:(EducationalSession)->Unit,onBack:()->Unit){
    var mode by remember{mutableStateOf(IndexModeV40.CPOD)}
    var tooth by remember{mutableStateOf(16)}
    var surfaceMark by remember{mutableStateOf(SurfaceMark.CARIES)}
    val teeth=when(mode){IndexModeV40.CPOD,IndexModeV40.CPOS->ClinicalContent.permanentTeeth;else->ClinicalContent.primaryTeeth}
    if(tooth !in teeth) tooth=teeth.first()
    val record=session.teeth[tooth]?:ToothRecord()
    val surfaceMap=TeachingStateV40.ceosSurfaces[tooth]?:emptyMap()

    fun setToothStatus(status:ToothStatus){onSessionChanged(session.copy(teeth=session.teeth+(tooth to record.copy(status=status)),presentTeeth=session.presentTeeth+tooth))}
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

    fun toothCounts(list:List<Int>):Triple<Int,Int,Int>{
        var c=0;var m=0;var f=0
        list.forEach{t->when(session.teeth[t]?.status){ToothStatus.CARIES,ToothStatus.EXTRACTION_INDICATED->c++;ToothStatus.MISSING_CARIES->m++;ToothStatus.RESTORED->f++;else->Unit}}
        return Triple(c,m,f)
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
    val(cpodC,cpodP,cpodO)=toothCounts(ClinicalContent.permanentTeeth)
    val(ceodC,ceodE,ceodO)=toothCounts(ClinicalContent.primaryTeeth)
    val(cposC,cposP,cposO)=surfaceCounts(ClinicalContent.permanentTeeth)
    val(ceosC,ceosE,ceosO)=surfaceCounts(ClinicalContent.primaryTeeth)

    ResponsiveScreenV17("CPOD · ceod · CPOS · ceos","Por diente y por superficie son unidades distintas. En superficie puedes marcar varias caras del mismo órgano dentario y cada marca se conserva.",onBack){profile->
        AdaptiveGridV17(4,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 4){i->val m=IndexModeV40.entries[i];FilterChip(mode==m,{mode=m},{Text(m.name)},Modifier.fillMaxWidth())}
        NoticeCard(when(mode){
            IndexModeV40.CPOD->"CPOD: diente permanente como unidad. C = cariado, P = perdido por caries, O = obturado."
            IndexModeV40.CEOD->"ceod: diente temporal como unidad. c = cariado, e = extracción indicada/perdido por caries según el protocolo docente, o = obturado."
            IndexModeV40.CPOS->"CPOS: superficies permanentes como unidad. Incisivos/caninos usan 4 caras; premolares/molares 5. Puedes pintar 1, 2, 3, 4 o 5 caras y conservarlas simultáneamente."
            IndexModeV40.CEOS->"ceos: superficies temporales como unidad. Incisivos/caninos usan 4 caras; molares 5. Puedes pintar varias caras del mismo diente sin borrar las anteriores."
        })
        ResponsiveSectionV17("Órgano dentario"){
            DentalArchSelector(teeth,tooth,{tooth=it}){t->(session.teeth[t]?.status!=null&&session.teeth[t]?.status!=ToothStatus.HEALTHY)||(TeachingStateV40.ceosSurfaces[t]?.isNotEmpty()==true)}
        }
        if(mode==IndexModeV40.CPOD||mode==IndexModeV40.CEOD){
            ResponsiveSectionV17("Estado del diente completo"){
                val opts=listOf(ToothStatus.HEALTHY to "Sano",ToothStatus.CARIES to "Cariado",ToothStatus.RESTORED to "Obturado",ToothStatus.MISSING_CARIES to "Perdido por caries",ToothStatus.EXTRACTION_INDICATED to "Extracción indicada")
                opts.forEach{(s,label)->FilterChip(record.status==s,{setToothStatus(s)},{Text(label)},Modifier.fillMaxWidth())}
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