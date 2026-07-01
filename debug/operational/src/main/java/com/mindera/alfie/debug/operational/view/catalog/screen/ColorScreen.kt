package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ColorScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Theme.spacing.spacing8)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Primary",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        BlackAndWhiteSection()
        MonoSection()

        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Secondary",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        GreenSection()
        RedSection()
    }
}

@Composable
private fun BlackAndWhiteSection() {
    val c = LocalTheme.current.primitive.colors
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Mono",
            style = Theme.typography.paragraph,
            color = c.neutrals600
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = c.neutrals900, text = "Black")
            ColorItem(color = c.neutrals0, text = "White")
        }
    }
}

@Composable
private fun MonoSection() {
    val c = LocalTheme.current.primitive.colors
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Mono",
            style = Theme.typography.paragraph,
            color = c.neutrals600
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = c.neutrals800, text = "Mono900")
            ColorItem(color = c.neutrals700, text = "Mono800")
            ColorItem(color = c.neutrals600, text = "Mono700")
            ColorItem(color = c.neutrals600, text = "Mono600")
            ColorItem(color = c.neutrals500, text = "Mono500")
            ColorItem(color = c.neutrals400, text = "Mono400")
            ColorItem(color = c.neutrals300, text = "Mono300")
            ColorItem(color = c.neutrals200, text = "Mono200")
            ColorItem(color = c.neutrals100, text = "Mono100")
            ColorItem(color = c.neutrals100, text = "Mono050")
        }
    }
}

@Composable
private fun GreenSection() {
    val c = LocalTheme.current.primitive.colors
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Green",
            style = Theme.typography.paragraph,
            color = c.neutrals600
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = c.semanticSuccess800, text = "Green900")
            ColorItem(color = c.semanticSuccess800, text = "Green800")
            ColorItem(color = c.semanticSuccess700, text = "Green700")
            ColorItem(color = c.semanticSuccess600, text = "Green600")
            ColorItem(color = c.semanticSuccess500, text = "Green500")
            ColorItem(color = c.semanticSuccess400, text = "Green400")
            ColorItem(color = c.semanticSuccess300, text = "Green300")
            ColorItem(color = c.semanticSuccess200, text = "Green200")
            ColorItem(color = c.semanticSuccess100, text = "Green100")
            ColorItem(color = c.semanticSuccess100, text = "Green050")
        }
    }
}

@Composable
private fun RedSection() {
    val c = LocalTheme.current.primitive.colors
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Red",
            style = Theme.typography.paragraph,
            color = c.neutrals600
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = c.semanticError800, text = "Red900")
            ColorItem(color = c.semanticError800, text = "Red800")
            ColorItem(color = c.semanticError700, text = "Red700")
            ColorItem(color = c.semanticError600, text = "Red600")
            ColorItem(color = c.semanticError500, text = "Red500")
            ColorItem(color = c.semanticError400, text = "Red400")
            ColorItem(color = c.semanticError300, text = "Red300")
            ColorItem(color = c.semanticError200, text = "Red200")
            ColorItem(color = c.semanticError100, text = "Red100")
            ColorItem(color = c.semanticError100, text = "Red050")
        }
    }
}

@Composable
private fun ColorItem(
    color: Color,
    text: String
) {
    val c = LocalTheme.current.primitive.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .background(color = color)
        )
        Text(
            text = text,
            color = c.neutrals600,
            style = Theme.typography.tiny
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ColorScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Color Screen"),
        showNavigationIcon = false
    )
    ColorScreen(topBarState = topBarState)
}
