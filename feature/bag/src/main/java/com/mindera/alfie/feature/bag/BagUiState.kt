package com.mindera.alfie.feature.bag

import androidx.compose.runtime.Stable
import com.mindera.alfie.feature.bag.models.BagContentUi

@Stable
internal sealed interface BagUiState {

    sealed interface Data : BagUiState {

        data object Loading : Data

        data class Loaded(val content: BagContentUi) : Data

        data object Empty : Data
    }

    data object Error : BagUiState
}
