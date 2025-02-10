package au.com.alfie.ecomm.feature.wishlist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.WishlistNavArgs
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.domain.usecase.wishlist.GetWishlistUseCase
import au.com.alfie.ecomm.domain.usecase.wishlist.RemoveFromWishlistUseCase
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
    private val wishlistUiFactory: WishlistUiFactory,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args: WishlistNavArgs = savedStateHandle.navArgs()
    val launchFromTop: Boolean = args.launchFromTop

    private val _state = MutableStateFlow<WishlistUiState>(Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWishlistList()
        }
    }

    private suspend fun getWishlistList() {
        getWishlistUseCase().collectLatest { result ->
            result.doOnResult(
                onSuccess = {
                    _state.value = WishlistUiState.Data.Loaded(
                        it.map {
                            wishlistUiFactory(
                                product = it,
                                onRemoveClick = { onRemoveClicked(it) }
                            )
                        }
                    )
                },
                onError = {
                    _state.value = WishlistUiState.Error
                }
            )
        }
    }

    private fun onRemoveClicked(product: Product) {
        viewModelScope.launch {
            removeFromWishlist(product.id)
        }
    }
}
