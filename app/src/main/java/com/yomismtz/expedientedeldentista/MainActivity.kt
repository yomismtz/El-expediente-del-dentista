package com.yomismtz.expedientedeldentista

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.SettingsStore
import com.yomismtz.expedientedeldentista.ui.AppRootV6
import com.yomismtz.expedientedeldentista.ui.theme.ExpedienteTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = SettingsStore(this)

        setContent {
            var preferences by remember { mutableStateOf(store.load()) }
            var session by remember { mutableStateOf(EducationalSession()) }

            ExpedienteTheme(
                paletteStyle = preferences.paletteStyle,
                fontStyle = preferences.fontStyle
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppRootV6(
                        preferences = preferences,
                        onPreferencesChanged = { updated ->
                            preferences = updated
                            store.save(updated)
                        },
                        onLanguageChanged = { tag ->
                            val updated = preferences.copy(languageTag = tag)
                            preferences = updated
                            store.save(updated)
                        },
                        session = session,
                        onSessionChanged = { session = it }
                    )
                }
            }
        }
    }
}
