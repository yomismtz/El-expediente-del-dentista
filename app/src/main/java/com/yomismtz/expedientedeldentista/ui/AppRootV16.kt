package com.yomismtz.expedientedeldentista.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import com.yomismtz.expedientedeldentista.ui.theme.BirdPaletteChoices
import com.yomismtz.expedientedeldentista.ui.theme.fontDisplayName
import com.yomismtz.expedientedeldentista.ui.theme.paletteDisplayName
import com.yomismtz.expedientedeldentista.ui.theme.paletteSwatches
import com.yomismtz.expedientedeldentista.ui.theme.textSizeDisplayName

private enum class YsmWindowSize { COMPACT, MEDIUM, EXPANDED }

@Composable
fun AppRootV16(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    session: EducationalSession,
    onSessionChanged: (EducationalSession) -> Unit
) {
    var settingsOpen by remember { mutableStateOf(false) }
    val lang = preferences.languageTag

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val sizeClass = when {
            maxWidth < 360.dp -> YsmWindowSize.COMPACT
            maxWidth < 600.dp -> YsmWindowSize.MEDIUM
            else -> YsmWindowSize.EXPANDED
        }

        AppRootV7(
            preferences = preferences,
            onPreferencesChanged = onPreferencesChanged,
            onLanguageChanged = onLanguageChanged,
            session = session,
            onSessionChanged = onSessionChanged
        )

        if (!settingsOpen) {
            OutlinedButton(
                onClick = { settingsOpen = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .safeDrawingPadding()
                    .padding(10.dp)
            ) {
                Text(
                    if (sizeClass == YsmWindowSize.COMPACT) "⚙"
                    else "⚙  ${if (lang == "en") "Settings" else "Configuración"}"
                )
            }
        }

        if (settingsOpen) {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                AccessibilitySettingsV16Screen(
                    preferences = preferences,
                    sizeClass = sizeClass,
                    onPreferencesChanged = onPreferencesChanged,
                    onBack = { settingsOpen = false }
                )
            }
        }
    }

    BackHandler(enabled = settingsOpen) { settingsOpen = false }
}

@Composable
private fun AccessibilitySettingsV16Screen(
    preferences: AppPreferences,
    sizeClass: YsmWindowSize,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onBack: () -> Unit
) {
    val lang = preferences.languageTag
    val cs = MaterialTheme.colorScheme
    val systemFontScale = LocalDensity.current.fontScale
    val largeSystemText = systemFontScale >= 1.20f
    val columns = when {
        sizeClass == YsmWindowSize.COMPACT || largeSystemText -> 1
        sizeClass == YsmWindowSize.MEDIUM -> 2
        else -> 3
    }

    Column(
        Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = when (sizeClass) {
                    YsmWindowSize.COMPACT -> 12.dp
                    YsmWindowSize.MEDIUM -> 18.dp
                    YsmWindowSize.EXPANDED -> 28.dp
                },
                vertical = 14.dp
            ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(onClick = onBack) {
                Text("‹ ${if (lang == "en") "Back" else "Volver"}")
            }
            Column(Modifier.weight(1f)) {
                Text(
                    if (lang == "en") "Appearance & accessibility" else "Apariencia y accesibilidad",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black
                )
                Text(
                    if (lang == "en") "Changes are saved automatically on this device."
                    else "Los cambios se guardan automáticamente en este dispositivo.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        SettingsCardV16(
            title = if (lang == "en") "Language and title" else "Idioma y tratamiento"
        ) {
            Text(if (lang == "en") "Language" else "Idioma", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = lang == "es",
                    onClick = { onPreferencesChanged(preferences.copy(languageTag = "es")) },
                    label = { Text("Español") }
                )
                FilterChip(
                    selected = lang == "en",
                    onClick = { onPreferencesChanged(preferences.copy(languageTag = "en")) },
                    label = { Text("English") }
                )
            }
            Text(if (lang == "en") "Professional title" else "Doctor / Doctora", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                    onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                    label = { Text("Doctor") }
                )
                FilterChip(
                    selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                    onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                    label = { Text("Doctora") }
                )
            }
        }

        SettingsCardV16(
            title = if (lang == "en") "Bird color palette" else "Paleta de colores inspirada en aves"
        ) {
            Text(
                if (lang == "en") "The selected palette remains active after closing and reopening the app."
                else "La paleta seleccionada permanece al cerrar y volver a abrir la app.",
                style = MaterialTheme.typography.bodyMedium
            )
            BirdPaletteChoices.chunked(columns).forEach { group ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    group.forEach { style ->
                        PaletteChoiceV16(
                            style = style,
                            lang = lang,
                            selected = preferences.birdPaletteStyle == style,
                            onClick = { onPreferencesChanged(preferences.copy(birdPaletteStyle = style)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(columns - group.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }

        SettingsCardV16(
            title = if (lang == "en") "Typeface" else "Tipo de letra"
        ) {
            FontStyle.entries.chunked(if (columns == 1) 1 else 2).forEach { group ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    group.forEach { style ->
                        FilterChip(
                            selected = preferences.fontStyle == style,
                            onClick = { onPreferencesChanged(preferences.copy(fontStyle = style)) },
                            label = { Text(fontDisplayName(style, lang)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (group.size == 1 && columns != 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        SettingsCardV16(
            title = if (lang == "en") "Text size" else "Tamaño de letra"
        ) {
            TextSizeStyle.entries.chunked(if (columns == 1) 1 else 2).forEach { group ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    group.forEach { style ->
                        FilterChip(
                            selected = preferences.textSizeStyle == style,
                            onClick = { onPreferencesChanged(preferences.copy(textSizeStyle = style)) },
                            label = { Text(textSizeDisplayName(style, lang)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (group.size == 1 && columns != 1) Spacer(Modifier.weight(1f))
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cs.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        if (lang == "en") "Preview" else "Vista previa",
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        if (lang == "en") "Dental examination · Tooth 36 · Clinical findings"
                        else "Exploración odontológica · OD 36 · Hallazgos clínicos",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        if (lang == "en") "This text also follows the font size configured in Android."
                        else "Este texto también respeta el tamaño de letra configurado en Android.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        SettingsCardV16(
            title = if (lang == "en") "Screen adaptation" else "Adaptación a la pantalla"
        ) {
            val sizeText = when (sizeClass) {
                YsmWindowSize.COMPACT -> if (lang == "en") "Small / compact phone" else "Teléfono pequeño / compacto"
                YsmWindowSize.MEDIUM -> if (lang == "en") "Standard phone" else "Teléfono mediano / estándar"
                YsmWindowSize.EXPANDED -> if (lang == "en") "Large phone or expanded screen" else "Teléfono grande o pantalla ampliada"
            }
            Text("📱 $sizeText", fontWeight = FontWeight.Bold)
            Text(
                if (lang == "en") "Android font scale detected: ${String.format("%.0f", systemFontScale * 100)}%."
                else "Escala de letra de Android detectada: ${String.format("%.0f", systemFontScale * 100)}%.",
                style = MaterialTheme.typography.bodyMedium
            )
            if (largeSystemText) {
                Text(
                    if (lang == "en") "Large system text is active. Configuration uses one-column layouts and scrollable content to reduce clipping."
                    else "Está activa la letra grande del teléfono. La configuración usa una sola columna y contenido desplazable para reducir recortes.",
                    color = cs.primary,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    if (lang == "en") "Layouts use the available width and scroll where needed."
                    else "Las pantallas aprovechan el ancho disponible y permiten desplazamiento cuando hace falta.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(if (lang == "en") "Save and return" else "Guardar y volver", fontWeight = FontWeight.Black)
        }
        Text(
            if (lang == "en") "Saving is automatic; this button only closes Settings."
            else "El guardado es automático; este botón solo cierra Configuración.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SettingsCardV16(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.45f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            content()
        }
    }
}

@Composable
private fun PaletteChoiceV16(
    style: BirdPaletteStyle,
    lang: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) cs.primaryContainer else cs.surface
        ),
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            if (selected) cs.primary else cs.outline.copy(alpha = 0.45f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                paletteDisplayName(style, lang),
                fontWeight = if (selected) FontWeight.Black else FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                paletteSwatches(style).forEach { color ->
                    Box(Modifier.size(18.dp).background(color, CircleShape))
                }
            }
            if (style == BirdPaletteStyle.AGAPORNI) {
                Text(
                    if (lang == "en") "Default" else "Predeterminada",
                    style = MaterialTheme.typography.labelLarge,
                    color = cs.secondary
                )
            }
        }
    }
}
