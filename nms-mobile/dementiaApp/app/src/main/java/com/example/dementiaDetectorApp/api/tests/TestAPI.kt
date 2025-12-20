package com.example.dementiaDetectorApp.api.tests

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TestAPI {
    @POST("mobile/stage1/insert")
    suspend fun reportStage1(
        @Header("Authorization") token: String,
        @Body body: Stage1Request
    ): Response<Unit>

    @POST("mobile/stage2/insert")
    suspend fun reportStage2(
        @Header("Authorization") token: String,
        @Body body: Stage2Request
    ): Response<Unit>

    @POST("lifestyle/insert")
    suspend fun reportQuestionnaire(
        @Header("Authorization") token: String,
        @Body body: LifestyleRequest
    ): Response<Unit>

    @GET("patient/results")
    suspend fun getStatus(
        @Header("Authorization") token: String,
        @Query("id") id:String
    ): Response<StatusResponse>
}
