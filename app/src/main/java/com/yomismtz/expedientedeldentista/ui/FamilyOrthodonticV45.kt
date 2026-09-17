package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class FamilyGroupV45(
    val titleEs: String,
    val titleEn: String,
    val diseasesEs: List<String>,
    val diseasesEn: List<String>
)

private val relativesV45 = listOf(
    "Madre", "Padre", "Abuela materna", "Abuelo materno", "Abuela paterna", "Abuelo paterno",
    "Hermana(s)", "Hermano(s)", "Hija(s)", "Hijo(s)", "Tía(s) materna(s)", "Tío(s) materno(s)",
    "Tía(s) paterna(s)", "Tío(s) paterno(s)"
)

private val familyGroupsV45 = listOf(
    FamilyGroupV45(
        "Cardiovasculares", "Cardiovascular",
        listOf("Hipertensión arterial", "Cardiopatía isquémica / angina", "Infarto de miocardio", "Arritmias", "Insuficiencia cardiaca", "Valvulopatías", "Cardiopatías congénitas"),
        listOf("Hypertension", "Ischemic heart disease / angina", "Myocardial infarction", "Arrhythmias", "Heart failure", "Valvular disease", "Congenital heart disease")
    ),
    FamilyGroupV45(
        "Endocrinas / metabólicas", "Endocrine / metabolic",
        listOf("Diabetes mellitus tipo 1", "Diabetes mellitus tipo 2", "Hipotiroidismo", "Hipertiroidismo", "Obesidad", "Dislipidemia", "Síndrome de ovario poliquístico", "Enfermedad suprarrenal"),
        listOf("Type 1 diabetes", "Type 2 diabetes", "Hypothyroidism", "Hyperthyroidism", "Obesity", "Dyslipidemia", "Polycystic ovary syndrome", "Adrenal disease")
    ),
    FamilyGroupV45(
        "Pulmonares / respiratorias", "Pulmonary / respiratory",
        listOf("Asma", "EPOC", "Bronquitis crónica", "Fibrosis pulmonar", "Apnea obstructiva del sueño", "Tuberculosis", "Bronquiectasias"),
        listOf("Asthma", "COPD", "Chronic bronchitis", "Pulmonary fibrosis", "Obstructive sleep apnea", "Tuberculosis", "Bronchiectasis")
    ),
    FamilyGroupV45(
        "Alérgicas", "Allergic",
        listOf("Rinitis alérgica", "Alergia alimentaria", "Alergia a medicamentos", "Dermatitis atópica", "Urticaria", "Anafilaxia", "Alergia al látex"),
        listOf("Allergic rhinitis", "Food allergy", "Drug allergy", "Atopic dermatitis", "Urticaria", "Anaphylaxis", "Latex allergy")
    ),
    FamilyGroupV45(
        "Autoinmunes / inmunomediadas", "Autoimmune / immune-mediated",
        listOf("Artritis reumatoide", "Lupus eritematoso sistémico", "Psoriasis", "Enfermedad celíaca", "Tiroiditis de Hashimoto", "Enfermedad de Graves", "Esclerosis múltiple", "Enfermedad inflamatoria intestinal"),
        listOf("Rheumatoid arthritis", "Systemic lupus erythematosus", "Psoriasis", "Celiac disease", "Hashimoto thyroiditis", "Graves disease", "Multiple sclerosis", "Inflammatory bowel disease")
    ),
    FamilyGroupV45(
        "Neurológicas", "Neurologic",
        listOf("Epilepsia", "Migraña", "Enfermedad de Parkinson", "Alzheimer / demencia", "Evento vascular cerebral", "Neuropatías hereditarias", "Trastornos del neurodesarrollo referidos"),
        listOf("Epilepsy", "Migraine", "Parkinson disease", "Alzheimer disease / dementia", "Stroke", "Inherited neuropathies", "Reported neurodevelopmental disorders")
    ),
    FamilyGroupV45(
        "Neoplásicas / cáncer", "Neoplastic / cancer",
        listOf("Cáncer de mama", "Cáncer colorrectal", "Cáncer de próstata", "Cáncer de ovario", "Cáncer de endometrio", "Cáncer de pulmón", "Cáncer de páncreas", "Leucemia / linfoma", "Otros cánceres"),
        listOf("Breast cancer", "Colorectal cancer", "Prostate cancer", "Ovarian cancer", "Endometrial cancer", "Lung cancer", "Pancreatic cancer", "Leukemia / lymphoma", "Other cancers")
    ),
    FamilyGroupV45(
        "Hematológicas", "Hematologic",
        listOf("Anemia hereditaria", "Anemia falciforme", "Talasemia", "Hemofilia", "Enfermedad de von Willebrand", "Trombofilia / trombosis", "Trastornos plaquetarios"),
        listOf("Inherited anemia", "Sickle cell disease", "Thalassemia", "Hemophilia", "von Willebrand disease", "Thrombophilia / thrombosis", "Platelet disorders")
    ),
    FamilyGroupV45(
        "Renales / urinarias", "Renal / urinary",
        listOf("Enfermedad renal crónica", "Enfermedad renal poliquística", "Litiasis renal", "Glomerulopatías", "Insuficiencia renal", "Trasplante renal", "Malformaciones urinarias referidas"),
        listOf("Chronic kidney disease", "Polycystic kidney disease", "Kidney stones", "Glomerular disease", "Kidney failure", "Kidney transplant", "Reported urinary malformations")
    ),
    FamilyGroupV45(
        "Óseas / reumatológicas", "Bone / rheumatologic",
        listOf("Osteoporosis", "Osteoartritis", "Artritis reumatoide", "Espondiloartritis", "Gota", "Osteogénesis imperfecta", "Otras enfermedades óseas familiares"),
        listOf("Osteoporosis", "Osteoarthritis", "Rheumatoid arthritis", "Spondyloarthritis", "Gout", "Osteogenesis imperfecta", "Other familial bone disease")
    ),
    FamilyGroupV45(
        "Gastrointestinales / hepáticas", "Gastrointestinal / hepatic",
        listOf("Enfermedad por reflujo", "Enfermedad inflamatoria intestinal", "Enfermedad celíaca", "Cirrosis", "Hepatitis crónica", "Hígado graso / enfermedad metabólica hepática", "Cáncer gastrointestinal familiar"),
        listOf("Reflux disease", "Inflammatory bowel disease", "Celiac disease", "Cirrhosis", "Chronic hepatitis", "Fatty / metabolic liver disease", "Familial gastrointestinal cancer")
    ),
    FamilyGroupV45(
        "Infecciosas / convivencia", "Infectious / household",
        listOf("Tuberculosis", "Hepatitis B", "Hepatitis C", "VIH referido", "Infecciones recurrentes en la familia", "Otra exposición infecciosa relevante"),
        listOf("Tuberculosis", "Hepatitis B", "Hepatitis C", "Reported HIV", "Recurrent infections in family", "Other relevant infectious exposure")
    ),
    FamilyGroupV45(
        "Dermatológicas", "Dermatologic",
        listOf("Psoriasis", "Dermatitis atópica", "Vitiligo", "Hidradenitis supurativa", "Alopecia areata", "Ictiosis / trastornos de queratinización", "Otras dermatosis familiares"),
        listOf("Psoriasis", "Atopic dermatitis", "Vitiligo", "Hidradenitis suppurativa", "Alopecia areata", "Ichthyosis / keratinization disorders", "Other familial dermatoses")
    ),
    FamilyGroupV45(
        "Psiquiátricas / adicciones", "Psychiatric / substance use",
        listOf("Depresión", "Trastorno bipolar", "Esquizofrenia", "Trastornos de ansiedad", "Trastornos por consumo de alcohol", "Trastornos por consumo de otras sustancias", "Conducta suicida referida en la familia"),
        listOf("Depression", "Bipolar disorder", "Schizophrenia", "Anxiety disorders", "Alcohol use disorder", "Other substance use disorders", "Reported suicidal behavior in family")
    ),
    FamilyGroupV45(
        "Genéticas / hereditarias", "Genetic / inherited",
        listOf("Síndrome de Down", "Fibrosis quística", "Distrofia muscular", "Síndromes de tejido conectivo", "Enfermedades metabólicas hereditarias", "Síndrome hereditario de cáncer", "Otra condición genética confirmada"),
        listOf("Down syndrome", "Cystic fibrosis", "Muscular dystrophy", "Connective tissue syndromes", "Inherited metabolic disorders", "Hereditary cancer syndrome", "Other confirmed genetic condition")
    )
)

@Composable
fun FamilyHistoryV45Screen(lang: String, onBack: () -> Unit) {
    var relative by remember { mutableStateOf(relativesV45.first()) }
    var groupIndex by remember { mutableStateOf(0) }
    val selected = remember { mutableStateMapOf<String, Set<String>>() }
    val group = familyGroupsV45[groupIndex]
    val diseases = if (lang == "en") group.diseasesEn else group.diseasesEs
    val keyPrefix = "$relative|${group.titleEs}"
    val current = selected[keyPrefix] ?: emptySet()

    ResponsiveScreenV17(
        tr(lang, "Antecedentes heredo-familiares", "Family health history"),
        tr(lang, "Toca primero el familiar, después una categoría y finalmente la enfermedad específica. No es necesario escribir nombres de familiares.", "Tap the relative, then a category, then the specific condition. No family member names are needed."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang,
            "Los antecedentes familiares ayudan a reconocer riesgos compartidos por genética, ambiente y hábitos. Registra, cuando se conozca, qué familiar tuvo la enfermedad y la edad aproximada de inicio; no conviertas un antecedente familiar en diagnóstico del paciente.",
            "Family history helps identify risks shared through genetics, environment and behaviors. When known, record which relative had the condition and approximate age at onset; do not convert family history into a diagnosis for the patient."
        ))

        ResponsiveSectionV17(tr(lang, "1 · Familiar", "1 · Relative")) {
            AdaptiveGridV17(relativesV45.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                val item = relativesV45[i]
                FilterChip(relative == item, { relative = item }, { Text(item) }, Modifier.fillMaxWidth())
            }
        }

        ResponsiveSectionV17(tr(lang, "2 · Categoría de enfermedad", "2 · Disease category")) {
            AdaptiveGridV17(familyGroupsV45.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                val item = familyGroupsV45[i]
                FilterChip(groupIndex == i, { groupIndex = i }, { Text(if (lang == "en") item.titleEn else item.titleEs) }, Modifier.fillMaxWidth())
            }
        }

        ResponsiveSectionV17(tr(lang, "3 · Enfermedades frecuentes de la categoría", "3 · Common conditions in this category")) {
            Text(if (lang == "en") group.titleEn else group.titleEs, fontWeight = FontWeight.Black)
            AdaptiveGridV17(diseases.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                val disease = diseases[i]
                FilterChip(
                    selected = disease in current,
                    onClick = { selected[keyPrefix] = if (disease in current) current - disease else current + disease },
                    label = { Text(disease) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "Resumen educativo", "Teaching summary")) {
            val all = selected.entries.filter { it.key.startsWith("$relative|") }.flatMap { entry ->
                val category = entry.key.substringAfter("|")
                entry.value.map { "$category: $it" }
            }
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(relative, fontWeight = FontWeight.Black)
                    Text(if (all.isEmpty()) tr(lang, "Sin antecedentes seleccionados todavía.", "No conditions selected yet.") else all.joinToString(" · "))
                }
            }
        }

        Text(
            tr(lang,
                "Fuente educativa: CDC, Family Health History (actualizado 2024–2026). La lista organiza ejemplos frecuentes y no pretende ser exhaustiva.",
                "Teaching source: CDC, Family Health History (updated 2024–2026). The list organizes common examples and is not exhaustive."
            ),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private data class ApplianceV45(
    val name: String,
    val type: String,
    val purpose: String,
    val timing: String
)

private val appliancesV45 = listOf(
    ApplianceV45("Brackets metálicos o cerámicos", "Ortodoncia fija", "Alinear, nivelar, cerrar/abrir espacios y corregir relaciones dentarias según el plan.", "Tratamiento integral frecuentemente 12–24 meses; los controles suelen espaciarse aproximadamente 4–12 semanas, según el caso."),
    ApplianceV45("Alineadores transparentes", "Ortodoncia removible", "Movimientos dentarios secuenciales en casos seleccionados.", "Duración muy variable según complejidad, cooperación y número de etapas/refinamientos."),
    ApplianceV45("Expansor rápido tipo Hyrax / Haas", "Ortopedia transversal", "Expandir el maxilar en deficiencia transversal cuando el crecimiento y la sutura lo permiten.", "Fase activa corta de semanas y fase de retención de meses; el protocolo exacto depende del aparato, edad y respuesta."),
    ApplianceV45("Quad-Helix", "Expansión dentoalveolar / ortodoncia interceptiva", "Expansión y corrección transversal dentoalveolar, especialmente en dentición mixta seleccionada.", "Se controla por etapas durante varios meses; la duración depende de la corrección y estabilidad."),
    ApplianceV45("Máscara facial de protracción", "Ortopedia Clase III", "Protracción maxilar en pacientes en crecimiento con componente maxilar deficiente y diagnóstico compatible.", "Se usa durante meses dentro de una fase ortopédica; horas de uso y duración son individualizadas."),
    ApplianceV45("Twin Block", "Aparato funcional", "Modificación funcional en determinados casos Clase II durante crecimiento.", "Suele requerir varios meses de uso y alta cooperación; no hay una duración universal."),
    ApplianceV45("Herbst", "Aparato funcional fijo", "Avance mandibular funcional en determinados casos Clase II durante crecimiento.", "Fase funcional de meses; el tiempo depende del crecimiento, respuesta y plan integral."),
    ApplianceV45("Arco lingual", "Mantenimiento / manejo de espacio", "Mantener longitud de arco o apoyar manejo de espacio en indicaciones específicas.", "Se mantiene mientras exista la indicación; puede retirarse al erupcionar dientes sucesores o al cambiar de fase."),
    ApplianceV45("Botón de Nance", "Mantenimiento de anclaje / espacio", "Mantener anclaje maxilar o preservar espacio en indicaciones específicas.", "Se mantiene durante la fase en que el anclaje o espacio es necesario."),
    ApplianceV45("Banda y ansa", "Mantenedor de espacio", "Mantener un espacio localizado tras pérdida prematura de un diente temporal cuando está indicado.", "Se controla periódicamente y se retira cuando erupciona el sucesor o deja de ser necesario."),
    ApplianceV45("Placa Hawley con resortes o tornillo", "Removible interceptivo", "Movimientos dentarios simples, recuperación de espacio o expansión dentoalveolar en casos seleccionados.", "Uso por meses; depende de cooperación, objetivo y magnitud del movimiento."),
    ApplianceV45("Plano inclinado / placa para mordida cruzada anterior", "Interceptivo", "Corregir mordida cruzada anterior dental simple cuando existe espacio y el diagnóstico lo permite.", "Suele plantearse como fase corta; la duración depende de la respuesta y de eliminar interferencias."),
    ApplianceV45("Retenedor Hawley / Essix / fijo", "Retención", "Mantener la posición alcanzada después del tratamiento activo.", "La retención sigue al tratamiento activo y puede prolongarse a largo plazo; el esquema depende del ortodoncista y del riesgo de recaída.")
)

private val orthoReasonsV45 = listOf(
    "Apiñamiento / discrepancia dentoalveolar", "Espaciamiento / diastemas", "Clase I con malposición dentaria",
    "Clase II división 1", "Clase II división 2", "Clase III", "Mordida cruzada anterior", "Mordida cruzada posterior",
    "Mordida abierta", "Mordida profunda", "Overjet aumentado", "Deficiencia transversal maxilar", "Desviación funcional mandibular",
    "Pérdida prematura de temporal / riesgo de pérdida de espacio", "Espacio perdido que requiere recuperación", "Erupción ectópica",
    "Diente impactado / retenido", "Agenesia / ausencia dentaria", "Supernumerario", "Asimetría / línea media desviada",
    "Alteración dentoalveolar asociada a hábito", "Necesidad de retención posterior a ortodoncia"
)

private val durationOptionsV45 = listOf(
    "Menos de 3 meses", "3–6 meses", "6–12 meses", "12–18 meses", "18–24 meses", "Más de 24 meses", "En curso / no recuerda"
)

@Composable
fun OrthodonticHistoryV45Screen(lang: String, onBack: () -> Unit) {
    var hadTreatment by remember { mutableStateOf("No recuerda / no sabe") }
    var treatmentType by remember { mutableStateOf("Ninguno seleccionado") }
    var appliance by remember { mutableStateOf<ApplianceV45?>(null) }
    var duration by remember { mutableStateOf("En curso / no recuerda") }
    var reason by remember { mutableStateOf("") }

    ResponsiveScreenV17(
        tr(lang, "Antecedentes ortodónticos y ortopédicos", "Orthodontic and orthopedic history"),
        tr(lang, "Guía para reconocer si hubo brackets u otros aparatos, para qué se usan y cómo registrar duración e indicación.", "Guide to recognize prior braces or appliances, their purpose, duration and indication."),
        onBack
    ) { profile ->
        NoticeCard(tr(lang,
            "Los tiempos son orientativos. La indicación y duración real dependen del diagnóstico completo, edad, crecimiento, cooperación, biomecánica y respuesta clínica. Esta pantalla enseña qué preguntar; no prescribe un aparato.",
            "Times are approximate. Actual indication and duration depend on complete diagnosis, age, growth, cooperation, biomechanics and response. This screen teaches what to ask; it does not prescribe an appliance."
        ))

        ResponsiveSectionV17(tr(lang, "¿Tuvo tratamiento previo?", "Previous treatment?")) {
            listOf("No", "Sí", "No recuerda / no sabe").forEach { option ->
                FilterChip(hadTreatment == option, { hadTreatment = option }, { Text(option) }, Modifier.fillMaxWidth())
            }
        }

        ResponsiveSectionV17(tr(lang, "Tipo de tratamiento que tuvo", "Type of previous treatment")) {
            listOf("Brackets / ortodoncia fija", "Alineadores", "Aparato removible", "Ortopedia dentofacial", "Mantenedor / recuperador de espacio", "Retención", "Otro").forEach { option ->
                FilterChip(treatmentType == option, { treatmentType = option }, { Text(option) }, Modifier.fillMaxWidth())
            }
        }

        ResponsiveSectionV17(tr(lang, "Ejemplos de aparatos y para qué sirven", "Appliance examples and purposes")) {
            AdaptiveGridV17(appliancesV45.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                val item = appliancesV45[i]
                Card(
                    onClick = { appliance = if (appliance == item) null else item },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = if (appliance == item) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(11.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(item.name, fontWeight = FontWeight.Black)
                        Text(item.type, style = MaterialTheme.typography.labelMedium)
                        if (appliance == item) {
                            Text("¿Para qué sirve?", fontWeight = FontWeight.Bold)
                            Text(item.purpose)
                            Text("Tiempo / protocolo orientativo", fontWeight = FontWeight.Bold)
                            Text(item.timing)
                        }
                    }
                }
            }
        }

        ResponsiveSectionV17(tr(lang, "Duración referida del tratamiento", "Reported treatment duration")) {
            AdaptiveGridV17(durationOptionsV45.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                val option = durationOptionsV45[i]
                FilterChip(duration == option, { duration = option }, { Text(option) }, Modifier.fillMaxWidth())
            }
        }

        ResponsiveSectionV17(tr(lang, "Motivo de indicación / diagnóstico referido", "Reason for treatment / reported diagnosis")) {
            AdaptiveGridV17(orthoReasonsV45.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                val option = orthoReasonsV45[i]
                FilterChip(reason == option, { reason = option }, { Text(option) }, Modifier.fillMaxWidth())
            }
        }

        ResponsiveSectionV17(tr(lang, "Resumen del antecedente", "History summary")) {
            val summary = buildString {
                append("Tratamiento previo: $hadTreatment")
                if (treatmentType != "Ninguno seleccionado") append(" · tipo: $treatmentType")
                appliance?.let { append(" · aparato: ${it.name}") }
                append(" · duración: $duration")
                if (reason.isNotBlank()) append(" · indicación referida: $reason")
            }
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text(summary, Modifier.padding(12.dp))
            }
        }

        Text(
            tr(lang,
                "Fuentes educativas: AAPD, Management of the Developing Dentition and Occlusion (revisión 2024); American Association of Orthodontists, Braces (duración y controles).",
                "Teaching sources: AAPD, Management of the Developing Dentition and Occlusion (2024 revision); American Association of Orthodontists, Braces (duration and adjustment intervals)."
            ),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
