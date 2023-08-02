package io.jetchart.bar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import io.jetchart.bar.BarChartUtils.axisAreas
import io.jetchart.bar.BarChartUtils.barDrawableArea
import io.jetchart.bar.BarChartUtils.forEachWithArea
import io.jetchart.bar.renderer.bar.SimpleBarDrawer
import io.jetchart.bar.renderer.label.LabelDrawer
import io.jetchart.bar.renderer.label.SimpleLabelDrawer
import io.jetchart.bar.renderer.xaxis.SimpleXAxisDrawer
import io.jetchart.bar.renderer.xaxis.XAxisDrawer
import io.jetchart.bar.renderer.yaxis.YAxisWithValueDrawer
import io.jetchart.bar.renderer.yaxis.YAxisDrawer
import io.jetchart.common.animation.fadeInAnimation
import androidx.compose.runtime.mutableStateMapOf
import io.jetchart.bar.renderer.label.SimpleValueDrawer
import io.jetchart.bar.renderer.label.ValueDrawer

@Composable
fun BarChart(
    barChartData: BarChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = fadeInAnimation(),
    xAxisDrawer: XAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer: YAxisDrawer = YAxisWithValueDrawer(),
    labelDrawer: LabelDrawer = SimpleLabelDrawer(),
    valueDrawer: ValueDrawer = SimpleValueDrawer()
) {
    val transitionAnimation = remember(barChartData.bars) { Animatable(initialValue = 0f) }
    val rectangles = remember { mutableStateMapOf<BarChartData.Bar, Rect>() }
    val barDrawer = SimpleBarDrawer()
    LaunchedEffect(barChartData.bars) {
        transitionAnimation.animateTo(1f, animationSpec = animation)
    }

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                rectangles
                    .filter { it.value.contains(offset) }
                    .forEach { it.key.onTap(it.key) }
            }
        }) {
        drawIntoCanvas { canvas ->
            val (xAxisArea, yAxisArea) = axisAreas(this, size, xAxisDrawer, yAxisDrawer, valueDrawer)
            val barDrawableArea = barDrawableArea(xAxisArea)

            yAxisDrawer.drawAxisLabels(this, canvas, yAxisArea, barChartData.minYValue, barChartData.maxYValue)

            yAxisDrawer.drawAxisLine(this, canvas, yAxisArea)

            xAxisDrawer.drawAxisLine(this, canvas, xAxisArea)

            barChartData.forEachWithArea(this, barDrawableArea, transitionAnimation.value, valueDrawer) { barArea, bar ->
                barDrawer.drawBar(this, canvas, barArea, bar)
                labelDrawer.draw(this, canvas, bar.label, barArea, xAxisArea)
                valueDrawer.draw(this, canvas, bar.value, barArea, xAxisArea)
                rectangles[bar] = barArea
            }
        }
    }
}