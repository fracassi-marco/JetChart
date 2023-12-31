package io.jetchart.bar

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import io.jetchart.bar.renderer.label.BarValueDrawer
import io.jetchart.bar.renderer.xaxis.XAxisDrawer
import io.jetchart.bar.renderer.yaxis.YAxisDrawer

object BarChartUtils {
  fun axisAreas(
    drawScope: DrawScope,
    totalSize: Size,
    xAxisDrawer: XAxisDrawer,
    yAxisDrawer: YAxisDrawer,
    valueDrawer: BarValueDrawer
  ): Pair<Rect, Rect> = with(drawScope) {

    val yAxisTop = valueDrawer.requiredAboveBarHeight(drawScope)
    val yAxisRight = yAxisDrawer.marginRight(this)
    val xAxisRight = totalSize.width
    val xAxisTop = totalSize.height - xAxisDrawer.requiredHeight(drawScope)

    return Pair(
      Rect(yAxisRight, xAxisTop, xAxisRight, totalSize.height),
      Rect(0f, yAxisTop, yAxisRight, xAxisTop)
    )
  }

  fun barDrawableArea(xAxisArea: Rect): Rect {
    return Rect(
      left = xAxisArea.left,
      top = 0f,
      right = xAxisArea.right,
      bottom = xAxisArea.top
    )
  }

  fun Bars.forEachWithArea(
    drawScope: DrawScope,
    barDrawableArea: Rect,
    progress: Float,
    valueDrawer: BarValueDrawer,
    barHorizontalMargin: Dp,
    block: (barArea: Rect, bar: Bar) -> Unit
  ) = with(drawScope){
    val totalBars = bars.size
    val widthOfBarArea = barDrawableArea.width / totalBars
    val barGapPx = barHorizontalMargin.toPx()

    bars.forEachIndexed { index, bar ->
      val left = barDrawableArea.left + (index * widthOfBarArea)
      val height = barDrawableArea.height

      val barHeight = (height - valueDrawer.requiredAboveBarHeight(drawScope)) * progress

      val barArea = Rect(
        left = left + barGapPx,
        top = barDrawableArea.bottom - (bar.value / maxBarValue) * barHeight,
        right = left + widthOfBarArea - barGapPx,
        bottom = barDrawableArea.bottom
      )
      block(barArea, bar)
    }
  }
}