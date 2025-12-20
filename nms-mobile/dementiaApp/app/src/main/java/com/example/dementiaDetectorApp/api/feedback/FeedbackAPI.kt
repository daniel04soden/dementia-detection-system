package com.example.dementiaDetectorApp.api.feedback

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface FeedbackAPI {
    @POST("review/insert")
    suspend fun submitReview(
        @Header("Authorization")token: String,
        @Query("id")id:String,
        @Body request:FeedbackRequest
    )
}