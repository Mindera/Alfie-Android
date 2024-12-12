package au.com.alfie.ecomm.designsystem.component.image.ratio

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint.MaxParentDimension
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint.MinParentDimension
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint.ParentHeight
import au.com.alfie.ecomm.designsystem.component.image.ratio.DimensionConstraint.ParentWidth
import kotlin.math.roundToInt

@Stable
fun Modifier.aspectRatio(
    ratioWidth: Float,
    ratioHeight: Float,
    reference: DimensionConstraint = MinParentDimension
) = then(
    AspectRatioModifier(
        ratioWidth = ratioWidth,
        ratioHeight = ratioHeight,
        reference = reference
    )
)

private class AspectRatioModifier(
    private val ratioWidth: Float,
    private val ratioHeight: Float,
    private val reference: DimensionConstraint
) : LayoutModifier {

    init {
        require(ratioWidth > 0) { "ratioWidth $ratioWidth must be > 0" }
        require(ratioHeight > 0) { "ratioHeight $ratioHeight must be > 0" }
    }

    private val ratio = ratioWidth / ratioHeight

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val size = constraints.size()
        val wrappedConstraints = if (size != IntSize.Zero) {
            Constraints.fixed(size.width, size.height)
        } else {
            constraints
        }
        val placeable: Placeable = measurable.measure(wrappedConstraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ) = if (height != Constraints.Infinity) {
        (height * ratio).roundToInt()
    } else {
        measurable.minIntrinsicWidth(height)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ) = if (height != Constraints.Infinity) {
        (height * ratio).roundToInt()
    } else {
        measurable.maxIntrinsicWidth(height)
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ) = if (width != Constraints.Infinity) {
        (width / ratio).roundToInt()
    } else {
        measurable.minIntrinsicHeight(width)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ) = if (width != Constraints.Infinity) {
        (width / ratio).roundToInt()
    } else {
        measurable.maxIntrinsicHeight(width)
    }

    private fun Constraints.size(): IntSize {
        val matchParentWidth = when (reference) {
            ParentWidth -> true
            ParentHeight -> false
            MinParentDimension -> maxWidth < maxHeight
            MaxParentDimension -> maxWidth > maxHeight
        }
        return if (matchParentWidth) {
            val height = ((maxWidth * ratioHeight) / ratioWidth).roundToInt()
            IntSize(maxWidth, height)
        } else {
            val width = ((maxHeight * ratioWidth) / ratioHeight).roundToInt()
            IntSize(width, maxHeight)
        }
    }
}
