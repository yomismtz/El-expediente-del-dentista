package com.yomismtz.expedientedeldentista.ui
import com.yomismtz.expedientedeldentista.clinical.tmjDifferentialV55

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
        OfflineClinicalImageV50(
            drawable = photo.drawable,
            contentDescription = photo.caption,
            maxHeight = 360.dp
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
    PhotoOptionV46("Plano terminal", "Escalón distal", "En dentición temporal, la superficie distal del segundo molar inferior queda distal respecto a la del superior. La imagen procede del material de referencia y fue auditada visualmente contra su página original.", ClinicalPhotoV46(R.drawable.pdf_terminal_distal, "Plano terminal · escalón distal", "Fotografía original extraída sin recorte ni redibujo del material proporcionado por el usuario.", "Fuente de referencia proporcionada por el usuario · p. 67 · uso educativo interno · compilación CI")),
    PhotoOptionV46("Dentición", "Dentición temporal", "Dentición decidua. Identifica la etapa antes de clasificar relaciones oclusales; la fotografía no documenta por sí sola un plano terminal específico.", ClinicalPhotoV46(R.drawable.ref_deciduous_teeth, "Dentición temporal", "Fotografía clínica de dentición temporal usada como referencia anatómica general.", "Wikimedia Commons · David Shankbone · CC BY-SA 3.0/GFDL")),
    PhotoOptionV46("Dentición", "Dentición mixta", "Coexisten dientes temporales y permanentes. La secuencia eruptiva y la edad dental deben valorarse clínicamente.", ClinicalPhotoV46(R.drawable.ref_mixed_dentition, "Dentición mixta", "Fotografía clínica de dentición mixta en un niño de aproximadamente 8 años y medio.", "Wikimedia Commons · Roquex · CC0 · File:MixedDentition.jpg")),
    PhotoOptionV46("Dentición", "Dentición permanente", "Predominan los dientes permanentes; registra dientes presentes, ausentes y estado eruptivo antes de interpretar la oclusión.", ClinicalPhotoV46(R.drawable.ref_permanent_dentition, "Dentición permanente", "Fotografía clínica de dentición permanente en una paciente de casi 19 años.", "Wikimedia Commons · Roquex · CC0 · File:PermanentTeeth.jpg")),
    PhotoOptionV46("Relación molar de Angle", "Clase I", "La relación molar debe valorarse bilateralmente en máxima intercuspidación. Una Clase I molar no significa que toda la oclusión sea normal.", ClinicalPhotoV46(R.drawable.ref_angle_i, "Referencia de Clase I", "Fotografía clínica de una relación de Clase I con protrusión bimaxilar; sirve para localizar las referencias molares, no para asumir normalidad global.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Relación molar de Angle", "Clase II", "El molar inferior está relativamente distal respecto al superior; registra lado y, cuando proceda, división incisiva.", ClinicalPhotoV46(R.drawable.ref_angle_ii, "Angle Clase II/1", "Fotografía clínica de maloclusión Clase II división 1; permite observar la relación molar y el resalte, sin sustituir la medición clínica.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0 · File:Class2division1malocclusion.jpg")),
    PhotoOptionV46("Relación molar de Angle", "Clase III", "El molar inferior está relativamente mesial respecto al superior; correlaciona con incisivos, caninos y componente funcional/esquelético.", ClinicalPhotoV46(R.drawable.ref_angle_iii, "Angle Clase III", "Fotografía clínica de maloclusión Clase III; sirve como referencia visual de la relación sagital y no define por sí sola el componente esquelético.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0 · File:Class 3 Malocclusion.jpg")),
    PhotoOptionV46("Overjet", "Resalte horizontal", "Mide en milímetros. La fotografía ayuda a reconocer la dimensión horizontal, pero el valor debe medirse clínicamente.", ClinicalPhotoV46(R.drawable.ref_overjet, "Overjet", "Fotografía clínica de overjet.", "Wikimedia Commons · Rama2k1 · CC BY-SA 4.0")),
    PhotoOptionV46("Overbite / relación vertical", "Mordida profunda", "Cuantifica el solapamiento vertical en mm o porcentaje y revisa si existe contacto traumático.", ClinicalPhotoV46(R.drawable.ref_deep_bite, "Mordida profunda", "Fotografía clínica de sobremordida profunda.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Overbite / relación vertical", "Mordida abierta anterior", "Se observa un espacio vertical anterior al cerrar. Registra magnitud y distribución.", ClinicalPhotoV46(R.drawable.ref_open_bite, "Mordida abierta anterior", "Fotografía clínica de mordida abierta anterior.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Líneas medias", "Desviación de línea media", "Compara las líneas interincisivas entre arcadas y, cuando la fotografía facial lo permita, con referencias faciales. Registra lado y milímetros; la imagen también muestra desgaste por bruxismo y no debe usarse para atribuir una causa a la desviación.", ClinicalPhotoV46(R.drawable.ref_midline, "Desviación de línea media dental", "Fotografía clínica cuyo archivo original documenta discrepancia de línea media; muestra además desgaste dentario por bruxismo.", "Wikimedia Commons · DRosenbach · dominio público · File:Deviated midline 2.JPG")),
    PhotoOptionV46("Mordida cruzada", "Anterior", "Uno o más dientes anteriores superiores ocluyen por lingual de sus antagonistas inferiores; diferencia componente dentario, funcional y esquelético.", ClinicalPhotoV46(R.drawable.ref_crossbite_anterior, "Mordida cruzada anterior", "Fotografía clínica de mordida cruzada anterior.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
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
    ClinicalPhotoV46(R.drawable.ref_tmj_movements, "Movimientos mandibulares · diagrama", "Diagrama educativo del sobre de movimiento mandibular (Posselt); correlaciona rotación, traslación, apertura y posiciones límite.", "Wikimedia Commons · Rjmedink · CC BY-SA 4.0 · File:TMJ movements.jpg"),
    ClinicalPhotoV46(R.drawable.ref_tmj_panorama, "Serie radiográfica de cóndilo y fosa", "Montaje de proyecciones radiográficas de la región condilar obtenido con equipo panorámico; muestra fosa y cóndilo, pero no es una ortopantomografía dental completa ni una prueba diagnóstica específica de TTM.", "Wikimedia Commons · ANUG · CC BY-SA 4.0 · File:TMJ panorama.jpg" )
)

@Composable
fun TmjPhotoAtlasV46Screen(lang: String, onBack: () -> Unit) {
    var photo by remember { mutableStateOf(0) }
    var opening by remember { mutableStateOf("40–50 mm aprox.") }
    val findings = remember { mutableStateListOf<String>() }
    val checklist = listOf("Dolor al abrir/cerrar", "Dolor preauricular", "Dolor al masticar", "Dolor muscular", "Fatiga mandibular", "Chasquido", "Crepitación", "Desviación", "Deflexión", "Bloqueo", "Limitación funcional", "Bruxismo / apretamiento", "Cefalea / dolor de cabeza", "Traumatismo reciente", "Aumento de volumen / inflamación", "Fiebre", "Parestesia / alteración sensitiva", "Sin alteraciones aparentes")
    ResponsiveScreenV17("Exploración de ATM y TTM · referencias visuales", "Fotografía/imagenología real cuando corresponde y diagrama educativo identificado como tal.", onBack) { profile ->
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
        val orientation = tmjDifferentialV55(findings.toSet(), opening)
        ResponsiveSectionV17("Orientación diagnóstica presuntiva") {
            Text(orientation.primaryOrientation, fontWeight = FontWeight.Black)
            orientation.differentials.forEach { Text("• $it") }
            orientation.redFlags.forEach { Text("⚠ $it", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) }
            Text(orientation.note, style = MaterialTheme.typography.bodySmall)
        }
        NoticeCard("Dolor, ruidos o una imagen aislada no equivalen automáticamente a desplazamiento discal, osteoartrosis u otro TTM. Integra historia, palpación, movimientos y estudios cuando estén indicados.")
    }
}

private val dentalAnomalyOptionsV46 = listOf(
    PhotoOptionV46("Número", "Agenesia / hipodoncia", "Ausencia congénita de uno o más dientes; confirmar con historia, etapa de desarrollo e imagen.", ClinicalPhotoV46(R.drawable.ref_hypodontia, "Hipodoncia", "Radiografía panorámica con ausencia congénita de varios elementos dentarios.", "Wikimedia Commons · Ramirotomasi · CC BY-SA 4.0")),
    PhotoOptionV46("Número", "Supernumerario / mesiodens", "Diente adicional a la fórmula normal; registra localización y relación con dientes vecinos.", ClinicalPhotoV46(R.drawable.ref_supernumerary, "Dientes supernumerarios", "Radiografía de dos dientes supernumerarios en premaxila.", "Wikimedia Commons · Albert · dominio público")),
    PhotoOptionV46("Forma", "Fusión / diente doble", "La fusión implica unión de gérmenes; el aspecto clínico debe diferenciarse de geminación y confirmarse con conteo e imagen.", ClinicalPhotoV46(R.drawable.ref_fusion, "Posible fusión dental", "Fotografía clínica descrita por el autor como posible fusión; sin radiografía no se presenta como diagnóstico definitivo.", "Wikimedia Commons · Roquex · CC0")),
    PhotoOptionV46("Forma", "Dens invaginatus", "Invaginación del órgano dentario; la extensión se determina con imagen y puede tener relevancia pulpar.", ClinicalPhotoV46(R.drawable.ref_dens_invaginatus, "Dens invaginatus", "Esquema de tipos de Oehlers derivado de un caso/revisión de acceso abierto.", "Wikimedia Commons · Meghana/Thejokrishna/Hellerhoff · CC BY 3.0")),
    PhotoOptionV46("Raíz / cámara", "Taurodontismo", "Cámara pulpar alargada con desplazamiento apical del piso/furcación; se reconoce radiográficamente.", ClinicalPhotoV46(R.drawable.ref_taurodontism, "Taurodontismo", "Radiografía de un diente con morfología taurodóntica y tratamiento endodóntico.", "Wikimedia Commons · Challiyan · CC BY-SA 4.0")),
    PhotoOptionV46("Estructura", "Hipoplasia del esmalte", "Defecto cuantitativo del esmalte. Describe distribución, profundidad y cronología probable sin asumir etiología por aspecto.", ClinicalPhotoV46(R.drawable.ref_enamel_hypoplasia, "Hipoplasia del esmalte", "Fotografía de líneas de hipoplasia del esmalte.", "Wikimedia Commons · Otis Historical Archives/NMHM · CC BY 2.0")),
    PhotoOptionV46("Estructura", "Hipomineralización", "Defecto cualitativo con opacidad demarcada; diferencia de caries, fluorosis e hipoplasia mediante historia y examen.", ClinicalPhotoV46(R.drawable.ref_hypomineralization, "Hipomineralización", "Fotografía clínica de opacidad demarcada en incisivo.", "Wikimedia Commons · Federico Morales Corona · CC BY-SA 4.0"))
)

@Composable
fun DentalAnomaliesPhotoV46Screen(lang: String, onBack: () -> Unit) = PhotoOptionAtlasV46(
    title = "Anomalías dentales · atlas fotográfico",
    subtitle = "Número, forma, raíz/cámara y estructura con fotografías, radiografías y esquemas identificados según su tipo.",
    options = dentalAnomalyOptionsV46,
    onBack = onBack
)

private val eruptionOptionsV46 = listOf(
    PhotoOptionV46("Trayectoria / retención", "Impactación de canino", "El diente no logra una erupción funcional por posición, espacio u obstáculo; localización y relación con raíces vecinas requieren imagen.", ClinicalPhotoV46(R.drawable.ref_impacted_canine, "Canino impactado", "Radiografía de canino impactado.", "Wikimedia Commons · DRosenbach · CC BY-SA 3.0")),
    PhotoOptionV46("Trayectoria / retención", "Impactación de segundo molar", "La inclinación y relación con el diente adyacente pueden impedir la erupción.", ClinicalPhotoV46(R.drawable.ref_impacted_second_molar, "Segundo molar impactado", "Radiografía con segundo molar impactado y germen de tercer molar en desarrollo.", "Wikimedia Commons · Coronation Dental Specialty Group · CC BY-SA 3.0")),
    PhotoOptionV46("Trayectoria / retención", "Inclusión / retención radiográfica", "Un diente no visible clínicamente puede estar intraóseo; determina desarrollo, posición, obstáculos y relación anatómica.", ClinicalPhotoV46(R.drawable.ref_impacted_panorama, "Panorámica con dientes impactados", "Radiografía panorámica de adolescente con terceros molares impactados; sirve para enseñar lectura de posición y retención.", "Wikimedia Commons · Coronation Dental Specialty Group · CC BY 3.0")),
    PhotoOptionV46("Erupción temprana", "Dentición infantil en erupción", "La cronología se interpreta por edad, diente, simetría y desarrollo. Un diente visible en un lactante no debe etiquetarse natal/neonatal sin la edad exacta de aparición.", ClinicalPhotoV46(R.drawable.ref_infant_teeth, "Dientes temporales en lactante", "Fotografía de incisivos temporales mandibulares en un lactante; referencia de erupción temprana, no ejemplo de diente natal.", "Wikimedia Commons · Chrisbwah · CC BY-SA 3.0")),
    PhotoOptionV46("Evaluación radiográfica", "Desarrollo y trayectoria", "Usa la panorámica para revisar etapa de formación, posición y trayectoria. Esta fotografía NO se presenta como demostración específica de retraso eruptivo o asimetría.", ClinicalPhotoV46(R.drawable.ref_impacted_panorama, "Panorámica para revisar desarrollo y posición", "Ejemplo radiográfico para enseñar una lectura sistemática de desarrollo y posición; no diagnostica retraso eruptivo por sí solo.", "Wikimedia Commons · Coronation Dental Specialty Group · CC BY 3.0"))
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
    PhotoOptionV46("Lesiones salivales", "Mucocele", "Aumento de volumen blando, a veces azulado/translúcido, frecuente en labio inferior; el diferencial depende de localización y consistencia.", ClinicalPhotoV46(R.drawable.ref_mucocele, "Mucocele de labio inferior", "Fotografía clínica de una lesión diagnosticada como mucocele del labio inferior; se usa para reconocer el aspecto superficial, no la histología.", "Wikimedia Commons · Dozenist · CC BY-SA 3.0/GFDL · File:Mucocele02-17-06cropped.jpg")),
    PhotoOptionV46("Lesiones salivales", "Ránula", "Aumento de volumen en piso de boca relacionado con glándula sublingual; documenta tamaño, lateralidad, color y función.", ClinicalPhotoV46(R.drawable.ref_ranula, "Ránula", "Fotografía clínica de ránula en piso de boca.", "Wikimedia Commons · Ph0t0happy · CC BY-SA 3.0/GFDL")),
    PhotoOptionV46("Lengua", "Lengua geográfica", "Áreas depapiladas eritematosas con borde blanquecino y patrón migratorio. El cambio de localización en el tiempo es un dato útil.", ClinicalPhotoV46(R.drawable.ref_geographic_tongue, "Lengua geográfica", "Fotografía clínica en alta resolución de lengua geográfica.", "Wikimedia Commons · Martanopue · CC BY-SA 3.0")),
    PhotoOptionV46("Lengua", "Lengua fisurada / geográfica", "Los surcos dorsales pueden coexistir con lengua geográfica. Describe profundidad, síntomas y retención de detritos.", ClinicalPhotoV46(R.drawable.ref_fissured_tongue, "Lengua fisurada y geográfica", "Fotografía clínica que muestra surcos dorsales junto con áreas de lengua geográfica; no se atribuye etiología a partir de la imagen.", "Wikimedia Commons · Kozlovsk~commonswiki · CC BY-SA 3.0/GFDL · File:Fissured geographic tongue.jpg")),
    PhotoOptionV46("Comisuras", "Queilitis angular", "Fisura, maceración o eritema en uno o ambos ángulos de la boca; suele ser multifactorial.", ClinicalPhotoV46(R.drawable.ref_angular_cheilitis, "Queilitis angular", "Fotografía clínica de queilitis angular.", "Wikimedia Commons · James Heilman, MD · CC BY-SA 3.0"))
)

private data class PrimaryLesionTermV52(val name: String, val definition: String)
private val primaryLesionTermsV52 = listOf(
    PrimaryLesionTermV52("Mácula / mancha", "Cambio circunscrito de color sin elevación ni depresión palpable."),
    PrimaryLesionTermV52("Pápula", "Lesión sólida, elevada y pequeña; describe tamaño, color, superficie y localización."),
    PrimaryLesionTermV52("Placa", "Lesión elevada o engrosada, de superficie relativamente amplia; puede formarse por confluencia de pápulas."),
    PrimaryLesionTermV52("Nódulo", "Lesión sólida palpable, más profunda que una pápula; registra tamaño, consistencia y movilidad."),
    PrimaryLesionTermV52("Vesícula", "Elevación pequeña con contenido líquido. Si se rompe puede dejar una erosión."),
    PrimaryLesionTermV52("Ampolla", "Elevación con contenido líquido de mayor tamaño que una vesícula."),
    PrimaryLesionTermV52("Pústula", "Elevación circunscrita con contenido purulento."),
    PrimaryLesionTermV52("Erosión", "Pérdida superficial del epitelio; no equivale a una úlcera."),
    PrimaryLesionTermV52("Úlcera", "Pérdida de epitelio que se extiende al tejido conjuntivo; describe fondo, bordes, tamaño, dolor y duración."),
    PrimaryLesionTermV52("Fisura", "Hendidura lineal de la superficie epitelial."),
    PrimaryLesionTermV52("Costra", "Exudado seco sobre una superficie; es más habitual en piel o bermellón que en mucosa húmeda."),
    PrimaryLesionTermV52("Pigmentación", "Describe color, distribución, bordes, simetría y evolución antes de proponer una etiología.")
)

@Composable
fun MucosaPhotoAtlasV46Screen(lang: String, onBack: () -> Unit) {
    var showTerms by remember { mutableStateOf(true) }
    if (showTerms) {
        ResponsiveScreenV17(
            tr(lang, "Términos para describir lesiones", "Terms for describing lesions"),
            tr(lang, "Antes del atlas, aprende la morfología básica. Melanoma y melasma son diagnósticos/entidades, no tipos morfológicos equivalentes a pápula, vesícula o úlcera.", "Before the atlas, learn basic morphology. Melanoma and melasma are diagnoses/entities, not morphologic lesion types equivalent to papule, vesicle or ulcer."),
            onBack
        ) { profile ->
            primaryLesionTermsV52.forEach { term ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(term.name, fontWeight = FontWeight.Black)
                        Text(term.definition)
                    }
                }
            }
            NoticeCard(tr(lang, "Melanoma: neoplasia maligna de melanocitos. Melasma: hiperpigmentación adquirida, típicamente cutánea. No deben usarse como términos elementales para describir la morfología de una lesión oral.", "Melanoma: malignant neoplasm of melanocytes. Melasma: acquired hyperpigmentation, typically cutaneous. They should not be used as elementary terms describing oral-lesion morphology."))
            FilterChip(selected = false, onClick = { showTerms = false }, label = { Text(tr(lang, "Abrir atlas de mucosas", "Open mucosal atlas")) }, modifier = Modifier.fillMaxWidth())
        }
    } else {
        PhotoOptionAtlasV46(
            title = "Examen de mucosas · atlas patológico fotográfico",
            subtitle = "Fotos clínicas reales de hallazgos frecuentes. Primero describe la lesión; después plantea diagnóstico presuntivo/diferencial.",
            options = mucosaPhotoOptionsV46,
            onBack = { showTerms = true }
        )
    }
}
