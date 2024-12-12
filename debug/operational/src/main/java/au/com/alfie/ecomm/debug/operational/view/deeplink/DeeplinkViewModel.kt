package au.com.alfie.ecomm.debug.operational.view.deeplink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.debug.operational.view.deeplink.model.DeeplinkEntry
import au.com.alfie.ecomm.debug.operational.view.deeplink.model.DeeplinkSection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DeeplinkViewModel @Inject constructor(
    private val deeplinkHandler: DeeplinkHandler
) : ViewModel() {

    val sections = listOf(
        DeeplinkSection(
            name = "Deep linking - Main tabs",
            entries = listOf(
                DeeplinkEntry(
                    name = "Home",
                    url = "https://www.alfie.com"
                ),
                DeeplinkEntry(
                    name = "Shop",
                    url = "http://www.alfie.com/shop"
                ),
                DeeplinkEntry(
                    name = "Bag",
                    url = "http://www.alfie.com/bag"
                ),
                DeeplinkEntry(
                    name = "Delivery & Returns",
                    url = "https://www.alfie.com/return-options"
                ),
                DeeplinkEntry(
                    name = "Payment Options",
                    url = "https://www.alfie.com/payment-options"
                )
            )
        ),
        DeeplinkSection(
            name = "Deep linking - Categories",
            entries = listOf(
                DeeplinkEntry(
                    name = "Brands",
                    url = "https://www.alfie.com/brand"
                ),
                DeeplinkEntry(
                    name = "Brands > Polo Ralph Lauren",
                    url = "https://www.alfie.com/brand/polo-ralph-lauren"
                ),
                DeeplinkEntry(
                    name = "Designer",
                    url = "https://www.alfie.com/designer"
                ),
                DeeplinkEntry(
                    name = "Women",
                    url = "https://www.alfie.com/women"
                ),
                DeeplinkEntry(
                    name = "Women > Clothing > Dresses",
                    url = "https://www.alfie.com/women/clothing/dresses"
                ),
                DeeplinkEntry(
                    name = "Women > Clothing > Dresses > Maxi Dresses",
                    url = "https://www.alfie.com/women/clothing/dresses/maxi-dresses"
                ),
                DeeplinkEntry(
                    name = "Men",
                    url = "https://www.alfie.com/men"
                ),
                DeeplinkEntry(
                    name = "Shoes",
                    url = "https://www.alfie.com/shoes"
                ),
                DeeplinkEntry(
                    name = "Bag & Accessories",
                    url = "https://www.alfie.com/bags-and-accessories"
                ),
                DeeplinkEntry(
                    name = "Beauty",
                    url = "https://www.alfie.com/beauty"
                ),
                DeeplinkEntry(
                    name = "Kids",
                    url = "https://www.alfie.com/kids"
                ),
                DeeplinkEntry(
                    name = "Home & Food",
                    url = "https://www.alfie.com/home-and-food"
                ),
                DeeplinkEntry(
                    name = "Electrical",
                    url = "https://www.alfie.com/electrical"
                ),
                DeeplinkEntry(
                    name = "Sale",
                    url = "https://www.alfie.com/sale"
                ),
                DeeplinkEntry(
                    name = "Services",
                    url = "https://www.alfie.com/services/store-services"
                )
            )
        )
    )

    fun openDeeplink(url: String) {
        viewModelScope.launch {
            deeplinkHandler.handle(url)
        }
    }
}
