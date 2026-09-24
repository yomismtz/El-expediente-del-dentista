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
        "Mantenedor de espacio","Colocación de banda y ansa","Ajuste oclusal","Guarda oclusal","Valoración de ATM","Urgencia odontológica",
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
    var selected by remember { mutableStateOf(0) }
    val g = allGuides[selected]
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Autorización y registro de actividades","Activity authorization and record"),onBack,
            tr(lang,"Selecciona una actividad para estudiar qué se planea, cómo se realiza y qué hallazgos pueden obligar a cambiar el procedimiento.","Select an activity to study what is planned, how it is performed and which findings may require changing the procedure.")) }
        item {
            SectionCard(tr(lang,"1 · Actividad planeada","1 · Planned activity")) {
                Column(verticalArrangement=Arrangement.spacedBy(7.dp)) {
                    allGuides.forEachIndexed { i,a ->
                        FilterChip(selected==i,{selected=i},{Text(if(lang=="en") a.nameEn else a.nameEs)},modifier=Modifier.fillMaxWidth())
                    }
                }
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
                    Text(tr(lang,"El cambio se decide por diagnóstico y supervisión clínica; una complicación aislada no determina automáticamente un procedimiento.","The change is based on diagnosis and clinical supervision; an isolated complication does not automatically determine a procedure."),style=MaterialTheme.typography.bodySmall)
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
        AtmFinding("painJoint", "Dolor localizado en ATM que aumenta con función", "Localized TMJ pain increased by function"),
        AtmFinding("muscle", "Dolor/sensibilidad en maseteros o temporales", "Masseter/temporalis pain or tenderness"),
        AtmFinding("click", "Chasquido reproducible", "Reproducible click"),
        AtmFinding("crepitus", "Crepitación", "Crepitus"),
        AtmFinding("limited", "Apertura limitada (<35 mm)", "Limited opening (<35 mm)"),
        AtmFinding("excess", "Apertura excesiva (>50 mm)", "Excessive opening (>50 mm)"),
        AtmFinding("lockOpen", "Boca abierta que no puede cerrar", "Open mouth that cannot close"),
        AtmFinding("deviation", "Desviación mandibular al abrir", "Mandibular deviation on opening"),
        AtmFinding("headache", "Cefalea relacionada con masticación", "Chewing-related headache"),
        AtmFinding("tinnitus", "Tinnitus acompañado de dolor mandibular", "Tinnitus with jaw pain")
    )
    val checked = remember { mutableStateMapOf<String, Boolean>() }
    fun on(key: String) = checked[key] == true
    val presumptive = when {
        on("lockOpen") -> tr(lang, "Luxación mandibular", "Mandibular dislocation")
        on("limited") && on("click") -> tr(lang, "Trastorno discal / bloqueo: requiere exploración diferencial", "Disc disorder/locking: differential examination required")
        on("crepitus") && on("painJoint") -> tr(lang, "Hallazgos compatibles con cambio degenerativo / osteoartritis de ATM", "Findings compatible with degenerative change / TMJ osteoarthritis")
        on("excess") -> tr(lang, "Hipermovilidad / hiperlaxitud articular", "Hypermobility / joint hyperlaxity")
        on("muscle") && (on("headache") || !on("painJoint")) -> tr(lang, "Mialgia masticatoria / dolor miofascial", "Masticatory myalgia / myofascial pain")
        on("painJoint") -> tr(lang, "Artralgia de ATM", "TMJ arthralgia")
        on("click") -> tr(lang, "Chasquido articular; valorar desplazamiento discal con reducción", "Joint click; assess disc displacement with reduction")
        else -> tr(lang, "Sin patrón suficiente: completa exploración de apertura, músculos, ruidos y dolor", "Insufficient pattern: complete opening, muscle, noise and pain examination")
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, "ATM y músculos · razonamiento", "TMJ and muscles · reasoning"), onBack,
            tr(lang, "Marca signos y síntomas para obtener la orientación presuntiva más cercana. No es un diagnóstico definitivo.", "Check signs and symptoms to obtain the nearest presumptive orientation. This is not a definitive diagnosis.")) }
        item { SectionCard(tr(lang, "Referencia anatómica", "Anatomic reference")) { AtmReferenceIllustration(lang) } }
        items(findings) { finding ->
            Row(Modifier.fillMaxWidth().clickable { checked[finding.key] = !on(finding.key) }, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(on(finding.key), { checked[finding.key] = it })
                Text(if (lang == "en") finding.en else finding.es, modifier = Modifier.weight(1f))
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("🧠 ${tr(lang,"Orientación presuntiva","Presumptive orientation")}", fontWeight = FontWeight.Bold)
                    Text(presumptive, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(tr(lang,"Correlaciona con palpación muscular/articular, amplitud y trayectoria de apertura, ruidos, oclusión, historia de trauma y diagnóstico diferencial de dolor orofacial.","Correlate with muscle/joint palpation, opening range/path, sounds, occlusion, trauma history and orofacial-pain differential diagnosis."))
                }
            }
        }
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
