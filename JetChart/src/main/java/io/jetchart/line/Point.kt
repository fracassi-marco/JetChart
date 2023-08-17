package io.jetchart.line

sealed interface LinePoint {
    val value: Float
    val label: String
}
data class Point(override val value: Float, override val label: String): LinePoint
data class NullPoint(override val label: String): LinePoint {
    override val value = 0f
}
