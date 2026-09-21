package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.TextSizeStyle
import com.yomismtz.expedientedeldentista.settings.ThemeMode
import com.yomismtz.expedientedeldentista.ui.theme.BirdPaletteChoices
import com.yomismtz.expedientedeldentista.ui.theme.fontDisplayName
import com.yomismtz.expedientedeldentista.ui.theme.paletteDisplayName
import com.yomismtz.expedientedeldentista.ui.theme.paletteSwatches
import com.yomismtz.expedientedeldentista.ui.theme.textSizeDisplayName

@Composable
fun AppRootV19(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var settingsOpen by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        AppRootV7(
            preferences = preferences,
            onPreferencesChanged = onPreferencesChanged,
            onLanguageChanged = onLanguageChanged,
            session = session,
            onSessionChanged = onSessionChanged,
            onOpenSettings = { settingsOpen = true }
        )
        if (settingsOpen) {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                SettingsV19Screen(preferences, onPreferencesChanged) { settingsOpen = false }
            }
        }
    }
    BackHandler(enabled = settingsOpen) { settingsOpen = false }
}

@Composable
private fun SettingsV19Screen(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onBack: () -> Unit
) {
    val lang = preferences.languageTag
    val systemScale = LocalDensity.current.fontScale
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val widthDp = maxWidth.value.toInt()
        val compact = maxWidth < 380.dp || systemScale >= 1.20f
        val paletteColumns = if (compact) 1 else if (maxWidth < 650.dp) 2 else 3
        Column(
            Modifier.fillMaxSize().safeDrawingPadding().verticalScroll(rememberScrollState()).padding(VisualSpacingV49.lg),
            verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.lg)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(VisualSpacingV49.md)) {
                OutlinedButton(onClick = onBack, shape = MaterialTheme.shapes.small) { Text("‹") }
                Column(Modifier.weight(1f)) {
                    Text(tr(lang,"Apariencia, accesibilidad e información","Appearance, accessibility & information"),style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black)
                    Text(tr(lang,"Los cambios se guardan automáticamente y permanecen al cerrar la app.","Changes save automatically and remain after closing the app."),style=MaterialTheme.typography.bodyMedium)
                }
            }

            SettingCardV19(tr(lang,"Idioma y tratamiento","Language and title")) {
                AdaptiveGridV17(2,if(compact)1 else 2) { i ->
                    val tag=if(i==0)"es" else "en"
                    FilterChip(preferences.languageTag==tag,{onPreferencesChanged(preferences.copy(languageTag=tag))},{Text(if(i==0)"Español" else "English")},modifier=Modifier.fillMaxWidth())
                }
                AdaptiveGridV17(2,if(compact)1 else 2) { i ->
                    val title=if(i==0)ClinicianTitle.DOCTOR else ClinicianTitle.DOCTORA
                    FilterChip(preferences.clinicianTitle==title,{onPreferencesChanged(preferences.copy(clinicianTitle=title))},{Text(if(i==0)"Doctor" else "Doctora")},modifier=Modifier.fillMaxWidth())
                }
            }

            SettingCardV19(tr(lang,"Tema de la aplicación","App theme")) {
                AdaptiveGridV17(2,if(compact)1 else 2) { i ->
                    val mode=if(i==0)ThemeMode.LIGHT else ThemeMode.DARK
                    FilterChip(
                        selected=preferences.themeMode==mode,
                        onClick={onPreferencesChanged(preferences.copy(themeMode=mode))},
                        label={Text(if(i==0)tr(lang,"☀ Claro","☀ Light") else tr(lang,"☾ Oscuro","☾ Dark"))},
                        modifier=Modifier.fillMaxWidth()
                    )
                }
                Text(tr(lang,"El modo claro es el predeterminado. Tu elección queda guardada en este dispositivo.","Light mode is the default. Your choice is saved on this device."),style=MaterialTheme.typography.bodySmall)
            }

            SettingCardV19(tr(lang,"Paleta de colores inspirada en aves","Bird-inspired color palette")) {
                BirdPaletteChoices.chunked(paletteColumns).forEach { group ->
                    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(VisualSpacingV49.sm)) {
                        group.forEach { style ->
                            PaletteCardV19(style,lang,preferences.birdPaletteStyle==style,{onPreferencesChanged(preferences.copy(birdPaletteStyle=style))},Modifier.weight(1f))
                        }
                        repeat(paletteColumns-group.size){Spacer(Modifier.weight(1f))}
                    }
                }
            }

            SettingCardV19(tr(lang,"Tipo de letra","Typeface")) {
                FontStyle.entries.forEach { style ->
                    FilterChip(preferences.fontStyle==style,{onPreferencesChanged(preferences.copy(fontStyle=style))},{Text(fontDisplayName(style,lang))},modifier=Modifier.fillMaxWidth())
                }
            }

            SettingCardV19(tr(lang,"Tamaño de letra","Text size")) {
                TextSizeStyle.entries.forEach { style ->
                    FilterChip(preferences.textSizeStyle==style,{onPreferencesChanged(preferences.copy(textSizeStyle=style))},{Text(textSizeDisplayName(style,lang))},modifier=Modifier.fillMaxWidth())
                }
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer),modifier=Modifier.fillMaxWidth(),shape=MaterialTheme.shapes.medium,border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant)) {
                    Column(Modifier.padding(VisualSpacingV49.md),verticalArrangement=Arrangement.spacedBy(VisualSpacingV49.xs)) {
                        Text(tr(lang,"Vista previa","Preview"),fontWeight=FontWeight.Black)
                        Text(tr(lang,"Exploración odontológica · OD 36 · Hallazgos clínicos","Dental examination · Tooth 36 · Clinical findings"),style=MaterialTheme.typography.bodyLarge)
                        Text(tr(lang,"También respeta la escala de letra configurada en Android.","Android system font scaling is also respected."),style=MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            SettingCardV19(tr(lang,"Adaptación a pantalla","Screen adaptation")) {
                Text("${tr(lang,"Ancho disponible","Available width")}: $widthDp dp")
                Text("${tr(lang,"Escala Android","Android scale")}: ${"%.0f".format(systemScale*100)}%")
                Text(if(compact)tr(lang,"Modo compacto activo: se prioriza una columna para evitar recortes.","Compact mode active: one column is prioritized to prevent clipping.") else tr(lang,"Modo estándar/ampliado: se aprovecha el ancho disponible.","Standard/expanded mode: available width is used."))
            }

            SettingCardV19(tr(lang,"Créditos","Credits")) {
                Text(
                    tr(lang,
                        "Parte del contenido visual, las ilustraciones educativas, la organización de información y el apoyo de redacción de esta aplicación fueron desarrollados con asistencia de ChatGPT (OpenAI), bajo revisión, selección y adaptación del autor del proyecto.",
                        "Part of the visual content, educational illustrations, information organization and writing support in this application were developed with assistance from ChatGPT (OpenAI), under review, selection and adaptation by the project author."
                    )
                )
                Text(
                    tr(lang,
                        "La mención de ChatGPT/OpenAI reconoce la herramienta utilizada durante el desarrollo y no implica patrocinio, certificación clínica ni respaldo de OpenAI al proyecto.",
                        "The ChatGPT/OpenAI reference acknowledges a tool used during development and does not imply sponsorship, clinical certification or endorsement of the project by OpenAI."
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            SettingCardV19(tr(lang,"Privacidad y uso de inteligencia artificial","Privacy and AI use")) {
                Text(
                    tr(lang,
                        "ChatGPT (OpenAI) se utilizó durante el desarrollo como herramienta de apoyo para crear o refinar material educativo. La versión Android instalada funciona sin conexión y no comparte automáticamente datos personales, clínicos ni información sensible con ChatGPT/OpenAI.",
                        "ChatGPT (OpenAI) was used during development as a support tool to create or refine educational material. The installed Android version works offline and does not automatically share personal, clinical or sensitive information with ChatGPT/OpenAI."
                    )
                )
                Text(
                    tr(lang,
                        "Esta aplicación es una guía educativa. No introduzcas información identificable de pacientes reales.",
                        "This application is an educational guide. Do not enter identifiable information about real patients."
                    ),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(onClick=onBack,modifier=Modifier.fillMaxWidth()) { Text(tr(lang,"Guardar y volver","Save and return"),fontWeight=FontWeight.Black) }
            Text(tr(lang,"El guardado ya fue automático; este botón solo cierra Configuración.","Saving was already automatic; this button only closes Settings."),modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SettingCardV19(title:String,content:@Composable ()->Unit) {
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),shape=MaterialTheme.shapes.large,elevation=CardDefaults.cardElevation(defaultElevation=1.dp)) {
        Column(Modifier.fillMaxWidth().padding(VisualSpacingV49.lg),verticalArrangement=Arrangement.spacedBy(VisualSpacingV49.sm)) {
            Text(title,fontWeight=FontWeight.SemiBold,style=MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
private fun PaletteCardV19(style:BirdPaletteStyle,lang:String,selected:Boolean,onClick:()->Unit,modifier:Modifier=Modifier) {
    Card(onClick=onClick,modifier=modifier,colors=CardDefaults.cardColors(containerColor=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),border=BorderStroke(if(selected)2.dp else 1.dp,if(selected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),shape=MaterialTheme.shapes.medium,elevation=CardDefaults.cardElevation(defaultElevation=if(selected)2.dp else 1.dp)) {
        Column(Modifier.fillMaxWidth().padding(VisualSpacingV49.sm),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(VisualSpacingV49.xs)) {
            Text(paletteDisplayName(style,lang),fontWeight=if(selected)FontWeight.Black else FontWeight.Medium,textAlign=TextAlign.Center)
            Row(horizontalArrangement=Arrangement.spacedBy(4.dp)) { paletteSwatches(style).forEach { c -> Box(Modifier.size(16.dp).background(c,CircleShape)) } }
            if(style==BirdPaletteStyle.AGAPORNI) Text(tr(lang,"Predeterminada","Default"),style=MaterialTheme.typography.labelSmall)
        }
    }
}
