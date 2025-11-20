package com.example.dementiaDetectorApp.models

data class Test(
    val name:String,
    val route: String,
    val isComplete: Boolean = false,
    val isGraded: Boolean = false,
    val step: Int
)
