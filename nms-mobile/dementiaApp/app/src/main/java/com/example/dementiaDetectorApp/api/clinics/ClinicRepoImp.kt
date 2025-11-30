package com.example.dementiaDetectorApp.api.clinics

import android.content.SharedPreferences
import android.util.Log
import com.example.dementiaDetectorApp.api.auth.AuthResult
import com.example.dementiaDetectorApp.api.tests.TestResult
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

           val response = api.getClinic("Bearer $token", request = ClinicRequest(id))
           ClinicResult.Authorized(response)
       }catch (e: HttpException) {
           if (e.code() == 401) {
               Log.d("AuthRepImp", "singIn 401 occurred ${e.message()}")
               ClinicResult.Unauthorized()
           } else {
               Log.d("AuthRepImp", "singIn Http Exception error occurred ${e.message()}")
               ClinicResult.UnknownError()
           }
       } catch (e: Exception) {
           Log.d("AuthRepImp", "UnknownHostException: ${e.message}")
           ClinicResult.UnknownError()
       }
    }

    override suspend fun filterByCounty(county: String): ClinicResult<List<Clinic>> {
       return try{
           val token = prefs.getString("jwt", null)
               ?: return ClinicResult.Unauthorized()

           val response = api.filterByCounty("Bearer $token", request = CountyRequest(county))
           ClinicResult.Authorized(response.list)
       }catch (e: HttpException) {
           if (e.code() == 401) {
               Log.d("AuthRepImp", "singIn 401 occurred ${e.message()}")
               ClinicResult.Unauthorized()
           } else {
               Log.d("AuthRepImp", "singIn Http Exception error occurred ${e.message()}")
               ClinicResult.UnknownError()
           }
       } catch (e: Exception) {
           Log.d("AuthRepImp", "UnknownHostException: ${e.message}")
           ClinicResult.UnknownError()
       }
    }
}