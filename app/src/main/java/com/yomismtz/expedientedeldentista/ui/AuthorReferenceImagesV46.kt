package com.yomismtz.expedientedeldentista.ui

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import java.util.zip.ZipInputStream

data class AuthorReferenceV46(
    val entry: String,
    val title: String,
    val source: String
)

private fun loadAuthorReferenceV46(context: Context, entryName: String): ImageBitmap? {
    return runCatching {
        context.resources.openRawResource(R.raw.author_reference_images).use { input ->
            ZipInputStream(input).use { zip ->
                var entry = zip.nextEntry
                while (entry != null) {
                    if (!entry.isDirectory && entry.name == entryName) {
                        val bytes = zip.readBytes()
                        return@runCatching BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                    }
                    zip.closeEntry()
                    entry = zip.nextEntry
                }
            }
        }
        null
    }.getOrNull()
}

@Composable
fun AuthorReferenceImageV46(
    reference: AuthorReferenceV46,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val image = remember(reference.entry) { loadAuthorReferenceV46(context, reference.entry) }
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(reference.title, fontWeight = FontWeight.Black)
            if (image != null) {
                Image(
                    bitmap = image,
                    contentDescription = reference.title,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 180.dp, max = 520.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text("No fue posible cargar esta referencia visual.", color = MaterialTheme.colorScheme.error)
            }
            Text(reference.source, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            Text("La referencia se muestra completa, sin recorte; sirve como apoyo educativo y no sustituye la exploración clínica.", style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun atmReferenceV46(kind: String): AuthorReferenceV46 = when (kind) {
    "Lateridad" -> AuthorReferenceV46("atm_lateralidad.webp", "Lateridad mandibular · referencia visual", "Material proporcionado por la autora · 3. Llenado de expediente.pdf · pág. 58")
    "Protrusión" -> AuthorReferenceV46("atm_protrusion.webp", "Protrusión mandibular · referencia visual", "Material proporcionado por la autora · 3. Llenado de expediente.pdf · pág. 60")
    "Apertura máxima" -> AuthorReferenceV46("atm_apertura_maxima.webp", "Apertura máxima · referencia visual", "Material proporcionado por la autora · 3. Llenado de expediente.pdf · pág. 61")
    else -> AuthorReferenceV46("atm_apertura.webp", "Exploración y apertura de ATM · referencia visual", "Material proporcionado por la autora · 3. Llenado de expediente.pdf · pág. 57")
}

fun occlusionReferenceV46(topic: String): AuthorReferenceV46 = when (topic) {
    "Líneas medias" -> AuthorReferenceV46("oclusion_lineas_medias.webp", "Líneas medias y relaciones anteriores · referencia clínica", "Material proporcionado por la autora · 3. Llenado de expediente.pdf · pág. 68")
    "Overjet", "Overbite", "Mordida abierta", "Mordida profunda" -> AuthorReferenceV46("oclusion_overjet_overbite.webp", "Overjet y overbite · guía clínica", "Material proporcionado por la autora · 3. Llenado de expediente.pdf · pág. 69")
    "Mordida cruzada", "Espacios y alineación" -> AuthorReferenceV46("erupcion_posicion.webp", "Mordidas, espacios y posición dentaria · referencia clínica", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 82")
    else -> AuthorReferenceV46("oclusion_plano_angle.webp", "Plano terminal, Angle y relación canina · referencia clínica", "Material proporcionado por la autora · 3. Llenado de expediente.pdf · pág. 67")
}

fun dentalAnomalyReferenceV46(name: String): AuthorReferenceV46 = when {
    name.contains("Agenesia", true) -> AuthorReferenceV46("anomalias_numero.webp", "Agenesia/anodoncia · referencia radiográfica y clínica", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 83")
    name.contains("Supernumer", true) || name.contains("Microdon", true) || name.contains("Macrodon", true) -> AuthorReferenceV46("anomalias_tamano.webp", "Número y tamaño dentario · referencias clínicas", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 84")
    name.contains("Gemin", true) || name.contains("Fusión", true) || name.contains("invagin", true) -> AuthorReferenceV46("anomalias_forma.webp", "Anomalías de forma · referencias clínicas/radiográficas", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 85")
    name.contains("evagin", true) || name.contains("Taurod", true) -> AuthorReferenceV46("anomalias_raiz.webp", "Anomalías radiculares y morfológicas · referencias", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 86")
    else -> AuthorReferenceV46("anomalias_esmalte.webp", "Alteraciones del esmalte · referencias clínicas", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 87")
}

fun eruptionReferenceV46(name: String): AuthorReferenceV46 = when {
    name.contains("ectóp", true) || name.contains("Natal", true) || name.contains("precoz", true) -> AuthorReferenceV46("erupcion_ectopica.webp", "Erupción y erupción ectópica · referencia clínica", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 78")
    else -> AuthorReferenceV46("erupcion_posicion.webp", "Erupción, posición y alteraciones oclusales · referencia del apartado", "Material proporcionado por la autora · expediente clinico(1).pdf · págs. 79–82")
}

fun mucosaReferenceV46(name: String): AuthorReferenceV46 = when {
    name.contains("Candid", true) || name.contains("pseudomembr", true) -> AuthorReferenceV46("mucosa_candidiasis.webp", "Candidiasis oral · referencias clínicas", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 95")
    name.contains("Queilitis", true) || name.contains("prótesis", true) || name.contains("romboidal", true) -> AuthorReferenceV46("mucosa_candidiasis_2.webp", "Alteraciones asociadas · referencias clínicas", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 96")
    name.contains("geográfica", true) || name.contains("fumador", true) || name.contains("Melanosis", true) || name.contains("Frenillo", true) -> AuthorReferenceV46("mucosa_fumador_geografica.webp", "Lesiones y variantes de mucosa · referencias clínicas", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 94")
    name.contains("Torus", true) || name.contains("varicos", true) || name.contains("papila", true) -> AuthorReferenceV46("mucosa_variantes_2.webp", "Variantes anatómicas orales · referencias clínicas", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 93")
    else -> AuthorReferenceV46("mucosa_variantes_1.webp", "Variantes y alteraciones de mucosa · referencias clínicas", "Material proporcionado por la autora · expediente clinico(1).pdf · pág. 92")
}
