package io.jetchart.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.jetchart.bar.BarChart
import io.jetchart.bar.BarChartData
import io.jetchart.bar.renderer.label.SimpleLabelDrawer
import io.jetchart.bar.renderer.label.SimpleValueDrawer
import io.jetchart.bar.renderer.label.SimpleValueDrawer.ValueDrawLocation.Inside
import io.jetchart.bar.renderer.xaxis.SimpleXAxisDrawer
import io.jetchart.bar.renderer.yaxis.YAxisWithValueDrawer
import io.jetchart.common.animation.fadeInAnimation
import io.jetchart.demo.ui.theme.JetChartTheme
import io.jetchart.demo.ui.theme.JetGreen
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val text = remember { mutableStateOf("Click on a bar!") }
                    Column {
                        Text(text.value, modifier = Modifier.padding(16.dp))
                        BarChartComposable(text)
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
    BarChart(barChartData = BarChartData(
        bars = (1..numberOfBars).map {
            BarChartData.Bar(label = "BAR$it", value = Random.nextFloat(), color = JetGreen) {
                    bar -> text.value = "You clicked on the bar ${bar.label}!"
            }
        }),
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .width(width.dp)
            .height(500.dp),
        animation = fadeInAnimation(3000),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = YAxisWithValueDrawer(),
        labelDrawer = SimpleLabelDrawer(),
        valueDrawer = SimpleValueDrawer(drawLocation = Inside)
    )
}