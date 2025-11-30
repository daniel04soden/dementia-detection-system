package com.example.dementiaDetectorApp.api.auth

data class SignUpRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val eircode: String,
    val clinicID: Int
)
