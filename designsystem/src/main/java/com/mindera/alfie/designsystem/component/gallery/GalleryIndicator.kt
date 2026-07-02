package com.mindera.alfie.designsystem.component.gallery

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

@Composable
internal fun GalleryIndicator(
    currentItem: Int,
    itemCount: Int,
    onLeftClick: ClickEvent,
    onRightClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val c = LocalTheme.current.primitive.colors
    Row(
        modifier = modifier
            .clip(Theme.shape.small)
            .background(color = c.neutrals0.copy(alpha = Theme.alpha.alpha70)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GalleryIndicatorButton(
            icon = AlfieIcons.ChevronLeft,
            contentDescription = R.string.gallery_controls_left_content_description,
            onClick = onLeftClick
        )
        Text(
            text = stringResource(id = R.string.gallery_controls_counter, currentItem, itemCount),
            style = Theme.typography.tiny,
            color = c.neutrals800,
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing8)
        )
        GalleryIndicatorButton(
            icon = AlfieIcons.ChevronRight,
            contentDescription = R.string.gallery_controls_right_content_description,
            onClick = onRightClick
        )
    }
}

@Composable
private fun GalleryIndicatorButton(
    @DrawableRes icon: Int,
    @StringRes contentDescription: Int,
    onClick: ClickEvent
) {
    val c = LocalTheme.current.primitive.colors
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(Theme.iconSize.xLarge)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = contentDescription),
            modifier = Modifier.size(Theme.iconSize.small),
            tint = c.neutrals800
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun GalleryIndicatorPreview() {
    Theme {
        GalleryIndicator(
            currentItem = 1,
            itemCount = 4,
            onLeftClick = { },
            onRightClick = { },
            modifier = Modifier.padding(Theme.spacing.spacing32)
        )
    }
}
