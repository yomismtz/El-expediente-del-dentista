package com.yomismtz.expedientedeldentista.ui

import androidx.compose.runtime.*
import com.yomismtz.expedientedeldentista.clinical.AppScreen
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

@Composable
fun ClinicalHistoryFlowV39(
    lang:String,
    session:EducationalSession,
    onSessionChanged:(EducationalSession)->Unit,
    onBack:()->Unit
){
    var pageName by rememberSaveable { mutableStateOf<String?>(null) }
    val page = pageName?.let(AppScreen::valueOf)
    val back={ pageName=null }
    if(page==null){
        ClinicalHistoryHubV38(lang,{pageName=it.name},onBack)
        return
    }
    when(page){
        AppScreen.HISTORY_IDENTIFICATION -> IdentificationScreen(lang,session,onSessionChanged,back)
        AppScreen.HISTORY_REASON -> HistoryReasonV38(lang,back)
        AppScreen.HISTORY_HEREDITARY -> HistoryHereditaryV38(lang,back)
        AppScreen.HISTORY_NONPATH -> HistoryNonPathV38(lang,back)
        AppScreen.HISTORY_GYNECO -> HistoryGynecoV38(lang,back)
        AppScreen.HISTORY_PATH -> PathologicalHistory37Screen(lang,session,onSessionChanged,{pageName=AppScreen.SYSTEMIC_PROTOCOLS.name},back)
        AppScreen.HISTORY_SURGICAL_TRAUMA -> HistorySurgicalTraumaV38(lang,back)
        AppScreen.HISTORY_PHYSICAL -> HistoryPhysicalV38(lang,back)
        AppScreen.OCCLUSION -> OcclusionInteractiveV19Screen(lang,back)
        AppScreen.HISTORY_ORTHO -> HistoryOrthoV38(lang,back)
        AppScreen.HISTORY_DENTAL_ALTERATIONS -> HistoryDentalAlterationsV38(lang,back)
        AppScreen.HISTORY_HABITS -> HistoryHabitsV38(lang,back)
        AppScreen.HISTORY_ORAL_EXAM -> MucosaInteractiveV19Screen(lang,back)
        AppScreen.SYSTEMIC_PROTOCOLS -> SystemicProtocols37Screen(lang,back)
        else -> ClinicalHistoryHubV38(lang,{pageName=it.name},onBack)
    }
}
