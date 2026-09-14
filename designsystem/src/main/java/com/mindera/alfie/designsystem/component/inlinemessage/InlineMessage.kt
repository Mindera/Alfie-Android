package com.mindera.alfie.designsystem.component.inlinemessage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

/**
 * Inline, non-dismissible notice attached to the content it describes — Figma "Inline Message"
 * (node `3689:31814`), used on the Bag line item for low-stock and unavailable-item copy.
 *
 * Icon and text are top-aligned so a wrapping message keeps the icon on its first line.
 */
@Composable
fun InlineMessage(
    message: String,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = AlfieIcons.AlertFill
) {
    val theme = LocalTheme.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .background(theme.color.surface.foregroundPrimary)
            .padding(theme.spacing.spacing8)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = theme.color.content.contentPrimary,
            modifier = Modifier.size(theme.sizing.icon.small)
        )
        Text(
            text = message,
            style = theme.typography.label.small,
            color = theme.color.content.contentPrimary,
            // Figma gives the message `flex-[1_0_0]`: it takes what is left after the icon and gap.
            // fillMaxWidth() would claim the whole row and push the tail past the right edge.
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun InlineMessagePreview() {
    Theme {
        InlineMessage(message = "Only 2 items left!")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
@Composable
private fun InlineMessageWrappingPreview() {
    Theme {
        InlineMessage(
            message = "Sorry, this item is not available anymore. Add it to your wishlist in case it gets back in stock."
        )
    }
}
