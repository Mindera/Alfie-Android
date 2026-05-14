package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.designsystem.component.productcarousel.ProductCarousel
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

private val items = List(8) {
    ProductCardType.Small(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6046229/pexels-photo-6046229.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Skims",
        name = "Soft Lounge Long Sleeve Dress",
        price = PriceType.Default(price = "$219.00")
    )
}

@Destination
@Composable
fun ProductCarouselScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = Theme.spacing.spacing16)
    ) {
        ProductCarouselSection(
            sectionTitle = "Carousel with title + desc. + action",
            title = StringResource.fromText("New In"),
            description = StringResource.fromText("Get a latest in fashion"),
            actionText = StringResource.fromText("See All"),
            action = { }
        )
        ProductCarouselSection(
            sectionTitle = "Carousel with title + action",
            title = StringResource.fromText("New In"),
            actionText = StringResource.fromText("See All"),
            action = { }
        )
        ProductCarouselSection(
            sectionTitle = "Carousel with title",
            title = StringResource.fromText("New In")
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    }
}

@Composable
private fun ProductCarouselSection(
    sectionTitle: String,
    title: StringResource? = null,
    description: StringResource? = null,
    actionText: StringResource? = null,
    action: ClickEvent? = null
) {
    Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
    Text(
        modifier = Modifier.padding(Theme.spacing.spacing12),
        text = sectionTitle,
        style = Theme.typography.heading3
    )
    HorizontalDivider()
    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    ProductCarousel(
        items = items,
        onProductClick = { },
        title = title,
        description = description,
        actionText = actionText,
        onActionClick = action
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ProductCarouselScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Product Carousel Screen"),
        showNavigationIcon = false
    )
    ProductCarouselScreen(topBarState = topBarState)
}
