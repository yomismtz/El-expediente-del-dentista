package com.yomismtz.expedientedeldentista.ui

import com.yomismtz.expedientedeldentista.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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

private data class VisualPosture(
    val nameEs: String,
    val nameEn: String,
    val descriptionEs: String,
    val descriptionEn: String,
    @androidx.annotation.DrawableRes val drawable: Int
)

@Composable
fun PostureVisualScreen(lang: String, onBack: () -> Unit) {
    var selected by rememberRecordState("posture.visual.selected",0)
    val list = listOf(
        VisualPosture("Natural / equilibrada", "Natural / balanced", "Cabeza erguida con relación visual equilibrada respecto al cuello. Se usa como referencia descriptiva.", "Upright head with a visually balanced relation to the neck. Used as a descriptive reference.", R.drawable.allimg_083_postura_cervical_equilibrada),
        VisualPosture("Cabeza adelantada", "Forward head posture", "El cráneo se observa desplazado hacia anterior respecto al tronco; describe la postura y busca compensaciones cervicales.", "The head appears translated anteriorly relative to the trunk; describe the posture and look for cervical compensation.", R.drawable.allimg_077_postura_cervical_cabeza_adelaantada),
        VisualPosture("Flexión", "Flexion", "El mentón se orienta hacia abajo por rotación de la cabeza. Distingue una postura sostenida de una inclinación accidental de la fotografía.", "The chin rotates downward. Distinguish sustained posture from accidental photo positioning.", R.drawable.allimg_080_postura_cervical_cabeza_flexion),
        VisualPosture("Extensión", "Extension", "El mentón se eleva y la cabeza rota hacia atrás. Describe el hallazgo sin asumir una causa.", "The chin elevates and the head rotates backward. Describe the finding without assuming a cause.", R.drawable.allimg_079_postura_cervical_cabeza_extension),
        VisualPosture("Rectificación cervical", "Cervical straightening", "La curvatura cervical se aprecia disminuida. Requiere correlación con exploración y estudios adecuados.", "The cervical curve appears reduced. Correlate with examination and appropriate studies.", R.drawable.allimg_082_postura_cervical_cabeza_rectificacion),
        VisualPosture("Lordosis cervical aumentada", "Increased cervical lordosis", "La curvatura cervical se observa más pronunciada. Es una descripción postural, no un diagnóstico etiológico aislado.", "The cervical curve appears more pronounced. It is a postural description, not an isolated etiologic diagnosis.", R.drawable.allimg_081_postura_cervical_cabeza_lordosis_cervical_aum)
    )
    val safeSelected=selected.coerceIn(0,list.lastIndex)
    val p = list[safeSelected]
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Postura craneocervical · guía visual", "Craniocervical posture · visual guide"),
                onBack,
                tr(lang, "Selecciona el hallazgo observado. La selección queda asociada al expediente activo y muestra su fotografía correspondiente.", "Select the observed finding. The selection is stored with the active record and shows its corresponding photograph.")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
                list.take(3).forEachIndexed { i, v ->
                    FilterChip(safeSelected == i, { selected = i }, { Text(if (lang == "en") v.nameEn else v.nameEs) }, modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
                list.drop(3).forEachIndexed { i, v ->
                    val n = i + 3
                    FilterChip(safeSelected == n, { selected = n }, { Text(if (lang == "en") v.nameEn else v.nameEs) }, modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (lang == "en") p.nameEn else p.nameEs, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Image(painterResource(p.drawable), if (lang == "en") p.nameEn else p.nameEs, Modifier.fillMaxWidth().heightIn(min=220.dp,max=420.dp), contentScale=ContentScale.Fit)
                    Text(if (lang == "en") p.descriptionEn else p.descriptionEs)
                }
            }
        }
        item {
            SectionCard(tr(lang, "Perfil facial", "Facial profile")) {
                Text("• ${tr(lang, "Recto: frente, labios y mentón se observan relativamente equilibrados.", "Straight: forehead, lips and chin appear relatively balanced.")}")
                Text("• ${tr(lang, "Convexo: el mentón se aprecia relativamente retruido.", "Convex: the chin appears relatively retruded.")}")
                Text("• ${tr(lang, "Cóncavo: el mentón se aprecia relativamente prominente/adelantado.", "Concave: the chin appears relatively prominent/forward.")}")
            }
        }
        item {
            NoticeCard(tr(lang, "La selección registrada describe postura craneocervical observada y no establece una etiología. La fotografía es una referencia visual educativa y no sustituye análisis cefalométrico, valoración funcional ni diagnóstico cervical.", "The stored selection describes observed craniocervical posture and does not establish an etiology. The photograph is an educational visual reference and does not replace cephalometric, functional or cervical diagnosis."))
        }
    }
}

