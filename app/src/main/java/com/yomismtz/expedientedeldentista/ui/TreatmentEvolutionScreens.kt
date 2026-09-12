package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.ToothRecord

@Composable
fun TreatmentScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    var selectedTooth by remember { mutableStateOf(16) }
    var primary by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val selectedPlan = ClinicalContent.treatmentPlans.firstOrNull { it.id == record.diagnosisId }
    val selectedOption = selectedPlan?.options?.firstOrNull { it.id == record.treatmentId }

    fun updateRecord(updated: ToothRecord) {
        onSessionChanged(session.copy(teeth=session.teeth+(selectedTooth to updated),presentTeeth=session.presentTeeth+selectedTooth))
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Diagnóstico y tratamiento por diente","Diagnosis and treatment by tooth"),onBack,
            tr(lang,"Relaciona un diagnóstico educativo con tres alternativas y revisa por qué una puede ser preferible en el escenario didáctico.","Relate a teaching diagnosis to three alternatives and review why one may be preferred in the teaching scenario.")) }
        item { Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            FilterChip(!primary,{primary=false},{Text(tr(lang,"Permanentes","Permanent"))});FilterChip(primary,{primary=true},{Text(tr(lang,"Temporales","Primary"))})
        } }
        item { SectionCard(tr(lang,"1 · Elige el diente","1 · Choose the tooth")) { DentalArchSelector(shown,selectedTooth,{selectedTooth=it}){session.teeth[it]?.diagnosisId!=null} } }
        item { SectionCard(tr(lang,"2 · Diagnóstico","2 · Diagnosis")) {
            ClinicalContent.treatmentPlans.forEach { plan -> FilterChip(record.diagnosisId==plan.id,{updateRecord(record.copy(diagnosisId=plan.id,treatmentId=null))},{Text(if(lang=="en")plan.diagnosisEn else plan.diagnosisEs)},modifier=Modifier.fillMaxWidth()) }
        } }
        if(selectedPlan!=null) item { SectionCard(tr(lang,"3 · Compara tres alternativas","3 · Compare three alternatives")) {
            selectedPlan.options.forEachIndexed { i,option ->
                Card(modifier=Modifier.fillMaxWidth(),onClick={updateRecord(record.copy(treatmentId=option.id))},colors=CardDefaults.cardColors(containerColor=if(record.treatmentId==option.id)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                        Text("${i+1}. ${if(lang=="en")option.labelEn else option.labelEs}",fontWeight=FontWeight.Bold)
                        Text(if(lang=="en")option.explanationEn else option.explanationEs)
                        if(record.treatmentId==option.id) Text(if(option.preferred)tr(lang,"✓ Preferible en este escenario educativo","✓ Preferred in this teaching scenario") else tr(lang,"△ Alternativa posible según indicación","△ Possible alternative depending on indication"),color=MaterialTheme.colorScheme.primary,fontWeight=FontWeight.Bold)
                    }
                }
            }
        } }
        if(selectedOption!=null) item { NoticeCard(tr(lang,"La elección clínica real requiere diagnóstico completo, restaurabilidad, edad/dentición, pronóstico, condiciones sistémicas, preferencias y supervisión. La app enseña razonamiento, no prescribe.","Real clinical selection requires complete diagnosis, restorability, age/dentition, prognosis, systemic conditions, preferences and supervision. The app teaches reasoning; it does not prescribe.")) }
        item { SectionCard(tr(lang,"Ortodoncia preventiva / mantenimiento de espacio","Preventive orthodontics / space maintenance")) {
            Text("🧭 ${ClinicalEngines.orthodonticSuggestion(session,lang)}")
            Text(tr(lang,"Ante pérdida prematura en dentición temporal se recuerda valorar edad, diente sucesor, erupción, espacio, oclusión y radiografía antes de considerar un aparato.","With premature loss in primary dentition, assess age, successor tooth, eruption, space, occlusion and imaging before considering an appliance."))
        } }
    }
}

private data class NoteTemplate(val titleEs:String,val titleEn:String,val textEs:String,val textEn:String)

private fun noteLibrary():List<NoteTemplate> = listOf(
    NoteTemplate("Ingreso / integración de expediente","Admission / record integration",
        "Paciente consciente, orientado y cooperador. Se revisan antecedentes y signos vitales pertinentes, se realiza exploración regional e intraoral y se integran auxiliares indicados. Se explican diagnóstico presuntivo, plan por fases y documentos de consentimiento. Registrar únicamente lo realmente realizado y los hallazgos obtenidos.",
        "Patient conscious, oriented and cooperative. Relevant history and vital signs are reviewed; regional/intraoral examination and indicated aids are integrated. Presumptive diagnosis, phased plan and consent documents are explained. Record only what was actually performed and found."),
    NoteTemplate("Alta","Discharge",
        "Se verifica la culminación de las fases previstas y el estado clínico al cierre del tratamiento. Documentar síntomas, tejidos, función, higiene, restauraciones, indicaciones de mantenimiento y fecha sugerida de revisión. Especificar si quedan condiciones en vigilancia o remisiones pendientes.",
        "Verify completion of planned phases and clinical status at closure. Document symptoms, tissues, function, hygiene, restorations, maintenance instructions and suggested review date. State any conditions under surveillance or pending referrals."),
    NoteTemplate("Baja / inasistencia / remisión","Discontinuation / nonattendance / referral",
        "Anotar fecha, motivo objetivo de la baja o remisión, estado en que queda el tratamiento, comunicaciones realizadas, riesgos explicados por no continuar y destino de la referencia cuando corresponda. Evitar juicios personales.",
        "Record date, objective reason for discontinuation/referral, treatment status, communications, explained risks of noncompletion and referral destination when applicable. Avoid personal judgments."),
    NoteTemplate("Profilaxis con pasta","Rubber-cup prophylaxis",
        "Registrar evaluación previa, superficies tratadas, remoción de biopelícula/manchas mediante profilaxis indicada, tolerancia, hallazgos posteriores e instrucciones de higiene. Si se aplica flúor, anotar producto/protocolo conforme a la clínica.",
        "Record preassessment, treated surfaces, indicated plaque/stain removal, tolerance, post-procedure findings and hygiene instructions. If fluoride is applied, document product/protocol according to the clinic."),
    NoteTemplate("Limpieza ultrasónica / destartraje","Ultrasonic scaling",
        "Documentar zonas con cálculo, instrumental ultrasónico/manual empleado según protocolo, irrigación, respuesta de tejidos, sangrado relevante, reevaluación al sondaje e instrucciones de higiene/mantenimiento.",
        "Document calculus areas, ultrasonic/manual instrumentation per protocol, irrigation, tissue response, relevant bleeding, probing reassessment and hygiene/maintenance instructions."),
    NoteTemplate("Resina compuesta","Composite resin",
        "OD __. Diagnóstico __. Registrar aislamiento, remoción de tejido indicado, preparación/conservación de estructura, protocolo adhesivo y material conforme al fabricante, técnica de colocación, ajuste de contactos/oclusión, acabado-pulido y ausencia/presencia de incidentes.",
        "Tooth __. Diagnosis __. Record isolation, indicated tissue removal, preparation/conservation of tooth structure, adhesive protocol/material per manufacturer, placement technique, contact/occlusion adjustment, finishing/polishing and incidents."),
    NoteTemplate("Amalgama","Amalgam",
        "OD __. Registrar indicación académica, aislamiento, preparación cavitaria, protección pulpar/base cuando proceda, inserción y conformación del material, ajuste oclusal y recomendaciones. Señalar claramente el material realmente utilizado.",
        "Tooth __. Record academic indication, isolation, preparation, pulp protection/base when applicable, material placement/contouring, occlusal adjustment and instructions. Clearly state the material actually used."),
    NoteTemplate("Incrustación / onlay","Inlay / onlay",
        "OD __. Registrar diagnóstico/restaurabilidad, preparación conservadora, protección pulpar si procede, impresión o escaneo, provisional cuando corresponda, prueba de restauración, adaptación/contactos, protocolo de cementación adhesiva según material/fabricante, oclusión y acabado.",
        "Tooth __. Record diagnosis/restorability, conservative preparation, pulp protection if applicable, impression/scan, provisionalization when applicable, restoration try-in, adaptation/contacts, bonding/cementation protocol per material/manufacturer, occlusion and finishing."),
    NoteTemplate("Corona","Crown",
        "OD __. Registrar diagnóstico, condición pulpar/periodontal, reducción y terminación de preparación, impresión/escaneo, provisional, prueba clínica, contactos y oclusión, selección de material/color, cementación definitiva y controles posteriores.",
        "Tooth __. Record diagnosis, pulpal/periodontal condition, preparation reduction/finish line, impression/scan, provisional, clinical try-in, contacts/occlusion, material/shade, definitive cementation and follow-up."),
    NoteTemplate("Pulpotomía","Pulpotomy",
        "OD __. Registrar diagnóstico pulpar/periapical, anestesia cuando corresponda, aislamiento absoluto, acceso, remoción de pulpa cameral, control de hemorragia y evaluación del tejido, biomaterial empleado según protocolo, sellado/restauración y plan de seguimiento.",
        "Tooth __. Record pulpal/periapical diagnosis, anesthesia when applicable, rubber-dam isolation, access, coronal pulp removal, bleeding control/tissue assessment, biomaterial per protocol, seal/restoration and follow-up plan."),
    NoteTemplate("Pulpectomía","Pulpectomy",
        "OD __. Registrar diagnóstico, aislamiento, acceso/localización de conductos, longitud de trabajo/conductometría según protocolo, preparación e irrigación segura, secado, material de obturación apropiado a dentición/indicación, sellado coronal y control.",
        "Tooth __. Record diagnosis, isolation, access/canal location, working length per protocol, preparation and safe irrigation, drying, filling material appropriate to dentition/indication, coronal seal and control."),
    NoteTemplate("Tratamiento de conductos","Root-canal treatment",
        "OD __. Anotar diagnóstico pulpar y periapical, pruebas relevantes, aislamiento absoluto, acceso, conductometría, preparación biomecánica e irrigación conforme al protocolo, obturación, radiografía/control indicado, sellado coronal y plan restaurador.",
        "Tooth __. Record pulpal/periapical diagnosis, relevant tests, rubber-dam isolation, access, working length, biomechanical preparation/irrigation per protocol, obturation, indicated control imaging, coronal seal and restorative plan."),
    NoteTemplate("Exodoncia simple","Simple extraction",
        "OD __. Registrar indicación, consentimiento, revisión de riesgo, anestesia y técnica bajo supervisión, hallazgos transoperatorios, integridad de la pieza/raíces, hemostasia, estado del alvéolo, incidentes y recomendaciones posoperatorias/signos de alarma.",
        "Tooth __. Record indication, consent, risk review, anesthesia/technique under supervision, intraoperative findings, tooth/root integrity, hemostasis, socket status, incidents and postoperative instructions/warning signs."),
    NoteTemplate("Cirugía menor","Minor oral surgery",
        "Registrar diagnóstico e indicación, sitio, estudios revisados, consentimiento, anestesia, abordaje realizado, hallazgos, tejido enviado a estudio cuando corresponda, hemostasia/sutura, complicaciones, indicaciones y cita de control.",
        "Record diagnosis/indication, site, studies reviewed, consent, anesthesia, approach, findings, tissue submitted for examination when applicable, hemostasis/suture, complications, instructions and follow-up."),
    NoteTemplate("Sellador de fosetas y fisuras","Pit and fissure sealant",
        "OD/superficie __. Registrar evaluación de la superficie, aislamiento, limpieza/preparación, material y protocolo del fabricante, colocación, polimerización cuando aplique, revisión de retención y oclusión, e indicación de control.",
        "Tooth/surface __. Record surface assessment, isolation, cleaning/preparation, material/manufacturer protocol, placement, curing when applicable, retention/occlusion check and recall."),
    NoteTemplate("Ionómero / restauración provisional","Glass ionomer / provisional restoration",
        "OD __. Registrar motivo de provisionalización o indicación del material, control de humedad, preparación, acondicionamiento si aplica, colocación, contorno, ajuste oclusal y plan para reevaluación/restauración definitiva.",
        "Tooth __. Record reason for provisionalization/material indication, moisture control, preparation, conditioning if applicable, placement, contour, occlusal adjustment and definitive reassessment plan."),
    NoteTemplate("Mantenedor de espacio","Space maintainer",
        "Registrar diente perdido, edad/dentición, valoración de espacio y sucesor, tipo de mantenedor indicado en el escenario educativo, prueba/adaptación, cementación o entrega, oclusión, higiene y calendario de revisión.",
        "Record missing tooth, age/dentition, space/successor assessment, teaching-scenario appliance type, try-in/adaptation, cementation/delivery, occlusion, hygiene and recall schedule.")
)

@Composable
fun EvolutionScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    var selected by remember { mutableStateOf(0) }
    val library=noteLibrary()
    val plans=ClinicalContent.treatmentPlans.associateBy{it.id}
    val options=ClinicalContent.treatmentPlans.flatMap{it.options}.associateBy{it.id}
    val dynamic=session.teeth.toSortedMap().mapNotNull{(tooth,record)->
        val d=record.diagnosisId?.let{plans[it]}?:return@mapNotNull null
        val o=record.treatmentId?.let{options[it]}?:return@mapNotNull null
        if(lang=="en") "Tooth $tooth · Diagnosis: ${d.diagnosisEn}. Selected plan: ${o.labelEn}. Convert this into a progress note by documenting only the procedure actually performed, relevant anesthesia/materials, incidents, instructions and follow-up."
        else "OD $tooth · Diagnóstico: ${d.diagnosisEs}. Plan seleccionado: ${o.labelEs}. Convierte esto en nota de evolución documentando únicamente el procedimiento realmente realizado, anestesia/materiales relevantes, incidencias, indicaciones y seguimiento."
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Notas de evolución · biblioteca","Progress notes · library"),onBack,
            tr(lang,"Modelos genéricos para aprender estructura y vocabulario. Deben adaptarse a lo que realmente ocurrió; nunca se copian de forma automática a un expediente real.","Generic models for learning structure and vocabulary. Adapt them to what actually occurred; never copy them automatically into a real record.")) }
        item { NoticeCard(tr(lang,"Estructura mínima: fecha/hora cuando corresponda · OD/zona · estado/signos vitales pertinentes · diagnóstico · procedimiento realizado · materiales/anestesia relevantes · incidentes · indicaciones · seguimiento · operador/supervisión conforme al formato.","Minimum structure: date/time when applicable · tooth/site · relevant status/vitals · diagnosis · procedure performed · relevant materials/anesthesia · incidents · instructions · follow-up · operator/supervision according to the form.")) }
        if(dynamic.isNotEmpty()) item { SectionCard(tr(lang,"Ejemplos ligados a tu práctica actual","Examples linked to current practice")) { dynamic.forEach{Text("• $it")} } }
        item { SectionCard(tr(lang,"Elige una nota genérica","Choose a generic note")) {
            library.chunked(3).forEachIndexed{row,items->Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                items.forEachIndexed{col,n->val idx=row*3+col;FilterChip(selected==idx,{selected=idx},{Text(if(lang=="en")n.titleEn else n.titleEs)},modifier=Modifier.weight(1f))}
                repeat(3-items.size){Text("",modifier=Modifier.weight(1f))}
            }}
        } }
        item { val n=library[selected];Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Text("📋 ${if(lang=="en")n.titleEn else n.titleEs}",fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleLarge)
                Text(if(lang=="en")n.textEn else n.textEs)
                Text(tr(lang,"Antes de usar una nota: sustituye los espacios por hallazgos reales y elimina cualquier frase que no corresponda a la cita.","Before using a note: replace placeholders with actual findings and remove any statement that did not occur."),fontWeight=FontWeight.SemiBold)
            }
        } }
    }
}
