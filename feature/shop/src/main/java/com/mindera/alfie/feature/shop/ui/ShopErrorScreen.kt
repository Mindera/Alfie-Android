package com.mindera.alfie.feature.shop.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.model.ApiErrorType
import com.mindera.alfie.feature.model.toStringRes
import com.mindera.alfie.feature.shop.R
import com.mindera.alfie.feature.R as FeatureR

@Composable
internal fun ShopErrorScreen(
    errorType: ApiErrorType,
    @StringRes customGenericError: Int = FeatureR.string.error_generic,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(Theme.iconSize.xLarge),
            painter = painterResource(id = AlfieIcons.AlertFill),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        Text(
            text = stringResource(
                errorType.toStringRes(
                    genericRes = customGenericError,
                    notFoundRes = R.string.shop_error_not_found
                )
            ),
            style = Theme.typography.paragraphLarge
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
            Button(
                type = ButtonType.Secondary,
                buttonSize = ButtonSize.Medium,
                text = stringResource(FeatureR.string.retry),
                onClick = onRetry
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ShopErrorScreenGenericPreview() {
    Theme {
        ShopErrorScreen(errorType = ApiErrorType.Generic, onRetry = {})
    }
}

@Composable
@Preview(showBackground = true)
private fun ShopErrorScreenThrottledPreview() {
    Theme {
        ShopErrorScreen(errorType = ApiErrorType.Throttled, onRetry = {})
    }
}

@Composable
@Preview(showBackground = true)
private fun ShopErrorScreenNetworkPreview() {
    Theme {
        ShopErrorScreen(errorType = ApiErrorType.Network, onRetry = {})
    }
}
