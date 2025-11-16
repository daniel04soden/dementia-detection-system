package com.example.dementiaDetectorApp.di
import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.dementiaDetectorApp.auth.AuthAPI
import com.example.dementiaDetectorApp.auth.AuthRepoImp
import com.example.dementiaDetectorApp.auth.AuthRepository
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
    @Provides
    @Singleton
    fun provAuthAPI(): AuthAPI{
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory()) // This enables Kotlin data class support for (de)serialization
            .build()

        return Retrofit.Builder()
            //.baseUrl("http://192.168.1.10:8080/")
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provSharedPref(app: Application): SharedPreferences{
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provAuthRepo(api: AuthAPI, prefs: SharedPreferences): AuthRepository{
        return AuthRepoImp(api, prefs)
    }
}