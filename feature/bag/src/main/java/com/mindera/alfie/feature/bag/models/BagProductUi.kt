package com.mindera.alfie.feature.bag.models

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.repository.bag.BagLine

/** One Bag line as the Bag screen draws it. [id] is the Cart's line id. */
@Stable
data class BagProductUi(
    val id: String,
    val line: BagLine,
    val productCardData: ProductCardType.Horizontal,
    val notice: BagItemNotice? = null
)
