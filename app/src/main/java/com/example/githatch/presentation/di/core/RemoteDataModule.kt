package com.example.githatch.presentation.di.core

import com.example.githatch.data.api.GitHubService
import com.example.githatch.data.repository.detail.dataSource.DetailRemoteDataSource
import com.example.githatch.data.repository.detail.dataSourceImpl.DetailRemoteDataSourceImpl
import com.example.githatch.data.repository.owner.dataSource.OwnerRemoteDataSource
import com.example.githatch.data.repository.owner.dataSourceImpl.OwnerRemoteDataSourceImpl
import com.example.githatch.data.repository.repo.dataSource.RepoRemoteDataSource
import com.example.githatch.data.repository.repo.dataSourceImpl.RepoRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteDataModule {
    @Singleton
    @Provides
    fun provideRepoRemoteDataSource(gitHubService: GitHubService): RepoRemoteDataSource {
        return RepoRemoteDataSourceImpl(gitHubService)
    }

    @Singleton
    @Provides
    fun provideDetailRemoteDataSource(gitHubService: GitHubService): DetailRemoteDataSource {
        return DetailRemoteDataSourceImpl(gitHubService)
    }

    @Singleton
    @Provides
    fun provideOwnerRemoteDataSource(gitHubService: GitHubService): OwnerRemoteDataSource {
        return OwnerRemoteDataSourceImpl(gitHubService)
    }
}