package au.com.alfie.ecomm.data.datastore.user

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import au.com.alfie.ecomm.data.datastore.UserPreferencesProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

internal class UserPreferencesProtoSerializer : Serializer<UserPreferencesProto> {

    override val defaultValue: UserPreferencesProto = UserPreferencesProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferencesProto = try {
        UserPreferencesProto.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: UserPreferencesProto, output: OutputStream) = t.writeTo(output)
}
