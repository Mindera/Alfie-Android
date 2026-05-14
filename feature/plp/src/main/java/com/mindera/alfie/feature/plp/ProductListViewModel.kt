package com.mindera.alfie.feature.plp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListNavArgs
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.domain.onSuccess
import com.mindera.alfie.domain.usecase.productlist.GetPaginatedProductListUseCase
import com.mindera.alfie.domain.usecase.productlist.GetProductListLayoutModeUseCase
import com.mindera.alfie.domain.usecase.productlist.UpdateProductListLayoutModeUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.feature.plp.factory.ProductListEntryUIFactory
import com.mindera.alfie.feature.plp.factory.ProductListUIFactory
import com.mindera.alfie.feature.plp.model.ProductListEntryUI
import com.mindera.alfie.feature.plp.model.ProductListEvent
import com.mindera.alfie.feature.plp.model.ProductListUI
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductListMetadata
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
    private val addWishlistUseCase: AddToWishlistUseCase,
    private val productListEntryUIFactory: ProductListEntryUIFactory,
    private val productListUIFactory: ProductListUIFactory,
    savedStateHandle: SavedStateHandle,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(),
    UIEventEmitter by uiEventEmitterDelegate {

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
