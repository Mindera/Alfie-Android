package au.com.alfie.ecomm.designsystem.component.bottombar

import androidx.annotation.DrawableRes
import au.com.alfie.ecomm.core.commons.string.StringResource

interface BottomBarItem {

    val state: BottomBarItemState

    @get:DrawableRes
    val icon: Int

    val label: StringResource

    val testTag: String
}
