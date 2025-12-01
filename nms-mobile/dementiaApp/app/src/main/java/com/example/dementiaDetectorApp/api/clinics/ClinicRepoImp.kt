package com.example.dementiaDetectorApp.api.clinics

import android.content.SharedPreferences
import android.util.Log
import com.example.dementiaDetectorApp.models.Clinic
import jakarta.inject.Inject
import retrofit2.HttpException

class ClinicRepoImp @Inject constructor(
    private val api: ClinicAPI,
    private val prefs: SharedPreferences
):ClinicRepository{

    override suspend fun getClinic(id: Int): ClinicResult<Clinic> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return ClinicResult.Unauthorized()

            val response = api.getClinic("Bearer $token", ClinicRequest(id).id.toString())

            if (response.isSuccessful && response.body() != null) {
                ClinicResult.Authorized(response.body()!!)
            } else if (response.code() == 401) {
                ClinicResult.Unauthorized()
            } else {
                ClinicResult.UnknownError()
            }
        } catch (e: HttpException) {
            if (e.code() == 401) ClinicResult.Unauthorized()
            else ClinicResult.UnknownError()
        } catch (e: Exception) {
            ClinicResult.UnknownError()
        }
    }

    override suspend fun filterByCounty(county: String): ClinicResult<CountyResponse> {
        return try {
            val token = prefs.getString("jwt", null)
                ?: return ClinicResult.Unauthorized()

            val response = api.filterByCounty("Bearer $token", CountyRequest(county).county)

            if (response.isSuccessful && response.body() != null) {
                ClinicResult.Authorized(response.body()!!)
            } else if (response.code() == 401) {
                ClinicResult.Unauthorized()
            } else {
                ClinicResult.UnknownError()
            }
        } catch (e: HttpException) {
            if (e.code() == 401) ClinicResult.Unauthorized()
            else ClinicResult.UnknownError()
        } catch (e: Exception) {
            ClinicResult.UnknownError()
        }
    }
}
