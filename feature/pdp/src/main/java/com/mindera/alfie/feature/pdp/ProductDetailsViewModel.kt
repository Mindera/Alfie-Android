package com.mindera.alfie.feature.pdp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.ProductDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.webview.webViewNavArgs
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.bag.AddToBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
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
            is ProductDetailsEvent.OnAddToBagClick -> onAddToBag()
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

    private fun onFavoriteClick() {
        // TODO
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
