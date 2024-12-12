package au.com.alfie.ecomm.designsystem.component.fullscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.animation.standard
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun Fullscreen(
    isOpen: Boolean,
    onDismissFullscreen: ClickEvent,
    content: @Composable () -> Unit
) {
    var isReadyToClose by remember { mutableStateOf(isOpen.not()) }

    val alphaState by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = standard(),
        label = "fullscreen alpha"
    ) {
        isReadyToClose = it == 0f
    }

    if (isReadyToClose && isOpen.not()) return

    Popup(
        onDismissRequest = onDismissFullscreen,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = alphaState }
        ) {
            Box {
                content()
                CloseButton(onDismissFullscreen)
            }
        }
    }
}

@Composable
private fun CloseButton(onDismissFullscreen: ClickEvent) {
    IconButton(
        modifier = Modifier.padding(
            top = Theme.spacing.spacing12,
            end = Theme.spacing.spacing16,
            bottom = Theme.spacing.spacing12,
            start = Theme.spacing.spacing8
        ),
        onClick = onDismissFullscreen,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = Theme.alpha.alpha70),
            contentColor = Theme.color.primary.mono900
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_action_close_dark),
            contentDescription = null,
            modifier = Modifier.size(Theme.iconSize.medium)
        )
    }
}
