package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import au.com.alfie.ecomm.core.ui.media.GalleryUI
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.component.bottomcard.BottomCard
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.gallery.Gallery
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio
import au.com.alfie.ecomm.designsystem.component.modal.BottomSheet
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Destination
@Composable
fun BottomCardScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var isFullscreen by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    BottomCard(
        backLayer = {
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            Gallery(
                gallery = createGallery(),
                ratio = Ratio.RATIO3x4,
                constraint = DimensionConstraint.ParentWidth,
                isFullscreen = isFullscreen,
                onClick = {
                    isFullscreen = true
                },
                onDismissFullscreen = {
                    isFullscreen = false
                }
            )
        },
        frontLayer = {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
                Text(
                    text = "COUNTRY ROAD",
                    style = Theme.typography.heading2,
                    modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
                )
                Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
                Text(
                    text = "VERIFIED AUSTRALIAN COTTON HERITAGE SWEAT",
                    style = Theme.typography.paragraph,
                    modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
                )
                Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
                Button(
                    type = ButtonType.Underlined,
                    text = "Open Size Guide",
                    onClick = { showBottomSheet = true },
                    modifier = Modifier.padding(horizontal = Theme.spacing.spacing12)
                )
                Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
                repeat(times = 100) {
                    Text(
                        text = "Lorem Ipsum $it",
                        style = Theme.typography.paragraph,
                        modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
                    )
                    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
                }
            }
        },
        bottomStickyLayer = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Theme.spacing.spacing16),
                type = ButtonType.Primary,
                text = "Add to Bag",
                buttonSize = ButtonSize.Large,
                onClick = {
                    coroutineScope.launch {
                        scrollState.scrollTo(0)
                        peek()
                    }
                }
            )
        }
    )

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
                ModalContent()
                Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
            }
        }
    }
}

@Composable
private fun ModalContent() {
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
}

private fun createGallery() = GalleryUI(
    persistentListOf(
        ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/9603628/pexels-photo-9603628.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/14641596/pexels-photo-14641596.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/9603630/pexels-photo-9603630.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/9603630/pexels-photo-9603630.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        )
    )
)
