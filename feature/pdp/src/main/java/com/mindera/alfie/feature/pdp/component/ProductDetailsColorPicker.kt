package com.mindera.alfie.feature.pdp.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindera.alfie.core.commons.extension.orZero
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.shimmer.shimmer
import com.mindera.alfie.designsystem.component.swatch.SwatchGroup
import com.mindera.alfie.designsystem.component.swatch.SwatchSize
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.pdp.model.ProductDetailsEvent
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun ProductDetailsColorPicker(
    state: ProductDetailsUIState.Data,
    onColorClick: ClickEventOneArg<ProductDetailsEvent.OnColorClick>,
    modifier: Modifier = Modifier,
    swatchSize: SwatchSize = SwatchSize.Large
) {
    val isLoading = state is ProductDetailsUIState.Data.Loading
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(
                isShimmering = isLoading,
                xScale = Theme.scale.scale40
            )
    ) {
        SwatchGroup(
            selectedIndex = state.details.selectedColorUI?.index.orZero(), // TODO: review UX requirements regarding selection
            swatchList = state.details.colors.map { it.type },
            swatchSize = swatchSize,
            onClick = { position ->
                val event = ProductDetailsEvent.OnColorClick(position)
                onColorClick(event)
            }
        )
    }
}
