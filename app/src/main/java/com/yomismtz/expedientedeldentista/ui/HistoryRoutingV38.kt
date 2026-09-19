package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen

internal enum class HistorySectionV38 {
    REASON_PRESENT,
    FAMILY,
    NON_PATHOLOGICAL,
    PATHOLOGICAL,
    SURGICAL_TRAUMA
}

private data class HistoryRouteItemV38(
    val es: String,
    val en: String,
    val detailEs: String,
    val detailEn: String,
    val screen: AppScreen? = null,
    val section: HistorySectionV38? = null
)

private val historyRoutesV38 = listOf(
    HistoryRouteItemV38(
        "Identificación del paciente",
        "Patient identification",
        "Nombre, sexo/género, edad, nacimiento, domicilio, teléfono, ocupación, religión, familia, estado civil, escolaridad y servicio de salud.",
        "Name, sex/gender, age, birth, address, telephone, occupation, religion, family, marital status, education and health service.",
        screen = AppScreen.IDENTIFICATION
    ),
    HistoryRouteItemV38(
        "Motivo de consulta y padecimiento actual",
        "Chief complaint and present illness",
        "Primero conserva literalmente lo que dice el paciente o tutor; después se redacta clínicamente con interrogatorio y exploración.",
        "First preserve the patient's or guardian's exact words; then write the clinical description from history and examination.",
        section = HistorySectionV38.REASON_PRESENT
    ),
    HistoryRouteItemV38(
        "Antecedentes heredo-familiares",
        "Family history",
        "Familiares específicos de línea materna y paterna más categorías de enfermedad.",
        "Specific maternal and paternal relatives plus disease categories.",
        section = HistorySectionV38.FAMILY
    ),
    HistoryRouteItemV38(
        "Antecedentes personales no patológicos",
        "Non-pathological personal history",
        "Vivienda, higiene general y bucal, alimentación, tabaco, alcohol, drogas, perforaciones, tatuajes y exposiciones.",
        "Housing, general and oral hygiene, diet, tobacco, alcohol, drugs, piercings, tattoos and exposures.",
        section = HistorySectionV38.NON_PATHOLOGICAL
    ),
    HistoryRouteItemV38(
        "Antecedentes personales patológicos",
        "Pathological personal history",
        "Vacunas, enfermedades por sistemas, inicio, estado actual, medicamentos y esquema referido por el paciente.",
        "Vaccines, diseases by system, onset, current status, medicines and the regimen reported by the patient.",
        section = HistorySectionV38.PATHOLOGICAL
    ),
    HistoryRouteItemV38(
        "Antecedentes quirúrgicos y traumáticos",
        "Surgical and trauma history",
        "Cirugías, hospitalizaciones, transfusiones, donación, fracturas, traumatismos, anestesia y grupo sanguíneo.",
        "Surgery, hospitalizations, transfusions, donation, fractures, trauma, anesthesia and blood group.",
        section = HistorySectionV38.SURGICAL_TRAUMA
    ),
    HistoryRouteItemV38(
        "Exploración física",
        "Physical examination",
        "Signos vitales y acceso a la exploración física del expediente.",
        "Vital signs and access to physical examination modules.",
        screen = AppScreen.VITALS
    ),
    HistoryRouteItemV38(
        "Exploración de ATM",
        "TMJ examination",
        "Dolor, ruidos, movimientos, desviación, limitación y palpación.",
        "Pain, sounds, movements, deviation, limitation and palpation.",
        screen = AppScreen.ATM
    ),
    HistoryRouteItemV38(
        "Examen de oclusión",
        "Occlusion examination",
        "Dentición, erupción, relaciones molares/caninas, líneas medias, overjet, overbite y mordidas.",
        "Dentition, eruption, molar/canine relations, midlines, overjet, overbite and bite relationships.",
        screen = AppScreen.OCCLUSION
    )
)

@Composable
internal fun HistoryMenuV38Screen(
    lang: String,
    onNavigate: (AppScreen) -> Unit,
    onSection: (HistorySectionV38) -> Unit,
    onBack: () -> Unit
) {
    ResponsiveScreenV17(
        tr(lang, "Historia clínica", "Clinical history"),
        tr(
            lang,
            "Cada apartado es independiente. La Ficha de identificación permanece fuera de Historia clínica.",
            "Each section is independent. The Identification sheet remains outside the clinical history."
        ),
        onBack
    ) { profile ->
        AdaptiveGridV17(
            historyRoutesV38.size,
            if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
        ) { index ->
            val item = historyRoutesV38[index]
            Card(
                onClick = {
                    when {
                        item.section != null -> onSection(item.section)
                        item.screen != null -> onNavigate(item.screen)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .4f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (lang == "en") item.en else item.es, fontWeight = FontWeight.Black)
                    Text(if (lang == "en") item.detailEn else item.detailEs, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
internal fun HistorySectionV38Screen(section: HistorySectionV38, lang: String, onBack: () -> Unit) {
    when (section) {
        HistorySectionV38.REASON_PRESENT -> ReasonPresentIllnessV37Screen(lang, onBack)
        HistorySectionV38.FAMILY -> FamilyHistoryV37Screen(lang, onBack)
        HistorySectionV38.NON_PATHOLOGICAL -> NonPathologicalHistoryV37Screen(lang, onBack)
        HistorySectionV38.PATHOLOGICAL -> PathologicalHistoryV37Screen(lang, onBack)
        HistorySectionV38.SURGICAL_TRAUMA -> SurgicalTraumaHistoryV37Screen(lang, onBack)
    }
}
