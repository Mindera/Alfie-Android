package au.com.alfie.ecomm.designsystem.component.overlay

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun OverlayLayout(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    overlayContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    properties: OverlayProperties = OverlayProperties(),
    alignment: Alignment = Alignment.TopCenter
) {
    BackHandler(
        enabled = isOpen && properties.dismissOnBackPress,
        onBack = onDismiss
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        content()
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Scrim(
                    onDismiss = onDismiss,
                    dismissOnClick = properties.dismissOnClickOutside
                )
                Surface(modifier = modifier.align(alignment)) {
                    overlayContent()
                }
            }
        }
    }
}

@Composable
private fun Scrim(
    onDismiss: () -> Unit,
    dismissOnClick: Boolean,
    color: Color = Theme.color.black.copy(alpha = .5f)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .clickable(
                enabled = dismissOnClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            )
    )
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun OverlayLayoutPreview() {
    var isOpen by remember { mutableStateOf(true) }
    OverlayLayout(
        isOpen = isOpen,
        onDismiss = { isOpen = false },
        alignment = Alignment.BottomCenter,
        overlayContent = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Lorem ipsum dolor sit amet", modifier = Modifier.padding(Theme.spacing.spacing40))
            }
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Button(onClick = { isOpen = true }) {
                    Text(text = "Open overlay")
                }
            }
        }
    )
}
