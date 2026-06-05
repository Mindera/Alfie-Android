package com.mindera.alfie.feature.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.bag.GetBagUseCase
import com.mindera.alfie.domain.usecase.bag.RemoveFromBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
import com.mindera.alfie.feature.bag.BagUiState.Data.Loading
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.product.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BagViewModel @Inject constructor(
    private val getBagUseCase: GetBagUseCase,
    private val removeFromBagUseCase: RemoveFromBagUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val bagUiFactory: BagUiFactory,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(),
    UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<BagUiState>(Loading)
    internal val state = _state.asStateFlow()

    init {
        getBagList()
    }

    private fun getBagList() {
        viewModelScope.launch {
            getBagUseCase().collectLatest { result ->
                result.doOnResult(
                    onSuccess = {
                        val bag = bagUiFactory(
                            bagProducts = it,
                            products = getBagProductDetails(it),
                            onRemoveClick = { onRemoveClicked(it) },
                            onProductClick = { openProduct(it) }
                        )
                        _state.value = BagUiState.Data.Loaded(bag)
                    },
                    onError = {
                        _state.value = BagUiState.Error
                    }
                )
            }
        }
    }

    private suspend fun getBagProductDetails(bagProducts: List<BagProduct>): List<Product> {
        return coroutineScope {
            bagProducts.map { bagProduct ->
                async { getProductUseCase(bagProduct.productId) }
            }.awaitAll()
                .mapNotNull { result ->
                    var product: Product? = null
                    result.doOnResult(
                        onSuccess = { product = it },
                        onError = { }
                    )
                    product
                }
        }
    }

    private fun openProduct(productId: String) {
        // TODO(ALFMOB-388): bag storage exposes ID; PDP requires the BFF handle/slug.
        // Tracked separately — bag domain migration is out of scope for ALFMOB-338.
        navigateTo(
            screen = Screen.ProductDetails(
                args = productDetailsNavArgs(handle = productId)
            )
        )
    }

    private fun onRemoveClicked(bagProduct: BagProduct) {
        viewModelScope.launch {
            removeFromBagUseCase(bagProduct)
        }
    }
}