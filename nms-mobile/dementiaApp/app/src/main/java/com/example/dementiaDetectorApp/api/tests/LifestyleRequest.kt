package com.example.dementiaDetectorApp.api.tests

data class LifestyleRequest(
    val patientID: Int,
    // S1: General
    val gender: Int,
    val age: Int,
    val dominantHand: Int,
    val familyHistory: Int,

    val education: String,

    // S2: Medical / Measurements
    val weight: Float,
    val bodyTemperature: Float,
    val heartRate: Int,
    val bloodOxygen: Float,
    val apoe4: Int,
    val diabetic: Int,
    val alcoholLevel: Float,
    val bloodPressure: Int,
    val hearingLoss: Int,
    val mriDelay: Float,
    val cognitiveTestScores: Int,
    val medicationHistory: Int,
    val chronicHealthConditions: String,

    // S3: Hobbies / Lifestyle
    val smoked: Int,
    val sleepQuality: Int,
    val depressionStatus: Int,
    val physicalActivity: String,
    val nutritionDiet: String,
    val dementiaStatus: String
)
