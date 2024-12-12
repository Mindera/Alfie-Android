package au.com.alfie.ecomm.designsystem.component.gallery

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
internal fun GalleryIndicator(
    currentItem: Int,
    itemCount: Int,
    onLeftClick: ClickEvent,
    onRightClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(Theme.shape.small)
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = Theme.alpha.alpha70)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GalleryIndicatorButton(
            icon = R.drawable.ic_action_chevron_left,
            contentDescription = R.string.gallery_controls_left_content_description,
            onClick = onLeftClick
        )
        Text(
            text = stringResource(id = R.string.gallery_controls_counter, currentItem, itemCount),
            style = Theme.typography.tiny,
            color = Theme.color.primary.mono900,
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing8)
        )
        GalleryIndicatorButton(
            icon = R.drawable.ic_action_chevron_right,
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
            tint = Theme.color.primary.mono900
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
