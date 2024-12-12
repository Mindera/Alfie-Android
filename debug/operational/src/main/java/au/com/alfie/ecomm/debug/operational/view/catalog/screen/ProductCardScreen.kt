package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCard
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

@Composable
@Destination
internal fun ProductCardScreen() {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(timeMillis = 3000)
        isLoading = false
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)
    ) {
        item(span = { GridItemSpan(2) }) {
            Header("Product Card - XS")
        }
        items(
            items = mockProductsXSmall(),
            span = { GridItemSpan(2) }
        ) {
            ProductCard(
                productCardType = it,
                onClick = {},
                modifier = Modifier.padding(
                    horizontal = Theme.spacing.spacing16,
                    vertical = Theme.spacing.spacing8
                ),
                isLoading = isLoading
            )
        }
        item(span = { GridItemSpan(2) }) {
            Header("Product Card - Small")
        }
        item(span = { GridItemSpan(2) }) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
                mockProductsSmall().forEach {
                    ProductCard(
                        productCardType = it,
                        onClick = { },
                        isLoading = isLoading
                    )
                    Spacer(modifier = Modifier.size(Theme.spacing.spacing12))
                }
            }
        }
        item(span = { GridItemSpan(2) }) {
            Header("Product Card - Medium")
        }
        itemsIndexed(items = mockProductsMedium()) { index, item ->
            ProductCard(
                productCardType = item,
                onClick = {},
                modifier = Modifier.padding(
                    start = if (index % 2 == 0) Theme.spacing.spacing16 else 0.dp,
                    end = if (index % 2 == 1) Theme.spacing.spacing16 else 0.dp,
                    bottom = Theme.spacing.spacing32
                ),
                isLoading = isLoading
            )
        }
        item(span = { GridItemSpan(2) }) {
            Header("Product Card - Large")
        }
        items(
            items = mockProductsLarge(),
            span = { GridItemSpan(2) }
        ) {
            ProductCard(
                productCardType = it,
                onClick = {},
                modifier = Modifier.padding(Theme.spacing.spacing16),
                isLoading = isLoading
            )
        }
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(Theme.spacing.spacing24))
        }
    }
}

@Composable
private fun Header(title: String) {
    Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)) {
        Spacer(modifier = Modifier.height(Theme.spacing.spacing24))
        Text(
            text = title,
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing24))
    }
}

private fun mockProductsXSmall() = listOf(
    ProductCardType.XSmall(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6046183/pexels-photo-6046183.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Sass & Bide",
        name = "One Line Pant",
        price = PriceType.Default(
            price = "$ 390.00"
        ),
        color = "Worn Blue",
        size = "29 in"
    ),
    ProductCardType.XSmall(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6046184/pexels-photo-6046184.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Redone",
        name = "90's high Rise Loose Jean",
        price = PriceType.Range(
            startPrice = "$ 429.00",
            endPrice = "$ 529.00"
        ),
        color = "Worn Blue",
        size = "29 in"
    ),
    ProductCardType.XSmall(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6046231/pexels-photo-6046231.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Bang & Olufsen",
        name = "Beosound A1 2nd Generation Speaker - Lunar Red",
        price = PriceType.Sale(
            fullPrice = "$ 550.00",
            salePrice = "$ 300.00"
        ),
        color = "Lunar Red",
        size = "No Size"
    )
)

private fun mockProductsSmall() = listOf(
    ProductCardType.Small(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6046228/pexels-photo-6046228.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Skims",
        name = "Soft Lounge Long Sleeve Dress",
        price = PriceType.Default(price = "$ 219.00")
    ),
    ProductCardType.Small(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6046213/pexels-photo-6046213.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Unison",
        name = "Racer Slip Dress",
        price = PriceType.Range(
            startPrice = "$ 229.00",
            endPrice = "$ 319.00"
        )
    ),
    ProductCardType.Small(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6046221/pexels-photo-6046221.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Alemais Blank Konage Gucci",
        name = "Paradiso Star Man Pleated Mini Shirtdress",
        price = PriceType.Sale(
            fullPrice = "$ 340.00",
            salePrice = "$ 280.00"
        )
    )
)

private fun mockProductsMedium() = listOf(
    ProductCardType.Medium(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6046219/pexels-photo-6046219.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Anine Bing",
        name = "Miles Sweartshirt Anine Bing Logo Washed Dark Sage",
        price = PriceType.Default(price = "$ 219.00"),
        onFavoriteClick = {}
    ),
    ProductCardType.Medium(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/6045708/pexels-photo-6045708.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Seed Heritage",
        name = "Half Sleeve Polo Top",
        price = PriceType.Range(
            startPrice = "$ 229.00",
            endPrice = "$ 319.00"
        ),
        onFavoriteClick = {}
    ),
    ProductCardType.Medium(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/7671168/pexels-photo-7671168.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Sportscraft",
        name = "Olivia Tape Yarn Cardi",
        price = PriceType.Sale(
            fullPrice = "$ 340.00",
            salePrice = "$ 280.00"
        ),
        onFavoriteClick = {}
    ),
    ProductCardType.Medium(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/2850487/pexels-photo-2850487.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Pangaia Sportscraft Casual Fashion",
        name = "Recycled Nylon NW FLWRDWN Quilted Collarless Jacket",
        price = PriceType.Default(price = "$ 222.00"),
        onFavoriteClick = {}
    )
)

private fun mockProductsLarge() = listOf(
    ProductCardType.Large(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/45982/pexels-photo-45982.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Trenery",
        name = "Organically Grown Cotton Blend Rib Knit Dress",
        price = PriceType.Default(price = "$ 219.00"),
        onFavoriteClick = {}
    ),
    ProductCardType.Large(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/14641437/pexels-photo-14641437.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Haulier",
        name = "Canyon Long Sleeve Tee",
        price = PriceType.Range(
            startPrice = "$ 229.00",
            endPrice = "$ 319.00"
        ),
        onFavoriteClick = {}
    ),
    ProductCardType.Large(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/14641430/pexels-photo-14641430.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Country Road",
        name = "Organically Grown Cotton Cashmere Stripe Polo",
        price = PriceType.Sale(
            fullPrice = "$ 340.00",
            salePrice = "$ 280.00"
        ),
        onFavoriteClick = {}
    ),
    ProductCardType.Large(
        image = ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/9603628/pexels-photo-9603628.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        brand = "Pangaia Sportscraft Casual Fashion Seed Heritage",
        name = "Recycled Nylon NW FLWRDWN Quilted Collarless Jacket  Bing Logo Washed Dark Sage",
        price = PriceType.Range(
            startPrice = "$ 229.00",
            endPrice = "$ 319.00"
        ),
        onFavoriteClick = {}
    )
)
