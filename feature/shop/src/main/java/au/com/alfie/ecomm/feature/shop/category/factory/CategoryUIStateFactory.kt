package au.com.alfie.ecomm.feature.shop.category.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI
import au.com.alfie.ecomm.feature.shop.category.model.CategoryUIState
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CategoryUIStateFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    companion object {
        private const val PLACEHOLDER_COUNT = 20
        val PLACEHOLDER = CategoryUIState.Data(
            title = StringResource.EMPTY,
            entries = List(PLACEHOLDER_COUNT) { index -> CategoryEntryUI.EMPTY.copy(id = index) }.toImmutableList(),
            isLoading = true
        )
    }

    suspend operator fun invoke(
        title: StringResource,
        navEntries: List<NavEntry>
    ): CategoryUIState.Data = withContext(dispatcher.default()) {
        CategoryUIState.Data(
            title = title,
            entries = navEntries.mapCategoryEntries().toImmutableList(),
            isLoading = false
        )
    }

    private fun List<NavEntry>.mapCategoryEntries(): List<CategoryEntryUI> = map { entry ->
        CategoryEntryUI(
            id = entry.id,
            title = StringResource.fromText(entry.title),
            path = entry.url.orEmpty()
        )
    }
}
