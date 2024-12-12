package au.com.alfie.ecomm.designsystem.component.productcard.size

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.image.Image
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio
import au.com.alfie.ecomm.designsystem.component.price.Price
import au.com.alfie.ecomm.designsystem.component.price.PriceOrientation
import au.com.alfie.ecomm.designsystem.component.price.PriceSize
import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.PRICE_PLACEHOLDER_WIDTH
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.persistentListOf

private const val NAME_MAX_LINES = 2

@Composable
internal fun ProductCardLarge(
    productCard: ProductCardType.Large,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier then Modifier
            .clickable(enabled = isLoading.not()) { onClick() }
            .testTag(productCard.cardTestTag)
    ) {
        ProductImage(
            productCard = productCard,
            isLoading = isLoading
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        ProductDescription(
            productCard = productCard,
            isLoading = isLoading
        )
    }
}

@Composable
private fun ProductImage(
    productCard: ProductCardType.Large,
    isLoading: Boolean
) {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Image(
            imageUI = productCard.image,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(isShimmering = isLoading)
                .testTag(productCard.imageTestTag),
            ratio = Ratio.RATIO3x4
        )
        if (isLoading.not()) {
            IconButton(
                modifier = Modifier.size(Theme.iconSize.large),
                onClick = productCard.onFavoriteClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_heart_outline),
                    contentDescription = null,
                    modifier = Modifier.size(Theme.iconSize.medium)
                )
            }
        }
    }
}

@Composable
private fun ProductDescription(
    productCard: ProductCardType.Large,
    isLoading: Boolean
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = productCard.brand,
                style = Theme.typography.paragraph,
                color = Theme.color.primary.mono900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(
                        isShimmering = isLoading,
                        xScale = Theme.scale.scale40
                    )
                    .testTag(productCard.brandTestTag)
            )
            Spacer(modifier = Modifier.size(Theme.spacing.spacing4))
            Text(
                text = productCard.name,
                style = Theme.typography.paragraph,
                color = Theme.color.primary.mono500,
                maxLines = NAME_MAX_LINES,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(
                        isShimmering = isLoading,
                        lines = NAME_MAX_LINES,
                        lineHeight = Theme.typography.paragraph.lineHeight,
                        lastLineFraction = Theme.scale.scale80
                    )
                    .testTag(productCard.nameTestTag)
            )
        }
        Spacer(modifier = Modifier.size(Theme.spacing.spacing24))
        Price(
            item = productCard.price,
            size = PriceSize.Medium,
            orientation = PriceOrientation.Vertical,
            modifier = Modifier
                .shimmer(
                    isShimmering = isLoading,
                    minWidth = PRICE_PLACEHOLDER_WIDTH
                )
                .testTag(productCard.priceTestTag)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ProductCardLargePreview() {
    val productCard = ProductCardType.Large(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "Sass & Bide",
        name = "One Line Pant",
        price = PriceType.Default(price = "$ 429.00"),
        onFavoriteClick = {}
    )
    ProductCardLarge(productCard = productCard, onClick = { })
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ProductCardLargeLoadingPreview() {
    val productCard = ProductCardType.Large(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "",
        name = "",
        price = PriceType.Default(price = ""),
        onFavoriteClick = {}
    )
    ProductCardLarge(
        productCard = productCard,
        onClick = { },
        isLoading = true
    )
}
