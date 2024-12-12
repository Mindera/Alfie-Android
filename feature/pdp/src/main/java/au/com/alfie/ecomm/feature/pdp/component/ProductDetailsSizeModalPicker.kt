package au.com.alfie.ecomm.feature.pdp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.component.modal.BottomSheet
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonProperties
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonState
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.pdp.R
import au.com.alfie.ecomm.feature.pdp.model.SizeUI
import kotlinx.collections.immutable.ImmutableList
import au.com.alfie.ecomm.designsystem.R as RD

@Composable
internal fun ProductDetailsSizeModalPicker(
    sizes: ImmutableList<SizeUI>,
    selectedSize: SizeUI?,
    onSizeClick: ClickEventOneArg<SizeUI>,
    onDismiss: ClickEvent
) {
    BottomSheet(
        title = stringResource(id = R.string.product_details_size_picker_title),
        onDismiss = onDismiss
    ) {
        HorizontalDivider(dividerType = DividerType.Solid1Mono200)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Theme.spacing.spacing16)
        ) {
            items(sizes) { sizeUI ->
                SizePickerItem(
                    sizeUI = sizeUI,
                    selectedSize = selectedSize,
                    onClick = onSizeClick
                )
            }
        }
    }
}

@Composable
private fun SizePickerItem(
    sizeUI: SizeUI,
    selectedSize: SizeUI?,
    onClick: ClickEventOneArg<SizeUI>
) {
    val hasStock = sizeUI.properties.state == SizingButtonState.Selectable
    val textColor = if (hasStock) Theme.color.primary.mono900 else Theme.color.primary.mono300

    Column(Modifier.fillMaxWidth()) {
        ListItem(
            modifier = Modifier.clickable(enabled = hasStock) { onClick(sizeUI) },
            headlineContent = {
                Text(
                    text = sizeUI.properties.text,
                    style = Theme.typography.paragraph,
                    color = textColor
                )
            },
            trailingContent = when {
                hasStock.not() -> {
                    {
                        Text(
                            text = stringResource(id = R.string.product_details_size_out_of_stock),
                            style = Theme.typography.tiny,
                            color = textColor
                        )
                    }
                }

                sizeUI.id == selectedSize?.id -> {
                    {
                        Icon(
                            modifier = Modifier.size(Theme.iconSize.small),
                            painter = painterResource(id = RD.drawable.ic_informational_checkmark),
                            contentDescription = null,
                            tint = Theme.color.primary.mono900
                        )
                    }
                }

                else -> null
            }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16),
            dividerType = DividerType.Solid1Mono100
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SizePickerItemPreview() {
    val sizeWithoutStock = SizeUI(
        id = "1",
        properties = SizingButtonProperties(
            text = "XS",
            state = SizingButtonState.OutOfStock
        )
    )
    val sizeSelected = SizeUI(
        id = "3",
        properties = SizingButtonProperties(
            text = "M",
            state = SizingButtonState.Selectable
        )
    )
    val sizeWithStock = SizeUI(
        id = "2",
        properties = SizingButtonProperties(
            text = "S",
            state = SizingButtonState.Selectable
        )
    )
    Theme {
        Column {
            SizePickerItem(
                sizeUI = sizeWithoutStock,
                selectedSize = sizeSelected,
                onClick = {}
            )
            SizePickerItem(
                sizeUI = sizeWithStock,
                selectedSize = sizeSelected,
                onClick = {}
            )
            SizePickerItem(
                sizeUI = sizeSelected,
                selectedSize = sizeSelected,
                onClick = {}
            )
        }
    }
}
