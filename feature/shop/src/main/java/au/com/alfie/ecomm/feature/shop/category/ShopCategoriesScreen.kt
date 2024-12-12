package au.com.alfie.ecomm.feature.shop.category

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEvent
import au.com.alfie.ecomm.feature.shop.category.model.CategoryUIState
import au.com.alfie.ecomm.feature.shop.ui.ShopErrorScreen
import au.com.alfie.ecomm.feature.uievent.UIEvent
import au.com.alfie.ecomm.feature.uievent.handleUIEvents

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
                text = stringResource(id = (state as CategoryUIState.Error).errorId)
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
