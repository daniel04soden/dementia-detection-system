package com.example.dementiaDetectorApp.api.tests

import android.net.http.UrlRequest.StatusListener


interface TestRepository {
    suspend fun reportStage1(
        patientID: Int,
        dateQuestion: String,
        clockID: Int,
        news: String,
        recallName: String,
        recallSurname: String,
        recallNumber: String,
        recallStreet: String,
        recallCity: String
    ): TestResult<Unit>

    suspend fun reportStage2(
        patientID: Int,
        memoryScore: Int,
        recallRes: Int,
        speakingScore: Int,
        financialScore: Int,
        medicineScore: Int,
        transportScore: Int
    ): TestResult<Unit>

    suspend fun reportQuestionnaire(request: LifestyleRequest): TestResult<Unit>

    suspend fun getStatus(request: StatusRequest): StatusResult<StatusResponse>
}
