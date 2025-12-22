package com.example.dementiaDetectorApp.api.news

import com.example.dementiaDetectorApp.models.NewsPiece

interface NewsRepo {
    suspend fun getNews(): NewsResult<List<NewsPiece>>
}