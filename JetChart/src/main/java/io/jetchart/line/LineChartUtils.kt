package io.jetchart.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import io.jetchart.line.renderer.yaxis.YAxisDrawer

object LineChartUtils {
  fun calculateDrawableArea(
    xAxisDrawableArea: Rect,
    yAxisDrawableArea: Rect,
    size: Size,
    offset: Float
  ): Rect {
    val horizontalOffset = xAxisDrawableArea.width * offset / 100f

    return Rect(
      left = yAxisDrawableArea.right + horizontalOffset,
      top = 0f,
      bottom = xAxisDrawableArea.top,
      right = size.width - horizontalOffset
    )
  }

  fun calculateXAxisDrawableArea(
    yAxisWidth: Float,
    labelHeight: Float,
    size: Size
  ): Rect {
    val top = size.height - labelHeight

    return Rect(
      left = yAxisWidth,
      top = top,
      bottom = size.height,
      right = size.width
    )
  }

  fun calculateXAxisLabelsDrawableArea(
    xAxisDrawableArea: Rect,
    offset: Float
  ): Rect {
    val horizontalOffset = xAxisDrawableArea.width * offset / 100f

    return Rect(
      left = xAxisDrawableArea.left + horizontalOffset,
      top = xAxisDrawableArea.top,
      bottom = xAxisDrawableArea.bottom,
      right = xAxisDrawableArea.right - horizontalOffset
    )
  }

  fun calculateYAxisDrawableArea(
    xAxisLabelSize: Float,
    drawScope: DrawScope,
    yAxisDrawer: YAxisDrawer
  ): Rect {
    return Rect(
      left = 0f,
      top = 0f,
      bottom = drawScope.size.height - xAxisLabelSize,
      right = yAxisDrawer.marginRight(drawScope)
    )
  }

  fun calculatePointLocation(
      drawableArea: Rect,
      lineChartData: Line,
      yRange: Float,
      point: LinePoint,
      index: Int
  ): Offset {
    val x = (index.toFloat() / (lineChartData.points.size - 1))
    val y = ((point.value - lineChartData.minYValue) / yRange)

    return Offset(
      x = (x * drawableArea.width) + drawableArea.left,
      y = drawableArea.height - (y * drawableArea.height)
    )
  }

  fun withProgress(
      index: Int,
      lineChartData: Line,
      transitionProgress: Float,
      showWithProgress: (progress: Float) -> Unit
  ) {
    val size = lineChartData.points.size
    val toIndex = (size * transitionProgress).toInt() + 1

    if (index == toIndex) {
      // Get the left over.
      val sizeF = lineChartData.points.size.toFloat()
      val perIndex = (1f / sizeF)
      val down = (index - 1) * perIndex

      showWithProgress((transitionProgress - down) / perIndex)
    } else if (index < toIndex) {
      showWithProgress(1f)
    }
  }

  fun calculateLinePath(
      drawableArea: Rect,
      lineChartData: Line,
      yRange: Float,
      transitionProgress: Float
  ): Path = Path().apply {
    var prevPointLocation: Offset? = null
    lineChartData.points.forEachIndexed { index, point ->
      withProgress(
        index = index,
        transitionProgress = transitionProgress,
        lineChartData = lineChartData
      ) { progress ->
        val pointLocation = when(point) {
          is Point -> calculatePointLocation(
            drawableArea = drawableArea,
            lineChartData = lineChartData,
            yRange = yRange,
            point = point,
            index = index
          )
          is NullPoint -> Offset.Unspecified
        }

        if (pointLocation.isSpecified) {
          if (index == 0) {
            moveTo(pointLocation.x, pointLocation.y)
          } else {
            if (progress <= 1f) {
              // We have to change the `dy` based on the progress
              val prevX = prevPointLocation!!.x
              val prevY = prevPointLocation!!.y

              val x = (pointLocation.x - prevX) * progress + prevX
              val y = (pointLocation.y - prevY) * progress + prevY

              lineTo(x, y)
            } else {
              lineTo(pointLocation.x, pointLocation.y)
            }
          }

          prevPointLocation = pointLocation
        }
      }
    }
  }

  fun calculateFillPath(drawableArea: Rect,
                        lineChartData: Line,
                        yRange: Float,
                        transitionProgress: Float
  ): Path = Path().apply {
    moveTo(drawableArea.left, drawableArea.bottom)
    var prevPointX : Float? = null
    var prevPointLocation: Offset? = null
    lineChartData.points.forEachIndexed { index, point ->
      withProgress(
        index = index,
        transitionProgress = transitionProgress,
        lineChartData = lineChartData
      ) { progress ->
        val pointLocation = when(point) {
          is Point -> calculatePointLocation(
            drawableArea = drawableArea,
            lineChartData = lineChartData,
            yRange = yRange,
            point = point,
            index = index
          )
          is NullPoint -> Offset.Unspecified
        }

        if (pointLocation.isSpecified) {
          if (index == 0) {
            lineTo(drawableArea.left, pointLocation.y)
            lineTo(pointLocation.x, pointLocation.y)
          } else {
            if (progress <= 1f) {
              // We have to change the `dy` based on the progress
              val prevX = prevPointLocation!!.x
              val prevY = prevPointLocation!!.y

              val x = (pointLocation.x - prevX) * progress + prevX
              val y = (pointLocation.y - prevY) * progress + prevY

              lineTo(x, y)

              prevPointX = x
            } else {
              lineTo(pointLocation.x, pointLocation.y)
              prevPointX = pointLocation.x
            }
          }

          prevPointLocation = pointLocation
        }
      }
    }
    // We need to connect the line to the end of the drawable area
    prevPointX?.let { x->
      lineTo(x, drawableArea.bottom)
      lineTo(drawableArea.right, drawableArea.bottom)
    } ?: lineTo(drawableArea.left, drawableArea.bottom)
  }
}
