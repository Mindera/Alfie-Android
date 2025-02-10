package au.com.alfie.ecomm.designsystem.component.searchbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.commons.extension.isNotNullOrBlank
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.test.SEARCH_CLEAR_BUTTON
import au.com.alfie.ecomm.core.ui.test.SEARCH_INPUT
import au.com.alfie.ecomm.designsystem.animation.DefaultVisibilityAnimation
import au.com.alfie.ecomm.designsystem.animation.defaultFadeIn
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
internal fun SearchTextField(
    state: SearchState,
    modifier: Modifier = Modifier,
    isPullDownToRefresh: Boolean = false,
    isEnabled: Boolean = true,
    onFocusChange: (Boolean) -> Unit = {},
    onTermChange: (String) -> Unit = {},
    onClick: ClickEvent = {}
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val isSearchOpen = state.isSearchOpen
    val type = state.searchTextType
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSearchOpen, isPullDownToRefresh) {
        if (isSearchOpen || isPullDownToRefresh) {
            focusRequester.requestFocus()
        } else {
            focusRequester.freeFocus()
            focusManager.clearFocus(force = true)
            state.updateSearchTerm(searchText = "")
        }
    }

    Surface(
        modifier = modifier,
        color = if (isSearchOpen) type.selectedColor else type.unselectedColor,
        shape = Theme.shape.full,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSearchOpen) type.selectedBorderColor else type.unselectedBorderColor
        )
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        if (!isSearchOpen) {
            keyboardController?.hide()
        }

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    onFocusChange(it.isFocused)
                    if (it.isFocused && state.isSearchOpen.not()) {
                        state.invertSearchOpenState()
                    }
                }
                .run {
                    if (isEnabled) {
                        this
                    } else {
                        this.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onClick()
                        }
                    }
                }
                .testTag(SEARCH_INPUT),
            value = state.searchTerm,
            onValueChange = { term ->
                onTermChange(term)
                state.updateSearchTerm(term)
            },
            singleLine = true,
            maxLines = 1,
            textStyle = type.textStyle.copy(color = type.textColor),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    state.onSearchAction()
                    keyboardController?.hide()
                }
            ),
            cursorBrush = SolidColor(type.cursorColor),
            decorationBox = { innerTextField ->
                DecorationBox(
                    type = type,
                    searchState = state,
                    innerTextField = innerTextField
                )
            },
            enabled = isEnabled
        )
    }
}

@Composable
private fun DecorationBox(
    type: SearchTextType,
    searchState: SearchState,
    innerTextField: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .padding(
                horizontal = type.horizontalPadding,
                vertical = type.verticalPadding
            )
            .indication(
                interactionSource = interactionSource,
                indication = ripple(bounded = false)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1F)
        ) {
            DefaultVisibilityAnimation(isVisible = searchState.searchTerm.isEmpty()) {
                Text(
                    text = searchState.placeholderText,
                    style = type.textStyle,
                    color = type.placeholderTextColor,
                    maxLines = 1
                )
            }
            innerTextField()
        }

        Spacer(modifier = Modifier.width(Theme.spacing.spacing6))

        AnimatedContent(
            targetState = searchState.searchTerm.isNotNullOrBlank(),
            transitionSpec = {
                defaultFadeIn() togetherWith fadeOut()
            },
            label = "SearchIconClearAnimation"
        ) {
            if (it) {
                IconButton(
                    modifier = Modifier
                        .size(Theme.iconSize.medium)
                        .testTag(SEARCH_CLEAR_BUTTON),
                    onClick = {
                        searchState.updateSearchTerm("")
                    }
                ) {
                    Icon(
                        painter = painterResource(id = type.clearIcon),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = type.searchIcon),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFF)
@Composable
private fun SearchTextFieldPreview() {
    SearchTextField(
        state = rememberSearchState()
    )
}
