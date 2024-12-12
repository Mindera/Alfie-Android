package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.designsystem.component.image.Image
import au.com.alfie.ecomm.designsystem.component.pulldown.PullDownLayout
import au.com.alfie.ecomm.designsystem.component.searchbar.rememberSearchState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBar
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.search.SearchOverlay
import com.ramcosta.composedestinations.annotation.Destination

val cardColors = CardColors(
    containerColor = Theme.color.primary.mono200,
    contentColor = Theme.color.primary.mono900,
    disabledContainerColor = Theme.color.white,
    disabledContentColor = Theme.color.black
)
private val imagesList = listOf(
    "https://images.pexels.com/photos/6667911/pexels-photo-6667911.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/991509/pexels-photo-991509.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/2364575/pexels-photo-2364575.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/1377457/pexels-photo-1377457.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/428340/pexels-photo-428340.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/1227699/pexels-photo-1227699.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/6786614/pexels-photo-6786614.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
)
private const val COLUMN_COUNT = 2

@Destination
@Composable
internal fun SearchPullDownScreen(
    topBarState: TopBarState,
    navController: NavController,
    directionProvider: DirectionProvider
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    var onPullDownToRefresh by remember { mutableStateOf(false) }

    val overlayState = TopBarState(
        title = TopBarTitle.Search(
            isPullDownToRefresh = onPullDownToRefresh,
            searchState = rememberSearchState(),
            onTermChanged = { /* Do nothing here */ },
            onFocusChange = {
                onPullDownToRefresh = false
            },
            customOverlay = { padding, appContent ->
                SearchOverlay(
                    onSearchAction = { /* Do nothing here */ },
                    onUpdateSearchTerm = { /* Do nothing here */ },
                    isOpen = onPullDownToRefresh,
                    onDismiss = {
                        onPullDownToRefresh = false
                    },
                    navController = navController,
                    directionProvider = directionProvider,
                    content = {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            containerColor = Theme.color.white
                        ) { innerPadding ->
                            appContent(innerPadding)
                        }
                    }
                )
            }
        ),
        showNavigationIcon = false
    )

    Column {
        TopBar(
            state = overlayState,
            onNavigationClick = {}
        )

        PullDownLayout(
            modifier = Modifier.fillMaxSize(),
            onRefresh = {
                onPullDownToRefresh = true
            },
            content = {
                ProductCardGridComponent()
            }
        )
    }
}

@Composable
private fun ProductCardGridComponent() {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(COLUMN_COUNT),
        contentPadding = PaddingValues(Theme.spacing.spacing16),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        itemsIndexed(items = imagesList) { index, item ->
            Card(
                shape = Theme.shape.small,
                colors = cardColors,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(url = item)
                    Text(
                        text = "Product $index",
                        style = Theme.typography.paragraphBold,
                        modifier = Modifier.padding(Theme.spacing.spacing6)
                    )
                    Text(
                        text = "Description $index",
                        style = Theme.typography.paragraphBold,
                        modifier = Modifier.padding(Theme.spacing.spacing12)
                    )
                }
            }
        }
    }
}
