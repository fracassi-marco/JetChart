package io.jetchart.bar.renderer.label

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface BarValueDrawer {
  fun requiredAboveBarHeight(drawScope: DrawScope): Float = 0f

  fun draw(
    drawScope: DrawScope,
    canvas: Canvas,
    value: Float,
    barArea: Rect,
    xAxisArea: Rect
  )
}