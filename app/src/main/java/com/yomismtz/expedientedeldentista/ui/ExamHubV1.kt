package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

internal enum class V7Overlay {
    NONE, INTAKE, HUB,
    IDENTIFICATION, HISTORY, VITALS, ATM, OCCLUSION, MUCOSA, AUXILIARIES,
    ODONTOGRAM, ICDAS, CPOD, OLEARY, IPC, IHOS, PERIODONTAL, POSTURE,
    PULPAL_APICAL, ENDO, PROSTHETIC, SURGICAL, CONSENT, EVOLUTION
}

private data class HubExam(val icon: String, val es: String, val en: String, val target: V7Overlay)

private val HubLavender = Color(0xFFE9DDF5)
private val HubLilac = Color(0xFFD3BCE9)
private val HubPurple = Color(0xFF7447A3)
private val HubDeep = Color(0xFF43235F)
private val HubMint = Color(0xFF66D6C7)
private val HubTurquoise = Color(0xFF2EB9B1)

@Composable
internal fun ExamHubV1Screen(lang: String, onOpen: (V7Overlay) -> Unit, onBack: () -> Unit) {
    val general = listOf(
        HubExam("▤", "Identificación", "Identification", V7Overlay.IDENTIFICATION),
        HubExam("●", "Anamnesis / ASA", "History / ASA", V7Overlay.HISTORY),
        HubExam("♥", "Signos vitales", "Vital signs", V7Overlay.VITALS),
        HubExam("◌", "ATM y músculos", "TMJ and muscles", V7Overlay.ATM),
        HubExam("◇", "Oclusión", "Occlusion", V7Overlay.OCCLUSION),
        HubExam("◡", "Mucosas", "Mucosa", V7Overlay.MUCOSA),
        HubExam("⚗", "Auxiliares", "Auxiliaries", V7Overlay.AUXILIARIES),
        HubExam("↕", "Postura", "Posture", V7Overlay.POSTURE)
    )
    val analyses = listOf(
        HubExam("🦷", "Odontograma", "Odontogram", V7Overlay.ODONTOGRAM),
        HubExam("▣", "ICDAS", "ICDAS", V7Overlay.ICDAS),
        HubExam("+", "CPOD / ceod", "DMFT / dmft", V7Overlay.CPOD),
        HubExam("⌁", "O'Leary", "O'Leary", V7Overlay.OLEARY),
        HubExam("Ⅵ", "IPC", "CPI", V7Overlay.IPC),
        HubExam("▥", "IHOS", "OHI-S", V7Overlay.IHOS),
        HubExam("⌇", "Periodontograma", "Periodontal chart", V7Overlay.PERIODONTAL),
        HubExam("⚡", "Pulpar + periapical", "Pulpal + apical", V7Overlay.PULPAL_APICAL)
    )
    val treatment = listOf(
        HubExam("│", "Endodoncia", "Endodontics", V7Overlay.ENDO),
        HubExam("⌒", "Prótesis / Kennedy", "Prosthetics / Kennedy", V7Overlay.PROSTHETIC),
        HubExam("✦", "Cirugía", "Surgery", V7Overlay.SURGICAL),
        HubExam("✓", "Consentimiento", "Consent", V7Overlay.CONSENT),
        HubExam("▤", "Notas de evolución", "Progress notes", V7Overlay.EVOLUTION)
    )

    LazyColumn(
        Modifier.fillMaxSize().safeDrawingPadding().background(
            Brush.verticalGradient(listOf(HubLavender, Color(0xFFF8F4FB), Color.White))
        ).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                tr(lang, "Exámenes interactivos", "Interactive examinations"),
                onBack,
                tr(lang,
                    "Abre el examen que estés realizando. Al terminar, usa “Qué escribir” para ver cómo llevar el resultado al expediente físico.",
                    "Open the examination you are performing. When finished, use “What to write” to see how to transfer the result to the physical record."
                )
            )
        }
        item { HubGroup(tr(lang, "Generales", "General"), general, lang, onOpen, HubLavender) }
        item { HubGroup(tr(lang, "Análisis", "Analyses"), analyses, lang, onOpen, HubLilac) }
        item { HubGroup(tr(lang, "Tratamiento y documentación", "Treatment and documentation"), treatment, lang, onOpen, HubPurple.copy(alpha = .18f)) }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = HubMint.copy(alpha = .20f)),
                border = BorderStroke(1.dp, HubTurquoise),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(tr(lang, "Regla de cierre", "Closing rule"), fontWeight = FontWeight.Black, color = HubDeep)
                    Text(tr(lang,
                        "Cada examen termina con: resultado → interpretación → hallazgos que lo apoyan → qué falta → ejemplo de redacción.",
                        "Every examination ends with: result → interpretation → supporting findings → missing information → writing example."
                    ))
                }
            }
        }
    }
}

@Composable
private fun HubGroup(title: String, exams: List<HubExam>, lang: String, onOpen: (V7Overlay) -> Unit, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, HubLilac),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontWeight = FontWeight.Black, color = HubDeep)
            exams.chunked(2).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { exam ->
                        Card(
                            modifier = Modifier.weight(1f),
                            onClick = { onOpen(exam.target) },
                            colors = CardDefaults.cardColors(containerColor = color),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                Text(exam.icon, fontWeight = FontWeight.Bold, color = HubDeep)
                                Text(if (lang == "en") exam.en else exam.es, fontWeight = FontWeight.Bold, color = HubDeep)
                            }
                        }
                    }
                    if (row.size == 1) Column(Modifier.weight(1f)) { }
                }
            }
        }
    }
}
