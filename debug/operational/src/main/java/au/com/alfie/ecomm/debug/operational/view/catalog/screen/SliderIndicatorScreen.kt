package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.indicator.SliderIndicator
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.random.Random

private const val PAGER_SIZE = 3

@Destination
@Composable
fun SliderIndicatorScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing16)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Slider",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SliderIndicator(
            currentItem = 0,
            itemCount = 3,
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        ScrollableSliderIndicator()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing48))
        PagedSliderIndicator()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing48))
    }
}

@Composable
private fun ScrollableSliderIndicator() {
    Column {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Scrollable Slider Indicator",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        val lazyState = rememberLazyListState()
        val colors = List(size = 10) {
            Color(Random.nextInt(until = 256), Random.nextInt(until = 256), Random.nextInt(until = 256))
        }
        LazyRow(
            state = lazyState,
            contentPadding = PaddingValues(horizontal = Theme.spacing.spacing16),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(colors) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(color = it)
                )
            }
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SliderIndicator(
            state = lazyState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.spacing16)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing32))
        val scrollState = rememberScrollState()
        val labels = List(size = 10) { "Label $it" }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.width(Theme.spacing.spacing16))
            labels.forEach {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.border(
                        width = 2.dp,
                        color = Theme.color.primary.mono900
                    )
                ) {
                    Text(
                        text = it,
                        style = Theme.typography.paragraph,
                        modifier = Modifier.padding(Theme.spacing.spacing8)
                    )
                }
            }
            Spacer(modifier = Modifier.width(Theme.spacing.spacing16))
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SliderIndicator(
            state = scrollState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.spacing16)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagedSliderIndicator() {
    Column {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Paged Slider Indicator",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
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
                    .background(
                        Color(
                            red = Random.nextInt(until = 256),
                            green = Random.nextInt(until = 256),
                            blue = Random.nextInt(until = 256)
                        )
                    )
            )
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SliderIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.spacing16),
            currentItem = pagerState.currentPage,
            itemCount = pagerState.pageCount
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SliderIndicatorScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Slider Indicator Screen"),
        showNavigationIcon = false
    )
    Theme {
        SliderIndicatorScreen(topBarState = topBarState)
    }
}
