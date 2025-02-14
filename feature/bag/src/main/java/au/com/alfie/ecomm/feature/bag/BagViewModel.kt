package au.com.alfie.ecomm.feature.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.domain.usecase.bag.GetBagUseCase
import au.com.alfie.ecomm.domain.usecase.product.GetProductUseCase
import au.com.alfie.ecomm.feature.bag.BagUiState.Data.Loading
import au.com.alfie.ecomm.repository.bag.BagProduct
import au.com.alfie.ecomm.repository.product.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BagViewModel @Inject constructor(
    private val getBagUseCase: GetBagUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val bagUiFactory: BagUiFactory
) : ViewModel() {

    private val _state = MutableStateFlow<BagUiState>(Loading)
    internal val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getBagList()
        }
    }

    private suspend fun getBagList() {
        getBagUseCase().doOnResult(
            onSuccess = {
                _state.value = BagUiState.Data.Loaded(
                    bagUiFactory(
                        bagProducts = it,
                        products = getBagProductDetails(it)
                    ).toImmutableList()
                )
            },
            onError = {
                _state.value = BagUiState.Error
            }
        )
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
}