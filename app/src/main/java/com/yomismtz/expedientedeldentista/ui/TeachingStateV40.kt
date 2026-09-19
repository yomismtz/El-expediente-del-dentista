package com.yomismtz.expedientedeldentista.ui

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.yomismtz.expedientedeldentista.clinical.Surface
import com.yomismtz.expedientedeldentista.clinical.SurfaceMark

data class ToothTeachingPlanV40(
    val diagnosisId: String,
    val diagnosisLabel: String,
    val treatmentId: String,
    val treatmentLabel: String,
    val priority: Int,
    val reason: String
)

data class EndoTeachingResultV40(
    val tooth: Int,
    val pulpalDiagnosis: String,
    val apicalDiagnosis: String,
    val suggestedTreatment: String,
    val visibleCanals: Int
)

object TeachingStateV40 {
    val toothPlans = mutableStateMapOf<Int, ToothTeachingPlanV40>()
    val moduleSummaries = mutableStateMapOf<String, String>()
    val savedPracticeSections = mutableStateMapOf<String, Boolean>()
    val ceosSurfaces = mutableStateMapOf<Int, Map<Surface, SurfaceMark>>()
    val endoResult = mutableStateOf<EndoTeachingResultV40?>(null)

    fun clear() {
        toothPlans.clear()
        moduleSummaries.clear()
        savedPracticeSections.clear()
        ceosSurfaces.clear()
        endoResult.value = null
    }
}
