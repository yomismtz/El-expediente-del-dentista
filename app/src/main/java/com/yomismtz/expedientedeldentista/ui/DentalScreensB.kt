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
import androidx.compose.ui.graphics.Color
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
    val olearySurfaces = listOf(Surface.VESTIBULAR, Surface.LINGUAL_PALATAL, Surface.MESIAL, Surface.DISTAL)
    val present = if (session.presentTeeth.intersect(shown.toSet()).isEmpty()) shown.toSet() else session.presentTeeth.intersect(shown.toSet())
    val selectedSurfaces = (session.oleary[selectedTooth] ?: emptySet()).intersect(olearySurfaces.toSet())
    val plaqueFaces = present.sumOf { tooth -> (session.oleary[tooth] ?: emptySet()).count { it in olearySurfaces } }
    val totalFaces = present.size * 4
    val percentage = if (totalFaces == 0) 0.0 else ClinicalEngines.round1(plaqueFaces * 100.0 / totalFaces)

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "O’Leary",
                onBack,
                tr(lang,
                    "Cada diente se divide en cuatro caras: vestibular, lingual/palatina, mesial y distal. Toca directamente la cara donde hay placa; la superficie oclusal NO forma parte de este índice.",
                    "Each tooth is divided into four surfaces: buccal, lingual/palatal, mesial and distal. Tap the plaque-positive surface directly; the occlusal surface is NOT part of this index.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang, "Permanentes", "Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang, "Temporales", "Primary")) })
            }
        }
        item {
            SectionCard(tr(lang, "Arcada y dientes evaluables", "Arch and evaluable teeth")) {
                Text(tr(lang,
                    "Por defecto se muestran como presentes. Selecciona un diente y usa “Excluir” si falta; sus cuatro caras salen del denominador.",
                    "Teeth are shown as present by default. Select a tooth and use “Exclude” if it is missing; its four surfaces leave the denominator."))
                DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { tooth ->
                    session.oleary[tooth]?.any { it in olearySurfaces } == true
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedTooth in present,
                        onClick = {
                            val updated = present.toMutableSet().apply { add(selectedTooth) }
                            onSessionChanged(session.copy(presentTeeth = updated))
                        },
                        label = { Text(tr(lang, "Presente", "Present")) }
                    )
                    FilterChip(
                        selected = selectedTooth !in present,
                        onClick = {
                            val updated = present.toMutableSet().apply { remove(selectedTooth) }
                            val marks = session.oleary.toMutableMap().apply { remove(selectedTooth) }
                            onSessionChanged(session.copy(presentTeeth = updated, oleary = marks))
                        },
                        label = { Text(tr(lang, "Excluir por ausencia", "Exclude as missing")) }
                    )
                }
            }
        }
        item {
            SectionCard("OD $selectedTooth · ${tr(lang, "control de placa", "plaque control")}") {
                if (selectedTooth !in present) {
                    Text(tr(lang, "Este diente está excluido. Márcalo como presente para registrar sus superficies.",
                        "This tooth is excluded. Mark it present to record its surfaces."))
                } else {
                    DentalSurfaceDiagram(
                        centerEnabled = false,
                        surfaceColor = { surface ->
                            if (surface in selectedSurfaces) Color(0xFFD64545) else MaterialTheme.colorScheme.surfaceVariant
                        },
                        onSurfaceTap = { surface ->
                            if (surface in olearySurfaces) {
                                val map = session.oleary.toMutableMap()
                                val set = (map[selectedTooth] ?: emptySet()).filter { it in olearySurfaces }.toMutableSet()
                                if (!set.add(surface)) set.remove(surface)
                                map[selectedTooth] = set
                                onSessionChanged(session.copy(oleary = map, presentTeeth = present))
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(tr(lang, "Rojo = superficie con placa dentobacteriana.", "Red = plaque-positive surface."),
                        color = Color(0xFFD64545), fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            SectionCard(tr(lang, "Cálculo", "Calculation")) {
                Text("$plaqueFaces / $totalFaces × 100", style = MaterialTheme.typography.titleMedium)
                Text("$percentage %", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(tr(lang,
                    "Caras evaluables = dientes presentes × 4. Se cuentan únicamente las caras marcadas con placa.",
                    "Evaluable surfaces = present teeth × 4. Only plaque-positive surfaces are counted."))
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
