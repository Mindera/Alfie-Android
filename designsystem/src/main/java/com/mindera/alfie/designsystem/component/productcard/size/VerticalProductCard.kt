package com.mindera.alfie.designsystem.component.productcard.size

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.mindera.alfie.designsystem.theme.Theme
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
        if (isLoading.not() && productCard.onFavoriteClick != null) {
            IconButton(
                modifier = Modifier.size(Theme.iconSize.large),
                onClick = productCard.onFavoriteClick
            ) {
                val iconRes =
                    if (isWishlisted) R.drawable.ic_action_heart_fill else R.drawable.ic_action_heart_outline
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(Theme.iconSize.medium)
                )
            }
        }
    }
}

@Composable
private fun ProductDescription(
    productCard: ProductCardType.Vertical,
    isLoading: Boolean
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = productCard.name,
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
        Spacer(modifier = Modifier.size(Theme.spacing.spacing24))
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
