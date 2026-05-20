package com.mindera.alfie.designsystem.component.input

import androidx.annotation.DrawableRes
import com.mindera.alfie.core.ui.event.ClickEvent

data class TextFieldIconData(
    @DrawableRes val icon: Int,
    val iconContentDescription: String? = null,
    val onIconClickEvent: ClickEvent = {}
)
