package com.yomismtz.expedientedeldentista

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityLaunchTest {
    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Before
    fun clearStateBeforeTest() {
        clearState()
    }

    @After
    fun clearStateAfterTest() {
        clearState()
    }

    @Test
    fun cleanInstallLaunchesAndRemainsResumed() {
        assertActivityRemainsOpen()
    }

    @Test
    fun completedOnboardingLaunchesMainAppAndRemainsResumed() {
        context.getSharedPreferences("expediente_settings", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("onboarding_complete", true)
            .commit()
        context.getSharedPreferences("expediente_runtime", Context.MODE_PRIVATE)
            .edit()
            .putInt("preview_seen_version", BuildConfig.VERSION_CODE)
            .commit()

        assertActivityRemainsOpen()
    }

    @Test
    fun corruptPreferenceTypesDoNotCrashStartup() {
        // Reproduces an upgrade edge case: SharedPreferences throws ClassCastException when
        // an older build stored the same key using another primitive type.
        context.getSharedPreferences("expediente_settings", Context.MODE_PRIVATE)
            .edit()
            .putString("onboarding_complete", "legacy-value")
            .commit()
        context.getSharedPreferences("expediente_runtime", Context.MODE_PRIVATE)
            .edit()
            .putString("preview_seen_version", "legacy-value")
            .commit()

        assertActivityRemainsOpen()
    }

    private fun assertActivityRemainsOpen() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.moveToState(Lifecycle.State.RESUMED)
            scenario.onActivity { activity ->
                assertFalse("MainActivity should remain open after launch", activity.isFinishing)
            }
        }
    }

    private fun clearState() {
        context.getSharedPreferences("expediente_settings", Context.MODE_PRIVATE)
            .edit().clear().commit()
        context.getSharedPreferences("expediente_runtime", Context.MODE_PRIVATE)
            .edit().clear().commit()
    }
}
