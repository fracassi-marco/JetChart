# JetChart
[![](https://jitpack.io/v/fracassi-marco/JetChart.svg)](https://jitpack.io/#fracassi-marco/JetChart)

JetChart the Jetpack Compose charts library.
* easy
* fast
* customizable

## Gradle setup
1. Add the JitPack repository to your build file 
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.fracassi-marco:JetChart:1.0.0'
}
```

## Bar Chart
![Contribution guidelines for this project](docs/bar1.gif)

### Features
You can:
- scroll horizontally
- show values inside or above the bar
- define for each bar an action on the tap event
- customize colors and sizes
- define custom x-axis labels
- show or hide y-axis values
- animate the drawing

### Example
```kotlin
@Composable
fun BarChartComposable(text: MutableState<String>) {
    val numberOfBars = 8
    val width = numberOfBars * 80
    BarChart(chars = Bars(
        bars = (1..numberOfBars).map {
            Bar(label = "BAR$it", value = Random.nextFloat(), color = JetGreen) {
                    bar -> text.value = "You clicked on the bar ${bar.label}!"
            }
        }),
        modifier = Modifier.horizontalScroll(rememberScrollState()).width(width.dp).height(500.dp),
        animation = fadeInAnimation(3000),
        xAxisDrawer = BarXAxisDrawer(),
        yAxisDrawer = BarYAxisWithValueDrawer(),
        labelDrawer = SimpleLabelDrawer(),
        valueDrawer = SimpleValueDrawer(drawLocation = Inside)
    )
}
```

## Line Chart
![Contribution guidelines for this project](docs/line1.gif)

### Features
You can:
- scroll horizontally
- draw multiple lines
- chose how to draw points (none, filled circle or empty circle)
- show lines shading (transparent, solid or gradient)
- define for each point an action on the tap event (TODO)
- customize colors and sizes
- define custom x-axis labels
- show or hide y-axis values
- animate the drawing

### Example
```kotlin
@Composable
fun LineChartComposable() {
    LineChart(lines = listOf(
        Line(points = points(10), lineDrawer = SolidLineDrawer(thickness = 8.dp, color = Blue)),
        Line(points = points(15), lineDrawer = SolidLineDrawer(thickness = 8.dp, color = Red))),
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .width(1000.dp)
            .height(500.dp),
        animation = fadeInAnimation(3000),
        pointDrawer = FilledPointDrawer(),
        xAxisDrawer = LineXAxisDrawer(),
        yAxisDrawer = LineYAxisWithValueDrawer(),
        horizontalOffsetPercentage = 1f,
        lineShader = GradientLineShader(listOf(JetGreen, Transparent))
    )
}

@Composable
private fun points(count: Int) = (1..count).map { Point(Random.nextFloat(), "Point$it") }
```