package com.yomismtz.expedientedeldentista.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object VisualSpacingV49 {
    val xxs: Dp = 4.dp
    val xs: Dp = 6.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 20.dp
    val xxl: Dp = 24.dp
}

@Composable
fun clinicalCariesColorV49(): Color = MaterialTheme.colorScheme.error

@Composable
fun clinicalRestorationColorV49(): Color = MaterialTheme.colorScheme.primary

@Composable
fun clinicalSealantColorV49(): Color = MaterialTheme.colorScheme.tertiary

@Composable
fun clinicalHealthySurfaceV49(): Color = MaterialTheme.colorScheme.surfaceVariant


/**
 * Displays an APK-bundled raster image without cropping, stretching or enlarging it
 * beyond its intrinsic width. This is deliberately conservative for clinical media:
 * a small source remains small instead of becoming a blurry full-width image.
 */
@Composable
fun OfflineClinicalImageV50(
    @DrawableRes drawable: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    maxHeight: Dp = 480.dp
) {
    val painter = painterResource(drawable)
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val intrinsicWidthDp = with(density) { painter.intrinsicSize.width.toDp() }
        val targetWidth = if (
            intrinsicWidthDp.value.isFinite() &&
            intrinsicWidthDp > 0.dp
        ) {
            minOf(maxWidth, intrinsicWidthDp)
        } else {
            maxWidth
        }

        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.width(targetWidth).heightIn(max = maxHeight),
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * Shared frame for clinical photos, radiographs and educational anatomical illustrations.
 *
 * Visual policy:
 * - clinical identification content: real photograph/radiograph whenever available;
 * - explanatory graphics: semirealistic educational illustration rather than cartoon art;
 * - neutral Material 3 frame so photographs and illustrations can coexist without looking
 *   like different applications.
 */
@Composable
fun EducationalVisualFrameV49(
    title: String? = null,
    caption: String? = null,
    credit: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(VisualSpacingV49.md),
            verticalArrangement = Arrangement.spacedBy(VisualSpacingV49.sm)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(VisualSpacingV49.xs)
            ) {
                content()
            }

            if (!title.isNullOrBlank()) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (!caption.isNullOrBlank()) {
                Text(
                    caption,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!credit.isNullOrBlank()) {
                Text(
                    credit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
