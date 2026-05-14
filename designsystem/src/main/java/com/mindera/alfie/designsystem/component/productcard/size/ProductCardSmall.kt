package com.mindera.alfie.designsystem.component.productcard.size

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.image.Image
import com.mindera.alfie.designsystem.component.image.ratio.Ratio
import com.mindera.alfie.designsystem.component.price.Price
import com.mindera.alfie.designsystem.component.price.PriceSize
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.PRICE_PLACEHOLDER_WIDTH
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.designsystem.component.shimmer.shimmer
import com.mindera.alfie.designsystem.theme.Theme
import kotlinx.collections.immutable.persistentListOf

private const val DESCRIPTION_LINES = 2

@Composable
internal fun ProductCardSmall(
    productCard: ProductCardType.Small,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = Modifier
            .clickable(enabled = isLoading.not()) { onClick() }
            .testTag(productCard.cardTestTag) then modifier.width(130.dp)
    ) {
        Image(
            imageUI = productCard.image,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(isShimmering = isLoading)
                .testTag(productCard.imageTestTag),
            ratio = Ratio.RATIO3x4
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
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
                    xScale = Theme.scale.scale50
                )
                .testTag(productCard.brandTestTag)
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
        Text(
            text = productCard.name,
            style = Theme.typography.small,
            color = Theme.color.primary.mono500,
            minLines = DESCRIPTION_LINES,
            maxLines = DESCRIPTION_LINES,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(
                    isShimmering = isLoading,
                    lines = DESCRIPTION_LINES,
                    lineHeight = Theme.typography.small.lineHeight,
                    lastLineFraction = Theme.scale.scale80
                )
                .testTag(productCard.nameTestTag)
        )
        productCard.price?.let { price ->
            Spacer(modifier = Modifier.size(Theme.spacing.spacing8))
            Price(
                item = price,
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ProductCardSmallPreview() {
    val productCard = ProductCardType.Small(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "Sass & Bide",
        name = "One Line Pant",
        price = PriceType.Default(price = "$ 429.00")
    )
    ProductCardSmall(productCard = productCard, onClick = { })
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ProductCardSmallLoadingPreview() {
    val productCard = ProductCardType.Small(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("url")),
            alt = ""
        ),
        brand = "",
        name = "",
        price = PriceType.Default(price = "")
    )
    ProductCardSmall(
        productCard = productCard,
        onClick = { },
        isLoading = true
    )
}
