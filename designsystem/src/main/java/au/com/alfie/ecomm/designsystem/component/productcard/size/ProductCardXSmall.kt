package au.com.alfie.ecomm.designsystem.component.productcard.size

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.image.Image
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio
import au.com.alfie.ecomm.designsystem.component.price.Price
import au.com.alfie.ecomm.designsystem.component.price.PriceSize
import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.PRICE_PLACEHOLDER_WIDTH
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ProductCardXSmall(
    productCard: ProductCardType.XSmall,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Row(
        modifier = Modifier
            .clickable(enabled = isLoading.not()) { onClick() }
            .testTag(productCard.cardTestTag) then modifier
    ) {
        Image(
            imageUI = productCard.image,
            modifier = Modifier
                .width(74.dp)
                .shimmer(isShimmering = isLoading)
                .testTag(productCard.imageTestTag),
            ratio = Ratio.RATIO3x4
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = productCard.brand,
                style = Theme.typography.small,
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
                style = Theme.typography.small,
                color = Theme.color.primary.mono500,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(
                        isShimmering = isLoading,
                        xScale = Theme.scale.scale60
                    )
                    .testTag(productCard.nameTestTag)
            )
            Spacer(modifier = Modifier.size(Theme.spacing.spacing4))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(
                        isShimmering = isLoading,
                        xScale = Theme.scale.scale30
                    )
                    .testTag(productCard.colorTestTag)
            ) {
                Text(
                    text = stringResource(id = R.string.product_card_color),
                    style = Theme.typography.tiny,
                    color = Theme.color.primary.mono500
                )
                Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
                Text(
                    text = productCard.color,
                    style = Theme.typography.tiny,
                    color = Theme.color.primary.mono700
                )
            }
            Spacer(modifier = Modifier.size(Theme.spacing.spacing4))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(
                        isShimmering = isLoading,
                        xScale = Theme.scale.scale20
                    )
                    .testTag(productCard.sizeTestTag)
            ) {
                Text(
                    text = stringResource(id = R.string.product_card_size),
                    style = Theme.typography.tiny,
                    color = Theme.color.primary.mono500
                )
                Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
                Text(
                    text = productCard.size,
                    style = Theme.typography.tiny,
                    color = Theme.color.primary.mono700
                )
            }
            Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
            Price(
                item = productCard.price,
                size = PriceSize.Small,
                modifier = Modifier
                    .shimmer(
                        isShimmering = isLoading,
                        minWidth = PRICE_PLACEHOLDER_WIDTH
                    )
                    .testTag(productCard.priceTestTag)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun ProductCardXSmallPreview() {
    val productCard = ProductCardType.XSmall(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "Sass & Bide",
        name = "One Line Pant",
        price = PriceType.Default(price = "$ 390.00"),
        color = "Worn Blue",
        size = "29 in"
    )

    ProductCardXSmall(productCard = productCard, onClick = {})
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun ProductCardXSmallLoadingPreview() {
    val productCard = ProductCardType.XSmall(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "",
        name = "",
        price = PriceType.Default(price = ""),
        color = "",
        size = ""
    )

    ProductCardXSmall(
        productCard = productCard,
        onClick = {},
        isLoading = true
    )
}
