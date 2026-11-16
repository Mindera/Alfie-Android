package com.mindera.alfie.data.datastore.cart

import androidx.datastore.core.DataStore
import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.data.datastore.CartSessionProto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CartSessionDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<CartSessionProto>,
    private val dispatcherProvider: DispatcherProvider
) : CartSessionDataSource {

    override suspend fun getCartId(): String? = withContext(dispatcherProvider.io()) {
        dataStore.data.first().cartId.takeIf { it.isNotEmpty() }
    }

    override suspend fun setCartId(cartId: String) {
        withContext(dispatcherProvider.io()) {
            dataStore.updateData { it.toBuilder().setCartId(cartId).build() }
        }
    }

    override suspend fun clearCartId() {
        withContext(dispatcherProvider.io()) {
            dataStore.updateData { it.toBuilder().clearCartId().build() }
        }
    }
}
