package com.example.dementiaDetectorApp.api.tests

import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TestAPI {
    @POST("mobile/stage1")
    suspend fun reportStage1(
        @Header("Authorization") token: String,
        @Body body: Stage1Request
    ): Response<Unit>

    @POST("mobile/stage2")
    suspend fun reportStage2(
        @Header("Authorization") token: String,
        @Body body: Stage2Request
    ): Response<Unit>

    @POST("mobile/lifestyle")
    suspend fun reportQuestionnaire(
        @Header("Authorization") token: String,
        @Body body: LifestyleRequest
    ): Response<Unit>
}
