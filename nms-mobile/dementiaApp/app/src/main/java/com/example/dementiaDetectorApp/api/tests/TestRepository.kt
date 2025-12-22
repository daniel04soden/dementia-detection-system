package com.example.dementiaDetectorApp.api.tests

import java.io.File

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
        recallScore: Int,
        speakingScore: Int,
        financialScore: Int,
        medicineScore: Int,
        transportScore: Int
    ): TestResult<Unit>

    suspend fun reportQuestionnaire(request: LifestyleRequest): TestResult<Unit>

    suspend fun getStatus(request: StatusRequest): StatusResult<StatusResponse>

    suspend fun uploadAudio(file: File): TestResult<Unit>

    suspend fun sendToAI():TestResult<Unit>
}