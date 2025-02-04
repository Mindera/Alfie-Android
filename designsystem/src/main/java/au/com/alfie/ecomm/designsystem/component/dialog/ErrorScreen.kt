package au.com.alfie.ecomm.designsystem.component.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun ErrorScreen(
    message: String,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(Theme.iconSize.xLarge),
                painter = painterResource(id = R.drawable.ic_informational_warning),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
            Text(
                text = message,
                style = Theme.typography.paragraphLarge
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
            Button(
                type = ButtonType.Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Theme.spacing.spacing20),
                onClick = onClick,
                isEnabled = true,
                text = buttonLabel
            )
        }
    }
}