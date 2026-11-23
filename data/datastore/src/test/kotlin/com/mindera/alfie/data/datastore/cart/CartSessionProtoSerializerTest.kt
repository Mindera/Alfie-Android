package com.mindera.alfie.data.datastore.cart

import androidx.datastore.core.CorruptionException
import com.mindera.alfie.data.datastore.CartSessionProto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class CartSessionProtoSerializerTest {

    private val serializer = CartSessionProtoSerializer()

    @Test
    fun `defaultValue - has no Cart id`() {
        assertEquals("", serializer.defaultValue.cartId)
    }

    @Test
    fun `writeTo and readFrom - round-trip the Cart id`() = runTest {
        val session = CartSessionProto.newBuilder().setCartId("cart-1").build()
        val output = ByteArrayOutputStream()

        serializer.writeTo(session, output)

        assertEquals(session, serializer.readFrom(ByteArrayInputStream(output.toByteArray())))
    }

    @Test
    fun `readFrom - fails when the input is not valid`() = runTest {
        assertThrows<CorruptionException> {
            serializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
        }
    }
}
