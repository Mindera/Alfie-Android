package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class BagUiFactoryTest {

    @InjectMockKs
    private lateinit var uiFactory: BagUiFactory

    @Test
    fun `map bag to ui xSmall`() = runTest {
        val items = uiFactory(
            bagProducts = bagProducts,
            products = products,
            onRemoveClick = { }
        )
        val expected = bagProductUi.map {
            BagProductUi(
                id = it.id,
                productCardData = ProductCardType.XSmall(
                    image = it.productCardData.image,
                    brand = it.productCardData.brand,
                    name = it.productCardData.name,
                    price = it.productCardData.price,
                    color = (it.productCardData as ProductCardType.XSmall).color,
                    size = (it.productCardData as ProductCardType.XSmall).size
                )
            )
        }
        val result = items.map {
            BagProductUi(
                id = it.id,
                productCardData = ProductCardType.XSmall(
                    image = it.productCardData.image,
                    brand = it.productCardData.brand,
                    name = it.productCardData.name,
                    price = it.productCardData.price,
                    color = (it.productCardData as ProductCardType.XSmall).color,
                    size = (it.productCardData as ProductCardType.XSmall).size
                )
            )
        }

        assertEquals(expected, result)
    }
}