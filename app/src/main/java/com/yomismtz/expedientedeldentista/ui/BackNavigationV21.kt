package com.yomismtz.expedientedeldentista.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * Gesto de regreso tipo Android/iOS: solo inicia desde el borde izquierdo y
 * exige un desplazamiento horizontal claro hacia la derecha. El callback usa
 * el mismo dispatcher que el botón físico/gesto del sistema, por lo que respeta
 * la pantalla anterior real en la pila de navegación.
 */
fun Modifier.edgeSwipeBackV21(onBack: () -> Unit): Modifier = composed {
    val density = LocalDensity.current
    val edgePx = with(density) { 32.dp.toPx() }
    val triggerPx = with(density) { 76.dp.toPx() }

    pointerInput(onBack, edgePx, triggerPx) {
        var eligible = false
        var accumulated = 0f
        detectHorizontalDragGestures(
            onDragStart = { start ->
                eligible = start.x <= edgePx
                accumulated = 0f
            },
            onHorizontalDrag = { _, dragAmount ->
                if (eligible) {
                    accumulated = max(0f, accumulated + dragAmount)
                    if (accumulated >= triggerPx) {
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
