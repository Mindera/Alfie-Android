package com.mindera.alfie.feature.wishlist

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.wishlist.WishlistNavArgs
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.snackbar.ActionToast
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.feature.wishlist.WishlistUiState.Data.Loading
import com.mindera.alfie.repository.product.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mindera.alfie.designsystem.R as DesignR

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val removeFromWishlist: RemoveFromWishlistUseCase,
    private val addToWishlist: AddToWishlistUseCase,
    private val wishlistUiFactory: WishlistUIFactory,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    private val args: WishlistNavArgs = savedStateHandle.navArgs()
    val launchFromTop: Boolean = args.launchFromTop

    private val _state = MutableStateFlow<WishlistUiState>(Loading)
    val state = _state.asStateFlow()

    private var wishlistJob: Job? = null

    init {
        getWishlistList()
    }

    fun onNavigateToProductDetails(productId: String) {
        navigateTo(screen = Screen.ProductDetails(args = productDetailsNavArgs(handle = productId)))
    }

    /** Re-runs the wishlist load after an error. */
    fun onRetry() {
        _state.value = Loading
        getWishlistList()
    }

    private fun getWishlistList() {
        wishlistJob?.cancel()
        wishlistJob = viewModelScope.launch {
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
    }

    /**
     * Unwishlisting from the card's heart is a single tap, so the removal is confirmed with a toast
     * that can undo it. A failure is surfaced rather than swallowed: the card would otherwise stay
     * put with nothing saying why.
     */
    private fun onRemoveClicked(product: Product) {
        viewModelScope.launch {
            removeFromWishlist(product.slug).doOnResult(
                onSuccess = {
                    showSnackbar(ActionToast.removed(context = context, onUndo = { undoRemove(product) }))
                },
                onError = { showError(DesignR.string.wishlist_error_remove_product) }
            )
        }
    }

    // The wishlist lists in insertion order, so a restored item comes back at the end rather than
    // where it was — there is no requirement for undo to preserve the position.
    private fun undoRemove(product: Product) {
        viewModelScope.launch {
            addToWishlist(product.slug).doOnResult(
                onSuccess = { },
                onError = { showError(DesignR.string.wishlist_error_add_product) }
            )
        }
    }

    private fun showError(message: Int) {
        showSnackbar(
            SnackbarCustomVisuals.Snackbar(
                type = SnackbarType.Error,
                message = context.getString(message)
            )
        )
    }
}
