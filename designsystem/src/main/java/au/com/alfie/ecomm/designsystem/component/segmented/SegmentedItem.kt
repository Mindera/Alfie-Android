package au.com.alfie.ecomm.designsystem.component.segmented

import androidx.annotation.DrawableRes
import au.com.alfie.ecomm.core.commons.string.StringResource

data class SegmentedItem(
    val label: StringResource,
    @DrawableRes val icon: Int? = null
)
