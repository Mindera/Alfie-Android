package com.mindera.alfie.feature.bag

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.bag.GetBagUseCase
import com.mindera.alfie.domain.usecase.bag.RemoveAllFromBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.feature.bag.BagUiState.Data.Loading
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.product.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mindera.alfie.designsystem.R as DesignR

@HiltViewModel
internal class BagViewModel @Inject constructor(
    private val getBagUseCase: GetBagUseCase,
    private val removeAllFromBagUseCase: RemoveAllFromBagUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val bagUiFactory: BagUiFactory,
    @ApplicationContext private val context: Context,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(),
    UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<BagUiState>(Loading)
    internal val state = _state.asStateFlow()

    private var bagJob: Job? = null

    init {
        getBagList()
    }

    /** Re-runs the bag load after an error. */
    internal fun onRetry() {
        _state.value = Loading
        getBagList()
    }

    private fun getBagList() {
        bagJob?.cancel()
        bagJob = viewModelScope.launch {
            getBagUseCase().collectLatest { result ->
                result.doOnResult(
                    onSuccess = { bagProducts ->
                        val content = bagUiFactory(
                            bagProducts = bagProducts,
                            products = getBagProductDetails(bagProducts),
                            onProductClick = { handle -> openProduct(handle) }
                        )
                        _state.value = if (content.items.isEmpty()) {
                            BagUiState.Data.Empty
                        } else {
                            BagUiState.Data.Loaded(content)
                        }
                    },
                    onError = {
                        _state.value = BagUiState.Error
                    }
                )
            }
        }
    }

    private suspend fun getBagProductDetails(bagProducts: List<BagProduct>): List<Product> {
        // Repeated entries are the same product, so fetch each product once rather than once per unit.
        val productIds = bagProducts.map { it.productId }.distinct()
        return coroutineScope {
            productIds.map { productId ->
                async { getProductUseCase(productId) }
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

    private fun openProduct(handle: String) {
        navigateTo(
            screen = Screen.ProductDetails(
                args = productDetailsNavArgs(handle = handle)
            )
        )
    }

    /** Clears the whole line: a bag row stands for every unit of that variant. */
    internal fun onRemoveClicked(bagProduct: BagProduct) {
        viewModelScope.launch {
            removeAllFromBagUseCase(bagProduct)
        }
    }

    /**
     * Adds the product to the wishlist and leaves the bag untouched — the design exposes Save and
     * Remove as separate actions, so Save is not a move.
     */
    internal fun onSaveClicked(bagProduct: BagProduct) {
        viewModelScope.launch {
            addToWishlistUseCase(bagProduct.productId).doOnResult(
                onSuccess = { },
                onError = {
                    showSnackbar(
                        SnackbarCustomVisuals(
                            type = SnackbarType.Error,
                            message = context.getString(DesignR.string.wishlist_error_add_product)
                        )
                    )
                }
            )
        }
    }
}
