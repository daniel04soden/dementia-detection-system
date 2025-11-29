package com.example.dementiaDetectorApp.api.tests

data class Stage1Request(
    val patientID: Int,
    val dateQuestion: String,
    val clockID: Int,
    val news: String,
    val recallName: String,
    val recallSurname: String,
    val recallNumber: String,
    val recallStreet: String,
    val recallCity: String
)