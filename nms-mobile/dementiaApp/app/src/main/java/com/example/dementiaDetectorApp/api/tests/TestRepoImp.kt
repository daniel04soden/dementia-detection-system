package com.example.dementiaDetectorApp.api.tests

import android.content.SharedPreferences
import android.util.Log
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

            val response = api.reportStage1(
                "Bearer $token",
                Stage1Request(
                    patientID = patientID,
                    dateQuestion = dateQuestion,
                    clockID = clockID,
                    news = news,
                    recallName = recallName,
                    recallSurname = recallSurname,
                    recallNumber = recallNumber,
                    recallStreet = recallStreet,
                    recallCity = recallCity
                )
            )

            if (response.isSuccessful) TestResult.Success(Unit)
            else TestResult.UnknownError()

        } catch (e: HttpException) {
            if (e.code() == 401) TestResult.Unauthorized() else TestResult.UnknownError()
        } catch (e: UnknownHostException) {
            TestResult.UnknownError()
        } catch (e: Exception) {
            TestResult.UnknownError()
        }
    }

    override suspend fun reportStage2(
        patientID: Int,
        memoryScore: Int,
        recallScore: Int,
        speakingScore: Int,
        financialScore: Int,
        medicineScore: Int,
        transportScore: Int
    ): TestResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return TestResult.Unauthorized()

            val response = api.reportStage2(
                "Bearer $token",
                Stage2Request(
                    patientID = patientID,
                    memoryScore = memoryScore,
                    recallScore = recallScore,
                    speakingScore = speakingScore,
                    financialScore = financialScore,
                    medicineScore = medicineScore,
                    transportScore = transportScore
                )
            )

            if (response.isSuccessful) TestResult.Success(Unit)
            else TestResult.UnknownError()

        } catch (e: HttpException) {
            if (e.code() == 401) TestResult.Unauthorized() else TestResult.UnknownError()
        } catch (e: UnknownHostException) {
            TestResult.UnknownError()
        } catch (e: Exception) {
            TestResult.UnknownError()
        }
    }

    override suspend fun reportQuestionnaire(request: LifestyleRequest): TestResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return TestResult.Unauthorized()

            val response = api.reportQuestionnaire(
                "Bearer $token",
                request
            )

            if (response.isSuccessful) TestResult.Success(Unit)
            else TestResult.UnknownError()

        } catch (e: HttpException) {
            if (e.code() == 401) TestResult.Unauthorized() else TestResult.UnknownError()
        } catch (e: UnknownHostException) {
            TestResult.UnknownError()
        } catch (e: Exception) {
            TestResult.UnknownError()
        }
    }

    override suspend fun getStatus(request: StatusRequest): StatusResult<StatusResponse> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return StatusResult.Unauthorized()

            val response = api.getStatus("Bearer $token", request.id.toString())

            if (response.isSuccessful && response.body() != null) {
                Log.d("Status Call", "Success")
                StatusResult.Authorized(response.body()!!)
            } else if (response.code() == 401) {
                Log.d("Status Call", "401 fail")
                StatusResult.Unauthorized()
            } else {
                Log.d("Status Call", "unknown fail")
                StatusResult.UnknownError()
            }
        } catch (e: HttpException) {
            if (e.code() == 401){
                Log.d("Status Call", "401 fail")
                StatusResult.Unauthorized()
            } else {
                Log.d("Status Call", "Unknown fail")
                StatusResult.UnknownError()
            }
        } catch (e: UnknownHostException) {
            Log.d("Status Call", "Unknown host exception")
            StatusResult.UnknownError()
        } catch (e: Exception) {
            Log.d("Status Call", "${e.message}")
            StatusResult.UnknownError()
        }
    }
}
