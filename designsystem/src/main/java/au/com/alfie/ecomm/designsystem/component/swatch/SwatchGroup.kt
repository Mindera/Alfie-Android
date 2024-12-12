package au.com.alfie.ecomm.designsystem.component.swatch

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.image.Image
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.designsystem.theme.shape.Shape.full
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

private val SWATCH_SHAPE = full
private const val NORMAL_ALPHA = 1F
private const val DISABLED_ALPHA = 0.5F

@ExperimentalGlideComposeApi
@Composable
fun SwatchGroup(
    selectedIndex: Int,
    swatchList: List<SwatchType>,
    swatchSize: SwatchSize,
    onClick: ClickEventOneArg<Int>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        swatchList.forEachIndexed { index, swatch ->
            Swatch(
                swatchType = swatch,
                swatchSize = swatchSize,
                isSelected = selectedIndex == index,
                onClick = { onClick(index) }
            )
        }
    }
}

@ExperimentalGlideComposeApi
@Composable
private fun Swatch(
    swatchType: SwatchType,
    swatchSize: SwatchSize,
    isSelected: Boolean,
    onClick: ClickEvent
) {
    val borderSize = swatchSize.borderWidth
    val alpha = if (swatchType.isEnabled) NORMAL_ALPHA else DISABLED_ALPHA
    val modifier = when {
        isSelected -> Modifier
            .size(swatchSize.externalSize)
            .border(
                width = borderSize,
                color = Theme.color.primary.mono900,
                shape = SWATCH_SHAPE
            )
            .padding(borderSize)
            .border(
                width = borderSize,
                color = Theme.color.white,
                shape = SWATCH_SHAPE
            )
            .alpha(alpha)

        swatchType.isEnabled.not() -> Modifier
            .size(swatchSize.internalSize)
            .alpha(DISABLED_ALPHA)

        else -> Modifier
            .size(swatchSize.internalSize)
    }

    Surface(
        modifier = Modifier
            .size(swatchSize.externalSize)
            .clip(SWATCH_SHAPE)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            SwatchContent(
                swatchType = swatchType,
                swatchSize = swatchSize
            )
        }
    }
}

@ExperimentalGlideComposeApi
@Composable
private fun SwatchContent(
    swatchType: SwatchType,
    swatchSize: SwatchSize
) {
    when (swatchType) {
        is SwatchType.PlainColor -> {
            Box(
                modifier = Modifier
                    .size(swatchSize.internalSize)
                    .clip(SWATCH_SHAPE)
                    .background(swatchType.color)
            )
        }

        is SwatchType.Image -> {
            Image(
                url = swatchType.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                ratio = Ratio.RATIO1x1,
                modifier = Modifier
                    .size(swatchSize.internalSize)
                    .clip(SWATCH_SHAPE)
            )
        }
    }
    if (swatchType.isEnabled.not()) {
        DrawDisabledLine(swatchSize = swatchSize)
    }
}

@Composable
private fun DrawDisabledLine(swatchSize: SwatchSize) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = 0f),
            color = Theme.color.white,
            strokeWidth = swatchSize.borderWidth.toPx()
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true)
@Composable
private fun SizingButtonsPreview() {
    Theme {
        val colorSwatchList = listOf(
            SwatchType.PlainColor(Color.Red),
            SwatchType.PlainColor(Color.Blue),
            SwatchType.PlainColor(Color.Gray),
            SwatchType.PlainColor(Color.Green),
            SwatchType.PlainColor(Color.Black, isEnabled = false)
        )
        SwatchGroup(
            selectedIndex = 0,
            swatchList = colorSwatchList,
            swatchSize = SwatchSize.Large,
            onClick = {}
        )
    }
}
