package io.jetchart.gauge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface GaugeValueDrawer {
    fun draw(drawScope: DrawScope, canvas: Canvas, center: Offset, value: Float)
}
