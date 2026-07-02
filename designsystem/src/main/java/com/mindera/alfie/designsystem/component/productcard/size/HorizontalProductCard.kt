package com.mindera.alfie.designsystem.component.productcard.size

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.component.image.Image
import com.mindera.alfie.designsystem.component.image.ratio.Ratio
import com.mindera.alfie.designsystem.component.price.Price
import com.mindera.alfie.designsystem.component.price.PriceSize
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.PRICE_PLACEHOLDER_WIDTH
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.designsystem.component.shimmer.shimmer
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun HorizontalProductCard(
    productCard: ProductCardType.Horizontal,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val c = LocalTheme.current.primitive.colors
    Row(
        modifier = Modifier
            .clickable(enabled = isLoading.not()) { productCard.onClick?.invoke() }
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
                color = c.neutrals800,
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
                color = c.neutrals500,
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
                    color = c.neutrals500
                )
                Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
                Text(
                    text = productCard.color,
                    style = Theme.typography.tiny,
                    color = c.neutrals600
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
                    color = c.neutrals500
                )
                Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
                Text(
                    text = productCard.size,
                    style = Theme.typography.tiny,
                    color = c.neutrals600
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
        if (isLoading.not() && productCard.onRemoveClick != null) {
            IconButton(
                modifier = Modifier.size(Theme.iconSize.large),
                onClick = productCard.onRemoveClick
            ) {
                Icon(
                    painter = painterResource(id = AlfieIcons.Close),
                    contentDescription = null,
                    modifier = Modifier.size(Theme.iconSize.small)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun HorizontalProductCardPreview() {
    val productCard = ProductCardType.Horizontal(
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

    HorizontalProductCard(productCard = productCard)
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun HorizontalProductCardLoadingPreview() {
    val productCard = ProductCardType.Horizontal(
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

    HorizontalProductCard(
        productCard = productCard,
        isLoading = true
    )
}
