package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ActivitiesScreen(lang: String, onBack: () -> Unit) {
    data class ActivityGuide(
        val nameEs:String,val nameEn:String,val purposeEs:String,val purposeEn:String,
        val stepsEs:List<String>,val stepsEn:List<String>,val changesEs:List<String>,val changesEn:List<String>
    )
    val guides = listOf(
        ActivityGuide("Restauración con resina","Composite restoration",
            "Eliminar tejido cariado cuando esté indicado, conservar estructura sana y restaurar forma, función y sellado.","Remove indicated carious tissue, preserve sound structure and restore form, function and seal.",
            listOf("Confirmar diagnóstico, profundidad y restaurabilidad.","Seleccionar aislamiento y anestesia cuando corresponda.","Remoción selectiva de caries/preparación conservadora.","Protección pulpar sólo si está indicada.","Adhesión, colocación de resina, fotocurado, ajuste oclusal y pulido."),
            listOf("Confirm diagnosis, depth and restorability.","Select isolation and anesthesia when indicated.","Selective caries removal/conservative preparation.","Pulp protection only when indicated.","Bonding, composite placement, curing, occlusal adjustment and polishing."),
            listOf("Caries más profunda de lo previsto → detener y reevaluar estado pulpar antes de continuar.","Exposición pulpar en diente vital → no implica automáticamente endodoncia; valorar control de hemorragia, contaminación, madurez y diagnóstico para terapia pulpar vital cuando proceda.","Hallazgos compatibles con pulpitis irreversible o necrosis → cambiar al protocolo pulpar/endodóntico; en dientes temporales puede corresponder pulpotomía o pulpectomía según diagnóstico y caso.","Diente no restaurable, fractura subgingival o pronóstico desfavorable → reevaluar plan; puede requerir cirugía/extracción o referencia.","Contaminación del campo o fallo adhesivo → corregir aislamiento y repetir el paso adhesivo afectado."),
            listOf("Caries deeper than expected → stop and reassess pulpal status.","Pulp exposure in a vital tooth → does not automatically mean root canal treatment; assess bleeding control, contamination, maturity and diagnosis for vital pulp therapy when indicated.","Findings compatible with irreversible pulpitis or necrosis → move to pulpal/endodontic protocol; in primary teeth pulpotomy or pulpectomy may be indicated according to diagnosis.","Non-restorable tooth/subgingival fracture/poor prognosis → reassess plan; surgery, extraction or referral may be required.","Field contamination or bonding failure → correct isolation and repeat the affected bonding step.")
        ),
        ActivityGuide("Sellador de fosetas y fisuras","Pit and fissure sealant","Prevenir o controlar lesiones oclusales no cavitadas seleccionadas mediante sellado.","Prevent or control selected non-cavitated occlusal lesions by sealing.",
            listOf("Valorar riesgo de caries y superficie.","Limpiar y aislar.","Grabar, lavar y secar según material.","Aplicar sellador, fotocurar y revisar retención/oclusión."),
            listOf("Assess caries risk and surface.","Clean and isolate.","Etch, rinse and dry according to material.","Apply sealant, cure and check retention/occlusion."),
            listOf("Cavitación o dentina comprometida → deja de ser un caso de sellador simple; valorar restauración.","Contaminación salival antes del curado → reacondicionar según protocolo del material.","Pérdida parcial de sellador → reparar o reemplazar tras reevaluación."),
            listOf("Cavitation/dentin involvement → no longer a simple sealant case; assess restoration.","Salivary contamination before curing → recondition according to material protocol.","Partial sealant loss → repair or replace after reassessment.")
        ),
        ActivityGuide("Profilaxis e higiene oral","Prophylaxis and oral hygiene","Eliminar depósitos blandos y enseñar control de placa individualizado.","Remove soft deposits and teach individualized plaque control.",
            listOf("Revelado/valoración de placa cuando proceda.","Instrucción de cepillado e higiene interdental.","Profilaxis selectiva y reevaluación."),
            listOf("Plaque assessment/disclosing when indicated.","Brushing and interdental hygiene instruction.","Selective prophylaxis and reassessment."),
            listOf("Cálculo supra/subgingival significativo → pasar a valoración periodontal y raspado según diagnóstico.","Sangrado importante, bolsas o movilidad → completar examen periodontal antes de limitarse a profilaxis."),
            listOf("Significant supra/subgingival calculus → periodontal assessment and scaling according to diagnosis.","Marked bleeding, pockets or mobility → complete periodontal examination rather than prophylaxis alone.")
        ),
        ActivityGuide("Raspado y alisado radicular","Scaling and root planing","Controlar depósitos y factores retentivos en enfermedad periodontal según diagnóstico.","Control deposits and retentive factors in periodontal disease according to diagnosis.",
            listOf("Periodontograma y diagnóstico periodontal.","Planificar cuadrantes/sesiones y control de dolor.","Instrumentación supra y subgingival indicada.","Reevaluación periodontal."),
            listOf("Periodontal chart and diagnosis.","Plan quadrants/sessions and pain control.","Indicated supra- and subgingival instrumentation.","Periodontal reevaluation."),
            listOf("Absceso, supuración o dolor agudo → valorar manejo de urgencia antes de continuar electivamente.","Movilidad avanzada/defecto complejo → reevaluar pronóstico y posible referencia periodontal.","Persistencia de bolsas tras fase inicial → reevaluación para tratamiento periodontal adicional."),
            listOf("Abscess, suppuration or acute pain → assess urgent management before elective continuation.","Advanced mobility/complex defect → reassess prognosis and possible periodontal referral.","Persistent pockets after initial therapy → reassess for additional periodontal treatment.")
        ),
        ActivityGuide("Extracción dental","Dental extraction","Retirar un órgano dentario cuando exista indicación y el diagnóstico/plan lo justifiquen.","Remove a tooth when an indication exists and diagnosis/treatment plan justify it.",
            listOf("Confirmar indicación, radiografía, antecedentes y consentimiento.","Anestesia y técnica de extracción adecuada.","Inspección del alveolo, hemostasia e indicaciones postoperatorias."),
            listOf("Confirm indication, radiograph, history and consent.","Anesthesia and appropriate extraction technique.","Socket inspection, hemostasis and postoperative instructions."),
            listOf("Fractura radicular → localizar y decidir recuperación o referencia según riesgo anatómico.","Hemorragia persistente → medidas locales, reevaluación médica y escalamiento según gravedad.","Comunicación oroantral/sospecha de lesión anatómica → suspender maniobras no indicadas y activar manejo/referencia."),
            listOf("Root fracture → locate and decide retrieval or referral according to anatomic risk.","Persistent bleeding → local measures, medical reassessment and escalation according to severity.","Oroantral communication/suspected anatomic injury → stop inappropriate manipulation and initiate management/referral.")
        ),
        ActivityGuide("Pulpotomía","Pulpotomy","Terapia pulpar vital indicada para conservar tejido radicular vital cuando el diagnóstico y el tipo de diente lo permiten.","Vital pulp therapy intended to preserve vital radicular tissue when diagnosis and tooth type allow.",
            listOf("Confirmar diagnóstico pulpar y periapical.","Aislamiento absoluto y acceso.","Retirar tejido coronal indicado y valorar hemostasia.","Material/protocolo correspondiente y restauración con buen sellado."),
            listOf("Confirm pulpal and periapical diagnosis.","Rubber dam isolation and access.","Remove indicated coronal tissue and assess hemostasis.","Appropriate material/protocol and well-sealed restoration."),
            listOf("Hemorragia que no se controla en tiempo clínicamente razonable o signos de inflamación radicular → reevaluar indicación; puede requerir pulpectomía/endodoncia o extracción según diente y caso.","Necrosis, supuración o lesión periapical incompatible con terapia vital → no continuar como pulpotomía."),
            listOf("Bleeding not controlled within a clinically reasonable period or signs of radicular inflammation → reassess indication; pulpectomy/root canal treatment or extraction may be needed depending on tooth/case.","Necrosis, suppuration or periapical disease incompatible with vital therapy → do not continue as pulpotomy.")
        ),
        ActivityGuide("Pulpectomía / tratamiento endodóntico","Pulpectomy / root canal treatment","Eliminar tejido pulpar infectado o necrótico, desinfectar y sellar el sistema de conductos según indicación.","Remove infected/necrotic pulp tissue, disinfect and seal the root canal system when indicated.",
            listOf("Diagnóstico pulpar/periapical y radiografías.","Aislamiento absoluto y acceso.","Conductometría, preparación, irrigación segura y control del conducto.","Obturación indicada y restauración coronal."),
            listOf("Pulpal/periapical diagnosis and radiographs.","Rubber dam isolation and access.","Working length, preparation, safe irrigation and canal control.","Indicated obturation and coronal restoration."),
            listOf("Anatomía compleja, instrumento separado, perforación o imposibilidad de negociación → detener, informar y valorar referencia endodóntica.","Exudado persistente o síntomas agudos → no forzar obturación; reevaluar diagnóstico y control de infección.","Diente temporal próximo a exfoliación o sin pronóstico restaurador → reconsiderar pulpectomía frente a extracción/manejo del espacio."),
            listOf("Complex anatomy, separated instrument, perforation or inability to negotiate canal → stop, disclose and assess endodontic referral.","Persistent exudate or acute symptoms → do not force obturation; reassess diagnosis and infection control.","Primary tooth near exfoliation or without restorative prognosis → reconsider pulpectomy versus extraction/space management.")
        ),
        ActivityGuide("Toma de impresión","Dental impression","Obtener una reproducción adecuada de dientes y tejidos para el procedimiento indicado.","Obtain an adequate reproduction of teeth and tissues for the intended procedure.",
            listOf("Seleccionar cubeta y material.","Probar cubeta y preparar al paciente.","Manipular material respetando proporciones/tiempo.","Retirar, revisar, desinfectar y manejar según material."),
            listOf("Select tray and material.","Try tray and prepare patient.","Mix material respecting ratio/time.","Remove, inspect, disinfect and handle according to material."),
            listOf("Vacíos, arrastres o zonas críticas incompletas → repetir impresión.","Reflejo nauseoso intenso → ajustar posición/técnica y cantidad de material; suspender si compromete seguridad.","Material separado de cubeta → revisar adhesión/retención y repetir."),
            listOf("Voids, pulls or missing critical areas → repeat impression.","Severe gag reflex → adjust position/technique and material amount; stop if safety is compromised.","Material separated from tray → review adhesion/retention and repeat.")
        )
    )
    val extraNames = listOf(
        "Aplicación tópica de flúor","Barniz de flúor","Control de placa O’Leary","IHOS","IPC","Periodontograma",
        "Restauración preventiva de resina","Resina Clase I","Resina Clase II","Resina Clase III","Resina Clase IV","Resina Clase V",
        "Ionómero de vidrio","Restauración provisional","Recubrimiento pulpar indirecto","Recubrimiento pulpar directo",
        "Corona de acero cromo","Corona provisional","Corona definitiva","Incrustación / onlay","Cementación provisional","Cementación definitiva",
        "Impresión para prótesis","Registro intermaxilar","Prueba de estructura protésica","Prueba de dientes","Entrega de prótesis","Ajuste de prótesis",
        "Destartraje supragingival","Control periodontal","Mantenimiento periodontal","Ferulización periodontal",
        "Exodoncia quirúrgica","Sutura","Retiro de sutura","Control posoperatorio","Manejo de alveolitis bajo supervisión",
        "Biopsia","Toma radiográfica periapical","Toma radiográfica bitewing","Fotografía clínica","Modelos de estudio",
        "Toma de impresiones diagnósticas","Análisis de modelos","Análisis radiográfico","Análisis fotográfico","Análisis cefalométrico de Steiner","Ficha endodóntica","Ficha quirúrgica","Ficha protésica","Ficha periodontal",
        "CAMBRA · valoración de riesgo de caries","Índice de placa O’Leary","CPO-D / ceo-d","ICDAS","IPC","IHOS","Exploración de mucosas",
        "Mantenedor de espacio","Mantenedor banda y ansa","Mantenedor corona y ansa","Arco lingual como mantenedor","Botón de Nance","Arco transpalatino","Zapatilla distal",
        "Recuperador de espacio","Extracciones seriadas",
        "Pulpotomía en dentición temporal","Pulpectomía instrumentada en dentición temporal","Terapia pulpar no instrumentada con pasta CTZ","Recubrimiento pulpar indirecto en odontopediatría","Recubrimiento pulpar directo en odontopediatría","Corona de acero cromo en odontopediatría","Corona estética pediátrica","Diamino fluoruro de plata","Infiltración de resina","Restauración provisional con Cavit",
        "Ajuste oclusal","Guarda oclusal","Valoración de ATM","Urgencia odontológica",
        "Interconsulta médica","Alta y mantenimiento"
    )
    val extraGuides = extraNames.map { name ->
        ActivityGuide(name,name,
            "Actividad clínica que debe partir de una indicación y diagnóstico documentados; estudia su objetivo, preparación, ejecución, control y seguimiento.",
            "Clinical activity that must start from a documented indication and diagnosis; review its objective, preparation, execution, control and follow-up.",
            listOf("Confirmar diagnóstico, indicación y órgano dentario o zona.","Revisar antecedentes, auxiliares, consentimiento y condiciones de seguridad.","Preparar instrumental, materiales, aislamiento y anestesia cuando correspondan.","Realizar la actividad conforme al protocolo docente y bajo supervisión.","Comprobar resultado, registrar incidentes, indicaciones y seguimiento."),
            listOf("Confirm diagnosis, indication and tooth/site.","Review history, aids, consent and safety conditions.","Prepare instruments, materials, isolation and anesthesia when applicable.","Perform according to the teaching protocol under supervision.","Check result and record incidents, instructions and follow-up."),
            listOf("Si el hallazgo clínico cambia el diagnóstico, detener y reevaluar antes de continuar.","Si aparece una complicación o el caso excede el nivel autorizado, solicitar supervisión y modificar o diferir la actividad.","Registrar la actividad realmente realizada, no sólo la que estaba planeada."),
            listOf("If findings change the diagnosis, stop and reassess before continuing.","If a complication occurs or the case exceeds the authorized level, obtain supervision and modify or defer the activity.","Record what was actually performed, not only what was planned.")
        )
    }
    val allGuides = guides + extraGuides
    fun rescueOptions(a:ActivityGuide):List<String> {
        val n=a.nameEs.lowercase()
        return when {
            listOf("endod","conduct","pulpect","pulpotom","recubrimiento pulpar","ctz").any{n.contains(it)} -> listOf("Reevaluar diagnóstico, restaurabilidad y control del problema; corregir o completar el procedimiento conservador si es predecible.","Si existe fracaso persistente o complicación corregible, valorar retratamiento o terapia de rescate indicada y documentar la iatrogenia/hallazgo.","Si el caso excede la competencia, el pronóstico es incierto o no es conservable, suspender y remitir; valorar alternativa quirúrgica o extracción sólo con nueva indicación.")
            listOf("extracción","exodon","cirugía","sutura","biopsia","alveol").any{n.contains(it)} -> listOf("Detener, controlar el evento inmediato y reevaluar clínica/radiográficamente antes de continuar.","Corregir la complicación local cuando esté dentro del protocolo autorizado y programar control.","Ante lesión de estructuras, hemorragia no controlada, infección progresiva u otra complicación fuera del alcance, remitir oportunamente.")
            listOf("prótesis","corona","impresión","cementación","intermaxilar","estructura protésica","prueba de dientes").any{n.contains(it)} -> listOf("Identificar la causa del problema de ajuste, soporte, retención, contactos u oclusión y corregirla si es reversible.","Reparar, rebasar, recementar, ajustar o repetir la fase/restauración cuando sea predecible.","Si existe daño biológico, fracaso del pilar o diseño no corregible, suspender esa vía y replantear la rehabilitación o remitir.")
            listOf("periodon","raspado","destartraje","gingiv","ipc","ihos","o’leary").any{n.contains(it)} -> listOf("Reevaluar control de biofilm, inflamación, sondaje y factores locales; reforzar medidas etiológicas.","Si persisten sitios activos, completar la terapia periodontal indicada y establecer reevaluación.","Ante progresión, defecto complejo o respuesta insuficiente, remitir a Periodoncia para valorar terapia avanzada.")
            listOf("mantenedor","espacio","nance","transpalatino","zapatilla","ortodon","aparato").any{n.contains(it)} -> listOf("Suspender o ajustar el aparato si produce trauma, interferencia o movimiento no deseado; reevaluar espacio y oclusión.","Reparar, recementar, sustituir o rediseñar el aparato cuando sea predecible.","Si hay pérdida de espacio, alteración eruptiva o complicación no corregible, replantear el plan y remitir.")
            listOf("resina","sellador","ionómero","incrustación","restaur","cavit","infiltración").any{n.contains(it)} -> listOf("Reevaluar diagnóstico, profundidad, sellado, contactos y oclusión; corregir defectos localizados cuando sea predecible.","Reparar o reemplazar la restauración si existe falla del material, contaminación, fractura o caries que lo justifique.","Si aparece compromiso pulpar, fractura extensa o pérdida de restaurabilidad, detener y cambiar el plan según diagnóstico o remitir.")
            else -> listOf("Detener y reevaluar diagnóstico, hallazgo o posible iatrogenia antes de continuar; documentar lo ocurrido.","Corregir o repetir la fase sólo cuando exista una alternativa conservadora, predecible y autorizada bajo supervisión.","Si empeora, excede el alcance o el pronóstico cambia, suspender y remitir o replantear el tratamiento.")
        }
    }
    var selected by remember { mutableStateOf(0) }
    var specialty by remember { mutableStateOf("Todas") }
    val specialties = listOf(
        "Todas" to listOf<String>(),
        "Preventiva" to listOf("sellador","profilaxis","higiene","flúor","fluor"),
        "Operatoria / restauradora" to listOf("resina","amalgama","ionómero","incrustación","corona"),
        "Periodoncia" to listOf("raspado","periodon","gingiv"),
        "Endodoncia" to listOf("pulpotom","pulpectom","endod","conducto"),
        "Cirugía" to listOf("extracción","exodon","cirugía","sutura","biopsia"),
        "Prótesis" to listOf("impresión","prótesis","protes","corona","provisional"),
        "Ortodoncia / ortopedia" to listOf("ortodon","mantenedor","espacio","aparat","nance","transpalatino","zapatilla distal","extracciones seriadas"),
        "Odontopediatría" to listOf("odontopedi","dentición temporal","pulpotom","pulpectom","ctz","recubrimiento pulpar","acero cromo","corona estética pediátrica","diamino fluoruro","infiltración de resina","cavit","mantenedor","zapatilla distal"),
        "Diagnóstico" to listOf("radiograf","explor","fotograf","signos vitales","índice","periodontograma","impresiones diagnósticas","modelos","steiner","cambra","o’leary","cpo-d","ceo-d","icdas","ipc","ihos","mucosas","ficha endodóntica","ficha quirúrgica","ficha protésica","ficha periodontal")
    )
    fun matchesSpecialty(a:ActivityGuide):Boolean {
        if(specialty=="Todas") return true
        val keys=specialties.firstOrNull{it.first==specialty}?.second.orEmpty()
        val hay=(a.nameEs+" "+a.nameEn).lowercase()
        return keys.any{hay.contains(it)}
    }
    val visibleGuides=allGuides.withIndex().filter{matchesSpecialty(it.value)}
    val g = allGuides[selected]
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Autorización y registro de actividades","Activity authorization and record"),onBack,
            tr(lang,"Selecciona una actividad para estudiar qué se planea, cómo se realiza y qué hallazgos pueden obligar a cambiar el procedimiento.","Select an activity to study what is planned, how it is performed and which findings may require changing the procedure.")) }
        item {
            SectionCard(tr(lang,"1 · Actividad planeada por especialidad","1 · Planned activity by specialty")) {
                Text(tr(lang,"Especialidad","Specialty"),fontWeight=FontWeight.Bold)
                ChipChoices(specialties.map{it.first to (specialty==it.first)},{i->specialty=specialties[i].first},columns=4)
                Text(tr(lang,"Actividad","Activity"),fontWeight=FontWeight.Bold)
                if(visibleGuides.isEmpty()) Text(tr(lang,"No hay actividades de esta especialidad en la biblioteca actual.","No activities from this specialty are in the current library."))
                else ChipChoices(visibleGuides.map{(idx,a)->(if(lang=="en")a.nameEn else a.nameEs) to (selected==idx)},{i->selected=visibleGuides[i].index},columns=3)
            }
        }
        item { SectionCard(tr(lang,"2 · ¿En qué consiste?","2 · What does it involve?")) { Text(if(lang=="en")g.purposeEn else g.purposeEs) } }
        item { SectionCard(tr(lang,"3 · Secuencia clínica educativa","3 · Educational clinical sequence")) {
            Column(verticalArrangement=Arrangement.spacedBy(7.dp)) { (if(lang=="en")g.stepsEn else g.stepsEs).forEachIndexed{i,s->Text("${i+1}. $s")} }
        } }
        item {
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text("⚠️ "+tr(lang,"4 · Si encuentro esto, ¿a qué actividad puede cambiar?","4 · If I find this, what can the activity change to?"),fontWeight=FontWeight.Bold)
                    (if(lang=="en")g.changesEn else g.changesEs).forEach { Text("• $it") }
                    Text(tr(lang,"3 opciones de rescate educativo","3 educational rescue options"),fontWeight=FontWeight.Bold)
                    rescueOptions(g).forEachIndexed { i,opt -> Text("${i+1}. $opt") }
                    Text(tr(lang,"Son rutas educativas, no instrucciones automáticas: primero identifica causa y gravedad, controla cualquier urgencia, documenta el evento y solicita supervisión antes de corregir, cambiar el plan o remitir.","These are educational pathways, not automatic instructions: first identify cause and severity, control any emergency, document the event and obtain supervision before correction, plan change or referral."),style=MaterialTheme.typography.bodySmall)
                }
            }
        }
        item { SectionCard(tr(lang,"5 · Autorización y supervisión","5 · Authorization and supervision")) {
            Text(tr(lang,"Antes de iniciar: el docente valida diagnóstico, actividad planeada y condiciones de seguridad. Si durante el procedimiento cambia el diagnóstico o el plan, se solicita una nueva autorización antes de continuar. Al finalizar se registra la actividad realmente realizada y la supervisión final.","Before starting: the instructor validates diagnosis, planned activity and safety conditions. If diagnosis or plan changes during treatment, new authorization is obtained before continuing. At the end, record the activity actually performed and final supervision."))
        } }
    }
}

private data class AtmFinding(val key: String, val es: String, val en: String)

@Composable
fun AtmScreen(lang: String, onBack: () -> Unit) {
    val findings = listOf(
        AtmFinding("painJoint","Dolor localizado en ATM que aumenta con función","Localized TMJ pain increased by function"),
        AtmFinding("muscle","Dolor/sensibilidad en maseteros o temporales","Masseter/temporalis pain or tenderness"),
        AtmFinding("click","Chasquido reproducible","Reproducible click"),
        AtmFinding("crepitus","Crepitación","Crepitus"),
        AtmFinding("headache","Cefalea modificada por masticación o función mandibular","Headache modified by chewing or jaw function"),
        AtmFinding("locking","Bloqueo o atoramiento mandibular referido","Reported jaw locking/catching"),
        AtmFinding("lockOpen","Boca abierta que no puede cerrar","Open mouth that cannot close"),
        AtmFinding("ear","Dolor preauricular/otalgia sin causa ótica confirmada","Preauricular/ear pain without confirmed otologic cause"),
        AtmFinding("parafunction","Bruxismo/apretamiento referido","Reported bruxism/clenching"),
        AtmFinding("trauma","Antecedente de traumatismo mandibular/ATM","History of mandibular/TMJ trauma")
    )
    val checked=remember { mutableStateMapOf<String,Boolean>() }
    fun on(key:String)=checked[key]==true
    var opening by remember { mutableStateOf("No medida") }
    var rightLat by remember { mutableStateOf("No medida") }
    var leftLat by remember { mutableStateOf("No medida") }
    var protrusion by remember { mutableStateOf("No medida") }
    var trajectory by remember { mutableStateOf("Recta / sin desviación evidente") }
    var jointPalpation by remember { mutableStateOf("Sin dolor reproducible") }
    var musclePalpation by remember { mutableStateOf("Sin dolor reproducible") }

    val orientation=when {
        on("lockOpen") -> tr(lang,"Luxación mandibular: situación que requiere valoración clínica inmediata","Mandibular dislocation: situation requiring prompt clinical assessment")
        opening=="<35 mm" && on("locking") -> tr(lang,"Limitación con bloqueo: requiere exploración diferencial del trastorno intraarticular","Limited opening with locking: differential examination for intra-articular disorder required")
        on("crepitus") && on("painJoint") -> tr(lang,"Hallazgos articulares con crepitación y dolor; correlacionar clínicamente y valorar cambios degenerativos","Joint findings with crepitus and pain; correlate clinically and assess for degenerative changes")
        on("muscle") && !on("painJoint") -> tr(lang,"Patrón de dolor muscular masticatorio; completar palpación y diagnóstico diferencial","Masticatory muscle pain pattern; complete palpation and differential diagnosis")
        on("painJoint") -> tr(lang,"Patrón de dolor articular de ATM; completar criterios clínicos y diagnóstico diferencial","TMJ joint-pain pattern; complete clinical criteria and differential diagnosis")
        on("click") -> tr(lang,"Chasquido articular reproducible; caracterizar durante apertura y cierre","Reproducible joint click; characterize during opening and closing")
        else -> tr(lang,"Sin patrón suficiente para orientación: completar exploración funcional","Insufficient pattern for orientation: complete functional examination")
    }

    @Composable fun Pick(title:String,values:List<String>,selected:String,set:(String)->Unit) {
        SectionCard(title) { Row(horizontalArrangement=Arrangement.spacedBy(6.dp),modifier=Modifier.fillMaxWidth()) { values.forEach { v -> FilterChip(selected==v,{set(v)},{Text(v)},modifier=Modifier.weight(1f)) } } }
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Ficha de ATM y trastornos temporomandibulares","TMJ and temporomandibular disorders sheet"),onBack,tr(lang,"Exploración educativa estructurada de dolor, músculos, ruidos y movimientos. La orientación generada no sustituye el diagnóstico clínico.","Structured educational examination of pain, muscles, sounds and movement. Generated orientation does not replace clinical diagnosis.")) }
        item { SectionCard(tr(lang,"1 · Síntomas y antecedentes","1 · Symptoms and history")) { findings.forEach { f -> Row(Modifier.fillMaxWidth().clickable { checked[f.key]=!on(f.key) },verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) { Checkbox(on(f.key),{checked[f.key]=it});Text(if(lang=="en")f.en else f.es,modifier=Modifier.weight(1f)) } } } }
        item { Pick(tr(lang,"2 · Apertura máxima interincisal","2 · Maximum interincisal opening"),listOf("No medida","<35 mm","35–39 mm","40–44 mm","45–50 mm",">50 mm"),opening){opening=it} }
        item { Pick(tr(lang,"3 · Lateralidad derecha","3 · Right lateral excursion"),listOf("No medida","<4 mm","4–6 mm","7–9 mm","≥10 mm"),rightLat){rightLat=it} }
        item { Pick(tr(lang,"4 · Lateralidad izquierda","4 · Left lateral excursion"),listOf("No medida","<4 mm","4–6 mm","7–9 mm","≥10 mm"),leftLat){leftLat=it} }
        item { Pick(tr(lang,"5 · Protrusión","5 · Protrusion"),listOf("No medida","<4 mm","4–5 mm","6–8 mm","≥9 mm"),protrusion){protrusion=it} }
        item { Pick(tr(lang,"6 · Trayectoria de apertura","6 · Opening trajectory"),listOf("Recta / sin desviación evidente","Desviación a derecha","Desviación a izquierda","Deflexión a derecha","Deflexión a izquierda","Bloqueo durante movimiento"),trajectory){trajectory=it} }
        item { Pick(tr(lang,"7 · Palpación articular","7 · Joint palpation"),listOf("Sin dolor reproducible","Dolor derecho","Dolor izquierdo","Dolor bilateral"),jointPalpation){jointPalpation=it} }
        item { Pick(tr(lang,"8 · Palpación muscular","8 · Muscle palpation"),listOf("Sin dolor reproducible","Masetero derecho","Masetero izquierdo","Temporal derecho","Temporal izquierdo","Dolor bilateral/múltiples músculos"),musclePalpation){musclePalpation=it} }
        item {
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(7.dp)) {
                    Text("🧠 "+tr(lang,"Orientación clínica educativa","Educational clinical orientation"),fontWeight=FontWeight.Bold)
                    Text(orientation,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold)
                    Text(tr(lang,"Apertura: $opening. Lateralidad D: $rightLat; I: $leftLat. Protrusión: $protrusion. Trayectoria: $trajectory. Palpación ATM: $jointPalpation. Palpación muscular: $musclePalpation.","Opening: $opening. Right excursion: $rightLat; left: $leftLat. Protrusion: $protrusion. Trajectory: $trajectory. TMJ palpation: $jointPalpation. Muscle palpation: $musclePalpation."))
                }
            }
        }
        item { NoticeCard(tr(lang,"La ficha ayuda a ordenar la exploración. El diagnóstico de TTM requiere historia, reproducción del dolor familiar cuando corresponda, exploración estandarizada y diagnóstico diferencial de dolor orofacial. Tinnitus u otalgia requieren considerar causas no odontológicas.","The sheet helps structure the examination. TMD diagnosis requires history, reproduction of familiar pain when applicable, standardized examination and orofacial-pain differential diagnosis. Tinnitus or ear pain require consideration of non-dental causes.")) }
    }
}

private data class OcclusionTopic(val es: String, val en: String, val bodyEs: String, val bodyEn: String)

@Composable
fun OcclusionScreen(lang: String, onBack: () -> Unit) {
    var selected by remember { mutableStateOf(0) }
    val topics = listOf(
        OcclusionTopic("Planos terminales", "Terminal planes", "En dentición temporal compara las caras distales de los segundos molares: plano recto, escalón mesial o escalón distal. Ayudan a anticipar, sin determinar por sí solos, la relación molar permanente.", "In primary dentition compare distal surfaces of second molars: flush terminal plane, mesial step or distal step. They help anticipate, but do not alone determine, the permanent molar relation."),
        OcclusionTopic("Clasificación de Angle", "Angle classification", "Describe la relación anteroposterior de los primeros molares permanentes: Clase I, Clase II (con sus divisiones) y Clase III. Debe correlacionarse con relación canina y patrón esquelético.", "Describes anteroposterior relation of permanent first molars: Class I, Class II (divisions) and Class III. Correlate with canine relation and skeletal pattern."),
        OcclusionTopic("Relación canina", "Canine relation", "Complementa la relación molar y es especialmente útil cuando la relación de molares no puede valorarse con claridad.", "Complements molar relation and is useful when molar relation cannot be assessed clearly."),
        OcclusionTopic("Overjet", "Overjet", "Superposición horizontal entre incisivos. El material docente utiliza 2–3 mm como referencia educativa y pide describir aumento, disminución o relación invertida.", "Horizontal overlap of incisors. The teaching material uses 2–3 mm as an educational reference and asks to describe increased, reduced or reversed relation."),
        OcclusionTopic("Overbite", "Overbite", "Superposición vertical anterior. Registra si es normal, profunda, reducida, borde a borde o existe mordida abierta.", "Anterior vertical overlap. Record normal, deep, reduced, edge-to-edge or open bite."),
        OcclusionTopic("Transversal y líneas medias", "Transverse relation and midlines", "Valora mordidas cruzadas, simetría y coincidencia/desviación de líneas medias, diferenciando lo dental de posibles componentes funcionales/esqueléticos.", "Assess crossbites, symmetry and midline coincidence/deviation, distinguishing dental from possible functional/skeletal components.")
    )
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, "Examen de oclusión", "Occlusal examination"), onBack,
            tr(lang,"Selecciona cada concepto para ver la imagen guía y su significado.","Select each concept to see the guide image and meaning.")) }
        item { SectionCard(tr(lang,"Vista de referencia","Reference view")) { OcclusionReferenceIllustration(lang) } }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
                topics.take(3).forEachIndexed { i,t -> FilterChip(selected == i,{selected=i},{Text(if(lang=="en") t.en else t.es)},modifier=Modifier.weight(1f)) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
                topics.drop(3).forEachIndexed { i,t -> val idx=i+3; FilterChip(selected == idx,{selected=idx},{Text(if(lang=="en") t.en else t.es)},modifier=Modifier.weight(1f)) }
            }
        }
        item {
            val t = topics[selected]
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text(if(lang=="en") t.en else t.es,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleLarge)
                    Text(if(lang=="en") t.bodyEn else t.bodyEs)
                }
            }
        }
        item { NoticeCard(tr(lang,"La oclusión se describe integrando dentición, erupción, plano terminal/Angle, relación canina, líneas medias, overjet/overbite, mordidas cruzadas/abiertas, apiñamiento, giros y espacios.","Occlusion is described by integrating dentition, eruption, terminal plane/Angle, canine relation, midlines, overjet/overbite, cross/open bite, crowding, rotations and spacing.")) }
    }
}

private data class MucosaRegion(val id: String,val es:String,val en:String,val normalEs:String,val normalEn:String,val changesEs:String,val changesEn:String)

@Composable
fun MucosaScreen(lang: String, onBack: () -> Unit) {
    val regions = listOf(
        MucosaRegion("labios","Labios y bermellón","Lips and vermilion","Rosados, íntegros, hidratados, contorno regular y sellado/movilidad conservados.","Pink, intact, hydrated, regular contour with preserved seal/mobility.","Palidez/eritema/pigmentación, fisuras, costras, úlceras, vesículas, edema, resequedad o frenillo anormal.","Pallor/erythema/pigmentation, fissures, crusts, ulcers, vesicles, edema, dryness or abnormal frenum."),
        MucosaRegion("carrillo","Carrillos / mucosa yugal","Cheeks / buccal mucosa","Rosada, húmeda y lisa; conducto de Stensen permeable sin aumento de volumen.","Pink, moist and smooth; patent Stensen duct without swelling.","Línea alba, lesión elevada, mordisqueo, úlcera, placa blanca, pigmentación o alteración de salida salival.","Linea alba, raised lesion, cheek biting, ulcer, white plaque, pigmentation or salivary-flow alteration."),
        MucosaRegion("paladar","Paladar duro y blando","Hard and soft palate","Rosado, íntegro; paladar blando móvil con elevación simétrica y úvula centrada.","Pink and intact; mobile soft palate with symmetric elevation and centered uvula.","Torus, eritema, placas, petequias, úlceras, edema, asimetría o úvula desviada.","Torus, erythema, plaques, petechiae, ulcers, edema, asymmetry or deviated uvula."),
        MucosaRegion("lengua","Lengua","Tongue","Rosada, papilada, movilidad conservada y sin induración.","Pink, papillae present, preserved mobility and no induration.","Lengua fisurada/geográfica, saburra, depapilación, placas, macroglosia, úlcera, pigmentación o induración.","Fissured/geographic tongue, coating, depapillation, plaques, macroglossia, ulcer, pigmentation or induration."),
        MucosaRegion("piso","Piso de boca","Floor of mouth","Rosado, blando, sin aumento de volumen; conductos salivales permeables y movilidad lingual conservada.","Pink, soft, no swelling; patent salivary ducts and preserved tongue mobility.","Ránula, edema, coloración azulada/roja, masa, úlcera, dolor, frenillo lingual alterado o disminución de saliva.","Ranula, edema, bluish/red change, mass, ulcer, pain, altered lingual frenum or reduced saliva."),
        MucosaRegion("orofaringe","Orofaringe","Oropharynx","Úvula centrada, pilares sin inflamación y amígdalas sin exudado.","Centered uvula, non-inflamed pillars and tonsils without exudate.","Eritema, hipertrofia amigdalina, exudado, placas, secreción, dolor o asimetría.","Erythema, tonsillar hypertrophy, exudate, plaques, secretion, pain or asymmetry.")
    )
    var selectedId by remember { mutableStateOf("labios") }
    val selected = regions.first { it.id == selectedId }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Examen de mucosas orales","Oral mucosa examination"), onBack,
            tr(lang,"Toca una zona de la boca o su botón para consultar apariencia normal, cambios posibles y cómo describirla.","Tap a mouth region or its button to view normal appearance, possible changes and documentation guidance.")) }
        item {
            SectionCard(tr(lang,"Boca interactiva","Interactive mouth")) {
                InteractiveMouthMap(selectedId) { selectedId = it }
                Row(horizontalArrangement=Arrangement.spacedBy(4.dp),modifier=Modifier.fillMaxWidth()) {
                    regions.take(3).forEach { r -> FilterChip(selectedId==r.id,{selectedId=r.id},{Text(if(lang=="en")r.en else r.es)},modifier=Modifier.weight(1f)) }
                }
                Row(horizontalArrangement=Arrangement.spacedBy(4.dp),modifier=Modifier.fillMaxWidth()) {
                    regions.drop(3).forEach { r -> FilterChip(selectedId==r.id,{selectedId=r.id},{Text(if(lang=="en")r.en else r.es)},modifier=Modifier.weight(1f)) }
                }
            }
        }
        item {
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text(if(lang=="en") selected.en else selected.es,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleLarge)
                    Text("✓ ${tr(lang,"Datos normales","Normal findings")}: ${if(lang=="en")selected.normalEn else selected.normalEs}")
                    Text("⚠ ${tr(lang,"Cambios a describir","Changes to describe")}: ${if(lang=="en")selected.changesEn else selected.changesEs}")
                    Text("✍️ ${tr(lang,"Describe siempre color, forma, tamaño, consistencia, integridad, superficie y función cuando correspondan.","When applicable describe color, shape, size, consistency, integrity, surface and function.")}")
                }
            }
        }
    }
}

@Composable
private fun InteractiveMouthMap(selected: String, onSelected: (String) -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    val outline = MaterialTheme.colorScheme.outline
    val surface = MaterialTheme.colorScheme.surfaceVariant
    Canvas(
        modifier=Modifier.fillMaxWidth().height(250.dp).pointerInput(Unit) {
            detectTapGestures { o ->
                val x=o.x/size.width; val y=o.y/size.height
                val id = when {
                    y < .22f || y > .82f -> "labios"
                    x < .25f || x > .75f -> "carrillo"
                    y < .48f -> "paladar"
                    y > .68f -> "piso"
                    x in .38f..62f && y in .45f..68f -> "lengua"
                    else -> "orofaringe"
                }
                onSelected(id)
            }
        }
    ) {
        val w=size.width; val h=size.height
        drawOval(if(selected=="labios") primary.copy(alpha=.35f) else surface, topLeft=Offset(w*.08f,h*.05f), size=Size(w*.84f,h*.9f))
        drawOval(Color(0xFF4E2028), topLeft=Offset(w*.15f,h*.16f), size=Size(w*.70f,h*.67f))
        drawOval(if(selected=="paladar") primary.copy(alpha=.55f) else Color(0xFFE7A7A1),topLeft=Offset(w*.29f,h*.22f),size=Size(w*.42f,h*.25f))
        drawOval(if(selected=="lengua") primary.copy(alpha=.55f) else Color(0xFFE78383),topLeft=Offset(w*.31f,h*.48f),size=Size(w*.38f,h*.26f))
        drawOval(if(selected=="piso") primary.copy(alpha=.45f) else Color(0xFFD99999),topLeft=Offset(w*.34f,h*.70f),size=Size(w*.32f,h*.09f))
        drawCircle(if(selected=="orofaringe") primary else Color(0xFFC85C68),radius=w*.035f,center=Offset(w*.5f,h*.43f))
        drawRect(if(selected=="carrillo") primary.copy(alpha=.35f) else Color.Transparent,topLeft=Offset(w*.15f,h*.30f),size=Size(w*.13f,h*.38f))
        drawRect(if(selected=="carrillo") primary.copy(alpha=.35f) else Color.Transparent,topLeft=Offset(w*.72f,h*.30f),size=Size(w*.13f,h*.38f))
        drawOval(outline, topLeft=Offset(w*.08f,h*.05f),size=Size(w*.84f,h*.9f),style=androidx.compose.ui.graphics.drawscope.Stroke(3f))
    }
}

private data class Aid(val titleEs:String,val titleEn:String,val whyEs:String,val whyEn:String,val exampleEs:String,val exampleEn:String)

@Composable
fun AuxiliariesScreen(lang: String, onBack: () -> Unit) {
    var selected by remember { mutableStateOf(0) }
    val aids = listOf(
        Aid("Histopatológico","Histopathology","Analiza microscópicamente tejido obtenido por biopsia y ayuda a confirmar el diagnóstico de lesiones sospechosas.","Microscopic analysis of biopsy tissue that helps confirm suspicious lesions.","Lesión persistente o tejido periapical retirado quirúrgicamente que necesita diagnóstico definitivo.","Persistent lesion or surgically removed periapical tissue needing definitive diagnosis."),
        Aid("Análisis clínicos de laboratorio","Clinical laboratory tests","Biometría, glucosa, función renal/hepática y pruebas seleccionadas ayudan a valorar riesgo sistémico cuando la historia o el procedimiento lo requieren.","CBC, glucose, kidney/liver tests and selected studies help assess systemic risk when history/procedure requires them.","Antes de cirugía invasiva en un paciente con sospecha de anemia, trastorno hemorrágico o enfermedad sistémica descontrolada.","Before invasive surgery in a patient with suspected anemia, bleeding disorder or uncontrolled systemic disease."),
        Aid("Coagulación","Coagulation testing","TP/INR, TTPa y otras pruebas se interpretan según indicación clínica, medicamentos y rango del laboratorio.","PT/INR, aPTT and other tests are interpreted according to clinical indication, medications and the reporting laboratory.","Paciente anticoagulado o con historia de sangrado anormal que requiere valoración antes de un procedimiento invasivo.","Anticoagulated patient or history of abnormal bleeding requiring assessment before an invasive procedure."),
        Aid("Microbiológico","Microbiology","Cultivos, estudios micológicos, antibiogramas o pruebas moleculares pueden identificar microorganismos en infecciones seleccionadas.","Cultures, mycology, susceptibility testing or molecular methods can identify organisms in selected infections.","Infección persistente/atípica donde conocer el agente puede modificar el tratamiento.","Persistent/atypical infection where identifying the organism may change management."),
        Aid("Modelos de estudio","Study models","Permiten analizar oclusión, forma de arcada, tamaño dental y discrepancia de espacio fuera de la boca.","Allow analysis of occlusion, arch form, tooth size and space discrepancy outside the mouth.","Paciente con apiñamiento, pérdida de espacio o planificación protésica/ortodóncica.","Patient with crowding, space loss or prosthetic/orthodontic planning."),
        Aid("Radiografías","Radiographs","Periapical, aleta de mordida, oclusal, panorámica y lateral de cráneo responden preguntas distintas; el estudio se elige por la información requerida.","Periapical, bitewing, occlusal, panoramic and lateral cephalometric images answer different questions; choose the study for the information needed.","Aleta mordible para caries interproximal; periapical para raíz/ápice; panorámica para panorama general de arcadas.","Bitewing for interproximal caries; periapical for root/apex; panoramic for overall arch survey."),
        Aid("Cefalometría","Cephalometry","Traza puntos y mediciones sobre radiografía lateral de cráneo para valorar relaciones dentoesqueléticas y crecimiento.","Uses landmarks and measurements on lateral cephalogram to assess dentoskeletal relations and growth.","Maloclusión con sospecha de componente esquelético o evaluación de crecimiento.","Malocclusion with suspected skeletal component or growth assessment.")
    )

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Auxiliares de diagnóstico","Diagnostic aids"), onBack,
            tr(lang,"Selecciona un estudio para saber para qué sirve, por qué puede ser importante y un ejemplo de indicación odontológica.","Select a test to see its purpose, why it matters and an example dental indication.")) }
        item { SectionCard(tr(lang,"Referencia visual","Visual reference")) { DiagnosticAidsIllustration(lang) } }
        items(aids.size) { i ->
            val a=aids[i]
            Card(modifier=Modifier.fillMaxWidth().clickable{selected=i},colors=CardDefaults.cardColors(containerColor=if(selected==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                    Text(if(lang=="en")a.titleEn else a.titleEs,fontWeight=FontWeight.Bold)
                    if(selected==i){
                        Text("💡 ${if(lang=="en")a.whyEn else a.whyEs}")
                        Text("🦷 ${tr(lang,"Ejemplo: ","Example: ")}${if(lang=="en")a.exampleEn else a.exampleEs}")
                    }
                }
            }
        }
        item {
            NoticeCard(tr(lang,"Los intervalos de laboratorio dependen del método, edad, sexo, estado fisiológico y laboratorio. Para práctica clínica se debe leer el intervalo de referencia impreso en el reporte y aplicar guías vigentes; la app no sustituye la interpretación médica.","Laboratory intervals depend on method, age, sex, physiologic state and laboratory. For clinical practice use the reference interval printed on the report and current guidance; the app does not replace medical interpretation."))
        }
    }
}

@Composable
fun SimpleEducationalSheet(
    lang: String,
    titleEs: String,
    titleEn: String,
    introEs: String,
    introEn: String,
    bulletsEs: List<String>,
    bulletsEn: List<String>,
    onBack: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, titleEs, titleEn), onBack, tr(lang, introEs, introEn)) }
        item {
            SectionCard(tr(lang, "Qué debe reconocer el alumno", "What the student should recognize")) {
                val bullets = if (lang == "en") bulletsEn else bulletsEs
                bullets.forEach { Text("• $it") }
            }
        }
        item { NoticeCard(tr(lang,"Esta hoja es explicativa y no almacena información clínica real.","This is an explanatory sheet and does not store real clinical information.")) }
    }
}
