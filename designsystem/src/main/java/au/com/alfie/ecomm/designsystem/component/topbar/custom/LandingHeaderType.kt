package au.com.alfie.ecomm.designsystem.component.topbar.custom

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.designsystem.R

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
