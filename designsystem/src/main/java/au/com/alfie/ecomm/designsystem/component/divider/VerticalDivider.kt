package au.com.alfie.ecomm.designsystem.component.divider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun VerticalDivider(
    dividerType: DividerType,
    modifier: Modifier = Modifier
) {
    VerticalDivider(
        modifier = modifier,
        color = dividerType.color,
        thickness = dividerType.thickness
    )
}

@Preview(showBackground = true)
@Composable
private fun DividerPreview() {
    Theme {
        Column {
            VerticalDivider(dividerType = DividerType.Solid1Mono100)
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            VerticalDivider(dividerType = DividerType.Solid2Mono100)
        }
    }
}
