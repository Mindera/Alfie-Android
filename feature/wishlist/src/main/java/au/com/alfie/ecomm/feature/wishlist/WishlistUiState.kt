package au.com.alfie.ecomm.feature.wishlist

import au.com.alfie.ecomm.feature.wishlist.models.WishlistProductUi

sealed interface WishlistUiState {

    sealed class Data(open val wishlist: List<WishlistProductUi>) : WishlistUiState {

        data object Loading : Data(emptyList())

        data class Loaded(override val wishlist: List<WishlistProductUi>) : Data(wishlist)
    }

    data object Error : WishlistUiState
}