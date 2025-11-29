package com.example.dementiaDetectorApp.api.tests

data class LifestyleRequest(
    val patientID: Int = 1,
    val gender: Int,
    val age: Int,
    val dHand: Int,
    val weight:Float,
    val avgTemp:Float,
    val restingHR:Int,
    val oxLv: Int,
    val history: Boolean,
    val smoke: Boolean,
    val apoe: Boolean,
    val activityLv: String,
    val depressed:Boolean,
    val diet: String,
    val goodSleep: Boolean,
    val edu: String
)