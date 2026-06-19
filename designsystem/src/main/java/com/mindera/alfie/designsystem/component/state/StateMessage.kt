package com.mindera.alfie.designsystem.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.theme.Theme

/**
 * Full-area, vertically centered message used for empty / no-results / error states.
 *
 * Renders a bold title, an optional muted subtitle, and an optional action button.
 */
@Composable
fun StateMessage(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    action: StateMessageAction? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Theme.spacing.spacing32)
    ) {
        Text(
            text = title,
            style = Theme.typography.paragraphBold,
            color = Theme.color.primary.mono900,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
            Text(
                text = subtitle,
                style = Theme.typography.paragraph,
                color = Theme.color.primary.mono500,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (action != null) {
            Spacer(modifier = Modifier.height(Theme.spacing.spacing24))
            Button(
                type = ButtonType.Primary,
                buttonSize = ButtonSize.Medium,
                text = action.label,
                onClick = action.onClick
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun StateMessagePreview() {
    Theme {
        StateMessage(
            title = "No results found for \"cream\"",
            subtitle = "Please check the spelling or try different words."
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun StateMessageWithActionPreview() {
    Theme {
        StateMessage(
            title = "Something went wrong",
            subtitle = "Please try again.",
            action = StateMessageAction(label = "Try again", onClick = {})
        )
    }
}
