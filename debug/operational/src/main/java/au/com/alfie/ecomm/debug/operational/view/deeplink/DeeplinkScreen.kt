package au.com.alfie.ecomm.debug.operational.view.deeplink

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.debug.operational.view.deeplink.model.DeeplinkSection
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.input.TextField
import au.com.alfie.ecomm.designsystem.component.input.TextFieldSupportComponent
import au.com.alfie.ecomm.designsystem.component.input.TextFieldType
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
internal fun DeeplinkScreen(
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: DeeplinkViewModel = hiltViewModel()

    topBarState.textTopBar(title = stringResource(id = R.string.deeplink_screen_title))
    bottomBarState.hideBottomBar()

    DeeplinkScreenContent(
        sections = viewModel.sections,
        onDeeplinkClick = viewModel::openDeeplink
    )
}

@Composable
private fun DeeplinkScreenContent(
    sections: List<DeeplinkSection>,
    onDeeplinkClick: ClickEventOneArg<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Theme.spacing.spacing16)
            .verticalScroll(rememberScrollState())
    ) {
        sections.forEach {
            DeeplinkScreenSection(
                section = it,
                onDeeplinkClick = onDeeplinkClick
            )
        }
        CustomDeeplinkSection(onDeeplinkClick = onDeeplinkClick)
    }
}

@Composable
private fun DeeplinkScreenSection(
    section: DeeplinkSection,
    onDeeplinkClick: ClickEventOneArg<String>
) {
    Column(modifier = Modifier.padding(vertical = Theme.spacing.spacing16)) {
        Text(
            text = section.name,
            style = Theme.typography.paragraphBold
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing4))
        HorizontalDivider()
        section.entries.forEach { entry ->
            Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
            Button(
                type = ButtonType.Underlined,
                text = entry.name,
                onClick = { onDeeplinkClick(entry.url) }
            )
        }
    }
}

@Composable
private fun CustomDeeplinkSection(onDeeplinkClick: ClickEventOneArg<String>) {
    var path by rememberSaveable { mutableStateOf("") }
    val url = remember(path) { "https://www.alfie.com/$path" }

    Column(modifier = Modifier.padding(vertical = Theme.spacing.spacing16)) {
        Text(
            text = "Deep linking - Custom path",
            style = Theme.typography.paragraphBold
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing4))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing8))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = path,
                type = TextFieldType.Default,
                onTextChange = { path = it },
                placeholder = "Deeplink path",
                label = "Path",
                supportComponent = TextFieldSupportComponent(url),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(Theme.spacing.spacing12))
            Button(
                type = ButtonType.Primary,
                text = "Open",
                onClick = { onDeeplinkClick(url) }
            )
        }
    }
}
