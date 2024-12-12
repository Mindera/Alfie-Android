package au.com.alfie.ecomm.designsystem.component.price

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

private const val PRICE_RANGE_SEPARATOR = "-"

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
    val style = when (size) {
        PriceSize.Small -> Theme.typography.small
        PriceSize.Medium -> Theme.typography.paragraph
    }
    Text(
        modifier = modifier,
        text = price.price,
        style = style
    )
}

@Composable
private fun PriceSale(
    price: PriceType.Sale,
    size: PriceSize,
    orientation: PriceOrientation,
    modifier: Modifier = Modifier
) {
    val fullPriceStyle = when (size) {
        PriceSize.Small -> Theme.typography.tinyStrikethrough.copy(color = Theme.color.primary.mono600)
        PriceSize.Medium -> Theme.typography.smallStrikethrough.copy(color = Theme.color.primary.mono600)
    }
    val salePriceStyle = when (size) {
        PriceSize.Small -> Theme.typography.small.copy(color = Theme.color.secondary.red800)
        PriceSize.Medium -> Theme.typography.paragraph.copy(color = Theme.color.secondary.red800)
    }
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
    val style = when (size) {
        PriceSize.Small -> Theme.typography.small
        PriceSize.Medium -> Theme.typography.paragraph
    }
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
        Spacer(modifier = Modifier.height(Theme.spacing.spacing4))
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
        Spacer(modifier = Modifier.width(Theme.spacing.spacing4))
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
            Spacer(modifier = Modifier.height(Theme.spacing.spacing6))
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
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.price_range, price.startPrice, price.endPrice),
        style = style
    )
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
