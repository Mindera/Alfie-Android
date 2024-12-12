package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.button.IconButton
import au.com.alfie.ecomm.designsystem.component.button.IconPosition
import au.com.alfie.ecomm.designsystem.component.image.ratio.aspectRatio
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import au.com.alfie.ecomm.designsystem.R as RD

private const val SHIMMER_TIME = 8000L

@Destination
@Composable
fun ShimmerScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)
    var isShimmering by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(SHIMMER_TIME)
            isShimmering = !isShimmering
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Theme.spacing.spacing16)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Skeleton Animation",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Image(
            painter = painterResource(id = R.drawable.ic_product_sample),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(ratioWidth = 3f, ratioHeight = 4f)
                .fillMaxWidth()
                .shimmer(isShimmering)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Text(
            text = "TOMMY HILFIGER",
            style = Theme.typography.heading2,
            modifier = Modifier.shimmer(isShimmering)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Text(
            text = "TH CITY TOTE",
            style = Theme.typography.paragraph,
            modifier = Modifier.shimmer(isShimmering)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Text(
            text = "For busy days on the move, choose this timeless tote featuring a handy removable coin purse to store your on-the-go cash. \n" +
                "\n" +
                "Highlights\n" +
                "\n" +
                "- Smooth finish\n" +
                "- Detachable coin purse\n" +
                "- Two top handles\n" +
                "- One main compartment\n" +
                "- TH monogram plaque on front\n" +
                "- Tommy Hilfiger branding\n" +
                "\n" +
                "Size & Fit\n" +
                "\n" +
                "- 32 x 39 x 19cm\n" +
                "- Capacity 22L\n" +
                "\n" +
                "Composition & Care\n" +
                "\n" +
                "- 100% polyurethane",
            style = Theme.typography.paragraph,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(
                    isShimmering = isShimmering,
                    lines = 18,
                    lineHeight = Theme.typography.paragraph.lineHeight,
                    lastLineFraction = .5f
                )
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                type = ButtonType.Primary,
                text = "Add To Bag",
                isShimmering = isShimmering,
                iconButton = IconButton(iconResource = RD.drawable.ic_action_bag, position = IconPosition.Right),
                buttonSize = ButtonSize.Medium,
                onClick = {}
            )
            Spacer(modifier = Modifier.width(Theme.spacing.spacing8))
            Button(
                type = ButtonType.Secondary,
                text = "Find in Store",
                isShimmering = isShimmering,
                iconButton = IconButton(iconResource = RD.drawable.ic_informational_store, position = IconPosition.Right),
                buttonSize = ButtonSize.Medium,
                onClick = {}
            )
        }
    }
}
