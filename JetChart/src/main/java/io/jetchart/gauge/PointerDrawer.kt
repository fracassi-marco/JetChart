package io.jetchart.gauge

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface PointerDrawer {
    fun draw(drawScope: DrawScope, canvas: Canvas, gaugeDegrees: Int, progressDegrees: Float)
}
