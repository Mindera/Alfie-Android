package com.mindera.alfie.feature.plp

import android.content.Context
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
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.onSuccess
import com.mindera.alfie.domain.usecase.productlist.GetPaginatedProductListUseCase
import com.mindera.alfie.domain.usecase.productlist.GetProductListLayoutModeUseCase
import com.mindera.alfie.domain.usecase.productlist.UpdateProductListLayoutModeUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistIdsUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.plp.factory.ProductListEntryUIFactory
import com.mindera.alfie.feature.plp.factory.ProductListUIFactory
import com.mindera.alfie.feature.plp.model.ProductListEntryUI
import com.mindera.alfie.feature.plp.model.ProductListEvent
import com.mindera.alfie.feature.plp.model.ProductListUI
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductListMetadata
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mindera.alfie.designsystem.R as DesignR

@HiltViewModel
internal class ProductListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPaginatedProductList: GetPaginatedProductListUseCase,
    private val getProductListLayoutMode: GetProductListLayoutModeUseCase,
    private val updateProductListLayoutMode: UpdateProductListLayoutModeUseCase,
    private val getWishlistIds: GetWishlistIdsUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeWishlistUseCase: RemoveFromWishlistUseCase,
    private val productListEntryUIFactory: ProductListEntryUIFactory,
    private val productListUIFactory: ProductListUIFactory,
    uiEventEmitterDelegate: UIEventEmitterDelegate,
    @ApplicationContext private val context: Context
) : ViewModel(),
    UIEventEmitter by uiEventEmitterDelegate {

    companion object {
        private const val PAGE_SIZE = 15

        // BFF doesn't yet expose a navigation/category lookup, so the collection handle is
        // hardcoded to "women" as a placeholder. Nav args are intentionally ignored when
        // selecting products until the BFF supports resolving a handle from category slug/id,
        // brand, or search query. The displayed title still uses nav args (see collectionTitle),
        // which means the title may not match the products shown — this is a known trade-off
        // until the BFF integration is complete.
        private const val COLLECTION_HANDLE = "women"

        private val initialPagerLoadState = LoadStates(
            refresh = LoadState.Loading,
            append = LoadState.NotLoading(false),
            prepend = LoadState.NotLoading(false)
        )
    }

    private val navArgs: ProductListNavArgs = savedStateHandle.navArgs()

    /**
     * Display title derived from nav args.
     *
     * Note: this title may not match the products shown, because the underlying collection
     * handle is hardcoded to [COLLECTION_HANDLE] until the BFF exposes a navigation query
     * to resolve a handle from category slug/id, brand, or search query.
     */
    val collectionTitle: String = when (val type = navArgs.type) {
        is ProductListType.Category.Slug -> type.slug
        is ProductListType.Category.Id -> type.id
        is ProductListType.Brand.Slug -> type.slug
        is ProductListType.Brand.Id -> type.id
        is ProductListType.Search -> type.query
    }

    private var pagerJob: Job? = null

    private val _productPager =
        MutableStateFlow<PagingData<ProductListEntryUI>>(PagingData.empty(initialPagerLoadState))
    val productPager: Flow<PagingData<ProductListEntryUI>> = _productPager

    private val _state = MutableStateFlow(ProductListUI.EMPTY)
    val state: StateFlow<ProductListUI> = _state.asStateFlow()

    init {
        collectPaginatedProductList()
        collectWishlistIds()
        checkLayoutModePreference()
    }

    fun handleEvent(event: ProductListEvent) {
        when (event) {
            is ProductListEvent.OpenProduct -> navigateToProduct(event.productId)
            is ProductListEvent.OpenFilters -> _state.update { it.copy(showRefine = true) }
            is ProductListEvent.DismissRefine -> _state.update { it.copy(showRefine = false) }
            is ProductListEvent.ChangeLayoutMode -> changeLayoutMode(event.layoutMode)
            is ProductListEvent.ApplyRefine -> applyRefine(event.sort, event.filters)
            is ProductListEvent.ToggleFilterChip -> toggleFilterChip(event.chipId)
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
        pagerJob?.cancel()
        pagerJob = viewModelScope.launch {
            val currentState = _state.value
            val pagerFlow = getPaginatedProductList(
                collectionHandle = COLLECTION_HANDLE,
                filters = currentState.selectedFilters,
                sort = currentState.selectedSort,
                pageSize = PAGE_SIZE,
                metadataProvider = ::handleProductMetadata
            )
                .cachedIn(viewModelScope)
                .combine(state) { pagingData, _ ->
                    pagingData.map { entry ->
                        productListEntryUIFactory(
                            entry = entry,
                            onFavoriteClick = { onFavoriteClick(entry.id) },
                            onProductClick = { navigateToProduct(entry.id) }
                        )
                    }
                }
            _productPager.emitAll(pagerFlow)
        }
    }

    private fun applyRefine(sort: ProductSortOption, filters: ProductListFilter?) {
        _state.update { it.copy(selectedSort = sort, selectedFilters = filters) }
        restartPager()
    }

    private fun toggleFilterChip(chipId: String) {
        _state.update { current ->
            current.copy(
                availableFilters = current.availableFilters.map { chip ->
                    if (chip.id == chipId) chip.copy(isSelected = !chip.isSelected) else chip
                }
            )
        }
        // TODO: When BFF exposes filter facets, map selected chips to the appropriate
        //  ProductListFilter fields (e.g. brandNames, productTypes) and merge with the
        //  price-range filter before restarting the pager.
        restartPager()
    }

    private fun restartPager() {
        _productPager.value = PagingData.empty(initialPagerLoadState)
        collectPaginatedProductList()
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

    private fun collectWishlistIds() {
        viewModelScope.launch {
            getWishlistIds.invoke().collect {
                _state.update { oldState -> oldState.copy(wishlistIds = it.toSet()) }
            }
        }
    }

    private fun onFavoriteClick(productId: String) {
        viewModelScope.launch {
            val wasWishlisted = _state.value.wishlistIds.contains(productId)

            _state.update { oldState ->
                val updatedIds = if (wasWishlisted) oldState.wishlistIds - productId else oldState.wishlistIds + productId
                oldState.copy(wishlistIds = updatedIds)
            }

            val result = if (wasWishlisted) removeWishlistUseCase(productId) else addToWishlistUseCase(productId)

            result.doOnResult(
                onSuccess = {},
                onError = {
                    _state.update { oldState ->
                        val revertedIds = if (wasWishlisted) oldState.wishlistIds + productId else oldState.wishlistIds - productId
                        oldState.copy(wishlistIds = revertedIds)
                    }
                    showSnackbar(
                        SnackbarCustomVisuals(
                            type = SnackbarType.Error,
                            message = context.getString(
                                if (wasWishlisted) DesignR.string.wishlist_error_remove_product else DesignR.string.wishlist_error_add_product
                            )
                        )
                    )
                }
            )
        }
    }
}
