package com.mindera.alfie.feature.bag.models

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.productcard.ProductCardType

@Stable
data class BagProductUi(
    val id: String,
    val productCardData: ProductCardType
)
