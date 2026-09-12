package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ActivitiesScreen(lang: String, onBack: () -> Unit) {
    val rows = listOf(
        tr(lang, "Alumno", "Student") to tr(lang, "Quién realiza la actividad clínica.", "Who performs the clinical activity."),
        tr(lang, "Actividad", "Activity") to tr(lang, "Procedimiento planeado para esa sesión.", "Procedure planned for that session."),
        tr(lang, "Autorización", "Authorization") to tr(lang, "Aprobación del docente antes de iniciar.", "Instructor approval before starting."),
        tr(lang, "Actividad realizada", "Activity performed") to tr(lang, "Lo que realmente se realizó, aunque difiera de lo planeado.", "What was actually performed, even if it differs from the plan."),
        tr(lang, "Supervisión", "Supervision") to tr(lang, "Validación final del docente después del procedimiento.", "Final instructor validation after the procedure.")
    )
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            ScreenHeader(
                tr(lang, "Autorización y registro de actividades", "Activity authorization and record"),
                onBack,
                tr(lang,
                    "La presentación usa esta hoja como bitácora operativa. En la app no se firma ni se almacena una actividad real: se enseña para qué sirve cada columna.",
                    "The presentation uses this sheet as an operational log. The app does not sign or store real activity; it teaches what each column is for.")
            )
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(tr(lang, "REGISTRO DE ACTIVIDADES", "ACTIVITY RECORD"), fontWeight = FontWeight.Bold)
                    Text("Alumno | Actividad | Autorización | Actividad realizada | Supervisión")
                    repeat(5) { Text("________ | ________ | ________ | ________ | ________") }
                }
            }
        }
        items(rows.size) { index ->
            SectionCard(rows[index].first) { Text(rows[index].second) }
        }
    }
}

@Composable
fun AtmScreen(lang: String, onBack: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, "ATM y músculos", "TMJ and muscles"), onBack) }
        item { SectionCard(tr(lang, "Imagen de referencia", "Reference image")) { AtmReferenceIllustration(lang) } }
        item {
            SectionCard(tr(lang, "¿Qué debe aprender a revisar el alumno?", "What should the student learn to examine?")) {
                Text("• ${tr(lang, "Apertura bucal y simetría del movimiento", "Mouth opening and movement symmetry")}")
                Text("• ${tr(lang, "Dolor articular o muscular", "Joint or muscle pain")}")
                Text("• ${tr(lang, "Chasquido, crepitación o bloqueo", "Clicking, crepitus or locking")}")
                Text("• ${tr(lang, "Fatiga o sensibilidad al masticar", "Fatigue or tenderness on chewing")}")
                Text("• ${tr(lang, "Relación con bruxismo y contactos oclusales", "Relation with bruxism and occlusal contacts")}")
            }
        }
        item {
            NoticeCard(tr(lang,
                "La presentación usa como referencia una apertura aproximada de 35–50 mm para ATM normal y explica que desviación, click y crepitación deben describirse clínicamente antes de asignar un diagnóstico.",
                "The presentation uses an approximate 35–50 mm opening as a normal TMJ reference and explains that deviation, clicking and crepitus should be clinically described before assigning a diagnosis."))
        }
    }
}

@Composable
fun OcclusionScreen(lang: String, onBack: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, "Examen de oclusión", "Occlusal examination"), onBack) }
        item { SectionCard(tr(lang, "Imagen de referencia", "Reference image")) { OcclusionReferenceIllustration(lang) } }
        item {
            SectionCard(tr(lang, "Rubros del formato", "Form fields")) {
                Text("• ${tr(lang, "Tipo de dentición: temporal, mixta o permanente", "Dentition: primary, mixed or permanent")}")
                Text("• ${tr(lang, "Erupción: adecuada, tardía, temprana o ectópica", "Eruption: adequate, delayed, early or ectopic")}")
                Text("• ${tr(lang, "Plano terminal derecho e izquierdo", "Right and left terminal plane")}")
                Text("• ${tr(lang, "Clasificación de Angle y relación canina", "Angle classification and canine relation")}")
                Text("• ${tr(lang, "Líneas medias en oclusión y apertura", "Midlines in occlusion and opening")}")
                Text("• Overjet / Overbite")
                Text("• ${tr(lang, "Mordida borde a borde, abierta y cruzada", "Edge-to-edge, open bite and crossbite")}")
                Text("• ${tr(lang, "Apiñamiento, giros y diastemas", "Crowding, rotations and diastemas")}")
            }
        }
        item {
            NoticeCard(tr(lang,
                "En el material, el overjet se mide horizontalmente en máxima intercuspidación y se usa 2–3 mm como referencia de normalidad.",
                "In the source material, overjet is measured horizontally in maximum intercuspation and 2–3 mm is used as the normal reference."))
        }
    }
}

@Composable
fun MucosaScreen(lang: String, onBack: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, "Examen de mucosas orales", "Oral mucosa examination"), onBack) }
        item { SectionCard(tr(lang, "Mapa visual de exploración", "Visual examination map")) { OralMucosaReferenceIllustration(lang) } }
        item {
            SectionCard(tr(lang, "Orden de observación", "Observation sequence")) {
                Text("1. ${tr(lang, "Labios y bermellón", "Lips and vermilion")}")
                Text("2. ${tr(lang, "Carrillos y mucosa yugal", "Cheeks and buccal mucosa")}")
                Text("3. ${tr(lang, "Encía y mucosa alveolar", "Gingiva and alveolar mucosa")}")
                Text("4. ${tr(lang, "Paladar duro y blando", "Hard and soft palate")}")
                Text("5. ${tr(lang, "Orofaringe, úvula, pilares y amígdalas", "Oropharynx, uvula, pillars and tonsils")}")
                Text("6. ${tr(lang, "Lengua", "Tongue")}")
                Text("7. ${tr(lang, "Piso de boca y conductos salivales", "Floor of mouth and salivary ducts")}")
            }
        }
        item {
            SectionCard(tr(lang, "Cómo describir un hallazgo", "How to describe a finding")) {
                Text(tr(lang,
                    "Color · forma · volumen · consistencia · integridad · superficie · función. Evita escribir solamente “normal”; describe lo observado.",
                    "Color · shape · volume · consistency · integrity · surface · function. Avoid writing only “normal”; describe what is observed."))
            }
        }
    }
}

@Composable
fun AuxiliariesScreen(lang: String, onBack: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, "Auxiliares de diagnóstico", "Diagnostic aids"), onBack) }
        item { SectionCard(tr(lang, "Imagen de referencia", "Reference image")) { DiagnosticAidsIllustration(lang) } }
        item {
            SectionCard(tr(lang, "Opciones que aparecen en el expediente", "Options appearing in the record")) {
                Text("• ${tr(lang, "Histopatológico", "Histopathology")}")
                Text("• ${tr(lang, "Microbiológico", "Microbiology")}")
                Text("• ${tr(lang, "Análisis clínicos", "Laboratory tests")}")
                Text("• ${tr(lang, "Modelos de estudio", "Study models")}")
                Text("• ${tr(lang, "Análisis cefalométrico", "Cephalometric analysis")}")
                Text("• ${tr(lang, "Análisis radiográfico", "Radiographic analysis")}")
                Text("• ICDAS / CAMBRA / ${tr(lang, "flujo salival / TAC / RM / otras radiografías", "salivary flow / CT / MRI / other radiographs")}")
            }
        }
        item { NoticeCard(tr(lang,
            "La lógica didáctica es: seleccionar el auxiliar porque responde una pregunta diagnóstica concreta, no marcar estudios de manera automática.",
            "Teaching logic: select an aid because it answers a concrete diagnostic question, not by routinely checking studies.")) }
    }
}

@Composable
fun SimpleEducationalSheet(
    lang: String,
    titleEs: String,
    titleEn: String,
    introEs: String,
    introEn: String,
    bulletsEs: List<String>,
    bulletsEn: List<String>,
    onBack: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang, titleEs, titleEn), onBack, tr(lang, introEs, introEn)) }
        item {
            SectionCard(tr(lang, "Qué debe reconocer el alumno", "What the student should recognize")) {
                val bullets = if (lang == "en") bulletsEn else bulletsEs
                bullets.forEach { Text("• $it") }
            }
        }
        item { NoticeCard(tr(lang,
            "Esta hoja es explicativa. No almacena firmas, presupuestos, consentimientos ni procedimientos reales.",
            "This is an explanatory sheet. It does not store signatures, budgets, consents or real procedures.")) }
    }
}
