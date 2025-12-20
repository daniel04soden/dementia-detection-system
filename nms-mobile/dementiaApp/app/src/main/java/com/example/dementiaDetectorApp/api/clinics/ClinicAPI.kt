package com.example.dementiaDetectorApp.api.clinics

import com.example.dementiaDetectorApp.models.Clinic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ClinicAPI {

    @GET("patient/clinic")
    suspend fun getClinic(
        @Header("Authorization") token: String,
        @Query("id") id:String
    ): Response<Clinic>

    @GET("clinics/county")
    suspend fun filterByCounty(
        @Query("county") county: String
    ): Response<List<Clinic>>

}