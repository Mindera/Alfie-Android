package com.mindera.alfie.feature.shop.category

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.shop.R
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.shop.category.model.CategoryEvent
import com.mindera.alfie.feature.shop.category.model.CategoryUIState
import com.mindera.alfie.feature.shop.ui.ShopErrorScreen
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.handleUIEvents
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ShopCategoriesScreen(
    onUiEvent: ClickEventOneArg<UIEvent>
) {
    val viewModel: CategoryViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.handleUIEvents {
        onUiEvent(it)
    }

    when (state) {
        is CategoryUIState.Data -> {
            ShopCategoriesScreenContent(
                categoryUiState = state as CategoryUIState.Data,
                onEvent = viewModel::handleEvent
            )
        }
        is CategoryUIState.Error -> {
            ShopErrorScreen(
                errorType = (state as CategoryUIState.Error).errorType,
                customGenericError = R.string.shop_error_cannot_load_categories_list,
                onRetry = viewModel::retry
            )
        }
    }
}

@Composable
private fun ShopCategoriesScreenContent(
    categoryUiState: CategoryUIState.Data,
    onEvent: ClickEventOneArg<CategoryEvent>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        categoryItems(
            entries = categoryUiState.entries,
            isPlaceholder = categoryUiState.isLoading,
            onEntryClick = { onEvent(CategoryEvent.OnEntryClickEvent(it)) }
        )
    }
}

// region Previews

private val previewEntries = persistentListOf(
    CategoryEntryUI(id = 1, title = StringResource.fromText("New in"), path = "new-in"),
    CategoryEntryUI(id = 2, title = StringResource.fromText("Women"), path = "women", hasChildren = true),
    CategoryEntryUI(id = 3, title = StringResource.fromText("Men"), path = "men", hasChildren = true),
    CategoryEntryUI(id = 4, title = StringResource.fromText("Kids"), path = "kids", hasChildren = true),
    CategoryEntryUI(id = 5, title = StringResource.fromText("Cosmetics"), path = "cosmetics", hasChildren = true)
)

@Preview(showBackground = true)
@Composable
private fun ShopCategoriesLoadedPreview() {
    Theme {
        ShopCategoriesScreenContent(
            categoryUiState = CategoryUIState.Data(
                title = StringResource.EMPTY,
                entries = previewEntries,
                isLoading = false
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShopCategoriesLoadingPreview() {
    Theme {
        ShopCategoriesScreenContent(
            categoryUiState = CategoryUIState.Data(
                title = StringResource.EMPTY,
                // Placeholders have no children, so they shimmer without chevrons.
                entries = previewEntries.map { it.copy(hasChildren = false) }.toImmutableList(),
                isLoading = true
            ),
            onEvent = {}
        )
    }
}

// endregion
