package com.example.dementiaDetectorApp.api.stripe

import android.content.SharedPreferences
import android.util.Log
import jakarta.inject.Inject

class StripeRepoImp @Inject constructor(
    private val api: StripeAPI,
    private val prefs: SharedPreferences
):StripeRepo{
    override suspend fun paymentIntent(request: StripeRequest): StripeResponse {
        return try {
            val token = prefs.getString("jwt", null) ?: run {
                Log.w("StripeRepo", "No JWT token found")
                throw Exception("Unauthorized - No JWT token")
            }
            api.paymentIntent("Bearer $token", request)
        } catch (e: Exception) {
            Log.e("StripeRepo", "Payment intent failed: ${e.message}", e)
            throw e
        }
    }

    override suspend fun checkIfPremium(): Boolean {
        val token = prefs.getString("jwt", null)
            ?: throw Exception("Unauthorized")

        return api.getMe("Bearer $token").premium
    }
}