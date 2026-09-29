package com.yomismtz.expedientedeldentista

import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNode
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class RecordOpenCloseTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun createOpenCloseOpenAgain_keepsActivityAlive() {
        compose.onNode(hasText("NUEVO EXPEDIENTE", substring = true))
            .assertExists()
            .performClick()

        val fields = compose.onAllNodes(hasSetTextAction())
        fields[0].performTextInput("ABC")
        fields[1].performTextInput("25")

        compose.onNode(hasText("Femenino", substring = false))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Crear y abrir expediente", substring = true))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Abrir expediente", substring = true))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Secciones del expediente", substring = true))
            .assertExists()

        compose.onNode(hasText("‹", substring = false))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Abrir expediente", substring = true))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Secciones del expediente", substring = true))
            .assertExists()
    }

    @Test
    fun createRecord_openClinicalSection_andReturnToSections() {
        compose.onNode(hasText("NUEVO EXPEDIENTE", substring = true))
            .assertExists()
            .performClick()

        val fields = compose.onAllNodes(hasSetTextAction())
        fields[0].performTextInput("NAV")
        fields[1].performTextInput("30")

        compose.onNode(hasText("Femenino", substring = false))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Crear y abrir expediente", substring = true))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Abrir expediente", substring = true))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Secciones del expediente", substring = true))
            .assertExists()

        compose.onNode(hasText("Exploración clínica", substring = true))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Signos vitales", substring = true))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Signos vitales", substring = true))
            .assertExists()

        compose.onNode(hasText("‹", substring = false))
            .assertExists()
            .performClick()

        compose.onNode(hasText("Exploración clínica", substring = true))
            .assertExists()
    }

}
