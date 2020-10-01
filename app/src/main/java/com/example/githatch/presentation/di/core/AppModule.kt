package com.example.githatch.presentation.di.core

import android.content.Context
import com.example.githatch.presentation.di.detail.DetailSubComponent
import com.example.githatch.presentation.di.owner.OwnerSubComponent
import com.example.githatch.presentation.di.repo.RepoSubComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    subcomponents = [
        RepoSubComponent::class,
        DetailSubComponent::class,
        OwnerSubComponent::class
    ]
)
class AppModule(private val context: Context) {
    @Singleton
    @Provides
    fun provideApplicationContext(): Context = context.applicationContext
}