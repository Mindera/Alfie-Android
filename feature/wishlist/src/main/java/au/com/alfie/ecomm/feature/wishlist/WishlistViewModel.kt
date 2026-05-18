package au.com.alfie.ecomm.feature.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.productDetailsNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.WishlistNavArgs
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.wishlist.GetWishlistUseCase
import au.com.alfie.ecomm.domain.usecase.wishlist.RemoveFromWishlistUseCase
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.feature.wishlist.WishlistUiState.Data.Loading
import au.com.alfie.ecomm.repository.product.model.Product
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
        navigateTo(screen = Screen.ProductDetails(args = productDetailsNavArgs(id = productId)))
    }

    private suspend fun getWishlistList() {
        getWishlistUseCase().collectLatest { result ->
            when (result) {
                is UseCaseResult.Success -> {
                    val wishlist = wishlistUiFactory(
                        products = result.data,
                        onRemoveClick = ::onRemoveClicked,
                        onAddToBagClick = { onNavigateToProductDetails(it.id) },
                        onProductClick = { onNavigateToProductDetails(it.id) }
                    )
                    _state.value = WishlistUiState.Data.Loaded(wishlist)
                }
                is UseCaseResult.Error -> _state.value = WishlistUiState.Error
            }
        }
    }

    private fun onRemoveClicked(product: Product) {
        viewModelScope.launch {
            removeFromWishlist(product.id)
        }
    }
}
