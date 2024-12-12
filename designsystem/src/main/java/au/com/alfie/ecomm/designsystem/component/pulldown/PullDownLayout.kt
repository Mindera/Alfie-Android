package au.com.alfie.ecomm.designsystem.component.pulldown

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.roundToInt

private const val MAX_HEIGHT = 100
private const val PULL_TO_REFRESH_PROGRESS_MULTIPLIER = 100

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullDownLayout(
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var pullDownIndicatorState by remember { mutableStateOf(PullDownIndicatorState.Default) }

    val pullToRefreshState = rememberPullRefreshState(
        refreshing = pullDownIndicatorState == PullDownIndicatorState.Refreshing,
        refreshThreshold = 120.dp,
        onRefresh = {
            onRefresh()

            pullDownIndicatorState = PullDownIndicatorState.Default
        }
    )

    LaunchedEffect(Unit) {
        snapshotFlow { pullToRefreshState.progress }
            .distinctUntilChanged()
            .collectLatest { progress ->
                when {
                    progress >= 1 -> {
                        pullDownIndicatorState = PullDownIndicatorState.ReachedThreshold
                    }

                    progress > 0 -> {
                        pullDownIndicatorState = PullDownIndicatorState.PullingDown
                    }
                }
            }
    }

    val heightModifier = when (pullDownIndicatorState) {
        PullDownIndicatorState.PullingDown -> {
            Modifier.height(
                (pullToRefreshState.progress * PULL_TO_REFRESH_PROGRESS_MULTIPLIER)
                    .roundToInt()
                    .coerceAtMost(MAX_HEIGHT).dp
            )
        }

        PullDownIndicatorState.ReachedThreshold -> Modifier.height(MAX_HEIGHT.dp)
        PullDownIndicatorState.Refreshing -> Modifier.wrapContentHeight()
        PullDownIndicatorState.Default -> Modifier.height(0.dp)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .animateContentSize()
            .pullRefresh(pullToRefreshState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .then(heightModifier)
                .padding(15.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}
