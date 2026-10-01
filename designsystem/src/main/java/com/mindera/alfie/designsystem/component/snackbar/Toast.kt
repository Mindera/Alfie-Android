package com.mindera.alfie.designsystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

/**
 * The DS Toast: a full-width, square-cornered bar docked on top of the bottom navigation, with an
 * optional underlined link. Unlike [Snackbar] it has no dismiss button and no icon, and it is not
 * inset from the screen edges — the host draws it without the snackbar's margins.
 */
@Composable
internal fun Toast(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val theme = LocalTheme.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing24),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(theme.color.content.contentPrimary)
            .padding(
                horizontal = theme.spacing.spacing16,
                vertical = theme.spacing.spacing8
            )
    ) {
        Text(
            text = message,
            style = theme.typography.body.medium,
            color = theme.color.content.contentInvertedPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        actionLabel?.let {
            Text(
                text = it,
                style = theme.typography.link.medium.copy(textDecoration = TextDecoration.Underline),
                color = theme.color.link.linkPrimaryInvertedDefault,
                modifier = Modifier.clickable {
                    onActionClick()
                    onDismiss()
                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
@Composable
private fun ToastPreview() {
    Theme {
        Toast(
            message = "Removed.",
            actionLabel = "Undo"
        )
    }
}
