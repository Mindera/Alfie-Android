package com.mindera.alfie.designsystem.component.price

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

private const val PRICE_RANGE_SEPARATOR = "-"

/**
 * Every live price value renders bold — in the Design System (component set `3457:17306`) bold is not
 * a variant, it is the only treatment, and there is no red anywhere in the component. The struck
 * through was-price is the sole exception, in `content/content-terciary`.
 *
 * Note the DS has **no size axis**; [PriceSize] is an Android-only extension kept because
 * `HorizontalProductCard` needs a denser row. `Medium` is the DS size.
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
    orientation: PriceOrientation = PriceOrientation.Horizontal
) {
    when (item) {
        is PriceType.Default -> PriceDefault(
            price = item,
            size = size,
            modifier = modifier
        )
        is PriceType.Range -> PriceRange(
            price = item,
            size = size,
            orientation = orientation,
            modifier = modifier
        )
        is PriceType.Sale -> PriceSale(
            price = item,
            size = size,
            orientation = orientation,
            modifier = modifier
        )
    }
}

@Composable
private fun PriceDefault(
    price: PriceType.Default,
    size: PriceSize,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = price.price,
        style = size.valueStyle(),
        color = LocalTheme.current.color.content.contentPrimary
    )
}

@Composable
private fun PriceSale(
    price: PriceType.Sale,
    size: PriceSize,
    orientation: PriceOrientation,
    modifier: Modifier = Modifier
) {
    // The DS uses content/content-primary for the sale price — there is no error/red treatment.
    val fullPriceStyle = size.wasPriceStyle()
    val salePriceStyle = size.valueStyle().copy(color = LocalTheme.current.color.content.contentPrimary)
    when (orientation) {
        PriceOrientation.Horizontal -> SaleHorizontal(
            modifier = modifier,
            price = price,
            salePriceStyle = salePriceStyle,
            fullPriceStyle = fullPriceStyle
        )
        PriceOrientation.Vertical -> SaleVertical(
            modifier = modifier,
            price = price,
            salePriceStyle = salePriceStyle,
            fullPriceStyle = fullPriceStyle
        )
    }
}

@Composable
private fun PriceRange(
    price: PriceType.Range,
    size: PriceSize,
    orientation: PriceOrientation,
    modifier: Modifier = Modifier
) {
    val style = size.valueStyle().copy(color = LocalTheme.current.color.content.contentPrimary)
    when (orientation) {
        PriceOrientation.Horizontal -> RangeHorizontal(
            modifier = modifier,
            price = price,
            style = style
        )
        PriceOrientation.Vertical -> RangeVertical(
            modifier = modifier,
            style = style,
            price = price
        )
    }
}

@Composable
private fun SaleVertical(
    price: PriceType.Sale,
    salePriceStyle: TextStyle,
    fullPriceStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = End
    ) {
        Text(
            text = price.fullPrice,
            style = fullPriceStyle
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
        Text(
            text = price.salePrice,
            style = salePriceStyle
        )
    }
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
private fun RangeVertical(
    style: TextStyle,
    price: PriceType.Range,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = CenterEnd
    ) {
        Text(
            text = PRICE_RANGE_SEPARATOR,
            style = style
        )
        Column(horizontalAlignment = End) {
            Text(
                text = price.startPrice,
                textAlign = TextAlign.End,
                style = style
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing4))
            Text(
                text = price.endPrice,
                textAlign = TextAlign.End,
                style = style
            )
        }
    }
}

@Composable
private fun RangeHorizontal(
    price: PriceType.Range,
    style: TextStyle,
    modifier: Modifier = Modifier
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
            color = LocalTheme.current.color.content.contentPrimary
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
                size = PriceSize.Medium,
                orientation = PriceOrientation.Vertical
            )
            Price(
                item = PriceType.Sale(
                    fullPrice = "$100",
                    salePrice = "$50"
                ),
                size = PriceSize.Medium,
                orientation = PriceOrientation.Horizontal
            )
        }
    }
}
