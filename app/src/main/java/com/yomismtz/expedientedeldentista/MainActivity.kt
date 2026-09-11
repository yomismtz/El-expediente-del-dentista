package com.yomismtz.expedientedeldentista

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.PaletteStyle
import com.yomismtz.expedientedeldentista.settings.SettingsStore
import com.yomismtz.expedientedeldentista.ui.theme.ExpedienteTheme

private enum class AppScreen { HOME, SETTINGS, PLACEHOLDER }

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = SettingsStore(this)

        setContentView(
            androidx.compose.ui.platform.ComposeView(this).apply {
                setContent {
                    var preferences by remember { mutableStateOf(store.load()) }
                    var screen by remember { mutableStateOf(AppScreen.HOME) }
                    var placeholderTitle by remember { mutableStateOf("") }

                    ExpedienteTheme(
                        paletteStyle = preferences.paletteStyle,
                        fontStyle = preferences.fontStyle
                    ) {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            if (!preferences.onboardingComplete) {
                                OnboardingScreen(
                                    preferences = preferences,
                                    onPreferencesChanged = {
                                        preferences = it
                                        store.save(it)
                                    },
                                    onLanguageChanged = { tag -> changeLanguage(tag) },
                                    onContinue = {
                                        val updated = preferences.copy(onboardingComplete = true)
                                        preferences = updated
                                        store.save(updated)
                                    }
                                )
                            } else {
                                when (screen) {
                                    AppScreen.HOME -> HomeScreen(
                                        preferences = preferences,
                                        onSettings = { screen = AppScreen.SETTINGS },
                                        onSection = { title ->
                                            placeholderTitle = title
                                            screen = AppScreen.PLACEHOLDER
                                        }
                                    )

                                    AppScreen.SETTINGS -> SettingsScreen(
                                        preferences = preferences,
                                        onPreferencesChanged = {
                                            preferences = it
                                            store.save(it)
                                        },
                                        onLanguageChanged = { tag -> changeLanguage(tag) },
                                        onBack = { screen = AppScreen.HOME }
                                    )

                                    AppScreen.PLACEHOLDER -> PlaceholderScreen(
                                        title = placeholderTitle,
                                        onBack = { screen = AppScreen.HOME }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    private fun changeLanguage(tag: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }
}

@Composable
private fun OnboardingScreen(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    onContinue: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.welcome_subtitle), style = MaterialTheme.typography.bodyLarge)
        }

        item {
            SettingBlock(title = stringResource(R.string.choose_language)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = preferences.languageTag == "es",
                        onClick = {
                            onPreferencesChanged(preferences.copy(languageTag = "es"))
                            onLanguageChanged("es")
                        },
                        label = { Text("Español") }
                    )
                    FilterChip(
                        selected = preferences.languageTag == "en",
                        onClick = {
                            onPreferencesChanged(preferences.copy(languageTag = "en"))
                            onLanguageChanged("en")
                        },
                        label = { Text("English") }
                    )
                }
            }
        }

        item {
            SettingBlock(title = stringResource(R.string.choose_title)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                        label = { Text(stringResource(R.string.doctor)) }
                    )
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                        label = { Text(stringResource(R.string.doctora)) }
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.educational_notice),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.continue_label))
            }
        }
    }
}

@Composable
private fun HomeScreen(
    preferences: AppPreferences,
    onSettings: () -> Unit,
    onSection: (String) -> Unit
) {
    val greeting = if (preferences.clinicianTitle == ClinicianTitle.DOCTOR) {
        stringResource(R.string.home_greeting_doctor)
    } else {
        stringResource(R.string.home_greeting_doctora)
    }

    val sections = listOf(
        stringResource(R.string.section_intake),
        stringResource(R.string.section_identification),
        stringResource(R.string.section_history),
        stringResource(R.string.section_odontogram),
        stringResource(R.string.section_icdas),
        stringResource(R.string.section_cpod),
        stringResource(R.string.section_oleary),
        stringResource(R.string.section_ipc),
        stringResource(R.string.section_ihos),
        stringResource(R.string.section_periodontogram),
        stringResource(R.string.section_pulpal),
        stringResource(R.string.section_treatment),
        stringResource(R.string.section_evolution)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(greeting, style = MaterialTheme.typography.bodyLarge)
                }
                OutlinedButton(onClick = onSettings) {
                    Text("⚙ ${stringResource(R.string.settings)}")
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text("📁 ${stringResource(R.string.folder_title)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(stringResource(R.string.folder_subtitle))
                }
            }
        }

        items(sections) { section ->
            Card(
                onClick = { onSection(section) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = section,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    preferences: AppPreferences,
    onPreferencesChanged: (AppPreferences) -> Unit,
    onLanguageChanged: (String) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack) { Text("← ${stringResource(R.string.back)}") }
                Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
        }

        item {
            Text(stringResource(R.string.settings_hint), style = MaterialTheme.typography.bodyMedium)
        }

        item {
            SettingBlock(stringResource(R.string.language)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = preferences.languageTag == "es",
                        onClick = {
                            onPreferencesChanged(preferences.copy(languageTag = "es"))
                            onLanguageChanged("es")
                        },
                        label = { Text("Español") }
                    )
                    FilterChip(
                        selected = preferences.languageTag == "en",
                        onClick = {
                            onPreferencesChanged(preferences.copy(languageTag = "en"))
                            onLanguageChanged("en")
                        },
                        label = { Text("English") }
                    )
                }
            }
        }

        item {
            SettingBlock(stringResource(R.string.professional_title)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTOR,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTOR)) },
                        label = { Text(stringResource(R.string.doctor)) }
                    )
                    FilterChip(
                        selected = preferences.clinicianTitle == ClinicianTitle.DOCTORA,
                        onClick = { onPreferencesChanged(preferences.copy(clinicianTitle = ClinicianTitle.DOCTORA)) },
                        label = { Text(stringResource(R.string.doctora)) }
                    )
                }
            }
        }

        item {
            SettingBlock(stringResource(R.string.appearance)) {
                PaletteStyle.entries.forEach { style ->
                    val label = when (style) {
                        PaletteStyle.WOOD -> stringResource(R.string.palette_wood)
                        PaletteStyle.CLINICAL_GREEN -> stringResource(R.string.palette_clinical_green)
                        PaletteStyle.DENTAL_BLUE -> stringResource(R.string.palette_dental_blue)
                        PaletteStyle.WINE -> stringResource(R.string.palette_wine)
                        PaletteStyle.SAGE -> stringResource(R.string.palette_sage)
                        PaletteStyle.MONO -> stringResource(R.string.palette_mono)
                    }
                    FilterChip(
                        selected = preferences.paletteStyle == style,
                        onClick = { onPreferencesChanged(preferences.copy(paletteStyle = style)) },
                        label = { Text(label) },
                        modifier = Modifier.padding(end = 8.dp, bottom = 6.dp)
                    )
                }
            }
        }

        item {
            SettingBlock(stringResource(R.string.typography)) {
                FontStyle.entries.forEach { style ->
                    val label = when (style) {
                        FontStyle.MODERN -> stringResource(R.string.font_modern)
                        FontStyle.ROUNDED -> stringResource(R.string.font_rounded)
                        FontStyle.ACADEMIC -> stringResource(R.string.font_academic)
                        FontStyle.ACCESSIBLE -> stringResource(R.string.font_accessible)
                    }
                    FilterChip(
                        selected = preferences.fontStyle == style,
                        onClick = { onPreferencesChanged(preferences.copy(fontStyle = style)) },
                        label = { Text(label) },
                        modifier = Modifier.padding(end = 8.dp, bottom = 6.dp)
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(stringResource(R.string.preview), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.folder_title), style = MaterialTheme.typography.titleLarge)
                    Text(stringResource(R.string.folder_subtitle), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun SettingBlock(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        content()
    }
}

@Composable
private fun PlaceholderScreen(title: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.coming_soon), style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = onBack) { Text("← ${stringResource(R.string.back)}") }
    }
}
