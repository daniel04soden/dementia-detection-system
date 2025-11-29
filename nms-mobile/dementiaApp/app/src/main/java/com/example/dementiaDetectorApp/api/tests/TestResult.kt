package com.example.dementiaDetectorApp.api.tests

sealed class TestResult<T>(val data: T? = null) {
    class Success<T>(data: T? = null) : TestResult<T>(data)
    class Unauthorized<T>(data: T? = null) : TestResult<T>(data)
    class UnknownError<T>(data: T? = null) : TestResult<T>(data)
}
