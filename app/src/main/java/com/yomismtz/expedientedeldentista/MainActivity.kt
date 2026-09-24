package com.yomismtz.expedientedeldentista

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.TextSizeStyle
import com.yomismtz.expedientedeldentista.ui.theme.ExpedienteTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpedienteTheme(
                paletteStyle = BirdPaletteStyle.AGAPORNI,
                fontStyle = FontStyle.MODERN,
                textSizeStyle = TextSizeStyle.NORMAL
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("YSM Expediente · diagnóstico de tema")
                    }
                }
            }
        }
    }
}
