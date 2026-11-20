package com.mindera.alfie.data.datastore.cart

import androidx.datastore.core.DataStore
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.core.test.TestDispatcherProvider
import com.mindera.alfie.data.datastore.CartSessionProto
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class CartSessionDataSourceImplTest {

    @RelaxedMockK
    private lateinit var dataStore: DataStore<CartSessionProto>

    private lateinit var subject: CartSessionDataSourceImpl

    @BeforeEach
    fun setUp() {
        subject = CartSessionDataSourceImpl(
            dataStore = dataStore,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @Test
    fun `getCartId - WHEN a Cart id is stored THEN returns it`() = runTest {
        coEvery { dataStore.data } returns flowOf(CartSessionProto.newBuilder().setCartId("cart-1").build())

        assertEquals("cart-1", subject.getCartId())
    }

    @Test
    fun `getCartId - WHEN nothing is stored THEN returns null, not an empty id`() = runTest {
        coEvery { dataStore.data } returns flowOf(CartSessionProto.getDefaultInstance())

        assertNull(subject.getCartId())
    }

    @Test
    fun `setCartId - writes the Cart id to the data store`() = runTest {
        val transform = slot<suspend (CartSessionProto) -> CartSessionProto>()
        coEvery { dataStore.updateData(capture(transform)) } coAnswers {
            transform.captured(CartSessionProto.getDefaultInstance())
        }

        subject.setCartId("cart-1")

        assertEquals("cart-1", transform.captured(CartSessionProto.getDefaultInstance()).cartId)
    }
}
