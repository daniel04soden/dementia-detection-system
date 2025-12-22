package com.example.dementiaDetectorApp.api.tests

data class Stage2Request(
    val patientID: Int,
    val memoryScore: Int,
    val recallScore: Int,
    val speakingScore: Int,
    val financialScore: Int,
    val medicineScore: Int,
    val transportScore: Int
)