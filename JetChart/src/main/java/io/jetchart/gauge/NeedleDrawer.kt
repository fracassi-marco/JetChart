package io.jetchart.gauge

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class NeedleDrawer(private val needleColor: Color = Color.Cyan, private val baseSize: Dp = 16.dp) : PointerDrawer {

    override fun draw(drawScope: DrawScope, canvas: Canvas, gaugeDegrees: Int, progressDegrees: Float) {
        with(drawScope) {
            canvas.save()

            canvas.rotate(
                degrees = progressDegrees,
                pivotX = center.x,
                pivotY = center.y
            )

            val baseSizePx = baseSize.toPx()
            val paint = Paint().apply { color = needleColor }
            canvas.drawPath(
                Path().apply {
                    moveTo(center.x, center.x)
                    lineTo(center.x, center.y + (baseSizePx / 2))
                    lineTo(size.width / 12, center.y)
                    lineTo(center.x, center.y - (baseSizePx / 2))
                    close()
                },
                paint
            )

            canvas.restore()
        }
    }
}
