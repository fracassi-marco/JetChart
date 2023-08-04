package io.jetchart.bar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
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
import io.jetchart.bar.renderer.label.SimpleBarValueDrawer
import io.jetchart.bar.renderer.label.BarValueDrawer
import io.jetchart.bar.renderer.xaxis.BarXAxisDrawer
import io.jetchart.bar.renderer.xaxis.XAxisDrawer
import io.jetchart.bar.renderer.yaxis.YAxisDrawer
import io.jetchart.bar.renderer.yaxis.BarYAxisWithValueDrawer
import io.jetchart.common.animation.fadeInAnimation

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    chars: Bars,
    animation: AnimationSpec<Float> = fadeInAnimation(),
    xAxisDrawer: XAxisDrawer = BarXAxisDrawer(),
    yAxisDrawer: YAxisDrawer = BarYAxisWithValueDrawer(),
    labelDrawer: LabelDrawer = SimpleLabelDrawer(),
    valueDrawer: BarValueDrawer = SimpleBarValueDrawer()
) {
    val transitionAnimation = remember(chars.bars) { Animatable(initialValue = 0f) }
    val rectangles = remember { mutableStateMapOf<Bar, Rect>() }
    val barDrawer = SimpleBarDrawer()
    LaunchedEffect(chars.bars) {
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

            yAxisDrawer.drawAxisLabels(this, canvas, yAxisArea, chars.minYValue, chars.maxYValue)

            yAxisDrawer.drawAxisLine(this, canvas, yAxisArea)

            xAxisDrawer.drawAxisLine(this, canvas, xAxisArea)

            chars.forEachWithArea(this, barDrawableArea, transitionAnimation.value, valueDrawer) { barArea, bar ->
                barDrawer.drawBar(this, canvas, barArea, bar)
                labelDrawer.draw(this, canvas, bar.label, barArea, xAxisArea)
                valueDrawer.draw(this, canvas, bar.value, barArea, xAxisArea)
                rectangles[bar] = barArea
            }
        }
    }
}