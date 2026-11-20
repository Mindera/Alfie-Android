package com.mindera.alfie.data.datastore.cart

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.mindera.alfie.data.datastore.CartSessionProto
import java.io.InputStream
import java.io.OutputStream

internal class CartSessionProtoSerializer : Serializer<CartSessionProto> {

    override val defaultValue: CartSessionProto = CartSessionProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CartSessionProto = try {
        CartSessionProto.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: CartSessionProto, output: OutputStream) = t.writeTo(output)
}
