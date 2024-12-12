package au.com.alfie.ecomm.designsystem.component.productcarousel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.commons.string.toString
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.indicator.SliderIndicator
import au.com.alfie.ecomm.designsystem.component.modifier.overflowNestedScroll
import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCard
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCarousel(
    items: List<ProductCardType.Small>,
    onProductClick: ClickEventOneArg<Int>,
    modifier: Modifier = Modifier,
    title: StringResource? = null,
    description: StringResource? = null,
    actionText: StringResource? = null,
    onActionClick: ClickEvent? = null,
    isSnapEnabled: Boolean = false,
    isSliderEnabled: Boolean = true,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    val contentDescription = stringResource(id = R.string.product_carousel_content_description, items.size)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .semantics {
                this.contentDescription = contentDescription
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = horizontalAlignment
    ) {
        val context = LocalContext.current

        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.spacing16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                title?.let {
                    Text(
                        text = title.toString(context),
                        style = Theme.typography.paragraphBold
                    )
                }
                description?.let {
                    Spacer(modifier = Modifier.size(Theme.spacing.spacing4))
                    Text(
                        text = description.toString(context),
                        style = Theme.typography.paragraph
                    )
                }
            }

            if (actionText != null && onActionClick != null) {
                Box(
                    modifier = Modifier
                        .padding(start = Theme.spacing.spacing16)
                        .clickable { onActionClick() }
                ) {
                    Text(
                        text = actionText.toString(context),
                        color = Theme.color.primary.mono900,
                        style = Theme.typography.smallBoldUnderline
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))

        val state: LazyListState = rememberLazyListState()
        val flingBehavior = if (isSnapEnabled) rememberSnapFlingBehavior(lazyListState = state) else ScrollableDefaults.flingBehavior()
        LazyRow(
            modifier = Modifier
                .wrapContentWidth()
                .overflowNestedScroll(state),
            state = state,
            flingBehavior = flingBehavior,
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing12),
            contentPadding = PaddingValues(horizontal = Theme.spacing.spacing16)
        ) {
            itemsIndexed(
                items = items,
                key = { index, item -> item.name + index }
            ) { index, item ->
                ProductCard(
                    productCardType = item,
                    onClick = {
                        onProductClick(index)
                    }
                )
            }
        }

        if (isSliderEnabled) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 44.dp)
                    .padding(vertical = Theme.spacing.spacing16),
                contentAlignment = Alignment.Center
            ) {
                SliderIndicator(
                    state = state,
                    modifier.fillMaxWidth().padding(Theme.spacing.spacing16)
                )
            }
        }
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductCarouselPreview() {
    Theme {
        val items = List(10) {
            ProductCardType.Small(
                image = ImageUI(
                    images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/4621424/pexels-photo-4621424.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
                    alt = ""
                ),
                brand = "Skims",
                name = "Soft Lounge Long Sleeve Dress",
                price = PriceType.Default(price = "$219.00")
            )
        }
        ProductCarousel(
            items = items,
            onProductClick = { },
            title = StringResource.fromText("New In"),
            description = StringResource.fromText("Get a latest in fashion"),
            actionText = StringResource.fromText("See All"),
            onActionClick = { }
        )
    }
}
