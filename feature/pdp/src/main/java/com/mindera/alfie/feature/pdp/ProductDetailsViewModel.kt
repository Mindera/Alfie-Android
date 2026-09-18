package com.mindera.alfie.feature.pdp

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.analytics.AnalyticsManager
import com.mindera.alfie.core.analytics.params.EmptyParams
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.ProductDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.webview.webViewNavArgs
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.bag.AddToBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
import com.mindera.alfie.domain.usecase.product.GetRelatedProductsUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistIdsUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.mappers.toApiErrorType
import com.mindera.alfie.feature.mappers.toEventErrorValue
import com.mindera.alfie.feature.pdp.factory.RelatedProductsUIFactory
import com.mindera.alfie.feature.pdp.model.ProductDetailsEvent
import com.mindera.alfie.feature.pdp.model.ProductDetailsSectionItem
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState.Data.Loaded
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState.Data.Loading
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState.Error
import com.mindera.alfie.feature.pdp.model.RelatedProductsUIState
import com.mindera.alfie.feature.pdp.model.ShareEvent
import com.mindera.alfie.feature.pdp.model.SizeUI
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mindera.alfie.designsystem.R as DesignR

@HiltViewModel
internal class ProductDetailsViewModel @Inject constructor(
    private val addToBagUseCase: AddToBagUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val getRelatedProductsUseCase: GetRelatedProductsUseCase,
    private val getWishlistIds: GetWishlistIdsUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeWishlistUseCase: RemoveFromWishlistUseCase,
    private val uiFactory: ProductDetailsUIFactory,
    private val relatedProductsUIFactory: RelatedProductsUIFactory,
    private val analyticsManager: AnalyticsManager,
    savedStateHandle: SavedStateHandle,
    uiEventEmitterDelegate: UIEventEmitterDelegate,
    @ApplicationContext private val context: Context
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<ProductDetailsUIState>(Loading)
    val state = _state.asStateFlow()

    private val _relatedProducts = MutableStateFlow<RelatedProductsUIState>(RelatedProductsUIState.Loading)
    val relatedProducts = _relatedProducts.asStateFlow()

    private val _wishlistIds = MutableStateFlow<List<String>>(emptyList())

    private val args: ProductDetailsNavArgs = savedStateHandle.navArgs()
    private val handle = args.handle

    init {
        collectWishlistIds()
        loadDetails()
        loadRelatedProducts()
    }

    fun handleEvent(event: ProductDetailsEvent) {
        when (event) {
            is ProductDetailsEvent.OnAddToBagClick -> onAddToBag()
            ProductDetailsEvent.OnShareClick -> onShareClick()
            is ProductDetailsEvent.OnColorClick -> onColorSelected(event.index)
            is ProductDetailsEvent.OnSectionClick -> onSectionClick(event.item)
            is ProductDetailsEvent.OnFavoriteClick -> onFavoriteClick(event.slug)
            is ProductDetailsEvent.OnSizeSelect -> onSizeSelect(event.sizeUI)
        }
    }

    fun retry() {
        loadDetails()
        loadRelatedProducts()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _state.value = Loading

            getProductUseCase(handle = handle).doOnResult(
                onSuccess = {
                    val shopUI = uiFactory(it)
                    _state.value = Loaded(
                        details = shopUI.copy(
                            isWishlisted = _wishlistIds.value.contains(shopUI.slug)
                        )
                    )
                },
                onError = {
                    analyticsManager.trackError(
                        screenName = SCREEN_NAME,
                        eventName = EVENT_LOAD_ERROR,
                        eventErrorValue = it.type.toEventErrorValue(),
                        params = EmptyParams()
                    )
                    _state.value = Error(it.type.toApiErrorType())
                }
            )
        }
    }

    /**
     * Recommendations are fetched alongside the product rather than after it: they are an
     * independent rail, so a failure here only hides the section and never touches [_state].
     */
    private fun loadRelatedProducts() {
        viewModelScope.launch {
            _relatedProducts.value = RelatedProductsUIState.Loading

            getRelatedProductsUseCase(handle = handle).doOnResult(
                onSuccess = { entries ->
                    val items = relatedProductsUIFactory(
                        entries = entries,
                        wishlistedSlugs = _wishlistIds.value,
                        onProductClick = ::navigateToProduct,
                        onFavoriteClick = ::onFavoriteClick
                    )
                    _relatedProducts.value = if (items.isEmpty()) {
                        RelatedProductsUIState.Hidden
                    } else {
                        RelatedProductsUIState.Loaded(items)
                    }
                },
                onError = { _relatedProducts.value = RelatedProductsUIState.Hidden }
            )
        }
    }

    private fun navigateToProduct(productHandle: String) {
        navigateTo(screen = Screen.ProductDetails(args = productDetailsNavArgs(handle = productHandle)))
    }

    private fun onColorSelected(index: Int) {
        viewModelScope.launch {
            val value = _state.value
            if (value is Loaded) {
                val updatedState = uiFactory.setSelectedColour(value.details, index)
                _state.value = Loaded(updatedState)
            }
            // TODO: update PDP content due to selection if needed
        }
    }

    private fun onAddToBag() {
        viewModelScope.launch {
            val value = (_state.value as? Loaded) ?: return@launch
            val selectedVariantSku = uiFactory.getSelectedVariantSku(value.details)
            selectedVariantSku?.let { addToBagUseCase(value.details.slug, it) }
        }
    }

    private fun onSectionClick(item: ProductDetailsSectionItem) {
        val args = webViewNavArgs(
            url = item.url,
            title = item.title
        )
        navigateTo(screen = Screen.WebView(args = args))
    }

    private fun onShareClick() {
        val value = _state.value
        if (value is Loaded) {
            val event = ShareEvent(
                title = value.details.shareInfo.name,
                content = value.details.shareInfo.content
            )

            emitUIEvent(event)
        }
    }

    private fun collectWishlistIds() {
        viewModelScope.launch {
            getWishlistIds().collect { wishlistIds ->
                _wishlistIds.value = wishlistIds
                _state.update { state ->
                    when (state) {
                        is Loaded -> {
                            state.copy(
                                details = state.details.copy(
                                    isWishlisted = wishlistIds.contains(state.details.slug)
                                )
                            )
                        }

                        else -> state
                    }
                }
                _relatedProducts.update { related ->
                    (related as? RelatedProductsUIState.Loaded)?.let { loaded ->
                        loaded.copy(
                            items = loaded.items
                                .map { it.copy(isWishlisted = wishlistIds.contains(it.slug)) }
                                .toImmutableList()
                        )
                    } ?: related
                }
            }
        }
    }

    private fun onFavoriteClick(slug: String) {
        viewModelScope.launch {
            val wasWishlisted = isWishlisted(slug) ?: return@launch

            setWishlisted(slug, !wasWishlisted)

            val result = if (wasWishlisted) removeWishlistUseCase(slug) else addToWishlistUseCase(slug)

            result.doOnResult(
                onSuccess = {},
                onError = {
                    setWishlisted(slug, wasWishlisted)
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

    /**
     * The same slug can be shown by the product itself and by a recommendation card, so the
     * current flag is read from whichever surface owns it. Null means the slug is on neither,
     * i.e. there is nothing to toggle.
     */
    private fun isWishlisted(slug: String): Boolean? {
        (_state.value as? Loaded)
            ?.takeIf { it.details.slug == slug }
            ?.let { return it.details.isWishlisted }

        return (_relatedProducts.value as? RelatedProductsUIState.Loaded)
            ?.items
            ?.firstOrNull { it.slug == slug }
            ?.isWishlisted
    }

    /** Optimistic toggle applied to every surface showing [slug]; reverted by the caller on error. */
    private fun setWishlisted(slug: String, isWishlisted: Boolean) {
        _state.update { state ->
            (state as? Loaded)
                ?.takeIf { it.details.slug == slug }
                ?.copy(details = state.details.copy(isWishlisted = isWishlisted))
                ?: state
        }
        _relatedProducts.update { related ->
            (related as? RelatedProductsUIState.Loaded)?.let { loaded ->
                loaded.copy(
                    items = loaded.items
                        .map { if (it.slug == slug) it.copy(isWishlisted = isWishlisted) else it }
                        .toImmutableList()
                )
            } ?: related
        }
    }

    private fun onSizeSelect(sizeUI: SizeUI) {
        viewModelScope.launch {
            val value = _state.value
            if (value is Loaded) {
                val updatedState =
                    uiFactory.setSelectedSize(details = value.details, sizeUI = sizeUI)
                _state.value = Loaded(updatedState)
            }
        }
    }

    companion object {
        private const val SCREEN_NAME = "product_details"
        private const val EVENT_LOAD_ERROR = "load_error"
    }
}
