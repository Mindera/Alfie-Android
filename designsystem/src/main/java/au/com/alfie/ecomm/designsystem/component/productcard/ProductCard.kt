package au.com.alfie.ecomm.designsystem.component.productcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.productcard.size.HorizontalProductCard
import au.com.alfie.ecomm.designsystem.component.productcard.size.VerticalProductCard

internal val PRICE_PLACEHOLDER_WIDTH = 50.dp

@Composable
fun ProductCard(
    productCardType: ProductCardType,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isWishlisted: Boolean = false
) {
    when (productCardType) {
        is ProductCardType.Horizontal -> HorizontalProductCard(
            productCard = productCardType,
            onClick = onClick,
            modifier = modifier,
            isLoading = isLoading
        )
        is ProductCardType.Vertical -> VerticalProductCard(
            productCard = productCardType,
            onClick = onClick,
            modifier = modifier,
            isLoading = isLoading,
            isWishlisted = isWishlisted,
        )
    }
}
