package au.com.alfie.ecomm.feature.home

import androidx.lifecycle.ViewModel
import au.com.alfie.ecomm.feature.home.model.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val uiFactory: HomeUIFactory
) : ViewModel() {

    // TODO: get actual state
    private val _state = MutableStateFlow(
        HomeUIState.Loaded(uiFactory())
    )
    val state: StateFlow<HomeUIState> = _state
}
