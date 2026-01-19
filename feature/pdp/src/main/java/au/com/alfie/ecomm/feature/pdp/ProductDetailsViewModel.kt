package au.com.alfie.ecomm.feature.pdp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.ProductDetailsNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.webview.webViewNavArgs
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.domain.usecase.bag.AddToBagUseCase
import au.com.alfie.ecomm.domain.usecase.product.GetProductUseCase
import au.com.alfie.ecomm.domain.usecase.wishlist.AddToWishlistUseCase
import au.com.alfie.ecomm.domain.usecase.wishlist.GetWishlistIdsUseCase
import au.com.alfie.ecomm.domain.usecase.wishlist.RemoveFromWishlistUseCase
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsEvent
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsSectionItem
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUIState
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUIState.Data.Loaded
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUIState.Data.Loading
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUIState.Error
import au.com.alfie.ecomm.feature.pdp.model.ShareEvent
import au.com.alfie.ecomm.feature.pdp.model.SizeUI
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ProductDetailsViewModel @Inject constructor(
    private val addToBagUseCase: AddToBagUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val getWishlistIds: GetWishlistIdsUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeWishlistUseCase: RemoveFromWishlistUseCase,
    private val uiFactory: ProductDetailsUIFactory,
    savedStateHandle: SavedStateHandle,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<ProductDetailsUIState>(Loading)
    val state = _state.asStateFlow()

    private val args: ProductDetailsNavArgs = savedStateHandle.navArgs()
    private val productId = args.id

    init {
        loadDetails()
        collectWishlistIds()
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
                    _state.value = Loaded(details = shopUI)
                },
                onError = {
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
                _state.update { state ->
                    when (state) {
                        is Loaded -> {
                            Timber.tag("WishlistTesting")
                                .d("isWishlisted: ${state.details.isWishlisted}")
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
            Timber.tag("WishlistTesting").d("FAv clicked: $productId")
            (_state.value as? ProductDetailsUIState.Data)?.details?.let {
                if (it.isWishlisted.not()) {
                    addToWishlistUseCase(productId)
                } else {
                    removeWishlistUseCase(productId)
                }
            }
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
}
