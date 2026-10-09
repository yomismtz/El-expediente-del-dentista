package com.yomismtz.expedientedeldentista.clinical

enum class AppScreen {
    HOME, FOLDER, SECTION, SETTINGS, SUMMARY,
    IDENTIFICATION, HISTORY, HISTORY_IDENTIFICATION, HISTORY_REASON, HISTORY_HEREDITARY, HISTORY_NONPATH, HISTORY_GYNECO, HISTORY_PATH, HISTORY_SURGICAL_TRAUMA, HISTORY_PHYSICAL, HISTORY_ORTHO, HISTORY_DENTAL_ALTERATIONS, HISTORY_HABITS, HISTORY_ORAL_EXAM, MEDICATIONS, SYSTEMIC_PROTOCOLS, INTAKE, ACTIVITIES, VITALS, ATM, OCCLUSION, MUCOSA, AUXILIARIES, CALCULATORS,
    ODONTOGRAM, ICDAS, CPOD, OLEARY, IPC, IHOS, PERIODONTOGRAM, POSTURE,
    PULPAL, APICAL, TREATMENT, SESSIONS, OPERATORIA, ENDO, PROSTHETIC, SURGICAL, EMERGENCY, CLINICAL_PHOTO, PERIODONTAL_FOLLOWUP, IMPLANTOLOGY, DENTOMAXILLARY_ORTHOPEDICS, PEDIATRIC_DENTISTRY, CARIES_RISK,
    CONSENT, REQUEST, BUDGET, EVOLUTION
}

enum class Surface { VESTIBULAR, LINGUAL_PALATAL, MESIAL, DISTAL, OCCLUSAL }

enum class SurfaceMark { HEALTHY, CARIES, RESTORATION, SEALANT }

enum class ToothStatus {
    HEALTHY, CARIES, RESTORED, MISSING_CARIES, MISSING_OTHER, UNERUPTED, EXTRACTION_INDICATED, SEALANT
}

data class PatientProfile(
    val exerciseName: String = "",
    val patientInitials: String = "",
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
    val temperature: String = "",
    val spo2: String = "",
    val weightKg: String = "",
    val heightCm: String = "",
    val bmi: String = "",
    val cardiovascularHistory: String = "",
    val anticoagulantsAntiplatelets: String = "",
    val betaBlockersAntiarrhythmics: String = "",
    val diabetesTreatment: String = "",
    val hepaticRenalDisease: String = "",
    val pregnancyStatus: String = "",
    val clinicalSigns: String = "",
    val painScore: String = "",
    val glucose: String = "",
    val glucoseContext: String = ""
)

data class DiseaseAnswer(
    val present: Boolean = false,
    val onset: String = "",
    val treatment: String = "",
    val currentStatus: String = "",
    val complications: String = "",
    /** True when this condition was explicitly assessed, including an explicit negative answer. */
    val recorded: Boolean = false,
    /** Specific substance/allergen when recording an allergy. */
    val allergen: String = "",
    /** Reaction reported by the patient; do not infer from the suspected allergen. */
    val reaction: String = ""
)

data class HistoryState(
    val diseases: Map<String, DiseaseAnswer> = emptyMap(),
    /** Algorithmic educational estimate derived from recorded history; not a clinician-confirmed ASA class. */
    val asaClass: Int = 1,
    /** Clinician-confirmed ASA physical status, kept separate from the educational estimate. Null means not assessed. */
    val asaClassClinician: Int? = null,
    val asaEmergency: Boolean = false,
    val tobaccoAlcohol: String = "",
    val hospitalizations: String = "",
    val pregnancy: String = ""
)

data class ToothRecord(
    val status: ToothStatus = ToothStatus.HEALTHY,
    /** Legacy summary code retained only for backward compatibility. Use icdasSurfaceRecords for new data. */
    @Deprecated("Use EducationalSession.icdasSurfaceRecords")
    val icdas: Int = 0,
    val icdasLegacyPending: Boolean = false,
    val diagnosisId: String? = null,
    val treatmentId: String? = null
)

data class PerioRecord(
    val probingDepths: List<Int> = List(6) { 0 },
    /** Sites whose probing depth was explicitly assessed, including a valid measured value of 0 mm. */
    val probingDepthRecordedSites: Set<Int> = emptySet(),
    val bleeding: Boolean = false,
    val plaque: Boolean = false,
    val suppuration: Boolean = false,
    val mobility: Int = 0,
    /** True when mobility was explicitly assessed; grade 0 is a valid negative finding. */
    val mobilityRecorded: Boolean = false,
    val furcation: Int = 0,
    /** True when furcation was explicitly assessed; grade 0 is a valid negative finding. */
    val furcationRecorded: Boolean = false,
    val recessionMm: Int = 0,
    /** Sites whose recession/margin was explicitly assessed, including 0 mm. */
    val recessionRecordedSites: Set<Int> = emptySet(),
    val bleedingSites: Set<Int> = emptySet(),
    val plaqueSites: Set<Int> = emptySet(),
    val suppurationSites: Set<Int> = emptySet(),
    val recessionBySite: List<Int> = List(6) { 0 }
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

data class MedicationRecord(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val activeIngredient: String = "",
    val dose: String = "",
    val unit: String = "",
    val route: String = "",
    val frequency: String = "",
    val schedule: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val indication: String = "",
    val prescriber: String = "",
    val asNeeded: Boolean = false,
    val active: Boolean = true,
    val notes: String = ""
)

data class InformedConsent(
    val timestamp: Long = System.currentTimeMillis(),
    val procedure: String = "",
    val toothOrSite: String = "",
    val diagnosis: String = "",
    val benefits: String = "",
    val risks: String = "",
    val alternatives: String = "",
    val questionsAnswered: Boolean = false,
    val understood: Boolean = false,
    val accepted: Boolean = false,
    val declined: Boolean = false,
    val notes: String = "",
    val responsible: String = ""
)

data class ClinicalPhotoRecord(val slot:String,val uri:String,val purpose:String="",val notes:String="",val capturedAt:Long=System.currentTimeMillis())

data class ClinicalMeasurement(
    val timestamp: Long = System.currentTimeMillis(),
    val systolic: Int? = null,
    val diastolic: Int? = null,
    val heartRate: Int? = null,
    val respiratoryRate: Int? = null,
    val spo2: Int? = null,
    val temperature: Double? = null,
    val glucose: Int? = null,
    val weightKg: Double? = null,
    val bmi: Double? = null,
    val pain: Int? = null
)

data class ClinicalEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "",
    val detail: String = ""
)

data class EducationalSession(
    val profile: PatientProfile = PatientProfile(),
    val history: HistoryState = HistoryState(),
    val teeth: Map<Int, ToothRecord> = emptyMap(),
    val odontogramSurfaces: Map<Int, Map<Surface, SurfaceMark>> = emptyMap(),
    /** Canonical ICDAS II model: restoration/sealant, caries, or special code per surface. */
    val icdasSurfaceRecords: Map<Int, Map<Surface, IcdasSurfaceRecord>> = emptyMap(),
    /** @deprecated JSON compatibility field for records created before the explicit ICDAS model. */
    @Deprecated("Use icdasSurfaceRecords")
    val icdasSurfaces: Map<Int, Map<Surface, Int>> = emptyMap(),
    val oleary: Map<Int, Set<Surface>> = emptyMap(),
    val presentTeeth: Set<Int> = emptySet(),
    val ipcCodes: List<String> = emptyList(),
    val ihosDebris: Map<Int, Int> = emptyMap(),
    val ihosCalculus: Map<Int, Int> = emptyMap(),
    val periodontogram: Map<Int, PerioRecord> = emptyMap(),
    val pulpal: PulpalAssessment = PulpalAssessment(),
    val clinicalEvents: List<ClinicalEvent> = emptyList(),
    val clinicalMeasurements: List<ClinicalMeasurement> = emptyList(),
    val informedConsents: List<InformedConsent> = emptyList(),
    val medicationsStructured: List<MedicationRecord> = emptyList(),
    val clinicalPhotos: Map<String, ClinicalPhotoRecord> = emptyMap()
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
