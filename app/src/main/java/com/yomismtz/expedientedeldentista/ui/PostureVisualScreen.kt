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
    var selected by rememberRecordState("posture.visual.selected",-1)
    val list = listOf(
        VisualPosture("Natural / equilibrada", "Natural / balanced", "Cabeza erguida con relación visual equilibrada respecto al cuello. Se usa como referencia descriptiva.", "Upright head with a visually balanced relation to the neck. Used as a descriptive reference.", R.drawable.allimg_083_postura_cervical_equilibrada),
        VisualPosture("Cabeza adelantada", "Forward head posture", "El cráneo se observa desplazado hacia anterior respecto al tronco; describe la postura y busca compensaciones cervicales.", "The head appears translated anteriorly relative to the trunk; describe the posture and look for cervical compensation.", R.drawable.allimg_077_postura_cervical_cabeza_adelaantada),
        VisualPosture("Flexión", "Flexion", "El mentón se orienta hacia abajo por rotación de la cabeza. Distingue una postura sostenida de una inclinación accidental de la fotografía.", "The chin rotates downward. Distinguish sustained posture from accidental photo positioning.", R.drawable.allimg_080_postura_cervical_cabeza_flexion),
        VisualPosture("Extensión", "Extension", "El mentón se eleva y la cabeza rota hacia atrás. Describe el hallazgo sin asumir una causa.", "The chin elevates and the head rotates backward. Describe the finding without assuming a cause.", R.drawable.allimg_079_postura_cervical_cabeza_extension),
        VisualPosture("Rectificación cervical", "Cervical straightening", "La curvatura cervical se aprecia disminuida. Requiere correlación con exploración y estudios adecuados.", "The cervical curve appears reduced. Correlate with examination and appropriate studies.", R.drawable.allimg_082_postura_cervical_cabeza_rectificacion),
        VisualPosture("Lordosis cervical aumentada", "Increased cervical lordosis", "La curvatura cervical se observa más pronunciada. Es una descripción postural, no un diagnóstico etiológico aislado.", "The cervical curve appears more pronounced. It is a postural description, not an isolated etiologic diagnosis.", R.drawable.allimg_081_postura_cervical_cabeza_lordosis_cervical_aum),
        VisualPosture("Cifosis", "Kyphotic posture", "Imagen local de postura cervical asociada a cifosis. Describe el hallazgo observado y correlaciónalo con la exploración clínica.", "Local image of cervical posture associated with kyphosis. Describe the observed finding and correlate it with the clinical examination.", R.drawable.allimg_078_postura_cervical_cabeza_cifosis)
    )
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Postura craneocervical", "Craniocervical posture"),
                onBack,
                tr(lang, "Selecciona el hallazgo observado; al tocar el recuadro aparece únicamente su imagen correspondiente.", "Select the observed finding; tapping its card shows only the corresponding image.")
            )
        }
        item {
            SectionCard(tr(lang,"Postura observada","Observed posture")){
                ChipChoices(list.mapIndexed{i,v->(if(lang=="en")v.nameEn else v.nameEs) to (selected==i)},{selected=it},columns=3)
            }
        }
        val p=list.getOrNull(selected)
        if(p!=null){
            item { LocalClinicalInlineZoomImageV48(lang,p.nameEs,p.nameEn,p.drawable,p.descriptionEs,p.descriptionEn) }
        }
        item {
            NoticeCard(tr(lang, "La selección describe el hallazgo observado y no establece una etiología. Interpreta las imágenes junto con la exploración clínica y los estudios indicados.", "The selection describes the observed finding and does not establish an etiology. Interpret the images together with the clinical examination and indicated studies."))
        }
    }
}
