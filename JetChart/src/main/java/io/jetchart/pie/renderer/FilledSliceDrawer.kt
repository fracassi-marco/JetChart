package io.jetchart.pie.renderer

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.jetchart.pie.Slice

class FilledSliceDrawer(private val thickness: Float = 30f) : SliceDrawer {

  private val sectionPaint = Paint().apply {
    isAntiAlias = true
    style = PaintingStyle.Stroke
  }

  override fun drawSlice(
    drawScope: DrawScope,
    canvas: Canvas,
    area: Size,
    startAngle: Float,
    sweepAngle: Float,
    slice: Slice
  ) {
    val sliceThickness = calculateSectorThickness(area = area)
    val drawableArea = calculateDrawableArea(area = area)

    canvas.drawArc(
      rect = drawableArea,
      paint = sectionPaint.apply {
        color = slice.color
        strokeWidth = sliceThickness
      },
      startAngle = startAngle,
      sweepAngle = sweepAngle,
      useCenter = false
    )
  }

  private fun calculateSectorThickness(area: Size): Float {
    val minSize = minOf(area.width, area.height)

    return minSize * (thickness / 200f)
  }

  private fun calculateDrawableArea(area: Size): Rect {
    val sliceThicknessOffset =
      calculateSectorThickness(area = area) / 2f
    val offsetHorizontally = (area.width - area.height) / 2f

    return Rect(
      left = sliceThicknessOffset + offsetHorizontally,
      top = sliceThicknessOffset,
      right = area.width - sliceThicknessOffset - offsetHorizontally,
      bottom = area.height - sliceThicknessOffset
    )
  }
}