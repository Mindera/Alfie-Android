package com.mindera.alfie.designsystem.component.bottombar

import androidx.annotation.DrawableRes
import com.mindera.alfie.core.commons.string.StringResource

interface BottomBarItem {

    val state: BottomBarItemState

    @get:DrawableRes
    val icon: Int

    val label: StringResource

    val testTag: String
}
