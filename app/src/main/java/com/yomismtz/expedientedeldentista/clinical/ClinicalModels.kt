package com.yomismtz.expedientedeldentista.clinical

enum class AppScreen {
    HOME, FOLDER, SETTINGS,
    IDENTIFICATION, HISTORY, INTAKE, ACTIVITIES, VITALS, ATM, OCCLUSION, MUCOSA, AUXILIARIES,
    ODONTOGRAM, ICDAS, CPOD, OLEARY, IPC, IHOS, PERIODONTOGRAM, POSTURE,
    PULPAL, APICAL, TREATMENT, SESSIONS, ENDO, PROSTHETIC, SURGICAL,
    CONSENT, REQUEST, BUDGET, EVOLUTION;

    /**
     * Compatibility aliases kept for the first V37 history menu implementation.
     * They are not enum entries, so legacy exhaustive `when(AppScreen)` blocks remain source-compatible.
     * The active V38 history router uses its own HistorySectionV38 enum and does not depend on these aliases.
     */
    companion object {
        val HISTORY_REASON: AppScreen get() = HISTORY
        val HISTORY_FAMILY: AppScreen get() = HISTORY
        val HISTORY_NONPATH: AppScreen get() = HISTORY
        val HISTORY_PATHOLOGICAL: AppScreen get() = HISTORY
        val HISTORY_SURGICAL_TRAUMA: AppScreen get() = HISTORY
    }
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

data class GeneralInspectionState(
    val apparentAge: String = "",
    val gait: String = "",
    val facies: String = "",
    val attitude: String = "",
    val constitution: String = "",
    val abnormalMovements: String = "",
    val consciousness: String = "",
    val psychologicalAttitude: String = "",
    val personalCare: String = "",
    val cooperation: String = "",
    val notes: String = ""
)

data class LymphNodeState(
    val status: String = "",
    val side: String = "",
    val sizeMm: String = "",
    val tenderness: String = "",
    val consistency: String = "",
    val mobility: String = "",
    val notes: String = ""
)

data class HeadNeckExplorationState(
    val craniumObservation: String = "",
    val cranialWidthMm: String = "",
    val cranialLengthMm: String = "",
    val headCircumferenceCm: String = "",
    val hairline: String = "",
    val cranialPalpation: String = "",
    val faceProfile: String = "",
    val faceSymmetry: String = "",
    val facialSkin: String = "",
    val facialMuscleFunction: String = "",
    val facialMuscleTone: String = "",
    val masticatoryMuscleFinding: String = "",
    val masticatoryMuscleTone: String = "",
    val neckInspection: String = "",
    val neckMobility: String = "",
    val neckTenderness: String = "",
    val lymphNodes: Map<String, LymphNodeState> = emptyMap(),
    val notes: Map<String, String> = emptyMap()
)

data class AtmExamState(
    val openingMm: String = "",
    val rightLateralityMm: String = "",
    val leftLateralityMm: String = "",
    val protrusionMm: String = "",
    val retrusionFinding: String = "",
    val openingPath: String = "",
    val pain: Boolean = false,
    val click: Boolean = false,
    val crepitus: Boolean = false,
    val locking: Boolean = false,
    val rightTenderness: Boolean = false,
    val leftTenderness: Boolean = false,
    val dvoMm: String = "",
    val dvrMm: String = "",
    val notes: String = ""
)

/** Fast chairside symptom record. Mirrors key history questions without replacing the full history. */
data class QuickSignsSymptomsState(
    val painStatus: String = "",
    val location: String = "",
    val onset: String = "",
    val evolution: String = "",
    val intensity: String = "",
    val character: String = "",
    val triggers: String = "",
    val relief: String = "",
    val swelling: String = "",
    val bleeding: String = "",
    val fever: String = "",
    val limitedOpening: String = "",
    val alteredSensation: String = "",
    val dysphagia: String = "",
    val notes: String = ""
)

data class MucosaFindingState(
    val findingType: String = "",
    val sizeMm: String = "",
    val color: String = "",
    val surface: String = "",
    val borders: String = "",
    val consistency: String = "",
    val symptoms: String = "",
    val notes: String = ""
)

data class ProstheticState(
    val upperPresent: Set<Int> = setOf(17,16,15,14,13,12,11,21,22,23,24,25,26,27),
    val lowerPresent: Set<Int> = setOf(47,46,45,44,43,42,41,31,32,33,34,35,36,37),
    val upperIgnored: Set<Int> = emptySet(),
    val lowerIgnored: Set<Int> = emptySet()
)

data class EducationalSession(
    val profile: PatientProfile = PatientProfile(),
    val history: HistoryState = HistoryState(),
    val generalInspection: GeneralInspectionState = GeneralInspectionState(),
    val headNeckExploration: HeadNeckExplorationState = HeadNeckExplorationState(),
    val atmExam: AtmExamState = AtmExamState(),
    val quickSignsSymptoms: QuickSignsSymptomsState = QuickSignsSymptomsState(),
    val mucosaFindings: Map<String, MucosaFindingState> = emptyMap(),
    val teeth: Map<Int, ToothRecord> = emptyMap(),
    val odontogramSurfaces: Map<Int, Map<Surface, SurfaceMark>> = emptyMap(),
    val icdasSurfaces: Map<Int, Map<Surface, Int>> = emptyMap(),
    val oleary: Map<Int, Set<Surface>> = emptyMap(),
    val olearyPresentTeeth: Set<Int> = emptySet(),
    val olearyPermanentInitialized: Boolean = false,
    val olearyPrimaryInitialized: Boolean = false,
    val presentTeeth: Set<Int> = emptySet(),
    val ipcCodes: List<String> = List(6) { "0" },
    val ipcSiteCodes: Map<Int, List<String>> = emptyMap(),
    val cpiBleedingByTooth: Map<Int, Int> = emptyMap(),
    val cpiPocketByTooth: Map<Int, Int> = emptyMap(),
    val ihosDebris: Map<Int, Int> = emptyMap(),
    val ihosCalculus: Map<Int, Int> = emptyMap(),
    val ihosSelections: Map<Int, Int> = emptyMap(),
    val ihosExcludedSlots: Set<Int> = emptySet(),
    val periodontogram: Map<Int, PerioRecord> = emptyMap(),
    val pulpal: PulpalAssessment = PulpalAssessment(),
    val prosthetic: ProstheticState = ProstheticState()
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
