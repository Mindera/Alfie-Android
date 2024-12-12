package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.component.divider.VerticalDivider
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import androidx.compose.material3.HorizontalDivider as MaterialHorizontalDivider

@Destination
@Composable
fun DividerScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Theme.spacing.spacing8)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Horizontal",
            style = Theme.typography.heading3
        )
        MaterialHorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "1.dp",
            style = Theme.typography.paragraph
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing4))
        Box(
            modifier = Modifier
                .height(24.dp)
                .background(Theme.color.primary.mono400),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDivider(dividerType = DividerType.Solid1Mono100)
        }
        Spacer(modifier = Modifier.size(Theme.spacing.spacing4))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "2.dp",
            style = Theme.typography.paragraph
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing4))
        Box(
            modifier = Modifier
                .height(24.dp)
                .background(Theme.color.primary.mono400),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDivider(dividerType = DividerType.Solid2Mono100)
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Vertical",
            style = Theme.typography.heading3
        )
        MaterialHorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Row(
            modifier = Modifier
                .padding(Theme.spacing.spacing16)
                .requiredHeight(120.dp)
        ) {
            Text(text = "1.dp")
            Spacer(modifier = Modifier.size(Theme.spacing.spacing4))
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .background(Theme.color.primary.mono400),
                contentAlignment = Alignment.Center
            ) {
                VerticalDivider(dividerType = DividerType.Solid1Mono100)
            }
            Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
            Text(text = "2.dp")
            Spacer(modifier = Modifier.size(Theme.spacing.spacing4))
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .background(Theme.color.primary.mono400),
                contentAlignment = Alignment.Center
            ) {
                VerticalDivider(dividerType = DividerType.Solid2Mono100)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DividerScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Dividers Screen"),
        showNavigationIcon = false
    )
    Theme {
        DividerScreen(topBarState = topBarState)
    }
}
