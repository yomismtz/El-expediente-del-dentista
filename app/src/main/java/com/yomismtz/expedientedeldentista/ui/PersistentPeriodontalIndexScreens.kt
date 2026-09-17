package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

private data class PersistentIpcSextant(val number: Int, val label: String, val teeth: List<Int>)

private val persistentIpcSextants = listOf(
    PersistentIpcSextant(1, "18–14", listOf(18, 17, 16, 15, 14)),
    PersistentIpcSextant(2, "13–23", listOf(13, 12, 11, 21, 22, 23)),
    PersistentIpcSextant(3, "24–28", listOf(24, 25, 26, 27, 28)),
    PersistentIpcSextant(4, "34–38", listOf(34, 35, 36, 37, 38)),
    PersistentIpcSextant(5, "33–43", listOf(33, 32, 31, 41, 42, 43)),
    PersistentIpcSextant(6, "44–48", listOf(44, 45, 46, 47, 48))
)

private val persistentIpcSites = listOf("MV", "V", "DV", "ML/P", "L/P", "DL/P")

private fun normalizeIpcSites(values: List<String>?): List<String> =
    List(6) { index -> values?.getOrNull(index) ?: "0" }

private fun highestPersistentIpcCode(values: List<String>): String {
    val numeric = values.mapNotNull { it.toIntOrNull() }
    return numeric.maxOrNull()?.toString() ?: if (values.any { it == "X" }) "X" else "0"
}

@Composable
fun IpcPersistentV22Screen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var sextantIndex by remember { mutableStateOf(0) }
    var selectedTooth by remember { mutableStateOf(persistentIpcSextants.first().teeth.first()) }
    var selectedSite by remember { mutableStateOf(0) }

    fun toothCodes(tooth: Int): List<String> = normalizeIpcSites(session.ipcSiteCodes[tooth])
    fun toothResult(tooth: Int): String = highestPersistentIpcCode(toothCodes(tooth))
    fun sextantResult(sextant: PersistentIpcSextant): String {
        val results = sextant.teeth.map { toothResult(it) }
        return if (results.count { it != "X" } < 2) "X" else highestPersistentIpcCode(results)
    }

    fun updateSite(code: String) {
        val list = toothCodes(selectedTooth).toMutableList()
        list[selectedSite] = code
        val updatedSites = session.ipcSiteCodes + (selectedTooth to list)
        val summary = persistentIpcSextants.map { sextant ->
            val results = sextant.teeth.map { tooth ->
                highestPersistentIpcCode(normalizeIpcSites(updatedSites[tooth]))
            }
            if (results.count { it != "X" } < 2) "X" else highestPersistentIpcCode(results)
        }
        onSessionChanged(session.copy(ipcSiteCodes = updatedSites, ipcCodes = summary))
    }

    ResponsiveScreenV17(
        tr(lang, "IPC por sextantes", "CPI by sextants"),
        tr(
            lang,
            "Los seis sitios por diente quedan guardados durante toda la sesión educativa.",
            "All six sites per tooth remain stored for the full educational session."
        ),
        onBack
    ) { profile ->
        val sextantColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
        ResponsiveSectionV17(tr(lang, "1 · Selecciona sextante", "1 · Select sextant")) {
            AdaptiveGridV17(persistentIpcSextants.size, sextantColumns) { index ->
                val sextant = persistentIpcSextants[index]
                Card(
                    onClick = {
                        sextantIndex = index
                        selectedTooth = sextant.teeth.first()
                        selectedSite = 0
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (sextantIndex == index) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (sextantIndex == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha = .4f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "S${sextant.number} · ${sextant.label} · ${sextantResult(sextant)}",
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        val selectedSextant = persistentIpcSextants[sextantIndex]
        ResponsiveSectionV17("${tr(lang, "2 · Dientes S", "2 · Teeth S")}${selectedSextant.number}") {
            val columns = when {
                profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT -> 2
                profile.width == ScreenWidthV17.MEDIUM -> 3
                else -> selectedSextant.teeth.size
            }
            AdaptiveGridV17(selectedSextant.teeth.size, columns) { index ->
                val tooth = selectedSextant.teeth[index]
                FilterChip(
                    selected = selectedTooth == tooth,
                    onClick = {
                        selectedTooth = tooth
                        selectedSite = 0
                    },
                    label = { Text("OD $tooth · ${toothResult(tooth)}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17("${tr(lang, "3 · Seis sitios OD", "3 · Six sites tooth")} $selectedTooth") {
            val siteColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 3
            AdaptiveGridV17(persistentIpcSites.size, siteColumns) { index ->
                FilterChip(
                    selected = selectedSite == index,
                    onClick = { selectedSite = index },
                    label = { Text("${persistentIpcSites[index]} · ${toothCodes(selectedTooth)[index]}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text("${tr(lang, "Código para", "Code for")} ${persistentIpcSites[selectedSite]}", fontWeight = FontWeight.Black)
            val codeColumns = if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 3 else 6
            val codes = listOf("0", "1", "2", "3", "4", "X")
            AdaptiveGridV17(codes.size, codeColumns) { index ->
                val code = codes[index]
                FilterChip(
                    selected = toothCodes(selectedTooth)[selectedSite] == code,
                    onClick = { updateSite(code) },
                    label = { Text(code) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "Resultado automático", "Automatic result")) {
            Text(
                persistentIpcSextants.joinToString(" · ") { "S${it.number}=${sextantResult(it)}" },
                fontWeight = FontWeight.Black
            )
            Text(
                tr(
                    lang,
                    "0 sin hallazgo · 1 sangrado · 2 cálculo/factor retentivo · 3 bolsa 4–5 mm · 4 bolsa ≥6 mm · X no evaluable.",
                    "0 no finding · 1 bleeding · 2 calculus/retentive factor · 3 pocket 4–5 mm · 4 pocket ≥6 mm · X not evaluable."
                )
            )
        }

        NoticeCard(
            tr(
                lang,
                "Qué escribir: IPC: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Añade los hallazgos periodontales relevantes.",
                "What to write: CPI: S1=__ · S2=__ · S3=__ · S4=__ · S5=__ · S6=__. Add relevant periodontal findings."
            )
        )
    }
}
