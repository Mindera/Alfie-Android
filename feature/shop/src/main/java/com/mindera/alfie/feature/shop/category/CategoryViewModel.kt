package com.mindera.alfie.feature.shop.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.navigation.GetRootNavEntriesUseCase
import com.mindera.alfie.feature.shop.category.factory.CategoryUIStateFactory
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.shop.category.model.CategoryEvent
import com.mindera.alfie.feature.shop.category.model.CategoryUIState
import com.mindera.alfie.feature.shop.delegate.NavigateToEntry
import com.mindera.alfie.feature.shop.delegate.NavigateToEntryDelegate
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
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
