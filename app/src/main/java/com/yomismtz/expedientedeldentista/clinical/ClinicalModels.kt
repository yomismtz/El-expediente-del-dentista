package com.yomismtz.expedientedeldentista.clinical

enum class AppScreen {
    HOME, FOLDER, SETTINGS,
    IDENTIFICATION, HISTORY, HISTORY_IDENTIFICATION, HISTORY_REASON, HISTORY_HEREDITARY, HISTORY_NONPATH, HISTORY_GYNECO, HISTORY_PATH, HISTORY_SURGICAL_TRAUMA, HISTORY_PHYSICAL, HISTORY_ORTHO, HISTORY_DENTAL_ALTERATIONS, HISTORY_HABITS, HISTORY_ORAL_EXAM, SYSTEMIC_PROTOCOLS, INTAKE, ACTIVITIES, VITALS, ATM, OCCLUSION, MUCOSA, AUXILIARIES, CALCULATORS,
    ODONTOGRAM, ICDAS, CPOD, OLEARY, IPC, IHOS, PERIODONTOGRAM, POSTURE,
    PULPAL, APICAL, TREATMENT, SESSIONS, ENDO, PROSTHETIC, SURGICAL,
    CONSENT, REQUEST, BUDGET, EVOLUTION
}

enum class Surface { VESTIBULAR, LINGUAL_PALATAL, MESIAL, DISTAL, OCCLUSAL }

enum class SurfaceMark { HEALTHY, CARIES, RESTORATION, SEALANT }

enum class ToothStatus {
    HEALTHY, CARIES, RESTORED, MISSING_CARIES, MISSING_OTHER, EXTRACTION_INDICATED, SEALANT
}

data class PatientProfile(
    val exerciseName: String = "",
    val age: String = "",
    val sex: String = "",
    val birthDate: String = "",
    val occupation: String = "",
    val reasonForVisit: String = "",
    val currentCondition: String = "",
    val medications: String = "",
    val allergies: String = "",
    val bloodPressure: String = "",
    val heartRate: String = "",
    val respiratoryRate: String = "",
    val temperature: String = ""
)

data class DiseaseAnswer(
    val present: Boolean = false,
    val onset: String = "",
    val treatment: String = "",
    val currentStatus: String = "",
    val complications: String = ""
)

data class HistoryState(
    val diseases: Map<String, DiseaseAnswer> = emptyMap(),
    val asaClass: Int = 1,
    val asaEmergency: Boolean = false,
    val tobaccoAlcohol: String = "",
    val hospitalizations: String = "",
    val pregnancy: String = ""
)

data class ToothRecord(
    val status: ToothStatus = ToothStatus.HEALTHY,
    val icdas: Int = 0,
    val diagnosisId: String? = null,
    val treatmentId: String? = null
)

data class PerioRecord(
    val probingDepths: List<Int> = List(6) { 0 },
    val bleeding: Boolean = false,
    val plaque: Boolean = false,
    val suppuration: Boolean = false,
    val mobility: Int = 0,
    val furcation: Int = 0,
    val recessionMm: Int = 0
)

data class PulpalAssessment(
    val tooth: Int = 0,
    val spontaneousPain: Boolean = false,
    val nightPain: Boolean = false,
    val coldPositive: Boolean = false,
    val coldLingering: Boolean = false,
    val heatPositive: Boolean = false,
    val sweetsPain: Boolean = false,
    val percussionPain: Boolean = false,
    val palpationPain: Boolean = false,
    val swelling: Boolean = false,
    val fistula: Boolean = false,
    val sensitivityNegative: Boolean = false,
    val apicalRadiolucency: Boolean = false,
    val widenedPdl: Boolean = false,
    val apicalRadiopacity: Boolean = false,
    val deepCariesOrExposure: Boolean = false,
    val previousRootCanal: Boolean = false,
    val previousPartialEndo: Boolean = false
)

data class EducationalSession(
    val profile: PatientProfile = PatientProfile(),
    val history: HistoryState = HistoryState(),
    val teeth: Map<Int, ToothRecord> = emptyMap(),
    val odontogramSurfaces: Map<Int, Map<Surface, SurfaceMark>> = emptyMap(),
    val icdasSurfaces: Map<Int, Map<Surface, Int>> = emptyMap(),
    val oleary: Map<Int, Set<Surface>> = emptyMap(),
    val presentTeeth: Set<Int> = emptySet(),
    val ipcCodes: List<String> = List(6) { "0" },
    val ihosDebris: Map<Int, Int> = emptyMap(),
    val ihosCalculus: Map<Int, Int> = emptyMap(),
    val periodontogram: Map<Int, PerioRecord> = emptyMap(),
    val pulpal: PulpalAssessment = PulpalAssessment()
)

data class IndexResult(
    val carious: Int,
    val missing: Int,
    val filled: Int,
    val total: Int
)

data class DiagnosisResult(
    val pulpalEs: String,
    val pulpalEn: String,
    val apicalEs: String,
    val apicalEn: String,
    val explanationEs: String,
    val explanationEn: String
)

data class TreatmentOption(
    val id: String,
    val labelEs: String,
    val labelEn: String,
    val explanationEs: String,
    val explanationEn: String,
    val preferred: Boolean = false
)

data class TreatmentPlan(
    val id: String,
    val diagnosisEs: String,
    val diagnosisEn: String,
    val options: List<TreatmentOption>
)
