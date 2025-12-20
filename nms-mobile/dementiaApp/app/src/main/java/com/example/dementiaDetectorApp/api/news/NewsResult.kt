package com.example.dementiaDetectorApp.api.news

sealed class NewsResult<T>(val data: T? = null){
    class Authorized<T>(data: T? = null) : NewsResult<T>(data)
    class Unauthorized<T>(data: T? = null) : NewsResult<T>(data)
    class UnknownError<T>(data: T? = null) : NewsResult<T>(data)
}