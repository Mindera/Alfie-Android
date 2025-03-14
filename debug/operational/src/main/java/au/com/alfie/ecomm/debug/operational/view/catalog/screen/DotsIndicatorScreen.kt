package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.indicator.DotsIndicator
import au.com.alfie.ecomm.designsystem.component.indicator.DotsIndicatorSize
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.random.Random

private const val PAGER_SIZE = 3

@Destination
@Composable
internal fun DotsIndicatorScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing16)
            .verticalScroll(rememberScrollState())
    ) {
        DotsIndicatorSection(
            title = "Small Dots Indicator",
            size = DotsIndicatorSize.Small
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing48))
        DotsIndicatorSection(
            title = "Medium Dots Indicator",
            size = DotsIndicatorSize.Medium
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing48))
        DotsIndicatorSection(
            title = "Large Dots Indicator",
            size = DotsIndicatorSize.Large
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing48))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DotsIndicatorSection(
    title: String,
    size: DotsIndicatorSize
) {
    Text(
        modifier = Modifier.padding(Theme.spacing.spacing12),
        text = title,
        style = Theme.typography.heading3
    )
    HorizontalDivider()
    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    DotsIndicator(
        currentItem = { 0 },
        itemCount = 3,
        size = size,
        modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
    )
    Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
    DotsIndicator(
        currentItem = { 1 },
        itemCount = 3,
        size = size,
        modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
    )
    Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
    DotsIndicator(
        currentItem = { 2 },
        itemCount = 3,
        size = size,
        modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
    )
    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    val pagerState = rememberPagerState { PAGER_SIZE }
    HorizontalPager(
        state = pagerState,
        beyondViewportPageCount = 2
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = Theme.spacing.spacing16)
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(Random.nextInt(until = 256), Random.nextInt(until = 256), Random.nextInt(until = 256)))
        )
    }
    Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
    DotsIndicator(
        currentItem = { pagerState.currentPage },
        itemCount = pagerState.pageCount,
        size = size,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun DotsIndicatorScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Dots Indicator Screen"),
        showNavigationIcon = false
    )
    Theme {
        DotsIndicatorScreen(topBarState = topBarState)
    }
}
