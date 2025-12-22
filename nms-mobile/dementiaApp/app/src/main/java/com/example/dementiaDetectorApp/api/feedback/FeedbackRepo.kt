package com.example.dementiaDetectorApp.api.feedback

interface FeedbackRepo {
    suspend fun submitReview(id:String, score:Int, critique:String):FeedbackResult<Unit>
}