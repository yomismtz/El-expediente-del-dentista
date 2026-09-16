package com.yomismtz.expedientedeldentista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yomismtz.expedientedeldentista.clinical.EducationalSession

/**
 * Keeps the current educational exercise alive across Activity recreation
 * (for example, rotation or another configuration change).
 *
 * The exercise is intentionally in-memory: this app is an educational tool,
 * not a persistent electronic patient record.
 */
class MainViewModel : ViewModel() {
    var session by mutableStateOf(EducationalSession())
        private set

    fun updateSession(updated: EducationalSession) {
        session = updated
    }
}
