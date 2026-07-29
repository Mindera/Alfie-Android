package com.mindera.alfie.feature.shop.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.categoryNavArgs
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListNavArgs
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
internal class NavigateToEntryDelegate @Inject constructor(
    private val uiEventEmitterDelegate: UIEventEmitterDelegate
) : NavigateToEntry {

    /**
     * Every entry is handled the same way: those with sub-categories drill down, everything else
     * opens the listing. [CategoryEntryUI.hasChildren] is the single source of truth shared with the
     * row's chevron, so the affordance and the destination cannot disagree.
     */
    override fun ViewModel.openCategoryEntry(entry: CategoryEntryUI) {
        viewModelScope.launch {
            runUIEvent {
                if (entry.hasChildren) {
                    // Children were persisted under this entry's row id when the menu was inserted,
                    // so the sub-category screen resolves them by parent id.
                    navigateTo(
                        Screen.Category(
                            args = categoryNavArgs(
                                id = entry.id,
                                title = entry.title
                            )
                        )
                    )
                } else {
                    navigateTo(
                        Screen.ProductList(
                            args = ProductListNavArgs(
                                type = ProductListType.Category.Slug(entry.path)
                            )
                        )
                    )
                }
            }
        }
    }

    private fun runUIEvent(block: UIEventEmitterDelegate.() -> Unit) {
        uiEventEmitterDelegate.run { block() }
    }
}
