package io.jetchart.bar.renderer.label

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.jetchart.common.Formatter
import io.jetchart.common.utils.toLegacyInt

class SimpleValueDrawer(
  private val drawLocation: ValueDrawLocation = ValueDrawLocation.Inside,
  private val labelTextSize: TextUnit = 12.sp,
  private val labelTextColor: Color = Color.Black,
  private val formatter: Formatter = { value -> "%.1f".format(value) },
) : ValueDrawer {
  private val _labelTextArea: Float? = null
  private val paint = android.graphics.Paint().apply {
    this.textAlign = android.graphics.Paint.Align.CENTER
    this.color = labelTextColor.toLegacyInt()
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
      ValueDrawLocation.Outside -> (barArea.top) - labelTextSize.toPx() / 2
    }

    canvas.nativeCanvas.drawText(formatter(value), xCenter, yCenter, paint(drawScope))
  }

  private fun labelTextHeight(drawScope: DrawScope) = with(drawScope) {
    _labelTextArea ?: ((3f / 2f) * labelTextSize.toPx())
  }

  private fun paint(drawScope: DrawScope) = with(drawScope) {
    paint.apply {
      this.textSize = labelTextSize.toPx()
    }
  }

  enum class ValueDrawLocation {
    Inside,
    Outside
  }
}