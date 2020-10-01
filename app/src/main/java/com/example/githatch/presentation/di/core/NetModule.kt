package com.example.githatch.presentation.di.core

import com.example.githatch.data.api.GitHubService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule(private val baseUrl: String) {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun provideGitHubService(retrofit: Retrofit): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }
}