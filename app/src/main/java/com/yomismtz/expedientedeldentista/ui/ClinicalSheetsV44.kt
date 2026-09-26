package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
private fun PersistTextV44(key:String,label:String){
    var value by rememberRecordState(key,"")
    OutlinedTextField(value,{value=it},Modifier.fillMaxWidth(),label={Text(label)})
}
@Composable
private fun PersistChoiceV44(key:String,options:List<String>,columns:Int){
    var value by rememberRecordState(key,"")
    AdaptiveGridV17(options.size,columns){i->FilterChip(value==options[i],{value=options[i]},{Text(options[i])},Modifier.fillMaxWidth())}
}

@Composable
fun PeriodontalFollowupV44(lang:String,onBack:()->Unit){
    ResponsiveScreenV17("📈 "+tr(lang,"Ficha de Seguimiento Periodontal","Periodontal Follow-up Sheet"),
        tr(lang,"Compara hallazgos obtenidos entre controles. Complementa el periodontograma existente y no modifica sus índices.","Compares obtained findings across visits. It complements the existing periodontal chart and does not modify its indices."),onBack){p->
        val cols=if(p.largeSystemText)1 else if(p.width==ScreenWidthV17.COMPACT)2 else 3
        ResponsiveSectionV17(tr(lang,"Control","Follow-up"),tr(lang,"Registra fecha o fase, motivo de reevaluación y respuesta observada.","Record date or phase, reason for reassessment and observed response.")){
            AdaptiveGridV17(3,cols){i->when(i){0->PersistTextV44("perioFollow.phase",tr(lang,"Fecha / fase","Date / phase"));1->PersistTextV44("perioFollow.reason",tr(lang,"Motivo del control","Reason for follow-up"));else->PersistTextV44("perioFollow.interval",tr(lang,"Intervalo desde última cita","Interval since last visit"))}}
        }
        ResponsiveSectionV17(tr(lang,"Hallazgos comparables","Comparable findings"),tr(lang,"Anota sólo mediciones realmente obtenidas. El cambio debe interpretarse junto con sitios, técnica de medición y contexto clínico.","Record only measurements actually obtained. Change must be interpreted with sites, measurement technique and clinical context.")){
            AdaptiveGridV17(6,cols){i->when(i){0->PersistTextV44("perioFollow.pd",tr(lang,"Profundidad de sondaje / cambios","Probing depth / changes"));1->PersistTextV44("perioFollow.bop",tr(lang,"Sangrado al sondaje","Bleeding on probing"));2->PersistTextV44("perioFollow.plaque",tr(lang,"Placa / higiene","Plaque / hygiene"));3->PersistTextV44("perioFollow.recession",tr(lang,"Recesión / margen gingival","Recession / gingival margin"));4->PersistTextV44("perioFollow.mobility",tr(lang,"Movilidad / furcación","Mobility / furcation"));else->PersistTextV44("perioFollow.tissues",tr(lang,"Tejidos / supuración","Tissues / suppuration"))}}
        }
        ResponsiveSectionV17(tr(lang,"Conducta y mantenimiento","Management and maintenance"),tr(lang,"Documenta lo realizado y el siguiente control; no asigna automáticamente estadio, grado ni pronóstico.","Document performed care and next review; it does not automatically assign stage, grade or prognosis.")){
            PersistTextV44("perioFollow.care",tr(lang,"Procedimiento / educación / control de factores","Procedure / education / factor control"))
            PersistTextV44("perioFollow.plan",tr(lang,"Plan de mantenimiento / reevaluación","Maintenance / reassessment plan"))
        }
    }
}

@Composable
fun ImplantologySheetV44(lang:String,onBack:()->Unit){
    ResponsiveScreenV17("🦷 "+tr(lang,"Ficha de Implantología","Implantology Sheet"),
        tr(lang,"Hoja educativa para documentar evaluación, planificación, procedimiento y seguimiento implantológico. No calcula por sí sola indicación, dimensiones ni posición del implante.","Educational sheet for implant assessment, planning, procedure and follow-up. It does not independently determine indication, implant dimensions or position."),onBack){p->
        val cols=if(p.largeSystemText)1 else if(p.width==ScreenWidthV17.COMPACT)2 else 3
        ResponsiveSectionV17(tr(lang,"Evaluación preoperatoria","Preoperative assessment"),tr(lang,"Integra historia médica, periodontal, sitio edéntulo y auxiliares pertinentes antes de decidir tratamiento.","Integrate medical and periodontal history, edentulous site and relevant diagnostic aids before deciding treatment.")){
            AdaptiveGridV17(6,cols){i->when(i){0->PersistTextV44("implant.site",tr(lang,"Sitio / OD ausente","Site / missing tooth"));1->PersistTextV44("implant.indication",tr(lang,"Indicación protésico-quirúrgica","Prosthetic-surgical indication"));2->PersistTextV44("implant.systemic",tr(lang,"Riesgos sistémicos / medicamentos","Systemic risks / medications"));3->PersistTextV44("implant.perio",tr(lang,"Estado periodontal e higiene","Periodontal status and hygiene"));4->PersistTextV44("implant.bone",tr(lang,"Evaluación ósea / tejidos","Bone / tissue assessment"));else->PersistTextV44("implant.imaging",tr(lang,"Imagenología / planificación","Imaging / planning"))}}
        }
        ResponsiveSectionV17(tr(lang,"Plan y procedimiento","Plan and procedure"),tr(lang,"Las dimensiones y componentes deben corresponder a la planificación clínica, imagenológica y al sistema realmente utilizado.","Dimensions and components must match clinical/imaging planning and the system actually used.")){
            AdaptiveGridV17(6,cols){i->when(i){0->PersistTextV44("implant.brand",tr(lang,"Sistema / fabricante","System / manufacturer"));1->PersistTextV44("implant.size",tr(lang,"Diámetro × longitud","Diameter × length"));2->PersistTextV44("implant.position",tr(lang,"Posición / angulación documentada","Documented position / angulation"));3->PersistTextV44("implant.graft",tr(lang,"Injerto / regeneración si aplica","Graft / regeneration if applicable"));4->PersistTextV44("implant.torque",tr(lang,"Torque / estabilidad registrados","Recorded torque / stability"));else->PersistTextV44("implant.procedure",tr(lang,"Procedimiento realizado","Procedure performed"))}}
        }
        ResponsiveSectionV17(tr(lang,"Seguimiento","Follow-up"),tr(lang,"Describe tejidos periimplantarios, síntomas, higiene, restauración y controles realmente evaluados.","Describe peri-implant tissues, symptoms, hygiene, restoration and controls actually assessed.")){
            PersistTextV44("implant.followup",tr(lang,"Evolución clínica / radiográfica","Clinical / radiographic evolution"))
            PersistTextV44("implant.plan",tr(lang,"Mantenimiento y siguiente control","Maintenance and next review"))
        }
        NoticeCard(tr(lang,"No interpretar un único valor de torque, sondaje o imagen como diagnóstico aislado. Correlaciona con el caso completo y protocolos vigentes.","Do not interpret a single torque, probing or imaging value as an isolated diagnosis. Correlate with the full case and current protocols."))
    }
}

@Composable
fun DentomaxillaryOrthopedicsV44(lang:String,onBack:()->Unit){
    ResponsiveScreenV17("🦷 "+tr(lang,"Ficha de Ortopedia Dentomaxilar","Dentomaxillary Orthopedics Sheet"),
        tr(lang,"Documenta crecimiento, relaciones dentomaxilares, objetivos, aparato y controles. La selección de un hallazgo no produce diagnóstico ni indicación automática.","Documents growth, dentomaxillary relationships, objectives, appliance and follow-up. Selecting a finding does not automatically produce a diagnosis or indication."),onBack){p->
        val cols=if(p.largeSystemText)1 else if(p.width==ScreenWidthV17.COMPACT)2 else 3
        ResponsiveSectionV17(tr(lang,"Evaluación","Assessment"),tr(lang,"Describe etapa de desarrollo y hallazgos clínicos/auxiliares relevantes para la ortopedia.","Describe developmental stage and clinical/diagnostic findings relevant to orthopedics.")){
            AdaptiveGridV17(6,cols){i->when(i){0->PersistTextV44("orthoMax.growth",tr(lang,"Etapa de crecimiento / maduración","Growth / maturation stage"));1->PersistTextV44("orthoMax.dentition",tr(lang,"Dentición","Dentition"));2->PersistTextV44("orthoMax.sagittal",tr(lang,"Relación sagital","Sagittal relationship"));3->PersistTextV44("orthoMax.transverse",tr(lang,"Relación transversal","Transverse relationship"));4->PersistTextV44("orthoMax.vertical",tr(lang,"Relación vertical","Vertical relationship"));else->PersistTextV44("orthoMax.function",tr(lang,"Función / hábitos / vía aérea observada","Observed function / habits / airway"))}}
        }
        ResponsiveSectionV17(tr(lang,"Objetivo y aparato","Objective and appliance"),tr(lang,"Registra la finalidad clínica y el aparato realmente indicado bajo supervisión; no se infiere sólo de una medida.","Record the clinical objective and appliance actually indicated under supervision; it is not inferred from one measurement.")){
            PersistTextV44("orthoMax.objective",tr(lang,"Objetivos terapéuticos","Treatment objectives"))
            PersistTextV44("orthoMax.appliance",tr(lang,"Aparato / diseño / componentes","Appliance / design / components"))
            PersistChoiceV44("orthoMax.use",listOf("Removible","Fijo","Funcional","Expansión / transversal","Otro"),cols)
            PersistTextV44("orthoMax.instructions",tr(lang,"Uso, higiene e indicaciones dadas","Wear, hygiene and instructions"))
        }
        ResponsiveSectionV17(tr(lang,"Control","Follow-up"),tr(lang,"Registra adaptación, cooperación, cambios observados, ajustes efectuados y siguiente revisión.","Record fit, cooperation, observed changes, performed adjustments and next review.")){
            PersistTextV44("orthoMax.control",tr(lang,"Hallazgos y ajustes del control","Follow-up findings and adjustments"))
            PersistTextV44("orthoMax.next",tr(lang,"Siguiente control / auxiliares pendientes","Next review / pending aids"))
        }
    }
}
