package com.mindera.alfie.feature.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.arguments.wishlist.WishlistNavArgs
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
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
                onSuccess = { productList ->
                    val wishlist = wishlistUiFactory(
                        products = productList,
                        onRemoveClick = ::onRemoveClicked
                    )
                    _state.value = WishlistUiState.Data.Loaded(wishlist)
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
