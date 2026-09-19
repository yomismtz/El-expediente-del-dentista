package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

fun tr(lang: String, es: String, en: String): String = if (lang == "en") en else es

@Composable
fun ScreenHeader(title: String, onBack: () -> Unit, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.sm)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onBack, shape = MaterialTheme.shapes.small) { Text("‹") }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(horizontal = VisualSpacingV49.md)
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
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(VisualSpacingV49.lg),
            verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.md)
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
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text,
            modifier = Modifier.padding(VisualSpacingV49.md),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
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
    labels.chunked(columns).forEachIndexed { rowIndex, row ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(VisualSpacingV49.sm)
        ) {
            row.forEachIndexed { colIndex, item ->
                val index = rowIndex * columns + colIndex
                FilterChip(
                    selected = item.second,
                    onClick = { onClick(index) },
                    label = { Text(item.first) },
                    modifier = Modifier.weight(1f)
                )
            }
            repeat(columns - row.size) { Text("", modifier = Modifier.weight(1f)) }
        }
    }
}


@Composable
fun PracticeSaveControlsV48(
    lang: String,
    sectionKey: String,
    modifier: Modifier = Modifier
) {
    val saved = TeachingStateV40.savedPracticeSections[sectionKey] == true
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.xs)
    ) {
        Button(
            onClick = { TeachingStateV40.savedPracticeSections[sectionKey] = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (saved) {
                    tr(lang, "Guardado ✓", "Saved ✓")
                } else {
                    tr(lang, "Guardar práctica", "Save practice")
                },
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            tr(
                lang,
                "Guarda únicamente el avance educativo de esta sección durante la sesión actual. No almacena datos personales ni sustituye el expediente clínico institucional.",
                "This only saves the educational progress of this section during the current session. It does not store personal data or replace the institutional clinical record."
            ),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
