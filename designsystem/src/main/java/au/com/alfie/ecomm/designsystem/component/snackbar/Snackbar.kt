package au.com.alfie.ecomm.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

private val HEIGHT_SINGLE_LINE = 22.dp
private val HEIGHT_DOUBLE_LINE = 44.dp
private const val SINGLE_LINE = 1
private const val DOUBLE_LINE = 2

@Composable
internal fun Snackbar(
    type: SnackbarType,
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = true,
    singleLine: Boolean = true,
    @DrawableRes icon: Int? = null,
    onActionClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val messageHeight: Dp
    val messagePadding: Dp
    val messageMaxLines: Int

    if (singleLine) {
        messageHeight = remember { HEIGHT_SINGLE_LINE }
        messagePadding = remember { Theme.spacing.spacing16 }
        messageMaxLines = remember { SINGLE_LINE }
    } else {
        messageHeight = remember { HEIGHT_DOUBLE_LINE }
        messagePadding = remember { Theme.spacing.spacing8 }
        messageMaxLines = remember { DOUBLE_LINE }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = type.backgroundColor,
                shape = Theme.shape.small
            )
    ) {
        Spacer(modifier = Modifier.width(Theme.spacing.spacing16))

        icon?.let {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = type.contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(Theme.spacing.spacing8))
        }

        Text(
            text = message,
            style = Theme.typography.paragraph,
            color = type.contentColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = messageMaxLines,
            modifier = Modifier
                .padding(vertical = messagePadding)
                .sizeIn(minHeight = messageHeight)
                .wrapContentHeight()
                .weight(1f)
        )

        actionLabel?.let {
            Text(
                text = it,
                style = Theme.typography.paragraphBold,
                color = type.contentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(Theme.spacing.spacing8)
                    .clickable {
                        onActionClick()
                        onDismiss()
                    }
            )
        }

        if (withDismissAction) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_action_close_dark),
                    contentDescription = null,
                    tint = type.contentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.width(Theme.spacing.spacing8))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SnackbarPreview() {
    val longMessage = "Action has occurred lorem ipsum maria joao jose jabuticaba laranja morango"
    val shortMessage = "Action has occurred"

    Column {
        Snackbar(
            type = SnackbarType.Info,
            message = longMessage,
            icon = R.drawable.ic_informational_checkmark,
            actionLabel = "Undo",
            withDismissAction = true
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Snackbar(
            type = SnackbarType.Info,
            message = longMessage,
            icon = R.drawable.ic_informational_checkmark,
            actionLabel = "Undo",
            singleLine = false
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Snackbar(
            type = SnackbarType.Info,
            message = longMessage,
            withDismissAction = false
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Snackbar(
            type = SnackbarType.Info,
            message = longMessage,
            actionLabel = "Undo",
            withDismissAction = false
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Snackbar(
            type = SnackbarType.Info,
            message = longMessage
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Snackbar(
            type = SnackbarType.Info,
            message = shortMessage,
            singleLine = false
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Snackbar(
            type = SnackbarType.Success,
            message = shortMessage,
            icon = R.drawable.ic_informational_checkmark,
            actionLabel = "Undo"
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Snackbar(
            type = SnackbarType.Error,
            message = shortMessage,
            icon = R.drawable.ic_informational_checkmark,
            actionLabel = "Undo"
        )
    }
}
