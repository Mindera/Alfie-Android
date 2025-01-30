package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchGroup
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchSize
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchType.Image
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchType.PlainColor
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.ramcosta.composedestinations.annotation.Destination

private val smallColorSwatchList = listOf(
    PlainColor(Color.Red),
    PlainColor(Color.Blue),
    PlainColor(Color.Gray),
    PlainColor(Color.Green),
    PlainColor(Color.Black)
)
private val smallImageSwatchList = listOf(
    Image("https://people.mindera.com/mindera_favicon_package/favicon-32x32.png"),
    Image("https://www.gstatic.com/devrel-devsite/prod/vb47a36f3a983ed748bf281529457db47955fe57e2b5ea15e7e9641c5e7b5032e/android/images/favicon.png"),
    Image("https://people.mindera.com/mindera_favicon_package/favicon-32x32.png")
)
private val largeColorSwatchList = listOf(
    PlainColor(Color.Red),
    PlainColor(Color.Blue),
    PlainColor(Color.Gray),
    PlainColor(Color.Green),
    PlainColor(Color.Black)
)
private val largeImageSwatchList = listOf(
    Image("https://people.mindera.com/mindera_favicon_package/favicon-32x32.png"),
    Image("https://www.gstatic.com/devrel-devsite/prod/vb47a36f3a983ed748bf281529457db47955fe57e2b5ea15e7e9641c5e7b5032e/android/images/favicon.png"),
    Image("https://people.mindera.com/mindera_favicon_package/favicon-32x32.png")
)
private val largeColorWithDisabledSwatchList = listOf(
    PlainColor(Color.Red),
    PlainColor(Color.Blue, isEnabled = false),
    PlainColor(Color.Gray, isEnabled = false),
    PlainColor(Color.Green, isEnabled = false),
    PlainColor(Color.Black, isEnabled = false)
)
private val mixedSwatchList = listOf(
    PlainColor(Color.Red),
    Image("https://people.mindera.com/mindera_favicon_package/favicon-32x32.png"),
    PlainColor(Color.Blue),
    Image("https://www.gstatic.com/devrel-devsite/prod/vb47a36f3a983ed748bf281529457db47955fe57e2b5ea15e7e9641c5e7b5032e/android/images/favicon.png")
)

@OptIn(ExperimentalGlideComposeApi::class)
@Destination
@Composable
fun SwatchScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .padding(Theme.spacing.spacing16)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Small size with colors",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        var smallSelectedColorIndex by rememberSaveable { mutableIntStateOf(2) }
        SwatchGroup(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing12),
            selectedIndex = smallSelectedColorIndex,
            swatchList = smallColorSwatchList,
            swatchSize = SwatchSize.Small,
            onClick = { smallSelectedColorIndex = it }
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Small With Images",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        var smallSelectedImageIndex by rememberSaveable { mutableIntStateOf(0) }
        SwatchGroup(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing12),
            selectedIndex = smallSelectedImageIndex,
            swatchList = smallImageSwatchList,
            swatchSize = SwatchSize.Small,
            onClick = { smallSelectedImageIndex = it }
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Large size with colors",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        var largeSelectedColorIndex by rememberSaveable { mutableIntStateOf(2) }
        SwatchGroup(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing12),
            selectedIndex = largeSelectedColorIndex,
            swatchList = largeColorSwatchList,
            swatchSize = SwatchSize.Large,
            onClick = { largeSelectedColorIndex = it }
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Large With Images",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        var largeSelectedImageIndex by rememberSaveable { mutableIntStateOf(0) }
        SwatchGroup(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing12),
            selectedIndex = largeSelectedImageIndex,
            swatchList = largeImageSwatchList,
            swatchSize = SwatchSize.Large,
            onClick = { largeSelectedImageIndex = it }
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Large size with colors and some disabled",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        var largeWithDisabledSelectedColorIndex by rememberSaveable { mutableIntStateOf(0) }
        SwatchGroup(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing12),
            selectedIndex = largeWithDisabledSelectedColorIndex,
            swatchList = largeColorWithDisabledSwatchList,
            swatchSize = SwatchSize.Large,
            onClick = { largeWithDisabledSelectedColorIndex = it }
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Large With Images and some disabled",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        val largeImageWithDisabledSwatchList = listOf(
            Image("https://people.mindera.com/mindera_favicon_package/favicon-32x32.png"),
            Image("https://www.gstatic.com/devrel-devsite/prod/vb47a36f3a983ed748bf281529457db47955fe57e2b5ea15e7e9641c5e7b5032e/android/images/favicon.png", isEnabled = false),
            Image("https://people.mindera.com/mindera_favicon_package/favicon-32x32.png", isEnabled = false)
        )
        var largeWithDisabledSelectedImageIndex by rememberSaveable { mutableIntStateOf(0) }
        SwatchGroup(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing12),
            selectedIndex = largeWithDisabledSelectedImageIndex,
            swatchList = largeImageWithDisabledSwatchList,
            swatchSize = SwatchSize.Large,
            onClick = { largeWithDisabledSelectedImageIndex = it }
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Mixed list",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        var selectedMixedIndex by rememberSaveable { mutableIntStateOf(0) }
        SwatchGroup(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing12),
            selectedIndex = selectedMixedIndex,
            swatchList = mixedSwatchList,
            swatchSize = SwatchSize.Large,
            onClick = { selectedMixedIndex = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwatchScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Swatch Screen"),
        showNavigationIcon = false
    )
    Theme {
        SwatchScreen(topBarState = topBarState)
    }
}
