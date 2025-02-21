package au.com.alfie.ecomm.designsystem.component.modal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.coroutines.launch

private const val MIN_HEIGHT_PERCENTAGE = 0.4F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isFullscreen: Boolean = true,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(LocalContext.current)
    val availableSize = remember {
        with(density) {
            Size(
                width = configuration.screenWidthDp.dp.toPx(),
                height = configuration.screenHeightDp.dp.toPx()
            )
        }
    }
    var minModalHeight by remember { mutableStateOf(Dp.Unspecified) }
    var maxModalHeight by remember { mutableStateOf(Dp.Unspecified) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                maxModalHeight = with(density) {
                    val screenHeight = windowMetrics.bounds.height()
                    minModalHeight = (screenHeight * MIN_HEIGHT_PERCENTAGE).toDp()

                    if (isFullscreen) {
                        val systemBottomBarHeight = screenHeight - it.boundsInWindow().bottom
                        val belowSystemTopBarHeight = availableSize.height + systemBottomBarHeight
                        belowSystemTopBarHeight
                    } else {
                        val belowToolBarHeight = screenHeight - it.boundsInWindow().top
                        belowToolBarHeight
                    }.toDp()
                }
            }
    )

    ModalBottomSheet(
        modifier = modifier.heightIn(
            min = minModalHeight,
            max = maxModalHeight
        ),
        sheetState = sheetState,
        shape = Theme.shape.medium.copy(
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        contentWindowInsets = { WindowInsets(0.dp) },
        onDismissRequest = { onDismiss() },
        dragHandle = { }
    ) {
        Column(Modifier.navigationBarsPadding()) {
            SheetTopBar(
                title = title,
                onDismiss = {
                    scope.launch { sheetState.hide() }
                    onDismiss()
                }
            )
            SheetContent(content)
        }
    }
}

@Composable
private fun SheetTopBar(
    title: String,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = Theme.spacing.spacing16,
                bottom = Theme.spacing.spacing16,
                start = Theme.spacing.spacing6
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            modifier = Modifier.size(Theme.iconSize.large),
            onClick = { onDismiss() }
        ) {
            Icon(
                modifier = Modifier.size(Theme.iconSize.large),
                painter = painterResource(id = R.drawable.ic_modal_action_close),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(Theme.spacing.spacing12))
        Text(
            text = title,
            style = Theme.typography.heading3,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SheetContent(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomBottomSheetPreview() {
    Theme {
        var showBottomSheet by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(Theme.spacing.spacing12),
                type = ButtonType.Primary,
                onClick = { showBottomSheet = true },
                text = "Show Modal",
                buttonSize = ButtonSize.Medium
            )
        }

        if (showBottomSheet) {
            BottomSheet(
                title = "Size Guide",
                onDismiss = { showBottomSheet = false }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = Theme.spacing.spacing16)
                ) {
                    items(30) {
                        Text("Item $it")
                    }
                }
            }
        }
    }
}
