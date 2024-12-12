package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.badge.BadgeType
import au.com.alfie.ecomm.designsystem.component.badge.IconBadge
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBar
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarItem
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarItemState
import au.com.alfie.ecomm.designsystem.component.bottombar.rememberBottomBarItemState
import au.com.alfie.ecomm.designsystem.component.bottombar.rememberBottomBarState
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

@Destination
@Composable
fun BadgeScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Icons()
        Toolbars()
        BottomBar()
    }
}

@Composable
private fun Header(title: String) {
    Column(modifier = Modifier.padding(horizontal = Theme.spacing.spacing8)) {
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = title,
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    }
}

@Composable
private fun Icons() {
    Header(title = "Icons")
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        IconItems(
            badges = listOf(
                BadgeType.Highlight,
                BadgeType.Counter(count = 1),
                BadgeType.Counter(count = 11),
                BadgeType.Counter(count = 111)
            ),
            icon = Icons.Filled.Favorite
        )

        IconItems(
            badges = listOf(
                BadgeType.Highlight,
                BadgeType.Counter(count = 2),
                BadgeType.Counter(count = 22),
                BadgeType.Counter(count = 222)
            ),
            icon = Icons.Filled.Home
        )

        IconItems(
            badges = listOf(
                BadgeType.Highlight,
                BadgeType.Counter(count = 3),
                BadgeType.Counter(count = 33),
                BadgeType.Counter(count = 333)
            ),
            icon = Icons.Filled.AccountCircle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbars() {
    Header(title = "Toolbar")
    TopAppBar(
        title = { Text(text = "Alfie") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconBadge(
                modifier = Modifier.width(46.dp),
                badge = BadgeType.Highlight
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = ""
                )
            }
        },
        modifier = Modifier
    )

    TopAppBar(
        title = { Text(text = "Alfie") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconBadge(
                modifier = Modifier.width(46.dp),
                badge = BadgeType.Counter(1)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = ""
                )
            }
        },
        modifier = Modifier
    )
}

@Composable
private fun BottomBar() {
    Header(title = "BottomBar")
    var selectedIndex by remember { mutableIntStateOf(0) }
    var bagCount by remember { mutableIntStateOf(1) }

    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            type = ButtonType.Primary,
            text = "+1 To Bag",
            onClick = { bagCount += 1 }
        )
        Button(
            type = ButtonType.Primary,
            text = "+100 To Bag",
            onClick = { bagCount += 100 }
        )
        Button(
            type = ButtonType.Primary,
            text = "Reset",
            onClick = { bagCount = 0 }
        )
    }
    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

    BottomBar(
        state = rememberBottomBarState(),
        items = bottomBarItems(selectedIndex, bagCount),
        onItemClick = { index, _ -> selectedIndex = index }
    )
}

@Composable
private fun RowScope.IconItems(
    badges: List<BadgeType>,
    icon: ImageVector
) {
    Column(
        modifier = Modifier.weight(1F),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        badges.forEach {
            IconBadge(
                badge = it
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun bottomBarItems(
    selectedIndex: Int,
    bagCount: Int
) = persistentListOf(
    object : BottomBarItem {
        override val state: BottomBarItemState = rememberBottomBarItemState().apply {
            updateSelectedState(selectedIndex == 0)
        }
        override val icon: Int = R.drawable.ic_action_house
        override val label: StringResource = StringResource.fromText("Home")
        override val testTag: String = ""
    },
    object : BottomBarItem {
        override val state: BottomBarItemState = rememberBottomBarItemState().apply {
            updateSelectedState(selectedIndex == 1)
            updateBadge(BadgeType.Highlight)
        }
        override val icon: Int = R.drawable.ic_informational_store
        override val label: StringResource = StringResource.fromText("Shop")
        override val testTag: String = ""
    },
    object : BottomBarItem {
        override val state: BottomBarItemState = rememberBottomBarItemState().apply {
            updateSelectedState(selectedIndex == 2)
            updateBadge(BadgeType.Highlight)
        }
        override val icon: Int = R.drawable.ic_action_heart_outline
        override val label: StringResource = StringResource.fromText("Wishlist")
        override val testTag: String = ""
    },
    object : BottomBarItem {
        override val state: BottomBarItemState = rememberBottomBarItemState().apply {
            updateSelectedState(selectedIndex == 3)
            updateBadge(BadgeType.Counter(bagCount))
        }
        override val icon: Int = R.drawable.ic_action_bag
        override val label: StringResource = StringResource.fromText("Bag")
        override val testTag: String = ""
    }
)

@Preview(showBackground = true)
@Composable
private fun BadgeScreenPreview() {
    Theme {
        val topBarState = TopBarState(
            title = TopBarTitle.Text("Badge Screen"),
            showNavigationIcon = false
        )
        BadgeScreen(topBarState = topBarState)
    }
}
