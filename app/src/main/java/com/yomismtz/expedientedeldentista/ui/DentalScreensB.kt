package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.clinical.ClinicalContent
import com.yomismtz.expedientedeldentista.clinical.ClinicalEngines
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.OlearyIndexV48
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.ToothStatus

@Composable
fun OlearyScreen(
    lang: String,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit,
    onBack: () -> Unit
) {
    var primary by remember { mutableStateOf(false) }
    var includeThirdMolars by remember { mutableStateOf(false) }

    val shown = if (primary) {
        ClinicalContent.primaryTeeth
    } else {
        OlearyIndexV48.permanentTeeth(includeThirdMolars)
    }
    val shownSet = shown.toSet()

    var selectedTooth by remember { mutableStateOf(shown.first()) }
    if (selectedTooth !in shown) selectedTooth = shown.first()

    val surfaces = OlearyIndexV48.surfaces
    val initialized = if (primary) {
        session.olearyPrimaryInitialized
    } else {
        session.olearyPermanentInitialized
    }

    val odontogramPresent = shownSet.filter { tooth ->
        session.teeth[tooth]?.status !in setOf(ToothStatus.MISSING_CARIES, ToothStatus.MISSING_OTHER)
    }.toSet()

    val present = if (initialized) {
        session.olearyPresentTeeth.intersect(shownSet)
    } else {
        odontogramPresent
    }

    val otherDentitionPresent = session.olearyPresentTeeth - shownSet
    val selectedSurfaces = session.oleary[selectedTooth].orEmpty().intersect(surfaces)
    val result = OlearyIndexV48.calculate(
        teethInScope = shown,
        presentTeeth = present,
        plaqueByTooth = session.oleary
    )

    fun saveState(
        currentDentitionPresent: Set<Int>,
        map: Map<Int, Set<Surface>> = session.oleary
    ) {
        onSessionChanged(
            session.copy(
                oleary = map,
                olearyPresentTeeth = otherDentitionPresent + currentDentitionPresent,
                olearyPermanentInitialized = session.olearyPermanentInitialized || !primary,
                olearyPrimaryInitialized = session.olearyPrimaryInitialized || primary
            )
        )
    }

    fun setMarks(newSet: Set<Surface>) {
        val cleanSet = newSet.intersect(surfaces)
        val map = session.oleary.toMutableMap().apply {
            if (cleanSet.isEmpty()) remove(selectedTooth) else put(selectedTooth, cleanSet)
        }
        saveState(present + selectedTooth, map)
    }

    fun toggleSurface(surface: Surface) {
        if (surface !in surfaces || selectedTooth !in present) return
        val updated = selectedSurfaces.toMutableSet()
        if (!updated.add(surface)) updated.remove(surface)
        setMarks(updated)
    }

    ResponsiveScreenV17(
        "O’Leary",
        tr(
            lang,
            "Registro binario de placa en cuatro superficies por diente evaluable: vestibular, lingual/palatina, mesial y distal. La superficie oclusal no participa.",
            "Binary plaque record on four surfaces per evaluable tooth: buccal, lingual/palatal, mesial and distal. Occlusal surfaces are not included."
        ),
        onBack
    ) { profile ->
        PracticeSaveControlsV48(lang, "oleary_v48")

        ResponsiveSectionV17(tr(lang, "1 · Dentición", "1 · Dentition")) {
            AdaptiveGridV17(
                2,
                if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
            ) { index ->
                val targetPrimary = index == 1
                FilterChip(
                    selected = primary == targetPrimary,
                    onClick = { primary = targetPrimary },
                    label = {
                        Text(
                            if (targetPrimary) {
                                tr(lang, "Temporal", "Primary")
                            } else {
                                tr(lang, "Permanente", "Permanent")
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (!primary) {
            ResponsiveSectionV17(tr(lang, "2 · Alcance permanente", "2 · Permanent scope")) {
                Text(
                    tr(
                        lang,
                        "Elige el alcance que exige tu protocolo. La opción de 28 excluye terceros molares; la de 32 los incluye.",
                        "Choose the scope required by your protocol. The 28-tooth option excludes third molars; the 32-tooth option includes them."
                    )
                )
                AdaptiveGridV17(
                    2,
                    if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
                ) { index ->
                    val include = index == 1
                    FilterChip(
                        selected = includeThirdMolars == include,
                        onClick = { includeThirdMolars = include },
                        label = {
                            Text(
                                if (include) {
                                    tr(lang, "32 · incluir terceros molares", "32 · include third molars")
                                } else {
                                    tr(lang, "28 · excluir terceros molares", "28 · exclude third molars")
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        ResponsiveSectionV17(
            tr(lang, "3 · Diente evaluable", "3 · Evaluable tooth"),
            tr(
                lang,
                "El denominador se calcula únicamente con dientes marcados como presentes/evaluables.",
                "The denominator uses only teeth marked present/evaluable."
            )
        ) {
            DentalArchSelector(
                shown,
                selectedTooth,
                { selectedTooth = it }
            ) { tooth ->
                session.oleary[tooth].orEmpty().any { it in surfaces }
            }

            AdaptiveGridV17(
                2,
                if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
            ) { index ->
                if (index == 0) {
                    FilterChip(
                        selected = selectedTooth in present,
                        onClick = { saveState(present + selectedTooth) },
                        label = { Text(tr(lang, "Presente / evaluable", "Present / evaluable")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    FilterChip(
                        selected = selectedTooth !in present,
                        onClick = {
                            val newPresent = present - selectedTooth
                            val map = session.oleary.toMutableMap().apply { remove(selectedTooth) }
                            saveState(newPresent, map)
                        },
                        label = { Text(tr(lang, "Ausente / excluir", "Missing / exclude")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        ResponsiveSectionV17(
            tr(lang, "4 · Superficies OD $selectedTooth", "4 · Surfaces tooth $selectedTooth")
        ) {
            if (selectedTooth !in present) {
                NoticeCard(
                    tr(
                        lang,
                        "Este diente está excluido del denominador. Márcalo como presente para registrar placa.",
                        "This tooth is excluded from the denominator. Mark it present to record plaque."
                    )
                )
            } else {
                val surfaceList = listOf(
                    Surface.VESTIBULAR,
                    Surface.LINGUAL_PALATAL,
                    Surface.MESIAL,
                    Surface.DISTAL
                )
                AdaptiveGridV17(
                    surfaceList.size,
                    if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 2 else 4
                ) { index ->
                    val surface = surfaceList[index]
                    val label = when (surface) {
                        Surface.VESTIBULAR -> tr(lang, "V · Vestibular", "B · Buccal")
                        Surface.LINGUAL_PALATAL -> tr(lang, "L/P · Lingual/palatina", "L/P · Lingual/palatal")
                        Surface.MESIAL -> tr(lang, "M · Mesial", "M · Mesial")
                        Surface.DISTAL -> tr(lang, "D · Distal", "D · Distal")
                        Surface.OCCLUSAL -> ""
                    }
                    FilterChip(
                        selected = surface in selectedSurfaces,
                        onClick = { toggleSurface(surface) },
                        label = { Text(label) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                DentalSurfaceDiagram(
                    centerEnabled = false,
                    surfaceColor = { surface ->
                        if (surface in selectedSurfaces) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    },
                    onSurfaceTap = { toggleSurface(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                AdaptiveGridV17(
                    2,
                    if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2
                ) { index ->
                    OutlinedButton(
                        onClick = {
                            if (index == 0) setMarks(surfaces) else setMarks(emptySet())
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (index == 0) {
                                tr(lang, "Marcar las 4 caras", "Mark all 4 surfaces")
                            } else {
                                tr(lang, "Limpiar OD $selectedTooth", "Clear tooth $selectedTooth")
                            }
                        )
                    }
                }

                Text(
                    tr(
                        lang,
                        "Superficies con placa en este OD: ${selectedSurfaces.size}/4. Cada cara es independiente; tocar una no borra las demás.",
                        "Plaque-positive surfaces on this tooth: ${selectedSurfaces.size}/4. Each surface is independent; tapping one does not clear the others."
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "5 · Cálculo automático", "5 · Automatic calculation")) {
            Text(
                "${result.plaqueSurfaces} / ${result.examinedSurfaces} × 100",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "${result.percentage} %",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                tr(
                    lang,
                    "Dientes evaluables: ${result.examinedSurfaces / 4}. Superficies con placa: ${result.plaqueSurfaces}. Superficies examinadas: ${result.examinedSurfaces}.",
                    "Evaluable teeth: ${result.examinedSurfaces / 4}. Plaque-positive surfaces: ${result.plaqueSurfaces}. Examined surfaces: ${result.examinedSurfaces}."
                )
            )
            Text(
                tr(
                    lang,
                    "Un porcentaje menor representa menos superficies con placa. La app no impone un punto de corte universal; usa el objetivo definido por tu protocolo docente o clínico.",
                    "A lower percentage represents fewer plaque-positive surfaces. The app does not impose a universal cutoff; use the goal defined by your teaching or clinical protocol."
                ),
                style = MaterialTheme.typography.bodySmall
            )

            Card(
                onClick = {
                    val scope = if (primary) {
                        tr(lang, "dentición temporal", "primary dentition")
                    } else if (includeThirdMolars) {
                        tr(lang, "permanente 32 dientes", "permanent 32 teeth")
                    } else {
                        tr(lang, "permanente 28 dientes", "permanent 28 teeth")
                    }
                    TeachingStateV40.moduleSummaries["oleary"] =
                        "O’Leary ${result.percentage}% · ${result.plaqueSurfaces}/${result.examinedSurfaces} superficies · $scope"
                    TeachingStateV40.savedPracticeSections["oleary_v48"] = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    tr(lang, "Guardar resultado de O’Leary", "Save O’Leary result"),
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    fontWeight = FontWeight.Black
                )
            }
        }

        NoticeCard(
            tr(
                lang,
                "Fórmula: superficies con placa ÷ superficies examinadas × 100. Se valoran cuatro superficies por diente evaluable: vestibular, lingual/palatina, mesial y distal; no oclusal.",
                "Formula: plaque-positive surfaces ÷ examined surfaces × 100. Four surfaces are assessed per evaluable tooth: buccal, lingual/palatal, mesial and distal; not occlusal."
            )
        )

        PracticeSaveControlsV48(lang, "oleary_v48")
    }
}

@Composable
fun IpcScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    val sextants = listOf("17–14", "13–23", "24–27", "47–44", "43–33", "34–37")
    val codes = listOf("0", "1", "2", "3", "4", "X")
    val highest = ClinicalEngines.ipcHighest(session.ipcCodes)
    val summary = session.ipcCodes.take(6).mapIndexed { index, code -> "S${index + 1}=$code" }.joinToString(" · ")
    var selected by remember { mutableStateOf(0) }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "IPC / CPI",
                onBack,
                tr(lang,
                    "El IPC se registra por sextantes. Toca uno de los seis sectores, examina sus dientes y conserva el peor hallazgo de ese sextante.",
                    "CPI is recorded by sextants. Tap one of the six areas, examine its teeth and retain the worst finding in that sextant.")
            )
        }
        item {
            SectionCard(tr(lang, "Guía visual IPC", "CPI visual guide")) {
                Image(
                    painter = painterResource(R.drawable.ipc_visual_guide),
                    contentDescription = tr(lang, "Infografías de sextantes, códigos y sonda OMS del IPC", "CPI sextant, code and WHO probe infographics"),
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Text(tr(lang,
                    "La lámina resume: mapa de sextantes, códigos 0–4/X, sonda OMS/CPI y secuencia de registro. Debajo puedes practicar el índice de forma interactiva.",
                    "The guide summarizes the sextant map, codes 0–4/X, WHO/CPI probe and recording sequence. Practice interactively below."))
            }
        }
        item {
            SectionCard(tr(lang, "Mapa interactivo por sextantes", "Interactive sextant map")) {
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    (0..2).forEach { i -> SextantTile(i,sextants[i],session.ipcCodes.getOrElse(i){"0"},selected==i){selected=i} }
                }
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    (5 downTo 3).forEach { i -> SextantTile(i,sextants[i],session.ipcCodes.getOrElse(i){"0"},selected==i){selected=i} }
                }
            }
        }
        item {
            SectionCard("${tr(lang,"Sextante","Sextant")} ${selected+1} · ${sextants[selected]}") {
                Text(tr(lang,"Selecciona el código que corresponda al peor hallazgo observado en el sextante.","Select the code corresponding to the worst finding observed in the sextant."))
                codes.chunked(3).forEach { row ->
                    Row(horizontalArrangement=Arrangement.spacedBy(6.dp),modifier=Modifier.fillMaxWidth()) {
                        row.forEach { code ->
                            FilterChip(
                                session.ipcCodes.getOrElse(selected){"0"} == code,
                                {
                                    val list = session.ipcCodes.toMutableList()
                                    while (list.size < 6) list.add("0")
                                    list[selected] = code
                                    onSessionChanged(session.copy(ipcCodes=list))
                                },
                                { Text(code) },
                                modifier=Modifier.weight(1f)
                            )
                        }
                    }
                }
                Text(ClinicalEngines.ipcInterpretation(session.ipcCodes.getOrElse(selected){"0"},lang),fontWeight=FontWeight.SemiBold)
            }
        }
        item {
            SectionCard(tr(lang,"Cómo reconocer los códigos","How to recognize codes")) {
                Text("0 · ${tr(lang,"sano: sin los hallazgos codificados del índice","healthy: no indexed findings")}")
                Text("1 · ${tr(lang,"sangrado después del sondaje","bleeding after probing")}")
                Text("2 · ${tr(lang,"cálculo y/o factores retentivos de placa","calculus and/or plaque-retentive factors")}")
                Text("3 · ${tr(lang,"bolsa de 4–5 mm","4–5 mm pocket")}")
                Text("4 · ${tr(lang,"bolsa de 6 mm o más","pocket 6 mm or greater")}")
                Text("X · ${tr(lang,"sextante excluido según los criterios del método","sextant excluded according to method criteria")}")
            }
        }
        item {
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(tr(lang,"Resumen para practicar la redacción","Summary for writing practice"),fontWeight=FontWeight.Bold)
                    Text(summary, style=MaterialTheme.typography.titleMedium, fontWeight=FontWeight.Bold)
                    Text("${tr(lang,"Código más alto","Highest code")}: $highest")
                    Text(ClinicalEngines.ipcInterpretation(highest,lang))
                    Text(tr(lang,
                        "Ejemplo: “IPC: $summary”. Después agrega en palabras los hallazgos relevantes y complétalos con la valoración periodontal cuando corresponda.",
                        "Example: “CPI: $summary”. Then describe relevant findings and complete them with periodontal assessment as appropriate."))
                }
            }
        }
    }
}

@Composable
private fun RowScope.SextantTile(index:Int,label:String,code:String,active:Boolean,onClick:()->Unit) {
    Card(modifier=Modifier.weight(1f),onClick=onClick,colors=CardDefaults.cardColors(containerColor=if(active)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxWidth().padding(9.dp),horizontalAlignment=Alignment.CenterHorizontally) {
            Text("S${index + 1}", fontWeight=FontWeight.Bold, color=MaterialTheme.colorScheme.primary)
            Text(label,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.bodySmall)
            Text(code,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
        }
    }
}

@Composable
fun IhosScreen(lang: String, session: EducationalSession, onSessionChanged: (EducationalSession) -> Unit, onBack: () -> Unit) {
    val teeth = listOf(16,11,26,36,31,46)
    val debrisAverage = teeth.map { session.ihosDebris[it] ?: 0 }.average()
    val calculusAverage = teeth.map { session.ihosCalculus[it] ?: 0 }.average()
    val result = ClinicalEngines.ihos(session)
    var selected by remember { mutableStateOf(16) }

    LazyColumn(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                "IHOS / OHI-S",
                onBack,
                tr(lang,
                    "Usa seis superficies índice: 16V, 11V, 26V, 36L, 31V y 46L. Toca cada diente, asigna detritos y cálculo de 0 a 3 y observa el cálculo automático.",
                    "Use six index surfaces: 16B, 11B, 26B, 36L, 31B and 46L. Tap each tooth, assign debris and calculus from 0 to 3 and view the automatic calculation.")
            )
        }
        item {
            SectionCard(tr(lang,"Guía visual IHOS","OHI-S visual guide")) {
                Image(
                    painter = painterResource(R.drawable.ihos_visual_guide),
                    contentDescription = tr(lang,"Dientes índice, escalas de detritos y cálculo y guía de cálculo del IHOS","Index teeth, debris/calculus scales and OHI-S calculation guide"),
                    modifier=Modifier.fillMaxWidth(),
                    contentScale=ContentScale.FillWidth
                )
                Text(tr(lang,
                    "La guía reúne los seis dientes índice, la escala de detritos 0–3, la escala de cálculo 0–3 y la fórmula. La captura de práctica sigue siendo interactiva debajo.",
                    "The guide combines the six index teeth, debris scale 0–3, calculus scale 0–3 and formula. Practice entry remains interactive below."))
            }
        }
        item {
            SectionCard(tr(lang,"Dientes y superficies índice","Index teeth and surfaces")) {
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    teeth.take(3).forEach { tooth -> IhosToothChip(tooth,selected==tooth,tooth in listOf(36,46),{selected=tooth},Modifier.weight(1f)) }
                }
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    teeth.drop(3).forEach { tooth -> IhosToothChip(tooth,selected==tooth,tooth in listOf(36,46),{selected=tooth},Modifier.weight(1f)) }
                }
            }
        }
        item {
            SectionCard("OD $selected · ${if(selected in listOf(36,46)) tr(lang,"Lingual","Lingual") else tr(lang,"Vestibular","Buccal")}") {
                Text(tr(lang,"Detritos / placa blanda","Debris / soft deposits"),fontWeight=FontWeight.Bold)
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    (0..3).forEach { c ->
                        FilterChip(
                            (session.ihosDebris[selected] ?: 0) == c,
                            { onSessionChanged(session.copy(ihosDebris=session.ihosDebris+(selected to c))) },
                            { Text(c.toString()) },
                            modifier=Modifier.weight(1f)
                        )
                    }
                }
                Text(tr(lang,"Cálculo dental","Dental calculus"),fontWeight=FontWeight.Bold)
                Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()) {
                    (0..3).forEach { c ->
                        FilterChip(
                            (session.ihosCalculus[selected] ?: 0) == c,
                            { onSessionChanged(session.copy(ihosCalculus=session.ihosCalculus+(selected to c))) },
                            { Text(c.toString()) },
                            modifier=Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        item {
            SectionCard(tr(lang,"Guía rápida de códigos","Quick code guide")) {
                Text(tr(lang,"Detritos: 0 ninguno · 1 <1/3 · 2 de 1/3 a 2/3 · 3 >2/3 de la superficie.","Debris: 0 none · 1 <1/3 · 2 from 1/3 to 2/3 · 3 >2/3 of surface."))
                Text(tr(lang,"Cálculo: 0 ninguno · 1 supragingival <1/3 · 2 de 1/3 a 2/3 y/o presencia ligera subgingival · 3 >2/3 y/o banda subgingival importante.","Calculus: 0 none · 1 supragingival <1/3 · 2 from 1/3 to 2/3 and/or light subgingival presence · 3 >2/3 and/or important subgingival band."))
            }
        }
        item {
            Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                    Text(tr(lang,"Cálculo automático","Automatic calculation"),fontWeight=FontWeight.Bold)
                    Text("ID-S = ${"%.2f".format(debrisAverage)}")
                    Text("IC-S = ${"%.2f".format(calculusAverage)}")
                    Text("IHOS = ${"%.2f".format(result)}",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
                    Text(ClinicalEngines.ihosInterpretation(result,lang))
                    Text(tr(lang,"Referencia didáctica: 0.0–1.2 buena · 1.3–3.0 regular · 3.1–6.0 deficiente.","Teaching reference: 0.0–1.2 good · 1.3–3.0 fair · 3.1–6.0 poor."))
                    Text(tr(lang,
                        "Ejemplo de redacción: “IHOS ${"%.2f".format(result)} (ID-S ${"%.2f".format(debrisAverage)} + IC-S ${"%.2f".format(calculusAverage)}), ${ClinicalEngines.ihosInterpretation(result,lang)}”.",
                        "Writing example: “OHI-S ${"%.2f".format(result)} (DI-S ${"%.2f".format(debrisAverage)} + CI-S ${"%.2f".format(calculusAverage)}), ${ClinicalEngines.ihosInterpretation(result,lang)}”."))
                }
            }
        }
    }
}

@Composable
private fun IhosToothChip(tooth:Int,selected:Boolean,lingual:Boolean,onClick:()->Unit,modifier:Modifier=Modifier) {
    Card(modifier=modifier,onClick=onClick,colors=CardDefaults.cardColors(containerColor=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxWidth().padding(8.dp),horizontalAlignment=Alignment.CenterHorizontally) {
            Text("🦷",textAlign=TextAlign.Center)
            Text("$tooth ${if(lingual)"L" else "V"}",fontWeight=FontWeight.Bold)
        }
    }
}
