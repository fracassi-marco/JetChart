package io.jetchart.line.renderer.yaxis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

class LineEmptyYAxisDrawer : YAxisDrawer {

  override fun drawAxisLine(
      drawScope: DrawScope,
      canvas: Canvas,
      drawableArea: Rect
  ) {}

  override fun drawAxisLabels(
      drawScope: DrawScope,
      canvas: Canvas,
      drawableArea: Rect,
      minValue: Float,
      maxValue: Float
  ) {}

    override fun marginRight(drawScope: DrawScope) = 0f
}