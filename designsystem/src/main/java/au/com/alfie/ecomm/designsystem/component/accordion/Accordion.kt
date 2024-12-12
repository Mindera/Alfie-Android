package au.com.alfie.ecomm.designsystem.component.accordion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.designsystem.R as RD

private const val ICON_ANIMATION_TIME = 300
private const val ROTATION_ANGLE = -180f
private const val DISABLED_ALPHA = .25f

@Composable
fun Accordion(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
    isEnabled: Boolean = true
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val alpha = if (isEnabled) 1F else DISABLED_ALPHA

    Column(
        modifier = modifier.alpha(alpha)
    ) {
        HorizontalDivider(dividerType = DividerType.Solid2Mono100)

        SectionHeader(
            title = title,
            isExpanded = isExpanded,
            isLarge = isLarge,
            isEnabled = isEnabled,
            onClick = { isExpanded = !isExpanded }
        )

        SectionContent(
            content = content,
            isExpanded = isExpanded
        )

        HorizontalDivider(dividerType = DividerType.Solid2Mono100)
    }
}

@Composable
private fun SectionHeader(
    title: String,
    isExpanded: Boolean,
    isLarge: Boolean,
    isEnabled: Boolean,
    onClick: ClickEvent
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) ROTATION_ANGLE else 0F,
        animationSpec = tween(ICON_ANIMATION_TIME),
        label = ""
    )
    Column(
        modifier = Modifier
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
            .fillMaxWidth()
    ) {
        val padding = if (isLarge) Theme.spacing.spacing16 else Theme.spacing.spacing8
        Row(
            modifier = Modifier.padding(horizontal = padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(vertical = padding)
            ) {
                Text(
                    text = title,
                    style = Theme.typography.paragraph,
                    color = Theme.color.primary.mono700
                )
            }

            Icon(
                painter = painterResource(id = RD.drawable.ic_action_chevron_down),
                modifier = Modifier
                    .size(Theme.iconSize.small)
                    .rotate(rotation),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SectionContent(
    content: @Composable () -> Unit,
    isExpanded: Boolean
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn(animationSpec = tween()) + expandVertically(animationSpec = tween()),
        exit = fadeOut(animationSpec = tween()) + shrinkVertically(animationSpec = tween())
    ) {
        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)) {
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            content()
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccordionPreview() {
    Theme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.spacing16)
        ) {
            val content: @Composable () -> Unit = {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Rhoncus, accumsan, vel interdum diam " +
                        "tortor cursus nam quisque ut. Blandit ut netus consequat ridiculus mi. Lacus a fermentum nec nisl " +
                        "consectetur molestie. Mauris mi cursus quis risus aliquam vivamus blandit. Maecenas dui odio odio aliquet.",
                    style = Theme.typography.small,
                    color = Theme.color.primary.mono700
                )
            }
            Accordion(
                title = "Accordion Title",
                content = content
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            Accordion(
                title = "Accordion Title",
                content = content,
                isLarge = true
            )
        }
    }
}
