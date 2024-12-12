package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

private val rectangleShapes = listOf(
    Theme.shape.none to "None",
    Theme.shape.extraSmall to "XS",
    Theme.shape.small to "S",
    Theme.shape.medium to "M",
    Theme.shape.large to "L",
    Theme.shape.extraLarge to "XL",
    Theme.shape.full to "Full"
)

private const val COLUMN_COUNT = 3

@Destination
@Composable
fun ShapeScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing16)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Rectangles",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        LazyVerticalGrid(columns = GridCells.Fixed(COLUMN_COUNT)) {
            items(rectangleShapes) { shape ->
                ShapeItem(
                    modifier = Modifier.padding(Theme.spacing.spacing8),
                    shape = shape.first,
                    text = shape.second
                )
            }
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Nested Corners",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OuterBorder {
                ShapeItem(
                    modifier = Modifier.padding(horizontal = Theme.spacing.spacing8),
                    shape = Theme.shape.small,
                    text = "S"
                )
            }

            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

            OuterBorder {
                ShapeItem(
                    modifier = Modifier.padding(horizontal = Theme.spacing.spacing8),
                    shape = Theme.shape.medium,
                    text = "M"
                )
            }
        }
    }
}

@Composable
private fun ShapeItem(
    shape: RoundedCornerShape,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Theme.color.white,
                contentColor = Theme.color.black
            ),
            border = BorderStroke(
                width = 2.dp,
                color = Theme.color.black
            ),
            shape = shape,
            onClick = {}
        ) {
            Text(
                text = text,
                style = Theme.typography.small,
                modifier = Modifier.padding(Theme.spacing.spacing8)
            )
        }
    }
    Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
}

@Composable
private fun OuterBorder(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .clip(Theme.shape.medium)
            .background(Theme.color.black)
            .size(width = 128.dp, height = 76.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(Theme.shape.medium)
                .background(Theme.color.white)
                .size(width = 122.dp, height = 70.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShapeScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Shape Screen"),
        showNavigationIcon = false
    )
    ShapeScreen(topBarState = topBarState)
}
