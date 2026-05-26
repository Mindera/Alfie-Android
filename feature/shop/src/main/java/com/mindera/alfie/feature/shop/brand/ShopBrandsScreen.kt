package com.mindera.alfie.feature.shop.brand

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindera.alfie.core.commons.extension.nextFloat
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.extension.ItemWithUpdate
import com.mindera.alfie.designsystem.component.divider.DividerType
import com.mindera.alfie.designsystem.component.divider.HorizontalDivider
import com.mindera.alfie.designsystem.component.searchbar.SearchBarWithCancelButton
import com.mindera.alfie.designsystem.component.shimmer.shimmer
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.shop.R
import com.mindera.alfie.feature.shop.brand.model.BrandEntryUI
import com.mindera.alfie.feature.shop.brand.model.BrandEvent
import com.mindera.alfie.feature.shop.brand.model.BrandUIState
import com.mindera.alfie.feature.shop.ui.EntryHeadlineContent
import com.mindera.alfie.feature.shop.ui.ShopErrorScreen
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.handleUIEvents
import kotlin.random.Random

private const val EMPTY_CHARACTER: Char = ' '

@Composable
internal fun ShopBrandsScreen(
    onUiEvent: ClickEventOneArg<UIEvent>
) {
    val viewModel: BrandsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.handleUIEvents {
        onUiEvent(it)
    }

    when (state) {
        is BrandUIState.Data -> {
            ShopBrandsScreenContent(
                state = state as BrandUIState.Data,
                onEvent = viewModel::handleEvent
            )
        }
        is BrandUIState.Error -> {
            ShopErrorScreen(
                errorType = (state as BrandUIState.Error).errorType,
                onRetry = viewModel::retry
            )
        }
    }
}

@Composable
private fun ShopBrandsScreenContent(
    state: BrandUIState.Data,
    onEvent: ClickEventOneArg<BrandEvent>
) {
    Column {
        SearchBarWithCancelButton(
            modifier = Modifier.fillMaxWidth(),
            onTermChange = { onEvent(BrandEvent.OnBrandSearchEvent(searchTerm = it)) },
            placeholderText = stringResource(id = R.string.shop_search_brands),
            shouldCloseOnSearchAction = false
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            brandItems(
                state = state,
                onEntryClick = { onEvent(BrandEvent.OnBrandEntryClickEvent(it)) }
            )
        }
    }
}

private fun LazyListScope.brandItems(
    state: BrandUIState.Data,
    onEntryClick: ClickEventOneArg<BrandEntryUI.Entry>
) {
    itemsIndexed(
        items = state.entries,
        key = { _, entry -> entry.hashCode() }
    ) { _, entry ->
        when (entry) {
            is BrandEntryUI.Entry -> {
                Row(
                    modifier = Modifier
                        .heightIn(44.dp)
                        .clickable { onEntryClick(entry) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EntryHeadlineContent(
                        modifier = Modifier.padding(horizontal = Theme.spacing.spacing16),
                        text = entry.name,
                        isLoading = state.isLoading
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = Theme.spacing.spacing16),
                    dividerType = DividerType.Solid1Mono100
                )
            }
            is BrandEntryUI.Divider -> {
                AlphabeticalSectionHeader(headerCharacter = entry.character)
            }
        }
    }
}

@Composable
private fun LazyItemScope.AlphabeticalSectionHeader(headerCharacter: Char) {
    val isLoading = headerCharacter == EMPTY_CHARACTER
    val scale by remember { mutableFloatStateOf(Random.nextFloat(Theme.scale.scale40, Theme.scale.scale60)) }
    ItemWithUpdate(targetState = isLoading) { isPlaceholder ->
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Theme.spacing.spacing8,
                    horizontal = Theme.spacing.spacing16
                )
                .shimmer(
                    isShimmering = isPlaceholder,
                    xScale = scale
                ),
            text = headerCharacter.toString(),
            style = Theme.typography.paragraphLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
