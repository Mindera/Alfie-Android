package au.com.alfie.ecomm.designsystem.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.core.ui.media.image.pickImageUrlBySize
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint.ParentWidth
import au.com.alfie.ecomm.designsystem.component.image.ratio.Ratio
import au.com.alfie.ecomm.designsystem.component.image.ratio.aspectRatio
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@Composable
fun Image(
    imageUI: ImageUI,
    modifier: Modifier = Modifier,
    contentDescription: String? = imageUI.alt,
    ratio: Ratio = Ratio.RATIO3x4,
    constraint: DimensionConstraint = ParentWidth,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    @DrawableRes failure: Int = R.drawable.ic_informational_image
) {
    BoxWithConstraints(
        modifier = modifier then Modifier.aspectRatio(
            ratioWidth = ratio.ratioWidth,
            ratioHeight = ratio.ratioHeight,
            reference = constraint
        )
    ) {
        val width by remember { derivedStateOf { constraints.maxWidth } }
        val url = remember(imageUI, width) { imageUI.pickImageUrlBySize(width) }

        Image(
            url = url,
            modifier = Modifier.fillMaxSize(),
            contentDescription = contentDescription,
            ratio = ratio,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            failure = failure
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Image(
    url: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    ratio: Ratio = Ratio.RATIO3x4,
    constraint: DimensionConstraint = ParentWidth,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    @DrawableRes failure: Int = R.drawable.ic_informational_image
) {
    GlideImage(
        model = url,
        modifier = modifier then Modifier.aspectRatio(
            ratioWidth = ratio.ratioWidth,
            ratioHeight = ratio.ratioHeight,
            reference = constraint
        ),
        contentDescription = contentDescription,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        loading = loadingPlaceholder(),
        failure = placeholder(failure)
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
private fun loadingPlaceholder() = placeholder {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shimmer(true)
    )
}
