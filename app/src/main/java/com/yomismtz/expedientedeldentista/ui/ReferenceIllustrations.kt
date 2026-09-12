package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AsaReferenceIllustration(lang: String, modifier: Modifier = Modifier) {
    val colors = listOf(
        Color(0xFF7CB342), Color(0xFF9CCC65), Color(0xFFFFCA28),
        Color(0xFFFFA726), Color(0xFFEF6C00), Color(0xFFD84315)
    )
    Column(modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(190.dp)) {
            val left = size.width * 0.08f
            val right = size.width * 0.92f
            val rowH = size.height / 6f
            for (i in 0 until 6) {
                val y = i * rowH
                val inset = i * size.width * 0.035f
                drawRoundRect(
                    color = colors[i],
                    topLeft = Offset(left + inset, y + 4f),
                    size = Size((right - left) - inset * 2f, rowH - 8f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
                )
            }
        }
        Text(
            tr(lang,
                "Referencia visual: ASA I → paciente sano; ASA II → enfermedad sistémica leve; ASA III → enfermedad sistémica grave; ASA IV → enfermedad grave con amenaza constante para la vida; ASA V → paciente moribundo; ASA VI → donador con muerte cerebral. “E” indica emergencia.",
                "Visual reference: ASA I → healthy patient; ASA II → mild systemic disease; ASA III → severe systemic disease; ASA IV → severe disease posing a constant threat to life; ASA V → moribund patient; ASA VI → brain-dead organ donor. “E” denotes emergency."),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AtmReferenceIllustration(lang: String, modifier: Modifier = Modifier) {
    val outline = MaterialTheme.colorScheme.outline
    val accent = MaterialTheme.colorScheme.primary
    val muscle = MaterialTheme.colorScheme.tertiary
    Column(modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(220.dp)) {
            val cx = size.width * 0.5f
            val cy = size.height * 0.42f
            // Cráneo lateral esquemático
            drawCircle(Color(0xFFF3E6D3), radius = size.height * 0.26f, center = Offset(cx, cy))
            drawCircle(outline, radius = size.height * 0.26f, center = Offset(cx, cy), style = Stroke(3f))
            // Mandíbula
            val mandible = Path().apply {
                moveTo(cx - size.width * 0.16f, cy + size.height * 0.06f)
                quadraticBezierTo(cx - size.width * 0.14f, cy + size.height * 0.32f, cx, cy + size.height * 0.34f)
                quadraticBezierTo(cx + size.width * 0.18f, cy + size.height * 0.30f, cx + size.width * 0.20f, cy + size.height * 0.08f)
            }
            drawPath(mandible, outline, style = Stroke(10f))
            // ATM y músculos
            drawCircle(accent, radius = 12f, center = Offset(cx + size.width * 0.18f, cy + size.height * 0.02f))
            drawOval(muscle.copy(alpha = 0.55f), topLeft = Offset(cx + size.width * 0.06f, cy - size.height * 0.18f), size = Size(size.width * 0.12f, size.height * 0.20f))
            drawOval(muscle.copy(alpha = 0.55f), topLeft = Offset(cx + size.width * 0.10f, cy + size.height * 0.08f), size = Size(size.width * 0.10f, size.height * 0.19f))
        }
        Text(
            tr(lang,
                "Punto azul = región de ATM. Zonas resaltadas = referencia de músculos temporales/maseterinos. En la exploración se describen apertura, simetría, dolor, ruidos, bloqueo y sensibilidad muscular.",
                "Blue point = TMJ region. Highlighted areas = temporalis/masseter reference. Examination describes opening, symmetry, pain, sounds, locking and muscle tenderness."),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun OcclusionReferenceIllustration(lang: String, modifier: Modifier = Modifier) {
    val outline = MaterialTheme.colorScheme.outline
    val upper = MaterialTheme.colorScheme.primaryContainer
    val lower = MaterialTheme.colorScheme.secondaryContainer
    Column(modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(210.dp)) {
            val mid = size.width / 2f
            val topY = size.height * 0.34f
            val bottomY = size.height * 0.58f
            // incisivos simplificados
            repeat(4) { i ->
                val x = mid - 92f + i * 60f
                drawRoundRect(upper, Offset(x, topY), Size(48f, 62f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f))
                drawRoundRect(outline, Offset(x, topY), Size(48f, 62f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f), style = Stroke(2f))
                drawRoundRect(lower, Offset(x + 6f, bottomY), Size(42f, 58f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f))
                drawRoundRect(outline, Offset(x + 6f, bottomY), Size(42f, 58f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f), style = Stroke(2f))
            }
            // líneas de referencia de overjet/overbite
            drawLine(MaterialTheme.colorScheme.primary, Offset(mid - 25f, topY + 62f), Offset(mid - 25f, bottomY), strokeWidth = 4f)
            drawLine(MaterialTheme.colorScheme.tertiary, Offset(mid + 65f, bottomY + 10f), Offset(mid + 105f, bottomY + 10f), strokeWidth = 4f)
        }
        Text(
            tr(lang,
                "Referencia: registrar dentición, plano terminal, Angle, relación canina, línea media, overjet, overbite, mordida borde a borde, abierta o cruzada, apiñamiento y diastemas.",
                "Reference: record dentition, terminal plane, Angle class, canine relation, midline, overjet, overbite, edge-to-edge, open/crossbite, crowding and diastemas."),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun OralMucosaReferenceIllustration(lang: String, modifier: Modifier = Modifier) {
    val outline = MaterialTheme.colorScheme.outline
    val mucosa = Color(0xFFF3A6A6)
    val tongue = Color(0xFFE77B7B)
    Column(modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(230.dp)) {
            val left = size.width * 0.18f
            val top = size.height * 0.16f
            val w = size.width * 0.64f
            val h = size.height * 0.66f
            drawOval(mucosa, Offset(left, top), Size(w, h))
            drawOval(outline, Offset(left, top), Size(w, h), style = Stroke(3f))
            drawOval(Color.White, Offset(left + w * 0.14f, top + h * 0.18f), Size(w * 0.72f, h * 0.50f))
            drawOval(tongue, Offset(left + w * 0.25f, top + h * 0.40f), Size(w * 0.50f, h * 0.34f))
            drawLine(outline, Offset(left + w * 0.5f, top + h * 0.40f), Offset(left + w * 0.5f, top + h * 0.67f), strokeWidth = 2f)
            // puntos de referencia
            listOf(
                Offset(left + w * 0.50f, top + h * 0.08f),
                Offset(left + w * 0.12f, top + h * 0.42f),
                Offset(left + w * 0.88f, top + h * 0.42f),
                Offset(left + w * 0.50f, top + h * 0.74f)
            ).forEach { drawCircle(MaterialTheme.colorScheme.primary, 8f, it) }
        }
        Text(
            tr(lang,
                "Explora de forma sistemática labios, carrillos, encía, paladar duro y blando, orofaringe, lengua y piso de boca. Describe color, forma, volumen, consistencia, integridad, superficie y función.",
                "Systematically inspect lips, cheeks, gingiva, hard/soft palate, oropharynx, tongue and floor of mouth. Describe color, shape, volume, consistency, integrity, surface and function."),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun DiagnosticAidsIllustration(lang: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(190.dp)) {
            val primary = MaterialTheme.colorScheme.primary
            val secondary = MaterialTheme.colorScheme.secondary
            val outline = MaterialTheme.colorScheme.outline
            // radiografía
            drawRoundRect(Color(0xFF4A4A4A), Offset(size.width * 0.08f, size.height * 0.18f), Size(size.width * 0.24f, size.height * 0.48f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f,14f))
            drawCircle(Color(0xFFDDDDDD), 24f, Offset(size.width * 0.20f, size.height * 0.39f))
            // modelos
            drawRoundRect(primary.copy(alpha=.25f), Offset(size.width * 0.39f, size.height * 0.20f), Size(size.width * 0.22f, size.height * 0.45f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f,18f))
            drawArc(primary, 200f, 140f, false, Offset(size.width * 0.415f, size.height * 0.29f), Size(size.width * 0.17f, size.height * 0.20f), style = Stroke(8f))
            // cefalometría / laboratorio
            drawRoundRect(secondary.copy(alpha=.25f), Offset(size.width * 0.68f, size.height * 0.18f), Size(size.width * 0.24f, size.height * 0.48f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f,14f))
            drawLine(outline, Offset(size.width * 0.72f, size.height * 0.55f), Offset(size.width * 0.86f, size.height * 0.30f), strokeWidth = 4f)
            drawCircle(secondary, 7f, Offset(size.width * 0.72f, size.height * 0.55f))
            drawCircle(secondary, 7f, Offset(size.width * 0.86f, size.height * 0.30f))
        }
        Text(
            tr(lang,
                "Los auxiliares pueden incluir análisis clínicos, histopatología, microbiología, modelos de estudio, análisis radiográfico/cefalométrico y otros estudios indicados.",
                "Diagnostic aids may include laboratory tests, histopathology, microbiology, study models, radiographic/cephalometric analysis and other indicated studies."),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun IcdasReferenceIllustration(code: Int, lang: String, modifier: Modifier = Modifier) {
    val lesion = when (code) {
        0 -> Color.Transparent
        1 -> Color(0xFFF2D36B)
        2 -> Color(0xFFE8B94F)
        3 -> Color(0xFFD58B39)
        4 -> Color(0xFF9A633F)
        5 -> Color(0xFF7B4A35)
        else -> Color(0xFF5C3328)
    }
    Column(modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(180.dp)) {
            val x = size.width * 0.34f
            val y = size.height * 0.08f
            val w = size.width * 0.32f
            val h = size.height * 0.78f
            val crown = Path().apply {
                moveTo(x + w * .12f, y)
                quadraticBezierTo(x, y + h * .20f, x + w * .12f, y + h * .45f)
                lineTo(x + w * .28f, y + h)
                lineTo(x + w * .46f, y + h * .48f)
                lineTo(x + w * .54f, y + h * .48f)
                lineTo(x + w * .72f, y + h)
                lineTo(x + w * .88f, y + h * .45f)
                quadraticBezierTo(x + w, y + h * .20f, x + w * .88f, y)
                close()
            }
            drawPath(crown, Color(0xFFF3EEE5))
            drawPath(crown, MaterialTheme.colorScheme.outline, style = Stroke(3f))
            if (code > 0) {
                val depth = when (code) { 1 -> .10f; 2 -> .16f; 3 -> .24f; 4 -> .34f; 5 -> .48f; else -> .62f }
                drawOval(lesion, Offset(x + w * .34f, y + h * .03f), Size(w * .32f, h * depth))
            }
        }
        Text(
            tr(lang, "Esquema ilustrativo del código ICDAS $code. No sustituye la inspección clínica, limpieza, secado e iluminación adecuados.",
                "Illustrative ICDAS code $code schematic. It does not replace proper cleaning, drying, illumination and clinical inspection."),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun PeriodontalSitesIllustration(lang: String, modifier: Modifier = Modifier) {
    val outline = MaterialTheme.colorScheme.outline
    val point = MaterialTheme.colorScheme.primary
    Column(modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(220.dp)) {
            val cx = size.width / 2f
            val crownY = size.height * .16f
            val crown = Path().apply {
                moveTo(cx - 70f, crownY)
                quadraticBezierTo(cx - 90f, size.height*.34f, cx - 55f, size.height*.47f)
                lineTo(cx - 25f, size.height*.86f)
                lineTo(cx, size.height*.50f)
                lineTo(cx + 25f, size.height*.86f)
                lineTo(cx + 55f, size.height*.47f)
                quadraticBezierTo(cx + 90f, size.height*.34f, cx + 70f, crownY)
                close()
            }
            drawPath(crown, Color(0xFFF3EEE5))
            drawPath(crown, outline, style = Stroke(3f))
            val y = size.height*.39f
            listOf(cx-65f, cx, cx+65f).forEach { x -> drawCircle(point, 9f, Offset(x,y)) }
            val y2 = size.height*.50f
            listOf(cx-55f, cx, cx+55f).forEach { x -> drawCircle(point.copy(alpha=.55f), 9f, Offset(x,y2)) }
        }
        Text(
            tr(lang,
                "Seis sitios por diente: mesiovestibular, vestibular, distovestibular, mesiolingual/palatino, lingual/palatino y distolingual/palatino.",
                "Six sites per tooth: mesiobuccal, buccal, distobuccal, mesiolingual/palatal, lingual/palatal and distolingual/palatal."),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
