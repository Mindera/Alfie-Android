package com.mindera.alfie.feature.snackbar

import android.content.Context
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.feature.R

/**
 * The DS Toast that confirms a user action ("Added." / "Removed.") with a single link. Every screen
 * builds it from here so the copy and the link stay the same wherever the action happens.
 */
object ActionToast {

    /** Confirms a wishlist toggle from a product card or the PDP, linking to the Wishlist. */
    fun wishlistUpdated(
        context: Context,
        added: Boolean,
        onViewWishlist: () -> Unit
    ): SnackbarCustomVisuals.Toast = SnackbarCustomVisuals.Toast(
        message = context.getString(if (added) R.string.action_toast_added else R.string.action_toast_removed),
        actionLabel = context.getString(R.string.action_toast_view_wishlist),
        onActionClick = onViewWishlist
    )

    /** Confirms an add to bag, linking to the Bag. */
    fun addedToBag(
        context: Context,
        onViewBag: () -> Unit
    ): SnackbarCustomVisuals.Toast = SnackbarCustomVisuals.Toast(
        message = context.getString(R.string.action_toast_added),
        actionLabel = context.getString(R.string.action_toast_view_bag),
        onActionClick = onViewBag
    )

    /** Confirms a removal from a list screen, offering to put the item back. */
    fun removed(
        context: Context,
        onUndo: () -> Unit
    ): SnackbarCustomVisuals.Toast = SnackbarCustomVisuals.Toast(
        message = context.getString(R.string.action_toast_removed),
        actionLabel = context.getString(R.string.action_toast_undo),
        onActionClick = onUndo
    )
}
