package com.example.dementiaDetectorApp.api.tests

import android.content.SharedPreferences
import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException
import java.net.UnknownHostException

class TestRepoImp(
    private val api: TestAPI,
    private val prefs: SharedPreferences
) : TestRepository {

    private val TAG = "TestRepoImp"

    override suspend fun reportStage1(
        patientID: Int,
        dateQuestion: String,
        clockID: Int,
        news: String,
        recallName: String,
        recallSurname: String,
        recallNumber: String,
        recallStreet: String,
        recallCity: String
    ): TestResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return TestResult.Unauthorized()

            val response = api.reportStage1("Bearer $token",
                body = Stage1Request(
                    patientID = patientID,
                    dateQuestion = dateQuestion,
                    clockID = clockID,
                    news = news,
                    recallName = recallName,
                    recallSurname = recallSurname,
                    recallNumber = recallNumber,
                    recallStreet = recallStreet,
                    recallCity =recallCity
                )
            )

            if (response.isSuccessful) {
                Log.d(TAG, "Stage 1 submitted successfully")
                TestResult.Success(Unit)
            } else {
                Log.d(
                    TAG,
                    "Stage 1 server error ${response.code()}: ${response.errorBody()?.string()}"
                )
                TestResult.UnknownError()
            }
        } catch (e: HttpException) {
            when {
                e.code() == 401 -> {
                    Log.d(TAG, "Stage 1 401 occurred: ${e.message()}")
                    TestResult.Unauthorized()
                }

                else -> {
                    Log.d(TAG, "Stage 1 HTTP error occurred: ${e.message()}")
                    TestResult.UnknownError()
                }
            }
        } catch (e: UnknownHostException) {
            Log.d(TAG, "Stage 1 network error: ${e.message}")
            TestResult.UnknownError()
        } catch (e: Exception) {
            Log.d(TAG, "Stage 1 unknown error: $e")
            TestResult.UnknownError()
        }
    }

    override suspend fun reportStage2(
        patientID: Int,
        memoryScore: Int,
        recallRes: Int,
        speakingScore: Int,
        financialScore: Int,
        medicineScore: Int,
        transportScore: Int
    ): TestResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return TestResult.Unauthorized()

            val response = api.reportStage2("Bearer $token",
                body = Stage2Request(
                    patientID = patientID,
                    memoryScore = memoryScore,
                    recallRes = recallRes,
                    speakingScore = speakingScore,
                    financialScore = financialScore,
                    medicineScore = medicineScore,
                    transportScore = transportScore
                )
            )

            if (response.isSuccessful) {
                Log.d(TAG, "Stage 1 submitted successfully")
                TestResult.Success(Unit)
            } else {
                Log.d(
                    TAG,
                    "Stage 1 server error ${response.code()}: ${response.errorBody()?.string()}"
                )
                TestResult.UnknownError()
            }
        } catch (e: HttpException) {
            when {
                e.code() == 401 -> {
                    Log.d(TAG, "Stage 1 401 occurred: ${e.message()}")
                    TestResult.Unauthorized()
                }

                else -> {
                    Log.d(TAG, "Stage 1 HTTP error occurred: ${e.message()}")
                    TestResult.UnknownError()
                }
            }
        } catch (e: UnknownHostException) {
            Log.d(TAG, "Stage 1 network error: ${e.message}")
            TestResult.UnknownError()
        } catch (e: Exception) {
            Log.d(TAG, "Stage 1 unknown error: $e")
            TestResult.UnknownError()
        }
    }

    override suspend fun reportQuestionnaire(
        patientID: Int,
        gender: Int,
        age: Int,
        dHand: Int,
        weight: Float,
        avgTemp: Float,
        restingHR: Int,
        oxLv: Int,
        history: Boolean,
        smoke: Boolean,
        apoe: Boolean,
        activityLv: String,
        depressed: Boolean,
        diet: String,
        goodSleep: Boolean,
        edu: String
    ): TestResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return TestResult.Unauthorized()

            val response = api.reportQuestionnaire(
                "Bearer $token",
                LifestyleRequest(
                    patientID = patientID,
                    gender = gender,
                    age = age,
                    dHand = dHand,
                    weight = weight,
                    avgTemp = avgTemp,
                    restingHR = restingHR,
                    oxLv = oxLv,
                    history = history,
                    smoke = smoke,
                    apoe = apoe,
                    activityLv = activityLv,
                    depressed = depressed,
                    diet = diet,
                    goodSleep = goodSleep,
                    edu = edu
                )
            )
            if (response.isSuccessful) {
                Log.d(TAG, "Questionnaire submitted successfully")
                TestResult.Success(Unit)
            } else {
                Log.d(
                    TAG,
                    "Questionnaire server error ${response.code()}: ${response.errorBody()?.string()}"
                )
                TestResult.UnknownError()
            }
        } catch (e: HttpException) {
            when {
                e.code() == 401 -> {
                    Log.d(TAG, "Questionnaire 401 occurred: ${e.message()}")
                    TestResult.Unauthorized()
                }

                else -> {
                    Log.d(TAG, "Questionnaire HTTP error occurred: ${e.message()}")
                    TestResult.UnknownError()
                }
            }
        } catch (e: UnknownHostException) {
            Log.d(TAG, "Questionnaire network error: ${e.message}")
            TestResult.UnknownError()
        } catch (e: Exception) {
            Log.d(TAG, "Questionnaire unknown error: $e")
            TestResult.UnknownError()
        }
    }
}
