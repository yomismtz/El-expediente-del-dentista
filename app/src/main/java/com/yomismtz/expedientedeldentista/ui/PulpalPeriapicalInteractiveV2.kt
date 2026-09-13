package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.PulpalAssessment

private val DxLavender = Color(0xFFE9DDF5)
private val DxLilac = Color(0xFFD3BCE9)
private val DxPurple = Color(0xFF7447A3)
private val DxDeep = Color(0xFF43235F)
private val DxMint = Color(0xFF66D6C7)
private val DxTurquoise = Color(0xFF2EB9B1)
private val DxPaper = Color(0xFFFFFCFF)

@Composable
fun PulpalPeriapicalInteractiveV2Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onOpenEndo: () -> Unit,
    onBack: () -> Unit
) {
    var primary by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    var selectedTooth by remember { mutableStateOf(session.pulpal.tooth.takeIf { it in shown } ?: shown.first()) }
    if (selectedTooth !in shown) selectedTooth = shown.first()

    val assessment = session.pulpal.copy(tooth = selectedTooth)
    val result = ClinicalEngines.pulpalDiagnosis(assessment)

    fun update(updated: PulpalAssessment) {
        onSessionChanged(session.copy(pulpal = updated.copy(tooth = selectedTooth)))
    }

    val missingTestsEs = buildList {
        if (!assessment.coldPositive && !assessment.sensitivityNegative) add("prueba de sensibilidad al frío o prueba equivalente comparativa")
        if (!assessment.percussionPain && !assessment.palpationPain) add("percusión y palpación apical documentadas")
        if (!assessment.apicalRadiolucency && !assessment.widenedPdl && !assessment.apicalRadiopacity) add("valoración radiográfica cuando esté indicada")
        if (!assessment.deepCariesOrExposure && !assessment.previousRootCanal && !assessment.previousPartialEndo) add("correlación con profundidad de lesión/restauración y antecedentes endodónticos")
    }
    val missingTestsEn = buildList {
        if (!assessment.coldPositive && !assessment.sensitivityNegative) add("cold sensitivity or an equivalent comparative test")
        if (!assessment.percussionPain && !assessment.palpationPain) add("documented percussion and apical palpation")
        if (!assessment.apicalRadiolucency && !assessment.widenedPdl && !assessment.apicalRadiopacity) add("radiographic assessment when indicated")
        if (!assessment.deepCariesOrExposure && !assessment.previousRootCanal && !assessment.previousPartialEndo) add("correlation with lesion/restoration depth and endodontic history")
    }

    val pulpalText = if (lang == "en") result.pulpalEn else result.pulpalEs
    val apicalText = if (lang == "en") result.apicalEn else result.apicalEs
    val explanation = if (lang == "en") result.explanationEn else result.explanationEs
    val missing = if (lang == "en") missingTestsEn else missingTestsEs
    val writeExample = tr(
        lang,
        "OD $selectedTooth: diagnóstico pulpar más compatible con $pulpalText; diagnóstico periapical más compatible con $apicalText. Hallazgos que apoyan: ${result.explanationEs.removePrefix("Hallazgos que orientan el resultado: ").substringBefore(". Es una ayuda educativa")}",
        "Tooth $selectedTooth: pulpal diagnosis most compatible with $pulpalText; apical diagnosis most compatible with $apicalText. Supporting findings: ${result.explanationEn.removePrefix("Findings supporting the result: ").substringBefore(". This is an educational aid")}" 
    )

    LazyColumn(
        Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .background(Brush.verticalGradient(listOf(DxLavender, Color(0xFFF9F5FC), Color.White)))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Diagnóstico pulpar + periapical", "Pulpal + apical diagnosis"),
                onBack,
                tr(
                    lang,
                    "Selecciona el órgano dentario, marca síntomas y pruebas, y compara dos resultados separados: uno pulpar y otro periapical. La orientación es educativa y no sustituye la valoración clínica completa.",
                    "Select the tooth, mark symptoms and tests, and compare two separate results: one pulpal and one apical. This educational orientation does not replace a complete clinical assessment."
                )
            )
        }

        item {
            SectionCard(tr(lang, "1 · Órgano dentario", "1 · Tooth")) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(!primary, { primary = false; selectedTooth = ClinicalContent.permanentTeeth.first() }, { Text(tr(lang, "Permanente", "Permanent")) })
                    FilterChip(primary, { primary = true; selectedTooth = ClinicalContent.primaryTeeth.first() }, { Text(tr(lang, "Temporal", "Primary")) })
                }
                DentalArchSelector(shown, selectedTooth, { selectedTooth = it; update(session.pulpal.copy(tooth = it)) }) { it == session.pulpal.tooth }
            }
        }

        item {
            SectionCard(tr(lang, "2 · Dolor y síntomas", "2 · Pain and symptoms")) {
                BooleanRow(tr(lang, "Dolor espontáneo", "Spontaneous pain"), assessment.spontaneousPain) { update(assessment.copy(spontaneousPain = it)) }
                BooleanRow(tr(lang, "Dolor nocturno", "Night pain"), assessment.nightPain) { update(assessment.copy(nightPain = it)) }
                BooleanRow(tr(lang, "Dolor con dulces", "Pain with sweets"), assessment.sweetsPain) { update(assessment.copy(sweetsPain = it)) }
                BooleanRow(tr(lang, "Caries profunda o exposición pulpar", "Deep caries or pulpal exposure"), assessment.deepCariesOrExposure) { update(assessment.copy(deepCariesOrExposure = it)) }
            }
        }

        item {
            SectionCard(tr(lang, "3 · Pruebas pulpares", "3 · Pulp tests")) {
                BooleanRow(tr(lang, "Respuesta al frío", "Cold response"), assessment.coldPositive) { update(assessment.copy(coldPositive = it)) }
                BooleanRow(tr(lang, "Dolor al frío persiste tras retirar el estímulo", "Cold pain lingers after stimulus removal"), assessment.coldLingering) { update(assessment.copy(coldLingering = it)) }
                BooleanRow(tr(lang, "Respuesta dolorosa al calor", "Painful heat response"), assessment.heatPositive) { update(assessment.copy(heatPositive = it)) }
                BooleanRow(tr(lang, "Sin respuesta a pruebas de sensibilidad", "No response to sensitivity testing"), assessment.sensitivityNegative) { update(assessment.copy(sensitivityNegative = it)) }
                BooleanRow(tr(lang, "Tratamiento de conductos previo", "Previous root-canal treatment"), assessment.previousRootCanal) { update(assessment.copy(previousRootCanal = it)) }
                BooleanRow(tr(lang, "Terapia endodóntica previamente iniciada", "Previously initiated endodontic therapy"), assessment.previousPartialEndo) { update(assessment.copy(previousPartialEndo = it)) }
            }
        }

        item {
            SectionCard(tr(lang, "4 · Pruebas y signos periapicales", "4 · Apical tests and signs")) {
                BooleanRow(tr(lang, "Dolor a la percusión o masticación", "Pain to percussion or biting"), assessment.percussionPain) { update(assessment.copy(percussionPain = it)) }
                BooleanRow(tr(lang, "Dolor a la palpación apical", "Apical palpation pain"), assessment.palpationPain) { update(assessment.copy(palpationPain = it)) }
                BooleanRow(tr(lang, "Aumento de volumen", "Swelling"), assessment.swelling) { update(assessment.copy(swelling = it)) }
                BooleanRow(tr(lang, "Fístula / tracto sinuoso", "Sinus tract"), assessment.fistula) { update(assessment.copy(fistula = it)) }
                BooleanRow(tr(lang, "Radiolucidez apical", "Apical radiolucency"), assessment.apicalRadiolucency) { update(assessment.copy(apicalRadiolucency = it)) }
                BooleanRow(tr(lang, "Ensanchamiento del ligamento periodontal", "Widened periodontal ligament"), assessment.widenedPdl) { update(assessment.copy(widenedPdl = it)) }
                BooleanRow(tr(lang, "Radiopacidad apical difusa", "Diffuse apical radiopacity"), assessment.apicalRadiopacity) { update(assessment.copy(apicalRadiopacity = it)) }
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = DxLilac.copy(alpha = .45f)),
                    border = BorderStroke(1.dp, DxPurple),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("⚡ ${tr(lang, "PULPAR", "PULPAL")}", fontWeight = FontWeight.Black, color = DxDeep)
                        Text(pulpalText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = DxMint.copy(alpha = .28f)),
                    border = BorderStroke(1.dp, DxTurquoise),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("◎ ${tr(lang, "PERIAPICAL", "APICAL")}", fontWeight = FontWeight.Black, color = DxDeep)
                        Text(apicalText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        item {
            SectionCard(tr(lang, "¿Por qué sugiere esto?", "Why does it suggest this?")) {
                Text(explanation)
                Text(
                    tr(
                        lang,
                        "No uses un solo síntoma para decidir. Compara el diente problema con dientes control, correlaciona anamnesis, pruebas pulpares, percusión/palpación, examen clínico y radiografía cuando corresponda.",
                        "Do not decide from a single symptom. Compare the problem tooth with control teeth and correlate history, pulp tests, percussion/palpation, clinical examination and radiography when appropriate."
                    ),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        item {
            SectionCard(tr(lang, "¿Qué pruebas faltan?", "Which tests are missing?")) {
                if (missing.isEmpty()) {
                    Text(tr(lang, "No se detectan grupos básicos sin explorar en este ejercicio; confirma que las pruebas se realizaron de forma correcta y comparativa.", "No basic test group appears unassessed in this exercise; confirm that tests were performed correctly and comparatively."))
                } else {
                    missing.forEach { Text("• $it") }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DxMint.copy(alpha = .20f)),
                border = BorderStroke(1.dp, DxTurquoise),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("✍️ ${tr(lang, "¿Qué escribo al final en el expediente?", "What do I write in the record?")}", fontWeight = FontWeight.Black, color = DxDeep)
                    Text(writeExample, fontWeight = FontWeight.SemiBold)
                    Text(tr(lang, "Si faltan pruebas, agrega: “orientación diagnóstica pendiente de completar pruebas pulpares/periapicales”.", "If tests are missing, add: “diagnostic orientation pending completion of pulpal/apical tests”."))
                }
            }
        }

        item {
            NoticeCard(
                tr(
                    lang,
                    "El resultado se expresa como “más compatible con”. La app enseña razonamiento y redacción; no establece por sí sola un diagnóstico clínico definitivo ni reemplaza la supervisión docente.",
                    "The result is phrased as “most compatible with”. The app teaches reasoning and documentation; it does not establish a definitive clinical diagnosis or replace faculty supervision."
                )
            )
        }

        item {
            Button(onClick = onOpenEndo, modifier = Modifier.fillMaxWidth()) {
                Text(tr(lang, "Continuar a ficha endodóntica →", "Continue to endodontic sheet →"))
            }
            Spacer(Modifier.height(18.dp))
        }
    }
}
