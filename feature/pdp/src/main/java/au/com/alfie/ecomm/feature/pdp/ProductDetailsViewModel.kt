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
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsEvent
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsSectionItem
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUI
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductDetailsViewModel @Inject constructor(
    private val addToBagUseCase: AddToBagUseCase,
    private val getProductUseCase: GetProductUseCase,
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
    }

    fun handleEvent(event: ProductDetailsEvent) {
        when (event) {
            is ProductDetailsEvent.OnAddToBagClick -> onAddToBag(event.item)
            ProductDetailsEvent.OnShareClick -> onShareClick()
            is ProductDetailsEvent.OnColorClick -> onColorSelected(event.index)
            is ProductDetailsEvent.OnSectionClick -> onSectionClick(event.item)
            is ProductDetailsEvent.OnFavoriteClick -> onFavoriteClick()
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

    private fun onAddToBag(item: ProductDetailsUI) {
        viewModelScope.launch {
            addToBagUseCase(item.id)
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

    private fun onFavoriteClick() {
        // TODO
    }

    private fun onSizeSelect(sizeUI: SizeUI) {
        viewModelScope.launch {
            val value = _state.value
            if (value is Loaded) {
                val updatedState = uiFactory.setSelectedSize(details = value.details, sizeUI = sizeUI)
                _state.value = Loaded(updatedState)
            }
        }
    }
}
