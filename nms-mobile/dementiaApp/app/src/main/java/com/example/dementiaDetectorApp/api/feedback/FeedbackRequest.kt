package com.example.dementiaDetectorApp.api.feedback

data class FeedbackRequest(
    val score: Int,
    val critique: String
)