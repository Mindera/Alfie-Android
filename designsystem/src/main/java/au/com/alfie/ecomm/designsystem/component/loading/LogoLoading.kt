package au.com.alfie.ecomm.designsystem.component.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LogoLoading(
    modifier: Modifier = Modifier,
    iconSize: Dp? = null,
    label: String? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        GlideImage(
            model = R.drawable.ic_animated_logo,
            contentDescription = null,
            modifier = Modifier
                .run {
                    if (iconSize == null) {
                        wrapContentSize()
                    } else {
                        size(iconSize)
                    }
                }
        )

        label?.let {
            Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
            Text(
                text = it,
                style = Theme.typography.paragraph,
                color = Theme.color.primary.mono900
            )
        }
    }
}

@Preview
@Composable
private fun LogoLoadingPreview() {
    LogoLoading()
}
