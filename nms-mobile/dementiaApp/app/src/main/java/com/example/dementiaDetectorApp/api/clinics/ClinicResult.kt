package com.example.dementiaDetectorApp.api.clinics

sealed class ClinicResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null) : ClinicResult<T>(data)
    class Unauthorized<T>(data: T? = null) : ClinicResult<T>(data)
    class UnknownError<T>(data: T? = null) : ClinicResult<T>(data)
}