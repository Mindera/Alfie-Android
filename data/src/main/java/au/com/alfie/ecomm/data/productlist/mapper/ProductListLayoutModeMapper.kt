package au.com.alfie.ecomm.data.productlist.mapper

import au.com.alfie.ecomm.data.datastore.UserPreferencesProto.ProductListLayoutModeProto
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode

internal fun ProductListLayoutModeProto.toDomain(): ProductListLayoutMode = when (this) {
    ProductListLayoutModeProto.GRID -> ProductListLayoutMode.GRID
    ProductListLayoutModeProto.COLUMN -> ProductListLayoutMode.COLUMN
    ProductListLayoutModeProto.UNRECOGNIZED -> ProductListLayoutMode.GRID
}

internal fun ProductListLayoutMode.toProto(): ProductListLayoutModeProto = when (this) {
    ProductListLayoutMode.GRID -> ProductListLayoutModeProto.GRID
    ProductListLayoutMode.COLUMN -> ProductListLayoutModeProto.COLUMN
}
