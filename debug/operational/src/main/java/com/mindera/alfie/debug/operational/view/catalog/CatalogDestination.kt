package com.mindera.alfie.debug.operational.view.catalog

import com.mindera.alfie.debug.operational.view.destinations.AccordionScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.BadgeScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.BottomCardScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ButtonScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.CheckboxScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ChipScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ColorScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.DateFieldScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.DividerScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.DotsIndicatorScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ElevationShadowingScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.FixedTabScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.GalleryScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.IconsScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.InputScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.LandingHeaderScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.LoadingScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ModalScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.MotionScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.PriceScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ProductCardScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ProductCarouselScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.RadioButtonScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ScrollableTabScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SearchPullDownScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SegmentedControlScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ShapeScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.ShimmerScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SizingButtonsScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SliderIndicatorScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SnackbarScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SortByScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SpacingScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SwatchScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.SwitchScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.TagScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.TitleHeaderScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.TitleSearchScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.TypographyScreenDestination
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
