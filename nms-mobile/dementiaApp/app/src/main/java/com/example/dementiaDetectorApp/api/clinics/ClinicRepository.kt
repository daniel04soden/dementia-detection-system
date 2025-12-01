package com.example.dementiaDetectorApp.api.clinics

import com.example.dementiaDetectorApp.models.Clinic

interface ClinicRepository {
    suspend fun getClinic(id: Int): ClinicResult<Clinic>
    suspend fun filterByCounty(county: String): ClinicResult<CountyResponse>
}