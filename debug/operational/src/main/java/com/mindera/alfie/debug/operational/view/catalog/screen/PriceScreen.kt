package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.component.price.Price
import com.mindera.alfie.designsystem.component.price.PriceSize
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.ramcosta.composedestinations.annotation.Destination

private val prices: List<PriceType> = listOf(
    PriceType.Default(
        price = "$100"
    ),
    PriceType.Range(
        startPrice = "$100",
        endPrice = "$200"
    ),
    PriceType.Sale(
        fullPrice = "$100",
        salePrice = "$50"
    )
)

@Destination
@Composable
internal fun PriceScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing16)
            .verticalScroll(rememberScrollState())
    ) {
        PriceSection(
            title = "Price Small/Medium",
            priceType = prices[0]
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        PriceSection(
            title = "Price Range Small/Medium",
            priceType = prices[1]
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        PriceSection(
            title = "Price Sale Small/Medium",
            priceType = prices[2]
        )

        Spacer(modifier = Modifier.height(Theme.spacing.spacing48))
    }
}

@Composable
private fun PriceSection(
    title: String,
    priceType: PriceType
) {
    Column {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = title,
            style = LocalTheme.current.typography.heading.small
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Row(modifier = Modifier.padding(Theme.spacing.spacing12)) {
            Price(
                item = priceType,
                size = PriceSize.Small
            )
            Spacer(modifier = Modifier.width(Theme.spacing.spacing24))
            Price(
                item = priceType,
                size = PriceSize.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PriceScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Price Screen"),
        showNavigationIcon = false
    )
    Theme {
        PriceScreen(topBarState = topBarState)
    }
}
