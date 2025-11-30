package com.example.dementiaDetectorApp.api.tests

data class LifestyleRequest(
    val patientID: Int,
    val gender: Int,
    val age: Int,
    val dominantHand: Int,
    val weight: Float,
    val bodyTemperature: Float,
    val heartRate: Int,
    val bloodOxygen: Float,
    val familyHistory: Int,
    val smoked: Int,
    val apoeE4: Int,
    val physicalActivity: String,
    val depressionStatus: Int,
    val nutritionDiet: String,
    val sleepQuality: Int,
    val cumulativePrimary: String,
    val cumulativeSecondary: String,
    val cumulativeDegree: String
)
