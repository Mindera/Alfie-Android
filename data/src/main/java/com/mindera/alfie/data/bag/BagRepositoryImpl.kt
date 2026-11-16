package com.mindera.alfie.data.bag

import com.apollographql.apollo.api.Optional
import com.mindera.alfie.data.bag.mapper.toDomain
import com.mindera.alfie.data.bag.service.CartService
import com.mindera.alfie.data.datastore.cart.CartSessionDataSource
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.graphql.bff.fragment.CartFragment
import com.mindera.alfie.graphql.bff.type.CartLineInput
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagRepository
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.result.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

/**
 * The Bag on the BFF Cart. Bound as a singleton, so the Bag screen, the PDP and the Bag tab badge
 * share one Bag. Decisions and their reasons: `docs/adr/0001-the-bag-lives-on-the-bff-cart.md`.
 */
internal class BagRepositoryImpl @Inject constructor(
    private val cartService: CartService,
    private val cartSession: CartSessionDataSource
) : BagRepository {

    // Every Cart call runs one at a time. Without it, a double-tap on Add to Bag before any Cart
    // exists creates two Carts and one line silently disappears; out-of-order responses could also
    // bring back a line that was just removed.
    private val mutex = Mutex()

    // Null until the Bag is first known.
    private val bag = MutableStateFlow<RepositoryResult<Bag>?>(null)

    override fun getBag(): Flow<RepositoryResult<Bag>> = bag
        .onStart { if (bag.value == null) refreshBag() }
        .filterNotNull()

    override suspend fun refreshBag(): RepositoryResult<Bag> = mutex.withLock {
        val cartId = cartSession.getCartId()
        val result = if (cartId == null) {
            RepositoryResult.Success(Bag.EMPTY)
        } else {
            cartService.getCart(cartId).toBag()
        }
        bag.value = result
        result
    }

    override suspend fun addToBag(
        variantId: String,
        productId: String?,
        quantity: Int
    ): RepositoryResult<Bag> = mutex.withLock {
        val lines = listOf(
            CartLineInput(
                variantId = variantId,
                productId = Optional.presentIfNotNull(productId),
                quantity = Optional.present(quantity)
            )
        )
        val cartId = cartSession.getCartId()
        val cart = if (cartId == null) {
            // The first add creates the Cart with its line, in one round-trip.
            cartService.createCart(lines).onSuccess { created -> storeCartId(created.id) }
        } else {
            cartService.addToCart(cartId = cartId, lines = lines)
        }
        cart.toBag().publish()
    }

    override suspend fun removeFromBag(lineId: String): RepositoryResult<Bag> = mutex.withLock {
        val cartId = cartSession.getCartId()
            ?: return@withLock RepositoryResult.Error(ErrorResult(type = ErrorType.RESOURCE_NOT_FOUND))
        cartService.removeFromCart(cartId = cartId, lineId = lineId).toBag().publish()
    }

    // A Cart the device can't remember is still a valid Cart for this session, so a failed write
    // of its id is logged rather than failing the add. The next launch starts a new Cart.
    private suspend fun storeCartId(cartId: String) {
        runCatching { cartSession.setCartId(cartId) }
            .onFailure { Timber.e(it, "Couldn't store the Cart id") }
    }

    private fun Result<CartFragment>.toBag(): RepositoryResult<Bag> =
        mapCatching { it.toDomain() }.toRepositoryResult()

    // Writes are pessimistic: the Cart the BFF answered with replaces the Bag wholesale. A failed
    // write leaves the Bag as it was.
    private fun RepositoryResult<Bag>.publish(): RepositoryResult<Bag> =
        onSuccess { bag.value = RepositoryResult.Success(it) }
}
