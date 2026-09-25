package com.yomismtz.expedientedeldentista.ui
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
private data class ImagingType15(val name:String,val use:String)
private val imagingTypes15=listOf(
ImagingType15("Periapical","Ápices, raíces, periodonto y región periapical; detalle dentoalveolar localizado."),
ImagingType15("Bitewing / aleta de mordida","Coronas posteriores, contactos proximales, caries interproximales y cresta ósea visible."),
ImagingType15("Oclusal","Vista amplia localizada de maxilar o mandíbula."),
ImagingType15("Panorámica","Visión general de maxilares, dentición, desarrollo, retenidos/impactados y estructuras adyacentes."),
ImagingType15("Cefalométrica lateral","Relaciones craneofaciales y análisis ortodóncico con técnica estandarizada."),
ImagingType15("CBCT","Información tridimensional; justificar cuando opciones de menor exposición no aportan la información necesaria."),
ImagingType15("Resonancia magnética","Tejidos blandos y ATM; sin radiación ionizante, indicada según la pregunta clínica."))
private val studyMethodsV23=linkedMapOf(
"Cefalométrica lateral" to listOf("Steiner · SNA: posición anteroposterior maxilar respecto a base craneal; SNB: posición mandibular; ANB: relación maxilomandibular; GoGn-SN: patrón vertical; 1-NA y 1-NB: posición/inclinación incisiva. Identificar S, N, A, B, Go y Gn; trazar planos y medir ángulos/distancias. Interpretar con normas apropiadas a edad/población y contexto clínico."),
"Panorámica" to listOf("Malla sistemática · revisar calidad/simetría → cóndilos y ramas → corticales y bases → senos/cavidad nasal → canal mandibular/forámenes → dentición y desarrollo → raíces/periapical → hueso alveolar → hallazgos radiolúcidos, radiopacos o mixtos. Comparar ambos lados sin asumir que asimetría equivale a enfermedad."),
"Periapical" to listOf("Fleming/Hunt · lectura sistemática: calidad y angulación → corona/restauraciones → cámara y conductos → raíces → espacio periodontal y lámina dura → región periapical → cresta/hueso alveolar → estructuras vecinas. Describir primero densidad, límites, localización y relación anatómica antes de proponer interpretación."),
"Oclusal" to listOf("Analizar dientes/estructuras incluidas, corticales, expansión, calcificaciones y relación espacial. Para localización bucolingual se requieren dos proyecciones con diferente angulación y aplicar la regla SLOB: si el objeto se desplaza en la misma dirección que el tubo está hacia lingual; si se desplaza en dirección opuesta está hacia bucal. Una sola oclusal no permite afirmar profundidad con certeza."),
"CBCT" to listOf("Tomografía CBCT · leer en los tres planos ortogonales (axial, coronal y sagital), ajustar ventana, seguir la estructura corte a corte y correlacionar reconstrucciones. Indicarla sólo cuando una pregunta clínica requiere información 3D que cambiará diagnóstico o manejo: impactados y relación anatómica compleja, endodoncia seleccionada, patología ósea, trauma, implantes/cirugía u otras indicaciones justificadas. No es estudio rutinario."),
"Resonancia magnética" to listOf("RM · útil principalmente para tejidos blandos; en odontología destaca la evaluación de disco y tejidos de ATM y determinadas lesiones de partes blandas. Leer secuencias y planos comparando anatomía, señal, posición y movimiento cuando existan series de boca cerrada/abierta. Solicitar cuando la pregunta clínica de tejidos blandos no se resuelva con exploración u otra imagen apropiada; no usa radiación ionizante.")
)
private val findingGroups15=listOf(
"Calidad diagnóstica" to listOf("Adecuada","Movimiento","Distorsión","Superposición","Contraste/exposición inadecuada","Artefacto","Zona de interés incompleta"),
"Dentición" to listOf("Sin hallazgo aparente","Ausencia dental","Supernumerario","Retenido / incluido","Impactado","Erupción ectópica","Alteración de posición"),
"Tejidos duros dentales" to listOf("Sin hallazgo aparente","Radiolucidez coronaria compatible con pérdida mineral","Restauración","Corona protésica","Desgaste","Fractura sospechada"),
"Pulpa y endodoncia" to listOf("Sin hallazgo aparente","Cámara/conducto visible","Tratamiento endodóntico","Material corto","Material sobreextendido","Calcificación aparente","Instrumento/material radiopaco"),
"Región periapical" to listOf("Sin hallazgo aparente","Ensanchamiento del espacio periodontal","Pérdida de lámina dura","Radiolucidez periapical","Radiopacidad periapical","Hallazgo mixto"),
"Periodonto / hueso alveolar" to listOf("Sin pérdida ósea aparente","Pérdida horizontal aparente","Pérdida vertical aparente","Defecto localizado","Furcación radiográfica aparente"),
"Raíces" to listOf("Sin hallazgo aparente","Dilaceración","Reabsorción interna aparente","Reabsorción externa aparente","Raíz residual","Morfología radicular atípica"),
"Maxilares" to listOf("Sin hallazgo aparente","Radiolucidez","Radiopacidad","Lesión mixta","Expansión/asimetría aparente","Alteración cortical aparente"),
"Estructuras anatómicas" to listOf("Sin hallazgo aparente","Seno maxilar: cambio aparente","Canal mandibular: relación relevante","Foramen mentoniano identificado","ATM/cóndilo: asimetría aparente","Cavidad nasal: cambio aparente"))
@Composable fun ImagingAuxiliariesV20Screen(lang:String,onBack:()->Unit){
 var study by remember{mutableStateOf(imagingTypes15.first().name)};var group by remember{mutableStateOf(findingGroups15.first().first)}
 val selected=remember{mutableStateMapOf<String,String>()}
 ResponsiveScreenV17("Imagenología dental · registro educativo","Selecciona el estudio y registra hallazgos mediante opciones. No se genera un diagnóstico automático.",onBack){profile->
  ResponsiveSectionV17("Tipo de estudio","Elige la modalidad que corresponde al estudio disponible."){
   AdaptiveGridV17(imagingTypes15.size,if(profile.largeSystemText||profile.width==ScreenWidthV17.COMPACT)2 else 3){i->val x=imagingTypes15[i];FilterChip(study==x.name,{study=x.name},{Text(x.name)},Modifier.fillMaxWidth())}
   Text(imagingTypes15.first{it.name==study}.use,style=MaterialTheme.typography.bodySmall)
  }
  studyMethodsV23[study]?.let{methods->ResponsiveSectionV17("Cómo analizar este estudio","Método educativo, puntos y significado clínico."){methods.forEach{Text("• "+it)}}}
  ResponsiveSectionV17("Revisión sistemática","Selecciona lo observado. Los términos «aparente» y «sospechada» evitan convertir una imagen aislada en diagnóstico definitivo."){
   AdaptiveGridV17(findingGroups15.size,if(profile.width==ScreenWidthV17.COMPACT)2 else 3){i->val name=findingGroups15[i].first;FilterChip(group==name,{group=name},{Text(name)},Modifier.fillMaxWidth())}
   val opts=findingGroups15.first{it.first==group}.second
   AdaptiveGridV17(opts.size,if(profile.width==ScreenWidthV17.COMPACT)1 else 2){i->val x=opts[i];FilterChip(selected[group]==x,{selected[group]=x},{Text(x)},Modifier.fillMaxWidth())}
  }
  ResponsiveSectionV17("Resumen automático"){
   Text("Estudio: "+study,fontWeight=FontWeight.Black)
   findingGroups15.forEach{item->selected[item.first]?.let{v->Text("• "+item.first+": "+v)}}
   if(selected.isEmpty())Text("Aún no se han seleccionado hallazgos.")
  }
  ResponsiveSectionV17("Seguridad radiológica"){Text("La indicación debe individualizarse después de revisar historia, imágenes previas y exploración clínica. CBCT no debe solicitarse por rutina; se reserva para preguntas clínicas donde la información tridimensional aporte un beneficio que justifique la exposición.")}
  NoticeCard("Módulo educativo. La interpretación definitiva depende de la calidad del estudio, la correlación clínica y, cuando corresponda, del informe de radiología oral y maxilofacial.")
 }
}
