package com.mindera.alfie.data.datastore.debug

import androidx.datastore.core.DataStore
import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.data.datastore.DebugPreferencesProto
import com.mindera.alfie.data.datastore.DebugPreferencesProto.Builder
import com.mindera.alfie.data.datastore.DebugPreferencesProto.EnvironmentProto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DebugPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<DebugPreferencesProto>,
    private val dispatcherProvider: DispatcherProvider
) : DebugPreferencesDataSource {

    override suspend fun updateEnvironment(map: () -> EnvironmentProto) {
        withContext(dispatcherProvider.io()) {
            updateDebugPreference { setCurrentEnvironment(map()) }
        }
    }

    override suspend fun updateCustomUrl(url: String) {
        withContext(dispatcherProvider.io()) {
            updateDebugPreference { setCustomUrl(url) }
        }
    }

    override suspend fun <T> getEnvironment(
        map: (EnvironmentProto, String?) -> T
    ): T = withContext(dispatcherProvider.io()) {
        val debugPreferences = dataStore.data.first()
        map(debugPreferences.currentEnvironment, debugPreferences.customUrl)
    }

    override suspend fun getCustomUrl(): String? = dataStore.data.first().customUrl

    private suspend fun updateDebugPreference(transformer: Builder.() -> Builder) {
        dataStore.updateData {
            it.toBuilder()
                .transformer()
                .build()
        }
    }
}
