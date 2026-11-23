package com.mindera.alfie.feature.bag.models

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.repository.bag.BagLine

/** One Bag line as the Bag screen draws it. */
@Stable
data class BagLineUi(
    val bagLine: BagLine,
    val productCardData: ProductCardType.Horizontal,
    val notice: BagItemNotice? = null
) {
    /** The Cart's line id. */
    val id: String get() = bagLine.id
}
