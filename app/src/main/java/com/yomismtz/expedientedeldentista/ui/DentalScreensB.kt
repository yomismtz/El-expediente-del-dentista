package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.Surface

@Composable
fun OlearyScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var primary by remember { mutableStateOf(false) }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    var selectedTooth by remember { mutableStateOf(shown.first()) }
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val selectedSurfaces = session.oleary[selectedTooth] ?: emptySet()
    val percentage = ClinicalEngines.round1(ClinicalEngines.olearyPercentage(session))

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "O’Leary",
                onBack,
                tr(lang, "Marca primero qué dientes están presentes. Después selecciona las superficies con placa; el porcentaje se calcula automáticamente.",
                    "First mark which teeth are present. Then select plaque-positive surfaces; the percentage is calculated automatically.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanentes", "Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporales", "Primary")) })
            }
        }
        item {
            SectionCard(tr(lang, "Dientes presentes", "Present teeth")) {
                Text(tr(lang, "Toca un número para incluirlo o excluirlo del conteo de superficies evaluables.",
                    "Tap a tooth number to include or exclude it from the evaluable-surface count."))
                shown.chunked(8).forEach { rowTeeth ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        rowTeeth.forEach { tooth ->
                            FilterChip(
                                selected = tooth in session.presentTeeth,
                                onClick = {
                                    val present = session.presentTeeth.toMutableSet()
                                    if (!present.add(tooth)) present.remove(tooth)
                                    onSessionChanged(session.copy(presentTeeth = present))
                                },
                                label = { Text(tooth.toString()) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(8 - rowTeeth.size) { Text("", modifier = Modifier.weight(1f)) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { onSessionChanged(session.copy(presentTeeth = shown.toSet())) }) {
                        Text(tr(lang, "Todos mostrados", "All shown"))
                    }
                    OutlinedButton(onClick = { onSessionChanged(session.copy(presentTeeth = session.presentTeeth - shown.toSet())) }) {
                        Text(tr(lang, "Ninguno", "None"))
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Superficies con placa", "Plaque-positive surfaces")) {
                ToothSelector(shown.filter { it in session.presentTeeth }.ifEmpty { shown }, selectedTooth, { selectedTooth = it }) { tooth ->
                    (session.oleary[tooth]?.isNotEmpty() == true)
                }
                if (selectedTooth !in session.presentTeeth) {
                    Text(tr(lang, "Este diente no está marcado como presente; inclúyelo arriba antes de registrar placa.",
                        "This tooth is not marked as present; include it above before recording plaque."))
                } else {
                    Surface.entries.forEach { surface ->
                        val label = when (surface) {
                            Surface.VESTIBULAR -> tr(lang, "Vestibular", "Buccal")
                            Surface.LINGUAL_PALATAL -> tr(lang, "Lingual / palatina", "Lingual / palatal")
                            Surface.MESIAL -> tr(lang, "Mesial", "Mesial")
                            Surface.DISTAL -> tr(lang, "Distal", "Distal")
                        }
                        FilterChip(
                            selected = surface in selectedSurfaces,
                            onClick = {
                                val map = session.oleary.toMutableMap()
                                val set = (map[selectedTooth] ?: emptySet()).toMutableSet()
                                if (!set.add(surface)) set.remove(surface)
                                map[selectedTooth] = set
                                onSessionChanged(session.copy(oleary = map))
                            },
                            label = { Text(label) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Resultado", "Result")) {
                val plaqueFaces = session.presentTeeth.sumOf { session.oleary[it]?.size ?: 0 }
                val totalFaces = session.presentTeeth.size * 4
                Text("$plaqueFaces / $totalFaces × 100", style = MaterialTheme.typography.titleMedium)
                Text("$percentage %", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun IpcScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val sextants = listOf("17–14", "13–23", "24–27", "47–44", "43–33", "34–37")
    val codes = listOf("0", "1", "2", "3", "4", "X", "9")
    val highest = ClinicalEngines.ipcHighest(session.ipcCodes)
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "IPC / CPI",
                onBack,
                tr(lang, "Registra el peor hallazgo de cada sextante. 0 sano, 1 sangrado, 2 cálculo, 3 bolsa 4–5 mm, 4 bolsa ≥6 mm, X excluido y 9 no registrable.",
                    "Record the worst finding in each sextant. 0 healthy, 1 bleeding, 2 calculus, 3 pocket 4–5 mm, 4 pocket ≥6 mm, X excluded, 9 not recordable.")
            )
        }
        items(sextants.size) { index ->
            SectionCard("${tr(lang, "Sextante", "Sextant")} ${index + 1} · ${sextants[index]}") {
                codes.chunked(4).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                        row.forEach { code ->
                            FilterChip(
                                selected = session.ipcCodes[index] == code,
                                onClick = {
                                    val list = session.ipcCodes.toMutableList()
                                    list[index] = code
                                    onSessionChanged(session.copy(ipcCodes = list))
                                },
                                label = { Text(code) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(4 - row.size) { Text("", modifier = Modifier.weight(1f)) }
                    }
                }
                Text(ClinicalEngines.ipcInterpretation(session.ipcCodes[index], lang), style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            SectionCard(tr(lang, "Código más alto", "Highest code")) {
                Text(highest, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(ClinicalEngines.ipcInterpretation(highest, lang))
            }
        }
    }
}

@Composable
fun IhosScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    val teeth = listOf(16, 11, 26, 36, 31, 46)
    val result = ClinicalEngines.ihos(session)
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "IHOS / OHI-S",
                onBack,
                tr(lang, "Dientes guía: 16V, 11V, 26V, 36L, 31V y 46L. Registra placa/detritos y cálculo de 0 a 3.",
                    "Index teeth: 16B, 11B, 26B, 36L, 31B and 46L. Record debris and calculus from 0 to 3.")
            )
        }
        items(teeth.size) { index ->
            val tooth = teeth[index]
            SectionCard("OD $tooth") {
                Text(if (tooth in listOf(36, 46)) tr(lang, "Superficie lingual", "Lingual surface") else tr(lang, "Superficie vestibular", "Buccal surface"))
                Text(tr(lang, "Placa / detritos", "Debris"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..3).forEach { code ->
                        FilterChip(
                            selected = (session.ihosDebris[tooth] ?: 0) == code,
                            onClick = { onSessionChanged(session.copy(ihosDebris = session.ihosDebris + (tooth to code))) },
                            label = { Text(code.toString()) }, modifier = Modifier.weight(1f)
                        )
                    }
                }
                Text(tr(lang, "Cálculo", "Calculus"), fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (0..3).forEach { code ->
                        FilterChip(
                            selected = (session.ihosCalculus[tooth] ?: 0) == code,
                            onClick = { onSessionChanged(session.copy(ihosCalculus = session.ihosCalculus + (tooth to code))) },
                            label = { Text(code.toString()) }, modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang, "Resultado IHOS", "OHI-S result")) {
                Text("${"%.2f".format(result)} / 6", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(ClinicalEngines.ihosInterpretation(result, lang))
            }
        }
    }
}
