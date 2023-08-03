package io.jetchart.line

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import io.jetchart.common.animation.fadeInAnimation
import io.jetchart.line.LineChartUtils.calculateDrawableArea
import io.jetchart.line.LineChartUtils.calculateFillPath
import io.jetchart.line.LineChartUtils.calculateLinePath
import io.jetchart.line.LineChartUtils.calculatePointLocation
import io.jetchart.line.LineChartUtils.calculateXAxisDrawableArea
import io.jetchart.line.LineChartUtils.calculateXAxisLabelsDrawableArea
import io.jetchart.line.LineChartUtils.calculateYAxisDrawableArea
import io.jetchart.line.LineChartUtils.withProgress
import io.jetchart.line.renderer.line.LineDrawer
import io.jetchart.line.renderer.line.LineShader
import io.jetchart.line.renderer.line.NoLineShader
import io.jetchart.line.renderer.line.SolidLineDrawer
import io.jetchart.line.renderer.point.FilledPointDrawer
import io.jetchart.line.renderer.point.PointDrawer
import io.jetchart.line.renderer.xaxis.LineXAxisDrawer
import io.jetchart.line.renderer.xaxis.XAxisDrawer
import io.jetchart.line.renderer.yaxis.LineYAxisWithValueDrawer
import io.jetchart.line.renderer.yaxis.YAxisDrawer

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    lines: List<Line>,
    labels: List<String> = lines.maxByOrNull { it.points.size }?.points?.map { it.label }
        ?: emptyList(),
    animation: AnimationSpec<Float> = fadeInAnimation(),
    pointDrawer: PointDrawer = FilledPointDrawer(),
    lineShader: LineShader = NoLineShader,
    xAxisDrawer: XAxisDrawer = LineXAxisDrawer(),
    yAxisDrawer: YAxisDrawer = LineYAxisWithValueDrawer(),
    horizontalOffsetPercentage: Float = 5f
) {
    val transitionAnimation = remember(lines.forEach { it.points }) {
        mutableStateListOf(
            *lines.map { Animatable(0f) }.toTypedArray()
        )
    }

    LaunchedEffect(lines.forEach { it.points }) {
        repeat(lines.size) { index ->
            transitionAnimation[index].snapTo(0f)
            transitionAnimation[index].animateTo(
                targetValue = 1f,
                animationSpec = animation
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        drawIntoCanvas { canvas ->

            val yAxisDrawableArea = calculateYAxisDrawableArea(
                xAxisLabelSize = xAxisDrawer.requiredHeight(this),
                this,
                yAxisDrawer
            )
            val xAxisDrawableArea = calculateXAxisDrawableArea(
                yAxisWidth = yAxisDrawableArea.width,
                labelHeight = xAxisDrawer.requiredHeight(this),
                size = size
            )
            val xAxisLabelsDrawableArea = calculateXAxisLabelsDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                offset = horizontalOffsetPercentage
            )
            val chartDrawableArea = calculateDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                yAxisDrawableArea = yAxisDrawableArea,
                size = size,
                offset = horizontalOffsetPercentage
            )

            // Draw the X Axis line.
            xAxisDrawer.drawAxisLine(
                drawScope = this,
                drawableArea = xAxisDrawableArea,
                canvas = canvas
            )

            xAxisDrawer.drawAxisLabels(
                drawScope = this,
                canvas = canvas,
                drawableArea = xAxisLabelsDrawableArea,
                labels = labels
            )

            // Draw the Y Axis line.
            yAxisDrawer.drawAxisLine(
                drawScope = this,
                canvas = canvas,
                drawableArea = yAxisDrawableArea
            )

            yAxisDrawer.drawAxisLabels(
                drawScope = this,
                canvas = canvas,
                drawableArea = yAxisDrawableArea,
                minValue = lines.minOf { it.minYValue },
                maxValue = lines.maxOf { it.maxYValue }
            )

            lines.forEachIndexed { index, lineChartData ->
                drawLine(
                    canvas = canvas,
                    lineChartData = lineChartData,
                    transitionAnimation = transitionAnimation[index],
                    pointDrawer = pointDrawer,
                    lineDrawer = lineChartData.lineDrawer,
                    lineShader = lineShader,
                    chartDrawableArea = chartDrawableArea
                )
            }
        }
    }
}

private fun DrawScope.drawLine(
    pointDrawer: PointDrawer = FilledPointDrawer(),
    lineDrawer: LineDrawer = SolidLineDrawer(),
    lineShader: LineShader = NoLineShader,
    canvas: Canvas,
    transitionAnimation: Animatable<Float, AnimationVector1D>,
    lineChartData: Line,
    chartDrawableArea: Rect
) {

    // Draw the chart line.
    lineDrawer.drawLine(
        drawScope = this,
        canvas = canvas,
        linePath = calculateLinePath(
            drawableArea = chartDrawableArea,
            lineChartData = lineChartData,
            transitionProgress = transitionAnimation.value
        )
    )

    lineShader.fillLine(
        drawScope = this,
        canvas = canvas,
        fillPath = calculateFillPath(
            drawableArea = chartDrawableArea,
            lineChartData = lineChartData,
            transitionProgress = transitionAnimation.value
        )
    )

    lineChartData.points.forEachIndexed { index, point ->
        withProgress(
            index = index,
            lineChartData = lineChartData,
            transitionProgress = transitionAnimation.value
        ) {
            pointDrawer.drawPoint(
                drawScope = this,
                canvas = canvas,
                center = calculatePointLocation(
                    drawableArea = chartDrawableArea,
                    lineChartData = lineChartData,
                    point = point,
                    index = index
                )
            )
        }
    }
}