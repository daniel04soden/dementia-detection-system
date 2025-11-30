package com.example.dementiaDetectorApp.api.clinics

import com.example.dementiaDetectorApp.models.Clinic
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import kotlin.reflect.jvm.internal.impl.descriptors.ConstUtil

interface ClinicAPI {

    @POST("clinic")
    suspend fun getClinic(
        @Header("Authorization") token: String,
        @Body request: ClinicRequest
    ): Clinic

    @POST("clinics/county")
    suspend fun filterByCounty(
        @Header("Authorization") token: String,
        @Body request: CountyRequest
    ):CountyResponse
}