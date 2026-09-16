package com.yomismtz.expedientedeldentista

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.SettingsStore
import com.yomismtz.expedientedeldentista.ui.AppRootV19
import com.yomismtz.expedientedeldentista.ui.OnboardingV40Screen
import com.yomismtz.expedientedeldentista.ui.edgeSwipeBackV21
import com.yomismtz.expedientedeldentista.ui.theme.ExpedienteTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store=SettingsStore(this)
        val initialPreferences=runCatching{store.load()}.getOrElse{AppPreferences()}
        setContent {
            var preferences by remember{mutableStateOf(initialPreferences)}
            var session by remember{mutableStateOf(EducationalSession())}
            val savePreferences:(AppPreferences)->Unit={updated->preferences=updated;runCatching{store.save(updated)}}
            ExpedienteTheme(paletteStyle=preferences.birdPaletteStyle,fontStyle=preferences.fontStyle,textSizeStyle=preferences.textSizeStyle){
                Surface(Modifier.fillMaxSize()){
                    val swipe=if(preferences.onboardingComplete)Modifier.edgeSwipeBackV21{onBackPressedDispatcher.onBackPressed()} else Modifier
                    Box(Modifier.fillMaxSize().then(swipe)){
                        if(!preferences.onboardingComplete){
                            OnboardingV40Screen(preferences,savePreferences){completed->savePreferences(completed.copy(onboardingComplete=true))}
                        }else{
                            AppRootV19(preferences,savePreferences,{tag->savePreferences(preferences.copy(languageTag=tag))},session){session=it}
                        }
                    }
                }
            }
        }
    }
}
