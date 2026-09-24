package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class ClinicalVisualV43(
    val title: String,
    val imageName: String,
    val imageSource: String,
    val imageLicense: String,
    val keyFeatures: String,
    val distribution: String,
    val oralClues: String,
    val clinicalSource: String
)

private val exanthemsV43 = listOf(
    ClinicalVisualV43(
        title = "Varicela",
        imageName = "clinical_varicella",
        imageSource = "CDC Public Health Image Library (PHIL), ID 6121 · https://wwwn.cdc.gov/phil/details.aspx?pid=6121",
        imageLicense = "Dominio público (CDC PHIL).",
        keyFeatures = "Exantema pruriginoso con lesiones que evolucionan rápidamente de mácula a pápula, vesícula y costra. Es típico encontrar lesiones en DIFERENTES etapas al mismo tiempo.",
        distribution = "Suele comenzar en pecho, espalda y cara; con predominio centrípeto en tronco.",
        oralClues = "Puede haber lesiones en mucosa oral. En el interrogatorio importa antecedente de vacunación/exposición y la presencia de prurito.",
        clinicalSource = "CDC, Características clínicas de la varicela (2024): https://www.cdc.gov/chickenpox/es/hcp/clinical-signs/caracteristicas-clinicas-de-la-varicela.html"
    ),
    ClinicalVisualV43(
        title = "Viruela (smallpox) · histórica/erradicada",
        imageName = "clinical_smallpox",
        imageSource = "CDC PHIL, ID 10491 · lesiones maculopapulares tempranas en la lengua de un paciente con viruela · https://phil.cdc.gov/Details.aspx?pid=10491",
        imageLicense = "Dominio público (CDC PHIL).",
        keyFeatures = "Pródromo febril intenso. Las lesiones clásicas son profundas, firmes, redondas y bien delimitadas; en una misma zona tienden a estar en la MISMA etapa de desarrollo.",
        distribution = "Patrón centrífugo: orofaringe, cara y extremidades, con afectación de palmas y plantas más característica que en varicela.",
        oralClues = "Puede iniciar con lesiones en lengua/orofaringe. No es una infección circulante habitual: la viruela fue erradicada; una sospecha real sería una emergencia de salud pública.",
        clinicalSource = "CDC, Clinical Signs and Symptoms of Smallpox (2024): https://www.cdc.gov/smallpox/hcp/clinical-signs/index.html"
    ),
    ClinicalVisualV43(
        title = "Sarampión",
        imageName = "clinical_measles",
        imageSource = "CDC PHIL, ID 4497 · https://phil.cdc.gov/details.aspx?pid=4497",
        imageLicense = "Dominio público (CDC PHIL).",
        keyFeatures = "Fiebre, tos, coriza y conjuntivitis preceden con frecuencia al exantema. El rash es maculopapular y tiende a confluir.",
        distribution = "Comienza típicamente en la cara/línea del cabello y se extiende hacia cuello, tronco y extremidades.",
        oralClues = "Las manchas de Koplik en mucosa yugal pueden aparecer antes del exantema y son un hallazgo clásico de apoyo.",
        clinicalSource = "CDC, Photos/clinical information of Measles (2024): https://www.cdc.gov/measles/signs-symptoms/photos.html"
    ),
    ClinicalVisualV43(
        title = "Rubéola",
        imageName = "clinical_rubella",
        imageSource = "CDC PHIL, ID 712 · https://wwwn.cdc.gov/phil/details.aspx?pid=712",
        imageLicense = "Dominio público (CDC PHIL).",
        keyFeatures = "Suele producir una enfermedad más leve que el sarampión, con exantema rosado/rojizo fino y fiebre baja o pocos síntomas.",
        distribution = "El exantema suele comenzar en la cara y extenderse al resto del cuerpo; dura alrededor de tres días.",
        oralClues = "Puede acompañarse de adenopatías, especialmente retroauriculares/suboccipitales. En embarazo es especialmente relevante por el riesgo de síndrome de rubéola congénita.",
        clinicalSource = "CDC, Síntomas y complicaciones de la rubéola (2025): https://www.cdc.gov/rubella/es/signs-symptoms/sintomas-y-complicaciones-de-la-rubeola.html"
    )
)

private data class SystemAtlasV53(val titleEs:String,val titleEn:String,val examplesEs:String,val examplesEn:String,val visualEs:String,val visualEn:String,val oralEs:String,val oralEn:String)

private val multisystemAtlasV53 = listOf(
 SystemAtlasV53("Gastrointestinales / hepáticas","Gastrointestinal / hepatic","ERGE, enfermedad celíaca, Crohn, colitis ulcerosa, hepatitis, cirrosis.","GERD, celiac disease, Crohn disease, ulcerative colitis, hepatitis, cirrhosis.","Usa fotografías sólo cuando exista un signo visible; para procesos internos muestra anatomía/órgano y signos externos pertinentes.","Use photographs only for visible signs; for internal disease show anatomy/organ and relevant external signs.","Busca erosión dental por reflujo, úlceras, cambios de mucosa, xerostomía e implicaciones de enfermedad hepática.","Look for reflux-related erosion, ulcers, mucosal changes, xerostomia and implications of liver disease."),
 SystemAtlasV53("Reumatológicas / autoinmunes","Rheumatologic / autoimmune","Artritis reumatoide, lupus, Sjögren, esclerodermia, espondiloartritis.","Rheumatoid arthritis, lupus, Sjögren disease, scleroderma, spondyloarthritis.","Prioriza signos cutáneos, articulares, faciales y de mucosas que sean realmente observables.","Prioritize genuinely observable skin, joint, facial and mucosal signs.","Xerostomía, úlceras, limitación de apertura/ATM y medicamentos inmunomoduladores son especialmente relevantes.","Xerostomia, ulcers, limited opening/TMJ and immunomodulatory medicines are especially relevant."),
 SystemAtlasV53("Cardiovasculares","Cardiovascular","Hipertensión, cardiopatía isquémica, insuficiencia cardiaca, valvulopatías, arritmias.","Hypertension, ischemic heart disease, heart failure, valvular disease, arrhythmias.","No inventar una apariencia externa para hipertensión o arritmia; usar diagramas y signos visibles sólo cuando correspondan.","Do not invent an external appearance for hypertension or arrhythmia; use diagrams and visible signs only when appropriate.","Relaciona anticoagulantes/antiagregantes, presión arterial, tolerancia al procedimiento y antecedentes cardiacos relevantes.","Relate anticoagulants/antiplatelets, blood pressure, procedure tolerance and relevant cardiac history."),
 SystemAtlasV53("Endocrinas / metabólicas","Endocrine / metabolic","Diabetes, hipo/hipertiroidismo, trastornos suprarrenales, obesidad/dislipidemia.","Diabetes, hypo/hyperthyroidism, adrenal disorders, obesity/dyslipidemia.","Combina diagramas endocrinos con signos clínicos verificables cuando existan; evita estereotipos corporales.","Combine endocrine diagrams with verifiable clinical signs when present; avoid body stereotypes.","Diabetes: cicatrización, infección y periodonto; tiroides/suprarrenal: estado de control y medicación.","Diabetes: healing, infection and periodontium; thyroid/adrenal: control status and medication."),
 SystemAtlasV53("Respiratorias","Respiratory","Asma, EPOC, tuberculosis, apnea obstructiva del sueño, fibrosis pulmonar.","Asthma, COPD, tuberculosis, obstructive sleep apnea, pulmonary fibrosis.","Muestra signos/estudios o anatomía pertinentes; una foto facial aislada rara vez identifica la enfermedad.","Show relevant signs/studies or anatomy; an isolated facial photo rarely identifies the disease.","Inhaladores, xerostomía, candidiasis, respiración oral y tolerancia a la posición dental.","Inhalers, xerostomia, candidiasis, mouth breathing and tolerance of dental positioning."),
 SystemAtlasV53("Renales / urinarias","Renal / urinary","Enfermedad renal crónica, diálisis, trasplante renal, litiasis.","Chronic kidney disease, dialysis, kidney transplant, nephrolithiasis.","Usa anatomía renal y signos clínicos reales; no atribuyas un aspecto específico a toda persona con enfermedad renal.","Use kidney anatomy and real clinical signs; do not assign a specific appearance to everyone with kidney disease.","Considera sangrado, anemia, fármacos, diálisis/trasplante y ajuste médico cuando corresponda.","Consider bleeding, anemia, medicines, dialysis/transplant and medical adjustment when relevant."),
 SystemAtlasV53("Hematológicas","Hematologic","Anemias, hemofilia, von Willebrand, trastornos plaquetarios, leucemias.","Anemias, hemophilia, von Willebrand disease, platelet disorders, leukemias.","Las imágenes deben centrarse en signos documentables como palidez, petequias/equimosis o hallazgos orales, no diagnosticar por fotografía.","Images should focus on documentable signs such as pallor, petechiae/ecchymoses or oral findings, not diagnose from a photograph.","Sangrado, petequias, palidez, infecciones y necesidad de conocer diagnóstico/tratamiento hematológico.","Bleeding, petechiae, pallor, infections and the need to know the hematologic diagnosis/treatment."),
 SystemAtlasV53("Neurológicas","Neurologic","Epilepsia, Parkinson, EVC, esclerosis múltiple, neuropatías.","Epilepsy, Parkinson disease, stroke, multiple sclerosis, neuropathies.","Evita representar diagnóstico por apariencia; usa esquemas neurológicos y manifestaciones funcionales pertinentes.","Avoid representing diagnosis by appearance; use neurologic diagrams and relevant functional manifestations.","Medicamentos, coordinación/deglución, trauma por crisis, xerostomía e hiperplasia gingival cuando aplique.","Medicines, coordination/swallowing, seizure trauma, xerostomia and gingival enlargement when applicable."),
 SystemAtlasV53("Dermatológicas","Dermatologic","Dermatitis atópica, psoriasis y otras dermatosis con manifestaciones visibles.","Atopic dermatitis, psoriasis and other dermatoses with visible manifestations.","Aquí sí son especialmente útiles fotografías clínicas reales con distribución y morfología explicadas.","Real clinical photographs are particularly useful here when distribution and morphology are explained.","Correlaciona piel y mucosa sin asumir que una lesión oral aislada establece el diagnóstico sistémico.","Correlate skin and mucosa without assuming an isolated oral lesion establishes systemic diagnosis."),
 SystemAtlasV53("Infecciosas / exantemáticas","Infectious / exanthematous","Varicela, sarampión, rubéola y otras infecciones relevantes.","Varicella, measles, rubella and other relevant infections.","Compara morfología, etapa de lesión y distribución con fotografías verificadas.","Compare morphology, lesion stage and distribution using verified photographs.","Incluye lesiones orales características cuando existan y contexto de vacunación/exposición.","Include characteristic oral lesions when present and vaccination/exposure context.")
)

private val inflammatorySkinV43 = listOf(
    ClinicalVisualV43(
        title = "Eczema / dermatitis atópica",
        imageName = "clinical_eczema",
        imageSource = "Wikimedia Commons · File:Atopic dermatitis close up ac.jpeg · autor Assianir · https://commons.wikimedia.org/wiki/File:Atopic_dermatitis_close_up_ac.jpeg",
        imageLicense = "CC BY-SA 3.0; copia de alta resolución empaquetada en la app.",
        keyFeatures = "Inflamación cutánea con prurito, xerosis y áreas eritematosas/descamativas. En fases agudas puede haber exudado o costras.",
        distribution = "La localización cambia con la edad; en niños son frecuentes cara y pliegues, y en mayores predominan con frecuencia superficies flexurales.",
        oralClues = "No tiene una lesión oral diagnóstica específica. Importa por antecedentes atópicos, prurito, alergias y tratamientos sistémicos/tópicos.",
        clinicalSource = "American Academy of Dermatology, Atopic dermatitis overview: https://www.aad.org/public/diseases/eczema/types/atopic-dermatitis"
    ),
    ClinicalVisualV43(
        title = "Psoriasis",
        imageName = "clinical_psoriasis",
        imageSource = "Wikimedia Commons · File:2803 Psoriasis.jpg · Dr. Gandikota Raghurama Rao · https://commons.wikimedia.org/wiki/File:2803_Psoriasis.jpg",
        imageLicense = "CC BY 4.0; copia de 1139×749 px empaquetada en la app.",
        keyFeatures = "Enfermedad inflamatoria inmunomediada. Son típicas las placas bien delimitadas, engrosadas, eritematosas, con escama blanquecina/plateada.",
        distribution = "Frecuente en cuero cabelludo, codos, rodillas y región lumbosacra; puede afectar uñas y articulaciones.",
        oralClues = "No debe diagnosticarse por una lesión oral aislada. Preguntar por diagnóstico dermatológico, artritis, medicamentos e inmunomoduladores.",
        clinicalSource = "American Academy of Dermatology, Psoriasis signs/appearance: https://www.aad.org/public/diseases/psoriasis/what/look-like"
    )
)

@Composable
fun PathologicalV43Screen(lang: String, onBack: () -> Unit) {
    var mode by remember { mutableStateOf(0) }
    if (mode == 1) {
        PathologicalV40Screen(lang) { mode = 0 }
        return
    }

    ResponsiveScreenV17(
        tr(lang, "Antecedentes personales patológicos", "Pathological personal history"),
        tr(lang, "Registro educativo y atlas visual con imágenes reales, fuentes y diferencias clínicas.", "Teaching record and visual atlas with real images, sources and clinical differences."),
        onBack
    ) { profile ->
        Card(
            onClick = { mode = 1 },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(tr(lang, "📝 Abrir registro interactivo de antecedentes", "📝 Open interactive history record"), fontWeight = FontWeight.Black)
                Text(tr(lang, "Vacunación, tiempo de evolución, estado actual y medicamentos referidos.", "Vaccines, disease duration, current status and reported medicines."))
            }
        }

        NoticeCard(tr(lang,
            "Las fotografías ayudan a reconocer patrones, pero no deben usarse solas para diagnosticar. La historia, distribución, evolución, síntomas, exploración y pruebas siguen siendo necesarias.",
            "Photographs help recognize patterns but should not be used alone for diagnosis. History, distribution, evolution, symptoms, examination and tests are still required."))

        ResponsiveSectionV17(tr(lang, "Enfermedades exantemáticas · diferencias visuales", "Exanthematous diseases · visual differences")) {
            AdaptiveGridV17(exanthemsV43.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                ClinicalVisualCardV43(exanthemsV43[i])
            }
            NoticeCard(tr(lang,
                "Diferencia clave para no confundir varicela y viruela: en varicela suelen coexistir lesiones en distintas etapas y predomina el tronco; en la viruela clásica las lesiones de una zona tienden a estar en la misma etapa, son profundas/firmes y el patrón es más centrífugo. La viruela está erradicada.",
                "Key distinction between chickenpox and smallpox: chickenpox usually has lesions in different stages with trunk predominance; classic smallpox lesions in one area tend to be in the same stage, deep/firm, with a more centrifugal pattern. Smallpox is eradicated."))
        }

        ResponsiveSectionV17(tr(lang, "Atlas visual multisistémico", "Multisystem visual atlas")) {
            NoticeCard(tr(lang,
                "Cada sistema usa el tipo de imagen que corresponde clínicamente: fotografía real para signos visibles y diagramas anatómicos/educativos para enfermedades internas sin una apariencia externa específica. No se representa un diagnóstico mediante una foto genérica.",
                "Each system uses the clinically appropriate image type: real photographs for visible signs and anatomical/educational diagrams for internal diseases without a specific external appearance. A diagnosis is not represented by a generic photo."
            ))
            AdaptiveGridV17(multisystemAtlasV53.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                val item = multisystemAtlasV53[i]
                Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant), shape=RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(VisualSpacingV49.md), verticalArrangement=Arrangement.spacedBy(VisualSpacingV49.sm)) {
                        Text(if(lang=="en") item.titleEn else item.titleEs, fontWeight=FontWeight.Black)
                        Text(if(lang=="en") item.examplesEn else item.examplesEs)
                        Text(tr(lang,"Cómo se ve / qué imagen usar","How it looks / image type"),fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.primary)
                        Text(if(lang=="en") item.visualEn else item.visualEs)
                        Text(tr(lang,"Relevancia odontológica","Dental relevance"),fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.primary)
                        Text(if(lang=="en") item.oralEn else item.oralEs)
                    }
                }
            }
            NoticeCard(tr(lang,
                "Fuentes visuales previstas: CDC PHIL para fotografías clínicas con estado de derechos comprobado y NIDDK/NIH para material anatómico de sistemas digestivo, endocrino, renal y hepático. La fuente se mostrará de forma compacta en cada recurso.",
                "Planned visual sources: CDC PHIL for clinical photographs with verified rights status and NIDDK/NIH for digestive, endocrine, kidney and liver anatomical material. Each resource will show a compact source label."
            ))
        }

        ResponsiveSectionV17(tr(lang, "Eczema vs psoriasis · inflamatorias/inmunomediadas", "Eczema vs psoriasis · inflammatory/immune-mediated")) {
            NoticeCard(tr(lang,
                "Corrección de clasificación: el eczema/dermatitis atópica NO se considera una enfermedad autoinmune clásica. La psoriasis sí es una enfermedad inflamatoria inmunomediada. Por eso se comparan aquí sin etiquetar a ambas como autoinmunes.",
                "Classification note: eczema/atopic dermatitis is NOT considered a classic autoimmune disease. Psoriasis is an immune-mediated inflammatory disease. They are compared here without labeling both as autoimmune."))
            AdaptiveGridV17(inflammatorySkinV43.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { i ->
                ClinicalVisualCardV43(inflammatorySkinV43[i])
            }
            NoticeCard(tr(lang,
                "Regla rápida: eczema suele picar mucho y presenta piel seca/inflamada con bordes menos nítidos; psoriasis suele formar placas más bien delimitadas y engrosadas con escama plateada. Hay solapamientos y el diagnóstico dermatológico puede requerir valoración especializada.",
                "Quick rule: eczema is often very itchy with dry inflamed skin and less sharply defined borders; psoriasis more often forms well-demarcated thick plaques with silvery scale. Overlap exists and dermatologic diagnosis may require specialist assessment."))
        }
    }
}

@Composable
private fun ClinicalVisualCardV43(item: ClinicalVisualV43) {
    var open by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imageId = context.resources.getIdentifier(item.imageName, "drawable", context.packageName)

    Card(
        onClick = { open = !open },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (open) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(VisualSpacingV49.md), verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.sm)) {
            if (imageId != 0) {
                EducationalVisualFrameV49(
                    title = item.title,
                    caption = item.imageSource,
                    credit = item.imageLicense
                ) {
                    OfflineClinicalImageV50(
                        drawable = imageId,
                        contentDescription = item.title,
                        maxHeight = 320.dp
                    )
                }
            } else {
                Text(item.title, fontWeight = FontWeight.Black)
                Text("Imagen no disponible en esta compilación.", color = MaterialTheme.colorScheme.error)
            }
            Text(
                if (open) "Toca para cerrar detalles" else "Toca para ver diferencias y claves clínicas",
                style = MaterialTheme.typography.bodySmall
            )
            if (open) {
                Text("Claves clínicas", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Text(item.keyFeatures)
                Text("Distribución", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Text(item.distribution)
                Text("Relevancia oral / interrogatorio", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Text(item.oralClues)
                Text("Fuente clínica", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Text(item.clinicalSource, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
