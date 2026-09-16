package com.mindera.alfie.feature.bag.models

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.repository.bag.BagProduct

/**
 * One Bag line. Bag entries are stored one per unit, so a line stands for every unit of the same
 * product variant and [ProductCardType.Horizontal.quantity] carries how many.
 */
@Stable
data class BagProductUi(
    val id: String,
    val bagProduct: BagProduct,
    val productCardData: ProductCardType.Horizontal,
    val notice: BagItemNotice? = null
)
