package au.com.alfie.ecomm.designsystem.component.segmented

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.animation.standard
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SegmentedPage(
    segments: ImmutableList<SegmentedItem>,
    selectedSegment: Int,
    onSegmentClick: ClickEventOneArg<Int>,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true,
    content: @Composable (Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        SegmentedControl(
            segments = segments,
            selectedSegment = selectedSegment,
            onSegmentClick = onSegmentClick,
            isCompact = isCompact,
            modifier = Modifier.padding(Theme.spacing.spacing16)
        )

        AnimatedContent(
            targetState = selectedSegment,
            transitionSpec = { fadeIn(standard()) togetherWith fadeOut(standard()) },
            modifier = Modifier.fillMaxSize(),
            label = "SegmentedPageContent"
        ) { page ->
            content(page)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SegmentedPagePreview() {
    Theme {
        var selectedSegment by remember { mutableIntStateOf(0) }
        val items = persistentListOf(
            SegmentedItem(StringResource.fromText("Categories")),
            SegmentedItem(StringResource.fromText("Brands")),
            SegmentedItem(StringResource.fromText("Services"))
        )

        SegmentedPage(
            segments = items,
            selectedSegment = selectedSegment,
            onSegmentClick = { selectedSegment = it }
        ) { page ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = stringResource(resource = items[page].label))
            }
        }
    }
}
