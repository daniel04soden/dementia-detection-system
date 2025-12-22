package com.example.dementiaDetectorApp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.dementiaDetectorApp.api.auth.AuthAPI
import com.example.dementiaDetectorApp.api.auth.AuthRepoImp
import com.example.dementiaDetectorApp.api.auth.AuthRepository
import com.example.dementiaDetectorApp.api.clinics.ClinicAPI
import com.example.dementiaDetectorApp.api.clinics.ClinicRepoImp
import com.example.dementiaDetectorApp.api.clinics.ClinicRepository
import com.example.dementiaDetectorApp.api.feedback.FeedbackAPI
import com.example.dementiaDetectorApp.api.feedback.FeedbackRepo
import com.example.dementiaDetectorApp.api.feedback.FeedbackRepoImp
import com.example.dementiaDetectorApp.api.news.NewsAPI
import com.example.dementiaDetectorApp.api.news.NewsRepo
import com.example.dementiaDetectorApp.api.news.NewsRepoImp
import com.example.dementiaDetectorApp.api.stripe.StripeAPI
import com.example.dementiaDetectorApp.api.stripe.StripeRepo
import com.example.dementiaDetectorApp.api.stripe.StripeRepoImp
import com.example.dementiaDetectorApp.api.tests.TestAPI
import com.example.dementiaDetectorApp.api.tests.TestRepoImp
import com.example.dementiaDetectorApp.api.tests.TestRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    val BASE_URL = "https://magestle.dev/api/"
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provAuthAPI(): AuthAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provAuthRepo(api: AuthAPI, prefs: SharedPreferences): AuthRepository {
        return AuthRepoImp(api, prefs)
    }

    @Provides
    @Singleton
    fun provTestAPI(): TestAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provTestRepo(api: TestAPI, prefs: SharedPreferences): TestRepository {
        return TestRepoImp(api, prefs)
    }

    @Provides
    @Singleton
    fun provClinicAPI(): ClinicAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ClinicAPI::class.java)
    }

    @Provides
    @Singleton
    fun provClinicRepo(api: ClinicAPI, prefs: SharedPreferences): ClinicRepository {
        return ClinicRepoImp(api, prefs)
    }

    @Provides
    @Singleton
    fun provNewsAPI(): NewsAPI{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NewsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provNewsRepo(api: NewsAPI, prefs: SharedPreferences): NewsRepo{
        return NewsRepoImp(api, prefs)
    }

    @Provides
    @Singleton
    fun provFeedbackAPI(): FeedbackAPI{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(FeedbackAPI::class.java)
    }

    @Provides
    @Singleton
    fun provFeedbackRepo(api:FeedbackAPI, prefs: SharedPreferences): FeedbackRepo{
        return FeedbackRepoImp(api, prefs)
    }

    @Provides
    @Singleton
    fun provStripeAPI(): StripeAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(StripeAPI::class.java)
    }

    @Provides
    @Singleton
    fun provStripeRepo(api: StripeAPI, prefs: SharedPreferences): StripeRepo {
        return StripeRepoImp(api, prefs)
    }
}