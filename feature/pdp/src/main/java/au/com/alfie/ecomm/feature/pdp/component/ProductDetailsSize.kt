package au.com.alfie.ecomm.feature.pdp.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.component.sizingbutton.INVALID_INDEX
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonGroup
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.pdp.R
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsEvent
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUIState
import au.com.alfie.ecomm.feature.pdp.model.SizeSectionUI
import au.com.alfie.ecomm.feature.pdp.model.SizeUI
import kotlinx.collections.immutable.toImmutableList
import au.com.alfie.ecomm.designsystem.R as RD

private val MIN_SIZE_MODAL_PICKER_BOX_HEIGHT = 44.dp

@Composable
internal fun ProductDetailsSize(
    state: ProductDetailsUIState.Data,
    onEvent: ClickEventOneArg<ProductDetailsEvent.OnSizeSelect>
) {
    val isLoading = state is ProductDetailsUIState.Data.Loading

    Spacer(modifier = Modifier.height(Theme.spacing.spacing24))
    if (isLoading) {
        LoadingPlaceholder()
    } else {
        when (val sizeSectionUI = state.details.sizeSectionUI) {
            SizeSectionUI.NoSize -> Unit
            SizeSectionUI.SingleSize -> SingleSize()
            is SizeSectionUI.SizeOnly -> SizeOnly(sizeOnly = sizeSectionUI)
            is SizeSectionUI.SizeModalPicker -> SizeModalPicker(sizeModalPicker = sizeSectionUI) { sizeUI ->
                onEvent(ProductDetailsEvent.OnSizeSelect(sizeUI))
            }
            is SizeSectionUI.SizeSelector -> SizeSelector(sizeSelector = sizeSectionUI) { sizeUI ->
                onEvent(ProductDetailsEvent.OnSizeSelect(sizeUI))
            }
        }
    }
}

@Composable
private fun SizeModalPicker(
    sizeModalPicker: SizeSectionUI.SizeModalPicker,
    onSizeSelected: ClickEventOneArg<SizeUI>
) {
    val title = sizeModalPicker.selectedSize?.properties?.text ?: stringResource(id = R.string.product_details_size_field_placeholder_text)
    var showModal by remember { mutableStateOf(false) }
    val color = if (sizeModalPicker.selectedSize != null) Theme.color.primary.mono900 else Theme.color.primary.mono500

    Row(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Theme.color.primary.mono100,
                shape = Theme.shape.extraSmall
            )
            .padding(horizontal = Theme.spacing.spacing16)
            .fillMaxWidth()
            .defaultMinSize(minHeight = MIN_SIZE_MODAL_PICKER_BOX_HEIGHT)
            .clickable { showModal = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(vertical = Theme.spacing.spacing12),
            text = title,
            style = Theme.typography.paragraph,
            color = color
        )

        Icon(
            painter = painterResource(id = RD.drawable.ic_action_chevron_down),
            modifier = Modifier.size(Theme.iconSize.small),
            tint = color,
            contentDescription = null
        )
    }

    if (showModal) {
        ProductDetailsSizeModalPicker(
            sizes = sizeModalPicker.sizes,
            selectedSize = sizeModalPicker.selectedSize,
            onSizeClick = { sizeUI ->
                onSizeSelected(sizeUI)
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
private fun SizeSelector(
    sizeSelector: SizeSectionUI.SizeSelector,
    onSizeSelected: ClickEventOneArg<SizeUI>
) {
    val sizes = sizeSelector.sizes
    val selectedIndex = sizes.indexOfFirst { it.id == sizeSelector.selectedSize?.id }

    SizingButtonGroup(
        options = sizeSelector.sizes.map { it.properties }.toImmutableList(),
        selectedIndex = selectedIndex,
        onSelectedOption = { index ->
            if (index != INVALID_INDEX) {
                val selectedSizeUI = sizeSelector.sizes[index]
                onSizeSelected(selectedSizeUI)
            }
        }
    )
}

@Composable
private fun SingleSize() {
    val text = getSizeText(customText = stringResource(id = R.string.product_details_one_size_label))

    Text(
        text = text,
        color = Theme.color.primary.mono900
    )
}

@Composable
private fun SizeOnly(sizeOnly: SizeSectionUI.SizeOnly) {
    val text = getSizeText(customText = sizeOnly.sizeUI.properties.text)

    Text(
        text = text,
        color = Theme.color.primary.mono900
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

@Composable
private fun getSizeText(customText: String): AnnotatedString {
    val styleBold = Theme.typography.paragraphBold
    val styleNormal = Theme.typography.paragraph
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = styleBold.fontSize,
                fontFamily = styleBold.fontFamily,
                fontWeight = styleBold.fontWeight
            )
        ) {
            append(stringResource(id = R.string.product_details_size_label))
        }
        append(" ")
        withStyle(
            style = SpanStyle(
                fontSize = styleNormal.fontSize,
                fontFamily = styleNormal.fontFamily,
                fontWeight = styleNormal.fontWeight
            )
        ) {
            append(customText)
        }
    }
}
