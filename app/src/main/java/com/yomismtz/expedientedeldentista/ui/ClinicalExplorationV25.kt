package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AtmExamState
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.HeadNeckExplorationState
import com.yomismtz.expedientedeldentista.clinical.LymphNodeState
import kotlin.math.roundToInt

private data class ChoiceV25(val code: String, val es: String, val en: String)
private data class SectionV25(val icon: String, val es: String, val en: String)
private data class ManeuverV25(val id: String, val es: String, val en: String, val muscleEs: String, val muscleEn: String, val observeEs: String, val observeEn: String)
private data class LymphChainV25(val id: String, val es: String, val en: String, val relationEs: String, val relationEn: String)

private val headNeckSectionsV25 = listOf(
    SectionV25("🧠", "Cráneo", "Cranium"),
    SectionV25("🙂", "Cara", "Face"),
    SectionV25("😀", "Expresión facial", "Facial expression"),
    SectionV25("💪", "Masticación", "Mastication"),
    SectionV25("🧣", "Cuello", "Neck"),
    SectionV25("🔵", "Ganglios", "Lymph nodes")
)

private val profileChoicesV25 = listOf(
    ChoiceV25("STRAIGHT", "Recto", "Straight"),
    ChoiceV25("CONVEX", "Convexo", "Convex"),
    ChoiceV25("CONCAVE", "Cóncavo", "Concave"),
    ChoiceV25("NOT_ASSESSED", "No valorado", "Not assessed")
)
private val symmetryChoicesV25 = listOf(
    ChoiceV25("SYMMETRIC", "Equilibrada / simetría conservada", "Balanced / symmetry preserved"),
    ChoiceV25("MILD", "Leve asimetría", "Mild asymmetry"),
    ChoiceV25("MANDIBULAR", "Mentón desviado", "Chin deviation"),
    ChoiceV25("SWELLING", "Aumento de volumen", "Swelling"),
    ChoiceV25("WEAKNESS", "Un lado más bajo o débil", "One side lower or weak")
)
private val skinToneChoicesV25 = listOf(
    ChoiceV25("VERY_LIGHT", "Muy clara", "Very light"),
    ChoiceV25("LIGHT", "Clara", "Light"),
    ChoiceV25("LIGHT_BROWN", "Morena clara", "Light brown"),
    ChoiceV25("MEDIUM_BROWN", "Morena media", "Medium brown"),
    ChoiceV25("DARK_BROWN", "Morena oscura", "Dark brown"),
    ChoiceV25("DARK", "Oscura", "Dark")
)
private val skinChangeChoicesV25 = listOf(
    ChoiceV25("NORMAL", "Normocrómica", "No abnormal color change"),
    ChoiceV25("PALE", "Palidez aparente", "Apparent pallor"),
    ChoiceV25("YELLOW", "Tinte amarillento / ictérico aparente", "Apparent yellow / icteric tint"),
    ChoiceV25("BLUE", "Coloración azulada / cianótica aparente", "Apparent bluish / cyanotic color"),
    ChoiceV25("RED", "Eritema / rubicundez", "Erythema / flushing"),
    ChoiceV25("OTHER", "Otro cambio", "Other change")
)
private val facialFunctionChoicesV25 = listOf(
    ChoiceV25("SYMMETRIC", "Movilidad simétrica conservada", "Symmetric mobility preserved"),
    ChoiceV25("RIGHT_WEAK", "Debilidad derecha", "Right weakness"),
    ChoiceV25("LEFT_WEAK", "Debilidad izquierda", "Left weakness"),
    ChoiceV25("BILATERAL_WEAK", "Debilidad bilateral", "Bilateral weakness"),
    ChoiceV25("INVOLUNTARY", "Movimiento involuntario", "Involuntary movement")
)
private val toneChoicesV25 = listOf(
    ChoiceV25("NORMOTONIC", "Normotónico", "Normotonic"),
    ChoiceV25("HYPOTONIC", "Hipotónico", "Hypotonic"),
    ChoiceV25("HYPERTONIC", "Hipertónico", "Hypertonic"),
    ChoiceV25("ASYMMETRIC", "Asimétrico", "Asymmetric")
)
private val masticatoryFindingChoicesV25 = listOf(
    ChoiceV25("NONE", "Sin dolor ni hipertrofia aparente", "No apparent tenderness or hypertrophy"),
    ChoiceV25("MASSETER", "Dolor en masetero", "Masseter tenderness"),
    ChoiceV25("TEMPORALIS", "Dolor en temporal", "Temporalis tenderness"),
    ChoiceV25("BILATERAL", "Dolor bilateral", "Bilateral tenderness"),
    ChoiceV25("HYPERTROPHY", "Hipertrofia / aumento de volumen", "Hypertrophy / enlargement"),
    ChoiceV25("SPASM", "Espasmo / contractura aparente", "Apparent spasm / contracture")
)
private val neckInspectionChoicesV25 = listOf(
    ChoiceV25("NORMAL", "Simétrico, sin masas aparentes", "Symmetric, no apparent masses"),
    ChoiceV25("ASYMMETRY", "Asimetría", "Asymmetry"),
    ChoiceV25("SWELLING", "Aumento de volumen", "Swelling"),
    ChoiceV25("MASS", "Masa", "Mass"),
    ChoiceV25("FISTULA", "Fístula / trayecto cutáneo", "Cutaneous sinus tract"),
    ChoiceV25("SKIN", "Cambio cutáneo", "Skin change")
)
private val neckMobilityChoicesV25 = listOf(
    ChoiceV25("PRESERVED", "Movilidad conservada", "Preserved mobility"),
    ChoiceV25("LIMITED", "Movilidad limitada", "Limited mobility"),
    ChoiceV25("PAINFUL", "Movilidad dolorosa", "Painful mobility")
)
private val yesNoChoicesV25 = listOf(
    ChoiceV25("NO", "No", "No"), ChoiceV25("YES", "Sí", "Yes")
)
private val tracheaChoicesV25 = listOf(
    ChoiceV25("CENTRAL", "Central", "Midline"),
    ChoiceV25("DEVIATED", "Desviada", "Deviated"),
    ChoiceV25("NOT_ASSESSED", "No valorada", "Not assessed")
)
private val thyroidChoicesV25 = listOf(
    ChoiceV25("NO_CHANGE", "Sin aumento aparente", "No apparent enlargement"),
    ChoiceV25("ENLARGED", "Aumento aparente", "Apparent enlargement"),
    ChoiceV25("NODULE", "Nódulo / asimetría aparente", "Apparent nodule / asymmetry"),
    ChoiceV25("NOT_ASSESSED", "No valorada", "Not assessed")
)
private val nodeStatusChoicesV25 = listOf(
    ChoiceV25("NOT_PALPABLE", "No palpable", "Not palpable"),
    ChoiceV25("PALPABLE", "Palpable", "Palpable"),
    ChoiceV25("NOT_ASSESSED", "No evaluado", "Not assessed")
)
private val sideChoicesV25 = listOf(
    ChoiceV25("RIGHT", "Derecha", "Right"),
    ChoiceV25("LEFT", "Izquierda", "Left"),
    ChoiceV25("BILATERAL", "Bilateral", "Bilateral")
)
private val consistencyChoicesV25 = listOf(
    ChoiceV25("SOFT", "Blanda", "Soft"),
    ChoiceV25("RUBBERY", "Elástica", "Rubbery"),
    ChoiceV25("FIRM", "Firme", "Firm"),
    ChoiceV25("HARD", "Dura", "Hard")
)
private val mobilityChoicesV25 = listOf(
    ChoiceV25("MOBILE", "Móvil", "Mobile"),
    ChoiceV25("FIXED", "Fija / adherida", "Fixed / adherent")
)

private val facialManeuversV25 = listOf(
    ManeuverV25("brows", "Levanta las cejas", "Raise eyebrows", "Frontal / occipitofrontal", "Frontal / occipitofrontal", "Compara ascenso de ambas cejas.", "Compare elevation of both eyebrows."),
    ManeuverV25("eyes", "Cierra fuerte los ojos", "Close eyes tightly", "Orbicular de los ojos", "Orbicularis oculi", "Valora cierre completo y fuerza.", "Assess complete closure and strength."),
    ManeuverV25("smile", "Sonríe", "Smile", "Cigomáticos y risorio", "Zygomatic muscles and risorius", "Compara ascenso de las comisuras.", "Compare elevation of the mouth corners."),
    ManeuverV25("teeth", "Muestra los dientes", "Show teeth", "Elevador del labio superior y cigomáticos", "Upper lip elevator and zygomatic muscles", "Compara exposición derecha/izquierda.", "Compare right/left exposure."),
    ManeuverV25("cheeks", "Infla los carrillos", "Puff cheeks", "Buccinador y orbicular de los labios", "Buccinator and orbicularis oris", "Comprueba que mantiene aire sin fuga.", "Check that air is retained without leakage."),
    ManeuverV25("purse", "Frunce los labios", "Purse lips", "Orbicular de los labios", "Orbicularis oris", "Valora sellado y protrusión simétrica.", "Assess seal and symmetric protrusion."),
    ManeuverV25("lower_lip", "Baja el labio inferior", "Lower the bottom lip", "Depresores del labio inferior", "Lower lip depressors", "Compara el movimiento de ambos lados.", "Compare movement on both sides."),
    ManeuverV25("nose", "Arruga la nariz", "Wrinkle nose", "Nasal / elevador del labio superior", "Nasalis / upper lip elevator", "Compara la contracción bilateral.", "Compare bilateral contraction.")
)

private val lymphChainsV25 = listOf(
    LymphChainV25("preauricular", "Preauriculares", "Preauricular", "Ojo, oído, piel de región temporal y vecina.", "Eye, ear, temporal and adjacent skin."),
    LymphChainV25("postauricular", "Postauriculares / mastoideos", "Postauricular / mastoid", "Oído y cuero cabelludo adyacente.", "Ear and adjacent scalp."),
    LymphChainV25("occipital", "Occipitales", "Occipital", "Cuero cabelludo posterior.", "Posterior scalp."),
    LymphChainV25("submandibular", "Submandibulares", "Submandibular", "Dientes, periodonto y estructuras bucales que drenan a esta región.", "Teeth, periodontium and oral structures draining to this region."),
    LymphChainV25("submental", "Submentonianos", "Submental", "Incisivos inferiores, labio inferior y piso de boca anterior.", "Lower incisors, lower lip and anterior floor of mouth."),
    LymphChainV25("anterior_cervical", "Cervicales anteriores", "Anterior cervical", "Cabeza, cuello y vías aerodigestivas según nivel de drenaje.", "Head, neck and aerodigestive tract according to drainage level."),
    LymphChainV25("deep_cervical", "Cervicales profundos", "Deep cervical", "Cadena profunda de drenaje de cabeza y cuello.", "Deep drainage chain of head and neck."),
    LymphChainV25("posterior_cervical", "Cervicales posteriores", "Posterior cervical", "Cuero cabelludo y estructuras posteriores del cuello.", "Scalp and posterior neck structures."),
    LymphChainV25("supraclavicular", "Supraclaviculares", "Supraclavicular", "Su aumento requiere valoración clínica cuidadosa.", "Enlargement warrants careful clinical assessment.")
)

private fun cleanNumberV25(raw: String, max: Int = 6): String = raw
    .replace(',', '.')
    .filter { it.isDigit() || it == '.' }
    .let { filtered ->
        val firstDot = filtered.indexOf('.')
        if (firstDot < 0) filtered else filtered.substring(0, firstDot + 1) + filtered.substring(firstDot + 1).replace(".", "")
    }
    .take(max)

private fun choiceLabelV25(lang: String, code: String, choices: List<ChoiceV25>): String =
    choices.firstOrNull { it.code == code }?.let { if (lang == "en") it.en else it.es } ?: tr(lang, "No evaluado", "Not assessed")

@Composable
private fun ChoiceRowV25(
    lang: String,
    titleEs: String,
    titleEn: String,
    value: String,
    choices: List<ChoiceV25>,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(tr(lang, titleEs, titleEn), fontWeight = FontWeight.Bold)
        choices.forEach { item ->
            FilterChip(
                selected = value == item.code,
                onClick = { onSelect(item.code) },
                label = { Text(if (lang == "en") item.en else item.es) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TeachingBoxV25(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .65f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, fontWeight = FontWeight.Black)
            Text(body)
        }
    }
}

@Composable
fun HeadNeckExplorationV25Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onOpenAtm: () -> Unit,
    onBack: () -> Unit
) {
    var section by remember { mutableStateOf(0) }
    val state = session.headNeckExploration
    fun save(next: HeadNeckExplorationState) = onSessionChanged(session.copy(headNeckExploration = next))
    fun saveNote(key: String, value: String) = save(state.copy(notes = state.notes + (key to value)))

    ResponsiveScreenV17(
        tr(lang, "IX.3 · Exploración de cabeza y cuello", "IX.3 · Head and neck examination"),
        tr(lang,
            "Usa los botones por apartado. La secuencia sugerida es cráneo → cara → expresión facial → masticación → cuello → cadenas ganglionares → ATM.",
            "Use the section buttons. Suggested sequence: cranium → face → facial expression → mastication → neck → lymph nodes → TMJ."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang, "Apartados", "Sections")) {
            val columns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
            AdaptiveGridV17(headNeckSectionsV25.size, columns) { index ->
                val item = headNeckSectionsV25[index]
                if (section == index) {
                    Button(onClick = { section = index }, modifier = Modifier.fillMaxWidth()) {
                        Text("${index + 1} · ${item.icon} ${if (lang == "en") item.en else item.es}", textAlign = TextAlign.Center)
                    }
                } else {
                    OutlinedButton(onClick = { section = index }, modifier = Modifier.fillMaxWidth()) {
                        Text("${index + 1} · ${item.icon} ${if (lang == "en") item.en else item.es}", textAlign = TextAlign.Center)
                    }
                }
            }
        }

        when (section) {
            0 -> CraniumV25(lang, state, ::save)
            1 -> FaceV25(lang, state, ::save, ::saveNote)
            2 -> FacialMusclesV25(lang, state, ::save, ::saveNote)
            3 -> MasticatoryMusclesV25(lang, state, ::save, ::saveNote)
            4 -> NeckV25(lang, state, ::save, ::saveNote)
            else -> LymphNodesV25(lang, state, ::save)
        }

        ResponsiveSectionV17(tr(lang, "Navegación clínica", "Clinical navigation")) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    enabled = section > 0,
                    onClick = { if (section > 0) section-- },
                    modifier = Modifier.weight(1f)
                ) { Text("← ${tr(lang, "Anterior", "Previous")}") }
                if (section < headNeckSectionsV25.lastIndex) {
                    Button(onClick = { section++ }, modifier = Modifier.weight(1f)) {
                        Text("${tr(lang, "Siguiente", "Next")} →")
                    }
                } else {
                    Button(onClick = onOpenAtm, modifier = Modifier.weight(1f)) {
                        Text("ATM →")
                    }
                }
            }
        }
    }
}

@Composable
private fun CraniumV25(lang: String, state: HeadNeckExplorationState, save: (HeadNeckExplorationState) -> Unit) {
    val shapes = listOf(
        ChoiceV25("NO_DEFORMITY", "Sin deformidad evidente", "No evident deformity"),
        ChoiceV25("ELONGATED", "Configuración alargada", "Elongated configuration"),
        ChoiceV25("BROAD", "Configuración ancha", "Broad configuration"),
        ChoiceV25("ASYMMETRY", "Asimetría / aplanamiento", "Asymmetry / flattening"),
        ChoiceV25("OTHER", "Otra", "Other")
    )
    val hairline = listOf(
        ChoiceV25("USUAL", "Sin alteración aparente", "No apparent alteration"),
        ChoiceV25("ANTERIOR_HIGH", "Anterior alta", "High anterior"),
        ChoiceV25("ANTERIOR_LOW", "Anterior baja", "Low anterior"),
        ChoiceV25("POSTERIOR_LOW", "Posterior baja", "Low posterior"),
        ChoiceV25("ASYMMETRIC", "Asimétrica", "Asymmetric")
    )
    val palpation = listOf(
        ChoiceV25("NORMAL", "Sin prominencias/depresiones dolorosas", "No painful prominences/depressions"),
        ChoiceV25("PROMINENCE", "Prominencia ósea", "Bony prominence"),
        ChoiceV25("DEPRESSION", "Depresión", "Depression"),
        ChoiceV25("TENDER", "Dolor a la palpación", "Tenderness"),
        ChoiceV25("MASS", "Masa / aumento de volumen", "Mass / swelling")
    )
    ResponsiveSectionV17(tr(lang, "IX.3.1 · Cráneo", "IX.3.1 · Cranium")) {
        ChoiceRowV25(lang, "Forma observada", "Observed shape", state.craniumObservation, shapes) { save(state.copy(craniumObservation = it)) }
        ChoiceRowV25(lang, "Implantación del cabello", "Hairline", state.hairline, hairline) { save(state.copy(hairline = it)) }
        ChoiceRowV25(lang, "Palpación del contorno", "Contour palpation", state.cranialPalpation, palpation) { save(state.copy(cranialPalpation = it)) }
        OutlinedTextField(state.cranialWidthMm, { save(state.copy(cranialWidthMm = cleanNumberV25(it))) }, label = { Text(tr(lang, "Ancho máximo craneal (mm), opcional", "Maximum cranial width (mm), optional")) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.cranialLengthMm, { save(state.copy(cranialLengthMm = cleanNumberV25(it))) }, label = { Text(tr(lang, "Largo máximo glabela-occipital (mm), opcional", "Maximum glabella-occiput length (mm), optional")) }, modifier = Modifier.fillMaxWidth())
        val width = state.cranialWidthMm.toDoubleOrNull()
        val length = state.cranialLengthMm.toDoubleOrNull()
        if (width != null && length != null && length > 0) {
            Text(tr(lang, "Índice cefálico = ${"%.1f".format(width / length * 100)}. Es una referencia antropométrica, no un diagnóstico.", "Cephalic index = ${"%.1f".format(width / length * 100)}. It is an anthropometric reference, not a diagnosis."), fontWeight = FontWeight.Bold)
        }
        OutlinedTextField(state.headCircumferenceCm, { save(state.copy(headCircumferenceCm = cleanNumberV25(it))) }, label = { Text(tr(lang, "Perímetro cefálico (cm), cuando esté indicado", "Head circumference (cm), when indicated")) }, modifier = Modifier.fillMaxWidth())
    }
    NoticeCard(tr(lang, "En pediatría, el perímetro cefálico debe interpretarse con referencias estandarizadas por edad y sexo y, cuando sea posible, de forma seriada.", "In pediatrics, head circumference should be interpreted using standardized age- and sex-specific references and, when possible, serially."))
}

@Composable
private fun FaceV25(
    lang: String,
    state: HeadNeckExplorationState,
    save: (HeadNeckExplorationState) -> Unit,
    saveNote: (String, String) -> Unit
) {
    ResponsiveSectionV17(tr(lang, "IX.3.2 · Cara", "IX.3.2 · Face")) {
        TeachingBoxV25(
            tr(lang, "Perfil facial", "Facial profile"),
            tr(lang, "Obsérvalo de lado, con la cabeza en posición natural y la oclusión habitual. Recto suele acompañar relaciones esqueléticas equilibradas; convexo puede acompañar retrusión mandibular/Clase II; cóncavo puede acompañar prognatismo mandibular/Clase III. Son asociaciones, no diagnósticos por sí solas.", "Observe from the side with the head in natural position and habitual occlusion. Straight profiles often accompany balanced skeletal relationships; convex profiles may accompany mandibular retrusion/Class II; concave profiles may accompany mandibular prognathism/Class III. These are associations, not diagnoses by themselves.")
        )
        ChoiceRowV25(lang, "Perfil", "Profile", state.faceProfile, profileChoicesV25) { save(state.copy(faceProfile = it)) }
        TeachingBoxV25(
            tr(lang, "Simetría facial", "Facial symmetry"),
            tr(lang, "Evalúa de frente. Distingue variación leve de una desviación mandibular, debilidad muscular o aumento de volumen localizado.", "Assess from the front. Distinguish mild variation from mandibular deviation, muscle weakness or localized swelling.")
        )
        ChoiceRowV25(lang, "Simetría", "Symmetry", state.faceSymmetry, symmetryChoicesV25) { save(state.copy(faceSymmetry = it)) }
        ChoiceRowV25(lang, "Tono de piel observado", "Observed baseline skin tone", state.notes["skin_tone"].orEmpty(), skinToneChoicesV25) { saveNote("skin_tone", it) }
        ChoiceRowV25(lang, "Cambio de coloración", "Color change", state.facialSkin, skinChangeChoicesV25) { save(state.copy(facialSkin = it)) }
        OutlinedTextField(state.notes["face"].orEmpty(), { saveNote("face", it.take(500)) }, label = { Text(tr(lang, "Localización, lado, tamaño y otros hallazgos", "Location, side, size and other findings")) }, modifier = Modifier.fillMaxWidth())
    }
    NoticeCard(tr(lang, "Palidez, tinte amarillento, coloración azulada o eritema deben interpretarse con iluminación adecuada y contexto clínico. La apariencia aislada no confirma anemia, ictericia, hipoxemia ni otra etiología.", "Pallor, yellow tint, bluish color or erythema should be interpreted with adequate lighting and clinical context. Appearance alone does not confirm anemia, jaundice, hypoxemia or another cause."))
}

@Composable
private fun FacialMusclesV25(
    lang: String,
    state: HeadNeckExplorationState,
    save: (HeadNeckExplorationState) -> Unit,
    saveNote: (String, String) -> Unit
) {
    val checked = state.notes["facial_tests"].orEmpty().split(',').filter { it.isNotBlank() }.toSet()
    fun toggle(id: String) {
        val next = checked.toMutableSet().apply { if (!add(id)) remove(id) }
        saveNote("facial_tests", next.joinToString(","))
    }
    ResponsiveSectionV17(tr(lang, "IX.3.3 · Músculos de la expresión facial", "IX.3.3 · Muscles of facial expression")) {
        Text(tr(lang, "Pide movimientos voluntarios y compara ambos lados. Marca las maniobras practicadas:", "Ask for voluntary movements and compare both sides. Mark the maneuvers practiced:"), fontWeight = FontWeight.Bold)
        facialManeuversV25.forEach { maneuver ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .45f))) {
                Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Checkbox(checked = maneuver.id in checked, onCheckedChange = { toggle(maneuver.id) })
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(if (lang == "en") maneuver.en else maneuver.es, fontWeight = FontWeight.Black)
                        Text("${tr(lang, "Principal", "Main")}: ${if (lang == "en") maneuver.muscleEn else maneuver.muscleEs}", style = MaterialTheme.typography.bodySmall)
                        Text(if (lang == "en") maneuver.observeEn else maneuver.observeEs, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        ChoiceRowV25(lang, "Resultado funcional", "Functional result", state.facialMuscleFunction, facialFunctionChoicesV25) { save(state.copy(facialMuscleFunction = it)) }
        ChoiceRowV25(lang, "Tono aparente", "Apparent tone", state.facialMuscleTone, toneChoicesV25) { save(state.copy(facialMuscleTone = it)) }
        OutlinedTextField(state.notes["facial_muscles"].orEmpty(), { saveNote("facial_muscles", it.take(500)) }, label = { Text(tr(lang, "Descripción adicional", "Additional description")) }, modifier = Modifier.fillMaxWidth())
    }
    NoticeCard(tr(lang, "Signos que ameritan descripción precisa: asimetría nueva, cierre ocular incompleto, fuga de aire, desviación de la comisura, debilidad focal o movimientos involuntarios.", "Findings that warrant precise description include new asymmetry, incomplete eye closure, air leakage, mouth-corner deviation, focal weakness or involuntary movements."))
}

@Composable
private fun MasticatoryMusclesV25(
    lang: String,
    state: HeadNeckExplorationState,
    save: (HeadNeckExplorationState) -> Unit,
    saveNote: (String, String) -> Unit
) {
    ResponsiveSectionV17(tr(lang, "IX.3.4 · Músculos de la masticación", "IX.3.4 · Muscles of mastication")) {
        TeachingBoxV25(tr(lang, "Temporal", "Temporalis"), tr(lang, "Palpa bilateralmente en la fosa temporal, en reposo y durante contracción suave al apretar dientes. Describe dolor, asimetría o volumen.", "Palpate bilaterally in the temporal fossa at rest and during gentle clenching. Describe tenderness, asymmetry or bulk."))
        TeachingBoxV25(tr(lang, "Masetero", "Masseter"), tr(lang, "Palpa sobre la rama/ángulo mandibular en reposo y contracción. Describe dolor, hipertrofia, hipertonía o asimetría.", "Palpate over the mandibular ramus/angle at rest and during contraction. Describe tenderness, hypertrophy, hypertonicity or asymmetry."))
        TeachingBoxV25(tr(lang, "Pterigoideos", "Pterygoids"), tr(lang, "Su valoración es principalmente funcional/indirecta. Relaciona dolor profundo con protrusión y lateralidades; evita presentar una palpación profunda aislada como prueba diagnóstica definitiva.", "Assessment is mainly functional/indirect. Relate deep pain to protrusion and lateral movements; do not present isolated deep palpation as a definitive diagnostic test."))
        ChoiceRowV25(lang, "Hallazgo principal", "Main finding", state.masticatoryMuscleFinding, masticatoryFindingChoicesV25) { save(state.copy(masticatoryMuscleFinding = it)) }
        ChoiceRowV25(lang, "Tono / volumen aparente", "Apparent tone / bulk", state.masticatoryMuscleTone, toneChoicesV25) { save(state.copy(masticatoryMuscleTone = it)) }
        OutlinedTextField(state.notes["masticatory"].orEmpty(), { saveNote("masticatory", it.take(500)) }, label = { Text(tr(lang, "Músculo, lado, dolor y maniobra que lo reproduce", "Muscle, side, pain and reproducing maneuver")) }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun NeckV25(
    lang: String,
    state: HeadNeckExplorationState,
    save: (HeadNeckExplorationState) -> Unit,
    saveNote: (String, String) -> Unit
) {
    ResponsiveSectionV17(tr(lang, "IX.3.5 · Revisión del cuello", "IX.3.5 · Neck examination")) {
        Text(tr(lang, "Inspecciona de frente y de lado; después valora movilidad activa y palpación cuando corresponda.", "Inspect from the front and side; then assess active mobility and palpation when appropriate."))
        ChoiceRowV25(lang, "Inspección", "Inspection", state.neckInspection, neckInspectionChoicesV25) { save(state.copy(neckInspection = it)) }
        ChoiceRowV25(lang, "Movilidad cervical", "Cervical mobility", state.neckMobility, neckMobilityChoicesV25) { save(state.copy(neckMobility = it)) }
        ChoiceRowV25(lang, "Dolor a la palpación", "Tenderness", state.neckTenderness, yesNoChoicesV25) { save(state.copy(neckTenderness = it)) }
        ChoiceRowV25(lang, "Posición de la tráquea", "Tracheal position", state.notes["trachea"].orEmpty(), tracheaChoicesV25) { saveNote("trachea", it) }
        ChoiceRowV25(lang, "Tiroides, cuando corresponda", "Thyroid, when appropriate", state.notes["thyroid"].orEmpty(), thyroidChoicesV25) { saveNote("thyroid", it) }
        OutlinedTextField(state.notes["neck"].orEmpty(), { saveNote("neck", it.take(500)) }, label = { Text(tr(lang, "Masas, aumento de volumen, piel, región submandibular u otros datos", "Masses, swelling, skin, submandibular region or other findings")) }, modifier = Modifier.fillMaxWidth())
    }
    NoticeCard(tr(lang, "Banderas rojas: dificultad respiratoria, disfagia importante, masa cervical persistente/progresiva, masa dura o fija, fiebre importante con aumento de volumen o deterioro clínico. Requieren valoración apropiada y no deben reducirse a un diagnóstico visual de la app.", "Red flags: breathing difficulty, significant dysphagia, persistent/progressive neck mass, hard or fixed mass, significant fever with swelling, or clinical deterioration. They require appropriate assessment and should not be reduced to a visual diagnosis by the app."))
}

@Composable
private fun LymphNodeDiagramV25(lang: String) {
    val nodeColor = MaterialTheme.colorScheme.primary
    val lineColor = MaterialTheme.colorScheme.outline
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .35f))) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(tr(lang, "Mapa didáctico de cadenas ganglionares", "Teaching map of lymph-node chains"), fontWeight = FontWeight.Black)
            Box(Modifier.fillMaxWidth().height(300.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))) {
                Canvas(Modifier.fillMaxWidth().height(300.dp)) {
                    val cx = size.width * .5f
                    val headR = size.width.coerceAtMost(size.height) * .22f
                    val headCenter = Offset(cx, size.height * .30f)
                    drawCircle(color = Color(0xFFFFD9BE), radius = headR, center = headCenter)
                    drawLine(lineColor, Offset(cx - headR * .45f, headCenter.y + headR * .78f), Offset(cx - headR * .32f, size.height * .82f), strokeWidth = 18f, cap = StrokeCap.Round)
                    drawLine(lineColor, Offset(cx + headR * .45f, headCenter.y + headR * .78f), Offset(cx + headR * .32f, size.height * .82f), strokeWidth = 18f, cap = StrokeCap.Round)
                    val pts = listOf(
                        Offset(cx - headR * .95f, headCenter.y), Offset(cx + headR * .95f, headCenter.y),
                        Offset(cx - headR * .8f, headCenter.y - headR * .35f), Offset(cx + headR * .8f, headCenter.y - headR * .35f),
                        Offset(cx - headR * .48f, headCenter.y + headR * .75f), Offset(cx + headR * .48f, headCenter.y + headR * .75f),
                        Offset(cx - headR * .18f, headCenter.y + headR * .96f), Offset(cx + headR * .18f, headCenter.y + headR * .96f),
                        Offset(cx - headR * .48f, size.height * .58f), Offset(cx + headR * .48f, size.height * .58f),
                        Offset(cx - headR * .56f, size.height * .68f), Offset(cx + headR * .56f, size.height * .68f),
                        Offset(cx - headR * .62f, size.height * .80f), Offset(cx + headR * .62f, size.height * .80f),
                        Offset(cx - headR * .76f, size.height * .88f), Offset(cx + headR * .76f, size.height * .88f)
                    )
                    pts.forEach { p -> drawCircle(nodeColor, radius = 10f, center = p) }
                }
            }
            val names = listOf(
                tr(lang, "Preauriculares", "Preauricular"), tr(lang, "Postauriculares / mastoideos", "Postauricular / mastoid"),
                tr(lang, "Occipitales", "Occipital"), tr(lang, "Submandibulares", "Submandibular"),
                tr(lang, "Submentonianos", "Submental"), tr(lang, "Cervicales anteriores", "Anterior cervical"),
                tr(lang, "Cervicales profundos", "Deep cervical"), tr(lang, "Cervicales posteriores", "Posterior cervical"),
                tr(lang, "Supraclaviculares", "Supraclavicular")
            )
            names.chunked(2).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { name ->
                        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                            Text("● $name", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    if (row.size == 1) Box(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun LymphNodesV25(lang: String, state: HeadNeckExplorationState, save: (HeadNeckExplorationState) -> Unit) {
    var selectedId by remember { mutableStateOf(lymphChainsV25.first().id) }
    val selected = lymphChainsV25.first { it.id == selectedId }
    val node = state.lymphNodes[selectedId] ?: LymphNodeState()
    fun saveNode(next: LymphNodeState) = save(state.copy(lymphNodes = state.lymphNodes + (selectedId to next)))

    ResponsiveSectionV17(tr(lang, "IX.3.6 · Cadenas ganglionares", "IX.3.6 · Lymph-node chains")) {
        LymphNodeDiagramV25(lang)
        Text(tr(lang, "Selecciona una cadena y documenta el hallazgo. La relación anatómica orienta el sitio de drenaje; no identifica por sí sola la causa.", "Select a chain and document the finding. Anatomic drainage suggests possible source regions; it does not establish the cause by itself."))
        lymphChainsV25.forEach { chain ->
            FilterChip(
                selected = selectedId == chain.id,
                onClick = { selectedId = chain.id },
                label = { Text(if (lang == "en") chain.en else chain.es) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        TeachingBoxV25(
            if (lang == "en") selected.en else selected.es,
            if (lang == "en") selected.relationEn else selected.relationEs
        )
        ChoiceRowV25(lang, "Estado", "Status", node.status, nodeStatusChoicesV25) {
            saveNode(if (it == "NOT_PALPABLE") LymphNodeState(status = it) else node.copy(status = it))
        }
        if (node.status == "PALPABLE") {
            ChoiceRowV25(lang, "Lado", "Side", node.side, sideChoicesV25) { saveNode(node.copy(side = it)) }
            OutlinedTextField(node.sizeMm, { saveNode(node.copy(sizeMm = cleanNumberV25(it))) }, label = { Text(tr(lang, "Tamaño aproximado del mayor ganglio (mm)", "Approximate size of largest node (mm)")) }, modifier = Modifier.fillMaxWidth())
            ChoiceRowV25(lang, "Dolor", "Tenderness", node.tenderness, yesNoChoicesV25) { saveNode(node.copy(tenderness = it)) }
            ChoiceRowV25(lang, "Consistencia", "Consistency", node.consistency, consistencyChoicesV25) { saveNode(node.copy(consistency = it)) }
            ChoiceRowV25(lang, "Movilidad", "Mobility", node.mobility, mobilityChoicesV25) { saveNode(node.copy(mobility = it)) }
            OutlinedTextField(node.notes, { saveNode(node.copy(notes = it.take(350))) }, label = { Text(tr(lang, "Número, conglomerados, piel suprayacente u otros datos", "Number, matting, overlying skin or other findings")) }, modifier = Modifier.fillMaxWidth())
        }
    }
    NoticeCard(tr(lang, "Ganglios persistentes o progresivos, duros, fijos, supraclaviculares o acompañados de otros datos de alarma requieren valoración clínica apropiada. La app registra hallazgos; no determina etiología.", "Persistent or progressive, hard, fixed, supraclavicular nodes or nodes with other warning findings require appropriate clinical assessment. The app records findings; it does not determine etiology."))
}

private val retrusionChoicesV25 = listOf(
    ChoiceV25("NORMAL", "Sin alteración aparente", "No apparent alteration"),
    ChoiceV25("PAIN", "Dolor retrodiscal / articular", "Retrodiscal / joint pain"),
    ChoiceV25("LIMITED", "Limitada", "Limited"),
    ChoiceV25("UNABLE", "No valorable", "Unable to assess")
)
private val openingPathChoicesV25 = listOf(
    ChoiceV25("STRAIGHT", "Recta / centrada", "Straight / centered"),
    ChoiceV25("RIGHT", "Desviación hacia derecha", "Deviation to the right"),
    ChoiceV25("LEFT", "Desviación hacia izquierda", "Deviation to the left"),
    ChoiceV25("DEFLECTION_RIGHT", "Deflexión persistente derecha", "Persistent right deflection"),
    ChoiceV25("DEFLECTION_LEFT", "Deflexión persistente izquierda", "Persistent left deflection")
)
private val atmSectionsV25 = listOf(
    SectionV25("↕", "Apertura", "Opening"),
    SectionV25("↔", "Lateralidades", "Laterality"),
    SectionV25("⇆", "Protrusión / retrusión", "Protrusion / retrusion"),
    SectionV25("📏", "DVO / DVR", "OVD / RVD"),
    SectionV25("🧩", "Hallazgos y orientación", "Findings & orientation")
)

@Composable
fun AtmDiagnosisV25Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var section by remember { mutableStateOf(0) }
    val state = session.atmExam
    fun save(next: AtmExamState) = onSessionChanged(session.copy(atmExam = next))

    ResponsiveScreenV17(
        tr(lang, "IX.3.7 · ATM · diagnóstico temporomandibular", "IX.3.7 · TMJ · temporomandibular assessment"),
        tr(lang, "Integra movimientos, ruidos, dolor, trayectoria y dimensiones verticales. Los hallazgos orientan; no sustituyen un diagnóstico clínico integral.", "Integrate movements, sounds, pain, path and vertical dimensions. Findings guide assessment; they do not replace a comprehensive clinical diagnosis."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang, "Apartados ATM", "TMJ sections")) {
            val columns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
            AdaptiveGridV17(atmSectionsV25.size, columns) { index ->
                val item = atmSectionsV25[index]
                if (section == index) Button(onClick = { section = index }, modifier = Modifier.fillMaxWidth()) {
                    Text("${item.icon} ${if (lang == "en") item.en else item.es}", textAlign = TextAlign.Center)
                } else OutlinedButton(onClick = { section = index }, modifier = Modifier.fillMaxWidth()) {
                    Text("${item.icon} ${if (lang == "en") item.en else item.es}", textAlign = TextAlign.Center)
                }
            }
        }

        when (section) {
            0 -> AtmOpeningV25(lang, state, ::save)
            1 -> AtmLateralityV25(lang, state, ::save)
            2 -> AtmProtrusionRetrusionV25(lang, state, ::save)
            3 -> AtmVerticalDimensionV25(lang, state, ::save)
            else -> AtmFindingsV25(lang, state, ::save)
        }

        ResponsiveSectionV17(tr(lang, "Navegación ATM", "TMJ navigation")) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(enabled = section > 0, onClick = { if (section > 0) section-- }, modifier = Modifier.weight(1f)) { Text("← ${tr(lang, "Anterior", "Previous")}") }
                Button(enabled = section < atmSectionsV25.lastIndex, onClick = { if (section < atmSectionsV25.lastIndex) section++ }, modifier = Modifier.weight(1f)) { Text("${tr(lang, "Siguiente", "Next")} →") }
            }
        }
    }
}

@Composable
private fun AtmOpeningV25(lang: String, state: AtmExamState, save: (AtmExamState) -> Unit) {
    ResponsiveSectionV17(tr(lang, "Apertura y cierre", "Opening and closing")) {
        TeachingBoxV25(tr(lang, "Cómo evaluar", "How to assess"), tr(lang, "Pide abrir y cerrar lentamente. Observa dolor, trayectoria, ruidos, bloqueo y simetría. Mide la distancia interincisal máxima con una regla o calibrador adecuado.", "Ask the patient to open and close slowly. Observe pain, path, sounds, locking and symmetry. Measure maximum interincisal distance with an appropriate ruler or caliper."))
        OutlinedTextField(state.openingMm, { save(state.copy(openingMm = cleanNumberV25(it))) }, label = { Text(tr(lang, "Apertura máxima (mm)", "Maximum opening (mm)")) }, modifier = Modifier.fillMaxWidth())
        ChoiceRowV25(lang, "Trayectoria", "Opening path", state.openingPath, openingPathChoicesV25) { save(state.copy(openingPath = it)) }
        ToggleFindingV25(lang, tr(lang, "Dolor", "Pain"), state.pain) { save(state.copy(pain = it)) }
        ToggleFindingV25(lang, tr(lang, "Chasquido", "Click"), state.click) { save(state.copy(click = it)) }
        ToggleFindingV25(lang, tr(lang, "Crepitación", "Crepitus"), state.crepitus) { save(state.copy(crepitus = it)) }
        ToggleFindingV25(lang, tr(lang, "Bloqueo / trabamiento", "Locking"), state.locking) { save(state.copy(locking = it)) }
        val opening = state.openingMm.toDoubleOrNull()
        if (opening != null) {
            val interpretation = when {
                opening < 35 -> tr(lang, "Por debajo de 35 mm: apertura limitada en esta referencia didáctica; correlaciona con dolor, bloqueo y contexto.", "Below 35 mm: limited opening in this teaching reference; correlate with pain, locking and context.")
                opening in 40.0..55.0 -> tr(lang, "Dentro de la referencia didáctica aproximada de 40–55 mm.", "Within the approximate teaching reference of 40–55 mm.")
                else -> tr(lang, "Fuera del intervalo didáctico 40–55 mm; interpreta según edad, anatomía, síntomas y técnica de medición.", "Outside the 40–55 mm teaching interval; interpret according to age, anatomy, symptoms and measurement technique.")
            }
            NoticeCard(interpretation)
        }
    }
}

@Composable
private fun AtmLateralityV25(lang: String, state: AtmExamState, save: (AtmExamState) -> Unit) {
    ResponsiveSectionV17(tr(lang, "Lateralidad derecha e izquierda", "Right and left laterality")) {
        TeachingBoxV25(tr(lang, "Cómo evaluar", "How to assess"), tr(lang, "Pide mover la mandíbula a cada lado y mide el desplazamiento de la línea media dental. Como referencia didáctica se suele usar aproximadamente 8–12 mm, pero debe interpretarse en contexto.", "Ask the patient to move the mandible to each side and measure displacement of the dental midline. An approximate 8–12 mm teaching reference is often used, but context matters."))
        OutlinedTextField(state.rightLateralityMm, { save(state.copy(rightLateralityMm = cleanNumberV25(it))) }, label = { Text(tr(lang, "Lateralidad derecha (mm)", "Right laterality (mm)")) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.leftLateralityMm, { save(state.copy(leftLateralityMm = cleanNumberV25(it))) }, label = { Text(tr(lang, "Lateralidad izquierda (mm)", "Left laterality (mm)")) }, modifier = Modifier.fillMaxWidth())
        val r = state.rightLateralityMm.toDoubleOrNull()
        val l = state.leftLateralityMm.toDoubleOrNull()
        if (r != null && l != null) {
            val diff = kotlin.math.abs(r - l)
            Text(tr(lang, "Diferencia derecha–izquierda: ${"%.1f".format(diff)} mm. Una asimetría debe correlacionarse con dolor, trayectoria, oclusión y función.", "Right–left difference: ${"%.1f".format(diff)} mm. Asymmetry should be correlated with pain, path, occlusion and function."), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AtmProtrusionRetrusionV25(lang: String, state: AtmExamState, save: (AtmExamState) -> Unit) {
    ResponsiveSectionV17(tr(lang, "Protrusión y retrusión", "Protrusion and retrusion")) {
        TeachingBoxV25(tr(lang, "Protrusión", "Protrusion"), tr(lang, "Pide llevar la mandíbula hacia delante. Registra rango, dolor, desviación y coordinación. Referencia didáctica aproximada: 7–10 mm.", "Ask the patient to move the mandible forward. Record range, pain, deviation and coordination. Approximate teaching reference: 7–10 mm."))
        OutlinedTextField(state.protrusionMm, { save(state.copy(protrusionMm = cleanNumberV25(it))) }, label = { Text(tr(lang, "Protrusión (mm)", "Protrusion (mm)")) }, modifier = Modifier.fillMaxWidth())
        TeachingBoxV25(tr(lang, "Retrusión", "Retrusion"), tr(lang, "Pide llevar suavemente la mandíbula hacia atrás sin forzar. Registra dolor o limitación; no interpretes dolor retrodiscal aislado como un diagnóstico definitivo.", "Ask the patient to gently move the mandible backward without forcing. Record pain or limitation; do not interpret isolated retrodiscal pain as a definitive diagnosis."))
        ChoiceRowV25(lang, "Hallazgo en retrusión", "Retrusion finding", state.retrusionFinding, retrusionChoicesV25) { save(state.copy(retrusionFinding = it)) }
    }
}

@Composable
private fun AtmVerticalDimensionV25(lang: String, state: AtmExamState, save: (AtmExamState) -> Unit) {
    ResponsiveSectionV17(tr(lang, "Dimensión vertical en oclusión y reposo", "Occlusal and rest vertical dimension")) {
        TeachingBoxV25("DVO / OVD", tr(lang, "Mide la distancia entre puntos faciales reproducibles, por ejemplo subnasal–mentón, con los dientes en oclusión habitual. Mantén los mismos puntos para comparar.", "Measure between reproducible facial points, for example subnasale–menton, with teeth in habitual occlusion. Use the same points for comparison."))
        OutlinedTextField(state.dvoMm, { save(state.copy(dvoMm = cleanNumberV25(it))) }, label = { Text("DVO / OVD (mm)") }, modifier = Modifier.fillMaxWidth())
        TeachingBoxV25("DVR / RVD", tr(lang, "Mide con la mandíbula en posición de reposo fisiológico. La diferencia DVR − DVO se usa como espacio libre interoclusal; aproximadamente 2–4 mm es una referencia didáctica frecuente, no un valor universal.", "Measure with the mandible in physiologic rest position. RVD − OVD is used as interocclusal rest space; approximately 2–4 mm is a common teaching reference, not a universal value."))
        OutlinedTextField(state.dvrMm, { save(state.copy(dvrMm = cleanNumberV25(it))) }, label = { Text("DVR / RVD (mm)") }, modifier = Modifier.fillMaxWidth())
        val dvo = state.dvoMm.toDoubleOrNull()
        val dvr = state.dvrMm.toDoubleOrNull()
        if (dvo != null && dvr != null) {
            val space = dvr - dvo
            Text(tr(lang, "Espacio libre calculado: ${"%.1f".format(space)} mm.", "Calculated rest space: ${"%.1f".format(space)} mm."), fontWeight = FontWeight.Black)
            NoticeCard(tr(lang, "Interpreta esta diferencia junto con comodidad, fonética, desgaste, prótesis, soporte facial y método de medición.", "Interpret this difference together with comfort, phonetics, wear, prostheses, facial support and measurement method."))
        }
    }
}

@Composable
private fun AtmFindingsV25(lang: String, state: AtmExamState, save: (AtmExamState) -> Unit) {
    ResponsiveSectionV17(tr(lang, "Hallazgos y orientación diagnóstica", "Findings and diagnostic orientation")) {
        ToggleFindingV25(lang, tr(lang, "Dolor durante movimientos", "Pain during movement"), state.pain) { save(state.copy(pain = it)) }
        ToggleFindingV25(lang, tr(lang, "Chasquido", "Click"), state.click) { save(state.copy(click = it)) }
        ToggleFindingV25(lang, tr(lang, "Crepitación", "Crepitus"), state.crepitus) { save(state.copy(crepitus = it)) }
        ToggleFindingV25(lang, tr(lang, "Bloqueo / trabamiento", "Locking"), state.locking) { save(state.copy(locking = it)) }
        ToggleFindingV25(lang, tr(lang, "Dolor a palpación ATM derecha", "Right TMJ tenderness"), state.rightTenderness) { save(state.copy(rightTenderness = it)) }
        ToggleFindingV25(lang, tr(lang, "Dolor a palpación ATM izquierda", "Left TMJ tenderness"), state.leftTenderness) { save(state.copy(leftTenderness = it)) }
        OutlinedTextField(state.notes, { save(state.copy(notes = it.take(700))) }, label = { Text(tr(lang, "Descripción adicional", "Additional description")) }, modifier = Modifier.fillMaxWidth())

        if (state.click) TeachingBoxV25(tr(lang, "Chasquido", "Click"), tr(lang, "Puede ser compatible con desplazamiento discal con reducción cuando la historia y el patrón clínico concuerdan. Un ruido aislado no confirma el diagnóstico.", "May be compatible with disc displacement with reduction when history and the clinical pattern agree. An isolated sound does not establish the diagnosis."))
        if (state.crepitus) TeachingBoxV25(tr(lang, "Crepitación", "Crepitus"), tr(lang, "Puede acompañar cambios degenerativos articulares; requiere correlación clínica y, si está indicado, imagen.", "May accompany degenerative joint changes; correlate clinically and with imaging when indicated."))
        if (state.pain || state.rightTenderness || state.leftTenderness) TeachingBoxV25(tr(lang, "Dolor", "Pain"), tr(lang, "Localiza si el dolor es articular o muscular y si se reproduce con movimientos o palpación. Esto orienta hacia artralgia o trastorno muscular, pero requiere criterios clínicos completos.", "Determine whether pain is joint or muscular and whether it is reproduced by movement or palpation. This may guide toward arthralgia or muscular disorder but requires complete clinical criteria."))
        if (state.locking) TeachingBoxV25(tr(lang, "Bloqueo", "Locking"), tr(lang, "Un episodio de bloqueo o limitación marcada requiere valorar desplazamiento discal sin reducción y otras causas mecánicas o musculares.", "Locking or marked limitation warrants assessment for disc displacement without reduction and other mechanical or muscular causes."))
        if (state.openingPath.isNotBlank() && state.openingPath != "STRAIGHT") TeachingBoxV25(tr(lang, "Desviación / deflexión", "Deviation / deflection"), tr(lang, "Puede reflejar asimetría funcional, restricción articular o muscular. Interpreta junto con el rango de lateralidad, dolor y antecedentes.", "May reflect functional asymmetry or joint/muscular restriction. Interpret together with lateral range, pain and history."))
    }
    NoticeCard(tr(lang, "Diagnóstico temporomandibular: los movimientos y signos orientan, pero el diagnóstico final requiere historia de dolor/disfunción, exploración reproducible y criterios clínicos apropiados. La app no sustituye valoración profesional.", "Temporomandibular diagnosis: movements and signs guide assessment, but final diagnosis requires pain/dysfunction history, reproducible examination and appropriate clinical criteria. The app does not replace professional assessment."))
}

@Composable
private fun ToggleFindingV25(lang: String, label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    FilterChip(
        selected = checked,
        onClick = { onChange(!checked) },
        label = { Text((if (checked) "✓ " else "○ ") + label) },
        modifier = Modifier.fillMaxWidth()
    )
}
