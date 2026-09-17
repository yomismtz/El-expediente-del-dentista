package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R

@Composable
fun HeadNeckTeachingV44Screen(lang: String, onBack: () -> Unit) {
    var full by remember { mutableStateOf(false) }
    if (full) {
        HeadNeckTeachingV40Screen(lang) { full = false }
        return
    }

    ResponsiveScreenV17(
        tr(lang, "Exploración de cabeza y cuello", "Head and neck examination"),
        tr(lang, "Referencias visuales para forma del cráneo, volumen/perímetro cefálico e implantación del cabello.", "Visual references for cranial shape, head circumference and hairline."),
        onBack
    ) { _ ->
        ResponsiveSectionV17(tr(lang, "Forma del cráneo", "Cranial shape")) {
            Image(
                painter = painterResource(R.drawable.cranial_shape_reference),
                contentDescription = tr(lang, "Referencia visual de formas craneales proporcionada por la autora", "Visual cranial-shape reference provided by the author"),
                modifier = Modifier.fillMaxWidth().height(310.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                tr(lang, "Imagen proporcionada por la autora. Se conserva con el mismo contenido visual, sin recorte ni rediseño.", "Image provided by the author. Its visual content is preserved without cropping or redesign."),
                style = MaterialTheme.typography.bodySmall
            )
            NoticeCard(tr(lang,
                "La forma craneal se describe por inspección desde distintos planos. La apariencia aislada no permite atribuir un síndrome o enfermedad; cualquier asimetría o deformidad debe correlacionarse con edad, crecimiento, antecedentes y exploración.",
                "Cranial shape is described by inspection from different planes. Appearance alone does not establish a syndrome or disease; asymmetry or deformity must be correlated with age, growth, history and examination."))
        }

        ResponsiveSectionV17(tr(lang, "Volumen del cráneo · perímetro cefálico", "Cranial volume · head circumference")) {
            Text(tr(lang,
                "Para determinar si un paciente pediátrico presenta microcefalia, macrocefalia o un perímetro cefálico dentro del rango esperado se utiliza principalmente el perímetro cefálico (PC), interpretado con tablas apropiadas para edad y sexo.",
                "To assess whether a pediatric patient has microcephaly, macrocephaly or a head circumference within the expected range, head circumference is measured and interpreted using age- and sex-appropriate charts."),
                fontWeight = FontWeight.SemiBold)
            Image(
                painter = painterResource(R.drawable.head_circumference_reference),
                contentDescription = tr(lang, "Fotografía de medición del perímetro cefálico", "Photograph of head circumference measurement"),
                modifier = Modifier.fillMaxWidth().height(245.dp),
                contentScale = ContentScale.Fit
            )
            Text("Imagen: U.S. Air Force / Airman 1st Class Anania Tekurio, vía Wikimedia Commons, dominio público · File:Head diameter measurement.jpg.", style = MaterialTheme.typography.bodySmall)
            Text("Cómo medir el perímetro cefálico", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            Text("1. Utiliza una cinta métrica flexible y no extensible.")
            Text("2. Rodea la cabeza pasando por la parte más prominente de la frente (glabela/región frontal) y la parte más prominente del occipital.")
            Text("3. Mantén la cinta horizontal, ajustada y en contacto con la cabeza, pero sin comprimir.")
            Text("4. Registra el resultado en centímetros; en pediatría compáralo con curvas de crecimiento apropiadas para edad y sexo.")
            NoticeCard(tr(lang,
                "El perímetro cefálico se usa sobre todo en lactantes y niños para vigilar crecimiento craneal. No deben aplicarse sin más curvas pediátricas a adultos para etiquetar microcefalia o macrocefalia.",
                "Head circumference is mainly used in infants and children to monitor cranial growth. Pediatric charts should not simply be applied to adults to label microcephaly or macrocephaly."))
            Text("Fuentes clínicas: CDC · Measuring Head Circumference; CDC/NHANES · Anthropometry Procedures Manual.", style = MaterialTheme.typography.bodySmall)
        }

        ResponsiveSectionV17(tr(lang, "Implantación del cabello", "Hairline")) {
            Image(
                painter = painterResource(R.drawable.hairline_reference),
                contentDescription = tr(lang, "Fotografía real de línea de implantación del cabello", "Real photograph of a hairline"),
                modifier = Modifier.fillMaxWidth().height(230.dp),
                contentScale = ContentScale.Fit
            )
            Text("Referencia fotográfica: Wikimedia Commons · File:Hairline.jpg · Acr319 · dominio público.", style = MaterialTheme.typography.bodySmall)
            val items = listOf(
                "Implantación habitual" to "Describir altura, simetría y contorno sin asignar significado patológico por sí solos.",
                "Implantación baja" to "La línea de cabello se observa relativamente cercana a la frente inferior; interpretar con proporciones faciales y antecedentes.",
                "Implantación alta" to "La línea frontal se encuentra relativamente superior; diferenciar variante anatómica de recesión adquirida.",
                "Recesión frontotemporal" to "Retroceso de la línea en región frontal/temporal; preguntar evolución y antecedentes familiares/dermatológicos.",
                "Pico frontal / widow's peak" to "Pico central de la línea frontal; puede ser una variante anatómica normal.",
                "Implantación irregular" to "Asimetría, áreas de menor densidad o pérdida localizada; documentar distribución y evolución."
            )
            items.forEach { (title, detail) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(title, fontWeight = FontWeight.Black)
                        Text(detail, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Card(
            onClick = { full = true },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(tr(lang, "Abrir exploración completa de cabeza, cuello y ganglios", "Open complete head, neck and lymph-node examination"), fontWeight = FontWeight.Black)
                Text(tr(lang, "Continúa con edad aparente, marcha, facies, postura, cráneo, cara, músculos masticatorios, cuello y cadenas ganglionares.", "Continue with apparent age, gait, facies, posture, skull, face, masticatory muscles, neck and lymph-node chains."))
            }
        }
    }
}
