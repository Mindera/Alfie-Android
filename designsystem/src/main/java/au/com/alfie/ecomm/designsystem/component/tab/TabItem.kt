package au.com.alfie.ecomm.designsystem.component.tab

import androidx.annotation.DrawableRes
import au.com.alfie.ecomm.core.commons.string.StringResource

data class TabItem(
    val label: StringResource,
    @DrawableRes val icon: Int? = null
)
