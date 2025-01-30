package au.com.alfie.ecomm.feature.pdp

import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.ui.media.GalleryUI
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonProperties
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonState
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchType
import au.com.alfie.ecomm.designsystem.component.tab.TabItem
import au.com.alfie.ecomm.feature.pdp.model.ColorUI
import au.com.alfie.ecomm.feature.pdp.model.InformationUI
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsSectionItem
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsShareInfo
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUI
import au.com.alfie.ecomm.feature.pdp.model.SizeSectionUI
import au.com.alfie.ecomm.feature.pdp.model.SizeUI
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.product.model.Variant
import au.com.alfie.ecomm.repository.shared.model.Attribute
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.HierarchyItem
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Money
import au.com.alfie.ecomm.repository.shared.model.Size
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.ui.graphics.Color as AC

internal val product = Product(
    id = "2666503",
    attributes = emptyList(),
    brand = Brand(
        id = "1",
        name = "Camilla and Marc",
        slug = "camilla-and-marc"
    ),
    defaultVariant = Variant(
        attributes = emptyList(),
        sku = "UNKNOWN",
        size = Size(
            id = "25927",
            value = "10 AU",
            scale = null,
            description = "10 AU",
            sizeGuide = null
        ),
        color = Color(
            id = "3406543",
            swatch = null,
            name = "steel",
            media = listOf(
                Media.Image(
                    alt = "patterson mini skirt",
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                ),
                Media.Image(
                    alt = "patterson mini skirt",
                    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                )
            ),
        ),
        media =
            Media.Image(
                alt = "patterson mini skirt",
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
        stock = 100,
        price = Price(
            amount = Money(
                currencyCode = "AUD",
                amount = 40000,
                amountFormatted = "$400.00"
            ),
            was = null
        )
    ),
    labels = emptyList(),
    longDescription = "Long description",
    name = "Seamless sculpt mid thigh short",
    priceRange = null,
    shortDescription = "Short description",
    slug = "camilla-and-marc-patterson-mini-skirt-26531650",
    styleNumber = "26531650",
    variants = listOf(
        Variant(
            attributes = listOf(
                Attribute(
                    key = "AttributeA",
                    value = "Attribute"
                )
            ),
            sku = "UNKNOWN",
            size = Size(
                id = "25927",
                value = "10 AU",
                scale = null,
                description = "10 AU",
                sizeGuide = null
            ),
            color = Color(
                id = "3406543",
                swatch = null,
                name = "steel",
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                ),
            ),
            media = Media.Image(
                alt = "patterson mini skirt",
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            stock = 100,
            price = Price(
                amount = Money(
                    currencyCode = "AUD",
                    amount = 40000,
                    amountFormatted = "$400.00"
                ),
                was = null
            )
        ),
        Variant(
            attributes = emptyList(),
            sku = "UNKNOWN",
            size = Size(
                id = "25936",
                value = "11 AU",
                scale = null,
                description = "11 AU",
                sizeGuide = null
            ),
            color = Color(
                id = "3406547",
                swatch = null,
                name = "steel",
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                ),
            ),
            media = Media.Image(
                alt = "patterson mini skirt",
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            stock = 0,
            price = Price(
                amount = Money(
                    currencyCode = "AUD",
                    amount = 40000,
                    amountFormatted = "$400.00"
                ),
                was = null
            )
        ),
        Variant(
            attributes = emptyList(),
            sku = "UNKNOWN",
            size = Size(
                id = "25937",
                value = "12 AU",
                scale = null,
                description = "12 AU",
                sizeGuide = null
            ),
            color = Color(
                id = "3406547",
                swatch = null,
                name = "steel",
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                ),
            ),
            media = Media.Image(
                alt = "patterson mini skirt",
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            stock = 100,
            price = Price(
                amount = Money(
                    currencyCode = "AUD",
                    amount = 40000,
                    amountFormatted = "$400.00"
                ),
                was = null
            )
        ),
        Variant(
            attributes = emptyList(),
            sku = "UNKNOWN",
            size = Size(
                id = "25974",
                value = "6 AU",
                scale = null,
                description = "6 AU",
                sizeGuide = null
            ),
            color = Color(
                id = "3406543",
                swatch = null,
                name = "steel",
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                ),
            ),
            media = Media.Image(
                alt = "patterson mini skirt",
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            stock = 100,
            price = Price(
                amount = Money(
                    currencyCode = "AUD",
                    amount = 40000,
                    amountFormatted = "$400.00"
                ),
                was = null
            )
        )
    ),
    colors = listOf(
        Color(
            id = "3406543",
            swatch = null,
            name = "steel",
            media = listOf(
                Media.Image(
                    alt = "patterson mini skirt",
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                ),
                Media.Image(
                    alt = "patterson mini skirt",
                    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                )
            ),
        ),
    )
)

internal const val BASE_URL = "https://www.alfie.com"

internal val productDetailsUI = ProductDetailsUI(
    id = "2666503",
    brand = "Camilla and Marc",
    name = "Seamless sculpt mid thigh short",
    slug = "camilla-and-marc-patterson-mini-skirt-26531650",
    shortDescription = "Short description",
    variants = persistentListOf(
        Variant(
            attributes = listOf(
                Attribute(
                    key = "AttributeA",
                    value = "Attribute"
                )
            ),
            color = Color(
                id = "3406543",
                name = "steel",
                swatch = null,
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                ),
            ),
            media = Media.Image(
                alt = "patterson mini skirt",
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            price = Price(
                amount = Money(
                    amount = 40000,
                    amountFormatted = "$400.00",
                    currencyCode = "AUD"
                ),
                was = null
            ),
            size = Size(
                id = "25927",
                description = "10 AU",
                scale = null,
                sizeGuide = null,
                value = "10 AU"
            ),
            sku = "UNKNOWN",
            stock = 100
        ),
        Variant(
            attributes = listOf(),
            color = Color(
                id = "3406547",
                name = "steel",
                swatch = null,
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                ),
            ),
            media = Media.Image(
                alt = "patterson mini skirt",
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            price = Price(
                amount = Money(
                    amount = 40000,
                    amountFormatted = "$400.00",
                    currencyCode = "AUD"
                ),
                was = null
            ),
            size = Size(
                id = "25936",
                value = "11 AU",
                scale = null,
                description = "11 AU",
                sizeGuide = null
            ),
            sku = "UNKNOWN",
            stock = 0
        ),
        Variant(
            attributes = listOf(),
            color = Color(
                id = "3406547",
                name = "steel",
                swatch = null,
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                ),
            ),
            media = Media.Image(
                alt = "patterson mini skirt",
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            price = Price(
                amount = Money(
                    amount = 40000,
                    amountFormatted = "$400.00",
                    currencyCode = "AUD"
                ),
                was = null
            ),
            size = Size(
                id = "25937",
                description = "12 AU",
                scale = null,
                sizeGuide = null,
                value = "12 AU"
            ),
            sku = "UNKNOWN",
            stock = 100
        ),
        Variant(
            attributes = listOf(),
            color = Color(
                id = "3406543",
                name = "steel",
                swatch = null,
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                ),
            ),
            media = Media.Image(
                    alt = "patterson mini skirt",
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            price = Price(
                amount = Money(
                    amount = 40000,
                    amountFormatted = "$400.00",
                    currencyCode = "AUD"
                ),
                was = null
            ),
            size = Size(
                id = "25974",
                description = "6 AU",
                scale = null,
                sizeGuide = null,
                value = "6 AU"
            ),
            sku = "UNKNOWN",
            stock = 100
        )
    ),
    information = persistentListOf(
        InformationUI.Description(
            tabItem = TabItem(
                StringResource.fromId(R.string.product_details_information_description)
            ),
            content = "Long description"
        )
    ),
    colors = persistentListOf(
        ColorUI(
            id = "3406543",
            type = SwatchType.PlainColor(color = AC.Black, isEnabled = true),
            index = 0
        ),
        ColorUI(
            id = "3406547",
            type = SwatchType.PlainColor(color = AC.Black, isEnabled = true),
            index = 1
        )
    ),
    isSelectionSoldOut = false,
    selectedColorUI = ColorUI(
        id = "3406543",
        type = SwatchType.PlainColor(color = AC.Black, isEnabled = true),
        index = 0
    ),
    sections = persistentListOf(
        ProductDetailsSectionItem(
            title = StringResource.fromId(R.string.product_details_section_delivery_and_returns),
            url = "$BASE_URL/${ProductDetailsUIFactory.DELIVERY_RETURNS_URL}"
        ),
        ProductDetailsSectionItem(
            title = StringResource.fromId(R.string.product_details_section_payment_options),
            url = "$BASE_URL/${ProductDetailsUIFactory.PAYMENT_OPTIONS_URL}"
        )
    ),
    shareInfo = ProductDetailsShareInfo(
        name = "Seamless sculpt mid thigh short",
        content = StringResource.fromId(
            id = R.string.product_details_share_text,
            args = listOf(
                "Camilla and Marc",
                "Seamless sculpt mid thigh short",
                "$400.00",
                "$BASE_URL/camilla-and-marc-patterson-mini-skirt-26531650"
            )
        )
    ),
    gallery = GalleryUI(
        medias = persistentListOf(
            ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Custom(
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
                    )
                ),
                alt = "patterson mini skirt"
            ),
            ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Custom(
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg",
                    )
                ),
                alt = "patterson mini skirt"
            )
        )
    ),
    sizeSectionUI = SizeSectionUI.SizeSelector(
        sizes = persistentListOf(
            SizeUI(
                id = "25927",
                properties = SizingButtonProperties(
                    text = "10 AU",
                    state = SizingButtonState.Selectable
                )
            ),
            SizeUI(
                id = "25974",
                properties = SizingButtonProperties(
                    text = "6 AU",
                    state = SizingButtonState.Selectable
                )
            )
        )
    )
)

internal val newColorSizeSectionUI = SizeSectionUI.SizeSelector(
    sizes = persistentListOf(
        SizeUI(
            id = "25936",
            properties = SizingButtonProperties(
                text = "11 AU",
                state = SizingButtonState.OutOfStock
            )
        ),
        SizeUI(
            id = "25937",
            properties = SizingButtonProperties(
                text = "12 AU",
                state = SizingButtonState.Selectable
            )
        )
    ),
    selectedSize = null
)

internal val sizeUI = SizeUI(
    id = "25927",
    properties = SizingButtonProperties(
        text = "10 AU",
        state = SizingButtonState.Selectable
    )
)

internal val selectedColorGalleryUI = GalleryUI(
    medias = persistentListOf(
        ImageUI(
            images = persistentListOf(
                ImageSizeUI.Custom(
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
                )
            ),
            alt = "patterson mini skirt"
        ),
        ImageUI(
            images = persistentListOf(
                ImageSizeUI.Custom(
                    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg",
                )
            ),
            alt = "patterson mini skirt"
        )
    )
)
