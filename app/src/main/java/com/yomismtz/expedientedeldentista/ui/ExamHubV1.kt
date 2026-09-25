package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

internal enum class V7Overlay {
    NONE, INTAKE, HUB,
    IDENTIFICATION, HISTORY, VITALS, ATM, OCCLUSION, MUCOSA, AUXILIARIES,
    ODONTOGRAM, ICDAS, CPOD, OLEARY, IPC, IHOS, PERIODONTAL, POSTURE,
    PULPAL_APICAL, ENDO, PROSTHETIC, SURGICAL, CONSENT, EVOLUTION
}

private data class HubExam(val icon: String, val es: String, val en: String, val target: V7Overlay)

@Composable
internal fun ExamHubV1Screen(lang: String, onOpen: (V7Overlay) -> Unit, onBack: () -> Unit) {
    val general = listOf(
        HubExam("👤", "Identificación", "Identification", V7Overlay.IDENTIFICATION),
        HubExam("📋", "Anamnesis / ASA", "History / ASA", V7Overlay.HISTORY),
        HubExam("♥", "Signos vitales", "Vital signs", V7Overlay.VITALS),
        HubExam("◉", "ATM y músculos", "TMJ and muscles", V7Overlay.ATM),
        HubExam("⌁", "Oclusión", "Occlusion", V7Overlay.OCCLUSION),
        HubExam("◒", "Mucosas", "Mucosa", V7Overlay.MUCOSA),
        HubExam("▣", "Auxiliares", "Auxiliaries", V7Overlay.AUXILIARIES),
        HubExam("🧍", "Postura", "Posture", V7Overlay.POSTURE)
    )
    val analyses = listOf(
        HubExam("◇", "Odontograma", "Odontogram", V7Overlay.ODONTOGRAM),
        HubExam("🔎", "ICDAS", "ICDAS", V7Overlay.ICDAS),
        HubExam("➕", "CPOD / ceod", "DMFT / dmft", V7Overlay.CPOD),
        HubExam("🔴", "O'Leary", "O'Leary", V7Overlay.OLEARY),
        HubExam("6️⃣", "IPC", "CPI", V7Overlay.IPC),
        HubExam("🪥", "IHOS", "OHI-S", V7Overlay.IHOS),
        HubExam("▥", "Periodontograma", "Periodontal chart", V7Overlay.PERIODONTAL),
        HubExam("⚡", "Pulpar + periapical", "Pulpal + apical", V7Overlay.PULPAL_APICAL)
    )
    val treatment = listOf(
        HubExam("∿", "Endodoncia", "Endodontics", V7Overlay.ENDO),
        HubExam("⌂", "Prótesis / Kennedy", "Prosthodontics / Kennedy", V7Overlay.PROSTHETIC),
        HubExam("✚", "Cirugía", "Surgery", V7Overlay.SURGICAL),
        HubExam("✍", "Consentimiento", "Consent", V7Overlay.CONSENT),
        HubExam("📄", "Notas de evolución", "Progress notes", V7Overlay.EVOLUTION)
    )

    ResponsiveScreenV17(
        tr(lang, "Exámenes interactivos", "Interactive examinations"),
        tr(lang,
            "La cuadrícula cambia automáticamente según el ancho de pantalla y el tamaño de letra de Android.",
            "The grid changes automatically according to screen width and Android text size."),
        onBack
    ) { profile ->
        HubGroupV17(tr(lang, "Generales", "General"), general, lang, onOpen, profile)
        HubGroupV17(tr(lang, "Análisis", "Analyses"), analyses, lang, onOpen, profile)
        HubGroupV17(tr(lang, "Tratamiento y documentación", "Treatment and documentation"), treatment, lang, onOpen, profile)
        ResponsiveSectionV17(tr(lang, "Regla de cierre", "Closing rule")) {
            Text(tr(lang,
                "Cada examen termina con: resultado → interpretación → hallazgos que lo apoyan → qué falta → ejemplo de redacción para el expediente físico.",
                "Every examination ends with: result → interpretation → supporting findings → missing information → a writing example for the physical record."))
        }
    }
}

@Composable
private fun HubGroupV17(
    title: String,
    exams: List<HubExam>,
    lang: String,
    onOpen: (V7Overlay) -> Unit,
    profile: ScreenProfileV17
) {
    ResponsiveSectionV17(title) {
        val columns = when {
            profile.largeSystemText -> 1
            profile.width == ScreenWidthV17.COMPACT -> 2
            profile.width == ScreenWidthV17.MEDIUM -> 2
            else -> 3
        }
        AdaptiveGridV17(exams.size, columns) { index ->
            val exam = exams[index]
            Card(
                onClick = { onOpen(exam.target) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .62f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .35f)),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text(exam.icon, style = MaterialTheme.typography.titleLarge)
                    Column(Modifier.weight(1f)) {
                        Text(if (lang == "en") exam.en else exam.es, fontWeight = FontWeight.Bold)
                        Text(tr(lang, "Abrir examen", "Open exam"), style = MaterialTheme.typography.bodyMedium)
                    }
                    Text("›", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}
