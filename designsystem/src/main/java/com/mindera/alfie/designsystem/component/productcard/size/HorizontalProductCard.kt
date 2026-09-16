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
import androidx.compose.foundation.layout.requiredSize
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

/**
 * Product card laid out as a row: a fixed-width image, then the product's details, quantity and
 * price filling the rest of the width.
 *
 * The image's aspect ratio sets the row height and the info column stretches to match, which is why
 * the row is measured at [IntrinsicSize.Min].
 *
 * The info column is centred rather than spread to the row's full height. Products vary in how many
 * detail rows they have — a single-variant product may have nothing but a name — and spreading the
 * content strands the name at the top with a large gap above the price.
 *
 * [ProductCardType.Horizontal.brand] is not drawn: this layout leads with the product name.
 */
@Composable
internal fun HorizontalProductCard(
    productCard: ProductCardType.Horizontal,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val theme = LocalTheme.current
    // An unavailable product is greyed out wholesale — image, text and price alike.
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
            alpha = if (productCard.isAvailable) DefaultAlpha else Theme.alpha.alpha50,
            modifier = Modifier
                // Fixed, so the 3:4 ratio decides the row height and the info column absorbs
                // whatever width is left.
                .width(114.333.dp)
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
        verticalArrangement = Arrangement.spacedBy(
            space = theme.spacing.spacing8,
            alignment = Alignment.CenterVertically
        ),
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
                // Blank values drop their row, but never while loading: the placeholder card carries
                // no values at all, and skipping them would shimmer fewer lines than the loaded card
                // draws.
                if (productCard.reference.isNotBlank() || isLoading) {
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
                // Single-variant products carry no colour or size option, and a bare "Color:" with
                // nothing after it reads as broken.
                if (productCard.color.isNotBlank() || isLoading) {
                    LabelledValue(
                        label = stringResource(id = R.string.product_card_color),
                        value = productCard.color,
                        contentColor = contentColor,
                        isLoading = isLoading,
                        shimmerScale = Theme.scale.scale30,
                        modifier = Modifier.testTag(productCard.colorTestTag)
                    )
                }
                if (productCard.size.isNotBlank() || isLoading) {
                    LabelledValue(
                        label = stringResource(id = R.string.product_card_size),
                        value = productCard.size,
                        contentColor = contentColor,
                        isLoading = isLoading,
                        shimmerScale = Theme.scale.scale20,
                        modifier = Modifier.testTag(productCard.sizeTestTag)
                    )
                }
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
            // Presentational for now — picking a quantity is not implemented yet.
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
            // Only unavailable rows override the colour — otherwise this would flatten the
            // struck-through was-price on a sale row to the primary content colour.
            overrideColor = contentColor.takeIf { productCard.isAvailable.not() },
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
        modifier = Modifier.size(theme.sizing.icon.large)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                // The control is drawn smaller than the minimum touch target. requiredSize ignores
                // the parent's constraints, so the touch area grows without the row reserving more
                // space for it or the layout shifting.
                .requiredSize(theme.spacing.spacing48)
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
