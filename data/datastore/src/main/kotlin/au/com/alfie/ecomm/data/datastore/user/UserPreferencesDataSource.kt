package au.com.alfie.ecomm.data.datastore.user

import au.com.alfie.ecomm.data.datastore.UserPreferencesProto.ProductListLayoutModeProto

interface UserPreferencesDataSource {

    suspend fun updateProductListLayoutMode(layoutMode: ProductListLayoutModeProto)

    suspend fun getProductListLayoutMode(): ProductListLayoutModeProto
}
