package com.example.githatch

import android.app.Application
import com.example.githatch.api.ApiService
import com.example.githatch.api.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var apiService: ApiService
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.d(it)
        }).setLevel(HttpLoggingInterceptor.Level.BASIC)

//NISAM UBACIO LOGGING
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.MAIN_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }
}