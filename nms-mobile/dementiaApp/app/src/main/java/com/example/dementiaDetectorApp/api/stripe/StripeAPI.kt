package com.example.dementiaDetectorApp.api.stripe

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface StripeAPI {
    @POST("api/mobile/payment")
    suspend fun paymentIntent(
        @Header("Authorization") token: String,
        @Body request: StripeRequest
    ):StripeResponse
}