package com.example.dementiaDetectorApp.api.stripe


interface StripeRepo {
    suspend fun paymentIntent(request: StripeRequest): StripeResponse
    suspend fun checkIfPremium(): Boolean
}