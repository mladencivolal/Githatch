package com.example.githatch.presentation.di.owner

import com.example.githatch.domain.usecase.GetAuthorUseCase
import com.example.githatch.domain.usecase.GetReposFromAuthorUseCase
import com.example.githatch.domain.usecase.LoadMoreReposFromAuthor
import com.example.githatch.presentation.di.detail.DetailScope
import com.example.githatch.presentation.owner.OwnerViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class OwnerModule {
    @OwnerScope
    @Provides
    fun provideOwnerViewModelFactory(
        getReposFromAuthorUseCase: GetReposFromAuthorUseCase,
        loadMoreReposFromAuthorUseCase: LoadMoreReposFromAuthor,
        getAuthorUseCase: GetAuthorUseCase
    ): OwnerViewModelFactory {
        return OwnerViewModelFactory(getReposFromAuthorUseCase, loadMoreReposFromAuthorUseCase, getAuthorUseCase)
    }
}