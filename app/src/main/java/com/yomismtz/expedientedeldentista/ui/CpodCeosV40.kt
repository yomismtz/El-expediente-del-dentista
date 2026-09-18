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
import com.yomismtz.expedientedeldentista.clinical.CariesSurfaceIndexV48
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
        IndexModeV40.CPOS->CariesToothIndexV48.permanentTeeth(includeThirdMolars)
        else->ClinicalContent.primaryTeeth
    }
    if(tooth !in teeth) tooth=teeth.first()
    val record=session.teeth[tooth]?:ToothRecord()
    val surfaceMap=TeachingStateV40.ceosSurfaces[tooth]?:emptyMap()
    val surfaceLocked=when(mode){
        IndexModeV40.CPOS->record.status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
        IndexModeV40.CEOS->record.status in setOf(ToothStatus.EXTRACTION_INDICATED,ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
        else->false
    }

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
        if(surfaceLocked || s !in eligibleSurfacesV40(tooth)) return
        // Leer siempre el mapa vivo. Así cada toque parte de lo ya pintado y no reemplaza
        // la cara anterior por un estado capturado en un render previo.
        val map=(TeachingStateV40.ceosSurfaces[tooth]?:emptyMap()).toMutableMap()
        if(map[s]==surfaceMark) map.remove(s) else map[s]=surfaceMark
        TeachingStateV40.ceosSurfaces[tooth]=map
    }
    fun markAllSurfaces(){
        if(surfaceLocked) return
        val map=(TeachingStateV40.ceosSurfaces[tooth]?:emptyMap()).toMutableMap()
        eligibleSurfacesV40(tooth).forEach{map[it]=surfaceMark}
        TeachingStateV40.ceosSurfaces[tooth]=map
    }

    val cpod=CariesToothIndexV48.cpod(session.teeth,includeThirdMolars)
    val ceod=CariesToothIndexV48.ceod(session.teeth)
    val cpodC=cpod.carious
    val cpodP=cpod.missing
    val cpodO=cpod.filled
    val ceodC=ceod.carious
    val ceodE=ceod.missing
    val ceodO=ceod.filled
    val cpos=CariesSurfaceIndexV48.cpos(session.teeth,TeachingStateV40.ceosSurfaces,includeThirdMolars)
    val ceos=CariesSurfaceIndexV48.ceos(session.teeth,TeachingStateV40.ceosSurfaces)
    val cposC=cpos.carious
    val cposP=cpos.missing
    val cposO=cpos.filled
    val ceosC=ceos.carious
    val ceosE=ceos.missing
    val ceosO=ceos.filled

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
            IndexModeV40.CPOS->tr(lang,
                "CPOS: cada superficie es una unidad. Anteriores = 4 superficies; posteriores = 5. Las superficies cariadas y obturadas se marcan individualmente. Un permanente perdido por caries aporta todas sus superficies elegibles a P.",
                "DMFS: each surface is one unit. Anterior teeth = 4 surfaces; posterior teeth = 5. Decayed and filled surfaces are marked individually. A permanent tooth missing due to caries contributes all eligible surfaces to M."
            )
            IndexModeV40.CEOS->tr(lang,
                "ceos (convención docente): cada superficie es una unidad. Las superficies cariadas y obturadas se marcan individualmente; un temporal con extracción indicada por caries aporta sus 4 o 5 superficies a e.",
                "defs (teaching convention): each surface is one unit. Decayed and filled surfaces are marked individually; a primary tooth indicated for extraction due to caries contributes its 4 or 5 surfaces to e."
            )
        })
        if(mode==IndexModeV40.CPOD||mode==IndexModeV40.CPOS){
            ResponsiveSectionV17(tr(lang,"Protocolo de dientes permanentes","Permanent-tooth scope")){
                Text(tr(lang,
                    "Selecciona el conjunto que exige tu protocolo para CPOD/CPOS. La configuración de 32 incluye terceros molares; la de 28 los excluye.",
                    "Select the tooth set required by your DMFT/DMFS protocol. The 32-tooth setting includes third molars; the 28-tooth setting excludes them."
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
        }else{
            PracticeSaveControlsV48(lang,"cpos_ceos_v48")
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
            ResponsiveSectionV17(tr(lang,"Condición del diente para el índice","Tooth condition for the index")){
                if(mode==IndexModeV40.CPOS){
                    val missingCaries=record.status==ToothStatus.MISSING_CARIES
                    val missingOther=record.status==ToothStatus.MISSING_OTHER
                    AdaptiveGridV17(3,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 3){i->
                        when(i){
                            0->FilterChip(
                                selected=!missingCaries&&!missingOther,
                                onClick={if(missingCaries||missingOther)setToothStatus(ToothStatus.HEALTHY)},
                                label={Text(tr(lang,"Presente · registrar superficies","Present · record surfaces"))},
                                modifier=Modifier.fillMaxWidth()
                            )
                            1->FilterChip(
                                selected=missingCaries,
                                onClick={
                                    TeachingStateV40.ceosSurfaces.remove(tooth)
                                    setToothStatus(ToothStatus.MISSING_CARIES)
                                },
                                label={Text(tr(lang,"Perdido por caries · P","Missing due to caries · M"))},
                                modifier=Modifier.fillMaxWidth()
                            )
                            else->FilterChip(
                                selected=missingOther,
                                onClick={
                                    TeachingStateV40.ceosSurfaces.remove(tooth)
                                    setToothStatus(ToothStatus.MISSING_OTHER)
                                },
                                label={Text(tr(lang,"Ausente otra causa · no suma","Missing other reason · no count"))},
                                modifier=Modifier.fillMaxWidth()
                            )
                        }
                    }
                    if(missingCaries){
                        Text(tr(lang,
                            "Este OD aporta ${eligibleSurfacesV40(tooth).size} superficies a P y no admite marcas individuales mientras esté ausente.",
                            "This tooth contributes ${eligibleSurfacesV40(tooth).size} surfaces to M and does not accept individual surface marks while missing."
                        ),fontWeight=FontWeight.Bold)
                    }
                }else{
                    val extraction=record.status==ToothStatus.EXTRACTION_INDICATED
                    val absent=record.status in setOf(ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER)
                    AdaptiveGridV17(3,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 3){i->
                        when(i){
                            0->FilterChip(
                                selected=!extraction&&!absent,
                                onClick={if(extraction||absent)setToothStatus(ToothStatus.HEALTHY)},
                                label={Text(tr(lang,"Presente · registrar superficies","Present · record surfaces"))},
                                modifier=Modifier.fillMaxWidth()
                            )
                            1->FilterChip(
                                selected=extraction,
                                onClick={
                                    TeachingStateV40.ceosSurfaces.remove(tooth)
                                    setToothStatus(ToothStatus.EXTRACTION_INDICATED)
                                },
                                label={Text(tr(lang,"Extracción indicada · e","Extraction indicated · e"))},
                                modifier=Modifier.fillMaxWidth()
                            )
                            else->FilterChip(
                                selected=absent,
                                onClick={
                                    TeachingStateV40.ceosSurfaces.remove(tooth)
                                    setToothStatus(ToothStatus.MISSING_OTHER)
                                },
                                label={Text(tr(lang,"Ya ausente/exfoliado · no suma","Already absent/exfoliated · no count"))},
                                modifier=Modifier.fillMaxWidth()
                            )
                        }
                    }
                    if(extraction){
                        Text(tr(lang,
                            "Este OD aporta ${eligibleSurfacesV40(tooth).size} superficies a e en la convención docente usada por la app.",
                            "This tooth contributes ${eligibleSurfacesV40(tooth).size} surfaces to e under the teaching convention used by the app."
                        ),fontWeight=FontWeight.Bold)
                    }
                }
            }

            ResponsiveSectionV17(tr(lang,"Marca superficies individualmente","Mark individual surfaces")){
                if(surfaceLocked){
                    NoticeCard(tr(lang,
                        "Las superficies individuales están bloqueadas porque la condición del diente ya determina su aporte al índice. Cámbialo a Presente para pintar superficies.",
                        "Individual surfaces are locked because the tooth condition already determines its index contribution. Change it to Present to mark surfaces."
                    ))
                }else{
                    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                        FilterChip(surfaceMark==SurfaceMark.CARIES,{surfaceMark=SurfaceMark.CARIES},{Text(tr(lang,"Caries","Caries"))},Modifier.weight(1f))
                        FilterChip(surfaceMark==SurfaceMark.RESTORATION,{surfaceMark=SurfaceMark.RESTORATION},{Text(tr(lang,"Obturada","Filled"))},Modifier.weight(1f))
                        FilterChip(surfaceMark==SurfaceMark.SEALANT,{surfaceMark=SurfaceMark.SEALANT},{Text(tr(lang,"Sellador","Sealant"))},Modifier.weight(1f))
                    }
                    DentalSurfaceDiagram(
                        centerEnabled=posteriorV40(tooth),
                        surfaceColor={surface->when(surfaceMap[surface]){
                            SurfaceMark.CARIES->MaterialTheme.colorScheme.errorContainer
                            SurfaceMark.RESTORATION->MaterialTheme.colorScheme.primaryContainer
                            SurfaceMark.SEALANT->MaterialTheme.colorScheme.tertiaryContainer
                            else->MaterialTheme.colorScheme.surfaceVariant
                        }},
                        onSurfaceTap={setSurface(it)},
                        modifier=Modifier.fillMaxWidth()
                    )
                    val selectedSurfaces=eligibleSurfacesV40(tooth).filter{it in surfaceMap}
                    Text(tr(lang,
                        "Caras marcadas en OD $tooth (${selectedSurfaces.size}): "+if(selectedSurfaces.isEmpty())"ninguna" else selectedSurfaces.joinToString{it.name},
                        "Marked surfaces on tooth $tooth (${selectedSurfaces.size}): "+if(selectedSurfaces.isEmpty())"none" else selectedSurfaces.joinToString{it.name}
                    ))
                    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                        Card(onClick={markAllSurfaces()},modifier=Modifier.weight(1f),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){
                            Text(tr(lang,"Marcar todas","Mark all"),Modifier.padding(10.dp),fontWeight=FontWeight.Black)
                        }
                        Card(onClick={TeachingStateV40.ceosSurfaces.remove(tooth)},modifier=Modifier.weight(1f),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){
                            Text(tr(lang,"Limpiar diente","Clear tooth"),Modifier.padding(10.dp),fontWeight=FontWeight.Black)
                        }
                    }
                    NoticeCard(tr(lang,
                        "Un estado por diente no se convierte automáticamente en varias superficies. En CPOS/ceos debes marcar las caras realmente afectadas; así se conservan varias superficies del mismo OD sin reemplazar las anteriores.",
                        "A whole-tooth status is not automatically expanded into multiple surfaces. In DMFS/defs, mark the surfaces actually affected; multiple surfaces on the same tooth are preserved without replacing previous marks."
                    ))
                }
            }
        }
        ResponsiveSectionV17(tr(lang,"Conteo acumulado","Accumulated count")){
            Text("CPOD = C $cpodC + P $cpodP + O $cpodO = ${cpod.total}",fontWeight=FontWeight.Black)
            Text("ceod = c $ceodC + e $ceodE + o $ceodO = ${ceod.total}",fontWeight=FontWeight.Black)
            Text("CPOS = C $cposC + P(superficies) $cposP + O $cposO = ${cpos.total}",fontWeight=if(mode==IndexModeV40.CPOS)FontWeight.Black else FontWeight.Normal)
            Text("ceos = c $ceosC + e(superficies) $ceosE + o $ceosO = ${ceos.total}",fontWeight=if(mode==IndexModeV40.CEOS)FontWeight.Black else FontWeight.Normal)
            if(mode==IndexModeV40.CPOD||mode==IndexModeV40.CEOD){
                Card(
                    onClick={
                        val scope=if(includeThirdMolars)"32" else "28"
                        TeachingStateV40.moduleSummaries["caries_tooth"]=
                            "CPOD ${cpod.total} (C$cpodC P$cpodP O$cpodO; $scope dientes) · ceod ${ceod.total} (c$ceodC e$ceodE o$ceodO)"
                        TeachingStateV40.savedPracticeSections["cpod_ceod_v48"]=true
                    },
                    modifier=Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),
                    border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline)
                ){
                    Text(tr(lang,"Guardar resultados CPOD · ceod","Save DMFT · deft results"),Modifier.padding(12.dp),fontWeight=FontWeight.Black)
                }
            }else{
                Card(
                    onClick={
                        val scope=if(includeThirdMolars)"32" else "28"
                        TeachingStateV40.moduleSummaries["caries_surface"]=
                            "CPOS ${cpos.total} (C$cposC P$cposP O$cposO; $scope dientes) · ceos ${ceos.total} (c$ceosC e$ceosE o$ceosO)"
                        TeachingStateV40.savedPracticeSections["cpos_ceos_v48"]=true
                    },
                    modifier=Modifier.fillMaxWidth(),
                    colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),
                    border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline)
                ){
                    Text(tr(lang,"Guardar resultados CPOS · ceos","Save DMFS · defs results"),Modifier.padding(12.dp),fontWeight=FontWeight.Black)
                }
            }
        }
        NoticeCard(tr(lang,
            "Regla de 5.3: CPOS/ceos cuentan superficies, no dientes. Un anterior aporta hasta 4 superficies y un posterior hasta 5. Selladores no incrementan el índice. CPOS cuenta como P todas las superficies de un permanente perdido por caries; ceos usa e para las superficies de un temporal con extracción indicada, según la convención docente seleccionada.",
            "Step 5.3 rule: DMFS/defs count surfaces, not teeth. An anterior tooth contributes up to 4 surfaces and a posterior tooth up to 5. Sealants do not increase the index. DMFS assigns all surfaces of a permanent tooth missing due to caries to M; defs uses e for the surfaces of a primary tooth indicated for extraction, under the selected teaching convention."
        ))
        if(mode==IndexModeV40.CPOD||mode==IndexModeV40.CEOD){
            PracticeSaveControlsV48(lang,"cpod_ceod_v48")
        }else{
            PracticeSaveControlsV48(lang,"cpos_ceos_v48")
        }
    }
}