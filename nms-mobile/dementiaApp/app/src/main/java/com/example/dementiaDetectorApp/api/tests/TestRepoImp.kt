package com.example.dementiaDetectorApp.api.tests

import android.content.SharedPreferences
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
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
            val token = prefs.getString("jwt", null) ?: return TestResult.Unauthorized()
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
            val token = prefs.getString("jwt", null) ?: return TestResult.Unauthorized()
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
            val token = prefs.getString("jwt", null) ?: return TestResult.Unauthorized()
            val response = api.reportQuestionnaire("Bearer $token", request)
            if (response.isSuccessful){
                Log.d("Lifestyle", "Submitted successfully")
                TestResult.Success(Unit)
            }
            else{
                Log.d("Lifestyle", "Unknown error")
                TestResult.UnknownError()
            }
        } catch (e: HttpException) {
            if (e.code() == 401){
                Log.d("Lifestyle", "401 error")
                TestResult.Unauthorized()
            } else{
                Log.d("Lifestyle", "Unknown HTTP error")
                TestResult.UnknownError()
            }
        } catch (e: UnknownHostException) {
            Log.d("Lifestyle", "Unknown host exception error")
            TestResult.UnknownError()
        } catch (e: Exception) {
            Log.d("Lifestyle", "Unknown error")
            TestResult.UnknownError()
        }
    }

    override suspend fun getStatus(request: StatusRequest): StatusResult<StatusResponse> {
        return try {
            val token = prefs.getString("jwt", null) ?: return StatusResult.Unauthorized()
            val response = api.getStatus("Bearer $token", request.id.toString())
            if (response.isSuccessful && response.body() != null) StatusResult.Authorized(response.body()!!)
            else if (response.code() == 401) StatusResult.Unauthorized()
            else StatusResult.UnknownError()
        } catch (e: HttpException) {
            if (e.code() == 401) StatusResult.Unauthorized() else StatusResult.UnknownError()
        } catch (e: UnknownHostException) {
            StatusResult.UnknownError()
        } catch (e: Exception) {
            StatusResult.UnknownError()
        }
    }

    override suspend fun uploadAudio(file: File): TestResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: run {
                Log.e(TAG, "JWT token not found")
                return TestResult.Unauthorized()
            }

            Log.d(TAG, "Starting upload for file: ${file.name} (size=${file.length()} bytes)")

            val requestFile = RequestBody.create("audio/wav".toMediaTypeOrNull(), file)
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = try {
                api.uploadAudio("Bearer $token", filePart)
            } catch (e: Exception) {
                Log.e(TAG, "Exception during API call", e)
                throw e
            }

            Log.d(TAG, "Upload HTTP code: ${response.code()}")
            val responseBody = try { response.body()?.toString() } catch (_: Exception) { null }
            val errorBody = try { response.errorBody()?.string() } catch (_: Exception) { null }
            Log.d(TAG, "Upload response body: $responseBody")
            if (errorBody != null) Log.e(TAG, "Upload error body: $errorBody")

            if (response.isSuccessful) {
                Log.d(TAG, "Upload succeeded")
                TestResult.Success(Unit)
            } else {
                Log.e(TAG, "Upload failed with code ${response.code()}")
                TestResult.UnknownError()
            }

        } catch (e: HttpException) {
            Log.e(TAG, "HttpException during upload: ${e.code()} ${e.message()}", e)
            if (e.code() == 401) TestResult.Unauthorized() else TestResult.UnknownError()
        } catch (e: UnknownHostException) {
            Log.e(TAG, "UnknownHostException during upload", e)
            TestResult.UnknownError()
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected exception during upload", e)
            TestResult.UnknownError()
        }
    }

}