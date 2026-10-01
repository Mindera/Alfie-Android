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
 * Full-area empty state: an icon above the copy, centred on both axes. [subtitle] adds a secondary
 * line in the tertiary colour, 4 dp below [message].
 *
 * Distinct from [StateMessage], which leads with a bold title and carries a subtitle and an action.
 */
@Composable
fun EmptyState(
    message: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    subtitle: String? = null
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing4),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = message,
                style = theme.typography.body.medium,
                color = theme.color.content.contentPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = theme.typography.body.medium,
                    color = theme.color.content.contentTerciary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun EmptyStateWithSubtitlePreview() {
    Theme {
        EmptyState(
            message = "Your wishlist is empty.",
            subtitle = "Tap this icon in the products you like to see them here.",
            icon = AlfieIcons.Wishlist
        )
    }
}
