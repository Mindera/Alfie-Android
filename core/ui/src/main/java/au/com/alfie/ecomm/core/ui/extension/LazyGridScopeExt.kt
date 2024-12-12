package au.com.alfie.ecomm.core.ui.extension

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

fun <T : Any> LazyGridScope.items(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    contentType: ((item: T) -> Any?)? = null,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key),
        contentType = items.itemContentType(contentType)
    ) { index ->
        itemContent(items[index])
    }
}

fun <T : Any> LazyGridScope.itemsIndexed(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    contentType: ((item: T) -> Any?)? = null,
    itemContent: @Composable LazyGridItemScope.(index: Int, value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key),
        contentType = items.itemContentType(contentType)
    ) { index ->
        itemContent(index, items[index])
    }
}

fun LazyGridScope.expandableSection(
    isEnabled: Boolean,
    header: @Composable (isEnabled: Boolean) -> Unit,
    expandable: LazyGridScope.() -> Unit,
    toggle: () -> Unit
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Box(
            modifier = Modifier.clickable(enabled = isEnabled) { toggle() }
        ) {
            header(isEnabled)
        }
    }

    expandable()
}

fun <T> LazyGridScope.expandableItems(
    isExpanded: Boolean,
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    content: @Composable (item: T) -> Unit
) {
    items(
        items = items,
        key = key
    ) { item ->
        ExpandableItem(isExpanded) {
            content(item)
        }
    }
}

fun <T : Any> LazyGridScope.expandableItems(
    isExpanded: Boolean,
    items: LazyPagingItems<T>,
    key: (item: T) -> Any,
    content: @Composable (item: T?) -> Unit
) {
    items(
        items = items,
        key = key
    ) { item ->
        ExpandableItem(isExpanded) {
            content(item)
        }
    }
}

fun LazyGridScope.expandableItem(
    isExpanded: Boolean,
    span: (LazyGridItemSpanScope.() -> GridItemSpan),
    content: @Composable () -> Unit
) {
    item(span = span) {
        ExpandableItem(isExpanded = isExpanded) {
            content()
        }
    }
}

@Composable
private fun LazyGridItemScope.ExpandableItem(
    isExpanded: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn(animationSpec = tween()) + expandVertically(animationSpec = tween()),
        exit = fadeOut(animationSpec = tween()) + shrinkVertically(animationSpec = tween())
    ) {
        content()
    }
}
