package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun TypographyScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(Theme.spacing.spacing8)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Heading",
            style = LocalTheme.current.typography.heading.small
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)) {
            Text(text = "Heading 1", style = LocalTheme.current.typography.display.large)
            Text(text = "Heading 2", style = LocalTheme.current.typography.heading.medium)
            Text(text = "Heading 3", style = LocalTheme.current.typography.heading.small)
        }

        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Paragraph",
            style = LocalTheme.current.typography.heading.small
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)) {
            Text("Paragraph", style = LocalTheme.current.typography.body.medium)
            Text("Paragraph Italic", style = LocalTheme.current.typography.body.medium.copy(fontStyle = FontStyle.Italic))
            Text("Paragraph Underlined", style = LocalTheme.current.typography.body.medium.copy(textDecoration = TextDecoration.Underline))
            Text("Paragraph Strikethrough", style = LocalTheme.current.typography.body.medium.copy(textDecoration = TextDecoration.LineThrough))
            Text("Paragraph Bold", style = LocalTheme.current.typography.body.mediumBold)
            Text("Paragraph Bold Italic", style = LocalTheme.current.typography.body.mediumBold.copy(fontStyle = FontStyle.Italic))
            Text("Paragraph Bold Underline", style = LocalTheme.current.typography.body.mediumBold.copy(textDecoration = TextDecoration.Underline))
            Text("Paragraph Strikethrough", style = LocalTheme.current.typography.body.mediumBold.copy(textDecoration = TextDecoration.LineThrough))
        }

        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Small",
            style = LocalTheme.current.typography.heading.small
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)) {
            Text("Small Italic", style = LocalTheme.current.typography.body.small.copy(fontStyle = FontStyle.Italic))
            Text("Small Underlined", style = LocalTheme.current.typography.body.small.copy(textDecoration = TextDecoration.Underline))
            Text("Small Strikethrough", style = LocalTheme.current.typography.body.small.copy(textDecoration = TextDecoration.LineThrough))
            Text("Small Bold", style = LocalTheme.current.typography.label.smallBold)
            Text("Small Bold Italic", style = LocalTheme.current.typography.label.smallBold.copy(fontStyle = FontStyle.Italic))
            Text("Small Bold Underline", style = LocalTheme.current.typography.label.smallBold.copy(textDecoration = TextDecoration.Underline))
            Text("Small Bold Strikethrough", style = LocalTheme.current.typography.label.smallBold.copy(textDecoration = TextDecoration.LineThrough))
        }

        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Tiny",
            style = LocalTheme.current.typography.heading.small
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)) {
            Text("Tiny", style = LocalTheme.current.typography.label.small)
            Text("Tiny Italic", style = LocalTheme.current.typography.label.small.copy(fontStyle = FontStyle.Italic))
            Text("Tiny Bold", style = LocalTheme.current.typography.label.smallBold)
            Text("Tiny Bold Italic", style = LocalTheme.current.typography.label.smallBold.copy(fontStyle = FontStyle.Italic))
            Text("Tiny Bold Underline", style = LocalTheme.current.typography.label.smallBold.copy(textDecoration = TextDecoration.Underline))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TypographyScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Typography Screen"),
        showNavigationIcon = false
    )
    TypographyScreen(topBarState = topBarState)
}
