package au.com.alfie.ecomm.designsystem.component.image.ratio

enum class Ratio(
    val ratioWidth: Float,
    val ratioHeight: Float
) {
    RATIO1x1(ratioWidth = 1f, ratioHeight = 1f),
    RATIO2x3(ratioWidth = 2f, ratioHeight = 3f),
    RATIO3x4(ratioWidth = 3f, ratioHeight = 4f)
}
