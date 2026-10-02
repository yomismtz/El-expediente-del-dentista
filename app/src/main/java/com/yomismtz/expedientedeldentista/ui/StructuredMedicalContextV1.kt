package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

@Composable
fun StructuredMedicalContextV1(
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    val p = session.profile
    SectionCard("1 · Contexto médico estructurado") {
        Text(
            "Registra antecedentes que pueden modificar la evaluación de riesgo, los medicamentos y la anestesia. Se guarda en el expediente y no sustituye la valoración clínica.",
            style = MaterialTheme.typography.bodySmall
        )

        Text("Antecedentes cardiovasculares", fontWeight = FontWeight.Bold)
        val cardio = listOf(
            "Sin antecedente conocido",
            "Cardiopatía estable referida",
            "Arritmia referida",
            "Hipertensión / control cardiovascular",
            "Antecedente cardiovascular relevante",
            "Síntomas actuales: dolor torácico / disnea / síncope",
            "No sabe / no recuerda"
        )
        ChipChoices(cardio.map { it to (p.cardiovascularHistory == it) }, { i ->
            onSessionChanged(session.copy(profile = p.copy(cardiovascularHistory = cardio[i])))
        }, columns = 2)

        Text("Anticoagulantes / antiagregantes", fontWeight = FontWeight.Bold)
        val bloodMeds = listOf(
            "Ninguno referido",
            "Anticoagulante",
            "Antiagregante",
            "Anticoagulante + antiagregante",
            "No sabe / no recuerda"
        )
        ChipChoices(bloodMeds.map { it to (p.anticoagulantsAntiplatelets == it) }, { i ->
            onSessionChanged(session.copy(profile = p.copy(anticoagulantsAntiplatelets = bloodMeds[i])))
        }, columns = 2)

        Text("Beta-bloqueadores / antiarrítmicos", fontWeight = FontWeight.Bold)
        val cardiacMeds = listOf(
            "Ninguno referido",
            "Beta-bloqueador",
            "Antiarrítmico",
            "Beta-bloqueador + antiarrítmico",
            "No sabe / no recuerda"
        )
        ChipChoices(cardiacMeds.map { it to (p.betaBlockersAntiarrhythmics == it) }, { i ->
            onSessionChanged(session.copy(profile = p.copy(betaBlockersAntiarrhythmics = cardiacMeds[i])))
        }, columns = 2)

        Text("Tratamiento de diabetes", fontWeight = FontWeight.Bold)
        val diabetes = listOf(
            "No tiene diabetes / no referida",
            "Solo alimentación / actividad física",
            "Antidiabético oral",
            "Insulina",
            "Tratamiento combinado",
            "No sabe / no recuerda"
        )
        ChipChoices(diabetes.map { it to (p.diabetesTreatment == it) }, { i ->
            onSessionChanged(session.copy(profile = p.copy(diabetesTreatment = diabetes[i])))
        }, columns = 2)

        Text("Enfermedad hepática / renal", fontWeight = FontWeight.Bold)
        val hepRenal = listOf(
            "Ninguna referida",
            "Enfermedad hepática",
            "Enfermedad renal",
            "Hepática + renal",
            "No sabe / no recuerda"
        )
        ChipChoices(hepRenal.map { it to (p.hepaticRenalDisease == it) }, { i ->
            onSessionChanged(session.copy(profile = p.copy(hepaticRenalDisease = hepRenal[i])))
        }, columns = 2)

        Text("Embarazo", fontWeight = FontWeight.Bold)
        val pregnancy = listOf(
            "No aplica",
            "No embarazada",
            "Embarazo confirmado",
            "Posible embarazo / pendiente de confirmar",
            "Posparto",
            "No sabe / no recuerda"
        )
        ChipChoices(pregnancy.map { it to (p.pregnancyStatus == it) }, { i ->
            onSessionChanged(session.copy(profile = p.copy(pregnancyStatus = pregnancy[i])))
        }, columns = 2)

        val registered = listOf(
            p.cardiovascularHistory,
            p.anticoagulantsAntiplatelets,
            p.betaBlockersAntiarrhythmics,
            p.diabetesTreatment,
            p.hepaticRenalDisease,
            p.pregnancyStatus
        ).count { it.isNotBlank() }

        if (registered > 0) {
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text("Contexto registrado: $registered/6", fontWeight = FontWeight.Bold)
                    Text(
                        "Estos datos estarán disponibles para la evaluación clínica y anestésica posterior.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
