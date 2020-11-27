package com.example.githatch.presentation.di.core

import com.example.githatch.domain.repository.DetailRepository
import com.example.githatch.domain.repository.OwnerRepository
import com.example.githatch.domain.repository.RepoRepository
import com.example.githatch.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Singleton
    @Provides
    fun provideGetReposUseCase(repoRepository: RepoRepository): GetReposUseCase = GetReposUseCase(repoRepository)

    @Singleton
    @Provides
    fun provideLoadMoreReposUseCase(repoRepository: RepoRepository): LoadMoreReposUseCase = LoadMoreReposUseCase(repoRepository)

    @Singleton
    @Provides
    fun provideGetContribsUseCase(detailRepository: DetailRepository): GetContribsUseCase = GetContribsUseCase(detailRepository)

    @Singleton
    @Provides
    fun provideGetReposFromAuthorUseCase(ownerRepository: OwnerRepository): GetReposFromAuthorUseCase = GetReposFromAuthorUseCase(ownerRepository)

    @Singleton
    @Provides
    fun provideLoadMoreReposFromAuthorUseCase(ownerRepository: OwnerRepository): LoadMoreReposFromAuthorUseCase = LoadMoreReposFromAuthorUseCase(ownerRepository)

    @Singleton
    @Provides
    fun provideGetAuthorUseCase(ownerRepository: OwnerRepository): GetAuthorUseCase = GetAuthorUseCase(ownerRepository)
}