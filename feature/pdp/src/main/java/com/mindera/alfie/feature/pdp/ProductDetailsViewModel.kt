package com.mindera.alfie.feature.pdp

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.analytics.AnalyticsManager
import com.mindera.alfie.core.analytics.params.EmptyParams
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.ProductDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.webview.webViewNavArgs
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.bag.AddToBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistIdsUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.mappers.toEventErrorValue
import com.mindera.alfie.feature.pdp.model.ProductDetailsEvent
import com.mindera.alfie.feature.pdp.model.ProductDetailsSectionItem
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState.Data.Loaded
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState.Data.Loading
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState.Error
import com.mindera.alfie.feature.pdp.model.ShareEvent
import com.mindera.alfie.feature.pdp.model.SizeUI
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val getWishlistIds: GetWishlistIdsUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeWishlistUseCase: RemoveFromWishlistUseCase,
    private val uiFactory: ProductDetailsUIFactory,
    private val analyticsManager: AnalyticsManager,
    savedStateHandle: SavedStateHandle,
    uiEventEmitterDelegate: UIEventEmitterDelegate,
    @ApplicationContext private val context: Context
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<ProductDetailsUIState>(Loading)
    val state = _state.asStateFlow()

    private val _wishlistIds = MutableStateFlow<List<String>>(emptyList())

    private val args: ProductDetailsNavArgs = savedStateHandle.navArgs()
    private val productId = args.id

    init {
        collectWishlistIds()
        loadDetails()
    }

    fun handleEvent(event: ProductDetailsEvent) {
        when (event) {
            is ProductDetailsEvent.OnAddToBagClick -> onAddToBag()
            ProductDetailsEvent.OnShareClick -> onShareClick()
            is ProductDetailsEvent.OnColorClick -> onColorSelected(event.index)
            is ProductDetailsEvent.OnSectionClick -> onSectionClick(event.item)
            is ProductDetailsEvent.OnFavoriteClick -> onFavoriteClick(event.productId)
            is ProductDetailsEvent.OnSizeSelect -> onSizeSelect(event.sizeUI)
        }
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _state.value = Loading

            getProductUseCase(productId).doOnResult(
                onSuccess = {
                    val shopUI = uiFactory(it)
                    _state.value = Loaded(
                        details = shopUI.copy(
                            isWishlisted = _wishlistIds.value.contains(shopUI.id)
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
                    _state.value = Error
                }
            )
        }
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
            selectedVariantSku?.let { addToBagUseCase(value.details.id, it) }
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
                                    isWishlisted = wishlistIds.contains(state.details.id)
                                )
                            )
                        }

                        else -> state
                    }
                }
            }
        }
    }

    private fun onFavoriteClick(productId: String) {
        viewModelScope.launch {
            val loaded = (_state.value as? Loaded) ?: return@launch
            val wasWishlisted = loaded.details.isWishlisted

            _state.update { state ->
                (state as? Loaded)?.copy(details = state.details.copy(isWishlisted = !wasWishlisted)) ?: state
            }

            val result = if (wasWishlisted) removeWishlistUseCase(productId) else addToWishlistUseCase(productId)

            result.doOnResult(
                onSuccess = {},
                onError = {
                    _state.update { state ->
                        (state as? Loaded)?.copy(details = state.details.copy(isWishlisted = wasWishlisted)) ?: state
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
