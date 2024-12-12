package au.com.alfie.ecomm.designsystem.component.divider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun HorizontalDivider(
    dividerType: DividerType,
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
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
            HorizontalDivider(dividerType = DividerType.Solid1Mono100)
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            HorizontalDivider(dividerType = DividerType.Solid2Mono100)
        }
    }
}
