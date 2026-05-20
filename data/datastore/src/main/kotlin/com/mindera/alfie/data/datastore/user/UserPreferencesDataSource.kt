package com.mindera.alfie.data.datastore.user

import com.mindera.alfie.data.datastore.UserPreferencesProto.ProductListLayoutModeProto

interface UserPreferencesDataSource {

    suspend fun updateProductListLayoutMode(layoutMode: ProductListLayoutModeProto)

    suspend fun getProductListLayoutMode(): ProductListLayoutModeProto
}
