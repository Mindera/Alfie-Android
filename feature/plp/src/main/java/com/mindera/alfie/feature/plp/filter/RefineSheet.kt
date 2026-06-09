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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.commons.string.currencySymbol
import com.mindera.alfie.core.commons.string.formatMoney
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.modal.BottomSheet
import com.mindera.alfie.designsystem.component.radio.RadioButtonGroup
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.feature.plp.R as PlpR

/** Placeholder upper price cap; replace with BFF-supplied max when filter metadata is available. */
private const val MAX_PRICE_CAP = 10_000f
private const val DISABLED_ALPHA = 0.4f

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

    Column {
        FilterCategoryRow(
            label = stringResource(PlpR.string.refine_category_sort_by),
            value = sortLabel,
            onClick = { onNavigate(RefinePanel.SortBy) }
        )
        HorizontalDivider(color = Theme.color.primary.mono100)
        FilterCategoryRow(
            label = stringResource(PlpR.string.refine_category_price),
            value = priceLabel,
            onClick = { onNavigate(RefinePanel.PriceRange) }
        )
        HorizontalDivider(color = Theme.color.primary.mono100)

        // TODO: Enable these rows when the BFF returns available filter metadata
        DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_size))
        HorizontalDivider(color = Theme.color.primary.mono100)
        DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_colour))
        HorizontalDivider(color = Theme.color.primary.mono100)
        DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_materials))
        HorizontalDivider(color = Theme.color.primary.mono100)
        DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_brand))
        HorizontalDivider(color = Theme.color.primary.mono100)
        DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_style))
        HorizontalDivider(color = Theme.color.primary.mono100)
        DisabledFilterCategoryRow(label = stringResource(PlpR.string.refine_category_function))
        HorizontalDivider(color = Theme.color.primary.mono100)
    }
}

@Composable
private fun FilterCategoryRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Theme.spacing.spacing16, vertical = Theme.spacing.spacing16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono900,
            modifier = Modifier.weight(1f)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = Theme.typography.smallBold,
                color = Theme.color.primary.mono500
            )
            Spacer(modifier = Modifier.width(Theme.spacing.spacing8))
            Icon(
                painter = painterResource(R.drawable.ic_action_chevron_right),
                contentDescription = null,
                modifier = Modifier.size(Theme.iconSize.small),
                tint = Theme.color.primary.mono900
            )
        }
    }
}

@Composable
private fun DisabledFilterCategoryRow(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(DISABLED_ALPHA)
            .padding(horizontal = Theme.spacing.spacing16, vertical = Theme.spacing.spacing16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono900,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(R.drawable.ic_action_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(Theme.iconSize.small),
            tint = Theme.color.primary.mono900
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriceRangeContent(
    currentFilters: ProductListFilter?,
    onFiltersChange: (ProductListFilter?) -> Unit
) {
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
            .padding(horizontal = Theme.spacing.spacing16, vertical = Theme.spacing.spacing16)
    ) {
        RangeSlider(
            value = sliderRange,
            onValueChange = { range ->
                sliderRange = range
                minText = if (range.start > 0f) range.start.toInt().toString() else ""
                maxText = if (range.endInclusive < MAX_PRICE_CAP) range.endInclusive.toInt().toString() else ""
            },
            onValueChangeFinished = { emitChange(sliderRange.start, sliderRange.endInclusive) },
            valueRange = 0f..MAX_PRICE_CAP,
            colors = SliderDefaults.colors(
                thumbColor = Theme.color.primary.mono900,
                activeTrackColor = Theme.color.primary.mono900,
                inactiveTrackColor = Theme.color.primary.mono200
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

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
        PriceTextField(
            label = stringResource(PlpR.string.price_filter_min_label),
            value = minText,
            currencySymbol = currencySymbol,
            modifier = Modifier.weight(1f),
            onValueChange = { text ->
                val clamped = (text.toFloatOrNull() ?: 0f).coerceIn(0f, sliderRange.endInclusive)
                onMinChange(text, clamped..sliderRange.endInclusive)
            }
        )
        PriceTextField(
            label = stringResource(PlpR.string.price_filter_max_label),
            value = maxText,
            currencySymbol = currencySymbol,
            modifier = Modifier.weight(1f),
            onValueChange = { text ->
                val clamped = (text.toFloatOrNull() ?: MAX_PRICE_CAP).coerceIn(sliderRange.start, MAX_PRICE_CAP)
                onMaxChange(text, sliderRange.start..clamped)
            }
        )
    }
}

@Composable
private fun PriceTextField(
    label: String,
    value: String,
    currencySymbol: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = Theme.typography.small) },
        prefix = { Text(currencySymbol, style = Theme.typography.paragraph) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = Theme.typography.paragraph,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Theme.color.primary.mono900,
            unfocusedBorderColor = Theme.color.primary.mono200,
            focusedLabelColor = Theme.color.primary.mono900,
            unfocusedLabelColor = Theme.color.primary.mono500,
            cursorColor = Theme.color.primary.mono900
        ),
        modifier = modifier
    )
}

@Composable
private fun BottomActionBar(
    totalCount: Int,
    onRemoveAll: () -> Unit,
    onShowResults: () -> Unit
) {
    HorizontalDivider(color = Theme.color.primary.mono100)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.spacing16, vertical = Theme.spacing.spacing12),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            type = ButtonType.Underlined,
            text = stringResource(PlpR.string.refine_remove_all),
            buttonSize = ButtonSize.Medium,
            onClick = onRemoveAll,
            modifier = Modifier.weight(1f)
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
            modifier = Modifier.weight(2f)
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

// endregion
