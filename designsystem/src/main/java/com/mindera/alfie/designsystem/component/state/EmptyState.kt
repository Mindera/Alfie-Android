package com.mindera.alfie.designsystem.component.state

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

/**
 * Full-area icon + message empty state — the modern Figma "Empty States" pattern, measured from the
 * empty Bag frame (node `673:90050`): a `sizing/icon/medium` glyph, `spacing/spacing-md` gap and an
 * unemphasised `body/medium` message, centred on both axes.
 *
 * Distinct from [StateMessage], which leads with a bold title and carries a subtitle and an action.
 * Use this where the design shows an icon and a single line of copy.
 */
@Composable
fun EmptyState(
    message: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = theme.spacing.spacing16,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = theme.spacing.spacing32,
                vertical = theme.spacing.spacing8
            )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = theme.color.content.contentPrimary,
            modifier = Modifier.size(theme.sizing.icon.medium)
        )
        Text(
            text = message,
            style = theme.typography.body.medium,
            color = theme.color.content.contentPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun EmptyStatePreview() {
    Theme {
        EmptyState(
            message = "Your bag is empty.",
            icon = AlfieIcons.Bag
        )
    }
}
