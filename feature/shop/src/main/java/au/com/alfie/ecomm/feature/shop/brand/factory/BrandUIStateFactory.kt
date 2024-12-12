package au.com.alfie.ecomm.feature.shop.brand.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.brand.model.BrandUIState
import au.com.alfie.ecomm.repository.shared.model.Brand
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class BrandUIStateFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    companion object {
        private const val EMPTY_CHARACTER: Char = ' '
        private const val PLACEHOLDER_COUNT = 20
        val PLACEHOLDER = BrandUIState.Data(
            entries = List(PLACEHOLDER_COUNT) { index -> BrandEntryUI.Entry.EMPTY.copy(id = index.toString()) }.toImmutableList(),
            isLoading = true
        )
    }

    suspend operator fun invoke(brands: List<Brand>): BrandUIState.Data = withContext(dispatcher.io()) {
        BrandUIState.Data(
            entries = brands.mapBrands().toImmutableList(),
            isLoading = false
        )
    }

    suspend fun filterBySearchTerm(
        entries: ImmutableList<BrandEntryUI>,
        searchTerm: String
    ): BrandUIState.Data = withContext(dispatcher.io()) {
        val filteredBrands = if (searchTerm.isNotEmpty()) {
            val filteredBrands = entries
                .filterIsInstance<BrandEntryUI.Entry>()
                .filter { it.name.contains(searchTerm, ignoreCase = true) }

            val groupedBrands = filteredBrands.groupBy { it.name.firstOrNull()?.uppercase() ?: "" }
            groupedBrands.flatMap { (initial, entries) ->
                listOfNotNull(
                    initial.takeIf { it.isNotEmpty() }?.let { BrandEntryUI.Divider(it.first()) },
                    *entries.toTypedArray()
                )
            }.toImmutableList()
        } else {
            entries
        }

        return@withContext BrandUIState.Data(
            entries = filteredBrands,
            isLoading = false
        )
    }

    private fun List<Brand>.mapBrands(): List<BrandEntryUI> {
        val groupedBrands = groupBy { it.name.firstOrNull()?.uppercaseChar() ?: EMPTY_CHARACTER }
        return buildList {
            groupedBrands.forEach { (initialChar, entries) ->
                add(BrandEntryUI.Divider(character = initialChar))
                addAll(
                    entries.map { brand ->
                        BrandEntryUI.Entry(
                            id = brand.id,
                            name = brand.name,
                            slug = brand.slug
                        )
                    }
                )
            }
        }
    }
}
