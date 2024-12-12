package au.com.alfie.ecomm.debug.operational.view.screen.model

import androidx.annotation.DrawableRes
import au.com.alfie.ecomm.core.commons.string.StringResource
import com.ramcosta.composedestinations.spec.Direction
import au.com.alfie.ecomm.designsystem.R as RD

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
        @DrawableRes val icon: Int = RD.drawable.ic_action_chevron_right
    ) : DebugScreenOpUI
}
