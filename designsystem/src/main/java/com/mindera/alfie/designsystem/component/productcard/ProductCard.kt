package com.mindera.alfie.designsystem.component.productcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.component.productcard.size.HorizontalProductCard
import com.mindera.alfie.designsystem.component.productcard.size.VerticalProductCard
import com.mindera.alfie.designsystem.component.productcard.size.VerticalProductCardSize

internal val PRICE_PLACEHOLDER_WIDTH = 50.dp

@Composable
fun ProductCard(
    productCardType: ProductCardType,
    modifier: Modifier = Modifier,
    size: VerticalProductCardSize = VerticalProductCardSize.Large,
    isLoading: Boolean = false,
    isWishlisted: Boolean = false
) {
    when (productCardType) {
        is ProductCardType.Horizontal -> HorizontalProductCard(
            productCard = productCardType,
            modifier = modifier,
            isLoading = isLoading
        )
        is ProductCardType.Vertical -> VerticalProductCard(
            productCard = productCardType,
            modifier = modifier,
            size = size,
            isLoading = isLoading,
            isWishlisted = isWishlisted
        )
    }
}
