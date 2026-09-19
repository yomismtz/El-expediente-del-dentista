package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.clinical.AppScreen

private data class MenuV40(val es:String,val en:String,val icon:String,val screen:AppScreen?=null,val extra:FolderExtraV33?=null)
private val leftV40=listOf(
    MenuV40("Autorización de actividades","Activity authorization","✓",AppScreen.ACTIVITIES),
    MenuV40("Diagnóstico y tratamiento","Diagnosis and treatment","📝",AppScreen.TREATMENT),
    MenuV40("Tratamiento por sesiones","Treatment sessions","🗓",AppScreen.SESSIONS),
    MenuV40("Ficha endodóntica","Endodontic record","⚡",AppScreen.ENDO),
    MenuV40("Ficha protésica","Prosthetic record","👑",AppScreen.PROSTHETIC),
    MenuV40("Ficha periodontal / periodontograma","Periodontal record / periodontogram","📈",AppScreen.PERIODONTOGRAM),
    MenuV40("Ficha quirúrgica","Surgical record","✚",AppScreen.SURGICAL),
    MenuV40("Ficha de trastornos temporomandibulares","Temporomandibular disorders record","◉",AppScreen.ATM),
    MenuV40("Signos vitales y glucosa capilar","Vital signs and capillary glucose","❤️",AppScreen.VITALS),
    MenuV40("Calculadora","Clinical calculators","🧮",extra=FolderExtraV33.CALCULATORS),
    MenuV40("O’Leary","O’Leary plaque index","🔴",AppScreen.OLEARY),
    MenuV40("CAMBRA","CAMBRA","🛡",extra=FolderExtraV33.CAMBRA)
)
private val rightV40=listOf(
    MenuV40("Ficha de identificación","Identification record","👤",AppScreen.IDENTIFICATION),
    MenuV40("Historia clínica","Clinical history","🩺",extra=FolderExtraV33.HISTORY_HUB),
    MenuV40("Examen de mucosas","Oral mucosa examination","👄",AppScreen.MUCOSA),
    MenuV40("Auxiliares de diagnóstico","Diagnostic aids","🧪",AppScreen.AUXILIARIES),
    MenuV40("Odontograma e índices","Odontogram and indices","🦷",extra=FolderExtraV33.ODONTOGRAM_HUB),
    MenuV40("Presupuesto","Budget","$",AppScreen.BUDGET),
    MenuV40("Consentimiento informado","Informed consent","✍",AppScreen.CONSENT),
    MenuV40("Solicitud de tratamiento","Treatment request","📨",AppScreen.REQUEST),
    MenuV40("Notas de evolución","Progress notes","📄",AppScreen.EVOLUTION),
    MenuV40("Exploración de cabeza y cuello","Head and neck examination","👤",extra=FolderExtraV33.HEAD_NECK),
    MenuV40("Exploración de ATM","TMJ examination","◉",AppScreen.ATM),
    MenuV40("Examen de oclusión","Occlusion examination","↔",AppScreen.OCCLUSION),
    MenuV40("Anomalías dentales","Dental anomalies","◆",extra=FolderExtraV33.DENTAL_ANOMALIES),
    MenuV40("Erupción y posición","Eruption and position","↥",extra=FolderExtraV33.ERUPTION_ANOMALIES),
    MenuV40("Hábitos y parafunciones","Habits and parafunctions","🦷",extra=FolderExtraV33.HABITS)
)

@Composable
internal fun FolderMenuV40Screen(lang:String,onNavigate:(AppScreen)->Unit,onExtra:(FolderExtraV33)->Unit,onIntake:()->Unit,onSettings:()->Unit,onBack:()->Unit){
    ResponsiveScreenV17(
        tr(lang,"Expediente del dentista · guía interactiva","The dentist's record · interactive guide"),
        tr(lang,"Selecciona un apartado para aprender qué registrar, cómo escribirlo y cómo interpretar el resultado. No sustituye un expediente clínico real.","Choose a section to learn what to record, how to write it and how to interpret the result. It does not replace a real clinical record."),
        onBack
    ){profile->
        AdaptiveGridV17(2,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->
            val title=if(i==0) tr(lang,"Nota de ingreso","Intake note") else tr(lang,"Configuración","Settings")
            val detail=if(i==0) tr(lang,"Comienza el recorrido clínico educativo","Start the educational clinical workflow") else tr(lang,"Idioma, profesional, paleta y accesibilidad","Language, clinician, palette and accessibility")
            val icon=if(i==0)"📋" else "⚙"
            val color=if(i==0)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            Card(onClick=if(i==0)onIntake else onSettings,modifier=Modifier.fillMaxWidth().heightIn(min=72.dp),colors=CardDefaults.cardColors(containerColor=color),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),shape=MaterialTheme.shapes.large,elevation=CardDefaults.cardElevation(defaultElevation=1.dp)){
                Column(Modifier.fillMaxWidth().padding(VisualSpacingV49.lg),verticalArrangement=Arrangement.spacedBy(VisualSpacingV49.xs)){
                    Text(title,Modifier.fillMaxWidth(),textAlign=TextAlign.Center,fontWeight=FontWeight.SemiBold,style=MaterialTheme.typography.titleMedium)
                    Text(detail,Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Text(tr(lang,"Apartados del expediente","Clinical record sections"),style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=4.dp))
        val stack=profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT
        if(stack){MenuColumnV40(leftV40,lang,MaterialTheme.colorScheme.tertiaryContainer,onNavigate,onExtra);MenuColumnV40(rightV40,lang,MaterialTheme.colorScheme.secondaryContainer,onNavigate,onExtra)}
        else Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(VisualSpacingV49.md)){
            Column(Modifier.weight(1f)){MenuColumnV40(leftV40,lang,MaterialTheme.colorScheme.tertiaryContainer,onNavigate,onExtra)}
            Column(Modifier.weight(1f)){MenuColumnV40(rightV40,lang,MaterialTheme.colorScheme.secondaryContainer,onNavigate,onExtra)}
        }
    }
}

@Composable private fun MenuColumnV40(items:List<MenuV40>,lang:String,color:androidx.compose.ui.graphics.Color,onNavigate:(AppScreen)->Unit,onExtra:(FolderExtraV33)->Unit){
    Column(verticalArrangement=Arrangement.spacedBy(VisualSpacingV49.sm)){items.forEach{item->
        Card(onClick={item.extra?.let(onExtra)?:item.screen?.let(onNavigate)},modifier=Modifier.fillMaxWidth().heightIn(min=58.dp),colors=CardDefaults.cardColors(containerColor=color),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),shape=MaterialTheme.shapes.medium,elevation=CardDefaults.cardElevation(defaultElevation=1.dp)){
            Text(if(lang=="en")item.en else item.es,Modifier.fillMaxWidth().padding(horizontal=VisualSpacingV49.md,vertical=VisualSpacingV49.md),textAlign=TextAlign.Center,fontWeight=FontWeight.SemiBold,style=MaterialTheme.typography.bodyLarge)
        }
    }}
}

private enum class HistoryV40{IDENTIFICATION,COMPLAINT,FAMILY,NONPATH,PATH,SURGICAL,PHYSICAL,ORTHO}
private data class HistoryCardV40(val section:HistoryV40,val title:String,val detail:String)
private val historyCardsV40=listOf(
    HistoryCardV40(HistoryV40.IDENTIFICATION,"Identificación del paciente","Qué datos suelen ir en el expediente real y para qué sirven; aquí no se capturan datos personales."),
    HistoryCardV40(HistoryV40.COMPLAINT,"Motivo de consulta y padecimiento actual","30 motivos frecuentes y tres hipótesis de padecimiento actual por ejemplo."),
    HistoryCardV40(HistoryV40.FAMILY,"Antecedentes heredo-familiares","Familiares maternos/paternos y categorías de enfermedad."),
    HistoryCardV40(HistoryV40.NONPATH,"Antecedentes personales no patológicos","Vivienda, servicios, higiene, alimentación, hábitos, perforaciones y exposiciones."),
    HistoryCardV40(HistoryV40.PATH,"Antecedentes personales patológicos","Vacunación, enfermedades, tiempo de evolución, estado y medicamentos referidos."),
    HistoryCardV40(HistoryV40.SURGICAL,"Antecedentes quirúrgicos y traumáticos","Cirugías, hospitalizaciones, transfusiones, fracturas/luxaciones y resumen."),
    HistoryCardV40(HistoryV40.PHYSICAL,"Exploración física","Dos rutas: signos vitales/glucosa e inspección general."),
    HistoryCardV40(HistoryV40.ORTHO,"Antecedentes ortodónticos","Tratamientos previos, aparatos, duración y motivo.")
)

@Composable fun HistoryHubV40Screen(lang:String,onVitals:()->Unit,onBack:()->Unit){
    var section by remember{mutableStateOf<HistoryV40?>(null)}
    when(section){
        HistoryV40.IDENTIFICATION->{PatientIdentificationV36Screen(lang){section=null};return}
        HistoryV40.COMPLAINT->{ChiefComplaintV40Screen(lang){section=null};return}
        HistoryV40.FAMILY->{FamilyHistoryV37Screen(lang){section=null};return}
        HistoryV40.NONPATH->{NonPathologicalV40Screen(lang){section=null};return}
        HistoryV40.PATH->{PathologicalV43Screen(lang){section=null};return}
        HistoryV40.SURGICAL->{SurgicalTraumaHistoryV40Screen(lang){section=null};return}
        HistoryV40.PHYSICAL->{PhysicalExamHubV40Screen(lang,onVitals){section=null};return}
        HistoryV40.ORTHO->{OrthodonticHistoryV33Screen(lang){section=null};return}
        null->Unit
    }
    ResponsiveScreenV17("Historia clínica","ATM y oclusión se dejaron únicamente en el menú principal para evitar duplicados.",onBack){profile->
        AdaptiveGridV17(historyCardsV40.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->val x=historyCardsV40[i];Card(onClick={section=x.section},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),shape=MaterialTheme.shapes.large,elevation=CardDefaults.cardElevation(defaultElevation=1.dp)){Column(Modifier.padding(VisualSpacingV49.lg),verticalArrangement=Arrangement.spacedBy(VisualSpacingV49.xs)){Text(x.title,fontWeight=FontWeight.SemiBold);Text(x.detail,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSecondaryContainer)}}}
    }
}