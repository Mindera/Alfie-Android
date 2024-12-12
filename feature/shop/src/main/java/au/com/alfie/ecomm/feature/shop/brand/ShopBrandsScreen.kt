package au.com.alfie.ecomm.feature.shop.brand

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
import au.com.alfie.ecomm.core.commons.extension.nextFloat
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.extension.ItemWithUpdate
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.component.searchbar.SearchBarWithCancelButton
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.shop.R
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEvent
import au.com.alfie.ecomm.feature.shop.brand.model.BrandUIState
import au.com.alfie.ecomm.feature.shop.ui.EntryHeadlineContent
import au.com.alfie.ecomm.feature.shop.ui.ShopErrorScreen
import au.com.alfie.ecomm.feature.uievent.UIEvent
import au.com.alfie.ecomm.feature.uievent.handleUIEvents
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
                text = stringResource(id = (state as BrandUIState.Error).errorId)
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
