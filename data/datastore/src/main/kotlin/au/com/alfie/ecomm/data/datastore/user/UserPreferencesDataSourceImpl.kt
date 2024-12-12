package au.com.alfie.ecomm.data.datastore.user

import androidx.datastore.core.DataStore
import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.data.datastore.UserPreferencesProto
import au.com.alfie.ecomm.data.datastore.UserPreferencesProto.ProductListLayoutModeProto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UserPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<UserPreferencesProto>,
    private val dispatcherProvider: DispatcherProvider
) : UserPreferencesDataSource {

    override suspend fun updateProductListLayoutMode(layoutMode: ProductListLayoutModeProto) = withContext(dispatcherProvider.io()) {
        updateUserPreference { setProductListLayoutMode(layoutMode) }
    }

    override suspend fun getProductListLayoutMode(): ProductListLayoutModeProto = withContext(dispatcherProvider.io()) {
        dataStore.data.first().productListLayoutMode
    }

    private suspend fun updateUserPreference(transformer: UserPreferencesProto.Builder.() -> UserPreferencesProto.Builder) {
        dataStore.updateData {
            it.toBuilder()
                .transformer()
                .build()
        }
    }
}
