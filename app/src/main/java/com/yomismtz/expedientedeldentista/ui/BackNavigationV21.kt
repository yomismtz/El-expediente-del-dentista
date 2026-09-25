package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * Gesto global de regreso: un deslizamiento horizontal claro hacia la izquierda
 * vuelve a la pantalla anterior real de la pila de navegación. El callback usa
 * el mismo dispatcher que el botón físico/gesto del sistema, por lo que respeta
 * la pantalla anterior real en la pila de navegación.
 */
fun Modifier.edgeSwipeBackV21(onBack: () -> Unit): Modifier = composed {
    val density = LocalDensity.current
    val triggerPx = with(density) { 76.dp.toPx() }

    pointerInput(onBack, triggerPx) {
        var eligible = false
        var accumulated = 0f
        detectHorizontalDragGestures(
            onDragStart = { start ->
                eligible = true
                accumulated = 0f
            },
            onHorizontalDrag = { _, dragAmount ->
                if (eligible) {
                    accumulated = min(0f, accumulated + dragAmount)
                    if (accumulated <= -triggerPx) {
                        eligible = false
                        accumulated = 0f
                        onBack()
                    }
                }
            },
            onDragEnd = {
                eligible = false
                accumulated = 0f
            },
            onDragCancel = {
                eligible = false
                accumulated = 0f
            }
        )
    }
}
