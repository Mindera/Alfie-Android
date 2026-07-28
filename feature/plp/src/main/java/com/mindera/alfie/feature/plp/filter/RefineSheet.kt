package com.mindera.alfie.feature.plp.filter

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.commons.string.currencySymbol
import com.mindera.alfie.core.commons.string.formatMoney
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.modal.BottomSheet
import com.mindera.alfie.designsystem.component.radio.RadioButtonGroup
import com.mindera.alfie.designsystem.component.slider.RangeSlider
import com.mindera.alfie.designsystem.component.slider.SliderInputField
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.feature.plp.R as PlpR

/** Placeholder upper price cap; replace with BFF-supplied max when filter metadata is available. */
private const val MAX_PRICE_CAP = 10_000f

/** Which sub-panel inside the Refine sheet is currently showing. */
private sealed interface RefinePanel {
    data object Main : RefinePanel
    data object SortBy : RefinePanel
    data object PriceRange : RefinePanel
}

/**
 * Full-screen BottomSheet for Refine (sort + filter).
 *
 * Pending state is managed locally: changes are only applied when the user taps
 * "Show X Results". Dismissing without tapping that button discards all changes.
 */
@Composable
internal fun RefineSheet(
    currentSort: ProductSortOption,
    currentFilters: ProductListFilter?,
    totalCount: Int,
    previewCount: Int?,
    onPreviewFilters: (ProductListFilter?) -> Unit,
    onApply: (sort: ProductSortOption, filters: ProductListFilter?) -> Unit,
    onDismiss: () -> Unit
) {
    var pendingSort by remember { mutableStateOf(currentSort) }
    var pendingFilters by remember { mutableStateOf(currentFilters) }
    var currentPanel by remember { mutableStateOf<RefinePanel>(RefinePanel.Main) }

    LaunchedEffect(pendingFilters) {
        onPreviewFilters(pendingFilters)
    }

    BackHandler(enabled = currentPanel != RefinePanel.Main) {
        currentPanel = RefinePanel.Main
    }

    val title = when (currentPanel) {
        RefinePanel.Main -> stringResource(PlpR.string.refine_title)
        RefinePanel.SortBy -> stringResource(PlpR.string.refine_category_sort_by)
        RefinePanel.PriceRange -> stringResource(PlpR.string.refine_category_price)
    }

    BottomSheet(
        title = title,
        onDismiss = onDismiss
    ) {
        Column {
            // Content — no weight needed; all panels have short content that fits without scrolling.
            // BottomSheet's content slot wraps in wrapContentHeight(), so weight is a no-op there.
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                when (currentPanel) {
                    RefinePanel.Main -> {
                        item { MainContent(pendingSort, pendingFilters) { currentPanel = it } }
                    }
                    RefinePanel.SortBy -> {
                        item {
                            SortByContent(
                                selectedSort = pendingSort,
                                onSortSelect = { pendingSort = it }
                            )
                        }
                    }
                    RefinePanel.PriceRange -> {
                        item {
                            PriceRangeContent(
                                currentFilters = pendingFilters,
                                onFiltersChange = { pendingFilters = it }
                            )
                        }
                    }
                }
            }

            // Sticky bottom action bar
            BottomActionBar(
                totalCount = previewCount ?: totalCount,
                onRemoveAll = {
                    pendingSort = ProductSortOption.RECOMMENDED
                    pendingFilters = null
                },
                onShowResults = { onApply(pendingSort, pendingFilters) }
            )
        }
    }
}

@Composable
private fun MainContent(
    pendingSort: ProductSortOption,
    pendingFilters: ProductListFilter?,
    onNavigate: (RefinePanel) -> Unit
) {
    val sortLabel = pendingSort.toLabel()
    val priceLabel = pendingFilters?.toPriceLabel() ?: stringResource(PlpR.string.price_filter_all_prices)

    // D10: Figma has no dividers between rows. The layout is two groups separated by a 24 dp gap —
    //  the standalone "Sort by" row, then a group holding the remaining seven at 0 dp spacing (their
    //  12 dp paddings already give 24 dp of air between labels).
    Column(verticalArrangement = Arrangement.spacedBy(LocalTheme.current.spacing.spacing24)) {
        FilterCategoryRow(
            label = stringResource(PlpR.string.refine_category_sort_by),
            value = sortLabel,
            onClick = { onNavigate(RefinePanel.SortBy) }
        )
        Column {
            FilterCategoryRow(
                label = stringResource(PlpR.string.refine_category_price),
                value = priceLabel,
                onClick = { onNavigate(RefinePanel.PriceRange) }
            )

            // TODO: ALFMOB-337 – Figma shows these rows enabled with their own sub-panels (colour
            //  swatches, checkbox lists, per-facet count badges). They stay unavailable until the BFF
            //  returns filter facet metadata; showing an enabled row that does nothing would be worse.
            DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_size))
            DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_colour))
            DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_materials))
            DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_brand))
            DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_style))
            DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_function))
        }
    }
}

@Composable
private fun FilterCategoryRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    val theme = LocalTheme.current
    // D-filter-row: DS `.filter-item` (3450:20328) — 12 dp vertical padding (48 dp row), value in
    //  body/medium + content/content-terciary, trailing Chevron Right at 24 dp.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = theme.spacing.spacing16, vertical = theme.spacing.spacing12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = theme.typography.body.medium,
            color = theme.color.content.contentPrimary,
            modifier = Modifier.weight(1f)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = theme.typography.body.medium,
                color = theme.color.content.contentTerciary
            )
            Spacer(modifier = Modifier.width(theme.spacing.spacing8))
            Icon(
                painter = painterResource(AlfieIcons.ChevronRight),
                contentDescription = null,
                modifier = Modifier.size(theme.sizing.icon.medium),
                tint = theme.color.content.contentPrimary
            )
        }
    }
}

@Composable
private fun DisabledFilterCategoryRow(label: String) {
    val theme = LocalTheme.current
    // D13: the DS has no disabled state for `.filter-item`, so unavailability is expressed with the
    //  standard disabled content token rather than a hardcoded alpha.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = theme.spacing.spacing16, vertical = theme.spacing.spacing12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = theme.typography.body.medium,
            color = theme.color.content.contentPrimaryDisabled,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(AlfieIcons.ChevronRight),
            contentDescription = null,
            modifier = Modifier.size(theme.sizing.icon.medium),
            tint = theme.color.content.contentPrimaryDisabled
        )
    }
}

@Composable
private fun SortByContent(
    selectedSort: ProductSortOption,
    onSortSelect: (ProductSortOption) -> Unit
) {
    val sortOptions = ProductSortOption.entries
    val labels = sortOptions.map { it.toLabel() }
    val selectedIndex = sortOptions.indexOf(selectedSort).coerceAtLeast(0)

    RadioButtonGroup(
        options = labels,
        optionSelected = selectedIndex,
        onSelectionChange = { index -> onSortSelect(sortOptions[index]) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Theme.spacing.spacing8)
    )
}

@Composable
private fun PriceRangeContent(
    currentFilters: ProductListFilter?,
    onFiltersChange: (ProductListFilter?) -> Unit
) {
    val theme = LocalTheme.current
    val currencySymbol = currencySymbol(currentFilters?.currencyCode ?: "USD")

    fun resolvedMin() = (currentFilters?.minPrice?.toFloat() ?: 0f).coerceIn(0f, MAX_PRICE_CAP)
    fun resolvedMax() = (currentFilters?.maxPrice?.toFloat() ?: MAX_PRICE_CAP).coerceIn(resolvedMin(), MAX_PRICE_CAP)

    var sliderRange by remember { mutableStateOf(resolvedMin()..resolvedMax()) }
    var minText by remember { mutableStateOf(if (resolvedMin() > 0f) resolvedMin().toInt().toString() else "") }
    var maxText by remember { mutableStateOf(if (resolvedMax() < MAX_PRICE_CAP) resolvedMax().toInt().toString() else "") }

    LaunchedEffect(currentFilters) {
        val min = resolvedMin()
        val max = resolvedMax()
        sliderRange = min..max
        minText = if (min > 0f) min.toInt().toString() else ""
        maxText = if (max < MAX_PRICE_CAP) max.toInt().toString() else ""
    }

    fun emitChange(min: Float, max: Float) {
        val minVal = min.toDouble().takeIf { it > 0 }
        val maxVal = max.toDouble().takeIf { it < MAX_PRICE_CAP }
        onFiltersChange(
            (currentFilters ?: ProductListFilter()).copy(minPrice = minVal, maxPrice = maxVal)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = theme.spacing.spacing16, vertical = theme.spacing.spacing16)
    ) {
        RangeSlider(
            value = sliderRange,
            onValueChange = { range ->
                sliderRange = range
                minText = if (range.start > 0f) range.start.toInt().toString() else ""
                maxText = if (range.endInclusive < MAX_PRICE_CAP) range.endInclusive.toInt().toString() else ""
            },
            onValueChangeFinished = { emitChange(sliderRange.start, sliderRange.endInclusive) },
            valueRange = 0f..MAX_PRICE_CAP
        )

        // DS gap between the rail and the inputs is spacing/spacing-xs.
        Spacer(modifier = Modifier.height(theme.spacing.spacing8))

        PriceRangeInputRow(
            minText = minText,
            maxText = maxText,
            currencySymbol = currencySymbol,
            sliderRange = sliderRange,
            onMinChange = { text, range ->
                minText = text
                sliderRange = range
                emitChange(range.start, range.endInclusive)
            },
            onMaxChange = { text, range ->
                maxText = text
                sliderRange = range
                emitChange(range.start, range.endInclusive)
            }
        )
    }
}

@Composable
private fun PriceRangeInputRow(
    minText: String,
    maxText: String,
    currencySymbol: String,
    sliderRange: ClosedFloatingPointRange<Float>,
    onMinChange: (text: String, range: ClosedFloatingPointRange<Float>) -> Unit,
    onMaxChange: (text: String, range: ClosedFloatingPointRange<Float>) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)
    ) {
        SliderInputField(
            value = minText,
            prefix = currencySymbol,
            modifier = Modifier.weight(1f),
            onValueChange = { text ->
                val clamped = (text.toFloatOrNull() ?: 0f).coerceIn(0f, sliderRange.endInclusive)
                onMinChange(text, clamped..sliderRange.endInclusive)
            }
        )
        SliderInputField(
            value = maxText,
            prefix = currencySymbol,
            modifier = Modifier.weight(1f),
            onValueChange = { text ->
                val clamped = (text.toFloatOrNull() ?: MAX_PRICE_CAP).coerceIn(sliderRange.start, MAX_PRICE_CAP)
                onMaxChange(text, sliderRange.start..clamped)
            }
        )
    }
}

@Composable
private fun BottomActionBar(
    totalCount: Int,
    onRemoveAll: () -> Unit,
    onShowResults: () -> Unit
) {
    val theme = LocalTheme.current
    // D11: Figma stacks the two actions vertically at full width with a 16 dp gap and no divider
    //  above them (node 1:3221). "Remove All" is the Secondary (outlined) button, not Underlined.
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = theme.spacing.spacing16, vertical = theme.spacing.spacing16),
        verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing16)
    ) {
        Button(
            type = ButtonType.Secondary,
            text = stringResource(PlpR.string.refine_remove_all),
            buttonSize = ButtonSize.Medium,
            onClick = onRemoveAll,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            type = ButtonType.Primary,
            text = if (totalCount > 0) {
                stringResource(PlpR.string.refine_show_results, totalCount)
            } else {
                stringResource(PlpR.string.refine_show_results_loading)
            },
            buttonSize = ButtonSize.Medium,
            onClick = onShowResults,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ProductSortOption.toLabel(): String = when (this) {
    ProductSortOption.RECOMMENDED -> stringResource(PlpR.string.sort_option_recommended)
    ProductSortOption.MOST_RECENT -> stringResource(PlpR.string.sort_option_most_recent)
    ProductSortOption.LOWEST_PRICE -> stringResource(PlpR.string.sort_option_lowest_price)
    ProductSortOption.HIGHEST_PRICE -> stringResource(PlpR.string.sort_option_highest_price)
}

@Composable
private fun ProductListFilter.toPriceLabel(): String? {
    val hasMin = minPrice != null
    val hasMax = maxPrice != null
    return when {
        hasMin && hasMax -> stringResource(
            PlpR.string.price_filter_range,
            formatMoney(minPrice!!, currencyCode, showFractionDigits = false),
            formatMoney(maxPrice!!, currencyCode, showFractionDigits = false)
        )
        hasMin -> stringResource(
            PlpR.string.price_filter_range,
            formatMoney(minPrice!!, currencyCode, showFractionDigits = false),
            "∞"
        )
        hasMax -> stringResource(
            PlpR.string.price_filter_range,
            formatMoney(0.0, currencyCode, showFractionDigits = false),
            formatMoney(maxPrice!!, currencyCode, showFractionDigits = false)
        )
        else -> null
    }
}

// region Previews

@Preview(showBackground = true)
@Composable
private fun RefineSheetMainPreview() {
    Theme {
        RefineSheet(
            currentSort = ProductSortOption.RECOMMENDED,
            currentFilters = null,
            totalCount = 283,
            previewCount = null,
            onPreviewFilters = {},
            onApply = { _, _ -> },
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RefineSheetWithFiltersPreview() {
    Theme {
        RefineSheet(
            currentSort = ProductSortOption.LOWEST_PRICE,
            currentFilters = ProductListFilter(minPrice = 50.0, maxPrice = 500.0),
            totalCount = 128,
            previewCount = null,
            onPreviewFilters = {},
            onApply = { _, _ -> },
            onDismiss = {}
        )
    }
}

/**
 * The sub-panels are previewed directly rather than through [RefineSheet], whose panel is private
 * local state that a preview cannot drive.
 */
@Preview(showBackground = true)
@Composable
private fun RefineSheetSortByPreview() {
    Theme {
        SortByContent(
            selectedSort = ProductSortOption.MOST_RECENT,
            onSortSelect = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RefineSheetPriceRangePreview() {
    Theme {
        PriceRangeContent(
            currentFilters = ProductListFilter(minPrice = 40.0, maxPrice = 120.0),
            onFiltersChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RefineSheetPriceRangeEmptyPreview() {
    Theme {
        PriceRangeContent(
            currentFilters = null,
            onFiltersChange = {}
        )
    }
}

// endregion
