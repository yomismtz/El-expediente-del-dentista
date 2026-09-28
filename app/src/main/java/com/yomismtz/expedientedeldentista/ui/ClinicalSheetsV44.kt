package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
internal fun ClinicalRegisterHelpV49(lang:String,titleEs:String,titleEn:String,bodyEs:String,bodyEn:String){
    var open by remember{mutableStateOf(false)}
    TextButton(onClick={open=true}){Text("ⓘ "+tr(lang,"¿Cómo registrar esto?","How do I record this?"))}
    if(open) AlertDialog(onDismissRequest={open=false},confirmButton={TextButton(onClick={open=false}){Text(tr(lang,"Cerrar","Close"))}},title={Text(if(lang=="en")titleEn else titleEs)},text={Text(if(lang=="en")bodyEn else bodyEs)})
}

private fun guidedOptionsV50(key:String):List<String> = when {
    key.contains("phase") || key.contains("interval") || key.contains("next") -> listOf("Inicial","Corto plazo","Mediano plazo","Largo plazo","No valorado")
    key.contains("dentition") -> listOf("Temporal","Mixta temprana","Mixta tardía","Permanente joven","Permanente adulta","No valorable")
    key.contains("growth") -> listOf("Prepuberal","Pico puberal","Postpuberal","No valorado")
    key.contains("sagittal") -> listOf("Clase I","Clase II","Clase III","No valorable")
    key.contains("transverse") -> listOf("Sin alteración aparente","Mordida cruzada unilateral","Mordida cruzada bilateral","Compresión transversal","No valorable")
    key.contains("vertical") -> listOf("Relación vertical conservada","Mordida abierta","Sobremordida aumentada","No valorable")
    key.contains("hygiene") || key.contains("plaque") -> listOf("Adecuada","Regular","Deficiente","No valorada")
    key.contains("bop") || key.contains("bleed") -> listOf("Ausente","Localizado","Generalizado","No valorado")
    key.contains("mobility") -> listOf("Sin movilidad","Grado I","Grado II","Grado III","No valorado")
    key.contains("recession") -> listOf("Ausente","Localizada","Generalizada","No valorada")
    key.contains("torque") || key.contains("stability") -> listOf("Baja","Moderada","Alta","No registrada")
    key.contains("graft") -> listOf("No aplica","Injerto óseo","Regeneración guiada","Tejido blando","Combinado")
    key.contains("imaging") || key.contains("radiograph") -> listOf("No indicado","Periapical","Panorámica","CBCT","Otro auxiliar seleccionado")
    key.contains("use") || key.contains("appliance") -> listOf("Removible","Fijo","Funcional","Expansión / transversal","Sin aparato")
    key.contains("followup") || key.contains("control") -> listOf("Evolución favorable","Sin cambios relevantes","Requiere reevaluación","Requiere remisión","No valorado")
    key.contains("plan") || key.contains("objective") || key.contains("indication") -> listOf("Preventivo","Restaurador","Periodontal","Quirúrgico","Protésico","Ortopédico / ortodóncico","Reevaluación")
    key.contains("systemic") -> listOf("Sin riesgo referido","Riesgo médico controlado","Requiere interconsulta","No valorado")
    key.contains("site") || key.contains("position") -> listOf("Anterior","Posterior","Derecho","Izquierdo","Bilateral","No valorado")
    else -> listOf("Dentro de parámetros esperados","Alteración leve","Alteración moderada","Alteración marcada","No valorado","No aplica")
}
@Composable
private fun PersistTextV44(key:String,label:String){
    var value by rememberRecordState(key,"")
    Column(Modifier.fillMaxWidth()){
        Text(label,fontWeight=FontWeight.SemiBold)
        val options=guidedOptionsV50(key)
        AdaptiveGridV17(options.size,2){i->val o=options[i];FilterChip(value==o,{value=o},{Text(o)},Modifier.fillMaxWidth())}
    }
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
        ClinicalRegisterHelpV49(lang,"Ayuda · Implantología","Help · Implantology","Registra únicamente datos obtenidos: sitio edéntulo, antecedentes y riesgos relevantes, estado periodontal, tejidos, imagenología y planificación. En procedimiento anota el sistema y componentes realmente utilizados; en seguimiento, sólo hallazgos evaluados y el próximo control. No deduzcas una indicación o diagnóstico a partir de un campo aislado.","Record only obtained data: edentulous site, relevant history and risks, periodontal status, tissues, imaging and planning. For the procedure, record the system and components actually used; for follow-up, only assessed findings and the next review. Do not infer an indication or diagnosis from an isolated field.")
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
        LocalClinicalImageSectionV46(lang,p,"Ortopedia y aparatología","Orthopedics and appliances")
    }
}
