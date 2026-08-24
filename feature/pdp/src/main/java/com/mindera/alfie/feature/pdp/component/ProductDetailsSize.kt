package com.mindera.alfie.feature.pdp.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.shimmer.shimmer
import com.mindera.alfie.designsystem.component.sizingbutton.INVALID_INDEX
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonGroup
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.pdp.R
import com.mindera.alfie.feature.pdp.model.ProductDetailsEvent
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState
import com.mindera.alfie.feature.pdp.model.SizeSectionUI
import com.mindera.alfie.feature.pdp.model.SizeUI
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ProductDetailsSize(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent.OnSizeSelect>
) {
    val isLoading = state is ProductDetailsUIState.Data.Loading

    if (isLoading) {
        LoadingPlaceholder()
    } else {
        when (val sizeSectionUI = state.details.sizeSectionUI) {
            SizeSectionUI.NoSize -> Unit
            SizeSectionUI.SingleSize -> SingleSize(customText = stringResource(id = R.string.product_details_one_size_label))
            is SizeSectionUI.SizeOnly -> SingleSize(customText = sizeSectionUI.sizeUI.properties.text)
            is SizeSectionUI.SizeSelector -> SizeSelector(sizeSelector = sizeSectionUI) { sizeUI ->
                onEvent(ProductDetailsEvent.OnSizeSelect(sizeUI))
            }
        }
    }
}

@Composable
private fun SizeSelector(
    sizeSelector: SizeSectionUI.SizeSelector,
    onSizeSelected: ClickEventOneArg<SizeUI>
) {
    SizeSectionHeader()
    Spacer(modifier = Modifier.height(Theme.spacing.spacing8))

    val sizes = sizeSelector.sizes
    val selectedIndex = sizes.indexOfFirst { it.id == sizeSelector.selectedSize?.id }

    SizingButtonGroup(
        options = sizes.map { it.properties }.toImmutableList(),
        selectedIndex = selectedIndex,
        onSelectedOption = { index ->
            if (index != INVALID_INDEX) {
                val selectedSizeUI = sizes[index]
                onSizeSelected(selectedSizeUI)
            }
        }
    )
}

// Section Heading (screens file): "Select Your Size" in heading/x-small on the leading edge,
// the Size Guide link trailing — body/medium-bold with a 1px underline in the same #111 as
// its text. Inert by design: the destination is still to be agreed (iOS ships it inert too).
@Composable
private fun SizeSectionHeader() {
    val c = LocalTheme.current.primitive.colors

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.product_details_select_your_size),
            style = LocalTheme.current.typography.heading.xSmall,
            color = c.neutrals800,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(id = R.string.product_details_size_guide),
            style = LocalTheme.current.typography.body.mediumBold,
            color = c.neutrals800,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
private fun SingleSize(customText: String) {
    val c = LocalTheme.current.primitive.colors

    Text(
        text = stringResource(id = R.string.product_details_single_size, customText),
        style = LocalTheme.current.typography.body.medium,
        color = c.neutrals800
    )
}

@Composable
private fun LoadingPlaceholder() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(
                isShimmering = true,
                xScale = Theme.scale.scale70
            ),
        text = ""
    )
}
