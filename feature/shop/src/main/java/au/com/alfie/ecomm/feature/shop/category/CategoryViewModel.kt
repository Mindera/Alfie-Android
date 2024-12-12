package au.com.alfie.ecomm.feature.shop.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.domain.usecase.navigation.GetRootNavEntriesUseCase
import au.com.alfie.ecomm.feature.shop.category.factory.CategoryUIStateFactory
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEvent
import au.com.alfie.ecomm.feature.shop.category.model.CategoryUIState
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntry
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntryDelegate
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoryViewModel @Inject constructor(
    private val getRootNavEntriesUseCase: GetRootNavEntriesUseCase,
    private val uiFactory: CategoryUIStateFactory,
    navigateToEntryDelegate: NavigateToEntryDelegate,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(),
    NavigateToEntry by navigateToEntryDelegate,
    UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<CategoryUIState>(CategoryUIStateFactory.PLACEHOLDER)
    val state: StateFlow<CategoryUIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getRootNavEntriesUseCase().doOnResult(
                onSuccess = {
                    _state.value = uiFactory(
                        title = StringResource.EMPTY,
                        navEntries = it
                    )
                },
                onError = { _state.value = CategoryUIState.Error() }
            )
        }
    }

    fun handleEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnEntryClickEvent -> navigateToCategoryEntry(event.entry)
        }
    }

    private fun navigateToCategoryEntry(entry: CategoryEntryUI) {
        val state = _state.value
        if (state is CategoryUIState.Data && entry.path.isNotEmpty()) {
            openCategoryEntry(entry)
        }
    }
}
