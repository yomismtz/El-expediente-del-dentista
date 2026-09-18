package com.yomismtz.expedientedeldentista.ui

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.IcdasSurfaceV48
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark
import com.yomismtz.expedientedeldentista.clinical.ToothRecord
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

@Composable
fun ToothSelector(teeth: List<Int>, selected: Int, onSelected: (Int) -> Unit, isMarked: (Int) -> Boolean = { false }) {
    DentalArchSelector(teeth, selected, onSelected, isMarked)
}

private fun statusName(status: ToothStatus, lang: String) = when (status) {
    ToothStatus.HEALTHY -> tr(lang, "Presente / sano", "Present / sound")
    ToothStatus.CARIES -> tr(lang, "Cariado", "Carious")
    ToothStatus.RESTORED -> tr(lang, "Obturado", "Filled")
    ToothStatus.MISSING_CARIES -> tr(lang, "Ausente por caries", "Missing due to caries")
    ToothStatus.MISSING_OTHER -> tr(lang, "Ausente por otra causa", "Missing for another reason")
    ToothStatus.EXTRACTION_INDICATED -> tr(lang, "Extracción indicada", "Extraction indicated")
    ToothStatus.SEALANT -> tr(lang, "Sellador", "Sealant")
}

private fun surfaceName(surface: Surface, lang: String) = when (surface) {
    Surface.VESTIBULAR -> tr(lang, "Vestibular", "Buccal")
    Surface.LINGUAL_PALATAL -> tr(lang, "Lingual / palatina", "Lingual / palatal")
    Surface.MESIAL -> "Mesial"
    Surface.DISTAL -> "Distal"
    Surface.OCCLUSAL -> tr(lang, "Oclusal", "Occlusal")
}

private fun markName(mark: SurfaceMark, lang: String) = when (mark) {
    SurfaceMark.HEALTHY -> tr(lang, "Borrar / sano", "Clear / sound")
    SurfaceMark.CARIES -> tr(lang, "Caries · rojo", "Caries · red")
    SurfaceMark.RESTORATION -> tr(lang, "Restauración · azul", "Restoration · blue")
    SurfaceMark.SEALANT -> tr(lang, "Sellador · verde", "Sealant · green")
}

@Composable
fun OdontogramScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    var primary by remember { mutableStateOf(false) }
    var selectedTooth by remember { mutableStateOf(16) }
    var selectedMark by remember { mutableStateOf(SurfaceMark.CARIES) }
    var legendMode by remember { mutableStateOf("ODONTO") }
    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()
    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val marks = session.odontogramSurfaces[selectedTooth] ?: emptyMap()
    val crownSurfaces = listOf(Surface.VESTIBULAR, Surface.LINGUAL_PALATAL, Surface.MESIAL, Surface.DISTAL, Surface.OCCLUSAL)

    fun saveMap(newMarks: Map<Surface, SurfaceMark>) {
        val all = session.odontogramSurfaces.toMutableMap().apply { put(selectedTooth, newMarks) }
        val derived = when {
            newMarks.values.any { it == SurfaceMark.CARIES } -> ToothStatus.CARIES
            newMarks.values.any { it == SurfaceMark.RESTORATION } -> ToothStatus.RESTORED
            newMarks.values.any { it == SurfaceMark.SEALANT } -> ToothStatus.SEALANT
            else -> ToothStatus.HEALTHY
        }
        val teeth = session.teeth + (selectedTooth to record.copy(status = derived))
        onSessionChanged(session.copy(teeth = teeth, odontogramSurfaces = all, presentTeeth = session.presentTeeth + selectedTooth))
    }

    fun saveSurface(surface: Surface) {
        val newMarks = marks.toMutableMap()
        if (selectedMark == SurfaceMark.HEALTHY) newMarks.remove(surface) else newMarks[surface] = selectedMark
        saveMap(newMarks)
    }

    fun markWholeTooth() {
        if (selectedMark == SurfaceMark.HEALTHY) saveMap(emptyMap())
        else saveMap(crownSurfaces.associateWith { selectedMark })
    }

    fun setWholeStatus(status: ToothStatus) {
        val teeth = session.teeth + (selectedTooth to record.copy(status = status))
        val present = session.presentTeeth.toMutableSet()
        if (status == ToothStatus.MISSING_CARIES || status == ToothStatus.MISSING_OTHER) present.remove(selectedTooth) else present.add(selectedTooth)
        onSessionChanged(session.copy(teeth = teeth, presentTeeth = present))
    }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, "Odontograma interactivo", "Interactive odontogram"), onBack,
            tr(lang, "Marca una o varias superficies. También puedes aplicar la marca elegida a toda la corona.", "Mark one or several surfaces. You can also apply the selected mark to the whole crown.")) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!primary, { primary = false }, { Text(tr(lang,"Permanentes","Permanent")) })
                FilterChip(primary, { primary = true }, { Text(tr(lang,"Temporales","Primary")) })
            }
        }
        item { SectionCard(tr(lang,"Arcada dental","Dental arch")) {
            DentalArchSelector(shown, selectedTooth, { selectedTooth = it }) { tooth ->
                session.odontogramSurfaces[tooth]?.isNotEmpty() == true || session.teeth[tooth]?.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER)
            }
        } }
        item { SectionCard("OD $selectedTooth") {
            SurfaceMark.entries.forEach { mark -> FilterChip(selectedMark==mark,{selectedMark=mark},{Text(markName(mark,lang))},modifier=Modifier.fillMaxWidth()) }
            DentalSurfaceDiagram(
                centerEnabled=true,
                surfaceColor={ surface -> when(marks[surface]) {
                    SurfaceMark.CARIES -> Color(0xFFD64545)
                    SurfaceMark.RESTORATION -> Color(0xFF3C74C9)
                    SurfaceMark.SEALANT -> Color(0xFF62A56A)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }},
                onSurfaceTap={saveSurface(it)}, modifier=Modifier.fillMaxWidth()
            )
            OutlinedButton(onClick={markWholeTooth()},modifier=Modifier.fillMaxWidth()) {
                Text(if(selectedMark==SurfaceMark.HEALTHY) tr(lang,"Limpiar todas las caras","Clear all surfaces") else tr(lang,"Aplicar a todas las caras","Apply to all surfaces"))
            }
            Text(tr(lang,"Estados que afectan al diente completo:","Whole-tooth states:"),fontWeight=FontWeight.Bold)
            listOf(ToothStatus.HEALTHY,ToothStatus.MISSING_CARIES,ToothStatus.MISSING_OTHER,ToothStatus.EXTRACTION_INDICATED).forEach { status ->
                FilterChip(record.status==status && (status!=ToothStatus.HEALTHY || marks.isEmpty()),{setWholeStatus(status)},{Text(statusName(status,lang))},modifier=Modifier.fillMaxWidth())
            }
        } }
        item {
            SectionCard(tr(lang,"Viñetas y lectura según el análisis","Legend by analysis")) {
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    listOf("ODONTO","ICDAS","CPOD","IHOS","IPC").forEach { mode -> FilterChip(legendMode==mode,{legendMode=mode},{Text(mode)},modifier=Modifier.weight(1f)) }
                }
                val text=when(legendMode) {
                    "ICDAS" -> tr(lang,"ICDAS se registra por superficie; el código principal del diente es el mayor código observado entre sus caras.","ICDAS is surface-based; the tooth's main code is the highest code observed among its surfaces.")
                    "CPOD" -> tr(lang,"CPOD/ceod usa el diente como unidad. Si un mismo diente está restaurado y además tiene caries activa, se clasifica como cariado para el índice.","DMFT/dmft uses the tooth as the unit. If a tooth is restored and also has active caries, it is counted as decayed for the index.")
                    "IHOS" -> tr(lang,"IHOS no usa todos los dientes: guía 16V, 11V, 26V, 36L, 31V y 46L, con códigos de detritos y cálculo.","OHI-S uses index surfaces: 16B, 11B, 26B, 36L, 31B and 46L, with debris/calculus codes.")
                    "IPC" -> tr(lang,"IPC se registra por sextantes y conserva únicamente el hallazgo de mayor código del sextante; no es un código por cara dental.","CPI is recorded by sextants and keeps the highest-code finding in the sextant; it is not a tooth-surface code.")
                    else -> tr(lang,"Rojo = caries · azul = restauración · verde = sellador · X/ausencia = diente completo. Las superficies pueden combinarse.","Red = caries · blue = restoration · green = sealant · X/missing = whole tooth. Surface markings can be combined.")
                }
                Text(text)
            }
        }
    }
}

private data class IcdasTeachingV48(
    val code: Int,
    val stageEs: String,
    val stageEn: String,
    val seeEs: String,
    val seeEn: String,
    val writeEs: String,
    val writeEn: String,
    val errorEs: String,
    val errorEn: String
)

private val icdasTeachingV48 = listOf(
    IcdasTeachingV48(
        0, "Sano", "Sound",
        "Sin evidencia de caries después de limpiar y secar aproximadamente 5 segundos. Tinciones, fluorosis, hipoplasia o desgaste no se convierten automáticamente en caries.",
        "No evidence of caries after cleaning and approximately 5 seconds of air drying. Staining, fluorosis, hypoplasia or wear are not automatically coded as caries.",
        "Ejemplo: “OD 16, superficie oclusal: ICDAS 0, sin evidencia visual de caries”.",
        "Example: “Tooth 16, occlusal surface: ICDAS 0, no visual evidence of caries”.",
        "Confundir una fisura pigmentada o un defecto del desarrollo con lesión de caries.",
        "Mistaking a stained fissure or developmental defect for a caries lesion."
    ),
    IcdasTeachingV48(
        1, "Lesión inicial", "Initial lesion",
        "Primer cambio visual del esmalte. Puede requerir secado prolongado para hacerse visible; suele verse como opacidad o cambio blanco/marrón limitado.",
        "First visual enamel change. Prolonged drying may be required to reveal it; it may appear as a limited white/brown opacity or discoloration.",
        "Ejemplo: “OD 26, O: ICDAS 1; primer cambio visual después del secado”.",
        "Example: “Tooth 26, O: ICDAS 1; first visual change after drying”.",
        "Asignar código 1 sin limpiar/secar la superficie o usarlo para cualquier mancha.",
        "Assigning code 1 without cleaning/drying the surface or using it for any stain."
    ),
    IcdasTeachingV48(
        2, "Lesión inicial", "Initial lesion",
        "Cambio visual evidente en esmalte visible con la superficie húmeda y que permanece visible después del secado; en fosas/fisuras suele extenderse más allá de la anatomía natural.",
        "Distinct visual enamel change visible while wet and still visible after drying; in pits/fissures it generally extends beyond the natural anatomy.",
        "Ejemplo: “OD 36, O: ICDAS 2; opacidad blanca/marrón evidente en húmedo y seco”.",
        "Example: “Tooth 36, O: ICDAS 2; distinct white/brown opacity visible wet and dry”.",
        "Confundir ICDAS 1 y 2 sin considerar si el cambio ya es visible antes del secado prolongado.",
        "Confusing ICDAS 1 and 2 without considering whether the change is already visible before prolonged drying."
    ),
    IcdasTeachingV48(
        3, "Lesión moderada", "Moderate lesion",
        "Ruptura localizada del esmalte atribuible a caries, sin dentina visible y sin la sombra dentinaria propia del código 4.",
        "Localized enamel breakdown caused by caries, without visible dentin and without the underlying dentinal shadow characteristic of code 4.",
        "Ejemplo: “OD 46, O: ICDAS 3; pérdida localizada de integridad del esmalte sin dentina visible”.",
        "Example: “Tooth 46, O: ICDAS 3; localized enamel breakdown without visible dentin”.",
        "Llamar código 3 a una cavidad donde ya se observa dentina.",
        "Using code 3 when dentin is already visibly exposed."
    ),
    IcdasTeachingV48(
        4, "Lesión moderada", "Moderate lesion",
        "Sombra oscura intrínseca de dentina visible a través del esmalte, con o sin ruptura localizada. Puede verse gris, azulada o marrón.",
        "Intrinsic dark shadow from dentin visible through enamel, with or without localized breakdown. It may look grey, blue or brown.",
        "Ejemplo: “OD 16, O: ICDAS 4; sombra oscura subyacente de dentina”.",
        "Example: “Tooth 16, O: ICDAS 4; underlying dark shadow from dentin”.",
        "Codificar como 4 una sombra que proviene claramente de caries en una superficie adyacente distinta.",
        "Coding as 4 a shadow clearly originating from caries on a different adjacent surface."
    ),
    IcdasTeachingV48(
        5, "Lesión extensa", "Extensive lesion",
        "Cavidad definida con dentina visible; la pérdida de estructura es inequívoca, pero no domina más de la mitad de la superficie evaluada.",
        "Distinct cavity with visible dentin; structural loss is unequivocal but does not involve more than half of the assessed surface.",
        "Ejemplo: “OD 37, O: ICDAS 5; cavidad definida con dentina visible”.",
        "Example: “Tooth 37, O: ICDAS 5; distinct cavity with visible dentin”.",
        "Usar código 5 cuando la cavidad ya es extensa y compromete más de la mitad de la superficie.",
        "Using code 5 when the cavity is already extensive and involves more than half of the surface."
    ),
    IcdasTeachingV48(
        6, "Lesión extensa", "Extensive lesion",
        "Cavidad extensa con dentina claramente visible en paredes y base; involucra más de la mitad de la superficie y puede aproximarse a la pulpa.",
        "Extensive distinct cavity with dentin clearly visible on walls and base; it involves more than half of the surface and may approach the pulp.",
        "Ejemplo: “OD 47, O: ICDAS 6; cavidad extensa con dentina visible”.",
        "Example: “Tooth 47, O: ICDAS 6; extensive cavity with visible dentin”.",
        "Convertir ICDAS 6 en un diagnóstico pulpar. La severidad de caries no sustituye las pruebas pulpares/periapicales.",
        "Turning ICDAS 6 into a pulpal diagnosis. Caries severity does not replace pulpal/periapical testing."
    )
)

private fun icdasActivityTextV48(code: Int, lang: String): String = when (code) {
    0 -> tr(
        lang,
        "Código 0 describe una superficie sana; no corresponde asignar actividad de lesión.",
        "Code 0 describes a sound surface; lesion activity is not assigned."
    )
    in 1..3 -> tr(
        lang,
        "Actividad se valora aparte del código: una lesión probablemente activa suele verse blanca/amarillenta, opaca, sin brillo y sentirse áspera con sonda de punta roma; una probablemente inactiva puede ser brillante, dura y lisa. La ubicación en zona de estancamiento de placa también orienta.",
        "Activity is assessed separately from severity: a likely active lesion is often whitish/yellowish, opaque, dull and rough with gentle ball-ended probing; a likely inactive lesion may be shiny, hard and smooth. Location in a plaque-stagnation area also contributes."
    )
    4 -> tr(
        lang,
        "La sombra dentinaria del código 4 se considera probablemente activa en los criterios de actividad, pero la actividad no debe inferirse sólo por el número: integra textura, brillo, placa y contexto clínico.",
        "A code-4 dentinal shadow is considered probably active in activity criteria, but activity should not be inferred from the number alone: integrate texture, luster, plaque and clinical context."
    )
    else -> tr(
        lang,
        "En códigos 5–6 la actividad también se valora por separado: dentina blanda/correosa favorece actividad; dentina brillante y dura favorece inactividad/arresto. Usa sondaje suave y juicio clínico.",
        "For codes 5–6, activity is also assessed separately: soft/leathery dentin supports activity; shiny hard dentin supports inactivity/arrest. Use gentle probing and clinical judgment."
    )
}

@Composable
fun IcdasScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var selectedTooth by remember { mutableStateOf(16) }
    var primary by remember { mutableStateOf(false) }
    var selectedSurface by remember { mutableStateOf(Surface.OCCLUSAL) }
    var guideCode by remember { mutableStateOf(0) }

    val shown = if (primary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
    if (selectedTooth !in shown) selectedTooth = shown.first()

    val eligibleSurfaces = IcdasSurfaceV48.eligibleSurfaces(selectedTooth)
    if (selectedSurface !in eligibleSurfaces) selectedSurface = eligibleSurfaces.first()

    val rawCodes = session.icdasSurfaces[selectedTooth].orEmpty()
    val surfaceCodes = IcdasSurfaceV48.sanitize(selectedTooth, rawCodes)
    val currentCode = surfaceCodes[selectedSurface] ?: 0
    val toothCode = IcdasSurfaceV48.highestCode(selectedTooth, surfaceCodes)
    val record = session.teeth[selectedTooth] ?: ToothRecord()
    val missing = record.status in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER)
    val selectedGuide = icdasTeachingV48.first { it.code == guideCode }

    val dentitionCodes = session.icdasSurfaces
        .filterKeys { it in shown }
        .flatMap { (tooth, codes) -> IcdasSurfaceV48.sanitize(tooth, codes).values }
    val recordedSurfaces = dentitionCodes.size
    val affectedSurfaces = dentitionCodes.count { it > 0 }
    val maxDentitionCode = dentitionCodes.maxOrNull() ?: 0

    fun saveCodes(codes: Map<Surface, Int>) {
        if (missing) return
        val clean = IcdasSurfaceV48.sanitize(selectedTooth, codes)
        val all = session.icdasSurfaces.toMutableMap().apply {
            if (clean.isEmpty()) remove(selectedTooth) else put(selectedTooth, clean)
        }
        val max = IcdasSurfaceV48.highestCode(selectedTooth, clean)
        val liveRecord = session.teeth[selectedTooth] ?: ToothRecord()
        onSessionChanged(
            session.copy(
                icdasSurfaces = all,
                teeth = session.teeth + (selectedTooth to liveRecord.copy(icdas = max))
            )
        )
    }

    fun setCode(code: Int) {
        saveCodes(IcdasSurfaceV48.setCode(selectedTooth, surfaceCodes, selectedSurface, code))
    }

    fun setAll(code: Int) {
        saveCodes(IcdasSurfaceV48.setAll(selectedTooth, code))
    }

    fun clearTooth() {
        val all = session.icdasSurfaces - selectedTooth
        val liveRecord = session.teeth[selectedTooth] ?: ToothRecord()
        onSessionChanged(
            session.copy(
                icdasSurfaces = all,
                teeth = session.teeth + (selectedTooth to liveRecord.copy(icdas = 0))
            )
        )
    }

    ResponsiveScreenV17(
        "ICDAS 0–6",
        tr(
            lang,
            "Registra la severidad de caries por superficie. Cada cara conserva su código y el resumen del diente muestra el código más alto, sin modificar automáticamente CPOD/ceod.",
            "Record caries severity by surface. Each surface keeps its own code and the tooth summary shows the highest code, without automatically changing DMFT/deft."
        ),
        onBack
    ) { profile ->
        PracticeSaveControlsV48(lang, "icdas_v48")

        ResponsiveSectionV17(tr(lang, "1 · Dentición", "1 · Dentition")) {
            AdaptiveGridV17(
                2,
                if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
            ) { index ->
                val targetPrimary = index == 1
                FilterChip(
                    selected = primary == targetPrimary,
                    onClick = {
                        primary = targetPrimary
                        val target = if (targetPrimary) ClinicalContent.primaryTeeth else ClinicalContent.permanentTeeth
                        selectedTooth = target.first()
                        selectedSurface = if (IcdasSurfaceV48.eligibleSurfaces(target.first()).contains(Surface.OCCLUSAL)) {
                            Surface.OCCLUSAL
                        } else {
                            Surface.VESTIBULAR
                        }
                        guideCode = session.icdasSurfaces[target.first()]?.get(selectedSurface) ?: 0
                    },
                    label = {
                        Text(
                            if (targetPrimary) tr(lang, "Temporal", "Primary")
                            else tr(lang, "Permanente", "Permanent")
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17(
            tr(lang, "2 · Diente y superficie", "2 · Tooth and surface"),
            tr(
                lang,
                "Anteriores: V, L/P, M y D. Posteriores: se añade O. Una cara oclusal no se habilita en incisivos/caninos.",
                "Anterior teeth: B, L/P, M and D. Posterior teeth add O. An occlusal surface is not enabled for incisors/canines."
            )
        ) {
            DentalArchSelector(
                shown,
                selectedTooth,
                { tooth ->
                    selectedTooth = tooth
                    val allowed = IcdasSurfaceV48.eligibleSurfaces(tooth)
                    if (selectedSurface !in allowed) selectedSurface = allowed.first()
                    guideCode = session.icdasSurfaces[tooth]?.get(selectedSurface) ?: 0
                }
            ) { tooth ->
                IcdasSurfaceV48.sanitize(tooth, session.icdasSurfaces[tooth].orEmpty()).values.any { it > 0 }
            }

            if (missing) {
                NoticeCard(
                    tr(
                        lang,
                        "Este diente está registrado como ausente en el expediente educativo y no puede recibir un código ICDAS hasta cambiar su condición en el odontograma.",
                        "This tooth is recorded as missing in the educational record and cannot receive an ICDAS code until its condition is changed in the odontogram."
                    )
                )
            } else {
                DentalSurfaceDiagram(
                    centerEnabled = Surface.OCCLUSAL in eligibleSurfaces,
                    surfaceColor = { surface ->
                        val code = surfaceCodes[surface] ?: 0
                        when {
                            surface == selectedSurface -> MaterialTheme.colorScheme.primaryContainer
                            code >= 5 -> MaterialTheme.colorScheme.errorContainer
                            code > 0 -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    },
                    onSurfaceTap = { surface ->
                        if (surface in eligibleSurfaces) {
                            selectedSurface = surface
                            guideCode = surfaceCodes[surface] ?: 0
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "${surfaceName(selectedSurface, lang)} · ICDAS $currentCode",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    tr(
                        lang,
                        "Código del diente = $toothCode (mayor código entre sus superficies elegibles)",
                        "Tooth code = $toothCode (highest code among eligible surfaces)"
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        ResponsiveSectionV17(
            tr(lang, "3 · Atlas fotográfico clínico ICDAS 0–6", "3 · ICDAS 0–6 clinical photo atlas")
        ) {
            Image(
                painter = painterResource(R.drawable.icdas_codes_photo),
                contentDescription = tr(
                    lang,
                    "Fotografías clínicas A a G correspondientes a ICDAS 0 a 6",
                    "Clinical photographs A through G corresponding to ICDAS 0 through 6"
                ),
                modifier = Modifier.fillMaxWidth().heightIn(min = 220.dp, max = 480.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                tr(
                    lang,
                    "Mapa de la figura: A=0 · B=1 · C=2 · D=3 · E=4 · F=5 · G=6.",
                    "Figure map: A=0 · B=1 · C=2 · D=3 · E=4 · F=5 · G=6."
                ),
                fontWeight = FontWeight.Black
            )
            Text(
                "Gugnani N, Pandit IK, Srivastava N, Gupta M, Sharma M. International Caries Detection and Assessment System (ICDAS): A New Concept. Int J Clin Pediatr Dent. 2011;4(2):93–100. Fig. 1A–G. CC BY 3.0. DOI: 10.5005/jp-journals-10005-1089",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Fuente: https://pmc.ncbi.nlm.nih.gov/articles/PMC5030492/",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        ResponsiveSectionV17(tr(lang, "4 · Selecciona el código que quieres estudiar", "4 · Select the code to study")) {
            AdaptiveGridV17(
                7,
                when {
                    profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT -> 2
                    profile.width == ScreenWidthV17.MEDIUM -> 4
                    else -> 7
                }
            ) { index ->
                FilterChip(
                    selected = guideCode == index,
                    onClick = { guideCode = index },
                    label = { Text("ICDAS $index") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17(
            tr(lang, "Código ${selectedGuide.code} · ${selectedGuide.stageEs}", "Code ${selectedGuide.code} · ${selectedGuide.stageEn}")
        ) {
            Text(
                if (lang == "en") selectedGuide.seeEn else selectedGuide.seeEs,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                tr(lang, "✍️ Cómo puede escribirse", "✍️ How it may be charted"),
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Text(if (lang == "en") selectedGuide.writeEn else selectedGuide.writeEs)
            Text(
                tr(lang, "⚠️ Error común", "⚠️ Common mistake"),
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.error
            )
            Text(if (lang == "en") selectedGuide.errorEn else selectedGuide.errorEs)

            OutlinedButton(
                onClick = { setCode(selectedGuide.code) },
                enabled = !missing,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    tr(
                        lang,
                        "Asignar ICDAS ${selectedGuide.code} a ${surfaceName(selectedSurface, lang)}",
                        "Assign ICDAS ${selectedGuide.code} to ${surfaceName(selectedSurface, lang)}"
                    )
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "5 · Severidad y actividad no son lo mismo", "5 · Severity and activity are not the same")) {
            Text(icdasActivityTextV48(guideCode, lang))
            NoticeCard(
                tr(
                    lang,
                    "La app no deduce automáticamente “activa/inactiva” a partir del número ICDAS. Esa valoración necesita brillo, textura, localización/placa y, en dentina, consistencia clínica.",
                    "The app does not automatically infer “active/inactive” from the ICDAS number. Activity assessment also requires luster, texture, location/plaque and, for dentin, clinical consistency."
                )
            )
        }

        ResponsiveSectionV17(tr(lang, "6 · Acciones del diente", "6 · Tooth actions")) {
            AdaptiveGridV17(
                2,
                if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
            ) { index ->
                OutlinedButton(
                    onClick = {
                        if (index == 0) setAll(guideCode) else clearTooth()
                    },
                    enabled = !missing,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (index == 0) {
                            tr(
                                lang,
                                "Mismo código ${guideCode} en todas las caras elegibles",
                                "Code ${guideCode} on all eligible surfaces"
                            )
                        } else {
                            tr(lang, "Limpiar códigos del OD $selectedTooth", "Clear codes on tooth $selectedTooth")
                        }
                    )
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "7 · Resumen de práctica", "7 · Practice summary")) {
            Text(
                tr(
                    lang,
                    "Superficies registradas en esta dentición: $recordedSurfaces",
                    "Recorded surfaces in this dentition: $recordedSurfaces"
                )
            )
            Text(
                tr(
                    lang,
                    "Superficies con ICDAS > 0: $affectedSurfaces",
                    "Surfaces with ICDAS > 0: $affectedSurfaces"
                )
            )
            Text(
                tr(
                    lang,
                    "Código máximo registrado: $maxDentitionCode",
                    "Highest recorded code: $maxDentitionCode"
                ),
                fontWeight = FontWeight.Bold
            )
            Card(
                onClick = {
                    TeachingStateV40.moduleSummaries["icdas"] =
                        "ICDAS · superficies registradas $recordedSurfaces · >0 $affectedSurfaces · máximo $maxDentitionCode"
                    TeachingStateV40.savedPracticeSections["icdas_v48"] = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    tr(lang, "Guardar resumen ICDAS", "Save ICDAS summary"),
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    fontWeight = FontWeight.Black
                )
            }
        }

        NoticeCard(
            tr(
                lang,
                "ICDAS debe aplicarse sobre superficies limpias y con secado apropiado. Usa sonda de punta roma sólo de forma suave cuando sea necesario; un explorador afilado puede dañar lesiones iniciales.",
                "ICDAS should be applied to clean surfaces with appropriate drying. Use a ball-ended probe gently only when needed; a sharp explorer can damage initial lesions."
            )
        )

        PracticeSaveControlsV48(lang, "icdas_v48")
    }
}

@Composable
fun CpodScreen(lang: String, session: EducationalSession, onBack: () -> Unit) {
    val permanent=ClinicalEngines.cpod(session.teeth,false)
    val primary=ClinicalEngines.cpod(session.teeth,true)
    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"CPOD / ceod","DMFT / dmft"),onBack,tr(lang,"La unidad es el diente completo, no la superficie.","The unit is the whole tooth, not the surface.")) }
        item { SectionCard(tr(lang,"Dentición permanente · CPOD","Permanent dentition · DMFT")) {
            Text("C = ${permanent.carious}   P = ${permanent.missing}   O = ${permanent.filled}",style=MaterialTheme.typography.titleLarge)
            Text("CPOD = ${permanent.total}",fontWeight=FontWeight.Bold,style=MaterialTheme.typography.headlineSmall)
            Text(ClinicalEngines.cpodInterpretation(permanent.total,lang))
        } }
        item { SectionCard(tr(lang,"Dentición temporal · ceod","Primary dentition · dmft")) {
            Text("c = ${primary.carious}   e = ${primary.missing}   o = ${primary.filled}",style=MaterialTheme.typography.titleLarge)
            Text("ceod = ${primary.total}",fontWeight=FontWeight.Bold,style=MaterialTheme.typography.headlineSmall)
            Text(ClinicalEngines.cpodInterpretation(primary.total,lang))
        } }
        item { NoticeCard(tr(lang,"Si un diente tiene una restauración y además caries activa en otra superficie, la caries tiene prioridad para el conteo. Las ausencias por causas distintas de caries no suman como P.","If a tooth has a restoration and active caries on another surface, decay takes priority for the count. Missing teeth for reasons other than caries do not count as M.")) }
    }
}
