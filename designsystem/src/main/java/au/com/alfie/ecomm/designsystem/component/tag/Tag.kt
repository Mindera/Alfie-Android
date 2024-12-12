package au.com.alfie.ecomm.designsystem.component.tag

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

private val MIN_TAG_HEIGHT = 32.dp
private val TAG_ELEVATION = 8.dp
private val TAG_DIVIDER_THICKNESS = 2.dp
private val TAG_LEADING_ICON_SIZE = 16.dp
private val TAG_CLOSE_ICON_SIZE = 12.dp
private const val TRANSITION_DURATION = 300

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    iconContentDescription: String? = null,
    isDismissible: Boolean = false
) {
    var isVisible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(TRANSITION_DURATION)),
        exit = fadeOut(animationSpec = tween(TRANSITION_DURATION))
    ) {
        Surface(
            modifier = modifier
                .clickable(enabled = isVisible) { if (isDismissible) isVisible = false }
                .defaultMinSize(minHeight = MIN_TAG_HEIGHT)
                .wrapContentWidth(),
            shadowElevation = TAG_ELEVATION,
            shape = Theme.shape.tiny,
            color = Theme.color.primary.mono100
        ) {
            Row(
                modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                verticalAlignment = CenterVertically
            ) {
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = Theme.color.primary.mono800,
                    thickness = TAG_DIVIDER_THICKNESS
                )
                if (icon != null) {
                    Icon(
                        modifier = Modifier
                            .padding(
                                top = Theme.spacing.spacing8,
                                bottom = Theme.spacing.spacing8,
                                start = Theme.spacing.spacing12
                            )
                            .size(TAG_LEADING_ICON_SIZE),
                        painter = painterResource(id = icon),
                        contentDescription = iconContentDescription
                    )
                }
                Text(
                    text = text,
                    modifier = Modifier.padding(
                        vertical = Theme.spacing.spacing8,
                        horizontal = Theme.spacing.spacing12
                    ),
                    style = Theme.typography.paragraph,
                    color = Theme.color.black
                )
                if (isDismissible) {
                    Icon(
                        modifier = Modifier
                            .padding(
                                top = Theme.spacing.spacing8,
                                bottom = Theme.spacing.spacing8,
                                end = Theme.spacing.spacing12
                            )
                            .size(TAG_CLOSE_ICON_SIZE),
                        painter = painterResource(id = R.drawable.ic_action_close_dark),
                        contentDescription = null,
                        tint = Theme.color.black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TagPreview() {
    Column {
        Tag(text = "This is a tag")
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Tag(
            text = "This is a tag",
            icon = R.drawable.ic_action_star
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Tag(
            text = "This is a tag",
            isDismissible = true
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Tag(
            text = "This is a tag",
            icon = R.drawable.ic_action_star,
            isDismissible = true
        )
    }
}
