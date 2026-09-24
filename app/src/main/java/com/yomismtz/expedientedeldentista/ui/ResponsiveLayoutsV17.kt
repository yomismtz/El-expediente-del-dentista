package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

internal enum class ScreenWidthV17 { COMPACT, MEDIUM, EXPANDED }

internal data class ScreenProfileV17(
    val width: ScreenWidthV17,
    val largeSystemText: Boolean,
    val columns: Int,
    val horizontalPadding: androidx.compose.ui.unit.Dp
)

@Composable
internal fun ResponsiveScreenV17(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    content: @Composable (ScreenProfileV17) -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val systemFontScale = LocalDensity.current.fontScale
        val widthClass = when {
            maxWidth < 360.dp -> ScreenWidthV17.COMPACT
            maxWidth < 600.dp -> ScreenWidthV17.MEDIUM
            else -> ScreenWidthV17.EXPANDED
        }
        val largeText = systemFontScale >= 1.20f
        val columns = when {
            largeText || widthClass == ScreenWidthV17.COMPACT -> 1
            widthClass == ScreenWidthV17.MEDIUM -> 2
            else -> 3
        }
        val padding = when (widthClass) {
            ScreenWidthV17.COMPACT -> VisualSpacingV49.md
            ScreenWidthV17.MEDIUM -> VisualSpacingV49.lg
            ScreenWidthV17.EXPANDED -> VisualSpacingV49.xxl
        }
        val profile = ScreenProfileV17(widthClass, largeText, columns, padding)

        Column(
            Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = padding,
                    end = padding,
                    top = VisualSpacingV49.md,
                    bottom = VisualSpacingV49.lg
                ),
            verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.md)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(VisualSpacingV49.md)
            ) {
                if (onBack != null) {
                    OutlinedButton(onClick = onBack, shape = MaterialTheme.shapes.small) { Text("‹") }
                }
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    if (!subtitle.isNullOrBlank()) {
                        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            content(profile)
        }
    }
}

@Composable
internal fun AdaptiveGridV17(
    itemCount: Int,
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    val safeColumns = columns.coerceAtLeast(1)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.sm)) {
        (0 until itemCount).toList().chunked(safeColumns).forEach { rowItems ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(VisualSpacingV49.sm)) {
                rowItems.forEach { index ->
                    Column(Modifier.weight(1f)) { content(index) }
                }
                repeat(safeColumns - rowItems.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
internal fun ResponsiveSectionV17(
    title: String,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(VisualSpacingV49.lg),
            verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.md)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            if (!subtitle.isNullOrBlank()) Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            content()
        }
    }
}

internal fun responsiveColumnsV17(profile: ScreenProfileV17, preferredExpanded: Int = 3): Int = when {
    profile.largeSystemText -> 1
    profile.width == ScreenWidthV17.COMPACT -> 1
    profile.width == ScreenWidthV17.MEDIUM -> 2
    else -> preferredExpanded.coerceAtLeast(2)
}
