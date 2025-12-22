package com.example.dementiaDetectorApp.api.news

import android.content.SharedPreferences
import android.util.Log
import com.example.dementiaDetectorApp.models.NewsPiece
import jakarta.inject.Inject
import retrofit2.HttpException

class NewsRepoImp @Inject constructor(
    private val api: NewsAPI,
    private val prefs: SharedPreferences
):NewsRepo{

    override suspend fun getNews(): NewsResult<List<NewsPiece>>{
        return try {
            val token = prefs.getString("jwt", null) ?: run {
                Log.w("NewsRepo", "No JWT token")
                return NewsResult.Unauthorized()
            }

            val response = api.getNews("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                Log.d("NewsRepo", "SUCCESS ${response.body()}")
                NewsResult.Authorized(response.body()!!)
            } else if (response.code() == 401) {
                Log.w("NewsRepo", "Unauthorized (401)")
                NewsResult.Unauthorized()
            } else {
                Log.e("NewsRepo", "Failed HTTP ${response.code()}: ${response.errorBody()?.string()}")
                NewsResult.UnknownError()
            }
        } catch (e: HttpException) {
            Log.e("NewsRepo", "HTTP ${e.code()}: ${e.message()}", e)
            if (e.code() == 401) NewsResult.Unauthorized() else NewsResult.UnknownError()
        } catch (e: Exception) {
            Log.e("NewsRepo", "Unexpected error: ${e.message}", e)
            NewsResult.UnknownError()
        }
    }
}