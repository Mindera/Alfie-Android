package com.mindera.alfie.debug.operational.view.catalog.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.divider.DividerType
import com.mindera.alfie.designsystem.component.divider.HorizontalDivider
import com.mindera.alfie.designsystem.component.switch.Switch
import com.mindera.alfie.designsystem.theme.Theme

@Composable
internal fun HeaderDivider(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(
                horizontal = Theme.spacing.spacing20,
                vertical = Theme.spacing.spacing12
            ),
            text = text,
            style = Theme.typography.heading3
        )
        HorizontalDivider(dividerType = DividerType.Solid1Mono300)
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    }
}

@Composable
internal fun SectionDivider(text: String) {
    Text(
        modifier = Modifier.padding(
            horizontal = Theme.spacing.spacing32,
            vertical = Theme.spacing.spacing12
        ),
        text = text,
        style = Theme.typography.paragraphBold
    )
    HorizontalDivider(dividerType = DividerType.Solid1Mono300)
    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
}

@Composable
internal fun SwitchItem(
    text: String,
    isChecked: Boolean,
    onCheckChange: ClickEventOneArg<Boolean>
) {
    Row(
        modifier = Modifier.padding(horizontal = Theme.spacing.spacing20),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = isChecked,
            onCheckChange = onCheckChange
        )
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = text,
            style = Theme.typography.paragraph
        )
    }
}
