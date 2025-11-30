package com.example.dementiaDetectorApp.api.clinics

import com.example.dementiaDetectorApp.api.tests.TestResult

sealed class ClinicResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null) : ClinicResult<T>(data)
    class Unauthorized<T>(data: T? = null) : ClinicResult<T>(data)
    class UnknownError<T>(data: T? = null) : ClinicResult<T>(data)
}