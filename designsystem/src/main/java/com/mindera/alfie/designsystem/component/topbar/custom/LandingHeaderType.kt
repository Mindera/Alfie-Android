package com.mindera.alfie.designsystem.component.topbar.custom

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.R

@Stable
sealed interface LandingHeaderType {

    data class Greeting(
        val userName: String,
        val subtitle: String? = null
    ) : LandingHeaderType

    data class Logo(
        @DrawableRes
        val icon: Int = R.drawable.ic_alfie_logo_dark,
        val contentDescription: String? = null
    ) : LandingHeaderType
}
