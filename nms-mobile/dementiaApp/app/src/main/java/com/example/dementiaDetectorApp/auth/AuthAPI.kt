package com.example.dementiaDetectorApp.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthAPI {

    @POST("RegisterUser")
    suspend fun signUp(
        @Body request: AuthRequest
    )

    @POST("LoginUser")
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse

    @GET("ValidateJWT")
    suspend fun authenticate(
        @Header("Authorization")token: String
    )
}