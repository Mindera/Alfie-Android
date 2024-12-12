package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.component.segmented.SegmentedItem
import au.com.alfie.ecomm.designsystem.component.segmented.SegmentedPage
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import au.com.alfie.ecomm.designsystem.R as RD

private val backgroundColors = persistentListOf(
    Theme.color.secondary.green300,
    Theme.color.secondary.blue300,
    Theme.color.secondary.red300
)

@Destination
@Composable
fun SegmentedControlScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    val segments = persistentListOf(
        SegmentedItem(label = StringResource.fromText("Categories")),
        SegmentedItem(label = StringResource.fromText("Brands")),
        SegmentedItem(label = StringResource.fromText("Services"))
    )
    val iconSegments = segments.map { it.copy(icon = RD.drawable.ic_action_gift) }.toImmutableList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = Theme.spacing.spacing16)
    ) {
        SegmentedSection(
            title = "3 segments with standard padding",
            segments = segments,
            isCompact = true
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SegmentedSection(
            title = "2 segments with standard padding",
            segments = segments.subList(0, 2),
            isCompact = true
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SegmentedSection(
            title = "3 segments with expanded padding",
            segments = segments,
            isCompact = false
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SegmentedSection(
            title = "2 segments with expanded padding",
            segments = segments.subList(0, 2),
            isCompact = false
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SegmentedSection(
            title = "2 icon segments with standard padding",
            segments = iconSegments.subList(0, 2),
            isCompact = true
        )
    }
}

@Composable
private fun SegmentedSection(
    title: String,
    segments: ImmutableList<SegmentedItem>,
    isCompact: Boolean
) {
    var selectedSegment by remember { mutableIntStateOf(0) }

    Column {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = title,
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        SegmentedPage(
            segments = segments,
            selectedSegment = selectedSegment,
            onSegmentClick = { selectedSegment = it },
            isCompact = isCompact
        ) { page ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(150.dp)
                    .background(backgroundColors[page])
            ) {
                Text(text = stringResource(resource = segments[page].label))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SegmentedControlScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Segmented Screen"),
        showNavigationIcon = false
    )
    SegmentedControlScreen(topBarState = topBarState)
}
