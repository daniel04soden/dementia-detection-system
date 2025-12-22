package com.example.dementiaDetectorApp

import android.app.Application
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AuthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //Init stripe with dev API key
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51I0ZIKCuCdDliuBqNBdF8OcpGAX5UjLtlPxLcmzeqOtRbV8OiaJSn8dOnuee3gBlms6QBg6oGI6TvhjGGV3ghuiV00BHMtWFxE"
        )
    }
}