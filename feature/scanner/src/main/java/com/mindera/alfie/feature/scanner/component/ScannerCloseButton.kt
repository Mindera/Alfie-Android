package com.mindera.alfie.feature.scanner.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.scanner.R

/**
 * The scanner's only way back: it hides both app bars, so without this the screen would be
 * escapable by system back alone — no visible affordance. Every scanner state shows one, over
 * the camera and over the message states alike.
 *
 * [tint] defaults to the theme's content colour for the message states; the camera overlay
 * passes white, since it sits on the preview rather than on a themed surface.
 */
@Composable
internal fun ScannerCloseButton(
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    val theme = LocalTheme.current
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(theme.spacing.spacing8)
            .size(Theme.iconSize.large)
    ) {
        Icon(
            painter = painterResource(id = AlfieIcons.Close),
            contentDescription = stringResource(id = R.string.scanner_close_content_description),
            tint = if (tint == Color.Unspecified) theme.color.content.contentPrimary else tint,
            modifier = Modifier.size(Theme.iconSize.medium)
        )
    }
}
