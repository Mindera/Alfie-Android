package com.mindera.alfie.feature.bag

import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagContentUi
import com.mindera.alfie.feature.bag.models.BagItemNotice
import com.mindera.alfie.feature.bag.models.BagLineUi
import com.mindera.alfie.feature.bag.models.BagSummaryUi
import com.mindera.alfie.repository.bag.BagLine
import com.mindera.alfie.repository.shared.model.Money
import kotlinx.collections.immutable.persistentListOf

/*
 * Fixtures for this module's @Preview functions. They live in `main` because previews cannot see the
 * test source set, and they take no DI so a preview never needs a factory or a ViewModel.
 */

private val previewMoney = Money(amount = 0.0, amountFormatted = "", currencyCode = "GBP")

internal val loadingPlaceholderCard = ProductCardType.Horizontal(
    image = ImageUI(images = persistentListOf(), alt = null),
    brand = "",
    name = "",
    price = PriceType.Default(price = ""),
    color = "",
    size = ""
)

private fun previewCard(
    name: String,
    reference: String,
    color: String,
    price: String
) = ProductCardType.Horizontal(
    image = ImageUI(images = persistentListOf(ImageSizeUI.Large("url")), alt = name),
    brand = "Alfie",
    name = name,
    price = PriceType.Default(price = price),
    color = color,
    size = "S",
    reference = reference,
    onOverflowClick = {}
)

private fun previewLine(
    productId: String,
    card: ProductCardType.Horizontal,
    notice: BagItemNotice? = null
) = BagLineUi(
    bagLine = BagLine(
        id = "$productId-${card.reference}",
        productId = productId,
        variantId = card.reference,
        sku = card.reference,
        slug = productId,
        name = card.name,
        image = null,
        quantity = card.quantity,
        unitPrice = previewMoney,
        lineTotal = previewMoney
    ),
    notice = notice,
    productCardData = card
)

internal val previewBagContent = BagContentUi(
    lines = persistentListOf(
        previewLine(
            productId = "linen-shirt",
            card = previewCard(
                name = "100% Linen Fluid Shirt",
                reference = "0283/764",
                color = "Cream",
                price = "£82.00"
            ).copy(quantity = 2),
            // Two units against one in stock, so the preview shows the notice next to the quantity
            // it contradicts.
            notice = BagItemNotice.ExceedsStock(remaining = 1)
        ),
        previewLine(
            productId = "wide-leg-jeans",
            card = previewCard(
                name = "Mid-Rise Wide Leg Jeans",
                reference = "0273/234",
                color = "Navy Blue",
                price = "£96.00"
            ),
            notice = BagItemNotice.LowStock(remaining = 2)
        ),
        previewLine(
            productId = "cotton-tshirt",
            card = previewCard(
                name = "100% Cotton T-shirt",
                reference = "0187/978",
                color = "Off-White",
                price = "£34.00"
            ).copy(isAvailable = false),
            notice = BagItemNotice.Unavailable
        )
    ),
    summary = BagSummaryUi(totalFormatted = "£294.00")
)

internal val previewSingleItemBagContent = BagContentUi(
    lines = persistentListOf(
        previewLine(
            productId = "linen-shirt",
            card = previewCard(
                name = "100% Linen Fluid Shirt",
                reference = "0283/764",
                color = "Cream",
                price = "£82.00"
            )
        )
    ),
    summary = BagSummaryUi(totalFormatted = "£82.00")
)
