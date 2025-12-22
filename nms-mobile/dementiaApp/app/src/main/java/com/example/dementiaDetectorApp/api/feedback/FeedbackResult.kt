package com.example.dementiaDetectorApp.api.feedback

sealed class FeedbackResult<T>(val data: T? = null){
    class Authorized<T>(data:T?=null): FeedbackResult<T>(data)
    class Unauthorized<T>(data:T?=null): FeedbackResult<T>(data)
    class UnknownError<T>(data:T?=null): FeedbackResult<T>(data)
}