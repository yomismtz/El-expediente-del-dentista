package com.yomismtz.expedientedeldentista.ui

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Reproductor local/offline para GIF animados empaquetados en res/drawable-nodpi.
 * No usa red ni dependencias externas. En Android 9+ conserva la animación GIF.
 */
@Composable
fun LocalAnimatedGifV49(
    lang:String,
    title:String,
    @DrawableRes resource:Int,
    observation:String=""
) {
    val context=LocalContext.current
    val animated=remember(resource) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P) {
            runCatching {
                val source=ImageDecoder.createSource(context.resources,resource)
                ImageDecoder.decodeDrawable(source).also { drawable ->
                    (drawable as? AnimatedImageDrawable)?.repeatCount=AnimatedImageDrawable.REPEAT_INFINITE
                }
            }.getOrNull()
        } else null
    }
    DisposableEffect(animated) {
        (animated as? AnimatedImageDrawable)?.start()
        onDispose { (animated as? AnimatedImageDrawable)?.stop() }
    }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(10.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
            Text(title,fontWeight=FontWeight.Bold)
            AndroidView(
                factory={ctx->ImageView(ctx).apply {
                    adjustViewBounds=true
                    scaleType=ImageView.ScaleType.FIT_CENTER
                }},
                update={view->
                    if(animated!=null) {
                        view.setImageDrawable(animated)
                        (animated as? AnimatedImageDrawable)?.start()
                    } else {
                        view.setImageResource(resource)
                    }
                },
                modifier=Modifier.fillMaxWidth().heightIn(min=180.dp,max=360.dp)
            )
            if(observation.isNotBlank()) Text("👁 "+observation,style=MaterialTheme.typography.bodySmall)
            Text(tr(lang,"Animación local incluida en el APK · funciona sin Internet.","Local animation included in the APK · works offline."),style=MaterialTheme.typography.bodySmall)
        }
    }
}
