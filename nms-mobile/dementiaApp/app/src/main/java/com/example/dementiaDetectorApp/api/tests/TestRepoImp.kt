package com.example.dementiaDetectorApp.api.tests

import android.content.SharedPreferences
import android.util.Log
import com.example.dementiaDetectorApp.models.results.RecallRes
import org.json.JSONObject
import retrofit2.HttpException
import java.net.UnknownHostException

class TestRepoImp(
    private val api: TestAPI,
    private val prefs: SharedPreferences
) : TestRepository {

    private val TAG = "TestRepoImp"

    override suspend fun reportStage1(
        testDate: String,
        dateQuestion: String,
        clockNumber: String,
        clockHands: String,
        news: String,
        recall: RecallRes
    ): TestResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return TestResult.Unauthorized()

            val body = JSONObject().apply {
                put("patientID", 1)
                put("doctorID", 1)
                put("testDate", testDate)
                put("dateQuestion", dateQuestion)
                put("clockNumber", clockNumber)
                put("clockHands", clockHands)
                put("news", news)
                put("recall", JSONObject().apply {
                    put("name", recall.name)
                    put("surname", recall.surname)
                    put("number", recall.number)
                    put("street", recall.street)
                    put("city", recall.city)
                })
            }

            val response = api.reportStage1("Bearer $token", body)

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
        memory: Int,
        conversation: Int,
        speaking: Int,
        financial: Int,
        medication: Int,
        transport: Int
    ): TestResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return TestResult.Unauthorized()

            val body = JSONObject().apply {
                put("memory", memory)
                put("conversation", conversation)
                put("speaking", speaking)
                put("financial", financial)
                put("medication", medication)
                put("transport", transport)
            }

            val response = api.reportStage2("Bearer $token", body)

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
