package au.com.alfie.ecomm.designsystem.component.productcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.productcard.size.ProductCardLarge
import au.com.alfie.ecomm.designsystem.component.productcard.size.ProductCardMedium
import au.com.alfie.ecomm.designsystem.component.productcard.size.ProductCardSmall
import au.com.alfie.ecomm.designsystem.component.productcard.size.ProductCardXSmall

internal val PRICE_PLACEHOLDER_WIDTH = 50.dp

@Composable
fun ProductCard(
    productCardType: ProductCardType,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    when (productCardType) {
        is ProductCardType.XSmall -> ProductCardXSmall(
            productCard = productCardType,
            onClick = onClick,
            modifier = modifier,
            isLoading = isLoading
        )
        is ProductCardType.Small -> ProductCardSmall(
            productCard = productCardType,
            onClick = onClick,
            modifier = modifier,
            isLoading = isLoading
        )
        is ProductCardType.Medium -> ProductCardMedium(
            productCard = productCardType,
            onClick = onClick,
            modifier = modifier,
            isLoading = isLoading
        )
        is ProductCardType.Large -> ProductCardLarge(
            productCard = productCardType,
            onClick = onClick,
            modifier = modifier,
            isLoading = isLoading
        )
    }
}
