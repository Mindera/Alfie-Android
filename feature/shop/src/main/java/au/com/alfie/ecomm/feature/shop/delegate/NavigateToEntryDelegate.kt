package au.com.alfie.ecomm.feature.shop.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.categoryNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListType
import au.com.alfie.ecomm.domain.usecase.navigation.GetNavEntriesByParentIdUseCase
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
internal class NavigateToEntryDelegate @Inject constructor(
    private val getNavEntriesByParentIdUseCase: GetNavEntriesByParentIdUseCase,
    private val deeplinkHandler: DeeplinkHandler,
    private val uiEventEmitterDelegate: UIEventEmitterDelegate,
    private val environmentManager: EnvironmentManager
) : NavigateToEntry {

    companion object {
        private const val BRANDS_FIXED_PATH = "/brands"
    }

    override fun ViewModel.openCategoryEntry(entry: CategoryEntryUI) {
        viewModelScope.launch {
            val environment = environmentManager.current()
            val items = getNavEntriesByParentIdUseCase(parentId = entry.id)

            if (items.isEmpty() || entry.path == BRANDS_FIXED_PATH) {
                deeplinkHandler.handle("${environment.webUrl}${entry.path}")
            } else {
                runUIEvent {
                    navigateTo(
                        Screen.Category(
                            args = categoryNavArgs(
                                id = entry.id,
                                title = entry.title
                            )
                        )
                    )
                }
            }
        }
    }

    override fun ViewModel.openBrandEntry(entry: BrandEntryUI.Entry) {
        viewModelScope.launch {
            runUIEvent {
                navigateTo(
                    Screen.ProductList(
                        args = ProductListNavArgs(
                            type = ProductListType.Brand.Slug(entry.slug)
                        )
                    )
                )
            }
        }
    }

    private fun runUIEvent(block: UIEventEmitterDelegate.() -> Unit) {
        uiEventEmitterDelegate.run { block() }
    }
}
