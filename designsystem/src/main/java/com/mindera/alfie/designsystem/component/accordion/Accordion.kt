package com.mindera.alfie.designsystem.component.accordion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.divider.DividerType
import com.mindera.alfie.designsystem.component.divider.HorizontalDivider
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val ICON_ANIMATION_TIME = 300
private const val DISABLED_ALPHA = .25f

/** border/soft at Stroke Weight/Default — the hairline that separates accordion rows. */
private val DIVIDER = DividerType.Solid1Mono200

/**
 * A single self-contained accordion: a hairline above and below the row.
 *
 * Stacking these directly would double the hairline between neighbours — use [AccordionGroup]
 * for a run of rows, which draws each boundary exactly once.
 */
@Composable
fun Accordion(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
    isEnabled: Boolean = true
) {
    Column(modifier = modifier) {
        HorizontalDivider(dividerType = DIVIDER)
        AccordionRow(
            title = title,
            content = content,
            isLarge = isLarge,
            isEnabled = isEnabled
        )
        HorizontalDivider(dividerType = DIVIDER)
    }
}

/**
 * A run of accordion rows sharing their hairlines, per the PDP design: one above each row plus a
 * closing one, so N rows render N+1 dividers rather than the 2N a stack of [Accordion]s would give.
 *
 * The rows carry no horizontal padding of their own — the caller supplies the screen margin.
 */
@Composable
fun <T> AccordionGroup(
    items: ImmutableList<T>,
    title: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
    isEnabled: Boolean = true,
    content: @Composable (T) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        items.forEach { item ->
            HorizontalDivider(dividerType = DIVIDER)
            AccordionRow(
                title = title(item),
                content = { content(item) },
                isLarge = isLarge,
                isEnabled = isEnabled
            )
        }
        HorizontalDivider(dividerType = DIVIDER)
    }
}

@Composable
private fun AccordionRow(
    title: String,
    content: @Composable () -> Unit,
    isLarge: Boolean,
    isEnabled: Boolean
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val alpha = if (isEnabled) 1F else DISABLED_ALPHA

    Column(modifier = Modifier.alpha(alpha)) {
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
    val theme = LocalTheme.current
    // interactive/small-padding-top-bottom; the row is 40dp tall because the 24dp icon sets the
    // height and the 20dp title line-height sits inside it.
    val verticalPadding = if (isLarge) Theme.spacing.spacing16 else Theme.spacing.spacing8

    Row(
        modifier = Modifier
            .clickable(enabled = isEnabled, onClick = onClick)
            .fillMaxWidth()
            .padding(vertical = verticalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = title,
            style = theme.typography.heading.xSmall,
            color = theme.color.content.contentPrimary
        )
        // The design specifies the collapsed "+" only; "−" for the expanded state follows the
        // plus/minus affordance and reuses the design system's existing Minus glyph.
        Crossfade(
            targetState = isExpanded,
            animationSpec = tween(ICON_ANIMATION_TIME),
            label = "AccordionIcon"
        ) { expanded ->
            Icon(
                painter = painterResource(id = if (expanded) AlfieIcons.Minus else AlfieIcons.Add),
                modifier = Modifier.size(Theme.iconSize.medium),
                tint = theme.color.content.contentPrimary,
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
        // spacing/spacing-xs — the gap the design puts between the header row and its content.
        Column(modifier = Modifier.padding(bottom = Theme.spacing.spacing8)) {
            content()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccordionGroupPreview() {
    Theme {
        val theme = LocalTheme.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.spacing16)
        ) {
            AccordionGroup(
                items = persistentListOf("Size & Fit", "Materials & Care Guide", "Shippings and Returns"),
                title = { it }
            ) { item ->
                Text(
                    text = "Everything worth knowing about $item.",
                    style = theme.typography.body.medium,
                    color = theme.color.content.contentPrimary
                )
            }
        }
    }
}
