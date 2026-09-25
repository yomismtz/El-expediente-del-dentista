package com.yomismtz.expedientedeldentista.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Visor único para recursos gráficos empaquetados dentro del APK.
 * No realiza solicitudes de red. Tocar abre la imagen; pellizcar amplía y
 * arrastrar permite recorrerla.
 */
@Composable
fun LocalZoomableImageV21(
    title:String,
    @DrawableRes resource:Int,
    attribution:String,
    modifier:Modifier=Modifier.fillMaxWidth()
){
    var open by remember{mutableStateOf(false)}
    Card(modifier.clickable{open=true}){
        Column(Modifier.padding(8.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
            Text(title,fontWeight=FontWeight.Bold)
            Image(painterResource(resource),title,Modifier.fillMaxWidth().heightIn(min=180.dp,max=360.dp),contentScale=ContentScale.Fit)
            Text("🔍 Toca para ampliar · recurso interno/offline",style=MaterialTheme.typography.bodySmall)
            if(attribution.isNotBlank()) Text(attribution,style=MaterialTheme.typography.bodySmall)
        }
    }
    if(open){
        var scale by remember{mutableStateOf(1f)}
        var x by remember{mutableStateOf(0f)}
        var y by remember{mutableStateOf(0f)}
        val state=rememberTransformableState{zoom,pan,_->scale=(scale*zoom).coerceIn(1f,6f);x+=pan.x;y+=pan.y}
        AlertDialog(
            onDismissRequest={open=false},
            confirmButton={TextButton(onClick={open=false}){Text("Cerrar")}},
            title={Text(title)},
            text={Column(verticalArrangement=Arrangement.spacedBy(6.dp)){
                Image(painterResource(resource),title,Modifier.fillMaxWidth().height(520.dp).graphicsLayer(scaleX=scale,scaleY=scale,translationX=x,translationY=y).transformable(state),contentScale=ContentScale.Fit)
                Text("Pellizca para ampliar y arrastra para recorrer.",style=MaterialTheme.typography.bodySmall)
                if(attribution.isNotBlank()) Text(attribution,style=MaterialTheme.typography.bodySmall)
            }}
        )
    }
}
