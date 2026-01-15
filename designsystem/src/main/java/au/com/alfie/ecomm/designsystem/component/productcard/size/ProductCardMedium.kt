package au.com.alfie.ecomm.designsystem.component.productcard.size

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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

private const val NAME_LINES = 2

@Composable
internal fun ProductCardMedium(
    productCard: ProductCardType.Medium,
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
        Spacer(modifier = Modifier.size(Theme.spacing.spacing12))
        ProductDescription(
            productCard = productCard,
            isLoading = isLoading
        )
    }
}

@Composable
private fun ProductImage(
    productCard: ProductCardType.Medium,
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
            if (productCard.onFavoriteClick != null) {
                ActionIconButton(
                    productCard.onFavoriteClick,
                    R.drawable.ic_action_heart_outline
                )
            } else if (productCard.onRemoveClick != null) {
                ActionIconButton(
                    productCard.onRemoveClick,
                    R.drawable.ic_action_close_dark
                )
            }
        }
    }
}

@Composable
private fun ProductDescription(
    productCard: ProductCardType.Medium,
    isLoading: Boolean
) {
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
        minLines = NAME_LINES,
        maxLines = NAME_LINES,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(
                isShimmering = isLoading,
                lines = NAME_LINES,
                lineHeight = Theme.typography.small.lineHeight,
                lastLineFraction = Theme.scale.scale80
            )
            .testTag(productCard.nameTestTag)
    )

    productCard.color?.let { colorName ->
        Text(
            text = stringResource(id = R.string.product_color, colorName),
            style = Theme.typography.tiny,
            color = Theme.color.primary.mono500,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(
                    isShimmering = isLoading,
                    lines = NAME_LINES,
                    lineHeight = Theme.typography.small.lineHeight,
                    lastLineFraction = Theme.scale.scale80
                )
                .testTag(productCard.colorTestTag)
        )
    }

    productCard.size?.let { sizeName ->
        Text(
            text = stringResource(id = R.string.product_size, sizeName),
            style = Theme.typography.tiny,
            color = Theme.color.primary.mono500,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(
                    isShimmering = isLoading,
                    lines = NAME_LINES,
                    lineHeight = Theme.typography.small.lineHeight,
                    lastLineFraction = Theme.scale.scale80
                )
                .testTag(productCard.sizeTestTag)
        )
    }

    Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
    Price(
        item = productCard.price,
        size = PriceSize.Medium,
        modifier = Modifier
            .shimmer(
                isShimmering = isLoading,
                minWidth = PRICE_PLACEHOLDER_WIDTH
            )
            .testTag(productCard.priceTestTag)
    )
}

@Composable
private fun ActionIconButton(
    onClick: (() -> Unit)?,
    iconRes: Int
) {
    onClick?.let {
        IconButton(
            modifier = Modifier.padding(8.dp).size(Theme.iconSize.large),
            onClick = it
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(Theme.iconSize.small)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ProductCardMediumPreview() {
    val productCard = ProductCardType.Medium(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "Sass & Bide",
        name = "One Line Pant",
        price = PriceType.Default(price = "$ 429.00"),
        onFavoriteClick = {},
        color = "black",
        size = "10AU"
    )
    ProductCardMedium(productCard = productCard, onClick = { })
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ProductCardMediumLoadingPreview() {
    val productCard = ProductCardType.Medium(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "",
        name = "",
        price = PriceType.Default(price = ""),
        onFavoriteClick = {},
        color = "black",
        size = "10AU"
    )
    ProductCardMedium(
        productCard = productCard,
        onClick = { },
        isLoading = true
    )
}
