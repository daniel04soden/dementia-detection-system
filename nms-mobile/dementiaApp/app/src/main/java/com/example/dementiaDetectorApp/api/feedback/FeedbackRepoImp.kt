package com.example.dementiaDetectorApp.api.feedback

import android.content.SharedPreferences
import android.util.Log
import jakarta.inject.Inject
import retrofit2.HttpException

class FeedbackRepoImp @Inject constructor(
    private val api: FeedbackAPI,
    private val prefs: SharedPreferences
):FeedbackRepo{
    override suspend fun submitReview(
        id: String,
        score: Int,
        critique: String
    ): FeedbackResult<Unit> {
       return try {
           val token = prefs.getString("jwt", null) ?: run {
               Log.w("FeedbackRepo", "No JWT token")
               return FeedbackResult.Unauthorized()
           }
           api.submitReview("Bearer $token", id, FeedbackRequest(score, critique))
           FeedbackResult.Authorized()
       }catch (e: HttpException) {
           if (e.code() == 401) {
               Log.d("FeedbackRepo", "A 401 error occurred ${e.message()}")
               FeedbackResult.Unauthorized()
           } else {
               Log.d("FeedbackRepo", "Unknown error occurred ${e.message()}")
               FeedbackResult.UnknownError()
           }
       } catch (e: Exception) {
           Log.d("FeedbackRepo", "Unknown error occurred $e")
           FeedbackResult.UnknownError()
       }
    }
}