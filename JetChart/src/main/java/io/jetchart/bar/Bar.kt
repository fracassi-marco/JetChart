package io.jetchart.bar

import androidx.compose.ui.graphics.Color

data class Bar(
    val value: Float,
    val label: String,
    val color: Color = Color(0xFF3ddc84),
    val onTap: (Bar) -> Unit = { }
)