package au.com.alfie.ecomm.designsystem.component.searchbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun SearchBarWithCancelButton(
    onTermChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    shouldCloseOnSearchAction: Boolean = true,
    searchFieldHorizontalPadding: Dp = Theme.spacing.spacing16,
    cancelButtonEndPadding: Dp = Theme.spacing.spacing16
) {
    val state = rememberSearchState(
        onSearchTermChange = onTermChange,
        placeholderText = placeholderText,
        shouldCloseOnSearchAction = shouldCloseOnSearchAction
    )
    state.updateSearchType(searchTextType = SearchTextType.Soft)

    Row(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        SearchTextField(
            modifier = Modifier
                .padding(horizontal = searchFieldHorizontalPadding)
                .weight(1f),
            state = state
        )
        AnimatedVisibility(visible = state.isSearchOpen) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = cancelButtonEndPadding)
                    .clip(shape = Theme.shape.extraSmall)
                    .clickable { state.invertSearchOpenState() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.search_cancel),
                    style = Theme.typography.small,
                    color = Theme.color.primary.mono900
                )
            }
        }
    }
}
