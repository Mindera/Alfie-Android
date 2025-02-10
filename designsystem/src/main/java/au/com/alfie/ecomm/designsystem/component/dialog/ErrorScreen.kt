package au.com.alfie.ecomm.designsystem.component.dialog

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun ErrorScreen(
    data: ErrorData
) {
    val context = LocalContext.current
    val noButtonActionMessage = stringResource(R.string.error_screen_button_no_action_message)
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
                text = data.message,
                style = Theme.typography.paragraphLarge
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
            Button(
                type = ButtonType.Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Theme.spacing.spacing20),
                onClick = data.onButtonClick ?: {
                    Toast.makeText(
                        context,
                        noButtonActionMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                isEnabled = true,
                text = data.buttonLabel
            )
        }
    }
}

data class ErrorData(
    val message: String,
    val buttonLabel: String,
    var onButtonClick: (() -> Unit)? = null
)