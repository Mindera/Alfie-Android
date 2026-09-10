package com.mindera.alfie.feature.bag.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.test.BAG_CONTINUE_CTA
import com.mindera.alfie.core.ui.test.BAG_TOTAL
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.divider.DividerType
import com.mindera.alfie.designsystem.component.divider.HorizontalDivider
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.bag.R
import com.mindera.alfie.feature.bag.models.BagSummaryUi

/**
 * Totals and checkout call to action pinned below the bag list — Figma "Purchase Summary"
 * (node `44:93925`): a soft top rule, the total, the shipping note, then a full-width primary CTA.
 */
@Composable
internal fun BagSummary(
    summary: BagSummaryUi,
    onContinueClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Column(modifier = modifier.background(theme.color.surface.backgroundPrimary)) {
        HorizontalDivider(dividerType = DividerType.Solid1Mono200)
        Column(
            verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
            modifier = Modifier.padding(
                horizontal = theme.spacing.spacing16,
                vertical = theme.spacing.spacing8
            )
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.bag_total),
                        style = theme.typography.body.mediumBold,
                        color = theme.color.content.contentPrimary
                    )
                    Text(
                        text = summary.totalFormatted,
                        style = theme.typography.body.mediumBold,
                        color = theme.color.content.contentPrimary,
                        modifier = Modifier.testTag(BAG_TOTAL)
                    )
                }
                Text(
                    text = stringResource(id = R.string.bag_shipping_note),
                    style = theme.typography.body.small,
                    color = theme.color.content.contentPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                type = ButtonType.Primary,
                buttonSize = ButtonSize.Medium,
                text = stringResource(id = R.string.bag_continue_cta),
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(BAG_CONTINUE_CTA)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
@Composable
private fun BagSummaryPreview() {
    Theme {
        BagSummary(
            summary = BagSummaryUi(totalFormatted = "£212.00"),
            onContinueClick = {}
        )
    }
}
