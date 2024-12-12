package au.com.alfie.ecomm.debug.operational.view.catalog

import au.com.alfie.ecomm.debug.operational.view.destinations.AccordionScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.BadgeScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.BottomCardScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ButtonScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.CheckboxScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ChipScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ColorScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.DateFieldScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.DividerScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.DotsIndicatorScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ElevationShadowingScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.FixedTabScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.GalleryScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.IconsScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.InputScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.LandingHeaderScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.LoadingScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ModalScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.MotionScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.PriceScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ProductCardScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ProductCarouselScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.RadioButtonScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ScrollableTabScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SearchPullDownScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SegmentedControlScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ShapeScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.ShimmerScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SizingButtonsScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SliderIndicatorScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SnackbarScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SortByScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SpacingScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SwatchScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.SwitchScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.TagScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.TitleHeaderScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.TitleSearchScreenDestination
import au.com.alfie.ecomm.debug.operational.view.destinations.TypographyScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

internal enum class CatalogDestination(
    val direction: DirectionDestinationSpec,
    val title: String
) {
    AccordionScreen(AccordionScreenDestination, "Accordions"),
    BadgeScreen(BadgeScreenDestination, "Badges"),
    BottomCard(BottomCardScreenDestination, "Bottom Card"),
    ButtonScreen(ButtonScreenDestination, "Buttons"),
    CheckboxScreen(CheckboxScreenDestination, "Checkboxes"),
    ChipScreen(ChipScreenDestination, "Chips"),
    ColorScreen(ColorScreenDestination, "Colors"),
    DateScreen(DateFieldScreenDestination, "Date Picker"),
    DividerScreen(DividerScreenDestination, "Dividers"),
    ElevationShadowingScreen(ElevationShadowingScreenDestination, "Elevation/Shadowing"),
    Icons(IconsScreenDestination, "Icons"),
    Input(InputScreenDestination, "Input"),
    LandingHeaderScreen(LandingHeaderScreenDestination, "Landing Page Header"),
    LoadingScreen(LoadingScreenDestination, "Loading"),
    ModalScreen(ModalScreenDestination, "Modal Screen"),
    Motion(MotionScreenDestination, "Motion"),
    Gallery(GalleryScreenDestination, "PDP Image Gallery"),
    Price(PriceScreenDestination, "Price Screen"),
    ProductCard(ProductCardScreenDestination, "Product Card"),
    ProductCarousel(ProductCarouselScreenDestination, "Product Carousel"),
    SliderIndicatorScreen(SliderIndicatorScreenDestination, "Progress Bar"),
    DotsIndicatorScreen(DotsIndicatorScreenDestination, "Progress Indicators"),
    RadioButtonScreen(RadioButtonScreenDestination, "Radio Buttons"),
    ShapeScreen(ShapeScreenDestination, "Rounded Corners"),
    TitleSearchScreen(TitleSearchScreenDestination, "Search"),
    SearchPullDownScreen(SearchPullDownScreenDestination, "Search Pull Down Animation"),
    SegmentedControl(SegmentedControlScreenDestination, "Segmented Controls"),
    SizingButtons(SizingButtonsScreenDestination, "Sizing"),
    ShimmerScreen(ShimmerScreenDestination, "Skeleton Animation"),
    SnackbarScreen(SnackbarScreenDestination, "Snackbar"),
    SortByScreen(SortByScreenDestination, "Sort By Component"),
    SpacingScreen(SpacingScreenDestination, "Spacing"),
    Swatch(SwatchScreenDestination, "Swatches"),
    SwitchScreen(SwitchScreenDestination, "Switch"),
    TabIntrinsicScreen(ScrollableTabScreenDestination, "Tab (Intrinsic Width)"),
    TabFixedScreen(FixedTabScreenDestination, "Tab (Fixed Width)"),
    TagScreen(TagScreenDestination, "Tag"),
    TitleHeaderScreen(TitleHeaderScreenDestination, "Title Header"),
    TypographyScreen(TypographyScreenDestination, "Typography")
}
