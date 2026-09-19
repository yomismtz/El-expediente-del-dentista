package com.yomismtz.expedientedeldentista.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.yomismtz.expedientedeldentista.R
import com.yomismtz.expedientedeldentista.settings.ClinicianTitle

@DrawableRes
internal fun clinicianPortraitResourceV47(title: ClinicianTitle): Int = when (title) {
    ClinicianTitle.DOCTOR -> R.drawable.clinician_doctor_photo
    ClinicianTitle.DOCTORA -> R.drawable.clinician_doctora_photo
}

/**
 * Retrato odontológico reutilizable para onboarding y portada.
 * Las fotografías se empaquetan en drawable-nodpi durante el build para funcionar offline.
 * ContentScale.Fit conserva la imagen completa y evita recortes en teléfonos, tablets y fuente grande.
 */
@Composable
internal fun ClinicianPortraitV47(
    title: ClinicianTitle,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(clinicianPortraitResourceV47(title)),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}
