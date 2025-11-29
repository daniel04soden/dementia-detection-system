package com.example.dementiaDetectorApp.api.tests

import com.example.dementiaDetectorApp.models.results.RecallRes

interface TestRepository {
    suspend fun reportStage1(
        testDate: String,
        dateQuestion: String,
        clockNumber: String,
        clockHands: String,
        news: String,
        recall: RecallRes
    ): TestResult<Unit>

    suspend fun reportStage2(
        memory: Int,
        conversation: Int,
        speaking: Int,
        financial: Int,
        medication: Int,
        transport: Int
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
