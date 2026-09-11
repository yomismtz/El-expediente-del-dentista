package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.Surface

@Composable
fun DentalArchSelector(
    teeth: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    isMarked: (Int) -> Boolean = { false }
) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        teeth.forEach { tooth ->
            Card(
                modifier = Modifier.width(58.dp).clickable { onSelected(tooth) },
                colors = CardDefaults.cardColors(
                    containerColor = if (tooth == selected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(if (isMarked(tooth)) "🦷•" else "🦷", style = MaterialTheme.typography.titleMedium)
                    Text(tooth.toString(), fontWeight = if (tooth == selected) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
    }
}

@Composable
fun DentalSurfaceDiagram(
    centerEnabled: Boolean,
    surfaceColor: (Surface) -> Color,
    onSurfaceTap: (Surface) -> Unit,
    modifier: Modifier = Modifier
) {
    val border = MaterialTheme.colorScheme.outline
    val background = MaterialTheme.colorScheme.surfaceVariant

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("V", fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("M", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Canvas(
                modifier = Modifier
                    .size(190.dp)
                    .pointerInput(centerEnabled) {
                        detectTapGestures { offset ->
                            val w = size.width.toFloat()
                            val h = size.height.toFloat()
                            val x = offset.x / w
                            val y = offset.y / h
                            val surface = if (centerEnabled && x in 0.34f..0.66f && y in 0.34f..0.66f) {
                                Surface.OCCLUSAL
                            } else {
                                val dx = x - 0.5f
                                val dy = y - 0.5f
                                if (kotlin.math.abs(dx) > kotlin.math.abs(dy)) {
                                    if (dx < 0) Surface.MESIAL else Surface.DISTAL
                                } else {
                                    if (dy < 0) Surface.VESTIBULAR else Surface.LINGUAL_PALATAL
                                }
                            }
                            onSurfaceTap(surface)
                        }
                    }
            ) {
                val w = size.width
                val h = size.height
                val c = Offset(w / 2f, h / 2f)
                val inset = if (centerEnabled) w * 0.34f else w * 0.5f
                val end = if (centerEnabled) w * 0.66f else w * 0.5f

                drawRect(background)

                val top = Path().apply {
                    moveTo(0f, 0f); lineTo(w, 0f)
                    lineTo(end, inset); lineTo(inset, inset); close()
                }
                val bottom = Path().apply {
                    moveTo(0f, h); lineTo(w, h)
                    lineTo(end, end); lineTo(inset, end); close()
                }
                val left = Path().apply {
                    moveTo(0f, 0f); lineTo(inset, inset)
                    lineTo(inset, end); lineTo(0f, h); close()
                }
                val right = Path().apply {
                    moveTo(w, 0f); lineTo(w, h)
                    lineTo(end, end); lineTo(end, inset); close()
                }

                drawPath(top, surfaceColor(Surface.VESTIBULAR))
                drawPath(bottom, surfaceColor(Surface.LINGUAL_PALATAL))
                drawPath(left, surfaceColor(Surface.MESIAL))
                drawPath(right, surfaceColor(Surface.DISTAL))

                if (centerEnabled) {
                    drawRect(
                        color = surfaceColor(Surface.OCCLUSAL),
                        topLeft = Offset(inset, inset),
                        size = Size(end - inset, end - inset)
                    )
                }

                drawPath(top, border, style = Stroke(2f))
                drawPath(bottom, border, style = Stroke(2f))
                drawPath(left, border, style = Stroke(2f))
                drawPath(right, border, style = Stroke(2f))
                if (centerEnabled) {
                    drawRect(border, topLeft = Offset(inset, inset), size = Size(end - inset, end - inset), style = Stroke(2f))
                } else {
                    drawLine(border, Offset(0f, 0f), c, strokeWidth = 2f)
                    drawLine(border, Offset(w, 0f), c, strokeWidth = 2f)
                    drawLine(border, Offset(0f, h), c, strokeWidth = 2f)
                    drawLine(border, Offset(w, h), c, strokeWidth = 2f)
                }
            }
            Spacer(Modifier.width(8.dp))
            Text("D", fontWeight = FontWeight.Bold)
        }
        Text("L / P", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        Text(
            if (centerEnabled) "V = vestibular · L/P = lingual/palatina · M = mesial · D = distal · centro = oclusal"
            else "V = vestibular · L/P = lingual/palatina · M = mesial · D = distal",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
