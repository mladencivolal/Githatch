package com.example.githatch.api.model

import com.example.githatch.api.ApiService
import com.example.githatch.api.Constants
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


object RetrofitFactory {


    var client = OkHttpClient()




    fun makeRetrofitService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.MAIN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(ApiService::class.java)
    }
}

