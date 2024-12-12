package au.com.alfie.ecomm.data.datastore.debug

import au.com.alfie.ecomm.data.datastore.DebugPreferencesProto.EnvironmentProto

interface DebugPreferencesDataSource {

    suspend fun updateEnvironment(map: () -> EnvironmentProto)

    suspend fun updateCustomUrl(url: String)

    suspend fun <T> getEnvironment(map: (EnvironmentProto, String?) -> T): T

    suspend fun getCustomUrl(): String?
}
