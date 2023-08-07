package io.jetchart.gauge

import androidx.compose.ui.graphics.drawscope.DrawScope

interface ArcDrawer {
    fun draw(drawScope: DrawScope, startAngle: Float, gaugeDegrees: Int)
}
