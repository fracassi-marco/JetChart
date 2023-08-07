package io.jetchart.gauge

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import io.jetchart.common.animation.fadeInAnimation
import kotlin.math.min

@Composable
fun GaugeChart(
    percentValue: Float,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = fadeInAnimation(),
    valueDrawer: GaugeValueDrawer = SimpleGaugeValueDrawer(),
    pointerDrawer: PointerDrawer = NeedleDrawer(),
    arcDrawer: ArcDrawer = GaugeArcDrawer()
) {
    val progress = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(target(percentValue)) {
        progress.animateTo(
            targetValue = target(percentValue),
            animationSpec = animation
        )
    }

    val gaugeDegrees = 180
    val startAngle = 180f

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {

        val canvasSize = min(constraints.maxWidth, constraints.maxHeight)
        val canvasSizeDp = with(LocalDensity.current) { canvasSize.toDp() }

        Canvas(modifier = Modifier.offset(y = canvasSizeDp / 4).size(canvasSizeDp)) {
            arcDrawer.draw(this, startAngle, gaugeDegrees)

            drawIntoCanvas { canvas ->
                pointerDrawer.draw(this, canvas, gaugeDegrees, progress.value.percentToAngle(max = gaugeDegrees))
                valueDrawer.draw(this, canvas, center, target(percentValue))
            }
        }
    }

}

private fun target(percentValue: Float) = percentValue.coerceIn(0f, 100f)

private fun Float.percentToAngle(max: Int): Float {
    return (this * max / 100)
}