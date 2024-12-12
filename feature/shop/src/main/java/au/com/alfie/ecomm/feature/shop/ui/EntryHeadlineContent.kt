package au.com.alfie.ecomm.feature.shop.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import au.com.alfie.ecomm.core.commons.extension.nextFloat
import au.com.alfie.ecomm.core.ui.extension.ItemWithUpdate
import au.com.alfie.ecomm.designsystem.component.shimmer.shimmer
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlin.random.Random

@Composable
internal fun LazyItemScope.EntryHeadlineContent(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by remember { mutableFloatStateOf(Random.nextFloat(Theme.scale.scale40, Theme.scale.scale60)) }
    ItemWithUpdate(targetState = isLoading) { isPlaceholder ->
        Text(
            modifier = modifier
                .fillMaxWidth()
                .shimmer(
                    isShimmering = isPlaceholder,
                    xScale = scale
                ),
            text = if (isPlaceholder) "" else text,
            style = Theme.typography.paragraph
        )
    }
}
