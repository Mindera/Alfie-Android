package com.mindera.alfie.data.productlist.mapper

import com.mindera.alfie.graphql.bff.ProductListQuery
import com.mindera.alfie.graphql.bff.fragment.ProductListEntryFragment
import com.mindera.alfie.repository.productlist.model.CursorPagination
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListPriceRange
import com.mindera.alfie.repository.shared.model.Media
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

internal fun ProductListQuery.ProductList.toDomain() = ProductList(
    products = products.map { it.productListEntryFragment.toDomain() },
    pagination = CursorPagination(
        endCursor = pageInfo?.endCursor,
        hasNextPage = pageInfo?.hasNextPage ?: false,
        totalCount = totalCount ?: 0
    )
)

private fun ProductListEntryFragment.toDomain() = ProductListEntry(
    id = id,
    slug = slug,
    name = name,
    brandName = brandName,
    productType = productType,
    primaryImage = primaryImage?.imageFragment?.let { Media.Image(alt = it.altText, url = it.url) },
    priceRange = priceRange.toDomain(),
    inventoryTotal = inventoryTotal,
    tags = tags.orEmpty().filterNotNull()
)

private fun ProductListEntryFragment.PriceRange.toDomain() = ProductListPriceRange(
    minAmount = minVariantPrice.moneyFragment.amount,
    maxAmount = maxVariantPrice.moneyFragment.amount,
    currencyCode = minVariantPrice.moneyFragment.currencyCode
)

internal fun ProductListPriceRange.formatMin(): String = formatAsMoney(minAmount, currencyCode)

internal fun ProductListPriceRange.formatMax(): String = formatAsMoney(maxAmount, currencyCode)

private fun formatAsMoney(amount: Double, currencyCode: String): String = runCatching {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.currency = Currency.getInstance(currencyCode)
    format.format(amount)
}.getOrElse { "%.2f".format(amount) }
