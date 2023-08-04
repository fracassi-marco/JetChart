package io.jetchart.bar.renderer.label

import android.graphics.Paint
import android.graphics.Paint.Align.CENTER
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.jetchart.common.Formatter
import io.jetchart.common.utils.toLegacyInt

class SimpleBarValueDrawer(
  private val drawLocation: ValueDrawLocation = ValueDrawLocation.Inside,
  private val valueTextSize: TextUnit = 14.sp,
  private val valueTextColor: Color = Black,
  private val formatter: Formatter = { value -> "%.1f".format(value) },
) : BarValueDrawer {

  private val _labelTextArea: Float? = null
  private val paint = Paint().apply {
    this.textAlign = CENTER
    this.color = valueTextColor.toLegacyInt()
  }

  override fun requiredAboveBarHeight(drawScope: DrawScope): Float = when (drawLocation) {
    ValueDrawLocation.Outside -> (3f / 2f) * labelTextHeight(drawScope)
    ValueDrawLocation.Inside -> 0f
  }

  override fun draw(
    drawScope: DrawScope,
    canvas: Canvas,
    value: Float,
    barArea: Rect,
    xAxisArea: Rect
  ) = with(drawScope) {
    val xCenter = barArea.left + (barArea.width / 2)

    val yCenter = when (drawLocation) {
      ValueDrawLocation.Inside -> (barArea.top + barArea.bottom) / 2
      ValueDrawLocation.Outside -> (barArea.top) - valueTextSize.toPx() / 2
    }

    canvas.nativeCanvas.drawText(formatter(value), xCenter, yCenter, paint(drawScope))
  }

  private fun labelTextHeight(drawScope: DrawScope) = with(drawScope) {
    _labelTextArea ?: ((3f / 2f) * valueTextSize.toPx())
  }

  private fun paint(drawScope: DrawScope) = with(drawScope) {
    paint.apply {
      this.textSize = valueTextSize.toPx()
    }
  }

  enum class ValueDrawLocation {
    Inside,
    Outside
  }
}