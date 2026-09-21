package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private data class TeachingFieldV39(
    val titleEs: String,
    val titleEn: String,
    val whyEs: String,
    val whyEn: String,
    val exampleEs: String,
    val exampleEn: String,
    val errorEs: String,
    val errorEn: String
)

private val identificationTeachingV39 = listOf(
    TeachingFieldV39(
        "Identificación básica",
        "Basic identification",
        "Explica al estudiante qué datos suelen identificar al paciente dentro del formato institucional. En esta app no se capturan datos reales.",
        "Explains which items commonly identify the patient in an institutional form. This app does not collect real patient data.",
        "Ejemplo ficticio: “Nombre completo según documento; edad calculada con fecha de nacimiento; sexo/género según el formato”.",
        "Fictional example: “Full name as documented; age calculated from date of birth; sex/gender according to the form”.",
        "Error común: usar apodos, edades aproximadas o completar información que el paciente no proporcionó.",
        "Common error: using nicknames, approximate ages or filling information the patient did not provide."
    ),
    TeachingFieldV39(
        "Clasificación ASA",
        "ASA classification",
        "Se coloca después de integrar antecedentes, condición sistémica actual y valoración clínica; no se decide por una sola enfermedad aislada.",
        "It is assigned after integrating medical history, current systemic condition and clinical assessment; it is not decided from one isolated diagnosis.",
        "Ejemplo didáctico: persona sana → considerar ASA I. Enfermedad sistémica leve y controlada → revisar criterios de ASA II.",
        "Teaching example: healthy person → consider ASA I. Mild, controlled systemic disease → review ASA II criteria.",
        "Error común: convertir automáticamente “diabetes”, “hipertensión” o “embarazo” en una clase ASA sin valorar control, gravedad y contexto.",
        "Common error: automatically converting “diabetes”, “hypertension” or “pregnancy” into an ASA class without assessing control, severity and context."
    ),
    TeachingFieldV39(
        "Grupo sanguíneo",
        "Blood group",
        "Se registra sólo si el paciente lo conoce o existe documentación. No se infiere.",
        "Record only if the patient knows it or documentation exists. Never infer it.",
        "Ejemplo: “O positivo, referido por el paciente; sin comprobante disponible” o “desconocido”.",
        "Example: “O positive, reported by patient; no document available” or “unknown”.",
        "Error común: asumir grupo/Rh por familiares, apariencia o antecedentes.",
        "Common error: assuming blood type/Rh from relatives, appearance or history."
    ),
    TeachingFieldV39(
        "Alergias",
        "Allergies",
        "Enseña a identificar sustancia, tipo de reacción, fecha aproximada y atención requerida. Debe distinguirse alergia de intolerancia o efecto adverso.",
        "Teaches how to identify the substance, reaction type, approximate date and required care. Allergy must be distinguished from intolerance or adverse effect.",
        "Ejemplos: penicilinas, cefalosporinas, AINE, látex, clorhexidina, eugenol, metacrilatos, níquel, cobalto-cromo, resinas/adhesivos, material de impresión y alimentos.",
        "Examples: penicillins, cephalosporins, NSAIDs, latex, chlorhexidine, eugenol, methacrylates, nickel, cobalt-chromium, resins/adhesives, impression material and foods.",
        "Error común: escribir únicamente “alérgico” sin especificar a qué ni qué reacción presentó.",
        "Common error: writing only “allergic” without the substance or the reaction."
    ),
    TeachingFieldV39(
        "Medicamentos actuales",
        "Current medicines",
        "Se enseña a registrar lo que realmente toma: nombre, concentración/presentación referida, dosis, vía, intervalo y motivo.",
        "Teaches how to record what is actually taken: name, reported strength/formulation, dose, route, interval and reason.",
        "Ejemplo ficticio: “Losartán 50 mg, vía oral, cada 24 h, por hipertensión, referido por el paciente”.",
        "Fictional example: “Losartan 50 mg, oral, every 24 h, for hypertension, reported by patient”.",
        "Error común: convertir esta sección en una receta o sugerir suspender medicamentos sistémicos.",
        "Common error: turning this section into a prescription or suggesting systemic medicines be stopped."
    ),
    TeachingFieldV39(
        "Resumen diagnóstico odontológico",
        "Dental diagnostic summary",
        "La ficha resume lo obtenido en los módulos de exploración. El diagnóstico debe estar sustentado por hallazgos; no se genera sólo por marcar un color o una casilla.",
        "The sheet summarizes findings from examination modules. A diagnosis must be supported by findings; it is not generated solely by checking a box or color.",
        "Ejemplo de estructura: sistémico/ASA · caries/anomalías · oclusión · periodontal · pulpar/periapical · ATM · mucosas/patología · prótesis · quirúrgico.",
        "Example structure: systemic/ASA · caries/anomalies · occlusion · periodontal · pulpal/apical · TMJ · mucosa/pathology · prosthetic · surgical.",
        "Error común: registrar un diagnóstico definitivo sin pruebas suficientes o confundir un signo clínico con el diagnóstico.",
        "Common error: recording a definitive diagnosis without adequate tests or confusing a clinical sign with the diagnosis."
    )
)

@Composable
internal fun IdentificationGuideV39Screen(lang: String, onBack: () -> Unit) {
    var opened by remember { mutableStateOf<Int?>(0) }
    ResponsiveScreenV17(
        tr(lang, "Ficha de identificación · guía", "Identification sheet · guide"),
        tr(lang,
            "Aprende cómo se estructura y cómo se redacta. Esta pantalla no solicita nombre, domicilio, teléfono, número de expediente ni datos identificables.",
            "Learn the structure and wording. This screen does not request name, address, telephone, record number or identifiable information."
        ),
        onBack
    ) { _ ->
        NoticeCard(tr(lang,
            "La app es un simulador educativo y atlas de consulta. Usa ejemplos ficticios; el expediente clínico real se llena en el sistema o formato de la institución correspondiente.",
            "The app is an educational simulator and reference atlas. Use fictional examples; the real clinical record belongs in the institution's own system or form."
        ))
        identificationTeachingV39.forEachIndexed { index, item ->
            val isOpen = opened == index
            Card(
                onClick = { opened = if (isOpen) null else index },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isOpen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .4f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (lang == "en") item.titleEn else item.titleEs, fontWeight = FontWeight.Black)
                    if (isOpen) {
                        Text("📌 ${if (lang == "en") item.whyEn else item.whyEs}")
                        Text("✅ ${if (lang == "en") item.exampleEn else item.exampleEs}")
                        Text("⚠️ ${if (lang == "en") item.errorEn else item.errorEs}")
                    } else {
                        Text(tr(lang, "Toca para ver cómo se llena y un ejemplo", "Tap for how-to guidance and an example"), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

private enum class CambraKindV39 { PROTECTIVE, RISK, DISEASE }
private data class CambraFactorV39(val id: String, val es: String, val en: String, val kind: CambraKindV39, val youngOnly: Boolean = false, val olderOnly: Boolean = false)

private val cambraFactorsV39 = listOf(
    CambraFactorV39("fluoride_toothpaste", "Pasta dental fluorada usada de forma adecuada", "Appropriate fluoride toothpaste use", CambraKindV39.PROTECTIVE),
    CambraFactorV39("fluoride_water", "Exposición a agua fluorada / fuente de fluoruro habitual", "Fluoridated water / regular fluoride source", CambraKindV39.PROTECTIVE),
    CambraFactorV39("professional_fluoride", "Fluoruro profesional según indicación", "Professional fluoride when indicated", CambraKindV39.PROTECTIVE),
    CambraFactorV39("adequate_saliva", "Función salival adecuada", "Adequate salivary function", CambraKindV39.PROTECTIVE),
    CambraFactorV39("caregiver_decay", "Cuidador principal con caries activa reciente", "Primary caregiver with recent active caries", CambraKindV39.RISK, youngOnly = true),
    CambraFactorV39("bottle", "Biberón/nocturno o exposición frecuente a líquidos azucarados", "Bedtime bottle or frequent sugared-liquid exposure", CambraKindV39.RISK, youngOnly = true),
    CambraFactorV39("snacks", "Azúcares o carbohidratos fermentables frecuentes entre comidas", "Frequent fermentable carbohydrate/sugar exposure between meals", CambraKindV39.RISK),
    CambraFactorV39("plaque", "Biopelícula visible / higiene insuficiente", "Visible biofilm / insufficient plaque control", CambraKindV39.RISK),
    CambraFactorV39("roots", "Superficies radiculares expuestas", "Exposed root surfaces", CambraKindV39.RISK, olderOnly = true),
    CambraFactorV39("xerostomia", "Hiposalivación / xerostomía con evidencia clínica o antecedente relevante", "Hyposalivation / xerostomia with clinical evidence or relevant history", CambraKindV39.RISK, olderOnly = true),
    CambraFactorV39("white_spot", "Lesión de caries activa no cavitada / mancha blanca activa", "Active non-cavitated caries lesion / active white spot", CambraKindV39.DISEASE),
    CambraFactorV39("cavity", "Lesión cavitada o caries dentinaria clínica/radiográfica", "Cavitated lesion or clinical/radiographic dentinal caries", CambraKindV39.DISEASE),
    CambraFactorV39("recent_restoration", "Restauración reciente relacionada con caries / nueva actividad", "Recent caries-related restoration / new disease activity", CambraKindV39.DISEASE),
    CambraFactorV39("missing_caries", "Pérdida dental atribuible a caries en el contexto valorado", "Tooth loss attributable to caries in the assessed context", CambraKindV39.DISEASE, olderOnly = true)
)

@Composable
internal fun CambraInteractiveV39Screen(lang: String, onBack: () -> Unit) {
    var young by remember { mutableStateOf(true) }
    val selected = remember { mutableStateListOf<String>() }
    val factors = cambraFactorsV39.filter { if (young) !it.olderOnly else !it.youngOnly }
    val protective = selected.count { id -> cambraFactorsV39.firstOrNull { it.id == id }?.kind == CambraKindV39.PROTECTIVE }
    val risk = selected.count { id -> cambraFactorsV39.firstOrNull { it.id == id }?.kind == CambraKindV39.RISK }
    val disease = selected.count { id -> cambraFactorsV39.firstOrNull { it.id == id }?.kind == CambraKindV39.DISEASE }
    val balance = disease * 3 + risk * 2 - protective
    val hyposalivation = "xerostomia" in selected

    ResponsiveScreenV17(
        "CAMBRA",
        tr(lang, "Simulador educativo basado en el esquema CAMBRA 2021: factores protectores, factores de riesgo e indicadores de enfermedad.", "Educational simulator based on the 2021 CAMBRA structure: protective factors, risk factors and disease indicators."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang,
            "No captures datos reales. Marca factores de un caso ficticio para aprender a interpretar el balance de caries. El nivel final de riesgo requiere juicio clínico.",
            "Do not enter real patient data. Mark factors from a fictional case to learn the caries-balance concept. Final risk level requires clinical judgment."
        ))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = young, onClick = { young = true; selected.clear() }, label = { Text(tr(lang, "0–6 años", "0–6 years")) }, modifier = Modifier.weight(1f))
            FilterChip(selected = !young, onClick = { young = false; selected.clear() }, label = { Text(tr(lang, "≥6 años–adulto", "≥6 years–adult")) }, modifier = Modifier.weight(1f))
        }

        CambraSectionV39(lang, profile, factors.filter { it.kind == CambraKindV39.PROTECTIVE }, selected, "🛡️", tr(lang, "Factores protectores", "Protective factors"))
        CambraSectionV39(lang, profile, factors.filter { it.kind == CambraKindV39.RISK }, selected, "⚠️", tr(lang, "Factores de riesgo", "Risk factors"))
        CambraSectionV39(lang, profile, factors.filter { it.kind == CambraKindV39.DISEASE }, selected, "🦷", tr(lang, "Indicadores de enfermedad", "Disease indicators"))

        ResponsiveSectionV17(tr(lang, "Balance didáctico", "Teaching balance")) {
            Text(tr(lang, "Protectores: $protective · Riesgo: $risk · Indicadores de enfermedad: $disease", "Protective: $protective · Risk: $risk · Disease indicators: $disease"), fontWeight = FontWeight.Black)
            Text(tr(lang, "Puntaje de visualización CAMBRA 123: $balance (−1 por protector, +2 por factor de riesgo, +3 por indicador de enfermedad).", "CAMBRA 123 visualization score: $balance (−1 per protective factor, +2 per risk factor, +3 per disease indicator)."))
            Text(tr(lang,
                when {
                    disease > 0 -> "Lectura educativa: hay indicadores de enfermedad; revisa criterios de riesgo alto."
                    risk == 0 && protective > 0 -> "Lectura educativa: los factores protectores predominan y no marcaste indicadores de enfermedad."
                    else -> "Lectura educativa: existe un balance mixto; compara con los principios de riesgo bajo/moderado/alto antes de concluir."
                },
                when {
                    disease > 0 -> "Teaching interpretation: disease indicators are present; review high-risk criteria."
                    risk == 0 && protective > 0 -> "Teaching interpretation: protective factors predominate and no disease indicators were marked."
                    else -> "Teaching interpretation: the balance is mixed; compare low/moderate/high-risk principles before concluding."
                }
            ))
            if (!young && hyposalivation && (disease > 0 || risk >= 2)) {
                Text(tr(lang, "⚠️ CAMBRA considera hiposalivación combinada con alto riesgo como escenario de riesgo extremo en ≥6 años/adultos.", "⚠️ CAMBRA considers hyposalivation combined with high-risk findings an extreme-risk scenario in ≥6 years/adults."), fontWeight = FontWeight.Bold)
            }
        }

        ResponsiveSectionV17(tr(lang, "Cómo razonar el nivel", "How to reason the level")) {
            Text(tr(lang, "• Bajo: sin indicadores de enfermedad; muy pocos o ningún factor de riesgo y predominio de protección.", "• Low: no disease indicators; very few or no risk factors and protective factors prevail."))
            Text(tr(lang, "• Moderado: no es claramente bajo ni claramente alto/extremo; requiere seguimiento y prevención reforzada.", "• Moderate: not clearly low and not clearly high/extreme; requires follow-up and enhanced prevention."))
            Text(tr(lang, "• Alto: indicadores de enfermedad y/o combinación importante de factores patológicos requieren manejo preventivo intensificado.", "• High: disease indicators and/or a substantial pathological-factor combination require intensified preventive management."))
            Text(tr(lang, if (young) "• Muy alto: el formulario de 0–6 años contempla una categoría muy alta según el conjunto de hallazgos." else "• Extremo: en ≥6 años/adultos, riesgo alto más hiposalivación es el escenario clásico de riesgo extremo.", if (young) "• Very high: the 0–6 form includes a very-high category based on the overall findings." else "• Extreme: in ≥6 years/adults, high risk plus hyposalivation is the classic extreme-risk scenario."))
        }
    }
}

@Composable
private fun CambraSectionV39(lang: String, profile: ScreenProfileV17, factors: List<CambraFactorV39>, selected: MutableList<String>, icon: String, title: String) {
    ResponsiveSectionV17("$icon $title") {
        AdaptiveGridV17(factors.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val item = factors[index]
            FilterChip(
                selected = item.id in selected,
                onClick = { if (item.id in selected) selected.remove(item.id) else selected.add(item.id) },
                label = { Text(if (lang == "en") item.en else item.es) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private enum class LesionVisualV39 { ULCER, WHITE_PLAQUE, RETICULAR, RED_PATCH, PSEUDOMEMBRANE, MUCOCELE, GEOGRAPHIC, FISSURED, PAPILLOMA, PIGMENT, VESICLES, NORMAL }
private data class MucosaLesionV39(val nameEs: String, val nameEn: String, val appearanceEs: String, val appearanceEn: String, val presumptiveEs: String, val presumptiveEn: String, val visual: LesionVisualV39, val cautionEs: String, val cautionEn: String)
private data class MucosaRegionV39(val id: String, val es: String, val en: String, val normalEs: String, val normalEn: String, val lesions: List<MucosaLesionV39>)

private val commonUlcerV39 = MucosaLesionV39("Úlcera traumática", "Traumatic ulcer", "Pérdida de epitelio dolorosa, fondo amarillento/blanquecino y halo eritematoso; debe existir un irritante compatible.", "Painful epithelial loss with yellow-white base and erythematous halo; a compatible irritant should be identified.", "Diagnóstico presuntivo: úlcera traumática si historia y evolución son concordantes.", "Presumptive diagnosis: traumatic ulcer when history and evolution are concordant.", LesionVisualV39.ULCER, "Retirar causa y reevaluar. Persistencia, induración o progresión requiere valoración/biopsia según el caso.", "Remove cause and reassess. Persistence, induration or progression requires referral/biopsy as appropriate.")
private val leukoplakiaV39 = MucosaLesionV39("Placa blanca no desprendible", "Non-wipeable white plaque", "Placa blanca adherida que no se elimina al raspado suave.", "Adherent white plaque that does not wipe off gently.", "Diagnóstico presuntivo/diferencial: queratosis friccional, leucoplasia, liquen plano u otra lesión blanca.", "Presumptive/differential diagnosis: frictional keratosis, leukoplakia, lichen planus or another white lesion.", LesionVisualV39.WHITE_PLAQUE, "No diagnosticar leucoplasia sólo por el color. Lesiones persistentes o sospechosas requieren estudio histopatológico.", "Do not diagnose leukoplakia by color alone. Persistent or suspicious lesions require histopathologic assessment.")
private val candidiasisV39 = MucosaLesionV39("Placas blancas desprendibles", "Wipeable white plaques", "Placas blanquecinas cremosas que pueden desprenderse dejando superficie eritematosa.", "Creamy white plaques that may wipe off leaving an erythematous surface.", "Diagnóstico presuntivo: candidiasis pseudomembranosa si clínica, factores predisponentes y respuesta son compatibles.", "Presumptive diagnosis: pseudomembranous candidiasis when clinical findings, predisposing factors and response are compatible.", LesionVisualV39.PSEUDOMEMBRANE, "Considerar diagnóstico diferencial y factores predisponentes; no asumir infección por una imagen aislada.", "Consider differential diagnosis and predisposing factors; do not assume infection from appearance alone.")
private val lichenV39 = MucosaLesionV39("Patrón reticular blanco", "White reticular pattern", "Estrías blancas entrecruzadas, a menudo bilaterales; puede coexistir eritema o erosión.", "Interlacing white striae, often bilateral; erythema or erosion may coexist.", "Diagnóstico presuntivo/diferencial: liquen plano oral o reacción liquenoide.", "Presumptive/differential diagnosis: oral lichen planus or lichenoid reaction.", LesionVisualV39.RETICULAR, "La correlación clínica y, cuando corresponda, biopsia ayudan a confirmar y excluir imitadores.", "Clinical correlation and, when indicated, biopsy help confirm and exclude mimics.")
private val erythroV39 = MucosaLesionV39("Placa roja persistente", "Persistent red plaque", "Área roja aterciopelada bien delimitada que no se explica por trauma evidente.", "Well-demarcated velvety red area not explained by obvious trauma.", "Diagnóstico presuntivo/diferencial: eritroplasia/eritroplasia, inflamación, trauma o lesión erosiva.", "Presumptive/differential diagnosis: erythroplakia, inflammation, trauma or erosive lesion.", LesionVisualV39.RED_PATCH, "Una lesión roja persistente, especialmente en piso de boca, lengua o paladar blando, merece evaluación prioritaria y posible biopsia.", "A persistent red lesion, especially on floor of mouth, tongue or soft palate, warrants prompt assessment and possible biopsy.")
private val mucoceleV39 = MucosaLesionV39("Aumento de volumen azulado", "Bluish swelling", "Nódulo fluctuante, translúcido o azulado, con frecuencia en labio inferior.", "Fluctuant translucent or bluish nodule, commonly on the lower lip.", "Diagnóstico presuntivo: mucocele cuando sitio, consistencia y antecedente son compatibles.", "Presumptive diagnosis: mucocele when site, consistency and history are compatible.", LesionVisualV39.MUCOCELE, "Diferenciar de otras lesiones de glándulas salivales o vasculares; valorar persistencia/recurrencia.", "Differentiate from other salivary or vascular lesions; assess persistence/recurrence.")
private val geographicV39 = MucosaLesionV39("Lengua geográfica", "Geographic tongue", "Áreas eritematosas depapiladas con borde blanquecino serpiginoso y patrón migratorio.", "Depapillated erythematous areas with a serpiginous white border and migratory pattern.", "Diagnóstico presuntivo: glositis migratoria benigna si el patrón clínico es típico.", "Presumptive diagnosis: benign migratory glossitis when the clinical pattern is typical.", LesionVisualV39.GEOGRAPHIC, "Explicar variante benigna cuando el cuadro es típico; investigar si la presentación es atípica o persistente sin patrón migratorio.", "Explain the benign variant when typical; investigate atypical or persistently non-migratory presentations.")
private val fissuredV39 = MucosaLesionV39("Lengua fisurada", "Fissured tongue", "Surcos de profundidad variable en dorso lingual, generalmente asintomáticos.", "Grooves of variable depth on the dorsal tongue, usually asymptomatic.", "Diagnóstico clínico presuntivo: lengua fisurada/variante anatómica.", "Presumptive clinical diagnosis: fissured tongue/anatomic variant.", LesionVisualV39.FISSURED, "No confundir fisuras con úlceras. Enseñar higiene del dorso si retiene detritos.", "Do not confuse fissures with ulcers. Teach dorsal tongue hygiene if debris accumulates.")
private val papillomaV39 = MucosaLesionV39("Lesión papilar/exofítica", "Papillary/exophytic lesion", "Proyección sésil o pediculada con superficie papilar/verrugosa.", "Sessile or pedunculated projection with papillary/verrucous surface.", "Diagnóstico diferencial: papiloma escamoso, verruga, hiperplasia/reactiva u otra lesión exofítica.", "Differential diagnosis: squamous papilloma, wart, reactive hyperplasia or another exophytic lesion.", LesionVisualV39.PAPILLOMA, "Las lesiones exofíticas persistentes deben documentarse y valorarse para diagnóstico definitivo.", "Persistent exophytic lesions should be documented and assessed for definitive diagnosis.")
private val pigmentV39 = MucosaLesionV39("Mácula pigmentada", "Pigmented macule", "Área marrón, azulada o negra; registrar tamaño, límites, simetría, evolución y posibles causas locales.", "Brown, blue or black area; record size, borders, symmetry, evolution and possible local causes.", "Diagnóstico diferencial: pigmentación fisiológica, mácula melanótica, tatuaje por material, nevo u otras lesiones pigmentadas.", "Differential diagnosis: physiologic pigmentation, melanotic macule, material tattoo, nevus or other pigmented lesions.", LesionVisualV39.PIGMENT, "Pigmentación nueva, cambiante, irregular o inexplicada requiere valoración especializada.", "New, changing, irregular or unexplained pigmentation requires specialist assessment.")
private val vesiclesV39 = MucosaLesionV39("Vesículas/erosiones agrupadas", "Grouped vesicles/erosions", "Pequeñas vesículas que pueden romperse y dejar erosiones agrupadas; el sitio y recurrencia orientan el diferencial.", "Small vesicles that may rupture into grouped erosions; site and recurrence guide the differential.", "Diagnóstico diferencial: infección herpética u otros procesos vesículo-ampollosos.", "Differential diagnosis: herpetic infection or other vesiculobullous processes.", LesionVisualV39.VESICLES, "No concluir etiología sin historia, distribución y evolución; lesiones extensas o sistémicas requieren valoración.", "Do not conclude etiology without history, distribution and evolution; extensive/systemic disease requires assessment.")

private val mucosaRegionsV39 = listOf(
    MucosaRegionV39("upper_lip", "Labio superior y mucosa labial superior", "Upper lip and upper labial mucosa", "Rosado uniforme, húmedo, sin úlceras, masas ni placas persistentes; frenillo y glándulas menores pueden ser visibles/palpables.", "Uniform pink, moist, without persistent ulcers, masses or plaques; frenum and minor salivary glands may be visible/palpable.", listOf(commonUlcerV39, leukoplakiaV39, vesiclesV39)),
    MucosaRegionV39("lower_lip", "Labio inferior y mucosa labial inferior", "Lower lip and lower labial mucosa", "Rosado, húmedo y flexible; la mucosa interna puede mostrar vasos finos y glándulas menores sin que ello sea patológico.", "Pink, moist and flexible; fine vessels and minor glands may be visible on the inner mucosa without being pathologic.", listOf(mucoceleV39, commonUlcerV39, vesiclesV39)),
    MucosaRegionV39("buccal", "Mucosa yugal", "Buccal mucosa", "Rosada, húmeda, lisa; línea alba y gránulos de Fordyce pueden ser variantes normales según el caso.", "Pink, moist and smooth; linea alba and Fordyce granules may be normal variants depending on context.", listOf(lichenV39, leukoplakiaV39, candidiasisV39, commonUlcerV39)),
    MucosaRegionV39("vestibule", "Fondo de saco / vestíbulo", "Vestibule", "Mucosa flexible, húmeda y móvil, sin aumento de volumen, fístula ni ulceración.", "Flexible, moist and mobile mucosa, without swelling, sinus tract or ulceration.", listOf(commonUlcerV39, MucosaLesionV39("Trayecto fistuloso", "Sinus tract", "Pápula o punto de drenaje en el vestíbulo, a veces con exudado.", "Papule or drainage point in the vestibule, sometimes with exudate.", "Presuntivo: fístula de origen odontógeno; localizar diente causal con pruebas clínicas/radiográficas.", "Presumptive: odontogenic sinus tract; identify the source tooth with clinical/radiographic tests.", LesionVisualV39.PAPILLOMA, "No diagnosticar absceso sólo por la presencia de una pápula; correlacionar con vitalidad, percusión, palpación e imagen.", "Do not diagnose an abscess from a papule alone; correlate with vitality, percussion, palpation and imaging."))),
    MucosaRegionV39("gingiva", "Encía", "Gingiva", "Color y pigmentación variables fisiológicamente; margen adaptado, textura compatible con salud y ausencia de ulceración/sangrado espontáneo.", "Physiologic color/pigmentation varies; adapted margin, health-compatible texture and no ulceration/spontaneous bleeding.", listOf(MucosaLesionV39("Eritema y edema gingival", "Gingival erythema and edema", "Cambio de color, aumento de volumen y tendencia al sangrado asociado a inflamación.", "Color change, swelling and bleeding tendency associated with inflammation.", "Presuntivo: gingivitis sólo después de integrar placa, sangrado al sondaje y evaluación periodontal.", "Presumptive: gingivitis only after integrating plaque, bleeding on probing and periodontal assessment.", LesionVisualV39.RED_PATCH, "La apariencia gingival aislada no clasifica periodontitis.", "Gingival appearance alone does not classify periodontitis."), lichenV39, commonUlcerV39)),
    MucosaRegionV39("dorsal_tongue", "Dorso de lengua", "Dorsal tongue", "Papilas filiformes/fungiformes visibles, superficie simétrica y sin ulceraciones, masas ni áreas induradas persistentes.", "Visible filiform/fungiform papillae, symmetric surface without persistent ulcers, masses or indurated areas.", listOf(geographicV39, fissuredV39, candidiasisV39)),
    MucosaRegionV39("lateral_tongue", "Bordes laterales de lengua", "Lateral tongue", "Mucosa rosada con papilas foliadas posteriores variables; debe ser blanda, sin placa persistente, úlcera ni induración.", "Pink mucosa with variable posterior foliate papillae; should be soft, without persistent plaque, ulcer or induration.", listOf(commonUlcerV39, leukoplakiaV39, erythroV39)),
    MucosaRegionV39("ventral_tongue", "Cara ventral de lengua", "Ventral tongue", "Mucosa delgada y translúcida con vasos superficiales prominentes que pueden ser normales.", "Thin translucent mucosa with prominent superficial vessels that may be normal.", listOf(commonUlcerV39, erythroV39, pigmentV39)),
    MucosaRegionV39("floor", "Piso de boca", "Floor of mouth", "Mucosa fina, húmeda y blanda; pliegues sublinguales y carúnculas pueden ser visibles.", "Thin, moist and soft mucosa; sublingual folds and caruncles may be visible.", listOf(erythroV39, leukoplakiaV39, MucosaLesionV39("Aumento de volumen translúcido", "Translucent swelling", "Tumefacción azulada/translúcida en piso de boca.", "Bluish/translucent floor-of-mouth swelling.", "Diagnóstico diferencial: ránula, lesión salival, vascular u otra masa.", "Differential diagnosis: ranula, salivary lesion, vascular lesion or another mass.", LesionVisualV39.MUCOCELE, "Documentar tamaño, consistencia y relación con glándulas; derivar si persiste o compromete función.", "Document size, consistency and gland relationship; refer if persistent or functionally significant."))),
    MucosaRegionV39("hard_palate", "Paladar duro", "Hard palate", "Mucosa queratinizada firme, pálido-rosada, rugas anteriores normales y rafe medio visible.", "Firm keratinized pale-pink mucosa, normal anterior rugae and visible midline raphe.", listOf(vesiclesV39, leukoplakiaV39, pigmentV39)),
    MucosaRegionV39("soft_palate", "Paladar blando", "Soft palate", "Mucosa no queratinizada más rojiza, móvil, simétrica y sin lesiones persistentes.", "More reddish non-keratinized mobile mucosa, symmetric and without persistent lesions.", listOf(erythroV39, commonUlcerV39, candidiasisV39)),
    MucosaRegionV39("oropharynx", "Orofaringe", "Oropharynx", "Coloración rosada/rojiza simétrica; pilares y tejido linfoide pueden variar de tamaño según contexto.", "Symmetric pink-red coloration; pillars and lymphoid tissue vary in size with context.", listOf(MucosaLesionV39("Eritema/exudado", "Erythema/exudate", "Eritema difuso con o sin exudado en pilares/amígdalas.", "Diffuse erythema with or without exudate on pillars/tonsils.", "Presuntivo: proceso inflamatorio/infeccioso; requiere historia sistémica y exploración médica/odontológica según signos.", "Presumptive: inflammatory/infectious process; requires systemic history and appropriate medical/dental assessment.", LesionVisualV39.PSEUDOMEMBRANE, "Fiebre, dificultad respiratoria/deglutoria o deterioro general son signos de alarma.", "Fever, respiratory/swallowing difficulty or systemic deterioration are warning signs."), commonUlcerV39)),
    MucosaRegionV39("commissure", "Comisuras labiales", "Lip commissures", "Integridad de piel/mucosa, sin fisuras persistentes, costras ni eritema doloroso.", "Intact skin/mucosa without persistent fissures, crusting or painful erythema.", listOf(MucosaLesionV39("Fisura eritematosa en comisura", "Erythematous commissural fissure", "Fisura, maceración o costra en uno o ambos ángulos de la boca.", "Fissure, maceration or crust at one or both mouth corners.", "Presuntivo/diferencial: queilitis angular; revisar factores locales, candidiasis, deficiencias y dimensión vertical según contexto.", "Presumptive/differential: angular cheilitis; review local factors, candidiasis, deficiencies and vertical dimension as relevant.", LesionVisualV39.ULCER, "No asumir una única causa; suele ser multifactorial.", "Do not assume a single cause; it is often multifactorial.")))
)

@Composable
internal fun MucosaAtlasV39Screen(lang: String, onBack: () -> Unit) {
    var selectedRegion by remember { mutableStateOf(mucosaRegionsV39.first()) }
    var selectedLesion by remember { mutableStateOf<MucosaLesionV39?>(null) }

    ResponsiveScreenV17(
        tr(lang, "Atlas interactivo de mucosas", "Interactive oral mucosa atlas"),
        tr(lang, "Toca una región para revisar normalidad, alteraciones clínicas frecuentes y diagnóstico presuntivo/diferencial.", "Tap a region to review normal appearance, common clinical alterations and presumptive/differential diagnosis."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang,
            "El atlas enseña descripción clínica; no convierte una apariencia en diagnóstico definitivo. Lesiones persistentes, induradas, ulceradas, eritroplásicas/eritroleucoplásicas o inexplicadas requieren valoración y, cuando corresponda, biopsia.",
            "The atlas teaches clinical description; appearance alone is not a definitive diagnosis. Persistent, indurated, ulcerated, erythroplakic/erythroleukoplakic or unexplained lesions require assessment and, when indicated, biopsy."
        ))

        OralCavityIllustrationV39(selectedRegion.id, MaterialTheme.colorScheme.primary)

        ResponsiveSectionV17(tr(lang, "Regiones", "Regions")) {
            AdaptiveGridV17(mucosaRegionsV39.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val region = mucosaRegionsV39[index]
                Card(
                    onClick = { selectedRegion = region; selectedLesion = null },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = if (selectedRegion.id == region.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .4f))
                ) {
                    Text(if (lang == "en") region.en else region.es, Modifier.fillMaxWidth().padding(12.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
        }

        ResponsiveSectionV17("🩷 ${tr(lang, "Cómo se ve normalmente", "Normal appearance")}") {
            TissueNormalIllustrationV39(selectedRegion.id)
            Text(if (lang == "en") selectedRegion.normalEn else selectedRegion.normalEs)
        }

        ResponsiveSectionV17(tr(lang, "Alteraciones clínicas que puedes encontrar", "Clinical alterations you may encounter")) {
            selectedRegion.lesions.forEach { lesion ->
                val active = selectedLesion == lesion
                Card(
                    onClick = { selectedLesion = if (active) null else lesion },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = if (active) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .4f))
                ) {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (lang == "en") lesion.nameEn else lesion.nameEs, fontWeight = FontWeight.Black)
                        LesionIllustrationV39(lesion.visual)
                        if (active) {
                            Text("🔎 ${if (lang == "en") lesion.appearanceEn else lesion.appearanceEs}")
                            Text("🧠 ${if (lang == "en") lesion.presumptiveEn else lesion.presumptiveEs}", fontWeight = FontWeight.Bold)
                            Text("⚠️ ${if (lang == "en") lesion.cautionEn else lesion.cautionEs}")
                        } else {
                            Text(tr(lang, "Toca para abrir descripción y razonamiento diagnóstico", "Tap to open description and diagnostic reasoning"), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OralCavityIllustrationV39(highlight: String, accent: Color) {
    val outline = MaterialTheme.colorScheme.outline
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, outline.copy(alpha = .35f)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Canvas(Modifier.fillMaxWidth().height(300.dp).padding(12.dp)) {
            val w = size.width
            val h = size.height
            val tissue = Color(0xFFF3A5A8)
            val tissueDeep = Color(0xFFD8757E)
            val tongue = Color(0xFFE88992)
            val teeth = Color(0xFFFFF8E8)
            val selected = accent.copy(alpha = .45f)

            val lipOuter = Path().apply {
                moveTo(w * .12f, h * .45f)
                cubicTo(w * .23f, h * .18f, w * .38f, h * .20f, w * .50f, h * .33f)
                cubicTo(w * .62f, h * .20f, w * .77f, h * .18f, w * .88f, h * .45f)
                cubicTo(w * .76f, h * .78f, w * .64f, h * .83f, w * .50f, h * .70f)
                cubicTo(w * .36f, h * .83f, w * .24f, h * .78f, w * .12f, h * .45f)
                close()
            }
            drawPath(lipOuter, tissueDeep)

            val mouth = Path().apply {
                moveTo(w * .20f, h * .45f)
                cubicTo(w * .31f, h * .31f, w * .42f, h * .32f, w * .50f, h * .39f)
                cubicTo(w * .58f, h * .32f, w * .69f, h * .31f, w * .80f, h * .45f)
                cubicTo(w * .69f, h * .67f, w * .59f, h * .69f, w * .50f, h * .62f)
                cubicTo(w * .41f, h * .69f, w * .31f, h * .67f, w * .20f, h * .45f)
                close()
            }
            drawPath(mouth, tissue)

            val palate = Path().apply {
                moveTo(w * .31f, h * .43f)
                quadraticBezierTo(w * .50f, h * .30f, w * .69f, h * .43f)
                quadraticBezierTo(w * .50f, h * .49f, w * .31f, h * .43f)
                close()
            }
            drawPath(palate, Color(0xFFF6B7B0))

            val tonguePath = Path().apply {
                moveTo(w * .34f, h * .55f)
                cubicTo(w * .40f, h * .47f, w * .60f, h * .47f, w * .66f, h * .55f)
                cubicTo(w * .64f, h * .67f, w * .57f, h * .70f, w * .50f, h * .68f)
                cubicTo(w * .43f, h * .70f, w * .36f, h * .67f, w * .34f, h * .55f)
                close()
            }
            drawPath(tonguePath, tongue)
            drawLine(Color(0xFFB95663), Offset(w * .50f, h * .54f), Offset(w * .50f, h * .65f), strokeWidth = 2.5f)

            // Upper and lower teeth: recognizable crowns, not plain rectangles.
            for (i in 0 until 8) {
                val x = w * (.32f + i * .052f)
                val crownW = w * .045f
                val topY = h * .405f
                val p = Path().apply {
                    moveTo(x, topY)
                    quadraticBezierTo(x + crownW * .5f, topY - h * .025f, x + crownW, topY)
                    lineTo(x + crownW * .88f, topY + h * .075f)
                    quadraticBezierTo(x + crownW * .5f, topY + h * .095f, x + crownW * .12f, topY + h * .075f)
                    close()
                }
                drawPath(p, teeth)
                drawPath(p, outline.copy(alpha = .45f), style = Stroke(1.3f))
            }
            for (i in 0 until 8) {
                val x = w * (.32f + i * .052f)
                val crownW = w * .045f
                val y = h * .525f
                val p = Path().apply {
                    moveTo(x, y + h * .065f)
                    quadraticBezierTo(x + crownW * .5f, y + h * .085f, x + crownW, y + h * .065f)
                    lineTo(x + crownW * .88f, y)
                    quadraticBezierTo(x + crownW * .5f, y - h * .018f, x + crownW * .12f, y)
                    close()
                }
                drawPath(p, teeth)
                drawPath(p, outline.copy(alpha = .45f), style = Stroke(1.3f))
            }

            fun halo(cx: Float, cy: Float, rx: Float, ry: Float) {
                drawOval(selected, topLeft = Offset(cx - rx, cy - ry), size = Size(rx * 2, ry * 2))
                drawOval(accent, topLeft = Offset(cx - rx, cy - ry), size = Size(rx * 2, ry * 2), style = Stroke(3f))
            }
            when (highlight) {
                "upper_lip" -> halo(w * .50f, h * .29f, w * .24f, h * .10f)
                "lower_lip" -> halo(w * .50f, h * .75f, w * .24f, h * .10f)
                "buccal" -> { halo(w * .24f, h * .49f, w * .08f, h * .14f); halo(w * .76f, h * .49f, w * .08f, h * .14f) }
                "vestibule" -> halo(w * .50f, h * .58f, w * .20f, h * .07f)
                "gingiva" -> halo(w * .50f, h * .50f, w * .20f, h * .055f)
                "dorsal_tongue", "lateral_tongue", "ventral_tongue" -> halo(w * .50f, h * .59f, w * .17f, h * .10f)
                "floor" -> halo(w * .50f, h * .66f, w * .16f, h * .055f)
                "hard_palate" -> halo(w * .50f, h * .40f, w * .16f, h * .07f)
                "soft_palate", "oropharynx" -> halo(w * .50f, h * .36f, w * .11f, h * .05f)
                "commissure" -> { halo(w * .17f, h * .46f, w * .035f, h * .055f); halo(w * .83f, h * .46f, w * .035f, h * .055f) }
            }
            drawPath(lipOuter, outline, style = Stroke(3f))
        }
    }
}

@Composable
private fun TissueNormalIllustrationV39(region: String) {
    val outline = MaterialTheme.colorScheme.outline
    Canvas(Modifier.fillMaxWidth().height(130.dp).padding(10.dp)) {
        val w = size.width
        val h = size.height
        val base = when (region) {
            "hard_palate", "gingiva" -> Color(0xFFF0A7A6)
            "dorsal_tongue", "lateral_tongue", "ventral_tongue" -> Color(0xFFE98C95)
            else -> Color(0xFFF4AEB1)
        }
        val tissue = Path().apply {
            moveTo(w * .04f, h * .50f)
            cubicTo(w * .17f, h * .17f, w * .34f, h * .12f, w * .50f, h * .25f)
            cubicTo(w * .66f, h * .12f, w * .83f, h * .17f, w * .96f, h * .50f)
            cubicTo(w * .84f, h * .82f, w * .66f, h * .87f, w * .50f, h * .74f)
            cubicTo(w * .34f, h * .87f, w * .16f, h * .82f, w * .04f, h * .50f)
            close()
        }
        drawPath(tissue, base)
        drawPath(tissue, outline.copy(alpha=.55f), style = Stroke(2f))
        if (region.contains("tongue")) {
            for (i in 0..12) {
                val x = w * (.17f + i * .055f)
                drawCircle(Color(0xFFC7606B).copy(alpha=.7f), radius = 2.8f + (i % 3), center = Offset(x, h * (.40f + (i % 4) * .06f)))
            }
        }
        if (region == "hard_palate") {
            for (i in 0..4) {
                val y = h * (.34f + i * .08f)
                drawArc(outline.copy(alpha=.35f), 195f, 150f, false, Offset(w*.24f, y), Size(w*.52f, h*.32f), style=Stroke(2f))
            }
        }
        if (region == "ventral_tongue" || region == "floor") {
            drawLine(Color(0xFF6C7FB5).copy(alpha=.7f), Offset(w*.27f,h*.38f), Offset(w*.40f,h*.67f), strokeWidth=3f)
            drawLine(Color(0xFF6C7FB5).copy(alpha=.7f), Offset(w*.73f,h*.38f), Offset(w*.60f,h*.67f), strokeWidth=3f)
        }
    }
}

@Composable
private fun LesionIllustrationV39(type: LesionVisualV39) {
    val outline = MaterialTheme.colorScheme.outline
    Canvas(Modifier.fillMaxWidth().height(145.dp).padding(8.dp)) {
        val w = size.width
        val h = size.height
        val mucosa = Path().apply {
            moveTo(w*.03f,h*.50f)
            cubicTo(w*.18f,h*.12f,w*.35f,h*.15f,w*.50f,h*.27f)
            cubicTo(w*.65f,h*.15f,w*.82f,h*.12f,w*.97f,h*.50f)
            cubicTo(w*.82f,h*.88f,w*.65f,h*.85f,w*.50f,h*.73f)
            cubicTo(w*.35f,h*.85f,w*.18f,h*.88f,w*.03f,h*.50f)
            close()
        }
        drawPath(mucosa, Color(0xFFF1A1A7))
        drawPath(mucosa, outline.copy(alpha=.45f), style=Stroke(2f))
        when(type) {
            LesionVisualV39.ULCER -> {
                drawOval(Color(0xFFCE5964), Offset(w*.34f,h*.29f), Size(w*.32f,h*.43f))
                drawOval(Color(0xFFF2E0B8), Offset(w*.39f,h*.35f), Size(w*.22f,h*.31f))
            }
            LesionVisualV39.WHITE_PLAQUE -> {
                val p = Path().apply { moveTo(w*.28f,h*.49f); cubicTo(w*.36f,h*.26f,w*.47f,h*.30f,w*.53f,h*.39f); cubicTo(w*.60f,h*.28f,w*.69f,h*.35f,w*.72f,h*.52f); cubicTo(w*.61f,h*.69f,w*.48f,h*.67f,w*.40f,h*.62f); close() }
                drawPath(p, Color(0xFFFFF8E7)); drawPath(p, Color(0xFFD8D0C0), style=Stroke(2f))
            }
            LesionVisualV39.RETICULAR -> {
                for (i in 0..5) {
                    val y = h*(.33f+i*.065f)
                    drawLine(Color.White.copy(alpha=.92f), Offset(w*.28f,y), Offset(w*.72f,y+h*.08f), strokeWidth=3f)
                    drawLine(Color.White.copy(alpha=.92f), Offset(w*.72f,y), Offset(w*.28f,y+h*.08f), strokeWidth=3f)
                }
            }
            LesionVisualV39.RED_PATCH -> drawOval(Color(0xFFB92E3C), Offset(w*.30f,h*.28f), Size(w*.40f,h*.44f))
            LesionVisualV39.PSEUDOMEMBRANE -> {
                listOf(.30f to .36f, .43f to .50f, .57f to .39f, .66f to .58f, .35f to .62f).forEach { (x,y) -> drawOval(Color(0xFFFFF8E8), Offset(w*x,h*y), Size(w*.14f,h*.14f)) }
            }
            LesionVisualV39.MUCOCELE -> {
                drawOval(Color(0xFF8A87B9).copy(alpha=.75f), Offset(w*.36f,h*.25f), Size(w*.28f,h*.50f))
                drawOval(Color.White.copy(alpha=.45f), Offset(w*.41f,h*.30f), Size(w*.08f,h*.12f))
            }
            LesionVisualV39.GEOGRAPHIC -> {
                val p = Path().apply { moveTo(w*.28f,h*.49f); cubicTo(w*.35f,h*.29f,w*.43f,h*.31f,w*.49f,h*.42f); cubicTo(w*.57f,h*.27f,w*.69f,h*.37f,w*.71f,h*.53f); cubicTo(w*.64f,h*.69f,w*.51f,h*.68f,w*.45f,h*.60f); cubicTo(w*.38f,h*.68f,w*.30f,h*.61f,w*.28f,h*.49f); close() }
                drawPath(p, Color(0xFFC74752)); drawPath(p, Color(0xFFFFF2E0), style=Stroke(7f))
            }
            LesionVisualV39.FISSURED -> {
                drawLine(Color(0xFF9F4C58), Offset(w*.50f,h*.28f), Offset(w*.50f,h*.72f), strokeWidth=5f)
                drawLine(Color(0xFF9F4C58), Offset(w*.50f,h*.42f), Offset(w*.36f,h*.32f), strokeWidth=4f)
                drawLine(Color(0xFF9F4C58), Offset(w*.50f,h*.51f), Offset(w*.66f,h*.39f), strokeWidth=4f)
                drawLine(Color(0xFF9F4C58), Offset(w*.50f,h*.61f), Offset(w*.37f,h*.69f), strokeWidth=4f)
            }
            LesionVisualV39.PAPILLOMA -> {
                val baseY=h*.70f
                drawLine(Color(0xFFE8C7BC),Offset(w*.50f,baseY),Offset(w*.50f,h*.43f),strokeWidth=12f)
                listOf(-.12f,-.06f,0f,.06f,.12f).forEachIndexed { i,dx ->
                    drawCircle(Color(0xFFF2DFD4), radius=w*.055f, center=Offset(w*(.50f+dx),h*(.38f+(i%2)*.05f)))
                }
            }
            LesionVisualV39.PIGMENT -> {
                val p=Path().apply{ moveTo(w*.34f,h*.50f); cubicTo(w*.39f,h*.30f,w*.54f,h*.26f,w*.64f,h*.39f); cubicTo(w*.71f,h*.55f,w*.59f,h*.70f,w*.44f,h*.67f); cubicTo(w*.34f,h*.65f,w*.29f,h*.58f,w*.34f,h*.50f);close() }
                drawPath(p,Color(0xFF6A4B4D))
            }
            LesionVisualV39.VESICLES -> {
                listOf(.38f to .45f,.47f to .37f,.56f to .48f,.62f to .39f,.46f to .56f).forEach{(x,y)->
                    drawCircle(Color(0xFFF7D7D8),radius=w*.035f,center=Offset(w*x,h*y)); drawCircle(Color(0xFFBE4B58),radius=w*.035f,center=Offset(w*x,h*y),style=Stroke(2f))
                }
            }
            LesionVisualV39.NORMAL -> Unit
        }
    }
}
