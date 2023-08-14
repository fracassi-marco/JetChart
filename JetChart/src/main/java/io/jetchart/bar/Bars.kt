package io.jetchart.bar

data class Bars(
    val bars: List<Bar>,
    val padBy: Float = 10f,
    val startAtZero: Boolean = true
) {
    init {
        require(padBy in 0f..100f)
    }

    private val yMinMax: Pair<Float, Float>
        get() {
            val min = bars.minByOrNull { it.value }?.value ?: 0f
            val max = bars.maxByOrNull { it.value }?.value ?: 0f

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

    val maxBarValue = bars.maxByOrNull { it.value }?.value ?: 0f
}
