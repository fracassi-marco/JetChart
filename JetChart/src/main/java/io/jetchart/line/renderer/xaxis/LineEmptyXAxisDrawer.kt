package io.jetchart.line.renderer.xaxis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.jetchart.line.renderer.xaxis.XAxisDrawer

class LineEmptyXAxisDrawer : XAxisDrawer {
    override fun drawAxisLabels(drawScope: DrawScope, canvas: Canvas, drawableArea: Rect, labels: List<String>) {
    }

    override fun drawAxisLine(drawScope: DrawScope, canvas: Canvas, drawableArea: Rect) {
    }

    override fun requiredHeight(drawScope: DrawScope) = 0f
}