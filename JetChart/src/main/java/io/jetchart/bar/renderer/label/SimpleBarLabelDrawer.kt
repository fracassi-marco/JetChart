package io.jetchart.bar.renderer.label

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.jetchart.common.utils.toLegacyInt

class SimpleBarLabelDrawer(
  private val labelTextSize: TextUnit = 12.sp,
  private val labelTextColor: Color = Color.Black
) : BarLabelDrawer {
  private val _labelTextArea: Float? = null
  private val paint = android.graphics.Paint().apply {
    this.textAlign = android.graphics.Paint.Align.CENTER
    this.color = labelTextColor.toLegacyInt()
  }

  override fun draw(
    drawScope: DrawScope,
    canvas: Canvas,
    label: String,
    barArea: Rect,
    xAxisArea: Rect
  ) = with(drawScope) {
    val xCenter = barArea.left + (barArea.width / 2)

    val yCenter = barArea.bottom + labelTextHeight(drawScope)

    canvas.nativeCanvas.drawText(label, xCenter, yCenter, paint(drawScope))
  }

  private fun labelTextHeight(drawScope: DrawScope) = with(drawScope) {
    _labelTextArea ?: ((3f / 2f) * labelTextSize.toPx())
  }

  private fun paint(drawScope: DrawScope) = with(drawScope) {
    paint.apply {
      this.textSize = labelTextSize.toPx()
    }
  }
}

