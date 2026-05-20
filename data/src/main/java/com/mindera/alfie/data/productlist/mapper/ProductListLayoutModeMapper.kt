package com.mindera.alfie.data.productlist.mapper

import com.mindera.alfie.data.datastore.UserPreferencesProto.ProductListLayoutModeProto
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode

internal fun ProductListLayoutModeProto.toDomain(): ProductListLayoutMode = when (this) {
    ProductListLayoutModeProto.GRID -> ProductListLayoutMode.GRID
    ProductListLayoutModeProto.COLUMN -> ProductListLayoutMode.COLUMN
    ProductListLayoutModeProto.UNRECOGNIZED -> ProductListLayoutMode.GRID
}

internal fun ProductListLayoutMode.toProto(): ProductListLayoutModeProto = when (this) {
    ProductListLayoutMode.GRID -> ProductListLayoutModeProto.GRID
    ProductListLayoutMode.COLUMN -> ProductListLayoutModeProto.COLUMN
}
