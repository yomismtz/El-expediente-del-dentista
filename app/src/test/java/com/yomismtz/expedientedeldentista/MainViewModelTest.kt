package com.yomismtz.expedientedeldentista

import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.PatientProfile
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {
    @Test
    fun updateSession_keepsCurrentEducationalExercise() {
        val viewModel = MainViewModel()
        val updated = EducationalSession(
            profile = PatientProfile(
                exerciseName = "Caso de prueba",
                age = "21"
            )
        )

        viewModel.updateSession(updated)

        assertEquals("Caso de prueba", viewModel.session.profile.exerciseName)
        assertEquals("21", viewModel.session.profile.age)
    }
}
