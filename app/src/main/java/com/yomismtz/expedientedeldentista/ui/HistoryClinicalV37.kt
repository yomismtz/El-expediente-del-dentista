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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen

private data class HistoryMenuItemV37(
    val es: String,
    val en: String,
    val detailEs: String,
    val detailEn: String,
    val screen: AppScreen
)

private data class ComplaintExampleV37(
    val literal: String,
    val clinical: String
)

private data class DiseaseGroupV37(
    val es: String,
    val en: String,
    val diseasesEs: List<String>,
    val diseasesEn: List<String>,
    val medicinesEs: List<String>,
    val medicinesEn: List<String>
)

private val historyMenuItemsV37 = listOf(
    HistoryMenuItemV37(
        "Identificación del paciente",
        "Patient identification",
        "Datos demográficos y sociales del apartado de Historia clínica. Es distinto de la Ficha de identificación del expediente.",
        "Demographic and social data within the clinical history. This is different from the record's Identification sheet.",
        AppScreen.IDENTIFICATION
    ),
    HistoryMenuItemV37(
        "Motivo de consulta y padecimiento actual",
        "Chief complaint and present illness",
        "Primero se registra literalmente lo que dice el paciente o tutor; después se redacta en lenguaje clínico con los hallazgos disponibles.",
        "First record the patient's or guardian's exact words; then write the clinical description using available findings.",
        AppScreen.HISTORY_REASON
    ),
    HistoryMenuItemV37(
        "Antecedentes heredo-familiares",
        "Family history",
        "Familiares específicos por línea materna/paterna y categorías de enfermedad.",
        "Specific maternal/paternal relatives and disease categories.",
        AppScreen.HISTORY_FAMILY
    ),
    HistoryMenuItemV37(
        "Antecedentes personales no patológicos",
        "Non-pathological personal history",
        "Vivienda, piso/techo, servicios, higiene general y bucal, alimentación, tabaco, alcohol, drogas, perforaciones y tatuajes.",
        "Housing, floor/roof, utilities, general/oral hygiene, diet, tobacco, alcohol, drugs, piercings and tattoos.",
        AppScreen.HISTORY_NONPATH
    ),
    HistoryMenuItemV37(
        "Antecedentes personales patológicos",
        "Pathological personal history",
        "Vacunación, enfermedades por sistemas, desde cuándo, tratamiento referido, esquema que toma, estado actual y complicaciones.",
        "Vaccination, disease categories, onset, reported treatment regimen, current status and complications.",
        AppScreen.HISTORY_PATHOLOGICAL
    ),
    HistoryMenuItemV37(
        "Antecedentes quirúrgicos y traumáticos",
        "Surgical and trauma history",
        "Cirugías, hospitalizaciones, transfusiones, donación de sangre, fracturas, traumatismos, anestesia y grupo sanguíneo.",
        "Surgery, hospitalizations, transfusions, blood donation, fractures, trauma, anesthesia and blood group.",
        AppScreen.HISTORY_SURGICAL_TRAUMA
    ),
    HistoryMenuItemV37("Exploración física", "Physical examination", "Signos vitales, signos y síntomas, exploración general y cabeza/cuello.", "Vital signs, signs/symptoms, general inspection and head/neck examination.", AppScreen.VITALS),
    HistoryMenuItemV37("Exploración de ATM", "TMJ examination", "Dolor, ruidos, apertura, lateralidades, protrusión/retrusión, desviaciones y limitaciones.", "Pain, sounds, opening, lateral excursions, protrusion/retrusion, deviations and limitations.", AppScreen.ATM),
    HistoryMenuItemV37("Examen de oclusión", "Occlusion examination", "Dentición, erupción, Angle, relación canina, líneas medias, overjet, overbite y mordidas.", "Dentition, eruption, Angle, canine relation, midlines, overjet, overbite and bite relationships.", AppScreen.OCCLUSION)
)

private val complaintExamplesV37 = listOf(
    ComplaintExampleV37(
        "“Traje a mi hijo porque tiene un hoyo en la muela.”",
        "Después de explorar: paciente pediátrico con lesión cavitada compatible con caries activa en el órgano dentario 16; describir superficie, extensión, síntomas y hallazgos ICDAS/odontograma cuando estén disponibles."
    ),
    ComplaintExampleV37(
        "“Me duele una muela cuando tomo frío.”",
        "Dolor dental provocado por estímulo frío en OD ___, de intensidad ___ y duración ___; señalar si el dolor desaparece al retirar el estímulo o persiste, y correlacionarlo con pruebas pulpares."
    ),
    ComplaintExampleV37(
        "“Le salió una bolita en la encía y le sale agua blanca.”",
        "Aumento de volumen/fístula en encía vestibular asociada a OD ___ con salida de exudado; documentar dolor, palpación, percusión, vitalidad y hallazgos radiográficos."
    ),
    ComplaintExampleV37(
        "“Me sangran las encías cuando me cepillo.”",
        "Sangrado gingival referido durante el cepillado; registrar distribución, inflamación, biopelícula, sangrado al sondaje y demás hallazgos periodontales antes de establecer diagnóstico."
    ),
    ComplaintExampleV37(
        "“Se me rompió un diente comiendo.”",
        "Pérdida/fractura de estructura dental en OD ___ durante la masticación; describir extensión, exposición dentinaria o pulpar, dolor, movilidad, oclusión y antecedentes restauradores."
    )
)

private val familyRelativesV37 = listOf(
    "Madre", "Padre",
    "Abuela materna", "Abuelo materno",
    "Abuela paterna", "Abuelo paterno",
    "Hermana(s)", "Hermano(s)",
    "Hija(s)", "Hijo(s)",
    "Tía(s) materna(s)", "Tío(s) materno(s)",
    "Tía(s) paterna(s)", "Tío(s) paterno(s)"
)

private val familyDiseaseGroupsV37 = listOf(
    "Cardiovasculares", "Endocrinas / metabólicas", "Pulmonares / respiratorias", "Alérgicas / inmunológicas",
    "Neurológicas", "Neoplásicas / cáncer", "Hematológicas", "Renales", "Óseas / reumatológicas",
    "Infecciosas", "ETS / ITS", "Dermatológicas", "Psiquiátricas", "Otras"
)

private val vaccinesV37 = listOf(
    "BCG", "Hepatitis B", "Hexavalente acelular", "Rotavirus", "Neumococo conjugada",
    "Influenza", "SRP (sarampión-rubéola-parotiditis)", "DPT", "VPH", "Td / Tdpa",
    "SR (sarampión-rubéola)", "COVID-19", "Otra documentada en cartilla"
)

private val diseaseGroupsV37 = listOf(
    DiseaseGroupV37(
        "Cardiovasculares", "Cardiovascular",
        listOf("Hipertensión arterial", "Cardiopatía isquémica / angina", "Infarto previo", "Arritmia", "Insuficiencia cardiaca", "Valvulopatía", "Cardiopatía congénita"),
        listOf("Hypertension", "Ischemic heart disease / angina", "Previous myocardial infarction", "Arrhythmia", "Heart failure", "Valvular disease", "Congenital heart disease"),
        listOf("Losartán", "Enalapril", "Amlodipino", "Metoprolol / propranolol", "Ácido acetilsalicílico", "Clopidogrel", "Warfarina", "Apixabán / rivaroxabán", "Estatina u otro referido"),
        listOf("Losartan", "Enalapril", "Amlodipine", "Metoprolol / propranolol", "Aspirin", "Clopidogrel", "Warfarin", "Apixaban / rivaroxaban", "Statin or other reported medicine")
    ),
    DiseaseGroupV37(
        "Endocrinas / metabólicas", "Endocrine / metabolic",
        listOf("Diabetes mellitus", "Resistencia a la insulina", "Hipotiroidismo", "Hipertiroidismo", "Obesidad", "Dislipidemia", "Trastorno suprarrenal"),
        listOf("Diabetes mellitus", "Insulin resistance", "Hypothyroidism", "Hyperthyroidism", "Obesity", "Dyslipidemia", "Adrenal disorder"),
        listOf("Metformina", "Insulina", "Empagliflozina / dapagliflozina", "Semaglutida u otro agonista GLP-1 referido", "Levotiroxina", "Metimazol / tiamazol", "Otro referido"),
        listOf("Metformin", "Insulin", "Empagliflozin / dapagliflozin", "Reported GLP-1 agonist such as semaglutide", "Levothyroxine", "Methimazole", "Other reported medicine")
    ),
    DiseaseGroupV37(
        "Pulmonares / respiratorias", "Pulmonary / respiratory",
        listOf("Asma", "EPOC", "Tuberculosis", "Bronquitis crónica", "Apnea obstructiva del sueño", "Fibrosis pulmonar", "Rinitis alérgica"),
        listOf("Asthma", "COPD", "Tuberculosis", "Chronic bronchitis", "Obstructive sleep apnea", "Pulmonary fibrosis", "Allergic rhinitis"),
        listOf("Salbutamol", "Budesonida / beclometasona inhalada", "Formoterol u otro broncodilatador referido", "Montelukast", "Antihistamínico referido", "Tratamiento antituberculoso referido", "Otro"),
        listOf("Albuterol/salbutamol", "Inhaled budesonide / beclomethasone", "Formoterol or other reported bronchodilator", "Montelukast", "Reported antihistamine", "Reported tuberculosis treatment", "Other")
    ),
    DiseaseGroupV37(
        "Hematológicas", "Hematologic",
        listOf("Anemia", "Trastorno plaquetario", "Hemofilia", "Enfermedad de von Willebrand", "Trombosis / trombofilia", "Leucemia", "Otro trastorno hematológico"),
        listOf("Anemia", "Platelet disorder", "Hemophilia", "von Willebrand disease", "Thrombosis / thrombophilia", "Leukemia", "Other hematologic disorder"),
        listOf("Hierro / ácido fólico / vitamina B12 referidos", "Factor de coagulación referido", "Anticoagulante referido", "Antiagregante referido", "Tratamiento hematológico específico referido"),
        listOf("Reported iron / folate / vitamin B12", "Reported clotting factor", "Reported anticoagulant", "Reported antiplatelet", "Reported disease-specific hematology treatment")
    ),
    DiseaseGroupV37(
        "Neurológicas", "Neurologic",
        listOf("Epilepsia", "Migraña", "Parkinson", "Alzheimer / demencia", "Esclerosis múltiple", "Evento vascular cerebral", "Neuropatía"),
        listOf("Epilepsy", "Migraine", "Parkinson disease", "Alzheimer disease / dementia", "Multiple sclerosis", "Stroke", "Neuropathy"),
        listOf("Levetiracetam", "Ácido valproico", "Carbamazepina", "Fenitoína", "Topiramato", "Levodopa/carbidopa", "Otro referido"),
        listOf("Levetiracetam", "Valproate", "Carbamazepine", "Phenytoin", "Topiramate", "Levodopa/carbidopa", "Other reported medicine")
    ),
    DiseaseGroupV37(
        "Renales / urinarias", "Renal / urinary",
        listOf("Enfermedad renal crónica", "Insuficiencia renal", "Diálisis", "Trasplante renal", "Litiasis", "Infecciones urinarias recurrentes"),
        listOf("Chronic kidney disease", "Renal failure", "Dialysis", "Kidney transplant", "Nephrolithiasis", "Recurrent urinary infection"),
        listOf("Antihipertensivo referido", "Diurético referido", "Quelante de fósforo referido", "Inmunosupresor referido", "Otro tratamiento nefrológico referido"),
        listOf("Reported antihypertensive", "Reported diuretic", "Reported phosphate binder", "Reported immunosuppressant", "Other reported nephrology treatment")
    ),
    DiseaseGroupV37(
        "Gastrointestinales / hepáticas", "Gastrointestinal / hepatic",
        listOf("Gastritis / reflujo", "Úlcera péptica", "Hepatitis", "Cirrosis", "Hígado graso", "Enfermedad inflamatoria intestinal"),
        listOf("Gastritis / reflux", "Peptic ulcer", "Hepatitis", "Cirrhosis", "Fatty liver disease", "Inflammatory bowel disease"),
        listOf("Omeprazol u otro inhibidor de bomba referido", "Antiácido referido", "Antiviral para hepatitis referido", "Inmunomodulador referido", "Otro"),
        listOf("Reported proton-pump inhibitor such as omeprazole", "Reported antacid", "Reported hepatitis antiviral", "Reported immunomodulator", "Other")
    ),
    DiseaseGroupV37(
        "Óseas / reumatológicas", "Bone / rheumatologic",
        listOf("Osteoporosis", "Artritis reumatoide", "Lupus", "Osteoartritis", "Espondiloartritis", "Enfermedad ósea metabólica"),
        listOf("Osteoporosis", "Rheumatoid arthritis", "Lupus", "Osteoarthritis", "Spondyloarthritis", "Metabolic bone disease"),
        listOf("Bisfosfonato referido", "Denosumab referido", "Corticoide sistémico referido", "Metotrexato", "Biológico / inmunomodulador referido", "AINE referido"),
        listOf("Reported bisphosphonate", "Reported denosumab", "Reported systemic corticosteroid", "Methotrexate", "Reported biologic / immunomodulator", "Reported NSAID")
    ),
    DiseaseGroupV37(
        "Alérgicas / inmunológicas", "Allergic / immunologic",
        listOf("Alergia medicamentosa", "Alergia alimentaria", "Alergia a látex/materiales", "Rinitis alérgica", "Anafilaxia previa", "Inmunodeficiencia", "Enfermedad autoinmune"),
        listOf("Drug allergy", "Food allergy", "Latex/material allergy", "Allergic rhinitis", "Previous anaphylaxis", "Immunodeficiency", "Autoimmune disease"),
        listOf("Antihistamínico referido", "Corticoide referido", "Autoinyector de adrenalina referido", "Inmunomodulador referido", "Otro"),
        listOf("Reported antihistamine", "Reported corticosteroid", "Reported epinephrine autoinjector", "Reported immunomodulator", "Other")
    ),
    DiseaseGroupV37(
        "Infecciosas / exantemáticas", "Infectious / exanthematous",
        listOf("Tuberculosis", "Hepatitis B/C", "VIH", "Sífilis", "Sarampión", "Rubéola", "Varicela", "COVID-19", "Otra infección relevante"),
        listOf("Tuberculosis", "Hepatitis B/C", "HIV", "Syphilis", "Measles", "Rubella", "Varicella", "COVID-19", "Other relevant infection"),
        listOf("Antiviral referido", "Antibiótico referido", "Antirretroviral referido", "Tratamiento antituberculoso referido", "Tratamiento sintomático referido", "Otro"),
        listOf("Reported antiviral", "Reported antibiotic", "Reported antiretroviral", "Reported tuberculosis treatment", "Reported symptomatic treatment", "Other")
    ),
    DiseaseGroupV37(
        "Neoplásicas", "Neoplastic",
        listOf("Cáncer sólido", "Leucemia", "Linfoma", "Antecedente de quimioterapia", "Antecedente de radioterapia", "Trasplante de médula / células hematopoyéticas"),
        listOf("Solid tumor", "Leukemia", "Lymphoma", "Previous chemotherapy", "Previous radiotherapy", "Bone marrow / hematopoietic cell transplant"),
        listOf("Quimioterapia referida", "Terapia dirigida referida", "Inmunoterapia referida", "Hormonoterapia referida", "Analgesia referida", "Otro"),
        listOf("Reported chemotherapy", "Reported targeted therapy", "Reported immunotherapy", "Reported hormone therapy", "Reported analgesia", "Other")
    ),
    DiseaseGroupV37(
        "Psiquiátricas / neurodesarrollo", "Psychiatric / neurodevelopmental",
        listOf("Ansiedad", "Depresión", "TDAH", "Autismo", "Trastorno bipolar", "Psicosis", "Trastorno del sueño"),
        listOf("Anxiety", "Depression", "ADHD", "Autism", "Bipolar disorder", "Psychosis", "Sleep disorder"),
        listOf("Sertralina", "Fluoxetina", "Metilfenidato", "Atomoxetina", "Risperidona", "Litio u otro estabilizador referido", "Hipnótico referido"),
        listOf("Sertraline", "Fluoxetine", "Methylphenidate", "Atomoxetine", "Risperidone", "Lithium or other reported mood stabilizer", "Reported hypnotic")
    ),
    DiseaseGroupV37(
        "Dermatológicas / otras", "Dermatologic / other",
        listOf("Psoriasis", "Dermatitis", "Enfermedad genética", "Enfermedad rara", "Otra condición no incluida"),
        listOf("Psoriasis", "Dermatitis", "Genetic disease", "Rare disease", "Other condition not listed"),
        listOf("Tratamiento tópico referido", "Antihistamínico referido", "Inmunomodulador referido", "Biológico referido", "Otro medicamento referido"),
        listOf("Reported topical treatment", "Reported antihistamine", "Reported immunomodulator", "Reported biologic", "Other reported medicine")
    )
)

@Composable
internal fun HistoryMenuV37Screen(lang: String, onNavigate: (AppScreen) -> Unit, onBack: () -> Unit) {
    ResponsiveScreenV17(
        tr(lang, "Historia clínica", "Clinical history"),
        tr(lang, "Cada apartado abre su propia guía interactiva. Sin números romanos y sin mezclar la Ficha de identificación con Identificación del paciente.", "Each section opens its own interactive guide. No Roman numerals and no mixing of the Identification sheet with Patient identification."),
        onBack
    ) { profile ->
        AdaptiveGridV17(historyMenuItemsV37.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
            val item = historyMenuItemsV37[index]
            Card(
                onClick = { onNavigate(item.screen) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .4f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (lang == "en") item.en else item.es, fontWeight = FontWeight.Black)
                    Text(if (lang == "en") item.detailEn else item.detailEs, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
internal fun ReasonPresentIllnessV37Screen(lang: String, onBack: () -> Unit) {
    var literal by remember { mutableStateOf("") }
    var clinical by remember { mutableStateOf("") }
    ResponsiveScreenV17(
        tr(lang, "Motivo de consulta y padecimiento actual", "Chief complaint and present illness"),
        tr(lang, "El motivo de consulta conserva las palabras del paciente/tutor. El padecimiento actual las convierte en una narración clínica sustentada por interrogatorio y exploración.", "The chief complaint preserves the patient's/guardian's own words. Present illness converts them into a clinical narrative supported by history and examination."),
        onBack
    ) { _ ->
        NoticeCard(tr(lang,
            "Regla: no conviertas automáticamente una frase coloquial en diagnóstico. Primero registra el literal entre comillas; después describe fecha de inicio, desencadenante, evolución, signos, síntomas, factores que alivian/agravan, medicamentos usados y hallazgos clínicos.",
            "Rule: do not automatically convert a colloquial complaint into a diagnosis. First record the exact quote; then document onset, trigger, course, signs, symptoms, relieving/aggravating factors, medicines used and clinical findings."
        ))
        ResponsiveSectionV17(tr(lang, "Práctica de redacción", "Writing practice")) {
            OutlinedTextField(literal, { literal = it }, label = { Text(tr(lang, "Motivo de consulta · literal", "Chief complaint · exact words")) }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            OutlinedTextField(clinical, { clinical = it }, label = { Text(tr(lang, "Padecimiento actual · lenguaje clínico", "Present illness · clinical language")) }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        }
        ResponsiveSectionV17(tr(lang, "5 ejemplos", "5 examples")) {
            complaintExamplesV37.forEachIndexed { index, example ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .35f))
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("${index + 1}. ${example.literal}", fontWeight = FontWeight.Bold)
                        Text("→ ${example.clinical}")
                    }
                }
            }
        }
    }
}

@Composable
internal fun FamilyHistoryV37Screen(lang: String, onBack: () -> Unit) {
    var relative by remember { mutableStateOf(familyRelativesV37.first()) }
    val diseasesByRelative = remember { mutableStateMapOf<String, Set<String>>() }
    val notesByRelative = remember { mutableStateMapOf<String, String>() }

    ResponsiveScreenV17(
        tr(lang, "Antecedentes heredo-familiares", "Family history"),
        tr(lang, "Selecciona un familiar y marca todas las categorías que correspondan. Después agrega enfermedad específica, inicio/evolución cuando se conozcan, estado actual, medicamentos y complicaciones.", "Select a relative and mark every applicable disease category. Then add the specific disease, known onset/course, current status, medicines and complications."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang, "Familiar", "Relative")) {
            AdaptiveGridV17(familyRelativesV37.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val item = familyRelativesV37[index]
                FilterChip(selected = relative == item, onClick = { relative = item }, label = { Text(item) }, modifier = Modifier.fillMaxWidth())
            }
        }
        ResponsiveSectionV17("${tr(lang, "Enfermedades en", "Diseases in")} $relative") {
            AdaptiveGridV17(familyDiseaseGroupsV37.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val disease = familyDiseaseGroupsV37[index]
                val current = diseasesByRelative[relative] ?: emptySet()
                FilterChip(
                    selected = disease in current,
                    onClick = {
                        diseasesByRelative[relative] = if (disease in current) current - disease else current + disease
                    },
                    label = { Text(disease) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            OutlinedTextField(
                notesByRelative[relative].orEmpty(),
                { notesByRelative[relative] = it },
                label = { Text(tr(lang, "Detalle: enfermedad, desde cuándo, estado, medicamentos, complicaciones", "Detail: disease, since when, status, medicines, complications")) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }
        NoticeCard(tr(lang,
            "Ejemplos de redacción: “Abuelo paterno: hipertensión arterial”; “Abuela materna: diabetes mellitus”; “Hermano: asma”; “Hermana: alergia referida a penicilina”. Si no hay antecedente: negado / sin antecedentes / no referido, según el formato.",
            "Writing examples: paternal grandfather: hypertension; maternal grandmother: diabetes; brother: asthma; sister: reported penicillin allergy. If absent: denied / no history / not reported, according to the form."
        ))
    }
}

@Composable
internal fun NonPathologicalHistoryV37Screen(lang: String, onBack: () -> Unit) {
    val habits = listOf("Tabaco", "Vapeo", "Alcohol", "Cannabis", "Cocaína / estimulantes", "Otras drogas", "Perforaciones", "Tatuajes")
    val selectedHabits = remember { mutableStateListOf<String>() }

    ResponsiveScreenV17(
        tr(lang, "Antecedentes personales no patológicos", "Non-pathological personal history"),
        tr(lang, "Describe vivienda y entorno, hábitos de higiene, alimentación, autocuidado y exposiciones. No basta con “buena higiene”.", "Describe housing/environment, hygiene, diet, self-care and exposures. Do not reduce this section to “good hygiene”."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang, "Vivienda y entorno", "Housing and environment")) {
            PracticeFieldV37(lang, "Número de cuartos / personas en el hogar", "Rooms / people in household")
            PracticeFieldV37(lang, "Material de paredes", "Wall material")
            PracticeFieldV37(lang, "Material del piso", "Floor material")
            PracticeFieldV37(lang, "Material del techo", "Roof material")
            PracticeFieldV37(lang, "Servicios: agua, drenaje, luz, gas, internet", "Utilities: water, drainage, electricity, gas, internet")
            PracticeFieldV37(lang, "Ventilación, humedad/moho, polvo, humo de leña u otras exposiciones", "Ventilation, damp/mold, dust, wood smoke or other exposures")
        }
        ResponsiveSectionV17(tr(lang, "Higiene general", "General hygiene")) {
            PracticeFieldV37(lang, "Baño: cuántas veces por semana", "Bathing: times per week")
            PracticeFieldV37(lang, "Cambio de ropa: frecuencia", "Clothing changes: frequency")
            PracticeFieldV37(lang, "Lavado de ropa: frecuencia", "Laundry: frequency")
            PracticeFieldV37(lang, "Lavado de manos / cuidado personal", "Hand washing / personal care")
        }
        ResponsiveSectionV17(tr(lang, "Higiene bucal", "Oral hygiene")) {
            PracticeFieldV37(lang, "Cepillado: veces al día y técnica", "Brushing: times per day and technique")
            PracticeFieldV37(lang, "Pasta dental: fluorada / concentración si la conoce", "Toothpaste: fluoridated / concentration if known")
            PracticeFieldV37(lang, "Hilo dental / cepillos interproximales", "Floss / interdental brushes")
            PracticeFieldV37(lang, "Enjuague bucal y frecuencia", "Mouthrinse and frequency")
            PracticeFieldV37(lang, "En niños: quién supervisa o ayuda", "For children: who supervises or assists")
            PracticeFieldV37(lang, "Última visita odontológica", "Last dental visit")
        }
        ResponsiveSectionV17(tr(lang, "Alimentación", "Diet")) {
            PracticeFieldV37(lang, "Número de comidas al día", "Meals per day")
            PracticeFieldV37(lang, "Frecuencia de dulces, refrescos y alimentos ultraprocesados", "Frequency of sweets, soft drinks and ultra-processed foods")
            PracticeFieldV37(lang, "Frecuencia de fruta, verdura, lácteos, huevo, carne y leguminosas", "Frequency of fruit, vegetables, dairy, eggs, meat and legumes")
        }
        ResponsiveSectionV17(tr(lang, "Hábitos, exposiciones y toxicomanías", "Habits, exposures and substance use")) {
            AdaptiveGridV17(habits.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val habit = habits[index]
                FilterChip(
                    selected = habit in selectedHabits,
                    onClick = { if (habit in selectedHabits) selectedHabits.remove(habit) else selectedHabits.add(habit) },
                    label = { Text(habit) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            PracticeFieldV37(lang, "Para cada positivo: desde cuándo, frecuencia, cantidad, vía y si suspendió, cuándo", "For each positive item: since when, frequency, amount, route and, if stopped, when")
            PracticeFieldV37(lang, "Perforaciones/tatuajes: sitio, antigüedad y complicaciones", "Piercings/tattoos: site, age and complications")
        }
    }
}

@Composable
internal fun PathologicalHistoryV37Screen(lang: String, onBack: () -> Unit) {
    val selectedVaccines = remember { mutableStateListOf<String>() }
    var selectedGroupIndex by remember { mutableStateOf(0) }
    var selectedDisease by remember { mutableStateOf("") }
    var selectedMedicine by remember { mutableStateOf("") }
    var sinceWhen by remember { mutableStateOf("") }
    var reportedDose by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var interval by remember { mutableStateOf("") }
    var currentStatus by remember { mutableStateOf("") }
    var complications by remember { mutableStateOf("") }
    val intervals = listOf("cada 4 h", "cada 6 h", "cada 8 h", "cada 12 h", "cada 24 h", "según necesidad", "otro")

    ResponsiveScreenV17(
        tr(lang, "Antecedentes personales patológicos", "Pathological personal history"),
        tr(lang, "Registra lo que el paciente realmente padece y el tratamiento que realmente refiere. Las opciones de fármacos sirven para reconocer nombres frecuentes, no para prescribir tratamiento sistémico.", "Record conditions the patient actually has and treatment actually reported. Medicine options help recognize common names; they are not systemic-treatment prescribing recommendations."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang, "Vacunación · lista seleccionable", "Vaccination · selectable checklist")) {
            Text(tr(lang,
                "Marca sólo las vacunas documentadas o referidas; cuando sea posible confirma en la Cartilla Nacional de Salud.",
                "Mark only vaccines documented or reported; when possible verify them in the National Health Card."
            ))
            AdaptiveGridV17(vaccinesV37.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val vaccine = vaccinesV37[index]
                FilterChip(
                    selected = vaccine in selectedVaccines,
                    onClick = { if (vaccine in selectedVaccines) selectedVaccines.remove(vaccine) else selectedVaccines.add(vaccine) },
                    label = { Text(vaccine) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResponsiveSectionV17(tr(lang, "Enfermedades por categoría", "Diseases by category")) {
            AdaptiveGridV17(diseaseGroupsV37.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val group = diseaseGroupsV37[index]
                FilterChip(
                    selected = selectedGroupIndex == index,
                    onClick = {
                        selectedGroupIndex = index
                        selectedDisease = ""
                        selectedMedicine = ""
                    },
                    label = { Text(if (lang == "en") group.en else group.es) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        val group = diseaseGroupsV37[selectedGroupIndex]
        val diseases = if (lang == "en") group.diseasesEn else group.diseasesEs
        val medicines = if (lang == "en") group.medicinesEn else group.medicinesEs

        ResponsiveSectionV17(if (lang == "en") group.en else group.es) {
            Text(tr(lang, "Selecciona la enfermedad referida", "Select the reported disease"), fontWeight = FontWeight.Bold)
            AdaptiveGridV17(diseases.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val disease = diseases[index]
                FilterChip(selected = selectedDisease == disease, onClick = { selectedDisease = disease }, label = { Text(disease) }, modifier = Modifier.fillMaxWidth())
            }
            OutlinedTextField(sinceWhen, { sinceWhen = it }, label = { Text(tr(lang, "¿Desde cuándo la tiene? / fecha de diagnóstico", "Since when? / diagnosis date")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(currentStatus, { currentStatus = it }, label = { Text(tr(lang, "Estado actual / control / última crisis o descompensación", "Current status / control / last exacerbation")) }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        }

        ResponsiveSectionV17(tr(lang, "Medicamento y esquema que REFIERE el paciente", "Medicine and regimen REPORTED by the patient")) {
            NoticeCard(tr(lang,
                "No se generan dosis para enfermedades sistémicas. El alumno debe documentar exactamente nombre, presentación, dosis, vía, intervalo y duración que el paciente refiere o que constan en receta/envase.",
                "No systemic-disease doses are generated. Document exactly the name, strength, dose, route, interval and duration the patient reports or that appear on the prescription/container."
            ))
            AdaptiveGridV17(medicines.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val medicine = medicines[index]
                FilterChip(selected = selectedMedicine == medicine, onClick = { selectedMedicine = medicine }, label = { Text(medicine) }, modifier = Modifier.fillMaxWidth())
            }
            OutlinedTextField(reportedDose, { reportedDose = it }, label = { Text(tr(lang, "Presentación / dosis referida", "Reported strength / dose")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(route, { route = it }, label = { Text(tr(lang, "Vía referida", "Reported route")) }, modifier = Modifier.fillMaxWidth())
            Text(tr(lang, "Intervalo referido", "Reported interval"), fontWeight = FontWeight.Bold)
            AdaptiveGridV17(intervals.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val option = intervals[index]
                FilterChip(selected = interval == option, onClick = { interval = option }, label = { Text(option) }, modifier = Modifier.fillMaxWidth())
            }
            OutlinedTextField(complications, { complications = it }, label = { Text(tr(lang, "Complicaciones, hospitalizaciones relacionadas o efectos adversos", "Complications, related hospitalizations or adverse effects")) }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        }
    }
}

@Composable
internal fun SurgicalTraumaHistoryV37Screen(lang: String, onBack: () -> Unit) {
    val items = listOf(
        "Hospitalización previa", "Cirugía previa", "Transfusión recibida", "Donación de sangre",
        "Fractura", "Traumatismo importante", "Trauma dental/maxilofacial", "Complicación con anestesia",
        "Implante/prótesis/dispositivo médico", "Ingreso a urgencias por evento grave"
    )
    val selected = remember { mutableStateListOf<String>() }
    var bloodGroup by remember { mutableStateOf("") }
    val bloodGroups = listOf("A+", "A−", "B+", "B−", "AB+", "AB−", "O+", "O−", "Desconocido")

    ResponsiveScreenV17(
        tr(lang, "Antecedentes quirúrgicos y traumáticos", "Surgical and trauma history"),
        tr(lang, "Registra eventos previos, fechas aproximadas, motivo, complicaciones, transfusiones y antecedentes de anestesia. Incluye grupo sanguíneo si está documentado.", "Record previous events, approximate dates, reason, complications, transfusions and anesthesia history. Include blood group if documented."),
        onBack
    ) { profile ->
        ResponsiveSectionV17(tr(lang, "Eventos", "Events")) {
            AdaptiveGridV17(items.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 2) { index ->
                val item = items[index]
                FilterChip(
                    selected = item in selected,
                    onClick = { if (item in selected) selected.remove(item) else selected.add(item) },
                    label = { Text(item) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            PracticeFieldV37(lang, "Detalle de cada positivo: qué ocurrió, cuándo, por qué, tratamiento y complicaciones", "For each positive item: what happened, when, why, treatment and complications")
        }
        ResponsiveSectionV17(tr(lang, "Sangre y transfusiones", "Blood and transfusions")) {
            Text(tr(lang, "Grupo y Rh documentados", "Documented ABO/Rh"), fontWeight = FontWeight.Bold)
            AdaptiveGridV17(bloodGroups.size, if (profile.largeSystemText || profile.width == ScreenWidthV17.COMPACT) 1 else 3) { index ->
                val group = bloodGroups[index]
                FilterChip(selected = bloodGroup == group, onClick = { bloodGroup = group }, label = { Text(group) }, modifier = Modifier.fillMaxWidth())
            }
            PracticeFieldV37(lang, "Si recibió sangre: cuándo, motivo y si hubo reacción", "If blood was received: when, why and whether there was a reaction")
            PracticeFieldV37(lang, "Si donó sangre: cuándo y si hubo complicaciones", "If blood was donated: when and whether there were complications")
        }
        ResponsiveSectionV17(tr(lang, "Trauma y anestesia", "Trauma and anesthesia")) {
            PracticeFieldV37(lang, "Fracturas: hueso, fecha, tratamiento y secuelas", "Fractures: bone, date, treatment and sequelae")
            PracticeFieldV37(lang, "Trauma dental/maxilofacial: OD o zona, fecha, manejo y secuelas", "Dental/maxillofacial trauma: tooth/area, date, management and sequelae")
            PracticeFieldV37(lang, "Anestesia previa: tipo y reacciones/complicaciones", "Previous anesthesia: type and reactions/complications")
        }
    }
}

@Composable
private fun PracticeFieldV37(lang: String, es: String, en: String) {
    var value by remember(es) { mutableStateOf("") }
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(if (lang == "en") en else es) },
        modifier = Modifier.fillMaxWidth()
    )
}
