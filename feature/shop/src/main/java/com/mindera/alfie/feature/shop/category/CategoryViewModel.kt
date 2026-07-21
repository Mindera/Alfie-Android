package com.mindera.alfie.feature.shop.category

import androidx.lifecycle.ViewModel
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.shop.category.model.CategoryEvent
import com.mindera.alfie.feature.shop.category.model.CategoryUIState
import com.mindera.alfie.feature.shop.delegate.NavigateToEntry
import com.mindera.alfie.feature.shop.delegate.NavigateToEntryDelegate
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class CategoryViewModel @Inject constructor(
    navigateToEntryDelegate: NavigateToEntryDelegate,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(),
    NavigateToEntry by navigateToEntryDelegate,
    UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<CategoryUIState>(STATIC_STATE)
    val state: StateFlow<CategoryUIState> = _state.asStateFlow()

    fun retry() = Unit

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
        private val STATIC_ENTRIES = listOf(
            CategoryEntryUI(id = 0, title = StringResource.fromText("Women"), path = "women"),
            CategoryEntryUI(id = 1, title = StringResource.fromText("Men"), path = "men"),
            CategoryEntryUI(id = 2, title = StringResource.fromText("Featured"), path = "frontpage"),
            CategoryEntryUI(id = 3, title = StringResource.fromText("Tops"), path = "womens-tops"),
            CategoryEntryUI(id = 4, title = StringResource.fromText("Beauty"), path = "spring-summer"),
            CategoryEntryUI(id = 5, title = StringResource.fromText("Bags"), path = "womens-bags"),
            CategoryEntryUI(id = 6, title = StringResource.fromText("Dresses"), path = "dresses"),
            CategoryEntryUI(id = 7, title = StringResource.fromText("Jackets"), path = "womens-jackets"),
            CategoryEntryUI(id = 8, title = StringResource.fromText("Jeans"), path = "womens-jeans")
        ).toImmutableList()

        private val STATIC_STATE = CategoryUIState.Data(
            title = StringResource.EMPTY,
            entries = STATIC_ENTRIES,
            isLoading = false
        )
    }
}
