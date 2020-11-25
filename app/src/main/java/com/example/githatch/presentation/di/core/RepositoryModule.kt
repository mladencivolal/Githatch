package com.example.githatch.presentation.di.core

import com.example.githatch.data.api.GitHubService
import com.example.githatch.data.repository.detail.dataSourceImpl.DetailRepositoryImpl
import com.example.githatch.data.repository.owner.OwnerRepositoryImpl
import com.example.githatch.data.repository.repo.RepoRepositoryImpl
import com.example.githatch.domain.repository.DetailRepository
import com.example.githatch.domain.repository.OwnerRepository
import com.example.githatch.domain.repository.RepoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideRepoRepository(gitHubService: GitHubService): RepoRepository =
        RepoRepositoryImpl(gitHubService)

    @Singleton
    @Provides
    fun provideDetailRepository(gitHubService: GitHubService): DetailRepository =
        DetailRepositoryImpl(gitHubService)

    @Singleton
    @Provides
    fun provideOwnerRepository(gitHubService: GitHubService): OwnerRepository =
        OwnerRepositoryImpl(gitHubService)
}