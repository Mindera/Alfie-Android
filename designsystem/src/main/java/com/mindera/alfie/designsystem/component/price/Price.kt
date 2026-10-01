package com.mindera.alfie.designsystem.component.price

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

private const val PRICE_RANGE_SEPARATOR = "-"

/**
 * Every live price value renders bold — bold is not a variant, it is the only treatment, and there
 * is no red anywhere in this component. The struck-through was-price is the sole exception.
 *
 * Note the DS has **no size axis**; [PriceSize] is an Android-only extension kept because
 * `HorizontalProductCard` needs a denser row. `Medium` is the DS size.
 *
 * Nor does it have an orientation axis: ranges and sales always lay out on one row. The stacked
 * vertical variants predated the modern design and had no Figma counterpart, so they were removed.
 */
@Composable
private fun PriceSize.valueStyle(): TextStyle = when (this) {
    PriceSize.Small -> LocalTheme.current.typography.label.smallBold
    PriceSize.Medium -> LocalTheme.current.typography.body.mediumBold
}

@Composable
private fun PriceSize.wasPriceStyle(): TextStyle = when (this) {
    PriceSize.Small -> LocalTheme.current.typography.label.small
    PriceSize.Medium -> LocalTheme.current.typography.body.mediumStrikethrough
}.copy(
    textDecoration = TextDecoration.LineThrough,
    color = LocalTheme.current.color.content.contentTerciary
)

@Composable
fun Price(
    item: PriceType,
    size: PriceSize,
    modifier: Modifier = Modifier,
    overrideColor: Color? = null
) {
    when (item) {
        is PriceType.Default -> PriceDefault(
            price = item,
            size = size,
            overrideColor = overrideColor,
            modifier = modifier
        )
        is PriceType.Range -> PriceRange(
            price = item,
            size = size,
            overrideColor = overrideColor,
            modifier = modifier
        )
        is PriceType.Sale -> PriceSale(
            price = item,
            size = size,
            overrideColor = overrideColor,
            modifier = modifier
        )
    }
}

@Composable
private fun PriceDefault(
    price: PriceType.Default,
    size: PriceSize,
    modifier: Modifier = Modifier,
    overrideColor: Color? = null
) {
    Text(
        modifier = modifier,
        text = price.price,
        style = size.valueStyle(),
        color = overrideColor ?: LocalTheme.current.color.content.contentPrimary
    )
}

@Composable
private fun PriceSale(
    price: PriceType.Sale,
    size: PriceSize,
    modifier: Modifier = Modifier,
    overrideColor: Color? = null
) {
    // The DS uses content/content-primary for the sale price — there is no error/red treatment.
    val fullPriceStyle = size.wasPriceStyle().let { style -> overrideColor?.let { style.copy(color = it) } ?: style }
    val salePriceStyle = size.valueStyle()
        .copy(color = overrideColor ?: LocalTheme.current.color.content.contentPrimary)
    SaleHorizontal(
        modifier = modifier,
        price = price,
        salePriceStyle = salePriceStyle,
        fullPriceStyle = fullPriceStyle
    )
}

@Composable
private fun PriceRange(
    price: PriceType.Range,
    size: PriceSize,
    modifier: Modifier = Modifier,
    overrideColor: Color? = null
) {
    val style = size.valueStyle().copy(color = overrideColor ?: LocalTheme.current.color.content.contentPrimary)
    RangeHorizontal(
        modifier = modifier,
        price = price,
        style = style,
        separatorColor = overrideColor
    )
}

@Composable
private fun SaleHorizontal(
    price: PriceType.Sale,
    salePriceStyle: TextStyle,
    fullPriceStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = price.salePrice,
            style = salePriceStyle
        )
        Spacer(modifier = Modifier.width(Theme.spacing.spacing8))
        Text(
            text = price.fullPrice,
            style = fullPriceStyle
        )
    }
}

@Composable
private fun RangeHorizontal(
    price: PriceType.Range,
    style: TextStyle,
    modifier: Modifier = Modifier,
    separatorColor: Color? = null
) {
    // The DS renders the bounds bold and the separator at regular weight, so this cannot collapse
    // into a single Text. Gap is spacing/spacing-xxs.
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing4)
    ) {
        Text(text = price.startPrice, style = style)
        Text(
            text = PRICE_RANGE_SEPARATOR,
            style = LocalTheme.current.typography.body.medium,
            color = separatorColor ?: LocalTheme.current.color.content.contentPrimary
        )
        Text(text = price.endPrice, style = style)
    }
}

@Preview(showBackground = true)
@Composable
private fun PricePreview() {
    Theme {
        Column {
            Price(
                item = PriceType.Default(price = "$100"),
                size = PriceSize.Medium
            )
            Price(
                item = PriceType.Range(
                    startPrice = "$100",
                    endPrice = "$200"
                ),
                size = PriceSize.Medium
            )
            Price(
                item = PriceType.Sale(
                    fullPrice = "$100",
                    salePrice = "$50"
                ),
                size = PriceSize.Medium
            )
        }
    }
}
