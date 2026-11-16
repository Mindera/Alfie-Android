package com.mindera.alfie.feature.bag

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.bag.AddToBagUseCase
import com.mindera.alfie.domain.usecase.bag.GetBagUseCase
import com.mindera.alfie.domain.usecase.bag.RefreshBagUseCase
import com.mindera.alfie.domain.usecase.bag.RemoveFromBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.feature.bag.BagUiState.Data.Loading
import com.mindera.alfie.feature.bag.models.BagContentUi
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagLine
import com.mindera.alfie.repository.product.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
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
    private val refreshBagUseCase: RefreshBagUseCase,
    private val removeFromBagUseCase: RemoveFromBagUseCase,
    private val addToBagUseCase: AddToBagUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val bagUiFactory: BagUiFactory,
    @ApplicationContext private val context: Context,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(),
    UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<BagUiState>(Loading)
    internal val state = _state.asStateFlow()

    // Products fetched for Bag lines, by slug, so a Bag change only fetches what's new. A failed
    // fetch isn't cached and is tried again on the next Bag.
    private val productsBySlug = mutableMapOf<String, Product>()

    // The Bag as last built, before hiding the lines being removed.
    private var content: BagContentUi? = null

    // Line ids whose remove is in flight or just succeeded. They stay hidden until the Bag that no
    // longer holds them arrives, so the row never flickers back.
    private val removingLineIds = mutableSetOf<String>()

    init {
        collectBag()
    }

    /** Fetches the Cart again after an error; the result arrives through the Bag collection. */
    internal fun onRetry() {
        _state.value = Loading
        viewModelScope.launch { refreshBagUseCase() }
    }

    private fun collectBag() {
        viewModelScope.launch {
            getBagUseCase().collectLatest { result ->
                result.doOnResult(
                    onSuccess = { bag ->
                        fetchMissingProducts(bag)
                        content = bagUiFactory(
                            bag = bag,
                            productsBySlug = productsBySlug,
                            onProductClick = ::openProduct
                        )
                        removingLineIds.retainAll(bag.lines.map { it.id }.toSet())
                        publish()
                    },
                    onError = {
                        content = null
                        _state.value = BagUiState.Error
                    }
                )
            }
        }
    }

    private suspend fun fetchMissingProducts(bag: Bag) {
        val missingSlugs = bag.lines.mapNotNull { it.slug }.distinct().filterNot(productsBySlug::containsKey)
        coroutineScope {
            missingSlugs.map { slug -> async { getProductUseCase(slug) } }
                .awaitAll()
                .forEach { result ->
                    result.doOnResult(
                        onSuccess = { product -> productsBySlug[product.slug] = product },
                        onError = { }
                    )
                }
        }
    }

    private fun publish() {
        val built = content ?: return
        val visible = built.items.filterNot { it.id in removingLineIds }
        _state.value = if (visible.isEmpty()) {
            BagUiState.Data.Empty
        } else {
            BagUiState.Data.Loaded(built.copy(items = visible.toImmutableList()))
        }
    }

    private fun openProduct(handle: String) {
        navigateTo(
            screen = Screen.ProductDetails(
                args = productDetailsNavArgs(handle = handle)
            )
        )
    }

    /**
     * Removes the whole line from the Cart. The line hides while the call is in flight and comes
     * back, with an error, if it fails. Undo adds the same variant and quantity back, and the line
     * returns at the end of the Bag.
     */
    internal fun onRemoveClicked(line: BagLine) {
        removingLineIds += line.id
        publish()

        viewModelScope.launch {
            removeFromBagUseCase(line.id).doOnResult(
                onSuccess = {
                    showSnackbar(
                        SnackbarCustomVisuals.Snackbar(
                            type = SnackbarType.Success,
                            message = context.getString(R.string.bag_item_removed),
                            actionLabel = line.variantId?.let { context.getString(R.string.bag_item_removed_undo) },
                            onActionClick = { undoRemove(line) }
                        )
                    )
                },
                onError = {
                    removingLineIds -= line.id
                    publish()
                    showSnackbar(
                        SnackbarCustomVisuals.Snackbar(
                            type = SnackbarType.Error,
                            message = context.getString(R.string.bag_item_remove_error)
                        )
                    )
                }
            )
        }
    }

    private fun undoRemove(line: BagLine) {
        val variantId = line.variantId ?: return
        viewModelScope.launch {
            addToBagUseCase(variantId = variantId, productId = line.productId, quantity = line.quantity).doOnResult(
                onSuccess = { },
                onError = {
                    showSnackbar(
                        SnackbarCustomVisuals.Snackbar(
                            type = SnackbarType.Error,
                            message = context.getString(R.string.bag_item_restore_error)
                        )
                    )
                }
            )
        }
    }

    /**
     * Adds the product to the wishlist and leaves the Bag untouched: Save and Remove are separate
     * actions, so saving is not a move.
     */
    internal fun onSaveClicked(line: BagLine) {
        viewModelScope.launch {
            val slug = line.slug
            if (slug == null) {
                showSaveError()
                return@launch
            }
            addToWishlistUseCase(slug).doOnResult(
                onSuccess = {
                    showSnackbar(
                        SnackbarCustomVisuals.Snackbar(
                            type = SnackbarType.Success,
                            message = context.getString(R.string.bag_item_saved)
                        )
                    )
                },
                onError = { showSaveError() }
            )
        }
    }

    private fun showSaveError() {
        showSnackbar(
            SnackbarCustomVisuals.Snackbar(
                type = SnackbarType.Error,
                message = context.getString(DesignR.string.wishlist_error_add_product)
            )
        )
    }
}
