package au.com.alfie.ecomm.feature.shop.ui

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
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.shop.R
import au.com.alfie.ecomm.designsystem.R as RD

@Composable
internal fun ShopErrorScreen(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(Theme.iconSize.xLarge),
            painter = painterResource(id = RD.drawable.ic_informational_warning),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        Text(
            text = text,
            style = Theme.typography.paragraphLarge
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing16))
        Text(text = stringResource(R.string.shop_error_please_try_again_later))
    }
}

@Composable
@Preview(showBackground = true)
private fun ShopErrorScreenPreview() {
    ShopErrorScreen(text = "Could not load data")
}
