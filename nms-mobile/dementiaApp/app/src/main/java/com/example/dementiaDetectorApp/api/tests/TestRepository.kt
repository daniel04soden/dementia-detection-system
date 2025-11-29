package com.example.dementiaDetectorApp.api.tests


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

    suspend fun reportQuestionnaire(
        patientID: Int,
        gender: Int,
        age: Int,
        dHand: Int,
        weight:Float,
        avgTemp:Float,
        restingHR:Int,
        oxLv: Int,
        history: Boolean,
        smoke: Boolean,
        apoe: Boolean,
        activityLv: String,
        depressed:Boolean,
        diet: String,
        goodSleep: Boolean,
        edu: String
    ): TestResult<Unit>
}
