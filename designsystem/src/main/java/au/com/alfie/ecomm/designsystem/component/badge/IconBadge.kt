package au.com.alfie.ecomm.designsystem.component.badge

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.animation.standardAccelerate
import au.com.alfie.ecomm.designsystem.component.badge.BadgeType.Counter
import au.com.alfie.ecomm.designsystem.component.badge.BadgeType.Highlight
import au.com.alfie.ecomm.designsystem.component.badge.BadgeType.None
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.designsystem.R as RD

private const val COUNTER_THRESHOLD = 99
private val BADGE_COLOR = Theme.color.secondary.red700

@Composable
fun IconBadge(
    badge: BadgeType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        when (badge) {
            is Counter -> Counter(
                count = badge.count,
                content = content
            )
            else -> Highlight(
                isVisible = badge != None,
                content = content
            )
        }
    }
}

@Composable
private fun Highlight(
    isVisible: Boolean,
    content: @Composable () -> Unit
) {
    BadgedBox(
        badge = {
            AnimatedVisibility(isVisible = isVisible) {
                Badge(containerColor = BADGE_COLOR)
            }
        }
    ) {
        content()
    }
}

@Composable
private fun Counter(
    count: Int,
    content: @Composable () -> Unit
) {
    val limitText = stringResource(id = RD.string.badge_count_limit)
    val countText = if (count > COUNTER_THRESHOLD) limitText else count.toString()
    BadgedBox(
        badge = {
            AnimatedVisibility(isVisible = count > 0) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Theme.color.white,
                            shape = CircleShape
                        )
                        .padding(1.dp)
                ) {
                    Badge(containerColor = BADGE_COLOR) {
                        AnimatedContent(
                            targetState = countText,
                            label = "countText"
                        ) {
                            Text(
                                text = it,
                                style = Theme.typography.tiny
                            )
                        }
                    }
                }
            }
        }
    ) {
        content()
    }
}

@Composable
private fun AnimatedVisibility(
    isVisible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(standardAccelerate()),
        exit = scaleOut(standardAccelerate()) + fadeOut()
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeModifierPreview() {
    Theme {
        Column(modifier = Modifier.fillMaxWidth()) {
            IconBadge(
                badge = Highlight
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = ""
                )
            }
            IconBadge(
                badge = Counter(0)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = ""
                )
            }
            IconBadge(
                badge = Counter(1)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = ""
                )
            }
            IconBadge(
                badge = Counter(11)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = ""
                )
            }
            IconBadge(
                badge = Counter(111)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = ""
                )
            }
        }
    }
}
