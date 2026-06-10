package com.mindera.alfie.feature.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.wishlist.WishlistNavArgs
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.feature.wishlist.WishlistUiState.Data.Loading
import com.mindera.alfie.repository.product.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val removeFromWishlist: RemoveFromWishlistUseCase,
    private val wishlistUiFactory: WishlistUIFactory,
    savedStateHandle: SavedStateHandle,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    private val args: WishlistNavArgs = savedStateHandle.navArgs()
    val launchFromTop: Boolean = args.launchFromTop

    private val _state = MutableStateFlow<WishlistUiState>(Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWishlistList()
        }
    }

    fun onNavigateToProductDetails(productId: String) {
        navigateTo(screen = Screen.ProductDetails(args = productDetailsNavArgs(handle = productId)))
    }

    private suspend fun getWishlistList() {
        getWishlistUseCase().collectLatest { result ->
            when (result) {
                is UseCaseResult.Success -> {
                    val wishlist = wishlistUiFactory(
                        products = result.data,
                        onRemoveClick = ::onRemoveClicked,
                        onAddToBagClick = { onNavigateToProductDetails(it.slug) },
                        onProductClick = { onNavigateToProductDetails(it.slug) }
                    )
                    _state.value = WishlistUiState.Data.Loaded(wishlist)
                }
                is UseCaseResult.Error -> _state.value = WishlistUiState.Error
            }
        }
    }

    private fun onRemoveClicked(product: Product) {
        viewModelScope.launch {
            removeFromWishlist(product.slug)
        }
    }
}
