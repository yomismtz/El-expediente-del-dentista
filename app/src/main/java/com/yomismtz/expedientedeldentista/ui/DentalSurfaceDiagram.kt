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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.Surface
import kotlin.math.abs

private fun isMaxillaryToothV19(tooth: Int): Boolean = tooth / 10 in setOf(1, 2, 5, 6)
private fun isMandibularToothV19(tooth: Int): Boolean = tooth / 10 in setOf(3, 4, 7, 8)

@Composable
fun DentalArchSelector(
    teeth: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    isMarked: (Int) -> Boolean = { false }
) {
    val upper = teeth.filter(::isMaxillaryToothV19)
    val lower = teeth.filter(::isMandibularToothV19)

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (upper.isNotEmpty()) {
            Text("↑ ${tr("es", "MAXILAR", "UPPER")}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            DentalToothRowV19(upper, selected, onSelected, isMarked)
        }
        if (lower.isNotEmpty()) {
            Text("↓ ${tr("es", "MANDIBULAR", "LOWER")}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            DentalToothRowV19(lower, selected, onSelected, isMarked)
        }
        if (upper.isEmpty() && lower.isEmpty()) DentalToothRowV19(teeth, selected, onSelected, isMarked)
    }
}

@Composable
private fun DentalToothRowV19(
    teeth: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    isMarked: (Int) -> Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        teeth.forEach { tooth ->
            Card(
                modifier = Modifier.width(58.dp).clickable { onSelected(tooth) },
                colors = CardDefaults.cardColors(
                    containerColor = if (tooth == selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp, horizontal = 3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(if (isMarked(tooth)) "🦷•" else "🦷", style = MaterialTheme.typography.titleMedium)
                    Text(tooth.toString(), fontWeight = if (tooth == selected) FontWeight.Black else FontWeight.Normal)
                }
            }
        }
    }
}

@Composable
fun DentalSurfaceDiagram(
    centerEnabled: Boolean,
    surfaceColor: @Composable (Surface) -> Color,
    onSurfaceTap: (Surface) -> Unit,
    modifier: Modifier = Modifier
) {
    val border = MaterialTheme.colorScheme.outline
    val base = MaterialTheme.colorScheme.surfaceVariant
    val colors = Surface.entries.associateWith { surfaceColor(it) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("V", fontWeight = FontWeight.Black)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("M", fontWeight = FontWeight.Black)
            Spacer(Modifier.width(8.dp))
            Canvas(
                Modifier
                    .width(230.dp)
                    .height(230.dp)
                    .pointerInput(centerEnabled) {
                        detectTapGestures { offset ->
                            val w = size.width.toFloat()
                            val h = size.height.toFloat()
                            val x = offset.x / w
                            val y = offset.y / h
                            val dx = x - .5f
                            val dy = y - .5f
                            val center = centerEnabled && x in .31f.. .69f && y in .31f.. .69f
                            val surface = when {
                                center -> Surface.OCCLUSAL
                                abs(dx) > abs(dy) -> if (dx < 0) Surface.MESIAL else Surface.DISTAL
                                dy < 0 -> Surface.VESTIBULAR
                                else -> Surface.LINGUAL_PALATAL
                            }
                            onSurfaceTap(surface)
                        }
                    }
            ) {
                val w = size.width
                val h = size.height
                val cx = w / 2f
                val cy = h / 2f

                // Contorno oclusal lobulado: corona vista desde arriba, sin raíces.
                val crown = Path().apply {
                    moveTo(cx, h * .055f)
                    cubicTo(w * .38f, h * .035f, w * .27f, h * .07f, w * .21f, h * .15f)
                    cubicTo(w * .12f, h * .18f, w * .075f, h * .30f, w * .095f, h * .40f)
                    cubicTo(w * .055f, h * .50f, w * .075f, h * .63f, w * .14f, h * .70f)
                    cubicTo(w * .14f, h * .82f, w * .25f, h * .91f, w * .36f, h * .90f)
                    cubicTo(w * .43f, h * .965f, w * .57f, h * .965f, w * .64f, h * .90f)
                    cubicTo(w * .75f, h * .91f, w * .86f, h * .82f, w * .86f, h * .70f)
                    cubicTo(w * .925f, h * .63f, w * .945f, h * .50f, w * .905f, h * .40f)
                    cubicTo(w * .925f, h * .30f, w * .88f, h * .18f, w * .79f, h * .15f)
                    cubicTo(w * .73f, h * .07f, w * .62f, h * .035f, cx, h * .055f)
                    close()
                }
                drawPath(crown, base)

                val cL = if (centerEnabled) w * .31f else cx
                val cR = if (centerEnabled) w * .69f else cx
                val cT = if (centerEnabled) h * .31f else cy
                val cB = if (centerEnabled) h * .69f else cy

                val vestibular = Path().apply {
                    moveTo(cx, h * .055f)
                    cubicTo(w * .34f, h * .04f, w * .23f, h * .09f, w * .16f, h * .20f)
                    lineTo(cL, cT); lineTo(cR, cT)
                    lineTo(w * .84f, h * .20f)
                    cubicTo(w * .77f, h * .09f, w * .66f, h * .04f, cx, h * .055f)
                    close()
                }
                val lingual = Path().apply {
                    moveTo(w * .14f, h * .78f); lineTo(cL, cB); lineTo(cR, cB); lineTo(w * .86f, h * .78f)
                    cubicTo(w * .80f, h * .90f, w * .69f, h * .94f, w * .64f, h * .90f)
                    cubicTo(w * .57f, h * .965f, w * .43f, h * .965f, w * .36f, h * .90f)
                    cubicTo(w * .31f, h * .94f, w * .20f, h * .90f, w * .14f, h * .78f)
                    close()
                }
                val mesial = Path().apply {
                    moveTo(w * .16f, h * .20f); lineTo(cL, cT); lineTo(cL, cB); lineTo(w * .14f, h * .78f)
                    cubicTo(w * .07f, h * .70f, w * .06f, h * .59f, w * .095f, h * .50f)
                    cubicTo(w * .06f, h * .40f, w * .08f, h * .28f, w * .16f, h * .20f)
                    close()
                }
                val distal = Path().apply {
                    moveTo(w * .84f, h * .20f); lineTo(cR, cT); lineTo(cR, cB); lineTo(w * .86f, h * .78f)
                    cubicTo(w * .93f, h * .70f, w * .94f, h * .59f, w * .905f, h * .50f)
                    cubicTo(w * .94f, h * .40f, w * .92f, h * .28f, w * .84f, h * .20f)
                    close()
                }

                drawPath(vestibular, colors.getValue(Surface.VESTIBULAR))
                drawPath(lingual, colors.getValue(Surface.LINGUAL_PALATAL))
                drawPath(mesial, colors.getValue(Surface.MESIAL))
                drawPath(distal, colors.getValue(Surface.DISTAL))

                if (centerEnabled) {
                    val occlusal = Path().apply {
                        moveTo(cL, cT); quadraticBezierTo(cx, h * .25f, cR, cT)
                        quadraticBezierTo(w * .75f, cy, cR, cB)
                        quadraticBezierTo(cx, h * .75f, cL, cB)
                        quadraticBezierTo(w * .25f, cy, cL, cT)
                        close()
                    }
                    drawPath(occlusal, colors.getValue(Surface.OCCLUSAL))
                    drawPath(occlusal, border, style = Stroke(2.2f))
                    // Surcos oclusales didácticos para que la vista se reconozca como corona.
                    drawLine(border.copy(alpha=.65f), Offset(cx, cT + 8f), Offset(cx, cB - 8f), strokeWidth = 2f)
                    drawLine(border.copy(alpha=.55f), Offset(cL + 8f, cy), Offset(cR - 8f, cy), strokeWidth = 2f)
                    drawCircle(border.copy(alpha=.55f), radius = 4f, center = Offset(cx, cy))
                } else {
                    // O'Leary: cuatro caras convergen al centro, sin cara oclusal.
                    drawLine(border, Offset(cL, cT), Offset(cx, cy), strokeWidth = 2f)
                    drawLine(border, Offset(cR, cT), Offset(cx, cy), strokeWidth = 2f)
                    drawLine(border, Offset(cL, cB), Offset(cx, cy), strokeWidth = 2f)
                    drawLine(border, Offset(cR, cB), Offset(cx, cy), strokeWidth = 2f)
                }

                drawPath(crown, border, style = Stroke(3f))
                drawPath(vestibular, border, style = Stroke(1.8f))
                drawPath(lingual, border, style = Stroke(1.8f))
                drawPath(mesial, border, style = Stroke(1.8f))
                drawPath(distal, border, style = Stroke(1.8f))
            }
            Spacer(Modifier.width(8.dp))
            Text("D", fontWeight = FontWeight.Black)
        }
        Text("L / P", fontWeight = FontWeight.Black)
        Spacer(Modifier.height(6.dp))
        Text(
            if (centerEnabled) "V = vestibular · L/P = lingual/palatina · M = mesial · D = distal · centro = oclusal"
            else "V = vestibular · L/P = lingual/palatina · M = mesial · D = distal",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
