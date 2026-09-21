package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun GynecoObstetricGuideV52Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "VI. Antecedentes gineco-obstétricos", "VI. Gynecologic-obstetric history"),
        tr(lang, "Guía educativa anónima: enseña qué tipo de información corresponde al apartado sin almacenar datos de pacientes.", "Anonymous teaching guide: shows the type of information that belongs in this section without storing patient data."),
        onBack
    ) {
        NoticeCard(tr(lang, "Conserva este apartado separado de los antecedentes personales no patológicos, como en el formato de referencia. Registra únicamente lo que el paciente refiera y lo que el formato clínico solicite.", "Keep this section separate from non-pathological history, as in the reference form. Record only what the patient reports and what the clinical form requests."))
        ResponsiveSectionV17(tr(lang, "Cómo estudiar el apartado", "How to study this section")) {
            Text(tr(lang, "Identifica primero los campos del formato físico; después practica una redacción objetiva, cronológica y sin inferir diagnósticos a partir de antecedentes incompletos.", "First identify the fields on the physical form; then practice objective, chronological wording without inferring diagnoses from incomplete history."))
        }
    }
}

@Composable
internal fun HistoryPeriodontalHubV52Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "XV. Examen periodontal", "XV. Periodontal examination"),
        tr(lang, "El formato de referencia agrupa IPC, IHOS y observaciones gingivales/periodontales.", "The reference form groups CPI, OHI-S and gingival/periodontal observations."),
        onBack
    ) {
        ResponsiveSectionV17("IPC / CPI") { Text(tr(lang, "Evalúa el índice con su protocolo por edad, sextantes/dientes índice y hallazgos registrados.", "Use the age protocol, sextants/index teeth and recorded findings.")) }
        ResponsiveSectionV17("IHOS / OHI-S") { Text(tr(lang, "Registra depósitos blandos y cálculo en las superficies indicadas y calcula el índice.", "Record debris and calculus on the indicated surfaces and calculate the index.")) }
        ResponsiveSectionV17(tr(lang, "Observaciones periodontales", "Periodontal observations")) { Text(tr(lang, "Describe encía, sangrado, cálculo, movilidad y otros hallazgos observables sin convertir una imagen aislada en diagnóstico.", "Describe gingiva, bleeding, calculus, mobility and other observable findings without turning an isolated image into a diagnosis.")) }
        NoticeCard(tr(lang, "Los módulos interactivos IPC, IHOS y periodontograma siguen disponibles en el índice general del expediente.", "Interactive CPI, OHI-S and periodontogram modules remain available from the general record index."))
    }
}

@Composable
internal fun RadiographicStudiesV52Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "XVI. Estudios radiográficos", "XVI. Radiographic studies"),
        tr(lang, "Organizado según el apartado radiográfico del formato de referencia.", "Organized according to the radiographic section of the reference form."),
        onBack
    ) {
        val studies = listOf(
            "Dentoalveolar / periapical" to "Dentoalveolar / periapical",
            "Aleta mordible" to "Bitewing",
            "Oclusal" to "Occlusal",
            "Extraoral" to "Extraoral"
        )
        studies.forEach { (es, en) ->
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (lang == "en") en else es, fontWeight = FontWeight.Black)
                    Text(tr(lang, "Primero identifica el tipo de estudio; después describe hallazgos objetivos y correlación clínica.", "Identify the study type first; then describe objective findings and clinical correlation."))
                }
            }
        }
    }
}

@Composable
internal fun HistoryOdontogramGuideV52Screen(lang: String, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "XVIII. Odontograma · CPOD/ceod", "XVIII. Odontogram · DMFT/dmft"),
        tr(lang, "Cierre del índice de historia clínica de referencia.", "Final section of the reference clinical-history index."),
        onBack
    ) {
        ResponsiveSectionV17(tr(lang, "Odontograma", "Odontogram")) {
            Text(tr(lang, "Registra por órgano dentario y superficie usando la convención del formato. Diferencia hallazgo clínico, restauración, ausencia y tratamiento indicado.", "Record by tooth and surface using the form convention. Distinguish clinical finding, restoration, absence and planned treatment."))
        }
        ResponsiveSectionV17("CPOD / ceod") {
            Text(tr(lang, "Separa dentición permanente y temporal y evita mezclar los componentes de ambos índices.", "Keep permanent and primary dentitions separate and do not mix the components of the two indices."))
        }
        NoticeCard(tr(lang, "El odontograma interactivo y la calculadora CPOD/ceod permanecen en Odontograma e índices para conservar toda la funcionalidad existente.", "The interactive odontogram and DMFT/dmft calculator remain under Odontogram and indices to preserve existing functionality."))
    }
}
