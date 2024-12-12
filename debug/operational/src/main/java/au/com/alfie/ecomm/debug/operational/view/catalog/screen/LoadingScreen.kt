package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.loading.Loading
import au.com.alfie.ecomm.designsystem.component.loading.LoadingType
import au.com.alfie.ecomm.designsystem.component.loading.LoadingWithLabel
import au.com.alfie.ecomm.designsystem.component.loading.LogoLoading
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun LoadingScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Theme.spacing.spacing16)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Loading ",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(modifier = Modifier.padding(Theme.spacing.spacing12)) {
            LoadingItem(
                type = LoadingType.Dark,
                hasLabel = false
            )
            Spacer(Modifier.width(Theme.spacing.spacing20))
            LoadingItem(
                type = LoadingType.Light,
                hasLabel = false
            )
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Loading with label",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(modifier = Modifier.padding(Theme.spacing.spacing12)) {
            LoadingItem(
                type = LoadingType.Dark,
                hasLabel = true
            )
            Spacer(Modifier.width(Theme.spacing.spacing20))
            LoadingItem(
                type = LoadingType.Light,
                hasLabel = true
            )
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Loading with logo",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.spacing12),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LogoLoading()
            LogoLoading(
                iconSize = 80.dp
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Loading with logo and label",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.spacing12),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LogoLoading(
                label = "Loading"
            )
        }
    }
}

@Composable
private fun LoadingItem(
    type: LoadingType,
    hasLabel: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Theme.color.primary.mono300)
                .border(
                    width = 1.dp,
                    color = Theme.color.primary.mono900
                ),
            contentAlignment = Alignment.Center
        ) {
            if (hasLabel) {
                LoadingWithLabel(
                    type = type,
                    modifier = Modifier.padding(Theme.spacing.spacing12)
                )
            } else {
                Loading(
                    type = type,
                    modifier = Modifier.padding(Theme.spacing.spacing12)
                )
            }
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun LoadingScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Loading Screen"),
        showNavigationIcon = false
    )
    LoadingScreen(topBarState = topBarState)
}
