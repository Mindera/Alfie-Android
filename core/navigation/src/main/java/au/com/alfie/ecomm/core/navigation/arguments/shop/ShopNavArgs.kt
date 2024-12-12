package au.com.alfie.ecomm.core.navigation.arguments.shop

fun shopNavArgs(
    tab: ShopTab = ShopTab.Categories
) = ShopNavArgs(
    tab = tab
)

data class ShopNavArgs(
    val tab: ShopTab
)
