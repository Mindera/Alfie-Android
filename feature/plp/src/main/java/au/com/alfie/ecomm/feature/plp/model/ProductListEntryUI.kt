package au.com.alfie.ecomm.feature.plp.model

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType

@Stable
internal data class ProductListEntryUI(
    val id: String,
    val productCardData: ProductCardType
)
