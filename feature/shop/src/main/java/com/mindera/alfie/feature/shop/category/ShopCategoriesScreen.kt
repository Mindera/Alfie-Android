package com.mindera.alfie.feature.shop.category

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.feature.shop.category.model.CategoryEvent
import com.mindera.alfie.feature.shop.category.model.CategoryUIState
import com.mindera.alfie.feature.shop.ui.ShopErrorScreen
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.handleUIEvents

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
