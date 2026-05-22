package com.mindera.alfie.feature.shop.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.analytics.AnalyticsManager
import com.mindera.alfie.core.analytics.params.EmptyParams
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.navigation.GetRootNavEntriesUseCase
import com.mindera.alfie.feature.mappers.toEventErrorValue
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
    private val analyticsManager: AnalyticsManager,
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
                onError = {
                    analyticsManager.trackError(
                        screenName = SCREEN_NAME,
                        eventName = EVENT_LOAD_ERROR,
                        eventErrorValue = it.type.toEventErrorValue(),
                        params = EmptyParams()
                    )
                    _state.value = CategoryUIState.Error()
                }
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

    companion object {
        private const val SCREEN_NAME = "shop_category"
        private const val EVENT_LOAD_ERROR = "load_error"
    }
}
