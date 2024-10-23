package io.jetchart.line.renderer.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class IndexesPointDrawer(
    val indexes: List<Int>,
    val pointDrawer: PointDrawer,
    val diameter: Dp = 8.dp,
    val color: Color = Color.Blue
) : PointDrawer {
  override fun drawPoint(
      drawScope: DrawScope,
      canvas: Canvas,
      center: Offset,
      index: Int
  ) {
    if(indexes.contains(index))
      pointDrawer.drawPoint(drawScope, canvas, center, index)
  }
}