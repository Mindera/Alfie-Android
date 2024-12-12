package au.com.alfie.ecomm.debug.operational.view.catalog.screen

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
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
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
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)) {
            Text(text = "Heading 1", style = Theme.typography.heading1)
            Text(text = "Heading 2", style = Theme.typography.heading2)
            Text(text = "Heading 3", style = Theme.typography.heading3)
        }

        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Paragraph",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)) {
            Text("Paragraph", style = Theme.typography.paragraph)
            Text("Paragraph Italic", style = Theme.typography.paragraphItalic)
            Text("Paragraph Underlined", style = Theme.typography.paragraphUnderlined)
            Text("Paragraph Strikethrough", style = Theme.typography.paragraphStrikethrough)
            Text("Paragraph Bold", style = Theme.typography.paragraphBold)
            Text("Paragraph Bold Italic", style = Theme.typography.paragraphBoldItalic)
            Text("Paragraph Bold Underline", style = Theme.typography.paragraphBoldUnderline)
            Text("Paragraph Strikethrough", style = Theme.typography.paragraphBoldStrikethrough)
        }

        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Small",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)) {
            Text("Small Italic", style = Theme.typography.smallItalic)
            Text("Small Underlined", style = Theme.typography.smallUnderlined)
            Text("Small Strikethrough", style = Theme.typography.smallStrikethrough)
            Text("Small Bold", style = Theme.typography.smallBold)
            Text("Small Bold Italic", style = Theme.typography.smallBoldItalic)
            Text("Small Bold Underline", style = Theme.typography.smallBoldUnderline)
            Text("Small Bold Strikethrough", style = Theme.typography.smallBoldStrikethrough)
        }

        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Tiny",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)) {
            Text("Tiny", style = Theme.typography.tiny)
            Text("Tiny Italic", style = Theme.typography.tinyItalic)
            Text("Tiny Bold", style = Theme.typography.tinyBold)
            Text("Tiny Bold Italic", style = Theme.typography.tinyBoldItalic)
            Text("Tiny Bold Underline", style = Theme.typography.tinyBoldUnderline)
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
