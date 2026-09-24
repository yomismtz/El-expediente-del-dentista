package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.yomismtz.expedientedeldentista.clinical.EducationalSession
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R

private val kennedyV40=listOf(
    "Kennedy I" to "Áreas edéntulas posteriores bilaterales a los dientes remanentes.",
    "Kennedy II" to "Área edéntula posterior unilateral a los dientes remanentes.",
    "Kennedy III" to "Área edéntula unilateral limitada por dientes anterior y posteriormente.",
    "Kennedy IV" to "Única área edéntula anterior que cruza la línea media.",
    "Edéntulo total" to "No corresponde a Kennedy; requiere diagnóstico y plan de prótesis total/otras opciones.",
    "Sin espacio edéntulo" to "No aplica clasificación de Kennedy."
)

private val kennedyHowV44 = listOf(
    "Kennedy I" to "Observa dos extensiones distales libres: faltan dientes posteriores en ambos lados y los dientes remanentes quedan por delante de ambas áreas edéntulas.",
    "Kennedy II" to "Existe una sola extensión distal libre posterior; el lado contrario conserva soporte dental posterior.",
    "Kennedy III" to "La brecha edéntula está limitada por dientes remanentes tanto por delante como por detrás; es un espacio intercalar.",
    "Kennedy IV" to "Existe una sola brecha anterior que cruza la línea media y queda por delante de todos los dientes remanentes.",
    "Edéntulo total" to "No hay dientes remanentes en la arcada; Kennedy clasifica edentulismo parcial, por lo que no aplica.",
    "Sin espacio edéntulo" to "No existe una brecha que clasificar."
)

@Composable
fun ProstheticBridgeV40Screen(lang:String,onBack:()->Unit){
    var full by remember{mutableStateOf(false)}
    var selected by remember{mutableStateOf(0)}
    var arch by remember{mutableStateOf("Maxilar")}
    var localSession by remember { mutableStateOf(EducationalSession()) }
    if(full){ProstheticResponsiveV17Screen(lang,localSession,{ localSession=it }){full=false};return}
    ResponsiveScreenV17(
        tr(lang,"Ficha protésica · Clasificación de Kennedy","Prosthetic sheet · Kennedy classification"),
        tr(lang,"Referencia visual e identificación interactiva de las clases I–IV.","Visual reference and interactive identification of Classes I–IV."),
        onBack
    ){_->
        ResponsiveSectionV17(tr(lang,"Referencia visual proporcionada por la autora","Visual reference provided by the author")){
            Image(
                painter=painterResource(R.drawable.kennedy_reference),
                contentDescription=tr(lang,"Clasificación de Kennedy clases I, II, III y IV","Kennedy classification Classes I, II, III and IV"),
                modifier=Modifier.fillMaxWidth().height(235.dp),
                contentScale=ContentScale.Fit
            )
            Text(tr(lang,"La imagen se conserva completa, sin recorte ni rediseño.","The image is kept complete, without cropping or redesign."),style=MaterialTheme.typography.bodySmall)
        }
        ResponsiveSectionV17("Arcada del ejercicio"){
            listOf("Maxilar","Mandibular","Ambas").forEach{x->FilterChip(arch==x,{arch=x},{Text(x)},Modifier.fillMaxWidth())}
        }
        ResponsiveSectionV17("Diagnóstico protésico"){
            kennedyV40.forEachIndexed{i,(name,detail)->
                Card(onClick={selected=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(selected==i)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)){
                    Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){
                        Text(name,fontWeight=FontWeight.Black)
                        if(selected==i){
                            Text("¿Qué es?",fontWeight=FontWeight.Black,color=MaterialTheme.colorScheme.primary)
                            Text(detail)
                            Text("¿Cómo identificarla?",fontWeight=FontWeight.Black,color=MaterialTheme.colorScheme.primary)
                            Text(kennedyHowV44[i].second)
                        }
                    }
                }
            }
            val summary="$arch · ${kennedyV40[selected].first} · ${kennedyV40[selected].second}"
            Card(onClick={TeachingStateV40.moduleSummaries["prosthetic"]=summary},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Text("Guardar diagnóstico protésico",Modifier.padding(12.dp),fontWeight=FontWeight.Black)}
        }
        NoticeCard("Reglas de Applegate: la clasificación se determina después de las extracciones que puedan modificarla; el área edéntula más posterior determina la clase; las áreas adicionales son modificaciones por número, no por extensión; la Clase IV no admite modificaciones.")
        Card(onClick={full=true},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){Text("Abrir ficha protésica completa existente",Modifier.padding(14.dp),fontWeight=FontWeight.Black)}
    }
}
