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

private data class PathologicalSystemV54(
    val es: String,
    val en: String,
    val imageName: String,
    val imageKindEs: String,
    val imageKindEn: String,
    val source: String,
    val license: String,
    val examplesEs: String,
    val examplesEn: String,
    val dentalEs: String,
    val dentalEn: String,
    val clinicalSource: String
)

private val pathologicalSystemsV54 = listOf(
    PathologicalSystemV54("Gastrointestinales / hepáticas","Gastrointestinal / hepatic","pathological_digestive_niddk","Diagrama anatómico educativo; no diagnostica por apariencia.","Educational anatomy diagram; it does not diagnose by appearance.","NIDDK/NIH Media Library · N00039-H","NIDDK/NIH: recurso educativo reutilizable con crédito a NIDDK/NIH.","ERGE, celiaquía, Crohn, colitis ulcerosa, hepatitis y cirrosis.","GERD, celiac disease, Crohn disease, ulcerative colitis, hepatitis and cirrhosis.","ERGE: erosión dental; EII: úlceras/cambios de mucosa; hepatopatía: revisar sangrado, fármacos y estado médico.","GERD: dental erosion; IBD: ulcers/mucosal changes; liver disease: review bleeding, medicines and medical status.","NIDDK · Digestive Diseases; ADA/AAOM principles for medical history."),
    PathologicalSystemV54("Reumatológicas / autoinmunes","Rheumatologic / autoimmune","pathological_rheum_schematic","Esquema original de articulaciones; las fotografías se reservan para signos visibles reales.","Original joint schematic; photographs are reserved for genuinely visible signs.","Ilustración educativa original de la app.","Recurso original del proyecto; uso educativo dentro de la app.","Artritis reumatoide, lupus, Sjögren, esclerodermia y espondiloartritis.","Rheumatoid arthritis, lupus, Sjögren disease, scleroderma and spondyloarthritis.","Xerostomía, úlceras, limitación de apertura/ATM y tratamientos inmunomoduladores.","Xerostomia, ulcers, limited opening/TMJ and immunomodulatory treatment.","NIAMS/NIH · Rheumatic Diseases; NIDCR · Sjögren disease."),
    PathologicalSystemV54("Cardiovasculares","Cardiovascular","pathological_cardio_schematic","Esquema cardiaco/ECG: hipertensión y arritmias no se identifican por una fotografía facial.","Heart/ECG schematic: hypertension and arrhythmias cannot be identified from a facial photograph.","Ilustración educativa original de la app.","Recurso original del proyecto; uso educativo dentro de la app.","Hipertensión, cardiopatía isquémica, insuficiencia cardiaca, valvulopatías y arritmias.","Hypertension, ischemic heart disease, heart failure, valvular disease and arrhythmias.","Registrar presión, capacidad funcional, anticoagulantes/antiagregantes y antecedentes cardiovasculares relevantes.","Record blood pressure, functional capacity, anticoagulants/antiplatelets and relevant cardiovascular history.","American Heart Association · cardiovascular conditions; ADA · medical history."),
    PathologicalSystemV54("Endocrinas / metabólicas","Endocrine / metabolic","pathological_endocrine_niddk","Diagrama endocrino educativo; evita estereotipos corporales.","Educational endocrine diagram; avoids body stereotypes.","NIDDK/NIH Media Library · N01124-H","NIDDK/NIH: recurso educativo reutilizable con crédito a NIDDK/NIH.","Diabetes, hipo/hipertiroidismo y trastornos suprarrenales/metabólicos.","Diabetes, hypo/hyperthyroidism and adrenal/metabolic disorders.","Diabetes: periodonto, infección y cicatrización; tiroides/suprarrenal: control clínico y medicación.","Diabetes: periodontium, infection and healing; thyroid/adrenal: clinical control and medication.","NIDDK · Diabetes and endocrine diseases."),
    PathologicalSystemV54("Respiratorias","Respiratory","pathological_respiratory_schematic","Esquema pulmonar original; una foto de la cara no identifica asma, EPOC o fibrosis.","Original lung schematic; a facial photo does not identify asthma, COPD or fibrosis.","Ilustración educativa original de la app.","Recurso original del proyecto; uso educativo dentro de la app.","Asma, EPOC, tuberculosis, apnea obstructiva del sueño y fibrosis pulmonar.","Asthma, COPD, tuberculosis, obstructive sleep apnea and pulmonary fibrosis.","Inhaladores, xerostomía, candidiasis, respiración oral y tolerancia a la posición del sillón.","Inhalers, xerostomia, candidiasis, mouth breathing and tolerance of dental-chair position.","NHLBI/NIH · Lung Diseases; CDC · Tuberculosis."),
    PathologicalSystemV54("Renales / urinarias","Renal / urinary","pathological_kidney_niddk","Anatomía renal educativa; no atribuye un aspecto físico específico a la enfermedad renal.","Educational kidney anatomy; does not assign a specific appearance to kidney disease.","NIDDK/NIH Media Library · N01055-H","NIDDK/NIH: recurso educativo reutilizable con crédito a NIDDK/NIH.","Enfermedad renal crónica, diálisis, trasplante renal y litiasis.","Chronic kidney disease, dialysis, kidney transplant and nephrolithiasis.","Revisar anemia/sangrado, fármacos, diálisis, trasplante y necesidad de coordinación médica.","Review anemia/bleeding, medicines, dialysis, transplant and need for medical coordination.","NIDDK · Kidney Diseases."),
    PathologicalSystemV54("Hematológicas","Hematologic","pathological_blood_schematic","Esquema hematológico original; los signos visibles apoyan, pero no establecen por sí solos el diagnóstico.","Original hematology schematic; visible signs may support but do not establish diagnosis alone.","Ilustración educativa original de la app.","Recurso original del proyecto; uso educativo dentro de la app.","Anemias, hemofilia, von Willebrand, trastornos plaquetarios y leucemias.","Anemias, hemophilia, von Willebrand disease, platelet disorders and leukemias.","Sangrado, petequias, palidez, infecciones y tratamiento hematológico son relevantes antes de procedimientos invasivos.","Bleeding, petechiae, pallor, infections and hematologic treatment matter before invasive procedures.","NHLBI/NIH · Blood Disorders."),
    PathologicalSystemV54("Neurológicas","Neurologic","pathological_neuro_schematic","Esquema neurológico original; no representa epilepsia, Parkinson o EVC mediante apariencia facial.","Original neurologic schematic; does not represent epilepsy, Parkinson disease or stroke by facial appearance.","Ilustración educativa original de la app.","Recurso original del proyecto; uso educativo dentro de la app.","Epilepsia, Parkinson, EVC, esclerosis múltiple y neuropatías.","Epilepsy, Parkinson disease, stroke, multiple sclerosis and neuropathies.","Medicamentos, deglución/coordinación, trauma por crisis, xerostomía e hiperplasia gingival cuando corresponda.","Medicines, swallowing/coordination, seizure trauma, xerostomia and gingival enlargement when relevant.","NINDS/NIH · Neurological Disorders."),
    PathologicalSystemV54("Dermatológicas","Dermatologic","clinical_psoriasis","Fotografía clínica real: útil porque la morfología y distribución cutánea sí son observables.","Real clinical photograph: useful because skin morphology and distribution are observable.","Wikimedia Commons · File:2803 Psoriasis.jpg · Dr. Gandikota Raghurama Rao","CC BY 4.0.","Psoriasis, dermatitis atópica y otras dermatosis con manifestaciones visibles.","Psoriasis, atopic dermatitis and other dermatoses with visible manifestations.","Correlacionar piel y mucosa sin convertir una lesión oral aislada en diagnóstico sistémico.","Correlate skin and mucosa without turning an isolated oral lesion into a systemic diagnosis.","American Academy of Dermatology · Psoriasis/Atopic dermatitis."),
    PathologicalSystemV54("Infecciosas / exantemáticas","Infectious / exanthematous","clinical_measles","Fotografía clínica real para comparar morfología y distribución del exantema.","Real clinical photograph to compare rash morphology and distribution.","CDC Public Health Image Library (PHIL), ID 4497","Dominio público (CDC PHIL).","Sarampión, rubéola, varicela y otras infecciones relevantes.","Measles, rubella, varicella and other relevant infections.","Buscar lesiones orales características cuando existan y registrar vacunación/exposición.","Look for characteristic oral lesions when present and record vaccination/exposure.","CDC · Measles, Rubella and Varicella clinical guidance."),
    PathologicalSystemV54("Neoplásicas / oncológicas","Neoplastic / oncology","pathological_oncology_schematic","Esquema oncológico original; no se asigna una apariencia genérica a una persona con cáncer.","Original oncology schematic; no generic appearance is assigned to a person with cancer.","Ilustración educativa original de la app.","Recurso original del proyecto; uso educativo dentro de la app.","Tumores sólidos, leucemia, linfoma, quimioterapia, radioterapia y trasplante hematopoyético.","Solid tumors, leukemia, lymphoma, chemotherapy, radiotherapy and hematopoietic transplant.","Mucositis, infección, sangrado, xerostomía, osteonecrosis/riesgo óseo según terapia y coordinación con oncología.","Mucositis, infection, bleeding, xerostomia, therapy-related bone risk and oncology coordination.","NCI/NIH · Oral Complications of Cancer Therapies."),
    PathologicalSystemV54("Psiquiátricas / neurodesarrollo","Psychiatric / neurodevelopmental","pathological_neuro_schematic","Esquema cerebral educativo; no se infiere salud mental o neurodesarrollo por apariencia.","Educational brain schematic; mental health or neurodevelopment is not inferred from appearance.","Ilustración educativa original de la app.","Recurso original del proyecto; uso educativo dentro de la app.","Ansiedad, depresión, TDAH, autismo, bipolaridad, psicosis y trastornos del sueño.","Anxiety, depression, ADHD, autism, bipolar disorder, psychosis and sleep disorders.","Registrar medicación, xerostomía, bruxismo, cooperación/tolerancia y necesidades de comunicación individualizadas.","Record medicines, xerostomia, bruxism, cooperation/tolerance and individualized communication needs.","NIMH/NIH · Mental Health Information." )
)

@Composable
internal fun PathologicalV54Screen(lang: String, onBack: () -> Unit) {
    var recordOpen by remember { mutableStateOf(false) }
    if (recordOpen) {
        PathologicalHistoryV37Screen(lang) { recordOpen = false }
        return
    }
    ResponsiveScreenV17(
        tr(lang,"Antecedentes personales patológicos","Pathological personal history"),
        tr(lang,"Registro interactivo y atlas multisistémico con recursos visuales clínicamente pertinentes, fuente y licencia.","Interactive record and multisystem atlas with clinically appropriate visuals, source and license."),
        onBack
    ) { profile ->
        Card(onClick={recordOpen=true},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer),shape=RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                Text(tr(lang,"📝 Abrir registro interactivo de antecedentes","📝 Open interactive history record"),fontWeight=FontWeight.Black)
                Text(tr(lang,"Vacunación, enfermedades, evolución, estado actual y medicamentos referidos.","Vaccination, diseases, course, current status and reported medicines."))
            }
        }
        NoticeCard(tr(lang,"Regla visual: fotografía real sólo cuando existe un signo observable pertinente. Para enfermedades internas o diagnósticos que no deben inferirse por apariencia se utiliza anatomía o un esquema educativo.","Visual rule: use a real photograph only when there is a relevant observable sign. Internal diseases or diagnoses that must not be inferred from appearance use anatomy or an educational schematic."))
        ResponsiveSectionV17(tr(lang,"Atlas visual multisistémico","Multisystem visual atlas")) {
            AdaptiveGridV17(pathologicalSystemsV54.size,if(profile.largeSystemText || profile.width==ScreenWidthV17.COMPACT)1 else 2) { i -> PathologicalSystemCardV54(pathologicalSystemsV54[i],lang) }
        }
        NoticeCard(tr(lang,"Las imágenes enseñan patrones y anatomía; no sustituyen interrogatorio, exploración, pruebas complementarias ni valoración médica. La app no debe usarse para diagnosticar una enfermedad sistémica por fotografía.","Images teach patterns and anatomy; they do not replace history, examination, complementary tests or medical assessment. The app must not be used to diagnose systemic disease from a photograph."))
    }
}

@Composable
private fun PathologicalSystemCardV54(item: PathologicalSystemV54, lang: String) {
    val context=LocalContext.current
    val id=context.resources.getIdentifier(item.imageName,"drawable",context.packageName)
    Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),shape=RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(7.dp)) {
            Text(if(lang=="en")item.en else item.es,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
            if(id!=0) Image(painterResource(id),contentDescription=if(lang=="en")item.en else item.es,modifier=Modifier.fillMaxWidth().height(150.dp),contentScale=ContentScale.Fit)
            else Text(tr(lang,"Recurso visual pendiente de empaquetado; la compilación no debe publicarse hasta incorporarlo.","Visual resource pending packaging; the build must not be published until it is included."),color=MaterialTheme.colorScheme.error,fontWeight=FontWeight.Bold)
            Text(if(lang=="en")item.imageKindEn else item.imageKindEs,fontWeight=FontWeight.SemiBold)
            Text(if(lang=="en")item.examplesEn else item.examplesEs)
            Text(tr(lang,"Relevancia odontológica","Dental relevance"),fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.primary)
            Text(if(lang=="en")item.dentalEn else item.dentalEs)
            Text("Fuente visual: ${item.source}",style=MaterialTheme.typography.bodySmall)
            Text("Licencia/crédito: ${item.license}",style=MaterialTheme.typography.bodySmall)
            Text("Fuente clínica: ${item.clinicalSource}",style=MaterialTheme.typography.bodySmall)
        }
    }
}
