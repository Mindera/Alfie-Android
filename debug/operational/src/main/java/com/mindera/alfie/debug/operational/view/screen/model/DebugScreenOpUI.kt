package com.mindera.alfie.debug.operational.view.screen.model

import androidx.annotation.DrawableRes
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.ramcosta.composedestinations.spec.Direction

internal sealed interface DebugScreenOpUI {

    data class Header(val title: StringResource) : DebugScreenOpUI

    data class TextItem(
        val title: StringResource,
        val value: StringResource
    ) : DebugScreenOpUI

    data class SwitchItem(
        val title: StringResource,
        val checked: Boolean,
        val onSwitch: (Boolean) -> Unit
    ) : DebugScreenOpUI

    data class NavigationItem(
        val title: StringResource,
        val direction: Direction
    ) : DebugScreenOpUI

    data class EventItem(
        val title: StringResource,
        val event: DebugScreenEvent,
        @DrawableRes val icon: Int = AlfieIcons.ChevronRight
    ) : DebugScreenOpUI
}
