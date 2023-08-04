package io.jetchart.gauge

import android.graphics.Paint
import android.graphics.Paint.Align.CENTER
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.jetchart.common.Formatter
import io.jetchart.common.utils.toLegacyInt

class SimpleGaugeValueDrawer(
    private val valueTextSize: TextUnit = 14.sp,
    private val valueTextColor: Color = Black,
    private val valueTextPadding: Dp = 15.dp,
    private val formatter: Formatter = { value -> "%.1f".format(value) },
) : GaugeValueDrawer {

    private val paint = Paint().apply {
        this.textAlign = CENTER
        this.color = valueTextColor.toLegacyInt()
    }

    override fun draw(drawScope: DrawScope, canvas: Canvas, center: Offset, value: Float) = with(drawScope) {
            val textY = center.y + valueTextSize.value + valueTextPadding.toPx()

            canvas.nativeCanvas.drawText(
                formatter(value),
                center.x,
                textY,
                paint(drawScope)
            )
        }

    private fun paint(drawScope: DrawScope) = with(drawScope) {
        paint.apply {
            this.textSize = valueTextSize.toPx()
        }
    }
}
