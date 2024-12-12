package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import au.com.alfie.ecomm.designsystem.R as RD

private val actionIcons = listOf(
    RD.drawable.ic_action_arrow_down,
    RD.drawable.ic_action_arrow_left,
    RD.drawable.ic_action_arrow_up,
    RD.drawable.ic_action_arrow_right,
    RD.drawable.ic_action_bag,
    RD.drawable.ic_action_bell,
    RD.drawable.ic_action_bookmark,
    RD.drawable.ic_action_camera,
    RD.drawable.ic_action_chevron_down,
    RD.drawable.ic_action_chevron_left,
    RD.drawable.ic_action_chevron_right,
    RD.drawable.ic_action_chevron_up,
    RD.drawable.ic_action_close_dark,
    RD.drawable.ic_action_copy,
    RD.drawable.ic_action_download,
    RD.drawable.ic_action_edit,
    RD.drawable.ic_action_ellipsis,
    RD.drawable.ic_action_expand,
    RD.drawable.ic_action_external_link,
    RD.drawable.ic_action_eye,
    RD.drawable.ic_action_eye_closed,
    RD.drawable.ic_action_face_id,
    RD.drawable.ic_action_filters,
    RD.drawable.ic_action_hamburger_menu,
    RD.drawable.ic_action_heart_fill,
    RD.drawable.ic_action_heart_outline,
    RD.drawable.ic_action_house,
    RD.drawable.ic_action_inbox,
    RD.drawable.ic_action_lock,
    RD.drawable.ic_action_log_in,
    RD.drawable.ic_action_log_out,
    RD.drawable.ic_action_microphone,
    RD.drawable.ic_action_minus,
    RD.drawable.ic_action_plus,
    RD.drawable.ic_action_refresh,
    RD.drawable.ic_action_reload,
    RD.drawable.ic_action_search_dark,
    RD.drawable.ic_action_settings,
    RD.drawable.ic_action_share_outline,
    RD.drawable.ic_action_share_fill,
    RD.drawable.ic_action_shrink,
    RD.drawable.ic_action_star,
    RD.drawable.ic_action_trash,
    RD.drawable.ic_action_unlock,
    RD.drawable.ic_action_upload,
    RD.drawable.ic_action_user,
    RD.drawable.ic_action_zoom_in,
    RD.drawable.ic_action_zoom_out,
    RD.drawable.ic_action_support,
    RD.drawable.ic_action_history,
    RD.drawable.ic_action_call_center,
    RD.drawable.ic_action_receipt,
    RD.drawable.ic_action_gift,
    RD.drawable.ic_action_file,
    RD.drawable.ic_action_page
)

private val informationIcons = listOf(
    RD.drawable.ic_informational_calendar,
    RD.drawable.ic_informational_chat,
    RD.drawable.ic_informational_checkmark,
    RD.drawable.ic_informational_credit_card,
    RD.drawable.ic_informational_grid,
    RD.drawable.ic_informational_help,
    RD.drawable.ic_informational_image,
    RD.drawable.ic_informational_info,
    RD.drawable.ic_informational_list,
    RD.drawable.ic_informational_location,
    RD.drawable.ic_informational_mail,
    RD.drawable.ic_informational_size_chart,
    RD.drawable.ic_informational_store,
    RD.drawable.ic_informational_warning,
    RD.drawable.ic_informational_clock,
    RD.drawable.ic_informational_authentication,
    RD.drawable.ic_informational_sale,
    RD.drawable.ic_informational_column
)

@Destination
@Composable
fun IconsScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.spacing8),
        columns = GridCells.Adaptive(minSize = 80.dp)
    ) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Text(
                modifier = Modifier.padding(Theme.spacing.spacing12),
                text = "Action",
                style = Theme.typography.heading3
            )
        }
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        }
        items(items = actionIcons) {
            Icon(
                modifier = Modifier
                    .padding(Theme.spacing.spacing4)
                    .size(Theme.iconSize.medium),
                painter = painterResource(id = it),
                contentDescription = null
            )
        }
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        }
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Text(
                modifier = Modifier.padding(Theme.spacing.spacing12),
                text = "Information",
                style = Theme.typography.heading3
            )
        }
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        }
        items(items = informationIcons) {
            Icon(
                modifier = Modifier
                    .padding(Theme.spacing.spacing4)
                    .size(Theme.iconSize.medium),
                painter = painterResource(id = it),
                contentDescription = null
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IconsScreenPreview() {
    Theme {
        val topBarState = TopBarState(
            title = TopBarTitle.Text("Icons Screen"),
            showNavigationIcon = false
        )
        IconsScreen(topBarState = topBarState)
    }
}
