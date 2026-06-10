package com.mindera.alfie.feature.plp.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.productcard.ProductCardType

@Stable
internal data class ProductListEntryUI(
    val id: String,
    val slug: String,
    val productCardData: ProductCardType
)
