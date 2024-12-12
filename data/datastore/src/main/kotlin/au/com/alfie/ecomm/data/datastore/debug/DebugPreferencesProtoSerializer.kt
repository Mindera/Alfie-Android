package au.com.alfie.ecomm.data.datastore.debug

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import au.com.alfie.ecomm.data.datastore.DebugPreferencesProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

internal class DebugPreferencesProtoSerializer : Serializer<DebugPreferencesProto> {

    override val defaultValue: DebugPreferencesProto = DebugPreferencesProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): DebugPreferencesProto = try {
        DebugPreferencesProto.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: DebugPreferencesProto, output: OutputStream) = t.writeTo(output)
}
