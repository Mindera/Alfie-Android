package au.com.alfie.ecomm.core.ui.media

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class GalleryUI(
    val medias: ImmutableList<MediaUI>
) : ImmutableList<MediaUI> by medias {

    companion object {
        val EMPTY = GalleryUI(persistentListOf())
    }
}

fun GalleryUI?.orEmpty(): GalleryUI = this ?: GalleryUI.EMPTY
