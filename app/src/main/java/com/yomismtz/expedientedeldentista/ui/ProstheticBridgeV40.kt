package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val kennedyV40=listOf(
    "Kennedy I" to "Áreas edéntulas posteriores bilaterales a los dientes remanentes.",
    "Kennedy II" to "Área edéntula posterior unilateral a los dientes remanentes.",
    "Kennedy III" to "Área edéntula unilateral limitada por dientes anterior y posteriormente.",
    "Kennedy IV" to "Única área edéntula anterior que cruza la línea media.",
    "Edéntulo total" to "No corresponde a Kennedy; requiere diagnóstico y plan de prótesis total/otras opciones.",
    "Sin espacio edéntulo" to "No aplica clasificación de Kennedy."
)

@Composable
fun ProstheticBridgeV40Screen(lang:String,onBack:()->Unit){
    var full by remember{mutableStateOf(false)}
    var selected by remember{mutableStateOf(0)}
    var arch by remember{mutableStateOf("Maxilar")}
    if(full){ProstheticResponsiveV17Screen(lang){full=false};return}
    ResponsiveScreenV17("Ficha protésica · resumen Kennedy","Conserva el módulo protésico existente y añade un resumen que puede integrarse a la ficha de identificación.",onBack){_->
        ResponsiveSectionV17("Arcada del ejercicio"){
            listOf("Maxilar","Mandibular","Ambas").forEach{x->FilterChip(arch==x,{arch=x},{Text(x)},Modifier.fillMaxWidth())}
        }
        ResponsiveSectionV17("Diagnóstico protésico"){
            kennedyV40.forEachIndexed{i,(name,detail)->
                Card(onClick={selected=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(selected==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){
                    Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){Text(name,fontWeight=FontWeight.Black);if(selected==i)Text(detail)}
                }
            }
            val summary="$arch · ${kennedyV40[selected].first} · ${kennedyV40[selected].second}"
            Card(onClick={TeachingStateV40.moduleSummaries["prosthetic"]=summary},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Text("Guardar diagnóstico protésico",Modifier.padding(12.dp),fontWeight=FontWeight.Black)}
        }
        Card(onClick={full=true},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){Text("Abrir ficha protésica completa existente",Modifier.padding(14.dp),fontWeight=FontWeight.Black)}
    }
}
