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
private fun PeriodontalFindingImage19(lang:String,kind:String) {
    val ref = when(kind) {
        "bleeding" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_079,"Sangrado después del sondaje","Bleeding after probing")
        "pocket" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_026,"Bolsa periodontal","Periodontal pocket")
        "attachment" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_067,"Pérdida de inserción clínica","Clinical attachment loss")
        "mobility" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_058,"Movilidad dental grados I, II y III","Tooth mobility grades I, II and III")
        "furcation" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_045,"Furcación","Furcation")
        else -> return
    }
    LocalClinicalInlineZoomImageV48(lang,if(lang=="en") ref.third else ref.second,if(lang=="en") ref.third else ref.second,ref.first,"Ejemplo clínico local del hallazgo seleccionado.","Local clinical example of the selected finding.")
}

@Composable
fun PeriodontogramScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedTooth by remember { mutableStateOf(16) }
    val storedRecord = session.periodontogram[selectedTooth] ?: PerioRecord()
    // Older/imported records may contain fewer than six sites. Normalize locally before any UI indexing.
    val record = storedRecord.copy(
        probingDepths = List(6) { storedRecord.probingDepths.getOrElse(it) { 0 } },
        recessionBySite = List(6) { storedRecord.recessionBySite.getOrElse(it) { 0 } },
        bleedingSites = storedRecord.bleedingSites.filter { it in 0..5 }.toSet(),
        plaqueSites = storedRecord.plaqueSites.filter { it in 0..5 }.toSet(),
        suppurationSites = storedRecord.suppurationSites.filter { it in 0..5 }.toSet()
    )
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
        item { SectionCard(tr(lang,"Referencia visual periodontal","Periodontal visual reference")) { UploadedPeriodontalRefsV51(lang) } }
        item {
            ClinicalRegisterHelpV49(lang,"Ayuda · Periodoncia","Help · Periodontics","Selecciona el diente y registra cada sitio medido. Anota profundidad de sondaje, recesión y sangrado, placa o supuración sólo cuando fueron evaluados. Mantén el sitio correcto y los milímetros; el resumen organiza datos y no sustituye la interpretación periodontal.","Select the tooth and record each measured site. Enter probing depth, recession, bleeding, plaque or suppuration only when assessed. Keep the correct site and millimeters; the summary organizes data and does not replace periodontal interpretation.")
            SectionCard(tr(lang, "1 · Selecciona diente", "1 · Select tooth")) {
                DentalArchSelector(ClinicalContent.permanentTeeth, selectedTooth, { selectedTooth = it }) { it in session.periodontogram }
            }
        }
        item {
            SectionCard("2 · OD $selectedTooth · ${tr(lang, "Registro por sitios", "Site recording")}") {
                Text(tr(lang,
                    "Selecciona los milímetros medidos en cada sitio; no es necesario escribirlos.",
                    "Select the measured millimeters at each site; no typing is required."))
                siteNames.forEachIndexed { index, site ->
                    Text(site, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(0,1,2,3,4,5,6,7,8,9,10,12,15).forEach { mm ->
                            FilterChip(
                                selected = record.probingDepths.getOrElse(index) { 0 } == mm,
                                onClick = {
                                    val values = record.probingDepths.take(6).toMutableList()
                                    while (values.size < 6) values.add(0)
                                    values[index] = mm
                                    update(record.copy(probingDepths = values))
                                },
                                label = { Text(mm.toString()) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                if ((record.probingDepths.maxOrNull() ?: 0) >= 4) PeriodontalFindingImage19(lang,"pocket")
                Text(tr(lang, "Margen/recesión gingival por sitio (mm)", "Gingival margin/recession by site (mm)"), fontWeight = FontWeight.SemiBold)
                siteNames.forEachIndexed { index, site ->
                    Text(site, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(-5,-3,-2,-1,0,1,2,3,4,5,7,10,15).forEach { mm ->
                            FilterChip(record.recessionBySite.getOrElse(index) { 0 } == mm, {
                                val values=record.recessionBySite.toMutableList(); while(values.size<6) values.add(0); values[index]=mm
                                update(record.copy(recessionBySite=values,recessionMm=values.maxByOrNull { kotlin.math.abs(it) } ?: 0))
                            }, { Text(mm.toString()) }, modifier=Modifier.weight(1f))
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        FilterChip(index in record.bleedingSites,{val s=record.bleedingSites.toMutableSet();if(!s.add(index))s.remove(index);update(record.copy(bleedingSites=s,bleeding=s.isNotEmpty()))},{Text(tr(lang,"Sangrado","Bleeding"))},modifier=Modifier.weight(1f))
                        FilterChip(index in record.plaqueSites,{val s=record.plaqueSites.toMutableSet();if(!s.add(index))s.remove(index);update(record.copy(plaqueSites=s,plaque=s.isNotEmpty()))},{Text(tr(lang,"Placa","Plaque"))},modifier=Modifier.weight(1f))
                        FilterChip(index in record.suppurationSites,{val s=record.suppurationSites.toMutableSet();if(!s.add(index))s.remove(index);update(record.copy(suppurationSites=s,suppuration=s.isNotEmpty()))},{Text(tr(lang,"Supuración","Suppuration"))},modifier=Modifier.weight(1f))
                    }
                }
                if(record.bleedingSites.isNotEmpty()) PeriodontalFindingImage19(lang,"bleeding")
                if(record.recessionBySite.any { it != 0 }) PeriodontalFindingImage19(lang,"attachment")
                Text(tr(lang, "Movilidad", "Mobility"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..3).forEach { grade ->
                        FilterChip(record.mobility == grade, { update(record.copy(mobility = grade)) }, { Text(grade.toString()) }, modifier = Modifier.weight(1f))
                    }
                }
                if(record.mobility > 0) PeriodontalFindingImage19(lang,"mobility")
                Text(tr(lang, "Furcación", "Furcation"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..3).forEach { grade ->
                        FilterChip(record.furcation == grade, { update(record.copy(furcation = grade)) }, { Text(grade.toString()) }, modifier = Modifier.weight(1f))
                    }
                }
                if(record.furcation > 0) PeriodontalFindingImage19(lang,"furcation")
            }
        }
        item {
            SectionCard(tr(lang,"3 · Resumen periodontal automático","3 · Automatic periodontal summary")) {
                val maxPd = record.probingDepths.maxOrNull() ?: 0
                val bleedingText = if(record.bleeding) tr(lang,"con sangrado al sondaje","with bleeding on probing") else tr(lang,"sin sangrado al sondaje","without bleeding on probing")
                val plaqueText = if(record.plaque) tr(lang,"placa presente","plaque present") else tr(lang,"sin placa marcada","no plaque marked")
                Text(tr(lang,
                    "OD $selectedTooth: profundidad máxima seleccionada $maxPd mm; $bleedingText; $plaqueText; movilidad grado ${record.mobility}; furcación grado ${record.furcation}; margen/recesión ${record.recessionMm} mm.",
                    "Tooth $selectedTooth: selected maximum probing depth $maxPd mm; $bleedingText; $plaqueText; mobility grade ${record.mobility}; furcation grade ${record.furcation}; gingival margin/recession ${record.recessionMm} mm."),fontWeight=FontWeight.Bold)
            }
        }
        item {
            SectionCard(tr(lang,"4 · Periodontograma total · resumen global","4 · Full-mouth periodontal chart · global summary")) {
                val records=session.periodontogram.values
                val measuredSites=records.flatMap { it.probingDepths }.filter { it > 0 }
                val totalSites=measuredSites.size
                val bleedingSites=records.sumOf { if(it.bleedingSites.isNotEmpty()) it.bleedingSites.size else if(it.bleeding) 1 else 0 }
                val plaqueSites=records.sumOf { if(it.plaqueSites.isNotEmpty()) it.plaqueSites.size else if(it.plaque) 1 else 0 }
                val suppurationSites=records.sumOf { if(it.suppurationSites.isNotEmpty()) it.suppurationSites.size else if(it.suppuration) 1 else 0 }
                val bleedingPct=if(totalSites>0) bleedingSites*100f/totalSites else 0f
                val plaquePct=if(totalSites>0) plaqueSites*100f/totalSites else 0f
                Text(tr(lang,"Dientes registrados: ${records.size}. Sitios medidos: $totalSites. Profundidad máxima: ${measuredSites.maxOrNull() ?: 0} mm. Sitios ≥4 mm: ${measuredSites.count { it>=4 }}; ≥6 mm: ${measuredSites.count { it>=6 }}.","Recorded teeth: ${records.size}. Measured sites: $totalSites. Maximum depth: ${measuredSites.maxOrNull() ?: 0} mm. Sites ≥4 mm: ${measuredSites.count { it>=4 }}; ≥6 mm: ${measuredSites.count { it>=6 }}."),fontWeight=FontWeight.Bold)
                Text(tr(lang,"Sangrado: $bleedingSites sitios ("+String.format("%.1f",bleedingPct)+"%); placa: $plaqueSites ("+String.format("%.1f",plaquePct)+"%); supuración: $suppurationSites. Dientes con movilidad: ${records.count { it.mobility>0 }}; furcación: ${records.count { it.furcation>0 }}.","Bleeding: $bleedingSites sites ("+String.format("%.1f",bleedingPct)+"%); plaque: $plaqueSites ("+String.format("%.1f",plaquePct)+"%); suppuration: $suppurationSites. Teeth with mobility: ${records.count { it.mobility>0 }}; furcation: ${records.count { it.furcation>0 }}."))
                Text(tr(lang,"Resumen descriptivo; no asigna automáticamente diagnóstico, estadio ni grado periodontal.","Descriptive summary; it does not automatically assign periodontal diagnosis, stage or grade."),style=MaterialTheme.typography.bodySmall)
            }
        }
        item { NoticeCard(ClinicalEngines.periodontalSummary(session, lang)) }
    }
}

@Composable
private fun PulpalClinicalImage19(lang:String,kind:String) {
    val r = when(kind) {
        "cold" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_013,"Prueba de frío","Cold test")
        "cold_increased" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_020,"Respuesta aumentada al frío","Increased cold response")
        "cold_lingering" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_021,"Respuesta persistente al frío","Lingering cold response")
        "heat" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_012,"Prueba de calor","Heat test")
        "negative" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_002,"Ausencia de respuesta","No response")
        "normal" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_016,"Pulpa clínicamente normal","Clinically normal pulp")
        else -> return
    }
    LocalClinicalInlineZoomImageV48(lang,if(lang=="en") r.third else r.second,if(lang=="en") r.third else r.second,r.first,"Imagen clínica local para apoyar la interpretación de la prueba seleccionada; no sustituye el diagnóstico.","Local clinical image supporting interpretation of the selected test; it does not replace diagnosis.")
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
                if(a.coldPositive) PulpalClinicalImage19(lang, if(a.coldLingering) "cold_lingering" else "cold_increased")
                BooleanRow(tr(lang, "El dolor al frío persiste después de retirar el estímulo", "Cold pain lingers after the stimulus is removed"), a.coldLingering) { update(a.copy(coldLingering = it)) }
                if(a.coldLingering) PulpalClinicalImage19(lang,"cold_lingering")
                BooleanRow(tr(lang, "Respuesta dolorosa al calor", "Painful response to heat"), a.heatPositive) { update(a.copy(heatPositive = it)) }
                if(a.heatPositive) PulpalClinicalImage19(lang,"heat")
                BooleanRow(tr(lang, "No responde a pruebas de sensibilidad", "No response to sensitivity testing"), a.sensitivityNegative) { update(a.copy(sensitivityNegative = it)) }
                if(a.sensitivityNegative) PulpalClinicalImage19(lang,"negative")
                if(!a.coldPositive && !a.coldLingering && !a.heatPositive && !a.sensitivityNegative) PulpalClinicalImage19(lang,"normal")
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
private fun ApicalClinicalImage19(lang:String,kind:String) {
    val r = when(kind) {
        "percussion" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_042,"Dolor a la percusión","Percussion pain")
        "palpation" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_003,"Dolor a la palpación","Palpation pain")
        "swelling" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_006,"Inflamación/absceso localizado","Localized swelling/abscess")
        "fistula" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_005,"Fístula","Sinus tract")
        "radiolucency" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_017,"Radiolucidez periapical","Periapical radiolucency")
        "pdl" -> Triple(com.yomismtz.expedientedeldentista.R.drawable.uploaded80_004,"Ensanchamiento del ligamento periodontal","Widened periodontal ligament")
        else -> return
    }
    LocalClinicalInlineZoomImageV48(lang,if(lang=="en") r.third else r.second,if(lang=="en") r.third else r.second,r.first,"Imagen clínica local relacionada con el hallazgo seleccionado; correlaciona con exploración y estudios.","Local clinical image related to the selected finding; correlate with examination and studies.")
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
                if(a.percussionPain) ApicalClinicalImage19(lang,"percussion")
                BooleanRow(tr(lang, "Dolor a la palpación apical", "Apical palpation pain"), a.palpationPain) { update(a.copy(palpationPain = it)) }
                if(a.palpationPain) ApicalClinicalImage19(lang,"palpation")
                BooleanRow(tr(lang, "Aumento de volumen", "Swelling"), a.swelling) { update(a.copy(swelling = it)) }
                if(a.swelling) ApicalClinicalImage19(lang,"swelling")
                BooleanRow(tr(lang, "Fístula / tracto sinuoso", "Sinus tract"), a.fistula) { update(a.copy(fistula = it)) }
                if(a.fistula) ApicalClinicalImage19(lang,"fistula")
            }
        }
        item {
            SectionCard(tr(lang, "Hallazgos radiográficos", "Radiographic findings")) {
                BooleanRow(tr(lang, "Radiolucidez apical", "Apical radiolucency"), a.apicalRadiolucency) { update(a.copy(apicalRadiolucency = it)) }
                if(a.apicalRadiolucency) ApicalClinicalImage19(lang,"radiolucency")
                BooleanRow(tr(lang, "Ensanchamiento del ligamento periodontal", "Widened periodontal ligament"), a.widenedPdl) { update(a.copy(widenedPdl = it)) }
                if(a.widenedPdl) ApicalClinicalImage19(lang,"pdl")
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
