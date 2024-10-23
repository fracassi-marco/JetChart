package io.jetchart.line

import io.jetchart.line.renderer.line.LineDrawer
import io.jetchart.line.renderer.line.LineShader
import io.jetchart.line.renderer.line.NoLineShader
import io.jetchart.line.renderer.point.FilledPointDrawer
import io.jetchart.line.renderer.point.PointDrawer

data class Line(
  val points: List<LinePoint>,
  val padBy: Float = 20f,
  val startAtZero: Boolean = false,
  val lineDrawer: LineDrawer,
  val pointDrawer: PointDrawer = FilledPointDrawer(),
  val shader: LineShader = NoLineShader,
) {
  init {
    require(padBy in 0f..100f)
  }

  private val yMinMax: Pair<Float, Float>
    get() {
      val min = points.minByOrNull { it.value }?.value ?: 0f
      val max = points.maxByOrNull { it.value }?.value ?: 0f

      return min to max
    }

  val maxYValue: Float = yMinMax.second + ((yMinMax.second - yMinMax.first) * padBy / 100f)
  val minYValue: Float
    get() {
      return if (startAtZero) {
        0f
      } else {
        yMinMax.first - ((yMinMax.second - yMinMax.first) * padBy / 100f)
      }
    }
  val yRange = maxYValue - minYValue
}