package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R

private data class UploadedClinicalRefV51(val drawable:Int,val titleEs:String,val titleEn:String)

@Composable
private fun UploadedClinicalRefsV51(lang:String, refs:List<UploadedClinicalRefV51>, columns:Int=3) {
    var selectedIndex by remember(refs) { mutableStateOf<Int?>(null) }

    if(columns==2){
        Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(8.dp)){
            refs.chunked(2).forEachIndexed { rowIndex,row ->
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
                    row.forEachIndexed { colIndex,r ->
                        val index=rowIndex*2+colIndex
                        FilterChip(
                            selected=selectedIndex==index,
                            onClick={selectedIndex=index},
                            label={
                                Text(
                                    if(lang=="en") r.titleEn else r.titleEs,
                                    minLines=2,
                                    maxLines=3,
                                    softWrap=true
                                )
                            },
                            modifier=Modifier.weight(1f)
                        )
                    }
                    if(row.size==1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }else{
        ChipChoices(
            refs.mapIndexed { i,r -> (if(lang=="en") r.titleEn else r.titleEs) to (selectedIndex==i) },
            { selectedIndex=it },
            columns=columns
        )
    }

    selectedIndex?.let { i ->
        refs.getOrNull(i)?.let { r ->
            LocalClinicalInlineZoomImageV48(
                lang,
                if(lang=="en") r.titleEn else r.titleEs,
                if(lang=="en") r.titleEn else r.titleEs,
                r.drawable,
                "Imagen clínica local. Úsala junto con los criterios escritos y la exploración; no genera diagnóstico automático.",
                "Local clinical image. Use it with written criteria and examination; it does not generate an automatic diagnosis."
            )
        }
    }
}

@Composable internal fun UploadedCpodRefsV51(lang:String)=UploadedClinicalRefsV51(lang,listOf(
 UploadedClinicalRefV51(R.drawable.uploaded80_032,"CPOD/ceod · sano","DMFT/dmft · sound"),
 UploadedClinicalRefV51(R.drawable.uploaded80_027,"CPOD/ceod · cariado","DMFT/dmft · decayed"),
 UploadedClinicalRefV51(R.drawable.uploaded80_029,"CPOD/ceod · obturado","DMFT/dmft · filled"),
 UploadedClinicalRefV51(R.drawable.uploaded80_030,"CPOD/ceod · perdido por caries","DMFT/dmft · missing due to caries"),
 UploadedClinicalRefV51(R.drawable.uploaded80_031,"CPOD/ceod · ausente por otra causa","DMFT/dmft · missing for another reason"),
 UploadedClinicalRefV51(R.drawable.uploaded80_028,"CPOD/ceod · sellador","DMFT/dmft · sealant")
))

@Composable internal fun UploadedOlearyRefsV51(lang:String)=UploadedClinicalRefsV51(lang,listOf(
 UploadedClinicalRefV51(R.drawable.uploaded80_065,"O’Leary · superficie sin placa","O’Leary · surface without plaque"),
 UploadedClinicalRefV51(R.drawable.uploaded80_063,"O’Leary · placa vestibular","O’Leary · buccal plaque"),
 UploadedClinicalRefV51(R.drawable.uploaded80_061,"O’Leary · placa lingual/palatina","O’Leary · lingual/palatal plaque"),
 UploadedClinicalRefV51(R.drawable.uploaded80_062,"O’Leary · placa mesial","O’Leary · mesial plaque"),
 UploadedClinicalRefV51(R.drawable.uploaded80_060,"O’Leary · placa distal","O’Leary · distal plaque"),
 UploadedClinicalRefV51(R.drawable.uploaded80_066,"O’Leary · tinción reveladora","O’Leary · plaque disclosing"),
 UploadedClinicalRefV51(R.drawable.uploaded80_064,"O’Leary · registro positivo y negativo","O’Leary · positive and negative record")
))

@Composable internal fun UploadedIpcRefsV51(lang:String)=UploadedClinicalRefsV51(lang,listOf(
 UploadedClinicalRefV51(R.drawable.uploaded80_048,"IPC 0 · sin hallazgo indexado","CPI 0 · no indexed finding"),
 UploadedClinicalRefV51(R.drawable.uploaded80_079,"IPC 1 · sangrado después del sondaje","CPI 1 · bleeding after probing"),
 UploadedClinicalRefV51(R.drawable.uploaded80_049,"IPC 2 · cálculo/factor retentivo","CPI 2 · calculus/retentive factor"),
 UploadedClinicalRefV51(R.drawable.uploaded80_050,"IPC 3 · bolsa 4–5 mm","CPI 3 · 4–5 mm pocket"),
 UploadedClinicalRefV51(R.drawable.uploaded80_051,"IPC 4 · bolsa ≥6 mm","CPI 4 · ≥6 mm pocket"),
 UploadedClinicalRefV51(R.drawable.uploaded80_052,"IPC X · no evaluable","CPI X · not evaluable"),
 UploadedClinicalRefV51(R.drawable.uploaded80_053,"IPC · técnica de sondaje con sonda OMS","CPI · WHO probe technique")
))

@Composable internal fun UploadedIhosRefsV51(lang:String)=UploadedClinicalRefsV51(lang,listOf(
 UploadedClinicalRefV51(R.drawable.uploaded80_069,"IHOS · superficies índice","OHI-S · index surfaces"),
 UploadedClinicalRefV51(R.drawable.uploaded80_075,"IHOS detritos · código 0","OHI-S debris · code 0"),
 UploadedClinicalRefV51(R.drawable.uploaded80_076,"IHOS detritos · código 1","OHI-S debris · code 1"),
 UploadedClinicalRefV51(R.drawable.uploaded80_077,"IHOS detritos · código 2","OHI-S debris · code 2"),
 UploadedClinicalRefV51(R.drawable.uploaded80_078,"IHOS detritos · código 3","OHI-S debris · code 3"),
 UploadedClinicalRefV51(R.drawable.uploaded80_071,"IHOS cálculo · código 0","OHI-S calculus · code 0"),
 UploadedClinicalRefV51(R.drawable.uploaded80_072,"IHOS cálculo · código 1","OHI-S calculus · code 1"),
 UploadedClinicalRefV51(R.drawable.uploaded80_073,"IHOS cálculo · código 2","OHI-S calculus · code 2"),
 UploadedClinicalRefV51(R.drawable.uploaded80_074,"IHOS cálculo · código 3","OHI-S calculus · code 3")
))

@Composable internal fun UploadedPulpalRefsV51(lang:String){
 val tests=listOf(
  UploadedClinicalRefV51(R.drawable.uploaded80_013,"Prueba de frío","Cold test"),
  UploadedClinicalRefV51(R.drawable.uploaded80_012,"Prueba de calor","Heat test"),
  UploadedClinicalRefV51(R.drawable.uploaded80_015,"Prueba eléctrica pulpar","Electric pulp test"),
  UploadedClinicalRefV51(R.drawable.uploaded80_010,"Percusión vertical","Vertical percussion"),
  UploadedClinicalRefV51(R.drawable.uploaded80_009,"Percusión horizontal","Horizontal percussion"),
  UploadedClinicalRefV51(R.drawable.uploaded80_008,"Palpación apical","Apical palpation"),
  UploadedClinicalRefV51(R.drawable.uploaded80_014,"Prueba de mordida","Bite test"),
  UploadedClinicalRefV51(R.drawable.uploaded80_007,"Movilidad dental","Tooth mobility"),
  UploadedClinicalRefV51(R.drawable.uploaded80_022,"Sondaje periodontal localizado","Localized periodontal probing"),
  UploadedClinicalRefV51(R.drawable.uploaded80_023,"Transiluminación por sospecha de fisura","Transillumination for suspected crack")
 )
 val pulpal=listOf(
  UploadedClinicalRefV51(R.drawable.uploaded80_016,"Pulpa clínicamente normal","Clinically normal pulp"),
  UploadedClinicalRefV51(R.drawable.uploaded80_020,"Respuesta aumentada al frío","Increased cold response"),
  UploadedClinicalRefV51(R.drawable.uploaded80_021,"Respuesta persistente al frío","Lingering cold response"),
  UploadedClinicalRefV51(R.drawable.uploaded80_002,"Ausencia de respuesta","No response")
 )
 val apical=listOf(
  UploadedClinicalRefV51(R.drawable.uploaded80_042,"Dolor a la percusión","Percussion pain"),
  UploadedClinicalRefV51(R.drawable.uploaded80_003,"Dolor a la palpación","Palpation pain"),
  UploadedClinicalRefV51(R.drawable.uploaded80_005,"Fístula","Sinus tract"),
  UploadedClinicalRefV51(R.drawable.uploaded80_006,"Inflamación/absceso localizado","Localized swelling/abscess"),
  UploadedClinicalRefV51(R.drawable.uploaded80_004,"Ensanchamiento del ligamento periodontal","Widened periodontal ligament"),
  UploadedClinicalRefV51(R.drawable.uploaded80_017,"Radiolucidez periapical","Periapical radiolucency"),
  UploadedClinicalRefV51(R.drawable.uploaded80_011,"Pérdida de lámina dura","Loss of lamina dura"),
  UploadedClinicalRefV51(R.drawable.uploaded80_019,"Reabsorción radicular interna","Internal root resorption"),
  UploadedClinicalRefV51(R.drawable.uploaded80_018,"Reabsorción radicular externa","External root resorption")
 )
 androidx.compose.material3.Text(if(lang=="en") "Tests" else "Pruebas",fontWeight=androidx.compose.ui.text.font.FontWeight.Bold)
 UploadedClinicalRefsV51(lang,tests,columns=2)
 androidx.compose.material3.Text(if(lang=="en") "Pulpal responses" else "Respuestas pulpares",fontWeight=androidx.compose.ui.text.font.FontWeight.Bold)
 UploadedClinicalRefsV51(lang,pulpal,columns=2)
 androidx.compose.material3.Text(if(lang=="en") "Periapical findings" else "Hallazgos periapicales",fontWeight=androidx.compose.ui.text.font.FontWeight.Bold)
 UploadedClinicalRefsV51(lang,apical,columns=2)
}

@Composable internal fun UploadedPeriodontalRefsV51(lang:String)=UploadedClinicalRefsV51(lang,listOf(
 UploadedClinicalRefV51(R.drawable.uploaded80_043,"Encía clínicamente sana","Clinically healthy gingiva"),
 UploadedClinicalRefV51(R.drawable.uploaded80_079,"Sangrado después del sondaje","Bleeding after probing"),
 UploadedClinicalRefV51(R.drawable.uploaded80_041,"Cálculo supragingival","Supragingival calculus"),
 UploadedClinicalRefV51(R.drawable.uploaded80_040,"Cálculo subgingival","Subgingival calculus"),
 UploadedClinicalRefV51(R.drawable.uploaded80_026,"Bolsa periodontal","Periodontal pocket"),
 UploadedClinicalRefV51(R.drawable.uploaded80_067,"Pérdida de inserción clínica","Clinical attachment loss"),
 UploadedClinicalRefV51(R.drawable.uploaded80_045,"Furcación","Furcation"),
 UploadedClinicalRefV51(R.drawable.uploaded80_058,"Movilidad dental grados I, II y III","Tooth mobility grades I, II and III")
),columns=2)

@Composable internal fun UploadedCariesRefsV51(lang:String)=UploadedClinicalRefsV51(lang,listOf(
 UploadedClinicalRefV51(R.drawable.uploaded80_055,"Lesión de mancha blanca activa","Active white-spot lesion"),
 UploadedClinicalRefV51(R.drawable.uploaded80_056,"Lesión de mancha blanca inactiva","Inactive white-spot lesion"),
 UploadedClinicalRefV51(R.drawable.edu_caries_inactiva,"Caries no cavitada","Non-cavitated caries"),
 UploadedClinicalRefV51(R.drawable.uploaded80_033,"Caries cavitada","Cavitated caries"),
 UploadedClinicalRefV51(R.drawable.uploaded80_035,"Caries activa","Active caries"),
 UploadedClinicalRefV51(R.drawable.edu_caries_inactiva,"Caries inactiva","Inactive caries"),
 UploadedClinicalRefV51(R.drawable.uploaded80_036,"Caries coronaria","Coronal caries"),
 UploadedClinicalRefV51(R.drawable.uploaded80_038,"Caries radicular","Root caries"),
 UploadedClinicalRefV51(R.drawable.uploaded80_057,"Mancha blanca temporal por deshidratación","Temporary dehydration white spot"),
 UploadedClinicalRefV51(R.drawable.uploaded80_047,"Hipoplasia del esmalte","Enamel hypoplasia"),
 UploadedClinicalRefV51(R.drawable.uploaded80_046,"Hipomineralización","Hypomineralization"),
 UploadedClinicalRefV51(R.drawable.uploaded80_044,"Fluorosis","Fluorosis"),
 UploadedClinicalRefV51(R.drawable.uploaded80_059,"Opacidad demarcada tipo MIH","MIH-type demarcated opacity"),
 UploadedClinicalRefV51(R.drawable.uploaded80_001,"Amelogénesis imperfecta","Amelogenesis imperfecta"),
 UploadedClinicalRefV51(R.drawable.uploaded80_054,"Lesión de Turner / alteración localizada","Turner lesion / localized alteration")
))

@Composable internal fun UploadedOrthoApplianceRefsV51(lang:String)=UploadedClinicalRefsV51(lang,listOf(
 UploadedClinicalRefV51(R.drawable.uploaded80_024,"Arco lingual","Lingual arch"),
 UploadedClinicalRefV51(R.drawable.uploaded80_025,"Arco transpalatino","Transpalatal arch"),
 UploadedClinicalRefV51(R.drawable.uploaded80_070,"Zapatilla distal","Distal shoe"),
 UploadedClinicalRefV51(R.drawable.uploaded80_039,"Corona y ansa","Crown and loop"),
 UploadedClinicalRefV51(R.drawable.uploaded80_068,"Recuperador de espacio","Space regainer")
))
