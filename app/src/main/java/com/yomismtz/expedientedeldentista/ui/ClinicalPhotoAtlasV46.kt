package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R

private data class ClinicalPhotoV46(
    val drawable: Int,
    val title: String,
    val caption: String,
    val credit: String
)

@Composable
private fun ClinicalPhotoCardV46(photo: ClinicalPhotoV46) {
    EducationalVisualFrameV49(
        title = photo.title,
        caption = photo.caption,
        credit = photo.credit
    ) {
        Image(
            painter = painterResource(photo.drawable),
            contentDescription = photo.caption,
            modifier = Modifier.fillMaxWidth().heightIn(min = 180.dp, max = 360.dp),
            contentScale = ContentScale.Fit
        )
    }
}

private data class PhotoOptionV46(
    val group: String,
    val label: String,
    val explanation: String,
    val photo: ClinicalPhotoV46
)

private val occlusionPhotoOptionsV46 = listOf(
    PhotoOptionV46("Dentición / plano terminal", "Dentición temporal y mixta", "Identifica primero segundos molares temporales y etapa de dentición antes de clasificar el plano terminal.", ClinicalPhotoV46(R.drawable.ref_deciduous_teeth, "Dentición temporal", "Fotografía clínica de dentición temporal. Úsala como referencia anatómica, no como patrón único de normalidad.", "Wikimedia Commons · David Shankbone · CC BY-SA 3.0/GFDL")),
    PhotoOptionV46("Relación molar de Angle", "Clase I", "La relación molar debe valorarse bilateralmente en máxima intercuspidación. Una Clase I molar no significa que toda la oclusión sea normal.", ClinicalPhotoV46(R.drawable.ref_angle_i, "Referencia de Clase I", "Fotografía clínica de una relación de Clase I con protrusión bimaxilar; sirve para localizar las referencias molares, no para asumir normalidad global.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Relación molar de Angle", "Clase II", "El molar inferior está relativamente distal respecto al superior; registra lado y, cuando proceda, división incisiva.", ClinicalPhotoV46(R.drawable.ref_angle_ii, "Angle Clase II/1", "Fotografía clínica de maloclusión Angle Clase II/1.", "Wikimedia Commons · Dr. Georg Risse · CC BY-SA 3.0/GFDL")),
    PhotoOptionV46("Relación molar de Angle", "Clase III", "El molar inferior está relativamente mesial respecto al superior; correlaciona con incisivos, caninos y componente funcional/esquelético.", ClinicalPhotoV46(R.drawable.ref_angle_iii, "Angle Clase III", "Fotografía clínica de maloclusión Angle Clase III.", "Wikimedia Commons · Dr. Georg Risse · CC BY-SA 3.0/GFDL")),
    PhotoOptionV46("Overjet", "Resalte horizontal", "Mide en milímetros. La fotografía ayuda a reconocer la dimensión horizontal, pero el valor debe medirse clínicamente.", ClinicalPhotoV46(R.drawable.ref_overjet, "Overjet", "Fotografía clínica de overjet.", "Wikimedia Commons · Rama2k1 · CC BY-SA 4.0")),
    PhotoOptionV46("Overbite / relación vertical", "Mordida profunda", "Cuantifica el solapamiento vertical en mm o porcentaje y revisa si existe contacto traumático.", ClinicalPhotoV46(R.drawable.ref_deep_bite, "Mordida profunda", "Fotografía clínica de sobremordida profunda.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Overbite / relación vertical", "Mordida abierta anterior", "Se observa un espacio vertical anterior al cerrar. Registra magnitud y distribución.", ClinicalPhotoV46(R.drawable.ref_open_bite, "Mordida abierta anterior", "Fotografía clínica de mordida abierta anterior.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Líneas medias", "Desviación / plano oclusal", "Compara líneas interincisivas con referencias faciales y entre arcadas; registra lado y milímetros.", ClinicalPhotoV46(R.drawable.ref_midline, "Referencia frontal", "Fotografía clínica con inclinación del plano oclusal útil para observar referencias faciales y dentales.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Mordida cruzada", "Anterior", "Uno o más dientes anteriores superiores ocluyen por lingual de sus antagonistas inferiores; diferencia componente dentario, funcional y esquelético.", ClinicalPhotoV46(R.drawable.ref_crossbite_anterior, "Mordida cruzada anterior", "Fotografía clínica de mordida cruzada anterior.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Mordida cruzada", "Posterior unilateral", "Valora ambos lados y la trayectoria de cierre para detectar desplazamiento funcional.", ClinicalPhotoV46(R.drawable.ref_crossbite_posterior, "Mordida cruzada posterior", "Fotografía clínica de mordida cruzada posterior unilateral.", "Wikimedia Commons · Giorgio Fiorelli · dominio público")),
    PhotoOptionV46("Espacios y alineación", "Diastema", "Describe localización, tamaño y distribución; la causa no se determina sólo por la fotografía.", ClinicalPhotoV46(R.drawable.ref_diastema, "Diastema", "Fotografía clínica de diastema maxilar medio.", "Wikimedia Commons · Ian Furst · CC BY-SA 4.0")),
    PhotoOptionV46("Espacios y alineación", "Apiñamiento", "Observa rotaciones, solapamientos y falta de espacio; el análisis de discrepancia arco-diente requiere medición.", ClinicalPhotoV46(R.drawable.ref_crowding, "Apiñamiento", "Fotografía clínica de apiñamiento dental severo.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0"))
)

@Composable
fun OcclusionPhotoAtlasV46Screen(lang: String, onBack: () -> Unit) {
    var selected by remember { mutableStateOf(0) }
    ResponsiveScreenV17(
        tr(lang, "Examen de oclusión · atlas fotográfico HD", "Occlusion exam · HD photo atlas"),
        tr(lang, "Fotografías clínicas reales empaquetadas en la app; sin triángulos ni dibujos sustituyendo dientes.", "Real clinical photographs packaged in the app; no geometric tooth substitutes."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang, "Las imágenes son ejemplos educativos. Clasifica y mide en el paciente; una fotografía aislada no establece diagnóstico.", "Images are teaching examples. Classify and measure the patient; a photo alone does not establish diagnosis."))
        ClinicalPhotoCardV46(occlusionPhotoOptionsV46[selected].photo)
        occlusionPhotoOptionsV46.groupBy { it.group }.forEach { (group, options) ->
            ResponsiveSectionV17(group) {
                AdaptiveGridV17(options.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else options.size.coerceAtMost(3)) { i ->
                    val item = options[i]
                    val global = occlusionPhotoOptionsV46.indexOf(item)
                    FilterChip(selected == global, { selected = global }, { Text(item.label) }, Modifier.fillMaxWidth())
                }
                val item = occlusionPhotoOptionsV46[selected]
                if (item.group == group) Text(item.explanation)
            }
        }
    }
}

private val tmjPhotosV46 = listOf(
    ClinicalPhotoV46(R.drawable.ref_tmj_anatomy, "Anatomía de la ATM", "Referencia anatómica del cóndilo, disco, fosa/eminencia y tejidos articulares.", "Wikimedia Commons · Frank Gaillard · CC BY-SA 3.0/GFDL"),
    ClinicalPhotoV46(R.drawable.ref_tmj_mri, "ATM en resonancia magnética", "Imagen real de resonancia magnética de la articulación temporomandibular.", "Wikimedia Commons · ARTICULATIONMAN · CC BY-SA 4.0"),
    ClinicalPhotoV46(R.drawable.ref_tmj_movements, "Movimientos mandibulares", "Referencia del movimiento mandibular y posiciones límite; correlaciona con apertura, protrusión y trayectorias.", "Wikimedia Commons · Rjmedink · licencia indicada en la ficha original"),
    ClinicalPhotoV46(R.drawable.ref_tmj_panorama, "Cóndilo y fosa en imagen radiográfica", "Panorámica enfocada a la región de cóndilo y fosa articular; no sustituye estudios indicados para patología específica.", "Wikimedia Commons · ANUG · CC BY-SA" )
)

@Composable
fun TmjPhotoAtlasV46Screen(lang: String, onBack: () -> Unit) {
    var photo by remember { mutableStateOf(0) }
    var opening by remember { mutableStateOf("40–50 mm aprox.") }
    val findings = remember { mutableStateListOf<String>() }
    val checklist = listOf("Dolor al abrir/cerrar", "Chasquido", "Crepitación", "Desviación", "Deflexión", "Bloqueo", "Limitación funcional", "Dolor muscular", "Sin alteraciones aparentes")
    ResponsiveScreenV17("Exploración de ATM y TTM · imágenes reales", "Anatomía e imagenología real, movimientos, apertura y hallazgos.", onBack) { profile ->
        ClinicalPhotoCardV46(tmjPhotosV46[photo])
        ResponsiveSectionV17("Referencias visuales") {
            AdaptiveGridV17(tmjPhotosV46.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                FilterChip(photo == i, { photo = i }, { Text(tmjPhotosV46[i].title) }, Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17("Apertura máxima") {
            listOf("<30 mm limitada", "30–39 mm reducida/valorar", "40–50 mm aprox.", ">50 mm variación/hipermovilidad a contextualizar").forEach { x ->
                FilterChip(opening == x, { opening = x }, { Text(x) }, Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17("Hallazgos") {
            checklist.forEach { x -> FilterChip(x in findings, { if (x in findings) findings.remove(x) else findings.add(x) }, { Text(x) }, Modifier.fillMaxWidth()) }
        }
        NoticeCard("Dolor, ruidos o una imagen aislada no equivalen automáticamente a desplazamiento discal, osteoartrosis u otro TTM. Integra historia, palpación, movimientos y estudios cuando estén indicados.")
    }
}

private val dentalAnomalyOptionsV46 = listOf(
    PhotoOptionV46("Número", "Agenesia / hipodoncia", "Ausencia congénita de uno o más dientes; confirmar con historia, etapa de desarrollo e imagen.", ClinicalPhotoV46(R.drawable.ref_hypodontia, "Hipodoncia", "Radiografía panorámica con ausencia congénita de varios elementos dentarios.", "Wikimedia Commons · Ramirotomasi · CC BY-SA 4.0")),
    PhotoOptionV46("Número", "Supernumerario / mesiodens", "Diente adicional a la fórmula normal; registra localización y relación con dientes vecinos.", ClinicalPhotoV46(R.drawable.ref_supernumerary, "Dientes supernumerarios", "Radiografía de dos dientes supernumerarios en premaxila.", "Wikimedia Commons · Albert · dominio público")),
    PhotoOptionV46("Tamaño", "Microdoncia", "Tamaño dentario menor al esperado; distingue microdoncia localizada de discrepancia relativa diente-arco.", ClinicalPhotoV46(R.drawable.ref_microdontia, "Microdoncia e hipodoncia", "Ejemplo clínico publicado con microdoncia e hipodoncia; no implica el síndrome en otros pacientes.", "Wikimedia Commons · artículo de caso · CC BY 2.0")),
    PhotoOptionV46("Tamaño", "Macrodoncia", "Tamaño dentario mayor al esperado; compara con homólogo y proporción de la arcada.", ClinicalPhotoV46(R.drawable.ref_macrodontia, "Macrodoncia de incisivos centrales", "Ejemplo clínico de macrodoncia de incisivos centrales en un caso publicado; la imagen no debe usarse para atribuir un síndrome.", "Wikimedia Commons · fuente clínica citada en la ficha original")),
    PhotoOptionV46("Forma", "Fusión / diente doble", "La fusión implica unión de gérmenes; el aspecto clínico debe diferenciarse de geminación y confirmarse con conteo e imagen.", ClinicalPhotoV46(R.drawable.ref_fusion, "Posible fusión dental", "Fotografía clínica descrita por el autor como posible fusión; sin radiografía no se presenta como diagnóstico definitivo.", "Wikimedia Commons · Roquex · CC0")),
    PhotoOptionV46("Forma", "Dens invaginatus", "Invaginación del órgano dentario; la extensión se determina con imagen y puede tener relevancia pulpar.", ClinicalPhotoV46(R.drawable.ref_dens_invaginatus, "Dens invaginatus", "Esquema de tipos de Oehlers derivado de un caso/revisión de acceso abierto.", "Wikimedia Commons · Meghana/Thejokrishna/Hellerhoff · CC BY 3.0")),
    PhotoOptionV46("Forma", "Dens evaginatus", "Cúspide o tubérculo accesorio; revisa desgaste/fractura y posible extensión pulpar.", ClinicalPhotoV46(R.drawable.ref_dens_evaginatus, "Dens evaginatus", "Fotografía clínica de anomalía del desarrollo tipo cúspide accesoria.", "Wikimedia Commons · Veeresh likhitha · CC BY-SA 4.0")),
    PhotoOptionV46("Raíz / cámara", "Taurodontismo", "Cámara pulpar alargada con desplazamiento apical del piso/furcación; se reconoce radiográficamente.", ClinicalPhotoV46(R.drawable.ref_taurodontism, "Taurodontismo", "Radiografía de un diente con morfología taurodóntica y tratamiento endodóntico.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Estructura", "Hipoplasia del esmalte", "Defecto cuantitativo del esmalte. Describe distribución, profundidad y cronología probable sin asumir etiología por aspecto.", ClinicalPhotoV46(R.drawable.ref_enamel_hypoplasia, "Hipoplasia del esmalte", "Fotografía de líneas de hipoplasia del esmalte.", "Wikimedia Commons · Otis Historical Archives/NMHM · CC BY 2.0")),
    PhotoOptionV46("Estructura", "Hipomineralización", "Defecto cualitativo con opacidad demarcada; diferencia de caries, fluorosis e hipoplasia mediante historia y examen.", ClinicalPhotoV46(R.drawable.ref_hypomineralization, "Hipomineralización", "Fotografía clínica de opacidad demarcada en incisivo.", "Wikimedia Commons · Federico Morales Corona · CC BY-SA 4.0"))
)

@Composable
fun DentalAnomaliesPhotoV46Screen(lang: String, onBack: () -> Unit) = PhotoOptionAtlasV46(
    title = "Anomalías dentales · atlas fotográfico",
    subtitle = "Número, tamaño, forma, raíz/cámara y estructura con fotografías o radiografías reales.",
    options = dentalAnomalyOptionsV46,
    onBack = onBack
)

private val eruptionOptionsV46 = listOf(
    PhotoOptionV46("Trayectoria / retención", "Impactación de canino", "El diente no logra una erupción funcional por posición, espacio u obstáculo; localización y relación con raíces vecinas requieren imagen.", ClinicalPhotoV46(R.drawable.ref_impacted_canine, "Canino impactado", "Radiografía de canino impactado.", "Wikimedia Commons · DRosenbach · CC BY-SA 3.0")),
    PhotoOptionV46("Trayectoria / retención", "Impactación de segundo molar", "La inclinación y relación con el diente adyacente pueden impedir la erupción.", ClinicalPhotoV46(R.drawable.ref_impacted_second_molar, "Segundo molar impactado", "Radiografía con segundo molar impactado y germen de tercer molar en desarrollo.", "Wikimedia Commons · Coronation Dental Specialty Group · CC BY-SA 3.0")),
    PhotoOptionV46("Trayectoria / retención", "Inclusión / retención radiográfica", "Un diente no visible clínicamente puede estar intraóseo; determina desarrollo, posición, obstáculos y relación anatómica.", ClinicalPhotoV46(R.drawable.ref_impacted_panorama, "Panorámica con dientes impactados", "Radiografía panorámica de adolescente con terceros molares impactados; sirve para enseñar lectura de posición y retención.", "Wikimedia Commons · Coronation Dental Specialty Group · CC BY 3.0")),
    PhotoOptionV46("Erupción temprana", "Dentición infantil en erupción", "La cronología se interpreta por edad, diente, simetría y desarrollo. Un diente visible en un lactante no debe etiquetarse natal/neonatal sin la edad exacta de aparición.", ClinicalPhotoV46(R.drawable.ref_infant_teeth, "Dientes temporales en lactante", "Fotografía de incisivos temporales mandibulares en un lactante; referencia de erupción temprana, no ejemplo de diente natal.", "Wikimedia Commons · Chrisbwah · CC BY-SA 3.0")),
    PhotoOptionV46("Evaluación radiográfica", "Retraso / asimetría eruptiva", "Compara con el homólogo contralateral y etapa de formación radicular; el retraso no se define sólo por una edad promedio.", ClinicalPhotoV46(R.drawable.ref_impacted_panorama, "Panorámica para valorar cronología y posición", "Ejemplo de panorámica para enseñar a revisar desarrollo, trayectoria y dientes retenidos.", "Wikimedia Commons · Coronation Dental Specialty Group · CC BY 3.0"))
)

@Composable
fun EruptionAnomaliesPhotoV46Screen(lang: String, onBack: () -> Unit) = PhotoOptionAtlasV46(
    title = "Anomalías de erupción y posición · atlas fotográfico",
    subtitle = "Fotografías y radiografías reales para reconocer trayectorias anómalas, retención e impactación.",
    options = eruptionOptionsV46,
    onBack = onBack
)

@Composable
private fun PhotoOptionAtlasV46(title: String, subtitle: String, options: List<PhotoOptionV46>, onBack: () -> Unit) {
    var selected by remember { mutableStateOf(0) }
    ResponsiveScreenV17(title, subtitle, onBack) { profile ->
        NoticeCard("Imagen de referencia ≠ diagnóstico. Describe primero el hallazgo y confirma con exploración, cronología, pruebas e imagenología cuando corresponda.")
        ClinicalPhotoCardV46(options[selected].photo)
        options.groupBy { it.group }.forEach { (group, items) ->
            ResponsiveSectionV17(group) {
                AdaptiveGridV17(items.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                    val item = items[i]
                    val global = options.indexOf(item)
                    Card(
                        onClick = { selected = global },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = if (selected == global) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(item.label, fontWeight = FontWeight.Black)
                            if (selected == global) Text(item.explanation)
                        }
                    }
                }
            }
        }
    }
}

private val mucosaPhotoOptionsV46 = listOf(
    PhotoOptionV46("Úlceras", "Úlcera aftosa", "Úlcera redonda/oval dolorosa con fondo fibrinoso y halo eritematoso. Localización, recurrencia y duración orientan el diferencial.", ClinicalPhotoV46(R.drawable.ref_aphthous, "Úlcera aftosa", "Fotografía clínica de úlcera aftosa en labio inferior.", "Wikimedia Commons · Genppy · GFDL/CC BY-SA migrada")),
    PhotoOptionV46("Lesiones blancas", "Leucoplasia clínica", "Placa blanca persistente que no se explica por otra entidad tras evaluación. El término clínico no equivale a resultado histopatológico.", ClinicalPhotoV46(R.drawable.ref_leukoplakia, "Leucoplasia oral", "Fotografía de leucoplasia oral cuya malignidad fue excluida histológicamente en el caso original.", "Wikimedia Commons · Klaus D. Peter · CC BY 3.0 DE")),
    PhotoOptionV46("Lesiones blancas", "Liquen plano / patrón reticular", "Estrías blancas reticulares pueden sugerir liquen plano/lesión liquenoide; integra distribución y, cuando proceda, histopatología.", ClinicalPhotoV46(R.drawable.ref_lichen_planus, "Liquen plano oral", "Fotografía de estrías blancas clásicas en mucosa yugal.", "Wikimedia Commons · Ian Furst · CC BY-SA 4.0")),
    PhotoOptionV46("Infecciosas / vesiculares", "Herpes labial", "Vesículas agrupadas que evolucionan a erosión/costra en un patrón recurrente típico; correlaciona con historia.", ClinicalPhotoV46(R.drawable.ref_herpes, "Herpes labial", "Fotografía de herpes labial en labio inferior.", "Wikimedia Commons · Metju12 · dominio público")),
    PhotoOptionV46("Infecciosas / placas", "Candidiasis oral", "La forma pseudomembranosa puede mostrar placas blancas removibles; otras formas pueden ser eritematosas. Contexto y pruebas complementarias pueden ser necesarios.", ClinicalPhotoV46(R.drawable.ref_candidiasis, "Candidiasis oral", "Fotografía clínica de candidiasis oral en lengua.", "Wikimedia Commons · James Heilman, MD · CC BY-SA 3.0/GFDL")),
    PhotoOptionV46("Lesiones salivales", "Mucocele", "Aumento de volumen blando, a veces azulado/translúcido, frecuente en labio inferior; el diferencial depende de localización y consistencia.", ClinicalPhotoV46(R.drawable.ref_mucocele, "Mucocele de labio inferior", "Fotografía clínica de mucocele en un niño pequeño.", "Wikimedia Commons · Ed Uthman · licencia Creative Commons indicada en la ficha")),
    PhotoOptionV46("Lesiones salivales", "Ránula", "Aumento de volumen en piso de boca relacionado con glándula sublingual; documenta tamaño, lateralidad, color y función.", ClinicalPhotoV46(R.drawable.ref_ranula, "Ránula", "Fotografía clínica de ránula en piso de boca.", "Wikimedia Commons · Ph0t0happy · CC BY-SA 3.0/GFDL")),
    PhotoOptionV46("Lengua", "Lengua geográfica", "Áreas depapiladas eritematosas con borde blanquecino y patrón migratorio. El cambio de localización en el tiempo es un dato útil.", ClinicalPhotoV46(R.drawable.ref_geographic_tongue, "Lengua geográfica", "Fotografía clínica en alta resolución de lengua geográfica.", "Wikimedia Commons · Martanopue · CC BY-SA 3.0")),
    PhotoOptionV46("Lengua", "Lengua fisurada / geográfica", "Los surcos dorsales pueden coexistir con lengua geográfica. Describe profundidad, síntomas y retención de detritos.", ClinicalPhotoV46(R.drawable.ref_fissured_tongue, "Lengua fisurada y geográfica", "Fotografía clínica de lengua fisurada con áreas geográficas.", "Wikimedia Commons · Kozlovsk~commonswiki · licencia indicada en la ficha")),
    PhotoOptionV46("Comisuras", "Queilitis angular", "Fisura, maceración o eritema en uno o ambos ángulos de la boca; suele ser multifactorial.", ClinicalPhotoV46(R.drawable.ref_angular_cheilitis, "Queilitis angular", "Fotografía clínica de queilitis angular.", "Wikimedia Commons · James Heilman, MD · CC BY-SA 3.0"))
)

@Composable
fun MucosaPhotoAtlasV46Screen(lang: String, onBack: () -> Unit) = PhotoOptionAtlasV46(
    title = "Examen de mucosas · atlas patológico fotográfico",
    subtitle = "Fotos clínicas reales de hallazgos frecuentes. Primero describe la lesión; después plantea diagnóstico presuntivo/diferencial.",
    options = mucosaPhotoOptionsV46,
    onBack = onBack
)
