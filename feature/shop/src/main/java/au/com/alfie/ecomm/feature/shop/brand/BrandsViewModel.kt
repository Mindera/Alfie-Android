package au.com.alfie.ecomm.feature.shop.brand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.domain.usecase.brand.GetBrandsUseCase
import au.com.alfie.ecomm.feature.shop.brand.factory.BrandUIStateFactory
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEvent
import au.com.alfie.ecomm.feature.shop.brand.model.BrandUIState
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntry
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntryDelegate
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BrandsViewModel @Inject constructor(
    private val getBrandsUseCase: GetBrandsUseCase,
    private val uiFactory: BrandUIStateFactory,
    navigateToEntryDelegate: NavigateToEntryDelegate,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(),
    NavigateToEntry by navigateToEntryDelegate,
    UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<BrandUIState>(BrandUIStateFactory.PLACEHOLDER)
    val state: StateFlow<BrandUIState> = _state.asStateFlow()

    private var entries: ImmutableList<BrandEntryUI> = persistentListOf()

    init {
        viewModelScope.launch {
            getBrandsUseCase().doOnResult(
                onSuccess = {
                    val uiState = uiFactory(it)
                    entries = uiState.entries
                    _state.value = uiState
                },
                onError = {
                    _state.value = BrandUIState.Error()
                }
            )
        }
    }

    fun handleEvent(event: BrandEvent) {
        when (event) {
            is BrandEvent.OnBrandEntryClickEvent -> navigateToBrandEntry(entry = event.entry)
            is BrandEvent.OnBrandSearchEvent -> filterBrandsBySearchTerm(searchTerm = event.searchTerm)
        }
    }

    private fun navigateToBrandEntry(entry: BrandEntryUI.Entry) {
        val state = _state.value
        if (state is BrandUIState.Data && entry.slug.isNotEmpty()) {
            openBrandEntry(entry)
        }
    }

    private fun filterBrandsBySearchTerm(searchTerm: String) {
        viewModelScope.launch {
            val state = state.value
            if (state is BrandUIState.Data && !state.isLoading) {
                val filteredBrandUiState = uiFactory.filterBySearchTerm(
                    entries = entries,
                    searchTerm = searchTerm
                )
                _state.value = filteredBrandUiState
            }
        }
    }
}
