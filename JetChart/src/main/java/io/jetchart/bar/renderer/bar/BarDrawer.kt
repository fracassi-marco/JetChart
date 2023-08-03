package io.jetchart.bar.renderer.bar

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.jetchart.bar.Bar

interface BarDrawer {
  fun drawBar(
    drawScope: DrawScope,
    canvas: Canvas,
    barArea: Rect,
    bar: Bar
  )
}