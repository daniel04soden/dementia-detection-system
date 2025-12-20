package com.example.dementiaDetectorApp.api.clinics

import android.content.SharedPreferences
import android.util.Log
import com.example.dementiaDetectorApp.models.Clinic
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import retrofit2.HttpException

class ClinicRepoImp @Inject constructor(
    private val api: ClinicAPI,
    private val prefs: SharedPreferences
) : ClinicRepository {

    private companion object {
        const val TAG = "ClinicRepo"
    }

    override suspend fun getClinic(id: Int): ClinicResult<Clinic> {
        return try {
            val token = prefs.getString("jwt", null) ?: run {
                Log.w(TAG, "getClinic($id): No JWT token")
                return ClinicResult.Unauthorized()
            }

            Log.d(TAG, "getClinic($id): Calling API with token: ${token.take(20)}...")
            val response = api.getClinic("Bearer $token", id.toString())

            Log.d(TAG, "getClinic($id): HTTP ${response.code()} body=${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "getClinic($id): SUCCESS ${response.body()}")
                ClinicResult.Authorized(response.body()!!)
            } else if (response.code() == 401) {
                Log.w(TAG, "getClinic($id): Unauthorized (401)")
                ClinicResult.Unauthorized()
            } else {
                Log.e(TAG, "getClinic($id): Failed HTTP ${response.code()}: ${response.errorBody()?.string()}")
                ClinicResult.UnknownError()
            }
        } catch (e: HttpException) {
            Log.e(TAG, "getClinic($id): HTTP ${e.code()}: ${e.message()}", e)
            if (e.code() == 401) ClinicResult.Unauthorized() else ClinicResult.UnknownError()
        } catch (e: Exception) {
            Log.e(TAG, "getClinic($id): Unexpected error: ${e.message}", e)
            ClinicResult.UnknownError()
        }
    }

    override suspend fun filterByCounty(county: String): ClinicResult<List<Clinic>> {
        return try {
            Log.d(TAG, "filterByCounty($county): Calling API")
            val response = api.filterByCounty(county)

            Log.d(TAG, "filterByCounty($county): HTTP ${response.code()} body=${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                ClinicResult.Authorized(response.body()!!)
            } else if (response.code() == 401) {
                Log.w(TAG, "filterByCounty($county): Unauthorized (401)")
                ClinicResult.Unauthorized()
            } else {
                Log.e(TAG, "filterByCounty($county): Failed HTTP ${response.code()}: ${response.errorBody()?.string()}")
                ClinicResult.UnknownError()
            }
        } catch (e: HttpException) {
            Log.e(TAG, "filterByCounty($county): HTTP ${e.code()}: ${e.message()}", e)
            if (e.code() == 401) ClinicResult.Unauthorized() else ClinicResult.UnknownError()
        } catch (e: Exception) {
            Log.e(TAG, "filterByCounty($county): Unexpected error: ${e.message}", e)
            ClinicResult.UnknownError()
        }
    }
}
