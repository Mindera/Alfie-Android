package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.debug.operational.view.catalog.util.SwitchItem
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.modal.BottomSheet
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ModalScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    var showBottomSheet by remember { mutableStateOf(false) }
    var isFullscreen by remember { mutableStateOf(false) }
    var isSmall by remember { mutableStateOf(false) }
    var isMedium by remember { mutableStateOf(false) }
    var isLarge by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(Theme.spacing.spacing16)) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Modal",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Theme.spacing.spacing24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)
        ) {
            Button(
                type = ButtonType.Primary,
                onClick = {
                    isSmall = true
                    isMedium = false
                    isLarge = false
                    showBottomSheet = true
                },
                text = "Show Small Modal",
                buttonSize = ButtonSize.Medium
            )
            Button(
                type = ButtonType.Primary,
                onClick = {
                    isSmall = false
                    isMedium = true
                    isLarge = false
                    showBottomSheet = true
                },
                text = "Show Medium Modal",
                buttonSize = ButtonSize.Medium
            )
            Button(
                type = ButtonType.Primary,
                onClick = {
                    isSmall = false
                    isMedium = false
                    isLarge = true
                    showBottomSheet = true
                },
                text = "Show Large Modal",
                buttonSize = ButtonSize.Medium
            )

            SwitchItem(
                text = "Fullscreen",
                isChecked = isFullscreen,
                onCheckChange = { isFullscreen = !isFullscreen }
            )
        }
    }

    if (showBottomSheet) {
        BottomSheet(
            title = "Size Guide",
            onDismiss = { showBottomSheet = false },
            isFullscreen = isFullscreen
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
                ModalContent(isMedium || isLarge)
                if (isLarge) {
                    ModalContent()
                    ModalContent()
                }
                Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
            }
        }
    }
}

@Composable
private fun ModalContent(showAllContent: Boolean = true) {
    Text(
        modifier = Modifier.padding(Theme.spacing.spacing12),
        text = "How To Measure",
        style = Theme.typography.heading3
    )
    Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
    Text(
        modifier = Modifier.padding(Theme.spacing.spacing12),
        text = "Bust",
        style = Theme.typography.paragraphBold
    )
    Text(
        modifier = Modifier.padding(Theme.spacing.spacing12),
        text = "Measure around the fullest part of your chest.",
        style = Theme.typography.paragraph
    )
    Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
    if (showAllContent) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Waist",
            style = Theme.typography.paragraphBold
        )
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "The slimmest part of your natural waistline, above your naval em below your ribcage",
            style = Theme.typography.paragraph
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Hip",
            style = Theme.typography.paragraphBold
        )
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Measure around the widest point of your hips, at the top of the legs.",
            style = Theme.typography.paragraph
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    }
}

@Preview(showBackground = true)
@Composable
private fun ModalScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Modal Component"),
        showNavigationIcon = false
    )
    ModalScreen(topBarState = topBarState)
}
