package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

private data class MenuV40(val es:String,val icon:String,val screen:AppScreen?=null,val extra:FolderExtraV33?=null)
private val leftV40=listOf(
    MenuV40("Autorización de actividades","✓",AppScreen.ACTIVITIES),
    MenuV40("Diagnóstico y tratamiento","📝",AppScreen.TREATMENT),
    MenuV40("Tratamiento por sesiones","🗓",AppScreen.SESSIONS),
    MenuV40("Ficha endodóntica","⚡",AppScreen.ENDO),
    MenuV40("Ficha protésica","👑",AppScreen.PROSTHETIC),
    MenuV40("Ficha periodontal / periodontograma","📈",AppScreen.PERIODONTOGRAM),
    MenuV40("Ficha quirúrgica","✚",AppScreen.SURGICAL),
    MenuV40("Ficha de trastornos temporomandibulares","◉",AppScreen.ATM),
    MenuV40("Signos vitales y glucosa capilar","❤️",AppScreen.VITALS),
    MenuV40("Calculadora","🧮",extra=FolderExtraV33.CALCULATORS),
    MenuV40("O’Leary","🔴",AppScreen.OLEARY),
    MenuV40("CAMBRA","🛡",extra=FolderExtraV33.CAMBRA)
)
private val rightV40=listOf(
    MenuV40("Ficha de identificación","👤",AppScreen.IDENTIFICATION),
    MenuV40("Historia clínica","🩺",extra=FolderExtraV33.HISTORY_HUB),
    MenuV40("Examen de mucosas","👄",AppScreen.MUCOSA),
    MenuV40("Auxiliares de diagnóstico","🧪",AppScreen.AUXILIARIES),
    MenuV40("Odontograma e índices","🦷",extra=FolderExtraV33.ODONTOGRAM_HUB),
    MenuV40("Presupuesto","$",AppScreen.BUDGET),
    MenuV40("Consentimiento informado","✍",AppScreen.CONSENT),
    MenuV40("Solicitud de tratamiento","📨",AppScreen.REQUEST),
    MenuV40("Notas de evolución","📄",AppScreen.EVOLUTION),
    MenuV40("Exploración de cabeza y cuello","👤",extra=FolderExtraV33.HEAD_NECK),
    MenuV40("Exploración de ATM","◉",AppScreen.ATM),
    MenuV40("Examen de oclusión","↔",AppScreen.OCCLUSION),
    MenuV40("Anomalías dentales","◆",extra=FolderExtraV33.DENTAL_ANOMALIES),
    MenuV40("Erupción y posición","↥",extra=FolderExtraV33.ERUPTION_ANOMALIES),
    MenuV40("Hábitos y parafunciones","🦷",extra=FolderExtraV33.HABITS)
)

@Composable
internal fun FolderMenuV40Screen(
    lang:String,
    onNavigate:(AppScreen)->Unit,
    onExtra:(FolderExtraV33)->Unit,
    onIntake:()->Unit,
    onSettings:()->Unit,
    onBack:()->Unit
){
    ResponsiveScreenV17("Expediente del dentista · guía interactiva","La app enseña a llenar e interpretar apartados; no crea un expediente real.",onBack){profile->
        AdaptiveGridV17(2,if(profile.largeSystemText)1 else 2){i->
            val title=if(i==0) tr(lang,"Nota de ingreso","Intake note") else tr(lang,"Configuración","Settings")
            val icon=if(i==0)"📋" else "⚙"
            val color=if(i==0)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            Card(
                onClick=if(i==0)onIntake else onSettings,
                modifier=Modifier.fillMaxWidth(),
                colors=CardDefaults.cardColors(containerColor=color),
                border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline),
                shape=RoundedCornerShape(12.dp)
            ){
                Text("$icon $title",Modifier.fillMaxWidth().padding(13.dp),textAlign=TextAlign.Center,fontWeight=FontWeight.Black)
            }
        }
        val stack=profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT
        if(stack){MenuColumnV40(leftV40,MaterialTheme.colorScheme.tertiaryContainer,onNavigate,onExtra);MenuColumnV40(rightV40,MaterialTheme.colorScheme.secondaryContainer,onNavigate,onExtra)}
        else Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(12.dp)){
            Column(Modifier.weight(1f)){MenuColumnV40(leftV40,MaterialTheme.colorScheme.tertiaryContainer,onNavigate,onExtra)}
            Column(Modifier.weight(1f)){MenuColumnV40(rightV40,MaterialTheme.colorScheme.secondaryContainer,onNavigate,onExtra)}
        }
    }
}

@Composable private fun MenuColumnV40(items:List<MenuV40>,color:androidx.compose.ui.graphics.Color,onNavigate:(AppScreen)->Unit,onExtra:(FolderExtraV33)->Unit){
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)){items.forEach{item->Card(onClick={item.extra?.let(onExtra)?:item.screen?.let(onNavigate)},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=color),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline),shape=RoundedCornerShape(12.dp)){Text("${item.icon} ${item.es}",Modifier.fillMaxWidth().padding(13.dp),textAlign=TextAlign.Center,fontWeight=FontWeight.Black)}}}
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

@Composable
fun HistoryHubV40Screen(lang:String,onVitals:()->Unit,onBack:()->Unit){
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
        AdaptiveGridV17(historyCardsV40.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)1 else 2){i->val x=historyCardsV40[i];Card(onClick={section=x.section},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer),border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline)){Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){Text(x.title,fontWeight=FontWeight.Black);Text(x.detail,style=MaterialTheme.typography.bodySmall)}}}
    }
}