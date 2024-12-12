package au.com.alfie.ecomm.feature.account.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.feature.uievent.UIEvent

@Stable
internal data class NavigationButtonUI(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val uiEvent: UIEvent,
    val testTag: String
)
