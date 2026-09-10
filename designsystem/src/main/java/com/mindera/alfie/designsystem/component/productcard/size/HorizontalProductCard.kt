package com.mindera.alfie.designsystem.component.productcard.size

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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

// Figma pins the image at 114.333 x 152.444 with `shrink-0` and lets the info column absorb the rest
// of the row (node 3004:3795). 152.444 / 114.333 is exactly 4:3, so the width alone drives the height
// through Ratio.RATIO3x4 — and with it the row height, which the info column then stretches to.
private val IMAGE_WIDTH = 114.333.dp

// Figma draws the overflow control as a fixed 32dp square (node 3659:49291).
private val OVERFLOW_BUTTON_SIZE = 32.dp

// Unavailable rows dim their image to 50% (node 673:90047). Theme.alpha has no 50% step.
private const val UNAVAILABLE_IMAGE_ALPHA = .5f

/**
 * Bag line item — Figma "Horizontal Product Card" (node `3004:3910`).
 *
 * The info column is stretched to the image's height so the product header sits at the top and the
 * quantity/price row at the bottom, which is why the row is measured at [IntrinsicSize.Min].
 *
 * The card does not draw [ProductCardType.Horizontal.brand]: the modern design leads with the
 * product name and follows it with the variant reference, colour and size.
 */
@Composable
internal fun HorizontalProductCard(
    productCard: ProductCardType.Horizontal,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val theme = LocalTheme.current
    // Unavailable rows drop every value to content/content-terciary (node 673:90047).
    val contentColor = if (productCard.isAvailable) {
        theme.color.content.contentPrimary
    } else {
        theme.color.content.contentTerciary
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(enabled = isLoading.not(), role = Role.Button) {
                productCard.onClick?.invoke()
            }
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .testTag(productCard.cardTestTag) then modifier
    ) {
        Image(
            imageUI = productCard.image,
            ratio = Ratio.RATIO3x4,
            alpha = if (productCard.isAvailable) DefaultAlpha else UNAVAILABLE_IMAGE_ALPHA,
            modifier = Modifier
                .width(IMAGE_WIDTH)
                .background(theme.color.surface.foregroundPrimary)
                .shimmer(isShimmering = isLoading)
                .testTag(productCard.imageTestTag)
        )
        ProductInfo(
            productCard = productCard,
            contentColor = contentColor,
            isLoading = isLoading,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun ProductInfo(
    productCard: ProductCardType.Horizontal,
    contentColor: Color,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = productCard.name,
                    style = theme.typography.body.medium,
                    color = contentColor,
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
                if (productCard.reference.isNotBlank()) {
                    Text(
                        text = stringResource(id = R.string.product_card_reference, productCard.reference),
                        style = theme.typography.label.small,
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shimmer(
                                isShimmering = isLoading,
                                xScale = Theme.scale.scale40
                            )
                            .testTag(productCard.referenceTestTag)
                    )
                }
                LabelledValue(
                    label = stringResource(id = R.string.product_card_color),
                    value = productCard.color,
                    contentColor = contentColor,
                    isLoading = isLoading,
                    shimmerScale = Theme.scale.scale30,
                    modifier = Modifier.testTag(productCard.colorTestTag)
                )
                LabelledValue(
                    label = stringResource(id = R.string.product_card_size),
                    value = productCard.size,
                    contentColor = contentColor,
                    isLoading = isLoading,
                    shimmerScale = Theme.scale.scale20,
                    modifier = Modifier.testTag(productCard.sizeTestTag)
                )
            }
            if (isLoading.not() && productCard.onOverflowClick != null) {
                OverflowButton(
                    contentColor = contentColor,
                    onClick = productCard.onOverflowClick
                )
            }
        }
        QuantityAndPrice(
            productCard = productCard,
            contentColor = contentColor,
            isLoading = isLoading
        )
    }
}

@Composable
private fun LabelledValue(
    label: String,
    value: String,
    contentColor: Color,
    isLoading: Boolean,
    shimmerScale: Float,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing4),
        modifier = modifier
            .fillMaxWidth()
            .shimmer(
                isShimmering = isLoading,
                xScale = shimmerScale
            )
    ) {
        Text(
            text = label,
            style = theme.typography.label.small,
            color = contentColor
        )
        Text(
            text = value,
            style = theme.typography.label.small,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuantityAndPrice(
    productCard: ProductCardType.Horizontal,
    contentColor: Color,
    isLoading: Boolean
) {
    val theme = LocalTheme.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing2),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = theme.spacing.spacing4)
                .shimmer(
                    isShimmering = isLoading,
                    xScale = Theme.scale.scale40
                )
                .testTag(productCard.quantityTestTag)
        ) {
            Text(
                text = stringResource(id = R.string.product_card_quantity),
                style = theme.typography.body.medium,
                color = contentColor
            )
            Text(
                text = productCard.quantity.toString(),
                style = theme.typography.body.medium,
                color = contentColor
            )
            // Editing the quantity is designed in the Figma section titled "Edit Quantity - To
            // discuss" (node 615:95163), so the affordance is drawn but not yet wired up.
            Icon(
                painter = painterResource(id = AlfieIcons.ChevronDown),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(theme.sizing.icon.medium)
            )
        }
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
}

@Composable
private fun OverflowButton(
    contentColor: Color,
    onClick: () -> Unit
) {
    val theme = LocalTheme.current
    Box(
        contentAlignment = Alignment.Center,
        // Figma fixes this control at 32dp, which is under the 48dp minimum touch target.
        // minimumInteractiveComponentSize() would fix that but inflates the reported size to 48dp
        // and narrows the product details column, so the design wins here and the touch target is
        // raised separately — the same call PR #39 made for the Clear link (ALFMOB-507).
        modifier = Modifier
            .size(OVERFLOW_BUTTON_SIZE)
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = AlfieIcons.More),
            contentDescription = stringResource(id = R.string.product_card_more_options_a11y),
            tint = contentColor,
            modifier = Modifier.size(theme.sizing.icon.medium)
        )
    }
}

private fun previewCard(
    isAvailable: Boolean = true,
    quantity: Int = 1
) = ProductCardType.Horizontal(
    image = ImageUI(
        images = persistentListOf(ImageSizeUI.Large("url")),
        alt = ""
    ),
    brand = "Sass & Bide",
    name = "100% Linen Fluid Shirt",
    price = PriceType.Default(price = "£82"),
    color = "Cream",
    size = "S",
    reference = "0283/764",
    quantity = quantity,
    isAvailable = isAvailable,
    onOverflowClick = {}
)

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
private fun HorizontalProductCardPreview() {
    Theme {
        HorizontalProductCard(
            productCard = previewCard(quantity = 2),
            modifier = Modifier.padding(Theme.spacing.spacing16)
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
private fun HorizontalProductCardMultipleQuantityPreview() {
    Theme {
        HorizontalProductCard(
            productCard = previewCard(quantity = 3),
            modifier = Modifier.padding(Theme.spacing.spacing16)
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
private fun HorizontalProductCardUnavailablePreview() {
    Theme {
        HorizontalProductCard(
            productCard = previewCard(isAvailable = false),
            modifier = Modifier.padding(Theme.spacing.spacing16)
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
private fun HorizontalProductCardLoadingPreview() {
    Theme {
        HorizontalProductCard(
            productCard = ProductCardType.Horizontal(
                image = ImageUI(
                    images = persistentListOf(ImageSizeUI.Large("url")),
                    alt = ""
                ),
                brand = "",
                name = "",
                price = PriceType.Default(price = ""),
                color = "",
                size = ""
            ),
            isLoading = true,
            modifier = Modifier.padding(Theme.spacing.spacing16)
        )
    }
}
