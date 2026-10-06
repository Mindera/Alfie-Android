package com.mindera.alfie.feature.mappers

import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.shared.model.Money
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MapPriceTest {

    private fun money(amount: Double, formatted: String) =
        Money(amount = amount, amountFormatted = formatted, currencyCode = "GBP")

    @Test
    fun `PriceRange toPriceType - WHEN both ends match THEN it is a single price, not a range`() {
        val range = PriceRange(low = money(34.90, "£34.90"), high = money(34.90, "£34.90"))

        assertEquals(PriceType.Default(price = "£34.90"), range.toPriceType(default = null))
    }

    @Test
    fun `PriceRange toPriceType - WHEN the ends differ THEN it is a range`() {
        val range = PriceRange(low = money(73.10, "£73.10"), high = money(93.74, "£93.74"))

        assertEquals(
            PriceType.Range(startPrice = "£73.10", endPrice = "£93.74"),
            range.toPriceType(default = null)
        )
    }

    @Test
    fun `PriceRange toPriceType - WHEN there is no upper bound THEN the default price is used`() {
        val range = PriceRange(low = money(20.00, "£20.00"), high = null)

        assertEquals(
            PriceType.Default(price = "£25.00"),
            range.toPriceType(default = Price(amount = money(25.00, "£25.00"), was = null))
        )
    }
}
