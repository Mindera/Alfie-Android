package com.mindera.alfie.designsystem.component.tab

import androidx.annotation.DrawableRes
import com.mindera.alfie.core.commons.string.StringResource

data class TabItem(
    val label: StringResource,
    @DrawableRes val icon: Int? = null
)
