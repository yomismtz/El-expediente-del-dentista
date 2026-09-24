package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.PerioRecord
import com.yomismtz.expedientedeldentista.clinical.PulpalAssessment

@Composable
fun PeriodontogramScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedTooth by remember { mutableStateOf(16) }
    val record = session.periodontogram[selectedTooth] ?: PerioRecord()
    val siteNames = listOf("MV/MB", "V/B", "DV/DB", "ML", "L/P", "DL")
    fun update(updated: PerioRecord) {
        onSessionChanged(session.copy(periodontogram = session.periodontogram + (selectedTooth to updated)))
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Periodontograma", "Periodontal chart"),
                onBack,
                tr(lang, "Seis sitios por diente: tres vestibulares y tres linguales/palatinos. Aprende qué dato corresponde a cada renglón del periodontograma.",
                    "Six sites per tooth: three buccal and three lingual/palatal. Learn which finding belongs in each periodontal-chart row.")
            )
        }
        item {
            SectionCard(tr(lang, "Selecciona diente", "Select tooth")) {
                DentalArchSelector(ClinicalContent.permanentTeeth, selectedTooth, { selectedTooth = it }) { it in session.periodontogram }
            }
        }
        item {
            SectionCard("OD $selectedTooth · ${tr(lang, "Profundidad de sondaje", "Probing depth")}") {
                Text(tr(lang,
                    "Selecciona los milímetros medidos en cada sitio; no es necesario escribirlos.",
                    "Select the measured millimeters at each site; no typing is required."))
                siteNames.forEachIndexed { index, site ->
                    Text(site, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(0,1,2,3,4,5,6,7,8,9,10,12,15).forEach { mm ->
                            FilterChip(
                                selected = record.probingDepths[index] == mm,
                                onClick = {
                                    val values = record.probingDepths.toMutableList()
                                    values[index] = mm
                                    update(record.copy(probingDepths = values))
                                },
                                label = { Text(mm.toString()) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Text(tr(lang, "Margen/recesión gingival (mm)", "Gingival margin/recession (mm)"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(-5,-3,-2,-1,0,1,2,3,4,5,7,10,15).forEach { mm ->
                        FilterChip(record.recessionMm == mm,{ update(record.copy(recessionMm = mm)) },{ Text(mm.toString()) },modifier=Modifier.weight(1f))
                    }
                }
                BooleanRow(tr(lang, "Sangrado al sondaje", "Bleeding on probing"), record.bleeding) { update(record.copy(bleeding = it)) }
                BooleanRow(tr(lang, "Placa", "Plaque"), record.plaque) { update(record.copy(plaque = it)) }
                BooleanRow(tr(lang, "Supuración", "Suppuration"), record.suppuration) { update(record.copy(suppuration = it)) }
                Text(tr(lang, "Movilidad", "Mobility"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..3).forEach { grade ->
                        FilterChip(record.mobility == grade, { update(record.copy(mobility = grade)) }, { Text(grade.toString()) }, modifier = Modifier.weight(1f))
                    }
                }
                Text(tr(lang, "Furcación", "Furcation"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..3).forEach { grade ->
                        FilterChip(record.furcation == grade, { update(record.copy(furcation = grade)) }, { Text(grade.toString()) }, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang,"Resumen periodontal automático","Automatic periodontal summary")) {
                val maxPd = record.probingDepths.maxOrNull() ?: 0
                val bleedingText = if(record.bleeding) tr(lang,"con sangrado al sondaje","with bleeding on probing") else tr(lang,"sin sangrado al sondaje","without bleeding on probing")
                val plaqueText = if(record.plaque) tr(lang,"placa presente","plaque present") else tr(lang,"sin placa marcada","no plaque marked")
                Text(tr(lang,
                    "OD $selectedTooth: profundidad máxima seleccionada $maxPd mm; $bleedingText; $plaqueText; movilidad grado ${record.mobility}; furcación grado ${record.furcation}; margen/recesión ${record.recessionMm} mm.",
                    "Tooth $selectedTooth: selected maximum probing depth $maxPd mm; $bleedingText; $plaqueText; mobility grade ${record.mobility}; furcation grade ${record.furcation}; gingival margin/recession ${record.recessionMm} mm."),fontWeight=FontWeight.Bold)
            }
        }
        item { NoticeCard(ClinicalEngines.periodontalSummary(session, lang)) }
    }
}

@Composable
fun PulpalScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val a = session.pulpal
    fun update(updated: PulpalAssessment) = onSessionChanged(session.copy(pulpal = updated))
    val result = ClinicalEngines.pulpalDiagnosis(a)

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Diagnóstico pulpar · signos y síntomas", "Pulpal diagnosis · signs and symptoms"),
                onBack,
                tr(lang,
                    "Esta hoja trabaja únicamente el razonamiento pulpar a partir de anamnesis y pruebas clínicas. Los hallazgos radiográficos periapicales están en una pestaña separada.",
                    "This sheet focuses only on pulpal reasoning from history and clinical tests. Periapical radiographic findings are on a separate tab.")
            )
        }
        item {
            SectionCard(tr(lang, "Dolor referido", "Reported pain")) {
                BooleanRow(tr(lang, "Dolor espontáneo", "Spontaneous pain"), a.spontaneousPain) { update(a.copy(spontaneousPain = it)) }
                BooleanRow(tr(lang, "Dolor nocturno", "Night pain"), a.nightPain) { update(a.copy(nightPain = it)) }
                BooleanRow(tr(lang, "Dolor con alimentos dulces", "Pain with sweets"), a.sweetsPain) { update(a.copy(sweetsPain = it)) }
            }
        }
        item {
            SectionCard(tr(lang, "Pruebas de sensibilidad", "Sensitivity tests")) {
                BooleanRow(tr(lang, "Responde al frío", "Responds to cold"), a.coldPositive) { update(a.copy(coldPositive = it)) }
                BooleanRow(tr(lang, "El dolor al frío persiste después de retirar el estímulo", "Cold pain lingers after the stimulus is removed"), a.coldLingering) { update(a.copy(coldLingering = it)) }
                BooleanRow(tr(lang, "Respuesta dolorosa al calor", "Painful response to heat"), a.heatPositive) { update(a.copy(heatPositive = it)) }
                BooleanRow(tr(lang, "No responde a pruebas de sensibilidad", "No response to sensitivity testing"), a.sensitivityNegative) { update(a.copy(sensitivityNegative = it)) }
            }
        }
        item {
            SectionCard(tr(lang, "Antecedentes endodónticos", "Endodontic history")) {
                BooleanRow(tr(lang, "Tratamiento de conductos previo", "Previous root-canal treatment"), a.previousRootCanal) { update(a.copy(previousRootCanal = it)) }
                BooleanRow(tr(lang, "Terapia endodóntica previamente iniciada", "Previously initiated endodontic therapy"), a.previousPartialEndo) { update(a.copy(previousPartialEndo = it)) }
                BooleanRow(tr(lang, "Caries profunda o exposición pulpar", "Deep caries or pulpal exposure"), a.deepCariesOrExposure) { update(a.copy(deepCariesOrExposure = it)) }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🧠 ${tr(lang, "Más compatible con", "Most compatible with")}", fontWeight = FontWeight.Bold)
                    Text(if (lang == "en") result.pulpalEn else result.pulpalEs, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(if (lang == "en") result.explanationEn else result.explanationEs)
                }
            }
        }
        item {
            NoticeCard(tr(lang,
                "Pista didáctica: dolor breve al frío que desaparece rápidamente orienta a un proceso reversible; dolor espontáneo/nocturno o persistente tras el estímulo orienta a compromiso irreversible. La ausencia de respuesta debe interpretarse junto con el resto de pruebas.",
                "Teaching hint: brief cold pain that resolves quickly suggests a reversible process; spontaneous/night pain or lingering pain suggests irreversible involvement. Lack of response must be interpreted with the other tests."))
        }
    }
}

@Composable
fun ApicalScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val a = session.pulpal
    fun update(updated: PulpalAssessment) = onSessionChanged(session.copy(pulpal = updated))
    val result = ClinicalEngines.pulpalDiagnosis(a)

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Diagnóstico periapical · clínica y radiografía", "Periapical diagnosis · clinical and radiographic findings"),
                onBack,
                tr(lang,
                    "Observa el esquema radiográfico y selecciona hallazgos. Se separa del diagnóstico pulpar para que el alumno razone ambos tejidos de forma independiente.",
                    "Observe the radiographic schematic and select findings. It is separated from pulpal diagnosis so both tissues are reasoned independently.")
            )
        }
        item {
            SectionCard(tr(lang, "Esquema radiográfico didáctico", "Teaching radiographic schematic")) {
                RadiographSchematic(a)
                Text(tr(lang,
                    "El dibujo no representa una radiografía de un paciente; solo resalta visualmente el ápice, el espacio del ligamento periodontal y posibles cambios radiolúcidos/radiopacos.",
                    "The drawing is not a patient radiograph; it only highlights the apex, periodontal-ligament space and possible radiolucent/radiopaque changes."))
            }
        }
        item {
            SectionCard(tr(lang, "Signos clínicos periapicales", "Periapical clinical signs")) {
                BooleanRow(tr(lang, "Dolor a la percusión o masticación", "Pain to percussion or biting"), a.percussionPain) { update(a.copy(percussionPain = it)) }
                BooleanRow(tr(lang, "Dolor a la palpación apical", "Apical palpation pain"), a.palpationPain) { update(a.copy(palpationPain = it)) }
                BooleanRow(tr(lang, "Aumento de volumen", "Swelling"), a.swelling) { update(a.copy(swelling = it)) }
                BooleanRow(tr(lang, "Fístula / tracto sinuoso", "Sinus tract"), a.fistula) { update(a.copy(fistula = it)) }
            }
        }
        item {
            SectionCard(tr(lang, "Hallazgos radiográficos", "Radiographic findings")) {
                BooleanRow(tr(lang, "Radiolucidez apical", "Apical radiolucency"), a.apicalRadiolucency) { update(a.copy(apicalRadiolucency = it)) }
                BooleanRow(tr(lang, "Ensanchamiento del ligamento periodontal", "Widened periodontal ligament"), a.widenedPdl) { update(a.copy(widenedPdl = it)) }
                BooleanRow(tr(lang, "Radiopacidad apical difusa", "Diffuse apical radiopacity"), a.apicalRadiopacity) { update(a.copy(apicalRadiopacity = it)) }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🩻 ${tr(lang, "Más compatible con", "Most compatible with")}", fontWeight = FontWeight.Bold)
                    Text(if (lang == "en") result.apicalEn else result.apicalEs, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(tr(lang,
                        "Compara el resultado con dolor a percusión/palpación, presencia o ausencia de radiolucidez, tumefacción, fístula y cambios del ligamento periodontal.",
                        "Compare the result with percussion/palpation pain, presence or absence of radiolucency, swelling, sinus tract and periodontal-ligament changes."))
                }
            }
        }
    }
}

@Composable
private fun RadiographSchematic(a: PulpalAssessment) {
    Canvas(modifier = Modifier.fillMaxWidth().height(220.dp)) {
        drawRoundRect(Color(0xFF343434), cornerRadius = CornerRadius(22f, 22f))
        val cx = size.width / 2f
        val crownTop = size.height * 0.14f
        val rootEnd = size.height * 0.76f
        drawRoundRect(
            color = Color(0xFFD9D9D9),
            topLeft = Offset(cx - size.width * 0.13f, crownTop),
            size = Size(size.width * 0.26f, size.height * 0.22f),
            cornerRadius = CornerRadius(20f, 20f)
        )
        drawLine(Color(0xFFE8E8E8), Offset(cx - 35f, size.height * 0.34f), Offset(cx - 16f, rootEnd), strokeWidth = 22f)
        drawLine(Color(0xFFE8E8E8), Offset(cx + 35f, size.height * 0.34f), Offset(cx + 16f, rootEnd), strokeWidth = 22f)
        if (a.widenedPdl) {
            drawLine(Color(0xFF111111), Offset(cx - 50f, size.height * 0.35f), Offset(cx - 27f, rootEnd), strokeWidth = 6f)
            drawLine(Color(0xFF111111), Offset(cx + 50f, size.height * 0.35f), Offset(cx + 27f, rootEnd), strokeWidth = 6f)
        }
        if (a.apicalRadiolucency) drawCircle(Color(0xFF111111), radius = 42f, center = Offset(cx, size.height * 0.83f))
        if (a.apicalRadiopacity) drawCircle(Color(0xFFDDDDDD), radius = 48f, center = Offset(cx, size.height * 0.83f))
        drawCircle(Color(0xFFBDBDBD), radius = 7f, center = Offset(cx, size.height * 0.79f))
    }
}

private data class PostureGuide(val icon: String, val es: String, val en: String, val cluesEs: String, val cluesEn: String)

@Composable
fun PostureScreen(lang: String, onBack: () -> Unit) {
    var selected by remember { mutableStateOf(0) }
    val postures = listOf(
        PostureGuide("🧍", "Posición natural / equilibrada", "Natural / balanced head posture",
            "Cabeza erguida, relación visual equilibrada entre cráneo y cuello; sirve como referencia para observar perfil y simetría.",
            "Upright head with a visually balanced skull-neck relationship; used as a reference for profile and symmetry."),
        PostureGuide("↗️", "Protracción cefálica / cabeza adelantada", "Forward head posture",
            "La cabeza se observa adelantada respecto al tronco. Revisa compensaciones cervicales y posición mandibular sin diagnosticar solo por la apariencia.",
            "The head appears forward relative to the trunk. Review cervical compensation and mandibular position without diagnosing from appearance alone."),
        PostureGuide("↘️", "Flexión de la cabeza", "Head flexion",
            "El mentón y el plano facial tienden a dirigirse hacia abajo. Diferénciala de una postura natural tomada con la cabeza inclinada accidentalmente.",
            "The chin and facial plane tend downward. Distinguish it from a natural posture accidentally recorded with the head tilted."),
        PostureGuide("↖️", "Extensión de la cabeza", "Head extension",
            "El mentón se eleva y el cráneo rota hacia atrás. Observa su relación con cuello, vía aérea y postura mandibular.",
            "The chin elevates and the skull rotates backward. Observe its relation to the neck, airway and mandibular posture."),
        PostureGuide("〰️", "Rectificación cervical", "Cervical straightening",
            "La curvatura cervical fisiológica se aprecia disminuida. En una valoración formal debe correlacionarse con exploración y estudios apropiados.",
            "The physiologic cervical curve appears reduced. Formal assessment requires correlation with examination and appropriate studies."),
        PostureGuide("🌙", "Lordosis cervical aumentada", "Increased cervical lordosis",
            "La curvatura cervical se observa más acentuada. Se describe el hallazgo y se evita convertirlo por sí solo en un diagnóstico etiológico.",
            "The cervical curve appears more pronounced. Describe the finding without turning it alone into an etiologic diagnosis.")
    )

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Exploración craneocervical y postura", "Craniocervical posture"),
                onBack,
                tr(lang,
                    "Selecciona cada postura para aprender cómo describirla. Esta sección enseña observación y redacción, no sustituye una valoración postural o cefalométrica.",
                    "Select each posture to learn how to describe it. This section teaches observation and wording; it does not replace postural or cephalometric assessment.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                postures.take(3).forEachIndexed { index, p ->
                    FilterChip(selected == index, { selected = index }, { Text(p.icon) }, modifier = Modifier.weight(1f))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                postures.drop(3).forEachIndexed { index, p ->
                    val actual = index + 3
                    FilterChip(selected == actual, { selected = actual }, { Text(p.icon) }, modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            val p = postures[selected]
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(p.icon, style = MaterialTheme.typography.headlineMedium)
                    Text(if (lang == "en") p.en else p.es, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("• ${if (lang == "en") p.cluesEn else p.cluesEs}")
                    Text("• ${tr(lang, "Describe lo que observas; evita escribir una causa que no hayas comprobado.", "Describe what you observe; avoid assigning an unverified cause.")}")
                }
            }
        }
        item {
            SectionCard(tr(lang, "Perfil facial dentro de cabeza y cuello", "Facial profile within head and neck examination")) {
                Text("▸ ${tr(lang, "Recto: frente, labios y mentón se observan equilibrados.", "Straight: forehead, lips and chin appear balanced.")}")
                Text("▸ ${tr(lang, "Convexo: el mentón se aprecia relativamente hacia atrás.", "Convex: the chin appears relatively retruded.")}")
                Text("▸ ${tr(lang, "Cóncavo: el mentón se aprecia relativamente prominente o adelantado.", "Concave: the chin appears relatively prominent or forward.")}")
            }
        }
    }
}
