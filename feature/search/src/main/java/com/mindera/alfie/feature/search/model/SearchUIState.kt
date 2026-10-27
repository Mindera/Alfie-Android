package com.mindera.alfie.feature.search.model

import androidx.compose.runtime.Stable

@Stable
internal sealed interface SearchUIState {

    /** Nothing typed: the overlay shows recent searches. */
    data object Empty : SearchUIState

    data object Loading : SearchUIState

    data class NoResults(val searchTerm: String) : SearchUIState

    data class Error(val searchTerm: String) : SearchUIState

    data class Loaded(val searchUI: SearchUI) : SearchUIState
}
