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
import au.com.alfie.ecomm.designsystem.component.searchbar.SearchTextType
import au.com.alfie.ecomm.designsystem.component.searchbar.rememberSearchState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBar
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun TitleSearchScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = Theme.spacing.spacing16)
            .imePadding()
    ) {
        TopBarSearchSections()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing24))
    }
}

@Composable
private fun TopBarSearchSections() {
    var showSoft by remember { mutableStateOf(true) }
    var showSoftLarge by remember { mutableStateOf(false) }
    var showDark by remember { mutableStateOf(false) }
    var showLight by remember { mutableStateOf(false) }

    HeaderDivider(text = "Top Bar Search")
    SectionDivider(text = "Properties")
    SwitchItem(
        text = "Soft (Default)",
        isChecked = showSoft,
        onCheckChange = {
            if (showSoft.not() && it) {
                showSoft = true
            }
            if (it) {
                showSoftLarge = false
                showLight = false
                showDark = false
            }
        }
    )
    SwitchItem(
        text = "Soft Large",
        isChecked = showSoftLarge,
        onCheckChange = {
            showSoftLarge = it
            if (it) {
                showSoft = false
                showLight = false
                showDark = false
            }
        }
    )
    SwitchItem(
        text = "Dark",
        isChecked = showDark,
        onCheckChange = {
            showDark = it
            if (it) {
                showSoft = false
                showLight = false
                showSoftLarge = false
            }
        }
    )
    SwitchItem(
        text = "Light",
        isChecked = showLight,
        onCheckChange = {
            showLight = it
            if (it) {
                showSoft = false
                showSoftLarge = false
                showDark = false
            }
        }
    )

    SectionDivider(text = "TopBar")

    val searchTextType = when {
        showSoftLarge -> SearchTextType.SoftLarge
        showDark -> SearchTextType.Dark
        showLight -> SearchTextType.Light
        else -> SearchTextType.Soft
    }

    val searchState = rememberSearchState()
    searchState.updateSearchType(searchTextType)

    val topBarState = TopBarState(
        title = TopBarTitle.Search(
            isPullDownToRefresh = false,
            searchState = searchState,
            onTermChanged = {},
            onFocusChange = {},
            customOverlay = { _, _ -> }
        ),
        showNavigationIcon = false
    )
    TopBar(
        state = topBarState,
        onNavigationClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TitleSearchScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Top Bar Search Screen"),
        showNavigationIcon = false
    )
    TitleSearchScreen(topBarState = topBarState)
}
