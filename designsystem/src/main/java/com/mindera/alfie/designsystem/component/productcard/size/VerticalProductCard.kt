package com.mindera.alfie.designsystem.component.productcard.size

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.core.ui.util.stringResource
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.image.Image
import com.mindera.alfie.designsystem.component.image.ratio.Ratio
import com.mindera.alfie.designsystem.component.price.Price
import com.mindera.alfie.designsystem.component.price.PriceOrientation
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
internal fun VerticalProductCard(
    productCard: ProductCardType.Vertical,
    modifier: Modifier = Modifier,
    size: VerticalProductCardSize = VerticalProductCardSize.Large,
    isLoading: Boolean = false,
    isWishlisted: Boolean = false
) {
    val sizeModifier = when (size) {
        VerticalProductCardSize.Large -> Modifier.fillMaxWidth()
        is VerticalProductCardSize.Medium -> Modifier.width(size.cardWidth)
    }
    Column(
        modifier = modifier then sizeModifier then Modifier
            .clickable(enabled = isLoading.not()) { productCard.onClick?.invoke() }
            .testTag(productCard.cardTestTag),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing8)
    ) {
        ProductImage(
            productCard = productCard,
            isLoading = isLoading,
            isWishlisted = isWishlisted
        )
        ProductDescription(
            productCard = productCard,
            isLoading = isLoading
        )
        productCard.addToBagClick?.let {
            Button(
                modifier = Modifier.fillMaxWidth(),
                type = ButtonType.Secondary,
                buttonSize = ButtonSize.Medium,
                text = stringResource(StringResource.fromId(R.string.add_to_bag)),
                onClick = it
            )
        }
    }
}

@Composable
private fun ProductImage(
    productCard: ProductCardType.Vertical,
    isLoading: Boolean,
    isWishlisted: Boolean
) {
    val theme = LocalTheme.current
    Box {
        Image(
            imageUI = productCard.image,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(isShimmering = isLoading)
                .testTag(productCard.imageTestTag),
            ratio = Ratio.RATIO3x4
        )
        if (isLoading.not()) {
            productCard.label?.let { label ->
                Text(
                    text = label,
                    style = theme.typography.body.small,
                    color = theme.color.content.contentInvertedPrimary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        // The DS insets the label 8 dp from the image's top-left corner; this padding
                        // sits outside the background so it offsets rather than inflates the chip.
                        .padding(theme.spacing.spacing8)
                        .background(
                            color = theme.color.surface.backgroundInvertedPrimary,
                            shape = Theme.shape.none
                        )
                        .padding(
                            horizontal = theme.spacing.spacing8,
                            vertical = theme.spacing.spacing4
                        )
                )
            }
            if (productCard.onFavoriteClick != null) {
                // Flush to the image's top-right corner. The 40 dp box around a 24 dp glyph is what
                // produces the 8 dp optical inset — the DS draws no scrim or circle behind it.
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(theme.sizing.icon.xlarge),
                    onClick = productCard.onFavoriteClick
                ) {
                    val iconRes =
                        if (isWishlisted) AlfieIcons.WishlistFill else AlfieIcons.Wishlist
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = theme.color.content.contentPrimary,
                        modifier = Modifier.size(theme.sizing.icon.medium)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDescription(
    productCard: ProductCardType.Vertical,
    isLoading: Boolean
) {
    val theme = LocalTheme.current
    // The DS `Product Details` block has itemSpacing 0 and no padding — the brand/name/price rhythm
    // comes from the line heights alone (16/24/24). It also has no trailing spacer, so the text
    // column spans the full card width rather than being inset by 24 dp.
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = productCard.brand,
            style = theme.typography.label.small,
            color = theme.color.content.contentPrimary,
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
        Text(
            text = productCard.name,
            style = theme.typography.body.medium,
            color = theme.color.content.contentPrimary,
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
private fun VerticalProductCardPreview() {
    val productCard = ProductCardType.Vertical(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "Sass & Bide",
        name = "One Line Pant",
        price = PriceType.Default(price = "$ 429.00"),
        onFavoriteClick = {},
        addToBagClick = {}
    )
    VerticalProductCard(productCard = productCard)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun VerticalProductCardLoadingPreview() {
    val productCard = ProductCardType.Vertical(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "",
        name = "",
        price = PriceType.Default(price = ""),
        onFavoriteClick = {}
    )
    VerticalProductCard(
        productCard = productCard,
        isLoading = true
    )
}
