package com.example.dementiaDetectorApp.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthAPI {

    @POST("mobile/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    )

    @POST("mobile/login")
    suspend fun signIn(
        @Body request: LoginRequest
    ): TokenResponse

    @GET("ValidateJWT")
    suspend fun authenticate(
        @Header("Authorization")token: String
    )
}