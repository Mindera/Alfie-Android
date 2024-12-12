package au.com.alfie.ecomm.designsystem.component.input

import androidx.annotation.DrawableRes

data class TextFieldSupportComponent(
    val text: String,
    @DrawableRes val icon: Int? = null,
    val iconContentDescription: String? = null
)
