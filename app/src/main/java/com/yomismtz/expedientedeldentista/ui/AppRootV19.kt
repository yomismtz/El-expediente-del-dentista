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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import com.yomismtz.expedientedeldentista.clinical.SavedRecord
import com.yomismtz.expedientedeldentista.clinical.PatientProfile
import com.yomismtz.expedientedeldentista.settings.AppPreferences
import com.yomismtz.expedientedeldentista.settings.BirdPaletteStyle
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle
import com.yomismtz.expedientedeldentista.settings.CardShapeStyle
import com.yomismtz.expedientedeldentista.settings.FontStyle
import com.yomismtz.expedientedeldentista.settings.TextSizeStyle
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
    onSessionChanged: (EducationalSession) -> Unit,
    savedRecords: List<SavedRecord>,
    activeRecordId: String?,
    onNewRecord: (PatientProfile) -> Unit,
    onLoadRecord: (SavedRecord) -> Unit,
    onDeleteRecord: (String) -> Unit
) {
    var settingsOpen by remember { mutableStateOf(false) }
    var recordMenuOpen by remember { mutableStateOf(activeRecordId == null) }
    Box(Modifier.fillMaxSize()) {
        if (!recordMenuOpen && activeRecordId != null) AppRootV7(
            preferences = preferences,
            onPreferencesChanged = onPreferencesChanged,
            onLanguageChanged = onLanguageChanged,
            session = session,
            onSessionChanged = onSessionChanged,
            onOpenSettings = { settingsOpen = true }
        )
        if (recordMenuOpen) {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                RecordMenuV19(savedRecords, activeRecordId, { profile ->
                    onNewRecord(profile); recordMenuOpen = false
                }, { r -> onLoadRecord(r); recordMenuOpen = false }, onDeleteRecord)
            }
        }
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
            Modifier.fillMaxSize().safeDrawingPadding().verticalScroll(rememberScrollState()).padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onBack) { Text("‹") }
                Column(Modifier.weight(1f)) {
                    Text(tr(lang,"Apariencia y accesibilidad","Appearance & accessibility"),style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black)
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

            SettingCardV19(tr(lang,"Elige tu mascota y su paleta","Choose your mascot and palette")) {
                BirdPaletteChoices.chunked(paletteColumns).forEach { group ->
                    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
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
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer),modifier=Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                        Text(tr(lang,"Vista previa","Preview"),fontWeight=FontWeight.Black)
                        Text(tr(lang,"Exploración odontológica · OD 36 · Hallazgos clínicos","Dental examination · Tooth 36 · Clinical findings"),style=MaterialTheme.typography.bodyLarge)
                        Text(tr(lang,"También respeta la escala de letra configurada en Android.","Android system font scaling is also respected."),style=MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            SettingCardV19(tr(lang,"Forma de tarjetas","Card shape")) {
                AdaptiveGridV17(CardShapeStyle.entries.size, if(compact)1 else 3) { i ->
                    val style=CardShapeStyle.entries[i]
                    val label=when(style){ CardShapeStyle.SOFT->tr(lang,"Suave","Soft"); CardShapeStyle.ROUNDED->tr(lang,"Redondeada","Rounded"); CardShapeStyle.SQUARE->tr(lang,"Recta","Square") }
                    FilterChip(preferences.cardShapeStyle==style,{onPreferencesChanged(preferences.copy(cardShapeStyle=style))},{Text(label)},modifier=Modifier.fillMaxWidth())
                }
            }

            SettingCardV19(tr(lang,"Estados visuales","Visual status")) {
                AdaptiveGridV17(3, if(compact)1 else 3) { i ->
                    val label=when(i){0->"✓ ${tr(lang,"Completado","Completed")}";1->"● ${tr(lang,"En progreso","In progress")}";else->"○ ${tr(lang,"Pendiente","Pending")}" }
                    Surface(color=when(i){0->MaterialTheme.colorScheme.secondaryContainer;1->MaterialTheme.colorScheme.primaryContainer;else->MaterialTheme.colorScheme.surfaceVariant},shape=MaterialTheme.shapes.medium){Text(label,Modifier.padding(10.dp),fontWeight=FontWeight.Bold)}
                }
            }

            SettingCardV19(tr(lang,"Adaptación a pantalla","Screen adaptation")) {
                Text("📱 ${tr(lang,"Ancho disponible","Available width")}: $widthDp dp")
                Text("🔤 ${tr(lang,"Escala Android","Android scale")}: ${"%.0f".format(systemScale*100)}%")
                Text(if(compact)tr(lang,"Modo compacto activo: se prioriza una columna para evitar recortes.","Compact mode active: one column is prioritized to prevent clipping.") else tr(lang,"Modo estándar/ampliado: se aprovecha el ancho disponible.","Standard/expanded mode: available width is used."))
            }

            Button(onClick=onBack,modifier=Modifier.fillMaxWidth()) { Text(tr(lang,"Guardar y volver","Save and return"),fontWeight=FontWeight.Black) }
            Text(tr(lang,"El guardado ya fue automático; este botón solo cierra Configuración.","Saving was already automatic; this button only closes Settings."),modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SettingCardV19(title:String,content:@Composable ()->Unit) {
    Card(modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline.copy(alpha=.4f)),shape=RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(14.dp),verticalArrangement=Arrangement.spacedBy(9.dp)) {
            Text(title,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
private fun PaletteCardV19(style:BirdPaletteStyle,lang:String,selected:Boolean,onClick:()->Unit,modifier:Modifier=Modifier) {
    var reaction by remember { mutableStateOf(0) }
    val motion = remember { Animatable(0f) }
    LaunchedEffect(reaction) { if(reaction>0){ motion.snapTo(0f); motion.animateTo(1f,tween(180)); motion.animateTo(0f,tween(300)) } }
    Card(onClick={ reaction++; onClick() },modifier=modifier,colors=CardDefaults.cardColors(containerColor=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),border=BorderStroke(if(selected)2.dp else 1.dp,if(selected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha=.4f)),shape=RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(9.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(6.dp)) {
            Text(mascotIconV19(style),style=MaterialTheme.typography.headlineMedium,modifier=Modifier.graphicsLayer { val k=motion.value; when(mascotMotionV19(style)){0->{translationY=-18f*k; scaleX=1f+.12f*k; scaleY=1f+.12f*k};1->{rotationZ=14f*k; scaleX=1f+.08f*k};2->{translationX=12f*k; rotationZ=-10f*k};else->{scaleX=1f+.16f*k; scaleY=1f-.10f*k} } })
            Text(paletteDisplayName(style,lang),fontWeight=if(selected)FontWeight.Black else FontWeight.Medium,textAlign=TextAlign.Center)
            Row(horizontalArrangement=Arrangement.spacedBy(4.dp)) { paletteSwatches(style).forEach { c -> Box(Modifier.size(16.dp).background(c,CircleShape)) } }
        }
    }
}


private fun mascotIconV19(style:BirdPaletteStyle)=when(style){
 BirdPaletteStyle.AZUL_TURQUESA->"🩵"; BirdPaletteStyle.AZUL_MARINO->"💙";
 BirdPaletteStyle.AGAPORNI->"🐦"; BirdPaletteStyle.TUCAN->"🦜"; BirdPaletteStyle.NINFA->"🐤"; BirdPaletteStyle.BUHO->"🦉"; BirdPaletteStyle.CUERVO->"🐦‍⬛"
 BirdPaletteStyle.GUACAMAYA->"🦜"; BirdPaletteStyle.PUG->"🐶"; BirdPaletteStyle.POMERANIA->"🐕"; BirdPaletteStyle.GATO->"🐱"; BirdPaletteStyle.CONEJO->"🐰"
 BirdPaletteStyle.ELEFANTE->"🐘"; BirdPaletteStyle.CABALLO_CAFE->"🐴"; BirdPaletteStyle.SERPIENTE->"🐍"; BirdPaletteStyle.IGUANA->"🦎"; BirdPaletteStyle.ARANA->"🕷️"
 BirdPaletteStyle.TORTUGA->"🐢"; BirdPaletteStyle.PEZ_PAYASO->"🐠"; BirdPaletteStyle.DELFIN->"🐬"; BirdPaletteStyle.TIBURON->"🦈"; BirdPaletteStyle.AJOLOTE->"🩷"
 BirdPaletteStyle.PINGUINO->"🐧"; BirdPaletteStyle.PATO_MANDARIN->"🦆"; BirdPaletteStyle.HAMSTER->"🐹"; BirdPaletteStyle.FENIX->"🔥"; BirdPaletteStyle.ZORRO->"🦊"
 BirdPaletteStyle.PAVO_REAL->"🦚"; BirdPaletteStyle.COLIBRI->"🐦"; BirdPaletteStyle.MARTIN_PESCADOR->"🐦"; BirdPaletteStyle.QUETZAL->"🐦"; BirdPaletteStyle.DRAGON->"🐉"
 BirdPaletteStyle.BALLENA_AZUL->"🐋"; BirdPaletteStyle.RANA_VERDE->"🐸"; BirdPaletteStyle.MARIPOSA_MONARCA->"🦋"; BirdPaletteStyle.FLAMENCO_ROSA->"🦩"; BirdPaletteStyle.CABALLITO_TURQUESA->"🐠"
 BirdPaletteStyle.CANGREJO_CORAL->"🦀"; BirdPaletteStyle.MURCIELAGO_NOCHE->"🦇"; BirdPaletteStyle.PANDA_MONO->"🐼"; BirdPaletteStyle.ABEJA_CONTRASTE->"🐝"
 BirdPaletteStyle.CAMALEON->"🦎"; BirdPaletteStyle.PERICO->"🦜"; BirdPaletteStyle.GALLO->"🐓"
}

private fun mascotMotionV19(style:BirdPaletteStyle)=when(style){
 BirdPaletteStyle.AZUL_TURQUESA,BirdPaletteStyle.AZUL_MARINO->3
 BirdPaletteStyle.AGAPORNI,BirdPaletteStyle.NINFA,BirdPaletteStyle.CONEJO,BirdPaletteStyle.RANA_VERDE,BirdPaletteStyle.PINGUINO->0
 BirdPaletteStyle.TUCAN,BirdPaletteStyle.GUACAMAYA,BirdPaletteStyle.PERICO,BirdPaletteStyle.GALLO,BirdPaletteStyle.COLIBRI,BirdPaletteStyle.MARIPOSA_MONARCA,BirdPaletteStyle.MURCIELAGO_NOCHE->1
 BirdPaletteStyle.SERPIENTE,BirdPaletteStyle.IGUANA,BirdPaletteStyle.CAMALEON,BirdPaletteStyle.CANGREJO_CORAL,BirdPaletteStyle.PEZ_PAYASO,BirdPaletteStyle.DELFIN,BirdPaletteStyle.CABALLITO_TURQUESA->2
 else->3
}


@Composable
private fun RecordMenuV19(
    records: List<SavedRecord>,
    activeId: String?,
    onNew: (PatientProfile) -> Unit,
    onLoad: (SavedRecord) -> Unit,
    onDelete: (String) -> Unit
) {
    var mode by remember { mutableStateOf("home") }
    var initials by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var deleteTarget by remember { mutableStateOf<SavedRecord?>(null) }

    Column(
        Modifier.fillMaxSize().safeDrawingPadding().verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("🦷 YSM Expediente", style=MaterialTheme.typography.headlineMedium, fontWeight=FontWeight.Black)
        Text("Simulador didáctico · los datos se conservan localmente para continuar prácticas previas.", style=MaterialTheme.typography.bodyMedium)

        if(mode=="home") {
            Text("Selecciona cómo quieres comenzar.", style=MaterialTheme.typography.titleMedium)
            Card(onClick={mode="new"}, modifier=Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(22.dp), verticalArrangement=Arrangement.spacedBy(5.dp)) {
                    Text("✨ NUEVO EXPEDIENTE", style=MaterialTheme.typography.titleLarge, fontWeight=FontWeight.Black)
                    Text("Inicia una práctica nueva.")
                }
            }
            Card(onClick={mode="load"}, modifier=Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(22.dp), verticalArrangement=Arrangement.spacedBy(5.dp)) {
                    Text("📚 CARGAR EXPEDIENTE", style=MaterialTheme.typography.titleLarge, fontWeight=FontWeight.Black)
                    Text("Consulta y continúa una práctica guardada anteriormente.")
                }
            }
            NoticeCard("Uso exclusivamente didáctico. Esta función organiza ejercicios y permite consultar datos de prácticas previas; no sustituye un expediente clínico institucional.")
        }

        if(mode=="new") {
            OutlinedButton(onClick={mode="home"}) { Text("‹ Volver") }
            Text("✨ Nuevo expediente didáctico", style=MaterialTheme.typography.titleLarge, fontWeight=FontWeight.Black)
            Text("Identifica la práctica sin registrar el nombre completo de una persona.")
            OutlinedTextField(initials,{initials=it.uppercase().filter{ch->ch.isLetter()}.take(5)},label={Text("Iniciales")},singleLine=true,modifier=Modifier.fillMaxWidth())
            OutlinedTextField(age,{age=it.filter(Char::isDigit).take(3)},label={Text("Edad")},singleLine=true,modifier=Modifier.fillMaxWidth())
            Text("Sexo", fontWeight=FontWeight.Bold)
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                listOf("Femenino","Masculino","Otro / no especificado").forEach { option ->
                    FilterChip(selected=sex==option,onClick={sex=option},label={Text(option)},modifier=Modifier.weight(1f))
                }
            }
            val valid=initials.isNotBlank() && age.toIntOrNull() in 0..120 && sex.isNotBlank()
            Button(onClick={onNew(PatientProfile(patientInitials=initials,age=age,sex=sex))},enabled=valid,modifier=Modifier.fillMaxWidth()) {
                Text("Crear y abrir expediente")
            }
            if(!valid) Text("Completa iniciales, una edad válida y sexo para comenzar.",style=MaterialTheme.typography.bodySmall)
        }

        if(mode=="load") {
            OutlinedButton(onClick={mode="home"}) { Text("‹ Volver") }
            Text("📚 Expedientes guardados", style=MaterialTheme.typography.titleLarge, fontWeight=FontWeight.Black)
            if(records.isEmpty()) {
                Card(Modifier.fillMaxWidth()) { Text("Todavía no hay prácticas guardadas.", Modifier.padding(18.dp)) }
            } else records.forEach { record ->
                Card(onClick={onLoad(record)}, modifier=Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        val p=record.session.profile
                        Text((if(record.id==activeId)"▶ " else "📁 ")+(p.patientInitials.ifBlank{record.title}),fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
                        Text("Edad: "+p.age.ifBlank{"—"}+" · Sexo: "+p.sex.ifBlank{"—"})
                        val date=remember(record.updatedAt){java.text.SimpleDateFormat("dd/MM/yyyy · HH:mm",java.util.Locale.getDefault()).format(java.util.Date(record.updatedAt))}
                        Text("Última consulta: $date", style=MaterialTheme.typography.bodySmall)
                        OutlinedButton(onClick={deleteTarget=record}) { Text("Eliminar práctica") }
                    }
                }
            }
            NoticeCard("Los ejercicios se guardan únicamente en este dispositivo para poder consultarlos y continuarlos después.")
        }
    }

    deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest={deleteTarget=null},
            title={Text("¿Eliminar esta práctica?")},
            text={Text("Se eliminará del dispositivo y no podrá recuperarse.")},
            confirmButton={Button(onClick={onDelete(target.id);deleteTarget=null}){Text("Eliminar")}},
            dismissButton={OutlinedButton(onClick={deleteTarget=null}){Text("Cancelar")}}
        )
    }
}
