package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

private enum class HistorySectionV44 { IDENTIFICATION, COMPLAINT, FAMILY, NONPATH, GYNECO, PATH, SURGICAL, PHYSICAL, OCCLUSION, ORTHO, DENTAL_ANOMALIES, HABITS, MUCOSA, PERIODONTAL, RADIOGRAPHIC, AUXILIARIES, ODONTOGRAM }
private data class HistoryCardV44(
    val section: HistorySectionV44,
    val titleEs: String,
    val titleEn: String,
    val detailEs: String,
    val detailEn: String
)
private val historyCardsV44 = listOf(
    HistoryCardV44(HistorySectionV44.IDENTIFICATION, "Identificación del paciente", "Patient identification", "Qué datos suelen ir en el expediente real, cómo se redactan y qué errores evitar; aquí no se capturan datos personales.", "Which data belong in the real record, how to write them and which mistakes to avoid; no personal data are collected here."),
    HistoryCardV44(HistorySectionV44.COMPLAINT, "Motivo de consulta y padecimiento actual", "Chief complaint and present illness", "Motivo literal, evolución del problema e hipótesis educativas que deben verificarse.", "Literal complaint, course of the problem and educational hypotheses that must be verified."),
    HistoryCardV44(HistorySectionV44.FAMILY, "Antecedentes heredo-familiares", "Family history", "Familiar → categoría → enfermedades frecuentes y qué ampliar cuando el antecedente es positivo.", "Relative → category → common conditions and what to expand when history is positive."),
    HistoryCardV44(HistorySectionV44.NONPATH, "Antecedentes personales no patológicos", "Non-pathological personal history", "Habitación · Hábitos higiénicos · Hábitos alimenticios · Inmunizaciones · Hábitos y toxicomanías.", "Housing · Hygiene · Diet · Immunizations · Habits and substance use."),
    HistoryCardV44(HistorySectionV44.GYNECO, "Antecedentes gineco-obstétricos", "Gynecologic-obstetric history", "Apartado independiente del formato de referencia; contenido educativo y anónimo.", "Independent section in the reference form; educational and anonymous content."),
    HistoryCardV44(HistorySectionV44.PATH, "Antecedentes personales patológicos", "Pathological personal history", "Vacunación, enfermedades, medicamentos y atlas visual de apoyo.", "Vaccination, diseases, medications and a supporting visual atlas."),
    HistoryCardV44(HistorySectionV44.SURGICAL, "Antecedentes quirúrgicos y traumáticos", "Surgical and trauma history", "Cirugías, hospitalizaciones, transfusiones, fracturas/luxaciones y resumen.", "Surgeries, hospitalizations, transfusions, fractures/dislocations and summary."),
    HistoryCardV44(HistorySectionV44.PHYSICAL, "Exploración física", "Physical examination", "Signos vitales · Inspección general · Cabeza y cuello: cráneo, cara, músculos, cuello, cadenas ganglionares y ATM.", "Vital signs · General inspection · Head and neck: cranium, face, muscles, neck, lymph nodes and TMJ."),
    HistoryCardV44(HistorySectionV44.OCCLUSION, "Examen de oclusión", "Occlusion examination", "Dentición/erupción, plano terminal, Angle, caninos, líneas medias, overjet/overbite y mordidas.", "Dentition/eruption, terminal plane, Angle, canine relation, midlines, overjet/overbite and bite relationships."),
    HistoryCardV44(HistorySectionV44.ORTHO, "Antecedentes de tratamientos ortodónticos", "Orthodontic treatment history", "Tratamientos previos, aparatología, finalidad y duración referida.", "Previous treatment, appliances, purpose and reported duration."),
    HistoryCardV44(HistorySectionV44.DENTAL_ANOMALIES, "Alteraciones de órganos dentarios", "Dental anomalies", "Alteraciones de forma, número y estructura.", "Anomalies of form, number and structure."),
    HistoryCardV44(HistorySectionV44.HABITS, "Hábitos y parafunciones", "Habits and parafunctions", "Presencia, duración/frecuencia y manifestaciones clínicas.", "Presence, duration/frequency and clinical manifestations."),
    HistoryCardV44(HistorySectionV44.MUCOSA, "Examen peribucal e intrabucal", "Perioral and intraoral examination", "Piel/mucosas, labios, comisuras, carrillos, paladar, orofaringe, lengua y piso de boca.", "Skin/mucosa, lips, commissures, cheeks, palate, oropharynx, tongue and floor of mouth."),
    HistoryCardV44(HistorySectionV44.PERIODONTAL, "Examen periodontal", "Periodontal examination", "IPC, IHOS y observaciones gingivales/periodontales.", "CPI, OHI-S and gingival/periodontal observations."),
    HistoryCardV44(HistorySectionV44.RADIOGRAPHIC, "Estudios radiográficos", "Radiographic studies", "Periapical/dentoalveolar, aleta mordible, oclusal y estudios extraorales.", "Periapical, bitewing, occlusal and extraoral studies."),
    HistoryCardV44(HistorySectionV44.AUXILIARIES, "Auxiliares de diagnóstico", "Diagnostic aids", "Histopatología, microbiología, análisis clínicos, modelos, cefalometría y fotografías.", "Histopathology, microbiology, laboratory tests, models, cephalometrics and photographs."),
    HistoryCardV44(HistorySectionV44.ODONTOGRAM, "Odontograma", "Odontogram", "Odontograma e índices de experiencia de caries.", "Odontogram and caries-experience indices.")
)

@Composable
fun HistoryHubV44Screen(lang: String, onVitals: () -> Unit, onBack: () -> Unit) {
    var section by remember { mutableStateOf<HistorySectionV44?>(null) }

    // El botón físico/gesto de Android vuelve primero al nivel inmediatamente anterior
    // dentro de Historia clínica, en vez de saltar al menú principal.
    BackHandler(enabled = section != null) { section = null }

    when (section) {
        HistorySectionV44.IDENTIFICATION -> { PatientIdentificationV36Screen(lang) { section = null }; return }
        HistorySectionV44.COMPLAINT -> { ChiefComplaintV40Screen(lang) { section = null }; return }
        HistorySectionV44.FAMILY -> { FamilyHistoryV45Screen(lang) { section = null }; return }
        HistorySectionV44.NONPATH -> { NonPathologicalHubV44Screen(lang) { section = null }; return }
        HistorySectionV44.GYNECO -> { GynecoObstetricGuideV52Screen(lang) { section = null }; return }
        HistorySectionV44.PATH -> { PathologicalV43Screen(lang) { section = null }; return }
        HistorySectionV44.SURGICAL -> { SurgicalTraumaHistoryV40Screen(lang) { section = null }; return }
        HistorySectionV44.PHYSICAL -> { PhysicalExamHubV44Screen(lang, onVitals) { section = null }; return }
        HistorySectionV44.OCCLUSION -> { OcclusionPhotoAtlasV46Screen(lang) { section = null }; return }
        HistorySectionV44.ORTHO -> { OrthodonticHistoryV45Screen(lang) { section = null }; return }
        HistorySectionV44.DENTAL_ANOMALIES -> { DentalAnomaliesPhotoV46Screen(lang) { section = null }; return }
        HistorySectionV44.HABITS -> { HabitsTeachingV40Screen(lang) { section = null }; return }
        HistorySectionV44.MUCOSA -> { MucosaPhotoAtlasV46Screen(lang) { section = null }; return }
        HistorySectionV44.PERIODONTAL -> { HistoryPeriodontalHubV52Screen(lang) { section = null }; return }
        HistorySectionV44.RADIOGRAPHIC -> { RadiographicStudiesV52Screen(lang) { section = null }; return }
        HistorySectionV44.AUXILIARIES -> { AuxiliariesV20Screen(lang) { section = null }; return }
        HistorySectionV44.ODONTOGRAM -> { HistoryOdontogramGuideV52Screen(lang) { section = null }; return }
        null -> Unit
    }

    ResponsiveScreenV17(
        tr(lang, "Historia clínica", "Clinical history"),
        tr(lang, "Índice educativo anónimo organizado como la historia clínica de referencia. No identifica ni enlaza a una escuela, clínica o institución.", "Anonymous teaching index organized like the reference clinical history. It does not identify or link to a school, clinic or institution."),
        onBack
    ) { profile ->
        AdaptiveGridV17(historyCardsV44.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
            val item = historyCardsV44[i]
            Card(
                onClick = { section = item.section },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(if (lang == "en") item.titleEn else item.titleEs, fontWeight = FontWeight.Black)
                    Text(if (lang == "en") item.detailEn else item.detailEs, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
