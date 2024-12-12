package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import au.com.alfie.ecomm.core.ui.media.GalleryUI
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.debug.operational.view.catalog.util.SwitchItem
import au.com.alfie.ecomm.designsystem.component.gallery.Gallery
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint.ParentWidth
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio.RATIO3x4
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Destination
@Composable
internal fun GalleryScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)
    var isFullscreen by remember { mutableStateOf(false) }
    var isSingleImage by remember { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        // Using if with two different Gallery to force the pager state re-creation
        if (isSingleImage) {
            Gallery(
                gallery = createGallery().let { it.copy(it.take(1).toImmutableList()) },
                ratio = RATIO3x4,
                constraint = ParentWidth,
                isFullscreen = isFullscreen,
                onClick = {
                    isFullscreen = true
                },
                onDismissFullscreen = {
                    isFullscreen = false
                }
            )
        } else {
            Gallery(
                gallery = createGallery(),
                ratio = RATIO3x4,
                constraint = ParentWidth,
                isFullscreen = isFullscreen,
                onClick = {
                    isFullscreen = true
                },
                onDismissFullscreen = {
                    isFullscreen = false
                }
            )
        }
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
        SwitchItem(
            text = "Single image",
            isChecked = isSingleImage,
            onCheckChange = { isSingleImage = it }
        )
    }
}

private fun createGallery() = GalleryUI(
    persistentListOf(
        ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/9603630/pexels-photo-9603630.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/5603243/pexels-photo-5603243.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/9603627/pexels-photo-9603627.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        ),
        ImageUI(
            images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/9362029/pexels-photo-9362029.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
            alt = ""
        )
    )
)
