package au.com.alfie.ecomm.designsystem.component.input

import androidx.annotation.DrawableRes
import au.com.alfie.ecomm.core.ui.event.ClickEvent

data class TextFieldIconData(
    @DrawableRes val icon: Int,
    val iconContentDescription: String? = null,
    val onIconClickEvent: ClickEvent = {}
)
