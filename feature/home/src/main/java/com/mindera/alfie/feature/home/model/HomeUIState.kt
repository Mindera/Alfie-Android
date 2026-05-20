package com.mindera.alfie.feature.home.model

import androidx.compose.runtime.Stable

@Stable
internal sealed interface HomeUIState {

    data class Loaded(val homeUI: HomeUI) : HomeUIState
}
