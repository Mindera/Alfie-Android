package com.mindera.alfie.debug.operational.view.catalog.screen

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
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.mindera.alfie.designsystem.R as RD
import com.mindera.alfie.designsystem.icons.AlfieIcons

private val actionIcons = listOf(
    AlfieIcons.LegacyArrowDown,
    AlfieIcons.Back,
    AlfieIcons.LegacyArrowUp,
    AlfieIcons.Forward,
    AlfieIcons.Bag,
    AlfieIcons.Notification,
    AlfieIcons.LegacyBookmark,
    AlfieIcons.LegacyCamera,
    AlfieIcons.ChevronDown,
    AlfieIcons.ChevronLeft,
    AlfieIcons.ChevronRight,
    AlfieIcons.ChevronUp,
    AlfieIcons.Close,
    AlfieIcons.LegacyCopy,
    AlfieIcons.Download,
    AlfieIcons.Pencil,
    AlfieIcons.More,
    AlfieIcons.LegacyExpand,
    AlfieIcons.LegacyExternalLink,
    AlfieIcons.LegacyEye,
    AlfieIcons.LegacyEyeClosed,
    AlfieIcons.LegacyFaceId,
    AlfieIcons.Refine,
    AlfieIcons.Menu,
    AlfieIcons.WishlistFill,
    AlfieIcons.Wishlist,
    AlfieIcons.Home,
    AlfieIcons.LegacyInbox,
    AlfieIcons.LegacyLock,
    AlfieIcons.LegacyLogIn,
    AlfieIcons.Exit,
    AlfieIcons.LegacyMicrophone,
    AlfieIcons.Minus,
    AlfieIcons.Add,
    AlfieIcons.LegacyRefresh,
    AlfieIcons.LegacyReload,
    AlfieIcons.Search,
    AlfieIcons.Settings,
    AlfieIcons.Share,
    AlfieIcons.Share,
    AlfieIcons.LegacyShrink,
    AlfieIcons.Star,
    AlfieIcons.Delete,
    AlfieIcons.LegacyUnlock,
    AlfieIcons.LegacyUpload,
    AlfieIcons.Account,
    AlfieIcons.LegacyZoomIn,
    AlfieIcons.LegacyZoomOut,
    AlfieIcons.LegacySupport,
    AlfieIcons.LegacyHistory,
    AlfieIcons.LegacyCallCenter,
    AlfieIcons.LegacyReceipt,
    AlfieIcons.Gift,
    AlfieIcons.LegacyFile,
    AlfieIcons.LegacyPage
)

private val informationIcons = listOf(
    AlfieIcons.LegacyCalendar,
    AlfieIcons.LegacyChat,
    AlfieIcons.Check,
    AlfieIcons.CreditCard,
    AlfieIcons.Grid2,
    AlfieIcons.Help,
    AlfieIcons.LegacyImage,
    AlfieIcons.LegacyInfo,
    AlfieIcons.LegacyList,
    AlfieIcons.LegacyLocation,
    AlfieIcons.LegacyMail,
    AlfieIcons.LegacySizeChart,
    AlfieIcons.LegacyStore,
    AlfieIcons.AlertFill,
    AlfieIcons.LegacyClock,
    AlfieIcons.LegacyAuthentication,
    AlfieIcons.LegacySale,
    AlfieIcons.Grid1
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
