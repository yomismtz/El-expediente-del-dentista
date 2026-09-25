package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Surface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    BoxWithConstraints(Modifier.fillMaxSize()) {
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
            ScreenWidthV17.COMPACT -> 10.dp
            ScreenWidthV17.MEDIUM -> 14.dp
            ScreenWidthV17.EXPANDED -> 22.dp
        }
        val profile = ScreenProfileV17(widthClass, largeText, columns, padding)

        Column(
            Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = padding, end = padding, top = 12.dp, bottom = 112.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (onBack != null) {
                    OutlinedButton(onClick = onBack) { Text("‹") }
                }
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    if (!subtitle.isNullOrBlank()) {
                        Text(subtitle, style = MaterialTheme.typography.bodyMedium)
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
    val availableWidth = with(LocalDensity.current) { androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp }
    val largeText = LocalDensity.current.fontScale >= 1.20f
    val requested = columns.coerceIn(1, 5)
    val safeColumns = when {
        availableWidth < 360.dp -> if (largeText) 1 else requested.coerceAtMost(2)
        availableWidth < 600.dp -> if (largeText) requested.coerceAtMost(2) else requested.coerceAtMost(3)
        availableWidth < 900.dp -> if (largeText) requested.coerceAtMost(3) else requested.coerceAtMost(4)
        else -> if (largeText) requested.coerceAtMost(4) else requested
    }.coerceAtLeast(1)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        (0 until itemCount).toList().chunked(safeColumns).forEach { rowItems ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, modifier=Modifier.weight(1f))
                SectionHelpV22(subtitle)
            }
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


@Composable
internal fun SectionHelpV22(text:String?) {
    if(text.isNullOrBlank()) return
    var open by remember { mutableStateOf(false) }
    OutlinedButton(onClick={open=!open}) { Text("?") }
    if(open) Surface(color=MaterialTheme.colorScheme.secondaryContainer,shape=MaterialTheme.shapes.medium) { Text(text,Modifier.padding(10.dp),style=MaterialTheme.typography.bodySmall) }
}

@Composable
internal fun CompletionBadgeV22(done:Boolean,inProgress:Boolean,lang:String) {
    val text=when { done -> "✓ "+tr(lang,"Completado","Completed"); inProgress -> "● "+tr(lang,"En progreso","In progress"); else -> "○ "+tr(lang,"Pendiente","Pending") }
    val color=when { done -> MaterialTheme.colorScheme.secondaryContainer; inProgress -> MaterialTheme.colorScheme.primaryContainer; else -> MaterialTheme.colorScheme.surfaceVariant }
    Surface(color=color,shape=MaterialTheme.shapes.small){ Text(text,Modifier.padding(horizontal=8.dp,vertical=4.dp),style=MaterialTheme.typography.labelMedium,fontWeight=FontWeight.Bold) }
}


@Composable
internal fun ExpandableTeachingCardV22(title:String,summary:String,details:String,icon:String="💡") {
    var expanded by remember { mutableStateOf(false) }
    val bg by animateColorAsState(if(expanded) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,animationSpec=tween(180),label="expandCard")
    Card(Modifier.fillMaxWidth().clickable{expanded=!expanded},colors=CardDefaults.cardColors(containerColor=bg),shape=MaterialTheme.shapes.large,border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.32f))){
        Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
            Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){Text("$icon $title",Modifier.weight(1f),fontWeight=FontWeight.Bold);Text(if(expanded)"⌃" else "⌄")}
            Text(summary,style=MaterialTheme.typography.bodyMedium)
            AnimatedVisibility(expanded){Text(details,style=MaterialTheme.typography.bodySmall)}
        }
    }
}

@Composable
internal fun FieldValidationBadgeV22(state:Int,lang:String){
    val label=when(state){2->"✓ "+tr(lang,"Completo","Complete");1->"● "+tr(lang,"Revisar","Review");else->"○ "+tr(lang,"Pendiente","Pending")}
    val bg=when(state){2->MaterialTheme.colorScheme.secondaryContainer;1->MaterialTheme.colorScheme.tertiaryContainer;else->MaterialTheme.colorScheme.surfaceVariant}
    Surface(color=bg,shape=MaterialTheme.shapes.small){Text(label,Modifier.padding(horizontal=8.dp,vertical=4.dp),style=MaterialTheme.typography.labelSmall,fontWeight=FontWeight.Bold)}
}
