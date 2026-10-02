package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

fun tr(lang: String, es: String, en: String): String = if (lang == "en") en else es

@Composable
fun ScreenHeader(title: String, onBack: () -> Unit, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onBack) { Text("←") }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
            )
        }
        if (!subtitle.isNullOrBlank()) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
fun NoticeCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text, modifier = Modifier.padding(14.dp), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun BooleanRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ChipChoices(
    labels: List<Pair<String, Boolean>>,
    onClick: (Int) -> Unit,
    columns: Int = 3
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val largeText = LocalDensity.current.fontScale >= 1.20f
        val longestLabel = labels.maxOfOrNull { it.first.length } ?: 0
        val requested = columns.coerceIn(1, 6)
        val responsiveColumns = when {
            maxWidth < 360.dp -> if (longestLabel > 28 || largeText && longestLabel > 20) 1 else requested.coerceAtMost(2)
            maxWidth < 480.dp -> when {
                longestLabel > 34 -> 1
                longestLabel > 18 || largeText -> requested.coerceAtMost(2)
                else -> requested.coerceAtMost(3)
            }
            maxWidth < 600.dp -> when {
                longestLabel > 36 -> requested.coerceAtMost(2)
                longestLabel > 20 || largeText -> requested.coerceAtMost(2)
                else -> requested.coerceAtMost(3)
            }
            maxWidth < 840.dp -> when {
                longestLabel > 40 -> requested.coerceAtMost(2)
                longestLabel > 24 || largeText -> requested.coerceAtMost(3)
                else -> requested.coerceAtMost(4)
            }
            maxWidth < 1200.dp -> when {
                longestLabel > 44 -> requested.coerceAtMost(3)
                largeText -> requested.coerceAtMost(4)
                else -> requested.coerceAtMost(5)
            }
            else -> when {
                longestLabel > 48 -> requested.coerceAtMost(4)
                largeText -> requested.coerceAtMost(5)
                else -> requested.coerceAtMost(6)
            }
        }.coerceAtLeast(1)
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            labels.chunked(responsiveColumns).forEachIndexed { rowIndex, row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEachIndexed { colIndex, item ->
                        val index = rowIndex * responsiveColumns + colIndex
                        FilterChip(
                            selected = item.second,
                            onClick = { onClick(index) },
                            label = { Text(item.first, softWrap = true) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(responsiveColumns - row.size) { androidx.compose.foundation.layout.Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}
