package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.GeneralInspectionState
import com.yomismtz.expedientedeldentista.clinical.HeadNeckExplorationState
import com.yomismtz.expedientedeldentista.clinical.LymphNodeState

private data class ClinicalOptionV24(val code: String, val es: String, val en: String)
private data class LymphChainV24(val id: String, val es: String, val en: String)

private fun optionLabelV24(lang: String, code: String, options: List<ClinicalOptionV24>): String =
    options.firstOrNull { it.code == code }?.let { if (lang == "en") it.en else it.es } ?: tr(lang, "No evaluado", "Not assessed")

@Composable
private fun ChoiceFieldV24(
    lang: String,
    titleEs: String,
    titleEn: String,
    value: String,
    options: List<ClinicalOptionV24>,
    onValue: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(tr(lang, titleEs, titleEn), fontWeight = FontWeight.Bold)
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = value == option.code,
                    onClick = { onValue(option.code) },
                    label = { Text(if (lang == "en") option.en else option.es) }
                )
            }
        }
    }
}

private val apparentAgeV24 = listOf(
    ClinicalOptionV24("CONGRUENT", "Acorde con cronológica", "Consistent with chronological age"),
    ClinicalOptionV24("YOUNGER", "Aparenta menor edad", "Appears younger"),
    ClinicalOptionV24("OLDER", "Aparenta mayor edad", "Appears older"),
    ClinicalOptionV24("UNABLE", "No valorable", "Unable to assess")
)
private val gaitV24 = listOf(
    ClinicalOptionV24("NORMAL", "Sin alteración aparente", "No apparent alteration"),
    ClinicalOptionV24("ANTALGIC", "Marcha antálgica", "Antalgic gait"),
    ClinicalOptionV24("UNSTEADY", "Inestable / atáxica aparente", "Unsteady / apparently ataxic"),
    ClinicalOptionV24("ASSISTED", "Requiere apoyo", "Requires assistance"),
    ClinicalOptionV24("NOT_OBSERVED", "No observada", "Not observed")
)
private val faciesV24 = listOf(
    ClinicalOptionV24("USUAL", "Sin alteración aparente", "No apparent alteration"),
    ClinicalOptionV24("PAIN", "Expresión de dolor", "Pain expression"),
    ClinicalOptionV24("ASYMMETRY", "Asimetría facial", "Facial asymmetry"),
    ClinicalOptionV24("SWELLING", "Aumento de volumen", "Swelling"),
    ClinicalOptionV24("OTHER", "Otra característica", "Other feature")
)
private val attitudeV24 = listOf(
    ClinicalOptionV24("COOPERATIVE", "Cooperadora", "Cooperative"),
    ClinicalOptionV24("PARTIAL", "Parcialmente cooperadora", "Partially cooperative"),
    ClinicalOptionV24("RESTLESS", "Inquieta", "Restless"),
    ClinicalOptionV24("LIMITED", "Cooperación limitada", "Limited cooperation")
)
private val constitutionV24 = listOf(
    ClinicalOptionV24("SLENDER", "Habitus delgado", "Slender habitus"),
    ClinicalOptionV24("INTERMEDIATE", "Habitus intermedio", "Intermediate habitus"),
    ClinicalOptionV24("ROBUST", "Habitus corpulento", "Robust habitus"),
    ClinicalOptionV24("ATHLETIC", "Habitus atlético", "Athletic habitus"),
    ClinicalOptionV24("NOT_RECORDED", "No registrar", "Do not record")
)
private val movementV24 = listOf(
    ClinicalOptionV24("NONE", "Sin movimientos anormales", "No abnormal movements"),
    ClinicalOptionV24("TREMOR", "Temblor", "Tremor"),
    ClinicalOptionV24("TIC", "Tic", "Tic"),
    ClinicalOptionV24("DYSKINESIA", "Movimiento involuntario / discinesia aparente", "Involuntary movement / apparent dyskinesia"),
    ClinicalOptionV24("OTHER", "Otro", "Other")
)
private val consciousnessV24 = listOf(
    ClinicalOptionV24("ALERT", "Alerta y orientada", "Alert and oriented"),
    ClinicalOptionV24("DROWSY", "Somnolienta", "Drowsy"),
    ClinicalOptionV24("CONFUSED", "Confusa / desorientada", "Confused / disoriented"),
    ClinicalOptionV24("ALTERED", "Estado de conciencia alterado", "Altered level of consciousness")
)
private val psychologicalV24 = listOf(
    ClinicalOptionV24("CALM", "Tranquila", "Calm"),
    ClinicalOptionV24("ANXIOUS", "Ansiosa", "Anxious"),
    ClinicalOptionV24("FEARFUL", "Temerosa", "Fearful"),
    ClinicalOptionV24("IRRITABLE", "Irritable", "Irritable"),
    ClinicalOptionV24("OTHER", "Otra", "Other")
)
private val personalCareV24 = listOf(
    ClinicalOptionV24("ADEQUATE", "Aseo y cuidado personal adecuados", "Adequate grooming and personal care"),
    ClinicalOptionV24("LIMITED", "Cuidado personal limitado", "Limited personal care"),
    ClinicalOptionV24("NOT_ASSESSED", "No valorado", "Not assessed")
)

@Composable
fun GeneralInspectionV24Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val state = session.generalInspection
    fun save(next: GeneralInspectionState) = onSessionChanged(session.copy(generalInspection = next))

    ResponsiveScreenV17(
        tr(lang, "IX.2 · Exploración general", "IX.2 · General inspection"),
        tr(lang,
            "Registra observaciones objetivas antes de interpretar. No conviertas una apariencia aislada en un diagnóstico.",
            "Record objective observations before interpretation. Do not turn an isolated appearance into a diagnosis."),
        onBack
    ) { _ ->
        ResponsiveSectionV17(tr(lang, "Observación inicial", "Initial observation")) {
            ChoiceFieldV24(lang, "Edad aparente", "Apparent age", state.apparentAge, apparentAgeV24) { save(state.copy(apparentAge = it)) }
            ChoiceFieldV24(lang, "Marcha", "Gait", state.gait, gaitV24) { save(state.copy(gait = it)) }
            ChoiceFieldV24(lang, "Facies", "Facies", state.facies, faciesV24) { save(state.copy(facies = it)) }
            ChoiceFieldV24(lang, "Actitud", "Attitude", state.attitude, attitudeV24) { save(state.copy(attitude = it)) }
            ChoiceFieldV24(lang, "Constitución / habitus corporal", "Body habitus", state.constitution, constitutionV24) { save(state.copy(constitution = it)) }
        }
        ResponsiveSectionV17(tr(lang, "Estado neurológico y conductual observable", "Observable neurologic and behavioral state")) {
            ChoiceFieldV24(lang, "Movimientos anormales", "Abnormal movements", state.abnormalMovements, movementV24) { save(state.copy(abnormalMovements = it)) }
            ChoiceFieldV24(lang, "Conciencia y orientación", "Consciousness and orientation", state.consciousness, consciousnessV24) { save(state.copy(consciousness = it)) }
            ChoiceFieldV24(lang, "Actitud psicológica observable", "Observable psychological attitude", state.psychologicalAttitude, psychologicalV24) { save(state.copy(psychologicalAttitude = it)) }
            ChoiceFieldV24(lang, "Cuidado personal", "Personal care", state.personalCare, personalCareV24) { save(state.copy(personalCare = it)) }
            ChoiceFieldV24(lang, "Cooperación para el examen", "Cooperation with examination", state.cooperation, attitudeV24) { save(state.copy(cooperation = it)) }
            OutlinedTextField(
                value = state.notes,
                onValueChange = { save(state.copy(notes = it.take(500))) },
                label = { Text(tr(lang, "Descripción adicional", "Additional description")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(tr(lang, "Ejemplo de redacción", "Writing example"), fontWeight = FontWeight.Black)
                Text(
                    tr(lang,
                        "Paciente ${optionLabelV24(lang, state.consciousness, consciousnessV24).lowercase()}, ${optionLabelV24(lang, state.attitude, attitudeV24).lowercase()}; edad aparente ${optionLabelV24(lang, state.apparentAge, apparentAgeV24).lowercase()}; marcha ${optionLabelV24(lang, state.gait, gaitV24).lowercase()}; sin inferir etiología a partir de estos datos aislados.",
                        "Patient ${optionLabelV24(lang, state.consciousness, consciousnessV24).lowercase()}, ${optionLabelV24(lang, state.attitude, attitudeV24).lowercase()}; apparent age ${optionLabelV24(lang, state.apparentAge, apparentAgeV24).lowercase()}; gait ${optionLabelV24(lang, state.gait, gaitV24).lowercase()}; no etiology inferred from isolated observations.")
                )
            }
        }
        NoticeCard(tr(lang,
            "Si existe alteración aguda del estado de conciencia, compromiso neurológico evidente o inestabilidad importante, se prioriza seguridad y valoración médica antes de continuar un examen odontológico electivo.",
            "If there is an acute alteration in consciousness, evident neurologic compromise, or marked instability, prioritize safety and medical assessment before continuing elective dental examination."))
    }
}

private val craniumObservationV24 = listOf(
    ClinicalOptionV24("NO_DEFORMITY", "Sin deformidad evidente", "No evident deformity"),
    ClinicalOptionV24("ASYMMETRY", "Asimetría / aplanamiento", "Asymmetry / flattening"),
    ClinicalOptionV24("ELONGATED", "Configuración alargada", "Elongated configuration"),
    ClinicalOptionV24("BROAD", "Configuración ancha", "Broad configuration"),
    ClinicalOptionV24("OTHER", "Otra alteración de forma", "Other shape alteration")
)
private val hairlineV24 = listOf(
    ClinicalOptionV24("USUAL", "Sin alteración aparente", "No apparent alteration"),
    ClinicalOptionV24("ANTERIOR_HIGH", "Implantación anterior alta", "High anterior hairline"),
    ClinicalOptionV24("ANTERIOR_LOW", "Implantación anterior baja", "Low anterior hairline"),
    ClinicalOptionV24("POSTERIOR_LOW", "Implantación posterior baja", "Low posterior hairline"),
    ClinicalOptionV24("ASYMMETRIC", "Asimétrica", "Asymmetric")
)
private val cranialPalpationV24 = listOf(
    ClinicalOptionV24("NORMAL", "Sin prominencias/depresiones dolorosas", "No painful prominences/depressions"),
    ClinicalOptionV24("PROMINENCE", "Prominencia ósea", "Bony prominence"),
    ClinicalOptionV24("DEPRESSION", "Depresión", "Depression"),
    ClinicalOptionV24("TENDER", "Dolor a la palpación", "Tenderness"),
    ClinicalOptionV24("MASS", "Masa / aumento de volumen", "Mass / swelling")
)
private val profileV24 = listOf(
    ClinicalOptionV24("STRAIGHT", "Recto", "Straight"),
    ClinicalOptionV24("CONVEX", "Convexo", "Convex"),
    ClinicalOptionV24("CONCAVE", "Cóncavo", "Concave"),
    ClinicalOptionV24("NOT_ASSESSED", "No valorado", "Not assessed")
)
private val symmetryV24 = listOf(
    ClinicalOptionV24("SYMMETRIC", "Simetría aparente", "Apparently symmetric"),
    ClinicalOptionV24("ASYMMETRY", "Asimetría", "Asymmetry"),
    ClinicalOptionV24("SWELLING", "Aumento de volumen", "Swelling"),
    ClinicalOptionV24("MASS", "Masa visible/palpable", "Visible/palpable mass")
)
private val skinV24 = listOf(
    ClinicalOptionV24("NORMAL", "Sin alteración aparente", "No apparent alteration"),
    ClinicalOptionV24("LESION", "Lesión cutánea", "Skin lesion"),
    ClinicalOptionV24("COLOR", "Cambio de coloración", "Color change"),
    ClinicalOptionV24("EDEMA", "Edema", "Edema"),
    ClinicalOptionV24("SCAR", "Cicatriz", "Scar")
)
private val facialFunctionV24 = listOf(
    ClinicalOptionV24("SYMMETRIC", "Movimientos simétricos", "Symmetric movements"),
    ClinicalOptionV24("WEAKNESS", "Debilidad / asimetría", "Weakness / asymmetry"),
    ClinicalOptionV24("LIMITED", "Movimiento limitado", "Limited movement"),
    ClinicalOptionV24("INVOLUNTARY", "Movimiento involuntario", "Involuntary movement")
)
private val toneV24 = listOf(
    ClinicalOptionV24("NORMOTONIC", "Normotónico", "Normotonic"),
    ClinicalOptionV24("HYPOTONIC", "Hipotónico", "Hypotonic"),
    ClinicalOptionV24("HYPERTONIC", "Hipertónico", "Hypertonic"),
    ClinicalOptionV24("ASYMMETRIC", "Asimétrico", "Asymmetric")
)
private val masticatoryFindingV24 = listOf(
    ClinicalOptionV24("NONE", "Sin dolor ni aumento de volumen", "No tenderness or swelling"),
    ClinicalOptionV24("MASSETER_TENDER", "Dolor en masetero", "Masseter tenderness"),
    ClinicalOptionV24("TEMPORALIS_TENDER", "Dolor en temporal", "Temporalis tenderness"),
    ClinicalOptionV24("BILATERAL_TENDER", "Dolor bilateral", "Bilateral tenderness"),
    ClinicalOptionV24("HYPERTROPHY", "Hipertrofia / aumento de volumen", "Hypertrophy / enlargement"),
    ClinicalOptionV24("SPASM", "Espasmo / contractura aparente", "Apparent spasm / contracture")
)
private val neckInspectionV24 = listOf(
    ClinicalOptionV24("NORMAL", "Simétrico, sin masas aparentes", "Symmetric, no apparent masses"),
    ClinicalOptionV24("ASYMMETRY", "Asimetría", "Asymmetry"),
    ClinicalOptionV24("SWELLING", "Aumento de volumen", "Swelling"),
    ClinicalOptionV24("MASS", "Masa", "Mass"),
    ClinicalOptionV24("SCAR", "Cicatriz / cambio cutáneo", "Scar / skin change")
)
private val neckMobilityV24 = listOf(
    ClinicalOptionV24("PRESERVED", "Movilidad conservada", "Preserved mobility"),
    ClinicalOptionV24("LIMITED", "Movilidad limitada", "Limited mobility"),
    ClinicalOptionV24("PAINFUL", "Movilidad dolorosa", "Painful mobility")
)
private val yesNoV24 = listOf(
    ClinicalOptionV24("NO", "No", "No"), ClinicalOptionV24("YES", "Sí", "Yes")
)
private val nodeStatusV24 = listOf(
    ClinicalOptionV24("NOT_PALPABLE", "Sin adenopatía palpable", "No palpable adenopathy"),
    ClinicalOptionV24("PALPABLE", "Ganglio palpable", "Palpable node"),
    ClinicalOptionV24("NOT_ASSESSED", "No evaluado", "Not assessed")
)
private val nodeSideV24 = listOf(
    ClinicalOptionV24("RIGHT", "Derecha", "Right"),
    ClinicalOptionV24("LEFT", "Izquierda", "Left"),
    ClinicalOptionV24("BILATERAL", "Bilateral", "Bilateral")
)
private val nodeConsistencyV24 = listOf(
    ClinicalOptionV24("SOFT", "Blanda", "Soft"),
    ClinicalOptionV24("RUBBERY", "Elástica", "Rubbery"),
    ClinicalOptionV24("FIRM", "Firme", "Firm"),
    ClinicalOptionV24("HARD", "Dura", "Hard")
)
private val nodeMobilityV24 = listOf(
    ClinicalOptionV24("MOBILE", "Móvil", "Mobile"),
    ClinicalOptionV24("FIXED", "Fija", "Fixed")
)
private val lymphChainsV24 = listOf(
    LymphChainV24("preauricular", "Preauriculares", "Preauricular"),
    LymphChainV24("postauricular", "Postauriculares / mastoideos", "Postauricular / mastoid"),
    LymphChainV24("occipital", "Occipitales", "Occipital"),
    LymphChainV24("submandibular", "Submandibulares", "Submandibular"),
    LymphChainV24("submental", "Submentonianos", "Submental"),
    LymphChainV24("anterior_cervical", "Cervicales anteriores", "Anterior cervical"),
    LymphChainV24("deep_cervical", "Cervicales profundos", "Deep cervical"),
    LymphChainV24("posterior_cervical", "Cervicales posteriores", "Posterior cervical"),
    LymphChainV24("supraclavicular", "Supraclaviculares", "Supraclavicular")
)

private fun numericClinicalV24(raw: String, max: Int = 7): String = raw
    .replace(',', '.')
    .filter { it.isDigit() || it == '.' }
    .let { filtered ->
        val firstDot = filtered.indexOf('.')
        if (firstDot < 0) filtered else filtered.substring(0, firstDot + 1) + filtered.substring(firstDot + 1).replace(".", "")
    }
    .take(max)

@Composable
fun HeadNeckExplorationV24Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var section by remember { mutableStateOf(0) }
    val state = session.headNeckExploration
    fun save(next: HeadNeckExplorationState) = onSessionChanged(session.copy(headNeckExploration = next))
    val sectionNames = listOf(
        tr(lang, "Cráneo", "Cranium"),
        tr(lang, "Cara", "Face"),
        tr(lang, "Músculos faciales", "Facial muscles"),
        tr(lang, "Músculos masticatorios", "Masticatory muscles"),
        tr(lang, "Cuello", "Neck"),
        tr(lang, "Cadenas ganglionares", "Lymph-node chains")
    )

    ResponsiveScreenV17(
        tr(lang, "IX.3 · Exploración de cabeza y cuello", "IX.3 · Head and neck examination"),
        tr(lang,
            "Secuencia: cráneo → cara → expresión facial → masticación → cuello → cadenas ganglionares.",
            "Sequence: cranium → face → facial expression → mastication → neck → lymph-node chains."),
        onBack
    ) { _ ->
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            sectionNames.forEachIndexed { index, name ->
                FilterChip(section == index, { section = index }, { Text("${index + 1} · $name") })
            }
        }

        when (section) {
            0 -> {
                ResponsiveSectionV17(tr(lang, "IX.3.1 · Cráneo", "IX.3.1 · Cranium")) {
                    ChoiceFieldV24(lang, "Forma observada", "Observed shape", state.craniumObservation, craniumObservationV24) { save(state.copy(craniumObservation = it)) }
                    ChoiceFieldV24(lang, "Implantación del cabello", "Hairline", state.hairline, hairlineV24) { save(state.copy(hairline = it)) }
                    ChoiceFieldV24(lang, "Palpación del contorno craneal", "Cranial contour palpation", state.cranialPalpation, cranialPalpationV24) { save(state.copy(cranialPalpation = it)) }
                }
                ResponsiveSectionV17(tr(lang, "Mediciones opcionales", "Optional measurements")) {
                    OutlinedTextField(
                        state.cranialWidthMm,
                        { save(state.copy(cranialWidthMm = numericClinicalV24(it))) },
                        label = { Text(tr(lang, "Ancho máximo craneal (mm)", "Maximum cranial width (mm)")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        state.cranialLengthMm,
                        { save(state.copy(cranialLengthMm = numericClinicalV24(it))) },
                        label = { Text(tr(lang, "Largo máximo glabela-occipital (mm)", "Maximum glabella-occiput length (mm)")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    val width = state.cranialWidthMm.toDoubleOrNull()
                    val length = state.cranialLengthMm.toDoubleOrNull()
                    if (width != null && length != null && length > 0.0) {
                        val ci = width / length * 100.0
                        Text(tr(lang,
                            "Índice cefálico calculado: ${"%.1f".format(ci)}. Las categorías antropométricas clásicas son sólo una referencia didáctica y varían con la población; no equivalen a diagnóstico.",
                            "Calculated cephalic index: ${"%.1f".format(ci)}. Classical anthropometric categories are educational references only and vary by population; they are not a diagnosis."),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    OutlinedTextField(
                        state.headCircumferenceCm,
                        { save(state.copy(headCircumferenceCm = numericClinicalV24(it))) },
                        label = { Text(tr(lang, "Perímetro cefálico, si está indicado (cm)", "Head circumference, when indicated (cm)")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    NoticeCard(tr(lang,
                        "En pacientes pediátricos, el perímetro cefálico se interpreta por edad y sexo en curvas estandarizadas y, de ser posible, de forma seriada. No uses una tabla fija de promedios para diagnosticar macrocefalia o microcefalia.",
                        "In pediatric patients, head circumference is interpreted by age and sex on standardized charts and, when possible, serially. Do not use a fixed average table to diagnose macrocephaly or microcephaly."))
                }
            }
            1 -> {
                ResponsiveSectionV17(tr(lang, "IX.3.2 · Cara", "IX.3.2 · Face")) {
                    ChoiceFieldV24(lang, "Perfil", "Profile", state.faceProfile, profileV24) { save(state.copy(faceProfile = it)) }
                    ChoiceFieldV24(lang, "Simetría", "Symmetry", state.faceSymmetry, symmetryV24) { save(state.copy(faceSymmetry = it)) }
                    ChoiceFieldV24(lang, "Piel y tejidos blandos", "Skin and soft tissues", state.facialSkin, skinV24) { save(state.copy(facialSkin = it)) }
                    OutlinedTextField(
                        state.notes["face"].orEmpty(),
                        { save(state.copy(notes = state.notes + ("face" to it.take(400)))) },
                        label = { Text(tr(lang, "Describe localización, lado, tamaño o cambios relevantes", "Describe location, side, size, or relevant changes")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            2 -> {
                ResponsiveSectionV17(tr(lang, "IX.3.3 · Músculos de la expresión facial", "IX.3.3 · Muscles of facial expression")) {
                    Text(tr(lang,
                        "Compara ambos lados al elevar cejas, cerrar ojos con fuerza, sonreír, mostrar dientes, fruncir labios e inflar carrillos.",
                        "Compare both sides while raising eyebrows, tightly closing eyes, smiling, showing teeth, pursing lips, and puffing cheeks."))
                    ChoiceFieldV24(lang, "Función", "Function", state.facialMuscleFunction, facialFunctionV24) { save(state.copy(facialMuscleFunction = it)) }
                    ChoiceFieldV24(lang, "Tono aparente", "Apparent tone", state.facialMuscleTone, toneV24) { save(state.copy(facialMuscleTone = it)) }
                    OutlinedTextField(
                        state.notes["facial_muscles"].orEmpty(),
                        { save(state.copy(notes = state.notes + ("facial_muscles" to it.take(400)))) },
                        label = { Text(tr(lang, "Descripción", "Description")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            3 -> {
                ResponsiveSectionV17(tr(lang, "IX.3.4 · Músculos de la masticación", "IX.3.4 · Muscles of mastication")) {
                    Text(tr(lang,
                        "Palpa comparativamente maseteros y temporales en reposo y contracción. La palpación de músculos profundos debe limitarse a maniobras enseñadas y clínicamente justificadas.",
                        "Compare masseters and temporalis at rest and during contraction. Palpation of deep muscles should be limited to taught, clinically justified maneuvers."))
                    ChoiceFieldV24(lang, "Hallazgo principal", "Main finding", state.masticatoryMuscleFinding, masticatoryFindingV24) { save(state.copy(masticatoryMuscleFinding = it)) }
                    ChoiceFieldV24(lang, "Tono / volumen aparente", "Apparent tone / bulk", state.masticatoryMuscleTone, toneV24) { save(state.copy(masticatoryMuscleTone = it)) }
                    OutlinedTextField(
                        state.notes["masticatory"].orEmpty(),
                        { save(state.copy(notes = state.notes + ("masticatory" to it.take(400)))) },
                        label = { Text(tr(lang, "Lado, músculo, dolor y maniobra que lo reproduce", "Side, muscle, pain, and maneuver reproducing it")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            4 -> {
                ResponsiveSectionV17(tr(lang, "IX.3.5 · Cuello", "IX.3.5 · Neck")) {
                    ChoiceFieldV24(lang, "Inspección", "Inspection", state.neckInspection, neckInspectionV24) { save(state.copy(neckInspection = it)) }
                    ChoiceFieldV24(lang, "Movilidad activa", "Active mobility", state.neckMobility, neckMobilityV24) { save(state.copy(neckMobility = it)) }
                    ChoiceFieldV24(lang, "Dolor a la palpación", "Tenderness", state.neckTenderness, yesNoV24) { save(state.copy(neckTenderness = it)) }
                    OutlinedTextField(
                        state.notes["neck"].orEmpty(),
                        { save(state.copy(notes = state.notes + ("neck" to it.take(400)))) },
                        label = { Text(tr(lang, "Descripción adicional", "Additional description")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            else -> LymphNodesV24(lang, state, ::save)
        }
    }
}

@Composable
private fun LymphNodesV24(
    lang: String,
    state: HeadNeckExplorationState,
    save: (HeadNeckExplorationState) -> Unit
) {
    var selectedId by remember { mutableStateOf(lymphChainsV24.first().id) }
    val selected = lymphChainsV24.first { it.id == selectedId }
    val node = state.lymphNodes[selectedId] ?: LymphNodeState()
    fun saveNode(next: LymphNodeState) = save(state.copy(lymphNodes = state.lymphNodes + (selectedId to next)))

    ResponsiveSectionV17(tr(lang, "IX.3.6 · Cadenas ganglionares", "IX.3.6 · Lymph-node chains")) {
        Text(tr(lang,
            "Palpa de forma sistemática y bilateral. Selecciona cada cadena para documentarla.",
            "Palpate systematically and bilaterally. Select each chain to document it."))
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            lymphChainsV24.forEach { chain ->
                FilterChip(
                    selectedId == chain.id,
                    { selectedId = chain.id },
                    { Text(if (lang == "en") chain.en else chain.es) }
                )
            }
        }
        Text(if (lang == "en") selected.en else selected.es, fontWeight = FontWeight.Black)
        ChoiceFieldV24(lang, "Estado", "Status", node.status, nodeStatusV24) {
            saveNode(if (it == "NOT_PALPABLE") LymphNodeState(status = it) else node.copy(status = it))
        }
        if (node.status == "PALPABLE") {
            ChoiceFieldV24(lang, "Lado", "Side", node.side, nodeSideV24) { saveNode(node.copy(side = it)) }
            OutlinedTextField(
                node.sizeMm,
                { saveNode(node.copy(sizeMm = numericClinicalV24(it))) },
                label = { Text(tr(lang, "Tamaño aproximado del mayor ganglio (mm)", "Approximate size of largest node (mm)")) },
                modifier = Modifier.fillMaxWidth()
            )
            ChoiceFieldV24(lang, "Dolor", "Tenderness", node.tenderness, yesNoV24) { saveNode(node.copy(tenderness = it)) }
            ChoiceFieldV24(lang, "Consistencia", "Consistency", node.consistency, nodeConsistencyV24) { saveNode(node.copy(consistency = it)) }
            ChoiceFieldV24(lang, "Movilidad", "Mobility", node.mobility, nodeMobilityV24) { saveNode(node.copy(mobility = it)) }
            OutlinedTextField(
                node.notes,
                { saveNode(node.copy(notes = it.take(300))) },
                label = { Text(tr(lang, "Número, conglomerados, piel suprayacente u otros datos", "Number, matting, overlying skin, or other findings")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    NoticeCard(tr(lang,
        "Ganglios persistentes, progresivos, duros/fijos, supraclaviculares o acompañados de otros datos de alarma requieren valoración clínica apropiada. La app registra hallazgos; no determina la causa.",
        "Persistent, progressive, hard/fixed, supraclavicular nodes or nodes accompanied by other warning findings require appropriate clinical assessment. The app records findings; it does not determine the cause."))
}
