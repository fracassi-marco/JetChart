package io.jetchart.bar.renderer.label

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface BarLabelDrawer {
  fun draw(
    drawScope: DrawScope,
    canvas: Canvas,
    label: String,
    barArea: Rect,
    xAxisArea: Rect
  )
}