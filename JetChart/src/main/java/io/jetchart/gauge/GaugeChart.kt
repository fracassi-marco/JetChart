package io.jetchart.gauge

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.jetchart.common.animation.fadeInAnimation
import kotlin.math.min

@Composable
fun GaugeChart(
    percentValue: Float,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = fadeInAnimation(),
    primaryColor: Color,
    valueDrawer: GaugeValueDrawer = SimpleGaugeValueDrawer()
) {
    val transitionProgress = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(target(percentValue)) {
        transitionProgress.animateTo(
            targetValue = target(percentValue),
            animationSpec = animation
        )
    }

    val density = LocalDensity.current
    val needleBaseSize = with(density) { 1.dp.toPx() }
    val strokeWidth = with(density) { 6.dp.toPx() }

    val gaugeDegrees = 180
    val startAngle = 180f

    val needlePaint = remember { Paint().apply { color = primaryColor } }

    val brush = Brush.horizontalGradient(
        0.1f to Color.Red,
        0.2f to Color.Yellow,
        0.5f to Color.Green,
    )

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {

        val canvasSize = min(constraints.maxWidth, constraints.maxHeight)
        val size = Size(canvasSize.toFloat(), canvasSize.toFloat())
        val canvasSizeDp = with(density) { canvasSize.toDp() }
        val w = size.width
        val h = size.height
        val center = Offset(w / 2, h / 2)

        Canvas(
            modifier = Modifier.size(canvasSizeDp),
            onDraw = {

                drawArc(
                    brush = brush,
                    startAngle = startAngle,
                    sweepAngle = gaugeDegrees.toFloat(),
                    useCenter = false,
                    size = size,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                drawIntoCanvas { canvas ->
                    canvas.save()

                    canvas.rotate(
                        degrees = transitionProgress.value.percentToAngle(max = gaugeDegrees),
                        pivotX = center.x,
                        pivotY = center.y
                    )

                    canvas.drawPath(
                        Path().apply {
                            moveTo(center.x, center.x)
                            lineTo(center.x, center.y + needleBaseSize)
                            lineTo(w / 6, center.y)
                            lineTo(center.x, center.y - 5)
                            close()
                        },
                        needlePaint
                    )

                    canvas.restore()

                    valueDrawer.draw(this, canvas, center, target(percentValue))
                }
            }
        )
    }

}

private fun target(percentValue: Float) = percentValue.coerceIn(0f, 100f)

private fun Float.percentToAngle(max: Int): Float {
    return (this * max / 100)
}