package io.jetchart.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import io.jetchart.bar.Bar
import io.jetchart.bar.BarChart
import io.jetchart.bar.Bars
import io.jetchart.bar.renderer.label.SimpleBarLabelDrawer
import io.jetchart.bar.renderer.label.SimpleBarValueDrawer
import io.jetchart.bar.renderer.label.SimpleBarValueDrawer.ValueDrawLocation.Inside
import io.jetchart.bar.renderer.xaxis.BarXAxisDrawer
import io.jetchart.bar.renderer.yaxis.BarYAxisWithValueDrawer
import io.jetchart.common.animation.fadeInAnimation
import io.jetchart.demo.ui.theme.JetChartTheme
import io.jetchart.demo.ui.theme.JetGreen
import io.jetchart.gauge.GaugeArcDrawer
import io.jetchart.gauge.GaugeChart
import io.jetchart.gauge.NeedleDrawer
import io.jetchart.line.Line
import io.jetchart.line.LineChart
import io.jetchart.line.NullPoint
import io.jetchart.line.Point
import io.jetchart.line.renderer.line.GradientLineShader
import io.jetchart.line.renderer.line.SolidLineDrawer
import io.jetchart.line.renderer.point.NoPointDrawer
import io.jetchart.line.renderer.xaxis.LineXAxisDrawer
import io.jetchart.line.renderer.yaxis.LineYAxisWithValueDrawer
import io.jetchart.pie.PieChart
import io.jetchart.pie.Pies
import io.jetchart.pie.Slice
import io.jetchart.pie.renderer.FilledSliceDrawer
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetChartTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val text = remember { mutableStateOf("Click on a bar!") }
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(text.value, modifier = Modifier.padding(16.dp))
                        BarChartComposable(text)
                        JetDivider()
                        LineChartComposable()
                        JetDivider()
                        PieChartComposable()
                        JetDivider()
                        DonutChartComposable()
                        JetDivider()
                        GaugeChartComposable()
                        JetDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun BarChartComposable(text: MutableState<String>) {
    val numberOfBars = 8
    val width = numberOfBars * 80
    BarChart(
        bars = Bars(
            bars = (1..numberOfBars).map {
                Bar(label = "BAR$it", value = Random.nextFloat(), color = if (it % 2 == 0) JetGreen else Red) { bar ->
                    text.value = "You clicked on the bar ${bar.label}!"
                }
            }),
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .width(width.dp)
            .height(500.dp),
        animation = fadeInAnimation(3000),
        xAxisDrawer = BarXAxisDrawer(),
        yAxisDrawer = BarYAxisWithValueDrawer(),
        labelDrawer = SimpleBarLabelDrawer(),
        valueDrawer = SimpleBarValueDrawer(drawLocation = Inside),
        barHorizontalMargin = 5.dp
    )
}

@Composable
fun LineChartComposable() {
    LineChart(
        lines = listOf(
            Line(
                points = points(10),
                lineDrawer = SolidLineDrawer(thickness = 8.dp, color = Blue),
                pointDrawer = NoPointDrawer,
                shader = GradientLineShader(listOf(JetGreen, Transparent))
            ),
            Line(points = points(15), lineDrawer = SolidLineDrawer(thickness = 8.dp, color = Red)),
            Line(points = points(6) + nullPoints(2), lineDrawer = SolidLineDrawer(thickness = 8.dp, color = Cyan))
        ),
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .width(1000.dp)
            .height(500.dp),
        animation = fadeInAnimation(3000),
        xAxisDrawer = LineXAxisDrawer(),
        yAxisDrawer = LineYAxisWithValueDrawer(),
        horizontalOffsetPercentage = 1f,
    )
}

private fun points(count: Int) = (1..count).map { Point(10 + Random.nextFloat(), "Point$it") }
private fun nullPoints(count: Int) = (1..count).map { NullPoint("Point$it") }

@Composable
fun PieChartComposable() {
    PieChart(
        pies = Pies(listOf(Slice(15f, Red), Slice(27f, JetGreen), Slice(5f, Yellow), Slice(11f, Cyan))),
        modifier = Modifier.height(340.dp),
        animation = fadeInAnimation(4000),
        sliceDrawer = FilledSliceDrawer(thickness = 100f)
    )
}

@Composable
fun DonutChartComposable() {
    PieChart(
        pies = Pies(listOf(Slice(35f, Red), Slice(45f, JetGreen), Slice(15f, Yellow), Slice(5f, Cyan))),
        modifier = Modifier.height(340.dp),
        animation = fadeInAnimation(4000),
        sliceDrawer = FilledSliceDrawer(thickness = 60f)
    )
}

@Composable
fun GaugeChartComposable() {
    GaugeChart(
        percentValue = 72f, //between 0 and 100
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        animation = fadeInAnimation(4000),
        pointerDrawer = NeedleDrawer(needleColor = JetGreen, baseSize = 12.dp),
        arcDrawer = GaugeArcDrawer(thickness = 32.dp, cap = StrokeCap.Round)
    )
}

@Composable
private fun JetDivider() {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 5.dp, vertical = 50.dp), thickness = 1.dp, color = JetGreen)
}