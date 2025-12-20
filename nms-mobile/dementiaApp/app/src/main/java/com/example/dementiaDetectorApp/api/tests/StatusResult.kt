package com.example.dementiaDetectorApp.api.tests

import com.example.dementiaDetectorApp.api.clinics.ClinicResult

sealed class StatusResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null) : StatusResult<T>(data)
    class Unauthorized<T>(data: T? = null) : StatusResult<T>(data)
    class UnknownError<T>(data: T? = null) : StatusResult<T>(data)
}