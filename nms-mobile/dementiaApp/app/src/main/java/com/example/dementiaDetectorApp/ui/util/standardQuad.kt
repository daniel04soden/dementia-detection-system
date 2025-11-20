package com.example.dementiaDetectorApp.ui.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

//Used as a mathematical way to draw waves
fun Path.standardQuadFromTo(from: Offset, to: Offset) {
    quadraticTo(
        from.x,
        from.y,
        (from.x + to.x) / 2f,
        (from.y + to.y) / 2f
    )
}