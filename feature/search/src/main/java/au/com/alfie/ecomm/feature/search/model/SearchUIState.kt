package au.com.alfie.ecomm.feature.search.model

import androidx.compose.runtime.Stable

@Stable
internal sealed interface SearchUIState {

    data object Loading : SearchUIState

    data class Error(val searchTerm: String) : SearchUIState

    data object Empty : SearchUIState

    data class Loaded(val searchUI: SearchUI) : SearchUIState
}
