package com.example.dementiaDetectorApp.api.news

import com.example.dementiaDetectorApp.models.NewsPiece
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface NewsAPI {
    @GET("mobile/news")
    suspend fun getNews(
        @Header("Authorization")token: String
    ): Response<List<NewsPiece>>
}