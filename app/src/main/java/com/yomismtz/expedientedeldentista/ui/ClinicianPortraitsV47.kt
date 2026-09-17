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
    ClinicianTitle.DOCTOR -> R.drawable.doctor_lori_anime
    ClinicianTitle.DOCTORA -> R.drawable.doctora_agaporni_anime
}

/**
 * Retrato odontológico reutilizable para onboarding y portada.
 * Usa Fit para conservar la ilustración completa y evitar recortes en pantallas compactas.
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
