package au.com.alfie.ecomm.feature.plp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.productDetailsNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListType
import au.com.alfie.ecomm.domain.onSuccess
import au.com.alfie.ecomm.domain.usecase.productlist.GetPaginatedProductListUseCase
import au.com.alfie.ecomm.domain.usecase.productlist.GetProductListLayoutModeUseCase
import au.com.alfie.ecomm.domain.usecase.productlist.UpdateProductListLayoutModeUseCase
import au.com.alfie.ecomm.domain.usecase.wishlist.AddToWishlistUseCase
import au.com.alfie.ecomm.feature.plp.factory.ProductListEntryUIFactory
import au.com.alfie.ecomm.feature.plp.factory.ProductListUIFactory
import au.com.alfie.ecomm.feature.plp.model.ProductListEntryUI
import au.com.alfie.ecomm.feature.plp.model.ProductListEvent
import au.com.alfie.ecomm.feature.plp.model.ProductListUI
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import au.com.alfie.ecomm.repository.productlist.model.ProductListMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductListViewModel @Inject constructor(
    private val getPaginatedProductList: GetPaginatedProductListUseCase,
    private val getProductListLayoutMode: GetProductListLayoutModeUseCase,
    private val updateProductListLayoutMode: UpdateProductListLayoutModeUseCase,
    private val productListEntryUIFactory: ProductListEntryUIFactory,
    private val productListUIFactory: ProductListUIFactory,
    savedStateHandle: SavedStateHandle,
    private val uiEventEmitterDelegate: UIEventEmitterDelegate,
    private val addWishlistUseCase: AddToWishlistUseCase
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    companion object {
        private const val PAGE_SIZE = 15

        private val initialPagerLoadState = LoadStates(
            refresh = LoadState.Loading,
            append = LoadState.NotLoading(false),
            prepend = LoadState.NotLoading(false)
        )
    }

    private val args: ProductListNavArgs = savedStateHandle.navArgs()
    private val listType = args.type

    private val _productPager = MutableStateFlow<PagingData<ProductListEntryUI>>(PagingData.empty(initialPagerLoadState))
    val productPager: Flow<PagingData<ProductListEntryUI>> = _productPager

    private val _state = MutableStateFlow(ProductListUI.EMPTY)
    val state: StateFlow<ProductListUI> = _state.asStateFlow()

    init {
        collectPaginatedProductList()
        checkLayoutModePreference()
    }

    fun handleEvent(event: ProductListEvent) {
        when (event) {
            is ProductListEvent.OpenProduct -> navigateToProduct(event.productId)
            is ProductListEvent.OpenFilters -> { /* TODO */ }
            is ProductListEvent.ChangeLayoutMode -> changeLayoutMode(event.layoutMode)
        }
    }

    fun checkLayoutModePreference() {
        viewModelScope.launch {
            val layoutMode = getProductListLayoutMode()
            _state.update {
                productListUIFactory(
                    productListUI = it,
                    layoutMode = layoutMode
                )
            }
        }
    }

    private fun collectPaginatedProductList() {
        viewModelScope.launch {
            // TODO Improve listing parameters when there's more support from the API
            val pagerFlow = getPaginatedProductList(
                categoryId = (listType as? ProductListType.Category.Id)?.id,
                query = (listType as? ProductListType.Search)?.query,
                pageSize = PAGE_SIZE,
                metadataProvider = ::handleProductMetadata
            )
                .cachedIn(viewModelScope)
                .combine(state) { pagingData, uiState ->
                    pagingData.map { entry ->
                        productListEntryUIFactory(
                            entry = entry,
                            layoutMode = uiState.layoutMode,
                            onFavoriteClick = { onFavoriteClick(entry) }
                        )
                    }
                }
            _productPager.emitAll(pagerFlow)
        }
    }

    private fun handleProductMetadata(metadata: ProductListMetadata) {
        viewModelScope.launch {
            _state.update {
                productListUIFactory(
                    productListUI = it,
                    metadata = metadata
                )
            }
        }
    }

    private fun navigateToProduct(id: String) {
        navigateTo(
            screen = Screen.ProductDetails(
                args = productDetailsNavArgs(id = id)
            )
        )
    }

    private fun changeLayoutMode(layoutMode: ProductListLayoutMode) {
        viewModelScope.launch {
            updateProductListLayoutMode(layoutMode).onSuccess {
                _state.update {
                    productListUIFactory(
                        productListUI = it,
                        layoutMode = layoutMode
                    )
                }
            }
        }
    }

    private fun onFavoriteClick(product: ProductListEntry) {
        viewModelScope.launch {
            addWishlistUseCase(product.id)
        }
    }
}
