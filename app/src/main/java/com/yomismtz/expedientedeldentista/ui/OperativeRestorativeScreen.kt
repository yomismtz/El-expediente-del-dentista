package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomismtz.expedientedeldentista.R

private data class OperativeStep(val es:String,val en:String,val descEs:String,val descEn:String,val image:Int)

@Composable
fun OperativeRestorativeScreen(lang:String,onBack:()->Unit) {
    val steps=listOf(
        OperativeStep("Aislamiento","Isolation","Control educativo de humedad y protección del campo operatorio antes de intervenir.","Educational moisture control and field protection before treatment.",R.drawable.clinical_operatoria_aislamiento),
        OperativeStep("Preparación cavitaria","Cavity preparation","Representación educativa de una preparación conservadora; la extensión real depende del diagnóstico y del material.","Educational representation of conservative preparation; actual extension depends on diagnosis and material.",R.drawable.clinical_operatoria_preparacion_cavitaria),
        OperativeStep("Grabado ácido","Acid etching","Acondicionamiento del sustrato siguiendo el sistema adhesivo y las instrucciones del fabricante.","Condition the substrate according to the adhesive system and manufacturer instructions.",R.drawable.clinical_operatoria_grabado_acido),
        OperativeStep("Adhesivo","Adhesive","Aplicación y manejo del sistema adhesivo conforme al protocolo del material.","Apply and manage the adhesive system according to the material protocol.",R.drawable.clinical_operatoria_adhesivo),
        OperativeStep("Resina incremental","Incremental composite","Colocación por incrementos con control de adaptación y anatomía.","Place controlled increments while managing adaptation and anatomy.",R.drawable.clinical_operatoria_resina_incremental),
        OperativeStep("Fotocurado","Light curing","Polimerización de cada incremento con el equipo y tiempo indicados por el fabricante.","Polymerize each increment with the equipment and time specified by the manufacturer.",R.drawable.clinical_operatoria_fotocurado),
        OperativeStep("Ajuste oclusal","Occlusal adjustment","Verificación de contactos y ajuste conservador cuando esté indicado.","Verify contacts and perform conservative adjustment when indicated.",R.drawable.clinical_operatoria_ajuste_oclusal),
        OperativeStep("Pulido","Polishing","Acabado superficial y control final de anatomía, contactos y textura.","Finish the surface and check anatomy, contacts and texture.",R.drawable.clinical_operatoria_pulido)
    )
    LazyColumn(Modifier.fillMaxWidth().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { ScreenHeader(tr(lang,"Operatoria y restauración","Operative and restorative dentistry"),onBack,tr(lang,"Secuencia visual educativa integrada al expediente. Las ilustraciones son esquemáticas y no sustituyen protocolo clínico.","Educational visual sequence integrated into the record. Illustrations are schematic and do not replace clinical protocols.")) }
        items(steps) { step ->
            Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text(if(lang=="en")step.en else step.es,fontWeight=FontWeight.Black,style=MaterialTheme.typography.titleMedium)
                    Image(painterResource(step.image),contentDescription=if(lang=="en")step.en else step.es,modifier=Modifier.fillMaxWidth())
                    Text(if(lang=="en")step.descEn else step.descEs)
                }
            }
        }
        item { NoticeCard(tr(lang,"La ficha se integra como referencia educativa; documenta únicamente lo que realmente se haya realizado y valida el protocolo del material, fabricante e institución.","This sheet is an educational reference; document only what was actually performed and validate the material, manufacturer and institutional protocol.")) }
    }
}
