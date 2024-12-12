package au.com.alfie.ecomm.feature.shop.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.navigation.arguments.CategoryNavArgs
import au.com.alfie.ecomm.domain.usecase.navigation.GetNavEntriesByParentIdUseCase
import au.com.alfie.ecomm.feature.shop.category.factory.CategoryUIStateFactory
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEvent
import au.com.alfie.ecomm.feature.shop.category.model.CategoryUIState
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntry
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntryDelegate
import au.com.alfie.ecomm.feature.shop.navArgs
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoryEntryViewModel @Inject constructor(
    private val getNavEntriesByParentIdUseCase: GetNavEntriesByParentIdUseCase,
    private val uiFactory: CategoryUIStateFactory,
    savedStateHandle: SavedStateHandle,
    navigateToEntryDelegate: NavigateToEntryDelegate,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(),
    NavigateToEntry by navigateToEntryDelegate,
    UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow(CategoryUIStateFactory.PLACEHOLDER)
    val state: StateFlow<CategoryUIState.Data> = _state.asStateFlow()

    init {
        val args: CategoryNavArgs = savedStateHandle.navArgs()
        viewModelScope.launch {
            _state.value = uiFactory(
                title = args.title,
                navEntries = getNavEntriesByParentIdUseCase(parentId = args.id)
            )
        }
    }

    fun handleEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnEntryClickEvent -> openCategoryEntry(event.entry)
        }
    }
}
