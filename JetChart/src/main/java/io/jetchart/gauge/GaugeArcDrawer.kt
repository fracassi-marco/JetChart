package io.jetchart.gauge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Square
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class GaugeArcDrawer(
    private val thickness: Dp = 32.dp,
    private val brush: Brush = Brush.horizontalGradient(listOf(Red, Yellow, Green)),
    private val cap: StrokeCap = Square
) : ArcDrawer {
    override fun draw(drawScope: DrawScope, startAngle: Float, gaugeDegrees: Int) = with(drawScope) {
        drawArc(
            brush = brush,
            startAngle = startAngle,
            sweepAngle = gaugeDegrees.toFloat(),
            useCenter = false,
            size = androidx.compose.ui.geometry.Size(size.width - thickness.toPx(), size.height),
            topLeft = Offset(x = thickness.toPx() / 2, 0f),
            style = Stroke(width = thickness.toPx(), cap = cap)
        )
    }
}
