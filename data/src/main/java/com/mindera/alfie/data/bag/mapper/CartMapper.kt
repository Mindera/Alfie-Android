package com.mindera.alfie.data.bag.mapper

import com.mindera.alfie.data.product.mapper.toDomain
import com.mindera.alfie.graphql.bff.fragment.CartFragment
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagLine

internal fun CartFragment.toDomain(): Bag = Bag(
    lines = lineItems.map { it.toDomain() },
    subtotal = totals.subtotal.moneyFragment.toDomain()
)

private fun CartFragment.LineItem.toDomain(): BagLine = BagLine(
    id = id,
    productId = productId,
    variantId = variantId,
    sku = sku,
    slug = slug,
    name = name,
    image = image?.imageFragment?.toDomain(),
    quantity = quantity,
    unitPrice = price.moneyFragment.toDomain(),
    lineTotal = lineTotal.moneyFragment.toDomain()
)
