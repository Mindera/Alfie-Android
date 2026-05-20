package com.mindera.alfie.designsystem.component.segmented

import androidx.annotation.DrawableRes
import com.mindera.alfie.core.commons.string.StringResource

data class SegmentedItem(
    val label: StringResource,
    @DrawableRes val icon: Int? = null
)
