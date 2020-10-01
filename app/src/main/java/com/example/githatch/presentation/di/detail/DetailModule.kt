package com.example.githatch.presentation.di.detail

import com.example.githatch.domain.usecase.GetContribsUseCase
import com.example.githatch.presentation.detail.DetailViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class DetailModule {
    @DetailScope
    @Provides
    fun provideDetailViewModelFactory(
        getContribsUseCase: GetContribsUseCase): DetailViewModelFactory {
        return DetailViewModelFactory(getContribsUseCase)
    }
}