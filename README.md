# JetChart

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

### Example
```kotlin
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
```