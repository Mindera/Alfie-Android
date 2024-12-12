package au.com.alfie.ecomm.feature.pdp.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import au.com.alfie.ecomm.core.commons.extension.orZero
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchGroup
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchSize
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsEvent
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUIState
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
