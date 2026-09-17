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

private enum class HistorySectionV44 { IDENTIFICATION, COMPLAINT, FAMILY, NONPATH, PATH, SURGICAL, PHYSICAL, ORTHO }
private data class HistoryCardV44(val section: HistorySectionV44, val title: String, val detail: String)
private val historyCardsV44 = listOf(
    HistoryCardV44(HistorySectionV44.IDENTIFICATION, "Identificación del paciente", "Qué datos suelen ir en el expediente real y para qué sirven; aquí no se capturan datos personales."),
    HistoryCardV44(HistorySectionV44.COMPLAINT, "Motivo de consulta y padecimiento actual", "30 motivos frecuentes y tres hipótesis de padecimiento actual por ejemplo."),
    HistoryCardV44(HistorySectionV44.FAMILY, "Antecedentes heredo-familiares", "Familiar → categoría → lista de enfermedades frecuentes; no requiere escribir nombres."),
    HistoryCardV44(HistorySectionV44.NONPATH, "Antecedentes personales no patológicos", "Vivienda, higiene, alimentación, hábitos y antecedentes gineco-obstétricos."),
    HistoryCardV44(HistorySectionV44.PATH, "Antecedentes personales patológicos", "Vacunación, enfermedades, medicamentos y atlas visual de exantemas/dermatología."),
    HistoryCardV44(HistorySectionV44.SURGICAL, "Antecedentes quirúrgicos y traumáticos", "Cirugías, hospitalizaciones, transfusiones, fracturas/luxaciones y resumen."),
    HistoryCardV44(HistorySectionV44.PHYSICAL, "Exploración física", "Dos rutas: signos vitales/glucosa e inspección general con los 10 rubros clínicos."),
    HistoryCardV44(HistorySectionV44.ORTHO, "Antecedentes ortodónticos y ortopédicos", "Brackets, aparatos de ortopedia, para qué sirven, duración orientativa y motivo/diagnóstico referido.")
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
        HistorySectionV44.PATH -> { PathologicalV43Screen(lang) { section = null }; return }
        HistorySectionV44.SURGICAL -> { SurgicalTraumaHistoryV40Screen(lang) { section = null }; return }
        HistorySectionV44.PHYSICAL -> { PhysicalExamHubV44Screen(lang, onVitals) { section = null }; return }
        HistorySectionV44.ORTHO -> { OrthodonticHistoryV45Screen(lang) { section = null }; return }
        null -> Unit
    }

    ResponsiveScreenV17(
        tr(lang, "Historia clínica", "Clinical history"),
        tr(lang, "ATM y oclusión permanecen únicamente en el menú principal para evitar duplicados.", "TMJ and occlusion remain only in the main menu to avoid duplicates."),
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
                    Text(item.title, fontWeight = FontWeight.Black)
                    Text(item.detail, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
