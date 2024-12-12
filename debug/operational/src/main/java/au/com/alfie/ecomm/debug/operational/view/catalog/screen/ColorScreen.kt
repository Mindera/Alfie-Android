package au.com.alfie.ecomm.debug.operational.view.catalog.screen

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
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
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
        YellowSection()
        BlueSection()
        OrangeSection()
    }
}

@Composable
private fun BlackAndWhiteSection() {
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Mono",
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono700
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = Theme.color.black, text = "Black")
            ColorItem(color = Theme.color.white, text = "White")
        }
    }
}

@Composable
private fun MonoSection() {
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Mono",
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono700
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = Theme.color.primary.mono900, text = "Mono900")
            ColorItem(color = Theme.color.primary.mono800, text = "Mono800")
            ColorItem(color = Theme.color.primary.mono700, text = "Mono700")
            ColorItem(color = Theme.color.primary.mono600, text = "Mono600")
            ColorItem(color = Theme.color.primary.mono500, text = "Mono500")
            ColorItem(color = Theme.color.primary.mono400, text = "Mono400")
            ColorItem(color = Theme.color.primary.mono300, text = "Mono300")
            ColorItem(color = Theme.color.primary.mono200, text = "Mono200")
            ColorItem(color = Theme.color.primary.mono100, text = "Mono100")
            ColorItem(color = Theme.color.primary.mono050, text = "Mono050")
        }
    }
}

@Composable
private fun GreenSection() {
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Green",
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono700
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = Theme.color.secondary.green900, text = "Green900")
            ColorItem(color = Theme.color.secondary.green800, text = "Green800")
            ColorItem(color = Theme.color.secondary.green700, text = "Green700")
            ColorItem(color = Theme.color.secondary.green600, text = "Green600")
            ColorItem(color = Theme.color.secondary.green500, text = "Green500")
            ColorItem(color = Theme.color.secondary.green400, text = "Green400")
            ColorItem(color = Theme.color.secondary.green300, text = "Green300")
            ColorItem(color = Theme.color.secondary.green200, text = "Green200")
            ColorItem(color = Theme.color.secondary.green100, text = "Green100")
            ColorItem(color = Theme.color.secondary.green050, text = "Green050")
        }
    }
}

@Composable
private fun RedSection() {
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Red",
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono700
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = Theme.color.secondary.red900, text = "Red900")
            ColorItem(color = Theme.color.secondary.red800, text = "Red800")
            ColorItem(color = Theme.color.secondary.red700, text = "Red700")
            ColorItem(color = Theme.color.secondary.red600, text = "Red600")
            ColorItem(color = Theme.color.secondary.red500, text = "Red500")
            ColorItem(color = Theme.color.secondary.red400, text = "Red400")
            ColorItem(color = Theme.color.secondary.red300, text = "Red300")
            ColorItem(color = Theme.color.secondary.red200, text = "Red200")
            ColorItem(color = Theme.color.secondary.red100, text = "Red100")
            ColorItem(color = Theme.color.secondary.red050, text = "Red050")
        }
    }
}

@Composable
private fun YellowSection() {
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Yellow",
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono700
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = Theme.color.secondary.yellow500, text = "Yellow500")
            ColorItem(color = Theme.color.secondary.yellow400, text = "Yellow400")
            ColorItem(color = Theme.color.secondary.yellow300, text = "Yellow300")
            ColorItem(color = Theme.color.secondary.yellow200, text = "Yellow200")
            ColorItem(color = Theme.color.secondary.yellow100, text = "Yellow100")
            ColorItem(color = Theme.color.secondary.yellow050, text = "Yellow050")
        }
    }
}

@Composable
private fun BlueSection() {
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Blue",
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono700
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = Theme.color.secondary.blue900, text = "Blue900")
            ColorItem(color = Theme.color.secondary.blue800, text = "Blue800")
            ColorItem(color = Theme.color.secondary.blue700, text = "Blue700")
            ColorItem(color = Theme.color.secondary.blue600, text = "Blue600")
            ColorItem(color = Theme.color.secondary.blue500, text = "Blue500")
            ColorItem(color = Theme.color.secondary.blue400, text = "Blue400")
            ColorItem(color = Theme.color.secondary.blue300, text = "Blue300")
            ColorItem(color = Theme.color.secondary.blue200, text = "Blue200")
            ColorItem(color = Theme.color.secondary.blue100, text = "Blue100")
            ColorItem(color = Theme.color.secondary.blue050, text = "Blue050")
        }
    }
}

@Composable
private fun OrangeSection() {
    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing12)
            .fillMaxWidth()
    ) {
        Text(
            text = "Orange",
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono700
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ColorItem(color = Theme.color.secondary.orange500, text = "Orange500")
            ColorItem(color = Theme.color.secondary.orange400, text = "Orange400")
            ColorItem(color = Theme.color.secondary.orange300, text = "Orange300")
            ColorItem(color = Theme.color.secondary.orange200, text = "Orange200")
            ColorItem(color = Theme.color.secondary.orange100, text = "Orange100")
            ColorItem(color = Theme.color.secondary.orange050, text = "Orange050")
        }
    }
}

@Composable
private fun ColorItem(
    color: Color,
    text: String
) {
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
            color = Theme.color.primary.mono700,
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
