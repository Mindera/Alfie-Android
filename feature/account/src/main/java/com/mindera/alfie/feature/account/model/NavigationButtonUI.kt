package com.mindera.alfie.feature.account.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.mindera.alfie.feature.uievent.UIEvent

@Stable
internal data class NavigationButtonUI(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val uiEvent: UIEvent,
    val testTag: String
)
