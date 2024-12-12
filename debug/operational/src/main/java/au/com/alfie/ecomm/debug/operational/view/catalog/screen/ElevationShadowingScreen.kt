package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.shadow.ShadowType
import au.com.alfie.ecomm.designsystem.component.shadow.shadow
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ElevationShadowingScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(Theme.spacing.spacing16)
    ) {
        ShadowRow(
            title = "Elevation",
            elements = mapOf(
                ShadowType.Elevation1 to "1",
                ShadowType.Elevation2 to "2",
                ShadowType.Elevation3 to "3",
                ShadowType.Elevation4 to "4",
                ShadowType.Elevation5 to "5"
            )
        )

        ShadowRow(
            title = "Medium Float",
            elements = mapOf(
                ShadowType.MediumFloat1 to "1",
                ShadowType.MediumFloat2 to "2",
                ShadowType.MediumFloat3 to "3",
                ShadowType.MediumFloat4 to "4",
                ShadowType.MediumFloat5 to "5"
            )
        )

        ShadowRow(
            title = "Soft Float",
            elements = mapOf(
                ShadowType.SoftFloat1 to "1",
                ShadowType.SoftFloat2 to "2",
                ShadowType.SoftFloat3 to "3",
                ShadowType.SoftFloat4 to "4",
                ShadowType.SoftFloat5 to "5"
            )
        )
    }
}

@Composable
private fun ShadowRow(
    title: String,
    elements: Map<ShadowType, String>
) {
    Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
    Text(text = title, style = Theme.typography.heading2)
    Spacer(modifier = Modifier.height(Theme.spacing.spacing8))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.spacing20),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        elements.forEach {
            ShadowItem(shadowType = it.key, label = it.value)
        }
    }
}

@Composable
private fun ShadowItem(shadowType: ShadowType, label: String) {
    val colors = CardColors(
        containerColor = Theme.color.white,
        contentColor = Theme.color.black,
        disabledContainerColor = Theme.color.white,
        disabledContentColor = Theme.color.black
    )
    Card(
        shape = Theme.shape.small,
        colors = colors,
        modifier = Modifier
            .size(50.dp)
            .shadow(
                shadowType = shadowType,
                cornersRadius = 8.dp
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = Theme.typography.paragraphBold
            )
        }
    }
}
