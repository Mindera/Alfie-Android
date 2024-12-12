package au.com.alfie.ecomm.debug.operational.view.screen

import android.util.DisplayMetrics
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.debug.operational.view.screen.model.DebugScreenEvent
import au.com.alfie.ecomm.debug.operational.view.screen.model.DebugScreenOpUI
import au.com.alfie.ecomm.designsystem.component.switch.Switch
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import au.com.alfie.ecomm.designsystem.R as RD

@RootNavGraph(start = true)
@Destination
@Composable
internal fun DebugScreenOp(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<DebugScreenOpViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    DebugScreenContent(
        state = state,
        onNavigate = navigator::navigate,
        onEvent = viewModel::handleEvents
    )
}

@Composable
private fun DebugScreenContent(
    state: List<DebugScreenOpUI>,
    onNavigate: ClickEventOneArg<Direction>,
    onEvent: ClickEventOneArg<DebugScreenEvent>
) {
    LazyColumn {
        state.forEach {
            when (it) {
                is DebugScreenOpUI.Header -> item { Header(title = it.title) }
                is DebugScreenOpUI.TextItem -> item {
                    TextItem(
                        title = it.title,
                        value = it.value
                    )
                }
                is DebugScreenOpUI.SwitchItem -> item {
                    SwitchItem(
                        title = it.title,
                        checked = it.checked,
                        onSwitch = it.onSwitch
                    )
                }
                is DebugScreenOpUI.NavigationItem -> item {
                    NavigationItem(title = it.title) {
                        onNavigate(it.direction)
                    }
                }
                is DebugScreenOpUI.EventItem -> item {
                    EventItem(
                        title = it.title,
                        icon = it.icon
                    ) {
                        onEvent(it.event)
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(title: StringResource) {
    Text(
        text = stringResource(title),
        style = Theme.typography.paragraphBold,
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.color.primary.mono200)
            .padding(horizontal = Theme.spacing.spacing12, vertical = Theme.spacing.spacing8)
    )
}

@Composable
private fun TextItem(
    title: StringResource,
    value: StringResource
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing12)
    ) {
        Text(
            text = stringResource(title),
            style = Theme.typography.paragraph
        )
        Text(
            text = stringResource(replaceDisplayMetrics(title = title, value = value)),
            style = Theme.typography.small
        )
    }
}

@Composable
private fun SwitchItem(
    title: StringResource,
    checked: Boolean,
    onSwitch: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.spacing12)
    ) {
        Text(
            text = stringResource(title),
            style = Theme.typography.small
        )
        Switch(checked = checked, onCheckChange = onSwitch)
    }
}

@Composable
private fun NavigationItem(
    title: StringResource,
    onNavigate: ClickEvent
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate() }
            .padding(Theme.spacing.spacing12)
    ) {
        Text(
            text = stringResource(title),
            style = Theme.typography.paragraph
        )
        Icon(
            painter = painterResource(id = RD.drawable.ic_action_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(Theme.iconSize.small)
        )
    }
}

@Composable
private fun EventItem(
    title: StringResource,
    @DrawableRes icon: Int,
    onEvent: ClickEvent
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEvent() }
            .padding(Theme.spacing.spacing12)
    ) {
        Text(
            text = stringResource(title),
            style = Theme.typography.paragraph
        )
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(Theme.iconSize.small)
        )
    }
}

@Composable
private fun replaceDisplayMetrics(
    title: StringResource,
    value: StringResource
): StringResource {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    return when (title) {
        StringResource.fromId(R.string.debug_screen_resolution_label) -> StringResource.fromText(
            "${displayMetrics.heightPixels}x${displayMetrics.widthPixels}"
        )
        StringResource.fromId(R.string.debug_screen_density_label) -> StringResource.fromText(
            "${displayMetrics.densityDpi}dpi (${displayMetrics.bucket})"
        )
        else -> value
    }
}

private val DisplayMetrics.bucket: String
    get() = when (densityDpi) {
        in 0..DisplayMetrics.DENSITY_LOW -> "ldpi"
        in DisplayMetrics.DENSITY_LOW..DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
        in DisplayMetrics.DENSITY_MEDIUM..DisplayMetrics.DENSITY_HIGH -> "hdpi"
        in DisplayMetrics.DENSITY_HIGH..DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
        in DisplayMetrics.DENSITY_XHIGH..DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
        in DisplayMetrics.DENSITY_XXHIGH..DisplayMetrics.DENSITY_XXXHIGH -> "xxxhdpi"
        else -> densityDpi.toString()
    }
