package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.debug.operational.view.catalog.util.HeaderDivider
import au.com.alfie.ecomm.debug.operational.view.catalog.util.SectionDivider
import au.com.alfie.ecomm.debug.operational.view.catalog.util.SwitchItem
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.searchbar.rememberSearchState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBar
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

@Destination
@Composable
fun TitleHeaderScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = Theme.spacing.spacing16)
            .imePadding()
    ) {
        TopBarTitleSections()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing24))
    }
}

@Composable
private fun TopBarTitleSections() {
    var showNavigation by remember { mutableStateOf(false) }
    var showOneIcon by remember { mutableStateOf(false) }
    var showTwoIcons by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(false) }
    var isTextLeftAligned by remember { mutableStateOf(false) }
    var isLogoTopBar by remember { mutableStateOf(false) }

    HeaderDivider(text = "TopBar Title")
    SectionDivider(text = "Properties")
    SwitchItem(
        text = "Show Back Navigation",
        isChecked = showNavigation,
        onCheckChange = { showNavigation = it }
    )
    SwitchItem(
        text = "Show Search Icon",
        isChecked = showOneIcon,
        onCheckChange = {
            showOneIcon = it
            if (it) showTwoIcons = false
        }
    )
    SwitchItem(
        text = "Show Two Icons",
        isChecked = showTwoIcons,
        onCheckChange = {
            showTwoIcons = it
            if (it) showOneIcon = false
        }
    )
    SwitchItem(
        text = "Show Logo Title",
        isChecked = isLogoTopBar,
        onCheckChange = {
            isLogoTopBar = it
            if (it) isTextLeftAligned = false
        }
    )
    SwitchItem(
        text = "Show Tab Title",
        isChecked = isTextLeftAligned,
        onCheckChange = {
            isTextLeftAligned = it
            if (it) isLogoTopBar = false
        }
    )
    SwitchItem(
        text = "Dark Mode",
        isChecked = isDarkMode,
        onCheckChange = { isDarkMode = it }
    )

    SectionDivider(text = "Title Bar")

    val title = when {
        isLogoTopBar -> TopBarTitle.Icon(
            icon = R.drawable.ic_alfie_logo_dark
        )

        isTextLeftAligned -> TopBarTitle.Text(
            title = "Title",
            isLeftAligned = true
        )

        else -> TopBarTitle.Text(
            title = "Title",
            isLeftAligned = false
        )
    }
    val actions = when {
        showOneIcon -> persistentListOf(TopBarAction.Search(rememberSearchState()))
        showTwoIcons -> persistentListOf(
            TopBarAction.Search(rememberSearchState()),
            TopBarAction.Account {}
        )
        else -> persistentListOf()
    }

    TopBar(
        state = TopBarState(
            title = title,
            showNavigationIcon = showNavigation,
            isDarkTheme = isDarkMode,
            actions = actions
        ),
        onNavigationClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TitleHeaderScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Top Bar Screen"),
        showNavigationIcon = false
    )
    TitleHeaderScreen(topBarState = topBarState)
}
