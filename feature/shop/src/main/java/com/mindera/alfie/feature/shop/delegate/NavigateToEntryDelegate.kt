package com.mindera.alfie.feature.shop.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.deeplink.DeeplinkHandler
import com.mindera.alfie.core.environment.EnvironmentManager
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
    private val deeplinkHandler: DeeplinkHandler,
    private val uiEventEmitterDelegate: UIEventEmitterDelegate,
    private val environmentManager: EnvironmentManager
) : NavigateToEntry {

    companion object {
        // Bare handle: the mapper strips the leading slash, since `path` is passed straight
// through as the PLP `collectionHandle` without further trimming.
        private const val BRANDS_FIXED_PATH = "brands"
    }

    /**
     * Resolution order matches iOS: the fixed brands link wins, then a drill-down into
     * sub-categories, and only a leaf opens the listing. [CategoryEntryUI.hasChildren] is the single
     * source of truth shared with the row's chevron, so the affordance and the destination agree.
     */
    override fun ViewModel.openCategoryEntry(entry: CategoryEntryUI) {
        viewModelScope.launch {
            if (entry.path == BRANDS_FIXED_PATH) {
                val environment = environmentManager.current()
                // `path` is a bare handle, so the separator has to be supplied here.
                deeplinkHandler.handle("${environment.webUrl}/${entry.path}")
                return@launch
            }

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
