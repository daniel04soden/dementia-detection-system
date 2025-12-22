package com.example.dementiaDetectorApp.api.stripe

data class MeResponse(
    val patientID: Int,
    val doctorID: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val eircode: String,
    val premium: Boolean
)