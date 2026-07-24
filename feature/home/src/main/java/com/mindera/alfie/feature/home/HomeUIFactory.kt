package com.mindera.alfie.feature.home

import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.highlights.HighlightsItem
import com.mindera.alfie.feature.home.model.HomeUI
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Inject

internal class HomeUIFactory @Inject constructor() {

    // TODO: get real data — Home content (incl. Highlights slides) is mocked until the BFF endpoint is ready.
    operator fun invoke() = HomeUI(
        userName = null,
        membershipDate = null,
        highlights = mockHighlights()
    )

    // Static placeholder slides for the hero carousel (visual-only; no BFF/navigation wiring yet).
    private fun mockHighlights() = persistentListOf(
        HighlightsItem(
            image = ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Large(
                        "https://images.pexels.com/photos/1926769/pexels-photo-1926769.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                    )
                ),
                alt = ""
            ),
            title = StringResource.fromText("Transcending Trends\nfor Breezy Nights"),
            actionText = StringResource.fromText("Explore Collection"),
            onActionClick = { }
        ),
        HighlightsItem(
            image = ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Large(
                        "https://images.pexels.com/photos/2983464/pexels-photo-2983464.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                    )
                ),
                alt = ""
            ),
            title = StringResource.fromText("Autumn Essentials\nfor Every Day"),
            actionText = StringResource.fromText("Explore Collection"),
            onActionClick = { }
        ),
        HighlightsItem(
            image = ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Large(
                        "https://images.pexels.com/photos/1183266/pexels-photo-1183266.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                    )
                ),
                alt = ""
            ),
            title = StringResource.fromText("The Winter Edit\nis Here"),
            actionText = StringResource.fromText("Explore Collection"),
            onActionClick = { }
        )
    )
}
