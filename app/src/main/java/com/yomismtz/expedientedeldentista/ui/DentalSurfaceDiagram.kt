package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
                modifier = Modifier.width(62.dp).clickable { onSelected(tooth) },
                colors = CardDefaults.cardColors(
                    containerColor = if (tooth == selected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(if (isMarked(tooth)) "🦷•" else "🦷", style = MaterialTheme.typography.titleLarge)
                    Text(tooth.toString(), fontWeight = if (tooth == selected) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
    }
}

/**
 * Esquema didáctico de un órgano dentario visto desde la corona.
 * Las superficies se representan en la corona y se dibujan raíces por debajo para que
 * visualmente no parezca una tabla de cuadrados sino un diente dentro del odontograma.
 */
@Composable
fun DentalSurfaceDiagram(
    centerEnabled: Boolean,
    surfaceColor: (Surface) -> Color,
    onSurfaceTap: (Surface) -> Unit,
    modifier: Modifier = Modifier
) {
    val border = MaterialTheme.colorScheme.outline
    val crownBackground = MaterialTheme.colorScheme.surfaceVariant
    val rootColor = MaterialTheme.colorScheme.surface

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("V", fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("M", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Canvas(
                modifier = Modifier
                    .width(200.dp)
                    .height(250.dp)
                    .pointerInput(centerEnabled) {
                        detectTapGestures { offset ->
                            val w = size.width.toFloat()
                            val crownH = w
                            if (offset.y > crownH) return@detectTapGestures
                            val x = offset.x / w
                            val y = offset.y / crownH
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
                val crownH = w
                val inset = if (centerEnabled) w * 0.34f else w * 0.5f
                val end = if (centerEnabled) w * 0.66f else w * 0.5f

                // Silueta externa de corona: bordes redondeados y zona cervical ligeramente angosta.
                val crown = Path().apply {
                    moveTo(w * 0.18f, crownH * 0.05f)
                    cubicTo(w * 0.06f, crownH * 0.18f, w * 0.07f, crownH * 0.72f, w * 0.22f, crownH * 0.92f)
                    quadraticBezierTo(w * 0.5f, crownH, w * 0.78f, crownH * 0.92f)
                    cubicTo(w * 0.93f, crownH * 0.72f, w * 0.94f, crownH * 0.18f, w * 0.82f, crownH * 0.05f)
                    quadraticBezierTo(w * 0.5f, -crownH * 0.02f, w * 0.18f, crownH * 0.05f)
                    close()
                }
                drawPath(crown, crownBackground)

                val top = Path().apply {
                    moveTo(w * 0.16f, crownH * 0.06f); lineTo(w * 0.84f, crownH * 0.06f)
                    lineTo(end, inset); lineTo(inset, inset); close()
                }
                val bottom = Path().apply {
                    moveTo(w * 0.22f, crownH * 0.91f); lineTo(w * 0.78f, crownH * 0.91f)
                    lineTo(end, end); lineTo(inset, end); close()
                }
                val left = Path().apply {
                    moveTo(w * 0.16f, crownH * 0.06f); lineTo(inset, inset)
                    lineTo(inset, end); lineTo(w * 0.22f, crownH * 0.91f); close()
                }
                val right = Path().apply {
                    moveTo(w * 0.84f, crownH * 0.06f); lineTo(w * 0.78f, crownH * 0.91f)
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

                drawPath(crown, border, style = Stroke(3f))
                drawPath(top, border, style = Stroke(2f))
                drawPath(bottom, border, style = Stroke(2f))
                drawPath(left, border, style = Stroke(2f))
                drawPath(right, border, style = Stroke(2f))
                if (centerEnabled) {
                    drawRect(border, topLeft = Offset(inset, inset), size = Size(end - inset, end - inset), style = Stroke(2f))
                }

                // Raíces esquemáticas para reforzar visualmente la forma de diente.
                val rootTop = crownH * 0.92f
                val rootBottom = size.height * 0.98f
                val rootLeft = Path().apply {
                    moveTo(w * 0.34f, rootTop)
                    cubicTo(w * 0.32f, size.height * 0.72f, w * 0.25f, size.height * 0.90f, w * 0.34f, rootBottom)
                    quadraticBezierTo(w * 0.43f, size.height * 0.92f, w * 0.48f, rootTop)
                    close()
                }
                val rootRight = Path().apply {
                    moveTo(w * 0.52f, rootTop)
                    quadraticBezierTo(w * 0.57f, size.height * 0.92f, w * 0.66f, rootBottom)
                    cubicTo(w * 0.75f, size.height * 0.90f, w * 0.68f, size.height * 0.72f, w * 0.66f, rootTop)
                    close()
                }
                drawPath(rootLeft, rootColor)
                drawPath(rootRight, rootColor)
                drawPath(rootLeft, border, style = Stroke(3f))
                drawPath(rootRight, border, style = Stroke(3f))
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
